package jobs;

import org.springframework.beans.factory.InitializingBean;

import negocio.comuns.utilitarias.Uteis;

public class JobSerasaApiGeoProcessamentoInterrompido  implements InitializingBean {

	
	public void run() {
		try {
			if(!Uteis.isVersaoDev()) {
				ThreadSerasaApiGeoProcessamentoInterrompido tsagpi = new ThreadSerasaApiGeoProcessamentoInterrompido();
				Thread t = new Thread(tsagpi);
				t.start();	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		run();
	}

}
