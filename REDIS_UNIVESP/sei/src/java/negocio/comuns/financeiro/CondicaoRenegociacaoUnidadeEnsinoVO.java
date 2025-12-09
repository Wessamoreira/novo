package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class CondicaoRenegociacaoUnidadeEnsinoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private CondicaoRenegociacaoVO condicaoRenegociacaoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ContaCorrenteVO contaCorrenteVO;
	private List<SelectItem> listaContaCorrenteCondicaoRenegociacaoVOs;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CondicaoRenegociacaoVO getCondicaoRenegociacaoVO() {
		if (condicaoRenegociacaoVO == null) {
			condicaoRenegociacaoVO = new CondicaoRenegociacaoVO();
		}
		return condicaoRenegociacaoVO;
	}
	public void setCondicaoRenegociacaoVO(CondicaoRenegociacaoVO condicaoRenegociacaoVO) {
		this.condicaoRenegociacaoVO = condicaoRenegociacaoVO;
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
	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}
	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public List<SelectItem> getListaContaCorrenteCondicaoRenegociacaoVOs() {
		if (listaContaCorrenteCondicaoRenegociacaoVOs == null) {
			listaContaCorrenteCondicaoRenegociacaoVOs = new ArrayList<SelectItem>(0);
		}
		return listaContaCorrenteCondicaoRenegociacaoVOs;
	}

	public void setListaContaCorrenteCondicaoRenegociacaoVOs(List<SelectItem> listaContaCorrenteCondicaoRenegociacaoVOs) {
		this.listaContaCorrenteCondicaoRenegociacaoVOs = listaContaCorrenteCondicaoRenegociacaoVOs;
	}

}
