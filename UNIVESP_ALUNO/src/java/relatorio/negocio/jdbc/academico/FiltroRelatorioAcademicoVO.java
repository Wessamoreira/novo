package relatorio.negocio.jdbc.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.faces.model.SelectItem;

//import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;

public class FiltroRelatorioAcademicoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	public FiltroRelatorioAcademicoVO() {
		try{
			setDataInicio(Uteis.getDataPassada(new Date(), 30));
		}catch(Exception e){
			setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
		}
		setDataTermino(new Date());
	}

	private Integer codigo;
	private Boolean ativo;
	private Boolean trancado;
	private Boolean cancelado;
	private Boolean abandonado;
	private Boolean formado; 
	private Boolean considerarSituacaoAtualMatricula;
	private Boolean transferenciaInterna;
	private Boolean transferenciaExterna;
	private Boolean preMatricula;
	private Boolean preMatriculaCancelada;
	private Boolean concluido;
	private Boolean pendenteFinanceiro;
	private Boolean excluida;
	private Boolean transferidaDe;
	private Boolean transferidaPara;
//	private Boolean matriculaRecebida;
//	private Boolean matriculaAReceber;
	private String ano;
	private String semestre;
	private TipoFiltroPeriodoAcademicoEnum tipoFiltroPeriodoAcademico;
	private Date dataInicio;
	private Date dataTermino;
	private Date dataInicioPeriodoAula;
	private Date dataTerminoPeriodoAula;
	private Boolean trazerApenaAlunoFinantropia;
	private Boolean confirmado;
	
	private Boolean filtrarCursoAnual; 
	private Boolean filtrarCursoSemestral;
	private Boolean filtrarCursoIntegral;
	private Boolean trazerAlunosComTransferenciaMatriz;
	private FormaIngresso formaIngresso;
	private Boolean apresentarSubtotalFormaIngresso;
	private PeriodicidadeEnum periodicidadeEnum;
	private List<SelectItem> listaSelectItemPeriodicidade;
	
	/*
	 * Situacoes Historico 
	 */
	
	private Boolean reprovado; 
	private Boolean aprovado;
	private Boolean naoCursada; 
	private Boolean cursando; 
	private Boolean trancamentoHistorico; 
	private Boolean abandonoHistorico; 
	private Boolean canceladoHistorico; 
	private Boolean transferidoHistorico; 
	private Boolean concessaoCreditoHistorico; 
	private Boolean concessaoCargaHorariaHistorico; 
	private Boolean preMatriculaHistorico; 
	private Boolean jubilado;
	
	
	
	
	

	public Boolean getAtivo() {
		if (ativo == null) {
			ativo = Boolean.TRUE;
		}
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getTrancado() {
		if (trancado == null) {
			trancado = Boolean.FALSE;
		}
		return trancado;
	}

	public void setTrancado(Boolean trancado) {
		this.trancado = trancado;
	}

	public Boolean getCancelado() {
		if (cancelado == null) {
			cancelado = Boolean.FALSE;
		}
		return cancelado;
	}

	public void setCancelado(Boolean cancelado) {
		this.cancelado = cancelado;
	}

	// public Boolean getCanceladaFinanceiro() {
	// if (canceladaFinanceiro == null) {
	// canceladaFinanceiro = Boolean.FALSE;
	// }
	// return canceladaFinanceiro;
	// }
	// public void setCanceladaFinanceiro(Boolean canceladaFinanceiro) {
	// this.canceladaFinanceiro = canceladaFinanceiro;
	// }
	public Boolean getPreMatricula() {
		if (preMatricula == null) {
			preMatricula = Boolean.FALSE;
		}
		return preMatricula;
	}

	public void setPreMatricula(Boolean preMatricula) {
		this.preMatricula = preMatricula;
	}

	public Boolean getPreMatriculaCancelada() {
		if (preMatriculaCancelada == null) {
			preMatriculaCancelada = Boolean.FALSE;
		}
		return preMatriculaCancelada;
	}

	public void setPreMatriculaCancelada(Boolean preMatriculaCancelada) {
		this.preMatriculaCancelada = preMatriculaCancelada;
	}

	public Boolean getConcluido() {
		if (concluido == null) {
			concluido = Boolean.FALSE;
		}
		return concluido;
	}

	public void setConcluido(Boolean concluido) {
		this.concluido = concluido;
	}

	public Boolean getPendenteFinanceiro() {
		if (pendenteFinanceiro == null) {
			pendenteFinanceiro = Boolean.TRUE;
		}
		return pendenteFinanceiro;
	}

	public void setPendenteFinanceiro(Boolean pendenteFinanceiro) {
		this.pendenteFinanceiro = pendenteFinanceiro;
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

//	public Boolean getMatriculaRecebida() {
//		if (matriculaRecebida == null) {
//			matriculaRecebida = true;
//		}
//		return matriculaRecebida;
//	}
//
//	public void setMatriculaRecebida(Boolean matriculaRecebida) {
//		this.matriculaRecebida = matriculaRecebida;
//	}
//
//	public Boolean getMatriculaAReceber() {
//		if (matriculaAReceber == null) {
//			matriculaAReceber = true;
//		}
//		return matriculaAReceber;
//	}

//	public void setMatriculaAReceber(Boolean matriculaAReceber) {
//		this.matriculaAReceber = matriculaAReceber;
//	}

	public Boolean getAbandonado() {
		if (abandonado == null) {
			abandonado = Boolean.FALSE;
		}
		return abandonado;
	}

	public void setAbandonado(Boolean abandonado) {
		this.abandonado = abandonado;
	}

	public Boolean getFormado() {
		if (formado == null) {
			formado = Boolean.FALSE;
		}
		return formado;
	}

	public void setFormado(Boolean formado) {
		this.formado = formado;
	}
	
	public Boolean getConsiderarSituacaoAtualMatricula() {
		if (considerarSituacaoAtualMatricula == null) {
			considerarSituacaoAtualMatricula = Boolean.FALSE;
		}
		return considerarSituacaoAtualMatricula;
	}

	public void setConsiderarSituacaoAtualMatricula(Boolean considerarSituacaoAtualMatricula) {
		this.considerarSituacaoAtualMatricula = considerarSituacaoAtualMatricula;
	}

	public Boolean getTransferenciaInterna() {
		if (transferenciaInterna == null) {
			transferenciaInterna = false;
		}
		return transferenciaInterna;
	}

	public void setTransferenciaInterna(Boolean transferenciaInterna) {
		this.transferenciaInterna = transferenciaInterna;
	}

	public Boolean getTransferenciaExterna() {
		if (transferenciaExterna == null) {
			transferenciaExterna = false;
		}
		return transferenciaExterna;
	}

	public void setTransferenciaExterna(Boolean transferenciaExterna) {
		this.transferenciaExterna = transferenciaExterna;
	}

	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public TipoFiltroPeriodoAcademicoEnum getTipoFiltroPeriodoAcademico() {
		if (tipoFiltroPeriodoAcademico == null) {
			tipoFiltroPeriodoAcademico = TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE;
		}
		return tipoFiltroPeriodoAcademico;
	}

	public void setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum tipoFiltroPeriodoAcademico) {
		this.tipoFiltroPeriodoAcademico = tipoFiltroPeriodoAcademico;
	}

	public Date getDataInicio() {
//		if (dataInicio == null) {
//			try {
//				dataInicio = ;
//			} catch (Exception e) {
//				dataInicio = new Date();
//			}
//		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {

		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}
	
	public Date getDataInicioPeriodoAula() {
		return dataInicioPeriodoAula;
	}

	public void setDataInicioPeriodoAula(Date dataInicioPeriodoAula) {
		this.dataInicioPeriodoAula = dataInicioPeriodoAula;
	}

	public Date getDataTerminoPeriodoAula() {
		return dataTerminoPeriodoAula;
	}

	public void setDataTerminoPeriodoAula(Date dataTerminoPeriodoAula) {
		this.dataTerminoPeriodoAula = dataTerminoPeriodoAula;
	}
	
	

	public boolean getFiltrarPorPeriodoData() {
		return getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.AMBOS) 
				|| getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA)
				|| getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO_PERIODO_DATA);
	}

	public boolean getFiltrarPorAnoSemestre() {
		return getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.AMBOS) || getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE);
	}
	
	public boolean getFiltrarPorAno() {
		return getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO) || getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO_PERIODO_DATA);
	}
	
	public String getFiltroAcademicoUtilizado(){
		StringBuilder filtros = new StringBuilder("");
		if(getAtivo()){
			filtros.append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_ativo"));
		}
		if(getConcluido()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_concluido"));
		}
		if(getFormado()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_formado"));
		}
		if(getConsiderarSituacaoAtualMatricula()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_considerarSituacaoAtualMatricula"));
		}
		if(getPreMatricula()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_preMatricula"));
		}
		if(getPreMatriculaCancelada()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_preMatriculaCancelada"));
		}
		if(getTrancado()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_trancado"));
		}
		if(getCancelado()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_cancelado"));
		}
		if(getAbandonado()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_abandonoCurso"));
		}
		if(getTransferenciaInterna()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_transferenciaInterna"));
		}
		if(getTransferenciaExterna()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_transferenciaSaida"));
		}
		if(getJubilado()) {
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_jubilado"));
		}
		return filtros.toString();
	}
	
	public String getFiltroSituacaoMatriculaUtilizado(){
		StringBuilder filtros = new StringBuilder("");
		if(getConfirmado()){
			filtros.append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_confirmado"));
		}
		if(getPendenteFinanceiro()){
			filtros.append(filtros.length()>0?", ":"").append(UteisJSF.internacionalizar("prt_FiltroRelatorioAcademico_pendenteFinanceiro"));
		}		
		return filtros.toString();
	}
	
	public void realizarMarcarTodasSituacoes(){
		realizarSelecionarTodosSituacoes(true);
	}
	
	public void realizarDesmarcarTodasSituacoes(){
		realizarSelecionarTodosSituacoes(false);
	}
	
	public void realizarSelecionarTodosSituacoes(boolean selecionado){
		setAbandonado(selecionado);
		setAtivo(selecionado);
		setCancelado(selecionado);
		setConcluido(selecionado);
		setFormado(selecionado);
		setPreMatricula(selecionado);
		setPreMatriculaCancelada(selecionado);
		setTrancado(selecionado);
		setTransferenciaExterna(selecionado);
		setTransferenciaInterna(selecionado);	
		setExcluida(selecionado);
		setTransferidaDe(selecionado);
		setTransferidaPara(selecionado);
		setJubilado(selecionado);
		setConsiderarSituacaoAtualMatricula(selecionado);
	}

	public Boolean getTrazerApenaAlunoFinantropia() {
		if (trazerApenaAlunoFinantropia == null) {
			trazerApenaAlunoFinantropia = false;
		}
		return trazerApenaAlunoFinantropia;
	}

	public void setTrazerApenaAlunoFinantropia(Boolean trazerApenaAlunoFinantropia) {
		this.trazerApenaAlunoFinantropia = trazerApenaAlunoFinantropia;
	}

	public Boolean getConfirmado() {
		if (confirmado == null) {
			confirmado = true;
		}
		return confirmado;
	}

	public void setConfirmado(Boolean confirmado) {
		this.confirmado = confirmado;
	}

	public Boolean getExcluida() {
		return excluida;
	}

	public void setExcluida(Boolean excluida) {
		this.excluida = excluida;
	}

	public Boolean getTransferidaDe() {
		return transferidaDe;
	}

	public void setTransferidaDe(Boolean transferidaDe) {
		this.transferidaDe = transferidaDe;
	}

	public Boolean getTransferidaPara() {
		return transferidaPara;
	}

	public void setTransferidaPara(Boolean transferidaPara) {
		this.transferidaPara = transferidaPara;
	}

	/**
	 * @return the filtrarCursoAnual
	 */
	public Boolean getFiltrarCursoAnual() {
		if (filtrarCursoAnual == null) {
			filtrarCursoAnual = true;
		}
		return filtrarCursoAnual;
	}

	/**
	 * @param filtrarCursoAnual the filtrarCursoAnual to set
	 */
	public void setFiltrarCursoAnual(Boolean filtrarCursoAnual) {
		this.filtrarCursoAnual = filtrarCursoAnual;
	}

	/**
	 * @return the filtrarCursoSemestral
	 */
	public Boolean getFiltrarCursoSemestral() {
		if (filtrarCursoSemestral == null) {
			filtrarCursoSemestral = true;
		}
		return filtrarCursoSemestral;
	}

	/**
	 * @param filtrarCursoSemestral the filtrarCursoSemestral to set
	 */
	public void setFiltrarCursoSemestral(Boolean filtrarCursoSemestral) {
		this.filtrarCursoSemestral = filtrarCursoSemestral;
	}

	/**
	 * @return the filtrarCursoIntegral
	 */
	public Boolean getFiltrarCursoIntegral() {
		if (filtrarCursoIntegral == null) {
			filtrarCursoIntegral = true;
		}
		return filtrarCursoIntegral;
	}

	/**
	 * @param filtrarCursoIntegral the filtrarCursoIntegral to set
	 */
	public void setFiltrarCursoIntegral(Boolean filtrarCursoIntegral) {
		this.filtrarCursoIntegral = filtrarCursoIntegral;
	}
	
	public Boolean getTrazerAlunosComTransferenciaMatriz() {
		if (trazerAlunosComTransferenciaMatriz == null) {
			trazerAlunosComTransferenciaMatriz = false;
		}
		return trazerAlunosComTransferenciaMatriz;
	}

	public void setTrazerAlunosComTransferenciaMatriz(Boolean trazerAlunosComTransferenciaMatriz) {
		this.trazerAlunosComTransferenciaMatriz = trazerAlunosComTransferenciaMatriz;
	}
	
	

	public void realizarMarcarSituacaoFinanceiraMatricula(){
		realizarMarcarSituacaoFinanceiraMatricula(true);
	}
	
	public void realizarDesmarcarSituacaoFinanceiraMatricula(){
		realizarMarcarSituacaoFinanceiraMatricula(false);
	}

	public void realizarMarcarSituacaoFinanceiraMatricula(boolean selecionado){
		setPendenteFinanceiro(selecionado);
		setConfirmado(selecionado);
	}
	
	public FormaIngresso getFormaIngresso() {
		if(formaIngresso == null){
			formaIngresso = FormaIngresso.NENHUM;
		}
		return formaIngresso;
	}

	public void setFormaIngresso(FormaIngresso formaIngresso) {
		this.formaIngresso = formaIngresso;
	}

	public Boolean getApresentarSubtotalFormaIngresso() {
		if(apresentarSubtotalFormaIngresso == null){
			apresentarSubtotalFormaIngresso = false;
		}
		return apresentarSubtotalFormaIngresso;
	}

	public void setApresentarSubtotalFormaIngresso(Boolean apresentarSubtotalFormaIngresso) {
		this.apresentarSubtotalFormaIngresso = apresentarSubtotalFormaIngresso;
	}

	public PeriodicidadeEnum getPeriodicidadeEnum() {
		if (periodicidadeEnum == null) {
			periodicidadeEnum = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidadeEnum;
	}

	public void setPeriodicidadeEnum(PeriodicidadeEnum periodicidadeEnum) {
		this.periodicidadeEnum = periodicidadeEnum;
	}

	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.ANUAL, PeriodicidadeEnum.ANUAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL, PeriodicidadeEnum.SEMESTRAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.INTEGRAL, PeriodicidadeEnum.INTEGRAL.getDescricao()));
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}
	
	public void realizarMarcarTodasSituacoesHistorico(){
		realizarSelecionarTodosSituacoesHistorico(true);
	}
	
	public void realizarDesmarcarTodasSituacoesHistorico(){
		realizarSelecionarTodosSituacoesHistorico(false);
	}
	
	public void realizarSelecionarTodosSituacoesHistorico(boolean selecionado){
		setReprovado(selecionado); 
		setAprovado(selecionado);
		setNaoCursada(selecionado); 
		setCursando(selecionado); 
		setTrancamentoHistorico(selecionado); 
		setAbandonoHistorico(selecionado); 
		setCanceladoHistorico(selecionado); 
		setTransferidoHistorico(selecionado); 
		setConcessaoCreditoHistorico(selecionado); 
		setConcessaoCargaHorariaHistorico(selecionado); 
		setPreMatriculaHistorico(selecionado);
		setJubilado(selecionado);
	}

	public Boolean getReprovado() {
		if (reprovado == null) {
			reprovado = true;
		}
		return reprovado;
	}

	public void setReprovado(Boolean reprovado) {
		this.reprovado = reprovado;
	}

	public Boolean getAprovado() {
		if (aprovado == null) {
			aprovado = true;
		}
		return aprovado;
	}

	public void setAprovado(Boolean aprovado) {
		this.aprovado = aprovado;
	}	

	public Boolean getNaoCursada() {
		if (naoCursada == null) {
			naoCursada = true;
		}
		return naoCursada;
	}

	public void setNaoCursada(Boolean naoCursada) {
		this.naoCursada = naoCursada;
	}

	public Boolean getCursando() {
		if (cursando == null) {
			cursando = true;
		}
		return cursando;
	}

	public void setCursando(Boolean cursando) {
		this.cursando = cursando;
	}

	public Boolean getTrancamentoHistorico() {
		if (trancamentoHistorico == null) {
			trancamentoHistorico = true;
		}
		return trancamentoHistorico;
	}

	public void setTrancamentoHistorico(Boolean trancamentoHistorico) {
		this.trancamentoHistorico = trancamentoHistorico;
	}

	public Boolean getAbandonoHistorico() {
		if (abandonoHistorico == null) {
			abandonoHistorico = true;
		}
		return abandonoHistorico;
	}

	public void setAbandonoHistorico(Boolean abandonoHistorico) {
		this.abandonoHistorico = abandonoHistorico;
	}

	public Boolean getCanceladoHistorico() {
		if (canceladoHistorico == null) {
			canceladoHistorico = true;
		}
		return canceladoHistorico;
	}

	public void setCanceladoHistorico(Boolean canceladoHistorico) {
		this.canceladoHistorico = canceladoHistorico;
	}

	public Boolean getTransferidoHistorico() {
		if (transferidoHistorico == null) {
			transferidoHistorico = true;
		}
		return transferidoHistorico;
	}

	public void setTransferidoHistorico(Boolean transferidoHistorico) {
		this.transferidoHistorico = transferidoHistorico;
	}

	public Boolean getConcessaoCreditoHistorico() {
		if (concessaoCreditoHistorico == null) {
			concessaoCreditoHistorico = true;
		}
		return concessaoCreditoHistorico;
	}

	public void setConcessaoCreditoHistorico(Boolean concessaoCreditoHistorico) {
		this.concessaoCreditoHistorico = concessaoCreditoHistorico;
	}

	public Boolean getConcessaoCargaHorariaHistorico() {
		if (concessaoCargaHorariaHistorico == null) {
			concessaoCargaHorariaHistorico = true;
		}
		return concessaoCargaHorariaHistorico;
	}

	public void setConcessaoCargaHorariaHistorico(Boolean concessaoCargaHorariaHistorico) {
		this.concessaoCargaHorariaHistorico = concessaoCargaHorariaHistorico;
	}

	public Boolean getPreMatriculaHistorico() {
		if (preMatriculaHistorico == null) {
			preMatriculaHistorico = true;
		}
		return preMatriculaHistorico;
	}

	public void setPreMatriculaHistorico(Boolean preMatriculaHistorico) {
		this.preMatriculaHistorico = preMatriculaHistorico;
	}
	
	public Boolean getJubilado() {
		if (jubilado == null) {
			jubilado = true;
		}
		return jubilado;
	}

	public void setJubilado(Boolean jubilado) {
		this.jubilado = jubilado;
	}
	
	public boolean isTodasSituacoesHistorico(){
		return getReprovado() 
		&& getAprovado()
		&& getNaoCursada() 
		&& getCursando() 
		&& getTrancamentoHistorico() 
		&& getAbandonoHistorico() 
		&& getCanceladoHistorico() 
		&& getTransferidoHistorico() 
		&& getConcessaoCreditoHistorico() 
		&& getConcessaoCargaHorariaHistorico() 
		&& getPreMatriculaHistorico()
		&& getJubilado(); 	
	}

	
	public boolean isTodasSituacoesHistoricoDesmarcadas(){
		return !getReprovado() 
		&& !getAprovado()
		&& !getNaoCursada() 
		&& !getCursando() 
		&& !getTrancamentoHistorico() 
		&& !getAbandonoHistorico() 
		&& !getCanceladoHistorico() 
		&& !getTransferidoHistorico() 
		&& !getConcessaoCreditoHistorico() 
		&& !getConcessaoCargaHorariaHistorico() 
		&& !getPreMatriculaHistorico(); 	
	}
	
	public boolean isTodasSituacoesMatriculaDesmarcadas(){
		return !getAtivo() 
				&& !getPreMatricula()
				&& !getPreMatriculaCancelada() 
				&& !getTrancado() 
				&& !getAbandonado() 
				&& !getTransferenciaInterna() 
				&& !getTransferenciaExterna() 
				&& !getCancelado() 
				&& !getConcluido() 
				&& !getFormado() 
				&& !getJubilado(); 	
	}
	
