- 可以认为是非常简化版的Spring WebFlux

# 设计图(简版)
- 数据流转:
    - ![数据流转](./imgs/data-flow.png)
    
- 相关内部概念
  - @Plugin=@Bean
  - @ActivePlugin=@Component ...
  - @AutoPlugin=@Autowired
  - @ActiveConfiguration=@Configuration
  - extension 约等于 starter (可插拔,按需加载)

- http模块总的来说基于reactor-cmd 模型
    - ![imgs](../imgs/logic.png)

# How to Use
- 便携式 pipeline : 只需要 @Manager和@ManagerNode 两个注解,即可组合n个pipeline(内部逻辑采用的是reactor-core)
- **注意**
  - command中不支持@Autowired, Reactor中支持,可以通过getReactor 然后get class 获取
  - 不支持启动类中 ,添加 @Plugin 注解的bean,因为我不想去改Spring启动类的逻辑,并且,实际上启动类也只会只是短短的一行代码,所以也没这个必要
    - ```
      如: 
      @CellSpringHttpApplication
      public class App{
          @Plugin
          public CC2 cc2()
          {
              return new CC2();
          }
      }
      这个cc2 并不会生效 ,但是对于非启动类的时候, cc2 可以生效,
      如果想在启动类上添加 bean,套娃套一层即可
       		@ActivePlugin
          public static class CC2
          {
              @Plugin
              public CC3 cc3()
              {
                  return new CC3();
              }
          }
      ```

- 前提:

  - 添加依赖:

    - ```
       <dependency>
           <groupId>com.cell</groupId>
           <artifactId>cell-http-extension</artifactId>
           <version>${cell.version}</version>
       </dependency>
      ```

    - 启动类上加上 @CellSpringHttpApplication 注解

    - cmd和reactor 都不可以为私有内部类
    
- [demo](https://github.com/ItsFunny/cell/tree/dev/cell-demo/cell-demo-http-demo3/src/main/java/com/cell)

- 第一种方式


  - ```
    CellApplication.builder()
                    .newReactor()
                    .withGroup("/demo")
                    .post("/post", (wp) ->
                    {
                        wp.success("post");
                    }).make().get("/get", (wp) ->
            {
                wp.success("get");
            }).make().done().build().start(App.class, args); 				
    ```

  -  就会有2个接口:   

    - post: /demo/post
    - get: /demo/gete

  - 创建n个接口:

    - ```
      				CellApplication.CellApplicationBuilder builder = CellApplication.builder();
              CellApplication.ReactorBuilder reactorBuilder = builder.newReactor();
              reactorBuilder.withGroup("/demo");
              for (int i = 0; i < 100; i++)
              {
                  final Integer ret = i;
                  reactorBuilder.post("/post" + i, (wp) ->
                  {
                      wp.success(ret);
                  });
              }
              reactorBuilder.done().build().start(App.class, args);
      ```

  - 如何与其他bean关联

    - 通过withBean:

      - ```
         CellApplication.builder()
                        .newReactor()
                        .withBean(CC1.class)
                        .withBean(CC2.class)
                        .withGroup("/demo")
                        .post("/cc1", (wp) ->
                        {
                            IMapDynamicHttpReactor reactor = wp.getReactor();
                            CC1 cc1 = (CC1) reactor.getDependency(CC1.class);
                            Assert.notNull(cc1, "bean cc1不可为空");
                            wp.success("post");
                        }).make().get("/get", (wp) ->
                {
                    wp.success("get");
                }).make().done().build().start(App.class, args);
             实际的 command 就可以 reactor.getDependency(CC1.class); ,获取得到bean
        ```
      - 

- 第二种方式
  - reactor 通过继承 AbstractHttpDymanicCommandReactor+ReactorAnno注解,cmd 通过继承 AbstractHttpCommand+HttpCmdAnno注解 实现, 

  - ```
    @CellSpringHttpApplication
    public class App
    {
        @HttpCmdAnno(uri = "/cmd1", httpCommandId = 1,reactor=ReactorAnno.class)
        public static class CMD1 extends AbstractHttpCommand
        {
            @Override
            protected void onExecute(IHttpContext ctx, Object bo) throws IOException
            {
                ctx.response(this.createResponseWp().ret("cmd1").build());
            }
        }
    
        @HttpCmdAnno(uri = "/cmd2", httpCommandId = 1,reactor=ReactorAnno.class)
        public static class CMD2 extends AbstractHttpCommand
        {
            @Override
            protected void onExecute(IHttpContext ctx, Object bo) throws IOException
            {
                Reactor2 reactor2 = (Reactor2) ctx.getHttpReactor();
                Assert.notNull(reactor2.autoasd, "asd");
                ctx.response(this.createResponseWp().ret("cmd2").build());
            }
        }
    
        public static class Autoasd
        {
    
        }
        @Bean
        public Autoasd asd()
        {
            return new Autoasd();
        }
    
        @ReactorAnno(group = "/demo")
        public static class Reactor2 extends AbstractHttpDymanicCommandReactor
        {
            @AutoPlugin
            private Autoasd autoasd;
        }
    
        public static void main(String[] args)
        {
            CellApplication.run(App.class, args);
        }
    }
    ```
- 当然,也可以混合
  - [demo](https://github.com/ItsFunny/cell/tree/dev/cell-demo/cell-demo-http-demo3/src/main/java/com/cell)

---

# TODO
- 同种类型的bean,不支持,注册多个(既@Autowired/@AutoPlugin的时候,会报发现多个bean)
    - 解决方法: 推荐注册的时候,还是使用@Bean 来注入
- 返回值还需要定制化,以及modelAndView模式未测试
- LOG模块有问题,第三方的日志没打印出来
- 网关模块,dao模块和discovery模块都会在后续添加
- 一些插件补全
- benchmark测试
- 还有一大堆.......