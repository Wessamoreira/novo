package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.ExpedicaoDiplomaControle;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.MapaNotaAlunoPorTurmaAlunoRelVO;
import relatorio.negocio.comuns.academico.MapaNotaAlunoPorTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.MapaNotaAlunoPorTurmaRel;

@Controller("MapaNotaAlunoPorTurmaRelControle")
@Scope("viewScope")
@Lazy
public class MapaNotaAlunoPorTurmaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private MapaNotaAlunoPorTurmaRelVO mapaNotaAlunoPorTurmaRelVO;
	private TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String tipoNota;

	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private List<SelectItem> listaSelectItemTipoDisciplina;
	private String tipoDisciplina;
	private String tipoAluno;
	private String unidadeEnsinoApresentar;
	private String tipoCurso;
	private String ano;
	private String semestre;
	private Date dataAtual;
	private List<SelectItem> listaSelectItemDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private DisciplinaVO disciplinaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private SalaLocalAulaVO salaLocalAula;
	private List<SalaLocalAulaVO> listaConsultaSalaLocalAula;
	private String campoConsultaSalaLocalAula;
	private String valorConsultaSalaLocalAula;
	private LocalAulaVO localAula;
	private String campoConsultaLocalAula;
	private String valorConsultaLocalAula;
	private List<LocalAulaVO> listaConsultaLocalAula;
	private FuncionarioVO funcionarioVO;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List<FuncionarioVO> listaConsultaProfessor;

	private String turnoApresentar;
	private String tipoLayout;
	private String ordenacao;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private FuncionarioVO funcionarioPrincipalVO;
	private FuncionarioVO funcionarioSecundarioVO;
	private CargoVO cargoFuncionarioPrincipal;
	private CargoVO cargoFuncionarioSecundario;

	private List<SelectItem> listaSelectItemTipoAluno;
	private List<SelectItem> listaSelectTipoOrdenacao;
	private List<SelectItem> listaSelectItemTipoLayout;
	private List<SelectItem> tipoConsultaComboLocalAula;
	private List<SelectItem> tipoConsultaComboProfessor;
	private List<SelectItem> tipoConsultaComboSalaLocalAula;
	private List<SelectItem> listaSelectItemTipoInformarNota;
	private List<SelectItem> selectItemsCargoFuncionarioPrincipal;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private List<SelectItem> selectItemsCargoFuncionarioSecundario;

	private Boolean marcarTodosTiposNotas;
	private List<String> listaNotas;
	private String tipoNotaApresentar;
	
	private List<ConfiguracaoAcademicaNotaVO> listaConfiguracaoAcademicaNotaVOs;
	
	public void marcarTodasTiposNotasAction() {
		for (ConfiguracaoAcademicaNotaVO configuracaoAcademicaNota : getListaConfiguracaoAcademicaNotaVOs()) {
			if (marcarTodosTiposNotas) {
				configuracaoAcademicaNota.setFiltrarConfiguracaoAcademicaNota(Boolean.TRUE);
			} else {
				configuracaoAcademicaNota.setFiltrarConfiguracaoAcademicaNota(Boolean.FALSE);
			}
		}
		verificarTodasTipoNotasSelecionados();	
	}
	
	public void verificarTodasTipoNotasSelecionados() {
		getListaNotas().clear();
		StringBuilder tipoNota = new StringBuilder();
		for (ConfiguracaoAcademicaNotaVO obj : getListaConfiguracaoAcademicaNotaVOs()) {
			if (obj.getFiltrarConfiguracaoAcademicaNota()) {				
				tipoNota.append(obj.getTitulo()).append("; ");
				getListaNotas().add(obj.getNotaTransiente());
			}
		}
		setTipoNotaApresentar(tipoNota.toString());
	}

	public MapaNotaAlunoPorTurmaRelControle() {
		setAno(Uteis.getAnoDataAtual4Digitos());
		setSemestre(Uteis.getSemestreAtual());
		consultarUnidadeEnsinoFiltroRelatorio("");
		verificarTodasUnidadeEnsinoSelecionados();
	}

	public void validarTurmaInformada() throws ConsistirException {
		if (getUnidadeEnsinosApresentar().length() == 0) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		if (getIsApresentarAno()) {
			if (!Uteis.isAtributoPreenchido(getMapaNotaAlunoPorTurmaRelVO().getAno()) || getMapaNotaAlunoPorTurmaRelVO().getAno().length() != 4) {
				throw new ConsistirException("O campo ANO deve ser informado.");
			}
		}
		if (getIsApresentarSemestre()) {
			if (!Uteis.isAtributoPreenchido(getMapaNotaAlunoPorTurmaRelVO().getSemestre())) {
				throw new ConsistirException("O campo SEMESTRE deve ser informado.");
			}
		}
		if (getIsApresentarPeriodo()) {
			if (!Uteis.isAtributoPreenchido(getFiltroRelatorioAcademicoVO().getDataInicio())) {
				throw new ConsistirException("O campo DATA INICIAL MATRÍCULA deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(getFiltroRelatorioAcademicoVO().getDataTermino())) {
				throw new ConsistirException("O campo DATA FINAL MATRÍCULA deve ser informado.");
			}
		}
	}

	public void montarDadosDoFiltroRelatorio() {
		getMapaNotaAlunoPorTurmaRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
		if (turmaVO.getCodigo() != 0) {
			getMapaNotaAlunoPorTurmaRelVO().setNomeUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
			getMapaNotaAlunoPorTurmaRelVO().setPeriodoLetivo(Uteis.isAtributoPreenchido(getTurmaVO().getPeridoLetivo().getNomeCertificacao()) ? getTurmaVO().getPeridoLetivo().getNomeCertificacao(): getTurmaVO().getPeridoLetivo().getDescricao());
		} else {
			getMapaNotaAlunoPorTurmaRelVO().setNomeUnidadeEnsino(getUnidadeEnsinosApresentar());
		}
		getMapaNotaAlunoPorTurmaRelVO().setCurso(getCursosApresentar());
		getMapaNotaAlunoPorTurmaRelVO().setTurno(getTurnosApresentar());
		/*
		 * if (getIsApresentarAno()) {
		 * getMapaNotaAlunoPorTurmaRelVO().setAno(Uteis.getAnoDataAtual4Digitos()); } if
		 * (getIsApresentarSemestre()) {
		 * getMapaNotaAlunoPorTurmaRelVO().setSemestre(Uteis.getSemestreAtual()); }
		 */
	}

	@SuppressWarnings("unchecked")
	public void imprimirPDF() throws Exception {
		List<MapaNotaAlunoPorTurmaRelVO> listaRegistro = new ArrayList<MapaNotaAlunoPorTurmaRelVO>(0);
		String titulo = null;
		String design = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "MapaNotaAlunoPorTurmaRelControle", "Inicializando Geração de Relátorio Mapa Nota Aluno Por Turma ", "Emitindo Relatório");
			validarTurmaInformada();
			montarDadosDoFiltroRelatorio();
			if (getTurmaVO().getCurso().getNivelEducacional().equals("PO") && tipoLayout.equals("MapaNotaAlunoPorTurmaPosGraduacaoRel")) {
				imprimirPDFTurmaPosGraduacao();
			} else {
				if (tipoLayout.equals("MapaNotaAlunoPorTurmaRel_layout3") || tipoLayout.equals("MapaNotaAlunoPorTurmaRel_layout4")) {
					listaRegistro = getFacadeFactory().getMapaNotaAlunoPorTurmaRelFacade().criarObjetoLayout3e4(
							getFiltroRelatorioAcademicoVO(), getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(),
							getTurmaVO(), getDisciplinaVO(), getMapaNotaAlunoPorTurmaRelVO().getAno(),
							getMapaNotaAlunoPorTurmaRelVO().getSemestre(), getFuncionarioVO(), getSalaLocalAula(),
							getTipoNota(), getTipoAluno(), getTipoCurso(), getTipoDisciplina(),
							tipoLayout.equals("MapaNotaAlunoPorTurmaRel_layout3") ? OrientacaoPaginaEnum.RETRATO : OrientacaoPaginaEnum.PAISAGEM,
							getUsuarioLogado());
					if (!listaRegistro.isEmpty()) {
						titulo = "Notas e Frequências por Turma";
						design = getDesignIReportRelatorio();
						setMensagemDetalhada("", "");
						getSuperParametroRelVO().setNomeDesignIreport(design);
						getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
						getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
						getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
						getSuperParametroRelVO().setTituloRelatorio(titulo);
						getSuperParametroRelVO().setFiltros("");
						getSuperParametroRelVO().setListaObjetos(listaRegistro);
						getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
						getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
						getSuperParametroRelVO().setQuantidade(listaRegistro.size());
						getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
						if (Uteis.isAtributoPreenchido(getTipoNota())) {
							String tituloNota = (String) UtilReflexao.invocarMetodoGet(getConfiguracaoAcademicoVO(),"tituloNotaApresentar" + getTipoNota().substring(4, getTipoNota().length()));
							getSuperParametroRelVO().adicionarParametro("tipoNota", tituloNota);
						} else {
							getSuperParametroRelVO().adicionarParametro("tipoNota", "Média Final");
						}
						if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO().getCodigo())) {
							getSuperParametroRelVO().adicionarParametro("funcionarioPrincipal", getFuncionarioPrincipalVO().getPessoa().getNome());
						} else {
							getSuperParametroRelVO().adicionarParametro("funcionarioPrincipal", "ASSINATURA DO(A) DIRETOR(A)");
						}
						if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getCodigo())) {
							getSuperParametroRelVO().adicionarParametro("funcionarioSecundario", getFuncionarioSecundarioVO().getPessoa().getNome());
						} else {
							getSuperParametroRelVO().adicionarParametro("funcionarioSecundario", "ASSINATURA DO(A) PROFESSOR(A)");
						}
						if (Uteis.isAtributoPreenchido(getCargoFuncionarioPrincipal().getCodigo())) {
							getSuperParametroRelVO().adicionarParametro("cargoFuncionarioPrincipal", getCargoFuncionarioPrincipal().getNome());
						} else {
							getSuperParametroRelVO().adicionarParametro("cargoFuncionarioPrincipal", "");
						}
						if (Uteis.isAtributoPreenchido(getCargoFuncionarioSecundario().getCodigo())) {
							getSuperParametroRelVO().adicionarParametro("cargoFuncionarioSecundario", getCargoFuncionarioSecundario().getNome());
						} else {
							getSuperParametroRelVO().adicionarParametro("cargoFuncionarioSecundario", "");
						}

						adicionarFiltroSituacaoAcademica(getSuperParametroRelVO(), getFiltroRelatorioAcademicoVO());
						
					
						montarDadosUrlLogoRelatorio();

						realizarImpressaoRelatorio();
						gravarDadosPadroesEmissaoRelatorio();
						setMensagemID("msg_relatorio_ok");
					} else {
						setMensagemDetalhada("Não existe nenhum registro para os filtros selecionados.");
					}
				} else {
					getMapaNotaAlunoPorTurmaRelVO().setMapaNotaAlunoPorTurmaAlunoRelVOs(
							getFacadeFactory().getMapaNotaAlunoPorTurmaRelFacade().montarListaAlunos(getTurmaVO(),
									getMapaNotaAlunoPorTurmaRelVO().getAno(),
									getMapaNotaAlunoPorTurmaRelVO().getSemestre(), getTipoNota(),
									getConfiguracaoAcademicoVO(), getFiltroRelatorioAcademicoVO(), getTipoDisciplina(),
									getTipoAluno(), getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(),
									getDisciplinaVO(), getSalaLocalAula(), getFuncionarioVO(), getTipoCurso(),
									getTipoLayout(), getUsuarioLogado(), getListaNotas(), getOrdenacao()));

					if(getMapaNotaAlunoPorTurmaRelVO().getMapaNotaAlunoPorTurmaAlunoRelVOs().isEmpty()) {						
						setMensagemDetalhada("Não existe nenhum registro para os filtros selecionados.");
						return;
					}
					//Responsável por gerar a legenda de disciplinas
					for (MapaNotaAlunoPorTurmaAlunoRelVO obj : getMapaNotaAlunoPorTurmaRelVO().getMapaNotaAlunoPorTurmaAlunoRelVOs()) {
						boolean adicionar = true;
						for (DisciplinaVO disc : getMapaNotaAlunoPorTurmaRelVO().getListaDisciplinas()) {
							if (obj.getCodigoDisciplina().equals(disc.getCodigo())) {
								adicionar = false;
								break;
							}
						}
						if (adicionar) {
							DisciplinaVO disciplinaVO = new DisciplinaVO();
							disciplinaVO.setCodigo(obj.getCodigoDisciplina());
							disciplinaVO.setNome(obj.getNomeDisciplina());
							disciplinaVO.setDataModulo(obj.getDataModulo());
							getMapaNotaAlunoPorTurmaRelVO().getListaDisciplinas().add(disciplinaVO);
						}
					}
					
					if (getTipoCurso().equals("AN")) {
						getMapaNotaAlunoPorTurmaRelVO().setSemestre("");
					} else {
						getSuperParametroRelVO().setSemestre(getMapaNotaAlunoPorTurmaRelVO().getSemestre());
					}

					if (getTipoLayout().equals("MapaNotasBoletimAlunosPorTurmaRel")) {						
						int totalAgrupadoPorNomeDisciplina = getMapaNotaAlunoPorTurmaRelVO().getMapaNotaAlunoPorTurmaAlunoRelVOs().stream().collect(Collectors.groupingBy(MapaNotaAlunoPorTurmaAlunoRelVO::getAgrupadorDisciplinaNota)).size();;
						int resto = (totalAgrupadoPorNomeDisciplina % 20);
						

						MapaNotaAlunoPorTurmaAlunoRelVO obj = null;
						while(resto < 20) {
							obj = new MapaNotaAlunoPorTurmaAlunoRelVO();
							obj.setOrdemColuna(1000 + resto);
							obj.setOrdemLinha(100000);
							obj.setOrdemColunaNota(100000);
							obj.setNomeDisciplina("--");
							obj.setCargaHoraria(0);
							obj.setCodigoDisciplina(0);
							obj.setTituloNota("--");
							obj.setNota("--");
							
							getMapaNotaAlunoPorTurmaRelVO().getMapaNotaAlunoPorTurmaAlunoRelVOs().add(obj);
							resto++;
						}
					}
					
					Ordenacao.ordenarLista(getMapaNotaAlunoPorTurmaRelVO().getListaDisciplinas(), "nome");
					
					listaRegistro.add(getMapaNotaAlunoPorTurmaRelVO());
					
					
					StringBuilder tipoNotaStringBuilder = new StringBuilder();

					if (!getMapaNotaAlunoPorTurmaRelVO().getMapaNotaAlunoPorTurmaAlunoRelVOs().isEmpty()) {
						titulo = montarTipoNota(tipoNotaStringBuilder);
						setTipoNota(getTipoNotaApresentar());
						design = getDesignIReportRelatorio();
						setMensagemDetalhada("", "");
						getSuperParametroRelVO().setNomeDesignIreport(design);
						getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
						getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
						getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
						getSuperParametroRelVO().setTituloRelatorio(titulo);
						getSuperParametroRelVO().setFiltros("");
						getSuperParametroRelVO().setListaObjetos(listaRegistro);
						getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
						getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
						getSuperParametroRelVO().setQuantidade(listaRegistro.size());
						getSuperParametroRelVO().adicionarParametro("tipoNota",getTipoNota().trim().isEmpty() ? "Média Final" : getTipoNota());
						adicionarFiltroSituacaoAcademica(getSuperParametroRelVO(), getFiltroRelatorioAcademicoVO());
						montarDadosUrlLogoRelatorio();
						realizarImpressaoRelatorio();
						gravarDadosPadroesEmissaoRelatorio();
						setMensagemID("msg_relatorio_ok");
					} else {
						setMensagemDetalhada("Não existe nenhum registro para os filtros selecionados.");
					}
				}

			}
			registrarAtividadeUsuario(getUsuarioLogado(), "MapaNotaAlunoPorTurmaRelControle",
					"Finalizando Geração de Relátorio Mapa Nota Aluno Por Turma ", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setTipoNota("");
			Uteis.liberarListaMemoria(listaRegistro);
			titulo = null;
			design = null;

		}
	}

	@SuppressWarnings("unchecked")
	private void montarDadosUrlLogoRelatorio() throws Exception {
		UnidadeEnsinoVO unidadeEnsinoVO = new UnidadeEnsinoVO();
		if (Uteis.isAtributoPreenchido(getTurmaVO())) {
			unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
					getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
		} else if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVOMarcadasParaSeremUtilizadas())) {
			for(UnidadeEnsinoVO obj : getUnidadeEnsinoVOMarcadasParaSeremUtilizadas()) {
				obj = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
						obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
				if (obj.getMatriz()) {
					unidadeEnsinoVO = obj;
					break;
				}
			}
			if (!Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
				unidadeEnsinoVO = getUnidadeEnsinoVOMarcadasParaSeremUtilizadas().get(0);				
			}
		}

		if (unidadeEnsinoVO.getExisteLogoRelatorio()) {
			String urlLogoUnidadeEnsinoRelatorio = unidadeEnsinoVO.getCaminhoBaseLogoRelatorio()
					.replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoRelatorio();
			String urlLogo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + "/" + urlLogoUnidadeEnsinoRelatorio;
			getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
		} else {
			getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
		}
	}

	private String montarTipoNota(StringBuilder tipoNotaStringBuilder) {
		String titulo;
		if (tipoLayout.equals("MapaNotasBoletimAlunosPorTurmaRel")) {
			titulo = "MAPA DE NOTAS";
			if (getMapaNotaAlunoPorTurmaRelVO().getMapaNotaAlunoPorTurmaAlunoRelVOs().size() > 0) {								
				getSuperParametroRelVO().setPeriodoLetivo(getMapaNotaAlunoPorTurmaRelVO().getMapaNotaAlunoPorTurmaAlunoRelVOs().get(0).getPeriodoLetivo());
			}
			
		} else {
			titulo = "Notas e Frequências por Turma";
			tipoNotaStringBuilder.append(getTipoNota());
		}
		return titulo;
	}

	@SuppressWarnings("unchecked")
	public void imprimirPDFTurmaPosGraduacao() {
		List<MapaNotaAlunoPorTurmaRelVO> listaObjetos = new ArrayList<MapaNotaAlunoPorTurmaRelVO>(0);
		try {
			listaObjetos = getFacadeFactory().getMapaNotaAlunoPorTurmaRelFacade()
					.executarConsultaMapaNotaTurmaPosGraduacao(getTurmaVO(), getMapaNotaAlunoPorTurmaRelVO().getAno(),
							getMapaNotaAlunoPorTurmaRelVO().getSemestre(), getTipoCurso(), getTipoNota(),
							getFiltroRelatorioAcademicoVO(), getTipoDisciplina(), getTipoAluno(), getOrdenacao(), getTipoLayout(), getDisciplinaVO(), getConfiguracaoAcademicaVO(), getFuncionarioVO(), getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getSalaLocalAula(), getListaNotas());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(
						MapaNotaAlunoPorTurmaRel.getDesignIReportRelatorioMapaNotaAlunoPorTurma());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO()
						.setSubReport_Dir(MapaNotaAlunoPorTurmaRel.getCaminhoBaseRelatorioMapaNotaAlunoPorTurma());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Mapa nota do aluno por Turma");
				getSuperParametroRelVO().getListaObjetos().clear();
				getSuperParametroRelVO().getListaObjetos().addAll(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(
						MapaNotaAlunoPorTurmaRel.getCaminhoBaseRelatorioMapaNotaAlunoPorTurma());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(1);
				getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().setCurso(getTurmaVO().getCurso().getNome());
				getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				getSuperParametroRelVO().setPeriodoLetivo(Uteis.isAtributoPreenchido(getTurmaVO().getPeridoLetivo().getNomeCertificacao()) ? getTurmaVO().getPeridoLetivo().getNomeCertificacao(): getTurmaVO().getPeridoLetivo().getDescricao());
				getSuperParametroRelVO().setDataInicio("");
				getSuperParametroRelVO().setDataFim("");				
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setOrdenadoPor(ordenacao);

				if (getTurmaVO().getCodigo() > 0) {
					getSuperParametroRelVO()
							.setTurma(getFacadeFactory().getTurmaFacade()
									.consultarPorChavePrimaria(getTurmaVO().getCodigo(),
											Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())
									.getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}
				getSuperParametroRelVO().setCurso(getTurmaVO().getCurso().getNome());
				getSuperParametroRelVO().setTurno(getTurmaVO().getTurno().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				getSuperParametroRelVO().adicionarParametro("crosstab", listaObjetos);
				realizarImpressaoRelatorio(getSuperParametroRelVO());
				// Uteis.removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemDetalhada("Não existe nenhum registro de notas para esta Turma.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void gravarDadosPadroesEmissaoRelatorio() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoCurso(),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "tipoCurso", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getMapaNotaAlunoPorTurmaRelVO().getAno(),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(
					getMapaNotaAlunoPorTurmaRelVO().getSemestre(), MapaNotaAlunoPorTurmaRel.class.getSimpleName(),
					"semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(
					Uteis.getData(getFiltroRelatorioAcademicoVO().getDataInicio()),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "dataInicio", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(
					Uteis.getData(getFiltroRelatorioAcademicoVO().getDataTermino()),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "dataTermino", getUsuarioLogado());			
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoAluno(),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "tipoAluno", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoDisciplina().toString(),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "tipoDisciplina", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoLayout(),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "tipoLayout", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(
					getFuncionarioPrincipalVO().getCodigo().toString(), MapaNotaAlunoPorTurmaRel.class.getSimpleName(),
					"assinaturaFuncionario1", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(
					getCargoFuncionarioPrincipal().getCodigo().toString(),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "cargoFuncionario1", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(
					getFuncionarioSecundarioVO().getCodigo().toString(), MapaNotaAlunoPorTurmaRel.class.getSimpleName(),
					"assinaturaFuncionario2", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(
					getCargoFuncionarioSecundario().getCodigo().toString(),
					MapaNotaAlunoPorTurmaRel.class.getSimpleName(), "cargoFuncionario2", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), MapaNotaAlunoPorTurmaRel.class.getSimpleName(), getUsuarioLogado());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void montarDadosPadroesEmissaoRelatorio() {
		try {
			Map<String, String> retorno = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(
					new String[] { "tipoCurso", "ano", "semestre", "dataInicio", "dataTermino", "situacao",
							"tipoLayout", "tipoAluno", "tipoDisciplina", "assinaturaFuncionario1",
							"assinaturaFuncionario2", "cargoFuncionario1", "cargoFuncionario2" },
					MapaNotaAlunoPorTurmaRel.class.getSimpleName());
			for (String key : retorno.keySet()) {
				if (key.equals("tipoCurso")) {
					setTipoCurso(retorno.get(key));
				} else if (key.equals("tipoAluno")) {
					setTipoAluno(retorno.get(key));
				} else if (key.equals("tipoDisciplina")) {
					setTipoDisciplina(retorno.get(key));
				} else if (key.equals("ano")) {
					getMapaNotaAlunoPorTurmaRelVO().setAno(retorno.get(key));
				} else if (key.equals("semestre")) {
					getMapaNotaAlunoPorTurmaRelVO().setSemestre(retorno.get(key));
				} else if (key.equals("assinaturaFuncionario1") && !retorno.get(key).equalsIgnoreCase("null")
						&& !retorno.get(key).equalsIgnoreCase("0") && !retorno.get(key).trim().isEmpty()) {
					getFuncionarioPrincipalVO().setCodigo(Integer.valueOf(retorno.get(key)));
					getFacadeFactory().getFuncionarioFacade().carregarDados(getFuncionarioPrincipalVO(),
							NivelMontarDados.BASICO, getUsuarioLogado());
					setSelectItemsCargoFuncionarioPrincipal(
							montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade()
									.consultarCargoPorCodigoFuncionario(getFuncionarioPrincipalVO().getCodigo(), false,
											Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					if (!getSelectItemsCargoFuncionarioPrincipal().isEmpty()) {
						setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
								(Integer) getSelectItemsCargoFuncionarioPrincipal().get(0).getValue(), false,
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					} else {
						getCargoFuncionarioPrincipal().setCodigo(0);
					}
				} else if (key.equals("cargoFuncionario1") && !retorno.get(key).equalsIgnoreCase("null")
						&& !retorno.get(key).equalsIgnoreCase("0") && !retorno.get(key).trim().isEmpty()) {
					getCargoFuncionarioPrincipal().setCodigo(Integer.valueOf(retorno.get(key)));
				} else if (key.equals("assinaturaFuncionario2") && !retorno.get(key).equalsIgnoreCase("null")
						&& !retorno.get(key).equalsIgnoreCase("0") && !retorno.get(key).trim().isEmpty()) {
					getFuncionarioSecundarioVO().setCodigo(Integer.valueOf(retorno.get(key)));
					getFacadeFactory().getFuncionarioFacade().carregarDados(getFuncionarioSecundarioVO(),
							NivelMontarDados.BASICO, getUsuarioLogado());
					setSelectItemsCargoFuncionarioSecundario(
							montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade()
									.consultarCargoPorCodigoFuncionario(getFuncionarioSecundarioVO().getCodigo(), false,
											Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					if (!getSelectItemsCargoFuncionarioSecundario().isEmpty()) {
						setCargoFuncionarioSecundario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
								(Integer) getSelectItemsCargoFuncionarioSecundario().get(0).getValue(), false,
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					} else {
						getCargoFuncionarioSecundario().setCodigo(0);
					}
				} else if (key.equals("cargoFuncionario2") && !retorno.get(key).equalsIgnoreCase("null")
						&& !retorno.get(key).equalsIgnoreCase("0") && !retorno.get(key).trim().isEmpty()) {
					getCargoFuncionarioSecundario().setCodigo(Integer.valueOf(retorno.get(key)));
				} else if (key.equals("tipoLayout")) {
					setTipoLayout(retorno.get(key));
				} else if (key.equals("dataInicio") && retorno.get(key) != null) {
					if( retorno.get(key).equalsIgnoreCase("null") || retorno.get(key).trim().isEmpty()) {
						getFiltroRelatorioAcademicoVO().setDataInicio(null);
					}else {
						getFiltroRelatorioAcademicoVO().setDataInicio(Uteis.getData(retorno.get(key), "dd/MM/YYYY"));
					}
				} else if (key.equals("dataTermino") && retorno.get(key) != null) {
						if(retorno.get(key).equalsIgnoreCase("null") || retorno.get(key).trim().isEmpty()) {
							getFiltroRelatorioAcademicoVO().setDataTermino(null);
						}else {
							getFiltroRelatorioAcademicoVO().setDataTermino(Uteis.getData(retorno.get(key), "dd/MM/YYYY"));
						}
				} 
			}
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(
					getFiltroRelatorioAcademicoVO(), MapaNotaAlunoPorTurmaRel.class.getSimpleName(),
					getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(),
						getUnidadeEnsinoLogado().getCodigo(), false, false, "", false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(),
						getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(),
						getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(),
						getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			obj.setTurmaSelecionada(true);
			setTurmaVO(obj);

			limparCursos();
			limparTurnos();
			limparUnidadeEnsinos();

			setTurnosApresentar(obj.getTurno().getNome());
			setCursosApresentar(obj.getCurso().getNome());
			setTipoCurso(obj.getCurso().getPeriodicidade());
			setUnidadeEnsinosApresentar(obj.getUnidadeEnsino().getNome());

			consultarTurmaPorChavePrimaria();
			montarConfiguracaoAcademico();
			montarListaSelectItemConfiguracaoAcademico();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarConfiguracaoAcademico() {
		try {
			if (Uteis.isAtributoPreenchido(getTurmaVO()) && getTurmaVO().getTurmaAgrupada() ) {
				if (!getTurmaVO().getTurmaAgrupadaVOs().isEmpty()) {
					
					setConfiguracaoAcademicoVO(getFacadeFactory().getCursoFacade()
							.consultarPorChavePrimaria(
									getTurmaVO().getTurmaAgrupadaVOs().get(0).getTurma().getCurso().getCodigo(),
									Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado())
							.getConfiguracaoAcademico());
				}
			} else if(Uteis.isAtributoPreenchido(getTurmaVO()) && Uteis.isAtributoPreenchido(getTurmaVO().getCurso())) {
				getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(
						getTurmaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false,
						getUsuarioLogado()));
				setConfiguracaoAcademicoVO(getTurmaVO().getCurso().getConfiguracaoAcademico());
			}
			montarListaOpcoesNotas();
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaOpcoesNotas() {
		try {
			setTipoNotaApresentar("");
			getListaNotas().clear();
			getListaSelectItemTipoInformarNota().clear();
			setTipoNota("");
			if (Uteis.isAtributoPreenchido(getConfiguracaoAcademicoVO())) {
				setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade()
						.consultarPorChavePrimaria(this.configuracaoAcademicoVO.getCodigo(), getUsuarioLogado()));
				montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
				montarListaOpcoesNotasLayout5(getConfiguracaoAcademicoVO());
			} 
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaOpcoesNotas(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("mediaFinal", ""));
		if (!Uteis.isAtributoPreenchido(configuracaoAcademicoVO)) {
			setListaSelectItemTipoInformarNota(lista);
			return;
		}
		for (int i = 1; i <= 30; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + i)) {
				lista.add(new SelectItem("nota" + i,
						(String) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "tituloNotaApresentar" + i)));
			}
		}
		setListaSelectItemTipoInformarNota(lista);
	}

	public void montarListaOpcoesNotasLayout5(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		listaConfiguracaoAcademicaNotaVOs.clear();
		for (int i = 1; i <= 40; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + i)) {
				ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicaNota"+i+"VO");
				configuracaoAcademicaNotaVO.setNotaTransiente("nota"+i);
				listaConfiguracaoAcademicaNotaVOs.add(configuracaoAcademicaNotaVO);
			}		
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Turma</code>
	 * por meio de sua respectiva chave primária. Esta rotina é utilizada
	 * fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para
	 * apresentação.
	 */
	public void consultarTurmaPorChavePrimaria() {
		try {
			String campoConsulta = getTurmaVO().getIdentificadorTurma();
			if (campoConsulta.equalsIgnoreCase("")) {
				throw new Exception();
			} else {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(),
						campoConsulta, super.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), getUsuarioLogado());
				getMapaNotaAlunoPorTurmaRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				getMapaNotaAlunoPorTurmaRelVO().setNomeUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				if (getTurmaVO().getSubturma()) {
					getTurmaVO().setCurso(
							getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(),
									false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				}
				montarConfiguracaoAcademico();
				montarListaSelectItemConfiguracaoAcademico();
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosTurma() throws Exception {
		removerObjetoMemoria(getTurmaVO());
		getMapaNotaAlunoPorTurmaRelVO().setAno("");
		getMapaNotaAlunoPorTurmaRelVO().setSemestre("");
		getMapaNotaAlunoPorTurmaRelVO().setTurma("");
		getMapaNotaAlunoPorTurmaRelVO().setNomeUnidadeEnsino("");
		setTipoNota("");
		limparUnidadeEnsinos();
		limparCursos();
		limparTurno();
		montarListaSelectItemConfiguracaoAcademico();
		setMensagemID("msg_entre_prmconsulta");
	}
	
	public void limparTipoNota() {
		setTipoNota("");
		getListaNotas().clear();
	}

	public void limparDadosSemestre() {
		if (getTipoCurso().equals("AN")) {
			setSemestre("");
			getMapaNotaAlunoPorTurmaRelVO().setSemestre("");
		}
	}

	public void limparConsultaTurma() {
		setListaConsultaTurma(null);
	}

	public List<SelectItem> getListaSelectSemestre() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
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

	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
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

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setMapaNotaAlunoPorTurmaRelVO(MapaNotaAlunoPorTurmaRelVO mapaNotaAlunoPorTurmaRelVO) {
		this.mapaNotaAlunoPorTurmaRelVO = mapaNotaAlunoPorTurmaRelVO;
	}

	public MapaNotaAlunoPorTurmaRelVO getMapaNotaAlunoPorTurmaRelVO() {
		if (mapaNotaAlunoPorTurmaRelVO == null) {
			mapaNotaAlunoPorTurmaRelVO = new MapaNotaAlunoPorTurmaRelVO();
		}
		return mapaNotaAlunoPorTurmaRelVO;
	}

	public String getDesignIReportRelatorio() {
		return (getCaminhoBaseRelatorio() + getTipoLayout() + ".jrxml");
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public String getIdEntidade() {
		return "MapaNotaAlunoPorTurmaRel";
	}

	public List<SelectItem> getListaSelectItemTipoInformarNota() {
		if (listaSelectItemTipoInformarNota == null) {
			listaSelectItemTipoInformarNota = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoInformarNota;
	}

	public void setListaSelectItemTipoInformarNota(List<SelectItem> listaSelectItemTipoInformarNota) {
		this.listaSelectItemTipoInformarNota = listaSelectItemTipoInformarNota;
	}

	public String getTipoNota() {
		if (tipoNota == null) {
			tipoNota = "";
		}
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public boolean getIsApresentarCampoAno() {
		return getTurmaVO().getCurso().getPeriodicidade().equals("AN")
				|| getTurmaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public boolean getIsApresentarCampoSemestre() {
		return getTurmaVO().getCurso().getPeriodicidade().equals("SE");
	}

	/**
	 * @return the filtroRelatorioAcademicoVO
	 */
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	/**
	 * @param filtroRelatorioAcademicoVO
	 *            the filtroRelatorioAcademicoVO to set
	 */
	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

	public List<SelectItem> getListaSelectItemTipoDisciplina() {
		if (listaSelectItemTipoDisciplina == null) {
			listaSelectItemTipoDisciplina = new ArrayList<SelectItem>(0);
			listaSelectItemTipoDisciplina.add(new SelectItem("ambas", "Ambas"));
			listaSelectItemTipoDisciplina.add(new SelectItem("composta", "Composta"));
			listaSelectItemTipoDisciplina.add(new SelectItem("composicao", "Composição"));
		}
		return listaSelectItemTipoDisciplina;
	}

	public String getTipoDisciplina() {
		if (tipoDisciplina == null) {
			tipoDisciplina = "";
		}
		return tipoDisciplina;
	}

	public void setTipoDisciplina(String tipoDisciplina) {
		this.tipoDisciplina = tipoDisciplina;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);
			try {
				montarListaSelectItemConfiguracaoAcademico();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaSelectItemConfiguracaoAcademico;
	}

	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	public void montarListaSelectItemConfiguracaoAcademico() throws Exception {
	
		List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = null;
		if(Uteis.isAtributoPreenchido(getDisciplinaVO())) {
			configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorDisciplinaPorTurma(getDisciplinaVO().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,getUsuarioLogado());
		}else if(Uteis.isAtributoPreenchido(getTurmaVO())) {
			configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,getUsuarioLogado());
		}else {
			configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarTodasConfiguracaoAcademica(Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
		}
		getListaSelectItemConfiguracaoAcademico().clear();
		getListaSelectItemConfiguracaoAcademico().add(new SelectItem(0, ""));
		boolean contemConf = false;
		for (ConfiguracaoAcademicoVO configuracaoAcademicoVO : configuracaoAcademicoVOs) {
			getListaSelectItemConfiguracaoAcademico().add(new SelectItem(configuracaoAcademicoVO.getCodigo(), configuracaoAcademicoVO.getCodigo() + " - " + configuracaoAcademicoVO.getNome()));
			if(configuracaoAcademicoVO.getCodigo().equals(getConfiguracaoAcademicoVO().getCodigo())) {
				contemConf =  true;
			}
		}		
		if(!contemConf) {
			setConfiguracaoAcademicoVO(null);
			setTipoNotaApresentar("");
			getListaNotas().clear();
			getListaSelectItemTipoInformarNota().clear();
			setTipoNota("");	
		}
	}

	public List<SelectItem> getListaSelectItemTipoAluno() {
		if (listaSelectItemTipoAluno == null) {
			listaSelectItemTipoAluno = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAluno.add(new SelectItem("todos", "Todos"));
			listaSelectItemTipoAluno.add(new SelectItem("normal", "Alunos (Turma Origem)"));
			listaSelectItemTipoAluno.add(new SelectItem("reposicao", "Alunos (Reposição/ Inclusão)"));
		}
		return listaSelectItemTipoAluno;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "normal";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadeEnsinoSelecionados();
			getTipoCurso();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getTipoCurso() {
		if (tipoCurso == null) {
			tipoCurso = "SE";
		}
		return tipoCurso;
	}

	public void setTipoCurso(String tipoCurso) {
		this.tipoCurso = tipoCurso;
	}

	public boolean getIsApresentarAno() {
		if (getTipoCurso().equals("AN") || getTipoCurso().equals("SE")) {
			return true;
		}

		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (getTipoCurso().equals("SE")) {
			return true;
		}
		setSemestre("");
		return false;
	}

	public boolean getIsApresentarPeriodo() {
		if (getTipoCurso().equals("IN")) {

			return true;
		}

		return false;
	}

	public Date getDataAtual() {
		if (dataAtual == null) {
			dataAtual = new Date();
		}
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
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

	public void incializarDados() {
		getFiltroRelatorioAcademicoVO().setDataInicio(null);
		getFiltroRelatorioAcademicoVO().setDataTermino(null);
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				if (getValorConsultaDisciplina().trim() != null || !getValorConsultaDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplina().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());

				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoCursoTurma(valorInt,
						getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(),
						getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {

				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurma(getValorConsultaDisciplina(),
						getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(),
						getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
		setDisciplinaVO(obj);
		getMapaNotaAlunoPorTurmaRelVO().setDisciplina(obj.getNome());		
		obj = null;
		setValorConsultaDisciplina("");
		setCampoConsultaDisciplina("");
		getListaConsultaDisciplina().clear();
		montarListaSelectItemConfiguracaoAcademico();
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public void limparDisciplina() throws Exception {
		try {
			getMapaNotaAlunoPorTurmaRelVO().setDisciplina("");
			setDisciplinaVO(null);
			montarListaSelectItemConfiguracaoAcademico();
		} catch (Exception e) {
		}
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

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public SalaLocalAulaVO getSalaLocalAula() {
		if (salaLocalAula == null) {
			salaLocalAula = new SalaLocalAulaVO();
		}
		return salaLocalAula;
	}

	public void setSalaLocalAula(SalaLocalAulaVO salaLocalAula) {
		this.salaLocalAula = salaLocalAula;
	}

	public void limparConsultaSalaLocalAula() {
		setListaConsultaSalaLocalAula(null);
		setMensagemID("msg_entre_prmconsulta");
	}

	public void consultarSalaLocalAula() {
		try {
			List<SalaLocalAulaVO> objs = new ArrayList<SalaLocalAulaVO>(0);
			if (getCampoConsultaSalaLocalAula().equals("sala")) {
				objs = getFacadeFactory().getSalaLocalAulaFacade().consultarPorSala(getValorConsultaSalaLocalAula(),
						getLocalAula(), getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoVOs(),
						StatusAtivoInativoEnum.ATIVO);
			}
			setListaConsultaSalaLocalAula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaSalaLocalAula(new ArrayList<SalaLocalAulaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarSalaLocalAula() throws Exception {
		SalaLocalAulaVO obj = (SalaLocalAulaVO) context().getExternalContext().getRequestMap().get("sala");
		setSalaLocalAula(obj);
		getMapaNotaAlunoPorTurmaRelVO().setSala(obj.getSala());
		setValorConsultaSalaLocalAula("");
		setCampoConsultaSalaLocalAula("");
		getListaConsultaSalaLocalAula().clear();
	}

	public List<SalaLocalAulaVO> getListaConsultaSalaLocalAula() {
		if (listaConsultaSalaLocalAula == null) {
			listaConsultaSalaLocalAula = new ArrayList<SalaLocalAulaVO>(0);
		}
		return listaConsultaSalaLocalAula;
	}

	public void setListaConsultaSalaLocalAula(List<SalaLocalAulaVO> listaConsultaSalaLocalAula) {
		this.listaConsultaSalaLocalAula = listaConsultaSalaLocalAula;
	}

	public String getCampoConsultaSalaLocalAula() {
		if (campoConsultaSalaLocalAula == null) {
			campoConsultaSalaLocalAula = "";
		}
		return campoConsultaSalaLocalAula;
	}

	public void setCampoConsultaSalaLocalAula(String campoConsultaSalaLocalAula) {
		this.campoConsultaSalaLocalAula = campoConsultaSalaLocalAula;
	}

	public String getValorConsultaSalaLocalAula() {
		if (valorConsultaSalaLocalAula == null) {
			valorConsultaSalaLocalAula = "";
		}
		return valorConsultaSalaLocalAula;
	}

	public void setValorConsultaSalaLocalAula(String valorConsultaSalaLocalAula) {
		this.valorConsultaSalaLocalAula = valorConsultaSalaLocalAula;
	}

	public LocalAulaVO getLocalAula() {
		if (localAula == null) {
			localAula = new LocalAulaVO();
		}
		return localAula;
	}

	public void setLocalAula(LocalAulaVO localAula) {
		this.localAula = localAula;
	}

	public void limparLocalAula() throws Exception {
		try {
			getMapaNotaAlunoPorTurmaRelVO().setSala("");
			setSalaLocalAula(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void consultarLocalAula() {
		try {
			List<LocalAulaVO> objs = new ArrayList<LocalAulaVO>(0);
			if (getCampoConsultaLocalAula().equals("local")) {
				objs = getFacadeFactory().getLocalAulaFacade().consultarPorLocal(getValorConsultaLocalAula(),
						getUnidadeEnsinoVO().getCodigo(), StatusAtivoInativoEnum.ATIVO, Uteis.NIVELMONTARDADOS_COMBOBOX,
						getUsuarioLogado());
			}
			setListaConsultaLocalAula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaLocalAula(new ArrayList<LocalAulaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarLocalAula() throws Exception {
		LocalAulaVO obj = (LocalAulaVO) context().getExternalContext().getRequestMap().get("local");
		setLocalAula(obj);
		setValorConsultaLocalAula("");
		setCampoConsultaLocalAula("");
		getListaConsultaLocalAula().clear();
	}

	public String getCampoConsultaLocalAula() {
		if (campoConsultaLocalAula == null) {
			campoConsultaLocalAula = "";
		}
		return campoConsultaLocalAula;
	}

	public void setCampoConsultaLocalAula(String campoConsultaLocalAula) {
		this.campoConsultaLocalAula = campoConsultaLocalAula;
	}

	public String getValorConsultaLocalAula() {
		if (valorConsultaLocalAula == null) {
			valorConsultaLocalAula = "";
		}
		return valorConsultaLocalAula;
	}

	public void setValorConsultaLocalAula(String valorConsultaLocalAula) {
		this.valorConsultaLocalAula = valorConsultaLocalAula;
	}

	public List<LocalAulaVO> getListaConsultaLocalAula() {
		if (listaConsultaLocalAula == null) {
			listaConsultaLocalAula = new ArrayList<LocalAulaVO>(0);
		}
		return listaConsultaLocalAula;
	}

	public void setListaConsultaLocalAula(List<LocalAulaVO> listaConsultaLocalAula) {
		this.listaConsultaLocalAula = listaConsultaLocalAula;
	}

	public List<SelectItem> getTipoConsultaComboSalaLocalAula() {
		if (tipoConsultaComboSalaLocalAula == null) {
			tipoConsultaComboSalaLocalAula = new ArrayList<SelectItem>();
			tipoConsultaComboSalaLocalAula.add(new SelectItem("sala", "Sala"));
		}
		return tipoConsultaComboSalaLocalAula;
	}

	public List<SelectItem> getTipoConsultaComboLocalAula() {
		if (tipoConsultaComboLocalAula == null) {
			tipoConsultaComboLocalAula = new ArrayList<SelectItem>();
			tipoConsultaComboLocalAula.add(new SelectItem("local", "Local"));
		}
		return tipoConsultaComboLocalAula;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List<FuncionarioVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public void limparConsultaProfessor() {
		setListaConsultaProfessor(null);
		setMensagemID("msg_entre_prmconsulta");
	}

	public List<SelectItem> getTipoConsultaComboProfessor() {
		if (tipoConsultaComboProfessor == null) {
			tipoConsultaComboProfessor = new ArrayList<SelectItem>();
			tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboProfessor.add(new SelectItem("matricula", "Matrícula"));
		}
		return tipoConsultaComboProfessor;
	}

	public void consultarProfessor() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(),
						"PR", null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaProfessor(),
						null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
		getFacadeFactory().getFuncionarioFacade().carregarDados(obj, getUsuarioLogado());
		setFuncionarioVO(obj);
		getMapaNotaAlunoPorTurmaRelVO().setProfessor(obj.getPessoa().getNome());
		setValorConsultaProfessor("");
		setCampoConsultaProfessor("");
		getListaConsultaProfessor().clear();
	}

	public void limparProfessor() throws Exception {
		try {
			setFuncionarioVO(null);
			getMapaNotaAlunoPorTurmaRelVO().setProfessor("");
		} catch (Exception e) {

		}
	}

	public void limparCursos() {
		try {
			super.limparCursos();
			getMapaNotaAlunoPorTurmaRelVO().setCurso("");
			setCursosApresentar(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurno() {
		try {
			super.limparTurnos();
			getMapaNotaAlunoPorTurmaRelVO().setTurno("");
			setTurnosApresentar(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparSalaLocalAula() {
		setSalaLocalAula(new SalaLocalAulaVO());
	}

	public List<SelectItem> getListaSelectItemTipoLayout() {
		if (listaSelectItemTipoLayout == null) {
			listaSelectItemTipoLayout = new ArrayList<SelectItem>(0);
			listaSelectItemTipoLayout.add(new SelectItem("MapaNotaAlunoPorTurmaRel", "Layout 1 - Alunos Por Turma"));
			listaSelectItemTipoLayout.add(new SelectItem("MapaNotaAlunoPorTurmaRel_unidadeTurmaDiscSala",
					"Layout 2 - Alunos Por Unidade Ensino, Turma, Disciplina e Sala"));
			listaSelectItemTipoLayout.add(new SelectItem("MapaNotaAlunoPorTurmaRel_layout3",
					"Layout 3 - Alunos Por Unidade Ensino, Turma, Disciplina, Professor e Sala (Retrato)"));
			listaSelectItemTipoLayout.add(new SelectItem("MapaNotaAlunoPorTurmaRel_layout4",
					"Layout 4 - Alunos Por Unidade Ensino, Turma, Disciplina, Professor e Sala (Paisagem)"));
			listaSelectItemTipoLayout.add(
					new SelectItem("MapaNotasBoletimAlunosPorTurmaRel", "Layout 5 - Notas Boletim Alunos Por Turma"));

		}
		return listaSelectItemTipoLayout;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public void consultarCursoFiltroRelatorio() {
		// if(getCursoVOs().isEmpty() ||
		// !getCursoVOs().get(0).getPeriodicidade().equals(getTipoCurso())){
		consultarCursoFiltroRelatorio(getTipoCurso());
		// }
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

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<>(0);
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
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionarioPrincipal() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioPrincipal");
		setFuncionarioPrincipalVO(obj);
		consultarFuncionarioPrincipal();
	}

	public void selecionarFuncionarioSecundario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioSecundario");
		setFuncionarioSecundarioVO(obj);
		consultarFuncionarioSecundario();
	}

	public void consultarFuncionarioPrincipal() throws Exception {
		try {
			setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(getFuncionarioPrincipalVO().getMatricula()));
			setSelectItemsCargoFuncionarioPrincipal(
					montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade()
							.consultarCargoPorCodigoFuncionario(getFuncionarioPrincipalVO().getCodigo(), false,
									Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
			if (!getSelectItemsCargoFuncionarioPrincipal().isEmpty()) {
				setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
						(Integer) getSelectItemsCargoFuncionarioPrincipal().get(0).getValue(), false,
						Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				getCargoFuncionarioPrincipal().setCodigo(0);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioSecundario() throws Exception {
		try {
			setFuncionarioSecundarioVO(consultarFuncionarioPorMatricula(getFuncionarioSecundarioVO().getMatricula()));
			setSelectItemsCargoFuncionarioSecundario(
					montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade()
							.consultarCargoPorCodigoFuncionario(getFuncionarioSecundarioVO().getCodigo(), false,
									Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
			if (!getSelectItemsCargoFuncionarioSecundario().isEmpty()) {
				setCargoFuncionarioSecundario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
						(Integer) getSelectItemsCargoFuncionarioSecundario().get(0).getValue(), false,
						Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				getCargoFuncionarioSecundario().setCodigo(0);
			}
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

	public void limparDadosFuncionarioPrincipal() {
		removerObjetoMemoria(getFuncionarioPrincipalVO());
		setFuncionarioPrincipalVO(new FuncionarioVO());
		setCargoFuncionarioPrincipal(null);
	}

	public void limparDadosFuncionarioSecundario() {
		removerObjetoMemoria(getFuncionarioSecundarioVO());
		setFuncionarioSecundarioVO(new FuncionarioVO());
		setCargoFuncionarioSecundario(null);
	}

	public FuncionarioVO getFuncionarioPrincipalVO() {
		if (funcionarioPrincipalVO == null) {
			funcionarioPrincipalVO = new FuncionarioVO();
		}
		return funcionarioPrincipalVO;
	}

	public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
		this.funcionarioPrincipalVO = funcionarioPrincipalVO;
	}

	public FuncionarioVO getFuncionarioSecundarioVO() {
		if (funcionarioSecundarioVO == null) {
			funcionarioSecundarioVO = new FuncionarioVO();
		}
		return funcionarioSecundarioVO;
	}

	public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
		this.funcionarioSecundarioVO = funcionarioSecundarioVO;
	}

	public CargoVO getCargoFuncionarioPrincipal() {
		if (cargoFuncionarioPrincipal == null) {
			cargoFuncionarioPrincipal = new CargoVO();
		}
		return cargoFuncionarioPrincipal;
	}

	public void setCargoFuncionarioPrincipal(CargoVO cargoFuncionarioPrincipal) {
		this.cargoFuncionarioPrincipal = cargoFuncionarioPrincipal;
	}

	public CargoVO getCargoFuncionarioSecundario() {
		if (cargoFuncionarioSecundario == null) {
			cargoFuncionarioSecundario = new CargoVO();
		}
		return cargoFuncionarioSecundario;
	}

	public void setCargoFuncionarioSecundario(CargoVO cargoFuncionarioSecundario) {
		this.cargoFuncionarioSecundario = cargoFuncionarioSecundario;
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

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}

	public String getTurnoApresentar() {
		if (turnoApresentar == null) {
			turnoApresentar = "";
		}
		return turnoApresentar;
	}

	public void setTurnoApresentar(String turnoApresentar) {
		this.turnoApresentar = turnoApresentar;
	}

	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "disciplina";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public List<SelectItem> getListaSelectTipoOrdenacao() {
		if (listaSelectTipoOrdenacao == null) {
			listaSelectTipoOrdenacao = new ArrayList<SelectItem>(0);
			listaSelectTipoOrdenacao.add(new SelectItem("disciplina", "Disciplina"));
			listaSelectTipoOrdenacao.add(new SelectItem("data_Inicio_Aula", "Data Inicio Aula"));

		}
		return listaSelectTipoOrdenacao;
	}

	public void setListaSelectTipoOrdenacao(List<SelectItem> listaSelectTipoOrdenacao) {
		this.listaSelectTipoOrdenacao = listaSelectTipoOrdenacao;
	}

	public List<String> getListaNotas() {
		if (listaNotas == null) {
			listaNotas = new ArrayList<>();
		}
		return listaNotas;
	}

	public void setListaNotas(List<String> listaNotas) {
		this.listaNotas = listaNotas;
	}

	public Boolean getMarcarTodosTiposNotas() {
		if (marcarTodosTiposNotas == null)  {
			marcarTodosTiposNotas = Boolean.FALSE;
		}
		return marcarTodosTiposNotas;
	}

	public void setMarcarTodosTiposNotas(Boolean marcarTodosTiposNotas) {
		this.marcarTodosTiposNotas = marcarTodosTiposNotas;
	}

	public List<ConfiguracaoAcademicaNotaVO> getListaConfiguracaoAcademicaNotaVOs() {
		if (listaConfiguracaoAcademicaNotaVOs == null) {
			listaConfiguracaoAcademicaNotaVOs = new ArrayList<>();
		}
		return listaConfiguracaoAcademicaNotaVOs;
	}

	public void setListaConfiguracaoAcademicaNotaVOs(List<ConfiguracaoAcademicaNotaVO> listaConfiguracaoAcademicaNotaVOs) {
		this.listaConfiguracaoAcademicaNotaVOs = listaConfiguracaoAcademicaNotaVOs;
	}

	public String getTipoNotaApresentar() {
		if (tipoNotaApresentar == null) {
			tipoNotaApresentar = "";
		}
		return tipoNotaApresentar;
	}

	public void setTipoNotaApresentar(String tipoNotaApresentar) {
		this.tipoNotaApresentar = tipoNotaApresentar;
	}
}