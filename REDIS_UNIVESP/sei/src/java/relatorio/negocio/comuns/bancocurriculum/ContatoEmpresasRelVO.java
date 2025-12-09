/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.bancocurriculum;

/**
 *
 * @author Philippe
 */
public class ContatoEmpresasRelVO {
    private String nomeContato;
    private String email;
    private String telefone;

    public String getNomeContato() {
        if (nomeContato == null) {
            nomeContato = "";
        }
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        if (telefone == null) {
            telefone = "";
        }
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
