/*
 * OpcaoPerfilAcesso.java
 *
 * Created on 6 de Setembro de 2007, 16:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.comuns.utilitarias;

/**
 *
 * @author Administrador
 */

import java.io.Serializable;
import java.util.Hashtable;
@Deprecated
public class OpcaoPerfilAcesso implements Serializable{
    public static int TP_ENTIDADE = 1;
    public static int TP_FUNCIONALIDADE = 2;
    public static int TP_SUBMODULO = 3;
    
    private String nome;
    private String titulo;
    private int tipo;
    private Hashtable acoes;
    private Boolean funcionalidade;
    private String url;
    

	/** Creates a new instance of OpcaoPerfilAcesso */
    public OpcaoPerfilAcesso() {
        inicializar();
    }

    public OpcaoPerfilAcesso(String nomePrm, String tituloPrm, int tipoPrm,  Hashtable acoesPrm, String caminhoUrl) {
        nome = nomePrm;
        titulo = tituloPrm;
        tipo = tipoPrm;
        acoes = acoesPrm;
        url = caminhoUrl;        
    }    
 
    
    public OpcaoPerfilAcesso(String nomePrm, String tituloPrm, int tipoPrm){
    	nome = nomePrm;
        titulo = tituloPrm;
        tipo = tipoPrm;
    }
    
    private void inicializar() {
        setNome("");
        setTitulo("");
        setTipo(TP_ENTIDADE);
        setAcoes(new Hashtable());
        setUrl("");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Hashtable getAcoes() {
		if (acoes == null) {
			return new Hashtable();
		}
		return acoes;
	}

	public void setAcoes(Hashtable acoes) {
		this.acoes = acoes;
	}
	
    public Boolean getFuncionalidade() {
        if (funcionalidade == null) {
            return false;
        }
        return funcionalidade;
    }

    /**
     * @param funcionalidade the funcionalidade to set
     */
    public void setFuncionalidade(Boolean funcionalidade) {
        this.funcionalidade = funcionalidade;
    }	
	
    
}
