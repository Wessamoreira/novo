/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package relatorio.controle.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.SecaoVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.biblioteca.ExemplaresRelVO;

@Controller("ExemplaresRelControle")
@Scope("viewScope")
public class ExemplaresRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5438687004270721669L;
	private ExemplarVO exemplarVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemSecao;
	private List<SelectItem> listaSelectItemBiblioteca;
	private String campoConsultarCatalogo;
	private String valorConsultarCatalogo;
	private List listaConsultarCatalogo;
	private CursoVO curso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private DisciplinaVO disciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List listaConsultaDisciplina;
	private List<SelectItem> listaSelectItemAreaDeConhecimento;
	private String tipoCatalogoPeriodico;
	private String tipoLayout;
	private String tituloCatalogoExemplar;
	private List<SelectItem> listaSelectItemTipoCatalogo;
	private String tipoOrdenacaoRelatorio;
    private Date  dataInicioCompraExemplar;
    private Date  dataFimCompraExemplar;
    private Date  dataInicioAquisicaoExemplar;
    private Date  dataFimAquisicaoExemplar;
    private Boolean apresentarResponsavelEdataCadastro;
    private String responsavelCadastro;
    private Date  dataPeriodoCadastroInicio;
    private Date  dataPeriodoCadastroFim;
    private Boolean apresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro;
    private String responsavelUltimaAlteracao ;
    private Date dataPeriodoUltimaAlteracaoInicio;
    private Date dataPeriodoUltimaAlteracaoFim;
    private FuncionarioVO responsavelCriacaoVO;
    private FuncionarioVO responsavelAlteracaoVO ;
    private List<UsuarioVO> listaConsultaUsuario;
	private String valorConsultaResponsavel;     
	private String campoConsultaResponsavel;
	private DataModelo dataModeloResponsavelCatalogo;
	private Boolean considerarSubTiposCatalogo;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public ExemplaresRelControle() throws Exception {
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmrelatorio");
		getControleConsulta().setDataFim(null);
		getControleConsulta().setDataIni(null);
	}
	

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemSecao();
		montarListaSelectItemAreaDeConhecimento();
		montarListaSelectItemTipoCatalogo();
	}

	public void limparDados() {
		setCurso(new CursoVO());
		setDisciplina(new DisciplinaVO());
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public void imprimirPDF() {
		List<ExemplaresRelVO> listaObjetos = null;
		try {
			getFacadeFactory().getExemplaresRelFacade().validarDados(getExemplarVO(), getUnidadeEnsinoVO().getCodigo(), getTipoLayout(),
					getDataInicioCompraExemplar(), getDataFimCompraExemplar() , getDataInicioAquisicaoExemplar() ,
					getDataFimAquisicaoExemplar(),getApresentarResponsavelEdataCadastro() ,getResponsavelCriacaoVO().getPessoa().getNome(),getDataPeriodoCadastroInicio(),
					getDataPeriodoCadastroFim(),getApresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro(),getResponsavelAlteracaoVO().getPessoa().getNome(),
					getDataPeriodoUltimaAlteracaoInicio(),getDataPeriodoUltimaAlteracaoFim());
			if (getTipoLayout().equals("ANALITICO_PERIODICO")) {
				getExemplarVO().setTituloExemplar(getTituloCatalogoExemplar());
			} else {
				getExemplarVO().getCatalogo().setTitulo(getTituloCatalogoExemplar());
			}
			listaObjetos = getFacadeFactory().getExemplaresRelFacade().criarObjeto(getExemplarVO(), getUnidadeEnsinoVO().getCodigo(), getTipoCatalogoPeriodico(), getTipoLayout(),tipoOrdenacaoRelatorio, getDataInicioCompraExemplar(), getDataFimCompraExemplar() , getDataInicioAquisicaoExemplar() , getDataFimAquisicaoExemplar(),
					getResponsavelCriacaoVO(),getResponsavelAlteracaoVO(),getDataPeriodoCadastroInicio(),getDataPeriodoCadastroFim(),getDataPeriodoUltimaAlteracaoInicio(),getDataPeriodoUltimaAlteracaoFim(),getApresentarResponsavelEdataCadastro(),getApresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro(), getConsiderarSubTiposCatalogo());	
			if (!listaObjetos.isEmpty()) {
				if (getTipoLayout().equals("SINTETICO_TITULO")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getExemplaresRelFacade().designIReportRelatorio());
				} else if (getTipoLayout().equals("SINTETICO_ASSUNTO")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getExemplaresRelFacade().designIReportRelatorioExemplaresRelSinteticoPorAssunto());
				} else if (getTipoLayout().equals("ANALITICO_CATALOGO")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getExemplaresRelFacade().designIReportRelatorioExemplaresRelAnaliticoCatalogo());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getExemplaresRelFacade().designIReportRelatorioExemplaresRelAnaliticoPeriodico());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getExemplaresRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Exemplares");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getExemplaresRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				String biblioteca = "Todas";
				if (getExemplarVO().getBiblioteca().getCodigo() > 0) {
					biblioteca = (getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getExemplarVO().getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome();
				}
				getSuperParametroRelVO().setBiblioteca(biblioteca);
				biblioteca = null;

				if (getExemplarVO().getCatalogo().getAreaConhecimentoVO().getCodigo() > 0) {
					getSuperParametroRelVO().setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(getExemplarVO().getCatalogo().getAreaConhecimentoVO().getCodigo(), getUsuarioLogado()).getNome());
				} else {
					getSuperParametroRelVO().setAreaConhecimento("Todos");
				}
				if (getExemplarVO().getCatalogo().getClassificacaoInicial().equals(getExemplarVO().getCatalogo().getClassificacaoFinal())) {
					getExemplarVO().getCatalogo().setClassificacaoBibliografica(getExemplarVO().getCatalogo().getClassificacaoInicial());
				}
				getSuperParametroRelVO().setClassificacaoBibliografica(getExemplarVO().getCatalogo().getClassificacaoBibliografica().equals("") ? "Todas" : getExemplarVO().getCatalogo().getClassificacaoBibliografica());
				
				if (Uteis.isAtributoPreenchido(getExemplarVO().getCatalogo().getTipoCatalogo())) {
					TipoCatalogoVO tipoCatalogoVO = getFacadeFactory().getTipoCatalogoFacade().consultarPorChavePrimaria(getExemplarVO().getCatalogo().getTipoCatalogo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					if(!getConsiderarSubTiposCatalogo()) {
						getSuperParametroRelVO().adicionarParametro("tipoCatalogo", tipoCatalogoVO.getNome()); 
					}else {
						getSuperParametroRelVO().adicionarParametro("tipoCatalogo", tipoCatalogoVO.getNome()+" e Subdivisões"); 
					}
				} else {
					getSuperParametroRelVO().adicionarParametro("tipoCatalogo", "Todos");
				}
				
				getSuperParametroRelVO().setCatalogo(getTipoCatalogoPeriodico());
				getSuperParametroRelVO().adicionarParametro("layout", getTipoLayout());
				if (!getTituloCatalogoExemplar().equals("")) {
					getSuperParametroRelVO().adicionarParametro("titulo", getTituloCatalogoExemplar());
				} else {
					getSuperParametroRelVO().adicionarParametro("titulo", "Todos");
				}
				if (getExemplarVO().getSecao().getCodigo() > 0) {
					getSuperParametroRelVO().adicionarParametro("secao", getExemplarVO().getSecao().getNome());
				} else {
					getSuperParametroRelVO().adicionarParametro("secao", "Todas");
				}
				if(Uteis.isAtributoPreenchido(getExemplarVO().getTipoEntrada())){
					getSuperParametroRelVO().adicionarParametro("tipoEntrada", TipoEntradaAcervo.getEnum(getExemplarVO().getTipoEntrada()).getDescricao());
				}else{
					getSuperParametroRelVO().adicionarParametro("tipoEntrada", "Todos");
				}				
			     getSuperParametroRelVO().adicionarParametro("apresentarResponsaveldataUltimaAlteracao",getApresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro() == true ? getApresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro() : false );
				 getSuperParametroRelVO().adicionarParametro("apresentarResponsaveldata",getApresentarResponsavelEdataCadastro() == true ? getApresentarResponsavelEdataCadastro() : false);				
				 UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
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
				montarListaSelectItemSecao();
				montarListaSelectItemAreaDeConhecimento();
				montarListaSelectItemBiblioteca();
				montarListaSelectItemTipoCatalogo();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public void montarListaSelectItemSecao() {
		try {
			List<SecaoVO> secaoVOs = getFacadeFactory().getSecaoFacade().consultarSecaoNivelComboBox(false, getUsuarioLogado());
			getListaSelectItemSecao().clear();
			getListaSelectItemSecao().add(new SelectItem("", ""));
			for (SecaoVO secaoVO : secaoVOs) {
				getListaSelectItemSecao().add(new SelectItem(secaoVO.getCodigo(), secaoVO.getNome()));
			}

		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public void montarListaSelectItemAreaDeConhecimento() {
		try {
			setListaSelectItemAreaDeConhecimento(UtilSelectItem.getListaSelectItem(getFacadeFactory().getAreaConhecimentoFacade().consultarSecaoNivelComboBox(false, getUsuarioLogado()), "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);			
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
				if(!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
					setUnidadeEnsinoVO(obj);
					montarListaSelectItemBiblioteca(obj);
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

	public void montarListaSelectItemTipoCatalogo() {
		try {
			montarListaSelectItemTipoCatalogo("");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Tipo Catalago</code>.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTipoCatalogo(String valorConsulta) throws Exception {
		try {
			List<TipoCatalogoVO> resultadoConsulta = getFacadeFactory().getTipoCatalogoFacade().consultarPorNome(valorConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemTipoCatalogo(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Biblioteca</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Biblioteca</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemBiblioteca() {
		try {
			montarListaSelectItemBiblioteca(getUnidadeEnsinoVO());
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Biblioteca</code>.
	 */
	public void montarListaSelectItemBiblioteca(UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		List<BibliotecaVO> resultadoConsulta = null;
		Iterator<BibliotecaVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			getExemplarVO().getBiblioteca().setCodigo(0);
			while (i.hasNext()) {
				BibliotecaVO obj = (BibliotecaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
				if(Uteis.isAtributoPreenchido(getExemplarVO().getBiblioteca().getCodigo())) {
					getExemplarVO().getBiblioteca().setCodigo(obj.getCodigo());
				}
			}
			Uteis.liberarListaMemoria(resultadoConsulta);
			setListaSelectItemBiblioteca(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List<SelectItem> getTipoConsultarComboCatalogo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("titulo", "Título"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getTipoComboCatalogoPeriodico() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TODOS", "Todos"));
		itens.add(new SelectItem("CATALOGO", "Catálogo"));
		itens.add(new SelectItem("PERIODICO", "Periódico"));
		return itens;
	}

	public List<SelectItem> getTipoComboLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		// itens.add(new SelectItem("ANALITICO_PERIODICO",
		// "Analítico Periódico"));
		itens.add(new SelectItem("SINTETICO_ASSUNTO", "Sintético por Assunto"));
		itens.add(new SelectItem("SINTETICO_TITULO", "Sintético por Título"));
		itens.add(new SelectItem("ANALITICO_CATALOGO", "Analítico"));
		return itens;
	}

	public List<SelectItem> getTipoComboLayoutPeriodico() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("ANALITICO_PERIODICO", "Analítico Periódico"));
		itens.add(new SelectItem("SINTETICO_ASSUNTO", "Sintético por Assunto"));
		itens.add(new SelectItem("SINTETICO_TITULO", "Sintético por Título"));
		return itens;
	}

	public void consultarCatalogo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultarCatalogo().equals("codigo")) {
				if (getValorConsultarCatalogo().equals("")) {
					setValorConsultarCatalogo("0");
				}
				int valorInt = Integer.parseInt(getValorConsultarCatalogo());
				objs = getFacadeFactory().getCatalogoFacade().consultarPorCodigoTipoCatalogoAssinaturaPeriodica(new Integer(valorInt), getTipoCatalogoPeriodico(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			}
			if (getCampoConsultarCatalogo().equals("titulo")) {
				objs = getFacadeFactory().getCatalogoFacade().consultarPorTituloTipoCatalogoAssinaturaPeriodico(getValorConsultarCatalogo(), getTipoCatalogoPeriodico(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			}
			setListaConsultarCatalogo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarCatalogo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCampoCatalogoPeriodico() {
		getExemplarVO().setCatalogo(null);
	}

	public boolean getIsApresentarLayoutCatalago() {
		if (getTipoCatalogoPeriodico().equals("CATALOGO") || getTipoCatalogoPeriodico().equals("TODOS")) {
			return true;
		} else {
			// setTipoLayout("");
			return false;
		}
	}

	public boolean getIsApresentarCampoAssunto() {
		if (getTipoLayout().equals("SINTETICO_ASSUNTO")) {
			return true;
		} else {
			return false;
		}
	}

	public void selecionarCatalogo() throws Exception {
		try {
			CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
			this.getExemplarVO().getCatalogo().setCodigo(obj.getCodigo());
			this.getExemplarVO().getCatalogo().setTitulo(obj.getTitulo());
			Uteis.liberarListaMemoria(this.getListaConsultarCatalogo());
			this.setValorConsultarCatalogo(null);
			this.setCampoConsultarCatalogo(null);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCampoCatalogo() {
		this.getExemplarVO().getCatalogo().setCodigo(0);
		this.getExemplarVO().getCatalogo().setTitulo("");
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	// public List getTipoConsultarComboClassificacaoBibliografica() {
	// List itens = new ArrayList(0);
	// itens.add(new SelectItem("nome", "Nome"));
	// itens.add(new SelectItem("codigo", "Código"));
	// return itens;
	// }

	// public void consultarClassificacaoBibliografica() {
	// try {
	// List objs = new ArrayList(0);
	// if (getCampoConsultarClassificacaoBibliografica().equals("codigo")) {
	// if (getValorConsultarClassificacaoBibliografica().equals("")) {
	// setValorConsultarClassificacaoBibliografica("0");
	// }
	// int valorInt =
	// Integer.parseInt(getValorConsultarClassificacaoBibliografica());
	// objs =
	// getFacadeFactory().getClassificacaoBibliograficaFacade().consultarPorCodigo(new
	// Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
	// getUsuarioLogado());
	// }
	// if (getCampoConsultarClassificacaoBibliografica().equals("nome")) {
	// objs =
	// getFacadeFactory().getClassificacaoBibliograficaFacade().consultarPorNome(getValorConsultarClassificacaoBibliografica(),
	// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
	// getUsuarioLogado());
	// }
	// setListaConsultarClassificacaoBibliografica(objs);
	// setMensagemID("msg_dados_consultados");
	// } catch (Exception e) {
	// setListaConsultarClassificacaoBibliografica(new ArrayList(0));
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }

	// public void selecionarClassificacaoBibliografica() throws Exception {
	// try {
	// ClassificacaoBibliograficaVO obj = (ClassificacaoBibliograficaVO)
	// context().getExternalContext().getRequestMap().get("classificacaoBibliografica");
	// this.getExemplarVO().getCatalogo().setClassificacaoBibliografica(obj);
	// Uteis.liberarListaMemoria(this.getListaConsultarClassificacaoBibliografica());
	// this.setValorConsultarClassificacaoBibliografica(null);
	// this.setCampoConsultarClassificacaoBibliografica(null);
	// setMensagemID("msg_dados_adicionados");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }

	// public void limparCampoClassificacaoBibliografica() {
	// this.getExemplarVO().getCatalogo().setClassificacaoBibliografica(new
	// ClassificacaoBibliograficaVO());
	// }

	public ExemplarVO getExemplarVO() {
		if (exemplarVO == null) {
			exemplarVO = new ExemplarVO();
		}
		return exemplarVO;
	}

	public void setExemplarVO(ExemplarVO exemplarVO) {
		this.exemplarVO = exemplarVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemUnidadeEnsino);
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

	public String getCampoConsultarCatalogo() {
		return campoConsultarCatalogo;
	}

	public void setCampoConsultarCatalogo(String campoConsultarCatalogo) {
		this.campoConsultarCatalogo = campoConsultarCatalogo;
	}

	public List getListaConsultarCatalogo() {
		return listaConsultarCatalogo;
	}

	public void setListaConsultarCatalogo(List listaConsultarCatalogo) {
		this.listaConsultarCatalogo = listaConsultarCatalogo;
	}

	public String getValorConsultarCatalogo() {
		return valorConsultarCatalogo;
	}

	public void setValorConsultarCatalogo(String valorConsultarCatalogo) {
		this.valorConsultarCatalogo = valorConsultarCatalogo;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
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

	// public String getCampoConsultarClassificacaoBibliografica() {
	// return campoConsultarClassificacaoBibliografica;
	// }
	//
	// public void setCampoConsultarClassificacaoBibliografica(String
	// campoConsultarClassificacaoBibliografica) {
	// this.campoConsultarClassificacaoBibliografica =
	// campoConsultarClassificacaoBibliografica;
	// }
	//
	// public List getListaConsultarClassificacaoBibliografica() {
	// return listaConsultarClassificacaoBibliografica;
	// }
	//
	// public void setListaConsultarClassificacaoBibliografica(List
	// listaConsultarClassificacaoBibliografica) {
	// this.listaConsultarClassificacaoBibliografica =
	// listaConsultarClassificacaoBibliografica;
	// }
	//
	// public String getValorConsultarClassificacaoBibliografica() {
	// return valorConsultarClassificacaoBibliografica;
	// }
	//
	// public void setValorConsultarClassificacaoBibliografica(String
	// valorConsultarClassificacaoBibliografica) {
	// this.valorConsultarClassificacaoBibliografica =
	// valorConsultarClassificacaoBibliografica;
	// }

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigoEspecifico(valorInt, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), null);

			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeEpecifico(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), null);

				// objs =
				// getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(),
				// getEmprestimoFiltroRel().getUnidadeEnsinoVO().getCodigo(),
				// false,
				// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO cur = ((CursoVO) context().getExternalContext().getRequestMap().get("curso"));
			getCurso().setNome(cur.getNome());
			getCurso().setCodigo(cur.getCodigo());
			getListaConsultaCurso().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>();
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public void consultarDisciplina() {
		try {
			List objs = new ArrayList(0);
			getListaConsultaDisciplina().clear();
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoEspecifico(valorInt, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeEpecifico(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() {
		try {
			DisciplinaVO disc = ((DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina"));
			getDisciplina().setNome(disc.getNome());
			getDisciplina().setCodigo(disc.getCodigo());
			getListaConsultaDisciplina().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public List<SelectItem> getListaSelectItemSecao() {
		if (listaSelectItemSecao == null) {
			listaSelectItemSecao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSecao;
	}

	public void setListaSelectItemSecao(List<SelectItem> listaSelectItemSecao) {
		this.listaSelectItemSecao = listaSelectItemSecao;
	}

	public List<SelectItem> getListaSelectItemAreaDeConhecimento() {
		if (listaSelectItemAreaDeConhecimento == null) {
			listaSelectItemAreaDeConhecimento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAreaDeConhecimento;
	}

	public void setListaSelectItemAreaDeConhecimento(List<SelectItem> listaSelectItemAreaDeConhecimento) {
		this.listaSelectItemAreaDeConhecimento = listaSelectItemAreaDeConhecimento;
	}

	public String getTipoCatalogoPeriodico() {
		if (tipoCatalogoPeriodico == null) {
			tipoCatalogoPeriodico = "TODOS";
		}
		return tipoCatalogoPeriodico;
	}

	public void setTipoCatalogoPeriodico(String tipoCatalogoPeriodico) {
		this.tipoCatalogoPeriodico = tipoCatalogoPeriodico;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getTituloCatalogoExemplar() {
		if (tituloCatalogoExemplar == null) {
			tituloCatalogoExemplar = "";
		}
		return tituloCatalogoExemplar;
	}

	public void setTituloCatalogoExemplar(String tituloCatalogoExemplar) {
		this.tituloCatalogoExemplar = tituloCatalogoExemplar;
	}

	public List<SelectItem> getListaSelectItemTipoCatalogo() {
		if (listaSelectItemTipoCatalogo == null) {
			listaSelectItemTipoCatalogo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoCatalogo;
	}

	public void setListaSelectItemTipoCatalogo(List<SelectItem> listaSelectItemTipoCatalogo) {
		this.listaSelectItemTipoCatalogo = listaSelectItemTipoCatalogo;
	}
    
	
	public List<SelectItem> getTipoComboOrdenacaoRelatorio() {
		switch (getTipoLayout()) {
		case "SINTETICO_ASSUNTO":
			return tipoComboOrdenacaoLayoutSinteticoPorAssunto();
		case "SINTETICO_TITULO":
			return tipoComboOrdenacaoLayoutSinteticoPorTitulo();
		case "ANALITICO_CATALOGO":
			return tipoComboOrdenacaoLayoutAnalitico();
		case "ANALITICO_PERIODICO":
			return tipoComboOrdenacaoLayoutAnaliticoPeriodico();
		default:
			return tipoComboOrdenacaoPadrao();
		}
	}

	public String getTipoOrdenacaoRelatorio() {
		if(tipoOrdenacaoRelatorio == null){
			tipoOrdenacaoRelatorio = "";
		}
		 return tipoOrdenacaoRelatorio;
	}

	public void setTipoOrdenacaoRelatorio(String tipoOrdenacaoRelatorio) {
		this.tipoOrdenacaoRelatorio = tipoOrdenacaoRelatorio;
	}
	
	public List<SelectItem> tipoComboOrdenacaoLayoutSinteticoPorTitulo(){
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TITULO", "Título"));
		itens.add(new SelectItem("SUBTITULO", "Subtítulo"));
		itens.add(new SelectItem("EDICAO_TITULO", "Edição"));
		itens.add(new SelectItem("AUTORES_TITULO", "Autores"));
		itens.add(new SelectItem("EDITORA_TITULO", "Editora"));
		itens.add(new SelectItem("ANOPUBLICACAO_TITULO", "Ano Publicação"));
		itens.add(new SelectItem("CLASSIFICACAO_TITULO", "Classificação"));
		return itens;
	}
	 
	public List<SelectItem> tipoComboOrdenacaoLayoutSinteticoPorAssunto () {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("ASSUNTO_CLASSIFICACAO", "Assunto"));
		itens.add(new SelectItem("TITULO_CLASSIFICACAO", "Título"));
		itens.add(new SelectItem("CLASSIFICACAO_ASSUNTO", "Classificação"));
		itens.add(new SelectItem("CUTTERPHA_ASSUNTO", "Cutter/Pha"));
		itens.add(new SelectItem("AUTORES_ASSUNTO", "Autores"));
		itens.add(new SelectItem("EDITORA_ASSUNTO", "Editora"));
		return itens;
	}
	
	public List<SelectItem> tipoComboOrdenacaoLayoutAnalitico () {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TITULO", "Título"));
		itens.add(new SelectItem("CLASSIFICACAO_TITULO", "Classificação"));
		itens.add(new SelectItem("CUTTERPHA_TITULO", "Cutter/Pha"));
		itens.add(new SelectItem("EDITORA_TITULO", "Editora"));
		itens.add(new SelectItem("AUTORES_TITULO", "Autores"));
		return itens;
	}
	
	public List<SelectItem> tipoComboOrdenacaoLayoutAnaliticoPeriodico () {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TITULO_CLASSIFICACAO_TOMBO", "Título"));
		itens.add(new SelectItem("CLASSIFICACAO_TITULO_TOMBO", "Classificação"));
		itens.add(new SelectItem("TOMBO_EDITORA_TITULO", "Tombo"));
		itens.add(new SelectItem("SECAO_TITULO", "Seção"));
		return itens;
	}
	
	public List<SelectItem> tipoComboOrdenacaoPadrao () {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TITULO", "Título"));
		return itens;
	}
	
	public Date getDataInicioCompraExemplar() {
		return dataInicioCompraExemplar;
	}

	public void setDataInicioCompraExemplar(Date dataInicioCompraExemplar) {
		this.dataInicioCompraExemplar = dataInicioCompraExemplar;
	}

	public Date getDataFimCompraExemplar() {
		return dataFimCompraExemplar;
	}

	public void setDataFimCompraExemplar(Date dataFimCompraExemplar) {
		this.dataFimCompraExemplar = dataFimCompraExemplar;
	}	
     
	 public Date getDataInicioAquisicaoExemplar() {
		return dataInicioAquisicaoExemplar;
	}

	public void setDataInicioAquisicaoExemplar(Date dataInicioAquisicaoExemplar) {
		this.dataInicioAquisicaoExemplar = dataInicioAquisicaoExemplar;
	}

	public Date getDataFimAquisicaoExemplar() {
		return dataFimAquisicaoExemplar;
	}

	public void setDataFimAquisicaoExemplar(Date dataFimAquisicaoExemplar) {
		this.dataFimAquisicaoExemplar = dataFimAquisicaoExemplar;
	}

	public List getListaSelectItemTipoEntrada() {
		 List<SelectItem> itens = new ArrayList<SelectItem>(0);
		 itens.add(new SelectItem("", ""));
		 itens.addAll(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoEntradaAcervo.class,false));
	    return  itens;
	 }

	public Boolean getApresentarResponsavelEdataCadastro() {
		if(apresentarResponsavelEdataCadastro == null) {
			apresentarResponsavelEdataCadastro = Boolean.FALSE;
		}
		return apresentarResponsavelEdataCadastro;
	}

	public void setApresentarResponsavelEdataCadastro(Boolean apresentarResponsavelEdataCadastro) {
		this.apresentarResponsavelEdataCadastro = apresentarResponsavelEdataCadastro;
	}

	public String getResponsavelCadastro() {
		if(responsavelCadastro == null) {			
			responsavelCadastro = "";
		}
		return responsavelCadastro;
	}

	public void setResponsavelCadastro(String responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}

	public Date getDataPeriodoCadastroInicio() {
		return dataPeriodoCadastroInicio;
	}

	public void setDataPeriodoCadastroInicio(Date dataPeriodoCadastroInicio) {
		this.dataPeriodoCadastroInicio = dataPeriodoCadastroInicio;
	}

	public Date getDataPeriodoCadastroFim() {
		return dataPeriodoCadastroFim;
	}

	public void setDataPeriodoCadastroFim(Date dataPeriodoCadastroFim) {
		this.dataPeriodoCadastroFim = dataPeriodoCadastroFim;
	}

	public Boolean getApresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro() {
		if(apresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro == null) {
			apresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro = Boolean.FALSE;
		}
		return apresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro;
	}

	public void setApresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro(
			Boolean apresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro) {
		this.apresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro = apresentarResponsavelAlteracaoEdataUltimaAlteracaoCadastro;
	}

	public String getResponsavelUltimaAlteracao() {
		if(responsavelUltimaAlteracao == null) {
			responsavelUltimaAlteracao = "";
		}
		return responsavelUltimaAlteracao;
	}

	public void setResponsavelUltimaAlteracao(String responsavelUltimaAlteracao) {
		this.responsavelUltimaAlteracao = responsavelUltimaAlteracao;
	}

	public Date getDataPeriodoUltimaAlteracaoInicio() {
		return dataPeriodoUltimaAlteracaoInicio;
	}

	public void setDataPeriodoUltimaAlteracaoInicio(Date dataPeriodoUltimaAlteracaoInicio) {
		this.dataPeriodoUltimaAlteracaoInicio = dataPeriodoUltimaAlteracaoInicio;
	}

	public Date getDataPeriodoUltimaAlteracaoFim() {
		return dataPeriodoUltimaAlteracaoFim;
	}

	public void setDataPeriodoUltimaAlteracaoFim(Date dataPeriodoUltimaAlteracaoFim) {
		this.dataPeriodoUltimaAlteracaoFim = dataPeriodoUltimaAlteracaoFim;
	}	
	public void selecionarResponsavelCriacao() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("responsavelCriacao");
		setResponsavelCriacaoVO(obj);
		limparFormularioConsulta();
	}
	
	public void consultarFuncionarioCriacao() throws Exception {
		try {FuncionarioVO obj = new FuncionarioVO();
		     obj.setPessoa(consultarResponsavelPorMatricula(getResponsavelAlteracaoVO().getMatricula()));	
			setResponsavelCriacaoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void selecionarResponsavelAlteracao() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("responsavelAlteracao");
		setResponsavelAlteracaoVO(obj);
		limparFormularioConsulta();
		
	}
	
	public void consultarFuncionarioAlteracao() throws Exception {
		try {		
			FuncionarioVO obj = new FuncionarioVO();
			 obj.setPessoa(consultarResponsavelPorMatricula(getResponsavelAlteracaoVO().getMatricula()));		
			setResponsavelAlteracaoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public PessoaVO consultarResponsavelPorMatricula(String matricula) throws Exception {
		PessoaVO responsavelVO = null;
		try {
			responsavelVO = getFacadeFactory().getPessoaFacade().consultarPorMatricula(matricula);
			if (Uteis.isAtributoPreenchido(responsavelVO)) {
				return responsavelVO;
			} else {
				setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new PessoaVO();
	}

	public FuncionarioVO getResponsavelCriacaoVO() {
		if(responsavelCriacaoVO == null ) {
			responsavelCriacaoVO =  new FuncionarioVO();
		}
		return responsavelCriacaoVO;
	}

	public void setResponsavelCriacaoVO(FuncionarioVO responsavelCriacaoVO) {
		this.responsavelCriacaoVO = responsavelCriacaoVO;
	}
	public FuncionarioVO getResponsavelAlteracaoVO() {
		if(responsavelAlteracaoVO == null ) {
			responsavelAlteracaoVO =  new FuncionarioVO();
		}
		return responsavelAlteracaoVO;
	}

	public void setResponsavelAlteracaoVO(FuncionarioVO responsavelAlteracaoVO) {
		this.responsavelAlteracaoVO = responsavelAlteracaoVO;
	}
	public void limparCampoResponsavelCriacao(){
		removerObjetoMemoria(getResponsavelCriacaoVO());
		removerObjetoMemoria(getDataPeriodoCadastroInicio());
		removerObjetoMemoria(getDataPeriodoCadastroFim());
	}
	public void limparCampoResponsavelUltimaAlteracao(){
		removerObjetoMemoria(getResponsavelAlteracaoVO());
		removerObjetoMemoria(getDataPeriodoUltimaAlteracaoInicio());
		removerObjetoMemoria(getDataPeriodoUltimaAlteracaoFim());
	}	
	
	public List<UsuarioVO> getListaConsultaUsuario() {
		if (listaConsultaUsuario == null) {
			listaConsultaUsuario = new ArrayList<>();
		}
		return listaConsultaUsuario;
	}

	public void setListaConsultaUsuario(List<UsuarioVO> listaConsultaUsuario) {
		this.listaConsultaUsuario = listaConsultaUsuario;
	}

	
    public void limparFormularioConsulta() {    	
    	removerObjetoMemoria(getDataModeloResponsavelCatalogo());
    }
	
	public List<SelectItem> getTipoConsultaComboResponsavel() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Codigo"));
		
		return itens;
	}

	
	
	

	private void preencherDadosParaConsulta(DataModelo dataModelo) {
		if (dataModelo == null) {
			dataModelo = new DataModelo();
		}
		dataModelo.setCampoConsulta(getCampoConsultaResponsavel());
		dataModelo.setValorConsulta(getValorConsultaResponsavel());
		
	}
	
	public void  scrollerListenerResponsavelCatalogo(DataScrollEvent dataScrollerEvent) {
		getDataModeloResponsavelCatalogo().setPaginaAtual(dataScrollerEvent.getPage());
		getDataModeloResponsavelCatalogo().setPage(dataScrollerEvent.getPage());
		this.consultarDadosResponsavelCatalogo();
	}
	
	public void consultarDadosResponsavelCatalogo() {
		try {
			final String USUARIOCRIACAO  ="USUARIOCRIACAO" ;
			preencherDadosParaConsulta(getDataModeloResponsavelCatalogo());
			getFacadeFactory().getExemplaresRelFacade().consultarPorEnumCampoConsulta(dataModeloResponsavelCatalogo,USUARIOCRIACAO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarDadosResponsavelAlteracaoCatalogo() {
		try {
			final String USUARIOALTERACAO  ="USUARIOALTERACAO" ;
			preencherDadosParaConsulta(getDataModeloResponsavelCatalogo());
			getFacadeFactory().getExemplaresRelFacade().consultarPorEnumCampoConsulta(dataModeloResponsavelCatalogo,USUARIOALTERACAO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getCampoConsultaResponsavel() {
		if(campoConsultaResponsavel == null) {
			campoConsultaResponsavel ="";
		}
		return campoConsultaResponsavel;
	}

	public void setCampoConsultaResponsavel(String campoConsultaResponsavel) {
		this.campoConsultaResponsavel = campoConsultaResponsavel;
	}

	

	public DataModelo getDataModeloResponsavelCatalogo() {
		if(dataModeloResponsavelCatalogo == null) {
			dataModeloResponsavelCatalogo = new DataModelo();
		}
		return dataModeloResponsavelCatalogo;
	}

	public void setDataModeloResponsavelCatalogo(DataModelo dataModeloResponsavelCatalogo) {
		this.dataModeloResponsavelCatalogo = dataModeloResponsavelCatalogo;
	}

	public String getValorConsultaResponsavel() {
		if(valorConsultaResponsavel == null) {
			valorConsultaResponsavel = "";
		}
		return valorConsultaResponsavel;
	}

	public void setValorConsultaResponsavel(String valorConsultaResponsavel) {
		this.valorConsultaResponsavel = valorConsultaResponsavel;
	}
	
	public Boolean getApresentarFiltroSubTiposCatalogo() {
		if(getExemplarVO().getCatalogo().getTipoCatalogo() != null 
				&& getExemplarVO().getCatalogo().getTipoCatalogo().getCodigo() != null 
				&& getExemplarVO().getCatalogo().getTipoCatalogo().getCodigo() != 0) {
			return true;
		}
		return false;
	}
	
	public Boolean getConsiderarSubTiposCatalogo() {
		return considerarSubTiposCatalogo;
	}

	public void setConsiderarSubTiposCatalogo(Boolean considerarSubTiposCatalogo) {
		this.considerarSubTiposCatalogo = considerarSubTiposCatalogo;
	}
}
