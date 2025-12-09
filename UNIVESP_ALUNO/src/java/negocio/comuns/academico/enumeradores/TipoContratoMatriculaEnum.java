package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoContratoMatriculaEnum {

	NORMAL("NO", "Matrícula Normal"), EXTENSAO("EX", "Matrícula de Extensão"), FIADOR("FI", "Fiador");

	private TipoContratoMatriculaEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	private String valor;
	private String descricao;

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
	
	public static TipoContratoMatriculaEnum getEnumPorValor(String valor) {
		for(TipoContratoMatriculaEnum tipoContratoMatriculaEnum: TipoContratoMatriculaEnum.values()) {
			if(tipoContratoMatriculaEnum.getValor().equals(valor)) {
				return tipoContratoMatriculaEnum;
			}
		}
		return TipoContratoMatriculaEnum.NORMAL;
	}
	
	public boolean isContratoNormal(){
		return Uteis.isAtributoPreenchido(this.name()) && this.name().equals(TipoContratoMatriculaEnum.NORMAL.name());
	}
	
	public boolean isContratoExtensao(){
		return Uteis.isAtributoPreenchido(this.name()) && this.name().equals(TipoContratoMatriculaEnum.EXTENSAO.name());
	}
	
	public boolean isContratoFiador(){
		return Uteis.isAtributoPreenchido(this.name()) && this.name().equals(TipoContratoMatriculaEnum.FIADOR.name());
	}

}
