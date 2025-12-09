package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;


public class ConteudoUnidadePaginaGraficoCategoriaVO extends SuperVO {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4816430591384395652L;
    private Integer sequencia;
    private String categoria;
    
    public ConteudoUnidadePaginaGraficoCategoriaVO clone() throws CloneNotSupportedException {
    	ConteudoUnidadePaginaGraficoCategoriaVO clone = (ConteudoUnidadePaginaGraficoCategoriaVO) super.clone();
    	clone.setNovoObj(true);
    	return clone;
    }
    
    public ConteudoUnidadePaginaGraficoCategoriaVO(Integer sequencia, String categoria) {
        super();
        this.sequencia = sequencia;
        this.categoria = categoria;
    }

    public Integer getSequencia() {
        if(sequencia ==null){
            sequencia=0;
        }
        return sequencia;
    }
    
    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }
    
    public String getCategoria() {
        if(categoria == null){
            categoria = "";
        }
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getCategoriaApresentar() {
        if(getCategoria().length() > 16){
            return getCategoria().substring(0, 14)+"...";
        }
        
        return getCategoria();
    }
    
    

}
