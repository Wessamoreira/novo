package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.basico.PessoaVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ComprovanteRenovacaoMatriculaVO {

    private MatriculaPeriodoVO matriculaPeriodoVO;
    private String ano;
    private String semestre;
    private String periodo;
    private String cidadeDataAtual;
    private Double valorMatricula;
    private Double valorRecebidoMatricula;
    private String valorMatriculaExtenso;
    private String valorRecebidoMatriculaExtenso;
    private String dataPagtoMatricula;
    private List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs;
    private AutorizacaoCursoVO autorizacaoCurso;

    public ComprovanteRenovacaoMatriculaVO() {
    }

    public JRDataSource getListaDocumetacaoMatriculaVOs() {
        JRBeanCollectionDataSource jr = new JRBeanCollectionDataSource(getDocumetacaoMatriculaVOs());
        return jr;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getPeriodo() {
        if (periodo == null) {
            periodo = "";
        }
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        if (matriculaPeriodoVO == null) {
            matriculaPeriodoVO = new MatriculaPeriodoVO();
        }
        return matriculaPeriodoVO;
    }

    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }

    public List<DocumetacaoMatriculaVO> getDocumetacaoMatriculaVOs() {
        if (documetacaoMatriculaVOs == null) {
            documetacaoMatriculaVOs = new ArrayList<DocumetacaoMatriculaVO>(0);
        }
        return documetacaoMatriculaVOs;
    }

    public void setDocumetacaoMatriculaVOs(List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs) {
        this.documetacaoMatriculaVOs = documetacaoMatriculaVOs;
    }

    public String getCidadeDataAtual() {
        if (cidadeDataAtual == null) {
            cidadeDataAtual = "";
        }
        return cidadeDataAtual;
    }

    public void setCidadeDataAtual(String cidadeDataAtual) {
        this.cidadeDataAtual = cidadeDataAtual;
    }

    public Double getValorMatricula() {
        if (valorMatricula == null) {
            valorMatricula = 0.0;
        }
        return valorMatricula;
    }

    public void setValorMatricula(Double valorMatricula) {
        this.valorMatricula = valorMatricula;
    }

    public Double getValorRecebidoMatricula() {
        if (valorRecebidoMatricula == null) {
            valorRecebidoMatricula = 0.0;
        }
        return valorRecebidoMatricula;
    }

    public void setValorRecebidoMatricula(Double valorRecebidoMatricula) {
        this.valorRecebidoMatricula = valorRecebidoMatricula;
    }

    public String getDataPagtoMatricula() {
        if (dataPagtoMatricula == null) {
            dataPagtoMatricula = "";
        }
        return dataPagtoMatricula;
    }

    public void setDataPagtoMatricula(String dataPagtoMatricula) {
        this.dataPagtoMatricula = dataPagtoMatricula;
    }

    public String getValorMatriculaExtenso() {
        if (valorMatriculaExtenso == null) {
            valorMatriculaExtenso = "";
        }
        return valorMatriculaExtenso;
    }

    public void setValorMatriculaExtenso(String valorMatriculaExtenso) {
        this.valorMatriculaExtenso = valorMatriculaExtenso;
    }

    public String getValorRecebidoMatriculaExtenso() {
        if (valorRecebidoMatriculaExtenso == null) {
            valorRecebidoMatriculaExtenso = "";
        }
        return valorRecebidoMatriculaExtenso;
    }

    public void setValorRecebidoMatriculaExtenso(String valorRecebidoMatriculaExtenso) {
        this.valorRecebidoMatriculaExtenso = valorRecebidoMatriculaExtenso;
    }

    public AutorizacaoCursoVO getAutorizacaoCurso() {
        if (autorizacaoCurso == null) {
            autorizacaoCurso = new AutorizacaoCursoVO();
        }
        return autorizacaoCurso;
    }

    public void setAutorizacaoCurso(AutorizacaoCursoVO autorizacaoCurso) {
        this.autorizacaoCurso = autorizacaoCurso;
    }
}
