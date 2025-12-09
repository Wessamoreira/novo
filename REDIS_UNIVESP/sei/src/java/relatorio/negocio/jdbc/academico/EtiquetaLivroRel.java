package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.biblioteca.AutorVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.biblioteca.enumeradores.TipoClassificacaoEnum;
import negocio.comuns.biblioteca.enumeradores.TipoRelatorioEtiquetaEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.EtiquetaLivroRelVO;
import relatorio.negocio.interfaces.academico.EtiquetaLivroRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

@Repository
@Scope("singleton")
@Lazy
public class EtiquetaLivroRel extends SuperRelatorio implements EtiquetaLivroRelInterfaceFacade {

	public EtiquetaLivroRel() {
	}

	public void validarDados(CatalogoVO catalogo, Integer biblioteca, String codigoBarraExemplarInicial, String codigoBarraExemplarFinal, Integer unidadeEnsino, LayoutEtiquetaVO layoutEtiquetaVO, List<ExemplarVO> exemplarVOs) throws Exception {
		if (!Uteis.isAtributoPreenchido(layoutEtiquetaVO)) {
			throw new Exception(UteisJSF.internacionalizar("msg_EtiquetaLivroRel_EtiquetaLivroRelVOInformeLayoutImpressaoEtiqueta"));
		}
		if (unidadeEnsino.equals(0)) {
			throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
		}
		if (biblioteca.equals(0)) {
			throw new Exception("O campo BIBLIOTECA deve ser informado.");
		}
		if (exemplarVOs.isEmpty()) {
			if (codigoBarraExemplarInicial.equals("") && codigoBarraExemplarFinal.equals("") && !Uteis.isAtributoPreenchido(catalogo)) {
				throw new Exception("O CATÁLOGO, INTERVALO CÓDIGO BARRA DE EXEMPLARES ou LISTA DE EXEMPLARES deve ser informado.");
			} else if (codigoBarraExemplarInicial.equals("") && codigoBarraExemplarFinal.equals("")) {
				if (!Uteis.isAtributoPreenchido(catalogo)) {
					throw new Exception("O CATÁLOGO deve ser informado.");
				}
			} else {
				if (codigoBarraExemplarInicial.equals("")) {
					throw new Exception("O campo IDENTIFICADOR CÓDIGO BARRA INICIAL DE EXEMPLARES deve ser informado.");
				} else if (!NumberUtils.isNumber(codigoBarraExemplarInicial)) {
					throw new Exception("O campo IDENTIFICADOR CÓDIGO BARRA INICIAL DE EXEMPLARES está em formato inválido.");
				}
				if (codigoBarraExemplarFinal.equals("")) {
					throw new Exception("O campo IDENTIFICADOR CÓDIGO BARRA FINAL DE EXEMPLARES deve ser informado.");
				} else if (!NumberUtils.isNumber(codigoBarraExemplarFinal)) {
					throw new Exception("O campo IDENTIFICADOR CÓDIGO BARRA FINAL DE EXEMPLARES está em formato inválido.");
				}
			}
		}
	}

	public void validarDadosPreenchimentoLista(List<EtiquetaLivroRelVO> vetResultado) throws Exception {
		if (vetResultado.isEmpty()) {
			throw new Exception("Não há dados a serem exibidos no relatório.");
		}
	}

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta.
	 * Tem como passagem por parâmetros a <code>LayoutEtiquetaVO</code> e
	 * <code>OrigemImagemVO</code>
	 */
	@Override
	public String realizarImpressaoEtiquetaBiblioteca(LayoutEtiquetaVO layoutEtiqueta, CatalogoVO catalogo, Integer unidadeEnsino, Integer biblioteca, List<ExemplarVO> listaExemplarVOs, String codigoBarraExemplarInicial, String codigoBarraExemplarFinal, Integer numeroCopias, Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado, String ordenacao, String nomeBiblioteca) throws Exception {
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		try {

			List<EtiquetaLivroRelVO> vetResultado = consultarDadosGeracaoEtiqueta(catalogo, listaExemplarVOs, unidadeEnsino, biblioteca, codigoBarraExemplarInicial, codigoBarraExemplarFinal, usuarioLogado);
            validarDadosGeracaoEtiqueta(vetResultado);
			realizarOrdenacaoListaObjetoEtiquetaLivroRel(vetResultado, codigoBarraExemplarInicial, codigoBarraExemplarFinal, ordenacao);
			layoutEtiqueta.setLayoutEtiquetaTagVO(getFacadeFactory().getLayoutEtiquetaTagFacade().consultarLayoutEtiquetaTagItens(layoutEtiqueta.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoGeralSistemaVO, usuarioLogado));
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();

			realizarMontagemEtiqueta(layoutEtiqueta, writer, pdf, vetResultado, numeroCopias, linha, coluna, removerEspacoTAGVazia, nomeBiblioteca);
			return nomeArquivo;
		} catch (Exception e) {
			throw e;
		} finally {
			if (pdf != null) {
				pdf.close();
			}
			if (arquivo != null) {
				arquivo.close();
			}
		}

	}

