/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import negocio.comuns.academico.MatriculaVO;

/**
 * 
 * @author otimize-ti
 */
public class EntregaBoletosRelVO {

    private MatriculaVO matriculaVO;
    private String turma;
    private Integer qtdeParcelas;
    private boolean turmaAgrupada;
    private String periodoLetivo;
    private String dataCidadePorExtenso;
    private boolean apresentarCampoData;

    public Integer getQtdeParcelas() {
        if (qtdeParcelas == null) {
            qtdeParcelas = 0;
        }
        return qtdeParcelas;
    }

    public void setQtdeParcelas(Integer qtdeParcelas) {
        this.qtdeParcelas = qtdeParcelas;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public boolean isTurmaAgrupada() {
        return turmaAgrupada;
    }

    public void setTurmaAgrupada(boolean turmaAgrupada) {
        this.turmaAgrupada = turmaAgrupada;
    }

    public String getPeriodoLetivo() {
        if (periodoLetivo == null) {
            periodoLetivo = "";
        }
        return periodoLetivo;
    }

    public void setPeriodoLetivo(String periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public String getDataCidadePorExtenso() {
        if (dataCidadePorExtenso == null) {
            dataCidadePorExtenso = "";
        }
        return dataCidadePorExtenso;
    }

    public void setDataCidadePorExtenso(String dataCidadePorExtenso) {
        this.dataCidadePorExtenso = dataCidadePorExtenso;
    }

    /**
     * @return the apresentarCampoData
     */
    public boolean getApresentarCampoData() {
        return apresentarCampoData;
    }

    /**
     * @param apresentarCampoData the apresentarCampoData to set
     */
    public void setApresentarCampoData(boolean apresentarCampoData) {
        this.apresentarCampoData = apresentarCampoData;
    }
}
