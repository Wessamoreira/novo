package negocio.comuns.avaliacaoinst;

import negocio.comuns.academico.*;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.negocio.comuns.avaliacaoInst.enumeradores.NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum;
import webservice.DateAdapterMobile;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Reponsável por manter os dados da entidade AvaliacaoInstitucional. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "avaliacaoInstitucional")
public class AvaliacaoInstitucionalVO extends SuperVO {

	public static final long serialVersionUID = 1L;

	private Integer codigo;
	private Date data;
	private String nome;
	private String descricao;
	private String mensagem;
	private String publicoAlvo;
	private Date dataInicio;
	private Date dataFinal;
	private Boolean avaliacaoObrigatoria;
	private UnidadeEnsinoVO unidadeEnsino;
	private QuestionarioVO questionarioVO;
	private UsuarioVO responsavel;
	private Boolean questionarioRespondido;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private Boolean proximo;
	private List<MatriculaPeriodoTurmaDisciplinaVO> disciplinaQuestionarioVOs;
	private List<CursoVO> cursoVOs;
	private List<TurmaVO> turmaVOs;
	private List<RespostaAvaliacaoInstitucionalDWVO> respostaAvaliacaoInstitucionalDWVOs;
	private Boolean informarImportanciaPergunta;
	private String situacao;
	private Boolean avaliacaoPresencial;
	private String tipoFiltroProfessor;
	private CursoVO curso;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private String nivelEducacional;
	private String ano;
	private String semestre;
	private Boolean avaliacaoUltimoModulo;
	private String horaInicio;
	private Integer diasDisponivel;
	private String horaFim;
	private DepartamentoVO departamento;
	private CargoVO cargo;
	private DepartamentoVO departamentoAvaliado;
	private CargoVO cargoAvaliado;
	private List<AvaliacaoInstitucionalPessoaAvaliadaVO> listaAvaliacaoInstitucionalPessoaAvaliadaVOs;
	private Date dataInicioAula;
	private Date dataTerminoAula;
	private Boolean publicarResultadoRespondente;
	private Boolean publicarResultadoAluno;
	private Boolean publicarResultadoProfessor;
	private Boolean publicarResultadoCoordenador;
	private Date dataInicioPublicarResultado;
	private Date dataTerminoPublicarResultado;
	private Boolean notificarRespondentes;
	private Integer recorrenciaDiaNotificar;
	private NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamentoPublicarResultado;
	private Date dataUltimaNotificacao;
	private TipoCoordenadorCursoEnum tipoCoordenadorCurso;
	private List<AvaliacaoInstitucionalCursoVO> avaliacaoInstitucionalCursoVOs;
	private List<QuestionarioVO> questionarioVOs;
    private List<UnidadeEnsinoVO> unidadeEnsinoVOs;
	private MatriculaVO matriculaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
    private String nomeCurso;
    private String nomeUnidadeEnsino;

	private Boolean avaliarDisciplinasReposicao;
	private List<AvaliacaoInstitucionalUnidadeEnsinoVO> avaliacaoInstitucionalUnidadeEnsinoVOs;

