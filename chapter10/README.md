### 第十章 使用Maven进行测试

Maven所做的只是在构建执行到特定生命周期阶段的时候，通过插件来执行JUnit或者TestNG的测试用例。这一插件就是maven-surefire-plugin，可以称之为测试运行器（Test Runner），它能很好地兼容JUnit 3、JUnit 4以及TestNG。

<strong><span style="color:red;">报告中的Failures、Errors、Skipped信息来源于JUnit测试框架。Failures（失败）表示要测试的结果与预期值不一致，例如测试代码期望返回值为true，但实际为false；Errors（错误）表示测试代码或产品代码发生了未预期的错误，例如产品代码抛出了一个空指针错误，该错误又没有被测试代码捕捉到；Skipped表示那些被标记为忽略的测试方法，在JUnit中用户可以使用@Ignore注解标记忽略测试方法。</span></strong>

<strong><span style="color:red;">在默认情况下，maven-surefire-plugin的test目标会自动执行测试源码路径（默认为src/test/java/）下所有符合一组命名模式的测试类。</span></strong>
这组模式为：
1. `**/Test*.java`：任何子目录下所有命名以Test开头的Java类。
2. `**/*Test.java`：任何子目录下所有命名以Test结尾的Java类。
3. `**/*TestCase.java`：任何子目录下所有命名以TestCase结尾的Java类。

只要将测试类按上述模式命名，Maven就能自动运行它们，用户也就不再需要定义测试集合（TestSuite）来聚合测试用例（TestCase）。

临时性地跳过测试代码的编译，Maven也允许你这么做，但记住这是不推荐的：$`mvn package -Dmaven.test.skip=true`；<strong><span style="color:red;">参数maven.test.skip同时控制了maven-compiler-plugin和maven-surefire-plugin两个插件的行为，测试代码编译跳过了，测试运行也跳过了。</span></strong>

<strong><span style="color:red;">skipTests参数，只跳过测试，但是还会编译测试代码。</span></strong>

maven-surefire-plugin提供了一个test参数让Maven用户能够在命令行指定要运行的测试用例。例如，如果只想运行RandomGeneratorTest，就可以使用如下命令：$`mvn test -Dtest=RandomGeneratorTest`