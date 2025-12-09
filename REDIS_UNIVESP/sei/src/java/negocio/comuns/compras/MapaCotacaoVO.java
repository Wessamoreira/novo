/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Rodrigo
 */
public class MapaCotacaoVO extends SuperVO {

	private Integer codigo;
	private CotacaoVO cotacaoVO;
	private List<CompraVO> compraVOs;
	private List<CompraVO> compraRejeitadaVOs;
	private List<CompraVO> compraRejeitadaTemporariaVOs;
	private HashMap<String, List<CompraVO>> hashCompraRejeitadaVOs;
	private CotacaoHistoricoVO cotacaoHistorico;
	public static final long serialVersionUID = 1L;

	public MapaCotacaoVO() {
	}

	public List<CompraVO> getCompraVOs() {
		if (compraVOs == null) {
			compraVOs = new ArrayList<>();
		}
		return compraVOs;
	}

	public void setCompraVOs(List<CompraVO> compraVOs) {
		this.compraVOs = compraVOs;
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

	public CotacaoVO getCotacaoVO() {
		if (cotacaoVO == null) {
			cotacaoVO = new CotacaoVO();
		}
		return cotacaoVO;
	}

	public void setCotacaoVO(CotacaoVO cotacaoVO) {
		this.cotacaoVO = cotacaoVO;
	}

	public List<CompraVO> getCompraRejeitadaVOs() {
		if (compraRejeitadaVOs == null) {
			compraRejeitadaVOs = new ArrayList<CompraVO>(0);
		}
		return compraRejeitadaVOs;
	}

	public void setCompraRejeitadaVOs(List<CompraVO> compraRejeitadaVOs) {
		this.compraRejeitadaVOs = compraRejeitadaVOs;
	}

	public HashMap<String, List<CompraVO>> getHashCompraRejeitadaVOs() {
		if (hashCompraRejeitadaVOs == null) {
			hashCompraRejeitadaVOs = new HashMap<String, List<CompraVO>>(0);
		}
		return hashCompraRejeitadaVOs;
	}

	public void setHashCompraRejeitadaVOs(HashMap<String, List<CompraVO>> hashCompraRejeitadaVOs) {
		this.hashCompraRejeitadaVOs = hashCompraRejeitadaVOs;
	}

	public List<CompraVO> getCompraRejeitadaTemporariaVOs() {
		if (compraRejeitadaTemporariaVOs == null) {
			compraRejeitadaTemporariaVOs = new ArrayList<CompraVO>(0);
		}
		return compraRejeitadaTemporariaVOs;
	}

	public CotacaoHistoricoVO getCotacaoHistorico() {
		return cotacaoHistorico;
	}

	public void setCotacaoHistorico(CotacaoHistoricoVO cotacaoHistorico) {
		this.cotacaoHistorico = cotacaoHistorico;
	}

	public void setCompraRejeitadaTemporariaVOs(List<CompraVO> compraRejeitadaTemporariaVOs) {
		this.compraRejeitadaTemporariaVOs = compraRejeitadaTemporariaVOs;
	}
	
	public boolean isExisteCotacaoHistorico(){
		return Uteis.isAtributoPreenchido(getCotacaoHistorico());
	}

}
