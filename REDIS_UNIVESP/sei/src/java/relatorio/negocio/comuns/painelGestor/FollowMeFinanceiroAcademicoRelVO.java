package relatorio.negocio.comuns.painelGestor;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.utilitarias.Uteis;


public class FollowMeFinanceiroAcademicoRelVO {
    
    private MesAnoEnum mesAno;
    private String titulo;
    private Integer ano;
    private Integer qtdeAluno;
    private Integer qtdeTurma;
    private Double despesaMes;
    private Double inadimplenciaMes;   
    private Double custoMedioAluno;
    private Double valorMedioParcelaAluno;
    
    
    
    public MesAnoEnum getMesAno() {
        if(mesAno == null){
            mesAno= MesAnoEnum.JANEIRO;
        }
        return mesAno;
    }
    
    public String getMesAnoApresentar() {
        return getMesAno().getMesAbreviado();
    }
    
    public void setMesAno(MesAnoEnum mesAno) {
        this.mesAno = mesAno;
    }
    
    public Integer getAno() {
        if(ano == null){
            ano= 0;
        }
        return ano;
    }
    
    public void setAno(Integer ano) {
        this.ano = ano;
    }
    
    public Integer getQtdeAluno() {
        if(qtdeAluno == null){
            qtdeAluno= 0;
        }
        return qtdeAluno;
    }
    
    public void setQtdeAluno(Integer qtdeAluno) {
        this.qtdeAluno = qtdeAluno;
    }
    
    public Integer getQtdeTurma() {
        if(qtdeTurma == null){
            qtdeTurma= 0;
        }
        return qtdeTurma;
    }
    
    public void setQtdeTurma(Integer qtdeTurma) {
        this.qtdeTurma = qtdeTurma;
    }
    
    public String getDespesaMesFormatado() {
        return Uteis.getDoubleFormatado(getDespesaMes());
    }
    public Double getDespesaMes() {
        if(despesaMes == null){
            despesaMes= 0.0;
        }
        return despesaMes;
    }
    
    public void setDespesaMes(Double despesaMes) {
        this.despesaMes = despesaMes;
    }
    
    public String getInadimplenciaMesFormatado() {
        return Uteis.getDoubleFormatado(getInadimplenciaMes());
    }
    
    public Double getInadimplenciaMes() {
        if(inadimplenciaMes == null){
            inadimplenciaMes= 0.0;
        }
        return inadimplenciaMes;
    }
    
    public void setInadimplenciaMes(Double inadimplenciaMes) {
        this.inadimplenciaMes = inadimplenciaMes;
    }
    
    public String getCustoMedioAlunoFormatado() {
        return Uteis.getDoubleFormatado(getCustoMedioAluno());
    }
    
    public Double getCustoMedioAluno() {
        if(custoMedioAluno == null){
            custoMedioAluno= 0.0;
        }
        return custoMedioAluno;
    }
    
    public void setCustoMedioAluno(Double custoMedioAluno) {
        this.custoMedioAluno = custoMedioAluno;
    }
    
    public String getValorMedioParcelaAlunoFormatado() {
        return Uteis.getDoubleFormatado(getValorMedioParcelaAluno());
    }
    
    public Double getValorMedioParcelaAluno() {
        if(valorMedioParcelaAluno == null){
            valorMedioParcelaAluno= 0.0;
        }
        return valorMedioParcelaAluno;
    }
    
    public void setValorMedioParcelaAluno(Double valorMedioParcelaAluno) {
        this.valorMedioParcelaAluno = valorMedioParcelaAluno;
    }

    
    public String getTitulo() {
        if(titulo == null){
            titulo = "";
        }
        return titulo;
    }

    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    
    

}
