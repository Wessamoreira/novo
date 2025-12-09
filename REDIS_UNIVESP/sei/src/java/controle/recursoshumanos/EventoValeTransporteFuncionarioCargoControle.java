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

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoValeTransporteFuncionarioCargoVO;
import negocio.comuns.recursoshumanos.EventoValeTransporteFuncionarioCargoVO.EnumCampoConsultaSalarioComposto;
import negocio.comuns.recursoshumanos.LinhaTransporteVO;
import negocio.comuns.recursoshumanos.ParametroValeTransporteVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("EventoValeTransporteFuncionarioCargoControle")
@Scope("viewScope")
@Lazy
public class EventoValeTransporteFuncionarioCargoControle extends SuperControle {
	
	private static final long serialVersionUID = -4972439716448593438L;
	
	private static final String TELA_FORM = "eventoValeTransporteForm";
	private static final String TELA_CONS = "eventoValeTransporteCons";
	private static final String CONTEXT_PARA_EDICAO = "itemEventoValeTransporte";

	private EventoValeTransporteFuncionarioCargoVO eventoValeTransporteFuncionarioCargo;
	private FuncionarioCargoVO funcionarioCargoVO;
	
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;

	private String campoConsultaLinhaTransporte;
	private String valorConsultaLinhaTransporte;
	private List<LinhaTransporteVO> listaLinhaTransporte;
	
	private List<EventoValeTransporteFuncionarioCargoVO> listaEventoValeTransporteFuncionarioCargo;

	private String valorConsultaSituacao;

	public EventoValeTransporteFuncionarioCargoControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}
	
	@PostConstruct 
	public void carregarEventoValeTransporteVindosDaTelaDoFuncionarioCargo () {
		
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

	public String editar(FuncionarioCargoVO funcionarioCargoVO) {
		return preparaDadosParaEdicao(funcionarioCargoVO);
	}

	public String editar() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		return preparaDadosParaEdicao(obj);
	}

	public String preparaDadosParaEdicao(FuncionarioCargoVO funcionarioCargoVO) {
		try {
			setFuncionarioCargoVO(funcionarioCargoVO);
			getEventoValeTransporteFuncionarioCargo().setFuncionarioCargo(funcionarioCargoVO);
			setListaEventoValeTransporteFuncionarioCargo(getFacadeFactory().getEventoValeTransporteFuncionarioCargoInterfaceFacade().consultarPorFuncionarioCargo(funcionarioCargoVO, false, getUsuarioLogado()));
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
			if (Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getCodigo())) {
				getFacadeFactory().getEventoValeTransporteFuncionarioCargoInterfaceFacade().persistirTodos(getListaEventoValeTransporteFuncionarioCargo(), getFuncionarioCargoVO(), true, getUsuarioLogado());
				getEventoValeTransporteFuncionarioCargo().setFuncionarioCargo(getFuncionarioCargoVO());
				setMensagemID(MSG_TELA.msg_dados_gravados.name());
			} else {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Nenhum Funcionário selecionado");
			}
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

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getEventoValeTransporteFuncionarioCargoInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), getValorConsultaSituacao());
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			} 
			
			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltroEProvento(campoConsultaEvento, valorConsultaEvento, "ATIVO", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void consultarLinhaTransporte() {
		try {
			if(getCampoConsultaLinhaTransporte().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getValorConsultaLinhaTransporte());
			} 
			DataModelo modelo = new DataModelo();
			modelo.setCampoConsulta(getCampoConsultaLinhaTransporte());
			modelo.setValorConsulta(getValorConsultaLinhaTransporte());
			getFacadeFactory().getParametroValeTransporteInterfaceFacade().consultarPorEnumCampoConsulta(modelo, "ATIVO");
			setListaLinhaTransporte((List<LinhaTransporteVO>) modelo.getListaConsulta());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarFuncionarioPelaMatriculaCargo() {

		FuncionarioCargoVO funcionarioCargo = new FuncionarioCargoVO(); 
		try {
			funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getEventoValeTransporteFuncionarioCargo().getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(funcionarioCargo.getCodigo() > 0) {
			getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
			editar(funcionarioCargo);
		} else {
			setMensagemID("msg_erro_dadosnaoencontrados");
			setFuncionarioCargoVO(new FuncionarioCargoVO());
			setEventoValeTransporteFuncionarioCargo(new EventoValeTransporteFuncionarioCargoVO());
			setListaEventoValeTransporteFuncionarioCargo(new ArrayList<>());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getEventoValeTransporteFuncionarioCargoInterfaceFacade().excluirPorFuncionarioCargo(getFuncionarioCargoVO(), false, getUsuario());
			setListaEventoValeTransporteFuncionarioCargo(new ArrayList<>());
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
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaSalarioComposto.FUNCIONARIO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}
	
	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}

	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
	}
	
	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(getEventoValeTransporteFuncionarioCargo().getEventoFolhaPagamento().getIdentificador())) {
				getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(getEventoValeTransporteFuncionarioCargo().getEventoFolhaPagamento().getIdentificador(), false, getUsuarioLogado()));
			}
		} catch (Exception e) {
			getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	}

	public void consultarLinhaTranportePorCodigo() {
		try {
			if (Uteis.isAtributoPreenchido(getEventoValeTransporteFuncionarioCargo().getParametroValeTransporte().getCodigo())) {
				getEventoValeTransporteFuncionarioCargo().setParametroValeTransporte(getFacadeFactory().getParametroValeTransporteInterfaceFacade().consultarPorChavePrimaria(
						getEventoValeTransporteFuncionarioCargo().getParametroValeTransporte().getCodigo().longValue()));
				
				getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(getEventoValeTransporteFuncionarioCargo().getParametroValeTransporte().getEventoFolhaPagamento());
			} else {
				getEventoValeTransporteFuncionarioCargo().setParametroValeTransporte(new ParametroValeTransporteVO());
				getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			getEventoValeTransporteFuncionarioCargo().setParametroValeTransporte(new ParametroValeTransporteVO());
			getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarLinhaTransporte() {
		ParametroValeTransporteVO obj = (ParametroValeTransporteVO) context().getExternalContext().getRequestMap().get("eventoLinhaTransporte");
		getEventoValeTransporteFuncionarioCargo().setParametroValeTransporte(obj);
		getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(obj.getEventoFolhaPagamento());

		valorConsultaLinhaTransporte = "";
		campoConsultaLinhaTransporte = "";
		getListaLinhaTransporte().clear();
	}

	public void limparDadosEvento() {
		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
		getListaEventosFolhaPagamento().clear();
	}

	public void limparLinhaTransporte() {
		valorConsultaLinhaTransporte = "";
		campoConsultaLinhaTransporte = "";
		getEventoValeTransporteFuncionarioCargo().setParametroValeTransporte(new ParametroValeTransporteVO());
		getEventoValeTransporteFuncionarioCargo().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
		getListaLinhaTransporte().clear();
	}

	public void adicionarEvento () {
		if (!Uteis.isAtributoPreenchido(getEventoValeTransporteFuncionarioCargo().getParametroValeTransporte().getCodigo())) {
			setMensagemDetalhada("Campo PARÂMETRO DE VALE TRANSPORTE deve ser informado");
			return;
		}
		
		if (getEventoValeTransporteFuncionarioCargo().getItemEmEdicao()) {
			Iterator<EventoValeTransporteFuncionarioCargoVO> i = getListaEventoValeTransporteFuncionarioCargo().iterator();
			int index = 0;
			int aux = -1;
			EventoValeTransporteFuncionarioCargoVO objAux = new EventoValeTransporteFuncionarioCargoVO();
			while(i.hasNext()) {
				EventoValeTransporteFuncionarioCargoVO objExistente = i.next();
				
				if (objExistente.getEventoFolhaPagamento().getCodigo().equals(getEventoValeTransporteFuncionarioCargo().getEventoFolhaPagamento().getCodigo()) && objExistente.getItemEmEdicao()){
					getEventoValeTransporteFuncionarioCargo().setItemEmEdicao(false);
			       	aux = index;
			       	objAux = getEventoValeTransporteFuncionarioCargo();
	 			}
	            index++;
			}
			
			if(aux >= 0) {
				getListaEventoValeTransporteFuncionarioCargo().set(aux, objAux);
			}
			
		} else {		
			getListaEventoValeTransporteFuncionarioCargo().add(getEventoValeTransporteFuncionarioCargo());
		}
		eventoValeTransporteFuncionarioCargo = new EventoValeTransporteFuncionarioCargoVO();
		getEventoValeTransporteFuncionarioCargo().setFuncionarioCargo(getFuncionarioCargoVO());
	}
	
	public String editarEventoFixoFuncionario()  {
        try {
        	EventoValeTransporteFuncionarioCargoVO obj =(EventoValeTransporteFuncionarioCargoVO) context().getExternalContext().getRequestMap().get("eventoVT");
        	obj.setEventoFolhaPagamento(obj.getParametroValeTransporte().getEventoFolhaPagamento());
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setEventoValeTransporteFuncionarioCargo((EventoValeTransporteFuncionarioCargoVO) SerializationUtils.clone(obj));       	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }

	public void removerEvento() {
		EventoValeTransporteFuncionarioCargoVO eventoVTNaGrid = (EventoValeTransporteFuncionarioCargoVO) context().getExternalContext().getRequestMap().get("eventoVT");
		
		getListaEventoValeTransporteFuncionarioCargo().removeIf(p -> p.getParametroValeTransporte().getCodigo().equals(eventoVTNaGrid.getParametroValeTransporte().getCodigo()));
        
		getEventoValeTransporteFuncionarioCargo().setFuncionarioCargo(getFuncionarioCargoVO());
        setMensagemID("msg_dados_excluidos");
	}
	
	public void cancelarEvento() {
		setEventoValeTransporteFuncionarioCargo(new EventoValeTransporteFuncionarioCargoVO());
		getEventoValeTransporteFuncionarioCargo().setFuncionarioCargo(getFuncionarioCargoVO());
	}
	
	//GETTER AND SETTER
	public EventoValeTransporteFuncionarioCargoVO getEventoValeTransporteFuncionarioCargo() {
		if (eventoValeTransporteFuncionarioCargo == null) {
			eventoValeTransporteFuncionarioCargo = new EventoValeTransporteFuncionarioCargoVO();
		}
		return eventoValeTransporteFuncionarioCargo;
	}

	public void setEventoValeTransporteFuncionarioCargo(
			EventoValeTransporteFuncionarioCargoVO eventoValeTransporteFuncionarioCargo) {
		this.eventoValeTransporteFuncionarioCargo = eventoValeTransporteFuncionarioCargo;
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
			valorConsultaSituacao = "TODOS";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public List<EventoValeTransporteFuncionarioCargoVO> getListaEventoValeTransporteFuncionarioCargo() {
		if (listaEventoValeTransporteFuncionarioCargo == null) {
			listaEventoValeTransporteFuncionarioCargo = new ArrayList<>();
		}
		return listaEventoValeTransporteFuncionarioCargo;
	}

	public void setListaEventoValeTransporteFuncionarioCargo(List<EventoValeTransporteFuncionarioCargoVO> listaEventoValeTransporteFuncionarioCargo) {
		this.listaEventoValeTransporteFuncionarioCargo = listaEventoValeTransporteFuncionarioCargo;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public String getCampoConsultaLinhaTransporte() {
		if (campoConsultaLinhaTransporte == null) {
			campoConsultaLinhaTransporte = "";
		}
		return campoConsultaLinhaTransporte;
	}

	public void setCampoConsultaLinhaTransporte(String campoConsultaLinhaTransporte) {
		this.campoConsultaLinhaTransporte = campoConsultaLinhaTransporte;
	}

	public String getValorConsultaLinhaTransporte() {
		if (valorConsultaLinhaTransporte == null) {
			valorConsultaLinhaTransporte = "";
		}
		return valorConsultaLinhaTransporte;
	}

	public void setValorConsultaLinhaTransporte(String valorConsultaLinhaTransporte) {
		this.valorConsultaLinhaTransporte = valorConsultaLinhaTransporte;
	}

	public List<LinhaTransporteVO> getListaLinhaTransporte() {
		if (listaLinhaTransporte == null) {
			listaLinhaTransporte = new ArrayList<>();
		}
		return listaLinhaTransporte;
	}

	public void setListaLinhaTransporte(List<LinhaTransporteVO> listaLinhaTransporte) {
		this.listaLinhaTransporte = listaLinhaTransporte;
	}
	
	public void calcularDadosVT() {
		getEventoValeTransporteFuncionarioCargo().getQuantidadeDeViagens();
		getEventoValeTransporteFuncionarioCargo().getValorAReceber();
	}
}