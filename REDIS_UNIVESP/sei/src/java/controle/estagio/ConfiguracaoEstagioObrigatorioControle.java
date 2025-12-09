package controle.estagio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.blackboard.FonteDeDadosBlackboardVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioFuncionarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("ConfiguracaoEstagioObrigatorioControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoEstagioObrigatorioControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 646282313726893673L;
	private static final String TELA_FORM = "configuracaoEstagioObrigatorioForm.xhtml";
	private ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioVO;
	private ConfiguracaoEstagioObrigatorioFuncionarioVO configuracaoEstagioObrigatorioFuncionarioVO;

	private String identificacaoConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private List<SelectItem> tipoSelectItemFuncionario;
	private List<SelectItem> listaSelectFonteDeDadosBlackboard;

	@PostConstruct
	public void carregarDadosConfiguracao() {
		ConfiguracaoEstagioObrigatorioVO obj = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
		if (Uteis.isAtributoPreenchido(obj)) {
			setConfiguracaoEstagioObrigatorioVO(obj);
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} else {
			setConfiguracaoEstagioObrigatorioVO(new ConfiguracaoEstagioObrigatorioVO());
			setMensagemID(MSG_TELA.msg_entre_dados.name());
		}
		montarListaSelectFonteDeDadosBlackboard();
	}

	public void persistir() {
		try {
			getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().persistir(getConfiguracaoEstagioObrigatorioVO(), true, getUsuarioLogado());
			getAplicacaoControle().setConfiguracaoEstagioObrigatorioPadraoVO(getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadraoUnico(false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().excluir(getConfiguracaoEstagioObrigatorioVO(), true, getUsuarioLogado());
			setConfiguracaoEstagioObrigatorioVO(new ConfiguracaoEstagioObrigatorioVO());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("NOME")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("MATRICULA")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, null, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CARGO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void selecionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			switch (getIdentificacaoConsultaFuncionario()) {
			case "FuncionarioTestemunha1":
				getConfiguracaoEstagioObrigatorioVO().setFuncionarioTestemunhaAssinatura1(obj);
				break;
			case "FuncionarioTestemunha2":
				getConfiguracaoEstagioObrigatorioVO().setFuncionarioTestemunhaAssinatura2(obj);
				break;
			case "FuncionarioDocente":
				getConfiguracaoEstagioObrigatorioFuncionarioVO().setFuncionarioVO(obj);
				getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().adicionarConfiguracaoEstagioObrigatorioFuncionarioVO(getConfiguracaoEstagioObrigatorioVO(), getConfiguracaoEstagioObrigatorioFuncionarioVO(), getUsuarioLogadoClone());
				setConfiguracaoEstagioObrigatorioFuncionarioVO(new ConfiguracaoEstagioObrigatorioFuncionarioVO());
				break;
			default:
				break;
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarConsultaFuncionarioTestemunhaAssinatura1() {
		try {
			setIdentificacaoConsultaFuncionario("FuncionarioTestemunha1");
			setCampoConsultaFuncionario("NOME");
			setValorConsultaFuncionario("");
			setListaConsultaFuncionario(new ArrayList<>());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparFuncionarioTestemunhaAssinatura1() {
		getConfiguracaoEstagioObrigatorioVO().setFuncionarioTestemunhaAssinatura1(new FuncionarioVO());
	}

	public void inicializarConsultaFuncionarioTestemunhaAssinatura2() {
		try {
			setIdentificacaoConsultaFuncionario("FuncionarioTestemunha2");
			setCampoConsultaFuncionario("NOME");
			setValorConsultaFuncionario("");
			setListaConsultaFuncionario(new ArrayList<>());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparFuncionarioTestemunhaAssinatura2() {
		getConfiguracaoEstagioObrigatorioVO().setFuncionarioTestemunhaAssinatura2(new FuncionarioVO());
	}
	
	public void inicializarConsultaFuncionarioDocente() {
		try {
			setIdentificacaoConsultaFuncionario("FuncionarioDocente");
			setCampoConsultaFuncionario("NOME");
			setValorConsultaFuncionario("");
			setListaConsultaFuncionario(new ArrayList<>());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void removerConfiguracaoEstagioObrigatorioFuncionarioVO() {
		try {
			ConfiguracaoEstagioObrigatorioFuncionarioVO obj = (ConfiguracaoEstagioObrigatorioFuncionarioVO) context().getExternalContext().getRequestMap().get("ceoFuncionario");
			getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().removerConfiguracaoEstagioObrigatorioFuncionarioVO(getConfiguracaoEstagioObrigatorioVO(), obj, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void montarListaSelectFonteDeDadosBlackboard() {
		try {
			List<FonteDeDadosBlackboardVO> resultadoConsulta = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().realizarConsultaFonteDeDadosBlackboardVO(getUsuarioLogado());
			setListaSelectFonteDeDadosBlackboard(UtilSelectItem.getListaSelectItem(resultadoConsulta, "id", "externalId"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public ConfiguracaoEstagioObrigatorioVO getConfiguracaoEstagioObrigatorioVO() {
		if (configuracaoEstagioObrigatorioVO == null) {
			configuracaoEstagioObrigatorioVO = new ConfiguracaoEstagioObrigatorioVO();
		}
		return configuracaoEstagioObrigatorioVO;
	}

	public void setConfiguracaoEstagioObrigatorioVO(ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioVO) {
		this.configuracaoEstagioObrigatorioVO = configuracaoEstagioObrigatorioVO;
	}
	

	public ConfiguracaoEstagioObrigatorioFuncionarioVO getConfiguracaoEstagioObrigatorioFuncionarioVO() {
		if (configuracaoEstagioObrigatorioFuncionarioVO == null) {
			configuracaoEstagioObrigatorioFuncionarioVO = new ConfiguracaoEstagioObrigatorioFuncionarioVO();
		}
		return configuracaoEstagioObrigatorioFuncionarioVO;
	}

	public void setConfiguracaoEstagioObrigatorioFuncionarioVO(ConfiguracaoEstagioObrigatorioFuncionarioVO configuracaoEstagioObrigatorioFuncionarioVO) {
		this.configuracaoEstagioObrigatorioFuncionarioVO = configuracaoEstagioObrigatorioFuncionarioVO;
	}

	public String getIdentificacaoConsultaFuncionario() {
		if (identificacaoConsultaFuncionario == null) {
			identificacaoConsultaFuncionario = "";
		}
		return identificacaoConsultaFuncionario;
	}

	public void setIdentificacaoConsultaFuncionario(String identificacaoConsultaFuncionario) {
		this.identificacaoConsultaFuncionario = identificacaoConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<>();
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}
	
	public List<SelectItem> getListaSelectFonteDeDadosBlackboard() {
		if (listaSelectFonteDeDadosBlackboard == null) {
			listaSelectFonteDeDadosBlackboard = new ArrayList<SelectItem>(0);
		}
		return listaSelectFonteDeDadosBlackboard;
	}

	public void setListaSelectFonteDeDadosBlackboard(List<SelectItem> listaSelectFonteDeDadosBlackboard) {
		this.listaSelectFonteDeDadosBlackboard = listaSelectFonteDeDadosBlackboard;
	}
	
	
	public List<SelectItem> getTipoSelectItemFuncionario() {
		if (tipoSelectItemFuncionario == null) {
			tipoSelectItemFuncionario = new ArrayList<SelectItem>(0);
			tipoSelectItemFuncionario.add(new SelectItem("NOME", "Nome"));
			tipoSelectItemFuncionario.add(new SelectItem("MATRICULA", "Matrícula"));
			tipoSelectItemFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoSelectItemFuncionario.add(new SelectItem("CARGO", "Cargo"));
			tipoSelectItemFuncionario.add(new SelectItem("UNIDADEENSINO", "Unidade de Ensino"));
		}
		return tipoSelectItemFuncionario;
	}

	public void setTipoSelectItemFuncionario(List<SelectItem> tipoSelectItemFuncionario) {
		this.tipoSelectItemFuncionario = tipoSelectItemFuncionario;
	}	

}
