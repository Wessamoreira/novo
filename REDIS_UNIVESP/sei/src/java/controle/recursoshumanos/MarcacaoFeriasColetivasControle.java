package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasColetivasVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO.EnumCampoConsultaPeriodoAquisitivoFuncionario;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO.EnumCampoConsultaSecaoFolhaPagamento;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Controller("MarcacaoFeriasColetivasControle")
@Scope("viewScope")
@Lazy
public class MarcacaoFeriasColetivasControle extends SuperControle {

	private static final long serialVersionUID = 2208053835267809665L;

	private static final String TELA_FORM = "marcacaoFeriasColetivasForm";
	private static final String TELA_CONS = "marcacaoFeriasColetivasCons";
	private static final String CONTEXT_PARA_EDICAO = "itens";
	
	private static final String SUCESSO_MARCACAO_FERIAS = "msg_MarcacaoFeriasColetivas_sucesso_marcacaoFerias";
	private static final String SUCESSO_FINALIZAR_FERIAS = "msg_MarcacaoFeriasColetivas_sucesso_finalizarFerias";
	private static final String SUCESSO_CALCULAR_RECIBO = "msg_MarcacaoFeriasColetivas_sucesso_calcularRecibo";
	private static final String SUCESSO_CANCELAR_RECIBO = "msg_MarcacaoFerias_reciboCanceladoComSucesso";
	private static final String SUCESSO_EXCLUIR = "msg_MarcacaoFeriasColetivas_excluidoComSucesso";

	private MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO;

	private String[] formaContratacao = {};
	private String[] recebimento = {};
	private String[] situacao = {};

	private Boolean marcarTodosFormaContratacao;
	private Boolean marcarTodosRecebimento;
	private Boolean marcarTodosSituacao;

	private List<SelectItem> listaSelectItemFormacontratacao;
	private List<SelectItem> listaSelectItemRecebimento;
	private List<SelectItem> listaSelectItemSituacao;

	private Date dataInicial;
	private Date dataFinal;
	
	private String valorConsultaSituacao;

	private String campoConsultaSecaoFolhaPagamento;
    private String valorConsultaSecaoFolhaPagamento;
	private List<SecaoFolhaPagamentoVO> listaConsultaSecaoFolhaPagamento;
	
	private DataModelo controleConsultaOtimizadoHistorico;
	private DataModelo controleConsultaOtimizadoSecao;
	
	public MarcacaoFeriasColetivasControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	@Override
	public void consultarDados() {
		try {
			getControleConsultaOtimizado().setDataIni(dataInicial);
			getControleConsultaOtimizado().setDataFim(dataFinal);
			
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), valorConsultaSituacao);

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	private void consultarDadosHistorico() throws Exception {
		getControleConsultaOtimizadoHistorico().getListaFiltros().clear();
		getControleConsultaOtimizadoHistorico().getListaFiltros().add(getMarcacaoFeriasColetivasVO().getCodigo());
		getControleConsultaOtimizadoHistorico().setLimitePorPagina(10);
		getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizadoHistorico());
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		setMarcacaoFeriasColetivasVO(new MarcacaoFeriasColetivasVO());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		MarcacaoFeriasColetivasVO obj = (MarcacaoFeriasColetivasVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		setMarcacaoFeriasColetivasVO(obj);

		formaContratacao = obj.getTemplateLancamentoFolhaPagamentoVO().getFormaContratacaoFuncionario().split(";");
		recebimento = obj.getTemplateLancamentoFolhaPagamentoVO().getTipoRecebimento().split(";");
		situacao = obj.getTemplateLancamentoFolhaPagamentoVO().getSituacaoFuncionario().split(";");

		selecionarTodos();
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_dados_editar.name());

