package controle.recursoshumanos;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.script.ScriptEngine;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO.EnumCampoConsultaCompetencia;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.ContraChequeVO.EnumCampoConsultaContraCheque;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FichaFinanceiraRelVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.recursoshumanos.CalculoContraCheque;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@SuppressWarnings("unchecked")
@Controller("ContraChequeControle")
@Scope("viewScope")
@Lazy
public class ContraChequeControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 5525151484180001369L;

	private static final String TELA_FORM = "contraChequeForm";
	private static final String TELA_CONS = "fichaFinanceiraCons";
	private static final String CONTEXT_PARA_EDICAO = "itemContraCheque";

	private ContraChequeVO contraCheque;
	private ContraChequeEventoVO contraChequeEvento;
	private EventoFolhaPagamentoVO eventoFolhaPagamento;

	private List<FuncionarioCargoVO> funcionarioCargoVOs;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;

	private List<CompetenciaFolhaPagamentoVO> competenciaFolhaPagamentoVOs;
	private String campoConsultaCompetencia;
	private String valorConsultaCompetencia;

	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	
	private Date dataInicial;
	private Date dataFinal;

	private List<SelectItem> listaSelectItemCompetenciaPeriodo;
	private List<SelectItem> listaSelectItemTipoRecebimento;
	private List<SelectItem> listaSelectItemPrevidencia;
	
	private List<ContraChequeEventoVO> contraChequeEventos;
	
	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamento;
	
	private Long codigoCompetenciaPeriodoAnterior;
	
	public ContraChequeControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
		montarListaSelectItemTipoRecebimento();
	}
	
	@PostConstruct
	public void carregarFichaFinanceiraVindoDeOutraTela () {
		
		FuncionarioCargoVO funcionarioCargoVO = (FuncionarioCargoVO) context().getExternalContext().getSessionMap().get("funcionarioCargo");
		CompetenciaFolhaPagamentoVO competencia = (CompetenciaFolhaPagamentoVO) context().getExternalContext().getSessionMap().get("competenciaFolhaPagamento");
		
		try {
			if (Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
				funcionarioCargoVO = (FuncionarioCargoVO) getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(funcionarioCargoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setContraCheque(getFacadeFactory().getContraChequeInterfaceFacade().consultarPorCodigoECompetenciaAtiva(funcionarioCargoVO, false, getUsuarioLogado()));
				editar(getContraCheque());
				
			} else if (Uteis.isAtributoPreenchido(competencia)) {
			
				montarCompetenciaPeriodo(competencia);
				setDataInicial(competencia.getDataCompetencia());
				setDataFinal(competencia.getDataCompetencia());
				CompetenciaPeriodoFolhaPagamentoVO periodo = (CompetenciaPeriodoFolhaPagamentoVO) context().getExternalContext().getSessionMap().get("periodoFolhaPagamento");
				getContraCheque().setPeriodo(periodo);
				consultarDados();
			}
		} catch (Exception e) {
			setMensagemID(e.getMessage());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("funcionarioCargo");
			context().getExternalContext().getSessionMap().remove("competenciaFolhaPagamento");
			context().getExternalContext().getSessionMap().remove("periodoFolhaPagamento");
		}
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Metodo que carrega os dados para edição vindo da ficha Financeira.
	 * 
	 * @return
	 */
	public String editar() {
		try {
			ContraChequeVO obj = (ContraChequeVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			if (Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCargoAtual().getCodigo())) {
				obj.getFuncionarioCargo().setCargoAtual(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargo().getCargoAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setContraCheque(obj);
			obj.setCompetenciaFolhaPagamento(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getCompetenciaFolhaPagamento().getCodigo().longValue()));
			montarCompetenciaPeriodo(obj.getCompetenciaFolhaPagamento());
			selecionarCompetenciaPeriodo();
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Metodo que carrega os dados para edição vindo do funcionario cargo.
	 * 
	 * @param obj {@link ContraChequeVO}
	 */
	public void editar(ContraChequeVO obj) {
		try {
			montarCompetenciaPeriodo(obj.getCompetenciaFolhaPagamento());
			getContraCheque().setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarPorContraCheque(obj.getCodigo(), false, getUsuarioLogado()));
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarEvento() {
		try {
			ContraChequeEventoVO obj =(ContraChequeEventoVO) context().getExternalContext().getRequestMap().get("contrachequeEvento");
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setContraChequeEvento( (ContraChequeEventoVO) SerializationUtils.clone(obj));
        	setEventoFolhaPagamento(obj.getEventoFolhaPagamento());
        	getContraChequeEvento().setInformadoManual(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getContraChequeInterfaceFacade().persistir(getContraCheque(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
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

	public void scrollerListenerEvento(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarEvento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerFuncionario(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarFuncionario();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			
			if(Uteis.isAtributoPreenchido(getDataInicial()) && Uteis.isAtributoPreenchido(getDataFinal())) {
				getFacadeFactory().getContraChequeInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), getDataInicial(), getDataFinal());				
			} else {
				throw new ConsistirException(UteisJSF.internacionalizar("javax.faces.converter.DateTimeConverter.DATE"));
			}
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getContraChequeInterfaceFacade().excluir(getContraCheque(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
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

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaContraCheque.FUNCIONARIO.name());
		setListaConsulta(new ArrayList<ContraChequeVO>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		
		try {
			setCompetenciaFolhaPagamento(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true));
			setDataInicial(getCompetenciaFolhaPagamento().getDataCompetencia());
			setDataFinal(getCompetenciaFolhaPagamento().getDataCompetencia());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Metodo que recalcula a folha de pagamento selecionada.
	 */
	public void recalcularFolhaPagamento() {
		try {
			ScriptEngine engine = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inicializaEngineFormula();

			atualizarListaDeEventos();
			getContraCheque().setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(getContraCheque().getFuncionarioCargo().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFacadeFactory().getContraChequeInterfaceFacade().realizarRecalculoContraChequeDoFuncionario(getContraCheque(), listaDeEventosDoContraChequeDoFuncionario(), inicializarDadosDoCalculoContraCheque(), getUsuarioLogado(), engine, contraCheque.getTemplateLancamentoFolhaPagamentoVO().getLancamentoFolhaPagamento());

			//getContraCheque().setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarPorContraCheque(getContraCheque().getCodigo(), false, getUsuarioLogado()));
			//getContraCheque().setPeriodo(new CompetenciaPeriodoFolhaPagamentoVO());
			this.selecionarCompetenciaPeriodo();
			setMensagemID("msg_LancamentoFolhaPagamento_folhaGeradaComSucesso");
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	
	/**
	 * Atualiza os eventos da folha de pagamento da lista de Contra Cheque Eventos  
	 */
	private void atualizarListaDeEventos() {
		
		if(!getContraCheque().getContraChequeEventos().isEmpty()) {
			
			EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
			
			Iterator<ContraChequeEventoVO> iterator = getContraCheque().getContraChequeEventos().iterator();

			while(iterator.hasNext()) {
				ContraChequeEventoVO contraChequeEventoVO = iterator.next();
				
				try {
					eventoFolhaPagamentoVO = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(contraChequeEventoVO.getEventoFolhaPagamento().getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS);
					eventoFolhaPagamentoVO.setValorInformado(contraChequeEventoVO.getEventoFolhaPagamento().getValorInformado());
					eventoFolhaPagamentoVO.setValorTemporario(Uteis.isAtributoPreenchido(contraChequeEventoVO.getValorReferencia()) ? contraChequeEventoVO.getValorReferencia(): contraChequeEventoVO.getEventoFolhaPagamento().getValorTemporario());

					contraChequeEventoVO.setEventoFolhaPagamento(eventoFolhaPagamentoVO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private CalculoContraCheque inicializarDadosDoCalculoContraCheque() {
		return CalculoContraCheque.inicializarCalculoContraCheque(getContraCheque().getFuncionarioCargo(), getContraCheque().getCompetenciaFolhaPagamento(), getContraCheque().getTemplateLancamentoFolhaPagamentoVO().getLancamentoFolhaPagamento());
	}

	private List<EventoFolhaPagamentoVO> listaDeEventosDoContraChequeDoFuncionario() {
		 return (List<EventoFolhaPagamentoVO>) getContraCheque().montarDadosEventosPreenchidosDoContraChequeEvento();
	}
	
	/**
	 * Seleciona o funcionario cargo pesquisado.
	 */
	public void selecionarFuncionarioCargo() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem");
		this.getContraCheque().setFuncionarioCargo(obj);
		
		montarCompetenciaPeriodo(getContraCheque().getCompetenciaFolhaPagamento());

		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";
		getFuncionarioCargoVOs().clear();
	}
	
	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionario() {
		try {
			List<FuncionarioCargoVO> objs = new ArrayList<FuncionarioCargoVO>(0);
			
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				setListaConsulta(new ArrayList<FuncionarioCargoVO>(0));
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionario(getControleConsultaOtimizado(), getValorConsultaFuncionario(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getValorConsultaFuncionario(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setFuncionarioCargoVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioPorMatricula() {
		try {
			if (Uteis.isAtributoPreenchido(getContraCheque().getFuncionarioCargo().getMatriculaCargo())) {
				FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getContraCheque().getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setContraCheque(new ContraChequeVO());
				getContraCheque().setFuncionarioCargo(funcionarioCargo);

				if (Uteis.isAtributoPreenchido(funcionarioCargo.getCargoAtual().getCodigo())) {
					getContraCheque().getFuncionarioCargo().setCargoAtual(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getContraCheque().getFuncionarioCargo().getCargoAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
				montarCompetenciaPeriodo(getContraCheque().getCompetenciaFolhaPagamento());
				consultarPorFuncionarioCargoEPeriodo();
				setMensagemID("msg_entre_dados");
			}
		} catch (Exception e) {
			getContraCheque().setFuncionarioCargo(new FuncionarioCargoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarPorFuncionarioCargoEPeriodo() {
		try {
			if (Uteis.isAtributoPreenchido(getContraCheque())) {
				atualizarListaDeEventosPorPeriodo();
			} else {
				FuncionarioCargoVO funcionarioCargoVO = getContraCheque().getFuncionarioCargo();
				CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO = getContraCheque().getCompetenciaFolhaPagamento();
				setContraCheque(new ContraChequeVO());
				getContraCheque().setContraChequeEventos(new ArrayList<>());
				getContraCheque().setFuncionarioCargo(funcionarioCargoVO);
				getContraCheque().setCompetenciaFolhaPagamento(competenciaFolhaPagamentoVO);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void atualizarListaDeEventosPorPeriodo() throws Exception {
		getContraCheque().setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarDados(getContraCheque(),getContraCheque().getPeriodo()));
		
		BigDecimal totalProventos = BigDecimal.ZERO;
		BigDecimal totalDescontos = BigDecimal.ZERO;
		
		if(Uteis.isAtributoPreenchido(getContraCheque().getContraChequeEventos())) {

			for(ContraChequeEventoVO contraChequeEvento : getContraCheque().getContraChequeEventos()) {
				
				if(contraChequeEvento.getPeriodo().getCodigo().equals(getContraCheque().getPeriodo().getCodigo()) ||
						!Uteis.isAtributoPreenchido(getContraCheque().getPeriodo().getCodigo())) {
					
					switch (TipoLancamentoFolhaPagamentoEnum.valueOf(contraChequeEvento.getEventoFolhaPagamento().getTipoLancamento().getValor())) {
					case PROVENTO:
						totalProventos = totalProventos.add(contraChequeEvento.recuperarValorDoEventoTratado());
						break;
					case DESCONTO:
						totalDescontos = totalDescontos.add(contraChequeEvento.recuperarValorDoEventoTratado());
						break;
					default:
						break;
					}
				}
			}
		}
		
		getContraCheque().setTotalProvento(totalProventos);
		getContraCheque().setTotalDesconto(totalDescontos);
		getContraCheque().setTotalReceber(totalProventos.subtract(totalDescontos));
		
		setControleConsultaOtimizado(new DataModelo());
	}

	/**
	 * Operacao que libera todos os recursos (atributos, listas, objetos) do backing
	 * bean. Garantindo uma melhor atuacao do Garbage Coletor do Java. A mesma e
	 * automaticamente quando realiza o logout.
	 */
	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
	}
	
	public void limparDadosFuncionario () {
		getContraCheque().setFuncionarioCargo(new FuncionarioCargoVO());
		setValorConsultaFuncionario("");
	}
	
	public void limparDadosCompetencia() {
		getContraCheque().setCompetenciaFolhaPagamento(new CompetenciaFolhaPagamentoVO());
		setListaSelectItemCompetenciaPeriodo(new ArrayList<SelectItem>());
		FuncionarioCargoVO funcionarioCargoVO = (FuncionarioCargoVO) SerializationUtils.clone(getContraCheque().getFuncionarioCargo());
		setContraCheque(new ContraChequeVO());
		getContraCheque().setFuncionarioCargo(funcionarioCargoVO);
		setValorConsultaCompetencia("");
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	/**
	 * Seleciona a competencia pesquisada.
	 * 
	 * @throws Exception 
	 */
	public void selecionarCompetencia() {
		try {
			CompetenciaFolhaPagamentoVO competencia = (CompetenciaFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("competenciaItem");
			setContraCheque(getFacadeFactory().getContraChequeInterfaceFacade().consultarPorFuncionarioCargoECompetencia(getContraCheque().getFuncionarioCargo(), competencia));
			getContraCheque().setCompetenciaFolhaPagamento(competencia);

			if (Uteis.isAtributoPreenchido(getContraCheque().getFuncionarioCargo().getCodigo())) {
				getContraCheque().setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(
						getContraCheque().getFuncionarioCargo().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}

			montarCompetenciaPeriodo(getContraCheque().getCompetenciaFolhaPagamento());
			getContraCheque().setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarPorContraCheque(getContraCheque().getCodigo(), false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), "msg_erro_LancamentoFolhaPagamento_competenciaAtivaNaoEncontrado");
		}
	}

	public void selecionarCompetenciaChange() {
		try {
			FuncionarioCargoVO funcionarioCargoVO = getContraCheque().getFuncionarioCargo();
			setCodigoCompetenciaPeriodoAnterior(Long.valueOf(getContraCheque().getPeriodo().getCodigo()));
			
			CompetenciaFolhaPagamentoVO competencia = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaFolhaPagamentoPorMesAno(
					UteisData.getMesData(this.getContraCheque().getCompetenciaFolhaPagamento().getDataCompetencia()),
					UteisData.getAnoData(this.getContraCheque().getCompetenciaFolhaPagamento().getDataCompetencia()), false, getUsuarioLogado());
			setContraCheque(getFacadeFactory().getContraChequeInterfaceFacade().consultarPorFuncionarioCargoECompetencia(getContraCheque().getFuncionarioCargo(), competencia));
			getContraCheque().setCompetenciaFolhaPagamento(competencia);

			getContraCheque().setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarPorContraCheque(getContraCheque().getCodigo(), false, getUsuarioLogado()));
			
			if (Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
				getContraCheque().setFuncionarioCargo(funcionarioCargoVO);
			}
			montarCompetenciaPeriodo(getContraCheque().getCompetenciaFolhaPagamento());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), "msg_erro_LancamentoFolhaPagamento_competenciaAtivaNaoEncontrado");
		}
	}

	public void selecionarCompetenciaPeriodo() {
		try {
			getContraCheque().setContraChequeEventos(getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarPorContraCheque(getContraCheque().getCodigo(), false, getUsuarioLogado()));
			consultarPorFuncionarioCargoEPeriodo();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), "msg_erro_LancamentoFolhaPagamento_competenciaAtivaNaoEncontrado");
		}
	}

	public void consultarCompetencia() {
		try {

			if (getCampoConsultaCompetencia().equals(EnumCampoConsultaCompetencia.CODIGO.name())) {
				if (getValorConsultaCompetencia().trim().isEmpty() || !Uteis.getIsValorNumerico(getValorConsultaCompetencia())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_SecaoFolhaPagamento_ConsultaCampoCodigoInvalido"));
				}
			}

			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getControleConsultaOtimizado().setValorConsulta(getValorConsultaCompetencia());
			getControleConsultaOtimizado().setCampoConsulta(getCampoConsultaCompetencia());
			getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), null);

			setCompetenciaFolhaPagamentoVOs((List<CompetenciaFolhaPagamentoVO>) getControleConsultaOtimizado().getListaConsulta());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void montarCompetenciaPeriodo(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		try {
			List<CompetenciaPeriodoFolhaPagamentoVO> periodos = getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorCompetenciaFolhaPagamento(competenciaFolhaPagamentoVO);
			getListaSelectItemCompetenciaPeriodo().clear();
			for (CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO : periodos) {
				getListaSelectItemCompetenciaPeriodo().add(new SelectItem(competenciaPeriodoFolhaPagamentoVO.getCodigo(), competenciaPeriodoFolhaPagamentoVO.getPeriodoApresentacao()));
			}
			
			getListaSelectItemCompetenciaPeriodo().add(new SelectItem("", "Todos"));
			if (Uteis.isAtributoPreenchido(getListaSelectItemCompetenciaPeriodo())) {
				if (Uteis.isAtributoPreenchido(getListaSelectItemCompetenciaPeriodo().get(0).getValue()) 
						&& !Uteis.isAtributoPreenchido(getCodigoCompetenciaPeriodoAnterior())) {
					getContraCheque().setPeriodo(
							getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(Long.valueOf(getListaSelectItemCompetenciaPeriodo().get(0).getValue().toString())));
				} else {
					//monta o mesmo período da competencia que estava selecionada anteriormente.
					CompetenciaPeriodoFolhaPagamentoVO obj = getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(getCodigoCompetenciaPeriodoAnterior());
					if (Uteis.isAtributoPreenchido(obj)) {
						for (CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO : periodos) {
							if (competenciaPeriodoFolhaPagamentoVO.getPeriodo().equals(obj.getPeriodo())) {
								getContraCheque().setPeriodo(competenciaPeriodoFolhaPagamentoVO);
								consultarPorFuncionarioCargoEPeriodo();
								break;
							}
						}
						
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getCampoConsultaEvento());
			} 
			
			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(campoConsultaEvento, valorConsultaEvento, "ATIVO" , false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(this.eventoFolhaPagamento.getIdentificador())) {
				this.setEventoFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(this.eventoFolhaPagamento.getIdentificador(), false, getUsuarioLogado()));
				getContraChequeEvento().setEventoFolhaPagamento(getEventoFolhaPagamento());
			} else {
				getContraChequeEvento().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
				this.setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			}
			getContraChequeEvento().setItemEmEdicao(Boolean.FALSE);
		} catch (Exception e) {
			this.setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCompetenciaFolhaPagamentoPorDataCompetencia() {
		try {

			this.getContraCheque().setCompetenciaFolhaPagamento(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaFolhaPagamentoPorMesAno(UteisData.getMesData(this.getContraCheque().getCompetenciaFolhaPagamento().getDataCompetencia()), UteisData.getAnoData(this.getContraCheque().getCompetenciaFolhaPagamento().getDataCompetencia()), false, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(this.getContraCheque().getCompetenciaFolhaPagamento())) {
				montarCompetenciaPeriodo(getContraCheque().getCompetenciaFolhaPagamento());
			} else {
				getContraCheque().setContraChequeEventos(new ArrayList<>());
				setListaSelectItemCompetenciaPeriodo(new ArrayList<>());
			}
			consultarPorFuncionarioCargoEPeriodo();
		} catch (Exception e) {
			setListaSelectItemCompetenciaPeriodo(new ArrayList<SelectItem>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarEvento() {
		try {
			EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
			obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS);
			setEventoFolhaPagamento(obj);
			getContraChequeEvento().setEventoFolhaPagamento(obj);

			valorConsultaEvento = "";
			campoConsultaEvento = "";
			getListaEventosFolhaPagamento().clear();			
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name() , e.getMessage());
		}
	}

	public void valorInformadoManual() {
		ContraChequeEventoVO obj = (ContraChequeEventoVO) context().getExternalContext().getRequestMap().get("contrachequeEvento");
		obj.getEventoFolhaPagamento().setInformadoManual(true);

		if (obj.getValorReferencia().intValue() != 0 ) {
			int contador = 0; 
			for (ContraChequeEventoVO contraChequeEventoVO : getContraCheque().getContraChequeEventos()) {
				if (contraChequeEventoVO.getCodigo().equals(obj.getCodigo())) {		
					getContraCheque().getContraChequeEventos().get(contador).setInformadoManual(true);
					getContraCheque().getContraChequeEventos().get(contador).getEventoFolhaPagamento().setValorTemporario(obj.getEventoFolhaPagamento().getValorTemporario());
					break;
				}
				contador++;
			}
		}
	}

	public void referenciaInformadoManual() {
		ContraChequeEventoVO obj = (ContraChequeEventoVO) context().getExternalContext().getRequestMap().get("contrachequeEvento");
		if (Uteis.isAtributoPreenchido(obj.getReferencia())) {
			int contador = 0; 
			for (ContraChequeEventoVO contraChequeEventoVO : getContraCheque().getContraChequeEventos()) {
				if (contraChequeEventoVO.getCodigo().equals(obj.getCodigo())) {		
					getContraCheque().getContraChequeEventos().get(contador).setInformadoManual(true);
					getContraCheque().getContraChequeEventos().get(contador).setValorInformado(true);
					getContraCheque().getContraChequeEventos().get(contador).getEventoFolhaPagamento().setReferencia(obj.getReferencia());
					break;
				}
				contador++;
			}
		}
	}

	/**
	 * Adiciona o evento do item do contra cheque selecionado a lista.
	 */
	public void adicionarEventoFolhaPagamento() {
		try {
			getFacadeFactory().getContraChequeInterfaceFacade().validarEventoContraChequeItem(getContraChequeEvento(), getContraCheque().getContraChequeEventos());
			if (getContraChequeEvento().getValorReferencia().intValue() != 0) {
				getContraChequeEvento().setValorInformado(true);
				getContraChequeEvento().setInformadoManual(true);
				getContraChequeEvento().getEventoFolhaPagamento().setValorTemporario(getContraChequeEvento().getValorReferencia());
				getContraChequeEvento().getEventoFolhaPagamento().setReferencia(getContraChequeEvento().getReferencia());
			}

			if (getContraChequeEvento().getItemEmEdicao()) {
				
				Iterator<ContraChequeEventoVO> i = getContraCheque().getContraChequeEventos().iterator();
				int index = 0;
				int aux = -1;
				ContraChequeEventoVO objAux = new ContraChequeEventoVO();
				while(i.hasNext()) {
					ContraChequeEventoVO objExistente = i.next();

					if (objExistente.getCodigo().equals(getContraChequeEvento().getCodigo()) && objExistente.getItemEmEdicao()){
						getContraChequeEvento().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getContraChequeEvento();
		 			}
		            index++;
				}

				if(aux >= 0) {
					getContraCheque().getContraChequeEventos().set(aux, objAux);
				}
			} else {		
				getContraCheque().getContraChequeEventos().add(0, getContraChequeEvento());
			}

			getContraCheque().getContraChequeEventos().sort((obj1, obj2) -> obj1.getEventoFolhaPagamento().getPrioridade().compareTo(obj2.getEventoFolhaPagamento().getPrioridade()));
			getContraCheque().getContraChequeEventos().sort((obj1, obj2) -> obj1.getEventoFolhaPagamento().getOrdemCalculo().compareTo(obj2.getEventoFolhaPagamento().getOrdemCalculo()));
			limparDadosEvento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void cancelarEdicao() {
		setContraChequeEvento(new ContraChequeEventoVO());
		limparDadosEvento();
	}

	/**
	 * Remove evento do item do contra cheque inserido na tabela.
	 */
	public void removerEventoFolhaPagamento(ContraChequeEventoVO contraChequeEvento) {
		setMensagemDetalhada("");
		getContraCheque().getContraChequeEventos().removeIf(obj -> obj.getEventoFolhaPagamento().getCodigo().equals(contraChequeEvento.getEventoFolhaPagamento().getCodigo()));
	}
	
	private void montarListaSelectItemTipoRecebimento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(TipoRecebimentoEnum.HORISTA.name(), TipoRecebimentoEnum.HORISTA.getDescricao()));
		itens.add(new SelectItem(TipoRecebimentoEnum.MENSALISTA.name(), TipoRecebimentoEnum.MENSALISTA.getDescricao()));
		
		setListaSelectItemTipoRecebimento(itens);
	}
	
	private void montarListaSelectItemPrevidencia() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(PrevidenciaEnum.INSS.getValor(), PrevidenciaEnum.INSS.getDescricao()));
		itens.add(new SelectItem(PrevidenciaEnum.PREVIDENCIA_PROPRIA.getValor(), PrevidenciaEnum.PREVIDENCIA_PROPRIA.getDescricao()));
		
		setListaSelectItemPrevidencia(itens);
	}

	public void limparDadosEvento() {
		setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
		setListaEventosFolhaPagamento(new ArrayList<>());
		setContraChequeEvento(new ContraChequeEventoVO());
	}

	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
	}

	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}
	
	public boolean getApresentarResultadoConsultaFuncionarioCargo() {
		return getFuncionarioCargoVOs().size() > 0;
	}

	public void imprimirRelatorioFichaFinanceira() {

		List<FichaFinanceiraRelVO> listaObjetos = new ArrayList<>();
		ContraChequeVO obj = new ContraChequeVO(); 
		if (Uteis.isAtributoPreenchido(context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO))) {
			obj = (ContraChequeVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		} else {
			obj = getContraCheque();
		}
		
		String retornoTipoRelatorio = null;
		try {
			obj = getFacadeFactory().getContraChequeInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo().longValue());
			obj.setCompetenciaFolhaPagamento(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getCompetenciaFolhaPagamento().getCodigo().longValue()));
			obj.setFuncionarioCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargo().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			listaObjetos.add(getFacadeFactory().getContraChequeInterfaceFacade().montarDadosFichaFinanceira(obj));
			getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "recursoshumanos" + File.separator + "FichaFinanceira" + ".jrxml");
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "recursoshumanos" + File.separator);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("FOLHA ANALÍTICA");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "recursoshumanos" + File.separator);
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			retornoTipoRelatorio = null;
		}
	}

	public ContraChequeVO getContraCheque() {
		if (contraCheque == null) {
			contraCheque = new ContraChequeVO();
		}
		return contraCheque;
	}
	
	public void setContraCheque(ContraChequeVO contraCheque) {
		this.contraCheque = contraCheque;
	}
	
	public ContraChequeEventoVO getContraChequeEvento() {
		if (contraChequeEvento == null) {
			contraChequeEvento = new ContraChequeEventoVO();
		}
		return contraChequeEvento;
	}
	
	public void setContraChequeEvento(ContraChequeEventoVO contraChequeEvento) {
		this.contraChequeEvento = contraChequeEvento;
	}
	
	public List<FuncionarioCargoVO> getFuncionarioCargoVOs() {
		if (funcionarioCargoVOs == null) {
			funcionarioCargoVOs = new ArrayList<FuncionarioCargoVO>();
		}
		return funcionarioCargoVOs;
	}
	
	public void setFuncionarioCargoVOs(List<FuncionarioCargoVO> funcionarioCargoVOs) {
		this.funcionarioCargoVOs = funcionarioCargoVOs;
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
	
	public List<SelectItem> getListaSelectItemCompetenciaPeriodo() {
		if (listaSelectItemCompetenciaPeriodo == null)
			listaSelectItemCompetenciaPeriodo = new ArrayList<>();
		return listaSelectItemCompetenciaPeriodo;
	}
	
	public void setListaSelectItemCompetenciaPeriodo(List<SelectItem> listaSelectItemCompetenciaPeriodo) {
		this.listaSelectItemCompetenciaPeriodo = listaSelectItemCompetenciaPeriodo;
	}
	
	public List<CompetenciaFolhaPagamentoVO> getCompetenciaFolhaPagamentoVOs() {
		if (competenciaFolhaPagamentoVOs == null) {
			competenciaFolhaPagamentoVOs = new ArrayList<>();
		}
		return competenciaFolhaPagamentoVOs;
	}
	
	public void setCompetenciaFolhaPagamentoVOs(List<CompetenciaFolhaPagamentoVO> competenciaFolhaPagamentoVOs) {
		this.competenciaFolhaPagamentoVOs = competenciaFolhaPagamentoVOs;
	}
	
	public String getCampoConsultaCompetencia() {
		if (campoConsultaCompetencia == null) {
			campoConsultaCompetencia = "";
		}
		return campoConsultaCompetencia;
	}
	
	public void setCampoConsultaCompetencia(String campoConsultaCompetencia) {
		this.campoConsultaCompetencia = campoConsultaCompetencia;
	}
	
	public String getValorConsultaCompetencia() {
		if (valorConsultaCompetencia == null) {
			valorConsultaCompetencia = "";
		}
		return valorConsultaCompetencia;
	}
	
	public void setValorConsultaCompetencia(String valorConsultaCompetencia) {
		this.valorConsultaCompetencia = valorConsultaCompetencia;
	}
	
	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		if (listaEventosFolhaPagamento == null) {
			listaEventosFolhaPagamento = new  ArrayList<>();
		}
		return listaEventosFolhaPagamento;
	}
	
	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
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
	
	public EventoFolhaPagamentoVO getEventoFolhaPagamento() {
		if (eventoFolhaPagamento == null) {
			eventoFolhaPagamento = new EventoFolhaPagamentoVO();
		}
		return eventoFolhaPagamento;
	}
	
	public void setEventoFolhaPagamento(EventoFolhaPagamentoVO eventoFolhaPagamento) {
		this.eventoFolhaPagamento = eventoFolhaPagamento;
	}
	
	public List<SelectItem> getListaSelectItemTipoRecebimento() {
		if (listaSelectItemTipoRecebimento == null) {
			montarListaSelectItemTipoRecebimento();
		}
		return listaSelectItemTipoRecebimento;
	}
	
	public void setListaSelectItemTipoRecebimento(List<SelectItem> listaSelectItemTipoRecebimento) {
		this.listaSelectItemTipoRecebimento = listaSelectItemTipoRecebimento;
	}
	
	public List<SelectItem> getListaSelectItemPrevidencia() {
		if (listaSelectItemPrevidencia == null) {
			montarListaSelectItemPrevidencia();
		}
		return listaSelectItemPrevidencia;
	}
	
	public void setListaSelectItemPrevidencia(List<SelectItem> listaSelectItemPrevidencia) {
		this.listaSelectItemPrevidencia = listaSelectItemPrevidencia;
	}
	
	public Date getDataInicial() {
		return dataInicial;
	}
	
	public Date getDataFinal() {
		return dataFinal;
	}
	
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public void visualizarEventoFolhaPagamento() {
		ContraChequeEventoVO obj = (ContraChequeEventoVO) context().getExternalContext().getRequestMap().get("contrachequeEvento");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("eventoFolhaPagamentoDoContraCheque", obj.getEventoFolhaPagamento());
		}
		removerControleMemoriaFlashTela(EventoFolhaPagamentoControle.class.getSimpleName());
	}
 
	/**
	 * Retorna a class do css de acordoo com o tipo de lancamento.
	 * 
	 * @return
	 */
	public String corDoEvento() {
		ContraChequeEventoVO obj = (ContraChequeEventoVO) context().getExternalContext().getRequestMap().get("contrachequeEvento");

		switch (obj.getEventoFolhaPagamento().getTipoLancamento()) {
		case PROVENTO:
			return obj.getInformadoManual() ? "textBlueNegrito" : "textBlue";
		case DESCONTO:
			return obj.getInformadoManual() ? "textRedNegrito" : "textRed";
		case BASE_CALCULO:
			return obj.getInformadoManual() ? "textGreenNegrito" : "textGreen";
		default:
			break;
		}
		return "";
	}

	public List<ContraChequeEventoVO> getContraChequeEventos() {
		if(contraChequeEventos == null)
			contraChequeEventos = new ArrayList<>();
		return contraChequeEventos;
	}

	public void setContraChequeEventos(List<ContraChequeEventoVO> contraChequeEventos) {
		this.contraChequeEventos = contraChequeEventos;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamento() {
		if (competenciaFolhaPagamento == null) {
			competenciaFolhaPagamento = new CompetenciaFolhaPagamentoVO();
		}
		return competenciaFolhaPagamento;
	}

	public void setCompetenciaFolhaPagamento(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento) {
		this.competenciaFolhaPagamento = competenciaFolhaPagamento;
	}

	public Long getCodigoCompetenciaPeriodoAnterior() {
		if (codigoCompetenciaPeriodoAnterior == null) {
			codigoCompetenciaPeriodoAnterior = 0L;
		}
		return codigoCompetenciaPeriodoAnterior;
	}

	public void setCodigoCompetenciaPeriodoAnterior(Long codigoCompetenciaPeriodoAnterior) {
		this.codigoCompetenciaPeriodoAnterior = codigoCompetenciaPeriodoAnterior;
	}
}