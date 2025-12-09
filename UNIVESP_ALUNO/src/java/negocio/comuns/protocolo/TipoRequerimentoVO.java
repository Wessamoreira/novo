package negocio.comuns.protocolo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.academico.PendenciaTipoDocumentoTipoRequerimentoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.enumeradores.TipoAlunoEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.protocolo.enumeradores.TipoControleCobrancaViaRequerimentoEnum;
import negocio.comuns.secretaria.enumeradores.TipoUploadArquivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;

/**
 * Reponsável por manter os dados da entidade TipoRequerimento. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
/**
 * @author otimize
 *
 */
@XmlRootElement(name = "tipoRequerimento")
public class TipoRequerimentoVO extends SuperVO {

    private Integer codigo;
    private String nome;    
    private Integer prazoExecucao;
    private String orientacao;
    private Boolean haDocumentoParaRetirada;
    
    private Integer qtdDiasVencimentoRequerimento;
    private Integer diasParaExclusaoRequerimentoDefazados;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Departamento </code>.
     */
    private DepartamentoVO departamentoResponsavel;
    private Boolean tramitaEntreDepartamentos;
    private Boolean requerimentoVisaoAluno;
    private Boolean requerimentoSituacaoFinanceiroVisaoAluno;
    private Boolean requerimentoVisaoPai;
    private Boolean requerimentoVisaoProfessor;
    private Boolean requerimentoVisaoCoordenador;
    private Boolean requerimentoVisaoFuncionario;    
    private Boolean permitirUploadArquivo;
    private Boolean uploadArquivoObrigatorio;
    private TipoUploadArquivoEnum tipoUploadArquivo;
    private String extensaoArquivo;
    private String orientacaoUploadArquivo;
    private Boolean permitirInformarEnderecoEntrega;
    private Boolean unidadeEnsinoEspecifica;
    private List<TipoRequerimentoUnidadeEnsinoVO> unidadeEnsinoEspecificaVOs;
    private List<TipoRequerimentoDepartamentoVO> tipoRequerimentoDepartamentoVOs;
    private String tipo;
    private String situacao;
    private String mensagemAlerta;
    private TextoPadraoDeclaracaoVO textoPadrao;
    private TextoPadraoVO textoPadraoVO;
    private QuestionarioVO questionario;
    private String mensagemDesabilitarTipoRequerimento;
    private Boolean desabilitarTipoRequerimento;
 
    
    private String sigla;
    
    private Boolean requerimentoMinhasNotas;
    
    private Boolean situacaoMatriculaAtiva;
    private Boolean situacaoMatriculaPreMatriculada;
    private Boolean situacaoMatriculaCancelada;
    private Boolean situacaoMatriculaTrancada;
    private Boolean situacaoMatriculaAbandonada;
    private Boolean situacaoMatriculaTransferida;
    private Boolean situacaoMatriculaFormada;
    private Boolean situacaoMatriculaJubilado;
    
    private Boolean verificarPendenciaApenasMatriculaRequerimento;
    private Boolean verificarPendenciaBiblioteca;
    private Boolean verificarPendenciaBibliotecaAtraso;
    private Boolean verificarPendenciaFinanceira;
    private Boolean verificarPendenciaFinanceiraAtraso;
    private Boolean verificarPendenciaDocumentacao;
    private Boolean verificarPendenciaEnade;
    private Boolean verificarPendenciaEstagio;
    private Boolean verificarPendenciaAtividadeComplementar;
 
    private Integer cobrarApartirVia;    
    private Boolean requerimentoVisaoAlunoApresImprimirDeclaracao;
    private TipoControleCobrancaViaRequerimentoEnum tipoControleCobrancaViaRequerimento;
    private Boolean deferirAutomaticamente;
    private Boolean validarDebitoFinanceiroRequerimentoIsento;
    // APENAS INFORMADO QUANDO O REQUERIMENTO PODE SER IMPRESSO NA VISAO DO ALUNO
    private Integer qtdeDiasDisponivel;
    private Integer qtdeDiasAposPrimeiraImpressao;
    private List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs;
    private Boolean considerarSegundaViaIndependenteSituacaoPrimeiraVia;
    private Boolean requerimentoMembroComunidade;
    private Boolean assinarDigitalmenteDeclaracoesGeradasNoRequerimento;
    
    private Boolean considerarDiasUteis;
    private Boolean requerAutorizacaoPagamento;
    private Boolean permiteDeferirAguardandoAutorizacaoPagamento;
    private Boolean realizarIsencaoTaxaReposicaoMatriculaAposDataAula;
    private Integer quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao;
    private Boolean permitirSolicitacaoIsencaoTaxaRequerimento;    
    private Boolean solicitarAnexoComprovanteIsencaoTaxaRequerimento;
    private String orientacaoDocumentoComprovanteIsencaoTaxaRequerimento; 
    private Boolean permiteIncluirDisciplinaPorEquivalencia;    
    private Boolean permiteIncluirReposicaoTurmaOutraUnidade;    
    private Boolean permiteIncluirReposicaoTurmaOutroCurso;   
    private Boolean permitirReposicaoComChoqueHorario;
    private Boolean usarCentroResultadoTurma;
    private Boolean filtrarTipoReq;
    private Boolean bloquearQuantidadeRequerimentoAbertosSimultaneamente;
    private Integer quantidadeLimiteRequerimentoAbertoSimultaneamente;
    private Boolean considerarBloqueioSimultaneoRequerimentoDeferido;
    private Boolean considerarBloqueioSimultaneoRequerimentoIndeferido;
    private Boolean permitirImpressaoHistoricoVisaoAluno;
    private String nivelEducacional;
    private String layoutHistoricoApresentar;
    private Boolean aprovadoSituacaoHistorico;
    private Boolean reprovadoSituacaoHistorico;
    private Boolean trancadoSituacaoHistorico;
    private Boolean cursandoSituacaoHistorico;
    private Boolean abandonoCursoSituacaoHistorico;
    private Boolean transferidoSituacaoHistorico;
    private Boolean canceladoSituacaoHistorico;
    private Boolean assinarDigitalmenteHistorico;

    private Boolean permitirAlterarDataPrevisaoConclusaoRequerimento;
    private Boolean abrirOutroRequerimentoAoDeferirEsteTipoRequerimento;
    private TipoRequerimentoVO tipoRequerimentoAbrirDeferimento;
    private Integer qtdDiasCobrarTaxa;
    private Boolean validarMatriculaIntegralizada;

    private Boolean permitirReporDisciplinaComAulaProgramada;

    private Boolean ocultarUnidadeEnsinoListaTurmaReposicao;
    
    private Boolean validarEntregaTccAluno;
    private Boolean deferirAutomaticamenteDocumentoImpresso;
    private Double percentualMinimoCargaHorariaAproveitamento;
	private Integer qtdeMinimaDeAnosAproveitamento;
	private Integer qtdeMaximaIndeferidoAproveitamento;
	private String msgBloqueioNovaSolicitacaoAproveitamento;
	private List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs;
	private CidTipoRequerimentoVO  cidTipoRequerimentoVO;
    private Boolean permitirAproveitarDisciplinaCursando;
	private Boolean campoAfastamento;

    public static final long serialVersionUID = 1L;
    
    /**
     *  Transiente
     */
    private Double valor;
    
    private String orientacaoAtendente;
    private TextoPadraoVO certificadoImpresso;    
    private Boolean apenasParaAlunosComTodasAulasRegistradas;
    private Boolean verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada;
    private Boolean registrarFormaturaAoRealizarImpressaoCerticadoDigital;
    
    private List<PendenciaTipoDocumentoTipoRequerimentoVO> pendenciaTipoDocumentoTipoRequerimentoVOs;
    private Boolean cobrarTaxaSomenteCertificadoImpresso;
    private String mensagemEmissaoCertificadoImpresso;
    private Boolean deferirAutomaticamenteTrancamento;
    private String bloqueioSimultaneoPelo;
    private Boolean permitirAlunoAlterarUnidadeEnsino;
    private Boolean permitirAlunoAlterarCurso;
    private Boolean validarVagasPorNumeroComputadoresUnidadeEnsino;
    private Boolean validarVagasPorNumeroComputadoresConsiderandoCurso;
    private Boolean registrarTransferenciaProximoSemestre;
    private Boolean permitirAlunoRejeitarDocumento;
    private Integer percentualIntegralizacaoCurricularInicial;
    private Integer percentualIntegralizacaoCurricularFinal;
    private Boolean registrarAproveitamentoDisciplinaTCC;
    private Boolean registrarTrancamentoProximoSemestre;
    private Boolean validarAnoSemestreIngresso;
    private String anoIngresso;
    private String semestreIngresso;
    private String ano;
    private String semestre;
    private Boolean considerarTodasMatriculasAlunoValidacaoAberturaSimultanea;
    private String cidDeferirAutomaticamente;

	private String bimestre;
	private String tipoNota;
	
	private Boolean enviarNotificacaoRequerente;
	private Boolean utilizarMensagemDeferimentoExclusivo;
	private Boolean utilizarMensagemIndeferimentoExclusivo;
	private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaDeferimento;
	private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaIndeferimento;
	
	private TipoAlunoEnum tipoAluno;
    
