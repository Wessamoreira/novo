package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.utilitarias.UteisJSF;

public enum VariaveisNotaEnum {

	    FALTASB1("FALTASB1", TipoCampoEnum.INTEIRO), 
        FALTASB2("FALTASB2", TipoCampoEnum.INTEIRO), 
        FALTASB3("FALTASB3", TipoCampoEnum.INTEIRO), 
        FALTASB4("FALTASB4", TipoCampoEnum.INTEIRO), 
        FALTASPER("FALTASPER", TipoCampoEnum.INTEIRO), 
        SUSPENB1("SUSPENB1", TipoCampoEnum.INTEIRO), 
        SUSPENB2("SUSPENB2", TipoCampoEnum.INTEIRO), 
        SUSPENB3("SUSPENB3", TipoCampoEnum.INTEIRO), 
        SUSPENB4("SUSPENB4", TipoCampoEnum.INTEIRO), 
        SUSPENPER("SUSPENPER", TipoCampoEnum.INTEIRO),
        ADVERB1("ADVERB1", TipoCampoEnum.INTEIRO), 
        ADVERB2("ADVERB2", TipoCampoEnum.INTEIRO), 
        ADVERB3("ADVERB3", TipoCampoEnum.INTEIRO), 
        ADVERB4("ADVERB4", TipoCampoEnum.INTEIRO), 
        ADVERPER("ADVERPER", TipoCampoEnum.INTEIRO),
		FREQUENCIA("FREQUENCIA", TipoCampoEnum.DOUBLE),
		COMPGERAL("COMPGERAL", TipoCampoEnum.BOOLEAN),		
		COMPSOMA("COMPSOMA", TipoCampoEnum.BOOLEAN),		
		COMPMEDIA("COMPMEDIA", TipoCampoEnum.BOOLEAN),		
		COMPFORMULA("COMPFORMULA", TipoCampoEnum.BOOLEAN),
		NR_CH("NR_CH", TipoCampoEnum.INTEIRO),
		NR_CRED("NR_CRED", TipoCampoEnum.INTEIRO),
		COMPOSICAO("COMPOSICAO", TipoCampoEnum.TEXTO);	
        
        String valor;
        TipoCampoEnum tipoValor;
        
        VariaveisNotaEnum(String valor, TipoCampoEnum tipoValor) {
        	this.tipoValor = tipoValor;
            this.valor = valor;
        }
        
        public static VariaveisNotaEnum getEnum(String valor) {
            VariaveisNotaEnum[] valores = values();
            for (VariaveisNotaEnum obj : valores) {
                if (obj.getValor().equals(valor)) {
                    return obj;
                }
            }
            return null;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
        

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_VariaveisNotaEnum_" + this.name());
	}

	private static List<SelectItem> listaSelectVariaveisNota;

	public static List<SelectItem> getListaSelectVariaveisNota() {
		if (listaSelectVariaveisNota == null) {
			listaSelectVariaveisNota = new ArrayList<SelectItem>(0);
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.FALTASB1, VariaveisNotaEnum.FALTASB1.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.FALTASB2, VariaveisNotaEnum.FALTASB2.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.FALTASB3, VariaveisNotaEnum.FALTASB3.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.FALTASB4, VariaveisNotaEnum.FALTASB4.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.FALTASPER, VariaveisNotaEnum.FALTASPER.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.ADVERB1, VariaveisNotaEnum.ADVERB1.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.ADVERB2, VariaveisNotaEnum.ADVERB2.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.ADVERB3, VariaveisNotaEnum.ADVERB3.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.ADVERB4, VariaveisNotaEnum.ADVERB4.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.ADVERPER, VariaveisNotaEnum.ADVERPER.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.FREQUENCIA, VariaveisNotaEnum.FREQUENCIA.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.COMPGERAL, VariaveisNotaEnum.COMPGERAL.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.COMPFORMULA, VariaveisNotaEnum.COMPFORMULA.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.COMPSOMA, VariaveisNotaEnum.COMPSOMA.getValorApresentar()));
			listaSelectVariaveisNota.add(new SelectItem(VariaveisNotaEnum.COMPMEDIA, VariaveisNotaEnum.COMPMEDIA.getValorApresentar()));
		}
		return listaSelectVariaveisNota;
	}

	/**
	 * @return the tipoValor
	 */
	public TipoCampoEnum getTipoValor() {
		if (tipoValor == null) {
			tipoValor = TipoCampoEnum.DOUBLE;
		}
		return tipoValor;
	}

	/**
	 * @param tipoValor the tipoValor to set
	 */
	public void setTipoValor(TipoCampoEnum tipoValor) {
		this.tipoValor = tipoValor;
	}

	/**
	 * @param listaSelectVariaveisNota the listaSelectVariaveisNota to set
	 */
	public static void setListaSelectVariaveisNota(List<SelectItem> listaSelectVariaveisNota) {
		VariaveisNotaEnum.listaSelectVariaveisNota = listaSelectVariaveisNota;
	}
	
	

}
