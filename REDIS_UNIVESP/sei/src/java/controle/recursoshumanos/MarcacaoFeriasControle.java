package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoVO;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.MarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO.EnumCampoConsultaPeriodoAquisitivoFuncionario;
import negocio.comuns.recursoshumanos.ReciboFeriasEventoVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoPeriodoAquisitivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Controller("MarcacaoFeriasControle")
@Scope("viewScope")
@Lazy
public class MarcacaoFeriasControle extends SuperControle {

	private static final long serialVersionUID = 2208053835267809665L;

	private static final String TELA_FORM = "marcacaoFeriasForm";
	private static final String TELA_CONS = "marcacaoFeriasCons";
	private static final String CONTEXT_PARA_EDICAO = "itens";
	
	private static final String SUCESSO_FINALIZAR_FERIAS = "msg_MarcacaoFeriasColetivas_sucesso_finalizarFerias";
	private static final String SUCESSO_CALCULAR_RECIBO = "msg_MarcacaoFerias_reciboCalculadoComSucesso";
	
	private MarcacaoFeriasVO marcacaoFeriasVO;
	private List<SelectItem> listaDeSituacoesDaMarcacao;

	private ReciboFeriasVO reciboFeriasVO;
	private ReciboFeriasEventoVO reciboFeriasEventoVO;
	
	private String valorConsultaSituacao;
	private String valorConsultaSituacaoMarcacao;
	
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;
	private String campoConsultaEvento;
	private String valorConsultaEvento;

	private LancamentoFolhaPagamentoVO lancamento;

	private ControleMarcacaoFeriasVO controleMarcacaoFeriasVO;

	private List<SelectItem> listaSelectItemCompetenciaPeriodo;
	
	@PostConstruct
	public void carregarFichaFinanceiraVindoDeOutraTela () {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();

		editarMarcacaoFeriasVindoDoFuncionarioCargo();

		editarMarcacaoFeriasVindoDoPeriodoAquisitivo();
	}

