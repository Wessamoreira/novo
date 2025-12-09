/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Otimize-TI
 */
public class GradeCurricularAlunoDisciplinaRelVO {

    private Integer periodoLetivo;        
    private String semestre;
    private String ano;
    private String situacao;
    private Double media;  
    private Integer disciplina;
    private String disciplinaNome;
    private String disciplinaAbreviatura;
    private Integer disciplinaCargaHoraria;
    private Integer disciplinaCredito;
    private Double frequencia;
    private String periodoLetivoDescricao;
    private Boolean disciplinaFazParteComposicao;
    private Boolean disciplinaComposta;
    private Boolean disciplinaGrupoOptativa;
    private String disciplinaPrincipalNome;
    private Integer disciplinaPrincipal;
    private Integer cargaHorariaTotalPeriodo;    
    private Integer cargaHorariaCumpridaTotalPeriodo;
    private Boolean optativa;
    private Boolean historicoPorEquivalencia;
    private Boolean apresentarAprovadoHistorico;
    private List<GradeCurricularAlunoDisciplinaRelVO> disciplinaComposicaoVOs;
    

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getDisciplinaCargaHoraria() {
        if (disciplinaCargaHoraria == null) {
            disciplinaCargaHoraria = 0;
        }
        return disciplinaCargaHoraria;
    }

    public void setDisciplinaCargaHoraria(Integer disciplinaCargaHoraria) {
        this.disciplinaCargaHoraria = disciplinaCargaHoraria;
    }

    public String getDisciplinaNome() {
        if (disciplinaNome == null) {
            disciplinaNome = "";
        }
        return disciplinaNome;
    }

    public void setDisciplinaNome(String disciplinaNome) {
        this.disciplinaNome = disciplinaNome;
    }    
    
    public String getDisciplinaAbreviatura() {
    	if (disciplinaAbreviatura == null) {
    		disciplinaAbreviatura = "";
        }
        return disciplinaAbreviatura;
    }

