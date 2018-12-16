package com.rudramanish.dumpster;

import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;



import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.rudramanish.dumpster.controller.MainController;
import com.rudramanish.dumpster.daoObjects.DatabaseDaoQuery;

@SpringBootApplication
public class Application {
	@Autowired
	public ServletContext servletContext;
	public static ConfigurableApplicationContext ctx;
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
    	ctx = SpringApplication.run(Application.class, args);
    }
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return arg -> {
			logger.info("commandLineRunner tested: it runs first");
        };
    }

/*
    @Bean
    public ServletContextTemplateResolver templateResolver() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setCacheable(false);
        templateResolver.setPrefix("classpath:templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        return viewResolver;
    }
    */
}