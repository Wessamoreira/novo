package negocio.comuns.financeiro.enumerador;

public enum EmpresaOperadoraCartaoEnum {
	NENHUM(OperadoraAntifraudeEnum.NENHUM),
	CIELO(OperadoraAntifraudeEnum.NENHUM),
	REDE(OperadoraAntifraudeEnum.NENHUM),
	GETNET(OperadoraAntifraudeEnum.GETNET),
	EA_BANK(OperadoraAntifraudeEnum.NENHUM);

	private final OperadoraAntifraudeEnum operadoraAntifraude;

	EmpresaOperadoraCartaoEnum(OperadoraAntifraudeEnum operadoraAntifraude) {
		this.operadoraAntifraude = operadoraAntifraude;
	}

	public OperadoraAntifraudeEnum getOperadoraAntifraude() {
		return operadoraAntifraude;
	}
    public Boolean isRede() {
        return name().equals(EmpresaOperadoraCartaoEnum.REDE.name());
    }

    public Boolean isCielo() {
        return name().equals(EmpresaOperadoraCartaoEnum.CIELO.name());
    }

    public Boolean isGetNet() {
        return name().equals(EmpresaOperadoraCartaoEnum.GETNET.name());
    }

    public Boolean isEaBank() {
        return name().equals(EmpresaOperadoraCartaoEnum.EA_BANK.name());
    }

    public Boolean isNenhum() {
        return name().equals(EmpresaOperadoraCartaoEnum.NENHUM.name());
    }
}
