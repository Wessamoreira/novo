package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoContabilizacaoDisciplinaDependenciaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.academico.Matricula;

/**
 * Reponsável por manter os dados da entidade MatriculaPeriodo. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os mï¿½todos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memï¿½ria os dados desta entidade.
 * 
 * @see SuperVO
 * @see Matricula
 */
@XmlRootElement(name = "matriculaPeriodoVO")
public class MatriculaPeriodoVO extends SuperVO {
	/**
	 * atributo transient para controlar a edicao de uma matriculaperiodo com a intencao
	 * de remover disciplinas que estao sendo cursadas por correspodencia. 
	 */
	private TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO;
    
    /**
     * Atributos TRANSIENT utilizadas em controles especificos
     * da renovacao de matricula.
     */
    private Boolean liberadoControleInclusaoDisciplinaPeriodoFuturo;
    private Boolean liberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular;
    private Boolean liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular;
    private Boolean liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular;
    private Boolean liberadoControleInclusaoObrigatoriaDisciplinaDependencia;
    private Boolean liberadoControleInclusaoMinimaObrigatoriaDisciplina;
    
   /** Atributos TRANSIENT utilizadas em controles especificos
     * da renovacao de matricula. Controles que so permitem a alunos
     * que nao estao regulares (devem materias dos periodos anteriores)
     * poderem incluir / exlcuir disciplinas. Alunos regulares (que estao
     * aprovados em todas as disciplinas dos periodos anteriores) devem
     * seguir estudando somente as disciplinas basicas do seu periodo letivo
     * atributo bloquearInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular e 
     * bloquearExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular da cfgAcademicoVO
     * Regras:
     *    Alunos que estiverem no primeiro letivo serao considerados regulares
     *    desde que sua forma de ingresso seja processo seletivo (vestibular, enem,
     *    prouni). Para todas as outras formas de ingresso (trasnferencia, portador,
     *    selecao de vagas remanencentes, etc) o aluno será tratado como irregular
     *    para permitir que ele possa incluir disciplinas para preencher seu horario
     *    de estudo.
     */
    private Boolean alunoRegularMatrizCurricular;
    /** 
     * Nr. disciplinas que aluno esta pendentes em periodos anterioes.
     * Informacao será útil para definir quantas disciplinas ele será obrigado
     * a incluir para pagar os debitos do passado - conforme configuracao academica
     * do curso.
     * atributo habilitarControleInclusaoObrigatoriaDisciplinaDependencia e
     * porcentagemMinimaInclusaoObrigatoriaDisciplinaDependencia da cfgAcademicoVO
     */
    private Integer nrDisciplinasPendentesPeriodosAnteriores;
    /**
     * Nr. disciplinas que o aluno será obrigado a incluir caso a configuracao
     * academica esteja obrigando o mesmo a resgatar disciplinas do passado que 
     * estao pendentes
     */
    private Integer nrDisciplinasPendentesDevemSerIncluidas;
    private Integer nrCreditoPendentesDevemSerIncluidas;
    private Integer nrCargaHorariaPendentesDevemSerIncluidas;
    private TipoContabilizacaoDisciplinaDependenciaEnum tipoContabilizacaoDisciplinaDependencia;
   
	/**
     * ATRIBUTO TRANSIENT UTILIZADO PARA CONTRALAR SE O USUARIO
     * LIBEROU (POR PERFIL DE ACESSO) A INCLUSAO DE DISCIPLINAS
     * ACIMA DO LIMITE DO PERIODO LETIVO
     */
    private Boolean usuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo;
    private String aluno;
    private Integer codigo;
    private Date data;
    private String situacao;
    private String matricula;
    private String situacaoMatriculaPeriodo;
    private String semestre;
    private String ano;
    private TurmaVO turma;
    private TurmaVO turmaPeriodo;
    private Integer unidadeEnsinoCurso;
    private Integer processoMatricula;
    private String titulacaoInstituicao;
    private ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO;
    private GradeCurricularVO gradeCurricular;
    /**
     * ATRIBUTO TRANSIENT utilizado para 
     */
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>GradeCurricular </code>.
     */
    private PeriodoLetivoVO peridoLetivo;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>PeriodoLetivo </code>.
     */
    // private PeriodoLetivoVO periodoLetivoMatricula;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private UsuarioVO responsavelRenovacaoMatricula;
    private UsuarioVO responsavelMatriculaForaPrazo;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>Turma </code>.
     */
    private TextoPadraoVO contratoMatricula;
    private TextoPadraoVO contratoFiador;
    private UsuarioVO responsavelLiberacaoMatricula;
    private UsuarioVO responsavelEmissaoBoletoMatricula;
    private Date dataLiberacaoMatricula;
    private Date dataEmissaoBoletoMatricula;
//    private List<LocalPeriodoVO> listaLocalPeriodoVO;
    //private Integer contaReceber;
    private String nrDocumento;
    private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTumaDisciplinaVOs;
    private List<MatriculaPeriodoTurmaDisciplinaVO> preMatriculaPeriodoTurmaDisciplinaVOs;
    private Integer tranferenciaEntrada;

    private Double valorMatriculaCheio;
    private Double valorDescontoMatricula;
    private Double valorDescontoConvenioMatricula;    
    private Double valorDescontoInstituicaoMatricula;
    private Double valorFinalMatricula;
    private Double valorMatriculaBolsaCusteada;
    private Double valorTotalTodosDescontosMatricula;
    private Double valorResidualMatricula;
    private Double valorBaseDescontoConvenioMatricula;
    
    
    private Double valorMensalidadeCheio;
    private Double valorDescontoMensalidade;
    private Double valorDescontoConvenioMensalidade;
    private Double valorDescontoInstituicaoMensalidade;
    private Double valorFinalMensalidade;
    private Double valorMensalidadeBolsaCusteada;
    private Double valorTotalTodosDescontosMensalidade;
    private Double valorResidualMensalidade;
    private Double valorBaseDescontoConvenioMensalidade;
    
    
    private Double valorMaterialDidaticoCheio;
    private Double valorDescontoMaterialDidatico;
    private Double valorDescontoConvenioMaterialDidatico;
    private Double valorDescontoInstituicaoMaterialDidatico;
    private Double valorFinalMaterialDidatico;
    private Double valorMaterialDidaticoBolsaCusteada;
    private Double valorTotalTodosDescontosMaterialDidatico;
    private Double valorResidualMaterialDidatico;
    private Double valorBaseDescontoConvenioMaterialDidatico;
    private Date dataBaseGeracaoParcelas;



    
    
    
    
    private String justificativaTrancamento;
    //private PlanoFinanceiroCursoVO planoFinanceiroCurso;
//    private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCurso;
    // TRANSIENT - VERSAO 5.0
    // Utilizado para controlar se o usuário de fato deseja alterar um plano/condicacao pgto
    // que já foi persistido para o aluno. Por padrão, quando editar uma matrícula, a opção
    // para alterar o plano estará desabilitada. E esta variável estará falso, por padrão, somente
    // quando o usuário clicar no botão informando que pretende definir um novo plano financeiro/condicao
    // que o sistema irá setar true para esta varíavel, fazendo com que o método inicializarPlanoFinanceiroMatriculaPeriodo
    // defina um novo planoFinanceiro/condicao de pagamento com as configuracoes atuais definidas na turma/unidadeEnsinoCurso.
    private Boolean alterarPlanoCondicacaoPagamentoPersistido;
    // TRANSIENT - VERSAO 5.0
    // Utiliza no momento de editar uma matriculaPeriodo, de forma a garantir que uma condicao
    // de pagamento anteriormente selecionada para o aluno, seja apresentada no momento da edição
    // da matriculaPeriodo.
   // private PlanoFinanceiroCursoVO planoFinanceiroCursoPersistido;
    // TRANSIENT - VERSAO 5.0
    // Utiliza no momento de editar uma matriculaPeriodo, de forma a garantir que uma condicao
    // de pagamento anteriormente selecionada para o aluno, seja apresentada no momento da edição
    // da matriculaPeriodo.
    //private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoPersistido;
    /*
     * Esta lista é utilizada somente para geração de parcelas manual
     * Esta lista armazena em memória, a lista de matriculaPeriodoVencimentoVOs que
     * foram geradas manualmente pelo usuário para uma determinada matriculaPeriodo
     * Isto é importante, pois quando editamos uma matrícula periodo, o SEI sozinho
     * exclui contas a receber geradas, e as gera novamente, de acordo com novas informações
     * definidas no plano financeiro ao aluno. Contudo, como o controle está manual, quando o SEI
     * exlcuir as parcelas geradas e nao pagas, a rotina de geracao precisa dominar quais parcelas
     * ja foram anteriormente geradas pelo usuario, garantindo assim, que as estas parcelas já sejam
     * automaticamente regeradas, evitando do aluno pagar uma parcela, sem que haja, uma conta a receber
     * para ser baixada. Por exemplo, imagine que o usuario gere a parcela 4/5 (com vencimento em 10/10/2010.
     * Considere que o aluno nao pagou esta parcela, contudo, o mesmo recebeu o boleto pertinente a mesma.
     * Apos o aluno receber o boleto, image que a matricula do mesmo seja editada por alguma razao, isto implicará
     * que o SEI irá apagar as contas a receber nao pagas. Contudo, a rotina de geracao deverá perceber que o controle
     * de geracao esta manual e automaticamente regerar a parcela 4/5. Caso controario se o aluno pagar a mesma,
     * teremos um problema grave, pois nao existirá uma conta a receber para ser baixada.
     */
    
    private List<MatriculaPeriodoHistoricoVO> historicoOperacoes;
    private Boolean inclusaoForaPrazo;
    private Boolean reposicao;
    private Double descontoReposicao;
    private Integer numParcelasInclusaoForaPrazo;
    private Double valorTotalParcelaInclusaoForaPrazo;
    private Date diaVencimentoInclusaoForaPrazo;
    private Boolean financeiroManual;
    private Boolean carneEntregue;
    /**
     * Indica um tipo de renovação especial, criada somente para permitir que um 
     * aluno renove sua matrícula por mais um período, para que ele cumpra com atividades
     * acadêmicas obrigatórias, que ele ainda não tenha cursado. Como por exemplo,
     * estágio, atividades complementares ou ENADE. Este tipo de matrícula especial
     * será utilziada para alunos que já tenham cumprido todas as disciplinas do curso,
     * restando somente o fechamento destes itens obrigatórios.
     */
    private Boolean matriculaEspecial;
    /**
     * Atributo necessario para se calcular o valor final a ser cobrado do
     * aluno, haja vista, que o valor cobrado de um aluno pode variar
     * dependendo, do número de disciplinas incluídas / excluídas durante a
     * matrícula.
     */
    private Integer nrDisciplinasIncluidas;
    private Integer nrDisciplinasExcluidas;
    /**
     * Totaliza o nrDisciplinas incluidas de periodos anteriores do aluno.
     * Isto é importante para controles especificdos da renovacao, como por 
     * exemplo para obrigar o aluno a pagar determinado percentual das disciplinas
     * do passado antes, prioritariamente.
     */
    private Integer nrDisciplinasIncluidasPeriodosAnteriores;
    /**
     * Atributo responsavel por armazenar a quantidade de creditos 
     * referentes a disciplinas optativas que o aluno está se matriculando
     * Este número é importante para fornecer uma informação ao aluno/responsável
     * pela matrícula, sobre o alinhamento do que está programado em optativas para o
     * período letivo e o que o aluno realmente está se matriculando.
     */
    private Integer nrCreditoDiscplinasOptativas;
    /**
     * Atributo responsavel por armazenar a quantidade de creditos 
     * referentes a disciplinas optativas que o aluno está se matriculando
     * Este número é importante para fornecer uma informação ao aluno/responsável
     * pela matrícula, sobre o alinhamento do que está programado em optativas para o
     * período letivo e o que o aluno realmente está se matriculando.
     */
    private Integer nrCargaHorariaDiscplinasOptativas;
    private Integer totalCreditoPadraoMatriculaPeriodo;
    private Integer totalCreditoAlunoMatriculaPeriodo;
    private Integer totalCargaHorariaPadraoMatriculaPeriodo;
    private Integer totalCargaHorariaAlunoMatriculaPeriodo;
    /**
     * Atributo transient que é utilizado para manter a cargaHoraria
     * planejada para o curso até o semestre anterior no qual o aluno
     * está renovando. Esta informacao será utilizada no controle da
     * quantidade de disciplinas que o aluno poderá incluir na renovacao
     * do memso. Existe um controle no SEI (configuracao) que [Controlar Inclusão de Disciplinas por Número Máximo de Crédito/Carga Horária Período]
     * que depende desta inforacao. Ou seja, caso o aluno tenha carga horaria
     * pendencia dos períodos anterior, então aumenta-se a quantidade de disciplinas
     * que ele pode incluir no período atual.
     */
    private Integer totalCargaHorariaPadraoAtePeriodoAnterior;
    /**
     * Transient, identico ao de cima, contudo fazendo o controle por credito
     */
    private Integer totalCreditoPadraoAtePeriodoAnterior;
    /**
     * Transient, utilizado em conjunto com os atributos acima, para se calcular
     * quantas horas o aluno está atrasado (ou adiantado) com relacao a carga horária
     * planejada para o curso.
     */
    private Integer totalCargaHorariaAlunoAtePeriodoAnterior;
    /**
     * Transient, utilizado em conjunto com os atributos acima, para se calcular
     * quantos creditos o aluno está atrasado (ou adiantado) com relacao a carga horária
     * planejada para o curso.
     */
    private Integer totalCreditoAlunoAtePeriodoAnterior;
    /**
     * TRANSIENT - populado pelo método MatriculaPeriodo.obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao
     * com explicacoes sobre restrições com relacao ao periodo letivo no qual uma matricula pode ser renovada.
     * * VERSAO 5.0 
     */
    private String mensagemRestricaoPeriodosLetivosRenovacao;
    /**
     * Atributo transiente utilizado somente para calculo
     */
    private Date dataProcessamentoAtual;
    private Boolean reconheceuDivida;
    private Boolean possuiDivida;
//    private PlanoDescontoInclusaoDisciplinaVO planoDescontoInclusaoDisciplinaVO;
    /**
     * -------------------------------------------------------------------------
     * -------------------------------------------------------------------------
     * -------- LISTA DE OBJETOS TRANSIENTS, UTILIZADOS SOMENTE DURANTE --------
     * -------- O PROCESSAMENTO, PORÉM NÃO SÃO PERSISTIDOS NO BD --------
     * -------- DEVEM SER INICIALIZADOS PARA SEREM UTILIZADOS DURANTE --------
     * -------- O REGISTRO OU ALTERAÇÃO DE UMA MATRÍCULA (PERIODO) --------
     * -------------------------------------------------------------------------
     * -------------------------------------------------------------------------
     */
    private MatriculaVO matriculaVO;
    private ProcessoMatriculaVO processoMatriculaVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private Boolean alunoConcordaComTermoRenovacaoOnline;
    private List<HistoricoVO> listaDisciplinasPeriodoLetivoAlunoJaAprovado;
    private List<HistoricoVO> listaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar;
    private ExpedicaoDiplomaVO expedicaoDiplomaVO; 
    // OS CAMPOS A SEGUIR SÃO TRANSIENT: Responsável por controlar os itens da
    // lista de matricula periodo apresentada na tela de Confirmação
    // Pré-Matrícula. Isso foi feito dessa maneira pensando em um processamento
    // mais rápido.
    // Evitando processamento de listas.
    private Boolean erro;
    private String mensagemErro;
    private Boolean apresentarSituacao;
   
    // BOOLEANO responsável por controlar na tela de renovação por turma se o objeto está selecionada na lista,
    // para assim realizar o processamento da rotina de renovação.
    private Boolean selecionarMatriculaPeriodoRenovar;
    private Date dataFechamentoMatriculaPeriodo;
    private OrigemFechamentoMatriculaPeriodoEnum origemFechamentoMatriculaPeriodo;
    private Integer codigoOrigemFechamentoMatriculaPeriodo;
    private Boolean possuiPermissaoInclusaoExclusaoDisciplina;
    private Integer usuarioResponsavelConfirmacaoOuCancelamentoPreMatricula;
    private Boolean bolsista;
    private TextoPadraoVO contratoExtensao;
    private Boolean alunoTransferidoUnidade;
    //Atributos Utilizados Apenas na impressão do contrato
    private Date dataInicioAula;
    private Date dataFinalAula;
    private Integer qtdeParcelaContrato;
    private String dataExtenso;
    private Boolean aceitouTermoContratoRenovacaoOnline;
    private Date dataNotificacaoAbandonoCurso;
    private String motivoLiberacaoPgtoMatricula;
    public static final long serialVersionUID = 1L;
    private Integer nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo;
    private Integer cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo;
    private Integer totalCreditoPendenteAlunoAtePeriodoAnterior; // disciplinas pendentes
    private Integer totalCHPendenteAlunoAtePeriodoAnterior; // disciplinas pendentes
    private Integer totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior;
    private Integer totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior;
    private Integer totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior;
    private Integer totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior;
    private Integer totalCHDisciplinasIncluidas;
    private Integer totalCreditosDisciplinasIncluidas;
    private Integer saldoCHDisponivelInclusaoDisplinas;
    private Integer saldoCreditoDisponivelInclusaoDisplinas;
    private Date dataAceitouTermoContratoRenovacaoOnline;
    private Date dataVencimentoMatriculaEspecifico;
//    private MatriculaPeriodoMensalidadeCalculadaVO matriculaPeriodoMatriculaCalculadaVO;
    /**
     * TRANSIENTE - VERSAO 5.0
     * UTILIZADO PARA MANTER A LISTA DE PERIODOS LETIVOS NOS QUAIS O ALUNO PODE
     * SER RENOVADO. ESTA LISTA É MONTADA CONFORME VARIOS PARAMETROS DA CONFIGURACAO
     * ACADEMICA DO CURSO. NO MÉTODO: obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao
     */
    private List<PeriodoLetivoVO> listaPeriodosLetivosValidosParaMatriculaPeriodo;
    /***
     * Transiente Plano Financeiro Aluno Utilizado na Ficha do Aluno
     */
//    public PlanoFinanceiroAlunoLogVO logPlanoFinanceiroAluno;
    
    /**
     * Atributo para controle na tela de inclusão fora do prazo
     * Autor Carlos
     */
    private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs;
	/**
	 * @author Leonardo Riciolle
	 * Atributo criado para o professor digitar uma observação vinculada ao critério de avaliação .
	 */
    private String observacaoCriterioAvaliacao;
	/**
	 * Inicio responsaveis por armazenar dados referente a inclusão fora do prazo.
	 */
	private Integer nrCreditoDiscplinasOptativasInclusaoForaPrazo;
	private Integer totalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo;
	private Integer nrCargaHorariaDiscplinasOptativasInclusaoForaPrazo;
	private Integer totalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo;
	private Integer nrDisciplinasIncluidasInclusaoForaPrazo;
	private Integer totalCHDisciplinasIncluidasInclusaoForaPrazo;
	private Integer totalCreditosDisciplinasIncluidasInclusaoForaPrazo;
	/**
	 * Atributo transient
	 * Utilizado para regra da tela de Mapa de Registro de Trancamento/Abandono de Curso
	 */
	private Boolean origemMapaRegistroTrancamentoAbandono;

	/**
	 * Atributo transient
	 * Utilizado para regra de ativação de uma matrícula Período Finalizada ou Formando tela renovarMatriculaCons.
	 */
	private Boolean ultimaMatriculaPeriodoAluno;
	
	private Double valorConvenioNaoRestituirAluno;
	

	private Boolean gravarContratoMatricula;
	private Boolean gravarContratoFiador;
	private Boolean gravarContratoExtensao;
	
	private String categoriaCondicaoPagamento;
	private List<HistoricoVO> listaHistoricoDesistenciaEquivalenciaVOs;
	
	private Boolean permitirVisualizarAbaDescontos;
	private Boolean vindoTelaAlteracaoPlanoFinanceiroAluno;
	private Boolean regerarDocumentoAssinado;
	
    /**
     * ATRIBUTO TRANSIENT utilizado para verificar o digito da turma da ultima matricula período do aluno.
     */
	private String digitoTurma;
	
	private List<DocumentoAssinadoVO> listaDocumentoAssinadoVO;
	
	
    /**
     * Construtor padrï¿½o da classe <code>MatriculaPeriodo</code>. Cria uma
     * nova instï¿½ncia desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public MatriculaPeriodoVO() {
        super();
        inicializarDados();
    }

    public static void validarProcessoMatriculaCalendario(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, boolean permitirMatriculaForaPrazo) throws Exception {
        CursoVO curso = matriculaVO.getCurso();
        if (!curso.utilizaProcessoMatriculaCalendario()) {
            // cursos como pós-graduação não precisam validar calendário letivo,
            // pois cada turma
            // possui seu proprio calendário letivo. Ou seja, uma turma começa a
            // qualquer momento,
            // e só é finalizada quando o semestre é concluído.
            return;
        }
        // if (matriculaPeriodoVO.isSituacaoAtiva()) {

        if (matriculaPeriodoVO.getProcessoMatricula() == null || matriculaPeriodoVO.getProcessoMatricula().intValue() == 0) {
            throw new ConsistirException("O campo PROCESSO DE MATRÍCULA (Matrícula) deve ser informado.");
        }
        if (matriculaPeriodoVO.getData() == null) {
            throw new ConsistirException("O campo DATA (Matrícula Período Letivo) deve ser informado.");
        }
        if (!matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().verificarDataEstaDentroPeriodoMatriculaValido(matriculaPeriodoVO.getData())) {
        	if (permitirMatriculaForaPrazo) {
        		if (!matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().verificarDataEstaDentroPeriodoMatriculaForaPrazoValido(matriculaPeriodoVO.getData())) {
					String mensagem = matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataInicioMatForaPrazo() == null
							&& matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataFinalMatForaPrazo() == null
									? "Não é possível realizar ou alterar uma matrícula fora do período definido no Calendário Letivo ("+ matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataInicioMatricula_Apresentar()
											+ " até " + matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataFinalMatricula_Apresentar() + ")"
									: "Não é possível realizar ou alterar uma matrícula fora dos períodos de matrículas (Período Matrícula e Período Mat. Fora do Prazo) definidos no Calendário Letivo ("
											+ matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataInicioMatricula_Apresentar()	+ " até " + 
									matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataFinalMatForaPrazo_Apresentar() + ").";
        			throw new ConsistirException(mensagem);
        		}
        	} else {
        		throw new ConsistirException("Não é possível realizar ou alterar uma matrícula fora do período definido no Calendário Letivo (" + matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataInicioMatricula_Apresentar() 
        					+ " até " + matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataFinalMatricula_Apresentar() + ").");
        	}
        }
        // }
        // if (matriculaPeriodoVO.isSituacaoPreMatricula()) {
        // if
        // (!matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().verificarProcessoMatriculaEstaAFrenteDataAtual(new
        // Date())) {
        // throw new
        // ConsistirException("Não é possível realizar ou alterar uma matrícula fora do período definido no Calendário Letivo ("
        // +
        // matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataInicioMatricula_Apresentar()
        // + " até " +
        // matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataFinalMatricula_Apresentar()
        // + ")");
        // }
        // }
    }

    public static void validarProcessoMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
        CursoVO curso = matriculaVO.getCurso();
        if (!curso.utilizaProcessoMatriculaCalendario()) {
            // cursos como pós-graduação não precisam validar calendário letivo,
            // pois cada turma
            // possui seu proprio calendário letivo. Ou seja, uma turma começa a
            // qualquer momento,
            // e só é finalizada quando o semestre é concluído.
            return;
        }
        if (matriculaPeriodoVO.getProcessoMatricula() == null || matriculaPeriodoVO.getProcessoMatricula().intValue() == 0) {
            throw new ConsistirException("O campo PROCESSO DE MATRÍCULA (Matrícula) deve ser informado.");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static void validarDados(MatriculaPeriodoVO obj) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Matrícula Período Letivo) deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Matrícula Período Letivo) deve ser informado.");
        }
        if ((obj.getPeridoLetivo() == null) || (obj.getPeridoLetivo().getCodigo() == null) || (obj.getPeridoLetivo().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PERÍODO LETIVO MATRÍCULA (Matrícula Período Letivo) deve ser informado.");
        }
        if ((obj.getResponsavelRenovacaoMatricula() == null) || (obj.getResponsavelRenovacaoMatricula().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL RENOVAÇÃO MATRÍCULA (Matrícula Período Letivo) deve ser informado.");
        }
        // if ((obj.getTurma() == null) ||
        // (obj.getTurma().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo TURMA (Matrícula Período Letivo) deve ser informado.");
        // }
        // if (!obj.getInclusaoForaPrazo().booleanValue()) {
        // if ((obj.getCondicaoPagamentoPlanoFinanceiroCurso() == null) ||
        // (obj.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo().intValue()
        // == 0)) {
        // throw new
        // ConsistirException("A CONDIÇÃO DE PAGAMENTO deve ser informada. Provavelmente não existe um Plano Financeiro Programado para este Curso ou Turma (Matrícula Período Letivo) deve ser informado.");
        // }
        // }

    }

    /**
     * deprecated 26/06/2014 - na versao 5.0
     */
    public static void validarVagaNaTurma(MatriculaPeriodoVO obj, Integer nrMatricula, TurmaVO turma) throws ConsistirException, Exception {
        if (obj.isSituacaoPreMatricula()) {
            // No caso de pré-matrícula, não temos que validar o número de vagas
            // na turma. Pois,
            // estamos somente garantindo a matrícula do aluno para o próximo
            // semestre.
            return;
        }

        if ((obj.getMatricula() == null) && (turma.getNrMaximoMatricula() <= nrMatricula)) {
            throw new ConsistirException("A Turma " + turma.getIdentificadorTurma() + " está completa, por favor selecione outra turma para matricular o aluno.");
        }
    }

