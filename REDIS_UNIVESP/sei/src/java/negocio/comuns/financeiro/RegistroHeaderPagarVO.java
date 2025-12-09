package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class RegistroHeaderPagarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1576818197481206973L;
	private Integer codigo;
	private String codigoBanco;
	private String loteServico;
	private Integer tipoRegistro;
	private Integer tipoInscricaoEmpresa;
	private Long numeroInscricaoEmpresa;
	private String codigoConvenioBanco;
	private Integer numeroAgencia;
	private String digitoAgencia;
	private String numeroConta;
	private String digitoConta;
	private Integer digitoAgenciaConta;
	private String nomeEmpresa;
	private String nomeBanco;
	private Integer codigoRemessaRetorno;
	private Date dataGeracaoArquivo;
	private String horaGeracaoArquivo;
	private Integer numeroSequencialArquivo;
	private Integer numeroVersaoArquivo;
	private Integer densidadeGravacao;
	private String reservadoBanco;
	private String reservadoEmpresa;
	private String ocorrenciaRetorno;
	private ControleCobrancaPagarVO controleCobrancaPagarVO;

	/**
	 * Construtor padrão da classe <code>RegistroHeader</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public RegistroHeaderPagarVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>RegistroHeaderVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(RegistroHeaderPagarVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
	}	

	public ControleCobrancaPagarVO getControleCobrancaPagarVO() {
		if (controleCobrancaPagarVO == null) {
			controleCobrancaPagarVO = new ControleCobrancaPagarVO();
		}
		return controleCobrancaPagarVO;
	}

	public void setControleCobrancaPagarVO(ControleCobrancaPagarVO controleCobrancaPagarVO) {
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
	}

	public void setOcorrenciaRetorno(String ocorrenciaRetorno) {
		this.ocorrenciaRetorno = ocorrenciaRetorno;
	}

	public String getReservadoEmpresa() {
		if (reservadoEmpresa == null) {
			reservadoEmpresa = "";
		}
		return (reservadoEmpresa);
	}

	public void setReservadoEmpresa(String reservadoEmpresa) {
		this.reservadoEmpresa = reservadoEmpresa;
	}

	public Integer getDensidadeGravacao() {
		if (densidadeGravacao == null) {
			densidadeGravacao = 0;
		}
		return (densidadeGravacao);
	}

	public void setDensidadeGravacao(Integer densidadeGravacao) {
		this.densidadeGravacao = densidadeGravacao;
	}

	public Integer getNumeroVersaoArquivo() {
		if (numeroVersaoArquivo == null) {
			numeroVersaoArquivo = 0;
		}
		return (numeroVersaoArquivo);
	}

	public void setNumeroVersaoArquivo(Integer numeroVersaoArquivo) {
		this.numeroVersaoArquivo = numeroVersaoArquivo;
	}

	public Integer getNumeroSequencialArquivo() {
		if (numeroSequencialArquivo == null) {
			numeroSequencialArquivo = 0;
		}
		return (numeroSequencialArquivo);
	}

	public void setNumeroSequencialArquivo(Integer numeroSequencialArquivo) {
		this.numeroSequencialArquivo = numeroSequencialArquivo;
	}

	public Date getDataGeracaoArquivo() {
		if (dataGeracaoArquivo == null) {
			dataGeracaoArquivo = new Date();
		}
		return (dataGeracaoArquivo);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	public String getDataGeracaoArquivo_Apresentar() {
		return (Uteis.getData(dataGeracaoArquivo));
	}

	public void setDataGeracaoArquivo(Date dataGeracaoArquivo) {
		this.dataGeracaoArquivo = dataGeracaoArquivo;
	}

	public Integer getCodigoRemessaRetorno() {
		if (codigoRemessaRetorno == null) {
			codigoRemessaRetorno = 0;
		}
		return (codigoRemessaRetorno);
	}

	public void setCodigoRemessaRetorno(Integer codigoRemessaRetorno) {
		this.codigoRemessaRetorno = codigoRemessaRetorno;
	}

	public String getNomeEmpresa() {
		if (nomeEmpresa == null) {
			nomeEmpresa = "";
		}
		return (nomeEmpresa);
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public Integer getDigitoAgenciaConta() {
		if (digitoAgenciaConta == null) {
			digitoAgenciaConta = 0;
		}
		return (digitoAgenciaConta);
	}

	public void setDigitoAgenciaConta(Integer digitoAgenciaConta) {
		this.digitoAgenciaConta = digitoAgenciaConta;
	}

	public String getDigitoConta() {
		if (digitoConta == null) {
			digitoConta = "";
		}
		return (digitoConta);
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}

	public String getNumeroConta() {
		if (numeroConta == null) {
			numeroConta = "";
		}
		return (numeroConta);
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getDigitoAgencia() {
		if (digitoAgencia == null) {
			digitoAgencia = "";
		}
		return (digitoAgencia);
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public Integer getNumeroAgencia() {
		if (numeroAgencia == null) {
			numeroAgencia = 0;
		}
		return (numeroAgencia);
	}

	public void setNumeroAgencia(Integer numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public String getCodigoConvenioBanco() {
		if (codigoConvenioBanco == null) {
			codigoConvenioBanco = "";
		}
		return (codigoConvenioBanco);
	}

	public void setCodigoConvenioBanco(String codigoConvenioBanco) {
		this.codigoConvenioBanco = codigoConvenioBanco;
	}

	public Long getNumeroInscricaoEmpresa() {
		if (numeroInscricaoEmpresa == null) {
			numeroInscricaoEmpresa = 0L;
		}
		return (numeroInscricaoEmpresa);
	}

	public void setNumeroInscricaoEmpresa(Long numeroInscricaoEmpresa) {
		this.numeroInscricaoEmpresa = numeroInscricaoEmpresa;
	}

	public Integer getTipoInscricaoEmpresa() {
		if (tipoInscricaoEmpresa == null) {
			tipoInscricaoEmpresa = 0;
		}
		return (tipoInscricaoEmpresa);
	}

	public void setTipoInscricaoEmpresa(Integer tipoInscricaoEmpresa) {
		this.tipoInscricaoEmpresa = tipoInscricaoEmpresa;
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

	public String getCodigoBanco() {
		if (codigoBanco == null) {
			codigoBanco = "";
		}
		return (codigoBanco);
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
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

	public String getOcorrenciaRetorno() {
		if (ocorrenciaRetorno == null) {
			ocorrenciaRetorno = "";
		}
		return ocorrenciaRetorno;
	}

	public void setOcorrenciaRetonro(String ocorrenciaRetorno) {
		this.ocorrenciaRetorno = ocorrenciaRetorno;
	}

	public String getLoteServico() {
		if (loteServico == null) {
			loteServico = "";
		}
		return loteServico;
	}

	public void setLoteServico(String loteServico) {
		this.loteServico = loteServico;
	}

	public String getNomeBanco() {
		if (nomeBanco == null) {
			nomeBanco = "";
		}
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getHoraGeracaoArquivo() {
		if (horaGeracaoArquivo == null) {
			horaGeracaoArquivo = "";
		}
		return horaGeracaoArquivo;
	}

	public void setHoraGeracaoArquivo(String horaGeracaoArquivo) {
		this.horaGeracaoArquivo = horaGeracaoArquivo;
	}

	public String getReservadoBanco() {
		if (reservadoBanco == null) {
			reservadoBanco = "";
		}
		return reservadoBanco;
	}

	public void setReservadoBanco(String reservadoBanco) {
		this.reservadoBanco = reservadoBanco;
	}

}
