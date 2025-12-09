package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.TipoHorarioEnum;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Turno. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

@XmlRootElement(name = "turnoVO")
public class TurnoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private String horaInicio;
	private Integer nrAulasAntes;
	private Integer nrAulas;
	private Integer duracaoAula;
	private Integer duracaoIntervalo;
	private String horaFinal;
	private String inicioIntervalo;
	private String finalIntervalo;
	private Boolean naoApresentarHorarioVisaoAluno;
	private TipoHorarioEnum tipoHorario;
	private String descricaoTurnoContrato;
	// private Map<Integer, String> horarios;
	private List<TurnoHorarioVO> turnoHorarioDomingo;
	private List<TurnoHorarioVO> turnoHorarioSegunda;
	private List<TurnoHorarioVO> turnoHorarioTerca;
	private List<TurnoHorarioVO> turnoHorarioQuarta;
	private List<TurnoHorarioVO> turnoHorarioQuinta;
	private List<TurnoHorarioVO> turnoHorarioSexta;
	private List<TurnoHorarioVO> turnoHorarioSabado;
	private Boolean considerarHoraAulaSessentaMinutosGeracaoDiario;
	
	// Variavel Usada na clasa RelatorioSEIDecidirControle
	private Boolean filtrarTurnoVO;
	
	private Boolean ocultarHorarioAulaVisaoProfessor;
	private  Boolean selecionado;
	
	public static final long serialVersionUID = 1L;

	public boolean getIsExisteHorarioDomingo() {
		return !getTurnoHorarioDomingo().isEmpty();
	}

	public boolean getIsExisteHorarioSegunda() {
		return !getTurnoHorarioSegunda().isEmpty();
	}

	public boolean getIsExisteHorarioTerca() {
		return !getTurnoHorarioTerca().isEmpty();
	}

	public boolean getIsExisteHorarioQuarta() {
		return !getTurnoHorarioQuarta().isEmpty();
	}

	public boolean getIsExisteHorarioQuinta() {
		return !getTurnoHorarioQuinta().isEmpty();
	}

	public boolean getIsExisteHorarioSexta() {
		return !getTurnoHorarioSexta().isEmpty();
	}

	public boolean getIsExisteHorarioSabado() {
		return !getTurnoHorarioSabado().isEmpty();
	}

	public List<TurnoHorarioVO> getTurnoHorarioDomingo() {
		if (turnoHorarioDomingo == null) {
			turnoHorarioDomingo = new ArrayList<TurnoHorarioVO>(0);
		}
		return turnoHorarioDomingo;
	}

	public void setTurnoHorarioDomingo(List<TurnoHorarioVO> turnoHorarioDomingo) {
		this.turnoHorarioDomingo = turnoHorarioDomingo;
	}

	public List<TurnoHorarioVO> getTurnoHorarioQuarta() {
		if (turnoHorarioQuarta == null) {
			turnoHorarioQuarta = new ArrayList<TurnoHorarioVO>(0);
		}
		return turnoHorarioQuarta;
	}

	public void setTurnoHorarioQuarta(List<TurnoHorarioVO> turnoHorarioQuarta) {
		this.turnoHorarioQuarta = turnoHorarioQuarta;
	}

	public List<TurnoHorarioVO> getTurnoHorarioQuinta() {
		if (turnoHorarioQuinta == null) {
			turnoHorarioQuinta = new ArrayList<TurnoHorarioVO>(0);
		}
		return turnoHorarioQuinta;
	}

	public void setTurnoHorarioQuinta(List<TurnoHorarioVO> turnoHorarioQuinta) {
		this.turnoHorarioQuinta = turnoHorarioQuinta;
	}

	public List<TurnoHorarioVO> getTurnoHorarioSabado() {
		if (turnoHorarioSabado == null) {
			turnoHorarioSabado = new ArrayList<TurnoHorarioVO>(0);
		}
		return turnoHorarioSabado;
	}

	public void setTurnoHorarioSabado(List<TurnoHorarioVO> turnoHorarioSabado) {
		this.turnoHorarioSabado = turnoHorarioSabado;
	}

	public List<TurnoHorarioVO> getTurnoHorarioSegunda() {
		if (turnoHorarioSegunda == null) {
			turnoHorarioSegunda = new ArrayList<TurnoHorarioVO>(0);
		}
		return turnoHorarioSegunda;
	}

	public void setTurnoHorarioSegunda(List<TurnoHorarioVO> turnoHorarioSegunda) {
		this.turnoHorarioSegunda = turnoHorarioSegunda;
	}

	public List<TurnoHorarioVO> getTurnoHorarioSexta() {
		if (turnoHorarioSexta == null) {
			turnoHorarioSexta = new ArrayList<TurnoHorarioVO>(0);
		}
		return turnoHorarioSexta;
	}

	public void setTurnoHorarioSexta(List<TurnoHorarioVO> turnoHorarioSexta) {
		this.turnoHorarioSexta = turnoHorarioSexta;
	}

	public List<TurnoHorarioVO> getTurnoHorarioTerca() {
		if (turnoHorarioTerca == null) {
			turnoHorarioTerca = new ArrayList<TurnoHorarioVO>(0);
		}
		return turnoHorarioTerca;
	}

	public void setTurnoHorarioTerca(List<TurnoHorarioVO> turnoHorarioTerca) {
		this.turnoHorarioTerca = turnoHorarioTerca;
	}

	/**
	 * Construtor padrão da classe <code>Turno</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public TurnoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNome("");
		setHoraInicio("");
		setNrAulasAntes(new Integer(3));
		setNrAulas(new Integer(5));
		setDuracaoAula(new Integer(50));
		setDuracaoIntervalo(new Integer(15));
		setHoraFinal("");
		setInicioIntervalo("");
		setFinalIntervalo("");
	}

	// public static String getHorarioInicioFim(TurnoVO obj, DiaSemana
	// diaSemana, Integer nrAula) {
	// if(obj.getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO) &&
	// (!obj.getIsExisteHorarioDomingo()
	// || !obj.getIsExisteHorarioSegunda() || !obj.getIsExisteHorarioTerca() ||
	// !obj.getIsExisteHorarioQuarta()
	// || !obj.getIsExisteHorarioQuinta() || !obj.getIsExisteHorarioSexta() ||
	// !obj.getIsExisteHorarioSabado())){
	//
	// }
	// // if (obj.getHorarios().isEmpty()) {
	// // obj.executarPreenchimentoHorarioTurno();
	// // }
	// // if (obj.getHorarios().containsKey(nrAula)) {
	// // return obj.getHorarios().get(nrAula);
	// // }
	// return "Horário não informado";
	// }
	//
	// public void executarPreenchimentoHorarioTurno() {
	// int x = 1;
	// String horaInicio = getHoraInicio();
	// String horaFinal = "";
	// while (x <= getNrAulas()) {
	// horaFinal = Uteis.getCalculodeHoraSemIntervalo(horaInicio, 1,
	// getDuracaoAula());
	// getHorarios().put(x, x + "ª Aula (" + horaInicio + " até " + horaFinal +
	// ")");
	// if (x == getNrAulasAntes()) {
	// horaInicio = getFinalIntervalo();
	// getHorarios().put(0, "Intervalo (" + horaFinal + " até " + horaInicio +
	// ")");
	// } else {
	// horaInicio = horaFinal;
	// }
	// x++;
	// }
	// }
	public boolean getIsHorarioFlexivel() {
		if (getTipoHorario().equals(TipoHorarioEnum.HORARIO_FLEXIVEL)) {
			return true;
		}
		return false;
	}

	public boolean getIsHorarioFixo() {
		if (getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO)) {
			return true;
		}
		return false;
	}

	public Integer getDuracaoAula() {
		return (duracaoAula);
	}

	public void setDuracaoAula(Integer duracaoAula) {
		this.duracaoAula = duracaoAula;
	}

	public Integer getNrAulas() {
		return (nrAulas);
	}

	public void setNrAulas(Integer nrAulas) {
		this.nrAulas = nrAulas;
	}

	public String getHoraInicio() {
		return (horaInicio);
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
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

	public Integer getNrAulasAntes() {
		return nrAulasAntes;
	}

	public void setNrAulasAntes(Integer nrAulasAntes) {
		this.nrAulasAntes = nrAulasAntes;
	}

	public Integer getDuracaoIntervalo() {
		return duracaoIntervalo;
	}

	public void setDuracaoIntervalo(Integer duracaoIntervalo) {
		this.duracaoIntervalo = duracaoIntervalo;
	}

	public String getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	public String getInicioIntervalo() {
		return inicioIntervalo;
	}

	public void setInicioIntervalo(String inicioIntervalo) {
		this.inicioIntervalo = inicioIntervalo;
	}

	public String getFinalIntervalo() {
		return finalIntervalo;
	}

	public void setFinalIntervalo(String finalIntervalo) {
		this.finalIntervalo = finalIntervalo;
	}

	// public Map<Integer, String> getHorarios() {
	// if (horarios == null) {
	// horarios = new HashMap<Integer, String>();
	// }
	// return horarios;
	// }
	//
	// public void setHorarios(Map<Integer, String> horarios) {
	// this.horarios = horarios;
	// }
	public TipoHorarioEnum getTipoHorario() {
		if (tipoHorario == null) {
			tipoHorario = TipoHorarioEnum.HORARIO_FLEXIVEL;
		}
		return tipoHorario;
	}

	public void setTipoHorario(TipoHorarioEnum tipoHorario) {
		this.tipoHorario = tipoHorario;
	}

	public Boolean getNaoApresentarHorarioVisaoAluno() {
		if (naoApresentarHorarioVisaoAluno == null) {
			naoApresentarHorarioVisaoAluno = false;
		}
		return naoApresentarHorarioVisaoAluno;
	}

	public void setNaoApresentarHorarioVisaoAluno(Boolean naoApresentarHorarioVisaoAluno) {
		this.naoApresentarHorarioVisaoAluno = naoApresentarHorarioVisaoAluno;
	}

	public Boolean getConsiderarHoraAulaSessentaMinutosGeracaoDiario() {
		if (considerarHoraAulaSessentaMinutosGeracaoDiario == null) {
			considerarHoraAulaSessentaMinutosGeracaoDiario = Boolean.TRUE;
		}
		return considerarHoraAulaSessentaMinutosGeracaoDiario;
	}

	public void setConsiderarHoraAulaSessentaMinutosGeracaoDiario(Boolean considerarHoraAulaSessentaMinutosGeracaoDiario) {
		this.considerarHoraAulaSessentaMinutosGeracaoDiario = considerarHoraAulaSessentaMinutosGeracaoDiario;
	}

	public Boolean getFiltrarTurnoVO() {
		if (filtrarTurnoVO == null) {
			filtrarTurnoVO = false;
		}
		return filtrarTurnoVO;
	}

	public void setFiltrarTurnoVO(Boolean filtrarTurnoVO) {
		this.filtrarTurnoVO = filtrarTurnoVO;
	}

	public String getDescricaoTurnoContrato() {
		if (descricaoTurnoContrato == null) {
			descricaoTurnoContrato = "";
		}
		return descricaoTurnoContrato;
	}

	public void setDescricaoTurnoContrato(String descricaoTurnoContrato) {
		this.descricaoTurnoContrato = descricaoTurnoContrato;
	}

	public Boolean getOcultarHorarioAulaVisaoProfessor() {
		if(ocultarHorarioAulaVisaoProfessor == null) {
			ocultarHorarioAulaVisaoProfessor = false;
		}
		return ocultarHorarioAulaVisaoProfessor;
	}

	public void setOcultarHorarioAulaVisaoProfessor(Boolean ocultarHorarioAulaVisaoProfessor) {
		this.ocultarHorarioAulaVisaoProfessor = ocultarHorarioAulaVisaoProfessor;
	}
	
	
	public Boolean getSelecionado() {
		if(selecionado == null) {
			selecionado =  false;
		}
		return selecionado;
		}
		public void setSelecionado(Boolean selecionado) {
			this.selecionado = selecionado;
		}
	
}