	/**
	 * Construtor padrão da classe <code>AvaliacaoInstitucional</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public AvaliacaoInstitucionalVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>AvaliacaoInstitucionalVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para os
	 * atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(AvaliacaoInstitucionalVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getData() == null) {
			throw new ConsistirException("O campo DATA (Avaliação Institucional) deve ser informado.");
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Avaliação Institucional) deve ser informado.");
		}
		if (obj.getAvaliacaoInstitucionalUnidadeEnsinoVOs().isEmpty() && !obj.getAvaliacaoUltimoModulo()) {
			throw new ConsistirException("O campo UNIDADE ENSINO (Avaliação Institucional) deve ser informado.");
		}
		if (obj.getPublicoAlvo().equals("")) {
			throw new ConsistirException("O campo PÚBLICO ALVO (Avaliação Institucional) deve ser informado.");
		}
		if (obj.getIsApresentarDepartamento() && obj.getDepartamento().getCodigo() == 0) {
			throw new ConsistirException("O campo DEPARTAMENTO (Avaliação Institucional) deve ser informado.");
		}
		if (obj.getIsApresentarCargo() && obj.getCargo().getCodigo() == 0) {
			throw new ConsistirException("O campo CARGO (Avaliação Institucional) deve ser informado.");
		}
		if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo RESPONSÁVEL (Avaliação Institucional) deve ser informado.");
		}
		if (obj.getQuestionarioVO().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo QUESTIONÁRIO deve ser informado.");
		}
		if ((obj.getNivelEducacional() == null || obj.getNivelEducacional().equals("")
				|| obj.getNivelEducacional().equals("0")) && obj.getIsPermiteInformarNivelEducacional()) {
			throw new ConsistirException("O campo NÍVEL EDUCACIONAL (Dados Básicos) deve ser informado.");
		}
		if (obj.getIsPermiteInformarNivelEducacional() && !obj.getPublicoAlvo_CargoCoordenador()
				&& !obj.getPublicoAlvo_DepartamentoCoordenador()) {
			if (obj.getNivelEducacional().equals(TipoNivelEducacional.BASICO.getValor())
					|| obj.getNivelEducacional().equals(TipoNivelEducacional.INFANTIL.getValor())
					|| obj.getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor())
					|| obj.getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor())
					|| obj.getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())) {
				if (obj.getAno().trim().isEmpty()) {
					throw new ConsistirException("O campo ANO (Dados Básicos) deve ser informado.");
				}
				if (obj.getAno().trim().length() != 4) {
					throw new ConsistirException("O campo ANO (Dados Básicos) deve ser informado com 4 dígitos.");
				}
			}
			if (obj.getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor())
					|| obj.getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())) {
				if (obj.getSemestre().trim().isEmpty()) {
					throw new ConsistirException("O campo SEMESTRE (Dados Básicos) deve ser informado.");
				}
			}
		}
		if (obj.getIsApresentarPeriodoAula() && obj.getDataInicioAula() == null) {
			throw new ConsistirException(
					"O campo DATA INÍCIO AULA PROG. PROFESSOR (Dados Básicos) deve ser informado.");
		}
		if (obj.getIsApresentarPeriodoAula() && obj.getDataTerminoAula() == null) {
			throw new ConsistirException("O campo DATA FINA AULA PROG. PROFESSOR (Dados Básicos) deve ser informado.");
		}
		if (obj.getIsApresentarPeriodoAula() && (obj.getDataTerminoAula().before(obj.getDataInicioAula())
				|| Uteis.getData(obj.getDataTerminoAula()).equals(Uteis.getData(obj.getDataInicioAula())))) {
			throw new ConsistirException(
					"O campo DATA FINA AULA PROG. PROFESSOR deve ser maior que a DATA INÍCIO AULA PROG. PROFESSOR.");
		}
		if (obj.getPublicoAlvo_Curso() && obj.getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
			throw new ConsistirException("O campo CURSO (Avaliação Institucional) deve ser informado.");
		}
		if (obj.getPublicoAlvo_Turma() && obj.getTurma().getCodigo().equals(0)) {
			throw new ConsistirException("O campo TURMA (Avaliação Institucional) deve ser informado.");
		}

		if (obj.getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO.name())
				&& obj.getDepartamentoAvaliado().getCodigo() == 0) {
			throw new ConsistirException("O campo DEPARTAMENTO AVALIADO (Avaliação Institucional) deve ser informado.");
		}

		if (obj.getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO.name())
				&& obj.getDepartamentoAvaliado().getCodigo() == 0) {
			throw new ConsistirException("O campo DEPARTAMENTO AVALIADO (Avaliação Institucional) deve ser informado.");
		}

		if (obj.getPublicoAlvo_Professor()) {
			if (obj.getTipoFiltroProfessor().equals("turma") && obj.getTurma().getCodigo().equals(0)) {
				throw new ConsistirException("O campo TURMA (Avaliação Institucional) deve ser informado.");
			} else if (obj.getTipoFiltroProfessor().equals("curso")
					&& obj.getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
				throw new ConsistirException("O campo CURSO (Avaliação Institucional) deve ser informado.");
			}
		}
		if (obj.getAvaliacaoUltimoModulo().booleanValue()) {
			if (obj.getHoraInicio().equals("")) {
				throw new ConsistirException("O campo HORA INÍCIO (Avaliação Institucional) deve ser informado.");
			} else if (obj.getHoraInicio().length() < 5) {
				throw new ConsistirException(
						"O campo HORA INÍCIO (Avaliação Institucional) deve possuir o formato 00:00.");
			}
			if (obj.getHoraFim().equals("")) {
				throw new ConsistirException("O campo HORA FIM (Avaliação Institucional) deve ser informado.");
			} else if (obj.getHoraFim().length() < 5) {
				throw new ConsistirException(
						"O campo HORA FIM (Avaliação Institucional) deve possuir o formato 00:00.");
			}
			if(obj.getDiasDisponivel().equals(0)) {
				 //String str = txttempoUtiliz.getText();  
				 Date dataInicio;
				 Date dataFim;
				 DateFormat formatador = new SimpleDateFormat("HH:mm");
				 try {
					dataInicio = formatador.parse(obj.getHoraInicio());
					dataFim = formatador.parse(obj.getHoraFim());
					if(dataFim.before(dataInicio)){
						throw new ConsistirException("O campo HORA FIM (Avaliação Institucional) deve ser maior ou igual a HORA INÍCIO (Avaliação Institucional).");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			obj.setPublicarResultadoAluno(false);
			obj.setPublicarResultadoProfessor(false);
			obj.setPublicarResultadoCoordenador(false);
		}
		if (obj.getDataInicio() == null) {
			throw new ConsistirException("O campo DATA INÍCIO (Avaliação Institucional) deve ser informado.");
		}
		if (obj.getDataFinal() == null) {
			throw new ConsistirException("O campo DATA FINAL (Avaliação Institucional) deve ser informado.");
		}
		if (!obj.getIsApresentarOpcaoUltimoModulo()) {
			obj.setAvaliacaoUltimoModulo(false);
		}
		if (obj.getPublicarResultadoRespondente()) {

			if (!Uteis.isAtributoPreenchido(obj.getDataInicioPublicarResultado())) {
				throw new ConsistirException(
						"O campo DATA INÍCIO PUBLICAÇÃO (Avaliação Institucional) deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getDataTerminoPublicarResultado())) {
				throw new ConsistirException(
						"O campo DATA TERMINO PUBLICAÇÃO (Avaliação Institucional) deve ser informado.");
			}
			if (obj.getDataTerminoPublicarResultado().before(obj.getDataInicioPublicarResultado())) {
				throw new ConsistirException("O campo DATA TERMINO PUBLICAÇÃO deve ser maior ou igual a DATA INÍCIO PUBLICAÇÃO (Avaliação Institucional).");
			}
		}
	}

	public boolean getIsPermiteInformarCurso() {
		return getPublicoAlvo_CargoCoordenador() || getPublicoAlvo_DepartamentoCoordenador()
				|| getPublicoAlvo_CoordenadorCargo() || getPublicoAlvo_CoordenadorDepartamento()
				|| getPublicoAlvo_CoordenadorProfessor() || getPublicoAlvo_Curso() || getPublicoAlvo_Turma()
				|| getPublicoAlvo_TodosCoordenadores() || getPublicoAlvo_AlunoCoordenador()
				|| getPublicoAlvo_ProfessorTurma() || getPublicoAlvo_ProfessorCurso()
				|| getPublicoAlvo_CoordenadorAvaliacaoCurso() || getPublicoAlvo_ProfessorCoordenador()
				|| (getPublicoAlvo_Professor() && getTipoFiltroProfessor().equals("curso"));
	}

	public boolean getIsPermiteInformarNivelEducacional() {
		return getPublicoAlvo_CargoCoordenador() || getPublicoAlvo_DepartamentoCoordenador() || getPublicoAlvo_Curso()
				|| getPublicoAlvo_TodosCursos() || getPublicoAlvo_Turma() || getPublicoAlvo_CoordenadorAvaliacaoCurso()
				|| getPublicoAlvo_CoordenadorCargo() || getPublicoAlvo_CoordenadorDepartamento()
				|| getPublicoAlvo_CoordenadorProfessor() || getPublicoAlvo_Professor()
				|| getPublicoAlvo_ProfessorCoordenador() || getPublicoAlvo_TodosCoordenadores()
				|| getPublicoAlvo_AlunoCoordenador() || getPublicoAlvo_ProfessorTurma()
				|| getPublicoAlvo_ProfessorCurso();
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
	 */
	public void realizarUpperCaseDados() {
		setNome(getNome().toUpperCase());
		setDescricao(getDescricao().toUpperCase());
		setPublicoAlvo(getPublicoAlvo().toUpperCase());
	}

