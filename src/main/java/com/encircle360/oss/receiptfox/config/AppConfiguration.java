package com.encircle360.oss.receiptfox.config;

import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import no.api.freemarker.java8.Java8ObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppConfiguration {

    private final Configuration freemarkerConfig;

    @PostConstruct
    public void init() {
        this.freemarkerConfig.setObjectWrapper(new Java8ObjectWrapper(Configuration.VERSION_2_3_23));
    }
}
