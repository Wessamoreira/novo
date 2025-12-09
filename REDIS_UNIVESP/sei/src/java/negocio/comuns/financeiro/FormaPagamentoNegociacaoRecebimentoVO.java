package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.financeiro.NegociacaoRecebimento;

/**
 * Reponsável por manter os dados da entidade
 * FormaPagamentoNegociacaoRecebimento. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos
 * de acesso a estes atributos. Classe utilizada para apresentar e manter em
 * memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see NegociacaoRecebimento
 */
public class FormaPagamentoNegociacaoRecebimentoVO extends SuperVO {

	private Integer codigo;
	private Integer negociacaoRecebimento;
	private FormaPagamentoVO formaPagamento;
	private Double valorRecebimento;
	private ContaCorrenteVO contaCorrente;
	private ChequeVO cheque;
	private Integer qtdeParcelasCartaoCredito;
	private OperadoraCartaoVO operadoraCartaoVO;
	private CategoriaDespesaVO categoriaDespesaVO;
	private ContaCorrenteVO contaCorrenteOperadoraCartaoVO;
	private String cidadeDataRecebimentoPorExtenso;
	private String nomeResponsavel;
//	private List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> lisstaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	//atributo dataPrevisaCartao utilizado no relatório de Comprovante de recebimento
	private Date dataPrevisaCartao;
	//Usado somente ná hora de imprimir o relatório de comprovante de recebimento de cartao
	private Date dataEmissaoCartao;
	private UsuarioVO usuarioDesbloqueouFormaRecebimentoNoRecebimento;
	private Date dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento;
	private Date dataCredito;
		
	private FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;	
	
	//Transient recurso para controle DCC
	private Boolean utilizarCartaoComoPagamentoRecorrenteProximaParcela;
	private FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoPagamentoAutomaticoParcelaRecorrencia;
	private Integer diaPadraoPagamentoRecorrencia;
	public static final long serialVersionUID = 1L;
	
	//Transient
	private List<SelectItem> listaSelectItemParcelas;

