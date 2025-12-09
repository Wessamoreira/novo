package negocio.comuns.processosel.enumeradores;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoLayoutApresentacaoResultadoPerguntaEnum {

		GRAFICO_PIZZA, GRAFICO_COLUNA, LISTA;
		
		public String getValorApresentar(){
			return UteisJSF.internacionalizar("enum_TipoLayoutApresentacaoResultadoPerguntaEnum_"+this.name());
		}
		
		private static List<SelectItem> listaSelectItemTipoLayoutApresentacaoResultadoPergunta;

		public static List<SelectItem> getListaSelectItemTipoLayoutApresentacaoResultadoPergunta() {
			if(listaSelectItemTipoLayoutApresentacaoResultadoPergunta == null){
				listaSelectItemTipoLayoutApresentacaoResultadoPergunta = new ArrayList<SelectItem>(0);
				for(TipoLayoutApresentacaoResultadoPerguntaEnum tipoLayoutApresentacaoResultadoPerguntaEnum:TipoLayoutApresentacaoResultadoPerguntaEnum.values()){
					listaSelectItemTipoLayoutApresentacaoResultadoPergunta.add(new SelectItem(tipoLayoutApresentacaoResultadoPerguntaEnum, tipoLayoutApresentacaoResultadoPerguntaEnum.getValorApresentar()));
				}
			}
			return listaSelectItemTipoLayoutApresentacaoResultadoPergunta;
		}

		public static void setListaSelectItemTipoLayoutApresentacaoResultadoPergunta(List<SelectItem> listaSelectItemTipoLayoutApresentacaoResultadoPergunta) {
			TipoLayoutApresentacaoResultadoPerguntaEnum.listaSelectItemTipoLayoutApresentacaoResultadoPergunta = listaSelectItemTipoLayoutApresentacaoResultadoPergunta;
		}
		
		
	
}
