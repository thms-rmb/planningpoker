package no.ramsen.planningpoker.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.List;
import java.util.Map;

@Configuration
public class FaviconRequestConfig {
    @Bean
    public static SimpleUrlHandlerMapping asBean(
            @Qualifier("faviconResourceRequestHandler") ResourceHttpRequestHandler faviconResourceRequestHandler) {
        return new SimpleUrlHandlerMapping(
                Map.of("/favicon.svg", faviconResourceRequestHandler),
                Integer.MIN_VALUE
        );
    }

    @Bean("faviconResourceRequestHandler")
    public static ResourceHttpRequestHandler faviconResourceRequestHandler() {
        var handler = new ResourceHttpRequestHandler();
        var resource = new ClassPathResource("favicon.svg");
        handler.setLocations(List.of(resource));
        return handler;
    }
}
