package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;


public class ConteudoUnidadePaginaGraficoSerieValorVO extends SuperVO {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6941499614190066524L;
    private Integer sequencia;
    private Double valor;
    
    
    public ConteudoUnidadePaginaGraficoSerieValorVO clone() throws CloneNotSupportedException {
    	ConteudoUnidadePaginaGraficoSerieValorVO clone = (ConteudoUnidadePaginaGraficoSerieValorVO) super.clone();
    	clone.setNovoObj(true);
    	
    	return clone;
    }
    
    public ConteudoUnidadePaginaGraficoSerieValorVO(Integer sequencia, Double valor) {
        super();
        this.sequencia = sequencia;
        this.valor = valor;
    }

    public Integer getSequencia() {
        if(sequencia ==  null){
            sequencia = 0;
        }
        return sequencia;
    }
    
    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }
    
    public Double getValor() {
        if(valor ==  null){
            valor = 0.0;
        }
        return valor;
    }
    
    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    

}
