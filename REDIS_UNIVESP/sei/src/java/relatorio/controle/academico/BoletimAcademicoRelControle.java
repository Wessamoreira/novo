package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.ExpedicaoDiplomaControle;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.BoletimAcademicoRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.BoletimAcademicoRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@SuppressWarnings("unchecked")
@Controller("BoletimAcademicoRelControle")
@Scope("viewScope")
@Lazy
public class BoletimAcademicoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private MatriculaVO matriculaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private TurmaVO turmaVO;
	private FuncionarioVO funcionarioPrincipalVO;
	private FuncionarioVO funcionarioSecundarioVO;
	private CargoVO cargoFuncionarioPrincipal;
	private CargoVO cargoFuncionarioSecundario;
	private List<MatriculaVO> listaConsultaAluno;
	private List<TurmaVO> listaConsultaTurma;
	private List<MatriculaVO> listaMatriculas;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> selectItemsCargoFuncionarioPrincipal;
	private List<SelectItem> selectItemsCargoFuncionarioSecundario;
	private List<SelectItem> listaSelectItemAnoSemestre;
	private List<SelectItem> listaSelectItemBimestre;
	private List<SelectItem> listaSelectItemSituacaoRecuperacao;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private String campoFiltroPor;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String situacaoMatricula;
	private String ano;
	private String semestre;
	private String anoSemestre;
	private String tipoLayout;
	private String dataPorExtenso;
	private Boolean apresentarDisciplinaComposta;
	private Boolean apresentarCampoAssinatura;
	private Boolean apresentarQuantidadeFaltasAluno;
	private Boolean gerarUmAlunoPorPagina;
	private Boolean apresentarApenasNotaTipoMedia;
	private Boolean apresentarAprovadoPorAproveitamentoComoAprovado;
	private BimestreEnum bimestre;
	private SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota;
	private Boolean apresentarCampoAssinaturaResponsavel;
	private Boolean apresentarAdvertenciaAluno;
	private Boolean apresentarReprovadoPorFaltaComoReprovado;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private GradeCurricularVO gradeCurricularVO;
	private Boolean apresentarCargaHorariaCursada;
	private Boolean filtrarPorRegistroAcademico;
	
	private ConfiguracaoAcademicoVO configuracaoAcademico;
	
	private Boolean marcarTodosTiposNotas;
	private List<String> listaNotas;
	private String tipoNotaApresentar;
	
	private List<ConfiguracaoAcademicaNotaVO> listaConfiguracaoAcademicaNotaVOs;
	private Boolean trazerAlunoTransferencia;
	private String observacao;
	private boolean assinarDigitalmente = false;
	
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
	
	public void montarConfiguracaoAcademico() {
		try {
			if (Uteis.isAtributoPreenchido(getMatriculaVO().getCurso())) {
				if(!Uteis.isAtributoPreenchido(getMatriculaVO().getCurso().getConfiguracaoAcademico())) {
					CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
					getMatriculaVO().getCurso().setConfiguracaoAcademico(curso.getConfiguracaoAcademico());
					setConfiguracaoAcademico(curso.getConfiguracaoAcademico());
				}else {
					setConfiguracaoAcademico(getMatriculaVO().getCurso().getConfiguracaoAcademico());
				}
				
			}
			//montarListaNotasLayout(getConfiguracaoAcademico());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaNotasLayout(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		listaConfiguracaoAcademicaNotaVOs.clear();
		for (int i = 1; i <= 40; i++) {
			if ((Boolean) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota" + i)) {
				ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = (ConfiguracaoAcademicaNotaVO) UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicaNota"+i+"VO");
				configuracaoAcademicaNotaVO.setNotaTransiente("nota"+i);
				listaConfiguracaoAcademicaNotaVOs.add(configuracaoAcademicaNotaVO);
			}
		}
	}

	public BoletimAcademicoRelControle() throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), AtaResultadosFinaisRelControle.class.getSimpleName(), getUsuarioLogado());
		Map<String, String> mapResultado = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] {"matricula_aluno_observacao", "tipoLayout"}, BoletimAcademicoRelControle.class.getSimpleName());
		if(mapResultado.containsKey("matricula_aluno_observacao")) {
			setObservacao(mapResultado.get("matricula_aluno_observacao"));
		}

		if(mapResultado.containsKey("tipoLayout")) {
			setTipoLayout(mapResultado.get("tipoLayout"));
		}

		setMensagemID("msg_entre_prmrelatorio");
	}

	@PostConstruct
	public void realizarCarregamentoBoletimAcademicoVindoTelaFichaAluno() {
		MatriculaPeriodoVO matriculaPeriodoFichaAlunoVO = (MatriculaPeriodoVO) context().getExternalContext()
				.getSessionMap().get("matriculaPeriodoFichaAluno");
		if (matriculaPeriodoFichaAlunoVO != null && !matriculaPeriodoFichaAlunoVO.getCodigo().equals(0)) {
			getMatriculaVO().setMatricula(matriculaPeriodoFichaAlunoVO.getMatricula());
			try {
				consultarAlunoPorMatricula();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaPeriodoFichaAluno");
			}
		}
	}
	
	public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
		Integer codigoUnidadeEnsino = 0;
		if(Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo())) {
			codigoUnidadeEnsino = getUnidadeEnsinoVO().getCodigo();
		}else if(Uteis.isAtributoPreenchido(getMatriculaVO().getUnidadeEnsino().getCodigo())) {
			codigoUnidadeEnsino = getMatriculaVO().getUnidadeEnsino().getCodigo();
		}
		if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino) || !isAssinarDigitalmente()){
			setProvedorDeAssinaturaEnum(null);
			return new ArrayList<SelectItem>();
		}
		return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO);
	}

	public void imprimirPDF() {
		String titulo = "";
		if (getTipoLayout().equals("FichaAluno2Rel") || getTipoLayout().equals("FichaIndividual") || getTipoLayout().equals("FichaIndividualRel") || getTipoLayout().equals("FichaIndividualLayout2Rel") || getTipoLayout().equals("FichaIndividualLayout2RetratoRel")) {
			titulo = "FICHA INDIVIDUAL";
		}
		else if (getMatriculaVO().getCurso().getNivelEducacional().equals("BA") || getMatriculaVO().getCurso().getNivelEducacional().equals("ME") || getMatriculaVO().getCurso().getNivelEducacional().equals("IN") || getTurmaVO().getCurso().getNivelEducacional().equals("BA") || getTurmaVO().getCurso().getNivelEducacional().equals("ME") || getTurmaVO().getCurso().getNivelEducacional().equals("IN") || getTipoLayout().equals("BoletimAcademicoEnsinoMedio2Rel")) {
			titulo = "BOLETIM ESCOLAR";
		} else {
			titulo = "BOLETIM ACADÊMICO";
		}
		String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();

		if (getGerarUmAlunoPorPagina() == false && getApresentarCampoAssinaturaResponsavel() == false && getApresentarCampoAssinatura() == false) {
			if (getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel")) {
				setTipoLayout("BoletimAcademicoEnsinoMedioRel2");
			}
			if (getTipoLayout().equals("FichaAluno2Rel") && getApresentarCampoAssinaturaResponsavel() == false && getApresentarCampoAssinatura() == false) {
				setTipoLayout("FichaAluno2Rel2Alunos");
			}
		}

		String design = BoletimAcademicoRel.getDesignIReportRelatorio(getTipoLayout());
		try {
			if (getIsFiltrarPorturma()) {
				setAnoSemestre(getAno() + getSemestre());
			}
			carregarDadosFuncionarioCargos();
			List<BoletimAcademicoRelVO> boletimAcademicoRelVOs = getFacadeFactory().getBoletimAcademicoRelFacade()
					.criarObjeto(getMatriculaVO(), getTipoLayout(), getIsFiltrarPorAluno(), getAnoSemestre(),
							getTurmaVO(), getUnidadeEnsinoVO(), getConfiguracaoFinanceiroPadraoSistema(),
							getApresentarDisciplinaComposta(), getUsuarioLogado(), getApresentarCampoAssinatura(),
							getApresentarQuantidadeFaltasAluno(), getFuncionarioPrincipalVO(),
							getCargoFuncionarioPrincipal(), getFuncionarioSecundarioVO(),
							getCargoFuncionarioSecundario(), getBimestre(), getSituacaoRecuperacaoNota(),
							getApresentarApenasNotaTipoMedia(), getApresentarAprovadoPorAproveitamentoComoAprovado(),
							getApresentarCampoAssinaturaResponsavel(), getApresentarReprovadoPorFaltaComoReprovado(),
							getFiltroRelatorioAcademicoVO(), getGradeCurricularVO(), true, 
							getApresentarCargaHorariaCursada(), new ArrayList<>(0),
							getTrazerAlunoTransferencia());

			List<BoletimAcademicoRelVO> listaFinal = new ArrayList<BoletimAcademicoRelVO>();
			Iterator i = boletimAcademicoRelVOs.iterator();
			while (i.hasNext()) {
				BoletimAcademicoRelVO bol = (BoletimAcademicoRelVO) i.next();
				if (getTipoLayout().equals("BoletimAcademicoRel")) {
					if (!bol.getHistorico().isEmpty()) {
						listaFinal.add(bol);
					}
				} else {
					listaFinal.add(bol);
				}
				
				bol.setLegendaSituacaoHistorico(getFacadeFactory().getBoletimAcademicoRelFacade()
						.realizarCriacaoLegendaSituacaoDisciplinaHistorico(bol, getMatriculaVO()));
			}
			if (listaFinal.isEmpty()) {
				setMensagemID("msg_relatorio_sem_dados");
				return;
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "BoletimAcademicoRelControle", "Inicializando Geração de Relatório Boletim Acadêmico", "Emitindo Relatório");
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(BoletimAcademicoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(BoletimAcademicoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
			getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
			getSuperParametroRelVO().setListaObjetos(listaFinal);
			getSuperParametroRelVO().adicionarParametro("apresentarQuantidadeFaltasAluno", getApresentarQuantidadeFaltasAluno());
			getSuperParametroRelVO().adicionarParametro("apresentarCampoAssinatura", getApresentarCampoAssinatura());
			getSuperParametroRelVO().adicionarParametro("apresentarAdvertenciaAluno", getApresentarAdvertenciaAluno());
			getSuperParametroRelVO().adicionarParametro("bimestre", getBimestre() != null ? getBimestre().name() : "");
			getSuperParametroRelVO().adicionarParametro("gerarUmAlunoPorPagina", getGerarUmAlunoPorPagina());
			getSuperParametroRelVO().adicionarParametro("apresentarCargaHorariaCursada", getApresentarCargaHorariaCursada());
			getSuperParametroRelVO().adicionarParametro("observacao", getObservacao());
			getSuperParametroRelVO().adicionarParametro("assinarDigitalmente", isAssinarDigitalmente());
			adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
			if (getIsFiltrarPorAluno()) {
				unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(matriculaVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
				String enderecoUnidadeEnsino = unidadeEnsinoVO.getEndereco() + ", nº " + unidadeEnsinoVO.getNumero() + ", " + unidadeEnsinoVO.getSetor() + ", " + unidadeEnsinoVO.getCidade().getNome() + "-" + unidadeEnsinoVO.getCidade().getEstado().getSigla() + ", Fone:" + unidadeEnsinoVO.getTelComercial1();
				getSuperParametroRelVO().adicionarParametro("endereco", enderecoUnidadeEnsino);
				getSuperParametroRelVO().setNomeEmpresa(unidadeEnsinoVO.getNomeExpedicaoDiploma().trim().isEmpty() ? unidadeEnsinoVO.getNome() : unidadeEnsinoVO.getNomeExpedicaoDiploma());
				if (unidadeEnsinoVO.getExisteLogoRelatorio()) {
					String urlLogoUnidadeEnsinoRelatorio = unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoRelatorio();
					String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
				} else {
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
				}
			} else {
				unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
				String enderecoUnidadeEnsino = unidadeEnsinoVO.getEndereco() + ", nº " + unidadeEnsinoVO.getNumero() + ", " + unidadeEnsinoVO.getSetor() + ", " + unidadeEnsinoVO.getCidade().getNome() + "-" + unidadeEnsinoVO.getCidade().getEstado().getSigla() + ", Fone:" + unidadeEnsinoVO.getTelComercial1();
				getSuperParametroRelVO().adicionarParametro("endereco", enderecoUnidadeEnsino);
				getSuperParametroRelVO().setNomeEmpresa(unidadeEnsinoVO.getNomeExpedicaoDiploma().trim().isEmpty() ? unidadeEnsinoVO.getNome() : unidadeEnsinoVO.getNomeExpedicaoDiploma());
				if (unidadeEnsinoVO.getExisteLogoRelatorio()) {
					String urlLogoUnidadeEnsinoRelatorio = unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoRelatorio();
					String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
				} else {
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
				}
			}
			if(isAssinarDigitalmente() && Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), getUsuarioLogado()))
					&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), getUsuarioLogado()).getConfiguracaoGedBoletimAcademicoVO().getApresentarAssinaturaDigitalizadoFuncionario()){
			if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO().getCodigo())) {
				getFuncionarioPrincipalVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(getFuncionarioPrincipalVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
				if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getCodigo())) {
					getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioPrimario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome());
					getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", true);
				} else {
					getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
				}
			}
