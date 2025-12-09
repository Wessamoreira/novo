package negocio.comuns.biblioteca.enumeradores;

import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;

public enum TipoMidiaEnum {
	
	NAO_POSSUI("NP", "Não Possui"),
	CD("CD", "CD-ROM"),
    DVD("DV", "DVD"),
	VHS("VH", "VHS");
    
    private String key;
    private String value;
    
	public static TipoMidiaEnum getEnumPorValor(String valor){
		for(TipoMidiaEnum tipoMidiaEnum: TipoMidiaEnum.values()){
			if(tipoMidiaEnum.getKey().equalsIgnoreCase(valor)){
				return tipoMidiaEnum;
			}
		}
		return null;
	}
    
    private TipoMidiaEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
}
