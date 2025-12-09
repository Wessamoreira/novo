package controle.contabil;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas planoContaForm.jsp
 * planoContaCons.jsp) com as funcionalidades da classe <code>PlanoConta</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see PlanoConta
 * @see PlanoContaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("PlanoContaControle")
@Scope("viewScope")
@Lazy
public class PlanoContaControle extends SuperControleRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3222377729679316295L;
	private PlanoContaVO planoContaVO;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	protected List<SelectItem> listaSelectItemPlanoContaPrincipal;
	protected List<SelectItem> listaSelectItemTipoPlanoConta;
	private TreeNodeCustomizado arvorePlanoConta;

	public PlanoContaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>PlanoConta</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		PlanoContaVO planoPrincipal = null;
		if (Uteis.isAtributoPreenchido(getPlanoContaVO().getPlanoContaPrincipal())) {
			planoPrincipal = getPlanoContaVO().getPlanoContaPrincipal();
		}
		setPlanoContaVO(new PlanoContaVO());
		getPlanoContaVO().setPlanoContaPrincipal(planoPrincipal);
		inicializarUnidadeEnsinoUsuarioLogadoSistema();
		inicializarListasSelectItemTodosComboBox();
		atualizarTipoPlanoConta();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoContaForm.xhtml");
	}

	public void inicializarUnidadeEnsinoUsuarioLogadoSistema() {
		try {
			planoContaVO.getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			planoContaVO.getUnidadeEnsino().setNome(getUnidadeEnsinoLogado().getNome());
		} catch (Exception e) {

		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>PlanoConta</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		TreeNodeCustomizado node = (TreeNodeCustomizado) context().getExternalContext().getRequestMap().get("item");
		setPlanoContaVO((PlanoContaVO) node.getData());
		getPlanoContaVO().setNovoObj(false);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoContaForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>PlanoConta</code>. Caso o objeto seja novo (ainda não gravado no
	 * BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
	 * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de
	 * erro.
	 */
	public void persistir() {
		try {
			if (planoContaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPlanoContaFacade().incluir(planoContaVO, getUsuarioLogado(),
						getConfiguracaoFinanceiroPadraoSistema());
			} else {
				inicializarUnidadeEnsinoUsuarioLogadoSistema();
				getFacadeFactory().getPlanoContaFacade().alterar(planoContaVO, getUsuarioLogado());
			}
			montarListaSelectItemPlanoContaPrincipal();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public void consultarDados() {
		try {
			super.consultar();
			setListaConsulta(getFacadeFactory().getPlanoContaFacade().consultar(getUnidadeEnsinoLogado().getCodigo(),
					getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), true,
					getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			getListaConsulta().clear();
			;
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPlanoContaFacade().excluir(planoContaVO, getUsuarioLogado());
			setPlanoContaVO(new PlanoContaVO());
			inicializarUnidadeEnsinoUsuarioLogadoSistema();
			montarListaSelectItemPlanoContaPrincipal();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoContaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoContaForm");
		}
	}

	public void irPaginaInicial() throws Exception {
		// controleConsulta.setPaginaAtual(1);
		consultarPlanoContaArvoreCompleta();
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

	public void atualizarTipoPlanoConta() {
		try {
			if (Uteis.isAtributoPreenchido(getPlanoContaVO().getPlanoContaPrincipal())) {
				getPlanoContaVO().setPlanoContaPrincipal(getFacadeFactory().getPlanoContaFacade()
						.consultarPorChavePrimaria(getPlanoContaVO().getPlanoContaPrincipal().getCodigo(),
								getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
				getPlanoContaVO()
						.setNivelPlanoConta(getPlanoContaVO().getPlanoContaPrincipal().getNivelPlanoConta() + 1);
				getPlanoContaVO().getUnidadeEnsino()
						.setCodigo(getPlanoContaVO().getPlanoContaPrincipal().getUnidadeEnsino().getCodigo());
				getPlanoContaVO().getUnidadeEnsino()
						.setNome(getPlanoContaVO().getPlanoContaPrincipal().getUnidadeEnsino().getNome());
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>tipoPlanoConta</code>
	 */
	public List<SelectItem> getListaSelectItemTipoPlanoConta() {
		if (listaSelectItemTipoPlanoConta == null) {
			listaSelectItemTipoPlanoConta = new ArrayList<SelectItem>(0);
			listaSelectItemTipoPlanoConta
					.add(new SelectItem(TipoPlanoContaEnum.CREDITO, TipoPlanoContaEnum.CREDITO.getValorApresentar()));
			listaSelectItemTipoPlanoConta
					.add(new SelectItem(TipoPlanoContaEnum.DEBITO, TipoPlanoContaEnum.DEBITO.getValorApresentar()));
		}
		return listaSelectItemTipoPlanoConta;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Banco</code>.
	 */
	private void montarListaSelectItemPlanoContaPrincipal(String prm) throws Exception {
		List<PlanoContaVO> resultadoConsulta = null;
		Iterator<PlanoContaVO> i = null;
		try {
			resultadoConsulta = consultarPlanoContaPrincipalPorDescricao(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PlanoContaVO obj = (PlanoContaVO) i.next();
				objs.add(
						new SelectItem(obj.getCodigo(), obj.getIdentificadorPlanoConta() + " - " + obj.getDescricao()));
			}
			setListaSelectItemPlanoContaPrincipal(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Banco</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Banco</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemPlanoContaPrincipal() {
		try {
			montarListaSelectItemPlanoContaPrincipal("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List<PlanoContaVO> consultarPlanoContaPrincipalPorDescricao(String nomePrm) throws Exception {
		List<PlanoContaVO> lista = getFacadeFactory().getPlanoContaFacade().consultarPorDescricaoOrdenarIdentrificador(
				nomePrm, getPlanoContaVO().getUnidadeEnsino().getCodigo(), getPlanoContaVO().getCodigo(), false,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela
	 * para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
				super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores
	 * (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemPlanoContaPrincipal();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("identificadorPlanoConta", "Identificador do Plano de Contas"));
			tipoConsultaCombo.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
			tipoConsultaCombo.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaCombo.add(new SelectItem("planoContaPrincipal", "Plano Conta Principal"));
			tipoConsultaCombo.add(new SelectItem("codigoReduzido", "Código Reduzido"));
		}
		return tipoConsultaCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		consultarPlanoContaArvoreCompleta();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoContaCons");
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public PlanoContaVO getPlanoContaVO() {
		if (planoContaVO == null) {
			planoContaVO = new PlanoContaVO();
		}
		return planoContaVO;
	}

	public void setPlanoContaVO(PlanoContaVO planoContaVO) {
		this.planoContaVO = planoContaVO;
	}

	public List<SelectItem> getListaSelectItemPlanoContaPrincipal() {
		if (listaSelectItemPlanoContaPrincipal == null) {
			listaSelectItemPlanoContaPrincipal = new ArrayList<SelectItem>();
		}
		return listaSelectItemPlanoContaPrincipal;
	}

	public void setListaSelectItemPlanoContaPrincipal(List<SelectItem> listaSelectItemPlanoContaPrincipal) {
		this.listaSelectItemPlanoContaPrincipal = listaSelectItemPlanoContaPrincipal;
	}

	public void setListaSelectItemTipoPlanoConta(List<SelectItem> listaSelectItemTipoPlanoConta) {
		this.listaSelectItemTipoPlanoConta = listaSelectItemTipoPlanoConta;
	}

	public void consultarPlanoContaSuperiorCons() {
		editar();
		consultarPlanoContaSuperior();
	}

	public void consultarPlanoContaInferiorCons() {
		editar();
		consultarPlanoContaSuperior();
	}

	public void consultarPlanoContaSuperior() {
		try {
			getPlanoContaVO().setArvorePlanoConta(getFacadeFactory().getPlanoContaFacade()
					.consultarArvorePlanoContaSuperior(getPlanoContaVO(), false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarPlanoContaInferior() {
		try {
			getPlanoContaVO().setArvorePlanoConta(getFacadeFactory().getPlanoContaFacade()
					.consultarArvorePlanoContaInferior(getPlanoContaVO(), false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public TreeNodeCustomizado getArvorePlanoConta() {
		if (arvorePlanoConta == null) {
			arvorePlanoConta = new TreeNodeCustomizado();
		}
		return arvorePlanoConta;
	}

	public void setArvorePlanoConta(TreeNodeCustomizado arvorePlanoConta) {
		this.arvorePlanoConta = arvorePlanoConta;
	}

	@PostConstruct
	public void consultarPlanoContaArvoreCompleta() {
		try {
			setArvorePlanoConta(getFacadeFactory().getPlanoContaFacade().consultarArvorePlanoContaInferior(null, false,
					getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>PlanoConta</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String novoPlanoContaFilho() {
		try {
			TreeNodeCustomizado node = (TreeNodeCustomizado) context().getExternalContext().getRequestMap().get("item");
			novo();
			getPlanoContaVO().setPlanoContaPrincipal(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(
					((PlanoContaVO) node.getData()).getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getPlanoContaVO().setUnidadeEnsino(getPlanoContaVO().getPlanoContaPrincipal().getUnidadeEnsino());
			getPlanoContaVO().setNivelPlanoConta(getPlanoContaVO().getPlanoContaPrincipal().getNivelPlanoConta() + 1);
			getPlanoContaVO().setIdentificadorPlanoConta(getFacadeFactory().getPlanoContaFacade().consultarPosFixoSugestaoPlanoConta(getPlanoContaVO().getPlanoContaPrincipal().getIdentificadorPlanoConta()+".", getPlanoContaVO().getPlanoContaPrincipal().getNivelPlanoConta() + 1));
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoContaForm");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoContaCons");
		}
	}
	

	public void imprimirRelatorioExcel() {			

        try {
        	
        	registrarAtividadeUsuario(getUsuarioLogado(), "PlanoContaControle", "Inicializando Geração de Relatório Plano Conta"  + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
        	TreeNodeCustomizado treeNodeCustomizado = getFacadeFactory().getPlanoContaFacade().consultarArvorePlanoContaInferior(null, true, getUsuarioLogado());
            
        	if (!treeNodeCustomizado.getListaObjetos().isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPlanoContaFacade().designIReportRelatorioExcel());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Plano de Contas");
                getSuperParametroRelVO().setListaObjetos(treeNodeCustomizado.getListaObjetos());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getPlanoContaFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

                realizarImpressaoRelatorio();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {

        }
	}

}
