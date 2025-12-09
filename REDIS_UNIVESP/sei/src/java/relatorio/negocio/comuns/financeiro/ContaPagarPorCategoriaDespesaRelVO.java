package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Carlos
 */
public class ContaPagarPorCategoriaDespesaRelVO {

    private String categoriaDespesa;
    private Date dataVencimento;
    private String fornecedor;
    private String banco;
    private String funcionario;
    private String nrDocumento;
    private String identificadorTurma;
    private String identificadorCategoriaDespesa;
    private Double valor;
    private Double juro;
    private Double multa;
    private Double desconto;
    private Double valorPago;
    private Double valorCategoriaDespesa;
    private Date data;
    private String favorecido;
    private String situacaoContaPagar;
    private List<ContaPagarCategoriaDespesaRelVO> listaContaPagarCategoriaDespesaRelVO;

    public ContaPagarPorCategoriaDespesaRelVO() {
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getFornecedor() {
        if (fornecedor == null) {
            fornecedor = "";
        }
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getBanco() {
        if (banco == null) {
            banco = "";
        }
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getFuncionario() {
        if (funcionario == null) {
            funcionario = "";
        }
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public String getIdentificadorTurma() {
        if (identificadorTurma == null) {
            identificadorTurma = "";
        }
        return identificadorTurma;
    }

    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
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

    public Double getJuro() {
        if (juro == null) {
            juro = 0.0;
        }
        return juro;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public Double getMulta() {
        if (multa == null) {
            multa = 0.0;
        }
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public Double getDesconto() {
        if (desconto == null) {
            desconto = 0.0;
        }
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getValorPago() {
        if (valorPago == null) {
            valorPago = 0.0;
        }
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFavorecido() {
        if (favorecido == null) {
            favorecido = "";
        }
        return favorecido;
    }

    public void setFavorecido(String favorecido) {
        this.favorecido = favorecido;
    }

    public JRDataSource getListaContaPagarCategoriaDespesaRelVOJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaContaPagarCategoriaDespesaRelVO().toArray());
        return jr;
    }

    public List<ContaPagarCategoriaDespesaRelVO> getListaContaPagarCategoriaDespesaRelVO() {
        if (listaContaPagarCategoriaDespesaRelVO == null) {
            listaContaPagarCategoriaDespesaRelVO = new ArrayList(0);
        }
        return listaContaPagarCategoriaDespesaRelVO;
    }

    public void setListaContaPagarCategoriaDespesaRelVO(List<ContaPagarCategoriaDespesaRelVO> listaContaPagarCategoriaDespesaRelVO) {
        this.listaContaPagarCategoriaDespesaRelVO = listaContaPagarCategoriaDespesaRelVO;
    }

    public String getCategoriaDespesa() {
        if (categoriaDespesa == null) {
            categoriaDespesa = "";
        }
        return categoriaDespesa;
    }

    public void setCategoriaDespesa(String categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
    }

	public String getIdentificadorCategoriaDespesa() {
		if(identificadorCategoriaDespesa == null){
			identificadorCategoriaDespesa = "";
		}
		return identificadorCategoriaDespesa;
	}

	public void setIdentificadorCategoriaDespesa(String identificadorCategoriaDespesa) {
		this.identificadorCategoriaDespesa = identificadorCategoriaDespesa;
	}

	public Double getValorCategoriaDespesa() {
		if (valorCategoriaDespesa == null) {
			valorCategoriaDespesa = 0.0;
		}
		return valorCategoriaDespesa;
	}

	public void setValorCategoriaDespesa(Double valorCategoriaDespesa) {
		this.valorCategoriaDespesa = valorCategoriaDespesa;
	}

	public String getSituacaoContaPagar() {
		if (situacaoContaPagar == null) {
			situacaoContaPagar = "";
		}
		return situacaoContaPagar;
	}

	public void setSituacaoContaPagar(String situacaoContaPagar) {
		this.situacaoContaPagar = situacaoContaPagar;
	}
    
    
}
