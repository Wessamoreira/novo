package controle.arquitetura;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

import jakarta.faces.annotation.FacesConfig;


@Configuration
@EnableWebMvc
@FacesConfig
public class WebMvcConfiguration  implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.xhtml");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);			
	}
	


	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> customCharacterEncodingFilter() {
		FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		registrationBean.setFilter(characterEncodingFilter);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());		
	}

	@Bean
	LocaleResolver localeResolverCustom() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.forLanguageTag("pt_BR"));
		return slr;
	}
	
	@Bean
	ResourceBundle getBeanResourceBundle() {
		final Locale locale = Locale.forLanguageTag("pt_BR");
		return new MessageSourceResourceBundle(messageSource(), locale);
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");	
		return lci;
	}
		

	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setAlwaysUseMessageFormat(true);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("ISO-8859-1");
		messageSource.setBasenames("classpath:Aplicacao.properties", "classpath:Botoes.properties", "classpath:enum_pt_BR", "classpath:permissao", "classpath:mensagens", "classpath:aplicacao", "classpath:menu", "classpath:sistema", "classpath:application");
		return messageSource;
	}

	@Bean	
	ControleAcessoListener controleAcessoListener() {
		return new ControleAcessoListener();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/imagens/**", "/resources/imagens/**", "/arquivos/**", "/arquivo/**")
	            .addResourceLocations("/resources/imagens/", "/resources/imagens/", "/arquivos/", "/arquivo/")
	            .setCachePeriod(3600);
	    	  
	}

}
