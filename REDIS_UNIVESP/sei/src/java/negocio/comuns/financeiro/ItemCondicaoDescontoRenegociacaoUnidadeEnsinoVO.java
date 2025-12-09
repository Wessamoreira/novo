package negocio.comuns.financeiro;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO extends SuperVO  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1984609617396411586L;
	private Integer codigo;
	private ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public ItemCondicaoDescontoRenegociacaoVO getItemCondicaoDescontoRenegociacaoVO() {
		if (itemCondicaoDescontoRenegociacaoVO == null) {
			itemCondicaoDescontoRenegociacaoVO = new ItemCondicaoDescontoRenegociacaoVO();
		}
		return itemCondicaoDescontoRenegociacaoVO;
	}
	public void setItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoVO) {
		this.itemCondicaoDescontoRenegociacaoVO = itemCondicaoDescontoRenegociacaoVO;
	}
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

}
