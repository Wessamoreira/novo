package controle.processosel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas TextoPadraoForm.jsp
 * TextoPadraoCons.jsp) com as funcionalidades da classe <code>TextoPadrao</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see TextoPadrao
 * @see TextoPadraoProcessoSeletivoVO
 */
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.MarcadorVO;
import negocio.comuns.financeiro.TextoPadraoTagVO;
import negocio.comuns.processosel.TextoPadraoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("TextoPadraoProcessoSeletivoControle")
@Scope("viewScope")
@Lazy
public class TextoPadraoProcessoSeletivoControle extends SuperControleRelatorio implements Serializable {

    private TextoPadraoProcessoSeletivoVO textoPadraoProcessoSeletivoVO;
    protected Boolean existeTexto;
    private List listaContaReceber;
    protected String variavelTelaContaReceber;
    private List listaSelectItemUnidadeEnsino;
    private ArquivoVO arquivoVO;
    private List<ArquivoVO> arquivoVOs;
	private List listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;	

    
    public TextoPadraoProcessoSeletivoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        inicializarDadosListaSelectItemUnidadelEnsino();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void inicializarDadosListaSelectItemUnidadelEnsino() {
        try {
            List<UnidadeEnsinoVO> listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
	public List<SelectItem> getListaSelectItemTipoNivelEducacional() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class);
	}

