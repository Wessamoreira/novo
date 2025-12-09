package negocio.comuns.financeiro.enumerador;

import jakarta.faces.model.SelectItem;
import negocio.comuns.utilitarias.UteisJSF;

import java.util.ArrayList;
import java.util.List;

public enum EmpresaFinanceiraEnum {
    NENHUM, COBRAFIX, ISAAC, PRINCIPIA;

    public String getValorApresentar() {
        return UteisJSF.internacionalizar("enum_EmpresaFinanceiraEnum_" + name());
    }
    
    public Boolean isCobrafix() {
        return equals(COBRAFIX);
    }
    public Boolean isIsaac() {
    	return equals(ISAAC);
    }
    public Boolean isPrincipia() {
    	return equals(PRINCIPIA);
    }

    public Boolean isNenhum() {
        return equals(NENHUM);
    }

    public static List<SelectItem> getItensComboBox() {
        List<SelectItem> combo = new ArrayList<>();

        for (EmpresaFinanceiraEnum item : values()) {
            combo.add(new SelectItem(item, item.getValorApresentar()));
        }

        return combo;
    }
}