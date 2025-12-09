package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Historico. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ExclusaoMatriculaVO extends SuperVO implements Cloneable {

    private Integer codigo;
    private MatriculaVO matriculaVO;
    private Date dataExclusao;
    private UsuarioVO responsavelExclusao;
    private String motivoExclusao;
    public static final long serialVersionUID = 1L;

    public ExclusaoMatriculaVO() {
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

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return (matriculaVO);
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public Date getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getDataExclusao_Apresentar() {
        return Uteis.getData(getDataExclusao());
    }

    public String getMotivoExclusao() {
        if (motivoExclusao == null) {
            motivoExclusao = "";
        }
        return motivoExclusao;
    }

    public void setMotivoExclusao(String motivoExclusao) {
        this.motivoExclusao = motivoExclusao;
    }

    public UsuarioVO getResponsavelExclusao() {
        if (responsavelExclusao == null) {
            responsavelExclusao = new UsuarioVO();
        }
        return responsavelExclusao;
    }

    public void setResponsavelExclusao(UsuarioVO responsavelExclusao) {
        this.responsavelExclusao = responsavelExclusao;
    }
}
