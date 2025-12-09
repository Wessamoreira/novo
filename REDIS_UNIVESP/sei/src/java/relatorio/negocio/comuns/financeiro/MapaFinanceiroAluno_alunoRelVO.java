/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import relatorio.negocio.comuns.processosel.*;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Rodrigo Araújo
 */
public class MapaFinanceiroAluno_alunoRelVO {

    protected Integer unidadeEnsinoId;
    protected String unidadeEnsino;
    protected String matriculaAluno;
    protected String nomeAluno;
    protected String curso;
    protected String turno;
    protected String matrizCurricular;
    protected String turma;
    protected String planoFinanceiroCurso;
//    protected String descProgressivo;
//    protected String descPlanoFinanceiro;
//    protected String descConvenio;
    protected String semestre;
    protected String ano;
    protected Integer unidadeEnsinoCursoId;
    protected Integer qtdDisciplina;
    protected Integer matriculaPeriodo;
    protected List mapaFinanceiroAluno_contaReceberRelVO;

    // USADO APENAS PARA O RELATORIO SINTETICO
    protected String parcela;
    protected Double valorDia5;
    protected Double valorDia30;
    protected Double valorRecebido;
    protected String dataRecebimento;
    protected Integer contaReceber;
    protected Double descProgressivo;
    protected Double descPlanoFinanceiro;
    protected Double descConvenio;
    protected String parcelaDois;
    protected Double valorDia5Dois;
    protected Double valorDia30Dois;
    protected Double valorRecebidoDois;
    protected String dataRecebimentoDois;
    protected Integer contaReceberDois;
    protected Double descProgressivoDois;
    protected Double descPlanoFinanceiroDois;
    protected Double descConvenioDois;
    
    public MapaFinanceiroAluno_alunoRelVO() {
        inicializarDados();
    }

    public String getUnidadeEnsinoCursoTurno() {
        return unidadeEnsino + curso + turno;
    }

    public void inicializarDados() {
        unidadeEnsino = "";
        curso = "";
        turno = "";
        mapaFinanceiroAluno_contaReceberRelVO = new ArrayList(0);
    }

