package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class CondicaoDescontoRenegociacaoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String descricao;
	private List<ItemCondicaoDescontoRenegociacaoVO> itemCondicaoDescontoRenegociacaoVOs;

	public CondicaoDescontoRenegociacaoVO() {
		super();
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

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<ItemCondicaoDescontoRenegociacaoVO> getItemCondicaoDescontoRenegociacaoVOs() {
		if (itemCondicaoDescontoRenegociacaoVOs == null) {
			itemCondicaoDescontoRenegociacaoVOs = new ArrayList<>(0);
		}
		return itemCondicaoDescontoRenegociacaoVOs;
	}

	public void setItemCondicaoDescontoRenegociacaoVOs(List<ItemCondicaoDescontoRenegociacaoVO> itemCondicaoDescontoRenegociacaoVOs) {
		this.itemCondicaoDescontoRenegociacaoVOs = itemCondicaoDescontoRenegociacaoVOs;
	}
}