	private Boolean getUtilizarTagEtiqueta(EtiquetaLivroRelVO etiquetaLivroRelVO, TagEtiquetaEnum tag, String label) {
		switch (tag) {
		case BIB_AUTOR:
			if (!etiquetaLivroRelVO.getAutores().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_CLASSIFICACAO:
			if (!etiquetaLivroRelVO.getTipoClassificacao().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_EDICAO:
			if (!etiquetaLivroRelVO.getEdicao().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_CODIGO_BARRAS:
			if (!etiquetaLivroRelVO.getCodigoBarra().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_EXEMPLAR:
			if (!etiquetaLivroRelVO.getExemplar().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_NUMERO_CODIGO_BARRA:
			if (!etiquetaLivroRelVO.getCodigoBarra().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_TITULO:
			if (!etiquetaLivroRelVO.getNomeLivro().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_PHA:
			if (!etiquetaLivroRelVO.getCutterPha().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_VOLUME:
			if (!etiquetaLivroRelVO.getVolume().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_ANOVOLUME:
			if (!etiquetaLivroRelVO.getAnovolume().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_MES:
			if (!etiquetaLivroRelVO.getMes().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_EDICAOESPECIAL:
			if (!etiquetaLivroRelVO.getEdicaoEspecial().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_ANO_PUBLICACAO:
			if (!etiquetaLivroRelVO.getAnoPublicacao().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_ANOPUBLICACAOEXEMPLAR:
			if (!etiquetaLivroRelVO.getAnoPublicacaoExemplar().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_NR_PAGINAS_EXEMPLAR:
			if (!etiquetaLivroRelVO.getNrPaginasExemplar().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_ISBN_EXEMPLAR:
			if (!etiquetaLivroRelVO.getIsbnExemplar().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_ISSN_EXEMPLAR:
			if (!etiquetaLivroRelVO.getIssnExemplar().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_EDICAOEXEMPLAR:
			if (!etiquetaLivroRelVO.getEdicaoExemplar().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_NOME_UNIDADE_ENSINO:
			if (!etiquetaLivroRelVO.getNomeUnidadeEnsino().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_SIGLA_UNIDADE_ENSINO:
			if (!etiquetaLivroRelVO.getSiglaUnidadeEnsino().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_SIGLA_TIPO_CATALOGO:
			if (!etiquetaLivroRelVO.getSiglaTipoCatalogo().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_FASCICULO:
			if (!etiquetaLivroRelVO.getFasciculos().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_TITULOEXEMPLAR:
			if (!etiquetaLivroRelVO.getTituloExemplar().trim().isEmpty()) {
				return true;
			}
			return false;
		case BIB_ABREVIACAO_TITULO:
			if (!etiquetaLivroRelVO.getAbreviacaoTitulo().trim().isEmpty()) {
				return true;
			}
			return false;
		default:
			return true;
		}
	}

	public List<LayoutEtiquetaTagVO> realizarCloneListaOriginalLayout(List<LayoutEtiquetaTagVO> listaLayoutEtiquetaTagVOs) throws Exception {
		List<LayoutEtiquetaTagVO> listaClonadaLayoutVOs = new ArrayList<LayoutEtiquetaTagVO>(0);
		for (LayoutEtiquetaTagVO layoutTagVO : listaLayoutEtiquetaTagVOs) {
			LayoutEtiquetaTagVO layoutTagClonadoVO = (LayoutEtiquetaTagVO) layoutTagVO.clone();
			listaClonadaLayoutVOs.add(layoutTagClonadoVO);
		}
		return listaClonadaLayoutVOs;
	}

	/**
	 * Método responsável por atualizar a Posicao da tag quando o valor da mesma
	 * estiver em branco
	 * 
	 * @param layoutEtiqueta
	 * @param etiquetaLivroRelVOs
	 * @throws Exception
	 */
	public void realizarAtualizacaoPosicaoTagQuandoValorBranco(List<LayoutEtiquetaTagVO> listaLayoutEtiquetaTagVOs, EtiquetaLivroRelVO etiquetaLivroRelVO) throws Exception {
		Ordenacao.ordenarLista(listaLayoutEtiquetaTagVOs, "margemTopo");
		HashMap<Integer, LayoutEtiquetaTagVO> mapTagLayoutOriginalVOs = new HashMap<Integer, LayoutEtiquetaTagVO>(0);
		HashMap<Integer, LayoutEtiquetaTagVO> mapMargemTopoLayoutPreenchidoVOs = new HashMap<Integer, LayoutEtiquetaTagVO>(0);
		HashMap<Integer, Integer> mapTagAtualizarValorTopoVOs = new HashMap<Integer, Integer>(0);
		Integer ultimoVazio = null;
		for (LayoutEtiquetaTagVO layoutOriginalTagVO : listaLayoutEtiquetaTagVOs) {

			// Verifica se o valor da etiqueta está preenchido
			if (!getUtilizarTagEtiqueta(etiquetaLivroRelVO, layoutOriginalTagVO.getTagEtiqueta(), layoutOriginalTagVO.getLabelTag())) {
				if (!mapMargemTopoLayoutPreenchidoVOs.containsKey(layoutOriginalTagVO.getMargemTopo())) {
					if (ultimoVazio == null || ultimoVazio.equals(layoutOriginalTagVO.getOrdem())) {
						ultimoVazio = layoutOriginalTagVO.getOrdem();
						mapTagLayoutOriginalVOs.put(layoutOriginalTagVO.getOrdem(), layoutOriginalTagVO);
					}
				}
			} else {
				
				if (!mapTagLayoutOriginalVOs.isEmpty()) {
					for (LayoutEtiquetaTagVO tagValorAtualizar : mapTagLayoutOriginalVOs.values()) {
						LayoutEtiquetaTagVO layoutEtiquetaClonado = (LayoutEtiquetaTagVO) layoutOriginalTagVO.clone();
						mapTagLayoutOriginalVOs.put(layoutEtiquetaClonado.getOrdem(), layoutEtiquetaClonado);

						if (mapTagAtualizarValorTopoVOs.containsKey(layoutOriginalTagVO.getMargemTopo())) {
							layoutOriginalTagVO.setMargemTopo(mapTagAtualizarValorTopoVOs.get(layoutOriginalTagVO.getMargemTopo()));
							mapTagLayoutOriginalVOs.remove(tagValorAtualizar.getOrdem());
							break;
						} else {
							mapTagAtualizarValorTopoVOs.put(layoutOriginalTagVO.getMargemTopo(), tagValorAtualizar.getMargemTopo());
							layoutOriginalTagVO.setMargemTopo(tagValorAtualizar.getMargemTopo());
							mapTagLayoutOriginalVOs.remove(tagValorAtualizar.getOrdem());
							break;
						}
					}
				}
				mapMargemTopoLayoutPreenchidoVOs.put(layoutOriginalTagVO.getMargemTopo(), layoutOriginalTagVO);
			}
		}
	}

	public static final float PONTO = 2.83f;

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta
	 * para o tipo de impressora Laser/Tinta. Tem como passagem por parâmetros a
	 * <code>LayoutEtiquetaVO</code>>
	 */

	public void realizarMontagemEtiqueta(LayoutEtiquetaVO layoutEtiqueta, PdfWriter writer, Document pdf, List<EtiquetaLivroRelVO> etiquetaLivroRelVOs, Integer numeroCopias, Integer linha, Integer coluna, Boolean removerEspacoTAGVazia, String nomeBiblioteca) throws Exception {
		float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;

		float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;

		float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

		float alturaEtiqueta = layoutEtiqueta.getAlturaEtiqueta().floatValue() * PONTO;
		float larguraEtiqueta = layoutEtiqueta.getLarguraEtiqueta().floatValue() * PONTO;
		float margemHorizontalEntreEtiquetas = (layoutEtiqueta.getMargemEntreEtiquetaHorizontal() == 0 ? 0.2f : layoutEtiqueta.getMargemEntreEtiquetaHorizontal().floatValue()) * PONTO;
		float margemVerticalEntreEtiquetas = layoutEtiqueta.getMargemEntreEtiquetaVertical() * PONTO;
		Float margemSuperiorColuna = 0f;
		Float margemEsquerdaLabel = 0f;
		Float margemSuperiorLabel = 0f;
		Float margemEsquerdaColuna = 0f;

		PdfContentByte canvas = writer.getDirectContent();

		Integer linhas = layoutEtiqueta.getNumeroLinhasEtiqueta();
		Integer colunas = layoutEtiqueta.getNumeroColunasEtiqueta();

		for (EtiquetaLivroRelVO etiquetaLivroRelVO : etiquetaLivroRelVOs) {

			// Essa regra só será utilizada caso o booleano para remover espaços
			// esteja marcado.
			// A Lista Original precisa ser clonada porque cada etiqueta pode
			// ser de um catalogo diferente
			// podendo ou não conter tags nulas, por isso para cada etiqueta é
			// necessário passar a lista iriginal e realizar
			// a verificação novamente.
			// Carlos 20/02/2014
			List<LayoutEtiquetaTagVO> listaLayoutEtiquetaTagVOs = null;
			if (removerEspacoTAGVazia) {
				listaLayoutEtiquetaTagVOs = realizarCloneListaOriginalLayout(layoutEtiqueta.getLayoutEtiquetaTagVOs());
				realizarAtualizacaoPosicaoTagQuandoValorBranco(listaLayoutEtiquetaTagVOs, etiquetaLivroRelVO);
				Ordenacao.ordenarLista(listaLayoutEtiquetaTagVOs, "ordem");
			} else {
				listaLayoutEtiquetaTagVOs = layoutEtiqueta.getLayoutEtiquetaTagVOs();
			}
			for (int copia = 1; copia <= numeroCopias; copia++) {

				margemSuperiorColuna = alturaPagina - margemSuperior - ((linha - 1) * margemHorizontalEntreEtiquetas) - ((linha - 1) * alturaEtiqueta);
				margemEsquerdaColuna = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);

				for (LayoutEtiquetaTagVO tag : listaLayoutEtiquetaTagVOs) {
					margemEsquerdaLabel = tag.getMargemDireita() * PONTO;
					margemSuperiorLabel = tag.getMargemTopo() * PONTO;

					PdfTemplate tmp = null;
					if (tag.getTagEtiqueta().equals(TagEtiquetaEnum.BIB_CODIGO_BARRAS)) {
						tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
						tmp.beginText();
						Barcode128 barcode128 = new Barcode128();
						barcode128.setCodeType(Barcode128.CODE128);
						barcode128.setCode(getValorImprimirEtiqueta(etiquetaLivroRelVO, tag.getTagEtiqueta(), "", tag.getApresentarLabelEtiquetaAposTagEtiqueta(), nomeBiblioteca));
						barcode128.setBarHeight(tag.getAlturaCodigoBarra());
						barcode128.setFont(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true));
						barcode128.setSize(tag.getTamanhoFonte().floatValue());
						if (!tag.getImprimirNumeroAbaixo()) {
							barcode128.setAltText("");
							barcode128.setStartStopText(tag.getImprimirNumeroAbaixo());
						}
						tmp.addTemplate(barcode128.createTemplateWithBarcode(canvas, null, null), margemEsquerdaLabel, 0f);
						tmp.endText();
						canvas.addTemplate(tmp, margemEsquerdaColuna, (margemSuperiorColuna - (margemSuperiorLabel)));

					} else {
						tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
						tmp.beginText();
						if (tag.getFontNegrito()) {
							tmp.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_BOLD, "", true), tag.getTamanhoFonte().floatValue());
						} else {
							tmp.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), tag.getTamanhoFonte().floatValue());
						}
						tmp.showTextAligned(Element.ALIGN_LEFT, (getValorImprimirEtiqueta(etiquetaLivroRelVO, tag.getTagEtiqueta(), tag.getLabelTag(), tag.getApresentarLabelEtiquetaAposTagEtiqueta(), nomeBiblioteca)).trim(), margemEsquerdaLabel.floatValue(), alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
						tmp.endText();
						canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
					}

				}
				if (coluna + 1 > colunas) {
					coluna = 1;
					if (linha + 1 > linhas) {
						pdf.newPage();
						linha = 1;
					} else {
						linha++;
					}
				} else {
					coluna++;
				}
			}
		}
	}

	private String getValorImprimirEtiqueta(EtiquetaLivroRelVO etiquetaLivroRelVO, TagEtiquetaEnum tag, String label, Boolean apresentarLabelEtiquetaAposTagEtiqueta, String nomeBiblioteca) {
		switch (tag) {
		case BIB_AUTOR:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getAutores().trim().isEmpty() ? "" : etiquetaLivroRelVO.getAutores().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getAutores().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getAutores().trim();
			}
		case BIB_CLASSIFICACAO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getTipoClassificacao().trim().isEmpty() ? "" : etiquetaLivroRelVO.getTipoClassificacao().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getTipoClassificacao().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getTipoClassificacao().trim();
			}
		case BIB_EDICAO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getEdicao().trim().isEmpty() ? "" : etiquetaLivroRelVO.getEdicao().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getEdicao().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getEdicao().trim();
			}
		case BIB_CODIGO_BARRAS:
			return etiquetaLivroRelVO.getCodigoBarra().trim();
		case BIB_EXEMPLAR:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getExemplar().trim().isEmpty() ? "" : etiquetaLivroRelVO.getExemplar().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getExemplar().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getExemplar().trim();
			}
		case BIB_NUMERO_CODIGO_BARRA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getCodigoBarra().trim().isEmpty() ? "" : etiquetaLivroRelVO.getCodigoBarra().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getCodigoBarra().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getCodigoBarra().trim();
			}
		case BIB_TITULO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getNomeLivro().trim().isEmpty() ? "" : etiquetaLivroRelVO.getNomeLivro().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getNomeLivro().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getNomeLivro().trim();
			}
		case BIB_PHA:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getCutterPha().trim().isEmpty() ? "" : etiquetaLivroRelVO.getCutterPha().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getCutterPha().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getCutterPha().trim();
			}
		case BIB_VOLUME:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getVolume().trim().isEmpty() ? "" : etiquetaLivroRelVO.getVolume().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getVolume().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getVolume().trim();
			}
		case BIB_ANOVOLUME:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getAnovolume().trim().isEmpty() ? "" : etiquetaLivroRelVO.getAnovolume().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getAnovolume().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getAnovolume().trim();
			}
		case BIB_MES:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getMes().trim().isEmpty() ? "" : etiquetaLivroRelVO.getMes().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getMes().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getMes().trim();
			}
		case BIB_EDICAOESPECIAL:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getEdicaoEspecial().trim().isEmpty() ? "" : etiquetaLivroRelVO.getEdicaoEspecial().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getEdicaoEspecial().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getEdicaoEspecial().trim();
			}
		case BIB_ANO_PUBLICACAO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getAnoPublicacao().trim().isEmpty() ? "" : etiquetaLivroRelVO.getAnoPublicacao().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getAnoPublicacao().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getAnoPublicacao().trim();
			}
		case BIB_EDICAOEXEMPLAR:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getEdicaoExemplar().trim().isEmpty() ? "" : etiquetaLivroRelVO.getEdicaoExemplar().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getEdicaoExemplar().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getEdicaoExemplar().trim();
			}
		case BIB_NOME_UNIDADE_ENSINO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getNomeUnidadeEnsino().trim().isEmpty() ? "" : etiquetaLivroRelVO.getNomeUnidadeEnsino().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getNomeUnidadeEnsino().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getNomeUnidadeEnsino().trim();
			}
		case BIB_SIGLA_UNIDADE_ENSINO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getSiglaUnidadeEnsino().trim().isEmpty() ? "" : etiquetaLivroRelVO.getSiglaUnidadeEnsino().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getSiglaUnidadeEnsino().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getSiglaUnidadeEnsino().trim();
			}
		case BIB_SIGLA_TIPO_CATALOGO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getSiglaTipoCatalogo().trim().isEmpty() ? "" : etiquetaLivroRelVO.getSiglaTipoCatalogo().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getSiglaTipoCatalogo().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getSiglaTipoCatalogo().trim();
			}
		case BIB_ABREVIACAO_TITULO:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getAbreviacaoTitulo().trim().isEmpty() ? "" : etiquetaLivroRelVO.getAbreviacaoTitulo().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getAbreviacaoTitulo().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getAbreviacaoTitulo().trim();
			}
		case BIB_TITULOEXEMPLAR:
			if (apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getTituloExemplar().trim().isEmpty() ? "" : etiquetaLivroRelVO.getTituloExemplar().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getTituloExemplar().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getTituloExemplar().trim();
			}
		case BIB_FASCICULO:
			if(apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getFasciculos().trim().isEmpty() ? "" : etiquetaLivroRelVO.getFasciculos().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getFasciculos().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getFasciculos().trim();
			}
		case BIB_NOME_BIBLIOTECA:
			return label + " " + nomeBiblioteca;
		case BIB_ANOPUBLICACAOEXEMPLAR:
			if(apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getAnoPublicacaoExemplar().trim().isEmpty() ? "" : etiquetaLivroRelVO.getAnoPublicacaoExemplar().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getAnoPublicacaoExemplar().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getAnoPublicacaoExemplar().trim();
			}
		case BIB_ISBN_EXEMPLAR:
			if(apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getIsbnExemplar().trim().isEmpty() ? "" : etiquetaLivroRelVO.getIsbnExemplar().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getIsbnExemplar().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getIsbnExemplar().trim();
			}
		case BIB_ISSN_EXEMPLAR:
			if(apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getIssnExemplar().trim().isEmpty() ? "" : etiquetaLivroRelVO.getIssnExemplar().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getIssnExemplar().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getIssnExemplar().trim();
			}
		case BIB_NR_PAGINAS_EXEMPLAR:
			if(apresentarLabelEtiquetaAposTagEtiqueta) {
				return etiquetaLivroRelVO.getNrPaginasExemplar().trim().isEmpty() ? "" : etiquetaLivroRelVO.getNrPaginasExemplar().trim() + " " + label;
			} else {
				return etiquetaLivroRelVO.getNrPaginasExemplar().trim().isEmpty() ? "" : label + " " + etiquetaLivroRelVO.getNrPaginasExemplar().trim();
			}
		default:
			return "";
		}
	}

//	public EtiquetaLivroRelVO realizarCriacaoOjbRel(CatalogoVO catalogo, Integer inicialExemplar, TipoRelatorioEtiquetaEnum tipoRelatorioEtiqueta) throws Exception {
//		EtiquetaLivroRelVO etiquetaLivroRelVO = new EtiquetaLivroRelVO();
//		List<EtiquetaLivroRelVO> vetResultado = consultarOjbRel(catalogo, inicialExemplar, tipoRelatorioEtiqueta);
//		validarDadosPreenchimentoLista(vetResultado);
//		executarDivisaoColunas(vetResultado, etiquetaLivroRelVO, tipoRelatorioEtiqueta);
//		return etiquetaLivroRelVO;
//	}

