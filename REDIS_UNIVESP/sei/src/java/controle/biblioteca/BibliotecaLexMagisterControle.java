package controle.biblioteca;

import java.io.Serializable;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.Uteis;

@Controller("BibliotecaLexMagisterControle")
@Scope("viewScope")
@Lazy
public class BibliotecaLexMagisterControle  extends SuperControle implements Serializable{

	
	
	private String chaveAcessoBibliotecaLexMagister ;
	private String session_idBibliotecaLexMagister ;
	private String valorHeadBibliotecaLexMagister ;
	
	public BibliotecaLexMagisterControle() {
		
		if(getUsuarioLogado() == null || !Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			getFacadeFactory().getConfiguracaoBibliotecaFacade().realizarNavegacaoParaBibliotecaLexMagister(null);
		}
		if(context().getExternalContext().getSessionMap() != null && context().getExternalContext().getSessionMap().get("HEAD") != null) {
		setChaveAcessoBibliotecaLexMagister(context().getExternalContext().getSessionMap().get("CHAVE").toString());
        setValorHeadBibliotecaLexMagister(context().getExternalContext().getSessionMap().get("HEAD").toString());
        if(Uteis.isAtributoPreenchido(getValorHeadBibliotecaLexMagister()) && Uteis.isAtributoPreenchido(getChaveAcessoBibliotecaLexMagister())) {
        	 setSession_idBibliotecaLexMagister(getUsuarioLogado().getCodigo().toString());
        	  context().getExternalContext().getSessionMap().remove("CHAVE");   
              context().getExternalContext().getSessionMap().remove("HEAD");   
        }       
		}
      
	}

	public String getChaveAcessoBibliotecaLexMagister() {
		return chaveAcessoBibliotecaLexMagister;
	}

	public void setChaveAcessoBibliotecaLexMagister(String chaveAcessoBibliotecaLexMagister) {
		this.chaveAcessoBibliotecaLexMagister = chaveAcessoBibliotecaLexMagister;
	}

	public String getSession_idBibliotecaLexMagister() {
		return session_idBibliotecaLexMagister;
	}

	public void setSession_idBibliotecaLexMagister(String session_idBibliotecaLexMagister) {
		this.session_idBibliotecaLexMagister = session_idBibliotecaLexMagister;
	}

	public String getValorHeadBibliotecaLexMagister() {
		return valorHeadBibliotecaLexMagister;
	}

	public void setValorHeadBibliotecaLexMagister(String valorHeadBibliotecaLexMagister) {
		this.valorHeadBibliotecaLexMagister = valorHeadBibliotecaLexMagister;
	}
	
	
	
	
	

}
