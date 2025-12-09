package controle.academico;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas TextoPadraoForm.jsp
 * TextoPadraoCons.jsp) com as funcionalidades da classe <code>TextoPadrao</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see TextoPadrao
 * @see TextoPadraoDeclaracaoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoFuncionarioVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;

import negocio.comuns.financeiro.TextoPadraoTagVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("TextoPadraoDeclaracaoControle")
@Scope("viewScope")
@Lazy
public class TextoPadraoDeclaracaoControle extends SuperControleRelatorio implements Serializable {

    private TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO;
    protected Boolean existeTexto;
    private List listaContaReceber;
    protected String variavelTelaContaReceber;
    private List listaSelectItemUnidadeEnsino;
    private ArquivoVO arquivoVO;
    private List<ArquivoVO> arquivoVOs;
	private List listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private ArquivoVO arquivoIreportSelecionado;
	private ArquivoVO arquivoIreport;
	private StringBuilder stb;

    public TextoPadraoDeclaracaoControle() throws Exception {
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
            textoPadraoDeclaracaoVO.setResponsavelDefinicao(getUsuarioLogadoClone());
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
        setTextoPadraoDeclaracaoVO(new TextoPadraoDeclaracaoVO());
        inicializarUsuarioLogado();
        setListaContaReceber(new ArrayList(0));
        setVariavelTelaContaReceber("");
        montarListaArquivosImagem();
       // executarFormatarLayout();
        setExisteTexto(Boolean.FALSE);
        getTextoPadraoDeclaracaoVO().setTipoDesigneTextoEnum(TipoDesigneTextoEnum.PDF);
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoForm.xhtml");
    }
    
    public void verificarExisteArquivoIreportPrincipal() {
    		setOncompleteModal("");
    		if(getTextoPadraoDeclaracaoVO().getListaArquivoIreport().isEmpty() || !getTextoPadraoDeclaracaoVO().possuiArquivoIreportPrincipal()) {
    			setOncompleteModal("PF('panelDefinirArquivoIreportPrincipal').show()");
    			return;
    		}
    		adicionarArquivoIreport();
    }
    
