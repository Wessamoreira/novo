/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package relatorio.negocio.comuns.processosel;

/**
 * 
 * @author Philippe
 */
public class CartaoRespostaRelVO {

    private Integer inscricao;
    private String nomeAluno;
    private String curso;
    private String turno;
    private String sala;
    private String codigoBarra;
    private String dataProva;

    public Integer getInscricao() {
        if (inscricao == null) {
            inscricao = 0000;
        }
        return inscricao;
    }

    public void setInscricao(Integer inscricao) {
        this.inscricao = inscricao;
    }

    /**
     * @return the nomeAluno
     */
    public String getNomeAluno() {
        return nomeAluno;
    }

    /**
     * @param nomeAluno the nomeAluno to set
     */
    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    /**
     * @return the codigoBarra
     */
    public String getCodigoBarra() {
        return codigoBarra;
    }

    /**
     * @param codigoBarra the codigoBarra to set
     */
    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    /**
     * @return the curso
     */
    public String getCurso() {
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDataProva() {
        if (dataProva == null) {
            dataProva = "";
        }
        return dataProva;
    }

    public void setDataProva(String dataProva) {
        this.dataProva = dataProva;
    }

    public String getTurno() {
        if (turno == null) {
            turno = "";
        }
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getSala() {
        if (sala == null) {
            sala = "";
        }
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

}
