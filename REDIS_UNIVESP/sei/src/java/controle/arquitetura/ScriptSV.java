package controle.arquitetura;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import negocio.comuns.basico.ScriptExecutadoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.FacadeFactory;

/**
 *
 * @author Alessandro Lima
 */
@Service
@Lazy
@WebServlet(name = "ScriptSV")
public class ScriptSV extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private WebApplicationContext applicationContext;
	private FacadeFactory facadeFactory;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		try {
			String codigoString = request.getParameter("codigo");
			String nome = request.getParameter("nome");
			String reload = request.getParameter("reload");
			if (Uteis.isAtributoPreenchido(codigoString)) {
				if (realizarExclusaoScriptExecutadoPeloCodigo(Integer.parseInt(codigoString))) {
					getFacadeFactory().getScriptExecutadoInterfaceFacade().executarScripts();
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} else if (Uteis.isAtributoPreenchido(nome)) {
				if (realizarExclusaoScriptAbortadoOuInvalidoOuNaoPersistidoPeloNome(nome)) {
					getFacadeFactory().getScriptExecutadoInterfaceFacade().executarScripts();
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} else if (Uteis.isAtributoPreenchido(reload) && "true".equals(reload)) {
				getFacadeFactory().getScriptExecutadoInterfaceFacade().executarScripts();
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
    }
	
	private boolean realizarExclusaoScriptExecutadoPeloCodigo(int codigo) throws Exception {
		boolean removido = false;
		Iterator<ScriptExecutadoVO> isne = AplicacaoControle.getScriptsNaoExecutados().iterator();
        while (isne.hasNext()) {
        	ScriptExecutadoVO script = (ScriptExecutadoVO) isne.next();
            if (script.getCodigo().intValue() == codigo) {
            	getFacadeFactory().getScriptExecutadoInterfaceFacade().alterarScriptNaoExecutadoParaExecutadoComSucesso(codigo);
            	isne.remove();
            	removido = true;
            	break;
            }
        }
        return removido;
	}
	
	private boolean realizarExclusaoScriptAbortadoOuInvalidoOuNaoPersistidoPeloNome(String nome) throws Exception {
		boolean removido = false;
		Iterator<ScriptExecutadoVO> isnp = AplicacaoControle.getScriptsNaoPersistidos().iterator();
        while (isnp.hasNext()) {
        	ScriptExecutadoVO script = (ScriptExecutadoVO) isnp.next();
        	if (script.getNome().replace("%25", "%").equals(nome)) {
            	script.setSucesso(true);
            	script.setMensagemErro("script marcado como executado");
            	getFacadeFactory().getScriptExecutadoInterfaceFacade().persistir(script);
            	isnp.remove();
            	removido = true;
            	break;
            }
        }
		if (!removido) {
			Iterator<ScriptExecutadoVO> isa = AplicacaoControle.getScriptsAbortados().iterator();
	        while (isa.hasNext()) {
	        	ScriptExecutadoVO script = (ScriptExecutadoVO) isa.next();
	            if (script.getNome().replace("%25", "%").equals(nome)) {
	            	script.setSucesso(true);
	            	script.setMensagemErro("script marcado como executado");
	            	getFacadeFactory().getScriptExecutadoInterfaceFacade().persistir(script);
	            	isa.remove();
	            	removido = true;
	            	break;
	            }
	        }
		}
        if (!removido) {
	        Iterator<ScriptExecutadoVO> isi = AplicacaoControle.getScriptsInvalidos().iterator();
	        while (isi.hasNext()) {
	        	ScriptExecutadoVO script = (ScriptExecutadoVO) isi.next();
	            if (script.getNome().replace("%25", "%").equals(nome)) {
	            	script.setSucesso(true);
	            	script.setMensagemErro("script marcado como executado");
	            	getFacadeFactory().getScriptExecutadoInterfaceFacade().persistir(script);
	            	isi.remove();
	            	removido = true;
	            	break;
	            }
	        }
        }
        return removido;
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		final WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		setApplicationContext(context);
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    public WebApplicationContext getApplicationContext() throws Exception {
		if (applicationContext == null) {
			throw new Exception("Não foi possível obter o Contexto do Spring");
		}
		return applicationContext;
	}

	public void setApplicationContext(WebApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
    
    public FacadeFactory getFacadeFactory() throws BeansException, Exception {
		if (facadeFactory == null) {
			facadeFactory = (FacadeFactory) getApplicationContext().getBean("facadeFactory");
		}
		return facadeFactory;
	}

	public void setFacadeFactory(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}

}