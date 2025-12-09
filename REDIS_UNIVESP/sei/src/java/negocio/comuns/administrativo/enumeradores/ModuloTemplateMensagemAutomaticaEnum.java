package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum ModuloTemplateMensagemAutomaticaEnum {
	
	TODOS,
	ACADEMICO,
	CRM,
	BANCO_CURRICULUM,
	PROCESSO_SELETIVO,
	OUVIDORIA,
	BIBLIOTECA,
	ADMINISTRATIVO,
	FINANCEIRO,
	EAD,
	GSUITE,
	AVALIACAO_INSTITUCIONAL,
	NOTA_FISCAL,
	RELATORIO_FACILITADOR;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_ModuloTemplateMensagemAutomaticaEnum_"+this.name());
	}
	

}
