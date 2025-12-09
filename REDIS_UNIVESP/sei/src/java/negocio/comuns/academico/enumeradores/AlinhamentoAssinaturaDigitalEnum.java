package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Pedro Andrade
 *
 */
public enum AlinhamentoAssinaturaDigitalEnum {
	
	TOPO_ESQUERDA, TOPO_DIREITA, RODAPE_ESQUERDA, RODAPE_DIREITA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoDesigneTextoEnum_" + this.name());
	}
	
	public boolean isTopoEsquerda() {
		return name() != null && name().equals(AlinhamentoAssinaturaDigitalEnum.TOPO_ESQUERDA.name());
	}
	
	public boolean isTopoDireita() {
		return name() != null && name().equals(AlinhamentoAssinaturaDigitalEnum.TOPO_DIREITA.name());
	}
	
	public boolean isRodapeEsquerda() {
		return name() != null && name().equals(AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA.name());
	}
	
	public boolean isRodapeDireita() {
		return name() != null && name().equals(AlinhamentoAssinaturaDigitalEnum.RODAPE_DIREITA.name());
	}

}
