/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.faturamento.nfe;

/**
 *
 * @author Euripedes Doutor
 */
public class EstadosCodigoIBGE {

    public static final String RONDONIA = "11";
    public static final String ACRE = "12";
    public static final String AMAZONAS = "13";
    public static final String RORAIMA = "14";
    public static final String PARA = "15";
    public static final String AMAPA = "16";
    public static final String TOCANTINS = "17";
    public static final String MARANHAO = "21";
    public static final String PIAUI = "22";
    public static final String CEARA = "23";
    public static final String RIO_GRANDE_DO_NORTE = "24";
    public static final String PARAIBA = "25";
    public static final String PERNAMBUCO = "26";
    public static final String ALAGOAS = "27";
    public static final String SERGIPE = "28";
    public static final String BAHIA = "29";
    public static final String MINAS_GERAIS = "31";
    public static final String ESPIRITO_SANTO = "32";
    public static final String RIO_DE_JANEIRO = "33";
    public static final String SAO_PAULO = "35";
    public static final String PARANA = "41";
    public static final String SANTA_CATARINA = "42";
    public static final String RIO_GRANDE_DO_SUL = "43";
    public static final String MATO_GROSSO_DO_SUL = "50";
    public static final String MATO_GROSSO = "51";
    public static final String GOIAS = "52";
    public static final String DISTRITO_FEDERAL = "53";
    
    
    public static String obterSiglaEstado(String codigoIBGE) {
    	if (codigoIBGE.equals("11")) {
    		return "RO";
    	} else if (codigoIBGE.equals("12")) {
    		return "AC";
    	} else if (codigoIBGE.equals("13")) {
    		return "AM";
    	} else if (codigoIBGE.equals("14")) {
    		return "RR";
    	} else if (codigoIBGE.equals("15")) {
    		return "PA";
    	} else if (codigoIBGE.equals("16")) {
    		return "AP";
    	} else if (codigoIBGE.equals("17")) {
    		return "TO";
    	} else if (codigoIBGE.equals("21")) {
    		return "MA";
    	} else if (codigoIBGE.equals("22")) {
    		return "PI";
    	} else if (codigoIBGE.equals("23")) {
    		return "CE";
    	} else if (codigoIBGE.equals("24")) {
    		return "RN";
    	} else if (codigoIBGE.equals("25")) {
    		return "PB";
    	} else if (codigoIBGE.equals("26")) {
    		return "PE";
    	} else if (codigoIBGE.equals("27")) {
    		return "AL";
    	} else if (codigoIBGE.equals("28")) {
    		return "SE";
    	} else if (codigoIBGE.equals("29")) {
    		return "BA";
    	} else if (codigoIBGE.equals("31")) {
    		return "MG";
    	} else if (codigoIBGE.equals("32")) {
    		return "ES";
    	} else if (codigoIBGE.equals("33")) {
    		return "RJ";
    	} else if (codigoIBGE.equals("35")) {
    		return "SP";
    	} else if (codigoIBGE.equals("41")) {
    		return "PR";
    	} else if (codigoIBGE.equals("42")) {
    		return "SC";
    	} else if (codigoIBGE.equals("43")) {
    		return "RS";
    	} else if (codigoIBGE.equals("50")) {
    		return "MS";
    	} else if (codigoIBGE.equals("51")) {
    		return "MT";
    	} else if (codigoIBGE.equals("52")) {
    		return "GO";
    	} else if (codigoIBGE.equals("53")) {
    		return "DF";
    	}
    	return "";
    }
    
    public static String obterCodigoIBGEEstado(String uf) {
        if (uf.equals("AC")) {
            return ACRE;
        } else if (uf.equals("AL")) {
            return ALAGOAS;
        } else if (uf.equals("AP")) {
            return AMAPA;
        } else if (uf.equals("AM")) {
            return AMAZONAS;
        } else if (uf.equals("BA")) {
            return BAHIA;
        } else if (uf.equals("CE")) {
            return CEARA;
        } else if (uf.equals("DF")) {
            return DISTRITO_FEDERAL;
        } else if (uf.equals("ES")) {
            return ESPIRITO_SANTO;
        } else if (uf.equals("GO")) {
            return GOIAS;
        } else if (uf.equals("MA")) {
            return MARANHAO;
        } else if (uf.equals("MT")) {
            return MATO_GROSSO;
        } else if (uf.equals("MS")) {
            return MATO_GROSSO_DO_SUL;
        } else if (uf.equals("MG")) {
            return MINAS_GERAIS;
        } else if (uf.equals("PA")) {
            return PARA;
        } else if (uf.equals("PB")) {
            return PARAIBA;
        } else if (uf.equals("PR")) {
            return PARANA;
        } else if (uf.equals("PE")) {
            return PERNAMBUCO;
        } else if (uf.equals("PI")) {
            return PIAUI;
        } else if (uf.equals("RJ")) {
            return RIO_DE_JANEIRO;
        } else if (uf.equals("RN")) {
            return RIO_GRANDE_DO_NORTE;
        } else if (uf.equals("RS")) {
            return RIO_GRANDE_DO_SUL;
        } else if (uf.equals("RO")) {
            return RONDONIA;
        } else if (uf.equals("RR")) {
            return RORAIMA;
        } else if (uf.equals("SC")) {
            return SANTA_CATARINA;
        } else if (uf.equals("SP")) {
            return SAO_PAULO;
        } else if (uf.equals("SE")) {
            return SERGIPE;
        } else if (uf.equals("TO")) {
            return TOCANTINS;
        }
        return uf;
    }
}
