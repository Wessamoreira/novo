package controle.administrativo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoMobileVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * @author Victor Hugo de Paula Costa - 3 de nov de 2016
 *
 */
@Controller("ConfiguracaoMobileControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoMobileControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 3 de nov de 2016
	 */
	private ConfiguracaoMobileVO configuracaoMobileVO;
	private Boolean permitirUsuarioRealizarUpload;

	public String editar() {
		setConfiguracaoMobileVO((ConfiguracaoMobileVO) context().getExternalContext().getRequestMap().get("itemConfiguracaoMobile"));
		verificarUsuarioPossuiPermissaoRealizarUpload();
		setMensagemID("msg_dados_editar", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoMobileForm.xhtml");
	}

	public String novo() {
		setConfiguracaoMobileVO(new ConfiguracaoMobileVO());
		verificarUsuarioPossuiPermissaoRealizarUpload();
		setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoMobileForm.xhtml");
	}

	public String consultar() {
		try {
			getControleConsulta().getListaConsulta().clear();
			getControleConsulta().setListaConsulta(getFacadeFactory().getConfiguracaoMobileFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (getControleConsulta().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void gravar() {
		try {
			getFacadeFactory().getConfiguracaoMobileFacade().persistir(getConfiguracaoMobileVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getConfiguracaoMobileFacade().excluir(getConfiguracaoMobileVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String inicializarConsultar() {
		getControleConsulta().getListaConsulta().clear();
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoMobileCons.xhtml");
	}

	public void ativar() {
		try {
			getFacadeFactory().getConfiguracaoMobileFacade().alterarSituacaoAtivo(getConfiguracaoMobileVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativar() {
		try {
			getFacadeFactory().getConfiguracaoMobileFacade().alterarSituacaoInativo(getConfiguracaoMobileVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getUploadedFile() != null) {
				getConfiguracaoMobileVO().setCertificadoAPNSApple(uploadEvent.getUploadedFile().getData());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void upLoadArquivoProfessor(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getUploadedFile() != null) {
				getConfiguracaoMobileVO().setCertificadoAPNSAppleProfessor(uploadEvent.getUploadedFile().getData());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void verificarUsuarioPossuiPermissaoRealizarUpload() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioRealizarUpload(getUsuarioLogado(), "PermitirUsuarioRealizarUpload");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPermitirUsuarioRealizarUpload(liberar);
	}

	public static void verificarPermissaoUsuarioRealizarUpload(UsuarioVO usuario, String nomeEntidade) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public List<SelectItem> getCamposConsulta() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("nome", "Nome"));
		objs.add(new SelectItem("codigo", "Código"));
		return objs;
	}

	public ConfiguracaoMobileVO getConfiguracaoMobileVO() {
		if (configuracaoMobileVO == null) {
			configuracaoMobileVO = new ConfiguracaoMobileVO();
		}
		return configuracaoMobileVO;
	}

	public void setConfiguracaoMobileVO(ConfiguracaoMobileVO configuracaoMobileVO) {
		this.configuracaoMobileVO = configuracaoMobileVO;
	}

	public Boolean getPermitirUsuarioRealizarUpload() {
		if (permitirUsuarioRealizarUpload == null) {
			permitirUsuarioRealizarUpload = false;
		}
		return permitirUsuarioRealizarUpload;
	}

	public void setPermitirUsuarioRealizarUpload(Boolean permitirUsuarioRealizarUpload) {
		this.permitirUsuarioRealizarUpload = permitirUsuarioRealizarUpload;
	}
}