    /**
     * Construtor padrão da classe <code>TipoRequerimento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TipoRequerimentoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TipoRequerimentoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TipoRequerimentoVO obj) throws Exception {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Tipo Requerimento) deve ser informado.");
        }
        if (obj.getTramitaEntreDepartamentos()) {
            if (obj.getTipoRequerimentoDepartamentoVOs().isEmpty()) {
                throw new ConsistirException("Este Tipo de Requerimento foi definido para tramitar entre departamentos. Ao menos um departamento deve ser informado para Trâmite.");
            }
        } else {
            if (obj.getPrazoExecucao().intValue() == 0) {
                throw new ConsistirException("O campo PRAZO EXECUÇÃO (Tipo Requerimento) deve ser informado.");
            }
            if ((obj.getDepartamentoResponsavel() == null)
                || (obj.getDepartamentoResponsavel().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo DEPARTAMENTO RESPONSÁVEL (Tipo Requerimento) deve ser informado.");
            }
        }
        if (obj.getTipo().equalsIgnoreCase("")) {
            throw new ConsistirException("O campo TIPO (Tipo Requerimento) deve ser informado.");
        }
        if(obj.getPermitirSolicitacaoIsencaoTaxaRequerimento() && !Uteis.isAtributoPreenchido(obj.getOrientacaoDocumentoComprovanteIsencaoTaxaRequerimento())) {
        	 throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoRequerimento_orientacaoSolicitacaoIsencao"));
        }
        if(obj.getPermitirUploadArquivo()) {
        	if(obj.getTipoUploadArquivo() == null) {
        		throw new ConsistirException("O campo TIPO ARQUIVO UPLOAD (Tipo Requerimento) deve ser informado.");
        	}
        	if(obj.getExtensaoArquivo().trim().isEmpty()) {
        		throw new ConsistirException("O campo EXTENSÃO ARQUIVO UPLOAD (Tipo Requerimento) deve ser informado.");
        	}
        }
        if(obj.getIsTipoTransferenciaInterna()) {
        	if(obj.getAno().trim().isEmpty()) {
        		throw new ConsistirException("O campo ANO TRANSFERÊNCIA (Tipo Requerimento) deve ser informado.");
        	}
        	if(obj.getAno().trim().length() != 4) {
        		throw new ConsistirException("O campo ANO TRANSFERÊNCIA (Tipo Requerimento) deve ser informado com 4 dígitos.");
        	}
        	if(!obj.getSemestre().equals("1") && !obj.getSemestre().equals("2")) {
        		throw new ConsistirException("O campo SEMESTRE TRANSFERÊNCIA (Tipo Requerimento) deve ser informado 1º ou 2º.");
        	}
        }
        if(obj.getTipo().equals("TR")) {
        	if(obj.getAno().trim().isEmpty()) {
        		throw new ConsistirException("O campo ANO TRANCAMENTO (Tipo Requerimento) deve ser informado.");
        	}
        	if(obj.getAno().trim().length() != 4) {
        		throw new ConsistirException("O campo ANO TRANCAMENTO (Tipo Requerimento) deve ser informado com 4 dígitos.");
        	}
        	if(!obj.getSemestre().equals("1") && !obj.getSemestre().equals("2")) {
        		throw new ConsistirException("O campo SEMESTRE TRANCAMENTO (Tipo Requerimento) deve ser informado 1º ou 2º.");
        	}
        }
//        if(!Uteis.isAtributoPreenchido(obj.getTaxa())) {
//        	obj.setRequerAutorizacaoPagamento(false);
//        }
//        if(!Uteis.isAtributoPreenchido(obj.getTaxa())) {
//        	obj.setPermitirSolicitacaoIsencaoTaxaRequerimento(false);
//        }
        if(!obj.getPermitirSolicitacaoIsencaoTaxaRequerimento()) {
        	obj.setOrientacaoDocumentoComprovanteIsencaoTaxaRequerimento("");
        	obj.setSolicitarAnexoComprovanteIsencaoTaxaRequerimento(false);
        }
        if(obj.getIsTipoTransferenciaInterna() && !obj.getPermitirAlunoAlterarCurso() && !obj.getPermitirAlunoAlterarUnidadeEnsino()) {
        	throw new ConsistirException("O campo PERMITIR ALUNO ALTERAR CURSO ou PERMITIR ALUNO ALTERAR UNIDADE DE ENSINO deve ser habilitado.");
        }
        if(obj.getValidarMatriculaIntegralizada()) {
        	if(obj.getPercentualIntegralizacaoCurricularInicial() > 100) {
        		throw new ConsistirException("O campo FAIXA PERCENTUAL INTEGRALIZAÇÃO CURRICULAR INICIAL (Pendências) não pode ser maior que 100%.");
        	}
        	if(obj.getPercentualIntegralizacaoCurricularFinal() > 100) {
        		throw new ConsistirException("O campo FAIXA PERCENTUAL INTEGRALIZAÇÃO CURRICULAR FINAL (Pendências) não pode ser maior que 100%.");
        	}
        	if(obj.getPercentualIntegralizacaoCurricularFinal() == 0) {
        		throw new ConsistirException("O campo FAIXA PERCENTUAL INTEGRALIZAÇÃO CURRICULAR FINAL (Pendências) não pode ser igual a 0.");
        	}
        	if(obj.getPercentualIntegralizacaoCurricularInicial()  > obj.getPercentualIntegralizacaoCurricularFinal() ) {
        		throw new ConsistirException("O campo FAIXA PERCENTUAL INTEGRALIZAÇÃO CURRICULAR INICIAL (Pendências) não pode ser maior o PERCENTUAL FINAL.");
        	}
        }
        if (obj.getValidarAnoSemestreIngresso()){
        	if(!Uteis.isAtributoPreenchido(obj.getAnoIngresso())) {
        		throw new ConsistirException("O campo Ano Ingresso deve ser informado.");
        	}
        	if (!Uteis.isAtributoPreenchido(obj.getSemestreIngresso())) {
        		throw new ConsistirException("O campo Semestre Ingresso deve ser informado.");
        	}
        	if (Integer.parseInt( obj.getSemestreIngresso()) > 2) {
        		throw new ConsistirException("O valor informado em Semestre Ingresso não é válido");
        	}
        }
        if (obj.getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
        	obj.setRequerimentoVisaoFuncionario(Boolean.FALSE);
        	obj.setRequerimentoVisaoProfessor(Boolean.FALSE);
        	obj.setRequerimentoVisaoCoordenador(Boolean.FALSE);
        	obj.setRequerimentoMembroComunidade(Boolean.FALSE);
        }
        if (obj.getUtilizarMensagemDeferimentoExclusivo()) {
        	obj.getPersonalizacaoMensagemAutomaticaDeferimento().validarDados();
        }
        if (obj.getUtilizarMensagemIndeferimentoExclusivo()) {
        	obj.getPersonalizacaoMensagemAutomaticaIndeferimento().validarDados();
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setValor(0.0);
        setPrazoExecucao(0);
        setOrientacao("");
        setHaDocumentoParaRetirada(Boolean.FALSE);        
        setTipo("OU");
    }
    

    /**
     * Retorna o objeto da classe <code>Departamento</code> relacionado com (
     * <code>TipoRequerimento</code>).
     */
    public DepartamentoVO getDepartamentoResponsavel() {
        if (departamentoResponsavel == null) {
            departamentoResponsavel = new DepartamentoVO();
        }
        return (departamentoResponsavel);
    }

    /**
     * Define o objeto da classe <code>Departamento</code> relacionado com (
     * <code>TipoRequerimento</code>).
     */
    public void setDepartamentoResponsavel(DepartamentoVO obj) {
        this.departamentoResponsavel = obj;
    }

    @XmlElement(name = "prazoExecucao")
    public Integer getPrazoExecucao() {
        return (prazoExecucao);
    }

    public void setPrazoExecucao(Integer prazoExecucao) {
        this.prazoExecucao = prazoExecucao;
    }

    @XmlElement(name = "valor")
    public Double getValor() {
        return (valor);
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @XmlElement(name = "nome")
    public String getNome() {
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "orientacao")
    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }

    public Boolean isHaDocumentoParaRetirada() {
        return haDocumentoParaRetirada;
    }

    @XmlElement(name = "haDocumentoParaRetirada")
    public Boolean getHaDocumentoParaRetirada() {
        return haDocumentoParaRetirada;
    }

    public void setHaDocumentoParaRetirada(Boolean haDocumentoParaRetirada) {
        this.haDocumentoParaRetirada = haDocumentoParaRetirada;
    }

