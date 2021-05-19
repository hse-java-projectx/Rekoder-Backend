package ru.hse.rekoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
public class RekoderApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(RekoderApplication.class, args);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> conver : converters) {
            System.out.println(conver.getSupportedMediaTypes());
        }
    }
}
