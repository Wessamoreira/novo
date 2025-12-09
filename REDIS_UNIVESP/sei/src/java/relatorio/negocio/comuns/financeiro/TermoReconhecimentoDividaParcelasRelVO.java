/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
public class TermoReconhecimentoDividaParcelasRelVO {

    private String dataVencimento;
    private String servico;
    private String parcela;
    private Double valor;
    private String nrDocumento;
    private String centroReceita;

    public String getDataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = "";
        }
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getServico() {
        if (servico == null) {
            servico = "";
        }
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getParcela() {
        if (parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
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

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

	public String getCentroReceita() {
		if (centroReceita == null) {
			centroReceita = "";
		}
		return centroReceita;
	}

	public void setCentroReceita(String centroReceita) {
		this.centroReceita = centroReceita;
	}
}
