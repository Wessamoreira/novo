package controle.arquitetura;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.faces. model.SelectItem;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.BloqueioFuncaoVO;
import negocio.comuns.arquitetura.CampoLogTriggerVO;
import negocio.comuns.arquitetura.ControleConcorrenciaHorarioTurmaVO;
import negocio.comuns.arquitetura.FuncaoBloqueadaVO;
import negocio.comuns.arquitetura.QueryAtivaLogTriggerVO;
import negocio.comuns.arquitetura.RegistroLogTriggerVO;
import negocio.comuns.arquitetura.TriggerVO;
import negocio.comuns.arquitetura.enumeradores.BloqueioFuncaoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * 
 * @author Alessandro Lima
 */
@Controller("LogTriggerControle")
@Scope("viewScope")
@Lazy
public class LogTriggerControle extends SuperControleRelatorio implements Serializable {
	

	private static final long serialVersionUID = 1L;

	public LogTriggerControle() {
		setControleConsulta(new ControleConsulta());
		inicializarTabelaLog();
		inicializarTriggers();
		inicializarUsuarios();
		inicializarTabelas();
		inicializarQuantidadeRegistrosPorPagina();
		buscarListaBloqueio();
		setDataInicial(Uteis.getDataPrimeiroDiaMes(new Date()));
		setDataFinal(Uteis.getDataUltimoDiaMes(new Date()));
	}

	private List<TriggerVO> listaTriggers;
	private TriggerVO trigger;
	private List<TriggerVO> tabelasLog;
	/* filtros */
	private Date dataInicial;
	private Date dataFinal;
	private List<SelectItem> listaUsuarios;
	private Integer usuarioLog;
	private List<SelectItem> listaTabelas;
	private String tabela;
	private List<SelectItem> listaQuantidadeRegistrosPorPagina;
	private List<SelectItem> listaMes;
	private List<SelectItem> listaAno;
	private Integer quantidadeRegistrosPorPagina;
	private List<CampoLogTriggerVO> campos;
	private Boolean filtroAdicional;
	private String mes;
	private String ano;
	private ProgressBarVO progressBarVO;
	
