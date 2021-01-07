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