	// public QuestionarioVO getNovoQuestionarioResponder() {
	// if (!getQuestionarioUnidadeEnsinoRespondido() &&
	// getQuestionarioUnidadeEnsino().getCodigo().intValue() != 0) {
	// setQuestionarioUnidadeEnsinoRespondido(true);
	// return questionarioUnidadeEnsino;
	// }
	// if (!getQuestionarioCursoRespondido() &&
	// getQuestionarioCurso().getCodigo().intValue() != 0) {
	// CursoVO curso = getNovoCursoResponder();
	// if (curso != null) {
	// questionarioCurso.setCurso(curso.getNome());
	// questionarioCurso.setCodigoCurso(curso.getCodigo());
	// return questionarioCurso;
	// }
	// }
	// if (!getQuestionarioDisciplinaRespondido() &&
	// getQuestionarioDisciplina().getCodigo().intValue() != 0) {
	// matriculaPeriodoTurmaDisciplinaVO = getNovaDisciplinaResponder();
	// if (matriculaPeriodoTurmaDisciplinaVO != null) {
	// questionarioDisciplina.setTurmaVO(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
	// questionarioDisciplina.setIdentificadorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getIdentificadorTurma());
	// questionarioDisciplina.getDisciplinaVO().setNome(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getNome());
	// questionarioDisciplina.getDisciplinaVO().setCodigo(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
	// matriculaPeriodoTurmaDisciplinaVO = new
	// MatriculaPeriodoTurmaDisciplinaVO();
	// return questionarioDisciplina;
	// }
	// }
	// if (!getQuestionarioPerfilSocioEconomicoRespondido()
	// && getQuestionarioPerfilSocioEconomico().getCodigo().intValue() != 0) {
	// setQuestionarioPerfilSocioEconomicoRespondido(true);
	// return questionarioPerfilSocioEconomico;
	// }
	// if (!getQuestionarioPlebicitoRespondido() &&
	// getQuestionarioPlebicito().getCodigo().intValue() != 0) {
	// setQuestionarioPlebicitoRespondido(true);
	// return questionarioPlebicito;
	// }
	// return null;
	// }
	public MatriculaPeriodoTurmaDisciplinaVO getNovaDisciplinaResponder() {
		for (MatriculaPeriodoTurmaDisciplinaVO obj : disciplinaQuestionarioVOs) {
			removerDisciplinaResponder(obj.getDisciplina().getCodigo(), obj.getTurma().getCodigo());
			return obj;
		}
		// setQuestionarioDisciplinaRespondido(true);
		return null;
	}

	public void removerDisciplinaResponder(Integer disciplina, Integer turma) {
		int index = 0;
		for (MatriculaPeriodoTurmaDisciplinaVO obj : disciplinaQuestionarioVOs) {
			if (obj.getTurma().getCodigo().equals(turma) && obj.getDisciplina().getCodigo().equals(disciplina)) {
				disciplinaQuestionarioVOs.remove(index);
				return;
			}
		}
		// setQuestionarioDisciplinaRespondido(true);
	}

	public Boolean verificarExistenciaDisciplinaResponder(Integer disciplina) {
		for (MatriculaPeriodoTurmaDisciplinaVO obj : disciplinaQuestionarioVOs) {
			if (obj.getDisciplina().getCodigo().equals(disciplina)) {
				return true;
			}
		}
		return false;
	}

