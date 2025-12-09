package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas cfgCustoAdministrativoForm.xhtml
 * cfgCustoAdministrativoCons.xhtml) com as funcionalidades da classe <code>CfgCustoAdministrativo</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CfgCustoAdministrativo
 * @see CfgCustoAdministrativoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.ConfiguracaoControleInterface;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.basico.ConfiguracoesControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.financeiro.CfgCustoAdministrativoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("CfgCustoAdministrativoControle")
@Scope("viewScope")
@Lazy
public class CfgCustoAdministrativoControle extends SuperControle implements ConfiguracaoControleInterface, Serializable {

	private static final long serialVersionUID = 516775542898275067L;
	private CfgCustoAdministrativoVO cfgCustoAdministrativoVO;
	private String responsavel_Erro;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	private String curso_Erro;
	private List<SelectItem> listaConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private ConfiguracoesControle configuracoesControle;
	private ConfiguracoesVO configuracoesVO;
	private Boolean editandoAPartirFormConfiguracores;

	public CfgCustoAdministrativoControle() throws Exception {
		// obterUsuarioLogado();
		novo();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}
	
	/**
	 * @author Geber
	 * 
	 * Método para recuperar a configuração criada em configuracoesForm.xhtml, pelo fato do ConfiguracaoAcademicoControle ser instanciado por outro controle (ConfiguraçõesControle)
	 * foi necessário criar essa função, pois após a navegação da página o esse controlador era instanciado e os dados perdidos. 
	 * Após recuperar o objeto da sessão, é setado no atributo configuraçõesVO e removido da sessão.
	 * 
	 */
	@PostConstruct
	public void recuperarConfiguracaoPeloForm(){
		if(context().getExternalContext().getSessionMap().get("configuracoesItem") != null) {
			setConfiguracoesVO((ConfiguracoesVO) context().getExternalContext().getSessionMap().get("configuracoesItem"));
			context().getExternalContext().getSessionMap().remove("configuracoesItem");
			editar();
		}	
	}

