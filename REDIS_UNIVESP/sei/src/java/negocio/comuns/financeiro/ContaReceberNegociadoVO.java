/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author rodrigo
 */
public class ContaReceberNegociadoVO extends SuperVO {

    private Integer codigo;
    private NegociacaoContaReceberVO negociacaoContaReceber;
    private ContaReceberVO contaReceber;
    private Long nrDiasAtraso;
    private Double valor;
    private Double valorOriginalConta;
    private Boolean desconsiderarDescontoProgressivo;
    private Double valorDescontoProgressivoDesconsiderado;
    private Boolean desconsiderarDescontoAluno;
    private Double valorDescontoAlunoDesconsiderado;
    private Boolean desconsiderarDescontoInstituicaoComValidade;
    private Double valorDescontoInstituicaoComValidadeDesconsiderado;
    private Boolean desconsiderarDescontoInstituicaoSemValidade;
    private Double valorDescontoInstituicaoSemValidadeDesconsiderado;
    /*private ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoVO;
    private Double valorOriginalJuro;
    private Double valorOriginalMulta;*/
    
    
    
    public static final long serialVersionUID = 1L;

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

    public Long getNrDiasAtraso() {
        if (nrDiasAtraso == null) {
            nrDiasAtraso = 0L;
        }
        return nrDiasAtraso;
    }