	public void executarDivisaoColunas(List<EtiquetaLivroRelVO> vetResultado, EtiquetaLivroRelVO etiquetaLivroRelVO, TipoRelatorioEtiquetaEnum tipoRelatorioEtiqueta) {
		int col = 1;
		for (EtiquetaLivroRelVO etiqueta : vetResultado) {
			if (col == 1) {
				etiquetaLivroRelVO.getListaEtiquetaLivroColuna1().add(etiqueta);
				col = 2;
				continue;
			}
			if (col == 2) {
				etiquetaLivroRelVO.getListaEtiquetaLivroColuna2().add(etiqueta);
				if (tipoRelatorioEtiqueta.equals(TipoRelatorioEtiquetaEnum.CODIGO_BARRA) || tipoRelatorioEtiqueta.equals(TipoRelatorioEtiquetaEnum.CODIGO_BARRA_LAYOUT_2)) {
					col = 3;
				} else if (tipoRelatorioEtiqueta.equals(TipoRelatorioEtiquetaEnum.CODIGO_BARRA_LAYOUT_3)) {
					col = 1;
				} else {
					col = 1;
				}
				continue;
			}
			if (col == 3) {
				etiquetaLivroRelVO.getListaEtiquetaLivroColuna3().add(etiqueta);
				col = 1;
				continue;
			}
		}
	}
	