    /**
     * Operaï¿½ï¿½o reponsï¿½vel por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setSituacao("PF");
        setSituacaoMatriculaPeriodo("PR");
        setDataLiberacaoMatricula(null);
        setDataEmissaoBoletoMatricula(null);
        //setContaReceber(0);
        setNrDocumento("");
        setUnidadeEnsinoCurso(0);
        setAno("");
        setSemestre("");
        setProcessoMatricula(0);
        setTranferenciaEntrada(0);
        setValorMatriculaCheio(0.0);
        setValorMensalidadeCheio(0.0);
        setValorMaterialDidaticoCheio(0.0);
        setValorDescontoMatricula(0.0);
        setValorDescontoMensalidade(0.0);
        setValorDescontoMaterialDidatico(0.0);
        setValorFinalMatricula(0.0);
        setValorFinalMensalidade(0.0);
        setValorFinalMaterialDidatico(0.0);
        setValorMatriculaBolsaCusteada(0.0);
        setValorMensalidadeBolsaCusteada(0.0);
        setValorMaterialDidaticoBolsaCusteada(0.0);
        setValorDescontoInstituicaoMatricula(0.0);
        setValorDescontoConvenioMatricula(0.0);
        setValorDescontoInstituicaoMensalidade(0.0);
        setValorDescontoConvenioMensalidade(0.0);
        setValorDescontoInstituicaoMaterialDidatico(0.0);
        setValorDescontoConvenioMaterialDidatico(0.0);
        setInclusaoForaPrazo(Boolean.FALSE);
        setNumParcelasInclusaoForaPrazo(0);
        setValorTotalParcelaInclusaoForaPrazo(new Double(0.0));
        setDiaVencimentoInclusaoForaPrazo(new Date());
        setErro(false);
        setApresentarSituacao(false);
        setMensagemErro("");
    }

    public void adicionarObjMatriculaPeriodoTurmaDisciplinaVOs(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
        MatriculaPeriodoTurmaDisciplinaVO.validarDados(obj);
        if (this.getCodigo().intValue() != 0) {
            obj.setMatriculaPeriodo(this.getCodigo());
        }
        int index = 0;
        Iterator i = getMatriculaPeriodoTumaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().intValue() == obj.getDisciplina().getCodigo().intValue()) {
                getMatriculaPeriodoTumaDisciplinaVOs().set(index, obj);
                return;
            }
            index++;
        }
        getMatriculaPeriodoTumaDisciplinaVOs().add(obj);
    }

    public void excluirObjMatriculaPeriodoTurmaDisciplinaVOs(Integer disciplinaPreMatriculaTurmaDisciplina) throws Exception {
        int index = 0;
        Iterator i = getMatriculaPeriodoTumaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().intValue() == disciplinaPreMatriculaTurmaDisciplina) {
                getMatriculaPeriodoTumaDisciplinaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    public void verificarDisciplinaASerAdicionadaTrataseDeUmaInclusaoDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
    	if ((Uteis.isAtributoPreenchido(obj.getGradeDisciplinaVO().getCodigo()) 
        		&& !this.getPeridoLetivo().getPeriodoLetivo().equals(obj.getGradeDisciplinaVO().getPeriodoLetivoVO().getPeriodoLetivo())) 
        		|| (!Uteis.isAtributoPreenchido(obj.getGradeDisciplinaVO().getCodigo()) && !this.getPeridoLetivo().getPeriodoLetivo().equals(obj.getTurma().getPeridoLetivo().getPeriodoLetivo())  )) {
            obj.setDisciplinaIncluida(true);
        }
        // tratar aqui no futuro se a disciplina é optativa...
    }

   

    public void adicionarObjMatriculaPeriodoVOs(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
        obj.setMatriculaPeriodo(this.getCodigo());

        MatriculaPeriodoTurmaDisciplinaVO.validarDados(obj);

        verificarDisciplinaASerAdicionadaTrataseDeUmaInclusaoDisciplina(obj);

        Boolean adicionouObjetoLista = false;
        int index = 0;
        Iterator i = getMatriculaPeriodoTumaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
                getMatriculaPeriodoTumaDisciplinaVOs().set(index, obj);
                adicionouObjetoLista = true;
                break;
            }
            index++;
        }
        if (!adicionouObjetoLista) {
            getMatriculaPeriodoTumaDisciplinaVOs().add(obj);
        }
    }

    public void adicionarObjMatriculaPeriodoVOsInclusaoForaPrazo(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
        obj.setMatriculaPeriodo(this.getCodigo());
        MatriculaPeriodoTurmaDisciplinaVO.validarDados(obj);
        obj.setDisciplinaIncluida(true);
        getMatriculaPeriodoTumaDisciplinaVOs().add(obj);
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por excluir um objeto da classe
     * <code>MatriculaPeriodoVO</code> no List <code>matriculaPeriodoVOs</code>.
     * Utiliza o atributo padrï¿½o de consulta da classe
     * <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() -
     * como identificador (key) do objeto no List.
     *
     * @param periodoLetivoMatricula
     *            Parï¿½metro para localizar e remover o objeto do List.
     */
    public void excluirObjMatriculaPeriodoVOs(Integer disciplina) throws Exception {
        int index = 0;
        Iterator i = getMatriculaPeriodoTumaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
                getMatriculaPeriodoTumaDisciplinaVOs().remove(index);
                return;
            }
            index++;
        }
    }

//    public void excluirObjMatriculaPeriodoVencimentoVOs(MatriculaPeriodoVencimentoVO vctoExcluir) throws Exception {
//        this.getMatriculaPeriodoVencimentoVOs().remove(vctoExcluir);
//    }

    public void excluirObjMatriculaPeriodoVOs(MatriculaPeriodoTurmaDisciplinaVO objExcluir) throws Exception {
        int index = 0;
        Iterator i = getMatriculaPeriodoTumaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(objExcluir.getDisciplina().getCodigo())) {
                // if (!objExcluir.getDisciplinaIncluida()) {
                // this.nrDisciplinasExcluidas++;
                // } else {
                // this.nrDisciplinasIncluidas--;
                // }
                getMatriculaPeriodoTumaDisciplinaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por consultar um objeto da classe
     * <code>MatriculaPeriodoVO</code> no List <code>matriculaPeriodoVOs</code>.
     * Utiliza o atributo padrï¿½o de consulta da classe
     * <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() -
     * como identificador (key) do objeto no List.
     *
     * @param periodoLetivoMatricula
     *            Parï¿½metro para localizar o objeto do List.
     */
    public MatriculaPeriodoTurmaDisciplinaVO consultarObjMatriculaPeriodoVO(Integer disciplina) throws Exception {
        Iterator i = getMatriculaPeriodoTumaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
                return objExistente;
            }
        }
        return null;
    }

    public void verificarAprovacaoPreRequisitos(Boolean livre) throws Exception {
        if (!livre) {
            throw new Exception("O Aluno não foi aprovados nos pré-requisitos desta disciplina.");
        }
    }

    public void verificarAprovacaoEmDisciplina(Integer disciplina) {
       // //System.out.println("teste");
    }

  
    public Boolean pagamentoMatriculaPeriodoLiberadaPorResponsavel() {
        if (getResponsavelLiberacaoMatricula().getCodigo().intValue() == 0) {
            return false;
        } else {
            return true;
        }
    }

    

  
    
   

   

//    public List criarContaReceberBolsaCusteadaMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,  ContaReceberVO contaReceberAlunoBaseGeracaoConvenio) throws Exception {
//        PlanoFinanceiroCursoVO planoFinCurso = this.getPlanoFinanceiroCurso();
//
//        List listaMatriculaBolsaCusteada = new ArrayList();
//
//        for (ItemPlanoFinanceiroAlunoVO obj : (List<ItemPlanoFinanceiroAlunoVO>) matriculaVO.getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs()) {
//            if (obj.getTipoItemPlanoFinanceiro().equals("CO")) {
//                if (obj.getRegerarConta()) {
//                    if (obj.getConvenio().getBolsaCusteadaParceiroMatricula().doubleValue() != 0.0) {
//                        if (contaReceberAlunoBaseGeracaoConvenio != null) {
//                            // se contaReceberAlunoBaseGeracaoConvenio foi fornecida para este método
//                            // o valor do convenio, já está calculado, considerando todas as regras
//                            // dentro desta contaAReceber. Logo, basta obte-lo de lá. Este processo
//                            // de obter o valor do convenio da contaReceberBase é mais seguro, pois
//                            // na conta a receber o calculo do convenio já considera diversas varíaveis
//                            // como base de calculo do convenio, ordem de aplicado do mesmo e outras 
//                            // variaveis
//                            boolean encontrouConvenio = false;
//                            for (PlanoDescontoContaReceberVO planoDesconto : contaReceberAlunoBaseGeracaoConvenio.getPlanoDescontoContaReceberVOs()) {
//                                if ((planoDesconto.getIsConvenio())
//                                        && (planoDesconto.getConvenio().getCodigo().equals(obj.getConvenio().getCodigo()))) {
//                                    // Se o plano de desconto refere-se a convenio e trata-se do mesmo convenio que estamos
//                                    // processando para gerar a conta a receber correspondente do mesmo. Então vamos obter
//                                    // o valor correspondente deste convenio para esta contaAReceber. Este valor pode sofrer
//                                    // interferencias de planos de descontos e descontos progressivos, por isto,  temos
//                                    // que obte-lo diretamente da conta a receber base, assim o valor do plano é calculado
//                                    // de forma precisa.
//                                    setValorMatriculaBolsaCusteada(planoDesconto.getValorUtilizadoRecebimento());
//                                    encontrouConvenio = true;
//                                    break;
//                                }
//                            }
//                            if (!encontrouConvenio) {
//                                throw new Exception("Ocorreu um erro ao tentar gerar a parcela de Matrícula do Convênio " + obj.getConvenio().getDescricao() + " desta Matrícula " + matriculaVO.getMatricula() + ". Procure o administador do sistema.");
//                            }
//                        } else {
//                            throw new Exception("Ocorreu um erro ao tentar gerar a parcela de Matrícula do Convênio " + obj.getConvenio().getDescricao() + " desta Matrícula " + matriculaVO.getMatricula() + ". Não foi possível determinar a base de cálculo do mesmo. Procure o administador do sistema.");
//                        }
//
//                        // Método comentado na versão 5.0, pois não é correto calcular o convenio somente sobre o valor cheio da conta a receber
//                        // agora existe a possibilidade de um convenio ser calculado sobre o valor liquido da conta a receber.
//                        // if (obj.getConvenio().getTipoBolsaCusteadaParceiroMatricula().equals("VA")) {
//                        //setValorMatriculaBolsaCusteada(obj.getConvenio().getBolsaCusteadaParceiroMatricula());
//                        //} else {
//                        //Double valorBaseConvenio = matriculaPeriodoVO.getValorMatriculaCheio();
//                        //setValorMatriculaBolsaCusteada(Uteis.arrendondarForcando2CadasDecimais(valorBaseConvenio * (obj.getConvenio().getBolsaCusteadaParceiroMatricula() / 100.0)));
//                        //}
//                        Double valorMatriculaConvenio = getValorMatriculaBolsaCusteada();
//                        if (obj.getConvenio().getTipoBolsaCusteadaParceiroMatricula().equals("VA")) {
//                            valorMatriculaConvenio = obj.getConvenio().getBolsaCusteadaParceiroMatricula();
//                        }
//                        listaMatriculaBolsaCusteada.add(montarMatriculaBolsaCusteada(planoFinCurso,  matriculaVO,
//                                matriculaPeriodoVO, processoMatriculaCalendarioVO, valorMatriculaConvenio, 0.0, 0, obj.getConvenio().getCodigo(), 0.0, 0.0, obj.getConvenio().getParceiro()));
//                    }
//                    setValorMatriculaBolsaCusteada(0.0);
//                }
//            }
//        }
//        return listaMatriculaBolsaCusteada;
//    }

    

//	public List criarContaReceberBolsaCusteadaMensalidade( MatriculaVO matriculaVO,
//			MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO,
//			MatriculaPeriodoVencimentoVO vctoGerar) throws Exception {
//		CondicaoPagamentoPlanoFinanceiroCursoVO condicao = this.getCondicaoPagamentoPlanoFinanceiroCurso();
//		PlanoFinanceiroCursoVO planoCursoVO = this.getPlanoFinanceiroCurso();
//		List listaMensalidadesBolsaCusteada = new ArrayList();
//
//			for (ItemPlanoFinanceiroAlunoVO obj : (List<ItemPlanoFinanceiroAlunoVO>) matriculaVO.getPlanoFinanceiroAluno()
//					.getItemPlanoFinanceiroAlunoVOs()) {
//
//				if (obj.getTipoItemPlanoFinanceiro().equals("CO")) {
//
//					if (obj.getRegerarConta()) {
//
//					if (obj.getConvenio().getTipoBolsaCusteadaParceiroParcela().equals("VA")) {
//						setValorMensalidadeBolsaCusteada(obj.getConvenio().getBolsaCusteadaParceiroParcela());
//					} else {
//						// if
//						// (matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenioValorCheio())
//						// {
//						setValorMensalidadeBolsaCusteada((vctoGerar.getValor() * obj.getConvenio().getBolsaCusteadaParceiroParcela()) / 100.0);
//						// } else {
//						// setValorMensalidadeBolsaCusteada((getValorBaseDescontoConvenioMensalidade()
//						// * obj.getConvenio().getBolsaCusteadaParceiroParcela()) /
//						// 100.0);
//						// }
//					}
//					Double desconto = 0.0;
//					Integer descontoProgressivo = 0;
//
//					if (getValorMensalidadeBolsaCusteada() - desconto > 0) {
//						// int qtdeMensalidades = 1;
//						// while (qtdeMensalidades <=
//						// condicao.getNrParcelasPeriodo().intValue()) {
//						listaMensalidadesBolsaCusteada.add(montarMensalidadeBolsaCusteada(planoCursoVO, configFinan, matriculaVO, matriculaPeriodoVO,
//								processoMatriculaCalendarioVO, vctoGerar.getParcela(), getValorMensalidadeBolsaCusteada(), desconto,
//								descontoProgressivo, obj.getConvenio().getCodigo(), 0.0, 0.0, condicao, obj.getConvenio().getParceiro()));
//						// qtdeMensalidades++;
//						// }
//					}
//					setValorMensalidadeBolsaCusteada(0.0);
//					}
//				}
//			}
//
//		return listaMensalidadesBolsaCusteada;
//	}
//    public ContaReceberVO montarMensalidade(PlanoFinanceiroCursoVO planoCursoVO,  MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, int nrMensalidade, Double valor, Double desconto, Integer descontoProgressivo, Integer convenio, Double descontoFaculdade,
//            Double descontoConvenio, CondicaoPagamentoPlanoFinanceiroCursoVO condicao) throws Exception {
//        ContaReceberVO obj = new ContaReceberVO();
//        String parcela = String.valueOf(nrMensalidade) + "/" + String.valueOf(condicao.getNrParcelasPeriodo());
//        String descricao = ("Taxa de Mensalidade do curso " + matriculaVO.getCurso().getNome().toUpperCase() + ", período " + matriculaVO.getTurno().getNome().toUpperCase());
//        String nrDoc = ((String.valueOf(matriculaVO.getUnidadeEnsino().getCodigo()) + "." + getCodigo() + "." + nrMensalidade));
//
//        Date dataVencimento = new MatriculaPeriodo().montarDataVencimento(nrMensalidade, matriculaPeriodoVO.getData(), processoMatriculaCalendarioVO);
//
//        String tipoDesconto = "PO";
//        if (!matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela().equals("PO")) {
//            tipoDesconto = "VE";
//        }
//        Double percDesconto = matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela();
//        Double valorDesconto = matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela();
//        obj = montarDadosContaReceber(configuracaoFinanceiro.getCentroReceitaMensalidadePadrao().getCodigo(), configuracaoFinanceiro.getContaCorrentePadraoMensalidade(), "MEN", matriculaVO, nrDoc, descricao, parcela, valor, tipoDesconto, valorDesconto, percDesconto, dataVencimento, descontoProgressivo, convenio, descontoFaculdade, descontoConvenio, matriculaPeriodoVO.getCodigo(), null,
//                matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenio(), matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenioValorCheio(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoAluno(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoAlunoValorCheio(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoProgressivo(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoProgressivoValorCheio(), matriculaVO.getPlanoFinanceiroAluno().getOrdemPlanoDesconto(), matriculaVO.getPlanoFinanceiroAluno().getOrdemPlanoDescontoValorCheio());
//        obj.adicionarListaPlanoDescontoContaReceberVOComBaseItensPlanoDescontoMatricula(matriculaVO.getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs());
//
//        return obj;
//    }
//    public ContaReceberVO montarMensalidadeBolsaCusteada(PlanoFinanceiroCursoVO planoCursoVO,  MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, String parcela, Double valor, Double desconto, Integer descontoProgressivo, Integer convenio, Double descontoFaculdade,
//            Double descontoConvenio,  ParceiroVO parceiroVO) throws Exception {
//        ContaReceberVO obj = new ContaReceberVO();
//        // String parcela = String.valueOf(nrMensalidade) + "/" +
//        // String.valueOf(condicao.getNrParcelasPeriodo());
//        String descricao = ("Bolsa Custeada referente a Mensalidade do curso " + matriculaVO.getCurso().getNome().toUpperCase() + ", período " + matriculaVO.getTurno().getNome().toUpperCase() + " referente ao aluno " + matriculaVO.getAluno().getNome());
//        int indice = parcela.indexOf("/");
//        String nrMensalidadeStr = "";
//        if (indice > 0) {
//            nrMensalidadeStr = parcela.substring(0, indice);
//        }
//        int nrMensalidade = Integer.valueOf(nrMensalidadeStr);
//        String nrDoc = ((String.valueOf(matriculaVO.getUnidadeEnsino().getCodigo()) + "." + getCodigo() + "." + nrMensalidade));
//        Date dataVencimento = new MatriculaPeriodo().montarDataVencimento(nrMensalidade, matriculaPeriodoVO.getData(), processoMatriculaCalendarioVO);
//        obj = montarDadosContaReceber(configuracaoFinanceiro.getCentroReceitaMensalidadePadrao().getCodigo(), configuracaoFinanceiro.getContaCorrentePadraoMensalidade(), "BCC", matriculaVO, nrDoc, descricao, parcela, valor, "VE", desconto, 0.0, dataVencimento, descontoProgressivo, convenio, descontoFaculdade, descontoConvenio, matriculaPeriodoVO.getCodigo(), parceiroVO, matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenio(), matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenioValorCheio(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoAluno(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoAlunoValorCheio(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoProgressivo(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoProgressivoValorCheio(), matriculaVO.getPlanoFinanceiroAluno().getOrdemPlanoDesconto(), matriculaVO.getPlanoFinanceiroAluno().getOrdemPlanoDescontoValorCheio());
//        return obj;
//
//        // String nrDoc =
//        // (String.valueOf(matriculaVO.getUnidadeEnsino().getCodigo()) + "." +
//        // String.valueOf(matriculaVO.getInscricao().getCodigo()) + "." +
//        // getCodigo());
//        // setNrDocumento(nrDoc);
//        // String descricao = ("Mensalidade " + parcelaMatricula.getParcela() +
//        // " para o curso " + matriculaVO.getCurso().getNome().toUpperCase() +
//        // ", período " + matriculaVO.getTurno().getNome().toUpperCase());
//        // descricao = descricao + '\n' +
//        // parcelaMatricula.getDescricaoPagamento();
//        // Date dataVencimento = parcelaMatricula.getDataVencimento();
//        // Double valor = parcelaMatricula.getValor();
//        // Double valorDesconto = parcelaMatricula.getValorDesconto();
//        // Double descontoInstituicao =
//        // parcelaMatricula.getValorDescontoInstituicao();
//        // Double descontoConvenio =
//        // parcelaMatricula.getValorDescontoConvenio();
//        // Integer descontoProgressivo = 0;
//        // descontoProgressivo =
//        // matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo().getCodigo();
//
//    }
    /**
     * responsï¿½vel para definir valor final da Matricula do Curso aluno
     *
     * @param valorMatricula
     * @param valorDescontoMatricula
     */
