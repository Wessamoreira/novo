package controle.biblioteca;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.AutorVariacaoNomeVO;
import negocio.comuns.biblioteca.CatalogoAreaConhecimentoVO;
import negocio.comuns.biblioteca.CatalogoAutorVO;
import negocio.comuns.biblioteca.CatalogoCursoVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.CidadePublicacaoCatalogoVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.LinguaPublicacaoCatalogoVO;
import negocio.comuns.biblioteca.LogTransferenciaBibliotecaExemplarVO;
import negocio.comuns.biblioteca.enumeradores.TipoAutoriaEnum;
import negocio.comuns.biblioteca.enumeradores.TipoMidiaEnum;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import negocio.facade.jdbc.biblioteca.Catalogo;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas catalogoForm.jsp
 * catalogoCons.jsp) com as funcionalidades da classe <code>Catalogo</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Catalogo
 * @see CatalogoVO
 */

@Controller("CatalogoControle")
@Scope("viewScope")
@Lazy
public class CatalogoControle extends SuperControle implements Serializable {

    private CatalogoVO catalogoVO;
    private String campoConsultarEditora;
    private String valorConsultarEditora;
    private List listaConsultarEditora;
    private String campoConsultarLinguaPublicacao;
    private String valorConsultarLinguaPublicacao;
    private List listaConsultarLinguaPublicacao;
    private String campoConsultarCidadePublicacao;
    private String valorConsultarCidadePublicacao;
    private List listaConsultarCidadePublicacao;
    private String campoConsultarAutor;
    private String valorConsultarAutor;
    private List listaConsultarAutor;
    private CatalogoAutorVO catalogoAutorVO;
    private String campoConsultarConfiguracaoBiblioteca;
    private String valorConsultarConfiguracaoBiblioteca;
    private List listaConsultarConfiguracaoBiblioteca;
//    private List listaSelectItemClassificacaoBibliografica; 
    private List listaSelectItemTipoCatalogo;
    private List listaSelectItemBiblioteca;
    private List listaSelectItemSecao;
    private List listaSelectItemAreaDeConhecimento;
    private Integer numeroExemplaresAGerar;
    private String nivelBibliografico;
    private String nomeAutor;
    private ExemplarVO exemplarVO;
    private Boolean abrirModalAutor;
    private Boolean abrirModalEditora;
    private Boolean abrirModalLinguaPublicacao;
    private Boolean abrirModalCidadePublicacao;
    private LinguaPublicacaoCatalogoVO novaLinguaPublicacaoCatalogoVO;
    private CidadePublicacaoCatalogoVO novaCidadePublicacaoCatalogoVO;
    private AutorVO novoAutorVO;
    private AutorVariacaoNomeVO novoAutorVariacaoNomeVO;
    private Integer posicao;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List<CursoVO> listaConsultaCurso;
    private Boolean fecharModalCurso;
    private CatalogoCursoVO catalogoCursoVO;
    private AreaConhecimentoVO areaConhecimentoVO;
    
    private String campoConsultaFornecedor;
    private String valorConsultaFornecedor;
    private List<FornecedorVO> listaConsultaFornecedor;
    private EmprestimoVO emprestimoVO;
	private ArquivoMarc21VO arquivoMarc21VO ;
    private ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO;
    private ExemplarVO exemplarExcluir;
    private Boolean fazerDownloadMarc21;
    private ExemplarVO exemplarEdicao;
    private Boolean tipoUploadCatalogoResumo;
    private Boolean tipoUploadCatalogoContraCapa;
    private Boolean tipoUploadCatalogoCapa;
    private Boolean tipoUploadCatalogoSumario;
    private Boolean tipoUploadCatalogoDownload;
    private String tipoUploadApresentar;
    private String erroUpload;
    private String MsgErroUpload;
    private ArquivoVO arquivoVO;
    private String caminhoImagemCapa;
    private String caminhoImagemContraCapa;
    private String caminhoSumario;
    private String caminhoResumo;
    private String caminhoDownload;
    private List<ArquivoVO> listaArquivosBibliotecaExterna;
    private Boolean valorAlteracaoBoolean;
    private String tipoAlterarColunaExemplar;
    List<ExemplarVO> listasExemplarEditar;
    private String valorAlteracao;
    private Date valorAlteracaoData;
    private String valorConsultaAlteracao; 
    private Date valorConsultaAlteracaoData;
    private String caminhoarquivoAnexado;
    private Boolean exibirBotaoConfirmaArquivo;
    private Boolean validarPossuiIntegracaoEbsco;
    private String modalpanelExportarMarc21XML;
    
   
 
   
    
    /**
     * Neste lista as referencias que o exemplar que está sendo excluido está vinculado, 
     * apresentando para o usuário a quantidade de dados que será excluido com a operação de exclusao
     */
    private List<String> mensagemEntidadeReferenciadaExemplarExcluir;
    
