package negocio.comuns.utilitarias;

import controle.arquitetura.SuperControle;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class UtilNavegacao implements Serializable{
	
	private static SpringUtil springUtil;
	
	public static String navegarPara(String controlador, String metodoASerExecutado, String fromOutcome, Object... parametroDoMetodo) throws Exception{
		executarMetodoControle(controlador, metodoASerExecutado, parametroDoMetodo);
		return fromOutcome;
	}
	
	public static void executarMetodoControle(String controlador, String metodoASerExecutado, Object... parametroDoMetodo){
		SuperControle controle = (SuperControle) getControlador(controlador);
		UtilReflexao.invocarMetodo(controle, metodoASerExecutado, parametroDoMetodo);
	}
	
	public static Object getControlador(String nomeControlador){
		FacesContext contexto = FacesContext.getCurrentInstance();
		if(contexto == null) {
			return getSpringUtil().getApplicationContext().getBean(nomeControlador);
		}
		return contexto.getELContext().getELResolver().getValue(contexto.getELContext(), null, nomeControlador);
	}

	@Autowired
	public static SpringUtil getSpringUtil() {
		return springUtil;
	}

	public static void setSpringUtil(SpringUtil springUtil) {
		UtilNavegacao.springUtil = springUtil;
	}
	
	
	
}
