package negocio.comuns.compras;

import java.util.Date;
import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;

/*
 * Pedro Andrade	
 */
public class OperacaoEstoqueVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2697744535078777744L;
	private Integer codigo;
	private EstoqueVO estoqueVO;
	private Date data;
	private String codOrigem;
	private TipoOperacaoEstoqueOrigemEnum tipoOperacaoEstoqueOrigemEnum;
	private OperacaoEstoqueEnum operacaoEstoqueEnum;
	private Double quantidade;
	private UsuarioVO usuario;

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	

	public EstoqueVO getEstoqueVO() {
		estoqueVO = Optional.ofNullable(estoqueVO).orElse(new EstoqueVO());
		return estoqueVO;
	}

	public void setEstoqueVO(EstoqueVO estoqueVO) {
		this.estoqueVO = estoqueVO;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getCodOrigem() {
		codOrigem = Optional.ofNullable(codOrigem).orElse("");
		return codOrigem;
	}

	public void setCodOrigem(String codOrigem) {
		this.codOrigem = codOrigem;
	}

	public TipoOperacaoEstoqueOrigemEnum getTipoOperacaoEstoqueOrigemEnum() {
		return tipoOperacaoEstoqueOrigemEnum;
	}

	public void setTipoOperacaoEstoqueOrigemEnum(TipoOperacaoEstoqueOrigemEnum tipoOperacaoEstoqueOrigemEnum) {
		this.tipoOperacaoEstoqueOrigemEnum = tipoOperacaoEstoqueOrigemEnum;
	}

	public OperacaoEstoqueEnum getOperacaoEstoqueEnum() {
		return operacaoEstoqueEnum;
	}

	public void setOperacaoEstoqueEnum(OperacaoEstoqueEnum operacaoEstoqueEnum) {
		this.operacaoEstoqueEnum = operacaoEstoqueEnum;
	}

	public Double getQuantidade() {
		quantidade = Optional.ofNullable(quantidade).orElse(0.0);
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public UsuarioVO getUsuario() {
		usuario = Optional.ofNullable(usuario).orElse(new UsuarioVO());
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}
	
	

}
