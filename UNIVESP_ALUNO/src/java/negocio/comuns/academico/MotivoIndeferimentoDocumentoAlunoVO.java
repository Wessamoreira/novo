package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;

public class MotivoIndeferimentoDocumentoAlunoVO extends SuperVO {

    private static final long serialVersionUID = -115616451917105821L;
    private Integer codigo;
    private String nome;
    private StatusAtivoInativoEnum situacao;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusAtivoInativoEnum getSituacao() {
        if (situacao == null) {
            situacao = StatusAtivoInativoEnum.ATIVO;
        }
        return situacao;
    }

    public void setSituacao(StatusAtivoInativoEnum situacao) {
        this.situacao = situacao;
    }
}