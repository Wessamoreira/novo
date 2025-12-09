package negocio.comuns.utilitarias;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.itextpdf.text.Document;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;

public class UnificadorPDF {
	
	public static void realizarUnificacaoPdf(String caminhoPdf1, String caminhoPdf2, String caminhoNovoPdf) throws Exception{
		 List<InputStream> list = new ArrayList<InputStream>();
		 list.add(new FileInputStream(new File(caminhoPdf1)));
        list.add(new FileInputStream(new File(caminhoPdf2)));        
        OutputStream out = new FileOutputStream(new File(caminhoNovoPdf));
        realizarUnificacaoPdf(list, out);
	}
	
	public static void realizarUnificacaoPdf(List<String> caminhoPdfMesclar, String caminhoNovoPdf) throws Exception{
		List<InputStream> list = new ArrayList<InputStream>();
		for(String pdf :  caminhoPdfMesclar) {
			 list.add(new FileInputStream(new File(pdf)));        
		}
       OutputStream out = new FileOutputStream(new File(caminhoNovoPdf));
       realizarUnificacaoPdf(list, out);
	}
	
	public static void realizarUnificacaoListaPdf(List<File> caminhoPdfMesclar, String caminhoNovoPdf) throws Exception{
		List<InputStream> list = new ArrayList<InputStream>();
		OutputStream out = null;
		try {
			for(File pdf :  caminhoPdfMesclar) {
				list.add(new FileInputStream(pdf));			        
			}
			out = new FileOutputStream(new File(caminhoNovoPdf));
			realizarUnificacaoPdf(list, out);
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
				out = null;
			}
			if (Uteis.isAtributoPreenchido(list)) {
				for (InputStream is : list) {
					if (is != null) {
						is.close();
						is = null;
					}
				}
			}
		}
	}

	private static void realizarUnificacaoPdf(List<InputStream> listaInputStreamUnificar, OutputStream outputStream) throws Exception {
		List<ByteArrayInputStream> listaByteArrayInputStream = new ArrayList<>(0);
		for (InputStream inputStream : listaInputStreamUnificar) {
			listaByteArrayInputStream.add(ArquivoHelper.criarByteArrayOutputStream(inputStream));
		}
		validarPdfCriptogrado(listaByteArrayInputStream);
		
		Document document = new Document();
		PdfCopy pdfCopy = new PdfCopy(document, outputStream);
		try {
			document.open();
			for (InputStream in : listaByteArrayInputStream) {
				PdfReader reader = null;
				try {
					try {
						reader = new PdfReader(in);
					} catch (Exception e2) {
						throw new Exception("Não foi possivel realizar a unificação do pdf para assinar, erro ao instância o PdfReader (" + e2.getMessage() + ")");
					}
					for (int i = 1; i <= reader.getNumberOfPages(); i++) {
						pdfCopy.addPage(pdfCopy.getImportedPage(reader, i));
					}
					pdfCopy.freeReader(reader);
					reader.close();
				} catch (Exception e) {
					throw e;
				} finally {
					if (reader != null) {
						reader.close();
						reader = null;
					}
				}
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (InvalidPdfException ipe) {
			if (ipe.getMessage().contains("Unknown encryption")) {
				throw new StreamSeiException("Não foi possível realizar essa operação, pois o arquivo informado esta bloqueado por senha.");
			}
			throw ipe;
		} catch (Exception e) {
			throw e;
		} finally {
			if (Uteis.isAtributoPreenchido(listaInputStreamUnificar)) {
				for (InputStream is : listaInputStreamUnificar) {
					if (is != null) {
						is.close();
						is = null;
					}
				}
			}
			if (Uteis.isAtributoPreenchido(listaByteArrayInputStream)) {
				for (ByteArrayInputStream is : listaByteArrayInputStream) {
					if (is != null) {
						is.close();
						is = null;
					}
				}
			}
			if (pdfCopy != null) {
				pdfCopy.close();
				pdfCopy = null;
			}
			if (document != null) {
				document.close();
				document = null;
			}
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
		}
	}
	
	private static void validarPdfCriptogrado(List<ByteArrayInputStream> listaInputStreamValidarCriptografia) throws Exception {
		if (Uteis.isAtributoPreenchido(listaInputStreamValidarCriptografia)) {
			try {
				for (ByteArrayInputStream inputStream : listaInputStreamValidarCriptografia) {
					inputStream.mark(Integer.MAX_VALUE);
					ArquivoHelper.validarArquivoPdfCriptografado(inputStream);
					inputStream.reset();
				}
			} catch (Exception e) {
				for (ByteArrayInputStream inputStream : listaInputStreamValidarCriptografia) {
					if (inputStream != null) {
						inputStream.close();
						inputStream = null;
					}
				}
				throw e;
			}
		}
	}
}
