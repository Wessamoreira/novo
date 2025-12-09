package relatorio.negocio.comuns.painelGestor;


public class FollowMeCategoriaDespesaPadraoAlteradoVO {
    
    private String categoriaDespesa;
    private String titulo;
    private Double valorMedio;
    private Double valorMesAtual;
    private Double percentualDesviado;
    
    public String getCategoriaDespesa() {
        if(categoriaDespesa == null){
            categoriaDespesa = "";
        }
        return categoriaDespesa;
    }
    
    public void setCategoriaDespesa(String categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
    }
    
    public Double getValorMedio() {
        if(valorMedio == null){
            valorMedio = 0.0;
        }
        return valorMedio;
    }
    
    public void setValorMedio(Double valorMedio) {
        this.valorMedio = valorMedio;
    }
    
    public Double getValorMesAtual() {
        if(valorMesAtual == null){
            valorMesAtual = 0.0;
        }
        return valorMesAtual;
    }
    
    public void setValorMesAtual(Double valorMesAtual) {
        this.valorMesAtual = valorMesAtual;
    }
    
    public Double getPercentualDesviado() {
        if(percentualDesviado == null){
            percentualDesviado = 0.0;
        }
        return percentualDesviado;
    }
    
    public void setPercentualDesviado(Double percentualDesviado) {
        this.percentualDesviado = percentualDesviado;
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
