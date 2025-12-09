/**
 * 
 */
package negocio.comuns.utilitarias;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author Rodrigo Wind
 *
 */
public class HeaderFooterHtmlPage extends PdfPageEventHelper {
	/** The header text. */
	String header;
	String footer;
	float marginTop;
	float marginBottom;
	float marginLeft;
	float marginRight;
	private float sizeTopo;
	private float sizeRodape;
	
	/** The template with the total number of pages. */
	PdfTemplate pdfHeader;
	PdfTemplate pdfFooter;

	/**
	 * 
	 */
	public HeaderFooterHtmlPage() {
		super();
	}

	/**
	 * @param header
	 * @param footer
	 * @param marginTop
	 * @param marginBottom
	 */
	public HeaderFooterHtmlPage(String header, String footer, float marginTop, float marginBottom, float marginLeft, float marginRight, float sizeTopo, float sizeRodape) {
		super();
		this.header = header;
		this.footer = footer;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.sizeTopo = sizeTopo;
		this.sizeRodape = sizeRodape;
	}

	/**
	 * Allows us to change the content of the header.
	 * 
	 * @param header
	 *            The new header String
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Creates the PdfTemplate that will hold the total number of pages.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		
			
	}

	/**
	 * Adds a header to every page
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onStartPage(PdfWriter writer, Document document) {
		ByteArrayInputStream bytes = new ByteArrayInputStream(getHeader().toString().getBytes());
		CSSResolver cssResolver = null;
		Phrase headerPhrase = null;
		CssFile cssFile = null;
		TagProcessorFactory factory = null;
		HtmlPipelineContext htmlContext =  null;
		ElementList elements =  null;
		ElementHandlerPipeline pdf = null;
		HtmlPipeline html =  null;
		CssResolverPipeline css  = null;
		XMLWorker worker = null;
		XMLParser p = null;
		PdfPTable table = null;
		PdfPCell cell = null;
		try {		
			headerPhrase = new Phrase();
			cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		    cssFile = XMLWorkerHelper.getCSS(new FileInputStream(UteisJSF.getCaminhoPastaWeb()+File.separator+"resources"+File.separator+"css"+File.separator+"otimize.css"));
		    cssResolver.addCss(cssFile);
			factory = Tags.getHtmlTagProcessorFactory();
			
			htmlContext = new HtmlPipelineContext(null);
			htmlContext.setTagFactory(factory);

			elements = new ElementList();
			
			pdf = new ElementHandlerPipeline(elements, null);
			html = new HtmlPipeline(htmlContext, pdf);
			css = new CssResolverPipeline(cssResolver, html);
			
			worker = new XMLWorker(css, true);			
			p = new XMLParser(worker);
			p.parse(bytes);
			p.flush();
			
			headerPhrase.addAll(elements);
			
			table = new PdfPTable(1);
			table.setTotalWidth(writer.getPageSize().getWidth()-getMarginLeft());
			table.setLockedWidth(true);			
			table.getDefaultCell().setFixedHeight(getSizeTopo());
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.getDefaultCell().setBorderWidth(0f);			
			
			cell = new PdfPCell();
			cell.setBorder(0);
			cell.setBorderWidth(0f);
			cell.addElement(headerPhrase);
			table.addCell(cell);
			table.writeSelectedRows(0, -1, getMarginLeft(), writer.getPageSize().getHeight()-getMarginTop(), writer.getDirectContent());
			 

		} catch (Exception de) {
			throw new ExceptionConverter(de);
		}finally{
			if(bytes != null){
				try {
					bytes.close();
				} catch (IOException e) {				
					e.printStackTrace();
				}
			}
			cssResolver = null;
			headerPhrase = null;
			cssFile = null;
			factory = null;
			htmlContext =  null;
			elements =  null;
			pdf = null;
			html =  null;
			css  = null;
			worker = null;
			p = null;
			table = null;
			cell = null;
		}
	}

	/**
	 * Adds a header to every page
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		ByteArrayInputStream bytes = new ByteArrayInputStream(getFooter().toString().getBytes());
		CSSResolver cssResolver = null;
		Phrase footerPhrase = null;
		CssFile cssFile = null;
		TagProcessorFactory factory = null;
		HtmlPipelineContext htmlContext =  null;
		ElementList elements =  null;
		ElementHandlerPipeline pdf = null;
		HtmlPipeline html =  null;
		CssResolverPipeline css  = null;
		XMLWorker worker = null;
		XMLParser p = null;
		PdfPTable table = null;
		PdfPCell cell = null;
		try {

			footerPhrase = new Phrase();
			cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		    cssFile = XMLWorkerHelper.getCSS(new FileInputStream(UteisJSF.getCaminhoPastaWeb()+File.separator+"resources"+File.separator+"css"+File.separator+"otimize.css"));
		    cssResolver.addCss(cssFile);
			factory = Tags.getHtmlTagProcessorFactory();
			
			htmlContext = new HtmlPipelineContext(null);
			htmlContext.setTagFactory(factory);

			elements = new ElementList();
			
			pdf = new ElementHandlerPipeline(elements, null);
			html = new HtmlPipeline(htmlContext, pdf);
			css = new CssResolverPipeline(cssResolver, html);
			
			worker = new XMLWorker(css, true);			
			p = new XMLParser(worker);
			p.parse(bytes);
			p.flush();
			footerPhrase.addAll(elements);
			
			table = new PdfPTable(1);
			table.setTotalWidth(writer.getPageSize().getWidth()-getMarginLeft());
			table.setLockedWidth(true);			
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.getDefaultCell().setFixedHeight(getSizeRodape());			
			table.getDefaultCell().setBorderWidth(0f);
			
			cell = new PdfPCell();
			cell.setBorder(0);
			cell.setBorderWidth(0f);
			cell.addElement(footerPhrase);
			table.addCell(cell);
			table.writeSelectedRows(0, -1, getMarginLeft(), getMarginBottom()+getSizeRodape(), writer.getDirectContent());
			

		} catch (Exception de) {
			throw new ExceptionConverter(de);
		}finally{
			if(bytes != null){
				try {
					bytes.close();
				} catch (IOException e) {				
					e.printStackTrace();
				}
			}
			cssResolver = null;
			footerPhrase = null;
			cssFile = null;
			factory = null;
			htmlContext =  null;
			elements =  null;
			pdf = null;
			html =  null;
			css  = null;
			worker = null;
			p = null;
			table = null;
			cell = null;
		}
	}

	/**
	 * Fills out the total number of pages before the document is closed.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		if (footer == null) {
			footer = "";
		}
		return footer;
	}

	/**
	 * @param footer
	 *            the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		if (header == null) {
			header = "";
		}
		return header;
	}

	/**
	 * @return the marginTop
	 */
	public float getMarginTop() {
		return marginTop;
	}

