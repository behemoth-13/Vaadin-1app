package com.example.myapp1.UI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.spring.annotation.EnableVaadin;

@Configuration
@EnableVaadin    // this imports VaadinConfiguration
public class MyConfiguration {
    // application specific configuration - register myBean in the context
    /*@Bean
    public MyBean myBean() {
        return new MyBean();
    }*/
}
