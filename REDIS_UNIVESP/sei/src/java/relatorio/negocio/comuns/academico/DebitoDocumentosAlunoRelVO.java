/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

import java.util.Date;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;

/**
 *
 * @author OTIMIZE09-04
 */
public class DebitoDocumentosAlunoRelVO {

    private MatriculaVO matriculaVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private TipoDocumentoVO tipoDocumentoVO;
    private CursoVO cursoVO;
    private TurmaVO turmaVO;
    private Date dataEntrega;
    private String nomeUsuario;
    private String tipoDocumento;
    private FuncionarioVO funcionarioVO;
    private String situacaoDocumento;
    private Date dataSituacaoDocumento;
    private String justificativaNegacao;
    

    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public String getNomeUsuario() {
        if (nomeUsuario == null) {
            nomeUsuario = "";
        }
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public String getTipoDocumento() {
        if (tipoDocumento == null) {
            tipoDocumento = "";
        }
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the unidadeEnsinoVO
     */
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    /**
     * @param unidadeEnsinoVO the unidadeEnsinoVO to set
     */
    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    /**
     * @return the tipoDocumentoVO
     */
    public TipoDocumentoVO getTipoDocumentoVO() {
        if (tipoDocumentoVO == null) {
            tipoDocumentoVO = new TipoDocumentoVO();
        }
        return tipoDocumentoVO;
    }

    /**
     * @param tipoDocumentoVO the tipoDocumentoVO to set
     */
    public void setTipoDocumentoVO(TipoDocumentoVO tipoDocumentoVO) {
        this.tipoDocumentoVO = tipoDocumentoVO;
    }

    /**
     * @return the funcionarioVO
     */
    public FuncionarioVO getFuncionarioVO() {
        if (funcionarioVO == null) {
            funcionarioVO = new FuncionarioVO();
        }
        return funcionarioVO;
    }

    /**
     * @param funcionarioVO the funcionarioVO to set
     */
    public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }

	public String getSituacaoDocumento() {
		return situacaoDocumento;
	}

	public void setSituacaoDocumento(String situacaoDocumento) {
		this.situacaoDocumento = situacaoDocumento;
	}
	
	public Date getDataSituacaoDocumento() {
		return dataSituacaoDocumento;
	}

	public void setDataSituacaoDocumento(Date dataSituacaoDocumento) {
		this.dataSituacaoDocumento = dataSituacaoDocumento;
	}

	public String getJustificativaNegacao() {
		return justificativaNegacao;
	}

	public void setJustificativaNegacao(String justificativaNegacao) {
		this.justificativaNegacao = justificativaNegacao;
	}   	
    
}