	public List<EtiquetaLivroRelVO> consultarDadosGeracaoEtiqueta(CatalogoVO catalogo, List<ExemplarVO> listaExemplarVOs, Integer unidadeEnsino, Integer biblioteca, String codigoBarraExemplarInicial, String codigoBarraExemplarFinal, UsuarioVO usuarioLogado) throws Exception {
		//Não precisa se preocupar se a unidadeensino está diferente de zero porque é feita a validação dos dados ao imprimir.
		UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuarioLogado);
		if (Uteis.isAtributoPreenchido(codigoBarraExemplarInicial) && Uteis.isAtributoPreenchido(codigoBarraExemplarFinal)) {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT distinct codigobarra, catalogo.titulo, case when (catalogo.assinaturaPeriodico = true) then exemplar.abreviacaoTitulo else catalogo.abreviacaoTitulo end AS abreviacaoTitulo, catalogo.cutterPha, catalogo.classificacaoBibliografica, exemplar.volume, numeroExemplar, exemplar.mes, exemplar.anovolume, exemplar.nredicaoespecial, exemplar.tituloexemplar, ");
			sb.append(" exemplar.edicao as \"exemplarEdicao\", catalogo.edicao, catalogo.anoPublicacao, exemplar.anoPublicacao as \"anoPublicacaoExemplar\", exemplar.nrPaginas as \"nrPaginasExemplar\", exemplar.isbn as \"isbnExemplar\", exemplar.issn as \"issnExemplar\", exemplar.biblioteca, tipoCatalogo.sigla, exemplar.fasciculos as \"fasciculos\", ");
			sb.append(" array_to_string(array(select autor.nome from catalogoautor inner join autor on autor.codigo = catalogoautor.autor ");
			sb.append(" where catalogoautor.catalogo = catalogo.codigo order by autor.nome), ', ') as autores ");
			sb.append(" FROM exemplar ");
			sb.append(" inner join catalogo on catalogo.codigo = exemplar.catalogo ");
			sb.append(" left join tipoCatalogo on tipoCatalogo.codigo = catalogo.tipoCatalogo ");
			sb.append(" LEFT JOIN biblioteca ON biblioteca.codigo = exemplar.biblioteca ");
			sb.append(" LEFT JOIN unidadeEnsinoBiblioteca ON unidadeEnsinoBiblioteca.biblioteca = biblioteca.codigo ");
			sb.append(" WHERE exemplar.codigobarra::NUMERIC >= ").append(Long.valueOf(codigoBarraExemplarInicial)).append("");
			sb.append(" AND exemplar.codigobarra::NUMERIC <= ").append(Long.valueOf(codigoBarraExemplarFinal)).append("");
			if (Uteis.isAtributoPreenchido(biblioteca)) {
				sb.append(" AND unidadeEnsinoBiblioteca.biblioteca = ").append(biblioteca);
			}
			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
				sb.append(" AND unidadeEnsinoBiblioteca.unidadeEnsino = ").append(unidadeEnsino);
			}
			if (Uteis.isAtributoPreenchido(catalogo)) {
				sb.append(" AND catalogo.codigo = ").append(catalogo.getCodigo());
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			return montarDadosConsulta(tabelaResultado, unidadeEnsinoVO);
		} else {
			if (listaExemplarVOs.isEmpty()) {
				listaExemplarVOs = getFacadeFactory().getExemplarFacade().consultarPorCatalogoBiblioteca(catalogo.getCodigo(), biblioteca, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			}
			return montarDadosEtiqueta(listaExemplarVOs, unidadeEnsino, usuarioLogado);
		}
	}

