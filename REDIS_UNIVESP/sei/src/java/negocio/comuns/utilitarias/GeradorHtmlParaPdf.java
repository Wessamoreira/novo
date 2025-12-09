package negocio.comuns.utilitarias;

/**
 * 
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author Rodrigo Wind
 *
 */
public class GeradorHtmlParaPdf {

	private String header;
	private String footer;
	private String body;
	private PdfWriter writer;
	private Document document;
	private Rectangle pageSize;
	private float marginTop;
	private float marginBottom;
	private float sizeTopo;
	private float sizeRodape;
	private float marginLeft;
	private float marginRight;
	

	/**
	 * @param header
	 * @param footer
	 * @param body
	 */
	public GeradorHtmlParaPdf(String header, String footer, String body) {
		super();
		this.header = header;
		this.footer = footer;
		this.body = UteisHTML.realizarRemocaoTagsPageAndSubPageHTML(body);
	}

	public static void main(String[] args) {

		String header = "<img src='E:\\Icones\\icons\\accept.png' width='48' height='48' alt='' />";
		String footer = "<img src='E:\\Icones\\icons\\add.png' width='48' height='48' alt='' />";
//		String body = "<img src='E:\\Icones\\icons\\application.png' width='48' height='48' alt='' />";
		
		StringBuilder body = new StringBuilder("");
		for(int x = 0; x<50;x++){
			body.append("<div><img src='E:\\Icones\\icons\\application.png' width='48' height='48' alt='' />");
			body.append("hdbvhjsbv sdvbh sdvsdh bvdh vbdbv sdhbv sdbv sdbvjh sdvjhbs dvhbsd hvbds vhdbv sdhbv sdhbv sdhbv sdhbv sdhbvsd vhsdbv ");			
			body.append("hdbvhjsbv sdvbh sdvsdh bvdh vbdbv sdhbv sdbv sdbvjh sdvjhbs dvhbsd hvbds vhdbv sdhbv sdhbv sdhbv sdhbv sdhbvsd vhsdbv ");
			body.append("hdbvhjsbv sdvbh sdvsdh bvdh vbdbv sdhbv sdbv sdbvjh sdvjhbs dvhbsd hvbds vhdbv sdhbv sdhbv sdhbv sdhbv sdhbvsd vhsdbv </div>");
		}
		GeradorHtmlParaPdf geradorPdf = new GeradorHtmlParaPdf(header, footer, body.toString());
		try{
		geradorPdf.realizarGeracaoDocumentoPdf(new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth()), 20f, 20f, 10f, 10f, 60f, 40f,  "E:" + File.separator + "teste.pdf");
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void realizarGeracaoDocumentoPdf(String arquivo) throws Exception{
		try {
			realizarCriacaoDocumento(arquivo);
			realizarCriacaoHeaderFooter();
			document.open();
//			writer.flush();
			realizarCriacaoCorpo();
			writer.flush();			
		} catch (Exception e) {
			throw e;
		}finally{
			if (writer != null && !writer.isCloseStream()) {				
				writer.close();
			}
			if (document != null) {
				document.close();
			}
		}
	}
	
	/**
	 * 
	 * @author Rodrigo Wind - 18/05/2016
	 * @param pageSize
	 * @param marginTop em cm
	 * @param marginBottom em cm
	 * @param marginLeft em cm
	 * @param marginRight em cm
	 * @param sizeTopo em cm
	 * @param sizeRodape em cm
	 * @param arquivo
	 */
	public void realizarGeracaoDocumentoPdf(Rectangle pageSize, float marginTop, float marginBottom, float marginLeft, float marginRight, float sizeTopo, float sizeRodape, String arquivo) throws Exception{
		this.pageSize = pageSize;
		this.marginTop = (marginTop*72)/2.54f;
		this.marginBottom = (marginBottom*72)/2.54f;
		this.marginLeft = (marginRight*72)/2.54f;
		this.marginLeft = (marginRight*72)/2.54f;
		
		this.sizeTopo = (sizeTopo*72)/2.54f;
		this.sizeRodape = (sizeRodape*72)/2.54f;
		
		realizarGeracaoDocumentoPdf(arquivo);

	}

	private void realizarCriacaoDocumento(String localArquivo) throws FileNotFoundException, DocumentException {
		
		document = new Document(pageSize, marginLeft, marginRight, marginTop+sizeTopo, marginBottom+sizeRodape);
		FileOutputStream file  = new FileOutputStream(localArquivo);
		writer = PdfWriter.getInstance(document, file);		
	}

	private void realizarCriacaoHeaderFooter() {
		HeaderFooterHtmlPage headerFooterHtmlPage = new HeaderFooterHtmlPage(getHeader(), getFooter(), marginTop, marginBottom, marginLeft, marginRight, sizeTopo, sizeRodape);
		writer.setPageEvent(headerFooterHtmlPage);
		headerFooterHtmlPage.onOpenDocument(writer, document);
	}

	private void realizarCriacaoCorpo() throws Exception {
		int x = 2;
		if(x==1){
			ByteArrayInputStream bytes = new ByteArrayInputStream(getBody().toString().getBytes());
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, bytes);
			return;
		}
		ByteArrayInputStream bytes = new ByteArrayInputStream(getBody().toString().getBytes());
		Paragraph boddyPhrase = null;
		CssFile cssFile = null;
		CSSResolver cssResolver =null;
		HtmlPipelineContext htmlContext = null;
//		PdfWriterPipeline pdf = null;
		HtmlPipeline html = null;
		CssResolverPipeline css = null;
		XMLWorker worker = null;
		XMLParser parseXml = null;
		ElementList elements =  null;
		ElementHandlerPipeline pdf = null;
		try{
			boddyPhrase = new Paragraph();
			cssResolver =  XMLWorkerHelper.getInstance().getDefaultCssResolver(true);			
		    cssFile = XMLWorkerHelper.getCSS(new FileInputStream(UteisJSF.getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "css" + File.separator + "otimize.css"));		    
		    cssResolver.addCss(cssFile);		    
		    
			htmlContext = new HtmlPipelineContext(null);
			htmlContext.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
			htmlContext.setImageProvider(new BaseImageProvider());			
			
//			pdf = new PdfWriterPipeline(document, writer);			
			html = new HtmlPipeline(htmlContext, pdf);
			css = new CssResolverPipeline(cssResolver, html);		
			
			elements = new ElementList();			
			pdf = new ElementHandlerPipeline(elements, null);
			html = new HtmlPipeline(htmlContext, pdf);
			css = new CssResolverPipeline(cssResolver, html);
			
			worker = new XMLWorker(css, true);			
			parseXml = new XMLParser(worker);
			parseXml.parse(bytes);
			parseXml.flush();
			boddyPhrase.addAll(elements);
			document.add(boddyPhrase);

		}catch(Exception e){
			throw e;
		}finally{
			if(bytes != null){
				bytes.close();
			}
			if(cssResolver != null){
				cssResolver.clear();
			}
			cssFile = null;
			cssResolver =null;
			htmlContext = null;
			pdf = null;
			html = null;
			
			css = null;
			worker = null;
			parseXml = null;
		}
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
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
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
	 * @return the body
	 */
	public String getBody() {
		if (body == null) {
			body = "";
		}
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
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

	/**
	 * @return the pageSize
	 */
	public Rectangle getPageSize() {
		if (pageSize == null) {
			pageSize = PageSize.A4;
		}
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the marginTop
	 */
	public float getMarginTop() {
		
		return marginTop;
	}

	/**
	 * @param marginTop the marginTop to set
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
	 * @param marginBottom the marginBottom to set
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
	 * @param marginLeft the marginLeft to set
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
	 * @param marginRight the marginRight to set
	 */
	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}

	/**
	 * @param sizeTopo the sizeTopo to set
	 */
	public void setSizeTopo(float sizeTopo) {
		this.sizeTopo = sizeTopo;
	}
	
	class BaseImageProvider extends AbstractImageProvider {
		
		@Override
		public String getImageRootPath() {
			try {
				return UteisJSF.getUrlAplicacaoExterna()+"/";	
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	

}
