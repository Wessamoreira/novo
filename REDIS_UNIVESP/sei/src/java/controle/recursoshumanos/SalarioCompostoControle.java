package controle.recursoshumanos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.recursoshumanos.HistoricoSalarialVO;
import negocio.comuns.recursoshumanos.SalarioCompostoVO;
import negocio.comuns.recursoshumanos.SalarioCompostoVO.EnumCampoConsultaSalarioComposto;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("SalarioCompostoControle")
@Scope("viewScope")
@Lazy
public class SalarioCompostoControle extends SuperControle {
	
	private static final long serialVersionUID = -4972439716448593438L;
	
	private static final String TELA_FORM = "salarioCompostoForm";
	private static final String TELA_CONS = "salarioCompostoCons";
	private static final String CONTEXT_PARA_EDICAO = "itemSalarioComposto";

	private SalarioCompostoVO salarioComposto;
	private FuncionarioCargoVO funcionarioCargoVO;
	
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;
	
	private List<SalarioCompostoVO> listaSalarioComposto;

	private String valorConsultaSituacao;

	private BigDecimal somaValorMensal;
	private Integer somaJornadas;
	private HistoricoSalarialVO historicoSalarialVO;
	
	private Boolean abrirPopup;
	
	public SalarioCompostoControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
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
		setAbrirPopup(Boolean.FALSE);
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
			setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(funcionarioCargoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuario()));
			getSalarioComposto().setFuncionarioCargo(funcionarioCargoVO);
			setListaSalarioComposto(getFacadeFactory().getSalarioCompostoInterfaceFacade().consultarPorFuncionarioCargo(funcionarioCargoVO, false, getUsuarioLogado()));
			setControleConsultaOtimizado(new DataModelo());

			atualizarValorHoraPeloSalario();
			getSalarioComposto().setJornada(getFuncionarioCargoVO().getJornada());
			realizarCalculoValorMensal();

			//Atualiza o somatorio do footer da grid
	        atualizarSomatorio();

	        setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	private void atualizarValorHoraPeloSalario() {
		BigDecimal salarioProgressaoSalarialItem = getFacadeFactory().getProgressaoSalarialItemInterfaceFacade().consultarSalarioPorNivelFaixaProgressao(
				getFuncionarioCargoVO().getNivelSalarial().getCodigo(),
				getFuncionarioCargoVO().getFaixaSalarial().getCodigo(),
				getFuncionarioCargoVO().getCargo().getProgressaoSalarial().getCodigo());
		getSalarioComposto().setValorHora(salarioProgressaoSalarialItem);
	}

	public void persistir() {
		try {
			getFacadeFactory().getSalarioCompostoInterfaceFacade().persistir(getListaSalarioComposto(), getHistoricoSalarialVO(), getFuncionarioCargoVO(), true, getUsuarioLogado());
			getSalarioComposto().setFuncionarioCargo(getFuncionarioCargoVO());
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
	
	public void realizarCalculoValorMensal() {
		if (Uteis.isAtributoPreenchido(getSalarioComposto().getValorHora())) {

			BigDecimal salario = getSalarioComposto().getValorHora().multiply(
					new BigDecimal(getSalarioComposto().getJornada()).divide(new BigDecimal(5) , 6, RoundingMode.HALF_EVEN));
			getSalarioComposto().setValorMensal(salario);
		}
	}

	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getSalarioCompostoInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), getValorConsultaSituacao());
			
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
			
			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltroEProvento(
					campoConsultaEvento, valorConsultaEvento, "ATIVO", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioPelaMatriculaCargo() {

		FuncionarioCargoVO funcionarioCargo = new FuncionarioCargoVO(); 
		try {
			funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(
					getSalarioComposto().getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(funcionarioCargo.getCodigo() > 0) {
			getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
			editar(funcionarioCargo);
			
		} else {
			setMensagemID("msg_erro_dadosnaoencontrados");
			getSalarioComposto().setFuncionarioCargo(new FuncionarioCargoVO());
			setListaSalarioComposto(new ArrayList<>());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getSalarioCompostoInterfaceFacade().excluirPorFuncionarioCargo(getFuncionarioCargoVO(), false, getUsuario());
			setListaSalarioComposto(new ArrayList<>());
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
			if (Uteis.isAtributoPreenchido(getSalarioComposto().getEventoFolhaPagamento().getIdentificador())) {
				getSalarioComposto().setEventoFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(getSalarioComposto().getEventoFolhaPagamento().getIdentificador(), false, getUsuarioLogado()));
				realizarCalculoJornada();
			} else {
				getSalarioComposto().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			getSalarioComposto().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		getSalarioComposto().setEventoFolhaPagamento(obj);

		realizarCalculoJornada();

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	}

	private void realizarCalculoJornada() {
		Integer jornada = getListaSalarioComposto().stream().map(p -> p.getJornada()).mapToInt(Integer::new).sum();
		if (getFuncionarioCargoVO().getJornada() > jornada) {
			getSalarioComposto().setJornada(getFuncionarioCargoVO().getJornada() - jornada);
			realizarCalculoValorMensal();
		}
	}

	public void limparDadosEvento() {
		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getSalarioComposto().setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
		getListaEventosFolhaPagamento().clear();
	}
	
	public void adicionarSalarioComposto() {
		if (!Uteis.isAtributoPreenchido(getSalarioComposto().getEventoFolhaPagamento().getCodigo())) {
			setMensagemDetalhada("Campo EVENTO deve ser informado");
			return;
		}

		if( (validarSeObjetoExisteNaListaDeSalarioComposto(getSalarioComposto().getEventoFolhaPagamento()) ||
				!Uteis.isAtributoPreenchido(getSalarioComposto().getEventoFolhaPagamento().getCodigo())) && !getSalarioComposto().getItemEmEdicao()) {
			return;
	    }
		getSalarioComposto().getHistoricoSalarialVO().setGerarHistorico(true);

		if (getSalarioComposto().getItemEmEdicao()) {
			Iterator<SalarioCompostoVO> i = getListaSalarioComposto().iterator();
			int index = 0;
			int aux = -1;
			SalarioCompostoVO objAux = new SalarioCompostoVO();
			while(i.hasNext()) {
				SalarioCompostoVO objExistente = i.next();
				
				if (objExistente.getEventoFolhaPagamento().getCodigo().equals(getSalarioComposto().getEventoFolhaPagamento().getCodigo()) && objExistente.getItemEmEdicao()){
					getSalarioComposto().setItemEmEdicao(false);
			       	aux = index;
			       	objAux = getSalarioComposto();
	 			}
	            index++;
			}

			if(aux >= 0) {
				getListaSalarioComposto().set(aux, objAux);
			}

		} else {
			getListaSalarioComposto().add(getSalarioComposto());
		}
		salarioComposto = new SalarioCompostoVO();
		getSalarioComposto().setFuncionarioCargo(getFuncionarioCargoVO());
		atualizarValorHoraPeloSalario();

		//Atualiza o somatorio do footer da grid
        atualizarSomatorio();
        realizarCalculoJornada();
        setAbrirPopup(Boolean.TRUE);
	}

	public String editarSalarioComposto()  {
        try {
        	SalarioCompostoVO obj =(SalarioCompostoVO) context().getExternalContext().getRequestMap().get("eventoSalarioComposto");
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setSalarioComposto((SalarioCompostoVO) SerializationUtils.clone(obj));       	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }

	public void removerEvento() {
		
		SalarioCompostoVO salario = (SalarioCompostoVO) context().getExternalContext().getRequestMap().get("eventoSalarioComposto");
        try {
            int index = 0;
            Iterator<SalarioCompostoVO> i = getListaSalarioComposto().iterator();
            while (i.hasNext()) {
            	SalarioCompostoVO objExistente = i.next();
                if (objExistente.getEventoFolhaPagamento().getCodigo().equals(salario.getEventoFolhaPagamento().getCodigo())) {
                	getListaSalarioComposto().remove(index);
                	setSalarioComposto(new SalarioCompostoVO());
                }
                index++;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        getSalarioComposto().setFuncionarioCargo(getFuncionarioCargoVO());
        
        //Atualiza o somatorio do footer da grid
        atualizarSomatorio();
        atualizarValorHoraPeloSalario();
        realizarCalculoJornada();
        
        setMensagemID("msg_dados_excluidos");
	}
	
	
	/**
	 * Atualizar somatorio do valor mensal e das jornadas 
	 * Aprenseta na footer da grid
	 */
	private void atualizarSomatorio() {
		setSomaJornadas(getFacadeFactory().getSalarioCompostoInterfaceFacade().realizarSomaDasJornadasDoSalarioComposto(getListaSalarioComposto()));
        setSomaValorMensal(getFacadeFactory().getSalarioCompostoInterfaceFacade().realizarSomaDoValorMensalDoSalarioComposto(getListaSalarioComposto()));		
	}

	public void cancelarEvento() {
		setSalarioComposto(new SalarioCompostoVO());
		getSalarioComposto().setFuncionarioCargo(getFuncionarioCargoVO());
	}
	
	public boolean validarSeObjetoExisteNaListaDeSalarioComposto(EventoFolhaPagamentoVO eventofolhaPagamento) {
		
		for(SalarioCompostoVO salarioCompostoVO : getListaSalarioComposto()) {
			if(salarioCompostoVO.getEventoFolhaPagamento().getCodigo().equals(eventofolhaPagamento.getCodigo()))
				return true;
		}
		return false;
	}

	public void validarHistoricoSalarial() {
		try {
			if (!Uteis.isAtributoPreenchido(getSalarioComposto().getHistoricoSalarialVO().getMotivoMudanca())) {
				setMensagemDetalhada("Campo MOTIVO deve ser informado");
				return;
			}
			montarDadosHistoricoSalarial();
			setAbrirPopup(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void montarDadosHistoricoSalarial() {
		HistoricoSalarialVO obj = new HistoricoSalarialVO(); 
		obj.setMotivoMudanca(getSalarioComposto().getHistoricoSalarialVO().getMotivoMudanca());
		obj.setDataMudanca(new Date());
		obj.setJornada(getSomaJornadas());
		obj.setSalario(getSomaValorMensal());
		obj.setFuncionarioCargo(getFuncionarioCargoVO());

		BigDecimal salarioHora = obj.getSalario().divide(new BigDecimal(obj.getJornada()).setScale(2, BigDecimal.ROUND_HALF_UP));
		obj.setSalarioHora(salarioHora);

		BigDecimal percentual = getSomaValorMensal()
				.subtract(getSalarioComposto().getFuncionarioCargo().getSalario())
				.divide(funcionarioCargoVO.getSalario(), BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal(100));
		obj.setPercentualVariacaoSalarial(percentual);

		setHistoricoSalarialVO(obj);
	}

	public String abrirPopUpSituacoesHistorico() {
		if (getAbrirPopup()) {
			return "RichFaces.$('panelSituacoesHistoricos').show();";
		} 
		return "RichFaces.$('panelSituacoesHistoricos').hide();";
	}

	//GETTER AND SETTER
	public SalarioCompostoVO getSalarioComposto() {
		if (salarioComposto == null) {
			salarioComposto = new SalarioCompostoVO();
		}
		return salarioComposto;
	}

	public void setSalarioComposto(SalarioCompostoVO salarioComposto) {
		this.salarioComposto = salarioComposto;
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

	public List<SalarioCompostoVO> getListaSalarioComposto() {
		if (listaSalarioComposto == null) {
			listaSalarioComposto = new ArrayList<>();
		}
		return listaSalarioComposto;
	}

	public void setListaSalarioComposto(List<SalarioCompostoVO> listaSalarioComposto) {
		this.listaSalarioComposto = listaSalarioComposto;
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

	public BigDecimal getSomaValorMensal() {
		if (somaValorMensal == null)
			somaValorMensal = BigDecimal.ZERO;
		return somaValorMensal;
	}

	public void setSomaValorMensal(BigDecimal somaValorMensal) {
		this.somaValorMensal = somaValorMensal;
	}

	public Integer getSomaJornadas() {
		if (somaJornadas == null)
			somaJornadas = 0;
		return somaJornadas;
	}

	public void setSomaJornadas(Integer somaJornadas) {
		this.somaJornadas = somaJornadas;
	}

	public Boolean getAbrirPopup() {
		if (abrirPopup == null) {
			abrirPopup = Boolean.FALSE;
		}
		return abrirPopup;
	}

	public void setAbrirPopup(Boolean abrirPopup) {
		this.abrirPopup = abrirPopup;
	}

	public HistoricoSalarialVO getHistoricoSalarialVO() {
		
		return historicoSalarialVO;
	}

	public void setHistoricoSalarialVO(HistoricoSalarialVO historicoSalarialVO) {
		this.historicoSalarialVO = historicoSalarialVO;
	}
}