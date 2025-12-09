package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade RegistroHeader. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class RegistroHeaderVO extends SuperVO {

    private Integer codigo;
    private String codigoBanco;
    private Integer numeroLote;
    private Integer tipoRegistro;
    private Integer identificacaoRegistro;
    private Integer identirifacaoArquivoRetorno;
    private String literalRetorno;
    private Integer codigoServico;
    private Integer literalServico;
    private Integer codigoEmpresa;
    private String nomeEmpresaExtenso;
    private Integer tipoInscricaoEmpresa;
    private Long numeroInscricaoEmpresa;
    private String codigoConvenioBanco;
    private Integer numeroAgencia;
    private String digitoAgencia;
    private Integer numeroConta;
    private String digitoConta;
    private Integer digitoAgenciaConta;
    private String nomeEmpresa;
    private String nomeBanco;
    private Integer codigoRemessaRetorno;
    private Date dataGeracaoArquivo;
    private Date dataCredito;
    private Integer numeroSequencialArquivo;
    private Integer numeroVersaoArquivo;
    private Integer densidadeGravacao;
    private String reservadoEmpresa;
    private Integer numeroAvisoBancario;
    private Integer numeroSequencialRegistro;
    private Integer registroArquivo;
    private String literalCobranca;
    private Integer codigoCedente;
    private String situacaoRemessa;
	private String linhaHeader;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RegistroHeader</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public RegistroHeaderVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>RegistroHeaderVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(RegistroHeaderVO obj) throws ConsistirException {
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
        setLiteralRetorno(getLiteralRetorno().toUpperCase());
        setNomeEmpresaExtenso(getNomeEmpresaExtenso().toUpperCase());
        setCodigoConvenioBanco(getCodigoConvenioBanco().toUpperCase());
        setDigitoAgencia(getDigitoAgencia().toUpperCase());
        setDigitoConta(getDigitoConta().toUpperCase());
        setNomeEmpresa(getNomeEmpresa().toUpperCase());
        setNomeBanco(getNomeBanco().toUpperCase());
        setReservadoEmpresa(getReservadoEmpresa().toUpperCase());
    }

    /**
     * Retorna o objeto da classe <code>RegistroArquivo</code> relacionado com (
     * <code>RegistroHeader</code>).
     */
    public Integer getRegistroArquivo() {
        if (registroArquivo == null) {
            registroArquivo = 0;
        }
        return (registroArquivo);
    }

    /**
     * Define o objeto da classe <code>RegistroArquivo</code> relacionado com (
     * <code>RegistroHeader</code>).
     */
    public void setRegistroArquivo(Integer registroArquivo) {
        this.registroArquivo = registroArquivo;
    }

    public Integer getNumeroSequencialRegistro() {
        if (numeroSequencialRegistro == null) {
            numeroSequencialRegistro = 0;
        }
        return (numeroSequencialRegistro);
    }

    public void setNumeroSequencialRegistro(Integer numeroSequencialRegistro) {
        this.numeroSequencialRegistro = numeroSequencialRegistro;
    }

    public Integer getNumeroAvisoBancario() {
        if (numeroAvisoBancario == null) {
            numeroAvisoBancario = 0;
        }
        return (numeroAvisoBancario);
    }

    public void setNumeroAvisoBancario(Integer numeroAvisoBancario) {
        this.numeroAvisoBancario = numeroAvisoBancario;
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

    public Date getDataGeracaoArquivo() {
        if (dataGeracaoArquivo == null) {
            dataGeracaoArquivo = new Date();
        }
        return (dataGeracaoArquivo);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
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

    public String getNomeBanco() {
        if (nomeBanco == null) {
            nomeBanco = "";
        }
        return (nomeBanco);
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
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

    public Integer getNumeroConta() {
        if (numeroConta == null) {
            numeroConta = 0;
        }
        return (numeroConta);
    }

    public void setNumeroConta(Integer numeroConta) {
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

    public String getNomeEmpresaExtenso() {
        if (nomeEmpresaExtenso == null) {
            nomeEmpresaExtenso = "";
        }
        return (nomeEmpresaExtenso);
    }

    public void setNomeEmpresaExtenso(String nomeEmpresaExtenso) {
        this.nomeEmpresaExtenso = nomeEmpresaExtenso;
    }

    public Integer getCodigoEmpresa() {
        if (codigoEmpresa == null) {
            codigoEmpresa = 0;
        }
        return (codigoEmpresa);
    }

    public void setCodigoEmpresa(Integer codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public Integer getLiteralServico() {
        if (literalServico == null) {
            literalServico = 0;
        }
        return (literalServico);
    }

    public void setLiteralServico(Integer literalServico) {
        this.literalServico = literalServico;
    }

    public Integer getCodigoServico() {
        if (codigoServico == null) {
            codigoServico = 0;
        }
        return (codigoServico);
    }

    public void setCodigoServico(Integer codigoServico) {
        this.codigoServico = codigoServico;
    }

    public String getLiteralRetorno() {
        if (literalRetorno == null) {
            literalRetorno = "";
        }
        return (literalRetorno);
    }

    public void setLiteralRetorno(String literalRetorno) {
        this.literalRetorno = literalRetorno;
    }

    public Integer getIdentirifacaoArquivoRetorno() {
        if (identirifacaoArquivoRetorno == null) {
            identirifacaoArquivoRetorno = 0;
        }
        return (identirifacaoArquivoRetorno);
    }

    public void setIdentirifacaoArquivoRetorno(Integer identirifacaoArquivoRetorno) {
        this.identirifacaoArquivoRetorno = identirifacaoArquivoRetorno;
    }

    public Integer getIdentificacaoRegistro() {
        if (identificacaoRegistro == null) {
            identificacaoRegistro = 0;
        }
        return (identificacaoRegistro);
    }

    public void setIdentificacaoRegistro(Integer identificacaoRegistro) {
        this.identificacaoRegistro = identificacaoRegistro;
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

    public Integer getNumeroLote() {
        if (numeroLote == null) {
            numeroLote = 0;
        }
        return (numeroLote);
    }

    public void setNumeroLote(Integer numeroLote) {
        this.numeroLote = numeroLote;
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

	public String getLiteralCobranca() {
		if (literalCobranca == null) {
			literalCobranca = "";
		}
		return literalCobranca;
	}

	public void setLiteralCobranca(String literalCobranca) {
		this.literalCobranca = literalCobranca;
	}

	public Integer getCodigoCedente() {
		if (codigoCedente == null) {
			codigoCedente = 0;
		}
		return codigoCedente;
	}

	public void setCodigoCedente(Integer codigoCedente) {
		this.codigoCedente = codigoCedente;
	}
	

	public String getSituacaoRemessa() {
		if (situacaoRemessa == null) {
			situacaoRemessa = "";
		}
		return situacaoRemessa;
	}

	public void setSituacaoRemessa(String situacaoRemessa) {
		this.situacaoRemessa = situacaoRemessa;
	}

	public String getLinhaHeader() {
		if (linhaHeader == null) {
			linhaHeader = "";
		}
		return linhaHeader;
	}

	public void setLinhaHeader(String linhaHeader) {
		this.linhaHeader = linhaHeader;
	}

}