		try {
			setControleConsultaOtimizado(new DataModelo());
			getControleConsultaOtimizado().setPage(1);
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setLimitePorPagina(10);

			getControleConsultaOtimizado().getListaFiltros().add(obj.getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().persistir(getMarcacaoFeriasColetivasVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void marcarFerias() {
		try {
			getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().marcarFerias(getMarcacaoFeriasColetivasVO(), false, getUsuarioLogado());
			setMensagemID(SUCESSO_MARCACAO_FERIAS);
			consultarDadosHistorico();
		} catch (Exception e) {
			getMarcacaoFeriasColetivasVO().setSituacao(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularRecibo() {
		try {
			
			validarDadosMarcacaoNaoPodeSofrerAlteracaoNaFaseDeCalculada();
			getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().calcularRecibo(getMarcacaoFeriasColetivasVO(), false, getUsuarioLogado());
			setMensagemID(SUCESSO_CALCULAR_RECIBO);
			consultarDadosHistorico();
		} catch (Exception e) {
			getMarcacaoFeriasColetivasVO().setSituacao(SituacaoMarcacaoFeriasEnum.MARCADA);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Consulta a marcacao de ferias coletivas anterior para que nao haja alteracao no registro nessa fase
	 * @throws Exception
	 */
	private void validarDadosMarcacaoNaoPodeSofrerAlteracaoNaFaseDeCalculada() throws Exception {
		setMarcacaoFeriasColetivasVO(getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().consultarPorChavePrimaria(getMarcacaoFeriasColetivasVO().getCodigo().longValue()));
	}

	public void finalizarFerias() {
		try {
			getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().finalizarFerias(getMarcacaoFeriasColetivasVO(), false, getUsuarioLogado());
			setMensagemID(SUCESSO_FINALIZAR_FERIAS);
			consultarDadosHistorico();
		} catch (Exception e) {
			getMarcacaoFeriasColetivasVO().setSituacao(SituacaoMarcacaoFeriasEnum.CALCULADA);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void cancelarRecibo() {
		try {
			getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().cancelarRecibo(getMarcacaoFeriasColetivasVO(), false, getUsuarioLogado());
			setMensagemID(SUCESSO_CANCELAR_RECIBO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getMarcacaoFeriasColetivasInterfaceFacade().excluir(getMarcacaoFeriasColetivasVO(), false, getUsuarioLogado());
			setMarcacaoFeriasColetivasVO(new MarcacaoFeriasColetivasVO());
			setMensagemID(SUCESSO_EXCLUIR);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Consulta os historicos da marcação de ferias selecionada.
	 */
	public void consultarHistoricoMarcacaoFerias() {
		try {
			if (Uteis.isAtributoPreenchido(getMarcacaoFeriasColetivasVO().getCodigo()) && 
					!Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getListaConsulta())) {
				consultarDadosHistorico();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Realiza a paginação da consulta da marcacaoFeriasColetivasCons.xhtml
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Realiza a paginação da consulta da {@link ControleMarcacaoFeriasVO}
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerHistorico(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizadoHistorico().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizadoHistorico().setPage(dataScrollerEvent.getPage());
			consultarDadosHistorico();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Realiza a paginação da consulta da {@link SecaoFolhaPagamentoVO}
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerSecao(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizadoSecao().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizadoSecao().setPage(dataScrollerEvent.getPage());
			consultarSecaoFolhaPagamento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
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
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaPeriodoAquisitivoFuncionario.MATRICULA_CARGO.name());
		setListaConsulta(new ArrayList<>(0));
		setMarcacaoFeriasColetivasVO(new MarcacaoFeriasColetivasVO());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}
	
	public void selecionarFormaContratacao() {
		setMarcarTodosFormaContratacao(FormaContratacaoFuncionarioEnum.values().length == this.formaContratacao.length ? Boolean.TRUE : Boolean.FALSE); 
		
		getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setFormaContratacaoFuncionario("");
		for(String formaContratacao : this.formaContratacao ) {
			getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setFormaContratacaoFuncionario(
					getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getFormaContratacaoFuncionario().concat(formaContratacao).concat(";"));
		}
	}

	public void selecionarRecebimento() {
		setMarcarTodosRecebimento(TipoRecebimentoEnum.values().length == this.recebimento.length ? Boolean.TRUE : Boolean.FALSE); 

		getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setTipoRecebimento("");
		for(String recebimento : this.recebimento ) {
			getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setTipoRecebimento(
					getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getTipoRecebimento().concat(recebimento).concat(";"));
		}
	}

	public void selecionarSituacao() {
		setMarcarTodosSituacao(SituacaoFuncionarioEnum.values().length == this.situacao.length ? Boolean.TRUE : Boolean.FALSE); 
		
		getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setSituacaoFuncionario("");
		for(String situacao : this.situacao ) {
			getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setSituacaoFuncionario(
					getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getSituacaoFuncionario().concat(situacao).concat(";"));
		}

	}

	/**
	 * Seleciona todas as formas de contratacao.
	 */
	public void selecionarTodosFormaContratacao() {
		getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setFormaContratacaoFuncionario("");
		if(getMarcarTodosFormaContratacao()) {
			for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
				getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setFormaContratacaoFuncionario(
						getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getFormaContratacaoFuncionario().concat(formaContratacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		formaContratacao = getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getFormaContratacaoFuncionario().split(";");
	}

	/**
	 * Seleciona todas os recebimetos.
	 */
	public void selecionarTodosRecebimento() {
		getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setTipoRecebimento("");
		if(getMarcarTodosRecebimento()) {
			for (TipoRecebimentoEnum formaRecebimentoEnum : TipoRecebimentoEnum.values()) {
				getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setTipoRecebimento(
						getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getTipoRecebimento().concat(formaRecebimentoEnum.toString()).concat(";"));
			}
		}

		recebimento = getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getTipoRecebimento().split(";");
	}

	/**
	 * Seleciona todas as situacoes.
	 */
	public void selecionarTodosSituacao() {
		getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setSituacaoFuncionario("");
		if(getMarcarTodosSituacao()) {
			for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
				getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setSituacaoFuncionario(
						getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getSituacaoFuncionario().concat(situacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		situacao = getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().getSituacaoFuncionario().split(";");
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
	 * Preenche os campos:
	 * - data final de gozo
	 * - data pagamento
	 * - inicio aviso
	 * - qtd dias
	 * 
	 */
	public void preencherCamposDoPeriodoDeGozo() {
		calcularQtdDiaGozo();
		calcularDataFinalGozo();
		getMarcacaoFeriasColetivasVO().setDataPagamento(UteisData.adicionarDiasEmData(getMarcacaoFeriasColetivasVO().getDataInicioGozo(), -2));
		getMarcacaoFeriasColetivasVO().setDataInicioAviso(UteisData.adicionarDiasEmData(getMarcacaoFeriasColetivasVO().getDataInicioGozo(), -30));
	}

	public void limparDadosSecaoFolhaPagamento() {
		getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
	}

	/**
	 * Calcula a data final de periodo de gozo das ferias.
	 */
	public void calcularDataFinalGozo() {
		//A data de inicio ja conta como ferias, por isso a subtração de 1 dia
		getMarcacaoFeriasColetivasVO().setDataFinalGozo(UteisData.adicionarDiasEmData(getMarcacaoFeriasColetivasVO().getDataInicioGozo(), getMarcacaoFeriasColetivasVO().getQuantidadeDias()-1));	
	}
	
	public void verificacaoDiasAbono() {
		if(!getMarcacaoFeriasColetivasVO().getAbono()) {
			getMarcacaoFeriasColetivasVO().setQuantidadeDiasAbono(0);
			calcularQtdDiaGozo();	
		}
	}

	public void calcularQtdDiaGozo() {
		getMarcacaoFeriasColetivasVO().setQuantidadeDias(30 - getMarcacaoFeriasColetivasVO().getQuantidadeDiasAbono());
	}

	/**
	 * Botao de consulta da Secao Folha de Pagamento chamado no formulario
	 */
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
	
	/**
	 * Selecionar Secao Folha de Pagamento na lista de secoes consultadas
	 */
	public void selecionarSecaoFolhaPagamento() {
    	SecaoFolhaPagamentoVO obj = (SecaoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("itemSecaoFolhaPagamento");
    	getMarcacaoFeriasColetivasVO().getTemplateLancamentoFolhaPagamentoVO().setSecaoFolhaPagamento(obj);
    	this.getListaConsultaSecaoFolhaPagamento().clear();
    }
	
	/**
	 * Apresentar resultado da consulta secao
	 * true: apresenta porque contem dados na lista
	 * false: nao apresenta porque nao contem dados na lista
	 * @return
	 */
	public boolean getApresentarResultadoConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 0;
	}
	
	//getter and setter
	public MarcacaoFeriasColetivasVO getMarcacaoFeriasColetivasVO() {
		if (marcacaoFeriasColetivasVO == null)
			marcacaoFeriasColetivasVO = new MarcacaoFeriasColetivasVO();
		return marcacaoFeriasColetivasVO;
	}

	public void setMarcacaoFeriasColetivasVO(MarcacaoFeriasColetivasVO marcacaoFeriasColetivasVO) {
		this.marcacaoFeriasColetivasVO = marcacaoFeriasColetivasVO;
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

	public Boolean getMarcarTodosFormaContratacao() {
		if (marcarTodosFormaContratacao == null) {
			marcarTodosFormaContratacao = Boolean.FALSE;
		}
		return marcarTodosFormaContratacao;
	}

	public void setMarcarTodosFormaContratacao(Boolean marcarTodosFormaContratacao) {
		this.marcarTodosFormaContratacao = marcarTodosFormaContratacao;
	}

	public Boolean getMarcarTodosRecebimento() {
		return marcarTodosRecebimento;
	}

	public void setMarcarTodosRecebimento(Boolean marcarTodosRecebimento) {
		this.marcarTodosRecebimento = marcarTodosRecebimento;
	}

	public Boolean getMarcarTodosSituacao() {
		return marcarTodosSituacao;
	}

	public void setMarcarTodosSituacao(Boolean marcarTodosSituacao) {
		this.marcarTodosSituacao = marcarTodosSituacao;
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

	public String getValorConsultaSituacao() {
		if (valorConsultaSituacao ==  null) {
			valorConsultaSituacao = "TODOS";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public String getCampoConsultaSecaoFolhaPagamento() {
		if (campoConsultaSecaoFolhaPagamento == null)
			campoConsultaSecaoFolhaPagamento = "";
		return campoConsultaSecaoFolhaPagamento;
	}

	public void setCampoConsultaSecaoFolhaPagamento(String campoConsultaSecaoFolhaPagamento) {
		this.campoConsultaSecaoFolhaPagamento = campoConsultaSecaoFolhaPagamento;
	}

	public String getValorConsultaSecaoFolhaPagamento() {
		if (valorConsultaSecaoFolhaPagamento == null)
			valorConsultaSecaoFolhaPagamento = "";
		return valorConsultaSecaoFolhaPagamento;
	}

	public void setValorConsultaSecaoFolhaPagamento(String valorConsultaSecaoFolhaPagamento) {
		this.valorConsultaSecaoFolhaPagamento = valorConsultaSecaoFolhaPagamento;
	}

	public List<SecaoFolhaPagamentoVO> getListaConsultaSecaoFolhaPagamento() {
		if (listaConsultaSecaoFolhaPagamento == null)
			listaConsultaSecaoFolhaPagamento = new ArrayList<>();
		return listaConsultaSecaoFolhaPagamento;
	}

	public void setListaConsultaSecaoFolhaPagamento(List<SecaoFolhaPagamentoVO> listaConsultaSecaoFolhaPagamento) {
		this.listaConsultaSecaoFolhaPagamento = listaConsultaSecaoFolhaPagamento;
	}

	public DataModelo getControleConsultaOtimizadoHistorico() {
		if (controleConsultaOtimizadoHistorico == null) {
			controleConsultaOtimizadoHistorico = new DataModelo();
		}
		return controleConsultaOtimizadoHistorico;
	}

	public void setControleConsultaOtimizadoHistorico(DataModelo controleConsultaOtimizadoHistorico) {
		this.controleConsultaOtimizadoHistorico = controleConsultaOtimizadoHistorico;
	}

	public DataModelo getControleConsultaOtimizadoSecao() {
		if (controleConsultaOtimizadoSecao == null) {
			controleConsultaOtimizadoSecao = new DataModelo();
		}
		return controleConsultaOtimizadoSecao;
	}

	public void setControleConsultaOtimizadoSecao(DataModelo controleConsultaOtimizadoSecao) {
		this.controleConsultaOtimizadoSecao = controleConsultaOtimizadoSecao;
	}
}