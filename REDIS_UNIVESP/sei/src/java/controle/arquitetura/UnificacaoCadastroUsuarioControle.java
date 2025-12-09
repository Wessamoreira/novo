package controle.arquitetura;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("UnificacaoCadastroUsuarioControle")
@Scope("viewScope")
@Lazy
public class UnificacaoCadastroUsuarioControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private UsuarioVO usuarioVO;
	private String valorConsultaUsuario;
	private String campoConsultaUsuario;
	private List<UsuarioVO> listaConsultaUsuarioVOs;

	private String campoConsultaUsuarioUnificar;
	private String valorConsultaUsuarioUnificar;
	private List<UsuarioVO> listaConsultaUsuarioUnificarVOs;

	public UnificacaoCadastroUsuarioControle() throws Exception {
		removerObjetoMemoria(this);

	}

	public String novo() throws Exception {
		try {
			removerObjetoMemoria(this);
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("unificacaoCadastroUsuario.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("unificacaoCadastroUsuario.xhtml");
		}
	}

	public List<SelectItem> getListaSelectItemSituacaoTipoAdvertencia() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		for (SituacaoTipoAdvertenciaEnum situacaoTipoAdvertenciaEnum : SituacaoTipoAdvertenciaEnum.values()) {
			objs.add(new SelectItem(situacaoTipoAdvertenciaEnum.name(), situacaoTipoAdvertenciaEnum.getValorApresentar()));
		}
		return objs;
	}

	public List<SelectItem> getTipoConsultaComboUsuarioUnificar() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("NOME", "Nome"));
		objs.add(new SelectItem("USER_NAME", "Username"));
		return objs;
	}

	public void consultarUsuarioManter() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaUsuario().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaUsuario().equals("NOME")) {
				if (getValorConsultaUsuario().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getUsuarioFacade().consultarPorNome(getValorConsultaUsuario(), getUnidadeEnsinoLogado().getCodigo(), null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaUsuario().equals("USER_NAME")) {
				if (getValorConsultaUsuario().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getUsuarioFacade().consultarPorUsername(getValorConsultaUsuario(), getUnidadeEnsinoLogado().getCodigo(), null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaUsuarioVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaUsuarioVOs(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarUsuarioManter() throws Exception {
		try {
			UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItens");
			setUsuarioVO(obj);
			setValorConsultaUsuario("");
			setCampoConsultaUsuario("");
			getListaConsultaUsuarioVOs().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarUsuarioUnificacao() {
		try {
			setListaConsultaUsuarioUnificarVOs(getFacadeFactory().getUsuarioFacade().consultarUsuarioUnificacao(getUsuarioVO(), getCampoConsultaUsuarioUnificar(), getValorConsultaUsuarioUnificar(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaUsuarioUnificarVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosUsuario() {
		setUsuarioVO(new UsuarioVO());
		getListaConsultaUsuarioUnificarVOs().clear();
	}

	public void realizarUnificacaoUsuario() {
		try {
			getFacadeFactory().getUsuarioFacade().realizarUnificacaoUsuario(getListaConsultaUsuarioUnificarVOs(), getUsuarioVO(), getUsuarioLogado());
			getListaConsultaUsuarioUnificarVOs().clear();
			setMensagemID("msg_dados_unificadosComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


	public String getValorConsultaUsuario() {
		if (valorConsultaUsuario == null) {
			valorConsultaUsuario = "";
		}
		return valorConsultaUsuario;
	}

	public void setValorConsultaUsuario(String valorConsultaUsuario) {
		this.valorConsultaUsuario = valorConsultaUsuario;
	}

	public String getCampoConsultaUsuario() {
		if (campoConsultaUsuario == null) {
			campoConsultaUsuario = "";
		}
		return campoConsultaUsuario;
	}

	public void setCampoConsultaUsuario(String campoConsultaUsuario) {
		this.campoConsultaUsuario = campoConsultaUsuario;
	}

	public List<UsuarioVO> getListaConsultaUsuarioVOs() {
		if (listaConsultaUsuarioVOs == null) {
			listaConsultaUsuarioVOs = new ArrayList<UsuarioVO>(0);
		}
		return listaConsultaUsuarioVOs;
	}

	public void setListaConsultaUsuarioVOs(List<UsuarioVO> listaConsultaUsuarioVOs) {
		this.listaConsultaUsuarioVOs = listaConsultaUsuarioVOs;
	}

	public String getCampoConsultaUsuarioUnificar() {
		if (campoConsultaUsuarioUnificar == null) {
			campoConsultaUsuarioUnificar = "";
		}
		return campoConsultaUsuarioUnificar;
	}

	public void setCampoConsultaUsuarioUnificar(String campoConsultaUsuarioUnificar) {
		this.campoConsultaUsuarioUnificar = campoConsultaUsuarioUnificar;
	}

	public String getValorConsultaUsuarioUnificar() {
		if (valorConsultaUsuarioUnificar == null) {
			valorConsultaUsuarioUnificar = "";
		}
		return valorConsultaUsuarioUnificar;
	}

	public void setValorConsultaUsuarioUnificar(String valorConsultaUsuarioUnificar) {
		this.valorConsultaUsuarioUnificar = valorConsultaUsuarioUnificar;
	}

	public List<UsuarioVO> getListaConsultaUsuarioUnificarVOs() {
		if (listaConsultaUsuarioUnificarVOs == null) {
			listaConsultaUsuarioUnificarVOs = new ArrayList<UsuarioVO>(0);
		}
		return listaConsultaUsuarioUnificarVOs;
	}

	public void setListaConsultaUsuarioUnificarVOs(List<UsuarioVO> listaConsultaUsuarioUnificarVOs) {
		this.listaConsultaUsuarioUnificarVOs = listaConsultaUsuarioUnificarVOs;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
}
