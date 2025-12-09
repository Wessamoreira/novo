/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author otimize-ti
 */
public class OrdemDescontoVO extends SuperVO {

    private String descricaoDesconto;// "Desconto Aluno"; "Plano Desconto"; // "Convênio" //Desc.Progressivo
    private Boolean valorCheio;
    private Integer posicaoAtual;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>MatriculaPeriodo</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public OrdemDescontoVO() {
        super();
    }

    /**
     * @return the descricaoDesconto
     */
    public String getDescricaoDesconto() {
        if (descricaoDesconto == null) {
            descricaoDesconto = "";
        }
        return descricaoDesconto;
    }

    /**
     * @param descricaoDesconto
     *            the descricaoDesconto to set
     */
    public void setDescricaoDesconto(String descricaoDesconto) {
        this.descricaoDesconto = descricaoDesconto;
    }

    /**
     * @return the valorCheio
     */
    public Boolean getValorCheio() {
        if (valorCheio == null) {
            valorCheio = true;
        }
        return valorCheio;
    }

    /**
     * @param valorCheio
     *            the valorCheio to set
     */
    public void setValorCheio(Boolean valorCheio) {
        this.valorCheio = valorCheio;
    }

    /**
     * @return the posicaoAtual
     */
    public Integer getPosicaoAtual() {
        if (posicaoAtual == null) {
            posicaoAtual = 0;
        }
        return posicaoAtual;
    }

    /**
     * @param posicaoAtual
     *            the posicaoAtual to set
     */
    public void setPosicaoAtual(Integer posicaoAtual) {
        this.posicaoAtual = posicaoAtual;
    }

    public Boolean isConvenio() {
        if (this.getDescricaoDesconto().equals("Convênio")) {
            return true;
        }
        return false;
    }

    public Boolean isDescontoProgressivo() {
        if (this.getDescricaoDesconto().equals("Desc.Progressivo")) {
            return true;
        }
        return false;
    }

    public Boolean isPlanoDesconto() {
        if (this.getDescricaoDesconto().equals("Plano Desconto")) {
            return true;
        }
        return false;
    }

    public Boolean isDescontoAluno() {
        if (this.getDescricaoDesconto().equals("Desconto Aluno")) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "OrdemDesconto [" + this.getDescricaoDesconto() + ", Ordem: " + this.getPosicaoAtual() + ", Valor Cheio: " + this.getValorCheio() + "]";
    }
}
