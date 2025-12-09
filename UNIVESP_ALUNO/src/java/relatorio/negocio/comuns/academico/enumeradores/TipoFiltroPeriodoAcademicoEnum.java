package relatorio.negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

public enum TipoFiltroPeriodoAcademicoEnum {

		PERIODO_DATA, ANO, ANO_PERIODO_DATA, ANO_SEMESTRE, AMBOS;
		
		private static List<SelectItem> listaSelectItem;
		private static List<SelectItem> listaSelectItemSemAmbos;
		
		public static List<SelectItem> getListaSelectItem(){
			if(TipoFiltroPeriodoAcademicoEnum.listaSelectItem == null){
				TipoFiltroPeriodoAcademicoEnum.listaSelectItem = new ArrayList<SelectItem>(0);
				TipoFiltroPeriodoAcademicoEnum.listaSelectItem.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO, "Ano"));
				TipoFiltroPeriodoAcademicoEnum.listaSelectItem.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE, "Ano/Semestre"));
				TipoFiltroPeriodoAcademicoEnum.listaSelectItem.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA, "Período de Data"));
				TipoFiltroPeriodoAcademicoEnum.listaSelectItem.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_PERIODO_DATA, "Ano e Período de Data"));
				TipoFiltroPeriodoAcademicoEnum.listaSelectItem.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.AMBOS, "Ano/Semestre e Período de Data"));
			}
			return TipoFiltroPeriodoAcademicoEnum.listaSelectItem;			
		}
		
		public static List<SelectItem> getListaSelectItemSemAmbos(){
			if(TipoFiltroPeriodoAcademicoEnum.listaSelectItemSemAmbos == null){
				TipoFiltroPeriodoAcademicoEnum.listaSelectItemSemAmbos = new ArrayList<SelectItem>(0);
				TipoFiltroPeriodoAcademicoEnum.listaSelectItemSemAmbos.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO, "Ano"));
				TipoFiltroPeriodoAcademicoEnum.listaSelectItemSemAmbos.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE, "Ano/Semestre"));
				TipoFiltroPeriodoAcademicoEnum.listaSelectItemSemAmbos.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA, "Período de Data"));				
			}
			return TipoFiltroPeriodoAcademicoEnum.listaSelectItemSemAmbos;			
		}
}
