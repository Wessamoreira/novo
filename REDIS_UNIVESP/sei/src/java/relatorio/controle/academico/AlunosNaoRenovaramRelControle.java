package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunosNaoRenovaramRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;


@Controller("AlunosNaoRenovaramRelControle")
@Scope("viewScope")
@Lazy
public class AlunosNaoRenovaramRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7112727457330342L;
	private UnidadeEnsinoVO unidadeEnsinoVO;	
	private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private Boolean desconsiderarAlunoPreMatriculado;
	private Boolean desconsiderarPossivelFormando;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Boolean trazerAlunosUltimoSemestre;

	private String tipoRelatorio;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private PeriodicidadeEnum periodicidade;
	private List<SelectItem> listaSelectItemPeriodicidade;
	

	public AlunosNaoRenovaramRelControle() throws Exception {		
		setMensagemID("msg_entre_prmconsulta");
	}

	@PostConstruct
	public void incializarDados() {
		montarListaSelectItemUnidadeEnsino();
		setAno(Uteis.getAnoDataAtual());
		setSemestre(Uteis.getSemestreAtual());
		consultarLayoutPadrao();
	}

	
	public void imprimirPDF() {
		List<AlunosNaoRenovaramRelVO> listaObjetos = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosNaoRenovaramRelControle", "Inicializando Geração de Relatório Alunos Não Renovaram", "Emitindo Relatório");			
			listaObjetos = getFacadeFactory().getAlunosNaoRenovaramRelFacade().criarObjeto(getUnidadeEnsinoVO(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getPeriodicidade(), getAno(), getSemestre(), getTipoRelatorio(), getDesconsiderarAlunoPreMatriculado(), getDesconsiderarPossivelFormando(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				if (tipoRelatorio.equals("AN")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosNaoRenovaramRelFacade().designIReportRelatorio());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosNaoRenovaramRelFacade().designIReportRelatorioSintetico());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosNaoRenovaramRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Não Renovaram Matrícula Comparado com o Período Anterior");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosNaoRenovaramRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

				getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());

				if (tipoRelatorio.equals("AN")) {
					getSuperParametroRelVO().setTipoRelatorio("Analítico");
				} else {
					getSuperParametroRelVO().setTipoRelatorio("Sintético");
				}

				if (!getCursosApresentar().trim().isEmpty()) {
					getSuperParametroRelVO().setCurso(getCursosApresentar());
				} else if(!Uteis.isAtributoPreenchido(getTurmaVO())) {
					getSuperParametroRelVO().setCurso("TODOS");
				}
				if (!getTurnosApresentar().trim().isEmpty()) {
					getSuperParametroRelVO().setTurno(getTurnosApresentar());
				} else if(!Uteis.isAtributoPreenchido(getTurmaVO())) {
					getSuperParametroRelVO().setTurno("TODOS");
				}
				if (!getTurmaVO().getIdentificadorTurma().equals("")) {
					getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
					getSuperParametroRelVO().setCurso(getTurmaVO().getCurso().getNome());
					getSuperParametroRelVO().setTurno(getTurmaVO().getTurno().getNome());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}	
				getSuperParametroRelVO().adicionarParametro("desconsiderarAlunoPreMatriculado", getDesconsiderarAlunoPreMatriculado()?"Sim":"Não");
				getSuperParametroRelVO().adicionarParametro("desconsiderarPossivelFormando", getDesconsiderarPossivelFormando()?"Sim":"Não");
				getSuperParametroRelVO().setAno(getAno());
				if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)){
					getSuperParametroRelVO().setSemestre(getSemestre());
				}else{
					getSuperParametroRelVO().setSemestre("");
				}
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
	    			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
	    			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
	    		}

				realizarImpressaoRelatorio();
				persistirLayoutPadrao();
				removerObjetoMemoria(this);
				incializarDados();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosNaoRenovaramRelControle", "Finalizando Geração de Relatório Alunos Não Renovaram", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);

		}

	}

	public void atualizarSemestre() {
		String x = getSemestre();
		setSemestre(x);
	}

	public List<SelectItem> listaSelectItemSemestre;
	public List<SelectItem> getListaSelectItemSemestre() {
		if(listaSelectItemSemestre == null){
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);		
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
			if(!getListaSelectItemUnidadeEnsino().isEmpty()){
				getUnidadeEnsinoVO().setCodigo((Integer)(getListaSelectItemUnidadeEnsino().get(0).getValue()));
				inicializarListaCursoETurno();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	
	public void consultarTurma() {
		try {
			getFacadeFactory().getAlunosNaoRenovaramRelFacade().validarDadosUnidaEnsino(getUnidadeEnsinoVO());	
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("IDENTIFICADORTURMA")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), 0, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {		
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			selecionarTurmaPorIdentificador(obj);	
			getListaConsultaTurma().clear();
	}

	public List<SelectItem> listaSelectItemTipoRelatorio;
	public List<SelectItem> getListaSelectItemTipoRelatorio() {
		if(listaSelectItemTipoRelatorio == null){
			listaSelectItemTipoRelatorio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoRelatorio.add(new SelectItem("AN", "Analítico"));
			listaSelectItemTipoRelatorio.add(new SelectItem("SI", "Sintético"));
		}
		return listaSelectItemTipoRelatorio;
	}

	
	public List<SelectItem> tipoConsultaComboTurma;
	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null){
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("IDENTIFICADORTURMA", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}	

	public void limparTurma() {
		try {
			setTurmaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public void setTrazerAlunosUltimoSemestre(Boolean trazerAlunosUltimoSemestre) {
		this.trazerAlunosUltimoSemestre = trazerAlunosUltimoSemestre;
	}

	public Boolean getTrazerAlunosUltimoSemestre() {
		if (trazerAlunosUltimoSemestre == null) {
			trazerAlunosUltimoSemestre = true;
		}
		return trazerAlunosUltimoSemestre;
	}	

	public Boolean getApresentarBotaoRelatorio() {
		return getIsApresentarAno() && !getAno().trim().isEmpty() &&
				(!getIsApresentarSemestre() || (getIsApresentarSemestre() && getSemestre().trim().isEmpty()));
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * @return the semestre
	 */
	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	/**
	 * @param semestre
	 *            the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "AN";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public boolean getApresentarAnoSemestre() {
		return getPeriodicidade().equals(PeriodicidadeEnum.ANUAL) || getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL);
	}

	public boolean getIsApresentarAno() {
		return getApresentarAnoSemestre();
	}

	public boolean getIsApresentarSemestre() {
		return getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL);
	}

	
	public Boolean getDesconsiderarAlunoPreMatriculado() {
		if (desconsiderarAlunoPreMatriculado == null) {
			desconsiderarAlunoPreMatriculado = Boolean.FALSE;
		}
		return desconsiderarAlunoPreMatriculado;
	}

	public void setDesconsiderarAlunoPreMatriculado(Boolean desconsiderarAlunoPreMatriculado) {
		this.desconsiderarAlunoPreMatriculado = desconsiderarAlunoPreMatriculado;
	}

	public PeriodicidadeEnum getPeriodicidade() {
		if(periodicidade == null){
			periodicidade = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}

	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if(listaSelectItemPeriodicidade == null){
			listaSelectItemPeriodicidade =  new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.ANUAL, PeriodicidadeEnum.ANUAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL, PeriodicidadeEnum.SEMESTRAL.getDescricao()));
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}
	
	public void inicializarListaCursoETurno(){		
		getUnidadeEnsinoVOs().clear();
		getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
		getUnidadeEnsinoVOs().add(getUnidadeEnsinoVO());
		setTurmaVO(null);
		consultarCursoFiltroRelatorio(getPeriodicidade().getValor());	
		consultarTurnoFiltroRelatorio();
	}
	
	public void consultarTurmaPorIdentificador(){
		selecionarTurmaPorIdentificador(getTurmaVO());
	}
	
	public void selecionarTurmaPorIdentificador(TurmaVO obj){
		try{
			setTurmaVO(obj);
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if(obj.getIntegral()){
				throw new Exception("Não é possível realizar a emissão deste relatório para turmas integrais.");
			}
			setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(getTurmaVO().getPeriodicidade()));
			limparCursos();		
			limparTurnos();
	
			setMensagemID("msg_dados_consultados");
		}catch(Exception e){
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private void persistirLayoutPadrao(){
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getPeriodicidade().getValor(), AlunosNaoRenovaramRelControle.class.getSimpleName(), "periodicidade", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(), AlunosNaoRenovaramRelControle.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(), AlunosNaoRenovaramRelControle.class.getSimpleName(), "semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoRelatorio(), AlunosNaoRenovaramRelControle.class.getSimpleName(), "tipoRelatorio", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getDesconsiderarAlunoPreMatriculado().toString(), AlunosNaoRenovaramRelControle.class.getSimpleName(), "desconsiderarAlunoPreMatriculado", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getDesconsiderarPossivelFormando().toString(), AlunosNaoRenovaramRelControle.class.getSimpleName(), "desconsiderarPossivelFormando", getUsuarioLogado());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void consultarLayoutPadrao(){
		Map<String, String> mapResult = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"periodicidade", "ano", "semestre", "tipoRelatorio", "desconsiderarPossivelFormando", "desconsiderarAlunoPreMatriculado"}, AlunosNaoRenovaramRelControle.class.getSimpleName());
		if(mapResult != null){
		if(mapResult.containsKey("periodicidade")){
			setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(mapResult.get("periodicidade")));
		}
		if(mapResult.containsKey("ano")){
			setAno(mapResult.get("ano"));
		}
		if(mapResult.containsKey("semestre")){
			setSemestre(mapResult.get("semestre"));
		}
		if(mapResult.containsKey("desconsiderarAlunoPreMatriculado")){
			setDesconsiderarAlunoPreMatriculado(Boolean.valueOf(mapResult.get("desconsiderarAlunoPreMatriculado")));
		}
		if(mapResult.containsKey("desconsiderarPossivelFormando")){
			setDesconsiderarPossivelFormando(Boolean.valueOf(mapResult.get("desconsiderarPossivelFormando")));
		}
		}
	}

	public Boolean getDesconsiderarPossivelFormando() {
		if(desconsiderarPossivelFormando == null){
			desconsiderarPossivelFormando = true;
		}
		return desconsiderarPossivelFormando;
	}

	public void setDesconsiderarPossivelFormando(Boolean desconsiderarPossivelFormando) {
		this.desconsiderarPossivelFormando = desconsiderarPossivelFormando;
	}
		

}