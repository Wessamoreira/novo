package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public enum TipoRecursoEducacionalPBLEnum {
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016 
	 */
	CONTEUDO, UNIDADE_CONTEUDO, CONTEUDO_UNIDADE_PAGINA, CONTEUDO_UNIDADE_PAGINA_RECURSO_EDUCACIONAL;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoRecursoEducacionalPBLEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
}
