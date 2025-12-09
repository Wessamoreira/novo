package relatorio.negocio.comuns.academico;


import java.util.Date;

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoHistorico;

/**
 * 
 * @author Diego
 */
public class HistoricoAlunoDisciplinaRelVO {

	private HistoricoVO historicoVO;
	private String nomeDisciplina;
	private Integer codigoDisciplina;
	private Integer ordem;
	private String anoSemstre;
	private String chDisciplina;
	private String mediaFinal;
	private String situacaoFinal;
	private String nomePeriodo;
	private Integer numeroPeriodoLetivo;
	private String professor;
	private String titulacaoProfessor;
	private Double freqDisciplina;
	private String frequencia;
	private Boolean apresentarFrequencia;
	private String situacaoPeriodo;
	private String instituicao;
	private String cidade;
	private String estado;
	private Integer cargaHorariaCursada;
	private Boolean diversificada;
	private Integer totalDiaLetivoAno;
	private Integer totalFalta;
	private String crDisciplina;
	private Boolean utilizaMediaConceito;
	private Double mediaFinalCalculo;
	private String situacaoMatriculaPeriodo;
	private Boolean disciplinaForaGrade;
	private Boolean disciplinaAtividadeComplementar;
	private String observacao;
	private Boolean disciplinaDependencia;
	private Boolean disciplinaAdaptacao;
	private TipoHistorico tipoHistorico;
	private String situacaoHistorico;	
	private String periodoDisciplinaCursado;
	private Boolean disciplinaEstagio;
	private String siglaTitulacaoProfessor;
	private String siglaProfessor;
	private Integer nrCreditos;
	private String abreviaturaDisciplina;
	private Double coeficienteRendimento;
	private String casasDecimaisCoeficienteRendimento;
	private Boolean disciplinaReferenteAUmGrupoOptativa;
	private GradeCurricularVO gradeCurricularVO;
	private Integer cargaHorariaGradeDisciplina;
	private Integer cargaHorariaPratica;
	private Integer cargaHorariaTeorica;
    private Boolean historicoDisciplinaOptativa;
	private Boolean gradeAtualAluno;
	private String notaConceito;
	private String situacaoHistoricoApresentarAdaptacaoMatriz;
	private String professoresDisciplinas;
    private Date dataInicioAula;
    private Date dataFimAula;
    private String bimestreAnoConclusao;
    private Boolean disciplinaTcc;
	private String tituloMonografia;
    private String orientadorMonografia;
    private String titulacaoOrientadorMonografia;
	private SituacaoHistorico situacaoHistoricoEnum;
	private SituacaoMatriculaPeriodoEnum situacaoFinalEnum;
	private SituacaoHistorico situacaoFinalHistoricoEnum;
	private SituacaoHistorico situacaoPeriodoEnum;
	private PeriodoLetivoVO periodoLetivo;
	private Boolean historicoAprovado;
	private String nomeDisciplinaEquivalente;
	private Boolean apresentarDisciplinasEquivalentes;

