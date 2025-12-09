package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.RescisaoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO.EnumCampoConsultaSecaoFolhaPagamento;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoRescisaoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoTemplateFolhaPagamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas rescisaoForm.xhtml e rescisaoCons.xhtl
 * com as funcionalidades da classe <code>RescisaoVO</code>.
 * Implemtacao da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaGEDVO
 */
@Controller("RescisaoControle")
@Scope("viewScope")
@Lazy
public class RescisaoControle extends SuperControle {

	private static final long serialVersionUID = 1455114507405935993L;
	
	private RescisaoVO rescisaoVO;
	
	private DataModelo dataModeloFuncionarioCargo;
	private DataModelo dataModeloRescisaoIndividual;

	private List<FuncionarioCargoVO> funcionarioCargoVOs;
	
	private List<SelectItem> listaSelectItemFormacontratacao;
	private List<SelectItem> listaSelectItemRecebimento;
	private List<SelectItem> listaSelectItemSituacao;
	
	private String valorConsultaNome;
	
	private String campoConsultaSecaoFolhaPagamento;
    private String valorConsultaSecaoFolhaPagamento;
    private List<SecaoFolhaPagamentoVO> listaConsultaSecaoFolhaPagamento;
	
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	
	private String[] formasContratacoes = {};
	private String[] recebimentos = {};
	private String[] situacoes = {};
	
	private Boolean marcarTodosFormaContratacao;
	private Boolean marcarTodosRecebimento;
	private Boolean marcarTodosSituacao;

	private ProgressBarVO progressBar;
	
	private static final String TELA_CONS = "rescisaoCons";
	private static final String TELA_FORM = "rescisaoForm";
	private static final String CONTEXT_PARA_EDICAO = "itemRescisao";
	
	private Date dataInicial;
	private Date dataFinal;

