package webservice.servicos.objetos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.HorarioAlunoDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import webservice.servicos.objetos.enumeradores.OrigemAgendaAlunoEnum;

/**
 * @author Victor Hugo de Paula Costa - 1 de nov de 2016
 *
 */
@XmlRootElement(name = "agendaAluno")
public class AgendaAlunoRSVO {
	/**
	 * @author Victor Hugo de Paula Costa - 1 de nov de 2016
	 */
	private OrigemAgendaAlunoEnum origemAgendaAluno;
	private FeriadoVO feriadoVO;
	private CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO;
	private HorarioAlunoDiaItemVO horarioAlunoDiaItemVO;
	private HorarioProfessorDiaItemVO horarioProfessorDiaItemVO;
	private GoogleMeetVO googleMeetVO;
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	private String dataOrdenacao;
	
	

	@XmlElement(name = "origemAgendaAluno")
	public OrigemAgendaAlunoEnum getOrigemAgendaAluno() {
		if (origemAgendaAluno == null) {
			origemAgendaAluno = OrigemAgendaAlunoEnum.HORARIO_AULA;
		}
		return origemAgendaAluno;
	}

	public void setOrigemAgendaAluno(OrigemAgendaAlunoEnum origemAgendaAluno) {
		this.origemAgendaAluno = origemAgendaAluno;
	}

	@XmlElement(name = "feriado")
	public FeriadoVO getFeriadoVO() {
		if (feriadoVO == null) {
			feriadoVO = new FeriadoVO();
		}
		return feriadoVO;
	}

	public void setFeriadoVO(FeriadoVO feriadoVO) {
		this.feriadoVO = feriadoVO;
	}