	public Boolean verificarExistenciaCursoResponder(Integer curso) {
		for (CursoVO obj : getCursoVOs()) {
			if (obj.getCodigo().equals(curso)) {
				return true;
			}
		}
		return false;
	}

	public CursoVO getNovoCursoResponder() {
		for (CursoVO curso : cursoVOs) {
			removerCursoResponder(curso.getCodigo());
			return curso;
		}
		// setQuestionarioCursoRespondido(true);
		return null;
	}

	public void removerCursoResponder(Integer curso) {
		int index = 0;
		for (CursoVO obj : cursoVOs) {
			if (obj.getCodigo().equals(curso)) {
				cursoVOs.remove(index);
				return;
			}
		}
		// setQuestionarioCursoRespondido(true);
	}

	// public void getExisteProximoQuestionario() {
	//
	// if (!getQuestionarioUnidadeEnsinoRespondido() &&
	// getQuestionarioVO().getCodigo().intValue() != 0) {
	// setProximo(true);
	// return;
	// }
	// setProximo(false);
	// }
	public void adicionarListaRespostaQuestionario(List<RespostaAvaliacaoInstitucionalDWVO> listaRespostaQuestionario) {
		for (RespostaAvaliacaoInstitucionalDWVO obj : listaRespostaQuestionario) {
			getRespostaAvaliacaoInstitucionalDWVOs().add(obj);
		}
	}

	/**
	 * Retorna o objeto da classe <code>Usuario</code> relacionado com (
	 * <code>AvaliacaoInstitucional</code>).
	 */
	@XmlElement(name = "responsavel")
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Usuario</code> relacionado com (
	 * <code>AvaliacaoInstitucional</code>).
	 */
	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	/**
	 * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
	 * <code>AvaliacaoInstitucional</code>).
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
	 * <code>AvaliacaoInstitucional</code>).
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
		this.unidadeEnsino = obj;
	}

	@XmlElement(name = "avaliacaoObrigatoria")
	public Boolean getAvaliacaoObrigatoria() {
		if (avaliacaoObrigatoria == null) {
			avaliacaoObrigatoria = Boolean.FALSE;
		}
		return (avaliacaoObrigatoria);
	}

	@XmlElement(name = "isAvaliacaoObrigatoria")
	public Boolean isAvaliacaoObrigatoria() {
		if (avaliacaoObrigatoria == null) {
			avaliacaoObrigatoria = Boolean.FALSE;
		}
		return (avaliacaoObrigatoria);
	}

	public void setAvaliacaoObrigatoria(Boolean avaliacaoObrigatoria) {
		this.avaliacaoObrigatoria = avaliacaoObrigatoria;
	}

	@XmlElement(name = "dataFinal")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataFinal() {
		if (dataFinal == null) {
			dataFinal = new Date();
		}
		return (dataFinal);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	@XmlElement(name = "dataFinalApresentar")
	public String getDataFinal_Apresentar() {
		return (Uteis.getData(getDataFinal()));
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	@XmlElement(name = "dataInicio")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return (dataInicio);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	@XmlElement(name = "dataInicioApresentar")
	public String getDataInicio_Apresentar() {
		return (Uteis.getData(getDataInicio()));
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@XmlElement(name = "publicoAlvo")
	public String getPublicoAlvo() {
		if (publicoAlvo == null) {
			publicoAlvo = "TC";
		}
		return (publicoAlvo);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com
	 * um domínio específico. Com base no valor de armazenamento do atributo esta
	 * função é capaz de retornar o de apresentação correspondente. Útil para campos
	 * como sexo, escolaridade, etc.
	 */
	@XmlElement(name = "publicoAlvoApresentar")
	public String getPublicoAlvo_Apresentar() {
		return (PublicoAlvoAvaliacaoInstitucional.getDescricao(getPublicoAlvo()));
	}

