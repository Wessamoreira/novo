package negocio.comuns.financeiro;

import java.util.Date;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.MotivoRejeicaoRemessa;
import negocio.comuns.financeiro.enumerador.SituacaoRegistroDetalheEnum;
import negocio.comuns.utilitarias.BancoFactory;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;
import negocio.comuns.utilitarias.boleto.bancos.BancoBrasil;
import negocio.comuns.utilitarias.boleto.bancos.Banestes;
import negocio.comuns.utilitarias.boleto.bancos.Bradesco;
import negocio.comuns.utilitarias.boleto.bancos.CaixaEconomica;
import negocio.comuns.utilitarias.boleto.bancos.Hsbc;
import negocio.comuns.utilitarias.boleto.bancos.Itau;
import negocio.comuns.utilitarias.boleto.bancos.Santander;
import negocio.comuns.utilitarias.boleto.bancos.Sicred;
import negocio.comuns.utilitarias.boleto.bancos.Unibanco;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.facade.jdbc.financeiro.RegistroArquivo;

/**
 * Reponsável por manter os dados da entidade RegistroDetalhe. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see RegistroArquivo
 */
public class RegistroDetalheVO extends SuperVO {

	private Integer codigo;
	private RegistroArquivoVO registroArquivo;
	private String codigoBanco;
	private Integer loteServico;
	private Integer tipoRegistro;
	private Integer numeroSequencialRegistroLote;
	private String codigoSeguimentoRegistroDetalhe;
	private Integer codigoMovimentoRemessaRetorno;
	private Integer sacadoTipoInscricao;
	private Integer sacadoNumeroInscricaoCorpo;
	private Integer sacadoNumeroInscricaoFilial;
	private String sacadoNumeroInscricaoControle;
	private String sacadoNome;
	private String sacadoEndereco;
	private String sacadoBairro;
	private Integer sacadoCep;
	private Integer sacadoSufixoCep;
	private String sacadoCidade;
	private String sacadoUnidadeFederacao;
	private String sacadoBancoCodigo;
	private String sacadoAgenciaCodigo;
	private Integer cedenteTipoInscricaoEmpresa;
	private String cedenteNumeroInscricaoEmpresa;
	private String cedenteIdentificacaoNoBanco;
	private String cedenteNumeroAgencia;
	private String cedenteNumeroConta;
	private Integer carteira;
	private Integer codigoCarteira;
	private Integer tipoCarteira;
	private Integer identificacaoOcorrencia;
	private String indicadorRateioCredito;
	private Double numeroIdentificacao1;
	private Double numeroIdentificacao2;
	private Integer identificaoEmissaoBloqueto;
	private String numeroDocumentoCobranca;
	private Double valorNominalTitulo;
	private Integer especieTitulo;
	private String identificacaoAceite;
	private Integer dataEmissaoTitulo;
	private Integer codigoJurosMora;
	private Integer dataJurosMora;
	private Double jurosMora;
	private Integer codigoDesconto;
	private Integer dataDesconto;
	private Double desconto;
	private Double valorAbatimento;
	private String identificacaoTituloEmpresa;
	private String identificacaoTituloBanco;
	private Integer codigoProtesto;
	private Integer numeroDiasProtesto;
	private Integer codigoMoeda;
	private Integer numeroContrato;
	private Date dataVencimentoTitulo;
	private Integer numeroContratoCobrancaRegistro;
	private Integer motivoOcorrencia;
	private String confirmacaoInstituicaoProtesto;
	private String motivoRegeicao;
	private Double acrescimos;
	private Double valorDesconto;
	private Double valorPago;
	private Double valorLiquido;
	private Double valorDespesas;
	private Double valorOutrasDespesas;
	private Double valorOutrosCreditos;
	private Double valorIOF;
	/**
	 * Corresponde a Data do Recebimento de Fato
	 */
	private Date dataOcorrencia;
	/**
	 * Corresponde a Data do Recebimento de Fato
	 */
	private Date dataCredito;
	private boolean boletoNaoEncontrado;
	private boolean boletoBaixado;
	private Integer numeroSequencialRegistro;
	private Double valorLancamento;
	private Double valorTarifa;
	private Integer indicativoCreditoDebito;
	private Integer indicativoValor;
	private String nossoNumero;
	private String codigoCedenteCodigoConvenioBanco;
	private String parcela;
	private Double tarifaCobranca;
	private Integer numeroContaCorrente;
	private Integer digitoVerificadorConta;
	private Boolean contaReceberAgrupada;
	private String codigoBarra;
	private Integer numeroAgencia;
	private SituacaoRegistroDetalheEnum situacaoRegistroDetalheEnum;
	// transient
	private Boolean selecionado;

	/**
	 * Utilizado na identificacao do registro detalhe para geração da negociacao
	 * de recebimento.
	 */
	private Integer codigoContaReceber;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>RegistroDetalhe</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public RegistroDetalheVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>RegistroDetalheVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(RegistroDetalheVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
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
		setCodigoSeguimentoRegistroDetalhe(getCodigoSeguimentoRegistroDetalhe().toUpperCase());
		setSacadoNumeroInscricaoControle(getSacadoNumeroInscricaoControle().toUpperCase());
		setSacadoNome(getSacadoNome().toUpperCase());
		setSacadoEndereco(getSacadoEndereco().toUpperCase());
		setSacadoBairro(getSacadoBairro().toUpperCase());
		setSacadoCidade(getSacadoCidade().toUpperCase());
		setSacadoUnidadeFederacao(getSacadoUnidadeFederacao().toUpperCase());
		setSacadoAgenciaCodigo(getSacadoAgenciaCodigo().toUpperCase());
		setCedenteNumeroInscricaoEmpresa(getCedenteNumeroInscricaoEmpresa().toUpperCase());
		setCedenteIdentificacaoNoBanco(getCedenteIdentificacaoNoBanco().toUpperCase());
		setCedenteNumeroAgencia(getCedenteNumeroAgencia().toUpperCase());
		setCedenteNumeroConta(getCedenteNumeroConta().toUpperCase());
		setIndicadorRateioCredito(getIndicadorRateioCredito().toUpperCase());
		setNumeroDocumentoCobranca(getNumeroDocumentoCobranca().toUpperCase());
		setIdentificacaoAceite(getIdentificacaoAceite().toUpperCase());
		setIdentificacaoTituloEmpresa(getIdentificacaoTituloEmpresa().toUpperCase());
		setIdentificacaoTituloBanco(getIdentificacaoTituloBanco().toUpperCase());
		setConfirmacaoInstituicaoProtesto(getConfirmacaoInstituicaoProtesto().toUpperCase());
		setMotivoRegeicao(getMotivoRegeicao().toUpperCase());
	}

