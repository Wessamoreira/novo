package relatorio.negocio.comuns.basico;


public class EtiquetaImagemVO {

    private String caminhoImagem;

    
    public String getCaminhoImagem() {
        if(caminhoImagem == null){
            caminhoImagem = "";
        }
        return caminhoImagem;
    }

    
    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }
    
    
}
