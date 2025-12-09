package jobs.enumeradores;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoUsoNotaEnum {
	NENHUM, AVALIACAO_ONLINE, FORUM, ATIVIDADE_DISCURSIVA, AVALIACAO_PRESENCIAL, BLACKBOARD, MOODLE;
	
	private static List<SelectItem> listaSelectItemTipoUsoNota;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoUsoNotaEnum_" + this.name());
	}

	public static List<SelectItem> getListaSelectItemItemTipoUsoNota() {
		if (listaSelectItemTipoUsoNota == null) {
			listaSelectItemTipoUsoNota = new ArrayList<SelectItem>(0);
			listaSelectItemTipoUsoNota.add(new SelectItem(TipoUsoNotaEnum.NENHUM, TipoUsoNotaEnum.NENHUM.getValorApresentar()));
			listaSelectItemTipoUsoNota.add(new SelectItem(TipoUsoNotaEnum.AVALIACAO_ONLINE, TipoUsoNotaEnum.AVALIACAO_ONLINE.getValorApresentar()));
			listaSelectItemTipoUsoNota.add(new SelectItem(TipoUsoNotaEnum.FORUM, TipoUsoNotaEnum.FORUM.getValorApresentar()));
			listaSelectItemTipoUsoNota.add(new SelectItem(TipoUsoNotaEnum.ATIVIDADE_DISCURSIVA, TipoUsoNotaEnum.ATIVIDADE_DISCURSIVA.getValorApresentar()));
			listaSelectItemTipoUsoNota.add(new SelectItem(TipoUsoNotaEnum.AVALIACAO_PRESENCIAL, TipoUsoNotaEnum.AVALIACAO_PRESENCIAL.getValorApresentar()));
			listaSelectItemTipoUsoNota.add(new SelectItem(TipoUsoNotaEnum.BLACKBOARD, TipoUsoNotaEnum.BLACKBOARD.getValorApresentar()));
            
        
            listaSelectItemTipoUsoNota.add(new SelectItem(TipoUsoNotaEnum.MOODLE, TipoUsoNotaEnum.MOODLE.getValorApresentar()));
		}
		return listaSelectItemTipoUsoNota;
	}

}