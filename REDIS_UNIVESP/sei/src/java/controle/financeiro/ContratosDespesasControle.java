package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas contratosDespesasForm.jsp
 * contratosDespesasCons.jsp) com as funcionalidades da classe <code>ContratosDespesas</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ContratosDespesas
 * @see ContratosDespesasVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContratoDespesaEspecificoVO;
import negocio.comuns.financeiro.ContratosDespesasVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.AutorizacaoPagamentoRelVO;
import relatorio.negocio.jdbc.financeiro.AutorizacaoPagamentoRel;

@Controller("ContratosDespesasControle")
@Scope("viewScope")
@Lazy
public class ContratosDespesasControle extends SuperControleRelatorio implements Serializable {

	protected ContratosDespesasVO contratosDespesasVO;
	private List<SelectItem> listaSelectItemContaCorrente;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List listaConsultaCategoriaDespesa;
	private String valorConsultaCategoriaDespesa;
	private String campoConsultaCategoriaDespesa;
	private List listaConsultaFornecedor;
	private List listaConsultaBanco;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String valorConsultaFornecedor;
	private String valorConsultaBanco;
	private String campoConsultaFornecedor;
	private String campoConsultaBanco;
	private List<SelectItem> listaSelectItemUnidadeEnsinoOrigemDespesa;
	private List<SelectItem> listaSelectItemDepartamento = null;
	private List<SelectItem> listaSelectItemFuncionario = null;
	private int numParcela = 0;
	private String aprovarData = "";
	private Boolean permitirAlterarDataTermino;
	private Double novoValorParcelas;
	private ContratoDespesaEspecificoVO contratoDespesaEspecificoVO;
	private List listaConsultaFuncionario;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaCursoTurno;
	private String valorConsultaCursoTurno;
	private List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno;
	private List<ContaPagarVO> contaPagarVOs;
	private DataModelo centroResultadoDataModelo;
	private CentroResultadoOrigemVO centroResultadoOrigemVO;
	private boolean centroResultadoAdministrativo = false;
	private List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum;
	private List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa;
	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;
	private String situacaoContrato;
	private String tipoContrato;
	private DataModelo dataModelofuncionarioCargo;
	
