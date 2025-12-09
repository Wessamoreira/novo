package negocio.comuns.utilitarias;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import negocio.facade.jdbc.arquitetura.FacadeFactory;

@Component
@Lazy(false)
public class ScriptExecutadoConfig implements InitializingBean {	

	@Autowired
	private FacadeFactory facadeFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			facadeFactory.getScriptExecutadoInterfaceFacade().executarScripts();
		} catch (Exception e) {
			AplicacaoControle.setMensagemErroScriptsExecutados(e.getMessage());
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.SCRIPT, e);
			e.printStackTrace();
		}
	}
}