package asu.eng.gofund.config;

import asu.eng.gofund.interceptors.AuthenticationInterceptor;
import asu.eng.gofund.interceptors.AuthorizationInterceptor;
import asu.eng.gofund.resolvers.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;

    public WebConfig(CurrentUserArgumentResolver currentUserArgumentResolver,
            AuthenticationInterceptor authenticationInterceptor, AuthorizationInterceptor authorizationInterceptor) {
        this.currentUserArgumentResolver = currentUserArgumentResolver;
        this.authenticationInterceptor = authenticationInterceptor;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor);

        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/control-panel")
                .addPathPatterns("/users/logout")
                .addPathPatterns("/donate/**")
                .addPathPatterns("/user-type/**");
    }
}
