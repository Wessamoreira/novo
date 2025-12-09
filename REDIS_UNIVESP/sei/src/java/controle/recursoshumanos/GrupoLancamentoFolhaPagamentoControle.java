package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.GrupoLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO.EnumCampoConsultaSecaoFolhaPagamento;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTemplateFolhaPagamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas grupoLancamentoFolhaPagamentoForm.xhtml e grupoLancamentoFolhaPagamentoCons.xhtl com as
 * funcionalidades da classe <code>GrupoLancamentoFolhaPagamentoControleVO</code>.
 * Implemtacao da camada controle (Backing Bean).
 * 
 * @see SuperControle
 */
@Controller("GrupoLancamentoFolhaPagamentoControle")
@Scope("viewScope")
@Lazy
public class GrupoLancamentoFolhaPagamentoControle extends SuperControle {

	private static final long serialVersionUID = -5587421283312115856L;

	private GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento;
	private TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO;
	private EventoFolhaPagamentoVO eventoFolhaPagamento;
	
	private List<FuncionarioVO> funcionarioVOs;
	private List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento;
	
	List<TemplateEventoFolhaPagamentoVO> listaAnteriorTemplateEvento;
	
	private List<SelectItem> listaSelectItemFormacontratacao;
	private List<SelectItem> listaSelectItemRecebimento;
	private List<SelectItem> listaSelectItemSituacao;
	
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	
	private String campoConsultaEvento;
	private String valorConsultaEvento;
	
	public String[] formaContratacao = {};
	public String[] recebimento = {};
	public String[] situacao = {};

	private Boolean marcarTodosFormaContratacao;
	private Boolean marcarTodosRecebimento;
	private Boolean marcarTodosSituacao;
	
	private String campoConsultaSecaoFolhaPagamento;
    private String valorConsultaSecaoFolhaPagamento;
	protected List<SecaoFolhaPagamentoVO> listaConsultaSecaoFolhaPagamento;
	
	List<FuncionarioCargoVO> funcionariosCargo;
	