    /**
     * @return the status
     */
    @XmlElement(name = "tipo")
    public String getTipo() {
        return tipo;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getRequerimentoVisaoAluno() {
        if (requerimentoVisaoAluno == null) {
            requerimentoVisaoAluno = Boolean.FALSE;
        }
        return requerimentoVisaoAluno;
    }

    public void setRequerimentoVisaoAluno(Boolean requerimentoVisaoAluno) {
        this.requerimentoVisaoAluno = requerimentoVisaoAluno;
    }

    public Boolean getRequerimentoSituacaoFinanceiroVisaoAluno() {
        if (requerimentoSituacaoFinanceiroVisaoAluno == null) {
            requerimentoSituacaoFinanceiroVisaoAluno = Boolean.TRUE;
        }
        return requerimentoSituacaoFinanceiroVisaoAluno;
    }

    public void setRequerimentoSituacaoFinanceiroVisaoAluno(Boolean requerimentoSituacaoFinanceiroVisaoAluno) {
        this.requerimentoSituacaoFinanceiroVisaoAluno = requerimentoSituacaoFinanceiroVisaoAluno;
    }

    public Boolean getRequerimentoVisaoProfessor() {
        if (requerimentoVisaoProfessor == null) {
            requerimentoVisaoProfessor = Boolean.FALSE;
        }
        return requerimentoVisaoProfessor;
    }

    public void setRequerimentoVisaoProfessor(Boolean requerimentoVisaoProfessor) {
        this.requerimentoVisaoProfessor = requerimentoVisaoProfessor;
    }

    public Boolean getRequerimentoVisaoCoordenador() {
        if (requerimentoVisaoCoordenador == null) {
            requerimentoVisaoCoordenador = Boolean.FALSE;
        }
        return requerimentoVisaoCoordenador;
    }

    public void setRequerimentoVisaoCoordenador(Boolean requerimentoVisaoCoordenador) {
        this.requerimentoVisaoCoordenador = requerimentoVisaoCoordenador;
    }

    @XmlElement(name = "permitirInformarEnderecoEntrega")
    public Boolean getPermitirInformarEnderecoEntrega() {
        if (permitirInformarEnderecoEntrega == null) {
            permitirInformarEnderecoEntrega = false;
        }
        return permitirInformarEnderecoEntrega;
    }

    public void setPermitirInformarEnderecoEntrega(Boolean permitirInformarEnderecoEntrega) {
        this.permitirInformarEnderecoEntrega = permitirInformarEnderecoEntrega;
    }

    @XmlElement(name = "permitirUploadArquivo")
    public Boolean getPermitirUploadArquivo() {
        if (permitirUploadArquivo == null) {
            permitirUploadArquivo = false;
        }
        return permitirUploadArquivo;
    }

    public void setPermitirUploadArquivo(Boolean permitirUploadArquivo) {
        this.permitirUploadArquivo = permitirUploadArquivo;
    }

    public Boolean getRequerimentoVisaoPai() {
        if (requerimentoVisaoPai == null) {
            requerimentoVisaoPai = false;
        }
        return requerimentoVisaoPai;
    }

    public void setRequerimentoVisaoPai(Boolean requerimentoVisaoPai) {
        this.requerimentoVisaoPai = requerimentoVisaoPai;
    }

    /**
     * @return the qtdDiasVencimentoRequerimento
     */
    public Integer getQtdDiasVencimentoRequerimento() {
        if (qtdDiasVencimentoRequerimento == null) {
            qtdDiasVencimentoRequerimento = 0;
        }
        return qtdDiasVencimentoRequerimento;
    }

    /**
     * @param qtdDiasVencimentoRequerimento the qtdDiasVencimentoRequerimento to set
     */
    public void setQtdDiasVencimentoRequerimento(Integer qtdDiasVencimentoRequerimento) {
        this.qtdDiasVencimentoRequerimento = qtdDiasVencimentoRequerimento;
    }

    /**
     * @return the diasParaExclusaoRequerimentoDefazados
     */
    public Integer getDiasParaExclusaoRequerimentoDefazados() {
        if (diasParaExclusaoRequerimentoDefazados == null) {
            diasParaExclusaoRequerimentoDefazados = 0;
        }
        return diasParaExclusaoRequerimentoDefazados;
    }

    /**
     * @param diasParaExclusaoRequerimentoDefazados the diasParaExclusaoRequerimentoDefazados to set
     */
    public void setDiasParaExclusaoRequerimentoDefazados(Integer diasParaExclusaoRequerimentoDefazados) {
        this.diasParaExclusaoRequerimentoDefazados = diasParaExclusaoRequerimentoDefazados;
    }

    /**
     * @return the situacao
     */
    @XmlElement(name = "situacao_Apresentar")
    public String getSituacao_Apresentar() {
        if (getSituacao().equals("IN")) {
            return "INATIVO";
        }
        if (getSituacao().equals("AT")) {
            return "ATIVO";
        }
        return "ATIVO";
    }

    @XmlElement(name = "situacao")
    public String getSituacao() {
        if (situacao == null) {
            situacao = "AT";
        }
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the unidadeEnsinoEspecifica
     */
    public Boolean getUnidadeEnsinoEspecifica() {
        if (unidadeEnsinoEspecifica == null) {
            unidadeEnsinoEspecifica = Boolean.FALSE;
        }
        return unidadeEnsinoEspecifica;
    }

    /**
     * @param unidadeEnsinoEspecifica the unidadeEnsinoEspecifica to set
     */
    public void setUnidadeEnsinoEspecifica(Boolean unidadeEnsinoEspecifica) {
        this.unidadeEnsinoEspecifica = unidadeEnsinoEspecifica;
    }

    public void preencherListaUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino) {
        this.setUnidadeEnsinoEspecificaVOs(null);
        Iterator<UnidadeEnsinoVO> i = listaUnidadeEnsino.iterator();
        while (i.hasNext()) {
            UnidadeEnsinoVO uni = (UnidadeEnsinoVO)i.next();
            if (uni.getEscolhidaParaFazerCotacao()) {
                TipoRequerimentoUnidadeEnsinoVO tipo = new TipoRequerimentoUnidadeEnsinoVO();
                tipo.setUnidadeEnsino(uni);
                this.getUnidadeEnsinoEspecificaVOs().add(tipo);
            }
        }

    }

    public void obterListaUnidadeEnsinoPreenchida(List<UnidadeEnsinoVO> listaUnidadeEnsino) {
        Iterator<UnidadeEnsinoVO> i = listaUnidadeEnsino.iterator();
        while (i.hasNext()) {
            UnidadeEnsinoVO uni = (UnidadeEnsinoVO)i.next();
            Iterator<TipoRequerimentoUnidadeEnsinoVO> j = this.getUnidadeEnsinoEspecificaVOs().iterator();
            while (j.hasNext()) {
                TipoRequerimentoUnidadeEnsinoVO tipo = (TipoRequerimentoUnidadeEnsinoVO)j.next();
                if (tipo.getUnidadeEnsino().getCodigo().intValue() == uni.getCodigo().intValue()) {
                    uni.setEscolhidaParaFazerCotacao(true);
                }
            }
        }
    }

    /**
     * @return the unidadeEnsinoEspecificaVOs
     */
    public List<TipoRequerimentoUnidadeEnsinoVO> getUnidadeEnsinoEspecificaVOs() {
        if (unidadeEnsinoEspecificaVOs == null) {
            unidadeEnsinoEspecificaVOs = new ArrayList<TipoRequerimentoUnidadeEnsinoVO>();
        }
        return unidadeEnsinoEspecificaVOs;
    }

    /**
     * @param unidadeEnsinoEspecificaVOs the unidadeEnsinoEspecificaVOs to set
     */
    public void setUnidadeEnsinoEspecificaVOs(List<TipoRequerimentoUnidadeEnsinoVO> unidadeEnsinoEspecificaVOs) {
        this.unidadeEnsinoEspecificaVOs = unidadeEnsinoEspecificaVOs;
    }

    @XmlElement(name = "mensagemAlerta")
	public String getMensagemAlerta() {
		if(mensagemAlerta == null){
			mensagemAlerta = "";
		}
		return mensagemAlerta;
	}

	public void setMensagemAlerta(String mensagemAlerta) {
		this.mensagemAlerta = mensagemAlerta;
	}

    /**
     * @return the tramitaEntreDepartamentos
     */
    public Boolean getTramitaEntreDepartamentos() {
        if (tramitaEntreDepartamentos == null) {
            tramitaEntreDepartamentos = Boolean.TRUE;
        }
        return tramitaEntreDepartamentos;
    }

    /**
     * @param tramitaEntreDepartamentos the tramitaEntreDepartamentos to set
     */
    public void setTramitaEntreDepartamentos(Boolean tramitaEntreDepartamentos) {
        this.tramitaEntreDepartamentos = tramitaEntreDepartamentos;
    }

    /**
     * @return the tipoRequerimentoDepartamentoVOs
     */
    @XmlElement(name = "tipoRequerimentoDepartamentoVOs")
    public List<TipoRequerimentoDepartamentoVO> getTipoRequerimentoDepartamentoVOs() {
        if (tipoRequerimentoDepartamentoVOs == null) {
            tipoRequerimentoDepartamentoVOs = new ArrayList<TipoRequerimentoDepartamentoVO>();
        }
        return tipoRequerimentoDepartamentoVOs;
    }

    /**
     * @param tipoRequerimentoDepartamentoVOs the tipoRequerimentoDepartamentoVOs to set
     */
    public void setTipoRequerimentoDepartamentoVOs(List<TipoRequerimentoDepartamentoVO> tipoRequerimentoDepartamentoVOs) {
        this.tipoRequerimentoDepartamentoVOs = tipoRequerimentoDepartamentoVOs;
    }
    
    public void subirNaOrdemExecucaoTipoRequerimentoDepartamentoVO(TipoRequerimentoDepartamentoVO obj) throws Exception {
        if (obj.getOrdemExecucao().equals(1)) {
            throw new Exception("Registro já está no topo da lista!");
        }
        int posicaoAcima = obj.getOrdemExecucao() - 1;
        for (TipoRequerimentoDepartamentoVO objAcimaParaInversao : this.getTipoRequerimentoDepartamentoVOs()) {
            if (objAcimaParaInversao.getOrdemExecucao().equals(posicaoAcima)) {
                objAcimaParaInversao.setOrdemExecucao(obj.getOrdemExecucao());
                obj.setOrdemExecucao(posicaoAcima);
            }
        }
        Ordenacao.ordenarLista(this.getTipoRequerimentoDepartamentoVOs(), "ordemExecucao");        
    }
    
    public void descerNaOrdemExecucaoTipoRequerimentoDepartamentoVO(TipoRequerimentoDepartamentoVO obj) throws Exception {
        int numeroRegistros = this.getTipoRequerimentoDepartamentoVOs().size();
        if (obj.getOrdemExecucao().equals(numeroRegistros)) {
            throw new Exception("Registro já está no fim da lista!");
        }
        int posicaoDescer = obj.getOrdemExecucao() + 1;
        for (TipoRequerimentoDepartamentoVO objAbaixoParaInversao : this.getTipoRequerimentoDepartamentoVOs()) {
            if (objAbaixoParaInversao.getOrdemExecucao().equals(posicaoDescer)) {
                objAbaixoParaInversao.setOrdemExecucao(obj.getOrdemExecucao());
                obj.setOrdemExecucao(posicaoDescer);
            }
        }
        Ordenacao.ordenarLista(this.getTipoRequerimentoDepartamentoVOs(), "ordemExecucao");        
    }    
    
    public void totalizarNrDiasExecucaoComBaseTramiteDepartamentos() throws Exception {
        if (!this.getTramitaEntreDepartamentos()) {
            return;
        }
        int total = 0;
        for (TipoRequerimentoDepartamentoVO obj : this.getTipoRequerimentoDepartamentoVOs()) {
            total = total + obj.getPrazoExecucao();
        }
        this.setPrazoExecucao(total);
    }

    public TipoRequerimentoDepartamentoVO consultarTipoRequerimentoDepartamentoVOs(Integer ordemTramite) throws Exception {
        for (Iterator<TipoRequerimentoDepartamentoVO> iterator = getTipoRequerimentoDepartamentoVOs().iterator(); iterator.hasNext();) {
            TipoRequerimentoDepartamentoVO objExistente = (TipoRequerimentoDepartamentoVO) iterator.next();
            if (objExistente.getOrdemExecucao().equals(ordemTramite)) {
                return objExistente;
            }
        }
        return null;
    }    

    public void adicionarTipoRequerimentoDepartamentoVOs(TipoRequerimentoDepartamentoVO obj) throws Exception {
        TipoRequerimentoDepartamentoVO.validarDados(obj);
        int index = 0;
        
        Iterator<TipoRequerimentoDepartamentoVO> i = getTipoRequerimentoDepartamentoVOs().iterator();
        
        while (i.hasNext()) {
            TipoRequerimentoDepartamentoVO objExistente = (TipoRequerimentoDepartamentoVO) i.next();
            if (objExistente.getOrdemExecucao().equals(obj.getOrdemExecucao())) {
                getTipoRequerimentoDepartamentoVOs().set(index, obj);
                totalizarNrDiasExecucaoComBaseTramiteDepartamentos();
                return;
            }
            index++;
        }
        obj.setOrdemExecucao(getTipoRequerimentoDepartamentoVOs().size()+1);
        getTipoRequerimentoDepartamentoVOs().add(obj);
        totalizarNrDiasExecucaoComBaseTramiteDepartamentos();
    }

    public void excluirTipoRequerimentoDepartamentoVOs(Integer posicaoOrdemRemover) throws Exception {
        int index = 0;
        Iterator<TipoRequerimentoDepartamentoVO> i = getTipoRequerimentoDepartamentoVOs().iterator();
        while (i.hasNext()) {
            TipoRequerimentoDepartamentoVO objExistente = (TipoRequerimentoDepartamentoVO) i.next();
            if (objExistente.getOrdemExecucao().equals(posicaoOrdemRemover)) {
                getTipoRequerimentoDepartamentoVOs().remove(index);
                int x = 1;
                for(TipoRequerimentoDepartamentoVO obj: getTipoRequerimentoDepartamentoVOs()){
                	obj.setOrdemExecucao(x);
                	x++;
                }
                totalizarNrDiasExecucaoComBaseTramiteDepartamentos();
                return;
            }
            index++;
        }
    }

    @XmlElement(name = "textoPadrao")
	public TextoPadraoDeclaracaoVO getTextoPadrao() {
		if(textoPadrao == null){
			textoPadrao = new TextoPadraoDeclaracaoVO();
		}
		return textoPadrao;
	}

	public void setTextoPadrao(TextoPadraoDeclaracaoVO textoPadrao) {
		this.textoPadrao = textoPadrao;
	}
	
	@XmlElement(name = "textoPadraoVO")
	public TextoPadraoVO getTextoPadraoVO() {
		if (textoPadraoVO == null) {
			textoPadraoVO = new TextoPadraoVO();
		}
		return textoPadraoVO;
	}
	
	public void setTextoPadraoVO(TextoPadraoVO textoPadraoVO) {
		this.textoPadraoVO = textoPadraoVO;
	}

	@XmlElement(name = "questionario")
	public QuestionarioVO getQuestionario() {
		if(questionario == null){
			questionario = new QuestionarioVO();
		}
		return questionario;
	}

	public void setQuestionario(QuestionarioVO questionario) {
		this.questionario = questionario;
	}    
    
	public Boolean getIsUtilizaTextoPadrao(){
		return (getTextoPadrao().getCodigo() != null && getTextoPadrao().getCodigo() !=0) ;
	}
	
	public Boolean getIsTipoAproveitamentoDisciplina(){
		return getTipo().equals("AD");
	}
	
	public Boolean getIsTipoHistorico(){
		return getTipo().equals("HI");
	}
	
	public Boolean getIsTipoReposicao(){
		return getTipo().equals("RE");
	}
	
	public Boolean getIsTipoInclusaoDisciplina(){
		return getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.getValor());
	}
	
	public Boolean getIsTipoSegundaChamada(){
		return getTipo().equals("SEGUNDA_CHAMADA");
	}
	
	public Boolean getIsAtividadeDiscursiva(){
		return getTipo().equals("ATIVIDADE_DISCURSIVA");
	}

	public Boolean getIsTipoTransfExternaPortDiplo(){
		return getTipo().equals("TE") || getTipo().equals("PO");
	}	

	public Boolean getIsTipoTransferenciaInterna(){
		return getTipo().equals("TI");
	}
	
	public Boolean getIsTipoCarteirinha(){
		return getTipo().equals("CE");
	}
	
	public Boolean getIsHabilitaImpressaoDeclaracao(){
		return getTipo().equals("DE") && getTextoPadrao().getCodigo()>0;
	}
	
	public Boolean getIsUtilizaQuestionario(){
		return getQuestionario().getCodigo() > 0;
	}

	public String getMensagemDesabilitarTipoRequerimento() {
		if (mensagemDesabilitarTipoRequerimento == null) {
			mensagemDesabilitarTipoRequerimento = "";
		}
		return mensagemDesabilitarTipoRequerimento;
	}

	public void setMensagemDesabilitarTipoRequerimento(String mensagemDesabilitarTipoRequerimento) {
		this.mensagemDesabilitarTipoRequerimento = mensagemDesabilitarTipoRequerimento;
	}

	public Boolean getDesabilitarTipoRequerimento() {
		if (desabilitarTipoRequerimento == null) {
			desabilitarTipoRequerimento = Boolean.FALSE;
		}
		return desabilitarTipoRequerimento;
	}

	public void setDesabilitarTipoRequerimento(Boolean desabilitarTipoRequerimento) {
		this.desabilitarTipoRequerimento = desabilitarTipoRequerimento;
	}

//	public CentroReceitaVO getCentroReceitaRequerimentoPadrao() {
//		if (centroReceitaRequerimentoPadrao == null) {
//			centroReceitaRequerimentoPadrao = new CentroReceitaVO();
//		}
//		return centroReceitaRequerimentoPadrao;
//	}
//
//	public void setCentroReceitaRequerimentoPadrao(CentroReceitaVO centroReceitaRequerimentoPadrao) {
//		this.centroReceitaRequerimentoPadrao = centroReceitaRequerimentoPadrao;
//	}

	public Boolean getRequerimentoMinhasNotas() {
		if(requerimentoMinhasNotas == null){
			requerimentoMinhasNotas = Boolean.FALSE;
		}
		return requerimentoMinhasNotas;
	}

	public void setRequerimentoMinhasNotas(Boolean requerimentoMinhasNotas) {
		this.requerimentoMinhasNotas = requerimentoMinhasNotas;
	}

