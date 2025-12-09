package controle.arquitetura;

import java.io.Serializable;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Alessandro Lima
 */
@Controller("SistemaControle")
@Scope("viewScope")
@Lazy
public class SistemaControle extends SuperControle implements Serializable {
	
	public SistemaControle() {		

	}
	
    public String getFornecedorJava() {
        return System.getProperty("java.vendor");
    }

    public String getVersaoJava() {
        return System.getProperty("java.version");
    }

    public String getJavaHome() {
        return System.getProperty("java.home");
    }
	
	public String getLocal() {
    	return System.getProperty("user.language") + "_" + System.getProperty("user.country");
    }
    
    public String getTimezone() {
    	return System.getProperty("user.timezone");
    }
    
    public Double getNumeroExemplo () {
    	return 1234.56;
    }
    
    public String getHorarioAplicacao() {
    	return Uteis.getDataComHora(new Date());
    }
    
    public String getNomeSO() {
        return System.getProperty("os.name") + " - " + System.getProperty("os.arch");
    }
    
    public String getUserSO() {
    	return System.getProperty("user.name");
    }

    public String getCaminhoWeb() throws Exception {
    	return UteisJSF.getCaminhoWeb();
    }
    
    public String getRequestURL() {
    	HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
    	return request.getRequestURL().toString();
    }
    
    public String getRequestURI() {
    	HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
    	return request.getRequestURI();
    }
    
    public String getHostname() {
    	return Uteis.realizarCapturaHostname();
    }
    
    public String getIdCliente() {
    	return getConfiguracaoGeralPadraoSistema().getIdAutenticacaoServOtimize();
    }
    
    public Date getDataAutenticacao() {
    	try {
    		AplicacaoControle aplicacaoControle = this.getControladorAplicacaoControle("AplicacaoControle");
    		return aplicacaoControle.getAutenticacaoRealizada();
    	} catch (Exception e) {
    		return null;
    	}
    }
    
}