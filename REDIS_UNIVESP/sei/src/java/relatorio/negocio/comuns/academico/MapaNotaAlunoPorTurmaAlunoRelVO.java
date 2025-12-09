package relatorio.negocio.comuns.academico;

import java.io.Serializable;

public class MapaNotaAlunoPorTurmaAlunoRelVO implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4562614899678270567L;
	private Integer codigoDisciplina;
	private String nome;
	private String matricula;
	private String tituloNota;
	private String nota;
	private String conceito;
	private Double falta;
	private String nomeDisciplina;
	private Integer ordemLinha;
	private Integer ordemColuna;
	private Integer ordemColunaNota;

	private String turma;
	private String sala;
	private String unidadeensino;
	private String turno;
	private String curso;
	private String situacao;
	private String situacaoApresentar;
	private Double notaDouble;
	private Double mediaParcial;
	private Integer cargaHoraria;
	private String periodoLetivo;
	private String dataModulo;


	public MapaNotaAlunoPorTurmaAlunoRelVO clone()  throws CloneNotSupportedException {
		return (MapaNotaAlunoPorTurmaAlunoRelVO)super.clone();
	}
	
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNota() {
		if (nota == null) {
			nota = "";
		}
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}

	public Integer getCodigoDisciplina() {
		if (codigoDisciplina == null) {
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setFalta(Double falta) {
		this.falta = falta;
	}

	public Double getFalta() {
		if (falta == null) {
			falta = 0.0;
		}
		return falta;
	}

	public String getConceito() {
		if (conceito == null) {
			conceito = "";
		}
		return conceito;
	}

	public void setConceito(String conceito) {
		this.conceito = conceito;
	}

	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public Integer getOrdemLinha() {
		if (ordemLinha == null) {
			ordemLinha = 0;
		}
		return ordemLinha;
	}

	public void setOrdemLinha(Integer ordemLinha) {
		this.ordemLinha = ordemLinha;
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

	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getUnidadeensino() {
		if (unidadeensino == null) {
			unidadeensino = "";
		}
		return unidadeensino;
	}

	public void setUnidadeensino(String unidadeensino) {
		this.unidadeensino = unidadeensino;
	}

	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
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

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getSituacaoApresentar() {
		if (situacaoApresentar == null) {
			situacaoApresentar = "";
		}
		return situacaoApresentar;
	}

	public void setSituacaoApresentar(String situacaoApresentar) {
		this.situacaoApresentar = situacaoApresentar;
	}

	public Double getNotaDouble() {
		if (notaDouble == null) {
			notaDouble = 0.0;
		}
		return notaDouble;
	}

	public void setNotaDouble(Double notaDouble) {
		this.notaDouble = notaDouble;
	} 

	public Double getMediaParcial() {
		return mediaParcial;
	}

	public void setMediaParcial(Double mediaParcial) {
		this.mediaParcial = mediaParcial;
	}

	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	public String getDataModulo() {
		if(dataModulo == null) {
			dataModulo = "";
		}
		return dataModulo;
	}

	public void setDataModulo(String dataModulo) {
		this.dataModulo = dataModulo;
	}


	public Integer getOrdemColuna() {
		if(ordemColuna ==null) {
			ordemColuna = 0;
		}
		return ordemColuna;
	}

	public void setOrdemColuna(Integer ordemColuna) {
		this.ordemColuna = ordemColuna;
	}

	public String getTituloNota() {
		if(tituloNota ==null) {
			tituloNota = "";
		}
		return tituloNota;
	}

	public void setTituloNota(String tituloNota) {
		this.tituloNota = tituloNota;
	}

	public Integer getOrdemColunaNota() {
		if(ordemColunaNota == null) {
			ordemColunaNota = 0;
		}
		return ordemColunaNota;
	}

	public void setOrdemColunaNota(Integer ordemColunaNota) {
		this.ordemColunaNota = ordemColunaNota;
	}

	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}
	
	public String getAgrupadorDisciplinaNota() {
		return getCodigoDisciplina()+getTituloNota()+getOrdemColunaNota();
	}
}