package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.HorarioAlunoDiaVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.HorarioAluno;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("HorarioAulaAlunoControle")
@Scope("viewScope")
public class HorarioAulaAlunoControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7393113327748412281L;
	private Date dataBaseHorarioAula;
	private Date dataInicio;
	private Date dataTermino;
	private Date dataMinima;
	private Date dataMaxima;
	private PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO;
	private List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs;
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs;
	private Boolean calendarioMensal;
	private List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private MatriculaVO matricula;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemSemestre;
	private String ano;
	private String semestre;
	private Boolean ocultarHorarioLivre;
	private List<ProgramacaoTutoriaOnlineVO> programacaoTutoriaOnlineVOs;

	public HorarioAulaAlunoControle() {
		super();
	}

	private String mesAnoApresentar;
	
	

	public void setMesAnoApresentar(String mesAnoApresentar) {
		this.mesAnoApresentar = mesAnoApresentar;
	}
	
	@PostConstruct
	public void realizarCarregamentoHorarioAulaVindoTelaFichaAluno() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
			montarListaSelectItemUnidadeEnsino();
		} 
		MatriculaPeriodoVO matriculaPeriodoFichaAlunoVO = (MatriculaPeriodoVO) context().getExternalContext().getSessionMap().get("matriculaPeriodoFichaAluno");
		try {
			if (matriculaPeriodoFichaAlunoVO != null && !matriculaPeriodoFichaAlunoVO.getCodigo().equals(0) ) {
				getMatricula().setMatricula(matriculaPeriodoFichaAlunoVO.getMatricula());
				consultarAlunoPorMatricula();
				if (!matriculaPeriodoFichaAlunoVO.getAno().equals("")) {
					setAno(matriculaPeriodoFichaAlunoVO.getAno());
				}
				if (!matriculaPeriodoFichaAlunoVO.getSemestre().equals("")) {
					setSemestre(matriculaPeriodoFichaAlunoVO.getSemestre());
				}
			} else {
				if(getUsuarioLogado().getIsApresentarVisaoAluno()  || getUsuarioLogado().getIsApresentarVisaoPais()){
					getFacadeFactory().getMatriculaFacade().validarConsultarMeusHorarios(getUsuarioLogadoClone());
					setCalendarioMensal(getVisaoAlunoControle().getMatricula().getCurso().getPeriodicidade().equals("IN"));
					if((getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getVisaoAlunoControle().getMatricula().getUnidadeEnsino().getCodigo()).getPermitirAcessoAlunoPreMatricula() 
							&& getVisaoAlunoControle().getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())
							&& !getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoPorSituacao(getVisaoAlunoControle().getMatricula().getMatricula(), "AT"))
							|| (getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getVisaoAlunoControle().getMatricula().getUnidadeEnsino().getCodigo()).getPermitirAcessoAlunoFormado()
									&& getVisaoAlunoControle().getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor()))
							|| (getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getVisaoAlunoControle().getMatricula().getUnidadeEnsino().getCodigo()).getPermitirAcessoAlunoEvasao() 
									&& (getVisaoAlunoControle().getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANCADA.getValor())
											|| getVisaoAlunoControle().getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO.getValor())
											|| getVisaoAlunoControle().getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.CANCELADA.getValor())
											|| getVisaoAlunoControle().getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getValor())
											|| getVisaoAlunoControle().getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor())))) {
						if (!Uteis.isAtributoPreenchido(getVisaoAlunoControle().getMatriculaPeriodoVO().getCodigo())) {
							getVisaoAlunoControle().setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getVisaoAlunoControle().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
						}
						setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestreGradeCurricularAtual(getVisaoAlunoControle().getMatricula().getMatricula(), 0, 0, getVisaoAlunoControle().getMatriculaPeriodoVO().getAno(), getVisaoAlunoControle().getMatriculaPeriodoVO().getSemestre(), false, true, true, true, false, "'PC'", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					}else {
						setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaAtiva(getVisaoAlunoControle().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado()));
					}
					setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorUltimaMatriculaPeriodo(getVisaoAlunoControle().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					// setDataMinima(getFacadeFactory().getHorarioAlunoFacade().consultarPrimeiroDiaAulaAluno(getMatriculaPeriodoTurmaDisciplinaVOs(),
					// getVisaoAlunoControle().getMatricula().getUnidadeEnsino(),
					// getUsuarioLogado()));
					// setDataMaxima(getFacadeFactory().getHorarioAlunoFacade().consultarUltimoDiaAulaAluno(getMatriculaPeriodoTurmaDisciplinaVOs(),
					// getVisaoAlunoControle().getMatricula().getUnidadeEnsino(),
					// getUsuarioLogado()));
					// if(getDataMaxima() != null &&
					// getDataBaseHorarioAula().after(getDataMaxima())){
					// setDataBaseHorarioAula(getDataMaxima());
					// }
					montarListaSelectItemUnidadeEnsino();
					consultarHorarioAulaAluno();
					consultarHorarioAulaEadAluno();
					if(getVisaoAlunoControle().getMatricula().getCurso().getAnual() || getVisaoAlunoControle().getMatricula().getCurso().getSemestral()) {
						setAno(Uteis.getAnoDataAtual4Digitos());
					}
					if(getVisaoAlunoControle().getMatricula().getCurso().getSemestral()) {
						setSemestre(Uteis.getSemestreAtual());
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("matriculaPeriodoFichaAluno");
		}
	}

	public String getMesAnoApresentar() {
		if (mesAnoApresentar == null) {
			if (getCalendarioMensal()) {
				return Uteis.getMesReferenciaData(getDataBaseHorarioAula());
			}
			int mesInicio = Uteis.getMesData(getDataInicio());
			int mesTermino = Uteis.getMesData(getDataTermino());
			if (mesInicio != mesTermino) {
				Date ultimaDataMes = Uteis.getDataUltimoDiaMes(getDataInicio());
				Date primeiraDataMes = Uteis.getDataPrimeiroDiaMes(getDataTermino());
				long nrDias1 = UteisData.nrDiasEntreDatas(ultimaDataMes, getDataInicio());
				long nrDias2 = UteisData.nrDiasEntreDatas(getDataTermino(), primeiraDataMes);
				if (UteisData.nrDiasEntreDatas(primeiraDataMes, ultimaDataMes) > 27) {
					return Uteis.getMesReferenciaData(Uteis.obterDataFutura(ultimaDataMes, 10));
				}
				if (nrDias1 > nrDias2) {
					mesAnoApresentar = Uteis.getMesReferenciaData(ultimaDataMes);
				} else {
					mesAnoApresentar = Uteis.getMesReferenciaData(primeiraDataMes);
				}

			}else{
				mesAnoApresentar = Uteis.getMesReferenciaData(getDataInicio());
			}
		}
		return mesAnoApresentar;
	}

	public Date getDataBaseHorarioAula() {
		if (dataBaseHorarioAula == null) {
			dataBaseHorarioAula = new Date();
		}
		return dataBaseHorarioAula;
	}


	public void selecionarHorarioAlunoDia() {
		HorarioAlunoDiaVO obj = ((HorarioAlunoDiaVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDia"));
		setDataBaseHorarioAula(obj.getData());
		consultarHorarioAulaAluno();
	}

	
	public void setDataBaseHorarioAula(Date dataBaseHorarioAula) {
		this.dataBaseHorarioAula = dataBaseHorarioAula;
	}
	
	public void inicializarAnoSemestrePorMatricula(){
		if(getApresentarAno()){
			setAno(Uteis.getAnoDataAtual4Digitos());
		}else{
			setAno("");
		}
		if(getApresentarSemestre()){
			setSemestre(Uteis.getSemestreAtual());
		}else{
			setSemestre("");
		}
		consultarHorarioAlunoRel();
	}

	public void consultarHorarioAlunoRel() {
		try {
			if(getMatricula().getMatricula().trim().isEmpty()){
				throw new ConsistirException("O campos MATRÍCULA deve ser informado.");
			}
			if(getApresentarAno() && getAno().trim().length()!=4){
				throw new ConsistirException("O campos ANO deve possuir 4 dígitos.");
			}
			if(getApresentarSemestre() && (!getSemestre().equals("1") && !getSemestre().equals("2"))){
				throw new ConsistirException("O campos SEMESTRE deve ser informado.");
			}
			setCalendarioMensal(getMatricula().getCurso().getPeriodicidade().equals("IN"));
			setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestreGradeCurricularAtual(getMatricula().getMatricula(), 0, 0, getAno(), getSemestre(), false, true, true, true, false, "'PC'", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorMatriculaAnoSemestre(getMatricula().getMatricula(), getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
			consultarHorarioAulaAluno();
			consultarHorarioAulaEadAluno();
	}
	
	public void consultarHorarioAulaAluno() {
		try {
			setMesAnoApresentar(null);
			if (getCalendarioMensal()) {
//				setDataInicio(Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(Uteis.getDataPrimeiroDiaMes(getDataBaseHorarioAula()))), 1));
//				setDataTermino(Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getUltimoDiaSemana(Uteis.getDataUltimoDiaMes(getDataBaseHorarioAula()))), 1));
				setDataInicio(Uteis.obterDataPassada(new Date(), 500));
				setDataTermino(Uteis.obterDataFutura(new Date(), 500));
			} else {
				setDataInicio(UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(getDataBaseHorarioAula())), 1));
				setDataTermino(UteisData.obterDataFutura(UteisData.getUltimoDiaSemana(getDataBaseHorarioAula()), 1));
			}
			setHorarioAlunoTurnoVOs(getFacadeFactory().getHorarioAlunoFacade().consultarMeusHorariosAluno(getMatriculaPeriodoTurmaDisciplinaVOs(), getDataBaseHorarioAula(), null, getCalendarioMensal(), getMatricula().getUnidadeEnsino(), getUsuarioLogado(), false, 0));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), this.getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatricula(objAluno);
//			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatricula(objAluno.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuario());
//			if(matriculaPeriodo == null){
//				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
//			}
			getUnidadeEnsinoVO().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
			inicializarAnoSemestrePorMatricula();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getHorarioAlunoTurnoVOs().clear();
			setMatricula(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (this.getUnidadeEnsinoVO().getCodigo() != 0) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatricula(obj);
			getUnidadeEnsinoVO().setCodigo(obj.getUnidadeEnsino().getCodigo());
	
//			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//			if(matriculaPeriodo == null){
//				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
//			}
			valorConsultaAluno = "";
			campoConsultaAluno = "";
			getListaConsultaAluno().clear();
			getHorarioAlunoTurnoVOs().clear();
			inicializarAnoSemestrePorMatricula();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));

			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		return lista;
	}
	
	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatricula());
		setHorarioAlunoTurnoVOs(new ArrayList<HorarioAlunoTurnoVO>());
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			limparDadosAluno();
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			setMensagemID("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}
	
	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}
	
	
	
	public void imprimirPDF() {
		List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
		Boolean isListaObjetosVazia = false;
		Boolean isListaProgramacaoTutoriaOnlineVazia = false;
		try {
			listaObjetos = getFacadeFactory().getHorarioAlunoFacade().criarObjetoRelatorio(getHorarioAlunoTurnoVOs(), getUsuarioLogado());
			if(listaObjetos.isEmpty()) {
				isListaObjetosVazia = true;
			}
			if(getProgramacaoTutoriaOnlineVOs().isEmpty()) {
				isListaProgramacaoTutoriaOnlineVazia = true;
			}
			if (!listaObjetos.isEmpty() || !getProgramacaoTutoriaOnlineVOs().isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(HorarioAluno.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(HorarioAluno.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Horário do Aluno");
				if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) && getVisaoAlunoControle().getMatricula().getCurso().getNivelEducacional().equals("PO")) {
					Ordenacao.ordenarLista(listaObjetos, "dataInicio");	
				} else {
					Ordenacao.ordenarLista(listaObjetos, "modulo");
				}
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(HorarioAluno.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().adicionarParametro("ocultarData", false);
				getSuperParametroRelVO().adicionarParametro("periodicidadeMatriz", getMatricula().getCurso().getPeriodicidade());
				getSuperParametroRelVO().adicionarParametro("anoAtualMatricula", getAno());
				
				getSuperParametroRelVO().adicionarParametro("semestreAtualMatricula", getSemestre());
				if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
					getSuperParametroRelVO().adicionarParametro("nivelEducacional", getVisaoAlunoControle().getMatricula().getCurso().getNivelEducacional());
				} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
					getSuperParametroRelVO().adicionarParametro("nivelEducacional", getMatricula().getCurso().getNivelEducacional());
				}
				if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
					getSuperParametroRelVO().setAluno(getMatricula().getAluno().getNome());
					getSuperParametroRelVO().setUnidadeEnsino(getMatricula().getUnidadeEnsino().getNome());
					getSuperParametroRelVO().setCurso(getMatricula().getCurso().getNome());
					getSuperParametroRelVO().adicionarParametro("dataMatricula", Uteis.getData(getFacadeFactory().getMatriculaPeriodoFacade().consultarDataMatriculaPeriodoPorMatriculaAnoSemestre(getMatricula().getMatricula(), getAno(), getSemestre())));
				}else if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()){
					getSuperParametroRelVO().setAluno(getVisaoAlunoControle().getMatricula().getAluno().getNome());
					getSuperParametroRelVO().setUnidadeEnsino(getVisaoAlunoControle().getMatricula().getUnidadeEnsino().getNome());
					getSuperParametroRelVO().setCurso(getVisaoAlunoControle().getMatricula().getCurso().getNome());
					getSuperParametroRelVO().adicionarParametro("dataMatricula", Uteis.getData(getFacadeFactory().getMatriculaPeriodoFacade().consultarDataMatriculaPeriodoPorMatriculaAnoSemestre(getVisaoAlunoControle().getMatricula().getMatricula(), getAno(), getSemestre())));					
				}
				getSuperParametroRelVO().adicionarParametro("listaProgramacaoTutoriaOnline", getProgramacaoTutoriaOnlineVOs());
				getSuperParametroRelVO().adicionarParametro("isListaObjetosVazia", isListaObjetosVazia);
				getSuperParametroRelVO().adicionarParametro("isListaProgramacaoTutoriaOnlineVazia", isListaProgramacaoTutoriaOnlineVazia);
				//getSuperParametroRelVO().setDataInicio(dataInicio);
				//getSuperParametroRelVO().setDataFim(dataInicio);
				realizarImpressaoRelatorio();
				//removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}
	
	
	
	public void consultarHorarioAulaAlunoTurnoEspecifico() {
		try {
			HorarioAlunoTurnoVO horarioAlunoTurnoVO = (HorarioAlunoTurnoVO) getRequestMap().get("turnoItens");
			setDataInicio(UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(getDataBaseHorarioAula())), 1));
			setDataTermino(UteisData.obterDataFutura(UteisData.getUltimaDataMes(getDataBaseHorarioAula()), 1));
			setHorarioAlunoTurnoVOs(getFacadeFactory().getHorarioAlunoFacade().consultarMeusHorariosAluno(getMatriculaPeriodoTurmaDisciplinaVOs(), getDataBaseHorarioAula(), horarioAlunoTurnoVO.getTurno().getCodigo(), getCalendarioMensal(), getVisaoAlunoControle().getMatricula().getUnidadeEnsino(), getUsuarioLogado(), false, getVisaoAlunoControle().getMatricula().getUnidadeEnsino().getCodigo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarProximaSemana() {
		if (getCalendarioMensal()) {
			setDataBaseHorarioAula(Uteis.getDataPrimeiroDiaMes(Uteis.obterDataAvancadaPorMes(getDataBaseHorarioAula(), 1)));
		} else {
			setDataBaseHorarioAula(UteisData.getPrimeiroDiaProximaSemana(getDataBaseHorarioAula()));
		}
		
		consultarHorarioAulaAluno();
	}

	public void visualizarProximoMes() {
		if (getCalendarioMensal()) {
			setDataBaseHorarioAula(Uteis.getDataPrimeiroDiaMes(Uteis.obterDataAvancadaPorMes(getDataBaseHorarioAula(), 1)));
		} 
		
		consultarHorarioAulaAluno();
	}
	public void visualizarSemanaAnterior() {
		if (getCalendarioMensal()) {
			setDataBaseHorarioAula(Uteis.getDataPrimeiroDiaMes(Uteis.obterDataAvancadaPorMes(getDataBaseHorarioAula(), -1)));
		} else {
			setDataBaseHorarioAula(UteisData.getPrimeiroDiaSemanaPassada(getDataBaseHorarioAula()));
		}

		consultarHorarioAulaAluno();
	}

	public List<HorarioAlunoTurnoVO> getHorarioAlunoTurnoVOs() {
		if (horarioAlunoTurnoVOs == null) {
			horarioAlunoTurnoVOs = new ArrayList<HorarioAlunoTurnoVO>(0);
		}
		return horarioAlunoTurnoVOs;
	}

	public void setHorarioAlunoTurnoVOs(List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs) {
		this.horarioAlunoTurnoVOs = horarioAlunoTurnoVOs;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaVOs() {
		if (matriculaPeriodoTurmaDisciplinaVOs == null) {
			matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		this.matriculaPeriodoTurmaDisciplinaVOs = matriculaPeriodoTurmaDisciplinaVOs;
	}

	public Date getDataInicio() {

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

	public Date getDataMinima() {

		return dataMinima;
	}

	public void setDataMinima(Date dataMinima) {
		this.dataMinima = dataMinima;
	}

	public Date getDataMaxima() {

		return dataMaxima;
	}

	public void setDataMaxima(Date dataMaxima) {
		this.dataMaxima = dataMaxima;
	}

	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO getPeriodoLetivoAtivoUnidadeEnsinoCursoVO() {
		if (periodoLetivoAtivoUnidadeEnsinoCursoVO == null) {
			periodoLetivoAtivoUnidadeEnsinoCursoVO = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		}
		return periodoLetivoAtivoUnidadeEnsinoCursoVO;
	}

	public void setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO) {
		this.periodoLetivoAtivoUnidadeEnsinoCursoVO = periodoLetivoAtivoUnidadeEnsinoCursoVO;
	}

	public Boolean getCalendarioMensal() {
		if (calendarioMensal == null) {
			calendarioMensal = false;
		}
		return calendarioMensal;
	}

	public void setCalendarioMensal(Boolean calendarioMensal) {
		this.calendarioMensal = calendarioMensal;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}
	
	public Boolean getApresentarAno(){
		return getMatricula().getCurso().getAnual() || getMatricula().getCurso().getSemestral();
	}
	
	public Boolean getApresentarSemestre(){
		return getMatricula().getCurso().getSemestral();
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
	 * @param ano the ano to set
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
	 * @param semestre the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the ocultarHorarioLivre
	 */
	public Boolean getOcultarHorarioLivre() {
		if (ocultarHorarioLivre == null) {
			ocultarHorarioLivre = false;
		}
		return ocultarHorarioLivre;
	}

	/**
	 * @param ocultarHorarioLivre the ocultarHorarioLivre to set
	 */
	public void setOcultarHorarioLivre(Boolean ocultarHorarioLivre) {
		this.ocultarHorarioLivre = ocultarHorarioLivre;
	}

	/**
	 * @return the listaSelectItemSemestre
	 */
	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1","1º"));
			listaSelectItemSemestre.add(new SelectItem("2","2º"));
		}
		return listaSelectItemSemestre;
	}

	/**
	 * @param listaSelectItemSemestre the listaSelectItemSemestre to set
	 */
	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}
	
	public void consultarHorarioAulaEadAluno() {
		try {
			setProgramacaoTutoriaOnlineVOs(getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarPorMatriculaPeriodoTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVOs(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), false));

			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<ProgramacaoTutoriaOnlineVO> getProgramacaoTutoriaOnlineVOs() {
		if(programacaoTutoriaOnlineVOs == null) {
			programacaoTutoriaOnlineVOs = new ArrayList<ProgramacaoTutoriaOnlineVO>(0);
		}
		return programacaoTutoriaOnlineVOs;
	}

	public void setProgramacaoTutoriaOnlineVOs(List<ProgramacaoTutoriaOnlineVO> programacaoTutoriaOnlineVOs) {
		this.programacaoTutoriaOnlineVOs = programacaoTutoriaOnlineVOs;
	}
	
	

}
