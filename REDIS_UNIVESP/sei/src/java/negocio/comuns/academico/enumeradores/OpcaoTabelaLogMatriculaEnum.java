package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum OpcaoTabelaLogMatriculaEnum {
	MATRICULA, MATRICULA_PERIODO;
	
	public static List<SelectItem> getListaSelectItemOpcaoTabelaLogMatricula() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("MATRICULA", "Matrícula"));
		itens.add(new SelectItem("MATRICULA_PERIODO", "Matrícula Período"));
		return itens;
	}
}
