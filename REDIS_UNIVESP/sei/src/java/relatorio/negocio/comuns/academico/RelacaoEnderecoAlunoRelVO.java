/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.MatriculaVO;

/**
 * 
 * @author Otimize-TI
 */
public class RelacaoEnderecoAlunoRelVO implements Cloneable {

    private MatriculaVO matricula;
    private String turma;
    private Integer numero;

    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return matricula;
    }

    public void setMatricula(MatriculaVO matricula) {
        this.matricula = matricula;
    }

    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    /**
     * @return the numero
     */
    public Integer getNumero() {
        if (numero == null) {
            numero = 0;
        }
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
