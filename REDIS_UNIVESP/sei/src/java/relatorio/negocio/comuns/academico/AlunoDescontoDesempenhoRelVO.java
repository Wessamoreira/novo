/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;

/**
 *
 * @author Carlos
 */
public class AlunoDescontoDesempenhoRelVO {

    private MatriculaVO matriculaVO;
    private TurmaVO turmaVO;
    private Integer totalDisciplinas;
    private Integer totalDisciplinasReprovadas;

    public AlunoDescontoDesempenhoRelVO(){
        setMatriculaVO(new MatriculaVO());
        setTurmaVO(new TurmaVO());
    }

    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    /**
     * @return the turmaVO
     */
    public TurmaVO getTurmaVO() {
        if(turmaVO == null){
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    /**
     * @param turmaVO the turmaVO to set
     */
    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    /**
     * @return the totalDisciplinas
     */
    public Integer getTotalDisciplinas() {
        if(totalDisciplinas == null){
            totalDisciplinas = 0;
        }
        return totalDisciplinas;
    }

    /**
     * @param totalDisciplinas the totalDisciplinas to set
     */
    public void setTotalDisciplinas(Integer totalDisciplinas) {
        this.totalDisciplinas = totalDisciplinas;
    }

    /**
     * @return the totalDisciplinasReprovadas
     */
    public Integer getTotalDisciplinasReprovadas() {
        if(totalDisciplinasReprovadas == null){
            totalDisciplinasReprovadas = 0;
        }
        return totalDisciplinasReprovadas;
    }

    /**
     * @param totalDisciplinasReprovadas the totalDisciplinasReprovadas to set
     */
    public void setTotalDisciplinasReprovadas(Integer totalDisciplinasReprovadas) {
        this.totalDisciplinasReprovadas = totalDisciplinasReprovadas;
    }
}
