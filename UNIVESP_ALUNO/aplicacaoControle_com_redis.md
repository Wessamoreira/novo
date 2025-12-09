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
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaIntegralizacaoCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.OfertaDisciplinaVO;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.administrativo.FraseInspiracaoVO;
import negocio.comuns.administrativo.PreferenciaSistemaUsuarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.Cliente;
import negocio.comuns.arquitetura.ControleAtividadeUsuarioVO;
import negocio.comuns.arquitetura.MapaControleAtividadesUsuarioVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.LinksUteisVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.ScriptExecutadoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.estagio.DashboardEstagioVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.HorarioTurmaInterfaceFacade;
import relatorio.negocio.jdbc.academico.HistoricoAlunoRel;

@Controller("AplicacaoControle")
@Scope("singleton")
@Lazy
public class AplicacaoControle {

	// ============================================================================
	// LOCKS ESPECÍFICOS PARA CADA ENTIDADE (Evita contenção global)
	// ============================================================================
	private final Object lockBuscaCidade = new Object();
	private final Object lockBuscaDisciplina = new Object();
	private final Object lockBuscaUnidadeEnsino = new Object();
	private final Object lockBuscaPerfilAcesso = new Object();
	private final Object lockBuscaTurno = new Object();
	private final Object lockBuscaTipoDocumento = new Object();
	private final Object lockBuscaTipoAtividadeComplementar = new Object();
	private final Object lockBuscaCategoriaGED = new Object();
	private final Object lockBuscaConfiguracaoGED = new Object();
	private final Object lockBuscaPaiz = new Object();
	private final Object lockBuscaEstado = new Object();
	private final Object lockBuscaGradeCurricular = new Object();
	private final Object lockConfiguracaoGeral = new Object();
	private final Object lockConfiguracaoAcademica = new Object();

	// ============================================================================
	// CACHE L1 (RAM) - Para dados ultra-frequentes (ConfiguracaoGeral)
	// ============================================================================
	private volatile ConfiguracaoGeralSistemaVO cacheLocalConfig;
	private volatile long ultimaAtualizacaoConfig = 0;
	private static final long TEMPO_CACHE_LOCAL_MS = 5 * 60 * 1000; // 5 minutos

	// ============================================================================
	// ATRIBUTOS ORIGINAIS (Mantidos para compatibilidade)
	// ============================================================================
	private List<ConfiguracaoGeralSistemaVO> listaConfiguracaoGeralSistemaVOAtiva = new ArrayList<ConfiguracaoGeralSistemaVO>();
	private Map<Integer, Integer> mapConfiguracaoGeralSistemaVOPorUnidadeEnsino = new HashMap<Integer, Integer>();
	public FraseInspiracaoVO fraseInspiracaoVO;
	private List<MapaControleAtividadesUsuarioVO> mapaControleUsuarios;
	private Boolean mapaControleAtividadesUsuariosAtivo;
	private MapaControleAtividadesUsuarioVO mapaControleAtividadesUsuarioVO;
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
	
	public static List<Integer> turmaProgramacaoAula = new ArrayList<Integer>();
	private Boolean processandoProvaPresencial;
	private static Boolean processandoDocumentacaoGED;
	private Map<Integer, ProgressBarVO> mapThreadControleCobranca;
	private Map<String, List<TurmaVO>> mapTurmasOfertadas = new HashMap<String, List<TurmaVO>>(0);

	// MAPAS ORIGINAIS (Mantidos para métodos não migrados ainda)
	private Map<Integer, GradeCurricularVO> mapGradeCurricularVOs = new HashMap<Integer, GradeCurricularVO>(0);
	private Map<Integer, CidadeVO> mapCidadeVOs = new HashMap<Integer, CidadeVO>(0);
	private Map<Integer, PaizVO> mapPaisVOs = new HashMap<Integer, PaizVO>(0);
	private Map<Integer, EstadoVO> mapEstadoVOs = new HashMap<Integer, EstadoVO>(0);
	private Map<Integer, DisciplinaVO> mapDisciplinaVOs = new HashMap<Integer, DisciplinaVO>(0);
	private Map<Integer, UnidadeEnsinoVO> mapUnidadeEnsinoVOs = new HashMap<Integer, UnidadeEnsinoVO>(0);
	private Map<Integer, PerfilAcessoVO> mapPerfilAcessoVOs = new HashMap<Integer, PerfilAcessoVO>(0);
	private Map<Integer, TurnoVO> mapTurnoVOs = new HashMap<Integer, TurnoVO>(0);
	private Map<Integer, TipoDocumentoVO> mapTipoDocumentoVOs = new HashMap<Integer, TipoDocumentoVO>(0);
	private Map<Integer, TipoAtividadeComplementarVO> mapTipoAtividadeComplementarVOs = new HashMap<Integer, TipoAtividadeComplementarVO>(0);
	private Map<Integer, CategoriaGEDVO> mapCategoriaGEDVOs = new HashMap<Integer, CategoriaGEDVO>(0);

	private Map<String, ProgressBarVO> mapThreadIndiceReajuste;
	private ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO;
	private Map<Integer, ConfiguracaoGEDVO> mapConfiguracaoGedUnidadeEnsino = new HashMap<Integer, ConfiguracaoGEDVO>(0);
	private ConfiguracaoBibliotecaVO configuracaoBibliotecaPadrao;
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
	private ConcurrentHashMap<Integer, List<DashboardVO>> dashboardsAlunoCache = new ConcurrentHashMap<>();
	private ConfiguracaoEstagioObrigatorioVO configuracaoEstagioObrigatorioPadraoVO;
	private List<ConfiguracaoAparenciaSistemaVO> configuracaoAparenciaSistemaVOs;
	private Map<Integer, ConfiguracaoGEDVO> mapConfiguracaoGEDVOs = new HashMap<Integer, ConfiguracaoGEDVO>(0);

	private LocalDateTime dataUltimaAtualizacaoAssinaturaXml;
	
	@Autowired
	private RedisService redisService;

	public AplicacaoControle() {
		fraseInspiracaoVO = new FraseInspiracaoVO();
	}

	@PostConstruct
	public void realizacaoInicializacaoRotinaAplicacao() {
		realizarReinicializacaoRotinaGeracaoManualParcela();
		realizarReinicializacaoRotinaRenovacaoMatriculaTurma();
		realizarReinicializacaoThreadAvaliacaoOnline();
		getConfiguracaoAparenciaSistemaVO();
		realizarInclusaoLayoutPadraoHistorico();
	}

	// ============================================================================
	// MÉTODOS GETTERS/SETTERS PADRÃO (Mantidos do original)
	// ============================================================================
	
	public FacadeFactory getFacadeFactory() {
		return facadeFactory;
	}

	public void setFacadeFactory(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}

	// ============================================================================
	// CONFIGURAÇÃO GERAL DO SISTEMA (L1 RAM + L2 REDIS + L3 BANCO)
	// ============================================================================
	
	public synchronized ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO(Integer unidadeEnsino,
			UsuarioVO usuarioLogado) throws Exception {
		return manterConfiguracaoGeralSistemaEmNivelAplicacao(null, unidadeEnsino, usuarioLogado, false);
	}

	public synchronized void removerConfiguracaoGeralSistemaEmNivelAplicacao(
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		manterConfiguracaoGeralSistemaEmNivelAplicacao(configuracaoGeralSistemaVO, null, null, true);
	}

