package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.DisponibilidadeHorarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.biblioteca.AssinaturaPeriodicoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.enumeradores.TipoRelatorioEtiquetaEnum;
import negocio.comuns.utilitarias.Uteis;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("EtiquetaLivroRelControle")
@Scope("viewScope")
@Lazy
public class EtiquetaLivroRelControle extends SuperControleRelatorio {

	private List listaSelectItemUnidadeEnsino;
	private BibliotecaVO bibliotecaVO;
	private CatalogoVO catalogo;
	AssinaturaPeriodicoVO assinaturaPeriodicoVO;
	private String campoConsultarCatalogo;
	private String valorConsultarCatalogo;
	private List listaConsultarCatalogo;
	private String codigoBarraExemplarInicial;
	private String codigoBarraExemplarFinal;
	private List listaSelectItemBiblioteca;
	// private TipoRelatorioEtiquetaEnum tipoRelatorioEtiqueta;
	private ExemplarVO exemplarVO;
	private String campoConsultarExemplar;
	private String valorConsultarExemplar;
	private List<ExemplarVO> listaConsultarExemplar;
	public Boolean fecharModalExemplar;
	private Integer tipoEtiqueta;
	private Boolean exibirCatalogo;
	private String campoConsultarPeriodico;
	private String valorConsultarPeriodico;
	private List<AssinaturaPeriodicoVO> listaConsultarPeriodico;
	private List<ExemplarVO> listaExemplarVOs;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Boolean removerEspacoTAGVazia;
	private List<SelectItem> listaSelectItemOrdenacao;
	private String ordenacao;
	private String codigoBarraExemplar;

	public EtiquetaLivroRelControle() throws Exception {
		setMensagemID("msg_entre_prmrelatorio");
		inicializarListasSelectItemTodosComboBox();
		// verificarLayoutPadrao();
		setExibirCatalogo(true);

		CatalogoVO catalogo = (CatalogoVO) context().getExternalContext().getSessionMap().get("Catalogo");
		if (catalogo != null) {
			setExibirCatalogo(true);
			setCatalogo(catalogo);
			setTipoEtiqueta(0);
			setListaExemplarVOs(getCatalogo().getExemplarVOs());
			context().getExternalContext().getSessionMap().remove("Catalogo");
		}

		CatalogoVO catalogoPeriodico = (CatalogoVO) context().getExternalContext().getSessionMap().get("Periodico");
		if (catalogoPeriodico != null) {
			setExibirCatalogo(false);
			setCatalogo(catalogoPeriodico);
			setTipoEtiqueta(1);
			setListaExemplarVOs(catalogoPeriodico.getExemplarVOs());
			context().getExternalContext().getSessionMap().remove("Periodico");
		}
	}

	private Integer numeroCopias;
	private Integer coluna;
	private Integer linha;
	private LayoutEtiquetaVO layoutEtiquetaVO;
	private List<SelectItem> listaSelectItemlayoutEtiqueta;
	private List<SelectItem> listaSelectItemColuna;
	private List<SelectItem> listaSelectItemLinha;

