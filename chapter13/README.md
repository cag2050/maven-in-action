### 第十三章 编写Maven插件

Maven的任何行为都是由插件完成的，包括项目的清理、编译、测试以及打包等操作都有其对应的Maven插件。<strong><span style="color:red;">每个插件拥有一个或者多个目标，用户可以直接从命令行运行这些插件目标，或者选择将目标绑定到Maven的生命周期。</span></strong>

#### 13.1 编写Maven插件的一般步骤
编写Maven插件的主要步骤。
1. 创建一个maven-plugin项目：<strong><span style="color:red;">插件本身也是Maven项目，特殊的地方在于它的packaging必须是maven-plugin。</span></strong>
2. 为插件编写目标：<strong><span style="color:red;">每个插件都必须包含一个或者多个目标，Maven称之为Mojo</span></strong>（与POJO对应，后者指Plain OldJava Object，这里指Maven Old Java Object）。编写插件的时候必须提供一个或者多个继承自AbstractMojo的类。
3. 为目标提供配置点：大部分Maven插件及其目标都是可配置的，因此在编写Mojo的时候需要注意提供可配置的参数。
4. 编写代码实现目标行为：根据实际的需要实现Mojo。
5. 错误处理及日志：当Mojo发生异常时，根据情况控制Maven的运行状态。在代码中编写必要的日志以便为用户提供足够的信息。
6. 测试插件：编写自动化的测试代码测试行为，然后再实际运行插件以验证其行为。

创建插件实例：

1.chapter13目录下，新建log-maven-plugin项目：
<img src="./log-maven-plugin.png">
2.log-maven-plugin项目中，修改pom.xml，新增：
```
    <packaging>maven-plugin</packaging>
```
```
    <dependencies>
        <dependency>
            <groupId>org.apache.maven</groupId>
            <artifactId>maven-plugin-api</artifactId>
            <version>3.9.10</version>
        </dependency>
        <dependency>
            <groupId>org.apache.maven.plugin-tools</groupId>
            <artifactId>maven-plugin-annotations</artifactId>
            <version>3.15.1</version>
        </dependency>
    </dependencies>
```
3.编写代码：log-maven-plugin/src/main/java/com/chenanguo/mvnbook/LogMojo.java。<strong><span style="color:red;">每个插件目标类，或者说Mojo，都必须继承AbstractMojo并实现execute()方法，只有这样Maven才能识别该插件目标，并执行execute()方法中的行为。</span></strong>

4.安装到本地仓库：`mvn install`

5.Maven命令行调用插件的log目标（使用message参数的默认值）：`mvn com.chenanguo.mvnbook:log-maven-plugin:1.0-SNAPSHOT:log`

6.Maven命令行调用插件的log目标（message参数传入值）：`mvn com.chenanguo.mvnbook:log-maven-plugin:1.0-SNAPSHOT:log -Dmessage='Hello Tom.'`