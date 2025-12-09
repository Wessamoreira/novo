package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoLayoutApresentacaoResultadoSegmentacaoEnum {

	GRAFICO_PIZZA, GRAFICO_COLUNA, GRAFICO_BARRA, LISTA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoLayoutApresentacaoResultadoSegmentacaoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
}
