/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;

import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

public class PainelGestorFinanceiroAcademicoNivelEducacionalVO implements Serializable {

    private String nivel;
    private String curso;
    private Integer codigoCurso;
    private String turma;
    private Integer codigoTurma;
    private Integer alunoAtivo;
    private Integer alunoAptoFormar;
    private Integer alunoNovo;
    private Integer alunoRenovado;
    private Integer alunoRetornoEvasao;
    private Integer alunoCancelado;
    private Integer alunoTrancado;
    private Integer alunoAbandonado;
    private Integer alunoPreMatriculado;
    private Integer alunoFormado;
    private Integer alunoNaoRenovado;
    private Integer alunoTransferido;
    private Integer alunoTransferenciaInterna;
    private Double receita;
    private Double despesa;
    private Double mediaReceitaAluno;
    private Double mediaDespesaAluno;
    public static final long serialVersionUID = 1L;

    public String getNivelApresentar() {
        if (!getNivel().equals("")) {
            return TipoNivelEducacional.getEnum(getNivel()).getDescricao();
        }
        return "Outras Receitas/Despesas";
    }

    public boolean getIsOutrasReceitasDespesas() {
        if (getNivel().equals("")) {
            return true;
        }
        return false;
    }

    public Integer getAlunoAtivo() {
        if (alunoAtivo == null) {
            alunoAtivo = 0;
        }
        return alunoAtivo;
    }

    public void setAlunoAtivo(Integer alunoAtivo) {
        this.alunoAtivo = alunoAtivo;
    }

    public Integer getAlunoCancelado() {
        if (alunoCancelado == null) {
            alunoCancelado = 0;
        }
        return alunoCancelado;
    }

    public void setAlunoCancelado(Integer alunoCancelado) {
        this.alunoCancelado = alunoCancelado;
    }

    public Integer getAlunoNovo() {
        if (alunoNovo == null) {
            alunoNovo = 0;
        }
        return alunoNovo;
    }

    public void setAlunoNovo(Integer alunoNovo) {
        this.alunoNovo = alunoNovo;
    }

    public Integer getAlunoRenovado() {
        if (alunoRenovado == null) {
            alunoRenovado = 0;
        }
        return alunoRenovado;
    }

    public void setAlunoRenovado(Integer alunoRenovado) {
        this.alunoRenovado = alunoRenovado;
    }

    public Integer getAlunoTrancado() {
        if (alunoTrancado == null) {
            alunoTrancado = 0;
        }
        return alunoTrancado;
    }

    public void setAlunoTrancado(Integer alunoTrancado) {
        this.alunoTrancado = alunoTrancado;
    }

    public Integer getAlunoTransferido() {
        if (alunoTransferido == null) {
            alunoTransferido = 0;
        }
        return alunoTransferido;
    }

    public void setAlunoTransferido(Integer alunoTransferido) {
        this.alunoTransferido = alunoTransferido;
    }

    public Double getDespesa() {
        if (despesa == null) {
            despesa = 0.0;
        }
        return despesa;
    }

    public void setDespesa(Double despesa) {
        this.despesa = despesa;
    }

    public Double getMediaDespesaAluno() {
        if (mediaDespesaAluno == null) {
            mediaDespesaAluno = 0.0;
        }
        return mediaDespesaAluno;
    }

    public void setMediaDespesaAluno(Double mediaDespesaAluno) {
        this.mediaDespesaAluno = mediaDespesaAluno;
    }

    public Double getMediaReceitaAluno() {
        if (mediaReceitaAluno == null) {
            mediaReceitaAluno = 0.0;
        }
        return mediaReceitaAluno;
    }

    public void setMediaReceitaAluno(Double mediaReceitaAluno) {
        this.mediaReceitaAluno = mediaReceitaAluno;
    }

    public String getNivel() {
        if (nivel == null) {
            nivel = "";
        }
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Double getReceita() {
        if (receita == null) {
            receita = 0.0;
        }
        return receita;
    }

    public void setReceita(Double receita) {
        this.receita = receita;
    }
    
    public Integer getCodigoCurso() {
        if (codigoCurso == null) {
            codigoCurso = 0;
        }
        return codigoCurso;
    }

    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public Integer getCodigoTurma() {
        if (codigoTurma == null) {
            codigoTurma = 0;
        }
        return codigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }
    
    public String getCurso() {
        if (curso == null) {
            curso = "";
        }
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    
    public Integer getAlunoAptoFormar() {
        if(alunoAptoFormar == null){
            alunoAptoFormar = 0;
        }
        return alunoAptoFormar;
    }

    
    public void setAlunoAptoFormar(Integer alunoAptoFormar) {
        this.alunoAptoFormar = alunoAptoFormar;
    }

	public Integer getAlunoAbandonado() {
		if(alunoAbandonado == null){
			alunoAbandonado = 0;
		}
		return alunoAbandonado;
	}

	public void setAlunoAbandonado(Integer alunoAbandonado) {
		this.alunoAbandonado = alunoAbandonado;
	}

	public Integer getAlunoPreMatriculado() {
		if(alunoPreMatriculado == null){
			alunoPreMatriculado = 0;
		}
		return alunoPreMatriculado;
	}

	public void setAlunoPreMatriculado(Integer alunoPreMatriculado) {
		this.alunoPreMatriculado = alunoPreMatriculado;
	}

	public Integer getAlunoFormado() {
		if(alunoFormado == null){
			alunoFormado = 0;
		}
		return alunoFormado;
	}

	public void setAlunoFormado(Integer alunoFormado) {
		this.alunoFormado = alunoFormado;
	}

	public Integer getAlunoNaoRenovado() {
		if(alunoNaoRenovado == null){
			alunoNaoRenovado = 0;
		}
		return alunoNaoRenovado;
	}

	public void setAlunoNaoRenovado(Integer alunoNaoRenovado) {
		this.alunoNaoRenovado = alunoNaoRenovado;
	}
    
	 public Integer getTotalEvasao(){
	    	return getAlunoAbandonado()+getAlunoCancelado()+getAlunoTrancado()+getAlunoTransferido()+getAlunoTransferenciaInterna();
	    }

	public Integer getAlunoRetornoEvasao() {
		if(alunoRetornoEvasao == null){
			alunoRetornoEvasao = 0;
		}
		return alunoRetornoEvasao;
	}

	public void setAlunoRetornoEvasao(Integer alunoRetornoEvasao) {
		this.alunoRetornoEvasao = alunoRetornoEvasao;
	}
	
	public String detalheAlunoAtivo;
	public String getDetalheAlunoAtivo(){
		if(detalheAlunoAtivo == null){
			detalheAlunoAtivo = "";
		}
		return detalheAlunoAtivo;
	}

	public Integer getAlunoTransferenciaInterna() {
		if(alunoTransferenciaInterna == null){
			alunoTransferenciaInterna = 0;
		}
		return alunoTransferenciaInterna;
	}

	public void setAlunoTransferenciaInterna(Integer alunoTransferenciaInterna) {
		this.alunoTransferenciaInterna = alunoTransferenciaInterna;
	}
	
	
	 
	 
}
