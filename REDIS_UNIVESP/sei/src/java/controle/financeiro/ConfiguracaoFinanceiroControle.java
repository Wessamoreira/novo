package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas configuracaoFinanceiroForm.jsp
 * configuracaoFinanceiroCons.jsp) com as funcionalidades da classe <code>ConfiguracaoFinanceiro</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ConfiguracaoFinanceiro
 * @see ConfiguracaoFinanceiroVO
 */
 import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.ConfiguracaoControleInterface;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import controle.basico.ConfiguracoesControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.contabil.HistoricoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoRecebimentoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.DadosEnvioContaMundipagg;
import negocio.comuns.financeiro.IndiceReajusteVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.TaxaOperacaoCartaoVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.GerenciadorSplitTransacaoMundiPagg;

@Controller("ConfiguracaoFinanceiroControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings("unchecked")
public class ConfiguracaoFinanceiroControle extends SuperControle implements ConfiguracaoControleInterface, Serializable {

	private static final long serialVersionUID = -6746780264737697076L;

	private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
	private List<SelectItem> listaSelectItemFormaPagamento;
	private List<SelectItem> listaSelectItemCategoriaDespesa;
	protected List<SelectItem> listaSelectItemDescontoProgressivoPadrao;
	protected List<SelectItem> listaSelectItemCentroReceita;
	protected List<SelectItem> listaSelectItemContaCorrente;
	protected List<SelectItem> listaSelectItemBanco;
	private List<SelectItem> listaSelectItemModeloBoleto;
	private List<SelectItem> listaSelectItemDepartamentoPadraoAntecipacaoCheque;
	private List<SelectItem> listaSelectItemPlanoConta;
	private List<SelectItem> listaSelectItemHistoricoContabil;
	private ConfiguracoesControle configuracoesControle;
	private ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO;
	private TaxaOperacaoCartaoVO taxaOperacaoCartaoVO;
	private List<SelectItem> listaSelectItemOperadoraCartao;
	private List<SelectItem> listaSelectItemGrupoDestinatario;
	private List<SelectItem> listaSelectItemIndiceReajuste;
	private ConfiguracoesVO configuracoesVO;
	private Boolean editandoAPartirFormConfiguracores;
	private Boolean apresentarTipoEnvioInadimplencia;
	private DataModelo centroResultadoDataModelo;
	
	private DataModelo consultaCategoriaDespesa;
	private DataModelo consultaFormaPagamento;
	private Boolean marcarTodosTipoOrigemEnvioInadimplencia;
	private List<SelectItem> listaSelectFiltroPadraoContaReceberVisaoAluno;

	public ConfiguracaoFinanceiroControle() throws Exception {
		// obterUsuarioLogado();
		// List listaResultado =
		// getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigo(0,
		// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		// if (listaResultado.equals(new ArrayList(0))) {
		novo();
		setMensagemID("msg_entre_prmconsulta");
		setControleConsulta(new ControleConsulta());
		// } else {
		// editar(listaResultado);
		// }
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

	public boolean getIsConfiguracaoDeCategoriaDeDespesasAlteraveis() throws Exception {
		return (!getFacadeFactory().getCategoriaDespesaFacade().consultarSeExisteCategoriaDespesa());
	}

	public void iniciarControleConfiguracao(ConfiguracoesVO configuracoesVO, ConfiguracoesControle configuracoesControle) throws Exception {
		setConfiguracoesControle(configuracoesControle);
		// setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigoConfiguracoes(configuracoesVO.getCodigo(),
		// false));
		setConfiguracaoFinanceiroVO(getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigoConfiguracoes(configuracoesVO.getCodigo(), false, getUsuarioLogado()));
		if (configuracoesVO.getCodigo().intValue() == 0) {
			novo();
		}
	}

	public void limparCamposParaClone() {
		getConfiguracaoFinanceiroVO().setCodigo(0);
		getConfiguracaoFinanceiroVO().setNovoObj(true);
		getConfiguracaoFinanceiroVO().getListaConfiguracaoFinanceiroCartaoVO().forEach(c -> {
			c.setCodigo(0);
			c.setNovoObj(true);
			c.setConfiguracaoFinanceiroVO(getConfiguracaoFinanceiroVO());
		});
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ConfiguracaoFinanceiro</code> para edição pelo usuário da
	 * aplicação.
	 */
	public void novo() throws Exception {
		List<ConfiguracaoFinanceiroVO> lista = consultarConfiguracaoFinanceiro(0);
		setConfiguracaoFinanceiroVO(new ConfiguracaoFinanceiroVO());
		inicializarListasSelectItemTodosComboBox();
	}

	public List<ConfiguracaoFinanceiroVO> consultarConfiguracaoFinanceiro(Integer prm) throws Exception {
		return getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigo(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public String editar() {
		try {			
			setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(getConfiguracoesVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));				
			ConfiguracaoFinanceiroVO cfgFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigoConfiguracoes(getConfiguracoesVO().getCodigo(), false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS);
			cfgFinanceiro.setConfiguracoesVO(getConfiguracoesVO());			
			setConfiguracaoFinanceiroVO(cfgFinanceiro);
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoFinanceiroForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ConfiguracaoFinanceiro</code> para alteração. O objeto desta classe
	 * é disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar(List listaResultado) {
		Iterator i = listaResultado.iterator();
		while (i.hasNext()) {
			ConfiguracaoFinanceiroVO obj = (ConfiguracaoFinanceiroVO) i.next();
			obj.setNovoObj(Boolean.FALSE);
			setConfiguracaoFinanceiroVO(obj);
		}
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoFinanceiroForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>ConfiguracaoFinanceiro</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar(ConfiguracoesVO configuracoesVO) throws Exception {
		getConfiguracaoFinanceiroVO().setConfiguracoesVO(configuracoesVO);
		if (getConfiguracaoFinanceiroVO().isNovoObj().booleanValue()) {
			getFacadeFactory().getConfiguracaoFinanceiroFacade().incluir(getConfiguracaoFinanceiroVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
		}
	}

	public String gravar() {
		try {
			// getFacadeFactory().getConfiguracoesFacade().alterarSomenteConfiguracores(getConfiguracaoFinanceiroVO().getConfiguracoesVO());
			if (getConfiguracaoFinanceiroVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getConfiguracaoFinanceiroFacade().incluir(getConfiguracaoFinanceiroVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			} else {
				getFacadeFactory().getConfiguracaoFinanceiroFacade().alterar(getConfiguracaoFinanceiroVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());				
				getAplicacaoControle().removerConfiguracaoFinanceiraEmNivelAplicacao(getConfiguracaoFinanceiroVO());
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ConfiguracaoFinanceiroCons.jsp. Define o tipo de consulta a ser
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
				objs = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ConfiguracaoFinanceiroVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() throws Exception {
		getFacadeFactory().getConfiguracaoFinanceiroFacade().excluir(configuracaoFinanceiroVO, getUsuarioLogado());
		setConfiguracaoFinanceiroVO(new ConfiguracaoFinanceiroVO());
		return "editar";
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

	public void montarListaSelectItemModeloBoleto() {
		try {
			List modeloBoletoVOs = getFacadeFactory().getModeloBoletoFacade().listarTodos(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			List<SelectItem> lista = UtilSelectItem.getListaSelectItem(modeloBoletoVOs, "codigo", "nome");
			setListaSelectItemModeloBoleto(lista);
		} catch (Exception e) {
			getConfiguracoesControle().setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoCalculoJuro</code>
	 */
	public List<SelectItem> getListaSelectItemTipoCalculoJuroConfiguracaoFinanceiro() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable configuracaoFinanceiroTipoCalcJuross = (Hashtable) Dominios.getConfiguracaoFinanceiroTipoCalcJuros();
		Enumeration keys = configuracaoFinanceiroTipoCalcJuross.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) configuracaoFinanceiroTipoCalcJuross.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		List resultadoConsulta = consultarFormaPagamentoPorNome(prm);
		setListaSelectItemFormaPagamento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
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
	public List consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemDepartamentoPadraoAntecipacaoCheque(String prm) throws Exception {
		List resultadoConsulta = consultarDepartamentoPorNome(prm);
		setListaSelectItemDepartamentoPadraoAntecipacaoCheque(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDepartamentoPadraoAntecipacaoCheque() {
		try {
			montarListaSelectItemDepartamentoPadraoAntecipacaoCheque("");
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
	public List consultarDepartamentoPorNome(String nomePrm) throws Exception {
		try {
			List lista = getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			return lista;
		} catch (Exception e) {
			return new ArrayList(0);
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemCategoriaDespesa(String prm) throws Exception {
		List resultadoConsulta = consultarCategoriaDespesaPorDescricao(prm);
		setListaSelectItemCategoriaDespesa(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCategoriaDespesa() {
		try {
			montarListaSelectItemCategoriaDespesa("");
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
	public List consultarCategoriaDespesaPorDescricao(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DescontoProgressivoPadrao</code>.
	 */
	public void montarListaSelectItemDescontoProgressivoPadrao(String prm) throws Exception {
		List resultadoConsulta = consultarDescontoProgressivoPorNome(prm);
		setListaSelectItemDescontoProgressivoPadrao(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>DescontoProgressivoPadrao</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>DescontoProgressivo</code>. Esta rotina
	 * não recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemDescontoProgressivoPadrao() {
		try {
			montarListaSelectItemDescontoProgressivoPadrao("");
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
	public List consultarDescontoProgressivoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCentroReceita(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCentroReceitaPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CentroReceitaVO obj = (CentroReceitaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString() + "-" + obj.getIdentificadorCentroReceita().toString()));
			}
			setListaSelectItemCentroReceita(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemCentroReceita() {
		try {
			montarListaSelectItemCentroReceita("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarCentroReceitaPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCentroReceitaFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarContaCorrentePorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
                	objs.add(new SelectItem(obj.getCodigo(),obj.getNomeApresentacaoSistema()));
                }else{    				    					
   					objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoCompletaConta()));    				
                }
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarContaCorrentePorNome(String nomePrm) throws Exception {
		return  getFacadeFactory().getContaCorrenteFacade().consultarRapidaContaCorrentePorTipo(false, false, 0, getUsuarioLogado());		
	}

	public void montarListaSelectItemOperadoraCartao(String prm) throws Exception {
		List<OperadoraCartaoVO> resultadoConsulta = consultarOperadoraCartaoPorNome(prm);
		getListaSelectItemOperadoraCartao().clear();
		for (OperadoraCartaoVO obj : resultadoConsulta) {
//			if(obj.getTipo().equals("CARTAO_CREDITO")) {
				getListaSelectItemOperadoraCartao().add(new SelectItem(obj.getCodigo(), obj.getNome() +" - "+ obj.getOperadoraCartaoCreditoApresentar()));				
//			} else {
//				getListaSelectItemOperadoraCartao().add(new SelectItem(obj.getCodigo(), obj.getNome()));				
//			}
		}
	}

	public void montarListaSelectItemOperadoraCartao() {
		try {
			montarListaSelectItemOperadoraCartao("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarOperadoraCartaoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getOperadoraCartaoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemPlanoConta(String prm) throws Exception {
		List<PlanoContaVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPlanoContaPorCodigo(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PlanoContaVO obj = (PlanoContaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorPlanoConta() + " - " + obj.getDescricao()));
			}
			setListaSelectItemPlanoConta(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemHistoricoContabil(String prm) throws Exception {
		List<HistoricoContabilVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarHistoricoContabilPorCodigo(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				HistoricoContabilVO obj = (HistoricoContabilVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
			setListaSelectItemHistoricoContabil(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemPlanoConta() {
		try {
			montarListaSelectItemPlanoConta("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public void montarListaSelectItemHistoricoContabil() {
		try {
			montarListaSelectItemHistoricoContabil("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List<PlanoContaVO> consultarPlanoContaPorCodigo(String nomePrm) throws Exception {
		int codigo = 0;
		if (!nomePrm.equals("")) {
			codigo = Integer.parseInt(nomePrm);
		}
		List<PlanoContaVO> lista = getFacadeFactory().getPlanoContaFacade().consultarPorCodigo(codigo, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public List<HistoricoContabilVO> consultarHistoricoContabilPorCodigo(String nomePrm) throws Exception {
		int codigo = 0;
		if (!nomePrm.equals("")) {
			codigo = Integer.parseInt(nomePrm);
		}
		List<HistoricoContabilVO> lista = getFacadeFactory().getHistoricoContabilFacade().consultarPorCodigo(codigo, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDescontoProgressivoPadrao();
		montarListaSelectItemCentroReceita();
		montarListaSelectItemContaCorrente();
		montarListaSelectItemCategoriaDespesa();
		montarListaSelectItemOperadoraCartao();
		montarListaSelectItemFormaPagamento();
		montarListaSelectItemDepartamentoPadraoAntecipacaoCheque();
		montarListaSelectItemModeloBoleto();
		montarListaSelectItemPlanoConta();
		montarListaSelectItemHistoricoContabil();
		montarListaSelectItemIndiceReajuste();
		montarListaSelectItemBanco();
		montarListaSelectFiltroPadraoContaReceberVisaoAluno();
	}

	public String adicionarConfiguracaoFinanceiroCartao() throws Exception {
		try {
			if (!getConfiguracaoFinanceiroVO().getCodigo().equals(0)) {
				getConfiguracaoFinanceiroCartaoVO().getConfiguracaoFinanceiroVO().setCodigo(getConfiguracaoFinanceiroVO().getCodigo());
			}
			if (getConfiguracaoFinanceiroCartaoVO().getCodigo() == null || getConfiguracaoFinanceiroCartaoVO().getCodigo().equals(0)) {
				getConfiguracaoFinanceiroCartaoVO().setNovoObj(true);
			}
			getConfiguracaoFinanceiroCartaoVO().setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getConfiguracaoFinanceiroCartaoVO().setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getConfiguracaoFinanceiroCartaoVO().setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getCategoriaDespesaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			getFacadeFactory().getConfiguracaoFinanceiroFacade().adicionarObjConfiguracaoFinanceiroCartaoVOs(getConfiguracaoFinanceiroVO(), getConfiguracaoFinanceiroCartaoVO(), getUsuarioLogado());
			setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
			setMensagemID("msg_dados_adicionados");
			return "";
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String editarConfiguracaoFinanceiroCartao() throws Exception {
		ConfiguracaoFinanceiroCartaoVO obj = (ConfiguracaoFinanceiroCartaoVO) context().getExternalContext().getRequestMap().get("itemConfiguracaoFinanceiroCartao");
		setConfiguracaoFinanceiroCartaoVO(obj);
		return "";
	}

	public String removerConfiguracaoFinanceiroCartao() throws Exception {
		ConfiguracaoFinanceiroCartaoVO obj = (ConfiguracaoFinanceiroCartaoVO) context().getExternalContext().getRequestMap().get("itemConfiguracaoFinanceiroCartao");
		getFacadeFactory().getConfiguracaoFinanceiroFacade().excluirObjConfiguracaoFinanceiroCartaoVOs(getConfiguracaoFinanceiroVO(), obj, getUsuarioLogado());
		setMensagemID("msg_dados_excluidos");
		return "";
	}
	
	public void inicializarDadoConsultaCentroResultadoAdministrativo() {
		try {
			setCentroResultadoDataModelo(new DataModelo());
			getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			getConfiguracaoFinanceiroCartaoVO().setCentroResultadoAdministrativo(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getCentroResultadoDataModelo().setPaginaAtual(dataScrollerEvent.getPage());
			getCentroResultadoDataModelo().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultado() {
		try {
			super.consultar();
			getCentroResultadoDataModelo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, false, null, null, null, getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	

	public void upLoadArquivoIreport(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getConfiguracaoFinanceiroVO().getArquivoIreportMovFin(), getConfiguracaoGeralPadraoSistema(),PastaBaseArquivoEnum.IREPORT_TMP, getUsuarioLogado());
			File file = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getConfiguracaoFinanceiroVO().getArquivoIreportMovFin().getPastaBaseArquivoEnum().getValue() + File.separator  + getConfiguracaoFinanceiroVO().getArquivoIreportMovFin().getNome());
			//getTextoPadraoProcessoSeletivoVO().setTexto(UteisTextoPadrao.carregarTagsTextoPadraoPorTipoDesignerIreportProcessoSeletivo(file));
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void downloadArquivoJasperIreport() throws Exception {
		try {
			File arquivo = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getConfiguracaoFinanceiroVO().getArquivoIreportMovFin().getPastaBaseArquivoEnum().getValue() + File.separator  + getConfiguracaoFinanceiroVO().getArquivoIreportMovFin().getNome());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void downloadTemplateIreport() throws Exception {
		try {
			File arquivo = new File(UteisJSF.getCaminhoBase() + File.separator+ "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator +  "template_movfin_ireport.rar");
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
    public void limparDadosUpLoadArquivoIreport() throws Exception {
    	getConfiguracaoFinanceiroVO().setArquivoIreportMovFin(new ArquivoVO());
	}
	
    public void preparModalTaxas() {
    	try {
//    		if (!getConfiguracaoFinanceiroCartaoVO().getConfFinCartaoTaxaOperacao().isEmpty()) {
//    			if (getConfiguracaoFinanceiroCartaoVO().getConfFinCartaoTaxaOperacao().size() != getConfiguracaoFinanceiroCartaoVO().getQuantidadeParcelasCartaoCredito()) {
//    				getConfiguracaoFinanceiroCartaoVO().setConfFinCartaoTaxaOperacao(null);
//    			}
//    		} 
//    		if (getConfiguracaoFinanceiroCartaoVO().getConfFinCartaoTaxaOperacao().isEmpty()) {
//	    		for (int i = 0; i < getConfiguracaoFinanceiroCartaoVO().getQuantidadeParcelasCartaoCredito(); i++) {
//	    			TaxaOperacaoCartaoVO taxa = new TaxaOperacaoCartaoVO();
//	    			taxa.setParcela(i+1);
//	    			getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().adicionarObjTaxaOperacaoCartaoVOs(getConfiguracaoFinanceiroCartaoVO(), taxa, getUsuarioLogado());
//	    		}
//    		}
    	} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public void adicionarTaxa() {
    	try {
//    		if (!getConfiguracaoFinanceiroCartaoVO().getConfFinCartaoTaxaOperacao().isEmpty()) {
//    			if (getConfiguracaoFinanceiroCartaoVO().getConfFinCartaoTaxaOperacao().size() != getConfiguracaoFinanceiroCartaoVO().getQuantidadeParcelasCartaoCredito()) {
//    				getConfiguracaoFinanceiroCartaoVO().setConfFinCartaoTaxaOperacao(null);
//    			}
//    		} 
//    		if (getConfiguracaoFinanceiroCartaoVO().getConfFinCartaoTaxaOperacao().isEmpty()) {
//	    		for (int i = 0; i < getConfiguracaoFinanceiroCartaoVO().getQuantidadeParcelasCartaoCredito(); i++) {
//	    			TaxaOperacaoCartaoVO taxa = new TaxaOperacaoCartaoVO();
//	    			taxa.setParcela(i+1);
			getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().adicionarObjTaxaOperacaoCartaoVOs(getConfiguracaoFinanceiroCartaoVO(), getTaxaOperacaoCartaoVO(), getUsuarioLogado());
			setTaxaOperacaoCartaoVO(null);
//	    		}
//    		}
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void editarTaxaOperacaoCartaoVO() {
    	try {
    		TaxaOperacaoCartaoVO obj = (TaxaOperacaoCartaoVO) context().getExternalContext().getRequestMap().get("taxaOperadora");
    		setTaxaOperacaoCartaoVO(obj);	    	
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void removerTaxaOperacaoCartaoVO() {
    	try {
    		TaxaOperacaoCartaoVO obj = (TaxaOperacaoCartaoVO) context().getExternalContext().getRequestMap().get("taxaOperadora");
    		getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().excluirObjTaxaOperacaoCartaoVOs(getConfiguracaoFinanceiroCartaoVO(), obj, getUsuarioLogado());
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void selecionarTipoEnvioInadimplencia() {
		if (getConfiguracaoFinanceiroVO().getTipoEnvioInadimplencia().equals("periodicidade")) {
			getConfiguracaoFinanceiroVO().setQuantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente(0);
			getConfiguracaoFinanceiroVO().setQuantidadeDiasEnviarSegundaMensagemCobrancaInadimplente(0);
			getConfiguracaoFinanceiroVO().setQuantidadeDiasEnviarTerceiraMensagemCobrancaInadimplente(0);
		} else {
			getConfiguracaoFinanceiroVO().setQuantidadeDiasEnviarMensagemCobrancaInadimplente(0);
			getConfiguracaoFinanceiroVO().setPeriodicidadeDiasEnviarMensagemCobrancaInadimplente(0);
		}
	}

	public Boolean getApresentarTipoEnvioInadimplencia() {
		boolean valor = true;
		if (getConfiguracaoFinanceiroVO().getTipoEnvioInadimplencia().equals("periodicidade")) {
			return true;
		} else {
			return false;
		}
	}

	public List<SelectItem> getListaSelectItemTipoInadimplencia() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("periodicidade", "Mensagem Periodica"));
		itens.add(new SelectItem("fixo", "Mensagem Fixa"));
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
		return "consultar";
	}

	public List<SelectItem> getListaSelectItemCentroReceita() {
		return listaSelectItemCentroReceita;
	}

	public void setListaSelectItemCentroReceita(List<SelectItem> listaSelectItemCentroReceita) {
		this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
	}

	public List<SelectItem> getListaSelectItemDescontoProgressivoPadrao() {
		return (listaSelectItemDescontoProgressivoPadrao);
	}

	public void setListaSelectItemDescontoProgressivoPadrao(List listaSelectItemDescontoProgressivoPadrao) {
		this.listaSelectItemDescontoProgressivoPadrao = listaSelectItemDescontoProgressivoPadrao;
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		if (configuracaoFinanceiroVO == null) {
			configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
		}
		return configuracaoFinanceiroVO;
	}

	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
	}

	public List getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		configuracaoFinanceiroVO = null;
		Uteis.liberarListaMemoria(listaSelectItemDescontoProgressivoPadrao);
		Uteis.liberarListaMemoria(listaSelectItemCentroReceita);
		Uteis.liberarListaMemoria(listaSelectItemContaCorrente);
	}

	/**
	 * @return the listaSelectItemFormaPagamento
	 */
	public List getListaSelectItemFormaPagamento() {
		return listaSelectItemFormaPagamento;
	}

	/**
	 * @param listaSelectItemFormaPagamento
	 *            the listaSelectItemFormaPagamento to set
	 */
	public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	/**
	 * @return the listaSelectItemCategoriaDespesa
	 */
	public List getListaSelectItemCategoriaDespesa() {
		return listaSelectItemCategoriaDespesa;
	}

	/**
	 * @param listaSelectItemCategoriaDespesa
	 *            the listaSelectItemCategoriaDespesa to set
	 */
	public void setListaSelectItemCategoriaDespesa(List listaSelectItemCategoriaDespesa) {
		this.listaSelectItemCategoriaDespesa = listaSelectItemCategoriaDespesa;
	}

	/**
	 * @return the listaSelectItemDepartamentoPadraoAntecipacaoCheque
	 */
	public List getListaSelectItemDepartamentoPadraoAntecipacaoCheque() {
		if (listaSelectItemDepartamentoPadraoAntecipacaoCheque == null) {
			listaSelectItemDepartamentoPadraoAntecipacaoCheque = new ArrayList(0);
		}
		return listaSelectItemDepartamentoPadraoAntecipacaoCheque;
	}

	/**
	 * @param listaSelectItemDepartamentoPadraoAntecipacaoCheque
	 *            the listaSelectItemDepartamentoPadraoAntecipacaoCheque to set
	 */
	public void setListaSelectItemDepartamentoPadraoAntecipacaoCheque(List listaSelectItemDepartamentoPadraoAntecipacaoCheque) {
		this.listaSelectItemDepartamentoPadraoAntecipacaoCheque = listaSelectItemDepartamentoPadraoAntecipacaoCheque;
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
	 * @return the listaSelectItemModeloBoleto
	 */
	public List getListaSelectItemModeloBoleto() {
		if (listaSelectItemModeloBoleto == null) {
			listaSelectItemModeloBoleto = new ArrayList(0);
		}
		return listaSelectItemModeloBoleto;
	}

	/**
	 * @param listaSelectItemModeloBoleto
	 *            the listaSelectItemModeloBoleto to set
	 */
	public void setListaSelectItemModeloBoleto(List listaSelectItemModeloBoleto) {
		this.listaSelectItemModeloBoleto = listaSelectItemModeloBoleto;
	}

	public List<SelectItem> getListaSelectItemPlanoConta() {
		if (listaSelectItemPlanoConta == null) {
			listaSelectItemPlanoConta = new ArrayList<SelectItem>();
		}
		return listaSelectItemPlanoConta;
	}

	public void setListaSelectItemPlanoConta(List<SelectItem> listaSelectItemPlanoConta) {
		this.listaSelectItemPlanoConta = listaSelectItemPlanoConta;
	}

	public List<SelectItem> getListaSelectItemHistoricoContabil() {
		if (listaSelectItemHistoricoContabil == null) {
			listaSelectItemHistoricoContabil = new ArrayList<SelectItem>();
		}
		return listaSelectItemHistoricoContabil;
	}

	public void setListaSelectItemHistoricoContabil(List<SelectItem> listaSelectItemHistoricoContabil) {
		this.listaSelectItemHistoricoContabil = listaSelectItemHistoricoContabil;
	}

	public ConfiguracaoFinanceiroCartaoVO getConfiguracaoFinanceiroCartaoVO() {
		if (configuracaoFinanceiroCartaoVO == null) {
			configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
		}
		return configuracaoFinanceiroCartaoVO;
	}

	public void setConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO) {
		this.configuracaoFinanceiroCartaoVO = configuracaoFinanceiroCartaoVO;
	}

	public List<SelectItem> getListaSelectItemOperadoraCartao() {
		if (listaSelectItemOperadoraCartao == null) {
			listaSelectItemOperadoraCartao = new ArrayList<SelectItem>();
		}
		return listaSelectItemOperadoraCartao;
	}

	public void setListaSelectItemOperadoraCartao(List<SelectItem> listaSelectItemOperadoraCartao) {
		this.listaSelectItemOperadoraCartao = listaSelectItemOperadoraCartao;
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

	public List<SelectItem> getListaSelectItemGrupoDestinatario() {
		if (listaSelectItemGrupoDestinatario == null) {
			listaSelectItemGrupoDestinatario = getFacadeFactory().getGrupoDestinatariosFacade().consultarDadosListaSelectItem(Obrigatorio.NAO);
		}
		return listaSelectItemGrupoDestinatario;
	}

	public void setListaSelectItemGrupoDestinatario(List<SelectItem> listaSelectItemGrupoDestinatario) {
		this.listaSelectItemGrupoDestinatario = listaSelectItemGrupoDestinatario;
	}
	
	
	
	public List<SelectItem> getListaSelectItemIndiceReajuste() {
		if (listaSelectItemIndiceReajuste == null) {
			listaSelectItemIndiceReajuste = new ArrayList<>();
		}
		return listaSelectItemIndiceReajuste;
	}

	public void setListaSelectItemIndiceReajuste(List<SelectItem> listaSelectItemIndiceReajuste) {
		this.listaSelectItemIndiceReajuste = listaSelectItemIndiceReajuste;
	}

	public boolean getIsClienteUNIRV() {
		try {
			return getConfiguracaoGeralPadraoSistema().getIsClienteUNIRV();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void setApresentarTipoEnvioInadimplencia(Boolean apresentarTipoEnvioInadimplencia) {
		this.apresentarTipoEnvioInadimplencia = apresentarTipoEnvioInadimplencia;
	}	
	
/**
 * @author Victor Hugo de Paula Costa
 * @return
 */
	private List<SelectItem> listaSelectItemTipoFinanciamentoEnum;
	private ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO;

	public List<SelectItem> getListaSelectItemTipoFinanciamentoEnum() {
		if(listaSelectItemTipoFinanciamentoEnum == null) {
			listaSelectItemTipoFinanciamentoEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoFinanciamentoEnum.class, "name", "valorApresentar", false);
		}
		return listaSelectItemTipoFinanciamentoEnum;
	}

	public void setListaSelectItemTipoFinanciamentoEnum(List<SelectItem> listaSelectItemTipoFinanciamentoEnum) {
		this.listaSelectItemTipoFinanciamentoEnum = listaSelectItemTipoFinanciamentoEnum;
	}

	public ConfiguracaoFinanceiroCartaoRecebimentoVO getConfiguracaoFinanceiroCartaoRecebimentoVO() {
		if(configuracaoFinanceiroCartaoRecebimentoVO == null) {
			configuracaoFinanceiroCartaoRecebimentoVO = new ConfiguracaoFinanceiroCartaoRecebimentoVO();
		}
		return configuracaoFinanceiroCartaoRecebimentoVO;
	}

	public void setConfiguracaoFinanceiroCartaoRecebimentoVO(ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO) {
		this.configuracaoFinanceiroCartaoRecebimentoVO = configuracaoFinanceiroCartaoRecebimentoVO;
	}
	
	private List<SelectItem> listaSelectItemSomenteContaCorrente;
	
	public List<SelectItem> getListaSelectItemSomenteContaCorrente() {
		if(listaSelectItemSomenteContaCorrente == null) {
			listaSelectItemSomenteContaCorrente = new ArrayList<SelectItem>();
		}
		return listaSelectItemSomenteContaCorrente;
	}

	public void setListaSelectItemSomenteContaCorrente(List<SelectItem> listaSelectItemSomenteContaCorrente) {
		this.listaSelectItemSomenteContaCorrente = listaSelectItemSomenteContaCorrente;
	}

	public void montarListaSelectItemSomenteContaCorrente() {
		List<ContaCorrenteVO> resultadoConsulta = null;
		Iterator<ContaCorrenteVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigoSomenteContasCorrente(0, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (obj.getContaCaixa().equals(Boolean.TRUE)) {
					objs.add(new SelectItem(obj.getCodigo(), "Ag:" + obj.getAgencia().getNumeroAgencia() + "-" + obj.getAgencia().getDigito() + " / " + "CC:" + obj.getNumero() + "-" + obj.getDigito()));
				} else {
					objs.add(new SelectItem(obj.getCodigo(), "CC:" + obj.getNumero() + "-" + obj.getDigito()));
				}
			}
			setListaSelectItemSomenteContaCorrente(objs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void executarVerificacaoUsuarioPodeAlterarTaxaOperacao() {
    	boolean usuarioValido = false;
    	UsuarioVO usuarioVerif = null;
    	try {
    		usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarTaxaOperacao(), this.getSenhaLiberarTaxaOperacao(), true, Uteis.NIVELMONTARDADOS_TODOS);
    		usuarioValido = true;
    	} catch (Exception e) {
    	}
    	boolean usuarioTemPermissaoLiberar = false;
    	try {
    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberarAlteracaoTaxaOperacaoCartaoCredito", usuarioVerif);
    		usuarioTemPermissaoLiberar = true;
    	} catch (Exception e) {
    	}
    	try {
    		if (!usuarioValido) {
    			throw new Exception("Usuário/Senha Inválidos");
    		}
    		if (!usuarioTemPermissaoLiberar) {
				throw new Exception("Você não tem permissão para alterar a Taxa de Operação.");
    		} else {
    			setAutorizarMudancaTaxaOperacao(Boolean.TRUE);
    		}
    		this.setUsernameLiberarTaxaOperacao("");
    		this.setSenhaLiberarTaxaOperacao("");
    		setMensagemID("msg_LiberadaAlteracaoTaxaOperacao");
    	} catch (Exception e) {
    		this.setUsernameLiberarTaxaOperacao("");
    		this.setSenhaLiberarTaxaOperacao("");
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
	public void executarVerificacaoUsuarioPodeAlterarContaCorrente() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarTaxaOperacao(), this.getSenhaLiberarTaxaOperacao(), true, Uteis.NIVELMONTARDADOS_TODOS);
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberarAlteracaoContaCorrenteCartaoCredito", usuarioVerif);
				setAutorizarMudancaContaCorrente(true);
				setMensagemID("msg_LiberadaAlteracaoContaCorrente");
			} catch (Exception e) {
				throw new Exception("Você não tem permissão para alterar a Categoria Despesa Operadora.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setUsernameLiberarTaxaOperacao("");
			setSenhaLiberarTaxaOperacao("");
		}
	}
    
    
    private String usernameLiberarTaxaOperacao;
    private String senhaLiberarTaxaOperacao;
    private Boolean autorizarMudancaTaxaOperacao;
    private Boolean autorizarMudancaContaCorrente;
    
    public String getUsernameLiberarTaxaOperacao() {
		if (usernameLiberarTaxaOperacao == null) {
			usernameLiberarTaxaOperacao = "";
		}
		return usernameLiberarTaxaOperacao;
	}

	public void setUsernameLiberarTaxaOperacao(String usernameLiberarTaxaOperacao) {
		this.usernameLiberarTaxaOperacao = usernameLiberarTaxaOperacao;
	}

	public String getSenhaLiberarTaxaOperacao() {
		if (senhaLiberarTaxaOperacao == null) {
			senhaLiberarTaxaOperacao = "";
		}
		return senhaLiberarTaxaOperacao;
	}
	
	public void setSenhaLiberarTaxaOperacao(String senhaLiberarTaxaOperacao) {
		this.senhaLiberarTaxaOperacao = senhaLiberarTaxaOperacao;
	}

	public Boolean getAutorizarMudancaTaxaOperacao() {
		if(autorizarMudancaTaxaOperacao == null) {
			autorizarMudancaTaxaOperacao = false;
		}
		return autorizarMudancaTaxaOperacao;
	}

	public void setAutorizarMudancaTaxaOperacao(Boolean autorizarMudancaTaxaOperacao) {
		this.autorizarMudancaTaxaOperacao = autorizarMudancaTaxaOperacao;
	}

	public Boolean getAutorizarMudancaContaCorrente() {
		if(autorizarMudancaContaCorrente == null) {
			autorizarMudancaContaCorrente = false;
		}
		return autorizarMudancaContaCorrente;
	}

	public void setAutorizarMudancaContaCorrente(Boolean autorizarMudancaContaCorrente) {
		this.autorizarMudancaContaCorrente = autorizarMudancaContaCorrente;
	}
	
	public void executarMontagemDadosOperadoraCartao() {
		try {
			getConfiguracaoFinanceiroCartaoVO().setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroCartaoVO().getOperadoraCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 03/11/2015 16:40
	 * 
	 */
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private DadosEnvioContaMundipagg dadosEnvioContaMundipagg;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ContaCorrenteVO contaCorrenteVO;
	
	public boolean getIsVerificarUsuarioOtimize() {
		return getUsuarioLogado().getUsername().equals("otimize-ti");
	}
	
	public void montarDadosCriacaoContaMundipagg() {
		montarListaSelectItemUnidadeEnsino();
	}
	
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
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
	
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		return lista;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


	public void montarListaSelectItemIndiceReajuste() {
		try {
			List<IndiceReajusteVO> resultadoConsulta = getFacadeFactory().getIndiceReajusteFacade().consultarPorDescricao("", getUsuarioLogado());
			setListaSelectItemIndiceReajuste(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
	}

	public DadosEnvioContaMundipagg getDadosEnvioContaMundipagg() {
		if(dadosEnvioContaMundipagg == null) {
			dadosEnvioContaMundipagg = new DadosEnvioContaMundipagg();
		}
		return dadosEnvioContaMundipagg;
	}

	public void setDadosEnvioContaMundipagg(DadosEnvioContaMundipagg dadosEnvioContaMundipagg) {
		this.dadosEnvioContaMundipagg = dadosEnvioContaMundipagg;
	}
	
	public void montarDadosEnvioContaUnidadeEnsino() {
		try {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			limparCamposUnidadeEnsino();
			getDadosEnvioContaMundipagg().setNomeFantasia(getUnidadeEnsinoVO().getNome());
			getDadosEnvioContaMundipagg().setRazaoSocial(getUnidadeEnsinoVO().getRazaoSocial());
			getDadosEnvioContaMundipagg().setCpfCnpj(getUnidadeEnsinoVO().getCNPJ());
			getDadosEnvioContaMundipagg().setEmailEmpresa(getUnidadeEnsinoVO().getEmail());
			getDadosEnvioContaMundipagg().setTelefone(getUnidadeEnsinoVO().getTelComercial1());
			getDadosEnvioContaMundipagg().setNumeroEndereco(getUnidadeEnsinoVO().getNumero());
			getDadosEnvioContaMundipagg().setCidade(getUnidadeEnsinoVO().getCidade().getNome());
			getDadosEnvioContaMundipagg().setComplemento(getUnidadeEnsinoVO().getComplemento());
			getDadosEnvioContaMundipagg().setPais(getUnidadeEnsinoVO().getCidade().getEstado().getPaiz().getNome());
			getDadosEnvioContaMundipagg().setDistrito("");
			getDadosEnvioContaMundipagg().setEstado(getUnidadeEnsinoVO().getCidade().getEstado().getSigla());
			getDadosEnvioContaMundipagg().setEndereco(getUnidadeEnsinoVO().getEndereco());
			getDadosEnvioContaMundipagg().setCep(getUnidadeEnsinoVO().getCEP());
			getDadosEnvioContaMundipagg().setTipoDocumento("CNPJ");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparCamposUnidadeEnsino() {
		getDadosEnvioContaMundipagg().setNomeFantasia("");
		getDadosEnvioContaMundipagg().setRazaoSocial("");
		getDadosEnvioContaMundipagg().setCpfCnpj("");
		getDadosEnvioContaMundipagg().setEmailEmpresa("");
		getDadosEnvioContaMundipagg().setTelefone("");
		getDadosEnvioContaMundipagg().setNumeroEndereco("");
		getDadosEnvioContaMundipagg().setCidade("");
		getDadosEnvioContaMundipagg().setPais("");
		getDadosEnvioContaMundipagg().setDistrito("");
		getDadosEnvioContaMundipagg().setEstado("");
		getDadosEnvioContaMundipagg().setEndereco("");
		getDadosEnvioContaMundipagg().setCep("");	
	}
	
	public void montarDadosEnvioContaContaCorrente() {
		try {
			setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			limparCamposContaCorrente();
			getDadosEnvioContaMundipagg().setNumeroContaBanco(getContaCorrenteVO().getNumero());
			getDadosEnvioContaMundipagg().setNumeroAgencia(getContaCorrenteVO().getAgencia().getNumeroAgencia());
			getDadosEnvioContaMundipagg().setCodigoBanco(getContaCorrenteVO().getAgencia().getBanco().getNrBanco());
			getDadosEnvioContaMundipagg().setDestino(getContaCorrenteVO().getAgencia().getGerente());
			getDadosEnvioContaMundipagg().setEmailDestino(getContaCorrenteVO().getAgencia().getEmail());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparCamposContaCorrente() {
		getDadosEnvioContaMundipagg().setNumeroContaBanco("");
		getDadosEnvioContaMundipagg().setNumeroAgencia("");
		getDadosEnvioContaMundipagg().setCodigoBanco("");
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if(contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}
	
	
	
	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public void criarConta() {
		try {
			getDadosEnvioContaMundipagg().setSmartWalletKey(getConfiguracaoFinanceiroVO().getChaveContaMundipagg());
			getConfiguracaoFinanceiroVO().setChaveContaMundipagg(GerenciadorSplitTransacaoMundiPagg.executarCriacaoConta(getDadosEnvioContaMundipagg()));
			gravar();
			setMensagemID("Conta Criada com Sucesso!", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void scrollerListenerCategoriaDespesa(DataScrollEvent dataScrollerEvent) {
		getConsultaCategoriaDespesa().setPaginaAtual(dataScrollerEvent.getPage());
		getConsultaCategoriaDespesa().setPage(dataScrollerEvent.getPage());
		this.consultarCategoriaDespesa();
	}

	public void scrollerListenerFormaPagamentoPadrao(DataScrollEvent dataScrollerEvent) {
		getConsultaFormaPagamento().setPaginaAtual(dataScrollerEvent.getPage());
		getConsultaFormaPagamento().setPage(dataScrollerEvent.getPage());
		this.consultarFormaPagamentoPadrao();
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

	public void consultarFormaPagamentoPadrao() {
		try {
			
			if (getConsultaFormaPagamento().getCampoConsulta().equals("CODIGO")) {
				Uteis.validarSomenteNumeroString(getConsultaFormaPagamento().getValorConsulta());
			}
			
			getConsultaFormaPagamento().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getFormaPagamentoFacade().consultarPorEnumCampoConsulta(getConsultaFormaPagamento());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCategoriaDespesaPorIdentificador() {
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoFinanceiroVO().getCategoriaDespesaVO().getIdentificadorCategoriaDespesa())) {
				this.getConfiguracaoFinanceiroVO().setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesaUnico(getConfiguracaoFinanceiroVO().getCategoriaDespesaVO().getIdentificadorCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			this.getConfiguracaoFinanceiroVO().setCategoriaDespesaVO(new CategoriaDespesaVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparCategoriaDespesa() {
		this.getConfiguracaoFinanceiroVO().setCategoriaDespesaVO(new CategoriaDespesaVO());
	}

	public void limparFormaPagamentoPadrao() {
		this.getConfiguracaoFinanceiroVO().setFormaPagamentoPadrao(new FormaPagamentoVO());
	}

	public void selecionarCategoriaDespesa() {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItem");
		getConfiguracaoFinanceiroVO().setCategoriaDespesaVO(obj);

		setConsultaCategoriaDespesa(new DataModelo());
	}

	public void selecionarFormaPagamentoPadrao() {
		FormaPagamentoVO obj = (FormaPagamentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoPadraoItem");
		getConfiguracaoFinanceiroVO().setFormaPagamentoPadrao(obj);
		
		setConsultaCategoriaDespesa(new DataModelo());
	}

	/**
	 * Atualiza a lisa de bancos cadastrados no sistema.
	 */
	public void montarListaSelectItemBanco() {
		try {
			montarListaSelectItemBanco("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo {@link BancoVO}
	 */
	public void montarListaSelectItemBanco(String prm) throws Exception {
        List<BancoVO> resultadoConsulta = null;

        try {
            resultadoConsulta = consultarBancoPorNome(prm);
            Iterator<?> i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<>();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                BancoVO obj = (BancoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemBanco(objs);
        } catch (Exception e) {
            throw e;
        }
	}
	
	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List<BancoVO> consultarBancoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getBancoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public TaxaOperacaoCartaoVO getTaxaOperacaoCartaoVO() {
		if (taxaOperacaoCartaoVO == null) {
			taxaOperacaoCartaoVO = new TaxaOperacaoCartaoVO();
		}
		return taxaOperacaoCartaoVO;
	}

	public void setTaxaOperacaoCartaoVO(TaxaOperacaoCartaoVO taxaOperacaoCartaoVO) {
		this.taxaOperacaoCartaoVO = taxaOperacaoCartaoVO;
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

	public DataModelo getConsultaFormaPagamento() {
		if (consultaFormaPagamento == null) {
			consultaFormaPagamento = new DataModelo();
		}
		return consultaFormaPagamento;
	}

	public void setConsultaFormaPagamento(DataModelo consultaFormaPagamento) {
		this.consultaFormaPagamento = consultaFormaPagamento;
	}

	public List<SelectItem> getListaSelectItemBanco() {
		return (listaSelectItemBanco);
	}

	public void setListaSelectItemBanco(List<SelectItem> listaSelectItemBanco) {
		this.listaSelectItemBanco = listaSelectItemBanco;
	}
	
	public Boolean getMarcarTodosTipoOrigemEnvioInadimplencia() {
		if (marcarTodosTipoOrigemEnvioInadimplencia == null) {
			marcarTodosTipoOrigemEnvioInadimplencia = true;
		}
		return marcarTodosTipoOrigemEnvioInadimplencia;
	}

	public void setMarcarTodosTipoOrigemEnvioInadimplencia(Boolean marcarTodosTipoOrigemEnvioInadimplencia) {
		this.marcarTodosTipoOrigemEnvioInadimplencia = marcarTodosTipoOrigemEnvioInadimplencia;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigemEnvioInadimplencia()) {
			getConfiguracaoFinanceiroVO().realizarMarcarTodasOrigens();
		} else {
			getConfiguracaoFinanceiroVO().realizarDesmarcarTodasOrigens();
		}
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigemEnvioInadimplencia()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public List<SelectItem> getListaSelectFiltroPadraoContaReceberVisaoAluno() {
		if (listaSelectFiltroPadraoContaReceberVisaoAluno == null) {
			listaSelectFiltroPadraoContaReceberVisaoAluno = new ArrayList<SelectItem>();
		}
		return listaSelectFiltroPadraoContaReceberVisaoAluno;
	}

	public void setListaSelectFiltroPadraoContaReceberVisaoAluno(
			List<SelectItem> listaSelectFiltroPadraoContaReceberVisaoAluno) {
		this.listaSelectFiltroPadraoContaReceberVisaoAluno = listaSelectFiltroPadraoContaReceberVisaoAluno;
	}
	
	public void montarListaSelectFiltroPadraoContaReceberVisaoAluno() {
		getListaSelectFiltroPadraoContaReceberVisaoAluno().clear();
		getListaSelectFiltroPadraoContaReceberVisaoAluno().add(new SelectItem("MA", "Mês Atual"));
		getListaSelectFiltroPadraoContaReceberVisaoAluno().add(new SelectItem("EM", "Em Aberto"));
		getListaSelectFiltroPadraoContaReceberVisaoAluno().add(new SelectItem("PG", "Pagas"));
		getListaSelectFiltroPadraoContaReceberVisaoAluno().add(new SelectItem("TO", "Todas"));
	}
	
 }
