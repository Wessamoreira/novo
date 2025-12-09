/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

/**
 * 
 * @author Otimize-TI
 */
public class DeclaracaoAprovacaoVestVO {

    protected String nome;
    protected String data;
    protected String cpf;
    protected String rg;
    protected String anoMat;
    protected String curso;
    protected Integer inscricao;

    public DeclaracaoAprovacaoVestVO() {
        setNome("");
        setData("");
        setCpf("");
        setRg("");
        setAnoMat("");
        setCurso("");
        setInscricao(0);
    }

    public String getAnoMat() {
        return anoMat;
    }

    public void setAnoMat(String anoMat) {
        this.anoMat = anoMat;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the dataExtenso
     */
    public String getData() {
        return data;
    }

    /**
     * @param dataExtenso the dataExtenso to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the inscricao
     */
    public Integer getInscricao() {
        return inscricao;
    }

    /**
     * @param inscricao the inscricao to set
     */
    public void setInscricao(Integer inscricao) {
        this.inscricao = inscricao;
    }
}
