package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

public class DisponibilidadeHorarioProfessorVO extends SuperVO {

    private String horario;
    private String segunda;
    private String terca;
    private String quarta;
    private String quinta;
    private String sexta;
    private String sabado;
    private String domingo;
    private Boolean segundaHorario;
    private Boolean tercaHorario;
    private Boolean quartaHorario;
    private Boolean quintaHorario;
    private Boolean sextaHorario;
    private Boolean sabadoHorario;
    private Boolean domingoHorario;
    private PessoaVO professorSegunda;
    private PessoaVO professorTerca;
    private PessoaVO professorQuarta;
    private PessoaVO professorQuinta;
    private PessoaVO professorSexta;
    private PessoaVO professorSabado;
    private PessoaVO professorDomingo;
    private PessoaVO professorDia;
    private DisciplinaVO disciplinaSegunda;
    private DisciplinaVO disciplinaTerca;
    private DisciplinaVO disciplinaQuarta;
    private DisciplinaVO disciplinaQuinta;
    private DisciplinaVO disciplinaSexta;
    private DisciplinaVO disciplinaSabado;
    private DisciplinaVO disciplinaDomingo;
    private DisciplinaVO disciplinaDia;
    private Integer nrAula;
    private String diaSemana;
    private Date data;
    public static final long serialVersionUID = 1L;

    /** Creates a new instance of HorarioProgramacao */
    public DisponibilidadeHorarioProfessorVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setSegunda("");
        setTerca("");
        setQuarta("");
        setQuinta("");
        setSexta("");
        setSabado("");
        setDomingo("");
        setSabadoHorario(Boolean.FALSE);
        setDomingoHorario(Boolean.FALSE);
        setSegundaHorario(Boolean.FALSE);
        setTercaHorario(Boolean.FALSE);
        setQuartaHorario(Boolean.FALSE);
        setQuintaHorario(Boolean.FALSE);
        setSextaHorario(Boolean.FALSE);
        setNrAula(0);
        setData(new Date());
        setDiaSemana("");
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getNrAula() {
        return nrAula;
    }

    public void setNrAula(Integer nrAula) {
        this.nrAula = nrAula;
    }

    public DisciplinaVO getDisciplinaDomingo() {
        if (disciplinaDomingo == null) {
            disciplinaDomingo = new DisciplinaVO();
        }
        return disciplinaDomingo;
    }

    public void setDisciplinaDomingo(DisciplinaVO disciplinaDomingo) {
        this.disciplinaDomingo = disciplinaDomingo;
    }

    public DisciplinaVO getDisciplinaQuarta() {
        if (disciplinaQuarta == null) {
            disciplinaQuarta = new DisciplinaVO();
        }
        return disciplinaQuarta;
    }

    public void setDisciplinaQuarta(DisciplinaVO disciplinaQuarta) {
        this.disciplinaQuarta = disciplinaQuarta;
    }

    public DisciplinaVO getDisciplinaQuinta() {
        if (disciplinaQuinta == null) {
            disciplinaQuinta = new DisciplinaVO();
        }
        return disciplinaQuinta;
    }

    public void setDisciplinaQuinta(DisciplinaVO disciplinaQuinta) {
        this.disciplinaQuinta = disciplinaQuinta;
    }

