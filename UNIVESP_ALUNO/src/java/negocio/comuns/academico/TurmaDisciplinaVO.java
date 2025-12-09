package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Turma;

/**
 * Reponsável por manter os dados da entidade TurmaDisciplina. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Turma
 */
public class TurmaDisciplinaVO extends SuperVO {

	private Integer codigo;
	private Integer turma;
	private DisciplinaVO disciplina;
	private ModalidadeDisciplinaEnum modalidadeDisciplina;
	private Boolean permiteApoioPresencial;
	private Integer nrAlunosMatriculados;
	private Integer nrVagasMatricula;
	private Integer nrMaximoMatricula;
	private Double avaliacao;
//	private LocalAulaVO localAula;
//	private SalaLocalAulaVO salaLocalAula;
	private DisciplinaVO disciplinaEquivalenteTurmaAgrupada;
	private String mensagemDisciplinaEquivalenteTurmaAgrupada;
	private Boolean disciplinaReferenteAUmGrupoOptativa;
	private GradeDisciplinaVO gradeDisciplinaVO;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private TurmaVO turmaDescricaoVO;
	private Boolean selecionado;
	private Boolean permiteReposicao;
	/**
	 * @author Victor Hugo 10/02/2015
	 * Transient
	 */
	private List<SelectItem> listaSelectItemModalidadeDisciplina;
	private List<TurmaDisciplinaCompostaVO> turmaDisciplinaCompostaVOs;
	private Boolean possuiRestricao;
	private boolean alterarMatrizCurricular=false;
	private Integer operacaoMatrizCurricular;
	private Integer operacaoMatrizCurricularTemp;
	private List<SelectItem> listaSelectItemOperacaoMatrizCurricular;
	private Integer operacaoMatrizCurricularReposicao;
	private List<SelectItem> listaSelectItemOperacaoMatrizCurricularReposicao;
	private Integer qtdAlunosReposicao;

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>TurmaDisciplina</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public TurmaDisciplinaVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>TurmaDisciplinaVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(TurmaDisciplinaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getDisciplina() == null || obj.getDisciplina().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo DISCIPLINA (Turma Disciplina) deve ser informado.");
		}
	}

	public static void validarDadosLocalAula(TurmaDisciplinaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
//		if (obj.getLocalAula() == null || obj.getLocalAula().getCodigo().intValue() == 0) {
//			throw new ConsistirException("O campo LOCAL (Turma Disciplina) deve ser informado.");
//		}
//		if (obj.getSalaLocalAula() == null || obj.getSalaLocalAula().getCodigo().intValue() == 0) {
//			throw new ConsistirException("O campo LOCAL (Turma Disciplina) deve ser informado.");
//		}
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNrAlunosMatriculados(0);
		setNrMaximoMatricula(0);
		setNrVagasMatricula(0);
	}

	public Integer getNrAlunosMatriculados() {
		return (nrAlunosMatriculados);
	}

	public void setNrAlunosMatriculados(Integer nrAlunosMatriculados) {
		this.nrAlunosMatriculados = nrAlunosMatriculados;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public Integer getNrMaximoMatricula() {
		return nrMaximoMatricula;
	}

	public void setNrMaximoMatricula(Integer nrMaximoMatricula) {
		this.nrMaximoMatricula = nrMaximoMatricula;
	}

	public Integer getNrVagasMatricula() {
		return nrVagasMatricula;
	}

	public void setNrVagasMatricula(Integer nrVagasMatricula) {
		this.nrVagasMatricula = nrVagasMatricula;
	}

	public Integer getTurma() {
		return (turma);
	}

	public void setTurma(Integer turma) {
		this.turma = turma;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		return "TurmaDisciplinaVO [codigo=" + codigo + "]" + "DisciplinaVO " + disciplina.toString();
	}

	public ModalidadeDisciplinaEnum getModalidadeDisciplina() {
		if (modalidadeDisciplina == null) {
			modalidadeDisciplina = ModalidadeDisciplinaEnum.PRESENCIAL;
		}
		return modalidadeDisciplina;
	}

	public void setModalidadeDisciplina(ModalidadeDisciplinaEnum modalidadeDisciplina) {
		this.modalidadeDisciplina = modalidadeDisciplina;
	}



	public String getMensagemDisciplinaEquivalenteTurmaAgrupada() {
		if (mensagemDisciplinaEquivalenteTurmaAgrupada == null) {
			mensagemDisciplinaEquivalenteTurmaAgrupada = "";
		}
		return mensagemDisciplinaEquivalenteTurmaAgrupada;
	}

	public void setMensagemDisciplinaEquivalenteTurmaAgrupada(String mensagemDisciplinaEquivalenteTurmaAgrupada) {
		this.mensagemDisciplinaEquivalenteTurmaAgrupada = mensagemDisciplinaEquivalenteTurmaAgrupada;
	}

	public boolean getApresentarMensagemDisciplinaEquivalente() {
		return !getMensagemDisciplinaEquivalenteTurmaAgrupada().equals("");
	}

	public DisciplinaVO getDisciplinaEquivalenteTurmaAgrupada() {
		if (disciplinaEquivalenteTurmaAgrupada == null) {
			disciplinaEquivalenteTurmaAgrupada = new DisciplinaVO();
		}
		return disciplinaEquivalenteTurmaAgrupada;
	}

	public void setDisciplinaEquivalenteTurmaAgrupada(DisciplinaVO disciplinaEquivalenteTurmaAgrupada) {
		this.disciplinaEquivalenteTurmaAgrupada = disciplinaEquivalenteTurmaAgrupada;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
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
	 * @param gradeCurricularGrupoOptativaDisciplinaVO
	 *            the gradeCurricularGrupoOptativaDisciplinaVO to set
	 */
	public void setGradeCurricularGrupoOptativaDisciplinaVO(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
		this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
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
	 * @param disciplinaReferenteAUmGrupoOptativa
	 *            the disciplinaReferenteAUmGrupoOptativa to set
	 */
	public void setDisciplinaReferenteAUmGrupoOptativa(Boolean disciplinaReferenteAUmGrupoOptativa) {
		this.disciplinaReferenteAUmGrupoOptativa = disciplinaReferenteAUmGrupoOptativa;
	}

	public Double getAvaliacao() {
		if (avaliacao == null) {
			avaliacao = new Double(0);
		}
		return avaliacao;
	}

	public void setAvaliacao(Double avaliacao) {
		this.avaliacao = avaliacao;
	}

	public TurmaVO getTurmaDescricaoVO() {
		if (turmaDescricaoVO == null) {
			turmaDescricaoVO = new TurmaVO();
			turmaDescricaoVO.setCodigo(getTurma());
		}
		return turmaDescricaoVO;
	}

	public void setTurmaDescricaoVO(TurmaVO turmaDescricaoVO) {
		this.turmaDescricaoVO = turmaDescricaoVO;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	/**
	 * @author Victor Hugo 10/11/2014
	 * 
	 */
	private DefinicoesTutoriaOnlineEnum definicoesTutoriaOnline;

	public DefinicoesTutoriaOnlineEnum getDefinicoesTutoriaOnline() {
		if (definicoesTutoriaOnline == null) {
			definicoesTutoriaOnline = DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA;
		}
		return definicoesTutoriaOnline;
	}

	public void setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum definicoesTutoriaOnline) {
		this.definicoesTutoriaOnline = definicoesTutoriaOnline;
	}
	
	/**
	 * @author Victor Hugo 21/11/2014
	 * 
	 */
	private Integer ordemEstudoOnline;

	public Integer getOrdemEstudoOnline() {
		if (ordemEstudoOnline == null) {
			ordemEstudoOnline = 0;
		}
		return ordemEstudoOnline;
	}

	public void setOrdemEstudoOnline(Integer ordemEstudoOnline) {
		this.ordemEstudoOnline = ordemEstudoOnline;
	}
	
	public Boolean getIsDesativarSelectOneMenuDefinicaoTutoriaSeModalidadePresencial() {
		if(getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)) {
			setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA);
			return true;
		}
		return false;
	}
	
	public List<SelectItem> getListaSelectItemModalidadeDisciplina() {
		if(listaSelectItemModalidadeDisciplina == null) {
			listaSelectItemModalidadeDisciplina = new ArrayList<SelectItem>();
		}
		return listaSelectItemModalidadeDisciplina;
	}

	public void setListaSelectItemModalidadeDisciplina(List<SelectItem> listaSelectItemModalidadeDisciplina) {
		this.listaSelectItemModalidadeDisciplina = listaSelectItemModalidadeDisciplina;
	}

	public Boolean getPermiteReposicao() {
		if (permiteReposicao == null) {
			permiteReposicao = Boolean.TRUE;
		}
		return permiteReposicao;
	}

	public void setPermiteReposicao(Boolean permiteReposicao) {
		this.permiteReposicao = permiteReposicao;
	}

	public List<TurmaDisciplinaCompostaVO> getTurmaDisciplinaCompostaVOs() {
		if (turmaDisciplinaCompostaVOs == null) {
			turmaDisciplinaCompostaVOs = new ArrayList<TurmaDisciplinaCompostaVO>(0);
		}
		return turmaDisciplinaCompostaVOs;
	}

	public void setTurmaDisciplinaCompostaVOs(List<TurmaDisciplinaCompostaVO> turmaDisciplinaCompostaVOs) {
		this.turmaDisciplinaCompostaVOs = turmaDisciplinaCompostaVOs;
	}

	public Integer getCargaHoraria() {
		if (getDisciplinaReferenteAUmGrupoOptativa()) {
			return getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria();
		} else {
			return getGradeDisciplinaVO().getCargaHoraria();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((disciplina == null) ? 0 : disciplina.hashCode());
		result = prime * result + ((turma == null) ? 0 : turma.hashCode());
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
		TurmaDisciplinaVO other = (TurmaDisciplinaVO) obj;
		if (disciplina == null) {
			if (other.disciplina != null)
				return false;
		} else if (!disciplina.getCodigo().equals(other.disciplina.getCodigo()))
			return false;
		if (turmaDescricaoVO == null) {
			if (other.turmaDescricaoVO != null)
				return false;
		} else if (!turmaDescricaoVO.getCodigo().equals(other.turmaDescricaoVO.getCodigo()))
			return false;
		return true;
	}

	public Boolean getPossuiRestricao() {
		if (possuiRestricao == null) {
			possuiRestricao = false;
		}
		return possuiRestricao;
	}

	public void setPossuiRestricao(Boolean possuiRestricao) {
		this.possuiRestricao = possuiRestricao;
	}

	public Boolean getPermiteApoioPresencial() {
		if (permiteApoioPresencial == null) {
			permiteApoioPresencial = Boolean.TRUE;
		}
		return permiteApoioPresencial;
	}

	public void setPermiteApoioPresencial(Boolean permiteApoioPresencial) {
		this.permiteApoioPresencial = permiteApoioPresencial;
	}

	public boolean isAlterarMatrizCurricular() {
		return alterarMatrizCurricular;
	}
	
	public void setAlterarMatrizCurricular(boolean alterarMatrizCurricular) {
		this.alterarMatrizCurricular = alterarMatrizCurricular;
	}
	
	/**
	 * É inicializado com -2 por ser o valor excluir da combobox
	 * @return
	 */
	public Integer getOperacaoMatrizCurricular() {
		if(operacaoMatrizCurricular == null){
			operacaoMatrizCurricular = -2;
		}
		return operacaoMatrizCurricular;
	}

	public void setOperacaoMatrizCurricular(Integer operacaoMatrizCurricular) {
		this.operacaoMatrizCurricular = operacaoMatrizCurricular;
	}
	
	public Integer getOperacaoMatrizCurricularTemp() {
		if(operacaoMatrizCurricularTemp == null){
			operacaoMatrizCurricularTemp = -2;
		}
		return operacaoMatrizCurricularTemp;
	}
	
	public void setOperacaoMatrizCurricularTemp(Integer operacaoMatrizCurricularTemp) {
		this.operacaoMatrizCurricularTemp = operacaoMatrizCurricularTemp;
	}

	public List<SelectItem> getListaSelectItemOperacaoMatrizCurricular() {
		if (listaSelectItemOperacaoMatrizCurricular == null) {
			listaSelectItemOperacaoMatrizCurricular = new ArrayList<>();
		}
		return listaSelectItemOperacaoMatrizCurricular;
	}

	public void setListaSelectItemOperacaoMatrizCurricular(List<SelectItem> listaSelectItemOperacaoMatrizCurricular) {
		this.listaSelectItemOperacaoMatrizCurricular = listaSelectItemOperacaoMatrizCurricular;
	}

	public Integer getOperacaoMatrizCurricularReposicao() {
		if (operacaoMatrizCurricularReposicao == null) {
			operacaoMatrizCurricularReposicao = -2;
		}
		return operacaoMatrizCurricularReposicao;
	}

	public void setOperacaoMatrizCurricularReposicao(Integer operacaoMatrizCurricularReposicao) {
		this.operacaoMatrizCurricularReposicao = operacaoMatrizCurricularReposicao;
	}

	public List<SelectItem> getListaSelectItemOperacaoMatrizCurricularReposicao() {
		if (listaSelectItemOperacaoMatrizCurricularReposicao == null) {
			listaSelectItemOperacaoMatrizCurricularReposicao = new ArrayList<>();
		}
		return listaSelectItemOperacaoMatrizCurricularReposicao;
	}

	public void setListaSelectItemOperacaoMatrizCurricularReposicao(List<SelectItem> listaSelectItemOperacaoMatrizCurricularReposicao) {
		this.listaSelectItemOperacaoMatrizCurricularReposicao = listaSelectItemOperacaoMatrizCurricularReposicao;
	}
	public Integer getQtdAlunosReposicao() {
		if (qtdAlunosReposicao == null) {
			qtdAlunosReposicao = 0;
		}
		return qtdAlunosReposicao;
	}

	public void setQtdAlunosReposicao(Integer qtdAlunosReposicao) {
		this.qtdAlunosReposicao = qtdAlunosReposicao;
	}	
		
	private PessoaVO professorVO;

	public PessoaVO getProfessorVO() {
		if(professorVO == null) {
			professorVO =  new PessoaVO();
		}
		return professorVO;
	}

	public void setProfessorVO(PessoaVO professorVO) {
		this.professorVO = professorVO;
	}
	
	public boolean isApresentarDefinirPeriodoAulaOnline() {
		return getDefinicoesTutoriaOnline().equals(DefinicoesTutoriaOnlineEnum.DINAMICA) && getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE);
	}
	
	

	
	
	
	
	
	
	
}
