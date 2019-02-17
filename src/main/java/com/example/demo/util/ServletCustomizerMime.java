package com.example.demo.util;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ServletCustomizerMime implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("woff", "application/x-font-woff");
        mappings.add("doc", "application/msword");
        mappings.add("docx", "application/msword");
        factory.setMimeMappings(mappings);
    }
}