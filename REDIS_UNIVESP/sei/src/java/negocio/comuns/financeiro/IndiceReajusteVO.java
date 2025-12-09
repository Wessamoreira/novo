package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class IndiceReajusteVO extends SuperVO implements Serializable  {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String descricao;
	private List<IndiceReajustePeriodoVO> listaIndiceReajustePeriodoVOs;
	
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
	public List<IndiceReajustePeriodoVO> getListaIndiceReajustePeriodoVOs() {
		if (listaIndiceReajustePeriodoVOs == null) {
			listaIndiceReajustePeriodoVOs = new ArrayList<IndiceReajustePeriodoVO>(0);
		}
		return listaIndiceReajustePeriodoVOs;
	}
	public void setListaIndiceReajustePeriodoVOs(List<IndiceReajustePeriodoVO> listaIndiceReajustePeriodoVOs) {
		this.listaIndiceReajustePeriodoVOs = listaIndiceReajustePeriodoVOs;
	}
	
	
	

}
