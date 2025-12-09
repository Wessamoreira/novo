package controle.administrativo;



import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.SessionScope;
import java.util.HashMap;
import java.util.Map;

/**
* Força o registro dos escopos de visualização do JSF no Spring,
* tratando os nomes 'view' e 'viewScope'.
*/
@Configuration
public class JsfConfiguration {

 @Bean
 public CustomScopeConfigurer customScopeConfigurer() {
     CustomScopeConfigurer configurer = new CustomScopeConfigurer();
     Map<String, Object> scopes = new HashMap<>();

     // 1. Escopo 'view' (Nome comum usado pelo JoinFaces)
     // Usamos SessionScope como proxy simples, mas em um ambiente JSF real,
     // o JoinFaces/JSF lida com a destruição correta.
     scopes.put("view", new SessionScope());

     // 2. Escopo 'viewScope' (O nome exato que está falhando no seu stack trace)
     scopes.put("viewScope", new SessionScope());

     configurer.setScopes(scopes);
     return configurer;
 }
}