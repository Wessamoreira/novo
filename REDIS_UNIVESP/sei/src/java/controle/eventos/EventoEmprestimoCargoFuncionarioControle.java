package controle.eventos;

import java.io.Serializable;
import java.math.BigDecimal;
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

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.EventoEmprestimoCargoFuncionarioVO;
import negocio.comuns.recursoshumanos.EventoEmprestimoCargoFuncionarioVO.EnumCampoConsultaEventoEmprestimoCargoFuncionario;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TipoEmprestimoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Gilberto Nery
 *
 */
@Controller("EventoEmprestimoCargoFuncionarioControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings({"rawtypes"})
public class EventoEmprestimoCargoFuncionarioControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -4699314420531096295L;

	private static final String TELA_FORM = "eventoEmprestimoCargoFuncionarioForm";
	private static final String TELA_CONS = "eventoEmprestimoCargoFuncionarioCons";
	private static final String CONTEXT_PARA_EDICAO = "eventoEmprestimoItens";
	private static final String CONTEXT_CONSULTA_EVENTO_EMPRESTIMO_FUNCIONARIO = "eventoFuncionario";

	private EventoEmprestimoCargoFuncionarioVO eventoEmprestimoCargoFuncionarioVO;
	protected FuncionarioCargoVO funcionarioCargoVO;
	private List<EventoFolhaPagamentoVO> listaDeEventos;
	private List<EventoEmprestimoCargoFuncionarioVO> listaDeEventosEmprestimosDoFuncionario;
	private EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
	
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;
	
	@Deprecated
	private String campoConsultaTipoEmprestimo;
	@Deprecated
	private String valorConsultaTipoEmprestimo;
	@Deprecated
	private List<TipoEmprestimoVO> listaTipoEmprestimo;

	private String valorConsultaSituacao;
	
	public EventoEmprestimoCargoFuncionarioControle() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaEventoEmprestimoCargoFuncionario.FUNCIONARIO.name());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}
	
	@PostConstruct
	public void carregarEventosEmprestimosVindosDaTelaDoFuncionarioCargo () {
		
		FuncionarioCargoVO funcionarioCargo = (FuncionarioCargoVO) context().getExternalContext().getSessionMap().get("funcionarioCargo");
		
		try {
			if (Uteis.isAtributoPreenchido(funcionarioCargo)) {
				getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(funcionarioCargo.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				editar(funcionarioCargo);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("funcionarioCargo");
		}
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		funcionarioCargoVO = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		return editar(funcionarioCargoVO);
	}
	
	public String editar(FuncionarioCargoVO funcionarioCargoVO) {
		return prepararDadosParaEdicao(funcionarioCargoVO);
	}
	
	public String prepararDadosParaEdicao(FuncionarioCargoVO funcionarioCargoVO) {
		try {
			setFuncionarioCargoVO(funcionarioCargoVO);
			
			//Consulta a lista de eventos cadastrados para o funcionario
			setListaDeEventosEmprestimosDoFuncionario(getFacadeFactory().getEventoEmprestimoCargoFuncionarioInterfaceFacade().consultarPorCargoFuncionario(funcionarioCargoVO, getControleConsultaOtimizado()));
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getEventoEmprestimoCargoFuncionarioInterfaceFacade().persistir(getListaDeEventosEmprestimosDoFuncionario(), getFuncionarioCargoVO(), true, getUsuarioLogado());
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
	
	public void scrollerListenerEventoFolhaPagamento(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarEvento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerTipoEmprestimo(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarTipoEmprestimo();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getEventoEmprestimoCargoFuncionarioInterfaceFacade().consultarPorEnumCampoConsultaEventoEmprestimoCargoFuncionario(getControleConsultaOtimizado(), valorConsultaSituacao);
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			
			if(validarSeExisteEmprestimoQueForamPagos()) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), UteisJSF.internacionalizar("msg_dadosNaoPodemSerExcluidosEmprestimoComParcelaPaga"), Uteis.ERRO);
			} else {
				getFacadeFactory().getEventoEmprestimoCargoFuncionarioInterfaceFacade().excluir(getFuncionarioCargoVO(), true, getUsuarioLogado());
				setListaDeEventosEmprestimosDoFuncionario(new ArrayList<>());
				setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
				setMensagemID(MSG_TELA.msg_dados_excluidos.name());				
			}
			
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
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaEventoEmprestimoCargoFuncionario.FUNCIONARIO.name());
		setListaConsulta(new ArrayList(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Rotina responsavel por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaEventoFolhaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}

	public void adicionarEvento () {
		if(validarSeObjetoExisteNaListaDeEventosEmprestimosDoFuncionario(getEventoEmprestimoCargoFuncionarioVO().getEventoFolhaPagamentoVO()) ||
				!Uteis.isAtributoPreenchido(getEventoEmprestimoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getCodigo())) {
			if (!getEventoEmprestimoCargoFuncionarioVO().getItemEmEdicao()) {
				return;
			}
	    }
		
		if (getEventoEmprestimoCargoFuncionarioVO().getQuitado() && !Uteis.isAtributoPreenchido(getEventoEmprestimoCargoFuncionarioVO().getDataPagamento())) {
			setMensagemDetalhada("Campo Data de Pagamento é obrigatório.");
			return;
		}

		if (getEventoEmprestimoCargoFuncionarioVO().getItemEmEdicao()) {
			Iterator<EventoEmprestimoCargoFuncionarioVO> i = getListaDeEventosEmprestimosDoFuncionario().iterator();
			int index = 0;
			int aux = -1;
			EventoEmprestimoCargoFuncionarioVO objAux = new EventoEmprestimoCargoFuncionarioVO();
			while(i.hasNext()) {
				EventoEmprestimoCargoFuncionarioVO objExistente = i.next();
				
				if (objExistente.getEventoFolhaPagamentoVO().getCodigo().equals(getEventoEmprestimoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getCodigo()) && objExistente.getItemEmEdicao()){
					getEventoEmprestimoCargoFuncionarioVO().setItemEmEdicao(false);
			       	aux = index;
			       	objAux = getEventoEmprestimoCargoFuncionarioVO();
	 			}
	            index++;
			}
			
			if(aux >= 0) {
				getListaDeEventosEmprestimosDoFuncionario().set(aux, objAux);
			}
			
		} else {		
			setEventoEmprestimoCargoFuncionarioVO(eventoEmprestimoCargoFuncionarioVO);
			getListaDeEventosEmprestimosDoFuncionario().add(getEventoEmprestimoCargoFuncionarioVO());
		}
		setEventoEmprestimoCargoFuncionarioVO(new EventoEmprestimoCargoFuncionarioVO());
		getEventoEmprestimoCargoFuncionarioVO().setFuncionarioCargoVO(getFuncionarioCargoVO());
	}

	public void cancelarEvento() {
		setEventoEmprestimoCargoFuncionarioVO(new EventoEmprestimoCargoFuncionarioVO());
	}
	
	public boolean validarSeObjetoExisteNaListaDeEventosEmprestimosDoFuncionario(EventoFolhaPagamentoVO eventofolhaPagamento) {
		
		for(EventoEmprestimoCargoFuncionarioVO evento : getListaDeEventosEmprestimosDoFuncionario()) {
			if(evento.getEventoFolhaPagamentoVO().getCodigo().equals(eventofolhaPagamento.getCodigo()))
				return true;
		}
		return false;
	}

	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			} 
			
			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(campoConsultaEvento, valorConsultaEvento, "ATIVO", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerEvento() {
		setEventoEmprestimoCargoFuncionarioVO((EventoEmprestimoCargoFuncionarioVO) context().getExternalContext().getRequestMap().get(CONTEXT_CONSULTA_EVENTO_EMPRESTIMO_FUNCIONARIO));
		
		getListaDeEventosEmprestimosDoFuncionario().removeIf(p -> p.getEventoFolhaPagamentoVO().getCodigo().equals(getEventoEmprestimoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getCodigo()));
		setEventoEmprestimoCargoFuncionarioVO(new EventoEmprestimoCargoFuncionarioVO());

		setMensagemID("msg_dados_excluidos");
	}
	
	public String editarEventoEmprestimo()  {
        try {
        	EventoEmprestimoCargoFuncionarioVO obj =(EventoEmprestimoCargoFuncionarioVO) context().getExternalContext().getRequestMap().get(CONTEXT_CONSULTA_EVENTO_EMPRESTIMO_FUNCIONARIO);
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setEventoEmprestimoCargoFuncionarioVO((EventoEmprestimoCargoFuncionarioVO) SerializationUtils.clone(obj));       	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }

	/**
	 * Retorna true caso exista algum emprestimo que tenha sido pago pelo menos uma parcela
	 * 
	 * @return
	 */
	private boolean validarSeExisteEmprestimoQueForamPagos() {
		
		for(EventoEmprestimoCargoFuncionarioVO evento : getListaDeEventosEmprestimosDoFuncionario()) {
			if(evento.getParcelaPaga() > 0)
				return true;
		}
		
		return false;
		
	}

	public void consultarFuncionarioPelaMatriculaCargo() {

		FuncionarioCargoVO funcionarioCargo = new FuncionarioCargoVO(); 
		try {
			funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getFuncionarioCargoVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(funcionarioCargo.getCodigo() > 0) {
			getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
			editar(funcionarioCargo);
		} else {
			setMensagemID("msg_erro_dadosnaoencontrados");
			setFuncionarioCargoVO(funcionarioCargo);
			setListaDeEventosEmprestimosDoFuncionario(new ArrayList<>());
		}
	}
	
	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(this.getEventoEmprestimoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getIdentificador())) {
				this.getEventoEmprestimoCargoFuncionarioVO().setEventoFolhaPagamentoVO(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(this.getEventoEmprestimoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getIdentificador(), false, getUsuarioLogado()));
			} else {
				this.getEventoEmprestimoCargoFuncionarioVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			this.getEventoEmprestimoCargoFuncionarioVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		getEventoEmprestimoCargoFuncionarioVO().setEventoFolhaPagamentoVO(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboTipoEmprestimo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("codigo", UteisJSF.internacionalizar("prt_TextoPadrao_codigo")));
		return itens;
	}
	
	/**
	 * Calcula o valor total do emprestimo
	 * ValorTotal = ValorParcela * NumeroParcela
	 */
	public void calcularValorTotal() {
		getEventoEmprestimoCargoFuncionarioVO().setValorTotal(
				getEventoEmprestimoCargoFuncionarioVO().getValorParcela().multiply(new BigDecimal(getEventoEmprestimoCargoFuncionarioVO().getNumeroParcela())));
	}
	
	/**
	 * Consulta os tipos de emprestimos.
	 */
	public void consultarTipoEmprestimo() {
		try {
			if(getCampoConsultaTipoEmprestimo().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getValorConsultaTipoEmprestimo());
			} 
			
			setListaTipoEmprestimo(getFacadeFactory().getTipoEmprestimoInterfaceFacade().consultarPorFiltro(getCampoConsultaTipoEmprestimo(), getValorConsultaTipoEmprestimo(), Uteis.NIVELMONTARDADOS_TODOS));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTipoEmprestimoPorCodigo() {
		try {
			if (Uteis.isAtributoPreenchido(this.getEventoEmprestimoCargoFuncionarioVO().getTipoEmprestimoVO().getCodigo())) {
				this.getEventoEmprestimoCargoFuncionarioVO().setTipoEmprestimoVO(getFacadeFactory().getTipoEmprestimoInterfaceFacade().consultarPorChavePrimaria(this.getEventoEmprestimoCargoFuncionarioVO().getTipoEmprestimoVO().getCodigo().longValue()));
			} else {
				this.getEventoEmprestimoCargoFuncionarioVO().setTipoEmprestimoVO(new TipoEmprestimoVO());
			}
		} catch (Exception e) {
			this.getEventoEmprestimoCargoFuncionarioVO().setTipoEmprestimoVO(new TipoEmprestimoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}		
	}
	
	public void selecionarTipoEmprestimo() {
		TipoEmprestimoVO obj = (TipoEmprestimoVO) context().getExternalContext().getRequestMap().get("tipoEmprestimoItem");
		getEventoEmprestimoCargoFuncionarioVO().setTipoEmprestimoVO(obj);

		setValorConsultaTipoEmprestimo("");
		setCampoConsultaEvento("");
		getListaTipoEmprestimo().clear();
		setListaTipoEmprestimo(new ArrayList<>());
	}
	
	public void limparDadosEvento() {
		this.getEventoEmprestimoCargoFuncionarioVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
	}
	
	public void limparDataPagamento() {
		if (!getEventoEmprestimoCargoFuncionarioVO().getQuitado()) {
			getEventoEmprestimoCargoFuncionarioVO().setDataPagamento(null);
		}
	}
	
	public void limparDadosTipoEmprestimo() {
		this.getEventoEmprestimoCargoFuncionarioVO().setTipoEmprestimoVO(new TipoEmprestimoVO());
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamentoVO() {
		if (eventoFolhaPagamentoVO == null)
			eventoFolhaPagamentoVO = new EventoFolhaPagamentoVO();
		return eventoFolhaPagamentoVO;
	}
	

	public void setEventoFolhaPagamentoVO(EventoFolhaPagamentoVO eventoFolhaPagamentoVO) {
		this.eventoFolhaPagamentoVO = eventoFolhaPagamentoVO;
	}
	
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}
	
	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}
	
	public boolean getApresentarResultadoConsultaEventoFolha() {
		return getListaDeEventos().size() > 0;
	}

	public EventoEmprestimoCargoFuncionarioVO getEventoEmprestimoCargoFuncionarioVO() {
		if (eventoEmprestimoCargoFuncionarioVO == null)
			eventoEmprestimoCargoFuncionarioVO = new EventoEmprestimoCargoFuncionarioVO();
		return eventoEmprestimoCargoFuncionarioVO;
	}

	public void setEventoEmprestimoCargoFuncionarioVO(EventoEmprestimoCargoFuncionarioVO eventoEmprestimoCargoFuncionarioVO) {
		this.eventoEmprestimoCargoFuncionarioVO = eventoEmprestimoCargoFuncionarioVO;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null)
			funcionarioCargoVO = new FuncionarioCargoVO();
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public List<EventoFolhaPagamentoVO> getListaDeEventos() {
		if (listaDeEventos == null)
			listaDeEventos = new ArrayList<>();
		return listaDeEventos;
	}

	public List<EventoEmprestimoCargoFuncionarioVO> getListaDeEventosEmprestimosDoFuncionario() {
		if (listaDeEventosEmprestimosDoFuncionario == null)
			listaDeEventosEmprestimosDoFuncionario = new ArrayList<>();
		return listaDeEventosEmprestimosDoFuncionario;
	}

	public void setListaDeEventos(List<EventoFolhaPagamentoVO> listaDeEventos) {
		this.listaDeEventos = listaDeEventos;
	}

	public void setListaDeEventosEmprestimosDoFuncionario(List<EventoEmprestimoCargoFuncionarioVO> listaDeEventosEmprestimosDoFuncionario) {
		this.listaDeEventosEmprestimosDoFuncionario = listaDeEventosEmprestimosDoFuncionario;
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

	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		if (listaEventosFolhaPagamento == null) {
			listaEventosFolhaPagamento = new ArrayList<>();
		}
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
	}

	public String getCampoConsultaTipoEmprestimo() {
		if(campoConsultaTipoEmprestimo == null)
			campoConsultaTipoEmprestimo = "";
		return campoConsultaTipoEmprestimo;
	}

	public void setCampoConsultaTipoEmprestimo(String campoConsultaTipoEmprestimo) {
		this.campoConsultaTipoEmprestimo = campoConsultaTipoEmprestimo;
	}

	public String getValorConsultaTipoEmprestimo() {
		if(valorConsultaTipoEmprestimo == null)
			valorConsultaTipoEmprestimo = "";
		return valorConsultaTipoEmprestimo;
	}

	public void setValorConsultaTipoEmprestimo(String valorConsultaTipoEmprestimo) {
		this.valorConsultaTipoEmprestimo = valorConsultaTipoEmprestimo;
	}

	public List<TipoEmprestimoVO> getListaTipoEmprestimo() {
		if(listaTipoEmprestimo == null)
			listaTipoEmprestimo = new ArrayList<TipoEmprestimoVO>();
		return listaTipoEmprestimo;
	}

	public void setListaTipoEmprestimo(List<TipoEmprestimoVO> listaTipoEmprestimo) {
		this.listaTipoEmprestimo = listaTipoEmprestimo;
	}	
}