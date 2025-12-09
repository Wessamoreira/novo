package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class MapaFinanceiroAlunosRelVO {

    private String matricula;
    private String nome;
    private String gradeCurricular;
    private String planoFinanceiro;
    private String turma;

    private List<MapaFinanceiroAlunosRel_ParcelasVO> listaMapaParcelas;

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

    public String getGradeCurricular() {
        return gradeCurricular;
    }

    public void setGradeCurricular(String gradeCurricular) {
        this.gradeCurricular = gradeCurricular;
    }

    public String getPlanoFinanceiro() {
        return planoFinanceiro;
    }

    public void setPlanoFinanceiro(String planoFinanceiro) {
        this.planoFinanceiro = planoFinanceiro;
    }

    public JRDataSource getListaMapaParcelasJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaMapaParcelas().toArray());
        return jr;
    }

    public List<MapaFinanceiroAlunosRel_ParcelasVO> getListaMapaParcelas() {
        if (listaMapaParcelas == null) {
            listaMapaParcelas = new ArrayList<MapaFinanceiroAlunosRel_ParcelasVO>(0);
        }
        return listaMapaParcelas;
    }

    public void setListaMapaParcelas(List<MapaFinanceiroAlunosRel_ParcelasVO> listaMapaParcelas) {
        this.listaMapaParcelas = listaMapaParcelas;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

}