    public DisciplinaVO getDisciplinaSabado() {
        if (disciplinaSabado == null) {
            disciplinaSabado = new DisciplinaVO();
        }
        return disciplinaSabado;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void setDisciplinaSabado(DisciplinaVO disciplinaSabado) {
        this.disciplinaSabado = disciplinaSabado;
    }

    public DisciplinaVO getDisciplinaSegunda() {
        if (disciplinaSegunda == null) {
            disciplinaSegunda = new DisciplinaVO();
        }
        return disciplinaSegunda;
    }

    public void setDisciplinaSegunda(DisciplinaVO disciplinaSegunda) {
        this.disciplinaSegunda = disciplinaSegunda;
    }

    public DisciplinaVO getDisciplinaSexta() {
        if (disciplinaSexta == null) {
            disciplinaSexta = new DisciplinaVO();
        }
        return disciplinaSexta;
    }

    public void setDisciplinaSexta(DisciplinaVO disciplinaSexta) {
        this.disciplinaSexta = disciplinaSexta;
    }

    public DisciplinaVO getDisciplinaTerca() {
        if (disciplinaTerca == null) {
            disciplinaTerca = new DisciplinaVO();
        }
        return disciplinaTerca;
    }

    public void setDisciplinaTerca(DisciplinaVO disciplinaTerca) {
        this.disciplinaTerca = disciplinaTerca;
    }

    public PessoaVO getProfessorDomingo() {
        if (professorDomingo == null) {
            professorDomingo = new PessoaVO();
        }
        return professorDomingo;
    }

    public void setProfessorDomingo(PessoaVO professorDomingo) {
        this.professorDomingo = professorDomingo;
    }

    public PessoaVO getProfessorQuarta() {
        if (professorQuarta == null) {
            professorQuarta = new PessoaVO();
        }
        return professorQuarta;
    }

    public void setProfessorQuarta(PessoaVO professorQuarta) {
        this.professorQuarta = professorQuarta;
    }

    public PessoaVO getProfessorQuinta() {
        if (professorQuinta == null) {
            professorQuinta = new PessoaVO();
        }
        return professorQuinta;
    }

    public void setProfessorQuinta(PessoaVO professorQuinta) {
        this.professorQuinta = professorQuinta;
    }

    public PessoaVO getProfessorSabado() {
        if (professorSabado == null) {
            professorSabado = new PessoaVO();
        }
        return professorSabado;
    }

    public void setProfessorSabado(PessoaVO professorSabado) {
        this.professorSabado = professorSabado;
    }

    public PessoaVO getProfessorSegunda() {
        if (professorSegunda == null) {
            professorSegunda = new PessoaVO();
        }
        return professorSegunda;
    }

    public void setProfessorSegunda(PessoaVO professorSegunda) {
        this.professorSegunda = professorSegunda;
    }

    public PessoaVO getProfessorSexta() {
        if (professorSexta == null) {
            professorSexta = new PessoaVO();
        }
        return professorSexta;
    }

    public void setProfessorSexta(PessoaVO professorSexta) {
        this.professorSexta = professorSexta;
    }

    public PessoaVO getProfessorTerca() {
        if (professorTerca == null) {
            professorTerca = new PessoaVO();
        }
        return professorTerca;
    }

    public void setProfessorTerca(PessoaVO professorTerca) {
        this.professorTerca = professorTerca;
    }

    public boolean getSegunda_Situacao() {
        if (getSegunda().equals("")) {
            return false;
        }
        return true;
    }

    public boolean getTerca_Situacao() {
        if (getTerca().equals("")) {
            return false;
        }
        return true;
    }

    public boolean getQuarta_Situacao() {
        if (getQuarta().equals("")) {
            return false;
        }
        return true;
    }

    public boolean getQuinta_Situacao() {
        if (getQuinta().equals("")) {
            return false;
        }
        return true;
    }

    public boolean getSexta_Situacao() {
        if (getSexta().equals("")) {
            return false;
        }
        return true;
    }

    public boolean getSabado_Situacao() {
        if (getSabado().equals("")) {
            return false;
        }
        return true;
    }

    public boolean getDomingo_Situacao() {
        if (getDomingo().equals("")) {
            return false;
        }
        return true;
    }

    public Boolean getDomingoHorario() {
        return domingoHorario;
    }

    public void setDomingoHorario(Boolean domingoHorario) {
        this.domingoHorario = domingoHorario;
    }

    public Boolean getQuartaHorario() {
        return quartaHorario;
    }

    public void setQuartaHorario(Boolean quartaHorario) {
        this.quartaHorario = quartaHorario;
    }

    public Boolean getQuintaHorario() {
        return quintaHorario;
    }

    public void setQuintaHorario(Boolean quintaHorario) {
        this.quintaHorario = quintaHorario;
    }

    public Boolean getSabadoHorario() {
        return sabadoHorario;
    }

    public void setSabadoHorario(Boolean sabadoHorario) {
        this.sabadoHorario = sabadoHorario;
    }

    public Boolean getSegundaHorario() {
        return segundaHorario;
    }

    public void setSegundaHorario(Boolean segundaHorario) {
        this.segundaHorario = segundaHorario;
    }

    public Boolean getSextaHorario() {
        return sextaHorario;
    }

    public void setSextaHorario(Boolean sextaHorario) {
        this.sextaHorario = sextaHorario;
    }

    public Boolean getTercaHorario() {
        return tercaHorario;
    }

    public void setTercaHorario(Boolean tercaHorario) {
        this.tercaHorario = tercaHorario;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getSegunda() {
        return segunda;
    }

    public void setSegunda(String segunda) {
        this.segunda = segunda;
    }

    public String getTerca() {
        return terca;
    }

    public void setTerca(String terca) {
        this.terca = terca;
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

    public String getSexta() {
        return sexta;
    }

    public void setSexta(String sexta) {
        this.sexta = sexta;
    }

    public String getSabado() {
        return sabado;
    }

    public void setSabado(String sabado) {
        this.sabado = sabado;
    }

    public String getDomingo() {
        return domingo;
    }

    public void setDomingo(String domingo) {
        this.domingo = domingo;
    }

    public DisciplinaVO getDisciplinaDia() {
        if (disciplinaDia == null) {
            disciplinaDia = new DisciplinaVO();
        }
        return disciplinaDia;
    }

    public void setDisciplinaDia(DisciplinaVO disciplinaDia) {
        this.disciplinaDia = disciplinaDia;
    }

    public PessoaVO getProfessorDia() {
        if (professorDia == null) {
            professorDia = new PessoaVO();
        }
        return professorDia;
    }

    public void setProfessorDia(PessoaVO professorDia) {
        this.professorDia = professorDia;
    }
}
