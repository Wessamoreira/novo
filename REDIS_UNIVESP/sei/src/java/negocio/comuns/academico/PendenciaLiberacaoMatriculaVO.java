package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.MotivoSolicitacaoLiberacaoMatriculaEnum;
import negocio.comuns.academico.enumeradores.SituacaoPendenciaLiberacaoMatriculaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Reponsável por manter os dados da entidade PendenciaLiberacaoMatriculaVO. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PendenciaLiberacaoMatriculaVO extends SuperVO {

    private Integer codigo;
    private MatriculaVO matricula;
    private Date dataSolicitacao;   
    private UsuarioVO usuarioSolicitacao;
    private MotivoSolicitacaoLiberacaoMatriculaEnum motivoSolicitacao;
    private SituacaoPendenciaLiberacaoMatriculaEnum situacao;
    private String motivoIndeferimento;
    private Date dataIndeferimento;
    private UsuarioVO usuarioIndeferimento;
    private Date dataDeferimento;
    private UsuarioVO usuarioDeferimento;
	
    
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

	public MatriculaVO getMatricula() {
		if(matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public UsuarioVO getUsuarioSolicitacao() {
		if(usuarioSolicitacao == null) {
			usuarioSolicitacao = new UsuarioVO();
		}
		return usuarioSolicitacao;
	}

	public void setUsuarioSolicitacao(UsuarioVO usuarioSolicitacao) {
		this.usuarioSolicitacao = usuarioSolicitacao;
	}

	public MotivoSolicitacaoLiberacaoMatriculaEnum getMotivoSolicitacao() {
		return motivoSolicitacao;
	}

	public void setMotivoSolicitacao(MotivoSolicitacaoLiberacaoMatriculaEnum motivoSolicitacao) {
		this.motivoSolicitacao = motivoSolicitacao;
	}

	public SituacaoPendenciaLiberacaoMatriculaEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPendenciaLiberacaoMatriculaEnum situacao) {
		this.situacao = situacao;
	}

	public String getMotivoIndeferimento() {
		return motivoIndeferimento;
	}

	public void setMotivoIndeferimento(String motivoIndeferimento) {
		this.motivoIndeferimento = motivoIndeferimento;
	}

	public Date getDataIndeferimento() {
		return dataIndeferimento;
	}

	public void setDataIndeferimento(Date dataIndeferimento) {
		this.dataIndeferimento = dataIndeferimento;
	}

	public UsuarioVO getUsuarioIndeferimento() {
		if(usuarioIndeferimento == null) {
			usuarioIndeferimento = new UsuarioVO();
		}
		return usuarioIndeferimento;
	}

	public void setUsuarioIndeferimento(UsuarioVO usuarioIndeferimento) {
		this.usuarioIndeferimento = usuarioIndeferimento;
	}

	public Date getDataDeferimento() {
		return dataDeferimento;
	}

	public void setDataDeferimento(Date dataDeferimento) {
		this.dataDeferimento = dataDeferimento;
	}

	public UsuarioVO getUsuarioDeferimento() {
		if(usuarioDeferimento == null) {
			usuarioDeferimento = new UsuarioVO();
		}
		return usuarioDeferimento;
	}

	public void setUsuarioDeferimento(UsuarioVO usuarioDeferimento) {
		this.usuarioDeferimento = usuarioDeferimento;
	}
        
}
