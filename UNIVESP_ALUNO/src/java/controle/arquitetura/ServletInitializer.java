package controle.arquitetura;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.sun.faces.config.ConfigureListener;

import controle.protocolo.UploadServlet;
import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import webservice.servicos.CORSFilter;

@Configuration
public class ServletInitializer extends SpringBootServletInitializer implements ServletContextAware {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SeiApplication.class);
    }

    // --- APAGUEI O 'viewScopeConfigurer' E A CLASSE 'ViewScope' DAQUI ---
    // O JoinFaces já faz isso sozinho.

    @Override
    public void setServletContext(ServletContext servletContext) {
        // Parâmetros do JSF
        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
        servletContext.setInitParameter("facelets.DEVELOPMENT", "true");
        servletContext.setInitParameter("jakarta.faces.DEFAULT_SUFFIX", ".xhtml");
        servletContext.setInitParameter("jakarta.faces.PARTIAL_STATE_SAVING_METHOD", "true"); 
        servletContext.setInitParameter("jakarta.faces.PROJECT_STAGE", "Production");
        servletContext.setInitParameter("jakarta.faces.FACELETS_REFRESH_PERIOD", "1");
        servletContext.setInitParameter("jakarta.faces.FACELETS_SKIP_COMMENTS", "true");
        servletContext.setInitParameter("jakarta.faces.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE", "true");
        servletContext.setInitParameter("timezone", "GMT-3");
        servletContext.setInitParameter("locale", "pt_BR");
        
        servletContext.setInitParameter("jakarta.faces.FACELETS_LIBRARIES", "/WEB-INF/otmcomponentes.xml");
        servletContext.setInitParameter("primefaces.FONT_AWESOME", Boolean.TRUE.toString());
        servletContext.setInitParameter("primefaces.UPLOADER", "auto");
        
        servletContext.addFilter("cors", CORSFilter.class);
        servletContext.addFilter("PrimeFaces FileUpload Filter", "org.primefaces.webapp.filter.FileUploadFilter");
        
        servletContext.setSessionTimeout(45);
    }

    @Bean
    ServletRegistrationBean<FacesServlet> servletRegistrationBean() {
        ServletRegistrationBean<FacesServlet> servletRegistrationBean = new ServletRegistrationBean<>(new FacesServlet(), "*.xhtml");
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.addUrlMappings("/faces/*", "*.jsf", "*.faces"); 
        
        servletRegistrationBean.addInitParameter("jakarta.faces.FACELETS_LIBRARIES", "/WEB-INF/otmcomponentes.xml");
        servletRegistrationBean.addInitParameter("jakarta.servlet.jsp.jstl.fmt.LocalizationContext", "resources.application");
        servletRegistrationBean.addInitParameter("jakarta.faces.AUTOMATIC_EXTENSIONLESS_MAPPING", "true");
        servletRegistrationBean.addInitParameter("jakarta.faces.FACELETS_SKIP_COMMENTS", "true");
        servletRegistrationBean.addInitParameter("jakarta.faces.DISABLE_FACESSERVLET_TO_XHTML", "true");
        servletRegistrationBean.addInitParameter("jakarta.faces.PARTIAL_STATE_SAVING", "true");
        
        return servletRegistrationBean;
    }

    @Bean
    ServletListenerRegistrationBean<ConfigureListener> servletListenerRegistrationBean() {
        return new ServletListenerRegistrationBean<>(new ConfigureListener());
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".xhtml");
        return resolver;
    }

    // Servlets Legados
    @Bean
    ServletRegistrationBean<Servlet> infoSVServletBean() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(new InfoSV(), "/InfoSV/*");
        bean.setLoadOnStartup(2);
        return bean;
    }

    @Bean
    ServletRegistrationBean<Servlet> downloadSVServletBean() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(new DownloadSV(), "/DownloadSV/*");
        bean.setLoadOnStartup(2);
        return bean;
    }

    @Bean
    ServletRegistrationBean<Servlet> downloadRelatorioSVServletBean() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(new DownloadRelatorioSV(), "/DownloadRelatorioSV/*");
        bean.setLoadOnStartup(2);
        return bean;
    }

    @Bean
    ServletRegistrationBean<Servlet> uploadServletBean() {
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(new UploadServlet(), "/UploadServlet/*");
        bean.setLoadOnStartup(2);
        return bean;
    }
}