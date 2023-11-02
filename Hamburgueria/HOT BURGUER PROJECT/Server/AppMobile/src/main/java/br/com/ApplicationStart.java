package br.com;

import br.com.comprazencasa.services.ServiceManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


import java.io.IOException;

@SpringBootApplication
public class ApplicationStart extends SpringBootServletInitializer {

    private String encoding;
    public static final ServiceManager services = new ServiceManager();

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApplicationStart.class);

//        services.getApiGuanabara().start();
        services.getApiEmailServer().setSmtHost("email@comprazencasa.com.br", "M@rlon7832@4512", "mail@comprazencasa.com.br");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApplicationStart.class);
    }



    /*@Bean
    public Filter getCharacterEncodingFilter() {

        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();

        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        return encodingFilter;
    }*/

   /* @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encoding-filter",
                new CharacterEncodingFilter());
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, true, "/*");
    }*/
}
