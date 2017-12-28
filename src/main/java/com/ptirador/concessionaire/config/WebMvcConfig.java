package com.ptirador.concessionaire.config;

import com.ptirador.concessionaire.interceptor.AccessControlInterceptor;
import com.ptirador.concessionaire.interceptor.MenuInterceptor;
import com.ptirador.concessionaire.repository.MenuRepository;
import com.ptirador.concessionaire.repository.UserRepository;
import com.ptirador.concessionaire.service.MenuService;
import com.ptirador.concessionaire.service.MenuServiceImpl;
import com.ptirador.concessionaire.service.UserService;
import com.ptirador.concessionaire.service.UserServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"};

    public WebMvcConfig(final UserRepository userRepository,
                        final MenuRepository menuRepository) {
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public MenuService menuService(MenuRepository menuRepository) {
        return new MenuServiceImpl(menuRepository);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        registry.addInterceptor(new AccessControlInterceptor(userService(userRepository), localeResolver()))
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**", "/webjars/**");
        registry.addInterceptor(new MenuInterceptor(menuService(menuRepository)))
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**", "/webjars/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");
        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/accessDenied").setViewName("accessDenied");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/cars/list").setViewName("cars/list");
        registry.addViewController("/administration").setViewName("administration/index");
        registry.addViewController("/administration/users").setViewName("administration/users/list");
        registry.addViewController("/administration/cars").setViewName("administration/cars/list");
    }

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}