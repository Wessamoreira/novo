package negocio.comuns.protocolo.enumeradores;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoDistribuicaoResponsavelEnum {
	
	GERENTE_DEPARTAMENTO, 
	FUNCIONARIO_DEPARTAMENTO, 
	FUNCIONARIO_CARGO_DEPARTAMENTO, 
	COORDENADOR_CURSO, 
	COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE, 
	COORDENADOR_TCC, 
	FUNCIONARIO_ESPECIFICO,	
	LISTA_FUNCIONARIO,	
	FUNCIONARIO_ESPECIFICO_NO_TRAMITE;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoDistribuicaoResponsavelEnum_"+this.name());
	}
	
	private static List<SelectItem> listaSelectItemTipoDistribuicaoResponsavel;
	
	public static List<SelectItem> getListaSelectItemTipoDistribuicaoResponsavel(){
		if(listaSelectItemTipoDistribuicaoResponsavel == null){
			listaSelectItemTipoDistribuicaoResponsavel = new ArrayList<SelectItem>(0);
			for(TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavelEnum:TipoDistribuicaoResponsavelEnum.values()){
				listaSelectItemTipoDistribuicaoResponsavel.add(new SelectItem(tipoDistribuicaoResponsavelEnum.name(), tipoDistribuicaoResponsavelEnum.getValorApresentar()));
			}
		}
		return listaSelectItemTipoDistribuicaoResponsavel;
	}
	
	

}
