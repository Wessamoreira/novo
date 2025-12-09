package negocio.comuns.gsuite;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

@XmlRootElement(name = "classroomGoogleVO")
public class ClassroomGoogleVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4359491416402755682L;
	private Integer codigo;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;
	private String linkClassroom;
	private String idClassRoomGoogle;
	private String idCalendario;
	private String idTurma;
	private String emailTurma;
	private List<PessoaGsuiteVO> classroomTeacherVOs;
	private List<PessoaGsuiteVO> classroomStudentVOs;
	private PessoaVO professorEad;	
	private boolean selecionado = false;
	
	public ClassroomGoogleVO() {
		super();
	}
	
	public ClassroomGoogleVO(TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre) {
		super();
		this.turmaVO = turmaVO;
		this.disciplinaVO = disciplinaVO;
		this.ano = ano;
		this.semestre = semestre;
	}
	
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
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

	
	public String getIdClassRoomGoogle() {
		if (idClassRoomGoogle == null) {
			idClassRoomGoogle = "";
		}
		return idClassRoomGoogle;
	}

	public void setIdClassRoomGoogle(String idClassRoomGoogle) {
		this.idClassRoomGoogle = idClassRoomGoogle;
	}
	
	public String getIdTurma() {
		if (idTurma == null) {
			idTurma = "";
		}
		return idTurma;
	}

	public void setIdTurma(String idTurma) {
		this.idTurma = idTurma;
	}

	public String getEmailTurma() {
		if (emailTurma == null) {
			emailTurma = "";
		}
		return emailTurma;
	}

	public void setEmailTurma(String emailTurma) {
		this.emailTurma = emailTurma;
	}


	public String getIdCalendario() {
		if (idCalendario == null) {
			idCalendario = "";
		}
		return idCalendario;
	}

	public void setIdCalendario(String idCalendario) {
		this.idCalendario = idCalendario;
	}

	@XmlElement(name = "linkClassroom")
	public String getLinkClassroom() {
		if (linkClassroom == null) {
			linkClassroom = "";
		}
		return linkClassroom;
	}

	public void setLinkClassroom(String linkClassroom) {
		this.linkClassroom = linkClassroom;
	}	


	public List<PessoaGsuiteVO> getClassroomTeacherVOs() {
		if (classroomTeacherVOs == null) {
			classroomTeacherVOs = new ArrayList<>();
		}
		return classroomTeacherVOs;
	}

	public void setClassroomTeacherVOs(List<PessoaGsuiteVO> classroomTeacherVOs) {
		this.classroomTeacherVOs = classroomTeacherVOs;
	}	
	
	public List<PessoaGsuiteVO> getClassroomStudentVOs() {
		if (classroomStudentVOs == null) {
			classroomStudentVOs = new ArrayList<>();
		}
		return classroomStudentVOs;
	}

	public void setClassroomStudentVOs(List<PessoaGsuiteVO> classroomStudentVOs) {
		this.classroomStudentVOs = classroomStudentVOs;
	}
	
	public PessoaVO getProfessorEad() {
		if(professorEad == null) {
			professorEad = new PessoaVO();
		}
		return professorEad;
	}

	public void setProfessorEad(PessoaVO professorEad) {
		this.professorEad = professorEad;
	}
	
	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

}
