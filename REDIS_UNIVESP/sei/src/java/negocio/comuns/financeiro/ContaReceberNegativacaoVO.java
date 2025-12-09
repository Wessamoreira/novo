package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class ContaReceberNegativacaoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
    private ContaReceberVO contaReceber;
    private RegistroNegativacaoCobrancaContaReceberVO registro;
    /*
     * transiente
     */
    private Double valorTotal;
    private Integer quantidadeContas;
    private List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs;
    

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public ContaReceberVO getContaReceber() {
        if (contaReceber == null) {
            contaReceber = new ContaReceberVO();
        }
        return contaReceber;
    }

    public void setContaReceber(ContaReceberVO contaReceber) {
        this.contaReceber = contaReceber;
    }

	public RegistroNegativacaoCobrancaContaReceberVO getRegistro() {
		if (registro == null) {
			registro = new RegistroNegativacaoCobrancaContaReceberVO();
		}
		return registro;
	}

	public void setRegistro(RegistroNegativacaoCobrancaContaReceberVO registro) {
		this.registro = registro;
	}

	public List<RegistroNegativacaoCobrancaContaReceberItemVO> getRegistroNegativacaoCobrancaContaReceberItemVOs() {
		if(registroNegativacaoCobrancaContaReceberItemVOs == null) {
			registroNegativacaoCobrancaContaReceberItemVOs =  new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>(0);
		}
		return registroNegativacaoCobrancaContaReceberItemVOs;
	}

	public void setRegistroNegativacaoCobrancaContaReceberItemVOs(
			List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs) {
		this.registroNegativacaoCobrancaContaReceberItemVOs = registroNegativacaoCobrancaContaReceberItemVOs;
	}

	public Double getValorTotal() {
		if(valorTotal == null) {
			valorTotal = getRegistroNegativacaoCobrancaContaReceberItemVOs().stream().mapToDouble(RegistroNegativacaoCobrancaContaReceberItemVO::getValor).sum();
		}
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Integer getQuantidadeContas() {
		if(quantidadeContas ==  null) {
			quantidadeContas = getRegistroNegativacaoCobrancaContaReceberItemVOs().size();
		}
		return quantidadeContas;
	}

	public void setQuantidadeContas(Integer quantidadeContas) {
		this.quantidadeContas = quantidadeContas;
	}
	
	
    
}