    public void adicionarArquivoIreport() {
    	try {
			getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarArquivoIreport(getTextoPadraoDeclaracaoVO().getListaArquivoIreport(), getArquivoIreport(), OrigemArquivo.TEXTO_PADRAO_DECLARACAO.getValor());
			setArquivoIreport(new ArquivoVO());
		} catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

	public void removerArquivoIreport() {
		try {
			getFacadeFactory().getArquivoFacade().excluir(getArquivoIreportSelecionado(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			getTextoPadraoDeclaracaoVO().getListaArquivoIreport().remove(getArquivoIreportSelecionado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TextoPadrao</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        TextoPadraoDeclaracaoVO obj = (TextoPadraoDeclaracaoVO) context().getExternalContext().getRequestMap().get("planoTextoPadraoItens");
        obj.setNovoObj(Boolean.FALSE);
        setTextoPadraoDeclaracaoVO(obj);
        inicializarDadosListaSelectItemUnidadelEnsino();
        montarListaArquivosImagem();
        setExisteTexto(Boolean.FALSE);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoForm.xhtml");
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
            if (textoPadraoDeclaracaoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getTextoPadraoDeclaracaoFacade().incluir(textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            } else {
                getFacadeFactory().getTextoPadraoDeclaracaoFacade().alterar(textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(),  getUsuarioLogado());
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
            textoPadraoDeclaracaoVO.clonar(getUsuarioLogadoClone());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoForm.xhtml");
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
                objs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), Arrays.asList(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
                objs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(),true, Arrays.asList(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataDefinicao")) {
                objs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorDataDefinicao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), Arrays.asList(), true,
                		Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("responsavelDefinicao")) {
                objs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorResponsavelDefinicao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Arrays.asList(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoCons.xhtml");
        }
    }
    
    public void adicionarNovaPaginaTexto() {
    	try {
    		StringBuilder sb = new StringBuilder();
    		if (getTextoPadraoDeclaracaoVO().getOrientacaoDaPagina().equals("PA")) {
	    		sb.append("<div class='page' style='width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;'>");
	    		sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;'><p>Nova Página</p></div>");
	    		sb.append("<div><span style='display:none'>&nbsp;</span>&nbsp;</div>");
	    		sb.append("</div></body>");
    		} else {
	    		sb.append("<div class='page' style='padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;'>");
	    		sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;'><p>Nova Página</p></div>");
	    		sb.append("<div><span style='display:none'>&nbsp;</span>&nbsp;</div>");
	    		sb.append("</div></body>"); 			
    		}
    		getTextoPadraoDeclaracaoVO().setTexto(getTextoPadraoDeclaracaoVO().getTexto().replaceAll("</body>", sb.toString()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void adicionarMargensPagina() {
    	try {
    		StringBuilder sb = new StringBuilder();
    		sb.append("padding-top: ").append(getTextoPadraoDeclaracaoVO().getMargemSuperior()).append("cm; ");
    		sb.append("padding-bottom: ").append(getTextoPadraoDeclaracaoVO().getMargemInferior()).append("cm; ");
    		sb.append("padding-left: ").append(getTextoPadraoDeclaracaoVO().getMargemEsquerda()).append("cm; ");
    		sb.append("padding-right: ").append(getTextoPadraoDeclaracaoVO().getMargemDireita()).append("cm; ");
    		if (getTextoPadraoDeclaracaoVO().getOrientacaoDaPagina().equals("PA")) {
	    		sb.append("height: 150mm; ");
    		} else {
    			sb.append("height: 237mm; ");
    		}
    		getTextoPadraoDeclaracaoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoDeclaracaoVO().getTexto(), ".subpage", sb.toString()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void alterarOrientacaoPagina() {
    	try {
			if (getTextoPadraoDeclaracaoVO().getOrientacaoDaPagina().equals("PA")) {
				getTextoPadraoDeclaracaoVO().setTexto(getTextoPadraoDeclaracaoVO().getTexto().replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;"));
				getTextoPadraoDeclaracaoVO().setTexto(getTextoPadraoDeclaracaoVO().getTexto().replaceAll("border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;", "border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;"));
	    		
		        getTextoPadraoDeclaracaoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoDeclaracaoVO().getTexto(), ".page", "width: 29.7cm; min-height: 21cm; padding: 2cm; margin: 1cm auto;"));
		        getTextoPadraoDeclaracaoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoDeclaracaoVO().getTexto(), ".subpage", "padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 150mm;"));
				getTextoPadraoDeclaracaoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoDeclaracaoVO().getTexto(), "@page", "size: A4 landscape; margin: 0;"));
			} else {
				getTextoPadraoDeclaracaoVO().setTexto(getTextoPadraoDeclaracaoVO().getTexto().replaceAll("width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;", "padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;"));
				getTextoPadraoDeclaracaoVO().setTexto(getTextoPadraoDeclaracaoVO().getTexto().replaceAll("border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;", "border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;"));
				
		        getTextoPadraoDeclaracaoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoDeclaracaoVO().getTexto(), ".page", "width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto;"));
		        getTextoPadraoDeclaracaoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoDeclaracaoVO().getTexto(), ".subpage", "padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm;"));
				getTextoPadraoDeclaracaoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoDeclaracaoVO().getTexto(), "@page", "size: A4; margin: 0;"));
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
    		texto = getTextoPadraoDeclaracaoVO().getTexto();
    		parte1 = texto.substring(texto.indexOf("<body>")+6, texto.length());
			parte2 = parte1.substring(0, parte1.indexOf("</body>"));
			texto = parte2;
			
			if(!((texto.contains("<div class=\"page\"") && texto.contains("<div class=\"subpage\"")) || (texto.contains("<div class='page'") && texto.contains("<div class='subpage'")))) {
				html.append("<div class=\"page\" style=\"padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;\">");
				html.append("<div class=\"subpage\" style=\"border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;\">");
				html.append("<div><span style='display:none'>&nbsp;</span>&nbsp;</div>");
				html.append(texto);
				html.append("</div></div>");
			} else {
				html.append(texto);
			}
			
			html.append("</body>");
			html.append("</html>");
			
			getTextoPadraoDeclaracaoVO().setTexto(html.toString());
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
//			getTextoPadraoDeclaracaoVO().setTexto(getTextoPadraoDeclaracaoVO().getTexto().replaceAll("</body>", sb.toString()));
    		
    		String texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(getTextoPadraoDeclaracaoVO().getTexto());
    		texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(texto, getTextoPadraoDeclaracaoVO().getOrientacaoDaPagina());
			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
	        request.getSession().setAttribute("textoRelatorio", texto);
	        return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
    }
    
    
    
    public void atualizarTipoDeclaracao() {
    	try {
    		limparDadosUpLoadArquivoIreport();
        	if (getTextoPadraoDeclaracaoVO().getTipo().equals("CO")) {
        		getTextoPadraoDeclaracaoVO().setTipoDesigneTextoEnum(TipoDesigneTextoEnum.PDF);
        	}
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void limparDadosUpLoadArquivoIreport() throws Exception {
    	setArquivoIreport(new ArquivoVO());
	}
	
	public void upLoadArquivoIreport(FileUploadEvent uploadEvent) {
		try {
			getArquivoIreport().setOrigem(OrigemArquivo.TEXTO_PADRAO_DECLARACAO.getValor());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent,getArquivoIreport(), getConfiguracaoGeralPadraoSistema(),PastaBaseArquivoEnum.IREPORT_TMP, getUsuarioLogado());
			File file = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getArquivoIreport().getPastaBaseArquivo() + File.separator  + getArquivoIreport().getNome());
			getStb().append(getTextoPadraoDeclaracaoVO().getTexto());
			getStb().append(UteisTextoPadrao.carregarTagsTextoPadraoPorTipoDesignerIreport(file));
			getTextoPadraoDeclaracaoVO().setTexto(stb.toString());
			verificarExisteArquivoIreportPrincipal();
			stb = new StringBuilder();
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
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoForm.xhtml");
    }

    public String textoConcatenado(String textoAntigo, String tag) {
        String texto = textoAntigo + " " + tag;
        return texto;
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
        String texto = adicionarMarcador(getTextoPadraoDeclaracaoVO().getTexto(), getVariavelTelaContaReceber());
        getTextoPadraoDeclaracaoVO().setTexto(texto);
    }

  

	public void consultarFuncionario() {
		List objs = new ArrayList(0);
		try {
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
//			executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}
		
    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TextoPadraoDeclaracaoVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
        	getFacadeFactory().getTextoPadraoDeclaracaoFacade().excluir(textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            setTextoPadraoDeclaracaoVO(new TextoPadraoDeclaracaoVO());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoDeclaracaoForm.xhtml");
        }
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
   

  
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

    public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracaoVO() {
        if (textoPadraoDeclaracaoVO == null) {
            textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
        }
        return textoPadraoDeclaracaoVO;
    }

    public void setTextoPadraoDeclaracaoVO(TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) {
        this.textoPadraoDeclaracaoVO = textoPadraoDeclaracaoVO;
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

    public void desmarcarOutrosCampos() {
        if (getTextoPadraoDeclaracaoVO().getControlarDocumentacaoPendente()) {
            getTextoPadraoDeclaracaoVO().setValidarApenasDocumentoSuspensao(false);
            getTextoPadraoDeclaracaoVO().setValidarTodosDocumentosCurso(false);
        }
    }

    public void marcarDesmarcarCampoValidarApenasDocumentoSuspensao() {
        getTextoPadraoDeclaracaoVO().setValidarApenasDocumentoSuspensao(!getTextoPadraoDeclaracaoVO().getValidarTodosDocumentosCurso());
    }

    public void marcarDesmarcarCampoValidarTodosDocumentosCurso() {
        getTextoPadraoDeclaracaoVO().setValidarTodosDocumentosCurso(!getTextoPadraoDeclaracaoVO().getValidarApenasDocumentoSuspensao());
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
    
	public List<SelectItem> getListaSelectItemTipoDeclaracao() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getTipoDeclaracao();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			objs.add(new SelectItem(value, label));
		}
		return Ordenacao.ordenarLista(objs, "label");
	}

	public Boolean getIsMostrarBotoes() {
		return getTextoPadraoDeclaracaoVO().getTipo().equals("OT") || getTextoPadraoDeclaracaoVO().getTipo().equals("TT") || getTextoPadraoDeclaracaoVO().getTipo().equals("CE");
	}
	
	public Boolean getIsTipoTextoPadraoAdvertencia() {
		return getTextoPadraoDeclaracaoVO().getTipo().equals("AD");
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
			ArquivoVO arquivoVO = (ArquivoVO)getRequestMap().get("imagemItens");
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
            if ((getTextoPadraoDeclaracaoVO().getTipo().equals("ES")) ||
                (getTextoPadraoDeclaracaoVO().getTipo().equals("EO")) ||
                (getTextoPadraoDeclaracaoVO().getTipo().equals("EC")) ||				
                (getTextoPadraoDeclaracaoVO().getTipo().equals("EN"))) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
	}

    public Boolean getIsTipoTextoPadraoInscProcSeletivo() {
    	if (getTextoPadraoDeclaracaoVO().getTipo().equals("PS")) {
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    }
    
    public Boolean getIsTipoTextoPadraoCartaCobranca() {
    	if (getTextoPadraoDeclaracaoVO().getTipo().equals("CO")) {
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

	public void adicionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			TextoPadraoDeclaracaoFuncionarioVO texto = new TextoPadraoDeclaracaoFuncionarioVO();
			texto.setFuncionario(obj);
			getTextoPadraoDeclaracaoVO().adicionarObjTextoPadraoDeclaracaoFuncionarioVOs(texto);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCampoConsultaFuncionario(null);
			setValorConsultaFuncionario(null);
		}		
	}
	
	public void removerFuncionario() throws Exception {
		TextoPadraoDeclaracaoFuncionarioVO obj = (TextoPadraoDeclaracaoFuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		getTextoPadraoDeclaracaoVO().excluirObjTextoPadraoDeclaracaoFuncionarioVOs(obj.getFuncionario().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}	
	
	public void downloadArquivoJasperIreport() throws Exception {
		try {
			File arquivo = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getTextoPadraoDeclaracaoVO().getArquivoIreport().getPastaBaseArquivoEnum().getValue() + File.separator  + getTextoPadraoDeclaracaoVO().getArquivoIreport().getNome());
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
			File arquivo = null;
			if (getTextoPadraoDeclaracaoVO().getTipo().equals("CO")) {
				arquivo = new File(UteisJSF.getCaminhoBase() + File.separator+ "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator +  "template_textoPadraoCartaCobranca_ireport.zip");
			}else if(getTextoPadraoDeclaracaoVO().getTipo().equals("AM")) {
				arquivo = new File(UteisJSF.getCaminhoBase() + File.separator+ "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator +  "template_professor_ireport.rar");
			}else if(getTextoPadraoDeclaracaoVO().getTipo().equals("DQ")) {
				arquivo = new File(UteisJSF.getCaminhoBase() + File.separator+ "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator +  "template_DeclaracaoImpostoRendaRel_ireport.rar");
			} else {
				arquivo = new File(UteisJSF.getCaminhoBase() + File.separator+ "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator +  "template_textoPadrao_ireport.rar");
			}
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
	
	public List<SelectItem> getListaSelectTipoDesigneTextoEnum() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		if (getIsTipoTextoPadraoCartaCobranca()) {
			lista.add(new SelectItem(TipoDesigneTextoEnum.PDF, UteisJSF.internacionalizar("enum_" + TipoDesigneTextoEnum.PDF.getClass().getSimpleName() + "_" + TipoDesigneTextoEnum.PDF.toString())));
		} else {
			for (Enum enumerador : TipoDesigneTextoEnum.values()) {
				lista.add(new SelectItem(enumerador, UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString())));
			}
		}
		return lista;
	}

	public ArquivoVO getArquivoIreportSelecionado() {
		if(arquivoIreportSelecionado == null) {
			arquivoIreportSelecionado = new ArquivoVO();
		}
		return arquivoIreportSelecionado;
	}

	public void setArquivoIreportSelecionado(ArquivoVO arquivoIreportSelecionado) {
		this.arquivoIreportSelecionado = arquivoIreportSelecionado;
	}

	public ArquivoVO getArquivoIreport() {
		if(arquivoIreport == null) {
			arquivoIreport = new ArquivoVO();
		}
		return arquivoIreport;
	}

	public void setArquivoIreport(ArquivoVO arquivoIreport) {
		this.arquivoIreport = arquivoIreport;
	}

	public StringBuilder getStb() {
		if(stb == null) {
			stb = new StringBuilder();
		}
		return stb;
	}

	public void setStb(StringBuilder stb) {
		this.stb = stb;
	}
	
	
}
