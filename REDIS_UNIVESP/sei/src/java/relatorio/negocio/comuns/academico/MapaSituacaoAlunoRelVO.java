/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Carlos
 */
public class MapaSituacaoAlunoRelVO {

    private List<DocumetacaoMatriculaVO> listaDocumentacaoVOs;
    private List<HistoricoVO> listaHistoricoVOs;
    private List<ContaReceberVO> listaContaReceberVOs;
    private String aluno;
    private String matricula;
    private String curso;
    private String turma;
    private Integer cargaHorariaCurso;
    private Integer cargaHorariaCumprida;
    private Boolean pendenciaFinanceira;
    private Boolean pendenciaDocumentacao;
    private String cpf;

    public MapaSituacaoAlunoRelVO() {
    }

    public JRDataSource getContaReceberVOs() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaContaReceberVOs().toArray());
        return jr;
    }

    public JRDataSource getHistoricoVOs() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaHistoricoVOs().toArray());
        return jr;
    }

    public JRDataSource getDocumentacaoVOs() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaDocumentacaoVOs().toArray());
        return jr;
    }

    /**
     * @return the listaDocumentacaoAluno
     */
    public List getListaDocumentacaoVOs() {
        if (listaDocumentacaoVOs == null) {
            listaDocumentacaoVOs = new ArrayList(0);
        }
        return listaDocumentacaoVOs;
    }

    /**
     * @param listaDocumentacaoAluno the listaDocumentacaoAluno to set
     */
    public void setListaDocumentacaoVOs(List listaDocumentacaoVOs) {
        this.listaDocumentacaoVOs = listaDocumentacaoVOs;
    }

    /**
     * @return the listaHistoricoAluno
     */
    public List getListaHistoricoVOs() {
        if (listaHistoricoVOs == null) {
            listaHistoricoVOs = new ArrayList(0);
        }
        return listaHistoricoVOs;
    }

    /**
     * @param listaHistoricoAluno the listaHistoricoAluno to set
     */
    public void setListaHistoricoVOs(List listaHistoricoVOs) {
        this.listaHistoricoVOs = listaHistoricoVOs;
    }

    /**
     * @return the listaFinanceiroAluno
     */
    public List getListaContaReceberVOs() {
        if (listaContaReceberVOs == null) {
            listaContaReceberVOs = new ArrayList(0);
        }
        return listaContaReceberVOs;
    }

    /**
     * @param listaFinanceiroAluno the listaFinanceiroAluno to set
     */
    public void setListaContaReceberVOs(List listaContaReceberVOs) {
        this.listaContaReceberVOs = listaContaReceberVOs;
    }

    /**
     * @return the aluno
     */
    public String getAluno() {
        if (aluno == null) {
            aluno = "";
        }
        return aluno;
    }

    /**
     * @param aluno the aluno to set
     */
    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the curso
     */
    public String getCurso() {
        if (curso == null) {
            curso = "";
        }
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(String curso) {
        this.curso = curso;
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
     * @return the cargaHorariaCurso
     */
    public Integer getCargaHorariaCurso() {
        if (cargaHorariaCurso == null) {
            cargaHorariaCurso = 0;
        }
        return cargaHorariaCurso;
    }

    /**
     * @param cargaHorariaCurso the cargaHorariaCurso to set
     */
    public void setCargaHorariaCurso(Integer cargaHorariaCurso) {
        this.cargaHorariaCurso = cargaHorariaCurso;
    }

    /**
     * @return the cargaHorariaCumprida
     */
    public Integer getCargaHorariaCumprida() {
        if (cargaHorariaCumprida == null) {
            cargaHorariaCumprida = 0;
        }
        return cargaHorariaCumprida;
    }

    /**
     * @param cargaHorariaCumprida the cargaHorariaCumprida to set
     */
    public void setCargaHorariaCumprida(Integer cargaHorariaCumprida) {
        this.cargaHorariaCumprida = cargaHorariaCumprida;
    }

    /**
     * @return the semPendenciaFinanceira
     */
    public Boolean getPendenciaFinanceira() {
        if (pendenciaFinanceira == null) {
            pendenciaFinanceira = Boolean.FALSE;
        }
        return pendenciaFinanceira;
    }

    /**
     * @param semPendenciaFinanceira the semPendenciaFinanceira to set
     */
    public void setPendenciaFinanceira(Boolean pendenciaFinanceira) {
        this.pendenciaFinanceira = pendenciaFinanceira;
    }

    /**
     * @return the pendenciaDocumentacao
     */
    public Boolean getPendenciaDocumentacao() {
        if (pendenciaDocumentacao == null) {
            pendenciaDocumentacao = Boolean.FALSE;
        }
        return pendenciaDocumentacao;
    }

    /**
     * @param pendenciaDocumentacao the pendenciaDocumentacao to set
     */
    public void setPendenciaDocumentacao(Boolean pendenciaDocumentacao) {
        this.pendenciaDocumentacao = pendenciaDocumentacao;
    }

	public String getCpf() {
		if(cpf == null){
			 cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
    
    
}
