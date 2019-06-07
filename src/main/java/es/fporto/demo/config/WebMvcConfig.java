package es.fporto.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import es.fporto.demo.filter.RateLimiterFilter;
import nz.net.ultraq.thymeleaf.LayoutDialect;


@Configuration
@EnableWebMvc
//@ComponentScan
public class WebMvcConfig implements  WebMvcConfigurer , ApplicationContextAware {

	private ApplicationContext applicationContext;

	private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages";
	private static final String VIEWS = "/WEB-INF/templates/";

	private static final String RESOURCES_LOCATION = "/resources/";
	private static final String RESOURCES_HANDLER = RESOURCES_LOCATION + "**";
	
	@Value("${rate.limit.value}")
	private int rateLimit;
	@Value("${rate.limit.duration.seconds}")
	private int rateLimitDuration;	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
	        this.applicationContext = applicationContext;
	}
	
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_SOURCE);
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

	@Bean
	public SpringResourceTemplateResolver templateResolver() {

		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix(VIEWS);
		resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
	        SpringTemplateEngine engine = new SpringTemplateEngine();
	        engine.addDialect(new LayoutDialect());
	        engine.addDialect(new Java8TimeDialect());
	        engine.setTemplateResolver(templateResolver());
			engine.setEnableSpringELCompiler(true);
			engine.addDialect(new SpringSecurityDialect());
			engine.addTemplateResolver(new UrlTemplateResolver());
	        //engine.setTemplateEngineMessageSource(messageSource());
	        return engine;
	}

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(0);
        return resolver;
    }

//    @Bean
//    public ViewResolver javascriptViewResolver() {
//        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setTemplateEngine(templateEngine(javascriptTemplateResolver()));
//        resolver.setContentType("application/javascript");
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setViewNames(new String[] {"*.js"});
//        return resolver;
//    }
//
//    @Bean
//    public ViewResolver plainViewResolver() {
//        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setTemplateEngine(templateEngine(plainTemplateResolver()));
//        resolver.setContentType("text/plain");
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setViewNames(new String[] {"*.txt"});
//        return resolver;
//    }

//    private ITemplateResolver javascriptTemplateResolver() {
//        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//        resolver.setApplicationContext(applicationContext);
//        resolver.setPrefix("/WEB-INF/js/");
//        resolver.setCacheable(false);
//        resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
//        return resolver;
//    }
//
//    private ITemplateResolver plainTemplateResolver() {
//        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//        resolver.setApplicationContext(applicationContext);
//        resolver.setPrefix("/WEB-INF/txt/");
//        resolver.setCacheable(false);
//        resolver.setTemplateMode(TemplateMode.TEXT);
//        return resolver;
////    }
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
    	registry.addResourceHandler(RESOURCES_HANDLER).addResourceLocations(RESOURCES_LOCATION);

    	registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

	@Bean
	public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilter(){
	    FilterRegistrationBean<RateLimiterFilter> registrationBean 
	      = new FilterRegistrationBean<>();
	         
	    registrationBean.setFilter(new RateLimiterFilter(rateLimit,rateLimitDuration));
	    registrationBean.addUrlPatterns("/sales/c/*");
	         
	    return registrationBean;    
	}

	@Controller
	static class FaviconController {
		@RequestMapping("favicon.ico")
		String favicon() {
			return "forward:/resources/images/favicon.ico";
		}
	}
}