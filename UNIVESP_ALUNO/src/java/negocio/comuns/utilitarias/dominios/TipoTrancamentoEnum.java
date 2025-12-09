package negocio.comuns.utilitarias.dominios;

public enum TipoTrancamentoEnum {
	NENHUM("NE", ""),
	JUBILAMENTO("JU", "Jubilamento"),
	TRANCAMENTO("TR", "Trancamento"),
	CANCELAMENTO_INGRESSANTE("CI","Cancelamento Ingressantes"),
	CANCELAMENTO_EXCESSO_TRANCAMENTO("CT","Cancelamento por Excesso Trancamento"),
	RENOVACAO_AUTOMATICA("RA","Renovação Automática"),
	ABANDONO_DE_CURSO("AC", "Abandono de Curso");
	
	String valor;
	String descricao;
	
	private TipoTrancamentoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public static TipoTrancamentoEnum getEnum(String valor) {
		TipoTrancamentoEnum[] valores = values();
        for (TipoTrancamentoEnum obj : valores) {
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isNenhum() {
		return name() != null && name().equals(TipoTrancamentoEnum.NENHUM.name());
	}
	
	public boolean isJubilamento() {
		return name() != null && name().equals(TipoTrancamentoEnum.JUBILAMENTO.name());
	}
	
	public boolean isTrancamento() {
		return name() != null && name().equals(TipoTrancamentoEnum.TRANCAMENTO.name());
	}
	
	public boolean isCancelamentoIngressante() {
		return name() != null && name().equals(TipoTrancamentoEnum.CANCELAMENTO_INGRESSANTE.name());
	}
	
	public boolean isCancelamentoExcessoTrancamento() {
		return name() != null && name().equals(TipoTrancamentoEnum.CANCELAMENTO_EXCESSO_TRANCAMENTO.name());
	}
	
	public boolean isRenovacaoAutomatica() {
		return name() != null && name().equals(TipoTrancamentoEnum.RENOVACAO_AUTOMATICA.name());
	}
	
	public boolean isAbandonoCurso() {
		return name() != null && name().equals(TipoTrancamentoEnum.ABANDONO_DE_CURSO.name());
	}
	
}
