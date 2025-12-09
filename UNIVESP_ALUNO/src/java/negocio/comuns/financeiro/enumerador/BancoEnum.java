package negocio.comuns.financeiro.enumerador;

public enum BancoEnum {
	
	// Enum para gestão dos banco e os serviços disponíveis no sistema
	// Primeiro parametro consiste em definir se o banco possuir porcessamento arquivo retorno.
	// Segundo parametro consiste em definir se o banco possuir porcessamento arquivo remessa.
	// Terceiro parametro consiste em definir se o banco possuir porcessamento arquivo retorno da remessa.
	// Quarta parametro consiste em definir se o banco possuir porcessamento arquivo de remessa para conta a pagar.
	BANCO_BRASIL_CNAB240("CNAB240", "001","00000000", true, true, true, false, "Banco do Brasil - CNAB 240",false), 
	BANCO_BRASIL_CNAB400("CNAB400", "001", "00000000", true, true, false, false, "Banco do Brasil - CNAB 400",true), 
	BRADESCO_CNAB240("CNAB240", "237", "60746948", false, false, true, true, "Bradesco - CNAB 240",false), 
	BRADESCO_CNAB400("CNAB400", "237", "60746948", true, true, true, false, "Bradesco - CNAB 400",false), 
	SANTANDER_CNAB240("CNAB240", "033", "90400888", true, true, false, true, "Santander - CNAB 240",false), 
	SANTANDER_CNAB400("CNAB400", "033", "90400888", true, true, false, false, "Santander - CNAB 400",false), 
	DAYCOVAL_CNAB240("CNAB240", "707", "62232889", false, false, false, false, "Daycoval - CNAB 240",false), 
	DAYCOVAL_CNAB400("CNAB400", "707", "62232889", true, true, false, false, "Daycoval - CNAB 400",false), 
	ITAU_CNAB240("CNAB240", "341", "60701190", true, true, true, true, "Itaú - CNAB 240",false), 
	ITAU_CNAB400("CNAB400", "341", "60701190", true, true, true, false, "Itaú - CNAB 400",true), 
	SICOOB_CNAB240("CNAB240", "756", "2038232", true, true, false, true, "Sicoob - CNAB 240",true), 
	SICOOB_CNAB400("CNAB400", "756", "2038232", true, true, false, false, "Sicoob - CNAB 400",false), 
	SICRED_CNAB240("CNAB240", "748", "2038232", true, true, false, false, "Sicred - CNAB 240",false), 
	SICRED_CNAB400("CNAB400", "748", "2038232", true, true, false, false, "Sicred - CNAB 400",false), 
	CAIXA_CNAB240("CNAB240", "104", "00360305", true, true, true, true, "Caixa - CNAB 240",false), 
	CAIXA_CNAB400("CNAB400", "104", "00360305", false, false, false, false, "Caixa - CNAB 400",false),
	SAFRA_CNAB400("CNAB400", "422", "00000000", true, true, true, false, "Safra - CNAB 400",false),
	BANESTES_CNAB400("CNAB400", "021", "00000000", true, true, false, false, "Banestes - CNAB 400",false);

    String cnab;
    String nrBanco;
    String numeroISPB;
    Boolean possuiRetorno;
    Boolean possuiRemessa;
    Boolean possuiRetornoRemessa;
    Boolean possuiRemessaContaPagar;	
    String descricao;
    Boolean permiteInformarValorMultaParaRemessa;
    
    BancoEnum(String cnab, String nrBanco, String numeroISPB, Boolean possuiRetorno, Boolean possuiRemessa, Boolean possuiRetornoRemessa, Boolean possuiRemessaContaPagar, String descricao, boolean permiteInformarValorMultaParaRemessa) {
        this.cnab = cnab;
        this.nrBanco = nrBanco;
        this.numeroISPB = numeroISPB;
        this.possuiRetorno = possuiRetorno;
        this.possuiRemessa = possuiRemessa;
        this.possuiRetornoRemessa = possuiRetornoRemessa;
        this.possuiRemessaContaPagar = possuiRemessaContaPagar;		
        this.descricao = descricao;
        this.permiteInformarValorMultaParaRemessa = permiteInformarValorMultaParaRemessa;
    }

    public static BancoEnum getEnum(String cnab, String nrBanco) {
    	BancoEnum[] valores = values();
        for (BancoEnum obj : valores) {
            if (obj.getCnab().equals(cnab) && obj.getNrBanco().equals(nrBanco)) {
                return obj;
            }
        }
        return null;
    }

    public static BancoEnum getEnum(String nrBanco) {
    	BancoEnum[] valores = values();
    	for (BancoEnum obj : valores) {
    		if (obj.getNrBanco().equals(nrBanco)) {
    			return obj;
    		}
    	}
    	return null;
    }

//    public static String getDescricao(String valor) {
//    	BancoEnum obj = getEnum(valor);
//        if (obj != null) {
//            return obj.getDescricao();
//        }
//        return valor;
//    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

	public Boolean getPossuiRetorno() {
		return possuiRetorno;
	}

	public void setPossuiRetorno(Boolean possuiRetorno) {
		this.possuiRetorno = possuiRetorno;
	}

	public Boolean getPossuiRemessa() {
		return possuiRemessa;
	}

	public void setPossuiRemessa(Boolean possuiRemessa) {
		this.possuiRemessa = possuiRemessa;
	}

	public Boolean getPossuiRetornoRemessa() {
		return possuiRetornoRemessa;
	}

	public void setPossuiRetornoRemessa(Boolean possuiRetornoRemessa) {
		this.possuiRetornoRemessa = possuiRetornoRemessa;
	}

	public String getCnab() {
		return cnab;
	}

	public void setCnab(String cnab) {
		this.cnab = cnab;
	}

	public String getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(String nrBanco) {
		this.nrBanco = nrBanco;
	}	
	
	public Boolean getPossuiRemessaContaPagar() {
		return possuiRemessaContaPagar;
	}

	public void setPossuiRemessaContaPagar(Boolean possuiRemessaContaPagar) {
		this.possuiRemessaContaPagar = possuiRemessaContaPagar;
	}

	public String getNumeroISPB() {
		return numeroISPB;
	}

	public void setNumeroISPB(String numeroISPB) {
		this.numeroISPB = numeroISPB;
	}	
	
	public Boolean getPermiteInformarValorMultaParaRemessa() {
		return permiteInformarValorMultaParaRemessa;
	}
	
	
	
}
