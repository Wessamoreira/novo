package controle.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import kong.unirest.HttpResponse;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoAdminSdkIntegracaoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.gsuite.AdminSdkIntegracaoVO;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.gsuite.GoogleMeetConvidadoVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import webservice.arquitetura.InfoWSVO;

@Controller("ConfiguracaoSeiGsuiteControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoSeiGsuiteControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5354632030591115358L;
	private static final String CONTEXT_PARA_EDICAO = "configuracaoSeiGsuiteItens";
	private static final String CONTEXT_CONFIGURACAOSEIGSUITEUNIDADEENISNO = "configuracaoSeiGsuiteUnidadeEnsinoItens";
	private static final String TELA_FORM = "configuracaoSeiGsuiteForm.xhtml";
	private static final String TELA_CONS = "configuracaoSeiGsuiteCons.xhtml";
	private ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO;
	private ConfiguracaoSeiGsuiteUnidadeEnsinoVO configuracaoSeiGsuiteUnidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String campoNavegarAutoriazacao;
	private boolean alterarSenhaSeiGsuite = false;
	private boolean marcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO = false;
	private String campoConsultaPessoa;
	private String valorConsultaPessoa;
	private List<PessoaVO> listaConsultaPessoa; 
	private DataModelo controleConsultaPessoaGsuite;
	private PessoaGsuiteVO pessoaGsuiteVOFiltro;
	private DataModelo dataModeloPessoaGsuiteImportadas;
	private PessoaGsuiteVO pessoaGsuiteVOFiltroImportadas;
	private AdminSdkIntegracaoVO adminSdkGoogleVOFiltro;
	private DataModelo controleConsultaAdminSdkGoogle;
	private RegistroExecucaoJobVO registroExecucaoJobVOFiltro;
	private DataModelo controleConsultaRegistroExecucaoJob;
	
	//private DataModelo dataModeloClassroom;
	
	private DataModelo dataModeloGoogleMeet;
	private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private List<GoogleMeetConvidadoVO> googleMeetConvidadoVOs;
	
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private String situacaoTurma;
	private List<SelectItem> listaSelectItemSituacaoTurma;
	
	private GoogleMeetVO googleMeetVO;
	private ClassroomGoogleVO classroomGoogleVO;
	private String controleAba;
	private String novaSenhaContaGsuite;
	private String novaSenhaContaGsuiteConfirmacao;
	private List<ClassroomGoogleVO> listaClassroomGoogleVO;
	private ProgressBarVO progressBarVO;
	private boolean marcaTodasClassroom = false;
	
	
	public String novo() {
		try {
			ConfiguracaoSeiGsuiteVO obj = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			if(Uteis.isAtributoPreenchido(obj)) {
				carregarDadosEdicao(obj);
				setMensagemID(MSG_TELA.msg_dados_editar.name());
			}else {
				setConfiguracaoSeiGsuiteVO(new ConfiguracaoSeiGsuiteVO());
				setMarcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO(false);
				montarListaSelectItemUnidadeEnsino();
				setAdminSdkGoogleVOFiltro(new AdminSdkIntegracaoVO());
				getConfiguracaoSeiGsuiteVO().setRedirectUrlAplicacao(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getUrlAcessoExternoAplicacao()+"/CredencialGoogleHttpServlet");
				setControleAba("tabDadosAutenticacaoSeiGsuite");
				setMensagemID(MSG_TELA.msg_entre_dados.name());		
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			ConfiguracaoSeiGsuiteVO obj = (ConfiguracaoSeiGsuiteVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			carregarDadosEdicao(obj);
			setMensagemID(MSG_TELA.msg_dados_editar.name());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
		}
	}

	private void carregarDadosEdicao(ConfiguracaoSeiGsuiteVO obj) {
		setConfiguracaoSeiGsuiteVO(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		setMarcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO(false);
		montarListaSelectItemUnidadeEnsino();
		setAdminSdkGoogleVOFiltro(new AdminSdkIntegracaoVO());
		setControleAba("tabDadosAutenticacaoSeiGsuite");
	}

	public void excluir() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().excluir(getConfiguracaoSeiGsuiteVO(), true, getUsuarioLogadoClone());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().persitir(getConfiguracaoSeiGsuiteVO(), true, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public String consultar() {
		try {
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getControleConsultaOtimizado().setLimitePorPagina(5);
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultar(getControleConsultaOtimizado(), getConfiguracaoSeiGsuiteVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Constantes.EMPTY;
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
		getControleConsultaOtimizado().setCampoConsulta(ConfiguracaoSeiGsuiteVO.enumCampoConsultaConfiguracaoSeiGsuite.CODIGO.name());
		getControleConsultaOtimizado().setDataIni(null);
		getControleConsultaOtimizado().setDataFim(null);
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);

	}
	
	public void adicionarConfiguracaoSeiGsuiteUnidadeEnsino() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().adicionarConfiguracaoSeiGsuiteUnidadeEnsinoVO(getConfiguracaoSeiGsuiteVO(), getConfiguracaoSeiGsuiteUnidadeEnsinoVO(), getUsuarioLogadoClone());
			setConfiguracaoSeiGsuiteUnidadeEnsinoVO(new ConfiguracaoSeiGsuiteUnidadeEnsinoVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void adicionarTodasUnidadeEnsinoParaConfiguracaoSeiGsuiteUnidadeEnsino() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().adicionarTodasUnidadeEnsinoParaConfiguracaoSeiGsuiteUnidadeEnsino(getConfiguracaoSeiGsuiteVO(), getConfiguracaoSeiGsuiteUnidadeEnsinoVO(), getListaSelectItemUnidadeEnsino(), getUsuarioLogadoClone());
			setConfiguracaoSeiGsuiteUnidadeEnsinoVO(new ConfiguracaoSeiGsuiteUnidadeEnsinoVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarConfiguracaoSeiGsuiteUnidadeEnsino() {
		try {
			ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj = (ConfiguracaoSeiGsuiteUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get(CONTEXT_CONFIGURACAOSEIGSUITEUNIDADEENISNO);
			setConfiguracaoSeiGsuiteUnidadeEnsinoVO((ConfiguracaoSeiGsuiteUnidadeEnsinoVO) obj.clone());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void removerConfiguracaoSeiGsuiteUnidadeEnsino() {
		try {
			ConfiguracaoSeiGsuiteUnidadeEnsinoVO csgue = (ConfiguracaoSeiGsuiteUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get(CONTEXT_CONFIGURACAOSEIGSUITEUNIDADEENISNO);
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().removerConfiguracaoSeiGsuiteUnidadeEnsinoVO(getConfiguracaoSeiGsuiteVO(), csgue, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAlteracaoDominioEmailPorLista() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarAlteracaoDominioEmailPorLista(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAlteracaoDominioEmailFuncionarioPorLista() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarAlteracaoDominioEmailFuncionarioPorLista(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAlteracaoUnidadeOrganizacionalAlunoPorLista() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarAlteracaoUnidadeOrganizacionalAlunoPorLista(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAlteracaoUnidadeOrganizacionalFuncionarioPorLista() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarAlteracaoUnidadeOrganizacionalFuncionarioPorLista(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	

	public void consultarStatusSeiGsuite() {
		try {
			inicializarMensagemVazia();
			setMensagemResponse(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarStatusSeiGsuite(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarExclusaoCredencialGoogle() {
		try {
			inicializarMensagemVazia();
			setMensagemResponse(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarExclusaoCredencialGoogle(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarGeracaoCredencialGoogle() {
		try {
			setCampoNavegarAutoriazacao(Constantes.EMPTY);
			inicializarMensagemVazia();
			HttpResponse<String> response = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarRequestUrlCodeGoogle(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone());
			InfoWSVO rep = new Gson().fromJson(response.getBody(), InfoWSVO.class);
			if (response.getStatus() != (HttpStatus.OK.value())) {				
				tratarMensagemErroWebService(rep, String.valueOf(response.getStatus()), response.getBody());
			}else {
				if (rep.getMensagem().contains("accounts.google.com/o/oauth2/auth")) {
					setCampoNavegarAutoriazacao("popup('"+rep.getMensagem()+"', 'credencialGoogle' , 1024, 800)");	
				} else {
					setCampoNavegarAutoriazacao(Constantes.EMPTY);
					setMensagem(rep.getMensagem());
				}
			}
		} catch (Exception e) {
			setCampoNavegarAutoriazacao(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void realizarGeracaoContaPessoaNoGsuite() {
		try {
			PessoaVO pessoa = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarGeracaoDaContaGsuitePorPessoa(getConfiguracaoSeiGsuiteVO(), pessoa, getUsuarioLogadoClone()));
			getControleConsultaPessoaGsuite().setPaginaAtual(1);
			getControleConsultaPessoaGsuite().setPage(1);
			getPessoaGsuiteVOFiltro().setPessoaVO(pessoa);
			consultarPessoaGsuite();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAlteracaoPorPessoaGsuite() {
		try {
			PessoaGsuiteVO pessoa = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("pessoaGsuiteItens");
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarAlteracaoPorPessoaGsuite(getConfiguracaoSeiGsuiteVO(), pessoa, getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarExclusaoPorPessoaGsuite() {
		try {
			PessoaGsuiteVO pessoa = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("pessoaGsuiteItens");
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarExclusaoPorPessoaGsuite(getConfiguracaoSeiGsuiteVO(), pessoa, getUsuarioLogadoClone()));
			getControleConsultaPessoaGsuite().setPaginaAtual(1);
			getControleConsultaPessoaGsuite().setPage(1);
			consultarPessoaGsuite();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarDesvinculacaoPessoaGsuite() {
		try {
			PessoaGsuiteVO pessoa = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("pessoaGsuiteItens");
			inicializarMensagemVazia();
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarDesvinculacaoPessoaGsuite(pessoa, getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone());
			setPessoaGsuiteVOFiltro(new PessoaGsuiteVO());
			getPessoaGsuiteVOFiltro().setEmail(pessoa.getEmail());
			getControleConsultaPessoaGsuite().setPaginaAtual(1);
			getControleConsultaPessoaGsuite().setPage(1);
			consultarPessoaGsuite();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarGeracaoUsuarioGsuiteLote() {
		try {
			inicializarMensagemVazia();
			getAdminSdkGoogleVOFiltro().setUsuarioVO(getUsuarioLogadoClone());
			setMensagemResponseJson(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarGeracaoUsuarioGsuiteLote(getConfiguracaoSeiGsuiteVO(), getAdminSdkGoogleVOFiltro(), getUsuarioLogadoClone()));
			getControleConsultaAdminSdkGoogle().setPaginaAtual(1);
			getControleConsultaAdminSdkGoogle().setPage(1);
			consultarAdminSdkGsuite();
			setControleAba("nameAdminSDKProcessamentoLote");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarImportacaoUsuarioGsuiteLote() {
		try {
			inicializarMensagemVazia();
			getAdminSdkGoogleVOFiltro().setUsuarioVO(getUsuarioLogadoClone());
			setMensagemResponseJson(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarImportacaoUsuarioGsuiteLote(getConfiguracaoSeiGsuiteVO(), getAdminSdkGoogleVOFiltro(), getUsuarioLogadoClone()));		
			getControleConsultaAdminSdkGoogle().setPaginaAtual(1);
			getControleConsultaAdminSdkGoogle().setPage(1);
			consultarAdminSdkGsuite();
			setControleAba("nameAdminSDKProcessamentoLote");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarMarcacaoTodasConfiguracaoSeiGsuiteUnidadeEnsino(String unidadeEnsino_nome) {
		try {
			getConfiguracaoSeiGsuiteVO().getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream()
			.filter(p -> (!Uteis.isAtributoPreenchido(unidadeEnsino_nome) || (Uteis.isAtributoPreenchido(unidadeEnsino_nome) && Uteis.removerAcentos(p.getUnidadeEnsinoVO().getNome().toString().toLowerCase()).contains(Uteis.removerAcentos(unidadeEnsino_nome.toLowerCase()))))
					)
			.forEach(p->p.setSelecionado(isMarcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void realizarValidacaoSeTodasConfiguracaoSeiGsuiteUnidadeEnsinoEstaoMarcadas() {
		try {
			setMarcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO(getConfiguracaoSeiGsuiteVO().getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().allMatch(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, Constantes.EMPTY));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getConfiguracaoSeiGsuiteUnidadeEnsinoVO().setUnidadeEnsinoVO(obj);
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome_CNPJ()));
				return;
			}
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(Constantes.EMPTY,0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome_CNPJ"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void consultarPessoaGsuitePorFiltros() {
		try {
			getControleConsultaPessoaGsuite().setPaginaAtual(1);
			getControleConsultaPessoaGsuite().setPage(1);
			consultarPessoaGsuite();
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaPessoaGsuite().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarPessoaGsuite() {
		try {
			getControleConsultaPessoaGsuite().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getFacadeFactory().getPessoaGsuiteFacade().consultar(getControleConsultaPessoaGsuite(), getPessoaGsuiteVOFiltro());
		} catch (Exception e) {
			getControleConsultaPessoaGsuite().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerPessoaGsuite(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaPessoaGsuite().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaPessoaGsuite().setPage(dataScrollerEvent.getPage());
			consultarPessoaGsuite();
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
	
	public void consultarAdminSdkGsuitePorFiltro() {
		try {
			getAdminSdkGoogleVOFiltro().setUsuarioVO(new UsuarioVO());
			getControleConsultaAdminSdkGoogle().setPaginaAtual(1);
			getControleConsultaAdminSdkGoogle().setPage(1);
			consultarAdminSdkGsuite();
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaAdminSdkGoogle().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarAdminSdkGsuite() {
		try {
			getControleConsultaAdminSdkGoogle().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getControleConsultaAdminSdkGoogle().setLimitePorPagina(5);
			getAdminSdkGoogleVOFiltro().setTipoAdminSdkIntegracaoEnum(TipoAdminSdkIntegracaoEnum.GSUITE);
			getFacadeFactory().getAdminSdkIntegracaoFacade().consultar(getControleConsultaAdminSdkGoogle(), getAdminSdkGoogleVOFiltro());
			getFacadeFactory().getAdminSdkIntegracaoFacade().consultarTotalUsuarioAdminSdkGoogle(getAdminSdkGoogleVOFiltro());
		} catch (Exception e) {
			getControleConsultaAdminSdkGoogle().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerAdminSdkGsuite(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaAdminSdkGoogle().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaAdminSdkGoogle().setPage(dataScrollerEvent.getPage());
			consultarAdminSdkGsuite();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoAdminSdkGsuite() {
		try {
			AdminSdkIntegracaoVO obj = (AdminSdkIntegracaoVO) context().getExternalContext().getRequestMap().get("adminSdkGoogleItens");
			getFacadeFactory().getAdminSdkIntegracaoFacade().consultarAtualizacaoAdminSdkIntegracao(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarRegistroExecucaoJobPorAdminSdkIntegracao() {
		try {
			AdminSdkIntegracaoVO obj = (AdminSdkIntegracaoVO) context().getExternalContext().getRequestMap().get("adminSdkGoogleItens");
			getControleConsultaRegistroExecucaoJob().setDataIni(obj.getDataRegistro());
			if(obj.getTipoServicoAdminSdkGoogleEnum().isCriar()) {
				getRegistroExecucaoJobVOFiltro().setNome(JobsEnum.JOB_CRIAR_CONTAS_GSUITE.getName());
				getRegistroExecucaoJobVOFiltro().setErro("Erro :");
			}else if(obj.getTipoServicoAdminSdkGoogleEnum().isImportar()) {
				getRegistroExecucaoJobVOFiltro().setNome(JobsEnum.JOB_CRIAR_CONTAS_GSUITE.getName());
				getRegistroExecucaoJobVOFiltro().setErro("Erro Importar Conta");
			}else if(obj.getTipoServicoAdminSdkGoogleEnum().isCriarClassroom()) {
				getRegistroExecucaoJobVOFiltro().setNome(JobsEnum.JOB_CLASSROOM_CONTAS_GSUITE.getName());
				getRegistroExecucaoJobVOFiltro().setErro("");
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
			getControleConsultaRegistroExecucaoJob().setDataIni(null);
			getRegistroExecucaoJobVOFiltro().setCodigoOrigem(null);
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
	
//	public void scrollerListenerClassroom(DataScrollEvent dataScrollerEvent) {
//		try {
//			getDataModeloClassroom().setPaginaAtual(dataScrollerEvent.getPage());
//			getDataModeloClassroom().setPage(dataScrollerEvent.getPage());
//			consultarClassroom();
//		} catch (Exception e) {
//			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
//		}
//	}

	public void consultarClassroom() {
		try {
			//getDataModeloClassroom().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			setListaClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().consultarClassroom(getTurmaVO(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone()));			
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaClassroomGoogleVO(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarClassroom() {
		try {
			ClassroomGoogleVO obj = (ClassroomGoogleVO) context().getExternalContext().getRequestMap().get("classroomItem");
			setClassroomGoogleVO(obj);	
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	public void realizarInicioProgressBarAdicionarProfessorAuxiliar() {
		try {
			setProgressBarVO(new ProgressBarVO());
			Uteis.checkState(getQtdClassroomSelecionadas() == 0L, "Não foi encontrado nenhuma classroom selecionada para realizar a operação de Adicionar o Professor Auxiliar.");
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().iniciar(0l, (getQtdClassroomSelecionadas().intValue()), "Iniciando Processamento da(s) operações.", true, this, "adicionarContaGoogleProfessorAuxiliarLista");
			setOncompleteModal(Constantes.EMPTY);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}
	
	public void realizarInicioProgressBarRemoverProfessorAuxiliar() {
		try {
			setProgressBarVO(new ProgressBarVO());
			Uteis.checkState(getQtdClassroomSelecionadas() == 0L, "Não foi encontrado nenhuma classroom selecionada para realizar a operação de Remoção do Professor Auxiliar.");
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().iniciar(0l, (getQtdClassroomSelecionadas().intValue()), "Iniciando Processamento da(s) operações.", true, this, "removerContaGoogleProfessorAuxiliarLista");
			setOncompleteModal(Constantes.EMPTY);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}
	
	public void adicionarContaGoogleProfessorAuxiliarLista() {
		try {			
			getFacadeFactory().getClassroomGoogleFacade().realizarAtualizacaoProfessorAuxiliarClassroomPorLista(getListaClassroomGoogleVO(), getProgressBarVO(), true, getProgressBarVO().getUsuarioVO());
			if(getProgressBarVO().getSuperControle() != null) {
				getProgressBarVO().getSuperControle().setMensagemID(MSG_TELA.msg_dados_gravados.name());
			}else {
				setMensagemID(MSG_TELA.msg_dados_gravados.name());
			}
			setOncompleteModal("RichFaces.$('panelProfessorAuxiliarLogs').show();");
		} catch (Exception e) {
			setOncompleteModal(Constantes.EMPTY);
			if(getProgressBarVO().getSuperControle() != null) {
				getProgressBarVO().getSuperControle().setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}else {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}			
		} finally{
			getProgressBarVO().incrementar();
			getProgressBarVO().setForcarEncerramento(true);			
		}
		
	}
	
	public void removerContaGoogleProfessorAuxiliarLista() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarAtualizacaoProfessorAuxiliarClassroomPorLista(getListaClassroomGoogleVO(), getProgressBarVO(), false, getProgressBarVO().getUsuarioVO());
			if(getProgressBarVO().getSuperControle() != null) {
				getProgressBarVO().getSuperControle().setMensagemID(MSG_TELA.msg_dados_gravados.name());
			}else {
				setMensagemID(MSG_TELA.msg_dados_gravados.name());
			}
			setOncompleteModal("RichFaces.$('panelProfessorAuxiliarLogs').show();");
		} catch (Exception e) {
			setOncompleteModal(Constantes.EMPTY);
			if(getProgressBarVO().getSuperControle() != null) {
				getProgressBarVO().getSuperControle().setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}else {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}			
		} finally{
			getProgressBarVO().incrementar();
			getProgressBarVO().setForcarEncerramento(true);			
		}
		
	}
	
	public void adicionarContaGoogleProfessorAuxiliar() {
		try {
			inicializarMensagemVazia();
			ClassroomGoogleVO obj = (ClassroomGoogleVO) context().getExternalContext().getRequestMap().get("classroomItem");
			setMensagemResponseJson(getFacadeFactory().getClassroomGoogleFacade().realizarAtualizacaoProfessorAuxiliarClassroom(obj, true, getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	public void removerContaGoogleProfessorAuxiliar() {
		try {
			inicializarMensagemVazia();
			ClassroomGoogleVO obj = (ClassroomGoogleVO) context().getExternalContext().getRequestMap().get("classroomItem");
			setMensagemResponseJson(getFacadeFactory().getClassroomGoogleFacade().realizarAtualizacaoProfessorAuxiliarClassroom(obj, false, getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	public void realizarAtualizacaoClassroom() {
		try {
			setClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().realizarRevisaoClassroom(getClassroomGoogleVO(), getUsuarioLogadoClone()));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	
	
	public void excluirClassroom() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarExclusaoClassroomGoogle(getClassroomGoogleVO(), getUsuarioLogadoClone());
			getListaClassroomGoogleVO().removeIf(p -> ((ClassroomGoogleVO) p).getCodigo().equals(getClassroomGoogleVO().getCodigo()));
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarBuscaAlunoClassroom() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarBuscaAlunoClassroom(getClassroomGoogleVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarEnvioConviteAlunoClassroom() {
		try {
			PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("classroomStudentVOItens");
			getFacadeFactory().getClassroomGoogleFacade().realizarEnvioConviteAlunoClassroom(getClassroomGoogleVO(), obj, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarAtualizacaoAlunoClassroom() {
		try {
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getClassroomGoogleFacade().realizarAtualizacaoAlunoClassroom(getClassroomGoogleVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarProcessamentoLoteClassroom() {
		try {
			setMensagemResponseJson(getFacadeFactory().getClassroomGoogleFacade().realizarProcessamentoLoteClassroom(getUsuarioLogadoClone()));
			getAdminSdkGoogleVOFiltro().setUsuarioVO(getUsuarioLogadoClone());
			getControleConsultaAdminSdkGoogle().setPaginaAtual(1);
			getControleConsultaAdminSdkGoogle().setPage(1);
			consultarAdminSdkGsuite();
			setControleAba("nameAdminSDKProcessamentoLote");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarMarcacaoTodasClassroom() {
		try {
			getListaClassroomGoogleVO().stream()
			.forEach(p->p.setSelecionado(isMarcaTodasClassroom()));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void realizarValidacaoSeTodasClassroomEstaoMarcadas() {
		try {
			setMarcaTodasClassroom(getListaClassroomGoogleVO().stream().allMatch(ClassroomGoogleVO::isSelecionado));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}	
	}
	
	public void realizarClassroomOperacao() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarClassroomOperacao(getUsuarioLogadoClone());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAlteracaoDonoDriveClassroom() {
		try {
			inicializarMensagemVazia();
			getFacadeFactory().getClassroomGoogleFacade().realizarAlteracaoDonoDriveClassroom(getClassroomGoogleVO(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_atualizados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	

	
	public void editarGoogleMeetVO() {
		GoogleMeetVO obj = (GoogleMeetVO) context().getExternalContext().getRequestMap().get("googleMeetItem");
		setGoogleMeetVO(obj);
	}

	public void realizarGeraracaoGoogleMeet() {
		try {
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarGeracaoEventoGoogleMeetConfiguracaoSeiGsuite(getGoogleMeetVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			consultarGoogleMeet();
			setMensagemID("msg_googleMeet_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void excluirGoogleMeet() {
		try {
			getGoogleMeetVO().setHorarioTurmaDiaItemVOs(getFacadeFactory().getHorarioTurmaDiaItemFacade().consultarPorGoogleMeet(getGoogleMeetVO()));
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarExclusaoGoogleMeet(getGoogleMeetVO(), getUsuarioLogado());
			getDataModeloGoogleMeet().getListaConsulta().removeIf(p -> ((GoogleMeetVO) p).getCodigo().equals(getGoogleMeetVO().getCodigo()));
			setMensagemID("msg_googleMeet_excluir");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarProcessamentoGoogleMeetLote() {
		try {
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarProcessamentoGoogleMeetLote(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerGoogleMeet(DataScrollEvent dataScrollerEvent) {
		try {
			getDataModeloGoogleMeet().setPaginaAtual(dataScrollerEvent.getPage());
			getDataModeloGoogleMeet().setPage(dataScrollerEvent.getPage());
			consultarGoogleMeet();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarGoogleMeet() {
		try {
			getDataModeloGoogleMeet().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getFacadeFactory().getGoogleMeetInterfaceFacade().consultarGoogleMeetPorDataModelo(getDataModeloGoogleMeet(), getTurmaVO(), getAno(), getSemestre(), getUsuarioLogadoClone());			
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarGoogleMeetConvidado() {
		try {
			GoogleMeetVO obj = (GoogleMeetVO) context().getExternalContext().getRequestMap().get("googleMeetItem");
			setGoogleMeetConvidadoVOs(obj.getGoogleMeetConvidadoVOs());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarProcessamentoLoteGoogleMeet() {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarProcessamentoLoteGoogleMeet(getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarTurmaProgramacaoAula(getCampoConsultaTurma(), getValorConsultaTurma(), getSituacaoTurma(), Constantes.EMPTY, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparTurma() {
		setTurmaVO(new TurmaVO());
	}
	
	public void realizarSelecaoPessoaGsuite() {
		try {
			PessoaGsuiteVO pessoa = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("pessoaGsuiteItens");
			setPessoaGsuiteVOFiltro(pessoa);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAlteracaoSenhaContaGsuite() {
		try {
			getFacadeFactory().getAdminSdkIntegracaoFacade().executarAlteracaoSenhaContaGsuite(getPessoaGsuiteVOFiltro().getPessoaVO(), getNovaSenhaContaGsuite(), getNovaSenhaContaGsuiteConfirmacao(), getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparPessoa() {
		try {
			setPessoaGsuiteVOFiltroImportadas(new PessoaGsuiteVO());
			setPessoaGsuiteVOFiltro(new PessoaGsuiteVO());
			getListaConsultaPessoa().clear();
			setValorConsultaPessoa(Constantes.EMPTY);
			setCampoConsultaPessoa(Constantes.EMPTY);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerPessoaGsuiteImportada(DataScrollEvent dataScrollerEvent) {
		try {
			getDataModeloPessoaGsuiteImportadas().setPaginaAtual(dataScrollerEvent.getPage());
			getDataModeloPessoaGsuiteImportadas().setPage(dataScrollerEvent.getPage());
			consultarPessoaGsuiteImportada();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarPessoaGsuiteImportada() {
		try {
			getDataModeloPessoaGsuiteImportadas().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogadoClone());
			getFacadeFactory().getPessoaGsuiteFacade().consultarPessoaGsuiteImportada(getDataModeloPessoaGsuiteImportadas(), getPessoaGsuiteVOFiltroImportadas());			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarPessoaGsuiteImportadasPorFiltros() {
		try {
			getDataModeloPessoaGsuiteImportadas().setPaginaAtual(1);
			getDataModeloPessoaGsuiteImportadas().setPage(1);
			consultarPessoaGsuiteImportada();
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaPessoaGsuite().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void carregarDadoParaConsultaPessoaGsuiteImportadas() {
		try {
			limparPessoa();
			consultarPessoaGsuiteImportada();
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaPessoaGsuite().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarPessoaParaVinculoComPessoaGsuiteImportada() {
		try {
			PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("pessoaGsuiteItens");
			setPessoaGsuiteVOFiltroImportadas(obj);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void realizarVinculoPessoaComPessoaGsuiteImportada() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			getPessoaGsuiteVOFiltroImportadas().setPessoaVO(obj);
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarVinculoPessoaComPessoaGsuiteImportada(getPessoaGsuiteVOFiltroImportadas(), getConfiguracaoSeiGsuiteVO(), getUsuarioLogadoClone());
			((List<PessoaGsuiteVO>) getDataModeloPessoaGsuiteImportadas().getListaConsulta()).removeIf(p-> p.getPessoaVO().getCodigo().equals(obj.getCodigo()));
			setPessoaGsuiteVOFiltroImportadas(new PessoaGsuiteVO());
			getControleConsultaPessoaGsuite().setPaginaAtual(1);
			getControleConsultaPessoaGsuite().setPage(1);
			getPessoaGsuiteVOFiltro().setPessoaVO(obj);
			consultarPessoaGsuite();
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void realizarExclusaoPorPessoaGsuiteImportada() {
		try {
			PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("pessoaGsuiteItens");
			getFacadeFactory().getPessoaGsuiteFacade().excluir(obj, getUsuarioLogadoClone());
			((List<PessoaGsuiteVO>) getDataModeloPessoaGsuiteImportadas().getListaConsulta()).removeIf(p-> p.getCodigo().equals(obj.getCodigo()));
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public List<SelectItem> tipoConsultaComboTurma;
	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<>(0);
			tipoConsultaComboTurma.add(new SelectItem(Constantes.identificadorTurma, Constantes.Identificador));
			tipoConsultaComboTurma.add(new SelectItem(Constantes.nomeUnidadeEnsino,Constantes.Unidade_Ensino));
			tipoConsultaComboTurma.add(new SelectItem(Constantes.nomeTurno, Constantes.Turno));
			tipoConsultaComboTurma.add(new SelectItem(Constantes.nomeCurso, Constantes.Curso));	
		}
		return tipoConsultaComboTurma;
	}

		
	
	public PessoaGsuiteVO getPessoaGsuiteVOFiltro() {
		if (pessoaGsuiteVOFiltro == null) {
			pessoaGsuiteVOFiltro = new PessoaGsuiteVO();
		}
		return pessoaGsuiteVOFiltro;
	}

	public void setPessoaGsuiteVOFiltro(PessoaGsuiteVO pessoaGsuiteVOFiltro) {
		this.pessoaGsuiteVOFiltro = pessoaGsuiteVOFiltro;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = Constantes.EMPTY;
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = Constantes.EMPTY;
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

	public DataModelo getControleConsultaPessoaGsuite() {
		if (controleConsultaPessoaGsuite == null) {
			controleConsultaPessoaGsuite = new DataModelo();
		}
		return controleConsultaPessoaGsuite;
	}

	public void setControleConsultaPessoaGsuite(DataModelo controleConsultaPessoaGsuite) {
		this.controleConsultaPessoaGsuite = controleConsultaPessoaGsuite;
	}	
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public ConfiguracaoSeiGsuiteVO getConfiguracaoSeiGsuiteVO() {
		if (configuracaoSeiGsuiteVO == null) {
			configuracaoSeiGsuiteVO = new ConfiguracaoSeiGsuiteVO();
		}
		return configuracaoSeiGsuiteVO;
	}

	public void setConfiguracaoSeiGsuiteVO(ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO) {
		this.configuracaoSeiGsuiteVO = configuracaoSeiGsuiteVO;
	}

	public ConfiguracaoSeiGsuiteUnidadeEnsinoVO getConfiguracaoSeiGsuiteUnidadeEnsinoVO() {
		if (configuracaoSeiGsuiteUnidadeEnsinoVO == null) {
			configuracaoSeiGsuiteUnidadeEnsinoVO = new ConfiguracaoSeiGsuiteUnidadeEnsinoVO();
		}
		return configuracaoSeiGsuiteUnidadeEnsinoVO;
	}

	public void setConfiguracaoSeiGsuiteUnidadeEnsinoVO(ConfiguracaoSeiGsuiteUnidadeEnsinoVO configuracaoSeiGsuiteUnidadeEnsinoVO) {
		this.configuracaoSeiGsuiteUnidadeEnsinoVO = configuracaoSeiGsuiteUnidadeEnsinoVO;
	}

	public String getCampoNavegarAutoriazacao() {
		if (campoNavegarAutoriazacao == null) {
			campoNavegarAutoriazacao = Constantes.EMPTY;
		}
		return campoNavegarAutoriazacao;
	}

	public void setCampoNavegarAutoriazacao(String campoNavegarAutoriazacao) {
		this.campoNavegarAutoriazacao = campoNavegarAutoriazacao;
	}
	
	public boolean isMarcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO() {
		return marcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO;
	}

	public void setMarcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO(boolean marcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO) {
		this.marcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO = marcaTodasConfiguracaoSeiGsuiteUnidadeEnsinoVO;
	}

	public AdminSdkIntegracaoVO getAdminSdkGoogleVOFiltro() {
		if (adminSdkGoogleVOFiltro == null) {
			adminSdkGoogleVOFiltro = new AdminSdkIntegracaoVO();
		}
		return adminSdkGoogleVOFiltro;
	}

	public void setAdminSdkGoogleVOFiltro(AdminSdkIntegracaoVO adminSdkGoogleVOFiltro) {
		this.adminSdkGoogleVOFiltro = adminSdkGoogleVOFiltro;
	}

	public DataModelo getControleConsultaAdminSdkGoogle() {
		if (controleConsultaAdminSdkGoogle == null) {
			controleConsultaAdminSdkGoogle = new DataModelo();
		}
		return controleConsultaAdminSdkGoogle;
	}

	public void setControleConsultaAdminSdkGoogle(DataModelo controleConsultaAdminSdkGoogle) {
		this.controleConsultaAdminSdkGoogle = controleConsultaAdminSdkGoogle;
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

	public boolean isAlterarSenhaSeiGsuite() {
		return alterarSenhaSeiGsuite;
	}

	public void setAlterarSenhaSeiGsuite(boolean alterarSenhaSeiGsuite) {
		this.alterarSenhaSeiGsuite = alterarSenhaSeiGsuite;
	}

//	public DataModelo getDataModeloClassroom() {
//		if (dataModeloClassroom == null) {
//			dataModeloClassroom = new DataModelo();
//		}
//		return dataModeloClassroom;
//	}
//
//	public void setDataModeloClassroom(DataModelo dataModeloClassroom) {
//		this.dataModeloClassroom = dataModeloClassroom;
//	}

	public DataModelo getDataModeloGoogleMeet() {
		if (dataModeloGoogleMeet == null) {
			dataModeloGoogleMeet = new DataModelo();
			dataModeloGoogleMeet.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		return dataModeloGoogleMeet;
	}

	public void setDataModeloGoogleMeet(DataModelo dataModeloGoogleMeet) {
		this.dataModeloGoogleMeet = dataModeloGoogleMeet;
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
			ano = Constantes.EMPTY;
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Constantes.EMPTY;
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = Constantes.EMPTY;
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = Constantes.EMPTY;
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public String getSituacaoTurma() {
		if (situacaoTurma == null) {
			situacaoTurma = Constantes.AB;
		}
		return situacaoTurma;
	}

	public void setSituacaoTurma(String situacaoTurma) {
		this.situacaoTurma = situacaoTurma;
	}

	public List<SelectItem> getListaSelectItemSituacaoTurma() {
		if (listaSelectItemSituacaoTurma == null) {
			listaSelectItemSituacaoTurma = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoTurma.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
			listaSelectItemSituacaoTurma.add(new SelectItem(Constantes.AB, Constantes.Aberta));
			listaSelectItemSituacaoTurma.add(new SelectItem(Constantes.FE, Constantes.Fechada));					
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

	public List<GoogleMeetConvidadoVO> getGoogleMeetConvidadoVOs() {
		if (googleMeetConvidadoVOs == null) {
			googleMeetConvidadoVOs = new ArrayList<>(0);
		}
		return googleMeetConvidadoVOs;
	}

	public void setGoogleMeetConvidadoVOs(List<GoogleMeetConvidadoVO> googleMeetConvidadoVOs) {
		this.googleMeetConvidadoVOs = googleMeetConvidadoVOs;
	}

	public GoogleMeetVO getGoogleMeetVO() {
		if (googleMeetVO == null) {
			googleMeetVO = new GoogleMeetVO();
		}
		return googleMeetVO;
	}

	public void setGoogleMeetVO(GoogleMeetVO googleMeetVO) {
		this.googleMeetVO = googleMeetVO;
	}

	public ClassroomGoogleVO getClassroomGoogleVO() {
		if (classroomGoogleVO == null) {
			classroomGoogleVO = new ClassroomGoogleVO();
		}
		return classroomGoogleVO;
	}

	public void setClassroomGoogleVO(ClassroomGoogleVO classroomGoogleVO) {
		this.classroomGoogleVO = classroomGoogleVO;
	}

	public DataModelo getDataModeloPessoaGsuiteImportadas() {
		if (dataModeloPessoaGsuiteImportadas == null) {
			dataModeloPessoaGsuiteImportadas = new DataModelo();
		}
		return dataModeloPessoaGsuiteImportadas;
	}

	public void setDataModeloPessoaGsuiteImportadas(DataModelo dataModeloPessoaGsuiteImportadas) {
		this.dataModeloPessoaGsuiteImportadas = dataModeloPessoaGsuiteImportadas;
	}

	public PessoaGsuiteVO getPessoaGsuiteVOFiltroImportadas() {
		if (pessoaGsuiteVOFiltroImportadas == null) {
			pessoaGsuiteVOFiltroImportadas = new PessoaGsuiteVO();
		}
		return pessoaGsuiteVOFiltroImportadas;
	}

	public void setPessoaGsuiteVOFiltroImportadas(PessoaGsuiteVO pessoaGsuiteVOFiltroImportadas) {
		this.pessoaGsuiteVOFiltroImportadas = pessoaGsuiteVOFiltroImportadas;
	}
	
	public String getControleAba() {
		if (controleAba == null) {
			controleAba = "tabDadosAutenticacaoSeiGsuite";
		}
		return controleAba;
	}

	public void setControleAba(String controleAba) {
		this.controleAba = controleAba;
	}

	public String getNovaSenhaContaGsuite() {
		if (novaSenhaContaGsuite == null) {
			novaSenhaContaGsuite = Constantes.EMPTY;
		}
		return novaSenhaContaGsuite;
	}

	public void setNovaSenhaContaGsuite(String novaSenhaContaGsuite) {
		this.novaSenhaContaGsuite = novaSenhaContaGsuite;
	}

	public String getNovaSenhaContaGsuiteConfirmacao() {
		if (novaSenhaContaGsuiteConfirmacao == null) {
			novaSenhaContaGsuiteConfirmacao = Constantes.EMPTY;
		}
		return novaSenhaContaGsuiteConfirmacao;
	}

	public void setNovaSenhaContaGsuiteConfirmacao(String novaSenhaContaGsuiteConfirmacao) {
		this.novaSenhaContaGsuiteConfirmacao = novaSenhaContaGsuiteConfirmacao;
	}
	
	public List<ClassroomGoogleVO> getListaClassroomGoogleVO() {
		if (listaClassroomGoogleVO == null) {
			listaClassroomGoogleVO = new ArrayList<>();
		}
		return listaClassroomGoogleVO;
	}

	public void setListaClassroomGoogleVO(List<ClassroomGoogleVO> listaClassroomGoogleVO) {
		this.listaClassroomGoogleVO = listaClassroomGoogleVO;
	}
	
	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO =  new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public boolean isApresentarBotaoOperacaoProfessorAuxiliar() {
		return getListaClassroomGoogleVO().stream().anyMatch(p-> p.isSelecionado());
	}
	
	public Long getQtdClassroomSelecionadas() {
		return getListaClassroomGoogleVO().stream().filter(p-> p.isSelecionado()).count();
	}
	
	
	public boolean isMarcaTodasClassroom() {
		return marcaTodasClassroom;
	}

	public void setMarcaTodasClassroom(boolean marcaTodasClassroom) {
		this.marcaTodasClassroom = marcaTodasClassroom;
	}
	
	
	
	
	
	
	
	
}
