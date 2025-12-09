package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class CalendarioLancamentoNotaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3705593141862029952L;
	private Integer codigo;
	private String ano;
	private String semestre;
	private ConfiguracaoAcademicoVO configuracaoAcademico;
	private UnidadeEnsinoVO unidadeEnsino;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private TurmaVO turma;
	protected CursoVO curso;
	protected PessoaVO pessoa;
	private PessoaVO professor;
	private DisciplinaVO disciplina;
	private String calendarioPor;
	private String periodicidade;
	private Date dataInicioNota1;
	private Date dataInicioNota2;
	private Date dataInicioNota3;
	private Date dataInicioNota4;
	private Date dataInicioNota5;
	private Date dataInicioNota6;
	private Date dataInicioNota7;
	private Date dataInicioNota8;
	private Date dataInicioNota9;
	private Date dataInicioNota10;
	private Date dataInicioNota11;
	private Date dataInicioNota12;
	private Date dataInicioNota13;
	private Date dataInicioNota14;
	private Date dataInicioNota15;
	private Date dataInicioNota16;
	private Date dataInicioNota17;
	private Date dataInicioNota18;
	private Date dataInicioNota19;
	private Date dataInicioNota20;
	private Date dataInicioNota21;
	private Date dataInicioNota22;
	private Date dataInicioNota23;
	private Date dataInicioNota24;
	private Date dataInicioNota25;
	private Date dataInicioNota26;
	private Date dataInicioNota27;
	private Date dataInicioNota28;
	private Date dataInicioNota29;
	private Date dataInicioNota30;
	private Date dataInicioNota31;
	private Date dataInicioNota32;
	private Date dataInicioNota33;
	private Date dataInicioNota34;
	private Date dataInicioNota35;
	private Date dataInicioNota36;
	private Date dataInicioNota37;
	private Date dataInicioNota38;
	private Date dataInicioNota39;
	private Date dataInicioNota40;
	
	private Date dataTerminoNota1;
	private Date dataTerminoNota2;
	private Date dataTerminoNota3;
	private Date dataTerminoNota4;
	private Date dataTerminoNota5;
	private Date dataTerminoNota6;
	private Date dataTerminoNota7;
	private Date dataTerminoNota8;
	private Date dataTerminoNota9;
	private Date dataTerminoNota10;
	private Date dataTerminoNota11;
	private Date dataTerminoNota12;
	private Date dataTerminoNota13;
	private Date dataTerminoNota14;
	private Date dataTerminoNota15;
	private Date dataTerminoNota16;
	private Date dataTerminoNota17;
	private Date dataTerminoNota18;
	private Date dataTerminoNota19;
	private Date dataTerminoNota20;
	private Date dataTerminoNota21;
	private Date dataTerminoNota22;
	private Date dataTerminoNota23;
	private Date dataTerminoNota24;
	private Date dataTerminoNota25;
	private Date dataTerminoNota26;
	private Date dataTerminoNota27;
	private Date dataTerminoNota28;
	private Date dataTerminoNota29;
	private Date dataTerminoNota30;
	private Date dataTerminoNota31;
	private Date dataTerminoNota32;
	private Date dataTerminoNota33;
	private Date dataTerminoNota34;
	private Date dataTerminoNota35;
	private Date dataTerminoNota36;
	private Date dataTerminoNota37;
	private Date dataTerminoNota38;
	private Date dataTerminoNota39;
	private Date dataTerminoNota40;
	
	private Date dataLiberacaoAlunoNota1;
	private Date dataLiberacaoAlunoNota2;
	private Date dataLiberacaoAlunoNota3;
	private Date dataLiberacaoAlunoNota4;
	private Date dataLiberacaoAlunoNota5;
	private Date dataLiberacaoAlunoNota6;
	private Date dataLiberacaoAlunoNota7;
	private Date dataLiberacaoAlunoNota8;
	private Date dataLiberacaoAlunoNota9;
	private Date dataLiberacaoAlunoNota10;
	private Date dataLiberacaoAlunoNota11;
	private Date dataLiberacaoAlunoNota12;
	private Date dataLiberacaoAlunoNota13;
	private Date dataLiberacaoAlunoNota14;
	private Date dataLiberacaoAlunoNota15;
	private Date dataLiberacaoAlunoNota16;
	private Date dataLiberacaoAlunoNota17;
	private Date dataLiberacaoAlunoNota18;
	private Date dataLiberacaoAlunoNota19;
	private Date dataLiberacaoAlunoNota20;
	private Date dataLiberacaoAlunoNota21;
	private Date dataLiberacaoAlunoNota22;
	private Date dataLiberacaoAlunoNota23;
	private Date dataLiberacaoAlunoNota24;
	private Date dataLiberacaoAlunoNota25;
	private Date dataLiberacaoAlunoNota26;
	private Date dataLiberacaoAlunoNota27;
	private Date dataLiberacaoAlunoNota28;
	private Date dataLiberacaoAlunoNota29;
	private Date dataLiberacaoAlunoNota30;
	private Date dataLiberacaoAlunoNota31;
	private Date dataLiberacaoAlunoNota32;
	private Date dataLiberacaoAlunoNota33;
	private Date dataLiberacaoAlunoNota34;
	private Date dataLiberacaoAlunoNota35;
	private Date dataLiberacaoAlunoNota36;
	private Date dataLiberacaoAlunoNota37;
	private Date dataLiberacaoAlunoNota38;
	private Date dataLiberacaoAlunoNota39;
	private Date dataLiberacaoAlunoNota40;
	
	private Date dataLiberacaoAlunoCalculoMediaFinal;
	private Date dataInicioCalculoMediaFinal;
	private Date dataTerminoCalculoMediaFinal;

	/**
	 * Início Transient
	 */
	private Boolean apresentarNota1VisaoAluno;
	private Boolean apresentarNota2VisaoAluno;
	private Boolean apresentarNota3VisaoAluno;
	private Boolean apresentarNota4VisaoAluno;
	private Boolean apresentarNota5VisaoAluno;
	private Boolean apresentarNota6VisaoAluno;
	private Boolean apresentarNota7VisaoAluno;
	private Boolean apresentarNota8VisaoAluno;
	private Boolean apresentarNota9VisaoAluno;
	private Boolean apresentarNota10VisaoAluno;
	private Boolean apresentarNota11VisaoAluno;
	private Boolean apresentarNota12VisaoAluno;
	private Boolean apresentarNota13VisaoAluno;
	private Boolean apresentarNota14VisaoAluno;
	private Boolean apresentarNota15VisaoAluno;
	private Boolean apresentarNota16VisaoAluno;
	private Boolean apresentarNota17VisaoAluno;
	private Boolean apresentarNota18VisaoAluno;
	private Boolean apresentarNota19VisaoAluno;
	private Boolean apresentarNota20VisaoAluno;
	private Boolean apresentarNota21VisaoAluno;
	private Boolean apresentarNota22VisaoAluno;
	private Boolean apresentarNota23VisaoAluno;
	private Boolean apresentarNota24VisaoAluno;
	private Boolean apresentarNota25VisaoAluno;
	private Boolean apresentarNota26VisaoAluno;
	private Boolean apresentarNota27VisaoAluno;
	private Boolean apresentarNota28VisaoAluno;
	private Boolean apresentarNota29VisaoAluno;
	private Boolean apresentarNota30VisaoAluno;
	private Boolean apresentarNota31VisaoAluno;
	private Boolean apresentarNota32VisaoAluno;
	private Boolean apresentarNota33VisaoAluno;
	private Boolean apresentarNota34VisaoAluno;
	private Boolean apresentarNota35VisaoAluno;
	private Boolean apresentarNota36VisaoAluno;
	private Boolean apresentarNota37VisaoAluno;
	private Boolean apresentarNota38VisaoAluno;
	private Boolean apresentarNota39VisaoAluno;
	private Boolean apresentarNota40VisaoAluno;
	
	private Boolean apresentarCalculoMediaFinalVisaoAluno;
	private Boolean atualizarCalendarioAtividadeMatriculaComPeriodo;
	
	private Date calcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde;
	private PessoaVO professorExclusivoLancamentoDeNota;

	/**
	 * Fim Transient
	 */

	public String getAno() {
		if (ano == null) {
//			ano = Uteis.getAnoDataAtual();
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre== null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademico;
	}

	public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademico = configuracaoAcademicoVO;
	}

	public Date getDataInicioNota1() {
		return dataInicioNota1;
	}

	public void setDataInicioNota1(Date dataInicioNota1) {
		this.dataInicioNota1 = dataInicioNota1;
	}

	public Date getDataInicioNota2() {
		return dataInicioNota2;
	}

	public void setDataInicioNota2(Date dataInicioNota2) {
		this.dataInicioNota2 = dataInicioNota2;
	}

	public Date getDataInicioNota3() {
		return dataInicioNota3;
	}

	public void setDataInicioNota3(Date dataInicioNota3) {
		this.dataInicioNota3 = dataInicioNota3;
	}

	public Date getDataInicioNota4() {
		return dataInicioNota4;
	}

	public void setDataInicioNota4(Date dataInicioNota4) {
		this.dataInicioNota4 = dataInicioNota4;
	}

	public Date getDataInicioNota5() {
		return dataInicioNota5;
	}

	public void setDataInicioNota5(Date dataInicioNota5) {
		this.dataInicioNota5 = dataInicioNota5;
	}

	public Date getDataInicioNota6() {
		return dataInicioNota6;
	}

	public void setDataInicioNota6(Date dataInicioNota6) {
		this.dataInicioNota6 = dataInicioNota6;
	}

	public Date getDataInicioNota7() {
		return dataInicioNota7;
	}

	public void setDataInicioNota7(Date dataInicioNota7) {
		this.dataInicioNota7 = dataInicioNota7;
	}

	public Date getDataInicioNota8() {
		return dataInicioNota8;
	}

	public void setDataInicioNota8(Date dataInicioNota8) {
		this.dataInicioNota8 = dataInicioNota8;
	}

	public Date getDataInicioNota9() {
		return dataInicioNota9;
	}

	public void setDataInicioNota9(Date dataInicioNota9) {
		this.dataInicioNota9 = dataInicioNota9;
	}

	public Date getDataInicioNota10() {
		return dataInicioNota10;
	}

	public void setDataInicioNota10(Date dataInicioNota10) {
		this.dataInicioNota10 = dataInicioNota10;
	}

	public Date getDataInicioNota11() {
		return dataInicioNota11;
	}

	public void setDataInicioNota11(Date dataInicioNota11) {
		this.dataInicioNota11 = dataInicioNota11;
	}

	public Date getDataInicioNota12() {
		return dataInicioNota12;
	}

	public void setDataInicioNota12(Date dataInicioNota12) {
		this.dataInicioNota12 = dataInicioNota12;
	}

	public Date getDataInicioNota13() {
		return dataInicioNota13;
	}

	public void setDataInicioNota13(Date dataInicioNota13) {
		this.dataInicioNota13 = dataInicioNota13;
	}

	public Date getDataTerminoNota1() {
		return dataTerminoNota1;
	}

	public void setDataTerminoNota1(Date dataTerminoNota1) {
		this.dataTerminoNota1 = dataTerminoNota1;
	}

	public Date getDataTerminoNota2() {
		return dataTerminoNota2;
	}

	public void setDataTerminoNota2(Date dataTerminoNota2) {
		this.dataTerminoNota2 = dataTerminoNota2;
	}

	public Date getDataTerminoNota3() {
		return dataTerminoNota3;
	}

	public void setDataTerminoNota3(Date dataTerminoNota3) {
		this.dataTerminoNota3 = dataTerminoNota3;
	}

	public Date getDataTerminoNota4() {
		return dataTerminoNota4;
	}

	public void setDataTerminoNota4(Date dataTerminoNota4) {
		this.dataTerminoNota4 = dataTerminoNota4;
	}

	public Date getDataTerminoNota5() {
		return dataTerminoNota5;
	}

	public void setDataTerminoNota5(Date dataTerminoNota5) {
		this.dataTerminoNota5 = dataTerminoNota5;
	}

	public Date getDataTerminoNota6() {
		return dataTerminoNota6;
	}

	public void setDataTerminoNota6(Date dataTerminoNota6) {
		this.dataTerminoNota6 = dataTerminoNota6;
	}

	public Date getDataTerminoNota7() {
		return dataTerminoNota7;
	}

	public void setDataTerminoNota7(Date dataTerminoNota7) {
		this.dataTerminoNota7 = dataTerminoNota7;
	}

	public Date getDataTerminoNota8() {
		return dataTerminoNota8;
	}

	public void setDataTerminoNota8(Date dataTerminoNota8) {
		this.dataTerminoNota8 = dataTerminoNota8;
	}

	public Date getDataTerminoNota9() {
		return dataTerminoNota9;
	}

	public void setDataTerminoNota9(Date dataTerminoNota9) {
		this.dataTerminoNota9 = dataTerminoNota9;
	}

	public Date getDataTerminoNota10() {
		return dataTerminoNota10;
	}

	public void setDataTerminoNota10(Date dataTerminoNota10) {
		this.dataTerminoNota10 = dataTerminoNota10;
	}

	public Date getDataTerminoNota11() {
		return dataTerminoNota11;
	}

	public void setDataTerminoNota11(Date dataTerminoNota11) {
		this.dataTerminoNota11 = dataTerminoNota11;
	}

	public Date getDataTerminoNota12() {
		return dataTerminoNota12;
	}

	public void setDataTerminoNota12(Date dataTerminoNota12) {
		this.dataTerminoNota12 = dataTerminoNota12;
	}

	public Date getDataTerminoNota13() {
		return dataTerminoNota13;
	}

	public void setDataTerminoNota13(Date dataTerminoNota13) {
		this.dataTerminoNota13 = dataTerminoNota13;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}
	
	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}
	
	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public Date getDataInicioCalculoMediaFinal() {
		return dataInicioCalculoMediaFinal;
	}

	public void setDataInicioCalculoMediaFinal(Date dataInicioCalculoMediaFinal) {
		this.dataInicioCalculoMediaFinal = dataInicioCalculoMediaFinal;
	}

	public Date getDataTerminoCalculoMediaFinal() {
		return dataTerminoCalculoMediaFinal;
	}

	public void setDataTerminoCalculoMediaFinal(Date dataTerminoCalculoMediaFinal) {
		this.dataTerminoCalculoMediaFinal = dataTerminoCalculoMediaFinal;
	}

	public Date getDataInicioNota14() {
		return dataInicioNota14;
	}

	public void setDataInicioNota14(Date dataInicioNota14) {
		this.dataInicioNota14 = dataInicioNota14;
	}

	public Date getDataInicioNota15() {
		return dataInicioNota15;
	}

	public void setDataInicioNota15(Date dataInicioNota15) {
		this.dataInicioNota15 = dataInicioNota15;
	}

	public Date getDataInicioNota16() {
		return dataInicioNota16;
	}

	public void setDataInicioNota16(Date dataInicioNota16) {
		this.dataInicioNota16 = dataInicioNota16;
	}

	public Date getDataInicioNota17() {
		return dataInicioNota17;
	}

	public void setDataInicioNota17(Date dataInicioNota17) {
		this.dataInicioNota17 = dataInicioNota17;
	}

	public Date getDataInicioNota18() {
		return dataInicioNota18;
	}

	public void setDataInicioNota18(Date dataInicioNota18) {
		this.dataInicioNota18 = dataInicioNota18;
	}

	public Date getDataInicioNota19() {
		return dataInicioNota19;
	}

	public void setDataInicioNota19(Date dataInicioNota19) {
		this.dataInicioNota19 = dataInicioNota19;
	}

	public Date getDataInicioNota20() {
		return dataInicioNota20;
	}

	public void setDataInicioNota20(Date dataInicioNota20) {
		this.dataInicioNota20 = dataInicioNota20;
	}

	public Date getDataInicioNota21() {
		return dataInicioNota21;
	}

	public void setDataInicioNota21(Date dataInicioNota21) {
		this.dataInicioNota21 = dataInicioNota21;
	}

	public Date getDataInicioNota22() {
		return dataInicioNota22;
	}

	public void setDataInicioNota22(Date dataInicioNota22) {
		this.dataInicioNota22 = dataInicioNota22;
	}

	public Date getDataInicioNota23() {
		return dataInicioNota23;
	}

	public void setDataInicioNota23(Date dataInicioNota23) {
		this.dataInicioNota23 = dataInicioNota23;
	}

	public Date getDataInicioNota24() {
		return dataInicioNota24;
	}

	public void setDataInicioNota24(Date dataInicioNota24) {
		this.dataInicioNota24 = dataInicioNota24;
	}

	public Date getDataInicioNota25() {
		return dataInicioNota25;
	}

	public void setDataInicioNota25(Date dataInicioNota25) {
		this.dataInicioNota25 = dataInicioNota25;
	}

	public Date getDataInicioNota26() {
		return dataInicioNota26;
	}

	public void setDataInicioNota26(Date dataInicioNota26) {
		this.dataInicioNota26 = dataInicioNota26;
	}

	public Date getDataInicioNota27() {
		return dataInicioNota27;
	}

	public void setDataInicioNota27(Date dataInicioNota27) {
		this.dataInicioNota27 = dataInicioNota27;
	}

	public Date getDataInicioNota28() {
		return dataInicioNota28;
	}

	public void setDataInicioNota28(Date dataInicioNota28) {
		this.dataInicioNota28 = dataInicioNota28;
	}

	public Date getDataTerminoNota14() {
		return dataTerminoNota14;
	}

	public void setDataTerminoNota14(Date dataTerminoNota14) {
		this.dataTerminoNota14 = dataTerminoNota14;
	}

	public Date getDataTerminoNota15() {
		return dataTerminoNota15;
	}

	public void setDataTerminoNota15(Date dataTerminoNota15) {
		this.dataTerminoNota15 = dataTerminoNota15;
	}

	public Date getDataTerminoNota16() {
		return dataTerminoNota16;
	}

	public void setDataTerminoNota16(Date dataTerminoNota16) {
		this.dataTerminoNota16 = dataTerminoNota16;
	}

	public Date getDataTerminoNota17() {
		return dataTerminoNota17;
	}

	public void setDataTerminoNota17(Date dataTerminoNota17) {
		this.dataTerminoNota17 = dataTerminoNota17;
	}

	public Date getDataTerminoNota18() {
		return dataTerminoNota18;
	}

	public void setDataTerminoNota18(Date dataTerminoNota18) {
		this.dataTerminoNota18 = dataTerminoNota18;
	}

	public Date getDataTerminoNota19() {
		return dataTerminoNota19;
	}

	public void setDataTerminoNota19(Date dataTerminoNota19) {
		this.dataTerminoNota19 = dataTerminoNota19;
	}

	public Date getDataTerminoNota20() {
		return dataTerminoNota20;
	}

	public void setDataTerminoNota20(Date dataTerminoNota20) {
		this.dataTerminoNota20 = dataTerminoNota20;
	}

	public Date getDataTerminoNota21() {
		return dataTerminoNota21;
	}

	public void setDataTerminoNota21(Date dataTerminoNota21) {
		this.dataTerminoNota21 = dataTerminoNota21;
	}

	public Date getDataTerminoNota22() {
		return dataTerminoNota22;
	}

	public void setDataTerminoNota22(Date dataTerminoNota22) {
		this.dataTerminoNota22 = dataTerminoNota22;
	}

	public Date getDataTerminoNota23() {
		return dataTerminoNota23;
	}

	public void setDataTerminoNota23(Date dataTerminoNota23) {
		this.dataTerminoNota23 = dataTerminoNota23;
	}

	public Date getDataTerminoNota24() {
		return dataTerminoNota24;
	}

	public void setDataTerminoNota24(Date dataTerminoNota24) {
		this.dataTerminoNota24 = dataTerminoNota24;
	}

	public Date getDataTerminoNota25() {
		return dataTerminoNota25;
	}

	public void setDataTerminoNota25(Date dataTerminoNota25) {
		this.dataTerminoNota25 = dataTerminoNota25;
	}

	public Date getDataTerminoNota26() {
		return dataTerminoNota26;
	}

	public void setDataTerminoNota26(Date dataTerminoNota26) {
		this.dataTerminoNota26 = dataTerminoNota26;
	}

	public Date getDataTerminoNota27() {
		return dataTerminoNota27;
	}

	public void setDataTerminoNota27(Date dataTerminoNota27) {
		this.dataTerminoNota27 = dataTerminoNota27;
	}

	public Date getDataTerminoNota28() {
		return dataTerminoNota28;
	}

	public void setDataTerminoNota28(Date dataTerminoNota28) {
		this.dataTerminoNota28 = dataTerminoNota28;
	}

	public Date getDataInicioNota29() {
		return dataInicioNota29;
	}

	public void setDataInicioNota29(Date dataInicioNota29) {
		this.dataInicioNota29 = dataInicioNota29;
	}

	public Date getDataInicioNota30() {
		return dataInicioNota30;
	}

	public void setDataInicioNota30(Date dataInicioNota30) {
		this.dataInicioNota30 = dataInicioNota30;
	}

	public Date getDataTerminoNota29() {
		return dataTerminoNota29;
	}

	public void setDataTerminoNota29(Date dataTerminoNota29) {
		this.dataTerminoNota29 = dataTerminoNota29;
	}

	public Date getDataTerminoNota30() {
		return dataTerminoNota30;
	}

	public void setDataTerminoNota30(Date dataTerminoNota30) {
		this.dataTerminoNota30 = dataTerminoNota30;
	}

	public Date getDataLiberacaoAlunoNota1() {
		return dataLiberacaoAlunoNota1;
	}

	public void setDataLiberacaoAlunoNota1(Date dataLiberacaoAlunoNota1) {
		this.dataLiberacaoAlunoNota1 = dataLiberacaoAlunoNota1;
	}

	public Date getDataLiberacaoAlunoNota2() {
		return dataLiberacaoAlunoNota2;
	}

	public void setDataLiberacaoAlunoNota2(Date dataLiberacaoAlunoNota2) {
		this.dataLiberacaoAlunoNota2 = dataLiberacaoAlunoNota2;
	}

	public Date getDataLiberacaoAlunoNota3() {
		return dataLiberacaoAlunoNota3;
	}

	public void setDataLiberacaoAlunoNota3(Date dataLiberacaoAlunoNota3) {
		this.dataLiberacaoAlunoNota3 = dataLiberacaoAlunoNota3;
	}

	public Date getDataLiberacaoAlunoNota4() {
		return dataLiberacaoAlunoNota4;
	}

	public void setDataLiberacaoAlunoNota4(Date dataLiberacaoAlunoNota4) {
		this.dataLiberacaoAlunoNota4 = dataLiberacaoAlunoNota4;
	}

	public Date getDataLiberacaoAlunoNota5() {
		return dataLiberacaoAlunoNota5;
	}

	public void setDataLiberacaoAlunoNota5(Date dataLiberacaoAlunoNota5) {
		this.dataLiberacaoAlunoNota5 = dataLiberacaoAlunoNota5;
	}

	public Date getDataLiberacaoAlunoNota6() {
		return dataLiberacaoAlunoNota6;
	}

	public void setDataLiberacaoAlunoNota6(Date dataLiberacaoAlunoNota6) {
		this.dataLiberacaoAlunoNota6 = dataLiberacaoAlunoNota6;
	}

	public Date getDataLiberacaoAlunoNota7() {
		return dataLiberacaoAlunoNota7;
	}

	public void setDataLiberacaoAlunoNota7(Date dataLiberacaoAlunoNota7) {
		this.dataLiberacaoAlunoNota7 = dataLiberacaoAlunoNota7;
	}

	public Date getDataLiberacaoAlunoNota8() {
		return dataLiberacaoAlunoNota8;
	}

	public void setDataLiberacaoAlunoNota8(Date dataLiberacaoAlunoNota8) {
		this.dataLiberacaoAlunoNota8 = dataLiberacaoAlunoNota8;
	}

	public Date getDataLiberacaoAlunoNota9() {
		return dataLiberacaoAlunoNota9;
	}

	public void setDataLiberacaoAlunoNota9(Date dataLiberacaoAlunoNota9) {
		this.dataLiberacaoAlunoNota9 = dataLiberacaoAlunoNota9;
	}

	public Date getDataLiberacaoAlunoNota10() {
		return dataLiberacaoAlunoNota10;
	}

	public void setDataLiberacaoAlunoNota10(Date dataLiberacaoAlunoNota10) {
		this.dataLiberacaoAlunoNota10 = dataLiberacaoAlunoNota10;
	}

	public Date getDataLiberacaoAlunoNota11() {
		return dataLiberacaoAlunoNota11;
	}

	public void setDataLiberacaoAlunoNota11(Date dataLiberacaoAlunoNota11) {
		this.dataLiberacaoAlunoNota11 = dataLiberacaoAlunoNota11;
	}

	public Date getDataLiberacaoAlunoNota12() {
		return dataLiberacaoAlunoNota12;
	}

	public void setDataLiberacaoAlunoNota12(Date dataLiberacaoAlunoNota12) {
		this.dataLiberacaoAlunoNota12 = dataLiberacaoAlunoNota12;
	}

	public Date getDataLiberacaoAlunoNota13() {
		return dataLiberacaoAlunoNota13;
	}

	public void setDataLiberacaoAlunoNota13(Date dataLiberacaoAlunoNota13) {
		this.dataLiberacaoAlunoNota13 = dataLiberacaoAlunoNota13;
	}

	public Date getDataLiberacaoAlunoNota14() {
		return dataLiberacaoAlunoNota14;
	}

	public void setDataLiberacaoAlunoNota14(Date dataLiberacaoAlunoNota14) {
		this.dataLiberacaoAlunoNota14 = dataLiberacaoAlunoNota14;
	}

	public Date getDataLiberacaoAlunoNota15() {
		return dataLiberacaoAlunoNota15;
	}

	public void setDataLiberacaoAlunoNota15(Date dataLiberacaoAlunoNota15) {
		this.dataLiberacaoAlunoNota15 = dataLiberacaoAlunoNota15;
	}

	public Date getDataLiberacaoAlunoNota16() {
		return dataLiberacaoAlunoNota16;
	}

	public void setDataLiberacaoAlunoNota16(Date dataLiberacaoAlunoNota16) {
		this.dataLiberacaoAlunoNota16 = dataLiberacaoAlunoNota16;
	}

	public Date getDataLiberacaoAlunoNota17() {
		return dataLiberacaoAlunoNota17;
	}

	public void setDataLiberacaoAlunoNota17(Date dataLiberacaoAlunoNota17) {
		this.dataLiberacaoAlunoNota17 = dataLiberacaoAlunoNota17;
	}

	public Date getDataLiberacaoAlunoNota18() {
		return dataLiberacaoAlunoNota18;
	}

	public void setDataLiberacaoAlunoNota18(Date dataLiberacaoAlunoNota18) {
		this.dataLiberacaoAlunoNota18 = dataLiberacaoAlunoNota18;
	}

	public Date getDataLiberacaoAlunoNota19() {
		return dataLiberacaoAlunoNota19;
	}

	public void setDataLiberacaoAlunoNota19(Date dataLiberacaoAlunoNota19) {
		this.dataLiberacaoAlunoNota19 = dataLiberacaoAlunoNota19;
	}

	public Date getDataLiberacaoAlunoNota20() {
		return dataLiberacaoAlunoNota20;
	}

	public void setDataLiberacaoAlunoNota20(Date dataLiberacaoAlunoNota20) {
		this.dataLiberacaoAlunoNota20 = dataLiberacaoAlunoNota20;
	}

	public Date getDataLiberacaoAlunoNota21() {
		return dataLiberacaoAlunoNota21;
	}

	public void setDataLiberacaoAlunoNota21(Date dataLiberacaoAlunoNota21) {
		this.dataLiberacaoAlunoNota21 = dataLiberacaoAlunoNota21;
	}

	public Date getDataLiberacaoAlunoNota22() {
		return dataLiberacaoAlunoNota22;
	}

	public void setDataLiberacaoAlunoNota22(Date dataLiberacaoAlunoNota22) {
		this.dataLiberacaoAlunoNota22 = dataLiberacaoAlunoNota22;
	}

	public Date getDataLiberacaoAlunoNota23() {
		return dataLiberacaoAlunoNota23;
	}

	public void setDataLiberacaoAlunoNota23(Date dataLiberacaoAlunoNota23) {
		this.dataLiberacaoAlunoNota23 = dataLiberacaoAlunoNota23;
	}

	public Date getDataLiberacaoAlunoNota24() {
		return dataLiberacaoAlunoNota24;
	}

	public void setDataLiberacaoAlunoNota24(Date dataLiberacaoAlunoNota24) {
		this.dataLiberacaoAlunoNota24 = dataLiberacaoAlunoNota24;
	}

	public Date getDataLiberacaoAlunoNota25() {
		return dataLiberacaoAlunoNota25;
	}

	public void setDataLiberacaoAlunoNota25(Date dataLiberacaoAlunoNota25) {
		this.dataLiberacaoAlunoNota25 = dataLiberacaoAlunoNota25;
	}

	public Date getDataLiberacaoAlunoNota26() {
		return dataLiberacaoAlunoNota26;
	}

	public void setDataLiberacaoAlunoNota26(Date dataLiberacaoAlunoNota26) {
		this.dataLiberacaoAlunoNota26 = dataLiberacaoAlunoNota26;
	}

	public Date getDataLiberacaoAlunoNota27() {
		return dataLiberacaoAlunoNota27;
	}

	public void setDataLiberacaoAlunoNota27(Date dataLiberacaoAlunoNota27) {
		this.dataLiberacaoAlunoNota27 = dataLiberacaoAlunoNota27;
	}

	public Date getDataLiberacaoAlunoNota28() {
		return dataLiberacaoAlunoNota28;
	}

	public void setDataLiberacaoAlunoNota28(Date dataLiberacaoAlunoNota28) {
		this.dataLiberacaoAlunoNota28 = dataLiberacaoAlunoNota28;
	}

	public Date getDataLiberacaoAlunoNota29() {
		return dataLiberacaoAlunoNota29;
	}

	public void setDataLiberacaoAlunoNota29(Date dataLiberacaoAlunoNota29) {
		this.dataLiberacaoAlunoNota29 = dataLiberacaoAlunoNota29;
	}

	public Date getDataLiberacaoAlunoNota30() {
		return dataLiberacaoAlunoNota30;
	}

	public void setDataLiberacaoAlunoNota30(Date dataLiberacaoAlunoNota30) {
		this.dataLiberacaoAlunoNota30 = dataLiberacaoAlunoNota30;
	}

	public Date getDataLiberacaoAlunoCalculoMediaFinal() {
		return dataLiberacaoAlunoCalculoMediaFinal;
	}

	public void setDataLiberacaoAlunoCalculoMediaFinal(Date dataLiberacaoAlunoCalculoMediaFinal) {
		this.dataLiberacaoAlunoCalculoMediaFinal = dataLiberacaoAlunoCalculoMediaFinal;
	}

	public Boolean getApresentarNota1VisaoAluno() {
		if (apresentarNota1VisaoAluno == null) {
			apresentarNota1VisaoAluno = true;
		}
		return apresentarNota1VisaoAluno;
	}

	public void setApresentarNota1VisaoAluno(Boolean apresentarNota1VisaoAluno) {
		this.apresentarNota1VisaoAluno = apresentarNota1VisaoAluno;
	}

	public Boolean getApresentarNota2VisaoAluno() {
		if (apresentarNota2VisaoAluno == null) {
			apresentarNota2VisaoAluno = true;
		}
		return apresentarNota2VisaoAluno;
	}

	public void setApresentarNota2VisaoAluno(Boolean apresentarNota2VisaoAluno) {
		this.apresentarNota2VisaoAluno = apresentarNota2VisaoAluno;
	}

	public Boolean getApresentarNota3VisaoAluno() {
		if (apresentarNota3VisaoAluno == null) {
			apresentarNota3VisaoAluno = true;
		}
		return apresentarNota3VisaoAluno;
	}

	public void setApresentarNota3VisaoAluno(Boolean apresentarNota3VisaoAluno) {
		this.apresentarNota3VisaoAluno = apresentarNota3VisaoAluno;
	}

	public Boolean getApresentarNota4VisaoAluno() {
		if (apresentarNota4VisaoAluno == null) {
			apresentarNota4VisaoAluno = true;
		}
		return apresentarNota4VisaoAluno;
	}

	public void setApresentarNota4VisaoAluno(Boolean apresentarNota4VisaoAluno) {
		this.apresentarNota4VisaoAluno = apresentarNota4VisaoAluno;
	}

	public Boolean getApresentarNota5VisaoAluno() {
		if (apresentarNota5VisaoAluno == null) {
			apresentarNota5VisaoAluno = true;
		}
		return apresentarNota5VisaoAluno;
	}

	public void setApresentarNota5VisaoAluno(Boolean apresentarNota5VisaoAluno) {
		this.apresentarNota5VisaoAluno = apresentarNota5VisaoAluno;
	}

	public Boolean getApresentarNota6VisaoAluno() {
		if (apresentarNota6VisaoAluno == null) {
			apresentarNota6VisaoAluno = true;
		}
		return apresentarNota6VisaoAluno;
	}

	public void setApresentarNota6VisaoAluno(Boolean apresentarNota6VisaoAluno) {
		this.apresentarNota6VisaoAluno = apresentarNota6VisaoAluno;
	}

	public Boolean getApresentarNota7VisaoAluno() {
		if (apresentarNota7VisaoAluno == null) {
			apresentarNota7VisaoAluno = true;
		}
		return apresentarNota7VisaoAluno;
	}

	public void setApresentarNota7VisaoAluno(Boolean apresentarNota7VisaoAluno) {
		this.apresentarNota7VisaoAluno = apresentarNota7VisaoAluno;
	}

	public Boolean getApresentarNota8VisaoAluno() {
		if (apresentarNota8VisaoAluno == null) {
			apresentarNota8VisaoAluno = true;
		}
		return apresentarNota8VisaoAluno;
	}

	public void setApresentarNota8VisaoAluno(Boolean apresentarNota8VisaoAluno) {
		this.apresentarNota8VisaoAluno = apresentarNota8VisaoAluno;
	}

	public Boolean getApresentarNota9VisaoAluno() {
		if (apresentarNota9VisaoAluno == null) {
			apresentarNota9VisaoAluno = true;
		}
		return apresentarNota9VisaoAluno;
	}

	public void setApresentarNota9VisaoAluno(Boolean apresentarNota9VisaoAluno) {
		this.apresentarNota9VisaoAluno = apresentarNota9VisaoAluno;
	}

	public Boolean getApresentarNota10VisaoAluno() {
		if (apresentarNota10VisaoAluno == null) {
			apresentarNota10VisaoAluno = true;
		}
		return apresentarNota10VisaoAluno;
	}

	public void setApresentarNota10VisaoAluno(Boolean apresentarNota10VisaoAluno) {
		this.apresentarNota10VisaoAluno = apresentarNota10VisaoAluno;
	}

	public Boolean getApresentarNota11VisaoAluno() {
		if (apresentarNota11VisaoAluno == null) {
			apresentarNota11VisaoAluno = true;
		}
		return apresentarNota11VisaoAluno;
	}

	public void setApresentarNota11VisaoAluno(Boolean apresentarNota11VisaoAluno) {
		this.apresentarNota11VisaoAluno = apresentarNota11VisaoAluno;
	}

	public Boolean getApresentarNota12VisaoAluno() {
		if (apresentarNota12VisaoAluno == null) {
			apresentarNota12VisaoAluno = true;
		}
		return apresentarNota12VisaoAluno;
	}

	public void setApresentarNota12VisaoAluno(Boolean apresentarNota12VisaoAluno) {
		this.apresentarNota12VisaoAluno = apresentarNota12VisaoAluno;
	}

	public Boolean getApresentarNota13VisaoAluno() {
		if (apresentarNota13VisaoAluno == null) {
			apresentarNota13VisaoAluno = true;
		}
		return apresentarNota13VisaoAluno;
	}

	public void setApresentarNota13VisaoAluno(Boolean apresentarNota13VisaoAluno) {
		this.apresentarNota13VisaoAluno = apresentarNota13VisaoAluno;
	}

	public Boolean getApresentarNota14VisaoAluno() {
		if (apresentarNota14VisaoAluno == null) {
			apresentarNota14VisaoAluno = true;
		}
		return apresentarNota14VisaoAluno;
	}

	public void setApresentarNota14VisaoAluno(Boolean apresentarNota14VisaoAluno) {
		this.apresentarNota14VisaoAluno = apresentarNota14VisaoAluno;
	}

	public Boolean getApresentarNota15VisaoAluno() {
		if (apresentarNota15VisaoAluno == null) {
			apresentarNota15VisaoAluno = true;
		}
		return apresentarNota15VisaoAluno;
	}

	public void setApresentarNota15VisaoAluno(Boolean apresentarNota15VisaoAluno) {
		this.apresentarNota15VisaoAluno = apresentarNota15VisaoAluno;
	}

	public Boolean getApresentarNota16VisaoAluno() {
		if (apresentarNota16VisaoAluno == null) {
			apresentarNota16VisaoAluno = true;
		}
		return apresentarNota16VisaoAluno;
	}

	public void setApresentarNota16VisaoAluno(Boolean apresentarNota16VisaoAluno) {
		this.apresentarNota16VisaoAluno = apresentarNota16VisaoAluno;
	}

	public Boolean getApresentarNota17VisaoAluno() {
		if (apresentarNota17VisaoAluno == null) {
			apresentarNota17VisaoAluno = true;
		}
		return apresentarNota17VisaoAluno;
	}

	public void setApresentarNota17VisaoAluno(Boolean apresentarNota17VisaoAluno) {
		this.apresentarNota17VisaoAluno = apresentarNota17VisaoAluno;
	}

	public Boolean getApresentarNota18VisaoAluno() {
		if (apresentarNota18VisaoAluno == null) {
			apresentarNota18VisaoAluno = true;
		}
		return apresentarNota18VisaoAluno;
	}

	public void setApresentarNota18VisaoAluno(Boolean apresentarNota18VisaoAluno) {
		this.apresentarNota18VisaoAluno = apresentarNota18VisaoAluno;
	}

	public Boolean getApresentarNota19VisaoAluno() {
		if (apresentarNota19VisaoAluno == null) {
			apresentarNota19VisaoAluno = true;
		}
		return apresentarNota19VisaoAluno;
	}

	public void setApresentarNota19VisaoAluno(Boolean apresentarNota19VisaoAluno) {
		this.apresentarNota19VisaoAluno = apresentarNota19VisaoAluno;
	}

	public Boolean getApresentarNota20VisaoAluno() {
		if (apresentarNota20VisaoAluno == null) {
			apresentarNota20VisaoAluno = true;
		}
		return apresentarNota20VisaoAluno;
	}

	public void setApresentarNota20VisaoAluno(Boolean apresentarNota20VisaoAluno) {
		this.apresentarNota20VisaoAluno = apresentarNota20VisaoAluno;
	}

	public Boolean getApresentarNota21VisaoAluno() {
		if (apresentarNota21VisaoAluno == null) {
			apresentarNota21VisaoAluno = true;
		}
		return apresentarNota21VisaoAluno;
	}

	public void setApresentarNota21VisaoAluno(Boolean apresentarNota21VisaoAluno) {
		this.apresentarNota21VisaoAluno = apresentarNota21VisaoAluno;
	}

	public Boolean getApresentarNota22VisaoAluno() {
		if (apresentarNota22VisaoAluno == null) {
			apresentarNota22VisaoAluno = true;
		}
		return apresentarNota22VisaoAluno;
	}

	public void setApresentarNota22VisaoAluno(Boolean apresentarNota22VisaoAluno) {
		this.apresentarNota22VisaoAluno = apresentarNota22VisaoAluno;
	}

	public Boolean getApresentarNota23VisaoAluno() {
		if (apresentarNota23VisaoAluno == null) {
			apresentarNota23VisaoAluno = true;
		}
		return apresentarNota23VisaoAluno;
	}

	public void setApresentarNota23VisaoAluno(Boolean apresentarNota23VisaoAluno) {
		this.apresentarNota23VisaoAluno = apresentarNota23VisaoAluno;
	}

	public Boolean getApresentarNota24VisaoAluno() {
		if (apresentarNota24VisaoAluno == null) {
			apresentarNota24VisaoAluno = true;
		}
		return apresentarNota24VisaoAluno;
	}

	public void setApresentarNota24VisaoAluno(Boolean apresentarNota24VisaoAluno) {
		this.apresentarNota24VisaoAluno = apresentarNota24VisaoAluno;
	}

	public Boolean getApresentarNota25VisaoAluno() {
		if (apresentarNota25VisaoAluno == null) {
			apresentarNota25VisaoAluno = true;
		}
		return apresentarNota25VisaoAluno;
	}

	public void setApresentarNota25VisaoAluno(Boolean apresentarNota25VisaoAluno) {
		this.apresentarNota25VisaoAluno = apresentarNota25VisaoAluno;
	}

	public Boolean getApresentarNota26VisaoAluno() {
		if (apresentarNota26VisaoAluno == null) {
			apresentarNota26VisaoAluno = true;
		}
		return apresentarNota26VisaoAluno;
	}

	public void setApresentarNota26VisaoAluno(Boolean apresentarNota26VisaoAluno) {
		this.apresentarNota26VisaoAluno = apresentarNota26VisaoAluno;
	}

	public Boolean getApresentarNota27VisaoAluno() {
		if (apresentarNota27VisaoAluno == null) {
			apresentarNota27VisaoAluno = true;
		}
		return apresentarNota27VisaoAluno;
	}

	public void setApresentarNota27VisaoAluno(Boolean apresentarNota27VisaoAluno) {
		this.apresentarNota27VisaoAluno = apresentarNota27VisaoAluno;
	}

	public Boolean getApresentarNota28VisaoAluno() {
		if (apresentarNota28VisaoAluno == null) {
			apresentarNota28VisaoAluno = true;
		}
		return apresentarNota28VisaoAluno;
	}

	public void setApresentarNota28VisaoAluno(Boolean apresentarNota28VisaoAluno) {
		this.apresentarNota28VisaoAluno = apresentarNota28VisaoAluno;
	}

	public Boolean getApresentarNota29VisaoAluno() {
		if (apresentarNota29VisaoAluno == null) {
			apresentarNota29VisaoAluno = true;
		}
		return apresentarNota29VisaoAluno;
	}

	public void setApresentarNota29VisaoAluno(Boolean apresentarNota29VisaoAluno) {
		this.apresentarNota29VisaoAluno = apresentarNota29VisaoAluno;
	}

	public Boolean getApresentarNota30VisaoAluno() {
		if (apresentarNota30VisaoAluno == null) {
			apresentarNota30VisaoAluno = true;
		}
		return apresentarNota30VisaoAluno;
	}

	public void setApresentarNota30VisaoAluno(Boolean apresentarNota30VisaoAluno) {
		this.apresentarNota30VisaoAluno = apresentarNota30VisaoAluno;
	}

	public Boolean getApresentarCalculoMediaFinalVisaoAluno() {
		if (apresentarCalculoMediaFinalVisaoAluno == null) {
			apresentarCalculoMediaFinalVisaoAluno = true;
		}
		return apresentarCalculoMediaFinalVisaoAluno;
	}

	public void setApresentarCalculoMediaFinalVisaoAluno(Boolean apresentarCalculoMediaFinalVisaoAluno) {
		this.apresentarCalculoMediaFinalVisaoAluno = apresentarCalculoMediaFinalVisaoAluno;
	}
	
	public boolean getIsApresentarAnoSemestre() {
		return ((getCalendarioPor().equals("CU") && Uteis.isAtributoPreenchido(getUnidadeEnsinoCurso().getCurso().getCodigo())) ||
				(getCalendarioPor().equals("TU") && Uteis.isAtributoPreenchido(getTurma().getCodigo())) ||
				((getCalendarioPor().equals("UE") || getCalendarioPor().equals("PR")) && !getPeriodicidade().isEmpty()));
	}
	
	public boolean getIsApresentarCampoSemestre() {
		return getPeriodicidade().equals("SE") && (
				(Uteis.isAtributoPreenchido(getUnidadeEnsinoCurso().getCurso().getCodigo()) && getUnidadeEnsinoCurso().getCurso().getSemestral()) || 
				(Uteis.isAtributoPreenchido(getTurma().getCodigo()) && getTurma().getSemestral()) ||
				(!Uteis.isAtributoPreenchido(getTurma().getCodigo()) && !Uteis.isAtributoPreenchido(getUnidadeEnsinoCurso().getCurso().getCodigo()))
			); 
	}
	
	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public String getCalendarioPor() {
		if (calendarioPor == null) {
			calendarioPor = "UE";
		}
		return calendarioPor;
	}

	public void setCalendarioPor(String calendarioPor) {
		this.calendarioPor = calendarioPor;
	}
	
	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "";
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public String getPeriodicidadeApresentar() {
        if (periodicidade.equals("AN")) {
            return "Anual";
        }
        if (periodicidade.equals("SE")) {
            return "Semestral";
        }
        return (periodicidade);
	}
	
	public Date getDataInicioNota31() {
		return dataInicioNota31;
	}

	public void setDataInicioNota31(Date dataInicioNota31) {
		this.dataInicioNota31 = dataInicioNota31;
	}

	public Date getDataInicioNota32() {
		return dataInicioNota32;
	}

	public void setDataInicioNota32(Date dataInicioNota32) {
		this.dataInicioNota32 = dataInicioNota32;
	}

	public Date getDataInicioNota33() {
		return dataInicioNota33;
	}

	public void setDataInicioNota33(Date dataInicioNota33) {
		this.dataInicioNota33 = dataInicioNota33;
	}

	public Date getDataInicioNota34() {
		return dataInicioNota34;
	}

	public void setDataInicioNota34(Date dataInicioNota34) {
		this.dataInicioNota34 = dataInicioNota34;
	}

	public Date getDataInicioNota35() {
		return dataInicioNota35;
	}

	public void setDataInicioNota35(Date dataInicioNota35) {
		this.dataInicioNota35 = dataInicioNota35;
	}

	public Date getDataInicioNota36() {
		return dataInicioNota36;
	}

	public void setDataInicioNota36(Date dataInicioNota36) {
		this.dataInicioNota36 = dataInicioNota36;
	}

	public Date getDataInicioNota37() {
		return dataInicioNota37;
	}

	public void setDataInicioNota37(Date dataInicioNota37) {
		this.dataInicioNota37 = dataInicioNota37;
	}

	public Date getDataInicioNota38() {
		return dataInicioNota38;
	}

	public void setDataInicioNota38(Date dataInicioNota38) {
		this.dataInicioNota38 = dataInicioNota38;
	}

	public Date getDataInicioNota39() {
		return dataInicioNota39;
	}

	public void setDataInicioNota39(Date dataInicioNota39) {
		this.dataInicioNota39 = dataInicioNota39;
	}

	public Date getDataInicioNota40() {
		return dataInicioNota40;
	}

	public void setDataInicioNota40(Date dataInicioNota40) {
		this.dataInicioNota40 = dataInicioNota40;
	}

	public Date getDataTerminoNota31() {
		return dataTerminoNota31;
	}

	public void setDataTerminoNota31(Date dataTerminoNota31) {
		this.dataTerminoNota31 = dataTerminoNota31;
	}

	public Date getDataTerminoNota32() {
		return dataTerminoNota32;
	}

	public void setDataTerminoNota32(Date dataTerminoNota32) {
		this.dataTerminoNota32 = dataTerminoNota32;
	}

	public Date getDataTerminoNota33() {
		return dataTerminoNota33;
	}

	public void setDataTerminoNota33(Date dataTerminoNota33) {
		this.dataTerminoNota33 = dataTerminoNota33;
	}

	public Date getDataTerminoNota34() {
		return dataTerminoNota34;
	}

	public void setDataTerminoNota34(Date dataTerminoNota34) {
		this.dataTerminoNota34 = dataTerminoNota34;
	}

	public Date getDataTerminoNota35() {
		return dataTerminoNota35;
	}

	public void setDataTerminoNota35(Date dataTerminoNota35) {
		this.dataTerminoNota35 = dataTerminoNota35;
	}

	public Date getDataTerminoNota36() {
		return dataTerminoNota36;
	}

	public void setDataTerminoNota36(Date dataTerminoNota36) {
		this.dataTerminoNota36 = dataTerminoNota36;
	}

	public Date getDataTerminoNota37() {
		return dataTerminoNota37;
	}

	public void setDataTerminoNota37(Date dataTerminoNota37) {
		this.dataTerminoNota37 = dataTerminoNota37;
	}

	public Date getDataTerminoNota38() {
		return dataTerminoNota38;
	}

	public void setDataTerminoNota38(Date dataTerminoNota38) {
		this.dataTerminoNota38 = dataTerminoNota38;
	}

	public Date getDataTerminoNota39() {
		return dataTerminoNota39;
	}

	public void setDataTerminoNota39(Date dataTerminoNota39) {
		this.dataTerminoNota39 = dataTerminoNota39;
	}

	public Date getDataTerminoNota40() {
		return dataTerminoNota40;
	}

	public void setDataTerminoNota40(Date dataTerminoNota40) {
		this.dataTerminoNota40 = dataTerminoNota40;
	}

	public Date getDataLiberacaoAlunoNota31() {
		return dataLiberacaoAlunoNota31;
	}

	public void setDataLiberacaoAlunoNota31(Date dataLiberacaoAlunoNota31) {
		this.dataLiberacaoAlunoNota31 = dataLiberacaoAlunoNota31;
	}

	public Date getDataLiberacaoAlunoNota32() {
		return dataLiberacaoAlunoNota32;
	}

	public void setDataLiberacaoAlunoNota32(Date dataLiberacaoAlunoNota32) {
		this.dataLiberacaoAlunoNota32 = dataLiberacaoAlunoNota32;
	}

	public Date getDataLiberacaoAlunoNota33() {
		return dataLiberacaoAlunoNota33;
	}

	public void setDataLiberacaoAlunoNota33(Date dataLiberacaoAlunoNota33) {
		this.dataLiberacaoAlunoNota33 = dataLiberacaoAlunoNota33;
	}

	public Date getDataLiberacaoAlunoNota34() {
		return dataLiberacaoAlunoNota34;
	}

	public void setDataLiberacaoAlunoNota34(Date dataLiberacaoAlunoNota34) {
		this.dataLiberacaoAlunoNota34 = dataLiberacaoAlunoNota34;
	}

	public Date getDataLiberacaoAlunoNota35() {
		return dataLiberacaoAlunoNota35;
	}

	public void setDataLiberacaoAlunoNota35(Date dataLiberacaoAlunoNota35) {
		this.dataLiberacaoAlunoNota35 = dataLiberacaoAlunoNota35;
	}

	public Date getDataLiberacaoAlunoNota36() {
		return dataLiberacaoAlunoNota36;
	}

	public void setDataLiberacaoAlunoNota36(Date dataLiberacaoAlunoNota36) {
		this.dataLiberacaoAlunoNota36 = dataLiberacaoAlunoNota36;
	}

	public Date getDataLiberacaoAlunoNota37() {
		return dataLiberacaoAlunoNota37;
	}

	public void setDataLiberacaoAlunoNota37(Date dataLiberacaoAlunoNota37) {
		this.dataLiberacaoAlunoNota37 = dataLiberacaoAlunoNota37;
	}

	public Date getDataLiberacaoAlunoNota38() {
		return dataLiberacaoAlunoNota38;
	}

	public void setDataLiberacaoAlunoNota38(Date dataLiberacaoAlunoNota38) {
		this.dataLiberacaoAlunoNota38 = dataLiberacaoAlunoNota38;
	}

	public Date getDataLiberacaoAlunoNota39() {
		return dataLiberacaoAlunoNota39;
	}

	public void setDataLiberacaoAlunoNota39(Date dataLiberacaoAlunoNota39) {
		this.dataLiberacaoAlunoNota39 = dataLiberacaoAlunoNota39;
	}

	public Date getDataLiberacaoAlunoNota40() {
		return dataLiberacaoAlunoNota40;
	}

	public void setDataLiberacaoAlunoNota40(Date dataLiberacaoAlunoNota40) {
		this.dataLiberacaoAlunoNota40 = dataLiberacaoAlunoNota40;
	}

	public Boolean getApresentarNota31VisaoAluno() {
		if (apresentarNota31VisaoAluno == null) {
			apresentarNota31VisaoAluno = true;
		}
		return apresentarNota31VisaoAluno;
	}

	public void setApresentarNota31VisaoAluno(Boolean apresentarNota31VisaoAluno) {
		this.apresentarNota31VisaoAluno = apresentarNota31VisaoAluno;
	}

	public Boolean getApresentarNota32VisaoAluno() {
		if (apresentarNota32VisaoAluno == null) {
			apresentarNota32VisaoAluno = true;
		}
		return apresentarNota32VisaoAluno;
	}

	public void setApresentarNota32VisaoAluno(Boolean apresentarNota32VisaoAluno) {
		this.apresentarNota32VisaoAluno = apresentarNota32VisaoAluno;
	}

	public Boolean getApresentarNota33VisaoAluno() {
		if (apresentarNota33VisaoAluno == null) {
			apresentarNota33VisaoAluno = true;
		}
		return apresentarNota33VisaoAluno;
	}

	public void setApresentarNota33VisaoAluno(Boolean apresentarNota33VisaoAluno) {
		this.apresentarNota33VisaoAluno = apresentarNota33VisaoAluno;
	}

	public Boolean getApresentarNota34VisaoAluno() {
		if (apresentarNota34VisaoAluno == null) {
			apresentarNota34VisaoAluno = true;
		}
		return apresentarNota34VisaoAluno;
	}

	public void setApresentarNota34VisaoAluno(Boolean apresentarNota34VisaoAluno) {
		this.apresentarNota34VisaoAluno = apresentarNota34VisaoAluno;
	}

	public Boolean getApresentarNota35VisaoAluno() {
		if (apresentarNota35VisaoAluno == null) {
			apresentarNota35VisaoAluno = true;
		}
		return apresentarNota35VisaoAluno;
	}

	public void setApresentarNota35VisaoAluno(Boolean apresentarNota35VisaoAluno) {
		this.apresentarNota35VisaoAluno = apresentarNota35VisaoAluno;
	}

	public Boolean getApresentarNota36VisaoAluno() {
		if (apresentarNota36VisaoAluno == null) {
			apresentarNota36VisaoAluno = true;
		}
		return apresentarNota36VisaoAluno;
	}

	public void setApresentarNota36VisaoAluno(Boolean apresentarNota36VisaoAluno) {
		this.apresentarNota36VisaoAluno = apresentarNota36VisaoAluno;
	}

	public Boolean getApresentarNota37VisaoAluno() {
		if (apresentarNota37VisaoAluno == null) {
			apresentarNota37VisaoAluno = true;
		}
		return apresentarNota37VisaoAluno;
	}

	public void setApresentarNota37VisaoAluno(Boolean apresentarNota37VisaoAluno) {
		this.apresentarNota37VisaoAluno = apresentarNota37VisaoAluno;
	}

	public Boolean getApresentarNota38VisaoAluno() {
		if (apresentarNota38VisaoAluno == null) {
			apresentarNota38VisaoAluno = true;
		}
		return apresentarNota38VisaoAluno;
	}

	public void setApresentarNota38VisaoAluno(Boolean apresentarNota38VisaoAluno) {
		this.apresentarNota38VisaoAluno = apresentarNota38VisaoAluno;
	}

	public Boolean getApresentarNota39VisaoAluno() {
		if (apresentarNota39VisaoAluno == null) {
			apresentarNota39VisaoAluno = true;
		}
		return apresentarNota39VisaoAluno;
	}

	public void setApresentarNota39VisaoAluno(Boolean apresentarNota39VisaoAluno) {
		this.apresentarNota39VisaoAluno = apresentarNota39VisaoAluno;
	}

	public Boolean getApresentarNota40VisaoAluno() {
		if (apresentarNota40VisaoAluno == null) {
			apresentarNota40VisaoAluno = true;
		}
		return apresentarNota40VisaoAluno;
	}

	public void setApresentarNota40VisaoAluno(Boolean apresentarNota40VisaoAluno) {
		this.apresentarNota40VisaoAluno = apresentarNota40VisaoAluno;
	}

	public Boolean getAtualizarCalendarioAtividadeMatriculaComPeriodo() {
		if(atualizarCalendarioAtividadeMatriculaComPeriodo == null) {
			atualizarCalendarioAtividadeMatriculaComPeriodo = false ;
		}
		return atualizarCalendarioAtividadeMatriculaComPeriodo;
	}

	public void setAtualizarCalendarioAtividadeMatriculaComPeriodo(
			Boolean atualizarCalendarioAtividadeMatriculaComPeriodo) {
		this.atualizarCalendarioAtividadeMatriculaComPeriodo = atualizarCalendarioAtividadeMatriculaComPeriodo;
	}

	public Date getCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde() {
		return calcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde;
	}

	public void setCalcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde(
			Date calcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde) {
		this.calcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde = calcularMediaAutomaticamenteGravarLancamentoNotasProfessorPartirde;
	}

	public PessoaVO getProfessorExclusivoLancamentoDeNota() {
		if (professorExclusivoLancamentoDeNota == null) {
			professorExclusivoLancamentoDeNota = new PessoaVO();
		}
		return professorExclusivoLancamentoDeNota;
	}

	public void setProfessorExclusivoLancamentoDeNota(PessoaVO professorExclusivoLancamentoDeNota) {
		this.professorExclusivoLancamentoDeNota = professorExclusivoLancamentoDeNota;
	}
	
	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}
	
	 public void setPessoa(PessoaVO pessoa) {
	        this.pessoa = pessoa;
    }
}
