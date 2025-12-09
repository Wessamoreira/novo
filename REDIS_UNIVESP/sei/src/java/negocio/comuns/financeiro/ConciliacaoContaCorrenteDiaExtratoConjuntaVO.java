package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.StreamSeiException;

public class ConciliacaoContaCorrenteDiaExtratoConjuntaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -761684383836208845L;
	private Integer codigo;
	private Integer codigoOfx;
	private Double valorOfx;
	private String lancamentoOfx;
	private String documentoOfx;
	private ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtrato;	

	public ConciliacaoContaCorrenteDiaExtratoConjuntaVO getClone() {
		try {
			ConciliacaoContaCorrenteDiaExtratoConjuntaVO clone = (ConciliacaoContaCorrenteDiaExtratoConjuntaVO) super.clone();
			clone.setCodigo(0);
			clone.setConciliacaoContaCorrenteDiaExtrato(new ConciliacaoContaCorrenteDiaExtratoVO());
			clone.setNovoObj(true);
			clone.setCodigoOfx(getCodigoOfx());
			clone.setValorOfx(getValorOfx());
			clone.setLancamentoOfx(getLancamentoOfx());
			clone.setDocumentoOfx(getDocumentoOfx());
			return clone;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public void preencherDados(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtrato) {
		setCodigoOfx(conciliacaoExtrato.getCodigoOfx());
		setValorOfx(conciliacaoExtrato.getValorOfx());
		setLancamentoOfx(conciliacaoExtrato.getLancamentoOfx());
		setDocumentoOfx(conciliacaoExtrato.getDocumentoOfx());		
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

	public Integer getCodigoOfx() {
		if (codigoOfx == null) {
			codigoOfx = 0;
		}
		return codigoOfx;
	}

	public void setCodigoOfx(Integer codigoOfx) {
		this.codigoOfx = codigoOfx;
	}

	public Double getValorOfx() {
		if (valorOfx == null) {
			valorOfx = 0.0;
		}
		return valorOfx;
	}

	public void setValorOfx(Double valorOfx) {
		this.valorOfx = valorOfx;
	}

	public String getLancamentoOfx() {
		if (lancamentoOfx == null) {
			lancamentoOfx = "";
		}
		return lancamentoOfx;
	}

	public void setLancamentoOfx(String lancamentoOfx) {
		this.lancamentoOfx = lancamentoOfx;
	}

	public String getDocumentoOfx() {
		if (documentoOfx == null) {
			documentoOfx = "";
		}
		return documentoOfx;
	}

	public void setDocumentoOfx(String documentoOfx) {
		this.documentoOfx = documentoOfx;
	}

	public ConciliacaoContaCorrenteDiaExtratoVO getConciliacaoContaCorrenteDiaExtrato() {
		if (conciliacaoContaCorrenteDiaExtrato == null) {
			conciliacaoContaCorrenteDiaExtrato = new ConciliacaoContaCorrenteDiaExtratoVO();
		}
		return conciliacaoContaCorrenteDiaExtrato;
	}

	public void setConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtrato) {
		this.conciliacaoContaCorrenteDiaExtrato = conciliacaoContaCorrenteDiaExtrato;
	}
	

}
