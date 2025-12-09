/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Philippe
 */
public class ControleRemessaDataGeracaoVO extends SuperVO {

    private Integer codigo;
    private Date dataArquivo;
    private String nossoNumero;
    private Double valor;
    private Date dataVencimento;
    private Double desconto;    
    private String codMovRemessa;    

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getDataArquivo() {
        if (dataArquivo == null) {
            dataArquivo = new Date();
        }
        return dataArquivo;
    }

    public void setDataArquivo(Date dataArquivo) {
        this.dataArquivo = dataArquivo;
    }

    public String getNossoNumero() {
        if(nossoNumero == null) {
            nossoNumero = "";
        }
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
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

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = new Double(0);
		}
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public String getCodMovRemessa() {
		if (codMovRemessa == null) {
			codMovRemessa = "";
		}
		return codMovRemessa;
	}

	public void setCodMovRemessa(String codMovRemessa) {
		this.codMovRemessa = codMovRemessa;
	}

}