	public Date getDataCredito() {
		if (dataCredito == null) {
			dataCredito = new Date();
		}
		return (dataCredito);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataCredito_Apresentar() {
		return (Uteis.getData(dataCredito));
	}

	public void setDataCredito(Date dataCredito) {
		this.dataCredito = dataCredito;
	}

	public Date getDataOcorrencia() {
		if (dataOcorrencia == null) {
			dataOcorrencia = new Date();
		}
		return (dataOcorrencia);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataOcorrencia_Apresentar() {
		return (Uteis.getData(dataOcorrencia));
	}

	public void setDataOcorrencia(Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

	public Double getValorIOF() {
		if (valorIOF == null) {
			valorIOF = 0.0;
		}
		return (valorIOF);
	}

	public void setValorIOF(Double valorIOF) {
		this.valorIOF = valorIOF;
	}

	public Double getValorOutrosCreditos() {
		if (valorOutrosCreditos == null) {
			valorOutrosCreditos = 0.0;
		}
		return (valorOutrosCreditos);
	}

	public void setValorOutrosCreditos(Double valorOutrosCreditos) {
		this.valorOutrosCreditos = valorOutrosCreditos;
	}

	public Double getValorOutrasDespesas() {
		if (valorOutrasDespesas == null) {
			valorOutrasDespesas = 0.0;
		}
		return (valorOutrasDespesas);
	}

	public void setValorOutrasDespesas(Double valorOutrasDespesas) {
		this.valorOutrasDespesas = valorOutrasDespesas;
	}

	public Double getValorDespesas() {
		if (valorDespesas == null) {
			valorDespesas = 0.0;
		}
		return (valorDespesas);
	}

	public void setValorDespesas(Double valorDespesas) {
		this.valorDespesas = valorDespesas;
	}

	public Double getValorLiquido() {
		if (valorLiquido == null) {
			valorLiquido = 0.0;
		}
		return (valorLiquido);
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public Double getValorPago() {
		if (valorPago == null) {
			valorPago = 0.0;
		}
		return (valorPago);
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return (valorDesconto);
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Double getAcrescimos() {
		if (acrescimos == null) {
			acrescimos = 0.0;
		}
		return (acrescimos);
	}

	public void setAcrescimos(Double acrescimos) {
		this.acrescimos = acrescimos;
	}

	public String getMotivoRegeicao() {
		if (motivoRegeicao == null) {
			motivoRegeicao = "";
		}
		return (motivoRegeicao);
	}

	public void setMotivoRegeicao(String motivoRegeicao) {
		this.motivoRegeicao = motivoRegeicao;
	}

	public String getConfirmacaoInstituicaoProtesto() {
		if (confirmacaoInstituicaoProtesto == null) {
			confirmacaoInstituicaoProtesto = "";
		}
		return (confirmacaoInstituicaoProtesto);
	}

	public void setConfirmacaoInstituicaoProtesto(String confirmacaoInstituicaoProtesto) {
		this.confirmacaoInstituicaoProtesto = confirmacaoInstituicaoProtesto;
	}

	public Integer getMotivoOcorrencia() {
		if (motivoOcorrencia == null) {
			motivoOcorrencia = 0;
		}
		return (motivoOcorrencia);
	}

	public void setMotivoOcorrencia(Integer motivoOcorrencia) {
		this.motivoOcorrencia = motivoOcorrencia;
	}

	public Integer getNumeroContratoCobrancaRegistro() {
		if (numeroContratoCobrancaRegistro == null) {
			numeroContratoCobrancaRegistro = 0;
		}
		return (numeroContratoCobrancaRegistro);
	}

	public void setNumeroContratoCobrancaRegistro(Integer numeroContratoCobrancaRegistro) {
		this.numeroContratoCobrancaRegistro = numeroContratoCobrancaRegistro;
	}

	public Date getDataVencimentoTitulo() {
		// if (dataVencimentoTitulo == null) {
		// dataVencimentoTitulo = new Date();
		// }
		return (dataVencimentoTitulo);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataVencimentoTitulo_Apresentar() {
		return (Uteis.getData(dataVencimentoTitulo));
	}

	public void setDataVencimentoTitulo(Date dataVencimentoTitulo) {
		this.dataVencimentoTitulo = dataVencimentoTitulo;
	}

	public Integer getNumeroContrato() {
		if (numeroContrato == null) {
			numeroContrato = 0;
		}
		return (numeroContrato);
	}

	public void setNumeroContrato(Integer numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public Integer getCodigoMoeda() {
		if (codigoMoeda == null) {
			codigoMoeda = 0;
		}
		return (codigoMoeda);
	}

	public void setCodigoMoeda(Integer codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}

	public Integer getNumeroDiasProtesto() {
		if (numeroDiasProtesto == null) {
			numeroDiasProtesto = 0;
		}
		return (numeroDiasProtesto);
	}

	public void setNumeroDiasProtesto(Integer numeroDiasProtesto) {
		this.numeroDiasProtesto = numeroDiasProtesto;
	}

	public Integer getCodigoProtesto() {
		if (codigoProtesto == null) {
			codigoProtesto = 0;
		}
		return (codigoProtesto);
	}

	public void setCodigoProtesto(Integer codigoProtesto) {
		this.codigoProtesto = codigoProtesto;
	}

	public String getIdentificacaoTituloBanco() {
		if (identificacaoTituloBanco == null) {
			identificacaoTituloBanco = "";
		}
		return (identificacaoTituloBanco.trim());
	}

	public void setIdentificacaoTituloBanco(String identificacaoTituloBanco) {
		this.identificacaoTituloBanco = identificacaoTituloBanco;
	}

	public String getIdentificacaoTituloEmpresa() {
		if (identificacaoTituloEmpresa == null) {
			identificacaoTituloEmpresa = "";
		}
		return (identificacaoTituloEmpresa).trim();
	}

	public void setIdentificacaoTituloEmpresa(String identificacaoTituloEmpresa) {
		this.identificacaoTituloEmpresa = identificacaoTituloEmpresa;
	}

	public Double getValorAbatimento() {
		if (valorAbatimento == null) {
			valorAbatimento = 0.0;
		}
		return (valorAbatimento);
	}

	public void setValorAbatimento(Double valorAbatimento) {
		this.valorAbatimento = valorAbatimento;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return (desconto);
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Integer getDataDesconto() {
		if (dataDesconto == null) {
			dataDesconto = 0;
		}
		return (dataDesconto);
	}

	public void setDataDesconto(Integer dataDesconto) {
		this.dataDesconto = dataDesconto;
	}

	public Integer getCodigoDesconto() {
		if (codigoDesconto == null) {
			codigoDesconto = 0;
		}
		return (codigoDesconto);
	}

	public void setCodigoDesconto(Integer codigoDesconto) {
		this.codigoDesconto = codigoDesconto;
	}

	public Double getJurosMora() {
		if (jurosMora == null) {
			jurosMora = 0.0;
		}
		return (jurosMora);
	}

	public void setJurosMora(Double jurosMora) {
		this.jurosMora = jurosMora;
	}

	public Integer getDataJurosMora() {
		if (dataJurosMora == null) {
			dataJurosMora = 0;
		}
		return (dataJurosMora);
	}

	public void setDataJurosMora(Integer dataJurosMora) {
		this.dataJurosMora = dataJurosMora;
	}

	public Integer getCodigoJurosMora() {
		if (codigoJurosMora == null) {
			codigoJurosMora = 0;
		}
		return (codigoJurosMora);
	}

	public void setCodigoJurosMora(Integer codigoJurosMora) {
		this.codigoJurosMora = codigoJurosMora;
	}

	public Integer getDataEmissaoTitulo() {
		if (dataEmissaoTitulo == null) {
			dataEmissaoTitulo = 0;
		}
		return (dataEmissaoTitulo);
	}

	public void setDataEmissaoTitulo(Integer dataEmissaoTitulo) {
		this.dataEmissaoTitulo = dataEmissaoTitulo;
	}

	public String getIdentificacaoAceite() {
		if (identificacaoAceite == null) {
			identificacaoAceite = "";
		}
		return (identificacaoAceite);
	}

	public void setIdentificacaoAceite(String identificacaoAceite) {
		this.identificacaoAceite = identificacaoAceite;
	}

	public Integer getEspecieTitulo() {
		if (especieTitulo == null) {
			especieTitulo = 0;
		}
		return (especieTitulo);
	}

	public void setEspecieTitulo(Integer especieTitulo) {
		this.especieTitulo = especieTitulo;
	}

	public Double getValorNominalTitulo() {
		if (valorNominalTitulo == null) {
			valorNominalTitulo = 0.0;
		}
		return (valorNominalTitulo);
	}

	public void setValorNominalTitulo(Double valorNominalTitulo) {
		this.valorNominalTitulo = valorNominalTitulo;
	}

	public String getNumeroDocumentoCobranca() {
		if (numeroDocumentoCobranca == null) {
			numeroDocumentoCobranca = "";
		}
		return (numeroDocumentoCobranca);
	}

	public void setNumeroDocumentoCobranca(String numeroDocumentoCobranca) {
		this.numeroDocumentoCobranca = numeroDocumentoCobranca;
	}

	public Integer getIdentificaoEmissaoBloqueto() {
		if (identificaoEmissaoBloqueto == null) {
			identificaoEmissaoBloqueto = 0;
		}
		return (identificaoEmissaoBloqueto);
	}

	public void setIdentificaoEmissaoBloqueto(Integer identificaoEmissaoBloqueto) {
		this.identificaoEmissaoBloqueto = identificaoEmissaoBloqueto;
	}

	public Double getNumeroIdentificacao2() {
		if (numeroIdentificacao2 == null) {
			numeroIdentificacao2 = 0.0;
		}
		return (numeroIdentificacao2);
	}

	public void setNumeroIdentificacao2(Double numeroIdentificacao2) {
		this.numeroIdentificacao2 = numeroIdentificacao2;
	}

	public Double getNumeroIdentificacao1() {
		if (numeroIdentificacao1 == null) {
			numeroIdentificacao1 = 0.0;
		}
		return (numeroIdentificacao1);
	}

	public void setNumeroIdentificacao1(Double numeroIdentificacao1) {
		this.numeroIdentificacao1 = numeroIdentificacao1;
	}

	public String getIndicadorRateioCredito() {
		if (indicadorRateioCredito == null) {
			indicadorRateioCredito = "";
		}
		return (indicadorRateioCredito);
	}

	public void setIndicadorRateioCredito(String indicadorRateioCredito) {
		this.indicadorRateioCredito = indicadorRateioCredito;
	}

	public Integer getIdentificacaoOcorrencia() {
		if (identificacaoOcorrencia == null) {
			identificacaoOcorrencia = 0;
		}
		return (identificacaoOcorrencia);
	}

	public void setIdentificacaoOcorrencia(Integer identificacaoOcorrencia) {
		this.identificacaoOcorrencia = identificacaoOcorrencia;
	}

	public Integer getTipoCarteira() {
		if (tipoCarteira == null) {
			tipoCarteira = 0;
		}
		return (tipoCarteira);
	}

	public void setTipoCarteira(Integer tipoCarteira) {
		this.tipoCarteira = tipoCarteira;
	}

	public Integer getCodigoCarteira() {
		if (codigoCarteira == null) {
			codigoCarteira = 0;
		}
		return (codigoCarteira);
	}

	public void setCodigoCarteira(Integer codigoCarteira) {
		this.codigoCarteira = codigoCarteira;
	}

	public Integer getCarteira() {
		if (carteira == null) {
			carteira = 0;
		}
		return (carteira);
	}

	public void setCarteira(Integer carteira) {
		this.carteira = carteira;
	}

	public String getCedenteNumeroConta() {
		if (cedenteNumeroConta == null) {
			cedenteNumeroConta = "";
		}
		return (cedenteNumeroConta);
	}

	public void setCedenteNumeroConta(String cedenteNumeroConta) {
		this.cedenteNumeroConta = cedenteNumeroConta;
	}

	public String getCedenteNumeroAgencia() {
		if (cedenteNumeroAgencia == null) {
			cedenteNumeroAgencia = "";
		}
		return (cedenteNumeroAgencia);
	}

	public void setCedenteNumeroAgencia(String cedenteNumeroAgencia) {
		this.cedenteNumeroAgencia = cedenteNumeroAgencia;
	}

	public String getCedenteIdentificacaoNoBanco() {
		if (cedenteIdentificacaoNoBanco == null) {
			cedenteIdentificacaoNoBanco = "";
		}
		return (cedenteIdentificacaoNoBanco);
	}

	public void setCedenteIdentificacaoNoBanco(String cedenteIdentificacaoNoBanco) {
		this.cedenteIdentificacaoNoBanco = cedenteIdentificacaoNoBanco;
	}

	public String getCedenteNumeroInscricaoEmpresa() {
		if (cedenteNumeroInscricaoEmpresa == null) {
			cedenteNumeroInscricaoEmpresa = "";
		}
		return (cedenteNumeroInscricaoEmpresa);
	}

	public void setCedenteNumeroInscricaoEmpresa(String cedenteNumeroInscricaoEmpresa) {
		this.cedenteNumeroInscricaoEmpresa = cedenteNumeroInscricaoEmpresa;
	}

	public Integer getCedenteTipoInscricaoEmpresa() {
		if (cedenteTipoInscricaoEmpresa == null) {
			cedenteTipoInscricaoEmpresa = 0;
		}
		return (cedenteTipoInscricaoEmpresa);
	}

	public void setCedenteTipoInscricaoEmpresa(Integer cedenteTipoInscricaoEmpresa) {
		this.cedenteTipoInscricaoEmpresa = cedenteTipoInscricaoEmpresa;
	}

	public String getSacadoAgenciaCodigo() {
		if (sacadoAgenciaCodigo == null) {
			sacadoAgenciaCodigo = "";
		}
		return (sacadoAgenciaCodigo);
	}

	public void setSacadoAgenciaCodigo(String sacadoAgenciaCodigo) {
		this.sacadoAgenciaCodigo = sacadoAgenciaCodigo;
	}

	public String getSacadoBancoCodigo() {
		if (sacadoBancoCodigo == null) {
			sacadoBancoCodigo = "";
		}
		return (sacadoBancoCodigo);
	}

	public void setSacadoBancoCodigo(String sacadoBancoCodigo) {
		this.sacadoBancoCodigo = sacadoBancoCodigo;
	}

	public String getSacadoUnidadeFederacao() {
		if (sacadoUnidadeFederacao == null) {
			sacadoUnidadeFederacao = "";
		}
		return (sacadoUnidadeFederacao);
	}

	public void setSacadoUnidadeFederacao(String sacadoUnidadeFederacao) {
		this.sacadoUnidadeFederacao = sacadoUnidadeFederacao;
	}

	public String getSacadoCidade() {
		if (sacadoCidade == null) {
			sacadoCidade = "";
		}
		return (sacadoCidade);
	}

	public void setSacadoCidade(String sacadoCidade) {
		this.sacadoCidade = sacadoCidade;
	}

	public Integer getSacadoSufixoCep() {
		if (sacadoSufixoCep == null) {
			sacadoSufixoCep = 0;
		}
		return (sacadoSufixoCep);
	}

	public void setSacadoSufixoCep(Integer sacadoSufixoCep) {
		this.sacadoSufixoCep = sacadoSufixoCep;
	}

	public Integer getSacadoCep() {
		if (sacadoCep == null) {
			sacadoCep = 0;
		}
		return (sacadoCep);
	}

	public void setSacadoCep(Integer sacadoCep) {
		this.sacadoCep = sacadoCep;
	}

	public String getSacadoBairro() {
		if (sacadoBairro == null) {
			sacadoBairro = "";
		}
		return (sacadoBairro);
	}

	public void setSacadoBairro(String sacadoBairro) {
		this.sacadoBairro = sacadoBairro;
	}

	public String getSacadoEndereco() {
		if (sacadoEndereco == null) {
			sacadoEndereco = "";
		}
		return (sacadoEndereco);
	}

	public void setSacadoEndereco(String sacadoEndereco) {
		this.sacadoEndereco = sacadoEndereco;
	}

	public String getSacadoNome() {
		if (sacadoNome == null) {
			sacadoNome = "";
		}
		return (sacadoNome);
	}

	public void setSacadoNome(String sacadoNome) {
		this.sacadoNome = sacadoNome;
	}

	public String getSacadoNumeroInscricaoControle() {
		if (sacadoNumeroInscricaoControle == null) {
			sacadoNumeroInscricaoControle = "";
		}
		return (sacadoNumeroInscricaoControle);
	}

	public void setSacadoNumeroInscricaoControle(String sacadoNumeroInscricaoControle) {
		this.sacadoNumeroInscricaoControle = sacadoNumeroInscricaoControle;
	}

	public Integer getSacadoNumeroInscricaoFilial() {
		if (sacadoNumeroInscricaoFilial == null) {
			sacadoNumeroInscricaoFilial = 0;
		}
		return (sacadoNumeroInscricaoFilial);
	}

	public void setSacadoNumeroInscricaoFilial(Integer sacadoNumeroInscricaoFilial) {
		this.sacadoNumeroInscricaoFilial = sacadoNumeroInscricaoFilial;
	}

	public Integer getSacadoNumeroInscricaoCorpo() {
		if (sacadoNumeroInscricaoCorpo == null) {
			sacadoNumeroInscricaoCorpo = 0;
		}
		return (sacadoNumeroInscricaoCorpo);
	}

	public void setSacadoNumeroInscricaoCorpo(Integer sacadoNumeroInscricaoCorpo) {
		this.sacadoNumeroInscricaoCorpo = sacadoNumeroInscricaoCorpo;
	}

	public Integer getSacadoTipoInscricao() {
		if (sacadoTipoInscricao == null) {
			sacadoTipoInscricao = 0;
		}
		return (sacadoTipoInscricao);
	}

	public void setSacadoTipoInscricao(Integer sacadoTipoInscricao) {
		this.sacadoTipoInscricao = sacadoTipoInscricao;
	}

	public Integer getCodigoMovimentoRemessaRetorno() {
		if (codigoMovimentoRemessaRetorno == null) {
			codigoMovimentoRemessaRetorno = 0;
		}
		return (codigoMovimentoRemessaRetorno);
	}

	public void setCodigoMovimentoRemessaRetorno(Integer codigoMovimentoRemessaRetorno) {
		this.codigoMovimentoRemessaRetorno = codigoMovimentoRemessaRetorno;
	}

	public String getCodigoSeguimentoRegistroDetalhe() {
		if (codigoSeguimentoRegistroDetalhe == null) {
			codigoSeguimentoRegistroDetalhe = "";
		}
		return (codigoSeguimentoRegistroDetalhe);
	}

	public void setCodigoSeguimentoRegistroDetalhe(String codigoSeguimentoRegistroDetalhe) {
		this.codigoSeguimentoRegistroDetalhe = codigoSeguimentoRegistroDetalhe;
	}

	public Integer getNumeroSequencialRegistroLote() {
		if (numeroSequencialRegistroLote == null) {
			numeroSequencialRegistroLote = 0;
		}
		return (numeroSequencialRegistroLote);
	}

	public void setNumeroSequencialRegistroLote(Integer numeroSequencialRegistroLote) {
		this.numeroSequencialRegistroLote = numeroSequencialRegistroLote;
	}

	public Integer getTipoRegistro() {
		if (tipoRegistro == null) {
			tipoRegistro = 0;
		}
		return (tipoRegistro);
	}

	public void setTipoRegistro(Integer tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public Integer getLoteServico() {
		if (loteServico == null) {
			loteServico = 0;
		}
		return (loteServico);
	}

	public void setLoteServico(Integer loteServico) {
		this.loteServico = loteServico;
	}

	public String getCodigoBanco() {
		if (codigoBanco == null) {
			codigoBanco = "";
		}
		return (codigoBanco);
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public RegistroArquivoVO getRegistroArquivo() {
		if (registroArquivo == null) {
			registroArquivo = new RegistroArquivoVO();
		}
		return (registroArquivo);
	}

	public void setRegistroArquivo(RegistroArquivoVO registroArquivo) {
		this.registroArquivo = registroArquivo;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public boolean isBoletoBaixado() {
		return boletoBaixado;
	}

	public void setBoletoBaixado(boolean boletoBaixado) {
		this.boletoBaixado = boletoBaixado;
	}

	public boolean isBoletoNaoEncontrado() {
		return boletoNaoEncontrado;
	}

	public void setBoletoNaoEncontrado(boolean boletoNaoEncontrado) {
		this.boletoNaoEncontrado = boletoNaoEncontrado;
	}

	public void setNumeroSequencialRegistro(Integer numeroSequencialRegistro) {
		this.numeroSequencialRegistro = numeroSequencialRegistro;
	}

	public Integer getNumeroSequencialRegistro() {
		if (numeroSequencialRegistro == null) {
			numeroSequencialRegistro = 0;
		}
		return numeroSequencialRegistro;
	}

	public Double getValorLancamento() {
		if (valorLancamento == null) {
			valorLancamento = 0.0;
		}
		return valorLancamento;
	}

	public void setValorLancamento(Double valorLancamento) {
		this.valorLancamento = valorLancamento;
	}

	public Integer getIndicativoCreditoDebito() {
		if (indicativoCreditoDebito == null) {
			indicativoCreditoDebito = 0;
		}
		return indicativoCreditoDebito;
	}

	public void setIndicativoCreditoDebito(Integer indicativoCreditoDebito) {
		this.indicativoCreditoDebito = indicativoCreditoDebito;
	}

	public Integer getIndicativoValor() {
		if (indicativoValor == null) {
			indicativoValor = 0;
		}
		return indicativoValor;
	}

	public void setIndicativoValor(Integer indicativoValor) {
		this.indicativoValor = indicativoValor;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}

	public void setCodigoContaReceber(Integer codigoContaReceber) {
		this.codigoContaReceber = codigoContaReceber;
	}

	public Integer getCodigoContaReceber() {
		if (codigoContaReceber == null) {
			codigoContaReceber = 0;
		}
		return codigoContaReceber;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RegistroDetalheVO other = (RegistroDetalheVO) obj;
		if (this.codigoContaReceber != other.codigoContaReceber && (this.codigoContaReceber == null || !this.codigoContaReceber.equals(other.codigoContaReceber))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + (this.codigoContaReceber != null ? this.codigoContaReceber.hashCode() : 0);
		return hash;
	}

	public String getCodigoCedenteCodigoConvenioBanco() {
		if (codigoCedenteCodigoConvenioBanco == null) {
			codigoCedenteCodigoConvenioBanco = "";
		}
		return codigoCedenteCodigoConvenioBanco;
	}

	public void setCodigoCedenteCodigoConvenioBanco(String codigoCedenteCodigoConvenioBanco) {
		this.codigoCedenteCodigoConvenioBanco = codigoCedenteCodigoConvenioBanco;
	}

	/**
	 * @return the parcela
	 */
	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}

	/**
	 * @param parcela
	 *            the parcela to set
	 */
	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public Double getTarifaCobranca() {
		if (tarifaCobranca == null) {
			tarifaCobranca = 0.0;
		}
		return tarifaCobranca;
	}

	public void setTarifaCobranca(Double tarifaCobranca) {
		this.tarifaCobranca = tarifaCobranca;
	}

	public Integer getNumeroContaCorrente() {
		if (numeroContaCorrente == null) {
			numeroContaCorrente = 0;
		}
		return numeroContaCorrente;
	}

	public void setNumeroContaCorrente(Integer numeroContaCorrente) {
		this.numeroContaCorrente = numeroContaCorrente;
	}

	public Integer getDigitoVerificadorConta() {
		if (digitoVerificadorConta == null) {
			digitoVerificadorConta = 0;
		}
		return digitoVerificadorConta;
	}

	public void setDigitoVerificadorConta(Integer digitoVerificadorConta) {
		this.digitoVerificadorConta = digitoVerificadorConta;
	}

	public Double getValorTarifa() {
		if (valorTarifa == null) {
			valorTarifa = 0.0;
		}
		return valorTarifa;
	}

	public void setValorTarifa(Double valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	public Boolean getContaReceberAgrupada() {
		if (contaReceberAgrupada == null) {
			contaReceberAgrupada = Boolean.FALSE;
		}
		return contaReceberAgrupada;
	}

	public void setContaReceberAgrupada(Boolean contaReceberAgrupada) {
		this.contaReceberAgrupada = contaReceberAgrupada;
	}

	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarBoleto(ContaCorrenteVO contaCorrenteVO) throws Exception {
		try {
			JBoletoBean jBoletoBean = new JBoletoBean();
			jBoletoBean.setDataDocumento(Uteis.getDataAno4Digitos(getDataOcorrencia()));
			jBoletoBean.setDataProcessamento(Uteis.getDataAno4Digitos(new Date()));
			jBoletoBean.setDataVencimento(Uteis.getDataAno4Digitos(getDataVencimentoTitulo()));
			jBoletoBean.setAgencia(contaCorrenteVO.getAgencia().getNumeroAgencia());
			jBoletoBean.setDvContaCorrente(contaCorrenteVO.getDigito());
			jBoletoBean.setContaCorrente(contaCorrenteVO.getNumero());
			jBoletoBean.setCarteira(contaCorrenteVO.getCarteira());
			jBoletoBean.setNumConvenio(contaCorrenteVO.getConvenio());
			jBoletoBean.setCedente(contaCorrenteVO.getCodigoCedente());

			BoletoBanco banco = BancoFactory.getBoletoInstancia(contaCorrenteVO.getAgencia().getBanco().getNrBanco(), jBoletoBean);
			if (banco instanceof BancoBrasil) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 10);
			} else if (banco instanceof Unibanco) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 14);
			} else if (banco instanceof Hsbc) {
				jBoletoBean.setCodCliente(contaCorrenteVO.getCodigoCedente());
				// Não devem ser calculados os dígitos verificadores e o tipo
				// identificador na linha
				// digitável e no código de brarras.
				// Qualquer dúvida acessar o documento no link na página 7
				// http://www.hsbc.com.br/1/2/br/para-sua-empresa/empresas/gestao-financeira/recebimentos/cobranca/cobranca-nao-registrada/layout-tecnico
				/**
				 * Eu Rodrigo comentei o codigo abaixo, pois ao realizar a
				 * substrig para 13 estava gerando o codigo de barra duplicado
				 * na base de dados
				 * jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero
				 * ().substring(0, 13)));
				 */
				//
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 13);
			} else if (banco instanceof Santander) {
				jBoletoBean.setIOS("0");
				jBoletoBean.setCodCliente(contaCorrenteVO.getCodigoCedente());
				if (getNossoNumero().length() > 8) {
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), getNossoNumero().length());
				} else {
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 8);
				}
			} else if (banco instanceof Bradesco) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 11);
			} else if (banco instanceof Itau) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 8);
			}else if (banco instanceof Banestes) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 17);
			} else if (banco instanceof Sicred) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 9);
			} else if (banco instanceof CaixaEconomica) {
				jBoletoBean.setCedente(String.valueOf(contaCorrenteVO.getCodigoCedente().toString()));
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 15);
			} else {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 14);
			}
			if (banco != null) {
				setCodigoBarra(banco.getCodigoBarras());
			}
		} catch (Exception e) {
			// //System.out.println(e.getMessage());
			throw new Exception("Ocorreu um erro na geração do nosso número das contas a receber. Provavelmente o nr de carteira e/ou convênio estão errados!");
			// throw e;
		}
	}

	public Integer getNumeroAgencia() {
		if (numeroAgencia == null) {
			numeroAgencia = 0;
		}
		return numeroAgencia;
	}

	public void setNumeroAgencia(Integer numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}
	
	public SituacaoRegistroDetalheEnum getSituacaoRegistroDetalheEnum() {
		if(situacaoRegistroDetalheEnum == null) {
			situacaoRegistroDetalheEnum = SituacaoRegistroDetalheEnum.NENHUM;			
		}
		return situacaoRegistroDetalheEnum;
	}

	public void setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum situacaoRegistroDetalheEnum) {
		this.situacaoRegistroDetalheEnum = situacaoRegistroDetalheEnum;
	}

	public boolean isValidarRegistroConfirmado() {
		if (Uteis.isAtributoPreenchido(getSacadoBancoCodigo())) {
			Bancos bancos = Bancos.getEnum(getSacadoBancoCodigo());
			Uteis.checkState(bancos == null, "Layout do Banco não encontrado para número do banco."+ getSacadoBancoCodigo()); 
			switch (bancos) {			
			case BRB:			
			case BRADESCO:
			case HSBC:
			case ITAU:
			case SAFRA:
			case DAYCOVAL:
			case UNIBANCO:
			case ITAU_RETORNO_REMESSA:
			case SANTANDER:
			case BANCO_DO_BRASIL:
			case CAIXA_ECONOMICA_FEDERAL:
			case CAIXA_ECONOMICA_FEDERAL_SICOB_15:
			case CAIXA_ECONOMICA_FEDERAL_SICOB:
			case SICOOB:
			case SICRED:
				return getIdentificacaoOcorrencia().equals(2);
			case REAL:
			case NOSSA_CAIXA:
				return false;
			default:
				return false;
			}
		}
		return false;
	}
	
	public boolean isValidarRegistroRejeitado() {
		if (Uteis.isAtributoPreenchido(getSacadoBancoCodigo())) {
			Bancos bancos = Bancos.getEnum(getSacadoBancoCodigo());
			Uteis.checkState(bancos == null, "Layout do Banco não encontrado para número do banco."+ getSacadoBancoCodigo()); 
			switch (bancos) {			
			case BRB:
			case HSBC:
			case ITAU:
			case SAFRA:
			case DAYCOVAL:
			case UNIBANCO:
			case ITAU_RETORNO_REMESSA:
			case SANTANDER:
			case BANCO_DO_BRASIL:
			case CAIXA_ECONOMICA_FEDERAL:
			case CAIXA_ECONOMICA_FEDERAL_SICOB_15:
			case CAIXA_ECONOMICA_FEDERAL_SICOB:
			case SICRED:
				return getIdentificacaoOcorrencia().equals(3);
			case REAL:
			case NOSSA_CAIXA:
				return false;
			//case SICOOB:
			default:
				return false;
			}
		}
		return false;
	}
	
	public String getMsgRegistroRejeitado() {
		if (Uteis.isAtributoPreenchido(getSacadoBancoCodigo())) {
			Bancos bancos = Bancos.getEnum(getSacadoBancoCodigo().toString());
			Uteis.checkState(bancos == null, "Layout do Banco não encontrado para número do banco."+ getSacadoBancoCodigo());
			switch (bancos) {
			case ITAU:
				return getMsgRegistroRejeitadoItau();
			case BRB:
			case BRADESCO:
				 return MotivoRejeicaoRemessa.getMensagem(getMotivoRegeicao());
			case HSBC:
			case SAFRA:
			case DAYCOVAL:
			case UNIBANCO:
			case ITAU_RETORNO_REMESSA:
			case SANTANDER:
			case BANCO_DO_BRASIL:
			case CAIXA_ECONOMICA_FEDERAL:
			case CAIXA_ECONOMICA_FEDERAL_SICOB_15:
			case CAIXA_ECONOMICA_FEDERAL_SICOB:
			case SICRED:
			case REAL:
			case NOSSA_CAIXA:
				return "";
			//case SICOOB:
			default:
				return "";
			}
		}
		return "";
	}
	
	public String getMsgRegistroRejeitadoItau() {
		int codRejeicao = Integer.parseInt(getMotivoRegeicao().length() > 2 ? getMotivoRegeicao().substring(0, 2) : "0");
		switch (codRejeicao) {
			case 3:
				return "Campo com Erro:Agencia Cobradora - Descrição:Cep sem Atendimento de prosteto no momento!";
			case 4:
				return "Campo com Erro:Estado - Descrição:Sigla do estado inválida!";
			case 5:
				return "Campo com Erro:Data Vencimento - Descrição:Prazo da operação menor que prazo mínimo ou maior que o máximo!";
			case 7:
				return "Campo com Erro:Valor do Título - Descrição:Valor do título maior que 10.000.000,00!";
			case 8:
				return "Campo com Erro:Nome do Pagador - Descrição:Não informado ou deslocado!";
			case 9:
				return "Campo com Erro:Agencia/Conta - Descrição:Agência encerrada!";
			case 10:
				return "Campo com Erro:Logradouro - Descrição:Não informado ou deslocado!";
			case 11:
				return "Campo com Erro:Cep - Descrição:Cep não numérico ou cep inválido!";
			case 12:
				return "Campo com Erro:Sacador/Avalista - Descrição:Nome não informado ou deslocado!";
			case 13:
				return "Campo com Erro:Estado/Cep - Descrição:Cep incompatível com a sigla do estado!";
			case 14:
				return "Campo com Erro:Nosso Número - Descrição:Nosso número já registrado do cadastro do banco ou fora da faixa!";
			case 15:
				return "Campo com Erro:Nosso Número - Descrição:Nosso número em duplicidade no mesmo movimento!";
			case 18:
				return "Campo com Erro:Data de Entrada - Descrição:Data de entrada inválida para operar com estar carteira!";
			case 19:
				return "Campo com Erro:Ocorrência - Descrição:Ocorrência inválida!";
			case 21:
				return "Campo com Erro:Ag.Cobradora - Descrição:Carteira não aceita depositária correspondente. Estado da agência diferente do estado do pagador. Ag. cobradorra não consta no cadastro ou encerrando!";
			case 22:
				return "Campo com Erro:Carteira - Descrição:Carteira não permitida!";
			case 26:
				return "Campo com Erro:Agência/Conta - Descrição:Agência/Conta não liberada para operar com cobrança!";
			case 27:
				return "Campo com Erro:Cnpj Inapto - Descrição:Cnpj do beneficiário inapto devolução de título em garantia!";
			case 29:
				return "Campo com Erro:Código Empresa - Descrição:Categoria da conta inválida!";
			case 30:
				return "Campo com Erro:Entrada Bloqueada - Descrição:Entradas bloqueadas, contas suspensa em cobrança!";
			case 31:
				return "Campo com Erro:Agência/Conta - Descrição:Conta não tem permissão para protestar!";
			case 35:
				return "Campo com Erro:Valor do IOF - Descrição:IOF maior que 5%!";
			case 36:
				return "Campo com Erro:Quantidade de Moeda - Descrição:Quantidade de moeda incompatível com valor do título!";
			case 37:
				return "Campo com Erro:Cnpj/Cpf do Pagador - Descrição:Não número fora de faixa!";
			case 42:
				return "Campo com Erro:Nosso Número - Descrição:Nosso número fora da faixa!";
			case 52:
				return "Campo com Erro:Ag. Cobradora - Descrição:Empresa não aceita banco correspondente!";
			case 53:
				return "Campo com Erro:Ag. Cobradora - Descrição:Empresa não aceita banco correspondente - cobrança mensagem!";
			case 54:
				return "Campo com Erro:Data de Vencimento - Descrição:Banco correspondente - título com vencimento inferior a 15 dias!";
			case 55:
				return "Campo com Erro:Deposito/Banco correspondente - Descrição:Cep não pertence à depositária informada!";
			case 56:
				return "Campo com Erro:Data Vencimento/Banco correspondente - Descrição:Vencimento superior a 180 dias da data de entrada!";
			case 57:
				return "Campo com Erro:Data de Vencimento - Descrição:Cep só depositária Banco do Brasil com vencimento inferior a 8 dias!";
			case 60:
				return "Campo com Erro:Abatimento - Descrição:Valor do abatimento inválido!";
			case 61:
				return "Campo com Erro:Juros de Mora - Descrição:Juros de mora maior que o permitido!";
			case 62:
				return "Campo com Erro:Desconto - Descrição:Valor do desconto maior que valor do título!";
			case 63:
				return "Campo com Erro:Desconto de Antecipação - Descrição:Valor da importância por dia de desconto não permitido!";
			case 64:
				return "Campo com Erro:Data de Emissão - Descrição:Data de emissão do título inválida!";
			case 65:
				return "Campo com Erro:Taxa Financiamento - Descrição:Taxa inválida!";
			case 66:
				return "Campo com Erro:Data de Vencimento - Descrição:Inválida fora de prazo de operação!";
			case 67:
				return "Campo com Erro:Valor/Quantidade - Descrição:Valor do título/Quantidade de moeda inválido!";
			case 68:
				return "Campo com Erro:Carteira - Descrição:Carteira inválida ou não cadastrada no intercâmbio da cobrança!";
			case 69:
				return "Campo com Erro:Carteira - Descrição:Carteira inválida para títulos com rateio de crédito!";
			case 70:
				return "Campo com Erro:Agência/Conta - Descrição:Beneficiário não cadastrado para fazer rateio de crédito!";
			case 78:
				return "Campo com Erro:Agência/Conta - Descrição:Duplicidade de Agência/Conta beneficiária do rateio de crédito!";
			case 80:
				return "Campo com Erro:Agência/Conta - Descrição:Quantidade de contas beneficiárias do rateio maior do que o ermitido!";
			case 81:
				return "Campo com Erro:Agência/Conta - Descrição:Conta para rateio de crédito inválida não pertence ao itaú!";
			case 82:
				return "Campo com Erro:Desconto/Abatimento - Descrição:Desconto/Abatimento não permitido para títulos com rateio de crédito!";
			case 83:
				return "Campo com Erro:Valor do Título - Descrição:Valor do título menor que a soma dos valores estipulados para rateio!";
			case 84:
				return "Campo com Erro:Agência/Conta - Descrição:Agência/Conta beneficiária do rateio é a centralizadora de crédito do beneficiário!";
			case 85:
				return "Campo com Erro:Agência/Conta - Descrição:Agência/Conta do beneficiário é contratual rateio de crédito não permitido!";
			case 86:
				return "Campo com Erro:Tipo de Valor - Descrição:Código do tipo de valor inválido nao previsto para títulos com rateio de crédito!";
			case 87:
				return "Campo com Erro:Agência/Conta - Descrição:Registro tipo 4 sem informação de agência/contas beneficiárias do rateio!";
			case 90:
				return "Campo com Erro:Número da Linha - Descrição:Cobrança mensagem-Número da linha da mensagem inválido ou quantidade de linhas excedidas!";
			case 97:
				return "Campo com Erro:Sem Mensagem - Descrição:Cobrança mensagem sem mensagem, porém com registro do tipo 7 ou 8!";
			case 98:
				return "Campo com Erro:Flash inválido - Descrição:Registro mensagem sem flash cadastrado ou flash informado diferente do cadastrado!";
			case 99:
				return "Campo com Erro:Flash inválido - Descrição:Conta de cobrança com flash cadastro e sem registro de mensagem correspondente!";
				//case SICOOB:
			default:
				return "";
		}
	}
	

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}
}
