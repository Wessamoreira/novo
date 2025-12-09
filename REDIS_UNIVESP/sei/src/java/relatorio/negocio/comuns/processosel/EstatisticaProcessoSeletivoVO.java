package relatorio.negocio.comuns.processosel;

import java.util.Date;

public class EstatisticaProcessoSeletivoVO {
    
    private Integer quantidade;
    private String descricao;
    private String descricaoComplementar;
    /*
     * Usados em LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA
     */
    private String curso;
    private String turno;
    private Integer quantidadeAusente;
    private Integer quantidadePresente;
    private Date dataProva;
    
    /*
     * Utilizado em LISTAGEM_MURAL_CANDIDATO
     */
	private String sala;
	private String numeroInscricao;
	private String nomeCandidato;
	
    public Integer getQuantidade() {
        if(quantidade == null){
            quantidade = 0;
        }
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getDescricao() {
        if(descricao == null){
            descricao = "";
        }
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    public String getDescricaoComplementar() {
        if(descricaoComplementar == null){
            descricaoComplementar = "";
        }
        return descricaoComplementar;
    }

    
    public void setDescricaoComplementar(String descricaoComplementar) {
        this.descricaoComplementar = descricaoComplementar;
    }

	public Integer getQuantidadeAusente() {
		return quantidadeAusente;
	}

	public void setQuantidadeAusente(Integer quantidadeAusente) {
		this.quantidadeAusente = quantidadeAusente;
	}

	public Integer getQuantidadePresente() {
		return quantidadePresente;
	}

	public void setQuantidadePresente(Integer quantidadePresente) {
		this.quantidadePresente = quantidadePresente;
	}

	public Date getDataProva() {
		if (dataProva == null) {
			dataProva = new Date();
		}
		return dataProva;
	}

	public void setDataProva(Date dataProva) {
		this.dataProva = dataProva;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getNumeroInscricao() {
		if (numeroInscricao == null) {
			numeroInscricao = "";
		}
		return numeroInscricao;
	}

	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public String getNomeCandidato() {
		if (nomeCandidato == null) {
			nomeCandidato = "";
		}
		return nomeCandidato;
	}

	public void setNomeCandidato(String nomeCandidato) {
		this.nomeCandidato = nomeCandidato;
	}

}
