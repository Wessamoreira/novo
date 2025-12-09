/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package controle.crm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CalendarioAgendaPessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.ReagendamentoCompromissoVO;
import negocio.comuns.crm.TipoProspectVO;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoVisaoAgendaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * 
 * @author edigarjr
 */
@Controller("AgendaPessoaControle")
@Scope("viewScope")
public class AgendaPessoaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private AgendaPessoaVO agendaPessoaVO;
	private Date dataCompromissoAdiado;
	private String horaCompromissoAdiado;
	private Boolean selecionarTodosCompromisso;
	private String campoConsultarPessoa;
	private String valorConsultarPessoa;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private AgendaPessoaHorarioVO agendaPessoaHorarioVO;
	private CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO;
	private Date filtroDia;
	private Date filtroMes;
	private InteracaoWorkflowVO interacaoWorkflowVO;
	private String abrirFecharModalInteracao;
	private String campoConsultarProspect;
	private String valorConsultarProspect;
	private List<ProspectsVO> listaConsultarProspect;
	private String campoConsultarCampanha;
	private String valorConsultarCampanha;
	private List<CampanhaVO> listaConsultarCampanha;
	private Boolean manterRichModalAberto;
	private ProspectsVO prospectsVO;
	private Boolean habilitarConsultaProspect;
	private List<TipoProspectVO> listaConsultaTipoProspect;
	private List<SelectItem> listaSelectItemCampanha;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private Integer codigoResponsavel;
	private List<SelectItem> listaSelectItemResponsavel;
	private List<CompromissoAgendaPessoaHorarioVO> listaCompromissoAgendaContatosPendentes;
	private CampanhaVO campanhaVO;
	private Boolean considerarFeriados;
	private Boolean considerarSabado;
	private Boolean permitirAlterarDataCompromisso;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CalendarioHorarioAulaVO<CalendarioAgendaPessoaVO> calendarioCompromisso;
	private Boolean permitirConsultarAgendaOutroConsultor;
	private Boolean permitirVisualizacaoAgendaOutrasUnidades;
	private Boolean permitirMatriculaDiretamenteAgenda;
	private String popUpMatricula;
	private List<CompromissoAgendaPessoaHorarioVO> listaCompromissoFuturo;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemCursoInteresse;
	private Boolean possuiPermissaoParaExcluirCompromisso;
	private String filtroHora;
	private String filtroNomeProspect;
	private String filtroNomeCursoInteresse;
	private String filtroNomeResponsavel;
	private TipoCompromissoEnum tipoCompromisso;
	private CompromissoAgendaPessoaHorarioVO compromissoVO;
	private String horaFimCompromissoAdiado;
	private String horaIntevaloInicioCompromissoAdiado;
	private String horaIntevaloFimCompromissoAdiado;
	private Integer intervaloAgendaCompromisso;
	

	public AgendaPessoaControle() throws Exception {
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		definirEscopoBuscaProspectComBaseNaPermissaoAcesso();
	}

	@PostConstruct
	public void verificarPermissaoConsultarOutrarUnidades(){
		if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			getUnidadeEnsinoVO().setCodigo(0);
		}
	}

	public Boolean verificarUsuarioPossuiPermissaoConsulta(String identificadorAcaoPermissao) {
		Boolean liberar = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao, getUsuarioLogado());
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		return liberar;
	}

	private void definirEscopoBuscaProspectComBaseNaPermissaoAcesso() {
		setPermitirVisualizacaoAgendaOutrasUnidades(verificarUsuarioPossuiPermissaoConsulta("verAgendaOutrasUnidadesVisaoAdministrativaCRM"));
		setPermitirMatriculaDiretamenteAgenda(verificarUsuarioPossuiPermissaoConsulta("Agenda_permitirMatriculaDiretamenteAgenda"));
		// setPermitirConsultarAgendaOutroConsultor(verificarUsuarioPossuiPermissaoConsulta("Agenda_verAgendaOutrosConsultores"));
		// if (!getPermitirConsultarAgendaOutroConsultor()) {
		// try {
		// this.setCodigoResponsavel(getUsuarioLogado().getPessoa().getCodigo());
		// } catch (Exception e) {
		// //setMensagemDetalhada("msg_erro", e.getMessage());
		// }
		// }
	}

	@PostConstruct
	public void realizarValidacaoSeExisteAgendaPessoa() {
		try {
			setCampoConsultarCampanha("");
			setCampoConsultarProspect("");
			setValorConsultarCampanha("");
			setValorConsultarProspect("");
			setListaConsultarCampanha(new ArrayList<CampanhaVO>());
			setListaConsultarProspect(new ArrayList<ProspectsVO>());
			setPermitirAlterarDataCompromisso(true);			
			CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getSessionMap().get("compromissoAgendaPessoaHorarioVO");
			if (compromissoAgendaPessoaHorarioVO != null) {
				context().getExternalContext().getSessionMap().remove("compromissoAgendaPessoaHorarioVO");				
				setFiltroDia(compromissoAgendaPessoaHorarioVO.getDataCompromisso());
			} else {
				if(getFiltroDia() == null) {
					setFiltroDia(new Date());
				}
				
			}
			montarListaSelectItemUnidadeEnsino();
			if (getUsuarioLogado().getPessoa().getCodigo() != 0) {
				setAgendaPessoaVO(getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(getUsuarioLogado().getPessoa(), getUsuarioLogado()));
				realizarCriacaoAgendaPessoa();
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				setAgendaPessoaHorarioVO(getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getFiltroDia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado()));
				// setAgendaPessoaHorarioVO(getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoAdministrador(getCampanhaVO().getCodigo(),
				// getFiltroDia(),
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(getAgendaPessoaHorarioVO());
				getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
				
				montarListaSelectItemResponsavel();
			}
			montarListaSelectItemCampanha();
			consultarCalendarioAgendaPessoa(MesAnoEnum.getMesData(getFiltroDia()), Uteis.getAnoData(getFiltroDia()));
			setMensagemID("", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void iniciarMatriculaAluno() {
		try {
			if (getInteracaoWorkflowVO().getProspect().getNome().equals("")) {
				setPopUpMatricula("");
				throw new Exception("O campo NOME (prospect) deve ser informado.");
			}
			if (getInteracaoWorkflowVO().getProspect().getCpf().equals("")) {
				setPopUpMatricula("");
				throw new Exception("O campo CPF (prospect) deve ser informado.");
			}
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			request.getSession().setAttribute("matricula", true);
			request.getSession().setAttribute("interacaoWorkFlowMatriculaAgenda", getInteracaoWorkflowVO());
			// getFacadeFactory().getProspectsFacade().alterarSemValidarDados(getInteracaoWorkflowVO().getProspect(),
			// true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			getInteracaoWorkflowVO().setNovoObj(false);
			setPopUpMatricula("abrirPopup('../academico/alunoForm.xhtml?iniciarMatriculaAgenda=" + getInteracaoWorkflowVO().getProspect().getCodigo() + "','Aluno', 950, 595);");
			// getFacadeFactory().getInteracaoWorkflowFacade().persistir(getInteracaoWorkflowVO());
			setMensagemDetalhada("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			// setAbrirModalMensagemErro(true);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerCompromissoFuturo() throws Exception {
		try {
			CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoFuturoItens");
			getFacadeFactory().getAgendaPessoaFacade().realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(obj, getAgendaPessoaVO(), false, getUsuarioLogado());
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluir(obj, getUsuarioLogado());
			verificarComromissoFuturo();
			setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean verificarComromissoFuturo() throws Exception {
		setListaCompromissoFuturo(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoFuturoProspect(getInteracaoWorkflowVO().getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		return !getListaCompromissoFuturo().isEmpty();
	}

	public void montarListaSelectItemCursoInteresse() throws Exception {
		List<CursoInteresseVO> resultadoConsulta = null;
		@SuppressWarnings("rawtypes")
		Iterator i = null;
		try {
			resultadoConsulta = getInteracaoWorkflowVO().getProspect().getCursoInteresseVOs();

			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (resultadoConsulta.isEmpty()) {
				objs.add(new SelectItem(0, ""));
				List<UnidadeEnsinoCursoVO> todosCursosUnidades = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino("", getInteracaoWorkflowVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				i = todosCursosUnidades.iterator();
				while (i.hasNext()) {
					UnidadeEnsinoCursoVO curso = (UnidadeEnsinoCursoVO) i.next();
					objs.add(new SelectItem(curso.getCurso().getCodigo(), curso.getCurso().getNome()));
				}
			} else {
				i = resultadoConsulta.iterator();
				while (i.hasNext()) {
					CursoInteresseVO obj = (CursoInteresseVO) i.next();
					objs.add(new SelectItem(obj.getCurso().getCodigo(), obj.getCurso().getNome()));
				}
			}
			setListaSelectItemCursoInteresse(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemTurno() throws Exception {
		montarListaSelectItemTurno(getInteracaoWorkflowVO().getUnidadeEnsino().getCodigo(), getInteracaoWorkflowVO().getCurso().getCodigo());
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTurno(Integer unidadeEnsino, Integer curso) throws Exception {
		try {
			List<TurnoVO> resultadoConsulta = getFacadeFactory().getTurnoFacade().consultarPorUnidadeEnsinoCurso(unidadeEnsino, curso, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemTurno(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", resultadoConsulta.size() > 1));
		} catch (Exception e) {
			throw e;
		}
	}

	public void validarDadosMatricularAluno() {
		try {
			CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItens");
			getInteracaoWorkflowVO().setCompromissoAgendaPessoaHorario(obj);
			getFacadeFactory().getProspectsFacade().carregarDados(obj.getProspect(), getUsuarioLogado());
			getInteracaoWorkflowVO().setProspect(obj.getProspect());
			getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
			getInteracaoWorkflowVO().getResponsavel().getPessoa().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
			getInteracaoWorkflowVO().getResponsavel().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
			getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());
			getInteracaoWorkflowVO().setCurso(obj.getCursoInteresseProspect());
			getInteracaoWorkflowVO().setDataInicio(new Date());
			if (getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo() != 0 && getInteracaoWorkflowVO().getProspect().getUnidadeEnsino().getCodigo() == 0) {
				getInteracaoWorkflowVO().getProspect().getUnidadeEnsino().setCodigo(getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo());
			}
			getInteracaoWorkflowVO().getUnidadeEnsino().setCodigo(getInteracaoWorkflowVO().getProspect().getUnidadeEnsino().getCodigo());
			montarListaSelectItemCursoInteresse();
			montarListaSelectItemTurno(getInteracaoWorkflowVO().getUnidadeEnsino().getCodigo(), getInteracaoWorkflowVO().getCurso().getCodigo());
			if (verificarComromissoFuturo()) {
				setPopUpMatricula("RichFaces.$('panelCompromissoFuturo').show();");
			} else {
				setPopUpMatricula("RichFaces.$('panelIniciarMatricula').show();");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCalendarioAgendaPessoa(MesAnoEnum mesAnoEnum, Integer ano) throws Exception {
		setCalendarioCompromisso(getFacadeFactory().getAgendaPessoaFacade().realizarGeracaoCalendarioAgendaPessoa(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa(), mesAnoEnum, ano, TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO));
	}

	public void visualizarCalendarioProximoMes() {
		try {
			if (getCalendarioCompromisso().getMesAno().getMesAnoPosterior().equals(MesAnoEnum.JANEIRO)) {
				consultarCalendarioAgendaPessoa(getCalendarioCompromisso().getMesAno().getMesAnoPosterior(), Integer.parseInt(getCalendarioCompromisso().getAno()) + 1);
			} else {
				consultarCalendarioAgendaPessoa(getCalendarioCompromisso().getMesAno().getMesAnoPosterior(), Integer.parseInt(getCalendarioCompromisso().getAno()));
			}
			setMensagemID("", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarCalendarioAnteriorMes() {
		try {
			if (getCalendarioCompromisso().getMesAno().getMesAnoAnterior().equals(MesAnoEnum.DEZEMBRO)) {
				consultarCalendarioAgendaPessoa(getCalendarioCompromisso().getMesAno().getMesAnoAnterior(), Integer.parseInt(getCalendarioCompromisso().getAno()) - 1);
			} else {
				consultarCalendarioAgendaPessoa(getCalendarioCompromisso().getMesAno().getMesAnoAnterior(), Integer.parseInt(getCalendarioCompromisso().getAno()));
			}
			setMensagemID("", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarDiaCalendario() {
		CalendarioAgendaPessoaVO calendarioAgendaPessoaVO = (CalendarioAgendaPessoaVO) context().getExternalContext().getRequestMap().get("diaCompromissoItens");
		if (calendarioAgendaPessoaVO != null) {
			setFiltroDia(calendarioAgendaPessoaVO.getDataCompromisso());
			if (!getAgendaPessoaVO().getIsVisaoAgendaDia()) {
				realizarAlteracaoVisaoDia();
			}
			realizarBuscaFiltroDia();
		}
	}

	public void realizarCriacaoAgendaPessoa() throws Exception {
		if (getAgendaPessoaVO().getNovoObj()) {
			getAgendaPessoaVO().setPessoa(getUsuarioLogado().getPessoa());
			getFacadeFactory().getAgendaPessoaFacade().persistir(getAgendaPessoaVO(), getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(getAgendaPessoaVO(), getFiltroDia(), getUsuarioLogado());
		} else {
			AgendaPessoaHorarioVO agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getAgendaPessoaVO().getPessoa().getCodigo(), getCampanhaVO().getCodigo(), getFiltroDia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
			if (!agendaHorario.getNovoObj()) {
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(agendaHorario);
			} else {
				getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(getAgendaPessoaVO(), getFiltroDia(), getUsuarioLogado());
			}
			agendaHorario = null;
		}
   		getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
		if (getAgendaPessoaVO().getPessoa().getExisteImagem()) {
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getAgendaPessoaVO().getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), null, "foto_usuario.png", false));
		}
	}

	public void realizarAlteracaoVisaoDia() {
		try {
			setFiltroDia(UteisData.getPrimeiroDataMes(getFiltroMes()));
			getAgendaPessoaVO().setTipoVisaoAgenda(TipoVisaoAgendaEnum.DIA);
			realizarValidacaoSeExisteAgendaPessoa();
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void realizarAlteracaoVisaoMes() {
		try {
			setFiltroMes(getFiltroDia());
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getAgendaPessoaVO().setTipoVisaoAgenda(TipoVisaoAgendaEnum.MES);
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			getFacadeFactory().getAgendaPessoaFacade().realizarGeracaoCalendarioMes(getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa(), getAgendaPessoaVO(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroMes(), getUsuarioLogado());
			Ordenacao.ordenarLista(getAgendaPessoaVO().getAgendaPessoaHorarioVOs(), "ordemApresentacao");
			if (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				montarListaSelectItemResponsavel();
			}
			montarListaSelectItemCampanha();
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO(), false);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBuscaPorCampanha() {
		try {
			executarMontagemDadosPorCampanha();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBuscaPorUnidadeEnsino() {
		try {
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				setCodigoResponsavel(getAgendaPessoaVO().getPessoa().getCodigo());
			}
			if (getAgendaPessoaVO().getIsVisaoAgendaDia()) {

				AgendaPessoaHorarioVO agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getFiltroDia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
				if (!agendaHorario.getNovoObj()) {
					getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(agendaHorario);
				} else {
					getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(getAgendaPessoaVO(), getFiltroDia(), getUsuarioLogado());
				}
				agendaHorario = null;
			} else if (getAgendaPessoaVO().getIsVisaoAgendaMes()) {

				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
				getFacadeFactory().getAgendaPessoaFacade().realizarGeracaoCalendarioMes(getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa(), getAgendaPessoaVO(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroMes(), getUsuarioLogado());
			}
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getCodigoResponsavel(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			Ordenacao.ordenarLista(getAgendaPessoaVO().getAgendaPessoaHorarioVOs(), "ordemApresentacao");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemCampanha() throws Exception {
		Calendar cal = Calendar.getInstance();
		if (getAgendaPessoaVO().getIsVisaoAgendaMes()) {
			cal.setTime(getFiltroMes());
		} else {
			cal.setTime(getFiltroDia());
		}
		List<AgendaPessoaHorarioVO> resultadoConsulta = null;
		try {
			if (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				resultadoConsulta = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoObterNomeCampanha(getCodigoResponsavel(), cal.getTime(), getAgendaPessoaVO().getTipoVisaoAgenda(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else {
				resultadoConsulta = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoObterNomeCampanha(getAgendaPessoaVO().getPessoa().getCodigo(), cal.getTime(), getAgendaPessoaVO().getTipoVisaoAgenda(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaSelectItemCampanha(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			resultadoConsulta = null;
			cal = null;
		}
	}

	public void montarListaSelectItemResponsavel() throws Exception {
		Calendar cal = Calendar.getInstance();
		if (getAgendaPessoaVO().getIsVisaoAgendaMes()) {
			cal.setTime(getFiltroMes());
		} else {
			cal.setTime(getFiltroDia());
		}
		try {
			@SuppressWarnings("unchecked")
			List<PessoaVO> resultadoConsulta = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoAdministradorObterNomeResponsavel(getUnidadeEnsinoVO().getCodigo(), cal.getTime(), getAgendaPessoaVO().getTipoVisaoAgenda(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemResponsavel(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nomeResumido"));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Rotina responsï¿½vel por disponibilizar um novo objeto da classe
	 * <code>AgendaPessoa</code> para ediï¿½ï¿½o pelo usuï¿½rio da
	 * aplicaï¿½ï¿½o.
	 */
	public String novo() {
		setAgendaPessoaVO(new AgendaPessoaVO());
		// inicializarListasSelectItemTodosComboBox();
		setAgendaPessoaHorarioVO(new AgendaPessoaHorarioVO());
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("agendaPessoaForm.xhtml");
	}

	/**
	 * Rotina responsï¿½vel por disponibilizar os dados de um objeto da classe
	 * <code>AgendaPessoa</code> para alteraï¿½ï¿½o. O objeto desta classe ï¿½
	 * disponibilizado na session da pï¿½gina (request) para que o JSP
	 * correspondente possa disponibilizï¿½-lo para ediï¿½ï¿½o.
	 */
	public String editar() {
		AgendaPessoaVO obj = (AgendaPessoaVO) context().getExternalContext().getRequestMap().get("agendaPessoa");
		inicializarAtributosRelacionados(obj);
		obj.setNovoObj(new Boolean(false));
		setAgendaPessoaVO(obj);
		// inicializarListasSelectItemTodosComboBox();
		setAgendaPessoaHorarioVO(new AgendaPessoaHorarioVO());
		setMensagemID("msg_dados_editar", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("agendaPessoaForm.xhtml");
	}

	/**
	 * Mï¿½todo responsï¿½vel inicializar objetos relacionados a classe
	 * <code>AgendaPessoaVO</code>. Esta inicializaï¿½ï¿½o ï¿½ necessï¿½ria por
	 * exigï¿½ncia da tecnologia JSF, que nï¿½o trabalha com valores nulos para
	 * estes atributos.
	 */
	public void inicializarAtributosRelacionados(AgendaPessoaVO obj) {
		if (obj.getPessoa() == null) {
			obj.setPessoa(new PessoaVO());
		}
	}

	/**
	 * Rotina responsï¿½vel por gravar no BD os dados editados de um novo objeto
	 * da classe <code>AgendaPessoa</code>. Caso o objeto seja novo (ainda nï¿½o
	 * gravado no BD) ï¿½ acionado a operaï¿½ï¿½o <code>incluir()</code>. Caso
	 * contrï¿½rio ï¿½ acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistï¿½ncia o objeto nï¿½o ï¿½ gravado, sendo re-apresentado para o
	 * usuï¿½rio juntamente com uma mensagem de erro.
	 */
	public String persistir() {
		try {
			getFacadeFactory().getAgendaPessoaFacade().persistir(agendaPessoaVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("agendaPessoaForm.xhtml");
	}

	public String persistirCompromissoRealizado() {
		try {
			CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItens");
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().gravarCompromissoRealizado(obj.getCodigo(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("agendaPessoaForm.xhtml");
	}

	/**
	 * Operaï¿½ï¿½o responsï¿½vel por processar a exclusï¿½o um objeto da classe
	 * <code>AgendaPessoaVO</code> Apï¿½s a exclusï¿½o ela automaticamente
	 * aciona a rotina para uma nova inclusï¿½o.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getAgendaPessoaFacade().excluir(getAgendaPessoaVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("agendaPessoaForm.xhtml");
	}

	// /* Mï¿½todo responsï¿½vel por adicionar um novo objeto da classe
	// <code>AgendaPessoaHorario</code>
	// * para o objeto <code>agendaPessoaVO</code> da classe
	// <code>AgendaPessoa</code>
	// */
	// public String adicionarAgendaPessoaHorario() throws Exception {
	// try {
	// if (!getAgendaPessoaVO().getCodigo().equals(0)) {
	// agendaPessoaHorarioVO.setAgendaPessoa(getAgendaPessoaVO());
	// }
	// getFacadeFactory().getAgendaPessoaFacade().adicionarObjAgendaPessoaHorarioVOs(
	// getAgendaPessoaVO(), getAgendaPessoaHorarioVO());
	// this.setAgendaPessoaHorarioVO(new AgendaPessoaHorarioVO());
	// setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
	// } catch (ConsistirException e) {
	// setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	// } finally {
	// return "editar";
	// }
	// }

	/*
	 * Mï¿½todo responsï¿½vel por disponibilizar dados de um objeto da classe
	 * <code>AgendaPessoaHorario</code> para ediï¿½ï¿½o pelo usuï¿½rio.
	 */
	// public String editarAgendaPessoaHorario() throws Exception {
	// AgendaPessoaHorarioVO obj =
	// (AgendaPessoaHorarioVO)context().getExternalContext().getRequestMap().get("agendaPessoaHorario");
	// setAgendaPessoaHorarioVO(obj);
	// return "editar";
	// }

	/*
	 * Mï¿½todo responsï¿½vel por remover um novo objeto da classe
	 * <code>AgendaPessoaHorario</code> do objeto <code>agendaPessoaVO</code> da
	 * classe <code>AgendaPessoa</code>
	 */
	// public String removerAgendaPessoaHorario() throws Exception {
	// AgendaPessoaHorarioVO obj =
	// (AgendaPessoaHorarioVO)context().getExternalContext().getRequestMap().get("agendaPessoaHorario");
	// getFacadeFactory().getAgendaPessoaFacade().excluirObjAgendaPessoaHorarioVOs(
	// getAgendaPessoaVO(), obj.getDia());
	// setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
	// return "editar";
	// }
	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	// public void montarListaSelectItemUnidadeEnsino(String prm) throws
	// Exception {
	// List resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
	// Iterator i = resultadoConsulta.iterator();
	// List objs = new ArrayList(0);
	// objs.add(new SelectItem(0, ""));
	// while (i.hasNext()) {
	// UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
	// objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
	// }
	// Uteis.liberarListaMemoria(resultadoConsulta);
	// setListaSelectItemUnidadeEnsino(objs);
	// }
	// /**
	// * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	// <code>UnidadeEnsino</code>.
	// * Buscando todos os objetos correspondentes a entidade
	// <code>UnidadeEnsino</code>. Esta rotina nï¿½o recebe parï¿½metros para
	// filtragem de dados, isto ï¿½
	// * importante para a inicializaï¿½ï¿½o dos dados da tela para o
	// acionamento por meio requisiï¿½ï¿½es Ajax.
	// */
	// public void montarListaSelectItemUnidadeEnsino() {
	// try {
	// montarListaSelectItemUnidadeEnsino("");
	// } catch (Exception e) {
	// //System.out.println("MENSAGEM => " + e.getMessage());;
	// }
	// }
	// /**
	// * Mï¿½todo responsï¿½vel por consultar dados da entidade <code><code> e
	// montar o atributo <code>nome</code>
	// * Este atributo ï¿½ uma lista (<code>List</code>) utilizada para definir
	// os valores a serem apresentados no ComboBox correspondente
	// */
	// public List consultarUnidadeEnsinoPorNome(String nomePrm) throws
	// Exception {
	// List lista =
	// getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
	// getUnidadeEnsinoLogado().getCodigo().intValue(), false,
	// Uteis.NIVELMONTARDADOS_TODOS,
	// getUsuarioLogado());
	// return lista;
	// }
	// /**
	// * Mï¿½todo responsï¿½vel por inicializar a lista de valores
	// (<code>SelectItem</code>) para todos os ComboBox's.
	// */
	// public void inicializarListasSelectItemTodosComboBox() {
	// montarListaSelectItemUnidadeEnsino();
	// }
	@SuppressWarnings("unchecked")
	public void consultarTipoProspect() throws Exception {
		setListaConsultaTipoProspect(getFacadeFactory().getProspectsFacade().consultarTipoProspect(getProspectsVO().getCpf(), getProspectsVO().getCnpj(),  getProspectsVO().getCodigo()));
		setHabilitarConsultaProspect(true);
	}

	public void incluirProspect() {
		try {
			getFacadeFactory().getProspectsFacade().persistir(getProspectsVO(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosReferenteProspect() {
		setProspectsVO(new ProspectsVO());
	}

	public void limparDadosReferenteTipoProspect() {
		setHabilitarConsultaProspect(true);
		getProspectsVO().setCodigo(0);
		getProspectsVO().setNovoObj(true);
		getProspectsVO().setNome("");
		getProspectsVO().setCEP("");
		getProspectsVO().setEndereco("");
		getProspectsVO().setSetor("");
		getProspectsVO().setCidade(new CidadeVO());
		getProspectsVO().setEmailPrincipal("");
		getProspectsVO().setEmailSecundario("");
		getProspectsVO().setSkype("");
		getProspectsVO().setCelular("");
		getProspectsVO().setTelefoneComercial("");
		getProspectsVO().setTelefoneRecado("");
		getProspectsVO().setTelefoneResidencial("");
		getProspectsVO().setUnidadeEnsino(new UnidadeEnsinoVO());
		if (getProspectsVO().getFisico()) {
			getProspectsVO().setCnpj("");
			getProspectsVO().setRazaoSocial("");
		} else if (getProspectsVO().getJuridico()) {
			getProspectsVO().setCpf("");
			getProspectsVO().setSexo("");
			getProspectsVO().setRg("");
			getProspectsVO().setDataNascimento(new Date());
		}
	}

	public String getAbrirModalProspect() {
		if (getHabilitarConsultaProspect()) {
			setHabilitarConsultaProspect(false);
			if (getListaConsultaTipoProspect().isEmpty()) {
				return "RichFaces.$('panelTipoProspect').hide();";
			} else if (getListaConsultaTipoProspect().size() == 1) {
				selecionarProspectPesquisaCpfCnpjSemModal();
				return "RichFaces.$('panelTipoProspect').hide();";
			} else {
				return "RichFaces.$('panelTipoProspect').hide();";
			}
		}
		return "RichFaces.$('panelTipoProspect').hide();";
	}

	public void gravarNovaInteracao() {
		try {
			getFacadeFactory().getInteracaoWorkflowFacade().incluirSemValidarDados(getInteracaoWorkflowVO(), getUsuarioLogado());
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().gravarCompromissoRealizadoComEtapa(getInteracaoWorkflowVO().getCompromissoAgendaPessoaHorario().getCodigo(), 0, getUsuarioLogado());
			if (getUsuarioLogado().getPessoa().getCodigo() != 0) {
				setAgendaPessoaVO(getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(getUsuarioLogado().getPessoa(), getUsuarioLogado()));
				realizarCriacaoAgendaPessoa();
			}
			setInteracaoWorkflowVO(new InteracaoWorkflowVO());
			setAbrirFecharModalInteracao("RichFaces.$('panelNovaInteracao').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setAbrirFecharModalInteracao("RichFaces.$('panelNovaInteracao').show()");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarDadosNovaInteracao() {
		CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItens");
		getInteracaoWorkflowVO().setCompromissoAgendaPessoaHorario(obj);
		getInteracaoWorkflowVO().setProspect(obj.getProspect());
		getInteracaoWorkflowVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
		getInteracaoWorkflowVO().getResponsavel().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
		getInteracaoWorkflowVO().setHoraInicio(Uteis.getHoraAtual());
		getInteracaoWorkflowVO().setDataInicio(new Date());
		setAbrirFecharModalInteracao("");
	}

	public void selecionarProspectPesquisaCpfCnpjSemModal() {
		TipoProspectVO obj = (TipoProspectVO) getListaConsultaTipoProspect().get(0);
		try {
			selecionarProspectPesquisaCpfCnpj(obj);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarProspectPesquisaCpfCnpj(TipoProspectVO obj) throws Exception {
		try {
			if (obj.getPerfisProspect().equals("PESSOA")) {
				setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
				if (!getProspectsVO().getArquivoFoto().isNovoObj()) {
					setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getProspectsVO().getArquivoFoto(), PastaBaseArquivoEnum.IMAGEM.toString().toLowerCase(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
				}
			} else if (obj.getPerfisProspect().equals("PARCEIRO") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
				setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorParceiroCnpj(obj.getCnpj(), getProspectsVO(), getUsuarioLogado()));
			} else if (obj.getPerfisProspect().equals("PARCEIRO") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.FISICO)) {
				setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorParceiroCpf(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
			} else if (obj.getPerfisProspect().equals("FORNECEDOR") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
				setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorFornecedorCnpj(obj.getCnpj(), getProspectsVO(), getUsuarioLogado()));
			} else if (obj.getPerfisProspect().equals("FORNECEDOR") && getProspectsVO().getTipoProspect().equals(TipoProspectEnum.FISICO)) {
				setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorFornecedorCpf(obj.getCpf(), getProspectsVO(), getUsuarioLogado()));
			} else if (obj.getPerfisProspect().equals("PROSPECT")) {
				setProspectsVO(getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(getProspectsVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			} else {
				setProspectsVO(getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorUnidade(obj.getCnpj(), obj.getNome(), getProspectsVO(), getUsuarioLogado()));
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.isEmpty()) {
				resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			}
			if (resultadoConsulta.size() == 1) {
				setUnidadeEnsinoVO(resultadoConsulta.get(0));
			}
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", resultadoConsulta.size() > 1));
		} catch (Exception e) {
			throw e;
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void consultarFuncionario() {
		try {
			setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultar(getValorConsultaFuncionario(), getCampoConsultaFuncionario(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", UteisJSF.internacionalizar("prt_Funcionario_nome")));
		itens.add(new SelectItem("CPF", UteisJSF.internacionalizar("prt_Funcionario_CPF")));
		itens.add(new SelectItem("matricula", UteisJSF.internacionalizar("prt_Funcionario_matricula")));
		itens.add(new SelectItem("cargo", UteisJSF.internacionalizar("prt_Funcionario_cargo")));
		itens.add(new SelectItem("departamento", UteisJSF.internacionalizar("prt_Funcionario_departamento")));
		return itens;
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().setPessoa(obj.getPessoa());
		setCampoConsultaFuncionario("");
		setValorConsultaFuncionario("");
		setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
	}

	public void realizarBuscaMesAtual() {
		try {
			setFiltroMes(new Date());
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getCampanhaVO().setCodigo(0);
			setCodigoResponsavel(0);
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			getFacadeFactory().getAgendaPessoaFacade().realizarGeracaoCalendarioMes(getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa(), getAgendaPessoaVO(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroMes(), getUsuarioLogado());
			Ordenacao.ordenarLista(getAgendaPessoaVO().getAgendaPessoaHorarioVOs(), "ordemApresentacao");
			limparMensagem();
			if (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				montarListaSelectItemResponsavel();
			}
			montarListaSelectItemCampanha();
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBuscaMesAnterior() {
		try {
			setFiltroMes(Uteis.obterDataAntigaPorMes(getFiltroMes(), 1));			
			
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getCampanhaVO().setCodigo(0);
			setCodigoResponsavel(0);
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			getFacadeFactory().getAgendaPessoaFacade().realizarGeracaoCalendarioMes(getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa(), getAgendaPessoaVO(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroMes(), getUsuarioLogado());
			Ordenacao.ordenarLista(getAgendaPessoaVO().getAgendaPessoaHorarioVOs(), "ordemApresentacao");
			limparMensagem();
			if (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				montarListaSelectItemResponsavel();
			}
			montarListaSelectItemCampanha();
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBuscaProximoMes() {
		try {
			setFiltroMes(Uteis.obterDataAvancadaPorMes(getFiltroMes(), 1));
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getCampanhaVO().setCodigo(0);
			setCodigoResponsavel(0);
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			getFacadeFactory().getAgendaPessoaFacade().realizarGeracaoCalendarioMes(getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa(), getAgendaPessoaVO(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroMes(), getUsuarioLogado());
			Ordenacao.ordenarLista(getAgendaPessoaVO().getAgendaPessoaHorarioVOs(), "ordemApresentacao");
			limparMensagem();
			if (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				montarListaSelectItemResponsavel();
			}
			montarListaSelectItemCampanha();
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBuscaDiaAtual() {
		try {
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getCampanhaVO().setCodigo(0);
			setCodigoResponsavel(0);
			if (getAgendaPessoaVO().getIsVisaoAgendaDia()) {
				realizarAtualizacaoAgendaHorarioPessoa(getFiltroDia());
			} else {
				realizarAtualizacaoAgendaHorarioPessoa(getFiltroMes());
			}
			setMensagem("");
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBuscaDiaAnterior() {
		try {
			setFiltroDia(Uteis.obterDataAntiga(getFiltroDia(), 1));
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getCampanhaVO().setCodigo(0);
			setCodigoResponsavel(0);
			AgendaPessoaHorarioVO agendaHorario = new AgendaPessoaHorarioVO();
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getAgendaPessoaVO().getPessoa().getCodigo(), getCampanhaVO().getCodigo(), getFiltroDia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
			} else {

				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoAdministrador(getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroDia(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getTipoCompromisso(), getUsuarioLogado());
				montarListaSelectItemResponsavel();
			}
			if (!agendaHorario.getNovoObj()) {
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(agendaHorario);
			} else {
				getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(getAgendaPessoaVO(), getFiltroDia(), getUsuarioLogado());
			}
			montarListaSelectItemCampanha();
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
			
			agendaHorario = null;
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBuscaProximoDia() {
		try {
			setFiltroDia(Uteis.obterDataAvancada(getFiltroDia(), 1));
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getCampanhaVO().setCodigo(0);
			setCodigoResponsavel(0);
			AgendaPessoaHorarioVO agendaHorario = new AgendaPessoaHorarioVO();
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getAgendaPessoaVO().getPessoa().getCodigo(), getCampanhaVO().getCodigo(), getFiltroDia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
			} else {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoAdministrador(getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroDia(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getTipoCompromisso(), getUsuarioLogado());
				montarListaSelectItemResponsavel();
			}
			if (!agendaHorario.getNovoObj()) {
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(agendaHorario);
			} else {
				getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(getAgendaPessoaVO(), getFiltroDia(), getUsuarioLogado());
			}
			montarListaSelectItemCampanha();
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
			agendaHorario = null;
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarVisualizacaoDiaMes() {
		try {
			AgendaPessoaHorarioVO obj = (AgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("agendaPessoaHorarioItens");
			setFiltroDia(obj.getDataCompromisso());
			AgendaPessoaHorarioVO agendaHorario = new AgendaPessoaHorarioVO();
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getAgendaPessoaVO().setTipoVisaoAgenda(TipoVisaoAgendaEnum.DIA);
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getAgendaPessoaVO().getPessoa().getCodigo(), getCampanhaVO().getCodigo(), getFiltroDia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
			} else {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getFiltroDia(),false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
				montarListaSelectItemResponsavel();
			}
			if (!agendaHorario.getNovoObj()) {
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(agendaHorario);
			} else {
				getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(getAgendaPessoaVO(), new Date(), getUsuarioLogado());
			}
			montarListaSelectItemCampanha();
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
			
			agendaHorario = null;
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarBuscaFiltroDia() {
		try {
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getCampanhaVO().setCodigo(0);
			setCodigoResponsavel(0);
			AgendaPessoaHorarioVO agendaHorario = new AgendaPessoaHorarioVO();
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getAgendaPessoaVO().getPessoa().getCodigo(), getCampanhaVO().getCodigo(), getFiltroDia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
			} else {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoAdministrador(getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroDia(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
				montarListaSelectItemResponsavel();
			}
			if (!agendaHorario.getNovoObj()) {
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(agendaHorario);
			} else {
				getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(getAgendaPessoaVO(), getFiltroDia(), getUsuarioLogado());
			}
			montarListaSelectItemCampanha();
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
			
			agendaHorario = null;
			limparMensagem();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarMatriculaProspect() {
		try {
			setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
			setCompromissoAgendaPessoaHorarioVO((CompromissoAgendaPessoaHorarioVO) getRequestMap().get("compromissoItens"));
			if (getCompromissoAgendaPessoaHorarioVO().getMatriculaVOs().isEmpty()) {
				getCompromissoAgendaPessoaHorarioVO().setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultarMatriculaProspectApresentarCompromisso(getCompromissoAgendaPessoaHorarioVO().getProspect().getCodigo(), getCompromissoAgendaPessoaHorarioVO().getDataCompromisso(), getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@Override
	public void limparMensagem() {
		setMensagem("");
		setMensagemDetalhada("", "");
	}

	public void limparCompromisso() {
		try {
			setMensagem("");
			setMensagemDetalhada("", "");
			setPermitirAlterarDataCompromisso(true);
			setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
			if ((!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) || (getCodigoResponsavel().intValue() == 0)) {
				getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().setPessoa(getUsuarioLogado().getPessoa());
			} else if (getCodigoResponsavel().intValue() != 0) {
				getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(getCodigoResponsavel(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por processar a consulta na entidade
	 * <code>Campanha</code> por meio dos parametros informados no richmodal.
	 * Esta rotina ï¿½ utilizada fundamentalmente por requisiï¿½ï¿½es Ajax, que
	 * realizam busca pelos parï¿½mentros informados no richModal montando
	 * automaticamente o resultado da consulta para apresentaï¿½ï¿½o.
	 */
	public void consultarCampanha() {
		try {
			List<CampanhaVO> objs = new ArrayList<CampanhaVO>(0);
			if (getCampoConsultarCampanha().equals("descricao")) {
				objs = getFacadeFactory().getCampanhaFacade().consultarPorDescricao(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultarCampanha().equals("unidadeensino")) {
				objs = getFacadeFactory().getCampanhaFacade().consultarPorUnidadeEnsino(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultarCampanha().equals("curso")) {
				objs = getFacadeFactory().getCampanhaFacade().consultarPorCurso(getValorConsultarCampanha(), "AT", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			// if (getCampoConsultarCampanha().equals("situacao")) {
			// objs =
			// getFacadeFactory().getCampanhaFacade().consultarPorSituacao(getValorConsultarCampanha(),
			// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			// }
			setListaConsultarCampanha(objs);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultarCampanha().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCampanha() throws Exception {
		CampanhaVO obj = (CampanhaVO) context().getExternalContext().getRequestMap().get("campanhaItens");
		if (getMensagemDetalhada().equals("")) {
			this.getCompromissoAgendaPessoaHorarioVO().setCampanha(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarCampanha());
		this.setValorConsultarCampanha("");
		this.setCampoConsultarCampanha("");
	}

	public void limparCampoCampanha() {
		this.getCompromissoAgendaPessoaHorarioVO().setCampanha(new CampanhaVO());
	}

	/**
	 * Rotina responsï¿½vel por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaComboCampanha() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("unidadeensino", "Unidade"));
		itens.add(new SelectItem("curso", "Curso"));
		// itens.add(new SelectItem("situacao", "Situaï¿½ï¿½o"));
		return itens;
	}

	/**
	 * Mï¿½todo responsï¿½vel por processar a consulta na entidade
	 * <code>Prospects</code> por meio dos parametros informados no richmodal.
	 * Esta rotina ï¿½ utilizada fundamentalmente por requisiï¿½ï¿½es Ajax, que
	 * realizam busca pelos parï¿½mentros informados no richModal montando
	 * automaticamente o resultado da consulta para apresentaï¿½ï¿½o.
	 */
	@SuppressWarnings("unchecked")
	public void consultarProspect() {
		try {
			setListaConsultarProspect(getFacadeFactory().getProspectsFacade().consultar(getValorConsultarProspect(), getUnidadeEnsinoLogado().getCodigo(), getCampoConsultarProspect(), false, getUsuarioLogado(), ""));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultarProspect().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarProspect() throws Exception {
		try {
			ProspectsVO obj = (ProspectsVO) context().getExternalContext().getRequestMap().get("prospectsItens");
			if (obj.getInativo()) {
				throw new Exception(UteisJSF.internacionalizar("msg_Agenda_prospectInativo"));
			}
			getCompromissoAgendaPessoaHorarioVO().setProspect(getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getListaConsultarProspect().clear();
			setValorConsultarProspect("");
			setCampoConsultarProspect("");
			limparMensagem();
		} catch (Exception e) {
			getListaConsultarProspect().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparCampoProspect() {
		this.getCompromissoAgendaPessoaHorarioVO().setProspect(new ProspectsVO());
	}

	public void limparCamposNovoProspect() {
		montarListaSelectItemUnidadeEnsino();
		setProspectsVO(new ProspectsVO());
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public void realizarPrrenchimentoCompromissoParaAgendamentoMes() {
		try {
			setMensagem("");
			setMensagemDetalhada("", "");
			setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
			AgendaPessoaHorarioVO obj = (AgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("agendaPessoaHorarioItens");
			getCompromissoAgendaPessoaHorarioVO().setDataCompromisso(obj.getDataCompromisso());
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().setPessoa(getUsuarioLogado().getPessoa());
			} else if (getCodigoResponsavel().intValue() != 0) {
				getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(getCodigoResponsavel(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getAbrirRichModalCompromisso() {
		if (getManterRichModalAberto()) {
			return "";
		}
		return "RichFaces.$('panelNovoCompromisso').hide()";
	}

	/*public void adiarCompromissosSelecionado() {
		try {
			if (!getListaCompromissoAgendaContatosPendentes().isEmpty()) {
				Iterator<CompromissoAgendaPessoaHorarioVO> i = getListaCompromissoAgendaContatosPendentes().iterator();
				boolean primeiro = true;
				while (i.hasNext()) {
					CompromissoAgendaPessoaHorarioVO comp = (CompromissoAgendaPessoaHorarioVO) i.next();
					if (comp.getCompromissoSelecionado()) {
						comp.setDataCompromisso(getDataCompromissoAdiado());
						if (primeiro) {
							comp.setHora(getHoraCompromissoAdiado());
							primeiro = false;
						} else {
							comp.setHora(Uteis.somarHorario(getHoraCompromissoAdiado(), 1));
						}
						comp.reagendarCompromissoParaDataFutura(getDataCompromissoAdiado(), getUsuarioLogado());
						CompromissoAgendaPessoaHorarioVO.validarDados(comp);
						getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().alterarSemValidarDados(comp, getUsuarioLogado());
					}
				}
			}
		} catch (Exception e) {
			setManterRichModalAberto(true);
			try {
				getFacadeFactory().getAgendaPessoaFacade().realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(getCompromissoAgendaPessoaHorarioVO(), getAgendaPessoaVO(), false, getUsuarioLogado());
			} catch (Exception ex) {
				setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}*/

	public void adicionarCompromissoAgendaPessoaHorario() {
		try {
			if (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				AgendaPessoaVO agenda = getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().getPessoa(), getUsuarioLogado());
				if (agenda.getCodigo() == 0) {
					agenda.setPessoa(getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().getPessoa());
					getFacadeFactory().getAgendaPessoaFacade().persistir(agenda, getUsuarioLogado());
				}
				getFacadeFactory().getAgendaPessoaFacade().realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(getCompromissoAgendaPessoaHorarioVO(), agenda, true, getUsuarioLogado());
			} else {
				getFacadeFactory().getAgendaPessoaFacade().realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(getCompromissoAgendaPessoaHorarioVO(), getAgendaPessoaVO(), true, getUsuarioLogado());
			}
			realizarAtualizacaoAgendaHorarioPessoa(getCompromissoAgendaPessoaHorarioVO().getDataCompromisso());
			setFiltroDia(getCompromissoAgendaPessoaHorarioVO().getDataCompromisso());
			setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
			setManterRichModalAberto(false);
			consultarCalendarioAgendaPessoa(MesAnoEnum.getMesData(getCompromissoAgendaPessoaHorarioVO().getDataCompromisso()), Uteis.getAnoData(getCompromissoAgendaPessoaHorarioVO().getDataCompromisso()));
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setManterRichModalAberto(true);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
	}

	public void realizarAtualizacaoAgendaHorarioPessoa(Date dataFiltro) throws Exception {
		// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
		// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
		// unidadeFiltro = 0;
		// }
		AgendaPessoaHorarioVO agendaHorario = new AgendaPessoaHorarioVO();
		if (getAgendaPessoaVO().getIsVisaoAgendaDia()) {
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getAgendaPessoaVO().getPessoa().getCodigo(), getCampanhaVO().getCodigo(), dataFiltro, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
			} else if (getCodigoResponsavel().intValue() != 0 && getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().equals(getCodigoResponsavel())) {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), dataFiltro, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUsuarioLogado());
			} else {
				setCodigoResponsavel(0);
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoAdministrador(getCampanhaVO().getCodigo(), dataFiltro, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(agendaHorario);
		} else {
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaPorMesPorAnoPorAgendaPessoaEspecificaDoMes(getAgendaPessoaVO().getPessoa().getCodigo(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), dataFiltro, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else if (getCodigoResponsavel().intValue() != 0 && getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo().equals(getCodigoResponsavel())) {
				agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaPorMesPorAnoPorAgendaPessoaEspecificaDoMes(getCodigoResponsavel(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), dataFiltro, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else {
				setCodigoResponsavel(0);
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
				getFacadeFactory().getAgendaPessoaFacade().realizarGeracaoCalendarioMes(getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa(), getAgendaPessoaVO(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), dataFiltro, getUsuarioLogado());
				Ordenacao.ordenarLista(getAgendaPessoaVO().getAgendaPessoaHorarioVOs(), "ordemApresentacao");
				limparMensagem();
			}
			if (agendaHorario.getCodigo() != null && agendaHorario.getCodigo().intValue() != 0) {
				int index = 0;
				for (AgendaPessoaHorarioVO objExistente : getAgendaPessoaVO().getAgendaPessoaHorarioVOs()) {
					if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(objExistente.getDataCompromisso(), dataFiltro)) {
						getAgendaPessoaVO().getAgendaPessoaHorarioVOs().set(index, agendaHorario);
						break;
					}
					index++;
				}
			}
		}
		if (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
			montarListaSelectItemResponsavel();
		}
		montarListaSelectItemCampanha();
		getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
		getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
	}

	public void editarCompromissoAgendaPessoaHorario() {
		try {
			CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItens");
			setCompromissoAgendaPessoaHorarioVO(obj);
			setPermitirAlterarDataCompromisso(true);
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerCompromissoAgendaPessoaHorario() {
		try {
			CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromissoItens");
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluir(obj, getUsuarioLogado());
			getFacadeFactory().getAgendaPessoaFacade().realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(obj, getAgendaPessoaVO(), false, getUsuarioLogado());
			setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarConsultarContatosPendentesNaoInicializados() {
		try {
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				setCodigoResponsavel(getAgendaPessoaVO().getPessoa().getCodigo());
			}
			// else {
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			// }
			setSelecionarTodosCompromisso(false);
			setHoraCompromissoAdiado("");
			setHoraFimCompromissoAdiado("");
			setHoraIntevaloInicioCompromissoAdiado("");
			setHoraIntevaloFimCompromissoAdiado("");
			setListaCompromissoAgendaContatosPendentes(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoPorCodigoPessoaContatosPendentes(getCodigoResponsavel(), getUnidadeEnsinoVO().getCodigo(), true, false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarConsultarContatosPendentesNaoFinalizados() {
		try {
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
				setCodigoResponsavel(getAgendaPessoaVO().getPessoa().getCodigo());
			}
			// else {
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			// }
			setSelecionarTodosCompromisso(false);
			setHoraCompromissoAdiado("");
			setHoraFimCompromissoAdiado("");
			setHoraIntevaloInicioCompromissoAdiado("");
			setHoraIntevaloFimCompromissoAdiado("");
			setListaCompromissoAgendaContatosPendentes(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarCompromissoPorCodigoPessoaContatosPendentes(getCodigoResponsavel(), getUnidadeEnsinoVO().getCodigo(), false, false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarSelecaoTodasContatosPendentesNaoFinalizados() {
//		if (getSelecionarTodosCompromisso()) {
//			getCompromissoAgendaPessoaHorarioVO().setCompromissoSelecionado(true);
//	 	} else {
//			getCompromissoAgendaPessoaHorarioVO().setCompromissoSelecionado(false);
//	 	}
		for (CompromissoAgendaPessoaHorarioVO compromisso : getListaCompromissoAgendaContatosPendentes()) {
			if (getSelecionarTodosCompromisso()) {
				compromisso.setCompromissoSelecionado(true);
			} else {
				compromisso.setCompromissoSelecionado(false);
			}
		}
	}

	public void realizarRemarcacaoCompromisso() {
		try {
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarRemarcacoesCompromissos(getListaCompromissoAgendaContatosPendentes(), getDataCompromissoAdiado(), getHoraCompromissoAdiado(), true, true,getHoraFimCompromissoAdiado(),getHoraIntevaloInicioCompromissoAdiado(),getHoraIntevaloFimCompromissoAdiado(),getIntervaloAgendaCompromisso(), getUsuarioLogado());
			Iterator<CompromissoAgendaPessoaHorarioVO> i = getListaCompromissoAgendaContatosPendentes().iterator();
			while (i.hasNext()) {
				CompromissoAgendaPessoaHorarioVO obj = i.next();
				if (obj.getCompromissoSelecionado()) {
					consultarCalendarioAgendaPessoa(MesAnoEnum.getMesData(obj.getDataCompromisso()), Uteis.getAnoData(obj.getDataCompromisso()));
					i.remove();
				}
			}
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
//			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			executarMontagemDadosPorCampanha();
			setManterRichModalAberto(false);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setManterRichModalAberto(true);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarExclusaoCompromisso() {
		try {
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarExclusaoCompromissos(getListaCompromissoAgendaContatosPendentes(), getUsuarioLogado());
			Iterator<CompromissoAgendaPessoaHorarioVO> i = getListaCompromissoAgendaContatosPendentes().iterator();
			while (i.hasNext()) {
				CompromissoAgendaPessoaHorarioVO obj = i.next();
				if (obj.getCompromissoSelecionado()) {
					i.remove();
				}
			}
			// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
			// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
			// unidadeFiltro = 0;
			// }
			getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getAgendaPessoaVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por processar a consulta na entidade
	 * <code>Pessoa</code> por meio dos parametros informados no richmodal. Esta
	 * rotina ï¿½ utilizada fundamentalmente por requisiï¿½ï¿½es Ajax, que
	 * realizam busca pelos parï¿½mentros informados no richModal montando
	 * automaticamente o resultado da consulta para apresentaï¿½ï¿½o.
	 */
	// public void selecionarPessoa() throws Exception {
	// PessoaVO obj = (PessoaVO)
	// context().getExternalContext().getRequestMap().get("pessoa");
	// if (getMensagemDetalhada().equals("")) {
	// this.getAgendaPessoaVO().setPessoa(obj);
	// }
	// Uteis.liberarListaMemoria(this.getListaConsultarPessoa());
	// this.setValorConsultarPessoa(null);
	// this.setCampoConsultarPessoa(null);
	// }
	//
	// public void limparCampoPessoa() {
	// this.getAgendaPessoaVO().setPessoa( new PessoaVO());
	// }
	/**
	 * Rotina responsï¿½vel por atribui um javascript com o mï¿½todo de mascara
	 * para campos do tipo Data, CPF, CNPJ, etc.
	 */
	// public String getMascaraConsulta() {
	// return "";
	// }
	/**
	 * Rotina responsï¿½vel por organizar a paginaï¿½ï¿½o entre as pï¿½ginas
	 * resultantes de uma consulta.
	 */

	// public String inicializarConsultar() {
	// setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	// return "consultar";
	// }
	/**
	 * Operaï¿½ï¿½o que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuaï¿½ï¿½o do Garbage Coletor do
	 * Java. A mesma ï¿½ automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		agendaPessoaVO = null;
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		agendaPessoaHorarioVO = null;
	}

	public AgendaPessoaHorarioVO getAgendaPessoaHorarioVO() {
		return agendaPessoaHorarioVO;
	}

	public void setAgendaPessoaHorarioVO(AgendaPessoaHorarioVO agendaPessoaHorarioVO) {
		this.agendaPessoaHorarioVO = agendaPessoaHorarioVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultarPessoa() {
		return campoConsultarPessoa;
	}

	public void setCampoConsultarPessoa(String campoConsultarPessoa) {
		this.campoConsultarPessoa = campoConsultarPessoa;
	}

	public String getValorConsultarPessoa() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return valorConsultarPessoa.toUpperCase();
		}
		return valorConsultarPessoa;
	}

	public void setValorConsultarPessoa(String valorConsultarPessoa) {
		this.valorConsultarPessoa = valorConsultarPessoa;
	}

	public AgendaPessoaVO getAgendaPessoaVO() {
		if (agendaPessoaVO == null) {
			agendaPessoaVO = new AgendaPessoaVO();
		}
		return agendaPessoaVO;
	}

	public CompromissoAgendaPessoaHorarioVO getCompromissoAgendaPessoaHorarioVO() {
		if (compromissoAgendaPessoaHorarioVO == null) {
			compromissoAgendaPessoaHorarioVO = new CompromissoAgendaPessoaHorarioVO();
		}
		return compromissoAgendaPessoaHorarioVO;
	}

	public void setCompromissoAgendaPessoaHorarioVO(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO) {
		this.compromissoAgendaPessoaHorarioVO = compromissoAgendaPessoaHorarioVO;
	}

	public void setAgendaPessoaVO(AgendaPessoaVO agendaPessoaVO) {
		this.agendaPessoaVO = agendaPessoaVO;
	}

	public String getCampoConsultarCampanha() {
		return campoConsultarCampanha;
	}

	public void setCampoConsultarCampanha(String campoConsultarCampanha) {
		this.campoConsultarCampanha = campoConsultarCampanha;
	}

	public String getValorConsultarCampanha() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return valorConsultarCampanha.toUpperCase();
		}
		return valorConsultarCampanha;
	}

	public void setValorConsultarCampanha(String valorConsultarCampanha) {
		this.valorConsultarCampanha = valorConsultarCampanha;
	}

	public List<CampanhaVO> getListaConsultarCampanha() {
		return listaConsultarCampanha;
	}

	public void setListaConsultarCampanha(List<CampanhaVO> listaConsultarCampanha) {
		this.listaConsultarCampanha = listaConsultarCampanha;
	}

	public String getCampoConsultarProspect() {
		return campoConsultarProspect;
	}

	public void setCampoConsultarProspect(String campoConsultarProspect) {
		this.campoConsultarProspect = campoConsultarProspect;
	}

	public String getValorConsultarProspect() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return valorConsultarProspect.toUpperCase();
		}
		return valorConsultarProspect;
	}

	public void setValorConsultarProspect(String valorConsultarProspect) {
		this.valorConsultarProspect = valorConsultarProspect;
	}

	public List<ProspectsVO> getListaConsultarProspect() {
		return listaConsultarProspect;
	}

	public void setListaConsultarProspect(List<ProspectsVO> listaConsultarProspect) {
		this.listaConsultarProspect = listaConsultarProspect;
	}

	public Boolean getManterRichModalAberto() {
		if (manterRichModalAberto == null) {
			manterRichModalAberto = false;
		}
		return manterRichModalAberto;
	}

	public void setManterRichModalAberto(Boolean manterRichModalAberto) {
		this.manterRichModalAberto = manterRichModalAberto;
	}

	public Integer getQuantidadeColunaAgenda() {
		if (getAgendaPessoaVO().getIsVisaoAgendaDia()) {
			return 1;
		} else if (getAgendaPessoaVO().getIsVisaoAgendaSemana() || getAgendaPessoaVO().getIsVisaoAgendaMes()) {
			return 7;
		}
		return 1;
	}

	public Integer getQuantidadeAgendaPessoaHorario() {
		if (getAgendaPessoaVO().getIsVisaoAgendaDia()) {
			return getAgendaPessoaVO().getAgendaPessoaHorarioVOs().size();
		} else if (getAgendaPessoaVO().getIsVisaoAgendaSemana() || getAgendaPessoaVO().getIsVisaoAgendaMes()) {
			return getAgendaPessoaVO().getAgendaPessoaHorarioVOs().size();
		}
		return 1;
	}

	public Date getFiltroDia() {
		if (filtroDia == null) {
			filtroDia = new Date();
		}
		return filtroDia;
	}

	public void setFiltroDia(Date filtroDia) {
		this.filtroDia = filtroDia;
	}

	public Date getFiltroMes() {
		if (filtroMes == null) {
			filtroMes = new Date();
		}
		return filtroMes;
	}

	public void setFiltroMes(Date filtroMes) {
		this.filtroMes = filtroMes;
	}

	public String getApresentarFiltroDia_Texto() {
		// if (Uteis.getDiaMesData(getFiltroDia()) < 10) {
		// return
		// Uteis.getDiaSemanaEnum(getFiltroDia()).getDescricao().toLowerCase() +
		// " - 0" + Uteis.getDiaMesData(getFiltroDia()) + " de " +
		// Uteis.getMesReferenciaData(getFiltroDia());
		// }
		// return
		// Uteis.getDiaSemanaEnum(getFiltroDia()).getDescricao().toLowerCase() +
		// " - " + Uteis.getDiaMesData(getFiltroDia()) + " de " +
		// Uteis.getMesReferenciaData(getFiltroDia());
		return Uteis.getDiaSemanaEnum(getFiltroDia()).getDescricao();

	}

	public String getApresentarFiltroMes_Texto() {
		Integer mes = Uteis.getMesData(getFiltroMes());
		return Uteis.getMesReferenciaExtenso(mes.toString()) + " - " + Uteis.getAnoData(getFiltroMes());

	}

	public ProspectsVO getProspectsVO() {
		if (prospectsVO == null) {
			prospectsVO = new ProspectsVO();
		}
		return prospectsVO;
	}

	public void setProspectsVO(ProspectsVO prospectsVO) {
		this.prospectsVO = prospectsVO;
	}

	public Boolean getHabilitarConsultaProspect() {
		if (habilitarConsultaProspect == null) {
			habilitarConsultaProspect = false;
		}
		return habilitarConsultaProspect;
	}

	public void setHabilitarConsultaProspect(Boolean habilitarConsultaProspect) {
		this.habilitarConsultaProspect = habilitarConsultaProspect;
	}

	public List<TipoProspectVO> getListaConsultaTipoProspect() {
		if (listaConsultaTipoProspect == null) {
			listaConsultaTipoProspect = new ArrayList<TipoProspectVO>(0);
		}
		return listaConsultaTipoProspect;
	}

	public void setListaConsultaTipoProspect(List<TipoProspectVO> listaConsultaTipoProspect) {
		this.listaConsultaTipoProspect = listaConsultaTipoProspect;
	}

	public CampanhaVO getCampanhaVO() {
		if (campanhaVO == null) {
			campanhaVO = new CampanhaVO();
		}
		return campanhaVO;
	}

	public void setCampanhaVO(CampanhaVO campanhaVO) {
		this.campanhaVO = campanhaVO;
	}

	public List<SelectItem> getListaSelectItemCampanha() {
		if (listaSelectItemCampanha == null) {
			listaSelectItemCampanha = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCampanha;
	}

	public void setListaSelectItemCampanha(List<SelectItem> listaSelectItemCampanha) {
		this.listaSelectItemCampanha = listaSelectItemCampanha;
	}

	public String getIsRichModalProspectAberto() {
		if (getManterRichModalAberto()) {
			return "";
		}
		return "RichFaces.$('panelNovoCompromisso').hide();";
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
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

	public Integer getCodigoResponsavel() {
		if (codigoResponsavel == null) {
			codigoResponsavel = 0;
		}
		return codigoResponsavel;
	}

	public void setCodigoResponsavel(Integer codigoResponsavel) {
		this.codigoResponsavel = codigoResponsavel;
	}

	public List<SelectItem> getListaSelectItemResponsavel() {
		if (listaSelectItemResponsavel == null) {
			listaSelectItemResponsavel = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemResponsavel;
	}

	public void setListaSelectItemResponsavel(List<SelectItem> listaSelectItemResponsavel) {
		this.listaSelectItemResponsavel = listaSelectItemResponsavel;
	}

	public List<CompromissoAgendaPessoaHorarioVO> getListaCompromissoAgendaContatosPendentes() {
		if (listaCompromissoAgendaContatosPendentes == null) {
			listaCompromissoAgendaContatosPendentes = new ArrayList<CompromissoAgendaPessoaHorarioVO>(0);
		}
		return listaCompromissoAgendaContatosPendentes;
	}

	public void setListaCompromissoAgendaContatosPendentes(List<CompromissoAgendaPessoaHorarioVO> listaCompromissoAgendaContatosPendentes) {
		this.listaCompromissoAgendaContatosPendentes = listaCompromissoAgendaContatosPendentes;
	}

	public Date getDataCompromissoAdiado() {
		if (dataCompromissoAdiado == null) {
			dataCompromissoAdiado = Uteis.obterDataAvancada(new Date(), 1);
		}
		return dataCompromissoAdiado;
	}

	public void setDataCompromissoAdiado(Date dataCompromissoAdiado) {
		this.dataCompromissoAdiado = dataCompromissoAdiado;
	}

	public String getHoraCompromissoAdiado() {
		if (horaCompromissoAdiado == null) {
			horaCompromissoAdiado = "";
		}
		return horaCompromissoAdiado;
	}

	public void setHoraCompromissoAdiado(String horaCompromissoAdiado) {
		this.horaCompromissoAdiado = horaCompromissoAdiado;
	}

	public Boolean getSelecionarTodosCompromisso() {
		if (selecionarTodosCompromisso == null) {
			selecionarTodosCompromisso = false;
		}
		return selecionarTodosCompromisso;
	}

	public void setSelecionarTodosCompromisso(Boolean selecionarTodosCompromisso) {
		this.selecionarTodosCompromisso = selecionarTodosCompromisso;
	}

	public Boolean getConsiderarFeriados() {
		if (considerarFeriados == null) {
			considerarFeriados = false;
		}
		return considerarFeriados;
	}

	public void setConsiderarFeriados(Boolean considerarFeriados) {
		this.considerarFeriados = considerarFeriados;
	}

	public Boolean getConsiderarSabado() {
		if (considerarSabado == null) {
			considerarSabado = false;
		}
		return considerarSabado;
	}

	public void setConsiderarSabado(Boolean considerarSabado) {
		this.considerarSabado = considerarSabado;
	}

	public Boolean getPermitirAlterarDataCompromisso() {
		if (permitirAlterarDataCompromisso == null) {
			permitirAlterarDataCompromisso = true;
		}
		return permitirAlterarDataCompromisso;
	}

	public void setPermitirAlterarDataCompromisso(Boolean permitirAlterarDataCompromisso) {
		this.permitirAlterarDataCompromisso = permitirAlterarDataCompromisso;
	}

	public Boolean getContatoPendentesNaoInicializadoAbaixoDoAceitavel() throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getQtdAceitavelContatosPendentesNaoIniciados() >= getAgendaPessoaVO().getQtdContatosPendentesNaoIniciados()) {
			return true;
		}
		return false;
	}

	public Boolean getContatoPendentesNaoInicializadoAceitavelPorLimite() throws Exception {
		if ((getConfiguracaoGeralPadraoSistema().getQtdAceitavelContatosPendentesNaoIniciados() < getAgendaPessoaVO().getQtdContatosPendentesNaoIniciados()) && (getConfiguracaoGeralPadraoSistema().getQtdLimiteContatosPendentesNaoIniciados() >= getAgendaPessoaVO().getQtdContatosPendentesNaoIniciados())) {
			return true;
		}
		return false;
	}

	public Boolean getContatoPendentesNaoInicializadoAcimaDoLimite() throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getQtdLimiteContatosPendentesNaoIniciados() < getAgendaPessoaVO().getQtdContatosPendentesNaoIniciados()) {
			return true;
		}
		return false;
	}

	public Boolean getContatoPendentesNaoFinalizadoAbaixoDoAceitavel() throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getQtaAceitavelContatosPendentesNaoFinalizados() >= getAgendaPessoaVO().getQtdContatosPendentesNaoFinalizados()) {
			return true;
		}
		return false;
	}

	public Boolean getContatoPendentesNaoFinalizadoAceitavelPorLimite() throws Exception {
		if ((getConfiguracaoGeralPadraoSistema().getQtaAceitavelContatosPendentesNaoFinalizados() < getAgendaPessoaVO().getQtdContatosPendentesNaoFinalizados()) && (getConfiguracaoGeralPadraoSistema().getQtaLimiteContatosPendentesNaoFinalizados() >= getAgendaPessoaVO().getQtdContatosPendentesNaoFinalizados())) {
			return true;
		}
		return false;
	}

	public Boolean getContatoPendentesNaoFinalizadoAcimaDoLimite() throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getQtaLimiteContatosPendentesNaoFinalizados() < getAgendaPessoaVO().getQtdContatosPendentesNaoFinalizados()) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarQuadroContatosPendentes() throws Exception {
		if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa() 
				|| (getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa() && getCodigoResponsavel() != 0)) {
			return true;
		}
		return false;
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

	public InteracaoWorkflowVO getInteracaoWorkflowVO() {
		if (interacaoWorkflowVO == null) {
			interacaoWorkflowVO = new InteracaoWorkflowVO();
		}
		return interacaoWorkflowVO;
	}

	public void setInteracaoWorkflowVO(InteracaoWorkflowVO interacaoWorkflowVO) {
		this.interacaoWorkflowVO = interacaoWorkflowVO;
	}

	public String getAbrirFecharModalInteracao() {
		if (abrirFecharModalInteracao == null) {
			abrirFecharModalInteracao = "RichFaces.$('panelNovaInteracao').hide()";
		}
		return abrirFecharModalInteracao;
	}

	public void setAbrirFecharModalInteracao(String abrirFecharModalInteracao) {
		this.abrirFecharModalInteracao = abrirFecharModalInteracao;
	}

	public CalendarioHorarioAulaVO<CalendarioAgendaPessoaVO> getCalendarioCompromisso() {
		if (calendarioCompromisso == null) {
			calendarioCompromisso = new CalendarioHorarioAulaVO<CalendarioAgendaPessoaVO>();
		}
		return calendarioCompromisso;
	}

	public void setCalendarioCompromisso(CalendarioHorarioAulaVO<CalendarioAgendaPessoaVO> calendarioCompromisso) {
		this.calendarioCompromisso = calendarioCompromisso;
	}

	/**
	 * @return the permitirConsultarAgendaOutroConsultor
	 */
	public Boolean getPermitirConsultarAgendaOutroConsultor() {
		return permitirConsultarAgendaOutroConsultor;
	}

	/**
	 * @param permitirConsultarAgendaOutroConsultor
	 *            the permitirConsultarAgendaOutroConsultor to set
	 */
	public void setPermitirConsultarAgendaOutroConsultor(Boolean permitirConsultarAgendaOutroConsultor) {
		this.permitirConsultarAgendaOutroConsultor = permitirConsultarAgendaOutroConsultor;
	}

	/**
	 * @return the permitirVisualizacaoAgendaOutrasUnidades
	 */
	public Boolean getPermitirVisualizacaoAgendaOutrasUnidades() {
		if (permitirVisualizacaoAgendaOutrasUnidades == null) {
			permitirVisualizacaoAgendaOutrasUnidades = false;
		}
		return permitirVisualizacaoAgendaOutrasUnidades;
	}

	/**
	 * @param permitirVisualizacaoAgendaOutrasUnidades
	 *            the permitirVisualizacaoAgendaOutrasUnidades to set
	 */
	public void setPermitirVisualizacaoAgendaOutrasUnidades(Boolean permitirVisualizacaoAgendaOutrasUnidades) {
		this.permitirVisualizacaoAgendaOutrasUnidades = permitirVisualizacaoAgendaOutrasUnidades;
	}

	/**
	 * @return the permitirMatriculaDiretamenteAgenda
	 */
	public Boolean getPermitirMatriculaDiretamenteAgenda() {
		if (permitirMatriculaDiretamenteAgenda == null) {
			permitirMatriculaDiretamenteAgenda = false;
		}
		return permitirMatriculaDiretamenteAgenda;
	}

	/**
	 * @param permitirMatriculaDiretamenteAgenda
	 *            the permitirMatriculaDiretamenteAgenda to set
	 */
	public void setPermitirMatriculaDiretamenteAgenda(Boolean permitirMatriculaDiretamenteAgenda) {
		this.permitirMatriculaDiretamenteAgenda = permitirMatriculaDiretamenteAgenda;
	}

	/**
	 * @return the popUpMatricula
	 */
	public String getPopUpMatricula() {
		if (popUpMatricula == null) {
			popUpMatricula = "";
		}
		return popUpMatricula;
	}

	/**
	 * @param popUpMatricula
	 *            the popUpMatricula to set
	 */
	public void setPopUpMatricula(String popUpMatricula) {
		this.popUpMatricula = popUpMatricula;
	}

	/**
	 * @return the listaCompromissoFuturo
	 */
	public List<CompromissoAgendaPessoaHorarioVO> getListaCompromissoFuturo() {
		if (listaCompromissoFuturo == null) {
			listaCompromissoFuturo = new ArrayList<CompromissoAgendaPessoaHorarioVO>();
		}
		return listaCompromissoFuturo;
	}

	/**
	 * @param listaCompromissoFuturo
	 *            the listaCompromissoFuturo to set
	 */
	public void setListaCompromissoFuturo(List<CompromissoAgendaPessoaHorarioVO> listaCompromissoFuturo) {
		this.listaCompromissoFuturo = listaCompromissoFuturo;
	}

	/**
	 * @return the listaSelectItemCursoInteresse
	 */
	public List<SelectItem> getListaSelectItemCursoInteresse() {
		if (listaSelectItemCursoInteresse == null) {
			listaSelectItemCursoInteresse = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCursoInteresse;
	}

	/**
	 * @param listaSelectItemCursoInteresse
	 *            the listaSelectItemCursoInteresse to set
	 */
	public void setListaSelectItemCursoInteresse(List<SelectItem> listaSelectItemCursoInteresse) {
		this.listaSelectItemCursoInteresse = listaSelectItemCursoInteresse;
	}

	/**
	 * @return the listaSelectItemTurno
	 */
	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurno;
	}

	/**
	 * @param listaSelectItemTurno
	 *            the listaSelectItemTurno to set
	 */
	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	/**
	 * @return the possuiPermissaoParaExcluirCompromisso
	 */
	public Boolean getPossuiPermissaoParaExcluirCompromisso() {
		if (possuiPermissaoParaExcluirCompromisso == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Agenda_permitirExcluirCompromissoNaoIniciado", getUsuarioLogado());
				possuiPermissaoParaExcluirCompromisso = true;
			} catch (Exception e) {
				possuiPermissaoParaExcluirCompromisso = false;
			}

		}
		return possuiPermissaoParaExcluirCompromisso;
	}
	
	public void executarInicializacaoDadosAdiarCompromissoVisaoDia() {
		try {
			setSelecionarTodosCompromisso(false);
			setHoraCompromissoAdiado("");
			setHoraFimCompromissoAdiado("");
			setHoraIntevaloInicioCompromissoAdiado("");
			setHoraIntevaloFimCompromissoAdiado("");
			setListaCompromissoAgendaContatosPendentes(null);
			
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().stream()
				.map(AgendaPessoaHorarioVO::getListaCompromissoAgendaPessoaHorarioVOs)
				.flatMap(Collection::stream)
				.filter(p -> p.getTipoSituacaoCompromissoEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO) && !p.getReagendado())
				.forEach(getListaCompromissoAgendaContatosPendentes()::add);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private void executarMontagemDadosPorCampanha() throws Exception {
		getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
		if (!getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa()) {
			setCodigoResponsavel(getAgendaPessoaVO().getPessoa().getCodigo());
		}
		// Integer unidadeFiltro = getUnidadeEnsinoVO().getCodigo();
		// if (getPermitirVisualizacaoAgendaOutrasUnidades()) {
		// unidadeFiltro = 0;
		// }
		if (getAgendaPessoaVO().getIsVisaoAgendaDia()) {
			AgendaPessoaHorarioVO agendaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(getUnidadeEnsinoVO().getCodigo(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getFiltroDia(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getTipoCompromisso(), getUsuarioLogado());
			if (!agendaHorario.getNovoObj()) {
				getAgendaPessoaVO().getAgendaPessoaHorarioVOs().add(agendaHorario);
			} else {
				getFacadeFactory().getAgendaPessoaFacade().executarCriacaoAgendaPessoaHorarioDoDia(getAgendaPessoaVO(), getFiltroDia(), getUsuarioLogado());
				
			}
			agendaHorario = null;
		} else if (getAgendaPessoaVO().getIsVisaoAgendaMes()) {
			getAgendaPessoaVO().getAgendaPessoaHorarioVOs().clear();
			getFacadeFactory().getAgendaPessoaFacade().realizarGeracaoCalendarioMes(getLoginControle().getPermissaoAcessoMenuVO().getVisaoAdministradorAgendaPessoa(), getAgendaPessoaVO(), getCodigoResponsavel(), getCampanhaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getFiltroMes(), getUsuarioLogado());
		}
		getFacadeFactory().getAgendaPessoaFacade().executarAtualizacaoContatosPendentes(getAgendaPessoaVO(), getCodigoResponsavel(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
		getFacadeFactory().getAgendaPessoaFacade().atualizarEstatisticasCompromissosAgendaPessoa(getAgendaPessoaVO());
		Ordenacao.ordenarLista(getAgendaPessoaVO().getAgendaPessoaHorarioVOs(), "ordemApresentacao");
	}
	
	public boolean filtrarNomeProspect(Object obj) {
		if (!getFiltroNomeProspect().trim().isEmpty()) {
			if (obj instanceof CompromissoAgendaPessoaHorarioVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((CompromissoAgendaPessoaHorarioVO) obj).getProspect().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeProspect().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarHora(Object obj) {
		if (!getFiltroHora().trim().isEmpty()) {
			if (obj instanceof CompromissoAgendaPessoaHorarioVO) {
				if(((CompromissoAgendaPessoaHorarioVO) obj).getHora().toUpperCase().contains(getFiltroHora().toUpperCase().trim())){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarNomeCursoInteresse(Object obj) {
		if (!getFiltroNomeCursoInteresse().trim().isEmpty()) {
			if (obj instanceof CompromissoAgendaPessoaHorarioVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((CompromissoAgendaPessoaHorarioVO) obj).getCursoInteresseProspect().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeCursoInteresse().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarNomeResponsavel(Object obj) {
		if (!getFiltroNomeResponsavel().trim().isEmpty()) {
			if (obj instanceof CompromissoAgendaPessoaHorarioVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((CompromissoAgendaPessoaHorarioVO) obj).getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getNomeResumido())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeResponsavel().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}

	public String getFiltroHora() {
		if (filtroHora == null) {
			filtroHora = "";
		}
		return filtroHora;
	}

	public void setFiltroHora(String filtroHora) {
		this.filtroHora = filtroHora;
	}

	public String getFiltroNomeProspect() {
		if (filtroNomeProspect == null) {
			filtroNomeProspect = "";
		}
		return filtroNomeProspect;
	}

	public void setFiltroNomeProspect(String filtroNomeProspect) {
		this.filtroNomeProspect = filtroNomeProspect;
	}

	public String getFiltroNomeCursoInteresse() {
		if (filtroNomeCursoInteresse == null) {
			filtroNomeCursoInteresse = "";
		}
		return filtroNomeCursoInteresse;
	}

	public void setFiltroNomeCursoInteresse(String filtroNomeCursoInteresse) {
		this.filtroNomeCursoInteresse = filtroNomeCursoInteresse;
	}

	public String getFiltroNomeResponsavel() {
		if (filtroNomeResponsavel == null) {
			filtroNomeResponsavel = "";
		}
		return filtroNomeResponsavel;
	}

	public void setFiltroNomeResponsavel(String filtroNomeResponsavel) {
		this.filtroNomeResponsavel = filtroNomeResponsavel;
	}
	
	public void realizarNavegacaoProspectTelaInscricao() {
		CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getRequestMap().get("compromisso");
		if (obj != null && !obj.getCodigo().equals(0)) {
			context().getExternalContext().getSessionMap().put("compromissoAgendaPessoaHorarioCandidatoVO", obj);
		}
	}
	
	public List<SelectItem> getListaSelectItemTipoCompromissoVOs() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem(TipoCompromissoEnum.CONTATO, "Contato"));
		itens.add(new SelectItem(TipoCompromissoEnum.TIRE_SUAS_DUVIDAS, "Tire Suas Dúvidas"));
		itens.add(new SelectItem(TipoCompromissoEnum.QUERO_SER_ALUNO, "Quero Ser Aluno"));
		itens.add(new SelectItem(TipoCompromissoEnum.TAREFA, "Tarefa"));
		return itens;
	}

	public TipoCompromissoEnum getTipoCompromisso() {
		return tipoCompromisso;
	}

	public void setTipoCompromisso(TipoCompromissoEnum tipoCompromisso) {
		this.tipoCompromisso = tipoCompromisso;
	}
	
	public void reagendarCompromissoLigacaoAtrasada() throws Exception {
		
		ReagendamentoCompromissoVO reagendamentoCompromissoVO = new ReagendamentoCompromissoVO();
		
		reagendamentoCompromissoVO.setDataInicioCompromisso(getCompromissoAgendaPessoaHorarioVO().getDataCompromissoAnterior());
		
		getCompromissoAgendaPessoaHorarioVO().setDescricao("Reagendamento Automatico - Compromisso Atrasado do dia " + getCompromissoAgendaPessoaHorarioVO().getDataCompromissoAnterior()+".");
		getCompromissoAgendaPessoaHorarioVO().setDataCompromisso(new Date());
		getCompromissoAgendaPessoaHorarioVO().setHora(Uteis.getHoraAtual());
		
		reagendamentoCompromissoVO.setDataInicioCompromisso(getCompromissoAgendaPessoaHorarioVO().getDataCompromissoAnterior());
		reagendamentoCompromissoVO.setDataReagendamentoCompromisso(new Date());
		reagendamentoCompromissoVO.setDataModificacaoReagendamento(new Date());
		reagendamentoCompromissoVO.setCompromissoAgendaPessoaHorario(getCompromissoAgendaPessoaHorarioVO().getCodigo());
		reagendamentoCompromissoVO.setAgendaPessoaHorario(getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getCodigo());
		reagendamentoCompromissoVO.setCampanha(getCompromissoAgendaPessoaHorarioVO().getCampanha().getCodigo());
		reagendamentoCompromissoVO.setResponsavelReagendamento(getUsuarioLogado().getPessoa().getNome());
		setCompromissoVO(getCompromissoAgendaPessoaHorarioVO());
		adicionarCompromissoAgendaPessoaHorario();
		getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirReagendamento(reagendamentoCompromissoVO, getUsuarioLogado());
		
	}
	public CompromissoAgendaPessoaHorarioVO getCompromissoVO() {
		if (compromissoVO == null) {
			compromissoVO = new CompromissoAgendaPessoaHorarioVO();
		}
		return compromissoVO;
	}
	public void setCompromissoVO(CompromissoAgendaPessoaHorarioVO compromissoVO) {
		this.compromissoVO = compromissoVO;
	}
	
	public void reagendarCompromisso() throws Exception {
		
		ReagendamentoCompromissoVO reagendamentoCompromissoVO = new ReagendamentoCompromissoVO();
		reagendamentoCompromissoVO.setDataInicioCompromisso(getCompromissoAgendaPessoaHorarioVO().getDataCompromissoAnterior());
		reagendamentoCompromissoVO.setDataReagendamentoCompromisso(getCompromissoAgendaPessoaHorarioVO().getDataCompromisso());
		reagendamentoCompromissoVO.setDataModificacaoReagendamento(new Date());
		reagendamentoCompromissoVO.setCompromissoAgendaPessoaHorario(getCompromissoAgendaPessoaHorarioVO().getCodigo());
		reagendamentoCompromissoVO.setAgendaPessoaHorario(getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getCodigo());
		reagendamentoCompromissoVO.setCampanha(getCompromissoAgendaPessoaHorarioVO().getCampanha().getCodigo());
		reagendamentoCompromissoVO.setResponsavelReagendamento(getUsuarioLogado().getPessoa().getNome());
		adicionarCompromissoAgendaPessoaHorario();
		getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirReagendamento(reagendamentoCompromissoVO, getUsuarioLogado());
		
	}

	public String getHoraFimCompromissoAdiado() {
		if (horaFimCompromissoAdiado == null) {
			horaFimCompromissoAdiado = "";
		}
		return horaFimCompromissoAdiado;
	}

	public void setHoraFimCompromissoAdiado(String horaFimCompromissoAdiado) {
		this.horaFimCompromissoAdiado = horaFimCompromissoAdiado;
	}

	public String getHoraIntevaloInicioCompromissoAdiado() {
		if (horaIntevaloInicioCompromissoAdiado == null) {
			horaIntevaloInicioCompromissoAdiado = "";
		}
		return horaIntevaloInicioCompromissoAdiado;
	}

	public void setHoraIntevaloInicioCompromissoAdiado(String horaIntevaloInicioCompromissoAdiado) {
		this.horaIntevaloInicioCompromissoAdiado = horaIntevaloInicioCompromissoAdiado;
	}

	public String getHoraIntevaloFimCompromissoAdiado() {
		if (horaIntevaloFimCompromissoAdiado == null) {
			horaIntevaloFimCompromissoAdiado = "";
		}
		return horaIntevaloFimCompromissoAdiado;
	}

	public void setHoraIntevaloFimCompromissoAdiado(String horaIntevaloFimCompromissoAdiado) {
		this.horaIntevaloFimCompromissoAdiado = horaIntevaloFimCompromissoAdiado;
	}

	public Integer getIntervaloAgendaCompromisso() {
		if (intervaloAgendaCompromisso == null) {
			intervaloAgendaCompromisso = 1;
		}
		return intervaloAgendaCompromisso;
	}

	public void setIntervaloAgendaCompromisso(Integer intervaloAgendaCompromisso) {
		this.intervaloAgendaCompromisso = intervaloAgendaCompromisso;
	}
	
	public String getAbrirRichModalContatosPendentesNaoFinalizados() {
		if (getManterRichModalAberto()) {
			return "";
		}
		return "RichFaces.$('panelContatosPendentesNaoFinalizados').hide()";
	}
			
}
