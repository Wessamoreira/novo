package negocio.comuns.gsuite;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//import negocio.comuns.academico.ChoqueHorarioAlunoVO;
import negocio.comuns.academico.DisciplinaVO;
//import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.JsonDateDeserializer;
import negocio.comuns.arquitetura.JsonDateSerializer;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
@XmlRootElement(name = "googleMeetVO")
public class GoogleMeetVO extends SuperVO {

	private static final long serialVersionUID = -1101756699340261795L;

	private Integer codigo;
	private ClassroomGoogleVO classroomGoogleVO;
	private PessoaVO professorVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;
	private String idEventoCalendar;
	private String linkGoogleMeet;
	@JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
	private Date diaEvento;
	private String horarioInicio;
	private String horarioTermino;
	
	private Boolean processado;
	private boolean googleMeetAvulso= false;

	// Transiente
	@JsonManagedReference
	private List<GoogleMeetConvidadoVO> googleMeetConvidadoVOs;
//	private List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs;
	@ExcluirJsonAnnotation
	@JsonIgnore
	private Boolean deletarCalendarGoogleMeet;
	@ExcluirJsonAnnotation
	@JsonIgnore
	private boolean eventoAulasSubsequentes = true;
//	@ExcluirJsonAnnotation
//	@JsonIgnore
//	private List<ChoqueHorarioAlunoVO> listaChoqueHorarioAluno;
	@ExcluirJsonAnnotation
	@JsonIgnore
	private List<TurmaDisciplinaVO> listaTurmaDisciplinaChoqueHorarioProfessor;

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	@XmlElement(name = "classroomGoogleVO")
	public ClassroomGoogleVO getClassroomGoogleVO() {
		if (classroomGoogleVO == null) {
			classroomGoogleVO = new ClassroomGoogleVO();
		}
		return classroomGoogleVO;
	}

	public void setClassroomGoogleVO(ClassroomGoogleVO classroomGoogleVO) {
		this.classroomGoogleVO = classroomGoogleVO;
	}

	@XmlElement(name = "professorVO")
	public PessoaVO getProfessorVO() {
		if (professorVO == null) {
			professorVO = new PessoaVO();
		}
		return professorVO;
	}

	public void setProfessorVO(PessoaVO professorVO) {
		this.professorVO = professorVO;
	}

	@XmlElement(name = "turmaVO")
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	@XmlElement(name = "ano")
	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	@XmlElement(name = "semestre")
	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	@XmlElement(name = "idEventoCalendar")
	public String getIdEventoCalendar() {
		if (idEventoCalendar == null) {
			idEventoCalendar = "";
		}
		return idEventoCalendar;
	}

	public void setIdEventoCalendar(String idEventoCalendar) {
		this.idEventoCalendar = idEventoCalendar;
	}

	public List<GoogleMeetConvidadoVO> getGoogleMeetConvidadoVOs() {
		if (googleMeetConvidadoVOs == null) {
			googleMeetConvidadoVOs = new ArrayList<>(0);
		}
		return googleMeetConvidadoVOs;
	}

	public void setGoogleMeetConvidadoVOs(List<GoogleMeetConvidadoVO> googleMeetConvidadeVOs) {
		this.googleMeetConvidadoVOs = googleMeetConvidadeVOs;
	}
	
//	public List<HorarioTurmaDiaItemVO> getHorarioTurmaDiaItemVOs() {
//		if (horarioTurmaDiaItemVOs == null) {
//			horarioTurmaDiaItemVOs = new ArrayList<>(0);
//		}
//		return horarioTurmaDiaItemVOs;
//	}
//
//	public void setHorarioTurmaDiaItemVOs(List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs) {
//		this.horarioTurmaDiaItemVOs = horarioTurmaDiaItemVOs;
//	}
//
//	public List<ChoqueHorarioAlunoVO> getListaChoqueHorarioAluno() {
//		if (listaChoqueHorarioAluno == null) {
//			listaChoqueHorarioAluno = new ArrayList<>();
//		}
//		return listaChoqueHorarioAluno;
//	}
//
//	public void setListaChoqueHorarioAluno(List<ChoqueHorarioAlunoVO> listaChoqueHorarioAluno) {
//		this.listaChoqueHorarioAluno = listaChoqueHorarioAluno;
//	}
//	
//	public Integer getQtdeChoqueHorarioAlunoVOs(){
//		return getListaChoqueHorarioAluno().size();
//	}

	public List<TurmaDisciplinaVO> getListaTurmaDisciplinaChoqueHorarioProfessor() {
		if (listaTurmaDisciplinaChoqueHorarioProfessor == null) {
			listaTurmaDisciplinaChoqueHorarioProfessor = new ArrayList<>();
		}
		return listaTurmaDisciplinaChoqueHorarioProfessor;
	}