	public String getOrdenacaoDisciplinaPeriodo() {
		return "P" + getNomePeriodo() + "D" + getNomeDisciplina();
	}

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}	

	public String getAnoSemstre() {
		if(anoSemstre == null){
			anoSemstre = "";
		}
		return anoSemstre;
	}

	public void setAnoSemstre(String anoSemstre) {
		this.anoSemstre = anoSemstre;
	}

	public String getChDisciplina() {
		if(chDisciplina == null ) {
			chDisciplina = "";
		}
		return chDisciplina;
	}

	public String getCargaHorariaCursada() {		
		if (cargaHorariaCursada == null || cargaHorariaCursada == 0) {
			if (chDisciplina != null && !chDisciplina.trim().isEmpty() && freqDisciplina != null) {
				try {
					return Integer.valueOf(Double.valueOf(Uteis.arredondar(Double.valueOf((Integer.valueOf(chDisciplina) * freqDisciplina) / 100), 0, 0)).intValue()).toString();
				} catch (Exception e) {
					return "--";
				}
			}
			return "--";
		}
		return cargaHorariaCursada.toString();
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}

	public void setChDisciplina(String chDisciplina) {
		this.chDisciplina = chDisciplina;
	}

	public String getMediaFinal() {
		return mediaFinal;
	}

	public void setMediaFinal(String mediaFinal) {
		this.mediaFinal = mediaFinal;
	}

	public String getSituacaoFinal() {
		return situacaoFinal;
	}

	public void setSituacaoFinal(String situacaoFinal) {
		this.situacaoFinal = situacaoFinal;
	}

	public String getNomePeriodo() {
		if(nomePeriodo == null){
			nomePeriodo = "";
		}
		return nomePeriodo;
	}

	public void setNomePeriodo(String nomePeriodo) {
		this.nomePeriodo = nomePeriodo;
	}

	/**
	 * @return the professor
	 */
	public String getProfessor() {
		if (professor == null) {
			professor = "";
		}
		return professor;
	}

	/**
	 * @param professor
	 *            the professor to set
	 */
	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public void setTitulacaoProfessor(String titulacaoProfessor) {
		this.titulacaoProfessor = titulacaoProfessor;
	}

	public String getTitulacaoProfessor() {
		if (titulacaoProfessor == null) {
			titulacaoProfessor = "";
		}
		return titulacaoProfessor;
	}

	public void setFreqDisciplina(Double freqDisciplina) {
		this.freqDisciplina = freqDisciplina;
	}

	public Double getFreqDisciplina() {
		if (freqDisciplina == null) {
			freqDisciplina = 0.0;
		}
		return freqDisciplina;
	}

	public String getFrequencia() {
		if (frequencia == null) {
			frequencia = "";
		}
		return frequencia;
	}

	public void setFrequencia(String frequencia) {
		this.frequencia = frequencia;
	}

	public Boolean getApresentarFrequencia() {
		if (apresentarFrequencia == null) {
			apresentarFrequencia = Boolean.FALSE;
		}
		return apresentarFrequencia;
	}

	public void setApresentarFrequencia(Boolean apresentarFrequencia) {
		this.apresentarFrequencia = apresentarFrequencia;
	}

	public String getSituacaoPeriodo() {
		if (situacaoPeriodo == null) {
			situacaoPeriodo = "";
		}
		return situacaoPeriodo;
	}

	public void setSituacaoPeriodo(String situacaoPeriodo) {
		this.situacaoPeriodo = situacaoPeriodo;
	}

	public String getInstituicao() {
		if (instituicao == null) {
			instituicao = "";
		}
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		if (estado == null) {
			estado = "";
		}
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getOrdenarAnoSemestreSituacaoPeriodo() {
		return getAnoSemstre() + getSituacaoPeriodo() + getNomePeriodo();
	}

	public String getOrdenarAnoSemestrePeriodoSituacao() {
		return getAnoSemstre() + getNomePeriodo() + getSituacaoPeriodo();
	}

	public String getOrdenarAnoSemestre() {
		return getAnoSemstre();
	}

	public Boolean getDiversificada() {
		if (diversificada == null) {
			diversificada = Boolean.FALSE;
		}
		return diversificada;
	}

	public void setDiversificada(Boolean diversificada) {
		this.diversificada = diversificada;
	}

	public Integer getTotalDiaLetivoAno() {
		if (totalDiaLetivoAno == null) {
			totalDiaLetivoAno = 0;
		}
		return totalDiaLetivoAno;
	}

	public void setTotalDiaLetivoAno(Integer totalDiaLetivoAno) {
		this.totalDiaLetivoAno = totalDiaLetivoAno;
	}

	public String getCrDisciplina() {
		if (crDisciplina == null) {
			crDisciplina = "";
		}
		return crDisciplina;
	}

	public void setCrDisciplina(String crDisciplina) {
		this.crDisciplina = crDisciplina;
	}

	public Boolean getUtilizaMediaConceito() {
		if (utilizaMediaConceito == null) {
			utilizaMediaConceito = Boolean.FALSE;
		}
		return utilizaMediaConceito;
	}

	public void setUtilizaMediaConceito(Boolean utilizaMediaConceito) {
		this.utilizaMediaConceito = utilizaMediaConceito;
	}

	public Double getMediaFinalCalculo() {
		if (mediaFinalCalculo == null) {
			mediaFinalCalculo = 0.0;
		}
		return mediaFinalCalculo;
	}

	public void setMediaFinalCalculo(Double mediaFinalCalculo) {
		this.mediaFinalCalculo = mediaFinalCalculo;
	}

	public String getSituacaoMatriculaPeriodo() {
		if (situacaoMatriculaPeriodo == null) {
			situacaoMatriculaPeriodo = "";
		}
		return situacaoMatriculaPeriodo;
	}

	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}

	public Boolean getDisciplinaForaGrade() {
		if (disciplinaForaGrade == null) {
			disciplinaForaGrade = false;
		}
		return disciplinaForaGrade;
	}

	public void setDisciplinaForaGrade(Boolean disciplinaForaGrade) {
		this.disciplinaForaGrade = disciplinaForaGrade;
	}

	public Boolean getDisciplinaAtividadeComplementar() {
		if (disciplinaAtividadeComplementar == null) {
			disciplinaAtividadeComplementar = false;
		}
		return disciplinaAtividadeComplementar;
	}

	public void setDisciplinaAtividadeComplementar(Boolean disciplinaAtividadeComplementar) {
		this.disciplinaAtividadeComplementar = disciplinaAtividadeComplementar;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Integer getNumeroPeriodoLetivo() {
		if (numeroPeriodoLetivo == null) {
			numeroPeriodoLetivo = 0;
		}
		return numeroPeriodoLetivo;
	}

	public void setNumeroPeriodoLetivo(Integer numeroPeriodoLetivo) {
		this.numeroPeriodoLetivo = numeroPeriodoLetivo;
	}

	public Boolean getDisciplinaDependencia() {
		if (disciplinaDependencia == null) {
			disciplinaDependencia = false;
		}
		return disciplinaDependencia;
	}

	public void setDisciplinaDependencia(Boolean disciplinaDependencia) {
		this.disciplinaDependencia = disciplinaDependencia;
	}

	public Integer getTotalFalta() {
		if (totalFalta == null) {
			totalFalta = 0;
		}
		return totalFalta;
	}

	public void setTotalFalta(Integer totalFalta) {
		this.totalFalta = totalFalta;
	}

	public Boolean getDisciplinaAdaptacao() {
		if (disciplinaAdaptacao == null) {
			disciplinaAdaptacao = false;
		}
		return disciplinaAdaptacao;
	}

	public void setDisciplinaAdaptacao(Boolean disciplinaAdaptacao) {
		this.disciplinaAdaptacao = disciplinaAdaptacao;
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

	public TipoHistorico getTipoHistorico() {
		if (tipoHistorico == null) {
			tipoHistorico = TipoHistorico.NORMAL;
		}
		return tipoHistorico;
	}

	public void setTipoHistorico(TipoHistorico tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}
	
	
	public Boolean getTipoHistoricoComplementacaoCargaHoraria(){
		return getTipoHistorico().equals(TipoHistorico.COMPLEMENTACAO_CARGA_HORARIA);
	}

	public String getSituacaoHistorico() {
		if (situacaoHistorico == null) {
			situacaoHistorico = "";
		}
		return situacaoHistorico;
	}

	public void setSituacaoHistorico(String situacaoHistorico) {
		this.situacaoHistorico = situacaoHistorico;
	}
	
	public String getPeriodoDisciplinaCursado() {
		if(periodoDisciplinaCursado == null){
			periodoDisciplinaCursado = "";
		}
		return periodoDisciplinaCursado;
	}

	public void setPeriodoDisciplinaCursado(String periodoDisciplinaCursado) {
		this.periodoDisciplinaCursado = periodoDisciplinaCursado;
	}
	
	/*public String getSiglaProfessor() {
		if (Uteis.isAtributoPreenchido(getSiglaTitulacaoProfessor())) {
			switch (getSiglaTitulacaoProfessor().trim()) {
			case "MS":				
				return "Me";
			case "ME":				
				return "Me";
			case "PD":				
				return "PhD";
			case "GRA":				
				return "Grad";
			case "GR":				
				return "Grad";
			case "EP":				
				return "Esp";
			case "ES":				
				return "Es";
			case "DR":				
				return "Dr";
			case "DRA":				
				return "Dra";
			case "Graduada /Especialista":				
				return "Grad/Esp";
			default:
				break;
			}
		}

		return getTitulacaoProfessor();
	}*/

	public String getSiglaProfessor() {
		if (siglaProfessor == null) {
			siglaProfessor = "";
		}
		return siglaProfessor;
	}

	public void setSiglaProfessor(String siglaProfessor) {
		this.siglaProfessor = siglaProfessor;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 99;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public Boolean getDisciplinaEstagio() {
		if (disciplinaEstagio == null) {
			disciplinaEstagio = Boolean.FALSE;
		}
		return disciplinaEstagio;
	}

	public void setDisciplinaEstagio(Boolean disciplinaEstagio) {
		this.disciplinaEstagio = disciplinaEstagio;
	}
	
	/**
	 * @return the siglaTitulacaoProfessor
	 */
	public String getSiglaTitulacaoProfessor() {
		if (siglaTitulacaoProfessor == null) {
			siglaTitulacaoProfessor = "";
		}
		return siglaTitulacaoProfessor;
	}

	/**
	 * @param siglaTitulacaoProfessor the siglaTitulacaoProfessor to set
	 */
	public void setSiglaTitulacaoProfessor(String siglaTitulacaoProfessor) {
		this.siglaTitulacaoProfessor = siglaTitulacaoProfessor;
	}

	public Integer getNrCreditos() {
		if (nrCreditos == null) {
			nrCreditos = 0;
		}
		return nrCreditos;
	}

	public void setNrCreditos(Integer nrCreditos) {
		this.nrCreditos = nrCreditos;
	}

	public String getAgrupadorDisciplinaNota() {
		return getCodigoDisciplina().toString() + getOrdem();
	}
	
	public String getAno() {
		return getAnoSemstre().contains("/") ? getAnoSemstre().substring(0, getAnoSemstre().indexOf("/")) : getAnoSemstre();
}
	
	public String getSemestre() {
		return getAnoSemstre().contains("/") ? getAnoSemstre().substring(getAnoSemstre().indexOf("/")+1, getAnoSemstre().length()) : "";
	}
	
	public String getAbreviaturaDisciplina() {
		if (abreviaturaDisciplina == null) {
			abreviaturaDisciplina = "";
		}
		return abreviaturaDisciplina;
	}

	public void setAbreviaturaDisciplina(String abreviaturaDisciplina) {
		this.abreviaturaDisciplina = abreviaturaDisciplina;
	}

	public Double getCoeficienteRendimento() {
		if (coeficienteRendimento == null) {
			coeficienteRendimento = 0.0;
		}
		return coeficienteRendimento;
	}

	public void setCoeficienteRendimento(Double coeficienteRendimento) {
		this.coeficienteRendimento = coeficienteRendimento;
	}
	
	public String getCasasDecimaisCoeficienteRendimento() {
		if (casasDecimaisCoeficienteRendimento == null) {
			casasDecimaisCoeficienteRendimento = "";
		}
		return casasDecimaisCoeficienteRendimento;
	}

	public void setCasasDecimaisCoeficienteRendimento(String casasDecimaisCoeficienteRendimento) {
		this.casasDecimaisCoeficienteRendimento = casasDecimaisCoeficienteRendimento;
	}

	public GradeCurricularVO getGradeCurricularVO() {
		if(gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}
		
	public String getOrdenacaoAnoSemestreGradeCurricular() {
		return getAnoSemstre() + "/" + getGradeCurricularVO().getCodigo();
	}
			
	public Integer getCargaHorariaGradeDisciplina() {
		if (cargaHorariaGradeDisciplina == null) {
			cargaHorariaGradeDisciplina = 0;
		}
		return cargaHorariaGradeDisciplina;
	}

	public void setCargaHorariaGradeDisciplina(Integer cargaHorariaGradeDisciplina) {
		this.cargaHorariaGradeDisciplina = cargaHorariaGradeDisciplina;
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

	public Boolean getDisciplinaReferenteAUmGrupoOptativa() {
		if(disciplinaReferenteAUmGrupoOptativa == null) {
			disciplinaReferenteAUmGrupoOptativa = Boolean.FALSE;
		}
		return disciplinaReferenteAUmGrupoOptativa;
	}

	public void setDisciplinaReferenteAUmGrupoOptativa(Boolean disciplinaReferenteAUmGrupoOptativa) {
		this.disciplinaReferenteAUmGrupoOptativa = disciplinaReferenteAUmGrupoOptativa;
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
	public Boolean getHistoricoDisciplinaOptativa() {
		if (historicoDisciplinaOptativa == null) {
			historicoDisciplinaOptativa = false;
		}
		return historicoDisciplinaOptativa;
	}

	public void setHistoricoDisciplinaOptativa(Boolean historicoDisciplinaOptativa) {
		this.historicoDisciplinaOptativa = historicoDisciplinaOptativa;
	}
	
	public Boolean getGradeAtualAluno() {
		if (gradeAtualAluno == null) {
			gradeAtualAluno = Boolean.TRUE;
		}
		return gradeAtualAluno;
	}

	public void setGradeAtualAluno(Boolean gradeAtualAluno) {
		this.gradeAtualAluno = gradeAtualAluno;
	}
	
	public void setSituacaoHistoricoApresentarAdaptacaoMatriz(String situacaoHistoricoApresentarAdaptacaoMatriz) {
		this.situacaoHistoricoApresentarAdaptacaoMatriz = situacaoHistoricoApresentarAdaptacaoMatriz;
	}
	
	public String getSituacaoHistoricoApresentarAdaptacaoMatriz() {
		if(situacaoHistoricoApresentarAdaptacaoMatriz == null) {
			situacaoHistoricoApresentarAdaptacaoMatriz= SituacaoHistorico.getDescricao(getSituacaoHistorico());
		}
        return situacaoHistoricoApresentarAdaptacaoMatriz;
	}

	public String getNotaConceito() {
		if (notaConceito == null) {
			notaConceito = "";
		}
		return notaConceito;
	}

	public void setNotaConceito(String notaConceito) {
		this.notaConceito = notaConceito;
	}

	public String getProfessoresDisciplinas() {
		if(professoresDisciplinas == null) {
			professoresDisciplinas= "";
		}
		return professoresDisciplinas;
	}

	public void setProfessoresDisciplinas(String professoresDisciplinas) {
		this.professoresDisciplinas = professoresDisciplinas;
	}	

	public HistoricoVO getHistoricoVO() {
		if(historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public SituacaoHistorico getSituacaoHistoricoEnum() {		
		return situacaoHistoricoEnum;
	}

	public void setSituacaoHistoricoEnum(SituacaoHistorico situacaoHistoricoEnum) {
		this.situacaoHistoricoEnum = situacaoHistoricoEnum;
	}

	public SituacaoMatriculaPeriodoEnum getSituacaoFinalEnum() {
		return situacaoFinalEnum;
	}

	public void setSituacaoFinalEnum(SituacaoMatriculaPeriodoEnum situacaoFinalEnum) {
		this.situacaoFinalEnum = situacaoFinalEnum;
	}

	public SituacaoHistorico getSituacaoPeriodoEnum() {
		return situacaoPeriodoEnum;
	}

	public void setSituacaoPeriodoEnum(SituacaoHistorico situacaoPeriodoEnum) {
		this.situacaoPeriodoEnum = situacaoPeriodoEnum;
	}	
	
	public Date getDataInicioAula() {
		return dataInicioAula;
	}

	public void setDataInicioAula(Date dataInicioAula) {
		this.dataInicioAula = dataInicioAula;
	}

	public Date getDataFimAula() {
		return dataFimAula;
	}

	public void setDataFimAula(Date dataFimAula) {
		this.dataFimAula = dataFimAula;
	}

	public String getBimestreAnoConclusao() {
		if(bimestreAnoConclusao == null) {
			bimestreAnoConclusao = "";
		}
		return bimestreAnoConclusao;
	}

	public void setBimestreAnoConclusao(String bimestreAnoConclusao) {
		this.bimestreAnoConclusao = bimestreAnoConclusao;
	}

	public Boolean getDisciplinaTcc() {
		if(disciplinaTcc == null) {
			disciplinaTcc = false;
		}
		return disciplinaTcc;
	}

	public void setDisciplinaTcc(Boolean disciplinaTcc) {
		this.disciplinaTcc = disciplinaTcc;
	}		
	
	/**
	 * @return the tituloMonografia
	 */
	public String getTituloMonografia() {
		if (tituloMonografia == null) {
			tituloMonografia = "";
		}
		return tituloMonografia;
	}

	/**
	 * @param tituloMonografia
	 *            the tituloMonografia to set
	 */
	public void setTituloMonografia(String tituloMonografia) {
		this.tituloMonografia = tituloMonografia;
	}
	
	public String getOrientadorMonografia() {
		if (orientadorMonografia == null) {
			orientadorMonografia = "";
		}
		return orientadorMonografia;
	}

	public void setOrientadorMonografia(String orientadorMonografia) {
		this.orientadorMonografia = orientadorMonografia;
	}
	
	public String getTitulacaoOrientadorMonografia() {
		if(titulacaoOrientadorMonografia == null) {
			titulacaoOrientadorMonografia = "";
		}
		return titulacaoOrientadorMonografia;
	}

	public void setTitulacaoOrientadorMonografia(String titulacaoOrientadorMonografia) {
		this.titulacaoOrientadorMonografia = titulacaoOrientadorMonografia;
	}
			
	public String getNomeDisciplinaPrimeiraLetraMaiusculo() {
		return Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(getNomeDisciplina()));
	}

	public PeriodoLetivoVO getPeriodoLetivo() {
		if(periodoLetivo == null) {
			periodoLetivo =  new PeriodoLetivoVO();
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(PeriodoLetivoVO periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}
	
	public String getProfessorPrimeiraLetraMaiusculo() {
		return Uteis.alterarpreposicaoParaMinusculo(Uteis.gerarUpperCasePrimeiraLetraDasPalavras(getProfessor()));
	}

	public Boolean getHistoricoAprovado() {
		if(historicoAprovado == null)
			historicoAprovado = Boolean.FALSE;
		return historicoAprovado;
    }

	public SituacaoHistorico getSituacaoFinalHistoricoEnum() {		
		return situacaoFinalHistoricoEnum;
	}

	public void setSituacaoFinalHistoricoEnum(SituacaoHistorico situacaoFinalHistoricoEnum) {
		this.situacaoFinalHistoricoEnum = situacaoFinalHistoricoEnum;
	}
	
	public void setHistoricoAprovado(Boolean historicoAprovado) {
		this.historicoAprovado = historicoAprovado;
	}
	
	public String getNomeDisciplinaEquivalente() {
		if (nomeDisciplinaEquivalente == null) {
			nomeDisciplinaEquivalente = "";
		}
		return nomeDisciplinaEquivalente;
	}

	public void setNomeDisciplinaEquivalente(String nomeDisciplinaEquivalente) {
		this.nomeDisciplinaEquivalente = nomeDisciplinaEquivalente;
	}
	
	public Boolean getApresentarDisciplinasEquivalentes() {
		if (apresentarDisciplinasEquivalentes == null) {
			apresentarDisciplinasEquivalentes = Boolean.FALSE;
		}
		return apresentarDisciplinasEquivalentes;
	}

	public void setApresentarDisciplinasEquivalentes(Boolean apresentarDisciplinasEquivalentes) {
		this.apresentarDisciplinasEquivalentes = apresentarDisciplinasEquivalentes;
	}
	
}