//			FUNCIONÁRIO SECUNDÁRIO
			if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getCodigo())) {
				getFuncionarioSecundarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(getFuncionarioSecundarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
				if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo())) {
					getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioSecundario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome());
					getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", true);
				} else {
					getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
				}
			} 
			}else {
				getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioPrincipal", "");
				getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioSecundario", "");
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
			}
			realizarImpressaoRelatorio();
			persistirParametroRelatorio();
			persistirLayoutPadrao();

			if (isAssinarDigitalmente()) {
				setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorBoletimAcademico(getCaminhoRelatorio(), getMatriculaVO(), getGradeCurricularVO(), getTurmaVO(), null, "", "", TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO,getProvedorDeAssinaturaEnum(), getCorAssinaturaDigitalmente(), 100f, getLarguraAssinatura(), getFuncionarioPrincipalVO(), getCargoFuncionarioPrincipal().getNome(), "", getFuncionarioSecundarioVO(), getCargoFuncionarioSecundario().getNome(), "", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				getListaDocumentoAsssinados().clear();
				montarDadosAssinaturaDigitalFuncionario();
				inicializarDadosAssinaturaBoletimAcademico();
			}

			// removerObjetoMemoria(this);
			registrarAtividadeUsuario(getUsuarioLogado(), "BoletimAcademicoRelControle", "Finalizando Geração de Relatório Boletim Acadêmico", "Emitindo Relatório");
			if (!listaFinal.isEmpty()) {
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			nomeEntidade = null;
			design = null;
		}
	}
	
	private void montarDadosAssinaturaDigitalFuncionario() throws Exception {
		ConfiguracaoGEDVO configGEDVO = null;
		if (getIsFiltrarPorAluno()) {
			configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
		} else {
			configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
		}
		if (!Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
			return;
		}
//		FUNCIONÁRIO PRIMÁRIO
		if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO().getCodigo())) {
			getFuncionarioPrincipalVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(getFuncionarioPrincipalVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioPrimario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", configGEDVO.getConfiguracaoGedBoletimAcademicoVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
			}
		}
