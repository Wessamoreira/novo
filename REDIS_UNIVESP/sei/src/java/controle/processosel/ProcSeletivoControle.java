package controle.processosel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.amazonaws.services.s3.transfer.Upload;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.EixoCursoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.PeriodoChamadaProcSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoDisciplinasProcSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoGabaritoDataVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ProcessoSeletivoProvaDataVO;
import negocio.comuns.processosel.ProvaProcessoSeletivoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.SituacaoGrupoDisciplinaProcSeletivoEnum;
import negocio.comuns.processosel.enumeradores.SituacaoProvaProcessoSeletivoEnum;
import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.processosel.enumeradores.TipoLayoutComprovanteEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.processosel.ProcSeletivo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas procSeletivoForm.jsp procSeletivoCons.jsp) com as funcionalidades da
 * classe <code>ProcSeletivo</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see ProcSeletivo
 * @see ProcSeletivoVO
 */

@Controller("ProcSeletivoControle")
@Scope("viewScope")
@Lazy
public class ProcSeletivoControle extends SuperControle implements Serializable {

	private Date dataInicialRemover;
	private Date dataFinalRemover;
	private String horaEspecificaRemover;

	private static final long serialVersionUID = 1L;
	private ProcSeletivoVO procSeletivoVO;
	private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
	private ProcSeletivoCursoVO procSeletivoCursoVO;
	private ProcSeletivoDisciplinasProcSeletivoVO procSeletivoDisciplinasProcSeletivoVO;
	private ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO;
	private ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoTemp;
	private ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO;
	private ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO;
	private EixoCursoVO eixoCursoVO;
	private Boolean adicionouUnidadeEnsino;
	private Boolean adicionouCurso;
	private Boolean exigeAgendamentoProva;
	private String responsavel_Erro;
	private String curso_Erro;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private List<SelectItem> listaSelectItemTipoLayoutComprovante;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemGrupoDisciplinaProcSeletivo;
	private List<SelectItem> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemQuestionario;
	private List<SelectItem> listaSelectItemDisciplinasProcSeletivo;
	private List<SelectItem> listaSelectItemDisciplinaIdioma;
	private List<SelectItem> listaSelectItemProvaProcessoSeletivo;
	private List<SelectItem> listaSelectItemGabaritoProcessoSeletivo;
	private List<SelectItem> listaSelectItemCampanha;
	private Date dataProvaAlterada;
	private Boolean enviarComunicadoCandidatoAlteracaoDataProva;
	private Boolean permitirFecharModalAlteracaoDataProva;
	private Boolean enviarSmsInformativoAlteracaoDataProva;
	private String corpoEmail;
	private String corpoSms;
	private String motivoAlteracaoDataProva;
	private String assuntoEmail;
	private Integer numeroCandidatoNotificado;
	private List<ComunicacaoInternaVO> listaComunicacaoInternaVO;
	private List listaNivelEducacional;
	private List<SelectItem> listaSelectItemTipoProvaGabarito;
	private Boolean utilizaTermoAceiteProva;
	private ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsinoEixoCurso;
	private List<SelectItem> listaSelectItemEixoCurso;
	private List<Exception> execoes;
	private Boolean utilizarNrVagasPeriodoLetivo;
	private PeriodoChamadaProcSeletivoVO periodoChamadaProcSeletivoVO;

