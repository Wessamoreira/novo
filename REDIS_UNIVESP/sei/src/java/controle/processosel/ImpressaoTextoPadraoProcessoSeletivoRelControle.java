package controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.processosel.TextoPadraoProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisEmail;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;

@Controller("ImpressaoTextoPadraoProcessoSeletivoRelControle")
@Lazy
@Scope("viewScope")
public class ImpressaoTextoPadraoProcessoSeletivoRelControle extends SuperControleRelatorio {

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
    private Integer layoutRelatorio;
    protected List<ProcSeletivoVO> listaConsultaProcSeletivo;
    private List<SelectItem> listaSelectItemSalaProcSeletivo;
	private List<InscricaoVO> listaConsultaInscricao;
	private String campoConsultaInscricao;
	private String valorConsultaInscricao;
	private InscricaoVO inscricaoVO;
	private String descricaoInscricao;	
	private List<SelectItem> listaSelectItemTipoLayoutRelatorio;
    private List<SelectItem> listaSelectItemTipoRelatorio;
    private List<SelectItem> tipoConsultaComboProcSeletivo;
    private List<SelectItem> tipoSelectItemGrupoDestinatario;
    private List<SelectItem> tipoSelectItemOrdenarPor;
    private String ordenarPor;
    private Boolean apresentarNomeCandidatoCaixaAlta;
    private ArquivoVO arquivoVO;
    private TextoPadraoProcessoSeletivoVO textoPadraoProcessoSeletivoVO;
    private List<SelectItem> tipoSelectItemNumeroChamada;
    private Integer numeroChamada;
    
    public ImpressaoTextoPadraoProcessoSeletivoRelControle() {
    	montarListaSelectItemTipoRelatorio();
    }
    
	public void selecionarProcSeletivo() {
        try {
            ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
            setProcSeletivoVO(obj);
            montarListaSelectItemDataProva();
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void selecionarConsultarPor() {
    	setProcSeletivoVO(null);
    	setUnidadeEnsinoCurso(null);
    	montarListaSelectItemTipoRelatorio();
    }
    
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("LC", "Listagem de Candidatos"));
        itens.add(new SelectItem("CI", "Candidato Individual"));
        return itens;
    }

