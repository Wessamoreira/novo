package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;

public class NivelSalarialVO extends SuperVO {

	private static final long serialVersionUID = 6419792483432213679L;
	
	private Integer codigo;
	private String descricao;
	private Integer valor;
	
	public enum EnumCampoConsultaNivelSalarial {
		DESCRICAO, 
		VALOR;
	}
	
	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}
	public String getDescricao() {
		if (descricao == null)
			descricao = "";
		return descricao;
	}
	public Integer getValor() {
		if (valor == null)
			valor = 0;
		return valor;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setValor(Integer valor) {
		this.valor = valor;
	}
	
}