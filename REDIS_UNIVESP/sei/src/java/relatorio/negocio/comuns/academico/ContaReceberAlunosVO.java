/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;

/**
 *
 * @author OTIMIZE09-04
 */
public class ContaReceberAlunosVO {

    private String nome;
    private String matricula;

    public ContaReceberAlunosVO() {
        setMatricula("");
        setNome("");

    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }





}
