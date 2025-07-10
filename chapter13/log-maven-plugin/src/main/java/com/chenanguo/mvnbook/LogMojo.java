package com.chenanguo.mvnbook;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "log", defaultPhase = LifecyclePhase.COMPILE)
public class LogMojo extends AbstractMojo {

    // 通过@Parameter注解从POM或命令行获取参数‌
    @Parameter(property = "message", defaultValue = "Hello!")
    private String message;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // 使用了AbstractMojo的getLog()方法，该方法返回一个类似于Log4j的日志对象，可以用来将日志输出到Maven命令行。
        getLog().info(message);
    }
}
