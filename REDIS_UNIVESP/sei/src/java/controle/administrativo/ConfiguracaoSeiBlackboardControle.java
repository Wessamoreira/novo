package controle.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardDominioVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.administrativo.enumeradores.TipoAdminSdkIntegracaoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.PermissaoBlackboardVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.CourseRoleIdEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.gsuite.AdminSdkIntegracaoVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

@Controller("ConfiguracaoSeiBlackboardControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoSeiBlackboardControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1730884513011821379L;
	private static final String CONTEXT_PARA_EDICAO = "configuracaoSeiBlackboardItens";
	private static final String TELA_FORM = "configuracaoSeiBlackboardForm.xhtml";
	private static final String TELA_CONS = "configuracaoSeiBlackboardCons.xhtml";
	private ConfiguracaoSeiBlackboardVO configuracaoSeiBlackboardVO;
	private ConfiguracaoSeiBlackboardDominioVO configuracaoSeiBlackboardDominioVO;
	private List<PermissaoBlackboardVO> listaPermissaoBlackboardVO;
	private Boolean marcarTodasPermissaoBlacboard;
	private String controleAba;
	private String campoConsultaPessoa;
	private String valorConsultaPessoa;
	private List<PessoaVO> listaConsultaPessoa;
	private PessoaVO pessoaVOFiltroImportadas;
	private PessoaVO pessoaVOFiltro;
	private AdminSdkIntegracaoVO adminSdkIntegracaoVOFiltro;
	private DataModelo controleConsultaAdminSdkIntegracao;
	private RegistroExecucaoJobVO registroExecucaoJobVOFiltro;
	private DataModelo controleConsultaRegistroExecucaoJob;

	private TurmaVO turmaVO;
	private String ano;
	private String semestre;

	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private String situacaoTurma;
	private List<SelectItem> listaSelectItemSituacaoTurma;

	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	private SalaAulaBlackboardVO salaAulaBlackboardVOFiltro;
	private List<SalaAulaBlackboardVO> listaSalaAulaBlackboardVO;
	private boolean marcaTodasBlackboard = false;
	private boolean alterarSenhaSeiBlackboard = false;
	private DataModelo dadosConsultaSalaAulaImportado;
	private String filtroNomeSala;
	private String filtroNomeGrupo;
	protected List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<SelectItem> listaSelectItemTipoUsuarioBlackboard;
	
	public String novo() {
		try {
			ConfiguracaoSeiBlackboardVO obj = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			if (Uteis.isAtributoPreenchido(obj)) {
				carregarDadosEdicao(obj);
				setMensagemID(MSG_TELA.msg_dados_editar.name());
			} else {
				setConfiguracaoSeiBlackboardVO(new ConfiguracaoSeiBlackboardVO());
				getConfiguracaoSeiBlackboardVO().carregarDadosIniciaisTemporario();
				setControleAba("tabDadosAutenticacaoSeiBlackboard");
				setMensagemID(MSG_TELA.msg_entre_dados.name());
			}
			consultarUnidadeEnsinoFiltroRelatorio("ConfiguracaoSeiBlackboardControle");
			setMarcarTodasUnidadeEnsino(false);
	    	marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			ConfiguracaoSeiBlackboardVO obj = (ConfiguracaoSeiBlackboardVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			carregarDadosEdicao(obj);
			setMensagemID(MSG_TELA.msg_dados_editar.name());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
		}
	}

	private void carregarDadosEdicao(ConfiguracaoSeiBlackboardVO obj) {
		setConfiguracaoSeiBlackboardVO(getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		setControleAba("tabDadosAutenticacaoSeiBlackboard");
	}

	public void excluir() {
		try {
			getFacadeFactory().getConfiguracaoSeiBlackboardFacade().excluir(getConfiguracaoSeiBlackboardVO(), true, getUsuarioLogadoClone());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getConfiguracaoSeiBlackboardFacade().persitir(getConfiguracaoSeiBlackboardVO(), true, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void adicionarConfiguracaoSeiBlackboardDominio() {
		try {
			getFacadeFactory().getConfiguracaoSeiBlackboardFacade().adicionarConfiguracaoSeiBlackboardDominioVO(getConfiguracaoSeiBlackboardVO(), getConfiguracaoSeiBlackboardDominioVO(), getUsuarioLogadoClone());
			setConfiguracaoSeiBlackboardDominioVO(new ConfiguracaoSeiBlackboardDominioVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarConfiguracaoSeiBlackboardDominio() {
		try {
			ConfiguracaoSeiBlackboardDominioVO obj = (ConfiguracaoSeiBlackboardDominioVO) context().getExternalContext().getRequestMap().get("configuracaoSeiBlackboardDominioItens");
			setConfiguracaoSeiBlackboardDominioVO((ConfiguracaoSeiBlackboardDominioVO) obj.clone());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerConfiguracaoSeiBlackboardDominio() {
		try {
			ConfiguracaoSeiBlackboardDominioVO csbd = (ConfiguracaoSeiBlackboardDominioVO) context().getExternalContext().getRequestMap().get("configuracaoSeiBlackboardDominioItens");
			getFacadeFactory().getConfiguracaoSeiBlackboardFacade().removerConfiguracaoSeiBlackboardDominioVO(getConfiguracaoSeiBlackboardVO(), csbd, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public String consultar() {
		try {
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getControleConsultaOtimizado().setLimitePorPagina(5);
			getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultar(getControleConsultaOtimizado(), getConfiguracaoSeiBlackboardVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(ConfiguracaoSeiBlackboardVO.enumCampoConsultaConfiguracaoSeiBlackboard.CODIGO.name());
		getControleConsultaOtimizado().setDataIni(null);
		getControleConsultaOtimizado().setDataFim(null);
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);

	}

	public void consultarStatusSeiBlackboard() {
		try {
			inicializarMensagemVazia();
			setMensagemResponse(getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarStatusSeiBlackboard(getConfiguracaoSeiBlackboardVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarStatusBlackboard() {
		try {
			inicializarMensagemVazia();
			setMensagemResponse(getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarStatusBlackboard(getConfiguracaoSeiBlackboardVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarGeracaoContaPessoaNoBlackboard() {
		try {
			PessoaVO pessoa = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getConfiguracaoSeiBlackboardFacade().realizarGeracaoDaContaBlackboardPorPessoa(getConfiguracaoSeiBlackboardVO(), pessoa, getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarPessoa() {
		try {
			setListaConsultaPessoa(new ArrayList<PessoaVO>());
			setListaConsultaPessoa(getFacadeFactory().getPessoaFacade().consultar(getCampoConsultaPessoa(), getValorConsultaPessoa(), TipoPessoa.NENHUM, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparPessoa() {
		try {
			setPessoaVOFiltroImportadas(new PessoaVO());
			setPessoaVOFiltro(new PessoaVO());
			getListaConsultaPessoa().clear();
			setValorConsultaPessoa("");
			setCampoConsultaPessoa("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarSalaAulaBlackboard() {
		try {
			// getDataModeloClassroom().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			setListaSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboard(getSalaAulaBlackboardVOFiltro(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaSalaAulaBlackboardVO(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarSalaAulaBlackboard() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get("blackboardItem");
			setSalaAulaBlackboardVO(obj);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	public void realizarAtualizacaoBlackboard() {
		try {
			setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().realizarRevisaoSalaAulaBlackboard(getSalaAulaBlackboardVO(), getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_atualizados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	

	public void excluirBlackboard() {
		try {
			TipoSalaAulaBlackboardEnum tipo = getSalaAulaBlackboardVO().getTipoSalaAulaBlackboardEnum();
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarExclusaoSalaAulaBlackboard(getSalaAulaBlackboardVO(), getUsuarioLogadoClone());
			getListaSalaAulaBlackboardVO().removeIf(p -> p.getCodigo().equals(getSalaAulaBlackboardVO().getCodigo()));
			if(tipo.isTcc()) {
				getListaSalaAulaBlackboardVO().removeIf(p -> p.getId().equals(getSalaAulaBlackboardVO().getId()) && p.getTipoSalaAulaBlackboardEnum().isTccGrupo());	
			}
			getSalaAulaBlackboardVO().setCodigo(0);
			getSalaAulaBlackboardVO().setLinkSalaAulaBlackboard("");
			getSalaAulaBlackboardVO().setId("");
			getSalaAulaBlackboardVO().setIdSalaAulaBlackboard("");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarBuscaAlunoBlackboard() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarBuscaAlunoSalaAulaBlackboard(getSalaAulaBlackboardVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_atualizados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarEnvioConviteAlunoBlackboard() {
		try {
			SalaAulaBlackboardPessoaVO obj = (SalaAulaBlackboardPessoaVO) context().getExternalContext().getRequestMap().get("salaAulaBlackboardPessoaItens");
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarEnvioConviteAlunoSalaAulaBlackboard(getSalaAulaBlackboardVO(), obj, getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarAtualizacaoAlunoBlackboard() {
		try {
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getSalaAulaBlackboardFacade().realizarAtualizacaoAlunoSalaAulaBlackboard(getSalaAulaBlackboardVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	

	public void realizarProcessamentoLoteSalaAulaBlackboard() {
		try {
			setMensagemResponseJson(getFacadeFactory().getSalaAulaBlackboardFacade().realizarProcessamentoLoteSalaAulaBlackboard(getUsuarioLogadoClone()));
			getAdminSdkIntegracaoVOFiltro().setUsuarioVO(getUsuarioLogadoClone());
			getControleConsultaAdminSdkIntegracao().setPaginaAtual(1);
			getControleConsultaAdminSdkIntegracao().setPage(1);
			consultarAdminSdkIntegracao();
			setControleAba("nameAdminSDKProcessamentoLote");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarMarcacaoTodasSalaAulaBlackboard() {
		try {
			getListaSalaAulaBlackboardVO().stream().forEach(p -> p.setSelecionado(isMarcaTodasBlackboard()));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarValidacaoSeTodasSalaAulaBlackboardEstaoMarcadas() {
		try {
			setMarcaTodasBlackboard(getListaSalaAulaBlackboardVO().stream().allMatch(SalaAulaBlackboardVO::isSelecionado));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarSalaAulaBlackboardOperacao() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarSalaAulaBlackboardOperacao(getUsuarioLogadoClone());
			getAdminSdkIntegracaoVOFiltro().setUsuarioVO(getUsuarioLogadoClone());
			getControleConsultaAdminSdkIntegracao().setPaginaAtual(1);
			getControleConsultaAdminSdkIntegracao().setPage(1);
			consultarAdminSdkIntegracao();
			setControleAba("nameAdminSDKProcessamentoLote");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarAdminSdkIntegracaoPorFiltro() {
		try {
			getAdminSdkIntegracaoVOFiltro().setUsuarioVO(new UsuarioVO());
			getControleConsultaAdminSdkIntegracao().setPaginaAtual(1);
			getControleConsultaAdminSdkIntegracao().setPage(1);
			consultarAdminSdkIntegracao();
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaAdminSdkIntegracao().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarAdminSdkIntegracao() {
		try {
			getControleConsultaAdminSdkIntegracao().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getControleConsultaAdminSdkIntegracao().setLimitePorPagina(5);
			getAdminSdkIntegracaoVOFiltro().setTipoAdminSdkIntegracaoEnum(TipoAdminSdkIntegracaoEnum.BLACKBOARD);
			getFacadeFactory().getAdminSdkIntegracaoFacade().consultar(getControleConsultaAdminSdkIntegracao(), getAdminSdkIntegracaoVOFiltro());
		} catch (Exception e) {
			getControleConsultaAdminSdkIntegracao().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerAdminSdkIntegracao(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaAdminSdkIntegracao().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaAdminSdkIntegracao().setPage(dataScrollerEvent.getPage());
			consultarAdminSdkIntegracao();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoAdminSdkIntegracao() {
		try {
			AdminSdkIntegracaoVO obj = (AdminSdkIntegracaoVO) context().getExternalContext().getRequestMap().get("adminSdkIntegracaoItens");
			getFacadeFactory().getAdminSdkIntegracaoFacade().consultarAtualizacaoAdminSdkIntegracao(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarRegistroExecucaoJobPorAdminSdkIntegracao() {
		try {
			AdminSdkIntegracaoVO obj = (AdminSdkIntegracaoVO) context().getExternalContext().getRequestMap().get("adminSdkIntegracaoItens");
			getControleConsultaRegistroExecucaoJob().setDataIni(obj.getDataRegistro());
			if(obj.getTipoServicoAdminSdkGoogleEnum().isCriarBlackboard()) {
				getRegistroExecucaoJobVOFiltro().setNome(JobsEnum.JOB_BLACKBOARD_CONTAS_SALA.getName());
			}
			getRegistroExecucaoJobVOFiltro().setCodigoOrigem(obj.getCodigo());
			getRegistroExecucaoJobVOFiltro().getListaJobsEnum().clear();
			getControleConsultaRegistroExecucaoJob().setPaginaAtual(1);
			getControleConsultaRegistroExecucaoJob().setPage(1);
			consultarRegistroExecucaoJob();
			setControleAba("nameRegistroExecucaoJob");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaRegistroExecucaoJob().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	

	public void consultarRegistroExecucaoJobPorFiltros() {
		try {
			getControleConsultaRegistroExecucaoJob().setPaginaAtual(1);
			getControleConsultaRegistroExecucaoJob().setPage(1);
			consultarRegistroExecucaoJob();
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaRegistroExecucaoJob().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarRegistroExecucaoJob() {
		try {
			getControleConsultaRegistroExecucaoJob().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getControleConsultaRegistroExecucaoJob().setLimitePorPagina(5);
			getFacadeFactory().getRegistroExecucaoJobFacade().consultar(getControleConsultaRegistroExecucaoJob(), getRegistroExecucaoJobVOFiltro());
		} catch (Exception e) {
			getControleConsultaRegistroExecucaoJob().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerRegistroExecucaoJob(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaRegistroExecucaoJob().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaRegistroExecucaoJob().setPage(dataScrollerEvent.getPage());
			consultarRegistroExecucaoJob();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarTurmaProgramacaoAula(getCampoConsultaTurma(), getValorConsultaTurma(), getSituacaoTurma(), "", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			getSalaAulaBlackboardVOFiltro().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			getSalaAulaBlackboardVOFiltro().setTurmaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		setTurmaVO(new TurmaVO());
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}
	
	
	
	public void realizarConsultaPermissaoSistemaBlackboard() {
		try {
			getConfiguracaoSeiBlackboardDominioVO().setTipoPermissaoSistema(true);
			getConfiguracaoSeiBlackboardDominioVO().realizarMontagemListaListaTempPermissaoBlackboardVO();
			setListaPermissaoBlackboardVO(getFacadeFactory().getConfiguracaoSeiBlackboardFacade().realizarConsultaPermissaoBlackboard(getConfiguracaoSeiBlackboardVO(), getConfiguracaoSeiBlackboardDominioVO(), getUsuarioLogadoClone()));
			verificarTodasPermissaoBlackboardSelecionados();
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarConsultaPermissaoInstitucionalBlackboard() {
		try {
			getConfiguracaoSeiBlackboardDominioVO().setTipoPermissaoSistema(false);
			getConfiguracaoSeiBlackboardDominioVO().realizarMontagemListaListaTempPermissaoBlackboardVO();
			setListaPermissaoBlackboardVO(getFacadeFactory().getConfiguracaoSeiBlackboardFacade().realizarConsultaPermissaoBlackboard(getConfiguracaoSeiBlackboardVO(), getConfiguracaoSeiBlackboardDominioVO(), getUsuarioLogadoClone()));
			verificarTodasPermissaoBlackboardSelecionados();
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparDadosPermissaoSistema() {
		try {
			getConfiguracaoSeiBlackboardDominioVO().setPermissaoSistema("");	
			getConfiguracaoSeiBlackboardDominioVO().setRoleIdSistema("");	
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparDadosPermissaoInstitucional() {
		try {
			getConfiguracaoSeiBlackboardDominioVO().setPermissaoInstitucional("");
			getConfiguracaoSeiBlackboardDominioVO().setRolerIdInstitucional("");
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void verificarTodasPermissaoBlackboardSelecionados() {
		getListaPermissaoBlackboardVO().stream().filter(p-> p.getSelecionado()).forEach(p->{
    		if(getConfiguracaoSeiBlackboardDominioVO().getListaTempPermissaoBlackboardVO().stream().noneMatch(s -> s.getRoleId().equals(p.getRoleId())) ) {
    			getConfiguracaoSeiBlackboardDominioVO().getListaTempPermissaoBlackboardVO().add(p);
    		}
    	});
		getListaPermissaoBlackboardVO().stream().filter(p-> !p.getSelecionado()).forEach(p->{
			getConfiguracaoSeiBlackboardDominioVO().getListaTempPermissaoBlackboardVO().removeIf(s -> s.getRoleId().equals(p.getRoleId()));
    	});
		getConfiguracaoSeiBlackboardDominioVO().realizarSeparacaoListaTempPermissaoBlackboardVO();
	}

	public void marcarTodasPermissaoBlackboardAction() {
		for (PermissaoBlackboardVO permissaoBlackboard : getListaPermissaoBlackboardVO()) {
			if (getMarcarTodasPermissaoBlacboard()) {
				permissaoBlackboard.setSelecionado(Boolean.TRUE);
			} else {
				permissaoBlackboard.setSelecionado(Boolean.FALSE);
			}
		}
		verificarTodasPermissaoBlackboardSelecionados();
	}
	
	public void limparPermissaoBlackboardAction(){
		setMarcarTodasPermissaoBlacboard(false);
		marcarTodasPermissaoBlackboardAction();
	}
	

	public ConfiguracaoSeiBlackboardVO getConfiguracaoSeiBlackboardVO() {
		if (configuracaoSeiBlackboardVO == null) {
			configuracaoSeiBlackboardVO = new ConfiguracaoSeiBlackboardVO();
		}
		return configuracaoSeiBlackboardVO;
	}

	public void setConfiguracaoSeiBlackboardVO(ConfiguracaoSeiBlackboardVO configuracaoSeiBlackboardVO) {
		this.configuracaoSeiBlackboardVO = configuracaoSeiBlackboardVO;
	}
	

	public ConfiguracaoSeiBlackboardDominioVO getConfiguracaoSeiBlackboardDominioVO() {
		if (configuracaoSeiBlackboardDominioVO == null) {
			configuracaoSeiBlackboardDominioVO = new ConfiguracaoSeiBlackboardDominioVO();
		}
		return configuracaoSeiBlackboardDominioVO;
	}

	public void setConfiguracaoSeiBlackboardDominioVO(ConfiguracaoSeiBlackboardDominioVO configuracaoSeiBlackboardDominioVO) {
		this.configuracaoSeiBlackboardDominioVO = configuracaoSeiBlackboardDominioVO;
	}
	
	public String getControleAba() {
		if (controleAba == null) {
			controleAba = "tabDadosAutenticacaoSeiBlackboard";
		}
		return controleAba;
	}

	public void setControleAba(String controleAba) {
		this.controleAba = controleAba;
	}

	public PessoaVO getPessoaVOFiltroImportadas() {
		if (pessoaVOFiltroImportadas == null) {
			pessoaVOFiltroImportadas = new PessoaVO();
		}
		return pessoaVOFiltroImportadas;
	}

	public void setPessoaVOFiltroImportadas(PessoaVO pessoaVOFiltroImportadas) {
		this.pessoaVOFiltroImportadas = pessoaVOFiltroImportadas;
	}

	public PessoaVO getPessoaVOFiltro() {
		return pessoaVOFiltro;
	}

	public void setPessoaVOFiltro(PessoaVO pessoaVOFiltro) {
		this.pessoaVOFiltro = pessoaVOFiltro;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public List<PessoaVO> getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList<>();
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List<PessoaVO> listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public boolean isApresentarCampoCpf() {
		return getCampoConsultaPessoa().equals("cpf");
	}
	
	public AdminSdkIntegracaoVO getAdminSdkIntegracaoVOFiltro() {
		if (adminSdkIntegracaoVOFiltro == null) {
			adminSdkIntegracaoVOFiltro = new AdminSdkIntegracaoVO();
		}
		return adminSdkIntegracaoVOFiltro;
	}

	public void setAdminSdkIntegracaoVOFiltro(AdminSdkIntegracaoVO adminSdkIntegracaoVOFiltro) {
		this.adminSdkIntegracaoVOFiltro = adminSdkIntegracaoVOFiltro;
	}

	public DataModelo getControleConsultaAdminSdkIntegracao() {
		if (controleConsultaAdminSdkIntegracao == null) {
			controleConsultaAdminSdkIntegracao = new DataModelo();
		}
		return controleConsultaAdminSdkIntegracao;
	}

	public void setControleConsultaAdminSdkIntegracao(DataModelo controleConsultaAdminSdkIntegracao) {
		this.controleConsultaAdminSdkIntegracao = controleConsultaAdminSdkIntegracao;
	}

	public DataModelo getControleConsultaRegistroExecucaoJob() {
		if (controleConsultaRegistroExecucaoJob == null) {
			controleConsultaRegistroExecucaoJob = new DataModelo();
		}
		return controleConsultaRegistroExecucaoJob;
	}

	public void setControleConsultaRegistroExecucaoJob(DataModelo controleConsultaRegistroExecucaoJob) {
		this.controleConsultaRegistroExecucaoJob = controleConsultaRegistroExecucaoJob;
	}

	public RegistroExecucaoJobVO getRegistroExecucaoJobVOFiltro() {
		if (registroExecucaoJobVOFiltro == null) {
			registroExecucaoJobVOFiltro = new RegistroExecucaoJobVO();
		}
		return registroExecucaoJobVOFiltro;
	}

	public void setRegistroExecucaoJobVOFiltro(RegistroExecucaoJobVO registroExecucaoJobVOFiltro) {
		this.registroExecucaoJobVOFiltro = registroExecucaoJobVOFiltro;
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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public String getSituacaoTurma() {
		if (situacaoTurma == null) {
			situacaoTurma = "AB";
		}
		return situacaoTurma;
	}

	public void setSituacaoTurma(String situacaoTurma) {
		this.situacaoTurma = situacaoTurma;
	}

	public List<SelectItem> getListaSelectItemSituacaoTurma() {
		if (listaSelectItemSituacaoTurma == null) {
			listaSelectItemSituacaoTurma = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoTurma.add(new SelectItem("", ""));
			listaSelectItemSituacaoTurma.add(new SelectItem("AB", "Aberta"));
			listaSelectItemSituacaoTurma.add(new SelectItem("FE", "Fechada"));
		}
		return listaSelectItemSituacaoTurma;
	}

	public void setListaSelectItemSituacaoTurma(List<SelectItem> listaSelectItemSituacaoTurma) {
		this.listaSelectItemSituacaoTurma = listaSelectItemSituacaoTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}
	
	

	public SalaAulaBlackboardVO getSalaAulaBlackboardVOFiltro() {
		if (salaAulaBlackboardVOFiltro == null) {
			salaAulaBlackboardVOFiltro = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVOFiltro;
	}

	public void setSalaAulaBlackboardVOFiltro(SalaAulaBlackboardVO salaAulaBlackboardVOFiltro) {
		this.salaAulaBlackboardVOFiltro = salaAulaBlackboardVOFiltro;
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public boolean isMarcaTodasBlackboard() {
		return marcaTodasBlackboard;
	}

	public void setMarcaTodasBlackboard(boolean marcaTodasBlackboard) {
		this.marcaTodasBlackboard = marcaTodasBlackboard;
	}

	public List<SalaAulaBlackboardVO> getListaSalaAulaBlackboardVO() {
		if (listaSalaAulaBlackboardVO == null) {
			listaSalaAulaBlackboardVO = new ArrayList<>();
		}
		return listaSalaAulaBlackboardVO;
	}

	public void setListaSalaAulaBlackboardVO(List<SalaAulaBlackboardVO> listaSalaAulaBlackboardVO) {
		this.listaSalaAulaBlackboardVO = listaSalaAulaBlackboardVO;
	}

	public List<PermissaoBlackboardVO> getListaPermissaoBlackboardVO() {
		if (listaPermissaoBlackboardVO == null) {
			listaPermissaoBlackboardVO = new ArrayList<>();
		}
		return listaPermissaoBlackboardVO;
	}

	public void setListaPermissaoBlackboardVO(List<PermissaoBlackboardVO> listaPermissaoBlackboardVO) {
		this.listaPermissaoBlackboardVO = listaPermissaoBlackboardVO;
	}

	public boolean isAlterarSenhaSeiBlackboard() {
		return alterarSenhaSeiBlackboard;
	}

	public void setAlterarSenhaSeiBlackboard(boolean alterarSenhaSeiBlackboard) {
		this.alterarSenhaSeiBlackboard = alterarSenhaSeiBlackboard;
	}

	public Boolean getMarcarTodasPermissaoBlacboard() {		
		return marcarTodasPermissaoBlacboard;
	}

	public void setMarcarTodasPermissaoBlacboard(Boolean marcarTodasPermissaoBlacboard) {
		this.marcarTodasPermissaoBlacboard = marcarTodasPermissaoBlacboard;
	}
	
	
	public void realizarImportacaoSalaAulaBlackboard() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarImportacaoSalaAulaBlackboard(getConfiguracaoSeiBlackboardVO(), getUsuarioLogado());
			setMensagemID("msg_importar_alunos_blackboard",  Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarSalaImportadaBlackboard(Integer pagina) {
		try {
			getDadosConsultaSalaAulaImportado().setPage(pagina);
			getDadosConsultaSalaAulaImportado().setPaginaAtual(pagina);
			getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaImportada(getDadosConsultaSalaAulaImportado(), getFiltroNomeSala(), getFiltroNomeGrupo(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados",  Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void persistirSala(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().persistir(salaAulaBlackboardVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados",  Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void excluirSala(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().excluir(salaAulaBlackboardVO, getUsuarioLogado());
			getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaImportada(getDadosConsultaSalaAulaImportado(), getFiltroNomeSala(), getFiltroNomeGrupo(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos",  Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void scrollerListenerSalaImportadaBlackboard(DataScrollEvent DataScrollEvent) throws Exception {
		consultarSalaImportadaBlackboard(DataScrollEvent.getPage());
	}

	public DataModelo getDadosConsultaSalaAulaImportado() {
		if(dadosConsultaSalaAulaImportado == null) {
			dadosConsultaSalaAulaImportado = new DataModelo();
			dadosConsultaSalaAulaImportado.setLimitePorPagina(10);
			dadosConsultaSalaAulaImportado.setPaginaAtual(0);
			dadosConsultaSalaAulaImportado.setPage(0);
		}
		return dadosConsultaSalaAulaImportado;
	}

	public void setDadosConsultaSalaAulaImportado(DataModelo dadosConsultaSalaAulaImportado) {
		this.dadosConsultaSalaAulaImportado = dadosConsultaSalaAulaImportado;
	}

	public String getFiltroNomeSala() {
		if(filtroNomeSala == null) {
			filtroNomeSala = "";
		}
		return filtroNomeSala;
	}

	public void setFiltroNomeSala(String filtroNomeSala) {
		this.filtroNomeSala = filtroNomeSala;
	}

	public String getFiltroNomeGrupo() {
		if(filtroNomeGrupo == null) {
			filtroNomeGrupo = "";
		}
		return filtroNomeGrupo;
	}

	public void setFiltroNomeGrupo(String filtroNomeGrupo) {
		this.filtroNomeGrupo = filtroNomeGrupo;
	}
	

	public void consultarDisciplina() {
		try {

			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getValorConsultaDisciplina().equals("")) {
				throw new Exception("Digite um valor para consulta");
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if(!Uteis.getIsValorNumerico(getValorConsultaDisciplina())) {
					throw new Exception("Informe apenas valores numéricos.");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt),  Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(disciplina)) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("abreviatura")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public String getCampoConsultaDisciplina() {
		if(this.campoConsultaDisciplina == null){
			this.campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}
	

	public String getValorConsultaDisciplina() {
		if(this.valorConsultaDisciplina == null){
			this.valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}
	
	public List<DisciplinaVO> getListaConsultaDisciplina() {
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}
	
	public List<SelectItem> tipoConsultaComboDisciplina;
	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if(tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("abreviatura", "Abreviatura"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	public void selecionarDisciplina() {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) getRequestMap().get("disciplinaItem");
			if (disciplina != null) {
				getSalaAulaBlackboardVO().setDisciplinaVO(disciplina);
			}
			getListaConsultaDisciplina().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoUsuarioBlackboard() {
		if(listaSelectItemTipoUsuarioBlackboard == null) {
			listaSelectItemTipoUsuarioBlackboard =  UtilSelectItem.getListaSelectItemEnum(CourseRoleIdEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoUsuarioBlackboard;
	}

	public void setListaSelectItemTipoUsuarioBlackboard(List<SelectItem> listaSelectItemTipoUsuarioBlackboard) {
		this.listaSelectItemTipoUsuarioBlackboard = listaSelectItemTipoUsuarioBlackboard;
	}

	public void realizarInterrupcaoImportacao() {
		try {
			getFacadeFactory().getConfiguracaoSeiBlackboardFacade().persistirImportacaoEmRealizacao(getConfiguracaoSeiBlackboardVO().getCodigo(), false);
			getConfiguracaoSeiBlackboardVO().setImportacaoEmRealizacao(false);
			setMensagemID("msg_dados_operacao",  Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
}