	public void setListaTurmaDisciplinaChoqueHorarioProfessor(List<TurmaDisciplinaVO> listaTurmaDisciplinaChoqueHorarioProfessor) {
		this.listaTurmaDisciplinaChoqueHorarioProfessor = listaTurmaDisciplinaChoqueHorarioProfessor;
	}

	public String getResumo() {
		return getTurmaVO().getIdentificadorTurma() + getTurmaVO().getCurso().getNome();
	}

	public int getQuantidadeRecorrencia() {
		return 1;
	}
	

	@XmlElement(name = "linkGoogleMeet")
	public String getLinkGoogleMeet() {
		if (linkGoogleMeet == null) {
			linkGoogleMeet = "";
		}
		return linkGoogleMeet;
	}

	public void setLinkGoogleMeet(String linkgooglemeet) {
		this.linkGoogleMeet = linkgooglemeet;
	}

	public Date getDiaEventoHorarioInicio() {
		if(!Uteis.isAtributoPreenchido(getDiaEvento()) 
				|| !Uteis.isAtributoPreenchido(getHorarioInicio())
					|| !getHorarioTermino().contains(":")
					|| getHorarioTermino().length() != 5) {
			return diaEvento;
		}
		int hora =  Integer.parseInt(getHorarioInicio().substring(0, getHorarioInicio().indexOf(":")));
		int minuto =  Integer.parseInt(getHorarioInicio().substring(getHorarioInicio().indexOf(":")+1, getHorarioInicio().length()));
		return UteisData.getDateTime(getDiaEvento(), hora, minuto, 0);
	}
	
	public Date getDiaEventoHorarioTermino() {
		if(!Uteis.isAtributoPreenchido(getDiaEvento()) 
				|| !Uteis.isAtributoPreenchido(getHorarioTermino())
					|| !getHorarioTermino().contains(":")
					|| getHorarioTermino().length() != 5) {
			return diaEvento;
		}
		int hora =  Integer.parseInt(getHorarioTermino().substring(0, getHorarioTermino().indexOf(":")));
		int minuto =  Integer.parseInt(getHorarioTermino().substring(getHorarioTermino().indexOf(":")+1, getHorarioTermino().length()));
		return UteisData.getDateTime(getDiaEvento(), hora, minuto, 0);
	}
	
	@XmlElement(name = "diaEvento")
	public Date getDiaEvento() {
		if (diaEvento == null) {
			diaEvento = new Date();
		}
		return diaEvento;
	}

	public void setDiaEvento(Date diaEvento) {
		this.diaEvento = diaEvento;
	}

	@XmlElement(name = "horarioInicio")
	public String getHorarioInicio() {
		if (horarioInicio == null) {
			horarioInicio = "";
		}
		return horarioInicio;
	}

	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	@XmlElement(name = "horarioTermino")
	public String getHorarioTermino() {
		if (horarioTermino == null) {
			horarioTermino = "";
		}
		return horarioTermino;
	}

	public void setHorarioTermino(String horarioTermino) {
		this.horarioTermino = horarioTermino;
	}	

	@XmlElement(name = "processado")
	public Boolean getProcessado() {
		if (processado == null) {
			processado = Boolean.FALSE;
		}
		return processado;
	}

	public void setProcessado(Boolean processado) {
		this.processado = processado;
	}
	
	public boolean isGoogleMeetAvulso() {
		return googleMeetAvulso;
	}

	public void setGoogleMeetAvulso(boolean googleMeetAvulso) {
		this.googleMeetAvulso = googleMeetAvulso;
	}

	public Boolean getDeletarCalendarGoogleMeet() {
		if (deletarCalendarGoogleMeet == null) {
			deletarCalendarGoogleMeet = Boolean.FALSE;
		}
		return deletarCalendarGoogleMeet;
	}

	public void setDeletarCalendarGoogleMeet(Boolean deletarCalendarGoogleMeet) {
		this.deletarCalendarGoogleMeet = deletarCalendarGoogleMeet;
	}	
	
	public boolean isEventoAulasSubsequentes() {
		return eventoAulasSubsequentes;
	}

	public void setEventoAulasSubsequentes(boolean eventoAulasSubsequentes) {
		this.eventoAulasSubsequentes = eventoAulasSubsequentes;
	}

	public boolean isGoogleMeetDentroDoLimiteFinalDeRealizacao() throws ParseException {
		return UteisData.validarDataInicialMaiorFinalComHora(getDiaEventoHorarioTermino() , new Date());
	}
	
	public String getHorarioApresentar() {
		return getHorarioInicio() + " à " + getHorarioTermino() ;
	}
	
	public boolean getApresentarLinkEvento() {
		return Uteis.isAtributoPreenchido(getLinkGoogleMeet());
	}
}
