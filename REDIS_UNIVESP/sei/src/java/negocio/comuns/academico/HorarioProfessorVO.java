package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;

@XmlRootElement(name = "horarioProfessor")
public class HorarioProfessorVO extends SuperVO {

    private Integer codigo;
    private TurnoVO turno;
    private PessoaVO professor;
    private String segunda;
    private String terca;
    private String quarta;
    private String quinta;
    private String sexta;
    private String sabado;
    private String domingo;
    private List<Integer> listaCodigoDisciplina;
    private List<Integer> listaCodigoTurma;
    private List<HorarioProfessorDiaVO> horarioProfessorDiaVOs;
    private List<DisponibilidadeHorarioTurmaProfessorVO> horarioDomingo;
    private List<DisponibilidadeHorarioTurmaProfessorVO> horarioSegunda;
    private List<DisponibilidadeHorarioTurmaProfessorVO> horarioTerca;
    private List<DisponibilidadeHorarioTurmaProfessorVO> horarioQuarta;
    private List<DisponibilidadeHorarioTurmaProfessorVO> horarioQuinta;
    private List<DisponibilidadeHorarioTurmaProfessorVO> horarioSexta;
    private List<DisponibilidadeHorarioTurmaProfessorVO> horarioSabado;
    private String nomeProfessor;
    
    //Atributos para controle
	private Integer minutosInicioTurno;
	private Integer minutosFinalTurno;
	private OperacaoFuncionalidadeVO operacaoFuncionalidadeVO;
	


    public static final long serialVersionUID = 1L;
//    private String semestreVigente;
//    private String anoVigente;

    public String getNomeProfessor() {
        if (nomeProfessor == null) {
            nomeProfessor = "";
        }
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }
    private List<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>> calendarioHorarioAulaVOs;

    public List<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>> getCalendarioHorarioAulaVOs() {
        if (calendarioHorarioAulaVOs == null) {
            calendarioHorarioAulaVOs = new ArrayList<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>>(0);
        }
        return calendarioHorarioAulaVOs;
    }

    public void setCalendarioHorarioAulaVOs(List<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>> calendarioHorarioAulaVOs) {
        this.calendarioHorarioAulaVOs = calendarioHorarioAulaVOs;
    }

    public boolean getIsExisteHorarioDomingo() {
        return !getHorarioDomingo().isEmpty();
    }

    public boolean getIsExisteHorarioSegunda() {
        return !getHorarioSegunda().isEmpty();
    }

    public boolean getIsExisteHorarioTerca() {
        return !getHorarioTerca().isEmpty();
    }

    public boolean getIsExisteHorarioQuarta() {
        return !getHorarioQuarta().isEmpty();
    }

    public boolean getIsExisteHorarioQuinta() {
        return !getHorarioQuinta().isEmpty();
    }

    public boolean getIsExisteHorarioSexta() {
        return !getHorarioSexta().isEmpty();
    }

    public boolean getIsExisteHorarioSabado() {
        return !getHorarioSabado().isEmpty();
    }

    public List<DisponibilidadeHorarioTurmaProfessorVO> getHorarioDomingo() {
        if (horarioDomingo == null) {
            horarioDomingo = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
        }
        return horarioDomingo;
    }

    public void setHorarioDomingo(List<DisponibilidadeHorarioTurmaProfessorVO> horarioDomingo) {
        this.horarioDomingo = horarioDomingo;
    }

    public List<DisponibilidadeHorarioTurmaProfessorVO> getHorarioQuarta() {
        if (horarioQuarta == null) {
            horarioQuarta = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
        }
        return horarioQuarta;
    }

    public void setHorarioQuarta(List<DisponibilidadeHorarioTurmaProfessorVO> horarioQuarta) {
        this.horarioQuarta = horarioQuarta;
    }

    public List<DisponibilidadeHorarioTurmaProfessorVO> getHorarioQuinta() {
        if (horarioQuinta == null) {
            horarioQuinta = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
        }
        return horarioQuinta;
    }

    public void setHorarioQuinta(List<DisponibilidadeHorarioTurmaProfessorVO> horarioQuinta) {
        this.horarioQuinta = horarioQuinta;
    }

    public List<DisponibilidadeHorarioTurmaProfessorVO> getHorarioSabado() {
        if (horarioSabado == null) {
            horarioSabado = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
        }
        return horarioSabado;
    }

    public void setHorarioSabado(List<DisponibilidadeHorarioTurmaProfessorVO> horarioSabado) {
        this.horarioSabado = horarioSabado;
    }

    public List<DisponibilidadeHorarioTurmaProfessorVO> getHorarioSegunda() {
        if (horarioSegunda == null) {
            horarioSegunda = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
        }
        return horarioSegunda;
    }

