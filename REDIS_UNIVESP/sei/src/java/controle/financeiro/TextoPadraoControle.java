package controle.financeiro;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas TextoPadraoForm.jsp
 * TextoPadraoCons.jsp) com as funcionalidades da classe <code>TextoPadrao</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see TextoPadrao
 * @see TextoPadraoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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

import controle.academico.ExpedicaoDiplomaControle;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.financeiro.MarcadorVO;
import negocio.comuns.financeiro.TextoPadraoTagVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("TextoPadraoControle")
@Scope("viewScope")
@Lazy
public class TextoPadraoControle extends SuperControleRelatorio implements Serializable {

    private TextoPadraoVO textoPadraoVO;
    protected Boolean existeTexto;
    private List listaContaReceber;
    private List listaDisciplina;
    protected String variavelTelaContaReceber;
    protected String variavelTelaDisciplina;
    private List listaSelectItemUnidadeEnsino;
    private ArquivoVO arquivoVO;
    private List<ArquivoVO> arquivoVOs;
	private ArquivoVO arquivoIreportSelecionado;
	private ArquivoVO arquivoIreport;
	private StringBuilder stb;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private List<SelectItem> selectItemsCargoFuncionarioPrincipal;
	private List<SelectItem> selectItemsCargoFuncionarioSecundario;
	
    public TextoPadraoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public void inicializarUsuarioLogado() {
        try {
            textoPadraoVO.setResponsavelDefinicao(getUsuarioLogadoClone());
        } catch (Exception exception) {
        }
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>TextoPadrao</code> para edição pelo usuário
     * da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        montarListaSelectItemUnidadeEnsino();
        setTextoPadraoVO(new TextoPadraoVO());
        inicializarUsuarioLogado();
        setListaContaReceber(new ArrayList(0));
        setListaDisciplina(new ArrayList(0));
        setVariavelTelaContaReceber("");
        setVariavelTelaDisciplina("");
        setExisteTexto(Boolean.FALSE);
        getTextoPadraoVO().setTipoDesigneTextoEnum(TipoDesigneTextoEnum.PDF);
        montarListaArquivosImagem();
        //executarFormatarLayout();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TextoPadrao</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        TextoPadraoVO obj = (TextoPadraoVO) context().getExternalContext().getRequestMap().get("planoTextoPadraoItens");
        obj.setNovoObj(Boolean.FALSE);
        montarListaSelectItemUnidadeEnsino();
        setTextoPadraoVO(obj);
        montarListaArquivosImagem();		
        setExisteTexto(Boolean.FALSE);
        montarListaArquivosImagem();
        consultarFuncionarioPrincipal();
        consultarFuncionarioSecundario();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
    }

