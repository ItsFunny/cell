# core 模块


## 日志
- 日志采用的是slf4j
- feature:
    - 可以使用全局的LOG 
    - 也可以使用一个类一个log
    - 支持定制化
- dispatcher: 分发日志
- Appender: 对哪里进行输出,文件还是控制台,或者是ES
- POLICY: 日志策略:
- Encoder: 对日志编码
- layout: 输出格式
- dispatcher->appender->policy->filter->encoder->hook(内置有一layOut)
- feature:
    - 需要支持结构化的输出格式
    - 添加hook机制
    - 所有的日志都由统一的包装entry
    - 所有日志都有sequenceId跟踪
- 流程:
 ```$xslt
 
```


## filter
- 有一个全局的filter,会自动的扫包,然后如下的类会被注入:
    - @CellFilter所注解的类
        - 处于active状态 并且 模块Id与当前这个相同filterManager相同

## consumers
- 作用: 
    - 依据consumerType,自动获取对应的所有consumer,然后消费信息,消费的主体依旧为event
        -   如logConsumer
- 有一个全局的consumerManager
       
       
       
### TODO
- 2021-07-07  写文件的时候,创建文件夹的地方,还需要根据类型来创建(既外层传一个参数过来)