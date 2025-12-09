package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;


public class ConteudoUnidadePaginaGraficoPizzaVO extends SuperVO {
        
    /**
     * 
     */
    private static final long serialVersionUID = 8635602405967899925L;
    private String serie;
    private Double valor;
    
    public ConteudoUnidadePaginaGraficoPizzaVO clone() throws CloneNotSupportedException {
    	ConteudoUnidadePaginaGraficoPizzaVO clone = (ConteudoUnidadePaginaGraficoPizzaVO) super.clone();
    	clone.setNovoObj(true);
        return clone;
    }
     
    
    public ConteudoUnidadePaginaGraficoPizzaVO(String serie, Double valor) {
        super();
        this.serie = serie;
        this.valor = valor;
    }

    public String getSerie() {
        if(serie ==null){
            serie = "";
        }
        return serie;
    }
    
    public void setSerie(String serie) {
        this.serie = serie;
    }
    
    public Double getValor() {
        if(valor ==null){
            valor = 0.0;
        }
        return valor;
    }
    
    public void setValor(Double valor) {
        this.valor = valor;
    }
    
   
    
  
    
    
    
    
    
    

}
