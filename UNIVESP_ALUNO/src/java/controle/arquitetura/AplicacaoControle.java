package controle.arquitetura;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
//import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaIntegralizacaoCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.OfertaDisciplinaVO;
//import negocio.comuns.academico.RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO;
//import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
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
//import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.HorarioTurmaInterfaceFacade;
import relatorio.negocio.jdbc.academico.HistoricoAlunoRel;

@Controller("AplicacaoControle")
@Scope("singleton")
@Lazy
public class AplicacaoControle {
	
	

	private List<ConfiguracaoGeralSistemaVO> listaConfiguracaoGeralSistemaVOAtiva = new ArrayList<ConfiguracaoGeralSistemaVO>();
	private Map<Integer, Integer> mapConfiguracaoGeralSistemaVOPorUnidadeEnsino = new HashMap<Integer, Integer>();
	public FraseInspiracaoVO fraseInspiracaoVO;
	private List<MapaControleAtividadesUsuarioVO> mapaControleUsuarios;
	private Boolean mapaControleAtividadesUsuariosAtivo;
	private MapaControleAtividadesUsuarioVO mapaControleAtividadesUsuarioVO;
	private String mensagem;
	private List<ControleAtividadeUsuarioVO> mapaAtividadesUsuarioPorEntidade;
	private List<ControleAtividadeUsuarioVO> mapaAtividadesUsuarioPorUsuario;
//	private Hashtable listaDataComemorativaApresentar;
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
	private Map<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademica = new HashMap<Integer, ConfiguracaoAcademicoVO>(
			0);
	@Autowired
	private FacadeFactory facadeFactory;
	public static List<Integer> turmaProgramacaoAula = new ArrayList<Integer>();
	private Boolean processandoProvaPresencial;
	private static Boolean processandoDocumentacaoGED;
	private Map<Integer, ProgressBarVO> mapThreadControleCobranca;
	private Map<String, List<TurmaVO>> mapTurmasOfertadas = new HashMap<String, List<TurmaVO>>(0);

	private Map<Integer, GradeCurricularVO> mapGradeCurricularVOs = new HashMap<Integer, GradeCurricularVO>(0);
	private Map<Integer, CidadeVO> mapCidadeVOs = new HashMap<Integer, CidadeVO>(0);
	private Map<Integer, PaizVO> mapPaisVOs = new HashMap<Integer, PaizVO>(0);
	private Map<Integer, EstadoVO> mapEstadoVOs = new HashMap<Integer, EstadoVO>(0);

	private Map<String, ProgressBarVO> mapThreadIndiceReajuste;
	private ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO;
	private Map<Integer, ConfiguracaoGEDVO> mapConfiguracaoGedUnidadeEnsino = new HashMap<Integer, ConfiguracaoGEDVO>(
			0);
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
//	private Map<Integer, ConfiguracaoDiplomaDigitalVO> mapConfiguracaoDiplomaDigital;
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
	
	
	 // CACHE L1 (RAM) - Relatórios Prontos
    private volatile List<ControleAtividadeUsuarioVO> cacheL1AtividadesEntidade;
    private volatile long cacheL1AtividadesEntidadeTime = 0;
    
    private volatile List<ControleAtividadeUsuarioVO> cacheL1AtividadesUsuario;
    private volatile long cacheL1AtividadesUsuarioTime = 0;
    
    private static final long TTL_MONITORAMENTO_MS = 60 * 1000; // 1 minuto de cache
    
   
    private final Map<Integer, MapaControleAtividadesUsuarioVO> indicePorCodigoUsuario = new ConcurrentHashMap<>();
    private final Map<String, MapaControleAtividadesUsuarioVO> indicePorSessao = new ConcurrentHashMap<>();
    
    
    
    // Locks
    private final Object lockMonitoramentoEntidade = new Object();
    private final Object lockMonitoramentoUsuario = new Object();
    private final Object lockRegistroAtividade = new Object();

	private LocalDateTime dataUltimaAtualizacaoAssinaturaXml;

	private volatile List cacheL1DataComemorativa;
	private volatile String cacheL1DataComemorativaKey;

	private final Object lockDataComemorativa = new Object();
	
	
	
	// Cache L1 para lista filtrada (por tipo de filtro)
		private final Map<String, List<MapaControleAtividadesUsuarioVO>> cacheL1ListaFiltrada = new ConcurrentHashMap<>();
		private final Map<String, Long> cacheL1ListaFiltradaTime = new ConcurrentHashMap<>();
		private static final long TTL_LISTA_FILTRADA_MS = 30 * 1000; // 30 segundos

		// Cache L1 para datas comemorativas
		private volatile Map<String, Object> cacheL1DatasComemorativas;
		private volatile long cacheL1DatasComemorativasTime = 0;
		private static final long TTL_DATAS_COMEMORATIVAS_MS = 60 * 60 * 1000; // 1 hora

		// Lista estática de tipos (imutável - carrega uma vez)
		private static final List<SelectItem> TIPOS_USUARIO_MONITORAMENTO;

		static {
		    // Inicialização estática - roda UMA vez quando a classe é carregada
		    List<SelectItem> tipos = new ArrayList<>(3);
		    tipos.add(new SelectItem("", ""));
		    tipos.add(new SelectItem("funcionario", "Funcionário"));
		    tipos.add(new SelectItem("aluno", "Aluno"));
		    TIPOS_USUARIO_MONITORAMENTO = Collections.unmodifiableList(tipos);
		}

		// Variáveis existentes (mantidas)
		private List<SelectItem> tipoUsuarioMonitoramento;

	
		private Map<String, Object> listaDataComemorativaApresentar; 
		
	
	
	
	@Autowired
    private RedisService redisService;

	public AplicacaoControle() {
		fraseInspiracaoVO = new FraseInspiracaoVO();
	}