	// public Boolean getPublicoAlvo_todos() {
	// if (getPublicoAlvo().equals("TO")) {
	// return true;
	// }
	// return false;
	// }
	//
	// public Boolean getPublicoAlvo_aluno() {
	// if (getPublicoAlvo().equals("TC")) {
	// return true;
	// }
	// return false;
	// }
	public Boolean getPublicoAlvo_TodosCursos() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_Curso() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CURSO.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_CoordenadorAvaliacaoCurso() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_Turma() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.TURMA.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_FuncionarioGestor() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_TodosCoordenadores() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_Professor() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_ProfessorCoordenador() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_CoordenadorProfessor() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_PROFESSOR.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_DepartamentoCoordenador() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_COORDENADORES.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_CoordenadorDepartamento() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_DEPARTAMENTO.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_CoordenadorCargo() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_CargoCoordenador() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_COORDENADORES.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_ColaboradorInstituicao() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.COLABORADORES_INSTITUICAO.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_AlunoCoordenador() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_ProfessorTurma() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getPublicoAlvo_ProfessorCurso() {
		if (getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getIsApresentarDepartamento() {
		return getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_COORDENADORES.getValor())
				|| getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_DEPARTAMENTO.getValor())
				|| getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_DEPARTAMENTO.getValor())
				|| getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO.getValor())
				|| getIsApresentarCargo();
	}

	public Boolean getIsApresentarCargo() {
		return getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO.getValor())
				|| getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO.getValor())
				|| getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_DEPARTAMENTO.getValor())
				|| getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_COORDENADORES.getValor());
	}

	public Boolean getIsApresentarCargoAvaliado() {
		return getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO.getValor())
				|| getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO.getValor());
	}

	public Boolean getIsApresentarDepartamentoAvaliado() {
		return getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_DEPARTAMENTO.getValor())
				|| getPublicoAlvo().equals(PublicoAlvoAvaliacaoInstitucional.CARGO_DEPARTAMENTO.getValor())
				|| getIsApresentarCargoAvaliado();
	}

	public void setPublicoAlvo(String publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	@XmlElement(name = "descricao")
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "data")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return (data);
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getDisciplinaQuestionarioVOs() {
		if (disciplinaQuestionarioVOs == null) {
			disciplinaQuestionarioVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return disciplinaQuestionarioVOs;
	}

	public void setDisciplinaQuestionarioVOs(List<MatriculaPeriodoTurmaDisciplinaVO> disciplinaQuestionarioVOs) {
		this.disciplinaQuestionarioVOs = disciplinaQuestionarioVOs;
	}

	public List<RespostaAvaliacaoInstitucionalDWVO> getRespostaAvaliacaoInstitucionalDWVOs() {
		if (respostaAvaliacaoInstitucionalDWVOs == null) {
			respostaAvaliacaoInstitucionalDWVOs = new ArrayList<RespostaAvaliacaoInstitucionalDWVO>(0);
		}
		return respostaAvaliacaoInstitucionalDWVOs;
	}

	public void setRespostaAvaliacaoInstitucionalDWVOs(
			List<RespostaAvaliacaoInstitucionalDWVO> respostaAvaliacaoInstitucionalDWVOs) {
		this.respostaAvaliacaoInstitucionalDWVOs = respostaAvaliacaoInstitucionalDWVOs;
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	public String getData_Apresentar() {
		return (Uteis.getData(getData()));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Boolean getProximo() {
		return proximo;
	}

	public void setProximo(Boolean proximo) {
		this.proximo = proximo;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
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

	public List<CursoVO> getCursoVOs() {
		if (cursoVOs == null) {
			cursoVOs = new ArrayList<CursoVO>(0);
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
	}

	@XmlElement(name = "questionarioVO")
	public QuestionarioVO getQuestionarioVO() {
		if (questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}

	@XmlElement(name = "questionarioRespondido")
	public Boolean getQuestionarioRespondido() {
		if (questionarioRespondido == null) {
			questionarioRespondido = Boolean.FALSE;
		}
		return questionarioRespondido;
	}

	public void setQuestionarioRespondido(Boolean questionarioRespondido) {
		this.questionarioRespondido = questionarioRespondido;
	}

	@XmlElement(name = "informarImportanciaPergunta")
	public Boolean getInformarImportanciaPergunta() {
		if (informarImportanciaPergunta == null) {
			informarImportanciaPergunta = Boolean.FALSE;
		}
		return informarImportanciaPergunta;
	}

	public void setInformarImportanciaPergunta(Boolean informarImportanciaPergunta) {
		this.informarImportanciaPergunta = informarImportanciaPergunta;
	}

	public List<TurmaVO> getTurmaVOs() {
		if (turmaVOs == null) {
			turmaVOs = new ArrayList<TurmaVO>(0);
		}
		return turmaVOs;
	}

	public void setTurmaVOs(List<TurmaVO> turmaVOs) {
		this.turmaVOs = turmaVOs;
	}

	@XmlElement(name = "situacao")
	public String getSituacao() {
		if (situacao == null) {
			situacao = "EC";
		}
		return situacao;
	}

	public String getSituacao_Apresentar() {
		if (situacao.equals("EC")) {
			return "Em construção";
		}
		if (situacao.equals("AT")) {
			return "Ativo";
		}
		if (situacao.equals("FI")) {
			return "Finalizado";
		}
		return (situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@XmlElement(name = "avaliacaoPresencial")
	public Boolean getAvaliacaoPresencial() {
		if (avaliacaoPresencial == null) {
			avaliacaoPresencial = false;
		}
		return avaliacaoPresencial;
	}

	public void setAvaliacaoPresencial(Boolean avaliacaoPresencial) {
		this.avaliacaoPresencial = avaliacaoPresencial;
	}

	@XmlElement(name = "tipoFiltroProfessor")
	public String getTipoFiltroProfessor() {
		if (tipoFiltroProfessor == null) {
			tipoFiltroProfessor = "";
		}
		return tipoFiltroProfessor;
	}

	public void setTipoFiltroProfessor(String tipoFiltroProfessor) {
		this.tipoFiltroProfessor = tipoFiltroProfessor;
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

	@XmlElement(name = "nivelEducacional")
	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return (nivelEducacional);
	}

	public String getNivelEducacional_Apresentar() {
		return TipoNivelEducacional.getDescricao(getNivelEducacional());
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	/**
	 * @return the ano
	 */
	@XmlElement(name = "ano")
	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * @return the semestre
	 */
	@XmlElement(name = "semestre")
	public String getSemestre() {
		if (semestre == null) {
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	/**
	 * @param semestre
	 *            the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the avaliacaoUltimoModulo
	 */
	@XmlElement(name = "avaliacaoUltimoModulo")
	public Boolean getAvaliacaoUltimoModulo() {
		if (avaliacaoUltimoModulo == null) {
			avaliacaoUltimoModulo = Boolean.FALSE;
		}
		return avaliacaoUltimoModulo;
	}

	/**
	 * @param avaliacaoUltimoModulo
	 *            the avaliacaoUltimoModulo to set
	 */
	public void setAvaliacaoUltimoModulo(Boolean avaliacaoUltimoModulo) {
		this.avaliacaoUltimoModulo = avaliacaoUltimoModulo;
	}

	/**
	 * @return the horaInicio
	 */
	@XmlElement(name = "horaInicio")
	public String getHoraInicio() {
		if (horaInicio == null) {
			horaInicio = "";
		}
		return horaInicio;
	}

	/**
	 * @param horaInicio
	 *            the horaInicio to set
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * @return the diasDisponivel
	 */
	@XmlElement(name = "diasDisponivel")
	public Integer getDiasDisponivel() {
		if (diasDisponivel == null) {
			diasDisponivel = 0;
		}
		return diasDisponivel;
	}

	/**
	 * @param diasDisponivel
	 *            the diasDisponivel to set
	 */
	public void setDiasDisponivel(Integer diasDisponivel) {
		this.diasDisponivel = diasDisponivel;
	}

	/**
	 * @return the horaFim
	 */
	@XmlElement(name = "horaFim")
	public String getHoraFim() {
		if (horaFim == null) {
			horaFim = "";
		}
		return horaFim;
	}

	/**
	 * @param horaFim
	 *            the horaFim to set
	 */
	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}

	/**
	 * @return the mensagem
	 */
	@XmlElement(name = "mensagem")
	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	/**
	 * @param mensagem
	 *            the mensagem to set
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@XmlElement(name = "departamento")
	public DepartamentoVO getDepartamento() {
		if (departamento == null) {
			departamento = new DepartamentoVO();
		}
		return departamento;
	}

	public void setDepartamento(DepartamentoVO departamento) {
		this.departamento = departamento;
	}

	@XmlElement(name = "cargo")
	public CargoVO getCargo() {
		if (cargo == null) {
			cargo = new CargoVO();
		}
		return cargo;
	}

	public void setCargo(CargoVO cargo) {
		this.cargo = cargo;
	}

	@XmlElement(name = "departamentoAvaliado")
	public DepartamentoVO getDepartamentoAvaliado() {
		if (departamentoAvaliado == null) {
			departamentoAvaliado = new DepartamentoVO();
		}
		return departamentoAvaliado;
	}

	public void setDepartamentoAvaliado(DepartamentoVO departamentoAvaliado) {
		this.departamentoAvaliado = departamentoAvaliado;
	}

	@XmlElement(name = "cargoAvaliado")
	public CargoVO getCargoAvaliado() {
		if (cargoAvaliado == null) {
			cargoAvaliado = new CargoVO();
		}
		return cargoAvaliado;
	}

	public void setCargoAvaliado(CargoVO cargoAvaliado) {
		this.cargoAvaliado = cargoAvaliado;
	}

	@XmlElement(name = "listaAvaliacaoInstitucionalPessoaAvaliadaVOs")
	public List<AvaliacaoInstitucionalPessoaAvaliadaVO> getListaAvaliacaoInstitucionalPessoaAvaliadaVOs() {
		if (listaAvaliacaoInstitucionalPessoaAvaliadaVOs == null) {
			listaAvaliacaoInstitucionalPessoaAvaliadaVOs = new ArrayList<AvaliacaoInstitucionalPessoaAvaliadaVO>();
		}
		return listaAvaliacaoInstitucionalPessoaAvaliadaVOs;
	}

	public void setListaAvaliacaoInstitucionalPessoaAvaliadaVOs(
			List<AvaliacaoInstitucionalPessoaAvaliadaVO> listaAvaliacaoInstitucionalPessoaAvaliadaVOs) {
		this.listaAvaliacaoInstitucionalPessoaAvaliadaVOs = listaAvaliacaoInstitucionalPessoaAvaliadaVOs;
	}

	public Boolean getIsApresentarPeriodoAula() {
		return (getPublicoAlvo_ProfessorTurma() || getPublicoAlvo_ProfessorCurso()
				|| getPublicoAlvo_ProfessorCoordenador() || getPublicoAlvo_CoordenadorProfessor()
				|| getPublicoAlvo_Professor())
				&& !getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor())
				&& !getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())
				&& !getNivelEducacional().equals(TipoNivelEducacional.BASICO.getValor())
				&& !getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor())
				&& !getNivelEducacional().equals(TipoNivelEducacional.INFANTIL.getValor())
				&& !getAvaliacaoUltimoModulo();

	}

	@XmlElement(name = "dataInicioAula")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)	public Date getDataInicioAula() {
		if (dataInicioAula == null) {
			dataInicioAula = new Date();
		}
		return dataInicioAula;
	}

	public void setDataInicioAula(Date dataInicioAula) {
		this.dataInicioAula = dataInicioAula;
	}

	@XmlElement(name = "dataTerminoAula")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataTerminoAula() {
		if (dataTerminoAula == null) {
			dataTerminoAula = new Date();
		}
		return dataTerminoAula;
	}

	public void setDataTerminoAula(Date dataTerminoAula) {
		this.dataTerminoAula = dataTerminoAula;
	}

	@XmlElement(name = "publicarResultadoRespondente")
	public Boolean getPublicarResultadoRespondente() {
		if (publicarResultadoRespondente == null) {
			publicarResultadoRespondente = false;
		}
		return publicarResultadoRespondente;
	}

	public void setPublicarResultadoRespondente(Boolean publicarResultadoRespondente) {
		this.publicarResultadoRespondente = publicarResultadoRespondente;
	}

	@XmlElement(name = "dataInicioPublicarResultado")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataInicioPublicarResultado() {
		if (dataInicioPublicarResultado == null) {
			dataInicioPublicarResultado = getDataFinal();
		}
		return dataInicioPublicarResultado;
	}

	public void setDataInicioPublicarResultado(Date dataInicioPublicarResultado) {
		this.dataInicioPublicarResultado = dataInicioPublicarResultado;
	}

	@XmlElement(name = "dataTerminoPublicarResultado")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataTerminoPublicarResultado() {
		if (dataTerminoPublicarResultado == null) {
			dataTerminoPublicarResultado = getDataFinal();
		}
		return dataTerminoPublicarResultado;
	}

	public void setDataTerminoPublicarResultado(Date dataTerminoPublicarResultado) {
		this.dataTerminoPublicarResultado = dataTerminoPublicarResultado;
	}

	@XmlElement(name = "notificarRespondentes")
	public Boolean getNotificarRespondentes() {
		if (notificarRespondentes == null) {
			notificarRespondentes = true;
		}
		return notificarRespondentes;
	}

	public void setNotificarRespondentes(Boolean notificarRespondentes) {
		this.notificarRespondentes = notificarRespondentes;
	}

	@XmlElement(name = "recorrenciaDiaNotificar")
	public Integer getRecorrenciaDiaNotificar() {
		if (recorrenciaDiaNotificar == null) {
			recorrenciaDiaNotificar = 5;
		}
		return recorrenciaDiaNotificar;
	}

	public void setRecorrenciaDiaNotificar(Integer recorrenciaDiaNotificar) {
		this.recorrenciaDiaNotificar = recorrenciaDiaNotificar;
	}

	public PublicoAlvoAvaliacaoInstitucional getPublicarAlvoEnum() {
		return PublicoAlvoAvaliacaoInstitucional.getEnum(getPublicoAlvo());
	}
	@XmlElement(name = "publicarResultadoAluno")
	public Boolean getPublicarResultadoAluno() {
		if (publicarResultadoAluno == null) {
			publicarResultadoAluno = false;
		}
		return publicarResultadoAluno;
	}

	public void setPublicarResultadoAluno(Boolean publicarResultadoAluno) {
		this.publicarResultadoAluno = publicarResultadoAluno;
	}
	@XmlElement(name = "publicarResultadoProfessor")
	public Boolean getPublicarResultadoProfessor() {
		if (publicarResultadoProfessor == null) {
			publicarResultadoProfessor = false;
		}
		return publicarResultadoProfessor;
	}

	public void setPublicarResultadoProfessor(Boolean publicarResultadoProfessor) {
		this.publicarResultadoProfessor = publicarResultadoProfessor;
	}

	@XmlElement(name = "publicarResultadoCoordenador")
	public Boolean getPublicarResultadoCoordenador() {
		if (publicarResultadoCoordenador == null) {
			publicarResultadoCoordenador = false;
		}
		return publicarResultadoCoordenador;
	}

	public void setPublicarResultadoCoordenador(Boolean publicarResultadoCoordenador) {
		this.publicarResultadoCoordenador = publicarResultadoCoordenador;
	}

	@XmlElement(name = "nivelDetalhamentoPublicarResultado")
	public NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum getNivelDetalhamentoPublicarResultado() {
		if (nivelDetalhamentoPublicarResultado == null) {
			nivelDetalhamentoPublicarResultado = NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.AVALIADO;
		}
		return nivelDetalhamentoPublicarResultado;
	}

	public void setNivelDetalhamentoPublicarResultado(
			NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDetalhamentoPublicarResultado) {
		this.nivelDetalhamentoPublicarResultado = nivelDetalhamentoPublicarResultado;
	}
	@XmlElement(name = "dataUltimaNotificacao")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataUltimaNotificacao() {
		return dataUltimaNotificacao;
	}

	public void setDataUltimaNotificacao(Date dataUltimaNotificacao) {
		this.dataUltimaNotificacao = dataUltimaNotificacao;
	}
	@XmlElement(name = "tipoCoordenadorCurso")
	public TipoCoordenadorCursoEnum getTipoCoordenadorCurso() {
		if (tipoCoordenadorCurso == null) {
			tipoCoordenadorCurso = TipoCoordenadorCursoEnum.GERAL;
		}
		return tipoCoordenadorCurso;
	}

	public void setTipoCoordenadorCurso(TipoCoordenadorCursoEnum tipoCoordenadorCurso) {
		this.tipoCoordenadorCurso = tipoCoordenadorCurso;
	}

	public Boolean getApresentarTipoCoordenadorCurso() {
		return getPublicoAlvo_AlunoCoordenador() || getPublicoAlvo_CargoCoordenador()
				|| getPublicoAlvo_CoordenadorCargo() || getPublicoAlvo_CoordenadorDepartamento()
				|| getPublicoAlvo_CoordenadorProfessor() || getPublicoAlvo_DepartamentoCoordenador()
				|| getPublicoAlvo_ProfessorCoordenador() || getPublicoAlvo_TodosCoordenadores()
				|| getPublicoAlvo_CoordenadorAvaliacaoCurso();
	}

	@XmlElement(name = "avaliacaoInstitucionalCursoVOs")
	public List<AvaliacaoInstitucionalCursoVO> getAvaliacaoInstitucionalCursoVOs() {
		if (avaliacaoInstitucionalCursoVOs == null) {
			avaliacaoInstitucionalCursoVOs = new ArrayList<AvaliacaoInstitucionalCursoVO>(0);
		}
		return avaliacaoInstitucionalCursoVOs;
	}

	public void setAvaliacaoInstitucionalCursoVOs(List<AvaliacaoInstitucionalCursoVO> avaliacaoInstitucionalCursoVOs) {
		this.avaliacaoInstitucionalCursoVOs = avaliacaoInstitucionalCursoVOs;
	}

	@XmlElement(name = "nomeCurso")
	public String getNomeCurso() {
		if (nomeCurso == null) {
			StringBuilder curso = new StringBuilder("");
			if (!getAvaliacaoInstitucionalCursoVOs().isEmpty()) {
				getAvaliacaoInstitucionalCursoVOs().forEach(
						item -> curso.append(curso.length() > 0 ? ", " : " ").append(item.getCursoVO().getNome()));
			} else {
				nomeCurso = getCurso().getNome();
			}
			nomeCurso = curso.toString();
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	@XmlElement(name = "questionarioVOs")
	public List<QuestionarioVO> getQuestionarioVOs() {
		if (questionarioVOs == null) {
			questionarioVOs = new ArrayList<QuestionarioVO>(0);
		}
		return questionarioVOs;
	}

	public void setQuestionarioVOs(List<QuestionarioVO> questionarioVOs) {
		this.questionarioVOs = questionarioVOs;
	}

	@XmlElement(name = "matriculaVO")
	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	@XmlElement(name = "matriculaPeriodoVO")
	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public Integer getQtdeQuestionarios() {
		return getQuestionarioVOs().size();
	}
	
	private String matriculaFuncionario;
	 
	 @XmlElement(name = "matriculaFuncionario")
	 public String getMatriculaFuncionario() {
		if (matriculaFuncionario == null) {
    		matriculaFuncionario = "";
    	}
        return matriculaFuncionario;
    }
	
	public void setMatriculaFuncionario(String matriculaFuncionario) {
		this.matriculaFuncionario = matriculaFuncionario;
	}

	public Boolean getIsApresentarOpcaoUltimoModulo() {
		return getNivelEducacional().equals("PO") && (getPublicoAlvo_Curso() || getPublicoAlvo_TodosCursos()
				|| getPublicoAlvo_Turma() || getPublicoAlvo_Professor());
	}

	public Boolean getAvaliarDisciplinasReposicao() {
		if (avaliarDisciplinasReposicao == null) {
			avaliarDisciplinasReposicao = Boolean.FALSE;
		}
		return avaliarDisciplinasReposicao;
	}

	public void setAvaliarDisciplinasReposicao(Boolean avaliarDisciplinasReposicao) {
		this.avaliarDisciplinasReposicao = avaliarDisciplinasReposicao;
	}
	

	private String keyPerguntaNaoRespondida;

	public String getKeyPerguntaNaoRespondida() {
		if(keyPerguntaNaoRespondida == null) {
			keyPerguntaNaoRespondida = "";
		}
		return keyPerguntaNaoRespondida;
	}

	public void setKeyPerguntaNaoRespondida(String keyPerguntaNaoRespondida) {
		this.keyPerguntaNaoRespondida = keyPerguntaNaoRespondida;
	}

	@XmlElement(name = "avaliacaoInstitucionalUnidadeEnsinoVOs")
	public List<AvaliacaoInstitucionalUnidadeEnsinoVO> getAvaliacaoInstitucionalUnidadeEnsinoVOs() {
		if(avaliacaoInstitucionalUnidadeEnsinoVOs == null){
			avaliacaoInstitucionalUnidadeEnsinoVOs = new ArrayList<AvaliacaoInstitucionalUnidadeEnsinoVO>(0);
		}
		return avaliacaoInstitucionalUnidadeEnsinoVOs;
	}

	public void setAvaliacaoInstitucionalUnidadeEnsinoVOs(List<AvaliacaoInstitucionalUnidadeEnsinoVO> avaliacaoInstitucionalUnidadeEnsinoVOs) {
		this.avaliacaoInstitucionalUnidadeEnsinoVOs = avaliacaoInstitucionalUnidadeEnsinoVOs;
	}
    @XmlElement(name = "nomeUnidadeEnsino")
    public String getNomeUnidadeEnsino() {
        if (nomeUnidadeEnsino == null) {
            StringBuilder unidadeEnsino = new StringBuilder("");
            if (!getAvaliacaoInstitucionalUnidadeEnsinoVOs().isEmpty()) {
                getAvaliacaoInstitucionalUnidadeEnsinoVOs().forEach( item -> unidadeEnsino.append(unidadeEnsino.length() > 0 ? ",": " ").append(item.getUnidadeEnsinoVO().getNome()));
            } else {
                nomeUnidadeEnsino = getUnidadeEnsino().getNome();
            }
            nomeUnidadeEnsino = unidadeEnsino.toString();
        }
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
        if(unidadeEnsinoVOs == null){
            unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
        }
        return unidadeEnsinoVOs;
    }

    public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
        this.unidadeEnsinoVOs = unidadeEnsinoVOs;
    }
}