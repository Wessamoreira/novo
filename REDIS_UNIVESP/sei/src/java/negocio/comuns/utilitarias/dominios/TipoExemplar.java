package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;



/**
 *
 * @author Diego
 */
public enum TipoExemplar {

    PERIODICO("PE", "Periódico"),
    PUBLICACAO("PU", "Publicação"),
    RECURSO_AUDIO_VISUAL("RA", "Recurso Áudio Visual");
    
    String valor;
    String descricao;

    TipoExemplar(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoExemplar getEnum(String valor) {
        TipoExemplar[] valores = values();
        for (TipoExemplar obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoExemplar obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    private static List<SelectItem> listaSelectItemTipoExemplar;    
    public static List<SelectItem> getListaSelectItemTipoExemplar(){
        if(listaSelectItemTipoExemplar == null){
            listaSelectItemTipoExemplar = new ArrayList<SelectItem>();
            listaSelectItemTipoExemplar.add(new SelectItem("","Todos"));
            for(TipoExemplar tipo:values()){
                listaSelectItemTipoExemplar.add(new SelectItem(tipo.getValor(),tipo.getDescricao()));    
            }
        }
        return listaSelectItemTipoExemplar;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
