# cell
- 自研框架

# cell内部概念
- Queue: 蜂后
- Bee: 工蜂: 可以单独的抽离,离群锁具
- Dispatcher 与 Handler 匹配
- Producer 与 Consumer 匹配
- Producer 分发给 Dispatcher  ,Dispatcher 将任务给Handler,Handler再将任务委托给consumer

# feature
- 配置:
    - 每个bee 都应该有一个默认的defaultConfigProperty

# 模块划分
- core/base模块(业务相关)
- 配置模块
- 日志模块
- web模块
    - dao模块
    - service模块
    - http模块
    - 网关模块
- 权限模块
- 缓存模块
- 任务模块
- MO模块
- 区块链模块



# 代码组织结构划分
- 每个module都有代码组织结构划分


