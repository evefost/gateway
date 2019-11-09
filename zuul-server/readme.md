
重写hystrix命令
实现在功能:
1.通过监听接口,可以监听到如果异常状态
2.用户可以扩展可以通过异常状态统计

实现
1.自定义fallback 实现
2.定义异常监听器
3.重写现有cmd fallback
HttpClientRibbonCommand
RestClientRibbonCommand
OkHttpRibbonCommand
4.自动配置

5.替换原有配置




