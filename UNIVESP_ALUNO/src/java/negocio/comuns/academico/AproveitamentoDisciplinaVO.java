package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade AproveitamentoDisciplina. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class AproveitamentoDisciplinaVO extends SuperVO {

	private static final long serialVersionUID = 8369667751413486705L;

	private Integer codigo;
	private Date data;
	private RequerimentoVO codigoRequerimento;
	private Boolean aproveitamentoPrevisto;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Matricula </code>.
	 */
	private MatriculaVO matricula;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>AproveitamentoDisciplinaDisciplinasAproveitadas</code>.
	 */
	private List<DisciplinasAproveitadasVO> disciplinasAproveitadasVOs;
//	private List<ConcessaoCreditoDisciplinaVO> concessaoCreditoDisciplinaVOs;
//	private List<ConcessaoCargaHorariaDisciplinaVO> concessaoCargaHorariaDisciplinaVOs;
        
    /**
     * TRANSIENTE usado para contralar quais disciplinas foram aprovietadas fora da
     * grade, facilitando a gestão no momento de persistir o AproveitamentoDisciplinaVO
     */
    private List<MapaEquivalenciaDisciplinaCursadaVO> disciplinasAproveitadasForaDaGradeMapaEquivalencia;
                
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Pessoa </code>.
	 */
	private UsuarioVO responsavelAutorizacao;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Curso </code>.
	 */
	private CursoVO curso;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Turno </code>.
	 */
	private TurnoVO turno;
	// private TurmaVO turma;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>gradeCurricular </code>.
	 */
	private GradeCurricularVO gradeCurricular;
        
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>matriculaPeriodo </code>.
	 */
	private UnidadeEnsinoVO unidadeEnsino;
	private PeriodoLetivoVO peridoLetivo;
	private PessoaVO pessoa;
	private Integer unidadeEnsinoCurso;
	private MatriculaPeriodoVO matriculaPeriodo;
	private Boolean matriculado;
	private String turma;
	private String tipo;
	private Boolean disciplinaForaGrade;
	private String instituicao;
	private CidadeVO cidadeVO;

	/**
	 * Construtor padrão da classe <code>AproveitamentoDisciplina</code>. Cria
	 * uma nova instância desta entidade, inicializando automaticamente seus
	 * atributos (Classe VO).
	 */
	public AproveitamentoDisciplinaVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>AproveitamentoDisciplinaVO</code>. Todos os tipos de consistência
	 * de dados são e devem ser implementadas neste método. São validações
	 * típicas: verificação de campos obrigatórios, verificação de valores
	 * válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(AproveitamentoDisciplinaVO obj, ConfiguracaoGeralSistemaVO c) throws ConsistirException {
		if (!c.getPermiteAproveitamentoDiscSemRequerimento()) {
			if (!Uteis.isAtributoPreenchido(obj.getCodigoRequerimento())) {
				throw new ConsistirException("O campo REQUERIMENTO  deve ser informado.");
			}
			if (obj.getCodigoRequerimento().getSituacao().equals("AP")) {
				throw new ConsistirException("Requerimento especificado está aguardando pagamento.");
			}
		}
		if (obj.getData() == null) {
			throw new ConsistirException("O campo DATA deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getResponsavelAutorizacao())) {
			throw new ConsistirException("O campo RESPONSÁVEL AUTORIZAÇÃO deve ser informado.");
		}
		if (obj.getDisciplinasAproveitadasVOs().isEmpty()) {
			throw new ConsistirException("Deve ser selecionado ao menos uma disciplina (Aba Disciplinas Aproveitadas) para o aproveitamento.");
		}
	}

	public static void validarDadosParaMatricula(AproveitamentoDisciplinaVO obj, ConfiguracaoGeralSistemaVO c) throws ConsistirException {
		validarDados(obj, c);
		if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo ALUNO deve ser informado.");
		}
		if (obj.getCurso() == null || obj.getCurso().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CURSO deve ser informado.");
		}
		if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo TURNO deve ser informado.");
		}
		if (obj.getGradeCurricular() == null || obj.getGradeCurricular().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo GRADE CURRICULAR deve ser informado.");
		}
		if (obj.getPeridoLetivo() == null || obj.getPeridoLetivo().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo PERIODO LETIVO deve ser informado.");
		}
		// if (obj.getTurma() == null || obj.getTurma().getCodigo().intValue()
		// == 0) {
		// throw new ConsistirException("O campo TURMA deve ser informado.");
		// }
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>DisciplinasAproveitadasVO</code> ao List
	 * <code>DisciplinasAproveitadasVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>DisciplinasAproveitadas</code> - getDisciplina()
	 * - como identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>DisciplinasAproveitadasVO</code> que
	 *            será adiocionado ao Hashtable correspondente.
	 */

	public void adicionarObjDisciplinasAproveitadasVOs(DisciplinasAproveitadasVO obj) throws Exception {
		DisciplinasAproveitadasVO.validarDados(obj, getCurso().getPeriodicidade());
                obj.setAproveitamentoDisciplinaVO(this); //referencia ao objeto do aproveitamento, que será importante para definir os dados do histórico do mesmo.
		int index = 0;
		Iterator<DisciplinasAproveitadasVO> i = getDisciplinasAproveitadasVOs().iterator();
		while (i.hasNext()) {
			DisciplinasAproveitadasVO objExistente = i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				getDisciplinasAproveitadasVOs().set(index, obj);
				return;
			}
			index++;
		}
		getDisciplinasAproveitadasVOs().add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>DisciplinasAproveitadasVO</code> no List
	 * <code>disciplinasAproveitadasVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>DisciplinasAproveitadas</code> - getDisciplina()
	 * - como identificador (key) do objeto no List.
	 * 
	 * @param disciplina
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjDisciplinasAproveitadasVOs(Integer disciplina) throws Exception {
		int index = 0;
		Iterator<DisciplinasAproveitadasVO> i = getDisciplinasAproveitadasVOs().iterator();
		while (i.hasNext()) {
			DisciplinasAproveitadasVO objExistente = i.next();
			if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
				getDisciplinasAproveitadasVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>DisciplinasAproveitadasVO</code> no List
	 * <code>disciplinasAproveitadasVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>DisciplinasAproveitadas</code> - getDisciplina()
	 * - como identificador (key) do objeto no List.
	 * 
	 * @param disciplina
	 *            Parâmetro para localizar o objeto do List.
	 */
	public DisciplinasAproveitadasVO consultarObjDisciplinasAproveitadasVO(Integer disciplina) throws Exception {
		Iterator<DisciplinasAproveitadasVO> i = getDisciplinasAproveitadasVOs().iterator();
		while (i.hasNext()) {
			DisciplinasAproveitadasVO objExistente = i.next();
			if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
				return objExistente;
			}
		}
		return null;
	}

	public Boolean getExisteMatricula() {
		if (!getMatricula().getMatricula().equals("")) {
			return true;
		}
		return false;
	}

	public UsuarioVO getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = new UsuarioVO();
		}
		return responsavelAutorizacao;
	}

	public void setResponsavelAutorizacao(UsuarioVO responsavelAutorizacao) {
		this.responsavelAutorizacao = responsavelAutorizacao;
	}

	/**
	 * Retorna o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>AproveitamentoDisciplina</code>).
	 */
	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return (matricula);
	}

	/**
	 * Define o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>AproveitamentoDisciplina</code>).
	 */
	public void setMatricula(MatriculaVO obj) {
		this.matricula = obj;
	}

	public Boolean getGradeCurricularDefina() {
		if (getGradeCurricular().getCodigo() != null && getGradeCurricular().getCodigo().intValue() > 0) {
			return true;
		}
		return false;
	}

	public Boolean getExisteRequerimento() {
		if (getCodigoRequerimento().getCodigo() != null && getCodigoRequerimento().getCodigo().intValue() > 0) {
			return true;
		}
		return false;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return (data);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getData_Apresentar() {
		return (Uteis.getData(getData()));
	}

	public void setData(Date data) {
		this.data = data;
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

	public RequerimentoVO getCodigoRequerimento() {
		if (codigoRequerimento == null) {
			codigoRequerimento = new RequerimentoVO();
		}
		return codigoRequerimento;
	}

	public void setCodigoRequerimento(RequerimentoVO codigoRequerimento) {
		this.codigoRequerimento = codigoRequerimento;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public TurnoVO getTurno() {
		if (turno == null) {
			turno = new TurnoVO();
		}
		return turno;
	}

	public void setTurno(TurnoVO turno) {
		this.turno = turno;
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

	// public TurmaVO getTurma() {
	// if (turma == null) {
	// turma = new TurmaVO();
	// }
	// return turma;
	// }
	//
	// public void setTurma(TurmaVO turma) {
	// this.turma = turma;
	// }
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
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>AproveitamentoDisciplinaDisciplinasAproveitadas</code>.
	 */
	public List<DisciplinasAproveitadasVO> getDisciplinasAproveitadasVOs() {
		if (disciplinasAproveitadasVOs == null) {
			disciplinasAproveitadasVOs = new ArrayList<DisciplinasAproveitadasVO>(0);
		}
		return (disciplinasAproveitadasVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>AproveitamentoDisciplinaDisciplinasAproveitadas</code>.
	 */
	public void setDisciplinasAproveitadasVOs(List<DisciplinasAproveitadasVO> disciplinasAproveitadasVOs) {
		this.disciplinasAproveitadasVOs = disciplinasAproveitadasVOs;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public Integer getUnidadeEnsinoCurso() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = 0;
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(Integer unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}

	/**
	 * @return the unidadeEnsino
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino
	 *            the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Boolean getMatriculado() {
		if (matriculado == null) {
			matriculado = false;
		}
		return matriculado;
	}

	public void setMatriculado(Boolean matriculado) {
		this.matriculado = matriculado;
	}

	public Boolean getIsPossuiRequerimento() {
		if (this.getCodigoRequerimento().getCodigo().intValue() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

//	public List<ConcessaoCreditoDisciplinaVO> getConcessaoCreditoDisciplinaVOs() {
//		if (concessaoCreditoDisciplinaVOs == null) {
//			concessaoCreditoDisciplinaVOs = new ArrayList<ConcessaoCreditoDisciplinaVO>(0);
//		}
//		return concessaoCreditoDisciplinaVOs;
//	}
//
//	public void setConcessaoCreditoDisciplinaVOs(List<ConcessaoCreditoDisciplinaVO> concessaoCreditoDisciplinaVOs) {
//		this.concessaoCreditoDisciplinaVOs = concessaoCreditoDisciplinaVOs;
//	}

	public String getTipo() {
		if (tipo == null) {
			tipo = "";
		}
		return tipo;
	}

	public String getTipo_Apresentar() {
		if (tipo == null) {
			tipo = "";
		}
		if (tipo.equals("AP")) {
			return "Aproveitamento de Disciplina";
		}
		if (tipo.equals("CO")) {
			return "Concessão de Crédito";
		}
		if (tipo.equals("CH")) {
			return "Concessão de Carga Horárias";
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

//	public List<ConcessaoCargaHorariaDisciplinaVO> getConcessaoCargaHorariaDisciplinaVOs() {
////		if (concessaoCargaHorariaDisciplinaVOs == null) {
////			concessaoCargaHorariaDisciplinaVOs = new ArrayList<ConcessaoCargaHorariaDisciplinaVO>(0);
////		}
//		return null; // concessaoCargaHorariaDisciplinaVOs;
//	}
//
//	public void setConcessaoCargaHorariaDisciplinaVOs(List<ConcessaoCargaHorariaDisciplinaVO> concessaoCargaHorariaDisciplinaVOs) {
////		this.concessaoCargaHorariaDisciplinaVOs = concessaoCargaHorariaDisciplinaVOs;
//	}

	public Boolean getDisciplinaForaGrade() {
		if (disciplinaForaGrade == null) {
			disciplinaForaGrade = Boolean.FALSE;
		}
		return disciplinaForaGrade;
	}

	public void setDisciplinaForaGrade(Boolean disciplinaForaGrade) {
		this.disciplinaForaGrade = disciplinaForaGrade;
	}

	public Boolean getAproveitamentoPrevisto() {
		if (aproveitamentoPrevisto == null) {
			aproveitamentoPrevisto = Boolean.FALSE;
		}
		return aproveitamentoPrevisto;
	}

	public void setAproveitamentoPrevisto(Boolean aproveitamentoPrevisto) {
		this.aproveitamentoPrevisto = aproveitamentoPrevisto;
	}

	public String getInstituicao() {
		if (instituicao == null) {
			instituicao = "";
		}
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public CidadeVO getCidadeVO() {
		if (cidadeVO == null) {
			cidadeVO = new CidadeVO();
		}
		return cidadeVO;
	}

	public void setCidadeVO(CidadeVO cidadeVO) {
		this.cidadeVO = cidadeVO;
	}

    /**
     * @return the disciplinasAproveitadasForaDaGradeMapaEquivalencia
     */
    public List<MapaEquivalenciaDisciplinaCursadaVO> getDisciplinasAproveitadasForaDaGradeMapaEquivalencia() {
        if (disciplinasAproveitadasForaDaGradeMapaEquivalencia == null) {
            disciplinasAproveitadasForaDaGradeMapaEquivalencia = new ArrayList<MapaEquivalenciaDisciplinaCursadaVO>();
        }
        return disciplinasAproveitadasForaDaGradeMapaEquivalencia;
    }

    /**
     * @param disciplinasAproveitadasForaDaGradeMapaEquivalencia the disciplinasAproveitadasForaDaGradeMapaEquivalencia to set
     */
    public void setDisciplinasAproveitadasForaDaGradeMapaEquivalencia(List<MapaEquivalenciaDisciplinaCursadaVO> disciplinasAproveitadasForaDaGradeMapaEquivalencia) {
        this.disciplinasAproveitadasForaDaGradeMapaEquivalencia = disciplinasAproveitadasForaDaGradeMapaEquivalencia;
    }
    
    
    public void adicionarDisciplinasAproveitadasForaDaGradeMapaEquivalencia(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVOAdicionar) {
        int pos = 0;
        while (pos < this.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().size()) {
            MapaEquivalenciaDisciplinaCursadaVO mapaExistente = this.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().get(pos);
            if ((mapaExistente.getDisciplinaVO().getCodigo().equals(mapaEquivalenciaDisciplinaCursadaVOAdicionar.getDisciplinaVO().getCodigo())) &&
                (mapaExistente.getCargaHoraria().equals(mapaEquivalenciaDisciplinaCursadaVOAdicionar.getCargaHoraria()))) {
                this.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().set(pos, mapaEquivalenciaDisciplinaCursadaVOAdicionar);
                return;
            }
            pos++;
        }
        this.getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().add(mapaEquivalenciaDisciplinaCursadaVOAdicionar);
    }
}
