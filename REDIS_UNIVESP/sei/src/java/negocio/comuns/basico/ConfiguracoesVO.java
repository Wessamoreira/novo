package negocio.comuns.basico;

import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.LayoutComprovanteMatriculaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Reponsável por manter os dados da entidade Configuracoes. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

public class ConfiguracoesVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String mensagemPlanoDeEstudo;
    private String mensagemBoletimAcademico;
    private Integer nrDiasPrimeiroAvisoRiscoSuspensao;
    private Integer nrDiasSegundoAvisoRiscoSuspensao;
    private Integer nrDiasTerceiroAvisoRiscoSuspensao;
    private Integer nrDiasQuartoAvisoRiscoSuspensao;
    private Integer periodicidadeQuartoAvisoRiscoSuspensao;
    private Integer nrDiasSuspenderMatriculaPendenciaDocumentos;
    private Integer nrDiasListarMatriculaPendenciaDocumentosParaCancelamento;
    private Boolean controlarSuspensaoMatriculaPendenciaDocumentos;
    
    private Boolean controlarNotificacaoPendenciaDocumentos;
    private Integer nrDiaAposInicioAulaNotificarPendenciaDocumento;
    private Integer periodicidadeNotificarPendenciaDocumento;
    
    
    private LayoutComprovanteMatriculaEnum layoutPadraoComprovanteMatricula;
    private Boolean padrao;
    private Boolean enviarMensagemNotificacaoFrequenciaAula;
    private Integer numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula;
    private Integer porcentagemConsiderarFrequenciaAulaBaixa;
    private Integer periodicidadeNotificacaoFrequenciaAula;
    private Integer primeiraNotificacaoNaoLancamentoRegistroAula;
    private Integer segundaNotificacaoNaoLancamentoRegistroAula;
    private Integer terceiraNotificacaoNaoLancamentoRegistroAula;
    private Integer quartaNotificacaoNaoLancamentoRegistroAula;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Configuracoes</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ConfiguracoesVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ConfiguracoesVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ConfiguracoesVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Configuracoes) deve ser informado.");
        }
        if (obj.getEnviarMensagemNotificacaoFrequenciaAula()) {
            if(obj.getNumeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula().intValue() == 0){
                throw new ConsistirException(UteisJSF.internacionalizar("msg_Configuracoes_numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula"));
            }
            if(obj.getPorcentagemConsiderarFrequenciaAulaBaixa().intValue() == 0){
                throw new ConsistirException(UteisJSF.internacionalizar("msg_Configuracoes_porcentagemConsiderarFrequenciaAulaBaixa"));
            }
            if(obj.getPeriodicidadeNotificacaoFrequenciaAula() < 0){
                throw new ConsistirException(UteisJSF.internacionalizar("msg_Configuracoes_periodicidadeNotificacaoFrequenciaAula"));
            }
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
        setNome(getNome().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
    }

    public Boolean getPadrao() {
        if (padrao == null) {
            padrao = Boolean.TRUE;
        }
        return (padrao);
    }

    public Boolean isPadrao() {
        if (padrao == null) {
            padrao = false;
        }
        return (padrao);
    }

    public void setPadrao(Boolean padrao) {
        this.padrao = padrao;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getMensagemPlanoDeEstudo() {
        if (mensagemPlanoDeEstudo == null) {
            mensagemPlanoDeEstudo = "";
        }
        return mensagemPlanoDeEstudo;
    }

    public void setMensagemPlanoDeEstudo(String mensagemPlanoDeEstudo) {
        this.mensagemPlanoDeEstudo = mensagemPlanoDeEstudo;
    }

    public String getMensagemBoletimAcademico() {
        if (mensagemBoletimAcademico == null) {
            mensagemBoletimAcademico = "";
        }
        return mensagemBoletimAcademico;
    }

    public void setMensagemBoletimAcademico(String mensagemBoletimAcademico) {
        this.mensagemBoletimAcademico = mensagemBoletimAcademico;
    }

    /**
     * @return the nrDiasPrimeiroAvisoRiscoSuspensao
     */
    public Integer getNrDiasPrimeiroAvisoRiscoSuspensao() {
        if (nrDiasPrimeiroAvisoRiscoSuspensao == null) {
            nrDiasPrimeiroAvisoRiscoSuspensao = 0;
        }
        return nrDiasPrimeiroAvisoRiscoSuspensao;
    }

    /**
     * @param nrDiasPrimeiroAvisoRiscoSuspensao the nrDiasPrimeiroAvisoRiscoSuspensao to set
     */
    public void setNrDiasPrimeiroAvisoRiscoSuspensao(Integer nrDiasPrimeiroAvisoRiscoSuspensao) {
        this.nrDiasPrimeiroAvisoRiscoSuspensao = nrDiasPrimeiroAvisoRiscoSuspensao;
    }

    /**
     * @return the nrDiasSegundoAvisoRiscoSuspensao
     */
    public Integer getNrDiasSegundoAvisoRiscoSuspensao() {
        if (nrDiasSegundoAvisoRiscoSuspensao == null) {
            nrDiasSegundoAvisoRiscoSuspensao = 0;
        }
        return nrDiasSegundoAvisoRiscoSuspensao;
    }

    /**
     * @param nrDiasSegundoAvisoRiscoSuspensao the nrDiasSegundoAvisoRiscoSuspensao to set
     */
    public void setNrDiasSegundoAvisoRiscoSuspensao(Integer nrDiasSegundoAvisoRiscoSuspensao) {
        this.nrDiasSegundoAvisoRiscoSuspensao = nrDiasSegundoAvisoRiscoSuspensao;
    }

    /**
     * @return the nrDiasTerceiroAvisoRiscoSuspensao
     */
    public Integer getNrDiasTerceiroAvisoRiscoSuspensao() {
        if (nrDiasTerceiroAvisoRiscoSuspensao == null) {
            nrDiasTerceiroAvisoRiscoSuspensao = 0;
        }
        return nrDiasTerceiroAvisoRiscoSuspensao;
    }

    /**
     * @param nrDiasTerceiroAvisoRiscoSuspensao the nrDiasTerceiroAvisoRiscoSuspensao to set
     */
    public void setNrDiasTerceiroAvisoRiscoSuspensao(Integer nrDiasTerceiroAvisoRiscoSuspensao) {
        this.nrDiasTerceiroAvisoRiscoSuspensao = nrDiasTerceiroAvisoRiscoSuspensao;
    }

    /**
     * @return the nrDiasSuspenderMatriculaPendenciaDocumentos
     */
    public Integer getNrDiasSuspenderMatriculaPendenciaDocumentos() {
        if (nrDiasSuspenderMatriculaPendenciaDocumentos == null) {
            nrDiasSuspenderMatriculaPendenciaDocumentos = 0;
        }
        return nrDiasSuspenderMatriculaPendenciaDocumentos;
    }

    /**
     * @param nrDiasSuspenderMatriculaPendenciaDocumentos the nrDiasSuspenderMatriculaPendenciaDocumentos to set
     */
    public void setNrDiasSuspenderMatriculaPendenciaDocumentos(Integer nrDiasSuspenderMatriculaPendenciaDocumentos) {
        this.nrDiasSuspenderMatriculaPendenciaDocumentos = nrDiasSuspenderMatriculaPendenciaDocumentos;
    }

    /**
     * @return the nrDiasListarMatriculaPendenciaDocumentosParaCancelamento
     */
    public Integer getNrDiasListarMatriculaPendenciaDocumentosParaCancelamento() {
        if (nrDiasListarMatriculaPendenciaDocumentosParaCancelamento == null) {
            nrDiasListarMatriculaPendenciaDocumentosParaCancelamento = 0;
        }
        return nrDiasListarMatriculaPendenciaDocumentosParaCancelamento;
    }

    /**
     * @param nrDiasListarMatriculaPendenciaDocumentosParaCancelamento the nrDiasListarMatriculaPendenciaDocumentosParaCancelamento to set
     */
    public void setNrDiasListarMatriculaPendenciaDocumentosParaCancelamento(Integer nrDiasListarMatriculaPendenciaDocumentosParaCancelamento) {
        this.nrDiasListarMatriculaPendenciaDocumentosParaCancelamento = nrDiasListarMatriculaPendenciaDocumentosParaCancelamento;
    }

    /**
     * @return the controlarSuspensaoMatriculaPendenciaDocumentos
     */
    public Boolean getControlarSuspensaoMatriculaPendenciaDocumentos() {
        if (controlarSuspensaoMatriculaPendenciaDocumentos == null) {
            return Boolean.FALSE;
        }
        return controlarSuspensaoMatriculaPendenciaDocumentos;
    }

    /**
     * @param controlarSuspensaoMatriculaPendenciaDocumentos the controlarSuspensaoMatriculaPendenciaDocumentos to set
     */
    public void setControlarSuspensaoMatriculaPendenciaDocumentos(Boolean controlarSuspensaoMatriculaPendenciaDocumentos) {
        this.controlarSuspensaoMatriculaPendenciaDocumentos = controlarSuspensaoMatriculaPendenciaDocumentos;
    }

    
    public LayoutComprovanteMatriculaEnum getLayoutPadraoComprovanteMatricula() {
        if(layoutPadraoComprovanteMatricula== null){
            layoutPadraoComprovanteMatricula = LayoutComprovanteMatriculaEnum.LAYOUT_01;
        }
        return layoutPadraoComprovanteMatricula;
    }

    
    public void setLayoutPadraoComprovanteMatricula(LayoutComprovanteMatriculaEnum layoutPadraoComprovanteMatricula) {
        this.layoutPadraoComprovanteMatricula = layoutPadraoComprovanteMatricula;
    }

    
    public Boolean getEnviarMensagemNotificacaoFrequenciaAula() {
        if(enviarMensagemNotificacaoFrequenciaAula == null){
            enviarMensagemNotificacaoFrequenciaAula = false;
        }
        return enviarMensagemNotificacaoFrequenciaAula;
    }

    
    public void setEnviarMensagemNotificacaoFrequenciaAula(Boolean enviarMensagemNotificacaoFrequenciaAula) {
        this.enviarMensagemNotificacaoFrequenciaAula = enviarMensagemNotificacaoFrequenciaAula;
    }

    
    public Integer getNumeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula() {
        if(numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula == null){
            numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula = 10;
        }
        return numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula;
    }

    
    public void setNumeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula(Integer numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula) {
        this.numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula = numeroMinimoAulaConsiderarParaNotificacaoFrequenciaAula;
    }

    
    public Integer getPorcentagemConsiderarFrequenciaAulaBaixa() {
        if(porcentagemConsiderarFrequenciaAulaBaixa == null){
            porcentagemConsiderarFrequenciaAulaBaixa = 25;
        }
        return porcentagemConsiderarFrequenciaAulaBaixa;
    }

    
    public void setPorcentagemConsiderarFrequenciaAulaBaixa(Integer porcentagemConsiderarFrequenciaAulaBaixa) {
        this.porcentagemConsiderarFrequenciaAulaBaixa = porcentagemConsiderarFrequenciaAulaBaixa;
    }

    
    public Integer getPeriodicidadeNotificacaoFrequenciaAula() {
        if(periodicidadeNotificacaoFrequenciaAula == null){
            periodicidadeNotificacaoFrequenciaAula = 30;
        }
        return periodicidadeNotificacaoFrequenciaAula;
    }

    
    public void setPeriodicidadeNotificacaoFrequenciaAula(Integer periodicidadeNotificacaoFrequenciaAula) {
        this.periodicidadeNotificacaoFrequenciaAula = periodicidadeNotificacaoFrequenciaAula;
    }

    
    public Boolean getControlarNotificacaoPendenciaDocumentos() {
        if(controlarNotificacaoPendenciaDocumentos == null){
            controlarNotificacaoPendenciaDocumentos = false;
        }
        return controlarNotificacaoPendenciaDocumentos;
    }

    
    public void setControlarNotificacaoPendenciaDocumentos(Boolean controlarNotificacaoPendenciaDocumentos) {
        this.controlarNotificacaoPendenciaDocumentos = controlarNotificacaoPendenciaDocumentos;
    }

    
    public Integer getNrDiaAposInicioAulaNotificarPendenciaDocumento() {
        if(nrDiaAposInicioAulaNotificarPendenciaDocumento == null){
            nrDiaAposInicioAulaNotificarPendenciaDocumento = 0;
        }
        return nrDiaAposInicioAulaNotificarPendenciaDocumento;
    }

    
    public void setNrDiaAposInicioAulaNotificarPendenciaDocumento(Integer nrDiaAposInicioAulaNotificarPendenciaDocumento) {
        this.nrDiaAposInicioAulaNotificarPendenciaDocumento = nrDiaAposInicioAulaNotificarPendenciaDocumento;
    }

    
    public Integer getPeriodicidadeNotificarPendenciaDocumento() {
        if(periodicidadeNotificarPendenciaDocumento == null){
            periodicidadeNotificarPendenciaDocumento = 0;
        }
        return periodicidadeNotificarPendenciaDocumento;
    }

    
    public void setPeriodicidadeNotificarPendenciaDocumento(Integer periodicidadeNotificarPendenciaDocumento) {
        this.periodicidadeNotificarPendenciaDocumento = periodicidadeNotificarPendenciaDocumento;
    }

	public Integer getPrimeiraNotificacaoNaoLancamentoRegistroAula() {
		if(primeiraNotificacaoNaoLancamentoRegistroAula == null){
			primeiraNotificacaoNaoLancamentoRegistroAula = 0;
		}
		return primeiraNotificacaoNaoLancamentoRegistroAula;
	}

	public void setPrimeiraNotificacaoNaoLancamentoRegistroAula(Integer primeiraNotificacaoNaoLancamentoRegistroAula) {
		this.primeiraNotificacaoNaoLancamentoRegistroAula = primeiraNotificacaoNaoLancamentoRegistroAula;
	}

	public Integer getSegundaNotificacaoNaoLancamentoRegistroAula() {
		if(segundaNotificacaoNaoLancamentoRegistroAula == null){
			segundaNotificacaoNaoLancamentoRegistroAula = 0;
		}
		return segundaNotificacaoNaoLancamentoRegistroAula;
	}

	public void setSegundaNotificacaoNaoLancamentoRegistroAula(Integer segundaNotificacaoNaoLancamentoRegistroAula) {
		this.segundaNotificacaoNaoLancamentoRegistroAula = segundaNotificacaoNaoLancamentoRegistroAula;
	}
	

	public Integer getTerceiraNotificacaoNaoLancamentoRegistroAula() {
		terceiraNotificacaoNaoLancamentoRegistroAula = Optional.ofNullable(terceiraNotificacaoNaoLancamentoRegistroAula).orElse(0);
		return terceiraNotificacaoNaoLancamentoRegistroAula;
	}

	public void setTerceiraNotificacaoNaoLancamentoRegistroAula(Integer terceiraNotificacaoNaoLancamentoRegistroAula) {
		this.terceiraNotificacaoNaoLancamentoRegistroAula = terceiraNotificacaoNaoLancamentoRegistroAula;
	}

	public Integer getQuartaNotificacaoNaoLancamentoRegistroAula() {
		quartaNotificacaoNaoLancamentoRegistroAula = Optional.ofNullable(quartaNotificacaoNaoLancamentoRegistroAula).orElse(0);
		return quartaNotificacaoNaoLancamentoRegistroAula;
	}

	public void setQuartaNotificacaoNaoLancamentoRegistroAula(Integer quartaNotificacaoNaoLancamentoRegistroAula) {
		this.quartaNotificacaoNaoLancamentoRegistroAula = quartaNotificacaoNaoLancamentoRegistroAula;
	}

	public Integer getNrDiasQuartoAvisoRiscoSuspensao() {
		if (nrDiasQuartoAvisoRiscoSuspensao == null) {
			nrDiasQuartoAvisoRiscoSuspensao = 0;
		}
		return nrDiasQuartoAvisoRiscoSuspensao;
	}

	public void setNrDiasQuartoAvisoRiscoSuspensao(Integer nrDiasQuartoAvisoRiscoSuspensao) {
		this.nrDiasQuartoAvisoRiscoSuspensao = nrDiasQuartoAvisoRiscoSuspensao;
	}

	public Integer getPeriodicidadeQuartoAvisoRiscoSuspensao() {
		if (periodicidadeQuartoAvisoRiscoSuspensao == null) {
			periodicidadeQuartoAvisoRiscoSuspensao = 0;
		}
		return periodicidadeQuartoAvisoRiscoSuspensao;
	}

	public void setPeriodicidadeQuartoAvisoRiscoSuspensao(Integer periodicidadeQuartoAvisoRiscoSuspensao) {
		this.periodicidadeQuartoAvisoRiscoSuspensao = periodicidadeQuartoAvisoRiscoSuspensao;
	}
    
    
}