    public JRDataSource getListaMapaFinanceiroAluno_contaReceberRelVO() {
        JRDataSource jr = new JRBeanArrayDataSource(getMapaFinanceiroAluno_contaReceberRelVO().toArray());
        return jr;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List getMapaFinanceiroAluno_contaReceberRelVO() {
        return mapaFinanceiroAluno_contaReceberRelVO;
    }

    public void setMapaFinanceiroAluno_contaReceberRelVO(List mapaFinanceiroAluno_contaReceberRelVO) {
        this.mapaFinanceiroAluno_contaReceberRelVO = mapaFinanceiroAluno_contaReceberRelVO;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Integer getUnidadeEnsinoId() {
        return unidadeEnsinoId;
    }

    public void setUnidadeEnsinoId(Integer unidadeEnsinoId) {
        this.unidadeEnsinoId = unidadeEnsinoId;
    }

    public Integer getUnidadeEnsinoCursoId() {
        return unidadeEnsinoCursoId;
    }

    public void setUnidadeEnsinoCursoId(Integer unidadeEnsinoCursoId) {
        this.unidadeEnsinoCursoId = unidadeEnsinoCursoId;
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

    /**
     * @return the matriculaAluno
     */
    public String getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = "";
        }
        return matriculaAluno;
    }

    /**
     * @param matriculaAluno the matriculaAluno to set
     */
    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    /**
     * @return the nomeAluno
     */
    public String getNomeAluno() {
        if (nomeAluno == null) {
            nomeAluno = "";
        }
        return nomeAluno;
    }

    /**
     * @param nomeAluno the nomeAluno to set
     */
    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    /**
     * @return the matrizCurricular
     */
    public String getMatrizCurricular() {
        if (matrizCurricular == null) {
            matrizCurricular = "";
        }
        return matrizCurricular;
    }

    /**
     * @param matrizCurricular the matrizCurricular to set
     */
    public void setMatrizCurricular(String matrizCurricular) {
        this.matrizCurricular = matrizCurricular;
    }

    /**
     * @return the planoFinanceiroCurso
     */
    public String getPlanoFinanceiroCurso() {
        if (planoFinanceiroCurso == null) {
            planoFinanceiroCurso = "";
        }
        return planoFinanceiroCurso;
    }

    /**
     * @param planoFinanceiroCurso the planoFinanceiroCurso to set
     */
    public void setPlanoFinanceiroCurso(String planoFinanceiroCurso) {
        this.planoFinanceiroCurso = planoFinanceiroCurso;
    }

//    /**
//     * @return the descProgressivo
//     */
//    public String getDescProgressivo() {
//        if (descProgressivo == null) {
//            descProgressivo = "";
//        }
//        return descProgressivo;
//    }
//
//    /**
//     * @param descProgressivo the descProgressivo to set
//     */
//    public void setDescProgressivo(String descProgressivo) {
//        this.descProgressivo = descProgressivo;
//    }

//    /**
//     * @return the descPlanoFinanceiro
//     */
//    public String getDescPlanoFinanceiro() {
//        if (descPlanoFinanceiro == null) {
//            descPlanoFinanceiro = "";
//        }
//        return descPlanoFinanceiro;
//    }
//
//    /**
//     * @param descPlanoFinanceiro the descPlanoFinanceiro to set
//     */
//    public void setDescPlanoFinanceiro(String descPlanoFinanceiro) {
//        this.descPlanoFinanceiro = descPlanoFinanceiro;
//    }
//
//    /**
//     * @return the descConvenio
//     */
//    public String getDescConvenio() {
//        if (descConvenio == null) {
//            descConvenio = "";
//        }
//        return descConvenio;
//    }
//
//    /**
//     * @param descConvenio the descConvenio to set
//     */
//    public void setDescConvenio(String descConvenio) {
//        this.descConvenio = descConvenio;
//    }
//
    /**
     * @return the qtdDisciplina
     */
    public Integer getQtdDisciplina() {
        if (qtdDisciplina == null) {
            qtdDisciplina = 0;
        }
        return qtdDisciplina;
    }

    /**
     * @param qtdDisciplina the qtdDisciplina to set
     */
    public void setQtdDisciplina(Integer qtdDisciplina) {
        this.qtdDisciplina = qtdDisciplina;
    }

    /**
     * @return the matriculaPeriodo
     */
    public Integer getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = 0;
        }
        return matriculaPeriodo;
    }

    /**
     * @param matriculaPeriodo the matriculaPeriodo to set
     */
    public void setMatriculaPeriodo(Integer matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }


    /**
     * @return the parcela
     */
    public String getParcela() {
        if (parcela == null) {
            parcela = "0/0";
        }
        return parcela;
    }

    /**
     * @param parcela the parcela to set
     */
    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    /**
     * @return the valorDia5
     */
    public Double getValorDia5() {
        if (valorDia5 == null) {
            valorDia5 = 0.0;
        }
        return valorDia5;
    }

    /**
     * @param valorDia5 the valorDia5 to set
     */
    public void setValorDia5(Double valorDia5) {
        this.valorDia5 = valorDia5;
    }

    /**
     * @return the valorDia30
     */
    public Double getValorDia30() {
        if (valorDia30 == null) {
            valorDia30 = 0.0;
        }
        return valorDia30;
    }

    /**
     * @param valorDia30 the valorDia30 to set
     */
    public void setValorDia30(Double valorDia30) {
        this.valorDia30 = valorDia30;
    }

    /**
     * @return the valorRecebido
     */
    public Double getValorRecebido() {
        if (valorRecebido == null) {
            valorRecebido = 0.0;
        }
        return valorRecebido;
    }

    /**
     * @param valorRecebido the valorRecebido to set
     */
    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    /**
     * @return the dataRecebimento
     */
    public String getDataRecebimento() {
        if (dataRecebimento == null) {
            dataRecebimento = "";
        }
        return dataRecebimento;
    }

    /**
     * @param dataRecebimento the dataRecebimento to set
     */
    public void setDataRecebimento(String dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    /**
     * @return the contaReceber
     */
    public Integer getContaReceber() {
        if (contaReceber == null) {
            contaReceber = 0;
        }
        return contaReceber;
    }

    /**
     * @param contaReceber the contaReceber to set
     */
    public void setContaReceber(Integer contaReceber) {
        this.contaReceber = contaReceber;
    }

    /**
     * @return the descProgressivo
     */
    public Double getDescProgressivo() {
        if (descProgressivo == null) {
            descProgressivo = 0.0;
        }
        return descProgressivo;
    }

    /**
     * @param descProgressivo the descProgressivo to set
     */
    public void setDescProgressivo(Double descProgressivo) {
        this.descProgressivo = descProgressivo;
    }

    /**
     * @return the descPlanoFinanceiro
     */
    public Double getDescPlanoFinanceiro() {
        if (descPlanoFinanceiro == null) {
            descPlanoFinanceiro = 0.0;
        }
        return descPlanoFinanceiro;
    }

    /**
     * @param descPlanoFinanceiro the descPlanoFinanceiro to set
     */
    public void setDescPlanoFinanceiro(Double descPlanoFinanceiro) {
        this.descPlanoFinanceiro = descPlanoFinanceiro;
    }

    /**
     * @return the descConvenio
     */
    public Double getDescConvenio() {
        if (descConvenio == null) {
            descConvenio = 0.0;
        }
        return descConvenio;
    }

    /**
     * @param descConvenio the descConvenio to set
     */
    public void setDescConvenio(Double descConvenio) {
        this.descConvenio = descConvenio;
    }

    /**
     * @return the parcelaDois
     */
    public String getParcelaDois() {
        if (parcelaDois == null) {
            parcelaDois = "0/0";
        }
        return parcelaDois;
    }

    /**
     * @param parcelaDois the parcelaDois to set
     */
    public void setParcelaDois(String parcelaDois) {
        this.parcelaDois = parcelaDois;
    }

    /**
     * @return the valorDia5Dois
     */
    public Double getValorDia5Dois() {
        if (valorDia5Dois == null) {
            valorDia5Dois = 0.0;
        }
        return valorDia5Dois;
    }

    /**
     * @param valorDia5Dois the valorDia5Dois to set
     */
    public void setValorDia5Dois(Double valorDia5Dois) {
        this.valorDia5Dois = valorDia5Dois;
    }

    /**
     * @return the valorDia30Dois
     */
    public Double getValorDia30Dois() {
        if (valorDia30Dois == null) {
            valorDia30Dois = 0.0;
        }
        return valorDia30Dois;
    }

    /**
     * @param valorDia30Dois the valorDia30Dois to set
     */
    public void setValorDia30Dois(Double valorDia30Dois) {
        this.valorDia30Dois = valorDia30Dois;
    }

    /**
     * @return the valorRecebidoDois
     */
    public Double getValorRecebidoDois() {
        if (valorRecebidoDois == null) {
            valorRecebidoDois = 0.0;
        }
        return valorRecebidoDois;
    }

    /**
     * @param valorRecebidoDois the valorRecebidoDois to set
     */
    public void setValorRecebidoDois(Double valorRecebidoDois) {
        this.valorRecebidoDois = valorRecebidoDois;
    }

    /**
     * @return the dataRecebimentoDois
     */
    public String getDataRecebimentoDois() {
        if (dataRecebimentoDois == null) {
            dataRecebimentoDois = "";
        }
        return dataRecebimentoDois;
    }

    /**
     * @param dataRecebimentoDois the dataRecebimentoDois to set
     */
    public void setDataRecebimentoDois(String dataRecebimentoDois) {
        this.dataRecebimentoDois = dataRecebimentoDois;
    }

    /**
     * @return the contaReceberDois
     */
    public Integer getContaReceberDois() {
        if (contaReceberDois == null) {
            contaReceberDois = 0;
        }
        return contaReceberDois;
    }

    /**
     * @param contaReceberDois the contaReceberDois to set
     */
    public void setContaReceberDois(Integer contaReceberDois) {
        this.contaReceberDois = contaReceberDois;
    }

    /**
     * @return the descProgressivoDois
     */
    public Double getDescProgressivoDois() {
        if (descProgressivoDois == null) {
            descProgressivoDois = 0.0;
        }
        return descProgressivoDois;
    }

    /**
     * @param descProgressivoDois the descProgressivoDois to set
     */
    public void setDescProgressivoDois(Double descProgressivoDois) {
        this.descProgressivoDois = descProgressivoDois;
    }

    /**
     * @return the descPlanoFinanceiroDois
     */
    public Double getDescPlanoFinanceiroDois() {
        if (descPlanoFinanceiroDois == null) {
            descPlanoFinanceiroDois = 0.0;
        }
        return descPlanoFinanceiroDois;
    }

    /**
     * @param descPlanoFinanceiroDois the descPlanoFinanceiroDois to set
     */
    public void setDescPlanoFinanceiroDois(Double descPlanoFinanceiroDois) {
        this.descPlanoFinanceiroDois = descPlanoFinanceiroDois;
    }

    /**
     * @return the descConvenioDois
     */
    public Double getDescConvenioDois() {
        if (descConvenioDois == null) {
            descConvenioDois = 0.0;
        }
        return descConvenioDois;
    }

    /**
     * @param descConvenioDois the descConvenioDois to set
     */
    public void setDescConvenioDois(Double descConvenioDois) {
        this.descConvenioDois = descConvenioDois;
    }
}