	public GrupoLancamentoFolhaPagamentoControle() {
		if (!Uteis.isAtributoPreenchido(grupoLancamentoFolhaPagamento)) {
			grupoLancamentoFolhaPagamento = new GrupoLancamentoFolhaPagamentoVO();
		}

		grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setTipoTemplateFolhaPagamento(TipoTemplateFolhaPagamentoEnum.GRUPO_LANCAMENTO);
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsavel por disponibilizar um novo objeto da classe
	 * <code>GrupoLancamentoFolhaPagamento</code> para edicao pelo usuario da aplicacao.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setGrupoLancamentoFolhaPagamento(new GrupoLancamentoFolhaPagamentoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("grupoLancamentoFolhaPagamentoForm");
	}

	/**
	 * Rotina responsavel por disponibilizar os dados de um objeto da classe
	 * <code>GrupoLancamentoFolhaPagamentoVO</code> para alteracao. O objeto desta classe e
	 * disponibilizado na session da pagina (request) para que o JSP correspondente
	 * possa disponibiliza-lo para edicao.
	 */
	public String editar() {
		try {
			GrupoLancamentoFolhaPagamentoVO obj = (GrupoLancamentoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("grupoLancamentoFP");
			obj.setNovoObj(Boolean.FALSE);
			if (obj.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo() != null || !obj.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo().equals(0)) {
				obj.getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimariaUnica(obj.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(obj.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo())) {
				obj.getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(obj.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo().longValue()));
			}
			obj.getTemplateLancamentoFolhaPagamento().setListaEventosDoTemplate(getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().consultarPorTemplateEventoFolhaPagamento(obj.getTemplateLancamentoFolhaPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			for (TemplateEventoFolhaPagamentoVO object : obj.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate()) {
				getListaAnteriorTemplateEvento().add(object);
			}
			formaContratacao = obj.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";");
			recebimento = obj.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";");
			situacao = obj.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";");
			
			selecionarTodos();
			setFuncionariosCargo(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoFuncionarioPorFiltrosGrupoLancamentoFolhaPagamento(obj));
			setGrupoLancamentoFolhaPagamento((GrupoLancamentoFolhaPagamentoVO) SerializationUtils.clone(obj));

			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("grupoLancamentoFolhaPagamentoForm");
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
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("grupoLancamentoFolhaPagamentoCons");
	}

	/**
	 * Rotina responsavel por gravar no BD os dados editados de um novo objeto da
	 * classe <code>GrupoLancamentoFolhaPagamento</code>. Caso o objeto seja novo (ainda nao gravado
	 * no BD) e acionado a operacao <code>incluir()</code>. Caso contrario e
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistencia o objeto
	 * nao e gravado, sendo re-apresentado para o usuario juntamente com uma
	 * mensagem de erro.
	 */
	public String persistir() {
		try {
			grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setTipoTemplateFolhaPagamento(TipoTemplateFolhaPagamentoEnum.GRUPO_LANCAMENTO);
			getFacadeFactory().getGrupoLancamentoFolhaPagamentoInterfaceFacade().persistir(grupoLancamentoFolhaPagamento, Boolean.TRUE, getUsuarioLogado(), getListaAnteriorTemplateEvento());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoLancamentoFolhaPagamentoForm"); 
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoLancamentoFolhaPagamentoForm");
		}
	}

	/**
	 * Operacao responsavel por processar a exclusao um objeto da classe
	 * <code>GrupoLancamentoFolhaPagamento</code> Apos a exclusao ela automaticamente aciona a
	 * rotina para uma nova inclusao.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getGrupoLancamentoFolhaPagamentoInterfaceFacade().excluir(grupoLancamentoFolhaPagamento, Boolean.TRUE, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoLancamentoFolhaPagamentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoLancamentoFolhaPagamentoForm");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis do
	 * tabelaReferenciaFolhaPagamentoCons.xhtml. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<GrupoLancamentoFolhaPagamentoVO> objs = new ArrayList<GrupoLancamentoFolhaPagamentoVO>(0);

			objs = getFacadeFactory().getGrupoLancamentoFolhaPagamentoInterfaceFacade().consultarPorFiltro(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TipoDocumentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				setListaConsulta(new ArrayList<FuncionarioVO>(0));
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setFuncionarioVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarEvento() {
		try {
			if(getCampoConsultaEvento().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getValorConsultaEvento());
			} 

			setListaEventosFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(campoConsultaEvento, valorConsultaEvento,"ATIVO", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarEventoPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(this.eventoFolhaPagamento.getIdentificador())) {
				this.setEventoFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(this.eventoFolhaPagamento.getIdentificador(), false, getUsuarioLogado()));
			} else {
				this.setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			}
		} catch (Exception e) {
			this.setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarSecaoFolhaPagamento() {
		try {

			if (getCampoConsultaSecaoFolhaPagamento().equals(EnumCampoConsultaSecaoFolhaPagamento.CODIGO.name())) {
				if (getValorConsultaSecaoFolhaPagamento().trim().isEmpty() || !Uteis.getIsValorNumerico(getValorConsultaSecaoFolhaPagamento())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_SecaoFolhaPagamento_ConsultaCampoCodigoInvalido"));
				}
			}

			setListaConsultaSecaoFolhaPagamento(getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultar(getCampoConsultaSecaoFolhaPagamento(), getValorConsultaSecaoFolhaPagamento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarSecaoFolhaPagamento() {
    	SecaoFolhaPagamentoVO obj = (SecaoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("itemSecaoFolhaPagamento");
    	getGrupoLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(obj);
    	this.getListaConsultaSecaoFolhaPagamento().clear();
    }

	public void selecionarFormaContratacao() {
		grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario("");
		for(String formaContratacao : this.formaContratacao ) {
			grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(
					grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().concat(formaContratacao).concat(";"));
		}
	}

	public void selecionarRecebimento() {
		grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setTipoRecebimento("");
		for(String recebimento : this.recebimento ) {
			grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setTipoRecebimento(grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().concat(recebimento).concat(";"));
		}
	}

	public void selecionarSituacao() {
		grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario("");
		for(String situacao : this.situacao ) {
			grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().concat(situacao).concat(";"));
		}
	}

	public void selecionarTipoDocumento() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		getGrupoLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(obj);

		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";
		getFuncionarioVOs().clear();
	}

	public void selecionarEvento() {
		EventoFolhaPagamentoVO obj = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		setEventoFolhaPagamento(obj);

		valorConsultaEvento = "";
		campoConsultaEvento = "";
		getListaEventosFolhaPagamento().clear();
	}

	/**
	 * Adiciona o evento da folha de pagamento selecionado a lista.
	 */
	public void adicionarEventoFolhaPagamento() {
		try {
			getFacadeFactory().getGrupoLancamentoFolhaPagamentoInterfaceFacade().validarEventoFolhaPagamento(grupoLancamentoFolhaPagamento, templateEventoFolhaPagamentoVO, eventoFolhaPagamento);

			templateEventoFolhaPagamentoVO.setEventoFolhaPagamento(eventoFolhaPagamento);
			this.grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate().add(templateEventoFolhaPagamentoVO);
			limparDadosEvento();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Remove evento da folha de pagamento inserido na tabela.
	 */
	public void removerEventoFolhaPagamento(TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamento) {
		setMensagemDetalhada("");
		Iterator<TemplateEventoFolhaPagamentoVO> iterator = grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate().iterator();

		while(iterator.hasNext()) {
			TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO = iterator.next();
			if (templateEventoFolhaPagamento.getEventoFolhaPagamento().getCodigo().equals(templateEventoFolhaPagamentoVO.getEventoFolhaPagamento().getCodigo())) {
				iterator.remove();
			}
		}
	}

	/**
	 * Rotina responsavel por realizar a paginacao da pagina de grupoLancamentoFolhaPagamentoCons.xhtml
	 * 
	 * @param DataScrollEvent
	 * @throws Exception
	 */
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
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

	/**
	 * Operacao que libera todos os recursos (atributos, listas, objetos) do backing
	 * bean. Garantindo uma melhor atuacao do Garbage Coletor do Java. A mesma e
	 * automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		grupoLancamentoFolhaPagamento = null;
	}

	public void limparDadosFuncionario() {
		grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
	}

	public void limparDadosEvento() {
		setEventoFolhaPagamento(new EventoFolhaPagamentoVO());
		setListaEventosFolhaPagamento(new ArrayList<>());
		setTemplateEventoFolhaPagamentoVO(new TemplateEventoFolhaPagamentoVO());
	}
	
	public void limparDadosSecaoFolhaPagamento() {
	  	getGrupoLancamentoFolhaPagamento().getTemplateLancamentoFolhaPagamento().setSecaoFolhaPagamento(new SecaoFolhaPagamentoVO());
    }

	public boolean getApresentarResultadoConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 0;
	}
	
	public boolean getApresentarPaginadorConsultaSecao() {
		return getListaConsultaSecaoFolhaPagamento().size() > 5;
	}

	/**
	 * Rotina responsavel por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
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
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemFormacontratacao;
	}

	public void setListaSelectItemFormacontratacao(List<SelectItem> listaSelectItemFormacontratacao) {
		this.listaSelectItemFormacontratacao = listaSelectItemFormacontratacao;
	}

	public List<SelectItem> getListaSelectItemRecebimento() {
		if (listaSelectItemRecebimento == null || listaSelectItemRecebimento.isEmpty()) {
			listaSelectItemRecebimento = new ArrayList<SelectItem>(0);
			try {
				for (TipoRecebimentoEnum tipoRecebimentoEnum : TipoRecebimentoEnum.values()) {
					listaSelectItemRecebimento.add(new SelectItem(tipoRecebimentoEnum, tipoRecebimentoEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemRecebimento;
	}

	public void setListaSelectItemRecebimento(List<SelectItem> listaSelectItemRecebimento) {
		this.listaSelectItemRecebimento = listaSelectItemRecebimento;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null || listaSelectItemSituacao.isEmpty()) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
			try {
				for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
					listaSelectItemSituacao.add(new SelectItem(situacaoFuncionarioEnum, situacaoFuncionarioEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public GrupoLancamentoFolhaPagamentoVO getGrupoLancamentoFolhaPagamento() {
		return grupoLancamentoFolhaPagamento;
	}

	public void setGrupoLancamentoFolhaPagamento(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento) {
		this.grupoLancamentoFolhaPagamento = grupoLancamentoFolhaPagamento;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<FuncionarioVO> getFuncionarioVOs() {
		if (funcionarioVOs == null) {
			funcionarioVOs = new ArrayList<FuncionarioVO>(0);
		}
		return funcionarioVOs;
	}

	public void setFuncionarioVOs(List<FuncionarioVO> funcionarioVOs) {
		this.funcionarioVOs = funcionarioVOs;
	}

	public List<EventoFolhaPagamentoVO> getListaEventosFolhaPagamento() {
		return listaEventosFolhaPagamento;
	}

	public void setListaEventosFolhaPagamento(List<EventoFolhaPagamentoVO> listaEventosFolhaPagamento) {
		this.listaEventosFolhaPagamento = listaEventosFolhaPagamento;
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

	public TemplateEventoFolhaPagamentoVO getTemplateEventoFolhaPagamentoVO() {
		if (templateEventoFolhaPagamentoVO == null) {
			templateEventoFolhaPagamentoVO = new TemplateEventoFolhaPagamentoVO();
		}
		return templateEventoFolhaPagamentoVO;
	}

	public void setTemplateEventoFolhaPagamentoVO(TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO) {
		this.templateEventoFolhaPagamentoVO = templateEventoFolhaPagamentoVO;
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

	public List<TemplateEventoFolhaPagamentoVO> getListaAnteriorTemplateEvento() {
		if (listaAnteriorTemplateEvento == null) {
			listaAnteriorTemplateEvento = new ArrayList<TemplateEventoFolhaPagamentoVO>(0);
		}
		return listaAnteriorTemplateEvento;
	}

	public void setListaAnteriorTemplateEvento(List<TemplateEventoFolhaPagamentoVO> listaAnteriorGrupoEvento) {
		this.listaAnteriorTemplateEvento = listaAnteriorGrupoEvento;
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

	public void selecionarTodosFormaContratacao() {
		grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario("");
		if(getMarcarTodosFormaContratacao()) {
			for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
				grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(
						grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().concat(formaContratacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		formaContratacao = grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().split(";");
	}
	
	public void selecionarTodosRecebimento() {
		grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setTipoRecebimento("");
		if(getMarcarTodosRecebimento()) {
			for (TipoRecebimentoEnum formaRecebimentoEnum : TipoRecebimentoEnum.values()) {
				grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setTipoRecebimento(
						grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().concat(formaRecebimentoEnum.toString()).concat(";"));
			}
		}

		recebimento = grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().split(";");
	}
	
	public void selecionarTodosSituacao() {
		grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario("");
		if(getMarcarTodosSituacao()) {
			for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
				grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(
						grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().concat(situacaoFuncionarioEnum.toString()).concat(";"));
			}
		}

		situacao = grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().split(";");
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

	public String getCampoConsultaSecaoFolhaPagamento() {
		if (campoConsultaSecaoFolhaPagamento == null) {
			campoConsultaSecaoFolhaPagamento = "";
		}
		return campoConsultaSecaoFolhaPagamento;
	}

	public void setCampoConsultaSecaoFolhaPagamento(String campoConsultaSecaoFolhaPagamento) {
		this.campoConsultaSecaoFolhaPagamento = campoConsultaSecaoFolhaPagamento;
	}

	public String getValorConsultaSecaoFolhaPagamento() {
		if (valorConsultaSecaoFolhaPagamento == null) {
			valorConsultaSecaoFolhaPagamento = "";
		}
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

	public List<FuncionarioCargoVO> getFuncionariosCargo() {
		if (funcionariosCargo == null) {
			funcionariosCargo = new ArrayList<>();
		}
		return funcionariosCargo;
	}

	public void setFuncionariosCargo(List<FuncionarioCargoVO> funcionariosCargo) {
		this.funcionariosCargo = funcionariosCargo;
	}
}