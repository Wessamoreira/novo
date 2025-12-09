package negocio.comuns.administrativo.enumeradores;

public enum TipoRecorrenciaCampanhaEnum {
  DIARIA("DI","Diária"),
  SEMANAL("SE","Semanal"),
  QUINZENAL("QI","Quinzenal"),
  MENSAL("ME","Mensal"),
  BIMESTRAL("BI","Bimestral"),
  TRIMESTRAL("TR","Trimestral"),
  SEMESTRAL("SM","Semestral"),
  ANUAL("AN","Anual");
  
  private String valor;
	private String descricao;
	
	
	private TipoRecorrenciaCampanhaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
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
	
	
  
}