    public void setNrDiasAtraso(Long nrDiasAtraso) {
        this.nrDiasAtraso = nrDiasAtraso;
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

    public void setContaReceber(ContaReceberVO contaReceber) {
        this.contaReceber = contaReceber;
    }

    public NegociacaoContaReceberVO getNegociacaoContaReceber() {
        if (negociacaoContaReceber == null) {
            negociacaoContaReceber = new NegociacaoContaReceberVO();
        }
        return negociacaoContaReceber;
    }

    public void setNegociacaoContaReceber(NegociacaoContaReceberVO negociacaoContaReceber) {
        this.negociacaoContaReceber = negociacaoContaReceber;
    }
    

	public Boolean getDesconsiderarDescontoProgressivo() {
		if(desconsiderarDescontoProgressivo == null){
			desconsiderarDescontoProgressivo = false;
		}
		return desconsiderarDescontoProgressivo;
	}

	public void setDesconsiderarDescontoProgressivo(Boolean desconsiderarDescontoProgressivo) {
		this.desconsiderarDescontoProgressivo = desconsiderarDescontoProgressivo;
	}

	public Boolean getDesconsiderarDescontoAluno() {
		if(desconsiderarDescontoAluno == null){
			desconsiderarDescontoAluno = false;
		}
		return desconsiderarDescontoAluno;
	}

	public void setDesconsiderarDescontoAluno(Boolean desconsiderarDescontoAluno) {
		this.desconsiderarDescontoAluno = desconsiderarDescontoAluno;
	}

	public Boolean getDesconsiderarDescontoInstituicaoComValidade() {
		if(desconsiderarDescontoInstituicaoComValidade == null){
			desconsiderarDescontoInstituicaoComValidade = false;
		}
		return desconsiderarDescontoInstituicaoComValidade;
	}

	public void setDesconsiderarDescontoInstituicaoComValidade(Boolean desconsiderarDescontoInstituicaoComValidade) {
		this.desconsiderarDescontoInstituicaoComValidade = desconsiderarDescontoInstituicaoComValidade;
	}

	public Boolean getDesconsiderarDescontoInstituicaoSemValidade() {
		if(desconsiderarDescontoInstituicaoSemValidade == null){
			desconsiderarDescontoInstituicaoSemValidade = false;
		}
		return desconsiderarDescontoInstituicaoSemValidade;
	}

	public void setDesconsiderarDescontoInstituicaoSemValidade(Boolean desconsiderarDescontoInstituicaoSemValidade) {
		this.desconsiderarDescontoInstituicaoSemValidade = desconsiderarDescontoInstituicaoSemValidade;
	}

	public Double getValorDescontoProgressivoDesconsiderado() {
		if(valorDescontoProgressivoDesconsiderado == null){
			valorDescontoProgressivoDesconsiderado = 0.0;
		}
		return valorDescontoProgressivoDesconsiderado;
	}

	public void setValorDescontoProgressivoDesconsiderado(Double valorDescontoProgressivoDesconsiderado) {
		this.valorDescontoProgressivoDesconsiderado = valorDescontoProgressivoDesconsiderado;
	}

	public Double getValorDescontoAlunoDesconsiderado() {
		if(valorDescontoAlunoDesconsiderado == null){
			valorDescontoAlunoDesconsiderado = 0.0;
		}
		return valorDescontoAlunoDesconsiderado;
	}

	public void setValorDescontoAlunoDesconsiderado(Double valorDescontoAlunoDesconsiderado) {
		
		this.valorDescontoAlunoDesconsiderado = valorDescontoAlunoDesconsiderado;
	}

	public Double getValorDescontoInstituicaoComValidadeDesconsiderado() {
		if(valorDescontoInstituicaoComValidadeDesconsiderado == null){
			valorDescontoInstituicaoComValidadeDesconsiderado = 0.0;
		}
		return valorDescontoInstituicaoComValidadeDesconsiderado;
	}

	public void setValorDescontoInstituicaoComValidadeDesconsiderado(
			Double valorDescontoInstituicaoComValidadeDesconsiderado) {		
		this.valorDescontoInstituicaoComValidadeDesconsiderado = valorDescontoInstituicaoComValidadeDesconsiderado;
	}

	public Double getValorDescontoInstituicaoSemValidadeDesconsiderado() {
		if(valorDescontoInstituicaoSemValidadeDesconsiderado == null){
			valorDescontoInstituicaoSemValidadeDesconsiderado = 0.0;
		}
		return valorDescontoInstituicaoSemValidadeDesconsiderado;
	}

	public void setValorDescontoInstituicaoSemValidadeDesconsiderado(Double valorDescontoInstituicaoSemValidadeDesconsiderado) {
		this.valorDescontoInstituicaoSemValidadeDesconsiderado = valorDescontoInstituicaoSemValidadeDesconsiderado;
	}

	public Double getValorOriginalConta() {
		if(valorOriginalConta == null){
			valorOriginalConta = getValor();
		}
		return valorOriginalConta;
	}

	public void setValorOriginalConta(Double valorOriginalConta) {
		this.valorOriginalConta = valorOriginalConta;
	}
    

	public Date getOrdenacao(){
		return getContaReceber().getDataVencimento();
	}

	/*public ItemCondicaoDescontoRenegociacaoVO getItemCondicaoDescontoRenegociacaoVO() {
		itemCondicaoDescontoRenegociacaoVO = Optional.ofNullable(itemCondicaoDescontoRenegociacaoVO).orElse(new ItemCondicaoDescontoRenegociacaoVO());
		return itemCondicaoDescontoRenegociacaoVO;
	}

	public void setItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoVO) {
		this.itemCondicaoDescontoRenegociacaoVO = itemCondicaoDescontoRenegociacaoVO;
	}

	public Double getValorOriginalJuro() {
		valorOriginalJuro = Optional.ofNullable(valorOriginalJuro).orElse(0.0);
		return valorOriginalJuro;
	}

	public void setValorOriginalJuro(Double valorOriginalJuro) {
		this.valorOriginalJuro = valorOriginalJuro;
	}

	public Double getValorOriginalMulta() {
		valorOriginalMulta = Optional.ofNullable(valorOriginalMulta).orElse(0.0);
		return valorOriginalMulta;
	}

	public void setValorOriginalMulta(Double valorOriginalMulta) {
		this.valorOriginalMulta = valorOriginalMulta;
	}
	
	public boolean isExisteItemCondicaoDescontoRenegociacao(){
		return Uteis.isAtributoPreenchido(getItemCondicaoDescontoRenegociacaoVO());
	}*/
	
	public PlanoFinanceiroAlunoDescricaoDescontosVO getPlanoFinanceiroAlunoDescricaoDescontosValido (Date dataReferenciaQuitacao) throws Exception{
		PlanoFinanceiroAlunoDescricaoDescontosVO pfaddAtual = null;
		for (PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO : getContaReceber().getListaDescontosAplicavesContaReceber()) {
			if (!planoFinanceiroAlunoDescricaoDescontosVO.getIsAplicavelDataParaQuitacao(getContaReceber().getDataVencimento(), dataReferenciaQuitacao)) {
				pfaddAtual =  planoFinanceiroAlunoDescricaoDescontosVO;
				break;
			}
		}
		return pfaddAtual;
	}
	
	
	
	
	
}
