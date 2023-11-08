package com.thewayhome.ptis.core;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PtisCoreApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PtisCoreApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
