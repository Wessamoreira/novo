/*
 * Comando.java
 *
 * Created on 21 de Abril de 2006, 18:19
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package negocio.comuns.utilitarias;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Administrador
 */
public class Comando implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2065260904508472745L;
	protected final char ENTER = '\n';
    //protected final char TAB = '\t';
    protected final String TAB = "    ";
    protected final char ASPAS = '"';
    protected final String ASPASDUPLOAS = ASPAS + "" + ASPAS;
    
    private Vector linhas;
    
    /** Creates a new instance of Comando */
    public Comando() {
        setLinhas(new Vector());
    }
    
    public void adComentario(String comentario, int identacao) {
        if (!comentario.equals("")) {
            comentario = "/** " + comentario + " */";
            adCmd(comentario, identacao);
        }
    }
    
    public void adCmd(String linha, int identacao) {
        adicionarLinhaComando(linha, identacao);
    }
    
    public void adCmdInicio(String linha, int identacao) {
        for (int i = 1; i <= identacao; i++) {
            linha = TAB + "" + linha;
        }
        linha = ENTER + linha;
        getLinhas().add(0, linha);        
    }    
    
    public boolean cmdExistente(String linha) {
        Enumeration e = getLinhas().elements();
        while (e.hasMoreElements()) {
            String comando = (String)e.nextElement();
            if (comando.indexOf(linha) != -1) {
                return true;
            }
        }
        return false;
    }
    
    public void adicionarLinhaComando(String linha, int identacao) {
        for (int i = 1; i <= identacao; i++) {
            linha = TAB + "" + linha;
        }
        getLinhas().add(linha);
    }
    
    public int getNrLinhas() {
        return getLinhas().size();
    }

    public Vector getLinhas() {
        return linhas;
    }

    public void setLinhas(Vector linhas) {
        this.linhas = linhas;
    }
    
}
