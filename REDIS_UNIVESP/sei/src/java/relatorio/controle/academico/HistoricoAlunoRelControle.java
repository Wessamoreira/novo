package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.ExpedicaoDiplomaControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.academico.ConfiguracaoObservacaoHistoricoVO;
import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.FiltroSituacaoDisciplina;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.HistoricoAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.comuns.academico.enumeradores.TipoObservacaoHistoricoEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.HistoricoAlunoRel;

@SuppressWarnings("unchecked")
@Controller("HistoricoAlunoRelControle")
@Scope("viewScope")
@Lazy
public class HistoricoAlunoRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6155511087978273037L;
	private HistoricoAlunoRelVO historicoAlunoRelVO;
	private ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO;
	private String observacaoComplementar;
	private String textoCertificadoEstudo;
	private String observacaoComplementarIntegralizado;
	private List<MatriculaVO> listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private TurmaVO turmaAluno;
	private UnidadeEnsinoVO unidadeEnsinoAluno;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private GradeCurricularVO gradeCurricularVO;
	private List<SelectItem> listaSelectItemSituacaoDisciplina;
	private List<SelectItem> listaSelectItemOrdemDisciplina;
	private int filtro;
	private Boolean filtroDisciplinasACursar;
	private int ordem;
	private List<SelectItem> selectItemsCargoFuncionarioPrincipal;
	private List<SelectItem> selectItemsCargoFuncionarioSecundario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private String campoFiltroPor;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<MatriculaVO> listaMatriculas;
	private String ano;
	private String semestre;
	private String campoConsultaTipoDesconto;
	private List<SelectItem> listaSelectItemDescontoPlanoFinanceiro;
	private List<SelectItem> listaSelectItemDescontoConvenio;
	private String tipoLayout;
	private Boolean imprimirExcel;
	private Boolean utilizarUnidadeEnsinoMatriz;
	private Date dataExpedicaoDiploma;
	private Boolean apresentarInstituicaoDisciplinaAproveitada;
	private Boolean apresentarFrequencia;
	private Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade;
	private Boolean apresentarDisciplinaForaGrade;
	private Boolean gradeAtualAluno;
	private String tipoNivelEducacional;
	private Boolean apresentarTopoRelatorio;
	private Boolean desconsiderarSituacaoMatriculaPeriodo;
	private Boolean apresentarDisciplinaPeriodoTrancadoCanceladoTransferido;
	private Boolean apresentarObservacaoComplementar;
	private boolean apresentarCargaHorariaDisciplina = false;
	private Boolean apresentarObservacaoTransferenciaMatrizCurricular;
	private Boolean possuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular;
	private String observacaoTransferenciaMatrizCurricular;
	private Boolean apresentarApenasUltimoHistoricoDisciplina;
	private Boolean permiteVisualizarAlterarDataExpedicaoDiploma;
	private List<SelectItem> selectItemsCargoFuncionarioTerciario;
	private ExpedicaoDiplomaVO expedicaoDiplomaVO;
	private Boolean permiteVisualizarAlterarDataEmissaoHistorico;
	private Boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista;
	private Boolean apresentarMediaNotasSemestreAnteriorAluno;
	private Boolean apresentarCoordenadorTitularCurso;
	private Boolean dataInicioTerminoAlteracoesCadastrais;
	private ProgramacaoFormaturaVO programacaoFormaturaVO;
	private List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura;
	private String valorConsultaProgramacaoFormatura;
	private String campoConsultaProgramacaoFormatura;
    private Boolean mostrarSegundoCampoProgramacaoFormatura;
    private Date valorConsultaDataInicioProgramacaoFormatura;
    private Date valorConsultaDataFinalProgramacaoFormatura;
    private String filtroAlunosPresentesColacaoGrau;
	/**
	 * campos utilizado para filtrar layout do ipog
	 */
	private boolean apresentarApenasProfessorTitulacaoTurmaOrigem = false;
	private String msgProfessorTitulacao;
	private ProgressBarVO progressBarVO;
	private RequerimentoVO requerimentoVO;
	private String regraApresentacaoProfessorDisciplinaAproveitamento;
	private Boolean trazerTodosProfessoresDisciplinas;
	private List<String> listaErroHistoricoEscolarDigital;
	private List<HistoricoAlunoRelVO> listaErroHistoricoAlunoRel;
	private List<SelectItem> listaSelectItemVersaoDiploma;
	private VersaoDiplomaDigitalEnum versaoDiploma;
	private Boolean gerarXmlHistorico;
	private List<SelectItem> listaSelectItemUnidadeEnsinoCertificadora;
	private String oncomplete;
	private UnidadeEnsinoVO unidadeEnsinoCertificadora;
	private ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital;

	public HistoricoAlunoRelControle() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		verificarUsuarioPermissaoApresentarObsComp();
		verificarUsuarioPermissaoApresentarObsTransferenciaMatrizCurricular();
		verificarUsuarioPermissaoVisualizarAlterarDataEmissaoHistorico();
		setMarcarTodasSituacoesHistorico(true);
		setAssinarDigitalmente(false);
        setControleConsulta(new ControleConsulta());
        setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
		// obterUsuarioLogado();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void verificarUsuarioPermissaoApresentarObsComp() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"ApresentarCampoObsCompLayout5", getUsuarioLogado());
			setApresentarObservacaoComplementar(Boolean.TRUE);
		} catch (Exception e) {
			setApresentarObservacaoComplementar(Boolean.FALSE);
		}
	}

	@PostConstruct
	public void realizarCarregamentoHistoricoVindoTelaFichaAluno() {
		String matricula = (String) ((HttpServletRequest) context().getExternalContext().getRequest())
				.getParameter("matricula");
		if (matricula != null && !matricula.trim().isEmpty()) {
			try {
				getMatriculaVO().setMatricula(matricula);
				consultarAlunoPorMatricula();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matricula");
			}
		}
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getSessionMap()
				.get("matriculaFichaAluno");
		if (matriculaVO != null && !matriculaVO.getMatricula().equals("")) {
			try {
				setMatriculaVO(matriculaVO);
				consultarAlunoPorMatricula();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaFichaAluno");
			}
		}
	}

	@PostConstruct
	public void realizarCarregamentoHistoricoVindoTelaExpedicaoDiploma() {
		String matricula = (String) context().getExternalContext().getSessionMap().get("matriculaExpedicaoDiploma");
		Integer programacaoFormatura = (Integer) context().getExternalContext().getSessionMap().get("programacaoFormaturaLote");
		if (Uteis.isAtributoPreenchido(matricula)) {
			try {
				setCampoFiltroPor("aluno");
				montarListaSelectItemUnidadeEnsino();
				getMatriculaVO().setMatricula(matricula);
				consultarAlunoPorMatricula();
				Boolean gerarXmlDiploma = (Boolean) context().getExternalContext().getSessionMap().get("gerarXmlDiploma");
				if (gerarXmlDiploma) {
					setAssinarDigitalmente(Boolean.TRUE);
					setGerarXmlHistorico(Boolean.TRUE);
				}
				getHistoricoAlunoRelVO().setFuncionarioPrincipalVO((FuncionarioVO) context().getExternalContext().getSessionMap().get("funcionarioPrincipal"));
				if (Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO())) {
					setSelectItemsCargoFuncionarioPrincipal(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					getHistoricoAlunoRelVO().setCargoFuncionarioPrincipal((CargoVO) context().getExternalContext().getSessionMap().get("cargoFuncionarioPrincipal"));
				}
				getHistoricoAlunoRelVO().setFuncionarioSecundarioVO((FuncionarioVO) context().getExternalContext().getSessionMap().get("funcionarioSecundario"));
				if (Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO())) {
					setSelectItemsCargoFuncionarioSecundario(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					getHistoricoAlunoRelVO().setCargoFuncionarioSecundario((CargoVO) context().getExternalContext().getSessionMap().get("cargoFuncionarioSecundario"));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaExpedicaoDiploma");
				context().getExternalContext().getSessionMap().remove("gerarXmlDiploma");
				context().getExternalContext().getSessionMap().remove("funcionarioPrincipal");
				context().getExternalContext().getSessionMap().remove("funcionarioSecundario");
				context().getExternalContext().getSessionMap().remove("cargoFuncionarioPrincipal");
				context().getExternalContext().getSessionMap().remove("cargoFuncionarioSecundario");
			}
		} else if (Uteis.isAtributoPreenchido(programacaoFormatura)) {
			try {
				setCampoFiltroPor("programacaoFormatura");
				montarListaSelectItemUnidadeEnsino();
				selecionarProgramacaoFormatura(programacaoFormatura);
				Boolean gerarXmlDiploma = (Boolean) context().getExternalContext().getSessionMap().get("gerarXmlDiploma");
				if (gerarXmlDiploma) {
					setAssinarDigitalmente(Boolean.TRUE);
					setGerarXmlHistorico(Boolean.TRUE);
				}
				getHistoricoAlunoRelVO().setFuncionarioPrincipalVO((FuncionarioVO) context().getExternalContext().getSessionMap().get("funcionarioPrincipal"));
				if (Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO())) {
					setSelectItemsCargoFuncionarioPrincipal(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					getHistoricoAlunoRelVO().setCargoFuncionarioPrincipal((CargoVO) context().getExternalContext().getSessionMap().get("cargoFuncionarioPrincipal"));
				}
				getHistoricoAlunoRelVO().setFuncionarioSecundarioVO((FuncionarioVO) context().getExternalContext().getSessionMap().get("funcionarioSecundario"));
				if (Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO())) {
					setSelectItemsCargoFuncionarioSecundario(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					getHistoricoAlunoRelVO().setCargoFuncionarioSecundario((CargoVO) context().getExternalContext().getSessionMap().get("cargoFuncionarioSecundario"));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("programacaoFormaturaLote");
				context().getExternalContext().getSessionMap().remove("gerarXmlDiploma");
				context().getExternalContext().getSessionMap().remove("funcionarioPrincipal");
				context().getExternalContext().getSessionMap().remove("funcionarioSecundario");
				context().getExternalContext().getSessionMap().remove("cargoFuncionarioPrincipal");
				context().getExternalContext().getSessionMap().remove("cargoFuncionarioSecundario");
			}
		}
	}

	public void imprimirExcel() {
		setImprimirExcel(true);
		imprimirPDF();
	}
	
	public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
    	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo()) ? getUnidadeEnsinoVO().getCodigo() : 0;
    	if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino) || !isAssinarDigitalmente()){
    		return new ArrayList<SelectItem>();
    	}
    	return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.HISTORICO);
    }

	public void imprimirPDF() {
		
		UsuarioVO usuarioLogado = new UsuarioVO();			

		if(Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			usuarioLogado = getUsuarioLogado();
		}else if(Uteis.isAtributoPreenchido(getUsuarioLogadoClone())) {
			usuarioLogado = getUsuarioLogadoClone();
		}else {
			if(getIsFiltrarPorturma() || getIsFiltrarPorProgramacaoFormatura()) {
				usuarioLogado = getProgressBarVO().getUsuarioVO();
			}
		}
		
		if (getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoPdfPrincipal() == null) {
			carregarDadosLayout();
		}
			

		HistoricoAlunoRelVO historicoTemp = null;
		String tipoLayoutComparar = !getTipoLayoutPersonalizado() ? getTipoLayout() : getConfiguracaoLayoutHistoricoSelecionadoVO().getChave().trim().isEmpty() || getConfiguracaoLayoutHistoricoSelecionadoVO().getChave().equals("0") ?  getConfiguracaoLayoutHistoricoSelecionadoVO().getCodigo()+"" : getConfiguracaoLayoutHistoricoSelecionadoVO().getChave();
		if (tipoLayoutComparar.equals("HistoricoAlunoEnsinoMedioLayout2Rel")
				|| (tipoLayoutComparar.equals("HistoricoAlunoEnsinoMedioLayout3Rel")
						&& !getMatriculaVO().getCurso().getNivelEducacional().equals("BA"))
				|| tipoLayoutComparar.equals("HistoricoAlunoEnsinoMedio")) {
			getSuperParametroRelVO().setTituloRelatorio("HISTÓRICO ESCOLAR - ENSINO MÉDIO");
		} else if (tipoLayoutComparar.equals("HistoricoAlunoEnsinoBasicoLayout2Rel")
				|| (tipoLayoutComparar.equals("HistoricoAlunoEnsinoMedioLayout3Rel")
						&& getMatriculaVO().getCurso().getNivelEducacional().equals("BA"))) {
			getSuperParametroRelVO().setTituloRelatorio("HISTÓRICO ESCOLAR - ENSINO FUNDAMENTAL");
		} else if (tipoLayoutComparar.equals("HistoricoAlunoNivelTecnicoRel")
				|| tipoLayoutComparar.equals("HistoricoAlunoNivelTecnico2Rel")) {
			getSuperParametroRelVO().setTituloRelatorio("HISTÓRICO ESCOLAR - NÍVEL TÉCNICO");
		} else if (tipoLayoutComparar.equals("HistoricoAlunoLayout4Rel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout13Rel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout12Rel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout14Rel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout4PortariaMECRel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout21PortariaMECRel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout22PortariaMECRel")
				|| tipoLayoutComparar.equals("HistoricoAlunoLayout24Graduacao")) {
			getSuperParametroRelVO().setTituloRelatorio("HISTÓRICO ACADÊMICO");
		} else if (tipoLayoutComparar.equals("HistoricoAlunoResidenciaMedicaRel")) {
			getSuperParametroRelVO().setTituloRelatorio("PROGRAMA DE RESIDÊNCIA MÉDICA");
		} else {
			getSuperParametroRelVO().setTituloRelatorio("HISTÓRICO ESCOLAR");
		}
		if (!tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel")) {
			getSuperParametroRelVO().setNomeEmpresa(getProgressBarVO().getUnidadeEnsinoVO().getNome());
		}
		String design = "";
		setMsgProfessorTitulacao("");
		setOncomplete(Constantes.EMPTY);
		setListaErroHistoricoEscolarDigital(new ArrayList<>());
		setListaErroHistoricoAlunoRel(new ArrayList<>(0));
		List<HistoricoAlunoRelVO> historicoAlunoRelVOs = new ArrayList<HistoricoAlunoRelVO>(0);
		try {
			registrarAtividadeUsuario(getProgressBarVO().getUsuarioVO(), "HistoricoAlunoRelControle",
					"Inicializando Geração de Relatório Histórico Aluno", "Emitindo Relatório");
			getFacadeFactory().getHistoricoAlunoRelFacade().validarDados(getMatriculaVO(), getTurmaVO(),
					getCampoFiltroPor(), getCampoConsultaTipoDesconto(), getUnidadeEnsinoVO());

			validarDadosHistoricoEscolarDigital();
			if (getListaMatriculas().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
			}
			for (MatriculaVO matricula : getListaMatriculas()) {
				if (getIsFiltrarPorturma() || getIsFiltrarPorProgramacaoFormatura()) {
					getProgressBarVO().setStatus("Processando Matrícula " + (getProgressBarVO().getProgresso() + 1)
							+ " de " + (getProgressBarVO().getMaxValue() - 1) + " ");
				}
				
				if(getIsFiltrarPorProgramacaoFormatura()) {
					getGradeCurricularVO().setCodigo(matricula.getGradeCurricularAtual().getCodigo());
					setUnidadeEnsinoVO(new UnidadeEnsinoVO());
					getUnidadeEnsinoVO().setCodigo(matricula.getUnidadeEnsino().getCodigo());
				}
				setExpedicaoDiplomaVO(
						getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(matricula.getMatricula(),
								false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getUsuarioVO()));
				HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();
				if (tipoLayoutComparar.equals("HistoricoAlunoPos3Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos3ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos2Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos2ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout6Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout8Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos14Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoResidenciaMedicaRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos17Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos17ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos18Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos18ExcelRel")) {
					if (getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().getNome().trim().equals("")) {

						getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().setNome("");
					} else {
						if (tipoLayoutComparar.equals("HistoricoAlunoResidenciaMedicaRel")) {
							getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().setNome(
									getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().getNome());
							histAlunoRelVO.setTituloFuncionarioPrincipal(
									getHistoricoAlunoRelVO().getTituloFuncionarioPrincipal());
						} else {
							getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getPessoa()
									.setNome(getHistoricoAlunoRelVO().getTituloFuncionarioPrincipal() + " "
											+ getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getPessoa()
													.getNome());
						}

					}
					histAlunoRelVO.setFuncionarioPrincipalVO(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO());
				} else {
					histAlunoRelVO.setFuncionarioPrincipalVO(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO());					
					if(getTipoLayoutPersonalizado()) {
						histAlunoRelVO.setTituloFuncionarioPrincipal(getHistoricoAlunoRelVO().getTituloFuncionarioPrincipal());
					}
				}
				if (tipoLayoutComparar.equals("HistoricoAlunoPos3Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos3ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos2Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos2ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout6Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout8Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos14Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoResidenciaMedicaRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos17Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos17ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos18Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos18ExcelRel")) {
					if (getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().getNome().trim().equals("")) {
						getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().setNome("");
					} else {
						if (tipoLayoutComparar.equals("HistoricoAlunoResidenciaMedicaRel")) {
							getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().setNome(
									getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().getNome());
							histAlunoRelVO.setTituloFuncionarioSecundario(
									getHistoricoAlunoRelVO().getTituloFuncionarioSecundario());
						} else {
							getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getPessoa()
									.setNome(getHistoricoAlunoRelVO().getTituloFuncionarioSecundario() + " "
											+ getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getPessoa()
													.getNome());
						}

					}
					histAlunoRelVO.setFuncionarioSecundarioVO(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO());
				} else {
					histAlunoRelVO.setFuncionarioSecundarioVO(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO());
					if(getTipoLayoutPersonalizado()) {
						histAlunoRelVO.setTituloFuncionarioSecundario(getHistoricoAlunoRelVO().getTituloFuncionarioSecundario());
					}
				}
				if (tipoLayoutComparar.equals("HistoricoAlunoPos3Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos3ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos2Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos2ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout6Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout8Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos17Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos17ExcelRel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos18Rel") || tipoLayoutComparar.equals("HistoricoAlunoPos18ExcelRel")) {
					if (getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getPessoa().getNome().trim().equals("")) {
						getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getPessoa().setNome("");
					} else {
						if (tipoLayoutComparar.equals("HistoricoAlunoResidenciaMedicaRel")) {
							getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getPessoa().setNome(
									getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getPessoa().getNome());
							histAlunoRelVO.setTituloFuncionarioTerciario(
									getHistoricoAlunoRelVO().getTituloFuncionarioTerciario());
						} else {
							getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getPessoa()
									.setNome(getHistoricoAlunoRelVO().getTituloFuncionarioTerciario() + " "
											+ getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getPessoa()
													.getNome());
						}

					}
					histAlunoRelVO.setFuncionarioTerciarioVO(getHistoricoAlunoRelVO().getFuncionarioTerciarioVO());
				} else {
					histAlunoRelVO.setFuncionarioTerciarioVO(getHistoricoAlunoRelVO().getFuncionarioTerciarioVO());
					if(getTipoLayoutPersonalizado()) {
						histAlunoRelVO.setTituloFuncionarioTerciario(getHistoricoAlunoRelVO().getTituloFuncionarioTerciario());
					}
				}

				if (Uteis.isAtributoPreenchido(histAlunoRelVO.getFuncionarioPrincipalVO())
						&& Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getCargoFuncionarioPrincipal())
						&& !getSelectItemsCargoFuncionarioPrincipal().isEmpty()) {
					getHistoricoAlunoRelVO()
							.setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
									getHistoricoAlunoRelVO().getCargoFuncionarioPrincipal().getCodigo(), false,
									Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
				} else {
					getHistoricoAlunoRelVO().getCargoFuncionarioPrincipal().setCodigo(0);
				}

				histAlunoRelVO.setCargoFuncionarioPrincipal(getHistoricoAlunoRelVO().getCargoFuncionarioPrincipal());
				histAlunoRelVO.getCargoFuncionarioPrincipal()
						.setPortariaCargo(getHistoricoAlunoRelVO().getCargoFuncionarioPrincipal().getPortariaCargo());

				if (Uteis.isAtributoPreenchido(histAlunoRelVO.getFuncionarioSecundarioVO())
						&& Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getCargoFuncionarioSecundario())
						&& !getSelectItemsCargoFuncionarioSecundario().isEmpty()) {
					getHistoricoAlunoRelVO().setCargoFuncionarioSecundario(
							getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
									getHistoricoAlunoRelVO().getCargoFuncionarioSecundario().getCodigo(), false,
									Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
				} else {
					getHistoricoAlunoRelVO().getCargoFuncionarioSecundario().setCodigo(0);
				}

				histAlunoRelVO.setCargoFuncionarioSecundario(getHistoricoAlunoRelVO().getCargoFuncionarioSecundario());
				histAlunoRelVO.getCargoFuncionarioSecundario()
						.setPortariaCargo(getHistoricoAlunoRelVO().getCargoFuncionarioSecundario().getPortariaCargo());

				if (Uteis.isAtributoPreenchido(histAlunoRelVO.getFuncionarioTerciarioVO())
						&& Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getCargoFuncionarioTerciario())
						&& !getSelectItemsCargoFuncionarioTerciario().isEmpty()) {
					getHistoricoAlunoRelVO()
							.setCargoFuncionarioTerciario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
									getHistoricoAlunoRelVO().getCargoFuncionarioTerciario().getCodigo(), false,
									Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
				} else {
					getHistoricoAlunoRelVO().getCargoFuncionarioTerciario().setCodigo(0);
				}

				histAlunoRelVO.setCargoFuncionarioTerciario(getHistoricoAlunoRelVO().getCargoFuncionarioTerciario());
				histAlunoRelVO.getCargoFuncionarioTerciario()
						.setPortariaCargo(getHistoricoAlunoRelVO().getCargoFuncionarioTerciario().getPortariaCargo());
				getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.TODOS,
						getProgressBarVO().getUsuarioVO());

				getFacadeFactory().getHistoricoAlunoRelFacade().setDescricaoFiltros("");

				if (Uteis.getIsValorNumerico(getTipoLayout())) {
					
					if (getImprimirExcel()) {
						if (getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoExcelPrincipal() == null) {
							throw new Exception("Não foi possível encontrar o modelo do layout principal selecionado ("
									+ getTipoLayout() + ").");
						}
						design = getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoExcelPrincipal() + File.separator
								+ getConfiguracaoLayoutHistoricoSelecionadoVO().getNomeArquivoExcelPrincipal();
					} else {
						if (getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoPdfPrincipal() == null) {
							throw new Exception("Não foi possível encontrar o modelo do layout principal selecionado ("
									+ getTipoLayout() + ").");
						}
						design = getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoPdfPrincipal() + File.separator
								+ getConfiguracaoLayoutHistoricoSelecionadoVO().getNomeArquivoPdfPrincipal();
					}
				} else {
					design = HistoricoAlunoRel.getDesignIReportRelatorio(
							TipoNivelEducacional.getEnum(matriculaVO.getCurso().getNivelEducacional()), getTipoLayout(),
							getImprimirExcel());
				}
				if (getTurmaVO().getTurmaAgrupada()) {
					verificarGradeCurricular(matricula);
				}
				Date dataExpedicaoDiploma = null;
				if (getExpedicaoDiplomaVO().getCodigo().intValue() > 0) {
					dataExpedicaoDiploma = getExpedicaoDiplomaVO().getDataExpedicao();
				}
				if (getIsFiltrarPorturma() || getIsFiltrarPorDesconto() || getIsFiltrarPorProgramacaoFormatura()) {
					matricula.setObservacaoComplementarHistoricoAlunoVO(
							getFacadeFactory().getMatriculaFacade().consultarObservacaoComplementarMatricula(matricula,
									matricula.getGradeCurricularAtual().getCodigo()));
					setObservacaoComplementar(matricula.getObservacaoComplementarHistoricoAlunoVO().getObservacao());
				} else {
					getFacadeFactory().getMatriculaFacade().alterarObservacaoComplementar(matricula,
							getGradeCurricularVO().getCodigo(), this.getObservacaoComplementar(),
							usuarioLogado);
				}
				if (Uteis.isAtributoPreenchido(getTurmaAluno())) {
					matricula.setTurma(getTurmaAluno().getCodigo().toString());
				}
				if (Uteis.isAtributoPreenchido(getTurmaVO())) {
					matricula.setTurma(getTurmaVO().getCodigo().toString());
				}	
				histAlunoRelVO.setTrazerTodosProfessoresDisciplinas(getTrazerTodosProfessoresDisciplinas());
				historicoTemp = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, matricula,
						getGradeCurricularVO(), getFiltroRelatorioAcademicoVO(), getOrdem(),
						getObservacaoComplementar(), getObservacaoComplementarIntegralizado(), getCampoFiltroPor(),
						tipoLayoutComparar, getUtilizarUnidadeEnsinoMatriz(), dataExpedicaoDiploma,
						getApresentarInstituicaoDisciplinaAproveitada(),
						getApresentarDisciplinaAnoSemestreTransferenciaGrade(), getApresentarDisciplinaForaGrade(),
						usuarioLogado, getFiltroDisciplinasACursar(), getDesconsiderarSituacaoMatriculaPeriodo(),
						getApresentarObservacaoTransferenciaMatrizCurricular(),
						getObservacaoTransferenciaMatrizCurricular(), getApresentarApenasUltimoHistoricoDisciplina(),
						getConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista(),
						isApresentarApenasProfessorTitulacaoTurmaOrigem(),
						getRegraApresentacaoProfessorDisciplinaAproveitamento(),
						getDataInicioTerminoAlteracoesCadastrais(), getConfiguracaoLayoutHistoricoSelecionadoVO(), getGerarXmlHistoricoEscolarDigital());

				historicoTemp.setApresentarFrequencia(getApresentarFrequencia());
				for (HistoricoAlunoDisciplinaRelVO histAlunoDiscRelVO : historicoTemp
						.getListaHistoricoAlunoDisciplinaRelVOs()) {
					if (Uteis.isAtributoPreenchido(histAlunoDiscRelVO)) {
						histAlunoDiscRelVO.setApresentarFrequencia(getApresentarFrequencia());
					}

				}
				histAlunoRelVO.setApresentarTopoRelatorio(getApresentarTopoRelatorio());
				
				if (tipoLayout.equals("HistoricoAlunoLayout8Rel")) {
					histAlunoRelVO.setTextoCertidaoEstudo(getTextoCertificadoEstudo());
				}
				if (Uteis.isAtributoPreenchido(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs())) {
					historicoTemp.setLegendaSituacaoHistorico(getFacadeFactory().getHistoricoAlunoRelFacade()
							.realizarCriacaoLegendaSituacaoDisciplinaHistorico(historicoTemp, tipoLayoutComparar));
					if (getFacadeFactory().getHistoricoAlunoRelFacade().validarTipoLayoutGraduacao(tipoLayoutComparar, getConfiguracaoLayoutHistoricoSelecionadoVO())) {
						historicoTemp.setLegendaTitulacaoProfessor((getFacadeFactory().getHistoricoAlunoRelFacade()
								.realizarCriacaoLegendaTitulacaoProfessorDisciplinaHistorico(historicoTemp,
										tipoLayoutComparar)));
					}
				}
				if (getUtilizarUnidadeEnsinoMatriz()) {
					UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade()
							.obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
					if (Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
						setUnidadeEnsinoVO(unidadeEnsinoVO);
						String cidade = "";
						CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorUnidadeEnsinoMatriz(false,
								getProgressBarVO().getUsuarioVO());
						if (cidadeVO != null && cidadeVO.getCodigo() != 0) {
							cidade = cidadeVO.getNome();
						} else {
							cidade = "";
						}
						if (cidade.equals("")) {
							cidade = matricula.getUnidadeEnsino().getCidade().getNome();
						}
						historicoTemp.setDataEmissaoHistorico(tipoLayoutComparar.equals("HistoricoAlunoLayout14Rel")
								? Uteis.getDataCidadeDiaMesPorExtensoEAno("",
										getMatriculaVO().getDataEmissaoHistorico(), true)
								: Uteis.getDataCidadeDiaMesPorExtensoEAno(cidade,
										getMatriculaVO().getDataEmissaoHistorico(), true));
						historicoTemp.setDataEmissaoHistorico(tipoLayoutComparar.equals("HistoricoAlunoLayout18PortariaMECRel")
								? Uteis.getDataCidadeEstadoDiaMesPorExtensoEAno(cidade, cidadeVO.getEstado().getSigla(),
										getMatriculaVO().getDataEmissaoHistorico(), true)
								: Uteis.getDataCidadeDiaMesPorExtensoEAno(cidade,
										getMatriculaVO().getDataEmissaoHistorico(), true));
					} else {
						historicoTemp.setDataEmissaoHistorico(tipoLayoutComparar.equals("HistoricoAlunoLayout14Rel")
								? Uteis.getDataCidadeDiaMesPorExtensoEAno("",
										getMatriculaVO().getDataEmissaoHistorico(), true)
								: Uteis.getDataCidadeDiaMesPorExtensoEAno(
										matricula.getUnidadeEnsino().getCidade().getNome(),
										getMatriculaVO().getDataEmissaoHistorico(), true));
					}
				} else {
					historicoTemp.setDataEmissaoHistorico(tipoLayoutComparar.equals("HistoricoAlunoLayout14Rel")
							? Uteis.getDataCidadeDiaMesPorExtensoEAno("", getMatriculaVO().getDataEmissaoHistorico(),
									true)
							: Uteis.getDataCidadeDiaMesPorExtensoEAno(
									matricula.getUnidadeEnsino().getCidade().getNome(),
									getMatriculaVO().getDataEmissaoHistorico(), true));
				}

				String enderecoUnidadeEnsino = null;
				if (tipoLayoutComparar.equals("HistoricoAlunoLayout11Rel")) {
					enderecoUnidadeEnsino = matricula.getUnidadeEnsino().getEnderecoCompleto() + " "
							+ matricula.getUnidadeEnsino().getCidade().getNome() + " - "
							+ matricula.getUnidadeEnsino().getCidade().getEstado().getSigla();
				} else {
					enderecoUnidadeEnsino = matricula.getUnidadeEnsino().getEnderecoCompleto();
				}
				getSuperParametroRelVO().adicionarParametro("enderecoUnidadeEnsino", enderecoUnidadeEnsino);

				if (getFacadeFactory().getHistoricoAlunoRelFacade().validarTipoLayoutGraduacao(tipoLayoutComparar, getConfiguracaoLayoutHistoricoSelecionadoVO())) {
					Matricula.montarDadosUnidadeEnsino(matriculaVO, NivelMontarDados.TODOS);
					historicoTemp.setNomeUnidadeEnsino(matricula.getUnidadeEnsino().getNome());
					historicoTemp.setCodigoIES(matricula.getUnidadeEnsino().getCodigoIES());
					historicoTemp.setCodigoIESMantenedora(matricula.getUnidadeEnsino().getCodigoIESMantenedora());
					historicoTemp.setMantenedora(matricula.getUnidadeEnsino().getMantenedora());
				}

				historicoAlunoRelVOs.add(historicoTemp);

				TitulacaoCursoVO tc = getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(
						matricula.getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
				if (Uteis.isAtributoPreenchido(tc)) {
					for (ItemTitulacaoCursoVO itc : tc.getItemTitulacaoCursoVOs()) {
						Map<String, List<HistoricoAlunoDisciplinaRelVO>> mapaProfessoresTitulares = historicoTemp
								.getListaHistoricoAlunoDisciplinaRelVOs().stream()
								.collect(Collectors.groupingBy(HistoricoAlunoDisciplinaRelVO::getProfessor));
						Long qtdProfessor = mapaProfessoresTitulares.entrySet().stream().count();
						if (Uteis.isAtributoPreenchido(qtdProfessor)) {
							Long qtdProfessorDoutorOuMestre = mapaProfessoresTitulares.entrySet().stream().filter(p -> p
									.getValue().get(0).getTitulacaoProfessor().equals(itc.getTitulacao())
									|| p.getValue().get(0).getTitulacaoProfessor().equals(itc.getSegundaTitulacao())
									|| ((itc.getTitulacao().equals("DR") || itc.getSegundaTitulacao().equals("DR"))
											&& p.getValue().get(0).getTitulacaoProfessor().equals("DRA")) // atender
																											// qnd o
																											// sexo
																											// feminino
									|| ((itc.getTitulacao().equals("GR") || itc.getSegundaTitulacao().equals("GR"))
											&& p.getValue().get(0).getTitulacaoProfessor().equals("GRA")) // atender
																											// qnd o
																											// sexo
																											// feminino
									|| ((itc.getTitulacao().equals("EP") || itc.getSegundaTitulacao().equals("EP"))
											&& p.getValue().get(0).getTitulacaoProfessor().equals("ESP"))) // aqui
																											// nao
																											// sei
																											// disser
																											// o pq
																											// Pedro
																											// Andrade.
									.count();
							Double porcentagemProfessoresTitulacao = Uteis.arrendondarForcando2CadasDecimais(
									((qtdProfessorDoutorOuMestre * 100) / qtdProfessor));
							if (itc.getQuantidade() > porcentagemProfessoresTitulacao) {
								setMsgProfessorTitulacao("A Titulação no histórico do aluno tem apenas "
										+ Uteis.arrendondarForcando2CadasDecimaisStrComSepador(
												porcentagemProfessoresTitulacao, ",")
										+ "% de " + itc.getTitulacao_Apresentar()
										+ ". Não respeitando a regra definida na Titulação de Curso de "
										+ Uteis.arrendondarForcando2CadasDecimaisStrComSepador(itc.getQuantidade(), ",")
										+ "%.");
								break;
							}
						}
					}
				}
				if ((tipoLayoutComparar.equals("HistoricoAlunoLayout10Rel") || (Uteis.isAtributoPreenchido(getConfiguracaoLayoutHistoricoSelecionadoVO()) && (getConfiguracaoLayoutHistoricoSelecionadoVO().getConfiguracaoHistoricoVO().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR) || getConfiguracaoLayoutHistoricoSelecionadoVO().getConfiguracaoHistoricoVO().getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA)))) && getApresentarMediaNotasSemestreAnteriorAluno()) {
					int indexPenultimoSemestre = 0;
					matricula.getUltimoMatriculaPeriodoVO();
					if (matricula.getMatriculaPeriodoVOs().size() >= 2) {
						indexPenultimoSemestre = matricula.getMatriculaPeriodoVOs().size() - 2;
						MatriculaPeriodoVO penultimoMatriculaPeriodoVO = matricula.getMatriculaPeriodoVOs()
								.get(indexPenultimoSemestre);
						getSuperParametroRelVO().adicionarParametro("ultimoPeriodo",
								penultimoMatriculaPeriodoVO.getAno() + "/" + penultimoMatriculaPeriodoVO.getSemestre());
						getSuperParametroRelVO().adicionarParametro("mediaNotasUltimoSemestre",
								getFacadeFactory().getHistoricoAlunoRelFacade().calcularMediaNotasSemestreAluno(
										historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs(),
										penultimoMatriculaPeriodoVO));
					} else {
						getSuperParametroRelVO().adicionarParametro("ultimoPeriodo", "");
						getSuperParametroRelVO().adicionarParametro("mediaNotasUltimoSemestre", 0);
					}
				} else {
					getSuperParametroRelVO().adicionarParametro("ultimoPeriodo", "");
					getSuperParametroRelVO().adicionarParametro("mediaNotasUltimoSemestre", 0);
				}
				if (tipoLayoutComparar.equals("HistoricoAlunoPos3Rel")
						|| tipoLayoutComparar.equals("HistoricoAlunoPos17Rel") || (Uteis.isAtributoPreenchido(getConfiguracaoLayoutHistoricoSelecionadoVO()) && getConfiguracaoLayoutHistoricoSelecionadoVO().getConfiguracaoHistoricoVO().getNivelEducacional().equals(TipoNivelEducacional.POS_GRADUACAO))) {
					String turmaUltimoMatriculaPeriodo = getFacadeFactory().getTurmaFacade()
							.consultaRapidaPorMatriculaUltimaMatriculaPeriodo(matricula.getMatricula(), getUsuario())
							.getIdentificadorTurma();
					getSuperParametroRelVO().adicionarParametro("turmaUltimoMatriculaPeriodo",
							turmaUltimoMatriculaPeriodo);
				}
				if (getIsFiltrarPorturma() || getIsFiltrarPorProgramacaoFormatura()) {
					getProgressBarVO().incrementar();
				}

			}
			

			List<File> files = new ArrayList<File>(0);
			for (HistoricoAlunoRelVO historicoAlunoRelVO : historicoAlunoRelVOs) {
				try {
					Integer totalizadorCreditoDisciplina = 0;
					Integer totalizadorCreditoEstagio = 0;
					Integer totalizadorCreditoObrigatorioEstagio = 0;
					if (tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel")) {
						for (HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO : historicoAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs()) {
							if (!historicoAlunoDisciplinaRelVO.getDisciplinaEstagio()) {
								if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getCrDisciplina())) {
									totalizadorCreditoDisciplina += Integer.parseInt(historicoAlunoDisciplinaRelVO.getCrDisciplina());
								}
							} else {
								if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getCrDisciplina())) {
									totalizadorCreditoEstagio += Integer.parseInt(historicoAlunoDisciplinaRelVO.getCrDisciplina());
								}
								totalizadorCreditoObrigatorioEstagio += historicoAlunoDisciplinaRelVO.getNrCreditos();
							}

						}

					}
					if (getImprimirExcel()) {
						getProgressBarVO().setStatus("Gerando Excel");
					} else {
						getProgressBarVO().setStatus("Gerando PDF");
					}
					getSuperParametroRelVO().setNomeDesignIreport(design);
					if (Uteis.getIsValorNumerico(getTipoLayout())) {

						if (getImprimirExcel()) {
							getSuperParametroRelVO().setSubReport_Dir(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoExcelPrincipal() + File.separator);
							getSuperParametroRelVO().setCaminhoBaseRelatorio(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoExcelPrincipal() + File.separator);
						} else {
							getSuperParametroRelVO().setSubReport_Dir(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoPdfPrincipal() + File.separator);
							getSuperParametroRelVO().setCaminhoBaseRelatorio(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConfiguracaoLayoutHistoricoSelecionadoVO().getPastaBaseArquivoPdfPrincipal() + File.separator);
						}

						getSuperParametroRelVO().adicionarParametro("configuracaoLayoutHistoricoVO", getConfiguracaoLayoutHistoricoSelecionadoVO());
					} else {
						getSuperParametroRelVO().setSubReport_Dir(HistoricoAlunoRel.getCaminhoBaseRelatorio());
						getSuperParametroRelVO().setCaminhoBaseRelatorio(HistoricoAlunoRel.getCaminhoBaseRelatorio());
					}
					getSuperParametroRelVO().setNomeUsuario(getProgressBarVO().getUsuarioVO().getNome());
					getSuperParametroRelVO().setListaObjetos(Collections.singletonList(historicoAlunoRelVO));
					getSuperParametroRelVO().setQuantidade(1);
					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
					UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getUsuarioVO());
					if (obj.getNomeExpedicaoDiploma().trim().isEmpty()) {
						getSuperParametroRelVO().setUnidadeEnsino(obj.getNome());
					} else {
						getSuperParametroRelVO().setUnidadeEnsino(obj.getNomeExpedicaoDiploma());
					}
					if (tipoLayoutComparar.equals("HistoricoAlunoLayout9Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout9PortariaMECRel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel") || Uteis.getIsValorNumerico(getTipoLayout())) {
						getSuperParametroRelVO().getParametros().put("logoMunicipio", getProgressBarVO().getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo() + File.separator + obj.getCaminhoBaseLogoMunicipio().replaceAll("\\\\", "/") + File.separator + obj.getNomeArquivoLogoMunicipio());
					}
					if (tipoLayoutComparar.equals("HistoricoAlunoLayout15Rel") || tipoLayoutComparar.equals("HistoricoAlunoLayout15PortariaMECRel")) {
						getSuperParametroRelVO().setNomeEmpresa("MUNICÍPIO DE " + obj.getCidade().getNome().toUpperCase() + " - ESTADO DO " + obj.getCidade().getEstado().getNome().toUpperCase());
						String informacoesAdicionaisEndereco = obj.getInformacoesAdicionaisEndereco().replace("|", "\n");
						getSuperParametroRelVO().getParametros().put("informacoesCabecalho", informacoesAdicionaisEndereco);
					}
					if (getImprimirExcel()) {
						// apresentarRelatorioObjetos(HistoricoAlunoRel.getIdEntidadePos2Excel(),
						// titulo, nomeEmpresa, "", "EXCEL", "", design,
						// getUsuarioLogado().getNome(),
						// getFacadeFactory().getHistoricoAlunoRelFacade().getDescricaoFiltros(),
						// historicoAlunoRelVOs,
						// HistoricoAlunoRel.getCaminhoBaseRelatorio());
						getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
					} else {
						getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
						// apresentarRelatorioObjetos(HistoricoAlunoRel.getIdEntidade(),
						// titulo, nomeEmpresa, "", "PDF", "", design,
						// getUsuarioLogado().getNome(),
						// getFacadeFactory().getHistoricoAlunoRelFacade().getDescricaoFiltros(),
						// historicoAlunoRelVOs,
						// HistoricoAlunoRel.getCaminhoBaseRelatorio());
					}
					getSuperParametroRelVO().adicionarParametro("assinarDigitalmente", isAssinarDigitalmente());
					if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado())) && getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()).getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario()) {
						getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioPrincipal", getUrlAssinaturaFuncionarioPrincipal());
						getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioSecundario", getUrlAssinaturaFuncionarioSecundario());
						getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioTerciario", getUrlAssinaturaFuncionarioTerciario());
					} else {
						getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioPrincipal", "");
						getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioSecundario", "");
						getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioTerciario", "");
					}
					getSuperParametroRelVO().adicionarParametro("apresentarInstituicaoDisciplinaAproveitada", getApresentarInstituicaoDisciplinaAproveitada());
					getSuperParametroRelVO().adicionarParametro("apresentarDisciplinaPeriodoTrancadoCanceladoTransferido", getApresentarDisciplinaPeriodoTrancadoCanceladoTransferido());
					getSuperParametroRelVO().adicionarParametro("apresentarCargaHorariaDisciplina", isApresentarCargaHorariaDisciplina());
					getSuperParametroRelVO().adicionarParametro("ordenarPor", getOrdem());
					if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo())) {
						if (getIsFiltrarPorturma()) {
							setCaminhoPastaWeb(getProgressBarVO().getCaminhoWebRelatorio());
						}
						setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getUsuarioVO()));
						if (!getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio().equals("") && !getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio().equals("")) {
							if (getIsFiltrarPorturma()) {
								getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getProgressBarVO().getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo() + File.separator + getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio() + File.separator + getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio());
							} else {
								getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio() + File.separator + getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio());
							}
						} else {
							getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
						}
						UnidadeEnsinoVO unidadeEnsinoMatriz = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
						getSuperParametroRelVO().adicionarParametro("enderecoCompletoUnidadeMatriz", unidadeEnsinoMatriz.getEndereco() + ",  " + unidadeEnsinoMatriz.getNumero() + " - " + unidadeEnsinoMatriz.getSetor() + " - " + unidadeEnsinoMatriz.getCEP() + " " + unidadeEnsinoMatriz.getCidade().getNome() + " - " + unidadeEnsinoMatriz.getCidade().getEstado().getSigla() + " fone: " + unidadeEnsinoMatriz.getTelComercial1());
						if (tipoLayout.equals("HistoricoAlunoPos18Rel") || tipoLayout.equals("HistoricoAlunoLayout24Rel") || tipoLayout.equals("HistoricoAlunoLayout24ExcelRel") || tipoLayout.equals("HistoricoAlunoLayout24PortariaMecRel") || tipoLayout.equals("HistoricoAlunoLayout19ExtRel")) {
							getSuperParametroRelVO().adicionarParametro("brasaoArmasEstadoSaoPauloPretoBranco", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "brasaoArmasEstadoSaoPauloPretoBranco.jpg");
							getSuperParametroRelVO().adicionarParametro("simboloBandeiraPretoBranco", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "simboloBandeiraPretoBranco.jpg");
							getSuperParametroRelVO().adicionarParametro("marcaDaguaUnivesp", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "marcaDaguaUnivesp.png");
						}
					}
					if (totalizadorCreditoDisciplina > 0) {
						getSuperParametroRelVO().adicionarParametro("totalizadorCreditoDisciplina", totalizadorCreditoDisciplina.toString());
					}
					if (totalizadorCreditoEstagio > 0) {
						getSuperParametroRelVO().adicionarParametro("totalizadorCreditoEstagio", totalizadorCreditoEstagio.toString());
					}
					if (totalizadorCreditoObrigatorioEstagio > 0) {
						getSuperParametroRelVO().adicionarParametro("totalizadorCreditoObrigatorioEstagio", totalizadorCreditoObrigatorioEstagio.toString());
					}
					getSuperParametroRelVO().adicionarParametro("filtrocancelado", getFiltroRelatorioAcademicoVO().getCanceladoHistorico());
					if (isAssinarDigitalmente()) {
						montarDadosAssinaturaDigitalFuncionario();
					} else {
						getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
						getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
						getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", false);
					}
					getSuperParametroRelVO().adicionarParametro("matriculaAluno", historicoAlunoRelVO.getMatriculaVO());
					realizarImpressaoRelatorio();
					persistirParametroRelatorio();
					persistirLayoutPadrao(getTipoLayout());
					boolean isHistoricoGeradoViaRequerimento = Uteis.isAtributoPreenchido(getRequerimentoVO());

					if (getGerarXmlHistoricoEscolarDigital()) {
						String caminhoBasePdf = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator;
						String caminhoPdf = caminhoBasePdf + getCaminhoRelatorio();
						realizarGeracaoXmlHistorico(historicoAlunoRelVO, new File(caminhoPdf), usuarioLogado);
					} else if (isAssinarDigitalmente() && !getImprimirExcel()) {
						setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorHistoricoAluno(getCaminhoRelatorio(), getMatriculaVO(), getGradeCurricularVO(), getTurmaVO(), null, "", "", isHistoricoGeradoViaRequerimento ? TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO : TipoOrigemDocumentoAssinadoEnum.HISTORICO, getProvedorDeAssinaturaEnum(), "#000000", 80f, 200f, null, "", "", null, "", "", null, "", "", getConfiguracaoGeralPadraoSistema(), isHistoricoGeradoViaRequerimento ? getRequerimentoVO().getCodigo() : null, getUsuarioLogado()));
						getListaDocumentoAsssinados().clear();
						inicializarDadosAssinaturaHistoricoAluno();
					} else if (isAssinarDigitalmente() && getImprimirExcel()) {
						montarDadosAssinaturaDigitalFuncionario();
					}

					String caminhoBasePdf = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator;
					String caminhoPdf = caminhoBasePdf + getCaminhoRelatorio();
					files.add(new File(caminhoPdf));
					registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoAlunoRelControle", "Finalizando Geração de Relatório Histórico Aluno", "Emitindo Relatório");
					// montarListaSelectItemUnidadeEnsino();
					// if (getIsFiltrarPorAluno()) {
					// montarListaSelectItemGradeCurricularAluno();
					// } else {
					// montarListaSelectItemGradeCurricularTurma();
					// }
				} catch (ConsistirException conExce) {
					if (!(getIsFiltrarPorProgramacaoFormatura() && getGerarXmlHistoricoEscolarDigital())) {
						historicoAlunoRelVO.setConsistirException(conExce);
						if (Uteis.isAtributoPreenchido(conExce.getListaMensagemErro())) {
							setListaErroHistoricoEscolarDigital(conExce.getListaMensagemErro());
							setOncomplete("RichFaces.$('panelListaErroGeracaoDiploma').show();");
							setMensagemDetalhada("msg_erro", "Não Foi Possível Concluir Essa Operação", Uteis.ERRO);
						}
					} else {
						if (!Uteis.isAtributoPreenchido(conExce.getListaMensagemErro())) {
							conExce.getListaMensagemErro().add(conExce.getMessage());
						}
						historicoAlunoRelVO.setConsistirException(conExce);
						getListaErroHistoricoAlunoRel().add(historicoAlunoRelVO);
					}
				} catch (Exception exce) {
					if (!(getIsFiltrarPorProgramacaoFormatura() && getGerarXmlHistoricoEscolarDigital())) {
						throw exce;
					} else {
						ConsistirException consistirException = new ConsistirException();
						consistirException.getListaMensagemErro().add(exce.getMessage());
						historicoAlunoRelVO.setConsistirException(consistirException);
						getListaErroHistoricoAlunoRel().add(historicoAlunoRelVO);
					}
				}
			}
			if (getIsFiltrarPorturma() || getIsFiltrarPorProgramacaoFormatura()) {
				getProgressBarVO().incrementar();
			}
			if (getIsFiltrarPorProgramacaoFormatura() && getGerarXmlHistoricoEscolarDigital()) {
				setFazerDownload(Boolean.FALSE);
				setCaminhoRelatorio(Constantes.EMPTY);
				if (Uteis.isAtributoPreenchido(getListaErroHistoricoAlunoRel())) {
					setOncomplete("RichFaces.$('panelListaErroGeracaoDiplomaLote').show();");
					getProgressBarVO().setForcarEncerramento(true);
				} else {
					setOncomplete(Constantes.EMPTY);
					setListaErroHistoricoAlunoRel(new ArrayList<>());
					setListaErroHistoricoEscolarDigital(new ArrayList<>(0));
					setMensagemID("msg_relatorio_ok", Uteis.SUCESSO, Boolean.TRUE);
				}
			} else {
				if (getUtilizarUnidadeEnsinoMatriz()) {
					setUnidadeEnsinoVO(getIsFiltrarPorAluno() ? getMatriculaVO().getUnidadeEnsino()
							: getListaMatriculas().get(0).getUnidadeEnsino());
				}
				if(files.size() > 1) {
					String nomeNovoArquivo = "";
					if(getIsFiltrarPorProgramacaoFormatura()) {
						nomeNovoArquivo = getProgramacaoFormaturaVO().getCodigo()+"_"+usuarioLogado.getCodigo()+""+new Date().getTime();
					}else {
						nomeNovoArquivo = getTurmaVO().getCodigo()+"_"+usuarioLogado.getCodigo()+""+new Date().getTime();
					}
					
					nomeNovoArquivo += ".zip";
					File[] filesArray = new File[files.size()];
					getFacadeFactory().getArquivoHelper().zip(files.toArray(filesArray), new File( getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + nomeNovoArquivo));
					setCaminhoRelatorio(nomeNovoArquivo);					
				}
				if (isAssinarDigitalmente() && !getIsFiltrarPorProgramacaoFormatura()) {
					inicializarDadosAssinaturaHistoricoAluno();
				}
				if (!Uteis.isAtributoPreenchido(getListaErroHistoricoEscolarDigital())) {
					setMensagemID("msg_relatorio_ok", Uteis.SUCESSO, Boolean.TRUE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			getProgressBarVO().setForcarEncerramento(true);
		} finally {
			historicoTemp = null;
			// titulo = null;
			// nomeEmpresa = null;

			design = null;
			Uteis.liberarListaMemoria(historicoAlunoRelVOs);
			setSuperParametroRelVO(null);
			setImprimirExcel(false);
			recuperarNomeFuncionarios();
			this.setUnidadeEnsinoVO(getMatriculaVO().getUnidadeEnsino());
			getProgressBarVO().setForcarEncerramento(true);
		}
	}

	public void inicializarDadosAssinaturaHistoricoAluno() throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		if (getCampoFiltroPor().equals("aluno")) {
			obj.setMatricula(getMatriculaVO());
		} else {
			obj.getTurma().setCodigo(getTurmaVO().getCodigo());
		}

		setListaDocumentoAsssinados(getFacadeFactory().getDocumentoAssinadoFacade()
				.consultarDocumentosAssinadoPorRelatorio(obj, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), Arrays.asList(TipoOrigemDocumentoAssinadoEnum.HISTORICO, TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL), null));
	}

	private void montarDadosAssinaturaDigitalFuncionario() throws Exception {
		ConfiguracaoGEDVO configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade()
				.consultarPorUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
		if (!Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
			return;
		}
//		FUNCIONÁRIO PRIMÁRIO
		if (Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo())) {
			getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().setArquivoAssinaturaVO(
					getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(
							getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(
					getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioPrimario",
						getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator
								+ getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getArquivoAssinaturaVO()
										.getPastaBaseArquivoEnum().getValue()
								+ File.separator + getHistoricoAlunoRelVO().getFuncionarioPrincipalVO()
										.getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario",
						configGEDVO.getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
			}
		} else {
			getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
		}
//		FUNCIONÁRIO SECUNDÁRIO
		if (Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo())) {
			getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().setArquivoAssinaturaVO(
					getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(
							getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(
					getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioSecundario",
						getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator
								+ getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO()
										.getPastaBaseArquivoEnum().getValue()
								+ File.separator + getHistoricoAlunoRelVO().getFuncionarioSecundarioVO()
										.getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario",
						configGEDVO.getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
			}
		} else {
			getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
		}
//		FUNCIONÁRIO TERCIÁRIO
		if (Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getCodigo())) {
			getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().setArquivoAssinaturaVO(
					getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(
							getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(
					getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioTerciario",
						getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator
								+ getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getArquivoAssinaturaVO()
										.getPastaBaseArquivoEnum().getValue()
								+ File.separator + getHistoricoAlunoRelVO().getFuncionarioTerciarioVO()
										.getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario",
						configGEDVO.getConfiguracaoGedHistoricoVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", false);
			}
		} else {
			getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", false);
		}
	}

	private void persistirLayoutPadrao(String valor) throws Exception {
		String key = "HistoricoAlunoRel";
		if (Uteis.isAtributoPreenchido(getConfiguracaoHistoricoVO())) {
			key = "HISTORICOREL_" + getConfiguracaoHistoricoVO().getNivelEducacional().getValor();
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, key, "designRelatorio",
					getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(),
					getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(),
					getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getCodigo(), getApresentarTopoRelatorio(),
					getHistoricoAlunoRelVO().getTituloFuncionarioPrincipal(),
					getHistoricoAlunoRelVO().getTituloFuncionarioSecundario(),
					getHistoricoAlunoRelVO().getTituloFuncionarioTerciario(), getObservacaoComplementarIntegralizado(),
					getTextoCertificadoEstudo(),getUsuarioLogado(), "", "");
		} else {

			if (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "historico",
						"designRelatorioPos", getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(),
						getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(),
						getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getCodigo(), getApresentarTopoRelatorio(),
						getHistoricoAlunoRelVO().getTituloFuncionarioPrincipal(),
						getHistoricoAlunoRelVO().getTituloFuncionarioSecundario(),
						getHistoricoAlunoRelVO().getTituloFuncionarioTerciario(),
						getObservacaoComplementarIntegralizado(), getTextoCertificadoEstudo(), getUsuarioLogado(), "",
						"");
			} else {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "historico",
						"designRelatorioGraduacao", getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(),
						getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(),
						getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getCodigo(), getApresentarTopoRelatorio(),
						getHistoricoAlunoRelVO().getTituloFuncionarioPrincipal(),
						getHistoricoAlunoRelVO().getTituloFuncionarioSecundario(),
						getHistoricoAlunoRelVO().getTituloFuncionarioTerciario(),
						getObservacaoComplementarIntegralizado(), getTextoCertificadoEstudo(), getUsuarioLogado(), "",
						"");
			}
		}
		getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoHistorico(getFiltroRelatorioAcademicoVO(), key, getUsuarioLogado());
		persistirLayoutPadraoSuperRelatorio(key);
	}

	public void receberDadosDiploma(MatriculaVO matricula, GradeCurricularVO grade,
			FuncionarioVO funcionarioPrincipalVO, FuncionarioVO funcionarioSecundarioVO,
			CargoVO cargoFuncionarioPrincipal, CargoVO cargoFuncionarioSecundario) {
		this.historicoAlunoRelVO = new HistoricoAlunoRelVO();
		this.historicoAlunoRelVO.setFuncionarioPrincipalVO(funcionarioPrincipalVO);
		this.historicoAlunoRelVO.setFuncionarioSecundarioVO(funcionarioSecundarioVO);
		this.historicoAlunoRelVO.setCargoFuncionarioPrincipal(cargoFuncionarioPrincipal);
		this.historicoAlunoRelVO.setCargoFuncionarioSecundario(cargoFuncionarioSecundario);
		setUnidadeEnsinoVO(matricula.getUnidadeEnsino());
		setFiltro(1);
		setOrdem(1);
		setMatriculaVO(matricula);
		setGradeCurricularVO(grade);
		imprimirPDF();
	}

	public void receberDadosDiplomaVersoHistorico(MatriculaVO matricula, GradeCurricularVO grade,
			FuncionarioVO funcionarioPrincipalVO, FuncionarioVO funcionarioSecundarioVO,
			CargoVO cargoFuncionarioPrincipal, CargoVO cargoFuncionarioSecundario, Date dataExpedicaoDiploma) {
		this.historicoAlunoRelVO = new HistoricoAlunoRelVO();
		this.historicoAlunoRelVO.setFuncionarioPrincipalVO(funcionarioPrincipalVO);
		this.historicoAlunoRelVO.setFuncionarioSecundarioVO(funcionarioSecundarioVO);
		this.historicoAlunoRelVO.setCargoFuncionarioPrincipal(cargoFuncionarioPrincipal);
		this.historicoAlunoRelVO.setCargoFuncionarioSecundario(cargoFuncionarioSecundario);
		setUnidadeEnsinoVO(matricula.getUnidadeEnsino());
		setDataExpedicaoDiploma(dataExpedicaoDiploma);
		setFiltro(1);
		setOrdem(1);
		setMatriculaVO(matricula);
		setGradeCurricularVO(grade);
		setTipoLayout("HistoricoAlunoPosPaisagemRel");
		imprimirPDF();
	}

	public void receberDadosDiplomaVerso(MatriculaVO matricula, GradeCurricularVO grade,
			FuncionarioVO funcionarioPrincipalVO, FuncionarioVO funcionarioSecundarioVO,
			CargoVO cargoFuncionarioPrincipal, CargoVO cargoFuncionarioSecundario) {
		this.historicoAlunoRelVO = new HistoricoAlunoRelVO();
		this.historicoAlunoRelVO.setFuncionarioPrincipalVO(funcionarioPrincipalVO);
		this.historicoAlunoRelVO.setFuncionarioSecundarioVO(funcionarioSecundarioVO);
		this.historicoAlunoRelVO.setCargoFuncionarioPrincipal(cargoFuncionarioPrincipal);
		this.historicoAlunoRelVO.setCargoFuncionarioSecundario(cargoFuncionarioSecundario);
		setUnidadeEnsinoVO(matricula.getUnidadeEnsino());
		setFiltro(1);
		setOrdem(1);
		setMatriculaVO(matricula);
		setGradeCurricularVO(grade);
		// setTipoLayout("HistoricoAlunoVersoDiplomaRel");
		setTipoLayout("HistoricoAlunoVersoDiplomaRel2");
		imprimirPDF();
	}

	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(),
						"FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(
						getValorConsultaFuncionario(), 0, true, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(),
						0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(),
						"FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(),
						0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(
						getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(
						getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID",
					"msg_dados_consultados");
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionarioPrincipal() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioPrincipal");
		getHistoricoAlunoRelVO().setFuncionarioPrincipalVO(obj);
		consultarFuncionarioPrincipal();
	}

	public void selecionarFuncionarioSecundario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioSecundario");
		getHistoricoAlunoRelVO().setFuncionarioSecundarioVO(obj);
		consultarFuncionarioSecundario();
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (this.getUnidadeEnsinoVO().getCodigo() != 0) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(),
							this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("registroAcademico")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(),
							this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
							this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
							this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemGradeCurricularAluno() {
		List<GradeCurricularVO> grades = null;
		try {
			grades = getFacadeFactory().getGradeCurricularFacade().consultarGradeAtualGradeAntigaPorMatriculaAluno(
					getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(grades, "codigo", "nome", false));
			getGradeCurricularVO().setCodigo(getMatriculaVO().getGradeCurricularAtual().getCodigo());
			Collections.reverse(getListaSelectItemGradeCurricular());

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(grades);
		}
	}

	public void consultarFuncionarioPrincipal() throws Exception {
		try {
			getHistoricoAlunoRelVO().setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(
					getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getMatricula()));
			setSelectItemsCargoFuncionarioPrincipal(montarComboCargoFuncionario(
					getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(
							getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioSecundario() throws Exception {
		try {
			getHistoricoAlunoRelVO().setFuncionarioSecundarioVO(consultarFuncionarioPorMatricula(
					getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getMatricula()));
			setSelectItemsCargoFuncionarioSecundario(montarComboCargoFuncionario(
					getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(
							getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
		FuncionarioVO funcionarioVO = null;
		try {
			funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(funcionarioVO)) {
				return funcionarioVO;
			} else {
				setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new FuncionarioVO();
	}

	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		selecionarAluno(obj);
	}

	public void selecionarAluno(MatriculaVO obj) {
		try {
			listaTipoLayout = null;
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
					obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaVO(obj);
			getUnidadeEnsinoVO().setCodigo(obj.getUnidadeEnsino().getCodigo());

			montarDataExpedicaoDiploma(obj.getMatricula());

			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade()
					.consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false,
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (matriculaPeriodo == null) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			montarListaSelectItemGradeCurricularAluno();
			setTurmaAluno(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(getMatriculaVO().getMatricula(), getUsuarioLogado()));
			setGradeAtualAluno(getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularUltimaMatriculaPeriodo(getMatriculaVO().getMatricula(), getGradeCurricularVO().getCodigo(), getUsuarioLogado()));
			obj.setObservacaoComplementarHistoricoAlunoVO(getFacadeFactory().getMatriculaFacade().consultarObservacaoComplementarMatricula(obj, getGradeCurricularVO().getCodigo()));
			setObservacaoComplementar(obj.getObservacaoComplementarHistoricoAlunoVO().getObservacao());
			setObservacaoTransferenciaMatrizCurricular(obj.getObservacaoComplementarHistoricoAlunoVO().getObservacaoTransferenciaMatrizCurricular());
			getListaTipoLayout();
			verificarLayoutPadrao();
			verificarParametroRelatoricoAluno();
			valorConsultaAluno = "";
			campoConsultaAluno = "";
			getListaConsultaAluno().clear();
			montarListaUnidadeCertificadora();
			verificarConfiguracaoDiplomaExistente(null, obj);
			setOncomplete(Constantes.EMPTY);
			setListaErroHistoricoEscolarDigital(new ArrayList<>());
			setMensagemID("msg_dados_consultados");
			verificarParametroRelatorio();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método que ao selecionar uma pessoa para geração do histórico, verifica se já
	 * existe uma preferência de layout para determinado relatório.
	 * 
	 * @throws Exception
	 */
	private void verificarLayoutPadrao() throws Exception {
		String key = "HistoricoAlunoRel";
		LayoutPadraoVO layoutPadraoVO = null;
		realizarDefinicaoConfiguracaoHistoricoUsar();
		if (Uteis.isAtributoPreenchido(getConfiguracaoHistoricoVO())) {
			key = "HISTORICOREL_" + getConfiguracaoHistoricoVO().getNivelEducacional().getValor();	
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(key, "designRelatorio", false, getUsuarioLogado());
		}else {
			if (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("historico",
					"designRelatorioPos", false, getUsuarioLogado());
			} else {
				layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("historico",
					"designRelatorioGraduacao", false, getUsuarioLogado());
			}
		}
		if (!layoutPadraoVO.getValor().equals("")) {
			setTipoLayout(layoutPadraoVO.getValor());
			inicializarDadosSelecaoLayout();
			if (layoutPadraoVO.getAssinaturaFunc1() != 0) {
				getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().setCodigo(layoutPadraoVO.getAssinaturaFunc1());
				getHistoricoAlunoRelVO().setTituloFuncionarioPrincipal(layoutPadraoVO.getTituloAssinaturaFunc1());
				getFacadeFactory().getFuncionarioFacade()
						.carregarDados(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO(), getUsuarioLogado());
				consultarFuncionarioPrincipal();
			}
			if (layoutPadraoVO.getAssinaturaFunc2() != 0) {
				getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().setCodigo(layoutPadraoVO.getAssinaturaFunc2());
				getHistoricoAlunoRelVO().setTituloFuncionarioSecundario(layoutPadraoVO.getTituloAssinaturaFunc2());
				getFacadeFactory().getFuncionarioFacade()
						.carregarDados(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO(), getUsuarioLogado());
				consultarFuncionarioSecundario();
			}
			if (layoutPadraoVO.getAssinaturaFunc3() != 0) {
				getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().setCodigo(layoutPadraoVO.getAssinaturaFunc2());
				getHistoricoAlunoRelVO().setTituloFuncionarioTerciario(layoutPadraoVO.getTituloAssinaturaFunc2());
				getFacadeFactory().getFuncionarioFacade()
						.carregarDados(getHistoricoAlunoRelVO().getFuncionarioTerciarioVO(), getUsuarioLogado());
				consultarFuncionarioTerciario();
			}
			if (layoutPadraoVO.getApresentarTopoRelatorio() != null) {
				setApresentarTopoRelatorio(layoutPadraoVO.getApresentarTopoRelatorio());
			}
			if (!Uteis.isAtributoPreenchido(getConfiguracaoHistoricoVO())) {
				setObservacaoComplementarIntegralizado(layoutPadraoVO.getObservacaoComplementarIntegralizado());
				setTextoCertificadoEstudo(layoutPadraoVO.getTextoCertidaoEstudo());
			}
		}
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setMatricula(getMatriculaVO());
		obj.setGradecurricular(getGradeCurricularVO());
		setListaDocumentoAsssinados(getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPorRelatorio(obj, false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), Arrays.asList(TipoOrigemDocumentoAssinadoEnum.HISTORICO, TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL), null));
		getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoHistorico(getFiltroRelatorioAcademicoVO(), key, getUsuarioLogado());
		
		// verificarLayoutPadraoSuperRelatorio("HistoricoAlunoRel");

	}

	public void selecionarAlunoVindoOutraTela(MatriculaVO obj) throws Exception {
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
				obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatriculaVO(obj);
		setObservacaoComplementar(getFacadeFactory().getMatriculaFacade()
				.consultarObservacaoComplementarMatricula(obj, getGradeCurricularVO().getCodigo()).getObservacao());
		montarListaSelectItemGradeCurricularAluno();
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
		verificarParametroRelatoricoAluno();
	}

	public List<SelectItem> montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
		try {
			if (cargos != null && !cargos.isEmpty()) {
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				for (FuncionarioCargoVO funcionarioCargoVO : cargos) {
					selectItems.add(new SelectItem(funcionarioCargoVO.getCargo().getCodigo(),
							funcionarioCargoVO.getCargo().getNome() + " - "
									+ funcionarioCargoVO.getUnidade().getNome()));
					removerObjetoMemoria(funcionarioCargoVO);
				}
				return selectItems;
			} else {
				setMensagemDetalhada("O Funcionário selecionado não possui cargo configurado");
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(cargos);
		}

	}

	public void consultarObservacaoComplementar() throws Exception {
		try {
			setObservacaoComplementar(getFacadeFactory().getMatriculaFacade()
					.consultarObservacaoComplementarMatricula(getMatriculaVO(), getGradeCurricularVO().getCodigo())
					.getObservacao());
			setGradeAtualAluno(
					getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularUltimaMatriculaPeriodo(
							getMatriculaVO().getMatricula(), getGradeCurricularVO().getCodigo(), getUsuarioLogado()));
			if (!getGradeAtualAluno()) {
				setApresentarDisciplinaAnoSemestreTransferenciaGrade(Boolean.FALSE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
					getMatriculaVO().getMatricula(), this.getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO,
					getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade()
					.consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(objAluno.getMatricula(), false,
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (matriculaPeriodo == null) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getUnidadeEnsinoVO().setCodigo(objAluno.getUnidadeEnsino().getCodigo());
			montarListaSelectItemGradeCurricularAluno();
			setTurmaAluno(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(
					getMatriculaVO().getMatricula(), getUsuarioLogado()));
			setUnidadeEnsinoAluno(getMatriculaVO().getUnidadeEnsino());
	 		setGradeAtualAluno(
					getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularUltimaMatriculaPeriodo(
							getMatriculaVO().getMatricula(), getGradeCurricularVO().getCodigo(), getUsuarioLogado()));
			objAluno.setObservacaoComplementarHistoricoAlunoVO(getFacadeFactory().getMatriculaFacade()
					.consultarObservacaoComplementarMatricula(objAluno, getGradeCurricularVO().getCodigo()));
			setObservacaoComplementar(objAluno.getObservacaoComplementarHistoricoAlunoVO().getObservacao());
			setObservacaoTransferenciaMatrizCurricular(
					objAluno.getObservacaoComplementarHistoricoAlunoVO().getObservacaoTransferenciaMatrizCurricular());
			listaTipoLayout = null;
			verificarLayoutPadrao();
			verificarParametroRelatorio();
			verificarParametroRelatoricoAluno();
			montarDataExpedicaoDiploma(getMatriculaVO().getMatricula());
			setMensagemDetalhada("");
			montarListaUnidadeCertificadora();
			verificarConfiguracaoDiplomaExistente(null, objAluno);
			setOncomplete(Constantes.EMPTY);
			setListaErroHistoricoEscolarDigital(new ArrayList<>());
			carregarDadosLayout();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatriculaVO());
	}

	public List<SelectItem> tipoConsultaComboAluno;

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}

	public List<SelectItem> tipoConsultaComboFuncionario;

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
		}
		return tipoConsultaComboFuncionario;
	}

	public List<SelectItem> tipoConsultaComboFiltroPor;

	public List<SelectItem> getTipoConsultaComboFiltroPor() {
		if (tipoConsultaComboFiltroPor == null) {
			tipoConsultaComboFiltroPor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFiltroPor.add(new SelectItem("aluno", "Aluno"));
			tipoConsultaComboFiltroPor.add(new SelectItem("turma", "Turma"));
			tipoConsultaComboFiltroPor.add(new SelectItem("desconto", "Desconto"));
			tipoConsultaComboFiltroPor.add(new SelectItem("programacaoFormatura", "Programação de Formatura"));
		}
		return tipoConsultaComboFiltroPor;
	}

	public List<SelectItem> tipoConsultaComboTipoDesconto;

	public List<SelectItem> getTipoConsultaComboTipoDesconto() {
		if (tipoConsultaComboTipoDesconto == null) {
			tipoConsultaComboTipoDesconto = new ArrayList<SelectItem>(0);
			tipoConsultaComboTipoDesconto.add(new SelectItem("", ""));
			tipoConsultaComboTipoDesconto.add(new SelectItem("PF", "Desconto Plano Financeiro"));
			tipoConsultaComboTipoDesconto.add(new SelectItem("PC", "Desconto Plano Convênio"));
		}
		return tipoConsultaComboTipoDesconto;
	}

	public void montarListaSelectItemDesconto() {
		try {
			if (getCampoConsultaTipoDesconto().equals("PF")) {
				montarListaSelectItemDescontoPlanoFinanceiro();
			} else {
				montarListaSelectItemDescontoConvenio();
			}
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}

	}

	public void montarListaSelectItemDescontoPlanoFinanceiro() throws Exception {
		List<PlanoDescontoVO> listaPlanoFinanceiro = null;
		Iterator<PlanoDescontoVO> i = null;
		try {
			listaPlanoFinanceiro = getFacadeFactory().getPlanoDescontoFacade().consultarPorNome("", 0, false,
					getUsuarioLogado(), 0, 0);
			i = listaPlanoFinanceiro.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			while (i.hasNext()) {
				PlanoDescontoVO obj = (PlanoDescontoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
			setListaSelectItemDescontoPlanoFinanceiro(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			i = null;
			Uteis.liberarListaMemoria(listaPlanoFinanceiro);

		}
	}

	public void montarListaSelectItemDescontoConvenio() throws Exception {
		List<ConvenioVO> listaDescontoconvenio = null;
		Iterator<ConvenioVO> i = null;
		try {
			listaDescontoconvenio = getFacadeFactory().getConvenioFacade().consultarPorDescricao("", false,
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			i = listaDescontoconvenio.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			while (i.hasNext()) {
				ConvenioVO obj = (ConvenioVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				removerObjetoMemoria(obj);
			}
			setListaSelectItemDescontoConvenio(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			i = null;
			Uteis.liberarListaMemoria(listaDescontoconvenio);
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					objs = getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoIdentificadorTurma(
							getUnidadeEnsinoVO().getCodigo().intValue(), getValorConsultaTurma(), false,
							Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(
							getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false,
							Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
				}

				setListaConsultaTurma(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		listaTipoLayout = null;
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
		setTurmaVO(obj);
		montarListaSelectItemGradeCurricularTurma();
		getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().setMatricula("");
		getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getPessoa().setNome("");
		getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().setMatricula("");
		getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getPessoa().setNome("");
		getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().setMatricula("");
		getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getPessoa().setNome("");
		setTipoNivelEducacional(getTurmaVO().getCurso().getNivelEducacional());
		verificarParametroRelatorio();
		getListaTipoLayout();
		setAno("");
		setSemestre("");
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();
	}

	public List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public List<SelectItem> tipoConsultaComboSemestre;

	public List<SelectItem> getTipoConsultaComboSemestre() {
		if (tipoConsultaComboSemestre == null) {
			tipoConsultaComboSemestre = new ArrayList<SelectItem>(0);
			tipoConsultaComboSemestre.add(new SelectItem("", ""));
			tipoConsultaComboSemestre.add(new SelectItem("1", "1º"));
			tipoConsultaComboSemestre.add(new SelectItem("2", "2º"));
		}
		return tipoConsultaComboSemestre;
	}

	public boolean isApresentarCampos() {
		return ((getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0
				&& getIsFiltrarPorAluno())
				|| (getTurmaVO() != null && getTurmaVO().getCodigo() != 0 && getIsFiltrarPorturma())
				|| !getCampoConsultaTipoDesconto().equals("") || getIsFiltrarPorProgramacaoFormatura());
	}

	public boolean isApresentarGradeCurricular() {
		return ((getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0
				&& getIsFiltrarPorAluno())
				|| (getTurmaVO() != null && getTurmaVO().getCodigo() != 0 && getIsFiltrarPorturma()
						&& !getTurmaVO().getTurmaAgrupada()));
	}

	public boolean isApresentarCamposAluno() {
		return (getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0
				&& getIsFiltrarPorAluno());
	}

	public boolean isApresentarCampoCargoFuncionarioPrincipal() {
		return isApresentarCampos() && Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO());
	}

	public boolean isApresentarCampoCargoFuncionarioSecundario() {
		return isApresentarCampos()
				&& Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO());
	}

	public boolean isApresentarCampoCargoFuncionarioTerciario() {
		return isApresentarCampos() && Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioTerciarioVO());
	}

	public void limparDadosFuncionarioPrincipal() {
		removerObjetoMemoria(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO());
		getHistoricoAlunoRelVO().setFuncionarioPrincipalVO(new FuncionarioVO());
		getHistoricoAlunoRelVO().setTituloFuncionarioPrincipal("");
		getHistoricoAlunoRelVO().setCargoFuncionarioPrincipal(null);
		setSelectItemsCargoFuncionarioPrincipal(null);
	}

	public void limparDadosFuncionarioSecundario() {
		removerObjetoMemoria(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO());
		getHistoricoAlunoRelVO().setFuncionarioSecundarioVO(new FuncionarioVO());
		getHistoricoAlunoRelVO().setTituloFuncionarioSecundario("");
		getHistoricoAlunoRelVO().setCargoFuncionarioSecundario(null);
		setSelectItemsCargoFuncionarioSecundario(null);
	}

	public void limparIdentificador() {
		removerObjetoMemoria(getMatriculaVO());
		removerObjetoMemoria(getTurmaVO());
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			limparIdentificador();
			limparDadosAluno();
			limparProgramacaoFormatura();
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			setUnidadeEnsinoCertificadora(new UnidadeEnsinoVO());
			setGerarXmlHistorico(Boolean.FALSE);
			setOncomplete(Constantes.EMPTY);
			setListaErroHistoricoEscolarDigital(new ArrayList<>());
			setListaErroHistoricoAlunoRel(new ArrayList<>(0));
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsinoPorNome(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsinoPorNome("");
			}
			setMensagemID("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade()
				.consultarUnidadeEnsinoComboBox(super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());

	}

	public void montarListaSelectItemUnidadeEnsinoPorNome(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
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

	public void montarListaSelectItemGradeCurricularTurma() {
		List<GradeCurricularVO> grades = new ArrayList<GradeCurricularVO>(0);
		try {
			if (!getTurmaVO().getTurmaAgrupada()) {
				GradeCurricularVO grade = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(
						getTurmaVO().getGradeCurricularVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
				grades.add(grade);
				setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(grades, "codigo", "nome", false));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(grades);
		}
	}

	public void verificarGradeCurricular(MatriculaVO matricula) {
		if (getAno() != null && !getAno().equals("")) {
			for (MatriculaPeriodoVO matriculaPeriodo : matricula.getMatriculaPeriodoVOs()) {
				if (matriculaPeriodo.getAno().equals(getAno())
						&& matriculaPeriodo.getSemestre().equals(getSemestre())) {
					getGradeCurricularVO().setCodigo(matriculaPeriodo.getGradeCurricular().getCodigo());
				}
			}
		} else {
			getGradeCurricularVO().setCodigo((matricula.getMatriculaPeriodoVOs()
					.get(matricula.getMatriculaPeriodoVOs().size() - 1).getGradeCurricular().getCodigo()));
		}

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
	 * @param valorConsultaAluno the valorConsultaAluno to set
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
	 * @param campoConsultaAluno the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the historicoAlunoRelVO
	 */
	public HistoricoAlunoRelVO getHistoricoAlunoRelVO() {
		if (historicoAlunoRelVO == null) {
			historicoAlunoRelVO = new HistoricoAlunoRelVO();
		}
		return historicoAlunoRelVO;
	}

	/**
	 * @param historicoAlunoRelVO the historicoAlunoRelVO to set
	 */
	public void setHistoricoAlunoRelVO(HistoricoAlunoRelVO historicoAlunoRelVO) {
		this.historicoAlunoRelVO = historicoAlunoRelVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>();
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	public List<SelectItem> getListaSelectItemSituacaoDisciplina() {
		if (listaSelectItemSituacaoDisciplina == null) {
			listaSelectItemSituacaoDisciplina = UtilPropriedadesDoEnum
					.getListaSelectItemDoEnum(FiltroSituacaoDisciplina.class, false);
		}
		return listaSelectItemSituacaoDisciplina;
	}

	public List<SelectItem> getListaSelectItemOrdemDisciplina() {
		if (listaSelectItemOrdemDisciplina == null) {
			listaSelectItemOrdemDisciplina = UtilPropriedadesDoEnum
					.getListaSelectItemDoEnum(OrdemHistoricoDisciplina.class, false);
		}
		return listaSelectItemOrdemDisciplina;
	}

	public void setListaSelectItemOrdemDisciplina(List<SelectItem> listaSelectItemOrdemDisciplina) {
		this.listaSelectItemOrdemDisciplina = listaSelectItemOrdemDisciplina;
	}

	public int getFiltro() {
		return filtro;
	}

	public void setFiltro(int filtro) {
		this.filtro = filtro;
	}

	public void setListaSelectItemSituacaoDisciplina(List<SelectItem> listaSelectItemSituacaoDisciplina) {
		this.listaSelectItemSituacaoDisciplina = listaSelectItemSituacaoDisciplina;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioPrincipal() {
		if (selectItemsCargoFuncionarioPrincipal == null) {
			selectItemsCargoFuncionarioPrincipal = new ArrayList<SelectItem>();
		}
		return selectItemsCargoFuncionarioPrincipal;
	}

	public void setSelectItemsCargoFuncionarioPrincipal(List<SelectItem> selectItemsCargoFuncionarioPrincipal) {
		this.selectItemsCargoFuncionarioPrincipal = selectItemsCargoFuncionarioPrincipal;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioSecundario() {
		if (selectItemsCargoFuncionarioSecundario == null) {
			selectItemsCargoFuncionarioSecundario = new ArrayList<SelectItem>();
		}
		return selectItemsCargoFuncionarioSecundario;
	}

	public void setSelectItemsCargoFuncionarioSecundario(List<SelectItem> selectItemsCargoFuncionarioSecundario) {
		this.selectItemsCargoFuncionarioSecundario = selectItemsCargoFuncionarioSecundario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = null;
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public int getOrdem() {
		return ordem;
	}

	public int getCount() {
		if (observacaoComplementar == null) {
			observacaoComplementar = "";
		}
		return observacaoComplementar.length();
	}

	/**
	 * @return the observacaoComplementar
	 */
	public String getObservacaoComplementar() {
		if (observacaoComplementar == null) {
			observacaoComplementar = "";
		}
		return observacaoComplementar;
	}

	/**
	 * @param observacaoComplementar the observacaoComplementar to set
	 */
	public void setObservacaoComplementar(String observacaoComplementar) {
		this.observacaoComplementar = observacaoComplementar;
	}

	public String getObservacaoComplementarIntegralizado() {
		if (observacaoComplementarIntegralizado == null) {
			observacaoComplementarIntegralizado = "";
		}
		return observacaoComplementarIntegralizado;
	}

	public void setObservacaoComplementarIntegralizado(String observacaoComplementarIntegralizado) {
		this.observacaoComplementarIntegralizado = observacaoComplementarIntegralizado;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getCampoFiltroPor() {
		if (campoFiltroPor == null) {
			campoFiltroPor = "aluno";
		}
		return campoFiltroPor;
	}

	public void setCampoFiltroPor(String campoFiltroPor) {
		this.campoFiltroPor = campoFiltroPor;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<MatriculaVO> getListaMatriculas() {
		if (listaMatriculas == null) {
			listaMatriculas = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculas;
	}

	public void setListaMatriculas(List<MatriculaVO> listaMatriculas) {
		this.listaMatriculas = listaMatriculas;
	}

	public String getAno() {
		if (ano == null) {
			return "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
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

	public Boolean getIsFiltrarPorturma() {
		if (getCampoFiltroPor().equals("turma")) {
			return true;
		}
		return false;
	}

	public Boolean getIsFiltrarPorDesconto() {
		if (getCampoFiltroPor().equals("desconto")) {
			return true;
		}
		return false;
	}

	public Boolean getIsFiltrarPorAluno() {
		if (getCampoFiltroPor().equals("aluno")) {
			return true;
		}
		return false;
	}

	public Boolean getIsApresentarUnidadeEnsino() {
		if (getCampoFiltroPor().equals("aluno") || getCampoFiltroPor().equals("desconto")) {
			return true;
		}
		return false;
	}

	public Boolean getIsFiltrarPorAno() {
		if ((getTurmaVO().getCodigo() != 0 && !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()
				&& !getTurmaVO().getTurmaAgrupada()
				&& (getTurmaVO().getCurso().getPeriodicidade().equals("AN")
						|| getTurmaVO().getCurso().getPeriodicidade().equals("SE")))
				|| (getCampoFiltroPor().equals("desconto") && !getCampoConsultaTipoDesconto().equals(""))) {
			return true;
		} else {
			if (getTurmaVO().getTurmaAgrupada() != null) {
				if (getTurmaVO().getTurmaAgrupada()) {
					for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
						if (!turmaAgrupada.getTurma().getAnual() && !turmaAgrupada.getTurma().getSemestral()) {
							return false;
						}
					}
				} else if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
					return false;
				} else if (getTurmaVO().getCodigo().equals(0)) {
					return false;
				} else if (!getCampoFiltroPor().equals("turma")) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	public Boolean getIsFiltrarPorSemestre() {
		if ((getTurmaVO().getCodigo() != 0 && !getTurmaVO().getTurmaAgrupada()
				&& getTurmaVO().getCurso().getPeriodicidade().equals("SE"))
				|| (getCampoFiltroPor().equals("desconto") && !getCampoConsultaTipoDesconto().equals(""))) {
			return true;
		} else {
			if (getTurmaVO().getTurmaAgrupada() != null) {
				if (getTurmaVO().getTurmaAgrupada()) {
					for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
						if (!turmaAgrupada.getTurma().getSemestral()) {
							return false;
						}
					}
				} else if (!getTurmaVO().getCurso().getPeriodicidade().equals("SE")) {
					return false;
				} else if (getTurmaVO().getCodigo().equals(0)) {
					return false;
				} else if (!getCampoFiltroPor().equals("turma")) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the itemPlanoFinanceiroAlunoVO
	 */
	public ItemPlanoFinanceiroAlunoVO getItemPlanoFinanceiroAlunoVO() {
		if (itemPlanoFinanceiroAlunoVO == null) {
			itemPlanoFinanceiroAlunoVO = new ItemPlanoFinanceiroAlunoVO();
		}
		return itemPlanoFinanceiroAlunoVO;
	}

	/**
	 * @param itemPlanoFinanceiroAlunoVO the itemPlanoFinanceiroAlunoVO to set
	 */
	public void setItemPlanoFinanceiroAlunoVO(ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO) {
		this.itemPlanoFinanceiroAlunoVO = itemPlanoFinanceiroAlunoVO;
	}

	/**
	 * @return the campoConsultaTipoDesconto
	 */
	public String getCampoConsultaTipoDesconto() {
		if (campoConsultaTipoDesconto == null) {
			campoConsultaTipoDesconto = "";
		}
		return campoConsultaTipoDesconto;
	}

	/**
	 * @param campoConsultaTipoDesconto the campoConsultaTipoDesconto to set
	 */
	public void setCampoConsultaTipoDesconto(String campoConsultaTipoDesconto) {
		this.campoConsultaTipoDesconto = campoConsultaTipoDesconto;
	}

	/**
	 * @return the listaSelectItemDescontoPlanoFinanceiro
	 */
	public List<SelectItem> getListaSelectItemDescontoPlanoFinanceiro() {
		if (listaSelectItemDescontoPlanoFinanceiro == null) {
			listaSelectItemDescontoPlanoFinanceiro = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDescontoPlanoFinanceiro;
	}

	/**
	 * @param listaSelectItemDescontoPlanoFinanceiro the
	 *                                               listaSelectItemDescontoPlanoFinanceiro
	 *                                               to set
	 */
	public void setListaSelectItemDescontoPlanoFinanceiro(List<SelectItem> listaSelectItemDescontoPlanoFinanceiro) {
		this.listaSelectItemDescontoPlanoFinanceiro = listaSelectItemDescontoPlanoFinanceiro;
	}

	/**
	 * @return the listaSelectItemDescontoConvenio
	 */
	public List<SelectItem> getListaSelectItemDescontoConvenio() {
		if (listaSelectItemDescontoConvenio == null) {
			listaSelectItemDescontoConvenio = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDescontoConvenio;
	}

	/**
	 * @param listaSelectItemDescontoConvenio the listaSelectItemDescontoConvenio to
	 *                                        set
	 */
	public void setListaSelectItemDescontoConvenio(List<SelectItem> listaSelectItemDescontoConvenio) {
		this.listaSelectItemDescontoConvenio = listaSelectItemDescontoConvenio;
	}

	public boolean getIsApresentarComboDescontoPlanofinanceiro() {
		if (getCampoConsultaTipoDesconto().equals("PF") && getCampoFiltroPor().equals("desconto")) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarComboDescontoConvenio() {
		if (getCampoConsultaTipoDesconto().equals("PC") && getCampoFiltroPor().equals("desconto")) {
			return true;
		}
		return false;
	}

	public Boolean getIsApresentarComboTipoDesconto() {
		if (getCampoFiltroPor().equals("desconto")) {
			return true;
		}
		return false;
	}

	public Boolean getIsNivelEducacionalSelecionado() {
		if (!getTipoNivelEducacional().equals("")) {
			getMatriculaVO().getCurso().setNivelEducacional(getTipoNivelEducacional());
			return true;
		}
		return false;
	}

	public List<SelectItem> listaSelectItemNivelEducacional;

	public List<SelectItem> getListaSelectItemNivelEducacional() {
		if (listaSelectItemNivelEducacional == null) {			
			listaSelectItemNivelEducacional = new ArrayList<SelectItem>(0);
			for(TipoNivelEducacional nivel: TipoNivelEducacional.values()) {
				listaSelectItemNivelEducacional.add(new SelectItem(nivel.getValor(), nivel.getDescricao()));
			}			
		}
		return listaSelectItemNivelEducacional;
	}

	public List<SelectItem> listaTipoLayout;

	public List<SelectItem> getListaTipoLayout() {
		if (listaTipoLayout == null) {
			try {
				if (Uteis.isAtributoPreenchido(getMatriculaVO().getCurso().getNivelEducacional()) && !getCampoFiltroPor().equals("programacaoFormatura")) {
					listaTipoLayout = getListaTipoLayoutHistorico(getTipoLayout(), getMatriculaVO().getCurso().getNivelEducacional());
					setTipoLayout(getTipoLayoutHistorico());
				} else if (Uteis.isAtributoPreenchido(getTurmaVO().getCurso().getNivelEducacional()) && !getCampoFiltroPor().equals("programacaoFormatura")) {
					listaTipoLayout = getListaTipoLayoutHistorico(getTipoLayout(), getTurmaVO().getCurso().getNivelEducacional());
					setTipoLayout(getTipoLayoutHistorico());
				} else if (getIsApresentarComboTipoDesconto() && !getCampoFiltroPor().equals("programacaoFormatura")) {
					listaTipoLayout = getListaTipoLayoutHistorico(getTipoLayout(), TipoNivelEducacional.SUPERIOR.getValor());
					setTipoLayout(getTipoLayoutHistorico());
				} else if (getCampoFiltroPor().equals("programacaoFormatura")) {
					listaTipoLayout = getListaTipoLayoutHistorico(getTipoLayout(), getProgramacaoFormaturaVO().getNivelEducacional());
					setTipoLayout(getTipoLayoutHistorico());
					if (!Uteis.getIsValorNumerico(getTipoLayoutHistorico())) {
						setTipoLayout(listaTipoLayout.get(0).getValue().toString());
					}
				}
				if (getTipoLayoutPersonalizado()) {
					carregarDadosLayout();
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaTipoLayout;
	}

	public Boolean getTipoLayout5() {
		return getTipoLayout().equals("HistoricoAlunoLayout5Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout5PortariaMECRel");
	}

	public Boolean getTipoLayout6() {
		return (getTipoLayout().equals("HistoricoAlunoLayout6Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout6PortariaMECRel")
				|| getTipoLayout().equals("HistoricoAlunoLayout8Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout8PortariaMECRel"))
				&& !getMatriculaVO().getMatricula().equals("");
	}

	public boolean getTipoLayout12Medicina() {
		return getTipoLayout().equals("HistoricoAlunoLayout12MedicinaRel")
				|| getTipoLayout().equals("HistoricoAlunoLayout12MedicinaPortariaMECRel")
				|| getTipoLayout().equals("HistoricoAlunoLayout12MedicinaAdaptacaoMatrizCurricularRel");
	}

	public Boolean getTipoLayout8() {
		return (getTipoLayout().equals("HistoricoAlunoLayout8Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout8PortariaMECRel"))
				&& !getMatriculaVO().getMatricula().equals("");
	}

	public Boolean getTipoLayoutPos14() {
		return getTipoLayout().equals("HistoricoAlunoPos14Rel") && !getMatriculaVO().getMatricula().equals("");
	}
	
	public Boolean getTipoLayoutPos18() {
		return getTipoLayout().equals("HistoricoAlunoPos18Rel") && !getMatriculaVO().getMatricula().equals("");
	}
	
	public Boolean getTipoLayoutExt19() {
		return getTipoLayout().equals("HistoricoAlunoLayout19ExtRel") && !getMatriculaVO().getMatricula().equals("");
	}
	public Boolean getTipoLayout24() {
		return getTipoLayout().equals("HistoricoAlunoLayout24Rel") && !getMatriculaVO().getMatricula().equals("");
	}
	
	public Boolean getTipoLayout24PortariaMec() {
		return getTipoLayout().equals("HistoricoAlunoLayout24PortariaMecRel") && !getMatriculaVO().getMatricula().equals("");
	}
	
	public Boolean getApresentarBooleanoApresentarFrequencia() {
		return !getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()
				&& !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()
				&& !getMatriculaVO().getCurso().getNivelEducacional().equals("ME");
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public Boolean getImprimirExcel() {
		if (imprimirExcel == null) {
			imprimirExcel = false;
		}
		return imprimirExcel;
	}

	public void setImprimirExcel(Boolean imprimirExcel) {
		this.imprimirExcel = imprimirExcel;
	}

	public Boolean getIsLayoutPos2() {
		if (getTipoLayout().equals("HistoricoAlunoPos2Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoPos3Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoPos17Rel")) {
			return true;
		}
//		if (getTipoLayout().equals("HistoricoAlunoPos18Rel")) {
//			return true;
//		}
		if (getTipoLayout().equals("HistoricoAlunoPos6Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout2Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout3Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout4Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout5Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout6Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout7Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout8Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout9Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout10Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout11Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout12Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout12MedicinaRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout13Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout14Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout15Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout16Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout17Rel")) {
			return true;
		}

		if (getTipoLayout().equals("HistoricoAlunoPos14Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayoutPortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout2PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout3PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout4PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout5PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout6PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout7PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout8PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout9PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout10PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout11PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout12PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout12MedicinaPortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout12MedicinaAdaptacaoMatrizCurricularRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout13PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout14PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout15PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout16PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout17PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout18PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout19PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoPos16Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout20PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout21PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout22PortariaMECRel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout23PortariaMECRel")) {
			return true;
		}
		if (Uteis.getIsValorNumerico(getTipoLayout())) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoPos18Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout24Rel")) {
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout19ExtRel")) { 
			return true;
		}
		if (getTipoLayout().equals("HistoricoAlunoLayout24PortariaMecRel")) {
			return true;
		}
		return false;
	}

	public TurmaVO getTurmaAluno() {
		if (turmaAluno == null) {
			turmaAluno = new TurmaVO();
		}
		return turmaAluno;
	}

	public void setTurmaAluno(TurmaVO turmaAluno) {
		this.turmaAluno = turmaAluno;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoAluno() {
		if (unidadeEnsinoAluno == null) {
			unidadeEnsinoAluno = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoAluno;
	}

	public void setUnidadeEnsinoAluno(UnidadeEnsinoVO unidadeEnsinoAluno) {
		this.unidadeEnsinoAluno = unidadeEnsinoAluno;
	}
	
	public Boolean getUtilizarUnidadeEnsinoMatriz() {
		if (utilizarUnidadeEnsinoMatriz == null) {
			utilizarUnidadeEnsinoMatriz = Boolean.TRUE;
		}
		return utilizarUnidadeEnsinoMatriz;
	}

	public void setUtilizarUnidadeEnsinoMatriz(Boolean utilizarUnidadeEnsinoMatriz) {
		this.utilizarUnidadeEnsinoMatriz = utilizarUnidadeEnsinoMatriz;
	}

	public Date getDataExpedicaoDiploma() {
		return dataExpedicaoDiploma;
	}

	public void setDataExpedicaoDiploma(Date dataExpedicaoDiploma) {
		this.dataExpedicaoDiploma = dataExpedicaoDiploma;
	}

	public Boolean getApresentarInstituicaoDisciplinaAproveitada() {
		if (apresentarInstituicaoDisciplinaAproveitada == null) {
			apresentarInstituicaoDisciplinaAproveitada = Boolean.FALSE;
		}
		return apresentarInstituicaoDisciplinaAproveitada;
	}

	public void setApresentarInstituicaoDisciplinaAproveitada(Boolean apresentarInstituicaoDisciplinaAproveitada) {
		this.apresentarInstituicaoDisciplinaAproveitada = apresentarInstituicaoDisciplinaAproveitada;
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

	public Boolean getApresentarDisciplinaAnoSemestreTransferenciaGrade() {
		if (apresentarDisciplinaAnoSemestreTransferenciaGrade == null) {
			apresentarDisciplinaAnoSemestreTransferenciaGrade = Boolean.FALSE;
		}
		return apresentarDisciplinaAnoSemestreTransferenciaGrade;
	}

	public void setApresentarDisciplinaAnoSemestreTransferenciaGrade(
			Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade) {
		this.apresentarDisciplinaAnoSemestreTransferenciaGrade = apresentarDisciplinaAnoSemestreTransferenciaGrade;
	}

	public Boolean getApresentarDisciplinaForaGrade() {
		if (apresentarDisciplinaForaGrade == null) {
			apresentarDisciplinaForaGrade = Boolean.TRUE;
		}
		return apresentarDisciplinaForaGrade;
	}

	public void setApresentarDisciplinaForaGrade(Boolean apresentarDisciplinaForaGrade) {
		this.apresentarDisciplinaForaGrade = apresentarDisciplinaForaGrade;
	}

	public Boolean getApresentarCheckBoxApresentarDisciplinaAnoSemestreTransferenciaGrade() {
		return getListaSelectItemGradeCurricular().size() > 1 && getGradeAtualAluno()
				&& !getMatriculaVO().getMatricula().equals("");
	}

	public Boolean getApresentarCheckBoxApresentarDisciplinaForaGrade() {
		return getListaSelectItemGradeCurricular().size() > 0 && !getMatriculaVO().getMatricula().equals("");
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

	public String getTipoNivelEducacional() {
		if (tipoNivelEducacional == null) {
			tipoNivelEducacional = "";
		}
		return tipoNivelEducacional;
	}

	public void setTipoNivelEducacional(String tipoNivelEducacional) {
		this.tipoNivelEducacional = tipoNivelEducacional;
	}

	public Boolean getFiltroDisciplinasACursar() {
		if (filtroDisciplinasACursar == null) {
			filtroDisciplinasACursar = Boolean.FALSE;
		}
		return filtroDisciplinasACursar;
	}

	public void setFiltroDisciplinasACursar(Boolean filtroDisciplinasACursar) {
		this.filtroDisciplinasACursar = filtroDisciplinasACursar;
	}

	public Boolean getApresentarTopoRelatorio() {
		if (apresentarTopoRelatorio == null) {
			apresentarTopoRelatorio = Boolean.FALSE;
		}
		return apresentarTopoRelatorio;
	}

	public void setApresentarTopoRelatorio(Boolean apresentarTopoRelatorio) {
		this.apresentarTopoRelatorio = apresentarTopoRelatorio;
	}

	public Boolean getDesconsiderarSituacaoMatriculaPeriodo() {
		if (desconsiderarSituacaoMatriculaPeriodo == null) {
			desconsiderarSituacaoMatriculaPeriodo = Boolean.FALSE;
		}
		return desconsiderarSituacaoMatriculaPeriodo;
	}

	public void setDesconsiderarSituacaoMatriculaPeriodo(Boolean desconsiderarSituacaoMatriculaPeriodo) {
		this.desconsiderarSituacaoMatriculaPeriodo = desconsiderarSituacaoMatriculaPeriodo;
	}

	public Boolean getApresentarOpcaoDesconsiderarSituacaoMatriculaPeriodo() {
		return (getMatriculaVO().getCurso().getNivelEducacional().equals("ME")
				|| getMatriculaVO().getCurso().getNivelEducacional().equals("BA"));
	}

	public String getUrlAssinaturaFuncionarioPrincipal() {
		if (!getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome().trim().isEmpty()) {
			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
					+ PastaBaseArquivoEnum.ASSINATURA.getValue() + "/"
					+ getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome();
		}
		return "";
	}

	public String getUrlAssinaturaFuncionarioSecundario() {
		if (!getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome().trim()
				.isEmpty()) {
			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
					+ PastaBaseArquivoEnum.ASSINATURA.getValue() + "/"
					+ getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome();
		}
		return "";
	}

	public String getUrlAssinaturaFuncionarioTerciario() {
		if (!getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getArquivoAssinaturaVO().getNome().trim().isEmpty()) {
			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
					+ PastaBaseArquivoEnum.ASSINATURA.getValue() + "/"
					+ getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getArquivoAssinaturaVO().getNome();
		}
		return "";
	}

	public Boolean getApresentarDisciplinaPeriodoTrancadoCanceladoTransferido() {
		if (apresentarDisciplinaPeriodoTrancadoCanceladoTransferido == null) {
			apresentarDisciplinaPeriodoTrancadoCanceladoTransferido = false;
		}
		return apresentarDisciplinaPeriodoTrancadoCanceladoTransferido;
	}

	public void setApresentarDisciplinaPeriodoTrancadoCanceladoTransferido(
			Boolean apresentarDisciplinaPeriodoTrancadoCanceladoTransferido) {
		this.apresentarDisciplinaPeriodoTrancadoCanceladoTransferido = apresentarDisciplinaPeriodoTrancadoCanceladoTransferido;
	}

	public boolean getIsTipoLayout4() {
		return getTipoLayout().equals("HistoricoAlunoLayout4Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout4PortariaMECRel");
	}

	public boolean getIsTipoLayout13() {
		return getTipoLayout().equals("HistoricoAlunoLayout13Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout13PortariaMECRel");
	}

	public boolean getIsTipoLayout12() {
		return getTipoLayout().equals("HistoricoAlunoLayout12Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout12PortariaMECRel");
	}

	private void persistirParametroRelatorio() throws Exception {
		getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("HistoricoAlunoRel",
				"apresentarDisciplinaPeriodoTrancadoCanceladoTransferido",
				getApresentarDisciplinaPeriodoTrancadoCanceladoTransferido(), getUsuarioLogado());
		getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("HistoricoAlunoRel",
				"apresentarCargaHorariaDisciplina", isApresentarCargaHorariaDisciplina(), getUsuarioLogado());
		getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("HistoricoAlunoRel",
				"considerarCargaHorariaCursadaIgualCargaHorariaPrevista",
				getConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista(), getUsuarioLogado());
		getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("HistoricoAlunoRel",
				"apresentarApenasProfessorTitulacaoTurmaOrigem", isApresentarApenasProfessorTitulacaoTurmaOrigem(),
				getUsuarioLogado());
		getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio(
				"HistoricoAlunoRel_" + getMatriculaVO().getMatricula(), "dataInicioTerminoAlteracoesCadastrais",
				getDataInicioTerminoAlteracoesCadastrais(), getUsuarioLogado());
		getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("HistoricoAlunoRel",
				"assinarDigitalmente", isAssinarDigitalmente(), getUsuarioLogado());
	}

	private void verificarParametroRelatorio() throws Exception {
		setApresentarDisciplinaPeriodoTrancadoCanceladoTransferido(getFacadeFactory().getParametroRelatorioFacade()
				.consultarPorEntidadeCampo("HistoricoAlunoRel",
						"apresentarDisciplinaPeriodoTrancadoCanceladoTransferido", false, getUsuarioLogado())
				.getApresentarCampo());
		setApresentarCargaHorariaDisciplina(
				getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("HistoricoAlunoRel",
						"apresentarCargaHorariaDisciplina", false, getUsuarioLogado()).getApresentarCampo());
		setConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista(getFacadeFactory().getParametroRelatorioFacade()
				.consultarPorEntidadeCampo("HistoricoAlunoRel",
						"considerarCargaHorariaCursadaIgualCargaHorariaPrevista", false, getUsuarioLogado())
				.getApresentarCampo());
		setApresentarApenasProfessorTitulacaoTurmaOrigem(getFacadeFactory()
				.getParametroRelatorioFacade().consultarPorEntidadeCampo("HistoricoAlunoRel",
						"apresentarApenasProfessorTitulacaoTurmaOrigem", false, getUsuarioLogado())
				.getApresentarCampo());
		setAssinarDigitalmente(false);
//		getFacadeFactory().getParametroRelatorioFacade()
//		.consultarPorEntidadeCampo("HistoricoAlunoRel", "assinarDigitalmente", false, getUsuarioLogado())
//		.getApresentarCampo()
	}

	private void verificarParametroRelatoricoAluno() throws Exception {
		setDataInicioTerminoAlteracoesCadastrais(
				getFacadeFactory().getParametroRelatorioFacade()
						.consultarPorEntidadeCampo("HistoricoAlunoRel_" + getMatriculaVO().getMatricula(),
								"dataInicioTerminoAlteracoesCadastrais", false, getUsuarioLogado())
						.getApresentarCampo());
	}

	public Boolean getApresentarObservacaoComplementar() {
		if (apresentarObservacaoComplementar == null) {
			apresentarObservacaoComplementar = Boolean.FALSE;
		}
		return apresentarObservacaoComplementar;
	}

	public void setApresentarObservacaoComplementar(Boolean apresentarObservacaoComplementar) {
		this.apresentarObservacaoComplementar = apresentarObservacaoComplementar;
	}

	public String getTextoCertificadoEstudo() {
		if (textoCertificadoEstudo == null) {
			textoCertificadoEstudo = "";
		}
		return textoCertificadoEstudo;
	}

	public void setTextoCertificadoEstudo(String textoCertificadoEstudo) {
		this.textoCertificadoEstudo = textoCertificadoEstudo;
	}

	public boolean isApresentarCargaHorariaDisciplina() {
		return apresentarCargaHorariaDisciplina;
	}

	public void setApresentarCargaHorariaDisciplina(boolean apresentarCargaHorariaDisciplina) {
		this.apresentarCargaHorariaDisciplina = apresentarCargaHorariaDisciplina;
	}

	public boolean isTipoLayoutEnsinoMedio1() {
		return "HistoricoAlunoEnsinoMedio".equals(getTipoLayout());
	}

	public Boolean getApresentarObservacaoTransferenciaMatrizCurricular() {
		if (apresentarObservacaoTransferenciaMatrizCurricular == null) {
			apresentarObservacaoTransferenciaMatrizCurricular = false;
		}
		return apresentarObservacaoTransferenciaMatrizCurricular;
	}

	public void setApresentarObservacaoTransferenciaMatrizCurricular(
			Boolean apresentarObservacaoTransferenciaMatrizCurricular) {
		this.apresentarObservacaoTransferenciaMatrizCurricular = apresentarObservacaoTransferenciaMatrizCurricular;
	}

	public Boolean getPossuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular() {
		if (possuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular == null) {
			possuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular = false;
		}
		return possuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular;
	}

	public void setPossuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular(
			Boolean possuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular) {
		this.possuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular = possuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular;
	}

	public void verificarUsuarioPermissaoApresentarObsTransferenciaMatrizCurricular() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"ApresentarObservacaoTransferenciaMatrizCurricular", getUsuarioLogado());
			setPossuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular(Boolean.TRUE);
		} catch (Exception e) {
			setPossuiPermissaoVisualizarObservacaoTransferenciaMatrizCurricular(Boolean.FALSE);
		}
	}

	public String getObservacaoTransferenciaMatrizCurricular() {
		if (observacaoTransferenciaMatrizCurricular == null) {
			observacaoTransferenciaMatrizCurricular = "";
		}
		return observacaoTransferenciaMatrizCurricular;
	}

	public void setObservacaoTransferenciaMatrizCurricular(String observacaoTransferenciaMatrizCurricular) {
		this.observacaoTransferenciaMatrizCurricular = observacaoTransferenciaMatrizCurricular;
	}

	public Boolean getApresentarApenasUltimoHistoricoDisciplina() {
		if (apresentarApenasUltimoHistoricoDisciplina == null) {
			apresentarApenasUltimoHistoricoDisciplina = Boolean.FALSE;
		}
		return apresentarApenasUltimoHistoricoDisciplina;
	}

	public void setApresentarApenasUltimoHistoricoDisciplina(Boolean apresentarApenasUltimoHistoricoDisciplina) {
		this.apresentarApenasUltimoHistoricoDisciplina = apresentarApenasUltimoHistoricoDisciplina;
	}

	public Boolean getPermiteVisualizarAlterarDataExpedicaoDiploma() {
		if (permiteVisualizarAlterarDataExpedicaoDiploma == null) {
			permiteVisualizarAlterarDataExpedicaoDiploma = false;
		}
		return permiteVisualizarAlterarDataExpedicaoDiploma;
	}

	public void setPermiteVisualizarAlterarDataExpedicaoDiploma(Boolean permiteVisualizarAlterarDataExpedicaoDiploma) {
		this.permiteVisualizarAlterarDataExpedicaoDiploma = permiteVisualizarAlterarDataExpedicaoDiploma;
	}

	public void montarDataExpedicaoDiploma(String matricula) {
		try {
			verificarUsuarioPermissaoVisualizarAlterarDataExpedicaoDiploma();
			setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(matricula, false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void verificarUsuarioPermissaoVisualizarAlterarDataExpedicaoDiploma() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"PermiteVisualizarAlterarDataExpedicaoDiploma", getUsuarioLogado());
			setPermiteVisualizarAlterarDataExpedicaoDiploma(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteVisualizarAlterarDataExpedicaoDiploma(Boolean.FALSE);
		}
	}

	public void gravarAlteracoesExpedicaoDiploma() {
		try {
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCodigo()))
				getFacadeFactory().getExpedicaoDiplomaFacade().alterarDataExpedicaoDiploma(
						getExpedicaoDiplomaVO().getCodigo(), getExpedicaoDiplomaVO().getDataExpedicao(),
						getUsuarioLogado(), true);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ExpedicaoDiplomaVO getExpedicaoDiplomaVO() {
		if (expedicaoDiplomaVO == null) {
			expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
		}
		return expedicaoDiplomaVO;
	}

	public void setExpedicaoDiplomaVO(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
		this.expedicaoDiplomaVO = expedicaoDiplomaVO;
	}

	public Boolean getPermiteVisualizarAlterarDataEmissaoHistorico() {
		if (permiteVisualizarAlterarDataEmissaoHistorico == null) {
			permiteVisualizarAlterarDataEmissaoHistorico = false;
		}
		return permiteVisualizarAlterarDataEmissaoHistorico;
	}

	public void setPermiteVisualizarAlterarDataEmissaoHistorico(Boolean permiteVisualizarAlterarDataEmissaoHistorico) {
		this.permiteVisualizarAlterarDataEmissaoHistorico = permiteVisualizarAlterarDataEmissaoHistorico;
	}

	public void verificarUsuarioPermissaoVisualizarAlterarDataEmissaoHistorico() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"PermiteVisualizarAlterarDataEmissaoHistorico", getUsuarioLogado());
			setPermiteVisualizarAlterarDataEmissaoHistorico(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteVisualizarAlterarDataEmissaoHistorico(Boolean.FALSE);
		}
	}

	public void gravarAlteracoesEmissaoHistorico() {
		try {
			if (Uteis.isAtributoPreenchido(getMatriculaVO().getDataEmissaoHistorico())) {
				getFacadeFactory().getMatriculaFacade().alterarDataEmissaoHistorico(getMatriculaVO().getMatricula(),
						getMatriculaVO().getDataEmissaoHistorico(), getUsuarioLogado(), true);
				setMensagemID("msg_dados_gravados");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista() {
		if (considerarCargaHorariaCursadaIgualCargaHorariaPrevista == null) {
			considerarCargaHorariaCursadaIgualCargaHorariaPrevista = false;
		}
		return considerarCargaHorariaCursadaIgualCargaHorariaPrevista;
	}

	public void setConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista(
			Boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista) {
		this.considerarCargaHorariaCursadaIgualCargaHorariaPrevista = considerarCargaHorariaCursadaIgualCargaHorariaPrevista;
	}

	public Boolean getApresentarMediaNotasSemestreAnteriorAluno() {
		if (apresentarMediaNotasSemestreAnteriorAluno == null) {
			apresentarMediaNotasSemestreAnteriorAluno = Boolean.FALSE;
		}
		return apresentarMediaNotasSemestreAnteriorAluno;
	}

	public void setApresentarMediaNotasSemestreAnteriorAluno(Boolean apresentarMediaNotasSemestreAnteriorAluno) {
		this.apresentarMediaNotasSemestreAnteriorAluno = apresentarMediaNotasSemestreAnteriorAluno;
	}

	public boolean getIsTipoLayout10() {
		return getTipoLayout().equals("HistoricoAlunoLayout10Rel");
	}

	public ProgressBarVO getProgressBarVO() {
		if (progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public void executarInicioProgressBarExcell() {
		setImprimirExcel(true);
		executarInicioProgressBar();
	}

	public void executarInicioProgressBar() {
		try {
			gerarListaMatriculas();
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarVO().setUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
			getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());
			if (getIsFiltrarPorturma() || getIsFiltrarPorProgramacaoFormatura()) {
				getProgressBarVO().resetar();
				getProgressBarVO().iniciar(0l, (getListaMatriculas().size() + 1), "Carregando Matrículas ", true, this,
						"imprimirPDF");
			} else {
				imprimirPDF();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gerarListaMatriculas() throws Exception {
		getListaMatriculas().clear();
		if (getIsFiltrarPorAluno()) {
			Uteis.liberarListaMemoria(getListaMatriculas());
			setListaMatriculas(getFacadeFactory().getMatriculaFacade()
					.consultaRapidaPorMatriculaUnicaParaHistoricoAluno(getMatriculaVO().getMatricula(),
							getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			// setListaMatriculas(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getMatriculaVO().getMatricula(),
			// getUnidadeEnsinoVO().getCodigo(), false,
			// getUsuarioLogado()));
			// getListaMatriculas().add(getMatriculaVO());
		} else if (getCampoFiltroPor().equals("turma")) {
			if (getTurmaVO().getTurmaAgrupada()) {
				setListaMatriculas(getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaAnoSemestre(
						getTurmaVO().getTurmaAgrupadaVOs(), getAno(), getSemestre(), getUnidadeEnsinoVO().getCodigo(),
						getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			} else {
				setListaMatriculas(
						getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaCursoGradeCurricularAnoSemestre(
								getTurmaVO().getCodigo(), getTurmaVO().getCurso().getCodigo(), getAno(), getSemestre(),
								getGradeCurricularVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(),
								getUsuarioLogado()));
			}
		} else if (getCampoFiltroPor().equals("desconto")) {
			Uteis.liberarListaMemoria(getListaMatriculas());
			setListaMatriculas(getFacadeFactory().getMatriculaFacade().consultaRapidaPorDescontoMatriculaUnidadeEnsino(
					getCampoConsultaTipoDesconto(), getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo(),
					getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false,
					getUsuarioLogado()));
		}else if (getCampoFiltroPor().equals("programacaoFormatura")) {
			Uteis.liberarListaMemoria(getListaMatriculas());
			if(getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().isEmpty()) {
				getProgramacaoFormaturaVO().setProgramacaoFormaturaAlunoVOs(getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarProgramacaoFormaturaAlunos(getProgramacaoFormaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			for(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO : getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs()) {	
				if(getFiltroAlunosPresentesColacaoGrau().equals("NI") || getFiltroAlunosPresentesColacaoGrau().equals(programacaoFormaturaAlunoVO.getColouGrau())) {
					getListaMatriculas().add(programacaoFormaturaAlunoVO.getMatricula());
				}
			}
		}

	}

	public void downloadProgressBar() {
		getProgressBarVO().setOncomplete(getDownload());
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioTerciario() {
		if (selectItemsCargoFuncionarioTerciario == null) {
			selectItemsCargoFuncionarioTerciario = new ArrayList<SelectItem>(0);
		}
		return selectItemsCargoFuncionarioTerciario;
	}

	public void setSelectItemsCargoFuncionarioTerciario(List<SelectItem> selectItemsCargoFuncionarioTerciario) {
		this.selectItemsCargoFuncionarioTerciario = selectItemsCargoFuncionarioTerciario;
	}

	public void consultarFuncionarioTerciario() throws Exception {
		try {
			getHistoricoAlunoRelVO().setFuncionarioTerciarioVO(consultarFuncionarioPorMatricula(
					getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getMatricula()));
			setSelectItemsCargoFuncionarioTerciario(montarComboCargoFuncionario(
					getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(
							getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionarioTerciario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioTerciario");
		getHistoricoAlunoRelVO().setFuncionarioTerciarioVO(obj);
		consultarFuncionarioTerciario();
	}

	public void limparDadosFuncionarioTerciario() {
		removerObjetoMemoria(getHistoricoAlunoRelVO().getFuncionarioTerciarioVO());
		getHistoricoAlunoRelVO().setFuncionarioTerciarioVO(new FuncionarioVO());
		getHistoricoAlunoRelVO().setTituloFuncionarioTerciario("");
		getHistoricoAlunoRelVO().setCargoFuncionarioTerciario(null);
		setSelectItemsCargoFuncionarioTerciario(null);
	}

	public boolean isMostrarCamposApenasProfessorTitularTurmaOrigem() {
		return (getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()
				|| getTurmaVO().getCurso().getNivelEducacionalPosGraduacao())
				&& (getTipoLayout().equals("HistoricoAlunoPos3Rel") || getTipoLayout().equals("HistoricoAlunoPos2Rel") || getTipoLayout().equals("HistoricoAlunoPos17Rel") || getTipoLayout().equals("HistoricoAlunoPos18Rel") || Uteis.getIsValorNumerico(getTipoLayout()));	}

	public boolean isApresentarApenasProfessorTitulacaoTurmaOrigem() {
		return apresentarApenasProfessorTitulacaoTurmaOrigem;
	}

	public void setApresentarApenasProfessorTitulacaoTurmaOrigem(
			boolean apresentarApenasProfessorTitulacaoTurmaOrigem) {
		this.apresentarApenasProfessorTitulacaoTurmaOrigem = apresentarApenasProfessorTitulacaoTurmaOrigem;
	}

	public String getMsgProfessorTitulacao() {
		msgProfessorTitulacao = Optional.ofNullable(msgProfessorTitulacao).orElse("");
		return msgProfessorTitulacao;
	}

	public void setMsgProfessorTitulacao(String msgProfessorTitulacao) {
		this.msgProfessorTitulacao = msgProfessorTitulacao;
	}

	public String getMostraMsgProfessorTitulacao() {
		if (Uteis.isAtributoPreenchido(getMsgProfessorTitulacao())) {
			return "RichFaces.$('panelMsgTitulacaoCurso').show();";
		}
		return "";
	}

	public boolean getIsTipoLayout14() {
		return getTipoLayout().equals("HistoricoAlunoLayout14Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout14PortariaMECRel");
	}

	public boolean getIsTipoLayout15() {
		return getTipoLayout().equals("HistoricoAlunoLayout15Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout15PortariaMECRel");
	}

	public boolean getApresentarselectBooleanCheckboxTopoRelatorio() {
		return Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula()) && (getTipoLayout5() || getTipoLayout6()
				|| getIsTipoLayout14() || getIsTipoLayout15() || getIsTipoLayout5PortariaMEC() || getTipoLayoutPos18() || getTipoLayout().equals("HistoricoAlunoPos15Rel") || Uteis.getIsValorNumerico(getTipoLayout())
				|| getTipoLayoutExt19() || getTipoLayout24() || getTipoLayout24PortariaMec());
	}

	public boolean isApresentarFiltroApresentarDisciplinaPeriodoTrancadoCanceladoTransferido() {
		return (isApresentarCampos() && (getIsTipoLayout4() || getIsTipoLayout12() || getIsTipoLayout13()
				|| Uteis.getIsValorNumerico(getTipoLayout())) && getFiltro() == 1)
				|| (isApresentarCampos() && (getIsTipoLayout15()));
	}

	public void inicializarDadosSelecaoLayout() {
		if (getTipoLayout().equals("HistoricoAlunoLayout17Rel")
				|| getTipoLayout().equals("HistoricoAlunoLayout18PortariaMECRel")
				|| Uteis.getIsValorNumerico(getTipoLayout())) {
			setOrdem(OrdemHistoricoDisciplina.PERIODO_LETIVO.getValor());
		}
	}

	public boolean getIsTipoLayout5PortariaMEC() {
		return getTipoLayout().equals("HistoricoAlunoLayout5PortariaMECRel");
	}

	public RequerimentoVO getRequerimentoVO() {
		if (requerimentoVO == null) {
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}

	public Boolean getApresentarCoordenadorTitularCurso() {
		if (apresentarCoordenadorTitularCurso == null) {
			apresentarCoordenadorTitularCurso = Boolean.FALSE;
		}
		return apresentarCoordenadorTitularCurso;
	}

	public void setApresentarCoordenadorTitularCurso(Boolean apresentarCoordenadorTitularCurso) {
		this.apresentarCoordenadorTitularCurso = apresentarCoordenadorTitularCurso;
	}

	public List<SelectItem> listaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento;

	public List<SelectItem> getListaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento() {
		if (listaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento == null) {
			listaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento = new ArrayList<SelectItem>(0);
			listaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento.add(new SelectItem(
					"PROFESSOR_APROVEITAMENTO_DISCIPLINA", "Professor Lançamento Aproveitamento Disciplina"));
			listaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento
					.add(new SelectItem("PROFESSOR_TURMA_BASE", "Professor Turma Base"));
			listaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento
					.add(new SelectItem("COORDENADOR_CURSO", "Coordenador Curso"));
			listaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento
					.add(new SelectItem("NAO_APRESENTAR", "Não Apresentar"));
		}
		return listaSelectItemRegraApresentacaoProfessorDisciplinaAproveitamento;
	}

	public String getRegraApresentacaoProfessorDisciplinaAproveitamento() {
		if (regraApresentacaoProfessorDisciplinaAproveitamento == null) {
			regraApresentacaoProfessorDisciplinaAproveitamento = "";
		}
		return regraApresentacaoProfessorDisciplinaAproveitamento;
	}

	public void setRegraApresentacaoProfessorDisciplinaAproveitamento(
			String regraApresentacaoProfessorDisciplinaAproveitamento) {
		this.regraApresentacaoProfessorDisciplinaAproveitamento = regraApresentacaoProfessorDisciplinaAproveitamento;
	}

	public Boolean getLayoutPortariaMEC() {
		return (getFacadeFactory().getHistoricoAlunoRelFacade().validarTipoLayoutGraduacao(tipoLayout, getConfiguracaoLayoutHistoricoSelecionadoVO()));
	}

	public Boolean getApresentarDataInicioTerminoAlteracoesCadastrais() {
		return getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao();
	}	

	public Boolean getDataInicioTerminoAlteracoesCadastrais() {
		if (dataInicioTerminoAlteracoesCadastrais == null) {
			dataInicioTerminoAlteracoesCadastrais = false;
		}
		return dataInicioTerminoAlteracoesCadastrais;
	}

	public void setDataInicioTerminoAlteracoesCadastrais(Boolean dataInicioTerminoAlteracoesCadastrais) {
		this.dataInicioTerminoAlteracoesCadastrais = dataInicioTerminoAlteracoesCadastrais;
	}

	private void recuperarNomeFuncionarios() {
		try {
			recuperarNomeFuncionario(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getPessoa());
			recuperarNomeFuncionario(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getPessoa());
			recuperarNomeFuncionario(getHistoricoAlunoRelVO().getFuncionarioTerciarioVO().getPessoa());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void recuperarNomeFuncionario(PessoaVO pessoaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(pessoaVO)) {
			PessoaVO consultarPorChavePrimaria = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(
					pessoaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(consultarPorChavePrimaria)) {
				pessoaVO.setNome(consultarPorChavePrimaria.getNome());
			}
		}
	}

	public Boolean getTrazerTodosProfessoresDisciplinas() {
		if(trazerTodosProfessoresDisciplinas == null) {
			trazerTodosProfessoresDisciplinas = Boolean.FALSE;
		}
		return trazerTodosProfessoresDisciplinas;
	}

	public void setTrazerTodosProfessoresDisciplinas(Boolean trazerTodosProfessoresDisciplinas) {
		this.trazerTodosProfessoresDisciplinas = trazerTodosProfessoresDisciplinas;
	}
	

	private List<ConfiguracaoHistoricoVO> configuracaoHistoricoVOs;
	private ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO;
	private ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoSelecionadoVO;
	private TipoRelatorioEnum tipoRelatorioEnum;
	private ConfiguracaoHistoricoVO configuracaoHistoricoVO;
	private ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO;
	private List<SelectItem> listaSelectItemTipoObservacaoHistorico;

	public ConfiguracaoHistoricoVO getConfiguracaoHistoricoVO() {
		if (configuracaoHistoricoVO == null) {
			configuracaoHistoricoVO = new ConfiguracaoHistoricoVO();
		}
		return configuracaoHistoricoVO;
	}

	public void setConfiguracaoHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		this.configuracaoHistoricoVO = configuracaoHistoricoVO;
	}

	public void consultarConfiguracaoHistorico() {
		try {
			setConfiguracaoHistoricoVOs(getFacadeFactory().getConfiguracaoHistoricoFacade()
					.consultarConfiguracoesHistorico(getUsuarioLogado()));
			getConfiguracaoHistoricoVOs().forEach(t -> t.setEdicaoManual(true));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<ConfiguracaoHistoricoVO> getConfiguracaoHistoricoVOs() {
		if (configuracaoHistoricoVOs == null) {
			configuracaoHistoricoVOs = new ArrayList<ConfiguracaoHistoricoVO>(0);
		}
		return configuracaoHistoricoVOs;
	}

	public void setConfiguracaoHistoricoVOs(List<ConfiguracaoHistoricoVO> configuracaoHistoricoVOs) {
		this.configuracaoHistoricoVOs = configuracaoHistoricoVOs;
	}

	public ConfiguracaoLayoutHistoricoVO getConfiguracaoLayoutHistoricoVO() {
		if (configuracaoLayoutHistoricoVO == null) {
			configuracaoLayoutHistoricoVO = new ConfiguracaoLayoutHistoricoVO();
		}
		return configuracaoLayoutHistoricoVO;
	}

	public void setConfiguracaoLayoutHistoricoVO(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		this.configuracaoLayoutHistoricoVO = configuracaoLayoutHistoricoVO;
	}

	public TipoRelatorioEnum getTipoRelatorioEnum() {
		if (tipoRelatorioEnum == null) {
			tipoRelatorioEnum = TipoRelatorioEnum.PDF;
		}
		return tipoRelatorioEnum;
	}

	public void setTipoRelatorioEnum(TipoRelatorioEnum tipoRelatorioEnum) {
		this.tipoRelatorioEnum = tipoRelatorioEnum;
	}

	public ConfiguracaoObservacaoHistoricoVO getConfiguracaoObservacaoHistoricoVO() {
		if (configuracaoObservacaoHistoricoVO == null) {
			configuracaoObservacaoHistoricoVO = new ConfiguracaoObservacaoHistoricoVO();
		}
		return configuracaoObservacaoHistoricoVO;
	}

	public void setConfiguracaoObservacaoHistoricoVO(
			ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO) {
		this.configuracaoObservacaoHistoricoVO = configuracaoObservacaoHistoricoVO;
	}

	public void novoConfiguracaoLayoutHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		setOncompleteModal("");
		setConfiguracaoLayoutHistoricoVO(new ConfiguracaoLayoutHistoricoVO());
		getConfiguracaoLayoutHistoricoVO().setConfiguracaoHistoricoVO(configuracaoHistoricoVO);
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public void novoConfiguracaoObservacaoHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		setOncompleteModal("");
		setConfiguracaoObservacaoHistoricoVO(new ConfiguracaoObservacaoHistoricoVO());
		getConfiguracaoObservacaoHistoricoVO().setConfiguracaoHistoricoVO(configuracaoHistoricoVO);
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public void editarConfiguracaoLayoutHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		setOncompleteModal("");
		setConfiguracaoLayoutHistoricoVO(new ConfiguracaoLayoutHistoricoVO());
		setConfiguracaoLayoutHistoricoVO(configuracaoLayoutHistoricoVO);
		getConfiguracaoLayoutHistoricoVO().setConfiguracaoHistoricoVO(configuracaoHistoricoVO);
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public void persistirConfiguracaoLayoutHistorico(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		try {
			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().persistir(
					getConfiguracaoHistoricoVOs().stream()
							.filter(l -> l.getNivelEducacional()
									.equals(getConfiguracaoLayoutHistoricoVO().getConfiguracaoHistoricoVO()
											.getNivelEducacional()))
							.findFirst().get(),
					configuracaoLayoutHistoricoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setOncompleteModal("RichFaces.$('panelConfiguracaoLayout').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDefinicaoArquivoPrincipalConfiguracaoLayoutHistorico(
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, ArquivoVO arquivoVO) {
		try {

			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade()
					.realizarDefinicaoArquivoPrincipalConfiguracaoLayoutHistorico(configuracaoLayoutHistoricoVO,
							arquivoVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarConfiguracaoLayoutHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		try {
			configuracaoLayoutHistoricoVO.setOcultarLayout(true);
			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().persistir(configuracaoHistoricoVO,
					configuracaoLayoutHistoricoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarTodasConfiguracaoLayoutHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		try {
			configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().forEach(t -> t.setOcultarLayout(true));
			getFacadeFactory().getConfiguracaoHistoricoFacade().persistir(configuracaoHistoricoVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativarTodasConfiguracaoLayoutHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		try {
			configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().forEach(t -> t.setOcultarLayout(false));
			getFacadeFactory().getConfiguracaoHistoricoFacade().persistir(configuracaoHistoricoVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativarConfiguracaoLayoutHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		try {
			configuracaoLayoutHistoricoVO.setOcultarLayout(false);
			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().persistir(configuracaoHistoricoVO,
					configuracaoLayoutHistoricoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirConfiguracaoLayoutHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		try {
			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().excluir(configuracaoHistoricoVO,
					configuracaoLayoutHistoricoVO, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void cancelarConfiguracaoLayoutHistorico() {
		if (Uteis.isAtributoPreenchido(getConfiguracaoLayoutHistoricoVO())) {
			try {
				ConfiguracaoHistoricoVO configuracaoHistoricoVO = getConfiguracaoHistoricoVOs().stream()
						.filter(l -> l.getNivelEducacional().equals(
								getConfiguracaoLayoutHistoricoVO().getConfiguracaoHistoricoVO().getNivelEducacional()))
						.findFirst().get();
				setConfiguracaoLayoutHistoricoVO(getFacadeFactory().getConfiguracaoLayoutHistoricoFacade()
						.consultarPorChavePrimaria(getConfiguracaoLayoutHistoricoVO().getCodigo(), getUsuarioLogado()));
				configuracaoHistoricoVO.getConfiguracaoLayoutHistoricoVOs().set(configuracaoHistoricoVO
						.getConfiguracaoLayoutHistoricoVOs().indexOf(getConfiguracaoLayoutHistoricoVO()),
						getConfiguracaoLayoutHistoricoVO());
				limparMensagem();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void persistirConfiguracaoHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		try {
			getFacadeFactory().getConfiguracaoHistoricoFacade().persistir(configuracaoHistoricoVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void uploadLayout(FileUploadEvent fileUploadEvent) {
		try {

			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().adicionarLayout(fileUploadEvent,
					getTipoRelatorioEnum(),
					getConfiguracaoHistoricoVOs().stream()
							.filter(l -> l.getNivelEducacional()
									.equals(getConfiguracaoLayoutHistoricoVO().getConfiguracaoHistoricoVO()
											.getNivelEducacional()))
							.findFirst().get(),
					getConfiguracaoLayoutHistoricoVO(), getUsuarioLogado());

			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerLayout(ArquivoVO arquivoVO, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		try {
			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().removerLayout(arquivoVO,
					configuracaoLayoutHistoricoVO, getUsuarioLogado());
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarObservacaoLayout() {
		try {
			getFacadeFactory().getConfiguracaoObservacaoHistoricoFacade().adicionarConfiguracaoObservacaoHistoricoVO(
					getConfiguracaoObservacaoHistoricoVO(),
					getConfiguracaoHistoricoVOs().stream()
							.filter(l -> l.getNivelEducacional().equals(getConfiguracaoObservacaoHistoricoVO()
									.getConfiguracaoHistoricoVO().getNivelEducacional()))
							.findFirst().get(),
					getUsuarioLogado());
			setOncompleteModal("RichFaces.$('panelConfiguracaoObservacao').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarAtivacaoObservacaoLayout(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO,
			ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		try {
			getFacadeFactory().getConfiguracaoObservacaoHistoricoFacade().adicionarConfiguracaoObservacaoHistoricoVO(
					configuracaoObservacaoHistoricoVO, configuracaoHistoricoVO, getUsuarioLogado());
			setMensagemID("msg_dados_ativados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerObservacaoLayout(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO,
			ConfiguracaoHistoricoVO configuracaoHistoricoVO) {
		try {
			getFacadeFactory().getConfiguracaoObservacaoHistoricoFacade().removerConfiguracaoObservacaoHistoricoVO(
					configuracaoObservacaoHistoricoVO, configuracaoHistoricoVO, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(configuracaoObservacaoHistoricoVO)) {
				setMensagemID("msg_dados_inativados", Uteis.SUCESSO);
			} else {
				setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDownloadArquivo(ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO,
			ArquivoVO arquivoVO) throws Exception {
		if (configuracaoLayoutHistoricoVO.getLayoutFixoSistema()) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			cloneArquivo.setPastaBaseArquivo(UteisJSF.getCaminhoWeb().replace("/", File.separator) + File.separator
					+ "WEB-INF" + File.separator + "classes" + File.separator + cloneArquivo.getPastaBaseArquivo());
			context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);
		} else if (!arquivoVO.getPastaBaseArquivo()
				.startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo())) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()
					+ File.separator + cloneArquivo.getPastaBaseArquivo());
			context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);
		} else {
			context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		}

	}
	
	public void realizarDefinicaoLayoutPadraoConfiguracaoLayoutHistorico(
			ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) {
		try {
			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade()
					.realizarDefinicaoLayoutPadraoConfiguracaoLayoutHistorico(configuracaoHistoricoVO,
							configuracaoLayoutHistoricoVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDefinicaoObservacaoPadraoConfiguracaoObservacaoHistorico(
			ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO) {
		try {
			getFacadeFactory().getConfiguracaoObservacaoHistoricoFacade()
					.realizarDefinicaoObservacaoPadraoConfiguracaoObservacaoHistorico(configuracaoHistoricoVO,
							configuracaoObservacaoHistoricoVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDefinicaoConfiguracaoHistoricoUsar() {
		try {
			if (Uteis.isAtributoPreenchido(getMatriculaVO().getCurso().getNivelEducacional())) {
				setConfiguracaoHistoricoVO(getFacadeFactory().getConfiguracaoHistoricoFacade()
						.consultarConfiguracaoHistoricoPorNivelEducacional(
								TipoNivelEducacional.getEnum(getMatriculaVO().getCurso().getNivelEducacional()),
								getUsuarioLogado()));
			} else if (Uteis.isAtributoPreenchido(getTurmaVO().getCurso().getNivelEducacional())) {
				setConfiguracaoHistoricoVO(getFacadeFactory().getConfiguracaoHistoricoFacade()
						.consultarConfiguracaoHistoricoPorNivelEducacional(
								TipoNivelEducacional.getEnum(getTurmaVO().getCurso().getNivelEducacional()),
								getUsuarioLogado()));
			} else if (getIsApresentarComboTipoDesconto()) {
				setConfiguracaoHistoricoVO(getFacadeFactory().getConfiguracaoHistoricoFacade()
						.consultarConfiguracaoHistoricoPorNivelEducacional(TipoNivelEducacional.SUPERIOR,
								getUsuarioLogado()));
			}
			
			if (Uteis.isAtributoPreenchido(getConfiguracaoHistoricoVO())) {
				setObservacaoComplementarIntegralizado(getConfiguracaoHistoricoVO().getObservacaoPadraoIntegralizacao());
				setTextoCertificadoEstudo(getConfiguracaoHistoricoVO().getObservacaoPadraoCertificadoEstudos());
				if(!Uteis.isAtributoPreenchido(getObservacaoComplementar())) {
					setObservacaoComplementar(getConfiguracaoHistoricoVO().getObservacaoPadraoComplementar());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public Boolean getTipoLayoutPersonalizado() {
		return Uteis.isAtributoPreenchido(getTipoLayout()) && Uteis.getIsValorNumerico(getTipoLayout());
	}

	public List<SelectItem> getListaSelectItemTipoObservacaoHistorico() {
		if(listaSelectItemTipoObservacaoHistorico == null) {
			listaSelectItemTipoObservacaoHistorico = UtilSelectItem.getListaSelectItemEnum(TipoObservacaoHistoricoEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoObservacaoHistorico;
	}

	public void setListaSelectItemTipoObservacaoHistorico(List<SelectItem> listaSelectItemTipoObservacaoHistorico) {
		this.listaSelectItemTipoObservacaoHistorico = listaSelectItemTipoObservacaoHistorico;
	}

	public ConfiguracaoLayoutHistoricoVO getConfiguracaoLayoutHistoricoSelecionadoVO() {
		if(configuracaoLayoutHistoricoSelecionadoVO == null) {
			configuracaoLayoutHistoricoSelecionadoVO = new ConfiguracaoLayoutHistoricoVO();
		}
		return configuracaoLayoutHistoricoSelecionadoVO;
	}

	public void setConfiguracaoLayoutHistoricoSelecionadoVO(
			ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoSelecionadoVO) {
		this.configuracaoLayoutHistoricoSelecionadoVO = configuracaoLayoutHistoricoSelecionadoVO;
	} 
	
	public void carregarDadosLayout() {
		try {
			if (Uteis.getIsValorNumerico(getTipoLayout())) {
				if (Uteis.isAtributoPreenchido(getConfiguracaoHistoricoVO()) && getConfiguracaoHistoricoVO().getConfiguracaoLayoutHistoricoVOs().stream().anyMatch(l -> l.getCodigo().toString().equals(getTipoLayout()))) {
					setConfiguracaoLayoutHistoricoSelecionadoVO(null);
					setConfiguracaoLayoutHistoricoSelecionadoVO(getConfiguracaoHistoricoVO().getConfiguracaoLayoutHistoricoVOs().stream().filter(l -> l.getCodigo().toString().equals(getTipoLayout())).findFirst().get());
				} else {
					setConfiguracaoLayoutHistoricoSelecionadoVO(getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().consultarPorChavePrimaria(Integer.parseInt(getTipoLayout()), getUsuario()));
				}
			} else {
				setConfiguracaoLayoutHistoricoSelecionadoVO(new ConfiguracaoLayoutHistoricoVO());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}

	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}
	
	public void limparProgramacaoFormatura() {
		removerObjetoMemoria(getProgramacaoFormaturaVO());
		Uteis.liberarListaMemoria(getListaConsultaProgramacaoFormatura());
	}
	
	
	 /**
    * Rotina responsavel por executar as consultas disponiveis no JSP ProgramacaoFormaturaCons.jsp. Define o tipo de
    * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
    * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
    */
   public String consultarProgramacaoFormatura() {
       try {
           super.consultar();
           List objs = new ArrayList(0);
			

	            if (getCampoConsultaProgramacaoFormatura().equals("codigo")) {
	                if (getValorConsultaProgramacaoFormatura().equals("")) {
	                    setValorConsultaProgramacaoFormatura("0");
	                }
	                if (getValorConsultaProgramacaoFormatura().trim() != null || !getValorConsultaProgramacaoFormatura().trim().isEmpty()) {
	                    Uteis.validarSomenteNumeroString(getValorConsultaProgramacaoFormatura().trim());
	                }
	                int valorInt = Integer.parseInt(getValorConsultaProgramacaoFormatura());
	                            	
	                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	                
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("nomeUnidadeEnsino")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("colacaoGrau")) {
	            	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorColacaoGrau(getValorConsultaProgramacaoFormatura(), null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("nomeCurso")) {
	            	          
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCurso(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
	                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("nomeTurno")) {
	            	          
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurno(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
	                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("identificadorTurmaTurma")) {
	            	
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaTurma(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
	                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("matriculaMatricula")) {
	            	
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatricula(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
	                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("nomeUsuario")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuario(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")
	                    || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
	                objs = validarDataConsultaProgramacaoFormatura(objs);
	            }
	            setListaConsultaProgramacaoFormatura(objs);
	            setMensagemID("msg_dados_consultados");
	            return "consultar";
			
       } catch (Exception e) {
       	setListaConsultaProgramacaoFormatura(new ArrayList(0));
           setMensagemDetalhada("msg_erro", e.getMessage());
           return "consultar";
       }
   }
   
	public boolean getApresentarResultadoConsultaProgramacaoFormatura() {
		if (this.getListaConsultaProgramacaoFormatura() == null || this.getListaConsultaProgramacaoFormatura().size() == 0) {
			return false;
		}
		return true;
	}

   public List validarDataConsultaProgramacaoFormatura(List objs) throws Exception {
       if (getValorConsultaDataFinalProgramacaoFormatura() != null && getValorConsultaDataInicioProgramacaoFormatura() != null) {
           if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento")) {
           	if(getFiltroAlunosPresentesColacaoGrau().equals("NI")) {          		            	
           		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimento(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                       Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
           	}else {
           		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimentoFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                           Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
           	}
           }
           if (getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")) {
           	if(getFiltroAlunosPresentesColacaoGrau().equals("NI")) {
           		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                       Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
           	}else {
           		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrauFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                           Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
           	}
           }
           if (getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
           	if(getFiltroAlunosPresentesColacaoGrau().equals("NI")) {
           		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastro(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                       Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
           	}else {
           		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastroFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                           Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
           	}
           }
       } else {
           throw new ConsistirException("Por favor digite uma data válida.");
       }

       return objs;
   }

   public void selecionarProgramacaoFormatura() throws Exception {
		selecionarProgramacaoFormatura(null);
	}
  
  public void selecionarProgramacaoFormatura(Integer codigoProgramacaoFormatura) throws Exception {
		try {
			listaTipoLayout = null;
			ProgramacaoFormaturaVO obj = null;
			if (Uteis.isAtributoPreenchido(codigoProgramacaoFormatura)) {
				obj = new ProgramacaoFormaturaVO();
				obj.setCodigo(codigoProgramacaoFormatura);
			} else {
				obj = (ProgramacaoFormaturaVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaItens");
			}
			obj.setNovoObj(Boolean.FALSE);
			setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setTipoNivelEducacional(getProgramacaoFormaturaVO().getNivelEducacional());
			getHistoricoAlunoRelVO().setFuncionarioPrincipalVO(new FuncionarioVO());
			getHistoricoAlunoRelVO().setCargoFuncionarioPrincipal(new CargoVO());
			getHistoricoAlunoRelVO().setTituloFuncionarioPrincipal(Constantes.EMPTY);
			getHistoricoAlunoRelVO().setFuncionarioSecundarioVO(new FuncionarioVO());
			getHistoricoAlunoRelVO().setCargoFuncionarioSecundario(new CargoVO());
			getHistoricoAlunoRelVO().setTituloFuncionarioSecundario(Constantes.EMPTY);
			getHistoricoAlunoRelVO().setFuncionarioTerciarioVO(new FuncionarioVO());
			getHistoricoAlunoRelVO().setCargoFuncionarioTerciario(new CargoVO());
			getHistoricoAlunoRelVO().setTituloFuncionarioTerciario(Constantes.EMPTY);
			verificarParametroRelatorio();
			getListaTipoLayout();
			setAno("");
			setSemestre("");
			setValorConsultaProgramacaoFormatura("");
			setCampoConsultaProgramacaoFormatura("");
			getListaConsultaProgramacaoFormatura().clear();
			montarListaUnidadeCertificadora();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
   }
   
   public void mostrarSegundoCampoProgramacaoFormatura() {
       if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")
               || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
       	setMostrarSegundoCampoProgramacaoFormatura(true);
       } else {
       	setMostrarSegundoCampoProgramacaoFormatura(false);
       }
       setValorConsultaProgramacaoFormatura("");
       getListaConsultaProgramacaoFormatura().clear();
   }
   
	public List<ProgramacaoFormaturaVO> getListaConsultaProgramacaoFormatura() {
		if (listaConsultaProgramacaoFormatura == null) {
			listaConsultaProgramacaoFormatura = new ArrayList<ProgramacaoFormaturaVO>(0);
		}
		return listaConsultaProgramacaoFormatura;
	}

	public void setListaConsultaProgramacaoFormatura(List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura) {
		this.listaConsultaProgramacaoFormatura = listaConsultaProgramacaoFormatura;
	} 	
   
   public String getValorConsultaProgramacaoFormatura() {
   	if(valorConsultaProgramacaoFormatura == null) {
   		valorConsultaProgramacaoFormatura = "";
   	}
		return valorConsultaProgramacaoFormatura;
	}

	public void setValorConsultaProgramacaoFormatura(String valorConsultaProgramacaoFormatura) {
		this.valorConsultaProgramacaoFormatura = valorConsultaProgramacaoFormatura;
	}

	public String getCampoConsultaProgramacaoFormatura() {
   	if(campoConsultaProgramacaoFormatura == null) {
   		campoConsultaProgramacaoFormatura = "colacaoGrau";
   	}
		return campoConsultaProgramacaoFormatura;
	}

	public void setCampoConsultaProgramacaoFormatura(String campoConsultaProgramacaoFormatura) {
		this.campoConsultaProgramacaoFormatura = campoConsultaProgramacaoFormatura;
	}

	/**
    * @return the mostrarSegundoCampoProgramacaoFormatura
    */
   public Boolean getMostrarSegundoCampoProgramacaoFormatura() {
       if (mostrarSegundoCampoProgramacaoFormatura == null) {
       	mostrarSegundoCampoProgramacaoFormatura = false;
       }
       return mostrarSegundoCampoProgramacaoFormatura;
   }

   /**
    * @param mostrarSegundoCampoProgramacaoFormatura
    *            the mostrarSegundoCampoProgramacaoFormatura to set
    */
   public void setMostrarSegundoCampoProgramacaoFormatura(Boolean mostrarSegundoCampoProgramacaoFormatura) {
       this.mostrarSegundoCampoProgramacaoFormatura = mostrarSegundoCampoProgramacaoFormatura;
   }
   
   /**
    * @return the valorConsultaDataInicioProgramacaoFormatura
    */
   public Date getValorConsultaDataInicioProgramacaoFormatura() {
       return valorConsultaDataInicioProgramacaoFormatura;
   }

   /**
    * @param valorConsultaDataInicioProgramacaoFormatura
    *            the valorConsultaDataInicioProgramacaoFormatura to set
    */
   public void setValorConsultaDataInicioProgramacaoFormatura(Date valorConsultaDataInicioProgramacaoFormatura) {
       this.valorConsultaDataInicioProgramacaoFormatura = valorConsultaDataInicioProgramacaoFormatura;
   }

   /**
    * @return the valorConsultaDataFinalProgramacaoFormatura
    */
   public Date getValorConsultaDataFinalProgramacaoFormatura() {
       return valorConsultaDataFinalProgramacaoFormatura;
   }

   /**
    * @param valorConsultaDataFinalProgramacaoFormatura
    *            the valorConsultaDataFinalProgramacaoFormatura to set
    */
   public void setValorConsultaDataFinalProgramacaoFormatura(Date valorConsultaDataFinalProgramacaoFormatura) {
       this.valorConsultaDataFinalProgramacaoFormatura = valorConsultaDataFinalProgramacaoFormatura;
   }
   
   /**
    * Rotina responsável por preencher a combo de consulta da telas.
    */
   public List<SelectItem> tipoConsultaComboProgramacaoFormatura;
   public List<SelectItem> getTipoConsultaComboProgramacaoFormatura() {
	   if(tipoConsultaComboProgramacaoFormatura == null) {
	   tipoConsultaComboProgramacaoFormatura = new ArrayList<SelectItem>(0);
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("colacaoGrau", "Colação Grau"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("codigo", "Código"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("nomeCurso", "Curso"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("nomeTurno", "Turno"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("identificadorTurmaTurma", "Turma"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("matriculaMatricula", "Matricula"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("periodoRequerimento", "Período Requerimento"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("periodoColacaoGrau", "Período Colação Grau"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("periodoCadastro", "Período Cadastro"));
	   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("nomeUsuario", "Responsável Cadastro"));
	   }
       return tipoConsultaComboProgramacaoFormatura;
   }
   
   public List<SelectItem> comboFiltroAlunosPresentesColacaoGrau;
   public List<SelectItem> getComboFiltroAlunosPresentesColacaoGrau() {
	   if(comboFiltroAlunosPresentesColacaoGrau == null) {
	   comboFiltroAlunosPresentesColacaoGrau = new ArrayList<SelectItem>(0);
	   comboFiltroAlunosPresentesColacaoGrau.add(new SelectItem("SI", "Presentes"));
	   comboFiltroAlunosPresentesColacaoGrau.add(new SelectItem("NO", "Ausentes"));
	   comboFiltroAlunosPresentesColacaoGrau.add(new SelectItem("NI", "Ambos"));
	   }
       return comboFiltroAlunosPresentesColacaoGrau;
   }

	public String getFiltroAlunosPresentesColacaoGrau() {
		if(filtroAlunosPresentesColacaoGrau == null) {
			filtroAlunosPresentesColacaoGrau = "NI";
		}
	
		return filtroAlunosPresentesColacaoGrau;
	}

	public void setFiltroAlunosPresentesColacaoGrau(String filtroAlunosPresentesColacaoGrau) {
		this.filtroAlunosPresentesColacaoGrau = filtroAlunosPresentesColacaoGrau;
	}
	
	public Boolean getIsFiltrarPorProgramacaoFormatura() {
		if (getCampoFiltroPor().equals("programacaoFormatura")) {
			return true;
		}
		return false;
	}
	
	public void realizarGeracaoXmlHistorico(HistoricoAlunoRelVO histAlunoRelVO, File arquivoVisualHistoricoEscolar, UsuarioVO usuario) throws Exception {
		try {
			if (getGerarXmlHistoricoEscolarDigital()) {
				setFazerDownload(Boolean.FALSE);
				ExpedicaoDiplomaVO expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
				expedicaoDiplomaVO.setCodigo(getFacadeFactory().getExpedicaoDiplomaFacade().consultarExpedicaoDiplomaUtilizarHistoricoDigital(histAlunoRelVO.getMatriculaVO().getMatricula()));
				if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO)) {
					expedicaoDiplomaVO = getFacadeFactory().getExpedicaoDiplomaFacade().carregarDadosCompletoExpedicaoDiploma(expedicaoDiplomaVO.getCodigo(), usuario);
				} else {
					expedicaoDiplomaVO.setMatricula(histAlunoRelVO.getMatriculaVO());
					expedicaoDiplomaVO.setPercentualCHIntegralizacaoMatricula(Uteis.getDoubleFormatado(getFacadeFactory().getHistoricoFacade().consultarPercentualCHIntegralizacaoPorMatriculaGradeCurricular(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuario)));
					expedicaoDiplomaVO.setCargaHorariaTotal(getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaExigidaGrade(expedicaoDiplomaVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuario));
					expedicaoDiplomaVO.setCargaHorariaCursada(getFacadeFactory().getDisciplinaFacade().consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalente(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getGradeCurricularAtual().getCodigo(), true, usuario));
				}
				expedicaoDiplomaVO.setGradeCurricularVO(histAlunoRelVO.getGradeCurricularVO());
				expedicaoDiplomaVO.setFuncionarioPrimarioVO(histAlunoRelVO.getFuncionarioPrincipalVO());
				expedicaoDiplomaVO.setCargoFuncionarioPrincipalVO(histAlunoRelVO.getCargoFuncionarioPrincipal());
				expedicaoDiplomaVO.setTituloFuncionarioPrincipal(histAlunoRelVO.getTituloFuncionarioPrincipal());
				expedicaoDiplomaVO.setFuncionarioTerceiroVO(histAlunoRelVO.getFuncionarioSecundarioVO());
				expedicaoDiplomaVO.setCargoFuncionarioTerceiroVO(histAlunoRelVO.getCargoFuncionarioSecundario());
				expedicaoDiplomaVO.setTituloFuncionarioTerceiro(histAlunoRelVO.getTituloFuncionarioSecundario());
				expedicaoDiplomaVO.setUnidadeEnsinoCertificadora(getUnidadeEnsinoCertificadora());
				expedicaoDiplomaVO.setVersaoDiploma(getVersaoDiploma());
				if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO)) {
					getFacadeFactory().getExpedicaoDiplomaFacade().montarNumeroProcessoERegistroDiplomaVindoMascaraConfiguracaoAcademico(expedicaoDiplomaVO, expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), usuario);
				}
				verificarConfiguracaoDiplomaExistente(expedicaoDiplomaVO, null);
				if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora())) {
					getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora(), usuario);
				} else {
					throw new Exception("A UNIDADE CERTIFICADORA deve ser preenchida.");
				}
				ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigo(), usuario);
				getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoXMLDiplomaDigital(expedicaoDiplomaVO, configGEDVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo()), null, usuario, arquivoVisualHistoricoEscolar, TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL, true, true, histAlunoRelVO);
			}
		} catch (ConsistirException ce) {
			throw ce;
		} catch (Exception e) {
			throw e;
		}
	}

	public void verificarConfiguracaoDiplomaExistente(ExpedicaoDiplomaVO expedicaoDiplomaVO, MatriculaVO matriculaVO) throws Exception {
		if (expedicaoDiplomaVO != null) {
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula()) && expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
				expedicaoDiplomaVO.setConfiguracaoDiplomaDigital(getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().consultarConfiguracaoExistente(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
				montarDadosCoordenadorCurso(expedicaoDiplomaVO);
			}
		} else {
			if (Uteis.isAtributoPreenchido(matriculaVO) && matriculaVO.getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
				setConfiguracaoDiplomaDigital(getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().consultarConfiguracaoExistente(matriculaVO.getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
				if (Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital())) {
					setVersaoDiploma(getConfiguracaoDiplomaDigital().getVersao());
					setUnidadeEnsinoCertificadora(getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao());
				}
			}
		}
	}

	public void montarDadosCoordenadorCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital())) {
			List<CursoCoordenadorVO> coordenadorVOs = getFacadeFactory().getCursoCoordenadorFacade().consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(0, expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), Constantes.EMPTY, expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), 0, true, true, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(coordenadorVOs) && coordenadorVOs.stream().anyMatch(coordenador -> Uteis.isAtributoPreenchido(coordenador.getUnidadeEnsino()))) {
				expedicaoDiplomaVO.setCursoCoordenadorVO(coordenadorVOs.stream().filter(coordenador -> Uteis.isAtributoPreenchido(coordenador.getUnidadeEnsino())).findFirst().get());
				List<FormacaoAcademicaVO> formacaoAcademica = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(expedicaoDiplomaVO.getCursoCoordenadorVO().getFuncionario().getPessoa().getCodigo(), getUsuarioLogado(), 1);
				if (!formacaoAcademica.isEmpty()) {
					expedicaoDiplomaVO.setFormacaoAcademicaVO(formacaoAcademica.get(0));
				}
			} else if (Uteis.isAtributoPreenchido(coordenadorVOs)) {
				expedicaoDiplomaVO.setCursoCoordenadorVO(coordenadorVOs.get(0));
				List<FormacaoAcademicaVO> formacaoAcademica = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(expedicaoDiplomaVO.getCursoCoordenadorVO().getFuncionario().getPessoa().getCodigo(), getUsuarioLogado(), 1);
				if (!formacaoAcademica.isEmpty()) {
					expedicaoDiplomaVO.setFormacaoAcademicaVO(formacaoAcademica.get(0));
				}
			}
		}
	}

	public Boolean getGerarXmlHistorico() {
		if (gerarXmlHistorico == null) {
			gerarXmlHistorico = false;
		}
		return gerarXmlHistorico;
	}

	public void setGerarXmlHistorico(Boolean gerarXmlHistorico) {
		this.gerarXmlHistorico = gerarXmlHistorico;
	}

	public void montarListaUnidadeCertificadora() throws Exception {
		try {
			setListaSelectItemUnidadeEnsinoCertificadora(new ArrayList<SelectItem>(0));
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("%", null, Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
				getListaSelectItemUnidadeEnsinoCertificadora().add(new SelectItem(null, Constantes.EMPTY));
				unidadeEnsinoVOs.stream().map(u -> new SelectItem(u.getCodigo(), u.getNome())).forEach(getListaSelectItemUnidadeEnsinoCertificadora()::add);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public VersaoDiplomaDigitalEnum getVersaoDiploma() {
		if (versaoDiploma == null) {
			versaoDiploma = VersaoDiplomaDigitalEnum.VERSAO_1_05;
		}
		return versaoDiploma;
	}
	
	public void setVersaoDiploma(VersaoDiplomaDigitalEnum versaoDiploma) {
		this.versaoDiploma = versaoDiploma;
	}
	
	public void realizarMontagemFuncionarioAssinanteHistorico() {
		if (getGerarXmlHistorico()) {
			setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum.SEI);
		}
		if (Uteis.isAtributoPreenchido(getConfiguracaoDiplomaDigital()) && getGerarXmlHistoricoEscolarDigital()) {
			try {
				if (!Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO())) {
					getHistoricoAlunoRelVO().setFuncionarioPrincipalVO(getConfiguracaoDiplomaDigital().getFuncionarioPrimario());
					getHistoricoAlunoRelVO().setTituloFuncionarioPrincipal(getConfiguracaoDiplomaDigital().getTituloFuncionarioPrimario());
					setSelectItemsCargoFuncionarioPrincipal(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					getHistoricoAlunoRelVO().setCargoFuncionarioPrincipal(getConfiguracaoDiplomaDigital().getCargoFuncionarioPrimario());
				}
				if (!Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO())) {
					getHistoricoAlunoRelVO().setFuncionarioSecundarioVO(getConfiguracaoDiplomaDigital().getFuncionarioTerceiro());
					getHistoricoAlunoRelVO().setTituloFuncionarioSecundario(getConfiguracaoDiplomaDigital().getTituloFuncionarioTerceiro());
					setSelectItemsCargoFuncionarioSecundario(montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					getHistoricoAlunoRelVO().setCargoFuncionarioSecundario(getConfiguracaoDiplomaDigital().getCargoFuncionarioTerceiro());
				}
				if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoCertificadora())) {
					setUnidadeEnsinoCertificadora(getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao());
				}
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
	}
	
	public Boolean getGerarXmlHistoricoEscolarDigital() {
		if (isAssinarDigitalmente() && !getImprimirExcel()) {
			if (getGerarXmlHistorico() && getIsFiltrarPorAluno() && Uteis.isAtributoPreenchido(getMatriculaVO()) && getMatriculaVO().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
				return Boolean.TRUE;
			} else if (getGerarXmlHistorico() && getIsFiltrarPorProgramacaoFormatura() && Uteis.isAtributoPreenchido(getProgramacaoFormaturaVO()) && getProgramacaoFormaturaVO().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	public void validarDadosHistoricoEscolarDigital() throws Exception {
		if (getGerarXmlHistoricoEscolarDigital()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getUnidadeEnsinoCertificadora()), "O campo UNIDADE DE EXPEDIÇÃO DO XML deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getVersaoDiploma()), "O campo VERSÃO HISTÓRICO DIGITAL deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioPrincipalVO()), "O FUNCIONÁRIO E-CPF deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getCargoFuncionarioPrincipal()), "O CARGO DO FUNCIONÁRIO E-CPF deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getFuncionarioSecundarioVO()), "O FUNCIONÁRIO E-CNPJ deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getHistoricoAlunoRelVO().getCargoFuncionarioSecundario()), "O CARGO DO FUNCIONÁRIO E-CNPJ deve ser informado");
		}
	}
	
	public List<String> getListaErroHistoricoEscolarDigital() {
		if (listaErroHistoricoEscolarDigital == null) {
			listaErroHistoricoEscolarDigital = new ArrayList<>();
		}
		return listaErroHistoricoEscolarDigital;
	}
	
	public void setListaErroHistoricoEscolarDigital(List<String> listaErroHistoricoEscolarDigital) {
		this.listaErroHistoricoEscolarDigital = listaErroHistoricoEscolarDigital;
	}

	public List<HistoricoAlunoRelVO> getListaErroHistoricoAlunoRel() {
		if (listaErroHistoricoAlunoRel == null) {
			listaErroHistoricoAlunoRel = new ArrayList<>(0);
		}
		return listaErroHistoricoAlunoRel;
	}
	
	public void setListaErroHistoricoAlunoRel(List<HistoricoAlunoRelVO> listaErroHistoricoAlunoRel) {
		this.listaErroHistoricoAlunoRel = listaErroHistoricoAlunoRel;
	}
	
	public List<SelectItem> getListaSelectItemVersaoDiploma() {
		if (listaSelectItemVersaoDiploma == null) {
			listaSelectItemVersaoDiploma = new ArrayList<SelectItem>(0);
			for (VersaoDiplomaDigitalEnum obj : VersaoDiplomaDigitalEnum.values()) {
				listaSelectItemVersaoDiploma.add(new SelectItem(obj, obj.getDescricao()));
			}
		}
		return listaSelectItemVersaoDiploma;
	}
	
	public void setListaSelectItemVersaoDiploma(List<SelectItem> listaSelectItemVersaoDiploma) {
		this.listaSelectItemVersaoDiploma = listaSelectItemVersaoDiploma;
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsinoCertificadora() {
		if (listaSelectItemUnidadeEnsinoCertificadora == null) {
			listaSelectItemUnidadeEnsinoCertificadora = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsinoCertificadora;
	}
	
	public void setListaSelectItemUnidadeEnsinoCertificadora(List<SelectItem> listaSelectItemUnidadeEnsinoCertificadora) {
		this.listaSelectItemUnidadeEnsinoCertificadora = listaSelectItemUnidadeEnsinoCertificadora;
	}
	
	public String getOncomplete() {
		if (oncomplete == null) {
			oncomplete = Constantes.EMPTY;
		}
		return oncomplete;
	}
	
	public void setOncomplete(String oncomplete) {
		this.oncomplete = oncomplete;
	}
	
	public UnidadeEnsinoVO getUnidadeEnsinoCertificadora() {
		if (unidadeEnsinoCertificadora == null) {
			unidadeEnsinoCertificadora = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoCertificadora;
	}
	
	public void setUnidadeEnsinoCertificadora(UnidadeEnsinoVO unidadeEnsinoCertificadora) {
		this.unidadeEnsinoCertificadora = unidadeEnsinoCertificadora;
	}
	
	public ConfiguracaoDiplomaDigitalVO getConfiguracaoDiplomaDigital() {
		if (configuracaoDiplomaDigital == null) {
			configuracaoDiplomaDigital = new ConfiguracaoDiplomaDigitalVO();
		}
		return configuracaoDiplomaDigital;
	}
	
	public void setConfiguracaoDiplomaDigital(ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital) {
		this.configuracaoDiplomaDigital = configuracaoDiplomaDigital;
	}

}
