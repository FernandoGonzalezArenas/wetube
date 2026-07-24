package com.teakter.admin.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

@Override
    public void apply(RequestTemplate template){
    ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes!=null){
        HttpServletRequest request=attributes.getRequest();
        template.header("X-User-Id", request.getHeader("X-User-Id"));
        template.header("X-User-Role", request.getHeader("X-User-Role"));
        template.header("X-User-Name", request.getHeader("X-User-Name"));
    }
}

}
