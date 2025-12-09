package relatorio.negocio.comuns.painelGestor;


public class FollowMeGraficoRelVO {
    
    
    private String serie;
    private String categoria;
    private Double valor;
    private String tituloGrafico;
    
    public String getSerie() {
        if(serie == null){
            serie = "";
        }
        return serie;
    }

    
    public void setSerie(String serie) {
        this.serie = serie;
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

    
    public Double getValor() {
        if(valor == null){
            valor = 0.0;
        }
        return valor;
    }
    
    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    public String getTituloGrafico() {
        if(tituloGrafico == null){
            tituloGrafico = "";
        }
        return tituloGrafico;
    }

    
    public void setTituloGrafico(String tituloGrafico) {
        this.tituloGrafico = tituloGrafico;
    }

  

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FollowMeGraficoRelVO){
            FollowMeGraficoRelVO fm = ((FollowMeGraficoRelVO) obj);
            return fm.getSerie().equals(this.getSerie())                    
                    && fm.getCategoria().equals(this.getCategoria());
        }
        return false;
    }

  

}
