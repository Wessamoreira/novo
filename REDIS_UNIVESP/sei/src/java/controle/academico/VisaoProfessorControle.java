package controle.academico;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import controle.arquitetura.LoginControle;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.AdvertenciaVO;
import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinasInteresseVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlineVO;
import negocio.comuns.academico.QuadroHorarioVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoProfessorEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.TutoriaOnlineSalaAulaIntegracaoVO;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.gsuite.GoogleMeetConvidadoVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.facade.jdbc.academico.HorarioProfessorDia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.DiarioRelControle;
import relatorio.controle.academico.DownloadRelControle;
import relatorio.controle.academico.EspelhoRelControle;
import relatorio.controle.academico.HistoricoTurmaRelControle;
import relatorio.controle.academico.PerfilTurmaRelControle;
import relatorio.controle.academico.RegistroAulaLancadaNaoLancadaRelControle;
import webservice.servicos.AplicativoSEISV;
import webservice.servicos.QRCodeLoginRSVO;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.DataEventosRSVO;

@Controller("VisaoProfessorControle")
@Scope("session")
@Lazy
public class VisaoProfessorControle extends SuperControle implements Serializable {

	private String login;
	private String loginAnterior;
	private String senha;
	private String senhaAnterior;
	private String backgroundFoto;
	private String backgroundTopo;
	private String backgroundTopoBack;
	private String backgroundFundo;
	private String backgroundMenu;
	private String backgroundHeader;
	private Boolean uploadArquivosProfessor;
	private Boolean ataProva;
	private Boolean menuRecado;
	private Boolean menuConfiguracao;
	private Boolean menuDadosPessoais;
	private Boolean menuMeusHorarios;
	private Boolean menuRegistroAula;
	private Boolean menuRegistroNota;
	private Boolean menuRegistroAulaNota;
	private Boolean menuDisciplina;
	private Boolean menuRequisicao;
	private Boolean menuRelatorio;
	private Boolean menuDiario;
	private Boolean menuEspelhoDiario;
	private Boolean menuHistoricoAluno;
	private Boolean menuHitoricoTurma;
	private Boolean menuDownload;
	private Boolean menuPerfilTurma;
	private Boolean menuSenha;
	private Boolean configuracao;
	private Boolean novoRegistroAula;
	private Boolean consultarRegistroAula;
	private Boolean disciplinaEditar;
	private Boolean disciplinaConsultar;
	protected Boolean requisicaoEditar;
	protected Boolean requisicaoConsultar;
	private Boolean horarioSimples;
	private Boolean horarioDetalhado;
	protected Boolean submeterPagina = true;
	private Boolean novoComunicado;
	private Boolean lerComunicado;
	private Boolean possuiPermissaoExcluirRegistroAula;
	private Boolean permitirAlterarUsername;
	// Atributos usados para os dados Pessoais
	protected List listaSelectItemCidade;
	protected List listaSelectItemAreaConhecimento;
	private FormacaoAcademicaVO formacaoAcademicaVO;
	private PessoaVO pessoaVO;
	protected List listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	protected List listaConsultaCidade;
	private String campoConsultaCidade;
	private String valorConsultaCidade;
	private List listaSelectItemTurma;
	private List listaSelectItemDisciplinasTurma;
	private String valorCssTopoLogo;
	private String valorCssBackground;
	private String valorCssMenu;
	private QuadroHorarioVO quadroHorarioVO;
	private Integer codigoTurno;
	private List listaSelectItemTurno;
	private Boolean permissaoAlterarSenha;
	private Boolean permissaoAlterarFoto;
	private Boolean permissaoAlterarCorTela;
	private PermissaoAcessoMenuVO permissaoAcessoMenuVO;
	private Boolean apresentarIconeRelatorio;
	private Integer qtdeAtualizacaoForum;
	private Integer qtdeAtualizacaoDuvidaProfessor;
	private Boolean apresentarAdicionarFormacao;
	private Boolean apresentarMenuTcc;
	private List listaSelectItemTurmaProfessor;
	private Boolean buscarTurmasAnteriores;
	private TurmaVO turmaVO;
	private String campoDisciplinaTurma;
	private String ano;
	private String semestre;
	private DisciplinaVO disciplinaVO;
	private List<AdvertenciaVO> advertenciaVOs;
	private AdvertenciaVO advertenciaVO;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO;
	private Integer qtdeInteracoesAlunosAtividadeDiscursiva;
	private Integer qtdeAtualizacaoRequerimentoProfessor;

	private Boolean apresentarModalResetarSenha;
	private boolean alterarSenhaContaGsuite = false;
	private boolean existeConfiguracaoSeiGsuite = false;
	private boolean existeConfiguracaoSeiBlackboard = false;
	
	private boolean omitirQrCode;

	private DashboardVO dashboardCalendario;
	private DashboardVO dashboardTutoriaOnline;	
	private DashboardVO dashboardPendencia;
	private Boolean menuRelatorioAulasRegistradasNaoRegistradas;

	public VisaoProfessorControle() {

	}