	/**
	 * Construtor padrão da classe
	 * <code>FormaPagamentoNegociacaoRecebimento</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public FormaPagamentoNegociacaoRecebimentoVO() {
		super();
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 */
	public static void validarUnicidade(List<FormaPagamentoNegociacaoRecebimentoVO> lista, FormaPagamentoNegociacaoRecebimentoVO obj) throws ConsistirException {
		for (FormaPagamentoNegociacaoRecebimentoVO repetido : lista) {
			if (repetido.getCodigo().intValue() == obj.getCodigo().intValue()) {
				throw new ConsistirException("O campo CODIGO já esta cadastrado!");
			}

		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code>. Todos os tipos de
	 * consistência de dados são e devem ser implementadas neste método. São
	 * validações típicas: verificação de campos obrigatórios, verificação de
	 * valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 */
	public static void validarDados(FormaPagamentoNegociacaoRecebimentoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo FORMA DE  PAGAMENTO (Recebimento) deve ser informado.");
		}
		if (!obj.getFormaPagamento().getTipo().equals("BO") && !obj.getFormaPagamento().getTipo().equals("IS")) {
			if (obj.getValorRecebimento().doubleValue() == 0) {
				throw new ConsistirException("O campo VALOR RECEBIMENTO (Recebimento) deve ser informado.");
			}
		}
		if ((obj.getFormaPagamento().getTipo().equals("BO") || obj.getFormaPagamento().getTipo().equals("CA") || obj.getFormaPagamento().getTipo().equals("DC")) && obj.getContaCorrente().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CONTA CORRENTE (Recebimento) deve ser informado.");
		}
		if ((obj.getFormaPagamento().getTipo().equals("CA")) && obj.getQtdeParcelasCartaoCredito().equals(0)) {
			throw new ConsistirException("O campo QUANTIDADE DE PARCELAS (Recebimento) deve ser informado.");
		}
		
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
	}

	public String getTipoFormaPagamento() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CH")) {
			return "RichFaces.$('panelCheque').show()";
		}
		return "";
	}

	public Boolean getInformaValor() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CH") || getFormaPagamento().getTipo().equals("IS") || getFormaPagamento().getTipo().equals("")) {
			return false;
		}
		return true;
	}
	
	public Boolean getApresentarBotaoAdicionar() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CH") || getFormaPagamento().getTipo().equals("")) {
			return false;
		}
		return true;
	}

	public Boolean getInformaContaCorrente() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		return getFormaPagamento().getTipo().equals(TipoFormaPagamento.DEPOSITO.getValor()) 
				|| getFormaPagamento().getTipo().equals(TipoFormaPagamento.BOLETO_BANCARIO.getValor())
				|| TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE.getValor().equals(getFormaPagamento().getTipo());
	}

	public Boolean getFormaPgtoBoletoBancario() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		return getFormaPagamento().getTipo().equals(TipoFormaPagamento.BOLETO_BANCARIO.getValor());
	}

	public Boolean getIsInformaOperadoraCartao() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		return getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor()) 
				|| getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_DEBITO.getValor());
	}

	public Boolean getIsInformaQtdeParcelasCartaoCredito() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CA")) {
			return true;
		}
		return false;
	}
	
	public Boolean getIsApresentarDadosCartaoCredito() {
		return getFormaPagamento().getTipo().equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor());
	}

	public Boolean getIsApresentarDadosCheque() {
		return getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor());
	}
	
	public Double getValorRecebimento() {
		if (valorRecebimento == null) {
			valorRecebimento = 0.0;
		}
		return (valorRecebimento);
	}

	public Integer getNegociacaoRecebimento() {
		if (negociacaoRecebimento == null) {
			negociacaoRecebimento = 0;
		}
		return (negociacaoRecebimento);
	}

	public void setNegociacaoRecebimento(Integer negociacaoRecebimento) {
		this.negociacaoRecebimento = negociacaoRecebimento;
	}

	public void setValorRecebimento(Double valorRecebimento) {
		this.valorRecebimento = valorRecebimento;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	/**
	 * @param formaPagamento
	 *            the formaPagamento to set
	 */
	public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the contaCorrente
	 */
	public ContaCorrenteVO getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = new ContaCorrenteVO();
		}
		return contaCorrente;
	}

	/**
	 * @param contaCorrente
	 *            the contaCorrente to set
	 */
	public void setContaCorrente(ContaCorrenteVO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	/**
	 * @return the cheque
	 */
	public ChequeVO getCheque() {
		if (cheque == null) {
			cheque = new ChequeVO();
		}
		return cheque;
	}

	/**
	 * @param cheque
	 *            the cheque to set
	 */
	public void setCheque(ChequeVO cheque) {
		this.cheque = cheque;
	}

	/**
	 * @return the formaPagamento
	 */
	public FormaPagamentoVO getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamentoVO();
		}
		return formaPagamento;
	}

	public Integer getQtdeParcelasCartaoCredito() {
		if (qtdeParcelasCartaoCredito == null) {
			qtdeParcelasCartaoCredito = 0;
		}
		return qtdeParcelasCartaoCredito;
	}

	public void setQtdeParcelasCartaoCredito(Integer qtdeParcelasCartaoCredito) {
		this.qtdeParcelasCartaoCredito = qtdeParcelasCartaoCredito;
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

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}

	public ContaCorrenteVO getContaCorrenteOperadoraCartaoVO() {
		if (contaCorrenteOperadoraCartaoVO == null) {
			contaCorrenteOperadoraCartaoVO = new ContaCorrenteVO();
		}
		return contaCorrenteOperadoraCartaoVO;
	}

	public void setContaCorrenteOperadoraCartaoVO(ContaCorrenteVO contaCorrenteOperadoraCartaoVO) {
		this.contaCorrenteOperadoraCartaoVO = contaCorrenteOperadoraCartaoVO;
	}

