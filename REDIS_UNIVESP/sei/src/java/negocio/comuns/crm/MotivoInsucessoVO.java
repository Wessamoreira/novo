/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.crm;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Philippe
 */
public class MotivoInsucessoVO extends SuperVO {
    private Integer codigo;
    private String descricao;

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    

}