//		FUNCIONÁRIO SECUNDÁRIO
		if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getCodigo())) {
			getFuncionarioSecundarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(getFuncionarioSecundarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioSecundario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", configGEDVO.getConfiguracaoGedBoletimAcademicoVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
			}
		} 
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(
						getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
//						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(),
						getUsuarioLogado());
//				if (!objs.getMatricula().equals("")) {
//					objs.add(objs);
//				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
						getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
						getUnidadeEnsinoLogado().getCodigo(), false,
						getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("situacao")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCursoSituacaoMatricula(
						getValorConsultaAluno(), getSituacaoMatricula(), this.getUnidadeEnsinoLogado().getCodigo(),
						false, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPadraoSistema(),
						getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,getUsuarioLogado());
  		    }
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public String getAbreModalCursoAuxiliaremConsultar() {
		if (getCampoConsultaAluno().equals("situacao") && getValorConsultaAluno().equals("")) {
			return "RichFaces.$('panelAlunoAux').show()";
		}
		return "";
	}

	public boolean getIsApresentarComboSituacaoMatricula() {
		if (getCampoConsultaAluno().equals("situacao")) {
			return true;
		}
		return false;
	}

	public String getAbreModalAuxiliarCurso() {
		if (!getSituacaoMatricula().equals("")) {
			return "RichFaces.$('panelAlunoAux').show()";
		}
		return "";
	}

	public void validarNomeCursoAuxiliar() {
		if (getValorConsultaAluno().equals("")) {
			try {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		} else {
			setMensagemID("msg_entre_prmrelatorio");
		}

	}

	public String getFechaModalAuxiliarCurso() {
		if (!getValorConsultaAluno().equals("")) {
			return "RichFaces.$('panelAlunoAux').hide()";
		}
		return "";
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
				obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
		setMatriculaVO(objCompleto);
		montarListaSelectItemGradeCurricularAluno();
		obj = null;
		objCompleto = null;
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
		verificarParametroRelatorio();
		montarListaSelectItemAnoSemestre();
		montarConfiguracaoAcademico();
		inicializarDadosAssinaturaBoletimAcademico();
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
					getMatriculaVO().getMatricula(), this.getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.TODOS,
					getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			montarListaSelectItemGradeCurricularAluno();
			inicializarDadosAssinaturaBoletimAcademico();
			getApresentarCampos();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
			verificarParametroRelatorio();
			montarListaSelectItemAnoSemestre();
			montarConfiguracaoAcademico();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}
	
	
	
	public void consultarAlunoPorRegistroAcademico() {
		try {
			MatriculaVO  objAluno = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorRegistroAcademico(getMatriculaVO().getAluno().getRegistroAcademico(), this.getUnidadeEnsinoLogado().getCodigo(), 0,  Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(this.getUnidadeEnsinoLogado().getCodigo()), getUsuarioLogado());
				if (objAluno == null || objAluno.getMatricula().equals("") ) {
					throw new Exception("Aluno de registro Acadêmico " + getMatriculaVO().getAluno().getRegistroAcademico() + " não encontrado. Verifique se o número de matrícula está correto.");
				}		
				setMatriculaVO(objAluno);
				montarListaSelectItemGradeCurricularAluno();
				inicializarDadosAssinaturaBoletimAcademico();
				getApresentarCampos();
				setMensagemDetalhada("");
				setMensagemID("msg_dados_consultados");
				verificarParametroRelatorio();
				montarListaSelectItemAnoSemestre();
				montarConfiguracaoAcademico();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
}
	
	
	

	public List<SelectItem> getTipoComboLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		if (getCampoFiltroPor().equals("aluno")) {
			if (getMatriculaVO().getCurso().getNivelEducacional().equals("IN")) {
				itens.add(new SelectItem("BoletimAcademicoEnsinoFundamentalRel", "Educação Infantil"));
				itens.add(new SelectItem("FichaIndividualRel", "Ficha Individual - Modelo Paisagem"));
				if (!getTipoLayout().equals("BoletimAcademicoEnsinoFundamentalRel")
						&& !getTipoLayout().equals("FichaIndividualRel") && !getTipoLayout().equals("FichaAluno2Rel")) {
					setTipoLayout("BoletimAcademicoEnsinoFundamentalRel");
				}
			} else if (getMatriculaVO().getCurso().getNivelEducacional().equals("BA")) {
				itens.add(new SelectItem("BoletimAcademicoEnsinoFundamentalRel", "Ensino Fundamental I"));
				itens.add(new SelectItem("BoletimAcademicoEnsinoMedioRel", "Ensino Fundamental - Modelo Retrato"));
				itens.add(new SelectItem("BoletimAcademicoEnsinoMedio2Rel", "Ensino Fundamental - Modelo Paisagem"));
				itens.add(new SelectItem("FichaIndividualRel", "Ficha Individual - Modelo Paisagem"));
				if (!getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel")
						&& !getTipoLayout().equals("BoletimAcademicoEnsinoMedio2Rel")
						&& !getTipoLayout().equals("FichaAluno2Rel") && !getTipoLayout().equals("FichaIndividualRel")
						&& !getTipoLayout().equals("BoletimAcademicoEnsinoFundamentalRel")) {

					setTipoLayout("BoletimAcademicoEnsinoMedioRel");

				}
			} else if (getMatriculaVO().getCurso().getNivelEducacional().equals("ME")) {
				itens.add(new SelectItem("BoletimAcademicoEnsinoMedioRel", "Ensino Médio - Modelo Retrato"));
				itens.add(new SelectItem("BoletimAcademicoEnsinoMedio2Rel", "Ensino Médio - Modelo Paisagem"));
				itens.add(new SelectItem("FichaIndividualRel", "Ficha Individual - Modelo Paisagem"));
				if (!getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel")
						&& !getTipoLayout().equals("BoletimAcademicoEnsinoMedio2Rel")
						&& !getTipoLayout().equals("FichaAluno2Rel") && !getTipoLayout().equals("FichaIndividualRel")) {

					setTipoLayout("BoletimAcademicoEnsinoMedioRel");

				}
			} else {
				if (!getTipoLayout().equals("BoletimAcademicoRel") && !getTipoLayout().equals("BoletimAcademico2Rel")
						&& !getTipoLayout().equals("FichaAluno2Rel")) {
					setTipoLayout("BoletimAcademico2Rel");
				}
				itens.add(new SelectItem("BoletimAcademicoRel", "Layout 1"));
				itens.add(new SelectItem("BoletimAcademico2Rel", "Layout 2"));
			}
		} else {
			if (getMatriculaVO().getCurso().getNivelEducacional().equals("IN")) {
				itens.add(new SelectItem("BoletimAcademicoEnsinoFundamentalRel", "Educação Infantil"));
				itens.add(new SelectItem("FichaIndividualRel", "Ficha Individual - Modelo Paisagem"));
				if (!getTipoLayout().equals("BoletimAcademicoEnsinoFundamentalRel")
						&& !getTipoLayout().equals("FichaIndividualRel") && !getTipoLayout().equals("FichaAluno2Rel")) {
					setTipoLayout("BoletimAcademicoEnsinoFundamentalRel");
				}
			} else if (getTurmaVO().getCurso().getNivelEducacional().equals("BA")) {
				itens.add(new SelectItem("BoletimAcademicoEnsinoFundamentalRel", "Ensino Fundamental I"));
				itens.add(new SelectItem("BoletimAcademicoEnsinoMedioRel", "Ensino Fundamental - Modelo Retrato"));
				itens.add(new SelectItem("BoletimAcademicoEnsinoMedio2Rel", "Ensino Fundamental - Modelo Paisagem"));
				itens.add(new SelectItem("FichaIndividualRel", "Ficha Individual - Modelo Paisagem"));
				if (!getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel")
						&& !getTipoLayout().equals("BoletimAcademicoEnsinoMedio2Rel")
						&& !getTipoLayout().equals("FichaAluno2Rel") && !getTipoLayout().equals("FichaIndividualRel")
						&& !getTipoLayout().equals("BoletimAcademicoEnsinoFundamentalRel")) {

					setTipoLayout("BoletimAcademicoEnsinoMedioRel");

				}
			} else if (getTurmaVO().getCurso().getNivelEducacional().equals("ME")) {
				itens.add(new SelectItem("BoletimAcademicoEnsinoMedioRel", "Ensino Médio - Modelo Retrato"));
				itens.add(new SelectItem("BoletimAcademicoEnsinoMedio2Rel", "Ensino Médio - Modelo Paisagem"));
				itens.add(new SelectItem("FichaIndividualRel", "Ficha Individual - Modelo Paisagem"));
				if (!getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel")
						&& !getTipoLayout().equals("BoletimAcademicoEnsinoMedio2Rel")
						&& !getTipoLayout().equals("FichaAluno2Rel") && !getTipoLayout().equals("FichaIndividualRel")) {

					setTipoLayout("BoletimAcademicoEnsinoMedioRel");

				}
			} else {
				if (!getTipoLayout().equals("BoletimAcademicoRel") && !getTipoLayout().equals("BoletimAcademico2Rel")
						&& !getTipoLayout().equals("FichaAluno2Rel")) {
					setTipoLayout("BoletimAcademico2Rel");
				}
				itens.add(new SelectItem("BoletimAcademicoRel", "Layout 1"));
				itens.add(new SelectItem("BoletimAcademico2Rel", "Layout 2"));
			}
		}
		itens.add(new SelectItem("FichaAluno2Rel", "Ficha Individual - Modelo Retrato"));
		itens.add(new SelectItem("FichaIndividualLayout2Rel", "Ficha Individual Layout 2 - Modelo Paisagem"));
		itens.add(new SelectItem("FichaIndividualLayout2RetratoRel", "Ficha Individual Layout 2 - Modelo Retrato"));
		return itens;
	}

	public Boolean getApresentarCampos() {
		if ((getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0)
				|| (getTurmaVO() != null && getTurmaVO().getCodigo() != 0)) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarCamposFiltroTurma() {
		if (getTurmaVO() != null && getTurmaVO().getCodigo() != 0) {
			return true;
		}
		return false;
	}

	public boolean isApresentarCamposAluno() {
		return (getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0
				&& getIsFiltrarPorAluno());
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void limparIdentificador() {
		setMatriculaVO(new MatriculaVO());
		setTurmaVO(new TurmaVO());
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			limparIdentificador();
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
		} catch (Exception e) {
			// //System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
				super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
				getUsuarioLogado());
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
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

	public List<SelectItem> getTipoConsultaComboFiltroPor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
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
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(obj);
		montarListaSelectItemGradeCurricularTurma();
		setAno("");
		setSemestre("");
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();
		inicializarDadosAssinaturaBoletimAcademico();
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1º"));
		itens.add(new SelectItem("2", "2º"));
		return itens;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
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

	public List<MatriculaVO> getListaMatriculas() {
		if (listaMatriculas == null) {
			listaMatriculas = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculas;
	}

	public void setListaMatriculas(List<MatriculaVO> listaMatriculas) {
		this.listaMatriculas = listaMatriculas;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public Boolean getIsFiltrarPorturma() {
		if (getCampoFiltroPor().equals("turma")) {
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

	public Boolean getIsFiltrarPorAno() {
		if (getIsFiltrarPorAluno()) {
			if (getMatriculaVO().getMatricula().trim().isEmpty()) {
				return false;
			}
			return getMatriculaVO().getCurso().getPeriodicidade().equals("AN")
					|| getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
		} else if (getIsFiltrarPorturma()) {
			if (getTurmaVO().getCodigo() != 0 
					&& (!getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() && !getTurmaVO().getTurmaAgrupada() && (getTurmaVO().getCurso().getPeriodicidade().equals("AN")))
					|| getTurmaVO().getCurso().getPeriodicidade().equals("SE")) {
				return true;
			} else {
				if (getTurmaVO().getTurmaAgrupada()) {
					for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
						if (!turmaAgrupada.getTurma().getAnual() && !turmaAgrupada.getTurma().getSemestral()) {
							return false;
						}
					}
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public Boolean getIsFiltrarPorSemestre() {
		if (getIsFiltrarPorAluno()) {
			if (getMatriculaVO().getMatricula().trim().isEmpty()) {
				return false;
			}
			return getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
		} else if (getIsFiltrarPorturma()) {
			if (getTurmaVO().getCodigo() != 0 
					&& (!getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() && !getTurmaVO().getTurmaAgrupada() && getTurmaVO().getCurso().getPeriodicidade().equals("SE"))
					|| getTurmaVO().getCurso().getPeriodicidade().equals("SE")) {
				return true;
			} else {
				if (getTurmaVO().getTurmaAgrupada()) {
					for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
						if (!turmaAgrupada.getTurma().getSemestral()) {
							return false;
						}
					}
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "BoletimAcademicoRel";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public Boolean getApresentarDisciplinaComposta() {
		if (apresentarDisciplinaComposta == null) {
			apresentarDisciplinaComposta = false;
		}
		return apresentarDisciplinaComposta;
	}

	public void setApresentarDisciplinaComposta(Boolean apresentarDisciplinaComposta) {
		this.apresentarDisciplinaComposta = apresentarDisciplinaComposta;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioPrincipal() {
		if (selectItemsCargoFuncionarioPrincipal == null) {
			selectItemsCargoFuncionarioPrincipal = new ArrayList<SelectItem>(0);
		}
		return selectItemsCargoFuncionarioPrincipal;
	}

	public void setSelectItemsCargoFuncionarioPrincipal(List<SelectItem> selectItemsCargoFuncionarioPrincipal) {
		this.selectItemsCargoFuncionarioPrincipal = selectItemsCargoFuncionarioPrincipal;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioSecundario() {
		if (selectItemsCargoFuncionarioSecundario == null) {
			selectItemsCargoFuncionarioSecundario = new ArrayList<SelectItem>(0);
		}
		return selectItemsCargoFuncionarioSecundario;
	}

	public void setSelectItemsCargoFuncionarioSecundario(List<SelectItem> selectItemsCargoFuncionarioSecundario) {
		this.selectItemsCargoFuncionarioSecundario = selectItemsCargoFuncionarioSecundario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public void selecionarFuncionarioPrincipal() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap()
					.get("funcionarioPrincipalItens");
			setFuncionarioPrincipalVO(obj);
			consultarFuncionarioPrincipal();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionarioSecundario() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap()
					.get("funcionarioSecundarioItens");
			setFuncionarioSecundarioVO(obj);
			consultarFuncionarioSecundario();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioPrincipal() throws Exception {
		try {
			setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(getFuncionarioPrincipalVO().getMatricula()));
			setSelectItemsCargoFuncionarioPrincipal(
					montarComboCargoFuncionario(getFuncionarioPrincipalVO().getFuncionarioCargoVOs()));
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
					montarComboCargoFuncionario(getFuncionarioSecundarioVO().getFuncionarioCargoVOs()));
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

	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
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

	public boolean isApresentarCampoCargoFuncionarioSecundario() {
		return getApresentarCampos() && Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO()) && getApresentarCampoAssinatura();
	}

	public boolean isApresentarCampoCargoFuncionarioPrincipal() {
		return getApresentarCampos() && Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO()) && getApresentarCampoAssinatura();
	}

	public void limparDadosFuncionarioPrincipal() {
		removerObjetoMemoria(getFuncionarioPrincipalVO());
		setFuncionarioPrincipalVO(new FuncionarioVO());
	}

	public void limparDadosFuncionarioSecundario() {
		removerObjetoMemoria(getFuncionarioSecundarioVO());
		setFuncionarioSecundarioVO(new FuncionarioVO());
	}

	private void persistirParametroRelatorio() throws Exception {
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("BoletimAcademicoRel", "apresentarCampoAssinatura", getApresentarCampoAssinatura(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("BoletimAcademicoRel", "apresentarQuantidadeFaltasAluno", getApresentarQuantidadeFaltasAluno(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("BoletimAcademicoRel", "apresentarAprovadoPorAproveitamentoComoAprovado", getApresentarAprovadoPorAproveitamentoComoAprovado(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("BoletimAcademicoRel", "apresentarCampoAssinaturaResponsavel", getApresentarCampoAssinaturaResponsavel(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("BoletimAcademicoRel", "apresentarAdvertenciaAluno", getApresentarAdvertenciaAluno(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("BoletimAcademicoRel", "apresentarReprovadoPorFaltaComoReprovado", getApresentarReprovadoPorFaltaComoReprovado(), getUsuarioLogado());
			getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("BoletimAcademicoRel", "assinarRelatorioDigitalmente", isAssinarDigitalmente(), getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO())) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFuncionarioPrincipalVO().getCodigo().toString(), "BoletimAcademicoRel", "assinaturafunc1", getUsuarioLogado());
			} else {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2("", "BoletimAcademicoRel", "assinaturafunc1", getUsuarioLogado());
			}
			if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO())) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFuncionarioSecundarioVO().getCodigo().toString(), "BoletimAcademicoRel", "assinaturafunc2", getUsuarioLogado());
			} else {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2("", "BoletimAcademicoRel", "assinaturafunc2", getUsuarioLogado());
			}
		}
	}
	
	private void persistirLayoutPadrao() throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getObservacao(), BoletimAcademicoRelControle.class.getSimpleName(), "matricula_aluno_observacao", getUsuarioLogado());	
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoLayout(), BoletimAcademicoRelControle.class.getSimpleName(), "tipoLayout", getUsuarioLogado());	
	}

	private void verificarParametroRelatorio() throws Exception {
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			setApresentarCampoAssinatura(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "apresentarCampoAssinatura", false, getUsuarioLogado()).getApresentarCampo());
			setApresentarQuantidadeFaltasAluno(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "apresentarQuantidadeFaltasAluno", false, getUsuarioLogado()).getApresentarCampo());
			setApresentarAprovadoPorAproveitamentoComoAprovado(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "apresentarAprovadoPorAproveitamentoComoAprovado", false, getUsuarioLogado()).getApresentarCampo());
			setApresentarCampoAssinaturaResponsavel(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "apresentarCampoAssinaturaResponsavel", false, getUsuarioLogado()).getApresentarCampo());
			setApresentarAdvertenciaAluno(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "apresentarAdvertenciaAluno", false, getUsuarioLogado()).getApresentarCampo());
			setApresentarReprovadoPorFaltaComoReprovado(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "apresentarReprovadoPorFaltaComoReprovado", false, getUsuarioLogado()).getApresentarCampo());
			setAssinarDigitalmente(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "apresentarReprovadoPorFaltaComoReprovado", false, getUsuarioLogado()).getApresentarCampo());
			String assinaturafunc1 = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "assinaturafunc1", false, getUsuarioLogado()).getValor();
			String assinaturafunc2 = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("BoletimAcademicoRel", "assinaturafunc2", false, getUsuarioLogado()).getValor();
			if (Uteis.isAtributoPreenchido(assinaturafunc1)) {
				setFuncionarioPrincipalVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(Integer.parseInt(assinaturafunc1), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				consultarFuncionarioPrincipal();
				setAssinarDigitalmente(true);
			}
			if (Uteis.isAtributoPreenchido(assinaturafunc2)) {
				setFuncionarioSecundarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(Integer.parseInt(assinaturafunc2), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				consultarFuncionarioSecundario();
				setAssinarDigitalmente(true);
			}
		}
	}

	public boolean getIsApresentarBoleanoQuantidadeFaltasAluno() {
		return getApresentarCampos() && (getTipoLayout().equals("BoletimAcademico2Rel")
				|| getTipoLayout().equals("BoletimAcademicoEnsinoMedio2Rel")
				|| getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel") || getTipoLayout().equals("FichaAluno2Rel")
				|| getTipoLayout().equals("FichaIndividualRel"));
	}

	public boolean getIsApresentarBooleanApenasNotaTipoMedia() {
		return getApresentarCampos() && (getTipoLayout().equals("BoletimAcademicoEnsinoFundamentalRel")
				|| getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel")
				|| getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel") || getTipoLayout().equals("FichaAluno2Rel")
				|| getTipoLayout().equals("FichaIndividualRel"));
	}
	public boolean getIsApresentarBooleanGerarUmAlunoPorPagina() {
		return getApresentarCampos() && ((getTipoLayout().equals("BoletimAcademicoEnsinoMedioRel") && getApresentarDisciplinaComposta().equals(false))
				|| getTipoLayout().equals("FichaAluno2Rel"));
	}

	public String getSituacaoMatricula() {
		if (situacaoMatricula == null) {
			situacaoMatricula = "";
		}
		return situacaoMatricula;
	}

	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
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

	public String getDataPorExtenso() {
		if (dataPorExtenso == null) {
			dataPorExtenso = "";
		}
		return dataPorExtenso;
	}

	public void setDataPorExtenso(String dataPorExtenso) {
		this.dataPorExtenso = dataPorExtenso;
	}

	public Boolean getApresentarCampoAssinatura() {
		if (apresentarCampoAssinatura == null) {
			apresentarCampoAssinatura = false;
		}
		return apresentarCampoAssinatura;
	}

	public void setApresentarCampoAssinatura(Boolean apresentarCampoAssinatura) {
		this.apresentarCampoAssinatura = apresentarCampoAssinatura;
	}

	public Boolean getApresentarQuantidadeFaltasAluno() {
		if (apresentarQuantidadeFaltasAluno == null) {
			apresentarQuantidadeFaltasAluno = false;
		}
		return apresentarQuantidadeFaltasAluno;
	}

	public void setApresentarQuantidadeFaltasAluno(Boolean apresentarQuantidadeFaltasAluno) {
		this.apresentarQuantidadeFaltasAluno = apresentarQuantidadeFaltasAluno;
	}

	public String getAnoSemestre() {
		if (anoSemestre == null) {
			anoSemestre = "";
		}
		return anoSemestre;
	}

	public void setAnoSemestre(String anoSemestre) {
		this.anoSemestre = anoSemestre;
	}

	public List<SelectItem> getListaSelectItemAnoSemestre() {
		if (listaSelectItemAnoSemestre == null) {
			listaSelectItemAnoSemestre = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAnoSemestre;
	}

	public void setListaSelectItemAnoSemestre(List<SelectItem> listaSelectItemAnoSemestre) {
		this.listaSelectItemAnoSemestre = listaSelectItemAnoSemestre;
	}

	public void montarListaSelectItemAnoSemestre() {
		try {
			getListaSelectItemAnoSemestre().clear();
			getListaSelectItemAnoSemestre().add(new SelectItem("", ""));
			List<String> anoSemestreAdicionados = new ArrayList<>();
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade()
					.consultarAnoSemestreHistoricoPorMatriculaBoletimAcademicoRel(getMatriculaVO().getMatricula(),
							getGradeCurricularVO().getCodigo(), false, getUsuarioLogado());
			for (HistoricoVO obj : historicoVOs) {
				String anoSemestre = obj.getAnoHistorico() + obj.getSemestreHistorico();
				String anoSemestreFormatado = Uteis.isAtributoPreenchido(obj.getAnoHistorico())  ? obj.getAnoHistorico() : "" +  "/" +  obj.getSemestreHistorico() != null ? obj.getSemestreHistorico() : "";
				String periodoMatrizCurricularFormatado = Uteis.isAtributoPreenchido(obj.getPeriodoLetivoMatrizCurricular().getDescricao()) ? obj.getPeriodoLetivoMatrizCurricular().getDescricao() : "";
				if (anoSemestreAdicionados.stream().anyMatch(anoSemestre::equals)) {
					continue;
				}
				if (Uteis.isAtributoPreenchido(obj.getPeriodoLetivoMatrizCurricular())) {
					getListaSelectItemAnoSemestre().add(new SelectItem(anoSemestre, getIsFiltrarPorSemestre()
							? anoSemestreFormatado + " - " + periodoMatrizCurricularFormatado
							: obj.getAnoHistorico() + " - " + periodoMatrizCurricularFormatado));
					anoSemestreAdicionados.add(anoSemestre);
				} else {
					getListaSelectItemAnoSemestre().add(new SelectItem(anoSemestre, getIsFiltrarPorSemestre()
							? anoSemestreFormatado
							: obj.getAnoHistorico() + " - " + periodoMatrizCurricularFormatado));
					anoSemestreAdicionados.add(anoSemestre);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemBimestre() {
		if (listaSelectItemBimestre == null) {
			listaSelectItemBimestre = new ArrayList<SelectItem>(0);
			listaSelectItemBimestre.add(new SelectItem(null, ""));
			listaSelectItemBimestre
					.add(new SelectItem(BimestreEnum.BIMESTRE_01, BimestreEnum.BIMESTRE_01.getValorApresentar()));
			listaSelectItemBimestre
					.add(new SelectItem(BimestreEnum.BIMESTRE_02, BimestreEnum.BIMESTRE_02.getValorApresentar()));
			listaSelectItemBimestre
					.add(new SelectItem(BimestreEnum.BIMESTRE_03, BimestreEnum.BIMESTRE_03.getValorApresentar()));
			listaSelectItemBimestre
					.add(new SelectItem(BimestreEnum.BIMESTRE_04, BimestreEnum.BIMESTRE_04.getValorApresentar()));
			listaSelectItemBimestre
			.add(new SelectItem(BimestreEnum.TRIMESTRE_01, BimestreEnum.TRIMESTRE_01.getValorApresentar()));
			listaSelectItemBimestre
			.add(new SelectItem(BimestreEnum.TRIMESTRE_02, BimestreEnum.TRIMESTRE_02.getValorApresentar()));
			listaSelectItemBimestre
			.add(new SelectItem(BimestreEnum.TRIMESTRE_03, BimestreEnum.TRIMESTRE_03.getValorApresentar()));
			// listaSelectItemBimestre.add(new
			// SelectItem(BimestreEnum.RECUPERACAO_01,
			// BimestreEnum.RECUPERACAO_01.getValorApresentar()));
			// listaSelectItemBimestre.add(new
			// SelectItem(BimestreEnum.RECUPERACAO_02,
			// BimestreEnum.RECUPERACAO_02.getValorApresentar()));
			// listaSelectItemBimestre.add(new
			// SelectItem(BimestreEnum.RESUMO_FINAL,
			// BimestreEnum.RESUMO_FINAL.getValorApresentar()));
			// listaSelectItemBimestre.add(new
			// SelectItem(BimestreEnum.NAO_CONTROLA, "Sem Agrupamento"));
		}
		return listaSelectItemBimestre;
	}

	public void setListaSelectItemBimestre(List<SelectItem> listaSelectItemBimestre) {
		this.listaSelectItemBimestre = listaSelectItemBimestre;
	}

	public List<SelectItem> getListaSelectItemSituacaoRecuperacao() {
		if (listaSelectItemSituacaoRecuperacao == null) {
			listaSelectItemSituacaoRecuperacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(null, ""));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.TODAS,
					SituacaoRecuperacaoNotaEnum.TODAS.getValorApresentar()));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.EM_RECUPERACAO,
					SituacaoRecuperacaoNotaEnum.EM_RECUPERACAO.getValorApresentar()));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA,
					SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA.getValorApresentar()));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA,
					SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA.getValorApresentar()));
			listaSelectItemSituacaoRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO,
					SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO.getValorApresentar()));
		}
		return listaSelectItemSituacaoRecuperacao;
	}

	public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO) {
		superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademicoVO().getAtivo());
		superParametroRelVO.adicionarParametro("filtroAcademicoTrancado",
				getFiltroRelatorioAcademicoVO().getTrancado());
		superParametroRelVO.adicionarParametro("filtroAcademicoCancelado",
				getFiltroRelatorioAcademicoVO().getCancelado());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula",
				getFiltroRelatorioAcademicoVO().getPreMatricula());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada",
				getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
		superParametroRelVO.adicionarParametro("filtroAcademicoConcluido",
				getFiltroRelatorioAcademicoVO().getConcluido());
		superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro",
				getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaExterna",
				getFiltroRelatorioAcademicoVO().getTransferenciaExterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaInterna",
				getFiltroRelatorioAcademicoVO().getTransferenciaInterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoAbandonado",
				getFiltroRelatorioAcademicoVO().getAbandonado());
		superParametroRelVO.adicionarParametro("filtroAcademicoFormado", getFiltroRelatorioAcademicoVO().getFormado());
		superParametroRelVO.adicionarParametro("filtroAcademicoMatriculaAReceber",
				getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoMatriculaRecebida",
				getFiltroRelatorioAcademicoVO().getConfirmado());
	}

	public void setListaSelectItemSituacaoRecuperacao(List<SelectItem> listaSelectItemSituacaoRecuperacao) {
		this.listaSelectItemSituacaoRecuperacao = listaSelectItemSituacaoRecuperacao;
	}

	public BimestreEnum getBimestre() {
		return bimestre;
	}

	public void setBimestre(BimestreEnum bimestre) {
		this.bimestre = bimestre;
	}

	public SituacaoRecuperacaoNotaEnum getSituacaoRecuperacaoNota() {
		return situacaoRecuperacaoNota;
	}

	public void setSituacaoRecuperacaoNota(SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota) {
		this.situacaoRecuperacaoNota = situacaoRecuperacaoNota;
	}

	public Boolean getApresentarApenasNotaTipoMedia() {
		if (apresentarApenasNotaTipoMedia == null) {
			apresentarApenasNotaTipoMedia = false;
		}
		return apresentarApenasNotaTipoMedia;
	}

	public void setApresentarApenasNotaTipoMedia(Boolean apresentarApenasNotaTipoMedia) {
		this.apresentarApenasNotaTipoMedia = apresentarApenasNotaTipoMedia;
	}

	public boolean getIsApresentarCampoBimestre() {
		if (getIsFiltrarPorAluno()) {
			return (getMatriculaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.INFANTIL.getValor())
					|| getMatriculaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.BASICO.getValor())
					|| getMatriculaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor()));
		}
		return (getTurmaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.INFANTIL.getValor())
				|| getTurmaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.BASICO.getValor())
				|| getTurmaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor()));
	}

	public Boolean getApresentarAprovadoPorAproveitamentoComoAprovado() {
		if (apresentarAprovadoPorAproveitamentoComoAprovado == null) {
			apresentarAprovadoPorAproveitamentoComoAprovado = true;
		}
		return apresentarAprovadoPorAproveitamentoComoAprovado;
	}

	public void setApresentarAprovadoPorAproveitamentoComoAprovado(
			Boolean apresentarAprovadoPorAproveitamentoComoAprovado) {
		this.apresentarAprovadoPorAproveitamentoComoAprovado = apresentarAprovadoPorAproveitamentoComoAprovado;
	}

	/**
	 * @return the apresentarCampoAssinaturaResponsavel
	 */
	public Boolean getApresentarCampoAssinaturaResponsavel() {
		if (apresentarCampoAssinaturaResponsavel == null) {
			apresentarCampoAssinaturaResponsavel = false;
		}
		return apresentarCampoAssinaturaResponsavel;
	}

	/**
	 * @param apresentarCampoAssinaturaResponsavel
	 *            the apresentarCampoAssinaturaResponsavel to set
	 */
	public void setApresentarCampoAssinaturaResponsavel(Boolean apresentarCampoAssinaturaResponsavel) {
		this.apresentarCampoAssinaturaResponsavel = apresentarCampoAssinaturaResponsavel;
	}

	/**
	 * @return the apresentarAdvertenciaAluno
	 */
	public Boolean getApresentarAdvertenciaAluno() {
		if (apresentarAdvertenciaAluno == null) {
			apresentarAdvertenciaAluno = false;
		}
		return apresentarAdvertenciaAluno;
	}

	/**
	 * @param apresentarAdvertenciaAluno
	 *            the apresentarAdvertenciaAluno to set
	 */
	public void setApresentarAdvertenciaAluno(Boolean apresentarAdvertenciaAluno) {
		this.apresentarAdvertenciaAluno = apresentarAdvertenciaAluno;
	}

	public Boolean getApresentarReprovadoPorFaltaComoReprovado() {
		if (apresentarReprovadoPorFaltaComoReprovado == null) {
			apresentarReprovadoPorFaltaComoReprovado = false;
		}
		return apresentarReprovadoPorFaltaComoReprovado;
	}

	public void setApresentarReprovadoPorFaltaComoReprovado(Boolean apresentarReprovadoPorFaltaComoReprovado) {
		this.apresentarReprovadoPorFaltaComoReprovado = apresentarReprovadoPorFaltaComoReprovado;
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

	public boolean isApresentarGradeCurricular() {
		return ((getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0
				&& getIsFiltrarPorAluno())
				|| (getTurmaVO() != null && getTurmaVO().getCodigo() != 0 && getIsFiltrarPorturma()
						&& !getTurmaVO().getTurmaAgrupada()));
	}

	public void montarListaSelectItemGradeCurricularAluno() {
		List<GradeCurricularVO> grades = null;
		try {
			// grades =
			// getFacadeFactory().getGradeCurricularFacade().consultarPorMatriculaAluno(getMatriculaVO().getMatricula(),
			// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			grades = getFacadeFactory().getGradeCurricularFacade().consultarGradeAtualGradeAntigaPorMatriculaAluno(
					getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(grades, "codigo", "nome", false));
			getGradeCurricularVO().setCodigo(getMatriculaVO().getGradeCurricularAtual().getCodigo());
			Collections.reverse(getListaSelectItemGradeCurricular());
			// if(getListaSelectItemGradeCurricular().size() > 1){
			// getListaSelectItemGradeCurricular().add(0, new SelectItem(0,
			// "Todas as Grades"));
			// }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(grades);
		}
	}

	public void montarListaSelectItemGradeCurricularTurma() {
		List<GradeCurricularVO> grades = new ArrayList(0);
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

	public Boolean getGerarUmAlunoPorPagina() {
		if (gerarUmAlunoPorPagina == null) {
			gerarUmAlunoPorPagina = true;
		}
		return gerarUmAlunoPorPagina;
	}

	public void setGerarUmAlunoPorPagina(Boolean gerarUmAlunoPorPagina) {
		this.gerarUmAlunoPorPagina = gerarUmAlunoPorPagina;
	}

	public Boolean getApresentarCargaHorariaCursada() {
		if (apresentarCargaHorariaCursada == null) {
			apresentarCargaHorariaCursada = Boolean.FALSE;
		}
		return apresentarCargaHorariaCursada;
	}

	public void setApresentarCargaHorariaCursada(Boolean apresentarCargaHorariaCursada) {
		this.apresentarCargaHorariaCursada = apresentarCargaHorariaCursada;
	}
	
	public Boolean getApresentarCampoApresentarCargaHorariaCursada() {
		return getTipoLayout().equals("FichaAluno2Rel") || getTipoLayout().equals("FichaIndividualRel");
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

	public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademico;
	}

	public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
	}

	public Boolean getTrazerAlunoTransferencia() {
		if (trazerAlunoTransferencia == null) {
			trazerAlunoTransferencia = false;
		}
		return trazerAlunoTransferencia;
	}

	public void setTrazerAlunoTransferencia(Boolean trazerAlunoTransferencia) {
		this.trazerAlunoTransferencia = trazerAlunoTransferencia;
	}
	
	public boolean getApresentarFiltroTrazerAlunoTransferencia() {
		return getIsFiltrarPorturma() && Uteis.isAtributoPreenchido(getTurmaVO()) 
				&& (tipoLayout.equals("BoletimAcademicoEnsinoMedioRel") || tipoLayout.equals("BoletimAcademicoEnsinoMedioRel2") 
				|| tipoLayout.equals("BoletimAcademico2Rel2Alunos") || tipoLayout.equals("FichaAluno2Rel2Alunos") 
				|| tipoLayout.equals("BoletimAcademico2Rel") || tipoLayout.equals("BoletimAcademicoEnsinoMedio2Rel") 
				|| tipoLayout.equals("FichaAluno2Rel") || tipoLayout.equals("FichaIndividualRel") 
				|| tipoLayout.equals("BoletimAcademicoEnsinoFundamentalRel") || tipoLayout.equals("FichaIndividualLayout2Rel") 
				|| tipoLayout.equals("FichaIndividualLayout2RetratoRel"));
	}
	
	public boolean getApresentarObservacao() {
		return Uteis.isAtributoPreenchido(getTurmaVO());
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
	
	public boolean isAssinarDigitalmente() {
		return assinarDigitalmente;
	}

	public void setAssinarDigitalmente(boolean assinarDigitalmente) {
		this.assinarDigitalmente = assinarDigitalmente;
	}
	
	public void inicializarDadosAssinaturaBoletimAcademico() throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		if (getCampoFiltroPor().equals("aluno")) {
			obj.setMatricula(getMatriculaVO());
		} else {
			obj.getTurma().setCodigo(getTurmaVO().getCodigo());
		}
		
		obj.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO);
		setListaDocumentoAsssinados(getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPorRelatorio(obj, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), null, null));
	}
	
	private void carregarDadosFuncionarioCargos() throws Exception {
		if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO())) {
			if (Uteis.isAtributoPreenchido(getCargoFuncionarioPrincipal())) {
				setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
						getCargoFuncionarioPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
						getUsuarioLogado()));
			}
		} else {
			setCargoFuncionarioPrincipal(new CargoVO());
		}
		if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO())) {
			if (Uteis.isAtributoPreenchido(getCargoFuncionarioSecundario())) {
				setCargoFuncionarioSecundario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
						getCargoFuncionarioSecundario().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
						getUsuarioLogado()));
			}
		} else {
			setCargoFuncionarioSecundario(new CargoVO());
		}
	}

	public Boolean getFiltrarPorRegistroAcademico() {
		if(filtrarPorRegistroAcademico == null) {
			filtrarPorRegistroAcademico = Boolean.FALSE;
		}
		return filtrarPorRegistroAcademico;
	}

	public void setFiltrarPorRegistroAcademico(Boolean filtrarPorRegistroAcademico) {
		this.filtrarPorRegistroAcademico = filtrarPorRegistroAcademico;
	}
	
	
	
	
}
