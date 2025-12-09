package relatorio.negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.utilitarias.Uteis;

public class CronogramaDeAulasRelVO {

	private Date dataInicio;
	private Date dataFim;
	private Date data;
	private String disciplina;
	private String unidadeEnsino;
	private String professor;
	private String telefoneProfessor;
	private String celularProfessor;
	private String emailProfessor;
	private String titulacaoProfessor;
	private String dataModulo;
	private String turma;
	private String cidade;
	private String dataUnidadeOrdenacao;
	private Integer qtdeAlunoAtivo;
	private Integer qtdeMaterialPostado;
	private Integer qtdeAlunoPreMatricula;
	private Integer qtdeMediaCalculada;
	private String modulo;
	private String local;
	private String sala;
	private String horarioInicio;
	private String horarioTermino;
	private String curso;
	private String periodo;
	private String ano;
	private String semestre;
	private String gradeCurricular;
	private String diaSemana;
	private Integer numeroAula;
	private Integer numeroVaga;
	private String matriculaProfessor;
	private String turno;
	private Integer cargaHorariaDisciplina;
	private Integer horaAulaDisciplina;
	private Integer qtdAlunoReposicao;
	private String descricaoComplementarDisciplina;
	private String nomeCoordenador;
	private String emailCoordenador;
	private Integer numeroVagaReposicao;
	private Integer numeroDisciplina;
	private Integer codigoTurma;
	private Integer codigoDisciplina;
	private Boolean turmaAgrupada;
	private Boolean subTurma;
	private TipoSubTurmaEnum tipoSubTurmaEnum;
	private Boolean anual;
	private Boolean semestral;
	private Boolean ocultarhorarioaulavisaoprofessor;
	private Boolean ead;
	private Integer codigoProfessor;
	private Integer qtdeAtivosSemReposicao;
	private Integer cargaHorariaTeorica;
	private Integer cargaHorariaPratica;
    private Date dataAbertura;

	/**
	 * Composto por COD TURMA (6), COD DIS (5), COD PROF(4), ANO(4), SEMESTRE(1)
	 * total de 15 digitos
	 */
	private String codigoBarra;

	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
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

	public String getDataModulo() {
		if (dataModulo == null) {
			dataModulo = "";
		}
		return dataModulo;
	}

