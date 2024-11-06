package asu.eng.gofund.resolvers;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final JwtService jwtService;

    public CurrentUserArgumentResolver(UserRepo userRepo, JwtService jwtService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null &&
                parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Object userAttribute = webRequest.getAttribute("user", NativeWebRequest.SCOPE_REQUEST);
        return userAttribute != null ? userAttribute : null;
    }
}