//    public void calcularValorMatricula(Double valorMatricula, Double valorDescontoMatricula) {
//        setValorFinalMatricula(valorMatricula - valorDescontoMatricula);
//    }
//
//    /**
//     * responsï¿½vel para definir valor final da Mensalidade do Curso aluno
//     *
//     * @param valorMatricula
//     * @param valorDescontoMatricula
//     */
//    public void calcularValorMensalidade(Double valorMensalidade, Double valorDescontoMensalidade) {
//        setValorFinalMensalidade(valorMensalidade - valorDescontoMensalidade);
//    }
//    
//    public void calcularValorMaterialDidatico(Double valorMaterialDidatico, Double valorDescontoMaterialDidatico) {
//        setValorFinalMaterialDidatico(valorMaterialDidatico - valorDescontoMaterialDidatico);
//    }

    /**
     * Responsï¿½vel para definir qual serï¿½ o valor Cheio da matricula do
     * Curso do Aluno
     *
     * @param planoCurso
     * @return
     * @throws Exception
     */
//    public Double valorMatriculaCurso(PlanoFinanceiroCursoVO planoCurso, CondicaoPagamentoPlanoFinanceiroCursoVO condicao) throws Exception {
//        if (condicao.utilizaValorMatriculaMensalidadeFixo()) {
//            // if (!planoCurso.getPlanoDescontoPadrao().getCodigo().equals(new
//            // Integer(0))) {
//            // valorFinal = planoCurso.getValorMatricula() -
//            // (planoCurso.getValorMatricula() *
//            // (planoCurso.getPlanoDescontoPadrao().getPercDescontoMatricula())
//            // / 100);
//            // } else {
//            setValorMatriculaCheio(condicao.getValorMatricula());
//            // }
//        } else {
//            setValorMatriculaCheio(condicao.getValorMatriculaSistemaPorCredito());
//        }
//        calcularValorMatricula(getValorMatriculaCheio(), getValorDescontoMatricula());
//        return getValorMatriculaCheio();
//    }

    /**
     * Responsï¿½vel para definir qual serï¿½ o valor Cheio da mensalidade do
     * Curso do Aluno
     *
     * @param planoCurso
     * @param gradeDisciplina
     * @return
     * @throws Exception
     */
//    public Double valorMensalidadeCurso(PlanoFinanceiroCursoVO planoCurso, List gradeDisciplina, List<TransferenciaEntradaDisciplinasAproveitadasVO> listaDisc, CondicaoPagamentoPlanoFinanceiroCursoVO condicao) throws Exception {
//        if (condicao.utilizaValorMatriculaMensalidadeFixo()) {
//            setValorMensalidadeCheio(condicao.getValorParcela());
//        } else {
//            Integer totalCreditos = 0;
//            Iterator i = gradeDisciplina.iterator();
//            boolean somarCredito = true;
//            while (i.hasNext()) {
//                GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
//                for (TransferenciaEntradaDisciplinasAproveitadasVO discAprov : listaDisc) {
//                    if (obj.getDisciplina().getCodigo().intValue() == discAprov.getDisciplina().getCodigo().intValue()) {
//                        somarCredito = false;
//                    }
//                }
//                if (somarCredito) {
//                    totalCreditos = totalCreditos + obj.getNrCreditos();
//                }
//                somarCredito = true;
//            }
//            setValorMensalidadeCheio(condicao.getValorUnitarioCredito() * totalCreditos);
//            if (getValorMensalidadeCheio() <= condicao.getValorMinimoParcelaSistemaPorCredito()) {
//                setValorMensalidadeCheio(condicao.getValorMinimoParcelaSistemaPorCredito());
//            }
//        }
//        calcularValorMensalidade(getValorMensalidadeCheio(), getValorDescontoMensalidade());
//        return getValorMensalidadeCheio();
//    }

   

    
   

    

    /**
     * Responsï¿½vel para retornar a data de vencimento da Matricula ou
     * Mensalidade ou sej a da conta a receber
     *
     * @param planoFinanceiroAlunoVO
     * @return
     */
//    public Date montarDataVencimento(Integer nrMensalidade, Date dataMatricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) throws Exception {
//        String diaVencimento = String.valueOf(processoMatriculaCalendarioVO.getDiaVencimentoPrimeiraMensalidade());
//        Integer anoVencimento = (processoMatriculaCalendarioVO.getAnoVencimentoPrimeiraMensalidade());
//        Integer mesVencimento = (processoMatriculaCalendarioVO.getMesVencimentoPrimeiraMensalidade());
//        String dataFinal = "";
//        if (nrMensalidade.intValue() == 1) {
//            if (processoMatriculaCalendarioVO.getMesSubsequenteMatricula()) {
//                Date novaDataFinal = montarDadosDataVencimentoMesSubsequenteMatricula(nrMensalidade, diaVencimento, dataMatricula);
//                return novaDataFinal;
//            } else {
//                dataFinal = diaVencimento + "/" + String.valueOf(mesVencimento) + "/" + String.valueOf(anoVencimento);
//                Date novaDataFinal = getNextWorkingDay(Uteis.getDate(dataFinal), null);
//                //String novaDataFinal = consultarDiaUtilSemanaGerarVencimento(dataFinal, diaVencimento, mesVencimento, anoVencimento);
//                return novaDataFinal;
//            }
//        } else {
//            if (processoMatriculaCalendarioVO.getMesSubsequenteMatricula()) {
//                Date novaDataFinal = montarDadosDataVencimentoMesSubsequenteMatricula(nrMensalidade, diaVencimento, dataMatricula);
//                return novaDataFinal;
//            } else {
//                mesVencimento = mesVencimento + nrMensalidade - 1;
//                if (mesVencimento > 12) {
//                    anoVencimento = anoVencimento + new Integer(1);
//                    mesVencimento = mesVencimento - 12;
//                }
//                dataFinal = diaVencimento + "/" + String.valueOf(mesVencimento) + "/" + String.valueOf(anoVencimento);
//                Date novaDataFinal = getNextWorkingDay(Uteis.getDate(dataFinal), null);
//                //String novaDataFinal = consultarDiaUtilSemanaGerarVencimento(dataFinal, diaVencimento, mesVencimento, anoVencimento);
//                return novaDataFinal;
//            }
//        }
//    }
//
//    public Date montarDadosDataVencimentoMesSubsequenteMatricula(Integer nrMensalidade, String diaVencimento, Date dataMatricula) throws Exception {
//        String dataFinal = "";
//        Integer mesVencimentoSubsequenteMatricula = Uteis.getMesDataVencimento(dataMatricula);
//        Integer anoVencimentoSubsequenteMatricula = Uteis.getAnoData(dataMatricula);
//        if (nrMensalidade.intValue() == 1) {
//            dataFinal = diaVencimento + "/" + String.valueOf(mesVencimentoSubsequenteMatricula) + "/" + String.valueOf(anoVencimentoSubsequenteMatricula);
//            Date novaDataFinal = getNextWorkingDay(Uteis.getDate(dataFinal), null);
//            //String novaDataFinal = consultarDiaUtilSemanaGerarVencimento(dataFinal, diaVencimento, mesVencimentoSubsequenteMatricula, anoVencimentoSubsequenteMatricula);
//            return novaDataFinal;
//        } else {
//            mesVencimentoSubsequenteMatricula = mesVencimentoSubsequenteMatricula + nrMensalidade - 1;
//            if (mesVencimentoSubsequenteMatricula > 12) {
//                anoVencimentoSubsequenteMatricula = anoVencimentoSubsequenteMatricula + new Integer(1);
//                mesVencimentoSubsequenteMatricula = mesVencimentoSubsequenteMatricula - 12;
//            }
//            dataFinal = diaVencimento + "/" + String.valueOf(mesVencimentoSubsequenteMatricula) + "/" + String.valueOf(anoVencimentoSubsequenteMatricula);
//            Date novaDataFinal = getNextWorkingDay(Uteis.getDate(dataFinal), null);
//            //String novaDataFinal = consultarDiaUtilSemanaGerarVencimento(dataFinal, diaVencimento, mesVencimentoSubsequenteMatricula, anoVencimentoSubsequenteMatricula);
//            return novaDataFinal;
//        }
//    }
//    public String consultarDiaUtilSemanaGerarVencimento(String dataFinal, String diaVencimento, Integer mesVencimento, Integer anoVencimento) throws Exception {
//        String novaDataFinal = "";
//        if (Uteis.getDiaSemana(Uteis.getDate(dataFinal)) == 7) {
//            Integer diaUtilAposFeriado = Uteis.getDiaDataVencimentoFeriadoSabado(Uteis.getDate(dataFinal));
//            if (consultarFeriadoVencimentoParcelas(diaUtilAposFeriado.toString(), mesVencimento)) {
//                diaUtilAposFeriado = Uteis.getDiaDataVencimentoSomarUmDia(Uteis.getDate(dataFinal));
//                return novaDataFinal = String.valueOf(diaUtilAposFeriado) + "/" + String.valueOf(mesVencimento) + "/" + String.valueOf(anoVencimento);
//            }
//            return novaDataFinal = String.valueOf(diaUtilAposFeriado) + "/" + String.valueOf(mesVencimento) + "/" + String.valueOf(anoVencimento);
//
//        } else if (Uteis.getDiaSemana(Uteis.getDate(dataFinal)) == 1) {
//            Integer diaUtilAposFeriado = Uteis.getDiaDataVencimentoFeriadoDomingo(Uteis.getDate(dataFinal));
//            if (consultarFeriadoVencimentoParcelas(diaUtilAposFeriado.toString(), mesVencimento)) {
//                diaUtilAposFeriado = Uteis.getDiaDataVencimentoSomarUmDia(Uteis.getDate(dataFinal));
//                return novaDataFinal = String.valueOf(diaUtilAposFeriado) + "/" + String.valueOf(mesVencimento) + "/" + String.valueOf(anoVencimento);
//            }
//            return novaDataFinal = String.valueOf(diaUtilAposFeriado) + "/" + String.valueOf(mesVencimento) + "/" + String.valueOf(anoVencimento);
//        }
//
//        if (consultarFeriadoVencimentoParcelas(diaVencimento, mesVencimento)) {
//            Integer diaUtilAposFeriado = Uteis.getDiaDataVencimentoSomarUmDia(Uteis.getDate(dataFinal));
//            return novaDataFinal = String.valueOf(diaUtilAposFeriado) + "/" + String.valueOf(mesVencimento) + "/" + String.valueOf(anoVencimento);
//
//        }
//        return dataFinal;
//    }
//
//    public Boolean consultarFeriadoVencimentoParcelas(String diaVencimento, Integer mesVencimento) throws Exception {
//        String dataFeriado = "";
//        Integer anoVencimento = 1970;
//        dataFeriado = diaVencimento + "/" + String.valueOf(mesVencimento) + "/" + String.valueOf(anoVencimento);
//        List listaFeriado = new Feriado().consultarPorData(Uteis.getDate(dataFeriado), false, Uteis.NIVELMONTARDADOS_TODOS);
//        if (listaFeriado.isEmpty()) {
//            return false;
//        }
//        return true;
//    }
    

