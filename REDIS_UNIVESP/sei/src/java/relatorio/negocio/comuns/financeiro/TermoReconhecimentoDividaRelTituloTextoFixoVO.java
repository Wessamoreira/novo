/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.financeiro;

/**
 *
 * @author Philippe
 */
public class TermoReconhecimentoDividaRelTituloTextoFixoVO {
    private String titulo;
    private String texto;

    public String getTitulo() {
        if (titulo == null) {
            titulo = "";
        }
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        if (texto == null) {
            texto = "";
        }
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
