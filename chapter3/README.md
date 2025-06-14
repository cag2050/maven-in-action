### 第三章 Maven使用入门

> 本章重点：mvn clean compile、mvn clean test、mvn clean package、mvn clean install。

#### 需要的软件和资源
1. 通过这个链接下载Java的编辑器：IntelliJ IDEA：https://www.jetbrains.com.cn/idea/download/ 。熟悉在编辑器中：新建项目Project、新建包Package、新建Java class。
2. 下载对应系统的JD-GUI，用来查看jar包中的文件：https://java-decompiler.github.io/ ，比如：macOS系统下载jd-gui-osx-1.6.6.tar。
3. 搜索jar包的网址：https://mvnrepository.com/

#### 3.1 编写POM

就像Make的Makefile、Ant的build.xml一样，<strong><span style="color:red;">Maven项目的核心是pom.xml。</span></strong>POM（Project Object Model，项目对象模型）定义了项目的基本信息，用于描述项目如何构建，声明项目依赖，等等。

现在先为Hello World项目编写一个最简单的pom.xml。

首先创建一个名为hello-world的文件夹，打开该文件夹，新建一个名为pom.xml的文件，输入其内容：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.chenanguo.mvnbook</groupId>
    <artifactId>hello-world</artifactId>
    <version>1.0-SNAPSHOT</version>

    <name>Maven Hello World Project</name>
</project>
```
代码的第一行是XML头，指定了该xml文档的版本和编码方式。紧接着是project元素，project是所有pom.xml的根元素，它还声明了一些POM相关的命名空间及xsd元素，虽然这些属性不是必须的，但使用这些属性能够让第三方工具（如IDE中的XML编辑器）帮助我们快速编辑POM。

<strong><span style="color:red;">根元素下的第一个子元素modelVersion指定了当前POM模型的版本，对于Maven 2及Maven 3来说，它只能是4.0.0。</span></strong>

<strong><span style="color:red;">这段代码中最重要的是包含groupId、artifactId和version的三行。这三个元素定义了一个项目基本的坐标</span></strong>，在Maven的世界，任何的jar、pom或者war都是以基于这些基本的坐标进行区分的。

<strong><span style="color:red;">groupId定义了项目属于哪个组，这个组往往和项目所在的组织或公司存在关联。artifactId定义了当前Maven项目在组中唯一的ID。version指定了Hello World项目当前的版本：1.0-SNAPSHOT。</span></strong>SNAPSHOT意为快照，说明该项目还处于开发中，是不稳定的版本。最后一个name元素声明了一个对于用户更为友好的项目名称，虽然这不是必须的，但还是推荐为每个POM声明name，以方便信息交流。

#### 3.2 编写主代码
项目主代码和测试代码不同，<strong><span style="color:red;">项目的主代码会被打包到最终的构件中（如jar），而测试代码只在运行测试时用到，不会被打包。</span></strong>默认情况下，Maven假设项目主代码位于src/main/java目录，我们遵循Maven的约定，创建该目录，然后在该目录下创建包Package：com.chenanguo.mvnbook.helloworld，在包里创建HelloWorld.java
```java
package com.chenanguo.mvnbook.helloworld;

public class HelloWorld {
    public String sayHello() {
        return"Hello Maven";
    }
    
    public static void main(String[] args) {
        System.out.print(new HelloWorld().sayHello());
    }
}
```
关于该Java代码有两点需要注意。首先，在绝大多数情况下，应该把项目主代码放到src/main/java/目录下（遵循Maven的约定），而无须额外的配置，Maven会自动搜寻该目录找到项目主代码。其次，该Java类的包名是com.chenanguo.mvnbook.helloworld，这与之前在POM中定义的groupId和artifactId相吻合。<strong><span style="color:red;">一般来说，项目中Java类的包都应该基于项目的groupId和artifactId，这样更加清晰，更加符合逻辑，也方便搜索构件或者Java类。</span></strong>

代码编写完毕后，使用Maven进行编译，在项目根目录即文件夹hello-world下运行命令：mvn clean compile。

clean告诉Maven清理输出目录target/，compile告诉Maven编译项目主代码，从输出中看到Maven首先执行了clean:clean任务，删除target/目录。默认情况下，Maven构建的所有输出都在target/目录中；接着执行resources:resources任务（未定义项目资源，暂且略过）；最后执行compiler:compile任务，将项目主代码编译至target/classes目录（编译好的类为com/chenanguo/mvnbook/helloworld/HelloWorld.Class）。

上文提到的clean:clean、resources:resources和compiler:compile对应了一些Maven插件及插件目标，比如clean:clean是clean插件的clean目标，compiler:compile是compiler插件的compile目标。后文会详细讲述Maven插件及其编写方法。

#### 3.3　编写测试代码
<strong><span style="color:red;">Maven项目中默认的主代码目录是src/main/java，对应地，Maven项目中默认的测试代码目录是src/test/java。</span></strong>

在Java世界中，由Kent Beck和Erich Gamma建立的JUnit是事实上的单元测试标准。要使用JUnit，首先需要为Hello World项目添加一个JUnit依赖，修改项目的POM：
```
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
上述POM代码中还有一个值为test的元素scope，scope为依赖范围，若依赖范围为test则表示该依赖只对测试有效。换句话说，测试代码中的import JUnit代码是没有问题的，但是如果在主代码中用import JUnit代码，就会造成编译错误。<strong><span style="color:red;">如果不声明依赖范围，那么默认值就是compile，表示该依赖对主代码和测试代码都有效。</span></strong>后文会详细讲述依赖范围。