	public ContratosDespesasControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getControleConsultaOtimizado().setCampoConsulta("favorecido");
		getControleConsultaOtimizado().getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ContratosDespesas</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setContratosDespesasVO(new ContratosDespesasVO());
		permitirAlterarDataTermino = !this.getSomenteLeitura();
		contratosDespesasVO.inicializarUnidadeEnsinoLogado(getUnidadeEnsinoLogadoClone());
		setContratoDespesaEspecificoVO(new ContratoDespesaEspecificoVO());
		setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ContratosDespesas</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		ContratosDespesasVO obj = (ContratosDespesasVO) context().getExternalContext().getRequestMap().get("contratosDespesasItens");
		try {
			obj = getFacadeFactory().getContratosDespesasFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			obj.setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTRATO_DESPESA, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setContaPagarVOs(getFacadeFactory().getContaPagarFacade().consultarPorOrigemContaPagar(obj.getCodigo().toString(), OrigemContaPagar.CONTRATO_DESPESA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasCons.xhtml");
		}
		obj.setNovoObj(Boolean.FALSE);
		setContratosDespesasVO(obj);
		setContratoDespesaEspecificoVO(new ContratoDespesaEspecificoVO());
		setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
		inicializarListasSelectItemTodosComboBox();
		permitirAlterarDataTermino = !this.getSomenteLeitura();
		if (obj.getSituacao().equals("AP")) {
			setMensagemDetalhada("Os dados deste CONTRATO DE DESPESA NÃO PODEM MAIS SEREM ALTERADOS. O mesmo só pode ser REAJUSTADO ou CANCELADO.");
		} else {
			if (obj.getSituacao().equals("IN")) {
				setMensagemDetalhada("Os dados deste CONTRATO DE DESPESA NÃO PODEM MAIS SEREM ALTERADOS, pois o mesmo já está CANCELADO.");
			} else {
				setMensagemID("msg_dados_editar");
			}
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>ContratosDespesas</code>. Caso o objeto seja novo (ainda
	 * não gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (contratosDespesasVO.isNovoObj().booleanValue()) {
				contratosDespesasVO.setSituacao("AP");
				contratosDespesasVO.setDataAprovacao(new Date());
				contratosDespesasVO.getResponsavelAprovacao().setCodigo(getUsuarioLogado().getCodigo());
				contratosDespesasVO.getResponsavelAprovacao().setNome(getUsuarioLogado().getNome());
				getFacadeFactory().getContratosDespesasFacade().incluir(contratosDespesasVO, getUsuarioLogado());
				setContaPagarVOs(getFacadeFactory().getContaPagarFacade().consultarPorOrigemContaPagar(getContratosDespesasVO().getCodigo().toString(), OrigemContaPagar.CONTRATO_DESPESA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			} else {
				getFacadeFactory().getContratosDespesasFacade().alterar(contratosDespesasVO, getUsuarioLogado());
			}
			getContratosDespesasVO().reiniciarControleBloqueioCompetencia();
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasForm.xhtml");
		} catch (Exception e) {
			if (contratosDespesasVO.getSituacao().equals("AP")) {
				contratosDespesasVO.setSituacao("");
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasForm.xhtml");
		}
	}

	public void alterarValorDasParcelasPendentes() {
		try {
			getFacadeFactory().getContratosDespesasFacade().alterarValorDasParcelasPendentes(getContratosDespesasVO(), getNovoValorParcelas(), getUsuarioLogado());
			setContaPagarVOs(null);
			setContaPagarVOs(getFacadeFactory().getContaPagarFacade().consultarPorOrigemContaPagar(getContratosDespesasVO().getCodigo().toString(), OrigemContaPagar.CONTRATO_DESPESA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			getContratosDespesasVO().reiniciarControleBloqueioCompetencia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarGeracaoContratoDespesaEspecificao() {
		try {
			getFacadeFactory().getContratosDespesasFacade().realizarGeracaoContratoDespesaEspecificao(getContratosDespesasVO());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ContratosDespesasCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	@Override
	public String consultar() {
		try {
			getFacadeFactory().getContratosDespesasFacade().consultar(getControleConsultaOtimizado(), getSituacaoContrato(), getTipoContrato(), true, getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasCons.xhtml");
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasCons.xhtml");
		}
	}
	
	public void scroll(DataScrollEvent dataScrollEvent) {
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		consultar();
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ContratosDespesasVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getContratosDespesasFacade().excluir(contratosDespesasVO, getUsuarioLogado());
			setContratosDespesasVO(new ContratosDespesasVO());
			setContratoDespesaEspecificoVO(new ContratoDespesaEspecificoVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasForm.xhtml");
		}
	}

	public void aprovar() {
		try {
			getFacadeFactory().getContratosDespesasFacade().verificarPermissaoAutorizarIndeferir(getContratosDespesasVO(), "AP", getUsuarioLogado());
			setContaPagarVOs(getFacadeFactory().getContaPagarFacade().consultarPorOrigemContaPagar(getContratosDespesasVO().getCodigo().toString(), OrigemContaPagar.CONTRATO_DESPESA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void inicializarDadosCancelamento() {
		try {
			getFacadeFactory().getContratosDespesasFacade().verificarPermissaoAutorizarIndeferir(getContratosDespesasVO(), "IN", getUsuarioLogado());
			setContaPagarVOs(getFacadeFactory().getContaPagarFacade().consultarPorOrigemContaPagar(getContratosDespesasVO().getCodigo().toString(), OrigemContaPagar.CONTRATO_DESPESA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//			setContaPagarVOs(getFacadeFactory().getContaPagarFacade().consultarPorOrigemContaPagar(String.valueOf(contratosDespesasVO.getCodigo()), OrigemContaPagar.CONTRATO_DESPESA.getValor(), SituacaoFinanceira.A_PAGAR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getContratosDespesasVO().setDataCancelamento(new Date());
			realizarDefinicaoContaPagarExcluir();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarDefinicaoContaPagarExcluir() {
		getFacadeFactory().getContratosDespesasFacade().realizarDefinicaoContaPagarExcluir(getContratosDespesasVO(), getContaPagarVOs());
	}

	public void cancelar() {
		try {
			getFacadeFactory().getContratosDespesasFacade().realizarCancelamentoContrato(getContratosDespesasVO(), getContaPagarVOs(), getUsuarioLogado());
			setContaPagarVOs(getFacadeFactory().getContaPagarFacade().consultarPorOrigemContaPagar(getContratosDespesasVO().getCodigo().toString(), OrigemContaPagar.CONTRATO_DESPESA.getValor(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			// setMensagemDetalhada("msg_dados_editar",
			// "Informe a Data de Término do CONTRATO. A partir desta data, todas as CONTA A PAGAR referentes ao mesmo serão canceladas.");
			// permitirAlterarDataTermino = true;
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCategoriaDespesa() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCategoriaDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaDespesa().equals("identificadorCategoriaDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCategoriaDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCategoriaDespesa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboCategoriaDespesa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCategoriaDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		this.getContratosDespesasVO().setFornecedor(obj);
	}

	public void selecionarBanco() {
		BancoVO obj = (BancoVO) context().getExternalContext().getRequestMap().get("bancoItens");
		this.getContratosDespesasVO().setBanco(obj);
	}

	public void selecionarCategoriaDespesa() {
		try {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItens");
		this.getCentroResultadoOrigemVO().setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		limparDadosPorCategoriaDespesa();
		this.getCentroResultadoOrigemVO().getUnidadeEnsinoVO().setCodigo(getContratosDespesasVO().getUnidadeEnsino().getCodigo());
		montarListaSelectItemTipoNivelCentroResultadoEnum();
		if(getListaSelectItemUnidadeEnsino().isEmpty()) {
			montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
		}
		getCentroResultadoOrigemVO().setPorcentagem(100.0);
		getCentroResultadoOrigemVO().calcularValor(1000.0);
		preencherDadosPorCategoriaDespesa();
		setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	
	public void consultarFornecedor() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboFornecedor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public void consultarBancos() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaBanco().equals("nome")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNome(getValorConsultaBanco(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaBanco().equals("nrBanco")) {
				objs = getFacadeFactory().getBancoFacade().consultarPorNrBanco(getValorConsultaBanco(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaBanco(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaBanco(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboBancos() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nrBanco", "Número do Banco"));
		return itens;
	}

	public boolean isCampoFornecedor() {
		if (getContratosDespesasVO().getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
			return true;
		}
		return false;
	}

	public boolean isCampoBanco() {
		if (getContratosDespesasVO().getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
			return true;
		}
		return false;
	}

	public boolean isCampoFuncionario() {
		if (getContratosDespesasVO().getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
			return true;
		}
		return false;
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>ContratoDespesaEspecifico</code> para o objeto
	 * <code>contratoDespesaVO</code> da classe <code>ContratoDespesa</code>
	 */
	public void adicionarContratoDespesaEspecifico() throws Exception {
		try {
			if (!getContratosDespesasVO().getCodigo().equals(0)) {
				contratoDespesaEspecificoVO.setContratoDespesa(getContratosDespesasVO().getCodigo());
			}
			getContratosDespesasVO().adicionarObjContratoDespesaEspecificoVOs(getContratoDespesaEspecificoVO());
			this.setContratoDespesaEspecificoVO(new ContratoDespesaEspecificoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ContratoDespesaEspecifico</code> para edição pelo usuário.
	 */
	public String editarContratoDespesaEspecifico() throws Exception {
		ContratoDespesaEspecificoVO obj = (ContratoDespesaEspecificoVO) context().getExternalContext().getRequestMap().get("contratoDespesaEspecificoItens");
		setContratoDespesaEspecificoVO(obj);
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasForm.xhtml");
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ContratoDespesaEspecifico</code> do objeto
	 * <code>contratoDespesaVO</code> da classe <code>ContratoDespesa</code>
	 */
	public String removerContratoDespesaEspecifico() throws Exception {
		ContratoDespesaEspecificoVO obj = (ContratoDespesaEspecificoVO) context().getExternalContext().getRequestMap().get("contratoDespesaEspecificoItens");
		getContratosDespesasVO().excluirObjContratoDespesaEspecificoVOs(obj.getNrParcela());
		setMensagemID("msg_dados_excluidos");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasForm.xhtml");
	}

	public void irPaginaInicial() throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(0);
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
	 * ComboBox correspondente ao atributo <code>tipoContrato</code>
	 */
	public List getListaSelectItemTipoContratoContratosDespesas() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoContratoDespesass = (Hashtable) Dominios.getTipoContratoDespesas();
		Enumeration keys = tipoContratoDespesass.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoContratoDespesass.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemSituacaoContratosDespesas() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoContratoDespesas = (Hashtable) Dominios.getSituacaoContratosDespesas();
		Enumeration keys = situacaoContratoDespesas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoContratoDespesas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>numero</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, getContratosDespesasVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>ContaCorrente</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNumero(prm);
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", !Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado())));
		} catch (Exception e) {
			throw e;
		} 
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>ContaCorrente</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNumero(String numeroPrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(numeroPrm, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();		
		montarSelectItemDepartamento();
		montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("favorecido", "Favorecido"));
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("consultarPorIdentificadorCategoriaDespesa", "Identificador Categoria Despesa"));
		itens.add(new SelectItem("consultarPordescricaoCategoriaDespesa", "Descrição Categoria Despesa"));
		return itens;
	}

	public boolean isCampoSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("situacao")) {
			return true;
		}
		return false;
	}

	public boolean isCampoTipoContrato() {
		if (getControleConsulta().getCampoConsulta().equals("tipoContrato")) {
			return true;
		}
		return false;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {				
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("contratosDespesasCons.xhtml");
	}

	public List<SelectItem> listaSelectItemTipoSacado;

	public List<SelectItem> getListaSelectItemTipoSacado() throws Exception {
		if (listaSelectItemTipoSacado == null) {
			listaSelectItemTipoSacado = new ArrayList<SelectItem>(0);
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.FORNECEDOR.getValor(), TipoSacado.FORNECEDOR.getDescricao()));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.BANCO.getValor(), TipoSacado.BANCO.getDescricao()));
			listaSelectItemTipoSacado.add(new SelectItem(TipoSacado.FUNCIONARIO_PROFESSOR.getValor(), TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao()));
		}

		// Hashtable listaTipoSacado = (Hashtable) Dominios.getTipoSacado();
		// Enumeration keys = listaTipoSacado.keys();
		// while (keys.hasMoreElements()) {
		// String value = (String) keys.nextElement();
		// String label = (String) listaTipoSacado.get(value);
		// objs.add(new SelectItem(value, label));
		// }
		// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		// Collections.sort((List) objs, ordenador);
		return listaSelectItemTipoSacado;
	}

	

	public void montarSelectItemDepartamento() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, ""));
		List<DepartamentoVO> listaDepartamento = null;
		listaDepartamento = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoUnidadeEnsinoESemUE(contratosDespesasVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		listaDepartamento.forEach(d -> {
			objs.add(new SelectItem(d.getCodigo(), d.getNome()));
		});
		setListaSelectItemDepartamento(objs);
	}

	public void consultarFuncionarioCentroCusto() {
		try {
			getDataModelofuncionarioCargo().setLimitePorPagina(10);
			getDataModelofuncionarioCargo().getUnidadeEnsinoVO().setCodigo(getContratosDespesasVO().getUnidadeEnsino().getCodigo());
			if (getDataModelofuncionarioCargo().getCampoConsulta().equals("nomeFuncionario")) {
				getDataModelofuncionarioCargo().setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade()
						.consultarPorNomeFuncionarioAtivo(getDataModelofuncionarioCargo(),
								Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getDataModelofuncionarioCargo()
						.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade()
								.consultarTotalPorNomeFuncionarioAtivo(getDataModelofuncionarioCargo(),
										Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}

			if (getDataModelofuncionarioCargo().getCampoConsulta().equals("matricula")) {
				getDataModelofuncionarioCargo().setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade()
						.consultarPorMatriculaCargo(getDataModelofuncionarioCargo().getValorConsulta(),
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerFuncionario(DataScrollEvent dataScrollerEvent) {
		try {
			getDataModelofuncionarioCargo().setPaginaAtual(dataScrollerEvent.getPage());
			getDataModelofuncionarioCargo().setPage(dataScrollerEvent.getPage());
			consultarFuncionarioCentroCusto();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFuncionarioCentroCusto() throws Exception {
		try {
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCentroCustoItens");
			getCentroResultadoOrigemVO().setFuncionarioCargoVO(obj);
			preencherDadosPorCategoriaDespesa();
			
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboFuncionarioCentroCusto() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomeFuncionario", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}


	public void alterarTipoSacado() {
		getContratosDespesasVO().setFuncionario(new FuncionarioVO());
		getContratosDespesasVO().setFornecedor(new FornecedorVO());		
	}

	public boolean getSomenteLeitura() {
		return this.getContratosDespesasVO().getDadosBasicosDisponiveisSomenteConsulta();
	}

	public boolean getApresentarComboDepartamento() {
		try {
			return (getCentroResultadoOrigemVO().getCategoriaDespesaVO().getApresentarDepartamento());			
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getApresentarContratoIndeterminado() {
		try {
			return getContratosDespesasVO().getApresentarContratoIndeterminado();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getApresentarRichModalFuncionario() {
		try {
			return (getCentroResultadoOrigemVO().getCategoriaDespesaVO().getApresentarFuncionario());
			
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getApresentarComboTurma() {
		try {
			return (getCentroResultadoOrigemVO().getCategoriaDespesaVO().getApresentarTurma());
		
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getApresentarCommandButtonindeferir() {
		try {
			if (contratosDespesasVO.isNovoObj().booleanValue()) {
				return false;
			} else {
				if (contratosDespesasVO.getSituacao().equals("IN")) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
	}

	public List consultarUnidadeEnsinoPorCodigo(Integer numeroPrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public ContratosDespesasVO getContratosDespesasVO() {
		if(contratosDespesasVO == null){
			contratosDespesasVO = new ContratosDespesasVO();
		}
		return contratosDespesasVO;
	}

	public void setContratosDespesasVO(ContratosDespesasVO contratosDespesasVO) {
		this.contratosDespesasVO = contratosDespesasVO;
	}

	public ContratoDespesaEspecificoVO getContratoDespesaEspecificoVO() {
		return contratoDespesaEspecificoVO;
	}

	public void setContratoDespesaEspecificoVO(ContratoDespesaEspecificoVO contratoDespesaEspecificoVO) {
		this.contratoDespesaEspecificoVO = contratoDespesaEspecificoVO;
	}

	public String getCampoConsultaCategoriaDespesa() {
		return campoConsultaCategoriaDespesa;
	}

	public void setCampoConsultaCategoriaDespesa(String campoConsultaCategoriaDespesa) {
		this.campoConsultaCategoriaDespesa = campoConsultaCategoriaDespesa;
	}

	public String getCampoConsultaFornecedor() {
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public List getListaConsultaCategoriaDespesa() {
		return listaConsultaCategoriaDespesa;
	}

	public void setListaConsultaCategoriaDespesa(List listaConsultaCategoriaDespesa) {
		this.listaConsultaCategoriaDespesa = listaConsultaCategoriaDespesa;
	}

	public List getListaConsultaFornecedor() {
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public String getValorConsultaCategoriaDespesa() {
		return valorConsultaCategoriaDespesa;
	}

	public void setValorConsultaCategoriaDespesa(String valorConsultaCategoriaDespesa) {
		this.valorConsultaCategoriaDespesa = valorConsultaCategoriaDespesa;
	}

	public String getValorConsultaFornecedor() {
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Boolean getHabilitarComboBoxUnidadeEnsino() throws Exception {
		Boolean retorno = contratosDespesasVO.getHabilitarComboBoxUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
		return retorno;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		contratosDespesasVO = null;
		Uteis.liberarListaMemoria(listaSelectItemContaCorrente);
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		contratoDespesaEspecificoVO = null;
	}

	/**
	 * @return the campoConsultaBanco
	 */
	public String getCampoConsultaBanco() {
		return campoConsultaBanco;
	}

	/**
	 * @param campoConsultaBanco
	 *            the campoConsultaBanco to set
	 */
	public void setCampoConsultaBanco(String campoConsultaBanco) {
		this.campoConsultaBanco = campoConsultaBanco;
	}

	/**
	 * @return the valorConsultaBanco
	 */
	public String getValorConsultaBanco() {
		return valorConsultaBanco;
	}

	/**
	 * @param valorConsultaBanco
	 *            the valorConsultaBanco to set
	 */
	public void setValorConsultaBanco(String valorConsultaBanco) {
		this.valorConsultaBanco = valorConsultaBanco;
	}

	/**
	 * @return the listaConsultaBanco
	 */
	public List getListaConsultaBanco() {
		return listaConsultaBanco;
	}

	/**
	 * @param listaConsultaBanco
	 *            the listaConsultaBanco to set
	 */
	public void setListaConsultaBanco(List listaConsultaBanco) {
		this.listaConsultaBanco = listaConsultaBanco;
	}

	/**
	 * @return the unidadeEnsinoOrigemDespesaFacade
	 */
	public List getListaSelectItemUnidadeEnsinoOrigemDespesa() {
		if (listaSelectItemUnidadeEnsinoOrigemDespesa == null) {
			listaSelectItemUnidadeEnsinoOrigemDespesa = new ArrayList();
		}
		return listaSelectItemUnidadeEnsinoOrigemDespesa;
	}

	/**
	 * @param unidadeEnsinoOrigemDespesaFacade
	 *            the unidadeEnsinoOrigemDespesaFacade to set
	 */
	public void setListaSelectItemUnidadeEnsinoOrigemDespesa(List listaSelectItemUnidadeEnsinoOrigemDespesa) {
		this.listaSelectItemUnidadeEnsinoOrigemDespesa = listaSelectItemUnidadeEnsinoOrigemDespesa;
	}

	/**
	 * @return the listaSelectItemDepartamento
	 */
	public List getListaSelectItemDepartamento() {
		return listaSelectItemDepartamento;
	}

	/**
	 * @param listaSelectItemDepartamento
	 *            the listaSelectItemDepartamento to set
	 */
	public void setListaSelectItemDepartamento(List listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	/**
	 * @return the valorConsultaFuncionario
	 */
	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	/**
	 * @param valorConsultaFuncionario
	 *            the valorConsultaFuncionario to set
	 */
	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	/**
	 * @return the campoConsultaFuncionario
	 */
	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	/**
	 * @param campoConsultaFuncionario
	 *            the campoConsultaFuncionario to set
	 */
	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	/**
	 * @return the listaSelectItemFuncionario
	 */
	public List getListaSelectItemFuncionario() {
		return listaSelectItemFuncionario;
	}

	/**
	 * @param listaSelectItemFuncionario
	 *            the listaSelectItemFuncionario to set
	 */
	public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
	}

	public List getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	/**
	 * @return the aprovarData
	 */
	public String getAprovarData() {
		return aprovarData;
	}

	/**
	 * @param aprovarData
	 *            the aprovarData to set
	 */
	public void setAprovarData(String aprovarData) {
		this.aprovarData = aprovarData;
	}

	/**
	 * @return the permitirAlterarDataTermino
	 */
	public Boolean getPermitirAlterarDataTermino() {
		return permitirAlterarDataTermino;
	}

	/**
	 * @param permitirAlterarDataTermino
	 *            the permitirAlterarDataTermino to set
	 */
	public void setPermitirAlterarDataTermino(Boolean permitirAlterarDataTermino) {
		this.permitirAlterarDataTermino = permitirAlterarDataTermino;
	}

	/**
	 * @return the novoValorParcelas
	 */
	public Double getNovoValorParcelas() {
		return novoValorParcelas;
	}

	public void setNovoValorParcelas(Double novoValorParcelas) {
		this.novoValorParcelas = novoValorParcelas;
	}

	public boolean getIsApresentarCampoCurso() {
		return getCentroResultadoOrigemVO().getCategoriaDespesaVO().getInformarTurma() != null && getCentroResultadoOrigemVO().getCategoriaDespesaVO().getInformarTurma().equals("CU");
	}

	public boolean getIsApresentarCampoCursoTurno() {
		return getCentroResultadoOrigemVO().getCategoriaDespesaVO().getInformarTurma() != null && getCentroResultadoOrigemVO().getCategoriaDespesaVO().getInformarTurma().equals("CT");
	}

	public boolean getIsApresentarCampoTurma() {
		return getCentroResultadoOrigemVO().getCategoriaDespesaVO().getInformarTurma() != null && getCentroResultadoOrigemVO().getCategoriaDespesaVO().getInformarTurma().equals("TU");
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getTipoConsultaComboCursoTurno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turno", "Turno"));
		return itens;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getContratosDespesasVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getCentroResultadoOrigemVO().setTurmaVO(obj);
			getCentroResultadoOrigemVO().setCursoVO(obj.getCurso());
			getCentroResultadoOrigemVO().setTurnoVO(obj.getTurno());
			preencherDadosPorCategoriaDespesa();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
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

	public void consultarCurso() {
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getContratosDespesasVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getCentroResultadoOrigemVO().setCursoVO(obj);
			preencherDadosPorCategoriaDespesa();
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
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

	public String getCampoConsultaCursoTurno() {
		if (campoConsultaCursoTurno == null) {
			campoConsultaCursoTurno = "";
		}
		return campoConsultaCursoTurno;
	}

	public void setCampoConsultaCursoTurno(String campoConsultaCursoTurno) {
		this.campoConsultaCursoTurno = campoConsultaCursoTurno;
	}

	public String getValorConsultaCursoTurno() {
		if (valorConsultaCursoTurno == null) {
			valorConsultaCursoTurno = "";
		}
		return valorConsultaCursoTurno;
	}

	public void setValorConsultaCursoTurno(String valorConsultaCursoTurno) {
		this.valorConsultaCursoTurno = valorConsultaCursoTurno;
	}

	public void consultarCursoTurno() {
		try {
			setListaConsultaCursoTurno(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(), getValorConsultaCursoTurno(), getContratosDespesasVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCursoTurno(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCursoTurno() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
			getCentroResultadoOrigemVO().setCursoVO(obj.getCurso());
			getCentroResultadoOrigemVO().setTurnoVO(obj.getTurno());
			preencherDadosPorCategoriaDespesa();
			setListaConsultaCursoTurno(null);
			this.setValorConsultaCursoTurno("");
			this.setCampoConsultaCursoTurno("");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCursoTurno() {
		if (listaConsultaCursoTurno == null) {
			listaConsultaCursoTurno = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCursoTurno;
	}

	public void setListaConsultaCursoTurno(List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno) {
		this.listaConsultaCursoTurno = listaConsultaCursoTurno;
	}

	public List<ContaPagarVO> getContaPagarVOs() {
		if (contaPagarVOs == null) {
			contaPagarVOs = new ArrayList<ContaPagarVO>(0);
		}
		return contaPagarVOs;
	}

	public void setContaPagarVOs(List<ContaPagarVO> contaPagarVOs) {
		this.contaPagarVOs = contaPagarVOs;
	}

	public void consultarFuncionario() {
		try {
			List objs = null;

			getFacadeFactory().getFuncionarioFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		this.getContratosDespesasVO().setFuncionario(obj);
//		this.getContratosDespesasVO().getPessoa().setCodigo(obj.getPessoa().getCodigo());
//		this.getContratosDespesasVO().getPessoa().setNome(obj.getPessoa().getNome());
		// montarSelectItemCentroCusto();
		Uteis.liberarListaMemoria(getListaConsultaFuncionario());
		campoConsultaFuncionario = null;
		valorConsultaFuncionario = null;
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	
	public void imprimirAutorizacaoPagamento() {
		ContaPagarVO contaPagarVO = (ContaPagarVO) getRequestMap().get("contaPagarItens");
		
		String titulo = "Autorização de Pagamento";
		String design = AutorizacaoPagamentoRel.getDesignIReportRelatorio();
		List<AutorizacaoPagamentoRelVO> listaRegistro = new ArrayList<AutorizacaoPagamentoRelVO>(0);
		try {
			contaPagarVO = getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(contaPagarVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "AutorizacaoPagamentoRelControle", "Inicializando Geração de Relatório Autorização de Pagamento", "Emitindo Relatório");
			listaRegistro.add(getFacadeFactory().getAutorizacaoPagamentoRelFacade().criarObjeto(contaPagarVO, getUsuarioLogado()));
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(AutorizacaoPagamentoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setListaObjetos(listaRegistro);
			getSuperParametroRelVO().setUnidadeEnsino(contaPagarVO.getUnidadeEnsino().getNome());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(AutorizacaoPagamentoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaRegistro.size());
			realizarImpressaoRelatorio();
			
			registrarAtividadeUsuario(getUsuarioLogado(), "AutorizacaoPagamentoRelControle", "Finalizando Geração de Relatório Autorização de Pagamento", "Emitindo Relatório");
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			design = null;
			Uteis.liberarListaMemoria(listaRegistro);
		}

	}
	
	public Boolean getIsApresentarVencimentoAnualMensal(){
		return getContratosDespesasVO().getAnual() || getContratosDespesasVO().getMensal();
	}
	
	public void liberarRegistroCompetenciaFechada() {
		try {
			this.getContratosDespesasVO().setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);		
			FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(this.getContratosDespesasVO().getFechamentoMesVOBloqueio(), getUsuarioLogado(), TipoOrigemHistoricoBloqueioEnum.APAGAR, this.getContratosDespesasVO().getDescricaoBloqueio(), this.getContratosDespesasVO().toString());
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluir(historico, getUsuarioLogado());
			setMensagemID("msg_registro_liberado_mes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getContratosDespesasVO().setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoLiberarBloqueioCompetencia() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberacaoBloqueioPorFechamentoMes(), this.getSenhaLiberacaoBloqueioPorFechamentoMes(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("FuncionarioMes_liberarBloqueioIncluirAlterarContaPagar", usuarioVerif);
			liberarRegistroCompetenciaFechada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
 	}	
	
    public Boolean getApresentarBotaoLiberarBloqueio() {
    	return this.getContratosDespesasVO().getApresentarBotaoLiberarBloqueioFechamentoMes();
    }
    
    public CentroResultadoOrigemVO getCentroResultadoOrigemVO() {
		centroResultadoOrigemVO = Optional.ofNullable(centroResultadoOrigemVO).orElse(new CentroResultadoOrigemVO());
		return centroResultadoOrigemVO;
	}

	public void setCentroResultadoOrigemVO(CentroResultadoOrigemVO centroResultadoOrigemVO) {
		this.centroResultadoOrigemVO = centroResultadoOrigemVO;
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public boolean isCentroResultadoAdministrativo() {
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(boolean centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}
	
	public void inicializarDadoConsultaCentroResultadoAdministrativo() {
		try {
			setCentroResultadoAdministrativo(true);
			inicializarDadosComunsCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}	

	private void inicializarDadosComunsCentroResultado() {
		setCentroResultadoDataModelo(new DataModelo());
		getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			if (isCentroResultadoAdministrativo()) {
				getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(obj);
			} 
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
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, getCentroResultadoOrigemVO().getDepartamentoVO(), getCentroResultadoOrigemVO().getCursoVO(), getCentroResultadoOrigemVO().getTurmaVO(), getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getCentroResultadoDataModelo().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	private void validarValorPorcentagemCentroResultadoOrigem(CentroResultadoOrigemVO centroResultadoOrigemVO) throws Exception {
		Double valor = Uteis.arrendondarForcando2CadasDecimais(centroResultadoOrigemVO.getValor());
		if (valor.doubleValue() == 0.00) {
			throw new ConsistirException("O valor informado/gerado é menor que R$ 0,01.");
		}
	}
	
	private Integer indiceSubstituirCentroResultadoLista(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem) {
		return IntStream.range(0, listaCentroResultadoOrigem.size()).filter(i -> listaCentroResultadoOrigem.get(i).isEdicaoManual()).findFirst().orElse(-1);
	}
	
	public void calcularPorcentagemCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().calcularPorcentagem(getContratosDespesasVO().getValorParcela());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void calcularValorCentroResultadoOrigem() {
		try {
			if(!getContratosDespesasVO().getEspecifico()) {
				getCentroResultadoOrigemVO().calcularValor(getContratosDespesasVO().getValorParcela());
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void addCentroResultadoOrigem() {
		try {
			getCentroResultadoOrigemVO().setQuantidade(1.0);
			getCentroResultadoOrigemVO().setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
			Integer indiceSubstituirCentroResultado = indiceSubstituirCentroResultadoLista(getContratosDespesasVO().getListaCentroResultadoOrigemVOs());
			if (indiceSubstituirCentroResultado >= 0 && indiceSubstituirCentroResultado < getContratosDespesasVO().getListaCentroResultadoOrigemVOs().size()) {
				getContratosDespesasVO().getListaCentroResultadoOrigemVOs().set(indiceSubstituirCentroResultado, getCentroResultadoOrigemVO());
			}
			/**
			 * O valor é passado 1000 por default, mas na verdade não importa o valor e sim a % visto que cada parcela poderá ter um valor diferenciado
			 */
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(getContratosDespesasVO().getListaCentroResultadoOrigemVOs(), getCentroResultadoOrigemVO(), 1000.0, false, getUsuarioLogado());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			getContratosDespesasVO().getListaCentroResultadoOrigemVOs().forEach(cro -> cro.setEdicaoManual(cro.equalsCentroResultadoOrigem(getCentroResultadoOrigemVO())));
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarCentroResultadoOrigem() {
		try {
			CentroResultadoOrigemVO crovo = (CentroResultadoOrigemVO) context().getExternalContext().getRequestMap().get("centroResultadoOrigemItem");
			getContratosDespesasVO().getListaCentroResultadoOrigemVOs().forEach(cro -> cro.setEdicaoManual(cro.equalsCentroResultadoOrigem(crovo)));
			setCentroResultadoOrigemVO(crovo.getClone());
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerCentroResultadoOrigem() {
		try {
			CentroResultadoOrigemVO centroResultadoOrigem = (CentroResultadoOrigemVO) context().getExternalContext().getRequestMap().get("centroResultadoOrigemItem");
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigem(getContratosDespesasVO().getListaCentroResultadoOrigemVOs(), centroResultadoOrigem, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void preencherDadosPorCategoriaDespesa() {
		try {
			if(Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getCategoriaDespesaVO())){
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().preencherDadosPorCategoriaDespesa(getCentroResultadoOrigemVO(), getUsuarioLogado());	
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	private void montarListaSelectItemTipoNivelCentroResultadoEnum() {
		try {
			getListaSelectItemTipoNivelCentroResultadoEnum().clear();
			if(getCentroResultadoOrigemVO().isCategoriaDespesaInformada()){
				getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(getCentroResultadoOrigemVO().getCategoriaDespesaVO(), getListaSelectItemTipoNivelCentroResultadoEnum());
				if(!getListaSelectItemTipoNivelCentroResultadoEnum().isEmpty()){
					getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) getListaSelectItemTipoNivelCentroResultadoEnum().get(0).getValue());	
				} else {
					getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
				}
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	public List<SelectItem> getListaSelectItemTipoNivelCentroResultadoEnum() {
		listaSelectItemTipoNivelCentroResultadoEnum = Optional.ofNullable(listaSelectItemTipoNivelCentroResultadoEnum).orElse(new ArrayList<>());
		return listaSelectItemTipoNivelCentroResultadoEnum;
	}

	public void setListaSelectItemTipoNivelCentroResultadoEnum(List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) {
		this.listaSelectItemTipoNivelCentroResultadoEnum = listaSelectItemTipoNivelCentroResultadoEnum;
	}
	
	public void limparDadosPorCategoriaDespesa() {
		getCentroResultadoOrigemVO().setUnidadeEnsinoVO(null);
		getCentroResultadoOrigemVO().setDepartamentoVO(null);
		getCentroResultadoOrigemVO().setFuncionarioCargoVO(null);
		getCentroResultadoOrigemVO().setTurmaVO(null);
		getCentroResultadoOrigemVO().setCursoVO(null);
		getCentroResultadoOrigemVO().setTurnoVO(null);
		getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(null);
	}
	
	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		try {
			getCentroResultadoOrigemVO().limparCamposPorTipoNivelCentroResultadoEnum();
			preencherDadosPorCategoriaDespesa();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsinoCategoriaDespesa() {
		listaSelectItemUnidadeEnsinoCategoriaDespesa = Optional.ofNullable(listaSelectItemUnidadeEnsinoCategoriaDespesa).orElse(new ArrayList<>());
		return listaSelectItemUnidadeEnsinoCategoriaDespesa;
	}

	public void setListaSelectItemUnidadeEnsinoCategoriaDespesa(List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa) {
		this.listaSelectItemUnidadeEnsinoCategoriaDespesa = listaSelectItemUnidadeEnsinoCategoriaDespesa;
	}
	
	public void montarListaSelectItemUnidadeEnsinoCategoriaDespesa() throws Exception {
		getListaSelectItemUnidadeEnsinoCategoriaDespesa().clear();
		if(Uteis.isAtributoPreenchido(getContratosDespesasVO().getUnidadeEnsino())){
			getCentroResultadoOrigemVO().setUnidadeEnsinoVO((UnidadeEnsinoVO)getContratosDespesasVO().getUnidadeEnsino().clone());
		}
		if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
			getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
			return;
		}
		getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(0, ""));
		List<UnidadeEnsinoVO> listaUnidadeEnsino = consultarUnidadeEnsinoPorCodigo(0);
		listaUnidadeEnsino.stream().forEach(p -> getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(p.getCodigo(), p.getNome())));

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
			listaConsultaDepartamento = new ArrayList<DepartamentoVO>(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}
	

	public String consultarDepartamento() {
		try {
			List objs = new ArrayList(0);
			Integer unidadeEnsino = 0;
			if (Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO())) {
				unidadeEnsino = getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo();
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

	public void selecionarDepartamento() {
		try {
			DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
			getCentroResultadoOrigemVO().setDepartamentoVO(obj);
			preencherDadosPorCategoriaDespesa();
			setCampoConsultaDepartamento("");
			setValorConsultaDepartamento("");
			getListaConsultaDepartamento().clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public List getTipoConsultaComboDepartamento() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public String getSituacaoContrato() {
		if(situacaoContrato == null){
			situacaoContrato = "";
		}
		return situacaoContrato;
	}

	public void setSituacaoContrato(String situacaoContrato) {
		this.situacaoContrato = situacaoContrato;
	}

	public String getTipoContrato() {
		if(tipoContrato == null){
			tipoContrato = "";
		}
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	

	public Boolean permiteAlterarCentroResultado;
	public Boolean getPermiteAlterarCentroResultado() {
			if(permiteAlterarCentroResultado == null) {
				try {
					ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_CONTRATO_DESPESA, getUsuarioLogado());
					permiteAlterarCentroResultado = true;
				} catch (Exception e) {
					permiteAlterarCentroResultado = false;
				}
			}
			return permiteAlterarCentroResultado;
	}
	

	public void addCentroResultadoOrigemConformeRateioCategoriaDespesa() {
		try {
			getCentroResultadoOrigemVO().setQuantidade(1.0);
			getCentroResultadoOrigemVO().setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemPorRateioCategoriaDespesa(getContratosDespesasVO().getListaCentroResultadoOrigemVOs(), getCentroResultadoOrigemVO(), 1000.00, true, getUsuarioLogado());
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public DataModelo getDataModelofuncionarioCargo() {
		if (dataModelofuncionarioCargo == null) {
			dataModelofuncionarioCargo = new DataModelo();
		}
		return dataModelofuncionarioCargo;
	}

	public void setDataModelofuncionarioCargo(DataModelo dataModelofuncionarioCargo) {
		this.dataModelofuncionarioCargo = dataModelofuncionarioCargo;
	}
	
}