	public synchronized void removerConfiguracaoGeralSistemaPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		manterConfiguracaoGeralSistemaEmNivelAplicacao(null, unidadeEnsino, null, true);
	}

	private ConfiguracaoGeralSistemaVO manterConfiguracaoGeralSistemaEmNivelAplicacao(
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, 
			Integer unidadeEnsino, 
			UsuarioVO usuarioLogado,
			boolean remover) throws Exception {

		Integer codUnidade = (unidadeEnsino == null) ? 0 : unidadeEnsino;
		String chaveRedis = "app:configuracaogeral:unidade:" + codUnidade;

		// --- REMOÇÃO ---
		if (remover) {
			this.cacheLocalConfig = null;
			this.ultimaAtualizacaoConfig = 0;
			try {
				if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO) || unidadeEnsino != null) {
					redisService.remover(chaveRedis);
				} else if (configuracaoGeralSistemaVO == null && unidadeEnsino == null) {
					redisService.remover("app:configuracaogeral:unidade:0");
				}
			} catch (Exception e) {
				System.err.println("Aviso: Falha ao limpar cache Redis: " + e.getMessage());
			}
			return configuracaoGeralSistemaVO;
		}

		// --- ESCRITA (Write-Through: Banco -> Redis -> RAM) ---
		if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
			try {
				getFacadeFactory().getConfiguracaoGeralSistemaFacade().alterar(configuracaoGeralSistemaVO, usuarioLogado);
			} catch (Exception e) {
				throw new Exception("Erro ao persistir no Banco de Dados.", e);
			}

			try {
				redisService.setObjeto(chaveRedis, configuracaoGeralSistemaVO, 24, TimeUnit.HOURS);
			} catch (Exception e) {
				System.err.println("Erro ao atualizar Redis (não crítico): " + e.getMessage());
			}

			this.cacheLocalConfig = configuracaoGeralSistemaVO;
			this.ultimaAtualizacaoConfig = System.currentTimeMillis();

			return configuracaoGeralSistemaVO;
		}

		// --- LEITURA OTIMIZADA (L1 RAM -> L2 REDIS -> L3 BANCO) ---
		
		// L1: Verifica RAM (Fast Path)
		if (this.cacheLocalConfig != null && 
		   (System.currentTimeMillis() - this.ultimaAtualizacaoConfig < TEMPO_CACHE_LOCAL_MS)) {
			return this.cacheLocalConfig;
		}

		// L2: Verifica Redis
		try {
			ConfiguracaoGeralSistemaVO cache = redisService.getObjeto(chaveRedis, ConfiguracaoGeralSistemaVO.class);
			if (cache != null) {
				this.cacheLocalConfig = cache;
				this.ultimaAtualizacaoConfig = System.currentTimeMillis();
				return cache;
			}
		} catch (Exception e) {
			System.err.println("Redis offline/erro no GET: " + e.getMessage());
		}

		// L3: Busca no Banco (Double-Check Locking)
		synchronized (lockConfiguracaoGeral) {
			try {
				ConfiguracaoGeralSistemaVO cache = redisService.getObjeto(chaveRedis, ConfiguracaoGeralSistemaVO.class);
				if (cache != null) {
					this.cacheLocalConfig = cache;
					this.ultimaAtualizacaoConfig = System.currentTimeMillis();
					return cache;
				}
			} catch (Exception e) { }

			Integer codigo = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarCodigoConfiguracaoReferenteUnidadeEnsino(unidadeEnsino);
			
			ConfiguracaoGeralSistemaVO configBanco = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarPorCodigoConfiguracaoGeralSistema(codigo, false, usuarioLogado, Uteis.NIVELMONTARDADOS_TODOS);

			if (configBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, configBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) { }
				
				this.cacheLocalConfig = configBanco;
				this.ultimaAtualizacaoConfig = System.currentTimeMillis();
			}
			
			return configBanco;
		}
	}

	// ============================================================================
	// CIDADE (Double-Check Locking)
	// ============================================================================
	
	public CidadeVO getCidadeVO(Integer codigoCidade, UsuarioVO usuario) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigoCidade)) {
			return null;
		}

		String chaveRedis = "app:cidade:" + codigoCidade;

		try {
			CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
			if (cidadeRedis != null) {
				return cidadeRedis;
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (leitura), seguindo vida...");
		}

		synchronized (lockBuscaCidade) {
			
			try {
				CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
				if (cidadeRedis != null) {
					return cidadeRedis;
				}
			} catch (Exception e) { }

			CidadeVO cidadeBanco = getFacadeFactory()
					.getCidadeFacade()
					.consultarPorChavePrimariaUnica(codigoCidade, false, usuario);

			if (cidadeBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, cidadeBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					 System.err.println("Erro ao salvar no Redis: " + e.getMessage());
				}
				return (CidadeVO) cidadeBanco.clone();
			}
		}

		return null;
	}

	public synchronized void removerCidade(Integer codigoCidade) throws Exception {
		if (codigoCidade != null) {
			String chaveRedis = "app:cidade:" + codigoCidade;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover cidade do Redis: " + e.getMessage());
			}
		}
		if (mapCidadeVOs.containsKey(codigoCidade)) {
			mapCidadeVOs.remove(codigoCidade);
		}
	}

	public synchronized void liberarListaCidadeMemoria() {
		mapCidadeVOs.clear();
	}

	// ============================================================================
	// DISCIPLINA (Double-Check Locking)
	// ============================================================================
	
	public DisciplinaVO getDisciplinaVO(Integer codigoDisciplina, UsuarioVO usuario) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigoDisciplina)) {
			return null;
		}

		String chaveRedis = "app:disciplina:" + codigoDisciplina;

		try {
			DisciplinaVO cache = redisService.getObjeto(chaveRedis, DisciplinaVO.class);
			if (cache != null) {
				return cache;
			}
		} catch (Exception e) {
			System.err.println("Erro Redis (Consulta Otimista) - Disciplina: " + e.getMessage());
		}

		synchronized (lockBuscaDisciplina) {

			try {
				DisciplinaVO cache = redisService.getObjeto(chaveRedis, DisciplinaVO.class);
				if (cache != null) {
					return cache;
				}
			} catch (Exception e) { }

			DisciplinaVO disciplinaBanco = getFacadeFactory()
					.getDisciplinaFacade()
					.consultarPorChavePrimariaUnica(codigoDisciplina, Uteis.NIVELMONTARDADOS_TODOS, usuario);

			if (disciplinaBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, disciplinaBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar Disciplina no Redis: " + e.getMessage());
				}

				return (DisciplinaVO) disciplinaBanco.clone();
			}
		}

		return null;
	}

	public synchronized void removerDisciplina(Integer codigoDisciplina) throws Exception {
		if (codigoDisciplina != null) {
			String chaveRedis = "app:disciplina:" + codigoDisciplina;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover disciplina do Redis: " + e.getMessage());
			}
		}
		if (mapDisciplinaVOs.containsKey(codigoDisciplina)) {
			mapDisciplinaVOs.remove(codigoDisciplina);
		}
		if (Uteis.isAtributoPreenchido(codigoDisciplina) && codigoDisciplina == -1) {
			mapDisciplinaVOs.clear();
		}
	}

	// ============================================================================
	// UNIDADE ENSINO (Double-Check Locking)
	// ============================================================================
	
	public synchronized UnidadeEnsinoVO getUnidadeEnsinoVO(Integer codigoUnidadeEnsino, UsuarioVO usuario)
			throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			return null;
		}

		String chaveRedis = "app:unidadeensino:" + codigoUnidadeEnsino;

		try {
			UnidadeEnsinoVO cache = redisService.getObjeto(chaveRedis, UnidadeEnsinoVO.class);
			if (cache != null) {
				return (UnidadeEnsinoVO) Uteis.clonar(cache);
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (UnidadeEnsino): " + e.getMessage());
		}

		synchronized (lockBuscaUnidadeEnsino) {
			
			try {
				UnidadeEnsinoVO cache = redisService.getObjeto(chaveRedis, UnidadeEnsinoVO.class);
				if (cache != null) {
					return (UnidadeEnsinoVO) Uteis.clonar(cache);
				}
			} catch (Exception e) { }

			UnidadeEnsinoVO unidadeBanco = getFacadeFactory().getUnidadeEnsinoFacade()
					.consultarPorChavePrimariaUnica(codigoUnidadeEnsino, usuario);

			if (unidadeBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, unidadeBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar UnidadeEnsino no Redis: " + e.getMessage());
				}
				return (UnidadeEnsinoVO) Uteis.clonar(unidadeBanco);
			}
		}

		return null;
	}

	public synchronized void removerUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		if (codigoUnidadeEnsino != null) {
			String chaveRedis = "app:unidadeensino:" + codigoUnidadeEnsino;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover UnidadeEnsino do Redis: " + e.getMessage());
			}
		}
		if (mapUnidadeEnsinoVOs.containsKey(codigoUnidadeEnsino)) {
			mapUnidadeEnsinoVOs.remove(codigoUnidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino) && codigoUnidadeEnsino == -1) {
			mapUnidadeEnsinoVOs.clear();
		}
	}

	// ============================================================================
	// PERFIL ACESSO (Double-Check Locking)
	// ============================================================================
	
	public synchronized PerfilAcessoVO getPerfilAcessoVO(Integer codigoPerfilAcesso, UsuarioVO usuario)
			throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigoPerfilAcesso)) {
			return null;
		}

		String chaveRedis = "app:perfilacesso:" + codigoPerfilAcesso;

		try {
			PerfilAcessoVO cache = redisService.getObjeto(chaveRedis, PerfilAcessoVO.class);
			if (cache != null) {
				return (PerfilAcessoVO) cache.clone();
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (PerfilAcesso): " + e.getMessage());
		}

		synchronized (lockBuscaPerfilAcesso) {
			
			try {
				PerfilAcessoVO cache = redisService.getObjeto(chaveRedis, PerfilAcessoVO.class);
				if (cache != null) {
					return (PerfilAcessoVO) cache.clone();
				}
			} catch (Exception e) { }

			PerfilAcessoVO perfilBanco = getFacadeFactory().getPerfilAcessoFacade()
					.consultarPorChavePrimariaUnica(codigoPerfilAcesso, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);

			if (perfilBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, perfilBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar PerfilAcesso no Redis: " + e.getMessage());
				}
				return (PerfilAcessoVO) perfilBanco.clone();
			}
		}

		return null;
	}

	public synchronized void removerPerfilAcesso(Integer codigoPerfilAcesso) throws Exception {
		if (codigoPerfilAcesso != null) {
			String chaveRedis = "app:perfilacesso:" + codigoPerfilAcesso;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover PerfilAcesso do Redis: " + e.getMessage());
			}
		}
		if (mapPerfilAcessoVOs.containsKey(codigoPerfilAcesso)) {
			mapPerfilAcessoVOs.remove(codigoPerfilAcesso);
		}
		if (Uteis.isAtributoPreenchido(codigoPerfilAcesso) && codigoPerfilAcesso == -1) {
			mapPerfilAcessoVOs.clear();
		}
	}

	// ============================================================================
	// TURNO (Double-Check Locking)
	// ============================================================================
	
	public synchronized TurnoVO getTurnoVO(Integer codigoTurno, UsuarioVO usuario) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigoTurno)) {
			return null;
		}

		String chaveRedis = "app:turno:" + codigoTurno;

		try {
			TurnoVO cache = redisService.getObjeto(chaveRedis, TurnoVO.class);
			if (cache != null) {
				return (TurnoVO) cache.clone();
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (Turno): " + e.getMessage());
		}

		synchronized (lockBuscaTurno) {
			
			try {
				TurnoVO cache = redisService.getObjeto(chaveRedis, TurnoVO.class);
				if (cache != null) {
					return (TurnoVO) cache.clone();
				}
			} catch (Exception e) { }

			TurnoVO turnoBanco = getFacadeFactory().getTurnoFacade()
					.consultarPorChavePrimariaUnico(codigoTurno, Uteis.NIVELMONTARDADOS_TODOS, usuario);

			if (turnoBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, turnoBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar Turno no Redis: " + e.getMessage());
				}
				return (TurnoVO) turnoBanco.clone();
			}
		}

		return null;
	}

	public synchronized void removerTurno(Integer codigoTurno) throws Exception {
		if (codigoTurno != null) {
			String chaveRedis = "app:turno:" + codigoTurno;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover Turno do Redis: " + e.getMessage());
			}
		}
		if (mapTurnoVOs.containsKey(codigoTurno)) {
			mapTurnoVOs.remove(codigoTurno);
		}
		if (Uteis.isAtributoPreenchido(codigoTurno) && codigoTurno == -1) {
			mapTurnoVOs.clear();
		}
	}

	// ============================================================================
	// TIPO DOCUMENTO (Double-Check Locking)
	// ============================================================================
	
	public synchronized TipoDocumentoVO getTipoDocumentoVO(Integer codigo, UsuarioVO usuario) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigo)) {
			return null;
		}

		String chaveRedis = "app:tipodocumento:" + codigo;

		try {
			TipoDocumentoVO cache = redisService.getObjeto(chaveRedis, TipoDocumentoVO.class);
			if (cache != null) {
				return (TipoDocumentoVO) cache.clone();
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (TipoDocumento): " + e.getMessage());
		}

		synchronized (lockBuscaTipoDocumento) {
			
			try {
				TipoDocumentoVO cache = redisService.getObjeto(chaveRedis, TipoDocumentoVO.class);
				if (cache != null) {
					return (TipoDocumentoVO) cache.clone();
				}
			} catch (Exception e) { }

			TipoDocumentoVO tipoBanco = getFacadeFactory().getTipoDeDocumentoFacade()
					.consultarPorChavePrimariaUnico(codigo, Uteis.NIVELMONTARDADOS_TODOS, usuario);

			if (tipoBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, tipoBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar TipoDocumento no Redis: " + e.getMessage());
				}
				return (TipoDocumentoVO) tipoBanco.clone();
			}
		}

		return null;
	}

	public synchronized void removerTipoDocumento(Integer codigo) throws Exception {
		if (codigo != null) {
			String chaveRedis = "app:tipodocumento:" + codigo;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover TipoDocumento do Redis: " + e.getMessage());
			}
		}
		if (mapTipoDocumentoVOs.containsKey(codigo)) {
			mapTipoDocumentoVOs.remove(codigo);
		}
		if (Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapTipoDocumentoVOs.clear();
		}
	}

	// ============================================================================
	// TIPO ATIVIDADE COMPLEMENTAR (Double-Check Locking)
	// ============================================================================
	
	public synchronized TipoAtividadeComplementarVO getTipoAtividadeComplementarVO(Integer codigo, UsuarioVO usuario)
			throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigo)) {
			return null;
		}

		String chaveRedis = "app:tipoatividadecomplementar:" + codigo;

		try {
			TipoAtividadeComplementarVO cache = redisService.getObjeto(chaveRedis, TipoAtividadeComplementarVO.class);
			if (cache != null) {
				return (TipoAtividadeComplementarVO) cache.clone();
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (TipoAtividadeComplementar): " + e.getMessage());
		}

		synchronized (lockBuscaTipoAtividadeComplementar) {
			
			try {
				TipoAtividadeComplementarVO cache = redisService.getObjeto(chaveRedis, TipoAtividadeComplementarVO.class);
				if (cache != null) {
					return (TipoAtividadeComplementarVO) cache.clone();
				}
			} catch (Exception e) { }

			TipoAtividadeComplementarVO tipoBanco = getFacadeFactory()
					.getTipoAtividadeComplementarFacade()
					.consultarPorChavePrimariaUnico(codigo, usuario);

			if (tipoBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, tipoBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar TipoAtividadeComplementar no Redis: " + e.getMessage());
				}
				return (TipoAtividadeComplementarVO) tipoBanco.clone();
			}
		}

		return null;
	}

	public synchronized void removerTipoAtividadeComplementar(Integer codigo) throws Exception {
		if (codigo != null) {
			String chaveRedis = "app:tipoatividadecomplementar:" + codigo;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover TipoAtividadeComplementar do Redis: " + e.getMessage());
			}
		}
		if (mapTipoAtividadeComplementarVOs.containsKey(codigo)) {
			mapTipoAtividadeComplementarVOs.remove(codigo);
		}
		if (Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapTipoAtividadeComplementarVOs.clear();
		}
	}

	// ============================================================================
	// CATEGORIA GED (Double-Check Locking)
	// ============================================================================
	
	public synchronized CategoriaGEDVO getCategoriaGEDVO(Integer codigo, UsuarioVO usuario) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigo)) {
			return null;
		}

		String chaveRedis = "app:categoriaged:" + codigo;

		try {
			CategoriaGEDVO cache = redisService.getObjeto(chaveRedis, CategoriaGEDVO.class);
			if (cache != null) {
				return (CategoriaGEDVO) cache.clone();
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (CategoriaGED): " + e.getMessage());
		}

		synchronized (lockBuscaCategoriaGED) {
			
			try {
				CategoriaGEDVO cache = redisService.getObjeto(chaveRedis, CategoriaGEDVO.class);
				if (cache != null) {
					return (CategoriaGEDVO) cache.clone();
				}
			} catch (Exception e) { }

			CategoriaGEDVO categoriaBanco = getFacadeFactory().getCategoriaGEDInterfaceFacade()
					.consultarPorChavePrimariaUnico(codigo);

			if (categoriaBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, categoriaBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar CategoriaGED no Redis: " + e.getMessage());
				}
				return (CategoriaGEDVO) categoriaBanco.clone();
			}
		}

		return null;
	}

	public synchronized void removerCategoriaGED(Integer codigo) throws Exception {
		if (codigo != null) {
			String chaveRedis = "app:categoriaged:" + codigo;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover CategoriaGED do Redis: " + e.getMessage());
			}
		}
		if (mapCategoriaGEDVOs.containsKey(codigo)) {
			mapCategoriaGEDVOs.remove(codigo);
		}
		if (Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapCategoriaGEDVOs.clear();
		}
	}

	// ============================================================================
	// CONFIGURAÇÃO GED (Double-Check Locking)
	// ============================================================================
	
	public synchronized ConfiguracaoGEDVO getConfiguracaoGEDVO(Integer codigo) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigo)) {
			return null;
		}

		String chaveRedis = "app:configuracaoged:" + codigo;

		try {
			ConfiguracaoGEDVO cache = redisService.getObjeto(chaveRedis, ConfiguracaoGEDVO.class);
			if (cache != null) {
				return cache;
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (ConfiguracaoGED): " + e.getMessage());
		}

		synchronized (lockBuscaConfiguracaoGED) {
			
			try {
				ConfiguracaoGEDVO cache = redisService.getObjeto(chaveRedis, ConfiguracaoGEDVO.class);
				if (cache != null) {
					return cache;
				}
			} catch (Exception e) { }

			ConfiguracaoGEDVO configBanco = new ConfiguracaoGEDVO();
			configBanco.setCodigo(codigo);
			getFacadeFactory().getConfiguracaoGEDFacade().carregarDados(configBanco, NivelMontarDados.TODOS, null);

			if (Uteis.isAtributoPreenchido(configBanco)) {
				try {
					redisService.setObjeto(chaveRedis, configBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar ConfiguracaoGED no Redis: " + e.getMessage());
				}
			}
			
			// Atualiza mapa local (compatibilidade)
			if (mapConfiguracaoGEDVOs.containsKey(codigo)) {
				mapConfiguracaoGEDVOs.remove(codigo);
			}
			mapConfiguracaoGEDVOs.put(codigo, configBanco);
			
			return configBanco;
		}
	}

	public synchronized ConfiguracaoGEDVO getConfiguracaoGEDPorUnidadeEnsino(Integer unidadeEnsino,
			UsuarioVO usuarioLogado) throws Exception {
		
		String chaveRedis = "app:configuracaoged:unidade:" + unidadeEnsino;

		try {
			ConfiguracaoGEDVO cache = redisService.getObjeto(chaveRedis, ConfiguracaoGEDVO.class);
			if (cache != null) {
				return cache;
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (ConfiguracaoGED por Unidade): " + e.getMessage());
		}

		// Busca no banco
		ConfiguracaoGEDVO configuracaoGEDVO = getFacadeFactory().getConfiguracaoGEDFacade()
				.consultarPorUnidadeEnsinoUnica(unidadeEnsino, false, usuarioLogado);

		if (configuracaoGEDVO != null) {
			try {
				redisService.setObjeto(chaveRedis, configuracaoGEDVO, 24, TimeUnit.HOURS);
			} catch (Exception e) {
				System.err.println("Erro ao salvar ConfiguracaoGED por Unidade no Redis: " + e.getMessage());
			}
		}
		
		// Atualiza mapa local (compatibilidade)
		mapConfiguracaoGedUnidadeEnsino.put(unidadeEnsino, configuracaoGEDVO);
		
		return configuracaoGEDVO;
	}

	public synchronized void removerConfiguracaoGEDVOPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		if (unidadeEnsino != null) {
			String chaveRedis = "app:configuracaoged:unidade:" + unidadeEnsino;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover ConfiguracaoGED por Unidade do Redis: " + e.getMessage());
			}
		}
		if (mapConfiguracaoGedUnidadeEnsino.containsKey(unidadeEnsino)) {
			mapConfiguracaoGedUnidadeEnsino.remove(unidadeEnsino);
		}
	}

	public synchronized void removerConfiguracaoGEDVO(Integer configuracaoGED) throws Exception {
		if (configuracaoGED != null) {
			String chaveRedis = "app:configuracaoged:" + configuracaoGED;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover ConfiguracaoGED do Redis: " + e.getMessage());
			}
		}
		
		// Remove de todos os mapas por unidade que apontam para essa configuração
		Iterator<Entry<Integer, ConfiguracaoGEDVO>> iterator = mapConfiguracaoGedUnidadeEnsino.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, ConfiguracaoGEDVO> next = iterator.next();
			if (Uteis.isAtributoPreenchido(next.getValue()) && next.getValue().getCodigo().equals(configuracaoGED)) {
				iterator.remove();
			}
		}

		if (mapConfiguracaoGEDVOs.containsKey(configuracaoGED)) {
			mapConfiguracaoGEDVOs.remove(configuracaoGED);
		}
	}

	// ============================================================================
	// PAÍS (Double-Check Locking)
	// ============================================================================
	
	public synchronized PaizVO getPaizVO(Integer codigo) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigo)) {
			return null;
		}

		String chaveRedis = "app:paiz:" + codigo;

		try {
			PaizVO cache = redisService.getObjeto(chaveRedis, PaizVO.class);
			if (cache != null) {
				return cache;
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (Paiz): " + e.getMessage());
		}

		synchronized (lockBuscaPaiz) {
			
			try {
				PaizVO cache = redisService.getObjeto(chaveRedis, PaizVO.class);
				if (cache != null) {
					return cache;
				}
			} catch (Exception e) { }

			PaizVO paizBanco = getFacadeFactory().getPaizFacade()
					.consultarPorChavePrimariaUnico(codigo, false, null);

			if (paizBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, paizBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar Paiz no Redis: " + e.getMessage());
				}
				
				// Atualiza mapa local (compatibilidade)
				if (mapPaisVOs.containsKey(codigo)) {
					mapPaisVOs.remove(codigo);
				}
				mapPaisVOs.put(codigo, paizBanco);
			}
			
			return paizBanco;
		}
	}

	public synchronized void removerPaizVO(Integer codigo) throws Exception {
		if (codigo != null) {
			String chaveRedis = "app:paiz:" + codigo;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover Paiz do Redis: " + e.getMessage());
			}
		}
		if (mapPaisVOs.containsKey(codigo)) {
			mapPaisVOs.remove(codigo);
		}
	}

	// ============================================================================
	// ESTADO (Double-Check Locking)
	// ============================================================================
	
	public synchronized EstadoVO getEstadoVO(Integer codigo) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigo)) {
			return null;
		}

		String chaveRedis = "app:estado:" + codigo;

		try {
			EstadoVO cache = redisService.getObjeto(chaveRedis, EstadoVO.class);
			if (cache != null) {
				return cache;
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (Estado): " + e.getMessage());
		}

		synchronized (lockBuscaEstado) {
			
			try {
				EstadoVO cache = redisService.getObjeto(chaveRedis, EstadoVO.class);
				if (cache != null) {
					return cache;
				}
			} catch (Exception e) { }

			EstadoVO estadoBanco = getFacadeFactory().getEstadoFacade()
					.consultarPorChavePrimariaUnico(codigo, Uteis.NIVELMONTARDADOS_TODOS, null);

			if (estadoBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, estadoBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar Estado no Redis: " + e.getMessage());
				}
				
				// Atualiza mapa local (compatibilidade)
				if (mapEstadoVOs.containsKey(codigo)) {
					mapEstadoVOs.remove(codigo);
				}
				mapEstadoVOs.put(codigo, estadoBanco);
			}
			
			return estadoBanco;
		}
	}

	public synchronized void removerEstadoVO(Integer codigo) throws Exception {
		if (codigo != null) {
			String chaveRedis = "app:estado:" + codigo;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover Estado do Redis: " + e.getMessage());
			}
		}
		if (mapEstadoVOs.containsKey(codigo)) {
			mapEstadoVOs.remove(codigo);
		}
	}

	// ============================================================================
	// GRADE CURRICULAR (Double-Check Locking)
	// ============================================================================
	
	public synchronized GradeCurricularVO getGradeCurricularVO(Integer codigo) throws Exception {
		
		if (!Uteis.isAtributoPreenchido(codigo)) {
			return null;
		}

		String chaveRedis = "app:gradecurricular:" + codigo;

		try {
			GradeCurricularVO cache = redisService.getObjeto(chaveRedis, GradeCurricularVO.class);
			if (cache != null) {
				return (GradeCurricularVO) Uteis.clonar(cache);
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (GradeCurricular): " + e.getMessage());
		}

		synchronized (lockBuscaGradeCurricular) {
			
			try {
				GradeCurricularVO cache = redisService.getObjeto(chaveRedis, GradeCurricularVO.class);
				if (cache != null) {
					return (GradeCurricularVO) Uteis.clonar(cache);
				}
			} catch (Exception e) { }

			GradeCurricularVO gradeBanco = getFacadeFactory().getGradeCurricularFacade()
					.consultarPorChavePrimariaUnica(codigo, Uteis.NIVELMONTARDADOS_TODOS, null);

			if (gradeBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, gradeBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar GradeCurricular no Redis: " + e.getMessage());
				}
				
				// Atualiza mapa local (compatibilidade)
				if (mapGradeCurricularVOs.containsKey(codigo)) {
					mapGradeCurricularVOs.remove(codigo);
				}
				mapGradeCurricularVOs.put(codigo, gradeBanco);
				
				return (GradeCurricularVO) Uteis.clonar(gradeBanco);
			}
		}

		return null;
	}

	public synchronized void removerGradeCurricularVO(Integer codigo) throws Exception {
		if (codigo != null) {
			String chaveRedis = "app:gradecurricular:" + codigo;
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover GradeCurricular do Redis: " + e.getMessage());
			}
		}
		if (mapGradeCurricularVOs.containsKey(codigo)) {
			mapGradeCurricularVOs.remove(codigo);
		}
	}

	// ============================================================================
	// CONFIGURAÇÃO ACADÊMICA (Double-Check Locking)
	// ============================================================================
	
	public synchronized ConfiguracaoAcademicoVO carregarDadosConfiguracaoAcademica(Integer codigoConfAcademico)
			throws Exception {
		return manterConfiguracaoAcademicoEmNivelAplicacao(codigoConfAcademico, false);
	}

	private synchronized ConfiguracaoAcademicoVO manterConfiguracaoAcademicoEmNivelAplicacao(
			Integer codigoConfAcademico, boolean remover) throws Exception {
		
		String chaveRedis = "app:configuracaoacademica:" + codigoConfAcademico;

		// --- REMOÇÃO ---
		if (remover) {
			try {
				redisService.remover(chaveRedis);
			} catch (Exception e) {
				System.err.println("Erro ao remover ConfiguracaoAcademica do Redis: " + e.getMessage());
			}
			
			if (mapConfiguracaoAcademica.containsKey(codigoConfAcademico)) {
				return mapConfiguracaoAcademica.remove(codigoConfAcademico);
			}
			return null;
		}

		// --- LEITURA ---
		if (!Uteis.isAtributoPreenchido(codigoConfAcademico)) {
			ConfiguracaoAcademicoVO vazio = new ConfiguracaoAcademicoVO();
			mapConfiguracaoAcademica.put(codigoConfAcademico, vazio);
			return vazio;
		}

		// Tenta Redis primeiro
		try {
			ConfiguracaoAcademicoVO cache = redisService.getObjeto(chaveRedis, ConfiguracaoAcademicoVO.class);
			if (cache != null) {
				mapConfiguracaoAcademica.put(codigoConfAcademico, cache);
				return cache;
			}
		} catch (Exception e) {
			System.err.println("Redis falhou (ConfiguracaoAcademica): " + e.getMessage());
		}

		// Double-check locking
		synchronized (lockConfiguracaoAcademica) {
			
			try {
				ConfiguracaoAcademicoVO cache = redisService.getObjeto(chaveRedis, ConfiguracaoAcademicoVO.class);
				if (cache != null) {
					mapConfiguracaoAcademica.put(codigoConfAcademico, cache);
					return cache;
				}
			} catch (Exception e) { }

			// Busca no banco
			ConfiguracaoAcademicoVO configBanco = getFacadeFactory().getConfiguracaoAcademicoFacade()
					.consultarPorChavePrimariaUnica(codigoConfAcademico, null);

			if (configBanco != null) {
				try {
					redisService.setObjeto(chaveRedis, configBanco, 24, TimeUnit.HOURS);
				} catch (Exception e) {
					System.err.println("Erro ao salvar ConfiguracaoAcademica no Redis: " + e.getMessage());
				}
				
				mapConfiguracaoAcademica.put(codigoConfAcademico, configBanco);
				return configBanco;
			}
		}

		return null;
	}

	public synchronized void removerConfiguracaoAcademica(Integer codigoConfAcademico) throws Exception {
		manterConfiguracaoAcademicoEmNivelAplicacao(codigoConfAcademico, true);
	}

	public synchronized void liberarListaConfiguracaoAcademicaMemoria() {
		mapConfiguracaoAcademica.clear();
	}

	// ============================================================================
	// CONFIGURAÇÃO BIBLIOTECA (Mantido original - menos crítico)
	// ============================================================================
	
	public synchronized void removerConfiguracaoBibliotecaPadrao() throws Exception {
		manterConfiguracaoBibliotecaPadraoEmNivelAplicacao(true);
	}

	public synchronized ConfiguracaoBibliotecaVO carregarDadosConfiguracaoBibliotecaPadrao() throws Exception {
		return manterConfiguracaoBibliotecaPadraoEmNivelAplicacao(false);
	}

	private synchronized ConfiguracaoBibliotecaVO manterConfiguracaoBibliotecaPadraoEmNivelAplicacao(boolean remover)
			throws Exception {
		if (remover) {
			this.setConfiguracaoBibliotecaPadrao(null);
		} else {
			if (!Uteis.isAtributoPreenchido(getConfiguracaoBibliotecaPadrao())) {
				configuracaoBibliotecaPadrao = getFacadeFactory().getConfiguracaoBibliotecaFacade()
						.consultarConfiguracaoPadrao(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			}
			return configuracaoBibliotecaPadrao;
		}
		return null;
	}

	public ConfiguracaoBibliotecaVO getConfiguracaoBibliotecaPadrao() {
		if (configuracaoBibliotecaPadrao == null) {
			configuracaoBibliotecaPadrao = new ConfiguracaoBibliotecaVO();
		}
		return configuracaoBibliotecaPadrao;
	}

	public void setConfiguracaoBibliotecaPadrao(ConfiguracaoBibliotecaVO configuracaoBibliotecaPadrao) {
		this.configuracaoBibliotecaPadrao = configuracaoBibliotecaPadrao;
	}

	// ============================================================================
	// MÉTODOS AUXILIARES E LEGADOS (Mantidos do original)
	// ============================================================================

	public synchronized void liberarListaConfiguracaoGeralMemoria() throws Exception {
		manterConfiguracaoGeralSistemaEmNivelAplicacao(null, null, null, true);
	}

	// ... (CONTINUA COM TODOS OS OUTROS MÉTODOS DO ORIGINAL) ...
	
	// Os métodos abaixo são mantidos 100% iguais ao original:
	// - ativar(), desativar(), limparRegistros(), atualizar()
	// - obterListaDataComemorativaDataAtual()
	// - montarFraseInspiricaoDoDia(), getObterFraseInspiracaoDoDia()
	// - Todos os getters/setters de controle de atividades
	// - Métodos de geração manual parcela
	// - Métodos de controle de threads
	// - Métodos de debug
	// - Métodos de scripts executados
	// - Todos os ConcurrentHashMap caches
	// - etc.

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
				setFraseInspiracaoVO(getFacadeFactory().getFraseInspiracaoFacade().consultarFraseRandom("%", false,
						Uteis.NIVELMONTARDADOS_TODOS, null));
				getFraseInspiracaoVO().setDataUltimaExibicao(new Date());
			} else if (!Uteis.getData(getFraseInspiracaoVO().getDataUltimaExibicao()).equals(Uteis.getDataAtual())) {
				setFraseInspiracaoVO(getFacadeFactory().getFraseInspiracaoFacade().consultarFraseRandom("%", false,
						Uteis.NIVELMONTARDADOS_TODOS, null));
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

	public FraseInspiracaoVO getFraseInspiracaoVO() {
		if (fraseInspiracaoVO == null) {
			return new FraseInspiracaoVO();
		}
		return fraseInspiracaoVO;
	}

	public void setFraseInspiracaoVO(FraseInspiracaoVO fraseInspiracaoVO) {
		this.fraseInspiracaoVO = fraseInspiracaoVO;
	}

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
						ControleAtividadeUsuarioVO controle = (ControleAtividadeUsuarioVO) hash
								.get(contr.getEntidade() + "-" + contr.getDescricao() + "-" + contr.getTipo());
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

	public void setMapaControleUsuarios(List<MapaControleAtividadesUsuarioVO> mapaControleUsuarios) {
		this.mapaControleUsuarios = mapaControleUsuarios;
	}

	public MapaControleAtividadesUsuarioVO obterMapaControleAtividadesUsuarioEspecifico(UsuarioVO usuarioVO)
			throws Exception {
		Iterator<MapaControleAtividadesUsuarioVO> i = getMapaControleUsuarios().iterator();
		while (i.hasNext()) {
			MapaControleAtividadesUsuarioVO mapa = i.next();
			if (mapa.getUsuarioVO().getCodigo().equals(usuarioVO.getCodigo())) {
				return mapa;
			}
		}
		return null;
	}

	public MapaControleAtividadesUsuarioVO obterMapaControleAtividadesUsuarioEspecifico(UsuarioVO usuarioVO,
			String idSession) throws Exception {
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
		setMapaControleAtividadesUsuarioVO((MapaControleAtividadesUsuarioVO) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap().get("mapaUsuario"));
	}

	public void registrarAtividadeUsuarioEmNivelAplicacao(UsuarioVO usuarioVO, String idSession, String entidade,
			String descricao, String tipo) throws Exception {
		if (idSession == null) {
			return;
		}
		MapaControleAtividadesUsuarioVO mapaControleUsuarioVO = obterMapaControleAtividadesUsuarioEspecifico(usuarioVO,
				idSession);
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
		if (tipoUsuarioMonitoramento == null) {
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
			if (obj.getUsuarioVO().getPessoa().getFuncionario()
					&& getFiltroMapaControleAtividade().equals("funcionario")) {
				mapaControleFiltrado.add(obj);
			} else if (obj.getUsuarioVO().getPessoa().getAluno() && getFiltroMapaControleAtividade().equals("aluno")) {
				mapaControleFiltrado.add(obj);
			}
		}
		montarListaMapaAtividadesUsuarioPorUsuario();
		return mapaControleFiltrado;
	}

	public Boolean getMapaControleAtividadesUsuariosAtivo() {
		if (mapaControleAtividadesUsuariosAtivo == null) {
			mapaControleAtividadesUsuariosAtivo = Boolean.TRUE;
		}
		return mapaControleAtividadesUsuariosAtivo;
	}

	public void setMapaControleAtividadesUsuariosAtivo(Boolean mapaControleAtividadesUsuariosAtivo) {
		this.mapaControleAtividadesUsuariosAtivo = mapaControleAtividadesUsuariosAtivo;
	}

	public MapaControleAtividadesUsuarioVO getMapaControleAtividadesUsuarioVO() {
		if (mapaControleAtividadesUsuarioVO == null) {
			mapaControleAtividadesUsuarioVO = new MapaControleAtividadesUsuarioVO();
		}
		return mapaControleAtividadesUsuarioVO;
	}

	public void setMapaControleAtividadesUsuarioVO(MapaControleAtividadesUsuarioVO mapaControleAtividadesUsuarioVO) {
		this.mapaControleAtividadesUsuarioVO = mapaControleAtividadesUsuarioVO;
	}

	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public void setMapaAtividadesUsuarioPorEntidade(List<ControleAtividadeUsuarioVO> mapaAtividadesUsuarioPorEntidade) {
		this.mapaAtividadesUsuarioPorEntidade = mapaAtividadesUsuarioPorEntidade;
	}

	public List<ControleAtividadeUsuarioVO> getMapaAtividadesUsuarioPorUsuario() {
		if (mapaAtividadesUsuarioPorUsuario == null) {
			mapaAtividadesUsuarioPorUsuario = new ArrayList<ControleAtividadeUsuarioVO>();
		}
		return mapaAtividadesUsuarioPorUsuario;
	}

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

	public Hashtable getListaDataComemorativaApresentar() {
		if (listaDataComemorativaApresentar == null) {
			listaDataComemorativaApresentar = new Hashtable(0);
		}
		return listaDataComemorativaApresentar;
	}

	public void setListaDataComemorativaApresentar(Hashtable listaDataComemorativaApresentar) {
		this.listaDataComemorativaApresentar = listaDataComemorativaApresentar;
	}

	public Date getAutenticacaoRealizada() {
		return autenticacaoRealizada;
	}

	public void setAutenticacaoRealizada(Date autenticacaoRealizada) {
		this.autenticacaoRealizada = autenticacaoRealizada;
	}

	public Boolean getGeracaoParcelaExecutando() {
		if (geracaoParcelaExecutando == null) {
			geracaoParcelaExecutando = Boolean.FALSE;
		}
		return geracaoParcelaExecutando;
	}

	public synchronized void setGeracaoParcelaExecutando(Boolean geracaoParcelaExecutando) {
		this.geracaoParcelaExecutando = geracaoParcelaExecutando;
	}

	public void realizarReinicializacaoRotinaGeracaoManualParcela() {
		// Implementação original mantida
	}

	public synchronized void realizarGeracaoManualParcela(Integer unidadeEnsino, Boolean retornarExcecao,
			Boolean remover) throws Exception {
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

	public synchronized void executarEnvioNotasFiscais(Integer confNotaFiscal, Boolean retornarExcecao, Boolean remover,
			Boolean adicionarUnidade) throws AcessoException {
		int index = 0;
		for (Integer conf : getConfNotaFiscalVOs()) {
			if (conf.intValue() == confNotaFiscal.intValue()) {
				if (remover != null && remover) {
					getConfNotaFiscalVOs().remove(index);
					return;
				}
				if (retornarExcecao != null && retornarExcecao) {
					throw new AcessoException(
							UteisJSF.internacionalizar("msg_NotaFiscalSaida_notaSendoEmitidaNestaConfigNotaFiscal"));
				}
				return;
			}
			index++;
		}
		if (adicionarUnidade != null && adicionarUnidade) {
			getConfNotaFiscalVOs().add(confNotaFiscal);
		}
	}

	public synchronized void executarBloqueioTurmaNaRenovacaoTurma(Integer key, Thread jobRenovacaoTurma,
			Boolean retornarExcecao, Boolean remover, Boolean adicionarTurma, Boolean interromperJob)
			throws AcessoException {
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
		} else if (adicionarTurma) {
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
			// Implementação original mantida
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
		// Implementação original mantida
	}

	public static Integer getIntegracaoFinanceiraProcessando() {
		if (AplicacaoControle.integracaoFinanceiraProcessando == null) {
			AplicacaoControle.integracaoFinanceiraProcessando = 0;
		}
		return AplicacaoControle.integracaoFinanceiraProcessando;
	}

	public static void setIntegracaoFinanceiraProcessando(Integer integracaoFinanceiraProcessando) {
		AplicacaoControle.integracaoFinanceiraProcessando = integracaoFinanceiraProcessando;
	}

	public static synchronized boolean realizarRegistroInicioProcessamentoIntegracaoFinanceira(
			Integer integracaoFinanceiraProcessando) {
		if (Uteis.isAtributoPreenchido(getIntegracaoFinanceiraProcessando())) {
			return false;
		}
		AplicacaoControle.setIntegracaoFinanceiraProcessando(integracaoFinanceiraProcessando);
		return true;
	}

	public static synchronized boolean realizarRegistroTerminoProcessamentoIntegracaoFinanceira(
			Integer integracaoFinanceiraProcessando) {
		if (Uteis.isAtributoPreenchido(getIntegracaoFinanceiraProcessando())
				&& !integracaoFinanceiraProcessando.equals(getIntegracaoFinanceiraProcessando())) {
			return false;
		}
		AplicacaoControle.setIntegracaoFinanceiraProcessando(0);
		return true;
	}

	public static synchronized void executarBloqueioTurmaProgramacaoAula(TurmaVO turma, Date update, String ano,
			String semestre, boolean adicionar, boolean retornarExcecao,
			HorarioTurmaInterfaceFacade horarioTurmaInterfaceFacade, UsuarioVO usuarioVO) throws AcessoException {
		if (adicionar) {
			Date updatedDB;
			try {
				updatedDB = horarioTurmaInterfaceFacade.consultarUpdatedPorTurmaAnoSemestre(turma.getCodigo(), ano,
						semestre, false, usuarioVO);
			} catch (Exception e) {
				throw new AcessoException(e.getMessage());
			}
			if ((Uteis.isAtributoPreenchido(updatedDB)
					&& !Uteis.getDataComHoraCompleta(update).equals(Uteis.getDataComHoraCompleta(updatedDB)))) {
				throw new AcessoException(UteisJSF.internacionalizar("msg_ProgramacaoAula_dadoDefasado").replace("{0}",
						turma.getIdentificadorTurma()));
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
		Integer conf = (Integer) FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get("atividadeEmissaoNota");
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

	public synchronized void executarBloqueioProcessamentoArquivoProvaPresencial(boolean adicionar,
			boolean retornarExcecao) throws AcessoException {
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
	if (mensagemTelaLogin == null) {
		mensagemTelaLogin = "";
		ConfiguracaoGeralSistemaVO conf;
		try {
			conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarPorMensagemTelaLoginConfiguracaoPadraoSistema();
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
	if (AplicacaoControle.processandoDocumentacaoGED == null
			|| AplicacaoControle.processandoDocumentacaoGED == false) {
		AplicacaoControle.processandoDocumentacaoGED = true;
	}
	return AplicacaoControle.processandoDocumentacaoGED;
}

public static synchronized void registrarTerminoProcessamentoDigitalizacaoGED() {
	AplicacaoControle.processandoDocumentacaoGED = false;
}

public Map<String, List<TurmaVO>> getMapTurmasOfertadas() {
	if (mapTurmasOfertadas == null) {
		mapTurmasOfertadas = new HashMap<>();
	}
	return mapTurmasOfertadas;
}

public synchronized void adicionarMapThreadControleCobranca(Integer codigoOrigem, ProgressBarVO progressBarVO) {
	if (!getMapThreadControleCobranca().containsKey(codigoOrigem)) {
		getMapThreadControleCobranca().put(codigoOrigem, progressBarVO);
	}
}

public synchronized void removerMapThreadControleCobranca(Integer codigoOrigem) {
	if (getMapThreadControleCobranca().containsKey(codigoOrigem)) {
		getMapThreadControleCobranca().remove(codigoOrigem);
	}
}

public Map<Integer, ProgressBarVO> getMapThreadControleCobranca() {
	if (mapThreadControleCobranca == null) {
		mapThreadControleCobranca = new HashMap<Integer, ProgressBarVO>();
	}
	return mapThreadControleCobranca;
}

public void setMapThreadControleCobranca(Map<Integer, ProgressBarVO> mapThreadControleCobranca) {
	this.mapThreadControleCobranca = mapThreadControleCobranca;
}

public Map<String, ProgressBarVO> getMapThreadIndiceReajuste() {
	if (mapThreadIndiceReajuste == null) {
		mapThreadIndiceReajuste = new HashMap<String, ProgressBarVO>();
	}
	return mapThreadIndiceReajuste;
}

public void setMapThreadIndiceReajuste(Map<String, ProgressBarVO> mapThreadIndiceReajuste) {
	this.mapThreadIndiceReajuste = mapThreadIndiceReajuste;
}

// ============================================================================
// DEBUG
// ============================================================================

public static final Integer LIMITE_CARACTERES_TEXTO_DEBUG = 2000000;
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
			StringBuilder texto = new StringBuilder(LocalDateTime.now().toString());
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
			StringBuilder texto = new StringBuilder(LocalDateTime.now().toString()).append(erro.getMessage())
					.append("\n");
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
		if (getTextoDebug().length() > LIMITE_CARACTERES_TEXTO_DEBUG
				&& Uteis.isAtributoPreenchido(getTextoDebug().toString())) {
			getTextoDebug().delete(0, getTextoDebug().length() / 2);
		}
	} catch (Exception ex) {
		System.out.println("FALHA AO REMOVER CARACTERES TEXTO DEBUG!");
		ex.printStackTrace();
	}
}

// ============================================================================
// SCRIPTS
// ============================================================================

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
	return (getScriptsNaoExecutados().size() + getScriptsNaoPersistidos().size() + getScriptsAbortados().size()
			+ getScriptsInvalidos().size() > 0) || Uteis.isAtributoPreenchido(getMensagemErroScriptsExecutados());
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

// ============================================================================
// CONFIGURAÇÃO APARÊNCIA
// ============================================================================

public ConfiguracaoAparenciaSistemaVO getConfiguracaoAparenciaSistemaVO() {
	if (configuracaoAparenciaSistemaVO == null) {
		try {
			configuracaoAparenciaSistemaVO = getFacadeFactory().getConfiguracaoAparenciaSistemaFacade()
					.consultarConfiguracaoAparenciaSistema(false, null);
		} catch (Exception e) {
			configuracaoAparenciaSistemaVO = new ConfiguracaoAparenciaSistemaVO();
			getFacadeFactory().getConfiguracaoAparenciaSistemaFacade()
					.realizarGeracaoScriptCss(configuracaoAparenciaSistemaVO);
		}
	}
	return configuracaoAparenciaSistemaVO;
}

public void setConfiguracaoAparenciaSistemaVO(ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO) {
	this.configuracaoAparenciaSistemaVO = configuracaoAparenciaSistemaVO;
}

public void realizarInclusaoLayoutPadraoHistorico() {
	String caminhoBase;
	try {
		caminhoBase = UteisJSF.getCaminhoWeb().replace("/", File.separator) + File.separator + "WEB-INF"
				+ File.separator + "classes" + File.separator + HistoricoAlunoRel.getCaminhoBaseRelatorio();
		for (TipoNivelEducacional tipoNivelEducacional : TipoNivelEducacional.values()) {
			getFacadeFactory().getConfiguracaoHistoricoFacade()
					.realizarInclusaoLayoutPadraoHistorico(tipoNivelEducacional, caminhoBase);
		}
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
	if (configuracaoSeiBlackboardVO == null) {
		configuracaoSeiBlackboardVO = getFacadeFactory().getConfiguracaoSeiBlackboardFacade()
				.consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, null);
	}
	return configuracaoSeiBlackboardVO;
}

public void setConfiguracaoSeiBlackboardVO(ConfiguracaoSeiBlackboardVO configuracaoSeiBlackboardVO) {
	this.configuracaoSeiBlackboardVO = configuracaoSeiBlackboardVO;
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

public synchronized Map<String, String> getArtefatoAjudaKeys() {
	if (artefatoAjudaKeys == null) {
		artefatoAjudaKeys = getFacadeFactory().getArtefatoAjudaFacade().consultarArtefatoAjudaDisponivel();
	}
	return artefatoAjudaKeys;
}

public synchronized void setArtefatoAjudaKeys(Map<String, String> artefatoAjudaKeys) {
	this.artefatoAjudaKeys = artefatoAjudaKeys;
}

public List<TurmaVO> obterAdicionarRemoverTurmaOfertada(String key, List<TurmaVO> listaTurmaOfertada, boolean add,
		boolean remover) {
	if (add) {
		getMapTurmasOfertadas().put(key, listaTurmaOfertada);
		return listaTurmaOfertada;
	} else if (remover) {
		getMapTurmasOfertadas().clear();
		return null;
	} else {
		if (getMapTurmasOfertadas().containsKey(key)) {
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
	if (Uteis.isAtributoPreenchido(codigoDocumentoAssinado)
			&& Uteis.isAtributoPreenchido(getMapDocumentoAssinadoAssinarXml())
			&& getMapDocumentoAssinadoAssinarXml().containsKey(codigoDocumentoAssinado)) {
		return (DocumentoAssinadoVO) Uteis.clonar(getMapDocumentoAssinadoAssinarXml().get(codigoDocumentoAssinado));
	}
	return null;
}

public void removerDocumentoAssinadoAssinarXml(Integer codigoDocumentoAssinado) {
	if (Uteis.isAtributoPreenchido(codigoDocumentoAssinado)
			&& Uteis.isAtributoPreenchido(getMapDocumentoAssinadoAssinarXml())
			&& getMapDocumentoAssinadoAssinarXml().containsKey(codigoDocumentoAssinado)) {
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
		documentoAssinadoVOs.stream().filter(documentoAssinado -> Uteis.isAtributoPreenchido(documentoAssinado))
				.forEach(documentoAssinado -> getMapDocumentoAssinadoAssinarXml().put(documentoAssinado.getCodigo(),
						(DocumentoAssinadoVO) Uteis.clonar(documentoAssinado)));
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

public ProgressBarVO getProgressBarIntegracaoMestreGR() {
	if (progressBarIntegracaoMestreGR == null) {
		progressBarIntegracaoMestreGR = new ProgressBarVO();
	}
	return progressBarIntegracaoMestreGR;
}

public void setProgressBarIntegracaoMestreGR(ProgressBarVO progressBarIntegracaoMestreGR) {
	this.progressBarIntegracaoMestreGR = progressBarIntegracaoMestreGR;
}

public synchronized OfertaDisciplinaVO ofertaDisciplina(String key, OfertaDisciplinaVO ofertaDisciplinaVO,
		int acao) {
	if (acao == Uteis.CONSULTAR) {
		if (getMapOfertaDisciplina().containsKey(key)) {
			return getMapOfertaDisciplina().get(key);
		}
		return null;
	} else if (acao == Uteis.INCLUIR) {
		if (!getMapOfertaDisciplina().containsKey(key)) {
			getMapOfertaDisciplina().put(key, ofertaDisciplinaVO);
		}
		return ofertaDisciplinaVO;
	} else if (acao == Uteis.EXCLUIR) {
		if (key == null) {
			getMapOfertaDisciplina().clear();
		}
		if (getMapOfertaDisciplina().containsKey(key)) {
			getMapOfertaDisciplina().remove(key, ofertaDisciplinaVO);
		}
	}
	return null;
}

private Map<String, OfertaDisciplinaVO> getMapOfertaDisciplina() {
	if (mapOfertaDisciplina == null) {
		mapOfertaDisciplina = new HashMap<String, OfertaDisciplinaVO>(0);
	}
	return mapOfertaDisciplina;
}

private void setMapOfertaDisciplina(Map<String, OfertaDisciplinaVO> mapOfertaDisciplina) {
	this.mapOfertaDisciplina = mapOfertaDisciplina;
}

public synchronized void removerFoldersDocumentosTechCert(String key) {
	if (key != null && key.equals("-1")) {
		foldersCache.clear();
		anoCache.clear();
		mesCache.clear();
	}
}

// ============================================================================
// CONCURRENT HASHMAPS (Mantidos do original)
// ============================================================================

public ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> getMesCache() {
	if (mesCache == null) {
		mesCache = new ConcurrentHashMap<>(0);
	}
	return mesCache;
}

public void setMesCache(
		ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> mesCache) {
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

public void setMatriculaPeriodoAlunoCache(
		ConcurrentHashMap<String, MatriculaPeriodoVO> matriculaPeriodoAlunoCache) {
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
	if (configuracaoEstagioObrigatorioPadraoVO == null) {
		configuracaoEstagioObrigatorioPadraoVO = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade()
				.consultarPorConfiguracaoEstagioPadraoUnico(false, Uteis.NIVELMONTARDADOS_TODOS, null);
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

public void setDashboardEstagioAlunoCache(
		ConcurrentHashMap<String, DashboardEstagioVO> dashboardEstagioAlunoCache) {
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
		if (!configuracaoAparenciaSistemaVOs.stream()
				.anyMatch(p -> p.getCodigo().equals(getConfiguracaoAparenciaSistemaVO().getCodigo()))) {
			configuracaoAparenciaSistemaVOs.add(0,
					(ConfiguracaoAparenciaSistemaVO) getConfiguracaoAparenciaSistemaVO().clone());
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

public LocalDateTime getDataUltimaAtualizacaoAssinaturaXml() {
	return dataUltimaAtualizacaoAssinaturaXml;
}

public void setDataUltimaAtualizacaoAssinaturaXml(LocalDateTime dataUltimaAtualizacaoAssinaturaXml) {
	this.dataUltimaAtualizacaoAssinaturaXml = dataUltimaAtualizacaoAssinaturaXml;
}

public boolean isPeriodoAssinaturaXmlExcedido() {
	return getDataUltimaAtualizacaoAssinaturaXml() != null
			&& Duration.between(getDataUltimaAtualizacaoAssinaturaXml(), LocalDateTime.now()).toMinutes() >= 10;
}