	@XmlElement(name = "calendarioEAD")
	public CalendarioAtividadeMatriculaVO getCalendarioAtividadeMatriculaVO() {
		if (calendarioAtividadeMatriculaVO == null) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		}
		return calendarioAtividadeMatriculaVO;
	}

	public void setCalendarioAtividadeMatriculaVO(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO) {
		this.calendarioAtividadeMatriculaVO = calendarioAtividadeMatriculaVO;
	}

	@XmlElement(name = "horarioAula")
	public HorarioAlunoDiaItemVO getHorarioAlunoDiaItemVO() {
		if (horarioAlunoDiaItemVO == null) {
			horarioAlunoDiaItemVO = new HorarioAlunoDiaItemVO();
		}
		return horarioAlunoDiaItemVO;
	}

	public void setHorarioAlunoDiaItemVO(HorarioAlunoDiaItemVO horarioAlunoDiaItemVO) {
		this.horarioAlunoDiaItemVO = horarioAlunoDiaItemVO;
	}
	
	public HorarioProfessorDiaItemVO getHorarioProfessorDiaItemVO() {
		if (horarioProfessorDiaItemVO == null) {
			horarioProfessorDiaItemVO = new HorarioProfessorDiaItemVO();
		}
		return horarioProfessorDiaItemVO;
	}

	public void setHorarioProfessorDiaItemVO(HorarioProfessorDiaItemVO horarioProfessorDiaItemVO) {
		this.horarioProfessorDiaItemVO = horarioProfessorDiaItemVO;
	}
	
	@XmlElement(name = "googleMeetVO")
	public GoogleMeetVO getGoogleMeetVO() {
		if (googleMeetVO == null) {
			googleMeetVO = new GoogleMeetVO();
		}
		return googleMeetVO;
	}

	public void setGoogleMeetVO(GoogleMeetVO googleMeetVO) {
		this.googleMeetVO = googleMeetVO;
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public String getTituloPanelGoogleMeet() {
		if(!getGoogleMeetVO().isGoogleMeetAvulso() && !isMostrarLinkEvento()) {
			return "Confirma a gravação do evento Google Meet?"; 
		}else if(!getGoogleMeetVO().isGoogleMeetAvulso() && isMostrarLinkEvento()) {
			return "Confirma a exclusão do evento Google Meet?";
		}else if(getGoogleMeetVO().isGoogleMeetAvulso() && !isMostrarLinkEvento()) {
			return "Confirma a gravação do evento Google Meet para a Turma de Ead?";
		}else if(getGoogleMeetVO().isGoogleMeetAvulso() && !isMostrarLinkEvento()) {
			return "Confirma a exclusão do evento Google Meet para a Turma de Ead?";
		}
		return "";
	}
	
	public String getCabecarioApresentar() {
		if(getOrigemAgendaAluno().isHorarioAula() && !getHorarioAlunoDiaItemVO().getHorario().isEmpty()) {
			return "Horário: "+getHorarioAlunoDiaItemVO().getNrAula() + "º - "+getHorarioAlunoDiaItemVO().getHorario();
		}else if(getOrigemAgendaAluno().isHorarioAula() && getHorarioAlunoDiaItemVO().getHorario().isEmpty()) {
			return "Horário: "+getHorarioAlunoDiaItemVO().getNrAula() + "º ";
		}else if(getOrigemAgendaAluno().isHorarioProfessor()) {
			return "Horário: "+getHorarioProfessorDiaItemVO().getNrAula() + "º - "+getHorarioProfessorDiaItemVO().getHorario();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getGoogleMeetVO())) {
			return "Horário: "+ getGoogleMeetVO().getHorarioInicio() + " à "+ getGoogleMeetVO().getHorarioTermino();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getSalaAulaBlackboardVO())) {
			return "";
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getCalendarioAtividadeMatriculaVO())) {
			return getCalendarioAtividadeMatriculaVO().getDescricao();
		}else if(getOrigemAgendaAluno().isFeriado()) {
			return "Feriado - " + getFeriadoVO().getDescricao();
		}
		return "";
	}
	
	public void setDataOrdenacao(String dataOrdenacao) {
		this.dataOrdenacao = dataOrdenacao;
	}
	
	public String getDataOrdenacao() {
		if(getOrigemAgendaAluno().isHorarioAula()) {
			return Uteis.getDataAplicandoFormatacao(getHorarioAlunoDiaItemVO().getData(), "ddMMyyy")+getHorarioAlunoDiaItemVO().getHorarioInicio().replaceAll(":", "");
		}else if(getOrigemAgendaAluno().isHorarioProfessor()) {
			return Uteis.getDataAplicandoFormatacao(getHorarioProfessorDiaItemVO().getData(), "ddMMyyy")+getHorarioProfessorDiaItemVO().getHorarioInicio().replaceAll(":", "");
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getGoogleMeetVO())) {
			return Uteis.getDataAplicandoFormatacao(getGoogleMeetVO().getDiaEvento(), "ddMMyyy")+getGoogleMeetVO().getHorarioInicio().replaceAll(":", "");
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && !Uteis.isAtributoPreenchido(getGoogleMeetVO())) {
			return Uteis.getDataAplicandoFormatacao(getCalendarioAtividadeMatriculaVO().getDataInicio(), "ddMMyyyHH:mm")+"0000";
		}else if(getOrigemAgendaAluno().isFeriado()) {
			return Uteis.getDataAplicandoFormatacao(getFeriadoVO().getData(), "ddMMyyy")+"00000000";
		}
		return "";
	}
	
	public String getIconeCalendarioApresentar() {
		if(isEventoHorarioAula()) {
			return "fas fa-clock";
		}else if(isEventoAvaliacaoOnline()) {
			return "fas fa-file-edit";
		}else if(isEventoAtividadeDiscursiva()) {
			return "fas fa-edit";
		}else if(getOrigemAgendaAluno().isFeriado()) {
			return "fas fa-umbrella";
		}
		return "";
	}
	
	public String getTitleCalendarioApresentar() {
		try {
			if(isEventoHorarioAula() && Uteis.isAtributoPreenchido(getGoogleMeetVO()) && getGoogleMeetVO().isGoogleMeetDentroDoLimiteFinalDeRealizacao()) {
				return "Visualizar Google Meet.";
			}else if(isEventoHorarioAula() && Uteis.isAtributoPreenchido(getSalaAulaBlackboardVO())) {
					return "Visualizar Sala Aula Blackboard.";
			}else if(isEventoHorarioAula() && !Uteis.isAtributoPreenchido(getGoogleMeetVO())) {
				return "Ainda não foi Lançado nenhum evento do Google Meet para essa Aula.";
			}else if(isEventoAvaliacaoOnline() && getCalendarioAtividadeMatriculaVO().isCalendarioAtividadeDentroDoPeriodoRealizacao()) {
				return "Realizar Navegação para Avaliação Online";
			}else if(isEventoAvaliacaoOnline() && !getCalendarioAtividadeMatriculaVO().isCalendarioAtividadeDentroDoPeriodoRealizacao()) {
				return "Avaliação Online ainda não esta no Período de Realização";
			}else if(isEventoAtividadeDiscursiva() && getCalendarioAtividadeMatriculaVO().isCalendarioAtividadeDentroDoPeriodoRealizacao()) {
				return "Realizar Navegação para Atividade Disrcursiva";
			}else if(isEventoAtividadeDiscursiva() && !getCalendarioAtividadeMatriculaVO().isCalendarioAtividadeDentroDoPeriodoRealizacao()) {
				return "Atividade Disrcursiva ainda não esta no Período de Realização";
			}else if(getOrigemAgendaAluno().isFeriado()) {
				return "Feriado -" +getFeriadoVO().getDescricao();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getDisciplinaApresentar() {
		if(getOrigemAgendaAluno().isHorarioAula()) {
			return getHorarioAlunoDiaItemVO().getDisciplina().getNome();
		}else if(getOrigemAgendaAluno().isHorarioProfessor()) {
			return getHorarioProfessorDiaItemVO().getDisciplinaVO().getNome();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getGoogleMeetVO())) {
			return getGoogleMeetVO().getDisciplinaVO().getNome();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getSalaAulaBlackboardVO())) {
			return getSalaAulaBlackboardVO().getDisciplinaVO().getNome();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && !Uteis.isAtributoPreenchido(getGoogleMeetVO())) {
			return getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome();
		}
		return "";
	}
	
	public String getTurmaApresentar() {
		if(getOrigemAgendaAluno().isHorarioAula()) {
			return getHorarioAlunoDiaItemVO().getTurma().getIdentificadorTurma();
		}else if(getOrigemAgendaAluno().isHorarioProfessor()) {
			return getHorarioProfessorDiaItemVO().getTurmaVO().getIdentificadorTurma();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getGoogleMeetVO())) {
			return getGoogleMeetVO().getTurmaVO().getIdentificadorTurma();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getSalaAulaBlackboardVO())) {
			return getSalaAulaBlackboardVO().getTurmaVO().getIdentificadorTurma();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getIdentificadorTurma())) {
			return getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getIdentificadorTurma();
		}
		return "";
	}
	
	public boolean isMostarPeriodoRealizacao() {
		return getOrigemAgendaAluno().isCalendarioEad();
	}
	
	
	public String getPeriodoRealizacaoApresentar() {
		if(getOrigemAgendaAluno().isHorarioAula()) {
			return getHorarioAlunoDiaItemVO().getHorario();
		}else if(getOrigemAgendaAluno().isHorarioProfessor()) {
			return getHorarioProfessorDiaItemVO().getHorario();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getGoogleMeetVO())) {
			return getGoogleMeetVO().getHorarioInicio() + " à "+ getGoogleMeetVO().getHorarioTermino();
		}else if(getOrigemAgendaAluno().isCalendarioEad()  && Uteis.isAtributoPreenchido(getCalendarioAtividadeMatriculaVO())) {
			return Uteis.getDataComHora(getCalendarioAtividadeMatriculaVO().getDataInicio()) +" às "+ Uteis.getDataComHora(getCalendarioAtividadeMatriculaVO().getDataFim());
		}
		return "";
	}
	
	public String getTituloLinkEvento() {
		if(isEventoAvaliacaoOnline()) {
			return "Entrar Avaliação Online";
		}else if(isEventoAtividadeDiscursiva()) {
			return "Entrar Atividade Discursiva";
		}
		return "";
	}
	
	public boolean isMostrarLinkEvento()  {
		try {
			return ((isEventoAvaliacaoOnline() && getCalendarioAtividadeMatriculaVO().getIsApresentarBotaoIniciarAvaliacaoOnline())
					|| (isEventoAtividadeDiscursiva() && getCalendarioAtividadeMatriculaVO().isCalendarioAtividadeDentroDoPeriodoRealizacao()));	
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean isPermitirGerarEventoGoogleMeet()  {
		try {
			return (isEventoHorarioAula() && 
					(getHorarioAlunoDiaItemVO().isHorarioAlunoDiaItemDentroDoLimiteFinalDeRealizacao() 
							|| getHorarioProfessorDiaItemVO().isHorarioProfessorDiaItemDentroDoLimiteFinalDeRealizacao()) 
					&& !Uteis.isAtributoPreenchido(getGoogleMeetVO()));	
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public boolean isEventoHorarioAula() {
		return getOrigemAgendaAluno().isHorarioAula() || getOrigemAgendaAluno().isHorarioProfessor() || (getOrigemAgendaAluno().isCalendarioEad() && (Uteis.isAtributoPreenchido(getGoogleMeetVO()) || Uteis.isAtributoPreenchido(getSalaAulaBlackboardVO())));
	}
	
	public boolean isEventoAvaliacaoOnline() {
		return getOrigemAgendaAluno().isCalendarioEad() 
				&& (getCalendarioAtividadeMatriculaVO().getTipoCalendarioAtividade().isPeriodoRealizacaoAvaliacaoOnline()
						|| getCalendarioAtividadeMatriculaVO().getTipoCalendarioAtividade().isPeriodoRealizacaoAvaliacaoOnlineRea());
	}
	
	public boolean isEventoAtividadeDiscursiva() {
		return getOrigemAgendaAluno().isCalendarioEad() && getCalendarioAtividadeMatriculaVO().getTipoCalendarioAtividade().isPeriodoMaximoAtividadeDiscursiva();
	}
	
	public boolean equalsCampoSelecao(AgendaAlunoRSVO obj) {
		return (getOrigemAgendaAluno().equals(OrigemAgendaAlunoEnum.HORARIO_AULA)
				&& obj.getOrigemAgendaAluno().equals(OrigemAgendaAlunoEnum.HORARIO_AULA)
				&& UteisData.getCompararDatas(getHorarioAlunoDiaItemVO().getData(), obj.getHorarioAlunoDiaItemVO().getData())
				&& getHorarioAlunoDiaItemVO().getNrAula().equals(obj.getHorarioAlunoDiaItemVO().getNrAula()))
			||(getOrigemAgendaAluno().equals(OrigemAgendaAlunoEnum.HORARIO_PROFESSOR)
				&& obj.getOrigemAgendaAluno().equals(OrigemAgendaAlunoEnum.HORARIO_PROFESSOR)
				&& getHorarioProfessorDiaItemVO().getCodigo().equals(obj.getHorarioProfessorDiaItemVO().getCodigo()))
			||(getOrigemAgendaAluno().equals(OrigemAgendaAlunoEnum.CALENDARIO_EAD)
					&& obj.getOrigemAgendaAluno().equals(OrigemAgendaAlunoEnum.CALENDARIO_EAD)
					&& getCalendarioAtividadeMatriculaVO().getCodigo().equals(obj.getCalendarioAtividadeMatriculaVO().getCodigo()));
	}
	
	
}
