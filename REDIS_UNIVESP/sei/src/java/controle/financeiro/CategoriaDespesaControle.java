package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas categoriaDespesaForm.jsp
 * categoriaDespesaCons.jsp) com as funcionalidades da classe <code>CategoriaDespesa</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaDespesa
 * @see CategoriaDespesaVO
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CategoriaDespesaRateioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.enumerador.TipoCentroNegocioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("CategoriaDespesaControle")
@Scope("viewScope")
@Lazy
public class CategoriaDespesaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private CategoriaDespesaVO categoriaDespesaVO;
	private CategoriaDespesaRateioVO categoriaDespesaRateioAdministrativoVO;
	private CategoriaDespesaRateioVO categoriaDespesaRateioAcademicoVO;
	protected List<SelectItem> listaSelectItemDepartamento;
	protected List<SelectItem> listaSelectItemCategoriaDespesaPrincipal;
	protected Boolean readonlyCategoriaDespesaPrincipal;

	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;

	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;
	private String mascaraPadraoCategoriaDespesa;
	private Integer tamanhoMascaraPadraoCategoriaDespesa;

	public CategoriaDespesaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>CategoriaDespesa</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Novo Categoria Despesa", "Novo");
		removerObjetoMemoria(this);
		setCategoriaDespesaVO(new CategoriaDespesaVO());
		inicializarListasSelectItemTodosComboBox();
		// this.setReadonlyDepartamento(Boolean.FALSE);
		this.setReadonlyCategoriaDespesaPrincipal(Boolean.FALSE);
		getCategoriaDespesaVO().setNivelCategoriaDespesa("UE");
		getCategoriaDespesaVO().setInformarTurma("NC");
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDespesaForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>CategoriaDespesa</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Editar Categoria Despesa", "Editando");
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItem");
		obj.setNovoObj(Boolean.FALSE);
		setCategoriaDespesaVO(obj);
		setReadonlyCategoriaDespesaPrincipal(Boolean.TRUE);
		inicializarListasSelectItemTodosComboBox();
		registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Finalizando Editar Categoria Despesa", "Editando");
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDespesaForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>CategoriaDespesa</code>. Caso o objeto seja novo (ainda
	 * não gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if (categoriaDespesaVO.isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Incluir Categoria Despesa", "Incluindo");
				getFacadeFactory().getCategoriaDespesaFacade().persistir(categoriaDespesaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Incluir Categoria Despesa", "Incluindo");
			} else {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Alterar Categoria Despesa", "Alterando");
				getFacadeFactory().getCategoriaDespesaFacade().persistir(categoriaDespesaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Alterar Categoria Despesa", "Alterando");
				getAplicacaoControle().removerCategoriaDespesa(categoriaDespesaVO.getCodigo());
			}
			setReadonlyCategoriaDespesaPrincipal(Boolean.TRUE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CategoriaDespesaCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	@SuppressWarnings("rawtypes")
	public String consultar() {
		try {
			super.consultar();
			List<CategoriaDespesaVO> objs = new ArrayList<CategoriaDespesaVO>(0);
			int valorInt = 0;
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (!getControleConsulta().getValorConsulta().equals("")) {
					valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				}
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Consultar Categoria Despesa", "Consultando");
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Finalizando Consultar Categoria Despesa", "Consultando");
			}
			if (getControleConsulta().getCampoConsulta().equals("categoriaDespesaPrincipal")) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Consultar Categoria Despesa", "Consultando");
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorNomeCategoriaDespesaPrincipal(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Finalizando Consultar Categoria Despesa", "Consultando");
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorCategoriaDespesa")) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Consultar Categoria Despesa", "Consultando");
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Finalizando Consultar Categoria Despesa", "Consultando");
			}
			if (getControleConsulta().getCampoConsulta().equals("nivelCategoriaDespesa")) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Consultar Categoria Despesa", "Consultando");
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorNivelCategoriaDespesa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Finalizando Consultar Categoria Despesa", "Consultando");
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Consultar Categoria Despesa", "Consultando");
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Finalizando Consultar Categoria Despesa", "Consultando");
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDespesaCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDespesaCons");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>CategoriaDespesaVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			if (!Uteis.isAtributoPreenchido(consultarCategoriaDespesaPrincipalPorFilho(getCategoriaDespesaVO().getCodigo()))) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Excluir Categoria Despesa", "Excluindo");
				getFacadeFactory().getCategoriaDespesaFacade().excluir(categoriaDespesaVO, getUsuarioLogado());
				setCategoriaDespesaVO(new CategoriaDespesaVO());
				setReadonlyCategoriaDespesaPrincipal(Boolean.FALSE);
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDespesaControle", "Inicializando Excluir Categoria Despesa", "Excluindo");
				setMensagemID("msg_dados_excluidos");
				return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDespesaForm");
			} else {
				setReadonlyCategoriaDespesaPrincipal(Boolean.TRUE);
				setMensagemDetalhada("msg_erro", "Existem Dependentes desta Categoria de Despesa");
				return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDespesaForm");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDespesaForm");
		}
	}

	public void addCategoriaDespesaRateioAcademico() {
		try {
			getCategoriaDespesaRateioAcademicoVO().setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum.ACADEMICO);
			getFacadeFactory().getCategoriaDespesaFacade().addCategoriaDespesaRateio(getCategoriaDespesaVO(), getCategoriaDespesaRateioAcademicoVO(), getUsuarioLogado());
			setCategoriaDespesaRateioAcademicoVO(new CategoriaDespesaRateioVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addCategoriaDespesaRateioAdministrativo() {
		try {
			getCategoriaDespesaRateioAdministrativoVO().setTipoCategoriaDespesaRateioEnum(TipoCentroNegocioEnum.ADMINISTRATIVO);
			getFacadeFactory().getCategoriaDespesaFacade().addCategoriaDespesaRateio(getCategoriaDespesaVO(), getCategoriaDespesaRateioAdministrativoVO(), getUsuarioLogado());
			setCategoriaDespesaRateioAdministrativoVO(new CategoriaDespesaRateioVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarCategoriaDespesaAcademicoRateio() {
		try {
			CategoriaDespesaRateioVO obj = (CategoriaDespesaRateioVO) context().getExternalContext().getRequestMap().get("cdrItens");
			setCategoriaDespesaRateioAcademicoVO(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void editarCategoriaDespesaAdministrativoRateio() {
		try {
			CategoriaDespesaRateioVO obj = (CategoriaDespesaRateioVO) context().getExternalContext().getRequestMap().get("cdrItens");
			setCategoriaDespesaRateioAdministrativoVO(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


	public void removeCategoriaDespesaRateio() {
		try {
			CategoriaDespesaRateioVO obj = (CategoriaDespesaRateioVO) context().getExternalContext().getRequestMap().get("cdrItens");
			getFacadeFactory().getCategoriaDespesaFacade().removeCategoriaDespesaRateio(getCategoriaDespesaVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");

			getCategoriaDespesaRateioAcademicoVO().setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() {
		try {
			getCategoriaDespesaRateioAcademicoVO().setCursoVO(new CursoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCursoAntesDaConsultar() {
		try {
			getListaConsultaCurso().clear();
			setValorConsultaCurso("");
			setCampoConsultaCurso("");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			Integer unidadeEnsino = 0;
			if (Uteis.isAtributoPreenchido(getCategoriaDespesaRateioAcademicoVO().getUnidadeEnsinoVO())) {
				unidadeEnsino = getCategoriaDespesaRateioAcademicoVO().getUnidadeEnsinoVO().getCodigo();
			} else {
				unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			}
			if (getCampoConsultaCurso().equals("codigo")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigoEUnidadeEnsino(Integer.parseInt(getValorConsultaCurso()), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome Curso"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void limparDepartamento() {
		try {
			getCategoriaDespesaRateioAdministrativoVO().setDepartamentoVO(new DepartamentoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDepartamentoAntesDaConsultar() {
		try {
			getListaConsultaCurso().clear();
			setValorConsultaCurso("");
			setCampoConsultaCurso("");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String consultarDepartamento() {
		try {
			List objs = new ArrayList(0);
			Integer unidadeEnsino = 0;
			if (Uteis.isAtributoPreenchido(getCategoriaDespesaRateioAdministrativoVO().getUnidadeEnsinoVO())) {
				unidadeEnsino = getCategoriaDespesaRateioAdministrativoVO().getUnidadeEnsinoVO().getCodigo();
			} else {
				unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			}
			if (getCampoConsultaDepartamento().equals("codigo")) {
				int valorInt = 0;
				if (!getValorConsultaDepartamento().equals("")) {
					valorInt = Integer.parseInt(getValorConsultaDepartamento());
				}
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPorUnidadeEnsino(new Integer(valorInt), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePorUnidadeEnsino(getValorConsultaDepartamento(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public void selecionarDepartamento() throws Exception {
		DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
		getCategoriaDespesaRateioAdministrativoVO().setDepartamentoVO(obj);
		
	}

	public List<SelectItem> getTipoConsultaComboDepartamento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}	
			
	public Boolean getApresentarNivelCategoriaDespesa() {
		if (getControleConsulta().getCampoConsulta().equals("nivelCategoriaDespesa")) {
			return true;
		}
		return false;
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

	public Boolean getExisteCategoriaDespesaPrincipal() {
		return Uteis.isAtributoPreenchido(getCategoriaDespesaVO()) || Uteis.isAtributoPreenchido(getCategoriaDespesaVO().getCategoriaDespesaPrincipal());
	}

	public void atualizarDadosCategoriaDespesa() {
		Integer codigo = this.getCategoriaDespesaVO().getCategoriaDespesaPrincipal();
		try {
			CategoriaDespesaVO cr = (CategoriaDespesaVO) getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(codigo, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			this.getCategoriaDespesaVO().setInformarTurma(cr.getInformarTurma());
			this.getCategoriaDespesaVO().setNivelCategoriaDespesa(cr.getNivelCategoriaDespesa());
		} catch (Exception e) {
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> getListaSelectItemTipoContaCategoriaDespesa() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable creditoDebitos = (Hashtable) Dominios.getCreditoDebito();
		Enumeration keys = creditoDebitos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) creditoDebitos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	@SuppressWarnings("rawtypes")
	public List<SelectItem> getListaSelectItemNivelCategoriaDespesa() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		Hashtable creditoDebitos = (Hashtable) Dominios.getNivelCategoriaDespesa();
		Enumeration keys = creditoDebitos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) creditoDebitos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	@SuppressWarnings("rawtypes")
	public List<SelectItem> getListaSelectItemNivelAcademico() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		Hashtable creditoDebitos = (Hashtable) Dominios.getInformarTurma();
		Enumeration keys = creditoDebitos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) creditoDebitos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				return;
			}
			List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void montarListaSelectItemCategoriaDespesaPrincipal(String prm) throws Exception {
		try {
			List<CategoriaDespesaVO> resultadoConsulta = consultarCategoriaDespesaPrincipalPorDescricao(prm);
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			for (CategoriaDespesaVO obj : resultadoConsulta) {
				if (!obj.getCodigo().equals(getCategoriaDespesaVO().getCodigo())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao() + " - " + obj.getIdentificadorCategoriaDespesa()));
				}
			}
			setListaSelectItemCategoriaDespesaPrincipal(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	public void montarListaSelectItemCategoriaDespesaPrincipal() {
		try {
			montarListaSelectItemCategoriaDespesaPrincipal("");
		} catch (Exception e) {
		}
	}

	public List<CategoriaDespesaVO> consultarCategoriaDespesaPrincipalPorFilho(Integer codigo) throws Exception {
		return getFacadeFactory().getCategoriaDespesaFacade().consultarPorCategoriaDespesaPrincipalFilho(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado());
	}

	public List<CategoriaDespesaVO> consultarCategoriaDespesaPrincipalPorDescricao(String nomePrm) throws Exception {
		return getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemCategoriaDespesaPrincipal();
		montarListaSelectItemUnidadeEnsino();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	private List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<>(0);
			tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
			tipoConsultaCombo.add(new SelectItem("identificadorCategoriaDespesa", "Identificador"));
			tipoConsultaCombo.add(new SelectItem("categoriaDespesaPrincipal", "Nome Cat. Principal"));
		}
		return tipoConsultaCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDespesaCons");
	}

	public List<SelectItem> getListaSelectItemDepartamento() {
		return (listaSelectItemDepartamento);
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public List<SelectItem> getListaSelectItemCategoriaDespesaPrincipal() {
		return listaSelectItemCategoriaDespesaPrincipal;
	}

	public void setListaSelectItemCategoriaDespesaPrincipal(List<SelectItem> listaSelectItemCategoriaDespesaPrincipal) {
		this.listaSelectItemCategoriaDespesaPrincipal = listaSelectItemCategoriaDespesaPrincipal;
	}

	public Boolean getReadonlyCategoriaDespesaPrincipal() {
		return readonlyCategoriaDespesaPrincipal;
	}

	public void setReadonlyCategoriaDespesaPrincipal(Boolean readonlyCategoriaDespesaPrincipal) {
		this.readonlyCategoriaDespesaPrincipal = readonlyCategoriaDespesaPrincipal;
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		categoriaDespesaVO = null;
		Uteis.liberarListaMemoria(listaSelectItemDepartamento);
		Uteis.liberarListaMemoria(listaSelectItemCategoriaDespesaPrincipal);
		readonlyCategoriaDespesaPrincipal = null;
	}
	

	public Boolean getApresentarBooleanoApresentarPlanoOrcamentario() throws Exception {
		return getConfiguracaoFinanceiroPadraoSistema().getUsaPlanoOrcamentario();
	}

	public CategoriaDespesaRateioVO getCategoriaDespesaRateioAdministrativoVO() {
		if (categoriaDespesaRateioAdministrativoVO == null) {
			categoriaDespesaRateioAdministrativoVO = new CategoriaDespesaRateioVO();
		}
		return categoriaDespesaRateioAdministrativoVO;
	}

	public void setCategoriaDespesaRateioAdministrativoVO(CategoriaDespesaRateioVO categoriaDespesaRateioAdministrativoVO) {
		this.categoriaDespesaRateioAdministrativoVO = categoriaDespesaRateioAdministrativoVO;
	}
	
	public CategoriaDespesaRateioVO getCategoriaDespesaRateioAcademicoVO() {
		if (categoriaDespesaRateioAcademicoVO == null) {
			categoriaDespesaRateioAcademicoVO = new CategoriaDespesaRateioVO();
		}
		return categoriaDespesaRateioAcademicoVO;
	}

	public void setCategoriaDespesaRateioAcademicoVO(CategoriaDespesaRateioVO categoriaDespesaRateioAcademicoVO) {
		this.categoriaDespesaRateioAcademicoVO = categoriaDespesaRateioAcademicoVO;
	}
	

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "nome";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultaDepartamento() {
		if (campoConsultaDepartamento == null) {
			campoConsultaDepartamento = "";
		}
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList<>(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public void limparDadosCategoriaDespesaPrincipal() {
		if (!getCategoriaDespesaVO().getIsUmaSubCategoria() && Uteis.isAtributoPreenchido(getCategoriaDespesaVO().getCategoriaDespesaPrincipal())) {
			getCategoriaDespesaVO().setCategoriaDespesaPrincipal(0);
		}
	}

	public String getMascaraPadraoCategoriaDespesa() {
		if (mascaraPadraoCategoriaDespesa == null) {
			try {
				mascaraPadraoCategoriaDespesa = getConfiguracaoFinanceiroPadraoSistema().getMascaraCategoriaDespesa().replace("x", "9");
			} catch (Exception e) {
				mascaraPadraoCategoriaDespesa = "";
			}
		}
		return mascaraPadraoCategoriaDespesa;
	}

	public Integer getTamanhoMascaraPadraoCategoriaDespesa() {
		if (tamanhoMascaraPadraoCategoriaDespesa == null) {
			tamanhoMascaraPadraoCategoriaDespesa = getMascaraPadraoCategoriaDespesa().length();
		}
		return tamanhoMascaraPadraoCategoriaDespesa;
	}
}
