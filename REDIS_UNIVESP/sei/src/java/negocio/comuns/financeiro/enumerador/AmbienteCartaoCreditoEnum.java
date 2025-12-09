package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum AmbienteCartaoCreditoEnum {
	
	HOMOLOGACAO(1), PRODUCAO(0);

    private Integer key;

    private AmbienteCartaoCreditoEnum(Integer key) {
        this.key = key;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
    
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_AmbienteCartaoCreditoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
}