	public void iniciarControleConfiguracao(ConfiguracoesVO configuracoesVO, ConfiguracoesControle configuracoesControle) throws Exception {
		setConfiguracoesControle(configuracoesControle);
		// setCfgCustoAdministrativoVO(getFacadeFactory().getCfgCustoAdministrativoFacade().consultarPorCodigoConfiguracoes(configuracoesVO.getCodigo(),
		// false));
		setCfgCustoAdministrativoVO(getFacadeFactory().getCfgCustoAdministrativoFacade().consultaRapidaConfiguracaoASerUsada(configuracoesVO.getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
		if (configuracoesVO.getCodigo().intValue() == 0) {
			novo();
		} else if(!Uteis.isAtributoPreenchido(getCfgCustoAdministrativoVO())) {
			novo();
			getCfgCustoAdministrativoVO().setConfiguracoesVO(configuracoesVO);
		}
	}

	public void limparCamposParaClone() {
		getCfgCustoAdministrativoVO().setCodigo(0);
		getCfgCustoAdministrativoVO().setNovoObj(true);
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>CfgCustoAdministrativo</code> para edição pelo usuário da
	 * aplicação.
	 */
	public void novo() {
		setCfgCustoAdministrativoVO(new CfgCustoAdministrativoVO());
		inicializarListasSelectItemTodosComboBox();
		inicializarResponsavel();
	}

	public void inicializarResponsavel() {
		try {
			getCfgCustoAdministrativoVO().setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}
	
	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>CfgCustoAdministrativo</code> para alteração. O objeto desta classe
	 * é disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {			
			setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(getConfiguracoesVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));			
			CfgCustoAdministrativoVO cfgObj = getFacadeFactory().getCfgCustoAdministrativoFacade().consultarPorCodigoConfiguracoes(getConfiguracoesVO().getCodigo(), false, getUsuarioLogado());
			if(!Uteis.isAtributoPreenchido(cfgObj)) {
				cfgObj.setConfiguracoesVO(getConfiguracoesVO());
			}			
			setCfgCustoAdministrativoVO(cfgObj);
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("cfgCustoAdministrativoForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>CfgCustoAdministrativo</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar(ConfiguracoesVO configuracoesVO) throws Exception {
		getCfgCustoAdministrativoVO().setConfiguracoesVO(configuracoesVO);
		if (cfgCustoAdministrativoVO.isNovoObj().booleanValue()) {
			getFacadeFactory().getCfgCustoAdministrativoFacade().incluir(cfgCustoAdministrativoVO, getUsuarioLogado());
		}
	}

	public String gravar() {
		try {
			if (cfgCustoAdministrativoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getCfgCustoAdministrativoFacade().incluir(cfgCustoAdministrativoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getCfgCustoAdministrativoFacade().alterar(cfgCustoAdministrativoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
//			return "editar";
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return "editar";
			return "";
		}
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
		getCfgCustoAdministrativoVO().setCurso(obj);
		listaConsultaCurso.clear();
		this.setValorConsultaCurso("");
		this.setCampoConsultaCurso("");
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CfgCustoAdministrativoCons.jsp. Define o tipo de consulta a ser
	 * executada, por meio de ComboBox denominado campoConsulta, disponivel
	 * neste mesmo JSP. Como resultado, disponibiliza um List com os objetos
	 * selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCfgCustoAdministrativoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				objs = getFacadeFactory().getCfgCustoAdministrativoFacade().consultarPorData(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCfgCustoAdministrativoFacade().consultarPorCodigoFuncionario(new Integer(valorInt), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getCfgCustoAdministrativoFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoCusto")) {
				objs = getFacadeFactory().getCfgCustoAdministrativoFacade().consultarPorTipoCusto(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
//			return "consultar";
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return "consultar";
			return "";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>CfgCustoAdministrativoVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 * 
	 * @return
	 */
	public String excluir() throws Exception {
		getFacadeFactory().getCfgCustoAdministrativoFacade().excluir(cfgCustoAdministrativoVO, getUsuarioLogado());
		setCfgCustoAdministrativoVO(new CfgCustoAdministrativoVO());
		inicializarResponsavel();
//		return "editar";
		return "";
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

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoCusto</code>
	 */
	public List<SelectItem> getListaSelectItemTipoCustoCfgCustoAdministrativo() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoCustoCFGCustoAdministrativos = (Hashtable) Dominios.getTipoCustoCFGCustoAdministrativo();
		Enumeration keys = tipoCustoCFGCustoAdministrativos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoCustoCFGCustoAdministrativos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Curso</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarCursoPorChavePrimaria() {
		try {
			Integer campoConsulta = cfgCustoAdministrativoVO.getCurso().getCodigo();
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			cfgCustoAdministrativoVO.getCurso().setNome(curso.getNome());
			this.setCurso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			cfgCustoAdministrativoVO.getCurso().setNome("");
			cfgCustoAdministrativoVO.getCurso().setCodigo(0);
			this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("tipoCusto", "Tipo de Custo"));
		return itens;
	}

	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public void inicializarConsultar() {
		removerObjetoMemoria(this);
		// setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		// definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
//		return "consultar";
	}

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getResponsavel_Erro() {
		return responsavel_Erro;
	}

	public void setResponsavel_Erro(String responsavel_Erro) {
		this.responsavel_Erro = responsavel_Erro;
	}

	public CfgCustoAdministrativoVO getCfgCustoAdministrativoVO() {
		return cfgCustoAdministrativoVO;
	}

	public void setCfgCustoAdministrativoVO(CfgCustoAdministrativoVO cfgCustoAdministrativoVO) {
		this.cfgCustoAdministrativoVO = cfgCustoAdministrativoVO;
	}

	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List getListaConsultaCurso() {
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		cfgCustoAdministrativoVO = null;
		responsavel_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		curso_Erro = null;
	}

	/**
	 * @return the configuracoesControle
	 */
	public ConfiguracoesControle getConfiguracoesControle() {
		return configuracoesControle;
	}

	/**
	 * @param configuracoesControle
	 *            the configuracoesControle to set
	 */
	public void setConfiguracoesControle(ConfiguracoesControle configuracoesControle) {
		this.configuracoesControle = configuracoesControle;
	}

	/**
	 * @return the configuracoesVO
	 */
	public ConfiguracoesVO getConfiguracoesVO() {
		return configuracoesVO;
	}

	/**
	 * @param configuracoesVO
	 *            the configuracoesVO to set
	 */
	public void setConfiguracoesVO(ConfiguracoesVO configuracoesVO) {
		this.configuracoesVO = configuracoesVO;
	}

	/**
	 * @return the editandoAPartirFormConfiguracores
	 */
	public Boolean getEditandoAPartirFormConfiguracores() {
		if (editandoAPartirFormConfiguracores == null) {
			return Boolean.FALSE;
		}
		return editandoAPartirFormConfiguracores;
	}

	/**
	 * @param editandoAPartirFormConfiguracores
	 *            the editandoAPartirFormConfiguracores to set
	 */
	public void setEditandoAPartirFormConfiguracores(Boolean editandoAPartirFormConfiguracores) {
		this.editandoAPartirFormConfiguracores = editandoAPartirFormConfiguracores;
	}
}
