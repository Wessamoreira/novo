package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.EstatisticaProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;

@Controller("EstatisticaProcessoSeletivoRelControle")
@Lazy
@Scope("viewScope")
public class EstatisticaProcessoSeletivoRelControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2649854726112023331L;
	private String consultarPor;
	private String ano;
	private String semestre;
	private FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO;
	private Boolean marcarTodasSituacoesInscricao;
	/*private SituacaoInscricaoEnum situacaoInscricao;
	private String situacao;*/
	private ProcSeletivoVO procSeletivoVO;
    private List<SelectItem> listaSelectItemDataProva;
    private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
    private TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio;
    protected String valorConsultaProcSeletivo;
    protected String campoConsultaProcSeletivo;
    private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
    private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List<SelectItem> listaSelectItemOpcaoConsultaCurso;

    private Integer sala;
    protected List<ProcSeletivoVO> listaConsultaProcSeletivo;
    private List<SelectItem> listaSelectItemSalaProcSeletivo;
    
    private List<SelectItem> listaSelectItemTipoRelatorio;
    private List<SelectItem> tipoConsultaComboProcSeletivo;
    private List<SelectItem> tipoSelectItemGrupoDestinatario;
    private List<SelectItem> tipoSelectItemOrdenarPor;
    private String ordenarPor;
    private Boolean apresentarNomeCandidatoCaixaAlta;
    
    public void selecionarProcSeletivo() {
        try {
            ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
            setProcSeletivoVO(obj);
            if (obj.getTipoAvaliacaoProcessoSeletivo().equals(TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR)) {
            	getFiltroRelatorioProcessoSeletivoVO().setProcessoSeletivo(false);
            	getFiltroRelatorioProcessoSeletivoVO().setEnem(false);
            	getFiltroRelatorioProcessoSeletivoVO().setPortadorDiploma(false);
            	getFiltroRelatorioProcessoSeletivoVO().setTransferencia(false);
            }
            montarListaSelectItemDataProva();
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void selecionarConsultarPor() {
    	if (getConsultarPor().equals("AS")) {
    		setProcSeletivoVO(null);
    		setUnidadeEnsinoCurso(null);
    		setItemProcSeletivoDataProvaVO(null);
    	} else {
    		setAno("");
    		setSemestre("");
    	}
    }
    
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("PS", "Processo Seletivo"));
        itens.add(new SelectItem("AS", "Ano/Semestre ou Ano"));
        return itens;
    }

    public void limparProcessoSeletivo(){
    	setProcSeletivoVO(null);
    	getListaSelectItemDataProva().clear();
    	getListaSelectItemSalaProcSeletivo().clear();
    	getFiltroRelatorioProcessoSeletivoVO().setProcessoSeletivo(false);
    	getFiltroRelatorioProcessoSeletivoVO().setEnem(false);
    	getFiltroRelatorioProcessoSeletivoVO().setPortadorDiploma(false);
    	getFiltroRelatorioProcessoSeletivoVO().setTransferencia(false);
    	setUnidadeEnsinoCurso(null);
    }

    public void montarListaSelectItemDataProva() throws Exception {

        List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        getListaSelectItemDataProva().clear();
        getListaSelectItemSalaProcSeletivo().clear();  
        setSala(0);
        getListaSelectItemDataProva().add(new SelectItem(0, ""));
        for (ItemProcSeletivoDataProvaVO obj : itemProcSeletivoDataProvaVOs) {
            getListaSelectItemDataProva().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
        }

    }
    
    public void montarListaUltimosProcSeletivos() {
    	try {
			setListaConsultaProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList<ProcSeletivoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>DisciplinaProcSeletivo</code>.
     */
    public void montarListaSelectItemSalaProcSeletivo() throws Exception {
    	setSala(0);
        List<SalaLocalAulaVO> resultadoConsulta = getFacadeFactory().getInscricaoFacade().consultarSalaPorProcessoSeletivoEDataAula(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo());
        if (!resultadoConsulta.isEmpty()) {
        	getListaSelectItemSalaProcSeletivo().clear();        	
        	getListaSelectItemSalaProcSeletivo().add(new SelectItem(0, "Todas as Sala"));
        	for (SalaLocalAulaVO sala : resultadoConsulta) {        		
        		getListaSelectItemSalaProcSeletivo().add(new SelectItem(sala.getCodigo(), sala.getLocalAula().getLocal() +" - "+ sala.getSala()));
        	}
        } else {
        	getListaSelectItemSalaProcSeletivo().clear();        	
        	getListaSelectItemSalaProcSeletivo().add(new SelectItem(0, "Sem Sala Cadastrada"));
        }
    }
    
    public void imprimirObjetoPDF() throws Exception {
    	imprimirObjeto(TipoRelatorioEnum.PDF);
    }

    public void imprimirObjeto(TipoRelatorioEnum tipoRelatorioEnum) throws Exception {
    	String caminho = "";
    	String design = "";
    	String titulo = "";
    	
    	@SuppressWarnings("rawtypes")
    	List listaRegistro = new ArrayList(0);
    	try {
    		if (getConsultarPor().equals("AS")) {
    			if (getAno().equals("")) {
    				throw new Exception("Para opção consultar por Ano/Semestre ou Ano, deve ser informado ao menos o ANO para emissão do relatório!");
    			}
    		} else {
    			if (getProcSeletivoVO().getCodigo().intValue() == 0) {
    				throw new Exception("Para opção consultar por Processo Seletivo, deve ser selecionado o PROCESSO SELETIVO para emissão do relatório!");
    			}
    		}
    		listaRegistro = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().consultarDadosGeracaoEstatistica(getTipoRelatorio(), getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSala(), getUnidadeEnsinoCurso().getCodigo(), getAno(), getSemestre(), getFiltroRelatorioProcessoSeletivoVO(), getOrdenarPor(), 0, 0, getApresentarNomeCandidatoCaixaAlta(), getConfiguracaoGeralPadraoSistema().getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(), getUsuarioLogado(), 0);
    		
    		titulo = getTipoRelatorio().getValorApresentar();
    		design = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().getDesignIReportRelatorio(getTipoRelatorio());
    		caminho = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().caminhoBaseIReportRelatorio();
    		
    		if (!listaRegistro.isEmpty()) {
    			getSuperParametroRelVO().setNomeDesignIreport(design);
    			getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
    			getSuperParametroRelVO().setSubReport_Dir(caminho);
    			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
    			getSuperParametroRelVO().setTituloRelatorio(titulo);
    			getSuperParametroRelVO().setListaObjetos(listaRegistro);
    			getSuperParametroRelVO().setCaminhoBaseRelatorio(caminho);
    			getSuperParametroRelVO().setNomeEmpresa("");
    			getSuperParametroRelVO().setVersaoSoftware("");
    			getSuperParametroRelVO().setFiltros("");
    			getSuperParametroRelVO().adicionarParametro("processoSeletivo", getProcSeletivoVO().getDescricao());
    			if(getUnidadeEnsinoCurso().getCodigo()>0){
    				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCurso", getUnidadeEnsinoCurso().getNomeUnidadeEnsino()+" - "+getUnidadeEnsinoCurso().getCurso().getNome()+"/"+getUnidadeEnsinoCurso().getTurno().getNome());
    			}else{
    				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCurso", "");     				
    			}
    			if(getItemProcSeletivoDataProvaVO().getCodigo() != null && getItemProcSeletivoDataProvaVO().getCodigo() > 0){
    				setItemProcSeletivoDataProvaVO(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(getItemProcSeletivoDataProvaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    				getSuperParametroRelVO().adicionarParametro("dataProva", getItemProcSeletivoDataProvaVO().getDataProva_Apresentar());
    			}else{
    				getSuperParametroRelVO().adicionarParametro("dataProva", "TODAS");
    			}
    			if(getSala() != null && getSala() > 0){
    				SalaLocalAulaVO salaLocalAulaVO = getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(getSala());
    				salaLocalAulaVO.setLocalAula(getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(salaLocalAulaVO.getLocalAula().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
    				getSuperParametroRelVO().adicionarParametro("sala", salaLocalAulaVO.getSala());
    				getSuperParametroRelVO().adicionarParametro("local", salaLocalAulaVO.getLocalAula().getLocal());
    			}else if(getSala() != null && getSala() < 0){    				
    				getSuperParametroRelVO().adicionarParametro("sala", "Sem Sala");
    				getSuperParametroRelVO().adicionarParametro("local", "Sem Local");
    			}else{
    				
    				getSuperParametroRelVO().adicionarParametro("sala", "TODAS");
    				getSuperParametroRelVO().adicionarParametro("local", "TODOS");
    			}
    			getSuperParametroRelVO().adicionarParametro("estatisticaCurso", getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_CURSO));
    			realizarImpressaoRelatorio();
    			setValorConsultaProcSeletivo("");
    			setCampoConsultaProcSeletivo("");
    			setMensagemID("msg_relatorio_ok");
    		} else {
    			setMensagemID("msg_relatorio_sem_dados");
    		}
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    		setFazerDownload(false);
    	} finally {
    		caminho = null;
    		design = null;
    		titulo = null;
    		
    		listaRegistro = null;
    	}
    }
    public void imprimirObjetoExcel() throws Exception {
    	imprimirObjeto(TipoRelatorioEnum.EXCEL);
    }
    
    public void consultarCurso(){
    	try{
    		getUnidadeEnsinoCursoVOs().clear();    		
    		setUnidadeEnsinoCursoVOs(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoProcSeletivo(getValorConsultaCurso(), getProcSeletivoVO().getCodigo(), getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
    		setMensagemID("msg_dados_consultados");
    	}catch(Exception e){
    		 setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }

    private List<SelectItem> listaSelectItemSituacaoInscricaoEnum;
    public List getListaSelectItemSituacaoInscricaoEnum() throws Exception {
    	List objs = new ArrayList(0);
        if(listaSelectItemSituacaoInscricaoEnum == null){
            listaSelectItemSituacaoInscricaoEnum = new ArrayList<SelectItem>(0);
            for(SituacaoInscricaoEnum situacaoInscricaoEnum:SituacaoInscricaoEnum.values()){
            	listaSelectItemSituacaoInscricaoEnum.add(new SelectItem(situacaoInscricaoEnum.name(), situacaoInscricaoEnum.getValorApresentar()));
            }
        }
        return listaSelectItemSituacaoInscricaoEnum;
    }


    public void selecionarCurso(){
    	setUnidadeEnsinoCurso((UnidadeEnsinoCursoVO)getRequestMap().get("cursoItens"));
    }
    
    public void limparCurso(){
    	setUnidadeEnsinoCurso(null);    	
    }
    
    public void consultarProcSeletivo() {
        try {
            List<ProcSeletivoVO> objs = new ArrayList<ProcSeletivoVO>(0);
            if (getCampoConsultaProcSeletivo().equals("descricao")) {
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataFim")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            
            setListaConsultaProcSeletivo(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaProcSeletivo(new ArrayList<ProcSeletivoVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }
    
    public String getMascaraConsultaProcSeletivo() {
        if (getCampoConsultaProcSeletivo().equals("dataInicio") || getCampoConsultaProcSeletivo().equals("dataFim") || getCampoConsultaProcSeletivo().equals("dataProva")) {
        	return "return mascaraData(this.form, this.id, 'dd/MM/yyyy', event)";
        }
        return "";
    }

   

    public String getCampoConsultaProcSeletivo() {
        if (campoConsultaProcSeletivo == null) {
            campoConsultaProcSeletivo = "";
        }
        return campoConsultaProcSeletivo;
    }

    public void setCampoConsultaProcSeletivo(String campoConsultaProcSeletivo) {
        this.campoConsultaProcSeletivo = campoConsultaProcSeletivo;
    }

    public List<ProcSeletivoVO> getListaConsultaProcSeletivo() {
        return listaConsultaProcSeletivo;
    }

    public void setListaConsultaProcSeletivo(List<ProcSeletivoVO> listaConsultaProcSeletivo) {
        this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
    }

    public String getValorConsultaProcSeletivo() {
        if (valorConsultaProcSeletivo == null) {
            valorConsultaProcSeletivo = "";
        }
        return valorConsultaProcSeletivo;
    }

    public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
        this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
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

    public List<SelectItem> getTipoConsultaComboProcSeletivo() {
        if (tipoConsultaComboProcSeletivo == null) {
            tipoConsultaComboProcSeletivo = new ArrayList<SelectItem>(0);
            tipoConsultaComboProcSeletivo.add(new SelectItem("descricao", "Descrição"));
            tipoConsultaComboProcSeletivo.add(new SelectItem("dataInicio", "Data Início"));
            tipoConsultaComboProcSeletivo.add(new SelectItem("dataFim", "Data Fim"));
            
        }
        return tipoConsultaComboProcSeletivo;
    }

    public TipoRelatorioEstatisticoProcessoSeletivoEnum getTipoRelatorio() {
        if (tipoRelatorio == null) {
            tipoRelatorio = TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_FREQUENCIA;
        }
        return tipoRelatorio;
    }

    public void setTipoRelatorio(TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProvaVO() {
        if (itemProcSeletivoDataProvaVO == null) {
            itemProcSeletivoDataProvaVO = new ItemProcSeletivoDataProvaVO();
        }
        return itemProcSeletivoDataProvaVO;
    }

    public void setItemProcSeletivoDataProvaVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) {
        this.itemProcSeletivoDataProvaVO = itemProcSeletivoDataProvaVO;
    }

    public List<SelectItem> getListaSelectItemDataProva() {
        if (listaSelectItemDataProva == null) {
            listaSelectItemDataProva = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemDataProva;
    }

    public void setListaSelectItemDataProva(List<SelectItem> listaSelectItemDataProva) {
        this.listaSelectItemDataProva = listaSelectItemDataProva;
    }

    // Responsável por preencher a lista de tipo de relatório de acordo com a permissão de acesso à consulta
	public List<SelectItem> getListaSelectItemTipoRelatorio() {
		if (listaSelectItemTipoRelatorio == null) {
			listaSelectItemTipoRelatorio = new ArrayList<SelectItem>(0);
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelDadosCandidato()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.DADOS_CANDIDATOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.DADOS_CANDIDATOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelInscritoBairro()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_BAIRRO, TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_BAIRRO.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelInscritoCurso()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_CURSO, TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_CURSO.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaAprovado()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_APROVADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_APROVADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaAusente()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_AUSENTES, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_AUSENTES.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaClassificado()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaFrequencia()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_FREQUENCIA, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_FREQUENCIA.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaMatriculado()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MATRICULADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MATRICULADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaNaoMatriculado()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_NAO_MATRICULADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_NAO_MATRICULADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaReprovado()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_REPROVADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_REPROVADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaMuralCandidato()) {
				listaSelectItemTipoRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MURAL_CANDIDATO, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MURAL_CANDIDATO.getValorApresentar()));
			}
		}
		return listaSelectItemTipoRelatorio;
	}

    
    public void setListaSelectItemTipoRelatorio(List<SelectItem> listaSelectItemTipoRelatorio) {
        this.listaSelectItemTipoRelatorio = listaSelectItemTipoRelatorio;
    }

	public List<SelectItem> getTipoSelectItemGrupoDestinatario() {
		if(tipoSelectItemGrupoDestinatario == null){
			tipoSelectItemGrupoDestinatario = new ArrayList<SelectItem>(0);
		}
		return tipoSelectItemGrupoDestinatario;
	}

	public void setTipoSelectItemGrupoDestinatario(List<SelectItem> tipoSelectItemGrupoDestinatario) {
		this.tipoSelectItemGrupoDestinatario = tipoSelectItemGrupoDestinatario;
	}

	public List<SelectItem> getListaSelectItemSalaProcSeletivo() {
		if(listaSelectItemSalaProcSeletivo == null){
			listaSelectItemSalaProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSalaProcSeletivo;
	}

	public void setListaSelectItemSalaProcSeletivo(List<SelectItem> listaSelectItemSalaProcSeletivo) {
		this.listaSelectItemSalaProcSeletivo = listaSelectItemSalaProcSeletivo;
	}

	public Integer getSala() {
		if(sala == null){
			sala = 0;
		}
		return sala;
	}

	public void setSala(Integer sala) {
		this.sala = sala;
	}
	
	public boolean getIsApresentarOrdenarPor(){
		return getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS);
	}

	public List<SelectItem> getTipoSelectItemOrdenarPor() {
		if(tipoSelectItemOrdenarPor == null){
			tipoSelectItemOrdenarPor = new ArrayList<SelectItem>(0);
			tipoSelectItemOrdenarPor.add(new SelectItem("candidato", "Candidato"));
			tipoSelectItemOrdenarPor.add(new SelectItem("classificacao", "Classificação"));
		}
		return tipoSelectItemOrdenarPor;
	}

	public void setTipoSelectItemOrdenarPor(List<SelectItem> tipoSelectItemOrdenarPor) {
		this.tipoSelectItemOrdenarPor = tipoSelectItemOrdenarPor;
	}

	public String getOrdenarPor() {
		if(ordenarPor == null){
			ordenarPor = "candidato";
		}
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if(unidadeEnsinoCurso == null){
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public List<UnidadeEnsinoCursoVO> getUnidadeEnsinoCursoVOs() {
		if(unidadeEnsinoCursoVOs == null){
			unidadeEnsinoCursoVOs = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return unidadeEnsinoCursoVOs;
	}

	public void setUnidadeEnsinoCursoVOs(List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs) {
		this.unidadeEnsinoCursoVOs = unidadeEnsinoCursoVOs;
	}

	public String getCampoConsultaCurso() {
		if(campoConsultaCurso == null){
			campoConsultaCurso = "curso";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if(valorConsultaCurso == null){
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaCurso() {
		if(listaSelectItemOpcaoConsultaCurso == null){
			listaSelectItemOpcaoConsultaCurso = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaCurso.add(new SelectItem("curso", "Curso"));
		}
		return listaSelectItemOpcaoConsultaCurso;
	}

	public void setListaSelectItemOpcaoConsultaCurso(List<SelectItem> listaSelectItemOpcaoConsultaCurso) {
		this.listaSelectItemOpcaoConsultaCurso = listaSelectItemOpcaoConsultaCurso;
	}

	public void setTipoConsultaComboProcSeletivo(List<SelectItem> tipoConsultaComboProcSeletivo) {
		this.tipoConsultaComboProcSeletivo = tipoConsultaComboProcSeletivo;
	}

	public String getConsultarPor() {
		if (consultarPor == null) {
			consultarPor = "PS";
		}
		return consultarPor;
	}

	public void setConsultarPor(String consultarPor) {
		this.consultarPor = consultarPor;
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

    public List getListaSelectItemSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    /*public List getListaSelectItemSituacao() {
    	List lista = new ArrayList(0);
    	lista.add(new SelectItem("CO", "Confirmado"));
    	lista.add(new SelectItem("PE", "Pendente Financeiro"));
    	return lista;
    }
    
	public SituacaoInscricaoEnum getSituacaoInscricao() {
		return situacaoInscricao;
	}

	public void setSituacaoInscricao(SituacaoInscricaoEnum situacaoInscricao) {
		this.situacaoInscricao = situacaoInscricao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "CO";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}*/
	
	public Boolean getApresentarNomeCandidatoCaixaAlta() {
		if (apresentarNomeCandidatoCaixaAlta == null) {
			apresentarNomeCandidatoCaixaAlta = true;
		}
		return apresentarNomeCandidatoCaixaAlta;
	}

	public void setApresentarNomeCandidatoCaixaAlta(Boolean apresentarNomeCandidatoCaixaAlta) {
		this.apresentarNomeCandidatoCaixaAlta = apresentarNomeCandidatoCaixaAlta;
	}
	
	public FiltroRelatorioProcessoSeletivoVO getFiltroRelatorioProcessoSeletivoVO() {
		if(filtroRelatorioProcessoSeletivoVO == null){
			filtroRelatorioProcessoSeletivoVO = new FiltroRelatorioProcessoSeletivoVO();
		}
		return filtroRelatorioProcessoSeletivoVO;
	}

	public void setFiltroRelatorioProcessoSeletivoVO(FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) {
		this.filtroRelatorioProcessoSeletivoVO = filtroRelatorioProcessoSeletivoVO;
	}
	
	
	
	public Boolean getMarcarTodasSituacoesInscricao() {
		if(marcarTodasSituacoesInscricao == null){
			marcarTodasSituacoesInscricao = false;
		}
		return marcarTodasSituacoesInscricao;
	}

	public void setMarcarTodasSituacoesInscricao(Boolean marcarTodasSituacoesInscricao) {
		this.marcarTodasSituacoesInscricao = marcarTodasSituacoesInscricao;
	}

	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoInscricao() {
		if (getMarcarTodasSituacoesInscricao()) {
			getFiltroRelatorioProcessoSeletivoVO().realizarMarcarTodasSituacoes();
		} else {
			getFiltroRelatorioProcessoSeletivoVO().realizarDesmarcarTodasSituacoes();
		}
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodos() {
		if (getMarcarTodasSituacoesInscricao()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public Boolean getApresentarTipoSelecao() {
		return (getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.DADOS_CANDIDATOS) ||
				getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_BAIRRO) ||
				getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_CURSO) ||
				getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_APROVADOS) ||	
				getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS) ||
				getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS) ||
				getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MATRICULADOS) ||
				getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_NAO_MATRICULADOS) ||
				getTipoRelatorio().equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_REPROVADOS)) && ((Uteis.isAtributoPreenchido(getProcSeletivoVO()) && !getProcSeletivoVO().getTipoAvaliacaoProcessoSeletivo().equals(TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR)) || (!Uteis.isAtributoPreenchido(getProcSeletivoVO())));
	}

}