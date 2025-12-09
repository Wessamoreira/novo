package controle.arquitetura;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.*;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import jobs.JobProvaProcessoSeletivo;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaIntegralizacaoCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.OfertaDisciplinaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoTurmaEnum;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.administrativo.FraseInspiracaoVO;
import negocio.comuns.administrativo.PreferenciaSistemaUsuarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.LinksUteisVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.ScriptExecutadoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.estagio.DashboardEstagioVO;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ModeloBoletoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.HorarioTurmaInterfaceFacade;
import relatorio.negocio.jdbc.academico.AtaResultadosFinaisRel;
import relatorio.negocio.jdbc.academico.HistoricoAlunoRel;

@Controller("AplicacaoControle")
@Scope("singleton")
@Lazy
public class AplicacaoControle {

	private List<ConfiguracaoGeralSistemaVO> listaConfiguracaoGeralSistemaVOAtiva =  new ArrayList<ConfiguracaoGeralSistemaVO>();	
	private Map<Integer, Integer> mapConfiguracaoGeralSistemaVOPorUnidadeEnsino = new HashMap<Integer, Integer>();	
	private List<ConfiguracaoFinanceiroVO> listaConfiguracaoFinanceiroVOAtiva =  new ArrayList<ConfiguracaoFinanceiroVO>();
	public FraseInspiracaoVO fraseInspiracaoVO;
	private List<MapaControleAtividadesUsuarioVO> mapaControleUsuarios;
	private Boolean mapaControleAtividadesUsuariosAtivo;	private MapaControleAtividadesUsuarioVO mapaControleAtividadesUsuarioVO;
	private String mensagem;
	private List<ControleAtividadeUsuarioVO> mapaAtividadesUsuarioPorEntidade;
	private List<ControleAtividadeUsuarioVO> mapaAtividadesUsuarioPorUsuario;
	private Hashtable listaDataComemorativaApresentar;
	private String filtroMapaControleAtividade;
	private Date autenticacaoRealizada;
	private Boolean geracaoParcelaExecutando;
	private List<Integer> listaUnidadeEnsinoGeracaoParcela;
	private List<Integer> confNotaFiscalVOs;
	private Map<Integer, Thread> listaTurmaRealizandoRenovacao;	
	private static Integer integracaoFinanceiraProcessando;	
	private static Date dataExecucaoJobEmail;
	private static Date dataExecucaoJobGED;
	private Cliente cliente;
	private Map<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademica = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);	
	@Autowired
	private FacadeFactory facadeFactory;
	public static List<Integer> turmaProgramacaoAula =  new ArrayList<Integer>();
	private Boolean processandoProvaPresencial;
	private static Boolean processandoDocumentacaoGED;
	private Map<Integer, ProgressBarVO> mapThreadControleCobranca;
	private Map<String, List<TurmaVO>> mapTurmasOfertadas  = new HashMap<String, List<TurmaVO>>(0);
	private Map<Integer, ContaCorrenteVO> mapContaCorrenteVOs  = new HashMap<Integer, ContaCorrenteVO>(0);	
	private Map<Integer, CategoriaDespesaVO> mapCategoriaDespesaVOs = new HashMap<Integer, CategoriaDespesaVO>(0);
	private Map<Integer, CentroReceitaVO> mapCentroReceitaVOs = new HashMap<Integer, CentroReceitaVO>(0);
	private Map<Integer, CentroResultadoVO> mapCentroResultadoVOs = new HashMap<Integer, CentroResultadoVO>(0);
	private Map<Integer, GradeCurricularVO> mapGradeCurricularVOs = new HashMap<Integer, GradeCurricularVO>(0);
	private Map<Integer, CidadeVO> mapCidadeVOs = new HashMap<Integer, CidadeVO>(0);
	private Map<Integer, PaizVO> mapPaisVOs = new HashMap<Integer, PaizVO>(0);
	private Map<Integer, EstadoVO> mapEstadoVOs = new HashMap<Integer, EstadoVO>(0);
	private Map<Integer, AreaConhecimentoVO> mapAreaConhecimentoVOs = new HashMap<Integer, AreaConhecimentoVO>(0);
	private Map<String, ProgressBarVO> mapThreadIndiceReajuste;
	private ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO;
	private Map<Integer, ConfiguracaoGEDVO> mapConfiguracaoGedUnidadeEnsino = new HashMap<Integer, ConfiguracaoGEDVO>(0);
	private Map<Integer, JobProvaProcessoSeletivo> mapListaJobProcessoSeletivo ;
	private ConfiguracaoBibliotecaVO configuracaoBibliotecaPadrao ;
	private ProgressBarVO progressBarImportarCandidatoVO;
	private ProgressBarVO progressBarMapaRegistroEvasaoCursoVO;
	private Map<String, Object> mapObjeto;
	private ConfiguracaoSeiBlackboardVO configuracaoSeiBlackboardVO;
	private ProgressBarVO progressBarLinksUteisVO;
	private Map<String, String> artefatoAjudaKeys;
	private ProgressBarVO progressBarImportarCid;
	private ProgressBarVO progressBarFechamentoNota;
	private ProgressBarVO progressBarAssinarXmlMec;
	private Map<Integer, DocumentoAssinadoVO> mapDocumentoAssinadoAssinarXml;
	private Boolean authenticationLacunaGenerated;
	private Map<Integer, ConfiguracaoDiplomaDigitalVO> mapConfiguracaoDiplomaDigital;
	private ProgressBarVO progressBarIntegracaoMestreGR;
	private Map<String, OfertaDisciplinaVO> mapOfertaDisciplina = new HashMap<String, OfertaDisciplinaVO>(0);
	private ConcurrentHashMap<String, String> foldersCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> anoCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> mesCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, List<MatriculaVO>> matriculasAlunoCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, UsuarioVO> usuarioAlunoPorEmailCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, UsuarioVO> usuarioAlunoPorUsernameSenhaCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, MatriculaVO> matriculaAlunoCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, List<AvaliacaoInstitucionalVO>> matriculaAvaliacaoInstitucionalCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, MatriculaPeriodoVO> matriculaPeriodoAlunoCache = new ConcurrentHashMap<>();	
	private ConcurrentHashMap<String, MatriculaIntegralizacaoCurricularVO> matriculaIntegralizacaoCurricularCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, List<String>> anoSemestreMatriculaCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, List<MatriculaPeriodoTurmaDisciplinaVO>> matriculaPeriodoTurmaDisciplinasCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, CalendarioAgrupamentoTccVO> calendarioProjetoIntegradorCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, CalendarioAgrupamentoTccVO> calendarioTccCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Map<Integer, Integer>> matriculaEstagioDeferidoCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, PreferenciaSistemaUsuarioVO> preferenciaSistemaUsuarioCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, List<LinksUteisVO>> linksUteisUsuarioCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, DashboardEstagioVO> dashboardEstagioFacilitadorCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, DashboardEstagioVO> dashboardEstagioAlunoCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer,List<DashboardVO>> dashboardsAlunoCache = new ConcurrentHashMap<>();
	private ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioPadraoVO;
	private List<ConfiguracaoAparenciaSistemaVO> configuracaoAparenciaSistemaVOs;
	private Map<Integer, ConfiguracaoGEDVO> mapConfiguracaoGEDVOs  = new HashMap<Integer, ConfiguracaoGEDVO>(0);
	
	private LocalDateTime dataUltimaAtualizacaoAssinaturaXml;
	

	public AplicacaoControle() {
		fraseInspiracaoVO = new FraseInspiracaoVO();
	}

	@PostConstruct
	public void realizacaoInicializacaoRotinaAplicacao() {
		realizarRegistroProcessamentoInterrompidoArquivoRetorno();
		realizarReinicializacaoRotinaGeracaoManualParcela();
		realizarReinicializacaoRotinaRenovacaoMatriculaTurma();
		realizarReinicializacaoThreadAvaliacaoOnline();
		realizarReinicializacaoThreadProvaProcessoSeletivo();
		getConfiguracaoAparenciaSistemaVO();
		realizarInclusaoLayoutPadraoHistorico();
		realizarInclusaoLayoutPadraoAtaResultadosFinais();
	}

	public FacadeFactory getFacadeFactory() {
		return facadeFactory;
	}

	public void setFacadeFactory(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}

	public void ativar() {
		setMapaControleAtividadesUsuariosAtivo(Boolean.TRUE);
		setMensagem("Controle de Atividades Ativado!");
	}

	public void desativar() {
		setMapaControleAtividadesUsuariosAtivo(Boolean.FALSE);
		setMensagem("Controle de Atividades Desativado!");
	}

	public void limparRegistros() {
		getMapaControleUsuarios().clear();
		setMapaControleUsuarios(null);
		setMapaControleUsuarios(new ArrayList(0));
		getMapaAtividadesUsuarioPorEntidade().clear();
		setMapaAtividadesUsuarioPorEntidade(null);
		setMapaAtividadesUsuarioPorEntidade(new ArrayList(0));
		setMensagem("Limpeza de Recursos Controle de Atividades Desativado!");
	}

	public void atualizar() {
		montarListaMapaAtividadesUsuarioPorEntidade();
		setMensagem("Atualização realizada com Sucesso!");
	}
	
	public List obterListaDataComemorativaDataAtual(UsuarioVO usuarioLogado) throws Exception {
		List lista = new ArrayList();
		boolean naoPossuiDataComemorativaDeHoje = true;
		Enumeration keys = getListaDataComemorativaApresentar().keys();
		while (keys.hasMoreElements()) {
			Date value = (Date) keys.nextElement();
			if (value.compareTo(Uteis.getDateSemHora(new Date())) != 0) {
				naoPossuiDataComemorativaDeHoje = true;
			} else {
				lista = (List) getListaDataComemorativaApresentar().get(value);
				if (lista.isEmpty()) {
					naoPossuiDataComemorativaDeHoje = true;
				} else {
					naoPossuiDataComemorativaDeHoje = false;
				}

				break;
			}
		}
		if (naoPossuiDataComemorativaDeHoje) {
			getListaDataComemorativaApresentar().clear();
			lista = getFacadeFactory().getDataComemorativaFacade().consultarDataComemorativaDataAtualTipoMensagemTopo();
			getListaDataComemorativaApresentar().put(Uteis.getDateSemHora(new Date()), lista);
		}
		return lista;
	}

	private void montarFraseInspiricaoDoDia() {
		try {
			if (getFraseInspiracaoVO().getDataUltimaExibicao() == null) {
				setFraseInspiracaoVO(getFacadeFactory().getFraseInspiracaoFacade().consultarFraseRandom("%", false, Uteis.NIVELMONTARDADOS_TODOS, null));
				getFraseInspiracaoVO().setDataUltimaExibicao(new Date());
				// return getFraseInspiracaoVO().getFrase();
			} else if (!Uteis.getData(getFraseInspiracaoVO().getDataUltimaExibicao()).equals(Uteis.getDataAtual())) {
				setFraseInspiracaoVO(getFacadeFactory().getFraseInspiracaoFacade().consultarFraseRandom("%", false, Uteis.NIVELMONTARDADOS_TODOS, null));
				getFraseInspiracaoVO().setDataUltimaExibicao(new Date());
			}
		} catch (Exception e) {
			fraseInspiracaoVO = new FraseInspiracaoVO();
		}
	}

	public String getObterFraseInspiracaoDoDia() {
		montarFraseInspiricaoDoDia();
		return getFraseInspiracaoVO().getFrase();
	}

	public String getObterAutorFraseInspiracaoDoDia() {
		montarFraseInspiricaoDoDia();
		return getFraseInspiracaoVO().getAutor();
	}

	/**
	 * @return the fraseInspiracaoVO
	 */
	public FraseInspiracaoVO getFraseInspiracaoVO() {
		if (fraseInspiracaoVO == null) {
			return new FraseInspiracaoVO();
		}
		return fraseInspiracaoVO;
	}

	/**
	 * @param fraseInspiracaoVO
	 *            the fraseInspiracaoVO to set
	 */
	public void setFraseInspiracaoVO(FraseInspiracaoVO fraseInspiracaoVO) {
		this.fraseInspiracaoVO = fraseInspiracaoVO;
	}

	/**
	 * @return the mapaControleUsuarios
	 */
	public List<MapaControleAtividadesUsuarioVO> getMapaControleUsuarios() {
		if (mapaControleUsuarios == null) {
			mapaControleUsuarios = new ArrayList<MapaControleAtividadesUsuarioVO>(0);
		}
		return mapaControleUsuarios;
	}

	public void montarListaMapaAtividadesUsuarioPorEntidade() {
		try {
			setMapaAtividadesUsuarioPorEntidade(new ArrayList(0));
			Hashtable hash = new Hashtable(0);
			Iterator i = getMapaControleUsuarios().iterator();
			while (i.hasNext()) {
				MapaControleAtividadesUsuarioVO mapa = (MapaControleAtividadesUsuarioVO) i.next();
				Iterator j = mapa.getAtividadesUsuario().iterator();
				while (j.hasNext()) {
					ControleAtividadeUsuarioVO contr = (ControleAtividadeUsuarioVO) j.next();
					if (!hash.containsKey(contr.getEntidade() + "-" + contr.getDescricao() + "-" + contr.getTipo())) {
						contr.setNrOperacoes(1);
						hash.put(contr.getEntidade() + "-" + contr.getDescricao() + "-" + contr.getTipo(), contr);
					} else {
						ControleAtividadeUsuarioVO controle = (ControleAtividadeUsuarioVO) hash.get(contr.getEntidade() + "-" + contr.getDescricao() + "-" + contr.getTipo());
						controle.setMomento(contr.getMomento());
						controle.setNrOperacoes(controle.getNrOperacoes() + 1);
					}
				}
			}
			Enumeration e = hash.keys();
			while (e.hasMoreElements()) {
				String controle = (String) e.nextElement();
				ControleAtividadeUsuarioVO c = (ControleAtividadeUsuarioVO) hash.get(controle);
				getMapaAtividadesUsuarioPorEntidade().add(c);
			}
		} catch (Exception e) {
			setMapaAtividadesUsuarioPorEntidade(new ArrayList<ControleAtividadeUsuarioVO>(0));
		}
	}

	public void montarListaMapaAtividadesUsuarioPorUsuario() {
		try {
			setMapaAtividadesUsuarioPorUsuario(new ArrayList(0));
			Hashtable hash = new Hashtable(0);
			Iterator i = getMapaAtividadesUsuarioPorEntidade().iterator();
			while (i.hasNext()) {
				ControleAtividadeUsuarioVO mapa = (ControleAtividadeUsuarioVO) i.next();
				if (!hash.containsKey(mapa.getUsername())) {
					hash.put(mapa.getUsername(), mapa);
				}
			}
			Enumeration e = hash.keys();
			while (e.hasMoreElements()) {
				String controle = (String) e.nextElement();
				ControleAtividadeUsuarioVO c = (ControleAtividadeUsuarioVO) hash.get(controle);
				getMapaAtividadesUsuarioPorUsuario().add(c);
			}
		} catch (Exception e) {
			setMapaAtividadesUsuarioPorUsuario(new ArrayList<ControleAtividadeUsuarioVO>(0));
		}
	}

	public List<ControleAtividadeUsuarioVO> getMapaAtividadesUsuarioPorEntidade() {
		if (mapaAtividadesUsuarioPorEntidade == null) {
			mapaAtividadesUsuarioPorEntidade = new ArrayList<ControleAtividadeUsuarioVO>(0);
		}
		return mapaAtividadesUsuarioPorEntidade;
	}

	/**
	 * @param mapaControleUsuarios
	 *            the mapaControleUsuarios to set
	 */
	public void setMapaControleUsuarios(List<MapaControleAtividadesUsuarioVO> mapaControleUsuarios) {
		this.mapaControleUsuarios = mapaControleUsuarios;
	}

	public MapaControleAtividadesUsuarioVO obterMapaControleAtividadesUsuarioEspecifico(UsuarioVO usuarioVO) throws Exception {
		Iterator<MapaControleAtividadesUsuarioVO> i = getMapaControleUsuarios().iterator();
		while (i.hasNext()) {
			MapaControleAtividadesUsuarioVO mapa = i.next();
			if (mapa.getUsuarioVO().getCodigo().equals(usuarioVO.getCodigo())) {
				return mapa;
			}
		}
		return null;
	}

	public MapaControleAtividadesUsuarioVO obterMapaControleAtividadesUsuarioEspecifico(UsuarioVO usuarioVO, String idSession) throws Exception {
		Iterator<MapaControleAtividadesUsuarioVO> i = getMapaControleUsuarios().iterator();
		while (i.hasNext()) {
			MapaControleAtividadesUsuarioVO mapa = i.next();
			if (mapa.getIdSessao().equals(idSession)) {
				return mapa;
			}
		}
		return null;
	}

	public void selecionarMapaUsuario() throws Exception {
		setMapaControleAtividadesUsuarioVO((MapaControleAtividadesUsuarioVO) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("mapaUsuario"));
	}

	public void registrarAtividadeUsuarioEmNivelAplicacao(UsuarioVO usuarioVO, String idSession, String entidade, String descricao, String tipo) throws Exception {
		if (idSession == null) {
			return;
		}
		MapaControleAtividadesUsuarioVO mapaControleUsuarioVO = obterMapaControleAtividadesUsuarioEspecifico(usuarioVO, idSession);
		if (mapaControleUsuarioVO == null) {
			mapaControleUsuarioVO = new MapaControleAtividadesUsuarioVO();
			mapaControleUsuarioVO.setUsuarioVO(usuarioVO);
			mapaControleUsuarioVO.setIdSessao(idSession);
			mapaControleUsuarioVO.setDataInicioAtividade(new Date());
			mapaControleUsuarioVO.registrarNovaAtividade(usuarioVO.getUsername(), entidade, descricao, tipo);
			getMapaControleUsuarios().add(mapaControleUsuarioVO);
		} else {
			mapaControleUsuarioVO.registrarNovaAtividade(usuarioVO.getUsername(), entidade, descricao, tipo);
		}
	}

	public List<SelectItem> tipoUsuarioMonitoramento;
	public List<SelectItem> getTipoUsuarioMonitoramento() {
		if(tipoUsuarioMonitoramento == null) {
		tipoUsuarioMonitoramento = new ArrayList<SelectItem>(0);
		tipoUsuarioMonitoramento.add(new SelectItem("", ""));
		tipoUsuarioMonitoramento.add(new SelectItem("funcionario", "Funcionário"));
		tipoUsuarioMonitoramento.add(new SelectItem("aluno", "Aluno"));
		}
		return tipoUsuarioMonitoramento;
	}

	public List getMapaControleAtividadeUsuarioFiltrada() {
		List mapaControleFiltrado = new ArrayList(0);
		for (MapaControleAtividadesUsuarioVO obj : getMapaControleUsuarios()) {
			if (obj.getUsuarioVO().getPessoa().getFuncionario() && getFiltroMapaControleAtividade().equals("funcionario")) {
				mapaControleFiltrado.add(obj);
			} else if (obj.getUsuarioVO().getPessoa().getAluno() && getFiltroMapaControleAtividade().equals("aluno")) {
				mapaControleFiltrado.add(obj);
			}
		}
		montarListaMapaAtividadesUsuarioPorUsuario();
		return mapaControleFiltrado;
	}

	/**
	 * @return the mapaControleAtividadesUsuariosAtivo
	 */
	public Boolean getMapaControleAtividadesUsuariosAtivo() {
		if (mapaControleAtividadesUsuariosAtivo == null) {
			mapaControleAtividadesUsuariosAtivo = Boolean.TRUE;
		}
		return mapaControleAtividadesUsuariosAtivo;
	}

	/**
	 * @param mapaControleAtividadesUsuariosAtivo
	 *            the mapaControleAtividadesUsuariosAtivo to set
	 */
	public void setMapaControleAtividadesUsuariosAtivo(Boolean mapaControleAtividadesUsuariosAtivo) {
		this.mapaControleAtividadesUsuariosAtivo = mapaControleAtividadesUsuariosAtivo;
	}

	/**
	 * @return the mapaControleAtividadesUsuarioVO
	 */
	public MapaControleAtividadesUsuarioVO getMapaControleAtividadesUsuarioVO() {
		if (mapaControleAtividadesUsuarioVO == null) {
			mapaControleAtividadesUsuarioVO = new MapaControleAtividadesUsuarioVO();
		}
		return mapaControleAtividadesUsuarioVO;
	}

	/**
	 * @param mapaControleAtividadesUsuarioVO
	 *            the mapaControleAtividadesUsuarioVO to set
	 */
	public void setMapaControleAtividadesUsuarioVO(MapaControleAtividadesUsuarioVO mapaControleAtividadesUsuarioVO) {
		this.mapaControleAtividadesUsuarioVO = mapaControleAtividadesUsuarioVO;
	}

	/**
	 * @return the mensagem
	 */
	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	/**
	 * @param mensagem
	 *            the mensagem to set
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * @param mapaAtividadesUsuarioPorEntidade
	 *            the mapaAtividadesUsuarioPorEntidade to set
	 */
	public void setMapaAtividadesUsuarioPorEntidade(List<ControleAtividadeUsuarioVO> mapaAtividadesUsuarioPorEntidade) {
		this.mapaAtividadesUsuarioPorEntidade = mapaAtividadesUsuarioPorEntidade;
	}

	/**
	 * @return the mapaAtividadesUsuarioPorUsuario
	 */
	public List<ControleAtividadeUsuarioVO> getMapaAtividadesUsuarioPorUsuario() {
		if (mapaAtividadesUsuarioPorUsuario == null) {
			mapaAtividadesUsuarioPorUsuario = new ArrayList<ControleAtividadeUsuarioVO>();
		}
		return mapaAtividadesUsuarioPorUsuario;
	}

	/**
	 * @param mapaAtividadesUsuarioPorUsuario
	 *            the mapaAtividadesUsuarioPorUsuario to set
	 */
	public void setMapaAtividadesUsuarioPorUsuario(List<ControleAtividadeUsuarioVO> mapaAtividadesUsuarioPorUsuario) {
		this.mapaAtividadesUsuarioPorUsuario = mapaAtividadesUsuarioPorUsuario;
	}
	
	public String getFiltroMapaControleAtividade() {
		if (filtroMapaControleAtividade == null) {
			filtroMapaControleAtividade = "";
		}
		return filtroMapaControleAtividade;
	}

	public void setFiltroMapaControleAtividade(String filtroMapaControleAtividade) {
		this.filtroMapaControleAtividade = filtroMapaControleAtividade;
	}

	/**
	 * @return the listaDataComemorativaApresentar
	 */
	public Hashtable getListaDataComemorativaApresentar() {
		if (listaDataComemorativaApresentar == null) {
			listaDataComemorativaApresentar = new Hashtable(0);
		}
		return listaDataComemorativaApresentar;
	}

	/**
	 * @param listaDataComemorativaApresentar
	 *            the listaDataComemorativaApresentar to set
	 */
	public void setListaDataComemorativaApresentar(Hashtable listaDataComemorativaApresentar) {
		this.listaDataComemorativaApresentar = listaDataComemorativaApresentar;
	}

	/**
	 * @return the autenticacaoRealizada
	 */
	public Date getAutenticacaoRealizada() {
		return autenticacaoRealizada;
	}

	/**
	 * @param autenticacaoRealizada
	 *            the autenticacaoRealizada to set
	 */
	public void setAutenticacaoRealizada(Date autenticacaoRealizada) {
		this.autenticacaoRealizada = autenticacaoRealizada;
	}

	/**
	 * @return the geracaoParcelaExecutando
	 */
	public Boolean getGeracaoParcelaExecutando() {
		if (geracaoParcelaExecutando == null) {
			geracaoParcelaExecutando = Boolean.FALSE;
		}
		return geracaoParcelaExecutando;
	}

	/**
	 * @param geracaoParcelaExecutando
	 *            the geracaoParcelaExecutando to set
	 */
	public synchronized void setGeracaoParcelaExecutando(Boolean geracaoParcelaExecutando) {
		this.geracaoParcelaExecutando = geracaoParcelaExecutando;
	}

	public void realizarRegistroProcessamentoInterrompidoArquivoRetorno() {
		try {
			getFacadeFactory().getControleCobrancaFacade().realizarRegistroProcessamentoInterrompido();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarReinicializacaoRotinaGeracaoManualParcela() {
		try {
			getFacadeFactory().getGeracaoManualParcelaFacade().realizarReinicializacaoGerarManualParcelas(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void realizarGeracaoManualParcela(Integer unidadeEnsino, Boolean retornarExcecao, Boolean remover) throws Exception {
		int index = 0;
		for (Integer unidade : getListaUnidadeEnsinoGeracaoParcela()) {
			if (unidade.intValue() == unidadeEnsino.intValue()) {
				if (remover != null && remover) {
					getListaUnidadeEnsinoGeracaoParcela().remove(index);
					return;
				}
				if (retornarExcecao) {
					throw new Exception(UteisJSF.internacionalizar("msg_geracaoManualParcelaConcorrente"));
				} else {
					return;
				}
			}
			index++;
		}
		getListaUnidadeEnsinoGeracaoParcela().add(unidadeEnsino);
	}

	public List<Integer> getListaUnidadeEnsinoGeracaoParcela() {
		if (listaUnidadeEnsinoGeracaoParcela == null) {
			listaUnidadeEnsinoGeracaoParcela = new ArrayList<Integer>(0);
		}
		return listaUnidadeEnsinoGeracaoParcela;
	}

	public void setListaUnidadeEnsinoGeracaoParcela(List<Integer> listaUnidadeEnsinoGeracaoParcela) {
		this.listaUnidadeEnsinoGeracaoParcela = listaUnidadeEnsinoGeracaoParcela;
	}

	public synchronized void executarEnvioNotasFiscais(Integer confNotaFiscal, Boolean retornarExcecao, Boolean remover, Boolean adicionarUnidade) throws AcessoException {
		int index = 0;
		for (Integer conf : getConfNotaFiscalVOs()) {
			if (conf.intValue() == confNotaFiscal.intValue()) {
				if (remover != null && remover) {
					getConfNotaFiscalVOs().remove(index);
					return;
				}
				if (retornarExcecao != null && retornarExcecao) {
					throw new AcessoException(UteisJSF.internacionalizar("msg_NotaFiscalSaida_notaSendoEmitidaNestaConfigNotaFiscal"));
				}
				return;
			}
			index++;
		}
		if (adicionarUnidade != null && adicionarUnidade) {
			getConfNotaFiscalVOs().add(confNotaFiscal);
		}
	}

	public synchronized void executarBloqueioTurmaNaRenovacaoTurma(Integer key, Thread jobRenovacaoTurma, Boolean retornarExcecao, Boolean remover, Boolean adicionarTurma, Boolean interromperJob) throws AcessoException {
		if (getListaTurmaRealizandoRenovacao().containsKey(key)) {
			if (remover != null && remover) {
				if (interromperJob) {
					Thread job = getListaTurmaRealizandoRenovacao().get(key);
					if (job.isAlive()) {
						job.interrupt();
						job = null;
					}
				}
				getListaTurmaRealizandoRenovacao().remove(key);
				return;
			}
			if (retornarExcecao) {
				throw new AcessoException(UteisJSF.internacionalizar("msg_RenovacaoTurma_turmaEmRenovacao"));
			}
		}
		if (adicionarTurma && getListaTurmaRealizandoRenovacao().size() > 0) {
			throw new AcessoException(UteisJSF.internacionalizar("msg_RenovacaoTurma_turmaEmRenovacaoOperacao"));
		}else if (adicionarTurma) {
			jobRenovacaoTurma.start();
			getListaTurmaRealizandoRenovacao().put(key, jobRenovacaoTurma);
		}
	}
	public List<Integer> getConfNotaFiscalVOs() {
		if (confNotaFiscalVOs == null) {
			confNotaFiscalVOs = new ArrayList<Integer>(0);
		}
		return confNotaFiscalVOs;
	}

	public void setConfNotaFiscalVOs(List<Integer> confNotaFiscalVOs) {
		this.confNotaFiscalVOs = confNotaFiscalVOs;
	}

	public void realizarReinicializacaoThreadAvaliacaoOnline() {
		try {
			getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendarioAtividadeMatriculasOndeSituacaoAvaliacaoOnlineMatriculaEmRealizacao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Map<Integer, Thread> getListaTurmaRealizandoRenovacao() {
		if (listaTurmaRealizandoRenovacao == null) {
			listaTurmaRealizandoRenovacao = new HashMap<Integer, Thread>(0);
		}
		return listaTurmaRealizandoRenovacao;
	}
	
	private void realizarReinicializacaoRotinaRenovacaoMatriculaTurma() {
		try {
			List<RenovacaoMatriculaTurmaVO> renovacaoMatriculaTurmaVOs = getFacadeFactory().getRenovacaoMatriculaTurmaFacade().consultarPorSituacao(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null, 0, 0);
			for (RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO : renovacaoMatriculaTurmaVOs) {
				List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
				List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs = getFacadeFactory().getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade().consultarPorRenovacaoMatriculaTurma(renovacaoMatriculaTurmaVO.getCodigo(), null);
				for (RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO rmtgdcVO : renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs) {
					gradeDisciplinaCompostaVOs.add(rmtgdcVO.getGradeDisciplinaCompostaVO());
				}
				getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarInicializacaoThreadRenovacaoTurma(renovacaoMatriculaTurmaVO, this, renovacaoMatriculaTurmaVO.getResponsavel(), gradeDisciplinaCompostaVOs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the integracaoFinanceiraProcessando
	 */
	public static Integer getIntegracaoFinanceiraProcessando() {
		if (AplicacaoControle.integracaoFinanceiraProcessando == null) {
			AplicacaoControle.integracaoFinanceiraProcessando = 0;
		}
		return AplicacaoControle.integracaoFinanceiraProcessando;
	}

	/**
	 * @param integracaoFinanceiraProcessando the integracaoFinanceiraProcessando to set
	 */
	public static void setIntegracaoFinanceiraProcessando(Integer integracaoFinanceiraProcessando) {
		AplicacaoControle.integracaoFinanceiraProcessando = integracaoFinanceiraProcessando;
	}

	public static synchronized boolean realizarRegistroInicioProcessamentoIntegracaoFinanceira(Integer integracaoFinanceiraProcessando){
		if(Uteis.isAtributoPreenchido(getIntegracaoFinanceiraProcessando())){
			return false;
		}
		AplicacaoControle.setIntegracaoFinanceiraProcessando(integracaoFinanceiraProcessando);
		return true;
	}
	
	public static synchronized boolean realizarRegistroTerminoProcessamentoIntegracaoFinanceira(Integer integracaoFinanceiraProcessando){
		if(Uteis.isAtributoPreenchido(getIntegracaoFinanceiraProcessando()) && !integracaoFinanceiraProcessando.equals(getIntegracaoFinanceiraProcessando())){
			return false;
		}
		AplicacaoControle.setIntegracaoFinanceiraProcessando(0);
		return true;
	}
	
	public static synchronized void executarBloqueioTurmaProgramacaoAula(TurmaVO turma, Date update, String ano, String semestre, boolean adicionar, boolean retornarExcecao, HorarioTurmaInterfaceFacade horarioTurmaInterfaceFacade, UsuarioVO usuarioVO) throws AcessoException {
		if(adicionar) {
			Date updatedDB;
			try {
				updatedDB = horarioTurmaInterfaceFacade.consultarUpdatedPorTurmaAnoSemestre(turma.getCodigo(), ano, semestre, false, usuarioVO);
			} catch (Exception e) {
				throw new AcessoException(e.getMessage());
			}
			if ((Uteis.isAtributoPreenchido(updatedDB) && !Uteis.getDataComHoraCompleta(update).equals(Uteis.getDataComHoraCompleta(updatedDB)))) {			
				throw new AcessoException(UteisJSF.internacionalizar("msg_ProgramacaoAula_dadoDefasado").replace("{0}", turma.getIdentificadorTurma()));
			}		
		}
		for (Iterator<Integer> iterator = AplicacaoControle.turmaProgramacaoAula.iterator(); iterator.hasNext();) {
			if (iterator.next().equals(turma.getCodigo())) {
				if (!adicionar)
					iterator.remove();
				if (retornarExcecao)
					throw new AcessoException(UteisJSF.internacionalizar("msg_ProgramacaoAula_bloqueioTurma"));
				return;
			}
		}
		if (adicionar) {
			AplicacaoControle.turmaProgramacaoAula.add(turma.getCodigo());
		}
	}

	public void removerConfNotaFiscalEmEmissao() {
		Integer conf = (Integer) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("atividadeEmissaoNota");
		getConfNotaFiscalVOs().remove(conf);
	}
	

	public String getDataExecucaoJobEmail_Apresentar() {
		if (getDataExecucaoJobEmail() != null) {
			return Uteis.getDataComHora(dataExecucaoJobEmail);
		}
		return "Job não executada!";
	}
	
	public String getDataExecucaoJobDocumentacaoGED_Apresentar() {
		if (getDataExecucaoJobGED() != null) {
			return Uteis.getDataComHora(dataExecucaoJobEmail);
		}
		return "Job não executada!";
	}

	public static Date getDataExecucaoJobEmail() {
		return dataExecucaoJobEmail;
	}

	public static void setDataExecucaoJobEmail(Date dataExecucaoJobEmail) {
		AplicacaoControle.dataExecucaoJobEmail = dataExecucaoJobEmail;
	}
	
	public static Date getDataExecucaoJobGED() {
		return dataExecucaoJobGED;
	}

	public static void setDataExecucaoJobGED(Date dataExecucaoJobGED) {
		AplicacaoControle.dataExecucaoJobGED = dataExecucaoJobGED;
	}
	
	public boolean getApresentarQRCODELoginAPP() {
		return this.getCliente().getAutorizadoAplicativo() || this.getCliente().getAutorizadoAplicativoExclusivo();
	}
	
	public synchronized void executarBloqueioProcessamentoArquivoProvaPresencial(boolean adicionar, boolean retornarExcecao) throws AcessoException {
		if (getProcessandoProvaPresencial()) {
			if (!adicionar) {
				setProcessandoProvaPresencial(false);
			}
			if (retornarExcecao) {
				throw new AcessoException(UteisJSF.internacionalizar("msg_ProcessarResultadoProvaPresencial_bloqueio"));
			}
			return;
		}
		if (adicionar) {
			setProcessandoProvaPresencial(true);
		}
			
	}

	public Boolean getProcessandoProvaPresencial() {
		if (processandoProvaPresencial == null) {
			processandoProvaPresencial = false;
		}
		return processandoProvaPresencial;
	}

	public void setProcessandoProvaPresencial(Boolean processandoProvaPresencial) {
		this.processandoProvaPresencial = processandoProvaPresencial;
	}
	
	public Cliente getCliente() {
		if (cliente == null) {
			cliente = new Cliente();
		}
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	private String mensagemTelaLogin;

	public String getMensagemTelaLogin() {
		if(mensagemTelaLogin == null) {
			mensagemTelaLogin = "";
			ConfiguracaoGeralSistemaVO conf;
			try {
				conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorMensagemTelaLoginConfiguracaoPadraoSistema();
				mensagemTelaLogin = conf.getMensagemTelaLogin();
			} catch (Exception e) {
				
			}
		}
		return mensagemTelaLogin;
	}

	public void setMensagemTelaLogin(String mensagemTelaLogin) {
		this.mensagemTelaLogin = mensagemTelaLogin;
	}
	
	public static synchronized boolean registrarProcessamentoDigitalizacaoGED() {
		if(AplicacaoControle.processandoDocumentacaoGED == null || AplicacaoControle.processandoDocumentacaoGED == false) {
			AplicacaoControle.processandoDocumentacaoGED = true;
		}
		return AplicacaoControle.processandoDocumentacaoGED;
	}			
	
	public static synchronized void registrarTerminoProcessamentoDigitalizacaoGED() {		
		AplicacaoControle.processandoDocumentacaoGED = false;		
	}

	public Map<String, List<TurmaVO>> getMapTurmasOfertadas() {
		if(mapTurmasOfertadas == null) {
			mapTurmasOfertadas = new HashMap<>();
		}
		return mapTurmasOfertadas;
	}



	public synchronized ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO(Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
		return manterConfiguracaoGeralSistemaEmNivelAplicacao(null, unidadeEnsino, usuarioLogado, false);
	}

	public synchronized void removerConfiguracaoGeralSistemaEmNivelAplicacao(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		manterConfiguracaoGeralSistemaEmNivelAplicacao(configuracaoGeralSistemaVO, null, null, true);
	}
	
	public synchronized void removerConfiguracaoGeralSistemaPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		manterConfiguracaoGeralSistemaEmNivelAplicacao(null, unidadeEnsino, null, true);
	}
	
	private synchronized ConfiguracaoGeralSistemaVO  manterConfiguracaoGeralSistemaEmNivelAplicacao(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Integer unidadeEnsino, UsuarioVO usuarioLogado, boolean remover) throws Exception{
		if(remover) {
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
			Iterator<ConfiguracaoGeralSistemaVO> i = listaConfiguracaoGeralSistemaVOAtiva.iterator();
			int index = 0;
			while (i.hasNext()) {
				ConfiguracaoGeralSistemaVO configuracaoVerificarVO = i.next();
				if (configuracaoVerificarVO.getCodigo().equals(configuracaoGeralSistemaVO.getCodigo())) {
					listaConfiguracaoGeralSistemaVOAtiva.remove(index);
					return configuracaoGeralSistemaVO;
				}
				index++;
			}
			}
			if(unidadeEnsino != null) {
				if(mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.containsKey(unidadeEnsino)) {
					mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.remove(unidadeEnsino);
				}
			}
			if(configuracaoGeralSistemaVO == null && unidadeEnsino== null) {
				listaConfiguracaoGeralSistemaVOAtiva.clear();
				mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.clear();
			}
		}else {
			Integer codigo = 0;
			if(mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.containsKey(unidadeEnsino == null ? 0 : unidadeEnsino) && Uteis.isAtributoPreenchido(mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.get(unidadeEnsino == null ? 0 : unidadeEnsino))) {
				codigo = mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.get(unidadeEnsino == null ? 0 : unidadeEnsino);
			}else {
				codigo = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarCodigoConfiguracaoReferenteUnidadeEnsino(unidadeEnsino);
			}
			Iterator<ConfiguracaoGeralSistemaVO> i = listaConfiguracaoGeralSistemaVOAtiva.iterator();		
			while (i.hasNext()) {
				ConfiguracaoGeralSistemaVO configuracaoVerificarVO = i.next();
				if (Uteis.isAtributoPreenchido(configuracaoVerificarVO) && configuracaoVerificarVO.getCodigo().equals(codigo)) {
					if(!mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.containsKey(unidadeEnsino == null ? 0 : unidadeEnsino)) {
						mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.put(unidadeEnsino == null ? 0 : unidadeEnsino, configuracaoVerificarVO.getCodigo());						
					}
					return configuracaoVerificarVO;
				}
			}
			configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracaoGeralSistema(codigo, false, usuarioLogado, Uteis.NIVELMONTARDADOS_TODOS);		
			if(mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.containsKey(unidadeEnsino == null ? 0 : unidadeEnsino)){
				mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.remove(unidadeEnsino == null ? 0 : unidadeEnsino);				
			}
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				boolean existeAdd =  false;
				for(ConfiguracaoGeralSistemaVO configuracaoGeralSistema: listaConfiguracaoGeralSistemaVOAtiva) {
					if(configuracaoGeralSistemaVO.getCodigo().equals(configuracaoGeralSistema.getCodigo())) {
						existeAdd = true;
						break;
					}				
				}
				if(!existeAdd) {
					listaConfiguracaoGeralSistemaVOAtiva.add(configuracaoGeralSistemaVO);
				}
				mapConfiguracaoGeralSistemaVOPorUnidadeEnsino.put(unidadeEnsino == null ? 0 : unidadeEnsino, configuracaoGeralSistemaVO.getCodigo());		
			}	
		}
		return configuracaoGeralSistemaVO;
	}
	

	

	public synchronized ConfiguracaoAcademicoVO carregarDadosConfiguracaoAcademica(Integer codigoConfAcademico) throws Exception {
		return manterConfiguracaoAcademicoEmNivelAplicacao(codigoConfAcademico, false);	
	}	
	
	private synchronized ConfiguracaoAcademicoVO manterConfiguracaoAcademicoEmNivelAplicacao(Integer codigoConfAcademico, boolean remover) throws Exception {
		if(remover) {
			if(mapConfiguracaoAcademica.containsKey(codigoConfAcademico)){
				return mapConfiguracaoAcademica.remove(codigoConfAcademico);
			}
		}else {
			if(!mapConfiguracaoAcademica.containsKey(codigoConfAcademico)){
				if(Uteis.isAtributoPreenchido(codigoConfAcademico)) {
					mapConfiguracaoAcademica.put(codigoConfAcademico, getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimariaUnica(codigoConfAcademico, null));
				}else {
					mapConfiguracaoAcademica.put(codigoConfAcademico, new ConfiguracaoAcademicoVO());
				}
			}
			return mapConfiguracaoAcademica.get(codigoConfAcademico);		
		}
		return null;
	}
	
	public synchronized void removerConfiguracaoAcademica(Integer codigoConfAcademico) throws Exception {
		manterConfiguracaoAcademicoEmNivelAplicacao(codigoConfAcademico, true);	
	}
	
	public synchronized void removerConfiguracaoBibliotecaPadrao() throws Exception {
		manterConfiguracaoBibliotecaPadraoEmNivelAplicacao(true);	
	}
	
	public synchronized ConfiguracaoBibliotecaVO carregarDadosConfiguracaoBibliotecaPadrao() throws Exception {
		return manterConfiguracaoBibliotecaPadraoEmNivelAplicacao(false);	
	}
	
	
	private synchronized ConfiguracaoBibliotecaVO manterConfiguracaoBibliotecaPadraoEmNivelAplicacao(boolean remover) throws Exception {
		if(remover) {
			this.setConfiguracaoBibliotecaPadrao(null);			
		}else {
			if(!Uteis.isAtributoPreenchido(getConfiguracaoBibliotecaPadrao())) {
				configuracaoBibliotecaPadrao = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPadrao(Uteis.NIVELMONTARDADOS_DADOSBASICOS ,null);
			}			
			return configuracaoBibliotecaPadrao ; 
		}
		return null;
	}

	//TODO 
	//private HashMap<Integer, Integer> mapConfiguracaoFinanceiroUnidadeEnsino = new HashMap<Integer, Integer>(0);	

	public synchronized ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO(Integer unidadeEnsino) throws Exception {			
		return manterConfiguracaoFinanceiraEmNivelAplicacao(null, unidadeEnsino, false, null);
	}
	
	public synchronized ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroPorCodigoVO(Integer codigo) throws Exception {			
		return manterConfiguracaoFinanceiraEmNivelAplicacao(null, null, false, codigo);
	}
	
	
	public synchronized void removerConfiguracaoFinanceiraEmNivelAplicacao(ConfiguracaoFinanceiroVO configuracaoCarregarDados) throws Exception {
		manterConfiguracaoFinanceiraEmNivelAplicacao(configuracaoCarregarDados, 0, true, null);
	}
	

	private synchronized ConfiguracaoFinanceiroVO manterConfiguracaoFinanceiraEmNivelAplicacao(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, boolean remover, Integer codigoConfiguracaoFinanceiro) throws Exception {
		if(remover) {
			if(Uteis.isAtributoPreenchido(configuracaoFinanceiroVO)) {
			Iterator<ConfiguracaoFinanceiroVO> i = listaConfiguracaoFinanceiroVOAtiva.iterator();
			int index = 0;
			while (i.hasNext()) {
				ConfiguracaoFinanceiroVO configuracaoVerificarVO = i.next();
				if (Uteis.isAtributoPreenchido(configuracaoVerificarVO) && configuracaoVerificarVO.getCodigo().equals(configuracaoFinanceiroVO.getCodigo())) {					
					listaConfiguracaoFinanceiroVOAtiva.remove(index).getListaConfiguracaoFinanceiroCartaoVO().forEach(confCartao -> removerConfiguracaoFinanceiroCartao(confCartao.getCodigo()));
					return configuracaoVerificarVO;
				}
				index++;
			}				
			}
			if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
				if(mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino)) {
					mapConfiguracaoFinanceiroUnidadeEnsino.remove(unidadeEnsino).getListaConfiguracaoFinanceiroCartaoVO().forEach(confCartao -> removerConfiguracaoFinanceiroCartao(confCartao.getCodigo()));				
				}	
			}
			if(!Uteis.isAtributoPreenchido(configuracaoFinanceiroVO) && !Uteis.isAtributoPreenchido(unidadeEnsino)) {
				listaConfiguracaoFinanceiroVOAtiva.clear();
				mapConfiguracaoFinanceiroUnidadeEnsino.clear();
				mapConfiguracaoFinanceiroCartaoVOs.clear();
			}
		}else {
			if(Uteis.isAtributoPreenchido(codigoConfiguracaoFinanceiro)) {
				
				for(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO2: listaConfiguracaoFinanceiroVOAtiva) {
					if(configuracaoFinanceiroVO2 != null && configuracaoFinanceiroVO2.getCodigo().equals(codigoConfiguracaoFinanceiro)) {
						return 	configuracaoFinanceiroVO2;
					}				
				}				
				configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorChavePrimariaUnica(codigoConfiguracaoFinanceiro, null);
				listaConfiguracaoFinanceiroVOAtiva.add(configuracaoFinanceiroVO);		
				return 	configuracaoFinanceiroVO;				
			}else {
				Integer codigoConfiguracaoFinanceira = 0;
				if(mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino == null ? 0 : unidadeEnsino) && Uteis.isAtributoPreenchido(mapConfiguracaoFinanceiroUnidadeEnsino.get(unidadeEnsino == null ? 0 : unidadeEnsino))) {
					codigoConfiguracaoFinanceira = mapConfiguracaoFinanceiroUnidadeEnsino.get(unidadeEnsino == null ? 0 : unidadeEnsino).getCodigo();
				}		
				if(Uteis.isAtributoPreenchido(codigoConfiguracaoFinanceira)) {
					for(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO2: listaConfiguracaoFinanceiroVOAtiva) {
						if(configuracaoFinanceiroVO2.getCodigo().equals(codigoConfiguracaoFinanceira)) {					
							return configuracaoFinanceiroVO2;
						}				
					}
				}
			configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoNivelAplicacaoSerUsada(unidadeEnsino);
			boolean existeAdd =  false;
			for(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO2: listaConfiguracaoFinanceiroVOAtiva) {
				if(configuracaoFinanceiroVO2 != null && configuracaoFinanceiroVO2.getCodigo().equals(configuracaoFinanceiroVO.getCodigo())) {
					existeAdd = true;
				}				
			}
			if(!existeAdd) {
				listaConfiguracaoFinanceiroVOAtiva.add(configuracaoFinanceiroVO);				
			}				
			if(!mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino == null ? 0 : unidadeEnsino)) {
				mapConfiguracaoFinanceiroUnidadeEnsino.put(unidadeEnsino == null ? 0 : unidadeEnsino, configuracaoFinanceiroVO);
			}
			}
		}
		return configuracaoFinanceiroVO;
	}

	public synchronized void removerConfiguracaoFinanceiraNoMapConfiguracaoUnidadeEnsino(Integer unidadeEnsino) throws Exception  {		
		manterConfiguracaoFinanceiraEmNivelAplicacao(null, unidadeEnsino, true, null);
	}

	public synchronized void atualizarModeloBoletoConfiguracaoFinanceiraEmNivelAplicacao(ModeloBoletoVO modeloBoletoVO) throws Exception {
		if(Uteis.isAtributoPreenchido(modeloBoletoVO)) {
		Iterator<ConfiguracaoFinanceiroVO> i = listaConfiguracaoFinanceiroVOAtiva.iterator();		
		while (i.hasNext()) {
			ConfiguracaoFinanceiroVO configuracaoVerificarVO = i.next();
			if(Uteis.isAtributoPreenchido(configuracaoVerificarVO)) {
			if (configuracaoVerificarVO.getModeloBoletoBiblioteca().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoBiblioteca((ModeloBoletoVO)modeloBoletoVO.clone());
			}					
			if (configuracaoVerificarVO.getModeloBoletoMaterialDidatico().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoMaterialDidatico((ModeloBoletoVO)modeloBoletoVO.clone());
			}					
			if (configuracaoVerificarVO.getModeloBoletoMatricula().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoMatricula((ModeloBoletoVO)modeloBoletoVO.clone());
			}					
			if (configuracaoVerificarVO.getModeloBoletoMensalidade().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoMensalidade((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if (configuracaoVerificarVO.getModeloBoletoOutros().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoOutros((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if (configuracaoVerificarVO.getModeloBoletoProcessoSeletivo().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoProcessoSeletivo((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if (configuracaoVerificarVO.getModeloBoletoReimpressao().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoReimpressao((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if (configuracaoVerificarVO.getModeloBoletoRenegociacao().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoRenegociacao((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if (configuracaoVerificarVO.getModeloBoletoRequerimento().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				configuracaoVerificarVO.setModeloBoletoRequerimento((ModeloBoletoVO)modeloBoletoVO.clone());
			}			
			}
		}
		}
	}

	public synchronized void atualizarContaCorrenteConfiguracaoFinanceiraEmNivelAplicacao(ContaCorrenteVO contaCorrenteVO) throws Exception {
		if (Uteis.isAtributoPreenchido(contaCorrenteVO)) {
			Iterator<ConfiguracaoFinanceiroVO> i = listaConfiguracaoFinanceiroVOAtiva.iterator();
			while (i.hasNext()) {
				ConfiguracaoFinanceiroVO configuracaoVerificarVO = i.next();
				if (Uteis.isAtributoPreenchido(configuracaoVerificarVO)) {
					if (configuracaoVerificarVO.getContaCorrentePadraoControleCobranca().getCodigo().equals(contaCorrenteVO.getCodigo())) {
						configuracaoVerificarVO.setContaCorrentePadraoControleCobranca((ContaCorrenteVO)contaCorrenteVO.clone());
					}
					if (configuracaoVerificarVO.getContaCorrenteReimpressaoBoletos().getCodigo().equals(contaCorrenteVO.getCodigo())) {
						configuracaoVerificarVO.setContaCorrenteReimpressaoBoletos((ContaCorrenteVO)contaCorrenteVO.clone());
					}
				}
			}
		}
	}
	
	public synchronized void liberarListaConfiguracaoFinanceiraMemoria() throws Exception {
		manterConfiguracaoFinanceiraEmNivelAplicacao(null, null, true, null);			
	}
	
	public synchronized void liberarListaConfiguracaoGeralMemoria()  throws Exception {
		manterConfiguracaoGeralSistemaEmNivelAplicacao(null, null, null, true);
	}
	
	public synchronized void liberarListaConfiguracaoAcademicaMemoria() {
		mapConfiguracaoAcademica.clear();		
	}
	
	public synchronized void liberarListaContaCorrenteMemoria() {
		mapContaCorrenteVOs.clear();		
	}
	
	public synchronized ContaCorrenteVO getContaCorrenteVO(Integer codigoConta, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoConta)) {
			if(!mapContaCorrenteVOs.containsKey(codigoConta) || !Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(codigoConta))  
					|| mapContaCorrenteVOs.get(codigoConta).getUnidadeEnsinoContaCorrenteVOs().isEmpty()
					|| !mapContaCorrenteVOs.get(codigoConta).getCodigo().equals(codigoConta)) {
				if(mapContaCorrenteVOs.containsKey(codigoConta)) {
					mapContaCorrenteVOs.remove(codigoConta);
				}
				ContaCorrenteVO contaCorrenteVO =  getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimariaUnica(codigoConta, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapContaCorrenteVOs.put(codigoConta, contaCorrenteVO);
			}
			return mapContaCorrenteVOs.get(codigoConta).getClonePersistido();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerContaCorrente(Integer contaCorrente) throws Exception {
		if(mapContaCorrenteVOs.containsKey(contaCorrente)) {
			mapContaCorrenteVOs.remove(contaCorrente);
		}
	}
	
	public synchronized void atualizarAgenciaContaCorrente(AgenciaVO agenciaVO) throws Exception {
		for(Integer contaCorrente: mapContaCorrenteVOs.keySet()) {			
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getCodigo().equals(agenciaVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).setAgencia((AgenciaVO)agenciaVO.clone());
			}
		}
	}
	
	public synchronized void atualizarBancoAgenciaContaCorrente(BancoVO bancoVO) throws Exception {
		for(Integer contaCorrente: mapContaCorrenteVOs.keySet()) {			
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().getCodigo().equals(bancoVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).getAgencia().setBanco((BancoVO)bancoVO.clone());
			}
		}
	}
	
	public synchronized void atualizarModeloBoletoBancoAgenciaContaCorrente(ModeloBoletoVO modeloBoletoVO) throws Exception {
		for(Integer contaCorrente: mapContaCorrenteVOs.keySet()) {			
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().getModeloBoletoMaterialDidatico().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().setModeloBoletoMaterialDidatico((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().getModeloBoletoMatricula().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().setModeloBoletoMatricula((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().getModeloBoletoMensalidade().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().setModeloBoletoMensalidade((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().getModeloBoletoOutros().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().setModeloBoletoOutros((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().getModeloBoletoProcessoSeletivo().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().setModeloBoletoProcessoSeletivo((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().getModeloBoletoRenegociacao().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().setModeloBoletoRenegociacao((ModeloBoletoVO)modeloBoletoVO.clone());
			}
			if(Uteis.isAtributoPreenchido(mapContaCorrenteVOs.get(contaCorrente)) && mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().getModeloBoletoRequerimento().getCodigo().equals(modeloBoletoVO.getCodigo())) {
				mapContaCorrenteVOs.get(contaCorrente).getAgencia().getBanco().setModeloBoletoRequerimento((ModeloBoletoVO)modeloBoletoVO.clone());
			}
		}
	}
	
	public synchronized void adicionarMapThreadControleCobranca(Integer codigoOrigem, ProgressBarVO progressBarVO)  {
		if(!getMapThreadControleCobranca().containsKey(codigoOrigem)) {
			getMapThreadControleCobranca().put(codigoOrigem, progressBarVO);
		}
	}
	
	public synchronized void removerMapThreadControleCobranca(Integer codigoOrigem)  {
		if(getMapThreadControleCobranca().containsKey(codigoOrigem)) {
			getMapThreadControleCobranca().remove(codigoOrigem);
		}
	}
	

	public Map<Integer, ProgressBarVO> getMapThreadControleCobranca() {
		if(mapThreadControleCobranca == null) {
			mapThreadControleCobranca =  new HashMap<Integer, ProgressBarVO>();
		}
		return mapThreadControleCobranca;
	}

	public void setMapThreadControleCobranca(Map<Integer, ProgressBarVO> mapThreadControleCobranca) {
		this.mapThreadControleCobranca = mapThreadControleCobranca;
	}
	
	public synchronized void liberarListaCategoriaDespesaMemoria() {
		mapCategoriaDespesaVOs.clear();		
	}
	
	public synchronized CategoriaDespesaVO getCategoriaDespesaVO(Integer codigoCategoriaDespesa, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoCategoriaDespesa)) {
			if(!mapCategoriaDespesaVOs.containsKey(codigoCategoriaDespesa) || !Uteis.isAtributoPreenchido(mapCategoriaDespesaVOs.get(codigoCategoriaDespesa))) {
				CategoriaDespesaVO categoriaDespesaVO =  getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimariaUnica(codigoCategoriaDespesa, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapCategoriaDespesaVOs.put(codigoCategoriaDespesa, categoriaDespesaVO);
			}
			return (CategoriaDespesaVO)mapCategoriaDespesaVOs.get(codigoCategoriaDespesa).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerCategoriaDespesa(Integer codigoCategoriaDespesa) throws Exception {
		if(mapCategoriaDespesaVOs.containsKey(codigoCategoriaDespesa)) {
			mapCategoriaDespesaVOs.remove(codigoCategoriaDespesa);
		}
	}
	
	public synchronized void liberarListaCentroResultadoMemoria() {
		mapCentroResultadoVOs.clear();		
	}
	
	public synchronized CentroResultadoVO getCentroResultadoVO(Integer codigoCentroResultado, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoCentroResultado)) {
			if(!mapCentroResultadoVOs.containsKey(codigoCentroResultado) || !Uteis.isAtributoPreenchido(mapCentroResultadoVOs.get(codigoCentroResultado))) {
				CentroResultadoVO centroResultadoVO =  getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimariaUnica(codigoCentroResultado, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapCentroResultadoVOs.put(codigoCentroResultado, centroResultadoVO);
			}
			return (CentroResultadoVO)mapCentroResultadoVOs.get(codigoCentroResultado).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerCentroResultado(Integer codigoCentroResultado) throws Exception {
		if(mapCentroResultadoVOs.containsKey(codigoCentroResultado)) {
			mapCentroResultadoVOs.remove(codigoCentroResultado);
		}
	}
	
	
	public synchronized void liberarListaCentroReceitaMemoria() {
		mapCentroReceitaVOs.clear();		
	}
	
	public synchronized CentroReceitaVO getCentroReceitaVO(Integer codigoCentroReceita, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoCentroReceita)) {
			if(!mapCentroReceitaVOs.containsKey(codigoCentroReceita) || !Uteis.isAtributoPreenchido(mapCentroReceitaVOs.get(codigoCentroReceita))) {
				CentroReceitaVO centroReceitaVO =  getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimariaUnica(codigoCentroReceita, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapCentroReceitaVOs.put(codigoCentroReceita, centroReceitaVO);
			}
			return (CentroReceitaVO)mapCentroReceitaVOs.get(codigoCentroReceita).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerCentroReceita(Integer codigoCentroReceita) throws Exception {
		if(mapCentroReceitaVOs.containsKey(codigoCentroReceita)) {
			mapCentroReceitaVOs.remove(codigoCentroReceita);
		}
	}
	
	
	public synchronized void liberarListaCidadeMemoria() {
		mapCidadeVOs.clear();		
	}
	
	public synchronized CidadeVO getCidadeVO(Integer codigoCidade, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoCidade)) {
			if(!mapCidadeVOs.containsKey(codigoCidade) || !Uteis.isAtributoPreenchido(mapCidadeVOs.get(codigoCidade))) {
				CidadeVO cidadeVO =  getFacadeFactory().getCidadeFacade().consultarPorChavePrimariaUnica(codigoCidade, false, usuario);
				mapCidadeVOs.put(codigoCidade, cidadeVO);
			}
			return (CidadeVO)mapCidadeVOs.get(codigoCidade).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerCidade(Integer codigoCidade) throws Exception {
		if(mapCidadeVOs.containsKey(codigoCidade)) {
			mapCidadeVOs.remove(codigoCidade);
		}
	}

	private HashMap<Integer, ConfiguracaoFinanceiroVO> mapConfiguracaoFinanceiroUnidadeEnsino = new HashMap<Integer, ConfiguracaoFinanceiroVO>(0);

	public synchronized ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			if (!mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino)) {
				ConfiguracaoFinanceiroVO cfg = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, null, unidadeEnsino);
				if ((cfg != null) && (!cfg.getCodigo().equals(0))) {
					mapConfiguracaoFinanceiroUnidadeEnsino.put(unidadeEnsino, cfg);
					return cfg;
				}else {
					return new ConfiguracaoFinanceiroVO();
				}
			} else {
				return mapConfiguracaoFinanceiroUnidadeEnsino.get(unidadeEnsino);
			}
		}else {
			if(mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(0)) {
				return mapConfiguracaoFinanceiroUnidadeEnsino.get(0);
			}else {
				ConfiguracaoFinanceiroVO cfg = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorCodigoConfiguracoes(getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoPadrao(false, Uteis.NIVELMONTARDADOS_TODOS, null).getCodigo(), true, null);
				if ((cfg != null) && (!cfg.getCodigo().equals(0))) {
					mapConfiguracaoFinanceiroUnidadeEnsino.put(0, cfg);
					return cfg;
				}else {
					return new ConfiguracaoFinanceiroVO();
				}
			}
		}
	}

	public synchronized void realizarAtualizacaoConfiguracaoFinanceiraNoMapConfiguracaoUnidadeEnsino(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		for(Integer unidadeEnsino : mapConfiguracaoFinanceiroUnidadeEnsino.keySet()) {
			if(mapConfiguracaoFinanceiroUnidadeEnsino.get(unidadeEnsino).getCodigo().equals(configuracaoFinanceiroVO.getCodigo())) {
				mapConfiguracaoFinanceiroUnidadeEnsino.remove(unidadeEnsino);
				mapConfiguracaoFinanceiroUnidadeEnsino.put(unidadeEnsino, configuracaoFinanceiroVO);
			}
		}
	}

	public synchronized void realizarConfiguracaoFinanceiraNoMapConfiguracaoUnidadeEnsino(Integer unidadeEnsino) {		
		if(mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino)) {
			mapConfiguracaoFinanceiroUnidadeEnsino.remove(unidadeEnsino);				
		}		
	}

	public Map<String, ProgressBarVO> getMapThreadIndiceReajuste() {
		if(mapThreadIndiceReajuste == null) {
			mapThreadIndiceReajuste =  new HashMap<String, ProgressBarVO>();
		}
		return mapThreadIndiceReajuste;
	}

	public void setMapThreadIndiceReajuste(Map<String, ProgressBarVO> mapThreadIndiceReajuste) {
		this.mapThreadIndiceReajuste = mapThreadIndiceReajuste;
	}

	/* Inicio Debug */
	public static final Integer LIMITE_CARACTERES_TEXTO_DEBUG = 2000000; // 2.000.000 caracteres correspondem a aproximadamente 4MB em memória.
	private static Boolean debug;
	private static StringBuilder textoDebug;
	
	public static Boolean getDebug() {
		if (debug == null) {
			debug = "true".equals(UteisJSF.getParametrosSistema("prt_debug").toLowerCase());
		}
		return debug;
	}

	public static StringBuilder getTextoDebug() {
		if (textoDebug == null) {
			textoDebug = new StringBuilder();
		}
		return textoDebug;
	}
	
	public Boolean getIsDebug() {
		return getDebug();
	}
	
	public String getStringDebug() {
		return getTextoDebug().toString();
	}
	
	public static void realizarAtivacaoDebug() {
		debug = true;
	}
	
	public static void realizarDesativacaoDebug() {
		debug = false;
	}
	
	public static void realizarEscritaTextoDebug(AssuntoDebugEnum assunto, String text) {
        if (getDebug()) {
            try {
                StringBuilder texto = new StringBuilder(UteisData.getDataComHoraMinutoSegundo(new Date()));
                texto.append(" [").append(assunto.name()).append("] ").append(text).append("\n");
                escreverTextoDebug(texto.toString());
            } catch (Exception e) {
                getTextoDebug().append("FALHA AO ESCREVER DEBUG: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
	
    public static void realizarEscritaErroDebug(AssuntoDebugEnum assunto, Exception erro) {
        if (getDebug()) {
            try {
                StringBuilder texto = new StringBuilder(UteisData.getDataComHoraMinutoSegundo(new Date())).append(" [").append(assunto.name()).append("] Exception: ").append(erro.getMessage()).append("\n");
                for (StackTraceElement ste : erro.getStackTrace()) {
                    texto.append("  ").append(ste.toString()).append("\n");
                }
                escreverTextoDebug(texto.toString());
            } catch (Exception e) {
                getTextoDebug().append("FALHA AO ESCREVER DEBUG: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private synchronized static void escreverTextoDebug(String texto) {
        getTextoDebug().append(texto.toString());
        realizarLimpezaDebug();
    }
	
	private static void realizarLimpezaDebug() {
		try {
			if (getTextoDebug().length() > LIMITE_CARACTERES_TEXTO_DEBUG && Uteis.isAtributoPreenchido(getTextoDebug().toString())) {
				getTextoDebug().delete(0, getTextoDebug().length() / 2);
			}
		} catch (Exception ex) {
			System.out.println("FALHA AO REMOVER CARACTERES TEXTO DEBUG!");
			ex.printStackTrace();
		}
	}
	/* Fim Debug */
	
	/* Inicio Scripts */
	private static String mensagemErroScriptsExecutados;
	private static List<ScriptExecutadoVO> scriptsNaoExecutados;
	private static List<ScriptExecutadoVO> scriptsNaoPersistidos;
	private static List<ScriptExecutadoVO> scriptsAbortados;
	private static List<ScriptExecutadoVO> scriptsInvalidos;

	public static String getMensagemErroScriptsExecutados() {
		if (mensagemErroScriptsExecutados == null) {
			mensagemErroScriptsExecutados = "";
		}
		return mensagemErroScriptsExecutados;
	}

	public static void setMensagemErroScriptsExecutados(String mensagemErroScriptsExecutados) {
		AplicacaoControle.mensagemErroScriptsExecutados = mensagemErroScriptsExecutados;
	}
	
	public boolean getIsPossuiFalhaScriptExecutado() {
		return (getScriptsNaoExecutados().size() + getScriptsNaoPersistidos().size() + getScriptsAbortados().size() + getScriptsInvalidos().size() > 0) ||
				Uteis.isAtributoPreenchido(getMensagemErroScriptsExecutados());
	}
	
	public static void limparScriptsComFalha() {
		setMensagemErroScriptsExecutados("");
		getScriptsNaoExecutados().clear();
		getScriptsNaoPersistidos().clear();
		getScriptsAbortados().clear();
		getScriptsInvalidos().clear();
	}
	
	public static List<ScriptExecutadoVO> getScriptsNaoExecutados() {
		if (scriptsNaoExecutados == null) {
			scriptsNaoExecutados = new ArrayList<ScriptExecutadoVO>(0);
		}
		return scriptsNaoExecutados;
	}

	public static void setScriptsNaoExecutados(List<ScriptExecutadoVO> scriptsNaoExecutados) {
		AplicacaoControle.scriptsNaoExecutados = scriptsNaoExecutados;
	}
	
	public static List<ScriptExecutadoVO> getScriptsNaoPersistidos() {
		if (scriptsNaoPersistidos == null) {
			scriptsNaoPersistidos = new ArrayList<ScriptExecutadoVO>(0);
		}
		return scriptsNaoPersistidos;
	}

	public static void setScriptsNaoPersistidos(List<ScriptExecutadoVO> scriptsNaoPersistidos) {
		AplicacaoControle.scriptsNaoPersistidos = scriptsNaoPersistidos;
	}
	
	public static List<ScriptExecutadoVO> getScriptsAbortados() {
		if (scriptsAbortados == null) {
			scriptsAbortados = new ArrayList<ScriptExecutadoVO>(0);
		}
		return scriptsAbortados;
	}
	
	public static void setScriptsAbortados(List<ScriptExecutadoVO> scriptsAbortados) {
		AplicacaoControle.scriptsAbortados = scriptsAbortados;
	}
	
	public static List<ScriptExecutadoVO> getScriptsInvalidos() {
		if (scriptsInvalidos == null) {
			scriptsInvalidos = new ArrayList<ScriptExecutadoVO>(0);
		}
		return scriptsInvalidos;
	}
	
	public static void setScriptsInvalidos(List<ScriptExecutadoVO> scriptsInvalidos) {
		AplicacaoControle.scriptsInvalidos = scriptsInvalidos;
	}
	/* Fim Scripts */

	public void realizarReinicializacaoThreadProvaProcessoSeletivo() {
		try {
			getFacadeFactory().getInscricaoFacade().consultarInscricaoProvaProcessoSeletivoEmRealizacao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized ConfiguracaoGEDVO getConfiguracaoGEDPorUnidadeEnsino(Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
		if(mapConfiguracaoGedUnidadeEnsino.containsKey(unidadeEnsino)) {
			return mapConfiguracaoGedUnidadeEnsino.get(unidadeEnsino);
		}
		ConfiguracaoGEDVO configuracaoGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsinoUnica(unidadeEnsino, false, usuarioLogado);
		mapConfiguracaoGedUnidadeEnsino.put(unidadeEnsino, configuracaoGEDVO);
		return configuracaoGEDVO;
	}

	public ConfiguracaoAparenciaSistemaVO getConfiguracaoAparenciaSistemaVO() {
		if(configuracaoAparenciaSistemaVO == null) {
			try {
				configuracaoAparenciaSistemaVO =  getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().consultarConfiguracaoAparenciaSistema(false, null);
			} catch (Exception e) {
				configuracaoAparenciaSistemaVO =  new ConfiguracaoAparenciaSistemaVO();
				getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().realizarGeracaoScriptCss(configuracaoAparenciaSistemaVO);
			}
		}
		return configuracaoAparenciaSistemaVO;
	}

	public void setConfiguracaoAparenciaSistemaVO(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO) {
		this.configuracaoAparenciaSistemaVO = configuracaoAparenciaSistemaVO;
	}
	
	
	public synchronized void removerConfiguracaoGEDVOPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		if(mapConfiguracaoGedUnidadeEnsino.containsKey(unidadeEnsino)) {
			mapConfiguracaoGedUnidadeEnsino.remove(unidadeEnsino);
		}
	}
	
	public synchronized void removerConfiguracaoGEDVO(Integer configuracaoGED) throws Exception {
		Iterator<Entry<Integer, ConfiguracaoGEDVO>> iterator = mapConfiguracaoGedUnidadeEnsino.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, ConfiguracaoGEDVO> next = iterator.next();
			if (Uteis.isAtributoPreenchido(next.getValue()) && next.getValue().getCodigo().equals(configuracaoGED)) {
				iterator.remove();
			}
		}
		
		if(mapConfiguracaoGEDVOs.containsKey(configuracaoGED)) {
			mapConfiguracaoGEDVOs.remove(configuracaoGED);
		}
	}

	
	
	
		
	
	public synchronized JobProvaProcessoSeletivo getJobProvaProcessoSeletivoPorInscricao(Integer inscricao) throws Exception {
		if(getMapListaJobProcessoSeletivo().containsKey(inscricao)) {
			return getMapListaJobProcessoSeletivo().get(inscricao);
		}
		return null ;
	}
	
	public synchronized void removerJobProvaProcessoSeletivoPorInscricao(Integer inscricao) throws Exception {
		if(getMapListaJobProcessoSeletivo().containsKey(inscricao)) {		
			getMapListaJobProcessoSeletivo().remove(inscricao);			
		}
	}
	
	
	public synchronized Map<Integer ,JobProvaProcessoSeletivo> getMapListaJobProcessoSeletivo() {
		if (mapListaJobProcessoSeletivo == null) {
			mapListaJobProcessoSeletivo = new HashMap<Integer, JobProvaProcessoSeletivo>(0);
		}
		return mapListaJobProcessoSeletivo;
	}
	
	public synchronized void adicionarJobProvaProcessoSeletivo(Integer inscricao ,JobProvaProcessoSeletivo  jobProvaProcessoSeletivo) throws Exception {
		Iterator<JobProvaProcessoSeletivo> iProva =  getMapListaJobProcessoSeletivo().values().iterator();
		while(iProva.hasNext()) {
			JobProvaProcessoSeletivo job = (JobProvaProcessoSeletivo) iProva.next();
			if(job.getCodigoInscricao().equals(inscricao)) {
				iProva.remove();
			}
		}
		getMapListaJobProcessoSeletivo().put(inscricao, jobProvaProcessoSeletivo);
		
	}


	public ConfiguracaoBibliotecaVO getConfiguracaoBibliotecaPadrao() {
		if(configuracaoBibliotecaPadrao == null ) {
			configuracaoBibliotecaPadrao =  new ConfiguracaoBibliotecaVO();
		}
		return configuracaoBibliotecaPadrao;
	}

	public void setConfiguracaoBibliotecaPadrao(ConfiguracaoBibliotecaVO configuracaoBibliotecaPadrao) {
		this.configuracaoBibliotecaPadrao = configuracaoBibliotecaPadrao;
	}


	private Map<Integer, DisciplinaVO> mapDisciplinaVOs  = new HashMap<Integer, DisciplinaVO>(0);
	private Map<Integer, UnidadeEnsinoVO> mapUnidadeEnsinoVOs  = new HashMap<Integer, UnidadeEnsinoVO>(0);
	private Map<Integer, PerfilAcessoVO> mapPerfilAcessoVOs  = new HashMap<Integer, PerfilAcessoVO>(0);
	private Map<Integer, FormaPagamentoVO> mapFormaPagamentoVOs  = new HashMap<Integer, FormaPagamentoVO>(0);
	private Map<Integer, OperadoraCartaoVO> mapOperadoraCartaoVOs  = new HashMap<Integer, OperadoraCartaoVO>(0);
	private Map<Integer, ConfiguracaoFinanceiroCartaoVO> mapConfiguracaoFinanceiroCartaoVOs  = new HashMap<Integer, ConfiguracaoFinanceiroCartaoVO>(0);
	private Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> mapConfiguracaoRecebimentoCartaoOnlineVOs  = new HashMap<Integer, ConfiguracaoRecebimentoCartaoOnlineVO>(0);
	
	public void realizarInclusaoLayoutPadraoHistorico() {
		String caminhoBase;
		try {
			caminhoBase = UteisJSF.getCaminhoWeb().replace("/", File.separator) + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + HistoricoAlunoRel.getCaminhoBaseRelatorio();
			for(TipoNivelEducacional tipoNivelEducacional: TipoNivelEducacional.values()) {
				getFacadeFactory().getConfiguracaoHistoricoFacade().realizarInclusaoLayoutPadraoHistorico(tipoNivelEducacional, caminhoBase);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void realizarInclusaoLayoutPadraoAtaResultadosFinais() {
		String caminhoBase;
		try {
			caminhoBase = UteisJSF.getCaminhoWeb().replace("/", File.separator) + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + AtaResultadosFinaisRel.getCaminhoBaseRelatorio();
				getFacadeFactory().getConfiguracaoAtaResultadosFinaisFacade().realizarInclusaoLayoutPadraoAtaResultadosFinais(caminhoBase);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProgressBarVO getProgressBarImportarCandidatoVO() {
		if (progressBarImportarCandidatoVO == null) {
			progressBarImportarCandidatoVO = new ProgressBarVO();
		}
		return progressBarImportarCandidatoVO;
	}

	public void setProgressBarImportarCandidatoVO(ProgressBarVO progressBarImportarCandidatoVO) {
		this.progressBarImportarCandidatoVO = progressBarImportarCandidatoVO;
	}
	
	public ProgressBarVO getProgressBarMapaRegistroEvasaoCursoVO() {
		if (progressBarMapaRegistroEvasaoCursoVO == null) {
			progressBarMapaRegistroEvasaoCursoVO = new ProgressBarVO();
		}
		return progressBarMapaRegistroEvasaoCursoVO;
	}
	
	public void setProgressBarMapaRegistroEvasaoCursoVO(ProgressBarVO progressBarMapaRegistroEvasaoCursoVO) {
		this.progressBarMapaRegistroEvasaoCursoVO = progressBarMapaRegistroEvasaoCursoVO;
	}
	
	public void atualizarProgressBar() {
		
	}

	public Map<String, Object> getMapObjeto() {
		if (mapObjeto == null) {
			mapObjeto = new HashMap<>();
		}
		return mapObjeto;
	}

	public void setMapObjeto(Map<String, Object> mapObjeto) {
		this.mapObjeto = mapObjeto;
	}

	public ConfiguracaoSeiBlackboardVO getConfiguracaoSeiBlackboardVO() {
		if(configuracaoSeiBlackboardVO == null) {
			configuracaoSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, null);
		}
		return configuracaoSeiBlackboardVO;
	}

	public void setConfiguracaoSeiBlackboardVO(ConfiguracaoSeiBlackboardVO configuracaoSeiBlackboardVO) {
		this.configuracaoSeiBlackboardVO = configuracaoSeiBlackboardVO;
	}
	
	
	public synchronized ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO(Integer codigoConfiguracaoRecebimentoCartaoOnline, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoConfiguracaoRecebimentoCartaoOnline)) {
			if(!mapConfiguracaoRecebimentoCartaoOnlineVOs.containsKey(codigoConfiguracaoRecebimentoCartaoOnline) || !Uteis.isAtributoPreenchido(mapConfiguracaoRecebimentoCartaoOnlineVOs.get(codigoConfiguracaoRecebimentoCartaoOnline))) {
				ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO =  getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarPorChavePrimariaUnica(codigoConfiguracaoRecebimentoCartaoOnline, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapConfiguracaoRecebimentoCartaoOnlineVOs.put(codigoConfiguracaoRecebimentoCartaoOnline, configuracaoRecebimentoCartaoOnlineVO);
			}
			return (ConfiguracaoRecebimentoCartaoOnlineVO) mapConfiguracaoRecebimentoCartaoOnlineVOs.get(codigoConfiguracaoRecebimentoCartaoOnline);
		}else {
			return null;
		}	}
	
	public synchronized void removerConfiguracaoRecebimentoCartaoOnline(Integer codigoConfiguracaoRecebimentoCartaoOnline) throws Exception {
		if(mapConfiguracaoRecebimentoCartaoOnlineVOs.containsKey(codigoConfiguracaoRecebimentoCartaoOnline)) {
			mapConfiguracaoRecebimentoCartaoOnlineVOs.remove(codigoConfiguracaoRecebimentoCartaoOnline);
		}
		if(Uteis.isAtributoPreenchido(codigoConfiguracaoRecebimentoCartaoOnline) && codigoConfiguracaoRecebimentoCartaoOnline == -1) {
			mapConfiguracaoRecebimentoCartaoOnlineVOs.clear();
	}
	}
	
	public synchronized UnidadeEnsinoVO getUnidadeEnsinoVO(Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			if(!mapUnidadeEnsinoVOs.containsKey(codigoUnidadeEnsino) || !Uteis.isAtributoPreenchido(mapUnidadeEnsinoVOs.get(codigoUnidadeEnsino))) {
				UnidadeEnsinoVO unidadeEnsinoVO =  getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimariaUnica(codigoUnidadeEnsino, usuario);
				mapUnidadeEnsinoVOs.put(codigoUnidadeEnsino, unidadeEnsinoVO);
			}
			return (UnidadeEnsinoVO) Uteis.clonar(mapUnidadeEnsinoVOs.get(codigoUnidadeEnsino));
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		if(mapUnidadeEnsinoVOs.containsKey(codigoUnidadeEnsino)) {
			mapUnidadeEnsinoVOs.remove(codigoUnidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino) && codigoUnidadeEnsino == -1) {
			mapUnidadeEnsinoVOs.clear();
	}
	}
	
	public synchronized PerfilAcessoVO getPerfilAcessoVO(Integer codigoPerfilAcesso, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoPerfilAcesso)) {
			if(!mapPerfilAcessoVOs.containsKey(codigoPerfilAcesso) || !Uteis.isAtributoPreenchido(mapPerfilAcessoVOs.get(codigoPerfilAcesso))) {
				PerfilAcessoVO perfilAcessoVO =  getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimariaUnica(codigoPerfilAcesso, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapPerfilAcessoVOs.put(codigoPerfilAcesso, perfilAcessoVO);
			}
			return (PerfilAcessoVO)mapPerfilAcessoVOs.get(codigoPerfilAcesso).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerPerfilAcesso(Integer codigoPerfilAcesso) throws Exception {
		if(mapPerfilAcessoVOs.containsKey(codigoPerfilAcesso)) {
			mapPerfilAcessoVOs.remove(codigoPerfilAcesso);
		}
		if(Uteis.isAtributoPreenchido(codigoPerfilAcesso) && codigoPerfilAcesso == -1) {
			mapPerfilAcessoVOs.clear();
	}
	}

	public synchronized DisciplinaVO getDisciplinaVO(Integer codigoDisciplina, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoDisciplina)) {
			if(!mapDisciplinaVOs.containsKey(codigoDisciplina) || !Uteis.isAtributoPreenchido(mapDisciplinaVOs.get(codigoDisciplina))) {
				DisciplinaVO disciplinaVO =  getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaUnica(codigoDisciplina, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapDisciplinaVOs.put(codigoDisciplina, disciplinaVO);
			}
			return (DisciplinaVO)mapDisciplinaVOs.get(codigoDisciplina).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerDisciplina(Integer codigoDisciplina) throws Exception {
		if(mapDisciplinaVOs.containsKey(codigoDisciplina)) {
			mapDisciplinaVOs.remove(codigoDisciplina);
		}
		if(Uteis.isAtributoPreenchido(codigoDisciplina) && codigoDisciplina == -1) {
			mapDisciplinaVOs.clear();
	}
	}
	
	public synchronized FormaPagamentoVO getFormaPagamentoVO(Integer codigoFormaPagamento, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoFormaPagamento)) {
			if(!mapFormaPagamentoVOs.containsKey(codigoFormaPagamento) || !Uteis.isAtributoPreenchido(mapFormaPagamentoVOs.get(codigoFormaPagamento))) {
				FormaPagamentoVO formaPagamentoVO =  getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimariaUnica(codigoFormaPagamento, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapFormaPagamentoVOs.put(codigoFormaPagamento, formaPagamentoVO);
			}
			return (FormaPagamentoVO)mapFormaPagamentoVOs.get(codigoFormaPagamento).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerFormaPagamento(Integer codigoFormaPagamento) throws Exception {
		if(mapFormaPagamentoVOs.containsKey(codigoFormaPagamento)) {
			mapFormaPagamentoVOs.remove(codigoFormaPagamento);
		}
		if(Uteis.isAtributoPreenchido(codigoFormaPagamento) && codigoFormaPagamento == -1) {
			mapFormaPagamentoVOs.clear();
	}
	}
	
	public synchronized OperadoraCartaoVO getOperadoraCartaoVO(Integer codigoOperadoraCartao, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoOperadoraCartao)) {
			if(!mapOperadoraCartaoVOs.containsKey(codigoOperadoraCartao) || !Uteis.isAtributoPreenchido(mapOperadoraCartaoVOs.get(codigoOperadoraCartao))) {
				OperadoraCartaoVO operadoraCartaoVO =  getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimariaUnica(codigoOperadoraCartao, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapOperadoraCartaoVOs.put(codigoOperadoraCartao, operadoraCartaoVO);
			}
			return (OperadoraCartaoVO)mapOperadoraCartaoVOs.get(codigoOperadoraCartao).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerOperadoraCartao(Integer codigoOperadoraCartao)  {
		if(codigoOperadoraCartao == null || codigoOperadoraCartao == -1) {
			mapOperadoraCartaoVOs.clear();
		}else if(mapOperadoraCartaoVOs.containsKey(codigoOperadoraCartao)) {
			mapOperadoraCartaoVOs.remove(codigoOperadoraCartao);
		}
	}
	
	public synchronized ConfiguracaoFinanceiroCartaoVO getConfiguracaoFinanceiroCartaoVO(Integer codigoCnfiguracaoFinanceiroCartao, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoCnfiguracaoFinanceiroCartao)) {
			if(!mapConfiguracaoFinanceiroCartaoVOs.containsKey(codigoCnfiguracaoFinanceiroCartao) || !Uteis.isAtributoPreenchido(mapConfiguracaoFinanceiroCartaoVOs.get(codigoCnfiguracaoFinanceiroCartao))) {
				ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO =  getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimariaUnica(codigoCnfiguracaoFinanceiroCartao, NivelMontarDados.TODOS, usuario);
				mapConfiguracaoFinanceiroCartaoVOs.put(codigoCnfiguracaoFinanceiroCartao, configuracaoFinanceiroCartaoVO);
			}
			return (ConfiguracaoFinanceiroCartaoVO)mapConfiguracaoFinanceiroCartaoVOs.get(codigoCnfiguracaoFinanceiroCartao).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerConfiguracaoFinanceiroCartao(Integer codigoCnfiguracaoFinanceiroCartao) {
		if(mapConfiguracaoFinanceiroCartaoVOs.containsKey(codigoCnfiguracaoFinanceiroCartao)) {
			mapConfiguracaoFinanceiroCartaoVOs.remove(codigoCnfiguracaoFinanceiroCartao);
		}
		if(Uteis.isAtributoPreenchido(codigoCnfiguracaoFinanceiroCartao) && codigoCnfiguracaoFinanceiroCartao == -1) {
			mapConfiguracaoFinanceiroCartaoVOs.clear();
	}
	}
	
	public ProgressBarVO getProgressBarLinksUteisVO() {
		if (progressBarLinksUteisVO == null) {
			progressBarLinksUteisVO = new ProgressBarVO();
		}
		return progressBarLinksUteisVO;
	}
	
	public void setProgressBarLinksUteisVO(ProgressBarVO progressBarLinksUteisVO) {
		this.progressBarLinksUteisVO = progressBarLinksUteisVO;
	}
	

	private Map<Integer, TurnoVO> mapTurnoVOs  = new HashMap<Integer, TurnoVO>(0);
	
	public synchronized TurnoVO getTurnoVO(Integer codigoTurno, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigoTurno)) {
			if(!mapTurnoVOs.containsKey(codigoTurno) || !Uteis.isAtributoPreenchido(mapTurnoVOs.get(codigoTurno))) {
				TurnoVO turnoVO =  getFacadeFactory().getTurnoFacade().consultarPorChavePrimariaUnico(codigoTurno, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapTurnoVOs.put(codigoTurno, turnoVO);
			}
			return (TurnoVO)mapTurnoVOs.get(codigoTurno).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerTurno(Integer codigoTurno) throws Exception {
		if(mapTurnoVOs.containsKey(codigoTurno)) {
			mapTurnoVOs.remove(codigoTurno);
		}
		if(Uteis.isAtributoPreenchido(codigoTurno) && codigoTurno == -1) {
			mapTurnoVOs.clear();
		}
	}
	
private Map<Integer, TipoDocumentoVO> mapTipoDocumentoVOs  = new HashMap<Integer, TipoDocumentoVO>(0);
	
	public synchronized TipoDocumentoVO getTipoDocumentoVO(Integer codigo, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigo)) {
			if(!mapTipoDocumentoVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapTipoDocumentoVOs.get(codigo))) {
				TipoDocumentoVO tipoDocumentoVO =  getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimariaUnico(codigo, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapTipoDocumentoVOs.put(codigo, tipoDocumentoVO);
			}
			return (TipoDocumentoVO)mapTipoDocumentoVOs.get(codigo).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerTipoDocumento(Integer codigo) throws Exception {
		if(mapTipoDocumentoVOs.containsKey(codigo)) {
			mapTipoDocumentoVOs.remove(codigo);
		}		
		if(Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapTipoDocumentoVOs.clear();
		}
	}
	
	private Map<Integer, TipoAtividadeComplementarVO> mapTipoAtividadeComplementarVOs  = new HashMap<Integer, TipoAtividadeComplementarVO>(0);
	
	public synchronized TipoAtividadeComplementarVO getTipoAtividadeComplementarVO(Integer codigo, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigo)) {
			if(!mapTipoAtividadeComplementarVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapTipoAtividadeComplementarVOs.get(codigo))) {
				TipoAtividadeComplementarVO TipoAtividadeComplementarVO =  getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorChavePrimariaUnico(codigo, usuario);
				mapTipoAtividadeComplementarVOs.put(codigo, TipoAtividadeComplementarVO);
			}
			return (TipoAtividadeComplementarVO)mapTipoAtividadeComplementarVOs.get(codigo).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerTipoAtividadeComplementar(Integer codigo) throws Exception {
		if(mapTipoAtividadeComplementarVOs.containsKey(codigo)) {
			mapTipoAtividadeComplementarVOs.remove(codigo);
		}
		if(Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapTipoAtividadeComplementarVOs.clear();
		}
	}
	
	private Map<Integer, CategoriaGEDVO> mapCategoriaGEDVOs  = new HashMap<Integer, CategoriaGEDVO>(0);
	
	public synchronized CategoriaGEDVO getCategoriaGEDVO(Integer codigo, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(codigo)) {
			if(!mapCategoriaGEDVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapCategoriaGEDVOs.get(codigo))) {
				CategoriaGEDVO CategoriaGEDVO =  getFacadeFactory().getCategoriaGEDInterfaceFacade().consultarPorChavePrimariaUnico(codigo);
				mapCategoriaGEDVOs.put(codigo, CategoriaGEDVO);
			}
			return (CategoriaGEDVO)mapCategoriaGEDVOs.get(codigo).clone();
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerCategoriaGED(Integer codigo) throws Exception {
		if(mapCategoriaGEDVOs.containsKey(codigo)) {
			mapCategoriaGEDVOs.remove(codigo);
		}
		if(Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapCategoriaGEDVOs.clear();
		}
	}

	public synchronized Map<String, String> getArtefatoAjudaKeys() {
		if(artefatoAjudaKeys == null) {
			artefatoAjudaKeys = getFacadeFactory().getArtefatoAjudaFacade().consultarArtefatoAjudaDisponivel();
		}
		return artefatoAjudaKeys;
	}

	public synchronized void setArtefatoAjudaKeys(Map<String, String> artefatoAjudaKeys) {
		this.artefatoAjudaKeys = artefatoAjudaKeys;
	}
	

	public List<TurmaVO> obterAdicionarRemoverTurmaOfertada(String key, List<TurmaVO> listaTurmaOfertada, boolean add, boolean remover) {
		if(add) {
			getMapTurmasOfertadas().put(key, listaTurmaOfertada);
			return listaTurmaOfertada;
		}else if(remover) {
			getMapTurmasOfertadas().clear();
			return null;
		}else {
			if(getMapTurmasOfertadas().containsKey(key)) {
				return getMapTurmasOfertadas().get(key);
			}
			return null;
		}
	}
	
	public ProgressBarVO getProgressBarImportarCid() {
		if (progressBarImportarCid == null) {
			progressBarImportarCid = new ProgressBarVO();
		}
		return progressBarImportarCid;
	}

	public void setProgressBarImportarCid(ProgressBarVO progressBarImportarCid) {
		this.progressBarImportarCid = progressBarImportarCid;
	}
	
	public ProgressBarVO getProgressBarFechamentoNota() {
		if (progressBarFechamentoNota == null) {
			progressBarFechamentoNota = new ProgressBarVO();
		}
		return progressBarFechamentoNota;
	}
	
	public void setProgressBarFechamentoNota(ProgressBarVO progressBarFechamentoNota) {
		this.progressBarFechamentoNota = progressBarFechamentoNota;
	}
	
	public ProgressBarVO getProgressBarAssinarXmlMec() {
		if (progressBarAssinarXmlMec == null) {
			progressBarAssinarXmlMec = new ProgressBarVO();
		}
		return progressBarAssinarXmlMec;
	}
	
	public void setProgressBarAssinarXmlMec(ProgressBarVO progressBarAssinarXmlMec) {
		this.progressBarAssinarXmlMec = progressBarAssinarXmlMec;
	}
	
	public Map<Integer, DocumentoAssinadoVO> getMapDocumentoAssinadoAssinarXml() {
		return mapDocumentoAssinadoAssinarXml;
	}
	
	public void setMapDocumentoAssinadoAssinarXml(Map<Integer, DocumentoAssinadoVO> mapDocumentoAssinadoAssinarXml) {
		this.mapDocumentoAssinadoAssinarXml = mapDocumentoAssinadoAssinarXml;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoAssinarXml(Integer codigoDocumentoAssinado) {
		if (Uteis.isAtributoPreenchido(codigoDocumentoAssinado) && Uteis.isAtributoPreenchido(getMapDocumentoAssinadoAssinarXml()) && getMapDocumentoAssinadoAssinarXml().containsKey(codigoDocumentoAssinado)) {
			return (DocumentoAssinadoVO) Uteis.clonar(getMapDocumentoAssinadoAssinarXml().get(codigoDocumentoAssinado));
		}
		return null;
	}
	
	public void removerDocumentoAssinadoAssinarXml(Integer codigoDocumentoAssinado) {
		if (Uteis.isAtributoPreenchido(codigoDocumentoAssinado) && Uteis.isAtributoPreenchido(getMapDocumentoAssinadoAssinarXml()) && getMapDocumentoAssinadoAssinarXml().containsKey(codigoDocumentoAssinado)) {
			getMapDocumentoAssinadoAssinarXml().remove(codigoDocumentoAssinado);
		}
	}
	
	public void limparAplicacaoMapDocumentoAssinarXml() {
		if (Uteis.isAtributoPreenchido(getMapDocumentoAssinadoAssinarXml())) {
			setMapDocumentoAssinadoAssinarXml(null);
		}
	}
	
	public void adicionarListaDocumentoAssinadoAssinarXmlEmAplicacao(List<DocumentoAssinadoVO> documentoAssinadoVOs) {
		if (Uteis.isAtributoPreenchido(documentoAssinadoVOs)) {
			if (Objects.isNull(getMapDocumentoAssinadoAssinarXml())) {
				setMapDocumentoAssinadoAssinarXml(new HashMap<Integer, DocumentoAssinadoVO>());
			}
			documentoAssinadoVOs.stream().filter(documentoAssinado -> Uteis.isAtributoPreenchido(documentoAssinado)).forEach(documentoAssinado -> getMapDocumentoAssinadoAssinarXml().put(documentoAssinado.getCodigo(), (DocumentoAssinadoVO) Uteis.clonar(documentoAssinado)));
		}
	}
	
	public Boolean getAuthenticationLacunaGenerated() {
		if (authenticationLacunaGenerated == null) {
			authenticationLacunaGenerated = Boolean.FALSE;
		}
		return authenticationLacunaGenerated;
	}
	
	public void setAuthenticationLacunaGenerated(Boolean authenticationLacunaGenerated) {
		this.authenticationLacunaGenerated = authenticationLacunaGenerated;
	}
	
	public Map<Integer, ConfiguracaoDiplomaDigitalVO> getMapConfiguracaoDiplomaDigital() {
		if (mapConfiguracaoDiplomaDigital == null) {
			mapConfiguracaoDiplomaDigital = new HashMap<>(0);
		}
		return mapConfiguracaoDiplomaDigital;
	}
	
	public void setMapConfiguracaoDiplomaDigital(Map<Integer, ConfiguracaoDiplomaDigitalVO> mapConfiguracaoDiplomaDigital) {
		this.mapConfiguracaoDiplomaDigital = mapConfiguracaoDiplomaDigital;
	}
	
	public synchronized ConfiguracaoDiplomaDigitalVO getConfiguracaoDiplomaDigitalVO(Integer codigoConfiguracaoDiplomaDigital, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(codigoConfiguracaoDiplomaDigital)) {
			if (!getMapConfiguracaoDiplomaDigital().containsKey(codigoConfiguracaoDiplomaDigital) || !Uteis.isAtributoPreenchido(getMapConfiguracaoDiplomaDigital().get(codigoConfiguracaoDiplomaDigital))) {
				ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigitalVO = new ConfiguracaoDiplomaDigitalVO();
				configuracaoDiplomaDigitalVO.setCodigo(codigoConfiguracaoDiplomaDigital);
				getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().carregarDados(configuracaoDiplomaDigitalVO, usuario);
				getMapConfiguracaoDiplomaDigital().put(codigoConfiguracaoDiplomaDigital, configuracaoDiplomaDigitalVO);
			}
			return (ConfiguracaoDiplomaDigitalVO) Uteis.clonar(getMapConfiguracaoDiplomaDigital().get(codigoConfiguracaoDiplomaDigital));
		} else {
			return null;
		}
	}
	
	public synchronized void removerConfiguracaoDiploma(Integer codigoConfiguracaoDiplomaDigital) throws Exception {
		if(getMapConfiguracaoDiplomaDigital().containsKey(codigoConfiguracaoDiplomaDigital)) {
			getMapConfiguracaoDiplomaDigital().remove(codigoConfiguracaoDiplomaDigital);
		}
		if(Uteis.isAtributoPreenchido(codigoConfiguracaoDiplomaDigital) && codigoConfiguracaoDiplomaDigital == -1) {
			getMapConfiguracaoDiplomaDigital().clear();
		}
	}

	public ProgressBarVO getProgressBarIntegracaoMestreGR() {
		if (progressBarIntegracaoMestreGR == null) {
			progressBarIntegracaoMestreGR = new ProgressBarVO();
		}
		return progressBarIntegracaoMestreGR;
	}

	public void setProgressBarIntegracaoMestreGR(ProgressBarVO progressBarIntegracaoMestreGR) {
		this.progressBarIntegracaoMestreGR = progressBarIntegracaoMestreGR;
	}

	public synchronized OfertaDisciplinaVO ofertaDisciplina(String key, OfertaDisciplinaVO ofertaDisciplinaVO, int acao) {
		if(acao == Uteis.CONSULTAR) {
			if(getMapOfertaDisciplina().containsKey(key)) {
				return getMapOfertaDisciplina().get(key);
			}			
			return null;
		}else if(acao == Uteis.INCLUIR){
			if(!getMapOfertaDisciplina().containsKey(key)) {
				 getMapOfertaDisciplina().put(key, ofertaDisciplinaVO);
			}			
			return ofertaDisciplinaVO;
		}else if(acao == Uteis.EXCLUIR){
			if(key == null) {
				getMapOfertaDisciplina().clear();
			}
			if(getMapOfertaDisciplina().containsKey(key)) {
				 getMapOfertaDisciplina().remove(key, ofertaDisciplinaVO);
			}
		}
		return null;
		
	}
	
	private Map<String, OfertaDisciplinaVO> getMapOfertaDisciplina() {
		if(mapOfertaDisciplina == null) {
			mapOfertaDisciplina = new HashMap<String, OfertaDisciplinaVO>(0);
		}
		return mapOfertaDisciplina;
	}

	private void setMapOfertaDisciplina(Map<String, OfertaDisciplinaVO> mapOfertaDisciplina) {
		this.mapOfertaDisciplina = mapOfertaDisciplina;
	}

	public synchronized void removerFoldersDocumentosTechCert(String key) {
		// limpeza total
		if (key != null && key.equals("-1")) {
			foldersCache.clear();
			anoCache.clear();
			mesCache.clear();
		}
	}

	public ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> getMesCache() {
		if (mesCache == null) {
			mesCache = new ConcurrentHashMap<>(0);
		}
		return mesCache;
	}

	public void setMesCache(ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> mesCache) {
		this.mesCache = mesCache;
	}

	public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getAnoCache() {
		if (anoCache == null) {
			anoCache = new ConcurrentHashMap<>(0);
		}
		return anoCache;
	}

	public void setAnoCache(ConcurrentHashMap<String, ConcurrentHashMap<String, String>> anoCache) {
		this.anoCache = anoCache;
	}

	public ConcurrentHashMap<String, String> getFoldersCache() {
		if (foldersCache == null) {
			foldersCache = new ConcurrentHashMap<>(0);
		}
		return foldersCache;
	}

	public void setFoldersCache(ConcurrentHashMap<String, String> foldersCache) {
		this.foldersCache = foldersCache;
	}

	public ConcurrentHashMap<Integer, List<MatriculaVO>> getMatriculasAlunoCache() {
		if (matriculasAlunoCache == null) {
			matriculasAlunoCache = new ConcurrentHashMap<>(0);
		}
		return matriculasAlunoCache;
	}

	public void setMatriculasAlunoCache(ConcurrentHashMap<Integer, List<MatriculaVO>> matriculasAlunoCache) {
		this.matriculasAlunoCache = matriculasAlunoCache;
	}

	public ConcurrentHashMap<String, MatriculaVO> getMatriculaAlunoCache() {
		if (matriculaAlunoCache == null) {
			matriculaAlunoCache = new ConcurrentHashMap<>(0);
		}
		return matriculaAlunoCache;
	}

	public void setMatriculaAlunoCache(ConcurrentHashMap<String, MatriculaVO> matriculaAlunoCache) {
		this.matriculaAlunoCache = matriculaAlunoCache;
	}

	public ConcurrentHashMap<String, MatriculaPeriodoVO> getMatriculaPeriodoAlunoCache() {
		if (matriculaPeriodoAlunoCache == null) {
			matriculaPeriodoAlunoCache = new ConcurrentHashMap<>(0);
		}
		return matriculaPeriodoAlunoCache;
	}

	public void setMatriculaPeriodoAlunoCache(ConcurrentHashMap<String, MatriculaPeriodoVO> matriculaPeriodoAlunoCache) {
		this.matriculaPeriodoAlunoCache = matriculaPeriodoAlunoCache;
	}

	public ConcurrentHashMap<String, MatriculaIntegralizacaoCurricularVO> getMatriculaIntegralizacaoCurricularCache() {
		if (matriculaIntegralizacaoCurricularCache == null) {
			matriculaIntegralizacaoCurricularCache = new ConcurrentHashMap<>(0);
		}
		return matriculaIntegralizacaoCurricularCache;
	}

	public void setMatriculaIntegralizacaoCurricularCache(
			ConcurrentHashMap<String, MatriculaIntegralizacaoCurricularVO> matriculaIntegralizacaoCurricularCache) {
		this.matriculaIntegralizacaoCurricularCache = matriculaIntegralizacaoCurricularCache;
	}


	public ConcurrentHashMap<String, List<String>> getAnoSemestreMatriculaCache() {
		if (anoSemestreMatriculaCache == null) {
			anoSemestreMatriculaCache = new ConcurrentHashMap<>(0);
		}
		return anoSemestreMatriculaCache;
	}

	public void setAnoSemestreMatriculaCache(ConcurrentHashMap<String, List<String>> anoSemestreMatriculaCache) {
		this.anoSemestreMatriculaCache = anoSemestreMatriculaCache;
	}

	public ConcurrentHashMap<String, List<MatriculaPeriodoTurmaDisciplinaVO>> getMatriculaPeriodoTurmaDisciplinasCache() {
		if (matriculaPeriodoTurmaDisciplinasCache == null) {
			matriculaPeriodoTurmaDisciplinasCache = new ConcurrentHashMap<>(0);
		}
		return matriculaPeriodoTurmaDisciplinasCache;
	}

	public void setMatriculaPeriodoTurmaDisciplinasCache(
			ConcurrentHashMap<String, List<MatriculaPeriodoTurmaDisciplinaVO>> matriculaPeriodoTurmaDisciplinaCache) {
		this.matriculaPeriodoTurmaDisciplinasCache = matriculaPeriodoTurmaDisciplinaCache;
	}

	public ConcurrentHashMap<String, CalendarioAgrupamentoTccVO> getCalendarioProjetoIntegradorCache() {
		if (calendarioProjetoIntegradorCache == null) {
			calendarioProjetoIntegradorCache = new ConcurrentHashMap<>(0);
		}
		return calendarioProjetoIntegradorCache;
	}

	public void setCalendarioProjetoIntegradorCache(
			ConcurrentHashMap<String, CalendarioAgrupamentoTccVO> calendarioProjetoIntegradorCache) {
		this.calendarioProjetoIntegradorCache = calendarioProjetoIntegradorCache;
	}

	public ConcurrentHashMap<String, CalendarioAgrupamentoTccVO> getCalendarioTccCache() {
		if (calendarioTccCache == null) {
			calendarioTccCache = new ConcurrentHashMap<>(0);
		}
		return calendarioTccCache;
	}

	public void setCalendarioTccCache(ConcurrentHashMap<String, CalendarioAgrupamentoTccVO> calendarioTccCache) {
		this.calendarioTccCache = calendarioTccCache;
	}

	public ConcurrentHashMap<String, UsuarioVO> getUsuarioAlunoPorEmailCache() {
		if (usuarioAlunoPorEmailCache == null) {
			usuarioAlunoPorEmailCache = new ConcurrentHashMap<>(0);
		}
		return usuarioAlunoPorEmailCache;
	}

	public void setUsuarioAlunoPorEmailCache(ConcurrentHashMap<String, UsuarioVO> usuarioAlunoPorEmailCache) {
		this.usuarioAlunoPorEmailCache = usuarioAlunoPorEmailCache;
	}

	public ConcurrentHashMap<String, UsuarioVO> getUsuarioAlunoPorUsernameSenhaCache() {
		if (usuarioAlunoPorUsernameSenhaCache == null) {
			usuarioAlunoPorUsernameSenhaCache = new ConcurrentHashMap<>(0);
		}
		return usuarioAlunoPorUsernameSenhaCache;
	}

	public void setUsuarioAlunoPorUsernameSenhaCache(
			ConcurrentHashMap<String, UsuarioVO> usuarioAlunoPorUsernameSenhaCache) {
		this.usuarioAlunoPorUsernameSenhaCache = usuarioAlunoPorUsernameSenhaCache;
	}

	public ConcurrentHashMap<String, List<AvaliacaoInstitucionalVO>> getMatriculaAvaliacaoInstitucionalCache() {
		if (matriculaAvaliacaoInstitucionalCache == null) {
			matriculaAvaliacaoInstitucionalCache = new ConcurrentHashMap<>(0);
		}
		return matriculaAvaliacaoInstitucionalCache;
	}

	public void setMatriculaAvaliacaoInstitucionalCache(
			ConcurrentHashMap<String, List<AvaliacaoInstitucionalVO>> matriculaAvaliacaoInstitucionalCache) {
		this.matriculaAvaliacaoInstitucionalCache = matriculaAvaliacaoInstitucionalCache;
	}

	public ConcurrentHashMap<Integer, PreferenciaSistemaUsuarioVO> getPreferenciaSistemaUsuarioCache() {
		if (preferenciaSistemaUsuarioCache == null) {
			preferenciaSistemaUsuarioCache = new ConcurrentHashMap<>(0);
		}
		return preferenciaSistemaUsuarioCache;
	}

	public void setPreferenciaSistemaUsuarioCache(
			ConcurrentHashMap<Integer, PreferenciaSistemaUsuarioVO> preferenciaSistemaUsuarioCache) {
		this.preferenciaSistemaUsuarioCache = preferenciaSistemaUsuarioCache;
	}

	public synchronized ConfiguracaoEstagioObrigatorioVO getConfiguracaoEstagioObrigatorioPadraoVO() {
		if(configuracaoEstagioObrigatorioPadraoVO == null) {
			configuracaoEstagioObrigatorioPadraoVO =  getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadraoUnico(false, Uteis.NIVELMONTARDADOS_TODOS, null);
		}
		return configuracaoEstagioObrigatorioPadraoVO;
	}

	public void setConfiguracaoEstagioObrigatorioPadraoVO(
			ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioPadraoVO) {
		this.configuracaoEstagioObrigatorioPadraoVO = configuracaoEstagioObrigatorioPadraoVO;
	}

	public ConcurrentHashMap<String, Map<Integer, Integer>> getMatriculaEstagioDeferidoCache() {
		if (matriculaEstagioDeferidoCache == null) {
			matriculaEstagioDeferidoCache = new ConcurrentHashMap<>(0);
		}
		return matriculaEstagioDeferidoCache;
	}

	public void setMatriculaEstagioDeferidoCache(
			ConcurrentHashMap<String, Map<Integer, Integer>> matriculaEstagioDeferidoCache) {
		this.matriculaEstagioDeferidoCache = matriculaEstagioDeferidoCache;
	}

	public ConcurrentHashMap<Integer, List<LinksUteisVO>> getLinksUteisUsuarioCache() {
			if (linksUteisUsuarioCache == null) {
				linksUteisUsuarioCache = new ConcurrentHashMap<>(0);
			}
		return linksUteisUsuarioCache;
	}

	public void setLinksUteisUsuarioCache(ConcurrentHashMap<Integer, List<LinksUteisVO>> linksUteisUsuarioCache) {
		this.linksUteisUsuarioCache = linksUteisUsuarioCache;
	}

	public ConcurrentHashMap<String, DashboardEstagioVO> getDashboardEstagioFacilitadorCache() {
		if (dashboardEstagioFacilitadorCache == null) {
			dashboardEstagioFacilitadorCache = new ConcurrentHashMap<>(0);
		}
		return dashboardEstagioFacilitadorCache;
	}

	public void setDashboardEstagioFacilitadorCache(
			ConcurrentHashMap<String, DashboardEstagioVO> dashboardEstagioFacilitadorCache) {
		this.dashboardEstagioFacilitadorCache = dashboardEstagioFacilitadorCache;
	}

	public ConcurrentHashMap<String, DashboardEstagioVO> getDashboardEstagioAlunoCache() {
		if (dashboardEstagioAlunoCache == null) {
			dashboardEstagioAlunoCache = new ConcurrentHashMap<>(0);
		}
		return dashboardEstagioAlunoCache;
	}

	public void setDashboardEstagioAlunoCache(ConcurrentHashMap<String, DashboardEstagioVO> dashboardEstagioAlunoCache) {
		this.dashboardEstagioAlunoCache = dashboardEstagioAlunoCache;
	}

	public ConcurrentHashMap<Integer, List<DashboardVO>> getDashboardsAlunoCache() {
		if (dashboardsAlunoCache == null) {
			dashboardsAlunoCache = new ConcurrentHashMap<>(0);
		}
		return dashboardsAlunoCache;
	}

	public void setDashboardsAlunoCache(ConcurrentHashMap<Integer, List<DashboardVO>> dashboardsAlunoCache) {
		this.dashboardsAlunoCache = dashboardsAlunoCache;
	}

	
	public void consultarConfiguracaoAparenciaSistema() {
		try {
			configuracaoAparenciaSistemaVOs = getFacadeFactory().getConfiguracaoAparenciaSistemaFacade()
					.consultarAparenciaDisponibilizadoUsuario(NivelMontarDados.COMBOBOX, false, null);
			if (!configuracaoAparenciaSistemaVOs.stream().anyMatch(p -> p.getCodigo()
					.equals(getConfiguracaoAparenciaSistemaVO().getCodigo()))) {
				configuracaoAparenciaSistemaVOs.add(0, (ConfiguracaoAparenciaSistemaVO) getConfiguracaoAparenciaSistemaVO().clone());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public synchronized List<ConfiguracaoAparenciaSistemaVO> getConfiguracaoAparenciaSistemaVOs() {
		if (configuracaoAparenciaSistemaVOs == null) {
			consultarConfiguracaoAparenciaSistema();
		}
		return configuracaoAparenciaSistemaVOs;
	}

	public void setConfiguracaoAparenciaSistemaVOs(
			List<ConfiguracaoAparenciaSistemaVO> configuracaoAparenciaSistemaVOs) {
		this.configuracaoAparenciaSistemaVOs = configuracaoAparenciaSistemaVOs;
	}
	

	public synchronized ConfiguracaoGEDVO getConfiguracaoGEDVO(Integer codigo) throws Exception{
		if(Uteis.isAtributoPreenchido(codigo)) {
			if(!mapConfiguracaoGEDVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapConfiguracaoGEDVOs.get(codigo))  					
					|| !mapConfiguracaoGEDVOs.get(codigo).getCodigo().equals(codigo)) {
				if(mapConfiguracaoGEDVOs.containsKey(codigo)) {
					mapConfiguracaoGEDVOs.remove(codigo);
				}
				ConfiguracaoGEDVO configuracaoGEDVO = new ConfiguracaoGEDVO();
				configuracaoGEDVO.setCodigo(codigo);
				getFacadeFactory().getConfiguracaoGEDFacade().carregarDados(configuracaoGEDVO, NivelMontarDados.TODOS, null);
				mapConfiguracaoGEDVOs.put(codigo, configuracaoGEDVO);
			}
			return mapConfiguracaoGEDVOs.get(codigo);
		}else {
			return null;
		}
	
	}
	
	public synchronized void removerPaizVO(Integer codigo) throws Exception{
		if(mapPaisVOs.containsKey(codigo)) {
			mapPaisVOs.remove(codigo);
		}
	}
	public synchronized PaizVO getPaizVO(Integer codigo) throws Exception{
		if(Uteis.isAtributoPreenchido(codigo)) {
			if(!mapPaisVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapPaisVOs.get(codigo))  					
					|| !mapPaisVOs.get(codigo).getCodigo().equals(codigo)) {
				if(mapPaisVOs.containsKey(codigo)) {
					mapPaisVOs.remove(codigo);
				}
				
				mapPaisVOs.put(codigo, getFacadeFactory().getPaizFacade().consultarPorChavePrimariaUnico(codigo, false, null));
			}
			return mapPaisVOs.get(codigo);
		}else {
			return null;
		}
	
	}
	public synchronized void removerEstadoVO(Integer codigo) throws Exception{
		if(mapEstadoVOs.containsKey(codigo)) {
			mapEstadoVOs.remove(codigo);
		}
	}
	public synchronized EstadoVO getEstadoVO(Integer codigo) throws Exception{
		if(Uteis.isAtributoPreenchido(codigo)) {
			if(!mapEstadoVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapEstadoVOs.get(codigo))  					
					|| !mapEstadoVOs.get(codigo).getCodigo().equals(codigo)) {
				if(mapEstadoVOs.containsKey(codigo)) {
					mapEstadoVOs.remove(codigo);
				}
				
				mapEstadoVOs.put(codigo, getFacadeFactory().getEstadoFacade().consultarPorChavePrimariaUnico(codigo, Uteis.NIVELMONTARDADOS_TODOS, null));
			}
			return mapEstadoVOs.get(codigo);
		}else {
			return null;
		}
	
	}
	public synchronized void removerAreaConhecimentoVO(Integer codigo) throws Exception{
		if(mapAreaConhecimentoVOs.containsKey(codigo)) {
			mapAreaConhecimentoVOs.remove(codigo);
		}
	}
	public synchronized AreaConhecimentoVO getAreaConhecimentoVO(Integer codigo) throws Exception{
		if(Uteis.isAtributoPreenchido(codigo)) {
			if(!mapAreaConhecimentoVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapAreaConhecimentoVOs.get(codigo))  					
					|| !mapAreaConhecimentoVOs.get(codigo).getCodigo().equals(codigo)) {
				if(mapAreaConhecimentoVOs.containsKey(codigo)) {
					mapAreaConhecimentoVOs.remove(codigo);
				}
				
				mapAreaConhecimentoVOs.put(codigo, getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimariaUnico(codigo,  null));
			}
			return mapAreaConhecimentoVOs.get(codigo);
		}else {
			return null;
		}
		
	}
	
	public synchronized void removerGradeCurricularVO(Integer codigo) throws Exception{
		if(mapGradeCurricularVOs.containsKey(codigo)) {
			mapGradeCurricularVOs.remove(codigo);
		}
	}
	public synchronized GradeCurricularVO getGradeCurricularVO(Integer codigo) throws Exception{
		if(Uteis.isAtributoPreenchido(codigo)) {
			if(!mapGradeCurricularVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapGradeCurricularVOs.get(codigo))  					
					|| !mapGradeCurricularVOs.get(codigo).getCodigo().equals(codigo)) {
				if(mapGradeCurricularVOs.containsKey(codigo)) {
					mapGradeCurricularVOs.remove(codigo);
				}
				
				mapGradeCurricularVOs.put(codigo, getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimariaUnica(codigo, Uteis.NIVELMONTARDADOS_TODOS,  null));
			}
			return (GradeCurricularVO)Uteis.clonar(mapGradeCurricularVOs.get(codigo));
		}else {
			return null;
		}
		
	}

	public LocalDateTime getDataUltimaAtualizacaoAssinaturaXml() {
		return dataUltimaAtualizacaoAssinaturaXml;
	}

	public void setDataUltimaAtualizacaoAssinaturaXml(LocalDateTime dataUltimaAtualizacaoAssinaturaXml) {
		this.dataUltimaAtualizacaoAssinaturaXml = dataUltimaAtualizacaoAssinaturaXml;
	}
	
	public boolean isPeriodoAssinaturaXmlExcedido() {
		return getDataUltimaAtualizacaoAssinaturaXml() != null && Duration.between(getDataUltimaAtualizacaoAssinaturaXml(), LocalDateTime.now()).toMinutes() >= 10;
	}
}
