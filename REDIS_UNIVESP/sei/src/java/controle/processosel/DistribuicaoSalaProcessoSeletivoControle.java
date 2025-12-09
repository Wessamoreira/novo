package controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisEmail;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.CartaoRespostaRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscritoSalaRelVO;

@Controller("DistribuicaoSalaProcessoSeletivoControle")
@Scope("viewScope")
@Lazy
public class DistribuicaoSalaProcessoSeletivoControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4569605319661748423L;
	private ProcSeletivoVO procSeletivoVO;
    private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO;
    private List<SelectItem> listaSelectItemProcessoSeletivo;
    private List<SelectItem> listaSelectItemLocalAula;
    private List<SelectItem> listaSelectItemSalaLocalAula;
    private Integer numeroInscritos;
    private Integer numeroInscritosNecessidadesEspeciais;
    private Integer numeroInscritosSemSala;
    private String formaDistribuicao;
    private Boolean agruparNecessidadesEspeciais;
    private List<SelectItem> listaSelectItemFormaDistribuicao;
    private List<SelectItem> listaSelectItemDataProva;
    private List<ProcessoSeletivoInscritoSalaRelVO> processoSeletivoInscritoSalaRelVOs;
    private List<InscricaoVO> inscricaoVOs;
    private Boolean visualizarInscricao;
    private Boolean distribuirCandidatosSemSala;
    private List<SalaLocalAulaVO> salaLocalAulaVOs;
    private Integer localAula;
    private List<SelectItem> listaSelectItemlayoutEtiqueta;
    private List<SelectItem> listaSelectItemColuna;
    private List<SelectItem> listaSelectItemLinha;
    private Integer numeroCopias;
    private Integer coluna;
    private Integer linha;
    private Integer sala;
    private Integer salaVisualizacao;
    private String layoutImpressao;
    private LayoutEtiquetaVO layoutEtiquetaVO;
    private InscricaoVO inscricao;
    private List<SelectItem> listaSelectItemTipoLayoutImpressao;
    private String campoConsultaProcSeletivo;
    private String valorConsultaProcSeletivo;
    private List<ProcSeletivoVO> listaConsultaProcSeletivo;
    
    public DistribuicaoSalaProcessoSeletivoControle() {
    	try {
			setProcSeletivoVO(getFacadeFactory().getProcSeletivoFacade().consultarUltimoProcessoSeletivo(Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			if (getProcSeletivoVO().getCodigo() > 0) {
				consultarDadosProcessoSeletivo();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public List<SelectItem> getListaSelectItemTipoLayoutImpressao() {
        if(listaSelectItemTipoLayoutImpressao == null){
            listaSelectItemTipoLayoutImpressao = new ArrayList<SelectItem>(0);
            listaSelectItemTipoLayoutImpressao.add(new SelectItem("CODIGO_BARRA", "Etiqueta Código Barra"));
            listaSelectItemTipoLayoutImpressao.add(new SelectItem("CARTAO_RESPOSTA", "Cartão Resposta"));
            listaSelectItemTipoLayoutImpressao.add(new SelectItem("PREENCHER_CARTAO_RESPOSTA", "Preenchimento Cartão Resposta"));
        }
        return listaSelectItemTipoLayoutImpressao;
    }

    public void consultarDadosProcessoSeletivo() {
        setNumeroInscritos(0);        
        setNumeroInscritosSemSala(0);                
        getProcessoSeletivoInscritoSalaRelVOs().clear();
        setItemProcSeletivoDataProvaVO(null);
        setVisualizarInscricao(false);
        if (getProcSeletivoVO().getCodigo() > 0) {
            montarListaSelectItemDataProva();
        }
    }
    
    public void limparCamposConsultaProcSeletivo() {
    	try {
			setListaConsultaProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setValorConsultaProcSeletivo(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public List<SelectItem> getTipoConsultaProcSeletivoCombo() {
   	 List itens = new ArrayList(0);
        // itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("dataInicio", "Data Início"));
        itens.add(new SelectItem("dataFim", "Data Fim"));
        itens.add(new SelectItem("dataInicioInternet", "Data Início Internet"));
        itens.add(new SelectItem("dataFimInternet", "Data Fim Internet"));
//        itens.add(new SelectItem("valorInscricao", "Valor Inscrição"));
//        itens.add(new SelectItem("dataProva", "Data Prova"));
        return itens;
   }
    
    public void selecionarProcSeletivo() {
    	ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
        setProcSeletivoVO(obj);
        consultarDadosProcessoSeletivo();
        setMensagemID("msg_dados_adicionados");
    }
    
    public void limparDadosProcSeletivo() {
    	setProcSeletivoVO(new ProcSeletivoVO());
    }
    
    public void consultarProcSeletivos() throws Exception {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaProcSeletivo().equals("descricao")) {
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataFim")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataInicioInternet")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioInternetUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProcSeletivo().equals("dataFimInternet")) {
                Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
                objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimInternetUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaProcSeletivo(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void alterarLocalAula(){
    	try{
    		if(getLocalAula() == 0){
    			getSalaLocalAulaVOs().clear();
    			getListaSelectItemSalaLocalAula().clear();
    			
    		}else{
    			setSalaLocalAulaVOs(getFacadeFactory().getSalaLocalAulaFacade().consultarPorLocalAula(getLocalAula()));
    			getListaSelectItemSalaLocalAula().clear();
    			getListaSelectItemSalaLocalAula().add(new SelectItem(0, ""));
    			for(SalaLocalAulaVO salaLocalAulaVO: getSalaLocalAulaVOs()){
    				getListaSelectItemSalaLocalAula().add(new SelectItem(salaLocalAulaVO.getCodigo(), salaLocalAulaVO.getSala()));
    			}	
    		}
    		
    	}catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void consultarDadosDataProcessoSeletivo() {
        try {
            setNumeroInscritos(0);
            setNumeroInscritosNecessidadesEspeciais(0);            
            setNumeroInscritosSemSala(0);
            getProcessoSeletivoInscritoSalaRelVOs().clear();
            setVisualizarInscricao(false);
            if (getItemProcSeletivoDataProvaVO().getCodigo() > 0) {
                realizarAtualizacaoValorProcessoSeletivo();
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarDistribuicaoSalaProcessoSeletivo() {
        try {
            getFacadeFactory().getInscricaoFacade().realizarDistribuicaoSalaProcessoSeletivo(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getAgruparNecessidadesEspeciais(), getFormaDistribuicao(), getDistribuirCandidatosSemSala(), getSalaLocalAulaVOs(), getUsuarioLogado());
            realizarAtualizacaoValorProcessoSeletivo();
            setMensagemID("msg_ProcessoSeletivo_distribuicaoRealizada", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarAtualizacaoValorProcessoSeletivo() throws Exception {
        setNumeroInscritos(getFacadeFactory().getInscricaoFacade().consultarNumeroInscritosConfirmadosProcessoSeletivo(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo()));
        setNumeroInscritosNecessidadesEspeciais(getFacadeFactory().getInscricaoFacade().consultarNumeroInscritosConfirmadosNecessidadesEspeciaisProcessoSeletivo(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo()));
        setNumeroInscritosSemSala(getFacadeFactory().getInscricaoFacade().consultarNumeroInscritosConfirmadosSemSalaProcessoSeletivo(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo()));
        setProcessoSeletivoInscritoSalaRelVOs(getFacadeFactory().getInscricaoFacade().consultarQtdeInscritosPorSala(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), true));
    }

    public void montarListaSelectItemDataProva() {
        try {
            List<ItemProcSeletivoDataProvaVO> itemProcSeletivoDataProvaVOs = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getListaSelectItemDataProva().clear();
            getListaSelectItemDataProva().add(new SelectItem(0, ""));
            for (ItemProcSeletivoDataProvaVO obj : itemProcSeletivoDataProvaVOs) {
                getListaSelectItemDataProva().add(new SelectItem(obj.getCodigo(), obj.getDataProva_Apresentar()));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }


    public void alterarSalaInscricao() {
        try {
        	SalaLocalAulaVO salaLocalAulaVO = getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(getInscricao().getSalaAlterar());
        	if(salaLocalAulaVO != null){
        		Integer nrInscritosSala = getFacadeFactory().getInscricaoFacade().consultarNumeroInscritosSalaEspecifica(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), salaLocalAulaVO.getCodigo());
        		if(nrInscritosSala >= salaLocalAulaVO.getCapacidade()){
        			throw new Exception("Esta SALA já chegou em sua capacidade máxima, escolher outra sala de aula.");
        		}
        		salaLocalAulaVO.setLocalAula(getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(salaLocalAulaVO.getLocalAula().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
        	}
            getFacadeFactory().getInscricaoFacade().alterarSalaInscricao(getInscricao().getCodigo(), getInscricao().getSalaAlterar(), getUsuarioLogado());
            getInscricao().setSala(salaLocalAulaVO);
            setInscricao(new InscricaoVO());
            setVisualizarInscricao(true);
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void visualizarCandidatosSala() {
        try {
            ProcessoSeletivoInscritoSalaRelVO sala = (ProcessoSeletivoInscritoSalaRelVO) context().getExternalContext().getRequestMap().get("salaItens");
            setInscricao(new InscricaoVO());
            setSala(sala.getSala().getCodigo());
            setSalaVisualizacao(sala.getSala().getCodigo());
            setNumeroCopias(1);
            setLinha(1);
            setColuna(1);
            setCaminhoRelatorio("");
            setInscricaoVOs(getFacadeFactory().getInscricaoFacade().consultaRapidaPorSalaProcSeletivoData(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), sala.getSala().getCodigo()));
            setVisualizarInscricao(true);
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void voltarDistribuicaoSala() {
        try {
            setVisualizarInscricao(false);
            realizarAtualizacaoValorProcessoSeletivo();
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void inicializarDadosLayoutEtiqueta() {
        try {
            getListaSelectItemColuna().clear();
            getListaSelectItemLinha().clear();
            if (getLayoutEtiquetaVO().getCodigo() > 0) {
                setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
                for (int x = 1; x <= getLayoutEtiquetaVO().getNumeroLinhasEtiqueta(); x++) {
                    getListaSelectItemLinha().add(new SelectItem(x, String.valueOf(x)));
                }
                for (int y = 1; y <= getLayoutEtiquetaVO().getNumeroColunasEtiqueta(); y++) {
                    getListaSelectItemColuna().add(new SelectItem(y, String.valueOf(y)));
                }
            }

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void realizarGeracaoArquivoEtiquetaSalaVisualizacao() {
        setInscricao(new InscricaoVO());
        setSala(getSalaVisualizacao());
        setNumeroCopias(1);
        setLinha(1);
        setColuna(1);
        setCaminhoRelatorio("");
    }

    public void realizarGeracaoArquivoEtiquetaSala() {
        ProcessoSeletivoInscritoSalaRelVO sala = (ProcessoSeletivoInscritoSalaRelVO) context().getExternalContext().getRequestMap().get("salaItens");
        setInscricao(new InscricaoVO());
        setSala(sala.getSala().getCodigo());
        setNumeroCopias(1);
        setLinha(1);
        setColuna(1);
        setCaminhoRelatorio("");
    }

    public void realizarGeracaoArquivoEtiquetaInscricao() {
        InscricaoVO inscricaoVO = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
        setInscricao(inscricaoVO);
        setSala(0);
        setNumeroCopias(1);
        setLinha(1);
        setColuna(1);
        setCaminhoRelatorio("");
    }

    public void realizarGeracaoArquivoEtiquetaTodasSala() {
        setInscricao(new InscricaoVO());
        setSala(0);
        setNumeroCopias(1);
        setLinha(1);
        setColuna(1);
        setCaminhoRelatorio("");
    }

    public void realizarGeracaoCartaoRespostaTodasSala() {
        try {
            setSala(0);
            setInscricao(new InscricaoVO());
            setFazerDownload(false);
            setSalaVisualizacao(0);
            
//            realizarGeracaoCartaoResposta(getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(true,
//                    0, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), null, null));
        } catch (Exception e) {
            setCaminhoRelatorio("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void realizarGeracaoCartaoRespostaSala() {
        try {
        	setFazerDownload(false);
            ProcessoSeletivoInscritoSalaRelVO sala = (ProcessoSeletivoInscritoSalaRelVO) context().getExternalContext().getRequestMap().get("salaItens");
            setSalaVisualizacao(sala.getSala().getCodigo());
            setInscricao(new InscricaoVO());
            setSala(0);
//            realizarGeracaoCartaoResposta(getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(true,
//                    0, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), sala.getSala(), null));
        } catch (Exception e) {
            setCaminhoRelatorio("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    
    public void realizarGeracaoCartaoRespostaSalaVisualizacao() {
        try {            
        	setFazerDownload(false);
        	setNumeroCopias(1);
//            realizarGeracaoCartaoResposta(getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(true,
//                    0, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSalaVisualizacao(), null));
        } catch (Exception e) {
            setCaminhoRelatorio("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    public void realizarGeracaoCartaoRespostaInscricao() {
        try {    
        	setFazerDownload(false);
        	setNumeroCopias(1);
            setInscricao(((InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens")));
            
//            realizarGeracaoCartaoResposta(getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(true,
//                    0, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSalaVisualizacao(), inscricaoVO.getCodigo()));
            
        } catch (Exception e) {
            setCaminhoRelatorio("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void realizarGeracaoCartaoResposta() throws Exception{
    	setFazerDownload(false);
    	List<CartaoRespostaRelVO> listaObjetos = getFacadeFactory().getCartaoRespostaRelFacade().criarObjeto(true,
                0, getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSalaVisualizacao(), getInscricao().getCodigo());
        if (!listaObjetos.isEmpty()) {
            if(getLayoutImpressao().equals("CARTAO_RESPOSTA")){
                super.setCaminhoRelatorio(getFacadeFactory().getCartaoRespostaRelFacade().realizarImpressaoCartaoRespostaLC3000(listaObjetos, getNumeroCopias(), getColuna(), false, false, getUsuarioLogado()));
                super.setFazerDownload(true);
            }else  if(getLayoutImpressao().equals("PREENCHER_CARTAO_RESPOSTA")){
                	super.setCaminhoRelatorio(getFacadeFactory().getCartaoRespostaRelFacade().realizarImpressaoCartaoRespostaLC3000(listaObjetos, getNumeroCopias(), getColuna(), true, false, getUsuarioLogado()));
                	super.setFazerDownload(true);
            }else{
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getCartaoRespostaRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getCartaoRespostaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getCartaoRespostaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                realizarImpressaoRelatorio();
//                removerObjetoMemoria(this);
//                montarListaSelectItemProcSeletivo();
            }
            setMensagemID("msg_relatorio_ok");
        } else {
            setMensagemID("msg_relatorio_sem_dados");
        }
    }

//    private void realizarGeracaoCartaoResposta(List<CartaoRespostaRelVO> listaObjetos) throws Exception {
//        if (!listaObjetos.isEmpty()) {
//            setNomeArquivo(getFacadeFactory().getCartaoRespostaRelFacade().realizarImpressaoCartaoRespostaLC3000(listaObjetos, 1, true, getUsuarioLogado()));
//            limparMensagem();
//        } else {
//            setNomeArquivo("");
//            setMensagemID("msg_relatorio_sem_dados");
//        }
//    }

//    private String nomeArquivo;

    public void realizarGeracaoArquivoEtiqueta() {
        try {
        	setFazerDownload(false);
            List<InscricaoVO> inscricaoVOs = getFacadeFactory().getInscricaoFacade().consultaRapidaParaImpressaoEtiqueta(getProcSeletivoVO().getCodigo(), getItemProcSeletivoDataProvaVO().getCodigo(), getSala(), getInscricao().getCodigo());
            setCaminhoRelatorio(getFacadeFactory().getInscricaoFacade().realizarImpressaoEtiquetaInscricaoProcessoSeletiva(getLayoutEtiquetaVO(), inscricaoVOs, getNumeroCopias(), getLinha(), getColuna(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
            super.setFazerDownload(true);
            limparMensagem();
        } catch (Exception e) {
        	setCaminhoRelatorio("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String getDownload() {
    	
    	 if (getFazerDownload()) {
             try {
                 if(UteisEmail.getURLAplicacao().endsWith("/SEI/") || UteisEmail.getURLAplicacao().endsWith("/SEI")
                         || UteisEmail.getURLAplicacao().endsWith("/SEI/faces")|| UteisEmail.getURLAplicacao().endsWith("/SEI/faces/")){
                     return "location.href='../../DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'; RichFaces.$('panelImprimirEtiqueta').hide();";
                 }
                 return "location.href='../../DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'; RichFaces.$('panelImprimirEtiqueta').hide();";
             } catch (Exception ex) {
                 Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
             } finally {
                 setFazerDownload(false);
             }
         }
         return "";
//        if (getNomeArquivo().trim().isEmpty()) {
//            return "";
//        }
//        return Uteis.getCaminhoDownloadRelatorio(true, getNomeArquivo()) + "; Richfaces.hideModalPanel('panelImprimirEtiqueta');";
    }

//    public String getNomeArquivo() {
//        if (nomeArquivo == null) {
//            nomeArquivo = "";
//        }
//        return nomeArquivo;
//    }
//
//    public void setNomeArquivo(String nomeArquivo) {
//        this.nomeArquivo = nomeArquivo;
//    }

    public List<SelectItem> getListaSelectItemDataProva() {
        if (listaSelectItemDataProva == null) {
            listaSelectItemDataProva = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemDataProva;
    }

    public void setListaSelectItemDataProva(List<SelectItem> listaSelectItemDataProva) {
        this.listaSelectItemDataProva = listaSelectItemDataProva;
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

    public List<SelectItem> getListaSelectItemProcessoSeletivo() {
        if (listaSelectItemProcessoSeletivo == null) {
            listaSelectItemProcessoSeletivo = new ArrayList<SelectItem>(0);
            try {
                List<ProcSeletivoVO> procSeletivoVOs = getFacadeFactory().getProcSeletivoFacade().consultarProcessoSeletivoAntesDataProvaPorUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
                listaSelectItemProcessoSeletivo.add(new SelectItem(0, ""));
                for (ProcSeletivoVO procSeletivoVO : procSeletivoVOs) {
                    listaSelectItemProcessoSeletivo.add(new SelectItem(procSeletivoVO.getCodigo(), procSeletivoVO.getDescricao()));
                }
            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
            }
        }
        return listaSelectItemProcessoSeletivo;
    }

    public void setListaSelectItemProcessoSeletivo(List<SelectItem> listaSelectItemProcessoSeletivo) {
        this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
    }

    public Integer getNumeroInscritos() {
        if (numeroInscritos == null) {
            numeroInscritos = 0;
        }
        return numeroInscritos;
    }

    public void setNumeroInscritos(Integer numeroInscritos) {
        this.numeroInscritos = numeroInscritos;
    }

    public Integer getNumeroInscritosSemSala() {
        return numeroInscritosSemSala;
    }

    public void setNumeroInscritosSemSala(Integer numeroInscritosSemSala) {
        this.numeroInscritosSemSala = numeroInscritosSemSala;
    }

    public Boolean getAgruparNecessidadesEspeciais() {
        if (agruparNecessidadesEspeciais == null) {
            agruparNecessidadesEspeciais = false;
        }
        return agruparNecessidadesEspeciais;
    }

    public void setAgruparNecessidadesEspeciais(Boolean agruparNecessidadesEspeciais) {
        this.agruparNecessidadesEspeciais = agruparNecessidadesEspeciais;
    }

    public List<SelectItem> getListaSelectItemFormaDistribuicao() {
        if (listaSelectItemFormaDistribuicao == null) {
            listaSelectItemFormaDistribuicao = new ArrayList<SelectItem>(0);
            listaSelectItemFormaDistribuicao.add(new SelectItem("ALUNO", "Ordem Alfabética"));
            listaSelectItemFormaDistribuicao.add(new SelectItem("CURSO", "Curso"));
        }
        return listaSelectItemFormaDistribuicao;
    }

    public void setListaSelectItemFormaDistribuicao(List<SelectItem> listaSelectItemFormaDistribuicao) {
        this.listaSelectItemFormaDistribuicao = listaSelectItemFormaDistribuicao;
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

    public Integer getNumeroInscritosNecessidadesEspeciais() {
        if (numeroInscritosNecessidadesEspeciais == null) {
            numeroInscritosNecessidadesEspeciais = 0;
        }
        return numeroInscritosNecessidadesEspeciais;
    }

    public void setNumeroInscritosNecessidadesEspeciais(Integer numeroInscritosNecessidadesEspeciais) {
        this.numeroInscritosNecessidadesEspeciais = numeroInscritosNecessidadesEspeciais;
    }

    public List<ProcessoSeletivoInscritoSalaRelVO> getProcessoSeletivoInscritoSalaRelVOs() {
        if (processoSeletivoInscritoSalaRelVOs == null) {
            processoSeletivoInscritoSalaRelVOs = new ArrayList<ProcessoSeletivoInscritoSalaRelVO>(0);
        }
        return processoSeletivoInscritoSalaRelVOs;
    }

    public void setProcessoSeletivoInscritoSalaRelVOs(List<ProcessoSeletivoInscritoSalaRelVO> processoSeletivoInscritoSalaRelVOs) {
        this.processoSeletivoInscritoSalaRelVOs = processoSeletivoInscritoSalaRelVOs;
    }

    public String getFormaDistribuicao() {
        if (formaDistribuicao == null) {
            formaDistribuicao = "ALUNO";
        }
        return formaDistribuicao;
    }

    public void setFormaDistribuicao(String formaDistribuicao) {
        this.formaDistribuicao = formaDistribuicao;
    }

    public List<InscricaoVO> getInscricaoVOs() {
        if (inscricaoVOs == null) {
            inscricaoVOs = new ArrayList<InscricaoVO>(0);
        }
        return inscricaoVOs;
    }

    public void setInscricaoVOs(List<InscricaoVO> inscricaoVOs) {
        this.inscricaoVOs = inscricaoVOs;
    }

    public Boolean getVisualizarInscricao() {
        if (visualizarInscricao == null) {
            visualizarInscricao = false;
        }
        return visualizarInscricao;
    }

    public void setVisualizarInscricao(Boolean visualizarInscricao) {
        this.visualizarInscricao = visualizarInscricao;
    }

    public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
        if (listaSelectItemlayoutEtiqueta == null) {
            listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
            try {
                List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.INSCRICAO_SELETIVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
                listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
                for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
                    listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));

                }
            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
            }
        }
        return listaSelectItemlayoutEtiqueta;
    }

    public void setListaSelectItemlayoutEtiqueta(List<SelectItem> listaSelectItemlayoutEtiqueta) {
        this.listaSelectItemlayoutEtiqueta = listaSelectItemlayoutEtiqueta;
    }

    public List<SelectItem> getListaSelectItemColuna() {
        if (listaSelectItemColuna == null) {
            listaSelectItemColuna = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemColuna;
    }

    public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
        this.listaSelectItemColuna = listaSelectItemColuna;
    }

    public List<SelectItem> getListaSelectItemLinha() {
        if (listaSelectItemLinha == null) {
            listaSelectItemLinha = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemLinha;
    }

    public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
        this.listaSelectItemLinha = listaSelectItemLinha;
    }

    public Integer getNumeroCopias() {
        if (numeroCopias == null) {
            numeroCopias = 1;
        }
        return numeroCopias;
    }

    public void setNumeroCopias(Integer numeroCopias) {
        this.numeroCopias = numeroCopias;
    }

    public LayoutEtiquetaVO getLayoutEtiquetaVO() {
        if (layoutEtiquetaVO == null) {
            layoutEtiquetaVO = new LayoutEtiquetaVO();
        }
        return layoutEtiquetaVO;
    }

    public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
        this.layoutEtiquetaVO = layoutEtiquetaVO;
    }

    public Integer getColuna() {
        if (coluna == null) {
            coluna = 1;
        }
        return coluna;
    }

    public void setColuna(Integer coluna) {
        this.coluna = coluna;
    }

    public Integer getLinha() {
        if (linha == null) {
            linha = 1;
        }
        return linha;
    }

    public void setLinha(Integer linha) {
        this.linha = linha;
    }

    public Integer getSala() {
        if (sala == null) {
            sala = 0;
        }
        return sala;
    }

    public void setSala(Integer sala) {
        this.sala = sala;
    }

    public InscricaoVO getInscricao() {
        if (inscricao == null) {
            inscricao = new InscricaoVO();
        }
        return inscricao;
    }

    public void setInscricao(InscricaoVO inscricao) {
        this.inscricao = inscricao;
    }

    public Integer getSalaVisualizacao() {
        if (salaVisualizacao == null) {
            salaVisualizacao = 0;
        }
        return salaVisualizacao;
    }

    public void setSalaVisualizacao(Integer salaVisualizacao) {
        this.salaVisualizacao = salaVisualizacao;
    }
    
    public String getLayoutImpressao() {
        if(layoutImpressao == null){
            layoutImpressao= "PREENCHER_CARTAO_RESPOSTA";
        }
        return layoutImpressao;
    }

    
    public void setLayoutImpressao(String layoutImpressao) {
        this.layoutImpressao = layoutImpressao;
    }

	public List<SelectItem> getListaSelectItemLocalAula() {
		if(listaSelectItemLocalAula == null){
			listaSelectItemLocalAula = new ArrayList<SelectItem>(0);
			try{
				List<LocalAulaVO> localAulaVOs = getFacadeFactory().getLocalAulaFacade().consultaLocalSalaAulaPorSituacao(StatusAtivoInativoEnum.ATIVO, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemLocalAula.add(new SelectItem(0, ""));
				for(LocalAulaVO localAulaVO:localAulaVOs){
					listaSelectItemLocalAula.add(new SelectItem(localAulaVO.getCodigo(), localAulaVO.getLocal()));
				}
			}catch(Exception e){
				
			}
		}
		return listaSelectItemLocalAula;
	}

	public void setListaSelectItemLocalAula(List<SelectItem> listaSelectItemLocalAula) {
		this.listaSelectItemLocalAula = listaSelectItemLocalAula;
	}

	public Boolean getDistribuirCandidatosSemSala() {
		if(distribuirCandidatosSemSala == null){
			distribuirCandidatosSemSala = false;
		}
		return distribuirCandidatosSemSala;
	}

	public void setDistribuirCandidatosSemSala(Boolean distribuirCandidatosSemSala) {
		this.distribuirCandidatosSemSala = distribuirCandidatosSemSala;
	}

	public List<SalaLocalAulaVO> getSalaLocalAulaVOs() {
		if(salaLocalAulaVOs == null){
			salaLocalAulaVOs = new ArrayList<SalaLocalAulaVO>(0);
		}
		return salaLocalAulaVOs;
	}

	public void setSalaLocalAulaVOs(List<SalaLocalAulaVO> salaLocalAulaVOs) {
		this.salaLocalAulaVOs = salaLocalAulaVOs;
	}

	public Integer getLocalAula() {
		if(localAula == null){
			localAula = 0;
		}
		return localAula;
	}

	public void setLocalAula(Integer localAula) {
		this.localAula = localAula;
	}

	public List<SelectItem> getListaSelectItemSalaLocalAula() {
		if(listaSelectItemSalaLocalAula == null){
			listaSelectItemSalaLocalAula = new ArrayList<SelectItem>();
		}
		return listaSelectItemSalaLocalAula;
	}

	public void setListaSelectItemSalaLocalAula(List<SelectItem> listaSelectItemSalaLocalAula) {
		this.listaSelectItemSalaLocalAula = listaSelectItemSalaLocalAula;
	}
    
    public Integer getColumnSala(){
    	if(getSalaLocalAulaVOs().size() >= 4){
    		return 4;
    	}
    	return getSalaLocalAulaVOs().size();
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

	public String getValorConsultaProcSeletivo() {
		if (valorConsultaProcSeletivo == null) {
			valorConsultaProcSeletivo = "";
		}
		return valorConsultaProcSeletivo;
	}

	public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
		this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
	}

	public List<ProcSeletivoVO> getListaConsultaProcSeletivo() {
		if (listaConsultaProcSeletivo == null) {
			listaConsultaProcSeletivo = new ArrayList<ProcSeletivoVO>(0);
		}
		return listaConsultaProcSeletivo;
	}

	public void setListaConsultaProcSeletivo(List<ProcSeletivoVO> listaConsultaProcSeletivo) {
		this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
	}
	
	public String getCssLocalProva() {
		if(getSalaLocalAulaVOs().size() > 1) {
			return "border-right: 1px solid #ababab;";
		} return "";
	}

}
