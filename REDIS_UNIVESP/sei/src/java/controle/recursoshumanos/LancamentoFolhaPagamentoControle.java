package controle.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.script.ScriptEngine;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.GrupoLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO.EnumCampoConsultaSecaoFolhaPagamento;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTemplateFolhaPagamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.ValorFixoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.recursoshumanos.CalculoContraCheque;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas lancamentoFolhaPagamentoForm.xhtml e lancamentoFolhaPagamentoCons.xhtl
 * com as funcionalidades da classe <code>LancamentoFolhaPagamentoVO</code>.
 * Implemtacao da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaGEDVO
 */
@Controller("LancamentoFolhaPagamentoControle")
@Scope("viewScope")
@Lazy
public class LancamentoFolhaPagamentoControle extends SuperControle {

	private static final long serialVersionUID = 1455114507405935993L;
	
	private LancamentoFolhaPagamentoVO lancamentoFolhaPagamento;
	
	private List<FuncionarioVO> funcionarioVOs;
	private List<FuncionarioCargoVO> funcionarioCargoVOs;
	
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;
	
	private List<GrupoLancamentoFolhaPagamentoVO> listaGrupoLancamentoFolhaPagamento;
	
	private List<SelectItem> listaSelectItemFormacontratacao;
	private List<SelectItem> listaSelectItemRecebimento;
	private List<SelectItem> listaSelectItemSituacao;
	
	private String campoConsultaSecaoFolhaPagamento;
    private String valorConsultaSecaoFolhaPagamento;
	private List<SecaoFolhaPagamentoVO> listaConsultaSecaoFolhaPagamento;
	
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	
	private String campoConsultaGrupoLancamento;
	private String valorConsultaGrupoLancamento;
	
	private String[] formaContratacao = {};
	private String[] recebimento = {};
	private String[] situacao = {};
	
	private Boolean marcarTodosFormaContratacao;
	private Boolean marcarTodosRecebimento;
	private Boolean marcarTodosSituacao;
	
	private static final String TELA_FORM = "lancamentoFolhaPagamentoForm";
	private static final String TELA_CONS = "lancamentoFolhaPagamentoCons";
	private static final String CONTEXT_PARA_EDICAO = "lancamentoFolha";

	private Date dataInicial;
	private Date dataFinal;
	
	private Boolean grupoLancamentoSelecionado;

	private List<SelectItem> listaSelectItemCompetenciaPeriodo;
	
	private TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO;
	
	private List<TemplateEventoFolhaPagamentoVO> listaTemplateEventoFolhaPagamentoVO;
	private List<FuncionarioCargoVO> listaDeFuncionarios;
	
	private ProgressBarVO progressBarVO;

	public LancamentoFolhaPagamentoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		getControleConsulta().setValorConsulta("");
		preencherCompetenciaFolhaPagamento();
	}

	/**
	 * Rotina responsavel por disponibilizar um novo objeto da classe
	 * <code>LancamentoFolhaPagamentoVO</code> para edicao pelo usuario da aplicacao.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setLancamentoFolhaPagamento(new LancamentoFolhaPagamentoVO());
		getLancamentoFolhaPagamento().setRascunho(Boolean.TRUE);
		getLancamentoFolhaPagamento().setAtivo(Boolean.TRUE);
		
		preencherCompetenciaFolhaPagamento();
		
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	private void preencherCompetenciaFolhaPagamento() {
		CompetenciaFolhaPagamentoVO competencia = new CompetenciaFolhaPagamentoVO();

		try {
			competencia = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
			setDataInicial(competencia.getDataCompetencia());
			setDataFinal(competencia.getDataCompetencia());
			getLancamentoFolhaPagamento().setCompetenciaFolhaPagamentoVO(competencia);
			getLancamentoFolhaPagamento().setDataCompetencia(competencia.getDataCompetencia());
			
			preencherCompetenciaPeriodo(getLancamentoFolhaPagamento().getCompetenciaFolhaPagamentoVO());
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), "msg_erro_LancamentoFolhaPagamento_competenciaAtivaNaoEncontrado");
		}		
	}

	private void preencherCompetenciaPeriodo(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		List<CompetenciaPeriodoFolhaPagamentoVO> periodos;
		try {
			periodos = getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorCompetenciaFolhaPagamento(competenciaFolhaPagamentoVO);
			setListaSelectItemCompetenciaPeriodo(UtilSelectItem.getListaSelectItem(periodos, "codigo", "periodoApresentacao", false));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por disponibilizar os dados de um objeto da classe
	 * <code>LancamentoFolhaPagamentoVO</code> para alteracao. O objeto desta classe e
	 * disponibilizado na session da pagina (request) para que o JSP correspondente
	 * possa disponibiliza-lo para edicao.
	 */
	public String editar() {
		LancamentoFolhaPagamentoVO obj = (LancamentoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		obj.setNovoObj(Boolean.FALSE);

		try {
			if (obj.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo() != null || !obj.getGrupoLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getCodigo().equals(0)) {
				obj.getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimariaUnica(obj.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}

			if (Uteis.isAtributoPreenchido(obj.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo())) {
				obj.getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo().longValue()));
			}

			if (Uteis.isAtributoPreenchido(obj.getGrupoLancamentoFolhaPagamento().getCodigo())) {
				obj.setGrupoLancamentoFolhaPagamento(getFacadeFactory().getGrupoLancamentoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getGrupoLancamentoFolhaPagamento().getCodigo().longValue()));
			}

			setListaTemplateEventoFolhaPagamentoVO(getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().consultarPorTemplateEventoFolhaPagamento(obj.getTemplateLancamentoFolhaPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			obj.getTemplateLancamentoFolhaPagamento().setListaEventosDoTemplate(getListaTemplateEventoFolhaPagamentoVO());

			formaContratacao = obj.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";");
			recebimento = obj.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";");
			situacao = obj.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";");

			preencherCompetenciaPeriodo(obj.getCompetenciaFolhaPagamentoVO());
			selecionarTodos();

			setLancamentoFolhaPagamento((LancamentoFolhaPagamentoVO) SerializationUtils.clone(obj));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	/**
	 * Verifica se o recebimento, forma contratacao e situacao foram selecionado todos
	 * e marca o checkbox.
	 */
	private void selecionarTodos() {
		if (getListaSelectItemRecebimento().size() == recebimento.length) {
			setMarcarTodosRecebimento(true);
		}

		if (getListaSelectItemFormacontratacao().size() == formaContratacao.length) {
			setMarcarTodosFormaContratacao(true);
		}

		if (getListaSelectItemSituacao().size() == situacao.length) {
			setMarcarTodosSituacao(true);
		}
	}

	/**
	 * Rotina responsavel por organizar a paginacao entre as paginas resultantes
	 * de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		preencherCompetenciaFolhaPagamento();
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Rotina responsavel por gravar no BD os dados editados de um novo objeto da
	 * classe <code>LancamentoFolhaPagamento</code>. Caso o objeto seja novo (ainda nao gravado
	 * no BD) e acionado a operacao <code>incluir()</code>. Caso contrario e
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistencia o objeto
	 * nao e gravado, sendo re-apresentado para o usuario juntamente com uma
	 * mensagem de erro.
	 */
	public String persistir(boolean rascunho) {
		try {
			getLancamentoFolhaPagamento().setRascunho(rascunho);
			getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setTipoTemplateFolhaPagamento(TipoTemplateFolhaPagamentoEnum.LANCAMENTO);
			getFacadeFactory().getLancamentoFolhaPagamentoInterfaceFacade().persistir(getLancamentoFolhaPagamento(), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			lancamentoFolhaPagamento.setRascunho(true);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis do
	 * lancamentoFolhaPagamentoCons.xhtml. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			tratarDataDaConsulta();
			List<LancamentoFolhaPagamentoVO> objs = getFacadeFactory().getLancamentoFolhaPagamentoInterfaceFacade().consultarPorFiltro(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(),this.getDataInicial(), this.getDataFinal(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

			setListaConsulta(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			setListaConsulta(new ArrayList<TipoDocumentoVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
	}

	/**
	 * Como a data da competencia da tela e por mes, esse metodo trata <br>
	 * a data de consulta inicial e final para que busque sempre do <br>
	 * inicio do mes e o final do mes da competencia informada 
	 *   
	 */
	private void tratarDataDaConsulta() {

		if(this.getDataInicial() == null || this.getDataFinal() == null)
			return;
			
		this.setDataInicial(Uteis.getDataPrimeiroDiaMes(this.getDataInicial()));
		this.setDataFinal(UteisData.getUltimaDataMes(this.getDataFinal()));
		
	}

	/**
	 * Rotina responsavel por realizar a paginacao da pagina de lancamentoFolhaPagamentoCons.xhtml
	 * 
	 * @param dataScrollEvent
	 * @throws Exception
	 */
	public void scrollerListener(DataScrollEvent dataScrollEvent) {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
        consultar();
    }

	/**
	 * Rotina responsavel por realizar a paginacao da pesquisa secao folha de pagamento.
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerSecaoFolhaPagamento(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public boolean getApresentarResultadoConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 0;
	}
	
	public boolean getApresentarPaginadorConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 5;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}
	
	/**
	 * Rotina responsavel por preencher a combo de consulta da tela.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("funcionario", "Funcionário"));
		return itens;
	}

	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionario() {
		
		try {
			
			List<FuncionarioCargoVO> objs = new ArrayList<>(0);
			
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
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(this.getTemplateEventoFolhaPagamentoVO().getEventoFolhaPagamento().getIdentificador())) {
				this.getTemplateEventoFolhaPagamentoVO().setEventoFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(this.getTemplateEventoFolhaPagamentoVO().getEventoFolhaPagamento().getIdentificador(), false, getUsuarioLogado()));
			} else {
				this.getTemplateEventoFolhaPagamentoVO().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			this.getTemplateEventoFolhaPagamentoVO().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarSecaoPorIdentificador() {
		try {
			String identificadorSecao = getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getIdentificador();
			if (Uteis.isAtributoPreenchido(identificadorSecao)) {
				getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(
						getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorIdentificador(identificadorSecao));
			} else {
				getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			} 
			
			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(campoConsultaEvento, valorConsultaEvento, "ATIVO", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarFuncionarioPorMatricula() {
		try {
			if (Uteis.isAtributoPreenchido(getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getMatriculaCargo())) {
				FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
				getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(funcionarioCargo);
						
				setMensagemID("msg_entre_dados");
			} else {
				getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
			}
		} catch (Exception e) {
			getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarGrupoLancamento() {
		try {
			if (getCampoConsultaGrupoLancamento().equals("codigo")) {
				if (getValorConsultaGrupoLancamento().trim().isEmpty() || !Uteis.getIsValorNumerico(getValorConsultaGrupoLancamento())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_GrupoLancamentoFolhaFolhaPagamento_ConsultaCampoCodigoInvalido"));
				}
			}
			setListaGrupoLancamentoFolhaPagamento(getFacadeFactory().getGrupoLancamentoFolhaPagamentoInterfaceFacade().consultarPorFiltro(campoConsultaGrupoLancamento, valorConsultaGrupoLancamento, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarSecaoFolhaPagamento() {
		try {

			if (getCampoConsultaSecaoFolhaPagamento().equals(EnumCampoConsultaSecaoFolhaPagamento.CODIGO.name()) && 
					getValorConsultaSecaoFolhaPagamento().trim().isEmpty() || !Uteis.getIsValorNumerico(getValorConsultaSecaoFolhaPagamento())) {
				throw new ConsistirException(UteisJSF.internacionalizar("prt_SecaoFolhaPagamento_ConsultaCampoCodigoInvalido"));
			}

			setListaConsultaSecaoFolhaPagamento(getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultar(getCampoConsultaSecaoFolhaPagamento(), getValorConsultaSecaoFolhaPagamento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFormaContratacao() {
		lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario("");
		for(String formaContratacao : this.formaContratacao ) {
			lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().concat(formaContratacao).concat(";"));
		}
	}

	public void selecionarRecebimento() {
		lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setTipoRecebimento("");
		for(String recebimento : this.recebimento ) {
			lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setTipoRecebimento(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().concat(recebimento).concat(";"));
		}
	}

	public void selecionarSituacao() {
		lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario("");
		for(String situacao : this.situacao ) {
			lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().concat(situacao).concat(";"));
		}
	}
	
	public void selecionarSecaoFolhaPagamento() {
    	SecaoFolhaPagamentoVO obj = (SecaoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("itemSecaoFolhaPagamento");
    	getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(obj);
    	this.getListaConsultaSecaoFolhaPagamento().clear();
    }

	public void selecionarFuncionarioCargo() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem");
		this.lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(obj);

		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";
		getFuncionarioVOs().clear();
	}

	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		getTemplateEventoFolhaPagamentoVO().setEventoFolhaPagamento(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	}

	public void selecionarGrupoLancamento() throws Exception {
		GrupoLancamentoFolhaPagamentoVO obj = (GrupoLancamentoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("grupoLancamentoItem");
		this.getLancamentoFolhaPagamento().setGrupoLancamentoFolhaPagamento((GrupoLancamentoFolhaPagamentoVO) SerializationUtils.clone(obj));
		getLancamentoFolhaPagamento().setTemplateLancamentoFolhaPagamento((TemplateLancamentoFolhaPagamentoVO) SerializationUtils.clone(obj.getTemplateLancamentoFolhaPagamento()));

		//Seta 0 para persistir um novo Template lancamento para o lancamento.
		if (getLancamentoFolhaPagamento().getNovoObj()) {
			getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setCodigo(0);
		} else {
			TemplateLancamentoFolhaPagamentoVO template = getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().consultarTemplatePorLancamentoFolha(getLancamentoFolhaPagamento().getCodigo());
			if (Uteis.isAtributoPreenchido(template)) {
				getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setCodigo(template.getCodigo());
			}
		}

		if (Uteis.isAtributoPreenchido(obj.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo())) {
			getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo().longValue()));
		}
		
		setListaTemplateEventoFolhaPagamentoVO(getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().consultarPorTemplateEventoFolhaPagamento(obj.getTemplateLancamentoFolhaPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setListaEventosDoTemplate(getListaTemplateEventoFolhaPagamentoVO());

		valorConsultaGrupoLancamento = "";
		campoConsultaGrupoLancamento = "";
		
		formaContratacao = getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";");
		recebimento = getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";");
		situacao = getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";");

		try {
			for (TemplateEventoFolhaPagamentoVO object : lancamentoFolhaPagamento.getGrupoLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate()) {				
				TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamento = new TemplateEventoFolhaPagamentoVO();
				templateEventoFolhaPagamento.setEventoFolhaPagamento(object.getEventoFolhaPagamento());
				templateEventoFolhaPagamento.setValor(object.getValor());
				templateEventoFolhaPagamento.setTemplateLancamentoFolhaPagamentoVO(getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento());

				getListaTemplateEventoFolhaPagamentoVO().add(templateEventoFolhaPagamento);
			}
			
			grupoLancamentoSelecionado = Boolean.FALSE;
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Adiciona o evento da folha de pagamento selecionado a lista.
	 */
	public void adicionarEventoFolhaPagamento() {
		try {
			getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().validarEventoFolhaPagamento(getListaTemplateEventoFolhaPagamentoVO(), getTemplateEventoFolhaPagamentoVO());
			getListaTemplateEventoFolhaPagamentoVO().add(getTemplateEventoFolhaPagamentoVO());

			getListaTemplateEventoFolhaPagamentoVO().sort((obj1, obj2) -> obj1.getEventoFolhaPagamento().getPrioridade().compareTo(obj2.getEventoFolhaPagamento().getPrioridade()));
			limparDadosEvento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Remove evento da folha de pagamento inserido na tabela.
	 */
	public void removerEventoFolhaPagamento(TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO) {
		
		setMensagemDetalhada("");
		
		Iterator<TemplateEventoFolhaPagamentoVO> iterator = getListaTemplateEventoFolhaPagamentoVO().iterator();

		while(iterator.hasNext()) {
			TemplateEventoFolhaPagamentoVO templateEvento = iterator.next();
			if (templateEvento.getEventoFolhaPagamento().getCodigo().equals(templateEventoFolhaPagamentoVO.getEventoFolhaPagamento().getCodigo())) {
				iterator.remove();
			}
		}
	}

	public void limparDadosEvento() {
		setTemplateEventoFolhaPagamentoVO(new TemplateEventoFolhaPagamentoVO());
		setMensagemID("msg_entre_dados");
	}

	public void limparGrupoLancamento() {
		if (getGrupoLancamentoSelecionado()) {
			getListaGrupoLancamentoFolhaPagamento().clear();
			lancamentoFolhaPagamento.setGrupoLancamentoFolhaPagamento(new GrupoLancamentoFolhaPagamentoVO());
			lancamentoFolhaPagamento.setTemplateLancamentoFolhaPagamento(new TemplateLancamentoFolhaPagamentoVO());

			formaContratacao = new String[] {};
			recebimento = new String[] {};
			situacao = new String[] {};
		} else {
			lancamentoFolhaPagamento.setGrupoLancamentoFolhaPagamento(new GrupoLancamentoFolhaPagamentoVO());			
		}
		setGrupoLancamentoSelecionado(Boolean.FALSE);
	}

	public void limparDadosFuncionario () {
		lancamentoFolhaPagamento.setGrupoLancamentoFolhaPagamento(new GrupoLancamentoFolhaPagamentoVO());
		lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
		getControleConsulta().setValorConsulta("");
	}

    public void limparDadosSecaoFolhaPagamento() {
    	getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
    }

	public List<SelectItem> getTipoConsultaLancamento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("DataCompetencia", UteisJSF.internacionalizar("prt_LancamentoFolhaPagamento_dataCompetencia")));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboGrupoLancamento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", UteisJSF.internacionalizar("prt_TextoPadrao_Nome")));
		itens.add(new SelectItem("codigo", UteisJSF.internacionalizar("prt_TextoPadrao_codigo")));
		return itens;
	}

	public List<SelectItem> getListaSelectItemFormacontratacao() {
		if (listaSelectItemFormacontratacao == null || listaSelectItemFormacontratacao.isEmpty()) {
			listaSelectItemFormacontratacao = new ArrayList<>();
			try {
				for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
					listaSelectItemFormacontratacao.add(new SelectItem(formaContratacaoFuncionarioEnum, formaContratacaoFuncionarioEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemFormacontratacao;
	}

	public void setListaSelectItemFormacontratacao(List<SelectItem> listaSelectItemFormacontratacao) {
		this.listaSelectItemFormacontratacao = listaSelectItemFormacontratacao;
	}

	public List<SelectItem> getListaSelectItemRecebimento() {
		if (listaSelectItemRecebimento == null || listaSelectItemRecebimento.isEmpty()) {
			listaSelectItemRecebimento = new ArrayList<>(0);
			try {
				for (TipoRecebimentoEnum tipoRecebimentoEnum : TipoRecebimentoEnum.values()) {
					listaSelectItemRecebimento.add(new SelectItem(tipoRecebimentoEnum, tipoRecebimentoEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemRecebimento;
	}

	public void setListaSelectItemRecebimento(List<SelectItem> listaSelectItemRecebimento) {
		this.listaSelectItemRecebimento = listaSelectItemRecebimento;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null || listaSelectItemSituacao.isEmpty()) {
			listaSelectItemSituacao = new ArrayList<>(0);
			try {
				for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
					listaSelectItemSituacao.add(new SelectItem(situacaoFuncionarioEnum, situacaoFuncionarioEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public String getCampoConsultaFuncionario() {
		if(campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if(valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<FuncionarioVO> getFuncionarioVOs() {
		if (funcionarioVOs == null) {
			funcionarioVOs = new ArrayList<>(0);
		}
		return funcionarioVOs;
	}

	public void setFuncionarioVOs(List<FuncionarioVO> funcionarioVOs) {
		this.funcionarioVOs = funcionarioVOs;
	}

	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		if(listaEventosFolhaPagamento == null) {
			listaEventosFolhaPagamento = new ArrayList<>();
		}
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
	}

	public String getCampoConsultaEvento() {
		if(campoConsultaEvento == null) {
			campoConsultaEvento = "";
		}
		return campoConsultaEvento;
	}

	public void setCampoConsultaEvento(String campoConsultaEvento) {
		this.campoConsultaEvento = campoConsultaEvento;
	}

	public String getValorConsultaEvento() {
		if(valorConsultaEvento == null) {
			valorConsultaEvento = "";
		}
		return valorConsultaEvento;
	}

	public void setValorConsultaEvento(String valorConsultaEvento) {
		this.valorConsultaEvento = valorConsultaEvento;
	}

	public String[] getFormaContratacao() {
		return formaContratacao;
	}

	public void setFormaContratacao(String[] formaContratacao) {
		this.formaContratacao = formaContratacao;
	}

	public String[] getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(String[] recebimento) {
		this.recebimento = recebimento;
	}

	public String[] getSituacao() {
		return situacao;
	}

	public void setSituacao(String[] situacao) {
		this.situacao = situacao;
	}

	public LancamentoFolhaPagamentoVO getLancamentoFolhaPagamento() {
		if (lancamentoFolhaPagamento == null) {
			lancamentoFolhaPagamento = new LancamentoFolhaPagamentoVO();
		}
		return lancamentoFolhaPagamento;
	}

	public void setLancamentoFolhaPagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) {
		this.lancamentoFolhaPagamento = lancamentoFolhaPagamento;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getCampoConsultaGrupoLancamento() {
		if(campoConsultaGrupoLancamento == null) {
			campoConsultaGrupoLancamento = "";
		}
		return campoConsultaGrupoLancamento;
	}

	public void setCampoConsultaGrupoLancamento(String campoConsultaGrupoLancamento) {
		this.campoConsultaGrupoLancamento = campoConsultaGrupoLancamento;
	}

	public String getValorConsultaGrupoLancamento() {
		return valorConsultaGrupoLancamento;
	}

	public void setValorConsultaGrupoLancamento(String valorConsultaGrupoLancamento) {
		this.valorConsultaGrupoLancamento = valorConsultaGrupoLancamento;
	}

	public List<GrupoLancamentoFolhaPagamentoVO> getListaGrupoLancamentoFolhaPagamento() {
		if (listaGrupoLancamentoFolhaPagamento == null) {
			listaGrupoLancamentoFolhaPagamento = new ArrayList<>(0);
		}
		return listaGrupoLancamentoFolhaPagamento;
	}

	public void setListaGrupoLancamentoFolhaPagamento(
			List<GrupoLancamentoFolhaPagamentoVO> listaGrupoLancamentoFolhaPagamento) {
		this.listaGrupoLancamentoFolhaPagamento = listaGrupoLancamentoFolhaPagamento;
	}
	
	public String getCampoConsultaSecaoFolhaPagamento() {
		return campoConsultaSecaoFolhaPagamento;
	}

	public void setCampoConsultaSecaoFolhaPagamento(String campoConsultaSecaoFolhaPagamento) {
		this.campoConsultaSecaoFolhaPagamento = campoConsultaSecaoFolhaPagamento;
	}

	public String getValorConsultaSecaoFolhaPagamento() {
		return valorConsultaSecaoFolhaPagamento;
	}

	public void setValorConsultaSecaoFolhaPagamento(String valorConsultaSecaoFolhaPagamento) {
		this.valorConsultaSecaoFolhaPagamento = valorConsultaSecaoFolhaPagamento;
	}

	public List<SecaoFolhaPagamentoVO> getListaConsultaSecaoFolhaPagamento() {
		if (listaConsultaSecaoFolhaPagamento == null) {
			listaConsultaSecaoFolhaPagamento = new ArrayList<>();
		}
		return listaConsultaSecaoFolhaPagamento;
	}

	public void setListaConsultaSecaoFolhaPagamento(List<SecaoFolhaPagamentoVO> listaConsultaSecaoFolhaPagamento) {
		this.listaConsultaSecaoFolhaPagamento = listaConsultaSecaoFolhaPagamento;
	}

	public List<FuncionarioCargoVO> getFuncionarioCargoVOs() {
		if (funcionarioCargoVOs == null)
			funcionarioCargoVOs = new ArrayList<>();
		return funcionarioCargoVOs;
	}

	public void setFuncionarioCargoVOs(List<FuncionarioCargoVO> funcionarioCargoVOs) {
		this.funcionarioCargoVOs = funcionarioCargoVOs;
	}

	public Boolean getMarcarTodosFormaContratacao() {
		if (marcarTodosFormaContratacao == null)
			marcarTodosFormaContratacao = false;
		return marcarTodosFormaContratacao;
	}

	public void setMarcarTodosFormaContratacao(Boolean marcarTodosFormaContratacao) {
		this.marcarTodosFormaContratacao = marcarTodosFormaContratacao;
	}

	public Boolean getMarcarTodosRecebimento() {
		if (marcarTodosRecebimento == null)
			marcarTodosRecebimento = false;
		return marcarTodosRecebimento;
	}

	public void setMarcarTodosRecebimento(Boolean marcarTodosRecebimento) {
		this.marcarTodosRecebimento = marcarTodosRecebimento;
	}

	public Boolean getMarcarTodosSituacao() {
		if (marcarTodosSituacao == null)
			marcarTodosSituacao = false;
		return marcarTodosSituacao;
	}

	public void setMarcarTodosSituacao(Boolean marcarTodosSituacao) {
		this.marcarTodosSituacao = marcarTodosSituacao;
	}
	
	public void selecionarTodosFormaContratacao() {
		getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario("");
		if(getMarcarTodosFormaContratacao()) {
			for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
				getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(
						getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().concat(formaContratacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		formaContratacao = getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";");
	}
	
	public void selecionarTodosRecebimento() {
		getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setTipoRecebimento("");
		if(getMarcarTodosRecebimento()) {
			for (TipoRecebimentoEnum formaRecebimentoEnum : TipoRecebimentoEnum.values()) {
				getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setTipoRecebimento(
						getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().concat(formaRecebimentoEnum.toString()).concat(";"));
			}
		}

		recebimento = getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";");
	}
	
	public void selecionarTodosSituacao() {
		getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario("");
		if(getMarcarTodosSituacao()) {
			for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
				getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(
						getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().concat(situacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		situacao = getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";");
	}

	public List<SelectItem> getListaSelectItemCompetenciaPeriodo() {
		if (listaSelectItemCompetenciaPeriodo == null)
			listaSelectItemCompetenciaPeriodo = new ArrayList<>();
		return listaSelectItemCompetenciaPeriodo;
	}

	public void setListaSelectItemCompetenciaPeriodo(List<SelectItem> listaSelectItemCompetenciaPeriodo) {
		this.listaSelectItemCompetenciaPeriodo = listaSelectItemCompetenciaPeriodo;
	}
	
	public TemplateEventoFolhaPagamentoVO getTemplateEventoFolhaPagamentoVO() {
		if (templateEventoFolhaPagamentoVO == null)
			templateEventoFolhaPagamentoVO = new TemplateEventoFolhaPagamentoVO();
		return templateEventoFolhaPagamentoVO;
	}

	public void setTemplateEventoFolhaPagamentoVO(TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO) {
		this.templateEventoFolhaPagamentoVO = templateEventoFolhaPagamentoVO;
	}

	public List<TemplateEventoFolhaPagamentoVO> getListaTemplateEventoFolhaPagamentoVO() {
		if (listaTemplateEventoFolhaPagamentoVO == null)
			listaTemplateEventoFolhaPagamentoVO = new ArrayList<>();
		return listaTemplateEventoFolhaPagamentoVO;
	}

	public void setListaTemplateEventoFolhaPagamentoVO(
			List<TemplateEventoFolhaPagamentoVO> listaTemplateEventoFolhaPagamentoVO) {
		this.listaTemplateEventoFolhaPagamentoVO = listaTemplateEventoFolhaPagamentoVO;
	}
	
	public void chamarTelaFichaFinanceira() {
		context().getExternalContext().getSessionMap().put("competenciaFolhaPagamento", getLancamentoFolhaPagamento().getCompetenciaFolhaPagamentoVO());
		context().getExternalContext().getSessionMap().put("periodoFolhaPagamento", getLancamentoFolhaPagamento().getPeriodo());
		removerControleMemoriaFlashTela(ContraChequeControle.class.getSimpleName());		
	}

	public ProgressBarVO getProgressBarVO() {
		if (progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public void calcularFolhaPagamento() {
		try {
			//Inserir esse valores manualmente via script ou inserir quando tiver evento de IRRF ou o evento retornar o valor 0 quando nao existir 
			BigDecimal valorDependente = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarValorFixoPorReferencia(ValorFixoEnum.VALOR_DEDUZIR_DEPENDENTE_IRRF, new Date());
			ValorReferenciaFolhaPagamentoVO valorReferenciaIRRF = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarValorReferenciaPorReferencia(ValorFixoEnum.IRRF.name(), new Date());
			List<String> identificadores = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarListaDeIdentificadoresAtivo();
			ScriptEngine engine = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inicializaEngineFormula();

			getFacadeFactory().getContraChequeInterfaceFacade().gerarFolhaPagamento(getLancamentoFolhaPagamento(), getUsuarioLogado(), getListaDeFuncionarios());
			CalculoContraCheque calculoContraCheque = new CalculoContraCheque();

			for(FuncionarioCargoVO funcionarioCargo : (ArrayList<FuncionarioCargoVO>) getListaDeFuncionarios()) {
				getFacadeFactory().getContraChequeInterfaceFacade().realizarCalculoContraChequeDaListaDeFuncionariosFiltrados(getLancamentoFolhaPagamento(),  
						getUsuarioLogado(), valorDependente, valorReferenciaIRRF, identificadores, engine, calculoContraCheque, funcionarioCargo);

				getProgressBarVO().incrementar();
				getProgressBarVO().setStatus(" ( " + getProgressBarVO().getProgresso() + " de " + getProgressBarVO().getMaxValue() + " Funcionários) ");
			}

			setMensagemID("msg_LancamentoFolhaPagamento_folhaGeradaComSucesso");
		} catch (Exception e) {
			getProgressBarVO().setForcarEncerramento(true);
            getProgressBarVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
			setMensagemDetalhada(MSG_TELA.msg_erro.name() , e.getMessage());
		}
	}

	public synchronized void gerarContaPagar() {
		try {
			List<String> errosContaPagar = new ArrayList<>();
			getFacadeFactory().getContraChequeInterfaceFacade().gerarContaPagar(getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento(), getUsuarioLogado(), errosContaPagar);

			setMensagemID("msg_LancamentoFolhaPagamento_contaPagarGeradaComSucesso");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name() , e.getMessage());
		} finally {
			getProgressBarVO().encerrar();
		}
	}

	public void cancelarFolhaPagamento() {
		try {
			getFacadeFactory().getLancamentoFolhaPagamentoInterfaceFacade().cancelarFolhaPagamento(getLancamentoFolhaPagamento(), getUsuarioLogado());
			setMensagemID("msg_dados_cancelados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name() , e.getMessage());
		}
	}
	
	/**
	 * Inicializa o progressBar ja validandos os filtros e {@link FuncionarioCargoVO}.
	 * 
	 * @param metodo
	 */
	public void executarInicioProgressBar(String metodo) {
		try {
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().resetar();
			getProgressBarVO().setSuperControle(this);
			getProgressBarVO().setUsuarioVO(getUsuarioLogado());

			getLancamentoFolhaPagamento().setRascunho(Boolean.FALSE);
			getFacadeFactory().getLancamentoFolhaPagamentoInterfaceFacade().validarDadosGerarFolhaPagamento(getLancamentoFolhaPagamento(), getListaTemplateEventoFolhaPagamentoVO(), getUsuarioLogado());

			setListaDeFuncionarios(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoFuncionarioPorFiltrosTemplateFolhaPagamento(getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento(), Uteis.NIVELMONTARDADOS_TODOS));
			if(getListaDeFuncionarios() == null || getListaDeFuncionarios().isEmpty()) {
				setMensagemDetalhada("msg_informe_dados", UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_nenhumFuncionarioSelecionado"), Uteis.ALERTA);
			}
			getProgressBarVO().iniciar(0l, listaDeFuncionarios.size(), "Iniciando Calculo da Folha. ", true, this, metodo);

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name() , e.getMessage());
		}
	}

	public Boolean getGrupoLancamentoSelecionado() {
		if (grupoLancamentoSelecionado == null) {
			grupoLancamentoSelecionado = Boolean.FALSE;
		}
		return grupoLancamentoSelecionado;
	}

	public void setGrupoLancamentoSelecionado(Boolean grupoLancamentoSelecionado) {
		this.grupoLancamentoSelecionado = grupoLancamentoSelecionado;
	}
	
	/**
	 * Alterna os valores da primeira e da segunda parcela
	 */
	public void alternarValoresDoLancamentoDo13PrimeiraParcela () {
		//RGN: So pode ser lancado a primeira OU a segunda parcela do 13
		if(getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getLancar13Parcela2()) {
			getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setLancar13Parcela2(!getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getLancar13Parcela1());			
		}
	}
	
	/**
	 * Alterna os valores da segunda e da primeira parcela
	 */
	public void alternarValoresDoLancamentoDo13SegundaParcela () {
		//RGN: So pode ser lancado a primeira OU a segunda parcela do 13
		if(getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getLancar13Parcela1()) {
			getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setLancar13Parcela1(!getLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().getLancar13Parcela2());	
		}
	}

	public List<FuncionarioCargoVO> getListaDeFuncionarios() {
		if (listaDeFuncionarios == null) {
			listaDeFuncionarios = new ArrayList<>();
		}
		return listaDeFuncionarios;
	}

	public void setListaDeFuncionarios(List<FuncionarioCargoVO> listaDeFuncionarios) {
		this.listaDeFuncionarios = listaDeFuncionarios;
	}
}