	@PostConstruct
	public void realizacaoInicializacaoRotinaAplicacao() {
//		realizarRegistroProcessamentoInterrompidoArquivoRetorno();
		realizarReinicializacaoRotinaGeracaoManualParcela();
		realizarReinicializacaoRotinaRenovacaoMatriculaTurma();
		realizarReinicializacaoThreadAvaliacaoOnline();
//		realizarReinicializacaoThreadProvaProcessoSeletivo();
		getConfiguracaoAparenciaSistemaVO();
		realizarInclusaoLayoutPadraoHistorico();
//		realizarInclusaoLayoutPadraoAtaResultadosFinais();
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

//	public void limparRegistros() {
//		getMapaControleUsuarios().clear();
//		setMapaControleUsuarios(null);
//		setMapaControleUsuarios(new ArrayList(0));
//		getMapaAtividadesUsuarioPorEntidade().clear();
//		setMapaAtividadesUsuarioPorEntidade(null);
//		setMapaAtividadesUsuarioPorEntidade(new ArrayList(0));
//		setMensagem("Limpeza de Recursos Controle de Atividades Desativado!");
//	}

//	public void atualizar() {
//		montarListaMapaAtividadesUsuarioPorEntidade();
//		setMensagem("Atualização realizada com Sucesso!");
//	}
	
	
	/**
     * Limpa todos os registros de monitoramento.
     */
//    public void limparRegistros() {
//        
//        // 1. Limpa as listas em memória da instância atual
//        setMapaControleUsuarios(new ArrayList<>());
//        setMapaAtividadesUsuarioPorEntidade(new ArrayList<>());
//        setMapaAtividadesUsuarioPorUsuario(new ArrayList<>());
//        
//        // 2. Limpa os Índices de busca rápida (O(1))
//        indicePorCodigoUsuario.clear();
//        indicePorSessao.clear();
//
//        // 3. Invalida as variáveis de Cache L1 (RAM)
//        // Isso força o próximo 'get' a buscar do zero
//        invalidarCacheMonitoramento(); 
//        
//        // 4. Limpa o Cache L2 (Redis)
//        // Sênior Tip: Deletamos as chaves exatas em vez de usar scan/prefixo (mais performance)
//        try {
//            redisService.remover("app:monitoramento:atividades:por_entidade");
//            redisService.remover("app:monitoramento:atividades:por_usuario");
//            // Se você estiver salvando a lista bruta de sessões também:
//            redisService.remover("app:monitoramento:sessoes:ativas");
//        } catch (Exception e) {
//            System.err.println("Erro não-crítico ao limpar Redis: " + e.getMessage());
//        }
//        
//        setMensagem("Limpeza de Recursos realizada! Cache L1 e L2 limpos.");
//    }
	
	
	/**
     * Método auxiliar para invalidar apenas a memória RAM (L1)
     * Força o próximo 'get' a buscar do Redis ou recalcular.
     */
    public void invalidarCacheMonitoramento() {
        // Zera o cache de Entidades
        this.cacheL1AtividadesEntidade = null;
        this.cacheL1AtividadesEntidadeTime = 0;
        
        // Zera o cache de Usuários
        this.cacheL1AtividadesUsuario = null;
        this.cacheL1AtividadesUsuarioTime = 0;
        
        System.out.println(">>> L1 (RAM): Cache de monitoramento invalidado localmente.");
    }
    
    
    
    public void limparRegistros() {
        // 1. Limpa as listas em memória da instância atual (Legado)
        setMapaControleUsuarios(new ArrayList<>());
        setMapaAtividadesUsuarioPorEntidade(new ArrayList<>());
        setMapaAtividadesUsuarioPorUsuario(new ArrayList<>());
        
        // 2. Limpa os Índices de busca rápida O(1)
        indicePorCodigoUsuario.clear();
        indicePorSessao.clear();

        // 3. Invalida as variáveis de Cache L1 (RAM) - O MÉTODO NOVO ENTRA AQUI
        invalidarCacheMonitoramento(); 
        
        // 4. Limpa o Cache L2 (Redis)
        try {
            redisService.remover("app:monitoramento:atividades:por_entidade");
            redisService.remover("app:monitoramento:atividades:por_usuario");
            redisService.remover("app:monitoramento:sessoes:ativas");
        } catch (Exception e) {
            System.err.println("Erro não-crítico ao limpar Redis: " + e.getMessage());
        }
        
        setMensagem("Limpeza de Recursos realizada! Cache L1 e L2 limpos.");
    }

    public void atualizar() {
        // 1. Marca o cache L1 como sujo/expirado - O MÉTODO NOVO ENTRA AQUI
        invalidarCacheMonitoramento();
        
        // 2. Chama os métodos de montagem
        // Como o L1 está nulo, eles vão recalcular e atualizar tudo
        montarListaMapaAtividadesUsuarioPorEntidade();
        montarListaMapaAtividadesUsuarioPorUsuario();
        
        setMensagem("Atualização realizada com Sucesso!");
    }

  


public List obterListaDataComemorativaDataAtual(UsuarioVO usuarioLogado) throws Exception {
        
        // 1. Gera a chave baseada na data de hoje (Ex: app:datacomemorativa:2023-10-27)
        // Usamos String para garantir consistência no Redis e no Map
        Date dataAtual = Uteis.getDateSemHora(new Date());
        String dataString = Uteis.getData(dataAtual); // Ou formatador simples se Uteis não tiver
        String chaveRedis = "app:datacomemorativa:" + dataString;

        // --- CAMADA 1: CACHE L1 (RAM) ---
        // Verifica se temos uma lista na memória E se ela pertence à chave de hoje
        if (this.cacheL1DataComemorativa != null && 
            chaveRedis.equals(this.cacheL1DataComemorativaKey)) {
            return this.cacheL1DataComemorativa;
        }

        // --- CAMADA 2: REDIS (L2) ---
        try {
            // Tenta buscar a lista pronta do Redis (como ArrayList)
            // Nota: Como não sei o tipo exato do VO dentro da lista, uso ArrayList.class. 
            // Se souber (ex: DataComemorativaVO.class), use array [] como fizemos antes.
            List listaRedis = redisService.getObjeto(chaveRedis, ArrayList.class);
            
            if (listaRedis != null) {
                // Atualiza L1
                this.cacheL1DataComemorativa = listaRedis;
                this.cacheL1DataComemorativaKey = chaveRedis;
                return listaRedis;
            }
        } catch (Exception e) {
            // Log silencioso, segue para o banco
        }

        // --- CAMADA 3: BANCO DE DADOS (L3) ---
        synchronized (lockDataComemorativa) {
            
            // Double Check na RAM (caso outra thread tenha carregado agora)
            if (this.cacheL1DataComemorativa != null && 
                chaveRedis.equals(this.cacheL1DataComemorativaKey)) {
                return this.cacheL1DataComemorativa;
            }

            // Busca no Banco (Método Original)
            List listaBanco = getFacadeFactory().getDataComemorativaFacade()
                    .consultarDataComemorativaDataAtualTipoMensagemTopo();

            if (listaBanco == null) {
                listaBanco = new ArrayList();
            }

            // Atualiza Caches
            try {
                // Salva no Redis com TTL de 24h (Datas comemorativas valem pro dia todo)
                redisService.setObjeto(chaveRedis, listaBanco, 24, TimeUnit.HOURS);
            } catch (Exception e) {
                System.err.println("Erro ao salvar Data Comemorativa no Redis: " + e.getMessage());
            }

            // Salva na RAM
            this.cacheL1DataComemorativa = listaBanco;
            this.cacheL1DataComemorativaKey = chaveRedis;

            return listaBanco;
        }
    }
	private void montarFraseInspiricaoDoDia() {
		try {
			if (getFraseInspiracaoVO().getDataUltimaExibicao() == null) {
				setFraseInspiracaoVO(getFacadeFactory().getFraseInspiracaoFacade().consultarFraseRandom("%", false,
						Uteis.NIVELMONTARDADOS_TODOS, null));
				getFraseInspiracaoVO().setDataUltimaExibicao(new Date());
				// return getFraseInspiracaoVO().getFrase();
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
	 * @param fraseInspiracaoVO the fraseInspiracaoVO to set
	 */
	public void setFraseInspiracaoVO(FraseInspiracaoVO fraseInspiracaoVO) {
		this.fraseInspiracaoVO = fraseInspiracaoVO;
	}

	/**
	 * @return the mapaControleUsuarios
	 */
	public List<MapaControleAtividadesUsuarioVO> getMapaControleUsuarios() {
		if (mapaControleUsuarios == null) {
			mapaControleUsuarios = new ArrayList<>();
		}
		return mapaControleUsuarios;
	}
	
	
	
	 public void montarListaMapaAtividadesUsuarioPorEntidade() {
	        
	        // 1. CHECK L1 (RAM)
	        if (this.cacheL1AtividadesEntidade != null && 
	           (System.currentTimeMillis() - this.cacheL1AtividadesEntidadeTime < TTL_MONITORAMENTO_MS)) {
	            setMapaAtividadesUsuarioPorEntidade(new ArrayList<>(this.cacheL1AtividadesEntidade));
	            return;
	        }

	        String chaveRedis = "app:monitoramento:entidade";

	        // 2. CHECK L2 (REDIS)
	        try {
	            // Truque: Ler como Array para o Gson entender a lista
	            ControleAtividadeUsuarioVO[] array = redisService.getObjeto(chaveRedis, ControleAtividadeUsuarioVO[].class);
	            if (array != null) {
	                List<ControleAtividadeUsuarioVO> lista = new ArrayList<>(java.util.Arrays.asList(array));
	                
	                // Atualiza RAM
	                this.cacheL1AtividadesEntidade = lista;
	                this.cacheL1AtividadesEntidadeTime = System.currentTimeMillis();
	                
	                setMapaAtividadesUsuarioPorEntidade(lista);
	                return;
	            }
	        } catch (Exception e) { /* Segue para processamento */ }

	        // 3. PROCESSAMENTO PESADO (Sincronizado)
	        synchronized (lockMonitoramentoEntidade) {
	            // Double Check L1
	            if (this.cacheL1AtividadesEntidade != null && 
	               (System.currentTimeMillis() - this.cacheL1AtividadesEntidadeTime < TTL_MONITORAMENTO_MS)) {
	                setMapaAtividadesUsuarioPorEntidade(new ArrayList<>(this.cacheL1AtividadesEntidade));
	                return;
	            }

	            try {
	                List<ControleAtividadeUsuarioVO> novaLista = new ArrayList<>();
	                Map<String, ControleAtividadeUsuarioVO> hash = new HashMap<>();

	                List<MapaControleAtividadesUsuarioVO> base = getMapaControleUsuarios();
	                if (base != null) {
	                    for (MapaControleAtividadesUsuarioVO mapa : base) {
	                        if (mapa.getAtividadesUsuario() != null) {
	                            for (ControleAtividadeUsuarioVO contr : mapa.getAtividadesUsuario()) {
	                                String key = contr.getEntidade() + "-" + contr.getDescricao() + "-" + contr.getTipo();
	                                
	                                if (!hash.containsKey(key)) {
	                                
	                                    ControleAtividadeUsuarioVO clone = new ControleAtividadeUsuarioVO(contr);
	                                    
	                                    clone.setNrOperacoes(1);
	                                    hash.put(key, clone);
	                                }
	                                else {
	                                    ControleAtividadeUsuarioVO existente = hash.get(key);
	                                    existente.setMomento(contr.getMomento());
	                                    existente.setNrOperacoes(existente.getNrOperacoes() + 1);
	                                }
	                            }
	                        }
	                    }
	                    novaLista.addAll(hash.values());
	                }

	                // 4. Salva nos Caches
	                this.cacheL1AtividadesEntidade = novaLista;
	                this.cacheL1AtividadesEntidadeTime = System.currentTimeMillis();
	                
	                try {
	                    // Salva no Redis com TTL curto (1 min) pois é dado volátil
	                    redisService.setObjeto(chaveRedis, novaLista.toArray(), 1, TimeUnit.MINUTES);
	                } catch (Exception e) {}

	                setMapaAtividadesUsuarioPorEntidade(novaLista);

	            } catch (Exception e) {
	                setMapaAtividadesUsuarioPorEntidade(new ArrayList<>());
	            }
	        }
	    }
	

//	public void montarListaMapaAtividadesUsuarioPorEntidade() {
//		try {
//			setMapaAtividadesUsuarioPorEntidade(new ArrayList(0));
//			Hashtable hash = new Hashtable(0);
//			Iterator i = getMapaControleUsuarios().iterator();
//			while (i.hasNext()) {
//				MapaControleAtividadesUsuarioVO mapa = (MapaControleAtividadesUsuarioVO) i.next();
//				Iterator j = mapa.getAtividadesUsuario().iterator();
//				while (j.hasNext()) {
//					ControleAtividadeUsuarioVO contr = (ControleAtividadeUsuarioVO) j.next();
//					if (!hash.containsKey(contr.getEntidade() + "-" + contr.getDescricao() + "-" + contr.getTipo())) {
//						contr.setNrOperacoes(1);
//						hash.put(contr.getEntidade() + "-" + contr.getDescricao() + "-" + contr.getTipo(), contr);
//					} else {
//						ControleAtividadeUsuarioVO controle = (ControleAtividadeUsuarioVO) hash
//								.get(contr.getEntidade() + "-" + contr.getDescricao() + "-" + contr.getTipo());
//						controle.setMomento(contr.getMomento());
//						controle.setNrOperacoes(controle.getNrOperacoes() + 1);
//					}
//				}
//			}
//			Enumeration e = hash.keys();
//			while (e.hasMoreElements()) {
//				String controle = (String) e.nextElement();
//				ControleAtividadeUsuarioVO c = (ControleAtividadeUsuarioVO) hash.get(controle);
//				getMapaAtividadesUsuarioPorEntidade().add(c);
//			}
//		} catch (Exception e) {
//			setMapaAtividadesUsuarioPorEntidade(new ArrayList<ControleAtividadeUsuarioVO>(0));
//		}
//	}
//
//	public void montarListaMapaAtividadesUsuarioPorUsuario() {
//		try {
//			setMapaAtividadesUsuarioPorUsuario(new ArrayList(0));
//			Hashtable hash = new Hashtable(0);
//			Iterator i = getMapaAtividadesUsuarioPorEntidade().iterator();
//			while (i.hasNext()) {
//				ControleAtividadeUsuarioVO mapa = (ControleAtividadeUsuarioVO) i.next();
//				if (!hash.containsKey(mapa.getUsername())) {
//					hash.put(mapa.getUsername(), mapa);
//				}
//			}
//			Enumeration e = hash.keys();
//			while (e.hasMoreElements()) {
//				String controle = (String) e.nextElement();
//				ControleAtividadeUsuarioVO c = (ControleAtividadeUsuarioVO) hash.get(controle);
//				getMapaAtividadesUsuarioPorUsuario().add(c);
//			}
//		} catch (Exception e) {
//			setMapaAtividadesUsuarioPorUsuario(new ArrayList<ControleAtividadeUsuarioVO>(0));
//		}
//	}
//	
	
public void montarListaMapaAtividadesUsuarioPorUsuario() {
        
        // 1. CHECK L1 (RAM)
        if (this.cacheL1AtividadesUsuario != null && 
           (System.currentTimeMillis() - this.cacheL1AtividadesUsuarioTime < TTL_MONITORAMENTO_MS)) {
            setMapaAtividadesUsuarioPorUsuario(new ArrayList<>(this.cacheL1AtividadesUsuario));
            return;
        }

        String chaveRedis = "app:monitoramento:usuario";

        // 2. CHECK L2 (REDIS)
        try {
            ControleAtividadeUsuarioVO[] array = redisService.getObjeto(chaveRedis, ControleAtividadeUsuarioVO[].class);
            if (array != null) {
                List<ControleAtividadeUsuarioVO> lista = new ArrayList<>(java.util.Arrays.asList(array));
                this.cacheL1AtividadesUsuario = lista;
                this.cacheL1AtividadesUsuarioTime = System.currentTimeMillis();
                setMapaAtividadesUsuarioPorUsuario(lista);
                return;
            }
        } catch (Exception e) { }

        // 3. PROCESSAMENTO
        synchronized (lockMonitoramentoUsuario) {
            if (this.cacheL1AtividadesUsuario != null && 
               (System.currentTimeMillis() - this.cacheL1AtividadesUsuarioTime < TTL_MONITORAMENTO_MS)) {
                setMapaAtividadesUsuarioPorUsuario(new ArrayList<>(this.cacheL1AtividadesUsuario));
                return;
            }

            try {
                List<ControleAtividadeUsuarioVO> novaLista = new ArrayList<>();
                Map<String, ControleAtividadeUsuarioVO> hash = new HashMap<>();
                
                // Garante que a fonte está populada
                if (getMapaAtividadesUsuarioPorEntidade().isEmpty()) {
                    montarListaMapaAtividadesUsuarioPorEntidade();
                }
                
                for (ControleAtividadeUsuarioVO mapa : getMapaAtividadesUsuarioPorEntidade()) {
                    if (!hash.containsKey(mapa.getUsername())) {
                        hash.put(mapa.getUsername(), mapa);
                    }
                }
                
                novaLista.addAll(hash.values());

                // Salva caches
                this.cacheL1AtividadesUsuario = novaLista;
                this.cacheL1AtividadesUsuarioTime = System.currentTimeMillis();
                
                try {
                    redisService.setObjeto(chaveRedis, novaLista.toArray(), 1, TimeUnit.MINUTES);
                } catch (Exception e) {}

                setMapaAtividadesUsuarioPorUsuario(novaLista);

            } catch (Exception e) {
                setMapaAtividadesUsuarioPorUsuario(new ArrayList<>());
            }
        }
    }

	public List<ControleAtividadeUsuarioVO> getMapaAtividadesUsuarioPorEntidade() {
		if (mapaAtividadesUsuarioPorEntidade == null) {
			mapaAtividadesUsuarioPorEntidade = new ArrayList<ControleAtividadeUsuarioVO>(0);
		}
		return mapaAtividadesUsuarioPorEntidade;
	}

	/**
	 * @param mapaControleUsuarios the mapaControleUsuarios to set
	 */
	public void setMapaControleUsuarios(List<MapaControleAtividadesUsuarioVO> mapaControleUsuarios) {
		
		this.mapaControleUsuarios = mapaControleUsuarios;
		
		
		// manter velocidade
		
		indicePorCodigoUsuario.clear();
		indicePorSessao.clear();
		
		if(mapaControleUsuarios != null) {
			for(MapaControleAtividadesUsuarioVO mapa : mapaControleUsuarios) {
				if(mapa.getIdSessao() !=null) {
					indicePorSessao.put(mapa.getIdSessao(), mapa);
				}
				
				if(mapa.getUsuarioVO() != null) {
					indicePorCodigoUsuario.put(mapa.getUsuarioVO().getCodigo(), mapa);
				}
			}
		}
	}
	
	
	

	

//	public MapaControleAtividadesUsuarioVO obterMapaControleAtividadesUsuarioEspecifico(UsuarioVO usuarioVO,
//			String idSession) throws Exception {
//		Iterator<MapaControleAtividadesUsuarioVO> i = getMapaControleUsuarios().iterator();
//		while (i.hasNext()) {
//			MapaControleAtividadesUsuarioVO mapa = i.next();
//			if (mapa.getIdSessao().equals(idSession)) {
//				return mapa;
//			}
//		}
//		return null;
//	}
	
	
	 public MapaControleAtividadesUsuarioVO obterMapaControleAtividadesUsuarioEspecifico(UsuarioVO usuarioVO) throws Exception {
        if (usuarioVO == null || usuarioVO.getCodigo() == null) return null;
        
        // Busca Direta no Índice
        MapaControleAtividadesUsuarioVO mapa = indicePorCodigoUsuario.get(usuarioVO.getCodigo());
        
        // Fallback (Segurança): Se não achou no índice, varre a lista e corrige o índice
        if (mapa == null) {
            for (MapaControleAtividadesUsuarioVO m : getMapaControleUsuarios()) {
                if (m.getUsuarioVO().getCodigo().equals(usuarioVO.getCodigo())) {
                    indicePorCodigoUsuario.put(usuarioVO.getCodigo(), m); // Auto-cura
                    return m;
                }
            }
        }
        return mapa;
    }
	
	
	 public MapaControleAtividadesUsuarioVO obterMapaControleAtividadesUsuarioEspecifico(UsuarioVO usuarioVO, String idSession) throws Exception {
	        if (idSession == null) return null;

	        // Busca Direta no Índice
	        MapaControleAtividadesUsuarioVO mapa = indicePorSessao.get(idSession);

	        // Fallback
	        if (mapa == null) {
	            for (MapaControleAtividadesUsuarioVO m : getMapaControleUsuarios()) {
	                if (idSession.equals(m.getIdSessao())) {
	                    indicePorSessao.put(idSession, m); // Auto-cura
	                    return m;
	                }
	            }
	        }
	        return mapa;
	    }

	

	public void selecionarMapaUsuario() throws Exception {
		setMapaControleAtividadesUsuarioVO((MapaControleAtividadesUsuarioVO) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap().get("mapaUsuario"));
	}

	 public void registrarAtividadeUsuarioEmNivelAplicacao(UsuarioVO usuarioVO, String idSession, String entidade,
	            String descricao, String tipo) throws Exception {
	        
	        if (idSession == null) return;

	        // Tenta pegar rápido do índice
	        MapaControleAtividadesUsuarioVO mapa = indicePorSessao.get(idSession);

	        // Se não existe, cria com thread-safety
	        if (mapa == null) {
	            synchronized (lockRegistroAtividade) {
	                // Double check
	                mapa = indicePorSessao.get(idSession);
	                
	                if (mapa == null) {
	                    mapa = new MapaControleAtividadesUsuarioVO();
	                    mapa.setUsuarioVO(usuarioVO);
	                    mapa.setIdSessao(idSession);
	                    mapa.setDataInicioAtividade(new Date());
	                    
	                    // Registra a primeira atividade
	                    mapa.registrarNovaAtividade(usuarioVO.getUsername(), entidade, descricao, tipo);
	                    
	                    // Adiciona na lista e nos índices
	                    getMapaControleUsuarios().add(mapa);
	                    indicePorSessao.put(idSession, mapa);
	                    if (usuarioVO != null && usuarioVO.getCodigo() != null) {
	                        indicePorCodigoUsuario.put(usuarioVO.getCodigo(), mapa);
	                    }
	                    return;
	                }
	            }
	        }

	        // Se já existe, apenas registra a nova atividade
	        mapa.registrarNovaAtividade(usuarioVO.getUsername(), entidade, descricao, tipo);
	    }


	 public List<SelectItem> getTipoUsuarioMonitoramento() {
		    return TIPOS_USUARIO_MONITORAMENTO;
		}

//	public List getMapaControleAtividadeUsuarioFiltrada() {
//		List mapaControleFiltrado = new ArrayList(0);
//		for (MapaControleAtividadesUsuarioVO obj : getMapaControleUsuarios()) {
//			if (obj.getUsuarioVO().getPessoa().getFuncionario()
//					&& getFiltroMapaControleAtividade().equals("funcionario")) {
//				mapaControleFiltrado.add(obj);
//			} else if (obj.getUsuarioVO().getPessoa().getAluno() && getFiltroMapaControleAtividade().equals("aluno")) {
//				mapaControleFiltrado.add(obj);
//			}
//		}
//		montarListaMapaAtividadesUsuarioPorUsuario();
//		return mapaControleFiltrado;
//	}
	 
	 public List<MapaControleAtividadesUsuarioVO> getMapaControleAtividadeUsuarioFiltrada() {
		    String filtro = getFiltroMapaControleAtividade();
		    
		    // Se filtro vazio, retorna lista vazia
		    if (filtro == null || filtro.isEmpty()) {
		        return new ArrayList<>(0);
		    }
		    
		    String chaveCache = "filtro:" + filtro;
		    
		   
		    Long ultimaAtualizacao = cacheL1ListaFiltradaTime.get(chaveCache);
		    if (ultimaAtualizacao != null && 
		        (System.currentTimeMillis() - ultimaAtualizacao < TTL_LISTA_FILTRADA_MS)) {
		        
		        List<MapaControleAtividadesUsuarioVO> cached = cacheL1ListaFiltrada.get(chaveCache);
		        if (cached != null) {
		            return cached;
		        }
		    }
		    
		   
		    List<MapaControleAtividadesUsuarioVO> resultado = new ArrayList<>();
		    
		    List<MapaControleAtividadesUsuarioVO> fonte = getMapaControleUsuarios();
		    if (fonte != null) {
		        for (MapaControleAtividadesUsuarioVO obj : fonte) {
		            // Null-safety em toda a cadeia
		            if (obj == null || obj.getUsuarioVO() == null || 
		                obj.getUsuarioVO().getPessoa() == null) {
		                continue;
		            }
		            
		            Boolean isFuncionario = obj.getUsuarioVO().getPessoa().getFuncionario();
		            Boolean isAluno = obj.getUsuarioVO().getPessoa().getAluno();
		            
		            if ("funcionario".equals(filtro) && Boolean.TRUE.equals(isFuncionario)) {
		                resultado.add(obj);
		            } else if ("aluno".equals(filtro) && Boolean.TRUE.equals(isAluno)) {
		                resultado.add(obj);
		            }
		        }
		    }
		    
		    // Salva no cache L1
		    cacheL1ListaFiltrada.put(chaveCache, resultado);
		    cacheL1ListaFiltradaTime.put(chaveCache, System.currentTimeMillis());
		    
		    // Atualiza lista por usuário (mantém comportamento original)
		    montarListaMapaAtividadesUsuarioPorUsuario();
		    
		    return resultado;
		}
	 
	 
		public void invalidarCacheListaFiltrada() {
			cacheL1ListaFiltrada.clear();
			cacheL1ListaFiltradaTime.clear();
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
	 * @param mapaControleAtividadesUsuariosAtivo the
	 *                                            mapaControleAtividadesUsuariosAtivo
	 *                                            to set
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
	 * @param mapaControleAtividadesUsuarioVO the mapaControleAtividadesUsuarioVO to
	 *                                        set
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
	 * @param mensagem the mensagem to set
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * @param mapaAtividadesUsuarioPorEntidade the mapaAtividadesUsuarioPorEntidade
	 *                                         to set
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
	 * @param mapaAtividadesUsuarioPorUsuario the mapaAtividadesUsuarioPorUsuario to
	 *                                        set
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

//	public void setFiltroMapaControleAtividade(String filtroMapaControleAtividade) {
//		this.filtroMapaControleAtividade = filtroMapaControleAtividade;
//	}
		
	public void setFiltroMapaControleAtividade(String filtroMapaControleAtividade) {
	    // Se filtro mudou, invalida cache
	    if (!Objects.equals(this.filtroMapaControleAtividade, filtroMapaControleAtividade)) {
	        invalidarCacheListaFiltrada();
	    }
	    this.filtroMapaControleAtividade = filtroMapaControleAtividade;
	}
	/**
	 * @return the listaDataComemorativaApresentar
	 */
//	public Hashtable getListaDataComemorativaApresentar() {
//		if (listaDataComemorativaApresentar == null) {
//			listaDataComemorativaApresentar = new Hashtable(0);
//		}
//		return listaDataComemorativaApresentar;
//	}
	
	
public Map<String, Object> getListaDataComemorativaApresentar() {
	    
	    String chaveRedis = "app:datas:comemorativas";
	    
	  
	    if (cacheL1DatasComemorativas != null &&
	        (System.currentTimeMillis() - cacheL1DatasComemorativasTime < TTL_DATAS_COMEMORATIVAS_MS)) {
	        return cacheL1DatasComemorativas;
	    }
	    
	   
	    try {
	        @SuppressWarnings("unchecked")
	        Map<String, Object> cacheRedis = redisService.getObjeto(chaveRedis, Map.class);
	        
	        if (cacheRedis != null && !cacheRedis.isEmpty()) {
	            cacheL1DatasComemorativas = new ConcurrentHashMap<>(cacheRedis);
	            cacheL1DatasComemorativasTime = System.currentTimeMillis();
	            return cacheL1DatasComemorativas;
	        }
	    } catch (Exception e) {
	        // Segue para criar novo
	    }
	    
	    
	    if (listaDataComemorativaApresentar == null) {
	        listaDataComemorativaApresentar = new ConcurrentHashMap<>();
	    }
	    
	    // Salva em L1
	    cacheL1DatasComemorativas = listaDataComemorativaApresentar;
	    cacheL1DatasComemorativasTime = System.currentTimeMillis();
	    
	    return listaDataComemorativaApresentar;
	}


	/**
	 * @param listaDataComemorativaApresentar the listaDataComemorativaApresentar to
	 *                                        set
	 */


//	public void setListaDataComemorativaApresentar(Hashtable listaDataComemorativaApresentar) {
//		this.listaDataComemorativaApresentar = listaDataComemorativaApresentar;
//	}

	public void setListaDataComemorativaApresentar(Map<String, Object> listaDataComemorativaApresentar) {
		this.listaDataComemorativaApresentar = listaDataComemorativaApresentar;

		// Atualiza cache L1
		this.cacheL1DatasComemorativas = listaDataComemorativaApresentar;
		this.cacheL1DatasComemorativasTime = System.currentTimeMillis();

		// Atualiza cache L2 (Redis)
		try {
			redisService.setObjeto("app:datas:comemorativas", listaDataComemorativaApresentar, 6, TimeUnit.HOURS);
		} catch (Exception e) {
			// Não crítico
		}
	}
		
	
	@Deprecated
	public Hashtable<String, Object> getListaDataComemorativaApresentarLegacy() {
	    Map<String, Object> mapa = getListaDataComemorativaApresentar();
	    return new Hashtable<>(mapa);
	}

	/**
	 * @return the autenticacaoRealizada
	 */
	public Date getAutenticacaoRealizada() {
		return autenticacaoRealizada;
	}

	/**
	 * @param autenticacaoRealizada the autenticacaoRealizada to set
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
	 * @param geracaoParcelaExecutando the geracaoParcelaExecutando to set
	 */
	public synchronized void setGeracaoParcelaExecutando(Boolean geracaoParcelaExecutando) {
		this.geracaoParcelaExecutando = geracaoParcelaExecutando;
	}
	
	
	/**
	 * Invalida cache de datas comemorativas.
	 * Chamado pelo CacheInvalidationListener quando ADM atualizar.
	 */
	public void invalidarCacheDatasComemorativas() {
	    cacheL1DatasComemorativas = null;
	    cacheL1DatasComemorativasTime = 0;
	    
	    try {
	        redisService.remover("app:datas:comemorativas");
	    } catch (Exception e) {
	        
	    }
	    
	    System.out.println(" Cache de Datas Comemorativas INVALIDADO");
	}

//	public void realizarRegistroProcessamentoInterrompidoArquivoRetorno() {
//		try {
//			getFacadeFactory().getControleCobrancaFacade().realizarRegistroProcessamentoInterrompido();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void realizarReinicializacaoRotinaGeracaoManualParcela() {
//		try {
//			getFacadeFactory().getGeracaoManualParcelaFacade().realizarReinicializacaoGerarManualParcelas(this);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
//			getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendarioAtividadeMatriculasOndeSituacaoAvaliacaoOnlineMatriculaEmRealizacao();
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
//		try {
//			List<RenovacaoMatriculaTurmaVO> renovacaoMatriculaTurmaVOs = getFacadeFactory().getRenovacaoMatriculaTurmaFacade().consultarPorSituacao(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null, 0, 0);
//			for (RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO : renovacaoMatriculaTurmaVOs) {
//				List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
//				List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs = getFacadeFactory().getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade().consultarPorRenovacaoMatriculaTurma(renovacaoMatriculaTurmaVO.getCodigo(), null);
//				for (RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO rmtgdcVO : renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs) {
//					gradeDisciplinaCompostaVOs.add(rmtgdcVO.getGradeDisciplinaCompostaVO());
//				}
//				getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarInicializacaoThreadRenovacaoTurma(renovacaoMatriculaTurmaVO, this, renovacaoMatriculaTurmaVO.getResponsavel(), gradeDisciplinaCompostaVOs);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
	 * @param integracaoFinanceiraProcessando the integracaoFinanceiraProcessando to
	 *                                        set
	 */
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
	
	
	
    private volatile ConfiguracaoGeralSistemaVO cacheLocalConfig;
    private volatile long ultimaAtualizacaoConfig = 0;
    
    private static final long TEMPO_CACHE_LOCAL_MS = 5 * 60 * 1000; 
    
    private final Object lockConfiguracaoGeral = new Object();

    private ConfiguracaoGeralSistemaVO manterConfiguracaoGeralSistemaEmNivelAplicacao(
            ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, 
            Integer unidadeEnsino, 
            UsuarioVO usuarioLogado,
            boolean remover) throws Exception {

        Integer codUnidade = (unidadeEnsino == null) ? 0 : unidadeEnsino;
        String chaveRedis = "app:configuracaogeral:unidade:" + codUnidade;

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

        
        
        if (this.cacheLocalConfig != null && 
           (System.currentTimeMillis() - this.ultimaAtualizacaoConfig < TEMPO_CACHE_LOCAL_MS)) {
            return this.cacheLocalConfig; 
        }

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
                } catch (Exception e) { /* log */ }
                
                // Salva na RAM (L1)
                this.cacheLocalConfig = configBanco;
                this.ultimaAtualizacaoConfig = System.currentTimeMillis();
            }
            
            return configBanco;
        }
    }
    
    


    /**
     * Chamado pelo Listener quando o ADM altera a configuração
     */
    public void forcarLimpezaCacheLocalConfiguracao() {
        this.cacheLocalConfig = null;
        this.ultimaAtualizacaoConfig = 0;
        
        // O Redis já foi limpo pelo ADM, mas por garantia podemos forçar:
        // redisService.remover("app:configuracaogeral:unidade:0");
        
        System.out.println(">>> CACHE L1: Configuração Geral invalidada via Pub/Sub.");
    }
    
 

    public void invalidarCacheConfiguracaoGeral() {
        this.cacheLocalConfig = null;
        this.ultimaAtualizacaoConfig = 0;
        System.out.println(" Cache ConfiguracaoGeral INVALIDADO via Pub/Sub");
    }

    public void invalidarTodoCache() {
        this.cacheLocalConfig = null;
        this.ultimaAtualizacaoConfig = 0;
    }
	
	public synchronized ConfiguracaoAcademicoVO carregarDadosConfiguracaoAcademica(Integer codigoConfAcademico)
			throws Exception {
		return manterConfiguracaoAcademicoEmNivelAplicacao(codigoConfAcademico, false);
	}
	
	

	private synchronized ConfiguracaoAcademicoVO manterConfiguracaoAcademicoEmNivelAplicacao(
			Integer codigoConfAcademico, boolean remover) throws Exception {
		if (remover) {
			if (mapConfiguracaoAcademica.containsKey(codigoConfAcademico)) {
				return mapConfiguracaoAcademica.remove(codigoConfAcademico);
			}
		} else {
			if (!mapConfiguracaoAcademica.containsKey(codigoConfAcademico)) {
				if (Uteis.isAtributoPreenchido(codigoConfAcademico)) {
					mapConfiguracaoAcademica.put(codigoConfAcademico,
							getFacadeFactory().getConfiguracaoAcademicoFacade()
									.consultarPorChavePrimariaUnica(codigoConfAcademico, null));
				} else {
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

//	private synchronized ConfiguracaoFinanceiroVO manterConfiguracaoFinanceiraEmNivelAplicacao( Integer unidadeEnsino, boolean remover, Integer codigoConfiguracaoFinanceiro) throws Exception {
//		if(remover) {
//			if(Uteis.isAtributoPreenchido(configuracaoFinanceiroVO)) {
//			Iterator<ConfiguracaoFinanceiroVO> i = listaConfiguracaoFinanceiroVOAtiva.iterator();
//			int index = 0;
//			while (i.hasNext()) {
//				ConfiguracaoFinanceiroVO configuracaoVerificarVO = i.next();
//				if (Uteis.isAtributoPreenchido(configuracaoVerificarVO) && configuracaoVerificarVO.getCodigo().equals(configuracaoFinanceiroVO.getCodigo())) {					
//					listaConfiguracaoFinanceiroVOAtiva.remove(index).getListaConfiguracaoFinanceiroCartaoVO().forEach(confCartao -> removerConfiguracaoFinanceiroCartao(confCartao.getCodigo()));
//					return configuracaoVerificarVO;
//				}
//				index++;
//			}				
//			}
//			if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
//				if(mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino)) {
//					mapConfiguracaoFinanceiroUnidadeEnsino.remove(unidadeEnsino).getListaConfiguracaoFinanceiroCartaoVO().forEach(confCartao -> removerConfiguracaoFinanceiroCartao(confCartao.getCodigo()));				
//				}	
//			}
//			if(!Uteis.isAtributoPreenchido(configuracaoFinanceiroVO) && !Uteis.isAtributoPreenchido(unidadeEnsino)) {
//				listaConfiguracaoFinanceiroVOAtiva.clear();
//				mapConfiguracaoFinanceiroUnidadeEnsino.clear();
//				mapConfiguracaoFinanceiroCartaoVOs.clear();
//			}
//		}else {
//			if(Uteis.isAtributoPreenchido(codigoConfiguracaoFinanceiro)) {
//				
//				for(2: listaConfiguracaoFinanceiroVOAtiva) {
//					if(configuracaoFinanceiroVO2 != null && configuracaoFinanceiroVO2.getCodigo().equals(codigoConfiguracaoFinanceiro)) {
//						return 	configuracaoFinanceiroVO2;
//					}				
//				}				
//				configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorChavePrimariaUnica(codigo null);
//				listaConfiguracaoFinanceiroVOAtiva.add(configuracaoFinanceiroVO);		
//				return 	configuracaoFinanceiroVO;				
//			}else {
//				Integer codigoConfiguracaoFinanceira = 0;
//				if(mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino == null ? 0 : unidadeEnsino) && Uteis.isAtributoPreenchido(mapConfiguracaoFinanceiroUnidadeEnsino.get(unidadeEnsino == null ? 0 : unidadeEnsino))) {
//					codigoConfiguracaoFinanceira = mapConfiguracaoFinanceiroUnidadeEnsino.get(unidadeEnsino == null ? 0 : unidadeEnsino).getCodigo();
//				}		
//				if(Uteis.isAtributoPreenchido(codigoConfiguracaoFinanceira)) {
//					for(2: listaConfiguracaoFinanceiroVOAtiva) {
//						if(configuracaoFinanceiroVO2.getCodigo().equals(codigoConfiguracaoFinanceira)) {					
//							return configuracaoFinanceiroVO2;
//						}				
//					}
//				}
//			configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoNivelAplicacaoSerUsada(unidadeEnsino);
//			boolean existeAdd =  false;
//			for(2: listaConfiguracaoFinanceiroVOAtiva) {
//				if(configuracaoFinanceiroVO2 != null && configuracaoFinanceiroVO2.getCodigo().equals(configuracaoFinanceiroVO.getCodigo())) {
//					existeAdd = true;
//				}				
//			}
//			if(!existeAdd) {
//				listaConfiguracaoFinanceiroVOAtiva.add(configuracaoFinanceiroVO);				
//			}				
//			if(!mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino == null ? 0 : unidadeEnsino)) {
//				mapConfiguracaoFinanceiroUnidadeEnsino.put(unidadeEnsino == null ? 0 : unidadeEnsino);
//			}
//			}
//		}
//		return configuracaoFinanceiroVO;
//	}

//	public synchronized void removerConfiguracaoFinanceiraNoMapConfiguracaoUnidadeEnsino(Integer unidadeEnsino) throws Exception  {		
//		manterConfiguracaoFinanceiraEmNivelAplicacao(null, unidadeEnsino, true, null);
//	}

	public synchronized void liberarListaConfiguracaoGeralMemoria() throws Exception {
		manterConfiguracaoGeralSistemaEmNivelAplicacao(null, null, null, true);
	}

	public synchronized void liberarListaConfiguracaoAcademicaMemoria() {
		mapConfiguracaoAcademica.clear();
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

	public synchronized void liberarListaCidadeMemoria() {
		mapCidadeVOs.clear();
	}

//	public synchronized CidadeVO getCidadeVO(Integer codigoCidade, UsuarioVO usuario) throws Exception {
//		if (Uteis.isAtributoPreenchido(codigoCidade)) {
//			if (!mapCidadeVOs.containsKey(codigoCidade)
//					|| !Uteis.isAtributoPreenchido(mapCidadeVOs.get(codigoCidade))) {
//				CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorChavePrimariaUnica(codigoCidade,
//						false, usuario);
//				mapCidadeVOs.put(codigoCidade, cidadeVO);
//			}
//			return (CidadeVO) mapCidadeVOs.get(codigoCidade).clone();
//		} else {
//			return null;
//		}
//
//	}
	
	
	
	
//	public  CidadeVO getCidadeVO(Integer codigoCidade, UsuarioVO usuario) throws Exception {
//	    
//	    
//	    if (!Uteis.isAtributoPreenchido(codigoCidade)) {
//	        return null;
//	    }
//
//	    String chaveRedis = "app:cidade:" + codigoCidade;
//
//	    // 1) Tenta buscar direto no Redis
//	    CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
//	    
//	    if (cidadeRedis != null) {
//	    	
//	    	System.out.println(" Recuperei a cidade  do REDIS.");
//	        // Retorna direto. O GSON já criou um objeto novo, não precisa de clone().
//	        return cidadeRedis; 
//	    }
//
//	    // 2) (não estava no Redis), busca no banco
//	    CidadeVO cidadeBanco = getFacadeFactory()
//	            .getCidadeFacade()
//	            .consultarPorChavePrimariaUnica(codigoCidade, false, usuario);
//
//	    if (cidadeBanco != null) {
//	    	
//	    	System.out.println(">>> SALVO NO REDIS! Chave: " );
//	        // 3) IMPORTANTE: Grava no Redis para o próximo acesso ser rápido.
//	        // Adicionei um tempo de expiração (Ex: 24 horas) para evitar dados eternos.
//	        redisService.setObjeto(chaveRedis, cidadeBanco, 24, TimeUnit.HOURS);
//
//	        // Se o objeto do banco for singleton/cacheado no hibernate, 
//	        // aí sim o clone seria útil, mas geralmente não é necessário se vai apenas para a view.
//	        // Se quiser garantir segurança total:
//	        return (CidadeVO) cidadeBanco.clone();
//	    }
//
//	    // 4) Se banco também não tem, retorna null
//	    return null;
//	}
//	
	
	
//	public CidadeVO getCidadeVO(Integer codigoCidade, UsuarioVO usuario) throws Exception {
//	    
//	    // 1. Fail Fast: Validação rápida
//	    if (!Uteis.isAtributoPreenchido(codigoCidade)) {
//	        return null;
//	    }
//
//	    String chaveRedis = "app:cidade:" + codigoCidade;
//
//	    try {
//	        // 2. Tenta Redis (Sem synchronized!)
//	        // O Redis client (Jedis/Lettuce) já gerencia conexões concorrentes.
//	        CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
//	        
//	        if (cidadeRedis != null) {
//	            // DICA: Se o JSON foi desserializado agora, é um objeto novo.
//	            // O clone() aqui seria redundante, a menos que você altere esse objeto
//	            // dentro da camada de controle antes de enviar pra tela.
//	            return cidadeRedis; 
//	        }
//	    } catch (Exception e) {
//	        // DICA DE SÊNIOR: Se o Redis cair, o sistema NÃO PODE PARAR.
//	        // Loga o erro e deixa o fluxo seguir para o banco como fallback.
//	        System.err.println("Erro ao buscar no Redis, indo para o banco: " + e.getMessage());
//	    }
//
//	    // 3. Fallback: Busca no Banco
//	    CidadeVO cidadeBanco = getFacadeFactory()
//	            .getCidadeFacade()
//	            .consultarPorChavePrimariaUnica(codigoCidade, false, usuario);
//
//	    if (cidadeBanco != null) {
//	        try {
//	            // 4. Salva no Redis (Async é melhor, mas Sync é aceitável aqui)
//	            redisService.setObjeto(chaveRedis, cidadeBanco, 24, TimeUnit.HOURS);
//	        } catch (Exception e) {
//	             System.err.println("Erro ao salvar no Redis: " + e.getMessage());
//	        }
//
//	        // Aqui o clone() faz sentido se o objeto veio do Hibernate/JPA e
//	        // está "atachado" na sessão, para evitar alterações indesejadas.
//	        return (CidadeVO) cidadeBanco.clone();
//	    }
//
//	    return null;
//	}
	
//	
//	// Objeto de lock (pode ser o próprio 'this' se for Singleton, ou um objeto específico)
//	private final Object lockBuscaCidade = new Object();
//
//	public CidadeVO getCidadeVO(Integer codigoCidade, UsuarioVO usuario) throws Exception {
//	    
//	    if (!Uteis.isAtributoPreenchido(codigoCidade)) {
//	        return null;
//	    }
//
//	    String chaveRedis = "app:cidade:" + codigoCidade;
//
//	    
//	    try {
//	        CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
//	        if (cidadeRedis != null) {
//	            return cidadeRedis; 
//	        }
//	    } catch (Exception e) {
//	        System.err.println("Redis falhou (leitura), seguindo vida...");
//	    }
//
//	   
//	    // Aqui sincronizamos para evitar que muitos caem no banco ao mesmo tempo.
//	    synchronized (lockBuscaCidade) {
//	        
//	        
//	        // verificar se outro thread já salvou no Redis enquanto esperava o lock
//	        
//	        try {
//	            CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
//	            if (cidadeRedis != null) {
//	                // salvou no Redis enquanto esperava o lock
//	                return cidadeRedis; 
//	            }
//	        } catch (Exception e) { 
//	            // Ignora erro do redis aqui
//	        }
//
//	        // --- BUSCA REAL NO BANCO (Só 1 thread chega aqui por vez) ---
//	        CidadeVO cidadeBanco = getFacadeFactory()
//	                .getCidadeFacade()
//	                .consultarPorChavePrimariaUnica(codigoCidade, false, usuario);
//
//	        if (cidadeBanco != null) {
//	            try {
//	                // Salva para as proximas thrads
//	                redisService.setObjeto(chaveRedis, cidadeBanco, 24, TimeUnit.HOURS);
//	            } catch (Exception e) {
//	                 System.err.println("Erro ao salvar no Redis: " + e.getMessage());
//	            }
//	            return (CidadeVO) cidadeBanco.clone();
//	        }
//	    }
//
//	    return null;
//	}
//	
//	
//	
//	
	
	// ========================================================================
    // CACHE L1 PARA CIDADES (Mapas Concorrentes)
    // ========================================================================
    private ConcurrentHashMap<Integer, CidadeVO> cacheL1Cidades = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Long> cacheL1CidadesTime = new ConcurrentHashMap<>();
    
    // Objeto de lock para busca de cidades
    private final Object lockBuscaCidade = new Object();
    
    
    
public CidadeVO getCidadeVO(Integer codigoCidade, UsuarioVO usuario) throws Exception {
        
        // 1. Fail Fast
        if (!Uteis.isAtributoPreenchido(codigoCidade)) {
            return null;
        }

        // --- CAMADA 1: CACHE LOCAL (RAM - Instantâneo) ---
        // Verifica se temos na RAM e se o dado ainda é válido (menos de 5 min)
        if (cacheL1Cidades.containsKey(codigoCidade)) {
            Long timestamp = cacheL1CidadesTime.get(codigoCidade);
            if (timestamp != null && (System.currentTimeMillis() - timestamp < TEMPO_CACHE_LOCAL_MS)) {
                // IMPORTANTE: Retornar CLONE para evitar que alteração na tela suje o cache
                return (CidadeVO) cacheL1Cidades.get(codigoCidade).clone();
            } else {
                // Expirou: remove da RAM para forçar busca nova
                cacheL1Cidades.remove(codigoCidade);
                cacheL1CidadesTime.remove(codigoCidade);
            }
        }

        String chaveRedis = "app:cidade:" + codigoCidade;

        // --- CAMADA 2: REDIS (Rede - Rápido) ---
        try {
            CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
            if (cidadeRedis != null) {
                // Achou no Redis! Popula a RAM (L1) para as próximas chamadas
                atualizarCacheL1Cidade(codigoCidade, cidadeRedis);
                return cidadeRedis; // Gson já cria nova instância, não precisa clonar
            }
        } catch (Exception e) {
            // Se o Redis falhar, apenas logamos e seguimos para o banco
            // Não pare o sistema por causa de cache
        }

        // --- CAMADA 3: BANCO DE DADOS (Disco - Lento e Seguro) ---
        synchronized (lockBuscaCidade) {
            
            // Double Check: Verifica Redis de novo (alguém pode ter salvo enquanto esperávamos)
            try {
                CidadeVO cidadeRedis = redisService.getObjeto(chaveRedis, CidadeVO.class);
                if (cidadeRedis != null) {
                    atualizarCacheL1Cidade(codigoCidade, cidadeRedis);
                    return cidadeRedis;
                }
            } catch (Exception e) { }

            // Busca Real no Facade
            CidadeVO cidadeBanco = getFacadeFactory()
                    .getCidadeFacade()
                    .consultarPorChavePrimariaUnica(codigoCidade, false, usuario);

            if (cidadeBanco != null) {
                // 1. Salva no Redis (L2) com TTL de 24h
                try {
                    redisService.setObjeto(chaveRedis, cidadeBanco, 24, TimeUnit.HOURS);
                } catch (Exception e) {
                     System.err.println("Erro ao salvar Cidade no Redis: " + e.getMessage());
                }
                
                // 2. Salva na RAM (L1)
                atualizarCacheL1Cidade(codigoCidade, cidadeBanco);
                
                return (CidadeVO) cidadeBanco.clone();
            }
        }

        return null;
    }

    /**
     * Método auxiliar para atualizar o cache L1 de forma centralizada e segura
     */
    private void atualizarCacheL1Cidade(Integer codigo, CidadeVO cidade) {
        if (codigo != null && cidade != null) {
            try {
                // Guardamos um clone na RAM para garantir que ele fique "puro"
                // caso o original seja modificado em outro lugar antes de clonarmos na saída
                cacheL1Cidades.put(codigo, (CidadeVO) cidade.clone());
                cacheL1CidadesTime.put(codigo, System.currentTimeMillis());
            } catch (Exception e) {
                // Ignora erro de clone
            }
        }
    }
	
	

	
	
	public synchronized void removerCidade(Integer codigoCidade) throws Exception {
        // Limpa L1 (RAM)
        if (cacheL1Cidades.containsKey(codigoCidade)) {
            cacheL1Cidades.remove(codigoCidade);
            cacheL1CidadesTime.remove(codigoCidade);
        }
        
        // Limpa L2 (Redis)
        redisService.remover("app:cidade:" + codigoCidade);
        
        // Remove do mapa legado (se ainda usar)
        if (mapCidadeVOs.containsKey(codigoCidade)) {
            mapCidadeVOs.remove(codigoCidade);
        }
    }

//	public synchronized void realizarAtualizacaoConfiguracaoFinanceiraNoMapConfiguracaoUnidadeEnsino() {
//		for(Integer unidadeEnsino : mapConfiguracaoFinanceiroUnidadeEnsino.keySet()) {
//			if(mapConfiguracaoFinanceiroUnidadeEnsino.get(unidadeEnsino).getCodigo().equals(configuracaoFinanceiroVO.getCodigo())) {
//				mapConfiguracaoFinanceiroUnidadeEnsino.remove(unidadeEnsino);
//				mapConfiguracaoFinanceiroUnidadeEnsino.put(unidadeEnsino);
//			}
//		}
//	}

//	public synchronized void realizarConfiguracaoFinanceiraNoMapConfiguracaoUnidadeEnsino(Integer unidadeEnsino) {		
//		if(mapConfiguracaoFinanceiroUnidadeEnsino.containsKey(unidadeEnsino)) {
//			mapConfiguracaoFinanceiroUnidadeEnsino.remove(unidadeEnsino);				
//		}		
//	}

	public Map<String, ProgressBarVO> getMapThreadIndiceReajuste() {
		if (mapThreadIndiceReajuste == null) {
			mapThreadIndiceReajuste = new HashMap<String, ProgressBarVO>();
		}
		return mapThreadIndiceReajuste;
	}

	public void setMapThreadIndiceReajuste(Map<String, ProgressBarVO> mapThreadIndiceReajuste) {
		this.mapThreadIndiceReajuste = mapThreadIndiceReajuste;
	}

	/* Inicio Debug */
	public static final Integer LIMITE_CARACTERES_TEXTO_DEBUG = 2000000; // 2.000.000 caracteres correspondem a
																			// aproximadamente 4MB em memória.
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
	/* Fim Scripts */

//	public void realizarReinicializacaoThreadProvaProcessoSeletivo() {
//		try {
//			getFacadeFactory().getInscricaoFacade().consultarInscricaoProvaProcessoSeletivoEmRealizacao();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public synchronized ConfiguracaoGEDVO getConfiguracaoGEDPorUnidadeEnsino(Integer unidadeEnsino,
			UsuarioVO usuarioLogado) throws Exception {
		if (mapConfiguracaoGedUnidadeEnsino.containsKey(unidadeEnsino)) {
			return mapConfiguracaoGedUnidadeEnsino.get(unidadeEnsino);
		}
		ConfiguracaoGEDVO configuracaoGEDVO = getFacadeFactory().getConfiguracaoGEDFacade()
				.consultarPorUnidadeEnsinoUnica(unidadeEnsino, false, usuarioLogado);
		mapConfiguracaoGedUnidadeEnsino.put(unidadeEnsino, configuracaoGEDVO);
		return configuracaoGEDVO;
	}

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

	public synchronized void removerConfiguracaoGEDVOPorUnidadeEnsino(Integer unidadeEnsino) throws Exception {
		if (mapConfiguracaoGedUnidadeEnsino.containsKey(unidadeEnsino)) {
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

		if (mapConfiguracaoGEDVOs.containsKey(configuracaoGED)) {
			mapConfiguracaoGEDVOs.remove(configuracaoGED);
		}
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

	private Map<Integer, DisciplinaVO> mapDisciplinaVOs = new HashMap<Integer, DisciplinaVO>(0);
	private Map<Integer, UnidadeEnsinoVO> mapUnidadeEnsinoVOs = new HashMap<Integer, UnidadeEnsinoVO>(0);
	private Map<Integer, PerfilAcessoVO> mapPerfilAcessoVOs = new HashMap<Integer, PerfilAcessoVO>(0);

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

//	public void realizarInclusaoLayoutPadraoAtaResultadosFinais() {
//		String caminhoBase;
//		try {
//			caminhoBase = UteisJSF.getCaminhoWeb().replace("/", File.separator) + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + AtaResultadosFinaisRel.getCaminhoBaseRelatorio();
//				getFacadeFactory().getConfiguracaoAtaResultadosFinaisFacade().realizarInclusaoLayoutPadraoAtaResultadosFinais(caminhoBase);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

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

	public synchronized UnidadeEnsinoVO getUnidadeEnsinoVO(Integer codigoUnidadeEnsino, UsuarioVO usuario)
			throws Exception {
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			if (!mapUnidadeEnsinoVOs.containsKey(codigoUnidadeEnsino)
					|| !Uteis.isAtributoPreenchido(mapUnidadeEnsinoVOs.get(codigoUnidadeEnsino))) {
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade()
						.consultarPorChavePrimariaUnica(codigoUnidadeEnsino, usuario);
				mapUnidadeEnsinoVOs.put(codigoUnidadeEnsino, unidadeEnsinoVO);
			}
			return (UnidadeEnsinoVO) Uteis.clonar(mapUnidadeEnsinoVOs.get(codigoUnidadeEnsino));
		} else {
			return null;
		}

	}

	public synchronized void removerUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		if (mapUnidadeEnsinoVOs.containsKey(codigoUnidadeEnsino)) {
			mapUnidadeEnsinoVOs.remove(codigoUnidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino) && codigoUnidadeEnsino == -1) {
			mapUnidadeEnsinoVOs.clear();
		}
	}

	public synchronized PerfilAcessoVO getPerfilAcessoVO(Integer codigoPerfilAcesso, UsuarioVO usuario)
			throws Exception {
		if (Uteis.isAtributoPreenchido(codigoPerfilAcesso)) {
			if (!mapPerfilAcessoVOs.containsKey(codigoPerfilAcesso)
					|| !Uteis.isAtributoPreenchido(mapPerfilAcessoVOs.get(codigoPerfilAcesso))) {
				PerfilAcessoVO perfilAcessoVO = getFacadeFactory().getPerfilAcessoFacade()
						.consultarPorChavePrimariaUnica(codigoPerfilAcesso, false, Uteis.NIVELMONTARDADOS_TODOS,
								usuario);
				mapPerfilAcessoVOs.put(codigoPerfilAcesso, perfilAcessoVO);
			}
			return (PerfilAcessoVO) mapPerfilAcessoVOs.get(codigoPerfilAcesso).clone();
		} else {
			return null;
		}

	}

	public synchronized void removerPerfilAcesso(Integer codigoPerfilAcesso) throws Exception {
		if (mapPerfilAcessoVOs.containsKey(codigoPerfilAcesso)) {
			mapPerfilAcessoVOs.remove(codigoPerfilAcesso);
		}
		if (Uteis.isAtributoPreenchido(codigoPerfilAcesso) && codigoPerfilAcesso == -1) {
			mapPerfilAcessoVOs.clear();
		}
	}
	
	
	
//	
//	private final Object lockDisciplina = new Object();
//
//	public DisciplinaVO getDisciplinaVO(Integer codigoDisciplina, UsuarioVO usuario) throws Exception {
//	    
//	    
//	    if (!Uteis.isAtributoPreenchido(codigoDisciplina)) {
//	        return null;
//	    }
//
//	   
//	    String chaveRedis = "app:disciplina:" + codigoDisciplina;
//
//	    
//	    // Tenta pegar rápido. Se estiver lá, retorna em milissegundos.
//	    try {
//	        DisciplinaVO cache = redisService.getObjeto(chaveRedis, DisciplinaVO.class);
//	        if (cache != null) {
//	            return cache; 
//	        }
//	    } catch (Exception e) {
//	        
//	        System.err.println("Erro Redis (Consulta Otimista) - Disciplina: " + e.getMessage());
//	    }
//
//	  
//	    // Entra aqui apenas se não achou no Redis 
//	    synchronized (lockDisciplina) {
//
//	      
//	        // Verifica novamente se alguém preencheu o cache enquanto eu esperava na fila do lock
//	        try {
//	            DisciplinaVO cache = redisService.getObjeto(chaveRedis, DisciplinaVO.class);
//	            if (cache != null) {
//	                return cache;
//	            }
//	        } catch (Exception e) { /* Ignora erro no segundo check */ }
//
//	        //  Busca Pesada no Banco de Dados (Original) ---
//	        DisciplinaVO disciplinaBanco = getFacadeFactory()
//	                .getDisciplinaFacade()
//	                .consultarPorChavePrimariaUnica(codigoDisciplina, Uteis.NIVELMONTARDADOS_TODOS, usuario);
//
//	        // Salva no Redis e Retorna 
//	        if (disciplinaBanco != null) {
//	            try {
//	                // Salva no Redis com expiração de 24 horas (Tabelas de apoio mudam pouco)
//	                redisService.setObjeto(chaveRedis, disciplinaBanco, 24, TimeUnit.HOURS);
//	            } catch (Exception e) {
//	                System.err.println("Erro ao salvar Disciplina no Redis: " + e.getMessage());
//	            }
//
//	            // Mantemos o .clone() original para garantir segurança total com o código legado
//	            return (DisciplinaVO) disciplinaBanco.clone();
//	        }
//	    }
//
//	    return null;
//	}
//
//
//	public synchronized void removerDisciplina(Integer codigoDisciplina) throws Exception {
//		if (mapDisciplinaVOs.containsKey(codigoDisciplina)) {
//			mapDisciplinaVOs.remove(codigoDisciplina);
//		}
//		if (Uteis.isAtributoPreenchido(codigoDisciplina) && codigoDisciplina == -1) {
//			mapDisciplinaVOs.clear();
//		}
//	}
	
	
	
	
    private ConcurrentHashMap<Integer, DisciplinaVO> cacheL1Disciplinas = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Long> cacheL1DisciplinasTime = new ConcurrentHashMap<>();
    
    private final Object lockDisciplina = new Object();
    
    
    public DisciplinaVO getDisciplinaVO(Integer codigoDisciplina, UsuarioVO usuario) throws Exception {
        
       
        if (!Uteis.isAtributoPreenchido(codigoDisciplina)) {
            return null;
        }

       
        // Verifica se está na memória e se não expirou 
        if (cacheL1Disciplinas.containsKey(codigoDisciplina)) {
            Long timestamp = cacheL1DisciplinasTime.get(codigoDisciplina);
            // Verifica validade do cache 
            if (timestamp != null && (System.currentTimeMillis() - timestamp < TEMPO_CACHE_LOCAL_MS)) {
                
              
                try {
                    return (DisciplinaVO) cacheL1Disciplinas.get(codigoDisciplina).clone();
                } catch (Exception e) {
                    
                    return cacheL1Disciplinas.get(codigoDisciplina);
                }
            } else {
                // Expirou: remove da RAM para forçar refresh
                cacheL1Disciplinas.remove(codigoDisciplina);
                cacheL1DisciplinasTime.remove(codigoDisciplina);
            }
        }

        String chaveRedis = "app:disciplina:" + codigoDisciplina;

        // REDIS (Rede - Rápido) ---
        try {
            DisciplinaVO cache = redisService.getObjeto(chaveRedis, DisciplinaVO.class);
            if (cache != null) {
                // Achou no Redis! Sobe para a RAM (L1) 
                atualizarCacheL1Disciplina(codigoDisciplina, cache);
                return cache; 
            }
        } catch (Exception e) {
            
        }

        
        synchronized (lockDisciplina) {
            
            //  Alguém salvou no Redis enquanto eu esperava na fila do lock
            try {
                DisciplinaVO cache = redisService.getObjeto(chaveRedis, DisciplinaVO.class);
                if (cache != null) {
                    atualizarCacheL1Disciplina(codigoDisciplina, cache);
                    return cache;
                }
            } catch (Exception e) { }

            // Busca Real no Banco 
            DisciplinaVO disciplinaBanco = getFacadeFactory()
                    .getDisciplinaFacade()
                    .consultarPorChavePrimariaUnica(codigoDisciplina, Uteis.NIVELMONTARDADOS_TODOS, usuario);

            if (disciplinaBanco != null) {
                //  Salva no Redis (L2)
                try {
                    redisService.setObjeto(chaveRedis, disciplinaBanco, 24, TimeUnit.HOURS);
                } catch (Exception e) {
                    System.err.println("Erro ao salvar Disciplina no Redis: " + e.getMessage());
                }

                //  Salva na RAM (L1) 
                atualizarCacheL1Disciplina(codigoDisciplina, disciplinaBanco);

                // Retorna clone por segurança
                return (DisciplinaVO) disciplinaBanco.clone();
            }
        }

        return null;
    }

    /**
     * Helper para atualizar o Cache L1 de Disciplina de forma segura
     */
    private void atualizarCacheL1Disciplina(Integer codigo, DisciplinaVO disciplina) {
        if (codigo != null && disciplina != null) {
            try {
                
                cacheL1Disciplinas.put(codigo, (DisciplinaVO) disciplina.clone());
                cacheL1DisciplinasTime.put(codigo, System.currentTimeMillis());
            } catch (Exception e) {
                
            }
        }
    }
    
    public synchronized void removerDisciplina(Integer codigoDisciplina) throws Exception {
        // 1. Limpa Cache L1 (RAM)
        if (cacheL1Disciplinas.containsKey(codigoDisciplina)) {
            cacheL1Disciplinas.remove(codigoDisciplina);
            cacheL1DisciplinasTime.remove(codigoDisciplina);
        }

        // 2. Limpa Cache L2 (Redis)
        if (Uteis.isAtributoPreenchido(codigoDisciplina)) {
             redisService.remover("app:disciplina:" + codigoDisciplina);
        }

        // 3. Limpa Cache Legado (Se ainda existir uso)
        if (mapDisciplinaVOs.containsKey(codigoDisciplina)) {
            mapDisciplinaVOs.remove(codigoDisciplina);
        }
        
        // Limpeza geral (Flush All Disciplinas) - Caso passe -1 ou null
        if (Uteis.isAtributoPreenchido(codigoDisciplina) && codigoDisciplina == -1) {
            mapDisciplinaVOs.clear();
            cacheL1Disciplinas.clear();
            cacheL1DisciplinasTime.clear();
            
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

	private Map<Integer, TurnoVO> mapTurnoVOs = new HashMap<Integer, TurnoVO>(0);

	public synchronized TurnoVO getTurnoVO(Integer codigoTurno, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(codigoTurno)) {
			if (!mapTurnoVOs.containsKey(codigoTurno) || !Uteis.isAtributoPreenchido(mapTurnoVOs.get(codigoTurno))) {
				TurnoVO turnoVO = getFacadeFactory().getTurnoFacade().consultarPorChavePrimariaUnico(codigoTurno,
						Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapTurnoVOs.put(codigoTurno, turnoVO);
			}
			return (TurnoVO) mapTurnoVOs.get(codigoTurno).clone();
		} else {
			return null;
		}

	}

	public synchronized void removerTurno(Integer codigoTurno) throws Exception {
		if (mapTurnoVOs.containsKey(codigoTurno)) {
			mapTurnoVOs.remove(codigoTurno);
		}
		if (Uteis.isAtributoPreenchido(codigoTurno) && codigoTurno == -1) {
			mapTurnoVOs.clear();
		}
	}

	private Map<Integer, TipoDocumentoVO> mapTipoDocumentoVOs = new HashMap<Integer, TipoDocumentoVO>(0);

	public synchronized TipoDocumentoVO getTipoDocumentoVO(Integer codigo, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(codigo)) {
			if (!mapTipoDocumentoVOs.containsKey(codigo)
					|| !Uteis.isAtributoPreenchido(mapTipoDocumentoVOs.get(codigo))) {
				TipoDocumentoVO tipoDocumentoVO = getFacadeFactory().getTipoDeDocumentoFacade()
						.consultarPorChavePrimariaUnico(codigo, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				mapTipoDocumentoVOs.put(codigo, tipoDocumentoVO);
			}
			return (TipoDocumentoVO) mapTipoDocumentoVOs.get(codigo).clone();
		} else {
			return null;
		}

	}

	public synchronized void removerTipoDocumento(Integer codigo) throws Exception {
		if (mapTipoDocumentoVOs.containsKey(codigo)) {
			mapTipoDocumentoVOs.remove(codigo);
		}
		if (Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapTipoDocumentoVOs.clear();
		}
	}

	private Map<Integer, TipoAtividadeComplementarVO> mapTipoAtividadeComplementarVOs = new HashMap<Integer, TipoAtividadeComplementarVO>(
			0);

	public synchronized TipoAtividadeComplementarVO getTipoAtividadeComplementarVO(Integer codigo, UsuarioVO usuario)
			throws Exception {
		if (Uteis.isAtributoPreenchido(codigo)) {
			if (!mapTipoAtividadeComplementarVOs.containsKey(codigo)
					|| !Uteis.isAtributoPreenchido(mapTipoAtividadeComplementarVOs.get(codigo))) {
				TipoAtividadeComplementarVO TipoAtividadeComplementarVO = getFacadeFactory()
						.getTipoAtividadeComplementarFacade().consultarPorChavePrimariaUnico(codigo, usuario);
				mapTipoAtividadeComplementarVOs.put(codigo, TipoAtividadeComplementarVO);
			}
			return (TipoAtividadeComplementarVO) mapTipoAtividadeComplementarVOs.get(codigo).clone();
		} else {
			return null;
		}

	}

	public synchronized void removerTipoAtividadeComplementar(Integer codigo) throws Exception {
		if (mapTipoAtividadeComplementarVOs.containsKey(codigo)) {
			mapTipoAtividadeComplementarVOs.remove(codigo);
		}
		if (Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapTipoAtividadeComplementarVOs.clear();
		}
	}

	private Map<Integer, CategoriaGEDVO> mapCategoriaGEDVOs = new HashMap<Integer, CategoriaGEDVO>(0);

	public synchronized CategoriaGEDVO getCategoriaGEDVO(Integer codigo, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(codigo)) {
			if (!mapCategoriaGEDVOs.containsKey(codigo)
					|| !Uteis.isAtributoPreenchido(mapCategoriaGEDVOs.get(codigo))) {
				CategoriaGEDVO CategoriaGEDVO = getFacadeFactory().getCategoriaGEDInterfaceFacade()
						.consultarPorChavePrimariaUnico(codigo);
				mapCategoriaGEDVOs.put(codigo, CategoriaGEDVO);
			}
			return (CategoriaGEDVO) mapCategoriaGEDVOs.get(codigo).clone();
		} else {
			return null;
		}

	}

	public synchronized void removerCategoriaGED(Integer codigo) throws Exception {
		if (mapCategoriaGEDVOs.containsKey(codigo)) {
			mapCategoriaGEDVOs.remove(codigo);
		}
		if (Uteis.isAtributoPreenchido(codigo) && codigo == -1) {
			mapCategoriaGEDVOs.clear();
		}
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
//	
//	public Map<Integer, ConfiguracaoDiplomaDigitalVO> getMapConfiguracaoDiplomaDigital() {
//		if (mapConfiguracaoDiplomaDigital == null) {
//			mapConfiguracaoDiplomaDigital = new HashMap<>(0);
//		}
//		return mapConfiguracaoDiplomaDigital;
//	}
//	
//	public void setMapConfiguracaoDiplomaDigital(Map<Integer, ConfiguracaoDiplomaDigitalVO> mapConfiguracaoDiplomaDigital) {
//		this.mapConfiguracaoDiplomaDigital = mapConfiguracaoDiplomaDigital;
//	}
//	
//	public synchronized ConfiguracaoDiplomaDigitalVO getConfiguracaoDiplomaDigitalVO(Integer codigoConfiguracaoDiplomaDigital, UsuarioVO usuario) throws Exception {
//		if (Uteis.isAtributoPreenchido(codigoConfiguracaoDiplomaDigital)) {
//			if (!getMapConfiguracaoDiplomaDigital().containsKey(codigoConfiguracaoDiplomaDigital) || !Uteis.isAtributoPreenchido(getMapConfiguracaoDiplomaDigital().get(codigoConfiguracaoDiplomaDigital))) {
//				ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigitalVO = new ConfiguracaoDiplomaDigitalVO();
//				configuracaoDiplomaDigitalVO.setCodigo(codigoConfiguracaoDiplomaDigital);
//				getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().carregarDados(configuracaoDiplomaDigitalVO, usuario);
//				getMapConfiguracaoDiplomaDigital().put(codigoConfiguracaoDiplomaDigital, configuracaoDiplomaDigitalVO);
//			}
//			return (ConfiguracaoDiplomaDigitalVO) Uteis.clonar(getMapConfiguracaoDiplomaDigital().get(codigoConfiguracaoDiplomaDigital));
//		} else {
//			return null;
//		}
//	}
//	
//	public synchronized void removerConfiguracaoDiploma(Integer codigoConfiguracaoDiplomaDigital) throws Exception {
//		if(getMapConfiguracaoDiplomaDigital().containsKey(codigoConfiguracaoDiplomaDigital)) {
//			getMapConfiguracaoDiplomaDigital().remove(codigoConfiguracaoDiplomaDigital);
//		}
//		if(Uteis.isAtributoPreenchido(codigoConfiguracaoDiplomaDigital) && codigoConfiguracaoDiplomaDigital == -1) {
//			getMapConfiguracaoDiplomaDigital().clear();
//		}
//	}

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

	public synchronized ConfiguracaoGEDVO getConfiguracaoGEDVO(Integer codigo) throws Exception {
		if (Uteis.isAtributoPreenchido(codigo)) {
			if (!mapConfiguracaoGEDVOs.containsKey(codigo)
					|| !Uteis.isAtributoPreenchido(mapConfiguracaoGEDVOs.get(codigo))
					|| !mapConfiguracaoGEDVOs.get(codigo).getCodigo().equals(codigo)) {
				if (mapConfiguracaoGEDVOs.containsKey(codigo)) {
					mapConfiguracaoGEDVOs.remove(codigo);
				}
				ConfiguracaoGEDVO configuracaoGEDVO = new ConfiguracaoGEDVO();
				configuracaoGEDVO.setCodigo(codigo);
				getFacadeFactory().getConfiguracaoGEDFacade().carregarDados(configuracaoGEDVO, NivelMontarDados.TODOS,
						null);
				mapConfiguracaoGEDVOs.put(codigo, configuracaoGEDVO);
			}
			return mapConfiguracaoGEDVOs.get(codigo);
		} else {
			return null;
		}

	}

	public synchronized void removerPaizVO(Integer codigo) throws Exception {
		if (mapPaisVOs.containsKey(codigo)) {
			mapPaisVOs.remove(codigo);
		}
	}

	public synchronized PaizVO getPaizVO(Integer codigo) throws Exception {
		if (Uteis.isAtributoPreenchido(codigo)) {
			if (!mapPaisVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapPaisVOs.get(codigo))
					|| !mapPaisVOs.get(codigo).getCodigo().equals(codigo)) {
				if (mapPaisVOs.containsKey(codigo)) {
					mapPaisVOs.remove(codigo);
				}

				mapPaisVOs.put(codigo,
						getFacadeFactory().getPaizFacade().consultarPorChavePrimariaUnico(codigo, false, null));
			}
			return mapPaisVOs.get(codigo);
		} else {
			return null;
		}

	}

	public synchronized void removerEstadoVO(Integer codigo) throws Exception {
		if (mapEstadoVOs.containsKey(codigo)) {
			mapEstadoVOs.remove(codigo);
		}
	}

	public synchronized EstadoVO getEstadoVO(Integer codigo) throws Exception {
		if (Uteis.isAtributoPreenchido(codigo)) {
			if (!mapEstadoVOs.containsKey(codigo) || !Uteis.isAtributoPreenchido(mapEstadoVOs.get(codigo))
					|| !mapEstadoVOs.get(codigo).getCodigo().equals(codigo)) {
				if (mapEstadoVOs.containsKey(codigo)) {
					mapEstadoVOs.remove(codigo);
				}

				mapEstadoVOs.put(codigo, getFacadeFactory().getEstadoFacade().consultarPorChavePrimariaUnico(codigo,
						Uteis.NIVELMONTARDADOS_TODOS, null));
			}
			return mapEstadoVOs.get(codigo);
		} else {
			return null;
		}

	}

	public synchronized void removerGradeCurricularVO(Integer codigo) throws Exception {
		if (mapGradeCurricularVOs.containsKey(codigo)) {
			mapGradeCurricularVOs.remove(codigo);
		}
	}

	public synchronized GradeCurricularVO getGradeCurricularVO(Integer codigo) throws Exception {
		if (Uteis.isAtributoPreenchido(codigo)) {
			if (!mapGradeCurricularVOs.containsKey(codigo)
					|| !Uteis.isAtributoPreenchido(mapGradeCurricularVOs.get(codigo))
					|| !mapGradeCurricularVOs.get(codigo).getCodigo().equals(codigo)) {
				if (mapGradeCurricularVOs.containsKey(codigo)) {
					mapGradeCurricularVOs.remove(codigo);
				}

				mapGradeCurricularVOs.put(codigo, getFacadeFactory().getGradeCurricularFacade()
						.consultarPorChavePrimariaUnica(codigo, Uteis.NIVELMONTARDADOS_TODOS, null));
			}
			return (GradeCurricularVO) Uteis.clonar(mapGradeCurricularVOs.get(codigo));
		} else {
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
		return getDataUltimaAtualizacaoAssinaturaXml() != null
				&& Duration.between(getDataUltimaAtualizacaoAssinaturaXml(), LocalDateTime.now()).toMinutes() >= 10;
	}
	
	
	@Autowired
	private LocalCacheService l1Cache; 
	@Autowired
	private RedisService l2Cache;      

	public CidadeVO getCidadeVO(Integer id) throws Exception {
	    String chave = "app:cidade:" + id;

	    // 1. Tenta L1 (RAM) - Nanosegundos
	    CidadeVO l1 = l1Cache.get(chave);
	    if (l1 != null) return l1;

	    // 2. Tenta L2 (Redis) - Milissegundos
	    CidadeVO l2 = l2Cache.getObjeto(chave, CidadeVO.class);
	    if (l2 != null) {
	        l1Cache.put(chave, l2); // Popula L1
	        return l2;
	    }

	    // 3. Double Check Locking
	    synchronized (lockBuscaCidade) {
	        // Repete checks L1 e L2
	        CidadeVO l1Recheck = l1Cache.get(chave);
	        if (l1Recheck != null) return l1Recheck;

	        CidadeVO l2Recheck = l2Cache.getObjeto(chave, CidadeVO.class);
	        if (l2Recheck != null) {
	            l1Cache.put(chave, l2Recheck);
	            return l2Recheck;
	        }

	        // 4. Busca Banco
	        CidadeVO banco = getFacadeFactory().getCidadeFacade()
	                .consultarPorChavePrimariaUnica(id, false, null);

	        if (banco != null) {
	            l2Cache.setObjeto(chave, banco, 24, TimeUnit.HOURS); // Salva L2
	            l1Cache.put(chave, banco); // Salva L1
	            return banco;
	        }
	    }
	    return null;
	}

}
