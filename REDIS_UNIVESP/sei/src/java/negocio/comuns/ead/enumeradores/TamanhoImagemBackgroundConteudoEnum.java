package negocio.comuns.ead.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TamanhoImagemBackgroundConteudoEnum {
	
	CEM_PORCENTO, TAMANHO_ORIGINAL;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TamanhoImagemBackgroundConteudoEnum_"+this.name());
	}
	
	private static List<SelectItem> listaSelectItemTamanhoImagemBackgroundConteudo;

	public static List<SelectItem> getListaSelectItemTamanhoImagemBackgroundConteudo() {
		if(listaSelectItemTamanhoImagemBackgroundConteudo == null){
			listaSelectItemTamanhoImagemBackgroundConteudo = new ArrayList<SelectItem>(0);
			listaSelectItemTamanhoImagemBackgroundConteudo.add(new SelectItem(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO, TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO.getValorApresentar()));
			listaSelectItemTamanhoImagemBackgroundConteudo.add(new SelectItem(TamanhoImagemBackgroundConteudoEnum.TAMANHO_ORIGINAL, TamanhoImagemBackgroundConteudoEnum.TAMANHO_ORIGINAL.getValorApresentar()));
		}
		return listaSelectItemTamanhoImagemBackgroundConteudo;
	}
	
}
