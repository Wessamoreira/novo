/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.financeiro;

/**
 *
 * @author Philippe
 */
public class DescontoTipoDescontoRelVO {

    private String tipo;
    private String descricao;
    private Double valor;
    
    public DescontoTipoDescontoRelVO() {
		super();
	
	}

	public DescontoTipoDescontoRelVO(String tipo, String descricao, Double valor) {
		super();
		this.tipo = tipo;
		this.descricao = descricao;
		this.valor = valor;
	}

    public String getTipo() {
        if (tipo == null) {
            tipo = "";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "Não há disconto deste tipo para esse aluno";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
}