	/**
	 * Método responsável por buscar a unidade de ensino dentro da biblioteca,
	 * caso exista mais de uma unidade de ensino para a mesma biblioteca o
	 * sistema irá utilizar a unidade de ensino matriz.
	 * 
	 * @param exemplar
	 * @return
	 * @throws Exception
	 */
	public static UnidadeEnsinoVO realizarBuscaUnidadeEnsinoUtilizarImpressaoEtiqueta(ExemplarVO exemplar) throws Exception {
		UnidadeEnsinoVO unidadeEnsinoVO = null;
		if (!exemplar.getBiblioteca().getCodigo().equals(0)) {
			exemplar.getBiblioteca().setUnidadeEnsinoBibliotecaVOs(getFacadeFactory().getUnidadeEnsinoBibliotecaFacade().consultarPorBiblioteca(exemplar.getBiblioteca().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			if (!exemplar.getBiblioteca().getUnidadeEnsinoBibliotecaVOs().isEmpty()) {
				if (exemplar.getBiblioteca().getUnidadeEnsinoBibliotecaVOs().size() > 1) {
					for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO : exemplar.getBiblioteca().getUnidadeEnsinoBibliotecaVOs()) {
						if (unidadeEnsinoBibliotecaVO.getUnidadeEnsino().getMatriz()) {
							unidadeEnsinoVO = unidadeEnsinoBibliotecaVO.getUnidadeEnsino();
							break;
						}
					}
				} else {
					unidadeEnsinoVO = exemplar.getBiblioteca().getUnidadeEnsinoBibliotecaVOs().get(0).getUnidadeEnsino();
				}
			}
		}
		return unidadeEnsinoVO;
	}

	public static List<EtiquetaLivroRelVO> montarDadosEtiqueta(List<ExemplarVO> listaExemplarVOs, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<EtiquetaLivroRelVO> vetResultado = new ArrayList<EtiquetaLivroRelVO>(0);
		HashMap<Integer, CatalogoVO> mapCatalogoVOs = new HashMap<Integer, CatalogoVO>(0);
		CatalogoVO catalogo = null;
		Iterator<ExemplarVO> i = listaExemplarVOs.iterator();
		while (i.hasNext()) {
			ExemplarVO exemplar = (ExemplarVO) i.next();

			UnidadeEnsinoVO unidadeEnsinoVO;
			if(!unidadeEnsino.equals(0)) {
				unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuarioVO);
			} else {
				unidadeEnsinoVO = realizarBuscaUnidadeEnsinoUtilizarImpressaoEtiqueta(exemplar);
			}
			getFacadeFactory().getExemplarFacade().carregarDados(exemplar, unidadeEnsinoVO.getCodigo(), NivelMontarDados.BASICO, usuarioVO);

			if (!mapCatalogoVOs.containsKey(exemplar.getCatalogo().getCodigo())) {
				catalogo = new CatalogoVO();
				catalogo = getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(exemplar.getCatalogo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, 0,  null);
				mapCatalogoVOs.put(catalogo.getCodigo(), catalogo);
			} else {
				catalogo = mapCatalogoVOs.get(exemplar.getCatalogo().getCodigo());
			}
			EtiquetaLivroRelVO etiqueta = new EtiquetaLivroRelVO();
			String autor = "";
			String virgula = "";
			for (AutorVO obj : catalogo.getAutorVOs()) {
				if (obj.getTipoAutoria().equals("AU") || obj.getTipoAutoria().equals("CA") || obj.getTipoAutoria().equals("CO")) {
					autor += virgula + obj.getNome();
					virgula = ", ";
				}
			}
			etiqueta.setAutores(autor);
			etiqueta.setNomeLivro(catalogo.getTitulo());
			etiqueta.setSiglaTipoCatalogo(catalogo.getTipoCatalogo().getSigla());
			etiqueta.setCutterPha(catalogo.getCutterPha());
			etiqueta.setTipoClassificacao(catalogo.getClassificacaoBibliografica());
			etiqueta.setEdicao(catalogo.getEdicao());
			etiqueta.setEdicaoExemplar(exemplar.getEdicao());
			etiqueta.setAnoPublicacao(catalogo.getAnoPublicacao());
			etiqueta.setAnoPublicacaoExemplar(exemplar.getAnoPublicacao());
			etiqueta.setIdentificador(catalogo.getClassificacaoBibliografica());
			etiqueta.setCodigoBarra(exemplar.getCodigoBarra());
			etiqueta.setExemplar("" + exemplar.getNumeroExemplar());
			etiqueta.setVolume(exemplar.getVolume());
			etiqueta.setAnovolume(exemplar.getAnovolume());
			etiqueta.setMes(exemplar.getMes());
			
			etiqueta.setFasciculos(exemplar.getFasciculos());
			
			if (catalogo.getAssinaturaPeriodico()) {
				etiqueta.setEdicaoEspecial(exemplar.getNrEdicaoEspecial());
				etiqueta.setEdicaoExemplar(exemplar.getEdicao());
				etiqueta.setAbreviacaoTitulo(exemplar.getAbreviacaoTitulo());
				etiqueta.setTituloExemplar(exemplar.getTituloExemplar());
			} else {
				if (!exemplar.getEdicao().equals("")) {
					etiqueta.setEdicaoExemplar(exemplar.getEdicao());
				} else {
					etiqueta.setEdicaoExemplar(catalogo.getEdicao());
				}
				if (!exemplar.getNrPaginas().equals(0)) {
					etiqueta.setNrPaginasExemplar(String.valueOf(exemplar.getNrPaginas()));
				} else {
					etiqueta.setNrPaginasExemplar(String.valueOf(catalogo.getNrPaginas()));
				}
				if (!exemplar.getIsbn().equals("")) {
					etiqueta.setIsbnExemplar(exemplar.getIsbn());
				} else {
					etiqueta.setIsbnExemplar(catalogo.getIsbn());
				}
				if (!exemplar.getIssn().equals("")) {
					etiqueta.setIssnExemplar(exemplar.getIssn());
				} else {
					etiqueta.setIssnExemplar(catalogo.getIssn());
				}
				etiqueta.setAbreviacaoTitulo(catalogo.getAbreviacaoTitulo());
			}
			if (unidadeEnsinoVO != null && !unidadeEnsinoVO.getCodigo().equals(0)) {
				etiqueta.setNomeUnidadeEnsino(unidadeEnsinoVO.getNome());
				etiqueta.setSiglaUnidadeEnsino(unidadeEnsinoVO.getAbreviatura());
			}
			vetResultado.add(etiqueta);
		}
		return vetResultado;
	}

//	public List<EtiquetaLivroRelVO> consultarOjbRel(CatalogoVO catalogo, Integer inicialExemplar, TipoRelatorioEtiquetaEnum tipoRelatorioEtiqueta) throws Exception {
//		//Não precisa se preocupar se a unidadeensino está diferente de zero porque é feita a validação dos dados ao imprimir.
//		UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, usuarioLogado);
//		if (inicialExemplar != null) {
//			StringBuilder sb = new StringBuilder();
//			sb.append(" SELECT codigobarra, catalogo.titulo, catalogo.cutterPha, catalogo.classificacaoBibliografica, exemplar.volume, ");
//			sb.append(" catalogo.edicao, catalogo.anoPublicacao, exemplar.biblioteca, tipoCatalogo.sigla, ");
//			sb.append(" array_to_string(array(select autor.nome from catalogoautor inner join autor on autor.codigo = catalogoautor.autor ");
//			sb.append(" where catalogoautor.catalogo = catalogo.codigo order by autor.nome), ', ') as autores ");
//			sb.append(" FROM exemplar ");
//			sb.append(" inner join catalogo on catalogo.codigo = exemplar.catalogo ");
//			sb.append(" left join tipoCatalogo on tipoCatalogo.codigo = catalogo.tipoCatalogo ");
//			sb.append(" WHERE exemplar.codigobarra::INT = ").append(inicialExemplar).append(" ");
//			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
//			return montarDadosConsulta(tabelaResultado);
//		} else {
//			return montarDadosConsulta(catalogo, tipoRelatorioEtiqueta);
//		}
//	}

