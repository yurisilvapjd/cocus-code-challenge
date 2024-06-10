package com.cocus.codechallenge.configurations.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PlainTextConverter plainTextConverter;

    public WebConfig(PlainTextConverter plainTextConverter) {
        this.plainTextConverter = plainTextConverter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(plainTextConverter);
    }
}