	/**
	 * Preenche a marcação de ferias vido da tela de Funcionario Cargo.
	 */
	private void editarMarcacaoFeriasVindoDoFuncionarioCargo() {
		FuncionarioCargoVO funcionarioCargoVO = (FuncionarioCargoVO) context().getExternalContext().getSessionMap().get("funcionarioCargo");
		try {
			if (Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
				funcionarioCargoVO = getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(funcionarioCargoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getMarcacaoFeriasVO().setFuncionarioCargoVO(funcionarioCargoVO);
				
			}
		} catch (Exception e) {
			setMensagemID(e.getMessage());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("funcionarioCargo");
		}
	}

	/**
	 * Preenche a marcação de ferias vido da tela de periodo aquisitivo
	 */
	private void editarMarcacaoFeriasVindoDoPeriodoAquisitivo() {
		PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO = (PeriodoAquisitivoFeriasVO) context().getExternalContext().getSessionMap().get("marcacaoFeriasChamadoDoPeriodoArquisitivo");
		
		try {
			if (Uteis.isAtributoPreenchido(periodoAquisitivoFeriasVO)) {
				setMarcacaoFeriasVO(getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarMarcacaoPorPeriodoAquisitivo(periodoAquisitivoFeriasVO, false, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("marcacaoFeriasChamadoDoPeriodoArquisitivo");
		}
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), getValorConsultaSituacao(), getValorConsultaSituacaoMarcacao());
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		setMarcacaoFeriasVO(new MarcacaoFeriasVO());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		MarcacaoFeriasVO obj = (MarcacaoFeriasVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		setMarcacaoFeriasVO(obj);
		return prepararDadosParaEdicao(obj);
	}

	public String prepararDadosParaEdicao(MarcacaoFeriasVO obj) {
		try {
			realizarAtualizacaoDadosDaTela(obj);
			setControleConsultaOtimizado(new DataModelo());
			calcularQuantidadeFaltas();
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	private void realizarAtualizacaoDadosDaTela(MarcacaoFeriasVO obj) throws Exception {
		setMarcacaoFeriasVO(getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS));
		setReciboFeriasVO(getFacadeFactory().getReciboFeriasInterfaceFacade().consultarPorMarcacao(getMarcacaoFeriasVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), false));
		getReciboFeriasVO().setListaReciboEvento(getFacadeFactory().getReciboFeriasEventoInterfaceFacade().consultarPorReciboFerias(getReciboFeriasVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

		setControleMarcacaoFeriasVO(getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarDadosPorMarcacaoFerias(obj));
	}

	public void persistir() {
		try {
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().persistirMarcacaoFerias(getMarcacaoFeriasVO(), true, getUsuarioLogado());
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
		setMarcacaoFeriasVO(new MarcacaoFeriasVO());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}
	
	public void cancelarEdicao() {
		setReciboFeriasEventoVO(new ReciboFeriasEventoVO());
		limparDadosEvento();
	}
	
	public void editarEvento() {
		try {
			ReciboFeriasEventoVO obj =(ReciboFeriasEventoVO) context().getExternalContext().getRequestMap().get("reciboEvento");
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setReciboFeriasEventoVO( (ReciboFeriasEventoVO) SerializationUtils.clone(obj));
        	getReciboFeriasEventoVO().setInformadoManual(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}

	public void removerItem(ReciboFeriasEventoVO obj) {
		getReciboFeriasVO().getListaReciboEvento().removeIf(p -> p.getEvento().getCodigo().equals(obj.getEvento().getCodigo()));
		limparDadosReciboEvento();
	}

	private void limparDadosReciboEvento() {
		setReciboFeriasEventoVO(new ReciboFeriasEventoVO());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}
	
	public void limparDadosEvento() {
		getReciboFeriasEventoVO().setEvento(new EventoFolhaPagamentoVO());
	}
	
	public void consultarMarcacaoDeFeriasDoFuncionarioPelaMatriculaDoCargo () {
		
		try {
			FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getMarcacaoFeriasVO().getFuncionarioCargoVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if(!Uteis.isAtributoPreenchido(funcionarioCargo)) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), UteisJSF.internacionalizar("msg_MarcacaoFerias_funcionarioNaoEncontrado"));
				setMarcacaoFeriasVO(new MarcacaoFeriasVO());
				setReciboFeriasEventoVO(new ReciboFeriasEventoVO());
				setReciboFeriasVO(new ReciboFeriasVO());
				return;
			}
			setMarcacaoFeriasVO(inicializarMarcacaoFerias(funcionarioCargo));
			
			getMarcacaoFeriasVO().setFuncionarioCargoVO(funcionarioCargo);
			
			calcularQuantidadeFaltas();
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	/**
	 * Deve existir apenas 1 marcacao de ferias aberta por funcionario
     * Primeiro deve se fechar uma marcacao de ferias para somente depois disso poder abrir outra
	 * @param funcionarioCargo
	 * @return
	 * @throws Exception
	 */
	private MarcacaoFeriasVO inicializarMarcacaoFerias(FuncionarioCargoVO funcionarioCargo) throws Exception {
		MarcacaoFeriasVO mf = getFacadeFactory().getMarcacaoFeriasInterfaceFacade().consultarMarcacaoDiferenteDeFechadaPorFuncionario(funcionarioCargo.getMatriculaCargo());
		
		if(!Uteis.isAtributoPreenchido(mf)) {
			mf = new MarcacaoFeriasVO();
			mf.setFuncionarioCargoVO(funcionarioCargo);
			mf.setSituacaoMarcacao(SituacaoMarcacaoFeriasEnum.MARCADA);
			mf.setPeriodoAquisitivoFeriasVO(getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarPeriodoAquisitivoValidoParaFerias(funcionarioCargo));
			
			if(!Uteis.isAtributoPreenchido(mf.getPeriodoAquisitivoFeriasVO())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_PeriodoAquisitivoFerias_naoExistePeriodoAquisito"));
			}
		}
		
		return mf; 
	}

	public void calcularQtdDiaGozo() {
		if (getMarcacaoFeriasVO().getAbono()) {
			getMarcacaoFeriasVO().setQtdDias(getMarcacaoFeriasVO().getQtdDias()-getMarcacaoFeriasVO().getQtdDiasAbono());
		}
	}
	
	public void verificacaoDiasAbono() {
		if(!getMarcacaoFeriasVO().getAbono()) {
			getMarcacaoFeriasVO().setQtdDiasAbono(0);
			calcularQtdDiaGozo();	
		}
	}
	
	public void calcularRecibo() {
		try {
			validarReciboLancadoNoContraCheque();
			atualizarListaDeEventos();
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().calcularRecibo(getMarcacaoFeriasVO(), getReciboFeriasVO(), true, getUsuarioLogado());
			realizarAtualizacaoDadosDaTela(getMarcacaoFeriasVO());
			setMensagemID(SUCESSO_CALCULAR_RECIBO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}

	private void validarReciboLancadoNoContraCheque() throws Exception {
		
		ControleMarcacaoFeriasVO controle = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarDadosPorMarcacaoFerias(getMarcacaoFeriasVO());
		
		if (Uteis.isAtributoPreenchido(controle.getAdiantamentoFerias()) || Uteis.isAtributoPreenchido(controle.getReciboFerias())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_funcionarioComReciboLancado"));
		}
	}

	/**
	 * Atualiza os eventos da folha de pagamento da lista de Contra Cheque Eventos  
	 */
	private void atualizarListaDeEventos() {
		
		if(!getReciboFeriasVO().getListaReciboEvento().isEmpty()) {
			
			EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
			
			Iterator<ReciboFeriasEventoVO> iterator = getReciboFeriasVO().getListaReciboEvento().iterator();

			while(iterator.hasNext()) {
				ReciboFeriasEventoVO reciboEventoVO = iterator.next();
				
				try {
					eventoFolhaPagamentoVO = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(reciboEventoVO.getEvento().getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS);
					reciboEventoVO.setEvento(eventoFolhaPagamentoVO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void valorReciboFeriasEventoInformadoManual() {
		getReciboFeriasEventoVO().setInformadoManual(Boolean.TRUE);
	}
	
	public String corDoEvento() {
		ReciboFeriasEventoVO obj = (ReciboFeriasEventoVO) context().getExternalContext().getRequestMap().get("reciboEvento");

		switch (obj.getEvento().getTipoLancamento()) {
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
	
	public void visualizarEventoFolhaPagamento() {
		ReciboFeriasEventoVO obj = (ReciboFeriasEventoVO) context().getExternalContext().getRequestMap().get("reciboEvento");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("eventoFolhaPagamentoDoContraCheque", obj.getEvento());
		}
		removerControleMemoriaFlashTela(EventoFolhaPagamentoControle.class.getSimpleName());
	}
	
	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(getReciboFeriasEventoVO().getEvento().getIdentificador())) {
				getReciboFeriasEventoVO().setEvento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(getReciboFeriasEventoVO().getEvento().getIdentificador(), false, getUsuarioLogado()));
			} else {
				getReciboFeriasEventoVO().setEvento(new EventoFolhaPagamentoVO());
			}
			getReciboFeriasEventoVO().setItemEmEdicao(Boolean.FALSE);
		} catch (Exception e) {
			getReciboFeriasEventoVO().setEvento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Adiciona o evento no recibo
	 */
	public void adicionarEventoFolhaPagamento() {
		try {
			getFacadeFactory().getReciboFeriasEventoInterfaceFacade().validarDadosReciboEvento(getReciboFeriasEventoVO(), getReciboFeriasVO());
			if (getReciboFeriasEventoVO().getValorReferencia().intValue() != 0) {
				getReciboFeriasEventoVO().setInformadoManual(true);
			}

			if (getReciboFeriasEventoVO().getItemEmEdicao()) {
				
				Iterator<ReciboFeriasEventoVO> i = getReciboFeriasVO().getListaReciboEvento().iterator();
				int index = 0;
				int aux = -1;
				ReciboFeriasEventoVO objAux = new ReciboFeriasEventoVO();
				while(i.hasNext()) {
					ReciboFeriasEventoVO objExistente = i.next();

					if (objExistente.getCodigo().equals(getReciboFeriasEventoVO().getCodigo()) && objExistente.getItemEmEdicao()){
						getReciboFeriasEventoVO().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getReciboFeriasEventoVO();
		 			}
		            index++;
				}

				if(aux >= 0) {
					getReciboFeriasVO().getListaReciboEvento().set(aux, objAux);
				}
			} else {		
				getReciboFeriasVO().getListaReciboEvento().add(0, getReciboFeriasEventoVO());
			}

			limparDadosReciboEvento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getCampoConsultaEvento());
			} 
			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(getCampoConsultaEvento(), getValorConsultaEvento(), "ATIVO" , false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarEvento() {
		try {
			EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
			obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS);
			getReciboFeriasEventoVO().setEvento(obj);

			valorConsultaEvento = "";
			campoConsultaEvento = "";
			getListaEventosFolhaPagamento().clear();			
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name() , e.getMessage());
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void cancelarRecibo () {
		try {
			validarReciboLancadoNoContraCheque();
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().cancelarRecibo(getMarcacaoFeriasVO(), true, getUsuarioLogado());
			setMensagemID("msg_MarcacaoFerias_excluidoComSucesso");
			realizarAtualizacaoDadosDaTela(getMarcacaoFeriasVO());			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name() , e.getMessage());
		}
	}
	
	public void excluir() {
		try {
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().excluir(getMarcacaoFeriasVO(), false, getUsuarioLogado());
			setMensagemID("msg_MarcacaoFerias_reciboCanceladoComSucesso");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void valorInformadoManual() {
		ReciboFeriasEventoVO obj = (ReciboFeriasEventoVO) context().getExternalContext().getRequestMap().get("reciboEvento");
		if (obj.getValorReferencia().intValue() != 0 ) {
			int contador = 0; 
			for (ReciboFeriasEventoVO reciboEvento : getReciboFeriasVO().getListaReciboEvento()) {
				if (reciboEvento.getEvento().getCodigo().equals(obj.getEvento().getCodigo())) {				
					getReciboFeriasVO().getListaReciboEvento().get(contador).setInformadoManual(true);
					break;
				}
				contador++;
			}
		}
	}
	
	public void referenciaInformadoManual() {
		ContraChequeEventoVO obj = (ContraChequeEventoVO) context().getExternalContext().getRequestMap().get("contrachequeEvento");
		if (obj.getValorReferencia().intValue() != 0 ) {
			int contador = 0; 
			for (ReciboFeriasEventoVO reciboFeriasEvento : getReciboFeriasVO().getListaReciboEvento()) {
				if (reciboFeriasEvento.getCodigo().equals(obj.getCodigo())) {		
					getReciboFeriasVO().getListaReciboEvento().get(contador).setInformadoManual(true);
					getReciboFeriasVO().getListaReciboEvento().get(contador).getEvento().setReferencia(obj.getEventoFolhaPagamento().getReferencia());
					break;
				}
				contador++;
			}
		}
	}

	public void lancarAdiantamento() {
		try {
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().validarDadosAdiantamento(getMarcacaoFeriasVO());
			//getFacadeFactory().getMarcacaoFeriasInterfaceFacade().lancarAdiantamendoNaFolha(getMarcacaoFeriasVO(), getReciboFeriasVO(), getLancamento());
			setMensagemID("msg_MarcacaoFerias_adiantamentoRealizadoComSucesso", Uteis.SUCESSO, true);
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void lancarContraCheque() {
		try {
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().validarDadosLancarContraCheque(getMarcacaoFeriasVO());
			//getFacadeFactory().getMarcacaoFeriasInterfaceFacade().lancarEventosDoReciboNoContraCheque(getMarcacaoFeriasVO(), getReciboFeriasVO(), getLancamento());
			setMensagemID("msg_MarcacaoFerias_reciboLancadoNoContraChequeComSucesso", Uteis.SUCESSO, true);
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherInformacoesParaLancamentoNoContraCheque() {
		preencherCompetenciaFolhaPagamento();
	}
	
	private void preencherCompetenciaFolhaPagamento() {
		CompetenciaFolhaPagamentoVO competencia = new CompetenciaFolhaPagamentoVO();
	
		try {
			competencia = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
			getLancamento().setCompetenciaFolhaPagamentoVO(competencia);
			getLancamento().setDataCompetencia(competencia.getDataCompetencia());
			
			preencherCompetenciaPeriodo(getLancamento().getCompetenciaFolhaPagamentoVO());
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), "msg_erro_LancamentoFolhaPagamento_competenciaAtivaNaoEncontrado");
		}
	}
	
	private void preencherCompetenciaPeriodo(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		try {
			List<CompetenciaPeriodoFolhaPagamentoVO> periodos = getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorCompetenciaFolhaPagamento(competenciaFolhaPagamentoVO);
			setListaSelectItemCompetenciaPeriodo(UtilSelectItem.getListaSelectItem(periodos, "codigo", "periodoApresentacao", false));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Metodo que calcula a Data final de gozo.
	 */
	public void calcularDataFinalGozo() {
		//A data de inicio ja conta como ferias, por isso a subtração de 1 dia
		getMarcacaoFeriasVO().setDataFinalGozo(UteisData.adicionarDiasEmData(getMarcacaoFeriasVO().getDataInicioGozo(), getMarcacaoFeriasVO().getQtdDias()-1));	
	}
	
	public void atualizarQuantidadeFaltas() {
		consultarMarcacaoDeFeriasDoFuncionarioPelaMatriculaDoCargo();			

		calcularQuantidadeFaltas();
	}

	private void calcularQuantidadeFaltas() {
		if (Uteis.isAtributoPreenchido(getMarcacaoFeriasVO().getPeriodoAquisitivoFeriasVO())) {
			getMarcacaoFeriasVO().setQtdFaltasPeriodo(
				getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().consultarQuantidadeFaltasPorPeriodoAquisitivo(getMarcacaoFeriasVO().getPeriodoAquisitivoFeriasVO().getCodigo()));

			calculcarQuantidadeDiasDeduzirFaltas();
			calcularDataFinalGozo();
		}
	}

	/**
	 * Calcula a quantidade de faltas que vai ser deduzida das ferias do funcionario.
	 */
	private void calculcarQuantidadeDiasDeduzirFaltas() {
		Integer valorDeduzirDias = getFacadeFactory().getFaixaValorInterfaceFacade().consultarValorEntreLimiteInferiorELimiteSuperior(getMarcacaoFeriasVO().getQtdFaltasPeriodo());
		getMarcacaoFeriasVO().setQtdDias(30 - valorDeduzirDias);
	}

	/**
	 * Finalizar as ferias do funcionario
	 */
	public void finalizarFerias() {
		try {
			if(!Uteis.isAtributoPreenchido(getMarcacaoFeriasVO())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_MarcacaoFerias_invalida"));
			}
			getFacadeFactory().getMarcacaoFeriasInterfaceFacade().finalizarMarcacaoDeFerias(getMarcacaoFeriasVO(), false, null, false, getUsuarioLogado());
			setMensagemID(SUCESSO_FINALIZAR_FERIAS);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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
		calculcarQuantidadeDiasDeduzirFaltas();
		calcularQtdDiaGozo();
		calcularDataFinalGozo();
		getMarcacaoFeriasVO().setDataPagamento(UteisData.adicionarDiasEmData(getMarcacaoFeriasVO().getDataInicioGozo(), -2));
		getMarcacaoFeriasVO().setDataInicioAviso(UteisData.adicionarDiasEmData(getMarcacaoFeriasVO().getDataInicioGozo(), -30));
	}
	
	//GETTER AND SETTER
	public LancamentoFolhaPagamentoVO getLancamento() {
		if (lancamento == null)
			lancamento = new LancamentoFolhaPagamentoVO();
		return lancamento;
	}

	public void setLancamento(LancamentoFolhaPagamentoVO lancamento) {
		this.lancamento = lancamento;
	}
	
	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		if (listaEventosFolhaPagamento == null)
			listaEventosFolhaPagamento = new ArrayList<>();
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
	}

	public String getCampoConsultaEvento() {
		if (campoConsultaEvento == null)
			campoConsultaEvento = "";
		return campoConsultaEvento;
	}

	public void setCampoConsultaEvento(String campoConsultaEvento) {
		this.campoConsultaEvento = campoConsultaEvento;
	}

	public String getValorConsultaEvento() {
		if (valorConsultaEvento == null)
			valorConsultaEvento = "";
		return valorConsultaEvento;
	}

	public void setValorConsultaEvento(String valorConsultaEvento) {
		this.valorConsultaEvento = valorConsultaEvento;
	}
	
	public List<SelectItem> getListaSelectItemCompetenciaPeriodo() {
		if (listaSelectItemCompetenciaPeriodo == null)
			listaSelectItemCompetenciaPeriodo = new ArrayList<>();
		return listaSelectItemCompetenciaPeriodo;
	}

	public void setListaSelectItemCompetenciaPeriodo(List<SelectItem> listaSelectItemCompetenciaPeriodo) {
		this.listaSelectItemCompetenciaPeriodo = listaSelectItemCompetenciaPeriodo;
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoPeriodo() {
		return SituacaoPeriodoAquisitivoEnum.getValorSituacaoPeriodoAquisitivoEnum();
	}
	
	public List<SelectItem> getListaSelectItemSituacaoMarcacaoFerias() {
		return SituacaoMarcacaoFeriasEnum.getValorSituacaoMarcacaoFeriasEnum();
	}

	public String getValorConsultaSituacaoMarcacao() {
		if (valorConsultaSituacaoMarcacao == null) {
			valorConsultaSituacaoMarcacao = "TODOS";
		}
		return valorConsultaSituacaoMarcacao;
	}

	public void setValorConsultaSituacaoMarcacao(String valorConsultaSituacaoMarcacao) {
		this.valorConsultaSituacaoMarcacao = valorConsultaSituacaoMarcacao;
	}
	
	public String getValorConsultaSituacao() {
		if (valorConsultaSituacao == null) {
			valorConsultaSituacao = "TODOS";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public MarcacaoFeriasVO getMarcacaoFeriasVO() {
		if (marcacaoFeriasVO == null)
			marcacaoFeriasVO = new MarcacaoFeriasVO();
		return marcacaoFeriasVO;
	}

	public void setMarcacaoFeriasVO(MarcacaoFeriasVO marcacaoFeriasVO) {
		this.marcacaoFeriasVO = marcacaoFeriasVO;
	}

	public List<SelectItem> getListaDeSituacoesDaMarcacao() {
		if (listaDeSituacoesDaMarcacao == null)
			listaDeSituacoesDaMarcacao = SituacaoMarcacaoFeriasEnum.getValorSituacaoMarcacaoFeriasEnum();
		return listaDeSituacoesDaMarcacao;
	}

	public void setListaDeSituacoesDaMarcacao(List<SelectItem> listaDeSituacoesDaMarcacao) {
		this.listaDeSituacoesDaMarcacao = listaDeSituacoesDaMarcacao;
	}

	public ReciboFeriasVO getReciboFeriasVO() {
		if (reciboFeriasVO == null)
			reciboFeriasVO = new ReciboFeriasVO();
		return reciboFeriasVO;
	}

	public void setReciboFeriasVO(ReciboFeriasVO reciboFeriasVO) {
		this.reciboFeriasVO = reciboFeriasVO;
	}

	public ReciboFeriasEventoVO getReciboFeriasEventoVO() {
		if (reciboFeriasEventoVO == null)
			reciboFeriasEventoVO = new ReciboFeriasEventoVO();
		return reciboFeriasEventoVO;
	}

	public void setReciboFeriasEventoVO(ReciboFeriasEventoVO reciboFeriasEventoVO) {
		this.reciboFeriasEventoVO = reciboFeriasEventoVO;
	}

	public ControleMarcacaoFeriasVO getControleMarcacaoFeriasVO() {
		if (controleMarcacaoFeriasVO == null) {
			controleMarcacaoFeriasVO = new ControleMarcacaoFeriasVO();
		}
		return controleMarcacaoFeriasVO;
	}

	public void setControleMarcacaoFeriasVO(ControleMarcacaoFeriasVO controleMarcacaoFeriasVO) {
		this.controleMarcacaoFeriasVO = controleMarcacaoFeriasVO;
	}
}