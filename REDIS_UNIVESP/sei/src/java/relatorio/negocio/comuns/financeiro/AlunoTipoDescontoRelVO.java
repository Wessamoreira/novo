/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Philippe
 */
public class AlunoTipoDescontoRelVO {
    private String aluno;
    private String matricula;
    private List<DescontoTipoDescontoRelVO> descontos;

    public List<DescontoTipoDescontoRelVO> getDescontos() {
        if (descontos == null) {
            descontos = new ArrayList<DescontoTipoDescontoRelVO>(0);
        }
        return descontos;
    }

    public void setDescontos(List descontos) {
        this.descontos = descontos;
    }

    public String getAluno() {
        if (aluno == null) {
            aluno = "";
        }
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public JRDataSource getDescontosJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getDescontos().toArray());
        return jr;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
