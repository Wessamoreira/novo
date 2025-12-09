package negocio.comuns.faturamento.nfe;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro Otimize
 *
 */
public class NotaFiscalEntradaRecebimentoCompraVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 897750239502615377L;
	private Integer codigo;
	private NotaFiscalEntradaVO notaFiscalEntradaVO;
	private RecebimentoCompraVO recebimentoCompraVO;
	/*
	 * transient
	 */
	private boolean selecionado = false;



	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public NotaFiscalEntradaVO getNotaFiscalEntradaVO() {
		if (notaFiscalEntradaVO == null) {
			notaFiscalEntradaVO = new NotaFiscalEntradaVO();
		}
		return notaFiscalEntradaVO;
	}

	public void setNotaFiscalEntradaVO(NotaFiscalEntradaVO notaFiscalEntradaVO) {
		this.notaFiscalEntradaVO = notaFiscalEntradaVO;
	}	

	public RecebimentoCompraVO getRecebimentoCompraVO() {
		if (recebimentoCompraVO == null) {
			recebimentoCompraVO = new RecebimentoCompraVO();
		}
		return recebimentoCompraVO;
	}

	public void setRecebimentoCompraVO(RecebimentoCompraVO recebimentoCompraVO) {
		this.recebimentoCompraVO = recebimentoCompraVO;
	}
	
	public boolean isSelecionado() {
		
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public boolean isExisteRecebimentoItemSelecionado() {
		return  isSelecionado() &&  Uteis.isAtributoPreenchido(getRecebimentoCompraVO()) && Uteis.isAtributoPreenchido(getRecebimentoCompraVO().getRecebimentoCompraItemVOs()) && getRecebimentoCompraVO().getRecebimentoCompraItemVOs().stream().anyMatch(RecebimentoCompraItemVO::isSelecionado);
	}

	public boolean equalsCampoSelecaoLista(NotaFiscalEntradaRecebimentoCompraVO obj) {
		return Uteis.isAtributoPreenchido(getRecebimentoCompraVO()) && Uteis.isAtributoPreenchido(obj.getRecebimentoCompraVO()) && getRecebimentoCompraVO().getCodigo().equals(obj.getRecebimentoCompraVO().getCodigo());

	}

}
