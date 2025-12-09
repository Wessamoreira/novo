package relatorio.negocio.jdbc.academico;

import java.awt.geom.RoundRectangle2D;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import relatorio.negocio.comuns.academico.AniversariantesDoMesRelVO;
import relatorio.negocio.interfaces.academico.EtiquetaAniversarianteRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EtiquetaAniversarianteRel extends SuperRelatorio implements EtiquetaAniversarianteRelInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	public static final float PONTO = 2.83f;

	public EtiquetaAniversarianteRel() {

	}

	@Override
	public String realizarImpressaoEtiqueta(Integer unidadeEnsino, TurmaVO turmaVO, String mes, String dia,String diaFim, boolean aluno, boolean funcionario, LayoutEtiquetaVO layoutEtiqueta,
			FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer numeroCopias, Integer linha, Integer coluna, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado)throws Exception {
		 
		List<AniversariantesDoMesRelVO> aniversariantesDoMesRelVOs = getFacadeFactory().getAniversariantesDoMesRelFacade().criarObjeto(unidadeEnsino, turmaVO, mes, dia, diaFim, usuarioLogado, aluno, funcionario, filtroRelatorioAcademicoVO);
		if (aniversariantesDoMesRelVOs.isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
		}
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		try {
			if (layoutEtiqueta.getAlturaEtiqueta().equals(0) || layoutEtiqueta.getAlturaFolhaImpressao().equals(0) || layoutEtiqueta.getLarguraEtiqueta().equals(0) || layoutEtiqueta.getLarguraFolhaImpressao().equals(0)) {
				throw new Exception("Insira um valor para altura ou largura");
			}
			layoutEtiqueta.setLayoutEtiquetaTagVO(getFacadeFactory().getLayoutEtiquetaTagFacade().consultarLayoutEtiquetaTagItens(layoutEtiqueta.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoGeralSistemaVO, usuarioLogado));
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + "/" + "relatorio" + "/" + nomeArquivo);
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();

			realizarMontagemPreviewEtiquetLaserTinta(layoutEtiqueta, aniversariantesDoMesRelVOs, writer, pdf, numeroCopias, linha, coluna);
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
	
	
	public void realizarMontagemPreviewEtiquetLaserTinta(LayoutEtiquetaVO layoutEtiqueta, List<AniversariantesDoMesRelVO> aniversariantesDoMesRelVOs, PdfWriter writer, Document pdf, Integer numeroCopias, Integer linha, Integer coluna) throws Exception {
		float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
		float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
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
		PdfGraphics2D pdfEtiqueta = (PdfGraphics2D) writer.getDirectContent().createGraphics(larguraPagina, alturaPagina);
		Integer linhas = layoutEtiqueta.getNumeroLinhasEtiqueta();
		Integer colunas = layoutEtiqueta.getNumeroColunasEtiqueta();

		for (AniversariantesDoMesRelVO aniversariantesDoMesRelVO : aniversariantesDoMesRelVOs) {
			for (int copia = 1; copia <= numeroCopias; copia++) {
				margemSuperiorColuna = alturaPagina - margemSuperior - ((linha - 1) * margemHorizontalEntreEtiquetas) - ((linha - 1) * alturaEtiqueta);
				margemEsquerdaColuna = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);
				pdfEtiqueta.draw(new RoundRectangle2D.Double(margemEsquerda + ((coluna -1) * margemVerticalEntreEtiquetas) + ((coluna -1) * larguraEtiqueta), margemSuperior + ((linha -1) * margemHorizontalEntreEtiquetas) + ((linha -1) * alturaEtiqueta), larguraEtiqueta, alturaEtiqueta, 0, 0));
				for (LayoutEtiquetaTagVO tag : layoutEtiqueta.getLayoutEtiquetaTagVOs()) {
					margemEsquerdaLabel = tag.getMargemDireita() * PONTO;
					margemSuperiorLabel = tag.getMargemTopo() * PONTO;
					PdfTemplate tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
					tmp.beginText();
					tmp.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), tag.getTamanhoFonte().floatValue());
					tmp.showTextAligned(Element.ALIGN_LEFT, (tag.getLabelTag() + " " + getValorImprimirEtiqueta(aniversariantesDoMesRelVO, tag.getTagEtiqueta())).trim(), margemEsquerdaLabel.floatValue(), alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
					tmp.endText();
					canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
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
	
	
	private String getValorImprimirEtiqueta(AniversariantesDoMesRelVO aniversariantesDoMesRelVO, TagEtiquetaEnum tag) {
		switch (tag) {
		case TAG_LABEL_FIXO1:
			return "";
		case TAG_LABEL_FIXO2:
			return "";
		case TAG_LABEL_FIXO3:
			return "";
		case NOME_PESSOA:
			return aniversariantesDoMesRelVO.getNomeAluno();
		case ENDERECO:
			return aniversariantesDoMesRelVO.getEndereco();
		case BAIRRO:
			return aniversariantesDoMesRelVO.getBairro();
		case CEP:
			return aniversariantesDoMesRelVO.getCep();
		case COMPLEMENTO:
			return aniversariantesDoMesRelVO.getComplemento();
		case TELEFONE:
			return aniversariantesDoMesRelVO.getTelefone();
		case CELULAR:
			return aniversariantesDoMesRelVO.getCelular();
		case CIDADE:
			return aniversariantesDoMesRelVO.getCidade();
		case ESTADO:
			return aniversariantesDoMesRelVO.getEstado();
		case NUMERO:
			return aniversariantesDoMesRelVO.getNumero();
		case EMAIL:
			return aniversariantesDoMesRelVO.getEmail();
		case EMAIL2:
			return aniversariantesDoMesRelVO.getEmail2();
		case DATA_NASCIMENTO:
			return aniversariantesDoMesRelVO.getDataNascimento();
		default:
			return "";
		}
	}
	
 

}
