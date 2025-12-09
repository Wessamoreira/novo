/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;

import java.util.Date;

/**
 *
 * @author Philippe
 */
public class UploadProfessorRelVO {

    private String nomeProfessor;
    private String nomeArquivo;
    private Date dataUpload;

    public String getNomeProfessor() {
        if (nomeProfessor == null) {
            nomeProfessor = "";
        }
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
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

    public Date getDataUpload() {
        if (dataUpload == null) {
            dataUpload = new Date();
        }
        return dataUpload;
    }

    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }

}
