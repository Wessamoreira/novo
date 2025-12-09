package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ParametroValeTransporteVO;
import negocio.comuns.recursoshumanos.ParametroValeTransporteVO.EnumCampoConsultaParametroValeTransporte;
import negocio.comuns.recursoshumanos.TipoTransporteVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTarifaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("ParametroValeTransporteControle")
@Scope("viewScope")
@Lazy
public class ParametroValeTransporteControle extends SuperControle {

	private static final long serialVersionUID = -6499489579795786309L;

	private final String TELA_FORM = "parametroValeTransporteForm";
	private final String TELA_CONS = "parametroValeTransporteCons";
	private final String CONTEXT_PARA_EDICAO = "itemParametroValeTransporte";

	private ParametroValeTransporteVO parametroValeTransporte;

	private List<SelectItem> tipoLinhasTransporte;
	private List<SelectItem> tipoTarifas;
	private List<SelectItem> situacoes;
	
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;

	private String valorConsultaSituacao;
	
	private ParametroValeTransporteVO parametroValeTransporteEdicao;
	
	public ParametroValeTransporteControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		inicializarTipoLinhaTransporte();
		inicializarTiposTarifas();
		getParametroValeTransporte().setSituacao(AtivoInativoEnum.ATIVO);
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			ParametroValeTransporteVO obj = (ParametroValeTransporteVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setParametroValeTransporte(obj);
			getParametroValeTransporte().setHistoricoVOs(getFacadeFactory().getParametroValeTransporteHistoricoInterfaceFacade().consultarPorParametroValeTransporte(obj.getCodigo()));
			//Clona o objeto para persistir no historico
			setParametroValeTransporteEdicao( (ParametroValeTransporteVO) SerializationUtils.clone(obj));
			setControleConsultaOtimizado(new DataModelo());
			inicializarTipoLinhaTransporte();
			inicializarTiposTarifas();
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getParametroValeTransporteInterfaceFacade().persistir(getParametroValeTransporte(), getParametroValeTransporteEdicao(), false, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());

			setParametroValeTransporteEdicao(new ParametroValeTransporteVO());
			setParametroValeTransporteEdicao( (ParametroValeTransporteVO) SerializationUtils.clone(getParametroValeTransporte()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public void consultarDados() {
		try {

			if (getControleConsultaOtimizado().getCampoConsulta().equals(EnumCampoConsultaParametroValeTransporte.CODIGO.name())) {
				if (getControleConsultaOtimizado().getValorConsulta().trim().isEmpty()
						|| !Uteis.getIsValorNumerico(getControleConsultaOtimizado().getValorConsulta())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_TextoPadrao_consultaCodigo"));
				}
			}

			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getFacadeFactory().getParametroValeTransporteInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), getValorConsultaSituacao());

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getValorConsultaEvento());
			} 

			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(campoConsultaEvento, valorConsultaEvento,"ATIVO", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(getParametroValeTransporte().getEventoFolhaPagamento().getIdentificador())) {
				getParametroValeTransporte().setEventoFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(getParametroValeTransporte().getEventoFolhaPagamento().getIdentificador(), false, getUsuarioLogado()));
			}
		} catch (Exception e) {
			getParametroValeTransporte().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getParametroValeTransporteInterfaceFacade().excluir(getParametroValeTransporte(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Altera o status da situacao para Inativo.
	 * 
	 * @return
	 */
	public String inativar() {
		try {
			getParametroValeTransporte().setSituacao(AtivoInativoEnum.INATIVO);
			getFacadeFactory().getParametroValeTransporteInterfaceFacade().inativar(getParametroValeTransporte(), false, getUsuarioLogado());
			setMensagemID("msg_dados_inativado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	/**
	 * Altera o status da situacao para ativo.
	 * 
	 * @return
	 */
	public String ativar() {
		try {
			getParametroValeTransporte().setSituacao(AtivoInativoEnum.ATIVO);
			getFacadeFactory().getParametroValeTransporteInterfaceFacade().inativar(getParametroValeTransporte(), false, getUsuarioLogado());
			setMensagemID("msg_dados_inativado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}
	
	public void limparDadosEvento() {
		getParametroValeTransporte().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaParametroValeTransporte.DESCRICAO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Consulta Todos os tipos das linhas de tranporte
	 */
	private void inicializarTipoLinhaTransporte() {
		try {
			getTipoLinhasTransporte().clear();
			List<TipoTransporteVO> lista = getFacadeFactory().getTipoTransporteInterfaceFacade().consultarTipoTransporte(new DataModelo(), "descricao");
			List<SelectItem> tiposLinhaTransporte = new ArrayList<>();
			tiposLinhaTransporte.add(new SelectItem("", ""));
			for (TipoTransporteVO tipoTransporteVO : lista) {
				tiposLinhaTransporte.add(new SelectItem(tipoTransporteVO.getCodigo(), tipoTransporteVO.getDescricao()));
			}
			setTipoLinhasTransporte(tiposLinhaTransporte);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void inicializarTiposTarifas() {
		try {
			getTipoTarifas().clear();
			setTipoTarifas(TipoTarifaEnum.getValorTipoTarifaEnum());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		getParametroValeTransporte().setEventoFolhaPagamento(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	}

	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public boolean validarAlteracaoValor() {
		return getFacadeFactory().getParametroValeTransporteInterfaceFacade().validarAlteracaoValor(
				getParametroValeTransporte().getValor(), getParametroValeTransporteEdicao().getValor());
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}

	public ParametroValeTransporteVO getParametroValeTransporte() {
		if (parametroValeTransporte == null) {
			parametroValeTransporte = new ParametroValeTransporteVO();
		}
		return parametroValeTransporte;
	}

	public void setParametroValeTransporte(ParametroValeTransporteVO parametroValeTransporte) {
		this.parametroValeTransporte = parametroValeTransporte;
	}

	public List<SelectItem> getTipoLinhasTransporte() {
		if (tipoLinhasTransporte == null) {
			tipoLinhasTransporte = new ArrayList<>();
		}
		return tipoLinhasTransporte;
	}

	public void setTipoLinhasTransporte(List<SelectItem> tipoLinhasTransporte) {
		this.tipoLinhasTransporte = tipoLinhasTransporte;
	}

	public List<SelectItem> getTipoTarifas() {
		if (tipoTarifas == null) {
			tipoTarifas = new ArrayList<>();
		}
		return tipoTarifas;
	}

	public void setTipoTarifas(List<SelectItem> tipoTarifas) {
		this.tipoTarifas = tipoTarifas;
	}

	public List<SelectItem> getSituacoes() {
		if (situacoes == null) {
			situacoes = new ArrayList<>();
		}
		return situacoes;
	}

	public void setSituacoes(List<SelectItem> situacoes) {
		this.situacoes = situacoes;
	}

	public String getCampoConsultaEvento() {
		if (campoConsultaEvento == null) {
			campoConsultaEvento = "";
		}
		return campoConsultaEvento;
	}

	public void setCampoConsultaEvento(String campoConsultaEvento) {
		this.campoConsultaEvento = campoConsultaEvento;
	}

	public String getValorConsultaEvento() {
		if (valorConsultaEvento == null) {
			valorConsultaEvento = "";
		}
		return valorConsultaEvento;
	}

	public void setValorConsultaEvento(String valorConsultaEvento) {
		this.valorConsultaEvento = valorConsultaEvento;
	}

	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		if (listaEventosFolhaPagamento == null) {
			listaEventosFolhaPagamento = new ArrayList<>();
		}
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
	}

	public String getValorConsultaSituacao() {
		if (valorConsultaSituacao == null) {
			valorConsultaSituacao = "";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public ParametroValeTransporteVO getParametroValeTransporteEdicao() {
		if (parametroValeTransporteEdicao == null) {
			parametroValeTransporteEdicao = new ParametroValeTransporteVO();
		}
		return parametroValeTransporteEdicao;
	}

	public void setParametroValeTransporteEdicao(ParametroValeTransporteVO parametroValeTransporteEdicao) {
		this.parametroValeTransporteEdicao = parametroValeTransporteEdicao;
	}
}
