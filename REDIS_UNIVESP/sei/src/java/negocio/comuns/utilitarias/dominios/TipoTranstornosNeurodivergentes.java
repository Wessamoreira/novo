package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum TipoTranstornosNeurodivergentes {

	NENHUM(" ", " "),
	ESPECTRO_AUTISTA("TRANSTORNO DO ESPECTRO AUTISTA", "Transtorno do Espectro Autista (TEA)"),
    ESPECTRO_OBSESSIVO_COMPULSIVO("TRANSTORNO DO ESPECTRO OBSESSIVO-COMPULSIVO", "Transtorno do Espectro Obsessivo-Compulsivo (TEOC)"),
    DISPRAXIA("DISPRAXIA", "Dispraxia"),
    DISLEXIA("DISLEXIA", "Dislexia"),
    DEFICIT_ATENCAO("TRANSTORNO DE DÉFICIT DE ATENÇÃO E HIPERATIVIDADE", "Transtorno de Déficit de Atenção e Hiperatividade (TDAH)"),
    TOURETTE("SÍNDROME DE TOURETTE", "Síndrome de Tourette"),
    EPILEPSIA("EPILEPSIA", "Epilepsia"),
    BIPOLARIDADE("BIPOLARIDADE", "Bipolaridade");
	
	private final String valor;
    private final String descricao;

    TipoTranstornosNeurodivergentes(String valor, String descricao) {
    	this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoTranstornosNeurodivergentes getEnum(String valor) {
    	TipoTranstornosNeurodivergentes[] valores = values();
        for (TipoTranstornosNeurodivergentes obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
    	TipoTranstornosNeurodivergentes obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public String getValor() {
    	return valor;
    }
	
    public static Boolean getExisteValor(String valor) {
		for (TipoTranstornosNeurodivergentes tipoTranstornosNeurodivergentes : TipoTranstornosNeurodivergentes.values()) {
			if (tipoTranstornosNeurodivergentes.getValor().equals(valor)) {
				return true;
			}
		}
		return false;
	}
    
	private static List<SelectItem> listaSelectItemTipoTranstornosNeurodivergentes;
	
	public static List<SelectItem> getListaSelectItemTipoTranstornosNeurodivergentes() {
		if (listaSelectItemTipoTranstornosNeurodivergentes == null) {
			listaSelectItemTipoTranstornosNeurodivergentes = new ArrayList<SelectItem>(0);
			
			TipoTranstornosNeurodivergentes[] valores = values();
			listaSelectItemTipoTranstornosNeurodivergentes.add(new SelectItem(null, "Todos"));
			for(TipoTranstornosNeurodivergentes tipoTranstornosNeurodivergentes : valores) {
				listaSelectItemTipoTranstornosNeurodivergentes.add(new SelectItem(tipoTranstornosNeurodivergentes, tipoTranstornosNeurodivergentes.getDescricao()));
			}
		}
		return listaSelectItemTipoTranstornosNeurodivergentes;
	}
}