    public void limparProcessoSeletivo(){
    	setProcSeletivoVO(null);
    	getListaSelectItemDataProva().clear();
    	getListaSelectItemSalaProcSeletivo().clear();
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

    public void realizarImpressaoTextoPadraoHtml() {
		try {
			limparMensagem();	
			this.setCaminhoRelatorio("");				
    		if (getConsultarPor().equals("LC")) {
    			if (getProcSeletivoVO().getCodigo().intValue() == 0) {
    				throw new Exception("Informe a Proc. Seletivo para realizar a operação!"); 
    			}
    		} else {
    			if (getInscricaoVO().getCodigo().intValue() == 0) {
    				throw new Exception("Informe a Inscrição para realizar a operação!"); 
    			}
    		}
			setMensagemID("msg_relatorio_ok");
    		//setCaminhoRelatorio(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().imprimirTextoPadrao(get, inscricao, listaImpressao, tipo, PDF, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));        		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void realizarImpressaoTextoPadrao() {
		try {
			limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
    		if (getConsultarPor().equals("LC")) {
    			if (getProcSeletivoVO().getCodigo().intValue() == 0) {
    				throw new Exception("Informe a Proc. Seletivo para realizar a operação!"); 
    			}
    		} else {
    			if (getInscricaoVO().getCodigo().intValue() == 0) {
    				throw new Exception("Informe a Inscrição para realizar a operação!"); 
    			}
    		}
    		if (getLayoutRelatorio().intValue() == 0) {
				throw new Exception("Informe o Layout Relatório para realizar a operação!"); 
			}
    		setTextoPadraoProcessoSeletivoVO(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().consultarPorChavePrimaria(getLayoutRelatorio(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));    		
    		List listaRegistro = new ArrayList(0);
    		if (getTextoPadraoProcessoSeletivoVO().getTipo().equals("LI")) {
        		listaRegistro = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().consultarDadosGeracaoEstatistica(getTipoRelatorio(), getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSala(), getUnidadeEnsinoCurso().getCodigo(), "", "", getFiltroRelatorioProcessoSeletivoVO(), getOrdenarPor(), 0, 0, false, getConfiguracaoGeralPadraoSistema().getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(), getUsuarioLogado(), getNumeroChamada());
    		}
    		setCaminhoRelatorio(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().imprimirTextoPadrao(getTextoPadraoProcessoSeletivoVO(), getInscricaoVO(), listaRegistro, getConsultarPor(), false, getTipoRelatorio(), getConfiguracaoGeralPadraoSistema(), getVersaoSistema(), getUsuarioLogado()));
    		setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");    		
		} catch (Exception e) {
			setFazerDownload(false);	
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoLayoutRelatorio() {
		if (listaSelectItemTipoLayoutRelatorio == null) {
			listaSelectItemTipoLayoutRelatorio = new ArrayList<SelectItem>(0);
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelDadosCandidato()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.DADOS_CANDIDATOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.DADOS_CANDIDATOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelInscritoBairro()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_BAIRRO, TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_BAIRRO.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelInscritoCurso()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_CURSO, TipoRelatorioEstatisticoProcessoSeletivoEnum.INSCRITOS_CURSO.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaAprovado()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_APROVADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_APROVADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaAusente()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_AUSENTES, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_AUSENTES.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaAusentePresentePorCursoTurno()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_PRESENTE_AUSENTES_CURSO_TURNO_DATA.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaClassificado()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_CLASSIFICADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaFrequencia()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_FREQUENCIA, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_FREQUENCIA.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaMatriculado()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MATRICULADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MATRICULADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaNaoMatriculado()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_NAO_MATRICULADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_NAO_MATRICULADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaReprovado()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_REPROVADOS, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_REPROVADOS.getValorApresentar()));
			}
			if (getLoginControle().getPermissaoAcessoMenuVO().getEstatisticaProcessoSeletivoRelListaMuralCandidato()) {
				listaSelectItemTipoLayoutRelatorio.add(new SelectItem(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MURAL_CANDIDATO, TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_MURAL_CANDIDATO.getValorApresentar()));
			}
		}
		return listaSelectItemTipoLayoutRelatorio;
	}
	

    public void setListaSelectItemTipoLayoutRelatorio(List<SelectItem> listaSelectItemTipoLayoutRelatorio) {
        this.listaSelectItemTipoLayoutRelatorio = listaSelectItemTipoLayoutRelatorio;
    }
    
    public String getDownload() {
        if (getFazerDownload()) {
            try {
            	if (getTextoPadraoProcessoSeletivoVO().getTipoDesigneTextoEnum().isPdf()) {
                if(UteisEmail.getURLAplicacao().endsWith("/SEI/") || UteisEmail.getURLAplicacao().endsWith("/SEI")
                        || UteisEmail.getURLAplicacao().endsWith("/SEI/faces")|| UteisEmail.getURLAplicacao().endsWith("/SEI/faces/")){
                    return "location.href='../../DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'";
                }
                return "location.href='../../DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'";
            	}else{
            		return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
            	}
            } catch (Exception ex) {
                Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                setFazerDownload(false);
            }
        }
        return "";
    }
    
    public void imprimirContratoDOC() {
    	try {
    		if (getConsultarPor().equals("LC")) {
    			if (getProcSeletivoVO().getCodigo().intValue() == 0) {
    				throw new Exception("Informe a Proc. Seletivo para realizar a operação!"); 
    			}
    		} else {
    			if (getInscricaoVO().getCodigo().intValue() == 0) {
    				throw new Exception("Informe a Inscrição para realizar a operação!"); 
    			}
    		}
    		//setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(getLayoutRelatorio(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));			
						 
        	HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			String textoHTML = (String) request.getSession().getAttribute("textoRelatorio");
			//ArquivoHelper.criarArquivoDOC(textoHTML, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
    	} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
	public String getDownloadContrato() {
		try {
			//if (getIsDownloadContrato()) {
				context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoVO().getNome());
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getArquivoVO().getPastaBaseArquivo());
				context().getExternalContext().getSessionMap().put("deletarArquivo", Boolean.TRUE);
				return "location.href='../DownloadSV'";
			//}
			//return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	

	public void consultarInscricao() {
		try {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			List<InscricaoVO> objs = new ArrayList<InscricaoVO>(0);
			if (getCampoConsultaInscricao().equals("codigo")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o CÓDIGO desejado.");
				}
				int valorInt = Integer.parseInt(getValorConsultaInscricao());
				objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigo(new Integer(valorInt), getProcSeletivoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaInscricao().equals("nomePessoa")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o NOME do CANDIDATO desejado.");
				}
				objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoa(getValorConsultaInscricao(), getUnidadeEnsinoLogado().getCodigo(), getProcSeletivoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaInscricao().equals("cpfPessoa")) {
				if (getValorConsultaInscricao().equals("")) {
					throw new ConsistirException("Por favor informe o CPF do CANDIDATO desejado.");
				}
				objs = getFacadeFactory().getInscricaoFacade().consultarPorCPFPessoa(getValorConsultaInscricao(), getUnidadeEnsinoLogado().getCodigo(), getProcSeletivoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaInscricao(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarInscricao() {
		try {
			InscricaoVO inscricao = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
			inscricao = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(inscricao.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setInscricaoVO(new InscricaoVO());
			getInscricaoVO().setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
			setInscricaoVO(inscricao);
			setDescricaoInscricao(getInscricaoVO().getCodigo() + " - " + getInscricaoVO().getCandidato().getNome());
//			getInscricaoVO().setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(inscricao.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//			getInscricaoVO().getResultadoProcessoSeletivoVO().setInscricao(inscricao);
			setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
		} catch (Exception e) {
			setDescricaoInscricao(null);
			setInscricaoVO(new InscricaoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboInscricao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("cpfPessoa", "CPF do Candidato"));
		itens.add(new SelectItem("nomePessoa", "Nome do Candidato"));
		itens.add(new SelectItem("codigo", "Número da Inscrição"));
		return itens;
	}
	
	public void limparInscricao() {
		try {
			setDescricaoInscricao(null);
			setInscricaoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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
    		listaRegistro = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().consultarDadosGeracaoEstatistica(getTipoRelatorio(), getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSala(), getUnidadeEnsinoCurso().getCodigo(), getAno(), getSemestre(), getFiltroRelatorioProcessoSeletivoVO(), getOrdenarPor(), 0, 0, getApresentarNomeCandidatoCaixaAlta(), getConfiguracaoGeralPadraoSistema().getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(), getUsuarioLogado(), getNumeroChamada());
    		
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
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataFim")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false,
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
            return "return mascara(this.form,'this.id','99/99/9999',event);";
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
		if (listaSelectItemTipoRelatorio == null ) {
			listaSelectItemTipoRelatorio = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoRelatorio;
	}
	
	public void montarListaSelectItemTipoRelatorio() {
		getListaSelectItemTipoRelatorio().clear();
		List objs = new ArrayList(0);		
		try {
			List resultadoConsulta = new ArrayList(0); 
			Iterator i = null;
			if (getConsultarPor().equals("CI")) {
				resultadoConsulta = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().consultarPorTipo("CA", 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else {
				resultadoConsulta = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().consultarPorTipo("LI", 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
            i = resultadoConsulta.iterator();            
            //objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TextoPadraoProcessoSeletivoVO obj = (TextoPadraoProcessoSeletivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
            }			
		} catch (Exception e) {
			e.getMessage();
		}
		setListaSelectItemTipoRelatorio(objs);
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
			consultarPor = "CI";
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

   /* public List getListaSelectItemSituacao() {
    	List lista = new ArrayList(0);
    	lista.add(new SelectItem("CO", "Confirmado"));
    	lista.add(new SelectItem("PE", "Pendente Financeiro"));
    	return lista;
    }*/
    
	/*public SituacaoInscricaoEnum getSituacaoInscricao() {
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

	public void setListaConsultaInscricao(List<InscricaoVO> listaConsultaInscricao) {
		this.listaConsultaInscricao = listaConsultaInscricao;
	}

	public List<InscricaoVO> getListaConsultaInscricao() {
		if (listaConsultaInscricao == null) {
			listaConsultaInscricao = new ArrayList<InscricaoVO>(0);
		}
		return listaConsultaInscricao;
	}
	
	public InscricaoVO getInscricaoVO() {
		if (inscricaoVO == null) {
			inscricaoVO = new InscricaoVO();
		}
		return inscricaoVO;
	}

	public void setInscricaoVO(InscricaoVO inscricaoVO) {
		this.inscricaoVO = inscricaoVO;
	}

	public void setCampoConsultaInscricao(String campoConsultaInscricao) {
		this.campoConsultaInscricao = campoConsultaInscricao;
	}

	public String getCampoConsultaInscricao() {
		if (campoConsultaInscricao == null) {
			campoConsultaInscricao = "nomeCandidato";
		}
		return campoConsultaInscricao;
	}

	public void setValorConsultaInscricao(String valorConsultaInscricao) {
		this.valorConsultaInscricao = valorConsultaInscricao;
	}

	public Boolean getDesabilitarInscricao() {
		return getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo().intValue() != 0;
	}

	public String getValorConsultaInscricao() {
		return valorConsultaInscricao;
	}

	public String getDescricaoInscricao() {
		if (descricaoInscricao == null) {
			descricaoInscricao = "";
		}
		return descricaoInscricao;
	}

	public void setDescricaoInscricao(String descricaoInscricao) {
		this.descricaoInscricao = descricaoInscricao;
	}

	public Integer getLayoutRelatorio() {
		if (layoutRelatorio == null) {
			layoutRelatorio = 0;
		}
		return layoutRelatorio;
	}

	public void setLayoutRelatorio(Integer layoutRelatorio) {
		this.layoutRelatorio = layoutRelatorio;
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}
	
	public TextoPadraoProcessoSeletivoVO getTextoPadraoProcessoSeletivoVO() {
		if (textoPadraoProcessoSeletivoVO == null) {
			textoPadraoProcessoSeletivoVO = new TextoPadraoProcessoSeletivoVO();
		}
		return textoPadraoProcessoSeletivoVO;
	}

	public void setTextoPadraoProcessoSeletivoVO(TextoPadraoProcessoSeletivoVO textoPadraoProcessoSeletivoVO) {
		this.textoPadraoProcessoSeletivoVO = textoPadraoProcessoSeletivoVO;
	}

	public Integer getNumeroChamada() {
		if (numeroChamada == null) {
			numeroChamada = 0;
		}
		return numeroChamada;
	}

	public void setNumeroChamada(Integer numeroChamada) {
		this.numeroChamada = numeroChamada;
	}

	
}