	public RescisaoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID(MSG_TELA.msg_dados_consultados.name());
		getControleConsulta().setValorConsulta("");
	}

	/**
	 * Rotina responsavel por disponibilizar um novo objeto da classe
	 * <code>RescisaoVO</code> para edicao pelo usuario da aplicacao.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		preencherCompetenciaFolhaPagamento();
		selecionarSituacaoAtiva();
		getRescisaoVO().getTemplateLancamentoFolhaPagamento().setTipoTemplateFolhaPagamento(TipoTemplateFolhaPagamentoEnum.RESCISAO);
		getRescisaoVO().setSituacao(SituacaoRescisaoEnum.ATIVO);
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Preenche a competencia da rescisão consultando a competencia ativa. 
	 */
	private void preencherCompetenciaFolhaPagamento() {
		CompetenciaFolhaPagamentoVO competencia = new CompetenciaFolhaPagamentoVO();

		try {
			competencia = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
			getRescisaoVO().setCompetenciaFolhaPagamento(competencia);

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_competenciaAtivaNaoEncontrado"));
		}		
	}

	/**
	 * Rotina responsavel por disponibilizar os dados de um objeto da classe
	 * <code>RescisaoVO</code> para alteracao. O objeto desta classe e
	 * disponibilizado na session da pagina (request) para que o JSP correspondente
	 * possa disponibiliza-lo para edicao.
	 */
	public String editar() {
		RescisaoVO obj = (RescisaoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		obj.setNovoObj(Boolean.FALSE);
		setRescisaoVO(obj);
		selecionarSituacaoAtiva();

		try {
			formasContratacoes = obj.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";");
			recebimentos = obj.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";");
			situacoes = obj.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";");

			selecionarTodos();

			setRescisaoVO((RescisaoVO) SerializationUtils.clone(obj));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Verifica se o recebimento, forma contratacao e situacao foram selecionado todos e marca o checkbox. 
	 */
	private void selecionarTodos() {
		if (getListaSelectItemRecebimento().size() == recebimentos.length) {
			setMarcarTodosRecebimento(true);
		}

		if (getListaSelectItemFormacontratacao().size() == formasContratacoes.length) {
			setMarcarTodosFormaContratacao(true);
		}

		if (getListaSelectItemSituacao().size() == situacoes.length) {
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
		setListaConsulta(new ArrayList(0));
		setMensagemID(MSG_TELA.msg_dados_consultados.name());
		setControleConsultaOtimizado(new DataModelo());
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Rotina responsavel por gravar no BD os dados editados de um novo objeto da
	 * classe <code>Rescisao</code>. Caso o objeto seja novo (ainda nao gravado
	 * no BD) e acionado a operacao <code>incluir()</code>. Caso contrario e
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistencia o objeto
	 * nao e gravado, sendo re-apresentado para o usuario juntamente com uma
	 * mensagem de erro.
	 */
	public String rescindirContrato() {
		try {
			getFacadeFactory().getRescisaoInterfaceFacade().rescindirContrato(getRescisaoVO(), false, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	/**
	 * Cancela o processo feito de rescindir contrato.
	 */
	public void cancelarRescisao() {
		try {
			getRescisaoVO().setSituacao(SituacaoRescisaoEnum.CANCELADO);
			getFacadeFactory().getRescisaoInterfaceFacade().cancelarRescisao(getRescisaoVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_cancelados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis do
	 * rescisaoCons.xhtml. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			if (Uteis.isAtributoPreenchido(getDataInicial())) {
				getControleConsultaOtimizado().setDataIni(UteisData.getPrimeiroDataMes(getDataInicial()));
			}

			if (Uteis.isAtributoPreenchido(getDataFinal())) {
				getControleConsultaOtimizado().setDataFim(UteisData.getUltimaDataMes(getDataFinal()));
			}

			getFacadeFactory().getRescisaoInterfaceFacade().consultarPorFiltros(getControleConsultaOtimizado(), getDataInicial(), getDataFinal());
			getFacadeFactory().getRescisaoInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado());

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarDadosRescisaoIndividual() {
		try {
			getDataModeloRescisaoIndividual().setLimitePorPagina(10);
			
			getDataModeloRescisaoIndividual().setValorConsulta(getRescisaoVO().getCodigo().toString());
			getFacadeFactory().getRescisaoIndividualInterfaceFacade().consultar(getDataModeloRescisaoIndividual());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por realizar a paginacao da pagina de rescisaoCons.xhtml
	 * 
	 * @param DataScrollEvent
	 */
	public void scrollerListener(DataScrollEvent dataScrollEvent) {
		getDataModeloFuncionarioCargo().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloFuncionarioCargo().setPage(dataScrollEvent.getPage());
        consultarDados();
    }

	public void scrollerListenerRescisaoIndividual(DataScrollEvent dataScrollEvent) {
		getDataModeloRescisaoIndividual().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloRescisaoIndividual().setPage(dataScrollEvent.getPage());
		consultarDadosRescisaoIndividual();
	}

	public void scrollerListenerFuncionarioCargo(DataScrollEvent dataScrollEvent) {
		getDataModeloFuncionarioCargo().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloFuncionarioCargo().setPage(dataScrollEvent.getPage());
		consultarFuncionario();
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
			consultarSecaoFolhaPagamento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}
	
	public boolean apresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() >=10;
	}
	
	public boolean getApresentarResultadoConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 0;
	}
	
	public boolean getApresentarPaginadorConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 5;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}
	
	/**
	 * Rotina responsavel por preencher a combo de consulta da tela.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("funcionario", "Funcionário"));
		return itens;
	}

	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionario() {
		try {
			
			if (getCampoConsultaFuncionario().equals("nome")) {
				getDataModeloFuncionarioCargo().setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionarioAtivo(getDataModeloFuncionarioCargo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getDataModeloFuncionarioCargo().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorNomeFuncionarioAtivo(getDataModeloFuncionarioCargo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				getDataModeloFuncionarioCargo().setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargoAtivo(getDataModeloFuncionarioCargo().getValorConsulta(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getDataModeloFuncionarioCargo().setTotalRegistrosEncontrados(getDataModeloFuncionarioCargo().getListaConsulta().size());
			}
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioPorMatricula() {
		try {
			if (Uteis.isAtributoPreenchido(getRescisaoVO().getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getMatriculaCargo())) {
				FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getRescisaoVO().getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
				getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(funcionarioCargo);

				setMensagemID(MSG_TELA.msg_entre_dados.name());

				if (!funcionarioCargo.getSituacaoFuncionario().equals("ATIVO")) {
					setMensagemDetalhada(MSG_TELA.msg_erro.name(), UteisJSF.internacionalizar("msg_erro_funcionarioCargoInativo"));
					getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
				}
			} else {
				getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
			}
		} catch (Exception e) {
			getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarSecaoFolhaPagamento() {
		try {

			if (getCampoConsultaSecaoFolhaPagamento().equals(EnumCampoConsultaSecaoFolhaPagamento.CODIGO.name()) && 
					(getValorConsultaSecaoFolhaPagamento().trim().isEmpty() || !Uteis.getIsValorNumerico(getValorConsultaSecaoFolhaPagamento()))) {

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
		setMarcarTodosFormaContratacao(FormaContratacaoFuncionarioEnum.values().length == getFormasContratacoes().length ? Boolean.TRUE : Boolean.FALSE); 
		
		getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario("");
		for(String formaContratacao : getFormasContratacoes() ) {
			getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(
					getRescisaoVO().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().concat(formaContratacao).concat(";"));
		}
	}

	public void selecionarRecebimento() {
		setMarcarTodosRecebimento(TipoRecebimentoEnum.values().length == getRecebimentos().length ? Boolean.TRUE : Boolean.FALSE); 

		getRescisaoVO().getTemplateLancamentoFolhaPagamento().setTipoRecebimento("");
		for(String recebimento : getRecebimentos() ) {
			getRescisaoVO().getTemplateLancamentoFolhaPagamento().setTipoRecebimento(
					getRescisaoVO().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().concat(recebimento).concat(";"));
		}
	}
	
	/**
	 * Seleciona a opcao ativa .
	 */
	public void selecionarSituacaoAtiva() {
		getRescisaoVO().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario("ATIVO;");
		setSituacoes(getRescisaoVO().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";"));
	}

	public void selecionarSecaoFolhaPagamento() {
    	SecaoFolhaPagamentoVO obj = (SecaoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("itemSecaoFolhaPagamento");
    	getRescisaoVO().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(obj);
    	this.getListaConsultaSecaoFolhaPagamento().clear();
    }

	public void selecionarFuncionarioCargo() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem");
		this.getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(obj);

		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";
	}

	public void limparDadosSecaoFolhaPagamento() {
    	getRescisaoVO().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
    }
	
	public void limparDadosFuncionario() {
		getFuncionarioCargoVOs().clear();
		getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
	}

	public void selecionarTodosFormaContratacao() {
		getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario("");
		if(getMarcarTodosFormaContratacao()) {
			for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
				getRescisaoVO().getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(
						getRescisaoVO().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().concat(formaContratacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		setFormasContratacoes(getRescisaoVO().getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";"));
	}

	public void selecionarTodosRecebimento() {
		getRescisaoVO().getTemplateLancamentoFolhaPagamento().setTipoRecebimento("");
		if(getMarcarTodosRecebimento()) {
			for (TipoRecebimentoEnum formaRecebimentoEnum : TipoRecebimentoEnum.values()) {
				getRescisaoVO().getTemplateLancamentoFolhaPagamento().setTipoRecebimento(
						getRescisaoVO().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().concat(formaRecebimentoEnum.toString()).concat(";"));
			}
		}

		setRecebimentos(getRescisaoVO().getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";"));
	}

	public void selecionarTodosSituacao() {
		getRescisaoVO().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario("");
		if(getMarcarTodosSituacao()) {
			for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
				getRescisaoVO().getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(
						getRescisaoVO().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().concat(situacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		setSituacoes(getRescisaoVO().getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";"));
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
	
	//GETTER AND SETTER
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

	public String[] getFormasContratacoes() {
		return formasContratacoes;
	}

	public void setFormasContratacoes(String[] formasContratacoes) {
		this.formasContratacoes = formasContratacoes;
	}

	public String[] getRecebimentos() {
		return recebimentos;
	}

	public void setRecebimentos(String[] recebimentos) {
		this.recebimentos = recebimentos;
	}

	public String[] getSituacoes() {
		return situacoes;
	}

	public void setSituacoes(String[] situacoes) {
		this.situacoes = situacoes;
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
	
	public ProgressBarVO getProgressBar() {
		if (progressBar == null) {
			progressBar = new ProgressBarVO();
		}
		return progressBar;
	}

	public void setProgressBar(ProgressBarVO progressBar) {
		this.progressBar = progressBar;
	}

	public RescisaoVO getRescisaoVO() {
		if (rescisaoVO == null) {
			rescisaoVO = new RescisaoVO();
		}
		return rescisaoVO;
	}

	public void setRescisaoVO(RescisaoVO rescisaoVO) {
		this.rescisaoVO = rescisaoVO;
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

	public DataModelo getDataModeloFuncionarioCargo() {
		if (dataModeloFuncionarioCargo == null) {
			dataModeloFuncionarioCargo = new DataModelo();
			dataModeloFuncionarioCargo.setLimitePorPagina(10);
		}
		return dataModeloFuncionarioCargo;
	}

	public void setDataModeloFuncionarioCargo(DataModelo dataModeloFuncionarioCargo) {
		this.dataModeloFuncionarioCargo = dataModeloFuncionarioCargo;
	}

	public DataModelo getDataModeloRescisaoIndividual() {
		if (dataModeloRescisaoIndividual == null) {
			dataModeloRescisaoIndividual = new DataModelo();
		}
		return dataModeloRescisaoIndividual;
	}

	public void setDataModeloRescisaoIndividual(DataModelo dataModeloRescisaoIndividual) {
		this.dataModeloRescisaoIndividual = dataModeloRescisaoIndividual;
	}

	public String getValorConsultaNome() {
		return valorConsultaNome;
	}

	public void setValorConsultaNome(String valorConsultaNome) {
		this.valorConsultaNome = valorConsultaNome;
	}
}