	@PostConstruct
	public void init() {
		try {
			incializarDadosPessoa();
			verificarPossuiTCCVinculado();
			verificarPossuiPermissaoExcluirRegistroAula();
			inicializarDadosFotoUsuario();
//			inicializarBanner();
			realizarInicializacaoCalendarioProfessor();
			realizarInicializacaoTutoriaOnline();
			verificarPrimeiroAcesso();
			consultarConfiguracaoDashboard();
			consultarQtdeInteracaoAtividadeDiscursivaProfessor();
			consultarQtdeAtualizacaoRequerimentoProfessor();
			inicializarQtdeCaixaEntradaUsuario();
			executarMetodoControle("ComunicacaoInternaControle", "consultarTodasEntradaMarketingLeituraObrigatoriaAlunoProfessorCoordenador");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarAtualizacaoTelaInicialVisaoProfessorPorAnoSemestre() {
		try {
			consultarTutoriaOnlinePorIntegracao();
			setModalCalendario("");
		} catch (Exception e) {
			setModalCalendario("RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String inicializarMenuEmprestimosEmAberto() {
		setConfiguracao(Boolean.FALSE);
		setMenuSenha(Boolean.TRUE);
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("emprestimosProfessor.xhtml");
	}

	public String inicializarMenuReservaPatrimonio() {
		setConfiguracao(Boolean.FALSE);
		setMenuSenha(Boolean.TRUE);
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("ocorrenciaPatrimonioProfessorCons.xhtml");
	}

	public String inicializarMenuBiblioteca() {
		setConfiguracao(Boolean.FALSE);
		setMenuSenha(Boolean.TRUE);
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("buscaBibliotecaProfessor.xhtml");
	}

	public void incializarDadosPessoa() {
		try {
			setPessoaVO(getUsuarioLogado().getPessoa());
		} catch (Exception e) {
			setPessoaVO(null);
		}
	}

	public void alterarSenha() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			Uteis.validarSenha(getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null), getSenha());
			getFacadeFactory().getUsuarioFacade().alterarSenha( getUsuarioLogado(), getLoginAnterior(),
					getSenhaAnterior(), getLogin(), getSenha(), isAlterarSenhaContaGsuite());
			getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, getUsuarioLogado(), getSenha());
			setMensagemID("msg_dados_gravados");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarCapturarFotoWebCam() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			String arquivoFoto = getFacadeFactory().getArquivoHelper().getArquivoUploadFoto(
					getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(),
					PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getPessoaVO().getCPF()
					+ File.separator + getPessoaVO().getArquivoImagem().getNome();
			session.setAttribute("arquivoFoto", arquivoFoto);
			setExibirBotao(Boolean.TRUE);
			setExibirUpload(false);
			setCaminhoFotoUsuario(arquivoExterno);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getPessoaVO().getArquivoImagem(),
				getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getPessoaVO().getArquivoImagem(),
				getConfiguracaoGeralPadraoSistema());
	}

	public void consultarCidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false,
						getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false,
						getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public String editarDadosPessoa() throws Exception {
		try {
			setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(
					getUsuarioLogado().getPessoa().getCodigo(), false, true, Uteis.NIVELMONTARDADOS_TODOS,
					getUsuarioLogado()));
			getPessoaVO().setNovoObj(Boolean.FALSE);
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			inicializarMenuDadosPessoais();
			setCodigoTurno(0);
			setQuadroHorarioVO(new QuadroHorarioVO());
			setHorarioSimples(true);
			setListaSelectItemTurno(consultarTurnoPorNome(""));
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("dadosPessoaisProfessor.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("dadosPessoaisProfessor.xhtml");
	}

	public void montarListaDisponibilidadeTurno(Boolean detalhado) throws Exception {

		if (getQuadroHorarioVO().getTurno().getCodigo().intValue() != 0) {
			adicionarQuadroHorario();
			setQuadroHorarioVO(new QuadroHorarioVO());
		}
		setQuadroHorarioVO(getPessoaVO().consultarObjQuadroHorarioVO(getCodigoTurno()));
		if (getQuadroHorarioVO().getTurno().getCodigo().equals(0) && getCodigoTurno().intValue() != 0) {
			for (HorarioProfessorVO horarioProfessorVO : getPessoaVO().getHorarioProfessorVOs()) {
				if (horarioProfessorVO.getTurno().getCodigo().intValue() == getCodigoTurno().intValue()) {
					getQuadroHorarioVO().setTurno(horarioProfessorVO.getTurno());
					break;
				}
			}
			if (getQuadroHorarioVO().getTurno().getCodigo() == 0) {
				HorarioProfessorVO horarioProfessorVO = getFacadeFactory().getHorarioProfessorFacade()
						.consultarRapidaHorarioProfessorTurno(getPessoaVO().getCodigo(), getCodigoTurno(),
								getUsuarioLogado());
				if (horarioProfessorVO.getCodigo() == 0) {
					getQuadroHorarioVO().setTurno(getFacadeFactory().getTurnoFacade()
							.consultarPorChavePrimaria(codigoTurno, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				} else {
					getPessoaVO().getHorarioProfessorVOs().add(horarioProfessorVO);
					getQuadroHorarioVO().setTurno(horarioProfessorVO.getTurno());
				}
			}

			atualizarQuadroHorario(false);
			// setQuadroHorario(new QuadroHorarioVO());
		} else if (getCodigoTurno().intValue() == 0) {
			setQuadroHorarioVO(new QuadroHorarioVO());
		}

	}

	public void adicionarQuadroHorario() throws Exception {
		try {
			getPessoaVO().adicionarObjQuadroHorarioVOs(getQuadroHorarioVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarQuadroHorario(Boolean detalhado) throws Exception {
		List lista = getPessoaVO().getHorarioProfessorVOs();
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			HorarioProfessorVO obj = (HorarioProfessorVO) i.next();
			if (obj.getTurno().getCodigo().equals(getQuadroHorarioVO().getTurno().getCodigo())) {
				setQuadroHorarioVO(getFacadeFactory().getHorarioProfessorFacade().atualizarDadosQuadroHorario(obj,
						getQuadroHorarioVO(), detalhado, null, null, getUsuarioLogado()));
				// montarDadosHorarioProfessorDia();
				adicionarQuadroHorario();
				return;
			}
		}
		getFacadeFactory().getHorarioProfessorFacade().montarDadosListaQuadroHorarioVO(getQuadroHorarioVO(),
				getUsuarioLogado());
		adicionarQuadroHorario();
		getPessoaVO().montarListaHorarioProfessor();
	}

	public void montarDadosHorarioProfessorDia() throws Exception {
		for (HorarioProfessorDiaVO dia : getQuadroHorarioVO().getHorarioProfessorVO().getHorarioProfessorDiaVOs()) {
			if (dia.getHorarioProfessorDiaItemVOs().isEmpty()) {
				getFacadeFactory().getHorarioProfessorDiaFacade().montarDadosHorarioProfessorDiaItemVOs(dia,
						getQuadroHorarioVO().getHorarioProfessorVO(),
						getQuadroHorarioVO().getHorarioProfessorVO().getTurno(), null, null, getUsuarioLogado());
			}
			HorarioProfessorDia.montarDadosHorarioTurmaDiaItem(dia, "");
		}
		getFacadeFactory().getHorarioProfessorFacade()
				.inicializarDadosCalendario(getQuadroHorarioVO().getHorarioProfessorVO(), getUsuarioLogado());

	}

	public Integer getTamanhoListaTurno() {
		if (getListaSelectItemTurno() == null) {
			return 0;
		}
		if (getListaSelectItemTurno().size() > 6) {
			return 6;
		}
		return getListaSelectItemTurno().size();
	}

	public void selecionarTurno() throws Exception {
		TurnoVO obj = (TurnoVO) context().getExternalContext().getRequestMap().get("turnoItens");
		setCodigoTurno(obj.getCodigo());
		setHorarioSimples(true);
		montarListaDisponibilidadeTurno(true);

	}

	public List consultarTurnoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(pessoaVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarDadosPessoa() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getPessoaFacade().setIdEntidade("Professor");
			getFacadeFactory().getPessoaFacade().validarIncluir(getUsuarioLogadoClone());
			// pessoaVO.montarListaHorarioProfessor();
			if (pessoaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPessoaFacade().incluir(pessoaVO, getUsuarioLogado(),
						getConfiguracaoGeralPadraoSistema(), false);
			} else {
				getFacadeFactory().getPessoaFacade().alterar(pessoaVO, false, getUsuarioLogado(),
						getConfiguracaoGeralPadraoSistema(), false);
			}
			pessoaVO.setHorarioProfessorVOs(pessoaVO.montarNovaListaHorarioProfessor());
			getFacadeFactory().getHorarioProfessorFacade().alterarHorarioProfessorPorTurno(pessoaVO, getCodigoTurno(),
					getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarFormacaoAcademica() throws Exception {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				formacaoAcademicaVO.setPessoa(getPessoaVO().getCodigo());
			}
			getFormacaoAcademicaVO().setFuncionario(true);
			getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
			this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>FormacaoAcademica</code> para edição pelo usuário.
	 */
	public void editarFormacaoAcademica() throws Exception {
		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap()
				.get("formacaoAcademicaItens");
		setFormacaoAcademicaVO(obj);
	}

	/**
	 * Método responsável por remover um novo objeto da classe
	 * <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 **/
	public void removerFormacaoAcademica() throws Exception {
		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap()
				.get("formacaoAcademicaItens");
		getPessoaVO().excluirObjFormacaoAcademicaVOs(obj.getCurso());
		setMensagemID("msg_dados_excluidos");

	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoFormacaoAcademica() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoFormacaoAcademicas = (Hashtable) Dominios.getSituacaoFormacaoAcademica();
		Enumeration keys = situacaoFormacaoAcademicas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFormacaoAcademicas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>escolaridade</code>
	 */
	public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
		// List objs = new ArrayList(0);
		// objs.add(new SelectItem("", ""));
		// Hashtable escolaridadeFormacaoAcademicas = (Hashtable)
		// Dominios.getEscolaridadeFormacaoAcademica();
		// Enumeration keys = escolaridadeFormacaoAcademicas.keys();
		// while (keys.hasMoreElements()) {
		// String value = (String) keys.nextElement();
		// String label = (String) escolaridadeFormacaoAcademicas.get(value);
		// objs.add(new SelectItem(value, label));
		// }
		// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		// Collections.sort((List) objs, ordenador);
		List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>estadoCivil</code>
	 */
	public List getListaSelectItemEstadoCivilPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
		Enumeration keys = estadoCivils.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) estadoCivils.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
			i = resultadoConsulta.iterator();
			getListaSelectItemAreaConhecimento().clear();
			getListaSelectItemAreaConhecimento().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
				getListaSelectItemAreaConhecimento().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) getListaSelectItemAreaConhecimento(), ordenador);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemAreaConhecimento() {
		try {
			montarListaSelectItemAreaConhecimento("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false,
				getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	// public void inicializarListasSelectItemTodosComboBox() {
	// montarListaSelectItemAreaConhecimento();
	//
	// }
	public void consultarDisciplina() {
		try {

			List objs = new ArrayList(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(
						new Integer(valorInt), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado());
				if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false,
						Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(
						getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarDisciplina() throws Exception {
		try {
			setMensagemDetalhada("");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap()
					.get("disciplinaItens");
			DisciplinasInteresseVO disciplinasInteresseVO = new DisciplinasInteresseVO();
			disciplinasInteresseVO.setDisciplina(disciplina);
			adicionarDisciplinasInteresse(disciplinasInteresseVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarDisciplinasInteresse(DisciplinasInteresseVO disciplinasInteresseVO) throws Exception {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				disciplinasInteresseVO.setProfessor(getPessoaVO().getCodigo());
			}
			getPessoaVO().adicionarObjDisciplinasInteresseVOs(disciplinasInteresseVO);
			removerDisciplinaListaConsulta(disciplinasInteresseVO.getDisciplina().getCodigo());
			setMensagemID("msg_dados_adicionados");
			// return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "editar";
		}
	}

	public void removerDisciplinaListaConsulta(Integer codigo) {
		Iterator i = getListaConsultaDisciplina().iterator();
		int index = 0;
		while (i.hasNext()) {
			DisciplinaVO objExistente = (DisciplinaVO) i.next();
			if (objExistente.getCodigo().intValue() == codigo.intValue()) {
				getListaConsultaDisciplina().remove(index);
				return;
			}
			index++;
		}
	}

	public void removerDisciplinasInteresse() throws Exception {
		DisciplinasInteresseVO obj = (DisciplinasInteresseVO) context().getExternalContext().getRequestMap()
				.get("disciplinasInteresseItens");
		getPessoaVO().excluirObjDisciplinasInteresseVOs(obj.getDisciplina().getCodigo());
		setMensagemID("msg_dados_excluidos");
		// return "editar";
	}

	public void inicializarDadosFotoUsuario() throws Exception {
		try {
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
		} catch (Exception e) {
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoPadrao("imagem",
					getConfiguracaoGeralPadraoSistema(), "foto_usuario.png"));
		}
	}

	public List getTipoConsultaComboDisciplina() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("areaConhecimento", "Área de Conhecimento"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getTipoConsultaCidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("estado", "Estado"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void recortarFoto() {
		try {
			getFacadeFactory().getArquivoHelper().recortarFoto(getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY());
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			getLoginControle().setCaminhoFotoUsuario(getCaminhoFotoUsuario());
			getPessoaVO().getArquivoImagem().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM);
			confirmarFoto();
			setRemoverFoto((Boolean) false);
			cancelar();
			setOncompleteModal("RichFaces.$('panelImagem').hide();");
		} catch (Exception ex) {
			setOncompleteModal("RichFaces.$('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void verificarPrimeiroAcesso() {
		try {
			if (!getUsuarioLogado().getResetouSenhaPrimeiroAcesso()
					&& getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getPrimeiroAcessoProfessorResetarSenha()
							.booleanValue()) {
				setApresentarModalResetarSenha(Boolean.TRUE);
				setLogin(getUsuarioLogado().getUsername());
				setSenha("");
				setSenhaAnterior("");
			}
		} catch (Exception e) {
			setApresentarModalResetarSenha(Boolean.FALSE);
		}
	}

	public void alterarSenhaPrimeiroAcesso() {
		try {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(),
							getUnidadeEnsinoLogado().getCodigo());
			UsuarioVO usuario = getUsuarioLogado();
			usuario.setSenha(getSenha());
			getFacadeFactory().getUsuarioFacade().alterarSenhaPrimeiroAcesso(true, usuario, config);
			getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, usuario, getSenha());
			setApresentarModalResetarSenha(Boolean.FALSE);
			setMensagem("Senha redefinida com Sucesso! Utilize o username acima e a nova senha para acessar!");
		} catch (ConsistirException e) {
			if (!e.getListaMensagemErro().isEmpty()) {
				setMensagemDetalhada("msg_erro", e.getListaMensagemErro().get(0));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getPessoaVO().setCidade(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	public void cancelar() throws Exception {
		try {
			setExibirUpload(true);
			setExibirBotao(false);
			removerImagensUploadArquivoTemp();
			// setCaminhoFotoUsuario(null);
		} catch (Exception ex) {
			setOncompleteModal("RichFaces.$('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void removerImagensUploadArquivoTemp() throws Exception {
		try {
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getPessoaVO().getCPF();
			File arquivo = new File(arquivoExterno);
			getArquivoHelper().deleteRecursivo(arquivo);
		} catch (Exception e) {
			throw e;
		}
	}

	private String urlMoodle;

	public void realizaLoginMoodle() throws Exception {
		try {
			String token = getFacadeFactory().getMatriculaFacade()
					.consultarTokenPessoaPorCodigo(getPessoaVO().getCodigo());
			String url = getConfiguracaoGeralPadraoSistema().getLinkAcessoVisoesMoodle() + token;
			if (!url.equals("")) {
				setUrlMoodle(url);
			} else {
				throw new Exception(
						"Não foi possível logar no ambiente de estudo, entre em contato com o departamento responsável!");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Metodo responsavel por retornar a URL atual para UPLOAD de fotos no servidor
	 * 
	 * @return
	 */
	public String getUrlWebCam() {
		try {
			String url = request().getRequestURL().toString().substring(0,
					request().getRequestURL().toString().indexOf(request().getContextPath()))
					+ request().getContextPath();
			return "webcam.freeze();webcam.upload('" + url + "/UploadServlet')";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void upLoadImagem(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void renderizarUpload() {
		setExibirUpload(false);
	}

	public String getShowFotoCrop() {
		try {
			if (getPessoaVO().getArquivoImagem().getNome() == null) {
				return "resources/imagens/usuarioPadrao.jpg";
			}
			return getCaminhoFotoUsuario() + "?UID=" + new Date().getTime();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}

	public void removerFoto() {
		try {
			getFacadeFactory().getArquivoHelper().removerArquivoDiretorio(true, getPessoaVO().getArquivoImagem(),
					PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema());
			getPessoaVO().setArquivoImagem(null);
			getFacadeFactory().getPessoaFacade().alterar(getPessoaVO(), false, getUsuarioLogado(),
					getConfiguracaoGeralPadraoSistema(), false);
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void paint(OutputStream out, Object data) throws Exception {

		try {
			getFacadeFactory().getArquivoHelper().renderizarImagemNaTela(out, getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public void paintTemp(OutputStream out, Object data) throws Exception {

		try {
			getFacadeFactory().getArquivoHelper().renderizarImagemNaTela(out, getPessoaVO().getArquivoImagem(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public void confirmarFoto() {
		try {
			getPessoaVO().inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(
					getConfiguracaoGeralPadraoSistema(), "FUNCIONARIO");
			getFacadeFactory().getPessoaFacade().alterarSemFiliacao(getPessoaVO(), false, getUsuarioLogado(),
					getConfiguracaoGeralPadraoSistema(), false);
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
					getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
					getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getExisteImagem() {
		return true;
	}

	public void inicializarDadosPadroes() {
		setBackgroundFoto("./resources/imagens/fotoRoxo.png");
		setBackgroundFundo("./resources/imagens/fundoRoxo.png");
		setBackgroundTopo("./resources/imagens/topoRoxo.png");
		setBackgroundMenu("./resources/imagens/menuRoxo.png");
		setBackgroundHeader("./resources/imagens/headerRoxo.png");
		setBackgroundTopoBack("./resources/imagens/topoBackRoxo.png");
		// inicializarMenuRecado();
	}

	public void carregarBackground(String cssPadrao) {
		if (cssPadrao == null) {
			cssPadrao = "";
		}
		if (cssPadrao.equals("")) {
			setBackgroundFoto("./resources/imagens/fotoRoxo.png");
			setBackgroundFundo("./resources/imagens/fundoRoxo.png");
			setBackgroundTopo("./resources/imagens/topoRoxo.png");
			setBackgroundTopoBack("./resources/imagens/topoBackRoxo.png");
			setBackgroundMenu("./resources/imagens/menuRoxo.png");
			setBackgroundHeader("./resources/imagens/headerRoxo.png");
		} else {
			setBackgroundFoto("./resources/imagens/foto" + cssPadrao + ".png");
			setBackgroundFundo("./resources/imagens/fundo" + cssPadrao + ".png");
			setBackgroundTopo("./resources/imagens/topo" + cssPadrao + ".png");
			setBackgroundTopoBack("./resources/imagens/topoBack" + cssPadrao + ".png");
			setBackgroundMenu("./resources/imagens/menu" + cssPadrao + ".png");
			setBackgroundHeader("./resources/imagens/header" + cssPadrao + ".png");
		}

	}

	public void carregarBackground(String valorCssTopoLogo, String valorCssBackground, String valorCssMenu)
			throws Exception {
		if (valorCssTopoLogo.equals("") && valorCssBackground.equals("") && valorCssMenu.equals("")) {
			setValorCssTopoLogo("#3f3659");
			setValorCssBackground("#e6e6e6");
			setValorCssMenu("#8784a8");
		} else {
			setValorCssTopoLogo(getUsuarioLogado().getPessoa().getValorCssTopoLogo());
			setValorCssBackground(getUsuarioLogado().getPessoa().getValorCssBackground());
			setValorCssMenu(getUsuarioLogado().getPessoa().getValorCssMenu());
		}

	}

	public void realizarAlteracaoCorTopoLogo() {
		HttpServletRequest request = request();
		request.getSession().setAttribute("valorCssTopoLogo", getValorCssTopoLogo());
		setBackgroundHeader("background-color: " + getValorCssTopoLogo()
				+ ";filter:progid:DXImageTransform.Microsoft.Gradient(startColorStr='" + getValorCssTopoLogo()
				+ "',endColorStr='#FFFFFF',gradientType='0');" + "background-image: -moz-linear-gradient(top, "
				+ getValorCssTopoLogo() + ", #FFFFFF);"
				+ "background-image: -webkit-gradient(linear, left top, left bottom, from(" + getValorCssTopoLogo()
				+ "), to(#ffffff)); ");
	}

	public void realizarAlteracaoCorBackground() {
		HttpServletRequest request = request();
		request.getSession().setAttribute("valorCssBackground", getValorCssBackground());
		setBackgroundFundo("background-color: " + getValorCssBackground()
				+ ";filter:progid:DXImageTransform.Microsoft.Gradient(startColorStr='" + getValorCssBackground()
				+ "',endColorStr='#FFFFFF',gradientType='0');" + "background-image: -moz-linear-gradient(top, "
				+ getValorCssBackground() + ", #FFFFFF);"
				+ "background-image: -webkit-gradient(linear, left top, left bottom, from(" + getValorCssBackground()
				+ "), to(#ffffff)); ");
	}

	public void realizarAlteracaoCorMenu() {
		HttpServletRequest request = request();
		request.getSession().setAttribute("valorCssMenu", getValorCssMenu());
		setBackgroundMenu("background-color: " + getValorCssMenu()
				+ ";filter:progid:DXImageTransform.Microsoft.Gradient(startColorStr='" + getValorCssMenu()
				+ "',endColorStr='#FFFFFF',gradientType='0');" + "background-image: -moz-linear-gradient(top, "
				+ getValorCssMenu() + ", #FFFFFF);"
				+ "background-image: -webkit-gradient(linear, left top, left bottom, from(" + getValorCssMenu()
				+ "), to(#ffffff)); ");
	}

	public void persistirCorCss() {
	}

	public void inicializarMenuRecado() {
		setMenuRecado(false);
		setMenuDisciplina(false);
		setMenuRequisicao(false);
		setMenuRelatorio(false);
		setMenuConfiguracao(false);
		setMenuMeusHorarios(false);
		setMenuDadosPessoais(false);
		setMenuRegistroAula(false);
		setMenuRegistroNota(false);
		setMenuSenha(false);
		setConfiguracao(false);
		setNovoComunicado(false);
		setLerComunicado(false);
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuNovoRecado() {
		setNovoComunicado(Boolean.TRUE);
		setLerComunicado(Boolean.FALSE);
	}

	public void inicializarMenuLerRecado() {
		setNovoComunicado(Boolean.FALSE);
		setLerComunicado(Boolean.TRUE);
	}

	public void inicializarMenuMeusHorarios() {

		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuRequisicao(Boolean.FALSE);
		setMenuRelatorio(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.TRUE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuRegistroAula(Boolean.FALSE);
		setMenuRegistroNota(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		inicializarHorarioSimples();
		setMensagemDetalhada("", "");
	}

	public String inicializarMenuConfiguracao() {
		setMenuConfiguracao(Boolean.TRUE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMensagemDetalhada("", "");
		setLogin("");
		setSenha("");
		setLoginAnterior("");
		setSenhaAnterior("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoProfessor.xhtml");
	}

	public void inicializarMenuDadosPessoais() {
		setMenuDadosPessoais(Boolean.TRUE);
		verificarPermissaoEditarFormacaoAcademica();
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuRegistroAula() {
		setMenuRegistroAula(Boolean.TRUE);
		setNovoRegistroAula(Boolean.TRUE);
		setConsultarRegistroAula(Boolean.FALSE);
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuRegistroNota() {
		setMenuRegistroNota(Boolean.TRUE);
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuRegistroAulaNota() {
		setMenuRegistroAulaNota(Boolean.TRUE);
		setMensagemDetalhada("", "");
	}

	public String inicializarMenuDisciplina() {
		inicializarDisciplinaConsultar();
		setMensagemDetalhada("", "");
		return "disciplinaProfessor";
	}

	public String inicializarMenuRequisicao() {
		inicializarRequisicaoConsultar();
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoProfessor.xhtml");
	}

	public void inicializarDisciplinaEditar() {
		setDisciplinaEditar(Boolean.TRUE);
		setDisciplinaConsultar(Boolean.FALSE);
	}

	public void inicializarDisciplinaConsultar() {
		setDisciplinaEditar(Boolean.FALSE);
		setDisciplinaConsultar(Boolean.TRUE);
	}

	public void inicializarRequisicaoEditar() {
		setRequisicaoEditar(Boolean.TRUE);
		setRequisicaoConsultar(Boolean.FALSE);
	}

	public void inicializarRequisicaoConsultar() {
		setRequisicaoEditar(Boolean.FALSE);
		setRequisicaoConsultar(Boolean.TRUE);
	}

	public void inicializarHorarioSimples() {
		setHorarioSimples(Boolean.TRUE);
		setHorarioDetalhado(Boolean.FALSE);
		setMensagemDetalhada("", "");
	}

	public void inicializarHorarioDetalhado() {
		setHorarioSimples(Boolean.FALSE);
		setHorarioDetalhado(Boolean.TRUE);
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuSenha() {
		setConfiguracao(Boolean.FALSE);
		setMenuSenha(Boolean.TRUE);
		setLoginAnterior("");
		setSenhaAnterior("");
		setMensagemDetalhada("", "");
		setLoginAnterior(getUsuarioLogado().getUsername());
		setLogin(getUsuarioLogado().getUsername());
		try {
			setPermitirAlterarUsername(getConfiguracaoGeralPadraoSistema().getNaoPermitirAlterarUsernameUsuario());
		} catch (Exception e) {
		}
	}

	public void inicializarConfiguracao() {
		setConfiguracao(Boolean.TRUE);
		setMenuSenha(Boolean.FALSE);
		setMensagemDetalhada("", "");
	}

	public void inicializarConsultaRegistroAula() {
		setNovoRegistroAula(Boolean.FALSE);
		setConsultarRegistroAula(Boolean.TRUE);
	}

	public String inicializarMenuRelatorio() {
		setMenuRelatorio(Boolean.TRUE);
		setMenuDiario(Boolean.FALSE);
		setMenuEspelhoDiario(Boolean.FALSE);
		setMenuHistoricoAluno(Boolean.FALSE);
		setMenuHistoricoTurma(Boolean.FALSE);
		setMenuPerfilTurma(Boolean.FALSE);
		setMenuDownload(Boolean.FALSE);
		setMenuRelatorioAulasRegistradasNaoRegistradas(Boolean.FALSE);
		setApresentarIconeRelatorio(Boolean.TRUE);
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("relatorioProfessor.xhtml");
	}

	public String inicializarMenuDiario() {

		setMenuDiario(Boolean.TRUE);
		setMenuEspelhoDiario(Boolean.FALSE);
		setMenuHistoricoAluno(Boolean.FALSE);
		setMenuHistoricoTurma(Boolean.FALSE);
		setMenuDownload(Boolean.FALSE);
		setMenuPerfilTurma(Boolean.FALSE);
		setMenuRelatorioAulasRegistradasNaoRegistradas(Boolean.FALSE);
		setApresentarIconeRelatorio(Boolean.FALSE);
		return removerControleMemoriaFlashTela(DiarioRelControle.class.getSimpleName(), "diarioProfessor.xhtml");
	}

	public String inicializarMenuEspelhoDiario() {
		setMenuDiario(Boolean.FALSE);
		setMenuEspelhoDiario(Boolean.TRUE);
		setMenuHistoricoAluno(Boolean.FALSE);
		setMenuHistoricoTurma(Boolean.FALSE);
		setMenuDownload(Boolean.FALSE);
		setMenuPerfilTurma(Boolean.FALSE);
		setMenuRelatorioAulasRegistradasNaoRegistradas(Boolean.FALSE);
		setApresentarIconeRelatorio(Boolean.FALSE);
		return removerControleMemoriaFlashTela(EspelhoRelControle.class.getSimpleName(),
				"espelhoDiarioProfessor.xhtml");
	}

	public String inicializarMenuHistoricoAluno() {
		setMenuDiario(Boolean.FALSE);
		setMenuEspelhoDiario(Boolean.FALSE);
		setMenuHistoricoAluno(Boolean.TRUE);
		setMenuHistoricoTurma(Boolean.FALSE);
		setMenuDownload(Boolean.FALSE);
		setMenuPerfilTurma(Boolean.FALSE);
		setMenuRelatorioAulasRegistradasNaoRegistradas(Boolean.FALSE);
		setApresentarIconeRelatorio(Boolean.FALSE);
		return Uteis.getCaminhoRedirecionamentoNavegacao("historicoAlunoProfessor.xhtml");
	}

	public String inicializarMenuHistoricoTurma() {
		setMenuDiario(Boolean.FALSE);
		setMenuEspelhoDiario(Boolean.FALSE);
		setMenuHistoricoAluno(Boolean.FALSE);
		setMenuHistoricoTurma(Boolean.TRUE);
		setMenuDownload(Boolean.FALSE);
		setMenuPerfilTurma(Boolean.FALSE);
		setMenuRelatorioAulasRegistradasNaoRegistradas(Boolean.FALSE);
		setApresentarIconeRelatorio(Boolean.FALSE);
		return removerControleMemoriaFlashTela(HistoricoTurmaRelControle.class.getSimpleName(),
				"historicoTurmaProfessor.xhtml");
	}

	public String inicializarMenuDownloads() {
		setMenuDiario(Boolean.FALSE);
		setMenuEspelhoDiario(Boolean.FALSE);
		setMenuHistoricoAluno(Boolean.FALSE);
		setMenuHistoricoTurma(Boolean.FALSE);
		setMenuPerfilTurma(Boolean.FALSE);
		setMenuDownload(Boolean.TRUE);
		setMenuRelatorioAulasRegistradasNaoRegistradas(Boolean.FALSE);
		setApresentarIconeRelatorio(Boolean.FALSE);
		return removerControleMemoriaFlashTela(DownloadRelControle.class.getSimpleName(),
				"acessoMaterialProfessor.xhtml");
	}
	
	public String inicializarMenuRelatorioAulasRegistradasNaoRegistradas() {
		setMenuDiario(Boolean.FALSE);
		setMenuEspelhoDiario(Boolean.FALSE);
		setMenuHistoricoAluno(Boolean.FALSE);
		setMenuHistoricoTurma(Boolean.FALSE);
		setMenuDownload(Boolean.FALSE);
		setMenuPerfilTurma(Boolean.FALSE);
		setMenuRelatorioAulasRegistradasNaoRegistradas(Boolean.TRUE);
		setApresentarIconeRelatorio(Boolean.FALSE);
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaLancadaNaoLancadaRelProfessor.xhtml");
	}

	public String inicializarMenuPerfilTurma() {
		setMenuDiario(Boolean.FALSE);
		setMenuEspelhoDiario(Boolean.FALSE);
		setMenuHistoricoAluno(Boolean.FALSE);
		setMenuHistoricoTurma(Boolean.FALSE);
		setMenuDownload(Boolean.FALSE);
		setMenuPerfilTurma(Boolean.TRUE);
		setApresentarIconeRelatorio(Boolean.FALSE);
		return removerControleMemoriaFlashTela(PerfilTurmaRelControle.class.getSimpleName(),
				"perfilTurmaProfessor.xhtml");
	}

	public void inicializarUploadArquivosProfessor() {
		setUploadArquivosProfessor(Boolean.TRUE);
		setMensagemDetalhada("", "");
	}

	public void inicializarAtaProva() {
		setAtaProva(Boolean.TRUE);
		setMensagemDetalhada("", "");
	}

	public List consultarTurmaPorProfessor() throws Exception {
		List listaConsulta = getFacadeFactory().getTurmaFacade().consultaRapidaTurmaPorProfessor(
				getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false,
				getUsuarioLogado());
		return listaConsulta;
	}

	public void gravarCssPadrao() throws Exception {

		Integer codigoPessoa = getUsuarioLogado().getPessoa().getCodigo();
		getFacadeFactory().getPessoaFacade().alterarCss(codigoPessoa, getValorCssTopoLogo(), getValorCssBackground(),
				getValorCssMenu());
		// getUsuarioLogado().getPessoa().setCssPadrao(css);

	}

	public void cssRosa() {
		try {
			HttpServletRequest request = request();
			request.getSession().setAttribute("cssPadrao", "Rosa");
			carregarBackground("Rosa");
			// gravarCssPadrao("Rosa");
		} catch (Exception e) {
		}
	}

	public void cssRed() {
		try {
			HttpServletRequest request = request();
			request.getSession().setAttribute("cssPadrao", "Vermelho");
			carregarBackground("Vermelho");
			// gravarCssPadrao("Vermelho");
		} catch (Exception e) {
		}
	}

	public void cssRoxo() {
		try {
			HttpServletRequest request = request();
			request.getSession().setAttribute("cssPadrao", "Roxo");
			carregarBackground("Roxo");
			// gravarCssPadrao("Roxo");
		} catch (Exception e) {
		}
	}

	public void cssBlue() {
		try {
			HttpServletRequest request = request();
			request.getSession().setAttribute("cssPadrao", "Azul");
			carregarBackground("Azul");
			// gravarCssPadrao("Azul");
		} catch (Exception e) {
		}
	}

	public void cssGreen() {
		try {
			HttpServletRequest request = request();
			request.getSession().setAttribute("cssPadrao", "Verde");
			carregarBackground("Verde");
			// gravarCssPadrao("Verde");
		} catch (Exception e) {
		}
	}

	public void cssCinza() {
		try {
			HttpServletRequest request = request();
			request.getSession().setAttribute("cssPadrao", "Cinza");
			carregarBackground("Cinza");
			// gravarCssPadrao("Cinza");
		} catch (Exception e) {
		}
	}

	public String getBackgroundFoto() {
		return backgroundFoto;
	}

	public void setBackgroundFoto(String backgroundFoto) {
		this.backgroundFoto = backgroundFoto;
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

	public List getListaConsultaDisciplina() {
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
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

	public String getBackgroundFundo() {
		return backgroundFundo;
	}

	public void setBackgroundFundo(String backgroundFundo) {
		this.backgroundFundo = backgroundFundo;
	}

	public String getBackgroundTopo() {
		return backgroundTopo;
	}

	public void setBackgroundTopo(String backgroundTopo) {
		this.backgroundTopo = backgroundTopo;
	}

	public String getBackgroundMenu() {
		return backgroundMenu;
	}

	public void setBackgroundMenu(String backgroundMenu) {
		this.backgroundMenu = backgroundMenu;
	}

	public String getBackgroundHeader() {
		return backgroundHeader;
	}

	public void setBackgroundHeader(String backgroundHeader) {
		this.backgroundHeader = backgroundHeader;
	}

	public Boolean getMenuConfiguracao() {
		return menuConfiguracao;
	}

	public void setMenuConfiguracao(Boolean menuConfiguracao) {
		this.menuConfiguracao = menuConfiguracao;
	}

	public Boolean getMenuDadosPessoais() {
		return menuDadosPessoais;
	}

	public void setMenuDadosPessoais(Boolean menuDadosPessoais) {
		this.menuDadosPessoais = menuDadosPessoais;
	}

	public Boolean getMenuRecado() {
		return menuRecado;
	}

	public void setMenuRecado(Boolean menuRecado) {
		this.menuRecado = menuRecado;
	}

	public String getBackgroundTopoBack() {
		return backgroundTopoBack;
	}

	public void setBackgroundTopoBack(String backgroundTopoBack) {
		this.backgroundTopoBack = backgroundTopoBack;
	}

	public List getListaSelectItemAreaConhecimento() {
		if (listaSelectItemAreaConhecimento == null) {
			listaSelectItemAreaConhecimento = new ArrayList(0);
			montarListaSelectItemAreaConhecimento();
		}
		return listaSelectItemAreaConhecimento;
	}

	public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	public FormacaoAcademicaVO getFormacaoAcademicaVO() {
		if (formacaoAcademicaVO == null) {
			formacaoAcademicaVO = new FormacaoAcademicaVO();
		}
		return formacaoAcademicaVO;
	}

	public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
		this.formacaoAcademicaVO = formacaoAcademicaVO;
	}

	public List getListaSelectItemCidade() {
		return (listaSelectItemCidade);
	}

	public void setListaSelectItemCidade(List listaSelectItemCidade) {
		this.listaSelectItemCidade = listaSelectItemCidade;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public Boolean getSubmeterPagina() {
		if (submeterPagina == null) {
			submeterPagina = Boolean.TRUE;
		}
		return submeterPagina;
	}

	public void setSubmeterPagina(Boolean submeterPagina) {
		this.submeterPagina = submeterPagina;
	}

	public String getLoginAnterior() {
		return loginAnterior;
	}

	public void setLoginAnterior(String loginAnterior) {
		this.loginAnterior = loginAnterior;
	}

	public String getSenhaAnterior() {
		return senhaAnterior;
	}

	public void setSenhaAnterior(String senhaAnterior) {
		this.senhaAnterior = senhaAnterior;
	}

	public Boolean getMenuSenha() {
		return menuSenha;
	}

	public void setMenuSenha(Boolean menuSenha) {
		this.menuSenha = menuSenha;
	}

	public Boolean getHorarioDetalhado() {
		return horarioDetalhado;
	}

	public void setHorarioDetalhado(Boolean horarioDetalhado) {
		this.horarioDetalhado = horarioDetalhado;
	}

	public Boolean getHorarioSimples() {
		return horarioSimples;
	}

	public void setHorarioSimples(Boolean horarioSimples) {
		this.horarioSimples = horarioSimples;
	}

	public Boolean getMenuRegistroNota() {
		return menuRegistroNota;
	}

	public void setMenuRegistroNota(Boolean menuRegistroNota) {
		this.menuRegistroNota = menuRegistroNota;
	}

	public Boolean getMenuDisciplina() {
		return menuDisciplina;
	}

	public void setMenuDisciplina(Boolean menuDisciplina) {
		this.menuDisciplina = menuDisciplina;
	}

	public Boolean getMenuRelatorio() {
		return menuRelatorio;
	}

	public void setMenuRelatorio(Boolean menuRelatorio) {
		this.menuRelatorio = menuRelatorio;
	}

	public Boolean getMenuDiario() {
		return menuDiario;
	}

	public void setMenuDiario(Boolean menuDiario) {
		this.menuDiario = menuDiario;
	}

	public Boolean getMenuEspelhoDiario() {
		return menuEspelhoDiario;
	}

	public void setMenuEspelhoDiario(Boolean menuEspelhoDiario) {
		this.menuEspelhoDiario = menuEspelhoDiario;
	}

	public Boolean getMenuHistoricoAluno() {
		return menuHistoricoAluno;
	}

	public void setMenuHistoricoAluno(Boolean menuHistoricoAluno) {
		this.menuHistoricoAluno = menuHistoricoAluno;
	}

	public Boolean getMenuHistoricoTurma() {
		return menuHitoricoTurma;
	}

	public void setMenuHistoricoTurma(Boolean menuHitoricoTurma) {
		this.menuHitoricoTurma = menuHitoricoTurma;
	}

	public Boolean getNovoComunicado() {
		return novoComunicado;
	}

	public void setNovoComunicado(Boolean novoComunicado) {
		this.novoComunicado = novoComunicado;
	}

	@Override
	public void liberarBackingBeanMemoria() {
		super.liberarBackingBeanMemoria();

		RegistroAulaControle registro = (RegistroAulaControle) context().getExternalContext().getSessionMap()
				.get("RegistroAulaControle");
		if (registro != null) {

			registro.limparRecursosMemoria();
			// System.out.println("BACKING....: Registro REMOVIDO DA MEMÓRIA.");
		}

		HistoricoTurmaControle historico = (HistoricoTurmaControle) context().getExternalContext().getSessionMap()
				.get("HistoricoTurmaControle");
		if (historico != null) {
			historico.limparRecursosMemoria();
			// System.out.println("BACKING....: Historico REMOVIDO DA MEMÓRIA.");
		}

		LoginControle logins = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
		if (logins != null) {
			logins.logout();
			// System.out.println("BACKING....: logins REMOVIDO DA MEMÓRIA.");
		}

		novoComunicado = null;
		login = null;
		loginAnterior = null;
		senha = null;
		senhaAnterior = null;
		backgroundFoto = null;
		backgroundTopo = null;
		backgroundTopoBack = null;
		backgroundFundo = null;
		backgroundMenu = null;
		backgroundHeader = null;
		menuRecado = null;
		menuRegistroAula = null;
		horarioDetalhado = null;
		horarioDetalhado = null;
		menuConfiguracao = null;
		menuDadosPessoais = null;
		menuRegistroNota = null;
		submeterPagina = false;
		menuSenha = false;
		configuracao = null;
		// Atributos usados para os dados Pessoais
		Uteis.liberarListaMemoria(listaSelectItemCidade);
		Uteis.liberarListaMemoria(listaSelectItemAreaConhecimento);
		formacaoAcademicaVO = null;
		pessoaVO = null;
		lerComunicado = null;
	}

	public Boolean getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Boolean configuracao) {
		this.configuracao = configuracao;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getMenuMeusHorarios() {
		return menuMeusHorarios;
	}

	public void setMenuMeusHorarios(Boolean menuMeusHorarios) {
		this.menuMeusHorarios = menuMeusHorarios;
	}

	public Boolean getMenuRegistroAula() {
		return menuRegistroAula;
	}

	public void setMenuRegistroAula(Boolean menuRegistroAula) {
		this.menuRegistroAula = menuRegistroAula;
	}

	public Boolean getConsultarRegistroAula() {
		return consultarRegistroAula;
	}

	public void setConsultarRegistroAula(Boolean consultarRegistroAula) {
		this.consultarRegistroAula = consultarRegistroAula;
	}

	public Boolean getNovoRegistroAula() {
		return novoRegistroAula;
	}

	public void setNovoRegistroAula(Boolean novoRegistroAula) {
		this.novoRegistroAula = novoRegistroAula;
	}

	public Boolean getDisciplinaConsultar() {
		return disciplinaConsultar;
	}

	public void setDisciplinaConsultar(Boolean disciplinaConsultar) {
		this.disciplinaConsultar = disciplinaConsultar;
	}

	public Boolean getDisciplinaEditar() {
		return disciplinaEditar;
	}

	public void setDisciplinaEditar(Boolean disciplinaEditar) {
		this.disciplinaEditar = disciplinaEditar;
	}

	public Boolean getLerComunicado() {
		return lerComunicado;
	}

	public void setLerComunicado(Boolean lerComunicado) {
		this.lerComunicado = lerComunicado;
	}

	/**
	 * @return the menuRequisicao
	 */
	public Boolean getMenuRequisicao() {
		return menuRequisicao;
	}

	/**
	 * @param menuRequisicao the menuRequisicao to set
	 */
	public void setMenuRequisicao(Boolean menuRequisicao) {
		this.menuRequisicao = menuRequisicao;
	}

	/**
	 * @return the requisicaoEditar
	 */
	public Boolean getRequisicaoEditar() {
		return requisicaoEditar;
	}

	/**
	 * @param requisicaoEditar the requisicaoEditar to set
	 */
	public void setRequisicaoEditar(Boolean requisicaoEditar) {
		this.requisicaoEditar = requisicaoEditar;
	}

	/**
	 * @return the requisicaoConsultar
	 */
	public Boolean getRequisicaoConsultar() {
		return requisicaoConsultar;
	}

	/**
	 * @param requisicaoConsultar the requisicaoConsultar to set
	 */
	public void setRequisicaoConsultar(Boolean requisicaoConsultar) {
		this.requisicaoConsultar = requisicaoConsultar;
	}

	public List getListaConsultaCidade() {
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	public String getCampoConsultaCidade() {
		if (campoConsultaCidade == null) {
			campoConsultaCidade = "";
		}
		return campoConsultaCidade;
	}

	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	public String getValorConsultaCidade() {
		return valorConsultaCidade;
	}

	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	public Boolean getUploadArquivosProfessor() {
		return uploadArquivosProfessor;
	}

	public void setUploadArquivosProfessor(Boolean uploadArquivosProfessor) {
		this.uploadArquivosProfessor = uploadArquivosProfessor;
	}

	public Boolean getAtaProva() {
		return ataProva;
	}

	public void setAtaProva(Boolean ataProva) {
		this.ataProva = ataProva;
	}

	public List getListaSelectItemTurma() {
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public List getListaSelectItemDisciplinasTurma() {
		if (listaSelectItemDisciplinasTurma == null) {
			listaSelectItemDisciplinasTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinasTurma;
	}

	public void setListaSelectItemDisciplinasTurma(List listaSelectItemDisciplinasTurma) {
		this.listaSelectItemDisciplinasTurma = listaSelectItemDisciplinasTurma;
	}

	public Boolean getMenuDownload() {
		return menuDownload;
	}

	public void setMenuDownload(Boolean menuDownload) {
		this.menuDownload = menuDownload;
	}

	/**
	 * @return the valorCssTopoLogo
	 */
	public String getValorCssTopoLogo() {
		if (valorCssTopoLogo == null) {
			valorCssTopoLogo = "#2c2c54";
		}
		return valorCssTopoLogo;
	}

	/**
	 * @param valorCssTopoLogo the valorCssTopoLogo to set
	 */
	public void setValorCssTopoLogo(String valorCssTopoLogo) {
		this.valorCssTopoLogo = valorCssTopoLogo;
	}

	/**
	 * @return the valorCssBackground
	 */
	public String getValorCssBackground() {
		if (valorCssBackground == null) {
			valorCssBackground = "#d6d2d2";
		}
		return valorCssBackground;
	}

	/**
	 * @param valorCssBackground the valorCssBackground to set
	 */
	public void setValorCssBackground(String valorCssBackground) {
		this.valorCssBackground = valorCssBackground;
	}

	/**
	 * @return the valorCssMenu
	 */
	public String getValorCssMenu() {
		if (valorCssMenu == null) {
			valorCssMenu = "#8c8aa8";
		}
		return valorCssMenu;
	}

	/**
	 * @param valorCssMenu the valorCssMenu to set
	 */
	public void setValorCssMenu(String valorCssMenu) {
		this.valorCssMenu = valorCssMenu;
	}

	public void verificarPossuiPermissaoExcluirRegistroAula() {
		try {
			ControleAcesso.excluir("RegistroAula");
			possuiPermissaoExcluirRegistroAula = (true);
		} catch (Exception e) {
			possuiPermissaoExcluirRegistroAula = (false);
		}
	}

	public void verificarPossuiTCCVinculado() {
		try {
			apresentarMenuTcc = getFacadeFactory().getTrabalhoConclusaoCursoFacade()
					.realizarVerificacaoTrabalhoConclusaoCursoVinculadoProfessor(getPessoaVO().getCodigo());
		} catch (Exception e) {
			apresentarMenuTcc = (false);
		}
	}

	public void verificarPermissaoEditarFormacaoAcademica() {
		try {
			if (getUsuarioLogado().getUsuarioPerfilAcessoVOs().isEmpty()) {
				UsuarioPerfilAcessoVO u = new UsuarioPerfilAcessoVO();
				u.setPerfilAcesso(getUsuarioLogado().getPerfilAcesso());
				getUsuarioLogado().getUsuarioPerfilAcessoVOs().add(u);
			}
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AlterarFormacaoAcademica",
					getUsuarioLogado());
			apresentarAdicionarFormacao = (true);
		} catch (Exception e) {
			apresentarAdicionarFormacao = (false);
		}
	}

	public boolean getIsMostrarExcluirRegistroAula() {
		if (getPossuiPermissaoExcluirRegistroAula() && getNovoRegistroAula()) {
			return true;
		}
		return false;
	}

	public Boolean getPossuiPermissaoExcluirRegistroAula() {
		if (possuiPermissaoExcluirRegistroAula == null) {
			verificarPossuiPermissaoExcluirRegistroAula();
		}
		return possuiPermissaoExcluirRegistroAula;
	}

	public void setPossuiPermissaoExcluirRegistroAula(Boolean possuiPermissaoExcluirRegistroAula) {
		this.possuiPermissaoExcluirRegistroAula = possuiPermissaoExcluirRegistroAula;
	}

	public void setMenuRegistroAulaNota(Boolean menuRegistroAulaNota) {
		this.menuRegistroAulaNota = menuRegistroAulaNota;
	}

	public Boolean getMenuRegistroAulaNota() {
		if (menuRegistroAulaNota == null) {
			menuRegistroAulaNota = Boolean.FALSE;
		}
		return menuRegistroAulaNota;
	}

	/**
	 * @return the quadroHorarioVO
	 */
	public QuadroHorarioVO getQuadroHorarioVO() {
		if (quadroHorarioVO == null) {
			quadroHorarioVO = new QuadroHorarioVO();
		}
		return quadroHorarioVO;
	}

	/**
	 * @param quadroHorarioVO the quadroHorarioVO to set
	 */
	public void setQuadroHorarioVO(QuadroHorarioVO quadroHorarioVO) {
		this.quadroHorarioVO = quadroHorarioVO;
	}

	/**
	 * @return the codigoTurno
	 */
	public Integer getCodigoTurno() {
		if (codigoTurno == null) {
			codigoTurno = 0;
		}
		return codigoTurno;
	}

	/**
	 * @param codigoTurno the codigoTurno to set
	 */
	public void setCodigoTurno(Integer codigoTurno) {
		this.codigoTurno = codigoTurno;
	}

	/**
	 * @return the listaSelectItemTurno
	 */
	public List getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList(0);
		}
		return listaSelectItemTurno;
	}

	/**
	 * @param listaSelectItemTurno the listaSelectItemTurno to set
	 */
	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	/**
	 * @return the permissaoAlterarSenha
	 */
	public Boolean getPermissaoAlterarSenha() {
		if (permissaoAlterarSenha == null) {
			permissaoAlterarSenha = Boolean.FALSE;
		}
		return permissaoAlterarSenha;
	}

	/**
	 * @param permissaoAlterarSenha the permissaoAlterarSenha to set
	 */
	public void setPermissaoAlterarSenha(Boolean permissaoAlterarSenha) {
		this.permissaoAlterarSenha = permissaoAlterarSenha;
	}

	/**
	 * @return the permissaoAlterarFoto
	 */
	public Boolean getPermissaoAlterarFoto() {
		if (permissaoAlterarFoto == null) {
			permissaoAlterarFoto = Boolean.FALSE;
		}
		return permissaoAlterarFoto;
	}

	/**
	 * @param permissaoAlterarFoto the permissaoAlterarFoto to set
	 */
	public void setPermissaoAlterarFoto(Boolean permissaoAlterarFoto) {
		this.permissaoAlterarFoto = permissaoAlterarFoto;
	}

	/**
	 * @return the permissaoAlterarCorTela
	 */
	public Boolean getPermissaoAlterarCorTela() {
		if (permissaoAlterarCorTela == null) {
			permissaoAlterarCorTela = Boolean.FALSE;
		}
		return permissaoAlterarCorTela;
	}

	/**
	 * @param permissaoAlterarCorTela the permissaoAlterarCorTela to set
	 */
	public void setPermissaoAlterarCorTela(Boolean permissaoAlterarCorTela) {
		this.permissaoAlterarCorTela = permissaoAlterarCorTela;
	}

	/**
	 * @return the permissaoAcessoMenuVO
	 */
	public PermissaoAcessoMenuVO getPermissaoAcessoMenuVO() {
		if (permissaoAcessoMenuVO == null) {
			permissaoAcessoMenuVO = new PermissaoAcessoMenuVO();
		}
		return permissaoAcessoMenuVO;
	}

	/**
	 * @param permissaoAcessoMenuVO the permissaoAcessoMenuVO to set
	 */
	public void setPermissaoAcessoMenuVO(PermissaoAcessoMenuVO permissaoAcessoMenuVO) {
		this.permissaoAcessoMenuVO = permissaoAcessoMenuVO;
	}

	/**
	 * @return the menuPerfilTurma
	 */
	public Boolean getMenuPerfilTurma() {
		if (menuPerfilTurma == null) {
			menuPerfilTurma = Boolean.FALSE;
		}
		return menuPerfilTurma;
	}

	/**
	 * @param menuPerfilTurma the menuPerfilTurma to set
	 */
	public void setMenuPerfilTurma(Boolean menuPerfilTurma) {
		this.menuPerfilTurma = menuPerfilTurma;
	}

	public Boolean getApresentarIconeRelatorio() {
		if (apresentarIconeRelatorio == null) {
			apresentarIconeRelatorio = Boolean.TRUE;
		}
		return apresentarIconeRelatorio;
	}

	public void setApresentarIconeRelatorio(Boolean apresentarIconeRelatorio) {
		this.apresentarIconeRelatorio = apresentarIconeRelatorio;
	}

	public Integer getQtdeAtualizacaoForum() {
		qtdeAtualizacaoForum = getFacadeFactory().getForumFacade().consultarQtdeAtualizacaoForumPorUsuarioProfessor(0,
				getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
		if (qtdeAtualizacaoForum >= 100) {
			qtdeAtualizacaoForum = 99;
		}
		return qtdeAtualizacaoForum;
	}

	public void setQtdeAtualizacaoForum(Integer qtdeAtualizacaoForum) {
		this.qtdeAtualizacaoForum = qtdeAtualizacaoForum;
	}

	public Integer getQtdeAtualizacaoDuvidaProfessor() {
		if (qtdeAtualizacaoDuvidaProfessor == null) {
			qtdeAtualizacaoDuvidaProfessor = getFacadeFactory().getDuvidaProfessorFacade()
					.consultarQtdeAtualizacaoDuvidaPorUsuarioProfessor(getUsuarioLogado(),
							getUnidadeEnsinoLogado().getCodigo(), getConfiguracaoGeralPadraoSistema());
			if (qtdeAtualizacaoDuvidaProfessor >= 100) {
				qtdeAtualizacaoDuvidaProfessor = 99;
			}
		}
		return qtdeAtualizacaoDuvidaProfessor;
	}

	public void setQtdeAtualizacaoDuvidaProfessor(Integer qtdeAtualizacaoDuvidaProfessor) {
		this.qtdeAtualizacaoDuvidaProfessor = qtdeAtualizacaoDuvidaProfessor;
	}

	public Boolean getApresentarAdicionarFormacao() {
		if (apresentarAdicionarFormacao == null) {
			apresentarAdicionarFormacao = Boolean.FALSE;
		}
		return apresentarAdicionarFormacao;
	}

	public void setApresentarAdicionarFormacao(Boolean apresentarAdicionarFormacao) {
		this.apresentarAdicionarFormacao = apresentarAdicionarFormacao;
	}

	public Boolean getApresentarMenuTcc() {
		if (apresentarMenuTcc == null) {
			apresentarMenuTcc = Boolean.FALSE;
		}
		return apresentarMenuTcc;
	}

	public void setApresentarMenuTcc(Boolean apresentarMenuTcc) {
		this.apresentarMenuTcc = apresentarMenuTcc;
	}

	public String inicializarAdvertenciaAlunos() {
		// montarListaSelectItemTurmaProfessor();
		setAdvertenciaVO(new AdvertenciaVO());
		setAdvertenciaVOs(new ArrayList<AdvertenciaVO>(0));
		// setTurmaVO(new TurmaVO());
		// setDisciplinaVO(new DisciplinaVO());
		return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaVisaoProfessor.xhtml");
	}

	public List consultarTurmaPorProfessorAdvertencia() {
		try {
			if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo()
					.intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade()
						.consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(
								getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(),
								Uteis.getData(new Date(), "yyyy"), getBuscarTurmasAnteriores(), "AT", 0,
								getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, false);
			} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo()
					.intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade()
						.consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(
								getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(),
								Uteis.getData(new Date(), "yyyy"), getBuscarTurmasAnteriores(), "AT", 0,
								getUsuarioLogado().getVisaoLogar().equals("professor"), true, false, false);
			} else {
				return getFacadeFactory().getTurmaFacade()
						.consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(
								getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(),
								Uteis.getData(new Date(), "yyyy"), getBuscarTurmasAnteriores(), "AT", 0,
								getUsuarioLogado().getVisaoLogar().equals("professor"), false, false, false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return null;
	}

	public void montarListaSelectItemTurmaProfessor() {
		List<Integer> mapAuxiliarSelectItem = new ArrayList<>();
		setTurmaVO(new TurmaVO());
		getListaSelectItemTurmaProfessor().clear();
		getListaSelectItemTurmaProfessor().add(new SelectItem(0, ""));
		List<TurmaVO> listaTurmas = consultarTurmaPorProfessorAdvertencia();
		for (TurmaVO turmaVO : listaTurmas) {
			if (!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())) {
				getListaSelectItemTurmaProfessor()
						.add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
				mapAuxiliarSelectItem.add(turmaVO.getCodigo());
			}
		}
		getListaSelectItemDisciplinasTurma().clear();
	}

	public void montarListaSelectItemDisciplinaTurmaProfessor() {
		try {
			List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade()
					.consultarDisciplinaDoProfessorEAD(getUsuarioLogado().getPessoa().getCodigo(),
							getUnidadeEnsinoLogado().getCodigo(), 0, getTurmaVO().getCodigo(),
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(disciplinaVOs, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List consultarAdvertenciaPorTurma() throws Exception {
		getAdvertenciaVOs().clear();
		setAdvertenciaVOs(getFacadeFactory().getAdvertenciaFacade().consultarAdvertenciaPorTurmaVisaoProfessor(
				getAdvertenciaVO().getMatriculaVO().getMatricula(),
				getAdvertenciaVO().getMatriculaVO().getAluno().getNome(), getUsuarioLogado().getPessoa().getCodigo(),
				getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
		try {
			if (getAdvertenciaVOs().isEmpty()) {
				throw new Exception("Aluno " + getAdvertenciaVO().getMatriculaVO().getAluno().getNome()
						+ " não encontrado. Verifique se o número de matrícula ou nome está correto e se o aluno está matriculado em sua Disciplina.");
			}
			setMensagemID("msg_dados_consultados");
			return getAdvertenciaVOs();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return advertenciaVOs;
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

	public boolean getIsBloquearSemestreAno() throws Exception {
		if (getUsuarioLogado().getVisaoLogar().equals("professor")
				|| getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			return !getLoginControle().getPermissaoAcessoMenuVO().getPermitirConsultarPlanoEnsinoAnterior();
		} else {
			return false;
		}
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

	public String getCampoDisciplinaTurma() {
		if (campoDisciplinaTurma == null) {
			campoDisciplinaTurma = "";
		}
		return campoDisciplinaTurma;
	}

	public void setCampoDisciplinaTurma(String campoDisciplinaTurma) {
		this.campoDisciplinaTurma = campoDisciplinaTurma;
	}

	public void carregarDadosTurma() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(),
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsApresentarAno() {
		if ((getUsuarioLogado().getVisaoLogar().equals("professor")
				|| getUsuarioLogado().getVisaoLogar().equals("coordenador"))
				&& Uteis.isAtributoPreenchido(getTurmaVO())) {
			if (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) {
				if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo()) {
					return true;
				} else {
					setAno(Uteis.getAnoDataAtual());
					return false;
				}
			}
			setAno("");
			return false;
		}
		return true;
	}

	public boolean getIsApresentarSemestre() {
		if ((getUsuarioLogado().getVisaoLogar().equals("professor")
				|| getUsuarioLogado().getVisaoLogar().equals("coordenador"))
				&& Uteis.isAtributoPreenchido(getTurmaVO())) {
			if (getTurmaVO().getSemestral()) {
				if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirLancarNotaRetroativo()) {
					return true;
				} else {
					setSemestre(Uteis.getSemestreAtual());
					return false;
				}
			}
			setSemestre("");
			return false;
		}
		return true;
	}

	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual4Digitos();
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

	public List<SelectItem> listaSelectItemSemestre;

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("", ""));
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
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

	public List<SelectItem> getListaSelectItemTurmaProfessor() {
		if (listaSelectItemTurmaProfessor == null) {
			listaSelectItemTurmaProfessor = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurmaProfessor;
	}

	public void setListaSelectItemTurmaProfessor(List listaSelectItemTurmaProfessor) {
		this.listaSelectItemTurmaProfessor = listaSelectItemTurmaProfessor;
	}

	public List<AdvertenciaVO> getAdvertenciaVOs() {
		if (advertenciaVOs == null) {
			advertenciaVOs = new ArrayList<AdvertenciaVO>(0);
		}
		return advertenciaVOs;
	}

	public void setAdvertenciaVOs(List<AdvertenciaVO> advertenciaVOs) {
		this.advertenciaVOs = advertenciaVOs;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Nome Curso"));
		return itens;
	}

	public AdvertenciaVO getAdvertenciaVO() {
		if (advertenciaVO == null) {
			advertenciaVO = new AdvertenciaVO();
		}
		return advertenciaVO;
	}

	public void setAdvertenciaVO(AdvertenciaVO advertenciaVO) {
		this.advertenciaVO = advertenciaVO;
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
					getAdvertenciaVO().getMatriculaVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(),
					NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getAdvertenciaVO().getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getAdvertenciaVO().setMatriculaVO(objAluno);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() throws Exception {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(
						getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(),
						getUsuarioLogado());
				if (!matriculaVO.getMatricula().equals("")) {
					objs.add(matriculaVO);
				} else {
					removerObjetoMemoria(matriculaVO);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		getAdvertenciaVO().setMatriculaVO(matriculaVO);
		setCampoConsultaAluno("");
		setValorConsultaAluno("");
		setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
	}

	public void limparDadosAluno() throws Exception {
		getAdvertenciaVO().setMatriculaVO(new MatriculaVO());
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

	public Integer getQtdeInteracoesAlunosAtividadeDiscursiva() {
		if (qtdeInteracoesAlunosAtividadeDiscursiva == null) {
			qtdeInteracoesAlunosAtividadeDiscursiva = 0;
		}
		return qtdeInteracoesAlunosAtividadeDiscursiva;
	}

	public void setQtdeInteracoesAlunosAtividadeDiscursiva(Integer qtdeInteracoesAlunosAtividadeDiscursiva) {
		this.qtdeInteracoesAlunosAtividadeDiscursiva = qtdeInteracoesAlunosAtividadeDiscursiva;
	}

	
	public void consultarQtdeInteracaoAtividadeDiscursivaProfessor() {
		try {
			setQtdeInteracoesAlunosAtividadeDiscursiva(getFacadeFactory()
					.getAtividadeDiscursivaInteracaoInterfaceFacade().consultarQtdeInteracoesAlunosPorCodigoProfessor(
							getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			setQtdeInteracoesAlunosAtividadeDiscursiva(0);
		}
	}

	private CalendarioHorarioAulaVO<DataEventosRSVO> calendarioDataEventoRsVO;
	private DataEventosRSVO dataEventosRSVO;
	private AgendaAlunoRSVO agendaAlunoRSVO;
	private boolean permitirGeracaoEventoGoogleMeet = false;
	private boolean permitirGeracaoEventoClassroom= false;

	private String modalCalendario;

	public CalendarioHorarioAulaVO<DataEventosRSVO> getCalendarioDataEventoRsVO() {
		if (calendarioDataEventoRsVO == null) {
			calendarioDataEventoRsVO = new CalendarioHorarioAulaVO<DataEventosRSVO>();
		}
		return calendarioDataEventoRsVO;
	}

	public void setCalendarioDataEventoRsVO(CalendarioHorarioAulaVO<DataEventosRSVO> calendarioDataEventoRsVO) {
		this.calendarioDataEventoRsVO = calendarioDataEventoRsVO;
	}

	public DataEventosRSVO getDataEventosRSVO() {
		if (dataEventosRSVO == null) {
			dataEventosRSVO = new DataEventosRSVO();
		}
		return dataEventosRSVO;
	}

	public void setDataEventosRSVO(DataEventosRSVO dataEventosRSVO) {
		this.dataEventosRSVO = dataEventosRSVO;
	}

	public AgendaAlunoRSVO getAgendaAlunoRSVO() {
		if (agendaAlunoRSVO == null) {
			agendaAlunoRSVO = new AgendaAlunoRSVO();
		}
		return agendaAlunoRSVO;
	}

	public void setAgendaAlunoRSVO(AgendaAlunoRSVO agendaAlunoRSVO) {
		this.agendaAlunoRSVO = agendaAlunoRSVO;
	}

	public String getModalCalendario() {
		if (modalCalendario == null) {
			modalCalendario = "";
		}
		return modalCalendario;
	}

	public void setModalCalendario(String modalCalendario) {
		this.modalCalendario = modalCalendario;
	}

	public void isValidarPermissaoGeracaoEventoGoogleMeet() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
					PerfilAcessoPermissaoVisaoProfessorEnum.PERMITIR_GERACAO_EVENTO_ONLINE_GOOGLE_MEET,
					getUsuarioLogadoClone());
			setPermitirGeracaoEventoGoogleMeet(true);
		} catch (Exception e) {
			setPermitirGeracaoEventoGoogleMeet(false);
		}
	}

	public boolean isPermitirGeracaoEventoGoogleMeet() {
		return permitirGeracaoEventoGoogleMeet;
	}

	public void setPermitirGeracaoEventoGoogleMeet(boolean permitirGeracaoEventoGoogleMeet) {
		this.permitirGeracaoEventoGoogleMeet = permitirGeracaoEventoGoogleMeet;
	}
	
	public void isValidarPermissaoGeracaoEventoClassroom() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoProfessorEnum.PERMITIR_GERACAO_EVENTO_ONLINE_CLASSROOM,getUsuarioLogadoClone());
			setPermitirGeracaoEventoClassroom(true);
		} catch (Exception e) {
			setPermitirGeracaoEventoClassroom(false);
		}
	}
	

	public boolean isPermitirGeracaoEventoClassroom() {
		return permitirGeracaoEventoClassroom;
	}

	public void setPermitirGeracaoEventoClassroom(boolean permitirGeracaoEventoClassroom) {
		this.permitirGeracaoEventoClassroom = permitirGeracaoEventoClassroom;
	}

	public void realizarInicializacaoCalendarioProfessor() {
		try {
			Date dataBase = new Date();
			consultarCalendarioAluno(MesAnoEnum.getMesData(dataBase), Uteis.getAnoData(dataBase));
			if (getCalendarioDataEventoRsVO().getObjetoSelecionado() != null
					&& getCalendarioDataEventoRsVO().getObjetoSelecionado() instanceof DataEventosRSVO) {
				setDataEventosRSVO((DataEventosRSVO) getCalendarioDataEventoRsVO().getObjetoSelecionado());
				if (getDataEventosRSVO().isCssHorarioRegistroLancado() || getDataEventosRSVO().isCssHorarioFeriado()) {
					getFacadeFactory().getHorarioProfessorFacade().realizarGeracaoItemCalendarioProfessor(getDataEventosRSVO(), getPessoaVO().getCodigo(), 0, 0, 0, 0, true, false,new FuncionarioCargoVO(), getUsuarioLogadoClone());
				}
				getDataEventosRSVO().setStyleClass("colunaHorarioSelecionada");
			}
			isValidarPermissaoGeracaoEventoGoogleMeet();
			isValidarPermissaoGeracaoEventoClassroom();
			isPermiteConfiguracaoSeiGsuite();
			isPermiteConfiguracaoSeiBlackboard();
			setMensagemID("", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCalendarioAluno(MesAnoEnum mesAnoEnum, Integer ano) throws Exception {
		setCalendarioDataEventoRsVO(getFacadeFactory().getHorarioProfessorFacade().realizarGeracaoCalendarioProfessor(getPessoaVO().getCodigo(), 0, 0, 0, 0, true, "", false, new FuncionarioCargoVO(), mesAnoEnum, ano,getUsuarioLogadoClone()));
		setDataEventosRSVO(new DataEventosRSVO());
		setAgendaAlunoRSVO(new AgendaAlunoRSVO());
	}

	public void visualizarCalendarioProximoMes() {
		try {
			if (getCalendarioDataEventoRsVO().getMesAno().getMesAnoPosterior().equals(MesAnoEnum.JANEIRO)) {
				consultarCalendarioAluno(getCalendarioDataEventoRsVO().getMesAno().getMesAnoPosterior(),Integer.parseInt(getCalendarioDataEventoRsVO().getAno()) + 1);
			} else {
				consultarCalendarioAluno(getCalendarioDataEventoRsVO().getMesAno().getMesAnoPosterior(),Integer.parseInt(getCalendarioDataEventoRsVO().getAno()));
			}
			setMensagemID("", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarCalendarioAnteriorMes() {
		try {
			if (getCalendarioDataEventoRsVO().getMesAno().getMesAnoAnterior().equals(MesAnoEnum.DEZEMBRO)) {
				consultarCalendarioAluno(getCalendarioDataEventoRsVO().getMesAno().getMesAnoAnterior(),	Integer.parseInt(getCalendarioDataEventoRsVO().getAno()) - 1);
			} else {
				consultarCalendarioAluno(getCalendarioDataEventoRsVO().getMesAno().getMesAnoAnterior(), Integer.parseInt(getCalendarioDataEventoRsVO().getAno()));
			}
			setMensagemID("", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarDiaCalendario() {
		try {
			DataEventosRSVO dataEventoRSVO = (DataEventosRSVO) context().getExternalContext().getRequestMap().get("diaAgendaItens");
			if (getDataEventosRSVO().getAgendaAlunoRSVOs().isEmpty()) {
				getDataEventosRSVO().setStyleClass("colunaHorarioLivre");
			} else if (!getDataEventosRSVO().getAgendaAlunoRSVOs().isEmpty() && getDataEventosRSVO().getAgendaAlunoRSVOs().stream().allMatch(p -> p.getOrigemAgendaAluno().isFeriado())) {
				getDataEventosRSVO().setStyleClass("horarioFeriado");
			} else if (!getDataEventosRSVO().getAgendaAlunoRSVOs().isEmpty()) {
				getDataEventosRSVO().setStyleClass("horarioRegistroLancado");
			}
			setDataEventosRSVO(dataEventoRSVO);
			if (getDataEventosRSVO() != null && (getDataEventosRSVO().isCssHorarioRegistroLancado() || getDataEventosRSVO().isCssHorarioFeriado())) {
				getFacadeFactory().getHorarioProfessorFacade().realizarGeracaoItemCalendarioProfessor(getDataEventosRSVO(), getPessoaVO().getCodigo(), 0, 0, 0, 0, true, false,	new FuncionarioCargoVO(), getUsuarioLogadoClone());
			}
			getDataEventosRSVO().setStyleClass("colunaHorarioSelecionada");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarInicializacaoClassroom() {
		try {
			AgendaAlunoRSVO agendaAlunoRSVO = (AgendaAlunoRSVO) context().getExternalContext().getRequestMap().get("agendaAlunoRSVOItem");
			setAgendaAlunoRSVO(agendaAlunoRSVO);
			if (!Uteis.isAtributoPreenchido(getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO())) {
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setAno(getAgendaAlunoRSVO().getHorarioProfessorDiaItemVO().getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().getAnoVigente());
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setSemestre(getAgendaAlunoRSVO().getHorarioProfessorDiaItemVO().getHorarioTurmaDiaItemVO().getHorarioTurmaDiaVO().getHorarioTurma().getSemestreVigente());
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setTurmaVO(getAgendaAlunoRSVO().getGoogleMeetVO().getTurmaVO());
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setDisciplinaVO(getAgendaAlunoRSVO().getGoogleMeetVO().getDisciplinaVO());
			}
			setModalCalendario("RichFaces.$('panelClassroomGoogle').show();");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setModalCalendario("RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarVisualizacaoClassroomTutoriaOnline() {
		try {
			TutoriaOnlineSalaAulaIntegracaoVO obj = (TutoriaOnlineSalaAulaIntegracaoVO) context().getExternalContext().getRequestMap().get("tutoriaOnlineSalaAulaIntegracao");
			if(Uteis.isAtributoPreenchido(obj.getClassroomGoogleVO())) {
				getAgendaAlunoRSVO().getGoogleMeetVO().setClassroomGoogleVO(obj.getClassroomGoogleVO());	
			}else {
				getAgendaAlunoRSVO().getGoogleMeetVO().setClassroomGoogleVO(new ClassroomGoogleVO());
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setTurmaVO(obj.getTurmaVO());
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setDisciplinaVO(obj.getDisciplinaVO());
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setAno(obj.getAno());
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setSemestre(obj.getSemestre());
				getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setProfessorEad(obj.getProfessorEad());
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setModalCalendario("RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarGeracaoClassroom() {
		try {
			getAgendaAlunoRSVO().getGoogleMeetVO().setClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().realizarGeracaoClassroomGoogle(getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO(), getUsuarioLogadoClone()));
			if (Uteis.isAtributoPreenchido(getAgendaAlunoRSVO().getGoogleMeetVO().getTurmaVO().getCodigo())) {
				realizarAtualizacaoClassroomDataEvento();
			} else {
				realizarInicializacaoTutoriaOnline();
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setModalCalendario("RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void realizarAtualizacaoClassroom() {
		try {
			getAgendaAlunoRSVO().getGoogleMeetVO().setClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().realizarRevisaoClassroom(getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO(), getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_atualizados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void excluirClassroom() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarExclusaoClassroomGoogle(getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO(), getUsuarioLogadoClone());
			getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setCodigo(0);
			getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO().setLinkClassroom("");
			if (Uteis.isAtributoPreenchido(getAgendaAlunoRSVO().getGoogleMeetVO().getTurmaVO().getCodigo())) {
				getFacadeFactory().getHorarioProfessorFacade().realizarGeracaoItemCalendarioProfessor(getDataEventosRSVO(), getPessoaVO().getCodigo(), 0, 0, 0, 0, true, false,	new FuncionarioCargoVO(), getUsuarioLogadoClone());
			} else {
				realizarInicializacaoTutoriaOnline();
			}
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void realizarAtualizacaoClassroomDataEvento() {
		for (AgendaAlunoRSVO agendaExistente : getDataEventosRSVO().getAgendaAlunoRSVOs()) {
			if (getAgendaAlunoRSVO().getGoogleMeetVO().getTurmaVO().getCodigo().equals(agendaExistente.getGoogleMeetVO().getTurmaVO().getCodigo())
					&& getAgendaAlunoRSVO().getGoogleMeetVO().getDisciplinaVO().getCodigo().equals(agendaExistente.getGoogleMeetVO().getDisciplinaVO().getCodigo())) {
				agendaExistente.getGoogleMeetVO().setClassroomGoogleVO(getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO());
			}
		}
	}

	public String realizarNavegacaoParaEventoCalendario() {
		String retorno = "";
		try {
			AgendaAlunoRSVO agendaAlunoRSVO = (AgendaAlunoRSVO) context().getExternalContext().getRequestMap().get("agendaAlunoRSVOItem");
			if (agendaAlunoRSVO.isEventoHorarioAula()) {
				setAgendaAlunoRSVO(agendaAlunoRSVO);
				if (getAgendaAlunoRSVO().getGoogleMeetVO().isGoogleMeetAvulso()) {
					setAno(getAgendaAlunoRSVO().getGoogleMeetVO().getAno());
					setSemestre(getAgendaAlunoRSVO().getGoogleMeetVO().getSemestre());
					montarListaSelectItemTurmaGoogleMeet();
					setTurmaVO(getAgendaAlunoRSVO().getGoogleMeetVO().getTurmaVO());
					preencherDadosTurmaGoogleMeet();
					setDisciplinaVO(getAgendaAlunoRSVO().getGoogleMeetVO().getDisciplinaVO());
				}
				setModalCalendario("RichFaces.$('panelGoogleMeet').show();");
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setModalCalendario("RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(retorno);
	}

	public void realizarNovoGoogleMeetAvulso() {
		try {
			setAgendaAlunoRSVO(new AgendaAlunoRSVO());
			getAgendaAlunoRSVO().setGoogleMeetVO(new GoogleMeetVO());
			setTurmaVO(new TurmaVO());
			setDisciplinaVO(new DisciplinaVO());
			getAgendaAlunoRSVO().getGoogleMeetVO().setGoogleMeetAvulso(true);
			montarListaSelectItemTurmaGoogleMeet();
			setModalCalendario("RichFaces.$('panelGoogleMeet').show();");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setModalCalendario("RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarNovoGoogleMeetPorTutoriaOnline() {
		try {
			TutoriaOnlineSalaAulaIntegracaoVO obj = (TutoriaOnlineSalaAulaIntegracaoVO) context().getExternalContext().getRequestMap().get("tutoriaOnlineSalaAulaIntegracao");
			setAgendaAlunoRSVO(new AgendaAlunoRSVO());
			getAgendaAlunoRSVO().setGoogleMeetVO(new GoogleMeetVO());
			getAgendaAlunoRSVO().getGoogleMeetVO().setGoogleMeetAvulso(true);
			montarListaSelectItemTurmaGoogleMeet();
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(),NivelMontarDados.BASICO, getUsuarioLogadoClone()));
			montarListaSelectItemDisciplinaTurmaProfessor();
			setDisciplinaVO(obj.getDisciplinaVO());
			setAno(obj.getAno());
			setSemestre(obj.getSemestre());
			setModalCalendario("RichFaces.$('panelGoogleMeet').show();");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setModalCalendario("RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurmaGoogleMeet() throws Exception {
		List<Integer> mapAuxiliarSelectItem = new ArrayList<>();
		setTurmaVO(new TurmaVO());
		getListaSelectItemTurmaProfessor().clear();
		getListaSelectItemTurmaProfessor().add(new SelectItem(0, ""));
		List<TurmaVO> listaTurmas = getFacadeFactory().getTurmaFacade().consultarTurmasEADProfessor(
				getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), 0, null);
		for (TurmaVO turmaVO : listaTurmas) {
			if (!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())) {
				getListaSelectItemTurmaProfessor()
						.add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
				mapAuxiliarSelectItem.add(turmaVO.getCodigo());
			}
		}
		getListaSelectItemDisciplinasTurma().clear();
	}

	public void preencherDadosTurmaGoogleMeet() {
		try {
			if (Uteis.isAtributoPreenchido(getTurmaVO())) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(),NivelMontarDados.BASICO, getUsuarioLogado()));
				montarListaSelectItemDisciplinaTurmaProfessor();
			} else {
				getListaSelectItemDisciplinasTurma().clear();
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void persistirGoogleMeet() {
		try {
			if (getAgendaAlunoRSVO().getGoogleMeetVO().isGoogleMeetAvulso()) {
				getAgendaAlunoRSVO().setGoogleMeetVO(getFacadeFactory().getGoogleMeetInterfaceFacade()
						.realizarPersistenciaGoogleMeetAvulso(getAgendaAlunoRSVO().getGoogleMeetVO(), getTurmaVO(),
								getAno(), getSemestre(), getDisciplinaVO(),
								getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone()));
			} else {
				getFacadeFactory().getGoogleMeetInterfaceFacade().realizarGeracaoEventoGoogleMeetVisaoProfessor(
						getDataEventosRSVO(), getAgendaAlunoRSVO(),
						getAgendaAlunoRSVO().getGoogleMeetVO().isEventoAulasSubsequentes(),
						getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			}
			setModalCalendario("");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setModalCalendario("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void fecharModalGoogleMeet() {
		try {
			if (Uteis.isAtributoPreenchido(getAgendaAlunoRSVO().getGoogleMeetVO())
					&& getAgendaAlunoRSVO().getGoogleMeetVO().isGoogleMeetAvulso()) {
				realizarInicializacaoCalendarioProfessor();
			} else {
				getFacadeFactory().getHorarioProfessorFacade().realizarGeracaoItemCalendarioProfessor(
						getDataEventosRSVO(), getPessoaVO().getCodigo(), 0, 0, 0, 0, true, false,
						new FuncionarioCargoVO(), getUsuarioLogadoClone());
			}

			setAgendaAlunoRSVO(new AgendaAlunoRSVO());
			setTurmaVO(new TurmaVO());
			setDisciplinaVO(new DisciplinaVO());
			setAno("");
			setSemestre("");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarExclusaoGoogleMeet() {
		try {
			if (getAgendaAlunoRSVO().getGoogleMeetVO().isGoogleMeetAvulso()) {
				getFacadeFactory().getGoogleMeetInterfaceFacade()
						.realizarExclusaoGoogleMeet(getAgendaAlunoRSVO().getGoogleMeetVO(), getUsuarioLogadoClone());
			} else {
				getFacadeFactory().getGoogleMeetInterfaceFacade().realizarExclusaoEventoGoogleMeetVisaoProfessor(
						getDataEventosRSVO(), getAgendaAlunoRSVO(),
						getAgendaAlunoRSVO().getGoogleMeetVO().isEventoAulasSubsequentes(), getUsuarioLogadoClone());
			}
			getFacadeFactory().getHorarioProfessorFacade().realizarGeracaoItemCalendarioProfessor(getDataEventosRSVO(),
					getPessoaVO().getCodigo(), 0, 0, 0, 0, true, false, new FuncionarioCargoVO(),
					getUsuarioLogadoClone());
			setAgendaAlunoRSVO(new AgendaAlunoRSVO());
			setModalCalendario(
					"RichFaces.$('panelExcluirGoogleMeet').hide();RichFaces.$('panelGoogleMeet').hide();RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setModalCalendario("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarBuscaAlunoClassroom() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarBuscaAlunoClassroom(getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO(),getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarEnvioConviteAlunoClassroom() {
		try {
			PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("classroomStudentVOItens");
			getFacadeFactory().getClassroomGoogleFacade().realizarEnvioConviteAlunoClassroom(getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO(), obj, getUsuarioLogadoClone());
			setModalCalendario("");
		} catch (Exception e) {
			setModalCalendario("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoAlunoClassroom() {
		try {
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getClassroomGoogleFacade().realizarAtualizacaoAlunoClassroom(getAgendaAlunoRSVO().getGoogleMeetVO().getClassroomGoogleVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarInicializacaoTutoriaOnline() {
		try {
			if (isExisteConfiguracaoSeiGsuite() || isExisteConfiguracaoSeiBlackboard()) {
				setAno(Uteis.getAnoDataAtual());
				setSemestre(Uteis.getSemestreAtual());				
				consultarTutoriaOnlinePorIntegracao();
				if (getProgramacaoTutoriaOnlineProfessorVO().isExisteTutoriaOnlineIntegracao()) {
					dashboardTutoriaOnline = new DashboardVO(TipoDashboardEnum.TUTORIA_ONLINE, true, 3, TipoVisaoEnum.PROFESSOR, getUsuarioLogadoClone());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTutoriaOnlinePorIntegracao() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getAno()),"O campo Ano deve ser informa para consulta de Classroom Tutoria Online.");
			Uteis.checkState(getAno().length() < 4, "O campo Ano não esta no formado correto AAAA.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getSemestre()),"O campo Semestre deve ser informa para consulta de Classroom Tutoria Online.");
			getProgramacaoTutoriaOnlineProfessorVO().setListaClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().consultarClassroomGoogleTutoriaOnline(getPessoaVO(),getAno(), getSemestre(), getUsuarioLogadoClone()));
			getProgramacaoTutoriaOnlineProfessorVO().setListaSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardTutoriaOnline(getPessoaVO(),getAno(), getSemestre(), getUsuarioLogadoClone()));
			getProgramacaoTutoriaOnlineProfessorVO().carregarTutoriaOnlineSalaAulaIntegracao();
			setModalCalendario("");
		} catch (Exception e) {
			setModalCalendario("RichFaces.$('panelMensagemGoogleMeet').show();");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void registrarPosicaoDashboard(DropEvent dropEvent) {
		DashboardVO drag = (DashboardVO) dropEvent.getDragValue();
		DashboardVO drop = (DashboardVO) dropEvent.getDropValue();
		if (drag != null && drop != null) {
			Integer ordemDrag = drag.getOrdem();
			drag.setOrdem(drop.getOrdem());
			drop.setOrdem(ordemDrag);
			persistirDashboard(drag);
			persistirDashboard(drop);
		}
	}

	public void persistirDashboard(DashboardVO dashboardVO) {
		try {
			getFacadeFactory().getDashboardInterfaceFacade().persistir(dashboardVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	
	public void consultarConfiguracaoDashboard() {
		try {
			List<DashboardVO> dashboards = getFacadeFactory().getDashboardInterfaceFacade()
					.consultarDashboardPorUsuarioAmbiente(getUsuarioLogado(), TipoVisaoEnum.PROFESSOR);
			if (dashboards != null) {
				dashboards.forEach(d -> {
					switch (d.getTipoDashboard()) {
					case BANNER_MATRICULA:
						setDashboardMatricular(d);
						break;

					case HORARIO_PROFESSOR:
						setDashboardCalendario(d);
						break;
					case TUTORIA_ONLINE:
						setDashboardTutoriaOnline(d);
						break;

					case PENDENCIA_PROFESSOR:
						setDashboardPendencia(d);
						break;
					case TWITTER:
						setDashboardTwitter(d);
						break;
					case MARKETING:
						setDashboardBannerMarketing(d);
						break;
					case FAVORITOS:
						setDashboardFavorito(d);
						break;
					case LINK_UTEIS:
						getLoginControle().setDashboardLinksUteis(d);
						break;	
					}
				});
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public DashboardVO getDashboardPendencia() {
		if (dashboardPendencia == null) {
			dashboardPendencia = new DashboardVO(TipoDashboardEnum.PENDENCIA_PROFESSOR, false, 4,
					TipoVisaoEnum.PROFESSOR, getUsuarioLogadoClone());
		}
		return dashboardPendencia;
	}

	public void setDashboardPendencia(DashboardVO dashboardPendencia) {
		this.dashboardPendencia = dashboardPendencia;
	}

	public DashboardVO getDashboardCalendario() {
		if (dashboardCalendario == null) {
			dashboardCalendario = new DashboardVO(TipoDashboardEnum.HORARIO_PROFESSOR,
					!getLoginControle().getPermissaoAcessoMenuVO().getMeusHorarios(), 5, TipoVisaoEnum.PROFESSOR,
					getUsuarioLogadoClone());
		}
		return dashboardCalendario;
	}

	public void setDashboardCalendario(DashboardVO dashboardCalendario) {
		this.dashboardCalendario = dashboardCalendario;
	}

	public DashboardVO getDashboardTutoriaOnline() {
		if (dashboardTutoriaOnline == null) {
			dashboardTutoriaOnline = new DashboardVO(TipoDashboardEnum.TUTORIA_ONLINE, !getProgramacaoTutoriaOnlineProfessorVO().isExisteTutoriaOnlineIntegracao() , 6, TipoVisaoEnum.PROFESSOR, getUsuarioLogadoClone());
		}
		return dashboardTutoriaOnline;
	}

	public void setDashboardTutoriaOnline(DashboardVO dashboardClassroomTutoriaOnline) {
		this.dashboardTutoriaOnline = dashboardClassroomTutoriaOnline;
	}

	public void isPermiteConfiguracaoSeiGsuite() {
		try {
			setExisteConfiguracaoSeiGsuite(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarSeExisteConfiguracaoSeiGsuitePadrao(getUsuarioLogadoClone()));
		} catch (Exception e) {
			setExisteConfiguracaoSeiGsuite(false);
		}
	}
	
	public void isPermiteConfiguracaoSeiBlackboard() {
		try {
			setExisteConfiguracaoSeiBlackboard(getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarSeExisteConfiguracaoSeiBlackboardPadrao(getUsuarioLogadoClone()));
		} catch (Exception e) {
			setExisteConfiguracaoSeiBlackboard(false);
		}
	}

	public void inicializarBanner() {
		try {
//			getLoginControle().inicializarBanner();
//			StringBuilder html = new StringBuilder();
//			String url = "";
//			int x = 0;
//			List<PoliticaDivulgacaoMatriculaOnlineVO> politicaDivulgacaoMatriculaOnlineVOs;
//			politicaDivulgacaoMatriculaOnlineVOs = getFacadeFactory()
//					.getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarBanners(getUsuarioLogadoClone());
//			for (PoliticaDivulgacaoMatriculaOnlineVO politica : politicaDivulgacaoMatriculaOnlineVOs) {
//				url = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
//						+ politica.getCaminhoBaseLogo().replaceAll("\\\\", "/").trim() + "/"
//						+ politica.getNomeArquivoLogo().replaceAll("\\\\", "/").trim();
////				html.append("<div class=\"col-md-12 text-center\">");
//				html.append("<a href=\"" + UteisJSF.getUrlAplicacaoExterna()
//						+ "/visaoProfessor/matriculaOnlineVisaoProfessorForm.xhtml?banner=" + politica.getCodigo()
//						+ "\" >");
//				html.append(" 	<img src=\"").append(url).append("\" height=\"290px\"   title=\"" + politica.getNome() + "\"  />");
//				html.append("</a>");
////				html.append("</div>");
//			}
//			setCaminhoArquivoBanner(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getPermitirAlterarUsername() {
		if (permitirAlterarUsername == null) {
			permitirAlterarUsername = Boolean.FALSE;
		}
		return permitirAlterarUsername;
	}

	public void setPermitirAlterarUsername(Boolean permitirAlterarUsername) {
		this.permitirAlterarUsername = permitirAlterarUsername;
	}

	/**
	 * @return the qtdeAtualizacaoRequerimentoProfessor
	 */
	public Integer getQtdeAtualizacaoRequerimentoProfessor() {
		if (qtdeAtualizacaoRequerimentoProfessor == null) {
			qtdeAtualizacaoRequerimentoProfessor = 0;
		}
		return qtdeAtualizacaoRequerimentoProfessor;
	}

	/**
	 * @param qtdeAtualizacaoRequerimentoProfessor the
	 *                                             qtdeAtualizacaoRequerimentoProfessor
	 *                                             to set
	 */
	public void setQtdeAtualizacaoRequerimentoProfessor(Integer qtdeAtualizacaoRequerimentoProfessor) {
		this.qtdeAtualizacaoRequerimentoProfessor = qtdeAtualizacaoRequerimentoProfessor;
	}

	
	public void consultarQtdeAtualizacaoRequerimentoProfessor() {
		try {
			setQtdeAtualizacaoRequerimentoProfessor(getFacadeFactory().getRequerimentoFacade()
					.consultarQtdeRequerimentoAlunoVisaoProfessorCoordenador(getUsuarioLogadoClone()));
		} catch (Exception e) {
			setQtdeAtualizacaoRequerimentoProfessor(0);
		}
	}

	public Boolean getApresentarModalResetarSenha() {
		if (apresentarModalResetarSenha == null) {
			apresentarModalResetarSenha = Boolean.FALSE;
		}
		return apresentarModalResetarSenha;
	}

	public void setApresentarModalResetarSenha(Boolean apresentarModalResetarSenha) {
		this.apresentarModalResetarSenha = apresentarModalResetarSenha;
	}

	private String qrCode;

	public String getUrlMoodle() {
		if (urlMoodle == null) {
			urlMoodle = "";
		}
		return urlMoodle;
	}

	public void setUrlMoodle(String urlMoodle) {
		this.urlMoodle = urlMoodle;
	}

	public void montarQRCodeLoginAplicativo() throws Exception {
		AplicativoSEISV webServiceAplicativo = new AplicativoSEISV();
		QRCodeLoginRSVO qrCodeLoginRSVO = new QRCodeLoginRSVO();
		// qrCodeLoginRSVO.setCodigoUnidadeEnsino(getMatricula().getUnidadeEnsino().getCodigo());
		// qrCodeLoginRSVO.setNomeUnidadeEnsino(getMatricula().getUnidadeEnsino().getNome());
		// qrCodeLoginRSVO.setMatricula(getMatricula().getMatricula());
		// qrCodeLoginRSVO.setNomeAluno(getUsuarioLogado().getPessoa().getNome());
		// qrCodeLoginRSVO.setCodigoCurso(getMatricula().getCurso().getCodigo());
		// qrCodeLoginRSVO.setNomeCurso(getMatricula().getCurso().getNome());
		// qrCodeLoginRSVO.setTipoRecursoEducacional(getMatricula().getCurso().getNivelEducacional());
		// qrCodeLoginRSVO.setUrlFotoPerfilAluno(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getUsuarioLogado().getPessoa().getArquivoImagem(),
		// PastaBaseArquivoEnum.IMAGEM.getValue(),
		// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
		// "foto_usuario.jpg", false));
		qrCodeLoginRSVO.setUrlBaseWS(
				webServiceAplicativo.getCaminhoWeb(getUsuarioLogadoClone()) + "webservice/aplicativoSEISV");
		qrCodeLoginRSVO.setLogin(getUsuarioLogado().getUsername());
		qrCodeLoginRSVO.setSenha(getUsuarioLogado().getSenha());
		Gson convert = new Gson();
		setQrCode(convert.toJson(qrCodeLoginRSVO));
	}

	public String getQrCode() {
		if (qrCode == null) {
			qrCode = "";
		}
		return qrCode;
	}

	public void setQrCode(String qrCode) {

		this.qrCode = qrCode;
	}

	public boolean isOmitirQrCode() {
		return omitirQrCode;
	}

	public void setOmitirQrCode(boolean omitirQrCode) {
		this.omitirQrCode = omitirQrCode;
	}

	public void iniciarForum() {
		setQtdeAtualizacaoForum(null);
	}

	/**
	 * @return the qtdMensagemCaixaEntrada
	 */
	public Boolean getApresentarQtdMensagemCaixaEntrada() {
		if (getQtdMensagemCaixaEntrada() != 0) {
			return true;
		}
		return false;
	}

	
	public void inicializarQtdeCaixaEntradaUsuario() {
		try {
			setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade()
					.consultaRapidaComunicacaoInternaNaoLidas(getUsuarioLogado().getPessoa().getCodigo()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Integer qtdMensagemCaixaEntrada;

	public Integer getQtdMensagemCaixaEntrada() {
		if (qtdMensagemCaixaEntrada == null) {
			qtdMensagemCaixaEntrada = 0;
		}
		return qtdMensagemCaixaEntrada;
	}

	public String getQtdMensagemCaixaEntradaStr() {
		if (getQtdMensagemCaixaEntrada() > 99) {
			return "99";
		} else if (getQtdMensagemCaixaEntrada() < 10) {
			return "0" + getQtdMensagemCaixaEntrada().toString();
		}
		return getQtdMensagemCaixaEntrada().toString();
	}

	/**
	 * @param qtdMensagemCaixaEntrada the qtdMensagemCaixaEntrada to set
	 */
	public void setQtdMensagemCaixaEntrada(Integer qtdMensagemCaixaEntrada) {
		this.qtdMensagemCaixaEntrada = qtdMensagemCaixaEntrada;
	}
	

	public ProgramacaoTutoriaOnlineProfessorVO getProgramacaoTutoriaOnlineProfessorVO() {
		if (programacaoTutoriaOnlineProfessorVO == null) {
			programacaoTutoriaOnlineProfessorVO = new ProgramacaoTutoriaOnlineProfessorVO();
		}
		return programacaoTutoriaOnlineProfessorVO;
	}

	public void setProgramacaoTutoriaOnlineProfessorVO(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO) {
		this.programacaoTutoriaOnlineProfessorVO = programacaoTutoriaOnlineProfessorVO;
	}

	public boolean isAlterarSenhaContaGsuite() {
		return alterarSenhaContaGsuite;
	}

	public void setAlterarSenhaContaGsuite(boolean alterarSenhaContaGsuite) {
		this.alterarSenhaContaGsuite = alterarSenhaContaGsuite;
	}

	public boolean isExisteConfiguracaoSeiGsuite() {
		return existeConfiguracaoSeiGsuite;
	}

	public void setExisteConfiguracaoSeiGsuite(boolean existeConfiguracaoSeiGsuite) {
		this.existeConfiguracaoSeiGsuite = existeConfiguracaoSeiGsuite;
	}	

	public boolean isExisteConfiguracaoSeiBlackboard() {
		return existeConfiguracaoSeiBlackboard;
	}

	public void setExisteConfiguracaoSeiBlackboard(boolean existeConfiguracaoSeiBlackboard) {
		this.existeConfiguracaoSeiBlackboard = existeConfiguracaoSeiBlackboard;
	}

	public Boolean getApresentarColunaModal() {
		if (getTam() < 625 && getTam() > 0) {
			return false;
		}
		return true;

	}
	
	public Boolean getMenuRelatorioAulasRegistradasNaoRegistradas() {
		return menuRelatorioAulasRegistradasNaoRegistradas;
	}

	public void setMenuRelatorioAulasRegistradasNaoRegistradas(Boolean menuRelatorioAulasRegistradasNaoRegistradas) {
		this.menuRelatorioAulasRegistradasNaoRegistradas = menuRelatorioAulasRegistradasNaoRegistradas;
	}

}