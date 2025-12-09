package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;

public class FaixaSalarialVO extends SuperVO {

	private static final long serialVersionUID = -828242478759607654L;
	
	private Integer codigo;
	private String descricao;
	private Integer valor;
	
	public enum EnumCampoConsultaFaixaSalarial {
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