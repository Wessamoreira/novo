package controle.financeiro;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas movimentacaoFinanceiraForm.jsp
 * movimentacaoFinanceiraCons.jsp) com as funcionalidades da classe <code>MovimentacaoFinanceira</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see MovimentacaoFinanceira
 * @see MovimentacaoFinanceiraVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberRelVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("MapaNegativacaoCobrancaContaReceberControle")
@Scope("viewScope")
@Lazy
public class MapaNegativacaoCobrancaContaReceberControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	private AgenteNegativacaoCobrancaContaReceberVO agente;
	public RegistroNegativacaoCobrancaContaReceberItemVO item;
	private String consultarPor;
	private String campoConsultaAgente;
	private String valorConsultaAgente;
	private String situacaoNegativacao;
	private TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente;
	private String situacaoContaReceber;
	private String situacaoParcelaNegociada;	
	List<SelectItem> tipoConsultaComboAgente;
	private List<AgenteNegativacaoCobrancaContaReceberVO> listaConsultaAgente;
	private List<ContaReceberVO> listaConsultaNegociacao;
	
	private MatriculaVO matriculaAluno;
	private FuncionarioVO funcionario;
	private PessoaVO responsavelFinanceiro;
	private PessoaVO pessoa;
	private ParceiroVO parceiroVO;
	private FornecedorVO fornecedor;

	protected List<FornecedorVO> listaConsultaFornecedor;
    protected String valorConsultaFornecedor;
    protected String campoConsultaFornecedor;
    protected List listaConsultaFuncionario;
    protected String valorConsultaFuncionario;
    protected int valorConsultaUnidadeEnsino;
    protected String campoConsultaFuncionario;
    protected List listaConsultaAluno;
    protected String valorConsultaAluno;
    protected String campoConsultaAluno;
    protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
    protected String valorConsultaResponsavelFinanceiro;
    protected String campoConsultaResponsavelFinanceiro;
    protected String valorConsultaParceiro;
    protected String campoConsultaParceiro;
    protected List<ParceiroVO> listaConsultaParceiro;
    
	private Boolean selecionarTodasParcelas;
	private Integer qtdeParcelasSelecionadas;
	private Double valorAReceberTotal;
	private List<RegistroNegativacaoCobrancaContaReceberItemVO> listaRegistroNegativacaoVO;
	private Date dataNegativacaoInicial;
	private Date dataNegativacaoFinal;
	private String motivo;
	private String tipoPessoa;
	private String situacaoRegistro;
	private List<SelectItem> listaSelectItemSituacaoRegistro;
	private boolean removerNegativacaoContaReceberViaIntegracao = false;
	private boolean apresentarRegistroViaIntegracao = false;
	

	

	


	public MapaNegativacaoCobrancaContaReceberControle() throws Exception {
		novo();
		setControleConsulta(new ControleConsulta());
		setDataNegativacaoInicial(Uteis.getDataPrimeiroDiaMes(new Date()));
		setDataNegativacaoFinal(Uteis.getDataUltimoDiaMes(new Date()));
		setSituacaoContaReceber("RC");
		setSituacaoParcelaNegociada("PR");
		setConsultarPor("dataRecebNeg");
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}
	
	

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>MovimentacaoFinanceira</code> para edição pelo usuário da
	 * aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		isPermitirRemoverNegativacaoContaReceberViaIntegracao();
		setSelecionarTodasParcelas(false);
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>MovimentacaoFinanceira</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			setFecharModalMotivo("");
			if (getMotivo().equals("") && getIsFiltroRegistrado()) {
				throw new Exception ("Informa o motivo da exclusão da(s) conta(s) a receber da negativação/cobrança!");
			}
			if(getIsFiltroRegistrado()) {
				getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().excluirNegativacaoCobrancaListagemVOs(getAgente(), getListaRegistroNegativacaoVO(), getMotivo() ,isRemoverNegativacaoContaReceberViaIntegracao() ,true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			}else {
				getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().estornarNegativacaoCobrancaListagemVOs(getListaRegistroNegativacaoVO(), getAgente(), getTipoAgente(), getUsuarioLogado());
			}
			setListaRegistroNegativacaoVO(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().consultarRegistroNegativacaoCobrancaContaReceberItem(
					getAgente(), getSituacaoContaReceber(), getTipoAgente(), getDataNegativacaoInicial(), getDataNegativacaoFinal(), getSituacaoParcelaNegociada(), getConsultarPor(), getUnidadeEnsinoVOs(), getTipoPessoa(), getMatriculaAluno(), getParceiroVO(), getFuncionario(), getResponsavelFinanceiro(), getSituacaoRegistro(), getFornecedor()));
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			return "editar";
		} catch (Exception e) {
			setFecharModalMotivo("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "editar";
		}
	}

	@Override
	public String consultar() {
		try {
			setSelecionarTodasParcelas(false);
			validarDadosConsulta();
			setQtdeParcelasSelecionadas(0);
			setValorAReceberTotal(0.0);
			getListaRegistroNegativacaoVO().clear();
			setListaRegistroNegativacaoVO(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade()
					.consultarRegistroNegativacaoCobrancaContaReceberItem(getAgente(),getSituacaoContaReceber(),getTipoAgente(),							
							getDataNegativacaoInicial(),getDataNegativacaoFinal(), 	getSituacaoParcelaNegociada(), 	getConsultarPor(), 
							getUnidadeEnsinoVOs(),	getTipoPessoa(), getMatriculaAluno(), getParceiroVO(), 
							getFuncionario(), 	getResponsavelFinanceiro(), getSituacaoRegistro(),getFornecedor()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			if (getListaRegistroNegativacaoVO().isEmpty()) {
				setMensagemID("msg_relatorio_sem_dados", Uteis.ALERTA);
			}
			return "consultar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "consultar";
		}
	}

	public void consultarNegociacao() {
		try {
			RegistroNegativacaoCobrancaContaReceberItemVO obj = (RegistroNegativacaoCobrancaContaReceberItemVO) context().getExternalContext().getRequestMap().get("registroItens");
			setItem(obj);
			setListaConsultaNegociacao(montarListaNegociacaoContaReceber(obj.getContaReceber()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<ContaReceberVO> montarListaNegociacaoContaReceber(Integer codigoContaReceber) {
		List<ContaReceberVO> listaAux = new ArrayList<ContaReceberVO>();
		listaAux.addAll(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarNegociacao(codigoContaReceber));
		List<ContaReceberVO> listaTemp = new ArrayList<ContaReceberVO>();
		listaTemp.addAll(listaAux);
		for (ContaReceberVO contaReceberVO : listaAux) {
			listaTemp.addAll(montarListaNegociacaoContaReceber(contaReceberVO.getCodigo()));
		}
		return listaTemp;
	}
	
	public void desbloquearConta() {
		try {
			//verificaPermissao();
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberarContaReceberNegociadaMapaNegativacaoCobranca", getUsuarioLogado());
			getItem().setBloqueio(Boolean.FALSE);
			getItem().setSelecionado(Boolean.TRUE);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarParcela() {
		try {
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTodasParcelas() {
		try {			
			for (RegistroNegativacaoCobrancaContaReceberItemVO obj : getListaRegistroNegativacaoVO()) {
				if(getIsFiltroRegistrado() || (!getIsFiltroRegistrado() && obj.getPermiteEstornar())) {
					obj.setSelecionado(getSelecionarTodasParcelas());										
				}
			}
//			setMapaPendenciaCartaoCreditoTotalVOs(getFacadeFactory().getMapaPendenciaCartaoCreditoFacade().realizarCalculoMapaPendenciaCartaCreditoTotal(getDataVencimento(), getListaMapaPendenciaCartaoCreditoVO()));
//			realizarCalculoTotal();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "Mapa Registro Cobrança Conta Receber Rel ", "Iniciando Impressao PDF", "Emitindo Relatorio");
			List<RegistroNegativacaoCobrancaContaReceberRelVO> listaRelatorio = new ArrayList<RegistroNegativacaoCobrancaContaReceberRelVO>();
			RegistroNegativacaoCobrancaContaReceberRelVO relatorio = new RegistroNegativacaoCobrancaContaReceberRelVO();
			RegistroNegativacaoCobrancaContaReceberVO re = new RegistroNegativacaoCobrancaContaReceberVO();
			List<RegistroNegativacaoCobrancaContaReceberItemVO> listaFinal = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>();
			
			for (RegistroNegativacaoCobrancaContaReceberItemVO registroNegativacaoCobrancaContaReceberItemVO : getListaRegistroNegativacaoVO()) {
				if (registroNegativacaoCobrancaContaReceberItemVO.getSelecionado()) {
					listaFinal.add(registroNegativacaoCobrancaContaReceberItemVO);
				}
			}			
			
			re.setAgente(getAgente());
			re.setTipoAgente(getAgente().getTipo());
			re.setUsuarioVO(getUsuarioLogadoClone());
			re.setDataGeracao(new Date());
			re.setListaContasReceberCobranca(listaFinal);
			relatorio.setRegistroNegativacaoCobrancaContaReceber(re);
			listaRelatorio.add(relatorio);
			
			getSuperParametroRelVO().setNomeDesignIreport(getCaminhoBaseRelatorio() + "RegistroNegativacaoCobrancaContaReceberRel.jrxml");
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
			if(getIsFiltroRegistrado()) {
				getSuperParametroRelVO().setTituloRelatorio("LISTA CONTAS EM "+(getTipoAgente().equals(TipoAgenteNegativacaoCobrancaContaReceberEnum.COBRANCA) ? "COBRANÇA" : "NEGATIVAÇÃO"));
			}else{
				getSuperParametroRelVO().setTituloRelatorio("LISTA CONTAS RETIRADAS "+(getTipoAgente().equals(TipoAgenteNegativacaoCobrancaContaReceberEnum.COBRANCA) ? "COBRANÇA" : "NEGATIVAÇÃO"));
			}
			getSuperParametroRelVO().setListaObjetos(listaRelatorio);			
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
//			logoPadraoRelatorio
//			nomeEmpresa
			realizarImpressaoRelatorio();
			
			setMensagemID("msg_relatorio_ok");
			registrarAtividadeUsuario(getUsuarioLogado(), "Registro Cobrança Conta Receber Rel", "Finalizando Impressao PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("RegistroNegativacaoCobrancaContaReceber");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
		limparDadosRelacionadosUnidadeEnsino();
	}

	public void limparDadosRelacionadosUnidadeEnsino() {		
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	private String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator;
	}
	
	/**
	 * Rotina responsável por preencher a combo de filtro pos situação.
	 */
	public List<SelectItem> getListaSelectItemSituacaoNegativacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("NG", "Em Negativação"));
		itens.add(new SelectItem("CO", "Em Cobrança"));
		itens.add(new SelectItem("TO", "Todas"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemSituacaoContaReceber() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AR", "A Receber"));
		itens.add(new SelectItem("NE", "Negociado"));
		itens.add(new SelectItem("RE", "Recebido"));
		itens.add(new SelectItem("RC", "Recebido/Negociado"));
		itens.add(new SelectItem("TO", "Todas"));
		return itens;
	}

	public Boolean getApresentarSituacaoParcelaNegociada() {
		if (getSituacaoContaReceber().equals("TO") || getSituacaoContaReceber().equals("RC") || getSituacaoContaReceber().equals("NE")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoParcelaNegociada() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("NR", "Nenhuma Parcela Negociada Recebida"));
		itens.add(new SelectItem("PR", "Pelo Menos 1 Parcela Negociada Recebida"));
		itens.add(new SelectItem("TR", "Todas Parcelas Negociadas Recebidas"));
		itens.add(new SelectItem("TO", "Todas"));
		return itens;
	}
	
	public List<SelectItem> getListaSelectItemConsultarPor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("dataNegativacao", "Data Negativação"));
		itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
		itens.add(new SelectItem("dataRecebNeg", "Data Recebimento/Negociado"));
		if(!getIsFiltroRegistrado()) {
			itens.add(new SelectItem("dataExclusao", "Data Exclusão Registro"));
		}
		return itens;
	}
	
	public void validarDadosConsulta() throws Exception {
		if (getAgente().getCodigo().intValue() == 0) {
			throw new ConsistirException("O Agente Negativação/Cobrança deve ser informado!");
		}
	}

	/**
	 * @return the selecionarTodasParcelas
	 */
	public Boolean getSelecionarTodasParcelas() {
		return selecionarTodasParcelas;
	}

	/**
	 * @param selecionarTodasParcelas
	 *            the selecionarTodasParcelas to set
	 */
	public void setSelecionarTodasParcelas(Boolean selecionarTodasParcelas) {
		this.selecionarTodasParcelas = selecionarTodasParcelas;
	}

	/**
	 * @return the valorAReceberTotal
	 */
	public Double getValorAReceberTotal() {
		if (valorAReceberTotal == null) {
			valorAReceberTotal = 0.0;
		}
		return valorAReceberTotal;
	}

	/**
	 * @param valorAReceberTotal
	 *            the valorAReceberTotal to set
	 */
	public void setValorAReceberTotal(Double valorAReceberTotal) {
		this.valorAReceberTotal = valorAReceberTotal;
	}

	public List<RegistroNegativacaoCobrancaContaReceberItemVO> getListaRegistroNegativacaoVO() {
		if (listaRegistroNegativacaoVO == null) {
			listaRegistroNegativacaoVO = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>(0);
		}
		return listaRegistroNegativacaoVO;
	}

	public void setListaRegistroNegativacaoVO(List<RegistroNegativacaoCobrancaContaReceberItemVO> listaRegistroNegativacaoVO) {
		this.listaRegistroNegativacaoVO = listaRegistroNegativacaoVO;
	}

	public Integer getQtdeParcelasSelecionadas() {
		if (qtdeParcelasSelecionadas == null) {
			qtdeParcelasSelecionadas = 0;
		}
		return qtdeParcelasSelecionadas;
	}

	public void setQtdeParcelasSelecionadas(Integer qtdeParcelasSelecionadas) {
		this.qtdeParcelasSelecionadas = qtdeParcelasSelecionadas;
	}

	public Double valorTotalTaxa;
	
	public Double getValorTotalTaxa() {
		if (valorTotalTaxa == null) {
			valorTotalTaxa = 0.0;
		}
		return valorTotalTaxa;
	}

	public void setValorTotalTaxa(Double valorTotalTaxa) {
		this.valorTotalTaxa = valorTotalTaxa;
	}

	public String getValorTotalTaxa_Apresentar() {		
		return "R$ " + Uteis.getDoubleFormatado(getValorTotalTaxa());
	}

	public void realizarCalculoTotal() {
		setQtdeParcelasSelecionadas(0);
		setValorAReceberTotal(0.0);
		setValorTotalTaxa(0.0);
//		for(MapaPendenciaCartaoCreditoTotalVO mapaPendenciaCartaoCreditoTotalVO: getMapaPendenciaCartaoCreditoTotalVOs()) {
//			setQtdeParcelasSelecionadas(getQtdeParcelasSelecionadas()+ mapaPendenciaCartaoCreditoTotalVO.getQuantidade());
//			setValorAReceberTotal(getValorAReceberTotal() + mapaPendenciaCartaoCreditoTotalVO.getValor());
//			setValorTotalTaxa(getValorTotalTaxa() + mapaPendenciaCartaoCreditoTotalVO.getValorTaxa());
//		}
		setValorAReceberTotal(Uteis.arrendondarForcando2CadasDecimais(getValorAReceberTotal()));
		setValorTotalTaxa(Uteis.arrendondarForcando2CadasDecimais(getValorTotalTaxa()));
	}	

	public Date getDataNegativacaoInicial() {
		if (dataNegativacaoInicial == null) {
			dataNegativacaoInicial = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataNegativacaoInicial;
	}

	public void setDataNegativacaoInicial(Date dataNegativacaoInicial) {
		this.dataNegativacaoInicial = dataNegativacaoInicial;
	}

	public Date getDataNegativacaoFinal() {
		if (dataNegativacaoFinal == null) {
			dataNegativacaoFinal = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataNegativacaoFinal;
	}

	public void setDataNegativacaoFinal(Date dataNegativacaoFinal) {
		this.dataNegativacaoFinal = dataNegativacaoFinal;
	}	

	public List<SelectItem> getListaSelectItensTipoAgente() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO, "Negativação"));
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.COBRANCA, "Cobrança"));
		return itens;
	}
	
	public void selecionarAgente() throws Exception {
		try {
			AgenteNegativacaoCobrancaContaReceberVO obj = (AgenteNegativacaoCobrancaContaReceberVO) context().getExternalContext().getRequestMap().get("agenteItens");
			setAgente(getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(obj.getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));			
			if (!getAgente().getTipoAmbos()) {
				setTipoAgente(getAgente().getTipo());
			} else {
				setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO);
			}
			setCampoConsultaAgente("");
			setValorConsultaAgente("");
			getListaRegistroNegativacaoVO().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparAgente() throws Exception {
		try {
			setAgente(new AgenteNegativacaoCobrancaContaReceberVO());
			setListaConsultaAgente(null);
			getListaRegistroNegativacaoVO().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparAluno() throws Exception {
		try {
			setMatriculaAluno(new MatriculaVO());
			setListaConsultaAluno(null);
			getListaRegistroNegativacaoVO().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean isApresentarCampoMatriculaAluno(){
//		return getTipoAluno() ||  (getTipoResponsavelFinanceiro() && Uteis.isAtributoPreenchido(getResponsavelFinanceiro())) || (getTipoParceiro() && Uteis.isAtributoPreenchido(getParceiroVO()));
		return getTipoAluno();
	}

    public void consultarAlunoParceiro() {
    	try {
    		limparMensagem();
    		setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoResponsavelFinanceiro(getResponsavelFinanceiro().getCodigo(), false, getUsuarioLogado()));
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
    	}
    }

	
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public List getListaSelectItemAlunoFuncionarioCandidato() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        objs.add(new SelectItem("AL", "Aluno"));
        objs.add(new SelectItem("PA", "Parceiro"));
        objs.add(new SelectItem("RF", "Responsável Financeiro"));
        objs.add(new SelectItem("FU", "Funcionário"));
        objs.add(new SelectItem("FO", "Fornecedor"));
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }
	
    public void consultarAlunoPorMatricula() {
        try {
            if (!getMatriculaAluno().getMatricula().equals("")) {
                MatriculaVO objs = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaAluno().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                if (!objs.getMatricula().equals("")) {
                    this.setMatriculaAluno(objs);
                    setMensagemID("msg_dados_consultados");
                    return;
                }
                getMatriculaAluno().setMatricula("");
                getMatriculaAluno().getAluno().setNome("");
                setMensagemID("msg_erro_dadosnaoencontrados");
            }
        } catch (Exception e) {
            getMatriculaAluno().setMatricula("");
            getMatriculaAluno().getAluno().setNome("");
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    public void consultarFuncionarioPorCodigo() {
        try {
            if (!this.getFuncionario().getMatricula().equals("") && getTipoPessoa().equals("FU")) {
                FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(this.getFuncionario().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (funcionario.getCodigo().intValue() != 0) {
                    this.setFuncionario(funcionario);
                    setMensagemID("msg_dados_consultados");
                } else {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            getFuncionario().setCodigo(0);
            getFuncionario().setMatricula("");
            getFuncionario().getPessoa().setNome("");
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }
    
	public void limparConsulta() {
    	getControleConsulta().setValorConsulta("");
    	setValorConsultaAgente("");
    }	
	
	public String abrirModalMotivo;
	public String fecharModalMotivo;
	
	public void validarBaixaDeNegativacao() {
		try {
//			setAbrirModalMotivo("#{rich:component('panelMotivo')}.show()");
//			Boolean umaSelecionada = false;
//			for (RegistroNegativacaoCobrancaContaReceberItemVO obj : getListaRegistroNegativacaoVO()) {
//				if (obj.getSelecionado()) {
//					umaSelecionada = true;
//				}
//			}
//			if (!umaSelecionada) {
//				throw new Exception ("Selecione ao menos uma conta para ser excluída!");
//			}
			limparMensagem();		
		} catch (Exception e) {
			setAbrirModalMotivo("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void consultarAgente() {
		try {
			List<AgenteNegativacaoCobrancaContaReceberVO> objs = new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0);
			if (getCampoConsultaAgente().equals("nome")) {
				objs = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorNome(getValorConsultaAgente(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAgente().equals("tipo")) {
            	objs = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorTipo(getValorConsultaAgente(), false, getUsuarioLogado());
            }
			setListaConsultaAgente(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAgente(new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


    public void consultarAluno() {
        try {

            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
            	throw new Exception(UteisJSF.internacionalizar("msg_entre_prmconsulta"));
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, "","", getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("data")) {
                Date valorData = Uteis.getDate(getValorConsultaAluno());
                objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("situacao")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeResponsavel")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
            this.setMatriculaAluno(obj);
            this.getPessoa().setCodigo(obj.getAluno().getCodigo());
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

    public void selecionarParceiro() {
    	try {
    		ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");    		
    		setParceiroVO(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
            	throw new Exception(UteisJSF.internacionalizar("msg_entre_prmconsulta"));
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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

    public void consultarResponsavelFinanceiro() {
        try {

            if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
            	throw new Exception(UteisJSF.internacionalizar("msg_entre_prmconsulta"));
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
            getListaConsultaResponsavelFinanceiro().clear();
            if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }
            if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }
            if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }

            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarParceiro() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getValorConsultaParceiro().equals("")) {
            	throw new Exception(UteisJSF.internacionalizar("msg_entre_prmconsulta"));
            }
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
    
    public List getTipoConsultaComboParceiro() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
        return itens;
    }
    
    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));
        return itens;
    }
    private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;

    public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
        if (tipoConsultaComboResponsavelFinanceiro == null) {
            tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
        }
        return tipoConsultaComboResponsavelFinanceiro;
    }
    
    public void selecionarFuncionario() {
        try {
            FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
            this.setFuncionario(obj);
            this.getPessoa().setCodigo(obj.getPessoa().getCodigo());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
	public void selecionarResponsavelFinanceiro() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
			getListaConsultaResponsavelFinanceiro().clear();
			this.setResponsavelFinanceiro(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void selecionarFornecedor() {
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
        this.setFornecedor(obj);
    }
    private List<SelectItem> tipoConsultaComboFornecedor;

    public String getMascaraConsultaFornecedor() {
        if (getCampoConsultaFornecedor().equals("CNPJ")) {
            return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
        } else if (getCampoConsultaFornecedor().equals("CPF")) {
            return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
        }
        return "";
    }

    public List<SelectItem> getTipoConsultaComboFornecedor() {
        if (tipoConsultaComboFornecedor == null) {
            tipoConsultaComboFornecedor = new ArrayList<SelectItem>(0);
            tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
            tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
            tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
            tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
            tipoConsultaComboFornecedor.add(new SelectItem("RG", "RG"));
        }
        return tipoConsultaComboFornecedor;
    }
    
	public List<SelectItem> getTipoConsultaComboAgente() {
		if (tipoConsultaComboAgente == null) {
			tipoConsultaComboAgente = new ArrayList<SelectItem>(0);
			tipoConsultaComboAgente.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboAgente.add(new SelectItem("tipo", "Tipo Agente"));
		}
		return tipoConsultaComboAgente;
	}

    public void carregarDadosPorTipoPessoa(){
    	try {
    		setMatriculaAluno(new MatriculaVO());
    		setParceiroVO(new ParceiroVO());
    		setFornecedor(new FornecedorVO());
    		setFuncionario(new FuncionarioVO());
    		setPessoa(new PessoaVO());
    		setResponsavelFinanceiro(new PessoaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void isPermitirRemoverNegativacaoContaReceberViaIntegracao() {
		try {			
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.MAPA_NEGATIVACAO_COBRANCA_CONTA_RECEBER_VIA_INTEGRACAO, getUsuarioLogadoClone());
			setApresentarRegistroViaIntegracao(true);
		} catch (Exception e) {
			setApresentarRegistroViaIntegracao(false);
		}
	}
    

	public boolean isRemoverNegativacaoContaReceberViaIntegracao() {
		return removerNegativacaoContaReceberViaIntegracao;
	}



	public void setRemoverNegativacaoContaReceberViaIntegracao(boolean removerNegativacaoContaReceberViaIntegracao) {
		this.removerNegativacaoContaReceberViaIntegracao = removerNegativacaoContaReceberViaIntegracao;
	}



	public AgenteNegativacaoCobrancaContaReceberVO getAgente() {
		if (agente == null) {
			agente = new AgenteNegativacaoCobrancaContaReceberVO();
		}
		return agente;
	}

	public void setAgente(AgenteNegativacaoCobrancaContaReceberVO agente) {
		this.agente = agente;
	}
	
	public String getCampoConsultaAgente() {
		if (campoConsultaAgente == null) {
			campoConsultaAgente = "nome";
		}
		return campoConsultaAgente;
	}

	public void setCampoConsultaAgente(String campoConsultaAgente) {
		this.campoConsultaAgente = campoConsultaAgente;
	}

	public String getValorConsultaAgente() {
		if (valorConsultaAgente == null) {
			valorConsultaAgente = "";
		}
		return valorConsultaAgente;
	}

	public void setValorConsultaAgente(String valorConsultaAgente) {
		this.valorConsultaAgente = valorConsultaAgente;
	}

	public List<AgenteNegativacaoCobrancaContaReceberVO> getListaConsultaAgente() {
		if (listaConsultaAgente == null) {
			listaConsultaAgente = new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>();
		}
		return listaConsultaAgente;
	}

	public void setListaConsultaAgente(List<AgenteNegativacaoCobrancaContaReceberVO> listaConsultaAgente) {
		this.listaConsultaAgente = listaConsultaAgente;
	}

	public String getSituacaoNegativacao() {
		if (situacaoNegativacao == null) {
			situacaoNegativacao = "TO";
		}
		return situacaoNegativacao;
	}

	public void setSituacaoNegativacao(String situacaoNegativacao) {
		this.situacaoNegativacao = situacaoNegativacao;
	}

	public String getSituacaoContaReceber() {
		if (situacaoContaReceber == null) {
			situacaoContaReceber = "TO";
		}
		return situacaoContaReceber;
	}

	public void setSituacaoContaReceber(String situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
	}

	public Boolean getPossuiRegistro() {
		return !getListaRegistroNegativacaoVO().isEmpty();
	}
	
	public Boolean getPossuiRegistroSelecionado() {
		if (!getListaRegistroNegativacaoVO().isEmpty()) {
			Boolean umaSelecionada = false;
			for (RegistroNegativacaoCobrancaContaReceberItemVO obj : getListaRegistroNegativacaoVO()) {
				if (obj.getSelecionado()) {
					umaSelecionada = true;
				}
			}
			if (!umaSelecionada) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public TipoAgenteNegativacaoCobrancaContaReceberEnum getTipoAgente() {
		return tipoAgente;
	}

	public void setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente) {
		this.tipoAgente = tipoAgente;
	}

	public Date getDataAtual () {
		return new Date();
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getAbrirModalMotivo() {
		if (abrirModalMotivo == null) {
			abrirModalMotivo = "";
		}
		return abrirModalMotivo;
	}

	public void setAbrirModalMotivo(String abrirModalMotivo) {
		this.abrirModalMotivo = abrirModalMotivo;
	}

	public String getFecharModalMotivo() {
		if (fecharModalMotivo == null) {
			fecharModalMotivo = "";
		}
		return fecharModalMotivo;
	}
	
	public void setFecharModalMotivo(String fecharModalMotivo) {
		this.fecharModalMotivo = fecharModalMotivo;
	}

	public List<ContaReceberVO> getListaConsultaNegociacao() {
		if (listaConsultaNegociacao == null) {
			listaConsultaNegociacao = new ArrayList<ContaReceberVO>();
		}
		return listaConsultaNegociacao;
	}

	public void setListaConsultaNegociacao(List<ContaReceberVO> listaConsultaNegociacao) {
		this.listaConsultaNegociacao = listaConsultaNegociacao;
	}

	public RegistroNegativacaoCobrancaContaReceberItemVO getItem() {
		if (item == null) {
			item = new RegistroNegativacaoCobrancaContaReceberItemVO();
		}
		return item;
	}

	public void setItem(RegistroNegativacaoCobrancaContaReceberItemVO item) {
		this.item = item;
	}

	public String getSituacaoParcelaNegociada() {
		if (situacaoParcelaNegociada == null) {
			situacaoParcelaNegociada = "TO";
		}
		return situacaoParcelaNegociada;
	}

	public void setSituacaoParcelaNegociada(String situacaoParcelaNegociada) {
		this.situacaoParcelaNegociada = situacaoParcelaNegociada;
	}

	public String getConsultarPor() {
		if (consultarPor == null) {
			consultarPor = "dataNegativacao";
		}
		return consultarPor;
	}

	public void setConsultarPor(String consultarPor) {
		this.consultarPor = consultarPor;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public Boolean getTipoAluno() {
		if (getTipoPessoa().equals("AL")) {
			return Boolean.TRUE;
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoResponsavelFinanceiro() {
		return getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
	}

	public Boolean getTipoFornecedor() {
		return getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor());
	}

	public Boolean getTipoFuncionario() {
		if (getTipoPessoa().equals("FU")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoParceiro() {
		if (getTipoPessoa().equals("PA")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public MatriculaVO getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = new MatriculaVO();
		}
		return (matriculaAluno);
	}

	public void setMatriculaAluno(MatriculaVO obj) {
		this.matriculaAluno = obj;
	}

	public FuncionarioVO getFuncionario() {
		if (funcionario == null) {
			funcionario = new FuncionarioVO();
		}
		return (funcionario);
	}

	public void setFuncionario(FuncionarioVO obj) {
		this.funcionario = obj;
	}

	public PessoaVO getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = new PessoaVO();
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

    public List<FornecedorVO> getListaConsultaFornecedor() {
        if (listaConsultaFornecedor == null) {
            listaConsultaFornecedor = new ArrayList<FornecedorVO>(0);
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
    
    public String getCampoConsultaFuncionario() {
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List getListaConsultaFuncionario() {
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public String getValorConsultaFuncionario() {
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
        if (listaConsultaResponsavelFinanceiro == null) {
            listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
        }
        return listaConsultaResponsavelFinanceiro;
    }

    public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
        this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
    }

    public String getValorConsultaResponsavelFinanceiro() {
        if (valorConsultaResponsavelFinanceiro == null) {
            valorConsultaResponsavelFinanceiro = "";
        }
        return valorConsultaResponsavelFinanceiro;
    }

    public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
        this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
    }

    public String getCampoConsultaResponsavelFinanceiro() {
        if (campoConsultaResponsavelFinanceiro == null) {
            campoConsultaResponsavelFinanceiro = "";
        }
        return campoConsultaResponsavelFinanceiro;
    }

    public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
        this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
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
            listaConsultaParceiro = new ArrayList<ParceiroVO>(0);
        }
        return listaConsultaParceiro;
    }

    public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
        this.listaConsultaParceiro = listaConsultaParceiro;
    }

    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public List getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

	public String getSituacaoRegistro() {
		if(situacaoRegistro == null){
			situacaoRegistro = "REGISTRADO";
		}
		return situacaoRegistro;
	}

	public void setSituacaoRegistro(String situacaoRegistro) {
		this.situacaoRegistro = situacaoRegistro;
	}

	public List<SelectItem> getListaSelectItemSituacaoRegistro() {
		if(listaSelectItemSituacaoRegistro == null){
			listaSelectItemSituacaoRegistro = new ArrayList<SelectItem>();
			listaSelectItemSituacaoRegistro.add(new SelectItem("REGISTRADO", "Registrado"));
			listaSelectItemSituacaoRegistro.add(new SelectItem("RETIRADO", "Retirado"));
		}
		return listaSelectItemSituacaoRegistro;
	}
	
	

	public void setListaSelectItemSituacaoRegistro(List<SelectItem> listaSelectItemSituacaoRegistro) {
		this.listaSelectItemSituacaoRegistro = listaSelectItemSituacaoRegistro;
	}
    

	public Boolean getIsFiltroRegistrado() {
		return getSituacaoRegistro().equals("REGISTRADO");
	}

	public void limparResultadoConsulta() {
		getListaRegistroNegativacaoVO().clear();
	}	
	
	public void realizarNavegacaoTelaRelatorioSituacaoFinanceiraAluno() throws Exception {
		removerControleMemoriaFlashTela("SituacaoFinanceiraAlunoRelControle");
		RegistroNegativacaoCobrancaContaReceberItemVO obj= (RegistroNegativacaoCobrancaContaReceberItemVO) context().getExternalContext().getRequestMap().get("registroItens");
		
		if (obj != null && !obj.getMatricula().equals("")) {
			context().getExternalContext().getSessionMap().put("matriculaAlunoNegativacaoCobrancaContaReceber", obj.getMatricula());
			context().getExternalContext().getSessionMap().put("dataInicio", getDataNegativacaoInicial());
			context().getExternalContext().getSessionMap().put("dataFim", getDataNegativacaoFinal());
			
		}
	}
	
	public boolean isApresentarRegistroViaIntegracao() {
		return apresentarRegistroViaIntegracao;
	}


	public void setApresentarRegistroViaIntegracao(boolean apresentarRegistroViaIntegracao) {
		this.apresentarRegistroViaIntegracao = apresentarRegistroViaIntegracao;
	}
	
}