//	public List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> getListaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO() {
//		if (listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO == null) {
//			listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO = new ArrayList<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO>(0);
//		}
//		return listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
//	}
//
//	public void setListaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO) {
//		this.listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO = listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
//	}

	/**
	 * @return the cidadeDataRecebimentoPorExtenso
	 */
	public String getCidadeDataRecebimentoPorExtenso() {
		return cidadeDataRecebimentoPorExtenso;
	}

	/**
	 * @param cidadeDataRecebimentoPorExtenso
	 *            the cidadeDataRecebimentoPorExtenso to set
	 */
	public void setCidadeDataRecebimentoPorExtenso(String cidadeDataRecebimentoPorExtenso) {
		this.cidadeDataRecebimentoPorExtenso = cidadeDataRecebimentoPorExtenso;
	}

	/**
	 * @return the nomeResponsavel
	 */
	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	/**
	 * @param nomeResponsavel
	 *            the nomeResponsavel to set
	 */
	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public Date getDataPrevisaCartao() {
		if (dataPrevisaCartao == null) {
			dataPrevisaCartao = new Date();
		}
		return dataPrevisaCartao;
	}

	public void setDataPrevisaCartao(Date dataPrevisaCartao) {
		this.dataPrevisaCartao = dataPrevisaCartao;
	}
	
	public String getDataPrevisaoCartao_Apresentar() {
		return Uteis.getData(getDataPrevisaCartao());
	}

	public Date getDataEmissaoCartao() {
		if(dataEmissaoCartao == null){
			dataEmissaoCartao = new Date();
		}
		return dataEmissaoCartao;
	}

	public void setDataEmissaoCartao(Date dataEmissaoCartao) {
		this.dataEmissaoCartao = dataEmissaoCartao;
	}
	
	public String getDataEmissaoCartao_Apresentar() {
		return Uteis.getData(getDataEmissaoCartao());
	}

	public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO() {
		if (formaPagamentoNegociacaoRecebimentoCartaoCreditoVO == null) {
			formaPagamentoNegociacaoRecebimentoCartaoCreditoVO = new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO();
		}
		return formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO formaPagamentoNegociacaoRecebimentoCartaoCreditoVO) {
		this.formaPagamentoNegociacaoRecebimentoCartaoCreditoVO = formaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
	}
	
	
	/**
	 * @author Victor Hugo de Paula Costa 27/05/2015
	 */
	//Transient
	private ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO;
	//Fim Transient
	private Double taxaDeOperacao;
	private Double taxaDeAntecipacao;
	private List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao;

	public ConfiguracaoFinanceiroCartaoVO getConfiguracaoFinanceiroCartaoVO() {
		if(configuracaoFinanceiroCartaoVO == null) {
			configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
		}
		return configuracaoFinanceiroCartaoVO;
	}

	public void setConfiguracaoFinanceiroCartaoVO(ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO) {
		this.configuracaoFinanceiroCartaoVO = configuracaoFinanceiroCartaoVO;
	}

	public Double getTaxaDeOperacao() {
		if(taxaDeOperacao == null) {
			taxaDeOperacao = 0.0;
		}
		return taxaDeOperacao;
	}

	public void setTaxaDeOperacao(Double taxaDeOperacao) {
		this.taxaDeOperacao = taxaDeOperacao;
	}

	public Double getTaxaDeAntecipacao() {
		if(taxaDeAntecipacao == null) {
			taxaDeAntecipacao = 0.0;
		}
		return taxaDeAntecipacao;
	}

	public void setTaxaDeAntecipacao(Double taxaDeAntecipacao) {
		this.taxaDeAntecipacao = taxaDeAntecipacao;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoFinanceiroCartao() {
		if(listaSelectItemConfiguracaoFinanceiroCartao == null) {
			listaSelectItemConfiguracaoFinanceiroCartao = new ArrayList<SelectItem>();
		}
		return listaSelectItemConfiguracaoFinanceiroCartao;
	}

	public void setListaSelectItemConfiguracaoFinanceiroCartao(List<SelectItem> listaSelectItemConfiguracaoFinanceiroCartao) {
		this.listaSelectItemConfiguracaoFinanceiroCartao = listaSelectItemConfiguracaoFinanceiroCartao;
	}
	
	//Transient
	private Integer quantidadeCartao;

	public Integer getQuantidadeCartao() {
		if(quantidadeCartao == null) {
			quantidadeCartao = 0;
		}
		return quantidadeCartao;
	}

	public void setQuantidadeCartao(Integer quantidadeCartao) {
		this.quantidadeCartao = quantidadeCartao;
	}
	
	//Transient
	private String textoDescricaoReciboMatriculaRenovacaoOnline;
	private Boolean validarCampoNomeCartaoCredito;
	private Boolean validarCampoNumeroCartaoCredito;
	private Boolean validarCampoMesVencimentoCartaoCredito;
	private Boolean validarCampoAnoVencimentoCartaoCredito;
	
	public String getTextoDescricaoReciboMatriculaRenovacaoOnline() {
		if(textoDescricaoReciboMatriculaRenovacaoOnline == null) {
			textoDescricaoReciboMatriculaRenovacaoOnline = getOperadoraCartaoVO().getOperadoraCartaoCreditoApresentar()+" XXXX XXXX XXXX "+getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().substring(getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao().length() - 4)+" em "+getQtdeParcelasCartaoCredito().toString()+" x";
		}
		return textoDescricaoReciboMatriculaRenovacaoOnline;
	}

	public void setTextoDescricaoReciboMatriculaRenovacaoOnline(String textoDescricaoReciboMatriculaRenovacaoOnline) {
		this.textoDescricaoReciboMatriculaRenovacaoOnline = textoDescricaoReciboMatriculaRenovacaoOnline;
	}

	public Boolean getValidarCampoNomeCartaoCredito() {
		if(validarCampoNomeCartaoCredito == null) {
			validarCampoNomeCartaoCredito = false;
		}
		return validarCampoNomeCartaoCredito;
	}

	public void setValidarCampoNomeCartaoCredito(Boolean validarCampoNomeCartaoCredito) {
		this.validarCampoNomeCartaoCredito = validarCampoNomeCartaoCredito;
	}

	public Boolean getValidarCampoNumeroCartaoCredito() {
		if(validarCampoNumeroCartaoCredito == null) {
			validarCampoNumeroCartaoCredito = false;
		}
		return validarCampoNumeroCartaoCredito;
	}

	public void setValidarCampoNumeroCartaoCredito(Boolean validarCampoNumeroCartaoCredito) {
		this.validarCampoNumeroCartaoCredito = validarCampoNumeroCartaoCredito;
	}

	public Boolean getValidarCampoMesVencimentoCartaoCredito() {
		if(validarCampoMesVencimentoCartaoCredito == null){
			validarCampoMesVencimentoCartaoCredito = false;
		}
		return validarCampoMesVencimentoCartaoCredito;
	}

	public void setValidarCampoMesVencimentoCartaoCredito(Boolean validarCampoMesVencimentoCartaoCredito) {
		this.validarCampoMesVencimentoCartaoCredito = validarCampoMesVencimentoCartaoCredito;
	}

	public Boolean getValidarCampoAnoVencimentoCartaoCredito() {
		return validarCampoAnoVencimentoCartaoCredito;
	}

	public void setValidarCampoAnoVencimentoCartaoCredito(Boolean validarCampoAnoVencimentoCartaoCredito) {
		this.validarCampoAnoVencimentoCartaoCredito = validarCampoAnoVencimentoCartaoCredito;
	}
	

	public UsuarioVO getUsuarioDesbloqueouFormaRecebimentoNoRecebimento() {
		return usuarioDesbloqueouFormaRecebimentoNoRecebimento;
	}

	public void setUsuarioDesbloqueouFormaRecebimentoNoRecebimento(UsuarioVO usuarioDesbloqueouFormaRecebimentoNoRecebimento) {
		this.usuarioDesbloqueouFormaRecebimentoNoRecebimento = usuarioDesbloqueouFormaRecebimentoNoRecebimento;
	}

	public Date getDataUsuarioDesbloqueouFormaRecebimentoNoRecebimento() {
		return dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento;
	}

	public void setDataUsuarioDesbloqueouFormaRecebimentoNoRecebimento(Date dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento) {
		this.dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento = dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento;
	}

	@Override
	public String toString() {
		return "FormaPagamentoNegociacaoRecebimentoVO [codigo=" + codigo + ", negociacaoRecebimento=" + negociacaoRecebimento + ", formaPagamento=" + formaPagamento + ", valorRecebimento=" + valorRecebimento + ", contaCorrente=" + contaCorrente + ", cheque=" + cheque + ", qtdeParcelasCartaoCredito=" + qtdeParcelasCartaoCredito + ", operadoraCartaoVO=" + operadoraCartaoVO + ", categoriaDespesaVO=" + categoriaDespesaVO + ", contaCorrenteOperadoraCartaoVO=" + contaCorrenteOperadoraCartaoVO + ", cidadeDataRecebimentoPorExtenso=" + cidadeDataRecebimentoPorExtenso + ", nomeResponsavel=" + nomeResponsavel + ", dataPrevisaCartao=" + dataPrevisaCartao + ", dataEmissaoCartao=" + dataEmissaoCartao + ", usuarioDesbloqueouFormaRecebimentoNoRecebimento=" + usuarioDesbloqueouFormaRecebimentoNoRecebimento + ", dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento=" + dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento + ", formaPagamentoNegociacaoRecebimentoCartaoCreditoVO="
				+ formaPagamentoNegociacaoRecebimentoCartaoCreditoVO + ", configuracaoFinanceiroCartaoVO=" + configuracaoFinanceiroCartaoVO + ", taxaDeOperacao=" + taxaDeOperacao + ", taxaDeAntecipacao=" + taxaDeAntecipacao + ", listaSelectItemConfiguracaoFinanceiroCartao=" + listaSelectItemConfiguracaoFinanceiroCartao + ", quantidadeCartao=" + quantidadeCartao + ", textoDescricaoReciboMatriculaRenovacaoOnline=" + textoDescricaoReciboMatriculaRenovacaoOnline + ", validarCampoNomeCartaoCredito=" + validarCampoNomeCartaoCredito + ", validarCampoNumeroCartaoCredito=" + validarCampoNumeroCartaoCredito + ", validarCampoMesVencimentoCartaoCredito=" + validarCampoMesVencimentoCartaoCredito + ", validarCampoAnoVencimentoCartaoCredito=" + validarCampoAnoVencimentoCartaoCredito + "]";
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 */
	private NegociacaoRecebimentoVO negociacaoRecebimentoVO;

	public NegociacaoRecebimentoVO getNegociacaoRecebimentoVO() {
		if(negociacaoRecebimentoVO == null) {
			negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		}
		return negociacaoRecebimentoVO;
	}

	public void setNegociacaoRecebimentoVO(NegociacaoRecebimentoVO negociacaoRecebimentoVO) {
		this.negociacaoRecebimentoVO = negociacaoRecebimentoVO;
	}
	
	
	
	
	

	/**
	 * @author Victor Hugo de Paula Costa 5.0.4.0 17/03/2016
	 */
	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;
	
	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
		if(configuracaoRecebimentoCartaoOnlineVO == null) {
			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
		}
		return configuracaoRecebimentoCartaoOnlineVO;
	}
	
	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
	}
	
	
	public FormaPagamentoNegociacaoRecebimentoVO clone() throws CloneNotSupportedException {
		FormaPagamentoNegociacaoRecebimentoVO clone = (FormaPagamentoNegociacaoRecebimentoVO) super.clone();
		clone.setCodigo(0);
		clone.setNovoObj(true);
		clone.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(new FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO());
		clone.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(this.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().clone());
		return clone;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 01/06/2016 16:20
	 */
	private String motivoCancelamento;

	public String getMotivoCancelamento() {
		if (motivoCancelamento == null) {
			motivoCancelamento = "";
		}
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}
	
	
	
	public String getDescricaoParcelamentoCartaoCredito(){
		if(Uteis.isAtributoPreenchido(getOperadoraCartaoVO()) && getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.OPERADORA)){
			return "Parcelado em "+getQtdeParcelasCartaoCredito()+"x ";
		}else if(Uteis.isAtributoPreenchido(getOperadoraCartaoVO()) && getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getTipoFinanciamentoEnum().equals(TipoFinanciamentoEnum.INSTITUICAO)){
			return "Parcela "+getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroParcela();
		} 
		return "";
	}

	public Date getDataCredito() {
		if (dataCredito == null) {
			dataCredito = new Date();
		}
		return dataCredito;
	}

	public void setDataCredito(Date dataCredito) {
		this.dataCredito = dataCredito;
	}
	
	public Boolean getApresentarDataCredito() {
		return (getFormaPgtoBoletoBancario() || getFormaPagamento().getTipo().equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE.getValor()) 
				|| getFormaPagamento().getTipo().equals(TipoFormaPagamento.DEPOSITO.getValor()));
	}	
	
	public boolean getExibirParcelas() {
		return TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(getOperadoraCartaoVO().getTipo()) &&
				!getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getSplitRealizado();
	}
	
	
	
	public void montarListasSelectItemParcelas(int parcelas) throws Exception {
		getListaSelectItemParcelas().clear();
		for (int i = 1; i <= parcelas; i++) {
			getListaSelectItemParcelas().add(new SelectItem(String.valueOf(i), i + "x"));
		}
	}
	
	public List<SelectItem> getListaSelectItemParcelas() {
		if (listaSelectItemParcelas == null) {
			listaSelectItemParcelas = new ArrayList<SelectItem>();
		}
		return listaSelectItemParcelas;
	}

	public void setListaSelectItemParcelas(List<SelectItem> listaSelectItemParcelas) {
		this.listaSelectItemParcelas = listaSelectItemParcelas;
	}
	

	public Boolean getUtilizarCartaoComoPagamentoRecorrenteProximaParcela() {
		if (utilizarCartaoComoPagamentoRecorrenteProximaParcela == null) {
			utilizarCartaoComoPagamentoRecorrenteProximaParcela = getConfiguracaoRecebimentoCartaoOnlineVO().getUtilizarOpcaoRecorrenciaDefaulMarcado();
		}
		return utilizarCartaoComoPagamentoRecorrenteProximaParcela;
	}

	public void setUtilizarCartaoComoPagamentoRecorrenteProximaParcela(Boolean utilizarCartaoComoPagamentoRecorrenteProximaParcela) {
		this.utilizarCartaoComoPagamentoRecorrenteProximaParcela = utilizarCartaoComoPagamentoRecorrenteProximaParcela;
	}

	public FormaPadraoDataBaseCartaoRecorrenteEnum getFormaPadraoPagamentoAutomaticoParcelaRecorrencia() {
		if (formaPadraoPagamentoAutomaticoParcelaRecorrencia == null) {
			formaPadraoPagamentoAutomaticoParcelaRecorrencia = getConfiguracaoRecebimentoCartaoOnlineVO().getFormaPadraoDataBaseCartaoRecorrente();
		}
		return formaPadraoPagamentoAutomaticoParcelaRecorrencia;
	}

	public void setFormaPadraoPagamentoAutomaticoParcelaRecorrencia(FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoPagamentoAutomaticoParcelaRecorrencia) {
		this.formaPadraoPagamentoAutomaticoParcelaRecorrencia = formaPadraoPagamentoAutomaticoParcelaRecorrencia;
	}

	public Integer getDiaPadraoPagamentoRecorrencia() {
		if (diaPadraoPagamentoRecorrencia == null) {
			diaPadraoPagamentoRecorrencia = UteisData.getDiaMesData(new Date());
		}
		return diaPadraoPagamentoRecorrencia;
	}

	public void setDiaPadraoPagamentoRecorrencia(Integer diaPadraoPagamentoRecorrencia) {
		this.diaPadraoPagamentoRecorrencia = diaPadraoPagamentoRecorrencia;
	}
	
	public void validarDiaPagamentoRecorrencia() {
		if (getDiaPadraoPagamentoRecorrencia() > 31) {
			setDiaPadraoPagamentoRecorrencia(UteisData.getUltimoDiaMes(new Date()));
		}
	}
	
	
	
}
