/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

/**
 *
 * @author Carlos
 */
public class LogExclusaoArquivoVO extends SuperVO {

    private Integer codigo;
    private Integer codigoArquivo;
    private String nomeArquivo;
    private String descricaoArquivo;
    private String origem;
    private String tipoVisao;
    private UsuarioVO responsavelExclusao;
    private DisciplinaVO disciplinaVO;
    private TurmaVO turmaVO;
    private PessoaVO professor;
    private Date dataUpload;
    private Date dataExclusao;
    public static final long serialVersionUID = 1L;

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the codigoArquivo
     */
    public Integer getCodigoArquivo() {
        if (codigoArquivo == null) {
            codigoArquivo = 0;
        }
        return codigoArquivo;
    }

    /**
     * @param codigoArquivo the codigoArquivo to set
     */
    public void setCodigoArquivo(Integer codigoArquivo) {
        this.codigoArquivo = codigoArquivo;
    }

    /**
     * @return the nomeArquivo
     */
    public String getNomeArquivo() {
        if (nomeArquivo == null) {
            nomeArquivo = "";
        }
        return nomeArquivo;
    }

    /**
     * @param nomeArquivo the nomeArquivo to set
     */
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    /**
     * @return the descricaoArquivo
     */
    public String getDescricaoArquivo() {
        if (descricaoArquivo == null) {
            descricaoArquivo = "";
        }
        return descricaoArquivo;
    }

    /**
     * @param descricaoArquivo the descricaoArquivo to set
     */
    public void setDescricaoArquivo(String descricaoArquivo) {
        this.descricaoArquivo = descricaoArquivo;
    }

    /**
     * @return the origem
     */
    public String getOrigem() {
        if (origem == null) {
            origem = "";
        }
        return origem;
    }

    /**
     * @param origem the origem to set
     */
    public void setOrigem(String origem) {
        this.origem = origem;
    }

    /**
     * @return the responsavelExclusao
     */
    public UsuarioVO getResponsavelExclusao() {
        if (responsavelExclusao == null) {
            responsavelExclusao = new UsuarioVO();
        }
        return responsavelExclusao;
    }

    /**
     * @param responsavelExclusao the responsavelExclusao to set
     */
    public void setResponsavelExclusao(UsuarioVO responsavelExclusao) {
        this.responsavelExclusao = responsavelExclusao;
    }

    /**
     * @return the disciplinaVO
     */
    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    /**
     * @param disciplinaVO the disciplinaVO to set
     */
    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    /**
     * @return the turmaVO
     */
    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    /**
     * @param turmaVO the turmaVO to set
     */
    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    /**
     * @return the professor
     */
    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return professor;
    }

    /**
     * @param professor the professor to set
     */
    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    /**
     * @return the dataUpload
     */
    public Date getDataUpload() {
        return dataUpload;
    }

    /**
     * @param dataUpload the dataUpload to set
     */
    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }

    /**
     * @return the dataExclusao
     */
    public Date getDataExclusao() {
        if (dataExclusao == null) {
            dataExclusao = new Date();
        }
        return dataExclusao;
    }

    /**
     * @param dataExclusao the dataExclusao to set
     */
    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    /**
     * @return the tipoVisao
     */
    public String getTipoVisao() {
        if (tipoVisao == null) {
            tipoVisao = "";
        }
        return tipoVisao;
    }

    /**
     * @param tipoVisao the tipoVisao to set
     */
    public void setTipoVisao(String tipoVisao) {
        this.tipoVisao = tipoVisao;
    }
}
