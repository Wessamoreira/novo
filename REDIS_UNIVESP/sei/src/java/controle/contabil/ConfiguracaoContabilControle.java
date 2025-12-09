package controle.contabil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraPlanoContaVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.enumeradores.TipoRegraContabilEnum;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoDesconto;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("ConfiguracaoContabilControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoContabilControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3353281672734374745L;
	private ConfiguracaoContabilVO configuracaoContabilVO;
	private ConfiguracaoContabilRegraVO configuracaoContabilRegraVO;
	private ConfiguracaoContabilRegraPlanoContaVO configuracaoContabilRegraPlanoContaVO;
	private Boolean filtrarUnidadeEnsino;
	private Boolean marcarTodasUnidadeEnsino;
	protected List<SelectItem> listaSelectItemLayoutIntegracaoContabil;
	protected List<SelectItem> listaSelectItemFormaPagamento;
	protected List<SelectItem> listaSelectItemFormaPagamentoCartoes;
	protected List<SelectItem> listaSelectItemFormaPagamentoMovFin;
	protected List<SelectItem> listaSelectItemOperadoraCartao;
	protected List<SelectItem> listaSelectItemContaCorrenteOrigem;
	protected List<SelectItem> listaSelectItemContaCorrenteDestino;
	protected List<SelectItem> listaSelectItemImposto;

	protected List listaConsultaCentroDespesa;
	protected String valorConsultaCentroDespesa;
	protected String campoConsultaCentroDespesa;

	protected List<CentroReceitaVO> listaConsultaCentroReceita;
	protected String valorConsultaCentroReceita;
	protected String campoConsultaCentroReceita;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;

	private String valorConsultaTurno;
	private String campoConsultaTurno;
	private List<TurnoVO> listaConsultaTurno;

	private String valorConsultaPlanoConta;
	private String campoConsultaPlanoConta;
	private List<PlanoContaVO> listaConsultaPlanoConta;

	protected String valorConsultaParceiro;
	protected String campoConsultaParceiro;
	protected List<ParceiroVO> listaConsultaParceiro;

	protected List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;

	protected List<FuncionarioVO> listaConsultaFuncionario;
	protected String valorConsultaFuncionario;
	protected String campoConsultaFuncionario;

	protected List<BancoVO> listaConsultaBanco;
	protected String valorConsultaBanco;
	protected String campoConsultaBanco;

	protected String campoConsultaCategoriaProduto;
	protected String valorConsultaCategoriaProduto;
	protected List<CategoriaProdutoVO> listaConsultaCategoriaProduto;

	public ConfiguracaoContabilControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PlanoConta</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			removerObjetoMemoria(this);
			setConfiguracaoContabilVO(new ConfiguracaoContabilVO());
			setConfiguracaoContabilRegraVO(new ConfiguracaoContabilRegraVO());
			inicializarListasSelectItemTodosComboBox();
			carregarUnidadeEnsinoParaConfiguracaoContabil();
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoContabilForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoConta</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ConfiguracaoContabilVO obj = (ConfiguracaoContabilVO) context().getExternalContext().getRequestMap().get("configuracaoContabilItens");
			setConfiguracaoContabilVO(getFacadeFactory().getConfiguracaoContabilFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setConfiguracaoContabilRegraVO(new ConfiguracaoContabilRegraVO());
			inicializarListasSelectItemTodosComboBox();
			carregarUnidadeEnsinoParaConfiguracaoContabil();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoContabilForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PlanoConta</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getConfiguracaoContabilFacade().persistir(getConfiguracaoContabilVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public void consultarDados() {
		try {
			super.consultar();
			List<ConfiguracaoContabilVO> objs = new ArrayList<>();
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigo(valorInt, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("layoutIntegracaoContabil")) {
				objs = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorLayoutIntegracaoContabil(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getConfiguracaoContabilFacade().excluir(getConfiguracaoContabilVO(), true, getUsuarioLogado());
			setConfiguracaoContabilVO(new ConfiguracaoContabilVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoContabilForm.xhtml");
	}

	public void addConfiguracaoContabilRegraDesconto() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.DESCONTO);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void addConfiguracaoContabilRegraDescontoPagar() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.DESCONTO_PAGAR);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addConfiguracaoContabilRegraJuroMultaAcrescimo() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.JURO_MULTA_ACRESCIMO);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void addConfiguracaoContabilRegraTaxaoCartoes() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.TAXA_CARTAOES);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void addConfiguracaoContabilRegraCartaoCredito() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.CARTAO_CREDITO);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void addConfiguracaoContabilRegraJuroMultaPagar() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.JURO_MULTA_PAGAR);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addConfiguracaoContabilRegraNotafiscalCategoriaProduto() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addConfiguracaoContabilRegraNotafiscalImposto() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.NOTA_FISCAL_ENTRADA_IMPOSTO);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addConfiguracaoContabilRegraMovimentacaoFinanceira() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.MOVIMENTACAO_FINANCEIRA);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addConfiguracaoContabilRegraPagamento() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.PAGAMENTO);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addConfiguracaoContabilRegraRecebimento() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.RECEBIMENTO);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void addConfiguracaoContabilRegraSacado() {
		try {
			getConfiguracaoContabilRegraVO().setTipoRegraContabilEnum(TipoRegraContabilEnum.SACADO);
			addConfiguracaoContabilRegra();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private void addConfiguracaoContabilRegra() throws Exception {
		if (Uteis.isAtributoPreenchido(getConfiguracaoContabilRegraPlanoContaVO().getPlanoContaCreditoVO()) && Uteis.isAtributoPreenchido(getConfiguracaoContabilRegraPlanoContaVO().getPlanoContaDebitoVO())) {
			getFacadeFactory().getConfiguracaoContabilRegraFacade().addConfiguracaoContabilRegraPlanoConta(getConfiguracaoContabilRegraVO(), getConfiguracaoContabilRegraPlanoContaVO(), getUsuarioLogado());
			setConfiguracaoContabilRegraPlanoContaVO(new ConfiguracaoContabilRegraPlanoContaVO());
		}
		getFacadeFactory().getConfiguracaoContabilFacade().addConfiguracaoContabilRegra(getConfiguracaoContabilVO(), getConfiguracaoContabilRegraVO(), getUsuarioLogado());
		setConfiguracaoContabilRegraVO(new ConfiguracaoContabilRegraVO());
		setMensagemID("msg_dados_adicionados");

	}

	public void editarConfiguracaoContabilRegra() {
		try {
			ConfiguracaoContabilRegraVO obj = (ConfiguracaoContabilRegraVO) context().getExternalContext().getRequestMap().get("configuracaoRegraItens");
			setConfiguracaoContabilRegraVO(obj);
			obj.setEdicaoManual(true);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removeConfiguracaoContabilRegra() {
		try {
			ConfiguracaoContabilRegraVO obj = (ConfiguracaoContabilRegraVO) context().getExternalContext().getRequestMap().get("configuracaoRegraItens");
			getFacadeFactory().getConfiguracaoContabilFacade().removeConfiguracaoContabilRegra(getConfiguracaoContabilVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void addConfiguracaoContabilRegraPlanoConta() {
		try {
			getFacadeFactory().getConfiguracaoContabilRegraFacade().addConfiguracaoContabilRegraPlanoConta(getConfiguracaoContabilRegraVO(), getConfiguracaoContabilRegraPlanoContaVO(), getUsuarioLogado());
			setConfiguracaoContabilRegraPlanoContaVO(new ConfiguracaoContabilRegraPlanoContaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removeConfiguracaoContabilRegraPlanoConta() {
		try {
			ConfiguracaoContabilRegraPlanoContaVO obj = (ConfiguracaoContabilRegraPlanoContaVO) context().getExternalContext().getRequestMap().get("regraPlanoContaItems");
			getFacadeFactory().getConfiguracaoContabilRegraFacade().removeConfiguracaoContabilRegraPlanoConta(getConfiguracaoContabilRegraVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
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

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemLayoutIntegracaoContabil();
		montarListaSelectItemFormaPagamento();
		montarListaSelectItemFormaPagamentoCartao();
		montarListaSelectItemFormaPagamentoMovFin();
		montarListaSelectItemContaCorrenteOrigem();
		montarListaSelectItemContaCorrenteDestino();
		montarListaSelectItemOperadoraCartao();
		montarListaSelectItemImposto();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCorrenteOrigem() {
		try {
			montarListaSelectItemContaCorrenteOrigem("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemContaCorrenteOrigem(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteCaixaPorNumero(prm, 0, verificarPermissaoMovimentacaoContaCaixaParaContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, "Todas"));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				} else {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeBancoAgenciaContaApresentar()));
				}
			}
			if (resultadoConsulta.size() == 1) {
				getConfiguracaoContabilRegraVO().setContaCorrenteOrigemVO((ContaCorrenteVO) resultadoConsulta.get(0));
			}
			setListaSelectItemContaCorrenteOrigem(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCorrenteDestino() {
		try {
			montarListaSelectItemContaCorrenteDestino("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemContaCorrenteDestino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteCaixaPorNumero(prm, 0, verificarPermissaoMovimentacaoContaCaixaParaContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, "Todas"));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
				} else {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNomeBancoAgenciaContaApresentar()));
				}
			}
			if (resultadoConsulta.size() == 1) {
				getConfiguracaoContabilRegraVO().setContaCorrenteDestinoVO((ContaCorrenteVO) resultadoConsulta.get(0));
			}
			setListaSelectItemContaCorrenteDestino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public boolean verificarPermissaoMovimentacaoContaCaixaParaContaCorrente() {
		try {
			ControleAcesso.incluir("MovimentacaoContaCaixaContaCorrente", getUsuarioLogado());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, "Todas"));
			while (i.hasNext()) {
				FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemFormaPagamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamentoMovFin() {
		try {
			montarListaSelectItemFormaPagamentoMovFin("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamentoMovFin(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarFormaPagamentoDaMovimentacaoFinanceira(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, "Todas"));
			while (i.hasNext()) {
				FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemFormaPagamentoMovFin(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamentoCartao() {
		try {
			montarListaSelectItemFormaPagamentoCartao("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamentoCartao(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getFormaPagamentoFacade().consultarFormaPagamentoCartoes(false, true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, "Todas"));
			while (i.hasNext()) {
				FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemFormaPagamentoCartoes(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemLayoutIntegracaoContabil(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getLayoutIntegracaoFacade().consultaRapidaPorDescricao(prm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				LayoutIntegracaoVO obj = (LayoutIntegracaoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
			}
			setListaSelectItemLayoutIntegracaoContabil(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemLayoutIntegracaoContabil() {
		try {
			montarListaSelectItemLayoutIntegracaoContabil("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemOperadoraCartao() {
		try {
			montarListaSelectItemOperadoraCartao("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemOperadoraCartao(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if(Uteis.isAtributoPreenchido(getConfiguracaoContabilRegraVO().getFormaPagamentoVO()) 
					&& Uteis.isAtributoPreenchido(getConfiguracaoContabilRegraVO().getFormaPagamentoVO().getTipo())
					&& getConfiguracaoContabilRegraVO().getFormaPagamentoVO().isCartaoCredito()){
				prm = TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name();
			}else if (Uteis.isAtributoPreenchido(getConfiguracaoContabilRegraVO().getFormaPagamentoVO()) 
					&& Uteis.isAtributoPreenchido(getConfiguracaoContabilRegraVO().getFormaPagamentoVO().getTipo())
					&& getConfiguracaoContabilRegraVO().getFormaPagamentoVO().isCartaoDebito()){
				prm = TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO.name();
			}
			resultadoConsulta = getFacadeFactory().getOperadoraCartaoFacade().consultarPorTipo(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, "Todas"));
			while (i.hasNext()) {
				OperadoraCartaoVO obj = (OperadoraCartaoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getOperadoraCartaoCreditoApresentarDetalhado().toString()));
			}
			setListaSelectItemOperadoraCartao(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemImposto() {
		try {
			montarListaSelectItemImposto("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemImposto(String prm) throws Exception {
		List<ImpostoVO> resultadoConsulta = null;
		try {
			setControleConsultaOtimizado(new DataModelo(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getControleConsultaOtimizado().setLimitePorPagina(0);
			resultadoConsulta = getFacadeFactory().getImpostoFacade().consultaRapidaPorNome(prm, getControleConsultaOtimizado());
			getListaSelectItemImposto().clear();
			getListaSelectItemImposto().add(new SelectItem(0, "Todos"));
			resultadoConsulta.stream().forEach(p -> {
				getListaSelectItemImposto().add(new SelectItem(p.getCodigo(), p.getNome().toString()));
			});
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void consultarCentroReceita() {
		try {
			List objs = new ArrayList<>(0);
			if (getCampoConsultaCentroReceita().equals("descricao")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceita().equals("identificadorCentroReceita")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroReceita().equals("nomeDepartamento")) {
				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroReceita(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroReceita(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCentroReceita() {
		try {
			CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItens");
			getConfiguracaoContabilRegraVO().setCentroReceitaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCentroReceita() {
		try {
			getConfiguracaoContabilRegraVO().setCentroReceitaVO(new CentroReceitaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCentroReceita() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
		itens.add(new SelectItem("nomeDepartamento", "Departamento"));
		return itens;
	}

	public void selecionarCentroDespesa() {
		try {
			CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
			getConfiguracaoContabilRegraVO().setCategoriaDespesaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCentroDespesa() {
		try {
			getConfiguracaoContabilRegraVO().setCategoriaDespesaVO(new CategoriaDespesaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCentroDespesa() {
		try {
			List objs = new ArrayList<>(0);
			if (getCampoConsultaCentroDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCentroDespesa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getConfiguracaoContabilRegraVO().setCursoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() {
		try {
			getConfiguracaoContabilRegraVO().setCursoVO(new CursoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigoEUnidadeEnsino(Integer.parseInt(getValorConsultaCurso()), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
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

	public void selecionarTurno() {
		try {
			TurnoVO obj = (TurnoVO) context().getExternalContext().getRequestMap().get("turnoItem");
			getConfiguracaoContabilRegraVO().setTurnoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurno() {
		try {
			getConfiguracaoContabilRegraVO().setTurnoVO(new TurnoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurno() {
		try {
			List<TurnoVO> objs = new ArrayList<TurnoVO>(0);
			if (getCampoConsultaTurno().equals("nome")) {
				objs = getFacadeFactory().getTurnoFacade().consultarPorNome(getValorConsultaTurno(), getConfiguracaoContabilRegraVO().getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurno(new ArrayList<TurnoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome Turno"));
		return itens;
	}

	public void selecionarCategoriaProduto() {
		try {
			CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaProdutoItem");
			getConfiguracaoContabilRegraVO().setCategoriaProdutoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCategoriaProduto() {
		try {
			getConfiguracaoContabilRegraVO().setCategoriaProdutoVO(new CategoriaProdutoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCategoriaProduto() {
		try {
			List objs = new ArrayList<>(0);
			if (getCampoConsultaCategoriaProduto().equals("codigo")) {
				if (getValorConsultaCategoriaProduto().equals("")) {
					setValorConsultaCategoriaProduto("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCategoriaProduto());
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_TODOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaProduto().equals("nome")) {
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_TODOS,
						getUsuarioLogado());
			}
			setListaConsultaCategoriaProduto(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCategoriaProduto(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCategoriaProduto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void limparPlanoConta() {
		try {
			getConfiguracaoContabilRegraVO().setPlanoContaVO(new PlanoContaVO());
			getConfiguracaoContabilRegraPlanoContaVO().setPlanoContaDebitoVO(new PlanoContaVO());
			getConfiguracaoContabilRegraPlanoContaVO().setPlanoContaCreditoVO(new PlanoContaVO());
		} catch (Exception e) {
			setListaConsultaPlanoConta(new ArrayList<PlanoContaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarPlanoConta() {
		try {
			setListaConsultaPlanoConta(getFacadeFactory().getPlanoContaFacade().consultar(0, getCampoConsultaPlanoConta(), getValorConsultaPlanoConta(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPlanoConta(new ArrayList<PlanoContaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPlanoConta() {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getConfiguracaoContabilRegraVO().setPlanoContaVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarPlanoContaDebito() {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getConfiguracaoContabilRegraPlanoContaVO().setPlanoContaDebitoVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarPlanoContaCredito() {
		try {
			PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
			getConfiguracaoContabilRegraPlanoContaVO().setPlanoContaCreditoVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboPlanoConta() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
		return itens;
	}

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			getConfiguracaoContabilRegraVO().setParceiroVO(obj);
			getConfiguracaoContabilRegraVO().setCodigoSacado(obj.getCodigo());
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarParceiro() {
		try {
			super.consultar();
			List objs = new ArrayList<>(0);
			if (getCampoConsultaParceiro().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("nome")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("razaoSocial")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("RG")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("CPF")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaParceiro().equals("tipoParceiro")) {
				objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaParceiro(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboParceiro() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	public void selecionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			getConfiguracaoContabilRegraVO().setFuncionarioVO(obj);
			getConfiguracaoContabilRegraVO().setCodigoSacado(obj.getCodigo());
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioPorCodigo() {
		FuncionarioVO funcionario = null;
		try {
			if (!getConfiguracaoContabilRegraVO().getFuncionarioVO().getMatricula().isEmpty()) {
				funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(getConfiguracaoContabilRegraVO().getFuncionarioVO().getMatricula(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (funcionario != null && funcionario.getCodigo().intValue() != 0) {
				getConfiguracaoContabilRegraVO().setFuncionarioVO(funcionario);
				getConfiguracaoContabilRegraVO().setCodigoSacado(funcionario.getCodigo());
				setMensagemID("msg_dados_selecionados");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			getConfiguracaoContabilRegraVO().getFuncionarioVO().setCodigo(0);
			getConfiguracaoContabilRegraVO().getFuncionarioVO().setMatricula("");
			getConfiguracaoContabilRegraVO().getFuncionarioVO().getPessoa().setNome("");
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList<>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public void limparFornecedor() {
		try {
			getConfiguracaoContabilRegraVO().setFornecedorVO(new FornecedorVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFornecedor() {
		try {
			FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			getConfiguracaoContabilRegraVO().setFornecedorVO(obj);
			getConfiguracaoContabilRegraVO().setCodigoSacado(obj.getCodigo());
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List<FornecedorVO> objs = new ArrayList<FornecedorVO>(0);
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("RG")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}

			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList<FornecedorVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("RG", "RG"));

		return itens;
	}

	public void selecionarBanco() {
		try {
			BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("bancoItens");
			getConfiguracaoContabilRegraVO().setCodigoSacado(obj.getCodigo());
			getConfiguracaoContabilRegraVO().setBancoVO(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarBancos() {
		try {
			List objs = new ArrayList<>(0);
			if (getCampoConsultaBanco().equals("nome")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNome(getValorConsultaBanco(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaBanco().equals("nrBanco")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNrBanco(getValorConsultaBanco(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaBanco(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaBanco(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboBancos() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nrBanco", "Número do Banco"));
		return itens;
	}

	public void consultarFormaPagamento() {
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoContabilRegraVO().getFormaPagamentoVO())) {
				getConfiguracaoContabilRegraVO().setFormaPagamentoVO(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getConfiguracaoContabilRegraVO().getFormaPagamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			getConfiguracaoContabilRegraVO().setOperadoraCartaoVO(new OperadoraCartaoVO());
			montarListaSelectItemOperadoraCartao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCamposPorSacado() {
		try {
			getConfiguracaoContabilRegraVO().setFornecedorVO(new FornecedorVO());
			getConfiguracaoContabilRegraVO().setParceiroVO(new ParceiroVO());
			getConfiguracaoContabilRegraVO().setBancoVO(new BancoVO());
			getConfiguracaoContabilRegraVO().setFuncionarioVO(new FuncionarioVO());
			getConfiguracaoContabilRegraVO().setCodigoSacado(0);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCamposPorPlanoConta() {
		try {
			getListaConsultaPlanoConta().clear();
			setCampoConsultaPlanoConta("");
			setValorConsultaPlanoConta("");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCamposPorConfiguracaoContabilRegra() {
		try {
			setConfiguracaoContabilRegraVO(new ConfiguracaoContabilRegraVO());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
			tipoConsultaCombo.add(new SelectItem("layoutIntegracaoContabil", "Layout Integração Contábil"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoContabilCons.xhtml");
	}

	public ConfiguracaoContabilVO getConfiguracaoContabilVO() {
		if (configuracaoContabilVO == null) {
			configuracaoContabilVO = new ConfiguracaoContabilVO();
		}
		return configuracaoContabilVO;
	}

	public void setConfiguracaoContabilVO(ConfiguracaoContabilVO configuracaoContabilVO) {
		this.configuracaoContabilVO = configuracaoContabilVO;
	}

	public ConfiguracaoContabilRegraVO getConfiguracaoContabilRegraVO() {
		if (configuracaoContabilRegraVO == null) {
			configuracaoContabilRegraVO = new ConfiguracaoContabilRegraVO();
		}
		return configuracaoContabilRegraVO;
	}

	public void setConfiguracaoContabilRegraVO(ConfiguracaoContabilRegraVO configuracaoContabilRegraVO) {
		this.configuracaoContabilRegraVO = configuracaoContabilRegraVO;
	}

	public ConfiguracaoContabilRegraPlanoContaVO getConfiguracaoContabilRegraPlanoContaVO() {
		if (configuracaoContabilRegraPlanoContaVO == null) {
			configuracaoContabilRegraPlanoContaVO = new ConfiguracaoContabilRegraPlanoContaVO();
		}
		return configuracaoContabilRegraPlanoContaVO;
	}

	public void setConfiguracaoContabilRegraPlanoContaVO(ConfiguracaoContabilRegraPlanoContaVO configuracaoContabilRegraPlanoContaVO) {
		this.configuracaoContabilRegraPlanoContaVO = configuracaoContabilRegraPlanoContaVO;
	}

	public List<SelectItem> getListaSelectItemLayoutIntegracaoContabil() {
		if (listaSelectItemLayoutIntegracaoContabil == null) {
			listaSelectItemLayoutIntegracaoContabil = new ArrayList<>(0);
		}
		return listaSelectItemLayoutIntegracaoContabil;
	}

	public void setListaSelectItemLayoutIntegracaoContabil(List<SelectItem> listaSelectItemLayoutIntegracaoContabil) {
		this.listaSelectItemLayoutIntegracaoContabil = listaSelectItemLayoutIntegracaoContabil;
	}

	public List<SelectItem> getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList<>(0);
		}
		return listaSelectItemFormaPagamento;
	}

	public void setListaSelectItemFormaPagamento(List<SelectItem> listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	public List<SelectItem> getListaSelectItemOperadoraCartao() {
		if (listaSelectItemOperadoraCartao == null) {
			listaSelectItemOperadoraCartao = new ArrayList<>(0);
		}
		return listaSelectItemOperadoraCartao;
	}

	public void setListaSelectItemOperadoraCartao(List<SelectItem> listaSelectItemOperadoraCartao) {
		this.listaSelectItemOperadoraCartao = listaSelectItemOperadoraCartao;
	}

	public List<SelectItem> getListaSelectItemContaCorrenteOrigem() {
		if (listaSelectItemContaCorrenteOrigem == null) {
			listaSelectItemContaCorrenteOrigem = new ArrayList<>(0);
		}
		return listaSelectItemContaCorrenteOrigem;
	}

	public void setListaSelectItemContaCorrenteOrigem(List<SelectItem> listaSelectItemContaCorrenteOrigem) {
		this.listaSelectItemContaCorrenteOrigem = listaSelectItemContaCorrenteOrigem;
	}

	public List<SelectItem> getListaSelectItemContaCorrenteDestino() {
		if (listaSelectItemContaCorrenteDestino == null) {
			listaSelectItemContaCorrenteDestino = new ArrayList<>(0);
		}
		return listaSelectItemContaCorrenteDestino;
	}

	public void setListaSelectItemContaCorrenteDestino(List<SelectItem> listaSelectItemContaCorrenteDestino) {
		this.listaSelectItemContaCorrenteDestino = listaSelectItemContaCorrenteDestino;
	}

	public List<SelectItem> getListaSelectItemFormaPagamentoMovFin() {
		if (listaSelectItemFormaPagamentoMovFin == null) {
			listaSelectItemFormaPagamentoMovFin = new ArrayList<>(0);
		}
		return listaSelectItemFormaPagamentoMovFin;
	}

	public void setListaSelectItemFormaPagamentoMovFin(List<SelectItem> listaSelectItemFormaPagamentoMovFin) {
		this.listaSelectItemFormaPagamentoMovFin = listaSelectItemFormaPagamentoMovFin;
	}
	
	

	public List<SelectItem> getListaSelectItemFormaPagamentoCartoes() {
		if (listaSelectItemFormaPagamentoCartoes == null) {
			listaSelectItemFormaPagamentoCartoes = new ArrayList<>(0);
		}
		return listaSelectItemFormaPagamentoCartoes;
	}

	public void setListaSelectItemFormaPagamentoCartoes(List<SelectItem> listaSelectItemFormaPagamentoCartoes) {
		this.listaSelectItemFormaPagamentoCartoes = listaSelectItemFormaPagamentoCartoes;
	}

	public List getListaConsultaCentroDespesa() {
		return listaConsultaCentroDespesa;
	}

	public void setListaConsultaCentroDespesa(List listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
	}

	public String getValorConsultaCentroDespesa() {
		return valorConsultaCentroDespesa;
	}

	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
	}

	public String getCampoConsultaCentroDespesa() {
		return campoConsultaCentroDespesa;
	}

	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public List<CentroReceitaVO> getListaConsultaCentroReceita() {
		return listaConsultaCentroReceita;
	}

	public void setListaConsultaCentroReceita(List<CentroReceitaVO> listaConsultaCentroReceita) {
		this.listaConsultaCentroReceita = listaConsultaCentroReceita;
	}

	public String getValorConsultaCentroReceita() {
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}

	public String getCampoConsultaCentroReceita() {
		return campoConsultaCentroReceita;
	}

	public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
		this.campoConsultaCentroReceita = campoConsultaCentroReceita;
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
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getValorConsultaTurno() {
		if (valorConsultaTurno == null) {
			valorConsultaTurno = "";
		}
		return valorConsultaTurno;
	}

	public void setValorConsultaTurno(String valorConsultaTurno) {
		this.valorConsultaTurno = valorConsultaTurno;
	}

	public String getCampoConsultaTurno() {
		if (campoConsultaTurno == null) {
			campoConsultaTurno = "";
		}
		return campoConsultaTurno;
	}

	public void setCampoConsultaTurno(String campoConsultaTurno) {
		this.campoConsultaTurno = campoConsultaTurno;
	}

	public List<TurnoVO> getListaConsultaTurno() {
		if (listaConsultaTurno == null) {
			listaConsultaTurno = new ArrayList<TurnoVO>();
		}
		return listaConsultaTurno;
	}

	public void setListaConsultaTurno(List<TurnoVO> listaConsultaTurno) {
		this.listaConsultaTurno = listaConsultaTurno;
	}

	public String getValorConsultaPlanoConta() {
		if (valorConsultaPlanoConta == null) {
			valorConsultaPlanoConta = "";
		}
		return valorConsultaPlanoConta;
	}

	public void setValorConsultaPlanoConta(String valorConsultaPlanoConta) {
		this.valorConsultaPlanoConta = valorConsultaPlanoConta;
	}

	public String getCampoConsultaPlanoConta() {
		if (campoConsultaPlanoConta == null) {
			campoConsultaPlanoConta = "";
		}
		return campoConsultaPlanoConta;
	}

	public void setCampoConsultaPlanoConta(String campoConsultaPlanoConta) {
		this.campoConsultaPlanoConta = campoConsultaPlanoConta;
	}

	public List<PlanoContaVO> getListaConsultaPlanoConta() {
		if (listaConsultaPlanoConta == null) {
			listaConsultaPlanoConta = new ArrayList<PlanoContaVO>();
		}
		return listaConsultaPlanoConta;
	}

	public void setListaConsultaPlanoConta(List<PlanoContaVO> listaConsultaPlanoConta) {
		this.listaConsultaPlanoConta = listaConsultaPlanoConta;
	}

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<>();
		}
		return listaConsultaParceiro;
	}

	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public List<FornecedorVO> getListaConsultaFornecedor() {
		if (listaConsultaFornecedor == null) {
			listaConsultaFornecedor = new ArrayList<>();
		}
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		if (valorConsultaFornecedor == null) {
			valorConsultaFornecedor = "";
		}
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	public String getCampoConsultaFornecedor() {
		if (campoConsultaFornecedor == null) {
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<>();
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		}
		return "";
	}

	public List<BancoVO> getListaConsultaBanco() {
		if (listaConsultaBanco == null) {
			listaConsultaBanco = new ArrayList<BancoVO>();
		}
		return listaConsultaBanco;
	}

	public void setListaConsultaBanco(List<BancoVO> listaConsultaBanco) {
		this.listaConsultaBanco = listaConsultaBanco;
	}

	public String getValorConsultaBanco() {
		if (valorConsultaBanco == null) {
			valorConsultaBanco = "";
		}
		return valorConsultaBanco;
	}

	public void setValorConsultaBanco(String valorConsultaBanco) {
		this.valorConsultaBanco = valorConsultaBanco;
	}

	public String getCampoConsultaBanco() {
		if (campoConsultaBanco == null) {
			campoConsultaBanco = "";
		}
		return campoConsultaBanco;
	}

	public void setCampoConsultaBanco(String campoConsultaBanco) {
		this.campoConsultaBanco = campoConsultaBanco;
	}

	public String getCampoConsultaCategoriaProduto() {
		if (campoConsultaCategoriaProduto == null) {
			campoConsultaCategoriaProduto = "";
		}
		return campoConsultaCategoriaProduto;
	}

	public void setCampoConsultaCategoriaProduto(String campoConsultaCategoriaProduto) {
		this.campoConsultaCategoriaProduto = campoConsultaCategoriaProduto;
	}

	public String getValorConsultaCategoriaProduto() {
		if (valorConsultaCategoriaProduto == null) {
			valorConsultaCategoriaProduto = "";
		}
		return valorConsultaCategoriaProduto;
	}

	public void setValorConsultaCategoriaProduto(String valorConsultaCategoriaProduto) {
		this.valorConsultaCategoriaProduto = valorConsultaCategoriaProduto;
	}

	public List<CategoriaProdutoVO> getListaConsultaCategoriaProduto() {
		if (listaConsultaCategoriaProduto == null) {
			listaConsultaCategoriaProduto = new ArrayList<CategoriaProdutoVO>();
		}
		return listaConsultaCategoriaProduto;
	}

	public void setListaConsultaCategoriaProduto(List<CategoriaProdutoVO> listaConsultaCategoriaProduto) {
		this.listaConsultaCategoriaProduto = listaConsultaCategoriaProduto;
	}

	public List<SelectItem> getListaSelectItemImposto() {
		if (listaSelectItemImposto == null) {
			listaSelectItemImposto = new ArrayList<SelectItem>();
		}
		return listaSelectItemImposto;
	}

	public void setListaSelectItemImposto(List<SelectItem> listaSelectItemImposto) {
		this.listaSelectItemImposto = listaSelectItemImposto;
	}

	public List<SelectItem> comboboxTipoOrigemContaReceber;
	public List<SelectItem> getComboboxTipoOrigemContaReceber() {
		if(comboboxTipoOrigemContaReceber == null) {
			comboboxTipoOrigemContaReceber = new ArrayList<SelectItem>(0);
			comboboxTipoOrigemContaReceber.add(new SelectItem(null, "Todas"));	
			for(TipoOrigemContaReceber tipoOrigemContaReceber: TipoOrigemContaReceber.values()) {
				comboboxTipoOrigemContaReceber.add(new SelectItem(tipoOrigemContaReceber, tipoOrigemContaReceber.getDescricao()));		
			}
		}
		return comboboxTipoOrigemContaReceber;
	}

	public List<SelectItem> getTipoSacadoReceber() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(null, "Todos"));
		itens.add(new SelectItem(TipoPessoa.FORNECEDOR, TipoPessoa.FORNECEDOR.getDescricao()));
		itens.add(new SelectItem(TipoPessoa.PARCEIRO, TipoPessoa.PARCEIRO.getDescricao()));
		/*
		 * itens.add(new SelectItem(TipoPessoa.ALUNO, TipoPessoa.ALUNO.getDescricao())); itens.add(new SelectItem(TipoPessoa.CANDIDATO, TipoPessoa.CANDIDATO.getDescricao())); itens.add(new SelectItem(TipoPessoa.MEMBRO_COMUNIDADE, TipoPessoa.MEMBRO_COMUNIDADE.getDescricao())); itens.add(new SelectItem(TipoPessoa.PROFESSOR, TipoPessoa.PROFESSOR.getDescricao())); itens.add(new SelectItem(TipoPessoa.REQUERENTE, TipoPessoa.REQUERENTE.getDescricao())); itens.add(new SelectItem(TipoPessoa.RESPONSAVEL_LEGAL, TipoPessoa.RESPONSAVEL_LEGAL.getDescricao())); itens.add(new SelectItem(TipoPessoa.FUNCIONARIO, TipoPessoa.FUNCIONARIO.getDescricao())); itens.add(new SelectItem(TipoPessoa.RESPONSAVEL_FINANCEIRO, TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao())); itens.add(new SelectItem(TipoPessoa.COORDENADOR_CURSO, TipoPessoa.COORDENADOR_CURSO.getDescricao()));
		 */
		return itens;
	}

	public List<SelectItem> comboboxTipoOrigemContaPagar;
	public List<SelectItem> getComboboxTipoOrigemContaPagar() {
		if(comboboxTipoOrigemContaPagar == null) {
			comboboxTipoOrigemContaPagar = new ArrayList<SelectItem>(0);
			comboboxTipoOrigemContaPagar.add(new SelectItem(null, "Todas"));
			for(OrigemContaPagar origemContaPagar: OrigemContaPagar.values()) {
				comboboxTipoOrigemContaPagar.add(new SelectItem(origemContaPagar, origemContaPagar.getDescricao()));
			}
		}

		return comboboxTipoOrigemContaPagar;
	}

	public List<SelectItem> getTipoSacadoPagar() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(null, "Todos"));
		itens.add(new SelectItem(TipoSacado.BANCO, TipoSacado.BANCO.getDescricao()));
		itens.add(new SelectItem(TipoSacado.FORNECEDOR, TipoSacado.FORNECEDOR.getDescricao()));
		itens.add(new SelectItem(TipoSacado.FUNCIONARIO_PROFESSOR, TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao()));
		itens.add(new SelectItem(TipoSacado.PARCEIRO, TipoSacado.PARCEIRO.getDescricao()));
		/*
		 * itens.add(new SelectItem(TipoSacado.ALUNO, TipoSacado.ALUNO.getDescricao())); itens.add(new SelectItem(TipoSacado.RESPONSAVEL_FINANCEIRO, TipoSacado.RESPONSAVEL_FINANCEIRO.getDescricao())); itens.add(new SelectItem(TipoSacado.FUNCIONARIO_PROFESSOR, TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao())); itens.add(new SelectItem(TipoSacado.OPERADORA_CARTAO, TipoSacado.OPERADORA_CARTAO.getDescricao()));
		 */
		return itens;
	}

	public List<SelectItem> getTipoDesconto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(null, "Todos"));
		itens.add(new SelectItem(TipoDesconto.ALUNO, TipoDesconto.ALUNO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.CONVENIO, TipoDesconto.CONVENIO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.CUSTEADO_CONVENIO, TipoDesconto.CUSTEADO_CONVENIO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.INSTITUCIONAL, TipoDesconto.INSTITUCIONAL.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.PROGRESSIVO, TipoDesconto.PROGRESSIVO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.RATEIO, TipoDesconto.RATEIO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.RECEBIMENTO, TipoDesconto.RECEBIMENTO.getDescricao()));
		return itens;
	}
	
	public List<SelectItem> getTipoDescontoPagar() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(null, "Todos"));
		itens.add(new SelectItem(TipoDesconto.PAGAMENTO, TipoDesconto.PAGAMENTO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.ADIANTAMENTO, TipoDesconto.ADIANTAMENTO.getDescricao()));
		return itens;
	}

	public List<SelectItem> getTipoDescontoMultaJuroAcrescimo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(null, "Todos"));
		itens.add(new SelectItem(TipoDesconto.ACRESCIMO, TipoDesconto.ACRESCIMO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.JURO, TipoDesconto.JURO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.MULTA, TipoDesconto.MULTA.getDescricao()));
		return itens;
	}
	
	public List<SelectItem> getTipoDescontoMultaJuroPagar() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(null, "Todos"));
		itens.add(new SelectItem(TipoDesconto.JURO, TipoDesconto.JURO.getDescricao()));
		itens.add(new SelectItem(TipoDesconto.MULTA, TipoDesconto.MULTA.getDescricao()));
		return itens;
	}

	public void consultarUnidadeEnsino() {
		try {
			setListaConsultarUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(new ArrayList<UnidadeEnsinoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getFiltrarUnidadeEnsino() {
		if (filtrarUnidadeEnsino == null) {
			filtrarUnidadeEnsino = false;
		}
		return filtrarUnidadeEnsino;
	}

	public void setFiltrarUnidadeEnsino(Boolean filtrarUnidadeEnsino) {
		this.filtrarUnidadeEnsino = filtrarUnidadeEnsino;
	}

	public Boolean getMarcarTodasUnidadeEnsino() {
		if (marcarTodasUnidadeEnsino == null) {
			marcarTodasUnidadeEnsino = false;
		}
		return marcarTodasUnidadeEnsino;
	}

	public void setMarcarTodasUnidadeEnsino(Boolean marcarTodasUnidadeEnsino) {
		this.marcarTodasUnidadeEnsino = marcarTodasUnidadeEnsino;
	}

	public Integer getColumn() {
		if (getConfiguracaoContabilVO().getUnidadeEnsinoVOs().size() > 3) {
			return 3;
		}
		return getConfiguracaoContabilVO().getUnidadeEnsinoVOs().size();
	}

	public Integer getElement() {
		return getConfiguracaoContabilVO().getUnidadeEnsinoVOs().size();
	}

	public void carregarUnidadeEnsinoParaConfiguracaoContabil() {
		try {
			if (!Uteis.isAtributoPreenchido(getConfiguracaoContabilVO().getUnidadeEnsinoVOs()) && getUnidadeEnsinoLogado().getCodigo() > 0) {
				getUnidadeEnsinoLogado().setFiltrarUnidadeEnsino(true);
				getConfiguracaoContabilVO().getUnidadeEnsinoVOs().add(getUnidadeEnsinoLogadoClone());
			} else {
				List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getConfiguracaoContabilVO().getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				lista.forEach(p -> p.setFiltrarUnidadeEnsino(false));
				getConfiguracaoContabilVO().getUnidadeEnsinoVOs().addAll(lista);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		getConfiguracaoContabilVO().getUnidadeEnsinoVOs().forEach(p -> p.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino()));
	}

}
