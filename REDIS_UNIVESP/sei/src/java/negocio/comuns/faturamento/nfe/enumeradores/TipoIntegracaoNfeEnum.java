package negocio.comuns.faturamento.nfe.enumeradores;

public enum TipoIntegracaoNfeEnum {

	NFE(1, "Nota Fiscal Eletrônica DF"), NFSE(2, "Nota Fiscal de Serviço Eletrônica");

	private Integer key;
    private String value;

    private TipoIntegracaoNfeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static TipoIntegracaoNfeEnum getEnumPorKey(Integer key){
        for(TipoIntegracaoNfeEnum tipoIntegracaoNfeEnum:values()){
            if(tipoIntegracaoNfeEnum.getKey().equals(key)){
                return tipoIntegracaoNfeEnum;
            }
        }
        return null;
    }

}
