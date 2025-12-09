package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum BimestreEnum {

	NAO_CONTROLA("NAO_CONTROLA"), 
        BIMESTRE_01("BIMESTRE_01"), 
        BIMESTRE_02("BIMESTRE_02"), 
        RECUPERACAO_01("RECUPERACAO_01"), 
        SEMESTRE_1("SEMESTRE_1"), 
        BIMESTRE_03("BIMESTRE_03"), 
        BIMESTRE_04("BIMESTRE_04"), 
        RECUPERACAO_02("RECUPERACAO_02"), 
        SEMESTRE_2("SEMESTRE_2"),
        TRIMESTRE_01("TRIMESTRE_01"), 
    	TRIMESTRE_02("TRIMESTRE_02"), 
    	TRIMESTRE_03("TRIMESTRE_03"),
        RESUMO_FINAL("RESUMO_FINAL");

        String valor;
        
       

        BimestreEnum(String valor) {
            this.valor = valor;
        }
        
        public static BimestreEnum getEnum(String valorBase) {
            BimestreEnum[] valores = values();
            for (BimestreEnum obj : valores) {
                if (obj.getValor().equals(valorBase)) {
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
		return UteisJSF.internacionalizar("enum_BimestreEnum_" + this.name());
	}

	public String getValorResumidoApresentar(){
        switch (this) {
		case BIMESTRE_01:
			return "1ª B";							
		case BIMESTRE_02:
			return "2ª B";
		case SEMESTRE_1:
			return "1ª Sem.";
		case RECUPERACAO_01:
			return "Recup.";
		case BIMESTRE_03:
			return "3ª B";			
		case BIMESTRE_04:
			return "4ª B";	
		case RECUPERACAO_02:
			return "Recup.";
		case SEMESTRE_2:
			return "2ª Sem.";
		case RESUMO_FINAL:
			return " ";	
		case TRIMESTRE_01:
			return "1ª T.";
		case  TRIMESTRE_02:  
			return "2ª T.";
		case  TRIMESTRE_03:
			return "3ª T.";
		case NAO_CONTROLA:
			return " ";		
		}
    	return "";
    }
	
	
	public static  BimestreEnum getBimestreEnum(String valor){
        switch (valor) {
		case "1ª B":
			return BIMESTRE_01;							
		case  "2ª B" :
			return BIMESTRE_02;
		case "1ª Sem.":
			return SEMESTRE_1;
		case "Recup.":
			return RECUPERACAO_01;
		case "3ª B":
			return BIMESTRE_03;			
		case "4ª B":
			return BIMESTRE_04;	
		case "Recup2.":
			return RECUPERACAO_02;
		case "2ª Sem.":
			return SEMESTRE_2;
		case " ":
			return RESUMO_FINAL;	
		case "1ª T.":
			return TRIMESTRE_01;
		case "2ª T." :  
			return TRIMESTRE_02;
		case  "3ª T.":
			return TRIMESTRE_03;			
		}
    	return NAO_CONTROLA;
    }
	
	public Integer getOrdemApresentar(){
        switch (this) {
					
		case BIMESTRE_01:
			return 1;			
		case BIMESTRE_02:
			return 2;
		case SEMESTRE_1:
			return 3;
		case RECUPERACAO_01:
			return 4;
		case BIMESTRE_03:
			return 5;			
		case BIMESTRE_04:
			return 6;
		case SEMESTRE_2:
			return 7;
		case RECUPERACAO_02:
			return 8;
		case RESUMO_FINAL:
			return 9;
		case TRIMESTRE_01:
			return 10;
		case  TRIMESTRE_02:  
			return 11;
		case  TRIMESTRE_03:
			return 12;
		case NAO_CONTROLA:
			return 0;		
		}
    	return 0;
    }

	private static List<SelectItem> listaSelectItemBimestre;

	public static List<SelectItem> getListaSelectItemItemBimestre() {
		if (listaSelectItemBimestre == null) {
			listaSelectItemBimestre = new ArrayList<SelectItem>(0);
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.NAO_CONTROLA, BimestreEnum.NAO_CONTROLA.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.BIMESTRE_01, BimestreEnum.BIMESTRE_01.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.BIMESTRE_02, BimestreEnum.BIMESTRE_02.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.BIMESTRE_03, BimestreEnum.BIMESTRE_03.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.BIMESTRE_04, BimestreEnum.BIMESTRE_04.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.RESUMO_FINAL, BimestreEnum.RESUMO_FINAL.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.TRIMESTRE_01, BimestreEnum.TRIMESTRE_01.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.TRIMESTRE_02, BimestreEnum.TRIMESTRE_02.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.TRIMESTRE_03, BimestreEnum.TRIMESTRE_03.getValorApresentar()));
		}
		return listaSelectItemBimestre;
	}
	
	public static  BimestreEnum getBimestreEnumPorSigla(String valor){
        switch (valor) {
		case "B1":
			return BIMESTRE_01;							
		case "b1":
			return BIMESTRE_01;							
		case  "B2" :
			return BIMESTRE_02;
		case  "b2" :
			return BIMESTRE_02;			
		case "S1":
			return SEMESTRE_1;
		case "s1":
			return SEMESTRE_1;
		case "B3":
			return BIMESTRE_03;			
		case "b3":
			return BIMESTRE_03;			
		case "B4":
			return BIMESTRE_04;	
		case "b4":
			return BIMESTRE_04;	
		case "s2":
			return SEMESTRE_2;
		case "S2":
			return SEMESTRE_2;
		default:	
    	return NAO_CONTROLA;
    }
	}

	

}
