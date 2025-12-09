package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoItemVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoMediaVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas tabelaReferenciaFPForm.xhtml é tabelaReferenciaFPCons.xhtl com as
 * funcionalidades da classe <code>EventoFolhaPagamentoVO</code>.
 * Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaGEDVO
 */
@SuppressWarnings("rawtypes")
@Controller("EventoFolhaPagamentoControle")
@Scope("viewScope")
@Lazy
public class EventoFolhaPagamentoControle extends SuperControle {

	private static final long serialVersionUID = 7027954144554326040L;
	private EventoFolhaPagamentoVO eventoFolhaPagamentoVO;
	private String valorConsultaIncidencia;
	private List listaConsultaIncidencia;
	private List listaConsultaFormula;
	private String campoConsultaFormula;
	private String valorConsultaFormula;
	private String formulaConsultada;
	
	private String valorConsultaSituacao;

	private EventoFolhaPagamentoVO eventoFolhaPagamento;
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;

	private DataModelo consultaCategoriaDespesa;

	private String valorTipoLancamento;
	
	private EventoFolhaPagamentoMediaVO eventoMediaVO;

	@PostConstruct
	public void init() {
		setControleConsulta(new ControleConsulta());
        
        EventoFolhaPagamentoVO eventoFolhaPagamentoVO = (EventoFolhaPagamentoVO) context().getExternalContext().getSessionMap().get("eventoFolhaPagamentoDoContraCheque");
		
		try {
			if (Uteis.isAtributoPreenchido(eventoFolhaPagamentoVO)) {
				getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_DADOSCONSULTA);
				eventoFolhaPagamentoVO = (EventoFolhaPagamentoVO) getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(eventoFolhaPagamentoVO.getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_COMBOBOX);
				editar(eventoFolhaPagamentoVO);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("eventoFolhaPagamentoDoContraCheque");
		}
    }

