package negocio.comuns.utilitarias;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import kong.unirest.Unirest;

@Component
@Lazy(false)
public class UnirestSeiConfig  implements InitializingBean {



	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Unirest.config().reset().socketTimeout(600000).connectTimeout(600000);
		} catch (Exception e) {
			System.out.println("Rotina de UnirestSeiConfig executado falhou. " + e.getMessage());
			e.printStackTrace();
		}
	}

}