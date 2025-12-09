package negocio.comuns.financeiro.enumerador;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

public enum SituacaoParcelaRemoverSerasaApiGeo {
	
	NENHUM("NN", ""),
	NEGOCIADO("NE", "Negociado"),
	RECEBIDO("RE", "Recebido"),
	RECEBIDO_NEGOCIADO("RC", "Recebido/Negociado");
	
	String valor;
    String descricao;
    static List<SelectItem> listaSelectItem;

    SituacaoParcelaRemoverSerasaApiGeo(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoParcelaRemoverSerasaApiGeo getEnum(String valor) {
    	SituacaoParcelaRemoverSerasaApiGeo[] valores = values();
        for (SituacaoParcelaRemoverSerasaApiGeo obj : valores) {
            if (obj.getDescricao().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
    
    public static List<SelectItem> getComboSituacaoParcelaRemoverSerasaApiGeo() {
        if (listaSelectItem == null || listaSelectItem.isEmpty()) {
            listaSelectItem = new ArrayList<SelectItem>();
            SituacaoParcelaRemoverSerasaApiGeo[] valores = values();
            for (SituacaoParcelaRemoverSerasaApiGeo obj : valores) {
                listaSelectItem.add(new SelectItem(obj, obj.getDescricao()));
            }
        }
        return listaSelectItem;
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