	public EventoFolhaPagamentoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsavel por disponibilizar um novo objeto da classe
	 * <code>EventoFolhaPagamentoVO</code> para edicao pelo usuario da aplicacao.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setEventoFolhaPagamentoVO(new EventoFolhaPagamentoVO());
		getEventoFolhaPagamentoVO().setTipoLancamento(TipoLancamentoFolhaPagamentoEnum.PROVENTO);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("eventoFolhaPagamentoForm");
	}

	/**
	 * Rotina responsavel por disponibilizar os dados de um objeto da classe
	 * <code>EventoFolhaPagamentoControle</code> para alteracao. O objeto desta classe e
	 * disponibilizado na session da pagina (request) para que o JSP correspondente
	 * possa disponibiliza-lo para edicao.
	 */
	public String editar() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoFP");
		return editarEvento(obj);
	}
	
	public String editar(EventoFolhaPagamentoVO obj) {
		return editarEvento(obj);
	}

	public String editarEvento(EventoFolhaPagamentoVO obj) {
		try {
			obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS);
			obj.setEventoFolhaPagamentoItemVOs(getFacadeFactory().getEventoFolhaPagamentoItemInterfaceFacade().consultarPorEventoFolha(obj, false, getUsuarioLogado()));
			obj.setEventoMediaVOs(getFacadeFactory().getEventoFolhaPagamentoMediaInterfaceFacade().consultarPorEventoFolha(obj, false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		obj.setNovoObj(Boolean.FALSE);
		setEventoFolhaPagamentoVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("eventoFolhaPagamentoForm");
	}

	/**
	 * Rotina responsavel por organizar a paginacao entre as paginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<EventoFolhaPagamentoVO>(0));
		setMensagemID("msg_entre_prmconsulta");
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("eventoFolhaPagamentoCons");
	}

	/**
	 * Rotina responsavel por gravar no BD os dados editados de um novo objeto da
	 * classe <code>EventoFolhaPagamento</code>. Caso o objeto seja novo (ainda nao gravado
	 * no BD) e acionado a operacao <code>incluir()</code>. Caso contrario e
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistencia o objeto
	 * nao e gravado, sendo re-apresentado para o usuario juntamente com uma
	 * mensagem de erro.
	 */
	public String gravar() {
		try {
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().persistir(eventoFolhaPagamentoVO, Boolean.TRUE, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	/**
	 * Operaçao responsavel por processar a exclusao um objeto da classe
	 * <code>EventoFolhaPagamento</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusao.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().excluir(eventoFolhaPagamentoVO, Boolean.TRUE, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("eventoFolhaPagamentoForm");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("eventoFolhaPagamentoForm");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis do
	 * eventoFolhaPagamentoCons.xhtml. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(getControleConsultaOtimizado(), getValorConsultaSituacao(), getValorTipoLancamento());

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Altera o status da situacao para Inativo.
	 * 
	 * @return
	 */
	public String inativar() {
		try {
			getEventoFolhaPagamentoVO().setSituacao(AtivoInativoEnum.INATIVO);
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().inativar(getEventoFolhaPagamentoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_inativado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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
			getEventoFolhaPagamentoVO().setSituacao(AtivoInativoEnum.ATIVO);
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().inativar(getEventoFolhaPagamentoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_inativado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	/**
	 * Rotina responsavel por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}

	/**
	 * Rotina responsavel por realizar a paginacao da pagina de eventoFolhaPagamentoCons.xhtml
	 * 
	 * @param DataScrollEvent
	 * @throws Exception
	 */
	public void scrollerListener(DataScrollEvent dataScrollEvent) {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
        consultarDados();
    }

	public void consultarIncidencia() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaIncidencia().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }

            objs = getFacadeFactory().getIncidenciaFolhaPagamentoInterfaceFacade().consultarPorFiltro("descricao", getValorConsultaIncidencia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

            setListaConsultaIncidencia(objs);
            setMensagemID(MSG_TELA.msg_dados_consultados.name());
        } catch (Exception e) {
        	setListaConsultaIncidencia(new ArrayList(0));
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

	public void consultarFormula() {
		try {
			if(getCampoConsultaFormula().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			} 

			List objs = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorFiltro(getCampoConsultaFormula(), getValorConsultaFormula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(),"ATIVO");
			setListaConsultaFormula(objs);
            setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
        	setListaConsultaIncidencia(new ArrayList(0));
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboFormula() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificador", "Identificador"));
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public void selecionarFormula() {
		FormulaFolhaPagamentoVO formula = (FormulaFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("formulaItens");
		
		switch (getFormulaConsultada()) {
		case "valor":
			getEventoFolhaPagamentoVO().getFormulaValor().setCodigo(formula.getCodigo());
			getEventoFolhaPagamentoVO().getFormulaValor().setIdentificador(formula.getIdentificador());
			getEventoFolhaPagamentoVO().getFormulaValor().setDescricao(formula.getDescricao());
			break;
		case "hora":
			getEventoFolhaPagamentoVO().getFormulaHora().setCodigo(formula.getCodigo());
			getEventoFolhaPagamentoVO().getFormulaHora().setIdentificador(formula.getIdentificador());
			getEventoFolhaPagamentoVO().getFormulaHora().setDescricao(formula.getDescricao());
			break;
		case "dia":
			getEventoFolhaPagamentoVO().getFormulaDia().setCodigo(formula.getCodigo());
			getEventoFolhaPagamentoVO().getFormulaDia().setIdentificador(formula.getIdentificador());
			getEventoFolhaPagamentoVO().getFormulaDia().setDescricao(formula.getDescricao());
			break;
		case "referencia":
			getEventoFolhaPagamentoVO().getFormulaReferencia().setCodigo(formula.getCodigo());
			getEventoFolhaPagamentoVO().getFormulaReferencia().setIdentificador(formula.getIdentificador());
			getEventoFolhaPagamentoVO().getFormulaReferencia().setDescricao(formula.getDescricao());
			break;
		case "fixa":
			getEventoFolhaPagamentoVO().getFormulaFixa().setCodigo(formula.getCodigo());
			getEventoFolhaPagamentoVO().getFormulaFixa().setIdentificador(formula.getIdentificador());
			getEventoFolhaPagamentoVO().getFormulaFixa().setDescricao(formula.getDescricao());
			break;
		default:
			getEventoFolhaPagamentoVO().getFormulaReferencia().setCodigo(formula.getCodigo());
			getEventoFolhaPagamentoVO().getFormulaReferencia().setIdentificador(formula.getIdentificador());
			getEventoFolhaPagamentoVO().getFormulaReferencia().setDescricao(formula.getDescricao());
			break;
		}
	}

	public void setFormulaValor() {
		setFormulaConsultada("valor");
		setValorConsultaFormula("");
		setCampoConsultaFormula("descricao");
	}
	
	public void setFormulaHora() {
		setFormulaConsultada("hora");
		setValorConsultaFormula("");
		setCampoConsultaFormula("descricao");
	}
	
	public void setFormulaDia() {
		setFormulaConsultada("dia");
		setValorConsultaFormula("");
		setCampoConsultaFormula("descricao");
	}
	
	public void setFormulaReferencia() {
		setFormulaConsultada("referencia");
		setValorConsultaFormula("");
		setCampoConsultaFormula("descricao");
	}
	
	public void setFormulaFixa() {
		setFormulaConsultada("fixa");
		setValorConsultaFormula("");
		setCampoConsultaFormula("descricao");
	}

	public void limparFormulaValor() {
		getEventoFolhaPagamentoVO().getFormulaValor().setCodigo(null);
		getEventoFolhaPagamentoVO().getFormulaValor().setIdentificador("");
		getEventoFolhaPagamentoVO().getFormulaValor().setDescricao("");
	}
	
	public void limparFormulaHora() {
		getEventoFolhaPagamentoVO().getFormulaHora().setCodigo(null);
		getEventoFolhaPagamentoVO().getFormulaHora().setIdentificador("");
		getEventoFolhaPagamentoVO().getFormulaHora().setDescricao("");
	}
	
	public void limparFormulaDia() {
		getEventoFolhaPagamentoVO().getFormulaDia().setCodigo(null);
		getEventoFolhaPagamentoVO().getFormulaDia().setIdentificador("");
		getEventoFolhaPagamentoVO().getFormulaDia().setDescricao("");
	}

	public void limparFormulaReferencia() {
		getEventoFolhaPagamentoVO().getFormulaReferencia().setCodigo(null);
		getEventoFolhaPagamentoVO().getFormulaReferencia().setIdentificador("");
		getEventoFolhaPagamentoVO().getFormulaReferencia().setDescricao("");
	}

	public void limparFormulaFixa() {
		getEventoFolhaPagamentoVO().getFormulaFixa().setCodigo(null);
		getEventoFolhaPagamentoVO().getFormulaFixa().setIdentificador("");
		getEventoFolhaPagamentoVO().getFormulaFixa().setDescricao("");
	}

	public void limparDadosEvento() {
		setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
		setListaEventosFolhaPagamento(new ArrayList<>());
	}
	
	public void limparCategoriaDespesa() {
		this.getEventoFolhaPagamentoVO().setCategoriaDespesaVO(new CategoriaDespesaVO());
	}

	public void limparDadosConsultaFormula() {
		listaConsultaFormula = new ArrayList<FormulaFolhaPagamentoVO>(0);
	}

	public void limparIncidencias() {
		switch (getEventoFolhaPagamentoVO().getTipoLancamento()) {
		case DESCONTO:
			limparTabIncidenciasProvento();
			break;
		case PROVENTO:
			limparTabIncidenciasDesconto();
			break;
		default:
			break;
		}
	}

	public void limparTabIncidenciasProvento() {
		getEventoFolhaPagamentoVO().setInssFolhaNormal(false);
		getEventoFolhaPagamentoVO().setIrrfFolhaNormal(false);
		getEventoFolhaPagamentoVO().setFgtsFolhaNormal(false);
		getEventoFolhaPagamentoVO().setDsrFolhaNormal(false);
		getEventoFolhaPagamentoVO().setSalarioFamiliaFolhaNormal(false);
		getEventoFolhaPagamentoVO().setIrrfFerias(false);
		getEventoFolhaPagamentoVO().setAdicionalFerias(false);
		getEventoFolhaPagamentoVO().setInssDecimoTerceiro(false);
		getEventoFolhaPagamentoVO().setIrrfDecimoTerceiro(false);
		getEventoFolhaPagamentoVO().setFgtsDecimoTerceiro(false);
		getEventoFolhaPagamentoVO().setFolhaPensao(false);
		getEventoFolhaPagamentoVO().setFeriasPensao(false);
		getEventoFolhaPagamentoVO().setDecimoTerceiroPensao(false);
		getEventoFolhaPagamentoVO().setParticipacaoLucroPensao(false);
		getEventoFolhaPagamentoVO().setRais(false);
		getEventoFolhaPagamentoVO().setInformeRendimento(false);
		getEventoFolhaPagamentoVO().setPrevidenciaPropria(false);
		getEventoFolhaPagamentoVO().setPrevidenciaObrigatoria(false);
		getEventoFolhaPagamentoVO().setPlanoSaude(false);
		getEventoFolhaPagamentoVO().setDecimoTerceiroPrevidenciaPropria(false);
		getEventoFolhaPagamentoVO().setDecimoTerceiroPlanoSaude(false);
		getEventoFolhaPagamentoVO().setValeTransporte(false);
	}

	public void limparTabIncidenciasDesconto() {
		getEventoFolhaPagamentoVO().setDedutivelIrrf(false);
		getEventoFolhaPagamentoVO().setEstornaInss(false);
		getEventoFolhaPagamentoVO().setEstornaIrrf(false);
		getEventoFolhaPagamentoVO().setEstornaFgts(false);
		getEventoFolhaPagamentoVO().setEstornaValeTransporte(false);
		getEventoFolhaPagamentoVO().setEstornaSalarioFamilia(false);
		getEventoFolhaPagamentoVO().setEstornaIrrfFerias(false);
		getEventoFolhaPagamentoVO().setDedutivelIrrfFerias(false);
		getEventoFolhaPagamentoVO().setEstornaInssDecimoTerceiro(false);
		getEventoFolhaPagamentoVO().setEstornaFgtsDecimoTerceiro(false);
		getEventoFolhaPagamentoVO().setEstornaIrrfDecimoTerceiro(false);
		getEventoFolhaPagamentoVO().setDedutivelIrrfDecimoTerceiro(false);
		getEventoFolhaPagamentoVO().setFolhaPensaoDesconto(false);
		getEventoFolhaPagamentoVO().setFeriasPensaoDesconto(false);
		getEventoFolhaPagamentoVO().setDecimentoTerceiroPensaoDesconto(false);
		getEventoFolhaPagamentoVO().setParticipacaoLucroPensaoDesconto(false);
		getEventoFolhaPagamentoVO().setValeTransporteDesconto(false);
	}

	public void consultarFormulaFixa() {
		if(getEventoFolhaPagamentoVO().getFormulaFixa().getIdentificador().isEmpty()){
			getEventoFolhaPagamentoVO().setFormulaFixa(new FormulaFolhaPagamentoVO());
			return;
		}

		consultarFormulaDinamica("fixa");
	}

	public void consultarFormulaValor() {
		if(getEventoFolhaPagamentoVO().getFormulaValor().getIdentificador().isEmpty()){
			getEventoFolhaPagamentoVO().setFormulaValor(new FormulaFolhaPagamentoVO());
			return;
		}

		consultarFormulaDinamica("valor");
	}

	public void consultarFormulaHora() {
		if(getEventoFolhaPagamentoVO().getFormulaHora().getIdentificador().isEmpty()){
			getEventoFolhaPagamentoVO().setFormulaHora(new FormulaFolhaPagamentoVO());
			return;
		}

		consultarFormulaDinamica("hora");
	}

	public void consultarFormulaDia() {
		if(getEventoFolhaPagamentoVO().getFormulaDia().getIdentificador().isEmpty()){
			getEventoFolhaPagamentoVO().setFormulaDia(new FormulaFolhaPagamentoVO());
			return;
		}

		consultarFormulaDinamica("dia");
	}

	public void consultarFormulaReferencia() {
		if(getEventoFolhaPagamentoVO().getFormulaReferencia().getIdentificador().isEmpty()){
			getEventoFolhaPagamentoVO().setFormulaReferencia(new FormulaFolhaPagamentoVO());
			return;
		}

		consultarFormulaDinamica("referencia");
	}

	private void consultarFormulaDinamica(String campo) {
		setarValoresConsulta(campo);
		consultarFormula();
		setValorFormula();
	}
	
	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getValorConsultaEvento());
			}
			
			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(getCampoConsultaEvento(), getValorConsultaEvento(), "ATIVO", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(this.eventoFolhaPagamento.getIdentificador())) {
				this.setEventoFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(this.eventoFolhaPagamento.getIdentificador(), false, getUsuarioLogado()));
			}
		} catch (Exception e) {
			this.setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		setEventoFolhaPagamento(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	} 

	public void scrollerListenerCategoriaDespesa(DataScrollEvent dataScrollerEvent) {
		getConsultaCategoriaDespesa().setPaginaAtual(dataScrollerEvent.getPage());
		getConsultaCategoriaDespesa().setPage(dataScrollerEvent.getPage());
		this.consultarCategoriaDespesa();
	}

	public void consultarCategoriaDespesa() {
		try {
			getConsultaCategoriaDespesa().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getCategoriaDespesaFacade().consultarPorEnumCampoConsulta(getConsultaCategoriaDespesa());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCategoriaDespesaPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(getEventoFolhaPagamentoVO().getCategoriaDespesaVO().getIdentificadorCategoriaDespesa())) {
				this.getEventoFolhaPagamentoVO().setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesaUnico(getEventoFolhaPagamentoVO().getCategoriaDespesaVO().getIdentificadorCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			this.setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarCategoriaDespesa() {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItem");
		getEventoFolhaPagamentoVO().setCategoriaDespesaVO(obj);

		setConsultaCategoriaDespesa(new DataModelo());
	}

	/**
	 * Adiciona os evento vinculados a lista.
	 */
	public void adicionarEventosVinculados() {
		try {
			if (!Uteis.isAtributoPreenchido(eventoFolhaPagamento.getCodigo())){
				throw new Exception(UteisJSF.internacionalizar("msg_GrupoLancamentoFolhaPagamento_evento"));
			}

			for (EventoFolhaPagamentoItemVO obj : getEventoFolhaPagamentoVO().getEventoFolhaPagamentoItemVOs()) {
				if (obj.getEventoFolhaPagamentoItem().getCodigo().equals(getEventoFolhaPagamento().getCodigo())) {
					setMensagemDetalhada(MSG_TELA.msg_erro.name(), UteisJSF.internacionalizar("msg_GrupoLancamentoFolhaPagamento_duplicidadeEventoFolha"));
					return;
				}
			}

			EventoFolhaPagamentoItemVO obj = new EventoFolhaPagamentoItemVO();
			obj.setEventoFolhaPagamentoItem(getEventoFolhaPagamento());
			getEventoFolhaPagamentoVO().getEventoFolhaPagamentoItemVOs().add(obj);
			
			setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Remove da lista o evento selecionado
	 * @param evento
	 */
	public void removerEventoFolhaPagamento(EventoFolhaPagamentoItemVO evento) {
		getEventoFolhaPagamentoVO().getEventoFolhaPagamentoItemVOs().removeIf(p -> p.getEventoFolhaPagamentoItem().getCodigo().equals(evento.getEventoFolhaPagamentoItem().getCodigo()));
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}
	
	private void setarValoresConsulta(String campo) {
		setCampoConsultaFormula("identificador");
		setFormulaConsultada(campo);

		switch (getFormulaConsultada()) {
		case "fixa":
			setValorConsultaFormula(getEventoFolhaPagamentoVO().getFormulaFixa().getIdentificador());
			break;
		case "valor":
			setValorConsultaFormula(getEventoFolhaPagamentoVO().getFormulaValor().getIdentificador());
			break;
		case "dia":
			setValorConsultaFormula(getEventoFolhaPagamentoVO().getFormulaDia().getIdentificador());
			break;
		case "hora":
			setValorConsultaFormula(getEventoFolhaPagamentoVO().getFormulaHora().getIdentificador());
			break;
		case "referencia":
			setValorConsultaFormula(getEventoFolhaPagamentoVO().getFormulaReferencia().getIdentificador());
			break;
		default:
			break;
		}
	}

	private void setValorFormula() {
		FormulaFolhaPagamentoVO formula = new FormulaFolhaPagamentoVO();
		if(!getListaConsultaFormula().isEmpty()) {
			formula = (FormulaFolhaPagamentoVO) getListaConsultaFormula().get(0);
		} 
		
		switch (getFormulaConsultada()) {
		case "fixa":
			getEventoFolhaPagamentoVO().setFormulaFixa(formula);
			break;
		case "valor":
			getEventoFolhaPagamentoVO().setFormulaValor(formula);
			break;
		case "dia":
			getEventoFolhaPagamentoVO().setFormulaDia(formula);
			break;
		case "hora":
			getEventoFolhaPagamentoVO().setFormulaHora(formula);
			break;
		case "referencia":
			getEventoFolhaPagamentoVO().setFormulaReferencia(formula);
			break;
		default:
			break;
		}
	}

	public EventoFolhaPagamentoVO getEventoFolhaPagamentoVO() {
		if (eventoFolhaPagamentoVO == null)
			eventoFolhaPagamentoVO = new EventoFolhaPagamentoVO();
		return eventoFolhaPagamentoVO;
	}

	public void setEventoFolhaPagamentoVO(EventoFolhaPagamentoVO eventoFolhaPagamentoVO) {
		this.eventoFolhaPagamentoVO = eventoFolhaPagamentoVO;
	}

    public String getValorConsultaIncidencia() {
		if (valorConsultaIncidencia == null)
			valorConsultaIncidencia = "";
		return valorConsultaIncidencia;
	}

	public void setValorConsultaIncidencia(String valorConsultaIncidencia) {
		this.valorConsultaIncidencia = valorConsultaIncidencia;
	}

	
	public List getListaConsultaIncidencia() {
		if (listaConsultaIncidencia == null)
			listaConsultaIncidencia = new ArrayList<>();
		return listaConsultaIncidencia;
	}

	public void setListaConsultaIncidencia(List listaConsultaIncidencia) {
		this.listaConsultaIncidencia = listaConsultaIncidencia;
	}

	public List getListaConsultaFormula() {
		if (listaConsultaFormula == null)
			listaConsultaFormula = new ArrayList<>();
		return listaConsultaFormula;
	}

	public void setListaConsultaFormula(List listaConsultaFormula) {
		this.listaConsultaFormula = listaConsultaFormula;
	}

	
	public String getCampoConsultaFormula() {
		if (campoConsultaFormula == null)
			campoConsultaFormula = "";
		return campoConsultaFormula;
	}

	public void setCampoConsultaFormula(String campoConsultaFormula) {
		this.campoConsultaFormula = campoConsultaFormula;
	}

	public String getValorConsultaFormula() {
		if (valorConsultaFormula == null)
			valorConsultaFormula = "";
		return valorConsultaFormula;
	}

	public void setValorConsultaFormula(String valorConsultaFormula) {
		this.valorConsultaFormula = valorConsultaFormula;
	}

	public String getFormulaConsultada() {
		if (formulaConsultada == null)
			formulaConsultada = "";
		return formulaConsultada;
	}

	public void setFormulaConsultada(String formulaConsultada) {
		this.formulaConsultada = formulaConsultada;
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

	public String getCampoConsultaEvento() {
		return campoConsultaEvento;
	}

	public void setCampoConsultaEvento(String campoConsultaEvento) {
		this.campoConsultaEvento = campoConsultaEvento;
	}

	public String getValorConsultaEvento() {
		return valorConsultaEvento;
	}

	public void setValorConsultaEvento(String valorConsultaEvento) {
		this.valorConsultaEvento = valorConsultaEvento;
	}

	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
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
	
	public void adicionarEventoMediaFolha() {
		try {
			validarEventoDeMedia();
			getEventoFolhaPagamentoVO().getEventoMediaVOs().add(getEventoMediaVO());
			setEventoMediaVO(new EventoFolhaPagamentoMediaVO());
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	/**
	 * Valida se o registro que está sendo inserido e valido
	 *  
	 * @return true -> caso seja verdadeiro || false -> registro com informacoes invalidas
	 */
	private void validarEventoDeMedia() throws ConsistirException {
		
		if(!Uteis.isAtributoPreenchido(getEventoMediaVO().getGrupo())) 
			throw new ConsistirException(UteisJSF.internacionalizar("msg_EventoFolhaPagamentoMedia_grupoNaoInformado"));
		
		if(getEventoFolhaPagamentoVO().getEventoMediaVOs().stream().filter(p -> p.getGrupo().equals(getEventoMediaVO().getGrupo()) && p.getTipoEventoMediaEnum().equals(getEventoMediaVO().getTipoEventoMediaEnum())).count() > 0)
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_valorduplicado"));
		
	}

	public void removerEventoMediaFolha (EventoFolhaPagamentoMediaVO eventoMedia) {
		getEventoFolhaPagamentoVO().getEventoMediaVOs().removeIf(p -> p.getGrupo().equals(eventoMedia.getGrupo()) && p.getTipoEventoMediaEnum().getValor().equals(eventoMedia.getTipoEventoMediaEnum().getValor()));		
	}

	public EventoFolhaPagamentoMediaVO getEventoMediaVO() {
		if(eventoMediaVO == null)
			eventoMediaVO = new EventoFolhaPagamentoMediaVO();
		return eventoMediaVO;
	}

	public void setEventoMediaVO(EventoFolhaPagamentoMediaVO eventoMediaVO) {
		this.eventoMediaVO = eventoMediaVO;
	}
	
	
	/**
	 * Visualiza a tela de Formula com os dados da Formula Fixa
	 */
	public void visualizarFormulaFixaFolhaPagamento() {
		visualizarFormulaFolhaPagamento(getEventoFolhaPagamentoVO().getFormulaFixa());
	}
	
	/**
	 * Visualiza a tela de Formula com os dados da Formula Valor
	 */
	public void visualizarFormulaValorFolhaPagamento() {
		visualizarFormulaFolhaPagamento(getEventoFolhaPagamentoVO().getFormulaValor());
	}
	
	/**
	 * Visualiza a tela de Formula com os dados da Formula Hora
	 */
	public void visualizarFormulaHoraFolhaPagamento() {
		visualizarFormulaFolhaPagamento(getEventoFolhaPagamentoVO().getFormulaHora());
	}
	
	/**
	 * Visualiza a tela de Formula com os dados da Formula Dia
	 */
	public void visualizarFormulaDiaFolhaPagamento() {
		visualizarFormulaFolhaPagamento(getEventoFolhaPagamentoVO().getFormulaDia());
	}
	
	/**
	 * Visualiza a tela de Formula com os dados da Formula Referencia
	 */
	public void visualizarFormulaReferenciaFolhaPagamento() {
		visualizarFormulaFolhaPagamento(getEventoFolhaPagamentoVO().getFormulaReferencia());
	}
	
	/**
	 * Visualiza a tela de Formula Folha De Pagamento com a Formula preenchida
	 * @param formula
	 */
	public void visualizarFormulaFolhaPagamento(FormulaFolhaPagamentoVO formula) {
		context().getExternalContext().getSessionMap().put("formulaFolhaPagamentoChamadoDaTelaDeEventoVO", formula);
		removerControleMemoriaFlashTela(FormulaFolhaPagamentoControle.class.getSimpleName());
	}

	public String getValorTipoLancamento() {
		if (valorTipoLancamento == null) {
			valorTipoLancamento = "TODOS";
		}
		return valorTipoLancamento;
	}

	public void setValorTipoLancamento(String valorTipoLancamento) {
		this.valorTipoLancamento = valorTipoLancamento;
	}

	public DataModelo getConsultaCategoriaDespesa() {
		if (consultaCategoriaDespesa == null) {
			consultaCategoriaDespesa = new DataModelo();
		}
		return consultaCategoriaDespesa;
	}

	public void setConsultaCategoriaDespesa(DataModelo consultaCategoriaDespesa) {
		this.consultaCategoriaDespesa = consultaCategoriaDespesa;
	}
}