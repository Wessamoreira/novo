package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

public class FuncionarioGrupoDestinatariosVO extends SuperVO {

    private FuncionarioVO funcionario;
    private Integer grupoDestinatarios;
    public static final long serialVersionUID = 1L;

    public FuncionarioGrupoDestinatariosVO() {
        super();
    }

    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new FuncionarioVO();
        }
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }

    public void setGrupoDestinatarios(Integer grupoDestinatarios) {
        this.grupoDestinatarios = grupoDestinatarios;
    }

    public Integer getGrupoDestinatarios() {
        if (grupoDestinatarios == null) {
            grupoDestinatarios = 0;
        }
        return grupoDestinatarios;
    }
}