	public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
		if (listaSelectItemlayoutEtiqueta == null) {
			listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
			try {
				List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade()
						.consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.BIBLIOTECA,
								getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
				for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
					listaSelectItemlayoutEtiqueta
							.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));

				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemlayoutEtiqueta;
	}

	public void selecionaTipoEtiqueta() {
		if (getTipoEtiqueta() == 0) {
			setExibirCatalogo(true);
		} else {
			setExibirCatalogo(false);
		}
	}

	public List getComboboxTipoEtiqueta() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem(0, "Catalogo"));
		itens.add(new SelectItem(1, "Periodico"));
		return itens;
	}

	public List getTipoConsultarComboPeriodico() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
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

	public void inicializarDadosLayoutEtiqueta() {
		try {
			getListaSelectItemColuna().clear();
			getListaSelectItemLinha().clear();
			if (getLayoutEtiquetaVO().getCodigo() > 0) {
				setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(
						getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
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

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void imprimirPDF() {
		try {
			super.setFazerDownload(false);
			setCaminhoRelatorio("");
			registrarAtividadeUsuario(getUsuarioLogado(), "EtiquetaLivroRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");
			setBibliotecaVO(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getBibliotecaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFacadeFactory().getEtiquetaLivroRelFacade().validarDados(getCatalogo(), getBibliotecaVO().getCodigo(), getCodigoBarraExemplarInicial().trim(), getCodigoBarraExemplarFinal().trim(), getUnidadeEnsinoVO().getCodigo(), getLayoutEtiquetaVO(), getListaExemplarVOs());
			setCaminhoRelatorio(getFacadeFactory().getEtiquetaLivroRelFacade().realizarImpressaoEtiquetaBiblioteca(getLayoutEtiquetaVO(), getCatalogo(), getUnidadeEnsinoVO().getCodigo(), getBibliotecaVO().getCodigo(), getListaExemplarVOs(), getCodigoBarraExemplarInicial().trim(), getCodigoBarraExemplarFinal().trim(), getNumeroCopias(), getLinha(), getColuna(), getRemoverEspacoTAGVazia(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), getOrdenacao(), getBibliotecaVO().getNome()));
			super.setFazerDownload(true);
			persistirLayoutPadrao(TipoRelatorioEtiquetaEnum.PERSONALIZADO.toString());
			setMensagemID("msg_relatorio_ok");
			registrarAtividadeUsuario(getUsuarioLogado(), "EtiquetaLivroRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
			removerObjetoMemoria(this);
			setExibirCatalogo(true);
			setTipoEtiqueta(0);
			getCatalogo().getExemplarVOs().clear();
			getListaExemplarVOs().clear();
			setExibirCatalogo(true);
			setTipoEtiqueta(0);
			montarListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//// System.out.println("MENSAGEM => " + e.getMessage());

		}
	}

	// private void verificarLayoutPadrao() throws Exception {
	// LayoutPadraoVO layoutPadraoVO =
	// getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("etiquetaLivro",
	// "designRelatorio", false, getUsuarioLogado());
	// ;
	// if (!layoutPadraoVO.getValor().equals("")) {
	// setTipoRelatorioEtiqueta(TipoRelatorioEtiquetaEnum.valueOf(layoutPadraoVO.getValor()));
	// }
	// }

	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "etiquetaLivro", "designRelatorio",
				getUsuarioLogado());
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm,
					this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);			
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
				if(!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
					getUnidadeEnsinoVO().setCodigo(obj.getCodigo());
					montarListaSelectItemBiblioteca();
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

	public void montarListaSelectItemBiblioteca() {
		try {
			montarListaSelectItemBiblioteca(getUnidadeEnsinoVO());
		} catch (Exception e) {
			//// System.out.println("MENSAGEM => " + e.getMessage());

		}
	}

	public List getTipoConsultarComboCatalogo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("titulo", "Título"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("autor", "Autor"));
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
				objs = getFacadeFactory().getCatalogoFacade().consultarPorCodigo(new Integer(valorInt), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getBibliotecaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			}
			if (getCampoConsultarCatalogo().equals("titulo")) {
				objs = getFacadeFactory().getCatalogoFacade().consultarPorTitulo(getValorConsultarCatalogo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getBibliotecaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), 0, getUsuarioLogado());
			}
			if (getCampoConsultarCatalogo().equals("autor")) {
				objs = getFacadeFactory().getCatalogoFacade().consultarPorNomeAutor(getValorConsultarCatalogo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getBibliotecaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			}
			setListaConsultarCatalogo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarCatalogo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarPeriodico() {
		try {
			List objs = new ArrayList(0);

			if (getCampoConsultarPeriodico().equals("nome")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTituloCatalogo(
						getValorConsultarPeriodico(), "", null, null, false, true, getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			}
			setListaConsultarPeriodico(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarPeriodico(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCatalogo() throws Exception {
		CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogoItens");
		if (getMensagemDetalhada().equals("")) {
			setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCodigo(),
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
		}
		Uteis.liberarListaMemoria(this.getListaConsultarCatalogo());
		this.setValorConsultarCatalogo(null);
		this.setCampoConsultarCatalogo(null);
	}

	public void selecionarPeriodico() throws Exception {
		CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("periodicoItens");

		if (getMensagemDetalhada().equals("")) {
			setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCodigo(),
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
		}
		Uteis.liberarListaMemoria(this.getListaConsultarPeriodico());
		this.setValorConsultarPeriodico(null);
		this.setCampoConsultarPeriodico(null);
	}

	public void montarListaSelectItemBiblioteca(UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		List<BibliotecaVO> resultadoConsulta = null;
		Iterator<BibliotecaVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(
					unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			setBibliotecaVO(null);
			while (i.hasNext()) {
				BibliotecaVO obj = (BibliotecaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
				if(!Uteis.isAtributoPreenchido(getBibliotecaVO())) {
					setBibliotecaVO(obj);
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

	public Boolean getIntervaloExemplaresInformado() {
		if (Uteis.isAtributoPreenchido(getCodigoBarraExemplarInicial()) || Uteis.isAtributoPreenchido(getCodigoBarraExemplarFinal())) {
			return true;
		}
		return false;
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

	public void limparCampoCatalogo() {
		this.setCatalogo(new CatalogoVO());
	}

	public void limparCampoPeriodico() {
		this.setAssinaturaPeriodicoVO(new AssinaturaPeriodicoVO());
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

	public List getListaConsultarCatalogo() {
		return listaConsultarCatalogo;
	}

	public void setListaConsultarCatalogo(List listaConsultarCatalogo) {
		this.listaConsultarCatalogo = listaConsultarCatalogo;
	}

	public CatalogoVO getCatalogo() {
		if (catalogo == null) {
			catalogo = new CatalogoVO();
		}
		return catalogo;
	}

	public void setCatalogo(CatalogoVO catalogo) {
		this.catalogo = catalogo;
	}

	public BibliotecaVO getBibliotecaVO() {
		if (bibliotecaVO == null) {
			bibliotecaVO = new BibliotecaVO();
		}
		return bibliotecaVO;
	}

	public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
		this.bibliotecaVO = bibliotecaVO;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	// public TipoRelatorioEtiquetaEnum getTipoRelatorioEtiqueta() {
	// if (tipoRelatorioEtiqueta == null) {
	// tipoRelatorioEtiqueta = TipoRelatorioEtiquetaEnum.ETIQUETA_EXTERNA;
	// }
	// return tipoRelatorioEtiqueta;
	// }
	//
	// public void setTipoRelatorioEtiqueta(TipoRelatorioEtiquetaEnum
	// tipoRelatorioEtiqueta) {
	// this.tipoRelatorioEtiqueta = tipoRelatorioEtiqueta;
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

	public String getCampoConsultarExemplar() {
		if (campoConsultarExemplar == null) {
			campoConsultarExemplar = "";
		}
		return campoConsultarExemplar;
	}

	public void setCampoConsultarExemplar(String campoConsultarExemplar) {
		this.campoConsultarExemplar = campoConsultarExemplar;
	}

	public String getValorConsultarExemplar() {
		if (valorConsultarExemplar == null) {
			valorConsultarExemplar = "";
		}
		return valorConsultarExemplar;
	}

	public void setValorConsultarExemplar(String valorConsultarExemplar) {
		this.valorConsultarExemplar = valorConsultarExemplar;
	}

	public List<ExemplarVO> getListaConsultarExemplar() {
		if (listaConsultarExemplar == null) {
			listaConsultarExemplar = new ArrayList<ExemplarVO>(0);
		}
		return listaConsultarExemplar;
	}

	public void setListaConsultarExemplar(List<ExemplarVO> listaConsultarExemplar) {
		this.listaConsultarExemplar = listaConsultarExemplar;
	}

	public List getTipoConsultarComboExemplar() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigoBarra", "Código de Barras"));
		itens.add(new SelectItem("tituloCatalogo", "Título Catálogo"));
		// itens.add(new SelectItem("catalogo", "Catálogo"));
		return itens;
	}

	public void consultarExemplar() {
		try {
			List objs = new ArrayList(0);
			validarDadosBiblioteca(getBibliotecaVO().getCodigo());
			if (getCampoConsultarExemplar().equals("codigoBarra")) {
				objs = getFacadeFactory().getExemplarFacade().consultaRapidaPorCodigoBarraCatalogo(valorConsultarExemplar, getUnidadeEnsinoLogado().getCodigo(), getCatalogo().getCodigo(), getBibliotecaVO().getCodigo(), null, null, false, getUsuarioLogado());
			}
			if (getCampoConsultarExemplar().equals("tituloCatalogo")) {
				objs = getFacadeFactory().getExemplarFacade().consultaRapidaPorTituloCatalogo(valorConsultarExemplar, getUnidadeEnsinoLogado().getCodigo(), getBibliotecaVO().getCodigo(), null, null, false, getUsuarioLogado());
			}
			setListaConsultarExemplar(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarExemplar(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosBiblioteca(Integer biblioteca) throws Exception {
		if (getUnidadeEnsinoVO().getCodigo().equals(0)) {
			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		if (biblioteca.equals(0)) {
			throw new Exception("O campo BIBLIOTECA deve ser informado para a consulta do exemplar.");
		}
	}

	public void preencherTodosListaExemplar() {
		getFacadeFactory().getEtiquetaLivroRelFacade().preencherTodosListaExemplar(getListaConsultarExemplar());
	}

	public void desmarcarTodosListaExemplar() {
		getFacadeFactory().getEtiquetaLivroRelFacade().desmarcarTodosListaExemplar(getListaConsultarExemplar());
	}

	public void validarDadosExemplarSelecionado() throws Exception {
		boolean exemplarSelecionado = false;
		for (ExemplarVO exemplarVO : getListaConsultarExemplar()) {
			if (exemplarVO.getExemplarSelecionado()) {
				exemplarSelecionado = true;
			}
		}
		if (exemplarSelecionado) {
			setFecharModalExemplar(Boolean.TRUE);
		} else {
			setFecharModalExemplar(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", "Ao menos um Exemplar deve ser selecionado.");
		}
	}

	public Boolean getFecharModalExemplar() {
		if (fecharModalExemplar == null) {
			fecharModalExemplar = Boolean.FALSE;
		}
		return fecharModalExemplar;
	}

	public void setFecharModalExemplar(Boolean fecharModalExemplar) {
		this.fecharModalExemplar = fecharModalExemplar;
	}

	public String getIsFecharModalExemplar() {
		if (getFecharModalExemplar()) {
			return "RichFaces.$('panelExemplar').hide()";
		}
		return "";
	}

	public void adicionarExemplarSelecionado() throws Exception {
		try {
			validarDadosExemplarSelecionado();
			for (ExemplarVO exemplarVO : getListaConsultarExemplar()) {
				if (exemplarVO.getExemplarSelecionado()) {
					Iterator i = getListaExemplarVOs().iterator();
					int index = 0;
					while (i.hasNext()) {
						ExemplarVO objExistente = (ExemplarVO) i.next();
						if (exemplarVO.getCodigoBarra().equals(objExistente.getCodigoBarra())) {
							// getListaExemplarVOs().set(index, exemplarVO);
							// getCatalogo().getExemplarVOs().set(index,
							// exemplarVO);
							return;
						}
						index++;
					}
					getListaExemplarVOs().add(exemplarVO);
					getCatalogo().getExemplarVOs().add(exemplarVO);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			getListaConsultarExemplar().clear();
			incluirItemExemplarAdicionarListaSelectItemOrdenacao();
		}
	}

	public void removerExemplarVOs() throws Exception {
		try {
			ExemplarVO exemplarVO = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplaresItens");
			for (Iterator<ExemplarVO> iterator = getListaExemplarVOs().iterator(); iterator.hasNext();) {
				ExemplarVO exemplar = (ExemplarVO) iterator.next();
				if (exemplarVO.getCodigoBarra().equals(exemplar.getCodigoBarra())) {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public AssinaturaPeriodicoVO getAssinaturaPeriodicoVO() {
		if (assinaturaPeriodicoVO == null) {
			assinaturaPeriodicoVO = new AssinaturaPeriodicoVO();
		}
		return assinaturaPeriodicoVO;
	}

	public void setAssinaturaPeriodicoVO(AssinaturaPeriodicoVO assinaturaPeriodicoVO) {
		this.assinaturaPeriodicoVO = assinaturaPeriodicoVO;
	}

	public Integer getTipoEtiqueta() {
		if (tipoEtiqueta == null) {
			tipoEtiqueta = 0;
		}
		return tipoEtiqueta;
	}

	public void setTipoEtiqueta(Integer tipoEtiqueta) {
		this.tipoEtiqueta = tipoEtiqueta;
	}

	public Boolean getExibirCatalogo() {
		if (exibirCatalogo == null) {
			exibirCatalogo = Boolean.FALSE;
		}
		return exibirCatalogo;
	}

	public void setExibirCatalogo(Boolean exibirCatalogo) {
		this.exibirCatalogo = exibirCatalogo;
	}

	public String getCampoConsultarPeriodico() {
		if (campoConsultarPeriodico == null) {
			campoConsultarPeriodico = "";
		}
		return campoConsultarPeriodico;
	}

	public void setCampoConsultarPeriodico(String campoConsultarPeriodico) {
		this.campoConsultarPeriodico = campoConsultarPeriodico;
	}

	public String getValorConsultarPeriodico() {
		if (valorConsultarPeriodico == null) {
			valorConsultarPeriodico = "";
		}
		return valorConsultarPeriodico;
	}

	public void setValorConsultarPeriodico(String valorConsultarPeriodico) {
		this.valorConsultarPeriodico = valorConsultarPeriodico;
	}

	public List<AssinaturaPeriodicoVO> getListaConsultarPeriodico() {
		if (listaConsultarPeriodico == null) {
			listaConsultarPeriodico = new ArrayList<AssinaturaPeriodicoVO>(0);
		}
		return listaConsultarPeriodico;
	}

	public void setListaConsultarPeriodico(List<AssinaturaPeriodicoVO> listaConsultarPeriodico) {
		this.listaConsultarPeriodico = listaConsultarPeriodico;
	}

	public List<ExemplarVO> getListaExemplarVOs() {
		if (listaExemplarVOs == null) {
			listaExemplarVOs = new ArrayList<ExemplarVO>(0);
		}
		return listaExemplarVOs;
	}

	public void setListaExemplarVOs(List<ExemplarVO> listaExemplarVOs) {
		this.listaExemplarVOs = listaExemplarVOs;
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

	public Boolean getRemoverEspacoTAGVazia() {
		if (removerEspacoTAGVazia == null) {
			removerEspacoTAGVazia = Boolean.FALSE;
		}
		return removerEspacoTAGVazia;
	}

	public void setRemoverEspacoTAGVazia(Boolean removerEspacoTAGVazia) {
		this.removerEspacoTAGVazia = removerEspacoTAGVazia;
	}

	public String getCodigoBarraExemplarInicial() {
		if (codigoBarraExemplarInicial == null) {
			codigoBarraExemplarInicial = "";
		}
		return codigoBarraExemplarInicial;
	}

	public void setCodigoBarraExemplarInicial(String codigoBarraExemplarInicial) {
		this.codigoBarraExemplarInicial = codigoBarraExemplarInicial;
	}

	public String getCodigoBarraExemplarFinal() {
		if (codigoBarraExemplarFinal == null) {
			codigoBarraExemplarFinal = "";
		}
		return codigoBarraExemplarFinal;
	}

	public void setCodigoBarraExemplarFinal(String codigoBarraExemplarFinal) {
		this.codigoBarraExemplarFinal = codigoBarraExemplarFinal;
	}

	public List<SelectItem> getListaSelectItemOrdenacao() {
		if (listaSelectItemOrdenacao == null) {
			listaSelectItemOrdenacao = new ArrayList<SelectItem>(0);
			listaSelectItemOrdenacao.add(new SelectItem("classificacao", "Classificação"));
			listaSelectItemOrdenacao.add(new SelectItem("cutterpha", "Cutter/PHA"));
			listaSelectItemOrdenacao.add(new SelectItem("titulo", "Título"));
			listaSelectItemOrdenacao.add(new SelectItem("tombo", "Tombo"));
		}
		return listaSelectItemOrdenacao;
	}

	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public void incluirItemExemplarAdicionarListaSelectItemOrdenacao() {
		if (Uteis.isAtributoPreenchido(getListaSelectItemOrdenacao())
				&& Uteis.isAtributoPreenchido(getListaExemplarVOs())) {
			List<SelectItem> listaSelectItemTemporaria = new ArrayList<SelectItem>(0);
			for (SelectItem item : getListaSelectItemOrdenacao()) {
				if (!item.getValue().equals("exemplaresAdicionados")) {
					listaSelectItemTemporaria.add(item);
				}
			}
			listaSelectItemTemporaria.add(new SelectItem("exemplaresAdicionados", "Exemplares Adicionados"));
			getListaSelectItemOrdenacao().clear();
			getListaSelectItemOrdenacao().addAll(listaSelectItemTemporaria);
		}
	}

	public void incluirItemExemplaresListaSelectItemOrdenacao() {
		if (Uteis.isAtributoPreenchido(getListaSelectItemOrdenacao())
				&& !Uteis.isAtributoPreenchido(getListaExemplarVOs())) {
			List<SelectItem> listaSelectItemTemporaria = new ArrayList<SelectItem>(0);
			for (SelectItem item : getListaSelectItemOrdenacao()) {
				if (!item.getValue().equals("exemplar(es)")) {
					listaSelectItemTemporaria.add(item);
				}
			}
			listaSelectItemTemporaria.add(new SelectItem("exemplar(es)", "Exemplar(es) de X até X"));
			getListaSelectItemOrdenacao().clear();
			getListaSelectItemOrdenacao().addAll(listaSelectItemTemporaria);
		}
	}

	public String getCodigoBarraExemplar() {
		if (codigoBarraExemplar == null) {
			codigoBarraExemplar = "";
		}
		return codigoBarraExemplar;
	}

	public void setCodigoBarraExemplar(String codigoBarraExemplar) {
		this.codigoBarraExemplar = codigoBarraExemplar;
	}
	
	public void consultarExemplarPorCodigoBarra() {
		try {
			if (!getCodigoBarraExemplar().equals("")) {
				if (!NumberUtils.isNumber(getCodigoBarraExemplar())) {
					throw new Exception("Formato inválido para o CÓDIGO DE BARRA.");
				}
				ExemplarVO obj = getFacadeFactory().getExemplarFacade().consultarPorCodigoBarrasUnico(getCodigoBarraExemplar(), getBibliotecaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				if (obj != null && !obj.getCodigo().equals(0)) {
					boolean deveAdicionar = true;
					for (ExemplarVO exemplarVO : getListaExemplarVOs()) {
						if (exemplarVO.getCodigoBarra().equals(obj.getCodigoBarra())) {
							deveAdicionar = false;
							return;
						}
					}
					if (deveAdicionar) {
						getListaExemplarVOs().add(obj);
						getCatalogo().getExemplarVOs().add(obj);
					}
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
}
