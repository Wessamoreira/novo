package negocio.comuns.academico;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Reponsável por manter os dados da entidade MatriculaPeriodoTurmaDisciplina.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "matriculaPeriodoTurmaDisciplina")
public class MatriculaPeriodoTurmaDisciplinaVO extends SuperVO {

    private Integer codigo;
    private String matricula;
    private Integer matriculaPeriodo;
    private DisciplinaVO disciplina;
    private Boolean selecionarAlunoConteudoDiferente;
    
    /**
     * ATRIBUTO TRANSIENT - utilizado na tela de matricula/renovacao
     * para apresentar e controlar o numero de alunos matriculados
     * em uma determinada turmaDisciplina. Evitando que numero de vadas
     * em uma turmaDisciplina - em um determina ano/semestre seja ultrassado.
     */
    private Integer nrAlunosMatriculados;
    private Integer nrAlunosMatriculadosTurmaPratica;
    private Integer nrAlunosMatriculadosTurmaTeorica;
    /**
     * ATRIBUTO TRANSIENT - utilizado na tela de matricula/renovacao
     * para apresentar e controlar o numero de alunos matriculados
     * em uma determinada turmaDisciplina. Evitando que numero de vadas
     * em uma turmaDisciplina - em um determina ano/semestre seja ultrassado.
     */
    private Integer nrVagasDisponiveis;
    private Integer nrVagasDisponiveisTurmaPratica;
    private Integer nrVagasDisponiveisTurmaTeorica;
    /**
     * ATRIBUTO TRANSIENT - utilizado na tela de matricula/renovacao
     * para apresentar e controlar o numero de alunos matriculados
     * em uma determinada turmaDisciplina. Evitando que numero de vadas
     * em uma turmaDisciplina - em um determina ano/semestre seja ultrassado.
     */
    private Boolean liberadaSemDisponibilidadeVagas;
    private UsuarioVO usuarioLiberadaSemDisponibilidadeVagas;
    private Date dataLiberadaSemDisponibilidadeVagas;

    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turma </code>.
     */
    private TurmaVO turma;
    private String semestre;
    private Integer bimestre;
    private String ano;
    
    /**
     * Quando o aluno estiver estudando uma disciplina equivalente este deve ser marcado como true para determinar a disciplina da matriz curricular, 
     *  ou seja o sistema deverá apresentar este no histórico somente quando as suas equivalencia forem cursadas.
     */
    private Boolean disciplinaPorEquivalencia;
    
    /**
     * Este deve ser marcado como true quando esta disciplina  for a equivalente da grade do aluno
     * Este deve ser apresentado no boletim acadêmico. Quando esta for true, significa que haverá
     * outras disciplinas da matriz, que estão com disciplinaPorEquivalencia 
     */
    private Boolean disciplinaEquivale;
    
    /**
     * @deprecated atributo nao utilizado mais, pois a equivalencia é gerenciada
     * por meio de uma mapa de equivalencia. Logo, a informacao da equivalencia
     * deve ser obtida por meio do mapa.
     */
    private DisciplinaVO disciplinaEquivalente;
    
    private ModalidadeDisciplinaEnum modalidadeDisciplina;
    private Boolean permiteEscolherModalidade;
    private ConteudoVO conteudo;
    /**
     * Atributo que indica se a disciplina em questão trata-se de uma inclusao.
     * Isto é determinante, na medida que o valor cobrado por disciplina
     * incluída, pode ser diferente do valor cobrado peloas disciplinas já
     * definidas na grade para que o aluno possa estudar.
     */
    private Boolean disciplinaIncluida;
    
    /**
     * TRANSIENT - atributo definido em tempo de execucação, com base nos históricos
     * registrados para uma determinada disciplina. Pois, caso o aluno já tenho sido
     * reprovado na disciplina, a instituicao poderá definir um preço pela disciplina
     * diferenciado quando o aluno for renovar.
     */
    private Boolean disciplinaDependencia;
    
    /**
     * Caso esteja TRUE significa que o usuário está cursando esta disciplina, em uma grade
     * anterior (da grade atual do aluno após migração) para uma disciplina que existe de forma identifca
     * na grade destino. Mas como o aluno estava cursando a mesma, então não tinhamos como
     * migrar o MatriculaPeriodoTurmaDisciplina/Histórico fazendo o aproveitamento academico do mesmo. Assim, 
     * quando este boolean TRUE, teremos que esta MatriculaPeriodoTurmaDisciplina apesar de estar na
     * referenciado na Transferencia de Matriz o aluno continua estudando na gradeDisciplina
     */
    private Boolean disciplinaCursandoPorCorrespondenciaAposTransferencia;    
    
    private Boolean disciplinaAprovada;
    
    private Boolean disciplinasPreRequisito;
    
    private List<DisciplinaVO> listaDisciplinasPreRequisitos;
    
    private String justificativa;
    private String observacaoJustificativa;
    //Campo Nao persistido no banco usado somente para relatorio
    private MatriculaVO matriculaObjetoVO;
    private MatriculaPeriodoVO matriculaPeriodoObjetoVO;
	private String jaPossuiAvaliacaoOnline;
    private String jaAcessouConteudo ;
    private Integer conteudoSugerido ;
    private String logAlteracao;  
    
    /**
     * Estará TRUE se trata-se de uma MatriculaPeriodoTurmaDisciplina que faz
     * parte de uma composicao. Ou seja, que foi incluída para o aluno estudar
     * pois o mesmo foi matricula em uma disciplina com composição. Neste caso
     * o atributo GradeDiscipliaCompostaVO iré indicar a disciplina da composicao
     * que originou este registro.
     */
    private Boolean disciplinaFazParteComposicao;
    
    private GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO;
    
    private Boolean reposicao;
    
    private Boolean inclusaoForaPrazo;
    
    private Boolean notificacaoDownloadMaterialEnviado;
    
    private Boolean realizouTransferenciaTurno;
    
    private Date dataNotificacaoFrequenciaBaixa;
    private Boolean isentarMediaFinal;
    
    /**
     * Utilizado no controle do número de disciplinas de dependência que podem ser incluídas com choque de horário (REGIME ESPECIAL).
     * Este controle existe para permitir que um aluno possa cursar algumas disciplina de dependência em regime especial(quando ocorre choque 
     * de horário da mesma no período letivo em que esta renovando), possibilitando que o aluno ainda conclua seus estudos dentro do prazo 
     * regular do curso. 
     * TODOURGENTE
     */
    private Boolean disciplinaEmRegimeEspecial;
    
    // atributo transiente utilizado para controlar se a disciplina será 
    // feita em registro especial ou não
    private String descricaoChoqueHorarioDisciplina;
    private String descricaoPreRequisitoDisciplina;    
    /**
     * Indica se a disciplina é optativa na matriz curricular do aluno
     * ou não. Isto não interessando se a disciplina veio de um grupo
     * de optativa ou não.
     */
    private Boolean disciplinaOptativa;
    /**
     * Indicar se a disciplina é optativa é ainda refere-se a uma optativa
     * de um grupo de optativas. Isto é importante, pois neste caso a mesma
     * não veio da entidade GradeDisciplina, mas sim de GradeCurricularGrupoOptativaDisciplina
     */
    private Boolean disciplinaReferenteAUmGrupoOptativa;
    private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO; 

    /**
     * Caso seja uma disciplina regular teremos aqui a gradeDisciplina que originou
     * esta MatriculaPeriodoTurmaDisciplina
     */
    private GradeDisciplinaVO gradeDisciplinaVO;
    
    /**
     * Caso esta seja uma disciplina que está sendo estudada pelo aluno com intuito de 
     * fechar (ser aprovados em todas as disciplinas que o mapa determina que ele tem que estudar)
     * um MapaEquivalencia, então o mapa será gravado neste atributo para controle do SEI.
     */
    private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVOIncluir;
    /**
     * Caso esta seja uma disciplina que está sendo estudada pelo aluno com intuito de 
     * fechar (ser aprovados em todas as disciplinas que o mapa determina que ele tem que estudar)
     * um MapaEquivalencia, então neste atributo será registrar uma (e somente uma) das disciplinas
     * que o aluno terá que estudar. Haja vista, que cada disciplina que o aluno precisa estudar
     * deve existir um registro em MatriculaPeriodoTurmaDisciplina.
     */
    private MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada;
    
    /**
     * ATRIBUTO TRANSIENT que será utilizado quando o usuário estiver incluindo uma
     * MatriculaPeriodoTurmaDisciplina destinada para uma disciplina de um mapa
     * de equivalencia específica a ser cursada pelo aluno.
     */
    private Boolean bloquarAlteracaoMapaEquivalenciaDisciplinaCursada;
    
    /**
     * ATRIBUTO TRANSIENT que será utilizado quando o usuário estiver incluindo uma
     * MatriculaPeriodoTurmaDisciplina. Quando ocorrer um choque de horário o sistema
     * irá verificar se o curso permite Disciplinas em Regime Especial (choque de horário)
     * e aí habilitar esta possibilidade para o aluno/funcionario da IE
     */
    private Boolean permitirUsuarioTentarInclusaoComChoqueHorario;
    
    /**
     * Objeto TRANSIENT na tabela matriculaPeriodoTurmaDisciplina. Quando consultamos
     * uma MatriculaPeriodoTurmaDisciplina este é montado com a configuracao definida no historico
     * do aluno. Quando estamos incluindo uma nova matriculaPeriodoTurmaDisciplina o SEI registra
     * neste atributo a configuracao a ser adotada para o historico.
     */
    private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
    private Boolean disciplinaForaGrade;
    
