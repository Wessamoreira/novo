package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade RegistroTrailer. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class RegistroTrailerVO extends SuperVO {

    private Integer codigo;
    private String codigoBanco;
    private Integer numeroLote;
    private Integer tipoRegistro;
    private Integer quantidadeLote;
    private Integer quantidadeRegistro;
    private Integer quantidadeContas;
    private Integer identificacaoRetorno;
    private Integer identificacaoTipoRegistro;
    private Double valorTotalRegistros;
    private Integer numeroAvisoBancario;
    private Integer quantidadeTitulosEmCobranca;
    private Double valorTitulosEmCobranca;
    private Integer quantidadeRegistrosConfirmacaoEntrada;
    private Double valorRegistrosConfirmacaoEntrada;
    private Integer quantidadeRegistrosLiquidacao;
    private Double valorRegistroLiquidacao;
    private Double valorRegistrosOcorrencia6;
    private Integer quantidadeRegistrosTitulosBaixados;
    private Double valorRegistrosTitulosBaixados;
    private Integer quantidadeRegistrosAbatimentosCancelados;
    private Double valorRegistrosAbatimentosCancelados;
    private Integer quantidadeRegistrosVencimentoAlterado;
    private Double valorRegistrosVencimentoAlterado;
    private Integer quantidadeRegistrosAbatimentosConcedidos;
    private Double valorRegistrosAbatimentosConcedidos;
    private Integer quantidadeRegistrosConfirmacaoProtesto;
    private Double valorRegistrosConfirmacaoProtesto;
    private Double valorTotalRateiosEfetuados;
    private Integer quantidadeTotalRateiosEfetuados;
    private Integer numeroSequencialRegistro;
    private Integer codigoCedente;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>RegistroArquivo </code>.
     */
    private Integer registroArquivo;
	private String numeroAvisoBancarioEmCobranca;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RegistroTrailer</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public RegistroTrailerVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>RegistroTrailerVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(RegistroTrailerVO obj) throws ConsistirException {
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
    }

    /**
     * Retorna o objeto da classe <code>RegistroArquivo</code> relacionado com (
     * <code>RegistroTrailer</code>).
     */
    public Integer getRegistroArquivo() {
        if (registroArquivo == null) {
            registroArquivo = 0;
        }
        return (registroArquivo);
    }

    /**
     * Define o objeto da classe <code>RegistroArquivo</code> relacionado com (
     * <code>RegistroTrailer</code>).
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

    public Integer getQuantidadeTotalRateiosEfetuados() {
        if (quantidadeTotalRateiosEfetuados == null) {
            quantidadeTotalRateiosEfetuados = 0;
        }
        return (quantidadeTotalRateiosEfetuados);
    }

    public void setQuantidadeTotalRateiosEfetuados(Integer quantidadeTotalRateiosEfetuados) {
        this.quantidadeTotalRateiosEfetuados = quantidadeTotalRateiosEfetuados;
    }

    public Double getValorTotalRateiosEfetuados() {
        if (valorTotalRateiosEfetuados == null) {
            valorTotalRateiosEfetuados = 0.0;
        }
        return (valorTotalRateiosEfetuados);
    }

    public void setValorTotalRateiosEfetuados(Double valorTotalRateiosEfetuados) {
        this.valorTotalRateiosEfetuados = valorTotalRateiosEfetuados;
    }

    public Double getValorRegistrosConfirmacaoProtesto() {
        if (valorRegistrosConfirmacaoProtesto == null) {
            valorRegistrosConfirmacaoProtesto = 0.0;
        }
        return (valorRegistrosConfirmacaoProtesto);
    }

    public void setValorRegistrosConfirmacaoProtesto(Double valorRegistrosConfirmacaoProtesto) {
        this.valorRegistrosConfirmacaoProtesto = valorRegistrosConfirmacaoProtesto;
    }

    public Integer getQuantidadeRegistrosConfirmacaoProtesto() {
        if (quantidadeRegistrosConfirmacaoProtesto == null) {
            quantidadeRegistrosConfirmacaoProtesto = 0;
        }
        return (quantidadeRegistrosConfirmacaoProtesto);
    }

    public void setQuantidadeRegistrosConfirmacaoProtesto(Integer quantidadeRegistrosConfirmacaoProtesto) {
        this.quantidadeRegistrosConfirmacaoProtesto = quantidadeRegistrosConfirmacaoProtesto;
    }

    public Double getValorRegistrosAbatimentosConcedidos() {
        if (valorRegistrosAbatimentosConcedidos == null) {
            valorRegistrosAbatimentosConcedidos = 0.0;
        }
        return (valorRegistrosAbatimentosConcedidos);
    }

    public void setValorRegistrosAbatimentosConcedidos(Double valorRegistrosAbatimentosConcedidos) {
        this.valorRegistrosAbatimentosConcedidos = valorRegistrosAbatimentosConcedidos;
    }

    public Integer getQuantidadeRegistrosAbatimentosConcedidos() {
        if (quantidadeRegistrosAbatimentosConcedidos == null) {
            quantidadeRegistrosAbatimentosConcedidos = 0;
        }
        return (quantidadeRegistrosAbatimentosConcedidos);
    }

    public void setQuantidadeRegistrosAbatimentosConcedidos(Integer quantidadeRegistrosAbatimentosConcedidos) {
        this.quantidadeRegistrosAbatimentosConcedidos = quantidadeRegistrosAbatimentosConcedidos;
    }

    public Double getValorRegistrosVencimentoAlterado() {
        if (valorRegistrosVencimentoAlterado == null) {
            valorRegistrosVencimentoAlterado = 0.0;
        }
        return (valorRegistrosVencimentoAlterado);
    }

    public void setValorRegistrosVencimentoAlterado(Double valorRegistrosVencimentoAlterado) {
        this.valorRegistrosVencimentoAlterado = valorRegistrosVencimentoAlterado;
    }

    public Integer getQuantidadeRegistrosVencimentoAlterado() {
        if (quantidadeRegistrosVencimentoAlterado == null) {
            quantidadeRegistrosVencimentoAlterado = 0;
        }
        return (quantidadeRegistrosVencimentoAlterado);
    }

    public void setQuantidadeRegistrosVencimentoAlterado(Integer quantidadeRegistrosVencimentoAlterado) {
        this.quantidadeRegistrosVencimentoAlterado = quantidadeRegistrosVencimentoAlterado;
    }

    public Double getValorRegistrosAbatimentosCancelados() {
        if (valorRegistrosAbatimentosCancelados == null) {
            valorRegistrosAbatimentosCancelados = 0.0;
        }
        return (valorRegistrosAbatimentosCancelados);
    }

    public void setValorRegistrosAbatimentosCancelados(Double valorRegistrosAbatimentosCancelados) {
        this.valorRegistrosAbatimentosCancelados = valorRegistrosAbatimentosCancelados;
    }

    public Integer getQuantidadeRegistrosAbatimentosCancelados() {
        if (quantidadeRegistrosAbatimentosCancelados == null) {
            quantidadeRegistrosAbatimentosCancelados = 0;
        }
        return (quantidadeRegistrosAbatimentosCancelados);
    }

    public void setQuantidadeRegistrosAbatimentosCancelados(Integer quantidadeRegistrosAbatimentosCancelados) {
        this.quantidadeRegistrosAbatimentosCancelados = quantidadeRegistrosAbatimentosCancelados;
    }

    public Double getValorRegistrosTitulosBaixados() {
        if (valorRegistrosTitulosBaixados == null) {
            valorRegistrosTitulosBaixados = 0.0;
        }
        return (valorRegistrosTitulosBaixados);
    }

    public void setValorRegistrosTitulosBaixados(Double valorRegistrosTitulosBaixados) {
        this.valorRegistrosTitulosBaixados = valorRegistrosTitulosBaixados;
    }

    public Integer getQuantidadeRegistrosTitulosBaixados() {
        if (quantidadeRegistrosTitulosBaixados == null) {
            quantidadeRegistrosTitulosBaixados = 0;
        }
        return (quantidadeRegistrosTitulosBaixados);
    }

    public void setQuantidadeRegistrosTitulosBaixados(Integer quantidadeRegistrosTitulosBaixados) {
        this.quantidadeRegistrosTitulosBaixados = quantidadeRegistrosTitulosBaixados;
    }

    public Double getValorRegistrosOcorrencia6() {
        if (valorRegistrosOcorrencia6 == null) {
            valorRegistrosOcorrencia6 = 0.0;
        }
        return (valorRegistrosOcorrencia6);
    }

    public void setValorRegistrosOcorrencia6(Double valorRegistrosOcorrencia6) {
        this.valorRegistrosOcorrencia6 = valorRegistrosOcorrencia6;
    }

    public Double getValorRegistroLiquidacao() {
        if (valorRegistroLiquidacao == null) {
            valorRegistroLiquidacao = 0.0;
        }
        return (valorRegistroLiquidacao);
    }

    public void setValorRegistroLiquidacao(Double valorRegistroLiquidacao) {
        this.valorRegistroLiquidacao = valorRegistroLiquidacao;
    }

    public Integer getQuantidadeRegistrosLiquidacao() {
        if (quantidadeRegistrosLiquidacao == null) {
            quantidadeRegistrosLiquidacao = 0;
        }
        return (quantidadeRegistrosLiquidacao);
    }

    public void setQuantidadeRegistrosLiquidacao(Integer quantidadeRegistrosLiquidacao) {
        this.quantidadeRegistrosLiquidacao = quantidadeRegistrosLiquidacao;
    }

    public Double getValorRegistrosConfirmacaoEntrada() {
        if (valorRegistrosConfirmacaoEntrada == null) {
            valorRegistrosConfirmacaoEntrada = 0.0;
        }
        return (valorRegistrosConfirmacaoEntrada);
    }

    public void setValorRegistrosConfirmacaoEntrada(Double valorRegistrosConfirmacaoEntrada) {
        this.valorRegistrosConfirmacaoEntrada = valorRegistrosConfirmacaoEntrada;
    }

    public Integer getQuantidadeRegistrosConfirmacaoEntrada() {
        if (quantidadeRegistrosConfirmacaoEntrada == null) {
            quantidadeRegistrosConfirmacaoEntrada = 0;
        }
        return (quantidadeRegistrosConfirmacaoEntrada);
    }

    public void setQuantidadeRegistrosConfirmacaoEntrada(Integer quantidadeRegistrosConfirmacaoEntrada) {
        this.quantidadeRegistrosConfirmacaoEntrada = quantidadeRegistrosConfirmacaoEntrada;
    }

    public Double getValorTitulosEmCobranca() {
        if (valorTitulosEmCobranca == null) {
            valorTitulosEmCobranca = 0.0;
        }
        return (valorTitulosEmCobranca);
    }

    public void setValorTitulosEmCobranca(Double valorTitulosEmCobranca) {
        this.valorTitulosEmCobranca = valorTitulosEmCobranca;
    }

    public Integer getQuantidadeTitulosEmCobranca() {
        if (quantidadeTitulosEmCobranca == null) {
            quantidadeTitulosEmCobranca = 0;
        }
        return (quantidadeTitulosEmCobranca);
    }

    public void setQuantidadeTitulosEmCobranca(Integer quantidadeTitulosEmCobranca) {
        this.quantidadeTitulosEmCobranca = quantidadeTitulosEmCobranca;
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

    public Double getValorTotalRegistros() {
        if (valorTotalRegistros == null) {
            valorTotalRegistros = 0.0;
        }
        return (valorTotalRegistros);
    }

    public void setValorTotalRegistros(Double valorTotalRegistros) {
        this.valorTotalRegistros = valorTotalRegistros;
    }

    public Integer getIdentificacaoTipoRegistro() {
        if (identificacaoTipoRegistro == null) {
            identificacaoTipoRegistro = 0;
        }
        return (identificacaoTipoRegistro);
    }

    public void setIdentificacaoTipoRegistro(Integer identificacaoTipoRegistro) {
        this.identificacaoTipoRegistro = identificacaoTipoRegistro;
    }

    public Integer getIdentificacaoRetorno() {
        if (identificacaoRetorno == null) {
            identificacaoRetorno = 0;
        }
        return (identificacaoRetorno);
    }

    public void setIdentificacaoRetorno(Integer identificacaoRetorno) {
        this.identificacaoRetorno = identificacaoRetorno;
    }

    public Integer getQuantidadeContas() {
        if (quantidadeContas == null) {
            quantidadeContas = 0;
        }
        return (quantidadeContas);
    }

    public void setQuantidadeContas(Integer quantidadeContas) {
        this.quantidadeContas = quantidadeContas;
    }

    public Integer getQuantidadeRegistro() {
        if (quantidadeRegistro == null) {
            quantidadeRegistro = 0;
        }
        return (quantidadeRegistro);
    }

    public void setQuantidadeRegistro(Integer quantidadeRegistro) {
        this.quantidadeRegistro = quantidadeRegistro;
    }

    public Integer getQuantidadeLote() {
        if (quantidadeLote == null) {
            quantidadeLote = 0;
        }
        return (quantidadeLote);
    }

    public void setQuantidadeLote(Integer quantidadeLote) {
        this.quantidadeLote = quantidadeLote;
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

	public Integer getCodigoCedente() {
		if (codigoCedente == null) {
			codigoCedente = 0;
		}
		return codigoCedente;
	}

	public void setCodigoCedente(Integer codigoCedente) {
		this.codigoCedente = codigoCedente;
	}

	public void setNumeroAvisoBancarioEmCobranca(String numeroAvisoBancarioEmCobranca) {
		this.numeroAvisoBancarioEmCobranca =numeroAvisoBancarioEmCobranca ;
		
	}
	
	public String getNumeroAvisoBancarioEmCobranca() {
		return numeroAvisoBancarioEmCobranca ;
		
	}
}
