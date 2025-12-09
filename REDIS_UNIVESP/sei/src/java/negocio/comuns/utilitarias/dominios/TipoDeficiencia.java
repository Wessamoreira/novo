package negocio.comuns.utilitarias.dominios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.enumeradores.BimestreEnum;

/**
 *
 * @author Diego
 */
public enum TipoDeficiencia {

	NENHUMA(1, "NE", "Nenhuma", false),
	AUDITIVA(5, "AU", "Auditiva", true),
	BAIXA_VISAO(3, "BV", "Baixa Visão", true),
	CEGUEIRA(2, "CE", "Cegueira", true),
	DISGRAFIA(14,"DG","Disgrafia",false),
	DISLEXIA(13,"DL","Dislexia",false),
    FISICA(6, "FI", "Física", true),
    INTELECTUAL(10, "IN", "Intelectual", true),
    MENTAL(9, "ME", "Mental", true),
    MULTIPLA(8, "MU", "Multipla", false),
    SUPERDOTACAO(12, "SD", "Superdotação", true),
    SURDEZ(4, "SU", "Surdez", true),
    SURDOCEGUEIRA(7, "SC", "Surdocegueira", true),
    TRANSTORNO_DEFICIT_ATENCAOHIPERATIVIDADE(16,"TDAH","Transtorno de Déficit de Atenção/Hiperatividade",false),
    TRANSTORNO_ESPECTRO_AUTISTA(15,"TEA","Transtorno do Espectro Autista", true),
    TGDTEA(11, "TgdTea", "Transtorno Global do Desenvolvimento",true),
    VISAO_MONOCULAR(17, "VM", "Visão Monocular", true),
    NAO_DECLARADO(0, "", "Não Declarado", false);
	
	private final int codigoCenso;
    private final String valor;
    private final String descricao;
    private final boolean considerarCenso;

    TipoDeficiencia(int codigoCenso, String valor, String descricao, boolean considerarCenso) {
    	this.codigoCenso = codigoCenso;
    	this.valor = valor;
        this.descricao = descricao;
        this.considerarCenso = considerarCenso;
    }

    public static TipoDeficiencia getEnum(String valor) {
        TipoDeficiencia[] valores = values();
        for (TipoDeficiencia obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoDeficiencia obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public String getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

	public int getCodigoCenso() {
		return codigoCenso;
	}
	
	private String getDescricaoSQL() {
		return getValor() + "," + getCodigoCenso();
	}

	public static Boolean getExisteValor(String valor) {
		for (TipoDeficiencia tipoDeficiencia : TipoDeficiencia.values()) {
			if (tipoDeficiencia.getValor().equals(valor)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isConsiderarCenso() {
		return considerarCenso;
	}
	
	public String getSQLMatrizChaveConsulta() {
		return equals(MENTAL) ? " ('{" + MENTAL.getDescricaoSQL() + "," + INTELECTUAL.getDescricaoSQL() + "}') " : " ('{" + getDescricaoSQL() + "}') ";
	}
	
	public static String getSQLMatrizGeralChaveConsulta() {
		return Stream.of(values()).filter(TipoDeficiencia::isConsiderarCenso).map(TipoDeficiencia::getDescricaoSQL).collect(Collectors.joining(",", " ('{", "}') "));
	}
	
	private static List<SelectItem> listaSelectItemTipoDeficiencia;
	
	public static List<SelectItem> getListaSelectItemTipoDeficiencia() {
		if (listaSelectItemTipoDeficiencia == null) {
			listaSelectItemTipoDeficiencia = new ArrayList<SelectItem>(0);
			
			TipoDeficiencia[] valores = values();
			listaSelectItemTipoDeficiencia.add(new SelectItem(null, "Todos"));
			for(TipoDeficiencia tipoDeficiencia : valores) {
				listaSelectItemTipoDeficiencia.add(new SelectItem(tipoDeficiencia, tipoDeficiencia.getDescricao()));
			}
			
		}
		return listaSelectItemTipoDeficiencia;
	}
}
