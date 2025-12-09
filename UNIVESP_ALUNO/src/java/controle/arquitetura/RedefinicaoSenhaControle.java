package controle.arquitetura;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;


@Controller("RedefinicaoSenhaControle")
@Scope("viewScope")
@Lazy
public class RedefinicaoSenhaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private String novaSenhaDeNovo;
	private String novaSenha;
	private UsuarioVO usuario;
	private String urlLogoIndexUnidadeEnsino;
	private PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO;

	public RedefinicaoSenhaControle() {
	}
	
	@PostConstruct
	public void inicializarRedefinicaoSenha() {
		inicializar();
	}

	public void inicializar() {
		try {
			String token = (String)((HttpServletRequest)context().getCurrentInstance().getExternalContext().getRequest()).getAttribute("token");
			setPessoaEmailInstitucionalVO(new PessoaEmailInstitucionalVO());
			if (token != null) {
				UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorTokenRedefinirSenha(token);
				setUsuario(usuario);				
				setNovaSenha("");
				setNovaSenhaDeNovo("");
//				if(Uteis.isAtributoPreenchido(getUsuario())) {
//					setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getUsuario().getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuario()));
//				}
			} 
			setMensagemID("");
			setMensagemDetalhada("");
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erroSenhaRedefinir", "Identificar não localizado, favor realizar procedimento novamente!");			
		}
	}

    public void redefinirSenha() {
	try {	
		UsuarioVO usua = getUsuario();
	    if ((getNovaSenha() == null || getNovaSenha().equals("") || getNovaSenhaDeNovo() == null || getNovaSenhaDeNovo().equals(""))) {
	    	throw new ConsistirException("Informe a nova senha para que a redefinição seja concluída.");
	    } else {
			Uteis.validarSenha(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null,null), getNovaSenha());
			usua.setSenha(getNovaSenha());
			if (usua == null || usua.getCodigo().intValue() == 0) {
			    throw new Exception("Nenhum USUÁRIO foi encontrado. Tente novamente mais tarde!");
			} else {
			    getFacadeFactory().getUsuarioFacade().alterarSenha(true, usua);
			    getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, usua, getNovaSenha());
			}
	    }
	    getUsuario().setCodigo(0);
	    limparMensagem();
	} catch (ConsistirException e) {
	    setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
	} catch (Exception e) {
	    setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	}
    }
	
	public String direcionarLogin() {
		limparMensagem();
		return "logout";
	}

	public Boolean getPermiteAlterarSenha() {
		if (getUsuario().getCodigo().intValue() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public UsuarioVO getUsuario() {
		if (usuario == null) {
			usuario = new UsuarioVO();
		}
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}
	
	public String getNovaSenhaDeNovo() {
		if (novaSenhaDeNovo == null) {
			novaSenhaDeNovo = "";
		}
		return novaSenhaDeNovo;
	}

	public void setNovaSenhaDeNovo(String novaSenhaDeNovo) {
		this.novaSenhaDeNovo = novaSenhaDeNovo;
	}

	public String getNovaSenha() {
		if (novaSenha == null) {
			novaSenha = "";
		}
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getUrlLogoIndexUnidadeEnsino() {		
		if (urlLogoIndexUnidadeEnsino != null && !urlLogoIndexUnidadeEnsino.trim().isEmpty()) {
			try {
				return getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), 0).getUrlExternoDownloadArquivo() + "/" + urlLogoIndexUnidadeEnsino;
			} catch (Exception e) {
				return "/resources/imagens/logo.png";
			}
		}
		return "/resources/imagens/logo.png";
	}

	public PessoaEmailInstitucionalVO getPessoaEmailInstitucionalVO() {
		if(pessoaEmailInstitucionalVO == null) {
			pessoaEmailInstitucionalVO =  new PessoaEmailInstitucionalVO();
		}
		return pessoaEmailInstitucionalVO;
	}

	public void setPessoaEmailInstitucionalVO(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO) {
		this.pessoaEmailInstitucionalVO = pessoaEmailInstitucionalVO;
	}
	
	

}