    public void setDisciplinaAbreviatura(String disciplinaAbreviatura) {
        this.disciplinaAbreviatura = disciplinaAbreviatura;
    }    
    
    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public Integer getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void setPeriodoLetivo(Integer periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	public Double getFrequencia() {
		if (frequencia == null) {
			frequencia = 0.0;
		}
		return frequencia;
	}
	
	public Integer getOrdenacaoSituacao() {
    	if (getSituacao().equals("A CURSAR")) {
    		return 3;
    	} else if (getSituacao().equals("CURSADA")) {
    		return 1;
    	} else {
    		return 2;
    	}
    }

	public String getPeriodoLetivoDescricao() {
		if (periodoLetivoDescricao == null) {
			periodoLetivoDescricao = "";
		}
		return periodoLetivoDescricao;
	}

	public void setPeriodoLetivoDescricao(String periodoLetivoDescricao) {
		this.periodoLetivoDescricao = periodoLetivoDescricao;
	}
	
	public String getOrdenacaoAnoSemestre() {
		return getAno() + "/" + getSemestre();
	}
	

	/**
	 * @return the disciplinaCredito
	 */
	public Integer getDisciplinaCredito() {		
		return disciplinaCredito;
	}

	/**
	 * @param disciplinaCredito the disciplinaCredito to set
	 */
	public void setDisciplinaCredito(Integer disciplinaCredito) {
		this.disciplinaCredito = disciplinaCredito;
	}

	/**
	 * @return the disciplinaFazParteComposicao
	 */
	public Boolean getDisciplinaFazParteComposicao() {
		if (disciplinaFazParteComposicao == null) {
			disciplinaFazParteComposicao = false;
		}
		return disciplinaFazParteComposicao;
	}

	/**
	 * @param disciplinaFazParteComposicao the disciplinaFazParteComposicao to set
	 */
	public void setDisciplinaFazParteComposicao(Boolean disciplinaFazParteComposicao) {
		this.disciplinaFazParteComposicao = disciplinaFazParteComposicao;
	}

	/**
	 * @return the disciplinaGrupoOptativa
	 */
	public Boolean getDisciplinaGrupoOptativa() {
		if (disciplinaGrupoOptativa == null) {
			disciplinaGrupoOptativa = false;
		}
		return disciplinaGrupoOptativa;
	}

	/**
	 * @param disciplinaGrupoOptativa the disciplinaGrupoOptativa to set
	 */
	public void setDisciplinaGrupoOptativa(Boolean disciplinaGrupoOptativa) {
		this.disciplinaGrupoOptativa = disciplinaGrupoOptativa;
	}

	/**
	 * @return the disciplinaPrincipal
	 */
	public Integer getDisciplinaPrincipal() {
		if (disciplinaPrincipal == null) {
			disciplinaPrincipal = 0;
		}
		return disciplinaPrincipal;
	}

	/**
	 * @param disciplinaPrincipal the disciplinaPrincipal to set
	 */
	public void setDisciplinaPrincipal(Integer disciplinaPrincipal) {
		this.disciplinaPrincipal = disciplinaPrincipal;
	}

	/**
	 * @return the cargaHorariaTotalPeriodo
	 */
	public Integer getCargaHorariaTotalPeriodo() {
		if (cargaHorariaTotalPeriodo == null) {
			cargaHorariaTotalPeriodo = 0;
		}
		return cargaHorariaTotalPeriodo;
	}

	/**
	 * @param cargaHorariaTotalPeriodo the cargaHorariaTotalPeriodo to set
	 */
	public void setCargaHorariaTotalPeriodo(Integer cargaHorariaTotalPeriodo) {
		this.cargaHorariaTotalPeriodo = cargaHorariaTotalPeriodo;
	}

	/**
	 * @return the cargaHorariaCumpridaTotalPeriodo
	 */
	public Integer getCargaHorariaCumpridaTotalPeriodo() {
		if (cargaHorariaCumpridaTotalPeriodo == null) {
			cargaHorariaCumpridaTotalPeriodo = 0;
		}
		return cargaHorariaCumpridaTotalPeriodo;
	}

	/**
	 * @param cargaHorariaCumpridaTotalPeriodo the cargaHorariaCumpridaTotalPeriodo to set
	 */
	public void setCargaHorariaCumpridaTotalPeriodo(Integer cargaHorariaCumpridaTotalPeriodo) {
		this.cargaHorariaCumpridaTotalPeriodo = cargaHorariaCumpridaTotalPeriodo;
	}
	
	public String getOrdenacaoLayout2(){
		return (getDisciplinaGrupoOptativa()?1000:getPeriodoLetivo())+getPeriodoLetivoDescricao()+(getDisciplinaFazParteComposicao()?getDisciplinaPrincipal()+getDisciplinaNome():getDisciplinaNome());
	}

	/**
	 * @return the optativa
	 */
	public Boolean getOptativa() {
		if (optativa == null) {
			optativa = false;
		}
		return optativa;
	}

	/**
	 * @param optativa the optativa to set
	 */
	public void setOptativa(Boolean optativa) {
		this.optativa = optativa;
	}

	/**
	 * @return the disciplina
	 */
	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	/**
	 * @param disciplina the disciplina to set
	 */
	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	/**
	 * @return the disciplinaComposicaoVOs
	 */
	public List<GradeCurricularAlunoDisciplinaRelVO> getDisciplinaComposicaoVOs() {
		if (disciplinaComposicaoVOs == null) {
			disciplinaComposicaoVOs = new ArrayList<GradeCurricularAlunoDisciplinaRelVO>(0);
		}
		return disciplinaComposicaoVOs;
	}

	/**
	 * @param disciplinaComposicaoVOs the disciplinaComposicaoVOs to set
	 */
	public void setDisciplinaComposicaoVOs(List<GradeCurricularAlunoDisciplinaRelVO> disciplinaComposicaoVOs) {
		this.disciplinaComposicaoVOs = disciplinaComposicaoVOs;
	}

	/**
	 * @return the disciplinaComposta
	 */
	public Boolean getDisciplinaComposta() {
		if (disciplinaComposta == null) {
			disciplinaComposta = false;
		}
		return disciplinaComposta;
	}

	/**
	 * @param disciplinaComposta the disciplinaComposta to set
	 */
	public void setDisciplinaComposta(Boolean disciplinaComposta) {
		this.disciplinaComposta = disciplinaComposta;
	}

	/**
	 * @return the disciplinaPrincipalNome
	 */
	public String getDisciplinaPrincipalNome() {
		if (disciplinaPrincipalNome == null) {
			disciplinaPrincipalNome = "";
		}
		return disciplinaPrincipalNome;
	}

	/**
	 * @param disciplinaPrincipalNome the disciplinaPrincipalNome to set
	 */
	public void setDisciplinaPrincipalNome(String disciplinaPrincipalNome) {
		this.disciplinaPrincipalNome = disciplinaPrincipalNome;
	}

	public Boolean getHistoricoPorEquivalencia() {
		if(historicoPorEquivalencia == null){
			historicoPorEquivalencia= false;
		}
		return historicoPorEquivalencia;
	}

	public void setHistoricoPorEquivalencia(Boolean historicoPorEquivalencia) {
		this.historicoPorEquivalencia = historicoPorEquivalencia;
	}

	public Boolean getIsAprovado(){
		return getSituacao().equals("Aprovado") || getSituacao().equals("Aprovado por Equivalência") || getSituacao().equals("Aprov. por Equiv.") || getSituacao().equals("Aprov. por Aprov.");
	}
	
	public Boolean getIsCursando(){
		return getSituacao().equals("Cursando") || getSituacao().equals("Cursando por Equivalência") || getSituacao().equals("Curs. por Equiv.");
	}

	public Boolean getApresentarAprovadoHistorico() {
		if(apresentarAprovadoHistorico == null){
			apresentarAprovadoHistorico = false;
		}
		return apresentarAprovadoHistorico;
	}

	public void setApresentarAprovadoHistorico(Boolean apresentarAprovadoHistorico) {
		this.apresentarAprovadoHistorico = apresentarAprovadoHistorico;
	}
	
	
}