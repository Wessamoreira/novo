package negocio.comuns.utilitarias;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.VeraGreenfieldFoundryProvider;
import org.verapdf.pdfa.results.ValidationResult;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfAConformance;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.signatures.SignatureUtil;
import com.spire.pdf.conversion.PdfStandardsConverter;


public class UteisPdfA {
	
	private static PdfOutputIntent intent;
	private static PdfOutputIntent intentIcm;
	
    private static ArquivoHelper arquivo = new ArquivoHelper();
	
	public static Boolean verificaPdfAssinadoDigitalmente(String arquivo) throws Exception {

		Security.setProperty("crypto.policy", "unlimited");
		BouncyCastleProvider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
		PdfDocument pdfDoc = null;
		try {
			pdfDoc = new PdfDocument(new PdfReader(arquivo));
			SignatureUtil signUtil = new SignatureUtil(pdfDoc);
			return Uteis.isAtributoPreenchido(signUtil.getSignatureNames());
		} catch (Exception e) {
			throw e;
		} finally {
			if (pdfDoc != null) {
				pdfDoc.close();
			}
		}

	}
	
	public static String realizarVerificacaoCompatibilidadePDFA(String urlFile) throws Exception {
		if (urlFile.toLowerCase().endsWith(".pdf")) {
			PdfDocument pdf = null;
			try {
				pdf = new PdfDocument(new PdfReader(urlFile));
				if (pdf.getReader().getPdfConformance() == null || !pdf.getReader().getPdfConformance().isPdfAOrUa()) {
					return "";
				}

				return pdf.getReader().getPdfConformance().getAConformance().toString();
			} catch (Exception e) {
				throw e;
			} finally {
				if (pdf != null) {
					pdf.close();
				}
			}
		} else {
			return null;
		}

	}
	
