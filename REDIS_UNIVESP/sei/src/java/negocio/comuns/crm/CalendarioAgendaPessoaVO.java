package negocio.comuns.crm;

import java.util.Date;

import negocio.comuns.utilitarias.dominios.DiaSemana;


public class CalendarioAgendaPessoaVO {
    
    private Date dataCompromisso;
    private DiaSemana diaSemana;
    private Integer qtdeCompromissoNaoRealizado;
    private Integer qtdeCompromissoARealizar;
    private Integer consultor;
    private String styleClass;
    
    
    
    public CalendarioAgendaPessoaVO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CalendarioAgendaPessoaVO(Date dataCompromisso, DiaSemana diaSemana, Integer consultor,String styleClass) {
        super();
        this.dataCompromisso = dataCompromisso;
        this.diaSemana = diaSemana;
        this.consultor = consultor;
        this.styleClass = styleClass;
    }
    
    public Date getDataCompromisso() {
        return dataCompromisso;
    }
    
    public void setDataCompromisso(Date dataCompromisso) {
        this.dataCompromisso = dataCompromisso;
    }
    
    public DiaSemana getDiaSemana() {
        return diaSemana;
    }
    
    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }
    
    public Integer getQtdeCompromissoNaoRealizado() {
        if(qtdeCompromissoNaoRealizado == null){
            qtdeCompromissoNaoRealizado = 0;
        }
        return qtdeCompromissoNaoRealizado;
    }
    
    public void setQtdeCompromissoNaoRealizado(Integer qtdeCompromissoNaoRealizado) {
        this.qtdeCompromissoNaoRealizado = qtdeCompromissoNaoRealizado;
    }
    
    public Integer getQtdeCompromissoARealizar() {
        if(qtdeCompromissoARealizar == null){
            qtdeCompromissoARealizar = 0;
        }
        return qtdeCompromissoARealizar;
    }
    
    public void setQtdeCompromissoARealizar(Integer qtdeCompromissoARealizar) {
        this.qtdeCompromissoARealizar = qtdeCompromissoARealizar;
    }
    
    public Integer getConsultor() {
        if(consultor == null){
            consultor = 0;
        }
        return consultor;
    }
    
    public void setConsultor(Integer consultor) {
        this.consultor = consultor;
    }

    
    public String getStyleClass() {
        if(styleClass == null){
            styleClass = "text-dark";
        }
        return styleClass;
    }

    
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
	
    
    
    
    
    
    
    

}