    public String ativarTextoPadrao() {
        try {
            if (textoPadraoVO.isNovoObj().booleanValue()) {
                throw new ConsistirException("Grave o texto padrão antes de tentar ativá-lo!");
            } else {
                textoPadraoVO.setSituacao("AT");
                getFacadeFactory().getTextoPadraoFacade().alterar(textoPadraoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            }
            setMensagemID("Texto padrão ATIVADO com sucesso!", Uteis.SUCESSO, true);
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
        }
    }

    public String desativarTextoPadrao() {
        try {
            if (!textoPadraoVO.getSituacao().equals("AT")) {
                throw new ConsistirException("Somente um Texto Padrão Ativo pode ser DESATIVADO!");
            } else {
                textoPadraoVO.setSituacao("IN");
                getFacadeFactory().getTextoPadraoFacade().alterar(textoPadraoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            }
            setMensagemID("Texto padrão DESATIVADO com sucesso!", Uteis.SUCESSO, true);            
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
        }
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
			if (!getArquivoVO().getNome().equals("")) {
				try {
					getFacadeFactory().getArquivoFacade().incluir(getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
					getArquivoVO().getDescricao().replace("\\", "/");
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}   
	
	public void excluirArquivo() {
		try {
			ArquivoVO arquivoVO = (ArquivoVO)getRequestMap().get("imagemItens");
			arquivoVO.setDataIndisponibilizacao(new Date());
			arquivoVO.setManterDisponibilizacao(false);
			getFacadeFactory().getArquivoFacade().alterarManterDisponibilizacao(arquivoVO, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			montarListaArquivosImagem();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	} 
	
    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TextoPadrao</code>. Caso
     * o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
        	if (textoPadraoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getTextoPadraoFacade().incluir(textoPadraoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            } else {
                getFacadeFactory().getTextoPadraoFacade().alterar(textoPadraoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            }
        	setMensagemID("Dados Gravados com Sucesso", Uteis.SUCESSO, true);
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
            //return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
            //return "editar";
        }
    }

    public String clonar() {
        try {
            textoPadraoVO.clonar(getUsuarioLogadoClone());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
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
                objs = getFacadeFactory().getTextoPadraoFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
                objs = getFacadeFactory().getTextoPadraoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataDefinicao")) {
                objs = getFacadeFactory().getTextoPadraoFacade().consultarPorDataDefinicao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("responsavelDefinicao")) {
                objs = getFacadeFactory().getTextoPadraoFacade().consultarPorResponsavelDefinicao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoCons.xhtml");
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
        return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
    }

    public String textoConcatenado(String textoAntigo, String tag) {
        String texto = textoAntigo + " " + tag;
        return texto;
    }
	

    public void adicionarNovaPaginaTexto() {
    	try {
    		StringBuilder sb = new StringBuilder();
    		if (getTextoPadraoVO().getOrientacaoDaPagina().equals("PA")) {
	    		sb.append("<div class='page' style='width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;'>");
	    		sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;'><p>Nova Página</p></div>");
	    		sb.append("<div><span style='display:none'>&nbsp;</span>&nbsp;</div>");
	    		sb.append("</div></body>");
    		} else {
    			sb.append("<div class='page' style='padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;'>");
	    		sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 190mm; padding-left: 1cm; padding-right: 1cm;'><p>Nova Página</p></div>");
	    		sb.append("<div><span style='display:none'>&nbsp;</span>&nbsp;</div>");
	    		sb.append("</div></body>"); 			
    		}
    		getTextoPadraoVO().setTexto(getTextoPadraoVO().getTexto().replaceAll("</body>", sb.toString()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void adicionarMargensPagina() {
    	try {
    		StringBuilder sb = new StringBuilder();
    		sb.append("padding-top: ").append(getTextoPadraoVO().getMargemSuperior()).append("cm; ");
    		sb.append("padding-bottom: ").append(getTextoPadraoVO().getMargemInferior()).append("cm; ");
    		sb.append("padding-left: ").append(getTextoPadraoVO().getMargemEsquerda()).append("cm; ");
    		sb.append("padding-right: ").append(getTextoPadraoVO().getMargemDireita()).append("cm; ");
    		if (getTextoPadraoVO().getOrientacaoDaPagina().equals("PA")) {
	    		sb.append("height: 150mm; ");
    		} else {
    			sb.append("height: 237mm; ");
    		}
    		if (!getTextoPadraoVO().getTexto().contains(".subpage")){
    			throw new Exception("O texto não está corretamente formatado para executar esta operação.");
    		}
    		getTextoPadraoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoVO().getTexto(), ".subpage", sb.toString()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void alterarOrientacaoPagina() {
    	try {
			if (getTextoPadraoVO().getOrientacaoDaPagina().equals("PA")) {
				getTextoPadraoVO().setTexto(getTextoPadraoVO().getTexto().replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;"));
				getTextoPadraoVO().setTexto(getTextoPadraoVO().getTexto().replaceAll("border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;", "border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;"));
	    		
		        getTextoPadraoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoVO().getTexto(), ".page", "width: 29.7cm; min-height: 21cm; padding: 2cm; margin: 1cm auto;"));
		        getTextoPadraoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoVO().getTexto(), ".subpage", "padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 150mm;"));
				getTextoPadraoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoVO().getTexto(), "@page", "size: A4 landscape; margin: 0;"));
			} else {
				getTextoPadraoVO().setTexto(getTextoPadraoVO().getTexto().replaceAll("width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;", "padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;"));
				getTextoPadraoVO().setTexto(getTextoPadraoVO().getTexto().replaceAll("border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;", "border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;"));
				
		        getTextoPadraoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoVO().getTexto(), ".page", "width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto;"));
		        getTextoPadraoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoVO().getTexto(), ".subpage", "padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm;"));
				getTextoPadraoVO().setTexto(getFacadeFactory().getTextoPadraoDeclaracaoFacade().substituirValorAtribuidoClass(getTextoPadraoVO().getTexto(), "@page", "size: A4; margin: 0;"));
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
    		texto = getTextoPadraoVO().getTexto();
			String tagBodyInicio = "<body>";
			String tagBodyFim = "</body>";
			if (!texto.contains(tagBodyInicio)) {
				texto = tagBodyInicio + texto; 
			}
			if (!texto.contains(tagBodyFim)) {
				texto =  texto + tagBodyFim; 
			}
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
			
			getTextoPadraoVO().setTexto(html.toString());
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
    		
    		String texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(getTextoPadraoVO().getTexto());
    		texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(texto, getTextoPadraoVO().getOrientacaoDaPagina());
			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
	        request.getSession().setAttribute("textoRelatorio", texto);
	        return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
    }
    
    public void visualizarTextoIreportPDF() {
		try {
			ImpressaoContratoVO impressao = new ImpressaoContratoVO();
			impressao.setGerarNovoArquivoAssinado(true);
			setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressao, getTextoPadraoVO(), "", false, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void limparDadosUpLoadArquivoIreport() throws Exception {
		getTextoPadraoVO().setArquivoIreport(new ArquivoVO());
		getTextoPadraoVO().setTexto("");
	}
	
	public void upLoadArquivoIreport(FileUploadEvent uploadEvent) {
		try {
			getArquivoIreport().setOrigem(OrigemArquivo.TEXTO_PADRAO.getValor());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoIreport(), getConfiguracaoGeralPadraoSistema(),PastaBaseArquivoEnum.IREPORT_TMP, getUsuarioLogado());
			File file = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getArquivoIreport().getPastaBaseArquivoEnum().getValue() + File.separator  + getArquivoIreport().getNome());
			if(!getTextoPadraoVO().getListaArquivoIreport().isEmpty()) {
				getStb().append(getTextoPadraoVO().getTexto());
			}else {
				setStb(new StringBuilder(""));
			}
			getStb().append(UteisTextoPadrao.carregarTagsTextoPadraoPorTipoDesignerIreport(file));
			getTextoPadraoVO().setTexto(stb.toString());
			verificarExisteArquivoIreportPrincipal();
			stb = new StringBuilder();
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	  public void verificarExisteArquivoIreportPrincipal() {
			setOncompleteModal("");
			if(getTextoPadraoVO().getListaArquivoIreport().isEmpty() || !getTextoPadraoVO().possuiArquivoIreportPrincipal()) {
				setOncompleteModal("RichFaces.$('panelDefinirArquivoIreportPrincipal').show()");
				return;
			}
			adicionarArquivoIreport();
	    }

		public void adicionarArquivoIreport() {
			try {
				getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarArquivoIreport(getTextoPadraoVO().getListaArquivoIreport(), getArquivoIreport(), OrigemArquivo.TEXTO_PADRAO.getValor());
				setArquivoIreport(new ArquivoVO());
			} catch (Exception e) {
				 setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		
		public void removerArquivoIreport() {
			try {
				getFacadeFactory().getArquivoFacade().excluir(getArquivoIreportSelecionado(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				getTextoPadraoVO().getListaArquivoIreport().remove(getArquivoIreportSelecionado());
				setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	
    public void selecionarMarcadorAluno() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorAlunoItens");
        String texto = adicionarMarcador(getTextoPadraoVO().getTexto(), obj.getTag());
        getTextoPadraoVO().setTexto(texto);
    }

    public void selecionarMarcadorProfessor() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorProfessorItens");
        String texto = adicionarMarcador(getTextoPadraoVO().getTexto(), obj.getTag());
        getTextoPadraoVO().setTexto(texto);
    }

    public void selecionarMarcadorUnidadeEnsino() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorUnidadeEnsinoItens");
        String texto = adicionarMarcador(getTextoPadraoVO().getTexto(), obj.getTag());
        getTextoPadraoVO().setTexto(texto);
    }

    public void selecionarMarcadorMatricula() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorMatriculaItens");
        String texto = adicionarMarcador(getTextoPadraoVO().getTexto(), obj.getTag());
        getTextoPadraoVO().setTexto(texto);
    }

//    public void selecionarMarcadorDisciplina() {
//        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorDisciplina");
//        String texto = adicionarMarcador(getTextoPadraoVO().getTexto(), obj.getTag());
//        getTextoPadraoVO().setTexto(texto);
//    }

    public void selecionarMarcadorCurso() {
        MarcadorVO obj = (MarcadorVO) context().getExternalContext().getRequestMap().get("marcadorCursoItens");
        String texto = adicionarMarcador(getTextoPadraoVO().getTexto(), obj.getTag());
        getTextoPadraoVO().setTexto(texto);
    }

    public String adicionarMarcador(String texto, String marcador) {
        int parametro = texto.lastIndexOf("</p>");
        if (parametro == -1) {
            parametro = texto.lastIndexOf("</body>");
        }
        TextoPadraoTagVO p = new TextoPadraoTagVO();
        p.setTag(marcador);
        getTextoPadraoVO().getListaTagUtilizado().add(p);
        String textoAntes = texto.substring(0, parametro);
        String textoDepois = texto.substring(parametro, texto.length());
        texto = textoAntes + " " + marcador + textoDepois;
        return texto;
    }

    public void selecionarMarcadorContaReceber() {
        String texto = adicionarMarcador(getTextoPadraoVO().getTexto(), getVariavelTelaContaReceber());
        getTextoPadraoVO().setTexto(texto);
    }

    public void selecionarMarcadorDisciplina() {
        String texto = adicionarMarcador(getTextoPadraoVO().getTexto(), getVariavelTelaDisciplina());
        getTextoPadraoVO().setTexto(texto);
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
        if (marcadorFinal.length() > 0) {
            setVariavelTelaContaReceber(marcadorFinal);
        }
    }
    
    public void setarTagVariavelDisciplina() {
        String marcadorFinal = "";
        Iterator i = listaDisciplina.iterator();
        while (i.hasNext()) {
            MarcadorVO obj = (MarcadorVO) i.next();
            if (obj.getSelecionado().booleanValue()) {
                marcadorFinal += obj.getTag() + ",";
            }
        }
        int tamanho = marcadorFinal.length();
        if (marcadorFinal.length() > 0) {
            setVariavelTelaDisciplina(marcadorFinal);
        }
    }

    public List getListaSelectItemMarcadoContaReceber() throws Exception {
        Hashtable plano = (Hashtable) Dominios.getMarcadoContaReceber();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = plano.keys();
        listaContaReceber = new ArrayList(0);
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) plano.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            listaContaReceber.add(marcador);
            marcador = new MarcadorVO();
        }
        return listaContaReceber;
    }

    public List getListaSelectItemMarcadoDisciplina() throws Exception {
        Hashtable plano = (Hashtable) Dominios.getMarcadoDisciplina();
        MarcadorVO marcador = new MarcadorVO();
        Enumeration keys = plano.keys();
        listaDisciplina = new ArrayList(0);
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) plano.get(value);
            marcador.setTag(value);
            marcador.setNome(label);
            listaDisciplina.add(marcador);
            marcador = new MarcadorVO();
        }
        return listaDisciplina;
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TextoPadraoVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
        	getFacadeFactory().getTextoPadraoFacade().excluir(textoPadraoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            setTextoPadraoVO(new TextoPadraoVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("textoPadraoForm.xhtml");
        }
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());            
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            if (resultadoConsulta.isEmpty()) {
                resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoTextoPadrao() throws Exception {
        List objs = new ArrayList(0);
        Hashtable situacaoClientes = (Hashtable) Dominios.getSituacaoTextoPadrao();
        Enumeration keys = situacaoClientes.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoClientes.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List<SelectItem> getListaSelectItemTipoTextoPadrao() throws Exception {
        List<SelectItem> opcoes = new ArrayList<SelectItem>();
        opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoTextoPadrao.class, false);
        return opcoes;
    }

    public List getListaSelectItemMarcadoAluno() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadoAluno();
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

    public List getListaSelectItemMarcadoProfessor() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadoProfessor();
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

    public List getListaSelectItemMarcadoUnidadeEnsino() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadoUnidadeEnsino();
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

    public List getListaSelectItemMarcadoMatricula() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadoMatricula();
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

    public List getListaSelectItemMarcadoCurso() throws Exception {
        List objs = new ArrayList(0);
        Hashtable cliente = (Hashtable) Dominios.getMarcadoCurso();
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

    public TextoPadraoVO getTextoPadraoVO() {
        return textoPadraoVO;
    }

    public void setTextoPadraoVO(TextoPadraoVO TextoPadraoVO) {
        this.textoPadraoVO = TextoPadraoVO;
    }

    public String getVariavelTelaContaReceber() {
        return variavelTelaContaReceber;
    }

    public void setVariavelTelaContaReceber(String variavelTelaContaReceber) {
        this.variavelTelaContaReceber = variavelTelaContaReceber;
    }

    public String getVariavelTelaDisciplina() {
        return variavelTelaDisciplina;
    }

    public void setVariavelTelaDisciplina(String variavelTelaDisciplina) {
        this.variavelTelaDisciplina = variavelTelaDisciplina;
    }

    public List getListaContaReceber() {
        return listaContaReceber;
    }

    public void setListaContaReceber(List listaContaReceber) {
        this.listaContaReceber = listaContaReceber;
    }

    public List getListaDisciplina() {
        return listaDisciplina;
    }

    public void setListaDisciplina(List listaDisciplina) {
        this.listaDisciplina = listaDisciplina;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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
	
	public List<SelectItem> listaSelectItemOrientacaoDaPagina;
	public List<SelectItem> getListaSelectItemOrientacaoDaPagina() {
		if(listaSelectItemOrientacaoDaPagina == null){
			listaSelectItemOrientacaoDaPagina = new ArrayList<SelectItem>(0);
			listaSelectItemOrientacaoDaPagina.add(new SelectItem("RE", "Retrato"));
			listaSelectItemOrientacaoDaPagina.add(new SelectItem("PA", "Paisagem"));
		}
        return listaSelectItemOrientacaoDaPagina;
	}
	
	public void downloadArquivoJasperIreport() throws Exception {
		try {
			File arquivo = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getTextoPadraoVO().getArquivoIreport().getPastaBaseArquivoEnum().getValue() + File.separator  + getTextoPadraoVO().getArquivoIreport().getNome());
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
			File arquivo = new File(UteisJSF.getCaminhoBase() + File.separator+ "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator +  "template_textoPadrao_ireport.rar");
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
	
	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = null;
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}
	
	public List<SelectItem> getSelectItemsCargoFuncionarioPrincipal() {
		if (selectItemsCargoFuncionarioPrincipal == null) {
			selectItemsCargoFuncionarioPrincipal = new ArrayList<SelectItem>();
		}
		return selectItemsCargoFuncionarioPrincipal;
	}

	public void setSelectItemsCargoFuncionarioPrincipal(List<SelectItem> selectItemsCargoFuncionarioPrincipal) {
		this.selectItemsCargoFuncionarioPrincipal = selectItemsCargoFuncionarioPrincipal;
	}

	public List<SelectItem> getSelectItemsCargoFuncionarioSecundario() {
		if (selectItemsCargoFuncionarioSecundario == null) {
			selectItemsCargoFuncionarioSecundario = new ArrayList<SelectItem>();
		}
		return selectItemsCargoFuncionarioSecundario;
	}

	public void setSelectItemsCargoFuncionarioSecundario(List<SelectItem> selectItemsCargoFuncionarioSecundario) {
		this.selectItemsCargoFuncionarioSecundario = selectItemsCargoFuncionarioSecundario;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}
	
	public void selecionarFuncionarioPrincipal() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioPrincipal");
		getTextoPadraoVO().setFuncionarioPrimarioVO(obj);
		consultarFuncionarioPrincipal();
	}

	public void selecionarFuncionarioSecundario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioSecundario");
		getTextoPadraoVO().setFuncionarioSecundarioVO(obj);
		consultarFuncionarioSecundario();
	}
	
	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(),
						"FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(
						getValorConsultaFuncionario(), 0, true, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(),
						0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(),
						"FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(),
						0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(
						getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(
						getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionarioPrincipal(){
		try {
			if(Uteis.isAtributoPreenchido(getTextoPadraoVO().getFuncionarioPrimarioVO().getMatricula())) {
			getTextoPadraoVO().setFuncionarioPrimarioVO(consultarFuncionarioPorMatricula(
					getTextoPadraoVO().getFuncionarioPrimarioVO().getMatricula()));
			setSelectItemsCargoFuncionarioPrincipal(montarComboCargoFuncionario(
					getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(
							getTextoPadraoVO().getFuncionarioPrimarioVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioSecundario(){
		try {
			if(Uteis.isAtributoPreenchido(getTextoPadraoVO().getFuncionarioSecundarioVO().getMatricula())) {
			getTextoPadraoVO().setFuncionarioSecundarioVO(consultarFuncionarioPorMatricula(
					getTextoPadraoVO().getFuncionarioSecundarioVO().getMatricula()));
			setSelectItemsCargoFuncionarioSecundario(montarComboCargoFuncionario(
					getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(
							getTextoPadraoVO().getFuncionarioSecundarioVO().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
		FuncionarioVO funcionarioVO = null;
		try {
			funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(funcionarioVO)) {
				return funcionarioVO;
			} else {
				setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new FuncionarioVO();
	}
	
	public List<SelectItem> montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
		try {
			if (cargos != null && !cargos.isEmpty()) {
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				for (FuncionarioCargoVO funcionarioCargoVO : cargos) {
					selectItems.add(new SelectItem(funcionarioCargoVO.getCargo().getCodigo(),
							funcionarioCargoVO.getCargo().getNome() + " - "
									+ funcionarioCargoVO.getUnidade().getNome()));
					removerObjetoMemoria(funcionarioCargoVO);
				}
				return selectItems;
			} else {
				setMensagemDetalhada("O Funcionário selecionado não possui cargo configurado");
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(cargos);
		}

	}
	
	public void limparDadosFuncionarioPrincipal() {
		removerObjetoMemoria(getTextoPadraoVO().getFuncionarioPrimarioVO());
		getTextoPadraoVO().setFuncionarioPrimarioVO(new FuncionarioVO());
		getTextoPadraoVO().setFuncionarioPrimarioVO(null);
		setSelectItemsCargoFuncionarioPrincipal(null);
	}

	public void limparDadosFuncionarioSecundario() {
		removerObjetoMemoria(getTextoPadraoVO().getFuncionarioSecundarioVO());
		getTextoPadraoVO().setFuncionarioSecundarioVO(new FuncionarioVO());
		getTextoPadraoVO().setCargoFuncionarioSecundarioVO(null);
		setSelectItemsCargoFuncionarioSecundario(null);
	}
	
}
