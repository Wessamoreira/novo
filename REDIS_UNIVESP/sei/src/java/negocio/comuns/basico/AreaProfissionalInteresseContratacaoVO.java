/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.basico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;

/**
 *
 * @author Rogerio
 */
public class AreaProfissionalInteresseContratacaoVO extends SuperVO {

    private PessoaVO pessoa;
    private AreaProfissionalVO areaProfissional;
    private Integer codigo;

    public AreaProfissionalVO getAreaProfissional() {
        if (areaProfissional == null) {
            areaProfissional = new AreaProfissionalVO();
        }
        return areaProfissional;
    }

    public void setAreaProfissional(AreaProfissionalVO areaProfissional) {
        this.areaProfissional = areaProfissional;
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
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
