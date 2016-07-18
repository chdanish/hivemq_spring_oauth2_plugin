package com.hivemq.plugin.spring_guice.springconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:log4j.properties")
@ComponentScan("com.hivemq.plugin.spring_guice")
public class AppConfig {

}
