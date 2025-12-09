package negocio.comuns.utilitarias;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class ConverterImgToPdf {
	
	
	
	public static void realizarConversaoImgParaPdf(String caminhoImg1, String caminhoImg2, String caminhoNovoPdf) throws Exception{
		 List<String> list = new ArrayList<String>();
		 list.add(caminhoImg1);
		 list.add(caminhoImg2);  
        realizarConversaoPdf(list, caminhoNovoPdf);
	}
	
	
//	public static void realizarConversaoPdf(List<String> list, String caminhoNovoPdf) throws DocumentException, IOException {
//		Document document = new Document(PageSize.A4);
//		PdfWriter.getInstance(document, new FileOutputStream(new File(caminhoNovoPdf)));
//		document.open();
//        for (String f : list) {
//			document.newPage();
//			Image image = Image.getInstance(new File(f).getAbsolutePath());
//			image.setAbsolutePosition(0, 0);
//			image.setBorderWidth(0);
//			image.scaleAbsolute(PageSize.A4);
//				image.scaleAbsoluteHeight(PageSize.A4.getHeight());
//				image.scaleAbsoluteWidth(PageSize.A4.getWidth());
//			document.add(image);
//		}
//		document.close();
//    }
	
	
	public static void realizarConversaoPdf(List<String> list, String caminhoNovoPdf) throws Exception {
		Document document = null;
		int cont = 0;
        for (String f : list) {
        	File file = new File(f);
        	if (!file.exists() || !file.canRead() || (file.getUsableSpace() < 1) || file.getTotalSpace() < 1 || file.length() < 1) {
        		String texto = f.substring(f.lastIndexOf(File.separator));
        		throw new Exception("Não foi possivel localizar ou abrir o arquivo "+texto.replace(File.separator, ""));
        	}
			Image image = Image.getInstance(new File(f).getAbsolutePath());
			image.setBorderWidth(0);
			if(image.getWidth() <= 595 && image.getHeight() <= 842) {
				if(cont == 0) {
					document = new Document();
					PdfWriter.getInstance(document, new FileOutputStream(new File(caminhoNovoPdf)));
					document.open();
				}else {
					document.setPageSize(PageSize.A4);
				}
				image.setAlignment(Element.ALIGN_TOP);
				image.scaleAbsoluteHeight(image.getHeight());
				image.scaleAbsoluteWidth(image.getWidth());
			}else if(image.getWidth() >= image.getHeight()) {
				Rectangle pageSize = new Rectangle(image.getWidth(), image.getHeight());
				if(cont == 0) {
					document = new Document(pageSize);
					PdfWriter.getInstance(document, new FileOutputStream(new File(caminhoNovoPdf)));
					document.open();
				}else {
					document.setPageSize(pageSize);
				}
				image.setAlignment(Element.ALIGN_CENTER);
				image.scaleAbsoluteHeight(image.getHeight());
				image.scaleAbsoluteWidth(image.getWidth());
			}else {
				if(cont == 0) {
					document = new Document(PageSize.A4);
					PdfWriter.getInstance(document, new FileOutputStream(new File(caminhoNovoPdf)));
					document.open();
				}else {
					document.setPageSize(PageSize.A4);
				}
				image.scaleAbsolute(PageSize.A4);
				image.setAbsolutePosition(0, 0);
			}
			document.bottom(100L);
			document.newPage();
			document.add(image);
			cont++;
		}
        
		document.close();
    }

}
