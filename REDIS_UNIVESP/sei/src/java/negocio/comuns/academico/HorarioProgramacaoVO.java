package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class HorarioProgramacaoVO extends SuperVO {

    private String horario;
    private ProgramacaoAulaVO segunda;
    private ProgramacaoAulaVO terca;
    private ProgramacaoAulaVO quarta;
    private ProgramacaoAulaVO quinta;
    private ProgramacaoAulaVO sexta;
    private ProgramacaoAulaVO sabado;
    private ProgramacaoAulaVO domingo;
    public static final long serialVersionUID = 1L;

    /** Creates a new instance of HorarioProgramacao */
    public HorarioProgramacaoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
    }

    public ProgramacaoAulaVO getSegunda() {
        if (segunda == null) {
            segunda = new ProgramacaoAulaVO();
        }
        return segunda;
    }

    public void setSegunda(ProgramacaoAulaVO segunda) {
        this.segunda = segunda;
    }

    public ProgramacaoAulaVO getTerca() {
        if (terca == null) {
            terca = new ProgramacaoAulaVO();
        }
        return terca;
    }

    public void setTerca(ProgramacaoAulaVO terca) {
        this.terca = terca;
    }

    public ProgramacaoAulaVO getQuarta() {
        if (quarta == null) {
            quarta = new ProgramacaoAulaVO();
        }
        return quarta;
    }

    public void setQuarta(ProgramacaoAulaVO quarta) {
        this.quarta = quarta;
    }

    public ProgramacaoAulaVO getQuinta() {
        if (quinta == null) {
            quinta = new ProgramacaoAulaVO();
        }
        return quinta;
    }

    public void setQuinta(ProgramacaoAulaVO quinta) {
        this.quinta = quinta;
    }

    public ProgramacaoAulaVO getSexta() {
        if (sexta == null) {
            sexta = new ProgramacaoAulaVO();
        }
        return sexta;
    }

    public void setSexta(ProgramacaoAulaVO sexta) {
        this.sexta = sexta;
    }

    public ProgramacaoAulaVO getSabado() {
        if (sabado == null) {
            sabado = new ProgramacaoAulaVO();
        }
        return sabado;
    }

    public void setSabado(ProgramacaoAulaVO sabado) {
        this.sabado = sabado;
    }

    public ProgramacaoAulaVO getDomingo() {
        if (domingo == null) {
            domingo = new ProgramacaoAulaVO();
        }
        return domingo;
    }

    public void setDomingo(ProgramacaoAulaVO domingo) {
        this.domingo = domingo;
    }

    public String getHorario() {
        if (horario == null) {
            horario = "";
        }
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
