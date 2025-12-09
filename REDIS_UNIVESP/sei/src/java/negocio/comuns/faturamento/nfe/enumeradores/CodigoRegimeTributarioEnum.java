/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.faturamento.nfe.enumeradores;

/**
 *
 * @author Rodrigo
 */
public enum CodigoRegimeTributarioEnum {

    SIMPLES_NACIONAL(1, "Simples Nacional"),
    SIMPLES_NACIONAL_EXCESSO_SUBLIMITE_RECEITA_BRUTA(2,"Simples Nacional - excesso de sublimite de receita bruta"),
    REGIME_NORMAL(3, "Regime Normal");

    private Integer key;
    private String value;

    private CodigoRegimeTributarioEnum(Integer key, String value) {
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

    public static CodigoRegimeTributarioEnum toString(String value){
        for(CodigoRegimeTributarioEnum codigoRegimeTributarioEnum:values()){
            if(codigoRegimeTributarioEnum.getKey().equals(Integer.parseInt(value))){
                return codigoRegimeTributarioEnum;
            }
        }
        return null;
    }




}
