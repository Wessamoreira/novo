package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public class ArquivoLogVO extends SuperVO {

    private Integer codigo;
    private UsuarioVO responsavelUpload;
    private ArquivoVO arquivo;
    private String nomeArquivo;
    private String origem;
    private Date dataUpload;
    private Boolean arquivoGravadoDisco;
    public static final long serialVersionUID = 1L;

    public Date getDataUpload() {
        if (dataUpload == null) {
            dataUpload = new Date();
        }
        return dataUpload;
    }

    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        if (codigo == null) {
            codigo = 0;
        }
        this.codigo = codigo;
    }

    public UsuarioVO getResponsavelUpload() {
        if (responsavelUpload == null) {
            responsavelUpload = new UsuarioVO();
        }
        return responsavelUpload;
    }

    public void setResponsavelUpload(UsuarioVO responsavelUpload) {
        this.responsavelUpload = responsavelUpload;
    }

    public ArquivoVO getArquivo() {
        if (arquivo == null) {
            arquivo = new ArquivoVO();
        }
        return arquivo;
    }

    public void setArquivo(ArquivoVO arquivo) {
        this.arquivo = arquivo;
    }

    public String getNomeArquivo() {
        if (nomeArquivo == null) {
            nomeArquivo = "";
        }
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Boolean getArquivoGravadoDisco() {
        if (arquivoGravadoDisco == null) {
            arquivoGravadoDisco = Boolean.FALSE;
        }
        return arquivoGravadoDisco;
    }

    public void setArquivoGravadoDisco(Boolean arquivoGravadoDisco) {
        this.arquivoGravadoDisco = arquivoGravadoDisco;
    }

    public String getOrigem() {
        if (origem == null) {
            origem = "";
        }
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
