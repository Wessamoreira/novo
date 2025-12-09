/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.bancocurriculum;

/**
 *
 * @author Philippe
 */
public class ContatoEmpresaBancoTalentoRelVO {

    private Integer codigoContato;
    private String nomeContato;
    private String emailContato;
    private String telefoneContato;

    public String getNomeContato() {
        if (nomeContato == null) {
            nomeContato = "";
        }
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getEmailContato() {
        if (emailContato == null) {
            emailContato = "";
        }
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getTelefoneContato() {
        if (telefoneContato == null) {
            telefoneContato = "";
        }
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public Integer getCodigoContato() {
        if (codigoContato == null) {
            codigoContato = 0;
        }
        return codigoContato;
    }

    public void setCodigoContato(Integer codigoContato) {
        this.codigoContato = codigoContato;
    }

}