    public void setHorarioSegunda(List<DisponibilidadeHorarioTurmaProfessorVO> horarioSegunda) {
        this.horarioSegunda = horarioSegunda;
    }

    public List<DisponibilidadeHorarioTurmaProfessorVO> getHorarioSexta() {
        if (horarioSexta == null) {
            horarioSexta = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
        }
        return horarioSexta;
    }

    public void setHorarioSexta(List<DisponibilidadeHorarioTurmaProfessorVO> horarioSexta) {
        this.horarioSexta = horarioSexta;
    }

    public List<DisponibilidadeHorarioTurmaProfessorVO> getHorarioTerca() {
        if (horarioTerca == null) {
            horarioTerca = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
        }
        return horarioTerca;
    }

    public void setHorarioTerca(List<DisponibilidadeHorarioTurmaProfessorVO> horarioTerca) {
        this.horarioTerca = horarioTerca;
    }

    public HorarioProfessorVO() {
        incializarDados();
    }

    public void incializarDados() {
        setCodigo(0);        
        setSegunda("");
        setTerca("");
        setQuarta("");
        setQuinta("");
        setSexta("");
        setSabado("");
        setDomingo("");
    }

    public void adicionarListaCodigoDisciplina(Integer disciplina) {
        for (Integer codDisciplina : getListaCodigoDisciplina()) {
            if (codDisciplina.intValue() == disciplina.intValue()) {
                return;
            }
        }
        getListaCodigoDisciplina().add(disciplina);
    }

    public void adicionarListaCodigoTurma(Integer turma) {
        for (Integer codTurma : getListaCodigoTurma()) {
            if (codTurma.intValue() == turma.intValue()) {
                return;
            }
        }
        getListaCodigoTurma().add(turma);
    }

    public static String montarDadosHorarioProfessor(Integer nrAula, Integer codigoDisciplina, Integer codigoTurma) {
        String dadosHorario = "";
        dadosHorario = (String.valueOf(nrAula) + "[" + String.valueOf(codigoDisciplina) + "]{"
                + String.valueOf(codigoTurma) + "};");
        return dadosHorario;
    }

