package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SemestreEnum {

    PRIMEIRO_SEMESTRE("1", "1º"),
    SEGUNDO_SEMESTRE("2", "2º");

	String valor;
    String descricao;

    SemestreEnum(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoControleRemessaContaReceberEnum_"+this.name());
	}
    
    public static SemestreEnum getEnum(String valor) {
    	SemestreEnum[] valores = values();
        for (SemestreEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
    	SemestreEnum obj = getEnum(valor);
        if (obj != null) {
            return UteisJSF.internacionalizar(obj.getDescricao());
        }
        return valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return UteisJSF.internacionalizar(descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }	
}
