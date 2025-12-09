package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class DadosMatriculaRelVO {

	protected List<MatriculaPeriodoTurmaDisciplinaVO> disciplinaVOs;
	protected List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs;
	protected ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO;
	protected String curso;
	protected String turno;
	protected String matricula;
	protected String cpf;
	protected String aluno;
	protected String usuario;
	protected String mensagemPlanoDeEstudo;
	protected String nomeEmpresa;

	public DadosMatriculaRelVO() {
		setDisciplinaVOs(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setDocumentacaoMatriculaVOs(new ArrayList<DocumetacaoMatriculaVO>(0));
		setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
	}

	public JRDataSource getListaDisciplinaVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getDisciplinaVOs().toArray());
		return jr;
	}

	public JRDataSource getListaDocumentacaoVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getDocumentacaoMatriculaVOs().toArray());
		return jr;
	}
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> getDisciplinaVOs() {
		return disciplinaVOs;
	}

	public void setDisciplinaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> disciplinaVOs) {
		this.disciplinaVOs = disciplinaVOs;
	}

//	public List<DisciplinaVO> getDisciplinaVOs() {
//		return disciplinaVOs;
//	}
//
//	public void setDisciplinaVOs(List<DisciplinaVO> disciplinaVOs) {
//		this.disciplinaVOs = disciplinaVOs;
//	}

	public ProcessoMatriculaCalendarioVO getProcessoMatriculaCalendarioVO() {
		if(processoMatriculaCalendarioVO == null){
			processoMatriculaCalendarioVO = new ProcessoMatriculaCalendarioVO();
		}
		return processoMatriculaCalendarioVO;
	}

	public void setProcessoMatriculaCalendarioVO(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) {
		this.processoMatriculaCalendarioVO = processoMatriculaCalendarioVO;
	}

	public List<DocumetacaoMatriculaVO> getDocumentacaoMatriculaVOs() {
		return documentacaoMatriculaVOs;
	}

	public void setDocumentacaoMatriculaVOs(List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs) {
		this.documentacaoMatriculaVOs = documentacaoMatriculaVOs;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getAluno() {
		return aluno;
	}

	public void setAluno(String aluno) {
		this.aluno = aluno;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getMensagemPlanoDeEstudo() {
		return mensagemPlanoDeEstudo;
	}

	public void setMensagemPlanoDeEstudo(String mensagemPlanoDeEstudo) {
		this.mensagemPlanoDeEstudo = mensagemPlanoDeEstudo;
	}

	public String getNomeEmpresa() {
		if (nomeEmpresa == null) {
			nomeEmpresa = "";
		}
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}
}
