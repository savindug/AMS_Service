package com.qss.AMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class QssAttendanceManagementApplication {

	public static void main(String[] args) {

		System.out.println("AMS Service Started Successfully!");
		SpringApplication.run(QssAttendanceManagementApplication.class, args);

	}

    @Configuration
    public class AppConf implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("*");
        }
    }


}
