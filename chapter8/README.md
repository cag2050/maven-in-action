### 第八章 聚合与继承

软件设计人员往往会采用各种方式对软件划分模块，以得到更清晰的设计及更高的重用性。当把Maven应用到实际项目中的时候，也需要将项目分成不同的模块。

<strong><span style="color:red;">Maven的聚合特性能够把项目的各个模块聚合在一起构建，而Maven的继承特性则能帮助抽取各模块相同的依赖和插件等配置，在简化POM的同时，还能促进各个模块配置的一致性。</span></strong>

#### 8.1 account、account-email、account-persist
在chapter8目录下，新建account项目：

