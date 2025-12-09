package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public enum TipoRecursoEnum {
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
	
	public boolean isConteudo() {
		return name().equals(TipoRecursoEnum.CONTEUDO.name());
	}
	
	public boolean isUnidadeConteudo() {
		return name().equals(TipoRecursoEnum.UNIDADE_CONTEUDO.name());
	}
	public boolean isConteudoUnidadePagina() {
		return name().equals(TipoRecursoEnum.CONTEUDO_UNIDADE_PAGINA.name());
	}
}
