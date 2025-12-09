package negocio.comuns.academico;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;

import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ChancelaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.IndiceReajusteVO;
import negocio.comuns.utilitarias.AdicionarEmTurmaAgrupadaException;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import webservice.DateAdapterMobile;

/**
 * Reponsável por manter os dados da entidade Turma. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@SuppressWarnings("rawtypes")
@XmlRootElement(name = "turma")
public class TurmaVO extends SuperVO implements Cloneable, Comparable {

    private Integer codigo;
    private String identificadorTurma;
    private Integer gradeCurricularAtiva;
    private Integer nrVagas;
    private Integer nrMaximoMatricula;
    private Integer nrMinimoMatricula;
    private Integer nrVagasInclusaoReposicao;    
    private String situacao;
    private CursoVO curso;
    private GradeCurricularVO gradeCurricularVO;
    private String sala;
    private Date dataBaseGeracaoParcelas;    
    private PeriodoLetivoVO peridoLetivo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turno </code>.
     */
    private TurnoVO turno;
    private List<TurmaDisciplinaVO> turmaDisciplinaVOs;
    private List<TurmaAberturaVO> turmaAberturaVOs;
    private Boolean turmaAgrupada;
    private List<TurmaAgrupadaVO> turmaAgrupadaVOs;
    private Boolean semestral;
    private Boolean anual;
    private PlanoFinanceiroCursoVO planoFinanceiroCurso;
    private String categoriaCondicaoPagamento; 

    private GrupoDestinatariosVO grupoDestinatarios;
    private ChancelaVO chancelaVO;
    private String tipoChancela;
    private Double porcentagemChancela;
    private Double valorFixoChancela;
    private Boolean valorPorAluno;
    private ContaCorrenteVO contaCorrente;
    private Integer qtdAlunosEstimado;
    private Double custoMedioAluno;
    private Double receitaMediaAluno;
    private Boolean utilizarDadosMatrizBoleto;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date dataPrevisaoFinalizacao;
    private Boolean considerarTurmaAvaliacaoInstitucional;

    
    // Esse campos é apenas para apresentação em tela, não deve ser persistido
    // no banco de dados.
    private String dataPrimeiraAulaProgramada;
    private String observacao;
    private Boolean turmaSelecionada;
    private String tipoAlunoFiltro;    
    public static final long serialVersionUID = 1L;
    private Boolean subturma;
    private Integer turmaPrincipal;
    private String identificadorTurmaPrincipal;
    /**
     * Utilizada apenas na distribuicao subturma
     */
    private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs;
    private Integer qtdeAlunosSubturma;
    private Integer qtdeAlunosDistribuir;
    private Boolean utilizarSubturmaNaDistribuicao;
    /**
     * Atributo transient, utilizado somente para apresentar para 
     * o usuário o total de CH já programado em disciplinas regulares (nao optativas)
     * Este atributo pode ser atualizado pelo método 
     * atualizarTotalCreditoCargaHorariaDisciplinasOptativas
     */
    private Integer totalCargaHorariaDisciplinasRegulares;
    /**
     * Atributo transient, utilizado somente para apresentar para 
     * o usuário o total de CH já programado em disciplinas regulares (nao optativas)
     * Este atributo pode ser atualizado pelo método 
     * atualizarTotalCreditoCargaHorariaDisciplinasOptativas
     */
    private Integer totalCreditosDisciplinasRegulares;
    /**
     * Atributo transient, utilizado somente para apresentar para 
     * o usuário o total de CH já programado em disciplinas optativas
     * Este atributo pode ser atualizado pelo método 
     * atualizarTotalCreditoCargaHorariaDisciplinasOptativas
     */
    private Integer totalCargaHorariaDisciplinasOptativas;
    /**
     * Atributo transient, utilizado somente para apresentar para 
     * o usuário o total de créditos já programado em disciplinas optativas
     * Este atributo pode ser atualizado pelo método 
     * atualizarTotalCreditoCargaHorariaDisciplinasOptativas
     */
    private Integer totalCreditosDisciplinasOptativas;
	private Boolean apresentarRenovacaoOnline;
    private List<TurmaDisciplinaInclusaoSugeridaVO> turmaDisciplinaInclusaoSugeridaVOs;
	/**
	 * Utilizado quando a turma for uma subturma
	 */
	private TipoSubTurmaEnum tipoSubTurma;
	private boolean possuiAulaProgramada = false;
	private String turmaAgrupadaProgramacaoAula;
	private String abreviaturaCurso;
	private Boolean disciplinasAtualizadasAlteracaoMatrizCurricular;

	private Integer qtdMatriculados;
	private Integer qtdConfirmados;
	private Integer qtdPreMatriculados;
	private IndiceReajusteVO IndiceReajusteVO;
	private Timestamp dataUltimaAlteracao;
	
	private CentroResultadoVO centroResultadoVO;
	/**
	 * 
	 * @author Rodrigo Wind
	 * Transiente apenas usado na tela de requerimento para apresentar o periodo da aula local e professor caso habilitado
	 *
	 */
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date dataPrimeiraAula;
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date dataUltimaAula;
	private SalaLocalAulaVO salaLocalAulaVO;
	private PessoaVO professor;
	private List<TurmaContratoVO> turmaContratoVOs;
	
	private VagaTurmaVO vagaTurmaVO;
	private Boolean validarDadosTurmaAgrupada;
	private List<TurmaAberturaVO> listaSituacaoAberturaTurmaBase;
	
	
	
	
    /**
     * Usado na classe {ConsultorPorMatriculaRelControle}
     */
	private Boolean filtrarTurmaVO;
	private List<TurmaAtualizacaoDisciplinaLogVO> listaTurmaAtualizacaoDisciplinaLog;
	private TurmaUnidadeEnsinoVO turmaUnidadeEnsino;
	private List<TurmaUnidadeEnsinoVO> listaTurmaUnidadeEnsino;
	
	private String digitoTurma;
	private Integer codigoTurnoApresentarCenso;
	private Integer codigoTurmaBase;
	private String identificadorTurmaBase;
	
	public enum enumCampoConsultaTurma {
		IDENTIFICADOR_TURMA;
	}
    
    /**
     * Construtor padrão da classe <code>Turma</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public TurmaVO() {
        super();
        inicializarDados();
    }

    @Override
    public TurmaVO clone() throws CloneNotSupportedException {
        TurmaVO obj = (TurmaVO) super.clone();
        return obj;
    }
    
    public TurmaVO clonar() throws CloneNotSupportedException {
		return (TurmaVO) Uteis.clonar(this);		
	}
	 

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TurmaVO</code>. Todos os tipos de consistência de dados são e devem
     * ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     * 
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TurmaVO obj) throws ConsistirException {
        if (obj.getIdentificadorTurma().trim().isEmpty()) {
            throw new ConsistirException("O campo IDENTIFICADOR (Turma) deve ser informado.");
        }
        if (obj.getSubturma() && obj.getTurmaPrincipal() == 0) {
            throw new ConsistirException("O campo TURMA PRINCIPAL (Turma) deve ser informado.");
        }
        if(obj.getNrVagas() > 0 && obj.getNrVagas() > obj.getNrMaximoMatricula()) {
        	  throw new ConsistirException("O campo NÚMERO DE VAGAS (Turma) não pode ser maior que o campo NÚMERO MÁXIMO MATRÍCULA.");
        }
//        if (obj.getNrVagas().equals(0) && !obj.getTurmaAgrupada()) {
//            throw new ConsistirException("O campo NÚMERO DE VAGAS (Turma) deve ser informado.");
//        }
//        if (obj.getNrMaximoMatricula().equals(0) && !obj.getTurmaAgrupada()) {
//            throw new ConsistirException("O campo NÚMERO MÁXIMO MATRÍCULA (Turma) deve ser informado.");
//        }
        // if (obj.getNrMinimoMatricula().intValue() == 0) {
        // throw new
        // ConsistirException("O campo NÚMERO MINIMO MATRÍCULA (Turma) deve ser informado.");
        // }
        if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Turma) deve ser informado.");
        }
        if (obj.getTurmaAgrupada() == false && !obj.getSubturma()) {
            if (!Uteis.isAtributoPreenchido(obj.getCurso())) {
                throw new ConsistirException("O campo Curso (Turma) deve ser informado.");
            }
			if (!Uteis.isAtributoPreenchido(obj.getPeridoLetivo())) {
                throw new ConsistirException("O campo Grade Curricular (Turma) deve ser informado.");
            }
			if (!Uteis.isAtributoPreenchido(obj.getPeridoLetivo()) && !obj.getTurmaAgrupada()) {
                throw new ConsistirException("O campo Periodo Letivo (Turma) deve ser informado.");
            }
            obj.setTurmaAgrupadaVOs(new ArrayList<>());
            // obj.setSemestral(obj.getCurso().getSemestral());
            // obj.setAnual(obj.getCurso().getAnual());
        }
        if (!Uteis.isAtributoPreenchido(obj.getTurno()) && !obj.getTurmaAgrupada()) {
            throw new ConsistirException("O campo TURNO (Turma) deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Turma) deve ser informado.");
        }
        Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCentroResultadoVO()), "O campo Centro de Resultado deve ser informado.");
        if (obj.getTurmaAgrupada() && obj.getValidarDadosTurmaAgrupada()) {
            // obj.setTurmaDisciplinaVOs(new ArrayList());
            if (obj.getTurmaAgrupadaVOs().isEmpty() || obj.getTurmaAgrupadaVOs().size() < 2) {
                throw new ConsistirException("Deve ser informada pelo menos duas TURMAS AGRUPADAS (Turma).");
            }
            obj.getCurso().setCodigo(0);
            obj.getPeridoLetivo().setCodigo(0);
            obj.getGradeCurricularVO().setCodigo(0);
            // obj.setTurmaDisciplinaVOs(new ArrayList());
        }
        if (obj.getDataBaseGeracaoParcelas() != null) {
        	if (!Uteis.isDate(obj.getDataBaseGeracaoParcelas())) {
        		throw new ConsistirException("O campo Data Base Geração Parcelas (Turma) deve ser informado corretamente.");
        	}
        }
        if (obj.getTurmaDisciplinaVOs().isEmpty()) {
        	throw new ConsistirException("O campo Disciplinas deve ser informado (Turma).");
        }
        
        if (Uteis.isAtributoPreenchido(obj.getIdentificadorTurma()) && obj.getIdentificadorTurma().contains("\\")) {
            throw new ConsistirException("O caractere \\ não é aceito no campo (Identificador Turma).");
        }
        
		if (Uteis.isAtributoPreenchido(obj.getTurmaContratoVOs())) {
			int totalContratoNormal = 0;
			int totalContratoPadraoNormal = 0;
			int totalContratoExtensao = 0;
			int totalContratoPadraoExtensao = 0;
			for (TurmaContratoVO turmaContratoVO : obj.getTurmaContratoVOs()) {
				if (turmaContratoVO.getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.NORMAL)) {
					totalContratoNormal++;
					if (turmaContratoVO.getPadrao()) {
						totalContratoPadraoNormal++;
					}
				} else {
					totalContratoExtensao++;
					if (turmaContratoVO.getPadrao()) {
						totalContratoPadraoExtensao++;
					}
				}
			}

			if (totalContratoNormal > 0) {
				if (totalContratoPadraoNormal == 0) {
					throw new ConsistirException("Pelo menos 1 contrato NORMAL dever ser definido como PADRÃO.");
				}
				if (totalContratoPadraoNormal > 1) {
					throw new ConsistirException("Somente 1 contrato NORMAL dever ser definido como PADRÃO.");
				}
			}

			if (totalContratoExtensao > 0) {
				if (totalContratoPadraoExtensao == 0) {
					throw new ConsistirException("Pelo menos 1 contrato EXTENSÃO dever ser definido como PADRÃO.");
				}
				if (totalContratoPadraoExtensao > 1) {
					throw new ConsistirException("Somente 1 contrato EXTENSÃO dever ser definido como PADRÃO.");
				}
			}
		}
    }

    @XmlElement(name = "integral")
    public boolean getIntegral() {
        if ((getSemestral() && !getCurso().getLiberarRegistroAulaEntrePeriodo()) || getAnual()) {
            return false;
        }
        return true;
    }
    
    public boolean getIntegralSemValidarLiberarRegistroAulaEntrePeriodo() {
        if (getSemestral() || getAnual()) {
            return false;
        }
        return true;
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setIdentificadorTurma("");
        setNrVagas(0);
        setNrMaximoMatricula(0);
        setNrMinimoMatricula(0);
        setSituacao("");
        setSala("");
        setGradeCurricularAtiva(0);
        setTurmaAgrupada(false);
        setAnual(false);
        setSemestral(false);
//        setTurmaPreMatricula(false);
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>MatriculaPeriodoVO</code> ao List <code>matriculaPeriodoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() -
     * como identificador (key) do objeto no List.
     * 
     * @param obj
     *            Objeto da classe <code>MatriculaPeriodoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjTurmaDisciplinaVOs(TurmaDisciplinaVO obj) throws Exception {
        TurmaDisciplinaVO.validarDados(obj);
        int index = 0;
        Iterator i = getTurmaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            TurmaDisciplinaVO objExistente = (TurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
                getTurmaDisciplinaVOs().set(index, obj);
                return;
            }
            index++;
        }
        getTurmaDisciplinaVOs().add(obj);
    }


    // public void adicionarObjTurmaAberturaVOs(TurmaAberturaVO obj) throws
    // Exception {
    // TurmaAberturaVO.validarDados(obj);
    // int index = 0;
    // Iterator i = getTurmaAberturaVOs().iterator();
    // while (i.hasNext()) {
    // TurmaAberturaVO objExistente = (TurmaAberturaVO) i.next();
    // if
    // (objExistente.getTurma().getCodigo().equals(obj.getTurma().getCodigo())
    // && objExistente.getSituacao().equals("AD")) {
    // getTurmaAberturaVOs().set(index, obj);
    // return;
    // } else {
    //
    // }
    // index++;
    // }
    // getTurmaAberturaVOs().add(obj);
    // }
	public void adicionarObjTurmaAgrupadaVOs(TurmaAgrupadaVO obj) throws Exception {
		TurmaAgrupadaVO.validarDados(obj);
		int index = 0;
		for (TurmaAgrupadaVO objExistente : getTurmaAgrupadaVOs()) {
			if (!objExistente.getTurma().getCurso().getPeriodicidade().equals(obj.getTurma().getCurso().getPeriodicidade())) {
				throw new AdicionarEmTurmaAgrupadaException("Devem ser selecionado turmas cuja periodicidade do curso seja a mesma.");
			}
			if (objExistente.getTurma().getCodigo().equals(obj.getTurma().getCodigo())) {
				getTurmaAgrupadaVOs().set(index, obj);
				return;
			}
			index++;
		}
		if (obj.getTurma().getTurmaAgrupada()) {
			throw new AdicionarEmTurmaAgrupadaException("Não é possivel adicionar uma turma agrupada dentro de outra!");
		} else {
			setSemestral(obj.getTurma().getSemestral());
			setAnual(obj.getTurma().getAnual());
			getTurmaAgrupadaVOs().add(obj);
		}
	}

    public void excluirObjTurmaVOs(Integer codigo) throws Exception {
        int index = 0;
        Iterator i = getTurmaAgrupadaVOs().iterator();
        while (i.hasNext()) {
            TurmaAgrupadaVO objExistente = (TurmaAgrupadaVO) i.next();
            if (objExistente.getCodigo().equals(codigo)) {
                getTurmaAgrupadaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>MatriculaPeriodoVO</code> no List <code>matriculaPeriodoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() -
     * como identificador (key) do objeto no List.
     * 
     * @param periodoLetivoMatricula
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjTurmaDisciplinaVOs(Integer disciplina) throws Exception {
        int index = 0;
        Iterator i = getTurmaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            TurmaDisciplinaVO objExistente = (TurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
                getTurmaDisciplinaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    // public void excluirObjTurmaAberturaVOs(Integer disciplina) throws
    // Exception {
    // int index = 0;
    // Iterator i = getTurmaAberturaVOs().iterator();
    // while (i.hasNext()) {
    // TurmaAberturaVO objExistente = (TurmaAberturaVO) i.next();
    // if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
    // getTurmaDisciplinaVOs().remove(index);
    // return;
    // }
    // index++;
    // }
    // }
    public void excluirObjTurmaAgrupadaVOs(Integer turma) throws Exception {
        int index = 0;
        Iterator i = getTurmaAgrupadaVOs().iterator();
        while (i.hasNext()) {
            TurmaAgrupadaVO objExistente = (TurmaAgrupadaVO) i.next();
            if (objExistente.getTurma().getCodigo().equals(turma)) {
                getTurmaAgrupadaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>MatriculaPeriodoVO</code> no List <code>matriculaPeriodoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>MatriculaPeriodo</code> - getPeriodoLetivoMatricula().getCodigo() -
     * como identificador (key) do objeto no List.
     * 
     * @param periodoLetivoMatricula
     *            Parâmetro para localizar o objeto do List.
     */
    public TurmaDisciplinaVO consultarObjTurmaDisciplinaVO(Integer disciplina) throws Exception {
        Iterator i = getTurmaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            TurmaDisciplinaVO objExistente = (TurmaDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
                return objExistente;
            }
        }
        return null;
    }
    
    public Integer getNrVagasDisponiveis(Integer disciplina) {
        Iterator i = this.getTurmaDisciplinaVOs().iterator();
        while (i.hasNext()) {
            TurmaDisciplinaVO turmaDisciplinaVO = (TurmaDisciplinaVO) i.next();
            if (turmaDisciplinaVO.getDisciplina().getCodigo().equals(disciplina)) {
                return turmaDisciplinaVO.getNrVagasMatricula();
            }
        }
        return 0;
    }

    public List<TurmaDisciplinaVO> getTurmaDisciplinaVOs() {
        if (turmaDisciplinaVOs == null) {
            turmaDisciplinaVOs = new ArrayList<>();
        }
        return turmaDisciplinaVOs;
    }

    public void setTurmaDisciplinaVOs(List<TurmaDisciplinaVO> turmaDisciplinaVOs) {
        this.turmaDisciplinaVOs = turmaDisciplinaVOs;
    }

    public List<TurmaAberturaVO> getTurmaAberturaVOs() {
        if (turmaAberturaVOs == null) {
            turmaAberturaVOs = new ArrayList<>();
        }
        return turmaAberturaVOs;
    }

    public void setTurmaAberturaVOs(List<TurmaAberturaVO> turmaAberturaVOs) {
        this.turmaAberturaVOs = turmaAberturaVOs;
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (
     * <code>Turma</code>).
     */
    @XmlElement(name = "turno")
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (
     * <code>Turma</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>Turma</code>).
     */
    @XmlElement(name = "unidadeEnsino")
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>Turma</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    @XmlElement(name = "curso")
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public GradeCurricularVO getGradeCurricularVO() {
        if (gradeCurricularVO == null) {
            gradeCurricularVO = new GradeCurricularVO();
        }
        return gradeCurricularVO;
    }

    public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
        this.gradeCurricularVO = gradeCurricularVO;
    }

    public PeriodoLetivoVO getPeridoLetivo() {
        if (peridoLetivo == null) {
            peridoLetivo = new PeriodoLetivoVO();
        }
        return peridoLetivo;
    }

    public void setPeridoLetivo(PeriodoLetivoVO peridoLetivo) {
        this.peridoLetivo = peridoLetivo;
    }

    /**
     * Retorna o objeto da classe <code>GradeCurricular</code> relacionado com (
     * <code>Turma</code>).
     */
    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (getSituacao().equals("FE")) {
            return "Fechada";
        }
        if (getSituacao().equals("AB")) {
            return "Aberta";
        }
        return (getSituacao());
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Integer getNrMinimoMatricula() {
    	if(nrMinimoMatricula == null){
    		nrMinimoMatricula = 0;
    	}
        return (nrMinimoMatricula);
    }

    public void setNrMinimoMatricula(Integer nrMinimoMatricula) {
        this.nrMinimoMatricula = nrMinimoMatricula;
    }

    public Integer getNrMaximoMatricula() {
        if (nrMaximoMatricula == null) {
            nrMaximoMatricula = 0;
        }
        return (nrMaximoMatricula);
    }

    public void setNrMaximoMatricula(Integer nrMaximoMatricula) {
        this.nrMaximoMatricula = nrMaximoMatricula;
    }

    public Integer getNrVagas() {
        if (nrVagas == null) {
            nrVagas = 0;
        }
        return (nrVagas);
    }

    public void setNrVagas(Integer nrVagas) {
        this.nrVagas = nrVagas;
    }

    @XmlElement(name = "identificador")
    public String getIdentificadorTurma() {
        if (identificadorTurma == null) {
            identificadorTurma = "";
        }
        return (identificadorTurma);
    }

    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
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

    public String getSala() {
        if (sala == null) {
            sala = "";
        }
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Integer getGradeCurricularAtiva() {
        if (gradeCurricularAtiva == null) {
            gradeCurricularAtiva = 0;
        }
        return gradeCurricularAtiva;
    }

    public void setGradeCurricularAtiva(Integer gradeCurricularAtiva) {
        this.gradeCurricularAtiva = gradeCurricularAtiva;
    }

    public Boolean getTurmaAgrupada() {
        if (turmaAgrupada == null) {
            turmaAgrupada = Boolean.FALSE;
        }
        return turmaAgrupada;
    }

    public void setTurmaAgrupada(Boolean turmaAgrupada) {
        this.turmaAgrupada = turmaAgrupada;
    }

    public List<TurmaAgrupadaVO> getTurmaAgrupadaVOs() {
        if (turmaAgrupadaVOs == null) {
            turmaAgrupadaVOs = new ArrayList<TurmaAgrupadaVO>(0);
        }
        return turmaAgrupadaVOs;
    }

    public void setTurmaAgrupadaVOs(List<TurmaAgrupadaVO> turmaAgrupadaVOs) {
        this.turmaAgrupadaVOs = turmaAgrupadaVOs;
    }

    @XmlElement(name = "anual")
    public Boolean getAnual() {
        if (anual == null) {
            anual = Boolean.FALSE;
        }
        return anual;
    }

    public void setAnual(Boolean anual) {
        this.anual = anual;
    }

    // public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
    // if (configuracaoAcademicoVO == null) {
    // configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
    // }
    // return configuracaoAcademicoVO;
    // }
    //
    // public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO
    // configuracaoAcademicoVO) {
    // this.configuracaoAcademicoVO = configuracaoAcademicoVO;
    // }
    @XmlElement(name = "semestral")
    public Boolean getSemestral() {
        if (semestral == null) {
            semestral = Boolean.FALSE;
        }
        return semestral;
    }

    public void setSemestral(Boolean semestral) {
        this.semestral = semestral;
    }

    /**
     * @return the planoFinanceiroCurso
     */
    public PlanoFinanceiroCursoVO getPlanoFinanceiroCurso() {
        if (planoFinanceiroCurso == null) {
            planoFinanceiroCurso = new PlanoFinanceiroCursoVO();
        }
        return planoFinanceiroCurso;
    }

    /**
     * @param planoFinanceiroCurso
     *            the planoFinanceiroCurso to set
     */
    public void setPlanoFinanceiroCurso(PlanoFinanceiroCursoVO planoFinanceiroCurso) {
        this.planoFinanceiroCurso = planoFinanceiroCurso;
    }

//    public Boolean getTurmaPreMatricula() {
//        if (turmaPreMatricula == null) {
//            turmaPreMatricula = Boolean.FALSE;
//        }
//        return turmaPreMatricula;
//    }
//
//    public void setTurmaPreMatricula(Boolean turmaPreMatricula) {
//        this.turmaPreMatricula = turmaPreMatricula;
//    }

    /**
     * Caso o curso seja Integral (padrão utilizado geralmente na pós-graduação,
     * não existe a necessidade de se controlar os registros de aula dentro de
     * um período de tempo delimitado, como um semestre ou um ano. Por exemplo,
     * em uma pós-graduação uma turma começa, tem todas as aulas (mesmo que se
     * leve dois anos) sequenciais até que seja finalizado.
     * 
     * @return
     */
    public boolean getIsUtilizaControleRegistroDeAulaBaseadoEmAnoOuSemestre() {
        if (this.getIntegral()) {
            return false;
        } else {
            if (this.getSemestral() || this.getAnual()) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return "Turma: " + this.getCodigo() + " Identificador: " + this.getIdentificadorTurma();
    }

    /**
     * @return the dataBaseGeracaoParcelas
     */
    public Date getDataBaseGeracaoParcelas() {
        // if (dataBaseGeracaoParcelas == null) {
        // dataBaseGeracaoParcelas = new Date();
        // }
        return dataBaseGeracaoParcelas;
    }

    /**
     * @param dataBaseGeracaoParcelas
     *            the dataBaseGeracaoParcelas to set
     */
    public void setDataBaseGeracaoParcelas(Date dataBaseGeracaoParcelas) {
        this.dataBaseGeracaoParcelas = dataBaseGeracaoParcelas;
    }

	public GrupoDestinatariosVO getGrupoDestinatarios() {
        if (grupoDestinatarios == null) {
            grupoDestinatarios = new GrupoDestinatariosVO();
        }
        return grupoDestinatarios;
    }

    public void setGrupoDestinatarios(GrupoDestinatariosVO grupoDestinatarios) {
        this.grupoDestinatarios = grupoDestinatarios;
    }

    public ChancelaVO getChancelaVO() {
        if (chancelaVO == null) {
            chancelaVO = new ChancelaVO();
        }
        return chancelaVO;
    }

    public void setChancelaVO(ChancelaVO chancelaVO) {
        this.chancelaVO = chancelaVO;
    }

    public String getTipoChancela() {
        if (tipoChancela == null) {
            tipoChancela = "";
        }
        return tipoChancela;
    }

    public void setTipoChancela(String tipoChancela) {
        this.tipoChancela = tipoChancela;
    }

    public Double getPorcentagemChancela() {
        if (porcentagemChancela == null) {
            porcentagemChancela = 0.0;
        }
        return porcentagemChancela;
    }

    public void setPorcentagemChancela(Double porcentagemChancela) {
        this.porcentagemChancela = porcentagemChancela;
    }

    public Double getValorFixoChancela() {
        if (valorFixoChancela == null) {
            valorFixoChancela = 0.0;
        }
        return valorFixoChancela;
    }

    public void setValorFixoChancela(Double valorFixoChancela) {
        this.valorFixoChancela = valorFixoChancela;
    }

    public Boolean getValorPorAluno() {
        if (valorPorAluno == null) {
            valorPorAluno = Boolean.FALSE;
        }
        return valorPorAluno;
    }

    public void setValorPorAluno(Boolean valorPorAluno) {
        this.valorPorAluno = valorPorAluno;
    }

    public void setContaCorrente(ContaCorrenteVO contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return contaCorrente;
    }

    /**
     * @return the qtdAlunosEstimado
     */
    public Integer getQtdAlunosEstimado() {
        if (qtdAlunosEstimado == null) {
            qtdAlunosEstimado = 0;
        }
        return qtdAlunosEstimado;
    }

    /**
     * @param qtdAlunosEstimado
     *            the qtdAlunosEstimado to set
     */
    public void setQtdAlunosEstimado(Integer qtdAlunosEstimado) {
        this.qtdAlunosEstimado = qtdAlunosEstimado;
    }

    /**
     * @return the custoMedioAluno
     */
    public Double getCustoMedioAluno() {
        if (custoMedioAluno == null) {
            custoMedioAluno = 0.0;
        }
        return custoMedioAluno;
    }

    /**
     * @param custoMedioAluno
     *            the custoMedioAluno to set
     */
    public void setCustoMedioAluno(Double custoMedioAluno) {
        this.custoMedioAluno = custoMedioAluno;
    }

    /**
     * @return the receitaMediaAluno
     */
    public Double getReceitaMediaAluno() {
        if (receitaMediaAluno == null) {
            receitaMediaAluno = 0.0;
        }
        return receitaMediaAluno;
    }

    /**
     * @param receitaMediaAluno
     *            the receitaMediaAluno to set
     */
    public void setReceitaMediaAluno(Double receitaMediaAluno) {
        this.receitaMediaAluno = receitaMediaAluno;
    }

    public TurmaVO getClone() throws Exception {
        TurmaVO obj = (TurmaVO) super.clone();
        return obj;
    }

    public boolean getIsPossuiCodigoTurmaAgrupada(Integer codigoTurma) {
        if (getTurmaAgrupada()) {
            for (TurmaAgrupadaVO obj : getTurmaAgrupadaVOs()) {
                if (codigoTurma.equals(obj.getTurma().getCodigo())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the utilizarDadosMatrizBoleto
     */
    public Boolean getUtilizarDadosMatrizBoleto() {
        if (utilizarDadosMatrizBoleto == null) {
            utilizarDadosMatrizBoleto = Boolean.FALSE;
        }
        return utilizarDadosMatrizBoleto;
    }

    /**
     * @param utilizarDadosMatrizBoleto
     *            the utilizarDadosMatrizBoleto to set
     */
    public void setUtilizarDadosMatrizBoleto(Boolean utilizarDadosMatrizBoleto) {
        this.utilizarDadosMatrizBoleto = utilizarDadosMatrizBoleto;
    }

    /**
     * @return the dataPrevisaoFinalizacao
     */
    public Date getDataPrevisaoFinalizacao() {
        return dataPrevisaoFinalizacao;
    }

    /**
     * @param dataPrevisaoFinalizacao
     *            the dataPrevisaoFinalizacao to set
     */
    public void setDataPrevisaoFinalizacao(Date dataPrevisaoFinalizacao) {
        this.dataPrevisaoFinalizacao = dataPrevisaoFinalizacao;
    }

    /**
     * @return the dataPrimeiraAulaProgramada
     */
    public String getDataPrimeiraAulaProgramada() {
        if (dataPrimeiraAulaProgramada == null) {
            dataPrimeiraAulaProgramada = "";
        }
        return dataPrimeiraAulaProgramada;
    }

    /**
     * @param dataPrimeiraAulaProgramada
     *            the dataPrimeiraAulaProgramada to set
     */
    public void setDataPrimeiraAulaProgramada(String dataPrimeiraAulaProgramada) {
        this.dataPrimeiraAulaProgramada = dataPrimeiraAulaProgramada;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    /**
     * @param observacao
     *            the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getTurmaSelecionada() {
        if (turmaSelecionada == null) {
            turmaSelecionada = Boolean.FALSE;
        }
        return turmaSelecionada;
    }

    public void setTurmaSelecionada(Boolean turmaSelecionada) {
        this.turmaSelecionada = turmaSelecionada;
    }

    public String getTipoAlunoFiltro() {
        if (tipoAlunoFiltro == null) {
            tipoAlunoFiltro = "normal";
        }
        return tipoAlunoFiltro;
    }

    public void setTipoAlunoFiltro(String tipoAlunoFiltro) {
        this.tipoAlunoFiltro = tipoAlunoFiltro;
    }

    @XmlElement(name = "subturma")
    public Boolean getSubturma() {
        if (subturma == null) {
            subturma = false;
        }
        return subturma;
    }

    public void setSubturma(Boolean subturma) {
        this.subturma = subturma;
    }

    public Integer getTurmaPrincipal() {
        if (turmaPrincipal == null) {
            turmaPrincipal = 0;
        }
        return turmaPrincipal;
    }

    public void setTurmaPrincipal(Integer turmaPrincipal) {
        this.turmaPrincipal = turmaPrincipal;
    }

    public Integer getQtdeAlunosSubturma() {
        if (qtdeAlunosSubturma == null) {
            qtdeAlunosSubturma = 0;
        }
        return qtdeAlunosSubturma;
    }

    public void setQtdeAlunosSubturma(Integer qtdeAlunosSubturma) {
        this.qtdeAlunosSubturma = qtdeAlunosSubturma;
    }

    public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaVOs() {
        if (matriculaPeriodoTurmaDisciplinaVOs == null) {
            matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
        }
        return matriculaPeriodoTurmaDisciplinaVOs;
    }

    public void setMatriculaPeriodoTurmaDisciplinaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
        this.matriculaPeriodoTurmaDisciplinaVOs = matriculaPeriodoTurmaDisciplinaVOs;
    }

    public Integer getQtdeAlunosDistribuir() {
        if (qtdeAlunosDistribuir == null) {
            qtdeAlunosDistribuir = 0;
        }
        return qtdeAlunosDistribuir;
    }

    public void setQtdeAlunosDistribuir(Integer qtdeAlunosDistribuir) {
        this.qtdeAlunosDistribuir = qtdeAlunosDistribuir;
    }

    /**
     * Método responsável por calcular o total de carga horaria / créditos
     * das disciplinas regulares e optativas já adicionadas para a turma.
     * Este método também leva em consideração as optativas adicionadas por meio
     * de Grupo de Optativas - que pode ser programado (definido) no período letivo.
     */
    public void atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares() {
        totalCargaHorariaDisciplinasRegulares = 0;
        totalCreditosDisciplinasRegulares = 0;
        totalCargaHorariaDisciplinasOptativas = 0;
        totalCreditosDisciplinasOptativas = 0;
        Iterator i = this.getTurmaDisciplinaVOs().iterator();
        if (this.getTurmaAgrupada()) {
        	totalCargaHorariaDisciplinasRegulares = turmaDisciplinaVOs.stream().map(td -> td.getGradeDisciplinaVO().getCargaHoraria()).reduce(0, Integer::sum);
        	totalCreditosDisciplinasRegulares = turmaDisciplinaVOs.stream().map(td -> td.getGradeDisciplinaVO().getNrCreditos()).reduce(0, Integer::sum);
        	return ;
        }
        while (i.hasNext()) {
            TurmaDisciplinaVO turmaDisciplinaVO = (TurmaDisciplinaVO) i.next();
            DisciplinaVO disciplinaVO = turmaDisciplinaVO.getDisciplina();
            boolean disciplinaEstaPresenteNaGradeDisciplina = false;
            for (GradeDisciplinaVO gradeDisciplinaVO : this.getPeridoLetivo().getGradeDisciplinaVOs()) {
                if (gradeDisciplinaVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
                    disciplinaEstaPresenteNaGradeDisciplina = true;
                    if (gradeDisciplinaVO.getTipoDisciplina().equals("OP")
                            || gradeDisciplinaVO.getTipoDisciplina().equals("LO")) {
                        // se entrar aqui é por que a disciplina é optativa 
                        // do tipo lançado na própria grade. Quando a optativa
                        // não está sendo controlada por meio do grupo de optativa
                        totalCargaHorariaDisciplinasOptativas = totalCargaHorariaDisciplinasOptativas + gradeDisciplinaVO.getCargaHoraria();
                        totalCreditosDisciplinasOptativas = totalCreditosDisciplinasOptativas + gradeDisciplinaVO.getNrCreditos();
                    } else {
                        // se entrar aqui é por que a disciplina é regular.
                        totalCargaHorariaDisciplinasRegulares = totalCargaHorariaDisciplinasRegulares + gradeDisciplinaVO.getCargaHoraria();
                        totalCreditosDisciplinasRegulares = totalCreditosDisciplinasRegulares + gradeDisciplinaVO.getNrCreditos();
                    }
                }
            }
            if (!disciplinaEstaPresenteNaGradeDisciplina) {
                // Se a disciplina não está presente na gradeDisciplina significa 
                // que a mesma deve ser oriunda do Grupo de Optativas do Periodo
                // Letivo. Logo temos que varrer o grupo de optativas para 
                // processar a carga horaria / créditos da mesma
                for (GradeCurricularGrupoOptativaDisciplinaVO gradeDisciplinaOptVO : this.getPeridoLetivo().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
                    if (gradeDisciplinaOptVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
                        totalCargaHorariaDisciplinasOptativas = totalCargaHorariaDisciplinasOptativas + gradeDisciplinaOptVO.getCargaHoraria();
                        totalCreditosDisciplinasOptativas = totalCreditosDisciplinasOptativas + gradeDisciplinaOptVO.getNrCreditos();
                    }
                }
            }
        }
    }

    public Integer getTotalCargaHorariaDisciplinasOptativas() {
        if (totalCargaHorariaDisciplinasOptativas == null) {
            atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
        }
        return totalCargaHorariaDisciplinasOptativas;
    }

    public Integer getTotalCreditosDisciplinasOptativas() {
        if (totalCreditosDisciplinasOptativas == null) {
            atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
        }
        return totalCreditosDisciplinasOptativas;
    }

    public Integer getTotalCargaHorariaDisciplinasRegulares() {
        if (totalCargaHorariaDisciplinasRegulares == null) {
            atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
        }
        return totalCargaHorariaDisciplinasRegulares;
    }

    public Integer getTotalCreditosDisciplinasRegulares() {
        if (totalCreditosDisciplinasRegulares == null) {
            atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
        }
        return totalCreditosDisciplinasRegulares;
    }

	public Boolean getUtilizarSubturmaNaDistribuicao() {
		if (utilizarSubturmaNaDistribuicao == null) {
			utilizarSubturmaNaDistribuicao = true;
		}
		return utilizarSubturmaNaDistribuicao;
	}

	public void setUtilizarSubturmaNaDistribuicao(Boolean utilizarSubturmaNaDistribuicao) {
		this.utilizarSubturmaNaDistribuicao = utilizarSubturmaNaDistribuicao;
	}

	public List<TurmaDisciplinaInclusaoSugeridaVO> getTurmaDisciplinaInclusaoSugeridaVOs() {
		if (turmaDisciplinaInclusaoSugeridaVOs == null) {
			turmaDisciplinaInclusaoSugeridaVOs = new ArrayList<TurmaDisciplinaInclusaoSugeridaVO>(0);
		}
		return turmaDisciplinaInclusaoSugeridaVOs;
	}

	public void setTurmaDisciplinaInclusaoSugeridaVOs(List<TurmaDisciplinaInclusaoSugeridaVO> turmaDisciplinaInclusaoSugeridaVOs) {
		this.turmaDisciplinaInclusaoSugeridaVOs = turmaDisciplinaInclusaoSugeridaVOs;
	}
	
	/**
	 * @author Victor Hugo 22/10/2014
	 */
	private ConfiguracaoEADVO configuracaoEADVO;
	private AvaliacaoOnlineVO avaliacaoOnlineVO;

	public ConfiguracaoEADVO getConfiguracaoEADVO() {
		if (configuracaoEADVO == null) {
			configuracaoEADVO = new ConfiguracaoEADVO();
		}
		return configuracaoEADVO;
	}

	public void setConfiguracaoEADVO(ConfiguracaoEADVO configuracaoEADVO) {
		this.configuracaoEADVO = configuracaoEADVO;
	}

	public AvaliacaoOnlineVO getAvaliacaoOnlineVO() {
		if (avaliacaoOnlineVO == null) {
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
		}
		return avaliacaoOnlineVO;
	}

	public void setAvaliacaoOnlineVO(AvaliacaoOnlineVO avaliacaoOnlineVO) {
		this.avaliacaoOnlineVO = avaliacaoOnlineVO;
	}
	
	private ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO;

	public ConfiguracaoNotaFiscalVO getConfiguracaoNotaFiscalVO() {
		if(configuracaoNotaFiscalVO == null) {
			configuracaoNotaFiscalVO = new ConfiguracaoNotaFiscalVO();
		}
		return configuracaoNotaFiscalVO;
	}

	public void setConfiguracaoNotaFiscalVO(ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		this.configuracaoNotaFiscalVO = configuracaoNotaFiscalVO;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TurmaVO other = (TurmaVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	/**
	 * Utilizado quando a turma for uma subturma
	 */
	@XmlElement(name = "tipoSubTurma")
	public TipoSubTurmaEnum getTipoSubTurma() {
		if (tipoSubTurma == null) {
			tipoSubTurma = TipoSubTurmaEnum.GERAL;
		}
		return tipoSubTurma;
	}

	/**
	 * Utilizado quando a turma for uma subturma
	 */
	public void setTipoSubTurma(TipoSubTurmaEnum tipoSubTurma) {
		this.tipoSubTurma = tipoSubTurma;
	}
	
	
	public Boolean getApresentarRenovacaoOnline() {
		if (apresentarRenovacaoOnline == null) {
			apresentarRenovacaoOnline = Boolean.TRUE;
		}
		return apresentarRenovacaoOnline;
	}
	
	public void setApresentarRenovacaoOnline(Boolean apresentarRenovacaoOnline) {
		this.apresentarRenovacaoOnline = apresentarRenovacaoOnline;
	}

	public boolean isPossuiAulaProgramada() {
		return possuiAulaProgramada;
	}

	public void setPossuiAulaProgramada(boolean possuiAulaProgramada) {
		this.possuiAulaProgramada = possuiAulaProgramada;
	}

	public String getTurmaAgrupadaProgramacaoAula() {
		if (turmaAgrupadaProgramacaoAula == null) {
			turmaAgrupadaProgramacaoAula = "";
		}
		return turmaAgrupadaProgramacaoAula;
	}

	public void setTurmaAgrupadaProgramacaoAula(String turmaAgrupadaProgramacaoAula) {
		this.turmaAgrupadaProgramacaoAula = turmaAgrupadaProgramacaoAula;
	}

	/**
	 * @return the abreviaturaCurso
	 */
	public String getAbreviaturaCurso() {
		if (abreviaturaCurso == null) {
			abreviaturaCurso = "";
		}
		return abreviaturaCurso;
	}

	/**
	 * @param abreviaturaCurso the abreviaturaCurso to set
	 */
	public void setAbreviaturaCurso(String abreviaturaCurso) {
		this.abreviaturaCurso = abreviaturaCurso;
	}

	/**
	 * @return the identificadorTurmaPrincipal
	 */
	public String getIdentificadorTurmaPrincipal() {
		if (identificadorTurmaPrincipal == null) {
			identificadorTurmaPrincipal = "";
		}
		return identificadorTurmaPrincipal;
	}

	/**
	 * @param identificadorTurmaPrincipal the identificadorTurmaPrincipal to set
	 */
	public void setIdentificadorTurmaPrincipal(String identificadorTurmaPrincipal) {
		this.identificadorTurmaPrincipal = identificadorTurmaPrincipal;
	}

	public Boolean getDisciplinasAtualizadasAlteracaoMatrizCurricular() {
		if (disciplinasAtualizadasAlteracaoMatrizCurricular == null) {
			disciplinasAtualizadasAlteracaoMatrizCurricular = false;
		}
		return disciplinasAtualizadasAlteracaoMatrizCurricular;
	}

	public void setDisciplinasAtualizadasAlteracaoMatrizCurricular(Boolean disciplinasAtualizadasAlteracaoMatrizCurricular) {
		this.disciplinasAtualizadasAlteracaoMatrizCurricular = disciplinasAtualizadasAlteracaoMatrizCurricular;
	}
	
	public boolean isApresentarAnoSemestre() {
		return getAnual() || getSemestral() ? true : false;
	}

	public String getPeriodicidade() {
		if (getAnual()) {
			return "AN";
		} else if (getSemestral()) {
			return "SE";
		} 
		return "IN";
	}


	public String tipoTurmaApresentar;
	
	public String getTipoTurmaApresentar(){
		if(tipoTurmaApresentar == null){
		if(getSubturma() && getTurmaAgrupada()){
			tipoTurmaApresentar =  "Agrupada "+getTipoSubTurma().getValorApresentar();
		}else if(getSubturma() && !getTurmaAgrupada()){
			tipoTurmaApresentar = getTipoSubTurma().getValorApresentar();
		}else if(!getSubturma() && getTurmaAgrupada()){
			tipoTurmaApresentar = "Agrupada";
		}else{
			tipoTurmaApresentar =  "Turma Base";
		}
		}
		return tipoTurmaApresentar;
	} 
	
	public String aplicarRegraNomeCursoApresentarCombobox() {
		String nomeApresentar = null;
		if (getTurmaAgrupada()) {
			nomeApresentar = "";
		} else {
			nomeApresentar = getCurso().getNome();
		}
		if (!nomeApresentar.equals("")) {
			nomeApresentar += " - ";
		}
		return getIdentificadorTurma() + " : " + nomeApresentar + getTurno().getNome();
	}
	
	

	public CentroResultadoVO getCentroResultadoVO() {
		centroResultadoVO = Optional.ofNullable(centroResultadoVO).orElse(new CentroResultadoVO());
		return centroResultadoVO;
	}

	public void setCentroResultadoVO(CentroResultadoVO centroResultadoVO) {
		this.centroResultadoVO = centroResultadoVO;
	}

	public Integer getNrVagasInclusaoReposicao() {
		if (nrVagasInclusaoReposicao == null) {
			nrVagasInclusaoReposicao = 0;
		}
		return nrVagasInclusaoReposicao;
	}
	public void setNrVagasInclusaoReposicao(Integer nrVagasInclusaoReposicao) {
		this.nrVagasInclusaoReposicao = nrVagasInclusaoReposicao;
	}
	
	public boolean isExisteAlunosMatriculados(){
		return Uteis.isAtributoPreenchido(getQtdMatriculados());
	}
	
	public boolean isExisteAlunosReposicao(){
		return Uteis.isAtributoPreenchido(getQtdReposicao());
	}
	
	public Integer getQtdReposicao() {
		return getTurmaDisciplinaVOs().stream().mapToInt(p-> p.getQtdAlunosReposicao()).sum();
	}
	
	public boolean isExisteAlteracaoParaMatrizCurricular(){
		return getTurmaDisciplinaVOs().stream().anyMatch(p->!p.isAlterarMatrizCurricular());
	}

	public Integer getQtdMatriculados() {
		if (qtdMatriculados == null) {
			qtdMatriculados = 0;
		}
		return qtdMatriculados;
	}

	public void setQtdMatriculados(Integer qtdMatriculados) {
		this.qtdMatriculados = qtdMatriculados;
	}

	public Integer getQtdConfirmados() {
		if (qtdConfirmados == null) {
			qtdConfirmados = 0;
		}
		return qtdConfirmados;
	}

	public void setQtdConfirmados(Integer qtdConfirmados) {
		this.qtdConfirmados = qtdConfirmados;
	}

	public Integer getQtdPreMatriculados() {
		if (qtdPreMatriculados == null) {
			qtdPreMatriculados = 0;
		}
		return qtdPreMatriculados;
	}

	public void setQtdPreMatriculados(Integer qtdPreMatriculados) {
		this.qtdPreMatriculados = qtdPreMatriculados;
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

	public IndiceReajusteVO getIndiceReajusteVO() {
		if (IndiceReajusteVO == null) {
			IndiceReajusteVO = new IndiceReajusteVO();
		}
		return IndiceReajusteVO;
	}

	public void setIndiceReajusteVO(IndiceReajusteVO indiceReajusteVO) {
		IndiceReajusteVO = indiceReajusteVO;
	}

	public Timestamp getDataUltimaAlteracao() {
		if (dataUltimaAlteracao == null)
			dataUltimaAlteracao = Uteis.getDataJDBCTimestamp(new Date());
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Timestamp dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	@XmlElement(name = "dataPrimeiraAula")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataPrimeiraAula() {
		return dataPrimeiraAula;
	}

	public void setDataPrimeiraAula(Date dataPrimeiraAula) {
		this.dataPrimeiraAula = dataPrimeiraAula;
	}

	@XmlElement(name = "dataUltimaAula")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataUltimaAula() {
		return dataUltimaAula;
	}

	public void setDataUltimaAula(Date dataUltimaAula) {
		this.dataUltimaAula = dataUltimaAula;
	}

	@XmlElement(name = "salaLocalAulaVO")
	public SalaLocalAulaVO getSalaLocalAulaVO() {
		if (salaLocalAulaVO == null) {
			salaLocalAulaVO = new SalaLocalAulaVO();
		}
		return salaLocalAulaVO;
	}

	public void setSalaLocalAulaVO(SalaLocalAulaVO salaLocalAulaVO) {
		this.salaLocalAulaVO = salaLocalAulaVO;
	}

	@XmlElement(name = "professor")
	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public String getValorApresentarRequerimento() {
		return "Turma: "+getIdentificadorTurma()+" - Unidade: "+getUnidadeEnsino().getNome()
				+ (Uteis.isAtributoPreenchido(getDataPrimeiraAula()) && Uteis.isAtributoPreenchido(getDataUltimaAula()) ? "" : " - Período: "+Uteis.getData(getDataPrimeiraAula())+" à "+Uteis.getData(getDataUltimaAula()))
				+ (getSalaLocalAulaVO().getLocalAula().getLocal().trim().isEmpty()?"":" - Local: "+getSalaLocalAulaVO().getLocalAula().getLocal() )
				+ (getSalaLocalAulaVO().getSala().trim().isEmpty()?"":" - Sala: "+getSalaLocalAulaVO().getSala() )
				;
	}

	public List<TurmaContratoVO> getTurmaContratoVOs() {
		if(turmaContratoVOs == null) {
			turmaContratoVOs = new ArrayList<TurmaContratoVO>(0);
		}
		return turmaContratoVOs;
	}

	public void setTurmaContratoVOs(List<TurmaContratoVO> turmaContratoVOs) {
		this.turmaContratoVOs = turmaContratoVOs;
	}

	@Override
	public int compareTo(Object o) {
		TurmaVO turmaVO = (TurmaVO) o;
		return Integer.compare(this.codigo, turmaVO.codigo);
	}

	public Boolean getFiltrarTurmaVO() {
		if (filtrarTurmaVO == null) {
			filtrarTurmaVO = false;
		}
		return filtrarTurmaVO;
	}

	public void setFiltrarTurmaVO(Boolean filtrarTurmaVO) {
		this.filtrarTurmaVO = filtrarTurmaVO;
	}

	public List<TurmaAtualizacaoDisciplinaLogVO> getListaTurmaAtualizacaoDisciplinaLog() {
		if (listaTurmaAtualizacaoDisciplinaLog == null) {
			listaTurmaAtualizacaoDisciplinaLog = new ArrayList<>();
		}
		return listaTurmaAtualizacaoDisciplinaLog;
	}

	public void setListaTurmaAtualizacaoDisciplinaLog(List<TurmaAtualizacaoDisciplinaLogVO> listaTurmaAtualizacaoDisciplinaLog) {
		this.listaTurmaAtualizacaoDisciplinaLog = listaTurmaAtualizacaoDisciplinaLog;
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

	public Integer getCodigoTurnoApresentarCenso() {
		if (codigoTurnoApresentarCenso == null) {
			codigoTurnoApresentarCenso = 0;
		}
		return codigoTurnoApresentarCenso;
	}

	public void setCodigoTurnoApresentarCenso(Integer codigoTurnoApresentarCenso) {
		this.codigoTurnoApresentarCenso = codigoTurnoApresentarCenso;
	}

	public Boolean getConsiderarTurmaAvaliacaoInstitucional() {
		if (considerarTurmaAvaliacaoInstitucional == null) {
			considerarTurmaAvaliacaoInstitucional = Boolean.TRUE;
		}
		return considerarTurmaAvaliacaoInstitucional;
	}

	public void setConsiderarTurmaAvaliacaoInstitucional(Boolean considerarTurmaAvaliacaoInstitucional) {
		this.considerarTurmaAvaliacaoInstitucional = considerarTurmaAvaliacaoInstitucional;
	}
	

	public List<TurmaUnidadeEnsinoVO> getListaTurmaUnidadeEnsino() {
		if(listaTurmaUnidadeEnsino == null) {
			listaTurmaUnidadeEnsino = new ArrayList<TurmaUnidadeEnsinoVO>();
		}
		return listaTurmaUnidadeEnsino;
	}

	public void setListaTurmaUnidadeEnsino(List<TurmaUnidadeEnsinoVO> listaTurmaUnidadeEnsino) {
		this.listaTurmaUnidadeEnsino = listaTurmaUnidadeEnsino;
	}


	public VagaTurmaVO getVagaTurmaVO() {
		if(vagaTurmaVO == null) {
			vagaTurmaVO = new VagaTurmaVO();
		}
		return vagaTurmaVO;
	}

	public void setVagaTurmaVO(VagaTurmaVO vagaTurmaVO) {
		this.vagaTurmaVO = vagaTurmaVO;
	}

	public TurmaUnidadeEnsinoVO getTurmaUnidadeEnsino() {
		if(turmaUnidadeEnsino == null) {
			turmaUnidadeEnsino = new TurmaUnidadeEnsinoVO();
		}
		return turmaUnidadeEnsino;
	}

	public void setTurmaUnidadeEnsino(TurmaUnidadeEnsinoVO turmaUnidadeEnsino) {
		this.turmaUnidadeEnsino = turmaUnidadeEnsino;
	}

	public Boolean getValidarDadosTurmaAgrupada() {
		if(validarDadosTurmaAgrupada == null) {
			validarDadosTurmaAgrupada = true;
		}
		return validarDadosTurmaAgrupada;
	}

	public void setValidarDadosTurmaAgrupada(Boolean validarDadosTurmaAgrupada) {
		this.validarDadosTurmaAgrupada = validarDadosTurmaAgrupada;
	}

	public List<TurmaAberturaVO> getListaSituacaoAberturaTurmaBase() {
		if(listaSituacaoAberturaTurmaBase == null) {
			listaSituacaoAberturaTurmaBase = new ArrayList<TurmaAberturaVO>();
		}
		return listaSituacaoAberturaTurmaBase;
	}

	public void setListaSituacaoAberturaTurmaBase(List<TurmaAberturaVO> listaSituacaoAberturaTurmaBase) {
		this.listaSituacaoAberturaTurmaBase = listaSituacaoAberturaTurmaBase;
	}

	@XmlElement(name = "codigoTurmaBase")
	public Integer getCodigoTurmaBase() {
        if (codigoTurmaBase == null) {
        	codigoTurmaBase = 0;
        }
		return codigoTurmaBase;
	}

	public void setCodigoTurmaBase(Integer codigoTurmaBase) {
		this.codigoTurmaBase = codigoTurmaBase;
	}

	@XmlElement(name = "identificadorTurmaBase")
	public String getIdentificadorTurmaBase() {
        if (identificadorTurmaBase == null) {
        	identificadorTurmaBase = "";
        }
		return identificadorTurmaBase;
	}

	public void setIdentificadorTurmaBase(String identificadorTurmaBase) {
		this.identificadorTurmaBase = identificadorTurmaBase;
	}

	
	
	
}