# config
- http 默认8000
- rpc 默认7000
- gateway 默认9999
- prometheus-servicediscvoery 默认6666
# feature
- 统一日志管理
    - 日志采取一个日志,统一格式打印: 如 LOG.info(ModuleInterface ,xxxx)
    - 日志归档: 只需要打开开关(目前默认为关闭,且还没加代码),打开之后,日志根据类型,将统一类型的日志放到同一个文件中,根据
        日志级别也会划分
    - 性能: 未测试
- 统一配置文件管理
    - 支持热刷
        - 
            ```.env
            Configuration.getDefault().getAndMonitorConfig
            结合 git 即可实现服务之间的配置自动同步并且热更新
            ```
    - 模块化: 
        - 根据不同的module 自定义配置,如 

# TODO
- [x] http
- [x] gateway
- [x] prometheus
- rpc
- event
- db

- 相关内部概念
  - @Plugin=@Bean
  - @ActivePlugin=@Component ...
  - @AutoPlugin=@Autowired
  - @ActiveConfiguration=@Configuration
  - extension 约等于 starter (可插拔,按需加载)

- http模块总的来说基于reactor-cmd 模型
    - ![imgs](../imgs/logic.png)
    

# 使用方法

- 前提

  - 添加repository

    ```
     <repository>
                <id>itsfunny.github.io</id>
                <name>charlie-cell</name>
                <url>https://itsfunny.github.io/maven_repository/maven-repo/</url>
    </repository>
    ```

- 注意
  - reactor 是会作为bean 注入到spring中,而cmd 不会

## http

- 相关命令行
  - -port 8080  默认http 端口为8080

- 1. 添加依赖

     ```
      <dependency>
     			<groupId>com.cell</groupId>
     			<artifactId>cell-http-extension</artifactId>
     			<version>${cell.version}</version>
     </dependency>
     ```

  2. 编写reactor

     - ReactorAnno
     - 继承AbstractHttpDymanicCommandReactor

     ```
     @ReactorAnno(group = "/reactor1")
     public static class Reactor1 extends AbstractHttpDymanicCommandReactor
     {
             @AutoPlugin
             private IHttpDispatcher commandDispatcher;
             @AutoPlugin
             private LogicImpl logic;
     }
     ```

  3. 添加对应的command

     - 添加注解: @HttpCmdAnno

     - > uri: 就是uri,支持通配符形式,如 /demo/{name}
       >
       > requestType: get or post
       >
       > buzzClz: 框架层会自动解析成buz对象
       >
       > reactor:属于哪个reactor

     - 继承AbstractHttpCommand,然后编写自己的业务逻辑

       - ```
         @HttpCmdAnno(uri = "/cmd3",
                     buzzClz = Cmd3Buz.class,
                     requestType = EnumHttpRequestType.HTTP_URL_GET, reactor = Reactor3.class)
         public static class cm3 extends AbstractHttpCommand
         {
                 @Override
                 protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
                 {
                     Cmd3Buz c = (Cmd3Buz) bo;
                     ctx.response(this.createResponseWp().ret(123).build());
                 }
         }
         ```

---

## rpc-server

- 相关命令行
  - -grpcPort 12000  默认12000 端口

- 1. 添加依赖

     ```
       <dependency>
     	  <groupId>com.cell</groupId>
       	<artifactId>cell-rpc-grpc-server-extension</artifactId>
     	  <version>${cell.version}</version>
      </dependency> 
     ```

  2. 编写reactor

     - 添加注解 @RPCServerReactorAnno

     - 继承AbstractRPCServerReactor

     ```
     @RPCServerReactorAnno
     public static class DemoRPCServerReactor1 extends AbstractRPCServerReactor
     {
     
     }
     ```

  3. 添加对应的command

     - 添加注解: @RPCServerCmdAnno(protocol = "/demo/1.0.0", reactor = RPCServerReactor1.class)

       > protocol  代表的是该cmd 对应的协议,基本格式为 /name/版本号 ,如 /demo/1.0.0 /demo/2.0.0,并没有做校验,后续会做校验
       >
       > reacotr: 则代表属于哪个reacotr, 允许为空
       >
       > buzzClz: 业务类,会自动的通过框架赋值,默认为空,允许为空

     - 继承AbstractGRPCServerCommand 

       > 暂时只提供了grpc ,所以继承这个即可,内部写业务逻辑

     ```
     public static class RPCServerCommand1 extends AbstractGRPCServerCommand
         {	
             @Override
             protected void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException
             {		
             //  do sth...
                 ServerRPCResponse response = new ServerRPCResponse();
                 ctx.response(this.createResponseWp().status(ContextConstants.SUCCESS).ret(response).build());
             }
         }
     ```

----

## rpc-client

- 1. 添加依赖

     ```
      <dependency>
     		<groupId>com.cell</groupId>
     	  <artifactId>cell-rpc-grpc-client-extension</artifactId>
     	 	<version>1.0-SNAPSHOT</version>
      </dependency>
     ```

  2. 定义注解属性 @GRPCClientRequestAnno

     > protocol: 与serverCmd 对应,代表请求的是哪个server
     >
     > async: 同步还是异步形式
     >
     > responseType: 返回类型,框架层自动反序列,不支持基本数据类型(未校验)
     >
     > timeOut: 超时时间,秒为单位

  3. 定义业务对象,这里为了方便,固定业务对象需要实现某个接口ISerializable ,可以直接继承DefaultSelfJsonSerialize

     ```
     @GRPCClientRequestAnno(protocol = "/demo/1.0.0", async = true, responseType = ServerRPCResponse.class)
     public class ClientRequestDemo extends DefaultSelfJsonSerialize
     {
         private String name = "charlie";
     }
     ```

     

  4. (`rpc-client不需要编写command`)编写业务代码

     ```
       @AutoPlugin // 为本地的grpc client
       private ILocalGRPCClientServer im;
       @AutoPlugin // 服务发现版的grpc client
       private IGRPCNacosClientServer nacosClientServer;
       ClientRequestDemo demo = new ClientRequestDemo();
       Future<Object> call = im.call(ctx, demo);
       try
       {
         Object o1 = call.get();
         ctx.response(this.createResponseWp().ret(o1).build());
       } catch (Exception e)
       {
         ctx.response(this.createResponseWp().exception(e).build());
     	}	
     ```

---

## 服务发现

- 添加对应的extension即可

  - http

    - ```
      <dependency>
                  <groupId>com.cell</groupId>
                  <artifactId>cell-discovery-nacos-http-extension</artifactId>
                  <version>${cell.version}</version>
              </dependency>
      ```

    - 

  - rpc-server

    - ```
       <dependency>
                  <groupId>com.cell</groupId>
                  <artifactId>cell-discovery-nacos-grpc-server-extension</artifactId>
                  <version>${cell.version}</version>
      </dependency>
      ```

  - rpc-client

    - ```
      <dependency>
                  <groupId>com.cell</groupId>
                  <artifactId>cell-discovery-nacos-grpc-client-extension</artifactId>
                  <version>${cell.version}</version>
      </dependency>
      ```

    - 


