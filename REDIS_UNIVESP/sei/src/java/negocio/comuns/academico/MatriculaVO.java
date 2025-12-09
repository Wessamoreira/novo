package negocio.comuns.academico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.TipoTrabalhoConclusaoCurso;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.PesquisaPadraoAlunoResponsavelVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeRespostaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.OpcaoPagamentoDividaVO;
import negocio.comuns.financeiro.PerfilEconomicoCondicaoNegociacaoVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.sad.MatriculaDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.JustificativaCensoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.SituacaoItemEmprestimo;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TipoMobilidadeAcademicaEnum;

/**
 * Reponsável por manter os dados da entidade Matricula. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

@XmlRootElement(name = "matricula")

public class MatriculaVO extends SuperVO {
    /**
     * TRANSIENT - VERSAO 5.0
     * Caso o usuário tenha permissão em seu perfil de acesso, poderá liberar
     * a matrícula / renovação em qualquer período letivo. Este recurso será
     * utilizado somente quando a configuracao do curso utilizar controle
     * de evolução acadêmica do aluno. 
     */
    private Boolean liberarMatriculaTodosPeriodos;
    /**
     * TRASIENT - VERSAO 5.0
     * Utilizado para controlar se algum usuario liberou a renovacao do aluno
     * mesmo que o aluno esteja devendo algum documento que impeça a renovação
     * de matrícula.
     */
    private Boolean liberarRenovacaoDocumentaoImpediRenovacaoPendente;
    /**
     * TRASIENT - VERSAO 5.0
     * Utilizado para controlar se algum usuario liberou a renovacao do aluno
     * mesmo que o aluno esteja devendo algum documento que impeça a renovação
     * de matrícula.
     */
    private Boolean existeDocumentoPendenteImpediRenovacao;
    
    /**
     * TRANSIENT - VERSAO 5.0
     * Atributo utilizado pelo método que carrega MatriculaComHistorivoAlunoVO
     * quando uma nova matrícula está sendo realizada por meio de uma transferencia
     * de entrada, com um aproveitamento de disciplinas previsto vinculado à mesma.
     * Assim, este atributo é utilizado para fornecer como parametro para este método
     * supracitado, a lista de históricos previstos que foram registrados no aproveitamento
     * de forma que a renovação já considere estas disciplinas do aproveitamento como aprovadas.
     * Isto possibilitará ao aluno, por exemplo, já se matricular em um período letivo 
     * avançado, evitando atrasos na vida academica do mesmo e economia financeira para o mesmo.
     */
    private List<HistoricoVO> historicosAproveitamentoDisciplinaPrevisto;
    /**
     * ATRIBUTO TRANSIENTE
     * utilizado na versão 5.0, para que quando uma matricula for gravada (Inserida) para um aproveitamento
     * que é previsto, então este aproveitamento seja persistido - como não sendo mais temporário
     * Isto fará com que automaticamente todos os históricos definitivos para os aproveitamentos das
     * disciplinas sejam geradas e vinculadas a matrícula em questão.
     */
    private AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO;
    private String matricula;
    /**
     * Atributo criado na versão 5.0 
     * O mesmo controla a matriz curricular atual do aluno. E que será utilizada
     * em caso de uma transferencia de matriz curricular ao longo dos periodos letivos
     * por registrar qual a última grade do aluno (ou seja, a que está atualmente válida
     * para o aluon).
     */
    private GradeCurricularVO gradeCurricularAtual;
    private Date data;
    private String situacao;
    private String localArmazenamentoDocumentosMatricula;
    private Boolean alunoAbandonouCurso;
    private Date dataInicioCurso;
    private Date dataConclusaoCurso;
    private String situacaoFinanceira;
    private Boolean descontoChancela;
    private Boolean naoEnviarMensagemCobranca;
    private Boolean considerarParcelasMaterialDidaticoReajustePreco;
    private Boolean alunoConcluiuDisciplinasRegulares;
    private Date dataAlunoConcluiuDisciplinasRegulares;
    private ObservacaoComplementarHistoricoAlunoVO observacaoComplementarHistoricoAlunoVO;
    private Boolean liberarBloqueioAlunoInadimplente;
    private Boolean bloquearEmissaoBoletoMatMenVisaoAluno;    
    @ExcluirJsonAnnotation
    private SimpleDateFormat formatadorDataAno4Digitos = new SimpleDateFormat("dd/MM/yyyy");
    private String codigoInscricaoOVG;	
    /**
     * Atributo responsável por manter os objetos da classe <code>DocumetacaoMatricula</code>.
     */
    private List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs;
    /**
     * Atributo responsável por manter os objetos da classe <code>MatriculaPeriodo</code>.
     */
    private List<MatriculaPeriodoVO> matriculaPeriodoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
     */
    private PessoaVO aluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Inscricao </code>.
     */
    private InscricaoVO inscricao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
     */
    private UsuarioVO usuario;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
     */
    private UsuarioVO usuarioLiberacaoDesconto;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Turno </code>.
     */
    private TurnoVO turno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>TipoMidiaCaptacao </code>.
     */
    private TipoMidiaCaptacaoVO tipoMidiaCaptacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
     */
    private PlanoFinanceiroAlunoVO planoFinanceiroAluno;
    // private CondicaoPagamentoPlanoFinanceiroCursoVO
    // condicaoPagamentoPlanoFinanceiroCursoVO;
    private Boolean liberarPagamentoMatricula;
    private Boolean liberacaoPendente;
    private Boolean matriculaIndeferida;
    private Boolean telaMapaPendencia;
    private Boolean descontoValorCheio;
    private List listaPendenciaFinanceira;
    private List listaOpcaoPagamento;
    private PerfilEconomicoVO perfilEconomicoVO;
    private Double valorTotalDivida;
    private TransferenciaEntradaVO transferenciaEntrada;
    private String guiaAba;
    private Double valorMatricula;
    private String formaIngresso;
    private Integer classificacaoIngresso;
    private String programaReservaVaga;
    private String financiamentoEstudantil;
    private String apoioSocial;
    private String atividadeComplementar;
    private String localProcessoSeletivo;
    private String anoIngresso;
    private String semestreIngresso;
    private String mesIngresso;
    private String anoConclusao;
    private String semestreConclusao;
    private String disciplinasProcSeletivo;
    private Double totalPontoProcSeletivo;
    private String tituloMonografia;
    private Double notaMonografia;
    private String tipoMatricula;
    private Boolean adesivoInstituicaoEntregue;
    private String codigoFinanceiroMatricula;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>EnadeVO </code>.
     */
    private EnadeVO enadeVO;
    private Boolean fezEnade;
    private Date dataEnade;
    private Double notaEnade;
    private Double horasComplementares;
    private String observacaoComplementar;
    private Boolean alterarFormaIngresso;
    private FormacaoAcademicaVO formacaoAcademica;
    private AutorizacaoCursoVO autorizacaoCurso;
    private FuncionarioVO consultor;
    private Boolean matriculaSerasa = false;
    //Atributo Utilizado para controle de matricula bolsista, considera ultima matricula periodo
    private Boolean alunoBolsista;
    //Atributos Utilizados para controle de suspensão de matrícula
    private Boolean matriculaSuspensa;
    private Date dataBaseSuspensao;
    private Date dataEnvioNotificacao1;
    private Date dataEnvioNotificacao2;
    private Date dataEnvioNotificacao3;
    private Date dataEnvioNotificacao4;
    private Date dataEnvioNotificacaoPendenciaDocumento;
    private Date dataAtualizacaoMatriculaFormada;
    private UsuarioVO responsavelAtualizacaoMatriculaFormada;
    private Date dataLiberacaoPendenciaFinanceira;
    private UsuarioVO responsavelLiberacaoPendenciaFinanceira;
    private Integer qtdDisciplinasExtensao;
    private Date dataColacaoGrau;
    @ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
    private List<MatriculaEnadeVO> matriculaEnadeVOs;
    private Boolean alunoSelecionado;
    private Boolean matriculaVerificadaSerasa;
    private Integer qtdDiasAdiarBloqueio;
    private Date dataAlteracaoMatriculaSerasa;
    private Date dataProcessoSeletivo;
    private Boolean naoApresentarCenso;
    private String semestreAnoIngressoCenso;
    /**
     * ATRIBUTO TRANSIENT UTILIZADO A PARTIR DA VERSAO 5.0
     * NESTE OBJETO, QUANDO MONTADO, EXISTEM TODAS AS INFORMACOES
     * IMPORTANTES SOBRE A VIDA ACADEMICA DO ALUNO.
     */
    private MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO;
    private String tipoTrabalhoConclusaoCurso;
    private Double notaEnem;
    private String observacaoDiploma;
    private String orientadorMonografia;
    private String titulacaoOrientadorMonografia;
    private Integer cargaHorariaMonografia;
	private Boolean matriculaOnlineProcSeletivo;
    public static final long serialVersionUID = 1L;
    /**
     * Atributo Transient
     */
    private String msgErro;
    private Boolean canceladoFinanceiro;
    /**
     * Atributo Transient
     * Criado para controle em Atualização de vencimento
     */
    private List<ContaReceberVO> listaContaReceberVOs;
    // Booleano para controle de matriculas bloqueadas o acesso
    private Boolean matriculaBloqueada;
	// String para controle de matriculas bloqueadas o acesso
    private String motivoMatriculaBloqueada;
    
    private UsuarioVO responsavelSuspensaoMatricula;
    private UsuarioVO responsavelCancelamentoSuspensaoMatricula;
    private Date dataCancelamentoSuspensaoMatricula;
    private String motivoCancelamentoSuspensaoMatricula;
    private UsuarioVO responsavelAdiamentoSuspensaoMatricula;
    private Date dataAdiamentoSuspensaoMatricula;
    private String motivoAdiamentoSuspensaoMatricula;
    
    private AutorizacaoCursoVO renovacaoReconhecimentoVO;
    
    //Atributo usado para a ficha do aluno
    @ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
    private List<RequerimentoVO> listaRequerimentoVOs;
    @ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
    private List<ItemEmprestimoVO> listaItemEmprestimoVOs;
    @ExcluirJsonAnnotation
    @Expose(deserialize = false, serialize = false)
    private List<InteracaoWorkflowVO> listaInteracaoWorkFlowVOs;
    
    //Data de emissão de histórico
    private Date dataEmissaoHistorico;
    /*
     * Campos criados apenas para carregar dados, não sao persistidos.
     * */
    private Date dataPagamento;
    private String situacaoParcela;
    
    /**
     * Campo transient usado para regra de alteracoes Cadastrais da matricula 
     */
    private Date dataBaseGeracaoParcelas;
    
    private Boolean permiteExecucaoReajustePreco;
    private String motivoCancelamentoReajustePreco;
    private Date dataCancelamentoReajustePreco;
    private UsuarioVO responsavelCancelamentoReajustePreco;
    
    /**
     * Campos transients usado para uso no relatório de ConsultorPorMatricula
     */
    
    private String condicaoPagamentoPlanoFinanceiroCurso;
    private String planoFinanceiroCurso;
    private Date dataMatriculaPeriodo;
    private Boolean matriculaConfirmada;
    private Date dataVencimentoMatricula;
    private Date dataPagamentoMatricula;
    private Boolean alunoInadimplente;
    private String categoriacondicaopagamento;
    private Double descontoMensalidade;
    private Double descontoMatricula;
    private Double valorParcela;
    
    
    
    /**
     * Campos transients usado para uso no relatório de MapaConvocacaoEnade
     */
    private String nomeMae;
    private String turma;
    private String situacaoMatriculaPeriodo;
    
    /**
     * Campo transiente
     */
	private List<PendenciaLiberacaoMatriculaVO> pendenciaLiberacaoMatriculaVOs;
	
	private Boolean bloqueioPorSolicitacaoLiberacaoMatricula;
	
    /**
     * Campos transientes para usar no mapa de suspensao da matrícula
     */
	private Boolean bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica;
	private Boolean bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira;
	private Date dataBaseGeracaoParcelasOriginal;
	
    /**
     * Campos transientes para usar na validação se o aluno assinou o contrato
     */
	private Boolean alunoNaoAssinouContratoMatricula;
	private String mensagemAlertaAlunoNaoAssinouContratoMatricula;      
	
    private String proficienciaLinguaEstrangeira;
    private String situacaoProficienciaLinguaEstrangeira;
    
    /**
     * Campos transientes para usar na validção de matriculas não integralizada
     */
	private List<GradeDisciplinaVO> listaGradeDisciplinaObrigatorioPendente;
	private Integer horasDisciplinaOptativaExigida;
	private Integer horasDisciplinaOptativaCumprida;
	private Integer horasEstagioExigida;
	private Integer horasEstagioCumprida;
	private Integer horasAtividadeComplementarExigida;
	private Integer horasAtividadeComplementarCumprida;
	
	private boolean geracaoMatriculaAutomatica = false;
	private Integer numeroMatriculaIncrimental;
	private Integer codigoNumeroMatricula;
	private Boolean permitirInclusaoExclusaoDisciplinasRenovacao;
	
	//Atributo Transient só para controle
	@ExcluirJsonAnnotation
	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO;
	@ExcluirJsonAnnotation
	private SituacaoAtividadeRespostaEnum situacaoAtividadeRespostaQuestao;
	@ExcluirJsonAnnotation
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	@ExcluirJsonAnnotation
	private SalaAulaBlackboardVO salaAulaBlackboardTcc;
	
	private Boolean bolsasAuxilios;
	private Boolean autodeclaracaoPretoPardoIndigena;
	private Boolean normasMatricula;
	private Boolean escolaPublica;
	private MatriculaEnadeVO matriculaEnadeVO;
	
	 private DiaSemana diaSemanaAula;
	 private NomeTurnoCensoEnum turnoAula;
	 
	 
	 private String  registroAcademicoOuMatriculaApresentar;
	private ColacaoGrauVO colacaoGrauVO;
	private ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO;
	private String financiamentoEstudantilCenso;
	private List<String> listaFinanciamentoEstudantilVOs;
	private JustificativaCensoEnum justificativaCensoEnum;
	private TipoMobilidadeAcademicaEnum tipoMobilidadeAcademicaEnum;
	private String mobilidadeAcademicaNacionalIESDestino;
	private String mobilidadeAcademicaInternacionalPaisDestino;
	private String informacoesCensoRelativoAno;
	private Integer totalCargaHorariaCumprido;
	private Double percentualCumprido;
	 private Boolean ingressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas;
	 private Boolean tipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas;
	 
	 private Boolean leiDeCotas;
	 private Boolean pretoPardoEIndigena;
	 private Boolean renda;
	 private Boolean pessoaComDeficiencia;
	 private Boolean estudanteProcedenteDeEscolaPublica;
	 private Boolean quilombola;
	 private Boolean estudanteTransgeneroETravesti;
	 private Boolean estudanteInternacional;
	 private Boolean refugiadoApatridaOuPortadorDeVistoHumanitario;
	 private Boolean idoso;
	 private Boolean estudantePertencenteAPovosEComunidadesTradicionais;
	 private Boolean medalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento;
	 private Boolean outrosTiposDeReservaDeVagas;
	 private Integer codigoBloqueioMatriculaRenovacao;
	@ExcluirJsonAnnotation
	@JsonIgnore
	@Expose(deserialize = false, serialize = false)
	private MatriculaPeriodoVO matriculaPeriodoVO;
	
	/**
	 * Atributos transients
	 */
	private Boolean existeDiploma;
	private Boolean matriculaIntegralizada;
	
	private String nrMatriculaCancelada;
    
    /**
     * Construtor padrão da classe <code>Matricula</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public MatriculaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>MatriculaVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static void validarDados(MatriculaVO obj) throws ConsistirException {
        if ((obj.getAluno() == null) || (obj.getAluno().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo ALUNO (Matrícula) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE DE ENSINO (Matrícula) deve ser informado.");
        }
        if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CURSO (Matrícula) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Matrícula) deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Matrícula) deve ser informado.");
        }
        if ((obj.getUsuario() == null) || (obj.getUsuario().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL MATRÍCULA (Matrícula) deve ser informado.");
        }
        if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURNO (Matrícula) deve ser informado.");
        }
       
    }

    public static ProcessoMatriculaCalendarioVO validarPeriodoProcessoMatricula(List listaPeriodoMatricula) throws Exception {
        if (!listaPeriodoMatricula.isEmpty()) {
            ProcessoMatriculaCalendarioVO obj = (ProcessoMatriculaCalendarioVO) listaPeriodoMatricula.get(0);
            return obj;
        }
        throw new ConsistirException("O PROCESSO DE MATRÍCULA para este curso ainda não está aberto");
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setMatricula("");
        setData(new Date());
        setSituacao("AT");
        setSituacaoFinanceira("PF");
        setLiberarPagamentoMatricula(Boolean.TRUE);
        setMatriculaIndeferida(Boolean.FALSE);
        setTelaMapaPendencia(Boolean.FALSE);
        setDescontoValorCheio(Boolean.TRUE);
        setValorTotalDivida(new Double(0.0));
        setValorMatricula(new Double(0.0));
        setGuiaAba("Inicio");
        setFormaIngresso("");
        setProgramaReservaVaga("");
        setFinanciamentoEstudantil("");
        setApoioSocial("");
        setAtividadeComplementar("");
    }

    public UsuarioVO criarUsuario(PerfilAcessoVO perfilAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        UsuarioVO obj = new UsuarioVO();
        obj.setNome(getAluno().getNome());
        obj.setPessoa(getAluno());
        // obj.setSenha(getAluno().getDataNasc_Apresentar());        
        obj.setSenha(Uteis.isAtributoPreenchido(getAluno().getRegistroAcademico()) ? getAluno().getRegistroAcademico() : getMatricula());
        obj.setUsername(Uteis.isAtributoPreenchido(getAluno().getRegistroAcademico()) ? getAluno().getRegistroAcademico() : getMatricula());
        
        if (configuracaoGeralSistema.getGerarSenhaCpfAluno()) {
            obj.setSenha(Uteis.removerMascara(getAluno().getCPF()));     
        }
        obj.setTipoUsuario("AL");

        UsuarioPerfilAcessoVO usuarioPerfilAcesso = new UsuarioPerfilAcessoVO();
        usuarioPerfilAcesso.getUnidadeEnsinoVO().setCodigo(getUnidadeEnsino().getCodigo());
        usuarioPerfilAcesso.setPerfilAcesso(perfilAcesso);
        obj.adicionarObjUsuarioPerfilAcessoAPartirMatriculaVOs(usuarioPerfilAcesso);
        return obj;
    }

    public UsuarioVO alterarDadosUsuario(MatriculaVO matricula, UsuarioVO usuario, PerfilAcessoVO perfilAcesso) throws Exception {
        if (matricula.getAluno().getCandidato().booleanValue() || !usuario.getUsername().equals(matricula.getMatricula())) {
            // usuario.setSenha(getAluno().getDataNasc_Apresentar());
            usuario.setSenha(getMatricula());
            usuario.setTipoUsuario("AL");
            usuario.setUsername(getMatricula());
            usuario.setValidaAlteracaoSenha(true);
        }
        UsuarioPerfilAcessoVO usuarioPerfilAcesso = new UsuarioPerfilAcessoVO();
        usuarioPerfilAcesso.getUnidadeEnsinoVO().setCodigo(getUnidadeEnsino().getCodigo());
        usuarioPerfilAcesso.setPerfilAcesso(perfilAcesso);
        usuario.adicionarObjUsuarioPerfilAcessoAPartirMatriculaVOs(usuarioPerfilAcesso);
        return usuario;
    }

    public MatriculaDWVO criarMatriculaDW(Integer processoMatricula, Integer peso) {
        MatriculaDWVO obj = new MatriculaDWVO();
        obj.setAno(Uteis.getAnoData(getData()));
        obj.setMes(Uteis.getMesData(getData()));
        obj.getCurso().setCodigo(getCurso().getCodigo());
        obj.getAreaConhecimento().setCodigo(getCurso().getAreaConhecimento().getCodigo());
        obj.setData(getData());
        obj.setNivelEducacional(obj.getCurso().getNivelEducacional());
        obj.getProcessoMatricula().setCodigo(processoMatricula);
        obj.setSituacao(getSituacao());
        obj.setPeso(peso);
        obj.getTurno().setCodigo(getTurno().getCodigo());
        obj.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
        return obj;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>MatriculaPeriodoVO</code> ao List <code>matriculaPeriodoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>MatriculaPeriodoVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjMatriculaPeriodoVOs(MatriculaPeriodoVO obj) throws Exception {
        MatriculaPeriodoVO.validarDados(obj);
        if (!this.getMatricula().equals("")) {
            obj.setMatricula(this.getMatricula());
        }
        int index = 0;
        Iterator i = getMatriculaPeriodoVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoVO objExistente = (MatriculaPeriodoVO) i.next();
            if (objExistente.getGradeCurricular().getCodigo().equals(obj.getGradeCurricular().getCodigo())) {
                getMatriculaPeriodoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getMatriculaPeriodoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>MatriculaPeriodoVO</code> no List <code>matriculaPeriodoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param periodoLetivoMatricula
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjMatriculaPeriodoVOs(Integer periodoLetivoMatricula) throws Exception {
        int index = 0;
        Iterator i = getMatriculaPeriodoVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoVO objExistente = (MatriculaPeriodoVO) i.next();
            if (objExistente.getGradeCurricular().getCodigo().equals(periodoLetivoMatricula)) {
                getMatriculaPeriodoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    public MatriculaPeriodoVO consultarObjMatriculaPeriodoVOPorCodigo(Integer codigoMatriculaPeriodo) throws Exception {
        Iterator i = getMatriculaPeriodoVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoVO objExistente = (MatriculaPeriodoVO) i.next();
            if (objExistente.getCodigo().equals(codigoMatriculaPeriodo)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>MatriculaPeriodoVO</code> no List <code>matriculaPeriodoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param periodoLetivoMatricula
     *            Parâmetro para localizar o objeto do List.
     */
    public MatriculaPeriodoVO consultarObjMatriculaPeriodoVO(Integer periodoLetivoMatricula) throws Exception {
        Iterator i = getMatriculaPeriodoVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoVO objExistente = (MatriculaPeriodoVO) i.next();
            if (objExistente.getGradeCurricular().getCodigo().equals(periodoLetivoMatricula)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>DocumetacaoMatriculaVO</code> ao List <code>documetacaoMatriculaVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>DocumetacaoMatricula</code> - getTipoDocumento() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>DocumetacaoMatriculaVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjDocumetacaoMatriculaVOs(DocumetacaoMatriculaVO obj) throws Exception {
        DocumetacaoMatriculaVO.validarDados(obj);
        int index = 0;
        Iterator i = getDocumetacaoMatriculaVOs().iterator();
        while (i.hasNext()) {
            DocumetacaoMatriculaVO objExistente = (DocumetacaoMatriculaVO) i.next();
            if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(obj.getTipoDeDocumentoVO().getCodigo())
            		&& objExistente.getAno().equals(obj.getAno())
            		&& objExistente.getSemestre().equals(obj.getSemestre())) {
            	if(!objExistente.getEntregue()) {
            		getDocumetacaoMatriculaVOs().set(index, obj);
            	}
                return;
            }
            index++;
        }
        getDocumetacaoMatriculaVOs().add(obj);
        // adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>DocumetacaoMatriculaVO</code> no List <code>documetacaoMatriculaVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>DocumetacaoMatricula</code> - getTipoDocumento() - como identificador (key) do objeto no List.
     *
     * @param tipoDocumento
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjDocumetacaoMatriculaVOs(Integer tipoDeDocumento) throws Exception {
        int index = 0;
        Iterator i = getDocumetacaoMatriculaVOs().iterator();
        while (i.hasNext()) {
            DocumetacaoMatriculaVO objExistente = (DocumetacaoMatriculaVO) i.next();
            if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(tipoDeDocumento)) {
                getDocumetacaoMatriculaVOs().remove(index);
                return;
            }
            index++;
        }
        // excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>DocumetacaoMatriculaVO</code> no List <code>documetacaoMatriculaVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>DocumetacaoMatricula</code> - getTipoDocumento() - como identificador (key) do objeto no List.
     *
     * @param tipoDocumento
     *            Parâmetro para localizar o objeto do List.
     */
    public DocumetacaoMatriculaVO consultarObjDocumetacaoMatriculaVO(Integer tipoDeDocumento) throws Exception {
        Iterator i = getDocumetacaoMatriculaVOs().iterator();
        while (i.hasNext()) {
            DocumetacaoMatriculaVO objExistente = (DocumetacaoMatriculaVO) i.next();
            if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(tipoDeDocumento)) {
                return objExistente;
            }
        }
        return null;
        // consultarObjSubordinadoOC
    }

    public void gerarOpcoesPagamento(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        calcularValorTotalDivida(configuracaoFinanceiroVO, usuario);
        montarOpcaoPagamentoDivida(configuracaoFinanceiroVO);
    }

    public void montarOpcaoPagamentoDivida(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        setListaOpcaoPagamento(new ArrayList());
        Iterator i = getPerfilEconomicoVO().getPerfilEconomicoCondicaoNegociacaoVOs().iterator();
        while (i.hasNext()) {
            PerfilEconomicoCondicaoNegociacaoVO obj = (PerfilEconomicoCondicaoNegociacaoVO) i.next();
            if ((obj.getCondicaoNegociacao().getValorMinimoValido().doubleValue() > 0) && (obj.getCondicaoNegociacao().getValorMinimoValido().doubleValue() > getValorTotalDivida().doubleValue())
                    && (obj.getCondicaoNegociacao().getValorMaximoValido().doubleValue() > 0)
                    && (obj.getCondicaoNegociacao().getValorMaximoValido().doubleValue() < getValorTotalDivida().doubleValue())) {
                break;
            }
            Double valor = Uteis.arrendondarForcando2CadasDecimais(getValorTotalDivida() + (getValorTotalDivida() * (obj.getCondicaoNegociacao().getJuro() / 100)));
            Double desconto = Uteis.arrendondarForcando2CadasDecimais(valor * (obj.getCondicaoNegociacao().getDesconto() / 100));
            OpcaoPagamentoDividaVO opcaoPagamentoDividaVO = new OpcaoPagamentoDividaVO();
            opcaoPagamentoDividaVO.montarListaCondicaoPagamento(valor, desconto, obj.getCondicaoNegociacao().getCondicaoPagamento().getParcelaCondicaoPagamentoVOs(), getUnidadeEnsino(),
                    getMatricula(), getAluno().getCodigo(), "AL", configuracaoFinanceiroVO);
            getListaOpcaoPagamento().add(opcaoPagamentoDividaVO);
        }
    }

    public Boolean getExisteOpcaoPagamento() {
        if (getListaPendenciaFinanceira().isEmpty()) {
            return false;
        }
        return true;
    }

    public String getDataIngressoValidaCenso(Date dataPadrao) {
        if (getData().before(getAluno().getDataNasc())) {
            return Uteis.getData(dataPadrao, "ddMMyyyy");
        }
        return Uteis.getData(getData(), "ddMMyyyy", dataPadrao);
    }

    public void calcularValorTotalDivida(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        Double valor = 0.0;
        Iterator i = getListaPendenciaFinanceira().iterator();
        while (i.hasNext()) {
            ContaReceberVO obj = (ContaReceberVO) i.next();
            valor = valor + obj.getCalcularValorFinal(configuracaoFinanceiroVO, usuario);
        }
        setValorTotalDivida(Uteis.arrendondarForcando2CadasDecimais(valor));
    }

    public PlanoFinanceiroAlunoVO getPlanoFinanceiroAluno() {
        if (planoFinanceiroAluno == null) {
            planoFinanceiroAluno = new PlanoFinanceiroAlunoVO();
        }
        return planoFinanceiroAluno;
    }

    public void setPlanoFinanceiroAluno(PlanoFinanceiroAlunoVO planoFinanceiroAluno) {
        this.planoFinanceiroAluno = planoFinanceiroAluno;
    }

    /**
     * Retorna o objeto da classe <code>TipoMidiaCaptacao</code> relacionado com (<code>Matricula</code>).
     */
    public TipoMidiaCaptacaoVO getTipoMidiaCaptacao() {
        if (tipoMidiaCaptacao == null) {
            tipoMidiaCaptacao = new TipoMidiaCaptacaoVO();
        }
        return (tipoMidiaCaptacao);
    }

    /**
     * Define o objeto da classe <code>TipoMidiaCaptacao</code> relacionado com (<code>Matricula</code>).
     */
    public void setTipoMidiaCaptacao(TipoMidiaCaptacaoVO obj) {
        this.tipoMidiaCaptacao = obj;
    }

    public Boolean getLiberarPagamentoMatricula() {
        return liberarPagamentoMatricula;
    }

    public void setLiberarPagamentoMatricula(Boolean liberarPagamentoMatricula) {
        this.liberarPagamentoMatricula = liberarPagamentoMatricula;
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com ( <code>Matricula</code>).
     */
    
    @XmlElement(name = "turno")
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com ( <code>Matricula</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    public UsuarioVO getUsuario() {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        return usuario;
    }

    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    /**
     * Retorna o objeto da classe <code>Inscricao</code> relacionado com ( <code>Matricula</code>).
     */
    public InscricaoVO getInscricao() {
        if (inscricao == null) {
            inscricao = new InscricaoVO();
        }
        return (inscricao);
    }

    /**
     * Define o objeto da classe <code>Inscricao</code> relacionado com ( <code>Matricula</code>).
     */
    public void setInscricao(InscricaoVO obj) {
        this.inscricao = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com ( <code>Matricula</code>).
     */
    @XmlElement(name = "curso")
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com ( <code>Matricula</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com ( <code>Matricula</code>).
     */
    @XmlElement(name = "unidadeEnsino")
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com ( <code>Matricula</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>Matricula</code>).
     */
    @XmlElement(name = "aluno")
    public PessoaVO getAluno() {
        if (aluno == null) {
            aluno = new PessoaVO();
        }
        return (aluno);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>Matricula</code>).
     */
    public void setAluno(PessoaVO obj) {
        this.aluno = obj;
    }

    public MatriculaPeriodoVO getMatriculaPeriodoVOAtiva() {
        for (MatriculaPeriodoVO matPeriodo : this.getMatriculaPeriodoVOs()) {
            if (matPeriodo.getSituacaoMatriculaPeriodo().equals("AT")) {
                return matPeriodo;
            }
        }
        return null;
    }

    public MatriculaPeriodoVO getUltimoMatriculaPeriodoVO() {
        Ordenacao.ordenarLista(this.getMatriculaPeriodoVOs(), "ordenacao");
        int cont = this.getMatriculaPeriodoVOs().size();
        if (cont > 0) {
            return (MatriculaPeriodoVO) this.getMatriculaPeriodoVOs().get(cont - 1);
        } else {
            return new MatriculaPeriodoVO();
        }
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe <code>MatriculaPeriodo</code>.
     */
    public List<MatriculaPeriodoVO> getMatriculaPeriodoVOs() {
        if (matriculaPeriodoVOs == null) {
            matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
        }
        return (matriculaPeriodoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe <code>MatriculaPeriodo</code>.
     */
    public void setMatriculaPeriodoVOs(List matriculaPeriodoVOs) {
        this.matriculaPeriodoVOs = matriculaPeriodoVOs;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe <code>DocumetacaoMatricula</code>.
     */
    public List<DocumetacaoMatriculaVO> getDocumetacaoMatriculaVOs() {
        if (documetacaoMatriculaVOs == null) {
            documetacaoMatriculaVOs = new ArrayList<DocumetacaoMatriculaVO>(0);
        }
        return (documetacaoMatriculaVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe <code>DocumetacaoMatricula</code>.
     */
    public void setDocumetacaoMatriculaVOs(List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs) {
        this.documetacaoMatriculaVOs = documetacaoMatriculaVOs;
    }

    public String getSituacaoFinanceira() {
        if (situacaoFinanceira == null) {
            situacaoFinanceira = "";
        }
        return (situacaoFinanceira);
    }

    public void setSituacaoFinanceira(String situacaoFinanceira) {
        this.situacaoFinanceira = situacaoFinanceira;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do atributo esta função é capaz de retornar o de
     * apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        try {
            if ((SituacaoVinculoMatricula.getEnum(situacao).equals(SituacaoVinculoMatricula.TRANCADA)) && (this.getAlunoAbandonouCurso())) {
                return SituacaoVinculoMatricula.getDescricao("AC");
            }
			if (situacao.equals(SituacaoVinculoMatricula.ATIVA.getValor()) && getCanceladoFinanceiro()) {
				return SituacaoVinculoMatricula.getDescricao(situacao) + " (Cancelado Financeiro)";
			}
            return SituacaoVinculoMatricula.getDescricao(situacao);
        } catch (Exception e) {
            return SituacaoVinculoMatricula.getDescricao("ER");
        }
    }

	public String validaMatriculaLiberadaFinanceiroCancelamentoTrancamento(Boolean trancamento) {
		if (!getCanceladoFinanceiro()) {
			if (trancamento) {
				return UteisJSF.internacionalizar("msg_Trancamento_matriculaPendenteFinanceira");
			}
			return UteisJSF.internacionalizar("msg_Cancelamento_matriculaPendenteFinanceira");
		}
		return "";
	}

    public Boolean getMatriculaCanceladaFinanceiramente() {
        if (this.getSituacao().equals("CF") || getCanceladoFinanceiro()) {
            return true;
        } else {
            return false;
        }
    }

    public int getSituacaoCenso() {
        return SituacaoVinculoMatricula.getEnum(situacao).getCodigoCenso();
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }
    
    /***
     * O método acima retorna o ano com 2 dígitos, esa função retorna ele com 4 digitos (SimplaDateFormat('dd/MM/yyyy')
     * @return
     */
    public String getDataAno4Digitos_Apresentar(){
    	return formatadorDataAno4Digitos.format(data);
    }

    public void setData(Date data) {
        this.data = data;
    }

    @XmlElement(name = "matricula")
    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return (matricula);
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do atributo esta função é capaz de retornar o de
     * apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    public String getSituacaoFinanceira_Apresentar() {
        if (getSituacaoFinanceira().equals("PF")) {
            return "Pendente Financeiramente";
        }
        if (getSituacaoFinanceira().equals("QU")) {
            return "Matrícula Paga";
        }
        return (getSituacaoFinanceira());
    }

    public boolean getMatriculaJaRegistrada() {
        if (!this.getMatricula().equals("") && !getNovoObj()) {
            return true;
        }
        return false;
    }

    public List getListaOpcaoPagamento() {
        if (listaOpcaoPagamento == null) {
            listaOpcaoPagamento = new ArrayList();
        }
        return listaOpcaoPagamento;
    }

    public void setListaOpcaoPagamento(List listaOpcaoPagamento) {
        this.listaOpcaoPagamento = listaOpcaoPagamento;
    }

    public List getListaPendenciaFinanceira() {
        if (listaPendenciaFinanceira == null) {
            listaPendenciaFinanceira = new ArrayList();
        }
        return listaPendenciaFinanceira;
    }

    public void setListaPendenciaFinanceira(List listaPendenciaFinanceira) {
        this.listaPendenciaFinanceira = listaPendenciaFinanceira;
    }

    public PerfilEconomicoVO getPerfilEconomicoVO() {
        if (perfilEconomicoVO == null) {
            perfilEconomicoVO = new PerfilEconomicoVO();
        }
        return perfilEconomicoVO;
    }

    public void setPerfilEconomicoVO(PerfilEconomicoVO perfilEconomicoVO) {
        this.perfilEconomicoVO = perfilEconomicoVO;
    }

    public Boolean getExistePendenciaFinanceira() {
        if (getListaPendenciaFinanceira().isEmpty()) {
            return false;
        }
        return true;
    }

    public TransferenciaEntradaVO getTransferenciaEntrada() {
        if (transferenciaEntrada == null) {
            transferenciaEntrada = new TransferenciaEntradaVO();
        }
        return transferenciaEntrada;
    }

    public void setTransferenciaEntrada(TransferenciaEntradaVO transferenciaEntrada) {
        this.transferenciaEntrada = transferenciaEntrada;
    }

    public Double getValorTotalDivida() {
        return valorTotalDivida;
    }

    public void setValorTotalDivida(Double valorTotalDivida) {
        this.valorTotalDivida = valorTotalDivida;
    }

    /**
     * @return the usuarioLiberacaoDesconto
     */
    public UsuarioVO getUsuarioLiberacaoDesconto() {
        if (usuarioLiberacaoDesconto == null) {
            usuarioLiberacaoDesconto = new UsuarioVO();
        }
        return usuarioLiberacaoDesconto;
    }

    /**
     * @param usuarioLiberacaoDesconto
     *            the usuarioLiberacaoDesconto to set
     */
    public void setUsuarioLiberacaoDesconto(UsuarioVO usuarioLiberacaoDesconto) {
        this.usuarioLiberacaoDesconto = usuarioLiberacaoDesconto;
    }

    /**
     * @return the guiaAba
     */
    public String getGuiaAba() {
        return guiaAba;
    }

    /**
     * @param guiaAba
     *            the guiaAba to set
     */
    public void setGuiaAba(String guiaAba) {
        this.guiaAba = guiaAba;
    }

    /**
     * @return the valorMensalidade
     */
    public Double getValorMatricula() {
        return valorMatricula;
    }

    /**
     * @param valorMensalidade
     *            the valorMensalidade to set
     */
    public void setValorMatricula(Double valorMatricula) {
        this.valorMatricula = valorMatricula;
    }

    /**
     * @return the matriculaIndeferida
     */
    public Boolean getMatriculaIndeferida() {
        return matriculaIndeferida;
    }

    /**
     * @param matriculaIndeferida
     *            the matriculaIndeferida to set
     */
    public void setMatriculaIndeferida(Boolean matriculaIndeferida) {
        this.matriculaIndeferida = matriculaIndeferida;
    }

    /**
     * @return the telaMapaPendencia
     */
    public Boolean getTelaMapaPendencia() {
        return telaMapaPendencia;
    }

    /**
     * @param telaMapaPendencia
     *            the telaMapaPendencia to set
     */
    public void setTelaMapaPendencia(Boolean telaMapaPendencia) {
        this.telaMapaPendencia = telaMapaPendencia;
    }

    /**
     * @return the descontoValorCheio
     */
    public Boolean getDescontoValorCheio() {
        return descontoValorCheio;
    }

    /**
     * @param descontoValorCheio
     *            the descontoValorCheio to set
     */
    public void setDescontoValorCheio(Boolean descontoValorCheio) {
        this.descontoValorCheio = descontoValorCheio;
    }

    public String getFormaIngresso() {
        if (formaIngresso == null) {
            return "";
        }
        return formaIngresso;
    }

    public String getFormaIngresso_Apresentar() {
        return FormaIngresso.getDescricao(formaIngresso);
    }

    public String getFormaIngressoComAnoSemestre_Apresentar() {
        String complemento = "";
        if (!this.getSemestreIngresso().equals("")) {
            complemento = this.getSemestreIngresso().trim();
        }
        if (!this.getAnoIngresso().equals("")) {
            if (!complemento.equals("")) {
                complemento = complemento + "/";
            }
            complemento = complemento + this.getAnoIngresso().trim();
        }
        if (!complemento.equals("")) {
            complemento = " - " + complemento;
        }
 //        return FormaIngresso.getDescricao(formaIngresso) + complemento;
        return complemento;
   }

    public void setFormaIngresso(String formaIngresso) {
        this.formaIngresso = formaIngresso;
    }

    public String getProgramaReservaVaga() {
        return programaReservaVaga;
    }

    public void setProgramaReservaVaga(String programaReservaVaga) {
        this.programaReservaVaga = programaReservaVaga;
    }

    public String getFinanciamentoEstudantil() {
        if (financiamentoEstudantil == null) {
            financiamentoEstudantil = "";
        }
        return financiamentoEstudantil;
    }

    public void setFinanciamentoEstudantil(String financiamentoEstudantil) {
        this.financiamentoEstudantil = financiamentoEstudantil;
    }

    public String getApoioSocial() {
        return apoioSocial;
    }

    public void setApoioSocial(String apoioSocial) {
        this.apoioSocial = apoioSocial;
    }

    public String getAtividadeComplementar() {
        return atividadeComplementar;
    }

    public void setAtividadeComplementar(String atividadeComplementar) {
        this.atividadeComplementar = atividadeComplementar;
    }

    public Boolean getExisteNrMatriculaGerado() {
        if ((this.matricula == null) || (this.matricula.equals(""))) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean getMatriculaJaPaga() {
        MatriculaPeriodoVO matAtiva = this.getMatriculaPeriodoVOAtiva();
        if ((!this.getMatriculaJaRegistrada()) || (matAtiva == null)) {
            return false;
        }
        if ((matAtiva.getMatriculaPeriodoVencimentoReferenteMatricula() != null)
                && (matAtiva.getMatriculaPeriodoVencimentoReferenteMatricula().getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA))) {
            return true;
        }
        return false;
    }

    public String getAnoIngresso() {
        if (anoIngresso == null) {
            anoIngresso = "";
        }
        return anoIngresso;
    }

    public void setAnoIngresso(String anoIngresso) {
        this.anoIngresso = anoIngresso;
    }

    public String getSemestreIngresso() {
        if (semestreIngresso == null) {
            semestreIngresso = "";
        }
        return semestreIngresso.trim();
    }

    public void setSemestreIngresso(String semestreIngresso) {
        this.semestreIngresso = semestreIngresso;
    }

    public String getAnoConclusao() {
        if (anoConclusao == null) {
            anoConclusao = "";
        }
        return anoConclusao;
    }

    public void setAnoConclusao(String anoConclusao) {
        this.anoConclusao = anoConclusao;
    }

    public String getSemestreConclusao() {
        if (semestreConclusao == null) {
            semestreConclusao = "";
        }
        return semestreConclusao;
    }

    public void setSemestreConclusao(String semestreConclusao) {
        this.semestreConclusao = semestreConclusao;
    }

    public void atualizarSituacaoFinanceira() {
//        if (this.getMatriculaJaPaga()) {
            this.setSituacaoFinanceira("QU");
//        }
    }

    /**
     * Atributo criado para dispensar a criação de processos seletivos durante implantação.
     *
     * @return disciplinasProcSeletivo
     */
    public String getDisciplinasProcSeletivo() {
//		if (Uteis.isAtributoPreenchido(disciplinasProcSeletivo)) {
        if (disciplinasProcSeletivo == null) {
            disciplinasProcSeletivo = "";
        }
        return disciplinasProcSeletivo;
//		}
//                else {
//			if (Uteis.isAtributoPreenchido(getInscricao()) && Uteis.isAtributoPreenchido(getInscricao().getProcSeletivo())) {
//				return getInscricao().getProcSeletivo().getDisciplinasProcSeletivoString();
//			} else {
//				return "";
//			}
//		}
    }

    public void setDisciplinasProcSeletivo(String disciplinasProcSeletivo) {
        this.disciplinasProcSeletivo = disciplinasProcSeletivo;
    }

    // rendered="#{((MatriculaDiretaControle.matriculaVO.matricula=='') || (MatriculaDiretaControle.matriculaVO.situacao=='PL') || (MatriculaDiretaControle.matriculaVO.situacao=='ID')) && (MatriculaDiretaControle.matriculaPeriodoVO.nrDocumento=='')}"
    public Boolean getIsAtiva() {
        if (this.getSituacao().equals("AT") || this.getSituacao().equals("PR")) {
            return true;
        }
        return false;
    }

    public Boolean getIsPreMatricula() {
        if (this.getSituacao().equals("PR")) {
            return true;
        }
        return false;
    }
    public Boolean getIsTrancada() {
    	return this.getSituacao().equals("TR");
    }

    public EnadeVO getEnadeVO() {
        if (enadeVO == null) {
            enadeVO = new EnadeVO();
        }
        return enadeVO;
    }

    public void setEnadeVO(EnadeVO enadeVO) {
        this.enadeVO = enadeVO;
    }

    public Boolean getFezEnade() {
        if (fezEnade == null) {
            fezEnade = false;
        }
        return fezEnade;
    }

    public void setFezEnade(Boolean fezEnade) {
        this.fezEnade = fezEnade;
    }

    public Date getDataEnade() {
        return dataEnade;
    }

    public void setDataEnade(Date dataEnade) {
        this.dataEnade = dataEnade;
    }

    public Double getNotaEnade() {
        return notaEnade;
    }

    public void setNotaEnade(Double notaEnade) {
        this.notaEnade = notaEnade;
    }

    public Double getHorasComplementares() {
        return horasComplementares;
    }

    public void setHorasComplementares(Double horasComplementares) {
        this.horasComplementares = horasComplementares;
    }

    public boolean getIsRenovacaoMatriculaSequencial() {
        if (getCurso().getConfiguracaoAcademico().getRenovacaoMatriculaSequencial()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getIsUnidadeEnsinoCursoSelecionado() {
        return getUnidadeEnsino().getCodigo().intValue() != 0 && getCurso().getCodigo().intValue() != 0;
    }

    public String getTituloMonografia() {
    	if(tituloMonografia == null) {
    		tituloMonografia = "";
    	}
        return tituloMonografia;
    }

    public void setTituloMonografia(String tituloMonografia) {
        this.tituloMonografia = tituloMonografia;
    }

    /**
     * @return the alunoAbandonouOCurso
     */
    public Boolean getAlunoAbandonouCurso() {
        if (alunoAbandonouCurso == null) {
            alunoAbandonouCurso = false;
        }
        return alunoAbandonouCurso;
    }

    /**
     * @param alunoAbandonouOCurso
     *            the alunoAbandonouOCurso to set
     */
    public void setAlunoAbandonouCurso(Boolean alunoAbandonouOCurso) {
        this.alunoAbandonouCurso = alunoAbandonouOCurso;
    }

    /**
     * @return the observacaoComplementar
     */
    public String getObservacaoComplementar() {
        if (observacaoComplementar == null) {
            observacaoComplementar = "";
        }
        return observacaoComplementar;
    }

    /**
     * @param observacaoComplementar
     *            the observacaoComplementar to set
     */
    public void setObservacaoComplementar(String observacaoComplementar) {
        this.observacaoComplementar = observacaoComplementar;
    }

    /**
     * @return the dataInicioCurso
     */
    public Date getDataInicioCurso() {
        return dataInicioCurso;
    }

    /**
     * @param dataInicioCurso
     *            the dataInicioCurso to set
     */
    public void setDataInicioCurso(Date dataInicioCurso) {       
    	this.dataInicioCurso = dataInicioCurso;
    }

    /**
     * @return the dataConclusaoCurso
     */
    public Date getDataConclusaoCurso() {
        return dataConclusaoCurso;
    }

    /**
     * @param dataConclusaoCurso
     *            the dataConclusaoCurso to set
     */
    public void setDataConclusaoCurso(Date dataConclusaoCurso) {
        this.dataConclusaoCurso = dataConclusaoCurso;
    }

    /**
     * @return the localArmazenamentoDocumentosMatricula
     */
    public String getLocalArmazenamentoDocumentosMatricula() {
        if (localArmazenamentoDocumentosMatricula == null) {
            localArmazenamentoDocumentosMatricula = "";
        }
        return localArmazenamentoDocumentosMatricula;
    }

    /**
     * @param localArmazenamentoDocumentosMatricula
     *            the localArmazenamentoDocumentosMatricula to set
     */
    public void setLocalArmazenamentoDocumentosMatricula(String localArmazenamentoDocumentosMatricula) {
        this.localArmazenamentoDocumentosMatricula = localArmazenamentoDocumentosMatricula;
    }

    public String getApresentarUltimoPeriodoAluno() {
        int varTempSemestre = 0;
        int varTempAno = 0;
        String descricaoPeriodo = "";
        for (MatriculaPeriodoVO matriculaPeriodo : getMatriculaPeriodoVOs()) {
            int semestre = 0;
            int ano = 0;
            try {
                if (matriculaPeriodo.getAno() != null) {
                    ano = Integer.parseInt(matriculaPeriodo.getAno());
                }
                if (matriculaPeriodo.getSemestre() != null) {
                    semestre = Integer.parseInt(matriculaPeriodo.getSemestre());
                }
            } catch (NumberFormatException e) {
               // //System.out.println("MatriculaVO Erro:" + e.getMessage());
            }
            if (ano == varTempAno) {
                if (semestre > varTempSemestre) {
                    varTempSemestre = semestre;
                    varTempAno = ano;
                    descricaoPeriodo = matriculaPeriodo.getPeridoLetivo().getDescricao();
                }
            } else if (ano > varTempAno) {
                varTempSemestre = semestre;
                varTempAno = ano;
                descricaoPeriodo = matriculaPeriodo.getPeridoLetivo().getDescricao();
            }

        }
        return descricaoPeriodo;

    }

    /**
     * @return the alterarFormaIngresso
     */
    public Boolean getAlterarFormaIngresso() {
        if (alterarFormaIngresso == null) {
            alterarFormaIngresso = Boolean.FALSE;
        }
        return alterarFormaIngresso;
    }

    /**
     * @param alterarFormaIngresso the alterarFormaIngresso to set
     */
    public void setAlterarFormaIngresso(Boolean alterarFormaIngresso) {
        this.alterarFormaIngresso = alterarFormaIngresso;
    }

    public void setNotaMonografia(Double notaMonografia) {
        this.notaMonografia = notaMonografia;
    }

    public Double getNotaMonografia() {
        return notaMonografia;
    }

    public Boolean getDescontoChancela() {
    	if (descontoChancela == null) {
    		descontoChancela = false;
    	}
        return descontoChancela;
    }

    public void setDescontoChancela(Boolean descontoChancela) {
        this.descontoChancela = descontoChancela;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatriculaVO other = (MatriculaVO) obj;
        if ((this.matricula == null) ? (other.matricula != null) : !this.matricula.equals(other.matricula)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.matricula != null ? this.matricula.hashCode() : 0);
        return hash;
    }

    /**
     * @return the formacaoAcademica
     */
    public FormacaoAcademicaVO getFormacaoAcademica() {
        if (formacaoAcademica == null) {
            formacaoAcademica = new FormacaoAcademicaVO();
        }
        return formacaoAcademica;
    }

    /**
     * @param formacaoAcademica the formacaoAcademica to set
     */
    public void setFormacaoAcademica(FormacaoAcademicaVO formacaoAcademica) {
        this.formacaoAcademica = formacaoAcademica;
    }

    /**
     * @return the autorizacaoCurso
     */
    public AutorizacaoCursoVO getAutorizacaoCurso() {
        if (autorizacaoCurso == null) {
            autorizacaoCurso = new AutorizacaoCursoVO();
        }
        return autorizacaoCurso;
    }

    /**
     * @param autorizacaoCurso the autorizacaoCurso to set
     */
    public void setAutorizacaoCurso(AutorizacaoCursoVO autorizacaoCurso) {
        this.autorizacaoCurso = autorizacaoCurso;
    }

    public String getSituacaoMatriculaAcademicaFinanceira() {
        if (getSituacao().equals("AT") && getSituacaoFinanceira().equals("PF")) {
            return getSituacaoFinanceira_Apresentar();
        }
        return getSituacao_Apresentar();
    }

    /**
     * @return the consultor
     */
    public FuncionarioVO getConsultor() {
        if (consultor == null) {
            consultor = new FuncionarioVO();
        }
        return consultor;
    }

    /**
     * @param consultor the consultor to set
     */
    public void setConsultor(FuncionarioVO consultor) {
        this.consultor = consultor;
    }

    public String getTipoMatricula() {
        if (tipoMatricula == null) {
            tipoMatricula = "NO";
        }
        return tipoMatricula;
    }

    public String getTipoMatricula_Apresentar() {
        if (getTipoMatricula().equals("EX")) {
            return "CONTRATO EXTENSÃO";
        } else if (getTipoMatricula().equals("CE")) {
            return "CONTRATO ESPECIAL";
        } else {
            return "CONTRATO NORMAL";
        }
    }

    public void setTipoMatricula(String tipoMatricula) {
        this.tipoMatricula = tipoMatricula;
    }

    /**
     * @return the matriculaSuspensa
     */
    public Boolean getMatriculaSuspensa() {
    	if (matriculaSuspensa == null) {
    		matriculaSuspensa = false;
    	}
        return matriculaSuspensa;
    }

    /**
     * @param matriculaSuspensa the matriculaSuspensa to set
     */
    public void setMatriculaSuspensa(Boolean matriculaSuspensa) {
        this.matriculaSuspensa = matriculaSuspensa;
    }

    /**
     * @return the dataBaseSuspensao
     */
    public Date getDataBaseSuspensao() {
    	if (dataBaseSuspensao == null) {
    		dataBaseSuspensao = new Date();
    	}
        return dataBaseSuspensao;
    }

    /**
     * @param dataBaseSuspensao the dataBaseSuspensao to set
     */
    public void setDataBaseSuspensao(Date dataBaseSuspensao) {
        this.dataBaseSuspensao = dataBaseSuspensao;
    }

    /**
     * @return the dataEnvioNotificacao1
     */
    public Date getDataEnvioNotificacao1() {
        return dataEnvioNotificacao1;
    }

    /**
     * @param dataEnvioNotificacao1 the dataEnvioNotificacao1 to set
     */
    public void setDataEnvioNotificacao1(Date dataEnvioNotificacao1) {
        this.dataEnvioNotificacao1 = dataEnvioNotificacao1;
    }

    /**
     * @return the dataEnvioNotificacao2
     */
    public Date getDataEnvioNotificacao2() {
        return dataEnvioNotificacao2;
    }

    /**
     * @param dataEnvioNotificacao2 the dataEnvioNotificacao2 to set
     */
    public void setDataEnvioNotificacao2(Date dataEnvioNotificacao2) {
        this.dataEnvioNotificacao2 = dataEnvioNotificacao2;
    }

    /**
     * @return the dataEnvioNotificacao3
     */
    public Date getDataEnvioNotificacao3() {
        return dataEnvioNotificacao3;
    }

    /**
     * @param dataEnvioNotificacao3 the dataEnvioNotificacao3 to set
     */
    public void setDataEnvioNotificacao3(Date dataEnvioNotificacao3) {
        this.dataEnvioNotificacao3 = dataEnvioNotificacao3;
    }

    /**
     * @return the matriculaSerasa
     */
    public Boolean getMatriculaSerasa() {
        return matriculaSerasa;
    }

    public String getMatriculaSerasa_Apresentar() {
        if (getMatriculaSerasa() == null) {
            return "NÃO";
        } else if (getMatriculaSerasa()) {
            return "SIM";
        } else {
            return "NÃO";
        }
    }

    /**
     * @param matriculaSerasa the matriculaSerasa to set
     */
    public void setMatriculaSerasa(Boolean matriculaSerasa) {
        this.matriculaSerasa = matriculaSerasa;
    }

    public Date getDataAtualizacaoMatriculaFormada() {
        if (dataAtualizacaoMatriculaFormada == null) {
            dataAtualizacaoMatriculaFormada = new Date();
        }
        return dataAtualizacaoMatriculaFormada;
    }

    public String getDataAtualizacaoMatriculaFormada_Apresentacao() {
    	return (Uteis.getData(dataAtualizacaoMatriculaFormada));
    }

    public void setDataAtualizacaoMatriculaFormada(Date dataAtualizacaoMatriculaFormada) {
        this.dataAtualizacaoMatriculaFormada = dataAtualizacaoMatriculaFormada;
    }

    public UsuarioVO getResponsavelAtualizacaoMatriculaFormada() {
        if (responsavelAtualizacaoMatriculaFormada == null) {
            responsavelAtualizacaoMatriculaFormada = new UsuarioVO();
        }
        return responsavelAtualizacaoMatriculaFormada;
    }

    public void setResponsavelAtualizacaoMatriculaFormada(UsuarioVO responsavelAtualizacaoMatriculaFormada) {
        this.responsavelAtualizacaoMatriculaFormada = responsavelAtualizacaoMatriculaFormada;
    }

    /**
     * @return the qtdDisciplinasExtensao
     */
    public Integer getQtdDisciplinasExtensao() {
        if (qtdDisciplinasExtensao == null) {
            qtdDisciplinasExtensao = 0;
        }
        return qtdDisciplinasExtensao;
    }

    /**
     * @param qtdDisciplinasExtensao the qtdDisciplinasExtensao to set
     */
    public void setQtdDisciplinasExtensao(Integer qtdDisciplinasExtensao) {
        this.qtdDisciplinasExtensao = qtdDisciplinasExtensao;
    }

    public Boolean getNaoEnviarMensagemCobranca() {
        if (naoEnviarMensagemCobranca == null) {
            naoEnviarMensagemCobranca = false;
        }
        return naoEnviarMensagemCobranca;
    }

    public void setNaoEnviarMensagemCobranca(Boolean naoEnviarMensagemCobranca) {
        this.naoEnviarMensagemCobranca = naoEnviarMensagemCobranca;
    }
    
    

    public Boolean getConsiderarParcelasMaterialDidaticoReajustePreco() {
    	if(considerarParcelasMaterialDidaticoReajustePreco == null) {
    		considerarParcelasMaterialDidaticoReajustePreco = true;
    	}
		return considerarParcelasMaterialDidaticoReajustePreco;
	}

	public void setConsiderarParcelasMaterialDidaticoReajustePreco(Boolean considerarParcelasMaterialDidaticoReajustePreco) {
		this.considerarParcelasMaterialDidaticoReajustePreco = considerarParcelasMaterialDidaticoReajustePreco;
	}

	/**
     * @return the alunoBolsista
     */
    public Boolean getAlunoBolsista() {
        if (alunoBolsista == null) {
            alunoBolsista = Boolean.FALSE;
        }
        return alunoBolsista;
    }

    public String getBolsista_Apresentar() {
        if (getAlunoBolsista()) {
            return "SIM";
        } else {
            return "NÃO";
        }
    }

    /**
     * @param alunoBolsista the alunoBolsista to set
     */
    public void setAlunoBolsista(Boolean alunoBolsista) {
        this.alunoBolsista = alunoBolsista;
    }

    /**
     * @return the dataLiberacaoPendenciaFinanceira
     */
    public Date getDataLiberacaoPendenciaFinanceira() {
        return dataLiberacaoPendenciaFinanceira;
    }

    /**
     * @param dataLiberacaoPendenciaFinanceira the dataLiberacaoPendenciaFinanceira to set
     */
    public void setDataLiberacaoPendenciaFinanceira(Date dataLiberacaoPendenciaFinanceira) {
        this.dataLiberacaoPendenciaFinanceira = dataLiberacaoPendenciaFinanceira;
    }

    /**
     * @return the responsavelLiberacaoPendenciaFinanceira
     */
    public UsuarioVO getResponsavelLiberacaoPendenciaFinanceira() {
        if (responsavelLiberacaoPendenciaFinanceira == null) {
            responsavelLiberacaoPendenciaFinanceira = new UsuarioVO();
        }
        return responsavelLiberacaoPendenciaFinanceira;
    }

    /**
     * @param responsavelLiberacaoPendenciaFinanceira the responsavelLiberacaoPendenciaFinanceira to set
     */
    public void setResponsavelLiberacaoPendenciaFinanceira(UsuarioVO responsavelLiberacaoPendenciaFinanceira) {
        this.responsavelLiberacaoPendenciaFinanceira = responsavelLiberacaoPendenciaFinanceira;
    }

    /**
     * @return the alunoConcluiuDisciplinasRegulares
     */
    public Boolean getAlunoConcluiuDisciplinasRegulares() {
        if (alunoConcluiuDisciplinasRegulares == null) {
            alunoConcluiuDisciplinasRegulares = false;
        }
        return alunoConcluiuDisciplinasRegulares;
    }

    /**
     * @param alunoConcluiuDisciplinasRegulares the alunoConcluiuDisciplinasRegulares to set
     */
    public void setAlunoConcluiuDisciplinasRegulares(Boolean alunoConcluiuDisciplinasRegulares) {
        this.alunoConcluiuDisciplinasRegulares = alunoConcluiuDisciplinasRegulares;
    }

    public Date getDataColacaoGrau() {
        return dataColacaoGrau;
    }

    public void setDataColacaoGrau(Date dataColacaoGrau) {
        this.dataColacaoGrau = dataColacaoGrau;
    }

    public String getDataColacaoGrau_Apresentar() {
        return (Uteis.getData(dataColacaoGrau));
    }

    public Date getDataEnvioNotificacaoPendenciaDocumento() {
        return dataEnvioNotificacaoPendenciaDocumento;
    }

    public void setDataEnvioNotificacaoPendenciaDocumento(Date dataEnvioNotificacaoPendenciaDocumento) {
        this.dataEnvioNotificacaoPendenciaDocumento = dataEnvioNotificacaoPendenciaDocumento;
    }

    public Date getDataAlunoConcluiuDisciplinasRegulares() {
        return dataAlunoConcluiuDisciplinasRegulares;
    }

    public void setDataAlunoConcluiuDisciplinasRegulares(Date dataAlunoConcluiuDisciplinasRegulares) {
        this.dataAlunoConcluiuDisciplinasRegulares = dataAlunoConcluiuDisciplinasRegulares;
    }

    public List<MatriculaEnadeVO> getMatriculaEnadeVOs() {
        if (matriculaEnadeVOs == null) {
            matriculaEnadeVOs = new ArrayList<MatriculaEnadeVO>(0);
        }
        return matriculaEnadeVOs;
    }

    public void setMatriculaEnadeVOs(List<MatriculaEnadeVO> matriculaEnadeVOs) {
        this.matriculaEnadeVOs = matriculaEnadeVOs;
    }

    public Boolean getAlunoSelecionado() {
        if (alunoSelecionado == null) {
            alunoSelecionado = Boolean.TRUE;
        }
        return alunoSelecionado;
    }

    public void setAlunoSelecionado(Boolean alunoSelecionado) {
        this.alunoSelecionado = alunoSelecionado;
    }

    /**
     * @return the qtdDiasAdiarBloqueio
     */
    public Integer getQtdDiasAdiarBloqueio() {
        if (qtdDiasAdiarBloqueio == null) {
            qtdDiasAdiarBloqueio = 0;
        }
        return qtdDiasAdiarBloqueio;
    }

    /**
     * @param qtdDiasAdiarBloqueio the qtdDiasAdiarBloqueio to set
     */
    public void setQtdDiasAdiarBloqueio(Integer qtdDiasAdiarBloqueio) {
        this.qtdDiasAdiarBloqueio = qtdDiasAdiarBloqueio;
    }

    public Boolean getAdesivoInstituicaoEntregue() {
        if (adesivoInstituicaoEntregue == null) {
            adesivoInstituicaoEntregue = Boolean.FALSE;
        }
        return adesivoInstituicaoEntregue;
    }

    public void setAdesivoInstituicaoEntregue(Boolean adesivoInstituicaoEntregue) {
        this.adesivoInstituicaoEntregue = adesivoInstituicaoEntregue;
    }

    public String getMesIngresso() {
        if (mesIngresso == null) {
            mesIngresso = "";
        }
        return mesIngresso;
    }

    public void setMesIngresso(String mesIngresso) {
        this.mesIngresso = mesIngresso;
    }

    public Boolean getMatriculaVerificadaSerasa() {
        if (matriculaVerificadaSerasa == null) {
            matriculaVerificadaSerasa = Boolean.FALSE;
        }
        return matriculaVerificadaSerasa;
    }

    public void setMatriculaVerificadaSerasa(Boolean matriculaVerificadaSerasa) {
        this.matriculaVerificadaSerasa = matriculaVerificadaSerasa;
    }

    public ObservacaoComplementarHistoricoAlunoVO getObservacaoComplementarHistoricoAlunoVO() {
        if (observacaoComplementarHistoricoAlunoVO == null) {
            observacaoComplementarHistoricoAlunoVO = new ObservacaoComplementarHistoricoAlunoVO();
        }
        return observacaoComplementarHistoricoAlunoVO;
    }

    public void setObservacaoComplementarHistoricoAlunoVO(ObservacaoComplementarHistoricoAlunoVO observacaoComplementarHistoricoAlunoVO) {
        this.observacaoComplementarHistoricoAlunoVO = observacaoComplementarHistoricoAlunoVO;
    }

    public Boolean getLiberarBloqueioAlunoInadimplente() {
        if (liberarBloqueioAlunoInadimplente == null) {
            liberarBloqueioAlunoInadimplente = Boolean.FALSE;
        }
        return liberarBloqueioAlunoInadimplente;
    }

    public void setLiberarBloqueioAlunoInadimplente(Boolean liberarBloqueioAlunoInadimplente) {
        this.liberarBloqueioAlunoInadimplente = liberarBloqueioAlunoInadimplente;
    }

    public Date getDataAlteracaoMatriculaSerasa() {
        if (dataAlteracaoMatriculaSerasa == null) {
            dataAlteracaoMatriculaSerasa = new Date();
        }
        return dataAlteracaoMatriculaSerasa;
    }

    public void setDataAlteracaoMatriculaSerasa(Date dataAlteracaoMatriculaSerasa) {
        this.dataAlteracaoMatriculaSerasa = dataAlteracaoMatriculaSerasa;
    }

    public Double getTotalPontoProcSeletivo() {
        return totalPontoProcSeletivo;
    }

    public void setTotalPontoProcSeletivo(Double totalPontoProcSeletivo) {
        this.totalPontoProcSeletivo = totalPontoProcSeletivo;
    }

    /**
     * @return the matriculaComHistoricoAlunoVO
     */
    public MatriculaComHistoricoAlunoVO getMatriculaComHistoricoAlunoVO() {
        if (matriculaComHistoricoAlunoVO == null) {
            matriculaComHistoricoAlunoVO = new MatriculaComHistoricoAlunoVO();
        }
        return matriculaComHistoricoAlunoVO;
    }

    /**
     * @param matriculaComHistoricoAlunoVO the matriculaComHistoricoAlunoVO to set
     */
    public void setMatriculaComHistoricoAlunoVO(MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO) {
        this.matriculaComHistoricoAlunoVO = matriculaComHistoricoAlunoVO;
    }

    public String getSituacaoENADE_Apresentar() {
        if (this.getFezEnade()) {
            return "Regularizada";
        }
        return "Pendente";
    }

    public Date getDataProcessoSeletivo() {
        return dataProcessoSeletivo;
    }

    public void setDataProcessoSeletivo(Date dataProcessoSeletivo) {
        this.dataProcessoSeletivo = dataProcessoSeletivo;
    }

    /**
     * @return the gradeCurricularAtual
     */
    @XmlElement(name = "gradeCurricularAtual")
    public GradeCurricularVO getGradeCurricularAtual() {
        if (gradeCurricularAtual == null) {
            gradeCurricularAtual = new GradeCurricularVO();
        }
        return gradeCurricularAtual;
    }

    /**
     * @param gradeCurricularAtual the gradeCurricularAtual to set
     */
    public void setGradeCurricularAtual(GradeCurricularVO gradeCurricularAtual) {
        this.gradeCurricularAtual = gradeCurricularAtual;
    }

    public Boolean getNaoApresentarCenso() {
        if (naoApresentarCenso == null) {
            naoApresentarCenso = Boolean.FALSE;
        }
        return naoApresentarCenso;
    }

    public void setNaoApresentarCenso(Boolean naoApresentarCenso) {
        this.naoApresentarCenso = naoApresentarCenso;
    }

    public Date getDataEnvioNotificacao4() {
        return dataEnvioNotificacao4;
    }

    public void setDataEnvioNotificacao4(Date dataEnvioNotificacao4) {
        this.dataEnvioNotificacao4 = dataEnvioNotificacao4;
    }

    /**
     * @return the historicosAproveitamentoDisciplinaPrevisto
     */
    public List<HistoricoVO> getHistoricosAproveitamentoDisciplinaPrevisto() {
        if (historicosAproveitamentoDisciplinaPrevisto == null) {
            historicosAproveitamentoDisciplinaPrevisto = new ArrayList();
        }
        return historicosAproveitamentoDisciplinaPrevisto;
    }

    /**
     * @param historicosAproveitamentoDisciplinaPrevisto the historicosAproveitamentoDisciplinaPrevisto to set
     */
    public void setHistoricosAproveitamentoDisciplinaPrevisto(List<HistoricoVO> historicosAproveitamentoDisciplinaPrevisto) {
        this.historicosAproveitamentoDisciplinaPrevisto = historicosAproveitamentoDisciplinaPrevisto;
    }

    /**
     * @return the aproveitamentoDisciplinaVO
     */
    public AproveitamentoDisciplinaVO getAproveitamentoDisciplinaVO() {
        if (aproveitamentoDisciplinaVO == null) {
            aproveitamentoDisciplinaVO = new AproveitamentoDisciplinaVO();
        }
        return aproveitamentoDisciplinaVO;
    }

    /**
     * @param aproveitamentoDisciplinaVO the aproveitamentoDisciplinaVO to set
     */
    public void setAproveitamentoDisciplinaVO(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO) {
        this.aproveitamentoDisciplinaVO = aproveitamentoDisciplinaVO;
    }

    /**
     * @return the liberarMatriculaTodosPeriodos
     */
    public Boolean getLiberarMatriculaTodosPeriodos() {
        if (liberarMatriculaTodosPeriodos == null) {
            liberarMatriculaTodosPeriodos = Boolean.FALSE;
        }
        return liberarMatriculaTodosPeriodos;
    }

    /**
     * @param liberarMatriculaTodosPeriodos the liberarMatriculaTodosPeriodos to set
     */
    public void setLiberarMatriculaTodosPeriodos(Boolean liberarMatriculaTodosPeriodos) {
        this.liberarMatriculaTodosPeriodos = liberarMatriculaTodosPeriodos;
    }

    /**
     * @return the liberarRenovacaoDocumentaoImpediRenovacaoPendente
     */
    public Boolean getLiberarRenovacaoDocumentaoImpediRenovacaoPendente() {
        if (liberarRenovacaoDocumentaoImpediRenovacaoPendente == null) {
            liberarRenovacaoDocumentaoImpediRenovacaoPendente = Boolean.FALSE;
        }
        return liberarRenovacaoDocumentaoImpediRenovacaoPendente;
    }

    /**
     * @param liberarRenovacaoDocumentaoImpediRenovacaoPendente the liberarRenovacaoDocumentaoImpediRenovacaoPendente to set
     */
    public void setLiberarRenovacaoDocumentaoImpediRenovacaoPendente(Boolean liberarRenovacaoDocumentaoImpediRenovacaoPendente) {
        this.liberarRenovacaoDocumentaoImpediRenovacaoPendente = liberarRenovacaoDocumentaoImpediRenovacaoPendente;
    }

    /**
     * @return the existeDocumentoPendenteImpediRenovacao
     */
    public Boolean getExisteDocumentoPendenteImpediRenovacao() {
        if (existeDocumentoPendenteImpediRenovacao == null) {
             existeDocumentoPendenteImpediRenovacao = Boolean.FALSE;
        }
        return existeDocumentoPendenteImpediRenovacao;
    }

    /**
     * @param existeDocumentoPendenteImpediRenovacao the existeDocumentoPendenteImpediRenovacao to set
     */
    public void setExisteDocumentoPendenteImpediRenovacao(Boolean existeDocumentoPendenteImpediRenovacao) {
        this.existeDocumentoPendenteImpediRenovacao = existeDocumentoPendenteImpediRenovacao;
    }

	public String getTipoTrabalhoConclusaoCurso() {
		if (tipoTrabalhoConclusaoCurso == null) {
			tipoTrabalhoConclusaoCurso = "";
		}
		return tipoTrabalhoConclusaoCurso;
	}
	
	public String getTipoTrabalhoConclusaoCurso_Apresentar() {
		return TipoTrabalhoConclusaoCurso.getDescricao(getTipoTrabalhoConclusaoCurso());
	}

	public void setTipoTrabalhoConclusaoCurso(String tipoTrabalhoConclusaoCurso) {
		this.tipoTrabalhoConclusaoCurso = tipoTrabalhoConclusaoCurso;
	}

	public Double getNotaEnem() {
		if (notaEnem == null) {
			notaEnem = 0.0;
		}
		return notaEnem;
	}

	public void setNotaEnem(Double notaEnem) {
		this.notaEnem = notaEnem;
	}

	public String getObservacaoDiploma() {
		if (observacaoDiploma == null) {
			observacaoDiploma = "";
		}
		return observacaoDiploma;
	}

	public void setObservacaoDiploma(String observacaoDiploma) {
		this.observacaoDiploma = observacaoDiploma;
	}

	public String getMsgErro() {
		if (msgErro == null) {
			msgErro = "";
		}
		return msgErro;
	}

	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MatriculaVO [matricula=" + matricula + ", matriculaPeriodoVOs=" + matriculaPeriodoVOs + ", aluno=" + aluno + ", curso=" + curso + ", turno=" + turno + "]";
	}

	/**
	 * @return the canceladoFinanceiro
	 */
	public Boolean getCanceladoFinanceiro() {
		if (canceladoFinanceiro == null) {
			canceladoFinanceiro = false;
		}
		return canceladoFinanceiro;
	}

	/**
	 * @param canceladoFinanceiro the canceladoFinanceiro to set
	 */
	public void setCanceladoFinanceiro(Boolean canceladoFinanceiro) {
		this.canceladoFinanceiro = canceladoFinanceiro;
	}

	public String getCodigoFinanceiroMatricula() {
		if (codigoFinanceiroMatricula == null) {
			codigoFinanceiroMatricula = "";
		}
		return codigoFinanceiroMatricula;
	}

	public void setCodigoFinanceiroMatricula(String codigoFinanceiroMatricula) {
		this.codigoFinanceiroMatricula = codigoFinanceiroMatricula;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 * Transient
	 */
	private Boolean matriculaOnlineExterna;

	public Boolean getMatriculaOnlineExterna() {
		if(matriculaOnlineExterna == null) {
			matriculaOnlineExterna = false;
		}
		return matriculaOnlineExterna;
	}

	public void setMatriculaOnlineExterna(Boolean matriculaOnlineExterna) {
		this.matriculaOnlineExterna = matriculaOnlineExterna;
	}

	public Integer getClassificacaoIngresso() {
		return classificacaoIngresso;
	}

	public void setClassificacaoIngresso(Integer classificacaoIngresso) {
		this.classificacaoIngresso = classificacaoIngresso;
	}

	public List<ContaReceberVO> getListaContaReceberVOs() {
		if (listaContaReceberVOs == null) {
			listaContaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return listaContaReceberVOs;
	}

	public void setListaContaReceberVOs(List<ContaReceberVO> listaContaReceberVOs) {
		this.listaContaReceberVOs = listaContaReceberVOs;
	}

	public Integer getQtdeContaReceber() {
		return getListaContaReceberVOs().size();
	}
	
	public String getSemestreAnoIngressoCenso() {
		if (semestreAnoIngressoCenso == null) {
			semestreAnoIngressoCenso = "";
		}
		return semestreAnoIngressoCenso;
	}

	public void setSemestreAnoIngressoCenso(String semestreAnoIngressoCenso) {
		this.semestreAnoIngressoCenso = semestreAnoIngressoCenso;
	}
	
	public AutorizacaoCursoVO getRenovacaoReconhecimentoVO() {
		if (renovacaoReconhecimentoVO == null) {
			renovacaoReconhecimentoVO = new AutorizacaoCursoVO();
		}
		return renovacaoReconhecimentoVO;
	}

	public void setRenovacaoReconhecimentoVO(AutorizacaoCursoVO renovacaoReconhecimentoVO) {
		this.renovacaoReconhecimentoVO = renovacaoReconhecimentoVO;
	}
	
	public Boolean getBloquearEmissaoBoletoMatMenVisaoAluno() {
		if (bloquearEmissaoBoletoMatMenVisaoAluno == null) {
			bloquearEmissaoBoletoMatMenVisaoAluno = Boolean.FALSE;
		}
		return bloquearEmissaoBoletoMatMenVisaoAluno;
	}

	public void setBloquearEmissaoBoletoMatMenVisaoAluno(Boolean bloquearEmissaoBoletoMatMenVisaoAluno) {
		this.bloquearEmissaoBoletoMatMenVisaoAluno = bloquearEmissaoBoletoMatMenVisaoAluno;
	}
	
	public String getCodigoInscricaoOVG() {
		if (codigoInscricaoOVG == null) {
			codigoInscricaoOVG = "";
		}
		return codigoInscricaoOVG;
	}

	public void setCodigoInscricaoOVG(String codigoInscricaoOVG) {
		this.codigoInscricaoOVG = codigoInscricaoOVG;
	}
	
    public Boolean getMatriculaBloqueada() {
    	if (matriculaBloqueada == null) {
    		matriculaBloqueada = Boolean.FALSE;
    	}
		return matriculaBloqueada;
	}

	public void setMatriculaBloqueada(Boolean matriculaBloqueada) {
		this.matriculaBloqueada = matriculaBloqueada;
	}

	public String getMotivoMatriculaBloqueada() {
		if (motivoMatriculaBloqueada == null) {
			motivoMatriculaBloqueada = "";
		}
		return motivoMatriculaBloqueada;
	}

	public void setMotivoMatriculaBloqueada(String motivoMatriculaBloqueada) {
		this.motivoMatriculaBloqueada = motivoMatriculaBloqueada;
	}
	

	public String getLocalProcessoSeletivo() {
		if (localProcessoSeletivo == null) {
			localProcessoSeletivo = "";
		}
		return localProcessoSeletivo;
	}

	public void setLocalProcessoSeletivo(String localProcessoSeletivo) {
		this.localProcessoSeletivo = localProcessoSeletivo;
	}
	
	public Double getValorTotalContaReceberAVencer() {
		Double valorTotal = 0.0;
		for (ContaReceberVO contaReceberVO : getListaContaReceberVOs()) {
			if (Uteis.getDataJDBC(contaReceberVO.getDataVencimento()).compareTo(Uteis.getDataJDBC(new Date())) > 0 && contaReceberVO.getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
				valorTotal = valorTotal + (contaReceberVO.getValorReceberCalculado());
			}
		}
		return valorTotal;
	}
	
	public Double getValorTotalContaReceberVencido() {
		Double valorTotal = 0.0;
		for (ContaReceberVO contaReceberVO : getListaContaReceberVOs()) {
			if (Uteis.getDataJDBC(contaReceberVO.getDataVencimento()).compareTo(Uteis.getDataJDBC(new Date())) < 0 && contaReceberVO.getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
				valorTotal = valorTotal + (contaReceberVO.getValorReceberCalculado());
			}
		}
		return valorTotal;
	}
	
	public Double getValorTotalContaReceberRecebido() {
		Double valorTotal = 0.0;
		for (ContaReceberVO contaReceberVO : getListaContaReceberVOs()) {
			if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
				valorTotal = valorTotal + (contaReceberVO.getValorRecebido());
			}
		}
		return valorTotal;
	}
	
	public Double getValorTotalContaReceberNegociado() {
		Double valorTotal = 0.0;
		for (ContaReceberVO contaReceberVO : getListaContaReceberVOs()) {
			if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.NEGOCIADO.getValor())) {
				valorTotal = valorTotal + (contaReceberVO.getValorReceberCalculado());
			}
		}
		return valorTotal;
	}

	public Double getValorTotalContaReceberCancelado() {
		Double valorTotal = 0.0;
		for (ContaReceberVO contaReceberVO : getListaContaReceberVOs()) {
			if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())) {
				valorTotal = valorTotal + (contaReceberVO.getValorReceberCalculado());
			}
		}
		return valorTotal;
	}

	public List<RequerimentoVO> getListaRequerimentoVOs() {
		if (listaRequerimentoVOs == null) {
			listaRequerimentoVOs = new ArrayList<RequerimentoVO>(0);
		}
		return listaRequerimentoVOs;
	}

	public void setListaRequerimentoVOs(List<RequerimentoVO> listaRequerimentoVOs) {
		this.listaRequerimentoVOs = listaRequerimentoVOs;
	}
	
	public Double getValorTotalRequerimentoDeferido() {
		Double valorTotal = 0.0;
		for (RequerimentoVO requerimentoVO : getListaRequerimentoVOs()) {
			if (requerimentoVO.getSituacao().equals("FD")) {
				valorTotal = valorTotal + requerimentoVO.getValor();
			}
		}
		return valorTotal;
	}
	
	public Double getValorTotalRequerimentoIndeferido() {
		Double valorTotal = 0.0;
		for (RequerimentoVO requerimentoVO : getListaRequerimentoVOs()) {
			if (requerimentoVO.getSituacao().equals("FI")) {
				valorTotal = valorTotal + requerimentoVO.getValor();
			}
		}
		return valorTotal;
	}
	
	public Double getValorTotalRequerimentoAguardandoPagamento() {
		Double valorTotal = 0.0;
		for (RequerimentoVO requerimentoVO : getListaRequerimentoVOs()) {
			if (requerimentoVO.getSituacao().equals("AP")) {
				valorTotal = valorTotal + requerimentoVO.getValor();
			}
		}
		return valorTotal;
	}
	
	public Double getValorTotalRequerimentoTotalExecucao() {
		Double valorTotal = 0.0;
		for (RequerimentoVO requerimentoVO : getListaRequerimentoVOs()) {
			if (requerimentoVO.getSituacao().equals("EX")) {
				valorTotal = valorTotal + requerimentoVO.getValor();
			}
		}
		return valorTotal;
	}
	
	public Double getValorTotalRequerimentoPendente() {
		Double valorTotal = 0.0;
		for (RequerimentoVO requerimentoVO : getListaRequerimentoVOs()) {
			if (requerimentoVO.getSituacao().equals("PE")) {
				valorTotal = valorTotal + requerimentoVO.getValor();
			}
		}
		return valorTotal;
	}
	
	public UsuarioVO getResponsavelSuspensaoMatricula() {
		if (responsavelSuspensaoMatricula == null) {
			responsavelSuspensaoMatricula = new UsuarioVO();
		}
		return responsavelSuspensaoMatricula;
	}

	public void setResponsavelSuspensaoMatricula(UsuarioVO responsavelSuspensaoMatricula) {
		this.responsavelSuspensaoMatricula = responsavelSuspensaoMatricula;
	}

	public UsuarioVO getResponsavelCancelamentoSuspensaoMatricula() {
		if (responsavelCancelamentoSuspensaoMatricula == null) {
			responsavelCancelamentoSuspensaoMatricula = new UsuarioVO();
		}
		return responsavelCancelamentoSuspensaoMatricula;
	}

	public void setResponsavelCancelamentoSuspensaoMatricula(UsuarioVO responsavelCancelamentoSuspensaoMatricula) {
		this.responsavelCancelamentoSuspensaoMatricula = responsavelCancelamentoSuspensaoMatricula;
	}

	public Date getDataCancelamentoSuspensaoMatricula() {
		if (dataCancelamentoSuspensaoMatricula == null) {
			dataCancelamentoSuspensaoMatricula = new Date();
		}
		return dataCancelamentoSuspensaoMatricula;
	}

	public void setDataCancelamentoSuspensaoMatricula(Date dataCancelamentoSuspensaoMatricula) {
		this.dataCancelamentoSuspensaoMatricula = dataCancelamentoSuspensaoMatricula;
	}

	public String getMotivoCancelamentoSuspensaoMatricula() {
		if (motivoCancelamentoSuspensaoMatricula == null) {
			motivoCancelamentoSuspensaoMatricula = "";
		}
		return motivoCancelamentoSuspensaoMatricula;
	}

	public void setMotivoCancelamentoSuspensaoMatricula(String motivoCancelamentoSuspensaoMatricula) {
		this.motivoCancelamentoSuspensaoMatricula = motivoCancelamentoSuspensaoMatricula;
	}

	public UsuarioVO getResponsavelAdiamentoSuspensaoMatricula() {
		if (responsavelAdiamentoSuspensaoMatricula == null) {
			responsavelAdiamentoSuspensaoMatricula = new UsuarioVO();
		}
		return responsavelAdiamentoSuspensaoMatricula;
	}

	public void setResponsavelAdiamentoSuspensaoMatricula(UsuarioVO responsavelAdiamentoSuspensaoMatricula) {
		this.responsavelAdiamentoSuspensaoMatricula = responsavelAdiamentoSuspensaoMatricula;
	}

	public Date getDataAdiamentoSuspensaoMatricula() {
		if (dataAdiamentoSuspensaoMatricula == null) {
			dataAdiamentoSuspensaoMatricula = new Date();
		}
		return dataAdiamentoSuspensaoMatricula;
	}

	public void setDataAdiamentoSuspensaoMatricula(Date dataAdiamentoSuspensaoMatricula) {
		this.dataAdiamentoSuspensaoMatricula = dataAdiamentoSuspensaoMatricula;
	}

	public String getMotivoAdiamentoSuspensaoMatricula() {
		if (motivoAdiamentoSuspensaoMatricula == null) {
			motivoAdiamentoSuspensaoMatricula = "";
		}
		return motivoAdiamentoSuspensaoMatricula;
	}

	public void setMotivoAdiamentoSuspensaoMatricula(String motivoAdiamentoSuspensaoMatricula) {
		this.motivoAdiamentoSuspensaoMatricula = motivoAdiamentoSuspensaoMatricula;
	}
	
	public List<ItemEmprestimoVO> getListaItemEmprestimoVOs() {
		if (listaItemEmprestimoVOs == null) {
			listaItemEmprestimoVOs = new ArrayList<ItemEmprestimoVO>(0);
		}
		return listaItemEmprestimoVOs;
	}

	public void setListaItemEmprestimoVOs(List<ItemEmprestimoVO> listaItemEmprestimoVOs) {
		this.listaItemEmprestimoVOs = listaItemEmprestimoVOs;
	}
	
	public Integer getQuantidadeTotalEmprestado() {
		Integer qtde = 0;
		for (ItemEmprestimoVO obj : getListaItemEmprestimoVOs()) {
			if (obj.getSituacao().equals(SituacaoItemEmprestimo.EM_EXECUCAO.getValor()) && obj.getDataPrevisaoDevolucao().compareTo(new Date()) >= 0) {
				qtde++;
			}
		}
		return qtde;
	}
	
	public Integer getQuantidadeTotalDevolvido() {
		Integer qtde = 0;
		for (ItemEmprestimoVO obj : getListaItemEmprestimoVOs()) {
			if (obj.getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())) {
				qtde++;
			}
		}
		return qtde;
	}
	
	public Integer getQuantidadeTotalAtrasado() {
		Integer qtde = 0;
		for (ItemEmprestimoVO obj : getListaItemEmprestimoVOs()) {
			if (obj.getSituacao().equals(SituacaoItemEmprestimo.EM_EXECUCAO.getValor()) && obj.getDataPrevisaoDevolucao().compareTo(new Date()) < 0) {
				qtde++;
			}
		}
		return qtde;
	}
	
	public Integer getQuantidadeTotalGeralItemEmprestimo() {
		return getListaItemEmprestimoVOs().size();
	}

	public List<InteracaoWorkflowVO> getListaInteracaoWorkFlowVOs() {
		if (listaInteracaoWorkFlowVOs == null) {
			listaInteracaoWorkFlowVOs = new ArrayList<InteracaoWorkflowVO>(0);
		}
		return listaInteracaoWorkFlowVOs;
	}

	public void setListaInteracaoWorkFlowVOs(List<InteracaoWorkflowVO> listaInteracaoWorkFlowVOs) {
		this.listaInteracaoWorkFlowVOs = listaInteracaoWorkFlowVOs;
	}

	public Date getDataEmissaoHistorico() {
		if (dataEmissaoHistorico == null) {
			dataEmissaoHistorico = new Date();
		}
		return dataEmissaoHistorico;
	}

	public void setDataEmissaoHistorico(Date dataEmissaoHistorico) {
		this.dataEmissaoHistorico = dataEmissaoHistorico;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataBaseGeracaoParcelas() {
		return dataBaseGeracaoParcelas;
	}

	public void setDataBaseGeracaoParcelas(Date dataBaseGeracaoParcelas) {
		this.dataBaseGeracaoParcelas = dataBaseGeracaoParcelas;
	}

	public String getSituacaoParcela() {
		if (situacaoParcela == null) {
			situacaoParcela = "";
		}
		return situacaoParcela;
	}

	public void setSituacaoParcela(String situacaoParcela) {
		this.situacaoParcela = situacaoParcela;
	}

	public Boolean getPermiteExecucaoReajustePreco() {
		if (permiteExecucaoReajustePreco == null) {
			permiteExecucaoReajustePreco = true;
		}
		return permiteExecucaoReajustePreco;
	}

	public void setPermiteExecucaoReajustePreco(Boolean permiteExecucaoReajustePreco) {
		this.permiteExecucaoReajustePreco = permiteExecucaoReajustePreco;
	}

	public Date getDataCancelamentoReajustePreco() {
		return dataCancelamentoReajustePreco;
	}

	public void setDataCancelamentoReajustePreco(Date dataCancelamentoReajustePreco) {
		this.dataCancelamentoReajustePreco = dataCancelamentoReajustePreco;
	}

	public UsuarioVO getResponsavelCancelamentoReajustePreco() {
		if (responsavelCancelamentoReajustePreco == null) {
			responsavelCancelamentoReajustePreco = new UsuarioVO();
		}
		return responsavelCancelamentoReajustePreco;
	}

	public void setResponsavelCancelamentoReajustePreco(UsuarioVO responsavelCancelamentoReajustePreco) {
		this.responsavelCancelamentoReajustePreco = responsavelCancelamentoReajustePreco;
	}

	public String getMotivoCancelamentoReajustePreco() {
		if (motivoCancelamentoReajustePreco == null) {
			motivoCancelamentoReajustePreco = "";
		}
		return motivoCancelamentoReajustePreco;
	}

	public void setMotivoCancelamentoReajustePreco(String motivoCancelamentoReajustePreco) {
		this.motivoCancelamentoReajustePreco = motivoCancelamentoReajustePreco;
	}

	public String getCondicaoPagamentoPlanoFinanceiroCurso() {
		return condicaoPagamentoPlanoFinanceiroCurso;
	}

	public void setCondicaoPagamentoPlanoFinanceiroCurso(String condicaoPagamentoPlanoFinanceiroCurso) {
		this.condicaoPagamentoPlanoFinanceiroCurso = condicaoPagamentoPlanoFinanceiroCurso;
	}

	public String getPlanoFinanceiroCurso() {
		return planoFinanceiroCurso;
	}

	public void setPlanoFinanceiroCurso(String planoFinanceiroCurso) {
		this.planoFinanceiroCurso = planoFinanceiroCurso;
	}

	public Date getDataMatriculaPeriodo() {
		return dataMatriculaPeriodo;
	}

	public void setDataMatriculaPeriodo(Date dataMatriculaPeriodo) {
		this.dataMatriculaPeriodo = dataMatriculaPeriodo;
	}

	public Boolean getMatriculaConfirmada() {
		return matriculaConfirmada;
	}

	public void setMatriculaConfirmada(Boolean matriculaConfirmada) {
		this.matriculaConfirmada = matriculaConfirmada;
	}

	public Date getDataVencimentoMatricula() {
		return dataVencimentoMatricula;
	}

	public void setDataVencimentoMatricula(Date dataVencimentoMatricula) {
		this.dataVencimentoMatricula = dataVencimentoMatricula;
	}
	
	public String getOrientadorMonografia() {
		if (orientadorMonografia == null) {
			orientadorMonografia = "";
		}
		return orientadorMonografia;
	}

	public void setOrientadorMonografia(String orientadorMonografia) {
		this.orientadorMonografia = orientadorMonografia;
	}
	public Date getDataPagamentoMatricula() {
		return dataPagamentoMatricula;
	}

	public void setDataPagamentoMatricula(Date dataPagamentoMatricula) {
		this.dataPagamentoMatricula = dataPagamentoMatricula;
	}

	public Boolean getAlunoInadimplente() {
		return alunoInadimplente;
	}

	public void setAlunoInadimplente(Boolean alunoInadimplente) {
		this.alunoInadimplente = alunoInadimplente;
	}

	public String getCategoriacondicaopagamento() {
		return categoriacondicaopagamento;
	}

	public void setCategoriacondicaopagamento(String categoriacondicaopagamento) {
		this.categoriacondicaopagamento = categoriacondicaopagamento;
	}

	public Double getDescontoMensalidade() {
		return descontoMensalidade;
	}

	public void setDescontoMensalidade(Double descontoMensalidade) {
		this.descontoMensalidade = descontoMensalidade;
	}

	public Double getDescontoMatricula() {
		return descontoMatricula;
	}

	public void setDescontoMatricula(Double descontoMatricula) {
		this.descontoMatricula = descontoMatricula;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getSituacaoMatriculaPeriodo() {
		return situacaoMatriculaPeriodo;
	}

	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}

	public Double getValorParcela() {
		if (valorParcela == null) {
			valorParcela = 0.0;
		}
		return valorParcela;
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public List<PendenciaLiberacaoMatriculaVO> getPendenciaLiberacaoMatriculaVOs() {
		return pendenciaLiberacaoMatriculaVOs;
	}

	public void setPendenciaLiberacaoMatriculaVOs(List<PendenciaLiberacaoMatriculaVO> pendenciaLiberacaoMatriculaVOs) {
		this.pendenciaLiberacaoMatriculaVOs = pendenciaLiberacaoMatriculaVOs;
	}

	public Boolean getBloqueioPorSolicitacaoLiberacaoMatricula() {
		if(bloqueioPorSolicitacaoLiberacaoMatricula == null) {
			bloqueioPorSolicitacaoLiberacaoMatricula = Boolean.FALSE;
		}
		return bloqueioPorSolicitacaoLiberacaoMatricula;
	}

	public void setBloqueioPorSolicitacaoLiberacaoMatricula(Boolean bloqueioPorSolicitacaoLiberacaoMatricula) {
		this.bloqueioPorSolicitacaoLiberacaoMatricula = bloqueioPorSolicitacaoLiberacaoMatricula;
	}

	public Boolean getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica() {
		if(bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica == null) {
			bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica = Boolean.FALSE;
		}
		return bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica;
	}

	public void setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica(
			Boolean bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica) {
		this.bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica = bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica;
	}

	public Boolean getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira() {
		if(bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira == null) {
			bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira = Boolean.FALSE;
		}
		return bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira;
	}

	public void setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira(
			Boolean bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira) {
		this.bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira = bloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira;
	}
		
	public void teste(Date dataAntiga , Date novaData) {
		if (dataAntiga != novaData) {
		setAnoIngresso(String.valueOf(Uteis.getAnoData(novaData)));
		setSemestreIngresso(String.valueOf(Uteis.getSemestreData(novaData)));
		setMesIngresso(String.valueOf(Uteis.getMesData(novaData)));
		}
	}
	
	public Date getDataBaseGeracaoParcelasOriginal() {
		return dataBaseGeracaoParcelasOriginal;
	}

	public void setDataBaseGeracaoParcelasOriginal(Date dataBaseGeracaoParcelasOriginal) {
		this.dataBaseGeracaoParcelasOriginal = dataBaseGeracaoParcelasOriginal;
	}
	
	public Boolean getAlunoNaoAssinouContratoMatricula() {
		if(alunoNaoAssinouContratoMatricula == null) {
			alunoNaoAssinouContratoMatricula = Boolean.FALSE;
		}
		return alunoNaoAssinouContratoMatricula;
	}

	public void setAlunoNaoAssinouContratoMatricula(Boolean alunoNaoAssinouContratoMatricula) {
		this.alunoNaoAssinouContratoMatricula = alunoNaoAssinouContratoMatricula;
	}
	
	public String getMensagemAlertaAlunoNaoAssinouContratoMatricula() {
		if(mensagemAlertaAlunoNaoAssinouContratoMatricula == null) {
			mensagemAlertaAlunoNaoAssinouContratoMatricula = "";
		}
		return mensagemAlertaAlunoNaoAssinouContratoMatricula;
	}

	public void setMensagemAlertaAlunoNaoAssinouContratoMatricula(String mensagemAlertaAlunoNaoAssinouContratoMatricula) {
		this.mensagemAlertaAlunoNaoAssinouContratoMatricula = mensagemAlertaAlunoNaoAssinouContratoMatricula;
	}
	
	public List<GradeDisciplinaVO> getListaGradeDisciplinaObrigatorioPendente() {
		if(listaGradeDisciplinaObrigatorioPendente == null) {
			listaGradeDisciplinaObrigatorioPendente = new ArrayList<GradeDisciplinaVO>();
		}
		return listaGradeDisciplinaObrigatorioPendente;
	}

	public void setListaGradeDisciplinaObrigatorioPendente(List<GradeDisciplinaVO> listaGradeDisciplinaObrigatorioPendente) {
		this.listaGradeDisciplinaObrigatorioPendente = listaGradeDisciplinaObrigatorioPendente;
	}

	public Integer getHorasDisciplinaOptativaExigida() {
		if(horasDisciplinaOptativaExigida == null) {
			horasDisciplinaOptativaExigida = 0;
		}
		return horasDisciplinaOptativaExigida;
	}

	public void setHorasDisciplinaOptativaExigida(Integer horasDisciplinaOptativaExigida) {
		this.horasDisciplinaOptativaExigida = horasDisciplinaOptativaExigida;
	}

	public Integer getHorasDisciplinaOptativaCumprida() {
		if(horasDisciplinaOptativaCumprida == null) {
			horasDisciplinaOptativaCumprida =0;
		}
		return horasDisciplinaOptativaCumprida;
	}

	public void setHorasDisciplinaOptativaCumprida(Integer horasDisciplinaOptativaCumprida) {
		this.horasDisciplinaOptativaCumprida = horasDisciplinaOptativaCumprida;
	}

	public Integer getHorasEstagioExigida() {
		if(horasEstagioExigida == null) {
			horasEstagioExigida = 0;
		}
		return horasEstagioExigida;
	}

	public void setHorasEstagioExigida(Integer horasEstagioExigida) {
		this.horasEstagioExigida = horasEstagioExigida;
	}

	public Integer getHorasEstagioCumprida() {
		if(horasEstagioCumprida == null) {
			horasEstagioCumprida =0;
		}
		return horasEstagioCumprida;
	}

	public void setHorasEstagioCumprida(Integer horasEstagioCumprida) {
		this.horasEstagioCumprida = horasEstagioCumprida;
	}

	public Integer getHorasAtividadeComplementarExigida() {
		if(horasAtividadeComplementarExigida == null) {
			horasAtividadeComplementarExigida =0;
		}
		return horasAtividadeComplementarExigida;
	}

	public void setHorasAtividadeComplementarExigida(Integer horasAtividadeComplementarExigida) {
		this.horasAtividadeComplementarExigida = horasAtividadeComplementarExigida;
	}

	public Integer getHorasAtividadeComplementarCumprida() {
		if(horasAtividadeComplementarCumprida == null) {
			horasAtividadeComplementarCumprida =0;
		}
		return horasAtividadeComplementarCumprida;
	}

	public void setHorasAtividadeComplementarCumprida(Integer horasAtividadeComplementarCumprida) {
		this.horasAtividadeComplementarCumprida = horasAtividadeComplementarCumprida;
	}
	
	
	public String getProficienciaLinguaEstrangeira() {
		if (proficienciaLinguaEstrangeira == null) {
			proficienciaLinguaEstrangeira = "";
		}
		return proficienciaLinguaEstrangeira;
	}


	public void setProficienciaLinguaEstrangeira(String proficienciaLinguaEstrangeira) {
		this.proficienciaLinguaEstrangeira = proficienciaLinguaEstrangeira;
	}


	public String getSituacaoProficienciaLinguaEstrangeira() {
		if (situacaoProficienciaLinguaEstrangeira == null) {
			situacaoProficienciaLinguaEstrangeira = "";
		}
		return situacaoProficienciaLinguaEstrangeira;
	}


	public void setSituacaoProficienciaLinguaEstrangeira(String situacaoProficienciaLinguaEstrangeira) {
		this.situacaoProficienciaLinguaEstrangeira = situacaoProficienciaLinguaEstrangeira;
	}

	public boolean isGeracaoMatriculaAutomatica() {
		return geracaoMatriculaAutomatica;
	}

	public void setGeracaoMatriculaAutomatica(boolean geracaoMatriculaAutomatica) {
		this.geracaoMatriculaAutomatica = geracaoMatriculaAutomatica;
	}

	public Integer getNumeroMatriculaIncrimental() {
		if (numeroMatriculaIncrimental == null) {
			numeroMatriculaIncrimental = 0;
		}
		return numeroMatriculaIncrimental;
	}

	public void setNumeroMatriculaIncrimental(Integer numeroMatriculaIncrimental) {
		this.numeroMatriculaIncrimental = numeroMatriculaIncrimental;
	}

	public Integer getCodigoNumeroMatricula() {
		if (codigoNumeroMatricula == null) {
			codigoNumeroMatricula = 0;
		}
		return codigoNumeroMatricula;
	}

	public void setCodigoNumeroMatricula(Integer codigoNumeroMatricula) {
		this.codigoNumeroMatricula = codigoNumeroMatricula;
	}
	
	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaVO() {
		if (avaliacaoOnlineMatriculaVO == null) {
			avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaVO;
	}

	public void setAvaliacaoOnlineMatriculaVO(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		this.avaliacaoOnlineMatriculaVO = avaliacaoOnlineMatriculaVO;
	}

	public SituacaoAtividadeRespostaEnum getSituacaoAtividadeRespostaQuestao() {
		if (situacaoAtividadeRespostaQuestao == null) {
			situacaoAtividadeRespostaQuestao = SituacaoAtividadeRespostaEnum.NAO_RESPONDIDA;
		}
		return situacaoAtividadeRespostaQuestao;
	}

	public void setSituacaoAtividadeRespostaQuestao(SituacaoAtividadeRespostaEnum situacaoAtividadeRespostaQuestao) {
		this.situacaoAtividadeRespostaQuestao = situacaoAtividadeRespostaQuestao;
	}

	public Boolean getPermitirInclusaoExclusaoDisciplinasRenovacao() {
		if (permitirInclusaoExclusaoDisciplinasRenovacao == null) {
			permitirInclusaoExclusaoDisciplinasRenovacao = true;
		}
		return permitirInclusaoExclusaoDisciplinasRenovacao;
	}

	public void setPermitirInclusaoExclusaoDisciplinasRenovacao(Boolean permitirInclusaoExclusaoDisciplinasRenovacao) {
		this.permitirInclusaoExclusaoDisciplinasRenovacao = permitirInclusaoExclusaoDisciplinasRenovacao;
	}
	
	public String getTitulacaoOrientadorMonografia() {
		if(titulacaoOrientadorMonografia == null) {
			titulacaoOrientadorMonografia = "";
		}
		return titulacaoOrientadorMonografia;
	}

	public void setTitulacaoOrientadorMonografia(String titulacaoOrientadorMonografia) {
		this.titulacaoOrientadorMonografia = titulacaoOrientadorMonografia;
	}

	public Integer getCargaHorariaMonografia() {
		if(cargaHorariaMonografia == null) {
			cargaHorariaMonografia = 0;
		}
		return cargaHorariaMonografia;
	}

	public void setCargaHorariaMonografia(Integer cargaHorariaMonografia) {
		this.cargaHorariaMonografia = cargaHorariaMonografia;
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if(salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}
	
	public SalaAulaBlackboardVO getSalaAulaBlackboardTcc() {
		if (salaAulaBlackboardTcc == null) {
			salaAulaBlackboardTcc = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardTcc;
	}

	public void setSalaAulaBlackboardTcc(SalaAulaBlackboardVO salaAulaBlackboardTcc) {
		this.salaAulaBlackboardTcc = salaAulaBlackboardTcc;
	}

	@XmlElement(name = "bolsasAuxilios")
	public Boolean getBolsasAuxilios() {
		if (bolsasAuxilios == null) {
			bolsasAuxilios = false;
		}
		return bolsasAuxilios;
	}

	public void setBolsasAuxilios(Boolean bolsasAuxilios) {
		this.bolsasAuxilios = bolsasAuxilios;
	}
	
	@XmlElement(name = "autodeclaracaoPretoPardoIndigena")
	public Boolean getAutodeclaracaoPretoPardoIndigena() {
		if (autodeclaracaoPretoPardoIndigena == null) {
			autodeclaracaoPretoPardoIndigena = false;
		}
		return autodeclaracaoPretoPardoIndigena;
	}

	public void setAutodeclaracaoPretoPardoIndigena(Boolean autodeclaracaoPretoPardoIndigena) {
		this.autodeclaracaoPretoPardoIndigena = autodeclaracaoPretoPardoIndigena;
	}
	
	@XmlElement(name = "normasMatricula")
	public Boolean getNormasMatricula() {
		if (normasMatricula == null) {
			normasMatricula = false;
		}
		return normasMatricula;
	}

	public void setNormasMatricula(Boolean normasMatricula) {
		this.normasMatricula = normasMatricula;
	}
	

	@XmlElement(name = "escolaPublica")
	public Boolean getEscolaPublica() {
		if (escolaPublica == null) {
			escolaPublica = false;
		}
		return escolaPublica;
	}

	public void setEscolaPublica(Boolean escolaPublica) {
		this.escolaPublica = escolaPublica;
	}
	

	public DiaSemana getDiaSemanaAula() {
		if(diaSemanaAula == null) {
			diaSemanaAula = DiaSemana.NENHUM; 
		}
		return diaSemanaAula;
	}

	public void setDiaSemanaAula(DiaSemana diaSemanaAula) {
		this.diaSemanaAula = diaSemanaAula;
	}

	public NomeTurnoCensoEnum getTurnoAula() {
		if(turnoAula == null) {
			turnoAula = NomeTurnoCensoEnum.NENHUM; 
		}
		return turnoAula;
	}

	public void setTurnoAula(NomeTurnoCensoEnum turnoAula) {
		this.turnoAula = turnoAula;
	}

	public String getRegistroAcademicoOuMatriculaApresentar() {
		if(registroAcademicoOuMatriculaApresentar == null ) {
			registroAcademicoOuMatriculaApresentar = "";
		}
		return registroAcademicoOuMatriculaApresentar;
	}

	public void setRegistroAcademicoOuMatriculaApresentar(String registroAcademicoOuMatriculaApresentar) {
		this.registroAcademicoOuMatriculaApresentar = registroAcademicoOuMatriculaApresentar;
	}
	
	public String getAutoCompleteMatriculaApresentarModelHTML() {
		int linhas = 2;	
        StringBuilder html = new StringBuilder("<table class=\"ipp\"")
		.append("\"><tr><td rowspan=\"").append(linhas).append("\" style=\"width:7%\"><img src=\"")
		.append("../../resources/imagens/foto_usuario.jpg").append("\" style=\"width:44px;padding:0px;margin:0px\"/></td>\r\n")
		.append("<td style=\"width:100%\"><span class=\"sp\">").append(getAluno().getNome()).append("</span>")
		.append("<span class=\"ssi\">&nbsp;&nbsp;RA: </span><span class=\"sc\">").append(getAluno().getRegistroAcademico()).append("</span>")		
		.append("<span class=\"ssi\">&nbsp;&nbsp;Matricula: </span><span class=\"sc\">").append(getMatricula()).append("</span></td></tr>\r\n")		
		.append("<tr><td><span class=\"ssi\">Curso: </span><span class=\"sc\">").append(getCurso().getNome())				
		.append("</span><span class=\"ssi\">&nbsp;&nbsp;Unidade Ensino: </span><span class=\"sc\">").append(getUnidadeEnsino().getNome()).append("</span></td>\r\n </table>");				
		 return html.toString();
	}
	public ColacaoGrauVO getColacaoGrauVO() {
		if(colacaoGrauVO == null) {
			colacaoGrauVO = new ColacaoGrauVO();
		}
		return colacaoGrauVO;
	}


	public void setColacaoGrauVO(ColacaoGrauVO colacaoGrauVO) {
		this.colacaoGrauVO = colacaoGrauVO;
	}

	public ProgramacaoFormaturaAlunoVO getProgramacaoFormaturaAlunoVO() {
		if(programacaoFormaturaAlunoVO == null) {
			programacaoFormaturaAlunoVO = new ProgramacaoFormaturaAlunoVO();
		}
		return programacaoFormaturaAlunoVO;
	}

	public void setProgramacaoFormaturaAlunoVO(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) {
		this.programacaoFormaturaAlunoVO = programacaoFormaturaAlunoVO;
	}
	
	private Integer percentualIntegralizacaoCurricular;
	

	public Integer getPercentualIntegralizacaoCurricular() {
		if(percentualIntegralizacaoCurricular == null) {
			if(getGradeCurricularAtual().getCargaHoraria().equals(0)) {
				percentualIntegralizacaoCurricular = 100;
			}else {
			Integer chNaoCumprida = 0;
			for(GradeDisciplinaVO gradeDisciplina : getListaGradeDisciplinaObrigatorioPendente()) {
				chNaoCumprida += gradeDisciplina.getCargaHoraria();
			}
			if(Uteis.isAtributoPreenchido(getHorasDisciplinaOptativaExigida()) && getHorasDisciplinaOptativaCumprida() < getHorasDisciplinaOptativaExigida()) {
				chNaoCumprida += (getHorasDisciplinaOptativaExigida() - getHorasDisciplinaOptativaCumprida());
			}
			if(Uteis.isAtributoPreenchido(getHorasEstagioExigida()) && getHorasEstagioCumprida() < getHorasEstagioExigida()) {
				chNaoCumprida += (getHorasEstagioExigida() - getHorasEstagioCumprida());
			}
			if(Uteis.isAtributoPreenchido(getHorasAtividadeComplementarExigida()) && getHorasAtividadeComplementarCumprida() < getHorasAtividadeComplementarExigida()) {
				chNaoCumprida +=  (getHorasAtividadeComplementarExigida() - getHorasAtividadeComplementarCumprida());
			}
			
			percentualIntegralizacaoCurricular = (((getGradeCurricularAtual().getCargaHoraria() - chNaoCumprida) * 100)/getGradeCurricularAtual().getCargaHoraria());
			}
		}
		return percentualIntegralizacaoCurricular;
	}

	public void setPercentualIntegralizacaoCurricular(Integer percentualIntegralizacaoCurricular) {
		this.percentualIntegralizacaoCurricular = percentualIntegralizacaoCurricular;
	}

	public MatriculaEnadeVO getMatriculaEnadeVO() {
		if (matriculaEnadeVO == null) {
			matriculaEnadeVO = new MatriculaEnadeVO();
		}
		return matriculaEnadeVO;
	}
	
	public void setMatriculaEnadeVO(MatriculaEnadeVO matriculaEnadeVO) {
		this.matriculaEnadeVO = matriculaEnadeVO;
	}
	
	private List<DocumentacaoGEDVO> listaDocumentacaoGED;

	public List<DocumentacaoGEDVO> getListaDocumentacaoGED() {
		if (listaDocumentacaoGED == null) {
			listaDocumentacaoGED = new ArrayList(0);
		}
		return listaDocumentacaoGED;
	}

	public void setListaDocumentacaoGED(List<DocumentacaoGEDVO> listaDocumentacaoGED) {
		this.listaDocumentacaoGED = listaDocumentacaoGED;
	}
	
	public String getFinanciamentoEstudantilCenso() {
		if (financiamentoEstudantilCenso == null) {
			financiamentoEstudantilCenso = "";
		}
		return financiamentoEstudantilCenso;
	}

	public void setFinanciamentoEstudantilCenso(String financiamentoEstudantilCenso) {
		this.financiamentoEstudantilCenso = financiamentoEstudantilCenso;
	}

	public List<String> getListaFinanciamentoEstudantilVOs() {
		if (listaFinanciamentoEstudantilVOs == null) {
			listaFinanciamentoEstudantilVOs = new ArrayList<>(0);
		}
		return listaFinanciamentoEstudantilVOs;
	}

	public void setListaFinanciamentoEstudantilVOs(List<String> listaFinanciamentoEstudantilVOs) {
		this.listaFinanciamentoEstudantilVOs = listaFinanciamentoEstudantilVOs;
	}

	public JustificativaCensoEnum getJustificativaCensoEnum() {
		if (justificativaCensoEnum == null) {
			justificativaCensoEnum = JustificativaCensoEnum.NENHUMA;
		}
		return justificativaCensoEnum;
	}

	public void setJustificativaCensoEnum(JustificativaCensoEnum justificativaCensoEnum) {
		this.justificativaCensoEnum = justificativaCensoEnum;
	}
	
	public TipoMobilidadeAcademicaEnum getTipoMobilidadeAcademicaEnum() {
		if (tipoMobilidadeAcademicaEnum == null) {
			tipoMobilidadeAcademicaEnum = TipoMobilidadeAcademicaEnum.NENHUMA;
		}
		return tipoMobilidadeAcademicaEnum;
	}

	public void setTipoMobilidadeAcademicaEnum(TipoMobilidadeAcademicaEnum tipoMobilidadeAcademicaEnum) {
		this.tipoMobilidadeAcademicaEnum = tipoMobilidadeAcademicaEnum;
	}

	public String getMobilidadeAcademicaNacionalIESDestino() {
		if (mobilidadeAcademicaNacionalIESDestino == null) {
			mobilidadeAcademicaNacionalIESDestino = "";
		}
		return mobilidadeAcademicaNacionalIESDestino;
	}

	public void setMobilidadeAcademicaNacionalIESDestino(String mobilidadeAcademicaNacionalIESDestino) {
		this.mobilidadeAcademicaNacionalIESDestino = mobilidadeAcademicaNacionalIESDestino;
	}

	public String getMobilidadeAcademicaInternacionalPaisDestino() {
		if (mobilidadeAcademicaInternacionalPaisDestino == null) {
			mobilidadeAcademicaInternacionalPaisDestino = "";
		}
		return mobilidadeAcademicaInternacionalPaisDestino;
	}

	public void setMobilidadeAcademicaInternacionalPaisDestino(String mobilidadeAcademicaInternacionalPaisDestino) {
		this.mobilidadeAcademicaInternacionalPaisDestino = mobilidadeAcademicaInternacionalPaisDestino;
	}
	public String getInformacoesCensoRelativoAno() {
		if (informacoesCensoRelativoAno == null) {
			informacoesCensoRelativoAno = "";
		}
		return informacoesCensoRelativoAno;
	}

	public void setInformacoesCensoRelativoAno(String informacoesCensoRelativoAno) {
		this.informacoesCensoRelativoAno = informacoesCensoRelativoAno;
	}
	
	public Integer getTotalCargaHorariaCumprido() {
		if (totalCargaHorariaCumprido == null) {
			totalCargaHorariaCumprido = 0;
		}
		return totalCargaHorariaCumprido;
	}
	
	public void setTotalCargaHorariaCumprido(Integer totalCargaHorariaCumprido) {
		this.totalCargaHorariaCumprido = totalCargaHorariaCumprido;
	}
	
	public Double getPercentualCumprido() {
		if (percentualCumprido == null) {
			percentualCumprido = 0.0;
		}
		return percentualCumprido;
	}
	
	public void setPercentualCumprido(Double percentualCumprido) {
		this.percentualCumprido = percentualCumprido;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if(matriculaPeriodoVO == null) {
			matriculaPeriodoVO =  new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
	
	private Boolean considerarValidacaoLdapBlackBoard ;
	private Boolean criarNovoUsuario ;
	private Boolean processouLdapSemFalhar;
	
	public Boolean getConsiderarValidacaoLdapBlackBoard() {
		if(considerarValidacaoLdapBlackBoard == null ) {
			considerarValidacaoLdapBlackBoard = Boolean.TRUE;
		}
		return considerarValidacaoLdapBlackBoard;
	}	
	
	public void setConsiderarValidacaoLdapBlackBoard(Boolean considerarValidacaoLdapBlackBoard) {
		this.considerarValidacaoLdapBlackBoard = considerarValidacaoLdapBlackBoard ;		
	}	
	
	public Boolean getCriarNovoUsuario() {
		if(criarNovoUsuario == null ) {
			criarNovoUsuario = Boolean.TRUE;
		}
		return criarNovoUsuario;
	}

	public void setCriarNovoUsuario(Boolean criarNovoUsuarioLdapBlackBoard) {
	  this.criarNovoUsuario =criarNovoUsuarioLdapBlackBoard ;		
	}

	public Boolean getProcessouLdapSemFalhar() {
		if(processouLdapSemFalhar == null ) {
			processouLdapSemFalhar = Boolean.TRUE;
		}
		return processouLdapSemFalhar;
	}

	public void setProcessouLdapSemFalhar(Boolean processouLdapSemFalhar) {
		this.processouLdapSemFalhar = processouLdapSemFalhar;
	}
	

	
	public boolean getMatriculaOnlineProcSeletivo() {
		if(matriculaOnlineProcSeletivo == null ) {
			matriculaOnlineProcSeletivo =Boolean.FALSE;
		}
		return matriculaOnlineProcSeletivo;
	}
	
	
	public void setMatriculaOnlineProcSeletivo(Boolean matriculaOnlineProcSeletivo) {
		this.matriculaOnlineProcSeletivo = matriculaOnlineProcSeletivo;
	}

	public Boolean getExisteDiploma() {
		if (existeDiploma == null) {
			existeDiploma = Boolean.FALSE;
		}
		return existeDiploma;
	}
	
	public void setExisteDiploma(Boolean existeDiploma) {
		this.existeDiploma = existeDiploma;
	}
	
	public Boolean getMatriculaIntegralizada() {
		if (matriculaIntegralizada == null) {
			matriculaIntegralizada = Boolean.FALSE;
		}
		return matriculaIntegralizada;
	}
	
	public void setMatriculaIntegralizada(Boolean matriculaIntegralizada) {
		this.matriculaIntegralizada = matriculaIntegralizada;
	}
	
	public Boolean getIngressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas() {
		if(ingressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas == null) {
			ingressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas = false;
		}
		return ingressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas;
	}

	public void setIngressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas(
			Boolean ingressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas) {
		this.ingressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas = ingressouPorPoliticaAcaoAfirmativaOUPorProgramaDiferenciadoDestinacaoVagas;
	}

	public Boolean getTipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas() {
		if(tipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas == null) {
			tipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas = false;
		}
		return tipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas;
	}

	public void setTipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas(
			Boolean tipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas) {
		this.tipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas = tipoPoliticaAcaoAfirmativaProgramaDiferenciadoDestinacaoVagas;
	}

	
	public Boolean getLeiDeCotas() {
		if(leiDeCotas == null) {
			leiDeCotas = false;
		}
		return leiDeCotas;
	}

	public void setLeiDeCotas(Boolean leiDeCotas) {
		this.leiDeCotas = leiDeCotas;
	}

	public Boolean getPretoPardoEIndigena() {
		if(pretoPardoEIndigena == null) {
			pretoPardoEIndigena = false;
		}
		return pretoPardoEIndigena;
	}

	public void setPretoPardoEIndigena(Boolean pretoPardoEIndigena) {
		this.pretoPardoEIndigena = pretoPardoEIndigena;
	}

	public Boolean getRenda() {
		if(renda == null) {
			renda = false;
		}
		return renda;
	}

	public void setRenda(Boolean renda) {
		this.renda = renda;
	}

	public Boolean getPessoaComDeficiencia() {
		if(pessoaComDeficiencia == null) {
			pessoaComDeficiencia = false;
		}
		return pessoaComDeficiencia;
	}

	public void setPessoaComDeficiencia(Boolean pessoaComDeficiencia) {
		this.pessoaComDeficiencia = pessoaComDeficiencia;
	}

	public Boolean getEstudanteProcedenteDeEscolaPublica() {
		if(pessoaComDeficiencia == null) {
			pessoaComDeficiencia = false;
		}
		return estudanteProcedenteDeEscolaPublica;
	}

	public void setEstudanteProcedenteDeEscolaPublica(Boolean estudanteProcedenteDeEscolaPublica) {
		this.estudanteProcedenteDeEscolaPublica = estudanteProcedenteDeEscolaPublica;
	}

	public Boolean getQuilombola() {
		if(quilombola == null) {
			quilombola = false;
		}
		return quilombola;
	}

	public void setQuilombola(Boolean quilombola) {
		this.quilombola = quilombola;
	}

	public Boolean getEstudanteTransgeneroETravesti() {
		if(estudanteTransgeneroETravesti == null) {
			estudanteTransgeneroETravesti = false;
		}
		return estudanteTransgeneroETravesti;
	}

	public void setEstudanteTransgeneroETravesti(Boolean estudanteTransgeneroETravesti) {
		this.estudanteTransgeneroETravesti = estudanteTransgeneroETravesti;
	}

	public Boolean getEstudanteInternacional() {
		if(estudanteInternacional == null) {
			estudanteInternacional = false;
		}
		return estudanteInternacional;
	}

	public void setEstudanteInternacional(Boolean estudanteInternacional) {
		this.estudanteInternacional = estudanteInternacional;
	}

	public Boolean getRefugiadoApatridaOuPortadorDeVistoHumanitario() {
		if(refugiadoApatridaOuPortadorDeVistoHumanitario == null) {
			refugiadoApatridaOuPortadorDeVistoHumanitario = false;
		}
		return refugiadoApatridaOuPortadorDeVistoHumanitario;
	}

	public void setRefugiadoApatridaOuPortadorDeVistoHumanitario(Boolean refugiadoApatridaOuPortadorDeVistoHumanitario) {
		this.refugiadoApatridaOuPortadorDeVistoHumanitario = refugiadoApatridaOuPortadorDeVistoHumanitario;
	}

	public Boolean getIdoso() {
		if(idoso == null) {
			idoso = false;
		}
		return idoso;
	}

	public void setIdoso(Boolean idoso) {
		this.idoso = idoso;
	}

	public Boolean getEstudantePertencenteAPovosEComunidadesTradicionais() {
		if(estudantePertencenteAPovosEComunidadesTradicionais == null) {
			estudantePertencenteAPovosEComunidadesTradicionais = false;
		}
		return estudantePertencenteAPovosEComunidadesTradicionais;
	}

	public void setEstudantePertencenteAPovosEComunidadesTradicionais(
			Boolean estudantePertencenteAPovosEComunidadesTradicionais) {
		this.estudantePertencenteAPovosEComunidadesTradicionais = estudantePertencenteAPovosEComunidadesTradicionais;
	}

	public Boolean getMedalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento() {
		if(medalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento == null) {
			medalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento = false;
		}
		return medalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento;
	}

	public void setMedalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento(
			Boolean medalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento) {
		this.medalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento = medalhistaEmOlimpiadasCientificasECompeticoesDeConhecimento;
	}

	public Boolean getOutrosTiposDeReservaDeVagas() {
		if(outrosTiposDeReservaDeVagas == null) {
			outrosTiposDeReservaDeVagas = false;
		}
		return outrosTiposDeReservaDeVagas;
	}

	public void setOutrosTiposDeReservaDeVagas(Boolean outrosTiposDeReservaDeVagas) {
		this.outrosTiposDeReservaDeVagas = outrosTiposDeReservaDeVagas;
	}

	public Integer getCodigoBloqueioMatriculaRenovacao() {
		if(codigoBloqueioMatriculaRenovacao == null) {
			codigoBloqueioMatriculaRenovacao = 0;
		}
		return codigoBloqueioMatriculaRenovacao;
	}

	public void setCodigoBloqueioMatriculaRenovacao(Integer codigoBloqueioMatriculaRenovacao) {
		this.codigoBloqueioMatriculaRenovacao = codigoBloqueioMatriculaRenovacao;
	}
	
	public String getNrMatriculaCancelada() {
		if (nrMatriculaCancelada == null) {
			nrMatriculaCancelada = "";
		}
		return nrMatriculaCancelada;
	}
	
	public void setNrMatriculaCancelada(String nrMatriculaCancelada) {
		this.nrMatriculaCancelada = nrMatriculaCancelada;
	}

    private List<HistoricoVO> listaProcessoSeletivoDisciplinasAproveitadas;

    public List<HistoricoVO> getListaProcessoSeletivoDisciplinasAproveitadas() {
        if (listaProcessoSeletivoDisciplinasAproveitadas == null) {
            listaProcessoSeletivoDisciplinasAproveitadas = new ArrayList<HistoricoVO>(0);
        }
        return listaProcessoSeletivoDisciplinasAproveitadas;
    }


    public void setListaProcessoSeletivoDisciplinasAproveitadas(List<HistoricoVO> listaProcessoSeletivoDisciplinasAproveitadas) {
        this.listaProcessoSeletivoDisciplinasAproveitadas = listaProcessoSeletivoDisciplinasAproveitadas;
    }
}
