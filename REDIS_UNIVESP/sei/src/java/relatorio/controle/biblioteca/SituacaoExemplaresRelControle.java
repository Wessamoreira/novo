package relatorio.controle.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.biblioteca.AssinaturaPeriodicoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ClassificacaoBibliograficaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoSaidaAcervo;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.biblioteca.SituacaoExemplaresRelVO;
import relatorio.negocio.jdbc.biblioteca.enumeradores.SituacaoExemplaresRelEnum;

@Controller("SituacaoExemplaresRelControle")
@Scope("viewScope")
@Lazy
public class SituacaoExemplaresRelControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SituacaoExemplaresRelVO situacaoExemplaresRelVO;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemBiblioteca;
    private List<SelectItem> listaSelectItemSecao;
    private List<SelectItem> listaSelectItemNivelBibliografico;
    private List<SelectItem> listaSelectItemSituacao;
    private String campoConsultarCatalogo;
    private String valorConsultarCatalogo;
    private List<CatalogoVO> listaConsultarCatalogo;
    private boolean filtrarPorCatalogo = false;
    
	private Boolean exibirCatalogo;
	private String campoConsultarPeriodico;
	private String valorConsultarPeriodico;
	private List<CatalogoVO> listaConsultarPeriodico;
	private String tipoSaida;
	private List<SelectItem> listaSelectItemTipoSaida;

    public SituacaoExemplaresRelControle() {
        novo();
    }

    public void novo() {
        try {
            setMensagemID("msg_entre_dados");
            setSituacaoExemplaresRelVO(new SituacaoExemplaresRelVO());
            getSituacaoExemplaresRelVO().setUnidadeEnsino(new UnidadeEnsinoVO());
            getSituacaoExemplaresRelVO().setExemplar(new ExemplarVO());
            getSituacaoExemplaresRelVO().getExemplar().setSituacaoAtual("");
            getSituacaoExemplaresRelVO().getExemplar().setBiblioteca(new BibliotecaVO());
            getSituacaoExemplaresRelVO().getExemplar().setCatalogo(new CatalogoVO());
            getSituacaoExemplaresRelVO().getExemplar().getCatalogo().setTitulo("");
            getSituacaoExemplaresRelVO().getExemplar().getCatalogo().setNivelBibliografico("");
//            getSituacaoExemplaresRelVO().getExemplar().getCatalogo().setClassificacaoBibliografica(new ClassificacaoBibliograficaVO());
//            montarListaSelectItemUnidadeEnsino();
        } catch (Exception e) {
            setMensagemID(e.getMessage());
        }
    }

    public void imprimirPDF() throws Exception {
        List<SituacaoExemplaresRelVO> listaObjetos = null;
        try {
            getFacadeFactory().getSituacaoExemplaresRelFacade().validarDados(getSituacaoExemplaresRelVO());
            listaObjetos = getFacadeFactory().getSituacaoExemplaresRelFacade().criarObjeto(getSituacaoExemplaresRelVO(), getTipoSaida());

            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getSituacaoExemplaresRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getSituacaoExemplaresRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Situação de Exemplares");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getSituacaoExemplaresRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                
				if (getSituacaoExemplaresRelVO().getExemplar().getSituacaoAtual().equals(SituacaoExemplar.INUTILIZADO.getValor())) {
					getSuperParametroRelVO().adicionarParametro("tipoSaida", TipoSaidaAcervo.getEnum(getTipoSaida()).getDescricao());
				} else {
					getSuperParametroRelVO().adicionarParametro("tipoSaida", "");
				}

                getSuperParametroRelVO().setUnidadeEnsino(
                        (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getSituacaoExemplaresRelVO().getUnidadeEnsino().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());

                String biblioteca = "Todas";
                if (getSituacaoExemplaresRelVO().getExemplar().getBiblioteca().getCodigo() > 0) {
                    biblioteca = (getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getSituacaoExemplaresRelVO().getExemplar().getBiblioteca().getCodigo(),
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome();
                }
                getSuperParametroRelVO().setBiblioteca(biblioteca);
                biblioteca = null;

                String secao = "Todas";
                if (getSituacaoExemplaresRelVO().getExemplar().getSecao().getCodigo() > 0) {
                    secao = (getFacadeFactory().getSecaoFacade().consultarPorChavePrimaria(getSituacaoExemplaresRelVO().getExemplar().getSecao().getCodigo(),
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome();
                }
                getSuperParametroRelVO().setSecao(secao);
                if(getSituacaoExemplaresRelVO().getExemplar().getCatalogo().getNivelBibliografico().equals("TODOS")){
                	getSuperParametroRelVO().setNivelBibliografico("Todos");
                }
                else{
                getSuperParametroRelVO().setNivelBibliografico(
                        SituacaoExemplaresRelEnum.getDescricao(getSituacaoExemplaresRelVO().getExemplar().getCatalogo().getNivelBibliografico()));
                }

                getSuperParametroRelVO().setCatalogo(
                        getSituacaoExemplaresRelVO().getExemplar().getCatalogo().getTitulo().equals("") ? "Todos" : getSituacaoExemplaresRelVO().getExemplar().getCatalogo().getTitulo());

                getSuperParametroRelVO().setSituacao(getSituacaoExemplaresRelVO().getExemplar().getSituacaoAtual().equals("0") ? "Todas" : SituacaoExemplar.getDescricao(getSituacaoExemplaresRelVO().getExemplar().getSituacaoAtual()));
                UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getSituacaoExemplaresRelVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
				if (ue.getExisteLogoRelatorio()) {
					String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
					String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
				} else {
					getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
				}
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
                setFazerDownload(false);
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo
     * <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String nomeUnidadeEnsino) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomeUnidadeEnsino, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);            
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
                if(!Uteis.isAtributoPreenchido(getSituacaoExemplaresRelVO().getUnidadeEnsino())) {
                	getSituacaoExemplaresRelVO().setUnidadeEnsino(obj);
                	montarListaSelectItemBiblioteca();
                	montarListaSelectItemSecao();
                }
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Biblioteca</code>.
     */
    public void montarListaSelectItemBiblioteca() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        try {
            if (getSituacaoExemplaresRelVO().getUnidadeEnsino().getCodigo() > 0) {

                List<BibliotecaVO> resultadoConsulta = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(
                        getSituacaoExemplaresRelVO().getUnidadeEnsino().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

                for (BibliotecaVO bibliotecaVO : resultadoConsulta) {
                    objs.add(new SelectItem(bibliotecaVO.getCodigo(), bibliotecaVO.getNome().toString()));
                    if(!Uteis.isAtributoPreenchido(getSituacaoExemplaresRelVO().getExemplar().getBiblioteca())) {
                    	getSituacaoExemplaresRelVO().getExemplar().setBiblioteca(bibliotecaVO);
                    }
                }
                setListaSelectItemBiblioteca(objs);
                montarListaSelectItemSecao();
            } else {
                getListaSelectItemBiblioteca().clear();
                getListaSelectItemSecao().clear();
                getListaSelectItemNivelBibliografico().clear();
                getListaSelectItemSituacao().clear();
                limparCampoCatalogo();
            }
        } catch (Exception e) {
            setMensagemID(e.getMessage());
        } finally {
            objs = null;
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List<SelectItem> getTipoConsultarComboClassificacaoBibliografica() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List<SelectItem> getTipoConsultarComboCatalogo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("titulo", "Título"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void consultarCatalogo() {
        try {
            List<CatalogoVO> objs = new ArrayList<CatalogoVO>(0);
            objs = getFacadeFactory().getCatalogoFacade().consultarRichModalSituacaoExemplarRel(valorConsultarCatalogo, campoConsultarCatalogo, 
            		getSituacaoExemplaresRelVO().getExemplar().getBiblioteca().getCodigo(),
            		getSituacaoExemplaresRelVO().getExemplar().getSecao().getCodigo(), 
            		getSituacaoExemplaresRelVO().getExemplar().getSituacaoAtual(), 
            		getSituacaoExemplaresRelVO().getExemplar().getCatalogo().getNivelBibliografico(), 
            		getUsuarioLogado());
            setListaConsultarCatalogo(objs);
            setMensagemID("msg_dados_consultados");
        }
        catch (NumberFormatException e) {
            getListaConsultarCatalogo().clear();
            setMensagemDetalhada("msg_erro", "Favor digitar somente números.");
        } 
        catch (Exception e) {
            getListaConsultarCatalogo().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

//    public void consultarClassificacaoBibliografica() {
//        try {
//            List<ClassificacaoBibliograficaVO> objs = new ArrayList<ClassificacaoBibliograficaVO>(0);
//            if (getCampoConsultarClassificacaoBibliografica().equals("codigo")) {
//                if (getValorConsultarClassificacaoBibliografica().equals("")) {
//                    setValorConsultarClassificacaoBibliografica("0");
//                }
//                int valorInt = Integer.parseInt(getValorConsultarClassificacaoBibliografica());
//                objs = getFacadeFactory().getClassificacaoBibliograficaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//            }
//            if (getCampoConsultarClassificacaoBibliografica().equals("titulo")) {
//                objs = getFacadeFactory().getClassificacaoBibliograficaFacade().consultarPorNome(getValorConsultarClassificacaoBibliografica(), false,
//                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//            }
//            setListaConsultarClassificacaoBibliografica(objs);
//            setMensagemID("msg_dados_consultados");
//        } catch (Exception e) {
//            getListaConsultarClassificacaoBibliografica().clear();
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
//    }

//    public void selecionarClassificacaoBibliografica() throws Exception {
//        ClassificacaoBibliograficaVO obj = (ClassificacaoBibliograficaVO) context().getExternalContext().getRequestMap().get("ClassificacaoBibliografica");
//        getSituacaoExemplaresRelVO().getExemplar().getCatalogo().setClassificacaoBibliografica(obj);
//        getListaConsultarClassificacaoBibliografica().clear();
//        setValorConsultarClassificacaoBibliografica(null);
//        setCampoConsultarClassificacaoBibliografica(null);
//    }

//    public void limparCampoClassificacaoBibliografica() {
//        getSituacaoExemplaresRelVO().getExemplar().getCatalogo().setClassificacaoBibliografica(new ClassificacaoBibliograficaVO());
//    }

    public void selecionarCatalogo() throws Exception {
        CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
        getSituacaoExemplaresRelVO().getExemplar().setCatalogo(obj);
        getListaConsultarCatalogo().clear();
        setValorConsultarCatalogo(null);
        setCampoConsultarCatalogo(null);
    }

    public void limparCampoCatalogo() {
        getSituacaoExemplaresRelVO().getExemplar().setCatalogo(new CatalogoVO());
    }
    
	public void selecionarPeriodico() throws Exception {
		CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("periodicoItens");
		
		if (getMensagemDetalhada().equals("")) {
			getSituacaoExemplaresRelVO().getExemplar().setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
		}
		Uteis.liberarListaMemoria(this.getListaConsultarPeriodico());
		this.setValorConsultarPeriodico(null);
		this.setCampoConsultarPeriodico(null);
	}
	
	public void consultarPeriodico(){
		try {
			List objs = new ArrayList(0);
			
			if(getCampoConsultarPeriodico().equals("nome")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTituloCatalogo(getValorConsultarPeriodico(), "", null, null, false, true, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			}
			setListaConsultarPeriodico(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarPeriodico(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
    public List getTipoConsultarComboPeriodico() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }
    
    public List getComboboxTipo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem(0, "Catalogo"));
        itens.add(new SelectItem(1, "Periodico"));
        itens.add(new SelectItem(2, "Ambos"));
        return itens;
    }
    
	public void selecionaTipo(){
		if (getSituacaoExemplaresRelVO().getTipo() == 0) {
			setExibirCatalogo(true);
		} else if (getSituacaoExemplaresRelVO().getTipo() == 1) {
			setExibirCatalogo(false);
		}
	}

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Secao</code>.
     */
    public void montarListaSelectItemSecao() throws Exception {
        try {
            if (getSituacaoExemplaresRelVO().getExemplar().getBiblioteca().getCodigo() > 0) {
            	setFiltrarPorCatalogo(true);

                setListaSelectItemSecao(UtilSelectItem.getListaSelectItem(
                        getFacadeFactory().getSecaoFacade().consultarPorNome("", false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()),
                        "codigo", "nome"));
                montarListaSelectItemNivelBibliografico();
                montarListaSelectItemSituacao();
            } else {
            	setFiltrarPorCatalogo(false);
                getListaSelectItemSecao().clear();
                getListaSelectItemNivelBibliografico().clear();
                getListaSelectItemSituacao().clear();
                limparCampoCatalogo();
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo
     * <code>NivelBibliografico</code>.
     */
    public void montarListaSelectItemNivelBibliografico() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        try {
        	objs.add(new SelectItem("TODOS" , "Todos"));
            objs.add(new SelectItem("MO", "Monográfico"));
            objs.add(new SelectItem("SE", "Série"));
            setListaSelectItemNivelBibliografico(objs);
        } finally {
            objs = null;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Situacao</code>.
     */
    public void montarListaSelectItemSituacao() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem(0, "Todas"));
        itens.add(new SelectItem(SituacaoExemplar.DISPONIVEL.getValor(), SituacaoExemplar.DISPONIVEL.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.CONSULTA.getValor(), SituacaoExemplar.CONSULTA.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.EMPRESTADO.getValor(), SituacaoExemplar.EMPRESTADO.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.INUTILIZADO.getValor(), SituacaoExemplar.INUTILIZADO.getDescricao()));
        setListaSelectItemSituacao(itens);
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
            montarListaSelectItemUnidadeEnsino();
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List<SelectItem> getListaSelectItemBiblioteca() {
        if (listaSelectItemBiblioteca == null) {
            listaSelectItemBiblioteca = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemBiblioteca;
    }

    public void setListaSelectItemBiblioteca(List<SelectItem> listaSelectItemBiblioteca) {
        this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
    }

    public SituacaoExemplaresRelVO getSituacaoExemplaresRelVO() {
        if (situacaoExemplaresRelVO == null) {
            situacaoExemplaresRelVO = new SituacaoExemplaresRelVO();
        }
        return situacaoExemplaresRelVO;
    }

    public void setSituacaoExemplaresRelVO(SituacaoExemplaresRelVO situacaoExemplaresRelVO) {
        this.situacaoExemplaresRelVO = situacaoExemplaresRelVO;
    }

    public String getCampoConsultarCatalogo() {
        return campoConsultarCatalogo;
    }

    public void setCampoConsultarCatalogo(String campoConsultarCatalogo) {
        this.campoConsultarCatalogo = campoConsultarCatalogo;
    }

    public String getValorConsultarCatalogo() {
        return valorConsultarCatalogo;
    }

    public void setValorConsultarCatalogo(String valorConsultarCatalogo) {
        this.valorConsultarCatalogo = valorConsultarCatalogo;
    }

    public List<CatalogoVO> getListaConsultarCatalogo() {
    	if(listaConsultarCatalogo == null){
    		listaConsultarCatalogo = new ArrayList(0);
    	}
        return listaConsultarCatalogo;
    }

    public void setListaConsultarCatalogo(List<CatalogoVO> listaConsultarCatalogo) {
        this.listaConsultarCatalogo = listaConsultarCatalogo;
    }

//    public String getCampoConsultarClassificacaoBibliografica() {
//        return campoConsultarClassificacaoBibliografica;
//    }
//
//    public void setCampoConsultarClassificacaoBibliografica(String campoConsultarClassificacaoBibliografica) {
//        this.campoConsultarClassificacaoBibliografica = campoConsultarClassificacaoBibliografica;
//    }
//
//    public String getValorConsultarClassificacaoBibliografica() {
//        return valorConsultarClassificacaoBibliografica;
//    }
//
//    public void setValorConsultarClassificacaoBibliografica(String valorConsultarClassificacaoBibliografica) {
//        this.valorConsultarClassificacaoBibliografica = valorConsultarClassificacaoBibliografica;
//    }
//
//    public List<ClassificacaoBibliograficaVO> getListaConsultarClassificacaoBibliografica() {
//        return listaConsultarClassificacaoBibliografica;
//    }
//
//    public void setListaConsultarClassificacaoBibliografica(List<ClassificacaoBibliograficaVO> listaConsultarClassificacaoBibliografica) {
//        this.listaConsultarClassificacaoBibliografica = listaConsultarClassificacaoBibliografica;
//    }

    public List<SelectItem> getListaSelectItemSecao() {
        if (listaSelectItemSecao == null) {
            listaSelectItemSecao = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemSecao;
    }

    public void setListaSelectItemSecao(List<SelectItem> listaSelectItemSecao) {
        this.listaSelectItemSecao = listaSelectItemSecao;
    }

    public List<SelectItem> getListaSelectItemNivelBibliografico() {
        if (listaSelectItemNivelBibliografico == null) {
            listaSelectItemNivelBibliografico = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNivelBibliografico;
    }

    public void setListaSelectItemNivelBibliografico(List<SelectItem> listaSelectItemNivelBibliografico) {
        this.listaSelectItemNivelBibliografico = listaSelectItemNivelBibliografico;
    }

    public List<SelectItem> getListaSelectItemSituacao() {
        if (listaSelectItemSituacao == null) {
            listaSelectItemSituacao = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemSituacao;
    }

    public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
        this.listaSelectItemSituacao = listaSelectItemSituacao;
    }

	public boolean isFiltrarPorCatalogo() {
		return filtrarPorCatalogo;
	}

	public void setFiltrarPorCatalogo(boolean filtrarPorCatalogo) {
		this.filtrarPorCatalogo = filtrarPorCatalogo;
	}

	public Boolean getExibirCatalogo() {
		if(exibirCatalogo == null){
			exibirCatalogo = Boolean.TRUE;
		}
		return exibirCatalogo;
	}

	public void setExibirCatalogo(Boolean exibirCatalogo) {
		this.exibirCatalogo = exibirCatalogo;
	}

	public String getCampoConsultarPeriodico() {
		if(campoConsultarPeriodico== null){
			campoConsultarPeriodico = "";
		}
		return campoConsultarPeriodico;
	}

	public void setCampoConsultarPeriodico(String campoConsultarPeriodico) {
		this.campoConsultarPeriodico = campoConsultarPeriodico;
	}

	public String getValorConsultarPeriodico() {
		if(valorConsultarPeriodico== null){
			valorConsultarPeriodico = "";
		}
		return valorConsultarPeriodico;
	}

	public void setValorConsultarPeriodico(String valorConsultarPeriodico) {
		this.valorConsultarPeriodico = valorConsultarPeriodico;
	}

	public List<CatalogoVO> getListaConsultarPeriodico() {
		if(listaConsultarPeriodico == null){
			listaConsultarPeriodico = new ArrayList<CatalogoVO>(0);
		}
		return listaConsultarPeriodico;
	}

	public void setListaConsultarPeriodico(List<CatalogoVO> listaConsultarPeriodico) {
		this.listaConsultarPeriodico = listaConsultarPeriodico;
	}

	public String getTipoSaida() {
		if (tipoSaida == null) {
			tipoSaida = "";
		}
		return tipoSaida;
	}

	public void setTipoSaida(String tipoSaida) {
		this.tipoSaida = tipoSaida;
	}

	public List<SelectItem> getListaSelectItemTipoSaida() {
		if (listaSelectItemTipoSaida == null) {
			listaSelectItemTipoSaida = new ArrayList<SelectItem>();
		}
		return listaSelectItemTipoSaida;
	}

	public void setListaSelectItemTipoSaida(List<SelectItem> listaSelectItemTipoSaida) {
		this.listaSelectItemTipoSaida = listaSelectItemTipoSaida;
	}
	
	public void montarListaSelectItemTipoSaida() {
		setListaSelectItemTipoSaida(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoSaidaAcervo.class, false));
	}
}