//    public List criarHistoricoAproveitamentoDisciplina(AproveitamentoDisciplinaVO obj) {
//        List listaHistorico = new ArrayList();
//        Iterator i = obj.getDisciplinasAproveitadasVOs().iterator();
//        while (i.hasNext()) {
//            DisciplinasAproveitadasVO objItem = (DisciplinasAproveitadasVO) i.next();
//            listaHistorico.add(historicoVO);
//
//        }
//
//        return listaHistorico;
//    }
//    public HistoricoVO criarHistoricoCH(ConcessaoCargaHorariaDisciplinaVO objItem, UsuarioVO responsavel, Integer codCurso, String situacao) {
//        HistoricoVO historicoVO = new HistoricoVO();
//        historicoVO.setDataRegistro(new Date());
//        historicoVO.getDisciplina().setCodigo(objItem.getDisciplinaVO().getCodigo());
//        historicoVO.getDisciplina().setNome(objItem.getDisciplinaVO().getNome());
//        historicoVO.setFreguencia(100.0);
//        historicoVO.getMatricula().setMatricula(matricula);
//        historicoVO.getMatriculaPeriodo().setCodigo(getCodigo());
//        historicoVO.setAnoHistorico(objItem.getAno());
//        historicoVO.setSemestreHistorico(objItem.getSemestre());
//        historicoVO.setMediaFinal(0.0);
//        historicoVO.setInstituicao("");
//        historicoVO.setSituacao(situacao);
//        historicoVO.setTipoHistorico("NO");
//        historicoVO.setResponsavel(responsavel);
//        historicoVO.setCargaHorariaCursada(objItem.getQtdeCargaHorariaConcedido());
//        try {
//            historicoVO.setConfiguracaoAcademico(new ConfiguracaoAcademico().consultarPorDisciplinaMatriculaPeriodoAlunoVinculadoGradeDisciplina(historicoVO.getDisciplina().getCodigo(), getCodigo(), null));
//            if (historicoVO.getConfiguracaoAcademico().getCodigo().intValue() == 0) {
//                historicoVO.setConfiguracaoAcademico(new ConfiguracaoAcademico().consultarPorCodigoCurso(codCurso, null));
//            }
//        } catch (Exception e) {
//        }
//
//        return historicoVO;
//    }
//
//    public HistoricoVO criarHistoricoCC(ConcessaoCreditoDisciplinaVO objItem, UsuarioVO responsavel, Integer codCurso, String situacao) {
//        HistoricoVO historicoVO = new HistoricoVO();
//        historicoVO.setDataRegistro(new Date());
//        historicoVO.getDisciplina().setCodigo(objItem.getDisciplinaVO().getCodigo());
//        historicoVO.getDisciplina().setNome(objItem.getDisciplinaVO().getNome());
//        historicoVO.setFreguencia(100.0);
//        historicoVO.getMatricula().setMatricula(matricula);
//        historicoVO.getMatriculaPeriodo().setCodigo(getCodigo());
//        historicoVO.setAnoHistorico(objItem.getAno());
//        historicoVO.setSemestreHistorico(objItem.getSemestre());
//        historicoVO.setMediaFinal(0.0);
//        historicoVO.setInstituicao("");
//        historicoVO.setSituacao(situacao);
//        historicoVO.setTipoHistorico("NO");
//        historicoVO.setResponsavel(responsavel);
//        historicoVO.setCargaHorariaCursada(objItem.getQtdeCreditoConcedido());
//        try {
//            historicoVO.setConfiguracaoAcademico(new ConfiguracaoAcademico().consultarPorDisciplinaMatriculaPeriodoAlunoVinculadoGradeDisciplina(historicoVO.getDisciplina().getCodigo(), getCodigo(), null));
//            if (historicoVO.getConfiguracaoAcademico().getCodigo().intValue() == 0) {
//                historicoVO.setConfiguracaoAcademico(new ConfiguracaoAcademico().consultarPorCodigoCurso(codCurso, null));
//            }
//        } catch (Exception e) {
//        }
//
//        return historicoVO;
//    }
    public UsuarioVO getResponsavelRenovacaoMatricula() {
        if (responsavelRenovacaoMatricula == null) {
            responsavelRenovacaoMatricula = new UsuarioVO();
        }
        return responsavelRenovacaoMatricula;
    }

    public void setResponsavelRenovacaoMatricula(UsuarioVO responsavelRenovacaoMatricula) {
        this.responsavelRenovacaoMatricula = responsavelRenovacaoMatricula;
    }

    public UsuarioVO getResponsavelMatriculaForaPrazo() {
        if (responsavelMatriculaForaPrazo == null) {
            responsavelMatriculaForaPrazo = new UsuarioVO();
        }
        return responsavelMatriculaForaPrazo;
    }

    public void setResponsavelMatriculaForaPrazo(UsuarioVO responsavelMatriculaForaPrazo) {
        this.responsavelMatriculaForaPrazo = responsavelMatriculaForaPrazo;
    }

    public PeriodoLetivoVO getPeriodoLetivo() {
        return getPeridoLetivo();
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>MatriculaPeriodo</code>).
     */
    public PeriodoLetivoVO getPeridoLetivo() {
        if (peridoLetivo == null) {
            peridoLetivo = new PeriodoLetivoVO();
        }
        return peridoLetivo;
    }

    public void setPeridoLetivo(PeriodoLetivoVO peridoLetivo) {
        this.peridoLetivo = peridoLetivo;
    }

    public GradeCurricularVO getGradeCurricular() {
        if (gradeCurricular == null) {
            gradeCurricular = new GradeCurricularVO();
        }
        return gradeCurricular;
    }

    public void setGradeCurricular(GradeCurricularVO gradeCurricular) {
        this.gradeCurricular = gradeCurricular;
    }

    @XmlElement(name = "matricula")
    public String getMatricula() {
        return (matricula);
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Boolean getSituacaoPreMatricula() {
        return isSituacaoPreMatricula();
    }

    public Boolean isSituacaoPreMatricula() {
        if (getSituacaoMatriculaPeriodo().equals("PR")) {
            return true;
        }
        return false;
    }

    public Boolean getSituacaoAtiva() {
        return isSituacaoAtiva();
    }

    public Boolean isSituacaoAtiva() {
        if (getSituacaoMatriculaPeriodo().equals("AT")) {
            return true;
        }
        return false;
    }

    public Boolean isSituacaoFinalizada() {
        if (situacaoMatriculaPeriodo.equals("FI")) {
            return true;
        }
        return false;
    }

    public boolean isPreMatricula() {
        if (situacaoMatriculaPeriodo.equals("PR")) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isTrancada() {
    	return getSituacaoMatriculaPeriodo().equals("TR");
    }

    public String getSituacaoMatriculaPeriodo_Apresentar() {
//        String situacaoMatriculaPrincipal = "";
    	String matrizIntegralizada ="";
    	if(this.getMatriculaVO().getAlunoConcluiuDisciplinasRegulares()) {
    		matrizIntegralizada= " INTEGRALIZADO - ";
    	}
    	
        if (this.getMatriculaVO() != null) {
        	if (getSituacaoMatriculaPeriodo().equals("TR") || getSituacaoMatriculaPeriodo().equals("AC") || getSituacaoMatriculaPeriodo().equals("CA")
        			|| getSituacaoMatriculaPeriodo().equals("TS") || getSituacaoMatriculaPeriodo().equals("TI") || getSituacaoMatriculaPeriodo().equals("PC")) {
        		if (getAlunoTransferidoUnidade()) {
                    return matrizIntegralizada+"Transferido de unidade";
                }        		
				return matrizIntegralizada+SituacaoMatriculaPeriodoEnum.getEnumPorValor(getSituacaoMatriculaPeriodo()).getDescricao();
        	}
//            if ((!this.getMatriculaVO().getIsAtiva()) && (!this.getMatriculaVO().getIsPreMatricula()) && getSituacaoMatriculaPeriodo().equals("TR")) {
//                return "Trancada - Finalizado";
//            } else {
            if ((!this.getMatriculaVO().getIsAtiva()) && (!this.getMatriculaVO().getIsPreMatricula())) {
                if (getAlunoTransferidoUnidade()) {
                    return matrizIntegralizada+ "Transferido de unidade";
                }
                if (this.getMatriculaVO().getSituacao().equals(SituacaoVinculoMatricula.FORMADO.getValor())) {
                    return matrizIntegralizada+ this.getMatriculaVO().getSituacao_Apresentar() + " - Finalizado";
                }
                if (this.getMatriculaVO().getSituacao().equals(SituacaoVinculoMatricula.INATIVA.getValor())) {
                    return matrizIntegralizada+ this.getMatriculaVO().getSituacao_Apresentar();
                }
                if (this.getMatriculaVO().getSituacao().equals(SituacaoVinculoMatricula.INDEFINIDA.getValor())) {
                    return matrizIntegralizada+ this.getMatriculaVO().getSituacao_Apresentar();
                }
//                } else {
//                    situacaoMatriculaPrincipal = " [" + this.getMatriculaVO().getSituacao_Apresentar() + "]";
//                }
            }
//
        }
//        if (situacaoMatriculaPeriodo.equals("PR")) {
//            return "Pré-matrícula";
//        }
//        if (situacaoMatriculaPeriodo.equals("AT")) {
//            return "Ativa";
//        }
//        if (situacaoMatriculaPeriodo.equals("FI")) {
//            return "Concluído" + situacaoMatriculaPrincipal;
//        }
//        if (situacaoMatriculaPeriodo.equals("TR")) {
//            return "Trancada" + situacaoMatriculaPrincipal;
//        }
//        if (situacaoMatriculaPeriodo.equals("PC")) {
//        	return "Pré-Matricula Cancelada";
//        }
        if (this.getMatriculaVO().getSituacao().equals(SituacaoVinculoMatricula.CANCELADA_FINANCEIRO.getValor())) {
            return matrizIntegralizada+ SituacaoMatriculaPeriodoEnum.getEnumPorValor(getSituacaoMatriculaPeriodo()).getDescricao() + " - (" + SituacaoVinculoMatricula.CANCELADA_FINANCEIRO.getDescricao() + ")";
        }
        if (getSituacaoMatriculaPeriodo().trim().isEmpty()) {
            return matrizIntegralizada+ "Situação Não Localizada";
        }
        return matrizIntegralizada+ SituacaoMatriculaPeriodoEnum.getEnumPorValor(getSituacaoMatriculaPeriodo()).getDescricao();
//        return "";
    }

    public String getSituacaoMatriculaPeriodo() {
        if (situacaoMatriculaPeriodo == null) {
            situacaoMatriculaPeriodo = "";
        }
        return situacaoMatriculaPeriodo;
    }

    public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
        this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
    }

    public String getSituacao() {
        return (situacao);
    }

    /**
     * Situação responsável por manter a situação da Matrícula Período sobre a
     * ótica financeira. Assim, ela pode estar pendente financeiramente
     * (matrícula ainda não foi paga), pendente de liberação (foi concedido um
     * desconto por exemplo, mas é necessário que algum gestor libere este
     * desconto para que ela possa ser efetivada) e CONFIRMADA OU ATIVA que
     * significa que a matrícula foi paga e está ativa para início das
     * atividades.
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("PF")) {
            return "Pendente Financeiramente";
        }
        if (situacao.equals("PL")) {
            return "Pendente de Liberação";
        }
        if (situacao.equals("TR")) {
            return "Trancada";
        }
        if ((situacao.equals("CO")) || (situacao.equals("AT"))) {
            return "Confirmada";
        }
        return (situacao);
    }

//    public Integer getContaReceber() {
//        return contaReceber;
//    }
//
//    public void setContaReceber(Integer contaReceber) {
//        this.contaReceber = contaReceber;
//    }
    public Date getDataEmissaoBoletoMatricula() {
        return dataEmissaoBoletoMatricula;
    }

    public void setDataEmissaoBoletoMatricula(Date dataEmissaoBoletoMatricula) {
        this.dataEmissaoBoletoMatricula = dataEmissaoBoletoMatricula;
    }

    public Date getDataLiberacaoMatricula() {
        return dataLiberacaoMatricula;
    }

    public void setDataLiberacaoMatricula(Date dataLiberacaoMatricula) {
        this.dataLiberacaoMatricula = dataLiberacaoMatricula;
    }

    public String getNrDocumento() {
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public UsuarioVO getResponsavelEmissaoBoletoMatricula() {
        if (responsavelEmissaoBoletoMatricula == null) {
            responsavelEmissaoBoletoMatricula = new UsuarioVO();
        }
        return responsavelEmissaoBoletoMatricula;
    }

    public void setResponsavelEmissaoBoletoMatricula(UsuarioVO responsavelEmissaoBoletoMatricula) {
        this.responsavelEmissaoBoletoMatricula = responsavelEmissaoBoletoMatricula;
    }

    public UsuarioVO getResponsavelLiberacaoMatricula() {
        if (responsavelLiberacaoMatricula == null) {
            responsavelLiberacaoMatricula = new UsuarioVO();
        }
        return responsavelLiberacaoMatricula;
    }

    public void setResponsavelLiberacaoMatricula(UsuarioVO responsavelLiberacaoMatricula) {
        this.responsavelLiberacaoMatricula = responsavelLiberacaoMatricula;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por retornar um atributo do tipo data no
     * formato padrï¿½o dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public String getData_ApresentarAnoQuatroDigitos() {
        return (Uteis.getDataAno4Digitos(data));
    }

    public void setData(Date data) {
        this.data = data;
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

    @XmlElement(name = "turma")
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    @XmlElement(name = "unidadeEnsinoCurso")
    public Integer getUnidadeEnsinoCurso() {
        return unidadeEnsinoCurso;
    }

    public void setUnidadeEnsinoCurso(Integer unidadeEnsinoCurso) {
        this.unidadeEnsinoCurso = unidadeEnsinoCurso;
    }

    public TurmaVO getTurmaPeriodo() {
        if (turmaPeriodo == null) {
            turmaPeriodo = new TurmaVO();
        }
        return turmaPeriodo;
    }

    public void setTurmaPeriodo(TurmaVO turmaPeriodo) {
        this.turmaPeriodo = turmaPeriodo;
    }

    @XmlElement(name = "ano")
    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    @XmlElement(name = "semestre")
    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTumaDisciplinaVOs() {
        if (matriculaPeriodoTumaDisciplinaVOs == null) {
            matriculaPeriodoTumaDisciplinaVOs = new ArrayList();
        }
        return matriculaPeriodoTumaDisciplinaVOs;
    }

    public void setMatriculaPeriodoTumaDisciplinaVOs(List matriculaPeriodoTumaDisciplinaVOs) {
        this.matriculaPeriodoTumaDisciplinaVOs = matriculaPeriodoTumaDisciplinaVOs;
    }

    public Integer getProcessoMatricula() {
        return processoMatricula;
    }

    public Integer getTranferenciaEntrada() {
        return tranferenciaEntrada;
    }

    public void setTranferenciaEntrada(Integer tranferenciaEntrada) {
        this.tranferenciaEntrada = tranferenciaEntrada;
    }

    public void setProcessoMatricula(Integer processoMatricula) {
        this.processoMatricula = processoMatricula;
    }

    /**
     * @return the valorMatriculaCheio
     */
    public Double getValorMatriculaCheio() {
        return valorMatriculaCheio;
    }

    /**
     * @param valorMatriculaCheio
     *            the valorMatriculaCheio to set
     */
    public void setValorMatriculaCheio(Double valorMatriculaCheio) {
        this.valorMatriculaCheio = valorMatriculaCheio;
    }

    /**
     * @return the valorMensalidadeCheio
     */
    public Double getValorMensalidadeCheio() {
        return valorMensalidadeCheio;
    }

    /**
     * @param valorMensalidadeCheio
     *            the valorMensalidadeCheio to set
     */
    public void setValorMensalidadeCheio(Double valorMensalidadeCheio) {
        this.valorMensalidadeCheio = valorMensalidadeCheio;
    }

    /**
     * @return the valorDescontoMatricula
     */
    public Double getValorDescontoMatricula() {
        return valorDescontoMatricula;
    }

    /**
     * @param valorDescontoMatricula
     *            the valorDescontoMatricula to set
     */
    public void setValorDescontoMatricula(Double valorDescontoMatricula) {
        this.valorDescontoMatricula = Uteis.arrendondarForcando2CadasDecimais(valorDescontoMatricula);
    }

    /**
     * @return the valorDescontoMensalidade
     */
    public Double getValorDescontoMensalidade() {
        return valorDescontoMensalidade;
    }

    /**
     * @param valorDescontoMensalidade
     *            the valorDescontoMensalidade to set
     */
    public void setValorDescontoMensalidade(Double valorDescontoMensalidade) {
        this.valorDescontoMensalidade = valorDescontoMensalidade;
    }

    /**
     * @return the valorFinalMatricula
     */
    public Double getValorFinalMatricula() {
        return valorFinalMatricula;
    }

    /**
     * @param valorFinalMatricula
     *            the valorFinalMatricula to set
     */
    public void setValorFinalMatricula(Double valorFinalMatricula) {
        this.valorFinalMatricula = valorFinalMatricula;
    }

    /**
     * @return the valorFinalMensalidade
     */
    public Double getValorFinalMensalidade() {
        return valorFinalMensalidade;
    }

    /**
     * @param valorFinalMensalidade
     *            the valorFinalMensalidade to set
     */
    public void setValorFinalMensalidade(Double valorFinalMensalidade) {
        this.valorFinalMensalidade = valorFinalMensalidade;
    }

    /**
     * @return the valorMatriculaBolsaCusteada
     */
    public Double getValorMatriculaBolsaCusteada() {
        return valorMatriculaBolsaCusteada;
    }

    /**
     * @param valorMatriculaBolsaCusteada
     *            the valorMatriculaBolsaCusteada to set
     */
    public void setValorMatriculaBolsaCusteada(Double valorMatriculaBolsaCusteada) {
        this.valorMatriculaBolsaCusteada = valorMatriculaBolsaCusteada;
    }

    /**
     * @return the valorMensalidadeBolsaCusteada
     */
    public Double getValorMensalidadeBolsaCusteada() {
        return valorMensalidadeBolsaCusteada;
    }

    /**
     * @param valorMensalidadeBolsaCusteada
     *            the valorMensalidadeBolsaCusteada to set
     */
    public void setValorMensalidadeBolsaCusteada(Double valorMensalidadeBolsaCusteada) {
        this.valorMensalidadeBolsaCusteada = valorMensalidadeBolsaCusteada;
    }

    /**
     * @return the valorDescontoFaculdadeMatricula
     */
    public Double getValorDescontoInstituicaoMatricula() {
        return valorDescontoInstituicaoMatricula;
    }

    /**
     * @param valorDescontoFaculdadeMatricula
     *            the valorDescontoFaculdadeMatricula to set
     */
    public void setValorDescontoInstituicaoMatricula(Double valorDescontoInstituicaoMatricula) {
        this.valorDescontoInstituicaoMatricula = valorDescontoInstituicaoMatricula;
    }

    /**
     * @return the valorDescontoFaculdadeMensalidade
     */
    public Double getValorDescontoInstituicaoMensalidade() {
        return valorDescontoInstituicaoMensalidade;
    }

    /**
     * @param valorDescontoFaculdadeMensalidade
     *            the valorDescontoFaculdadeMensalidade to set
     */
    public void setValorDescontoInstituicaoMensalidade(Double valorDescontoInstituicaoMensalidade) {
        this.valorDescontoInstituicaoMensalidade = valorDescontoInstituicaoMensalidade;
    }

    /**
     * @return the valorDescontoConvenioMatricula
     */
    public Double getValorDescontoConvenioMatricula() {
        return valorDescontoConvenioMatricula;
    }

    /**
     * @param valorDescontoConvenioMatricula
     *            the valorDescontoConvenioMatricula to set
     */
    public void setValorDescontoConvenioMatricula(Double valorDescontoConvenioMatricula) {
        this.valorDescontoConvenioMatricula = valorDescontoConvenioMatricula;
    }

    /**
     * @return the valorDescontoConvenioMensalidade
     */
    public Double getValorDescontoConvenioMensalidade() {
        return valorDescontoConvenioMensalidade;
    }

    /**
     * @param valorDescontoConvenioMensalidade
     *            the valorDescontoConvenioMensalidade to set
     */
    public void setValorDescontoConvenioMensalidade(Double valorDescontoConvenioMensalidade) {
        this.valorDescontoConvenioMensalidade = valorDescontoConvenioMensalidade;
    }

    /**
     * @return the valorResidualMatricula
     */
    public Double getValorResidualMatricula() {
        return valorResidualMatricula;
    }

    /**
     * @param valorResidualMatricula
     *            the valorResidualMatricula to set
     */
    public void setValorResidualMatricula(Double valorResidualMatricula) {
        this.valorResidualMatricula = valorResidualMatricula;
    }

    /**
     * @return the valorResidualMensalidade
     */
    public Double getValorResidualMensalidade() {
        return valorResidualMensalidade;
    }

    /**
     * @param valorResidualMensalidade
     *            the valorResidualMensalidade to set
     */
    public void setValorResidualMensalidade(Double valorResidualMensalidade) {
        this.valorResidualMensalidade = valorResidualMensalidade;
    }

    /**
     * @return the valorBaseDescontoConvenioMatricula
     */
    public Double getValorBaseDescontoConvenioMatricula() {
        return valorBaseDescontoConvenioMatricula;
    }

    /**
     * @param valorBaseDescontoConvenioMatricula
     *            the valorBaseDescontoConvenioMatricula to set
     */
    public void setValorBaseDescontoConvenioMatricula(Double valorBaseDescontoConvenioMatricula) {
        this.valorBaseDescontoConvenioMatricula = valorBaseDescontoConvenioMatricula;
    }

    /**
     * @return the valorBaseDescontoConvenioMensalidade
     */
    public Double getValorBaseDescontoConvenioMensalidade() {
        return valorBaseDescontoConvenioMensalidade;
    }

    /**
     * @param valorBaseDescontoConvenioMensalidade
     *            the valorBaseDescontoConvenioMensalidade to set
     */
    public void setValorBaseDescontoConvenioMensalidade(Double valorBaseDescontoConvenioMensalidade) {
        this.valorBaseDescontoConvenioMensalidade = valorBaseDescontoConvenioMensalidade;
    }

    /**
     * @return the contratoMatriculaPeriodo
     */
    public TextoPadraoVO getContratoMatricula() {
        if (contratoMatricula == null) {
            contratoMatricula = new TextoPadraoVO();
        }
        return contratoMatricula;
    }

    /**
     * @param contratoMatriculaPeriodo
     *            the contratoMatriculaPeriodo to set
     */
    public void setContratoMatricula(TextoPadraoVO contratoMatriculaPeriodo) {
        this.contratoMatricula = contratoMatriculaPeriodo;
    }

    /**
     * @return the contratoFiador
     */
    public TextoPadraoVO getContratoFiador() {
        if (contratoFiador == null) {
            contratoFiador = new TextoPadraoVO();
        }
        return contratoFiador;
    }

    /**
     * @param contratoFiador
     *            the contratoFiador to set
     */
    public void setContratoFiador(TextoPadraoVO contratoFiador) {
        this.contratoFiador = contratoFiador;
    }

    public Boolean inicializarContratoFiadorPadraoMatricula() throws Exception {
        // PlanoFinanceiroCursoVO planoCursoFinanceiro =
        // getFacade().getMatricula().obterPlanoFinanceiroCursoMatriculaVO(matriculaVO);
//        if ((this.getPlanoFinanceiroCurso().getTextoPadraoContratoFiador().getCodigo() != null) && (!this.getPlanoFinanceiroCurso().getTextoPadraoContratoFiador().getCodigo().equals(0))) {
//            // Entrar aqui ï¿½ por que existe um texto de contrato definido para
//            // o Curso...
//            // logo o mesmo deve ser setado para a matricula curso...
//            this.setContratoFiador(this.getPlanoFinanceiroCurso().getTextoPadraoContratoFiador());
//            return true;
//        }
        return false;
    }

    public Boolean inicializarContratoExtensao() throws Exception {
//        if ((this.getPlanoFinanceiroCurso().getTextoPadraoContratoExtensao().getCodigo() != null) && (!this.getPlanoFinanceiroCurso().getTextoPadraoContratoExtensao().getCodigo().equals(0))) {
//            this.setContratoExtensao(this.getPlanoFinanceiroCurso().getTextoPadraoContratoExtensao());
//            return true;
//        }
        return false;
    }

    /**
     * @return the planoFinanceiroCurso
     */
//    public PlanoFinanceiroCursoVO getPlanoFinanceiroCurso() {
//        if (planoFinanceiroCurso == null) {
//            planoFinanceiroCurso = new PlanoFinanceiroCursoVO();
//        }
//        return planoFinanceiroCurso;
//    }
//
//    /**
//     * @param planoFinanceiroCurso
//     *            the planoFinanceiroCurso to set
//     */
//    public void setPlanoFinanceiroCurso(PlanoFinanceiroCursoVO planoFinanceiroCurso) {
//        this.planoFinanceiroCurso = planoFinanceiroCurso;
//    }

   

//    public List<MatriculaPeriodoVencimentoVO> getMatriculaPeriodoVencimentoVOsCompleto() {
//        if (matriculaPeriodoVencimentoVOs == null) {
//            matriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
//        }
//        return matriculaPeriodoVencimentoVOs;
//    }
//
//    public List<MatriculaPeriodoVencimentoVO> getMatriculaPeriodoVencimentoVOsExtras() {
//        if (matriculaPeriodoVencimentoVOs == null) {
//            matriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
//        }
//        List<MatriculaPeriodoVencimentoVO> listaRetorno = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
//        for (MatriculaPeriodoVencimentoVO vcto : matriculaPeriodoVencimentoVOs) {
//            if (vcto.getVencimentoExtraCobrancaValorDiferencaInclusaoDisciplina()) {
//                listaRetorno.add(vcto);
//            }
//        }
//        return listaRetorno;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public List<MatriculaPeriodoVencimentoVO> getMatriculaPeriodoVencimentoVOsExcluindoParcelasExtras() {
//        if (matriculaPeriodoVencimentoVOs == null) {
//            matriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
//        }
//        List<MatriculaPeriodoVencimentoVO> listaRetorno = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
//        for (MatriculaPeriodoVencimentoVO vcto : matriculaPeriodoVencimentoVOs) {
//            if (!vcto.getVencimentoExtraCobrancaValorDiferencaInclusaoDisciplina()) {
//                listaRetorno.add(vcto);
//            }
//        }
//        return listaRetorno;
//    }
//
//    public List<MatriculaPeriodoVencimentoVO> getMatriculaPeriodoVencimentoVOsNaoPagos() {
//        if (matriculaPeriodoVencimentoVOs == null) {
//            matriculaPeriodoVencimentoVOs = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
//        }
//        List<MatriculaPeriodoVencimentoVO> listaRetorno = new ArrayList<MatriculaPeriodoVencimentoVO>(0);
//        for (MatriculaPeriodoVencimentoVO vcto : matriculaPeriodoVencimentoVOs) {
//            if ((vcto.getVencimentoReferenteMatricula()) && (!vcto.getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA))) {
//                listaRetorno.add(vcto);
//            }
//        }
//        return listaRetorno;
//    }

   

//    /**
//     * @param matriculaPeriodoPreMatricula
//     *            the matriculaPeriodoPreMatricula to set
//     */
//    public void setMatriculaPeriodoPreMatricula(MatriculaPeriodoPreMatriculaVO matriculaPeriodoPreMatricula) {
//        this.matriculaPeriodoPreMatricula = matriculaPeriodoPreMatricula;
//    }
//
//    public MatriculaPeriodoVencimentoVO getMatriculaPeriodoVencimentoEspecifico(String nrParcela) {
//        for (MatriculaPeriodoVencimentoVO vcto : this.getMatriculaPeriodoVencimentoVOs()) {
//            if (vcto.getParcela().equals(nrParcela)) {
//                return vcto;
//            }
//        }
//        return null;
//    }

    public Boolean getBoletoJaEmitidoMatricula() {
        if (this.getResponsavelEmissaoBoletoMatricula().getCodigo().intValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean getParcelaReferenteMatriculaLiberadaPagamento() {
        return verificarParcelaReferenteMatriculaLiberadaPagamento();
    }

    public Boolean verificarParcelaReferenteMatriculaLiberadaPagamento() {
        if (this.getResponsavelLiberacaoMatricula().getCodigo().intValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

//    public void alterarSituacaoVencimentoRelativoMatricula(SituacaoVencimentoMatriculaPeriodo situacaoAlterar) {
//        MatriculaPeriodoVencimentoVO vctoMatricula = getMatriculaPeriodoVencimentoEspecifico("MA");
//        vctoMatricula.setSituacao(situacaoAlterar);
//    }

    /**
     * @return the processoMatriculaCalendarioVO
     */
    public ProcessoMatriculaCalendarioVO getProcessoMatriculaCalendarioVO() {
        if (processoMatriculaCalendarioVO == null) {
            processoMatriculaCalendarioVO = new ProcessoMatriculaCalendarioVO();
        }
        return processoMatriculaCalendarioVO;
    }

    /**
     * @param processoMatriculaCalendarioVO
     *            the processoMatriculaCalendarioVO to set
     */
    public void setProcessoMatriculaCalendarioVO(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) {
        this.processoMatriculaCalendarioVO = processoMatriculaCalendarioVO;
    }

    /**
     * @return the configuracaoFinanceiro
     */
//    public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiro() {
//        if (configuracaoFinanceiro == null) {
//            configuracaoFinanceiro = new ConfiguracaoFinanceiroVO();
//        }
//        return configuracaoFinanceiro;
//    }

    /**
     * @param configuracaoFinanceiro
     *            the configuracaoFinanceiro to set
     */
//    public void setConfiguracaoFinanceiro() {
//        this.configuracaoFinanceiro = configuracaoFinanceiro;
//    }

    /**
     * @return the historicoOperacoes
     */
    public List<MatriculaPeriodoHistoricoVO> getHistoricoOperacoes() {
        if (historicoOperacoes == null) {
            historicoOperacoes = new ArrayList<MatriculaPeriodoHistoricoVO>(0);
        }
        return historicoOperacoes;
    }

    /**
     * @param historicoOperacoes
     *            the historicoOperacoes to set
     */
    public void setHistoricoOperacoes(List<MatriculaPeriodoHistoricoVO> historicoOperacoes) {
        this.historicoOperacoes = historicoOperacoes;
    }

    public void adicionarHistoricoOperacao(String descricao, Date data) {
        MatriculaPeriodoHistoricoVO novoHist = new MatriculaPeriodoHistoricoVO();
        novoHist.setDescricaoOperacao(descricao);
        novoHist.setDataOperacao(data);
        this.getHistoricoOperacoes().add(novoHist);
    }

  

    /**
     * @return the dataProcessamentoAtual
     */
    public Date getDataProcessamentoAtual() {
        if (dataProcessamentoAtual == null) {
            dataProcessamentoAtual = new Date();
        }
        return dataProcessamentoAtual;
    }

    /**
     * @param dataProcessamentoAtual
     *            the dataProcessamentoAtual to set
     */
    public void setDataProcessamentoAtual(Date dataProcessamentoAtual) {
        this.dataProcessamentoAtual = dataProcessamentoAtual;
    }

    /**
     * @return the nrDisciplinasIncluidas
     */
    public Integer getNrDisciplinasIncluidas() {
        if (nrDisciplinasIncluidas == null) {
            nrDisciplinasIncluidas = 0;
        }
        return nrDisciplinasIncluidas;
    }

    /**
     * @param nrDisciplinasIncluidas
     *            the nrDisciplinasIncluidas to set
     */
    public void setNrDisciplinasIncluidas(Integer nrDisciplinasIncluidas) {
        this.nrDisciplinasIncluidas = nrDisciplinasIncluidas;
    }

    /**
     * @return the nrDisciplinasExcluidas
     */
    public Integer getNrDisciplinasExcluidas() {
        if (nrDisciplinasExcluidas == null) {
            nrDisciplinasExcluidas = 0;
        }
        return nrDisciplinasExcluidas;
    }

    /**
     * @param nrDisciplinasExcluidas
     *            the nrDisciplinasExcluidas to set
     */
    public void setNrDisciplinasExcluidas(Integer nrDisciplinasExcluidas) {
        this.nrDisciplinasExcluidas = nrDisciplinasExcluidas;
    }

    public Boolean getPermitirIncluirExcluirDisciplinas() {
        try {
            if (this.getProcessoMatriculaCalendarioVO() == null) {
                return false;
            }
            if (!getPossuiPermissaoInclusaoExclusaoDisciplina()) {
                return getProcessoMatriculaCalendarioVO().verificarDataEstaDentroPeriodoInclusaoExclusaoValido(new Date());
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public Boolean getInclusaoForaPrazo() {
        return inclusaoForaPrazo;
    }

    public void setInclusaoForaPrazo(Boolean inclusaoForaPrazo) {
        this.inclusaoForaPrazo = inclusaoForaPrazo;
    }

    public Integer getNumParcelasInclusaoForaPrazo() {
        return numParcelasInclusaoForaPrazo;
    }

    public void setNumParcelasInclusaoForaPrazo(Integer numParcelasInclusaoForaPrazo) {
        this.numParcelasInclusaoForaPrazo = numParcelasInclusaoForaPrazo;
    }

    public Double getValorTotalParcelaInclusaoForaPrazo() {
        return valorTotalParcelaInclusaoForaPrazo;
    }

    public void setValorTotalParcelaInclusaoForaPrazo(Double valorTotalParcelaInclusaoForaPrazo) {
        this.valorTotalParcelaInclusaoForaPrazo = valorTotalParcelaInclusaoForaPrazo;
    }

    /**
     * @return the preMatriculaPeriodoTurmaDisciplinaVOs
     */
    public List<MatriculaPeriodoTurmaDisciplinaVO> getPreMatriculaPeriodoTurmaDisciplinaVOs() {
        if (preMatriculaPeriodoTurmaDisciplinaVOs == null) {
            preMatriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
        }
        return preMatriculaPeriodoTurmaDisciplinaVOs;
    }

    /**
     * @param preMatriculaPeriodoTurmaDisciplinaVOs
     *            the preMatriculaPeriodoTurmaDisciplinaVOs to set
     */
    public void setPreMatriculaPeriodoTurmaDisciplinaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> preMatriculaPeriodoTurmaDisciplinaVOs) {
        this.preMatriculaPeriodoTurmaDisciplinaVOs = preMatriculaPeriodoTurmaDisciplinaVOs;
    }

    /**
     * @return the valorTotalTodosDescontosMatricula
     */
    public Double getValorTotalTodosDescontosMatricula() {
        return valorTotalTodosDescontosMatricula;
    }

    /**
     * @param valorTotalTodosDescontosMatricula
     *            the valorTotalTodosDescontosMatricula to set
     */
    public void setValorTotalTodosDescontosMatricula(Double valorTotalTodosDescontosMatricula) {
        this.valorTotalTodosDescontosMatricula = Uteis.arrendondarForcando2CadasDecimais(valorTotalTodosDescontosMatricula);
    }

    /**
     * @return the valorTotalTodosDescontosMensalidade
     */
    public Double getValorTotalTodosDescontosMensalidade() {
        return valorTotalTodosDescontosMensalidade;
    }

    /**
     * @param valorTotalTodosDescontosMensalidade
     *            the valorTotalTodosDescontosMensalidade to set
     */
    public void setValorTotalTodosDescontosMensalidade(Double valorTotalTodosDescontosMensalidade) {
        this.valorTotalTodosDescontosMensalidade = valorTotalTodosDescontosMensalidade;
    }

    public Date getDiaVencimentoInclusaoForaPrazo() {
        return diaVencimentoInclusaoForaPrazo;
    }

    public void setDiaVencimentoInclusaoForaPrazo(Date diaVencimentoInclusaoForaPrazo) {
        this.diaVencimentoInclusaoForaPrazo = diaVencimentoInclusaoForaPrazo;
    }

    public List getMatriculaPeriodoTumaDisciplinaVOsDeAcordoSituacaoAcademica() {
        // if (this.isSituacaoPreMatricula()) {
        // return this.getPreMatriculaPeriodoTurmaDisciplinaVOs();
        // } else {
        return this.getMatriculaPeriodoTumaDisciplinaVOs();
        // }
    }

    /**
     * @return the matriculaVO
     */
    @XmlElement(name = "matriculaVO")
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO
     *            the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

   

    /**
     * @return the erro
     */
    public Boolean getErro() {

        return erro;
    }

    /**
     * @param erro
     *            the erro to set
     */
    public void setErro(Boolean erro) {
        this.erro = erro;
    }

    /**
     * @return the mensagemErro
     */
    public String getMensagemErro() {
        return mensagemErro;
    }

    /**
     * @param mensagemErro
     *            the mensagemErro to set
     */
    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    /**
     * @return the apresentarSituacao
     */
    public Boolean getApresentarSituacao() {
        return apresentarSituacao;
    }

    /**
     * @param apresentarSituacao
     *            the apresentarSituacao to set
     */
    public void setApresentarSituacao(Boolean apresentarSituacao) {
        this.apresentarSituacao = apresentarSituacao;
    }

    public Integer getNrPeriodoLetivo() {
        return this.getPeridoLetivo().getPeriodoLetivo();
    }

    /**
     * @return the financeiroManual
     */
    public Boolean getFinanceiroManual() {
        if (financeiroManual == null) {
            financeiroManual = false;
        }
        return financeiroManual;
    }

    /**
     * @param financeiroManual
     *            the financeiroManual to set
     */
    public void setFinanceiroManual(Boolean financeiroManual) {
        this.financeiroManual = financeiroManual;
    }

    public boolean getIsNovaMatriculaPeriodo() {
        if (getCodigo().intValue() == 0) {
            return true;
        }
        return false;
    }

    public boolean getIsDesabilitarCamposTela() {
        if (getNovoObj()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the processoMatriculaVO
     */
    public ProcessoMatriculaVO getProcessoMatriculaVO() {
        if (processoMatriculaVO == null) {
            processoMatriculaVO = new ProcessoMatriculaVO();
        }
        return processoMatriculaVO;
    }

    /**
     * @param processoMatriculaVO
     *            the processoMatriculaVO to set
     */
    public void setProcessoMatriculaVO(ProcessoMatriculaVO processoMatriculaVO) {
        this.processoMatriculaVO = processoMatriculaVO;
    }

    /**
     * @return the unidadeEnsinoCursoVO
     */
    @XmlElement(name = "unidadeEnsinoCursoVO")
    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCursoVO;
    }

    /**
     * @param unidadeEnsinoCursoVO
     *            the unidadeEnsinoCursoVO to set
     */
    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
    }

    /**
     * @return the listaDisciplinasPeriodoLetivoAlunoJaAprovado
     */
    public List<HistoricoVO> getListaDisciplinasPeriodoLetivoAlunoJaAprovado() {
        if (listaDisciplinasPeriodoLetivoAlunoJaAprovado == null) {
            listaDisciplinasPeriodoLetivoAlunoJaAprovado = new ArrayList<HistoricoVO>(0);
        }
        return listaDisciplinasPeriodoLetivoAlunoJaAprovado;
    }

    /**
     * @param listaDisciplinasPeriodoLetivoAlunoJaAprovado
     *            the listaDisciplinasPeriodoLetivoAlunoJaAprovado to set
     */
    public void setListaDisciplinasPeriodoLetivoAlunoJaAprovado(List<HistoricoVO> listaDisciplinasPeriodoLetivoAlunoJaAprovado) {
        this.listaDisciplinasPeriodoLetivoAlunoJaAprovado = listaDisciplinasPeriodoLetivoAlunoJaAprovado;
    }

    public HistoricoVO verificarDisciplinaJaAprovada(DisciplinaVO disciplina) throws Exception {
        for (HistoricoVO historicoDisciplina : this.getListaDisciplinasPeriodoLetivoAlunoJaAprovado()) {
            if (historicoDisciplina.getDisciplina().getCodigo().equals(disciplina.getCodigo())) {
                return historicoDisciplina;
            }
        }
        return null;
    }

    public List<HistoricoVO> getListaDisciplinasPeriodoLetivoAlunoJaAprovado(Integer periodoLetivo) throws Exception {
        List<HistoricoVO> listaRetornar = new ArrayList<HistoricoVO>(0);
        PeriodoLetivoVO periodoLetivoVO = this.getGradeCurricular().consultarObjPeriodoLetivoVOPorCodigo(periodoLetivo);
        if (periodoLetivoVO != null) {
            for (GradeDisciplinaVO gradeDisciplina : periodoLetivoVO.getGradeDisciplinaVOs()) {
                HistoricoVO historicoAprovacao = verificarDisciplinaJaAprovada(gradeDisciplina.getDisciplina());
                if (historicoAprovacao != null) {
                    listaRetornar.add(historicoAprovacao);
                }
            }
        }
        return listaRetornar;
    }

    public Boolean verificarDisciplinaEstaRegistradaMatriculaPeriodoParaSerCursada(DisciplinaVO disciplina) throws Exception {
        for (MatriculaPeriodoTurmaDisciplinaVO obj : this.getMatriculaPeriodoTumaDisciplinaVOs()) {
            if (obj.getDisciplina().getCodigo().equals(disciplina.getCodigo())) {
                return true;
            }
            if (obj.getDisciplinaEquivale()) {
                // caso a discilpina que está na grade do aluno, seja uma
                // disciplina equivalente (pois nao havia
                // vaga na disciplina original, por exemplo), então devemos
                // verificar se a disciplina em questão,
                // não se equivale a disciplina equivalnete da grade do aluno.
                // Caso sim, temos que retornar
                // true, indicando que a disciplina já está registrada por meio
                // de uma equivalencia.
                if (obj.getDisciplinaEquivalente().getCodigo().equals(disciplina.getCodigo())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<DisciplinaVO> getListaDisciplinasPeriodoLetivoAlunoPendente(Integer periodoLetivo) throws Exception {
        List<DisciplinaVO> listaRetornar = new ArrayList<DisciplinaVO>(0);
        PeriodoLetivoVO periodoLetivoVO = this.getGradeCurricular().consultarObjPeriodoLetivoVOPorCodigo(periodoLetivo);
        if (periodoLetivoVO != null) {
            for (GradeDisciplinaVO gradeDisciplina : periodoLetivoVO.getGradeDisciplinaVOs()) {
                HistoricoVO historicoAprovacao = verificarDisciplinaJaAprovada(gradeDisciplina.getDisciplina());
                if ((historicoAprovacao == null) && (!verificarDisciplinaEstaRegistradaMatriculaPeriodoParaSerCursada(gradeDisciplina.getDisciplina()))) {
                    listaRetornar.add(gradeDisciplina.getDisciplina());
                }
            }
        }
        return listaRetornar;
    }

    /**
     * @return the totalCargaHorariaPadraoMatriculaPeriodo
     */
    public Integer getTotalCargaHorariaPadraoMatriculaPeriodo() {
        if (totalCargaHorariaPadraoMatriculaPeriodo == null) {
            totalCargaHorariaPadraoMatriculaPeriodo = 0;
        }
        return totalCargaHorariaPadraoMatriculaPeriodo;
    }

    /**
     * @param totalCargaHorariaPadraoMatriculaPeriodo
     *            the totalCargaHorariaPadraoMatriculaPeriodo to set
     */
    public void setTotalCargaHorariaPadraoMatriculaPeriodo(Integer totalCargaHorariaPadraoMatriculaPeriodo) {
        this.totalCargaHorariaPadraoMatriculaPeriodo = totalCargaHorariaPadraoMatriculaPeriodo;
    }

    /**
     * @return the totalCargaHorariaAlunoMatriculaPeriodo
     */
    public Integer getTotalCargaHorariaAlunoMatriculaPeriodo() {
        if (totalCargaHorariaAlunoMatriculaPeriodo == null) {
            totalCargaHorariaAlunoMatriculaPeriodo = 0;
        }
        return totalCargaHorariaAlunoMatriculaPeriodo;
    }

    /**
     * @param totalCargaHorariaAlunoMatriculaPeriodo
     *            the totalCargaHorariaAlunoMatriculaPeriodo to set
     */
    public void setTotalCargaHorariaAlunoMatriculaPeriodo(Integer totalCargaHorariaAlunoMatriculaPeriodo) {
        this.totalCargaHorariaAlunoMatriculaPeriodo = totalCargaHorariaAlunoMatriculaPeriodo;
    }

    public void inicializarPeriodoLetivoDeAcordoGradeCurricular() {
        try {
            this.setPeridoLetivo(this.getGradeCurricular().consultarObjPeriodoLetivoVOPorCodigo(this.getPeridoLetivo().getCodigo()));
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public Boolean getIsPrimeiroPeriodoLetivo() {
        PeriodoLetivoVO primeiroPeriodo = this.getGradeCurricular().getPrimeiroPeriodoLetivoGrade();
        if (this.getPeridoLetivo().getPeriodoLetivo().equals(primeiroPeriodo.getPeriodoLetivo())) {
            return true;
        }
        return false;
    }

    public Boolean getIsUltimoPeriodoLetivo() {
        PeriodoLetivoVO ultimoPeriodo = this.getGradeCurricular().getUltimoPeriodoLetivoGrade();
        if (this.getPeridoLetivo().getPeriodoLetivo().equals(ultimoPeriodo.getPeriodoLetivo())) {
            return true;
        }
        return false;
    }

    public Boolean getPermiteAlteracaoPeloUsuario() {
        if ((this.getMatriculaVO().getIsAtiva() || this.getMatriculaVO().getIsPreMatricula() || this.getMatriculaVO().getIsTrancada())) {
            if ((this.getSituacaoAtiva() || this.getSituacaoPreMatricula() || this.isTrancada())) {
                return true;
            }
        }
        return false;
    }

    public String getApresentarAnoSemestre() {
        return getAno() + " / " + getSemestre();
    }

    /**
     * @return the listaDescontosMatricula
     */
    

    public void refletirValoresReferentesADescontosCalculadosParaMatriculaPeriodoVO() {
        // vctoVO.setValorDesconto(matriculaPeriodoVO.getValorDescontoMatricula());
        // vctoVO.setValorDescontoConvenio(matriculaPeriodoVO.getValorDescontoConvenioMatricula());
    }

   

    

    public void setReconheceuDivida(Boolean reconheceuDivida) {
        this.reconheceuDivida = reconheceuDivida;
    }

    public Boolean getReconheceuDivida() {
        if (reconheceuDivida == null) {
            reconheceuDivida = false;
        }
        return reconheceuDivida;
    }

    public Boolean getPossuiDivida() {
        if (possuiDivida == null) {
            possuiDivida = false;
        }
        return possuiDivida;
    }

    public void setPossuiDivida(Boolean possuiDivida) {
        this.possuiDivida = possuiDivida;
    }

    public Boolean getCarneEntregue() {
        if (carneEntregue == null) {
            carneEntregue = false;
        }
        return carneEntregue;
    }

    public void setCarneEntregue(Boolean carneEntregue) {
        this.carneEntregue = carneEntregue;
    }

    /**
     * @return the listaParcelasGeradasManualmenteAnteriormenteAptasParaRegeracao
     */
    
    /**
     * @return the selecionarMatriculaPeriodoRenovar
     */
    public Boolean getSelecionarMatriculaPeriodoRenovar() {
        if (selecionarMatriculaPeriodoRenovar == null) {
            selecionarMatriculaPeriodoRenovar = Boolean.TRUE;
        }
        return selecionarMatriculaPeriodoRenovar;
    }

    /**
     * @param selecionarMatriculaPeriodoRenovar the selecionarMatriculaPeriodoRenovar to set
     */
    public void setSelecionarMatriculaPeriodoRenovar(Boolean selecionarMatriculaPeriodoRenovar) {
        this.selecionarMatriculaPeriodoRenovar = selecionarMatriculaPeriodoRenovar;
    }

    public Integer getCodigoOrigemFechamentoMatriculaPeriodo() {
        return codigoOrigemFechamentoMatriculaPeriodo;
    }

    public void setCodigoOrigemFechamentoMatriculaPeriodo(Integer codigoOrigemFechamentoMatriculaPeriodo) {
        this.codigoOrigemFechamentoMatriculaPeriodo = codigoOrigemFechamentoMatriculaPeriodo;
    }

    public Date getDataFechamentoMatriculaPeriodo() {
        return dataFechamentoMatriculaPeriodo;
    }

    public void setDataFechamentoMatriculaPeriodo(Date dataFechamentoMatriculaPeriodo) {
        this.dataFechamentoMatriculaPeriodo = dataFechamentoMatriculaPeriodo;
    }

    public OrigemFechamentoMatriculaPeriodoEnum getOrigemFechamentoMatriculaPeriodo() {
        if (origemFechamentoMatriculaPeriodo == null) {
            origemFechamentoMatriculaPeriodo = OrigemFechamentoMatriculaPeriodoEnum.NAO_FECHADO;
        }
        return origemFechamentoMatriculaPeriodo;
    }

    public void setOrigemFechamentoMatriculaPeriodo(OrigemFechamentoMatriculaPeriodoEnum origemFechamentoMatriculaPeriodo) {
        this.origemFechamentoMatriculaPeriodo = origemFechamentoMatriculaPeriodo;
    }

    /**
     * @return the possuiPermissaoInclusaoExclusaoDisciplina
     */
    public Boolean getPossuiPermissaoInclusaoExclusaoDisciplina() {
        if (possuiPermissaoInclusaoExclusaoDisciplina == null) {
            possuiPermissaoInclusaoExclusaoDisciplina = Boolean.FALSE;
        }
        return possuiPermissaoInclusaoExclusaoDisciplina;
    }

    /**
     * @param possuiPermissaoInclusaoExclusaoDisciplina the possuiPermissaoInclusaoExclusaoDisciplina to set
     */
    public void setPossuiPermissaoInclusaoExclusaoDisciplina(Boolean possuiPermissaoInclusaoExclusaoDisciplina) {
        this.possuiPermissaoInclusaoExclusaoDisciplina = possuiPermissaoInclusaoExclusaoDisciplina;
    }

    public Integer getUsuarioResponsavelConfirmacaoOuCancelamentoPreMatricula() {
        return usuarioResponsavelConfirmacaoOuCancelamentoPreMatricula;
    }

    public void setUsuarioResponsavelConfirmacaoOuCancelamentoPreMatricula(Integer usuarioResponsavelConfirmacaoOuCancelamentoPreMatricula) {
        this.usuarioResponsavelConfirmacaoOuCancelamentoPreMatricula = usuarioResponsavelConfirmacaoOuCancelamentoPreMatricula;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatriculaPeriodoVO other = (MatriculaPeriodoVO) obj;
        if (this.codigo != other.codigo && (this.codigo == null || !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    public String getObterDescricaoPeriodoLetivo() {
        String strRetornar = "";
        try {
            if ((!this.getSemestre().equals(""))
                    && (!this.getAno().equals(""))) {
                strRetornar = this.getSemestre() + "/" + this.getAno() + " - ";
            }else  if (!this.getAno().equals("")) {
                strRetornar = this.getAno() + " - ";
            }
            strRetornar = strRetornar + this.getPeridoLetivo().getDescricao();
        } catch (Exception e) {
            strRetornar = "";
        }
        return strRetornar;
    }

    public void setBolsista(Boolean bolsista) {
        this.bolsista = bolsista;
    }

    public Boolean getBolsista() {
        if (bolsista == null) {
            bolsista = Boolean.FALSE;
        }
        return bolsista;
    }

    public void setContratoExtensao(TextoPadraoVO contratoExtensao) {
        this.contratoExtensao = contratoExtensao;
    }

    public TextoPadraoVO getContratoExtensao() {
        if (contratoExtensao == null) {
            contratoExtensao = new TextoPadraoVO();
        }
        return contratoExtensao;
    }

    /*public SituacaoVencimentoMatriculaPeriodo obterSituacaoParcelasReferenteMensaLidadeEspecifica(String parcela) {
        MatriculaPeriodoVencimentoVO vctoMatricula = this.getMatriculaPeriodoVencimentoEspecifico(parcela + "/" + getMatriculaPeriodoVencimentoVOs().size());
        if (vctoMatricula == null) {
            return SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_NAO_GERADA;
        } else {
            return vctoMatricula.getSituacao();
        }
    }*/

    public Boolean getReposicao() {
        if (reposicao == null) {
            reposicao = Boolean.FALSE;
        }
        return reposicao;
    }

    public void setReposicao(Boolean reposicao) {
        this.reposicao = reposicao;
    }

    public Double getDescontoReposicao() {
        if (descontoReposicao == null) {
            descontoReposicao = 0.0;
        }
        return descontoReposicao;
    }

    public void setDescontoReposicao(Double descontoReposicao) {
        this.descontoReposicao = descontoReposicao;
    }

  



    public Boolean getAlunoTransferidoUnidade() {
        if (alunoTransferidoUnidade == null) {
            alunoTransferidoUnidade = Boolean.FALSE;
        }
        return alunoTransferidoUnidade;
    }

    public void setAlunoTransferidoUnidade(Boolean alunoTransferidoUnidade) {
        this.alunoTransferidoUnidade = alunoTransferidoUnidade;
    }

    /**
     * @return the dataInicioAula
     */
    public Date getDataInicioAula() {        
        return dataInicioAula;
    }

    /**
     * @param dataInicioAula the dataInicioAula to set
     */
    public void setDataInicioAula(Date dataInicioAula) {
        this.dataInicioAula = dataInicioAula;
    }

    /**
     * @return the dataFinalAula
     */
    public Date getDataFinalAula() {       
        return dataFinalAula;
    }

    /**
     * @param dataFinalAula the dataFinalAula to set
     */
    public void setDataFinalAula(Date dataFinalAula) {
        this.dataFinalAula = dataFinalAula;
    }

    public Integer getQtdeParcelaContrato() {
        if (qtdeParcelaContrato == null) {
            qtdeParcelaContrato = 0;
        }
        return qtdeParcelaContrato;
    }

    public void setQtdeParcelaContrato(Integer qtdeParcelaContrato) {
        this.qtdeParcelaContrato = qtdeParcelaContrato;
    }

  

    public void setJustificativaTrancamento(String justificativaTrancamento) {
        this.justificativaTrancamento = justificativaTrancamento;
    }

    public String getTitulacaoInstituicao() {
        if (titulacaoInstituicao == null) {
            titulacaoInstituicao = "Instituição";
        }
        return titulacaoInstituicao;
    }

    public void setTitulacaoInstituicao(String titulacaoInstituicao) {
        this.titulacaoInstituicao = titulacaoInstituicao;
    }

    /**
     * @return the dataExtenso
     */
    public String getDataExtenso() {
        if (dataExtenso == null) {
            dataExtenso = "";
        }
        return dataExtenso;
    }

    /**
     * @param dataExtenso the dataExtenso to set
     */
    public void setDataExtenso(String dataExtenso) {
        this.dataExtenso = dataExtenso;
    }

    public Boolean getAceitouTermoContratoRenovacaoOnline() {
        if (aceitouTermoContratoRenovacaoOnline == null) {
            aceitouTermoContratoRenovacaoOnline = Boolean.FALSE;
        }
        return aceitouTermoContratoRenovacaoOnline;
    }

    public void setAceitouTermoContratoRenovacaoOnline(Boolean aceitouTermoContratoRenovacaoOnline) {
        this.aceitouTermoContratoRenovacaoOnline = aceitouTermoContratoRenovacaoOnline;
    }

//    public PlanoFinanceiroAlunoLogVO getLogPlanoFinanceiroAluno() {
//        if (logPlanoFinanceiroAluno == null) {
//            logPlanoFinanceiroAluno = new PlanoFinanceiroAlunoLogVO();
//        }
//        return logPlanoFinanceiroAluno;
//    }
//
//    public void setLogPlanoFinanceiroAluno(PlanoFinanceiroAlunoLogVO logPlanoFinanceiroAluno) {
//        this.logPlanoFinanceiroAluno = logPlanoFinanceiroAluno;
//    }

    public List<HistoricoVO> getListaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar() {
        if (listaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar == null) {
            listaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar = new ArrayList<HistoricoVO>(0);
        }
        return listaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar;
    }

    public void setListaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar(List<HistoricoVO> listaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar) {
        this.listaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar = listaDisciplinasPeriodoLetivoAlunoJaAprovadoDeveReprovar;
    }

    public Date getDataNotificacaoAbandonoCurso() {
        return dataNotificacaoAbandonoCurso;
    }

    public void setDataNotificacaoAbandonoCurso(Date dataNotificacaoAbandonoCurso) {
        this.dataNotificacaoAbandonoCurso = dataNotificacaoAbandonoCurso;
    }

    public String getMotivoLiberacaoPgtoMatricula() {
        if (motivoLiberacaoPgtoMatricula == null) {
            motivoLiberacaoPgtoMatricula = "";
        }
        return motivoLiberacaoPgtoMatricula;
    }

    public void setMotivoLiberacaoPgtoMatricula(String motivoLiberacaoPgtoMatricula) {
        this.motivoLiberacaoPgtoMatricula = motivoLiberacaoPgtoMatricula;
    }

    /**
     * @return the nrCreditoDiscplinasOptativas
     */
    public Integer getNrCreditoDiscplinasOptativas() {
        if (nrCreditoDiscplinasOptativas == null) {
            nrCreditoDiscplinasOptativas = 0;
        }
        return nrCreditoDiscplinasOptativas;
    }

    /**
     * @param nrCreditoDiscplinasOptativas the nrCreditoDiscplinasOptativas to set
     */
    public void setNrCreditoDiscplinasOptativas(Integer nrCreditoDiscplinasOptativas) {
        this.nrCreditoDiscplinasOptativas = nrCreditoDiscplinasOptativas;
    }

    /**
     * @return the nrCargaHorariaDiscplinasOptativas
     */
    public Integer getNrCargaHorariaDiscplinasOptativas() {
        if (nrCargaHorariaDiscplinasOptativas == null) {
            nrCargaHorariaDiscplinasOptativas = 0;
        }
        return nrCargaHorariaDiscplinasOptativas;
    }

    /**
     * @param nrCargaHorariaDiscplinasOptativas the nrCargaHorariaDiscplinasOptativas to set
     */
    public void setNrCargaHorariaDiscplinasOptativas(Integer nrCargaHorariaDiscplinasOptativas) {
        this.nrCargaHorariaDiscplinasOptativas = nrCargaHorariaDiscplinasOptativas;
    }

    public void atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares() {
        //totalCreditoPadraoMatriculaPeriodo = 0;
        totalCreditoAlunoMatriculaPeriodo = 0;
        //totalCargaHorariaPadraoMatriculaPeriodo = 0;
        totalCargaHorariaAlunoMatriculaPeriodo = 0;
        nrCreditoDiscplinasOptativas = 0;
        nrCargaHorariaDiscplinasOptativas = 0;
        nrCreditoDiscplinasOptativas = 0;
        nrCargaHorariaDiscplinasOptativas = 0;
//        Iterator i = this.getTurmaDisciplinaVOs().iterator();
//        while (i.hasNext()) {
//            TurmaDisciplinaVO turmaDisciplinaVO = (TurmaDisciplinaVO) i.next();
//            DisciplinaVO disciplinaVO = turmaDisciplinaVO.getDisciplina();
//            boolean disciplinaEstaPresenteNaGradeDisciplina = false;
//            for (GradeDisciplinaVO gradeDisciplinaVO : this.getPeridoLetivo().getGradeDisciplinaVOs()) {
//                if (gradeDisciplinaVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
//                    disciplinaEstaPresenteNaGradeDisciplina = true;
//                    if (gradeDisciplinaVO.getTipoDisciplina().equals("OP")
//                            || gradeDisciplinaVO.getTipoDisciplina().equals("LO")) {
//                        // se entrar aqui é por que a disciplina é optativa 
//                        // do tipo lançado na própria grade. Quando a optativa
//                        // não está sendo controlada por meio do grupo de optativa
//                        totalCargaHorariaDisciplinasOptativas = totalCargaHorariaDisciplinasOptativas + gradeDisciplinaVO.getCargaHoraria();
//                        totalCreditosDisciplinasOptativas = totalCreditosDisciplinasOptativas + gradeDisciplinaVO.getNrCreditos();
//                    } else {
//                        // se entrar aqui é por que a disciplina é regular.
//                        totalCargaHorariaDisciplinasRegulares = totalCargaHorariaDisciplinasRegulares + gradeDisciplinaVO.getCargaHoraria();
//                        totalCreditosDisciplinasRegulares = totalCreditosDisciplinasRegulares + gradeDisciplinaVO.getNrCreditos();
//                    }
//                }
//            }
//            if (!disciplinaEstaPresenteNaGradeDisciplina) {
//                // Se a disciplina não está presente na gradeDisciplina significa 
//                // que a mesma deve ser oriunda do Grupo de Optativas do Periodo
//                // Letivo. Logo temos que varrer o grupo de optativas para 
//                // processar a carga horaria / créditos da mesma
//                for (GradeCurricularGrupoOptativaDisciplinaVO gradeDisciplinaOptVO : this.getPeridoLetivo().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
//                    if (gradeDisciplinaOptVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
//                        totalCargaHorariaDisciplinasOptativas = totalCargaHorariaDisciplinasOptativas + gradeDisciplinaOptVO.getCargaHoraria();
//                        totalCreditosDisciplinasOptativas = totalCreditosDisciplinasOptativas + gradeDisciplinaOptVO.getNrCreditos();
//                    }
//                }
//            }
//        }
    }

    /**
     * @return the totalCreditoPadraoMatriculaPeriodo
     */
    public Integer getTotalCreditoPadraoMatriculaPeriodo() {
        if (totalCreditoPadraoMatriculaPeriodo == null) {
            totalCreditoPadraoMatriculaPeriodo = 0;
        }
        return totalCreditoPadraoMatriculaPeriodo;
    }

    /**
     * @param totalCreditoPadraoMatriculaPeriodo the totalCreditoPadraoMatriculaPeriodo to set
     */
    public void setTotalCreditoPadraoMatriculaPeriodo(Integer totalCreditoPadraoMatriculaPeriodo) {
        this.totalCreditoPadraoMatriculaPeriodo = totalCreditoPadraoMatriculaPeriodo;
    }

    /**
     * @return the totalCreditoAlunoMatriculaPeriodo
     */
    public Integer getTotalCreditoAlunoMatriculaPeriodo() {
        if (totalCreditoAlunoMatriculaPeriodo == null) {
            totalCreditoAlunoMatriculaPeriodo = 0;
        }
        return totalCreditoAlunoMatriculaPeriodo;
    }

    /**
     * @param totalCreditoAlunoMatriculaPeriodo the totalCreditoAlunoMatriculaPeriodo to set
     */
    public void setTotalCreditoAlunoMatriculaPeriodo(Integer totalCreditoAlunoMatriculaPeriodo) {
        this.totalCreditoAlunoMatriculaPeriodo = totalCreditoAlunoMatriculaPeriodo;
    }

    /**
     * @return the matriculaEspecial
     */
    public Boolean getMatriculaEspecial() {
        if (matriculaEspecial == null) {
            matriculaEspecial = Boolean.FALSE;
        }
        return matriculaEspecial;
    }

    /**
     * @param matriculaEspecial the matriculaEspecial to set
     */
    public void setMatriculaEspecial(Boolean matriculaEspecial) {
        this.matriculaEspecial = matriculaEspecial;
    }

    /**
     * @return the totalCargaHorariaPadraoAtePeriodoAnterior
     */
    public Integer getTotalCargaHorariaPadraoAtePeriodoAnterior() {
        if (totalCargaHorariaPadraoAtePeriodoAnterior == null) {
            totalCargaHorariaPadraoAtePeriodoAnterior = 0;
        }
        return totalCargaHorariaPadraoAtePeriodoAnterior;
    }

    /**
     * @param totalCargaHorariaPadraoAtePeriodoAnterior the totalCargaHorariaPadraoAtePeriodoAnterior to set
     */
    public void setTotalCargaHorariaPadraoAtePeriodoAnterior(Integer totalCargaHorariaPadraoAtePeriodoAnterior) {
        this.totalCargaHorariaPadraoAtePeriodoAnterior = totalCargaHorariaPadraoAtePeriodoAnterior;
    }

    /**
     * @return the totalCreditoPadraoAtePeriodoAnterior
     */
    public Integer getTotalCreditoPadraoAtePeriodoAnterior() {
        if (totalCreditoPadraoAtePeriodoAnterior == null) {
            totalCreditoPadraoAtePeriodoAnterior = 0;
        }
        return totalCreditoPadraoAtePeriodoAnterior;
    }

    /**
     * @param totalCreditoPadraoAtePeriodoAnterior the totalCreditoPadraoAtePeriodoAnterior to set
     */
    public void setTotalCreditoPadraoAtePeriodoAnterior(Integer totalCreditoPadraoAtePeriodoAnterior) {
        this.totalCreditoPadraoAtePeriodoAnterior = totalCreditoPadraoAtePeriodoAnterior;
    }

    /**
     * @return the totalCargaHorariaAlunoAtePeriodoAnterior
     */
    public Integer getTotalCargaHorariaAlunoAtePeriodoAnterior() {
        if (totalCargaHorariaAlunoAtePeriodoAnterior == null) {
            totalCargaHorariaAlunoAtePeriodoAnterior = 0;
        }
        return totalCargaHorariaAlunoAtePeriodoAnterior;
    }

    /**
     * @param totalCargaHorariaAlunoAtePeriodoAnterior the totalCargaHorariaAlunoAtePeriodoAnterior to set
     */
    public void setTotalCargaHorariaAlunoAtePeriodoAnterior(Integer totalCargaHorariaAlunoAtePeriodoAnterior) {
        this.totalCargaHorariaAlunoAtePeriodoAnterior = totalCargaHorariaAlunoAtePeriodoAnterior;
    }

    /**
     * @return the totalCreditoAlunoAtePeriodoAnterior
     */
    public Integer getTotalCreditoAlunoAtePeriodoAnterior() {
        if (totalCreditoAlunoAtePeriodoAnterior == null) {
            totalCreditoAlunoAtePeriodoAnterior = 0;
        }
        return totalCreditoAlunoAtePeriodoAnterior;
    }

    /**
     * @param totalCreditoAlunoAtePeriodoAnterior the totalCreditoAlunoAtePeriodoAnterior to set
     */
    public void setTotalCreditoAlunoAtePeriodoAnterior(Integer totalCreditoAlunoAtePeriodoAnterior) {
        this.totalCreditoAlunoAtePeriodoAnterior = totalCreditoAlunoAtePeriodoAnterior;
    }

    /**
     * @return the mensagemRestricaoPeriodosLetivosRenovacao
     */
    public String getMensagemRestricaoPeriodosLetivosRenovacao() {
        if (mensagemRestricaoPeriodosLetivosRenovacao == null) {
            mensagemRestricaoPeriodosLetivosRenovacao = "";
        }
        return mensagemRestricaoPeriodosLetivosRenovacao;
    }

    /**
     * @param mensagemRestricaoPeriodosLetivosRenovacao the mensagemRestricaoPeriodosLetivosRenovacao to set
     */
    public void setMensagemRestricaoPeriodosLetivosRenovacao(String mensagemRestricaoPeriodosLetivosRenovacao) {
        this.mensagemRestricaoPeriodosLetivosRenovacao = mensagemRestricaoPeriodosLetivosRenovacao;
    }

    /**
     * @return the listaPeriodosLetivosValidosParaMatriculaPeriodo
     */
    public List<PeriodoLetivoVO> getListaPeriodosLetivosValidosParaMatriculaPeriodo() {
        if (listaPeriodosLetivosValidosParaMatriculaPeriodo == null) {
            listaPeriodosLetivosValidosParaMatriculaPeriodo = new ArrayList<PeriodoLetivoVO>(0);
        }
        return listaPeriodosLetivosValidosParaMatriculaPeriodo;
    }

    /**
     * @param listaPeriodosLetivosValidosParaMatriculaPeriodo the listaPeriodosLetivosValidosParaMatriculaPeriodo to set
     */
    public void setListaPeriodosLetivosValidosParaMatriculaPeriodo(List<PeriodoLetivoVO> listaPeriodosLetivosValidosParaMatriculaPeriodo) {
        this.listaPeriodosLetivosValidosParaMatriculaPeriodo = listaPeriodosLetivosValidosParaMatriculaPeriodo;
    }

    /**
     * @return the totalCHPendenteAlunoAtePeriodoAnterior
     */
    public Integer getTotalCHPendenteAlunoAtePeriodoAnterior() {
        if (totalCHPendenteAlunoAtePeriodoAnterior == null) {
            totalCHPendenteAlunoAtePeriodoAnterior = 0;
        }
        return totalCHPendenteAlunoAtePeriodoAnterior;
    }

    /**
     * @param totalCHPendenteAlunoAtePeriodoAnterior the totalCHPendenteAlunoAtePeriodoAnterior to set
     */
    public void setTotalCHPendenteAlunoAtePeriodoAnterior(Integer totalCHPendenteAlunoAtePeriodoAnterior) {
        this.totalCHPendenteAlunoAtePeriodoAnterior = totalCHPendenteAlunoAtePeriodoAnterior;
    }

    /**
     * @return the totalCHDisciplinasIncluidas
     */
    public Integer getTotalCHDisciplinasIncluidas() {
        if (totalCHDisciplinasIncluidas == null) {
            totalCHDisciplinasIncluidas = 0;
        }
        return totalCHDisciplinasIncluidas;
    }

    /**
     * @param totalCHDisciplinasIncluidas the totalCHDisciplinasIncluidas to set
     */
    public void setTotalCHDisciplinasIncluidas(Integer totalCHDisciplinasIncluidas) {
        this.totalCHDisciplinasIncluidas = totalCHDisciplinasIncluidas;
    }

    /**
     * @return the totalCreditosDisciplinasIncluidas
     */
    public Integer getTotalCreditosDisciplinasIncluidas() {
        if (totalCreditosDisciplinasIncluidas == null) {
            totalCreditosDisciplinasIncluidas = 0;
        }
        return totalCreditosDisciplinasIncluidas;
    }

    /**
     * @param totalCreditosDisciplinasIncluidas the totalCreditosDisciplinasIncluidas to set
     */
    public void setTotalCreditosDisciplinasIncluidas(Integer totalCreditosDisciplinasIncluidas) {
        this.totalCreditosDisciplinasIncluidas = totalCreditosDisciplinasIncluidas;
    }

    /**
     * @return the totalCreditoPendenteAlunoAtePeriodoAnterior
     */
    public Integer getTotalCreditoPendenteAlunoAtePeriodoAnterior() {
        if (totalCreditoPendenteAlunoAtePeriodoAnterior == null) {
            totalCreditoPendenteAlunoAtePeriodoAnterior = 0;
        }
        return totalCreditoPendenteAlunoAtePeriodoAnterior;
    }

    /**
     * @param totalCreditoPendenteAlunoAtePeriodoAnterior the totalCreditoPendenteAlunoAtePeriodoAnterior to set
     */
    public void setTotalCreditoPendenteAlunoAtePeriodoAnterior(Integer totalCreditoPendenteAlunoAtePeriodoAnterior) {
        this.totalCreditoPendenteAlunoAtePeriodoAnterior = totalCreditoPendenteAlunoAtePeriodoAnterior;
    }

    /**
     * @return the saldoCHDisponivelInclusaoDisplinas
     */
    public Integer getSaldoCHDisponivelInclusaoDisplinas() {
        if (saldoCHDisponivelInclusaoDisplinas == null) {
            saldoCHDisponivelInclusaoDisplinas = 0;
        }
        return saldoCHDisponivelInclusaoDisplinas;
    }

    /**
     * @param saldoCHDisponivelInclusaoDisplinas the saldoCHDisponivelInclusaoDisplinas to set
     */
    public void setSaldoCHDisponivelInclusaoDisplinas(Integer saldoCHDisponivelInclusaoDisplinas) {
        this.saldoCHDisponivelInclusaoDisplinas = saldoCHDisponivelInclusaoDisplinas;
    }

    /**
     * @return the saldoCreditoDisponivelInclusaoDisplinas
     */
    public Integer getSaldoCreditoDisponivelInclusaoDisplinas() {
        if (saldoCreditoDisponivelInclusaoDisplinas == null) {
            saldoCreditoDisponivelInclusaoDisplinas = 0;
        }
        return saldoCreditoDisponivelInclusaoDisplinas;
    }

    /**
     * @param saldoCreditoDisponivelInclusaoDisplinas the saldoCreditoDisponivelInclusaoDisplinas to set
     */
    public void setSaldoCreditoDisponivelInclusaoDisplinas(Integer saldoCreditoDisponivelInclusaoDisplinas) {
        this.saldoCreditoDisponivelInclusaoDisplinas = saldoCreditoDisponivelInclusaoDisplinas;
    }
        
    /**
     * Retorna o total da carga horaria de disciplinas pendentes (de periodos anteriores ao qual o
     * aluno está renovando) que já foi adicionada para o aluno estudar.
     */
    public Integer calcularNrDisciplinasPendentesPeriodosAnterioresAdicionadasParaMatriculaPeriodo(Boolean desconsiderarDisciplinaOptativa) {
        if (this.getMatriculaPeriodoTumaDisciplinaVOs().isEmpty()) {
            return 0;
        }
        int totalDisciplinas = 0;
        Integer periodoLetivoRenovacao = this.getPeriodoLetivo().getPeriodoLetivo();
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : this.getMatriculaPeriodoTumaDisciplinaVOs()) {
            if ((!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao())            	
                 && (!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa())) {
                if (matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getPeriodoLetivoVO().getPeriodoLetivo().compareTo(periodoLetivoRenovacao) < 0
                		&& (!desconsiderarDisciplinaOptativa || (desconsiderarDisciplinaOptativa && !matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getIsDisciplinaOptativa()))) {
                    totalDisciplinas += 1;
                }
            }
        }
        return totalDisciplinas;
    }
	
    /**
     * Retorna o total da carga horaria de disciplinas pendentes (de periodos anteriores ao qual o
     * aluno está renovando) que já foi adicionada para o aluno estudar.
     */
    public Integer calcularCargaHorariaDisciplinasPeriodosAnterioresAdicionadasParaMatriculaPeriodo(MatriculaVO matriculaVO) {
        if (this.getMatriculaPeriodoTumaDisciplinaVOs().isEmpty()) {
            return 0;
        }
        int totalCargaHorariaIncluidoPeriodosAnteriores = 0;
        Integer periodoLetivoRenovacao = this.getPeriodoLetivo().getPeriodoLetivo();
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : this.getMatriculaPeriodoTumaDisciplinaVOs()) {        	
            if (!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao()
            		&& !matriculaPeriodoTurmaDisciplinaVO.getDisciplinaEquivale()
                    && !matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa()) {
            	if (matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getPeriodoLetivoVO().getPeriodoLetivo().compareTo(periodoLetivoRenovacao) < 0) {
            		totalCargaHorariaIncluidoPeriodosAnteriores += matriculaPeriodoTurmaDisciplinaVO.getCargaHorariaDisciplina();
                }
            } else if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaEquivale() 
            		&& matriculaVO != null 
            		&& matriculaVO.getMatriculaComHistoricoAlunoVO().getIsInicializado()) {
            	for (MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatriz : matriculaPeriodoTurmaDisciplinaVO.getMapaEquivalenciaDisciplinaVOIncluir().getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
    				for(PeriodoLetivoVO periodoLetivoVO2 : matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs()) {
    					for(GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO2.getGradeDisciplinaVOs()) {
    						if(gradeDisciplinaVO.getDisciplina().getCodigo().equals(mapaEquivalenciaDisciplinaMatriz.getDisciplinaVO().getCodigo()) && 
    								periodoLetivoVO2.getPeriodoLetivo().compareTo(periodoLetivoRenovacao) < 0) {
    							totalCargaHorariaIncluidoPeriodosAnteriores += gradeDisciplinaVO.getCargaHoraria();
    						}
    					}
    				}
    			}
            }
        }
        return totalCargaHorariaIncluidoPeriodosAnteriores;
    }

    /**
     * Retorna o total da carga horaria de disciplinas pendentes (de periodos anteriores ao qual o
     * aluno está renovando) que já foi adicionada para o aluno estudar.
     */
    public Integer calcularCreditosDisciplinasPeriodosAnterioresAdicionadasParaMatriculaPeriodo() {
        if (this.getMatriculaPeriodoTumaDisciplinaVOs().isEmpty()) {
            return 0;  
        }
        int totalCreditosIncluidoPeriodosAnteriores = 0;
        Integer periodoLetivoRenovacao = this.getPeriodoLetivo().getPeriodoLetivo();
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : this.getMatriculaPeriodoTumaDisciplinaVOs()) {
            if ((!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao())
                    && (!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa())) {
            	if (matriculaPeriodoTurmaDisciplinaVO.getGradeDisciplinaVO().getPeriodoLetivoVO().getPeriodoLetivo().compareTo(periodoLetivoRenovacao) < 0) {
                    totalCreditosIncluidoPeriodosAnteriores += matriculaPeriodoTurmaDisciplinaVO.getCreditosDisciplina();
                }
            }
        }
        return totalCreditosIncluidoPeriodosAnteriores;
    }

    /**
     * Retorna o total da carga horaria de disciplinas de Grupo Optativas pendentes (de periodos anteriores ao qual o
     * aluno está renovando) que já foi adicionada para o aluno estudar.
     */
    public Integer calcularCargaHorariaGrupoOptativaPeriodosAnterioresAdicionadasParaMatriculaPeriodo() {
        if (this.getMatriculaPeriodoTumaDisciplinaVOs().isEmpty()) {
            return 0;
        }
        int totalCargaHorariaGrupoOptativaPeriodosAnteriores = 0;
        Integer periodoLetivoRenovacao = this.getPeriodoLetivo().getPeriodoLetivo();
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : this.getMatriculaPeriodoTumaDisciplinaVOs()) {
            if ((!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao())
                    && (!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaEquivale())
                    && (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa())) {

                if (matriculaPeriodoTurmaDisciplinaVO.getTurma().getPeridoLetivo().getPeriodoLetivo().compareTo(periodoLetivoRenovacao) < 0) {
                    totalCargaHorariaGrupoOptativaPeriodosAnteriores += matriculaPeriodoTurmaDisciplinaVO.getCargaHorariaDisciplina();
                }
            }
        }
        return totalCargaHorariaGrupoOptativaPeriodosAnteriores;
    }

    public Integer calcularNrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo() {
        Integer totalCreditosGrupoOptativa = 0;
        Integer codigoPeriodoLetivo = this.getPeriodoLetivo().getCodigo();
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : this.getMatriculaPeriodoTumaDisciplinaVOs()) {
            if ((!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao())
                    && (!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaEquivale())
                    && (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa())) {
                if (matriculaPeriodoTurmaDisciplinaVO.getTurma().getPeridoLetivo().getCodigo().equals(codigoPeriodoLetivo)) {
                    totalCreditosGrupoOptativa += matriculaPeriodoTurmaDisciplinaVO.getCreditosDisciplina();
                }
            }
        }
        return totalCreditosGrupoOptativa;
    }

    public Integer calcularCargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo() {
        Integer totalCargaHorariaGrupoOptativa = 0;
        //Integer codigoPeriodoLetivo = this.getPeriodoLetivo().getCodigo();
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : this.getMatriculaPeriodoTumaDisciplinaVOs()) {            
                       
        	if ((!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao())
                    && (!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaEquivale())
                    && (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa())
                    && matriculaPeriodoTurmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeCurricularGrupoOptativa().getCodigo().equals(getPeriodoLetivo().getGradeCurricularGrupoOptativa().getCodigo())) {
        		                
               
                	totalCargaHorariaGrupoOptativa += matriculaPeriodoTurmaDisciplinaVO.getCargaHorariaDisciplina();
                    
            }
        }
        return totalCargaHorariaGrupoOptativa;
    }

    public Integer calcularCargaHorariaDisciplinasGrupoOptativaPeriodoLetivo(Integer nrPeriodoLetivo) {
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : this.getMatriculaPeriodoTumaDisciplinaVOs()) {
            Integer totalCargaHorariaGrupoOptativa = 0;
            if ((!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaFazParteComposicao())
                    && (!matriculaPeriodoTurmaDisciplinaVO.getDisciplinaEquivale())
                    && (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa())) {
                if (matriculaPeriodoTurmaDisciplinaVO.getTurma().getPeridoLetivo().getPeriodoLetivo().equals(nrPeriodoLetivo)) {
                    totalCargaHorariaGrupoOptativa += matriculaPeriodoTurmaDisciplinaVO.getCargaHorariaDisciplina();
                }
            }
            return totalCargaHorariaGrupoOptativa;
        }
        return 0;
    }

    /**
     * @return the totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior
     */
    public Integer getTotalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior() {
        if (totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior == null) {
            totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior = 0;
        }
        return totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior;
    }

    /**
     * @param totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior the totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior to set
     */
    public void setTotalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior(Integer totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior) {
        this.totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior = totalCreditoIncluidoAlunoDisciplinasAtePeriodoAnterior;
    }

    /**
     * @return the totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior
     */
    public Integer getTotalCHIncluidoAlunoDisciplinasAtePeriodoAnterior() {
        if (totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior == null) {
            totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior = 0;
        }
        return totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior;
    }

    /**
     * @param totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior the totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior to set
     */
    public void setTotalCHIncluidoAlunoDisciplinasAtePeriodoAnterior(Integer totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior) {
        this.totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior = totalCHIncluidoAlunoDisciplinasAtePeriodoAnterior;
    }

    /**
     * @return the totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior
     */
    public Integer getTotalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior() {
        if (totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior == null) {
            totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior = 0;
        }
        return totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior;
    }

    /**
     * @param totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior the totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior to set
     */
    public void setTotalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior(Integer totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior) {
        this.totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior = totalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior;
    }

    /**
     * @return the totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior
     */
    public Integer getTotalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior() {
        if (totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior == null) {
            totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior = 0;
        }
        return totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior;
    }

    /**
     * @param totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior the totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior to set
     */
    public void setTotalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior(Integer totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior) {
        this.totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior = totalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior;
    }

    public Integer getSaldoCHPendenteAlunoGrupoOptativaAtePeriodoAnterior() {
        Integer saldo = this.getTotalCHPendenteAlunoGrupoOptativaAtePeriodoAnterior()
                - this.getTotalCHIncluidoAlunoGrupoOptativaAtePeriodoAnterior();
        if (saldo < 0) {
            saldo = 0;
        }
        return saldo;
    }

    public Integer getSaldoCHPendenteAlunoAtePeriodoAnterior() {
        Integer saldo = this.getTotalCHPendenteAlunoAtePeriodoAnterior()
                - this.getTotalCHIncluidoAlunoDisciplinasAtePeriodoAnterior();
        if (saldo < 0) {
            saldo = 0;
        }
        return saldo;
    }

    /**
     * @return the nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo
     */
    public Integer getNrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo() {
        if (nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo == null) {
            nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo = 0;
        }
        return nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo;
    }

    /**
     * @param nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo the nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo to set
     */
    public void setNrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo(Integer nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo) {
        this.nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo = nrCreditosDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo;
    }

    /**
     * @return the cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo
     */
    public Integer getCargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo() {
        if (cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo == null) {
            cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo = 0;
        }
        return cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo;
    }

    /**
     * @param cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo the cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo to set
     */
    public void setCargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo(Integer cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo) {
        this.cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo = cargaHorariaDisciplinasGrupoOptativaPeriodoLetivoMatriculaPeriodo;
    }

    /**
     * @return the alunoConcordaComTermoRenovacaoOnline
     */
    public Boolean getAlunoConcordaComTermoRenovacaoOnline() {
        if (alunoConcordaComTermoRenovacaoOnline == null) {
            alunoConcordaComTermoRenovacaoOnline = Boolean.FALSE;
        }
        return alunoConcordaComTermoRenovacaoOnline;
    }

    /**
     * @param alunoConcordaComTermoRenovacaoOnline the alunoConcordaComTermoRenovacaoOnline to set
     */
    public void setAlunoConcordaComTermoRenovacaoOnline(Boolean alunoConcordaComTermoRenovacaoOnline) {
        this.alunoConcordaComTermoRenovacaoOnline = alunoConcordaComTermoRenovacaoOnline;
    }

    /**
     * @return the planoFinanceiroCursoPersistido
     */
//    public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoPersistido() {
//        if (planoFinanceiroCursoPersistido == null) {
//            planoFinanceiroCursoPersistido = new PlanoFinanceiroCursoVO();
//        }
//        return planoFinanceiroCursoPersistido;
//    }
//
//    /**
//     * @param planoFinanceiroCursoPersistido the planoFinanceiroCursoPersistido to set
//     */
//    public void setPlanoFinanceiroCursoPersistido(PlanoFinanceiroCursoVO planoFinanceiroCursoPersistido) {
//        this.planoFinanceiroCursoPersistido = planoFinanceiroCursoPersistido;
//    }
//
//    /**
//     * @return the condicaoPagamentoPlanoFinanceiroCursoPersistido
//     */
//    public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoPersistido() {
//        if (condicaoPagamentoPlanoFinanceiroCursoPersistido == null) {
//            condicaoPagamentoPlanoFinanceiroCursoPersistido = new CondicaoPagamentoPlanoFinanceiroCursoVO();
//        }
//        return condicaoPagamentoPlanoFinanceiroCursoPersistido;
//    }
//
//    /**
//     * @param condicaoPagamentoPlanoFinanceiroCursoPersistido the condicaoPagamentoPlanoFinanceiroCursoPersistido to set
//     */
//    public void setCondicaoPagamentoPlanoFinanceiroCursoPersistido(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoPersistido) {
//        this.condicaoPagamentoPlanoFinanceiroCursoPersistido = condicaoPagamentoPlanoFinanceiroCursoPersistido;
//    }

    /**
     * @return the alterarPlanoCondicacaoPagamentoPersistido
     */
    public Boolean getAlterarPlanoCondicacaoPagamentoPersistido() {
        if (alterarPlanoCondicacaoPagamentoPersistido == null) {
            alterarPlanoCondicacaoPagamentoPersistido = Boolean.FALSE;
        }
        return alterarPlanoCondicacaoPagamentoPersistido;
    }

    /**
     * @param alterarPlanoCondicacaoPagamentoPersistido the alterarPlanoCondicacaoPagamentoPersistido to set
     */
    public void setAlterarPlanoCondicacaoPagamentoPersistido(Boolean alterarPlanoCondicacaoPagamentoPersistido) {
        this.alterarPlanoCondicacaoPagamentoPersistido = alterarPlanoCondicacaoPagamentoPersistido;
    }

	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs() {
		if (matriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs == null) {
			matriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return matriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs;
	}
	

	public void setMatriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs) {
		this.matriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs = matriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs;
	}
    
    public boolean getExisteNovaMatriculaPeriodoTurmaDisciplina() {
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoVerificarVO : this.getMatriculaPeriodoTumaDisciplinaVOs()) {
            if (matriculaPeriodoVerificarVO.getCodigo().equals(0) || matriculaPeriodoVerificarVO.isAlterandoSubturmaPraticaTeorica()) {
                return true;
            }
        }
        return false;
    }

    

   

	public Boolean getUsuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo() {
		if (usuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo == null) {
			usuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo = false;
		}
		return usuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo;
	}

	public void setUsuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo(Boolean usuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo) {
		this.usuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo = usuarioLiberouIncluaoDisciplinaAcimaLimiteMaxPeriodoLetivo;
	}

	public String getObservacaoCriterioAvaliacao() {
		if (observacaoCriterioAvaliacao == null) {
			observacaoCriterioAvaliacao = "";
		}
		return observacaoCriterioAvaliacao;
	}

	public void setObservacaoCriterioAvaliacao(String observacaoCriterioAvaliacao) {
		this.observacaoCriterioAvaliacao = observacaoCriterioAvaliacao;
	}

    /**
     * @return the expedicaoDiplomaVO
     */
    public ExpedicaoDiplomaVO getExpedicaoDiplomaVO() {
        if (expedicaoDiplomaVO == null) {
            expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
        }
        return expedicaoDiplomaVO;
    }

    /**
     * @param expedicaoDiplomaVO the expedicaoDiplomaVO to set
     */
    public void setExpedicaoDiplomaVO(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
        this.expedicaoDiplomaVO = expedicaoDiplomaVO;
    }

    /**
     * @return the alunoRegularMatrizCurricular
     */
    public Boolean getAlunoRegularMatrizCurricular() {
        if (alunoRegularMatrizCurricular == null) { 
            alunoRegularMatrizCurricular = Boolean.FALSE;
        }
        return alunoRegularMatrizCurricular;
    }

    /**
     * @param alunoRegularMatrizCurricular the alunoRegularMatrizCurricular to set
     */
    public void setAlunoRegularMatrizCurricular(Boolean alunoRegularMatrizCurricular) {
        this.alunoRegularMatrizCurricular = alunoRegularMatrizCurricular;
    }

    /**
     * @return the nrDisciplinasPendentesPeriodosAnteriores
     */
    public Integer getNrDisciplinasPendentesPeriodosAnteriores() {
        if (nrDisciplinasPendentesPeriodosAnteriores == null) {
            nrDisciplinasPendentesPeriodosAnteriores = 0;
        }
        return nrDisciplinasPendentesPeriodosAnteriores;
    }

    /**
     * @param nrDisciplinasPendentesPeriodosAnteriores the nrDisciplinasPendentesPeriodosAnteriores to set
     */
    public void setNrDisciplinasPendentesPeriodosAnteriores(Integer nrDisciplinasPendentesPeriodosAnteriores) {
        this.nrDisciplinasPendentesPeriodosAnteriores = nrDisciplinasPendentesPeriodosAnteriores;
    }

    /**
     * @return the nrDisciplinasPendentesDevemSerIncluidas
     */
    public Integer getNrDisciplinasPendentesDevemSerIncluidas() {
        if (nrDisciplinasPendentesDevemSerIncluidas == null) { 
            nrDisciplinasPendentesDevemSerIncluidas = 0;
        }
        return nrDisciplinasPendentesDevemSerIncluidas;
    }

    /**
     * @param nrDisciplinasPendentesDevemSerIncluidas the nrDisciplinasPendentesDevemSerIncluidas to set
     */
    public void setNrDisciplinasPendentesDevemSerIncluidas(Integer nrDisciplinasPendentesDevemSerIncluidas) {
        this.nrDisciplinasPendentesDevemSerIncluidas = nrDisciplinasPendentesDevemSerIncluidas;
    }

    /**
     * @return the nrDisciplinasIncluidasPeriodosAnteriores
     */
    public Integer getNrDisciplinasIncluidasPeriodosAnteriores() {
        if (nrDisciplinasIncluidasPeriodosAnteriores == null) { 
            nrDisciplinasIncluidasPeriodosAnteriores = 0;
        }
        return nrDisciplinasIncluidasPeriodosAnteriores;
    }

    /**
     * @param nrDisciplinasIncluidasPeriodosAnteriores the nrDisciplinasIncluidasPeriodosAnteriores to set
     */
    public void setNrDisciplinasIncluidasPeriodosAnteriores(Integer nrDisciplinasIncluidasPeriodosAnteriores) {
        this.nrDisciplinasIncluidasPeriodosAnteriores = nrDisciplinasIncluidasPeriodosAnteriores;
    }
    
    public Boolean getLiberadoControleInclusaoDisciplinaPeriodoFuturo() {
        if (liberadoControleInclusaoDisciplinaPeriodoFuturo == null) { 
            liberadoControleInclusaoDisciplinaPeriodoFuturo = Boolean.FALSE;
        }
        return liberadoControleInclusaoDisciplinaPeriodoFuturo;
    }

    public void setLiberadoControleInclusaoDisciplinaPeriodoFuturo(Boolean liberadoControleInclusaoDisciplinaPeriodoFuturo) {
        this.liberadoControleInclusaoDisciplinaPeriodoFuturo = liberadoControleInclusaoDisciplinaPeriodoFuturo;
	}

	public Integer getNrCreditoDiscplinasOptativasInclusaoForaPrazo() {
		if (nrCreditoDiscplinasOptativasInclusaoForaPrazo == null) {
			nrCreditoDiscplinasOptativasInclusaoForaPrazo = 0;
		}
		return nrCreditoDiscplinasOptativasInclusaoForaPrazo;
	}

	public void setNrCreditoDiscplinasOptativasInclusaoForaPrazo(Integer nrCreditoDiscplinasOptativasInclusaoForaPrazo) {
		this.nrCreditoDiscplinasOptativasInclusaoForaPrazo = nrCreditoDiscplinasOptativasInclusaoForaPrazo;
	}

	public Integer getNrCargaHorariaDiscplinasOptativasInclusaoForaPrazo() {
		if (nrCargaHorariaDiscplinasOptativasInclusaoForaPrazo == null) {
			nrCargaHorariaDiscplinasOptativasInclusaoForaPrazo = 0;
		}
		return nrCargaHorariaDiscplinasOptativasInclusaoForaPrazo;
	}

	public void setNrCargaHorariaDiscplinasOptativasInclusaoForaPrazo(Integer nrCargaHorariaDiscplinasOptativasInclusaoForaPrazo) {
		this.nrCargaHorariaDiscplinasOptativasInclusaoForaPrazo = nrCargaHorariaDiscplinasOptativasInclusaoForaPrazo;
	}

	public Integer getTotalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo() {
		if (totalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo == null) {
			totalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo = 0;
		}
		return totalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo;
	}

	public void setTotalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo(Integer totalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo) {
		this.totalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo = totalCreditoAlunoMatriculaPeriodoInclusaoForaPrazo;
	}

	public Integer getTotalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo() {
		if (totalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo == null) {
			totalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo = 0;
		}
		return totalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo;
	}

	public void setTotalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo(Integer totalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo) {
		this.totalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo = totalCargaHorariaAlunoMatriculaPeriodoInclusaoForaPrazo;
	}

	public Integer getNrDisciplinasIncluidasInclusaoForaPrazo() {
		if (nrDisciplinasIncluidasInclusaoForaPrazo == null) {
			nrDisciplinasIncluidasInclusaoForaPrazo = 0;
		}
		return nrDisciplinasIncluidasInclusaoForaPrazo;
	}

	public void setNrDisciplinasIncluidasInclusaoForaPrazo(Integer nrDisciplinasIncluidasInclusaoForaPrazo) {
		this.nrDisciplinasIncluidasInclusaoForaPrazo = nrDisciplinasIncluidasInclusaoForaPrazo;
	}

	public Integer getTotalCHDisciplinasIncluidasInclusaoForaPrazo() {
		if (totalCHDisciplinasIncluidasInclusaoForaPrazo == null) {
			totalCHDisciplinasIncluidasInclusaoForaPrazo = 0;
		}
		return totalCHDisciplinasIncluidasInclusaoForaPrazo;
	}

	public void setTotalCHDisciplinasIncluidasInclusaoForaPrazo(Integer totalCHDisciplinasIncluidasInclusaoForaPrazo) {
		this.totalCHDisciplinasIncluidasInclusaoForaPrazo = totalCHDisciplinasIncluidasInclusaoForaPrazo;
	}

	public Integer getTotalCreditosDisciplinasIncluidasInclusaoForaPrazo() {
		if (totalCreditosDisciplinasIncluidasInclusaoForaPrazo == null) {
			totalCreditosDisciplinasIncluidasInclusaoForaPrazo = 0;
		}
		return totalCreditosDisciplinasIncluidasInclusaoForaPrazo;
	}

	public void setTotalCreditosDisciplinasIncluidasInclusaoForaPrazo(Integer totalCreditosDisciplinasIncluidasInclusaoForaPrazo) {
		this.totalCreditosDisciplinasIncluidasInclusaoForaPrazo = totalCreditosDisciplinasIncluidasInclusaoForaPrazo;
	}

    public Boolean getLiberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular() {
        if (liberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular == null) { 
            liberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular = Boolean.FALSE;
        }
        return liberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular;
    }

    public void setLiberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular(Boolean liberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular) {
        this.liberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular = liberadoInclusaoDisciplinaPeriodoLetivoFuturoAlunoRegular;
    }
    
    public Boolean getLiberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular() {
        if (liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular == null) { 
            liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular = Boolean.FALSE;
        }
        return liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular;
    }

    public void setLiberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular(Boolean liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular) {
        this.liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular = liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoRegular;
    }
    
    public Boolean getLiberadoControleInclusaoObrigatoriaDisciplinaDependencia() {
        if (liberadoControleInclusaoObrigatoriaDisciplinaDependencia == null) { 
            liberadoControleInclusaoObrigatoriaDisciplinaDependencia = Boolean.FALSE;
        }
        return liberadoControleInclusaoObrigatoriaDisciplinaDependencia;
    }

    public void setLiberadoControleInclusaoObrigatoriaDisciplinaDependencia(Boolean liberadoControleInclusaoObrigatoriaDisciplinaDependencia) {
        this.liberadoControleInclusaoObrigatoriaDisciplinaDependencia = liberadoControleInclusaoObrigatoriaDisciplinaDependencia;
    }
    
    public Boolean getLiberadoControleInclusaoMinimaObrigatoriaDisciplina() {
		if (liberadoControleInclusaoMinimaObrigatoriaDisciplina == null) {
			liberadoControleInclusaoMinimaObrigatoriaDisciplina = false;
		}
		return liberadoControleInclusaoMinimaObrigatoriaDisciplina;
	}

	public void setLiberadoControleInclusaoMinimaObrigatoriaDisciplina(Boolean liberadoControleInclusaoMinimaObrigatoriaDisciplina) {
		this.liberadoControleInclusaoMinimaObrigatoriaDisciplina = liberadoControleInclusaoMinimaObrigatoriaDisciplina;
	}

	public String getOrdenacao(){
    	return getAno()+getSemestre()+((("AT, PR, FI, FO").contains(getSituacaoMatriculaPeriodo()))?"0":"1")+getCodigo();
    }
    
    public Boolean getOrigemMapaRegistroTrancamentoAbandono() {
		if (origemMapaRegistroTrancamentoAbandono == null) {
			origemMapaRegistroTrancamentoAbandono = false;
		}
		return origemMapaRegistroTrancamentoAbandono;
	}

	public void setOrigemMapaRegistroTrancamentoAbandono(Boolean origemMapaRegistroTrancamentoAbandono) {
		this.origemMapaRegistroTrancamentoAbandono = origemMapaRegistroTrancamentoAbandono;
	}
	
	public String getAnoSemestreCodigo() {
		return getAno() + "/" + getSemestre() + getCodigo().toString();
	}
	
	public Boolean getUltimaMatriculaPeriodoAluno() {
		if (ultimaMatriculaPeriodoAluno == null) {
			ultimaMatriculaPeriodoAluno = Boolean.FALSE;
		}
		return ultimaMatriculaPeriodoAluno;
	}

	public void setUltimaMatriculaPeriodoAluno(Boolean ultimaMatriculaPeriodoAluno) {
		this.ultimaMatriculaPeriodoAluno = ultimaMatriculaPeriodoAluno;
	}
	
	public Boolean getPermitirAtivarUltimaMatriculaPeriodoSituacaoFinalizada() {
		return (this.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor()) && (this.getMatriculaVO().getSituacao().equals(SituacaoVinculoMatricula.ATIVA.getValor()) || this.getMatriculaVO().getSituacao().equals(SituacaoVinculoMatricula.FINALIZADA.getValor())));
 
	}
	
	public Boolean getPermitirAtivarUltimaMatriculaPeriodoSituacaoFormada(){
		return (this.getUltimaMatriculaPeriodoAluno() && this.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor()) && this.getMatriculaVO().getSituacao().equals(SituacaoVinculoMatricula.FORMADO.getValor()));
	}
	

	public Integer getNrCreditoPendentesDevemSerIncluidas() {
		if(nrCreditoPendentesDevemSerIncluidas == null){
			nrCreditoPendentesDevemSerIncluidas = 0;
		}
		return nrCreditoPendentesDevemSerIncluidas;
	}

	public void setNrCreditoPendentesDevemSerIncluidas(Integer nrCreditoPendentesDevemSerIncluidas) {
		this.nrCreditoPendentesDevemSerIncluidas = nrCreditoPendentesDevemSerIncluidas;
	}

	public Integer getNrCargaHorariaPendentesDevemSerIncluidas() {
		if(nrCargaHorariaPendentesDevemSerIncluidas == null){
			nrCargaHorariaPendentesDevemSerIncluidas = 0;
		}
		return nrCargaHorariaPendentesDevemSerIncluidas;
	}

	public void setNrCargaHorariaPendentesDevemSerIncluidas(Integer nrCargaHorariaPendentesDevemSerIncluidas) {
		this.nrCargaHorariaPendentesDevemSerIncluidas = nrCargaHorariaPendentesDevemSerIncluidas;
	}

	public TipoContabilizacaoDisciplinaDependenciaEnum getTipoContabilizacaoDisciplinaDependencia() {
		if(tipoContabilizacaoDisciplinaDependencia == null){
			tipoContabilizacaoDisciplinaDependencia = TipoContabilizacaoDisciplinaDependenciaEnum.QTDE_DISCIPLINA;
		}
		return tipoContabilizacaoDisciplinaDependencia;
	}

	public void setTipoContabilizacaoDisciplinaDependencia(
			TipoContabilizacaoDisciplinaDependenciaEnum tipoContabilizacaoDisciplinaDependencia) {
		this.tipoContabilizacaoDisciplinaDependencia = tipoContabilizacaoDisciplinaDependencia;
	}	
	
	public TransferenciaMatrizCurricularMatriculaVO getTransferenciaMatrizCurricularMatriculaVO() {
		return transferenciaMatrizCurricularMatriculaVO;
	}

	public void setTransferenciaMatrizCurricularMatriculaVO(TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO) {
		this.transferenciaMatrizCurricularMatriculaVO = transferenciaMatrizCurricularMatriculaVO;
	}	
	
	
	

	public Double getValorMaterialDidaticoCheio() {
		if (valorMaterialDidaticoCheio == null) {
			valorMaterialDidaticoCheio = 0.0;
		}
		return valorMaterialDidaticoCheio;
	}

	public void setValorMaterialDidaticoCheio(Double valorMaterialDidaticoCheio) {
		this.valorMaterialDidaticoCheio = valorMaterialDidaticoCheio;
	}

	public Double getValorDescontoMaterialDidatico() {
		if (valorDescontoMaterialDidatico == null) {
			valorDescontoMaterialDidatico = 0.0;
		}
		return valorDescontoMaterialDidatico;
	}

	public void setValorDescontoMaterialDidatico(Double valorDescontoMaterialDidatico) {
		this.valorDescontoMaterialDidatico = valorDescontoMaterialDidatico;
	}

	public Double getValorDescontoConvenioMaterialDidatico() {
		if (valorDescontoConvenioMaterialDidatico == null) {
			valorDescontoConvenioMaterialDidatico = 0.0;
		}
		return valorDescontoConvenioMaterialDidatico;
	}

	public void setValorDescontoConvenioMaterialDidatico(Double valorDescontoConvenioMaterialDidatico) {
		this.valorDescontoConvenioMaterialDidatico = valorDescontoConvenioMaterialDidatico;
	}

	public Double getValorDescontoInstituicaoMaterialDidatico() {
		if (valorDescontoInstituicaoMaterialDidatico == null) {
			valorDescontoInstituicaoMaterialDidatico = 0.0;
		}
		return valorDescontoInstituicaoMaterialDidatico;
	}

	public void setValorDescontoInstituicaoMaterialDidatico(Double valorDescontoInstituicaoMaterialDidatico) {
		this.valorDescontoInstituicaoMaterialDidatico = valorDescontoInstituicaoMaterialDidatico;
	}

	public Double getValorFinalMaterialDidatico() {
		if (valorFinalMaterialDidatico == null) {
			valorFinalMaterialDidatico = 0.0;
		}
		return valorFinalMaterialDidatico;
	}

	public void setValorFinalMaterialDidatico(Double valorFinalMaterialDidatico) {
		this.valorFinalMaterialDidatico = valorFinalMaterialDidatico;
	}

	public Double getValorMaterialDidaticoBolsaCusteada() {
		if (valorMaterialDidaticoBolsaCusteada == null) {
			valorMaterialDidaticoBolsaCusteada = 0.0;
		}
		return valorMaterialDidaticoBolsaCusteada;
	}

	public void setValorMaterialDidaticoBolsaCusteada(Double valorMaterialDidaticoBolsaCusteada) {
		this.valorMaterialDidaticoBolsaCusteada = valorMaterialDidaticoBolsaCusteada;
	}

	public Double getValorTotalTodosDescontosMaterialDidatico() {
		if (valorTotalTodosDescontosMaterialDidatico == null) {
			valorTotalTodosDescontosMaterialDidatico = 0.0;
		}
		return valorTotalTodosDescontosMaterialDidatico;
	}

	public void setValorTotalTodosDescontosMaterialDidatico(Double valorTotalTodosDescontosMaterialDidatico) {
		this.valorTotalTodosDescontosMaterialDidatico = valorTotalTodosDescontosMaterialDidatico;
	}

	public Double getValorResidualMaterialDidatico() {
		if (valorResidualMaterialDidatico == null) {
			valorResidualMaterialDidatico = 0.0;
		}
		return valorResidualMaterialDidatico;
	}

	public void setValorResidualMaterialDidatico(Double valorResidualMaterialDidatico) {
		this.valorResidualMaterialDidatico = valorResidualMaterialDidatico;
	}

	public Double getValorBaseDescontoConvenioMaterialDidatico() {
		if (valorBaseDescontoConvenioMaterialDidatico == null) {
			valorBaseDescontoConvenioMaterialDidatico = 0.0;
		}
		return valorBaseDescontoConvenioMaterialDidatico;
	}

	public void setValorBaseDescontoConvenioMaterialDidatico(Double valorBaseDescontoConvenioMaterialDidatico) {
		this.valorBaseDescontoConvenioMaterialDidatico = valorBaseDescontoConvenioMaterialDidatico;
	}

	public Date getDataBaseGeracaoParcelas() {
		return dataBaseGeracaoParcelas;
	}

	public void setDataBaseGeracaoParcelas(Date dataBaseGeracaoParcelas) {
		this.dataBaseGeracaoParcelas = dataBaseGeracaoParcelas;
	}

	public String getCssTimeLineFichaAluno() {
		if (getSituacaoMatriculaPeriodo().equals("AT")) {
			return "timeline-badge-ativo";
		} else if (getSituacaoMatriculaPeriodo().equals("CO") || getSituacaoMatriculaPeriodo().equals("FI")) {
			return "timeline-badge-concluido";
		} else if (getSituacaoMatriculaPeriodo().equals("PR")) {
			return "timeline-badge-preMatriculado";
		} else if (getSituacaoMatriculaPeriodo().equals("TR") 
				|| getSituacaoMatriculaPeriodo().equals("CA") 
				|| getSituacaoMatriculaPeriodo().equals("AC")
				|| getSituacaoMatriculaPeriodo().equals("TS")) {
			return "timeline-badge-evasao";
		} else if (getSituacaoMatriculaPeriodo().equals("FO")) {
			return "timeline-badge-formado";
		}
		return "timeline-badge";
	}
	
	

	public Double getValorConvenioNaoRestituirAluno() {
		if(valorConvenioNaoRestituirAluno == null){
			valorConvenioNaoRestituirAluno = 0.0;
		}
		return valorConvenioNaoRestituirAluno;
	}

	public void setValorConvenioNaoRestituirAluno(Double valorConvenioNaoRestituirAluno) {
		this.valorConvenioNaoRestituirAluno = valorConvenioNaoRestituirAluno;
	}

	
	
	private String descricaoDescontoConvenioNaoRestituirAluno;
	
	
	


	public Boolean getGravarContratoMatricula() {
		if(gravarContratoMatricula == null){
			gravarContratoMatricula = false;
		}
		return gravarContratoMatricula;
	}

	public void setGravarContratoMatricula(Boolean gravarContratoMatricula) {
		this.gravarContratoMatricula = gravarContratoMatricula;
	}

	public Boolean getGravarContratoFiador() {
		if(gravarContratoFiador == null){
			gravarContratoFiador = false;
		}
		return gravarContratoFiador;
	}

	public void setGravarContratoFiador(Boolean gravarContratoFiador) {
		this.gravarContratoFiador = gravarContratoFiador;
	}
	
	private Boolean calouro;

	public Boolean getCalouro() {
		if(calouro == null){
			calouro = false;
		}		
		return calouro;
	}

	public void setCalouro(Boolean calouro) {
		this.calouro = calouro;
	}

	public String getCategoriaCondicaoPagamento() {
		if(categoriaCondicaoPagamento == null) {
			categoriaCondicaoPagamento = "";
		}
		return categoriaCondicaoPagamento;
	}

	public void setCategoriaCondicaoPagamento(String categoriaCondicaoPagamento) {
		this.categoriaCondicaoPagamento = categoriaCondicaoPagamento;
	}
	
	public Date getDataAceitouTermoContratoRenovacaoOnline() {
		return dataAceitouTermoContratoRenovacaoOnline;
	}
	
	public void setDataAceitouTermoContratoRenovacaoOnline(Date dataAceitouTermoContratoRenovacaoOnline) {
		this.dataAceitouTermoContratoRenovacaoOnline = dataAceitouTermoContratoRenovacaoOnline;
	}
	
	//Data nao pode ser inicializado Pedro Andrade.
	public Date getDataVencimentoMatriculaEspecifico() {
		return dataVencimentoMatriculaEspecifico;
	}

	public void setDataVencimentoMatriculaEspecifico(Date dataVencimentoMatriculaEspecifico) {
		this.dataVencimentoMatriculaEspecifico = dataVencimentoMatriculaEspecifico;
	}

	/**
	 * ATRIBUTO TRANSIENT, SOMENTE PARA CONTROLES DA RENOVACAO DE MATRICULA
	 */
	private String apresentarConfirmacaoRenovacaoESolicitarCondicaoPagto;
	
	public String getApresentarConfirmacaoRenovacaoESolicitarCondicaoPagto() {
		if (apresentarConfirmacaoRenovacaoESolicitarCondicaoPagto == null) { 
			apresentarConfirmacaoRenovacaoESolicitarCondicaoPagto = "NAO_DEFINIDO";
		}
		return apresentarConfirmacaoRenovacaoESolicitarCondicaoPagto;
	}

	public void setApresentarConfirmacaoRenovacaoESolicitarCondicaoPagto(String apresentarConfirmacaoRenovacaoESolicitarCondicaoPagto) {
		this.apresentarConfirmacaoRenovacaoESolicitarCondicaoPagto = apresentarConfirmacaoRenovacaoESolicitarCondicaoPagto;
	}	
	
	public String getIdentificadorRenovacaoFollowUp() {
		return getSemestre() + "/" + getAno();
	}

	
	
	private ProcessoMatriculaVO processoMatriculaOriginal;
	private String anoSemestreOriginal; 

	public ProcessoMatriculaVO getProcessoMatriculaOriginal() {
		if (processoMatriculaOriginal == null) {
			try {
				processoMatriculaOriginal = (ProcessoMatriculaVO) getProcessoMatriculaVO().clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return processoMatriculaOriginal;
	}

	public void setProcessoMatriculaOriginal(ProcessoMatriculaVO processoMatriculaOriginal) {
		this.processoMatriculaOriginal = processoMatriculaOriginal;
	}

	public String getAnoSemestreOriginal() {
		if (anoSemestreOriginal == null) {
			anoSemestreOriginal = getAno()+"/"+getSemestre();
		}
		return anoSemestreOriginal;
	}

	public void setAnoSemestreOriginal(String anoSemestreOriginal) {
		this.anoSemestreOriginal = anoSemestreOriginal;
	}

	public List<HistoricoVO> getListaHistoricoDesistenciaEquivalenciaVOs() {
		if (listaHistoricoDesistenciaEquivalenciaVOs == null) {
			listaHistoricoDesistenciaEquivalenciaVOs = new ArrayList<HistoricoVO>(0);
		}
		return listaHistoricoDesistenciaEquivalenciaVOs;
	}

	public void setListaHistoricoDesistenciaEquivalenciaVOs(List<HistoricoVO> listaHistoricoDesistenciaEquivalenciaVOs) {
		this.listaHistoricoDesistenciaEquivalenciaVOs = listaHistoricoDesistenciaEquivalenciaVOs;
	}
	
	public Boolean getVerificarHistoricoDesistiuEquivalencia(HistoricoVO historicoVO) {
		for (HistoricoVO historicoDesistiuVO : getListaHistoricoDesistenciaEquivalenciaVOs()) {
			if (historicoDesistiuVO.getCodigo().equals(historicoVO.getCodigo())) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean getPermitirVisualizarAbaDescontos() {
		if (permitirVisualizarAbaDescontos == null) {
			permitirVisualizarAbaDescontos = false;
		}
		return permitirVisualizarAbaDescontos;
	}

	public void setPermitirVisualizarAbaDescontos(Boolean permitirVisualizarAbaDescontos) {
		this.permitirVisualizarAbaDescontos = permitirVisualizarAbaDescontos;
	}
	/**
	 * atributos transient para controlar acoes sobre conta a receber editadas manualmente
	 */
	private Boolean usuarioDefiniuAcaoSobreContasEdicaoManualAtiva;
	private Boolean existemContaReceberComEdicaoManualAtiva;
	private Boolean regerarContaReceberComEdicaoManualAtiva;	
	
	
    public Boolean getUsuarioDefiniuAcaoSobreContasEdicaoManualAtiva() {
		if (usuarioDefiniuAcaoSobreContasEdicaoManualAtiva == null) {
			usuarioDefiniuAcaoSobreContasEdicaoManualAtiva = Boolean.FALSE;
		}
		return usuarioDefiniuAcaoSobreContasEdicaoManualAtiva;
	}

	public void setUsuarioDefiniuAcaoSobreContasEdicaoManualAtiva(Boolean usuarioDefiniuAcaoSobreContasEdicaoManualAtiva) {
		this.usuarioDefiniuAcaoSobreContasEdicaoManualAtiva = usuarioDefiniuAcaoSobreContasEdicaoManualAtiva;
	}
    
    public Boolean getExistemContaReceberComEdicaoManualAtiva() {
		if (existemContaReceberComEdicaoManualAtiva == null) {
			existemContaReceberComEdicaoManualAtiva = Boolean.FALSE;
		}
		return existemContaReceberComEdicaoManualAtiva;
	}

	public void setExistemContaReceberComEdicaoManualAtiva(Boolean existemContaReceberComEdicaoManualAtiva) {
		this.existemContaReceberComEdicaoManualAtiva = existemContaReceberComEdicaoManualAtiva;
	}

	public Boolean getRegerarContaReceberComEdicaoManualAtiva() {
		if (regerarContaReceberComEdicaoManualAtiva == null) {
			regerarContaReceberComEdicaoManualAtiva = Boolean.FALSE;
		}
		return regerarContaReceberComEdicaoManualAtiva;
	}

	public void setRegerarContaReceberComEdicaoManualAtiva(Boolean regerarContaReceberComEdicaoManualAtiva) {
		this.regerarContaReceberComEdicaoManualAtiva = regerarContaReceberComEdicaoManualAtiva;
	}
 
	public Boolean getVindoTelaAlteracaoPlanoFinanceiroAluno() {
		if (vindoTelaAlteracaoPlanoFinanceiroAluno == null) {
			vindoTelaAlteracaoPlanoFinanceiroAluno = false;
		}
		return vindoTelaAlteracaoPlanoFinanceiroAluno;
	}

	public void setVindoTelaAlteracaoPlanoFinanceiroAluno(Boolean vindoTelaAlteracaoPlanoFinanceiroAluno) {
		this.vindoTelaAlteracaoPlanoFinanceiroAluno = vindoTelaAlteracaoPlanoFinanceiroAluno;
	}
 
	/**
	 * Esta Lista é usada na validação de choque de horário de cursos diferentes
	 */
	private Map<MatriculaPeriodoVO, List<MatriculaPeriodoTurmaDisciplinaVO>> matriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs;


	public  Map<MatriculaPeriodoVO, List<MatriculaPeriodoTurmaDisciplinaVO>> getMatriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs() {
		if (matriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs == null) {
			matriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs = new HashMap<MatriculaPeriodoVO, List<MatriculaPeriodoTurmaDisciplinaVO>>(0);
		}
		return matriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs( Map<MatriculaPeriodoVO, List<MatriculaPeriodoTurmaDisciplinaVO>> matriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs) {
		this.matriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs = matriculaPeriodoTurmaDisciplinaOutrasMatriculasAtivasVOs;
	}

	public Boolean getGravarContratoExtensao() {
		if(gravarContratoExtensao == null) {
			gravarContratoExtensao =  false;
		}
		return gravarContratoExtensao;
	}

	public void setGravarContratoExtensao(Boolean gravarContratoExtensao) {
		this.gravarContratoExtensao = gravarContratoExtensao;
	}
	
	@Override
	public String toString() {
		return "Matricula Periodo [" + this.getCodigo() + "]: " + 
                        " Matricula: " + this.getMatricula() + 
                        " Ano: " + this.getAno() + 
                        " Semestre: " + this.getSemestre() +
                        " Codigo Aluno: " + this.getMatriculaVO().getAluno().getCodigo() + 
                        " Nome Aluno: " + this.getMatriculaVO().getAluno().getNome() +
                        " Codigo Curso: " + this.getMatriculaVO().getCurso().getCodigo() +
                        " Nome Curso: " + this.getMatriculaVO().getCurso().getNome();
	}

	public String getDigitoTurma() {
		if(digitoTurma == null) {
			digitoTurma = "";
		}
		return digitoTurma;
	}

	public void setDigitoTurma(String digitoTurma) {
		this.digitoTurma = digitoTurma;
	}

	
	
	private Double valorParcelaRateioDesconsiderado;
	private Double valorFinalSerRestituido;
	private Double valorDiferencaParcelasPagasParcelasGeradas;
	
	public void setValorParcelaRateioDesconsiderado(Double valorParcelaRateioDesconsiderado) {
		this.valorParcelaRateioDesconsiderado = valorParcelaRateioDesconsiderado;
		
	}

	public Double getValorParcelaRateioDesconsiderado() {
		if (valorParcelaRateioDesconsiderado == null) {
			valorParcelaRateioDesconsiderado = 0.0;
		}
		return valorParcelaRateioDesconsiderado;
	}
	
	public Double getValorDiferencaParcelasPagasParcelasGeradas() {
		if (valorDiferencaParcelasPagasParcelasGeradas == null) {
			valorDiferencaParcelasPagasParcelasGeradas = 0.0;
		}
		return valorDiferencaParcelasPagasParcelasGeradas;
	}

	public void setValorDiferencaParcelasPagasParcelasGeradas(Double valorDiferencaParcelasPagasParcelasGeradas) {
		this.valorDiferencaParcelasPagasParcelasGeradas = valorDiferencaParcelasPagasParcelasGeradas;
	}
	
	public void setValorFinalSerRestituido(Double valorFinalSerRestituido) {
		this.valorFinalSerRestituido = valorFinalSerRestituido;
	}
	
	public Double getValorFinalSerRestituido(){
		if(valorFinalSerRestituido == null) {
			valorFinalSerRestituido = getValorDiferencaParcelasPagasParcelasGeradas() + (getValorConvenioNaoRestituirAluno()  + getValorParcelaRateioDesconsiderado());

//			valorFinalSerRestituido = getValorDiferencaParcelasPagasParcelasGeradas();
//			if(valorFinalSerRestituido < 0 && matriculaPeriodoVO.getValorConvenioNaoRestituirAluno() > 0){
//				if(matriculaPeriodoVO.getValorConvenioNaoRestituirAluno() > (valorFinalSerRestituido*-1)){
//					valorFinalSerRestituido = 0.0;
//				}else{
//					valorFinalSerRestituido += matriculaPeriodoVO.getValorConvenioNaoRestituirAluno();
//				}
//			}
//			
//			if(!matriculaPeriodoVO.getValorParcelaRateioDesconsiderado().equals(0.0)){
//				
//					valorFinalSerRestituido += matriculaPeriodoVO.getValorParcelaRateioDesconsiderado();
//				
//			}
			
//			
//			
//		valorFinalSerRestituido = getValorDiferencaParcelasPagasParcelasGeradas();
//		if(valorFinalSerRestituido < 0 && getMatriculaPeriodoVO().getValorConvenioNaoRestituirAluno() > 0 
//				&& getMatriculaPeriodoVO().getValorConvenioNaoRestituirAluno() < (valorFinalSerRestituido*-1)){
//			valorFinalSerRestituido =  Uteis.arrendondarForcando2CadasDecimais(valorFinalSerRestituido + getMatriculaPeriodoVO().getValorConvenioNaoRestituirAluno());
//		}
//		if(valorFinalSerRestituido < 0 && getMatriculaPeriodoVO().getValorParcelaRateioDesconsiderado() > 0){
//			valorFinalSerRestituido =  Uteis.arrendondarForcando2CadasDecimais(valorFinalSerRestituido + getMatriculaPeriodoVO().getValorParcelaRateioDesconsiderado());
//		}
		}
		return valorFinalSerRestituido;
	}
	
	

	public void setAluno(String aluno) {
		this.aluno = aluno;
	}

	public Boolean getLiberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular() {
		if (liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular == null) {
			liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular = false;
		}
		return liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular;
	}

	public void setLiberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular(
			Boolean liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular) {
		this.liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular = liberadoExclusaoDisciplinaPeriodoLetivoAtualAlunoIrregular;
	}

	public List<DocumentoAssinadoVO> getListaDocumentoAssinadoVO() {
		if (listaDocumentoAssinadoVO == null) {
			listaDocumentoAssinadoVO = new ArrayList<DocumentoAssinadoVO>();
		}
		return listaDocumentoAssinadoVO;
	}

	public void setListaDocumentoAssinadoVO(List<DocumentoAssinadoVO> listaDocumentoAssinadoVO) {
		this.listaDocumentoAssinadoVO = listaDocumentoAssinadoVO;
	}

	public Boolean getRegerarDocumentoAssinado() {
		if (regerarDocumentoAssinado == null) {
			regerarDocumentoAssinado = false;
		}
		return regerarDocumentoAssinado;
	}

	public void setRegerarDocumentoAssinado(Boolean regerarDocumentoAssinado) {
		this.regerarDocumentoAssinado = regerarDocumentoAssinado;
	}
	
	private Boolean maximizarLista;
	private Boolean minimizarLista;
	 	
	public Boolean getMaximizarLista() {
		if (maximizarLista == null) {
			maximizarLista = false;
		}
		return maximizarLista;
	}
	
	public void setMaximizarLista(Boolean maximizarLista) {
		this.maximizarLista = maximizarLista;
	}
	
	public Boolean getMinimizarLista() {
		if (minimizarLista == null) {
			minimizarLista = false;
		}
		return minimizarLista;
	}
	
	public void setMinimizarLista(Boolean minimizarLista) {
		this.minimizarLista = minimizarLista;
	}
	
  public Boolean verificarDisciplinaEstaRegistradaMatriculaPeriodoParaSerCursadaComoEquivalencia(DisciplinaVO disciplina,MatriculaVO matriculaVO) throws Exception {
        for (MatriculaPeriodoTurmaDisciplinaVO obj : this.getMatriculaPeriodoTumaDisciplinaVOs()) {
            if (obj.getDisciplinaEquivale() &&  obj.getMapaEquivalenciaDisciplinaVOIncluir().getMapaEquivalenciaDisciplinaMatrizCurricularVOs().stream().anyMatch( p-> p.getDisciplinaVO().getCodigo().equals(disciplina.getCodigo()))) {
                  return true;
            }
        }
        if(matriculaVO.getMatriculaOnlineProcSeletivo() && Uteis.isAtributoPreenchido(matriculaVO.getListaProcessoSeletivoDisciplinasAproveitadas())){
           for(HistoricoVO hist :   matriculaVO.getListaProcessoSeletivoDisciplinasAproveitadas()){
              if(hist.getHistoricoEquivalente() &&  hist.getMapaEquivalenciaDisciplina().getMapaEquivalenciaDisciplinaMatrizCurricularVOs().stream().anyMatch( p-> p.getDisciplinaVO().getCodigo().equals(disciplina.getCodigo()))){
               return true;
              }
           }
        }
        return false;
    }
  
  
  public Boolean verificarAlunoEstaAprovadoDisciplinaNaGradeAntigaReferenteAtransferenciaInternaMatricula(DisciplinaVO disciplina,MatriculaVO matriculaVO) throws Exception {
           
      HistoricoVO hist = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().verificarAlunoAprovadoDisciplina(disciplina.getCodigo());
      if (hist != null && !this.getVerificarHistoricoDesistiuEquivalencia(hist)) {
        return true ; 
		}
      return false;
  }
  
  public Long getQuantidadeDisciplinaMatriculaPeriodo() {
		return getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaComposta()).count();
	}
  
  public Long getQuantidadeDisciplinaAdaptacaoMatriculaPeriodo() {
	  return getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaComposta() && d.getDisciplinaAdaptacao()).count();
  }
  
  public Integer getChTotalDisciplinaAdaptacaoMatriculaPeriodo() {
	  return getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaComposta() && d.getDisciplinaAdaptacao()).mapToInt(c -> c.getCargaHoraria()).sum();
  }
  
  public Long getQuantidadeDisciplinaOptativaMatriculaPeriodo() {
	  return getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaComposta() && d.getDisciplinaReferenteAUmGrupoOptativa()).count();
  }
  
  public Integer getChTotalDisciplinaOptativaMatriculaPeriodo() {
	  return getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaComposta() && d.getDisciplinaReferenteAUmGrupoOptativa()).mapToInt(c -> c.getCargaHoraria()).sum();
  }
  
  public Long getQuantidadeDisciplinaRegularMatriculaPeriodo() {
	  return getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaComposta() && !d.getDisciplinaReferenteAUmGrupoOptativa() && !d.getDisciplinaAdaptacao() && !d.getDisciplinaDependencia() && !d.getDisciplinaForaGrade()).count();
  }
  
  public Integer getChTotalDisciplinaRegularMatriculaPeriodo() {
	  return getMatriculaPeriodoTumaDisciplinaVOs().stream().filter(d -> !d.getDisciplinaComposta() && !d.getDisciplinaReferenteAUmGrupoOptativa() && !d.getDisciplinaAdaptacao() && !d.getDisciplinaDependencia()  && !d.getDisciplinaForaGrade()).mapToInt(c -> c.getCargaHoraria()).sum();
  }

  public SituacaoMatriculaPeriodoEnum getSituacaoMatriculaPeriodoEnum() {
	  if (Uteis.isAtributoPreenchido(getSituacaoMatriculaPeriodo())) {
		  return SituacaoMatriculaPeriodoEnum.getEnumPorValor(getSituacaoMatriculaPeriodo());
	  }
	  return null;
  }
}