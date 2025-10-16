package com.nordic.backend.company.Common.ServiceBeans;

import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class UrlSuccessHandler {
  @Bean
  public SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler() {
    SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
    handler.setDefaultTargetUrl("/api/v1/guest/public"); // your redirect after GitHub login
    handler.setAlwaysUseDefaultTargetUrl(true);
    return handler;
  }
}
