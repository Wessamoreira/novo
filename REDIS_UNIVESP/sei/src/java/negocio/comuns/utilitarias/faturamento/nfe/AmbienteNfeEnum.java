package negocio.comuns.utilitarias.faturamento.nfe;


public enum AmbienteNfeEnum {

    HOMOLOGACAO(2, "Homologação"), PRODUCAO(1, "Produção");

    private Integer key;
    private String value;

    private AmbienteNfeEnum(Integer key, String value) {
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

    public static AmbienteNfeEnum toString(String value){
        for(AmbienteNfeEnum ambienteNfeEnum:values()){
            if(ambienteNfeEnum.getKey().equals(Integer.parseInt(value))){
                return ambienteNfeEnum;
            }
        }
        return null;
    }
}