	public ProcSeletivoControle() throws Exception {
		setListaConsulta(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5,
				Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		setControleConsulta(new ControleConsulta());
		setAdicionouUnidadeEnsino(Boolean.FALSE);
		setAdicionouCurso(Boolean.FALSE);
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ProcSeletivo</code> para edição pelo usuário da aplicação.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setProcSeletivoVO(new ProcSeletivoVO());
		setItemProcSeletivoDataProvaVO(new ItemProcSeletivoDataProvaVO());
		inicializarListasSelectItemTodosComboBox();
		inicializarResponsavelUsuarioLogado();
		setProcSeletivoUnidadeEnsinoVO(new ProcSeletivoUnidadeEnsinoVO());
		setProcSeletivoDisciplinasProcSeletivoVO(new ProcSeletivoDisciplinasProcSeletivoVO());
		setProcSeletivoCursoVO(new ProcSeletivoCursoVO());
		setListaConsulta(new ArrayList(0));
		setAdicionouUnidadeEnsino(Boolean.FALSE);
		setAdicionouCurso(Boolean.FALSE);
		setExigeAgendamentoProva(true);
		setTipoConsultaComboTipoProvaGabarito(null);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoForm.xhtml");
	}

	public void inicializarResponsavelUsuarioLogado() {
		try {
			procSeletivoVO.setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ProcSeletivo</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
		ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
		setTipoConsultaComboTipoProvaGabarito(null);
		ProcSeletivoVO procSeletivoVO = getFacadeFactory().getProcSeletivoFacade()
				.consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setProcSeletivoVO(procSeletivoVO);
		if (obj.getProcSeletivoUnidadeEnsinoVOs().equals(new ArrayList<ProcSeletivoUnidadeEnsinoVO>(0))) {
			setAdicionouUnidadeEnsino(Boolean.FALSE);
		} else {
			setAdicionouUnidadeEnsino(Boolean.TRUE);
		}
		inicializarListasSelectItemTodosComboBox();
		setProcSeletivoUnidadeEnsinoVO(new ProcSeletivoUnidadeEnsinoVO());
		setProcSeletivoDisciplinasProcSeletivoVO(new ProcSeletivoDisciplinasProcSeletivoVO());
		setProcSeletivoCursoVO(new ProcSeletivoCursoVO());
		setProcSeletivoUnidadeEnsinoEixoCursoVO(new ProcSeletivoUnidadeEnsinoEixoCursoVO());
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoForm.xhtml");
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoCons.xhtml");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>ProcSeletivo</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto
	 * não é gravado, sendo re-apresentado para o usuário juntamente com uma
	 * mensagem de erro.
	 */
	public void gravar() {
		try {
			if (!getUtilizaTermoAceiteProva()) {
				getProcSeletivoVO().setTermoAceiteProva("");
			}
			if (getIsExigeAgendamentoProva().equals(false)) {
				getFacadeFactory().getProcSeletivoFacade().inicializarProcSeletivoSemAgendamento(procSeletivoVO);
			}
			if (procSeletivoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getProcSeletivoFacade().incluir(procSeletivoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getProcSeletivoFacade().alterar(procSeletivoVO, getUsuarioLogado());
				if (getEnviarComunicadoCandidatoAlteracaoDataProva() || getEnviarSmsInformativoAlteracaoDataProva()) {
					enviarNotificacaoAlteracaoDataProva();
				}
				limparDadosAlteracaoDataProva();
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			limparDadosAlteracaoDataProva();
			if (e.getMessage().contains("fk_inscricao_itemprocessoseletivodataprova")) {
				setMensagemDetalhada("msg_erro",
						UteisJSF.internacionalizar("msg_ProcSeletivo_erroExclusaoItemDataProvaEmEdicao"));
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ProcSeletivoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@SuppressWarnings("rawtypes")
	public String consultar() {
		try {
			super.consultar();
			List<ProcSeletivoVO> objs = new ArrayList<ProcSeletivoVO>(0);
			// if (getControleConsulta().getCampoConsulta().equals("codigo")) {
			// if (getControleConsulta().getValorConsulta().equals("")) {
			// getControleConsulta().setValorConsulta("0");
			// }
			// int valorInt =
			// Integer.parseInt(getControleConsulta().getValorConsulta());
			// objs =
			// getFacadeFactory().getProcSeletivoFacade().consultarPorCodigo(new
			// Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			// }
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				// objs =
				// getFacadeFactory().getProcSeletivoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(),
				// true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				// getUsuarioLogado());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(
						getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				// objs =
				// getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicio(Uteis.getDateTime(valorData,
				// 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
				// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(
						Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
						getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFim")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				// objs =
				// getFacadeFactory().getProcSeletivoFacade().consultarPorDataFim(Uteis.getDateTime(valorData,
				// 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
				// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(
						Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
						getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicioInternet")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				// objs =
				// getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioInternet(Uteis.getDateTime(valorData,
				// 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
				// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioInternetUnidadeEnsino(
						Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
						getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFimInternet")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				// objs =
				// getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimInternet(Uteis.getDateTime(valorData,
				// 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
				// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimInternetUnidadeEnsino(
						Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
						getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			// if
			// (getControleConsulta().getCampoConsulta().equals("valorInscricao"))
			// {
			// if (getControleConsulta().getValorConsulta().equals("")) {
			// getControleConsulta().setValorConsulta("0");
			// }
			// double valorDouble =
			// Double.parseDouble(getControleConsulta().getValorConsulta());
			// objs =
			// getFacadeFactory().getProcSeletivoFacade().consultarPorValorInscricao(new
			// Double(valorDouble), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
			// getUsuarioLogado());
			// }
			// if (getControleConsulta().getCampoConsulta().equals("dataProva"))
			// {
			// Date valorData =
			// Uteis.getDate(getControleConsulta().getValorConsulta());
			// objs =
			// getFacadeFactory().getProcSeletivoFacade().consultarPorDataProva(Uteis.getDateTime(valorData,
			// 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
			// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			// }
			// objs = ControleConsulta.obterSubListPaginaApresentar(objs,
			// controleConsulta);
			// definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(),
			// controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ProcSeletivoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getProcSeletivoFacade().excluir(procSeletivoVO, getUsuarioLogado());
			novo();
			// setProcSeletivoVO( new ProcSeletivoVO());
			// setProcSeletivoUnidadeEnsinoVO(new
			// ProcSeletivoUnidadeEnsinoVO());
			// setProcSeletivoDisciplinasProcSeletivoVO(new
			// ProcSeletivoDisciplinasProcSeletivoVO());
			// setProcSeletivoCursoVO(new ProcSeletivoCursoVO());
			setAdicionouCurso(Boolean.FALSE);
			setAdicionouUnidadeEnsino(Boolean.FALSE);
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoForm.xhtml");
		}
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade()
						.consultarPorCodigoCursoUnidadeEnsinoNivelEducacional(valorInt,
								getProcSeletivoUnidadeEnsinoTemp().getUnidadeEnsino().getCodigo(),
								getProcSeletivoVO().getNivelEducacional(), false, Uteis.NIVELMONTARDADOS_TODOS,
								getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade()
						.consultarPorNomeCursoUnidadeEnsinoNivelEducacional(getValorConsultaCurso(),
								getProcSeletivoUnidadeEnsinoTemp().getUnidadeEnsino().getCodigo(),
								getProcSeletivoVO().getNivelEducacional(), false, Uteis.NIVELMONTARDADOS_TODOS,
								getUsuarioLogado(), "");
			}
			setListaConsultaCurso(objs);
			if (objs.isEmpty()) {
				setMensagemID("msg_consulta_cursoNivelEducacional");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>ProcSeletivoUnidadeEnsino</code> para o objeto
	 * <code>procSeletivoVO</code> da classe <code>ProcSeletivo</code>
	 */
	public void adicionarProcSeletivoUnidadeEnsino() throws Exception {
		try {
			if (!getProcSeletivoUnidadeEnsinoVO().getUnidadeEnsino().getCodigo().equals(0)) {
				if (!getProcSeletivoVO().getCodigo().equals(0)) {
					procSeletivoUnidadeEnsinoVO.getProcSeletivo().setCodigo(getProcSeletivoVO().getCodigo());
				}
				if (getProcSeletivoUnidadeEnsinoVO().getUnidadeEnsino().getCodigo().intValue() != 0) {
					Integer campoConsulta = getProcSeletivoUnidadeEnsinoVO().getUnidadeEnsino().getCodigo();
					UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade()
							.consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS,
									getUsuarioLogado());
					getProcSeletivoUnidadeEnsinoVO().setUnidadeEnsino(unidadeEnsino);
				}
				getProcSeletivoVO().adicionarObjProcSeletivoUnidadeEnsinoVOs(getProcSeletivoUnidadeEnsinoVO());
				this.setProcSeletivoUnidadeEnsinoVO(new ProcSeletivoUnidadeEnsinoVO());
			} else {
				List<UnidadeEnsinoVO> lista = consultarUnidadeEnsinoPorNome("");
				Iterator<UnidadeEnsinoVO> i = lista.iterator();
				while (i.hasNext()) {
					UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
					getProcSeletivoUnidadeEnsinoVO().setUnidadeEnsino(obj);
					if (!getProcSeletivoVO().getCodigo().equals(0)) {
						procSeletivoUnidadeEnsinoVO.getProcSeletivo().setCodigo(getProcSeletivoVO().getCodigo());
					}
					this.getProcSeletivoVO().adicionarObjProcSeletivoUnidadeEnsinoVOs(getProcSeletivoUnidadeEnsinoVO());
					this.setProcSeletivoUnidadeEnsinoVO(new ProcSeletivoUnidadeEnsinoVO());
				}
			}
			setMensagemID("msg_dados_adicionados");
			// return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "editar";
		}
	}

	public void adicionarItemProcSeletivoDataProva() throws Exception {
		try {
			if (TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR
					.equals(getProcSeletivoVO().getTipoAvaliacaoProcessoSeletivo())
					&& Uteis.isAtributoPreenchido(getProcSeletivoVO().getItemProcSeletivoDataProvaVOs())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ProcSeletivo_adicionarDataProcessoSeletivo"));
			}
			if (getItemProcSeletivoDataProvaVO().getDataProva() == null) {
				itemProcSeletivoDataProvaVO.setDataProva(new Date());
			}
			if (getItemProcSeletivoDataProvaVO().getReplicarDatasEHorariosAteDeterminadaDataFutura()) {
				getFacadeFactory().getItemProcSeletivoDataProvaFacade()
				.adicionarObjItemProcSeletivoDataProvaVOsAteDataFutura(getProcSeletivoVO(),
								getItemProcSeletivoDataProvaVO(), false, getUsuarioLogado());
			} else {
				getProcSeletivoVO().adicionarObjItemProcSeletivoDataProvaVOs(getItemProcSeletivoDataProvaVO());
			}
			this.setItemProcSeletivoDataProvaVO(new ItemProcSeletivoDataProvaVO());
			this.getItemProcSeletivoDataProvaVO().setTipoProvaGabarito("PR");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setMotivoAlteracaoDataProva(null);
			setDataProvaAlterada(null);
		}
	}

	public void removerItemProcSeletivoDataProvaVOPorParametros() throws Exception {
		int i = this.getProcSeletivoVO().getItemProcSeletivoDataProvaVOs().size() - 1;
		while (i >= 0) {
			ItemProcSeletivoDataProvaVO itemDataProvaVO = this.getProcSeletivoVO().getItemProcSeletivoDataProvaVOs()
					.get(i);
			if ((Uteis.getDataBD2359(itemDataProvaVO.getDataProva())
					.compareTo(Uteis.getDataBD2359(this.getDataInicialRemover())) >= 0)
					&& (Uteis.getDataBD2359(itemDataProvaVO.getDataProva())
							.compareTo(Uteis.getDataBD2359(this.getDataFinalRemover())) <= 0)) {
				if (!this.getHoraEspecificaRemover().equals("")) {
					if (this.getHoraEspecificaRemover().equals(itemDataProvaVO.getHora())) {
						// se foi informada uma hora especifica para exclusao entao só
						// podemos excluir datas de provas dentro do período da hora informada pelo
						// usuario
						this.getProcSeletivoVO().getItemProcSeletivoDataProvaVOs().remove(i);
					}
				} else {
					// se não é para remover somente uma hora específica, já podemos então
					// neste ponto remover o objeto da data de prova.
					this.getProcSeletivoVO().getItemProcSeletivoDataProvaVOs().remove(i);
				}
			}
			i--;
		}
	}

	public void prepararRemoverItemProcSeletivoDataGabarito() throws Exception {
		Date maiorDataRegistradaProva = new Date();
		for (ItemProcSeletivoDataProvaVO itemDataProvaVO : this.getProcSeletivoVO().getItemProcSeletivoDataProvaVOs()) {
			if (itemDataProvaVO.getDataProva().compareTo(maiorDataRegistradaProva) > 0) {
				maiorDataRegistradaProva = itemDataProvaVO.getDataProva();
			}
		}
		setDataInicialRemover(new Date());
		setDataFinalRemover(maiorDataRegistradaProva);
		setHoraEspecificaRemover("");
	}

	public void adicionarItemProcSeletivoDataGabarito() throws Exception {
		try {
			if (TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR
					.equals(getProcSeletivoVO().getTipoAvaliacaoProcessoSeletivo())
					&& Uteis.isAtributoPreenchido(getProcSeletivoVO().getItemProcSeletivoDataProvaVOs())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ProcSeletivo_adicionarDataProcessoSeletivo"));
			}
			if (getItemProcSeletivoDataProvaVO().getDataProva() == null) {
				itemProcSeletivoDataProvaVO.setDataProva(new Date());
			}
			if (getItemProcSeletivoDataProvaVO().getReplicarDatasEHorariosAteDeterminadaDataFutura()) {
				getFacadeFactory().getItemProcSeletivoDataProvaFacade()
						.adicionarObjItemProcSeletivoDataProvaVOsAteDataFutura(getProcSeletivoVO(),
								getItemProcSeletivoDataProvaVO(), true, getUsuarioLogado());
			} else {
				getProcSeletivoVO().adicionarObjItemProcSeletivoDataGabaritoVOs(getItemProcSeletivoDataProvaVO());
			}
			this.setItemProcSeletivoDataProvaVO(new ItemProcSeletivoDataProvaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosProcessoSeletivoProvaData() {
		setItemProcSeletivoDataProvaVO(new ItemProcSeletivoDataProvaVO());
		setItemProcSeletivoDataProvaVO((ItemProcSeletivoDataProvaVO) context().getExternalContext().getRequestMap()
				.get("itemProcSeletivoDataProvaItens"));
		getItemProcSeletivoDataProvaVO().setTipoProvaGabarito("PR");
	}

	public void inicializarDadosProcessoSeletivoGabaritoData() {
		setItemProcSeletivoDataProvaVO(new ItemProcSeletivoDataProvaVO());
		setItemProcSeletivoDataProvaVO((ItemProcSeletivoDataProvaVO) context().getExternalContext().getRequestMap()
				.get("itemProcSeletivoDataProvaItens"));
	}

	public void finalizarDadosProcessoSeletivoProvaData() {
		setItemProcSeletivoDataProvaVO(new ItemProcSeletivoDataProvaVO());
	}

	public void adicionarProcessoSeletivoProvaDataVO() throws Exception {
		try {
			getFacadeFactory().getItemProcSeletivoDataProvaFacade().adicionarProcessoSeletivoProvaDataVO(
					getItemProcSeletivoDataProvaVO(), getProcessoSeletivoProvaDataVO());
			this.setProcessoSeletivoProvaDataVO(new ProcessoSeletivoProvaDataVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerProcessoSeletivoProvaDataVO() throws Exception {
		try {
			ProcessoSeletivoProvaDataVO obj = (ProcessoSeletivoProvaDataVO) context().getExternalContext()
					.getRequestMap().get("provaItens");
			getFacadeFactory().getItemProcSeletivoDataProvaFacade()
					.removerProcessoSeletivoProvaDataVO(getItemProcSeletivoDataProvaVO(), obj);
			this.setProcessoSeletivoProvaDataVO(new ProcessoSeletivoProvaDataVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarProcessoSeletivoGabaritoDataVO() throws Exception {
		try {
			getFacadeFactory().getItemProcSeletivoDataProvaFacade().adicionarProcessoSeletivoGabaritoDataVO(
					getItemProcSeletivoDataProvaVO(), getProcSeletivoGabaritoDataVO(), getUsuarioLogado());
			this.setProcSeletivoGabaritoDataVO(new ProcSeletivoGabaritoDataVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerProcessoSeletivoGabaritoDataVO() throws Exception {
		try {
			ProcSeletivoGabaritoDataVO obj = (ProcSeletivoGabaritoDataVO) context().getExternalContext().getRequestMap()
					.get("gabaritoItens");
			getFacadeFactory().getItemProcSeletivoDataProvaFacade()
					.removerProcessoSeletivoGabaritoDataVO(getItemProcSeletivoDataProvaVO(), obj);
			this.setProcSeletivoGabaritoDataVO(new ProcSeletivoGabaritoDataVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ProcSeletivoUnidadeEnsino</code> para edição pelo usuário.
	 */
	public void editarProcSeletivoUnidadeEnsino() throws Exception {
		ProcSeletivoUnidadeEnsinoVO obj = (ProcSeletivoUnidadeEnsinoVO) context().getExternalContext().getRequestMap()
				.get("procSeletivoUnidadeEnsinoItens");
		setProcSeletivoUnidadeEnsinoVO(obj);
		// return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ProcSeletivoUnidadeEnsino</code> do objeto <code>procSeletivoVO</code>
	 * da classe <code>ProcSeletivo</code>
	 */
	public void removerProcSeletivoUnidadeEnsino() throws Exception {
		ProcSeletivoUnidadeEnsinoVO obj = (ProcSeletivoUnidadeEnsinoVO) context().getExternalContext().getRequestMap()
				.get("procSeletivoUnidadeEnsinoItens");
		try {
			for (ProcSeletivoCursoVO psc : obj.getProcSeletivoCursoVOs()) {
				if (getFacadeFactory().getInscricaoFacade()
						.verificarExisteInscricaoVinculadaProcSeletivoUnidadeEnsinoCurso(
								getProcSeletivoVO().getCodigo(), psc.getUnidadeEnsinoCurso().getCodigo(), false,
								getUsuarioLogado())) {
					throw new Exception(UteisJSF.internacionalizar("msg_ProcSeletivo_inscricaoVinculadoUnidadeEnsino"));
				}
			}
			getProcSeletivoVO().excluirObjProcSeletivoUnidadeEnsinoVOs(obj.getUnidadeEnsino().getCodigo());
			setMensagemID("msg_dados_excluidos");
			if (getProcSeletivoVO().getProcSeletivoUnidadeEnsinoVOs().size() >= 1) {
				setAdicionouUnidadeEnsino(Boolean.TRUE);
			} else {
				setAdicionouUnidadeEnsino(Boolean.FALSE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerItemProcSeletivoDataProva() throws Exception {
		try {
			ItemProcSeletivoDataProvaVO obj = (ItemProcSeletivoDataProvaVO) context().getExternalContext()
					.getRequestMap().get("itemProcSeletivoDataProvaItens");
			if (getFacadeFactory().getInscricaoFacade()
					.consultarQuantidadeIncricaoVinculadaItemProcessoSeletivoDataProva(obj.getCodigo()) > 0) {
				throw new Exception(UteisJSF
						.internacionalizar("msg_ProcSeletivo_ExcluirItemProcessoSeletivoVinculadoInscricaoCandidato"));
			}
			getProcSeletivoVO().excluirObjItemProcSeletivoDataProvaVOs(obj.getDataProva(), obj.getHora());
			setMensagemID("msg_dados_excluidos");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerItemProcSeletivoDataGabarito() throws Exception {
		ItemProcSeletivoDataProvaVO obj = (ItemProcSeletivoDataProvaVO) context().getExternalContext().getRequestMap()
				.get("itemProcSeletivoDataProvaItens");
		getProcSeletivoVO().excluirObjItemProcSeletivoDataProvaVOs(obj.getDataProva(), obj.getHora());
		setMensagemID("msg_dados_excluidos");
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>ProcSeletivoDisciplinasProcSeletivo</code> para o objeto
	 * <code>procSeletivoVO</code> da classe <code>ProcSeletivo</code>
	 */
	public void adicionarProcSeletivoDisciplinasProcSeletivo() throws Exception {
		try {
			if (!getProcSeletivoVO().getCodigo().equals(0)) {
				procSeletivoDisciplinasProcSeletivoVO.setProcSeletivo(getProcSeletivoVO().getCodigo());
			}
			if (getProcSeletivoDisciplinasProcSeletivoVO().getDisciplinasProcSeletivo().getCodigo().intValue() != 0) {
				Integer campoConsulta = getProcSeletivoDisciplinasProcSeletivoVO().getDisciplinasProcSeletivo()
						.getCodigo();
				DisciplinasProcSeletivoVO disciplinasProcSeletivo = getFacadeFactory()
						.getDisciplinasProcSeletivoFacade()
						.consultarPorChavePrimaria(campoConsulta, getUsuarioLogado());
				getProcSeletivoDisciplinasProcSeletivoVO().setDisciplinasProcSeletivo(disciplinasProcSeletivo);
			}
			getProcSeletivoVO()
					.adicionarObjProcSeletivoDisciplinasProcSeletivoVOs(getProcSeletivoDisciplinasProcSeletivoVO());
			this.setProcSeletivoDisciplinasProcSeletivoVO(new ProcSeletivoDisciplinasProcSeletivoVO());
			setMensagemID("msg_dados_adicionados");
			// return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "editar";
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ProcSeletivoDisciplinasProcSeletivo</code> para edição pelo usuário.
	 */
	public void editarProcSeletivoDisciplinasProcSeletivo() throws Exception {
		ProcSeletivoDisciplinasProcSeletivoVO obj = (ProcSeletivoDisciplinasProcSeletivoVO) context()
				.getExternalContext().getRequestMap().get("procSeletivoDisciplinasProcSeletivo");
		setProcSeletivoDisciplinasProcSeletivoVO(obj);
		// return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ProcSeletivoDisciplinasProcSeletivo</code> do objeto
	 * <code>procSeletivoVO</code> da classe <code>ProcSeletivo</code>
	 */
	public void removerProcSeletivoDisciplinasProcSeletivo() throws Exception {
		ProcSeletivoDisciplinasProcSeletivoVO obj = (ProcSeletivoDisciplinasProcSeletivoVO) context()
				.getExternalContext().getRequestMap().get("procSeletivoDisciplinasProcSeletivo");
		getProcSeletivoVO()
				.excluirObjProcSeletivoDisciplinasProcSeletivoVOs(obj.getDisciplinasProcSeletivo().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public void selecionarProcSeletivoUnidadeEnsino() throws Exception {
		ProcSeletivoUnidadeEnsinoVO obj = (ProcSeletivoUnidadeEnsinoVO) context().getExternalContext().getRequestMap()
				.get("procSeletivoUnidadeEnsinoItens");
		setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
		setUtilizarNrVagasPeriodoLetivo(null);
		setCampoConsultaCurso("");
		setValorConsultaCurso("");
		setProcSeletivoUnidadeEnsinoTemp(new ProcSeletivoUnidadeEnsinoVO());
		setProcSeletivoUnidadeEnsinoTemp(obj);
		if(obj.getProcSeletivoUnidadeEnsinoEixoCursoVOs().isEmpty()) {
			setUtilizarNrVagasPeriodoLetivo(Boolean.TRUE);
		}
		//Aqui
		// return "editar";
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>ProcSeletivoCurso</code> para o objeto <code>procSeletivoVO</code> da
	 * classe <code>ProcSeletivo</code>
	 */

	public void adicionarProcSeletivoTodosCurso() throws Exception {
		try {
			if (!getListaConsultaCurso().isEmpty()) {
				Iterator<UnidadeEnsinoCursoVO> i = getListaConsultaCurso().iterator();
				while (i.hasNext()) {
					UnidadeEnsinoCursoVO uni = (UnidadeEnsinoCursoVO) i.next();
					getProcSeletivoCursoVO().setUnidadeEnsinoCurso(uni);
					getProcSeletivoCursoVO().setNumeroVaga(uni.getNrVagasPeriodoLetivo());
					if (!getProcSeletivoUnidadeEnsinoTemp().getCodigo().equals(0)) {
						procSeletivoCursoVO.getProcSeletivoUnidadeEnsino()
								.setCodigo(getProcSeletivoUnidadeEnsinoTemp().getCodigo());
					}
					getProcSeletivoUnidadeEnsinoTemp()
							.adicionarObjProcSeletivoUnidadeEnsinoCursoVOs(getProcSeletivoCursoVO());
					this.setProcSeletivoCursoVO(new ProcSeletivoCursoVO());
				}
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarProcSeletivoCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext()
					.getRequestMap().get("unidadeensinocursoItens");
			getProcSeletivoCursoVO().setUnidadeEnsinoCurso(unidadeEnsinoCurso);
			getProcSeletivoCursoVO().setNumeroVaga(unidadeEnsinoCurso.getNrVagasPeriodoLetivo());
			if (!getProcSeletivoUnidadeEnsinoTemp().getCodigo().equals(0)) {
				procSeletivoCursoVO.getProcSeletivoUnidadeEnsino()
						.setCodigo(getProcSeletivoUnidadeEnsinoTemp().getCodigo());
			}
			getProcSeletivoUnidadeEnsinoTemp().adicionarObjProcSeletivoUnidadeEnsinoCursoVOs(getProcSeletivoCursoVO());
			this.setProcSeletivoCursoVO(new ProcSeletivoCursoVO());
			setMensagemID("msg_dados_adicionados");
			// return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "editar";
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ProcSeletivoCurso</code> para edição pelo usuário.
	 */
	public void editarProcSeletivoCurso() throws Exception {
		ProcSeletivoCursoVO obj = (ProcSeletivoCursoVO) context().getExternalContext().getRequestMap()
				.get("procSeletivoCursoItens");
		setProcSeletivoCursoVO(obj);
		// return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ProcSeletivoCurso</code> do objeto <code>procSeletivoVO</code> da
	 * classe <code>ProcSeletivo</code>
	 */
	public void removerProcSeletivoCurso() throws Exception {
		try {
			ProcSeletivoCursoVO obj = (ProcSeletivoCursoVO) context().getExternalContext().getRequestMap()
					.get("procSeletivoCursoItens");
			if (getFacadeFactory().getInscricaoFacade().verificarExisteInscricaoVinculadaProcSeletivoUnidadeEnsinoCurso(
					getProcSeletivoVO().getCodigo(), obj.getUnidadeEnsinoCurso().getCodigo(), false,
					getUsuarioLogado())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ProcSeletivo_inscricaoVinculadoCurso"));
			}
			getProcSeletivoUnidadeEnsinoTemp().excluirObjProcSeletivoCursoVOs(obj);
			setMensagemID("msg_dados_excluidos");
			if (getProcSeletivoUnidadeEnsinoTemp().getProcSeletivoCursoVOs().size() >= 1) {
				setAdicionouCurso(Boolean.TRUE);
			} else {
				setAdicionouCurso(Boolean.FALSE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
		return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoCons.xhtml");
	}

	public String irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
		return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoCons.xhtml");
	}

	public String irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
		return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoCons.xhtml");
	}

	public String irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
		return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoCons.xhtml");
	}

	public void montarListaSelectItemGrupoDisciplinaProcSeletivo(String prm) throws Exception {
		try {
			List<SelectItem> objs = UtilSelectItem
					.getListaSelectItem(consultarGrupoDisciplinaProcSeletivoPorDescricao(prm), "codigo", "descricao");
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemGrupoDisciplinaProcSeletivo(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<GrupoDisciplinaProcSeletivoVO> consultarGrupoDisciplinaProcSeletivoPorDescricao(String nomePrm)
			throws Exception {
		List<GrupoDisciplinaProcSeletivoVO> listaFinal = new ArrayList<GrupoDisciplinaProcSeletivoVO>();
		List<GrupoDisciplinaProcSeletivoVO> lista = getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade()
				.consultarPorDescricao(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		Iterator<GrupoDisciplinaProcSeletivoVO> i = lista.iterator();
		while (i.hasNext()) {
			GrupoDisciplinaProcSeletivoVO grupo = (GrupoDisciplinaProcSeletivoVO) i.next();
			if (grupo.getSituacao().equals(SituacaoGrupoDisciplinaProcSeletivoEnum.ATIVA)) {
				listaFinal.add(grupo);
			} else if (grupo.getSituacao().equals(SituacaoGrupoDisciplinaProcSeletivoEnum.INATIVA)) {
				Iterator<ProcSeletivoUnidadeEnsinoVO> j = getProcSeletivoVO().getProcSeletivoUnidadeEnsinoVOs()
						.iterator();
				while (j.hasNext()) {
					ProcSeletivoUnidadeEnsinoVO pro = (ProcSeletivoUnidadeEnsinoVO) j.next();
					Iterator<ProcSeletivoCursoVO> k = pro.getProcSeletivoCursoVOs().iterator();
					while (k.hasNext()) {
						ProcSeletivoCursoVO proc = (ProcSeletivoCursoVO) k.next();
						if (proc.getGrupoDisciplinaProcSeletivo().getCodigo().intValue() != 0) {
							if (grupo.getCodigo().intValue() == proc.getGrupoDisciplinaProcSeletivo().getCodigo()
									.intValue()) {
								listaFinal.add(grupo);
							}
						}
					}
				}
			}
		}
		return listaFinal;
	}

	public void montarListaSelectItemGrupoDisciplinaProcSeletivo() {
		try {
			montarListaSelectItemGrupoDisciplinaProcSeletivo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		try {
			List<SelectItem> objs = UtilSelectItem.getListaSelectItem(consultarUnidadeEnsinoPorNome(prm), "codigo",
					"nome");
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void montarListaSelectItemUnidadeEnsinoEixoCurso(String prm) throws Exception {
		try {
			List<SelectItem> objs = UtilSelectItem.getListaSelectItem(consultarUnidadeEnsinoEixoCursoPorNome(prm), "codigo",
					"nome");
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemUnidadeEnsinoEixoCurso(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	public void montarListaSelectItemQuestionario(String prm) throws Exception {
		try {
			List<SelectItem> objs = UtilSelectItem.getListaSelectItem(consultarQuestionarioPorNome(prm), "codigo",
					"descricao", true);
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemQuestionario(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	public void montarListaSelectItemGabarito() throws Exception {
		try {
			List<SelectItem> objs = UtilSelectItem.getListaSelectItem(consultarGabaritoPorDescricao(), "codigo",
					"descricao", true);
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemGabaritoProcessoSeletivo(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	public void montarListaSelectItemProva() throws Exception {
		try {
			List<SelectItem> objs = UtilSelectItem.getListaSelectItem(consultarProvaProcessoSeletivo(), "codigo",
					"descricao", true);
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemProvaProcessoSeletivo(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela
	 * para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public void montarListaSelectItemQuestionario() {
		try {
			montarListaSelectItemQuestionario("PS");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * ( <code>List</code>) utilizada para definir os valores a serem apresentados
	 * no ComboBox correspondente
	 */
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
				super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				getUsuarioLogado());
	}
	public List<EixoCursoVO> consultarUnidadeEnsinoEixoCursoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getEixoCursoFacade().consultarPorNome(nomePrm, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
	}

	public List<QuestionarioVO> consultarQuestionarioPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getQuestionarioFacade().consultarPorEscopo(nomePrm, false,
				Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	public List<GabaritoVO> consultarGabaritoPorDescricao() throws Exception {
		return getFacadeFactory().getGabaritoFacade().consultar("descricao", "", getUsuarioLogado());
	}

	public List<ProvaProcessoSeletivoVO> consultarProvaProcessoSeletivo() throws Exception {
		return getFacadeFactory().getProvaProcessoSeletivoFacade().consultar("",
				SituacaoProvaProcessoSeletivoEnum.ATIVA, false, "ProvaProcessoSeletivo", getUsuarioLogado(), 0, 0);
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DisciplinasProcSeletivo</code>.
	 */
	public void montarListaSelectItemDisciplinasProcSeletivo(String prm) throws Exception {
		try {
			List<SelectItem> objs = UtilSelectItem.getListaSelectItem(consultarDisciplinasProcSeletivoPorNome(prm),
					"codigo", "nome", true);
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemDisciplinasProcSeletivo(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>DisciplinasProcSeletivo</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>DisciplinasProcSeletivo</code>. Esta rotina
	 * não recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDisciplinasProcSeletivo() {
		try {
			montarListaSelectItemDisciplinasProcSeletivo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * ( <code>List</code>) utilizada para definir os valores a serem apresentados
	 * no ComboBox correspondente
	 */
	@SuppressWarnings("unchecked")
	public List<DisciplinasProcSeletivoVO> consultarDisciplinasProcSeletivoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorNome(nomePrm, false,
				getUsuarioLogado());
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 * 
	 * @throws Exception
	 */
	// public void montarListaSelectItemCurso(String prm) {
	// try {
	// List resultadoConsulta =
	// consultarCursoPorUnidadeEnsino(this.getProcSeletivoUnidadeEnsinoVO().getUnidadeEnsino().getCodigo());
	// Iterator i = resultadoConsulta.iterator();
	// List objs = new ArrayList(0);
	// objs.add(new SelectItem(0, ""));
	// while (i.hasNext()) {
	// UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) i.next();
	// objs.add(new SelectItem(obj.getCurso().getCodigo(),
	// obj.getCurso().getNome()));
	// }
	// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
	// Collections.sort((List) objs, ordenador);
	// setListaSelectItemCurso(objs);
	// } catch (Exception e) {
	// //System.out.println("MENSAGEM => " + e.getMessage());;
	// }
	// }
	//
	// public void montarListaSelectItemCurso() {
	// try {
	// montarListaSelectItemCurso("");
	// } catch (Exception e) {
	// //System.out.println("MENSAGEM => " + e.getMessage());;
	// }
	// }
	//
	// public List consultarCursoPorUnidadeEnsino(Integer prm) throws Exception
	// {
	// List lista =
	// getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarUnidadeEnsinoCursos(prm);
	// return lista;
	// }
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemDisciplinasProcSeletivo();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemQuestionario();
		montarListaSelectItemGrupoDisciplinaProcSeletivo();
		// montarListaSelectItemCurso();
		montarListaSelectItemCampanha();
		montarListaSelectItemNivelEducacionalCurso();
		montarListaSelectItemEixoCurso();
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Curso</code>
	 * por meio de sua respectiva chave primária. Esta rotina é utilizada
	 * fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para
	 * apresentação.
	 */
	// public void consultarCursoPorChavePrimaria() {
	// try {
	// Integer campoConsulta =
	// procSeletivoCursoVO.getUnidadeEnsinoCurso().getCurso().getCodigo();
	// CursoVO curso =
	// getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta);
	// procSeletivoCursoVO.getUnidadeEnsinoCurso().getCurso().setNome(curso.getNome());
	// this.setCurso_Erro("");
	// setMensagemID("msg_dados_consultados");
	// } catch (Exception e) {
	// setMensagemID("msg_erro_dadosnaoencontrados");
	// procSeletivoCursoVO.getUnidadeEnsinoCurso().getCurso().setNome("");
	// procSeletivoCursoVO.getUnidadeEnsinoCurso().getCurso().setCodigo(0);
	// this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
	// }
	// }
	/**
	 * Operação responsável por processar a consulta pelo parâmetro informado pelo
	 * usuário.<code>CursoVO</code> Após a consulta ela automaticamente adciona o
	 * código e o nome da cidade na tela.
	 */

	// public List consultarCursoSuggestionbox(Object event) {
	// try {
	// String valor = event.toString();
	// List lista = getFacadeFactory().getCursoFacade().consultarPorNome(valor,
	// false);
	// return lista;
	// } catch (Exception e) {
	// return new ArrayList(0);
	// }
	// }
	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code>
	 * por meio de sua respectiva chave primária. Esta rotina é utilizada
	 * fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para
	 * apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = procSeletivoVO.getResponsavel().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			procSeletivoVO.getResponsavel().setNome(pessoa.getNome());
			this.setResponsavel_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			procSeletivoVO.getResponsavel().setNome("");
			procSeletivoVO.getResponsavel().setCodigo(0);
			this.setResponsavel_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("dataInicio", "Data Início"));
		itens.add(new SelectItem("dataFim", "Data Fim"));
		itens.add(new SelectItem("dataInicioInternet", "Data Início Internet"));
		itens.add(new SelectItem("dataFimInternet", "Data Fim Internet"));
		// itens.add(new SelectItem("valorInscricao", "Valor Inscrição"));
		// itens.add(new SelectItem("dataProva", "Data Prova"));
		return itens;
	}

	public List<SelectItem> getTipoRegimeAprovacaoCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("notaPorDisciplina", "Nota por disciplina"));
		itens.add(new SelectItem("quantidadeAcertos", "Quantidade de acertos"));
		itens.add(new SelectItem("quantidadeAcertosRedacao", "Quantidade de acertos e Redação"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> tipoConsultaComboTipoProvaGabarito;

	public List<SelectItem> getTipoConsultaComboTipoProvaGabarito() {
		if (tipoConsultaComboTipoProvaGabarito == null) {
			tipoConsultaComboTipoProvaGabarito = new ArrayList<SelectItem>(0);
			if (!getProcSeletivoVO().getTipoAvaliacaoProcessoSeletivo()
					.equals(TipoAvaliacaoProcessoSeletivoEnum.ON_LINE)) {
				tipoConsultaComboTipoProvaGabarito.add(new SelectItem("GA", "Gabarito"));
			}
			tipoConsultaComboTipoProvaGabarito.add(new SelectItem("PR", "Prova"));
		}
		return tipoConsultaComboTipoProvaGabarito;
	}

	public void setTipoConsultaComboTipoProvaGabarito(List<SelectItem> tipoConsultaComboTipoProvaGabarito) {
		this.tipoConsultaComboTipoProvaGabarito = tipoConsultaComboTipoProvaGabarito;
	}

	public List<SelectItem> getListaSelectItemTipoLayoutComprovante() {
		if (listaSelectItemTipoLayoutComprovante == null) {
			listaSelectItemTipoLayoutComprovante = new ArrayList<SelectItem>(0);
			listaSelectItemTipoLayoutComprovante.add(new SelectItem(TipoLayoutComprovanteEnum.LAYOUT_1,
					TipoLayoutComprovanteEnum.LAYOUT_1.getValorApresentar()));
			listaSelectItemTipoLayoutComprovante.add(new SelectItem(TipoLayoutComprovanteEnum.LAYOUT_2,
					TipoLayoutComprovanteEnum.LAYOUT_2.getValorApresentar()));
		}
		return listaSelectItemTipoLayoutComprovante;
	}

	public void validarDadosMediaMinimaAprovacao() {
		try {
			getFacadeFactory().getProcSeletivoFacade().validarDadosMediaMinimaAprovacao(getProcSeletivoVO());
			setMensagemID("");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void limparCamposRegimeAprovacao() {
		getProcSeletivoVO().setQuantidadeAcertosMinimosAprovacao(null);
		getProcSeletivoVO().setMediaMinimaAprovacao(null);
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		// setPaginaAtualDeTodas("0/0");
		// setListaConsulta(new ArrayList(0));
		// definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("procSeletivoCons.xhtml");
	}

	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public Boolean getAdicionouCurso() {
		return adicionouCurso;
	}

	public void setAdicionouCurso(Boolean adicionouCurso) {
		this.adicionouCurso = adicionouCurso;
	}

	public Boolean getAdicionouUnidadeEnsino() {
		return adicionouUnidadeEnsino;
	}

	public void setAdicionouUnidadeEnsino(Boolean adicionouUnidadeEnsino) {
		this.adicionouUnidadeEnsino = adicionouUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public ProcSeletivoUnidadeEnsinoVO getProcSeletivoUnidadeEnsinoVO() {
		if (procSeletivoUnidadeEnsinoVO == null) {
			procSeletivoUnidadeEnsinoVO = new ProcSeletivoUnidadeEnsinoVO();
		}
		return procSeletivoUnidadeEnsinoVO;
	}

	public void setProcSeletivoUnidadeEnsinoVO(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO) {
		this.procSeletivoUnidadeEnsinoVO = procSeletivoUnidadeEnsinoVO;
	}

	public List<SelectItem> getListaSelectItemDisciplinasProcSeletivo() {
		return (listaSelectItemDisciplinasProcSeletivo);
	}

	public void setListaSelectItemDisciplinasProcSeletivo(List<SelectItem> listaSelectItemDisciplinasProcSeletivo) {
		this.listaSelectItemDisciplinasProcSeletivo = listaSelectItemDisciplinasProcSeletivo;
	}

	public ProcSeletivoDisciplinasProcSeletivoVO getProcSeletivoDisciplinasProcSeletivoVO() {
		return procSeletivoDisciplinasProcSeletivoVO;
	}

	public void setProcSeletivoDisciplinasProcSeletivoVO(
			ProcSeletivoDisciplinasProcSeletivoVO procSeletivoDisciplinasProcSeletivoVO) {
		this.procSeletivoDisciplinasProcSeletivoVO = procSeletivoDisciplinasProcSeletivoVO;
	}

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public ProcSeletivoCursoVO getProcSeletivoCursoVO() {
		return procSeletivoCursoVO;
	}

	public void setProcSeletivoCursoVO(ProcSeletivoCursoVO procSeletivoCursoVO) {
		this.procSeletivoCursoVO = procSeletivoCursoVO;
	}

	public String getResponsavel_Erro() {
		return responsavel_Erro;
	}

	public void setResponsavel_Erro(String responsavel_Erro) {
		this.responsavel_Erro = responsavel_Erro;
	}

	public ProcSeletivoVO getProcSeletivoVO() {
		if (procSeletivoVO == null) {
			procSeletivoVO = new ProcSeletivoVO();
		}
		return procSeletivoVO;
	}

	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
	}

	public List<SelectItem> getListaSelectItemCurso() {
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public ProcSeletivoUnidadeEnsinoVO getProcSeletivoUnidadeEnsinoTemp() {
		return procSeletivoUnidadeEnsinoTemp;
	}

	public void setProcSeletivoUnidadeEnsinoTemp(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoTemp) {
		this.procSeletivoUnidadeEnsinoTemp = procSeletivoUnidadeEnsinoTemp;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>nivelEducacional</code>
	 */
	// public List getListaSelectItemNivelEducacionalProcSeletivo() throws
	// Exception {
	// return
	// UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class,
	// true);
	// }

	/*
	 * public List<SelectItem> getListaSelectItemNivelEducacionalProcSeletivo()
	 * throws Exception { List<SelectItem> itens = new ArrayList<SelectItem>(0);
	 * itens.add(new SelectItem("TO", "Todos")); itens.add(new SelectItem("BA",
	 * "Ensino Básico")); itens.add(new SelectItem("ME", "Ensino Médio"));
	 * itens.add(new SelectItem("EX", "Extensão")); itens.add(new SelectItem("SE",
	 * "Sequencial")); itens.add(new SelectItem("GT", "Graduação Tecnológica"));
	 * itens.add(new SelectItem("SU", "Ensino Superior")); itens.add(new
	 * SelectItem("PO", "Pós-graduação")); return itens; }
	 */

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox
	 * correspondente ao atributo <code>nrOpcoesCurso</code>
	 */
	@SuppressWarnings("rawtypes")
	public List<SelectItem> getListaSelectItemNrOpcoesCursoProcSeletivo() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		Hashtable procSeletivoNrOpcoesCursos = (Hashtable) Dominios.getProcSeletivoNrOpcoesCurso();
		Enumeration keys = procSeletivoNrOpcoesCursos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) procSeletivoNrOpcoesCursos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		Uteis.liberarListaMemoria(listaConsultaCurso);
		Uteis.liberarListaMemoria(listaSelectItemDisciplinasProcSeletivo);
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		Uteis.liberarListaMemoria(listaSelectItemCurso);
		procSeletivoVO = null;
		responsavel_Erro = null;
		curso_Erro = null;
		procSeletivoCursoVO = null;
		procSeletivoDisciplinasProcSeletivoVO = null;
		procSeletivoUnidadeEnsinoVO = null;
		procSeletivoUnidadeEnsinoTemp = null;		
		adicionouUnidadeEnsino = null;
		adicionouCurso = null;
		campoConsultaCurso = null;
		valorConsultaCurso = null;
	}

	public void setExigeAgendamentoProva(Boolean exigeAgendamentoProva) {
		this.exigeAgendamentoProva = exigeAgendamentoProva;
	}

	public Boolean getIsExigeAgendamentoProva() {
		if (exigeAgendamentoProva == null) {
			exigeAgendamentoProva = false;
		}
		return exigeAgendamentoProva;
	}

	public Boolean getExigeAgendamentoProva() {
		return exigeAgendamentoProva;
	}

	/**
	 * @return the itemProcSeletivoDataProvaVO
	 */
	public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProvaVO() {
		if (itemProcSeletivoDataProvaVO == null) {
			itemProcSeletivoDataProvaVO = new ItemProcSeletivoDataProvaVO();
		}
		return itemProcSeletivoDataProvaVO;
	}

	/**
	 * @param itemProcSeletivoDataProvaVO the itemProcSeletivoDataProvaVO to set
	 */
	public void setItemProcSeletivoDataProvaVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) {
		this.itemProcSeletivoDataProvaVO = itemProcSeletivoDataProvaVO;
	}

	/**
	 * @return the listaSelectItemQuestionario
	 */
	public List<SelectItem> getListaSelectItemQuestionario() {
		return listaSelectItemQuestionario;
	}

	/**
	 * @param listaSelectItemQuestionario the listaSelectItemQuestionario to set
	 */
	public void setListaSelectItemQuestionario(List<SelectItem> listaSelectItemQuestionario) {
		this.listaSelectItemQuestionario = listaSelectItemQuestionario;
	}

	public Boolean getCalculoMedia() {
		if (getProcSeletivoVO().getRegimeAprovacao().equals("notaPorDisciplina")) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarNotaMininaRedacao() {
		return getProcSeletivoVO().getRegimeAprovacao().equals("quantidadeAcertosRedacao");
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemDisciplinaIdioma() throws Exception {
		if (listaSelectItemDisciplinaIdioma == null) {
			listaSelectItemDisciplinaIdioma = new ArrayList<SelectItem>(0);
			List<DisciplinasProcSeletivoVO> disciplinasProcSeletivoVOs = getFacadeFactory()
					.getDisciplinasProcSeletivoFacade().consultarPorDisciplinasIdioma(true, false, getUsuarioLogado());
			listaSelectItemDisciplinaIdioma
					.addAll(UtilSelectItem.getListaSelectItem(disciplinasProcSeletivoVOs, "codigo", "nome", true));
		}
		return listaSelectItemDisciplinaIdioma;
	}

	public void setListaSelectItemDisciplinaIdioma(List<SelectItem> listaSelectItemDisciplinaIdioma) {
		this.listaSelectItemDisciplinaIdioma = listaSelectItemDisciplinaIdioma;
	}

	public List<SelectItem> getListaSelectItemGabaritoProcessoSeletivo() throws Exception {
		if (listaSelectItemGabaritoProcessoSeletivo == null) {
			listaSelectItemGabaritoProcessoSeletivo = new ArrayList<SelectItem>();
			List<GabaritoVO> gabaritoProcessoSeletivoVOs = getFacadeFactory().getGabaritoFacade().consultar("descricao",
					"", getUsuarioLogado());
			listaSelectItemGabaritoProcessoSeletivo.addAll(
					UtilSelectItem.getListaSelectItem(gabaritoProcessoSeletivoVOs, "codigo", "descricao", true));
		}
		return listaSelectItemGabaritoProcessoSeletivo;
	}

	public void setListaSelectItemGabaritoProcessoSeletivo(List<SelectItem> listaSelectItemGabaritoProcessoSeletivo) {
		this.listaSelectItemGabaritoProcessoSeletivo = listaSelectItemGabaritoProcessoSeletivo;
	}

	public List<SelectItem> getListaSelectItemProvaProcessoSeletivo() throws Exception {
		if (listaSelectItemProvaProcessoSeletivo == null) {
			listaSelectItemProvaProcessoSeletivo = new ArrayList<SelectItem>();
			List<ProvaProcessoSeletivoVO> provaProcessoSeletivoVOs = getFacadeFactory().getProvaProcessoSeletivoFacade()
					.consultar("", SituacaoProvaProcessoSeletivoEnum.ATIVA, false, "ProvaProcessoSeletivo",
							getUsuarioLogado(), 0, 0);
			listaSelectItemProvaProcessoSeletivo
					.addAll(UtilSelectItem.getListaSelectItem(provaProcessoSeletivoVOs, "codigo", "descricao", true));
		}
		return listaSelectItemProvaProcessoSeletivo;
	}

	public void setListaSelectItemProvaProcessoSeletivo(List<SelectItem> listaSelectItemProvaProcessoSeletivo) {
		this.listaSelectItemProvaProcessoSeletivo = listaSelectItemProvaProcessoSeletivo;
	}

	public ProcessoSeletivoProvaDataVO getProcessoSeletivoProvaDataVO() {
		if (processoSeletivoProvaDataVO == null) {
			processoSeletivoProvaDataVO = new ProcessoSeletivoProvaDataVO();
		}
		return processoSeletivoProvaDataVO;
	}

	public void setProcessoSeletivoProvaDataVO(ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO) {
		this.processoSeletivoProvaDataVO = processoSeletivoProvaDataVO;
	}

	public ProcSeletivoGabaritoDataVO getProcSeletivoGabaritoDataVO() {
		if (procSeletivoGabaritoDataVO == null) {
			procSeletivoGabaritoDataVO = new ProcSeletivoGabaritoDataVO();
		}
		return procSeletivoGabaritoDataVO;
	}

	public void setProcSeletivoGabaritoDataVO(ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO) {
		this.procSeletivoGabaritoDataVO = procSeletivoGabaritoDataVO;
	}

	public boolean getApresentarItemProcSeletivoProva() {
		return getItemProcSeletivoDataProvaVO().getTipoProvaGabarito().equals("PR");
	}

	public boolean getReadOnlyTipoProvaGabarito() {
		return !getProcSeletivoVO().getItemProcSeletivoDataProvaVOs().isEmpty();
	}
	
	public void validarDadosProcSeletivoCurso() throws Exception {
		try {
			getProcSeletivoUnidadeEnsinoTemp();
			for (ProcSeletivoUnidadeEnsinoVO unidade : getProcSeletivoVO().getProcSeletivoUnidadeEnsinoVOs()) {
				unidade.setProcSeletivo(getProcSeletivoVO());
				for (ProcSeletivoCursoVO obj : unidade.getProcSeletivoCursoVOs()) {
					obj.setProcSeletivoUnidadeEnsino(unidade);
					ProcSeletivoCursoVO.validarDados(obj);
				}
			}
			gravar();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosProcSeletivoEixoCurso() throws Exception {
		try {
			getProcSeletivoUnidadeEnsinoTemp();
			for (ProcSeletivoUnidadeEnsinoVO unidade : getProcSeletivoVO().getProcSeletivoUnidadeEnsinoVOs()) {
				unidade.setProcSeletivo(getProcSeletivoVO());
				for (ProcSeletivoUnidadeEnsinoEixoCursoVO obj : unidade.getProcSeletivoUnidadeEnsinoEixoCursoVOs()) {
					obj.setProcSeletivoUnidadeEnsino(unidade);
				}
			}
			gravar();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void editarItemProcSeletivoDataProva() throws Exception {
		setItemProcSeletivoDataProvaVO((ItemProcSeletivoDataProvaVO) context().getExternalContext().getRequestMap()
				.get("itemProcSeletivoDataProvaItens"));
		getProcSeletivoVO().getItemProcSeletivoDataProvaVOs().remove(getItemProcSeletivoDataProvaVO());
		setDataProvaAlterada(getItemProcSeletivoDataProvaVO().getDataProva());
		setMensagemID("msg_dados_editar");
	}

	public void editarItemProcSeletivoDataGabarito() throws Exception {
		setItemProcSeletivoDataProvaVO((ItemProcSeletivoDataProvaVO) context().getExternalContext().getRequestMap()
				.get("itemProcSeletivoDataProvaItens"));
		getProcSeletivoVO().getItemProcSeletivoDataProvaVOs().remove(getItemProcSeletivoDataProvaVO());
		setMensagemID("msg_dados_editar");
	}

	public boolean getIsPermitirEditarDataProva() {
		return getItemProcSeletivoDataProvaVO().isNovoObj()
				|| (UteisData.getDataComMinutos(getItemProcSeletivoDataProvaVO().getDataProva())
						.before(UteisData.getDataComMinutos(new Date()))
						&& verificarUsuarioPossuiPermissaoAlterarDataProva("PermitirAlterarDataProvaProcessoSeletivo"));
	}

	/**
	 * @return the dataInicialRemover
	 */
	public Date getDataInicialRemover() {
		if (dataInicialRemover == null) {
			dataInicialRemover = new Date();
		}
		return dataInicialRemover;
	}

	/**
	 * @param dataInicialRemover the dataInicialRemover to set
	 */
	public void setDataInicialRemover(Date dataInicialRemover) {
		this.dataInicialRemover = dataInicialRemover;
	}

	/**
	 * @return the dataFinalRemover
	 */
	public Date getDataFinalRemover() {
		if (dataFinalRemover == null) {
			dataFinalRemover = new Date();
		}
		return dataFinalRemover;
	}

	/**
	 * @param dataFinalRemover the dataFinalRemover to set
	 */
	public void setDataFinalRemover(Date dataFinalRemover) {
		this.dataFinalRemover = dataFinalRemover;
	}

	/**
	 * @return the horaEspecificaRemover
	 */
	public String getHoraEspecificaRemover() {
		if (horaEspecificaRemover == null) {
			horaEspecificaRemover = "";
		}
		return horaEspecificaRemover;
	}

	/**
	 * @param horaEspecificaRemover the horaEspecificaRemover to set
	 */
	public void setHoraEspecificaRemover(String horaEspecificaRemover) {
		this.horaEspecificaRemover = horaEspecificaRemover;
	}

	public List<SelectItem> getTipoConsultaComboSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1º"));
		itens.add(new SelectItem("2", "2º"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemGrupoDisciplinaProcSeletivo() {
		if (listaSelectItemGrupoDisciplinaProcSeletivo == null) {
			listaSelectItemGrupoDisciplinaProcSeletivo = new ArrayList<SelectItem>();
		}
		return listaSelectItemGrupoDisciplinaProcSeletivo;
	}

	public void setListaSelectItemGrupoDisciplinaProcSeletivo(
			List<SelectItem> listaSelectItemGrupoDisciplinaProcSeletivo) {
		this.listaSelectItemGrupoDisciplinaProcSeletivo = listaSelectItemGrupoDisciplinaProcSeletivo;
	}

	public List<SelectItem> getListaSelectItemCampanha() {
		if (listaSelectItemCampanha == null) {
			listaSelectItemCampanha = new ArrayList<SelectItem>();
		}
		return listaSelectItemCampanha;
	}

	public void setListaSelectItemCampanha(List<SelectItem> listaSelectItemCampanha) {
		this.listaSelectItemCampanha = listaSelectItemCampanha;
	}

	public void montarListaSelectItemCampanha() {
		try {
			List<CampanhaVO> listaCampanhas = getFacadeFactory().getCampanhaFacade().consultarPorDescricao("",
					TipoCampanhaEnum.CONTACTAR_INSCRITOS_PROCSELETIVO.toString(), "AT",
					super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					getUsuarioLogado());
			List<SelectItem> objs = UtilSelectItem.getListaSelectItem(listaCampanhas, "codigo", "descricao");
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemCampanha(objs);
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getListaSelectItemTipoAvaliacaoProcessoSeletivo() {
		return TipoAvaliacaoProcessoSeletivoEnum.getTipoAvaliacaoProcessoSeletivo();
	}

	public Boolean validarPermissaoAlterarDataProva() {
		return true;
	}

	public Date getDataProvaAlterada() {
		if (dataProvaAlterada == null) {
			return new Date();
		}
		return dataProvaAlterada;
	}

	public void setDataProvaAlterada(Date dataProvaAlterada) {
		this.dataProvaAlterada = dataProvaAlterada;
	}

	public Boolean getEnviarComunicadoCandidatoAlteracaoDataProva() {
		if (enviarComunicadoCandidatoAlteracaoDataProva == null) {
			enviarComunicadoCandidatoAlteracaoDataProva = Boolean.FALSE;
		}
		return enviarComunicadoCandidatoAlteracaoDataProva;
	}

	public void setEnviarComunicadoCandidatoAlteracaoDataProva(Boolean enviarComunicadoCandidatoAlteracaoDataProva) {
		this.enviarComunicadoCandidatoAlteracaoDataProva = enviarComunicadoCandidatoAlteracaoDataProva;
	}

	public Boolean getPermitirFecharModalAlteracaoDataProva() {
		if (permitirFecharModalAlteracaoDataProva == null) {
			permitirFecharModalAlteracaoDataProva = Boolean.FALSE;
		}
		return permitirFecharModalAlteracaoDataProva;
	}

	public void setPermitirFecharModalAlteracaoDataProva(Boolean permitirFecharModalAlteracaoDataProva) {
		this.permitirFecharModalAlteracaoDataProva = permitirFecharModalAlteracaoDataProva;
	}

	public Boolean getEnviarSmsInformativoAlteracaoDataProva() {
		if (enviarSmsInformativoAlteracaoDataProva == null) {
			enviarSmsInformativoAlteracaoDataProva = Boolean.FALSE;
		}
		return enviarSmsInformativoAlteracaoDataProva;
	}

	public void setEnviarSmsInformativoAlteracaoDataProva(Boolean enviarSmsInformativoAlteracaoDataProva) {
		this.enviarSmsInformativoAlteracaoDataProva = enviarSmsInformativoAlteracaoDataProva;
	}

	public String getCorpoEmail() {
		if (corpoEmail == null) {
			corpoEmail = "";
		}
		return corpoEmail;
	}

	public void setCorpoEmail(String corpoEmail) {
		this.corpoEmail = corpoEmail;
	}

	public String getCorpoSms() {
		if (corpoSms == null) {
			corpoSms = "";
		}
		return corpoSms;
	}

	public void setCorpoSms(String corpoSms) {
		this.corpoSms = corpoSms;
	}

	public String getMotivoAlteracaoDataProva() {
		if (motivoAlteracaoDataProva == null) {
			motivoAlteracaoDataProva = "";
		}
		return motivoAlteracaoDataProva;
	}

	public void setMotivoAlteracaoDataProva(String motivoAlteracaoDataProva) {
		this.motivoAlteracaoDataProva = motivoAlteracaoDataProva;
	}

	public String getAssuntoEmail() {
		if (assuntoEmail == null) {
			assuntoEmail = "Notificação Alteração Data Prova Processo Seletivo";
		}
		return assuntoEmail;
	}

	public void setAssuntoEmail(String assuntoEmail) {
		this.assuntoEmail = assuntoEmail;
	}

	public Integer getNumeroCandidatoNotificado() {
		if (numeroCandidatoNotificado == null) {
			numeroCandidatoNotificado = 0;
		}
		return numeroCandidatoNotificado;
	}

	public void setNumeroCandidatoNotificado(Integer numeroCandidatoNotificado) {
		this.numeroCandidatoNotificado = numeroCandidatoNotificado;
	}

	public List<ComunicacaoInternaVO> getListaComunicacaoInternaVO() {
		if (listaComunicacaoInternaVO == null) {
			listaComunicacaoInternaVO = new ArrayList<ComunicacaoInternaVO>(0);
		}
		return listaComunicacaoInternaVO;
	}

	public void setListaComunicacaoInternaVO(List<ComunicacaoInternaVO> listaComunicacaoInternaVO) {
		this.listaComunicacaoInternaVO = listaComunicacaoInternaVO;
	}

	public Boolean getIsApresentarIconeAlteracaoDataProva() {
		return (verificarUsuarioPossuiPermissaoAlterarDataProva("PermitirAlterarDataProvaProcessoSeletivo")
				&& !getItemProcSeletivoDataProvaVO().isNovoObj()
				&& UteisData.getDataComMinutos(getItemProcSeletivoDataProvaVO().getDataProva())
						.after(UteisData.getDataComMinutos(new Date())));
	}

	public void realizarAlteracaoDataProvaProcessoSeletivoFuturo() throws Exception {
		Date dataAnterior = getItemProcSeletivoDataProvaVO().getDataProva();
		try {
			getItemProcSeletivoDataProvaVO().setDataProva(getDataProvaAlterada());
			getItemProcSeletivoDataProvaVO().setMotivoAlteracaoDataProva(getMotivoAlteracaoDataProva());
			getFacadeFactory().getItemProcSeletivoDataProvaFacade().validarDados(getItemProcSeletivoDataProvaVO());
			setPermitirFecharModalAlteracaoDataProva(Boolean.TRUE);
			adicionarNotificacaoCandidato();
			if (getEnviarComunicadoCandidatoAlteracaoDataProva() || getEnviarSmsInformativoAlteracaoDataProva()) {
				setMensagemID("msg_notificacaoEnviadaAlteracaoDataProva");
			} else {
				setMensagemID("msg_alteracaoDataProvaRealizadaComSucesso");
			}
		} catch (Exception e) {
			getItemProcSeletivoDataProvaVO().setDataProva(dataAnterior);
			setPermitirFecharModalAlteracaoDataProva(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getIsFecharModalAlterarDataProvaProcessoSeletivo() {
		if (getPermitirFecharModalAlteracaoDataProva()) {
			return "RichFaces.$('panelAlterarDataProvaProcessoSeletivo').hide();";
		} else {
			return "";
		}
	}

	public void montarCorpoMensagemNotificacaoEmail() {
		try {
			if (getEnviarComunicadoCandidatoAlteracaoDataProva()) {
				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory()
						.getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(
								TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ADIAMENTO_PROVA_PROCESSO_SELETIVO,
								false, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(mensagemTemplate)
						&& !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
					setAssuntoEmail(mensagemTemplate.getAssunto());
					setCorpoEmail(mensagemTemplate.getMensagem());
				}
				setCorpoEmail(getCorpoEmail().replaceAll("/imagens/email/cima_sei.jpg",
						"../resources//imagens/email/cima_sei.jpg"));
				setCorpoEmail(getCorpoEmail().replaceAll("/imagens/email/baixo_sei.jpg",
						"../resources//imagens/email/baixo_sei.jpg"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarCorpoMensagemNotificacaoSms() {
		try {
			if (getEnviarSmsInformativoAlteracaoDataProva()) {
				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory()
						.getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(
								TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ADIAMENTO_PROVA_PROCESSO_SELETIVO,
								false, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(mensagemTemplate)
						&& !mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					setCorpoSms(mensagemTemplate.getMensagemSMS());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarContagemCandidatoNotificado() throws Exception {
		setNumeroCandidatoNotificado(getFacadeFactory().getItemProcSeletivoDataProvaFacade()
				.consultarNumeroCandidatoNotificado(getProcSeletivoVO(), getItemProcSeletivoDataProvaVO()));
	}

	public Boolean getIsApresentarNumeroCandidatoNotificado() {
		return getEnviarComunicadoCandidatoAlteracaoDataProva() || getEnviarSmsInformativoAlteracaoDataProva();
	}

	public void adicionarNotificacaoCandidato() {
		try {
			validarDadosEnvioNotificacao();
			if (getEnviarComunicadoCandidatoAlteracaoDataProva() || getEnviarSmsInformativoAlteracaoDataProva()) {
				List<InscricaoVO> listaObjetos = getFacadeFactory().getInscricaoFacade()
						.consultarCandidatoNotificacaoAlteracaoDataProva(getProcSeletivoVO(),
								getItemProcSeletivoDataProvaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
								getUsuarioLogado());
				for (InscricaoVO inscricaoVO : listaObjetos) {
					inscricaoVO.getItemProcessoSeletivoDataProva().setDataProva(getDataProvaAlterada());
					inscricaoVO.getItemProcessoSeletivoDataProva()
							.setMotivoAlteracaoDataProva(getMotivoAlteracaoDataProva());
					if (Uteis.isAtributoPreenchido(inscricaoVO.getCandidato().getEmail())
							|| Uteis.isAtributoPreenchido(inscricaoVO.getCandidato().getEmail2())) {
						getListaComunicacaoInternaVO().add(getFacadeFactory().getComunicacaoInternaFacade()
								.realizarMontagemNotificacaoAlteracaoDataProva(inscricaoVO,
										getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), getCorpoSms(),
										getCorpoEmail(), getEnviarSmsInformativoAlteracaoDataProva(),
										getAssuntoEmail()));
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void enviarNotificacaoAlteracaoDataProva() throws Exception {
		try {
			Iterator<ComunicacaoInternaVO> i = getListaComunicacaoInternaVO().iterator();
			while (i.hasNext()) {
				ComunicacaoInternaVO obj = i.next();
				getFacadeFactory().getComunicacaoInternaFacade().executarNotificacaoAlteracaoDataProvaCandidado(obj,
						getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosEnvioNotificacao() throws Exception {
		if (getCorpoSms().length() > 150 && getEnviarSmsInformativoAlteracaoDataProva()) {
			throw new Exception(getMensagemInternalizacao("msg_LimiteCampoTextoSms"));
		}
		if (getCorpoSms().isEmpty() && getEnviarSmsInformativoAlteracaoDataProva()) {
			throw new Exception(getMensagemInternalizacao("msg_EnviarSmsTextoVazio"));
		}
	}

	public void limparDadosAlteracaoDataProva() {
		setEnviarComunicadoCandidatoAlteracaoDataProva(Boolean.FALSE);
		setEnviarSmsInformativoAlteracaoDataProva(Boolean.FALSE);
		setAssuntoEmail(null);
		setCorpoEmail(null);
		setCorpoSms(null);
		setDataProvaAlterada(null);
	}

	public Boolean verificarUsuarioPossuiPermissaoAlterarDataProva(String identificadorAcaoPermissao) {
		Boolean liberar = Boolean.FALSE;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao,
					getUsuarioLogado());
			liberar = Boolean.TRUE;
		} catch (Exception e) {
			liberar = Boolean.FALSE;
		}
		return liberar;
	}

	public void montarListaSelectItemNivelEducacionalCurso() throws Exception {
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		for (SelectItem item : opcoes) {
			if (!item.getValue().equals("SE")) {
				getListaNivelEducacional().add(item);
			}
		}
	}

	public void setListaNivelEducacional(List listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public List getListaNivelEducacional() {
		if (listaNivelEducacional == null) {
			listaNivelEducacional = new ArrayList(0);
		}
		return listaNivelEducacional;
	}

	/**
	 * @return the listaSelectItemTipoProvaGabarito
	 */
	public List<SelectItem> getListaSelectItemTipoProvaGabarito() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		if (listaSelectItemTipoProvaGabarito == null) {
			itens.add(new SelectItem("PR", "Prova"));
			itens.add(new SelectItem("GA", "Gabarito"));
		}
		return itens;
	}

	/**
	 * @param listaSelectItemTipoProvaGabarito the listaSelectItemTipoProvaGabarito
	 *                                         to set
	 */
	public void setListaSelectItemTipoProvaGabarito(List<SelectItem> listaSelectItemTipoProvaGabarito) {
		this.listaSelectItemTipoProvaGabarito = listaSelectItemTipoProvaGabarito;
	}

	public String getModalApresentarTipoProvaGabarito() {
		if (Uteis.isAtributoPreenchido(getItemProcSeletivoDataProvaVO().getTipoProvaGabarito())
				&& getItemProcSeletivoDataProvaVO().getTipoProvaGabarito().equals("PR")) {
			return "RichFaces.$('panelDataGabarito').hide(); RichFaces.$('panelDataProva').show();";
		} else if (Uteis.isAtributoPreenchido(getItemProcSeletivoDataProvaVO().getTipoProvaGabarito())
				&& getItemProcSeletivoDataProvaVO().getTipoProvaGabarito().equals("GA")) {
			return "RichFaces.$('panelDataProva').hide(); RichFaces.$('panelDataGabarito').show();";
		}

		return "";
	}

	public void limparDadosOrientacao() {
		if (!getProcSeletivoVO().getUploadComprovanteEnem()) {
			getProcSeletivoVO().setOrientacaoUploadEnem("");
		}
		if (!getProcSeletivoVO().getUploadComprovantePortadorDiploma()) {
			getProcSeletivoVO().setOrientacaoUploadPortadorDiploma("");
		}
		if (!getProcSeletivoVO().getObrigarUploadComprovanteTransferencia()) {
			getProcSeletivoVO().setOrientacaoTransferencia("");
		}
	}

	public Boolean getUtilizaTermoAceiteProva() {
		if (utilizaTermoAceiteProva == null) {
			utilizaTermoAceiteProva = Uteis.isAtributoPreenchido(getProcSeletivoVO().getTermoAceiteProva());
		}
		return utilizaTermoAceiteProva;
	}

	public void setUtilizaTermoAceiteProva(Boolean utilizaTermoAceiteProva) {
		this.utilizaTermoAceiteProva = utilizaTermoAceiteProva;
	}

	public void selecionarTipoProcSeletivo() {
		setTipoConsultaComboTipoProvaGabarito(null);
		if (getProcSeletivoVO().getTipoAvaliacaoProcessoSeletivo().equals(TipoAvaliacaoProcessoSeletivoEnum.ON_LINE)) {
			getProcSeletivoVO().setRealizarProvaOnline(true);
			getProcSeletivoVO().setTipoProcessoSeletivo(true);
			setUtilizaTermoAceiteProva(true);
			getProcSeletivoVO().setUtilizarAutenticacaoEmail(true);
			getProcSeletivoVO().setUtilizarAutenticacaoSMS(true);
		} else if (getProcSeletivoVO().getTipoAvaliacaoProcessoSeletivo()
				.equals(TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR)) {
			getProcSeletivoVO().setRealizarProvaOnline(false);
			getProcSeletivoVO().setTipoProcessoSeletivo(true);
			getProcSeletivoVO().setTipoEnem(false);
			getProcSeletivoVO().setTipoPortadorDiploma(false);
			getProcSeletivoVO().setTipoTransferencia(false);
			setUtilizaTermoAceiteProva(false);
			getProcSeletivoVO().setUtilizarAutenticacaoEmail(false);
			getProcSeletivoVO().setUtilizarAutenticacaoSMS(false);
		} else {
			getProcSeletivoVO().setRealizarProvaOnline(false);
			setUtilizaTermoAceiteProva(false);
			getProcSeletivoVO().setUtilizarAutenticacaoEmail(false);
			getProcSeletivoVO().setUtilizarAutenticacaoSMS(false);
		}
	}

	private final static String CSS_COLUNA_6 = "col-md-6";
	private final static String CSS_COLUNA_4 = "col-md-4";

	public String getCssFlagComprovante() {
		Integer qtdeColuna = (getProcSeletivoVO().getTipoEnem() ? 1 : 0)
				+ (getProcSeletivoVO().getTipoPortadorDiploma() ? 1 : 0)
				+ (getProcSeletivoVO().getTipoTransferencia() ? 1 : 0);
		if (qtdeColuna == 1) {
			return CSS_COLUNA_6;
		}
		if (qtdeColuna == 2) {
			return CSS_COLUNA_4;
		}
		if (qtdeColuna == 3) {
			return CSS_COLUNA_4;
		} else {
			return CSS_COLUNA_6;
		}

	}

	public String getCssFlagBoolean() {
		if (getProcSeletivoVO().getTipoEnem().equals(true) && getProcSeletivoVO().getTipoPortadorDiploma().equals(true)
				&& getProcSeletivoVO().getTipoTransferencia().equals(true)) {
			return CSS_COLUNA_4;
		}
		if (getProcSeletivoVO().getTipoEnem().equals(true)
				&& getProcSeletivoVO().getTipoPortadorDiploma().equals(true)) {
			return CSS_COLUNA_4;
		}
		if (getProcSeletivoVO().getTipoEnem().equals(true) && getProcSeletivoVO().getTipoTransferencia().equals(true)) {
			return CSS_COLUNA_4;
		}
		if (getProcSeletivoVO().getTipoPortadorDiploma().equals(true)
				&& getProcSeletivoVO().getTipoTransferencia().equals(true)) {
			return CSS_COLUNA_4;
		} 
		if ((getProcSeletivoVO().getTipoEnem().equals(true) || getProcSeletivoVO().getTipoPortadorDiploma().equals(true))
				|| getProcSeletivoVO().getTipoTransferencia().equals(true)) {
			return CSS_COLUNA_6;
		}
		else {
			return CSS_COLUNA_4;
		}
	}

	public ProcSeletivoUnidadeEnsinoEixoCursoVO getProcSeletivoUnidadeEnsinoEixoCursoVO() {
		if(procSeletivoUnidadeEnsinoEixoCursoVO == null) {
			procSeletivoUnidadeEnsinoEixoCursoVO = new ProcSeletivoUnidadeEnsinoEixoCursoVO();
		}
		return procSeletivoUnidadeEnsinoEixoCursoVO;
	}

	public void setProcSeletivoUnidadeEnsinoEixoCursoVO(
			ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO) {
		this.procSeletivoUnidadeEnsinoEixoCursoVO = procSeletivoUnidadeEnsinoEixoCursoVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsinoEixoCurso() {
		return listaSelectItemUnidadeEnsinoEixoCurso;
	}

	public void setListaSelectItemUnidadeEnsinoEixoCurso(List<SelectItem> listaSelectItemUnidadeEnsinoEixoCurso) {
		this.listaSelectItemUnidadeEnsinoEixoCurso = listaSelectItemUnidadeEnsinoEixoCurso;
	}
	
	public List<SelectItem> getListaSelectItemEixoCurso() {
		return listaSelectItemEixoCurso;
	}

	public void setListaSelectItemEixoCurso(List<SelectItem> listaSelectItemEixoCurso) {
		this.listaSelectItemEixoCurso = listaSelectItemEixoCurso;
	}
	
	private void montarListaSelectItemEixoCurso() {
		try {
			setListaSelectItemEixoCurso(new ArrayList<>(0));
			getListaSelectItemEixoCurso().add(new SelectItem(0, ""));
			for (EixoCursoVO eixoCursoVO : getFacadeFactory().getEixoCursoFacade().consultarTodos(Uteis.NIVELMONTARDADOS_COMBOBOX)) {
				getListaSelectItemEixoCurso().add(new SelectItem(eixoCursoVO.getCodigo(), eixoCursoVO.getNome()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public EixoCursoVO getEixoCursoVO() {
		if(eixoCursoVO == null) {
			eixoCursoVO = new EixoCursoVO();
		}
		return eixoCursoVO;
	}

	public void setEixoCursoVO(EixoCursoVO eixoCursoVO) {
		this.eixoCursoVO = eixoCursoVO;
	}

	public void adicionarProcSeletivoUnidadeEnsinoEixoCursoVO() throws Exception {
		try {
			getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().adicionarProcSeletivoUnidadeEnsinoEixoCursoVO(getProcSeletivoUnidadeEnsinoTemp(), getProcSeletivoUnidadeEnsinoEixoCursoVO());
			setProcSeletivoUnidadeEnsinoEixoCursoVO(new ProcSeletivoUnidadeEnsinoEixoCursoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
			
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerProcSeletivoUnidadeEnsinoEixoCursoVO() throws Exception {
		try {
			getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().removerProcSeletivoUnidadeEnsinoEixoCursoVO(getProcSeletivoUnidadeEnsinoTemp(), getProcSeletivoUnidadeEnsinoEixoCursoVO());
			setProcSeletivoUnidadeEnsinoEixoCursoVO(new ProcSeletivoUnidadeEnsinoEixoCursoVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void inicializarDadosProcSeletivoUnidadeEnsinoExcel(FileUploadEvent uploadEvent)  {
		try {
			limparMensagem();
			getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().realizarProcessamentoPlanilha(uploadEvent, procSeletivoVO, getUsuario());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	
	
	public void adicionarPeriodoChamadaProcSeletivoVO() {
		try {			
			getFacadeFactory().getPeriodoChamadaProcSeletivoFacade().adicionarPeriodoChamadaProcSeletivoVO(getPeriodoChamadaProcSeletivoVO(), getProcSeletivoVO());
			Ordenacao.ordenarLista( getProcSeletivoVO().getPeriodoChamadaProcSeletivoVOs(), "nrChamada");
			this.setPeriodoChamadaProcSeletivoVO(new PeriodoChamadaProcSeletivoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}
	
	
	public void editarPeriodoChamadaProcSeletivo() {
		setPeriodoChamadaProcSeletivoVO((PeriodoChamadaProcSeletivoVO) context().getExternalContext().getRequestMap().get("periodoChamadaProcSelItens"));
		getProcSeletivoVO().getPeriodoChamadaProcSeletivoVOs().remove(getPeriodoChamadaProcSeletivoVO());		
		Ordenacao.ordenarLista( getProcSeletivoVO().getPeriodoChamadaProcSeletivoVOs(), "nrChamada");
		setMensagemID("msg_dados_editar");
		
	}
	
	public void removerPeriodoChamadaProcSeletivo() {
		PeriodoChamadaProcSeletivoVO periodoChamadaProcSel = ((PeriodoChamadaProcSeletivoVO) context().getExternalContext().getRequestMap().get("periodoChamadaProcSelItens"));
		getProcSeletivoVO().getPeriodoChamadaProcSeletivoVOs().remove(periodoChamadaProcSel);	
		Ordenacao.ordenarLista( getProcSeletivoVO().getPeriodoChamadaProcSeletivoVOs(), "nrChamada");
		setMensagemID("msg_dados_excluidos");
	}	
	
	public List<SelectItem> getTipoConsultaComboNrChamadaProcSeletivo() {
		return getFacadeFactory().getPeriodoChamadaProcSeletivoFacade().getConsultaComboNrChamadaProcSeletivo();
	
	}

	public PeriodoChamadaProcSeletivoVO getPeriodoChamadaProcSeletivoVO() {
		if(periodoChamadaProcSeletivoVO == null ) {
			periodoChamadaProcSeletivoVO = new PeriodoChamadaProcSeletivoVO();
		}
		return periodoChamadaProcSeletivoVO;
	}

	public void setPeriodoChamadaProcSeletivoVO(PeriodoChamadaProcSeletivoVO periodoChamadaProcSeletivoVO) {
		this.periodoChamadaProcSeletivoVO = periodoChamadaProcSeletivoVO;
	}
	public Boolean getUtilizarNrVagasPeriodoLetivo() {
		if(utilizarNrVagasPeriodoLetivo == null ) {
			utilizarNrVagasPeriodoLetivo = Boolean.FALSE;
		}
		return utilizarNrVagasPeriodoLetivo;
	}

	public void setUtilizarNrVagasPeriodoLetivo(Boolean utilizarNrVagasPeriodoLetivo) {
		this.utilizarNrVagasPeriodoLetivo = utilizarNrVagasPeriodoLetivo;
	}
	
	public void removerProcSeletivoUnidadeEnsinoTodas() throws Exception {
		try {
			getFacadeFactory().getProcSeletivoFacade().removerProcSeletivoUnidadeEnsinoTodas(getProcSeletivoVO(), getUsuarioLogado());
			if (getProcSeletivoVO().getProcSeletivoUnidadeEnsinoVOs().size() >= 1) {
				setAdicionouUnidadeEnsino(Boolean.TRUE);
			} else {
				setAdicionouUnidadeEnsino(Boolean.FALSE);
			}
			setMensagemID("msg_dados_excluidos");
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private List<CursoTurnoVO> cursoTurnoVOs;
		
	public List<CursoTurnoVO> getCursoTurnoVOs() {
		if(cursoTurnoVOs == null) {
			cursoTurnoVOs =  new ArrayList<CursoTurnoVO>(0);
		}
		return cursoTurnoVOs;
	}

	public void setCursoTurnoVOs(List<CursoTurnoVO> cursoTurnoVOs) {
		this.cursoTurnoVOs = cursoTurnoVOs;
	}

	public void consultarCursoTurno() {
		try {
			limparMensagem();
			if(getCursoTurnoVOs().isEmpty() || !getCursoTurnoVOs().get(0).getCursoVO().getNivelEducacional().equals(getProcSeletivoVO().getNivelEducacional())) {
				setCursoTurnoVOs(getFacadeFactory().getCursoTurnoFacade().consultarCursoTurnoProcessoMatricula("nome", "%%", getProcSeletivoVO().getNivelEducacional(), null, null));
			}
		} catch (Exception e) {
			getCursoTurnoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarCursoTurno() {
		try {
			CursoTurnoVO cursoTurnoVO = (CursoTurnoVO) getRequestMap().get("cursoTurnoItens");
			getFacadeFactory().getProcSeletivoFacade().adicionarCursoTurnoGeral(procSeletivoVO, cursoTurnoVO, getUsuario());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

}
