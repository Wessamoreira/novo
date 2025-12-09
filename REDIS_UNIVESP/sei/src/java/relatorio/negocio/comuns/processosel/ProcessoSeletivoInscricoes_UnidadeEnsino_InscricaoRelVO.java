/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.processosel;

import java.util.Date;


/**
 *
 * @author Rodrigo Araújo
 */
public class ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO {

    public String cpf;
    public String nome;
    private String situacaoCR;
    private String situacao;
    private String situacaoInscricao;
    public Integer inscricao;
    public String sala;
    private Boolean inscricaoPresencial = Boolean.TRUE;
    private Date dataProva;

    public ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO() {
        inicializarDados();
    }

    public void inicializarDados() {
        cpf = "";
        nome = "";
        setInscricao(0);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getInscricao() {
        return inscricao;
    }

    public void setInscricao(Integer inscricao) {
        this.inscricao = inscricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the inscricaoPresencial
     */
    public Boolean getInscricaoPresencial() {
        if(inscricaoPresencial == null){
            inscricaoPresencial = Boolean.TRUE;
        }
        return inscricaoPresencial;
    }

    /**
     * @param inscricaoPresencial the inscricaoPresencial to set
     */
    public void setInscricaoPresencial(Boolean inscricaoPresencial) {
        this.inscricaoPresencial = inscricaoPresencial;
    }

    public String getInscricaoPresencial_Apresentar() {
        if (inscricaoPresencial) {
            return "Presencial";
        }
        if (!inscricaoPresencial) {
            return "Internet";
        }
        return "";
    }

    /**
     * @return the dataProva
     */
    public Date getDataProva() {
        return dataProva;
    }

    /**
     * @param dataProva the dataProva to set
     */
    public void setDataProva(Date dataProva) {
        this.dataProva = dataProva;
    }

    /**
     * @return the situacaoCR
     */
    public String getSituacaoCR() {
        if (situacaoCR == null) {
            situacaoCR = "";
        }
        return situacaoCR;
    }

    public String getSituacaoCR_Apresentar() {
        if (getSituacaoCR().equals("RE")) {
            return "RECEBIDA";
        } else if (getSituacaoCR().equals("AR")) {
            return "A RECEBER";
        } else {
            return "NÃO LOCAL./ISENTO";
        }
    }

    /**
     * @param situacaoCR the situacaoCR to set
     */
    public void setSituacaoCR(String situacaoCR) {
        this.situacaoCR = situacaoCR;
    }

	public String getSala() {
		if(sala == null){
			sala = "";
		}
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getSituacaoInscricao() {
		return situacaoInscricao;
	}

	public void setSituacaoInscricao(String situacaoInscricao) {
		this.situacaoInscricao = situacaoInscricao;
	}
 
	
    
}