	public void setDataModulo(String dataModulo) {
		this.dataModulo = dataModulo;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getNomeProfessorResumido() {
		return Uteis.getNomeResumidoPessoa(getProfessor());
	}

	public String getProfessor() {
		if (professor == null) {
			professor = "";
		}
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getDataUnidadeOrdenacao() {
		if (dataUnidadeOrdenacao == null) {
			dataUnidadeOrdenacao = "";
		}
		return dataUnidadeOrdenacao;
	}

	public void setDataUnidadeOrdenacao(String dataUnidadeOrdenacao) {
		this.dataUnidadeOrdenacao = dataUnidadeOrdenacao;
	}

	public Integer getQtdeAlunoAtivo() {
		if (qtdeAlunoAtivo == null) {
			qtdeAlunoAtivo = 0;
		}
		return qtdeAlunoAtivo;
	}

	public void setQtdeAlunoAtivo(Integer qtdeAlunoAtivo) {
		this.qtdeAlunoAtivo = qtdeAlunoAtivo;
	}

	public Integer getQtdeAlunoPreMatricula() {
		if (qtdeAlunoPreMatricula == null) {
			qtdeAlunoPreMatricula = 0;
		}
		return qtdeAlunoPreMatricula;
	}

	public void setQtdeAlunoPreMatricula(Integer qtdeAlunoPreMatricula) {
		this.qtdeAlunoPreMatricula = qtdeAlunoPreMatricula;
	}

	public Integer getQtdeMediaCalculada() {
		if (qtdeMediaCalculada == null) {
			qtdeMediaCalculada = 0;
		}
		return qtdeMediaCalculada;
	}

	public void setQtdeMediaCalculada(Integer qtdeMediaCalculada) {
		this.qtdeMediaCalculada = qtdeMediaCalculada;
	}

	public String getTelefoneProfessor() {
		if (telefoneProfessor == null) {
			telefoneProfessor = "";
		}
		return telefoneProfessor;
	}

	public void setTelefoneProfessor(String telefoneProfessor) {
		this.telefoneProfessor = telefoneProfessor;
	}

	public String getEmailProfessor() {
		if (emailProfessor == null) {
			emailProfessor = "";
		}
		return emailProfessor;
	}

	public void setEmailProfessor(String emailProfessor) {
		this.emailProfessor = emailProfessor;
	}

	public String getCelularProfessor() {
		if (celularProfessor == null) {
			celularProfessor = "";
		}
		return celularProfessor;
	}

	public void setCelularProfessor(String celularProfessor) {
		this.celularProfessor = celularProfessor;
	}

	public String getModulo() {
		if (modulo == null) {
			modulo = "";
		}
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getTitulacaoProfessor() {
		if (titulacaoProfessor == null) {
			titulacaoProfessor = "";
		}
		return titulacaoProfessor;
	}

	public void setTitulacaoProfessor(String titulacaoProfessor) {
		this.titulacaoProfessor = titulacaoProfessor;
	}

	public Integer getQtdeMaterialPostado() {
		if (qtdeMaterialPostado == null) {
			qtdeMaterialPostado = 0;
		}
		return qtdeMaterialPostado;
	}

	public void setQtdeMaterialPostado(Integer qtdeMaterialPostado) {
		this.qtdeMaterialPostado = qtdeMaterialPostado;
	}

	public String getLocal() {
		if (local == null) {
			local = "";
		}
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getHorarioInicio() {
		if (horarioInicio == null) {
			horarioInicio = "";
		}
		return horarioInicio;
	}

	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public String getHorarioTermino() {
		if (horarioTermino == null) {
			horarioTermino = "";
		}
		return horarioTermino;
	}

	public void setHorarioTermino(String horarioTermino) {
		this.horarioTermino = horarioTermino;
	}

	/**
	 * @return the curso
	 */
	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	/**
	 * @param curso
	 *            the curso to set
	 */
	public void setCurso(String curso) {
		this.curso = curso;
	}

	/**
	 * @return the periodo
	 */
	public String getPeriodo() {
		if (periodo == null) {
			periodo = "";
		}
		return periodo;
	}

	/**
	 * @param periodo
	 *            the periodo to set
	 */
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * @return the semestre
	 */
	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	/**
	 * @param semestre
	 *            the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the gradeCurricular
	 */
	public String getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = "";
		}
		return gradeCurricular;
	}

	/**
	 * @param gradeCurricular
	 *            the gradeCurricular to set
	 */
	public void setGradeCurricular(String gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	/**
	 * @return the diaSemana
	 */
	public String getDiaSemana() {
		if (diaSemana == null) {
			diaSemana = "";
		}
		return diaSemana;
	}

	/**
	 * @return the numeroAula
	 */
	public Integer getNumeroAula() {
		if (numeroAula == null) {
			numeroAula = 0;
		}
		return numeroAula;
	}

	/**
	 * @param numeroAula
	 *            the numeroAula to set
	 */
	public void setNumeroAula(Integer numeroAula) {
		this.numeroAula = numeroAula;
	}

	/**
	 * @return the sala
	 */
	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}

	/**
	 * @param sala
	 *            the sala to set
	 */
	public void setSala(String sala) {
		this.sala = sala;
	}

	/**
	 * @param diaSemana
	 *            the diaSemana to set
	 */
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public String getOrdenacaoAulaHorario() {
		return getUnidadeEnsino() + getTurma() + getAno() + getSemestre() + getDataModulo() + getHorarioInicio();
	}

	/**
	 * @return the numeroVaga
	 */
	public Integer getNumeroVaga() {
		if (numeroVaga == null) {
			numeroVaga = 0;
		}
		return numeroVaga;
	}

	/**
	 * @param numeroVaga
	 *            the numeroVaga to set
	 */
	public void setNumeroVaga(Integer numeroVaga) {
		this.numeroVaga = numeroVaga;
	}

	public String getMatriculaProfessor() {
		if (matriculaProfessor == null) {
			matriculaProfessor = "";
		}
		return matriculaProfessor;
	}

	public void setMatriculaProfessor(String matriculaProfessor) {
		this.matriculaProfessor = matriculaProfessor;
	}

	/**
	 * @return the turno
	 */
	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}

	/**
	 * @param turno
	 *            the turno to set
	 */
	public void setTurno(String turno) {
		this.turno = turno;
	}

	/**
	 * @return the codigoBarra
	 */
	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return codigoBarra;
	}

	/**
	 * @param codigoBarra
	 *            the codigoBarra to set
	 */
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	/**
	 * @return the cargaHorariaDisciplina
	 */
	public Integer getCargaHorariaDisciplina() {
		if (cargaHorariaDisciplina == null) {
			cargaHorariaDisciplina = 0;
		}
		return cargaHorariaDisciplina;
	}