    public Boolean getDisponibilidadeHorario(Integer nrAula, DiaSemana diaSemana) {
        Integer codigoDisciplina = getCodigoDisciplina(nrAula, diaSemana);
        if (codigoDisciplina.intValue() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean getIndisponibilidadeHorario(Integer nrAula, DiaSemana diaSemana) {
        Integer codigoDisciplina = getCodigoDisciplina(nrAula, diaSemana);
        if (codigoDisciplina.intValue() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<DisponibilidadeHorarioTurmaProfessorVO> consultarListaHorarioDisponivelTurmaProfessorVO(DiaSemana diaSemana) {
        switch (diaSemana) {
            case DOMINGO:
                return getHorarioDomingo();
            case SEGUNGA:
                return getHorarioSegunda();
            case TERCA:
                return getHorarioTerca();
            case QUARTA:
                return getHorarioQuarta();
            case QUINTA:
                return getHorarioQuinta();
            case SEXTA:
                return getHorarioSexta();
            case SABADO:
                return getHorarioSabado();
            default:
                return null;
        }
    }

    public void adicionarListaHorarioDisponivelTurmaProfessorVO(DiaSemana diaSemana, List<DisponibilidadeHorarioTurmaProfessorVO> listaHorario) {
        switch (diaSemana) {
            case DOMINGO: {
                setHorarioDomingo(listaHorario);
                break;
            }
            case SEGUNGA: {
                setHorarioSegunda(listaHorario);
                break;
            }
            case TERCA: {
                setHorarioTerca(listaHorario);
                break;
            }
            case QUARTA: {
                setHorarioQuarta(listaHorario);
                break;
            }
            case QUINTA: {
                setHorarioQuinta(listaHorario);
                break;
            }
            case SEXTA: {
                setHorarioSexta(listaHorario);
                break;
            }
            case SABADO: {
                setHorarioSabado(listaHorario);
                break;
            }
        }
    }

    public void montarAulaJaCadastradasProfessorDia(Date dia) {
        List<DisponibilidadeHorarioTurmaProfessorVO> objs = consultarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(dia));
        for (HorarioProfessorDiaVO obj : getHorarioProfessorDiaVOs()) {
            if (Uteis.getDateHoraFinalDia(obj.getData()).compareTo(Uteis.getDateHoraFinalDia(dia)) == 0) {
                for (DisponibilidadeHorarioTurmaProfessorVO item : objs) {
                    if (item.getHorarioLivre()) {
                        if (obj.getCodigoDisciplina(item.getNrAula()) > 0) {
                            item.setHorarioLivre(false);
                        }
                    }
                }
            }
        }
        adicionarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(dia), objs);
    }

    public String verificaHorarioProfessorVazio(String horario, DiaSemana diaSemana, Integer nrAula) {
        if(horario == null){
            horario = "";
        }
        if (!horario.contains(String.valueOf(nrAula) + "[")) {
            horario = horario + String.valueOf(nrAula) + "[0]{0};";
            setHorarioManipulado(horario, diaSemana);
        }
        return horario;
    }

    //THYAGO
    public String verificaHorarioProfessorVazio(String diaSemana, Integer nrAula) {
        if (!diaSemana.contains(String.valueOf(nrAula) + "[")) {
            diaSemana = diaSemana + String.valueOf(nrAula) + "[0]{0};";
        }
        return diaSemana;
    }

    public Integer getCodigoDisciplina(Integer nrAula, DiaSemana diaSemana) {
        String periodo = "";
        int inicio = 0;
        int fim = 0;
        int tam = 2;
        if (nrAula > 9) {
            tam = 3;
        }
        String horario = getHorarioManipular(diaSemana);
        horario = (verificaHorarioProfessorVazio(horario, diaSemana, nrAula));
        inicio = horario.indexOf(String.valueOf(nrAula) + "[");
        fim = horario.indexOf("]", inicio);
        periodo = (horario.substring(inicio + tam, fim));
        return Integer.parseInt(periodo);
    }

    public Integer getCodigoTurma(Integer nrAula, DiaSemana diaSemana) {
        String periodo = "";
        int inicio = 0;
        int fim = 0;
        int inicioChave = 0;
        String horario = getHorarioManipular(diaSemana);
        horario = (verificaHorarioProfessorVazio(horario, diaSemana, nrAula));
        inicio = horario.indexOf(String.valueOf(nrAula) + "[");
        inicioChave = horario.indexOf("{", inicio);
        fim = horario.indexOf("};", inicioChave);
        periodo = (horario.substring(inicioChave + 1, fim));
        return Integer.parseInt(periodo);
    }

    public String getHorarioManipular(DiaSemana diaSemana) {
        switch (diaSemana) {
            case DOMINGO:
                return getDomingo();
            case SEGUNGA:
                return getSegunda();
            case TERCA:
                return getTerca();
            case QUARTA:
                return getQuarta();
            case QUINTA:
                return getQuinta();
            case SEXTA:
                return getSexta();
            case SABADO:
                return getSabado();
            default:
                return "";
        }
    }

    public void setHorarioManipulado(String horario, DiaSemana diaSemana) {
        switch (diaSemana) {
            case DOMINGO: {
                setDomingo(horario);
                break;
            }
            case SEGUNGA: {
                setSegunda(horario);
                break;
            }
            case TERCA: {
                setTerca(horario);
                break;
            }
            case QUARTA: {
                setQuarta(horario);
                break;
            }
            case QUINTA: {
                setQuinta(horario);
                break;
            }
            case SEXTA: {
                setSexta(horario);
                break;
            }
            case SABADO: {
                setSabado(horario);
                break;
            }
            default: {
                horario = "";
                break;
            }

        }
    }

    public void setCodigoDisciplina(Integer nrAula, DiaSemana diaSemana, Integer codigoDisciplina) {
        int inicio = 0;
        int fim = 0;
        String parte1 = "";
        String parte2 = "";
        String horario = getHorarioManipular(diaSemana);
        horario = (verificaHorarioProfessorVazio(horario, diaSemana, nrAula));
        inicio = horario.indexOf(String.valueOf(nrAula) + "[");
        fim = horario.indexOf("]", inicio);
        parte1 = horario.substring(0, inicio + 2);
        parte2 = horario.substring(fim, horario.length());
        horario = (parte1 + String.valueOf(codigoDisciplina) + parte2);
        setHorarioManipulado(horario, diaSemana);
    }

    public void setCodigoTurma(Integer nrAula, DiaSemana diaSemana, Integer codigoTurma) {
        int inicio = 0;
        int fim = 0;
        String parte1 = "";
        String parte2 = "";
        String horario = getHorarioManipular(diaSemana);
        horario = (verificaHorarioProfessorVazio(horario, diaSemana, nrAula));
        inicio = horario.indexOf(String.valueOf(nrAula) + "[");
        inicio = horario.indexOf("{", inicio);
        fim = horario.indexOf("};", inicio);
        parte1 = horario.substring(0, inicio + 1);
        parte2 = horario.substring(fim, horario.length());
        horario = (parte1 + String.valueOf(codigoTurma) + parte2);
        setHorarioManipulado(horario, diaSemana);
    }

    public void adicinarHorarioProfessorPorDiaPorDia(HorarioProfessorDiaVO obj) {
        for (HorarioProfessorDiaVO objExistente : getHorarioProfessorDiaVOs()) {
            if (Uteis.getDateHoraFinalDia(objExistente.getData()).compareTo(Uteis.getDateHoraFinalDia(obj.getData())) == 0) {
                return;
            }
        }
        getHorarioProfessorDiaVOs().add(obj);
        Ordenacao.ordenarLista(getHorarioProfessorDiaVOs(), "data");

    }

    /**
     * Este método é chamado quando o Horarário da Turma For baseado no DIA.
     *
     * @param obj
     *            = Objeto que contém os horários selecionados na tela pelo
     *            usuario que define qual deles deverá ser utilizado pelo
     *            Horário da Turma
     */
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return turno;
    }

    public void setTurno(TurnoVO turno) {
        this.turno = turno;
    }
    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDomingo() {
        return domingo;
    }

    public void setDomingo(String domingo) {
        this.domingo = domingo;
    }

    public PessoaVO getProfessor() {
    	if(professor == null){
    		professor = new PessoaVO();
    	}
        return professor;
    }

    public void setProfessor(PessoaVO professor) {    	
        this.professor = professor;
    }

    public String getQuarta() {
        return quarta;
    }

    public void setQuarta(String quarta) {
        this.quarta = quarta;
    }

    public String getQuinta() {
        return quinta;
    }

    public void setQuinta(String quinta) {
        this.quinta = quinta;
    }

    public String getSabado() {
        return sabado;
    }

    public void setSabado(String sabado) {
        this.sabado = sabado;
    }

    public String getSegunda() {
        return segunda;
    }

    public void setSegunda(String segunda) {
        this.segunda = segunda;
    }

    public String getSexta() {
        return sexta;
    }

    public void setSexta(String sexta) {
        this.sexta = sexta;
    }

    public String getTerca() {
        return terca;
    }

    public void setTerca(String terca) {
        this.terca = terca;
    }

    public List<Integer> getListaCodigoDisciplina() {
        if (listaCodigoDisciplina == null) {
            listaCodigoDisciplina = new ArrayList<Integer>();
        }
        return listaCodigoDisciplina;
    }

    public void setListaCodigoDisciplina(List<Integer> listaCodigoDisciplina) {
        this.listaCodigoDisciplina = listaCodigoDisciplina;
    }

    public List<Integer> getListaCodigoTurma() {
        if (listaCodigoTurma == null) {
            listaCodigoTurma = new ArrayList<Integer>();
        }
        return listaCodigoTurma;
    }

    public void setListaCodigoTurma(List<Integer> listaCodigoTurma) {
        this.listaCodigoTurma = listaCodigoTurma;
    }

    public List<HorarioProfessorDiaVO> getHorarioProfessorDiaVOs() {
        if (horarioProfessorDiaVOs == null) {
            horarioProfessorDiaVOs = new ArrayList<HorarioProfessorDiaVO>(0);
        }
        return horarioProfessorDiaVOs;
    }

    public void setHorarioProfessorDiaVOs(List<HorarioProfessorDiaVO> horarioProfessorDiaVOs) {
        this.horarioProfessorDiaVOs = horarioProfessorDiaVOs;
    }
//	public String getSemestreVigente() {
//		if(semestreVigente == null){
//			semestreVigente = "";
//		}
//		return semestreVigente;
//	}
//
//	public void setSemestreVigente(String semestreVigente) {
//		this.semestreVigente = semestreVigente;
//	}
//
//	public String getAnoVigente() {
//		if(anoVigente == null){
//			anoVigente = "";
//		}
//		return anoVigente;
//	}
//
//	public void setAnoVigente(String anoVigente) {
//		this.anoVigente = anoVigente;
//	}
//    
	
	public Integer getMinutosInicioTurno() {
		if (minutosInicioTurno == null) {
			minutosInicioTurno = 0;
		}
		return minutosInicioTurno;
	}

	public void setMinutosInicioTurno(Integer minutosInicioTurno) {
		this.minutosInicioTurno = minutosInicioTurno;
	}

	public Integer getMinutosFinalTurno() {
		if (minutosFinalTurno == null)  {
			minutosFinalTurno = 0;
		}
		return minutosFinalTurno;
	}

	public void setMinutosFinalTurno(Integer minutosFinalTurno) {
		this.minutosFinalTurno = minutosFinalTurno;
	}

	public OperacaoFuncionalidadeVO getOperacaoFuncionalidadeVO() {
		if(operacaoFuncionalidadeVO == null){
			operacaoFuncionalidadeVO = new OperacaoFuncionalidadeVO();
		}
		return operacaoFuncionalidadeVO;
	}

	public void setOperacaoFuncionalidadeVO(OperacaoFuncionalidadeVO operacaoFuncionalidadeVO) {
		this.operacaoFuncionalidadeVO = operacaoFuncionalidadeVO;
	}
	
	

	
	
}
