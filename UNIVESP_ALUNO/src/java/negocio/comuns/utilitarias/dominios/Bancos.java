package negocio.comuns.utilitarias.dominios;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Diego
 */
public enum Bancos {

    BRADESCO(1, "237", "Bradesco", "60746948"),
    CAIXA_ECONOMICA_FEDERAL(2, "104", "Caixa Economica Federal", "00360305"),    
    REAL(3, "441", "Real", ""),
    BANCO_DO_BRASIL(4, "001", "Banco do Brasil", "00000000"),
    HSBC(5, "399", "HSBC", ""),
    ITAU(6, "341", "Itaú", "60701190"),
    SANTANDER(7, "033", "Santander", "90400888"),
    NOSSA_CAIXA(8, "151", "Nossa Caixa", ""),
    UNIBANCO(9, "409", "Unibanco", ""),
    ITAU_RETORNO_REMESSA(10, "341", "Itaú Retorno Remessa", "60701190"),
    SICRED(11, "748", "Sicred", "01181521"),
    SICOOB(12, "756", "Sicoob", "2038232"),    
    CAIXA_ECONOMICA_FEDERAL_SICOB(13, "104-S", "Caixa Economica Federal (SICOOB)", ""),
    CAIXA_ECONOMICA_FEDERAL_SICOB_15(15, "104-N15", "Caixa Economica Federal (SICOOB - 15 Posições)", ""),	
    SAFRA(16, "422", "Safra", ""),	
    DAYCOVAL(14, "707", "Daycoval", "62232889"),
    BANESTE(17,"021","Banestes","00000208"),
    BRB(18,"070","Brb","");

    int codigo;
    String numeroBanco;
    String nome;
    String numeroISPB;

    Bancos(int codigo, String numeroBanco, String nome, String numeroISPB) {
    	this.codigo = codigo;
    	this.numeroBanco = numeroBanco;
        this.nome = nome;
        this.numeroISPB=numeroISPB;
    }
    
    public static Bancos getNumeroBancoParseInt(Integer nrBanco) {
        Bancos[] valores = values();
        for (Bancos obj : valores) {
            if (obj.getCodigo()!= 13 && obj.getCodigo()!= 15 &&  Uteis.getValorInteiro(obj.getNumeroBanco()).equals(nrBanco)) {
                return obj;
            }
        }
        return null;
    }
    
    public static Bancos getEnum(String numeroBanco) {
    	Bancos[] valores = values();
    	for (Bancos obj : valores) {
    		if (obj.getNumeroBanco().equals(numeroBanco)) {
    			return obj;
    		}
    	}
    	return null;
    }

    public static Bancos getEnum(int codigo) {
        Bancos[] valores = values();
        for (Bancos obj : valores) {
            if (obj.getCodigo() == codigo) {
                return obj;
            }
        }
        return null;
    }

    public static String getNome(int codigo) {
        Bancos obj = getEnum(codigo);
        if (obj != null) {
            return obj.getNome();
        }
        return null;
    }

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public String getNumeroISPB() {
		return numeroISPB;
	}

	public void setNumeroISPB(String numeroISPB) {
		this.numeroISPB = numeroISPB;
	}
	
	

}