//	public void realizarMarcarSituacoesConfiguracaoDiploma(ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigitalVO) {
//		if (Uteis.isAtributoPreenchido(configuracaoDiplomaDigitalVO)) {
//			setAprovado(configuracaoDiplomaDigitalVO.getHistoricoConsiderarAprovado());
//			setReprovado(configuracaoDiplomaDigitalVO.getHistoricoConsiderarReprovado());
//			setCursando(configuracaoDiplomaDigitalVO.getHistoricoConsiderarCursando());
//			setTrancamentoHistorico(configuracaoDiplomaDigitalVO.getHistoricoConsiderarEvasao());
//			setAbandonoHistorico(configuracaoDiplomaDigitalVO.getHistoricoConsiderarEvasao());
//			setCanceladoHistorico(configuracaoDiplomaDigitalVO.getHistoricoConsiderarEvasao());
//			setTransferidoHistorico(configuracaoDiplomaDigitalVO.getHistoricoConsiderarEvasao());
//			setJubilado(configuracaoDiplomaDigitalVO.getHistoricoConsiderarEvasao());
//			setNaoCursada(!configuracaoDiplomaDigitalVO.getHistoricoConsiderarCursando());
//			setConcessaoCreditoHistorico(configuracaoDiplomaDigitalVO.getHistoricoConsiderarEvasao());
//			setConcessaoCargaHorariaHistorico(configuracaoDiplomaDigitalVO.getHistoricoConsiderarEvasao());
//			setPreMatriculaHistorico(configuracaoDiplomaDigitalVO.getHistoricoConsiderarEvasao());
//		} else {
//			setAprovado(Boolean.TRUE);
//			setReprovado(Boolean.TRUE);
//			setCursando(Boolean.TRUE);
//			setTrancamentoHistorico(Boolean.FALSE);
//			setAbandonoHistorico(Boolean.FALSE);
//			setCanceladoHistorico(Boolean.FALSE);
//			setTransferidoHistorico(Boolean.FALSE);
//			setJubilado(Boolean.FALSE);
//			setNaoCursada(Boolean.FALSE);
//			setConcessaoCreditoHistorico(Boolean.FALSE);
//			setConcessaoCargaHorariaHistorico(Boolean.FALSE);
//			setPreMatriculaHistorico(Boolean.FALSE);
//		}
//	}
}
