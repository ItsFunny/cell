# 大体架构:


- switch-reactor模式
- 从大到小:
    - switch -> reactor -> shim

```$xslt
每个reactor都有一个baseReactor



```


---
# TODO
- spring 添加一个boundle ,继承factoryPostProcessor 接口,然后可以获取绑定的beanPostProcessor

---
# extension
- extension 可以通过命令行参数来实现各自的option
- extension分为4个阶段
    - init阶段 (当bean被创建完毕之后会调用)
    - start阶段  ()
    - 