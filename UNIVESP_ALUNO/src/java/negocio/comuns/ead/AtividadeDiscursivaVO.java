package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.ArtefatoEntregaEnum;
import negocio.comuns.ead.enumeradores.DefinicaoDataEntregaAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.PublicoAlvoAtividadeDiscursivaEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Victor Hugo 17/09/2014
 */
@XmlRootElement(name = "atividadeDiscursivaVO")
public class AtividadeDiscursivaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String enunciado;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private PublicoAlvoAtividadeDiscursivaEnum publicoAlvo;
	private Date dataLiberacao;
	private Date dataLimiteEntrega;
	private String ano;
	private String semestre;
	private ArtefatoEntregaEnum artefatoEntrega;
	private Boolean vincularNotaEspecifica;
	private String notaCorrespondente;
	private UsuarioVO responsavelCadastro;
	private Date dataCadastro;
	private Integer qtdDiasAposInicioLiberar;
	private Integer qtdDiasParaConclusao;
	private List<ArquivoVO>listaMaterialDeApoio;
	private DefinicaoDataEntregaAtividadeDiscursivaEnum definicaoDataEntregaAtividadeDiscursivaEnum;
	/*
	 * 
	 * Inicio Transient
	 */
	private List<AtividadeDiscursivaRespostaAlunoVO> atividadeDiscursivaRepostaAlunoVO;
	private Integer quantidadeNaoRespondido;
	private Integer quantidadeAguardandoAvalProfessor;
	private Integer quantidadeNovaResposta;
	private Integer quantidadeAvaliado;
	private Integer quantidadeInteracao;
	private String anoSemestre;
	private DefinicoesTutoriaOnlineEnum turmaDisciplinaDefinicoesTutoriaOnlineEnum;

	/*
	 * Fim Transient
	 */
	@XmlElement(name="codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name="enunciado")
	public String getEnunciado() {
		if (enunciado == null) {
			enunciado = "";
		}
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	@XmlElement(name="disciplinaVO")
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public Date getDataLiberacao() {
		/*if (dataLiberacao == null) {
			dataLiberacao = new Date();
		}*/
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public Date getDataLimiteEntrega() {
	/*	if (dataLimiteEntrega == null) {
			dataLimiteEntrega = new Date();
		}*/
		return dataLimiteEntrega;
	}

	public void setDataLimiteEntrega(Date dataLimiteEntrega) {
		this.dataLimiteEntrega = dataLimiteEntrega;
	}

	public Boolean getVincularNotaEspecifica() {
		if (vincularNotaEspecifica == null) {
			vincularNotaEspecifica = false;
		}
		return vincularNotaEspecifica;
	}

	public void setVincularNotaEspecifica(Boolean vincularNotaEspecifica) {
		this.vincularNotaEspecifica = vincularNotaEspecifica;
	}

	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual4Digitos();
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


	public List<AtividadeDiscursivaRespostaAlunoVO> getAtividadeDiscursivaRepostaAlunoVO() {
		if (atividadeDiscursivaRepostaAlunoVO == null) {
			atividadeDiscursivaRepostaAlunoVO = new ArrayList<AtividadeDiscursivaRespostaAlunoVO>();
		}
		return atividadeDiscursivaRepostaAlunoVO;
	}

	public void setAtividadeDiscursivaRepostaAlunoVO(List<AtividadeDiscursivaRespostaAlunoVO> atividadeDiscursivaRepostaAlunoVO) {
		this.atividadeDiscursivaRepostaAlunoVO = atividadeDiscursivaRepostaAlunoVO;
	}


	public ArtefatoEntregaEnum getArtefatoEntrega() {
		if (artefatoEntrega == null) {
			artefatoEntrega = ArtefatoEntregaEnum.TEXTUAL;
		}
		return artefatoEntrega;
	}

	public void setArtefatoEntrega(ArtefatoEntregaEnum artefatoEntrega) {
		this.artefatoEntrega = artefatoEntrega;
	}

	public Integer getQuantidadeNaoRespondido() {
		if (quantidadeNaoRespondido == null) {
			quantidadeNaoRespondido = 0;
		}
		return quantidadeNaoRespondido;
	}

	public void setQuantidadeNaoRespondido(Integer quantidadeNaoRespondido) {
		this.quantidadeNaoRespondido = quantidadeNaoRespondido;
	}

	public Integer getQuantidadeAguardandoAvalProfessor() {
		if (quantidadeAguardandoAvalProfessor == null) {
			quantidadeAguardandoAvalProfessor = 0;
		}
		return quantidadeAguardandoAvalProfessor;
	}

	public void setQuantidadeAguardandoAvalProfessor(Integer quantidadeAguardandoAvalProfessor) {
		this.quantidadeAguardandoAvalProfessor = quantidadeAguardandoAvalProfessor;
	}

	public Integer getQuantidadeNovaResposta() {
		if (quantidadeNovaResposta == null) {
			quantidadeNovaResposta = 0;
		}
		return quantidadeNovaResposta;
	}

	public void setQuantidadeNovaResposta(Integer quantidadeNovaResposta) {
		this.quantidadeNovaResposta = quantidadeNovaResposta;
	}

	public Integer getQuantidadeAvaliado() {
		if (quantidadeAvaliado == null) {
			quantidadeAvaliado = 0;
		}
		return quantidadeAvaliado;
	}

	public void setQuantidadeAvaliado(Integer quantidadeAvaliado) {
		this.quantidadeAvaliado = quantidadeAvaliado;
	}

	public String getAnoSemestre() {
		if (anoSemestre == null) {
			anoSemestre = "";
		}
		return getAno() + "/" + getSemestre();
	}

	public void setAnoSemestre(String anoSemestre) {
		this.anoSemestre = anoSemestre;
	}

	public UsuarioVO getResponsavelCadastro() {
		if (responsavelCadastro == null) {
			responsavelCadastro = new UsuarioVO();
		}
		return responsavelCadastro;
	}

	public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public DefinicaoDataEntregaAtividadeDiscursivaEnum getDefinicaoDataEntregaAtividadeDiscursivaEnum() {
		if (definicaoDataEntregaAtividadeDiscursivaEnum == null) {
			definicaoDataEntregaAtividadeDiscursivaEnum = DefinicaoDataEntregaAtividadeDiscursivaEnum.DATA_FIXA;
		}
		return definicaoDataEntregaAtividadeDiscursivaEnum;
	}

	public void setDefinicaoDataEntregaAtividadeDiscursivaEnum(
			DefinicaoDataEntregaAtividadeDiscursivaEnum definicaoDataEntregaAtividadeDiscursivaEnum) {
		this.definicaoDataEntregaAtividadeDiscursivaEnum = definicaoDataEntregaAtividadeDiscursivaEnum;
	}

	public boolean getIsApresentarArtefatoEntregaTextual() {
		if (getArtefatoEntrega().equals(ArtefatoEntregaEnum.TEXTUAL)) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarArtefatoEntregaUploadArquivo() {
		if (getArtefatoEntrega().equals(ArtefatoEntregaEnum.UPLOAD_ARQUIVO)) {
			return true;
		}
		return false;
	}
	
	public boolean getIsApresentarDefinicaoDataFixa() {
		if (getDefinicaoDataEntregaAtividadeDiscursivaEnum().equals(DefinicaoDataEntregaAtividadeDiscursivaEnum.DATA_FIXA)) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarDefinicaoInicioEstudoOnline() {
		if (getDefinicaoDataEntregaAtividadeDiscursivaEnum().equals(DefinicaoDataEntregaAtividadeDiscursivaEnum.INICIO_ESTUDO_ONLINE)) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarAnoVisaoProfessor() {
		if((getPublicoAlvo().getIsTipoTurma() && (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) && getTurmaVO().getCodigo() != 0)
		|| (getPublicoAlvo().getIsTipoAluno() && (getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getSemestral() || getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getAnual()) && getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getCodigo() != 0)){
			return true;
		}
		if(getNovoObj()){
			setAno("");
		}
		return false;
	}

	public boolean getIsApresentarSemestreVisaoProfessor() {
		if((getPublicoAlvo().getIsTipoTurma() && (getTurmaVO().getSemestral()) && getTurmaVO().getCodigo() != 0)
				|| (getPublicoAlvo().getIsTipoAluno() && (getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getSemestral()) && getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getCodigo() != 0)){			
			return true;
		} else {
			if(getNovoObj()){
				setSemestre("");
			}
			return false;
		}
	}

	public String getNotaCorrespondente() {
		if (notaCorrespondente == null) {
			notaCorrespondente = "";
		}
		return notaCorrespondente;
	}

	public void setNotaCorrespondente(String notaCorrespondente) {
		this.notaCorrespondente = notaCorrespondente;
	}

	/**
	 * @return the matriculaPeriodoTurmaDisciplinaVO
	 */
	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	/**
	 * @param matriculaPeriodoTurmaDisciplinaVO the matriculaPeriodoTurmaDisciplinaVO to set
	 */
	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	/**
	 * @return the publicoAlvo
	 */
	public PublicoAlvoAtividadeDiscursivaEnum getPublicoAlvo() {
		if (publicoAlvo == null) {
			publicoAlvo = PublicoAlvoAtividadeDiscursivaEnum.TURMA;
		}
		return publicoAlvo;
	}

	/**
	 * @param publicoAlvo the publicoAlvo to set
	 */
	public void setPublicoAlvo(PublicoAlvoAtividadeDiscursivaEnum publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}
	
	
	public Integer getQtdDiasAposInicioLiberar() {
		return qtdDiasAposInicioLiberar;
	}

	public void setQtdDiasAposInicioLiberar(Integer qtdDiasAposInicioLiberar) {
		this.qtdDiasAposInicioLiberar = qtdDiasAposInicioLiberar;
	}

	public Integer getQtdDiasParaConclusao() {
		return qtdDiasParaConclusao;
	}

	public void setQtdDiasParaConclusao(Integer qtdDiasParaConclusao) {
		this.qtdDiasParaConclusao = qtdDiasParaConclusao;
	}

	public DefinicoesTutoriaOnlineEnum getTurmaDisciplinaDefinicoesTutoriaOnlineEnum() {	
		if(turmaDisciplinaDefinicoesTutoriaOnlineEnum == null){
			turmaDisciplinaDefinicoesTutoriaOnlineEnum = DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA;
		}
		return turmaDisciplinaDefinicoesTutoriaOnlineEnum;
	}

	public void setTurmaDisciplinaDefinicoesTutoriaOnlineEnum(DefinicoesTutoriaOnlineEnum turmaDisciplinaDefinicoesTutoriaOnlineEnum) {
		this.turmaDisciplinaDefinicoesTutoriaOnlineEnum = turmaDisciplinaDefinicoesTutoriaOnlineEnum;
	}
	
	public List<ArquivoVO> getListaMaterialDeApoio() {
		if(listaMaterialDeApoio == null){
			listaMaterialDeApoio = new ArrayList<ArquivoVO>();
		}
		return listaMaterialDeApoio;
	}

	public void setListaMaterialDeApoio(List<ArquivoVO> listaMaterialDeApoio) {
		this.listaMaterialDeApoio = listaMaterialDeApoio;
	}

	public Integer getQuantidadeInteracao() {
		if (quantidadeInteracao == null) {
			quantidadeInteracao = 0;
		}
		return quantidadeInteracao;
	}

	public void setQuantidadeInteracao(Integer quantidadeInteracao) {
		this.quantidadeInteracao = quantidadeInteracao;
	}
	
	public String getDataLimiteEntregaApresentar() {
		return (Uteis.getData(getDataLimiteEntrega()));
	}
}
