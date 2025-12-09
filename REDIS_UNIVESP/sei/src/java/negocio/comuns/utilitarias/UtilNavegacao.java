package negocio.comuns.utilitarias;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import controle.arquitetura.SuperControle; public class UtilNavegacao implements Serializable{
	
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
			return SpringUtil.getApplicationContext().getBean(nomeControlador);
		}
		return contexto.getELContext().getELResolver().getValue(contexto.getELContext(), null, nomeControlador);
	}
	
}