	public static void realizarConversaoPDFA(String origem, String destino, PdfAConformance versao) throws Exception {
        PdfDocument pdfOrigem = new PdfDocument(new PdfReader(origem));
        if (pdfOrigem.getNumberOfPages() > 10) {
            List<String> fileDeletar = new ArrayList<String>(0);
            PdfDocument pdfO = null;
            PdfADocument pdfDestino = null;
            try {
                String origem2 = origem.toLowerCase().replace(".pdf", "NR.pdf");
                String destino2 = destino.toLowerCase().replace(".pdf", "_NR.pdf");
                int nr = 1;
                List<String> fileMesclar = new ArrayList<String>(0);
                pdfO = new PdfDocument(new PdfWriter(origem2.replace("_NR.pdf", "" + nr + ".pdf")));
                PdfFont font = PdfFontFactory.createFont(arquivo.lerArquivoDeFontes("arial.ttf"), PdfEncodings.WINANSI, EmbeddingStrategy.FORCE_EMBEDDED);
                pdfO.addFont(font);
                for (int x = 1; x <= pdfOrigem.getNumberOfPages();) {
                    int y = (x + 9) > pdfOrigem.getNumberOfPages() ? pdfOrigem.getNumberOfPages() : (x + 9);
                    pdfO = new PdfDocument(new PdfWriter(origem2.toLowerCase().replace("_NR.pdf", ""+nr+".pdf")));
                    pdfOrigem.copyPagesTo(x, y, pdfO);
                    int nrPagina = pdfO.getNumberOfPages();
                    pdfO.close();
                    x = y + 1;
                    PdfStandardsConverter converter = new PdfStandardsConverter(origem2.toLowerCase().replace("NR.pdf", "" + nr + ".pdf"));
                    switch (versao) {
                    case PDF_A_1A: {
                        converter.toPdfA1A(destino2.replace("NR.pdf", "" + nr + ".pdf"));
                        break;
                    }
                    case PDF_A_1B: {
                        converter.toPdfA1B(destino2.replace("NR.pdf", "" + nr + ".pdf"));
                        break;
                    }
                    case PDF_A_2A: {
                        converter.toPdfA2A(destino2.replace("NR.pdf", "" + nr + ".pdf"));
                        break;
                    }
                    case PDF_A_2B: {
                        converter.toPdfA2B(destino2.replace("NR.pdf", "" + nr + ".pdf"));
                        break;
                    }
                    case PDF_A_3A: {
                        converter.toPdfA3A(destino2.replace("NR.pdf", "" + nr + ".pdf"));
                        break;
                    }
                    case PDF_A_3B: {
                        converter.toPdfA3B(destino2.replace("NR.pdf", "" + nr + ".pdf"));
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Formato de PDF A não aceito: " + versao.name());
                    }
                    };

                fileDeletar.add(destino2.replace("_NR.pdf", "_" + nr + ".pdf"));
                fileDeletar.add(origem2.replace("_NR.pdf", "_" + nr + ".pdf"));
                fileMesclar.add(destino2.replace("_NR.pdf", "_" + nr + ".pdf"));
                nr++;
                if (nrPagina == 10 && y < pdfOrigem.getNumberOfPages()) {                        
                    pdfO = null;
                    pdfO = new PdfDocument(new PdfWriter(origem2.replace("_NR.pdf", "_" + nr + ".pdf")));
                    pdfO.addFont(font);
                } else {                        
                    pdfO = null;
                }

            }

            WriterProperties props = new WriterProperties();

            PdfOutputIntent intent = versao.equals(PdfAConformance.PDF_A_4) || versao.equals(PdfAConformance.PDF_A_4E) || versao.equals(PdfAConformance.PDF_A_4F) ? getRGBPdfOutputIntentIcm() : getRGBPdfOutputIntent();
            if (versao.equals(PdfAConformance.PDF_A_4) || versao.equals(PdfAConformance.PDF_A_4E) || versao.equals(PdfAConformance.PDF_A_4F)) {
                props.setPdfVersion(PdfVersion.PDF_2_0);
            }

            pdfOrigem.close();
            pdfDestino = new PdfADocument(new PdfWriter(destino, props.addXmpMetadata()), versao,  intent);
            pdfDestino.setTagged();
            
            for (String file : fileMesclar) {
                PdfDocument pdfOrigem2 = new PdfDocument(new PdfReader(file));
                pdfOrigem2.copyPagesTo(1, pdfOrigem2.getNumberOfPages(), pdfDestino, pdfDestino.getNumberOfPages() == 0 ? 1 : pdfDestino.getNumberOfPages() + 1);
                pdfOrigem2.close();
            }
            pdfDestino.close();

        } catch (Exception e) {
            throw e;
        } finally {
            pdfOrigem.close();
            if (pdfO != null && !pdfO.isClosed()) {
                pdfO.close();
                pdfO = null;
            }
            if (pdfDestino != null && !pdfDestino.isClosed()) {
                pdfDestino.close();
                pdfDestino = null;
            }
            fileDeletar.forEach(f -> {
                try {
                    File file = new File(f);
                    if (file.exists() && file.canRead()) {
                        FileUtils.forceDelete(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    } else {
        pdfOrigem.close();
        PdfStandardsConverter converter = new PdfStandardsConverter(origem);
        switch (versao) {
        case PDF_A_1A: {
            converter.toPdfA1A(destino);
            break;
        }
        case PDF_A_1B: {
            converter.toPdfA1B(destino);
            break;
        }
        case PDF_A_2A: {
            converter.toPdfA2A(destino);
            break;
        }
        case PDF_A_2B: {
            converter.toPdfA2B(destino);
            break;
        }
        case PDF_A_3A: {
            converter.toPdfA3A(destino);
            break;
        }
        case PDF_A_3B: {
            converter.toPdfA3B(destino);
            break;
        }
        default: {
            throw new IllegalArgumentException("Formato de PDF A não aceito: " + versao.name());
        }
        }
        ;

    }

}
	/* Método de conversão antigo
	public static void realizarConversaoPDFA(String origem, String destino, PdfAConformance versao) throws Exception {
        PdfDocument pdfOrigem = new PdfDocument(new PdfReader(origem));
        if(pdfOrigem.getNumberOfPages() > 0) {
            List<String> fileDeletar = new ArrayList<String>(0);
            PdfDocument pdfO = null;
            PdfADocument pdfDestino = null;
            try {
            String origem2 = origem.toLowerCase().replace(".pdf", "NR.pdf");
            String destino2 = destino.toLowerCase().replace(".pdf", "_NR.pdf");
            int nr  = 1;
            List<String> fileMesclar = new ArrayList<String>(0);
            pdfO = new PdfDocument(new PdfWriter(origem2.toLowerCase().replace("_NR.pdf", ""+nr+".pdf")));
            PdfFont font  = PdfFontFactory.createFont(arquivo.lerArquivoDeFontes("arial.ttf"), PdfEncodings.WINANSI, EmbeddingStrategy.FORCE_EMBEDDED);
            pdfO.addFont(font);
            for(int x = 1; x < pdfOrigem.getNumberOfPages();) {
                int y = (x + 9) > pdfOrigem.getNumberOfPages() ? pdfOrigem.getNumberOfPages() : (x + 9);
                pdfO = new PdfDocument(new PdfWriter(origem2.toLowerCase().replace("_NR.pdf", ""+nr+".pdf")));
                pdfOrigem.copyPagesTo(x, y, pdfO);
                x = y+1;
                if(pdfO.getNumberOfPages() == 10 && x < pdfOrigem.getNumberOfPages()) {
                    pdfO.close();
                    PdfStandardsConverter converter = new PdfStandardsConverter(origem2.toLowerCase().replace("NR.pdf", ""+nr+".pdf"));
                    converter.toPdfA3B(destino2.replace("NR.pdf", ""+nr+".pdf"));
                    fileDeletar.add(destino2.replace("NR.pdf", ""+nr+".pdf"));
                    fileDeletar.add(origem2.replace("NR.pdf", ""+nr+".pdf"));
                    fileMesclar.add(destino2.replace("NR.pdf", ""+nr+".pdf"));
                    nr++;
                    pdfO = null;
                    pdfO = new PdfDocument(new PdfWriter(origem2.toLowerCase().replace("NR.pdf", ""+nr+".pdf")));
                    pdfO.addFont(font);
                }else if(pdfO.getNumberOfPages() != 10 && x >= pdfOrigem.getNumberOfPages()) {
                    pdfO.close();
                    PdfStandardsConverter converter = new PdfStandardsConverter(origem2.toLowerCase().replace("NR.pdf", ""+nr+".pdf"));
                    converter.toPdfA3B(destino2.replace("NR.pdf", ""+nr+".pdf"));
                    fileMesclar.add(destino2.replace("NR.pdf", ""+nr+".pdf"));
                    fileDeletar.add(origem2.replace("NR.pdf", ""+nr+".pdf"));
                    fileDeletar.add(destino2.replace("NR.pdf", ""+nr+".pdf"));
                    pdfO = null;
                }
            }

            WriterProperties props = new WriterProperties();

        PdfOutputIntent intent = versao.equals(PdfAConformance.PDF_A_4)
                || versao.equals(PdfAConformance.PDF_A_4E) || versao.equals(PdfAConformance.PDF_A_4F)
                        ? getRGBPdfOutputIntentIcm()
                        : getRGBPdfOutputIntent();
        if (versao.equals(PdfAConformance.PDF_A_4) || versao.equals(PdfAConformance.PDF_A_4E)
                || versao.equals(PdfAConformance.PDF_A_4F)) {
            props.setPdfVersion(PdfVersion.PDF_2_0);
        }
        pdfOrigem.close();
        pdfDestino = new PdfADocument(new PdfWriter(destino, props.addXmpMetadata()), versao,  intent);
        pdfDestino.setTagged();
        for(String file: fileMesclar) {    
            PdfDocument pdfOrigem2 = new PdfDocument(new PdfReader(file));
            pdfOrigem2.copyPagesTo(1, pdfOrigem2.getNumberOfPages(), pdfDestino, pdfDestino.getNumberOfPages() == 0 ? 1 : pdfDestino.getNumberOfPages()+1);
            pdfOrigem2.close();                
        }
        pdfDestino.close();
        
        }catch (Exception e) {
            throw e;
        }finally {                
            pdfOrigem.close();
            if(pdfO != null && !pdfO.isClosed()) {                
                pdfO.close();
                pdfO = null;
            }
            if(pdfDestino != null && !pdfDestino.isClosed()) {                
                pdfDestino.close();
                pdfDestino = null;
            }
            fileDeletar.forEach(f -> {
                try {
                    File file = new File(f);
                    if(file.exists() && file.canRead()) {
                        FileUtils.forceDelete(file);
                    }
                } catch (IOException e) {                
                    e.printStackTrace();
                }
            });
        }
    }else {
    	pdfOrigem.close();
        PdfStandardsConverter converter = new PdfStandardsConverter(origem);            
        converter.toPdfA3B(destino);
    }
    pdfOrigem.close();
}
*/
	private static PdfOutputIntent getRGBPdfOutputIntent() throws Exception {
		intent = new PdfOutputIntent("", "", "", "sRGB IEC61966-2.1",
				new FileInputStream(arquivo.lerArquivoDeFontes("sRGB_ICC_v4_Appearance.icc")));
		return intent;
	}

	private static PdfOutputIntent getRGBPdfOutputIntentIcm() throws Exception {
		intentIcm = new PdfOutputIntent("Custom", "", null, "sRGB IEC61966-2.1",
				new FileInputStream(arquivo.lerArquivoDeFontes("sRGB_CS_profile.icm")));
		return intentIcm;
	}
	
	public static boolean realizarVerificacaoCompatibilidadePDFA(ByteArrayInputStream inputStream) throws Exception {
		if (Objects.isNull(inputStream)) {
			return Boolean.FALSE;
		}
		PdfReader pdfReader = null;
		PdfDocument pdfDocument = null;
		try {
			inputStream.mark(Integer.MAX_VALUE);
			pdfReader = new PdfReader(inputStream);
			pdfDocument = new PdfDocument(pdfReader);
			return pdfDocument != null && pdfDocument.getReader() != null && pdfDocument.getReader().getPdfConformance() != null && pdfDocument.getReader().getPdfConformance().isPdfAOrUa() && pdfDocument.getReader().getPdfConformance().getAConformance() != null && pdfDocument.getReader().getPdfConformance().getAConformance().toString().toUpperCase().contains("PDF_A");
		} catch (Exception e) {
			if (Objects.nonNull(inputStream)) {
				inputStream.close();
				inputStream = null;
			}
			throw e;
		} finally {
			if (pdfDocument != null) {
				pdfDocument.close();
			}
			if (pdfReader != null) {
				pdfReader.close();
			}
			if (Objects.nonNull(inputStream)) {
				inputStream.reset();
			}
		}
	}
	
	/**
	 * método com a finalidade de validar o arquivo upado de um usuário se ele está em conformidade
	 * de acordo com o seu tipo de PDF, retornando true caso sim, para os pdf's padrões
	 * geralmente são retornados falso
	 * 
	 * @param arquivoVO
	 * @param inpuStream
	 * @throws Exception
	 * 
	 * @author Felipi Alves
	 * @chamado 44966
	 */
	public static boolean validarArquivoIsConformidade(ByteArrayInputStream inputStream) throws Exception {
		if (Objects.isNull(inputStream)) {
			return false;
		}
		VeraGreenfieldFoundryProvider.initialise();
		PDFAParser parser = null;
		PDFAValidator validator = null;
		ValidationResult result = null;
		try {
			inputStream.mark(Integer.MAX_VALUE);
			parser = Foundries.defaultInstance().createParser(inputStream);
			validator = Foundries.defaultInstance().createValidator(parser.getFlavour(), false);
			result = validator.validate(parser);
			return result.isCompliant();
		} catch (Exception e) {
			if (Objects.nonNull(inputStream)) {
				inputStream.close();
				inputStream = null;
			}
			throw new Exception("O arquivo PDF apresenta inconsistências em sua formatação interna que impedem a validação. Recomendamos gerar um novo arquivo ou solicitar uma versão corrigida do documento.");
		} finally {
			if (Objects.nonNull(result)) {
				result = null;
			}
			if (Objects.nonNull(validator)) {
				validator.close();
				validator = null;
			}
			if (Objects.nonNull(parser)) {
				parser.close();
				parser = null;
			}
			if (Objects.nonNull(inputStream)) {
				inputStream.reset();
			}
		}
	}
	
	public static void validarConformidadeArquivoConvertidoPdfa(File file) throws Exception {
		if (!Uteis.isAtributoPreenchido(file)) {
			return;
		}
		VeraGreenfieldFoundryProvider.initialise();
		FileInputStream inputStream = null;
		PDFAParser parser = null;
		PDFAValidator validator = null;
		ValidationResult result = null;
		try {
			inputStream = new FileInputStream(file);
			parser = Foundries.defaultInstance().createParser(inputStream);
			validator = Foundries.defaultInstance().createValidator(parser.getFlavour(), false);
			result = validator.validate(parser);
			Uteis.checkState(!result.isCompliant(), "Falha na conversão para PDF/A: o processo foi concluído, mas o arquivo convertido não atende aos critérios de conformidade");
		} catch (StreamSeiException sse) {
			throw sse;
		} catch (Exception e) {
			throw new Exception("O arquivo PDF apresenta inconsistências em sua formatação interna que impedem a validação. Recomendamos gerar um novo arquivo ou solicitar uma versão corrigida do documento.");
		} finally {
			if (Objects.nonNull(result)) {
				result = null;
			}
			if (Objects.nonNull(validator)) {
				validator.close();
				validator = null;
			}
			if (Objects.nonNull(parser)) {
				parser.close();
				parser = null;
			}
			if (Objects.nonNull(inputStream)) {
				inputStream.close();
				inputStream = null;
			}
		}
	}
}