	@XmlElement(name = "sigla")
	public String getSigla() {
		if(sigla == null){
			sigla = "";
		}
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Boolean getSituacaoMatriculaAtiva() {
		if (situacaoMatriculaAtiva == null) {
			situacaoMatriculaAtiva = true;
		}
		return situacaoMatriculaAtiva;
	}

	public void setSituacaoMatriculaAtiva(Boolean situacaoMatriculaAtiva) {
		this.situacaoMatriculaAtiva = situacaoMatriculaAtiva;
	}

	public Boolean getSituacaoMatriculaPreMatriculada() {
		if (situacaoMatriculaPreMatriculada == null) {
			situacaoMatriculaPreMatriculada = true;
		}
		return situacaoMatriculaPreMatriculada;
	}

	public void setSituacaoMatriculaPreMatriculada(Boolean situacaoMatriculaPreMatriculada) {
		this.situacaoMatriculaPreMatriculada = situacaoMatriculaPreMatriculada;
	}

	public Boolean getSituacaoMatriculaCancelada() {
		if (situacaoMatriculaCancelada == null) {
			situacaoMatriculaCancelada = true;
		}
		return situacaoMatriculaCancelada;
	}

	public void setSituacaoMatriculaCancelada(Boolean situacaoMatriculaCancelada) {
		this.situacaoMatriculaCancelada = situacaoMatriculaCancelada;
	}

	public Boolean getSituacaoMatriculaTrancada() {
		if (situacaoMatriculaTrancada == null) {
			situacaoMatriculaTrancada = true;
		}
		return situacaoMatriculaTrancada;
	}

	public void setSituacaoMatriculaTrancada(Boolean situacaoMatriculaTrancada) {
		this.situacaoMatriculaTrancada = situacaoMatriculaTrancada;
	}

	public Boolean getSituacaoMatriculaAbandonada() {
		if (situacaoMatriculaAbandonada == null) {
			situacaoMatriculaAbandonada = true;
		}
		return situacaoMatriculaAbandonada;
	}

	public void setSituacaoMatriculaAbandonada(Boolean situacaoMatriculaAbandonada) {
		this.situacaoMatriculaAbandonada = situacaoMatriculaAbandonada;
	}

	public Boolean getSituacaoMatriculaTransferida() {
		if (situacaoMatriculaTransferida == null) {
			situacaoMatriculaTransferida = true;
		}
		return situacaoMatriculaTransferida;
	}

	public void setSituacaoMatriculaTransferida(Boolean situacaoMatriculaTransferida) {
		this.situacaoMatriculaTransferida = situacaoMatriculaTransferida;
	}

	public Boolean getSituacaoMatriculaFormada() {
		if (situacaoMatriculaFormada == null) {
			situacaoMatriculaFormada = true;
		}
		return situacaoMatriculaFormada;
	}

	public void setSituacaoMatriculaFormada(Boolean situacaoMatriculaFormada) {
		this.situacaoMatriculaFormada = situacaoMatriculaFormada;
	}
	
	public Boolean getSituacaoMatriculaJubilado() {
		if (situacaoMatriculaJubilado == null) {
			situacaoMatriculaJubilado = Boolean.TRUE;
		}
		return situacaoMatriculaJubilado;
	}
	
	public void setSituacaoMatriculaJubilado(Boolean situacaoMatriculaJubilado) {
		this.situacaoMatriculaJubilado = situacaoMatriculaJubilado;
	}

	public Boolean getVerificarPendenciaBiblioteca() {
		if (verificarPendenciaBiblioteca == null) {
			verificarPendenciaBiblioteca = false;
		}
		return verificarPendenciaBiblioteca;
	}

	public void setVerificarPendenciaBiblioteca(Boolean verificarPendenciaBiblioteca) {
		this.verificarPendenciaBiblioteca = verificarPendenciaBiblioteca;
	}

	public Boolean getVerificarPendenciaFinanceira() {
		if (verificarPendenciaFinanceira == null) {
			verificarPendenciaFinanceira = false;
		}
		return verificarPendenciaFinanceira;
	}

	public void setVerificarPendenciaFinanceira(Boolean verificarPendenciaFinanceira) {
		this.verificarPendenciaFinanceira = verificarPendenciaFinanceira;
	}

	public Boolean getVerificarPendenciaDocumentacao() {
		if (verificarPendenciaDocumentacao == null) {
			verificarPendenciaDocumentacao = false;
		}
		return verificarPendenciaDocumentacao;
	}

	public void setVerificarPendenciaDocumentacao(Boolean verificarPendenciaDocumentacao) {
		this.verificarPendenciaDocumentacao = verificarPendenciaDocumentacao;
	}

	public Boolean getVerificarPendenciaEnade() {
		if (verificarPendenciaEnade == null) {
			verificarPendenciaEnade = false;
		}
		return verificarPendenciaEnade;
	}

	public void setVerificarPendenciaEnade(Boolean verificarPendenciaEnade) {
		this.verificarPendenciaEnade = verificarPendenciaEnade;
	}

	public Boolean getVerificarPendenciaEstagio() {
		if (verificarPendenciaEstagio == null) {
			verificarPendenciaEstagio = false;
		}
		return verificarPendenciaEstagio;
	}

	public void setVerificarPendenciaEstagio(Boolean verificarPendenciaEstagio) {
		this.verificarPendenciaEstagio = verificarPendenciaEstagio;
	}

	public Boolean getVerificarPendenciaAtividadeComplementar() {
		if (verificarPendenciaAtividadeComplementar == null) {
			verificarPendenciaAtividadeComplementar = false;
		}
		return verificarPendenciaAtividadeComplementar;
	}

	public void setVerificarPendenciaAtividadeComplementar(Boolean verificarPendenciaAtividadeComplementar) {
		this.verificarPendenciaAtividadeComplementar = verificarPendenciaAtividadeComplementar;
	}

//	public TaxaVO getTaxa() {
//		if (taxa == null) {
//			taxa = new TaxaVO();
//		}
//		return taxa;
//	}
//
//	public void setTaxa(TaxaVO taxa) {
//		this.taxa = taxa;
//	}

	public Integer getCobrarApartirVia() {
		if (cobrarApartirVia == null) {
			cobrarApartirVia = 0;
		}
		return cobrarApartirVia;
	}

	public void setCobrarApartirVia(Integer cobrarApartirVia) {
		this.cobrarApartirVia = cobrarApartirVia;
	}

	public TipoControleCobrancaViaRequerimentoEnum getTipoControleCobrancaViaRequerimento() {
		if (tipoControleCobrancaViaRequerimento == null) {
			tipoControleCobrancaViaRequerimento = TipoControleCobrancaViaRequerimentoEnum.PERIODO_MATRICULA;
		}
		return tipoControleCobrancaViaRequerimento;
	}

	public void setTipoControleCobrancaViaRequerimento(TipoControleCobrancaViaRequerimentoEnum tipoControleCobrancaViaRequerimento) {
		this.tipoControleCobrancaViaRequerimento = tipoControleCobrancaViaRequerimento;
	}

	public Boolean getVerificarPendenciaBibliotecaAtraso() {
		if (verificarPendenciaBibliotecaAtraso == null) {
			verificarPendenciaBibliotecaAtraso = false;
		}
		return verificarPendenciaBibliotecaAtraso;
	}

	public void setVerificarPendenciaBibliotecaAtraso(Boolean verificarPendenciaBibliotecaAtraso) {
		this.verificarPendenciaBibliotecaAtraso = verificarPendenciaBibliotecaAtraso;
	}

	public Boolean getVerificarPendenciaFinanceiraAtraso() {
		if (verificarPendenciaFinanceiraAtraso == null) {
			verificarPendenciaFinanceiraAtraso = false;
		}
		return verificarPendenciaFinanceiraAtraso;
	}

	public void setVerificarPendenciaFinanceiraAtraso(Boolean verificarPendenciaFinanceiraAtraso) {
		this.verificarPendenciaFinanceiraAtraso = verificarPendenciaFinanceiraAtraso;
	}

	public Boolean getRequerimentoVisaoAlunoApresImprimirDeclaracao() {
		if (requerimentoVisaoAlunoApresImprimirDeclaracao == null) {
			requerimentoVisaoAlunoApresImprimirDeclaracao = Boolean.FALSE;
		}
		return requerimentoVisaoAlunoApresImprimirDeclaracao;
	}

	public void setRequerimentoVisaoAlunoApresImprimirDeclaracao(Boolean requerimentoVisaoAlunoApresImprimirDeclaracao) {
		this.requerimentoVisaoAlunoApresImprimirDeclaracao = requerimentoVisaoAlunoApresImprimirDeclaracao;
	}

	public Boolean getDeferirAutomaticamente() {
		if(deferirAutomaticamente == null){
			deferirAutomaticamente = Boolean.FALSE;
		}
		return deferirAutomaticamente;
	}

	public void setDeferirAutomaticamente(Boolean deferirAutomaticamente) {
		this.deferirAutomaticamente = deferirAutomaticamente;
	}

	public Integer getQtdeDiasDisponivel() {
		return qtdeDiasDisponivel;
	}

	public void setQtdeDiasDisponivel(Integer qtdeDiasDisponivel) {
		this.qtdeDiasDisponivel = qtdeDiasDisponivel;
	}

	public Integer getQtdeDiasAposPrimeiraImpressao() {
		return qtdeDiasAposPrimeiraImpressao;
	}

	public void setQtdeDiasAposPrimeiraImpressao(Integer qtdeDiasAposPrimeiraImpressao) {
		this.qtdeDiasAposPrimeiraImpressao = qtdeDiasAposPrimeiraImpressao;
	}

	public List<TipoRequerimentoCursoVO> getTipoRequerimentoCursoVOs() {
		if (tipoRequerimentoCursoVOs == null) {
			tipoRequerimentoCursoVOs = new ArrayList<TipoRequerimentoCursoVO>(0);
		}
		return tipoRequerimentoCursoVOs;
	}

	public void setTipoRequerimentoCursoVOs(List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs) {
		this.tipoRequerimentoCursoVOs = tipoRequerimentoCursoVOs;
	}
	
	@XmlElement(name = "isPermiteInformarDisciplina")
	public Boolean getIsPermiteInformarDisciplina(){
		return getTipo().equals(TiposRequerimento.REPOSICAO.getValor()) 
				|| getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor())  
				|| getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.getValor())  
				|| getTipo().equals(TiposRequerimento.ATIVIDADE_DISCURSIVA.getValor())
				|| getTipo().equals(TiposRequerimento.CERTIFICADO_MODULAR.getValor());
	}

	/**
	 * @return the requerimentoVisaoFuncionario
	 */
	public Boolean getRequerimentoVisaoFuncionario() {
		if (requerimentoVisaoFuncionario == null) {
			requerimentoVisaoFuncionario = false;
		}
		return requerimentoVisaoFuncionario;
	}

	/**
	 * @param requerimentoVisaoFuncionario the requerimentoVisaoFuncionario to set
	 */
	public void setRequerimentoVisaoFuncionario(Boolean requerimentoVisaoFuncionario) {
		this.requerimentoVisaoFuncionario = requerimentoVisaoFuncionario;
	}

	/**
	 * @return the considerarSegundaViaIndependenteSituacaoPrimeiraVia
	 */
	public Boolean getConsiderarSegundaViaIndependenteSituacaoPrimeiraVia() {
		if (considerarSegundaViaIndependenteSituacaoPrimeiraVia == null) {
			considerarSegundaViaIndependenteSituacaoPrimeiraVia = false;
		}
		return considerarSegundaViaIndependenteSituacaoPrimeiraVia;
	}

	/**
	 * @param considerarSegundaViaIndependenteSituacaoPrimeiraVia the considerarSegundaViaIndependenteSituacaoPrimeiraVia to set
	 */
	public void setConsiderarSegundaViaIndependenteSituacaoPrimeiraVia(Boolean considerarSegundaViaIndependenteSituacaoPrimeiraVia) {
		this.considerarSegundaViaIndependenteSituacaoPrimeiraVia = considerarSegundaViaIndependenteSituacaoPrimeiraVia;
	}

	/**
	 * @return the requerimentoMembroComunidade
	 */
	public Boolean getRequerimentoMembroComunidade() {
		if (requerimentoMembroComunidade == null) {
			requerimentoMembroComunidade = false;
		}
		return requerimentoMembroComunidade;
	}

	/**
	 * @param requerimentoMembroComunidade the requerimentoMembroComunidade to set
	 */
	public void setRequerimentoMembroComunidade(Boolean requerimentoMembroComunidade) {
		this.requerimentoMembroComunidade = requerimentoMembroComunidade;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 29/03/2016
	 */
	private Boolean bloquearRecebimentoCartaoCreditoOnline;

	@XmlElement(name = "bloquearRecebimentoCartaoCreditoOnline")
	public Boolean getBloquearRecebimentoCartaoCreditoOnline() {
		if(bloquearRecebimentoCartaoCreditoOnline == null) {
			bloquearRecebimentoCartaoCreditoOnline = Boolean.FALSE;
		}
		return bloquearRecebimentoCartaoCreditoOnline;
	}

	public void setBloquearRecebimentoCartaoCreditoOnline(Boolean bloquearRecebimentoCartaoCreditoOnline) {
		this.bloquearRecebimentoCartaoCreditoOnline = bloquearRecebimentoCartaoCreditoOnline;
	}
	
	public Boolean getAssinarDigitalmenteDeclaracoesGeradasNoRequerimento() {
		if(assinarDigitalmenteDeclaracoesGeradasNoRequerimento == null) {
			assinarDigitalmenteDeclaracoesGeradasNoRequerimento = Boolean.FALSE;
		}
		return assinarDigitalmenteDeclaracoesGeradasNoRequerimento;
	}

	public void setAssinarDigitalmenteDeclaracoesGeradasNoRequerimento(Boolean assinarDigitalmenteDeclaracoesGeradasNoRequerimento) {
		this.assinarDigitalmenteDeclaracoesGeradasNoRequerimento = assinarDigitalmenteDeclaracoesGeradasNoRequerimento;
	}
	
	public Boolean getConsiderarDiasUteis() {
		if(considerarDiasUteis == null){
			considerarDiasUteis = false;
		}
		return considerarDiasUteis;
	}

	public void setConsiderarDiasUteis(Boolean considerarDiasUteis) {
		this.considerarDiasUteis = considerarDiasUteis;
	}
	
	public boolean getIsCertificadoModular(){
		return getTipo().equals(TiposRequerimento.CERTIFICADO_MODULAR.getValor());
	}
	
	public Boolean getVerificarPendenciaApenasMatriculaRequerimento() {
		if (verificarPendenciaApenasMatriculaRequerimento == null) {
			verificarPendenciaApenasMatriculaRequerimento = Boolean.FALSE;
		}
		return verificarPendenciaApenasMatriculaRequerimento;
	}

	public void setVerificarPendenciaApenasMatriculaRequerimento(Boolean verificarPendenciaApenasMatriculaRequerimento) {
		this.verificarPendenciaApenasMatriculaRequerimento = verificarPendenciaApenasMatriculaRequerimento;
	}

	public TipoUploadArquivoEnum getTipoUploadArquivo() {
		if (tipoUploadArquivo == null) {
			tipoUploadArquivo = TipoUploadArquivoEnum.TODOS;
		}
		return tipoUploadArquivo;
	}

	public void setTipoUploadArquivo(TipoUploadArquivoEnum tipoUploadArquivo) {
		this.tipoUploadArquivo = tipoUploadArquivo;
	}

	@XmlElement(name = "extensaoArquivo")
	public String getExtensaoArquivo() {
		if (extensaoArquivo == null) {
			extensaoArquivo = getTipoUploadArquivo().getExtensao();
		}
		return extensaoArquivo;
	}

	public void setExtensaoArquivo(String extensaoArquivo) {
		this.extensaoArquivo = extensaoArquivo;
	}

	@XmlElement(name = "orientacaoUploadArquivo")
	public String getOrientacaoUploadArquivo() {
		if (orientacaoUploadArquivo == null) {
			orientacaoUploadArquivo = "";
		}
		return orientacaoUploadArquivo;
	}

	public void setOrientacaoUploadArquivo(String orientacaoUploadArquivo) {
		this.orientacaoUploadArquivo = orientacaoUploadArquivo;
	}

	public Boolean getIsUploudImagem() {
		return getTipoUploadArquivo().equals(TipoUploadArquivoEnum.IMAGEM);
	}

	public Boolean getRequerAutorizacaoPagamento() {
		if (requerAutorizacaoPagamento == null) {
			requerAutorizacaoPagamento = false;
		}
		return requerAutorizacaoPagamento;
	}

	public void setRequerAutorizacaoPagamento(Boolean requerAutorizacaoPagamento) {
		this.requerAutorizacaoPagamento = requerAutorizacaoPagamento;
	}
	
	

	public Double getPercentualMinimoCargaHorariaAproveitamento() {
		if (percentualMinimoCargaHorariaAproveitamento == null) {
			percentualMinimoCargaHorariaAproveitamento = 0.0;
		}
		return percentualMinimoCargaHorariaAproveitamento;
	}

	public void setPercentualMinimoCargaHorariaAproveitamento(Double percentualMinimoCargaHorariaAproveitamento) {
		this.percentualMinimoCargaHorariaAproveitamento = percentualMinimoCargaHorariaAproveitamento;
	}

	public Integer getQtdeMinimaDeAnosAproveitamento() {
		if (qtdeMinimaDeAnosAproveitamento == null) {
			qtdeMinimaDeAnosAproveitamento = 0;
		}
		return qtdeMinimaDeAnosAproveitamento;
	}

	public void setQtdeMinimaDeAnosAproveitamento(Integer qtdeMinimaDeAnosAproveitamento) {
		this.qtdeMinimaDeAnosAproveitamento = qtdeMinimaDeAnosAproveitamento;
	}

	public Integer getQtdeMaximaIndeferidoAproveitamento() {
		if (qtdeMaximaIndeferidoAproveitamento == null) {
			qtdeMaximaIndeferidoAproveitamento = 0;
		}
		return qtdeMaximaIndeferidoAproveitamento;
	}

	public void setQtdeMaximaIndeferidoAproveitamento(Integer qtdeMaximaIndeferidoAproveitamento) {
		this.qtdeMaximaIndeferidoAproveitamento = qtdeMaximaIndeferidoAproveitamento;
	}

	public String getMsgBloqueioNovaSolicitacaoAproveitamento() {
		if (msgBloqueioNovaSolicitacaoAproveitamento == null) {
			msgBloqueioNovaSolicitacaoAproveitamento = "";
		}
		return msgBloqueioNovaSolicitacaoAproveitamento;
	}

	public void setMsgBloqueioNovaSolicitacaoAproveitamento(String msgBloqueioNovaSolicitacaoAproveitamento) {
		this.msgBloqueioNovaSolicitacaoAproveitamento = msgBloqueioNovaSolicitacaoAproveitamento;
	}

	public Boolean getPermiteDeferirAguardandoAutorizacaoPagamento() {
		if (permiteDeferirAguardandoAutorizacaoPagamento == null) {
			permiteDeferirAguardandoAutorizacaoPagamento = false;
		}
		return permiteDeferirAguardandoAutorizacaoPagamento;
	}

	public void setPermiteDeferirAguardandoAutorizacaoPagamento(Boolean permiteDeferirAguardandoAutorizacaoPagamento) {
		this.permiteDeferirAguardandoAutorizacaoPagamento = permiteDeferirAguardandoAutorizacaoPagamento;
	}

	@XmlElement(name = "realizarIsencaoTaxaReposicaoMatriculaAposDataAula")
	public Boolean getRealizarIsencaoTaxaReposicaoMatriculaAposDataAula() {
		if (realizarIsencaoTaxaReposicaoMatriculaAposDataAula == null) {
			realizarIsencaoTaxaReposicaoMatriculaAposDataAula = false;
		}
		return realizarIsencaoTaxaReposicaoMatriculaAposDataAula;
	}

	public void setRealizarIsencaoTaxaReposicaoMatriculaAposDataAula(Boolean realizarIsencaoTaxaReposicaoMatriculaAposDataAula) {
		this.realizarIsencaoTaxaReposicaoMatriculaAposDataAula = realizarIsencaoTaxaReposicaoMatriculaAposDataAula;
	}

	@XmlElement(name = "quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao")
	public Integer getQuantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao() {
		if (quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao == null) {
			quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao = 0;
		}
		return quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao;
	}

	public void setQuantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao(Integer quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao) {
		this.quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao = quantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao;
	}

	@XmlElement(name = "permitirSolicitacaoIsencaoTaxaRequerimento")
	public Boolean getPermitirSolicitacaoIsencaoTaxaRequerimento() {
		if (permitirSolicitacaoIsencaoTaxaRequerimento == null) {
			permitirSolicitacaoIsencaoTaxaRequerimento = false;
		}
		return permitirSolicitacaoIsencaoTaxaRequerimento;
	}

	public void setPermitirSolicitacaoIsencaoTaxaRequerimento(Boolean permitirSolicitacaoIsencaoTaxaRequerimento) {
		this.permitirSolicitacaoIsencaoTaxaRequerimento = permitirSolicitacaoIsencaoTaxaRequerimento;
	}

	@XmlElement(name = "solicitarAnexoComprovanteIsencaoTaxaRequerimento")
	public Boolean getSolicitarAnexoComprovanteIsencaoTaxaRequerimento() {
		if (solicitarAnexoComprovanteIsencaoTaxaRequerimento == null) {
			solicitarAnexoComprovanteIsencaoTaxaRequerimento = false;
		}
		return solicitarAnexoComprovanteIsencaoTaxaRequerimento;
	}

	public void setSolicitarAnexoComprovanteIsencaoTaxaRequerimento(Boolean solicitarAnexoComprovanteIsencaoTaxaRequerimento) {
		this.solicitarAnexoComprovanteIsencaoTaxaRequerimento = solicitarAnexoComprovanteIsencaoTaxaRequerimento;
	}

	@XmlElement(name = "orientacaoDocumentoComprovanteIsencaoTaxaRequerimento")
	public String getOrientacaoDocumentoComprovanteIsencaoTaxaRequerimento() {
		if (orientacaoDocumentoComprovanteIsencaoTaxaRequerimento == null) {
			orientacaoDocumentoComprovanteIsencaoTaxaRequerimento = "";
		}
		return orientacaoDocumentoComprovanteIsencaoTaxaRequerimento;
	}

	public void setOrientacaoDocumentoComprovanteIsencaoTaxaRequerimento(String orientacaoDocumentoComprovanteIsencaoTaxaRequerimento) {
		this.orientacaoDocumentoComprovanteIsencaoTaxaRequerimento = orientacaoDocumentoComprovanteIsencaoTaxaRequerimento;
	}

	@XmlElement(name = "permiteIncluirDisciplinaPorEquivalencia")
	public Boolean getPermiteIncluirDisciplinaPorEquivalencia() {
		if (permiteIncluirDisciplinaPorEquivalencia == null) {
			permiteIncluirDisciplinaPorEquivalencia = false;
		}
		return permiteIncluirDisciplinaPorEquivalencia;
	}

	public void setPermiteIncluirDisciplinaPorEquivalencia(Boolean permiteIncluirDisciplinaPorEquivalencia) {
		this.permiteIncluirDisciplinaPorEquivalencia = permiteIncluirDisciplinaPorEquivalencia;
	}


	public Boolean getTipoSomenteVisaoAluno() {
		return getTipo().equals(TiposRequerimento.APROVEITAMENTO_DISCIPLINA.getValor()) ||
			  getTipo().equals(TiposRequerimento.ATIVIDADE_DISCURSIVA.getValor()) || 
			  getTipo().equals(TiposRequerimento.CANCELAMENTO.getValor()) ||
			  getTipo().equals(TiposRequerimento.CERTIFICADO_MODULAR.getValor()) ||
			  getTipo().equals(TiposRequerimento.COLACAO_GRAU.getValor()) ||
			  getTipo().equals(TiposRequerimento.DECLARACAO.getValor()) ||
			  getTipo().equals(TiposRequerimento.EXPEDICAO_DIPLOMA.getValor()) ||
			  getTipo().equals(TiposRequerimento.HISTORICO.getValor()) ||
			  getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.getValor()) ||
			  getTipo().equals(TiposRequerimento.PORTADOR_DE_DIPLOMA.getValor()) ||
			  getTipo().equals(TiposRequerimento.REATIVACAO_MATRICULA.getValor()) ||
			  getTipo().equals(TiposRequerimento.REPOSICAO.getValor()) ||
			  getTipo().equals(TiposRequerimento.SEGUNDA_CHAMADA.getValor()) ||
			  getTipo().equals(TiposRequerimento.TAXA_TCC.getValor()) ||
			  getTipo().equals(TiposRequerimento.TRANCAMENTO.getValor()) ||
			  getTipo().equals(TiposRequerimento.TRANSF_INTERNA.getValor()) ||
			  getTipo().equals(TiposRequerimento.TRANSF_SAIDA.getValor())||
			  getTipo().equals(TiposRequerimento.EMISSAO_CERTIFICADO.getValor())
			  ;
	}

	public Boolean getPermiteIncluirReposicaoTurmaOutraUnidade() {
		if (permiteIncluirReposicaoTurmaOutraUnidade == null) {
			permiteIncluirReposicaoTurmaOutraUnidade = false;
		}
		return permiteIncluirReposicaoTurmaOutraUnidade;
	}

	public void setPermiteIncluirReposicaoTurmaOutraUnidade(Boolean permiteIncluirReposicaoTurmaOutraUnidade) {
		this.permiteIncluirReposicaoTurmaOutraUnidade = permiteIncluirReposicaoTurmaOutraUnidade;
	}

	@XmlElement(name = "permiteIncluirReposicaoTurmaOutroCurso")
	public Boolean getPermiteIncluirReposicaoTurmaOutroCurso() {
		if (permiteIncluirReposicaoTurmaOutroCurso == null) {
			permiteIncluirReposicaoTurmaOutroCurso = false;
		}
		return permiteIncluirReposicaoTurmaOutroCurso;
	}

	public void setPermiteIncluirReposicaoTurmaOutroCurso(Boolean permiteIncluirReposicaoTurmaOutroCurso) {
		this.permiteIncluirReposicaoTurmaOutroCurso = permiteIncluirReposicaoTurmaOutroCurso;
	}

	@XmlElement(name = "permitirReposicaoComChoqueHorario")
	public Boolean getPermitirReposicaoComChoqueHorario() {
		if (permitirReposicaoComChoqueHorario == null) {
			permitirReposicaoComChoqueHorario = false;
		}
		return permitirReposicaoComChoqueHorario;
	}

	public void setPermitirReposicaoComChoqueHorario(Boolean permitirReposicaoComChoqueHorario) {
		this.permitirReposicaoComChoqueHorario = permitirReposicaoComChoqueHorario;
	}

	public Boolean getUsarCentroResultadoTurma() {
		usarCentroResultadoTurma = Optional.ofNullable(usarCentroResultadoTurma).orElse(true);
		return usarCentroResultadoTurma;
	}

	public void setUsarCentroResultadoTurma(Boolean usarCentroResultadoTurma) {
		this.usarCentroResultadoTurma = usarCentroResultadoTurma;
	}
	
	@XmlElement(name = "uploadArquivoObrigatorio")
	public Boolean getUploadArquivoObrigatorio() {
		if (uploadArquivoObrigatorio == null) {
			uploadArquivoObrigatorio = false;
        }
		return uploadArquivoObrigatorio;
	}
	
	public void setUploadArquivoObrigatorio(Boolean uploadArquivoObrigatorio) {
		this.uploadArquivoObrigatorio  = uploadArquivoObrigatorio;
	}
	
	public Boolean getFiltrarTipoReq() {
		if (filtrarTipoReq == null) {
			filtrarTipoReq = false;
		}
		return filtrarTipoReq;
	}

	public void setFiltrarTipoReq(Boolean filtrarTipoReq) {
		this.filtrarTipoReq = filtrarTipoReq;
	}
	
	public String getOrientacaoAtendente() {
		return orientacaoAtendente;
	}

	public void setOrientacaoAtendente(String orientacaoAtendente) {
		this.orientacaoAtendente = orientacaoAtendente;
	}
	public Boolean getBloquearQuantidadeRequerimentoAbertosSimultaneamente() {
		if (bloquearQuantidadeRequerimentoAbertosSimultaneamente == null) {
			bloquearQuantidadeRequerimentoAbertosSimultaneamente = false;
		}
		return bloquearQuantidadeRequerimentoAbertosSimultaneamente;
	}

	public void setBloquearQuantidadeRequerimentoAbertosSimultaneamente(
			Boolean bloquearQuantidadeRequerimentoAbertosSimultaneamente) {
		this.bloquearQuantidadeRequerimentoAbertosSimultaneamente = bloquearQuantidadeRequerimentoAbertosSimultaneamente;
	}

	public Integer getQuantidadeLimiteRequerimentoAbertoSimultaneamente() {
		if (quantidadeLimiteRequerimentoAbertoSimultaneamente == null) {
			quantidadeLimiteRequerimentoAbertoSimultaneamente = 1;
		}
		return quantidadeLimiteRequerimentoAbertoSimultaneamente;
	}

	public void setQuantidadeLimiteRequerimentoAbertoSimultaneamente(
			Integer quantidadeLimiteRequerimentoAbertoSimultaneamente) {
		this.quantidadeLimiteRequerimentoAbertoSimultaneamente = quantidadeLimiteRequerimentoAbertoSimultaneamente;
	}

	public Boolean getConsiderarBloqueioSimultaneoRequerimentoDeferido() {
		if (considerarBloqueioSimultaneoRequerimentoDeferido == null) {
			considerarBloqueioSimultaneoRequerimentoDeferido = false;
		}
		return considerarBloqueioSimultaneoRequerimentoDeferido;
	}

	public void setConsiderarBloqueioSimultaneoRequerimentoDeferido(
			Boolean considerarBloqueioSimultaneoRequerimentoDeferido) {
		this.considerarBloqueioSimultaneoRequerimentoDeferido = considerarBloqueioSimultaneoRequerimentoDeferido;
	}

	public Boolean getConsiderarBloqueioSimultaneoRequerimentoIndeferido() {
		if (considerarBloqueioSimultaneoRequerimentoIndeferido == null) {
			considerarBloqueioSimultaneoRequerimentoIndeferido = false;
		}
		return considerarBloqueioSimultaneoRequerimentoIndeferido;
	}

	public void setConsiderarBloqueioSimultaneoRequerimentoIndeferido(
			Boolean considerarBloqueioSimultaneoRequerimentoIndeferido) {
		this.considerarBloqueioSimultaneoRequerimentoIndeferido = considerarBloqueioSimultaneoRequerimentoIndeferido;
	}
	
	public Boolean getPermitirImpressaoHistoricoVisaoAluno() {
		if (permitirImpressaoHistoricoVisaoAluno == null) {
			permitirImpressaoHistoricoVisaoAluno = false;
		}
		return permitirImpressaoHistoricoVisaoAluno;
	}

	public void setPermitirImpressaoHistoricoVisaoAluno(Boolean permitirImpressaoHistoricoVisaoAluno) {
		this.permitirImpressaoHistoricoVisaoAluno = permitirImpressaoHistoricoVisaoAluno;
	}

	@XmlElement(name = "nivelEducacional")
	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}
	
	@XmlElement(name = "certificadoImpresso")
	public TextoPadraoVO getCertificadoImpresso() {
		if(certificadoImpresso == null) {
			certificadoImpresso = new TextoPadraoVO();
		}
		return certificadoImpresso;
	}

	public void setCertificadoImpresso(TextoPadraoVO certificadoImpresso) {
		this.certificadoImpresso = certificadoImpresso;
	}	

	public Boolean getApenasParaAlunosComTodasAulasRegistradas() {
		if(apenasParaAlunosComTodasAulasRegistradas == null) {
			apenasParaAlunosComTodasAulasRegistradas = Boolean.FALSE;
		}
		return apenasParaAlunosComTodasAulasRegistradas;
	}

	public void setApenasParaAlunosComTodasAulasRegistradas(Boolean apenasParaAlunosComTodasAulasRegistradas) {
		this.apenasParaAlunosComTodasAulasRegistradas = apenasParaAlunosComTodasAulasRegistradas;
	}

	public Boolean getVerificarAlunoPossuiPeloMenosUmaDisciplinaAprovada() {
		if(verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada == null) {
			verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada = Boolean.FALSE;
		}
		return verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada;
	}

	public void setVerificarAlunoPossuiPeloMenosUmaDisciplinaAprovada(
			Boolean verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada) {
		this.verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada = verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada;
	}

	@XmlElement(name = "registrarFormaturaAoRealizarImpressaoCerticadoDigital")
	public Boolean getRegistrarFormaturaAoRealizarImpressaoCerticadoDigital() {
		if(registrarFormaturaAoRealizarImpressaoCerticadoDigital == null) {
			registrarFormaturaAoRealizarImpressaoCerticadoDigital = Boolean.FALSE;
		}
		return registrarFormaturaAoRealizarImpressaoCerticadoDigital;
	}
	
	public List<TipoDocumentoVO> tipoDocumentoVOs;

	public List<TipoDocumentoVO> getTipoDocumentoVOs() {
		if(tipoDocumentoVOs == null) {
			tipoDocumentoVOs =  new ArrayList<>(0);
			for(PendenciaTipoDocumentoTipoRequerimentoVO pendenciaTipoDocumentoTipoRequerimentoVO: getPendenciaTipoDocumentoTipoRequerimentoVOs()) {
				tipoDocumentoVOs.add(pendenciaTipoDocumentoTipoRequerimentoVO.getTipoDocumento());
			}
		}
		return tipoDocumentoVOs;
	}

	public void setTipoDocumentoVOs(List<TipoDocumentoVO> tipoDocumentoVOs) {
		this.tipoDocumentoVOs = tipoDocumentoVOs;
	}

	public void setRegistrarFormaturaAoRealizarImpressaoCerticadoDigital(
			Boolean registrarFormaturaAoRealizarImpressaoCerticadoDigital) {
		this.registrarFormaturaAoRealizarImpressaoCerticadoDigital = registrarFormaturaAoRealizarImpressaoCerticadoDigital;
	}
	
	public boolean getIsEmissaoCertificado(){
		return getTipo().equals(TiposRequerimento.EMISSAO_CERTIFICADO.getValor());
	}
	
	public boolean getIsDeclaracao(){
		return getTipo().equals(TiposRequerimento.DECLARACAO.getValor());
	}

	public List<PendenciaTipoDocumentoTipoRequerimentoVO> getPendenciaTipoDocumentoTipoRequerimentoVOs() {
        if (pendenciaTipoDocumentoTipoRequerimentoVOs == null) {
        	pendenciaTipoDocumentoTipoRequerimentoVOs = new ArrayList<PendenciaTipoDocumentoTipoRequerimentoVO>();
        }
		return pendenciaTipoDocumentoTipoRequerimentoVOs;
	}

	public void setPendenciaTipoDocumentoTipoRequerimentoVOs(
			List<PendenciaTipoDocumentoTipoRequerimentoVO> pendenciaTipoDocumentoTipoRequerimentoVOs) {
		this.pendenciaTipoDocumentoTipoRequerimentoVOs = pendenciaTipoDocumentoTipoRequerimentoVOs;
	}

	public String getLayoutHistoricoApresentar() {
		if (layoutHistoricoApresentar == null) {
			layoutHistoricoApresentar = "";
		}
		return layoutHistoricoApresentar;
	}

	public void setLayoutHistoricoApresentar(String layoutHistoricoApresentar) {
		this.layoutHistoricoApresentar = layoutHistoricoApresentar;
	}

	public Boolean getAprovadoSituacaoHistorico() {
		if (aprovadoSituacaoHistorico == null) {
			aprovadoSituacaoHistorico = true;
		}
		return aprovadoSituacaoHistorico;
	}

	public void setAprovadoSituacaoHistorico(Boolean aprovadoSituacaoHistorico) {
		this.aprovadoSituacaoHistorico = aprovadoSituacaoHistorico;
	}

	public Boolean getReprovadoSituacaoHistorico() {
		if (reprovadoSituacaoHistorico == null) {
			reprovadoSituacaoHistorico = true;
		}
		return reprovadoSituacaoHistorico;
	}

	public void setReprovadoSituacaoHistorico(Boolean reprovadoSituacaoHistorico) {
		this.reprovadoSituacaoHistorico = reprovadoSituacaoHistorico;
	}

	public Boolean getTrancadoSituacaoHistorico() {
		if (trancadoSituacaoHistorico == null) {
			trancadoSituacaoHistorico = true;
		}
		return trancadoSituacaoHistorico;
	}

	public void setTrancadoSituacaoHistorico(Boolean trancadoSituacaoHistorico) {
		this.trancadoSituacaoHistorico = trancadoSituacaoHistorico;
	}

	public Boolean getCursandoSituacaoHistorico() {
		if (cursandoSituacaoHistorico == null) {
			cursandoSituacaoHistorico = true;
		}
		return cursandoSituacaoHistorico;
	}

	public void setCursandoSituacaoHistorico(Boolean cursandoSituacaoHistorico) {
		this.cursandoSituacaoHistorico = cursandoSituacaoHistorico;
	}

	public Boolean getAbandonoCursoSituacaoHistorico() {
		if (abandonoCursoSituacaoHistorico == null) {
			abandonoCursoSituacaoHistorico = true;
		}
		return abandonoCursoSituacaoHistorico;
	}

	public void setAbandonoCursoSituacaoHistorico(Boolean abandonoCursoSituacaoHistorico) {
		this.abandonoCursoSituacaoHistorico = abandonoCursoSituacaoHistorico;
	}

	public Boolean getTransferidoSituacaoHistorico() {
		if (transferidoSituacaoHistorico == null) {
			transferidoSituacaoHistorico = true;
		}
		return transferidoSituacaoHistorico;
	}

	public void setTransferidoSituacaoHistorico(Boolean transferidoSituacaoHistorico) {
		this.transferidoSituacaoHistorico = transferidoSituacaoHistorico;
	}

	public Boolean getCanceladoSituacaoHistorico() {
		if (canceladoSituacaoHistorico == null) {
			canceladoSituacaoHistorico = true;
		}
		return canceladoSituacaoHistorico;
	}

	public void setCanceladoSituacaoHistorico(Boolean canceladoSituacaoHistorico) {
		this.canceladoSituacaoHistorico = canceladoSituacaoHistorico;
	}
	
	public void realizarMarcarTodasSituacoesHistorico(){
		realizarSelecionarTodosSituacoesHistorico(true);
	}
	
	public void realizarDesmarcarTodasSituacoesHistorico(){
		realizarSelecionarTodosSituacoesHistorico(false);
	}

	public void realizarSelecionarTodosSituacoesHistorico(boolean selecionado){
		setReprovadoSituacaoHistorico(selecionado); 
		setAprovadoSituacaoHistorico(selecionado);
		setCursandoSituacaoHistorico(selecionado); 
		setTrancadoSituacaoHistorico(selecionado); 
		setAbandonoCursoSituacaoHistorico(selecionado); 
		setCanceladoSituacaoHistorico(selecionado); 
		setTransferidoSituacaoHistorico(selecionado); 
		 	
	}

	public Boolean getAssinarDigitalmenteHistorico() {
		if (assinarDigitalmenteHistorico == null) {
			assinarDigitalmenteHistorico = false;
		}
		return assinarDigitalmenteHistorico;
	}

	public void setAssinarDigitalmenteHistorico(Boolean assinarDigitalmenteHistorico) {
		this.assinarDigitalmenteHistorico = assinarDigitalmenteHistorico;
	}

	@XmlElement(name = "cobrarTaxaSomenteCertificadoImpresso")
	public Boolean getCobrarTaxaSomenteCertificadoImpresso() {
		if(cobrarTaxaSomenteCertificadoImpresso == null) {
			cobrarTaxaSomenteCertificadoImpresso = Boolean.FALSE;
		}
		return cobrarTaxaSomenteCertificadoImpresso;
	}

	public void setCobrarTaxaSomenteCertificadoImpresso(Boolean cobrarTaxaSomenteCertificadoImpresso) {
		this.cobrarTaxaSomenteCertificadoImpresso = cobrarTaxaSomenteCertificadoImpresso;
	}

	@XmlElement(name = "mensagemEmissaoCertificadoImpresso")
	public String getMensagemEmissaoCertificadoImpresso() {
		if(mensagemEmissaoCertificadoImpresso == null) {
			mensagemEmissaoCertificadoImpresso = "";
		}
		return mensagemEmissaoCertificadoImpresso;
	}

	public void setMensagemEmissaoCertificadoImpresso(String mensagemEmissaoCertificadoImpresso) {
		this.mensagemEmissaoCertificadoImpresso = mensagemEmissaoCertificadoImpresso;
	}

	public Boolean getPermitirAlterarDataPrevisaoConclusaoRequerimento() {
		if (permitirAlterarDataPrevisaoConclusaoRequerimento == null) {
			permitirAlterarDataPrevisaoConclusaoRequerimento = Boolean.FALSE;
		}
		return permitirAlterarDataPrevisaoConclusaoRequerimento;
	}

	public void setPermitirAlterarDataPrevisaoConclusaoRequerimento(
			Boolean permitirAlterarDataPrevisaoConclusaoRequerimento) {
		this.permitirAlterarDataPrevisaoConclusaoRequerimento = permitirAlterarDataPrevisaoConclusaoRequerimento;
	}

	public Boolean getAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento() {
		if (abrirOutroRequerimentoAoDeferirEsteTipoRequerimento == null) {
			abrirOutroRequerimentoAoDeferirEsteTipoRequerimento = Boolean.FALSE;
		}
		return abrirOutroRequerimentoAoDeferirEsteTipoRequerimento;
	}

	public void setAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento(
			Boolean abrirOutroRequerimentoAoDeferirEsteTipoRequerimento) {
		this.abrirOutroRequerimentoAoDeferirEsteTipoRequerimento = abrirOutroRequerimentoAoDeferirEsteTipoRequerimento;
	}

	public TipoRequerimentoVO getTipoRequerimentoAbrirDeferimento() {
		if (tipoRequerimentoAbrirDeferimento == null) {
			tipoRequerimentoAbrirDeferimento = new TipoRequerimentoVO();
		}
		return tipoRequerimentoAbrirDeferimento;
	}

	public void setTipoRequerimentoAbrirDeferimento(TipoRequerimentoVO tipoRequerimentoAbrirDeferimento) {
		this.tipoRequerimentoAbrirDeferimento = tipoRequerimentoAbrirDeferimento;
	}

	@XmlElement(name = "qtdDiasCobrarTaxa")
	public Integer getQtdDiasCobrarTaxa() {
		if (qtdDiasCobrarTaxa == null) {
			qtdDiasCobrarTaxa = 0;
		}
		return qtdDiasCobrarTaxa;
	}

	public void setQtdDiasCobrarTaxa(Integer qtdDiasCobrarTaxa) {
		this.qtdDiasCobrarTaxa = qtdDiasCobrarTaxa;
	}

	public Boolean getValidarMatriculaIntegralizada() {
		if(validarMatriculaIntegralizada == null) {
			validarMatriculaIntegralizada = Boolean.FALSE;
		}
		return validarMatriculaIntegralizada;
	}

	public void setValidarMatriculaIntegralizada(Boolean validarMatriculaIntegralizada) {
		this.validarMatriculaIntegralizada = validarMatriculaIntegralizada;
	}
	

	@XmlElement(name = "ocultarUnidadeEnsinoListaTurmaReposicao")
	public Boolean getOcultarUnidadeEnsinoListaTurmaReposicao() {
		if (ocultarUnidadeEnsinoListaTurmaReposicao == null) {
			ocultarUnidadeEnsinoListaTurmaReposicao = Boolean.FALSE;
		}
		return ocultarUnidadeEnsinoListaTurmaReposicao;
	}

	public void setOcultarUnidadeEnsinoListaTurmaReposicao(Boolean ocultarUnidadeEnsinoListaTurmaReposicao) {
		this.ocultarUnidadeEnsinoListaTurmaReposicao = ocultarUnidadeEnsinoListaTurmaReposicao;
	}

	public Boolean getValidarDebitoFinanceiroRequerimentoIsento() {
		if (validarDebitoFinanceiroRequerimentoIsento == null) {
			validarDebitoFinanceiroRequerimentoIsento = false;
		}
		return validarDebitoFinanceiroRequerimentoIsento;
	}

	public void setValidarDebitoFinanceiroRequerimentoIsento(Boolean validarDebitoFinanceiroRequerimentoIsento) {
		this.validarDebitoFinanceiroRequerimentoIsento = validarDebitoFinanceiroRequerimentoIsento;
	}
	
	public Boolean getPermitirReporDisciplinaComAulaProgramada() {
		if (permitirReporDisciplinaComAulaProgramada == null) {
			permitirReporDisciplinaComAulaProgramada = Boolean.FALSE;
		}
		return permitirReporDisciplinaComAulaProgramada;
	}

	public void setPermitirReporDisciplinaComAulaProgramada(Boolean permitirReporDisciplinaComAulaProgramada) {
		this.permitirReporDisciplinaComAulaProgramada = permitirReporDisciplinaComAulaProgramada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoRequerimentoVO other = (TipoRequerimentoVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public Boolean getValidarEntregaTccAluno() {
		if(validarEntregaTccAluno == null) {
			validarEntregaTccAluno = Boolean.FALSE;
		}
		return validarEntregaTccAluno;
	}

	public void setValidarEntregaTccAluno(Boolean validarEntregaTccAluno) {
		this.validarEntregaTccAluno = validarEntregaTccAluno;
	}

	public Boolean getDeferirAutomaticamenteDocumentoImpresso() {
		if(deferirAutomaticamenteDocumentoImpresso == null) {
			deferirAutomaticamenteDocumentoImpresso = Boolean.FALSE;
		}
		return deferirAutomaticamenteDocumentoImpresso;
	}

	public void setDeferirAutomaticamenteDocumentoImpresso(Boolean deferirAutomaticamenteDocumentoImpresso) {
		this.deferirAutomaticamenteDocumentoImpresso = deferirAutomaticamenteDocumentoImpresso;
	}

	public Boolean getDeferirAutomaticamenteTrancamento() {
		if (deferirAutomaticamenteTrancamento == null) {
			deferirAutomaticamenteTrancamento = false;
		}
		return deferirAutomaticamenteTrancamento;
	}

	public void setDeferirAutomaticamenteTrancamento(Boolean deferirAutomaticamenteTrancamento) {
		this.deferirAutomaticamenteTrancamento = deferirAutomaticamenteTrancamento;
	}
	
	public String getBloqueioSimultaneoPelo() {
		if (bloqueioSimultaneoPelo == null) {
			bloqueioSimultaneoPelo = "TIPO";
		}
    	return bloqueioSimultaneoPelo;
	}
    
    public void setBloqueioSimultaneoPelo(String bloqueioSimultaneoPelo) {
		this.bloqueioSimultaneoPelo = bloqueioSimultaneoPelo;
	}
	
    public void validarTipoRequerimentoTrasferenciaInterna() throws Exception {
        if(!getIsTipoTransferenciaInterna()) {
        	throw new Exception("Não e possivel adicionar Curso para Transf. Interna , O campo TIPO (Tipo Requerimento) esta diferente de Transf. Interna .");
		}    	
	 
	
    
	}

	public Boolean getPermitirAlunoAlterarUnidadeEnsino() {
		if(permitirAlunoAlterarUnidadeEnsino == null ) {
			permitirAlunoAlterarUnidadeEnsino = Boolean.FALSE ;
		}
		return permitirAlunoAlterarUnidadeEnsino;
	}

	public void setPermitirAlunoAlterarUnidadeEnsino(Boolean permitirAlunoAlterarUnidadeEnsino) {
		this.permitirAlunoAlterarUnidadeEnsino = permitirAlunoAlterarUnidadeEnsino;
	}

	public Boolean getPermitirAlunoRejeitarDocumento() {
		if(permitirAlunoRejeitarDocumento == null ) {
			permitirAlunoRejeitarDocumento = Boolean.FALSE ;
		}
		return permitirAlunoRejeitarDocumento;
	}

	public void setPermitirAlunoRejeitarDocumento(Boolean permitirAlunoRejeitarDocumento) {
		this.permitirAlunoRejeitarDocumento = permitirAlunoRejeitarDocumento;
	}

	public Integer getPercentualIntegralizacaoCurricularInicial() {
		if(percentualIntegralizacaoCurricularInicial == null) {
			percentualIntegralizacaoCurricularInicial = 100;
		}
		return percentualIntegralizacaoCurricularInicial;
	}

	public void setPercentualIntegralizacaoCurricularInicial(Integer percentualIntegralizacaoCurricularInicial) {
		this.percentualIntegralizacaoCurricularInicial = percentualIntegralizacaoCurricularInicial;
	}

	public Integer getPercentualIntegralizacaoCurricularFinal() {
		if(percentualIntegralizacaoCurricularFinal == null) {
			percentualIntegralizacaoCurricularFinal = 100;
		}
		return percentualIntegralizacaoCurricularFinal;
	}

	public void setPercentualIntegralizacaoCurricularFinal(Integer percentualIntegralizacaoCurricularFinal) {
		this.percentualIntegralizacaoCurricularFinal = percentualIntegralizacaoCurricularFinal;
	}

	public Boolean getRegistrarAproveitamentoDisciplinaTCC() {
		if(registrarAproveitamentoDisciplinaTCC == null) {
			registrarAproveitamentoDisciplinaTCC = false;
		}
		return registrarAproveitamentoDisciplinaTCC;
	}

	public void setRegistrarAproveitamentoDisciplinaTCC(Boolean registrarAproveitamentoDisciplinaTCC) {
		this.registrarAproveitamentoDisciplinaTCC = registrarAproveitamentoDisciplinaTCC;
	}

	public Boolean getRegistrarTrancamentoProximoSemestre() {
		if(registrarTrancamentoProximoSemestre == null) {
			registrarTrancamentoProximoSemestre =  false;
		}
		return registrarTrancamentoProximoSemestre;
	}

	public void setRegistrarTrancamentoProximoSemestre(Boolean registrarTrancamentoProximoSemestre) {
		this.registrarTrancamentoProximoSemestre = registrarTrancamentoProximoSemestre;
	}

	public Boolean getValidarAnoSemestreIngresso() {
		if(validarAnoSemestreIngresso == null) {
			validarAnoSemestreIngresso = Boolean.FALSE;
		}
		return validarAnoSemestreIngresso;
	}

	public void setValidarAnoSemestreIngresso(Boolean validarAnoSemestreIngresso) {
		this.validarAnoSemestreIngresso = validarAnoSemestreIngresso;
	}

	public String getAnoIngresso() {
		if(anoIngresso == null) {
			anoIngresso = "";
		}
		return anoIngresso;
	}

	public void setAnoIngresso(String anoIngresso) {
		this.anoIngresso = anoIngresso;
	}

	public String getSemestreIngresso() {
		if(semestreIngresso == null) {
			semestreIngresso = "";
		}
		return semestreIngresso;
	}

	public void setSemestreIngresso(String semestreIngresso) {
		this.semestreIngresso = semestreIngresso;
	}

	public Boolean getConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea() {
		if (considerarTodasMatriculasAlunoValidacaoAberturaSimultanea == null) {
			considerarTodasMatriculasAlunoValidacaoAberturaSimultanea = Boolean.FALSE;		
			}
		return considerarTodasMatriculasAlunoValidacaoAberturaSimultanea;
	}

	public void setConsiderarTodasMatriculasAlunoValidacaoAberturaSimultanea(
			Boolean considerarTodasMatriculasAlunoValidacaoAberturaSimultanea) {
		this.considerarTodasMatriculasAlunoValidacaoAberturaSimultanea = considerarTodasMatriculasAlunoValidacaoAberturaSimultanea;
	} 
	
	public boolean getIsCertificadoParticipacaoTCC(){
		return getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor());
	}

	public Boolean getPermitirAlunoAlterarCurso() {
		if(permitirAlunoAlterarCurso == null) {
			permitirAlunoAlterarCurso =  false;
		}
		return permitirAlunoAlterarCurso;
	}

	public void setPermitirAlunoAlterarCurso(Boolean permitirAlunoAlterarCurso) {
		
		this.permitirAlunoAlterarCurso = permitirAlunoAlterarCurso;
	}

	public Boolean getValidarVagasPorNumeroComputadoresUnidadeEnsino() {
		if(validarVagasPorNumeroComputadoresUnidadeEnsino == null) {
			validarVagasPorNumeroComputadoresUnidadeEnsino =  false;
		}
		return validarVagasPorNumeroComputadoresUnidadeEnsino;
	}

	public void setValidarVagasPorNumeroComputadoresUnidadeEnsino(Boolean validarVagasPorNumeroComputadoresUnidadeEnsino) {
		this.validarVagasPorNumeroComputadoresUnidadeEnsino = validarVagasPorNumeroComputadoresUnidadeEnsino;
	}

	public Boolean getRegistrarTransferenciaProximoSemestre() {
		if(registrarTransferenciaProximoSemestre == null) {
			registrarTransferenciaProximoSemestre =  false;
		}
			
		return registrarTransferenciaProximoSemestre;
	}

	public void setRegistrarTransferenciaProximoSemestre(Boolean registrarTransferenciaProximoSemestre) {
		this.registrarTransferenciaProximoSemestre = registrarTransferenciaProximoSemestre;
	}

	public String getCidDeferirAutomaticamente() {
		if(cidDeferirAutomaticamente == null) {
			cidDeferirAutomaticamente = "";
		}
		return cidDeferirAutomaticamente;
	}

	public void setCidDeferirAutomaticamente(String cidDeferirAutomaticamente) {
		this.cidDeferirAutomaticamente = cidDeferirAutomaticamente;
	}

	public Boolean getValidarVagasPorNumeroComputadoresConsiderandoCurso() {
		if(validarVagasPorNumeroComputadoresConsiderandoCurso == null) {
			validarVagasPorNumeroComputadoresConsiderandoCurso =  false;
		}
		return validarVagasPorNumeroComputadoresConsiderandoCurso;
	}

	public void setValidarVagasPorNumeroComputadoresConsiderandoCurso(
			Boolean validarVagasPorNumeroComputadoresConsiderandoCurso) {
		this.validarVagasPorNumeroComputadoresConsiderandoCurso = validarVagasPorNumeroComputadoresConsiderandoCurso;
	}

	public String getAno() {
		if(ano == null) {
			ano =  Uteis.getAnoDataAtual();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null) {
			semestre =  Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	public CidTipoRequerimentoVO getCidTipoRequerimentoVO() {
		if (cidTipoRequerimentoVO == null) {
			cidTipoRequerimentoVO = new CidTipoRequerimentoVO();
        }
		return cidTipoRequerimentoVO;
	}

	public void setCidTipoRequerimentoVO(CidTipoRequerimentoVO cidTipoRequerimentoVO) {
		this.cidTipoRequerimentoVO = cidTipoRequerimentoVO;
	}

	public List<CidTipoRequerimentoVO> getCidTipoRequerimentoVOs() {
		if (cidTipoRequerimentoVOs == null) {
			cidTipoRequerimentoVOs = new ArrayList<CidTipoRequerimentoVO>(0);
		}
		return cidTipoRequerimentoVOs;
	}

	public void setCidTipoRequerimentoVOs(List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs) {
		this.cidTipoRequerimentoVOs = cidTipoRequerimentoVOs;
	}

	public Boolean getPermitirAproveitarDisciplinaCursando() {
		if(permitirAproveitarDisciplinaCursando == null) {
			permitirAproveitarDisciplinaCursando =  true;
		}
		return permitirAproveitarDisciplinaCursando;
	}

	public void setPermitirAproveitarDisciplinaCursando(Boolean permitirAproveitarDisciplinaCursando) {
		this.permitirAproveitarDisciplinaCursando = permitirAproveitarDisciplinaCursando;
	}
	public String getBimestre() {
		if(bimestre == null) {
			bimestre = "";
		}
		return bimestre;
	}

	public void setBimestre(String bimestre) {
		this.bimestre = bimestre;
	}

	public String getTipoNota() {
		if(tipoNota == null) {
			tipoNota = "";
		}
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	public Boolean getEnviarNotificacaoRequerente() {
		if(enviarNotificacaoRequerente == null) {
			enviarNotificacaoRequerente = Boolean.TRUE;
		}
		return enviarNotificacaoRequerente;
	}

	public void setEnviarNotificacaoRequerente(Boolean enviarNotificacaoRequerente) {
		this.enviarNotificacaoRequerente = enviarNotificacaoRequerente;
	}

	public Boolean getCampoAfastamento() {
		if (campoAfastamento == null) {
			campoAfastamento = Boolean.FALSE;
		}
		return campoAfastamento;
	}

	public void setCampoAfastamento(Boolean campoAfastamento) {
		this.campoAfastamento = campoAfastamento;
	}
	
	public Boolean getUtilizarMensagemDeferimentoExclusivo() {
		if (utilizarMensagemDeferimentoExclusivo == null) {
			utilizarMensagemDeferimentoExclusivo = Boolean.FALSE;
		}
		return utilizarMensagemDeferimentoExclusivo;
	}

	public void setUtilizarMensagemDeferimentoExclusivo(Boolean utilizarMensagemDeferimentoExclusivo) {
		this.utilizarMensagemDeferimentoExclusivo = utilizarMensagemDeferimentoExclusivo;
	}

	public Boolean getUtilizarMensagemIndeferimentoExclusivo() {
		if (utilizarMensagemIndeferimentoExclusivo == null) {
			utilizarMensagemIndeferimentoExclusivo = Boolean.FALSE;
		}
		return utilizarMensagemIndeferimentoExclusivo;
	}

	public void setUtilizarMensagemIndeferimentoExclusivo(Boolean utilizarMensagemIndeferimentoExclusivo) {
		this.utilizarMensagemIndeferimentoExclusivo = utilizarMensagemIndeferimentoExclusivo;
	}

	public PersonalizacaoMensagemAutomaticaVO getPersonalizacaoMensagemAutomaticaDeferimento() {
		if (personalizacaoMensagemAutomaticaDeferimento == null) {
			personalizacaoMensagemAutomaticaDeferimento = new PersonalizacaoMensagemAutomaticaVO();
		}
		return personalizacaoMensagemAutomaticaDeferimento;
	}

	public void setPersonalizacaoMensagemAutomaticaDeferimento(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaDeferimento) {
		this.personalizacaoMensagemAutomaticaDeferimento = personalizacaoMensagemAutomaticaDeferimento;
	}

	public PersonalizacaoMensagemAutomaticaVO getPersonalizacaoMensagemAutomaticaIndeferimento() {
		if (personalizacaoMensagemAutomaticaIndeferimento == null) {
			personalizacaoMensagemAutomaticaIndeferimento = new PersonalizacaoMensagemAutomaticaVO();
		}
		return personalizacaoMensagemAutomaticaIndeferimento;
	}

	public void setPersonalizacaoMensagemAutomaticaIndeferimento(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaIndeferimento) {
		this.personalizacaoMensagemAutomaticaIndeferimento = personalizacaoMensagemAutomaticaIndeferimento;
	}
	
	public TipoAlunoEnum getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = TipoAlunoEnum.AMBOS;
		}
		return tipoAluno;
	}
	
	public void setTipoAluno(TipoAlunoEnum tipoAluno) {
		this.tipoAluno = tipoAluno;
	}
	
	public boolean isTipoAlunoAmbos() {
		return Uteis.isAtributoPreenchido(getTipoAluno()) && getTipoAluno().equals(TipoAlunoEnum.AMBOS);
	}
	
	public boolean isTipoAlunoCalouro() {
		return Uteis.isAtributoPreenchido(getTipoAluno()) && getTipoAluno().equals(TipoAlunoEnum.CALOURO);
	}
	
	public boolean isTipoAlunoVeterano() {
		return Uteis.isAtributoPreenchido(getTipoAluno()) && getTipoAluno().equals(TipoAlunoEnum.VETERANO);
	}
}