    /**
     * Atributo preenchido como matriculaPeriodoTurmaDisciplina for alterada - impactada 
     * com uma transferencia de matriz curricular, realizada ainda quando o aluno estava 
     * estudando uma determinada disciplina.
     */
    private TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatricula;
	
    /**
     * Atributo TRANSIENTE responsável por manter um historico anterior a uma transferencia,
     * onde esta MatriculaPeriodoTurmaDisciplina ficou sendo cursada por correspodencia.
     * Assim, tesmo que manter este historico antes da remocao definitiva da correspondencia, 
     * para que as notas e fequencia do aluno sejam posteriormente movidas para o historico
     * definitivo que será criado após a remocao da cursandoPorCorresponcia.
     */
    private HistoricoVO historicoAnteriorDisciplinaMigrarNotas;
    /**
     * TRANSIENT
     * Utilizado somente quando a MatriculaPeriodoTurmaDisciplina estiver sendo migrada
     * para uma turma da nova matriz. Assim, esta validacao de disciplina ja estar em curso
     * nao poderá ser realizada, pois impediria que uma nova MatriculaPeriodoTurmaDisciplina
     * seja adicionada para substituir a anterior.
     */
    private Boolean ignorarValidacaoDisciplinaSendoCursadaAluno;

	/**
	 * Transient Distribuicao Sub Turma
	 */
	private Boolean existeRegistroAula;
	private Boolean realizarAbonoRegistroAula;
	private Integer qtdeHorasRegistrarAbono;
	/**
	 * Transient Inclusão Disciplina
	 */
	private UsuarioVO usuarioLiberacaoChoqueHorario;
	private UsuarioVO usuarioLiberacaoPreRequisito;	
	/**
	 * Fim Transient Distribuicao Sub Turma
	 */
            
    public static final long serialVersionUID = 1L;
    private TurmaVO turmaTeorica;
    private TurmaVO turmaPratica;


    /**
     * Atributo transiente usado na sugestao de turma pratica e teorica
     */
    private Boolean sugestaoTurmaPraticaTeoricaRealizada;
    
    /**
     * Construtor padrão da classe <code>MatriculaPeriodoTurmaDisciplina</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente
     * seus atributos (Classe VO).
     */
    
    /**
     * Atributo transiente usado na alteração da data de registro do histórico na inclusão/exclusão de disciplia e no lançamento de nota
     */
    private Date dataRegistroHistorico;
    private boolean alterandoSubturmaPraticaTeorica = false;
    /**
     * Atributo TRANSIENTE responsável por manter um historico anterior a uma transferencia,
     * onde esta MatriculaPeriodoTurmaDisciplina ficou sendo cursada por Equivalencia.
     * Assim, tesmo que manter este historico antes da remocao definitiva da Equivalencia, 
     * para que as notas e fequencia do aluno sejam posteriormente movidas para o historico
     * definitivo que será criado após a remocao da cursandoPorEquivalencia.
     */
    private HistoricoVO historicoAnteriorDisciplinaEquivalenteMigrarNotas;
    private Boolean disciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia;
    private Integer numeroAgrupamentoEquivalenciaDisciplina;
    
    /*REST WS Moodle */
    private Timestamp dataultimaalteracao;
    private Boolean liberarInclusaoDisciplinaPreRequisito;
    private Boolean liberarInclusaoDisciplinaChoqueHorario;    
    /**
     * Atributo TRANSIENTE
     */
	private DefinicoesTutoriaOnlineEnum definicoesTutoriaOnline;
	private ClassroomGoogleVO classroomGoogleVO;
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	private SalaAulaBlackboardVO salaAulaBlackboardGrupoVO;
	private String periodoLetivoApresentar;
	
	private PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO;	
	private Boolean apresentarRemoverVisaoAluno;
	
	/*
	 * ATRIBUTO TRANSIENTE
	 * dataInicioAula
	 * dataFimAula
	 * 
	 */
	private Date dataInicioAula;
	private Date dataFimAula;
	
