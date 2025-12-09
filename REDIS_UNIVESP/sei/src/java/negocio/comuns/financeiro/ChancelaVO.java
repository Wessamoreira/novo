package negocio.comuns.financeiro;

import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.basico.SuperEmpresaVO;

public class ChancelaVO extends SuperEmpresaVO implements PossuiEndereco {

    private transient Integer codigo;
    private String instituicaoChanceladora;
    public static final long serialVersionUID = 1L;

    public ChancelaVO() {
        super();
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

    public void setInstituicaoChanceladora(String instituicaoChanceladora) {
        this.instituicaoChanceladora = instituicaoChanceladora;
    }

    public String getInstituicaoChanceladora() {
        if (instituicaoChanceladora == null) {
            instituicaoChanceladora = "";
        }
        return instituicaoChanceladora;
    }
}
