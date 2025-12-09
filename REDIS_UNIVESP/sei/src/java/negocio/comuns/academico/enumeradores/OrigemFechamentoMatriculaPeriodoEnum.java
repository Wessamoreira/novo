/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.academico.enumeradores;

/**
 *
 * @author Rodrigo
 */
public enum OrigemFechamentoMatriculaPeriodoEnum {

    FECHAMENTO_PERIODO_LETIVO("FP","Fechamento Período Letivo", false),
    FORMATURA("FO","Formatura", false),
    TRANCAMENTO("TR","Trancamento", true),
    CANCELAMENTO("CA","Cancelamento", true),
    TRANSFERENCIA_INTERNA("TI","Transferência Interna", true),
    TRANSFERENCIA_SAIDA("TS","Transferência Saída", true),
    NAO_FECHADO("NF","Não Fechado", false),
    ABANDONO("AB","Abandono", true),
    JUBILAMENTO("JU", "Jubilamento", true);

    String valor;
    String descricao;
    boolean bloquearRegistroAula;

    private OrigemFechamentoMatriculaPeriodoEnum(String valor, String descricao, boolean bloquearRegistroAula) {
        this.valor = valor;
        this.descricao = descricao;
        this.bloquearRegistroAula = bloquearRegistroAula;
    }


    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public boolean getBloquearRegistroAula() {
		return bloquearRegistroAula;
	}
}
