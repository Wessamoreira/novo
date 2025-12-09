package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoTextoEnadeEnum {
	CALENDARIO_TRIENAL, REALIZACAO, DISPENSA, NAO_COMPARECIMENTO, NAO_PARTICIPANTE, ESTUDANTE_NAO_HABILITADO_AO_ENADE_EM_RAZAO_DO_CALENDARIO_DO_CICLO_AVALIATIVO, ESTUDANTE_NAO_HABILITADO_AO_ENADE_EM_RAZAO_DA_NATUREZA_DO_PROJETO_PEDAGOGICO_DO_CURSO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoTextoEnadeEnum_"+this.name());
	}
}