	/* registros */
	private List<RegistroLogTriggerVO> registros;
	private RegistroLogTriggerVO registro;
	private RegistroLogTriggerVO query;
	private List<RegistroLogTriggerVO> transacoes;
	private List<RegistroLogTriggerVO> querys;
	private List<List<CampoLogTriggerVO>> resultadoConsultaQuery;
	private String[] colunas;
	
	
	public void inicializarTabelaLog() {
		
		try {
			setTabelasLog(getFacadeFactory().getLogTriggerInterfaceFacade().consultarTabelaLog());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarTriggers() {
		
		try {
			setListaTriggers(getFacadeFactory().getLogTriggerInterfaceFacade().consultarTriggers());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarUsuarios() {
		
		try {
			setListaUsuarios(getFacadeFactory().getLogTriggerInterfaceFacade().consultarUsuarios());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarTabelas() {
		
		try {
			setListaTabelas(getFacadeFactory().getLogTriggerInterfaceFacade().consultarTabelas());
			getCampos().clear();
			limparRegistros();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarCampos() {

		try {
			getCampos().clear();
			String tabelaNome = "";
			getControleConsultaOtimizado().getListaConsulta().clear();
			for (SelectItem item : getListaTabelas()) {
				if (item.getValue().equals(getTabela())) {
					tabelaNome = item.getLabel();
					break;
				}
			}
			getFacadeFactory().getLogTriggerInterfaceFacade().montarCampos(getCampos(), tabelaNome);
			limparRegistros();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarCamposConsulta() {
		
		try {
			// TODO
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void limparRegistros() {
		
		getRegistros().clear();
	}
	
	public boolean getIsRegistrosVazio() {
		
		return getControleConsultaOtimizado().getListaConsulta().isEmpty();
	}
	
	public boolean getIsCamposVazio() {
		
		return getCampos().isEmpty();
	}
	
	public void inicializarQuantidadeRegistrosPorPagina() {
		
		try {
			getListaQuantidadeRegistrosPorPagina().clear();
	        getListaQuantidadeRegistrosPorPagina().add(new SelectItem(10, "10"));
	        getListaQuantidadeRegistrosPorPagina().add(new SelectItem(25, "25"));
	        getListaQuantidadeRegistrosPorPagina().add(new SelectItem(50, "50"));
	        getListaQuantidadeRegistrosPorPagina().add(new SelectItem(100, "100"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ativarTodasTriggersTabelas() {
		
		try {
			getFacadeFactory().getLogTriggerInterfaceFacade().ativarTodasTriggersTabelas(getListaTriggers());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", "Falha ao ativar todas as tabela!");
			e.printStackTrace();
		} finally {
			inicializarTriggers();
		}
	}
	
	public void desativarTodasTriggersTabelas() {
		
		try {
			getFacadeFactory().getLogTriggerInterfaceFacade().desativarTodasTriggersTabelas(getListaTriggers());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", "Falha ao desativar todas as tabela!");
			e.printStackTrace();
		} finally {
			inicializarTriggers();
		}
	}
	
	public void ativarDesativarTriggerTabela() {

		try {
			if (!getTrigger().getAtiva()) {
				getFacadeFactory().getLogTriggerInterfaceFacade().ativarTriggerTabela(getTrigger());
			} else {
				getFacadeFactory().getLogTriggerInterfaceFacade().desativarTriggerTabela(getTrigger());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", "Falha ao ativar ou desativar tabela!");
			e.printStackTrace();
		} finally {
			inicializarTriggers();
		}
	}
	
	public String consultar() {
		
		try {
			if (getCamposSelecionados().isEmpty()) {
				setRegistros(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultaRegistrosPorDataUsuarioTabela(getDataInicial(), getDataFinal(), getUsuarioLog(), getTabela(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			} else {
				setRegistros(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultaRegistrosLogTrigger(getTabela(), getDataInicial(), getDataFinal(), getUsuarioLog(), getCamposSelecionados(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
			}
			setMensagemID("msg_dados_consultados");
			return getMensagemDetalhada();
		} catch (Exception e) {
			limparRegistros();
			setMensagemDetalhada("msg_erro", "Falha ao realizar a consulta!");
			
			return "";
		}
	}
	
	public void scrollerListener() throws Exception {
		
		consultarOtimizado();
	}

	public String consultarOtimizado() throws Exception {
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setLimitePorPagina(getQuantidadeRegistrosPorPagina());
		try {
			if (getCamposSelecionados().isEmpty()) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultaRegistrosPorDataUsuarioTabela(getDataInicial(), getDataFinal(), getUsuarioLog(), getTabela(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultaTotalRegistroPorDataUsuarioTabela(getDataInicial(), getDataFinal(), getUsuarioLog(), getTabela()));
			} else {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultaRegistrosLogTrigger(getTabela(), getDataInicial(), getDataFinal(), getUsuarioLog(), getCamposSelecionados(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultaTotalRegistrosLogTrigger(getTabela(), getDataInicial(), getDataFinal(), getUsuarioLog(), getCamposSelecionados()));
				setFiltroAdicional(false);
			}
		     
			if(Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getListaConsulta())) {
				setMensagemID("msg_dados_consultados");
			}else {
				setMensagemID("msg_relatorio_vazio");
			}
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getControleConsultaOtimizado().getListaConsulta().clear();
			return "";
		}
	}
	
	public void consultarEvento() {
		
		try {
			if (getRegistro().getId().longValue() == 0l) {
				setMensagemDetalhada("msg_erro", "Falha ao consultar o evento!");
				return;
			}
			getRegistro().setCampos(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultaCamposEventoLogTrigger(getRegistro().getId(), getRegistro().getTabela(), getRegistro().getData()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getRegistro().getCampos().clear();
			setMensagemDetalhada("msg_erro", "Falha ao consultar o evento!");
			e.printStackTrace();
		}
	}
	
	public void consultarEvento2() {
		
		try {
			if (getRegistro().getTransaction().longValue() == 0l) {
				setMensagemDetalhada("msg_erro", "Falha ao consultar o eventos na mesma transação!");
				return;
			}
			getRegistro().getCampos().clear();
			setTransacoes(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultaRegistrosMesmaTransacao(getRegistro().getTransaction(), getRegistro().getData()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getRegistro().getCampos().clear();
			setMensagemDetalhada("msg_erro", "Falha ao consultar o evento!");
			e.printStackTrace();
		}
	}
	
	public List<CampoLogTriggerVO> getCamposSelecionados() {
		
		List<CampoLogTriggerVO> filtros = new ArrayList<CampoLogTriggerVO>(0);
		for (CampoLogTriggerVO campo : getCampos()) {
			if (campo.getSelecionado()) {
				filtros.add(campo);
			}
		}
		return filtros;
	}

	public int getQuantidadeCampos() {
		return getCampos().size();
	}
	
	public int getQuantidadeColunas() {
		if (getCampos().size() < 6) {
			return getCampos().size();
		} else {
			return 6;
		}
	}

	/* getters and setters */
	public TriggerVO getTrigger() {
		if (trigger == null) {
			trigger = new TriggerVO();
		}
		return trigger;
	}

	public void setTrigger(TriggerVO trigger) {
		this.trigger = trigger;
	}
	
	public List<TriggerVO> getListaTriggers() {
		if (listaTriggers == null) {
			listaTriggers = new ArrayList<TriggerVO>(0);
		}
		return listaTriggers;
	}

	public void setListaTriggers(List<TriggerVO> listaTriggers) {
		this.listaTriggers = listaTriggers;
	}
	
	public Date getDataInicial() {
		if(dataInicial == null){
			dataInicial = new Date();
		}
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		if(dataFinal == null){
			dataFinal = new Date();
		}
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public List<SelectItem> getListaUsuarios() {
		if (listaUsuarios == null) {
			listaUsuarios = new ArrayList<SelectItem>(0);
		}
		return listaUsuarios;
	}

	public void setListaUsuarios(List<SelectItem> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
	public Integer getUsuarioLog() {
		if (usuarioLog == null) {
			usuarioLog = 0;
		}
		return usuarioLog;
	}

	public void setUsuarioLog(Integer usuarioLog) {
		this.usuarioLog = usuarioLog;
	}
	
	public List<SelectItem> getListaTabelas() {
		if (listaTabelas == null) {
			listaTabelas = new ArrayList<SelectItem>(0);
		}
		return listaTabelas;
	}

	public void setListaTabelas(List<SelectItem> listaTabelas) {
		this.listaTabelas = listaTabelas;
	}
	
	public String getTabela() {
		if (tabela == null) {
			tabela = "";
		}
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public List<SelectItem> getListaQuantidadeRegistrosPorPagina() {
		if (listaQuantidadeRegistrosPorPagina == null) {
			listaQuantidadeRegistrosPorPagina = new ArrayList<SelectItem>(0);
		}
		return listaQuantidadeRegistrosPorPagina;
	}

	public void setListaQuantidadeRegistrosPorPagina(List<SelectItem> listaQuantidadeRegistrosPorPagina) {
		this.listaQuantidadeRegistrosPorPagina = listaQuantidadeRegistrosPorPagina;
	}
	
	public Integer getQuantidadeRegistrosPorPagina() {
		if (quantidadeRegistrosPorPagina == null) {
			quantidadeRegistrosPorPagina = 10;
		}
		return quantidadeRegistrosPorPagina;
	}

	public void setQuantidadeRegistrosPorPagina(Integer quantidadeRegistrosPorPagina) {
		this.quantidadeRegistrosPorPagina = quantidadeRegistrosPorPagina;
	}
	
	public List<CampoLogTriggerVO> getCampos() {
		if (campos == null) {
			campos = new ArrayList<CampoLogTriggerVO>(0);
		}
		return campos;
	}

	public void setCampos(List<CampoLogTriggerVO> campos) {
		this.campos = campos;
	}

	public List<RegistroLogTriggerVO> getRegistros() {
		if (registros == null) {
			registros = new ArrayList<RegistroLogTriggerVO>(0);
		}
		return registros;
	}

	public void setRegistros(List<RegistroLogTriggerVO> registros) {
		this.registros = registros;
	}
	
	public RegistroLogTriggerVO getRegistro() {
		if (registro == null) {
			registro = new RegistroLogTriggerVO();
		}
		return registro;
	}

	public void setRegistro(RegistroLogTriggerVO registro) {
		this.registro = registro;
	}

	public List<RegistroLogTriggerVO> getTransacoes() {
		if (transacoes == null) {
			transacoes = new ArrayList<RegistroLogTriggerVO>(0);
		}
		return transacoes;
	}

	public void setTransacoes(List<RegistroLogTriggerVO> transacoes) {
		this.transacoes = transacoes;
	}

	public List<TriggerVO> getTabelasLog() {
		if (tabelasLog == null) {
			tabelasLog = new ArrayList<TriggerVO>(0);
		}
		return tabelasLog;
	}

	public void setTabelasLog(List<TriggerVO> tabelasLog) {
		this.tabelasLog = tabelasLog;
	}

	/**
	 * @return the filtroAdicional
	 */
	public Boolean getFiltroAdicional() {
		if (filtroAdicional == null) {
			filtroAdicional = false;
		}
		return filtroAdicional;
	}

	/**
	 * @param filtroAdicional the filtroAdicional to set
	 */
	public void setFiltroAdicional(Boolean filtroAdicional) {
		this.filtroAdicional = filtroAdicional;
	}

	/**
	 * @return the mes
	 */
	public String getMes() {
		if (mes == null) {
			mes = "06";
		}
		return mes;
	}

	/**
	 * @param mes the mes to set
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = "2015";
		}
		return ano;
	}

	/**
	 * @param ano the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * @return the listaMes
	 */
	public List<SelectItem> getListaMes() {
		if (listaMes == null) {
			listaMes = new ArrayList<SelectItem>(0);
			listaMes.add(new SelectItem("01", "Janeiro"));
			listaMes.add(new SelectItem("02", "Fevereiro"));
			listaMes.add(new SelectItem("03", "Março"));
			listaMes.add(new SelectItem("04", "Abril"));
			listaMes.add(new SelectItem("05", "Maio"));
			listaMes.add(new SelectItem("06", "Junho"));
			listaMes.add(new SelectItem("07", "Julho"));
			listaMes.add(new SelectItem("08", "Agosto"));
			listaMes.add(new SelectItem("09", "Setembro"));
			listaMes.add(new SelectItem("10", "Outubro"));
			listaMes.add(new SelectItem("11", "Novembro"));
			listaMes.add(new SelectItem("12", "Dezembro"));
		}
		return listaMes;
	}

	/**
	 * @param listaMes the listaMes to set
	 */
	public void setListaMes(List<SelectItem> listaMes) {
		this.listaMes = listaMes;
	}

	/**
	 * @return the listaAno
	 */
	public List<SelectItem> getListaAno() {
		if (listaAno == null) {
			listaAno = new ArrayList<SelectItem>(0);
			listaAno.add(new SelectItem("2014", "2014"));
			listaAno.add(new SelectItem("2015", "2015"));
		}
		return listaAno;
	}

	/**
	 * @param listaAno the listaAno to set
	 */
	public void setListaAno(List<SelectItem> listaAno) {
		this.listaAno = listaAno;
	}
	
	public void realizarInicioProgressBar(){
		setProgressBarVO(new ProgressBarVO());		
		Date dataFim;
		try {
			dataFim = Uteis.getDataUltimoDiaMes(Uteis.getData(getAno() + "/" + getMes() + "/01", "yyyy/MM/dd"));		
			getProgressBarVO().setMaxValue(Uteis.getDiaMesData(dataFim));
			getProgressBarVO().setProgresso(1l);
			getProgressBarVO().setAtivado(true);
			getProgressBarVO().setStatus("Iniciando extração de dados.");
		} catch (ParseException e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}
	
	public void realizarCriacaoTabelaLogMesAno(){
		try{
			Date horaInicio = new Date();
			getFacadeFactory().getLogTriggerInterfaceFacade().realizarCriacaoTabelaLogPorMesAno(getMes(), getAno(), getProgressBarVO());
			inicializarTabelaLog();
			setMensagemID("msg_dados_gravados");
			setMensagemDetalhada("msg_dados_gravados", "Tempo Processamento: "+Uteis.obterDiferencaHorasDuasDatas(horaInicio, new Date()));
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			getProgressBarVO().setAtivado(false);
			getProgressBarVO().setProgresso(getProgressBarVO().getMaxValue().longValue());
		}
		
	}

	/**
	 * @return the progressBarVO
	 */
	public ProgressBarVO getProgressBarVO() {
		if (progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	/**
	 * @param progressBarVO the progressBarVO to set
	 */
	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	
	
	public void realizarConsultaQuery(){
		try{
			super.consultar();
			Map<String, Object> resultado = getFacadeFactory().getLogTriggerInterfaceFacade().realizarConsultaParametrizada(getQuery().getQuery(), 0, 0); 
			setResultadoConsultaQuery((List<List<CampoLogTriggerVO>>)resultado.get("LISTA"));
			setColunas((String[])resultado.get("COLUNAS"));
			
			setMensagemID("msg_dados_consultados");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
						
		}
	}
	
	public void irPaginaInicial() throws Exception {
		this.realizarConsultaQuery();
	}
	
	public void irPaginaAnterior() throws Exception {
		this.realizarConsultaQuery();
	}
	
	public void irPaginaPosterior() throws Exception {
		this.realizarConsultaQuery();
	}
	
	public void irPaginaFinal() throws Exception {
		this.realizarConsultaQuery();
	}
	
	public Integer getQtdeColunaResultadoQuery(){
		return getColunas() != null ? getColunas().length : 1;
	}
	
	public Integer getQtdeElementosResultadoQuery(){
		return getColunas() != null ? 10*getColunas().length : 1;
	}
	
	public void scrollerListener2() {
		
		realizarConsultaQuery();
	}

	
	/**
	 * @return the resultadoConsultaQuery
	 */
	public List<List<CampoLogTriggerVO>> getResultadoConsultaQuery() {
		if (resultadoConsultaQuery == null) {
			resultadoConsultaQuery = new ArrayList<List<CampoLogTriggerVO>>(0);
		}
		return resultadoConsultaQuery;
	}

	/**
	 * @param resultadoConsultaQuery the resultadoConsultaQuery to set
	 */
	public void setResultadoConsultaQuery(List<List<CampoLogTriggerVO>> resultadoConsultaQuery) {
		this.resultadoConsultaQuery = resultadoConsultaQuery;
	}

	public void consultarQuerys(){
		try{
			setQuerys(getFacadeFactory().getLogTriggerInterfaceFacade().consultarScripts());
			setMensagemID("msg_dados_consultados");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
						
		}
	}	
	
		
	
	public void limparQuery(){		
		setQuery(null);		
	}
	
	public void excluirQuery(){
		try{			
			getFacadeFactory().getLogTriggerInterfaceFacade().excluir(getQuery(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
						
		}
	}
	
	public void excluirQueryLista(){
		try{			
			getFacadeFactory().getLogTriggerInterfaceFacade().excluir((RegistroLogTriggerVO)getRequestMap().get("query"), getUsuarioLogado());
			consultarQuerys();
			setMensagemID("msg_dados_excluidos");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
						
		}
	}
	public void selecionarQuery(){
		try{			
			setQuery((RegistroLogTriggerVO)getRequestMap().get("query"));
			setMensagemID("msg_dados_selecionados");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			
		}
	}
	
	public void incluirQuery(){
		try{
			getQuery().setId(0l);
			getFacadeFactory().getLogTriggerInterfaceFacade().persistir(getQuery(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
						
		}
	}
	
	public void alterarQuery(){
		try{
			getFacadeFactory().getLogTriggerInterfaceFacade().persistir(getQuery(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			
		}
	}

	/**
	 * @return the query
	 */
	public RegistroLogTriggerVO getQuery() {
		if (query == null) {
			query = new RegistroLogTriggerVO();
		}
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(RegistroLogTriggerVO query) {
		this.query = query;
	}

	/**
	 * @return the querys
	 */
	public List<RegistroLogTriggerVO> getQuerys() {
		if (querys == null) {
			querys = new ArrayList<RegistroLogTriggerVO>(0);
		}
		return querys;
	}

	/**
	 * @param querys the querys to set
	 */
	public void setQuerys(List<RegistroLogTriggerVO> querys) {
		this.querys = querys;
	}

	/**
	 * @return the colunas
	 */
	public String[] getColunas() {		
		return colunas;
	}

	/**
	 * @param colunas the colunas to set
	 */
	public void setColunas(String[] colunas) {
		this.colunas = colunas;
	}

	
	public void realizarGeracaoRelatorio() {
		try {
			setFazerDownload(false);
			this.setCaminhoRelatorio(getFacadeFactory().getLogTriggerInterfaceFacade().realizarCriacaoArquivo(getQuery(), getLogoPadraoRelatorio(), getResultadoConsultaQuery(), getColunas(), getUsuarioLogado()));
			setFazerDownload(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarGeracaoRelatorioCsv() {
		try {
			setFazerDownload(false);
			this.setCaminhoRelatorio(getFacadeFactory().getLogTriggerInterfaceFacade().realizarCriacaoArquivoCsv(getQuery(), getLogoPadraoRelatorio(), getResultadoConsultaQuery(), getColunas(), getUsuarioLogado()));
			setFazerDownload(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
    public void buscarListaBloqueio() {
    	getBloqueioFuncaoVOs().clear();
		try {
			 if (AplicacaoControle.turmaProgramacaoAula.size() > 0) {
			    	getBloqueioFuncaoVOs().add(getFacadeFactory().getFuncaoBloqueadaFacade().montarListaTurmasBloqueada(AplicacaoControle.turmaProgramacaoAula, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			 }
		   
		    if (getAplicacaoControle().getConfNotaFiscalVOs().size() > 0) {
		    	getBloqueioFuncaoVOs().add(getFacadeFactory().getFuncaoBloqueadaFacade().montarListaConfiguracaoNotaFiscalBloqueada(getAplicacaoControle().getConfNotaFiscalVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		    }
		    if (getAplicacaoControle().getProcessandoProvaPresencial()) {
		    	getBloqueioFuncaoVOs().add(getFacadeFactory().getFuncaoBloqueadaFacade().montarProcessamentoProvaPresencial(getUsuarioLogado()));
		    }
		    if (getAplicacaoControle().getListaUnidadeEnsinoGeracaoParcela().size() > 0) {
		    	getBloqueioFuncaoVOs().add(getFacadeFactory().getFuncaoBloqueadaFacade().montarListaUnidadeEnsinoGeracaoParcela(getAplicacaoControle().getListaUnidadeEnsinoGeracaoParcela(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		    }	    
			getBloqueioFuncaoVOs().add(getFacadeFactory().getFuncaoBloqueadaFacade().montarListaTurmaRealizandoRenovacao(Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
		    setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public String revomerBloqueioTelaLotTrigger() {
	BloqueioFuncaoVO bloqueioFuncaoVO = (BloqueioFuncaoVO) context().getExternalContext().getRequestMap().get("bloqFuncao");
	FuncaoBloqueadaVO funcaoBloqueadaVO = (FuncaoBloqueadaVO) context().getExternalContext().getRequestMap().get("funcBloqueada");

	try {
	    if (bloqueioFuncaoVO.getBloqueioFuncaoEnum().equals(BloqueioFuncaoEnum.BLOQUEIO_TURMA_PROGRAMACAO_AULA)) {
	    		TurmaVO turmaVO =  new TurmaVO();
	    		turmaVO.setCodigo(funcaoBloqueadaVO.getCodigo());
	    		AplicacaoControle.executarBloqueioTurmaProgramacaoAula(turmaVO, null, "", "", false, false, null, null);

	    } else if (bloqueioFuncaoVO.getBloqueioFuncaoEnum().equals(BloqueioFuncaoEnum.BLOQUEIO_NOTA_FISCAL)) {
	    	getAplicacaoControle().executarEnvioNotasFiscais(funcaoBloqueadaVO.getCodigo(), false, true, false);
	    } 
//	    else if (bloqueioFuncaoVO.getBloqueioFuncaoEnum().equals(BloqueioFuncaoEnum.BLOQUEIO_PROFESSOR_TURMA_PROGRAMACAO_AULA)) {
//	    	ControleConcorrencia.removerProfessorListaProfessorHorario(funcaoBloqueadaVO.getCodigo());
//	    } 
	    else if (bloqueioFuncaoVO.getBloqueioFuncaoEnum().equals(BloqueioFuncaoEnum.BLOQUEIO_PROCESSAMENTO_RROVA_PRESENCIAL)) {
	    	getAplicacaoControle().executarBloqueioProcessamentoArquivoProvaPresencial(false, false);
	    } else if (bloqueioFuncaoVO.getBloqueioFuncaoEnum().equals(BloqueioFuncaoEnum.BLOQUEIO_UNIDADE_ENSINO_GERACAO_PARCELA)) {
	    	getAplicacaoControle().realizarGeracaoManualParcela(funcaoBloqueadaVO.getCodigo(), false, true);
	    } else if (bloqueioFuncaoVO.getBloqueioFuncaoEnum().equals(BloqueioFuncaoEnum.BLOQUEIO_TURMA_REALIZANDO_RENOVACAO)) {
	    	getAplicacaoControle().executarBloqueioTurmaNaRenovacaoTurma(funcaoBloqueadaVO.getCodigo(), null, false, true, false, false);
	    }
	    buscarListaBloqueio();
	    return "";
	} catch (Exception e) {
	    setMensagemDetalhada("msg_erro", e.getMessage());
	    return "";
	}
    }

    private List<BloqueioFuncaoVO> bloqueioFuncaoVOs;

    public List<BloqueioFuncaoVO> getBloqueioFuncaoVOs() {
	if (bloqueioFuncaoVOs == null) {
	    bloqueioFuncaoVOs = new ArrayList<BloqueioFuncaoVO>();
	}
	return bloqueioFuncaoVOs;
    }

    public void setBloqueioFuncaoVOs(List<BloqueioFuncaoVO> bloqueioFuncaoVOs) {
	this.bloqueioFuncaoVOs = bloqueioFuncaoVOs;
    }
    
	private List<QueryAtivaLogTriggerVO> listaQueryAtivaVOs;

	public List<QueryAtivaLogTriggerVO> getListaQueryAtivaVOs() {
		if (listaQueryAtivaVOs == null) {
			listaQueryAtivaVOs = new ArrayList<QueryAtivaLogTriggerVO>();
		}
		return listaQueryAtivaVOs;
	}

	public void setListaQueryAtivaVOs(List<QueryAtivaLogTriggerVO> listaQueryAtivaVOs) {
		this.listaQueryAtivaVOs = listaQueryAtivaVOs;
	}

	public void consultarQueryAtivaLogTrigger() {
		 try {
			getListaQueryAtivaVOs().clear();
			setListaQueryAtivaVOs(getFacadeFactory().getLogTriggerInterfaceFacade().executarConsultarQueryAtivaLogTrigger());
			System.out.println(getListaQueryAtivaVOs().size());
			if(Uteis.isAtributoPreenchido(getListaQueryAtivaVOs())) {
				setMensagemID("msg_dados_consultados");
			}else {
				setMensagemID("msg_relatorio_vazio");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", "Falha ao consultar o evento!");
		}
	}
	
	
	public String cancelarQueryAtivaLogTrigger() {
	   try {
			QueryAtivaLogTriggerVO queryAtivaLogTriggerVO = (QueryAtivaLogTriggerVO) context().getExternalContext().getRequestMap().get("listaQueryLock");
			getFacadeFactory().getLogTriggerInterfaceFacade().realizarCancelamentoQueryAtivaLogTrigger(queryAtivaLogTriggerVO);
			getListaQueryAtivaVOs().clear();
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	

}