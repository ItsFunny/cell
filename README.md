# How to Use 
- 编写一个类,继承dynamicReactor ,然后添加cmd 即可(**注意,不能是内部类**)
```$xslt
@Command(commandId = 4)
    @HttpCmdAnno(uri = "/demo/demo2")
    public static class MyComd2 extends AbstractHttpCommand
    {
        @Override
        protected ICommandExecuteResult onExecute(IHttpContext ctx, ISerializable bo) throws IOException
        {
            ctx.response(this.createResponseWp()
                    .ret("123").build());
            return null;
        }
    }

    @ReactorAnno
    public static class MyReactor2 extends AbstractHttpDymanicCommandReactor
    {
        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(MyComd2.class);
        }
    }
```

```$xslt
                ReactoryFactory.builder()
                .withGroup("/demo")
                .withBean(CC.class)
                .newCommand()
                .withUri("/getUserName")
                .withBuzzHandler((ctx) ->
                {
                    IMapDynamicHttpReactor reactor = (IMapDynamicHttpReactor) ctx.getContext().getHttpReactor();
                    Object dependency = reactor.getDependency(CC.class);
                    Assert.notNull(dependency, "cc不可为空");
                    ctx.success("getUserName");
                    return null;
                })
                .newCommand()
                .withUri("/getFile")
                .withBuzzHandler((ctx) ->
                {
                    ctx.success("getFile");
                    return null;
                }).make().build();
```