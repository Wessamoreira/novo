package negocio.comuns.academico.enumeradores;


/**
 * 
 * @author Pedro Andrade
 *
 */
public enum TipoDoTextoImpressaoContratoEnum {
	TEXTO_PADRAO, TEXTO_PADRAO_DECLARACAO;
	
	
	public boolean isTextoPadrao() {
		return name() != null && name().equals(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO.name());
	}
	
	public boolean isTextoPadraoDeclaracao() {
		return name() != null && name().equals(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO.name());
	}

}
