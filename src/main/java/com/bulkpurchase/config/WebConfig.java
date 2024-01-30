package com.bulkpurchase.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:C:\\Users\\krkarma777.DESKTOP-R0EM2VJ\\Desktop\\개인 프로젝트\\BulkPurchaseSubscription\\uploaded-images\\");
    }
}
