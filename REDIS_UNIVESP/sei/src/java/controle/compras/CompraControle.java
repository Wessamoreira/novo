package controle.compras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.CondicaoPagamentoVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("CompraControle")
@Scope("viewScope")
@Lazy
public class CompraControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6758395253641980175L;
	private CompraVO compraVO;
	private Integer recebimentoCompraVO;

	private List<SelectItem> listaSelectItemCotacao;
	private List<SelectItem> listaSelectItemFormaPagamento;
	private List<SelectItem> listaSelectItemCondicaoPagamento;
	private List<SelectItem> listaSelectItemContaCorrente;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDepartamentoFornecedor;
	private List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum;
	private String valorConsultaSituacaoFinanceira;
	private String valorConsultaSituacaoEntregaRecebimento;
	private CompraItemVO compraItemVO;

	private String valorConsultaCategoriaDespesa;
	private String campoConsultaCategoriaDespesa;
	private List<CategoriaDespesaVO> listaConsultaCategoriaDespesa;

	private String campoConsultaCategoriaProduto;
	private String valorConsultaCategoriaProduto;
	private List<CategoriaProdutoVO> listaConsultaCategoriaProduto;

	private List<FornecedorVO> listaConsultaFornecedor;
	private String valorConsultaFornecedor;
	private String campoConsultaFornecedor;

	private String campoConsultaProduto;
	private String valorConsultaProduto;
	private List<ProdutoServicoVO> listaConsultaProduto;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;

	private String campoConsultaCursoTurno;
	private String valorConsultaCursoTurno;
	private List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno;
	private DataModelo centroResultadoDataModelo;
	private boolean centroResultadoAdministrativo = false;

	private String oncompletGravar;
	private Boolean abrirPanelGravar;

	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	
	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;
	private boolean habilitarModalRequisicao = false;
	private List<RequisicaoVO> listaRequisicaoVO;
	private String motivoExclusaoCompra;
	private ProdutoServicoVO produtoServico;

	public CompraControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public void inicializarResponsavel() {
		try {
			compraVO.setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogadoClone(), "CompraControle", "Novo Compra", "Novo");
		removerObjetoMemoria(this);
		setCompraVO(new CompraVO());		
		inicializarListasSelectItemTodosComboBox();
		setCompraItemVO(new CompraItemVO());
		inicializarResponsavel();
		setHabilitarModalRequisicao(false);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("compraForm.xhtml");
	}

	
	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Inicializando Editar Compra", "Editando");
		CompraVO obj = (CompraVO) context().getExternalContext().getRequestMap().get("compraItens");
		setCompraVO(getFacadeFactory().getCompraFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		montarListaCondicaoPagamento();
		montarRecebimentoCompra();
		inicializarListasSelectItemTodosComboBox();
		setCompraItemVO(new CompraItemVO());
		setHabilitarModalRequisicao(false);
		registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Finalizando Editar Compra", "Editando");
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("compraForm.xhtml");
	}

	public CompraVO montarCompraCompletaParaEdicao(CompraVO obj) {
		try {
			
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return new CompraVO();
	}

	public void montarRecebimentoCompra() {
		try {
			getCompraVO().setRecebimentoCompraVOs(getFacadeFactory().getRecebimentoCompraFacade().consultarPorCodigoCompra(getCompraVO().getCodigo(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Compra</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (getCompraVO().isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Inicializando Incluir Compra", "Incluindo");
				getFacadeFactory().getCompraFacade().incluir(compraVO, true, getUsuarioLogado());
				montarRecebimentoCompra();
				registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Finalizando Incluir Compra", "Incluindo");
			}
			montarCompra(getCompraVO());
			setMensagemID("msg_dados_gravados");
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	private void montarCompra(CompraVO compraVO) {
		setCompraVO(compraVO);
		montarListaCondicaoPagamento();
		montarRecebimentoCompra();
		inicializarListasSelectItemTodosComboBox();
		setCompraItemVO(new CompraItemVO());
	}

	
	public void validarDados() throws ConsistirException {
		try {
			CompraVO.validarDados(getCompraVO());
			setAbrirPanelGravar(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setOncompletGravar("");
		}

	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Inicializando Consultar Compra", "Consultando");
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCompraFacade().consultarPorCodigo(new Integer(valorInt), getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoLogado().getCodigo(), false, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCompraFacade().consultarTotalPorCodigo(new Integer(valorInt), getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoLogado().getCodigo(), false, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeFornecedor")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCompraFacade().consultarPorNomeFornecedor(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoLogado().getCodigo(), false,  getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCompraFacade().consultarTotalPorNomeFornecedor(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoLogado().getCodigo(), false, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("cnpjFornecedor")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCompraFacade().consultarPorCnpjFornecedor(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoLogado().getCodigo(), false,  getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCompraFacade().consultarTotalPorCnpjFornecedor(getControleConsulta().getValorConsulta(), getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoLogado().getCodigo(), false, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}			
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = getControleConsulta().getDataIni();
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCompraFacade().consultarPorDataCompra(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoLogado().getCodigo(), false,  getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCompraFacade().consultarTotalPorDataCompra(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoLogado().getCodigo(), false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoCotacao")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCompraFacade().consultarPorCodigoCotacao(valorInt, getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(),  getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCompraFacade().consultarTotalPorCodigoCotacao(valorInt, getValorConsultaSituacaoFinanceira(), getValorConsultaSituacaoEntregaRecebimento(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Finalizando Consultar Compra", "Consultando");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("compraCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("compraCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>CompraVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Inicializando Excluir Compra", "Excluindo");
			getFacadeFactory().getCompraFacade().excluir(compraVO, getMotivoExclusaoCompra(), true, getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getCompraVO().getUnidadeEnsino().getCodigo()));
			setCompraVO(new CompraVO());
			setCompraItemVO(new CompraItemVO());
			inicializarResponsavel();
			registrarAtividadeUsuario(getUsuarioLogado(), "CompraControle", "Finalizando Excluir Compra", "Consultando");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("compraForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("compraForm.xhtml");
		}
	}

	public void limparValorConsulta() {
		getControleConsulta().setValorConsulta("");
	}

	public void limparCampoCategoriaDespesa() {
		getCompraItemVO().setCategoriaDespesa(null);
		getCompraItemVO().setCursoVO(null);
		getCompraItemVO().setTurnoVO(null);
		getCompraItemVO().setTurma(null);
		getCompraItemVO().setDepartamentoVO(null);
		getCompraItemVO().setCentroResultadoAdministrativo(null);
	}

	public void selecionarCategoriaDespesa() {
		try {
			CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItens");
			limparCampoCategoriaDespesa();			
			getCompraItemVO().setCategoriaDespesa(obj);
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			preencherDadosPorCategoriaDespesa();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_dados_selecionados.name(), e.getMessage());
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
			setListaConsultaCategoriaDespesa(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboCategoriaDespesa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCategoriaDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public String receberCompra() {
		try {
			RecebimentoCompraVO obj = (RecebimentoCompraVO) context().getExternalContext().getRequestMap().get("recebimentoItens");
			setRecebimentoCompraVO(obj.getCodigo());
			RecebimentoCompraControle recebimentoCompraControle = (RecebimentoCompraControle) context().getExternalContext().getSessionMap().get("RecebimentoCompraControle");
			if (recebimentoCompraControle != null) {
				obj = getFacadeFactory().getRecebimentoCompraFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				recebimentoCompraControle.setRecebimentoCompraVO(obj);
				recebimentoCompraControle.inicializarListasSelectItemTodosComboBox();
				context().getExternalContext().getSessionMap().put("RecebimentoCompraControle", recebimentoCompraControle);
			} else {
				recebimentoCompraControle = new RecebimentoCompraControle();
				obj = getFacadeFactory().getRecebimentoCompraFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				recebimentoCompraControle.setRecebimentoCompraVO(obj);
				recebimentoCompraControle.inicializarListasSelectItemTodosComboBox();
				context().getExternalContext().getSessionMap().put("RecebimentoCompraControle", recebimentoCompraControle);
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("recebimentoCompraForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("Não foi possivel receber esta compra.");
			return Uteis.getCaminhoRedirecionamentoNavegacao("compraForm.xhtml");
		}
	}

	public void selecionarFornecedor() {
		try {
			FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			getCompraVO().setFornecedor(obj);			
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			getCompraVO().setFornecedor(new FornecedorVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List<FornecedorVO> objs = new ArrayList<>(0);
			switch (FornecedorVO.enumCampoConsultaFornecedor.valueOf(getCampoConsultaFornecedor())) {
			case NOME:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case RAZAOSOCIAL:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case RG:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case CPF:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			case CNPJ:
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				break;
			default:
				break;

			}
			setListaConsultaFornecedor(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getMascaraConsultaFornecedor() {
		if (getCampoConsultaFornecedor().equals("CNPJ")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		} else if (getCampoConsultaFornecedor().equals("CPF")) {
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		}
		return "";
	}

	public void limparCampoCategoriaProduto() {
		getCompraVO().setCategoriaProduto(null);
		getCompraItemVO().setCategoriaDespesa(null);
		getCompraVO().getListaCentroResultadoOrigemVOs().clear();
		getCompraVO().getCompraItemVOs().clear();
	}

	public void selecionarCategoriaProduto() {
		try {
			CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaProdutoItem");
			getCompraVO().setCategoriaProduto(getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setListaRequisicaoVO(getFacadeFactory().getRequisicaoFacade().consultarPorCategoriaProdutoPorUnidadeEnsinoPorTipoAutorizacaoRequisicaoComSituacaoAutorizadaComSituacaoEntreguePendente(getCompraVO().getCategoriaProduto().getCodigo(), null, TipoAutorizacaoRequisicaoEnum.COMPRA_DIRETA, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));			
			//getCompraVO().getCompraItemVOs().clear();
			//getCompraVO().getListaCentroResultadoOrigemVOs().clear();
			this.listaConsultaCategoriaProduto.clear();
			this.valorConsultaCategoriaProduto = null;
			this.campoConsultaCategoriaProduto = null;
			if (Uteis.isAtributoPreenchido(getListaRequisicaoVO())) {
				setHabilitarModalRequisicao(true);
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCategoriaProduto() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCategoriaProduto().equals("codigo")) {
				if (getValorConsultaCategoriaProduto().equals("")) {
					setValorConsultaCategoriaProduto("0");
				}
				int valorInt = Uteis.getValorInteiro(getValorConsultaCategoriaProduto());
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo((valorInt), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaProduto().equals("nome")) {
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaCategoriaProduto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCategoriaProduto(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List getTipoConsultaComboCategoriaProduto() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void selecionarProduto() throws Exception {	
		getProdutoServico().setValorUnitario(getFacadeFactory().getItemCotacaoFacade().consultarUltimoPrecoProdutoFornecedor(getCompraVO().getFornecedor().getCodigo(), getProdutoServico().getCodigo()));
		this.getCompraItemVO().setProduto(getProdutoServico());
		this.listaConsultaProduto.clear();
		this.valorConsultaProduto = null;
		this.campoConsultaProduto = null;

	}

	public void consultarProdutoPorChavePrimaria() {
		try {
			Integer campoConsulta = compraItemVO.getProduto().getCodigo();
			ProdutoServicoVO produto = getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			compraItemVO.setProduto(produto);

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getCompraItemVO().setProduto(new ProdutoServicoVO());
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void consultarProduto() {
		try {
			setOncompleteModal("");
			List objs = new ArrayList(0);
			//Uteis.checkState(!Uteis.isAtributoPreenchido(getCompraVO().getCategoriaProduto()), "O campo Categoria Produto (Compra) deve ser informado.");
			if (getCampoConsultaProduto().equals("codigo")) {
				if (getValorConsultaProduto().equals("")) {
					setValorConsultaProduto("0");
				}
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorCodigo(Uteis.getValorInteiro(getValorConsultaProduto()), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaProduto().equals("nome")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNome(getValorConsultaProduto(), null, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaProduto().equals("categoriaProduto")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNomeCategoriaProduto(getValorConsultaProduto(), null, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaProduto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaProduto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboProduto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("categoriaProduto", "Categoria Produto"));
		return itens;
	}
	
	

	public void atualizarCompraItem() {
		try {
			limparCampoCategoriaDespesa();
			if(!Uteis.isAtributoPreenchido(getCompraItemVO().getCategoriaDespesa()) 
					&& Uteis.isAtributoPreenchido(getCompraVO().getCategoriaProduto().getCategoriaDespesa())
					&& Uteis.isAtributoPreenchido(getCompraItemVO().getQuantidadeAdicional())){
				getCompraItemVO().setCategoriaDespesa(getCompraVO().getCategoriaProduto().getCategoriaDespesa());
				montarListaSelectItemTipoNivelCentroResultadoEnum();
				preencherDadosPorCategoriaDespesa();	
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarCompraItem() {
		try {
			if(!Uteis.isAtributoPreenchido(getCompraItemVO().getQuantidadeAdicional())){
				limparCampoCategoriaDespesa();
			}
			getFacadeFactory().getCompraFacade().adicionarObjCompraItemVOs(getCompraVO(), getCompraItemVO(), getUsuarioLogado());
			setCompraItemVO(new CompraItemVO(getCompraItemVO().getCategoriaDespesa(),getCompraItemVO().getCentroResultadoAdministrativo(), getCompraItemVO().getTipoNivelCentroResultadoEnum(),  getCompraItemVO().getDepartamentoVO(), getCompraItemVO().getCursoVO(), getCompraItemVO().getTurnoVO(), getCompraItemVO().getTurma()));
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editarCompraItem() {
		try {
			CompraItemVO obj = (CompraItemVO) context().getExternalContext().getRequestMap().get("compraItemItens");
			setCompraItemVO(obj);
			getCompraItemVO().setEdicaoManual(true);
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void removerCompraItem() {
		try {
			CompraItemVO obj = (CompraItemVO) context().getExternalContext().getRequestMap().get("compraItemItens");
			getCompraVO().excluirObjCompraItemVOs(obj.getProduto().getCodigo());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void visualizarRequisicaoCompraDireta() {
		try {
			setHabilitarModalRequisicao(true);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarRequisicaoCompraDireta() {
		try {
			getFacadeFactory().getCompraFacade().adicionarRequisicaoCompraDireta(getCompraVO(), getListaRequisicaoVO(), getUsuarioLogado());
			setHabilitarModalRequisicao(false);
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void removerRequisicaoCompraDireta() {
		try {
			RequisicaoVO obj = (RequisicaoVO) context().getExternalContext().getRequestMap().get("requisicaoItens");
			getFacadeFactory().getCompraFacade().removerRequisicaoCompraDireta(getCompraVO(), obj, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaCondicaoPagamento() {
		try {
			List<ParcelaCondicaoPagamentoVO> objs = getFacadeFactory().getParcelaCondicaoPagamentoFacade().consultarParcelaCondicaoPagamentos(getCompraVO().getCondicaoPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS);
			getCompraVO().montarListaCondicaoPagamento(objs);
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			getCompraVO().setListaCondicaoPagamento(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
				getCompraItemVO().setCentroResultadoAdministrativo(obj);
			}
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
			getCentroResultadoDataModelo().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, getCompraItemVO().getDepartamentoVO(), getCompraItemVO().getCursoVO(), getCompraItemVO().getTurma(), getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void preencherDadosPorCategoriaDespesa() {
		try {
			getCompraItemVO().setCompra(getCompraVO());
			if(getCompraItemVO().getTipoNivelCentroResultadoEnum().isDepartamento() && !Uteis.isAtributoPreenchido(getCompraItemVO().getDepartamentoVO())){
				getCompraItemVO().setDepartamentoVO(getFacadeFactory().getDepartamentoFacade().consultarDepartamentoControlaEstoquePorUnidadeEnsino(getCompraVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			getFacadeFactory().getCompraFacade().preencherDadosPorCategoriaDespesa(getCompraItemVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
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

	public List getListaSelectItemSituacaoRecebimentoCompra() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoEntregaRecebimentos = (Hashtable) Dominios.getSituacaoEntregaRecebimento();
		Enumeration keys = situacaoEntregaRecebimentos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoEntregaRecebimentos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemSituacaoFinanceiraCompra() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoFinanceiras = (Hashtable) Dominios.getSituacaoFinanceira();
		Enumeration keys = situacaoFinanceiras.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFinanceiras.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public void montarListaSelectItemCotacao(Integer prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCotacaoPorCodigo(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CotacaoVO obj = (CotacaoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getCodigo().toString()));
			}
			setListaSelectItemCotacao(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemCotacao() {
		try {
			montarListaSelectItemCotacao(0);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarCotacaoPorCodigo(Integer codigoPrm) throws Exception {
		List lista = getFacadeFactory().getCotacaoFacade().consultarPorCodigo(codigoPrm, getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCondicaoPagamento(Integer prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCondicaoPagamentoPorCodigo(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CondicaoPagamentoVO obj = (CondicaoPagamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemCondicaoPagamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemCondicaoPagamento() {
		try {
			montarListaSelectItemCondicaoPagamento(0);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarCondicaoPagamentoPorCodigo(Integer codigoPrm) throws Exception {
		List lista = getFacadeFactory().getCondicaoPagamentoFacade().consultarPorCodigo(codigoPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarFormaPagamentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
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

	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (this.getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				List objs = new ArrayList(0);
				objs.add(new SelectItem(this.getUnidadeEnsinoLogado().getCodigo(), this.getUnidadeEnsinoLogado().getNome()));
				setListaSelectItemUnidadeEnsino(objs);
				getCompraVO().getUnidadeEnsino().setCodigo(this.getUnidadeEnsinoLogado().getCodigo());
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
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
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>GrupoTrabalho</code>. Buscando todos os objetos correspondentes a entidade <code>GrupoTrabalho</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemCondicaoPagamento();
		montarListaSelectItemFormaPagamento();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemContaCorrente();
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
				if (obj.getContaCaixa()) {
					objs.add(new SelectItem(obj.getCodigo(), "CC:" + obj.getNumero() + "-" + obj.getDigito()));
				} else {
					objs.add(new SelectItem(obj.getCodigo(), "Banco:" + obj.getAgencia().getBanco().getNome() + " Ag:" + obj.getAgencia().getNumeroAgencia() + "-" + obj.getAgencia().getDigito() + " CC:" + obj.getNumero() + "-" + obj.getDigito()));
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
		List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigo(0, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeFornecedor", "Fornecedor"));
		itens.add(new SelectItem("cnpjFornecedor", "CNPJ Fornecedor"));
		itens.add(new SelectItem("codigoCotacao", "Cotação"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("codigo", "Número Compra"));
		return itens;
	}

	public boolean isCampoComboBox() {
		if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
			return true;
		}
		if (getControleConsulta().getCampoConsulta().equals("situacaoRecebimento")) {
			return true;
		}
		return false;
	}

	public List getListaParaCampoDeConsulta() {
		try {
			if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
				return getListaSelectItemSituacaoFinanceiraCompra();
			}
			if (getControleConsulta().getCampoConsulta().equals("situacaoRecebimento")) {
				return getListaSelectItemSituacaoRecebimentoCompra();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new ArrayList(0);
	}

	public boolean isCampoData() {
		return getControleConsulta().getCampoConsulta().equals("data");
	}

	public List getListaSelectItemSituacaoFinanceira() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoFinanceira = (Hashtable) Dominios.getSituacaoFinanceira();
		Enumeration keys = situacaoFinanceira.keys();
		objs.add(new SelectItem("", ""));
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFinanceira.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(objs, ordenador);
		return objs;
	}

	public List getListaSelectItemSituacaoEntregaRecebimento() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoEntregaRecebimento = (Hashtable) Dominios.getSituacaoEntregaRecebimento();
		Enumeration keys = situacaoEntregaRecebimento.keys();
		objs.add(new SelectItem("", ""));
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoEntregaRecebimento.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(objs, ordenador);
		return objs;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("compraCons.xhtml");
	}

	public CompraItemVO getCompraItemVO() {
		if(compraItemVO == null){
			compraItemVO = new CompraItemVO();
		}
		return compraItemVO;
	}

	public void setCompraItemVO(CompraItemVO compraItemVO) {
		this.compraItemVO = compraItemVO;
	}

	public List getListaSelectItemCotacao() {
		return (listaSelectItemCotacao);
	}

	public void setListaSelectItemCotacao(List listaSelectItemCotacao) {
		this.listaSelectItemCotacao = listaSelectItemCotacao;
	}

	public CompraVO getCompraVO() {
		if (compraVO == null) {
			compraVO = new CompraVO();
		}
		return compraVO;
	}

	public void setCompraVO(CompraVO compraVO) {
		this.compraVO = compraVO;
	}

	public String getCampoConsultaProduto() {
		return campoConsultaProduto;
	}

	public void setCampoConsultaProduto(String campoConsultaProduto) {
		this.campoConsultaProduto = campoConsultaProduto;
	}

	public List getListaConsultaProduto() {
		return listaConsultaProduto;
	}

	public void setListaConsultaProduto(List listaConsultaProduto) {
		this.listaConsultaProduto = listaConsultaProduto;
	}

	public String getValorConsultaProduto() {
		return valorConsultaProduto;
	}

	public void setValorConsultaProduto(String valorConsultaProduto) {
		this.valorConsultaProduto = valorConsultaProduto;
	}

	public List getListaSelectItemCondicaoPagamento() {
		return listaSelectItemCondicaoPagamento;
	}

	public void setListaSelectItemCondicaoPagamento(List listaSelectItemCondicaoPagamento) {
		this.listaSelectItemCondicaoPagamento = listaSelectItemCondicaoPagamento;
	}

	public List getListaSelectItemFormaPagamento() {
		return listaSelectItemFormaPagamento;
	}

	public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	public String getCampoConsultaCategoriaDespesa() {
		return campoConsultaCategoriaDespesa;
	}

	public void setCampoConsultaCategoriaDespesa(String campoConsultaCategoriaDespesa) {
		this.campoConsultaCategoriaDespesa = campoConsultaCategoriaDespesa;
	}

	public List getListaConsultaCategoriaDespesa() {
		if (listaConsultaCategoriaDespesa == null) {
			listaConsultaCategoriaDespesa = new ArrayList(0);
		}
		return listaConsultaCategoriaDespesa;
	}

	public void setListaConsultaCategoriaDespesa(List listaConsultaCategoriaDespesa) {
		this.listaConsultaCategoriaDespesa = listaConsultaCategoriaDespesa;
	}

	public String getValorConsultaCategoriaDespesa() {
		return valorConsultaCategoriaDespesa;
	}

	public void setValorConsultaCategoriaDespesa(String valorConsultaCategoriaDespesa) {
		this.valorConsultaCategoriaDespesa = valorConsultaCategoriaDespesa;
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

	public List getListaConsultaCategoriaProduto() {
		return listaConsultaCategoriaProduto;
	}

	public void setListaConsultaCategoriaProduto(List listaConsultaCategoriaProduto) {
		this.listaConsultaCategoriaProduto = listaConsultaCategoriaProduto;
	}

	public String getValorConsultaCategoriaProduto() {
		return valorConsultaCategoriaProduto;
	}

	public void setValorConsultaCategoriaProduto(String valorConsultaCategoriaProduto) {
		this.valorConsultaCategoriaProduto = valorConsultaCategoriaProduto;
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

	public List getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List getListaSelectItemContaCorrente() {
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public Integer getRecebimentoCompraVO() {
		return recebimentoCompraVO;
	}

	public void setRecebimentoCompraVO(Integer recebimentoCompraVO) {
		this.recebimentoCompraVO = recebimentoCompraVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		compraVO = null;
		recebimentoCompraVO = null;
		Uteis.liberarListaMemoria(listaSelectItemCotacao);
		campoConsultaProduto = null;
		valorConsultaProduto = null;
		Uteis.liberarListaMemoria(listaConsultaProduto);
		Uteis.liberarListaMemoria(listaSelectItemFormaPagamento);
		Uteis.liberarListaMemoria(listaSelectItemCondicaoPagamento);
		compraItemVO = null;
	}

	/**
	 * @return the valorConsultaSituacaoAutorizacao
	 */
	public String getValorConsultaSituacaoFinanceira() {
		return valorConsultaSituacaoFinanceira;
	}

	/**
	 * @param valorConsultaSituacaoAutorizacao
	 *            the valorConsultaSituacaoAutorizacao to set
	 */
	public void setValorConsultaSituacaoFinanceira(String valorConsultaSituacaoFinanceira) {
		this.valorConsultaSituacaoFinanceira = valorConsultaSituacaoFinanceira;
	}

	/**
	 * @return the valorConsultaSituacaoEntregaRecebimento
	 */
	public String getValorConsultaSituacaoEntregaRecebimento() {
		return valorConsultaSituacaoEntregaRecebimento;
	}

	/**
	 * @param valorConsultaSituacaoEntregaRecebimento
	 *            the valorConsultaSituacaoEntregaRecebimento to set
	 */
	public void setValorConsultaSituacaoEntregaRecebimento(String valorConsultaSituacaoEntregaRecebimento) {
		this.valorConsultaSituacaoEntregaRecebimento = valorConsultaSituacaoEntregaRecebimento;
	}

	public void montarListaSelectItemUnidadeEnsino(Integer prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorCodigo(prm);
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

	public List consultarUnidadeEnsinoPorCodigo(Integer numeroPrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public boolean getIsApresentarCampoCurso() {
		return (Uteis.isAtributoPreenchido(getCompraItemVO().getTipoNivelCentroResultadoEnum()) && getCompraItemVO().getTipoNivelCentroResultadoEnum().isCurso() && isApresentarCampoCategoriaDespesa());
	}

	public boolean getIsApresentarCampoCursoTurno() {
		return (Uteis.isAtributoPreenchido(getCompraItemVO().getTipoNivelCentroResultadoEnum()) && getCompraItemVO().getTipoNivelCentroResultadoEnum().isCursoTurno() && isApresentarCampoCategoriaDespesa());
	}

	public boolean getIsApresentarCampoTurma() {
		return (Uteis.isAtributoPreenchido(getCompraItemVO().getTipoNivelCentroResultadoEnum()) && getCompraItemVO().getTipoNivelCentroResultadoEnum().isTurma() && isApresentarCampoCategoriaDespesa());
	}
	
	public boolean isApresentarCampoDepartamento() {
		return (Uteis.isAtributoPreenchido(getCompraItemVO().getTipoNivelCentroResultadoEnum()) && getCompraItemVO().getTipoNivelCentroResultadoEnum().isDepartamento() && isApresentarCampoCategoriaDespesa());
	}
	
	public boolean isApresentarCampoCategoriaDespesa() {
		return (getCompraItemVO().getQuantidadeAdicional() > 0.0);
	}

	public List getListaSelectItemDepartamentoFornecedor() {
		if (listaSelectItemDepartamentoFornecedor == null) {
			listaSelectItemDepartamentoFornecedor = new ArrayList(0);
		}
		return listaSelectItemDepartamentoFornecedor;
	}

	public void setListaSelectItemDepartamentoFornecedor(List<SelectItem> listaSelectItemDepartamentoFornecedor) {
		this.listaSelectItemDepartamentoFornecedor = listaSelectItemDepartamentoFornecedor;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
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
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
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

	public void consultarCurso() {
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getCompraVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCurso() {
		try {
			getCompraItemVO().setCursoVO(new CursoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getCompraItemVO().setCursoVO(obj);
			preencherDadosPorCategoriaDespesa();
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}
	
	public void limparTurno() {
		try {
			getCompraItemVO().setTurnoVO(new TurnoVO());
			getCompraItemVO().setCursoVO(new CursoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCursoTurno() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
			getCompraItemVO().setTurnoVO(obj.getTurno());
			getCompraItemVO().setCursoVO(obj.getCurso());
			preencherDadosPorCategoriaDespesa();
			setListaConsultaCursoTurno(null);
			this.setValorConsultaCursoTurno("");
			this.setCampoConsultaCursoTurno("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public String getCampoConsultaCursoTurno() {
		return campoConsultaCursoTurno;
	}

	public void setCampoConsultaCursoTurno(String campoConsultaCursoTurno) {
		this.campoConsultaCursoTurno = campoConsultaCursoTurno;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCursoTurno() {
		return listaConsultaCursoTurno;
	}

	public void setListaConsultaCursoTurno(List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno) {
		this.listaConsultaCursoTurno = listaConsultaCursoTurno;
	}

	public String getValorConsultaCursoTurno() {
		return valorConsultaCursoTurno;
	}

	public void setValorConsultaCursoTurno(String valorConsultaCursoTurno) {
		this.valorConsultaCursoTurno = valorConsultaCursoTurno;
	}

	public List getTipoConsultaComboCursoTurno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turno", "Turno"));
		return itens;
	}

	public void consultarCursoTurno() {
		try {
			setListaConsultaCursoTurno(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(), getValorConsultaCursoTurno(), getCompraVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCursoTurno(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getOncompletGravar() {
		if (getAbrirPanelGravar()) {
			return "RichFaces.$('panelGravar').show();";
		}
		return "";
	}

	public void setOncompletGravar(String oncompletGravar) {
		this.oncompletGravar = oncompletGravar;
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public Boolean getIsApresentarPeriodo() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	public Boolean getAbrirPanelGravar() {
		if (abrirPanelGravar == null) {
			abrirPanelGravar = Boolean.FALSE;
		}
		return abrirPanelGravar;
	}

	public void setAbrirPanelGravar(Boolean abrirPanelGravar) {
		this.abrirPanelGravar = abrirPanelGravar;
	}
	
	public void limparTurma() {
		try {
			getCompraItemVO().setTurma(new TurmaVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getCompraItemVO().setTurma(obj);
			preencherDadosPorCategoriaDespesa();
			setListaConsultaTurma(null);
			setCampoConsultaTurma("");
			setValorConsultaTurma("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getCompraVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String consultarDepartamento() {
		try {
			List objs = new ArrayList(0);
			Integer unidadeEnsino = 0;
			if (Uteis.isAtributoPreenchido(getCompraVO().getUnidadeEnsino())) {
				unidadeEnsino = getCompraVO().getUnidadeEnsino().getCodigo();
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

	public void selecionarDepartamento()  {
		try {
			DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
			getCompraItemVO().setDepartamentoVO(obj);
			preencherDadosPorCategoriaDespesa();
			getListaConsultaDepartamento().clear();
			setValorConsultaDepartamento("");
			setCampoConsultaDepartamento("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
		
	}

	public List getTipoConsultaComboDepartamento() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		try {
			getCompraItemVO().limparCamposPorTipoNivelCentroResultadoEnum();
			preencherDadosPorCategoriaDespesa();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	private void montarListaSelectItemTipoNivelCentroResultadoEnum() {
		try {
			getListaSelectItemTipoNivelCentroResultadoEnum().clear();
			if(getCompraItemVO().isCategoriaDespesaInformada()){
				getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(getCompraItemVO().getCategoriaDespesa(), getListaSelectItemTipoNivelCentroResultadoEnum());
				if(!getListaSelectItemTipoNivelCentroResultadoEnum().isEmpty() && !Uteis.isAtributoPreenchido(getCompraItemVO().getTipoNivelCentroResultadoEnum())){
					getCompraItemVO().setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) getListaSelectItemTipoNivelCentroResultadoEnum().get(0).getValue());	
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<RequisicaoVO> getListaRequisicaoVO() {
		listaRequisicaoVO = Optional.ofNullable(listaRequisicaoVO).orElse(new ArrayList<>());
		return listaRequisicaoVO;
	}

	public void setListaRequisicaoVO(List<RequisicaoVO> listaRequisicaoVO) {
		this.listaRequisicaoVO = listaRequisicaoVO;
	}

	public boolean isHabilitarModalRequisicao() {
		return habilitarModalRequisicao;
	}

	public void setHabilitarModalRequisicao(boolean habilitarModalRequisicao) {
		this.habilitarModalRequisicao = habilitarModalRequisicao;
	}
	
	public boolean isCentroResultadoAdministrativo() {
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(boolean centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
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

	public List getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}
	
	public String getMotivoExclusaoCompra() {
		motivoExclusaoCompra = Optional.ofNullable(motivoExclusaoCompra).orElse("");
		return motivoExclusaoCompra;
	}

	public void setMotivoExclusaoCompra(String motivoExclusaoCompra) {
		this.motivoExclusaoCompra = motivoExclusaoCompra;
	}

	public boolean isPermiteAlterarCategoriaDespesa() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_CATEGORIA_DESPESA_COMPRA, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermiteAlterarCentroResultado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_COMPRA, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void verificarCategoriaProduto() throws Exception {

		boolean encontrou = false;
		setProdutoServico((ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produtoItens"));				
		Map<Integer, List<CompraItemVO>> mapa =  getCompraVO().getCompraItemVOs().stream().collect(Collectors.groupingBy(p -> p.getProduto().getCategoriaProduto().getCodigo()));		
		Iterator<Map.Entry<Integer, List<CompraItemVO>>> itr = mapa.entrySet().iterator(); 
        
        while (itr.hasNext()){ 
            Map.Entry<Integer, List<CompraItemVO>> entry = itr.next(); 
            if(entry.getKey().equals(getProdutoServico().getCategoriaProduto().getCodigo())) {
            	encontrou = true;				
				break;
            }           
       } 
        if(encontrou) {
        	selecionarProduto();
        }
        else {
        	setOncompleteModal("RichFaces.$('panelAvisoCategoriaProduto').show()");
        }
	}
	
	public ProdutoServicoVO getProdutoServico() {
		if (produtoServico == null) {
			produtoServico = new ProdutoServicoVO();
		}
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServicoVO produtoServico) {
		this.produtoServico = produtoServico;
	}
	
	public void consultarCategoriaProdutoPassandoCodigoFornecedor() {
		try {			
			setListaConsultaCategoriaProduto(getFacadeFactory().getCategoriaProdutoFacade().consultarCategoriaProdutoPassandoCodigoFornecedor(getCompraVO().getFornecedor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCategoriaProduto(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
}
