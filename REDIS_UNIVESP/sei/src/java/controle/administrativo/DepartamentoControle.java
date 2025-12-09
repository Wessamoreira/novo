package controle.administrativo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * departamentoForm.jsp departamentoCons.jsp) com as funcionalidades da classe <code>Departamento</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Departamento
 * @see DepartamentoVO
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("DepartamentoControle")
@Scope("viewScope")
public class DepartamentoControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private DepartamentoVO departamentoVO;
	private List<SelectItem> listaSelectItemDepartamentoSuperior;
	private String matricula;
	private String valorConsultaResponsavel;
	private String campoConsultaResponsavel;
	private List<FuncionarioVO> listaConsultaResponsavel;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemConfiguracaoLdap;

	public DepartamentoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Departamento</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setDepartamentoVO(new DepartamentoVO());
		inicializarListasSelectItemTodosComboBox();
		setMatricula("");
		setCampoConsultaResponsavel("");
		setValorConsultaResponsavel("");
		setListaConsultaResponsavel(new ArrayList(0));
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("departamentoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Departamento</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItem");
		obj = montarDadosDepartamentoVOCompleto(obj);
		FuncionarioVO funcionario = consultarFunionario(obj.getResponsavel().getCodigo());
		setMatricula(funcionario.getMatricula());
		obj.setNovoObj(Boolean.FALSE);
		setCampoConsultaResponsavel("");
		setValorConsultaResponsavel("");
		setListaConsultaResponsavel(new ArrayList(0));
		setDepartamentoVO(obj);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("departamentoForm");
	}

	/**
	 * Metodo responsavel por montar os dados do departamentoVO para edicao
	 * 
	 * @param obj
	 * @return
	 */
	public DepartamentoVO montarDadosDepartamentoVOCompleto(DepartamentoVO obj) {
		try {
			return getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new DepartamentoVO();
	}

	public FuncionarioVO consultarFunionario(Integer codigo) {
		try {
			return getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(codigo, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			return new FuncionarioVO();
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Departamento</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if (departamentoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getDepartamentoFacade().incluir(departamentoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getDepartamentoFacade().alterar(departamentoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * DepartamentoCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			List<DepartamentoVO> objs = new ArrayList<DepartamentoVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				int valorInt = 0;
				if (!getControleConsulta().getValorConsulta().equals("")) {
					valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				}
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	@SuppressWarnings("rawtypes")
	public void consultarResponsavel() {
		try {
			super.consultar();
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaResponsavel().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaResponsavel(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavel().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaResponsavel(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaResponsavel(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarResponsavel() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		getDepartamentoVO().setResponsavel(obj.getPessoa());
		setMatricula(obj.getMatricula());
		obj = null;
		setCampoConsultaResponsavel("");
		setValorConsultaResponsavel("");
		listaConsultaResponsavel.clear();

	}

	public void consultarFuncionarioPorMatricula() {
		try {
			if (!matricula.equals("")) {
				FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultarFuncionarioPorMatricula(matricula, "FU", null, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				this.departamentoVO.setResponsavel(obj.getPessoa());
				this.matricula = obj.getMatricula();
			} else {
				matricula = "";
				departamentoVO.setResponsavel(new PessoaVO());
				return;
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			matricula = "";
			departamentoVO.setResponsavel(new PessoaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>DepartamentoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getDepartamentoFacade().excluir(departamentoVO, getUsuarioLogado());
			setMatricula("");
			setDepartamentoVO(new DepartamentoVO());
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
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
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DepartamentoSuperior</code>.
	 */
	public void montarListaSelectItemDepartamentoSuperior(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getDepartamentoFacade().consultarPorDiferenteDepartamentoSuperior(getDepartamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			List objs = new ArrayList(0);
			DepartamentoVO deptoExistente = new DepartamentoVO();
			objs.add(new SelectItem(0, ""));
			if (getDepartamentoVO().getCodigo().intValue() != 0) {
				deptoExistente = getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(getDepartamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (resultadoConsulta != null) {
				i = resultadoConsulta.iterator();
				while (i.hasNext()) {
					DepartamentoVO obj = (DepartamentoVO) i.next();
					if (obj.getCodigo().intValue() != deptoExistente.getCodigo().intValue()) {
						objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
					}
				}
			}
			setListaSelectItemDepartamentoSuperior(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>DepartamentoSuperior</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Departamento</code>. Esta rotina não
	 * recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemDepartamentoSuperior() {
		try {
			montarListaSelectItemDepartamentoSuperior("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<DepartamentoVO> consultarDepartamentoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, "(TODAS AS UNIDADES)"));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}
	
	public void montarListaSelectItemConfiguracaoLdap()  {
		getListaSelectItemConfiguracaoLdap().clear();
		try {
			getListaSelectItemConfiguracaoLdap().add(new SelectItem(0, ""));
			getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdaps()
					.stream()
					.map(q -> new SelectItem(q.getCodigo(), q.getDominio()))
					.forEach(getListaSelectItemConfiguracaoLdap()::add);
			Collections.sort(getListaSelectItemConfiguracaoLdap(), new SelectItemOrdemValor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemDepartamentoSuperior();
		montarListaSelectItemConfiguracaoLdap();
	}
	
	public List<SelectItem> getListaSelectItemConfiguracaoLdap() {
		if (listaSelectItemConfiguracaoLdap == null) {
			listaSelectItemConfiguracaoLdap = new ArrayList<>();
		}
		return listaSelectItemConfiguracaoLdap;
	}

	public void setListaSelectItemConfiguracaoLdap(List<SelectItem> listaSelectItemConfiguracaoLdap) {
		this.listaSelectItemConfiguracaoLdap = listaSelectItemConfiguracaoLdap;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nomePessoa", "Responsável"));
		return itens;
	}

	public List getTipoConsultaComboResponsavel() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("departamentoCons");
	}
	
	public void limparCentroResultado() {
		try {
			getDepartamentoVO().setCentroResultadoVO(new CentroResultadoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			getDepartamentoVO().setCentroResultadoVO(obj);
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarCentroResultado() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, false, null, null, null, getControleConsultaOtimizado());			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getListaSelectItemDepartamentoSuperior() {
		return (listaSelectItemDepartamentoSuperior);
	}

	public void setListaSelectItemDepartamentoSuperior(List listaSelectItemDepartamentoSuperior) {
		this.listaSelectItemDepartamentoSuperior = listaSelectItemDepartamentoSuperior;
	}

	public DepartamentoVO getDepartamentoVO() {
		if (departamentoVO == null) {
			departamentoVO = new DepartamentoVO();
		}
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCampoConsultaResponsavel() {
		return campoConsultaResponsavel;
	}

	public void setCampoConsultaResponsavel(String campoConsultaResponsavel) {
		this.campoConsultaResponsavel = campoConsultaResponsavel;
	}

	public List<FuncionarioVO> getListaConsultaResponsavel() {
		return listaConsultaResponsavel;
	}

	public void setListaConsultaResponsavel(List<FuncionarioVO> listaConsultaResponsavel) {
		this.listaConsultaResponsavel = listaConsultaResponsavel;
	}

	public String getValorConsultaResponsavel() {
		return valorConsultaResponsavel;
	}

	public void setValorConsultaResponsavel(String valorConsultaResponsavel) {
		this.valorConsultaResponsavel = valorConsultaResponsavel;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		departamentoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemDepartamentoSuperior);
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		matricula = null;
	}
}