    public MatriculaPeriodoTurmaDisciplinaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>MatriculaPeriodoTurmaDisciplinaVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(MatriculaPeriodoTurmaDisciplinaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getTurma() == null) || (obj.getTurma().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURMA (Inclusão de Disciplinas Histórico Fora do Prazo) deve ser informado.");
        }
        if (obj.getTurma().getAnual()) {
            if (obj.getAno().isEmpty()) {
                throw new ConsistirException("O campo ANO (Inclusão de Disciplinas Histórico Fora do Prazo) deve ser informado.");
            }
        }
        if (obj.getTurma().getSemestral()) {
            if (obj.getSemestre().isEmpty()) {
                throw new ConsistirException("O campo SEMESTRE (Inclusão de Disciplinas Histórico Fora do Prazo) deve ser informado.");
            }
            if (obj.getAno().isEmpty()) {
                throw new ConsistirException("O campo ANO (Inclusão de Disciplinas Histórico Fora do Prazo) deve ser informado.");
            }
        }
        if (obj.getDisciplina() == null || obj.getDisciplina().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo DISCIPLINA (Inclusão de Disciplinas Histórico Fora do Prazo) deve ser informado.");
        }
        if(obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL) && obj.getConteudo().getCodigo() > 0){
            obj.setConteudo(new ConteudoVO());
        }
    }

    public static void validarDadosTransferenciaGradeCurricular(MatriculaPeriodoTurmaDisciplinaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getTurma() == null) || (obj.getTurma().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURMA (Transferência Matriz Curricular) deve ser informado.");
        }
        if (obj.getTurma().getAnual()) {
            if (obj.getAno().isEmpty()) {
                throw new ConsistirException("O campo ANO (Transferência Matriz Curricular) deve ser informado.");
            }
        }
        if (obj.getTurma().getSemestral()) {
            if (obj.getSemestre().isEmpty()) {
                throw new ConsistirException("O campo SEMESTRE (Transferência Matriz Curricular) deve ser informado.");
            }
            if (obj.getAno().isEmpty()) {
                throw new ConsistirException("O campo ANO (Transferência Matriz Curricular) deve ser informado.");
            }
        }
        if (obj.getDisciplina() == null || obj.getDisciplina().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo DISCIPLINA (Transferência Matriz Curricular) deve ser informado.");
        }
        if(obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL) && obj.getConteudo().getCodigo() > 0){
            obj.setConteudo(new ConteudoVO());
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setMatriculaPeriodo(0);
        setSemestre("");
        setAno("");
        setMatricula("");
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    /**
     * Retorna o objeto da classe <code>Turma</code> relacionado com (
     * <code>MatriculaPeriodoTurmaDisciplina</code>).
     */
    
    @XmlElement(name = "turma")
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return (turma);
    }

    /**
     * Define o objeto da classe <code>Turma</code> relacionado com (
     * <code>MatriculaPeriodoTurmaDisciplina</code>).
     */
    public void setTurma(TurmaVO obj) {
        this.turma = obj;
    }

    @XmlElement(name = "disciplina")
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getMatriculaPeriodo() {
        return (matriculaPeriodo);
    }

    public void setMatriculaPeriodo(Integer matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the disciplinaEquivalente
     *
     */
    public Boolean getDisciplinaEquivale() {
        if (disciplinaEquivale == null) {
            disciplinaEquivale = false;
        }
        return disciplinaEquivale;
    }

    /**
     * @param disciplinaEquivalente
     *            the disciplinaEquivalente to set
     */
    public void setDisciplinaEquivale(Boolean disciplinaEquivale) {
        this.disciplinaEquivale = disciplinaEquivale;
    }

    /**
     * @return the disciplinaEquivalente
     * @deprecated nao mais utilizado 
     */
    public DisciplinaVO getDisciplinaEquivalente() {
        if (disciplinaEquivalente == null) {
            disciplinaEquivalente = new DisciplinaVO();
        }
        return disciplinaEquivalente;
    }

    /**
     * @param disciplinaEquivalente
     *            the disciplinaEquivalente to set
     */
    public void setDisciplinaEquivalente(DisciplinaVO disciplinaEquivalente) {
        this.disciplinaEquivalente = disciplinaEquivalente;
    }

    /**
     * @return the disciplinaIncluida
     */
    public Boolean getDisciplinaIncluida() {
        if (disciplinaIncluida == null) {
            disciplinaIncluida = Boolean.FALSE;
        }
        return disciplinaIncluida;
    }

    public String getDisciplinaIncluida_Apresentar() {
        if (getDisciplinaIncluida() && !getInclusaoForaPrazo()) {
            return "Sim";
        } else if (getDisciplinaIncluida() && getInclusaoForaPrazo()) {
            return "Sim - FP";
        }else{
            return "Não";
        }
    }

    /**
     * @param disciplinaIncluida
     *            the disciplinaIncluida to set
     */
    public void setDisciplinaIncluida(Boolean disciplinaIncluida) {
        this.disciplinaIncluida = disciplinaIncluida;
    }

    /**
     * @return the disciplinaAprovada
     */
    public Boolean getDisciplinaAprovada() {
        if (disciplinaAprovada == null) {
            disciplinaAprovada = false;
        }
        return disciplinaAprovada;
    }

    /**
     * @param disciplinaAprovada the disciplinaAprovada to set
     */
    public void setDisciplinaAprovada(Boolean disciplinaAprovada) {
        this.disciplinaAprovada = disciplinaAprovada;
    }

    /**
     * @return the disciplinasPreRequisito
     */
    public Boolean getDisciplinasPreRequisito() {
        if (disciplinasPreRequisito == null) {
            disciplinasPreRequisito = false;
        }
        return disciplinasPreRequisito;
    }

    /**
     * @param disciplinasPreRequisito the disciplinasPreRequisito to set
     */
    public void setDisciplinasPreRequisito(Boolean disciplinasPreRequisito) {
        this.disciplinasPreRequisito = disciplinasPreRequisito;
    }

    /**
     * @return the listaDisciplinasPreRequisitos
     */
    public List<DisciplinaVO> getListaDisciplinasPreRequisitos() {
        if (listaDisciplinasPreRequisitos == null) {
            listaDisciplinasPreRequisitos = new ArrayList<DisciplinaVO>(0);
        }
        return listaDisciplinasPreRequisitos;
    }

    /**
     * @param listaDisciplinasPreRequisitos the listaDisciplinasPreRequisitos to set
     */
    public void setListaDisciplinasPreRequisitos(List<DisciplinaVO> listaDisciplinasPreRequisitos) {
        this.listaDisciplinasPreRequisitos = listaDisciplinasPreRequisitos;
    }

    @Override
    public String toString() {
        return "MatriculaPeriodoTurmaDisciplina [Código: "
                + this.getCodigo()
                + " Semestre/Ano: " + this.getSemestre() + "/" + this.getAno()
                + this.getDisciplina().toString()
                + this.getTurma().toString()
                + " MatrículaPeriodo: " + this.getMatriculaPeriodo()
                + " Discplina Incluída: " + this.getDisciplinaIncluida_Apresentar();

    }

    public String getObservacaoJustificativa() {
        if (observacaoJustificativa == null) {
            observacaoJustificativa = "";
        }
        return observacaoJustificativa;
    }

    public void setObservacaoJustificativa(String observacaoJustificativa) {
        this.observacaoJustificativa = observacaoJustificativa;
    }

    public String getJustificativa() {
        if (justificativa == null) {
            justificativa = "";
        }
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public MatriculaVO getMatriculaObjetoVO() {
        if (matriculaObjetoVO == null) {
            matriculaObjetoVO = new MatriculaVO();
        }
        return matriculaObjetoVO;
    }

    public void setMatriculaObjetoVO(MatriculaVO matriculaObjetoVO) {
        this.matriculaObjetoVO = matriculaObjetoVO;
    }

    public Boolean getReposicao() {
        if (reposicao == null) {
            reposicao = Boolean.FALSE;
        }
        return reposicao;
    }

    public void setReposicao(Boolean reposicao) {
        this.reposicao = reposicao;
    }

    public Boolean getInclusaoForaPrazo() {
        if (inclusaoForaPrazo == null) {
            inclusaoForaPrazo = false;
        }
        return inclusaoForaPrazo;
    }

    public void setInclusaoForaPrazo(Boolean inclusaoForaPrazo) {
        this.inclusaoForaPrazo = inclusaoForaPrazo;
    }

	public Boolean getNotificacaoDownloadMaterialEnviado() {
		if (notificacaoDownloadMaterialEnviado == null) {
			notificacaoDownloadMaterialEnviado = Boolean.FALSE;
		}
		return notificacaoDownloadMaterialEnviado;
	}

	public void setNotificacaoDownloadMaterialEnviado(
			Boolean notificacaoDownloadMaterialEnviado) {
		this.notificacaoDownloadMaterialEnviado = notificacaoDownloadMaterialEnviado;
	}

	public Boolean getRealizouTransferenciaTurno() {
		if (realizouTransferenciaTurno == null) {
			realizouTransferenciaTurno = Boolean.FALSE;
		}
		return realizouTransferenciaTurno;
	}

	public void setRealizouTransferenciaTurno(Boolean realizouTransferenciaTurno) {
		this.realizouTransferenciaTurno = realizouTransferenciaTurno;
	}

    
    public ModalidadeDisciplinaEnum getModalidadeDisciplina() {
        if(modalidadeDisciplina == null){
            modalidadeDisciplina = ModalidadeDisciplinaEnum.PRESENCIAL;
        }
        return modalidadeDisciplina;
    }

    
    public void setModalidadeDisciplina(ModalidadeDisciplinaEnum modalidadeDisciplina) {
        this.modalidadeDisciplina = modalidadeDisciplina;
    }

    
    public ConteudoVO getConteudo() {
        if(conteudo == null){
            conteudo = new ConteudoVO();
        }
        return conteudo;
    }

    
    public void setConteudo(ConteudoVO conteudo) {
        this.conteudo = conteudo;
    }
    
    public boolean isExisteConteudo() {
    	return Uteis.isAtributoPreenchido(getConteudo());
    }

    
    public Boolean getPermiteEscolherModalidade() {
        if(permiteEscolherModalidade == null){
            permiteEscolherModalidade = false;
        }
        return permiteEscolherModalidade;
    }

    
    public void setPermiteEscolherModalidade(Boolean permiteEscolherModalidade) {
        this.permiteEscolherModalidade = permiteEscolherModalidade;
    }

    
    public Date getDataNotificacaoFrequenciaBaixa() {
        return dataNotificacaoFrequenciaBaixa;
    }

    
    public void setDataNotificacaoFrequenciaBaixa(Date dataNotificacaoFrequenciaBaixa) {
        this.dataNotificacaoFrequenciaBaixa = dataNotificacaoFrequenciaBaixa;
    }

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if(gradeDisciplinaVO == null){
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public Boolean getIsentarMediaFinal() {
		if (isentarMediaFinal == null) {
			isentarMediaFinal = false;
		}
		return isentarMediaFinal;
	}

	public void setIsentarMediaFinal(Boolean isentarMediaFinal) {
		this.isentarMediaFinal = isentarMediaFinal;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoObjetoVO() {
		if (matriculaPeriodoObjetoVO == null) {
			matriculaPeriodoObjetoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoObjetoVO;
	}

	public void setMatriculaPeriodoObjetoVO(MatriculaPeriodoVO matriculaPeriodoObjetoVO) {
		this.matriculaPeriodoObjetoVO = matriculaPeriodoObjetoVO;
	}

    /**
     * @return the disciplinaOptativa
     */
    public Boolean getDisciplinaOptativa() {
        if (disciplinaOptativa == null) {
            disciplinaOptativa = Boolean.FALSE;
        }
        return disciplinaOptativa;
    }

    /**
     * @param disciplinaOptativa the disciplinaOptativa to set
     */
    public void setDisciplinaOptativa(Boolean disciplinaOptativa) {
        this.disciplinaOptativa = disciplinaOptativa;
    }

    /**
     * @return the disciplinaReferenteAUmGrupoOptativa
     */
    public Boolean getDisciplinaReferenteAUmGrupoOptativa() {
        if (disciplinaReferenteAUmGrupoOptativa == null) {
            disciplinaReferenteAUmGrupoOptativa = Boolean.FALSE;
        }
        return disciplinaReferenteAUmGrupoOptativa;
    }

    /**
     * @param disciplinaReferenteAUmGrupoOptativa the disciplinaReferenteAUmGrupoOptativa to set
     */
    public void setDisciplinaReferenteAUmGrupoOptativa(Boolean disciplinaReferenteAUmGrupoOptativa) {
        this.disciplinaReferenteAUmGrupoOptativa = disciplinaReferenteAUmGrupoOptativa;
    }

    /**
     * @return the gradeCurricularGrupoOptativaDisciplinaVO
     */
    public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
        if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
            gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
        }
        return gradeCurricularGrupoOptativaDisciplinaVO;
    }

    /**
     * @param gradeCurricularGrupoOptativaDisciplinaVO the gradeCurricularGrupoOptativaDisciplinaVO to set
     */
    public void setGradeCurricularGrupoOptativaDisciplinaVO(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
        this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
    }

    /**
     * @return the disciplinaEmRegimeEspecial
     */
    public Boolean getDisciplinaEmRegimeEspecial() {
        if (disciplinaEmRegimeEspecial == null) {
            disciplinaEmRegimeEspecial = Boolean.FALSE;
        }
        return disciplinaEmRegimeEspecial;
    }

    /**
     * @param disciplinaEmRegimeEspecial the disciplinaEmRegimeEspecial to set
     */
    public void setDisciplinaEmRegimeEspecial(Boolean disciplinaEmRegimeEspecial) {
        this.disciplinaEmRegimeEspecial = disciplinaEmRegimeEspecial;
    }

    /**
     * @return the mapaEquivalenciaDisciplinaVOIncluir
     */
    public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplinaVOIncluir() {
        if (mapaEquivalenciaDisciplinaVOIncluir == null) {
            mapaEquivalenciaDisciplinaVOIncluir = new MapaEquivalenciaDisciplinaVO();
        }
        return mapaEquivalenciaDisciplinaVOIncluir;
    }

    /**
     * @param mapaEquivalenciaDisciplinaVOIncluir the mapaEquivalenciaDisciplinaVOIncluir to set
     */
    public void setMapaEquivalenciaDisciplinaVOIncluir(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVOIncluir) {
        this.mapaEquivalenciaDisciplinaVOIncluir = mapaEquivalenciaDisciplinaVOIncluir;
    }
    
    public Boolean getDisciplinaComChoqueHorario() {
        if (!getDescricaoChoqueHorarioDisciplina().equals("")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @return the descricaoChoqueHorarioDisciplina
     */
    public String getDescricaoChoqueHorarioDisciplina() {
        if (descricaoChoqueHorarioDisciplina == null) {
            descricaoChoqueHorarioDisciplina = "";
        }
        return descricaoChoqueHorarioDisciplina;
    }

    /**
     * @param descricaoChoqueHorarioDisciplina the descricaoChoqueHorarioDisciplina to set
     */
    public void setDescricaoChoqueHorarioDisciplina(String descricaoChoqueHorarioDisciplina) {
        this.descricaoChoqueHorarioDisciplina = descricaoChoqueHorarioDisciplina;
    }

    /**
     * @return the displinaPorEquivalencia
     */
    public Boolean getDisciplinaPorEquivalencia() {
        if (disciplinaPorEquivalencia == null) {
            disciplinaPorEquivalencia = Boolean.FALSE;
        }
        return disciplinaPorEquivalencia;
    }

    /**
     * @param displinaPorEquivalencia the displinaPorEquivalencia to set
     */
    public void setDisciplinaPorEquivalencia(Boolean disciplinaPorEquivalencia) {
        this.disciplinaPorEquivalencia = disciplinaPorEquivalencia;
    }

    /**
     * @return the mapaEquivalenciaDisciplinaCursada
     */
    public MapaEquivalenciaDisciplinaCursadaVO getMapaEquivalenciaDisciplinaCursada() {
        if (mapaEquivalenciaDisciplinaCursada == null) {
            mapaEquivalenciaDisciplinaCursada = new MapaEquivalenciaDisciplinaCursadaVO();
        }
        return mapaEquivalenciaDisciplinaCursada;
    }

    /**
     * @param mapaEquivalenciaDisciplinaCursada the mapaEquivalenciaDisciplinaCursada to set
     */
    public void setMapaEquivalenciaDisciplinaCursada(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada) {
        this.mapaEquivalenciaDisciplinaCursada = mapaEquivalenciaDisciplinaCursada;
    }
    
    public Integer getCreditosDisciplina() {
        if (this.getDisciplinaPorEquivalencia()) { 
            return this.getMapaEquivalenciaDisciplinaCursada().getNumeroCreditos();
        }
        if (!this.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
            return this.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos();
        } else {
            return this.getGradeDisciplinaVO().getNrCreditos();
        }
    }
    
    public Double getCreditosFinanceirosDisciplina() {
        if (!this.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
            return this.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditoFinanceiro();
        } else {
            return this.getGradeDisciplinaVO().getNrCreditoFinanceiro();
        }
    }

    /**
     * Retorna a cargaHoraria da disciplina para esta MatriculaPeriodoTurmaDisciplina.
     * Esta carga horaria pode vir de três locais distintos. GradeDisciplinaVO para 
     * disciplinas regulares. GradeGrupoOptativaDisciplinaVO para disciplinas de um grupo
     * de optativa, ou para disciplinas equivalentes, vem de: MapaEquivalenciaDisciplinaCursada.
     * @return 
     */
    public Integer getCargaHorariaDisciplina() {
        if (this.getDisciplinaPorEquivalencia()) {
            return this.getMapaEquivalenciaDisciplinaCursada().getCargaHoraria();
        }
        if (!this.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
            return this.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
        } else {
            if (this.getDisciplinaFazParteComposicao()) {
                return this.getGradeDisciplinaCompostaVO().getCargaHoraria();
            }
            return this.getGradeDisciplinaVO().getCargaHoraria();
        }
    }

    /**
     * @return the configuracaoAcademicoVO
     */
    public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
        if (configuracaoAcademicoVO == null) {
            configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
        }
        return configuracaoAcademicoVO;
    }

    /**
     * @param configuracaoAcademicoVO the configuracaoAcademicoVO to set
     */
    public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
        this.configuracaoAcademicoVO = configuracaoAcademicoVO;
    }

    /**
     * @return the gradeDiscipliaCompostaVO
     */
    public GradeDisciplinaCompostaVO getGradeDisciplinaCompostaVO() {
        if (gradeDisciplinaCompostaVO == null) {
            gradeDisciplinaCompostaVO = new GradeDisciplinaCompostaVO();
        }
        return gradeDisciplinaCompostaVO;
    }

    /**
     * @param gradeDiscipliaCompostaVO the gradeDiscipliaCompostaVO to set
     */
    public void setGradeDisciplinaCompostaVO(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) {
        this.gradeDisciplinaCompostaVO = gradeDisciplinaCompostaVO;
    }

    /**
     * @return the bloquarAlteracaoMapaEquivalenciaDisciplinaCursada
     */
    public Boolean getBloquarAlteracaoMapaEquivalenciaDisciplinaCursada() {
        if (bloquarAlteracaoMapaEquivalenciaDisciplinaCursada == null) {
            bloquarAlteracaoMapaEquivalenciaDisciplinaCursada = Boolean.FALSE;
        }
        return bloquarAlteracaoMapaEquivalenciaDisciplinaCursada;
    }

    /**
     * @param bloquarAlteracaoMapaEquivalenciaDisciplinaCursada the bloquarAlteracaoMapaEquivalenciaDisciplinaCursada to set
     */
    public void setBloquarAlteracaoMapaEquivalenciaDisciplinaCursada(Boolean bloquarAlteracaoMapaEquivalenciaDisciplinaCursada) {
        this.bloquarAlteracaoMapaEquivalenciaDisciplinaCursada = bloquarAlteracaoMapaEquivalenciaDisciplinaCursada;
    }

    /**
     * @return the permitirUsuarioTentarInclusaoComChoqueHorario
     */
    public Boolean getPermitirUsuarioTentarInclusaoComChoqueHorario() {
        if (permitirUsuarioTentarInclusaoComChoqueHorario == null) {
            permitirUsuarioTentarInclusaoComChoqueHorario = Boolean.FALSE;
        }
        return permitirUsuarioTentarInclusaoComChoqueHorario;
    }

    /**
     * @param permitirUsuarioTentarInclusaoComChoqueHorario the permitirUsuarioTentarInclusaoComChoqueHorario to set
     */
    public void setPermitirUsuarioTentarInclusaoComChoqueHorario(Boolean permitirUsuarioTentarInclusaoComChoqueHorario) {
        this.permitirUsuarioTentarInclusaoComChoqueHorario = permitirUsuarioTentarInclusaoComChoqueHorario;
    }

    /**
     * @return the disciplinaDependencia
     */
    public Boolean getDisciplinaDependencia() {
        if (disciplinaDependencia == null) {
            disciplinaDependencia = Boolean.FALSE;
        }
        return disciplinaDependencia;
    }

    /**
     * @param disciplinaDependencia the disciplinaDependencia to set
     */
    public void setDisciplinaDependencia(Boolean disciplinaDependencia) {
        this.disciplinaDependencia = disciplinaDependencia;
    }

    /**
     * @return the disciplinaFazParteComposicao
     */
    public Boolean getDisciplinaFazParteComposicao() {
        if (disciplinaFazParteComposicao == null) {
            disciplinaFazParteComposicao = Boolean.FALSE;
        }
        return disciplinaFazParteComposicao;
    }

    /**
     * @param disciplinaFazParteComposicao the disciplinaFazParteComposicao to set
     */
    public void setDisciplinaFazParteComposicao(Boolean disciplinaFazParteComposicao) {
        this.disciplinaFazParteComposicao = disciplinaFazParteComposicao;
    }

    /**
     * Método atualizado na versão 5.0. Este método determina se a disciplina é composta
     * ou não, olhando para as possíveis formas de disciplinas que o aluno pode estudar.
     * Ou seja, para GradeDisciplinaVO, quando trata-se de uma disciplina regular da matriz.
     * Ou olha para GradeCurricularGrupoOptativaDisciplinaVO quando trata-se de uma disciplina
     * Quando trata-se de uma disciplina por equivalencia, temos que olhar para estas mesmas
     * duas variáveis. Pois ao estudar por equivalencia, o aluno fica condicionado a situacao
     * da disciplina na turma/curso da disciplina equivalente. Ou seja, se lá a disciplina é 
     * de um grupo de optativa, então esta informacao já será automaticamente deduzida abaixo.
     * @return 
     */
    public Boolean getDisciplinaComposta() {
        if (this.getDisciplinaReferenteAUmGrupoOptativa()) {
            return this.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinaComposta();
        } else {
            return this.getGradeDisciplinaVO().getDisciplinaComposta();
        }
    }
    
    public List<GradeDisciplinaCompostaVO> getGradeDisciplinaCompostaVOs() {
        if (this.getDisciplinaReferenteAUmGrupoOptativa()) {
            return this.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs();
        } else {
            return this.getGradeDisciplinaVO().getGradeDisciplinaCompostaVOs();
        }
    }

    /**
     * @return the nrAlunosMatriculados
     */
    public Integer getNrAlunosMatriculados() {
        if (nrAlunosMatriculados == null) {
            nrAlunosMatriculados = 0;
        }
        return nrAlunosMatriculados;
    }

    /**
     * @param nrAlunosMatriculados the nrAlunosMatriculados to set
     */
    public void setNrAlunosMatriculados(Integer nrAlunosMatriculados) {
        this.nrAlunosMatriculados = nrAlunosMatriculados;
    }

    /**
     * @return the nrVagasDisponiveis
     */
    public Integer getNrVagasDisponiveis() {
        if (nrVagasDisponiveis == null) {
            nrVagasDisponiveis = 0;
        }
        return nrVagasDisponiveis;
    }

    /**
     * @param nrVagasDisponiveis the nrVagasDisponiveis to set
     */
    public void setNrVagasDisponiveis(Integer nrVagasDisponiveis) {
        this.nrVagasDisponiveis = nrVagasDisponiveis;
    }

    /**
     * @return the liberadaSemDisponibilidadeVagas
     */
    public Boolean getLiberadaSemDisponibilidadeVagas() {
        if (liberadaSemDisponibilidadeVagas == null) {
            liberadaSemDisponibilidadeVagas = Boolean.FALSE;
        }
        return liberadaSemDisponibilidadeVagas;
    }

    /**
     * @param liberadaSemDisponibilidadeVagas the liberadaSemDisponibilidadeVagas to set
     */
    public void setLiberadaSemDisponibilidadeVagas(Boolean liberadaSemDisponibilidadeVagas) {
        this.liberadaSemDisponibilidadeVagas = liberadaSemDisponibilidadeVagas;
    }
    
    /**
     * Método responsável por determinar se o objeto desta classe 
     * está com restrição (bloqueio) para a matrícula correspondente do mesmo.
     * Este bloqueio deve existir quando:
     *    a) Se esta incluido uma nova MatriculaPeriodoTurmaDisciplina
     *          - Se está sendo editado a MatriculaPeriodoTurmaDisciplina entao é por que a 
     *            vaga correspondente já foi validada.
     *    b) Se ao incluir a mesma o campo nrVagasDisponivel está zerado (ou seja, nao há vagas)
     *    c) Se não existir vagas (item b acima) e nao existir uma liberacao de matricula
     *           mesmo com a indisponibilidade de vagas
     * @return 
     */
	public Boolean getPossuiRestricaoComRelacaoDisponibilidadeVaga() {
		if (!this.getCodigo().equals(0) && !isAlterandoSubturmaPraticaTeorica()) {
			// se está editando, então não deve haver restrição
			return Boolean.FALSE;
		}
//		if (this.getNrVagasDisponiveis() > 0) {
//			// se já vagas nao há restrição
//			return Boolean.FALSE;
//		} else {
//			if (this.getLiberadaSemDisponibilidadeVagas()) {
//				return Boolean.FALSE;
//			} else {
//				return Boolean.TRUE;
//			}
//		}
		return !getLiberadaSemDisponibilidadeVagas() 
				&& (((Uteis.isAtributoPreenchido(getTurmaPratica()) && getNrVagasDisponiveisTurmaPratica() <= 0)
						|| (Uteis.isAtributoPreenchido(getTurmaTeorica()) && getNrVagasDisponiveisTurmaTeorica() <= 0)
						|| (!Uteis.isAtributoPreenchido(getTurmaPratica()) && !Uteis.isAtributoPreenchido(getTurmaTeorica()) && Uteis.isAtributoPreenchido(getTurma()) && getNrVagasDisponiveis() <= 0))
						&& (getDisciplinaComposta() && TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS.equals(getGradeDisciplinaVO().getTipoControleComposicao()) || !getDisciplinaComposta()));
	}
    
    @Override
    public Boolean getNovoObj() {
        if (this.getCodigo().equals(0)) {
            return true;
        }
        return false;
    }

	public Boolean getDisciplinaForaGrade() {
		if (disciplinaForaGrade == null) {
			disciplinaForaGrade = Boolean.FALSE;
		}
		return disciplinaForaGrade;
	}
	

	public void setDisciplinaForaGrade(Boolean disciplinaForaGrade) {
		this.disciplinaForaGrade = disciplinaForaGrade;
	}
        
    public Boolean getModificadoPorTransferenciaMatriculaCurricular() {
        if (!getTransferenciaMatrizCurricularMatricula().getCodigo().equals(0)) {
            return true;
        }
        return false;
   }

    /**
     * @return the transferenciaMatrizCurricularMatricula
     */
    public TransferenciaMatrizCurricularMatriculaVO getTransferenciaMatrizCurricularMatricula() {
        if (transferenciaMatrizCurricularMatricula == null) {
            transferenciaMatrizCurricularMatricula = new TransferenciaMatrizCurricularMatriculaVO();
        }
        return transferenciaMatrizCurricularMatricula;
    }

    /**
     * @param transferenciaMatrizCurricularMatricula the transferenciaMatrizCurricularMatricula to set
     */
    public void setTransferenciaMatrizCurricularMatricula(TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatricula) {
        this.transferenciaMatrizCurricularMatricula = transferenciaMatrizCurricularMatricula;
    }

    /**
     * @return the disciplinaCursandoPorCorrespondenciaAposTransferencia
     */
    public Boolean getDisciplinaCursandoPorCorrespondenciaAposTransferencia() {
        if (disciplinaCursandoPorCorrespondenciaAposTransferencia == null) {
            disciplinaCursandoPorCorrespondenciaAposTransferencia = Boolean.FALSE;
        }
        return disciplinaCursandoPorCorrespondenciaAposTransferencia;
    }

    /**
     * @param disciplinaCursandoPorCorrespondenciaAposTransferencia the disciplinaCursandoPorCorrespondenciaAposTransferencia to set
     */
    public void setDisciplinaCursandoPorCorrespondenciaAposTransferencia(Boolean disciplinaCursandoPorCorrespondenciaAposTransferencia) {
        this.disciplinaCursandoPorCorrespondenciaAposTransferencia = disciplinaCursandoPorCorrespondenciaAposTransferencia;
    }
    
    /**
     * @author Victor Hugo 14/11/2014
     * 
     */
    private PessoaVO professor;
    /*
     * Transient
     */
    private Boolean selecionarMatricula; 
    
	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}
	
	public String getApresentarAnoSemestre() {
		return getAno()+"/"+getSemestre();
	}

	public Boolean getSelecionarMatricula() {
		if (selecionarMatricula == null) {
			selecionarMatricula = false;
		}
		return selecionarMatricula;
	}

	public void setSelecionarMatricula(Boolean selecionarMatricula) {
		this.selecionarMatricula = selecionarMatricula;
	}
	
	/**
	 * 
	 * @author Victor Hugo 08/12/2014
	 * 
	 */
	/*
	 * Transient
	 */
	private String situacaoNotificacaoAtrasoEstudosAluno;



	public String getSituacaoNotificacaoAtrasoEstudosAluno() {
		if (situacaoNotificacaoAtrasoEstudosAluno == null) {
			situacaoNotificacaoAtrasoEstudosAluno = "";
		}
		return situacaoNotificacaoAtrasoEstudosAluno;
	}

	public void setSituacaoNotificacaoAtrasoEstudosAluno(String situacaoNotificacaoAtrasoEstudosAluno) {
		this.situacaoNotificacaoAtrasoEstudosAluno = situacaoNotificacaoAtrasoEstudosAluno;
	}
	/*
	 * Fim Transient
	 */
	
	
	/**
	 * @author Victor Hugo 15/12/2014
	 * 
	 */
	/*
	 * Transient
	 */
	private String graficoDesempenhoAluno;
	
	public String getGraficoDesempenhoAluno() {
		if (graficoDesempenhoAluno == null) {
			StringBuilder grafico = new StringBuilder();

			grafico.append("<div id=\"container").append(getCodigo()).append("\" style=\" height: 160px; margin: 0 auto\" class=\"col-md-12\" /> ");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("	var options").append(getCodigo()).append(" = {");
			grafico.append(" 			chart: {type: \"bar\", renderTo : \"container").append(getCodigo()).append("\", marginTop:5},");
			grafico.append(" 			title: {style: {font: '2px arial', color: '#424242', margin:0},");
			grafico.append(" 			text: ' '},");
			grafico.append(" 			credits : {enabled : false}, ");
			grafico.append(" 			exporting: { enabled: false }, ");
			grafico.append(" 			xAxis: {categories: [' ']}, ");
			grafico.append(" 			yAxis: {min: 0, title: {text: ' '}}, ");
			grafico.append(" 			legend: {borderWidth: 0, align: 'center', margin:5, verticalAlign: 'bottom', itemStyle: {'fontSize': '10px'}},");
			grafico.append(" 			tooltip: { ");
			grafico.append(" 			pointFormat: '").append("<span style=\"color:{series.color};\">{series.name}</span>', ");
			grafico.append(" 			shared: false,  useHTML: true}, ");
			grafico.append(" 			plotOptions: {bar: {stacking: 'percent'}}, ");
			grafico.append(" 			series: [{name: 'Pendente (").append(getConteudo().getPercentARealizar()).append("%)', data: [" + (getConteudo().getPercentARealizar()) + "],  color: '#FF6600',").append("index: 2, zIndex: 100 } ");
			grafico.append("			, {name: 'Atrasado (").append(getConteudo().getPercentAtrasado()).append("%)', data: [").append(getConteudo().getPercentAtrasado()).append("], color: '").append("#AA4643").append("', index: 1, zIndex: 50 }");
			grafico.append("			, {name: 'Estudado (").append(getConteudo().getPercentEstudado()).append("%)', data: [" + getConteudo().getPercentEstudado() + "], color: '#4572A7', font: '10px arial',").append("index: 0, zIndex: 1 }");
			grafico.append(" 		]};");
			grafico.append(" 		var chart").append(getCodigo()).append(" = new Highcharts.Chart(options").append(getCodigo()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");
			
			graficoDesempenhoAluno = grafico.toString();
		}
		return graficoDesempenhoAluno;
	}

	public void setGraficoDesempenhoAluno(String graficoDesempenhoAluno) {
		this.graficoDesempenhoAluno = graficoDesempenhoAluno;
	}
	/*
	 * Fim Transient
	 */
	
	/**
	 * @author Victor Hugo 19/01/2015
	 */
	private ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO;



	public ProgramacaoTutoriaOnlineProfessorVO getProgramacaoTutoriaOnlineProfessorVO() {
		if (programacaoTutoriaOnlineProfessorVO == null) {
			programacaoTutoriaOnlineProfessorVO = new ProgramacaoTutoriaOnlineProfessorVO();
		}
		return programacaoTutoriaOnlineProfessorVO;
	}

	public void setProgramacaoTutoriaOnlineProfessorVO(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO) {
		this.programacaoTutoriaOnlineProfessorVO = programacaoTutoriaOnlineProfessorVO;
	}
	
	/**
	 * Variável Transient Ordenadora. Ordena a lista de Disciplinas On-line
	 */
	private Integer ordemEstudoOnline;



	public Integer getOrdemEstudoOnline() {
		if(ordemEstudoOnline == null) {
			ordemEstudoOnline = 0;
		}
		return ordemEstudoOnline;
	}

	public void setOrdemEstudoOnline(Integer ordemEstudoOnline) {
		this.ordemEstudoOnline = ordemEstudoOnline;
	}

	public Boolean getExisteRegistroAula() {
		if (existeRegistroAula == null) {
			existeRegistroAula = false;
		}
		return existeRegistroAula;
	}

	public void setExisteRegistroAula(Boolean existeRegistroAula) {
		this.existeRegistroAula = existeRegistroAula;
	}

	public Boolean getRealizarAbonoRegistroAula() {
		if (realizarAbonoRegistroAula == null) {
			realizarAbonoRegistroAula = false;
		}
		return realizarAbonoRegistroAula;
	}

	public void setRealizarAbonoRegistroAula(Boolean realizarAbonoRegistroAula) {
		this.realizarAbonoRegistroAula = realizarAbonoRegistroAula;
	}

	public Integer getQtdeHorasRegistrarAbono() {
		if (qtdeHorasRegistrarAbono == null) {
			qtdeHorasRegistrarAbono = 0;
		}
		return qtdeHorasRegistrarAbono;
	}

	public void setQtdeHorasRegistrarAbono(Integer qtdeHorasRegistrarAbono) {
		this.qtdeHorasRegistrarAbono = qtdeHorasRegistrarAbono;
	}

	public UsuarioVO getUsuarioLiberacaoChoqueHorario() {
		return usuarioLiberacaoChoqueHorario;
	}

	public void setUsuarioLiberacaoChoqueHorario(UsuarioVO usuarioLiberacaoChoqueHorario) {
		this.usuarioLiberacaoChoqueHorario = usuarioLiberacaoChoqueHorario;
	}

	@XmlElement(name = "turmaTeorica")
	public TurmaVO getTurmaTeorica() {
		if (turmaTeorica == null) {
			turmaTeorica = new TurmaVO();
		}
		return turmaTeorica;
	}

	public void setTurmaTeorica(TurmaVO turmaTeorica) {
		this.turmaTeorica = turmaTeorica;
	}

	@XmlElement(name = "turmaPratica")
	public TurmaVO getTurmaPratica() {
		if (turmaPratica == null) {
			turmaPratica = new TurmaVO();
		}
		return turmaPratica;
	}

	public void setTurmaPratica(TurmaVO turmaPratica) {
		this.turmaPratica = turmaPratica;
	}

	/**
	 * @return the sugestaoTurmaPraticaTeoricaRealizada
	 */
	public Boolean getSugestaoTurmaPraticaTeoricaRealizada() {
		if (sugestaoTurmaPraticaTeoricaRealizada == null) {
			sugestaoTurmaPraticaTeoricaRealizada = false;
		}
		return sugestaoTurmaPraticaTeoricaRealizada;
	}

	/**
	 * @param sugestaoTurmaPraticaTeoricaRealizada the sugestaoTurmaPraticaTeoricaRealizada to set
	 */
	public void setSugestaoTurmaPraticaTeoricaRealizada(Boolean sugestaoTurmaPraticaTeoricaRealizada) {
		this.sugestaoTurmaPraticaTeoricaRealizada = sugestaoTurmaPraticaTeoricaRealizada;
	}

	/**
	 * @return the nrAlunosMatriculadosTurmaPratica
	 */
	public Integer getNrAlunosMatriculadosTurmaPratica() {
		if (nrAlunosMatriculadosTurmaPratica == null) {
			nrAlunosMatriculadosTurmaPratica = 0;
		}
		return nrAlunosMatriculadosTurmaPratica;
	}

	/**
	 * @param nrAlunosMatriculadosTurmaPratica the nrAlunosMatriculadosTurmaPratica to set
	 */
	public void setNrAlunosMatriculadosTurmaPratica(Integer nrAlunosMatriculadosTurmaPratica) {
		this.nrAlunosMatriculadosTurmaPratica = nrAlunosMatriculadosTurmaPratica;
	}

	/**
	 * @return the nrAlunosMatriculadosTurmaTeorica
	 */
	public Integer getNrAlunosMatriculadosTurmaTeorica() {
		if (nrAlunosMatriculadosTurmaTeorica == null) {
			nrAlunosMatriculadosTurmaTeorica = 0;
		}
		return nrAlunosMatriculadosTurmaTeorica;
	}

	/**
	 * @param nrAlunosMatriculadosTurmaTeorica the nrAlunosMatriculadosTurmaTeorica to set
	 */
	public void setNrAlunosMatriculadosTurmaTeorica(Integer nrAlunosMatriculadosTurmaTeorica) {
		this.nrAlunosMatriculadosTurmaTeorica = nrAlunosMatriculadosTurmaTeorica;
	}

	/**
	 * @return the nrVagasDisponiveisTurmaPratica
	 */
	public Integer getNrVagasDisponiveisTurmaPratica() {
		if (nrVagasDisponiveisTurmaPratica == null) {
			nrVagasDisponiveisTurmaPratica = 0;
		}
		return nrVagasDisponiveisTurmaPratica;
	}

	/**
	 * @param nrVagasDisponiveisTurmaPratica the nrVagasDisponiveisTurmaPratica to set
	 */
	public void setNrVagasDisponiveisTurmaPratica(Integer nrVagasDisponiveisTurmaPratica) {
		this.nrVagasDisponiveisTurmaPratica = nrVagasDisponiveisTurmaPratica;
	}

	/**
	 * @return the nrVagasDisponiveisTurmaTeorica
	 */
	public Integer getNrVagasDisponiveisTurmaTeorica() {
		if (nrVagasDisponiveisTurmaTeorica == null) {
			nrVagasDisponiveisTurmaTeorica = 0;
		}
		return nrVagasDisponiveisTurmaTeorica;
	}

	/**
	 * @param nrVagasDisponiveisTurmaTeorica the nrVagasDisponiveisTurmaTeorica to set
	 */
	public void setNrVagasDisponiveisTurmaTeorica(Integer nrVagasDisponiveisTurmaTeorica) {
		this.nrVagasDisponiveisTurmaTeorica = nrVagasDisponiveisTurmaTeorica;
	}
	
	public Boolean getApresentarLiberacaoVaga() {
		return (this.getCodigo().intValue() == 0  || isAlterandoSubturmaPraticaTeorica()) 
				&& !getLiberadaSemDisponibilidadeVagas() 
				&& (((Uteis.isAtributoPreenchido(getTurmaPratica()) && getNrVagasDisponiveisTurmaPratica() <= 0)
						|| (Uteis.isAtributoPreenchido(getTurmaTeorica()) && getNrVagasDisponiveisTurmaTeorica() <= 0)
						|| (!Uteis.isAtributoPreenchido(getTurmaPratica()) && !Uteis.isAtributoPreenchido(getTurmaTeorica()) && Uteis.isAtributoPreenchido(getTurma()) && getNrVagasDisponiveis() <= 0))
						);
	}
	
	public Date getDataRegistroHistorico() {
		return dataRegistroHistorico;
	}

	public void setDataRegistroHistorico(Date dataRegistroHistorico) {
		this.dataRegistroHistorico = dataRegistroHistorico;
	}

	public boolean isAlterandoSubturmaPraticaTeorica() {
		return alterandoSubturmaPraticaTeorica;
	}

	public void setAlterandoSubturmaPraticaTeorica(boolean alterandoSubturmaPraticaTeorica) {
		this.alterandoSubturmaPraticaTeorica = alterandoSubturmaPraticaTeorica;
	}

	public String getDescricaoPreRequisitoDisciplina() {
		if (descricaoPreRequisitoDisciplina == null) {
			descricaoPreRequisitoDisciplina = "";
		}
		return descricaoPreRequisitoDisciplina;
	}

	public void setDescricaoPreRequisitoDisciplina(String descricaoPreRequisitoDisciplina) {
		this.descricaoPreRequisitoDisciplina = descricaoPreRequisitoDisciplina;
	}

	public UsuarioVO getUsuarioLiberacaoPreRequisito() {
		if (usuarioLiberacaoPreRequisito == null) {
			usuarioLiberacaoPreRequisito = new UsuarioVO();
		}
		return usuarioLiberacaoPreRequisito;
	}

	public void setUsuarioLiberacaoPreRequisito(UsuarioVO usuarioLiberacaoPreRequisito) {
		this.usuarioLiberacaoPreRequisito = usuarioLiberacaoPreRequisito;
	}

	public Integer getCargaHoraria() {
		if (getDisciplinaReferenteAUmGrupoOptativa()) {
			return getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
		} else {
			return getGradeDisciplinaVO().getCargaHoraria();
		}
	}

	public HistoricoVO getHistoricoAnteriorDisciplinaMigrarNotas() {
		return historicoAnteriorDisciplinaMigrarNotas;
	}

	public void setHistoricoAnteriorDisciplinaMigrarNotas(HistoricoVO historicoAnteriorDisciplinaMigrarNotas) {
		this.historicoAnteriorDisciplinaMigrarNotas = historicoAnteriorDisciplinaMigrarNotas;
	}

	public Boolean getIgnorarValidacaoDisciplinaSendoCursadaAluno() {
		if (ignorarValidacaoDisciplinaSendoCursadaAluno == null) {
			ignorarValidacaoDisciplinaSendoCursadaAluno = Boolean.FALSE;
		}
		return ignorarValidacaoDisciplinaSendoCursadaAluno;
	}

	public void setIgnorarValidacaoDisciplinaSendoCursadaAluno(Boolean ignorarValidacaoDisciplinaSendoCursadaAluno) {
		this.ignorarValidacaoDisciplinaSendoCursadaAluno = ignorarValidacaoDisciplinaSendoCursadaAluno;
	}
	
	public HistoricoVO getHistoricoAnteriorDisciplinaEquivalenteMigrarNotas() {
		if (historicoAnteriorDisciplinaEquivalenteMigrarNotas == null) {
			historicoAnteriorDisciplinaEquivalenteMigrarNotas = new HistoricoVO();
		}
		return historicoAnteriorDisciplinaEquivalenteMigrarNotas;
	}

	public void setHistoricoAnteriorDisciplinaEquivalenteMigrarNotas(HistoricoVO historicoAnteriorDisciplinaEquivalenteMigrarNotas) {
		this.historicoAnteriorDisciplinaEquivalenteMigrarNotas = historicoAnteriorDisciplinaEquivalenteMigrarNotas;
	}

	public Boolean getDisciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia() {
		if (disciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia == null) {
			disciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia = false;
		}
		return disciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia;
	}

	public void setDisciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia(Boolean disciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia) {
		this.disciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia = disciplinaIncluidaAposTransferenciaMatrizCurricularControlarEquivalencia;
	}

	public UsuarioVO getUsuarioLiberadaSemDisponibilidadeVagas() {
		if (usuarioLiberadaSemDisponibilidadeVagas == null) {
			usuarioLiberadaSemDisponibilidadeVagas = new UsuarioVO();
		}
		return usuarioLiberadaSemDisponibilidadeVagas;
	}

	public void setUsuarioLiberadaSemDisponibilidadeVagas(UsuarioVO usuarioLiberadaSemDisponibilidadeVagas) {
		this.usuarioLiberadaSemDisponibilidadeVagas = usuarioLiberadaSemDisponibilidadeVagas;
	}

	public Date getDataLiberadaSemDisponibilidadeVagas() {
		if (dataLiberadaSemDisponibilidadeVagas == null) {
			dataLiberadaSemDisponibilidadeVagas = new Date();
		}
		return dataLiberadaSemDisponibilidadeVagas;
	}

	public void setDataLiberadaSemDisponibilidadeVagas(Date dataLiberadaSemDisponibilidadeVagas) {
		this.dataLiberadaSemDisponibilidadeVagas = dataLiberadaSemDisponibilidadeVagas;
	}

	public Timestamp getDataultimaalteracao() {
		if (dataultimaalteracao == null)
			dataultimaalteracao = Uteis.getDataJDBCTimestamp(new Date());;
		return dataultimaalteracao;
	}

	public void setDataultimaalteracao(Timestamp dataultimaalteracao) {
		this.dataultimaalteracao = dataultimaalteracao;
	}
	
	public Integer getNumeroAgrupamentoEquivalenciaDisciplina() {
		if (numeroAgrupamentoEquivalenciaDisciplina == null) {
			numeroAgrupamentoEquivalenciaDisciplina = 0;
		}
		return numeroAgrupamentoEquivalenciaDisciplina;
	}

	public void setNumeroAgrupamentoEquivalenciaDisciplina(Integer numeroAgrupamentoEquivalenciaDisciplina) {
		this.numeroAgrupamentoEquivalenciaDisciplina = numeroAgrupamentoEquivalenciaDisciplina;
	}
	
	private Boolean disciplinaAdaptacao;

	public Boolean getDisciplinaAdaptacao() {
		if(disciplinaAdaptacao == null) {
			disciplinaAdaptacao = false;
		}
		return disciplinaAdaptacao;
	}

	public void setDisciplinaAdaptacao(Boolean disciplinaAdaptacao) {
		this.disciplinaAdaptacao = disciplinaAdaptacao;
	}
	
	 public String getJaPossuiAvaliacaoOnline() {
		 if(jaPossuiAvaliacaoOnline ==null ) {
			 jaPossuiAvaliacaoOnline ="";
		 }
			return jaPossuiAvaliacaoOnline;
		}

		public void setJaPossuiAvaliacaoOnline(String jaPossuiAvaliacaoOnline) {
			this.jaPossuiAvaliacaoOnline = jaPossuiAvaliacaoOnline;
		}

		public String getJaAcessouConteudo() {
			if(jaAcessouConteudo == null) {
				jaAcessouConteudo = "";
			}
			return jaAcessouConteudo;
		}

		public void setJaAcessouConteudo(String jaAcessouConteudo) {
			this.jaAcessouConteudo = jaAcessouConteudo;
		}

		public Integer getConteudoSugerido() {
			if(conteudoSugerido == null){
				conteudoSugerido = 0;
			}
			return conteudoSugerido;
		}

		public void setConteudoSugerido(Integer conteudoSugerido) {
			this.conteudoSugerido = conteudoSugerido;
		}	

		public Boolean getSelecionarAlunoConteudoDiferente() {
			if(selecionarAlunoConteudoDiferente == null ) {
				selecionarAlunoConteudoDiferente = Boolean.FALSE;
			}
			return selecionarAlunoConteudoDiferente;
		}

		public void setSelecionarAlunoConteudoDiferente(Boolean selecionarAlunoConteudoDiferente) {
			this.selecionarAlunoConteudoDiferente = selecionarAlunoConteudoDiferente;
		}

		public String getLogAlteracao() {
			if(logAlteracao == null ) {
				logAlteracao = "";
			}
			return logAlteracao;
		}

		public void setLogAlteracao(String logAlteracao) {
			this.logAlteracao = logAlteracao;
		}


	public Boolean getLiberarInclusaoDisciplinaPreRequisito() {
		if (liberarInclusaoDisciplinaPreRequisito == null) {
			liberarInclusaoDisciplinaPreRequisito = Boolean.FALSE;
		}
		return liberarInclusaoDisciplinaPreRequisito;
	}

	public void setLiberarInclusaoDisciplinaPreRequisito(Boolean liberarInclusaoDisciplinaPreRequisito) {
		this.liberarInclusaoDisciplinaPreRequisito = liberarInclusaoDisciplinaPreRequisito;
	}
	
		

	public Boolean getLiberarInclusaoDisciplinaChoqueHorario() {
		if (liberarInclusaoDisciplinaChoqueHorario == null) {
			liberarInclusaoDisciplinaChoqueHorario = Boolean.FALSE;
		}
		return liberarInclusaoDisciplinaChoqueHorario;
	}

	public void setLiberarInclusaoDisciplinaChoqueHorario(Boolean liberarInclusaoDisciplinaChoqueHorario) {
		this.liberarInclusaoDisciplinaChoqueHorario = liberarInclusaoDisciplinaChoqueHorario;
	}
	
	public DefinicoesTutoriaOnlineEnum getDefinicoesTutoriaOnline() {
		return definicoesTutoriaOnline;
	}

	public void setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum definicoesTutoriaOnline) {
		this.definicoesTutoriaOnline = definicoesTutoriaOnline;
	}

	public ClassroomGoogleVO getClassroomGoogleVO() {
		if(classroomGoogleVO == null) {
			classroomGoogleVO = new ClassroomGoogleVO();
		}
		return classroomGoogleVO;
	}

	public void setClassroomGoogleVO(ClassroomGoogleVO classroomGoogleVO) {
		this.classroomGoogleVO = classroomGoogleVO;
	}
	
	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardGrupoVO() {
		if (salaAulaBlackboardGrupoVO == null) {
			salaAulaBlackboardGrupoVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardGrupoVO;
	}

	public void setSalaAulaBlackboardGrupoVO(SalaAulaBlackboardVO salaAulaBlackboardGrupoVO) {
		this.salaAulaBlackboardGrupoVO = salaAulaBlackboardGrupoVO;
	}

	public String getPeriodoLetivoApresentar() {
		if (periodoLetivoApresentar == null) {
			periodoLetivoApresentar = "";
			if (Uteis.isAtributoPreenchido(getGradeDisciplinaVO())) {
				periodoLetivoApresentar = getGradeDisciplinaVO().getPeriodoLetivoVO().getDescricao();
			} else if (Uteis.isAtributoPreenchido(getGradeDisciplinaCompostaVO())) {
				periodoLetivoApresentar = getGradeDisciplinaCompostaVO().getPeriodoLetivoVO().getDescricao();
			} else if (Uteis.isAtributoPreenchido(getGradeCurricularGrupoOptativaDisciplinaVO())) {
				periodoLetivoApresentar = getGradeCurricularGrupoOptativaDisciplinaVO().getPeriodoLetivoDisciplinaReferenciada().getDescricao();
			}
		}
		return periodoLetivoApresentar;
	}
	@ExcluirJsonAnnotation
	private HistoricoVO historicoVO;
	@ExcluirJsonAnnotation
	private Integer qtdeMaterialDownload;
	@ExcluirJsonAnnotation
	private Integer qtdeAdvertencia;
	@ExcluirJsonAnnotation
	private Integer qtdeForum;
	@ExcluirJsonAnnotation
	private Integer qtdeAtividadeDiscursiva;
	@ExcluirJsonAnnotation
	private Integer qtdeDuvidaProfessor;
	@ExcluirJsonAnnotation
	private Integer qtdeListaExercicio;
	@ExcluirJsonAnnotation
	private Integer qtdeAvaliacaoOnline;
	@ExcluirJsonAnnotation
	private Boolean permiteAcessoEAD;
	@ExcluirJsonAnnotation
	private Boolean ocultarDownload;
	
	
	public HistoricoVO getHistoricoVO() {
		if(historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public Integer getQtdeMaterialDownload() {
		if(qtdeMaterialDownload == null) {
			qtdeMaterialDownload = 0;
		}
		return qtdeMaterialDownload;
	}

	public void setQtdeMaterialDownload(Integer qtdeMaterialDownload) {
		this.qtdeMaterialDownload = qtdeMaterialDownload;
	}

	public Integer getQtdeAdvertencia() {
		if(qtdeAdvertencia == null) {
			qtdeAdvertencia = 0;
		}
		return qtdeAdvertencia;
	}

	public void setQtdeAdvertencia(Integer qtdeAdvertencia) {
		this.qtdeAdvertencia = qtdeAdvertencia;
	}

	public Integer getQtdeForum() {
		if(qtdeForum == null) {
			qtdeForum = 0;
		}
		return qtdeForum;
	}

	public void setQtdeForum(Integer qtdeForum) {
		this.qtdeForum = qtdeForum;
	}

	public Integer getQtdeAtividadeDiscursiva() {
		if(qtdeAtividadeDiscursiva == null) {
			qtdeAtividadeDiscursiva = 0;
		}
		return qtdeAtividadeDiscursiva;
	}

	public void setQtdeAtividadeDiscursiva(Integer qtdeAtividadeDiscursiva) {
		this.qtdeAtividadeDiscursiva = qtdeAtividadeDiscursiva;
	}

	public Integer getQtdeDuvidaProfessor() {
		if(qtdeDuvidaProfessor == null) {
			qtdeDuvidaProfessor = 0;
		}
		return qtdeDuvidaProfessor;
	}

	public void setQtdeDuvidaProfessor(Integer qtdeDuvidaProfessor) {
		this.qtdeDuvidaProfessor = qtdeDuvidaProfessor;
	}

	public Integer getQtdeListaExercicio() {
		if(qtdeListaExercicio == null) {
			qtdeListaExercicio = 0;
		}
		return qtdeListaExercicio;
	}

	public void setQtdeListaExercicio(Integer qtdeListaExercicio) {
		this.qtdeListaExercicio = qtdeListaExercicio;
	}
	
	

	public Integer getQtdeAvaliacaoOnline() {
		if(qtdeAvaliacaoOnline == null) {
			qtdeAvaliacaoOnline = 0;
		}
		return qtdeAvaliacaoOnline;
	}

	public void setQtdeAvaliacaoOnline(Integer qtdeAvaliacaoOnline) {
		this.qtdeAvaliacaoOnline = qtdeAvaliacaoOnline;
	}

	public Boolean getPermiteAcessoEAD() {
		if(permiteAcessoEAD == null) {
			permiteAcessoEAD = false;
		}
		return permiteAcessoEAD;
	}

	public void setPermiteAcessoEAD(Boolean permiteAcessoEAD) {
		this.permiteAcessoEAD = permiteAcessoEAD;
	}
	@ExcluirJsonAnnotation
	private Date dataInicioPeriodoEstudo;
	@ExcluirJsonAnnotation
	private Date dataFimPeriodoEstudo;

	public Date getDataInicioPeriodoEstudo() {
		
		return dataInicioPeriodoEstudo;
	}

	public void setDataInicioPeriodoEstudo(Date dataInicioPeriodoEstudo) {
		this.dataInicioPeriodoEstudo = dataInicioPeriodoEstudo;
	}

	public Date getDataFimPeriodoEstudo() {
		return dataFimPeriodoEstudo;
	}

	public void setDataFimPeriodoEstudo(Date dataFimPeriodoEstudo) {
		this.dataFimPeriodoEstudo = dataFimPeriodoEstudo;
	}
	
	
	
	@ExcluirJsonAnnotation
	public String periodoEstudoApresentar;
	public String getPeriodoEstudoApresentar() {
		if(periodoEstudoApresentar == null) {
			periodoEstudoApresentar =  getDataInicioPeriodoEstudo() == null ? getCodigo().equals(0) ? "" : "Aguardando Definição" : "Período Estudo: "+Uteis.getData(getDataInicioPeriodoEstudo()) +" à "+Uteis.getData(getDataFimPeriodoEstudo()); 
		}
		return periodoEstudoApresentar;
	}
	

	public String getOrdenacaoTelaInicialVisaoAluno() {
		try {
			return (getDataFimPeriodoEstudo() == null || getDataInicioPeriodoEstudo() == null ? "9" : 
				((UteisData.getMesData(getDataFimPeriodoEstudo()) ==  UteisData.getMesData(new Date()) 
				&& UteisData.getAnoData(getDataFimPeriodoEstudo()) ==  UteisData.getAnoData(new Date()))
						|| (UteisData.getMesData(getDataInicioPeriodoEstudo()) ==  UteisData.getMesData(new Date()) 
								&& UteisData.getAnoData(getDataInicioPeriodoEstudo()) ==  UteisData.getAnoData(new Date()))
						|| (UteisData.getCompareData(getDataInicioPeriodoEstudo() , new Date()) <= 0 && UteisData.getCompareData(getDataFimPeriodoEstudo() , new Date()) >= 0)) ? "0" : UteisData.getCompareData(getDataFimPeriodoEstudo() , new Date()) >= 0 ? "1" : "2") + 
				(getDataFimPeriodoEstudo() == null ? "9999/99/99" : Uteis.getData(getDataFimPeriodoEstudo(), "yyyy/MM/dd")) + 
				getOrdemEstudoOnline()+getDisciplina().getNome();
		} catch (ParseException e) {
			return "99999/99/99";
		}
	}

	public Boolean getOcultarDownload() {
		if(ocultarDownload == null) {
			ocultarDownload = false;
		}
		return ocultarDownload;
	}

	public void setOcultarDownload(Boolean ocultarDownload) {
		this.ocultarDownload = ocultarDownload;
	}

	public PessoaEmailInstitucionalVO getPessoaEmailInstitucionalVO() {
		if(pessoaEmailInstitucionalVO == null) {
			pessoaEmailInstitucionalVO =  new PessoaEmailInstitucionalVO();
		}
		return pessoaEmailInstitucionalVO;
	}

	public void setPessoaEmailInstitucionalVO(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO) {
		this.pessoaEmailInstitucionalVO = pessoaEmailInstitucionalVO;
	}
	
	public Date getDataInicioAula() {
		return dataInicioAula;
	}
	
	public void setDataInicioAula(Date dataInicioAula) {
		this.dataInicioAula = dataInicioAula;
	}
	
	public Date getDataFimAula() {
		return dataFimAula;
	}
	
	public void setDataFimAula(Date dataFimAula) {
		this.dataFimAula = dataFimAula;
	}

	public Integer getBimestre() {		
		return bimestre;
	}

	public void setBimestre(Integer bimestre) {
		this.bimestre = bimestre;
	}
	
	public Boolean getApresentarRemoverVisaoAluno() {
		if (apresentarRemoverVisaoAluno == null) {
			apresentarRemoverVisaoAluno = Boolean.FALSE;
		}
		return apresentarRemoverVisaoAluno;
	}

	public void setApresentarRemoverVisaoAluno(Boolean apresentarRemoverVisaoAluno) {
		this.apresentarRemoverVisaoAluno = apresentarRemoverVisaoAluno;
	}
	
	private Boolean apresentarRelatoriofinalfacilitador;
	private Boolean apresentarConsultarRelatoriofinalfacilitador;

	public Boolean getApresentarRelatoriofinalfacilitador() {
		if (apresentarRelatoriofinalfacilitador == null) {
			apresentarRelatoriofinalfacilitador = Boolean.FALSE;
		}
		return apresentarRelatoriofinalfacilitador;
	}

	public void setApresentarRelatoriofinalfacilitador(Boolean apresentarRelatoriofinalfacilitador) {
		this.apresentarRelatoriofinalfacilitador = apresentarRelatoriofinalfacilitador;
	}

	public Boolean getApresentarConsultarRelatoriofinalfacilitador() {
		if (apresentarConsultarRelatoriofinalfacilitador == null) {
			apresentarConsultarRelatoriofinalfacilitador = Boolean.FALSE;
		}
		return apresentarConsultarRelatoriofinalfacilitador;
	}

	public void setApresentarConsultarRelatoriofinalfacilitador(Boolean apresentarConsultarRelatoriofinalfacilitador) {
		this.apresentarConsultarRelatoriofinalfacilitador = apresentarConsultarRelatoriofinalfacilitador;
	}

	
}
