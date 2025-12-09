package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.PerfilTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.parametroRelatorio.academico.PerfilTurmaSuperParametroRelVO;
import controle.arquitetura.SelectItemOrdemValor;

@Controller("PerfilTurmaRelControle")
@Scope("viewScope")
@Lazy
public class PerfilTurmaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private TurmaVO turma;
	private String ano;
	private String semestre;
	private Boolean apresentarFoto;
	private PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;

	public PerfilTurmaRelControle() throws Exception {
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() {
		try {
			String caminhoImagemPadrao = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "visao" + File.separator + "foto_usuario.png";
			registrarAtividadeUsuario(getUsuarioLogado(), "PerfilTurmaRelControle", "Inicializando Geração de Relatório Perfil Turma", "Emitindo Relatório");
			getFacadeFactory().getPerfilTurmaRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo(), getTurma().getIdentificadorTurma());
			setPerfilTurmaSuperParametroRelVO(new PerfilTurmaSuperParametroRelVO());
			List<PerfilTurmaRelVO> listaAlunos = getFacadeFactory().getPerfilTurmaRelFacade().criarObjeto(0, turma.getCodigo(), getAno(), getSemestre(), SituacaoVinculoMatricula.ATIVA.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getPerfilTurmaSuperParametroRelVO(), getConfiguracaoGeralPadraoSistema(), getApresentarFoto(), caminhoImagemPadrao, getFiltroRelatorioAcademicoVO(), true, getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), getUnidadeEnsinoVO().getCodigo());
			if (!listaAlunos.isEmpty()) {
				if (getApresentarFoto()) {
					getPerfilTurmaSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPerfilTurmaRelFacade().getDesignIReportRelatorioPerfilTurmaFoto());
				} else {
					getPerfilTurmaSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPerfilTurmaRelFacade().getDesignIReportRelatorioPerfilTurma());
				}
				getPerfilTurmaSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getPerfilTurmaSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPerfilTurmaRelFacade().getCaminhoBaseRelatorioPerfilTurma());
				getPerfilTurmaSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getPerfilTurmaSuperParametroRelVO().setTituloRelatorio("Relatório Perfil da Turma");
				getPerfilTurmaSuperParametroRelVO().setListaObjetos(listaAlunos);
				getPerfilTurmaSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getPerfilTurmaRelFacade().getCaminhoBaseRelatorioPerfilTurma());
				getPerfilTurmaSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getPerfilTurmaSuperParametroRelVO().setQuantidade(listaAlunos.size());
				getPerfilTurmaSuperParametroRelVO().setMediaIdade(getPerfilTurmaSuperParametroRelVO().getSomaIdade() / getPerfilTurmaSuperParametroRelVO().getQuantidade());
				getPerfilTurmaSuperParametroRelVO().setMediaFeminino(Uteis.arrendondarForcando2CadasDecimais(getPerfilTurmaSuperParametroRelVO().getQtdeFeminino() / getPerfilTurmaSuperParametroRelVO().getQuantidade() * 100));
				getPerfilTurmaSuperParametroRelVO().setMediaMasculino(Uteis.arrendondarForcando2CadasDecimais(getPerfilTurmaSuperParametroRelVO().getQtdeMasculino() / getPerfilTurmaSuperParametroRelVO().getQuantidade() * 100));
				try {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					if (!getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio().equals("") && !getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio().equals("")) {
						File imagem = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
						if(imagem.exists()){
							getPerfilTurmaSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
						}else {
							getPerfilTurmaSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
						}
					} else {
						getPerfilTurmaSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
					}
				} catch (Exception e) {
					e.getMessage();
				}
				getPerfilTurmaSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getPerfilTurmaSuperParametroRelVO().setCurso(listaAlunos.get(0).getCurso());
				getPerfilTurmaSuperParametroRelVO().setTurma(listaAlunos.get(0).getTurma());
				getPerfilTurmaSuperParametroRelVO().setDataInicio("");
				getPerfilTurmaSuperParametroRelVO().setDataFim("");
				getPerfilTurmaSuperParametroRelVO().setFuncionario(listaAlunos.get(0).getCoordenadorCurso());
				if (getTurma().getCodigo() > 0) {
					getPerfilTurmaSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
				} else {
					getPerfilTurmaSuperParametroRelVO().setTurma("TODAS");
				}
				realizarImpressaoRelatorio(getPerfilTurmaSuperParametroRelVO());
				Uteis.removerObjetoMemoria(this);
				inicializarListasSelectItemTodosComboBox();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "PerfilTurmaRelControle", "Finalizando Geração de Relatório Perfil Turma", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDFVisaoProfessor() {
		try {
			String caminhoImagemPadrao = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "visao" + File.separator + "foto_usuarioContorno.png";
			if (getTurma().getCodigo() == null || getTurma().getCodigo() == 0) {
				throw new Exception("O campo Turma deve ser informado");
			}
			setPerfilTurmaSuperParametroRelVO(new PerfilTurmaSuperParametroRelVO());
			boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			List<PerfilTurmaRelVO> listaAlunos = getFacadeFactory().getPerfilTurmaRelFacade().criarObjeto(0, turma.getCodigo(), getAno(), getSemestre(), SituacaoVinculoMatricula.ATIVA.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getPerfilTurmaSuperParametroRelVO(), getConfiguracaoGeralPadraoSistema(), getApresentarFoto(), caminhoImagemPadrao, null, trazerAlunoPendenteFinanceiramente, getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), getUnidadeEnsinoVO().getCodigo());
			if (!listaAlunos.isEmpty()) {
				if (getApresentarFoto()) {
					getPerfilTurmaSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPerfilTurmaRelFacade().getDesignIReportRelatorioPerfilTurmaFoto());
				} else {
					getPerfilTurmaSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPerfilTurmaRelFacade().getDesignIReportRelatorioPerfilTurma());
				}
				getPerfilTurmaSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getPerfilTurmaSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPerfilTurmaRelFacade().getCaminhoBaseRelatorioPerfilTurma());
				getPerfilTurmaSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getPerfilTurmaSuperParametroRelVO().setTituloRelatorio("Relatório Perfil da Turma");
				getPerfilTurmaSuperParametroRelVO().setListaObjetos(listaAlunos);
				getPerfilTurmaSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getPerfilTurmaRelFacade().getCaminhoBaseRelatorioPerfilTurma());
				getPerfilTurmaSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getPerfilTurmaSuperParametroRelVO().setQuantidade(listaAlunos.size());
				getPerfilTurmaSuperParametroRelVO().setMediaIdade(getPerfilTurmaSuperParametroRelVO().getSomaIdade() / getPerfilTurmaSuperParametroRelVO().getQuantidade());
				getPerfilTurmaSuperParametroRelVO().setMediaFeminino(Uteis.arrendondarForcando2CadasDecimais(getPerfilTurmaSuperParametroRelVO().getQtdeFeminino() / getPerfilTurmaSuperParametroRelVO().getQuantidade() * 100));
				getPerfilTurmaSuperParametroRelVO().setMediaMasculino(Uteis.arrendondarForcando2CadasDecimais(getPerfilTurmaSuperParametroRelVO().getQtdeMasculino() / getPerfilTurmaSuperParametroRelVO().getQuantidade() * 100));
				getPerfilTurmaSuperParametroRelVO().setUnidadeEnsino(listaAlunos.get(0).getUnidadeEnsino());
				getPerfilTurmaSuperParametroRelVO().setCurso(listaAlunos.get(0).getCurso());
				getPerfilTurmaSuperParametroRelVO().setTurma(listaAlunos.get(0).getTurma());
				getPerfilTurmaSuperParametroRelVO().setDataInicio("");
				getPerfilTurmaSuperParametroRelVO().setDataFim("");
				getPerfilTurmaSuperParametroRelVO().setFuncionario(listaAlunos.get(0).getCoordenadorCurso());
				if (getTurma().getCodigo() > 0) {
					getPerfilTurmaSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
				} else {
					getPerfilTurmaSuperParametroRelVO().setTurma("TODAS");
				}
				realizarImpressaoRelatorio(getPerfilTurmaSuperParametroRelVO());
				Uteis.removerObjetoMemoria(this);
				if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					montarListaSelectItemTurmaVisaoCoordenador();
				} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					montarListaSelectItemTurma();
				}
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultarTurma() {
		try {
			super.consultar();
			if (getUnidadeEnsinoVO().getCodigo() == 0) {
				throw new Exception("Selecione a Unidade de Ensino.");
			}
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}

	}

	public void selecionarTurma() throws Exception {
		TurmaVO turmaVO = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turmaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		setCampoConsultaTurma("");
		setValorConsultaTurma("");
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getListaTipoLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("RecebimentoPorTurmaRel", "Layout 1 - Sem Observação"));
		itens.add(new SelectItem("RecebimentoPorTurmaRelComObservacao", "Layout 2 - Com Observação"));
		return itens;
	}

	public void limparDadosTurma() {
		setTurma(null);
		setAno(null);
		setSemestre(null);
		setMensagemID("msg_entre_prmrelatorio");
	}

	@PostConstruct
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		if(getUsuarioLogado().getIsApresentarVisaoCoordenador()){
			montarListaSelectItemTurmaVisaoCoordenador();
		}else if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
			montarListaSelectItemTurma();
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemTurmaVisaoCoordenador() {
		List<TurmaVO> listaResultado = null;
		Iterator<TurmaVO> i = null;
		try {
			List<SelectItem> obj = new ArrayList<SelectItem>(0);
			listaResultado = consultarTurmaPorCoordenador();
			getListaSelectItemTurma().clear();
			// obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = i.next();
				if (turma.getTurmaAgrupada()) {
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
				}
				obj.add(new SelectItem(turma.getCodigo(), value));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) obj, ordenador);
			setListaSelectItemTurma(obj);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void preencherDadosTurma() throws Exception {
		if (!getTurma().getCodigo().equals(0)) {
			getFacadeFactory().getTurmaFacade().carregarDados(getTurma(), NivelMontarDados.BASICO, getUsuarioLogado());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemTurma() {
		List<TurmaVO> listaResultado = null;
		Iterator<TurmaVO> i = null;
		try {
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			listaResultado = consultarTurmaPorProfessor();
			objs.add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = i.next();
				if (turma.getTurmaAgrupada()) {					
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
				}
				objs.add(new SelectItem(turma.getCodigo(), value));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemTurma(objs);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
			if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()
					&& getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() != getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultaRapidaTurmaPorProfessorSemestreAnoSituacao(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, false, getUsuarioLogado(), false, true);
			} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()
					&& getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() != getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultaRapidaTurmaPorProfessorSemestreAnoSituacao(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, false, getUsuarioLogado(), true, false);
			} else {
				return getFacadeFactory().getTurmaFacade().consultaRapidaTurmaPorProfessorSemestreAnoSituacao(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, false, getUsuarioLogado(), false, false);
			}
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
	}

	/**
	 * @return the listaSelectItemTurma
	 */
	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	/**
	 * @param listaSelectItemTurma
	 *            the listaSelectItemTurma to set
	 */
	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	/**
	 * @return the listaConsultaTurma
	 */
	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	/**
	 * @param listaConsultaTurma
	 *            the listaConsultaTurma to set
	 */
	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	/**
	 * @return the valorConsultaTurma
	 */
	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	/**
	 * @param valorConsultaTurma
	 *            the valorConsultaTurma to set
	 */
	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	/**
	 * @return the campoConsultaTurma
	 */
	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	/**
	 * @param campoConsultaTurma
	 *            the campoConsultaTurma to set
	 */
	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	/**
	 * @return the unidadeEnsinoVO
	 */
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	/**
	 * @param unidadeEnsinoVO
	 *            the unidadeEnsinoVO to set
	 */
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ano = getVisaoProfessorControle().getAno();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				ano = getVisaoCoordenadorControle().getAno();
			}else {
				ano = Uteis.getAnoDataAtual4Digitos();
			}
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
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				semestre = getVisaoProfessorControle().getSemestre();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				semestre = getVisaoCoordenadorControle().getSemestre();
			}else {
				semestre = Uteis.getSemestreAtual();
			}
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
	 * @return the perfilTurmaSuperParametroRelVO
	 */
	public PerfilTurmaSuperParametroRelVO getPerfilTurmaSuperParametroRelVO() {
		if (perfilTurmaSuperParametroRelVO == null) {
			perfilTurmaSuperParametroRelVO = new PerfilTurmaSuperParametroRelVO();
		}
		return perfilTurmaSuperParametroRelVO;
	}

	/**
	 * @param perfilTurmaSuperParametroRelVO
	 *            the perfilTurmaSuperParametroRelVO to set
	 */
	public void setPerfilTurmaSuperParametroRelVO(PerfilTurmaSuperParametroRelVO perfilTurmaSuperParametroRelVO) {
		this.perfilTurmaSuperParametroRelVO = perfilTurmaSuperParametroRelVO;
	}

	/**
	 * @return the apresentarFoto
	 */
	public Boolean getApresentarFoto() {
		if (apresentarFoto == null) {
			apresentarFoto = Boolean.FALSE;
		}
		return apresentarFoto;
	}

	/**
	 * @param apresentarFoto
	 *            the apresentarFoto to set
	 */
	public void setApresentarFoto(Boolean apresentarFoto) {
		this.apresentarFoto = apresentarFoto;
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

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
			if (Uteis.isAtributoPreenchido(getTurma())) {
				if (getTurma().getAnual() || getTurma().getSemestral()) {
					if (!Uteis.isAtributoPreenchido(getAno()))
						setAno(Uteis.getAnoDataAtual4Digitos());
					return true;
				}
				setAno("");
				return false;
			}			
			return true;
		}
		setAno(Uteis.getAnoDataAtual4Digitos());
		return false;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
			if (Uteis.isAtributoPreenchido(getTurma())) {
				if (getTurma().getSemestral()) {
					if (!Uteis.isAtributoPreenchido(getSemestre()))
						setSemestre(Uteis.getSemestreAtual());
					return true;
				}
				setSemestre("");
				return false;
			}			
			return true;
		}
		setSemestre(Uteis.getSemestreAtual());
		return false;
	}

	public boolean getIsApresentarCampoAno() {
		if (!getTurma().getIntegral()) {
			if (!Uteis.isAtributoPreenchido(getAno()))
				setAno(Uteis.getAnoDataAtual4Digitos());
			return true;
		}
		setAno("");
		return false;
	}

	public boolean getIsApresentarCampoSemestre() {
		if (getTurma().getSemestral()) {
			if (!Uteis.isAtributoPreenchido(getSemestre()))
				setSemestre(Uteis.getSemestreAtual());
			return true;
		}
		setSemestre("");
		return false;
	}

}
