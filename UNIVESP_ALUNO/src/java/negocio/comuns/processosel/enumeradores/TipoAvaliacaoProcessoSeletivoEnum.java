/**
 * 
 */
package negocio.comuns.processosel.enumeradores;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Carlos Eugênio
 *
 */
public enum TipoAvaliacaoProcessoSeletivoEnum {
	PRESENCIAL,
	ON_LINE,
	AVALIACAO_CURRICULAR;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoAvaliacaoEnum_" + this.name());
	}

	public static List<SelectItem> lipoAvaliacaoProcessoSeletivo;
	public static List<SelectItem> getTipoAvaliacaoProcessoSeletivo() {
		return getListaSelectItemTipoAvaliacaoProcessoSeletivo();
	}
	
	private static List<SelectItem> getListaSelectItemTipoAvaliacaoProcessoSeletivo() {
		if(lipoAvaliacaoProcessoSeletivo == null) {
			lipoAvaliacaoProcessoSeletivo = new ArrayList<SelectItem>();
			for (TipoAvaliacaoProcessoSeletivoEnum tipoAvaliacao : TipoAvaliacaoProcessoSeletivoEnum.values()) {
				lipoAvaliacaoProcessoSeletivo.add(new SelectItem(tipoAvaliacao, tipoAvaliacao.getValorApresentar()));
			}
		}
		return lipoAvaliacaoProcessoSeletivo;
	}
}
