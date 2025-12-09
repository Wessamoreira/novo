/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Philippe
 */
public class AlunosCandidatadosVagaRelVO {
    private Integer codigoPessoa;
    private String aluno;
    private String curso;
    private String identificadorTurma;
    private String cidadeAluno;
    private List<AlunosCandidatadosVagaDadosVagaRelVO> listaAlunosCadidatadosVagaDadosVagaRelVO;

    public Integer getCodigoPessoa() {
        if (codigoPessoa == null) {
            codigoPessoa = 0;
        }
        return codigoPessoa;
    }

    public void setCodigoPessoa(Integer codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
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

    public String getCurso() {
        if (curso == null) {
            curso = "";
        }
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getIdentificadorTurma() {
        if (identificadorTurma == null) {
            identificadorTurma = "";
        }
        return identificadorTurma;
    }

    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
    }
   
    public String getCidadeAluno() {
        if (cidadeAluno == null) {
            cidadeAluno = "";
        }
        return cidadeAluno;
    }

    public void setCidadeAluno(String cidadeAluno) {
        this.cidadeAluno = cidadeAluno;
    }

    public List<AlunosCandidatadosVagaDadosVagaRelVO> getListaAlunosCadidatadosVagaDadosVagaRelVO() {
        if (listaAlunosCadidatadosVagaDadosVagaRelVO == null) {
            listaAlunosCadidatadosVagaDadosVagaRelVO = new ArrayList(0);
        }
        return listaAlunosCadidatadosVagaDadosVagaRelVO;
    }

    public void setListaAlunosCadidatadosVagaDadosVagaRelVO(List<AlunosCandidatadosVagaDadosVagaRelVO> listaAlunosCadidatadosVagaDadosVagaRelVO) {
        this.listaAlunosCadidatadosVagaDadosVagaRelVO = listaAlunosCadidatadosVagaDadosVagaRelVO;
    }

    public JRDataSource getListaAlunosCadidatadosVagaDadosVagaRelVOJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaAlunosCadidatadosVagaDadosVagaRelVO().toArray());
        return jr;
    }

}
