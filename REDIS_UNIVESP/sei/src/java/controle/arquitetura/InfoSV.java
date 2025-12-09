package controle.arquitetura;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.basico.ScriptExecutadoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Alessandro Lima
 */
@Service
@Lazy
@WebServlet(name = "InfoSV")
public class InfoSV extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).create();

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		String json = "";
		try {
	        Info info = new Info(Uteis.VERSAO_SISTEMA,
	        		AplicacaoControle.getMensagemErroScriptsExecutados(),
	        		AplicacaoControle.getScriptsNaoExecutados(),
	        		AplicacaoControle.getScriptsNaoPersistidos(),
	        		AplicacaoControle.getScriptsAbortados(),
	        		AplicacaoControle.getScriptsInvalidos());
			json = this.gson.toJson(info);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			e.printStackTrace();
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.SCRIPT, e);
			json = "{\"versao\":\"" + Uteis.VERSAO_SISTEMA + "\",\"mensagemErroScriptsExecutados\":\"" + e.getMessage() +
					"\",\"scriptsNaoExecutados\":[],\"scriptsNaoPersistidos\":[],\"scriptsAbortados\":[],\"scriptsInvalidos\":[]}";
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    class Info {
    	@SuppressWarnings("unused")
		private String versao;
    	@SuppressWarnings("unused")
		private String mensagemErroScriptsExecutados;
    	@SuppressWarnings("unused")
		private List<ScriptExecutadoVO> scriptsNaoExecutados;
    	@SuppressWarnings("unused")
		private List<ScriptExecutadoVO> scriptsNaoPersistidos;
    	@SuppressWarnings("unused")
		private List<ScriptExecutadoVO> scriptsAbortados;
    	@SuppressWarnings("unused")
		private List<ScriptExecutadoVO> scriptsInvalidos;
    	
    	private Info (String versao,
    			String mensagemErroScriptsExecutados,
    			List<ScriptExecutadoVO> scriptsNaoExecutados,
    			List<ScriptExecutadoVO> scriptsNaoPersistidos,
    			List<ScriptExecutadoVO> scriptsAbortados,
    			List<ScriptExecutadoVO> scriptsInvalidos) {
    		this.versao = versao;
    		this.mensagemErroScriptsExecutados = mensagemErroScriptsExecutados;
    		this.scriptsNaoExecutados = scriptsNaoExecutados;
    		this.scriptsNaoPersistidos = scriptsNaoPersistidos;
    		this.scriptsAbortados = scriptsAbortados;
    		this.scriptsInvalidos = scriptsInvalidos;
    	}
    }

}