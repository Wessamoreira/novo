/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.compras;

import java.util.Date;

/**
 *
 * @author Philippe
 */
public class RequisicaoRelVO {

    private String categoriaDespesa;
    private String categoriaProduto;
    private Integer codigoProduto;
    private String produto;
    private Integer quantidadeEstoque;
    private Integer quantidadeRequisicao;
    private String situacao;
    private String unidadeEnsino;
    private String departamento;
    private Double valorUnitario;
    private Double valorTotal;
    private Date dataSolicitacao;
    private String requisitante;
    private Double quantidadeSolicitada;
    private Integer codigoRequisicao;
    private String centroResultado;
    private String formaAutorizacao;
    private String responsavelAutorizacao;
    private Date dataAutorizacao;
    private String centroResultadoEstoque;
    private Date dataConsumo;
    private double quantidadeRequisicaoEntrega;
    private double precoMedioUnitario;
    private double precoMedioTotal;
    private Integer codigoCentroResultadoEstoque;
    private Integer codigoCentroResultado;
    private String situacaoAutorizacao;
    
    
    
    
    public String getCategoriaDespesa() {
        if (categoriaDespesa == null) {
            categoriaDespesa = "";
        }
        return categoriaDespesa;
    }

    public void setCategoriaDespesa(String categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
    }

    public String getCategoriaProduto() {
        if (categoriaProduto == null) {
            categoriaProduto = "";
        }
        return categoriaProduto;
    }

    public void setCategoriaProduto(String categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public String getProduto() {
        if (produto == null) {
            produto = "";
        }
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Integer getQuantidadeEstoque() {
        if (quantidadeEstoque == null) {
            quantidadeEstoque = 0;
        }
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer getQuantidadeRequisicao() {
        if (quantidadeRequisicao == null) {
            quantidadeRequisicao = 0;
        }
        return quantidadeRequisicao;
    }

    public void setQuantidadeRequisicao(Integer quantidadeRequisicao) {
        this.quantidadeRequisicao = quantidadeRequisicao;
    }

    public Integer getCodigoProduto() {
        if (codigoProduto == null) {
            codigoProduto = 0;
        }
        return codigoProduto;
    }

    public void setCodigoProduto(Integer codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacaoEntrega_Apresentar() {
        if (situacao.equals("FI")) {
            return "Finalizada";
        }
        if (situacao.equals("PE")) {
            return "Pendente";
        }
        if (situacao.equals("PA")) {
            return "Parcial";
        }
        return (situacao);
    }

    public String getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = "";
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

	public String getDepartamento() {
		if (departamento == null) {
			departamento = "";
		}
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public Double getValorUnitario() {
		if (valorUnitario == null) {
			valorUnitario = 0.0;
		}
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Double getQuantidadeSolicitada() {
		if (quantidadeSolicitada == null) {
			quantidadeSolicitada = 0.0;
		}
		return quantidadeSolicitada;
	}

	public void setQuantidadeSolicitada(Double quantidadeSolicitada) {
		this.quantidadeSolicitada = quantidadeSolicitada;
	}

	public Double getValorTotal() {
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getRequisitante() {
		if (requisitante == null) {
			requisitante = "";
		}
		return requisitante;
	}

	public void setRequisitante(String requisitante) {
		this.requisitante = requisitante;
	}

	public Integer getCodigoRequisicao() {
		if (codigoRequisicao == null) {
			codigoRequisicao = 0;
        }
		return codigoRequisicao;
	}

	public void setCodigoRequisicao(Integer codigoRequisicao) {
		this.codigoRequisicao = codigoRequisicao;
	}

	public String getCentroResultado() {
		if (centroResultado == null) {
			centroResultado = "";
        }
		return centroResultado;
	}

	public void setCentroResultado(String centroResultado) {
		this.centroResultado = centroResultado;
	}

	public String getFormaAutorizacao() {
		if (formaAutorizacao == null) {
			formaAutorizacao = "";
        }
		return formaAutorizacao;
	}

	public void setFormaAutorizacao(String formaAutorizacao) {
		this.formaAutorizacao = formaAutorizacao;
	}

	public String getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = "";
        }
		return responsavelAutorizacao;
	}

	public void setResponsavelAutorizacao(String responsavelAutorizacao) {
		this.responsavelAutorizacao = responsavelAutorizacao;
	}

	public Date getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataAutorizacao(Date dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public String getCentroResultadoEstoque() {
		return centroResultadoEstoque;
	}

	public void setCentroResultadoEstoque(String centroResultadoEstoque) {
		this.centroResultadoEstoque = centroResultadoEstoque;
	}

	public Date getDataConsumo() {
		return dataConsumo;
	}

	public void setDataConsumo(Date dataConsumo) {
		this.dataConsumo = dataConsumo;
	}

	public double getQuantidadeRequisicaoEntrega() {
		return quantidadeRequisicaoEntrega;
	}

	public void setQuantidadeRequisicaoEntrega(double quantidadeRequisicaoEntrega) {
		this.quantidadeRequisicaoEntrega = quantidadeRequisicaoEntrega;
	}

	public double getPrecoMedioUnitario() {
		return precoMedioUnitario;
	}

	public void setPrecoMedioUnitario(double precoMedioUnitario) {
		this.precoMedioUnitario = precoMedioUnitario;
	}

	public double getPrecoMedioTotal() {
		return precoMedioTotal;
	}

	public void setPrecoMedioTotal(double precoMedioTotal) {
		this.precoMedioTotal = precoMedioTotal;
	}

	public Integer getCodigoCentroResultadoEstoque() {
		return codigoCentroResultadoEstoque;
	}

	public void setCodigoCentroResultadoEstoque(Integer codigoCentroResultadoEstoque) {
		this.codigoCentroResultadoEstoque = codigoCentroResultadoEstoque;
	}

	public Integer getCodigoCentroResultado() {
		return codigoCentroResultado;
	}

	public void setCodigoCentroResultado(Integer codigoCentroResultado) {
		this.codigoCentroResultado = codigoCentroResultado;
	}

	public String getSituacaoAutorizacao() {
		if(situacaoAutorizacao == null) {
			situacaoAutorizacao = "";
		}
		return situacaoAutorizacao;
	}

	public void setSituacaoAutorizacao(String situacaoAutorizacao) {
		this.situacaoAutorizacao = situacaoAutorizacao;
	}
	
}