	public static List<EtiquetaLivroRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		List<EtiquetaLivroRelVO> vetResultado = new ArrayList<EtiquetaLivroRelVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, unidadeEnsinoVO));
		}
		return vetResultado;
	}

	public static List<EtiquetaLivroRelVO> montarDadosConsulta(CatalogoVO catalogo, TipoRelatorioEtiquetaEnum tipoRelatorioEtiqueta) throws Exception {
		List<EtiquetaLivroRelVO> vetResultado = new ArrayList<EtiquetaLivroRelVO>(0);
		Iterator i = catalogo.getExemplarVOs().iterator();
		while (i.hasNext()) {
			ExemplarVO exemplar = (ExemplarVO) i.next();
			EtiquetaLivroRelVO etiqueta = new EtiquetaLivroRelVO();
			etiqueta.setCodigoBarra(exemplar.getCodigoBarra());
			etiqueta.setCutterPha(catalogo.getCutterPha());
			if (tipoRelatorioEtiqueta.equals(TipoRelatorioEtiquetaEnum.CODIGO_BARRA)) {
				etiqueta.setNomeLivro(catalogo.getTitulo());
				vetResultado.add(etiqueta);
				continue;
			}
			if (tipoRelatorioEtiqueta.equals(TipoRelatorioEtiquetaEnum.CODIGO_BARRA_LAYOUT_2)) {
				etiqueta.setTipoClassificacao(catalogo.getClassificacaoBibliografica());
				etiqueta.setCutterPha(catalogo.getCutterPha());
				// etiqueta.setVolume(catalogo.getVolume());
				etiqueta.setVolume(exemplar.getVolume());
				vetResultado.add(etiqueta);
				continue;
			}

			etiqueta.setNomeLivro("Título: " + catalogo.getTitulo());
			etiqueta.setAnoPublicacao(catalogo.getAnoPublicacao());
			etiqueta.setEdicao(catalogo.getEdicao() + ". ed.");

			// catalogo.setClassificacaoBibliografica(getFacadeFactory().getClassificacaoBibliograficaFacade().consultarPorChavePrimaria(catalogo.getClassificacaoBibliografica().getCodigo(),
			// Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			etiqueta.setTipoClassificacao(TipoClassificacaoEnum.CDU.toString());
			etiqueta.setIdentificador(catalogo.getClassificacaoBibliografica());
			String autor = "Autor: ";
			String virgula = "";
			for (AutorVO obj : catalogo.getAutorVOs()) {
				if (obj.getTipoAutoria().equals("AU") || obj.getTipoAutoria().equals("CA") || obj.getTipoAutoria().equals("CO")) {
					autor += virgula + obj.getNome();
					virgula = ", ";
				}
			}
			etiqueta.setAutores(autor);
			etiqueta.setVolume("V. " + exemplar.getVolume());
			etiqueta.setExemplar("Ex. " + exemplar.getNumeroExemplar());
			if (tipoRelatorioEtiqueta.equals(TipoRelatorioEtiquetaEnum.CODIGO_BARRA_LAYOUT_3)) {
				etiqueta.setNomeLivro(catalogo.getTitulo());
				etiqueta.setTipoClassificacao(catalogo.getClassificacaoBibliografica());
			}
			vetResultado.add(etiqueta);
		}
		return vetResultado;
	}

	public static EtiquetaLivroRelVO montarDados(SqlRowSet dadosSql, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		EtiquetaLivroRelVO obj = new EtiquetaLivroRelVO();
		obj.setCodigoBarra(dadosSql.getString("codigobarra"));
		obj.setNomeLivro(dadosSql.getString("titulo"));
		obj.setCutterPha(dadosSql.getString("cutterPha"));
		obj.setTipoClassificacao(dadosSql.getString("classificacaoBibliografica"));
		obj.setIdentificador(dadosSql.getString("classificacaoBibliografica"));
		obj.setVolume(dadosSql.getString("volume"));
		obj.setEdicao(dadosSql.getString("edicao"));
		obj.setAnoPublicacao(dadosSql.getString("anoPublicacao"));
		obj.setAnoPublicacaoExemplar(dadosSql.getString("anoPublicacaoExemplar"));
		obj.setNrPaginasExemplar(String.valueOf(dadosSql.getInt("nrPaginasExemplar")));
		obj.setIsbnExemplar(dadosSql.getString("isbnExemplar"));
		obj.setIssnExemplar(dadosSql.getString("issnExemplar"));
		obj.setAutores(dadosSql.getString("autores"));
		obj.setExemplar(dadosSql.getString("numeroExemplar"));
		obj.setMes(dadosSql.getString("mes"));
		obj.setAnovolume(dadosSql.getString("anovolume"));
		obj.setEdicaoEspecial(dadosSql.getString("nredicaoespecial"));
		obj.setTituloExemplar(dadosSql.getString("tituloexemplar"));
		obj.setEdicaoExemplar(dadosSql.getString("exemplarEdicao"));
		obj.setCodigoBiblioteca(dadosSql.getInt("biblioteca"));
		obj.setSiglaTipoCatalogo(dadosSql.getString("sigla"));
		obj.setAbreviacaoTitulo(dadosSql.getString("abreviacaoTitulo"));
		obj.setFasciculos(dadosSql.getString("fasciculos"));
		obj.setNomeUnidadeEnsino(unidadeEnsinoVO.getNome());
		obj.setSiglaUnidadeEnsino(unidadeEnsinoVO.getAbreviatura());

		return obj;
	}
	
	

	/**
	 * Método responsável por buscar a unidade de ensino dentro da biblioteca,
	 * caso exista mais de uma unidade de ensino para a mesma biblioteca o
	 * sistema irá utilizar a unidade de ensino matriz.
	 * 
	 * @param exemplar
	 * @return
	 * @throws Exception
	 */
	public static void realizarPreenchimentoDadosUnidadeEnsinoUtilizarImpressaoEtiquetaPorBiblioteca(Integer biblioteca, EtiquetaLivroRelVO obj) throws Exception {
		UnidadeEnsinoVO unidadeEnsinoVO = null;
		List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs = null;
		if (!biblioteca.equals(0)) {
			unidadeEnsinoBibliotecaVOs = getFacadeFactory().getUnidadeEnsinoBibliotecaFacade().consultarPorBiblioteca(biblioteca, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			if (!unidadeEnsinoBibliotecaVOs.isEmpty()) {
				if (unidadeEnsinoBibliotecaVOs.size() > 1) {
					for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO : unidadeEnsinoBibliotecaVOs) {
						if (unidadeEnsinoBibliotecaVO.getUnidadeEnsino().getMatriz()) {
							unidadeEnsinoVO = unidadeEnsinoBibliotecaVO.getUnidadeEnsino();
							break;
						}
					}
				} else {
					unidadeEnsinoVO = unidadeEnsinoBibliotecaVOs.get(0).getUnidadeEnsino();
				}
				if (unidadeEnsinoVO != null && !unidadeEnsinoVO.getCodigo().equals(0)) {
					obj.setNomeUnidadeEnsino(unidadeEnsinoVO.getNome());
					obj.setSiglaUnidadeEnsino(unidadeEnsinoVO.getAbreviatura());
				}
			}
		}
	}

	public static String getDesignIReportRelatorio(TipoRelatorioEtiquetaEnum tipoRelatorioEtiqueta) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade(tipoRelatorioEtiqueta) + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade(TipoRelatorioEtiquetaEnum tipoRelatorioEtiqueta) {
		if (tipoRelatorioEtiqueta.equals(tipoRelatorioEtiqueta.ETIQUETA_EXTERNA)) {
			return ("EtiquetaExternaRel");
		} else if (tipoRelatorioEtiqueta.equals(tipoRelatorioEtiqueta.ETIQUETA_INTERNA)) {
			return ("EtiquetaInternaRel");
		} else if (tipoRelatorioEtiqueta.equals(tipoRelatorioEtiqueta.CODIGO_BARRA_LAYOUT_2)) {
			return ("EtiquetaCodigoBarraLayout2Rel");
		} else if (tipoRelatorioEtiqueta.equals(tipoRelatorioEtiqueta.CODIGO_BARRA_LAYOUT_3)) {
			return ("EtiquetaCodigoBarraLayout3Rel");
		}
		return ("EtiquetaLivrosRel");
	}

	@Override
	public void preencherTodosListaExemplar(List<ExemplarVO> exemplarVOs) {
		for (ExemplarVO exemplarVO : exemplarVOs) {
			exemplarVO.setExemplarSelecionado(Boolean.TRUE);
		}
	}

	@Override
	public void desmarcarTodosListaExemplar(List<ExemplarVO> exemplarVOs) {
		for (ExemplarVO exemplarVO : exemplarVOs) {
			exemplarVO.setExemplarSelecionado(Boolean.FALSE);
		}
	}
	
	public void realizarOrdenacaoListaObjetoEtiquetaLivroRel(List<EtiquetaLivroRelVO> etiquetaLivroRelVO, String codigoBarraExemplarInicial, String codigoBarraExemplarFinal, String ordenacao) {
		if (ordenacao.equals("classificacao")) {
			Ordenacao.ordenarLista(etiquetaLivroRelVO, "tipoClassificacao");
		} else if (ordenacao.equals("cutterpha")) {
			Ordenacao.ordenarLista(etiquetaLivroRelVO, "cutterPha");
		} else if (ordenacao.equals("titulo")) {
			Ordenacao.ordenarLista(etiquetaLivroRelVO, "nomeLivro");
		} else condicaoExemplaresAdicionados:if (ordenacao.equals("exemplaresAdicionados")) {
           //Mantem a ordem da lista que o usuário adicionou na tela. 
			break condicaoExemplaresAdicionados;
		} else if (ordenacao.equals("exemplar(es)") && Uteis.isAtributoPreenchido(codigoBarraExemplarInicial) && Uteis.isAtributoPreenchido(codigoBarraExemplarFinal)) {
			Ordenacao.ordenarLista(etiquetaLivroRelVO, "codigoBarra");
		} else {
			Ordenacao.ordenarLista(etiquetaLivroRelVO, "codigoBarra");
		}
	}

	public void validarDadosGeracaoEtiqueta(List<EtiquetaLivroRelVO> etiquetaLivroRelVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(etiquetaLivroRelVO)) {
			throw new Exception(UteisJSF.internacionalizar("msg_EtiquetaLivroRel_EtiquetaLivroRelVONaoEncontrado"));
		}
	}
	 
}