	/**
	 * @param cargaHorariaDisciplina
	 *            the cargaHorariaDisciplina to set
	 */
	public void setCargaHorariaDisciplina(Integer cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	/**
	 * @return the horaAulaDisciplina
	 */
	public Integer getHoraAulaDisciplina() {
		if (horaAulaDisciplina == null) {
			horaAulaDisciplina = 0;
		}
		return horaAulaDisciplina;
	}

	/**
	 * @param horaAulaDisciplina
	 *            the horaAulaDisciplina to set
	 */
	public void setHoraAulaDisciplina(Integer horaAulaDisciplina) {
		this.horaAulaDisciplina = horaAulaDisciplina;
	}

	public Integer getQtdAlunoReposicao() {
		if (qtdAlunoReposicao == null) {
			qtdAlunoReposicao = 0;
		}
		return qtdAlunoReposicao;
	}

	public void setQtdAlunoReposicao(Integer qtdAlunoReposicao) {
		this.qtdAlunoReposicao = qtdAlunoReposicao;
	}

	public String getDescricaoComplementarDisciplina() {
		if (descricaoComplementarDisciplina == null) {
			descricaoComplementarDisciplina = "";
		}
		return descricaoComplementarDisciplina;
	}

	public void setDescricaoComplementarDisciplina(String descricaoComplementarDisciplina) {
		this.descricaoComplementarDisciplina = descricaoComplementarDisciplina;
	}

	public String getNomeCoordenador() {
		if (nomeCoordenador == null) {
			nomeCoordenador = "";
		}
		return nomeCoordenador;
	}

	public void setNomeCoordenador(String nomeCoordenador) {
		this.nomeCoordenador = nomeCoordenador;
	}

	public String getEmailCoordenador() {
		if (emailCoordenador == null) {
			emailCoordenador = "";
		}
		return emailCoordenador;
	}

	public void setEmailCoordenador(String emailCoordenador) {
		this.emailCoordenador = emailCoordenador;
	}

	public Integer getNumeroDisciplina() {
		if (numeroDisciplina == null) {
			numeroDisciplina = 0;
		}
		return numeroDisciplina;
	}

	public void setNumeroDisciplina(Integer numeroDisciplina) {
		this.numeroDisciplina = numeroDisciplina;
	}

	public Integer getNumeroVagaReposicao() {
		if (numeroVagaReposicao == null) {
			numeroVagaReposicao = 0;
		}
		return numeroVagaReposicao;
	}

	public void setNumeroVagaReposicao(Integer numeroVagaReposicao) {
		this.numeroVagaReposicao = numeroVagaReposicao;
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

	public Integer getCodigoDisciplina() {
		if (codigoDisciplina == null) {
			codigoDisciplina = 0;
		}
		return codigoDisciplina;
	}

	public void setCodigoDisciplina(Integer codigoDisciplina) {
		this.codigoDisciplina = codigoDisciplina;
	}

	public Boolean getTurmaAgrupada() {
		if (turmaAgrupada == null) {
			turmaAgrupada = false;
		}
		return turmaAgrupada;
	}

	public void setTurmaAgrupada(Boolean turmaAgrupada) {
		this.turmaAgrupada = turmaAgrupada;
	}

	public Boolean getSubTurma() {
		if (subTurma == null) {
			subTurma = false;
		}
		return subTurma;
	}

	public void setSubTurma(Boolean subTurma) {
		this.subTurma = subTurma;
	}

	public TipoSubTurmaEnum getTipoSubTurmaEnum() {
		if (tipoSubTurmaEnum == null) {
			tipoSubTurmaEnum = TipoSubTurmaEnum.GERAL;
		}
		return tipoSubTurmaEnum;
	}

	public void setTipoSubTurmaEnum(TipoSubTurmaEnum tipoSubTurmaEnum) {
		this.tipoSubTurmaEnum = tipoSubTurmaEnum;
	}

	public Boolean getAnual() {
		if (anual == null) {
			anual = false;
		}
		return anual;
	}

	public void setAnual(Boolean anual) {
		this.anual = anual;
	}

	public Boolean getSemestral() {
		if (semestral == null) {
			semestral = false;
		}
		return semestral;
	}

	public void setSemestral(Boolean semestral) {
		this.semestral = semestral;
	}

	public Boolean getOcultarhorarioaulavisaoprofessor() {
		if (ocultarhorarioaulavisaoprofessor == null) {
			ocultarhorarioaulavisaoprofessor = false;
		}

		return ocultarhorarioaulavisaoprofessor;
	}

	public void setOcultarhorarioaulavisaoprofessor(Boolean ocultarhorarioaulavisaoprofessor) {
		this.ocultarhorarioaulavisaoprofessor = ocultarhorarioaulavisaoprofessor;
	}

	public Boolean getEad() {
		if (ead == null) {
			ead = false;
		}
		return ead;
	}

	public void setEad(Boolean ead) {
		this.ead = ead;
	}

	public Integer getCodigoProfessor() {
		if (codigoProfessor == null) {
			codigoProfessor = 0;
		}
		return codigoProfessor;
	}

	public void setCodigoProfessor(Integer codigoProfessor) {
		this.codigoProfessor = codigoProfessor;
	}

	public Integer getQtdeAtivosSemReposicao() {
		if (qtdeAtivosSemReposicao == null) {
			qtdeAtivosSemReposicao = 0;
		}
		return qtdeAtivosSemReposicao;
	}

	public void setQtdeAtivosSemReposicao(Integer qtdeAtivosSemReposicao) {
		this.qtdeAtivosSemReposicao = qtdeAtivosSemReposicao;
	}

	public Integer getCargaHorariaTeorica() {
		if (cargaHorariaTeorica == null) {
			cargaHorariaTeorica = 0;
		}
		return cargaHorariaTeorica;
	}

	public void setCargaHorariaTeorica(Integer cargaHorariaTeorica) {
		this.cargaHorariaTeorica = cargaHorariaTeorica;
	}

	public Integer getCargaHorariaPratica() {
		if (cargaHorariaPratica == null) {
			cargaHorariaPratica = 0;
		}
		return cargaHorariaPratica;
	}

	public void setCargaHorariaPratica(Integer cargaHorariaPratica) {
		this.cargaHorariaPratica = cargaHorariaPratica;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}	
}
