package negocio.comuns.utilitarias;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class UteisPDF {

	private Document document;
	private PdfWriter writer;
	private Font fonte12;
	private Font fonte10;
	private Font fonte8;
	private Font fonte6;
	private Font fonte18;
	private Font fonte16;
	private Font fonte14;
	private FontFamily fontFamily;

	public void criarDocumento(String filename, float largura, float altura, boolean paisagem, float marginEsquerda, float marginDireita, float marginTop, float marginRodape) throws Exception {
		Document document = new Document(paisagem ? new Rectangle(largura, altura) : new Rectangle(altura, largura), marginEsquerda, marginDireita, marginTop, marginRodape);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		setWriter(writer);
		setDocument(document);
		getDocument().open();
	}
	
	public void fecharDocumento() throws Exception {
		getDocument().close();
	}

	public PdfPTable criarTable(int qtdeColunas, float porcentagem, int[] tamanhoColunas) throws Exception {
		PdfPTable table = new PdfPTable(qtdeColunas);
		table.setWidthPercentage(porcentagem);
		table.setWidths(tamanhoColunas);
		return table;
	}

	public PdfPCell criarCelula(String texto, int cols, int rows, Font fonte, int alinhamentoHorizontal, int alinhamentoVertical, int rotacionarTexto) throws Exception {
		PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
		cell.setColspan(cols);
		cell.setRowspan(rows);
		cell.setRotation(rotacionarTexto);
		cell.setHorizontalAlignment(alinhamentoHorizontal);
		cell.setVerticalAlignment(alinhamentoVertical);
		return cell;
	}

	public void criarBordaArredondada(float margemEsquerda, float margemTopo, float largura, float altura, float radius) throws Exception {
		PdfContentByte canvas = getWriter().getDirectContent();
		canvas.saveState();
		canvas.roundRectangle(margemEsquerda, getDocument().getPageSize().getHeight() - margemTopo, largura, altura, 5);
		// Define a cor da linha
		canvas.setColorStroke(new GrayColor(0.2f));
		// Define a cor de back
		// canvas.setColorFill(new GrayColor(0.9f));
		// Preenche o back
		// canvas.fillStroke();
		canvas.restoreState();
	}

	public void criarTopoPadraoRelatorio(String tituloRelatorio, String unidadeEsino, String endereco, String caminhoLogo) throws Exception {
		criarBordaArredondada(10, 90, getDocument().getPageSize().getWidth() - 20, 80, 5);
		PdfContentByte canvas = getWriter().getDirectContent();		
		canvas.saveState();
		Image img = Image.getInstance(caminhoLogo);
		img.setAbsolutePosition(15, getDocument().getPageSize().getHeight() - 85);
		if (img.getWidth() > 100 || img.getHeight() > 70) {
			img.scaleToFit(150, 100);
		}
		canvas.addImage(img);
		ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(unidadeEsino, getFonte18()), getDocument().getPageSize().getWidth() / 2, getDocument().getPageSize().getHeight() - 34, 0);
		ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(endereco, getFonte8()), getDocument().getPageSize().getWidth() / 2, getDocument().getPageSize().getHeight() - 46, 0);
		ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(tituloRelatorio, getFonte18()), getDocument().getPageSize().getWidth() / 2, getDocument().getPageSize().getHeight() - 76, 0);
		ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(Uteis.getDataComHoraCompleta(new Date()), getFonte8()), getDocument().getPageSize().getWidth() - 20, getDocument().getPageSize().getHeight() - 34, 0);
		ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(getWriter().getCurrentPageNumber() + "/" + getWriter().getPageNumber(), getFonte8()), getDocument().getPageSize().getWidth() - 20, getDocument().getPageSize().getHeight() - 46, 0, 0, 0);
		canvas.restoreState();
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public PdfWriter getWriter() {
		return writer;
	}

	public void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

	public Font getFonte12() {
		if (fonte12 == null) {
			fonte12 = new Font(getFontFamily(), 12);
		}
		return fonte12;
	}

	public void setFonte12(Font fonte12) {
		this.fonte12 = fonte12;
	}

	public Font getFonte10() {
		if (fonte10 == null) {
			fonte10 = new Font(getFontFamily(), 10);
		}
		return fonte10;
	}

	public void setFonte10(Font fonte10) {
		this.fonte10 = fonte10;
	}

	public Font getFonte8() {
		if (fonte8 == null) {
			fonte8 = new Font(getFontFamily(), 8);
		}
		return fonte8;
	}

	public void setFonte8(Font fonte8) {
		this.fonte8 = fonte8;
	}

	public Font getFonte6() {
		if (fonte6 == null) {
			fonte6 = new Font(getFontFamily(), 6);
		}
		return fonte6;
	}

	public void setFonte6(Font fonte6) {
		this.fonte6 = fonte6;
	}

	public Font getFonte18() {
		if (fonte18 == null) {
			fonte18 = new Font(getFontFamily(), 18);
		}
		return fonte18;
	}

	public void setFonte18(Font fonte18) {
		this.fonte18 = fonte18;
	}

	public Font getFonte16() {
		if (fonte16 == null) {
			fonte16 = new Font(getFontFamily(), 16);
		}
		return fonte16;
	}

	public void setFonte16(Font fonte16) {
		this.fonte16 = fonte16;
	}

	public Font getFonte14() {
		if (fonte14 == null) {
			fonte14 = new Font(getFontFamily(), 14);
		}
		return fonte14;
	}

	public void setFonte14(Font fonte14) {
		this.fonte14 = fonte14;
	}

	public FontFamily getFontFamily() {
		if (fontFamily == null) {
			fontFamily = FontFamily.TIMES_ROMAN;
		}
		return fontFamily;
	}

	public void setFontFamily(FontFamily fontFamily) {
		this.fontFamily = fontFamily;
	}

}
