/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author edigarjr
 */
public class ControleAtividadeUsuarioVO implements Serializable {
    private String username;
    private String entidade;
    private Date momento;
    /**
     * Incluindo
     * Alterando
     * Excluindo
     * Consultando
     * Uploading
     * Downloading
     * Emitindo Relatório
     */
    private String tipo;
    private String descricao;
    private Integer nrOperacoes;
    
    
    public ControleAtividadeUsuarioVO() {}

    public ControleAtividadeUsuarioVO(ControleAtividadeUsuarioVO original) {
        this.entidade = original.getEntidade();
        this.descricao = original.getDescricao();
        this.tipo = original.getTipo();
        this.username = original.getUsername();
        this.momento = original.getMomento(); 
        this.nrOperacoes = original.getNrOperacoes();
    }

    /**
     * @return the entidade
     */
    public String getEntidade() {
        if (entidade == null) {
            entidade = "";
        }
        return entidade;
    }

    /**
     * @param entidade the entidade to set
     */
    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    /**
     * @return the momento
     */
    public Date getMomento() {
        if (momento == null) {
            momento = new Date();
        }
        return momento;
    }

    public String getMomento_Apresentar() {
        return Uteis.getData(momento);
    }

    public String getHoraMomento_Apresentar() {
        return Uteis.getHoraMinutoComMascara(momento);
    }

    /**
     * @param momento the momento to set
     */
    public void setMomento(Date momento) {
        this.momento = momento;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        if (tipo == null) {
            tipo = "";
        }
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        if (username == null) {
            username = "";
        }
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the nrOperacoes
     */
    public Integer getNrOperacoes() {
        if (nrOperacoes == null) {
            nrOperacoes = 0;
        }
        return nrOperacoes;
    }

    /**
     * @param nrOperacoes the nrOperacoes to set
     */
    public void setNrOperacoes(Integer nrOperacoes) {
        this.nrOperacoes = nrOperacoes;
    }
}
