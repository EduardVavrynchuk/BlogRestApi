package com.blog;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@org.springframework.boot.test.context.TestConfiguration
@ComponentScan(
        basePackages = {
                "com.blog.services",
                "com.blog.db",
                "com.blog.webapp.controllers"
        }
)
@EnableAutoConfiguration
public class TestConfiguration {
}
