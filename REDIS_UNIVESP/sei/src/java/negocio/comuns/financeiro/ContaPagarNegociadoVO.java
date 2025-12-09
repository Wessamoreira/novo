/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author rodrigo
 */
public class ContaPagarNegociadoVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2996886989065946149L;
	private Integer codigo;
    private NegociacaoContaPagarVO negociacaoContaPagarVO;
    private ContaPagarVO contaPagarVO;    
    private Double valor;        
    

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

	public NegociacaoContaPagarVO getNegociacaoContaPagarVO() {
		if (negociacaoContaPagarVO == null) {
			negociacaoContaPagarVO = new NegociacaoContaPagarVO();
		}
		return negociacaoContaPagarVO;
	}

	public void setNegociacaoContaPagarVO(NegociacaoContaPagarVO negociacaoContaPagarVO) {
		this.negociacaoContaPagarVO = negociacaoContaPagarVO;
	}

	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contaPagarVO == null) ? 0 : contaPagarVO.getCodigo().hashCode());		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaPagarNegociadoVO other = (ContaPagarNegociadoVO) obj;
		if (contaPagarVO == null) {
			if (other.contaPagarVO != null)
				return false;
		} else if (!contaPagarVO.getCodigo().equals(other.contaPagarVO.getCodigo()))
			return false;
		
		return true;
	}

    
}
