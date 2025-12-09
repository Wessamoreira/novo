package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade ArtefatoEntregaAlunoVO. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ArtefatoEntregaAlunoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private String nivelControle;
	private String periodicidadeCurso;
	private Boolean trazerAlunoPreMatricula;
	private String situacao;
	private String scriptsRegraRestricaoAluno;
	private List<NivelControleArtefatoVO> nivelControleArtefatoUnidadeEnsinoVOs;
	private List<NivelControleArtefatoVO> nivelControleArtefatoCursoVOs;
	private List<NivelControleArtefatoVO> nivelControleArtefatoDisciplinaVOs;
	private List<NivelControleArtefatoVO> nivelControleArtefatoFuncionarioVOs;

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>ArtefatoEntregaAlunoVO</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ArtefatoEntregaAlunoVO() {
		super();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNivelControle() {
		return nivelControle;
	}

	public void setNivelControle(String nivelControle) {
		this.nivelControle = nivelControle;
	}

	public String getPeriodicidadeCurso() {
		return periodicidadeCurso;
	}

	public void setPeriodicidadeCurso(String periodicidadeCurso) {
		this.periodicidadeCurso = periodicidadeCurso;
	}

	public Boolean getTrazerAlunoPreMatricula() {
		if (trazerAlunoPreMatricula == null) {
			trazerAlunoPreMatricula = false;
		}
		return trazerAlunoPreMatricula;
	}

	public void setTrazerAlunoPreMatricula(Boolean trazerAlunoPreMatricula) {
		this.trazerAlunoPreMatricula = trazerAlunoPreMatricula;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getScriptsRegraRestricaoAluno() {
		if (scriptsRegraRestricaoAluno == null) {
			scriptsRegraRestricaoAluno = "";
		}
		return scriptsRegraRestricaoAluno;
	}

	public void setScriptsRegraRestricaoAluno(String scriptsRegraRestricaoAluno) {
		this.scriptsRegraRestricaoAluno = scriptsRegraRestricaoAluno;
	}

	public List<NivelControleArtefatoVO> getNivelControleArtefatoUnidadeEnsinoVOs() {
		if (nivelControleArtefatoUnidadeEnsinoVOs == null) {
			nivelControleArtefatoUnidadeEnsinoVOs = new ArrayList<NivelControleArtefatoVO>();
		}
		return nivelControleArtefatoUnidadeEnsinoVOs;
	}

	public void setNivelControleArtefatoUnidadeEnsinoVOs(
			List<NivelControleArtefatoVO> nivelControleArtefatoUnidadeEnsinoVOs) {
		this.nivelControleArtefatoUnidadeEnsinoVOs = nivelControleArtefatoUnidadeEnsinoVOs;
	}

	public List<NivelControleArtefatoVO> getNivelControleArtefatoCursoVOs() {
		if (nivelControleArtefatoCursoVOs == null) {
			nivelControleArtefatoCursoVOs = new ArrayList<NivelControleArtefatoVO>();
		}
		return nivelControleArtefatoCursoVOs;
	}

	public void setNivelControleArtefatoCursoVOs(List<NivelControleArtefatoVO> nivelControleArtefatoCursoVOs) {
		this.nivelControleArtefatoCursoVOs = nivelControleArtefatoCursoVOs;
	}

	public List<NivelControleArtefatoVO> getNivelControleArtefatoDisciplinaVOs() {
		if (nivelControleArtefatoDisciplinaVOs == null) {
			nivelControleArtefatoDisciplinaVOs = new ArrayList<NivelControleArtefatoVO>();
		}
		return nivelControleArtefatoDisciplinaVOs;
	}

	public void setNivelControleArtefatoDisciplinaVOs(
			List<NivelControleArtefatoVO> nivelControleArtefatoDisciplinaVOs) {
		this.nivelControleArtefatoDisciplinaVOs = nivelControleArtefatoDisciplinaVOs;
	}

	public List<NivelControleArtefatoVO> getNivelControleArtefatoFuncionarioVOs() {
		if (nivelControleArtefatoFuncionarioVOs == null) {
			nivelControleArtefatoFuncionarioVOs = new ArrayList<NivelControleArtefatoVO>();
		}
		return nivelControleArtefatoFuncionarioVOs;
	}

	public void setNivelControleArtefatoFuncionarioVOs(
			List<NivelControleArtefatoVO> nivelControleArtefatoFuncionarioVOs) {
		this.nivelControleArtefatoFuncionarioVOs = nivelControleArtefatoFuncionarioVOs;
	}

	public void adicionarObjNivelControleArtefatoUnidadeEnsinoVOs(NivelControleArtefatoVO obj) throws Exception {
		int index = 0;
		Iterator i = getNivelControleArtefatoUnidadeEnsinoVOs().iterator();
		while (i.hasNext()) {
			NivelControleArtefatoVO objExistente = (NivelControleArtefatoVO) i.next();
			if (objExistente.getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsino().getCodigo())) {
				getNivelControleArtefatoUnidadeEnsinoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getNivelControleArtefatoUnidadeEnsinoVOs().add(obj);
	}

	public void excluirObjNivelControleArtefatoUnidadeEnsinoVOs(Integer unidadeEnsino) throws Exception {
		int index = 0;
		Iterator i = getNivelControleArtefatoUnidadeEnsinoVOs().iterator();
		while (i.hasNext()) {
			NivelControleArtefatoVO objExistente = (NivelControleArtefatoVO) i.next();
			if (objExistente.getUnidadeEnsino().getCodigo().equals(unidadeEnsino)) {
				getNivelControleArtefatoUnidadeEnsinoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public void adicionarObjNivelControleArtefatoCursoVOs(NivelControleArtefatoVO obj) throws Exception {
		int index = 0;
		Iterator i = getNivelControleArtefatoCursoVOs().iterator();
		while (i.hasNext()) {
			NivelControleArtefatoVO objExistente = (NivelControleArtefatoVO) i.next();
			if (objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo())) {
				getNivelControleArtefatoCursoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getNivelControleArtefatoCursoVOs().add(obj);
	}

	public void excluirObjNivelControleArtefatoCursoVOs(Integer curso) throws Exception {
		int index = 0;
		Iterator i = getNivelControleArtefatoCursoVOs().iterator();
		while (i.hasNext()) {
			NivelControleArtefatoVO objExistente = (NivelControleArtefatoVO) i.next();
			if (objExistente.getCurso().getCodigo().equals(curso)) {
				getNivelControleArtefatoCursoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public void adicionarObjNivelControleArtefatoDisciplinaVOs(NivelControleArtefatoVO obj) throws Exception {
		int index = 0;
		Iterator i = getNivelControleArtefatoDisciplinaVOs().iterator();
		while (i.hasNext()) {
			NivelControleArtefatoVO objExistente = (NivelControleArtefatoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				getNivelControleArtefatoDisciplinaVOs().set(index, obj);
				return;
			}
			index++;
		}
		getNivelControleArtefatoDisciplinaVOs().add(obj);
	}

	public void excluirObjNivelControleArtefatoDisciplinaVOs(Integer disciplina) throws Exception {
		int index = 0;
		Iterator i = getNivelControleArtefatoDisciplinaVOs().iterator();
		while (i.hasNext()) {
			NivelControleArtefatoVO objExistente = (NivelControleArtefatoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
				getNivelControleArtefatoDisciplinaVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public void adicionarObjNivelControleArtefatoFuncionarioVOs(NivelControleArtefatoVO obj) throws Exception {
		int index = 0;
		Iterator i = getNivelControleArtefatoFuncionarioVOs().iterator();
		while (i.hasNext()) {
			NivelControleArtefatoVO objExistente = (NivelControleArtefatoVO) i.next();
			if (objExistente.getFuncionario().getCodigo().equals(obj.getFuncionario().getCodigo())) {
				getNivelControleArtefatoFuncionarioVOs().set(index, obj);
				return;
			}
			index++;
		}
		getNivelControleArtefatoFuncionarioVOs().add(obj);
	}

	public void excluirObjNivelControleArtefatoFuncionarioVOs(Integer funcionario) throws Exception {
		int index = 0;
		Iterator i = getNivelControleArtefatoFuncionarioVOs().iterator();
		while (i.hasNext()) {
			NivelControleArtefatoVO objExistente = (NivelControleArtefatoVO) i.next();
			if (objExistente.getFuncionario().getCodigo().equals(funcionario)) {
				getNivelControleArtefatoFuncionarioVOs().remove(index);
				return;
			}
			index++;
		}
	}

}