	/**
	 * @param marginTop
	 *            the marginTop to set
	 */
	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}

	/**
	 * @return the marginBottom
	 */
	public float getMarginBottom() {
		return marginBottom;
	}

	/**
	 * @param marginBottom
	 *            the marginBottom to set
	 */
	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}

	/**
	 * @return the marginLeft
	 */
	public float getMarginLeft() {
		return marginLeft;
	}

	/**
	 * @param marginLeft
	 *            the marginLeft to set
	 */
	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

	/**
	 * @return the marginRight
	 */
	public float getMarginRight() {

		return marginRight;
	}

	/**
	 * @param marginRight
	 *            the marginRight to set
	 */
	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}

	/**
	 * @return the pdfHeader
	 */
	public PdfTemplate getPdfHeader() {		
		return pdfHeader;
	}

	/**
	 * @param pdfHeader the pdfHeader to set
	 */
	public void setPdfHeader(PdfTemplate pdfHeader) {
		this.pdfHeader = pdfHeader;
	}

	/**
	 * @return the pdfFooter
	 */
	public PdfTemplate getPdfFooter() {		
		return pdfFooter;
	}

	/**
	 * @param pdfFooter the pdfFooter to set
	 */
	public void setPdfFooter(PdfTemplate pdfFooter) {
		this.pdfFooter = pdfFooter;
	}
	
	
	/**
	 * @return the sizeTop
	 */
	public float getSizeTopo() {
		return sizeTopo;
	}

	/**
	 * @param sizeTop
	 *            the sizeTop to set
	 */
	public void setSizeTop(float sizeTopo) {
		this.sizeTopo = sizeTopo;
	}

	/**
	 * @return the sizeRodape
	 */
	public float getSizeRodape() {
		return sizeRodape;
	}

	/**
	 * @param sizeRodape
	 *            the sizeRodape to set
	 */
	public void setSizeRodape(float sizeRodape) {
		this.sizeRodape = sizeRodape;
	}
	

}