    public void inicializarUsuarioLogado() {
        try {
            textoPadraoProcessoSeletivoVO.setResponsavelDefinicao(getUsuarioLogadoClone());
        } catch (Exception exception) {
        }
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>TextoPadrao</code> para edição pelo usuário
     * da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        inicializarDadosListaSelectItemUnidadelEnsino();
        setTextoPadraoProcessoSeletivoVO(new TextoPadraoProcessoSeletivoVO());
        inicializarUsuarioLogado();
        setListaContaReceber(new ArrayList(0));
        setVariavelTelaContaReceber("");
        montarListaArquivosImagem();
        executarFormatarLayout();
        setExisteTexto(Boolean.FALSE);
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TextoPadrao</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        TextoPadraoProcessoSeletivoVO obj = (TextoPadraoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("planoTextoPadrao");
        obj.setNovoObj(Boolean.FALSE);
        setTextoPadraoProcessoSeletivoVO(obj);
        inicializarDadosListaSelectItemUnidadelEnsino();
        montarListaArquivosImagem();
        setExisteTexto(Boolean.FALSE);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoForm.xhtml");
    }
    
    public void montarListaArquivosImagem() {
    	try {
    		setArquivoVOs(getFacadeFactory().getArquivoFacade().consultarArquivosPorPastaBaseArquivo("imagemTextoPadrao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TEXTOPADRAO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
			if (!getArquivoVO().getNome().equals("")) {
				try {
					getFacadeFactory().getArquivoFacade().incluir(getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
		}
	}
    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TextoPadrao</code>. Caso
     * o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            if (textoPadraoProcessoSeletivoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().incluir(textoPadraoProcessoSeletivoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            } else {
                getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().alterar(textoPadraoProcessoSeletivoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            //return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            //return "editar";
        }
    }

    public String clonar() {
        try {
            textoPadraoProcessoSeletivoVO.clonar(getUsuarioLogadoClone());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TextoPadraoCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
                objs = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataDefinicao")) {
                objs = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().consultarPorDataDefinicao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("responsavelDefinicao")) {
                objs = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().consultarPorResponsavelDefinicao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoCons.xhtml");
        }
    }
    
    public void adicionarNovaPaginaTexto() {
    	try {
    		StringBuilder sb = new StringBuilder();
    		if (getTextoPadraoProcessoSeletivoVO().getOrientacaoDaPagina().equals("PA")) {
	    		sb.append("<div class='page' style='width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;'>");
	    		sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;'><p>Nova Página</p></div>");
	    		sb.append("</div></body>");
    		} else {
	    		sb.append("<div class='page' style='padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;'>");
	    		sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;'><p>Nova Página</p></div>");
	    		sb.append("</div></body>"); 			
    		}
    		getTextoPadraoProcessoSeletivoVO().setTexto(getTextoPadraoProcessoSeletivoVO().getTexto().replaceAll("</body>", sb.toString()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void adicionarMargensPagina() {
    	try {
    		StringBuilder sb = new StringBuilder();
    		sb.append("padding-top: ").append(getTextoPadraoProcessoSeletivoVO().getMargemSuperior()).append("cm; ");
    		sb.append("padding-bottom: ").append(getTextoPadraoProcessoSeletivoVO().getMargemInferior()).append("cm; ");
    		sb.append("padding-left: ").append(getTextoPadraoProcessoSeletivoVO().getMargemEsquerda()).append("cm; ");
    		sb.append("padding-right: ").append(getTextoPadraoProcessoSeletivoVO().getMargemDireita()).append("cm; ");
    		if (getTextoPadraoProcessoSeletivoVO().getOrientacaoDaPagina().equals("PA")) {
	    		sb.append("height: 150mm; ");
    		} else {
    			sb.append("height: 237mm; ");
    		}
    		getTextoPadraoProcessoSeletivoVO().setTexto(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().substituirValorAtribuidoClass(getTextoPadraoProcessoSeletivoVO().getTexto(), ".subpage", sb.toString()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void alterarOrientacaoPagina() {
    	try {
			if (getTextoPadraoProcessoSeletivoVO().getOrientacaoDaPagina().equals("PA")) {
				getTextoPadraoProcessoSeletivoVO().setTexto(getTextoPadraoProcessoSeletivoVO().getTexto().replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;"));
				getTextoPadraoProcessoSeletivoVO().setTexto(getTextoPadraoProcessoSeletivoVO().getTexto().replaceAll("border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;", "border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;"));
	    		
		        getTextoPadraoProcessoSeletivoVO().setTexto(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().substituirValorAtribuidoClass(getTextoPadraoProcessoSeletivoVO().getTexto(), ".page", "width: 29.7cm; min-height: 21cm; padding: 2cm; margin: 1cm auto;"));
		        getTextoPadraoProcessoSeletivoVO().setTexto(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().substituirValorAtribuidoClass(getTextoPadraoProcessoSeletivoVO().getTexto(), ".subpage", "padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 150mm;"));
				getTextoPadraoProcessoSeletivoVO().setTexto(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().substituirValorAtribuidoClass(getTextoPadraoProcessoSeletivoVO().getTexto(), "@page", "size: A4 landscape; margin: 0;"));
			} else {
				getTextoPadraoProcessoSeletivoVO().setTexto(getTextoPadraoProcessoSeletivoVO().getTexto().replaceAll("width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;", "padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;"));
				getTextoPadraoProcessoSeletivoVO().setTexto(getTextoPadraoProcessoSeletivoVO().getTexto().replaceAll("border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;", "border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;"));
				
		        getTextoPadraoProcessoSeletivoVO().setTexto(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().substituirValorAtribuidoClass(getTextoPadraoProcessoSeletivoVO().getTexto(), ".page", "width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto;"));
		        getTextoPadraoProcessoSeletivoVO().setTexto(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().substituirValorAtribuidoClass(getTextoPadraoProcessoSeletivoVO().getTexto(), ".subpage", "padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm;"));
				getTextoPadraoProcessoSeletivoVO().setTexto(getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().substituirValorAtribuidoClass(getTextoPadraoProcessoSeletivoVO().getTexto(), "@page", "size: A4; margin: 0;"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void executarFormatarLayout() {
    	try {
    		StringBuilder html = new StringBuilder();
    		html.append("<html>");
    		html.append("<head>");
    		html.append("<style type='text/css'>");
    		html.append(" body { margin: 0; padding: 0; font-size:11px; } ");
    		html.append(" th { font-weight: normal; } ");
    		html.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
    		html.append(" .page { width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto; } ");
    		html.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm; } ");
    		html.append(" @page { size: A4; margin: 0; } ");
    		html.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
	        html.append("</style>");
	        html.append("</head>");
	        html.append("<body>");
    		
    		String parte1, parte2, texto = "";
    		texto = getTextoPadraoProcessoSeletivoVO().getTexto();
    		parte1 = texto.substring(texto.indexOf("<body>")+6, texto.length());
			parte2 = parte1.substring(0, parte1.indexOf("</body>"));
			texto = parte2;
			
			if(!((texto.contains("<div class=\"page\"") && texto.contains("<div class=\"subpage\"")) || (texto.contains("<div class='page'") && texto.contains("<div class='subpage'")))) {
				html.append("<div class=\"page\" style=\"padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;\">");
				html.append("<div class=\"subpage\" style=\"border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;\">");
				html.append(texto);
				html.append("</div></div>");
			} else {
				html.append(texto);
			}
			
			html.append("</body>");
			html.append("</html>");
			
			getTextoPadraoProcessoSeletivoVO().setTexto(html.toString());
			alterarOrientacaoPagina();
    		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public String getVisualizarTexto() {
    	try {
//			StringBuilder sb = new StringBuilder();
//			sb.append("<script type='text/javascript'>");
//			sb.append("window.onload=function(){ window.print(); }");
//			sb.append("</script></body>");
//			getTextoPadraoProcessoSeletivoVO().setTexto(getTextoPadraoProcessoSeletivoVO().getTexto().replaceAll("</body>", sb.toString()));
    		
    		    		
    		String texto = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().removerBordaDaPagina(getTextoPadraoProcessoSeletivoVO().getTexto());
    		texto = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().adicionarStyleFormatoPaginaTextoPadrao(texto, getTextoPadraoProcessoSeletivoVO().getOrientacaoDaPagina());
			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
	        request.getSession().setAttribute("textoRelatorio", texto);
	        return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
    }
    
    public void realizarVisualizacaoTexto() {
    	try {    	
    		limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			if(getTextoPadraoProcessoSeletivoVO().getTipoDesigneTextoEnum().isHtml()){
				String texto = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().removerBordaDaPagina(getTextoPadraoProcessoSeletivoVO().getTexto());
	    		texto = getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().adicionarStyleFormatoPaginaTextoPadrao(texto, getTextoPadraoProcessoSeletivoVO().getOrientacaoDaPagina());
		        setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarConversaoHtmlParaPdf(texto, getTextoPadraoProcessoSeletivoVO(), getUsuarioLogado()));	
			}else{
				ImpressaoContratoVO impressao = new ImpressaoContratoVO();
				setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressao, getTextoPadraoProcessoSeletivoVO(), "", false, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			}
			setFazerDownload(true);
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void limparDadosUpLoadArquivoIreport() throws Exception {
    	getTextoPadraoProcessoSeletivoVO().setArquivoIreport(new ArquivoVO());
    	getTextoPadraoProcessoSeletivoVO().setTexto("");
	}
	
	public void upLoadArquivoIreport(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getTextoPadraoProcessoSeletivoVO().getArquivoIreport(), getConfiguracaoGeralPadraoSistema(),PastaBaseArquivoEnum.IREPORT_TMP, getUsuarioLogado());
			File file = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getTextoPadraoProcessoSeletivoVO().getArquivoIreport().getPastaBaseArquivoEnum().getValue() + File.separator  + getTextoPadraoProcessoSeletivoVO().getArquivoIreport().getNome());
			getTextoPadraoProcessoSeletivoVO().setTexto(UteisTextoPadrao.carregarTagsTextoPadraoPorTipoDesignerIreportProcessoSeletivo(file));
			//getTextoPadraoProcessoSeletivoVO().setTexto(UteisTextoPadrao.carregarTagsTextoPadraoPorTipoDesignerIreport(file));
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
    
    /**
     * limpa os campos da tela
     * se selecionar uma opcao diferente de data de definição
     * os campos serão limpos
     * @return
     */
    public String realizarLimparCampos() {
        if (!getControleConsulta().getCampoConsulta().equals("dataDefinicao")) {
            getControleConsulta().setValorConsulta("");
        }
        return "";
    }

    public String adicionarMarcador() {
        setExisteTexto(Boolean.TRUE);
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoForm.xhtml");
    }

    public String textoConcatenado(String textoAntigo, String tag) {
        String texto = textoAntigo + " " + tag;
        return texto;
    }

    public void selecionarMarcadorCandidato() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorCandidato");
        String texto = adicionarMarcador(getTextoPadraoProcessoSeletivoVO().getTexto(), obj.getTag());
        getTextoPadraoProcessoSeletivoVO().setTexto(texto);
    }

    public void selecionarMarcadorInscricao() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorInscricao");
        String texto = adicionarMarcador(getTextoPadraoProcessoSeletivoVO().getTexto(), obj.getTag());
        getTextoPadraoProcessoSeletivoVO().setTexto(texto);
    }

    public void selecionarMarcadorProcessoSeletivo() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorProcessoSeletivo");
        String texto = adicionarMarcador(getTextoPadraoProcessoSeletivoVO().getTexto(), obj.getTag());
        getTextoPadraoProcessoSeletivoVO().setTexto(texto);
    }
    
    public void selecionarMarcadorListaProcessoSeletivo() {
    	MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorListaProcessoSeletivo");
    	String texto = adicionarMarcador(getTextoPadraoProcessoSeletivoVO().getTexto(), obj.getTag());
    	getTextoPadraoProcessoSeletivoVO().setTexto(texto);
    }

    public void selecionarMarcadorResultadoProcessoSeletivo() {
    	MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorResultadoProcessoSeletivo");
    	String texto = adicionarMarcador(getTextoPadraoProcessoSeletivoVO().getTexto(), obj.getTag());
    	getTextoPadraoProcessoSeletivoVO().setTexto(texto);
    }
    
    public String adicionarMarcador(String texto, String marcador) {
        int parametro = texto.lastIndexOf("</p>");
        if (parametro == -1) {
            parametro = texto.lastIndexOf("</body>");
        }
        TextoPadraoTagVO p = new TextoPadraoTagVO();
        p.setTag(marcador);
        String textoAntes = texto.substring(0, parametro);
        String textoDepois = texto.substring(parametro, texto.length());
        texto = textoAntes + " " + marcador + textoDepois;
        return texto;
    }

    public void selecionarMarcadorContaReceber() {
        String texto = adicionarMarcador(getTextoPadraoProcessoSeletivoVO().getTexto(), getVariavelTelaContaReceber());
        getTextoPadraoProcessoSeletivoVO().setTexto(texto);
    }

    public void setarTagVariavelContaReceber() {
        String marcadorFinal = "";
        Iterator i = listaContaReceber.iterator();
        while (i.hasNext()) {
            MarcadorVO obj = (MarcadorVO) i.next();
            if (obj.getSelecionado().booleanValue()) {
                marcadorFinal += obj.getTag() + ",";
            }
        }
        int tamanho = marcadorFinal.length();
        marcadorFinal = marcadorFinal.substring(0, tamanho - 2) + "]";
        marcadorFinal = marcadorFinal.replace("],[", ", ");
        setVariavelTelaContaReceber(marcadorFinal);
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TextoPadraoProcessoSeletivoVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getTextoPadraoProcessoSeletivoFacade().excluir(textoPadraoProcessoSeletivoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            setTextoPadraoProcessoSeletivoVO(new TextoPadraoProcessoSeletivoVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoProcessoSeletivoForm.xhtml");
        }
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemMarcadoCandidato() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadoCandidato();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = cliente.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) cliente.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            objs.add(marcador);
            marcador = new MarcadorVO();
        }
        return objs;
    }

    public List getListaSelectItemMarcadoInscricao() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadoInscricao();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = cliente.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) cliente.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            objs.add(marcador);
            marcador = new MarcadorVO();
        }
        return objs;
    }

    public List getListaSelectItemMarcadoProcessoSeletivo() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadoProcessoSeletivo();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = cliente.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) cliente.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            objs.add(marcador);
            marcador = new MarcadorVO();
        }
        return objs;
    }

    public List getListaSelectItemMarcadoListaProcessoSeletivo() throws Exception {
    	List objs = new ArrayList(0);
    	Hashtable cliente = (Hashtable) Dominios.getMarcadoListaProcessoSeletivo();
    	MarcadorVO marcador = new MarcadorVO();
    	Enumeration keys = cliente.keys();
    	while (keys.hasMoreElements()) {
    		String value = (String) keys.nextElement();
    		String label = (String) cliente.get(value);
    		marcador.setTag(value);
    		marcador.setNome(label);
    		objs.add(marcador);
    		marcador = new MarcadorVO();
    	}
    	return objs;
    }
    
    public List getListaSelectItemMarcadoResultadoProcessoSeletivo() throws Exception {
    	List objs = new ArrayList(0);
    	Hashtable cliente = (Hashtable) Dominios.getMarcadoResultadoProcessoSeletivo();
    	MarcadorVO marcador = new MarcadorVO();
    	Enumeration keys = cliente.keys();
    	while (keys.hasMoreElements()) {
    		String value = (String) keys.nextElement();
    		String label = (String) cliente.get(value);
    		marcador.setTag(value);
    		marcador.setNome(label);
    		objs.add(marcador);
    		marcador = new MarcadorVO();
    	}
    	return objs;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("dataDefinicao", "Data Definição"));
        itens.add(new SelectItem("responsavelDefinicao", "Responsável Definição"));
        return itens;
    }

    public Boolean getExisteTexto() {
        return existeTexto;
    }

    public void setExisteTexto(Boolean existeTexto) {
        this.existeTexto = existeTexto;
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

    public String getVariavelTelaContaReceber() {
        return variavelTelaContaReceber;
    }

    public void setVariavelTelaContaReceber(String variavelTelaContaReceber) {
        this.variavelTelaContaReceber = variavelTelaContaReceber;
    }

    public List getListaContaReceber() {
        return listaContaReceber;
    }

    public void setListaContaReceber(List listaContaReceber) {
        this.listaContaReceber = listaContaReceber;
    }

    /**
     * @return the listaSelectItemUnidadeEnsino
     */
    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    /**
     * @param listaSelectItemUnidadeEnsino the listaSelectItemUnidadeEnsino to set
     */
    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }
    
	public List<SelectItem> getListaSelectItemTipo() throws Exception {
		List objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("CA", "Candidato"));
		objs.add(new SelectItem("LI", "Listagem"));
        return objs;
	}

	public List<SelectItem> listaSelectItemOrientacaoDaPagina;
	public List<SelectItem> getListaSelectItemOrientacaoDaPagina() {
		if(listaSelectItemOrientacaoDaPagina == null){
			listaSelectItemOrientacaoDaPagina = new ArrayList<SelectItem>(0);
			listaSelectItemOrientacaoDaPagina.add(new SelectItem("RE", "Retrato"));
			listaSelectItemOrientacaoDaPagina.add(new SelectItem("PA", "Paisagem"));
		}
        return listaSelectItemOrientacaoDaPagina;
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

	public List<ArquivoVO> getArquivoVOs() {
		if (arquivoVOs == null) {
			arquivoVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoVOs;
	}

	public void setArquivoVOs(List<ArquivoVO> arquivoVOs) {
		this.arquivoVOs = arquivoVOs;
	}
	
	public void excluirArquivo() {
		try {
			ArquivoVO arquivoVO = (ArquivoVO)getRequestMap().get("imagem");
			arquivoVO.setDataIndisponibilizacao(new Date());
			arquivoVO.setManterDisponibilizacao(false);
			getFacadeFactory().getArquivoFacade().alterarManterDisponibilizacao(arquivoVO, getUsuarioLogado());
//			getFacadeFactory().getArquivoFacade().excluir(arquivoVO, false, "", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			montarListaArquivosImagem();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	} 
        
    public Boolean getIsTipoTextoPadraoEstagio() {
            if ((getTextoPadraoProcessoSeletivoVO().getTipo().equals("ES")) ||
                (getTextoPadraoProcessoSeletivoVO().getTipo().equals("EO")) ||
                (getTextoPadraoProcessoSeletivoVO().getTipo().equals("EC")) ||
                (getTextoPadraoProcessoSeletivoVO().getTipo().equals("EN"))) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
	}

    public Boolean getIsTipoTextoPadraoInscProcSeletivo() {
    	if (getTextoPadraoProcessoSeletivoVO().getTipo().equals("PS")) {
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    }

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));

		return itens;
	}

	public void downloadArquivoJasperIreport() throws Exception {
		try {
			File arquivo = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getTextoPadraoProcessoSeletivoVO().getArquivoIreport().getPastaBaseArquivoEnum().getValue() + File.separator  + getTextoPadraoProcessoSeletivoVO().getArquivoIreport().getNome());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void downloadTemplateIreport() throws Exception {
		try {
			File arquivo = new File(UteisJSF.getCaminhoBase() + File.separator+ "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator +  "template_procSeletivo_ireport.rar");
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	

}
