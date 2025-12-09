package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioRequerimentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;

/**
 * Reponsável por manter os dados da entidade Questionario. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "questionario")
public class QuestionarioVO extends SuperVO {
	
	protected Integer codigo;
	protected String descricao;
	protected String situacao;
	private String escopo;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>PerguntaQuestionario</code>.
	 */
	private List<PerguntaQuestionarioVO> perguntaQuestionarioVOs;
	public List<RespostaPerguntaVO> respostasVOs;
	private DisciplinaVO disciplinaVO;
	private Integer turmaVO;
	private String identificadorTurma;
	private String curso;
	private Integer codigoCurso;
	private PessoaVO professor;
	// Atributo para controle de tela
	private Boolean abrirToggle;
	private Boolean todasPerguntasRespondidas;
	private TipoEscopoQuestionarioRequerimentoEnum tipoEscopoRequerimento;
	public static final long serialVersionUID = 1L;

	/**
	 * Atributos transientes utilizado na apresentacao dos dados da avaliacao
	 * institucional
	 */
	private DepartamentoVO departamento;
	private CargoVO cargo;
	private PessoaVO coordenador;
	private PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional;
	private Integer turno;
	private Integer unidadeEnsino;
	private Boolean desabilitarAlterarEscopoQuestionarioRequerimento;

	/**
	 * Construtor padrão da classe <code>Questionario</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public QuestionarioVO() {
		super();
		inicializarDados();
	}

	@XmlElement(name = "nomeApresentar")
	public String getNomeApresentar() {
		if (getEscopo().equals("DI")) {
			// return " - " + getDisciplinaVO().getNome();
			return getDisciplinaVO().getNome();
		}
		if (getEscopo().equals("UM")) {
			// return " - " + descricao;
			return descricao;
		}
		if (getEscopo().equals("CU")) {
			// return " - " + getCurso();
			return getCurso();
		}
		return descricao;
	}

	@XmlElement(name = "apresentarDisciplina")
	public Boolean getApresentarDisciplina() {
		if (getEscopo().equals("DI")) {
			return true;
		}
		if (getEscopo().equals("UM")) {
			return true;
		}
		return false;
	}

	@XmlElement(name = "apresentarUnidadeEnsino")
	public Boolean getApresentarUnidadeEnsino() {
		if (getEscopo().equals("UE")) {
			return true;
		}
		return false;
	}

	@XmlElement(name = "apresentarCurso")
	public Boolean getApresentarCurso() {
		if (getEscopo().equals("CU")) {
			return true;
		}
		return false;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>QuestionarioVO</code>. Todos os tipos de consistência de dados são
	 * e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(QuestionarioVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getDescricao().equals("")) {
			throw new ConsistirException("O campo DESCRIÇÃO (Questionário) deve ser informado.");
		}
		if (obj.getEscopo().equals("")) {
			throw new ConsistirException("O campo ESCOPO (Questionário) deve ser informado.");
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getPerguntaQuestionarioVOs())) {
			throw new ConsistirException("Nenhuma pergunta foi adicionada ao Questionário.");
		}
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setDescricao("");
		setSituacao("EC");
		setEscopo("");
		setTurmaVO(0);
		setIdentificadorTurma("");
		setCurso("");
		setCodigoCurso(0);
	}

	public void varrerListaQuestionarioRetornarPerguntaRespondida(RespostaPerguntaVO obj) {

		Iterator<PerguntaQuestionarioVO> i = getPerguntaQuestionarioVOs().iterator();
		while (i.hasNext()) {
			PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
			if (objExistente.getPergunta().getCodigo().equals(obj.getPergunta())) {
				if (objExistente.getPergunta().getTipoRespostaSimplesEscolha()) {
					objExistente.getPergunta().setarValorFalsoSimplesEscolha(obj.getCodigo());
				}
				return;
			}
		}
	}

	public void limparDadosRespostaPegunta() {
		for (PerguntaQuestionarioVO obj : getPerguntaQuestionarioVOs()) {
			obj.getPergunta().limparResposta();

		}
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>PerguntaQuestionarioVO</code> ao List
	 * <code>perguntaQuestionarioVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>PerguntaQuestionario</code> -
	 * getPergunta().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param obj
	 *            Objeto da classe <code>PerguntaQuestionarioVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjPerguntaQuestionarioVOs(PerguntaQuestionarioVO obj) throws Exception {
		PerguntaQuestionarioVO.validarDados(obj);
		int index = 0;
		Iterator<PerguntaQuestionarioVO> i = getPerguntaQuestionarioVOs().iterator();
		while (i.hasNext()) {
			PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
			if (objExistente.getPergunta().getCodigo().equals(obj.getPergunta().getCodigo())) {
				getPerguntaQuestionarioVOs().set(index, obj);
				return;
			}
			index++;
		}
		getPerguntaQuestionarioVOs().add(obj);
		realizaReorganizacaoOrdemPergunta();
	}

	public Integer getNumeroOpcoes() {
		return getPerguntaQuestionarioVOs().size();
	}

	public void realizaReorganizacaoOrdemPergunta() {
		int index = 1;
		Iterator<PerguntaQuestionarioVO> i = getPerguntaQuestionarioVOs().iterator();
		while (i.hasNext()) {
			PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
			objExistente.setOrdem(index++);
		}
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>PerguntaQuestionarioVO</code> no List
	 * <code>perguntaQuestionarioVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>PerguntaQuestionario</code> -
	 * getPergunta().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param pergunta
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjPerguntaQuestionarioVOs(Integer pergunta) throws Exception {
		int index = 0;
		Iterator<PerguntaQuestionarioVO> i = getPerguntaQuestionarioVOs().iterator();
		while (i.hasNext()) {
			PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
			if (objExistente.getPergunta().getCodigo().equals(pergunta)) {
				getPerguntaQuestionarioVOs().remove(index);
				realizaReorganizacaoOrdemPergunta();
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>PerguntaQuestionarioVO</code> no List
	 * <code>perguntaQuestionarioVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>PerguntaQuestionario</code> -
	 * getPergunta().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param pergunta
	 *            Parâmetro para localizar o objeto do List.
	 */
	public PerguntaQuestionarioVO consultarObjPerguntaQuestionarioVO(Integer pergunta) throws Exception {
		Iterator<PerguntaQuestionarioVO> i = getPerguntaQuestionarioVOs().iterator();
		while (i.hasNext()) {
			PerguntaQuestionarioVO objExistente = (PerguntaQuestionarioVO) i.next();
			if (objExistente.getPergunta().getCodigo().equals(pergunta)) {
				return objExistente;
			}
		}
		return null;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>PerguntaQuestionario</code>.
	 */
	@XmlElement(name = "perguntaQuestionarioVOs")
	public List<PerguntaQuestionarioVO> getPerguntaQuestionarioVOs() {
		if (perguntaQuestionarioVOs == null) {
			perguntaQuestionarioVOs = new ArrayList<PerguntaQuestionarioVO>(0);
		}
		return (perguntaQuestionarioVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>PerguntaQuestionario</code>.
	 */
	public void setPerguntaQuestionarioVOs(List<PerguntaQuestionarioVO> perguntaQuestionarioVOs) {
		this.perguntaQuestionarioVOs = perguntaQuestionarioVOs;
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

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			return 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "situacao")
	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@XmlElement(name = "situacaoApresentar")
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

	@XmlElement(name = "situacaoQuestionarioAtivoFinalizado")
	public Boolean getSituacaoQuestionarioAtivoFinalizado() {
		if (getSituacao().equals("AT") || getSituacao().equals("FI")) {
			return true;
		}
		return false;
	}

	@XmlElement(name = "respostasVOs")
	public List<RespostaPerguntaVO> getRespostasVOs() {
		if (respostasVOs == null) {
			respostasVOs = new ArrayList<>();
		}
		return respostasVOs;
	}

	public void setRespostasVOs(List<RespostaPerguntaVO> respostasVOs) {
		this.respostasVOs = respostasVOs;
	}

	/**
	 * @return the escopo
	 */
	@XmlElement(name = "escopoApresentar")
	public String getEscopo_Apresentar() {
		if (escopo == null) {
			escopo = "";
		}
		if (escopo.equals("GE")) {
			return "Geral";
		}
		if (escopo.equals("DI")) {
			return "Disciplina";
		}
		if (escopo.equals("UM")) {
			return "Último Módulo";
		}
		if (escopo.equals("PS")) {
			return "Processo Seletivo";
		}
		if (escopo.equals("RE")) {
			return "Requerimento";
		}
		if (escopo.equals("PR")) {
			return "Professores";
		}
		if (escopo.equals("FG")) {
			return "Funcionário/Gestor";
		}
		if (escopo.equals("CO")) {
			return "Coordenadores";
		}
		if (escopo.equals("BC")) {
			return "Banco Curriculum";
		}

		return escopo;
	}

	@XmlElement(name = "escopo")
	public String getEscopo() {
		if (escopo == null) {
			escopo = "";
		}
		return escopo;
	}

	@XmlElement(name = "disciplinaVO")
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	@XmlElement(name = "identificadorTurma")
	public String getIdentificadorTurma() {
		if (identificadorTurma == null) {
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}

	@XmlElement(name = "turmaVO")
	public Integer getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = 0;
		}
		return turmaVO;
	}

	public void setTurmaVO(Integer turmaVO) {
		this.turmaVO = turmaVO;
	}

	@XmlElement(name = "curso")
	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
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

	@XmlElement(name = "codigoCurso")
	public Integer getCodigoCurso() {
		if (codigoCurso == null) {
			codigoCurso = 0;
		}
		return codigoCurso;
	}

	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	/**
	 * @param escopo
	 *            the escopo to set
	 */
	public void setEscopo(String escopo) {
		this.escopo = escopo;
	}

	@XmlElement(name = "abrirToggle")
	public Boolean getAbrirToggle() {
		if (abrirToggle == null) {
			abrirToggle = Boolean.FALSE;
		}
		return abrirToggle;
	}

	public void setAbrirToggle(Boolean abrirToggle) {
		this.abrirToggle = abrirToggle;
	}

	@XmlElement(name = "todasPerguntasRespondidas")
	public Boolean getTodasPerguntasRespondidas() {
		if (todasPerguntasRespondidas == null) {
			todasPerguntasRespondidas = Boolean.FALSE;
		}
		return todasPerguntasRespondidas;
	}

	public void setTodasPerguntasRespondidas(Boolean todasPerguntasRespondidas) {
		this.todasPerguntasRespondidas = todasPerguntasRespondidas;
	}

	@XmlElement(name = "tituloCamposQuestionarioRespondido")
	public String getCssApresentarCorHeader() {
		if (getTodasPerguntasRespondidas()) {
			return "tituloCamposQuestionarioRespondido";
		}
		return "tituloCamposQuestionarioNaoRespondido";
	}

	private String tituloApresentar;

	@XmlElement(name = "tituloToggle_Apresentar")
	public String getTituloToggle_Apresentar() {
		if (tituloApresentar == null) {
			if (getEscopo().equals("UM") || getEscopo().equals("DI")) {
				tituloApresentar = getDisciplinaVO().getNome();
				if (!getProfessor().getNome().isEmpty()) {
					tituloApresentar += " - Professor(a): " + getProfessor().getNome();
				}
			}else{
				if(!getDepartamento().getNome().isEmpty()){
					tituloApresentar = "Departamento: "+getDepartamento().getNome();
				}
				if(!getCargo().getNome().isEmpty()){
					tituloApresentar += " - Cargo: "+getCargo().getNome();
				}
				if(!getCoordenador().getNome().isEmpty()){
					tituloApresentar = " Coordenador(a) de Curso: "+getCoordenador().getNome();
				}
				if(!getProfessor().getNome().isEmpty()){
					tituloApresentar = " Professor(a): "+getProfessor().getNome();
				}
				if(Uteis.isAtributoPreenchido(getPublicoAlvoAvaliacaoInstitucional()) &&  getPublicoAlvoAvaliacaoInstitucional().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA)){
					tituloApresentar = " Turma: "+getIdentificadorTurma();
				}
				if(Uteis.isAtributoPreenchido(getPublicoAlvoAvaliacaoInstitucional()) && (getPublicoAlvoAvaliacaoInstitucional().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO) || getPublicoAlvoAvaliacaoInstitucional().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO))){
					tituloApresentar = " Curso: "+getCurso();
				}
				if (tituloApresentar == null) {
					tituloApresentar = "";
				}
				
			}
		}
		return tituloApresentar;
	}

	@XmlElement(name = "tipoEscopoRequerimento")
	public TipoEscopoQuestionarioRequerimentoEnum getTipoEscopoRequerimento() {
		if (tipoEscopoRequerimento == null) {
			if (getEscopo().equals("RE")) {
				tipoEscopoRequerimento = TipoEscopoQuestionarioRequerimentoEnum.REQUERENTE;
			}
		}
		return tipoEscopoRequerimento;
	}

	public void setTipoEscopoRequerimento(TipoEscopoQuestionarioRequerimentoEnum tipoEscopoRequerimento) {
		this.tipoEscopoRequerimento = tipoEscopoRequerimento;
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

	@XmlElement(name = "coordenador")
	public PessoaVO getCoordenador() {
		if (coordenador == null) {
			coordenador = new PessoaVO();
		}
		return coordenador;
	}

	public void setCoordenador(PessoaVO coordenador) {
		this.coordenador = coordenador;
	}

	@XmlElement(name = "publicoAlvoAvaliacaoInstitucional")
	public PublicoAlvoAvaliacaoInstitucional getPublicoAlvoAvaliacaoInstitucional() {
		return publicoAlvoAvaliacaoInstitucional;
	}

	public void setPublicoAlvoAvaliacaoInstitucional(PublicoAlvoAvaliacaoInstitucional publicoAlvoAvaliacaoInstitucional) {
		this.publicoAlvoAvaliacaoInstitucional = publicoAlvoAvaliacaoInstitucional;
	}
	
//	public Integer getQtdePerguntaPendente(){
//		int x= 0;
//		for(PerguntaQuestionarioVO obj: getPerguntaQuestionarioVOs()){
//			x += obj.getPergunta().getPerguntaPendente()?1:0;
//		}
//		return x;
//	}
	
	@XmlElement(name = "apresentarTitulo")
	public Boolean getApresentarTitulo(){
		return !getTituloToggle_Apresentar().isEmpty();
	}

	public Integer getTurno() {
		if (turno == null) {
			turno = 0;
		}
		return turno;
	}

	public void setTurno(Integer turno) {
		this.turno = turno;
	}

	public Integer getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = 0;
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	
	
	public int getKey() {
		return hashCode();
	}

	@Override
	public int hashCode() {
		final int prime = 1;
		int result = 1;
		result = prime * result + ((cargo == null) ? 0 : cargo.getCodigo().hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((coordenador == null) ? 0 : coordenador.getCodigo().hashCode());
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((departamento == null) ? 0 : departamento.getCodigo().hashCode());
		result = prime * result + ((disciplinaVO == null) ? 0 : disciplinaVO.getCodigo().hashCode());
		result = prime * result + ((professor == null) ? 0 : professor.getCodigo().hashCode());
		result = prime * result + ((turmaVO == null) ? 0 : turmaVO.hashCode());
		result = prime * result + ((turno == null) ? 0 : turno.hashCode());
		result = prime * result + ((unidadeEnsino == null) ? 0 : unidadeEnsino.hashCode());
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
		QuestionarioVO other = (QuestionarioVO) obj;
		if (cargo == null) {
			if (other.cargo != null)
				return false;
		} else if (!cargo.getCodigo().equals(other.cargo.getCodigo()))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (coordenador == null) {
			if (other.coordenador != null)
				return false;
		} else if (!coordenador.getCodigo().equals(other.coordenador.getCodigo()))
			return false;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.equals(other.curso))
			return false;
		if (departamento == null) {
			if (other.departamento != null)
				return false;
		} else if (!departamento.getCodigo().equals(other.departamento.getCodigo()))
			return false;
		if (disciplinaVO == null) {
			if (other.disciplinaVO != null)
				return false;
		} else if (!disciplinaVO.getCodigo().equals(other.disciplinaVO.getCodigo()))
			return false;
		if (professor == null) {
			if (other.professor != null)
				return false;
		} else if (!professor.getCodigo().equals(other.professor.getCodigo()))
			return false;
		if (turmaVO == null) {
			if (other.turmaVO != null)
				return false;
		} else if (!turmaVO.equals(other.turmaVO))
			return false;
		if (turno == null) {
			if (other.turno != null)
				return false;
		} else if (!turno.equals(other.turno))
			return false;
		if (unidadeEnsino == null) {
			if (other.unidadeEnsino != null)
				return false;
		} else if (!unidadeEnsino.equals(other.unidadeEnsino))
			return false;
		return true;
	}

	public Boolean getDesabilitarAlterarEscopoQuestionarioRequerimento() {
		if (desabilitarAlterarEscopoQuestionarioRequerimento == null) {
			desabilitarAlterarEscopoQuestionarioRequerimento = false;
		}
		return desabilitarAlterarEscopoQuestionarioRequerimento;
	}

	public void setDesabilitarAlterarEscopoQuestionarioRequerimento(Boolean desabilitarAlterarEscopoQuestionarioRequerimento) {
		this.desabilitarAlterarEscopoQuestionarioRequerimento = desabilitarAlterarEscopoQuestionarioRequerimento;
	}
}
