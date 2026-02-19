package com.rutuj.EmployeeMavenProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.rutuj.EmployeeMavenProject")
public class MvcConfiguration implements WebMvcConfigurer {

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    // 🔥 Multipart Support
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10485760); // 10MB
        return resolver;
    }

    // 🔥 Static Resource Mapping
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // existing resources
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");

        // 🔥 VERY IMPORTANT - Uploads Folder Mapping
        registry.addResourceHandler("/employee_uploads/**")
                .addResourceLocations("file:///C:/employee_uploads/");
    }
}
