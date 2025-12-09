package controle.biblioteca;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.AssinaturaPeriodicoVO;
import negocio.comuns.biblioteca.CatalogoAreaConhecimentoVO;
import negocio.comuns.biblioteca.CatalogoCursoVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.CidadePublicacaoCatalogoVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.LogTransferenciaBibliotecaExemplarVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.EstadoHistoricoExemplar;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import negocio.comuns.utilitarias.dominios.TipoExemplar;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas assinaturaForm.jsp
 * assinaturaCons.jsp) com as funcionalidades da classe <code>Assinatura</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Assinatura
 * @see AssinaturaPeriodicoVO
 */

@Controller("AssinaturaPeriodicoControle")
@Scope("viewScope")
@Lazy
public class AssinaturaPeriodicoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 5733512421695775847L;

	private AssinaturaPeriodicoVO assinaturaPeriodicoVO;
    private String usuarioResponsavel_Erro;
    private List listaSelectItemBiblioteca;
    private ExemplarVO exemplarVO;
    private CatalogoVO catalogo;
    private List listaSelectItemEstadoExemplar;
    private String campoConsultarEditora;
    private String valorConsultarEditora;
    private List listaConsultarEditora;
    private String campoConsultarCidadePublicacao;
    private String valorConsultarCidadePublicacao;
    private List listaConsultarCidadePublicacao;
    private Integer numeroExemplaresAGerar;
    private List listaSelectItemSecao;
    private List listaSelectItemAreaDeConhecimento;
    private String campoConsultaFornecedor;
    private String valorConsultaFornecedor;
    private List<FornecedorVO> listaConsultaFornecedor;
    private List<SelectItem> listaSelectItemTipoCatalogo;
    private ArquivoMarc21VO arquivoMarc21VO;
    private AreaConhecimentoVO areaConhecimentoVO;  
    private String campoConsultaCurso;
    private String valorConsultaCurso;    
    private List<CursoVO> listaConsultaCurso;
    private Boolean fecharModalCurso;
    private CatalogoCursoVO catalogoCursoVO;    
    private CidadePublicacaoCatalogoVO novaCidadePublicacaoCatalogoVO;
    private ExemplarVO exemplarExcluir;
    private Boolean fazerDownloadMarc21;
    private ExemplarVO exemplarEdicao;
    private Boolean isEdicaoExemplar;
    private Boolean valorAlteracaoBoolean;
    private String tipoAlterarColunaExemplar;
    List<ExemplarVO> listasExemplarEditar;
    private String valorAlteracao;
    private Date valorAlteracaoData;
    private String valorConsultaAlteracao; 
    private Date valorConsultaAlteracaoData;
    /**
     * Neste lista as referencias que o exemplar que está sendo excluido está vinculado, 
     * apresentando para o usuário a quantidade de dados que será excluido com a operação de exclusao
     */
    private List<String> mensagemEntidadeReferenciadaExemplarExcluir;

    public AssinaturaPeriodicoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Assinatura</code> para edição pelo usuário
     * da aplicação.
     */
    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setCatalogo(new CatalogoVO());
        getCatalogo().setResponsavel(getUsuarioLogadoClone());
        setNovaCidadePublicacaoCatalogoVO(null);
        montarListaSelectItemBiblioteca();
        montarListaSelectItemEstadoExemplar();
        montarListaSelectItemSecao();
        montarListaSelectItemTipoCatalogo();
        montarListaSelectItemAreaDeConhecimento();
        montarTipoCatalogoAssinaturaPeriodico();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Assinatura</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("assinaturaPeriodicoItens");
        getFacadeFactory().getCatalogoFacade().carregarDados(obj, getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());        
        obj.setNovoObj(Boolean.FALSE);
        setCatalogo(obj);
        montarListaSelectItemBiblioteca();
        montarListaSelectItemEstadoExemplar();
        montarListaSelectItemTipoCatalogo();
        montarListaSelectItemAreaDeConhecimento();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoForm.xhtml");
    }

    /**
     * Método responsável inicializar objetos relacionados a classe <code>AssinaturaPeriodicoVO</code>. Esta inicialização é
     * necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
     */
    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Assinatura</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
        	getCatalogo().setAssinaturaPeriodico(true);
        	getFacadeFactory().getCatalogoFacade().inicializarDadosCatalogoEditar(getCatalogo(), getUsuarioLogado());
            if (getCatalogo().isNovoObj().booleanValue()) {
                getFacadeFactory().getCatalogoFacade().incluir(getCatalogo(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), true);
            } else {
                getFacadeFactory().getCatalogoFacade().alterar(getCatalogo(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }
    
    public void prepararImportarArquivoMarc21() {
    	try {
    		setCatalogo(new CatalogoVO());
			setArquivoMarc21VO(new ArquivoMarc21VO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}    	
    }
    
	public void uploadArquivo(FileUploadEvent upload) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(upload, getArquivoMarc21VO().getArquivoVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemID("");
			setMensagem("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			upload = null;
		}
	}
	
	public String executarProcessarArquivoMarc21() {
		try {
			getFacadeFactory().getArquivoMarc21Facade().executarProcessarArquivoMarc21(getArquivoMarc21VO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			if (!getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().isEmpty()) {
				setCatalogo(getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().get(0).getCatalogoVO());
			}
			if (!getCatalogo().getAssinaturaPeriodico()) {
				setCatalogo(new CatalogoVO());
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroProcessarArquivoMARC21Catalogo"));
			}
			getCatalogo().setResponsavel(getUsuarioLogadoClone());
	        montarListaSelectItemBiblioteca();
	        montarListaSelectItemEstadoExemplar();
	        montarListaSelectItemSecao();
	        montarListaSelectItemTipoCatalogo();
	        montarListaSelectItemAreaDeConhecimento();
			setMensagemID("msg_arquivoProcessado");
			return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoForm.xhtml");
		}
	}
	
	public void executarExportarArquivoMarc21() {
		try {
			CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("assinaturaPeriodicoItens");
			if (obj == null){
				obj = getCatalogo();
			}
			String numeroControle = getCatalogo().getNumeroControle();
			ArquivoMarc21CatalogoVO marc21CatalogoVO = new ArquivoMarc21CatalogoVO();
			setArquivoMarc21VO(new ArquivoMarc21VO());
			if (obj != null && !obj.getCodigo().equals(0)) {
				getFacadeFactory().getCatalogoFacade().carregarDados(obj, getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
				marc21CatalogoVO.setCatalogoVO(obj);
			} else {
				marc21CatalogoVO.setCatalogoVO(getCatalogo());
			}
			if (marc21CatalogoVO.getCatalogoVO().getNumeroControle().trim().equals("") && !numeroControle.trim().equals(marc21CatalogoVO.getCatalogoVO().getNumeroControle())){
				marc21CatalogoVO.getCatalogoVO().setNumeroControle(numeroControle);
			}
			getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().add(marc21CatalogoVO);
			getFacadeFactory().getArquivoMarc21Facade().executarExportarArquivoMarc21(getArquivoMarc21VO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			setFazerDownloadMarc21(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFazerDownloadMarc21(false);
		}
	}
	
	public String getDownloadArquivoMarc21() {
		try {
			if (!getArquivoMarc21VO().getArquivoVO().getNome().equals("") && getFazerDownloadMarc21() == true) {
				context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoMarc21VO().getArquivoVO().getNome());
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + getArquivoMarc21VO().getArquivoVO().getPastaBaseArquivo());
				context().getExternalContext().getSessionMap().put("deletarArquivo", true);
				return "location.href='../../DownloadSV'";
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
    
    public void persistirNovaCidadePublicacao() {
        try {
            getFacadeFactory().getCidadePublicacaoCatalogoFacade().incluir(getNovaCidadePublicacaoCatalogoVO(), getUsuarioLogado());
            getCatalogo().setCidadePublicacaoCatalogo(getNovaCidadePublicacaoCatalogoVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setNovaCidadePublicacaoCatalogoVO(null);
        }
    }

    public void alterarCidadePublicacao() {
        try {
        	CidadePublicacaoCatalogoVO obj = (CidadePublicacaoCatalogoVO) context().getExternalContext().getRequestMap().get("cidadePublicacaoItens");
        	getFacadeFactory().getCidadePublicacaoCatalogoFacade().alterar(obj, getUsuarioLogado());
        	setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            
        }
    }
    
    public void excluirCidadePublicacao() {
        try {
        	CidadePublicacaoCatalogoVO obj = (CidadePublicacaoCatalogoVO) context().getExternalContext().getRequestMap().get("cidadePublicacaoItens");
        	getFacadeFactory().getCidadePublicacaoCatalogoFacade().excluir(obj, getUsuarioLogado());
        	consultarCidadePublicacaoRichModal();
        	setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            
        }
    }

    public void montarListaSelectItemEstadoExemplar() {
        setListaSelectItemEstadoExemplar(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(EstadoHistoricoExemplar.class));
    }
    
    public void limparCampoCidadePublicacao() {
        getCatalogo().setCidadePublicacaoCatalogo(new CidadePublicacaoCatalogoVO());
    }
    
	public boolean getIsArquivoSelecionado() {
		if ((getArquivoMarc21VO().getArquivoVO().getNome() != null && !getArquivoMarc21VO().getArquivoVO().getNome().equals(""))) {
			return true;
		}
		return false;
	}
    
    public List getTipoConsultarComboCidadePublicacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }
    
    public List getTipoOrdenarPorCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("edicao", "Edição"));
        itens.add(new SelectItem("titulo", "Título"));
        itens.add(new SelectItem("crescente", "Ordem Crescente"));
        itens.add(new SelectItem("decrescente", "Ordem Decrescente"));
        return itens;
    }
    
    public List getTipoConsultarComboFornecedor() {
   	 List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
   }
    
    public void limparCampoFornecedor() {
    	getExemplarVO().setFornecedorVO(new FornecedorVO());
    	getListaConsultaFornecedor().clear();
        setValorConsultaFornecedor(null);
        setCampoConsultaFornecedor(null);
    }
   
   public void consultarFornecedor(){
   	try {
   		List objs = new ArrayList(0);
			if(getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaFornecedor(objs);
   		setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
   }
   
   public void consultarCurso() {
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultarCursoApresentarBiblioteca(getCampoConsultaCurso(), getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
   
   public void montarListaSelectItemTipoCatalogo() throws Exception {
       try {
           setListaSelectItemTipoCatalogo(UtilSelectItem.getListaSelectItem(getFacadeFactory().getTipoCatalogoFacade().consultarTipoCatalogoComboBox(false, getUsuarioLogado()), "codigo", "nome",false));
       } catch (Exception e) {
           setMensagemDetalhada("msg_erro", e.getMessage());
       }
   }
   
   public void montarListaSelectItemAreaDeConhecimento() throws Exception {
       try {
       	setListaSelectItemAreaDeConhecimento(UtilSelectItem.getListaSelectItem(getFacadeFactory().getAreaConhecimentoFacade().consultarSecaoNivelComboBox(false, getUsuarioLogado()), "codigo", "nome"));
       } catch (Exception e) {
           setMensagemDetalhada("msg_erro", e.getMessage());
       }
   }
   
   public void selecionarFornecedor(){
   	FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
   	  try {
             if (getMensagemDetalhada().equals("")) {
           	  getExemplarVO().setFornecedorVO(obj);
             }
         } finally {
             getListaConsultaFornecedor().clear();
             setValorConsultaFornecedor(null);
             setCampoConsultaFornecedor(null);
             obj = null;
         }
   }
    
    public void selecionarCidadePublicacao() throws Exception {
        CidadePublicacaoCatalogoVO obj = (CidadePublicacaoCatalogoVO) context().getExternalContext().getRequestMap().get("cidadePublicacaoItens");
        try {
            if (getMensagemDetalhada().equals("")) {
                this.getCatalogo().setCidadePublicacaoCatalogo(obj);
            }
        } finally {
            getListaConsultarCidadePublicacao().clear();
            setValorConsultarCidadePublicacao(null);
            setCampoConsultarCidadePublicacao(null);
            obj = null;
        }
    }
    
    public void montarListaSelectItemSecao() throws Exception {
        try {
            setListaSelectItemSecao(UtilSelectItem.getListaSelectItem(getFacadeFactory().getSecaoFacade().consultarSecaoNivelComboBox(false, getUsuarioLogado()), "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void gerarExemplares() {
        List<ExemplarVO> listaExemplaresASeremGerados = new ArrayList<ExemplarVO>(0);
        try {
            getFacadeFactory().getExemplarFacade().validarDadosGeracaoExemplares(getNumeroExemplaresAGerar(), getExemplarVO(), false);
            getExemplarVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getExemplarVO().getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            getExemplarVO().setSecao(getFacadeFactory().getSecaoFacade().consultarPorChavePrimaria(getExemplarVO().getSecao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//            getCatalogoVO().setClassificacaoBibliografica(getFacadeFactory().getClassificacaoBibliograficaFacade().consultarPorChavePrimaria(getCatalogoVO().getClassificacaoBibliografica().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            getFacadeFactory().getExemplarFacade().gerarExemplares(getCatalogo(), getExemplarVO(), listaExemplaresASeremGerados, getNumeroExemplaresAGerar(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
            getCatalogo().getExemplarVOs().addAll(listaExemplaresASeremGerados);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setExemplarVO(null);
            listaExemplaresASeremGerados = null;
        }
    }
    
    public void consultarCidadePublicacaoRichModal() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarCidadePublicacao().equals("codigo")) {
                if (getValorConsultarCidadePublicacao().equals("")) {
                    setValorConsultarCidadePublicacao("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarCidadePublicacao());
                objs = getFacadeFactory().getCidadePublicacaoCatalogoFacade().consultarPorCodigo(new Integer(valorInt), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarCidadePublicacao().equals("nome")) {
                objs = getFacadeFactory().getCidadePublicacaoCatalogoFacade().consultarPorNome(getValorConsultarCidadePublicacao(), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultarCidadePublicacao(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarCidadePublicacao(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP AssinaturaCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getValorConsulta().length() < 1) {
                throw new Exception(getMensagemInternalizacao("msg_entre_prmconsulta"));
            }
            if (getControleConsulta().getCampoConsulta().equals("titulo")) {
                objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTituloCatalogo(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), null, null, false, true, 0, getUsuarioLogado());
            }
            
            if (getControleConsulta().getCampoConsulta().equals("nomeEditora")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorNomeEditora(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), null, null, false, true, 0, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("classificacao")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorClassificacao(getControleConsulta().getValorConsulta(), "", null, null, false, true, 0, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tombo")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorCodigoTomboCatalogoAssinaturaPeriodico(getControleConsulta().getCampoConsulta(),getControleConsulta().getValorConsulta(), "", null, null, false, true, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoCons.xhtml");
        }
    }

    public void montarResumoPeriodico() {
		try {
	    	CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("assinaturaPeriodicoItens");
	    	getFacadeFactory().getCatalogoFacade().executarMontarResumoCatalogo(obj);	    		    	
	    	getFacadeFactory().getCatalogoFacade().executarMontarAnoVolumeNrEdicaoInicialFinal(obj);
	    	setCatalogo(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCatalogo(new CatalogoVO());
		}
    }
    
    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>AssinaturaPeriodicoVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            if (getCatalogo().getExemplarVOs().isEmpty()) {
				getFacadeFactory().getCatalogoFacade().excluir(getCatalogo(), getUsuarioLogado());
                novo();
                setMensagemID("msg_dados_excluidos");
            } else {
                throw new ConsistirException("A assinatura de periódico não pode ser excluída, pois ela já possui exemplares.");
            }
            return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoForm.xhtml");
        }
    }

    public void montarListaSelectItemBiblioteca() throws Exception {
        try {
            setListaSelectItemBiblioteca(UtilSelectItem.getListaSelectItem(getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()), "codigo",
                    "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void adicionarExemplarLista() throws Exception {
        try {
            getFacadeFactory().getExemplarFacade().validarDadosGeracaoExemplares(null, getExemplarVO(), true);
            if (!getAssinaturaPeriodicoVO().getCodigo().equals(0)) {
                getExemplarVO().setAssinaturaPeriodico(getAssinaturaPeriodicoVO());
            }
            getExemplarVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getExemplarVO().getBiblioteca().getCodigo(),
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            if (getExemplarVO().getTipoExemplar().equals("")) {
                getExemplarVO().setTipoExemplar(TipoExemplar.PERIODICO.getValor());
            }
            getFacadeFactory().getCatalogoFacade().adicionarExemplares(getCatalogo(), getExemplarVO());
            this.setExemplarVO(new ExemplarVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ContaReceberNegociacaoRecebimento</code>
     * para edição pelo usuário.
     */
    public void editarExemplar() throws Exception {
        ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
        setExemplarVO(obj);
    }

    public void selecionarExemplarExcluir(){
    	try{
    		limparMensagem();
    		setExemplarExcluir(new ExemplarVO());
    		getMensagemEntidadeReferenciadaExemplarExcluir().clear();
    		setExemplarExcluir((ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens"));
    		if(getFacadeFactory().getExemplarFacade().realizaVerificacaoExemplarEstaEmprestado(getExemplarExcluir().getCodigo())){
    			getExemplarExcluir().setSituacaoAtual("EM");
    		}
    		setMensagemEntidadeReferenciadaExemplarExcluir(getFacadeFactory().getExemplarFacade().consultarMensagemCadastrosReferenciamExemplar(getExemplarExcluir().getCodigo()));
    	}catch(Exception e){
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    public void removerExemplarLista() {
        try {
        	if(getFacadeFactory().getExemplarFacade().excluirExemplarCatalogoValidandoItemEmprestimo(getExemplarExcluir(), getUsuarioLogado())) {
        		getCatalogo().getExemplarVOs().remove(getExemplarExcluir());
        		setMensagemID("msg_dados_excluidos");
        	}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			setExemplarExcluir(new ExemplarVO());
    		getMensagemEntidadeReferenciadaExemplarExcluir().clear();
		}
    }

    public void adicionarAreaDeConhecimento() {
    	try {
    		validarAreaDeConhecimentoSelecionado();
    		AreaConhecimentoVO areaConhecimentoVO = getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(getAreaConhecimentoVO().getCodigo(), getUsuarioLogado());
    		CatalogoAreaConhecimentoVO catalogoAreaConhecimentoVO = new CatalogoAreaConhecimentoVO();
    		catalogoAreaConhecimentoVO.setAreaConhecimentoVO(areaConhecimentoVO);
    		
    		getCatalogo().getCatalogoAreaConhecimentoVOs().add(catalogoAreaConhecimentoVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
	public void validarAreaDeConhecimentoSelecionado() throws Exception {
		try {
			for (CatalogoAreaConhecimentoVO catalogoAreaConhecimentoVO : getCatalogo().getCatalogoAreaConhecimentoVOs()) {
				if (getAreaConhecimentoVO().getCodigo().equals(catalogoAreaConhecimentoVO.getAreaConhecimentoVO().getCodigo())) {
					throw new Exception("Area de Conhecimento já adicionado na lista.");
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void adicionarCatalogoCursoVOs() throws Exception {
		try {
			validarCursoSelecionado();
			for (CursoVO cursoVO : getListaConsultaCurso()) {
				if (cursoVO.getCursoSelecionado()) {
					getCatalogoCursoVO().setCursoVO(cursoVO);
					getCatalogoCursoVO().setCatalogoVO(getCatalogo());
					getCatalogo().getCatalogoCursoVOs().add(getCatalogoCursoVO());
					setCatalogoCursoVO(new CatalogoCursoVO());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			getListaConsultaCurso().clear();
		}
	}

	public void removerCatalogoCursoVOs() throws Exception {
		CatalogoCursoVO catalogoCursoVO = (CatalogoCursoVO) context().getExternalContext().getRequestMap().get("catalogoCursoItens");
		for (Iterator iterator = getCatalogo().getCatalogoCursoVOs().iterator(); iterator.hasNext();) {
			CatalogoCursoVO catalogoCurso = (CatalogoCursoVO) iterator.next();
			if (catalogoCursoVO.getCursoVO().getCodigo().equals(catalogoCurso.getCursoVO().getCodigo())) {
				iterator.remove();
			}
		}
	}
	
	public void removerAreaConhecimentoVOs() throws Exception {
		CatalogoAreaConhecimentoVO catalogoAreaConhecimentoVO = (CatalogoAreaConhecimentoVO) context().getExternalContext().getRequestMap().get("catalogoAreaConhecimentoItens");
		for (Iterator iterator = getCatalogo().getCatalogoAreaConhecimentoVOs().iterator(); iterator.hasNext();) {
			CatalogoAreaConhecimentoVO catalogoAreaConhecimento = (CatalogoAreaConhecimentoVO) iterator.next();
			if (catalogoAreaConhecimentoVO.getAreaConhecimentoVO().getCodigo().equals(catalogoAreaConhecimento.getAreaConhecimentoVO().getCodigo())) {
				iterator.remove();
			}
		}
	}
	
	public void validarCursoSelecionado() throws Exception {
		boolean cursoSelecionado = false;
		try {
			for (CursoVO cursoVO : getListaConsultaCurso()) {
				if (cursoVO.getCursoSelecionado()) {
					for (CatalogoCursoVO catalogoCursoVO : getCatalogo().getCatalogoCursoVOs()) {
						if (catalogoCursoVO.getCursoVO().getCodigo().equals(cursoVO.getCodigo())) {
							throw new Exception("Curso já adicionado na lista.");
						}
					}
					cursoSelecionado = true;
				}
			}
			if (cursoSelecionado) {
				setFecharModalCurso(Boolean.TRUE);
			} else {
				setFecharModalCurso(Boolean.FALSE);
				setMensagemDetalhada("msg_erro", "Ao menos um Curso deve ser selecionado!");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
    public List getListaSelectItemTipoEntrada() {
    	return  UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoEntradaAcervo.class,false);
    }
    
    public Boolean getExibirCamposAdicionaisCompra() {
    	if(getExemplarVO().getTipoEntrada().equals("CO")) {
    		return true;
    	}
    	return false;
    }
    
    public void selecionaExemplar(){
    	try {
    		setExemplarVO(new ExemplarVO());
    		ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
    		setExemplarVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void salvarExemplarSelecionado() {
    	try {
    		setExemplarVO(new ExemplarVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }


    public List<SelectItem> getListaSelectItemSituacaoAtual() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem(SituacaoExemplar.DISPONIVEL.getValor(), SituacaoExemplar.DISPONIVEL.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.CONSULTA.getValor(), SituacaoExemplar.CONSULTA.getDescricao()));
//		itens.add(new SelectItem(SituacaoExemplar.RESERVADO.getValor(), SituacaoExemplar.RESERVADO.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.EMPRESTADO.getValor(), SituacaoExemplar.EMPRESTADO.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.INUTILIZADO.getValor(), SituacaoExemplar.INUTILIZADO.getDescricao()));
        return itens;
    }

    public void gerarEtiquetas() throws Exception {
    	context().getExternalContext().getSessionMap().put("Periodico", getCatalogo());
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>periodicidade</code>
     */
    public List getListaSelectItemPeriodicidadeAssinatura() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable periodicidadeBibliotecas = (Hashtable) Dominios.getPeriodicidadeBiblioteca();
        Enumeration keys = periodicidadeBibliotecas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) periodicidadeBibliotecas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPessoaPorChavePrimaria() {
        try {
            UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(assinaturaPeriodicoVO.getUsuarioResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            assinaturaPeriodicoVO.setUsuarioResponsavel(usuario);
            this.setUsuarioResponsavel_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            assinaturaPeriodicoVO.getUsuarioResponsavel().setNome("");
            assinaturaPeriodicoVO.getUsuarioResponsavel().setCodigo(0);
            this.setUsuarioResponsavel_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("titulo", "Título"));
        itens.add(new SelectItem("nomeEditora", "Editora"));
        itens.add(new SelectItem("classificacao", "Classificação"));
        itens.add(new SelectItem("tombo", "Tombo"));
        return itens;
    }
    
    public List getTipoConsultarComboEditora() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }
    
    public List<SelectItem> getTipoComboAssinatura(){
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("vigente", "Vigente"));
        itens.add(new SelectItem("cancelada", "Cancelada"));
        itens.add(new SelectItem("interrompida", "Interrompida"));
        itens.add(new SelectItem("doacao", "Doação"));
        return itens;    	
    }
    
    public void consultarEditora(){
    	try {
    		List objs = new ArrayList(0);
    		
    		if (getCampoConsultarEditora().equals("codigo")) {
    			objs = getFacadeFactory().getEditoraFacade().consultarPorCodigo(Integer.parseInt(getValorConsultarEditora()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    		}
    		
    		if (getCampoConsultarEditora().equals("nome")) {
    			objs = getFacadeFactory().getEditoraFacade().consultarPorNome(getValorConsultarEditora() , false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
    		}
    		
    		setListaConsultarEditora(objs);
    		setMensagemID("msg_dados_consultados");
    		
		} catch (Exception e) {
			setListaConsultarEditora(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void selecionarEditora() {
    	EditoraVO obj = (EditoraVO) context().getExternalContext().getRequestMap().get("editoraItens");
    	getCatalogo().setEditora(obj);
    	setValorConsultarEditora(null);
        setCampoConsultarEditora(null);
    }
    
    public void limparEditora() {
    	getCatalogo().setEditora(new EditoraVO());
    	setValorConsultarEditora(null);
        setCampoConsultarEditora(null);
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("assinaturaPeriodicoCons.xhtml");
    }

	public void preencherTodosCurso() throws Exception {
		for (CursoVO cursoVO : getListaConsultaCurso()) {
			cursoVO.setCursoSelecionado(Boolean.TRUE);
		}
	}

	public void desmarcarTodosCurso() throws Exception {
		for (CursoVO cursoVO : getListaConsultaCurso()) {
			cursoVO.setCursoSelecionado(Boolean.FALSE);
		}
	}

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        assinaturaPeriodicoVO = null;
        usuarioResponsavel_Erro = null;
    }

    public String getUsuarioResponsavel_Erro() {
    	if (usuarioResponsavel_Erro == null) {
    		usuarioResponsavel_Erro = "";
    	}
        return usuarioResponsavel_Erro;
    }

    public void setUsuarioResponsavel_Erro(String usuarioResponsavel_Erro) {
        this.usuarioResponsavel_Erro = usuarioResponsavel_Erro;
    }

    public AssinaturaPeriodicoVO getAssinaturaPeriodicoVO() {
        return assinaturaPeriodicoVO;
    }

    public void setAssinaturaPeriodicoVO(AssinaturaPeriodicoVO assinaturaPeriodicoVO) {
        this.assinaturaPeriodicoVO = assinaturaPeriodicoVO;
    }

    public List getListaSelectItemBiblioteca() {
        if (listaSelectItemBiblioteca == null) {
            listaSelectItemBiblioteca = new ArrayList(0);
        }
        return listaSelectItemBiblioteca;
    }

    public void setListaSelectItemBiblioteca(List listaSelectItemBiblioteca) {
        this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
    }

    public void setExemplarVO(ExemplarVO exemplarVO) {
        this.exemplarVO = exemplarVO;
    }

    public ExemplarVO getExemplarVO() {
        if (exemplarVO == null) {
            exemplarVO = new ExemplarVO();
        }
        return exemplarVO;
    }

    public List getListaSelectItemEstadoExemplar() {
        if (listaSelectItemEstadoExemplar == null) {
            listaSelectItemEstadoExemplar = new ArrayList(0);
        }
        return listaSelectItemEstadoExemplar;
    }

    public void setListaSelectItemEstadoExemplar(List listaSelectItemEstadoExemplar) {
        this.listaSelectItemEstadoExemplar = listaSelectItemEstadoExemplar;
    }

	public String getCampoConsultarEditora() {
		if(campoConsultarEditora == null){
			campoConsultarEditora = "";
		}
		return campoConsultarEditora;
	}

	public void setCampoConsultarEditora(String campoConsultarEditora) {
		this.campoConsultarEditora = campoConsultarEditora;
	}

	public String getValorConsultarEditora() {
		if(valorConsultarEditora == null){
			valorConsultarEditora  = "";
		}
		return valorConsultarEditora;
	}

	public void setValorConsultarEditora(String valorConsultarEditora) {
		this.valorConsultarEditora = valorConsultarEditora;
	}

	public List getListaConsultarEditora() {
		if(listaConsultarEditora==null){
			listaConsultarEditora = new ArrayList(0);
		}
		return listaConsultarEditora;
	}

	public void setListaConsultarEditora(List listaConsultarEditora) {
		this.listaConsultarEditora = listaConsultarEditora;
	}

	public CatalogoVO getCatalogo() {
		if(catalogo == null){
			catalogo = new CatalogoVO();
		}
		return catalogo;
	}

	public void setCatalogo(CatalogoVO catalogo) {
		this.catalogo = catalogo;
	}

	public String getCampoConsultarCidadePublicacao() {
		if(campoConsultarCidadePublicacao == null){
			campoConsultarCidadePublicacao = "";
		}
		return campoConsultarCidadePublicacao;
	}

	public void setCampoConsultarCidadePublicacao(String campoConsultarCidadePublicacao) {
		this.campoConsultarCidadePublicacao = campoConsultarCidadePublicacao;
	}

	public String getValorConsultarCidadePublicacao() {
		if(valorConsultarCidadePublicacao == null){
			valorConsultarCidadePublicacao = "";
		}
		return valorConsultarCidadePublicacao;
	}

	public void setValorConsultarCidadePublicacao(String valorConsultarCidadePublicacao) {
		this.valorConsultarCidadePublicacao = valorConsultarCidadePublicacao;
	}

	public List getListaConsultarCidadePublicacao() {
		if(listaConsultarCidadePublicacao == null){
			listaConsultarCidadePublicacao = new ArrayList(0);
		}
		return listaConsultarCidadePublicacao;
	}

	public void setListaConsultarCidadePublicacao(List listaConsultarCidadePublicacao) {
		this.listaConsultarCidadePublicacao = listaConsultarCidadePublicacao;
	}

	public Integer getNumeroExemplaresAGerar() {
		if(numeroExemplaresAGerar== null){
			numeroExemplaresAGerar = 0;
		}
		return numeroExemplaresAGerar;
	}

	public void setNumeroExemplaresAGerar(Integer numeroExemplaresAGerar) {
		this.numeroExemplaresAGerar = numeroExemplaresAGerar;
	}

	public List getListaSelectItemSecao() {
		if(listaSelectItemSecao == null){
			listaSelectItemSecao = new ArrayList(0);
		}
		return listaSelectItemSecao;
	}

	public void setListaSelectItemSecao(List listaSelectItemSecao) {
		this.listaSelectItemSecao = listaSelectItemSecao;
	}

	public String getCampoConsultaFornecedor() {
		if(campoConsultaFornecedor == null){
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		if(valorConsultaFornecedor == null){
			valorConsultaFornecedor = "";
		}
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}

	public List<FornecedorVO> getListaConsultaFornecedor() {
		if(listaConsultaFornecedor == null){
			listaConsultaFornecedor = new ArrayList<FornecedorVO>(0);
		}
		return listaConsultaFornecedor;
	}

	public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public List<SelectItem> getListaSelectItemTipoCatalogo() {
		if (listaSelectItemTipoCatalogo == null) {
			listaSelectItemTipoCatalogo = new ArrayList(0);
		}
		return listaSelectItemTipoCatalogo;
	}

	public void setListaSelectItemTipoCatalogo(List listaSelectItemTipoCatalogo) {
		this.listaSelectItemTipoCatalogo = listaSelectItemTipoCatalogo;
	}

	public ArquivoMarc21VO getArquivoMarc21VO() {
		if (arquivoMarc21VO == null) {
			arquivoMarc21VO = new ArquivoMarc21VO();
		}
		return arquivoMarc21VO;
	}

	public void setArquivoMarc21VO(ArquivoMarc21VO arquivoMarc21VO) {
		this.arquivoMarc21VO = arquivoMarc21VO;
	}
	
	public AreaConhecimentoVO getAreaConhecimentoVO() {
		if(areaConhecimentoVO == null) {
			areaConhecimentoVO = new AreaConhecimentoVO();
		}
		return areaConhecimentoVO;
	}

	public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
		this.areaConhecimentoVO = areaConhecimentoVO;
	}


	public Boolean getFecharModalCurso() {
		if (fecharModalCurso == null) {
			fecharModalCurso = Boolean.FALSE;
		}
		return fecharModalCurso;
	}

	public String getIsFecharModalCurso() {
		if (getFecharModalCurso()) {
			return "RichFaces.$('panelCurso').hide()";
		}
		return "";
	}

	public void setFecharModalCurso(Boolean fecharModalCurso) {
		this.fecharModalCurso = fecharModalCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}
	
	public CatalogoCursoVO getCatalogoCursoVO() {
		if(catalogoCursoVO == null) {
			catalogoCursoVO = new CatalogoCursoVO();
		}
		return catalogoCursoVO;
	}

	public void setCatalogoCursoVO(CatalogoCursoVO catalogoCursoVO) {
		this.catalogoCursoVO = catalogoCursoVO;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}
	
	public List getListaSelectItemAreaDeConhecimento() {
		return listaSelectItemAreaDeConhecimento;
	}

	public void setListaSelectItemAreaDeConhecimento(List listaSelectItemAreaDeConhecimento) {
		this.listaSelectItemAreaDeConhecimento = listaSelectItemAreaDeConhecimento;
	}

    public void setNovaCidadePublicacaoCatalogoVO(CidadePublicacaoCatalogoVO novaCidadePublicacaoCatalogoVO) {
        this.novaCidadePublicacaoCatalogoVO = novaCidadePublicacaoCatalogoVO;
    }

    public CidadePublicacaoCatalogoVO getNovaCidadePublicacaoCatalogoVO() {
        if (novaCidadePublicacaoCatalogoVO == null) {
            novaCidadePublicacaoCatalogoVO = new CidadePublicacaoCatalogoVO();
        }
        return novaCidadePublicacaoCatalogoVO;
    }
    

	/**
	 * @return the exemplarExcluir
	 */
	public ExemplarVO getExemplarExcluir() {
		if (exemplarExcluir == null) {
			exemplarExcluir = new ExemplarVO();
		}
		return exemplarExcluir;
	}

	/**
	 * @param exemplarExcluir the exemplarExcluir to set
	 */
	public void setExemplarExcluir(ExemplarVO exemplarExcluir) {
		this.exemplarExcluir = exemplarExcluir;
	}

	/**
	 * @return the mensagemEntidadeReferenciadaExemplarExcluir
	 */
	public List<String> getMensagemEntidadeReferenciadaExemplarExcluir() {
		if (mensagemEntidadeReferenciadaExemplarExcluir == null) {
			mensagemEntidadeReferenciadaExemplarExcluir = new ArrayList<String>(0);
		}
		return mensagemEntidadeReferenciadaExemplarExcluir;
	}

	/**
	 * @param mensagemEntidadeReferenciadaExemplarExcluir the mensagemEntidadeReferenciadaExemplarExcluir to set
	 */
	public void setMensagemEntidadeReferenciadaExemplarExcluir(List<String> mensagemEntidadeReferenciadaExemplarExcluir) {
		this.mensagemEntidadeReferenciadaExemplarExcluir = mensagemEntidadeReferenciadaExemplarExcluir;
	}

	public Boolean getFazerDownloadMarc21() {
		if (fazerDownloadMarc21 == null) {
			fazerDownloadMarc21 = false;
		}
		return fazerDownloadMarc21;
	}

	public void setFazerDownloadMarc21(Boolean fazerDownloadMarc21) {
		this.fazerDownloadMarc21 = fazerDownloadMarc21;
	}

	public void executarTranferirExemplarBiblioteca(){
		try {
			validarDadosTransferenciaBiblioteca(getExemplarEdicao());
			LogTransferenciaBibliotecaExemplarVO log = new LogTransferenciaBibliotecaExemplarVO();
			log.setExemplar(getExemplarEdicao());
			log.setBibliotecaDestino(getExemplarEdicao().getBibliotecaDestino());
			log.setBibliotecaOrigem(getExemplarEdicao().getBiblioteca());
			log.setResponsavel(getUsuarioLogadoClone());
			getFacadeFactory().getExemplarFacade().executarTransferenciaExemplarBiblioteca(getExemplarEdicao(), getUsuarioLogado());
			getFacadeFactory().getLogTransferenciaBibliotecaExemplarFacade().incluir(log, getUsuarioLogado());
			getFacadeFactory().getCatalogoFacade().carregarDados(catalogo, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			setMensagemID("msg_TransferenciaExemplar_sucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarDadosTransferenciaBiblioteca(ExemplarVO obj) throws Exception{	
		if(!Uteis.isAtributoPreenchido(obj.getBibliotecaDestino())){
			throw new Exception("Biblioteca de Destino deve ser Informada.");
		}
	} 
	
	 public void selecionaExemplarEdicao(){
	    	try {
	    		setExemplarEdicao(new ExemplarVO());
	    		ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
	    		setExemplarEdicao(obj);
	    		setIsEdicaoExemplar(Boolean.TRUE);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
	}

	public ExemplarVO getExemplarEdicao() {
		return exemplarEdicao;
	}

	public void setExemplarEdicao(ExemplarVO exemplarEdicao) {
		this.exemplarEdicao = exemplarEdicao;
	}
	
	public void selecionarFornecedorEdicao() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		try {
			if (getMensagemDetalhada().equals("")) {
				getExemplarEdicao().setFornecedorVO(obj);
			}
		} finally {
			getListaConsultaFornecedor().clear();
			setValorConsultaFornecedor(null);
			setCampoConsultaFornecedor(null);
			obj = null;
			setIsEdicaoExemplar(Boolean.FALSE);
		}
	}
	
	public void limparCampoFornecedorEdicao() {
    	getExemplarEdicao().setFornecedorVO(new FornecedorVO());
    	getListaConsultaFornecedor().clear();
        setValorConsultaFornecedor(null);
        setCampoConsultaFornecedor(null);
    }
	
	public Boolean getIsEdicaoExemplar() {
		if (isEdicaoExemplar == null) {
			isEdicaoExemplar = Boolean.FALSE;
		}
		return isEdicaoExemplar;
	}
	
	public void setIsEdicaoExemplar(Boolean edicao) {
		this.isEdicaoExemplar = edicao;
	}
	
	public void setFalseEdicao() {
		setIsEdicaoExemplar(Boolean.FALSE);
	}

	
	private void montarTipoCatalogoAssinaturaPeriodico() {
		if (getListaSelectItemTipoCatalogo().isEmpty()) {
			return;
		}
		SelectItem selectItem = getListaSelectItemTipoCatalogo().stream().filter(s -> {
			String descricao = Uteis.removeCaractersEspeciais(s.getLabel());
			return descricao.toUpperCase().contains("periodico".toUpperCase());
		}).findFirst().orElse(getListaSelectItemTipoCatalogo().stream().findFirst().get());
		getCatalogo().getTipoCatalogo().setCodigo((Integer) selectItem.getValue());
	}

	public List<SelectItem> getListaSelectItemNivelBibliografico() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("MO", "Monográfico"));
        itens.add(new SelectItem("SE", "Série"));
        return itens;
    }
	
	public Boolean getValorAlteracaoBoolean() {
		if (valorAlteracaoBoolean == null) {
			valorAlteracaoBoolean = false;	
		}
		return valorAlteracaoBoolean;
	}

	public void setValorAlteracaoBoolean(Boolean valorAlteracaoBoolean) {
		this.valorAlteracaoBoolean = valorAlteracaoBoolean;
	}

	public String getTipoAlterarColunaExemplar() {
		if (tipoAlterarColunaExemplar == null) {
			tipoAlterarColunaExemplar = "";
		}
		return tipoAlterarColunaExemplar;
	}

	public void setTipoAlterarColunaExemplar(String tipoAlterarColunaExemplar) {
		this.tipoAlterarColunaExemplar = tipoAlterarColunaExemplar;
	}

	public String getValorAlteracao() {
		if (valorAlteracao == null) {
			valorAlteracao = "";
		}
		return valorAlteracao;
	}

	public void setValorAlteracao(String valorAlteracao) {
		this.valorAlteracao = valorAlteracao;
	}

	public Date getValorAlteracaoData() {
		return valorAlteracaoData;
	}

	public void setValorAlteracaoData(Date valorAlteracaoData) {
		this.valorAlteracaoData = valorAlteracaoData;
	}

	public String getValorConsultaAlteracao() {
		if (valorConsultaAlteracao == null) {
			valorConsultaAlteracao = "";
		}
		return valorConsultaAlteracao;
	}

	public void setValorConsultaAlteracao(String valorConsultaAlteracao) {
		this.valorConsultaAlteracao = valorConsultaAlteracao;
	}

	public Date getValorConsultaAlteracaoData() {
		return valorConsultaAlteracaoData;
	}

	public void setValorConsultaAlteracaoData(Date valorConsultaAlteracaoData) {
		this.valorConsultaAlteracaoData = valorConsultaAlteracaoData;
	}
	

	public List<ExemplarVO> getListasExemplarEditar() {
		if (listasExemplarEditar == null) {
			listasExemplarEditar = new ArrayList<ExemplarVO>(0);  
		}
		return listasExemplarEditar;
	}

	public void setListasExemplarEditar(List<ExemplarVO> listasExemplarEditar) {
		this.listasExemplarEditar = listasExemplarEditar;
	}

	public List getListaSelectItemAlterarexemplar() {
        List itens = new ArrayList(0);
        
        itens.add(new SelectItem("abreviacaotitulo", "Abreviação Titulo"));
        itens.add(new SelectItem("anovolume", "Ano/Volume"));
        itens.add(new SelectItem("dataaquisicao", "Data Aquisição"));
        itens.add(new SelectItem("datacompra", "Data Compra"));
        itens.add(new SelectItem("datapublicacao", "Data Publicação"));
        itens.add(new SelectItem("desconsiderarreserva", "Desconsiderar Reserva"));
        itens.add(new SelectItem("emprestarsomentefinaldesemana", "Emprestimo Especial"));
        itens.add(new SelectItem("fasciculo", "Fascículo"));
        itens.add(new SelectItem("link", "Link"));
        itens.add(new SelectItem("local", "Local"));
        itens.add(new SelectItem("mes", "Mês"));
        itens.add(new SelectItem("numeroedicao", "Número/Edição"));
        itens.add(new SelectItem("nredicaoespecial", "N° Edição Especial"));
        itens.add(new SelectItem("paraconsulta", "Exemplar Consulta"));
        itens.add(new SelectItem("secao", "Seção"));  
        itens.add(new SelectItem("subtitulo", "Subtítuto"));
        itens.add(new SelectItem("tipoentrada", "Forma Entrada"));
        itens.add(new SelectItem("tituloexemplar", "Título"));        
        
        return itens;
    }
	
	public List getListaSelectItemTipoEntradaAlterarexemplar() {
        List itens = new ArrayList(0);
        
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("DO", "Doação"));
        itens.add(new SelectItem("TR", "Trasferência"));
        itens.add(new SelectItem("CO", "Compra"));
        itens.add(new SelectItem("RE", "Restauração"));
        itens.add(new SelectItem("RP", "Recuperado"));
        itens.add(new SelectItem("PE", "Permuta"));
        itens.add(new SelectItem("AS", "Assinatura"));
        itens.add(new SelectItem("PP", "Publicação Própria"));
        itens.add(new SelectItem("SU", "Substituição"));
        itens.add(new SelectItem("ES", "Entrada Simples"));
        itens.add(new SelectItem("OU", "Outros"));
        return itens;
    }
	
	public void buscarExemplaresEdicao() throws Exception {
		setValorConsultaAlteracao(null);
		setValorConsultaAlteracaoData(null);
		setListasExemplarEditar(getFacadeFactory().getExemplarFacade().buscarExemplaresEdicao(getCatalogo().getCodigo()  , getTipoAlterarColunaExemplar() , getValorConsultaAlteracao() , getValorConsultaAlteracaoData()));
		desmarcarTodosListaExemplares();
		
	}
	
	public boolean isCampoData() {
	    return getTipoAlterarColunaExemplar().equals("dataaquisicao") || getTipoAlterarColunaExemplar().equals("datacompra") || getTipoAlterarColunaExemplar().equals("datapublicacao");
	}
	
	public boolean isCampoBoolean() {
	    return getTipoAlterarColunaExemplar().equals("paraconsulta") || getTipoAlterarColunaExemplar().equals("desconsiderarreserva") || getTipoAlterarColunaExemplar().equals("emprestarsomentefinaldesemana");
	}
	
	public boolean isCampoAno() {
	    return getTipoAlterarColunaExemplar().equals("anopublicacao") || getTipoAlterarColunaExemplar().equals("numeroedicao") ;
	}
	
	public boolean isCampoSecao() {
	    return getTipoAlterarColunaExemplar().equals("secao");
	}
	
	public boolean isCampoTipoEntrada() {
	    return getTipoAlterarColunaExemplar().equals("tipoentrada");
	}
	
	public boolean isCampoString() {
	    return getTipoAlterarColunaExemplar().equals("tituloexemplar") || getTipoAlterarColunaExemplar().equals("subtitulo") ||
	    		getTipoAlterarColunaExemplar().equals("edicao") || getTipoAlterarColunaExemplar().equals("volume") ||
	    		getTipoAlterarColunaExemplar().equals("local") || getTipoAlterarColunaExemplar().equals("link") ||
	    		getTipoAlterarColunaExemplar().equals("fasciculo") || getTipoAlterarColunaExemplar().equals("abreviacaotitulo") || 
	    		getTipoAlterarColunaExemplar().equals("mes") || getTipoAlterarColunaExemplar().equals("nredicaoespecial") ;
	}
	
		
	public void marcarTodosListaExemplares() throws Exception {
		getFacadeFactory().getExemplarFacade().marcarTodosListaExemplares(getListasExemplarEditar());	
	}

	public void desmarcarTodosListaExemplares() throws Exception {
		getFacadeFactory().getExemplarFacade().desmarcarTodosListaExemplares(getListasExemplarEditar());		
	}
	
	public String editarDadosExemplares() {
        try {            
        	getFacadeFactory().getExemplarFacade().editarDadosExemplares(getListasExemplarEditar() , getUsuarioLogado() , getValorAlteracao() ,getValorAlteracaoBoolean(),getValorAlteracaoData(),  getTipoAlterarColunaExemplar() , getCatalogo().getAssinaturaPeriodico());
        	getFacadeFactory().getCatalogoFacade().carregarDados(getCatalogo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
        	setMensagemID("msg_AlteracaoExemplar_sucesso");
        	setListasExemplarEditar(null);
        	setValorConsultaAlteracao(null);
    		setValorConsultaAlteracaoData(null);
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }
	public boolean getApresentarResultadoConsultaEdicao() {
		if (this.getListasExemplarEditar() == null || this.getListasExemplarEditar().size() == 0) {
			return false;
		}
		return true;
	} 
	
	public void buscarExemplares() throws Exception {
		setListasExemplarEditar(getFacadeFactory().getExemplarFacade().buscarExemplaresEdicao(getCatalogo().getCodigo() , getTipoAlterarColunaExemplar() , getValorConsultaAlteracao() , getValorConsultaAlteracaoData()));
		desmarcarTodosListaExemplares();
	}
	public List getListaSelectItemTipoBoolean() {
        List itens = new ArrayList(0);
        
        itens.add(new SelectItem("", "Ambos"));
        itens.add(new SelectItem("true", "Marcado"));
        itens.add(new SelectItem("false", "Desmarcado"));        
        return itens;
    }
	
	public void realizarValidacaoDisponibilizarExemplarConsulta() {
		try {
			ExemplarVO exemplarConsulta = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
			getFacadeFactory().getExemplarFacade().realizarValidacaoDisponibilizarExemplarConsulta(exemplarConsulta);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
}
