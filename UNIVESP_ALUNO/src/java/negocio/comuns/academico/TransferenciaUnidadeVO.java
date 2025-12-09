package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class TransferenciaUnidadeVO extends SuperVO {

    private Integer codigo;
    private MatriculaVO matriculaVoOrigem;
    private MatriculaVO matriculaVoDestino;
    private UsuarioVO responsavel;
    private Date data;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getData_Apresentar() {
        return (Uteis.getDataAno4Digitos(getData()));
    }

    public MatriculaVO getMatriculaVoDestino() {
        if (matriculaVoDestino == null) {
            matriculaVoDestino = new MatriculaVO();
        }
        return matriculaVoDestino;
    }

    public void setMatriculaVoDestino(MatriculaVO matriculaVoDestino) {
        this.matriculaVoDestino = matriculaVoDestino;
    }

    public MatriculaVO getMatriculaVoOrigem() {
        if (matriculaVoOrigem == null) {
            matriculaVoOrigem = new MatriculaVO();
        }
        return matriculaVoOrigem;
    }

    public void setMatriculaVoOrigem(MatriculaVO matriculaVoOrigem) {
        this.matriculaVoOrigem = matriculaVoOrigem;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }
}