编写测试类，在src/test/java目录下，创建包Package：com.chenanguo.mvnbook.helloworld，在包里创建文件HelloWorldTest.java：
```java
package com.chenanguo.mvnbook.helloworld;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloWorldTest {

    @Test
    public void testSayHello() {
        HelloWorld helloWorld=new HelloWorld();
        String result=helloWorld.sayHello();
        assertEquals("Hello Maven", result);
    }
}
```
<strong><span style="color:red;">在JUnit 4中，约定所有需要执行测试的方法都以test开头，需要执行的测试方法都应该以@Test进行标注。</span></strong>

调用Maven执行测试，在项目根目录即文件夹hello-world下运行命令：mvn clean test

在Maven执行测试（test）之前，它会先自动执行项目主资源处理、主代码编译、测试资源处理、测试代码编译等工作，这是Maven生命周期的一个特性。本书后续章节会详细解释Maven的生命周期。

surefire:test任务运行测试，<strong><span style="color:red;">surefire是Maven中负责执行测试的插件</span></strong>，这里它运行测试用例HelloWorldTest，并且输出测试报告，显示一共运行了多少测试，失败了多少，出错了多少，跳过了多少。

#### 3.4　打包和运行
将项目进行编译、测试之后，下一个重要步骤就是打包（package）。Hello World的POM中没有指定打包类型，使用默认打包类型jar。在项目根目录即文件夹hello-world下运行命令进行打包：mvn clean package

jar:jar任务负责打包，实际上就是jar插件的jar目标将项目主代码打包成一个名为hello-world-1.0-SNAPSHOT.jar的文件。该文件也位于target/输出目录中，<strong><span style="color:red;">jar文件是根据artifactId-version.jar规则进行命名的。</span></strong>

如何才能让其他的Maven项目直接引用这个jar呢？还需要一个安装的步骤，在项目根目录即文件夹hello-world下执行命令：mvn clean install

该任务将项目输出的jar安装到了Maven本地仓库中，可以打开相应的文件夹看到Hello World项目的pom和jar。之前讲述JUnit的POM及jar的下载的时候，我们说<strong><span style="color:red;">只有构件被下载到本地仓库后，才能由所有Maven项目使用</span></strong>，这里是同样的道理，只有将Hello World的构件安装到本地仓库之后，其他Maven项目才能使用它。

Maven最主要的命令：mvn clean compile、mvn clean test、mvn clean package、mvn clean install。执行顺序：compile、test、package、install。这是Maven约定的。

<strong><span style="color:red;">默认打包生成的jar是不能够直接运行的，因为带有main方法的类信息不会添加到manifest中（打开jar文件中的META-INF/MANIFEST.MF文件，将无法看到Main-Class一行）。为了生成可执行的jar文件，需要借助maven-shade-plugin</span></strong>，配置该插件如下：
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId> <!-- https://maven.apache.org/plugins/maven-shade-plugin/plugin-info.html -->
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.chenanguo.mvnbook.helloworld.HelloWorld</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

在项目根目录即文件夹hello-world下执行：mvn clean install，待构建完成之后打开target/目录，可以看到hel-lo-world-1.0-SNAPSHOT.jar和original-hello-world-1.0-SNAPSHOT.jar，前者是带有Main-Class信息的可运行jar，后者是原始的jar，打开hello-world-1.0-SNAPSHOT.jar的META-INF/MANIFEST.MF，可以看到它包含这样一行信息：
```
Main-Class: com.chenanguo.mvnbook.helloworld.HelloWorld
```

在项目根目录即文件夹hello-world下执行该jar文件：java -jar target/hello-world-1.0-SNAPSHOT.jar，控制台输出为：Hello Maven，这正是我们所期望的。




