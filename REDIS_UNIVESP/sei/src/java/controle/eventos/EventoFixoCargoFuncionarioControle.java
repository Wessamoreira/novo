package controle.eventos;

import java.io.Serializable;
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
import negocio.comuns.recursoshumanos.EventoFixoCargoFuncionarioVO;
import negocio.comuns.recursoshumanos.EventoFixoCargoFuncionarioVO.EnumCampoConsultaEventoFixoCargoFuncionario;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Gilberto Nery
 *
 */
@Controller("EventoFixoCargoFuncionarioControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings({"rawtypes"})
public class EventoFixoCargoFuncionarioControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -4699314420531096295L;

	private final String TELA_FORM = "eventoFixoCargoFuncionarioForm";
	private final String TELA_CONS = "eventoFixoCargoFuncionarioCons";
	private final String CONTEXT_PARA_EDICAO = "eventoFixoItens";
	private final String CONTEXT_CONSULTA_EVENTO_FIXO_FUNCIONARIO = "eventoFuncionario";

	private EventoFixoCargoFuncionarioVO eventoFixoCargoFuncionarioVO;
	protected FuncionarioCargoVO funcionarioCargoVO;
	private List<EventoFolhaPagamentoVO> listaDeEventos;
	private List<EventoFixoCargoFuncionarioVO> listaDeEventosFixosDoFuncionario;
	private EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
	
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;

	private String valorConsultaSituacao;
	
	public EventoFixoCargoFuncionarioControle() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaEventoFixoCargoFuncionario.FUNCIONARIO.name());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}
	
	@PostConstruct
	public void carregarEventosFixosVindosDaTelaDoFuncionarioCargo () {
		
		FuncionarioCargoVO funcionarioCargoVO = (FuncionarioCargoVO) context().getExternalContext().getSessionMap().get("funcionarioCargo");
		
		try {
			if (Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
				getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				funcionarioCargoVO = (FuncionarioCargoVO) getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(funcionarioCargoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				editar(funcionarioCargoVO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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
			setListaDeEventosFixosDoFuncionario(getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().consultarPorCargoFuncionario(funcionarioCargoVO, getControleConsultaOtimizado()));
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
			getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().persistir(getListaDeEventosFixosDoFuncionario(), getFuncionarioCargoVO(), true, getUsuarioLogado());
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

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().consultarPorEnumCampoConsultaEventoFixoCargoFuncionario(getControleConsultaOtimizado(), valorConsultaSituacao);
			
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
			getFacadeFactory().getEventoFixoCargoFuncionarioInterfaceFacade().excluir(getFuncionarioCargoVO(), true, getUsuarioLogado());
			setListaDeEventosFixosDoFuncionario(new ArrayList<>());
			setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
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
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaEventoFixoCargoFuncionario.FUNCIONARIO.name());
		setListaConsulta(new ArrayList(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Rotina responsavel por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaEventoFolhaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}

	public void adicionarEvento () {
		if (!Uteis.isAtributoPreenchido(getEventoFixoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getCodigo())) {
			setMensagemDetalhada("Campo EVENTO deve ser informado");
			return;
		}
		
		if(validarSeObjetoExisteNaListaDeEventosFixosDoFuncionario(getEventoFixoCargoFuncionarioVO().getEventoFolhaPagamentoVO()) ||
				!Uteis.isAtributoPreenchido(getEventoFixoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getCodigo())) {
			if (!getEventoFixoCargoFuncionarioVO().getItemEmEdicao()) {
				return;
			}
	    }

		if (getEventoFixoCargoFuncionarioVO().getItemEmEdicao()) {
			Iterator<EventoFixoCargoFuncionarioVO> i = getListaDeEventosFixosDoFuncionario().iterator();
			int index = 0;
			int aux = -1;
			EventoFixoCargoFuncionarioVO objAux = new EventoFixoCargoFuncionarioVO();
			while(i.hasNext()) {
				EventoFixoCargoFuncionarioVO objExistente = (EventoFixoCargoFuncionarioVO) i.next();
				
				if (objExistente.getEventoFolhaPagamentoVO().getCodigo().equals(getEventoFixoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getCodigo()) && objExistente.getItemEmEdicao()){
					getEventoFixoCargoFuncionarioVO().setItemEmEdicao(false);
			       	aux = index;
			       	objAux = getEventoFixoCargoFuncionarioVO();
	 			}
	            index++;
			}
			
			if(aux >= 0) {
				getListaDeEventosFixosDoFuncionario().set(aux, objAux);
			}
			
		} else {
			getListaDeEventosFixosDoFuncionario().add(getEventoFixoCargoFuncionarioVO());
		}
		eventoFixoCargoFuncionarioVO = new EventoFixoCargoFuncionarioVO();
		eventoFixoCargoFuncionarioVO.setFuncionarioCargoVO(getFuncionarioCargoVO());
		
		setMensagemID(MSG_TELA.msg_dados_editar.name());
		setMensagemDetalhada("");
	}

	public void cancelarEvento() {
		setEventoFixoCargoFuncionarioVO(new EventoFixoCargoFuncionarioVO());
	}
	
	 public String editarEventoFixoFuncionario()  {
        try {
        	EventoFixoCargoFuncionarioVO obj =(EventoFixoCargoFuncionarioVO) context().getExternalContext().getRequestMap().get("eventoFuncionario");
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setEventoFixoCargoFuncionarioVO((EventoFixoCargoFuncionarioVO) SerializationUtils.clone(obj));       	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }
	
	public boolean validarSeObjetoExisteNaListaDeEventosFixosDoFuncionario(EventoFolhaPagamentoVO eventofolhaPagamento) {
		
		for(EventoFixoCargoFuncionarioVO evento : getListaDeEventosFixosDoFuncionario()) {
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
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosEvento() {
		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getEventoFixoCargoFuncionarioVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
		getListaEventosFolhaPagamento().clear();
	}

	public void removerEvento() {
		
		setEventoFixoCargoFuncionarioVO((EventoFixoCargoFuncionarioVO) context().getExternalContext().getRequestMap().get(CONTEXT_CONSULTA_EVENTO_FIXO_FUNCIONARIO));
        try {
            int index = 0;
            Iterator i = getListaDeEventosFixosDoFuncionario().iterator();
            while (i.hasNext()) {
            	EventoFixoCargoFuncionarioVO objExistente = (EventoFixoCargoFuncionarioVO) i.next();
                if (objExistente.getEventoFolhaPagamentoVO().getCodigo().equals(getEventoFixoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getCodigo())) {
                	getListaDeEventosFixosDoFuncionario().remove(index);
                	setEventoFixoCargoFuncionarioVO(new EventoFixoCargoFuncionarioVO());
                    return;
                }
                index++;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        setMensagemID("msg_dados_excluidos");
	}

	public void consultarFuncionarioPelaMatriculaCargo() {

		FuncionarioCargoVO funcionarioCargoVO = new FuncionarioCargoVO(); 
		try {
			funcionarioCargoVO = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getFuncionarioCargoVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(funcionarioCargoVO.getCodigo() > 0) {
			getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
			editar(funcionarioCargoVO);
		} else {
			setMensagemID("msg_erro_dadosnaoencontrados");
			setFuncionarioCargoVO(funcionarioCargoVO);
			setListaDeEventosFixosDoFuncionario(new ArrayList<>());
		}
	}
	
	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(this.getEventoFixoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getIdentificador())) {
				this.getEventoFixoCargoFuncionarioVO().setEventoFolhaPagamentoVO(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(this.getEventoFixoCargoFuncionarioVO().getEventoFolhaPagamentoVO().getIdentificador(), false, getUsuarioLogado()));
			} else {
				this.getEventoFixoCargoFuncionarioVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			this.getEventoFixoCargoFuncionarioVO().setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		getEventoFixoCargoFuncionarioVO().setEventoFolhaPagamentoVO(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
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

	public EventoFixoCargoFuncionarioVO getEventoFixoCargoFuncionarioVO() {
		if (eventoFixoCargoFuncionarioVO == null)
			eventoFixoCargoFuncionarioVO = new EventoFixoCargoFuncionarioVO();
		return eventoFixoCargoFuncionarioVO;
	}

	public void setEventoFixoCargoFuncionarioVO(EventoFixoCargoFuncionarioVO eventoFixoCargoFuncionarioVO) {
		this.eventoFixoCargoFuncionarioVO = eventoFixoCargoFuncionarioVO;
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

	public List<EventoFixoCargoFuncionarioVO> getListaDeEventosFixosDoFuncionario() {
		if (listaDeEventosFixosDoFuncionario == null)
			listaDeEventosFixosDoFuncionario = new ArrayList<>();
		return listaDeEventosFixosDoFuncionario;
	}

	public void setListaDeEventos(List<EventoFolhaPagamentoVO> listaDeEventos) {
		this.listaDeEventos = listaDeEventos;
	}

	public void setListaDeEventosFixosDoFuncionario(List<EventoFixoCargoFuncionarioVO> listaDeEventosFixosDoFuncionario) {
		this.listaDeEventosFixosDoFuncionario = listaDeEventosFixosDoFuncionario;
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
}