    public CatalogoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Catalogo</code> para edição pelo usuário da
     * aplicação.
     *
     * @throws Exception
     */
    public String novo() {
        removerObjetoMemoria(this);
        try {
            setCatalogoVO(null);
            // getCatalogoVO().setAutorVOs(null);
            setNovoAutorVariacaoNomeVO(null);
            setNovoAutorVO(null);
            setNovaCidadePublicacaoCatalogoVO(null);
            setNovaLinguaPublicacaoCatalogoVO(null);
            setListaArquivosBibliotecaExterna(null);
            inicializarListasSelectItemTodosComboBox();
            getFacadeFactory().getCatalogoFacade().inicializarDadosCatalogoNovo(getCatalogoVO(), getUsuarioLogado());
            setCaminhoarquivoAnexado(null);
            setMensagemID("msg_entre_dados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Catalogo</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
            //obj = getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getFacadeFactory().getCatalogoFacade().carregarDados(obj, getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
            Ordenacao.ordenarLista(obj.getExemplarVOs(), "codigoHashComNumeroExemplar");
            obj.setNovoObj(Boolean.FALSE);
            setCatalogoVO(obj);
            inicializarListasSelectItemTodosComboBox();
            buscarArquivos();
            setCaminhoarquivoAnexado(null);
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoForm.xhtml");
        }
    }
    
    public void prepararImportarArquivoMarc21() {
    	try {
    		setCatalogoVO(new CatalogoVO());
			setArquivoMarc21VO(new ArquivoMarc21VO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}    	
    }

    public void persistirNovaLinguaPublicacao() {
        try {
        	if(getNovaLinguaPublicacaoCatalogoVO().isNovoObj()) {
            getFacadeFactory().getLinguaPublicacaoCatalogoFacade().incluir(getNovaLinguaPublicacaoCatalogoVO(), getUsuarioLogado());
            getCatalogoVO().setLinguaPublicacaoCatalogo(getNovaLinguaPublicacaoCatalogoVO());
        	}else {
        		getFacadeFactory().getLinguaPublicacaoCatalogoFacade().alterar(getNovaLinguaPublicacaoCatalogoVO(), getUsuarioLogado());
        	}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setNovaLinguaPublicacaoCatalogoVO(null);
        }
    }

    public void alterarLinguaPublicacao() {
        try {
        	LinguaPublicacaoCatalogoVO obj = (LinguaPublicacaoCatalogoVO) context().getExternalContext().getRequestMap().get("linguaPublicacaoItens");
        	getFacadeFactory().getLinguaPublicacaoCatalogoFacade().alterar(obj, getUsuarioLogado());
        	setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            
        }
    }
    
    public void excluirLinguaPublicacao() {
        try {
        	LinguaPublicacaoCatalogoVO obj = (LinguaPublicacaoCatalogoVO) context().getExternalContext().getRequestMap().get("linguaPublicacaoItens");
        	getFacadeFactory().getLinguaPublicacaoCatalogoFacade().excluir(obj, getUsuarioLogado());
        	consultarLinguaPublicacao();
        	setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            
        }
    }


    public void persistirNovaCidadePublicacao() {
        try {
            getFacadeFactory().getCidadePublicacaoCatalogoFacade().incluir(getNovaCidadePublicacaoCatalogoVO(), getUsuarioLogado());
            getCatalogoVO().setCidadePublicacaoCatalogo(getNovaCidadePublicacaoCatalogoVO());
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

    public void persistirNovoAutor() {
        try {
            getFacadeFactory().getAutorFacade().incluir(getNovoAutorVO(), false, getUsuarioLogado());
            getCatalogoVO().getAutorVOs().add(getNovoAutorVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setNovoAutorVariacaoNomeVO(null);
            setNovoAutorVO(null);
        }
    }
    
	public void validarAreaDeConhecimentoSelecionado() throws Exception {
		try {
			for (CatalogoAreaConhecimentoVO catalogoAreaConhecimentoVO : getCatalogoVO().getCatalogoAreaConhecimentoVOs()) {
				if (getAreaConhecimentoVO().getCodigo().equals(catalogoAreaConhecimentoVO.getAreaConhecimentoVO().getCodigo())) {
					throw new Exception("Area de Conhecimento já adicionado na lista.");
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
    
    public void adicionarAreaDeConhecimento() {
    	try {
    		validarAreaDeConhecimentoSelecionado();
    		AreaConhecimentoVO areaConhecimentoVO = getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(getAreaConhecimentoVO().getCodigo(), getUsuarioLogado());
    		CatalogoAreaConhecimentoVO catalogoAreaConhecimentoVO = new CatalogoAreaConhecimentoVO();
    		catalogoAreaConhecimentoVO.setAreaConhecimentoVO(areaConhecimentoVO);
    		
    		getCatalogoVO().getCatalogoAreaConhecimentoVOs().add(catalogoAreaConhecimentoVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public void gerarEtiquetas() throws Exception {
//        EtiquetaLivroRelControle EtiquetaLivroRelControle = new EtiquetaLivroRelControle();
//        EtiquetaLivroRelControle.setCatalogo(getCatalogoVO());
        context().getExternalContext().getSessionMap().put("Catalogo", getCatalogoVO());
    }

    /**
     * Método que pega o número de exemplares a ser gerado informado na tela, e monta os objetos desses exemplares,
     * colocando em uma lista. Ao gerar os exemplares, deve ser levado em conta os seguintes aspectos: - A biblioteca
     * selecionada na combobox será espelhada no exemplar, assim como a seção. - O código de barras será populado
     * automaticamente com base em uma sequence do banco de dados. - O local será preenchido também automaticamente com
     * a String de classificação Bibliográfica e um ponto. - Ambos os campos gerados automaticamente (local e código de
     * barras) poderão ser alterados pelo usuário.
     *
     * @author Murillo Parreira
     */
    public void gerarExemplares() {
        List<ExemplarVO> listaExemplaresASeremGerados = new ArrayList<ExemplarVO>(0);
        try {
            getFacadeFactory().getExemplarFacade().validarDadosGeracaoExemplares(getNumeroExemplaresAGerar(), getExemplarVO(), false);
            getExemplarVO().setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getExemplarVO().getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            if(!getExemplarVO().getSecao().getCodigo().equals(0)){
            	getExemplarVO().setSecao(getFacadeFactory().getSecaoFacade().consultarPorChavePrimaria(getExemplarVO().getSecao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            }                        
                       
                        
                        
//            getCatalogoVO().setClassificacaoBibliografica(getFacadeFactory().getClassificacaoBibliograficaFacade().consultarPorChavePrimaria(getCatalogoVO().getClassificacaoBibliografica().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            getFacadeFactory().getExemplarFacade().gerarExemplares(getCatalogoVO(), getExemplarVO(), listaExemplaresASeremGerados, getNumeroExemplaresAGerar(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
            
            getCatalogoVO().getExemplarVOs().addAll(listaExemplaresASeremGerados);
            Ordenacao.ordenarLista(getCatalogoVO().getExemplarVOs(), "codigoHashComNumeroExemplar");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setExemplarVO(null);
            listaExemplaresASeremGerados = null;
        }
    }
    
    public void selecionarExemplarExcluir(){
    	try{
    		limparMensagem();
    		setExemplarExcluir(new ExemplarVO());
    		getMensagemEntidadeReferenciadaExemplarExcluir().clear();
    		setExemplarExcluir((ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens"));
    		if(getFacadeFactory().getExemplarFacade().realizaVerificacaoExemplarEstaEmprestado(getExemplarExcluir().getCodigo())){
    			getExemplarExcluir().setSituacaoAtual("EM");
    		}else if(getExemplarExcluir().getSituacaoAtual().equals("EM")){
    			getExemplarExcluir().setSituacaoAtual("DI");
    		}
    		setMensagemEntidadeReferenciadaExemplarExcluir(getFacadeFactory().getExemplarFacade().consultarMensagemCadastrosReferenciamExemplar(getExemplarExcluir().getCodigo()));
    	}catch(Exception e){
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    public void removerExemplarLista() {
        try {
        	if(getFacadeFactory().getExemplarFacade().excluirExemplarCatalogoValidandoItemEmprestimo(getExemplarExcluir(), getUsuarioLogado())) {
        		getCatalogoVO().getExemplarVOs().remove(getExemplarExcluir());
        		setMensagemID("msg_dados_excluidos");
        	}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			setExemplarExcluir(new ExemplarVO());
    		getMensagemEntidadeReferenciadaExemplarExcluir().clear();
		}
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
    
    public synchronized String executarProcessarArquivoMarc21() throws ConsistirException {
    	ConsistirException consistirException = new ConsistirException();
		try {
			getFacadeFactory().getArquivoMarc21Facade().executarProcessarArquivoMarc21(getArquivoMarc21VO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			if (!getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().isEmpty()) {
				if (getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().size() > 1) {
					for (ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO : getArquivoMarc21VO().getArquivoMarc21CatalogoVOs()) {
						if (Uteis.isAtributoPreenchido(arquivoMarc21CatalogoVO.getCatalogoVO().getTitulo())) {
							try {
								if (!Uteis.isAtributoPreenchido(arquivoMarc21CatalogoVO.getCatalogoVO().getNivelBibliografico())) {
									arquivoMarc21CatalogoVO.getCatalogoVO().setNivelBibliografico("SE");
								}
								getFacadeFactory().getCatalogoFacade().incluir(arquivoMarc21CatalogoVO.getCatalogoVO(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), false);
							} catch (Exception e) {
								consistirException.adicionarListaMensagemErro(e.getMessage());
							}
						}
					}
				}
				setCatalogoVO(getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().get(0).getCatalogoVO());
			}
			if (getCatalogoVO().getAssinaturaPeriodico()) {
				setCatalogoVO(new CatalogoVO());
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroProcessarArquivoMARC21Periodico"));
			}
            inicializarListasSelectItemTodosComboBox();
            getFacadeFactory().getCatalogoFacade().inicializarDadosCatalogoNovo(getCatalogoVO(), getUsuarioLogado());
			setMensagemID("msg_arquivoProcessado");
			return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoForm.xhtml");
		} finally {
			if (!consistirException.getListaMensagemErro().isEmpty()) {
	            setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
			} else {
				consistirException = null;
			}
		}
	}
	
	public void executarExportarArquivoMarc21() {
		try {
			CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
			if (obj == null){
				obj = getCatalogoVO();
			}
			String numeroControle = getCatalogoVO().getNumeroControle();
			ArquivoMarc21CatalogoVO marc21CatalogoVO = new ArquivoMarc21CatalogoVO();
			setArquivoMarc21VO(new ArquivoMarc21VO());
			if (obj != null && !obj.getCodigo().equals(0)) {
				getFacadeFactory().getCatalogoFacade().carregarDados(obj, getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
				marc21CatalogoVO.setCatalogoVO(obj);
			} else {
				marc21CatalogoVO.setCatalogoVO(getCatalogoVO());
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
    
	
	
	
	public String getDownloadArquivoMarc21Catalago(){
		try {
			if (!getArquivoMarc21CatalogoVO().getArquivoVO().getNome().equals("") && getFazerDownloadMarc21() == true) {
				context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoMarc21CatalogoVO().getArquivoVO().getNome());
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + getArquivoMarc21CatalogoVO().getArquivoVO().getPastaBaseArquivo());
				context().getExternalContext().getSessionMap().put("deletarArquivo", true);
				return "location.href='../../DownloadSV'";
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
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

//    public void montarConfiguracaoEspecifica() throws Exception {
//        ConfiguracaoBibliotecaVO obj = (ConfiguracaoBibliotecaVO) context().getExternalContext().getRequestMap().get("configuracaoBiblioteca");
//        getCatalogoVO().setConfiguracaoBiblioteca(obj);
//    }

    public void upLoadArquivo(FileUploadEvent upload) {
        UploadedFile item = upload.getUploadedFile();
//        File item1 = item.getFile();
//
//        try{
//			InputStream is = item.getInputStream();
//		    OutputStream out = new FileOutputStream(new File(upload.getUploadedFile().getName()));
//		    byte buf[] = new byte[1024];
//		    int len;
//		    while ((len = is.read(buf)) > 0)
//		        out.write(buf, 0, len);
//		    is.close();
//		    out.close();
//        }catch(Exception e){
//        	setMensagemDetalhada(e.getMessage());
//        }
        File item1 = new File(upload.getUploadedFile().getName());
        String nome = item1.getName();
        String extensao = nome.substring(nome.lastIndexOf('.') + 1);
        String origem = OrigemArquivo.OBRA_DIGITAL.getValor();
        ArquivoVO arquivoVO = null;
        FileInputStream fi = null;
        byte buffer[] = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {

            buffer = new byte[(int) item1.length()];
            int bytesRead = 0;
            fi = new FileInputStream(item1.getAbsolutePath());
            while ((bytesRead = fi.read(buffer)) != -1) {
                arrayOutputStream.write(buffer, 0, bytesRead);
            }
            arquivoVO = getFacadeFactory().getArquivoFacade().montarArquivo(arrayOutputStream.toByteArray(), nome, extensao, origem, getUsuarioLogado());
            getFacadeFactory().getCatalogoFacade().adicionarArquivoLista(arquivoVO, getCatalogoVO());
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (arrayOutputStream != null) {
                    arrayOutputStream.close();
                }
                upload = null;
                item = null;
                item1 = null;
                nome = null;
                extensao = null;
                origem = null;
                fi = null;
                buffer = null;
                arrayOutputStream = null;
            } catch (IOException ex) {
            }
        }
    }

    public void upLoadArquivoSumarioCapa(FileUploadEvent upload) {
        UploadedFile item = upload.getUploadedFile();
//        upload.getUploadedFile().getName();
//        InputStream input = item.getInputStream();
//        File item1 = item.get
        File item1 =  new File(upload.getUploadedFile().getName());
        String nome = item1.getName();
        String extensao = nome.substring(nome.lastIndexOf('.') + 1);
        String origem = OrigemArquivo.OBRA_SUMARIO.getValor();
        ArquivoVO arquivoVO = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        byte buffer[] = null;
        FileInputStream fi = null;
        try {

            buffer = new byte[(int) item1.length()];
            int bytesRead = 0;
            fi = new FileInputStream(item1.getAbsolutePath());
            while ((bytesRead = fi.read(buffer)) != -1) {
                arrayOutputStream.write(buffer, 0, bytesRead);
            }
            arquivoVO = getFacadeFactory().getArquivoFacade().montarArquivo(arrayOutputStream.toByteArray(), nome, extensao, origem, getUsuarioLogado());
            getFacadeFactory().getCatalogoFacade().adicionarArquivoListaSumarioCapa(arquivoVO, getCatalogoVO());
        } catch (Exception e) {
            setMensagemDetalhada(e.getMessage());
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (arrayOutputStream != null) {
                    arrayOutputStream.close();
                }
                upload = null;
                item = null;
                item1 = null;
                nome = null;
                extensao = null;
                origem = null;
                fi = null;
                buffer = null;
                arrayOutputStream = null;
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Catalogo</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
        	String requisicaocomandoIncluirAlterarExcluirCatalogoEbsco="";
        	getCatalogoVO().setAssinaturaPeriodico(false);
        	getFacadeFactory().getCatalogoFacade().inicializarDadosCatalogoEditar(getCatalogoVO(), getUsuarioLogado());            
            //inicializarDadosListaCatalogoAutor();
            if (getCatalogoVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getCatalogoFacade().incluir(getCatalogoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), true);               
            } else {
                getFacadeFactory().getCatalogoFacade().alterar(getCatalogoVO(), getUsuarioLogado());
                setExibirBotao(Boolean.FALSE);
                if(getCatalogoVO().getEnviadoEbsco()) {                	
                	requisicaocomandoIncluirAlterarExcluirCatalogoEbsco = "c";
                }
            }
            Ordenacao.ordenarLista(getCatalogoVO().getExemplarVOs(), "codigoHashComNumeroExemplar");           
            salvarArquivoBibliotecaExterna();           
            setValidarPossuiIntegracaoEbsco(Boolean.FALSE);
            realizarEnvioCatalogoIntegracaoEbsco(requisicaocomandoIncluirAlterarExcluirCatalogoEbsco);
            setMensagemID("msg_dados_gravados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }
    
    public void executarExportarArquivoMarc21XML() {
    	try {
			CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
			if (obj == null){
				obj = getCatalogoVO();
			}			
			List<ArquivoMarc21VO> arquivoMarc21VOs = getFacadeFactory().getArquivoMarc21Facade().montarArquivoMarc21DadosCatalogo(obj.getCodigo() , getUsuarioLogado());		
			setArquivoMarc21VO(arquivoMarc21VOs.get(0));
			getFacadeFactory().getArquivoMarc21Facade().executarExportarArquivoMarc21XML(arquivoMarc21VOs.get(0),  getUsuarioLogado() , getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade() ,"c");
			setFazerDownloadMarc21(true);
		
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
    	} catch (Exception e) {
    		setFazerDownloadMarc21(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
    	
    }
   
    public void realizarEnvioCatalogoIntegracaoEbsco(){
    	  realizarEnvioCatalogoIntegracaoEbsco("c");
    }
    public void realizarEnvioCatalogoIntegracaoEbsco(String requisicaocomandoIncluirAlterarExcluirCatalogoEbsco) {    	
		try {			
		        CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
				if (obj == null){
					obj = getCatalogoVO();
				}			
			    setArquivoMarc21VO(new ArquivoMarc21VO());
				List<ArquivoMarc21VO> arquivoMarc21VOs =  new ArrayList<ArquivoMarc21VO>(0);				
				ArquivoMarc21CatalogoVO arquivoMarcCatalogoVO = new ArquivoMarc21CatalogoVO();
				arquivoMarcCatalogoVO.setCatalogoVO(obj);
				getArquivoMarc21VO().getArquivoMarc21CatalogoVOs().add(arquivoMarcCatalogoVO);
				arquivoMarc21VOs.add(getArquivoMarc21VO());
				ProgressBarVO progresBar  =  new ProgressBarVO();
				progresBar.setAplicacaoControle(getAplicacaoControle());
				progresBar.setUsuarioVO(getUsuarioLogadoClone());				
				getFacadeFactory().getConfiguracaoBibliotecaFacade().realizarEnvioCatalogoIntegracaoEbsco(arquivoMarc21VOs,progresBar ,getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(),false,requisicaocomandoIncluirAlterarExcluirCatalogoEbsco);				
				setValidarPossuiIntegracaoEbsco(null);
			
				setMensagemID(MSG_TELA.msg_dados_Enviados.name());
		} catch (Exception e) {

			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
    	
    }

	private void inicializarDadosListaCatalogoAutor() {
        getCatalogoVO().getCatalogoAutorVOs().clear();
        for (AutorVO autorVO : getCatalogoVO().getAutorVOs()) {
            getCatalogoAutorVO().setAutor(autorVO);
            getCatalogoAutorVO().setTipoAutoria(autorVO.getTipoAutoria());
            getCatalogoVO().getCatalogoAutorVOs().add(getCatalogoAutorVO());
            setCatalogoAutorVO(new CatalogoAutorVO());
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP CatalogoCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            getControleConsultaOtimizado().setLimitePorPagina(10);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorCodigo(new Integer(valorInt), getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorCodigoCatalogo(new Integer(valorInt), true, 0, getUsuarioLogado()));
            }
            if (getControleConsulta().getCampoConsulta().equals("tituloTitulo")) {
                objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTituloCatalogo(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTituloCatalogo(getControleConsulta().getValorConsulta(), true, 0, getUsuarioLogado()));
                buscarArquivos();
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeEditora")) {
                objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorNomeEditora(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorNomeEditora(getControleConsulta().getValorConsulta(), true, 0, getUsuarioLogado()));
            }
            if(getControleConsulta().getCampoConsulta().equals("autor")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorNomeAutor(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
            	getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorNomeAutor(getControleConsulta().getValorConsulta(), true, 0, getUsuarioLogado()));
            }
            if(getControleConsulta().getCampoConsulta().equals("tombo")) {
            	if (getControleConsulta().getValorConsulta().equals("")) {
            		getControleConsulta().setValorConsulta("%");
            	}
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTombo(getControleConsulta().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
            	getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTombo(getControleConsulta().getValorConsulta(), true, false, 0, getUsuarioLogado()));
            }
            
            
            if(getControleConsulta().getCampoConsulta().equals("assunto")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorAssunto(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
            	getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorAssunto(getControleConsulta().getValorConsulta(), true, false, 0, getUsuarioLogado()));
            }
            if(getControleConsulta().getCampoConsulta().equals("classificacao")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorClassificacao(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorClassificacao(getControleConsulta().getValorConsulta(), true, false, 0, getUsuarioLogado()));
            }
            if(getControleConsulta().getCampoConsulta().equals("tipoCatalogo")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTipoCatalogo(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(),
            			getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
            	getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorTipoCatalogo(getControleConsulta().getValorConsulta(), true, false, 0, getUsuarioLogado()));
            }
            if(getControleConsulta().getCampoConsulta().equals("cutterPha")) {
            	objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorCutterPha(getControleConsulta().getValorConsulta(), getControleConsulta().getOrdenarPor(), getControleConsultaOtimizado().getLimitePorPagina(),
            			getControleConsultaOtimizado().getOffset(), true, false, 0, getUsuarioLogado());
            	getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCatalogoFacade().consultarTotalDeGegistroPorCutterPha(getControleConsulta().getValorConsulta(), true, false, 0, getUsuarioLogado()));
            }
            getControleConsultaOtimizado().setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoCons.xhtml");
        }
    }
    
    public void montarResumoCatalogo() {
		try {
	    	CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
	    	getFacadeFactory().getCatalogoFacade().executarMontarResumoCatalogo(obj);
	    	setCatalogoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setCatalogoVO(new CatalogoVO());
		}
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContaReceberCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();

    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>CatalogoVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
             getFacadeFactory().getCatalogoFacade().excluir(getCatalogoVO(), getUsuarioLogado());
             if(getCatalogoVO().getEnviadoEbsco()) {                	
            	  realizarEnvioCatalogoIntegracaoEbsco("d");
             }           
             novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoForm.xhtml");
        }
    }

    public void montarListaSelectItemSecao() throws Exception {
        try {
            setListaSelectItemSecao(UtilSelectItem.getListaSelectItem(getFacadeFactory().getSecaoFacade().consultarSecaoNivelComboBox(false, getUsuarioLogado()), "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemBiblioteca() throws Exception {
        try {
            setListaSelectItemBiblioteca(UtilSelectItem.getListaSelectItem(getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsinoNivelComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()), "codigo", "nome"));
        } catch (Exception e) {
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

//    public void montarListaSelectItemClassificacaoBibliografica() {
//        try {
//            setListaSelectItemClassificacaoBibliografica(UtilSelectItem.getListaSelectItem(getFacadeFactory().getClassificacaoBibliograficaFacade().consultarClassificacaoBibliograficaComboBox(false, getUsuarioLogado()), "codigo", "nome"));
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
//    }

    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemTipoCatalogo();
//        montarListaSelectItemClassificacaoBibliografica();
        montarListaSelectItemBiblioteca();
        montarListaSelectItemSecao();
        montarListaSelectItemAreaDeConhecimento();
    }

    public String getVerificarAbrirModalEditora() {
        if (getAbrirModalEditora().equals(Boolean.TRUE)) {
            return "RichFaces.$('panelEditora').show()";
        } else {
            return "document.getElementById('form:titulo').focus();";
        }
    }

    public String getVerificarAbrirModalLinguaPublicacao() {
        if (getAbrirModalLinguaPublicacao().equals(Boolean.TRUE)) {
            return "RichFaces.$('panelLinguaPublicacao').show()";
        } else {
            return "document.getElementById('form:dataPadronizada').focus();";
        }
    }

    public String getVerificarAbrirModalCidadePublicacao() {
        if (getAbrirModalCidadePublicacao().equals(Boolean.TRUE)) {
            return "RichFaces.$('panelCidadePublicacao').show()";
        } else {
            return "";
        }
    }

    public String getVerificarAbrirModalAutor() {
        if (getAbrirModalAutor().equals(Boolean.TRUE)) {
            return "RichFaces.$('panelAutor').show()";
        } else {
            return "document.getElementById('form:nomeAutor').focus();";
        }
    }

    public void consultarAutor() {
        List objs = new ArrayList(0);
        try {
            //objs = getFacadeFactory().getAutorFacade().consultarPorNomeAutorOuVariacaoNomeAutor(getNomeAutor(), Uteis.NIVELMONTARDADOS_TODOS);
            objs = getFacadeFactory().getAutorFacade().consultaRapidaNivelComboBoxPorNome(getNomeAutor(), getUsuarioLogado());
            setListaConsultarAutor(objs);
            if (getListaConsultarAutor().isEmpty() || getListaConsultarAutor().size() > 1) {
                setValorConsultarAutor(getNomeAutor());
                setAbrirModalAutor(Boolean.TRUE);
            } else {
                getCatalogoVO().getAutorVOs().add((AutorVO) getListaConsultarAutor().get(0));
                setAbrirModalAutor(Boolean.FALSE);
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarAutor(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
            setNomeAutor(null);
        }
    }

    public void consultarAutorRichModal() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarAutor().equals("codigo")) {
                if (getValorConsultarAutor().length() < 1) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                if (getValorConsultarAutor().equals("")) {
                    setValorConsultarAutor("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarAutor());
                objs = getFacadeFactory().getAutorFacade().consultaRapidaNivelComboBoxPorCodigo(new Integer(valorInt), getUsuarioLogado());
            }
            if (getCampoConsultarAutor().equals("nome")) {
                if (getValorConsultarAutor().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getAutorFacade().consultaRapidaNivelComboBoxPorNome(getValorConsultarAutor(), getUsuarioLogado());
            }
            setListaConsultarAutor(objs);
            setAbrirModalAutor(Boolean.FALSE);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarAutor(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAutor() throws Exception {
        AutorVO obj = (AutorVO) context().getExternalContext().getRequestMap().get("autorConsultadoItens");
        try {
            if (getMensagemDetalhada().equals("")) {
                setCatalogoAutorVO(new CatalogoAutorVO());
                getCatalogoAutorVO().setAutor(obj);
                getCatalogoAutorVO().setTipoAutoria(TipoAutoriaEnum.getTipoAutoriaEnumPorKey("AU").getKey());
                getCatalogoAutorVO().setSiglaAutoria(TipoAutoriaEnum.getTipoAutoriaEnumPorKey("AU").getSigla());
                if(getCatalogoAutorVO().getOrdemApresentacao() == null || getCatalogoAutorVO().getOrdemApresentacao() == 0) {
                	getCatalogoAutorVO().setOrdemApresentacao(getCatalogoVO().getCatalogoAutorVOs().size() + 1);
                    getCatalogoVO().getCatalogoAutorVOs().add(getCatalogoAutorVO());
                } else {
                	getCatalogoVO().getCatalogoAutorVOs().set(getCatalogoAutorVO().getOrdemApresentacao() - 1, getCatalogoAutorVO());
                }
                int i = 1;
                for(CatalogoAutorVO cat : getCatalogoVO().getCatalogoAutorVOs()) {
                	cat.setOrdemApresentacao(i++);
                }
            }
        } finally {
            getListaConsultarAutor().clear();
            setValorConsultarAutor(null);
            setCampoConsultarAutor(null);
            obj = null;
        }
    }

    public void limparCampoAutor() {
        setNomeAutor(null);
        setNovoAutorVO(null);
        setNovoAutorVariacaoNomeVO(null);
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboAutor() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }
    
    public List getTipoConsultarComboFornecedor() {
    	 List itens = new ArrayList(0);
         itens.add(new SelectItem("nome", "Nome"));
         return itens;
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
    
    public void limparCampoFornecedor() {
    	getExemplarVO().setFornecedorVO(new FornecedorVO());
    	getListaConsultaFornecedor().clear();
        setValorConsultaFornecedor(null);
        setCampoConsultaFornecedor(null);
    }

    public void adicionarVariacaoNome() {
        try {
            if (!getNovoAutorVariacaoNomeVO().getVariacaoNome().equals("")) {
                getNovoAutorVO().getListaAutorVariacaoNome().add(getNovoAutorVariacaoNomeVO());
            }
        } finally {
            setNovoAutorVariacaoNomeVO(null);
        }
    }

    public void removerAutorVariacaoNome() {
        AutorVariacaoNomeVO obj = (AutorVariacaoNomeVO) context().getExternalContext().getRequestMap().get("autorVariacaoNomeItens");
        int index = 0;
        for (AutorVariacaoNomeVO autorVariacaoNomeVO : getNovoAutorVO().getListaAutorVariacaoNome()) {
            if (obj.getVariacaoNome().equals(autorVariacaoNomeVO.getVariacaoNome())) {
                getNovoAutorVO().getListaAutorVariacaoNome().remove(index);
                break;
            }
            index++;
        }
        index = 1;
        for (CatalogoAutorVO catalogoAut : getCatalogoVO().getCatalogoAutorVOs()) {
        	catalogoAut.setOrdemApresentacao(index++);
        }
    }

    public void removerAutorLista() {
        CatalogoAutorVO obj = (CatalogoAutorVO) context().getExternalContext().getRequestMap().get("catalogoAutorItens");
        int index = 0;
        for (CatalogoAutorVO catalogoAut : getCatalogoVO().getCatalogoAutorVOs()) {
            if (catalogoAut.getCodigo().equals(obj.getCodigo())) {
                getCatalogoVO().getCatalogoAutorVOs().remove(index);
                break;
            }
            index = index + 1;
        }
        index = 1;
        for (CatalogoAutorVO catalogoAut : getCatalogoVO().getCatalogoAutorVOs()) {
        	catalogoAut.setOrdemApresentacao(index++);
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Editora</code> por meio dos parametros informados
     * no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
     * informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarEditora() {
        try {
            List objs = new ArrayList(0);
            objs = getFacadeFactory().getEditoraFacade().consultarPorNome(getCatalogoVO().getEditora().getNome(), false,
                    Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            setListaConsultarEditora(objs);
            if (getListaConsultarEditora().size() == 0 || getListaConsultarEditora().size() > 1) {
                setValorConsultarEditora(getCatalogoVO().getEditora().getNome());
                setAbrirModalEditora(Boolean.TRUE);
            } else {
                getCatalogoVO().setEditora((EditoraVO) getListaConsultarEditora().get(0));
                setAbrirModalEditora(Boolean.FALSE);
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarEditora(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarEditoraRichModal() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarEditora().equals("codigo")) {
                if (getValorConsultarEditora().equals("")) {
                    setValorConsultarEditora("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarEditora());
                //objs = getFacadeFactory().getEditoraFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                objs = getFacadeFactory().getEditoraFacade().consultarEdiraPorCodigoComboBox(new Integer(valorInt), false, getUsuarioLogado());
            }
            if (getCampoConsultarEditora().equals("nome")) {
                //objs = getFacadeFactory().getEditoraFacade().consultarPorNome(getValorConsultarEditora(), false,Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                objs = getFacadeFactory().getEditoraFacade().consultarEdiraPorNomeComboBox(getValorConsultarEditora(), false, getUsuarioLogado());
            }
            setListaConsultarEditora(objs);
            setAbrirModalEditora(Boolean.FALSE);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarEditora(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarEditora() throws Exception {
        EditoraVO obj = (EditoraVO) context().getExternalContext().getRequestMap().get("editoraItens");
        try {
            if (getMensagemDetalhada().equals("")) {
                this.getCatalogoVO().setEditora(obj);
            }
        } finally {
            getListaConsultarEditora().clear();
            setValorConsultarEditora(null);
            setCampoConsultarEditora(null);
            obj = null;
        }
    }

    public void limparCampoEditora() {
        getCatalogoVO().setEditora(null);
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboEditora() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void consultarLinguaPublicacao() {
        try {
//            List objs = new ArrayList(0);
//            objs = getFacadeFactory().getLinguaPublicacaoCatalogoFacade().consultarPorNome(
//                    getCatalogoVO().getLinguaPublicacaoCatalogo().getNome(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//            setListaConsultarLinguaPublicacao(objs);
//            if (getListaConsultarLinguaPublicacao().size() == 0 || getListaConsultarLinguaPublicacao().size() > 1) {
//                setValorConsultarLinguaPublicacao(getCatalogoVO().getLinguaPublicacaoCatalogo().getNome());
            setAbrirModalLinguaPublicacao(Boolean.TRUE);
//            } else {
//                getCatalogoVO().setLinguaPublicacaoCatalogo((LinguaPublicacaoCatalogoVO) getListaConsultarLinguaPublicacao().get(0));
//                setAbrirModalLinguaPublicacao(Boolean.FALSE);
//            }
//            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarLinguaPublicacao(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarLinguaPublicacaoRichModal() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarLinguaPublicacao().equals("codigo")) {
                if (getValorConsultarLinguaPublicacao().equals("")) {
                    setValorConsultarLinguaPublicacao("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarLinguaPublicacao());
                objs = getFacadeFactory().getLinguaPublicacaoCatalogoFacade().consultarPorCodigo(new Integer(valorInt), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarLinguaPublicacao().equals("nome")) {
                objs = getFacadeFactory().getLinguaPublicacaoCatalogoFacade().consultarPorNome(getValorConsultarLinguaPublicacao(), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultarLinguaPublicacao(objs);
            setAbrirModalLinguaPublicacao(Boolean.FALSE);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarLinguaPublicacao(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarLinguaPublicacao() throws Exception {
        LinguaPublicacaoCatalogoVO obj = (LinguaPublicacaoCatalogoVO) context().getExternalContext().getRequestMap().get("linguaPublicacaoItens");
        try {
            if (getMensagemDetalhada().equals("")) {
                this.getCatalogoVO().setLinguaPublicacaoCatalogo(obj);
            }
        } finally {
            getListaConsultarLinguaPublicacao().clear();
            setValorConsultarLinguaPublicacao(null);
            setCampoConsultarLinguaPublicacao(null);
            obj = null;
        }
    }

    public void limparCampoLinguaPublicacao() {
        getCatalogoVO().setLinguaPublicacaoCatalogo(null);
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboLinguaPublicacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void consultarCidadePublicacao() {
        try {
//            List objs = new ArrayList(0);
//            objs = getFacadeFactory().getCidadePublicacaoCatalogoFacade().consultarPorNome(
//                    getCatalogoVO().getCidadePublicacaoCatalogo().getNome(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//            setListaConsultarCidadePublicacao(objs);
//            if (getListaConsultarCidadePublicacao().size() == 0 || getListaConsultarCidadePublicacao().size() > 1) {
//                setValorConsultarCidadePublicacao(getCatalogoVO().getCidadePublicacaoCatalogo().getNome());
            setAbrirModalCidadePublicacao(Boolean.TRUE);
//            } else {
//                getCatalogoVO().setCidadePublicacaoCatalogo((CidadePublicacaoCatalogoVO) getListaConsultarCidadePublicacao().get(0));
//                setAbrirModalCidadePublicacao(Boolean.FALSE);
//            }
//            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarCidadePublicacao(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
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
            setAbrirModalCidadePublicacao(Boolean.FALSE);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarCidadePublicacao(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
	public boolean getIsArquivoSelecionado() {
		if ((getArquivoMarc21VO().getArquivoVO().getNome() != null && !getArquivoMarc21VO().getArquivoVO().getNome().equals(""))) {
			return true;
		}
		return false;
	}

    public void selecionarCidadePublicacao() throws Exception {
        CidadePublicacaoCatalogoVO obj = (CidadePublicacaoCatalogoVO) context().getExternalContext().getRequestMap().get("cidadePublicacaoItens");
        try {
            if (getMensagemDetalhada().equals("")) {
                this.getCatalogoVO().setCidadePublicacaoCatalogo(obj);
            }
        } finally {
            getListaConsultarCidadePublicacao().clear();
            setValorConsultarCidadePublicacao(null);
            setCampoConsultarCidadePublicacao(null);
            obj = null;
        }
    }

    public void limparCampoCidadePublicacao() {
        getCatalogoVO().setCidadePublicacaoCatalogo(null);
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboCidadePublicacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void consultarConfiguracaoBiblioteca() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarConfiguracaoBiblioteca().equals("codigo")) {
                if (getValorConsultarConfiguracaoBiblioteca().equals("")) {
                    setValorConsultarConfiguracaoBiblioteca("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarConfiguracaoBiblioteca());
                objs = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorCodigo(new Integer(valorInt), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultarConfiguracaoBiblioteca(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarConfiguracaoBiblioteca(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultarComboConfiguracaoBiblioteca() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

//    public void limparCampoConfiguracaoBiblioteca() throws Exception {
//        this.getCatalogoVO().setConfiguracaoBiblioteca(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPadrao(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//    }

    public Boolean getTipoCatalogo() {
        if (getControleConsulta().getCampoConsulta().equals("tipoCatalogo")) {
            return true;
        }
        return false;
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("tituloTitulo", "Título"));
        itens.add(new SelectItem("nomeEditora", "Editora"));
        itens.add(new SelectItem("autor", "Autor"));
        itens.add(new SelectItem("tombo", "Tombo"));
        itens.add(new SelectItem("assunto", "Assunto"));
        itens.add(new SelectItem("classificacao", "Classificação"));
        itens.add(new SelectItem("tipoCatalogo", "Tipo Catálogo"));
        itens.add(new SelectItem("cutterPha", "Cutter/PHA"));
        return itens;
    }
    
    public List getTipoOrdenarPorCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("edicao", "Edição"));
        itens.add(new SelectItem("titulo", "Título"));
        itens.add(new SelectItem("ano", "Ano Publicação"));
        itens.add(new SelectItem("crescente", "Ordem Crescente"));
        itens.add(new SelectItem("decrescente", "Ordem Decrescente"));
        return itens;
    }

    public List getListaSelectItemNivelBibliografico() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("MO", "Monográfico"));
        itens.add(new SelectItem("SE", "Série"));
        return itens;
    }

    public List getListaSelectItemSituacaoAtual() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem(SituacaoExemplar.DISPONIVEL.getValor(), SituacaoExemplar.DISPONIVEL.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.CONSULTA.getValor(), SituacaoExemplar.CONSULTA.getDescricao()));
//		itens.add(new SelectItem(SituacaoExemplar.RESERVADO.getValor(), SituacaoExemplar.RESERVADO.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.EMPRESTADO.getValor(), SituacaoExemplar.EMPRESTADO.getDescricao()));
        itens.add(new SelectItem(SituacaoExemplar.INUTILIZADO.getValor(), SituacaoExemplar.INUTILIZADO.getDescricao()));
        return itens;
    }

    private List<SelectItem> listaSelectItemTipoAutoria;
    public List<SelectItem> getListaSelectItemTipoAutoria() {
        if(listaSelectItemTipoAutoria == null){
            listaSelectItemTipoAutoria = new ArrayList<SelectItem>(0);
            for(TipoAutoriaEnum tipoAutoriaEnum:TipoAutoriaEnum.values()){
                listaSelectItemTipoAutoria.add(new SelectItem(tipoAutoriaEnum.getKey(), tipoAutoriaEnum.getValue(), tipoAutoriaEnum.getSigla()));
            }
        }
        return listaSelectItemTipoAutoria;
    }

    public List getListaSelectItemTipoEntrada() {
      return  UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoEntradaAcervo.class,false);
    }
    
    public List getListaSelectItemTipoMidia() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem(TipoMidiaEnum.NAO_POSSUI.getKey(), TipoMidiaEnum.NAO_POSSUI.getValue()));
        itens.add(new SelectItem(TipoMidiaEnum.CD.getKey(), TipoMidiaEnum.CD.getValue()));
        itens.add(new SelectItem(TipoMidiaEnum.DVD.getKey(), TipoMidiaEnum.DVD.getValue()));
        itens.add(new SelectItem(TipoMidiaEnum.VHS.getKey(), TipoMidiaEnum.VHS.getValue()));
        return itens;
    }

    public List getListaSelectItemTipoExemplar() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("AV", "Recurso Audio-Visual"));
        itens.add(new SelectItem("PU", "Publicação"));
        return itens;
    }
    
    public Boolean getExibirCamposAdicionaisCompra() {
    	if(getExemplarVO().getTipoEntrada().equals("CO")) {
    		return true;
    	}
    	return false;
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        getControleConsultaOtimizado().getListaConsulta().clear();
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("catalogoCons.xhtml");
    }

    public String getCampoConsultarEditora() {
        return campoConsultarEditora;
    }

    public void setCampoConsultarEditora(String campoConsultarEditora) {
        this.campoConsultarEditora = campoConsultarEditora;
    }

    public String getValorConsultarEditora() {
        return valorConsultarEditora;
    }

    public void setValorConsultarEditora(String valorConsultarEditora) {
        this.valorConsultarEditora = valorConsultarEditora;
    }

    public List getListaConsultarEditora() {
        if (listaConsultarEditora == null) {
            listaConsultarEditora = new ArrayList(0);
        }
        return listaConsultarEditora;
    }

    public void setListaConsultarEditora(List listaConsultarEditora) {
        this.listaConsultarEditora = listaConsultarEditora;
    }

    public CatalogoVO getCatalogoVO() {
        if (catalogoVO == null) {
            catalogoVO = new CatalogoVO();
        }
        return catalogoVO;
    }

    public void setCatalogoVO(CatalogoVO catalogoVO) {
        this.catalogoVO = catalogoVO;
    }

    public List getListaSelectItemTipoCatalogo() {
        if (listaSelectItemTipoCatalogo == null) {
            listaSelectItemTipoCatalogo = new ArrayList(0);
        }
        return listaSelectItemTipoCatalogo;
    }

    public void setListaSelectItemTipoCatalogo(List listaSelectItemTipoCatalogo) {
        this.listaSelectItemTipoCatalogo = listaSelectItemTipoCatalogo;
    }

    public String getCampoConsultarConfiguracaoBiblioteca() {
        if (campoConsultarConfiguracaoBiblioteca == null) {
            campoConsultarConfiguracaoBiblioteca = "";
        }
        return campoConsultarConfiguracaoBiblioteca;
    }

    public void setCampoConsultarConfiguracaoBiblioteca(String campoConsultarConfiguracaoBiblioteca) {
        this.campoConsultarConfiguracaoBiblioteca = campoConsultarConfiguracaoBiblioteca;
    }

    public String getValorConsultarConfiguracaoBiblioteca() {
        if (valorConsultarConfiguracaoBiblioteca == null) {
            valorConsultarConfiguracaoBiblioteca = "";
        }
        return valorConsultarConfiguracaoBiblioteca;
    }

    public void setValorConsultarConfiguracaoBiblioteca(String valorConsultarConfiguracaoBiblioteca) {
        this.valorConsultarConfiguracaoBiblioteca = valorConsultarConfiguracaoBiblioteca;
    }

    public List getListaConsultarConfiguracaoBiblioteca() {
        if (listaConsultarConfiguracaoBiblioteca == null) {
            listaConsultarConfiguracaoBiblioteca = new ArrayList(0);
        }
        return listaConsultarConfiguracaoBiblioteca;
    }

    public void setListaConsultarConfiguracaoBiblioteca(List listaConsultarConfiguracaoBiblioteca) {
        this.listaConsultarConfiguracaoBiblioteca = listaConsultarConfiguracaoBiblioteca;
    }

//    public List getListaSelectItemClassificacaoBibliografica() {
//        if (listaSelectItemClassificacaoBibliografica == null) {
//            listaSelectItemClassificacaoBibliografica = new ArrayList(0);
//        }
//        return listaSelectItemClassificacaoBibliografica;
//    }
//
//    public void setListaSelectItemClassificacaoBibliografica(List listaSelectItemClassificacaoBibliografica) {
//        this.listaSelectItemClassificacaoBibliografica = listaSelectItemClassificacaoBibliografica;
//    }

    public List getListaSelectItemBiblioteca() {
        if (listaSelectItemBiblioteca == null) {
            listaSelectItemBiblioteca = new ArrayList(0);
        }
        return listaSelectItemBiblioteca;
    }

    public void setListaSelectItemBiblioteca(List listaSelectItemBiblioteca) {
        this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
    }

    public List getListaSelectItemSecao() {
        if (listaSelectItemSecao == null) {
            listaSelectItemSecao = new ArrayList(0);
        }
        return listaSelectItemSecao;
    }

    public void setListaSelectItemSecao(List listaSelectItemSecao) {
        this.listaSelectItemSecao = listaSelectItemSecao;
    }

    public void setNivelBibliografico(String nivelBibliografico) {
        this.nivelBibliografico = nivelBibliografico;
    }

    public String getNivelBibliografico() {
        if (nivelBibliografico == null) {
            nivelBibliografico = "";
        }
        return nivelBibliografico;
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

    public void setNumeroExemplaresAGerar(Integer numeroExemplaresAGerar) {
        this.numeroExemplaresAGerar = numeroExemplaresAGerar;
    }

    public Integer getNumeroExemplaresAGerar() {
        if (numeroExemplaresAGerar == null) {
            numeroExemplaresAGerar = 0;
        }
        return numeroExemplaresAGerar;
    }

    public void setAbrirModalEditora(Boolean abrirModalEditora) {
        this.abrirModalEditora = abrirModalEditora;
    }

    public Boolean getAbrirModalEditora() {
        if (abrirModalEditora == null) {
            abrirModalEditora = Boolean.FALSE;
        }
        return abrirModalEditora;
    }

    public String getCampoConsultarLinguaPublicacao() {
        if (campoConsultarLinguaPublicacao == null) {
            campoConsultarLinguaPublicacao = "";
        }
        return campoConsultarLinguaPublicacao;
    }

    public void setCampoConsultarLinguaPublicacao(String campoConsultarLinguaPublicacao) {
        this.campoConsultarLinguaPublicacao = campoConsultarLinguaPublicacao;
    }

    public String getValorConsultarLinguaPublicacao() {
        if (valorConsultarLinguaPublicacao == null) {
            valorConsultarLinguaPublicacao = "";
        }
        return valorConsultarLinguaPublicacao;
    }

    public void setValorConsultarLinguaPublicacao(String valorConsultarLinguaPublicacao) {
        this.valorConsultarLinguaPublicacao = valorConsultarLinguaPublicacao;
    }

    public List getListaConsultarLinguaPublicacao() {
        if (listaConsultarLinguaPublicacao == null) {
            listaConsultarLinguaPublicacao = new ArrayList(0);
        }
        return listaConsultarLinguaPublicacao;
    }

    public void setListaConsultarLinguaPublicacao(List listaConsultarLinguaPublicacao) {
        this.listaConsultarLinguaPublicacao = listaConsultarLinguaPublicacao;
    }

    public void setAbrirModalLinguaPublicacao(Boolean abrirModalLinguaPublicacao) {
        this.abrirModalLinguaPublicacao = abrirModalLinguaPublicacao;
    }

    public Boolean getAbrirModalLinguaPublicacao() {
        if (abrirModalLinguaPublicacao == null) {
            abrirModalLinguaPublicacao = Boolean.FALSE;
        }
        return abrirModalLinguaPublicacao;
    }

    public void setNovaLinguaPublicacaoCatalogoVO(LinguaPublicacaoCatalogoVO novaLinguaPublicacaoCatalogoVO) {
        this.novaLinguaPublicacaoCatalogoVO = novaLinguaPublicacaoCatalogoVO;
    }

    public LinguaPublicacaoCatalogoVO getNovaLinguaPublicacaoCatalogoVO() {
        if (novaLinguaPublicacaoCatalogoVO == null) {
            novaLinguaPublicacaoCatalogoVO = new LinguaPublicacaoCatalogoVO();
        }
        return novaLinguaPublicacaoCatalogoVO;
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

    public String getCampoConsultarCidadePublicacao() {
        if (campoConsultarCidadePublicacao == null) {
            campoConsultarCidadePublicacao = "";
        }
        return campoConsultarCidadePublicacao;
    }

    public void setCampoConsultarCidadePublicacao(String campoConsultarCidadePublicacao) {
        this.campoConsultarCidadePublicacao = campoConsultarCidadePublicacao;
    }

    public String getValorConsultarCidadePublicacao() {
        if (valorConsultarCidadePublicacao == null) {
            valorConsultarCidadePublicacao = "";
        }
        return valorConsultarCidadePublicacao;
    }

    public void setValorConsultarCidadePublicacao(String valorConsultarCidadePublicacao) {
        this.valorConsultarCidadePublicacao = valorConsultarCidadePublicacao;
    }

    public List getListaConsultarCidadePublicacao() {
        if (listaConsultarCidadePublicacao == null) {
            listaConsultarCidadePublicacao = new ArrayList(0);
        }
        return listaConsultarCidadePublicacao;
    }

    public void setListaConsultarCidadePublicacao(List listaConsultarCidadePublicacao) {
        this.listaConsultarCidadePublicacao = listaConsultarCidadePublicacao;
    }

    public Boolean getAbrirModalCidadePublicacao() {
        if (abrirModalCidadePublicacao == null) {
            abrirModalCidadePublicacao = Boolean.FALSE;
        }
        return abrirModalCidadePublicacao;
    }

    public void setAbrirModalCidadePublicacao(Boolean abrirModalCidadePublicacao) {
        this.abrirModalCidadePublicacao = abrirModalCidadePublicacao;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public String getNomeAutor() {
        if (nomeAutor == null) {
            nomeAutor = "";
        }
        return nomeAutor;
    }

    public String getCampoConsultarAutor() {
        if (campoConsultarAutor == null) {
            campoConsultarAutor = "";
        }
        return campoConsultarAutor;
    }

    public void setCampoConsultarAutor(String campoConsultarAutor) {
        this.campoConsultarAutor = campoConsultarAutor;
    }

    public String getValorConsultarAutor() {
        if (valorConsultarAutor == null) {
            valorConsultarAutor = "";
        }
        return valorConsultarAutor;
    }

    public void setValorConsultarAutor(String valorConsultarAutor) {
        this.valorConsultarAutor = valorConsultarAutor;
    }

    public List getListaConsultarAutor() {
        if (listaConsultarAutor == null) {
            listaConsultarAutor = new ArrayList(0);
        }
        return listaConsultarAutor;
    }

    public void setListaConsultarAutor(List listaConsultarAutor) {
        this.listaConsultarAutor = listaConsultarAutor;
    }

    public Boolean getAbrirModalAutor() {
        if (abrirModalAutor == null) {
            abrirModalAutor = Boolean.FALSE;
        }
        return abrirModalAutor;
    }

    public void setAbrirModalAutor(Boolean abrirModalAutor) {
        this.abrirModalAutor = abrirModalAutor;
    }

    public void setNovoAutorVO(AutorVO novoAutorVO) {
        this.novoAutorVO = novoAutorVO;
    }

    public AutorVO getNovoAutorVO() {
        if (novoAutorVO == null) {
            novoAutorVO = new AutorVO();
        }
        return novoAutorVO;
    }

    public void setNovoAutorVariacaoNomeVO(AutorVariacaoNomeVO novoAutorVariacaoNomeVO) {
        this.novoAutorVariacaoNomeVO = novoAutorVariacaoNomeVO;
    }

    public AutorVariacaoNomeVO getNovoAutorVariacaoNomeVO() {
        if (novoAutorVariacaoNomeVO == null) {
            novoAutorVariacaoNomeVO = new AutorVariacaoNomeVO();
        }
        return novoAutorVariacaoNomeVO;
    }

    public void setCatalogoAutorVO(CatalogoAutorVO catalogoAutorVO) {
        this.catalogoAutorVO = catalogoAutorVO;
    }

    public CatalogoAutorVO getCatalogoAutorVO() {
        if (catalogoAutorVO == null) {
            catalogoAutorVO = new CatalogoAutorVO();
        }
        return catalogoAutorVO;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }
    

    public void renderizarCampoBibliografia() {
        if (!getCatalogoVO().getControlaTempoDefasagem()) {
            getCatalogoVO().setBibliograficaBasicaMes(0);
            getCatalogoVO().setBibliograficaComplementarMes(0);
        }
    }
    
    public void consultarCurso() {
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultarCursoApresentarBiblioteca(getCampoConsultaCurso(), getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
	public void adicionarCatalogoCursoVOs() throws Exception {
		try {
			validarCursoSelecionado();
			for (CursoVO cursoVO : getListaConsultaCurso()) {
				if (cursoVO.getCursoSelecionado()) {
					getCatalogoCursoVO().setCursoVO(cursoVO);
					getCatalogoCursoVO().setCatalogoVO(getCatalogoVO());
					getCatalogoVO().getCatalogoCursoVOs().add(getCatalogoCursoVO());
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
		for (Iterator iterator = getCatalogoVO().getCatalogoCursoVOs().iterator(); iterator.hasNext();) {
			CatalogoCursoVO catalogoCurso = (CatalogoCursoVO) iterator.next();
			if (catalogoCursoVO.getCursoVO().getCodigo().equals(catalogoCurso.getCursoVO().getCodigo())) {
				iterator.remove();
			}
		}
	}
	
	public void removerAreaConhecimentoVOs() throws Exception {
		CatalogoAreaConhecimentoVO catalogoAreaConhecimentoVO = (CatalogoAreaConhecimentoVO) context().getExternalContext().getRequestMap().get("catalogoAreaConhecimentoItens");
		for (Iterator iterator = getCatalogoVO().getCatalogoAreaConhecimentoVOs().iterator(); iterator.hasNext();) {
			CatalogoAreaConhecimentoVO catalogoAreaConhecimento = (CatalogoAreaConhecimentoVO) iterator.next();
			if (catalogoAreaConhecimentoVO.getAreaConhecimentoVO().getCodigo().equals(catalogoAreaConhecimento.getAreaConhecimentoVO().getCodigo())) {
				iterator.remove();
			}
		}
	}
	
	public void montarResumoEmprestimoExemplar() throws Exception {
		try {
			setEmprestimoVO(new EmprestimoVO());
			ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
			setEmprestimoVO(getFacadeFactory().getEmprestimoFacade().consultarResumoEmprestimoPorCodigoExemplar(obj.getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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
	
	public void validarCursoSelecionado() throws Exception {
		boolean cursoSelecionado = false;
		try {
			for (CursoVO cursoVO : getListaConsultaCurso()) {
				if (cursoVO.getCursoSelecionado()) {
					for (CatalogoCursoVO catalogoCursoVO : getCatalogoVO().getCatalogoCursoVOs()) {
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

	public CatalogoCursoVO getCatalogoCursoVO() {
		if(catalogoCursoVO == null) {
			catalogoCursoVO = new CatalogoCursoVO();
		}
		return catalogoCursoVO;
	}

	public void setCatalogoCursoVO(CatalogoCursoVO catalogoCursoVO) {
		this.catalogoCursoVO = catalogoCursoVO;
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
		if( valorConsultaFornecedor== null){
			valorConsultaFornecedor= "";
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

	public List getListaSelectItemAreaDeConhecimento() {
		return listaSelectItemAreaDeConhecimento;
	}

	public void setListaSelectItemAreaDeConhecimento(List listaSelectItemAreaDeConhecimento) {
		this.listaSelectItemAreaDeConhecimento = listaSelectItemAreaDeConhecimento;
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

	public EmprestimoVO getEmprestimoVO() {
		if (emprestimoVO == null) {
			emprestimoVO = new EmprestimoVO();
		}
		return emprestimoVO;
	}

	public void setEmprestimoVO(EmprestimoVO emprestimoVO) {
		this.emprestimoVO = emprestimoVO;
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
			getFacadeFactory().getCatalogoFacade().carregarDados(catalogoVO, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
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
	
	public void alterarSiglaAutor() {
		CatalogoAutorVO catalogoAutorVO = (CatalogoAutorVO) getRequestMap().get("catalogoAutorItens");
		catalogoAutorVO.setSiglaAutoria(catalogoAutorVO.getTipoAutoriaEnum().getSigla());
	}
	
	public void editarOrdemAutoresApresentacao() {
		try {
			CatalogoAutorVO catAut = (CatalogoAutorVO) context().getExternalContext().getRequestMap().get("catalogoAutorItens");
			if(catAut == null && getCatalogoAutorVO().getOrdemApresentacao() != null && getCatalogoAutorVO().getOrdemApresentacao() > 0) {
				for(CatalogoAutorVO catAut1 : getCatalogoVO().getCatalogoAutorVOs()) {
					if (catAut1.getOrdemApresentacao().equals(getCatalogoAutorVO().getOrdemApresentacao())) {
						getCatalogoAutorVO().setOrdemApresentacao(0);
					}
				}
			}
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void alterarOrdemApresentacaoAutores (DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof CatalogoAutorVO && dropEvent.getDropValue() instanceof CatalogoAutorVO) {
				getFacadeFactory().getCatalogoFacade().alterarOrdemAutores(getCatalogoVO(), (CatalogoAutorVO) dropEvent.getDragValue(), (CatalogoAutorVO) dropEvent.getDropValue());
				limparMensagem();
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public List getListaSelectItemTipoUploadCatalogo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("Capa", "Capa"));
        itens.add(new SelectItem("Contra-Capa", "Contra-Capa"));
        itens.add(new SelectItem("Sumário", "Sumário"));
        itens.add(new SelectItem("Resumo", "Resumo"));
        itens.add(new SelectItem("Download", "Download"));
        return itens;
    }

	public Boolean getTipoUploadCatalogoResumo() {
		if (tipoUploadCatalogoResumo == null) {
			tipoUploadCatalogoResumo = false;
		}
		return tipoUploadCatalogoResumo;
	}
	

	public void setTipoUploadCatalogoResumo(Boolean tipoUploadCatalogoResumo) {
		this.tipoUploadCatalogoResumo = tipoUploadCatalogoResumo;

	}public String getTipoUploadApresentar() {
				
		if (tipoUploadApresentar == null) {
			tipoUploadApresentar = "";
		}
		
		return tipoUploadApresentar;
	}

	public void setTipoUploadApresentar(String tipoUploadApresentar) {
		this.tipoUploadApresentar = tipoUploadApresentar;
	}

	public String getErroUpload() {
	
		if (erroUpload == null) {
			erroUpload = "";
		}
		return erroUpload;
	}

	public void setErroUpload(String erroUpload) {
		this.erroUpload = erroUpload;
	}

	public String getMsgErroUpload() {
		if (MsgErroUpload == null) {
			MsgErroUpload = "";
		}
		return MsgErroUpload;
	}

	public void setMsgErroUpload(String msgErroUpload) {
		MsgErroUpload = msgErroUpload;
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

	
	
	public String getCaminhoImagemCapa() {
		if (caminhoImagemCapa == null) {
			caminhoImagemCapa = "";
		}
		return caminhoImagemCapa;
	}

	public void setCaminhoImagemCapa(String caminhoImagemCapa) {
		this.caminhoImagemCapa = caminhoImagemCapa;
	}

	public String getCaminhoImagemContraCapa() {
		if (caminhoImagemContraCapa == null) {
			caminhoImagemContraCapa = "";
		}
		return caminhoImagemContraCapa;
	}

	public void setCaminhoImagemContraCapa(String caminhoImagemContraCapa) {
		this.caminhoImagemContraCapa = caminhoImagemContraCapa;
	}	
	

	public Boolean getTipoUploadCatalogoContraCapa() {
		if (tipoUploadCatalogoContraCapa == null) {
			tipoUploadCatalogoContraCapa = false;
		}
		return tipoUploadCatalogoContraCapa;
	}

	public void setTipoUploadCatalogoContraCapa(Boolean tipoUploadCatalogoContraCapa) {
		this.tipoUploadCatalogoContraCapa = tipoUploadCatalogoContraCapa;
	}

	public Boolean getTipoUploadCatalogoCapa() {
		if (tipoUploadCatalogoCapa == null) {
			tipoUploadCatalogoCapa = false;
		}
		return tipoUploadCatalogoCapa;
	}

	public void setTipoUploadCatalogoCapa(Boolean tipoUploadCatalogoCapa) {
		this.tipoUploadCatalogoCapa = tipoUploadCatalogoCapa;
	}

	public Boolean getTipoUploadCatalogoSumario() {
		if (tipoUploadCatalogoSumario == null) {
			tipoUploadCatalogoSumario = false;
		}
		return tipoUploadCatalogoSumario;
	}

	public void setTipoUploadCatalogoSumario(Boolean tipoUploadCatalogoSumario) {
		this.tipoUploadCatalogoSumario = tipoUploadCatalogoSumario;
	}

	public Boolean getTipoUploadCatalogoDownload() {
		if (tipoUploadCatalogoDownload == null) {
			tipoUploadCatalogoDownload = false;
		}
		return tipoUploadCatalogoDownload;
	}

	public void setTipoUploadCatalogoDownload(Boolean tipoUploadCatalogoDownload) {
		this.tipoUploadCatalogoDownload = tipoUploadCatalogoDownload;
	}
	
	

	public String getCaminhoSumario() {
		if (caminhoSumario == null) {
			caminhoSumario = "";
		}		
		return caminhoSumario;
	}

	public void setCaminhoSumario(String caminhoSumario) {
		this.caminhoSumario = caminhoSumario;
	}

	public String getCaminhoResumo() {
		if (caminhoResumo == null) {
			caminhoResumo = "";
		}
		return caminhoResumo;
	}

	public void setCaminhoResumo(String caminhoResumo) {
		this.caminhoResumo = caminhoResumo;
	}

	public String getCaminhoDownload() {
		if (caminhoDownload == null) {
			caminhoDownload = "";
		}
		return caminhoDownload;
	}

	public void setCaminhoDownload(String caminhoDownload) {
		this.caminhoDownload = caminhoDownload;
	}
	
	

	public List<ArquivoVO> getListaArquivosBibliotecaExterna() {
		
		if (listaArquivosBibliotecaExterna == null) {
			listaArquivosBibliotecaExterna = new ArrayList<ArquivoVO>(0);
		}	
		return listaArquivosBibliotecaExterna;
	}

	public void setListaArquivosBibliotecaExterna(List<ArquivoVO> listaArquivosBibliotecaExterna) {
		this.listaArquivosBibliotecaExterna = listaArquivosBibliotecaExterna;
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

	public List<ExemplarVO> getListasExemplarEditar() {
		if (listasExemplarEditar == null) {
			listasExemplarEditar = new ArrayList<ExemplarVO>(0); 
		}
		return listasExemplarEditar;
	}

	public void setListasExemplarEditar(List<ExemplarVO> listasExemplarEditar) {
		this.listasExemplarEditar = listasExemplarEditar;
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
	
	public Boolean getValorAlteracaoBoolean() {
		if (valorAlteracaoBoolean == null) {
			valorAlteracaoBoolean = false;
		}
		return valorAlteracaoBoolean;
	}

	public void setValorAlteracaoBoolean(Boolean valorAlteracaoBoolean) {
		this.valorAlteracaoBoolean = valorAlteracaoBoolean;
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
	
	

	public String getCaminhoarquivoAnexado() {
		if (caminhoarquivoAnexado == null) {
			caminhoarquivoAnexado = "";
		}
		return caminhoarquivoAnexado;
	}

	public void setCaminhoarquivoAnexado(String caminhoarquivoAnexado) {
		this.caminhoarquivoAnexado = caminhoarquivoAnexado;
	}

	

	public Boolean getExibirBotaoConfirmaArquivo() {
		if (exibirBotaoConfirmaArquivo == null) {
			exibirBotaoConfirmaArquivo = false;
		}
		return exibirBotaoConfirmaArquivo;
	}

	public void setExibirBotaoConfirmaArquivo(Boolean exibirBotaoConfirmaArquivo) {
		this.exibirBotaoConfirmaArquivo = exibirBotaoConfirmaArquivo;
	}

	public void upLoadArquivoBibliotecaExterna(FileUploadEvent uploadEvent) {
			
		limparDadosArquivos();		
			arquivoVO = new ArquivoVO();			
		File arquivoImagem = null;
		BufferedImage imagemOriginal = null;
		BufferedImage resizedImage = null;
		int newImageWidth = 280;
		int newImageHeight = 405;
			
		try {				
					
			
				if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile().getSize() > 15360000) {
					setErroUpload("RichFaces.$('panelMsgErroUpload').show()");
					setMsgErroUpload("Arquivo maior que o permitido na configuração geral do sistema.");
				} else {
					setErroUpload("RichFaces.$('panelMsgErroUpload').hide()");
					getArquivoVO().setAgrupador(getTipoUploadApresentar());	
				//	getArquivoVO().setDataDisponibilizacao(new Date());
			//		getArquivoVO().setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+getArquivoVO().getPastaBaseArquivo()+File.separator+getCatalogoVO().getCodigo());
					getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP, getUsuarioLogado());				   			    
					arquivoVO.setExtensao(arquivoVO.getDescricao().substring(arquivoVO.getDescricao().lastIndexOf("."), arquivoVO.getDescricao().length()));
					String diretorioFoto = 	getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + "/"+PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP.getValue()+ "/"+ arquivoVO.getNome();					
					arquivoImagem = new File(diretorioFoto);
					imagemOriginal = ImageIO.read(arquivoImagem);				
					resizedImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = resizedImage.createGraphics();
					g.setComposite(AlphaComposite.Src);
					g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.drawImage(imagemOriginal, 0, 0, newImageWidth, newImageHeight, null);
					g.dispose();
					ImageIO.write(resizedImage, arquivoVO.getDescricao().substring(arquivoVO.getDescricao().lastIndexOf(".") +1, arquivoVO.getDescricao().length()), arquivoImagem);
					getListaArquivosBibliotecaExterna().add(arquivoVO);
				    arquivoTemporario();
				    if (arquivoVO.getExtensao().equals(".jpg") || arquivoVO.getExtensao().equals(".pnj") || arquivoVO.getExtensao().equals(".png") || arquivoVO.getExtensao().equals(".jpeg") || arquivoVO.getExtensao().equals(".bmp") || arquivoVO.getExtensao().equals(".bitmap")) {
				    	setExibirBotaoConfirmaArquivo(Boolean.FALSE);
						setExibirBotao(Boolean.TRUE);				    	
					}else{
						setExibirBotaoConfirmaArquivo(Boolean.TRUE);
						setExibirBotao(Boolean.FALSE);
				}
			
				    
				    
				    
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			resizedImage.flush();						
			imagemOriginal.flush();
			uploadEvent = null;
		}
	}
	
	public void salvarArquivoBibliotecaExterna() throws Exception {
		
		try {	
			
			for (ArquivoVO objArquivo : getListaArquivosBibliotecaExterna()) {				
				if (objArquivo.getCodigo().equals(0)) {
					objArquivo.setCodigoCatalogo(getCatalogoVO().getCodigo());
					objArquivo.setCodOrigem(getCatalogoVO().getCodigo());
					objArquivo.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP);
					getFacadeFactory().getArquivoFacade().incluir(objArquivo, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				}
			}	
			setCaminhoarquivoAnexado(null);
			setMensagemID("msg_dados_gravados");				
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
	public void removerUploadArquivoTemp() throws Exception {
		
		try {
			String arquivoTemporario = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP.getValue() + File.separator + getCatalogoVO().getCodigo() + File.separator + arquivoVO.getNome();
			File arquivoExcluir = new File(arquivoTemporario);
			getArquivoHelper().deleteRecursivo(arquivoExcluir);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void buscarArquivos() {
		try {
			setListaArquivosBibliotecaExterna(getFacadeFactory().getArquivoFacade().consultarArquivosBibliotecaExterna(getCatalogoVO().getCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}	
	
	public void arquivoTemporario() {
		setCaminhoarquivoAnexado(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/arquivosBibliotecaExternaTMP/" + getArquivoVO().getNome());
		}
	
	public List getListaSelectItemAlterarexemplar() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("anopublicacao", "Ano Publicação"));
        itens.add(new SelectItem("dataaquisicao", "Data Aquisição"));
        itens.add(new SelectItem("datacompra", "Data Compra"));
        itens.add(new SelectItem("desconsiderarreserva", "Desconsiderar Reserva"));
        itens.add(new SelectItem("edicao", "Edição"));
        itens.add(new SelectItem("paraconsulta", "Exemplar Consulta"));
        itens.add(new SelectItem("emprestarsomentefinaldesemana", "Emprestimo Especial"));
        itens.add(new SelectItem("fasciculo", "Fascículo"));
        itens.add(new SelectItem("tipoentrada", "Forma Entrada"));
        itens.add(new SelectItem("link", "Link"));
        itens.add(new SelectItem("local", "Local"));
        itens.add(new SelectItem("secao", "Seção"));  
        itens.add(new SelectItem("subtitulo", "Subtítuto"));
        itens.add(new SelectItem("tituloexemplar", "Título"));
        itens.add(new SelectItem("volume", "Volume"));        
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
		setListasExemplarEditar(getFacadeFactory().getExemplarFacade().buscarExemplaresEdicao(getCatalogoVO().getCodigo() , getTipoAlterarColunaExemplar() , getValorConsultaAlteracao() , getValorConsultaAlteracaoData()));
		desmarcarTodosListaExemplares();
		
	}
	
	public boolean isCampoData() {
	    return getTipoAlterarColunaExemplar().equals("dataaquisicao") || getTipoAlterarColunaExemplar().equals("datacompra");
	}
	
	public boolean isCampoBoolean() {
	    return getTipoAlterarColunaExemplar().equals("paraconsulta") || getTipoAlterarColunaExemplar().equals("desconsiderarreserva") || getTipoAlterarColunaExemplar().equals("emprestarsomentefinaldesemana");
	}
	
	public boolean isCampoAno() {
	    return getTipoAlterarColunaExemplar().equals("anopublicacao");
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
	    		getTipoAlterarColunaExemplar().equals("fasciculo") ;
	}
	
		
	public void marcarTodosListaExemplares() throws Exception {
		getFacadeFactory().getExemplarFacade().marcarTodosListaExemplares(getListasExemplarEditar());	
	}

	public void desmarcarTodosListaExemplares() throws Exception {
		getFacadeFactory().getExemplarFacade().desmarcarTodosListaExemplares(getListasExemplarEditar());		
	}
	
	public String editarDadosExemplares() {
        try {            
        	getFacadeFactory().getExemplarFacade().editarDadosExemplares(getListasExemplarEditar() , getUsuarioLogado() , getValorAlteracao() ,getValorAlteracaoBoolean(),getValorAlteracaoData(),  getTipoAlterarColunaExemplar() , getCatalogoVO().getAssinaturaPeriodico());
        	getFacadeFactory().getCatalogoFacade().carregarDados(getCatalogoVO(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
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
	
	public boolean getApresentarResultadoConsulta() {
		if (this.getListasExemplarEditar() == null || this.getListasExemplarEditar().size() == 0) {
			return false;
		}
		return true;
	} 
	
	public void buscarExemplares() throws Exception {
		setListasExemplarEditar(getFacadeFactory().getExemplarFacade().buscarExemplaresEdicao(getCatalogoVO().getCodigo() , getTipoAlterarColunaExemplar() , getValorConsultaAlteracao() , getValorConsultaAlteracaoData()));
		desmarcarTodosListaExemplares();
	}
	
	public List getListaSelectItemTipoBoolean() {
        List itens = new ArrayList(0);
        
        itens.add(new SelectItem("", "Ambos"));
        itens.add(new SelectItem("true", "Marcado"));
        itens.add(new SelectItem("false", "Desmarcado"));        
        return itens;
    }
	
	public String getShowFotoCrop() {
		try {
			if (getCaminhoarquivoAnexado() == null) {
				return "resources/imagens/usuarioPadrao.jpg";
			}
			return getCaminhoarquivoAnexado()+"?UID="+new Date().getTime();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}
	public void renderizarUpload() {
		setExibirUpload(false);
	}
	
	public void recortarFoto() {
		try {
		String arquivoTemporario = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP.getValue() +  File.separator + arquivoVO.getNome();
		getArquivoVO().setNome(getFacadeFactory().getArquivoHelper().recortarImagem("arquivosBibliotecaExternaTMP",getArquivoVO().getNome(), getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY() , getUsuarioLogado()));
		File arquivoExcluir = new File(arquivoTemporario);
		getArquivoHelper().deleteRecursivo(arquivoExcluir);
		setCaminhoarquivoAnexado(null);
		
		} catch (Exception ex) {
			setOncompleteModal("RichFaces.$('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}
	
	public void confirmarArquivoAnexado(){
		setCaminhoarquivoAnexado(null);
		setExibirBotaoConfirmaArquivo(false);
	}
	
	public void realizarDownloadArquivo() throws CloneNotSupportedException {
		ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
		if(!arquivoVO.getPastaBaseArquivo().startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo())) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			if (cloneArquivo.getPastaBaseArquivo().endsWith("TMP")) {
				cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo());
					
			}else {
			cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo()+File.separator+getCatalogoVO().getCodigo());
			} context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);		
		}else {
			context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		}
	}
	
	

	public void realizarConsultaImagem() throws CloneNotSupportedException {
		ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
		if (arquivoVO.getPastaBaseArquivo().endsWith("TMP")) {
			setCaminhoarquivoAnexado(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo()+"/"+arquivoVO.getPastaBaseArquivo()+"/"+arquivoVO.getNome());
		}else {
		setCaminhoarquivoAnexado(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo()+"/"+arquivoVO.getPastaBaseArquivo()+"/"+getCatalogoVO().getCodigo()+"/"+arquivoVO.getNome());
		setArquivoVO(arquivoVO);
		}
	}
	
	public void selecionarArquivoRemover(){
		ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");		
		setArquivoVO(arquivoVO);
		
		
	}
	
	public void removerArquivo(){

		try {
			getFacadeFactory().getArquivoFacade().excluir(arquivoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			
			for(int i = 0; i < getListaArquivosBibliotecaExterna().size(); i++){	
				if (arquivoVO.getNome().equals(getListaArquivosBibliotecaExterna().get(i).getNome())) {
					getListaArquivosBibliotecaExterna().remove(i);
				}		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
				
	}
	
	public String getUrlLogoIndexApresentar() {
		if (!getCaminhoarquivoAnexado().equals(null)) {
			try {
				return getCaminhoarquivoAnexado();
						} catch (Exception e) {
				return "/resources/imagens/biblioteca/Fundo2.png";
			}
		}
		return "/resources/imagens/biblioteca/Fundo2.png";
	}
	public void novoObjAnexado() {
		setExibirBotao(Boolean.FALSE);
		setArquivoVO(new ArquivoVO());
		
	}
	public void clonar() throws Exception {
		
		List<ArquivoVO> listaArquivoClonar = getListaArquivosBibliotecaExterna();
		List<ExemplarVO> listasExemplarEditarClonar = getCatalogoVO().getExemplarVOs();
		setListaArquivosBibliotecaExterna(null);
		setListasExemplarEditar(null);
		getCatalogoVO().setNovoObj(true);
		getCatalogoVO().setCodigo(0);
		getCatalogoVO().setDataCadastro(null);
		getCatalogoVO().setTitulo(getCatalogoVO().getTitulo().concat("- Clone "));
		getCatalogoVO().setExemplarVOs(null);
		
		for (ExemplarVO ExemplarVOClonado : listasExemplarEditarClonar) {
			ExemplarVOClonado.setCodigoBarra(null);	
			ExemplarVOClonado.setCodigo(null);
			ExemplarVOClonado.setCatalogo(null);
			ExemplarVOClonado.setSituacaoAtual(null);
		    ExemplarVOClonado.getBiblioteca().setConfiguracaoBiblioteca(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPorBiblioteca(ExemplarVOClonado.getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getCatalogoVO().getExemplarVOs().add(ExemplarVOClonado);
		    
			
		}
		for (ArquivoVO arquivoVOClonado : listaArquivoClonar) {
			
			getFacadeFactory().getArquivoHelper().clonarUpLoadArquivo(arquivoVOClonado,arquivoVOClonado.getPastaBaseArquivo() ,getConfiguracaoGeralPadraoSistema());				   			    
			arquivoVOClonado.setCodOrigem(0);
			arquivoVOClonado.setCodigo(0);
			getCatalogoVO().setDataCadastro(null);
			getListaArquivosBibliotecaExterna().add(arquivoVOClonado);
		    
			
		}
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
	}
	
	public void limparDadosArquivos(){
		if (Uteis.isAtributoPreenchido(arquivoVO.getNome())) {
			File fileTmp = null;
			fileTmp = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator +PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP.getValue()+ File.separator +arquivoVO.getNome());		
			fileTmp.delete();		
			setCaminhoarquivoAnexado(null);
			setExibirBotao(false);
			setExibirBotaoConfirmaArquivo(false);
			for(int i = 0; i < getListaArquivosBibliotecaExterna().size(); i++){	
				if (arquivoVO.getNome().equals(getListaArquivosBibliotecaExterna().get(i).getNome())) {
					getListaArquivosBibliotecaExterna().remove(i);
	}
			}
		}
		
	}
	
	public Boolean getApresentarAnexoCapaContraCapa() {
		
		if (getTipoUploadApresentar().equals("Capa") || getTipoUploadApresentar().equals("Contra-Capa")) {
			return true;
		}else {
			return false;
		}
	}

	public Boolean getValidarPossuiIntegracaoEbsco() {
		if(validarPossuiIntegracaoEbsco == null  ) {
			validarPossuiIntegracaoEbsco = Boolean.TRUE;
		}
		return validarPossuiIntegracaoEbsco;
	}

	public void setValidarPossuiIntegracaoEbsco(Boolean validarPossuiIntegracaoEbsco) {
		this.validarPossuiIntegracaoEbsco = validarPossuiIntegracaoEbsco;
	}
	
	public void realizarValidacaoDisponibilizarExemplarConsulta() {
		try {
			ExemplarVO exemplarConsulta = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
			getFacadeFactory().getExemplarFacade().realizarValidacaoDisponibilizarExemplarConsulta(exemplarConsulta);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}	
	 public String getModalpanelExportarMarc21XML() {
		 if(modalpanelExportarMarc21XML == null ) {
			 modalpanelExportarMarc21XML = "";
		 }
		    return modalpanelExportarMarc21XML;
		    
	   }
	 
	 public void validarAbrirModalExportarArquivoMarc21() {		 
		 if(!getFacadeFactory().getCatalogoFacade().realizarVerificacaoPosssuiIntegracaoEbsco()) {
		    	executarExportarArquivoMarc21XML();		    	
		    	setModalPanelExportarMarc21XML("");
		    	
		    }else {
		    	setFazerDownloadMarc21(false);
		    	setModalPanelExportarMarc21XML("RichFaces.$('panelExportarMarc21XML').show()");
		    }  
	 }

	public void setModalPanelExportarMarc21XML(String modalpanelExportarMarc21XML) {
		this.modalpanelExportarMarc21XML = modalpanelExportarMarc21XML;

	}

	public ArquivoMarc21CatalogoVO getArquivoMarc21CatalogoVO() {
		return arquivoMarc21CatalogoVO;
	}

	public void setArquivoMarc21CatalogoVO(ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO) {
		this.arquivoMarc21CatalogoVO = arquivoMarc21CatalogoVO;
	}

	
	
	
}
