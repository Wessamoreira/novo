/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.comuns.sad;

/**
 * 
 * @author Rodrigo
 */
import java.io.Serializable;

import negocio.comuns.utilitarias.Uteis;

public class LegendaGraficoVO implements Serializable {

    private Integer codigo;
    private String nome;
    private String nivel;
    private String legenda;
    private Double valor;
    private String cor;
    private Integer quantidade;
    public static final long serialVersionUID = 1L;

    public String getCor() {
        if (cor == null) {
            cor = "";
        }
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public LegendaGraficoVO(Integer codigo, String nome, String nivel, Double valor) {

        this.codigo = codigo;
        this.nome = nome;
        this.nivel = nivel;
        this.valor = valor;
        this.legenda = nome + "- R$ " + Uteis.formatarDecimal(Uteis.arrendondarForcando2CadasDecimais(valor), "0,000.00#");

    }

    public LegendaGraficoVO(Integer codigo, String nome, String nivel, Double valor, String cor, boolean concatenarValorLegenda) {

        this.codigo = codigo;
        this.nome = nome;
        this.nivel = nivel;
        this.valor = valor;
        this.cor = cor;
        if (concatenarValorLegenda) {
            this.legenda = nome + "- R$ " + Uteis.formatarDecimal(Uteis.arrendondarForcando2CadasDecimais(valor), "0,000.00#");
        } else {
            this.legenda = nome;
        }

    }

    public LegendaGraficoVO(String nome, Double valor, String cor) {

        this.nome = nome;
        this.valor = valor;
        this.legenda = nome + " - R$ " + Uteis.formatarDecimal(Uteis.arrendondarForcando2CadasDecimais(valor), "0,000.00#");
        this.cor = cor;

    }
    
    public LegendaGraficoVO(String nome, Integer quantidade, String cor) {

        this.nome = nome;
        this.quantidade = quantidade;
        this.legenda = nome + " - Quantidade: " + quantidade;
        this.cor = cor;

    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Integer getQuantidade() {
		if (quantidade == null) {
			quantidade = 0;
		}
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}
