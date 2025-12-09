package controle.arquitetura;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

public class ViewScope implements Scope {
	
	public static final String VIEW_SCOPE_CALLBACKS = "viewScope.callbacks";

	@Override
	public synchronized Object get(String name, ObjectFactory<?> objectFactory) {
		Flash flash = getFlashMemoria();		
		if(!name.equals("FilterFactory")){
			String parans = getIdControlador();
			if(parans != null && !parans.trim().isEmpty() && !name.equals("FilterFactory")){			
				name = parans;
			}
		}
//		else{
//			parans = getParameters(name);		
//			if(parans != null && !parans.trim().isEmpty()){
//				name += parans;
//			}
//		}		
		Object instanceFlash = flash.get(name);
		// System.out.println("ViewScope recebe nome: " + name);
		Object instanceView = getViewMap().get(name);
				
		if (instanceFlash == null && instanceView == null) {
			//Entra aqui o controlador é criado a 1ª vez
			instanceView = objectFactory.getObject();
			// System.out.println("ADICIONANDO FLASH:");
			flash.put(name, instanceView);
			getViewMap().put(name, instanceView);
		} else if (instanceView != null && instanceFlash == null) {
			//Entra aqui sempre que não muda de pagina usando o mesmo controlador
			// System.out.println("LIMPANDO FLASH:");
			flash.remove(name);
			flash.put(name, instanceView);
		} else if (instanceFlash != null && instanceView == null) {
			//Entra aqui sempre que muda de pagina usando o mesmo controlador
			getViewMap().put(name, instanceFlash);
			instanceView = instanceFlash;
			// System.out.println("LIMPANDO FLASH 2:");
			flash.clear();
			objectFactory = null;
		}else if(instanceFlash != null && instanceView != null && instanceFlash.equals(instanceView)){
			flash.put(name, instanceView);
		}
		// exibirMemoriaFlash();
		return instanceView;
	}

	public Flash getFlashMemoria() {
		return FacesContext.getCurrentInstance().getExternalContext().getFlash();
	}

	public void exibirMemoriaFlash() {
		System.out.println("MEMORIA FLASH:");
		System.out.println(getFlashMemoria().toString());

	}

	public Object remove(String name) {
		if (FacesContext.getCurrentInstance().getViewRoot() != null) {
			return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public void registerDestructionCallback(String name, Runnable runnable) {
		Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
		if (callbacks != null) {
			callbacks.put(name, runnable);
		}
	}

	public Object resolveContextualObject(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.resolveReference(name);

	}

	public String getConversationId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();

	}

	private Map<String, Object> getViewMap() {
		if (FacesContext.getCurrentInstance().getViewRoot() != null) {
			return (Map<String, Object>) FacesContext.getCurrentInstance().getViewRoot().getViewMap();
		}
		return null;

	}
	
	private String getIdControlador(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		FacesContext context = (FacesContext.getCurrentInstance());
		
		if(request != null && request.getParameterMap() != null && request.getParameterMap().size() > 0){
			if(request.getParameterMap().containsKey("idControlador")){
				return request.getParameter("idControlador");
			}else if(request.getParameterMap().containsKey("form:idControlador")){
					return request.getParameter("form:idControlador");
			}else if(request.getParameterMap().containsKey("idControlador")){
				return request.getParameter("idControlador");
			}else if(context != null ){
				String idControlador = request.getHeader("referer");				
				if(idControlador != null && idControlador.contains("idControlador") && idControlador.contains(context.getViewRoot().getViewId())){
					idControlador = idControlador.substring(idControlador.indexOf("idControlador")+14, idControlador.length());
					if(idControlador.contains("&")){
						idControlador = idControlador.substring(0, idControlador.indexOf("&"));
					}
					return idControlador;					
				}
			}
		}
		return null;
	}
	
}