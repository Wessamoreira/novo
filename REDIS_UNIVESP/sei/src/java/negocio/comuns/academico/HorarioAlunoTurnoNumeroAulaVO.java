package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorarioAlunoTurnoNumeroAulaVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1557181293376928098L;
	private Integer numeroAula;	
	private String horarioInicioSegunda;
	private String horarioTerminoSegunda;
	private String horarioInicioTerca;
	private String horarioTerminoTerca;
	private String horarioInicioQuarta;
	private String horarioTerminoQuarta;
	private String horarioInicioQuinta;
	private String horarioTerminoQuinta;
	private String horarioInicioSexta;
	private String horarioTerminoSexta;
	private String horarioInicioSabado;
	private String horarioTerminoSabado;
	private String horarioInicioDomingo;
	private String horarioTerminoDomingo;
	private List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaSegundaVOs;
	private List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaTercaVOs;
	private List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaQuartaVOs;
	private List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaQuintaVOs;
	private List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaSextaVOs;
	private List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaSabadoVOs;
	private List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaDomingoVOs;
	private Boolean choqueHorarioSegunda;
	private String mensagemChoqueHorarioSegunda;
	private Boolean choqueHorarioTerca;
	private String mensagemChoqueHorarioTerca;
	private Boolean choqueHorarioQuarta;
	private String mensagemChoqueHorarioQuarta;
	private Boolean choqueHorarioQuinta;
	private String mensagemChoqueHorarioQuinta;
	private Boolean choqueHorarioSexta;
	private String mensagemChoqueHorarioSexta;
	private Boolean choqueHorarioSabado;
	private String mensagemChoqueHorarioSabado;
	private Boolean choqueHorarioDomingo;
	private String mensagemChoqueHorarioDomingo;
	/**
	 * Usado para validar se existe choque de horário para o aluno, 
	 * nele é armazenado a data da aula em questa para ser comparado com as novas datas 
	 * a serem adicionadas, ou seja se já existir a data adicionada e verificar que a mesma existe então 
	 * deve ser marcado o choque de horário no calendário do aluno.
	 * 
	 */
    private Map<Date, TurmaProfessorDisciplinaVO> dataAulaTurmaProfessorDisciplinaVOs;
    

	public Integer getNumeroAula() {
		if (numeroAula == null) {
			numeroAula = 0;
		}
		return numeroAula;
	}

	public void setNumeroAula(Integer numeroAula) {
		this.numeroAula = numeroAula;
	}

	public String getHorarioInicioSegunda() {
		if (horarioInicioSegunda == null) {
			horarioInicioSegunda = "";
		}
		return horarioInicioSegunda;
	}

	public void setHorarioInicioSegunda(String horarioInicioSegunda) {
		this.horarioInicioSegunda = horarioInicioSegunda;
	}

	public String getHorarioTerminoSegunda() {
		if (horarioTerminoSegunda == null) {
			horarioTerminoSegunda = "";
		}
		return horarioTerminoSegunda;
	}

	public void setHorarioTerminoSegunda(String horarioTerminoSegunda) {
		this.horarioTerminoSegunda = horarioTerminoSegunda;
	}

	public String getHorarioInicioTerca() {
		if (horarioInicioTerca == null) {
			horarioInicioTerca = "";
		}
		return horarioInicioTerca;
	}

	public void setHorarioInicioTerca(String horarioInicioTerca) {
		this.horarioInicioTerca = horarioInicioTerca;
	}

	public String getHorarioTerminoTerca() {
		if (horarioTerminoTerca == null) {
			horarioTerminoTerca = "";
		}
		return horarioTerminoTerca;
	}

	public void setHorarioTerminoTerca(String horarioTerminoTerca) {
		this.horarioTerminoTerca = horarioTerminoTerca;
	}

	public String getHorarioInicioQuarta() {
		if (horarioInicioQuarta == null) {
			horarioInicioQuarta = "";
		}
		return horarioInicioQuarta;
	}

	public void setHorarioInicioQuarta(String horarioInicioQuarta) {
		this.horarioInicioQuarta = horarioInicioQuarta;
	}

	public String getHorarioTerminoQuarta() {
		if (horarioTerminoQuarta == null) {
			horarioTerminoQuarta = "";
		}
		return horarioTerminoQuarta;
	}

	public void setHorarioTerminoQuarta(String horarioTerminoQuarta) {
		this.horarioTerminoQuarta = horarioTerminoQuarta;
	}

	public String getHorarioInicioQuinta() {
		if (horarioInicioQuinta == null) {
			horarioInicioQuinta = "";
		}
		return horarioInicioQuinta;
	}

	public void setHorarioInicioQuinta(String horarioInicioQuinta) {
		this.horarioInicioQuinta = horarioInicioQuinta;
	}

	public String getHorarioTerminoQuinta() {
		if (horarioTerminoQuinta == null) {
			horarioTerminoQuinta = "";
		}
		return horarioTerminoQuinta;
	}

	public void setHorarioTerminoQuinta(String horarioTerminoQuinta) {
		this.horarioTerminoQuinta = horarioTerminoQuinta;
	}

	public String getHorarioInicioSexta() {
		if (horarioInicioSexta == null) {
			horarioInicioSexta = "";
		}
		return horarioInicioSexta;
	}

	public void setHorarioInicioSexta(String horarioInicioSexta) {
		this.horarioInicioSexta = horarioInicioSexta;
	}

	public String getHorarioTerminoSexta() {
		if (horarioTerminoSexta == null) {
			horarioTerminoSexta = "";
		}
		return horarioTerminoSexta;
	}

	public void setHorarioTerminoSexta(String horarioTerminoSexta) {
		this.horarioTerminoSexta = horarioTerminoSexta;
	}

	public String getHorarioInicioSabado() {
		if (horarioInicioSabado == null) {
			horarioInicioSabado = "";
		}
		return horarioInicioSabado;
	}

	public void setHorarioInicioSabado(String horarioInicioSabado) {
		this.horarioInicioSabado = horarioInicioSabado;
	}

	public String getHorarioTerminoSabado() {
		if (horarioTerminoSabado == null) {
			horarioTerminoSabado = "";
		}
		return horarioTerminoSabado;
	}

	public void setHorarioTerminoSabado(String horarioTerminoSabado) {
		this.horarioTerminoSabado = horarioTerminoSabado;
	}

	public String getHorarioInicioDomingo() {
		if (horarioInicioDomingo == null) {
			horarioInicioDomingo = "";
		}
		return horarioInicioDomingo;
	}

	public void setHorarioInicioDomingo(String horarioInicioDomingo) {
		this.horarioInicioDomingo = horarioInicioDomingo;
	}

	public String getHorarioTerminoDomingo() {
		if (horarioTerminoDomingo == null) {
			horarioTerminoDomingo = "";
		}
		return horarioTerminoDomingo;
	}

	public void setHorarioTerminoDomingo(String horarioTerminoDomingo) {
		this.horarioTerminoDomingo = horarioTerminoDomingo;
	}

	public List<TurmaProfessorDisciplinaVO> getTurmaProfessorDisciplinaSegundaVOs() {
		if (turmaProfessorDisciplinaSegundaVOs == null) {
			turmaProfessorDisciplinaSegundaVOs = new ArrayList<TurmaProfessorDisciplinaVO>(0);
		}
		return turmaProfessorDisciplinaSegundaVOs;
	}

	public void setTurmaProfessorDisciplinaSegundaVOs(List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaSegundaVOs) {
		this.turmaProfessorDisciplinaSegundaVOs = turmaProfessorDisciplinaSegundaVOs;
	}

	public List<TurmaProfessorDisciplinaVO> getTurmaProfessorDisciplinaTercaVOs() {
		if (turmaProfessorDisciplinaTercaVOs == null) {
			turmaProfessorDisciplinaTercaVOs = new ArrayList<TurmaProfessorDisciplinaVO>(0);
		}
		return turmaProfessorDisciplinaTercaVOs;
	}

	public void setTurmaProfessorDisciplinaTercaVOs(List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaTercaVOs) {
		this.turmaProfessorDisciplinaTercaVOs = turmaProfessorDisciplinaTercaVOs;
	}

	public List<TurmaProfessorDisciplinaVO> getTurmaProfessorDisciplinaQuartaVOs() {
		if (turmaProfessorDisciplinaQuartaVOs == null) {
			turmaProfessorDisciplinaQuartaVOs = new ArrayList<TurmaProfessorDisciplinaVO>(0);
		}
		return turmaProfessorDisciplinaQuartaVOs;
	}

	public void setTurmaProfessorDisciplinaQuartaVOs(List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaQuartaVOs) {
		this.turmaProfessorDisciplinaQuartaVOs = turmaProfessorDisciplinaQuartaVOs;
	}

	public List<TurmaProfessorDisciplinaVO> getTurmaProfessorDisciplinaQuintaVOs() {
		if (turmaProfessorDisciplinaQuintaVOs == null) {
			turmaProfessorDisciplinaQuintaVOs = new ArrayList<TurmaProfessorDisciplinaVO>(0);
		}
		return turmaProfessorDisciplinaQuintaVOs;
	}

	public void setTurmaProfessorDisciplinaQuintaVOs(List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaQuintaVOs) {
		this.turmaProfessorDisciplinaQuintaVOs = turmaProfessorDisciplinaQuintaVOs;
	}

	public List<TurmaProfessorDisciplinaVO> getTurmaProfessorDisciplinaSextaVOs() {
		if (turmaProfessorDisciplinaSextaVOs == null) {
			turmaProfessorDisciplinaSextaVOs = new ArrayList<TurmaProfessorDisciplinaVO>(0);
		}
		return turmaProfessorDisciplinaSextaVOs;
	}

	public void setTurmaProfessorDisciplinaSextaVOs(List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaSextaVOs) {
		this.turmaProfessorDisciplinaSextaVOs = turmaProfessorDisciplinaSextaVOs;
	}

	public List<TurmaProfessorDisciplinaVO> getTurmaProfessorDisciplinaSabadoVOs() {
		if (turmaProfessorDisciplinaSabadoVOs == null) {
			turmaProfessorDisciplinaSabadoVOs = new ArrayList<TurmaProfessorDisciplinaVO>(0);
		}
		return turmaProfessorDisciplinaSabadoVOs;
	}

	public void setTurmaProfessorDisciplinaSabadoVOs(List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaSabadoVOs) {
		this.turmaProfessorDisciplinaSabadoVOs = turmaProfessorDisciplinaSabadoVOs;
	}

	public List<TurmaProfessorDisciplinaVO> getTurmaProfessorDisciplinaDomingoVOs() {
		if (turmaProfessorDisciplinaDomingoVOs == null) {
			turmaProfessorDisciplinaDomingoVOs = new ArrayList<TurmaProfessorDisciplinaVO>(0);
		}
		return turmaProfessorDisciplinaDomingoVOs;
	}

	public void setTurmaProfessorDisciplinaDomingoVOs(List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaDomingoVOs) {
		this.turmaProfessorDisciplinaDomingoVOs = turmaProfessorDisciplinaDomingoVOs;
	}

	public Boolean getChoqueHorarioSegunda() {
		if (choqueHorarioSegunda == null) {
			choqueHorarioSegunda = false;
		}
		return choqueHorarioSegunda;
	}

	public void setChoqueHorarioSegunda(Boolean choqueHorarioSegunda) {
		this.choqueHorarioSegunda = choqueHorarioSegunda;
	}

	public String getMensagemChoqueHorarioSegunda() {
		if (mensagemChoqueHorarioSegunda == null) {
			mensagemChoqueHorarioSegunda = "";
		}
		return mensagemChoqueHorarioSegunda;
	}

	public void setMensagemChoqueHorarioSegunda(String mensagemChoqueHorarioSegunda) {
		this.mensagemChoqueHorarioSegunda = mensagemChoqueHorarioSegunda;
	}

	public Boolean getChoqueHorarioTerca() {
		if (choqueHorarioTerca == null) {
			choqueHorarioTerca = false;
		}
		return choqueHorarioTerca;
	}

	public void setChoqueHorarioTerca(Boolean choqueHorarioTerca) {
		this.choqueHorarioTerca = choqueHorarioTerca;
	}

	public String getMensagemChoqueHorarioTerca() {
		if (mensagemChoqueHorarioTerca == null) {
			mensagemChoqueHorarioTerca = "";
		}
		return mensagemChoqueHorarioTerca;
	}

	public void setMensagemChoqueHorarioTerca(String mensagemChoqueHorarioTerca) {
		this.mensagemChoqueHorarioTerca = mensagemChoqueHorarioTerca;
	}

	public Boolean getChoqueHorarioQuarta() {
		if (choqueHorarioQuarta == null) {
			choqueHorarioQuarta = false;
		}
		return choqueHorarioQuarta;
	}

	public void setChoqueHorarioQuarta(Boolean choqueHorarioQuarta) {
		this.choqueHorarioQuarta = choqueHorarioQuarta;
	}

	public String getMensagemChoqueHorarioQuarta() {
		if (mensagemChoqueHorarioQuarta == null) {
			mensagemChoqueHorarioQuarta = "";
		}
		return mensagemChoqueHorarioQuarta;
	}

	public void setMensagemChoqueHorarioQuarta(String mensagemChoqueHorarioQuarta) {
		this.mensagemChoqueHorarioQuarta = mensagemChoqueHorarioQuarta;
	}

	public Boolean getChoqueHorarioQuinta() {
		if (choqueHorarioQuinta == null) {
			choqueHorarioQuinta = false;
		}
		return choqueHorarioQuinta;
	}

	public void setChoqueHorarioQuinta(Boolean choqueHorarioQuinta) {
		this.choqueHorarioQuinta = choqueHorarioQuinta;
	}

	public String getMensagemChoqueHorarioQuinta() {
		if (mensagemChoqueHorarioQuinta == null) {
			mensagemChoqueHorarioQuinta = "";
		}
		return mensagemChoqueHorarioQuinta;
	}

	public void setMensagemChoqueHorarioQuinta(String mensagemChoqueHorarioQuinta) {
		this.mensagemChoqueHorarioQuinta = mensagemChoqueHorarioQuinta;
	}

	public Boolean getChoqueHorarioSexta() {
		if (choqueHorarioSexta == null) {
			choqueHorarioSexta = false;
		}
		return choqueHorarioSexta;
	}

	public void setChoqueHorarioSexta(Boolean choqueHorarioSexta) {
		this.choqueHorarioSexta = choqueHorarioSexta;
	}

	public String getMensagemChoqueHorarioSexta() {
		if (mensagemChoqueHorarioSexta == null) {
			mensagemChoqueHorarioSexta = "";
		}
		return mensagemChoqueHorarioSexta;
	}

	public void setMensagemChoqueHorarioSexta(String mensagemChoqueHorarioSexta) {
		this.mensagemChoqueHorarioSexta = mensagemChoqueHorarioSexta;
	}

	public Boolean getChoqueHorarioSabado() {
		if (choqueHorarioSabado == null) {
			choqueHorarioSabado = false;
		}
		return choqueHorarioSabado;
	}

	public void setChoqueHorarioSabado(Boolean choqueHorarioSabado) {
		this.choqueHorarioSabado = choqueHorarioSabado;
	}

	public String getMensagemChoqueHorarioSabado() {
		if (mensagemChoqueHorarioSabado == null) {
			mensagemChoqueHorarioSabado = "";
		}
		return mensagemChoqueHorarioSabado;
	}

	public void setMensagemChoqueHorarioSabado(String mensagemChoqueHorarioSabado) {
		this.mensagemChoqueHorarioSabado = mensagemChoqueHorarioSabado;
	}

	public Boolean getChoqueHorarioDomingo() {
		if (choqueHorarioDomingo == null) {
			choqueHorarioDomingo = false;
		}
		return choqueHorarioDomingo;
	}

	public void setChoqueHorarioDomingo(Boolean choqueHorarioDomingo) {
		this.choqueHorarioDomingo = choqueHorarioDomingo;
	}

	public String getMensagemChoqueHorarioDomingo() {
		if (mensagemChoqueHorarioDomingo == null) {
			mensagemChoqueHorarioDomingo = "";
		}
		return mensagemChoqueHorarioDomingo;
	}

	public void setMensagemChoqueHorarioDomingo(String mensagemChoqueHorarioDomingo) {
		this.mensagemChoqueHorarioDomingo = mensagemChoqueHorarioDomingo;
	}
	
	public String getProfessorDomingoApresentar() {
		if (getTurmaProfessorDisciplinaDomingoVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaDomingoVOs().get(0).getProfessorVO().getNome();
	}

	public String getProfessorSegundaApresentar() {
		if (getTurmaProfessorDisciplinaSegundaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSegundaVOs().get(0).getProfessorVO().getNome();
	}

	public String getProfessorTercaApresentar() {
		if (getTurmaProfessorDisciplinaTercaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaTercaVOs().get(0).getProfessorVO().getNome();
	}

	public String getProfessorQuartaApresentar() {
		if (getTurmaProfessorDisciplinaQuartaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaQuartaVOs().get(0).getProfessorVO().getNome();
	}

	public String getProfessorQuintaApresentar() {
		if (getTurmaProfessorDisciplinaQuintaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaQuintaVOs().get(0).getProfessorVO().getNome();
	}

	public String getProfessorSextaApresentar() {
		if (getTurmaProfessorDisciplinaSextaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSextaVOs().get(0).getProfessorVO().getNome();
	}

	public String getProfessorSabadoApresentar() {
		if (getTurmaProfessorDisciplinaSabadoVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSabadoVOs().get(0).getProfessorVO().getNome();
	}

	public String getDisciplinaDomingoApresentar() {
		if (getTurmaProfessorDisciplinaDomingoVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaDomingoVOs().get(0).getDisciplinaVO().getNome();
	}

	public String getDisciplinaSegundaApresentar() {
		if (getTurmaProfessorDisciplinaSegundaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSegundaVOs().get(0).getDisciplinaVO().getNome();
	}

	public String getDisciplinaTercaApresentar() {
		if (getTurmaProfessorDisciplinaTercaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaTercaVOs().get(0).getDisciplinaVO().getNome();
	}

	public String getDisciplinaQuartaApresentar() {
		if (getTurmaProfessorDisciplinaQuartaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaQuartaVOs().get(0).getDisciplinaVO().getNome();
	}

	public String getDisciplinaQuintaApresentar() {
		if (getTurmaProfessorDisciplinaQuintaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaQuintaVOs().get(0).getDisciplinaVO().getNome();
	}

	public String getDisciplinaSextaApresentar() {
		if (getTurmaProfessorDisciplinaSextaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSextaVOs().get(0).getDisciplinaVO().getNome();
	}

	public String getDisciplinaSabadoApresentar() {
		if (getTurmaProfessorDisciplinaSabadoVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSabadoVOs().get(0).getDisciplinaVO().getNome();
	}
	
	public String getTurmaDomingoApresentar() {
		if (getTurmaProfessorDisciplinaDomingoVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaDomingoVOs().get(0).getTurmaVO().getIdentificadorTurma();
	}

	public String getTurmaSegundaApresentar() {
		if (getTurmaProfessorDisciplinaSegundaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSegundaVOs().get(0).getTurmaVO().getIdentificadorTurma();
	}

	public String getTurmaTercaApresentar() {
		if (getTurmaProfessorDisciplinaTercaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaTercaVOs().get(0).getTurmaVO().getIdentificadorTurma();
	}

	public String getTurmaQuartaApresentar() {
		if (getTurmaProfessorDisciplinaQuartaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaQuartaVOs().get(0).getTurmaVO().getIdentificadorTurma();
	}

	public String getTurmaQuintaApresentar() {
		if (getTurmaProfessorDisciplinaQuintaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaQuintaVOs().get(0).getTurmaVO().getIdentificadorTurma();
	}

	public String getTurmaSextaApresentar() {
		if (getTurmaProfessorDisciplinaSextaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSextaVOs().get(0).getTurmaVO().getIdentificadorTurma();
	}

	public String getTurmaSabadoApresentar() {
		if (getTurmaProfessorDisciplinaSabadoVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSabadoVOs().get(0).getTurmaVO().getIdentificadorTurma();
	}
	
	
	
	public boolean getExisteHorarioLivreDomingo(){
		return !getHorarioInicioDomingo().trim().isEmpty() && getTurmaProfessorDisciplinaDomingoVOs().isEmpty();
	}
	
	public boolean getExisteHorarioLivreSegunda(){
		return !getHorarioInicioSegunda().trim().isEmpty()  && getTurmaProfessorDisciplinaSegundaVOs().isEmpty();
	}
	
	public boolean getExisteHorarioLivreTerca(){
		return !getHorarioInicioTerca().trim().isEmpty()  && getTurmaProfessorDisciplinaTercaVOs().isEmpty();
	}
	
	public boolean getExisteHorarioLivreQuarta(){
		return !getHorarioInicioQuarta().trim().isEmpty()  && getTurmaProfessorDisciplinaQuartaVOs().isEmpty();
	}
	
	public boolean getExisteHorarioLivreQuinta(){
		return !getHorarioInicioQuinta().trim().isEmpty() && getTurmaProfessorDisciplinaQuintaVOs().isEmpty();
	}
	
	public boolean getExisteHorarioLivreSexta(){
		return !getHorarioInicioSexta().trim().isEmpty() && getTurmaProfessorDisciplinaSextaVOs().isEmpty();
	}
	
	public boolean getExisteHorarioLivreSabado(){
		return !getHorarioInicioSabado().trim().isEmpty() && getTurmaProfessorDisciplinaSabadoVOs().isEmpty();
	}

	public String getBackDomingo(){
		return getChoqueHorarioDomingo()? "backgroundBranco" : getExisteHorarioLivreDomingo() ? "backgroundBranco": "backgroundCinza";
	}
	public String getBackSegunda(){
		return getChoqueHorarioSegunda()? "backgroundBranco" : getExisteHorarioLivreSegunda() ? "backgroundBranco": "backgroundCinza";
	}
	public String getBackTerca(){
		return getChoqueHorarioTerca()? "backgroundBranco" : getExisteHorarioLivreTerca() ? "backgroundBranco": "backgroundCinza";
	}
	public String getBackQuarta(){
		return getChoqueHorarioQuarta()? "backgroundBranco" : getExisteHorarioLivreQuarta() ? "backgroundBranco": "backgroundCinza";
	}
	public String getBackQuinta(){
		return getChoqueHorarioQuinta()? "backgroundBranco" : getExisteHorarioLivreQuinta() ? "backgroundBranco": "backgroundCinza";
	}
	public String getBackSexta(){
		return getChoqueHorarioSexta()? "backgroundBranco" : getExisteHorarioLivreSexta() ? "backgroundBranco": "backgroundCinza";
	}
	public String getBackSabado(){
		return getChoqueHorarioSabado()? "backgroundBranco" : getExisteHorarioLivreSabado() ? "backgroundBranco": "backgroundCinza";
	}
	
	public Boolean getExisteVariasDisciplinasSegunda(){
		return getTurmaProfessorDisciplinaSegundaVOs().size() > 1;
	}
	public Boolean getExisteVariasDisciplinasTerca(){
		return getTurmaProfessorDisciplinaTercaVOs().size() > 1;
	}
	public Boolean getExisteVariasDisciplinasQuarta(){
		return getTurmaProfessorDisciplinaQuartaVOs().size() > 1;
	}
	public Boolean getExisteVariasDisciplinasQuinta(){
		return getTurmaProfessorDisciplinaQuintaVOs().size() > 1;
	}
	public Boolean getExisteVariasDisciplinasSexta(){
		return getTurmaProfessorDisciplinaSextaVOs().size() > 1;
	}
	public Boolean getExisteVariasDisciplinasSabado(){
		return getTurmaProfessorDisciplinaSabadoVOs().size() > 1;
	}
	public Boolean getExisteVariasDisciplinasDomingo(){
		return getTurmaProfessorDisciplinaDomingoVOs().size() > 1;
	}
	
	
	public String getSalaDomingoApresentar() {
		if (getTurmaProfessorDisciplinaDomingoVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaDomingoVOs().get(0).getSala().getLocalSala();
	}

	public String getSalaSegundaApresentar() {
		if (getTurmaProfessorDisciplinaSegundaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSegundaVOs().get(0).getSala().getLocalSala();
	}

	public String getSalaTercaApresentar() {
		if (getTurmaProfessorDisciplinaTercaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaTercaVOs().get(0).getSala().getLocalSala();
	}

	public String getSalaQuartaApresentar() {
		if (getTurmaProfessorDisciplinaQuartaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaQuartaVOs().get(0).getSala().getLocalSala();
	}

	public String getSalaQuintaApresentar() {
		if (getTurmaProfessorDisciplinaQuintaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaQuintaVOs().get(0).getSala().getLocalSala();
	}

	public String getSalaSextaApresentar() {
		if (getTurmaProfessorDisciplinaSextaVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSextaVOs().get(0).getSala().getLocalSala();
	}

	public String getSalaSabadoApresentar() {
		if (getTurmaProfessorDisciplinaSabadoVOs().isEmpty()) {
			return "";
		}
		return getTurmaProfessorDisciplinaSabadoVOs().get(0).getSala().getLocalSala();
	}

	/**
	 * @return the dataAulaTurmaProfessorDisciplinaVOs
	 */
	public Map<Date, TurmaProfessorDisciplinaVO> getDataAulaTurmaProfessorDisciplinaVOs() {
		if (dataAulaTurmaProfessorDisciplinaVOs == null) {
			dataAulaTurmaProfessorDisciplinaVOs = new HashMap<Date, TurmaProfessorDisciplinaVO>(0);
		}
		return dataAulaTurmaProfessorDisciplinaVOs;
	}

	/**
	 * @param dataAulaTurmaProfessorDisciplinaVOs the dataAulaTurmaProfessorDisciplinaVOs to set
	 */
	public void setDataAulaTurmaProfessorDisciplinaVOs(Map<Date, TurmaProfessorDisciplinaVO> dataAulaTurmaProfessorDisciplinaVOs) {
		this.dataAulaTurmaProfessorDisciplinaVOs = dataAulaTurmaProfessorDisciplinaVOs;
	}
	
	
}
