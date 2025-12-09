package negocio.comuns.patrimonio;

import negocio.comuns.arquitetura.SuperVO;

public class TipoPatrimonioVO extends SuperVO{

	private static final long serialVersionUID = -3284733940971426467L;
	private String descricao;
	private Integer codigo;
	private Integer quantidadeReservasSimultaneas;
	private Integer quantidadeDiasLimiteReserva;
	
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public Integer getQuantidadeReservasSimultaneas() {
		if(quantidadeReservasSimultaneas == null){
			quantidadeReservasSimultaneas = 0;
		}
		return quantidadeReservasSimultaneas;
	}
	public void setQuantidadeReservasSimultaneas(Integer quantidadeReservasSimultaneas) {
		this.quantidadeReservasSimultaneas = quantidadeReservasSimultaneas;
	}
	public Integer getQuantidadeDiasLimiteReserva() {
		if(quantidadeDiasLimiteReserva == null){
			quantidadeDiasLimiteReserva = 0;
		}
		return quantidadeDiasLimiteReserva;
	}
	public void setQuantidadeDiasLimiteReserva(Integer quantidadeDiasLimiteReserva) {
		this.quantidadeDiasLimiteReserva = quantidadeDiasLimiteReserva;
	}

}
