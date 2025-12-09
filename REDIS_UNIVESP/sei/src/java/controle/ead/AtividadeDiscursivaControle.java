package controle.ead;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.FilterFactory;
import controle.arquitetura.SuperControle;
import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.ead.AtividadeDiscursivaInteracaoVO;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.enumeradores.ArtefatoEntregaEnum;
import negocio.comuns.ead.enumeradores.DefinicaoDataEntregaAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.PublicoAlvoAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.SituacaoRespostaAtividadeDiscursivaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("AtividadeDiscursivaControle")
@Scope("viewScope")
@Lazy
public class AtividadeDiscursivaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private AtividadeDiscursivaVO atividadeDiscursivaVO;
	private AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO;
	private AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO;
	private List<SelectItem> listaSelectItemDisciplinasTurma;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemNota;
	private List<SelectItem> listaSelectItemNotaConceito;
	private List<SelectItem> listaSelectItemPublicoAlvo;
	private List<SelectItem> listaSelectItemAnoSemestre;
	private Boolean buscarTurmasAnteriores;
	private Boolean permitirInformarTipoNota;
	private DataModelo controleConsultaInteracao;
	private HistoricoVO historico;

	private List<SelectItem> tipoConsultaComboAluno;
	protected List<MatriculaVO> listaConsultaAluno;
	protected String valorConsultaAluno;
	protected String campoConsultaAluno;
	private boolean navegarProfessorAtividadeDiscursivaMonitoramentoEad = false;
	
	private String campoSemestre;
	private String campoAno;
	
	private String filterAnoSemestre;
	private List<SelectItem> listaSelectItemVariavelNotaCfaVOs;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private Boolean mensagemAlertaConfiguracaoNota;
	private String abrirModalConfirmarSoliticaoAvaliacaoProfessor;
	private Boolean gravandoInteracao;

	@PostConstruct
	public void init() {
		try {
			setAtividadeDiscursivaVO(new AtividadeDiscursivaVO());
			getAtividadeDiscursivaVO().setNovoObj(true);
			getAtividadeDiscursivaVO().setSemestre(Uteis.getSemestreAtual());
			getAtividadeDiscursivaVO().setAno(Uteis.getAnoDataAtual4Digitos());
			getControleConsulta().getListaConsulta().clear();
			montarListaSemestreAno();
			if (context().getExternalContext().getSessionMap().containsKey("requerimentoAtividadeDiscursiva") && context().getExternalContext().getSessionMap().get("requerimentoAtividadeDiscursiva") != null && context().getExternalContext().getSessionMap().get("requerimentoAtividadeDiscursiva") instanceof RequerimentoVO && getNomeTelaAtual().contains("atividadeDiscursivaProfessorForm")) {
				inicializarAtividadeDiscursivaPorRequerimento();
			} else if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
				if(context().getExternalContext().getSessionMap().get("disciplinaSelecionada") != null) {	
					FilterFactory ff = (FilterFactory) getControlador("FilterFactory");
					ff.getMapFilter().clear();
					ff.getMapFilter().put("items3", new FilterFactory("atividadeDiscursivaVO.disciplinaVO.nome", "items3", TipoCampoEnum.INTEIRO));
					ff.getMapFilter().get("items3").setFiltro(((MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getSessionMap().remove("disciplinaSelecionada")).getDisciplina().getNome());
					setFilterAnoSemestre(getVisaoAlunoControle().getFiltroAnoSemestreTelaInicial());					
				}
				consultarAtividadesDiscursivasVisaoAluno();
				if(Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("navegarAtividadeDiscursivaPorCalendario"))) {
					inicializarTelaConsultaAtividadeDiscursivaVisaoAlunoPorCalendario();	
				}
			} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {				
				if (Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("navegarProfessorAtividadeDiscursivaMonitoramentoEad"))) {
					carregarDadosMonitorAlunoEad();
				} else {
					montarListaSelectItemTurmaProfessor();
					inicializarTelaConsultaAtividadeDiscursivaVisaoProfessor();
				}
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void carregarDadosMonitorAlunoEad() {
		try {
			if (context().getExternalContext().getSessionMap().get("ativiadesDiscursivas") != null) {
				setAtividadeDiscursivaRespostaAlunoVO((AtividadeDiscursivaRespostaAlunoVO) context().getExternalContext().getSessionMap().get("ativiadesDiscursivas"));
			}
			if (getAtividadeDiscursivaRespostaAlunoVO().getCodigo() != 0) {
				setAtividadeDiscursivaRespostaAlunoVO(getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarPorChavePrimaria(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				setAtividadeDiscursivaVO(getAtividadeDiscursivaRespostaAlunoVO().getAtividadeDiscursivaVO());
				getControleConsultaInteracao().setPage(0);
				getControleConsultaInteracao().setPaginaAtual(1);
				getControleConsultaInteracao().setLimitePorPagina(10);
				getControleConsultaInteracao().setOffset(1);
				getControleConsultaInteracao().setTotalRegistrosEncontrados(0);
				getControleConsultaInteracao().setListaConsulta(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarInteracoesPorChaveAtividadeDiscursivaAluno(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getControleConsultaInteracao().getLimitePorPagina(), getControleConsultaInteracao().getOffset(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getControleConsultaInteracao().setTotalRegistrosEncontrados(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarTotalRegistroInteracao(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getUsuarioLogado().getCodigo()));
			} else {
				setControleConsultaInteracao(null);
			}
			setNavegarProfessorAtividadeDiscursivaMonitoramentoEad(true);
			context().getExternalContext().getSessionMap().remove("setNavegarProfessorAtividadeDiscursivaMonitoramentoEad");
			context().getExternalContext().getSessionMap().remove("ativiadesDiscursivas");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String navegarProfessorAtividadeDiscursivaMonitoramentoEad() {
		context().getExternalContext().getSessionMap().put("ativiadesDiscursivas", getAtividadeDiscursivaRespostaAlunoVO());
		context().getExternalContext().getSessionMap().put("navegarProfessorAtividadeDiscursivaMonitoramentoEad", true);
		return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoProfessor/monitoramentoAlunosEADVisaoProfessorCons.xhtml");
	}

	public String novo() {

		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		setMensagemDetalhada("");
		setAtividadeDiscursivaVO(new AtividadeDiscursivaVO());
		getAtividadeDiscursivaVO().getResponsavelCadastro().setNome(getUsuarioLogado().getNome());
		getAtividadeDiscursivaVO().setResponsavelCadastro(getUsuarioLogadoClone());
		getAtividadeDiscursivaVO().setNovoObj(true);
		getAtividadeDiscursivaVO().setSemestre(Uteis.getSemestreAtual());
		getAtividadeDiscursivaVO().setAno(Uteis.getAnoDataAtual4Digitos());
		montarListaSelectItemTurmaProfessor();
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaProfessorForm.xhtml");
	}

	public String voltar() {
		atividadeDiscursivaVO = new AtividadeDiscursivaVO();
		montarListaSelectItemTurmaProfessor();
		getControleConsulta().getListaConsulta().clear();
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaProfessorCons.xhtml");
	}

	public String inicializarTelaConsultaAtividadeDiscursivaVisaoProfessor() {
		listarTodasAtividadesDiscursivasNaoRespondidas();
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaProfessorCons.xhtml");
	}

	public String inicializarTelaConsultaAtividadeDiscursivaVisaoAluno() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaAlunoCons.xhtml");
	}
	
	public void inicializarTelaConsultaAtividadeDiscursivaVisaoAlunoPorCalendario() throws Exception {
		FilterFactory ff = (FilterFactory) getControlador("FilterFactory");
		ff.getMapFilter().clear();
		ff.getMapFilter().put("itemsCodigo", new FilterFactory("atividadeDiscursivaVO.codigo", "itemsCodigo", TipoCampoEnum.INTEIRO));
		ff.getMapFilter().get("itemsCodigo").setFiltro((String) context().getExternalContext().getSessionMap().get("ativiadeDiscursiva"));
		context().getExternalContext().getSessionMap().remove("ativiadeDiscursiva");
		context().getExternalContext().getSessionMap().remove("navegarAtividadeDiscursivaPorCalendario");
	}

	public void excluir() {
		try {
			
			executarValidacaoSimulacaoVisaoProfessor();
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().excluir(getAtividadeDiscursivaVO(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			this.novo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravar() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().persistir(getAtividadeDiscursivaVO(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			if (getAtividadeDiscursivaVO().getNovoObj()) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNovaAtividadeDiscursiva(getAtividadeDiscursivaVO(), getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void uploadArquivoAtividadeDiscursivaRespostaAluno(FileUploadEvent uploadEvent) {
		try {
			setAbrirModalConfirmarSoliticaoAvaliacaoProfessor("");
			getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().uploadArquivo(uploadEvent, getAtividadeDiscursivaRespostaAlunoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setAbrirModalConfirmarSoliticaoAvaliacaoProfessor("RichFaces.$('panelConfirmarSoliticaoAvaliacaoProfessor').show()");
			registrarLogAtividadeDiscursivaRespostaAluno("UPLOAD_ARQUIVO_ATIVIDADE_DISCURSIVA", "", getAtividadeDiscursivaRespostaAlunoVO().getArquivo());
		} catch (Exception e) {
			setAbrirModalConfirmarSoliticaoAvaliacaoProfessor("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			registrarLogAtividadeDiscursivaRespostaAluno("ERRO_UPLOAD_ARQUIVO_ATIVIDADE_DISCURSIVA", e.getMessage(), getAtividadeDiscursivaRespostaAlunoVO().getArquivo());
		}
	}

	public String editar() {
		try {
			setAtividadeDiscursivaVO((AtividadeDiscursivaVO) context().getExternalContext().getRequestMap().get("atividadeItens"));
			setAtividadeDiscursivaVO(getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarPorChavePrimaria(getAtividadeDiscursivaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			montarListaDeNotasDaConfiguracaoAcademico();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaProfessorForm.xhtml");

	}

	public String selecionarArquivoApoioParaDownload() {
		try {
			ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("materialApoio");
			arquivoVO.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator +arquivoVO.getPastaBaseArquivo());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("arquivoVO", arquivoVO);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void uploadArquivo(FileUploadEvent upload) {
		try {
			getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().uploadMaterialApoio(upload, getAtividadeDiscursivaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerMaterialDeApoio() {
		try {
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("materialApoio");
			getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().removerMaterialApoio(obj, getAtividadeDiscursivaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAtividadesDiscursivasVisaoAluno() {
		try {
			limparMensagem();
			setControleConsultaOtimizado(new DataModelo());
			if (getFilterAnoSemestre().isEmpty()) {
				setCampoAno("");
				setCampoSemestre("");
			} else {
				setCampoSemestre(getFilterAnoSemestre().contains("/") ? getFilterAnoSemestre().substring(getFilterAnoSemestre().indexOf("/")+1, getFilterAnoSemestre().length()) : "");
				setCampoAno(getFilterAnoSemestre().contains("/") ? getFilterAnoSemestre().substring(0, getFilterAnoSemestre().indexOf("/")) : "");
			}
			
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarAtividadeDiscursivasPorMatriculaOuCodigoMatriculaPeriodoTurmaDisciplina(getVisaoAlunoControle().getMatricula().getMatricula(),
					getVisaoAlunoControle().getAmbienteVisaoAluno() ? 0 : getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO().getCodigo(), 
					getUsuarioLogado(), getCampoAno(), getCampoSemestre()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String inicializarAtividadeDiscursivaAlunoEstudoOnline() {
		try {
			limparMensagem();
			getControleConsultaOtimizado().getListaConsulta().clear();
			if (getFilterAnoSemestre().isEmpty()) {
				setCampoAno("");
				setCampoSemestre("");
			} else {
				setCampoSemestre(getFilterAnoSemestre().contains("/") ? getFilterAnoSemestre().substring(getFilterAnoSemestre().indexOf("/")+1, getFilterAnoSemestre().length()) : "");
				setCampoAno(getFilterAnoSemestre().contains("/") ? getFilterAnoSemestre().substring(0, getFilterAnoSemestre().indexOf("/")) : "");
			}
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarAtividadeDiscursivasPorMatriculaOuCodigoMatriculaPeriodoTurmaDisciplina("", getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getUsuarioLogado(), getCampoAno(), getCampoSemestre()));
			if (getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaAlunoCons.xhtml");
	}
	
	private void montarListaSemestreAno() throws Exception {
		getListaSelectItemAnoSemestre().clear();
		getListaSelectItemAnoSemestre().add(new SelectItem("", ""));
		setFilterAnoSemestre("");
		MatriculaVO matricula = getVisaoAlunoControle().getMatricula();
		if (matricula == null || matricula.getMatricula().equals("")) {
			matricula = getFacadeFactory().getMatriculaFacade()
					.consultarMatriculaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new ConfiguracaoFinanceiroVO(), getUsuarioLogado())
					.stream().filter(m -> m.getSituacao().equals("AT")).findFirst().orElse(null);
		}
		if (matricula != null && !matricula.getMatricula().equals("")) {
			setFilterAnoSemestre(getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(matricula.getMatricula(), getListaSelectItemAnoSemestre()));
		}
	}

	public void gravarInteracao() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().persistirAtividadeDiscursivaInteracaoAlunoProfessor(getAtividadeDiscursivaRespostaAlunoVO(), getAtividadeDiscursivaInteracaoVO(), getConfiguracaoGeralPadraoSistema(), false, true, getUsuarioLogado());
			getControleConsultaInteracao().getListaConsulta().clear();
			getControleConsultaInteracao().setPage(0);
			getControleConsultaInteracao().setPaginaAtual(1);
			getControleConsultaInteracao().setLimitePorPagina(10);
			getControleConsultaInteracao().setOffset(1);
			getControleConsultaInteracao().setTotalRegistrosEncontrados(0);
			getControleConsultaInteracao().setListaConsulta(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarInteracoesPorChaveAtividadeDiscursivaAluno(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getControleConsultaInteracao().getLimitePorPagina(), getControleConsultaInteracao().getOffset(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getControleConsultaInteracao().setTotalRegistrosEncontrados(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarTotalRegistroInteracao(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getUsuarioLogado().getCodigo()));
			setAtividadeDiscursivaInteracaoVO(new AtividadeDiscursivaInteracaoVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void solicitarNovaRespostaAluno() {
		SituacaoRespostaAtividadeDiscursivaEnum situacaoAnt = getAtividadeDiscursivaRespostaAlunoVO().getSituacaoRespostaAtividadeDiscursiva();
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getAtividadeDiscursivaRespostaAlunoVO().setSituacaoRespostaAtividadeDiscursiva(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_NOVA_RESPOSTA);
			getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().persistir(getAtividadeDiscursivaRespostaAlunoVO(), getConfiguracaoGeralPadraoSistema(), false, getUsuarioLogado());
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoSolicitarNovaRespostaAlunoAtividadeDiscursiva(getAtividadeDiscursivaRespostaAlunoVO(), getUsuarioLogado());
			setMensagemID("msg_AtividadeDiscursiva_solicitarNovaResposta", Uteis.SUCESSO);
		} catch (Exception e) {
			getAtividadeDiscursivaRespostaAlunoVO().setSituacaoRespostaAtividadeDiscursiva(situacaoAnt);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void solicitarAvaliacaoProfessor() {
		SituacaoRespostaAtividadeDiscursivaEnum situacaoAnt = getAtividadeDiscursivaRespostaAlunoVO().getSituacaoRespostaAtividadeDiscursiva();
		try {
			executarValidacaoSimulacaoVisaoAluno();
			validarAtividadeDiscursivaRespostaAlunoAptaSolicitarAvaliacaoProfessor();
			if (getGravandoInteracao()) {
				getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().persistirAtividadeDiscursivaInteracaoSolicitandoAvaliacaoProfessor(getAtividadeDiscursivaRespostaAlunoVO(), 
						getAtividadeDiscursivaInteracaoVO(), getConfiguracaoGeralPadraoSistema(), false, getUsuarioLogado());
				setGravandoInteracao(false);
				registrarLogAtividadeDiscursivaRespostaAluno("PERSISTIDO_POR_INTERACAO_SOLICITAR_AVALIACAO_PROFESSOR", "", getAtividadeDiscursivaRespostaAlunoVO().getArquivo());
			} else {
				getAtividadeDiscursivaRespostaAlunoVO().setSituacaoRespostaAtividadeDiscursiva(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_AVALIACAO_PROFESSOR);
				getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().persistir(getAtividadeDiscursivaRespostaAlunoVO(), getConfiguracaoGeralPadraoSistema(), false, getUsuarioLogado());
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoSolicitarAvaliacaoProfessorAtividadeDiscursiva(getAtividadeDiscursivaRespostaAlunoVO(), getUsuarioLogado());
				registrarLogAtividadeDiscursivaRespostaAluno("SOLICITAR_AVALIACAO_PROFESSOR", "", getAtividadeDiscursivaRespostaAlunoVO().getArquivo());
			}
			setMensagemID("msg_AtividadeDiscursiva_solicitarNovaResposta", Uteis.SUCESSO);
		} catch (Exception e) {
			getAtividadeDiscursivaRespostaAlunoVO().setSituacaoRespostaAtividadeDiscursiva(situacaoAnt);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			registrarLogAtividadeDiscursivaRespostaAluno("ERRO_SOLICITAR_AVALIACAO_PROFESSOR", e.getMessage(), getAtividadeDiscursivaRespostaAlunoVO().getArquivo());
		}
	}

	public void avaliarRespostaAluno(){
		try {
			setOncompleteModal("");
			executarValidacaoSimulacaoVisaoAluno();
			validarNotaLancaoAtividadeDiscursiva();
			getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().realizarAvaliarRespostaAlunoAtividadeDiscursiva(getAtividadeDiscursivaRespostaAlunoVO(), getHistorico(), getConfiguracaoGeralPadraoSistema(), false, getUsuarioLogado());
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoAlunoAvaliadoAtividadeDiscursiva(getAtividadeDiscursivaRespostaAlunoVO(), getUsuarioLogado());
			setPermitirInformarTipoNota(false);
			setOncompleteModal("RichFaces.$('panelNotaAluno').hide()");
			setMensagemID("msg_AlunoAvaliadoComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editarAtvidadeDiscursivaRespostaAluno() {
		String retorno = null;
		try {
			setAtividadeDiscursivaInteracaoVO(new AtividadeDiscursivaInteracaoVO());
			setControleConsultaInteracao(new DataModelo());
			getControleConsultaInteracao().setPage(0);
			getControleConsultaInteracao().setPaginaAtual(1);
			getControleConsultaInteracao().setLimitePorPagina(10);
			getControleConsultaInteracao().setOffset(1);
			getControleConsultaInteracao().setTotalRegistrosEncontrados(0);
			if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
				setAtividadeDiscursivaRespostaAlunoVO((AtividadeDiscursivaRespostaAlunoVO) context().getExternalContext().getRequestMap().get("atividadeItens"));
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(getAtividadeDiscursivaRespostaAlunoVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				setAtividadeDiscursivaRespostaAlunoVO((AtividadeDiscursivaRespostaAlunoVO) context().getExternalContext().getRequestMap().get("atividadeAlunoItens"));
			}
			setAtividadeDiscursivaRespostaAlunoVO(getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().realizarCriacaoAtividadeDiscursivaRespostaAluno(getAtividadeDiscursivaRespostaAlunoVO(), getUsuarioLogado()));
			if (getAtividadeDiscursivaRespostaAlunoVO().getCodigo() != 0) {
				getControleConsultaInteracao().setListaConsulta(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarInteracoesPorChaveAtividadeDiscursivaAluno(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getControleConsultaInteracao().getLimitePorPagina(), getControleConsultaInteracao().getOffset(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getControleConsultaInteracao().setTotalRegistrosEncontrados(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarTotalRegistroInteracao(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getUsuarioLogado().getCodigo()));
			} else {
				setControleConsultaInteracao(null);
			}
			String telaAtual = getNomeTelaAtual();
			if (telaAtual.contains("atividadeDiscursivaAlunoEstudoOnlineCons")) {
				retorno = Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaAlunoEstudoOnlineForm.xhtml");
			} else if (telaAtual.contains("atividadeDiscursivaAlunoCons")) {
				retorno = Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaAlunoForm.xhtml");
			} else {
				retorno = Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaProfessorAlunoForm.xhtml");
			}
			if (getUsuarioLogado().getIsApresentarVisaoProfessor() && getVisaoProfessorControle() != null) {
				getVisaoProfessorControle().consultarQtdeInteracaoAtividadeDiscursivaProfessor();
			} else if (getUsuarioLogado().getIsApresentarVisaoAluno() && getVisaoAlunoControle() != null) {
				getVisaoAlunoControle().consultarAlertaInteracoesAtividadeDiscursivaEstudoOnline();
			}
		} catch (Exception e) {
			retorno = "";
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return retorno;
	}

	public String montarDadosAtividadeDiscursivasRespostaAluno() {
		try {
			setAtividadeDiscursivaVO((AtividadeDiscursivaVO) context().getExternalContext().getRequestMap().get("atividadeItens"));
			setAtividadeDiscursivaVO(getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarPorChavePrimaria(getAtividadeDiscursivaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		// return "consultarAlunos";
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaProfessorAlunoCons.xhtml");
	}

	public String voltarTelaConsulta() {
		limparMensagem();
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaProfessorCons.xhtml");
	}

	public String voltarTelaConsultaAlunosProfessor() {
		try {
			setAtividadeDiscursivaVO(getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarPorChavePrimaria(getAtividadeDiscursivaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaProfessorAlunoCons.xhtml");
	}

	public String voltarTelaConsultaVisaoAluno() {
		limparMensagem();
		consultarAtividadesDiscursivasVisaoAluno();
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeDiscursivaAlunoCons.xhtml");
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			getListaSelectItemDisciplinasTurma().clear();
			if (getAtividadeDiscursivaVO().getTurmaVO().getCodigo() != 0) {
				getAtividadeDiscursivaVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorChavePrimariaDadosBasicosTurmaAgrupada(getAtividadeDiscursivaVO().getTurmaVO().getCodigo(), getUsuarioLogado()));
				setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(consultarDisciplinaProfessorTurma(), "codigo", "descricaoParaCombobox", false, false));
				getAtividadeDiscursivaVO().getDisciplinaVO().setCodigo(0);
				montarTurmaDisciplinaVO();
				validarPeriodicidadeTurmaAtividadeDiscursiva(getAtividadeDiscursivaVO());
			} else {
				getAtividadeDiscursivaVO().setAno(Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getAno()) ? getAtividadeDiscursivaVO().getAno() : Uteis.getAno(new Date()));
			}
			if (getAtividadeDiscursivaVO().getVincularNotaEspecifica()) {
				montarListaDeNotasDaConfiguracaoAcademico();
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	/*
	 * public void montarListaDisciplinaTurmaVisaoProfessorCons() { try {
	 * getListaSelectItemDisciplinasTurma().clear(); if
	 * (getAtividadeDiscursivaVO().getTurmaVO().getCodigo() != 0) {
	 * getAtividadeDiscursivaVO().setTurmaVO(getFacadeFactory().getTurmaFacade().
	 * consultaRapidaPorChavePrimariaDadosBasicosTurmaAgrupada(
	 * getAtividadeDiscursivaVO().getTurmaVO().getCodigo(), getUsuarioLogado()));
	 * setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(
	 * consultarDisciplinaProfessorTurma(), "codigo", "nome", false));
	 * getAtividadeDiscursivaVO().getDisciplinaVO().setCodigo(0);
	 * montarTurmaDisciplinaVO(); } } catch (Exception e) {
	 * setListaSelectItemDisciplinasTurma(null); } }
	 */

	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() {
		try {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getAtividadeDiscursivaVO().getTurmaVO().getCodigo(), getAtividadeDiscursivaVO().getSemestre(), getAtividadeDiscursivaVO().getAno(), Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<DisciplinaVO>();
	}

	/*
	 * public void montarListaSelectItemDisciplinasProfessor() { try {
	 * getListaSelectItemDisciplinasTurma().clear(); if
	 * (getAtividadeDiscursivaVO().getPublicoAlvo().equals(
	 * PublicoAlvoAtividadeDiscursivaEnum.ALUNO)) { TipoRequerimentoVO tipo = new
	 * TipoRequerimentoVO();
	 * tipo.setTipo(TiposRequerimento.ATIVIDADE_DISCURSIVA.getValor());
	 * tipo.setCodigo(1); List<DisciplinaVO> disciplinaVOs =
	 * getFacadeFactory().getDisciplinaFacade().
	 * consultarDisciplinaPorMatriculaAptoVincularRequerimento(
	 * getAtividadeDiscursivaVO().getMatriculaPeriodoTurmaDisciplinaVO().
	 * getMatriculaObjetoVO().getMatricula(), getAtividadeDiscursivaVO().getAno(),
	 * getAtividadeDiscursivaVO().getSemestre(), tipo); for (DisciplinaVO
	 * disciplinaVO : disciplinaVOs) { getListaSelectItemDisciplinasTurma().add(new
	 * SelectItem(disciplinaVO.getCodigo(), disciplinaVO.getCodigo() + " - " +
	 * disciplinaVO.getNome())); } } else {
	 * setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(
	 * getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(
	 * getUsuarioLogado().getPessoa().getCodigo(),
	 * getUnidadeEnsinoLogado().getCodigo(), null, null, null, null,
	 * Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo",
	 * "nome", false)); } getAtividadeDiscursivaVO().getDisciplinaVO().setCodigo(0);
	 * montarTurmaDisciplinaVO(); inicializarMensagemVazia(); } catch (Exception e)
	 * { e.printStackTrace(); } }
	 */

	/*
	 * public void montarListaSelectItemTurmaProfessorCons() { List<TurmaVO>
	 * listaTurmas = null; List<Integer> mapAuxiliarSelectItem = new ArrayList();
	 * try { listaTurmas = consultarTurmaPorProfessor();
	 * getListaSelectItemTurma().clear(); getListaSelectItemTurma().add(new
	 * SelectItem(0, "")); for (TurmaVO turmaVO : listaTurmas) { if
	 * (!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())) {
	 * getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(),
	 * turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
	 * mapAuxiliarSelectItem.add(turmaVO.getCodigo());
	 * removerObjetoMemoria(turmaVO); } } inicializarMensagemVazia(); } catch
	 * (Exception e) { getListaSelectItemTurma().clear(); } finally {
	 * Uteis.liberarListaMemoria(listaTurmas); mapAuxiliarSelectItem = null; } }
	 */

	public void montarListaSelectItemTurmaProfessor() {
		List<TurmaVO> listaTurmas = null;
		List<Integer> mapAuxiliarSelectItem = new ArrayList<>();
		try {
			if (!Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getAno()) || !Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getSemestre())) {
				getListaSelectItemTurma().clear();
				getListaSelectItemDisciplinasTurma().clear();
				return;
			}
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemDisciplinasTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : listaTurmas) {
				if (!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())) {
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turmaVO.getCodigo());
					removerObjetoMemoria(turmaVO);
				}
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			mapAuxiliarSelectItem = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() {
		try {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getAtividadeDiscursivaVO().getSemestre(), getAtividadeDiscursivaVO().getAno(), getBuscarTurmasAnteriores(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<TurmaVO>();
	}

	public void montarTurmaDisciplinaVO() {
		try {
			if (!getListaSelectItemDisciplinasTurma().isEmpty() && !Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getDisciplinaVO().getCodigo())) {
				SelectItem selectItem = getListaSelectItemDisciplinasTurma().get(0);
				getAtividadeDiscursivaVO().getDisciplinaVO().setCodigo((Integer) selectItem.getValue());
			}
			if (Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getTurmaVO().getCodigo()) && Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getDisciplinaVO().getCodigo())) {
				getAtividadeDiscursivaVO().setTurmaDisciplinaDefinicoesTutoriaOnlineEnum(getFacadeFactory().getTurmaDisciplinaFacade().consultarDefinicoesTutoriaOnlineTurmaDisciplina(getAtividadeDiscursivaVO().getTurmaVO().getCodigo(), getAtividadeDiscursivaVO().getDisciplinaVO().getCodigo()));
				getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().realizarVinculoMatriculaPeriodoTurmaDisciplinaPorAtividadeDiscursiva(getAtividadeDiscursivaVO(), getUsuarioLogado());

			}
			if (getAtividadeDiscursivaVO().getVincularNotaEspecifica()) {
				montarListaDeNotasDaConfiguracaoAcademico();
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String consultar() {
		try {
			if (!Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getTurmaVO().getCodigo())) {
				throw new Exception("O campo Turma deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getDisciplinaVO().getCodigo())) {
				throw new Exception("O campo Disciplina deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getAno()) && getAtividadeDiscursivaVO().getIsApresentarAnoVisaoProfessor()) {
				throw new Exception("O campo Ano deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(getAtividadeDiscursivaVO().getSemestre()) && getAtividadeDiscursivaVO().getIsApresentarSemestreVisaoProfessor()) {
				throw new Exception("O campo Semestre deve ser informado.");
			}
			getControleConsulta().setListaConsulta(getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarDadosTelaConsulta(getAtividadeDiscursivaVO().getTurmaVO().getCodigo(), getAtividadeDiscursivaVO().getDisciplinaVO().getCodigo(), getAtividadeDiscursivaVO().getAno(), getAtividadeDiscursivaVO().getSemestre(), getUsuarioLogado()));
			if (getControleConsulta().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void scrollListenerInteracao(DataScrollEvent DataScrollEvent) {
		getControleConsultaInteracao().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaInteracao().setPage(DataScrollEvent.getPage());
		try {
			getControleConsultaInteracao().setListaConsulta(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarInteracoesPorChaveAtividadeDiscursivaAluno(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getControleConsultaInteracao().getLimitePorPagina(), getControleConsultaInteracao().getOffset(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getControleConsultaInteracao().setTotalRegistrosEncontrados(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarTotalRegistroInteracao(getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getUsuarioLogado().getCodigo()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void validarAvaliacaoRespostaAluno() {
		try {

			setHistorico(getFacadeFactory().getHistoricoFacade().consultarHistoricoParaRegistroLancamentoNotaHistoricoAutomatico(getAtividadeDiscursivaRespostaAlunoVO().getMatriculaPeriodoTurmaDisciplinaVO(), getUsuarioLogado()));
			if (!getAtividadeDiscursivaRespostaAlunoVO().getSituacaoRespostaAtividadeDiscursiva().equals(SituacaoRespostaAtividadeDiscursivaEnum.AVALIADO)) {
				if (getAtividadeDiscursivaVO().getVincularNotaEspecifica()) {
					getAtividadeDiscursivaRespostaAlunoVO().setTipoNota(getHistorico().getConfiguracaoAcademico().getNumeroNotaPorVariavel(getAtividadeDiscursivaVO().getNotaCorrespondente()));
				} else {
					getAtividadeDiscursivaRespostaAlunoVO().setTipoNota(getHistorico().getConfiguracaoAcademico().getNumeroNotaPorVariavel(getAtividadeDiscursivaVO().getTurmaVO().getConfiguracaoEADVO().getVariavelNotaCfgPadraoAtividadeDiscursiva()));
				}
			}
			setPermitirInformarTipoNota(getAtividadeDiscursivaRespostaAlunoVO().getTipoNota() == null);
			montarListaSelectItemNota();
			if (getAtividadeDiscursivaRespostaAlunoVO().getTipoNota() != null) {
				selecionarTipoNota();
			}
			if (Uteis.isAtributoPreenchido(getListaSelectItemNota()) && getListaSelectItemNota().size() == 1) {
				setMensagemAlertaConfiguracaoNota(true);
			}else {
				setMensagemAlertaConfiguracaoNota(false);
			}
			setMensagemID("");
			setMensagemDetalhada("");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void validarNotaAvaliarRespostaAluno(){
		try {
			if (getAtividadeDiscursivaVO().getVincularNotaEspecifica()) {
				if (getAtividadeDiscursivaRespostaAlunoVO().getTipoNota() != null) {
					UtilReflexao.invocarMetodo(getHistorico(), "setNota" + getAtividadeDiscursivaRespostaAlunoVO().getTipoNota().getNumeroNota(), getAtividadeDiscursivaRespostaAlunoVO().getNota());
					getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(getHistorico(), null, getHistorico().getConfiguracaoAcademico(), getAtividadeDiscursivaRespostaAlunoVO().getTipoNota().getNumeroNota());
					limparMensagem();
				} else {
					throw new Exception(UteisJSF.internacionalizar("msg_Erro_LancarNotaHistorico"));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			getAtividadeDiscursivaRespostaAlunoVO().setNota(null);
		}
	}

	// Getters and Setters

	public String getCaminhoBaseFoto() {
		try {
			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo();
		} catch (Exception e) {
			return "./resources/imagens/visao/foto_usuario.png";
		}
	}

	public boolean getIsApresentarCampoNotaCorrespondente() {
		return getAtividadeDiscursivaVO().getVincularNotaEspecifica();
	}

	public AtividadeDiscursivaVO getAtividadeDiscursivaVO() {
		if (atividadeDiscursivaVO == null) {
			atividadeDiscursivaVO = new AtividadeDiscursivaVO();
		}
		return atividadeDiscursivaVO;
	}

	public void setAtividadeDiscursivaVO(AtividadeDiscursivaVO atividadeDiscursivaVO) {
		this.atividadeDiscursivaVO = atividadeDiscursivaVO;
	}

	public List<SelectItem> getListaSelectItemDisciplinasTurma() {
		if (listaSelectItemDisciplinasTurma == null) {
			listaSelectItemDisciplinasTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasTurma;
	}

	public void setListaSelectItemDisciplinasTurma(List<SelectItem> listaSelectItemDisciplinasTurma) {
		this.listaSelectItemDisciplinasTurma = listaSelectItemDisciplinasTurma;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public Boolean getBuscarTurmasAnteriores() {
		if (buscarTurmasAnteriores == null) {
			buscarTurmasAnteriores = false;
		}
		return buscarTurmasAnteriores;
	}

	public void setBuscarTurmasAnteriores(Boolean buscarTurmasAnteriores) {
		this.buscarTurmasAnteriores = buscarTurmasAnteriores;
	}

	public AtividadeDiscursivaRespostaAlunoVO getAtividadeDiscursivaRespostaAlunoVO() {
		if (atividadeDiscursivaRespostaAlunoVO == null) {
			atividadeDiscursivaRespostaAlunoVO = new AtividadeDiscursivaRespostaAlunoVO();
		}
		return atividadeDiscursivaRespostaAlunoVO;
	}

	public void setAtividadeDiscursivaRespostaAlunoVO(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO) {
		this.atividadeDiscursivaRespostaAlunoVO = atividadeDiscursivaRespostaAlunoVO;
	}

	public AtividadeDiscursivaInteracaoVO getAtividadeDiscursivaInteracaoVO() {
		if (atividadeDiscursivaInteracaoVO == null) {
			atividadeDiscursivaInteracaoVO = new AtividadeDiscursivaInteracaoVO();
		}
		return atividadeDiscursivaInteracaoVO;
	}

	public void setAtividadeDiscursivaInteracaoVO(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO) {
		this.atividadeDiscursivaInteracaoVO = atividadeDiscursivaInteracaoVO;
	}

	public DataModelo getControleConsultaInteracao() {
		if (controleConsultaInteracao == null) {
			controleConsultaInteracao = new DataModelo();
		}
		return controleConsultaInteracao;
	}

	public void setControleConsultaInteracao(DataModelo controleConsultaInteracao) {
		this.controleConsultaInteracao = controleConsultaInteracao;
	}

	public HistoricoVO getHistorico() {

		return historico;
	}

	public void setHistorico(HistoricoVO historico) {
		this.historico = historico;
	}

	public String getExecutarDownloadArquivo() {
		try {
			context().getExternalContext().getSessionMap().put("nomeArquivo", getAtividadeDiscursivaRespostaAlunoVO().getArquivo());
			context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getAtividadeDiscursivaRespostaAlunoVO().getPastaBaseArquivo().getValue());
			context().getExternalContext().getSessionMap().put("nomeReal", extractFileName(getAtividadeDiscursivaRespostaAlunoVO().getNomeArquivoApresentar()));
			return "location.href='../DownloadSV'";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String extractFileName(String filePathName) {
		if (filePathName == null)
			return null;

		int dotPos = filePathName.lastIndexOf('.');
		int slashPos = filePathName.lastIndexOf('\\');
		if (slashPos == -1)
			slashPos = filePathName.lastIndexOf('/');

		if (dotPos > slashPos) {
			return filePathName.substring(slashPos > 0 ? slashPos + 1 : 0);
		}

		return filePathName.substring(slashPos > 0 ? slashPos + 1 : 0);
	}

	public List<SelectItem> getCampoSemestreTurma() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));

		return objs;
	}

	public List<SelectItem> getListaSelectItemArtefatoEntrega() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(ArtefatoEntregaEnum.class, "name", "valorApresentar", false);
	}

	public void inicializarAtividadeDiscursivaPorRequerimento() {
		if (context().getExternalContext().getSessionMap().containsKey("requerimentoAtividadeDiscursiva") && context().getExternalContext().getSessionMap().get("requerimentoAtividadeDiscursiva") != null && context().getExternalContext().getSessionMap().get("requerimentoAtividadeDiscursiva") instanceof RequerimentoVO) {
			RequerimentoVO requerimentoVO = (RequerimentoVO) context().getExternalContext().getSessionMap().remove("requerimentoAtividadeDiscursiva");
			try {
				setAtividadeDiscursivaVO(new AtividadeDiscursivaVO());
				TipoRequerimentoVO tipo = new TipoRequerimentoVO();
				tipo.setTipo(TiposRequerimento.ATIVIDADE_DISCURSIVA.getValor());
				tipo.setCodigo(1);
				List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorMatriculaAptoVincularRequerimento(getAtividadeDiscursivaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula(), getAtividadeDiscursivaVO().getAno(), getAtividadeDiscursivaVO().getSemestre(), tipo, null);
				for (DisciplinaVO disciplinaVO : disciplinaVOs) {
					getListaSelectItemDisciplinasTurma().add(new SelectItem(disciplinaVO.getCodigo(), disciplinaVO.getCodigo() + " - " + disciplinaVO.getNome()));
				}
				getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().realizarCriacaoAtividadeDiscursivaPorRequerimento(getAtividadeDiscursivaVO(), requerimentoVO, getUsuarioLogado());
				getAtividadeDiscursivaVO().getResponsavelCadastro().setCodigo(getUsuarioLogado().getCodigo());
				getAtividadeDiscursivaVO().getResponsavelCadastro().setNome(getUsuarioLogado().getNome());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
			}
		}
	}

	public void alterarPublicoAlvo() {
		getAtividadeDiscursivaVO().setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
	}

	/**
	 * @return the listaSelectItemPublicoAlvo
	 */
	public List<SelectItem> getListaSelectItemPublicoAlvo() {
		if (listaSelectItemPublicoAlvo == null) {
			listaSelectItemPublicoAlvo = new ArrayList<SelectItem>(0);
			listaSelectItemPublicoAlvo = UtilSelectItem.getListaSelectItemEnum(PublicoAlvoAtividadeDiscursivaEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemPublicoAlvo;
	}

	/**
	 * @param listaSelectItemPublicoAlvo
	 *            the listaSelectItemPublicoAlvo to set
	 */
	public void setListaSelectItemPublicoAlvo(List<SelectItem> listaSelectItemPublicoAlvo) {
		this.listaSelectItemPublicoAlvo = listaSelectItemPublicoAlvo;
	}

	public void consultarAluno() {
		try {
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getListaConsultaAluno().isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados", Uteis.ALERTA);
			} else {
				setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			}
		} catch (Exception e) {
			getListaConsultaAluno().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarAluno() {
		try {
			setMensagemDetalhada("");
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			getFacadeFactory().getMatriculaFacade().carregarDados(obj, NivelMontarDados.BASICO, getUsuarioLogado());
			inicializarDadosMatriculaAtividadeDiscursiva(obj);

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Matricula</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca pela
	 * chave primária da entidade montando automaticamente o resultado da consulta
	 * para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		try {

			if (!getAtividadeDiscursivaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula().trim().isEmpty()) {
				MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getAtividadeDiscursivaVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				inicializarDadosMatriculaAtividadeDiscursiva(matricula);
			} else {
				getAtividadeDiscursivaVO().setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
				getListaSelectItemDisciplinasTurma().clear();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosMatriculaAtividadeDiscursiva(MatriculaVO matriculaVO) throws Exception {
		if (matriculaVO.getMatricula().trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		} else {
			getAtividadeDiscursivaVO().setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			getAtividadeDiscursivaVO().getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaObjetoVO(matriculaVO);
			getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().realizarVinculoMatriculaPeriodoTurmaDisciplinaPorAtividadeDiscursiva(getAtividadeDiscursivaVO(), getUsuarioLogado());
		}
	}

	public void limparDadosMatricula() {
		getAtividadeDiscursivaVO().setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
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

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}

	/**
	 * @return the listaSelectItemNota
	 */
	public List<SelectItem> getListaSelectItemNota() {
		if (listaSelectItemNota == null) {
			listaSelectItemNota = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota;
	}

	/**
	 * @param listaSelectItemNota
	 *            the listaSelectItemNota to set
	 */
	public void setListaSelectItemNota(List<SelectItem> listaSelectItemNota) {
		this.listaSelectItemNota = listaSelectItemNota;
	}

	void montarListaSelectItemNota() throws Exception {
		getListaSelectItemNota().clear();
		getListaSelectItemNota().add(new SelectItem(null, ""));
		if (Uteis.isAtributoPreenchido(getHistorico().getCodigo()) && Uteis.isAtributoPreenchido(getHistorico().getConfiguracaoAcademico().getCodigo())) {
			List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs = getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarPorConfiguracaoAcademicoNotaPermiteLancamentoNota(getHistorico().getConfiguracaoAcademico().getCodigo(), false, TipoUsoNotaEnum.ATIVIDADE_DISCURSIVA);
			for (ConfiguracaoAcademicaNotaVO conf : configuracaoAcademicaNotaVOs) {
				getListaSelectItemNota().add(new SelectItem(conf.getNota(), conf.getTitulo() + (conf.getAgrupamentoNota().equals(BimestreEnum.NAO_CONTROLA) || conf.getAgrupamentoNota().equals(BimestreEnum.RESUMO_FINAL) ? "" : " - " + conf.getAgrupamentoNota().getValorResumidoApresentar())));
			}
		}
	}

	/**
	 * @return the permitirInformarTipoNota
	 */
	public Boolean getPermitirInformarTipoNota() {
		if (permitirInformarTipoNota == null) {
			permitirInformarTipoNota = false;
		}
		return permitirInformarTipoNota;
	}

	/**
	 * @param permitirInformarTipoNota
	 *            the permitirInformarTipoNota to set
	 */
	public void setPermitirInformarTipoNota(Boolean permitirInformarTipoNota) {
		this.permitirInformarTipoNota = permitirInformarTipoNota;
	}

	public void selecionarTipoNota() {
		try {
			getListaSelectItemNotaConceito().clear();
			getAtividadeDiscursivaRespostaAlunoVO().setUtilizaNotaConceito(false);
			getAtividadeDiscursivaRespostaAlunoVO().setNotaConceito(null);
			List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicaNotaConceitoVOs = null;
			if (Uteis.isAtributoPreenchido(getHistorico().getConfiguracaoAcademico().getCodigo()) && Uteis.isAtributoPreenchido(getAtividadeDiscursivaRespostaAlunoVO().getTipoNota())) {
				configuracaoAcademicaNotaConceitoVOs = getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorConfiguracaoAcademicoTipoNota(getHistorico().getConfiguracaoAcademico().getCodigo(), getAtividadeDiscursivaRespostaAlunoVO().getTipoNota());
			}
			if (Uteis.isAtributoPreenchido(configuracaoAcademicaNotaConceitoVOs)) {
				getAtividadeDiscursivaRespostaAlunoVO().setUtilizaNotaConceito(true);
				getAtividadeDiscursivaRespostaAlunoVO().setNotaConceito(null);
				for (ConfiguracaoAcademicoNotaConceitoVO confCon : configuracaoAcademicaNotaConceitoVOs) {
					getListaSelectItemNotaConceito().add(new SelectItem(confCon.getCodigo(), confCon.getConceitoNota()));
					if (!Uteis.isAtributoPreenchido(getAtividadeDiscursivaRespostaAlunoVO().getNotaConceito())) {
						getAtividadeDiscursivaRespostaAlunoVO().setNotaConceito(confCon);
					}
				}
				Uteis.liberarListaMemoria(configuracaoAcademicaNotaConceitoVOs);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	/**
	 * @return the listaSelectItemNotaConceito
	 */
	public List<SelectItem> getListaSelectItemNotaConceito() {
		if (listaSelectItemNotaConceito == null) {
			listaSelectItemNotaConceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNotaConceito;
	}

	/**
	 * @param listaSelectItemNotaConceito
	 *            the listaSelectItemNotaConceito to set
	 */
	public void setListaSelectItemNotaConceito(List<SelectItem> listaSelectItemNotaConceito) {
		this.listaSelectItemNotaConceito = listaSelectItemNotaConceito;
	}

	public void listarTodasAtividadesDiscursivasNaoRespondidas() {
		try {
			getControleConsulta().setListaConsulta(getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarInteracoesNaoLidasAlunosPorCodigoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isNavegarProfessorAtividadeDiscursivaMonitoramentoEad() {
		return navegarProfessorAtividadeDiscursivaMonitoramentoEad;
	}

	public void setNavegarProfessorAtividadeDiscursivaMonitoramentoEad(boolean navegarProfessorAtividadeDiscursivaMonitoramentoEad) {
		this.navegarProfessorAtividadeDiscursivaMonitoramentoEad = navegarProfessorAtividadeDiscursivaMonitoramentoEad;
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (!getAtividadeDiscursivaVO().getTurmaVO().getCodigo().equals(0)) {
				if (getAtividadeDiscursivaVO().getTurmaVO().getSemestral()) {
					return true;
				} else if (getAtividadeDiscursivaVO().getTurmaVO().getAnual()) {
					return true;
				} else {
					getAtividadeDiscursivaVO().setAno("");
					return false;
				}
			}
			return true;

		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {

			if (!getAtividadeDiscursivaVO().getTurmaVO().getCodigo().equals(0)) {
				if (getAtividadeDiscursivaVO().getTurmaVO().getSemestral()) {
					return true;
				} else {
					getAtividadeDiscursivaVO().setSemestre("");
					return false;
				}
			}
			return true;
		}
		return true;
	}

	private void validarPeriodicidadeTurmaAtividadeDiscursiva(AtividadeDiscursivaVO atividadeDiscursivaVO) {
		if (atividadeDiscursivaVO.getTurmaVO().getAnual()) {
			atividadeDiscursivaVO.setSemestre("");
		} else if (!atividadeDiscursivaVO.getTurmaVO().getAnual() && !atividadeDiscursivaVO.getTurmaVO().getSemestral()) {
			atividadeDiscursivaVO.setAno("");
			atividadeDiscursivaVO.setSemestre("");
		}
	}
	
	public String getCampoSemestre() {
		if (campoSemestre == null) {
			campoSemestre = "";
		}
		return campoSemestre;
	}

	public void setCampoSemestre(String campoSemestre) {
		this.campoSemestre = campoSemestre;
	}

	public String getCampoAno() {
		if (campoAno == null) {
			campoAno = "";
		}
		return campoAno;
	}

	public void setCampoAno(String campoAno) {
		this.campoAno = campoAno;
	}
	
	public String getFilterAnoSemestre() {
		if (filterAnoSemestre == null) {
			filterAnoSemestre = "";
		}
		return filterAnoSemestre;
	}

	public void setFilterAnoSemestre(String filterAnoSemestre) {
		this.filterAnoSemestre = filterAnoSemestre;
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
	
	public void montarListaDeNotasDaConfiguracaoAcademico() {
		try {
			getListaSelectItemVariavelNotaCfaVOs().clear();
			getListaSelectItemVariavelNotaCfaVOs().addAll(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarVariavelTituloConfiguracaoAcademicoPorTipoUsoNota(TipoUsoNotaEnum.ATIVIDADE_DISCURSIVA, getUsuarioLogado()));
			validarDadosExistenciaVariavelConfiguracao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarDadosExistenciaVariavelConfiguracao() throws Exception {
		boolean encontrou = false;
		for (SelectItem variavel : getListaSelectItemVariavelNotaCfaVOs()) {
			if (getAtividadeDiscursivaVO().getNotaCorrespondente().equals(variavel.getValue())) {
				encontrou = true;
				break;
			}
		}
		if (!encontrou) {
			throw new Exception("Não foi encontrado a Nota Correspondente do tipo Atividade Discursiva na Configuração Acadêmica, favor escolher outra Nota Correspondente.");
		}
	}

	public List<SelectItem> getListaSelectItemVariavelNotaCfaVOs() {
		if (listaSelectItemVariavelNotaCfaVOs == null) {
			listaSelectItemVariavelNotaCfaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemVariavelNotaCfaVOs;
	}

	public void setListaSelectItemVariavelNotaCfaVOs(List<SelectItem> listaSelectItemVariavelNotaCfaVOs) {
		this.listaSelectItemVariavelNotaCfaVOs = listaSelectItemVariavelNotaCfaVOs;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if(matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
			
	public List<SelectItem> getCampoDefinicaoDataInicioFim() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(DefinicaoDataEntregaAtividadeDiscursivaEnum.class, "name", "valorApresentar", false);
	}

	
	public void validarNotaLancaoAtividadeDiscursiva() throws Exception{
		if (getAtividadeDiscursivaVO().getVincularNotaEspecifica() && !Uteis.isAtributoPreenchido(getAtividadeDiscursivaRespostaAlunoVO().getTipoNota())) {
			throw new Exception("O Campo Tipo Nota tem que ser informado.");
		}
		if (getAtividadeDiscursivaRespostaAlunoVO().getUtilizaNotaConceito()) {
			if (getAtividadeDiscursivaRespostaAlunoVO().getTipoNota() != null && getAtividadeDiscursivaRespostaAlunoVO().getNotaConceito().getCodigo() == null) {
				throw new Exception("O Campo Nota tem que ser informado."); 
			}
		}else {
			if (getAtividadeDiscursivaRespostaAlunoVO().getTipoNota() != null && getAtividadeDiscursivaRespostaAlunoVO().getNota() == null) {
				throw new Exception("O Campo Nota tem que ser informado."); 
			}
		}
	}
	
	public Boolean getValidarAtividadeDiscursivaRespondida() {
		try {
			return getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarAtividadeDiscursivaRespondida(getAtividadeDiscursivaVO().getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}
	}

	public Boolean getMensagemAlertaConfiguracaoNota() {
		if (mensagemAlertaConfiguracaoNota == null) {
			mensagemAlertaConfiguracaoNota = false;
		}
		return mensagemAlertaConfiguracaoNota;
	}

	public void setMensagemAlertaConfiguracaoNota(Boolean mensagemAlertaConfiguracaoNota) {
		this.mensagemAlertaConfiguracaoNota = mensagemAlertaConfiguracaoNota;
	}

	public String getAbrirModalConfirmarSoliticaoAvaliacaoProfessor() {
		if (abrirModalConfirmarSoliticaoAvaliacaoProfessor == null) {
			abrirModalConfirmarSoliticaoAvaliacaoProfessor = "";
		}
		return abrirModalConfirmarSoliticaoAvaliacaoProfessor;
	}

	public void setAbrirModalConfirmarSoliticaoAvaliacaoProfessor(String abrirModalConfirmarSoliticaoAvaliacaoProfessor) {
		this.abrirModalConfirmarSoliticaoAvaliacaoProfessor = abrirModalConfirmarSoliticaoAvaliacaoProfessor;
	}
	
	private void limparArquivoUploadAtividadeDiscursiva() {
		String nomeArquivo = getAtividadeDiscursivaRespostaAlunoVO().getArquivo();
		try {
			getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().removerArquivo(getAtividadeDiscursivaRespostaAlunoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			registrarLogAtividadeDiscursivaRespostaAluno("REMOVER_ARQUIVO_UPLOAD", "", nomeArquivo);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			registrarLogAtividadeDiscursivaRespostaAluno("ERRO_REMOVER_ARQUIVO_UPLOAD", e.getMessage(), nomeArquivo);
		}
	}
	
	private void validarAtividadeDiscursivaRespostaAlunoAptaSolicitarAvaliacaoProfessor() throws ConsistirException {
		String resposta = getAtividadeDiscursivaRespostaAlunoVO().getResposta().replaceAll("(\r\n|\n\r|\r|\n)", "").replace(" ", "");
		if (getAtividadeDiscursivaRespostaAlunoVO().getAtividadeDiscursivaVO().getArtefatoEntrega().equals(ArtefatoEntregaEnum.TEXTUAL) && resposta.equals("")) {
			getAtividadeDiscursivaRespostaAlunoVO().setResposta("");
			throw new ConsistirException("É obrigatório informar a resposta.");
		} else if (getAtividadeDiscursivaRespostaAlunoVO().getAtividadeDiscursivaVO().getArtefatoEntrega().equals(ArtefatoEntregaEnum.UPLOAD_ARQUIVO)
				&& getAtividadeDiscursivaRespostaAlunoVO().getArquivo().equals("")) {
			throw new ConsistirException("É obrigatório o upload do arquivo com a resposta.");
		}
	}
	
	private void registrarLogAtividadeDiscursivaRespostaAluno(String acao, String erro, String arquivo) {
		getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().registrarLogAtividadeDiscursivaRespostaAluno(getAtividadeDiscursivaRespostaAlunoVO().getAtividadeDiscursivaVO().getCodigo(), 
				getAtividadeDiscursivaRespostaAlunoVO().getCodigo(), getAtividadeDiscursivaRespostaAlunoVO().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), acao, new Date(), erro, 
				getAtividadeDiscursivaRespostaAlunoVO().getSituacaoRespostaAtividadeDiscursiva(), getAtividadeDiscursivaRespostaAlunoVO().getAtividadeDiscursivaVO().getArtefatoEntrega(), arquivo, getUsuarioLogado().getCodigo());
	}
	
	public void realizarVerificacaoGravarSoliticarAvaliacaoProfessor() {
		setAbrirModalConfirmarSoliticaoAvaliacaoProfessor("");
		setGravandoInteracao(false);
		if (getAtividadeDiscursivaRespostaAlunoVO().getIsAptaSolicitarAvaliacaoProfessor()) {
			setGravandoInteracao(true);
			setAbrirModalConfirmarSoliticaoAvaliacaoProfessor("RichFaces.$('panelConfirmarSoliticaoAvaliacaoProfessor').show()");
		} else {
			gravarInteracao();
		}
	}
	
	public void cancelarSolicitacaoAvaliacaoProfessor() {
		if (getGravandoInteracao()) {
			gravarInteracao();
			setGravandoInteracao(false);
		} else {
			if (getAtividadeDiscursivaRespostaAlunoVO().getAtividadeDiscursivaVO().getIsApresentarArtefatoEntregaUploadArquivo()) {
				limparArquivoUploadAtividadeDiscursiva();
			}
		}
	}

	public Boolean getGravandoInteracao() {
		if (gravandoInteracao == null) {
			gravandoInteracao = false;
		}
		return gravandoInteracao;
	}

	public void setGravandoInteracao(Boolean gravandoInteracao) {
		this.gravandoInteracao = gravandoInteracao;
	}
}
