package negocio.comuns.utilitarias;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.mozilla.universalchardet.UniversalDetector;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfAConformance;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.widget.PdfFieldWidget;
import com.spire.pdf.widget.PdfFormWidget;
import com.spire.pdf.widget.PdfSignatureFieldWidget;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

/**
 * Classe responsavel pela manipulacao dos arquivos
 * 
 * @author Diego Renan
 */
@SuppressWarnings({ "resource", "unchecked", "rawtypes" })
@Service
@Lazy
public class ArquivoHelper extends SuperFacadeJDBC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9035919512084698674L;

	private static boolean imageLoaded = false;

	public PrintWriter criarArquivoTexto(String caminho, String nomeArquivo, boolean autoFlush)
			throws FileNotFoundException, IOException {
		FileOutputStream arquivoSaida = new FileOutputStream(caminho + File.separator + nomeArquivo);
		try {
			File arq = new File(caminho + File.separator + nomeArquivo);
			OutputStream OS = (OutputStream) new FileOutputStream(arq);
			OutputStreamWriter OSW = new OutputStreamWriter(OS, "Cp1252");
			PrintWriter saida = new PrintWriter(OSW);
			// PrintWriter saida = new PrintWriter(arquivoSaida, autoFlush);
			return saida;
		} catch (Exception e) {
			try {
				if (arquivoSaida != null) {
					arquivoSaida.close();
				}
			} catch (Exception e1) {
				//// System.out.println("Erro:" + e1.getMessage());
			}
		} finally {
			// if (autoFlush) {
			// arquivoSaida.close();
			// }
		}
		return null;
	}

	public void downloadArquivoTXT(String texto, String nomeArquivo) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		httpServletResponse.reset();

		// tipo arquivo txt
		httpServletResponse.setContentType("text/plain");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\" " + nomeArquivo + ".txt\"");

		OutputStream output = httpServletResponse.getOutputStream();

		String[] teste = texto.split("\n");
		for (String string : teste) {
			string = string + "\r\n";
			output.write(string.getBytes());
		}

		output.flush();
		output.close();

		facesContext.responseComplete();
	}

	/**
	 * Metodo que faz a leitura de um arquivo txt e adiciona cada linha do arquivo a
	 * uma posição de uma lista.
	 * 
	 * @param file - Arquivo txt para leitura dos dados.
	 * @return Uma lista de String que em casa posição da lista contem uma linha do
	 *         arquivo txt.
	 */
	public List<String> lerArquivoTexto(File file) throws IOException {
		List<String> linhasArquivo = new ArrayList<>();

		try (BufferedReader Reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "ISO-8859-1"))) {
			String line = null;
			while ((line = Reader.readLine()) != null) {
				linhasArquivo.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return linhasArquivo;
	}

	/**
	 * 
	 * @param file
	 * @param encode - Ex: UTF-8
	 * @return
	 * @throws IOException
	 */
	public String lerArquivoTexto(File file, String encode) throws IOException {
		StringBuilder linhasArquivo = new StringBuilder();

		try (BufferedReader Reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file.getAbsolutePath()), encode))) {
			String line = null;
			while ((line = Reader.readLine()) != null) {
				linhasArquivo.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return linhasArquivo.toString();
	}

	/**
	 * Pesquisa e retorna todo arquivo ou pasta com o nome desejado
	 * 
	 * @param startingDirectory
	 * @param pattern
	 * @return
	 */
	public List<File> procurarArquivos(final File startingDirectory, final String pattern) {
		List<File> files = new ArrayList<File>();
		if (startingDirectory.isDirectory()) {
			File[] sub = startingDirectory.listFiles(new FileFilter() {

				public boolean accept(File pathname) {
					return pathname.isDirectory() || pathname.getPath().equals(pattern);
				}
			});
			for (File fileDir : sub) {
				if (fileDir.isDirectory()) {
					files.addAll(procurarArquivos(fileDir, pattern));
					if (fileDir.getPath().equals(pattern)) {
						files.add(fileDir);
					}
				} else {
					files.add(fileDir);
				}

			}
		}
		return files;
	}

	/**
	 * Pesquisa todas as expressoes no arquivo inicial.
	 * 
	 * @param arquivoInicial
	 * @param expressoes
	 * @return @
	 */
	public List<File> procurarArquivos(File arquivoInicial, String... expressoes) {
		return procurarArquivos(arquivoInicial, getList(expressoes));
	}

	/**
	 * Pesquisa todas as expressoes no arquivo inicial.
	 * 
	 * @param arquivoInicial
	 * @param expressoes
	 * @return
	 */
	public List<File> procurarArquivos(File arquivoInicial, List<String> expressoes) {
		List<File> files = new ArrayList<File>();
		for (String expressao : expressoes) {
			files.addAll(procurarArquivos(arquivoInicial, expressao));
		}
		return files;
	}

	/**
	 * Copia ou substitui diretorios
	 * 
	 * @param diretorioOrigem
	 * @param diretorioDestino
	 * @throws IOException
	 */
	public void copiarDiretorio(File diretorioOrigem, File diretorioDestino) throws IOException {
		if (diretorioOrigem.isDirectory()) {
			if (!diretorioDestino.exists()) {
				diretorioDestino.mkdirs();
			}

			String[] filho = diretorioOrigem.list();
			for (int i = 0; i < filho.length; i++) {
				copiarDiretorio(new File(diretorioOrigem, filho[i]), new File(diretorioDestino, filho[i]));
			}
		} else {
			copiar(diretorioOrigem, diretorioDestino);
		}
	}

	/**
	 * Substitui arquivos.
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public void copiar(File src, File dest) throws IOException {
		FileInputStream srcInputStream = new FileInputStream(src);
		FileChannel in = null;
		FileChannel out = null;
		try {
			in = srcInputStream.getChannel();
			out = new FileOutputStream(dest).getChannel();
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			in = null;
			out = null;
		}
	}

	/**
	 * Converte array em <code>java.util.List</code>
	 * 
	 * @param lista
	 * @return
	 */
	public List getList(Object[] lista) {
		return Arrays.asList(lista);
	}

	/**
	 * Metodo responsavel por compactar arquivos com a extrutura de arquivos zip com
	 * a extensao desejada.
	 * 
	 * @param parentDirs
	 * @param files
	 * @param out
	 * @throws IOException
	 */
	public void zip(File[] files, File outputFile) throws IOException {

		if (files != null && files.length > 0) {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
			Stack<File> parentDirs = new Stack<File>();
			zipFiles(parentDirs, files, out);
			out.flush();
			out.close();
		}
	}

	private void zipFiles(Stack<File> parentDirs, File[] files, ZipOutputStream out) throws IOException {
		byte[] buf = new byte[1024];
		StringBuilder path = new StringBuilder();
		int len;
		for (int i = 0; i < files.length; i++) {
			if (files[i] != null) {
				if (files[i].isDirectory()) {
					parentDirs.push(files[i]);
					zipFiles(parentDirs, files[i].listFiles(), out);
					parentDirs.pop();
				} else {
					FileInputStream in = new FileInputStream(files[i]);
					path.append("");
					for (File parentDir : parentDirs) {
						path.append(parentDir.getName()).append("/");
					}
					out.putNextEntry(new ZipEntry(path.append(files[i].getName()).toString()));
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
					path = new StringBuilder();
				}
			}
		}
	}

	public static byte[] getArray(File arquivo) throws Exception {
		FileInputStream is = null;
		try {
			byte[] buffer = new byte[(int) arquivo.length()];
			is = new FileInputStream(arquivo);
			is.read(buffer);

			return buffer;
		} catch (Exception e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
				is = null;
			}
		}
	}

	public static boolean deleteRecursivo(File arquivo) {
		if (arquivo.exists()) {
			if (arquivo.isDirectory()) {
				File[] files = arquivo.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteRecursivo(files[i]);
					} else {
						files[i].delete();
					}
				}
				if (arquivo != null && arquivo.exists() && arquivo.listFiles().length == 0) {
					arquivo.delete();
				}
			} else {
				return arquivo.delete();
			}
		}
		return true;
	}

	public static boolean delete(File arquivo) {
		if (arquivo.exists()) {
			if (!arquivo.isDirectory()) {
				return arquivo.delete();
			}
		}
		return true;
	}

	/**
	 * Metodo responsavel por descompactar arquivos .zip ou equivalente
	 * 
	 * @param zipFile
	 * @param dir
	 * @throws IOException
	 */
	public void unzip(File zipFile, File dir) throws IOException {
		ZipFile zip = null;
		File arquivo = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[1024];
		ZipEntry entrada;
		int bytesLidos;

		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!dir.exists() || !dir.isDirectory()) {
				throw new IOException("O diretório " + dir.getName() + " não é um diretorio válido");
			}

			zip = new ZipFile(zipFile);
			Enumeration e = zip.entries();
			while (e.hasMoreElements()) {
				entrada = (ZipEntry) e.nextElement();
				arquivo = new File(dir, entrada.getName());

				if (entrada.isDirectory() && !arquivo.exists()) {
					arquivo.mkdirs();
					continue;
				}

				if (!arquivo.getParentFile().exists()) {
					arquivo.getParentFile().mkdirs();
				}
				try {
					is = zip.getInputStream(entrada);
					os = new FileOutputStream(arquivo);
					bytesLidos = 0;
					if (is == null) {
						throw new ZipException("Erro ao ler a entrada do zip: " + entrada.getName());
					}
					while ((bytesLidos = is.read(buffer)) > 0) {
						os.write(buffer, 0, bytesLidos);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception ex) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception ex) {
							//// System.out.println("Erro:" + ex.getMessage());
						}
					}
				}
			}
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (Exception e) {
					//// System.out.println("Arquivo Helper Erro:" + e.getMessage());

				}
			}
		}
	}

	/**
	 * Rotina que faz o upload do arquivo selecionado pelo usuário na tela, e coloca
	 * esse arquivo numa pasta temporária.
	 * 
	 * @param upload
	 * @param caminhoWebFotos
	 * @param configuracaoGeralSistemaVO
	 * @throws Exception
	 */
	public void upLoadDocumentacaoMatriculaRequerimento(FileUploadEvent upload, ArquivoVO arquivoVO, String novoNome,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			UsuarioVO usuarioVO) throws Exception {
		upLoadDocumentacaoMatriculaRequerimento(upload, upload.getUploadedFile().getInputStream(), arquivoVO, novoNome, configuracaoGeralSistemaVO, pastaBaseArquivoEnum, usuarioVO);
	}
	
	public void upLoadDocumentacaoMatriculaRequerimento(FileUploadEvent upload, InputStream inputStream, ArquivoVO arquivoVO, String novoNome,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		String nomeArquivoSemAcento = "";
		String caminhoAteDiretorio = criarCaminhoPastaAteDiretorio(arquivoVO, pastaBaseArquivoEnum.getValue(),
				configuracaoGeralSistemaVO.getLocalUploadArquivoTemp());

		try {
			dir = new File(caminhoAteDiretorio);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			nomeArquivoSemAcento = criarNomeArquivo(novoNome, pastaBaseArquivoEnum, usuarioVO);
			arquivoVO.setDescricaoArquivo(upload.getUploadedFile().getName());
			arquivoVO.setNome(nomeArquivoSemAcento + "." + upload.getUploadedFile().getName()
					.substring(upload.getUploadedFile().getName().lastIndexOf(".") + 1));
			fileArquivo = new File(dir.getPath() + File.separator + arquivoVO.getNome());
			byte[] buff = new byte[4080];
			InputStream bis = inputStream;
			OutputStream bos = new FileOutputStream(fileArquivo);
			try {
				int tamanho = 0;
				while ((tamanho = bis.read(buff)) > 0) {
					bos.write(buff, 0, tamanho);
				}
				bos.flush();
				bos.close();
				bis.close();
			} finally {
				bis = null;
				bos = null;
				buff = null;
			}
			arquivoVO.setPastaBaseArquivo(arquivoVO.recuperaNomePastaBaseCorrigidoLocalUpload(caminhoAteDiretorio,
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()));
			arquivoVO.setPastaBaseArquivoEnum(pastaBaseArquivoEnum);
			arquivoVO.setUploadRealizado(true);
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
			upload = null;
			nomeArquivoSemAcento = null;
		}
	}

	/**
	 * Método Responsável em aumentar/diminuir a qualidde da iamgem
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void filtrarImg(File file, float[] escala) throws IOException {

		imageLoaded = false;

		String extensao = file.getName().substring(file.getName().lastIndexOf('.') + 1);
		if (extensao.equalsIgnoreCase("pdf")) {
			return;
		}
		ImageObserver myImageObserver = new ImageObserver() {

			@Override
			public boolean imageUpdate(Image image, int flags, int x, int y, int width, int height) {

				if ((flags & ALLBITS) != 0) {

					imageLoaded = true;

					System.out.println("Image loading finished!");

					return false;

				}

				return true;

			}
		};

		Image sourceImage = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());

		sourceImage.getWidth(myImageObserver);

		while (!imageLoaded) {

			try {
				Thread.sleep(100);

			} catch (InterruptedException e) {
			}
		}

		BufferedImage imageBuffer = new BufferedImage(sourceImage.getWidth(null), sourceImage.getHeight(null),
				BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g = imageBuffer.createGraphics();
		g.drawImage(sourceImage, 0, 0, null);
		g.dispose();

		Kernel kernel = new Kernel(3, 3, escala);
		BufferedImageOp op = new ConvolveOp(kernel);
		imageBuffer = op.filter(imageBuffer, null);
		ImageIO.write(imageBuffer, extensao, new File(file.getAbsolutePath()));
	}

	public void diminuirResolucaoImagem(File file, float qualidade) throws IOException {

		String caminhoBase = file.getParentFile().toString();
		String extensao = file.getName().substring(file.getName().lastIndexOf('.') + 1);

		File FileTemp = criarArquivoTemporario(caminhoBase, file);
		BufferedImage image = ImageIO.read(FileTemp);

		OutputStream out = new FileOutputStream(file);
		Iterator ir = ImageIO.getImageWritersByFormatName(extensao);
		ImageWriter writer = (ImageWriter) ir.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(out);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();
		if (param.canWriteCompressed()) {
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(qualidade);
		}

		writer.write(null, new IIOImage(image, null, null), param);

		out.close();
		ios.close();
		writer.dispose();
		FileUtils.forceDelete(FileTemp);

	}

	/**
	 * Rotina Responspavel pro redimencionar o tamanho da imagem
	 * 
	 * @param arquivoVO
	 * @param scaledWidth
	 * @param scaledHeight
	 * @param caminhoOriginal
	 * @throws IOException
	 */
	public void redimensionarImg(ArquivoVO arquivoVO, int scaledWidth, int scaledHeight, String caminhoOriginal)
			throws IOException {
		String extensao = "";
		try {
			BufferedImage imgOriginal = ImageIO.read(new File(caminhoOriginal));

			extensao = arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf('.') + 1);
			BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH);
			Graphics2D g = scaledBI.createGraphics();
			g.drawImage(imgOriginal, 0, 0, scaledWidth, scaledHeight, null);
			g.dispose();
			ImageIO.write(scaledBI, extensao, new File(caminhoOriginal));

		} catch (IOException ex) {
			throw ex;
		} finally {
			extensao = null;
		}
	}

	/**
	 * Rotina responsável em adicionar uma imgem em um documento pdf.
	 * 
	 * @param caminhoImg
	 * @param caminhoPDF
	 * @param caminhoBase
	 * @throws Exception
	 */
	public void adicionarImagemPDF(String caminhoImg, String caminhoPDF, String caminhoBase, int alinhamento,
			int alturaEscala, int larguraEscala, int posicaoX, int posicaoY, boolean ultimaPagina) throws Exception {
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		File arquivoTemp = null;
		FileOutputStream os = null;
		PdfContentByte pdfContentByte = null;

		try {
			File fileParaAssina = new File(caminhoPDF);
			arquivoTemp = criarArquivoTemporario(caminhoBase, fileParaAssina);
			reader = new PdfReader(arquivoTemp.getAbsolutePath());
			os = new FileOutputStream(fileParaAssina);

			pdfStamper = new PdfStamper(reader, os);
			com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(caminhoImg);

			image.setAlignment(alinhamento);
			image.scaleAbsoluteHeight(alturaEscala);
			image.scaleAbsoluteWidth((image.getWidth() * larguraEscala) / image.getHeight());
//			if((reader.getPageSize(ultimaPagina ? reader.getNumberOfPages() : 1).getWidth() < reader.getPageSize(ultimaPagina ? reader.getNumberOfPages() : 1).getHeight()) || reader.getPageRotation(ultimaPagina ? reader.getNumberOfPages() : 1) == 90 || reader.getPageRotation(ultimaPagina ? reader.getNumberOfPages() : 1) == 270) {
//				image.setAbsolutePosition(posicaoY, posicaoX);
//			}else {
				image.setAbsolutePosition(posicaoX, posicaoY);
//			}

			pdfContentByte = pdfStamper.getOverContent(ultimaPagina ? reader.getNumberOfPages() : 1);			
			pdfContentByte.addImage(image);

		} catch (Exception e) {
			throw e;
		} finally {

			if (pdfStamper != null) {
				pdfStamper.close();
			}

			if (os != null) {
				os.close();
			}

			if (reader != null) {
				reader.close();
			}
			FileUtils.forceDelete(arquivoTemp);
		}
	}

	public void adicionarQRCodePDF(String caminhoPDF, String url, int alinhamento, int alturaEscala, int larguraEscala,
			int posicaoX, int posicaoY, boolean ultimaPagina) throws Exception {
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		File arquivoTemp = null;
		FileOutputStream os = null;
		PdfContentByte pdfContentByte = null;

		try {
			File fileParaAssina = new File(caminhoPDF);
			arquivoTemp = criarArquivoTemporario(fileParaAssina.getParent(), fileParaAssina);
			reader = new PdfReader(arquivoTemp.getAbsolutePath());
			os = new FileOutputStream(fileParaAssina);
			pdfStamper = new PdfStamper(reader, os);
			pdfContentByte = pdfStamper.getOverContent(ultimaPagina ? reader.getNumberOfPages() : 1);

			BarcodeQRCode barcodeQRCode = new BarcodeQRCode(url, alturaEscala, larguraEscala, null);
			com.itextpdf.text.Image codeQrImage = barcodeQRCode.getImage();
			codeQrImage.setAlignment(alinhamento);
			codeQrImage.scaleAbsoluteHeight(alturaEscala);
			codeQrImage.scaleAbsoluteWidth((codeQrImage.getWidth() * larguraEscala) / codeQrImage.getHeight());
//			if((reader.getPageSize(ultimaPagina ? reader.getNumberOfPages() : 1).getWidth() < reader.getPageSize(ultimaPagina ? reader.getNumberOfPages() : 1).getHeight()) || reader.getPageRotation(ultimaPagina ? reader.getNumberOfPages() : 1) == 90 || reader.getPageRotation(ultimaPagina ? reader.getNumberOfPages() : 1) == 270) {
//				codeQrImage.setAbsolutePosition(posicaoY, posicaoX);
//			}else {
				codeQrImage.setAbsolutePosition(posicaoX, posicaoY);				
//			}


			pdfContentByte.addImage(codeQrImage);

		} catch (Exception e) {
			throw e;
		} finally {
			if (pdfStamper != null) {
				pdfStamper.close();
			}

			if (os != null) {
				os.close();
			}

			if (reader != null) {
				reader.close();
			}
			FileUtils.forceDelete(arquivoTemp);
		}
	}

	public File criarArquivoTemporario(String caminhoPDFOrigem, File file) throws IOException {
		String extensao = file.getName().substring(file.getName().lastIndexOf('.'));
		File arquivoTemp = new File(caminhoPDFOrigem + File.separator + (new Date().getTime()) + "_temp" + extensao);
		FileUtils.copyFile(file, arquivoTemp);
		return arquivoTemp;
	}

	public String realizarDownloadArquivoAmazon(ArquivoVO obj, String caminhoTemporario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws IOException, ConsistirException {

		File file = new File(caminhoTemporario);
		if (!file.exists()) {
			file.mkdirs();
		}

		String nomeArquivo = caminhoTemporario + File.separator + obj.getNome();
		file = new File(nomeArquivo);
		if (!file.exists()) {
			file.createNewFile();
		}

		copiarArquivoDaAmazonS3ParaServidorLocal(obj.getPastaBaseArquivo() + "/" + obj.getDescricao(), nomeArquivo,
				configuracaoGeralSistemaVO);
		return nomeArquivo;
	}

	public void copiarArquivoDaAmazonS3ParaServidorLocal(String nomeArquivo, String arquivoTemporario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws ConsistirException {
		ServidorArquivoOnlineS3RS servidorAws = new ServidorArquivoOnlineS3RS(
				configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(),
				configuracaoGeralSistemaVO.getNomeRepositorio());
		servidorAws.downloadObjeto(nomeArquivo, arquivoTemporario);
	}

	/**
	 * Rotina que faz o upload do arquivo selecionado pelo usuário na tela, e coloca
	 * esse arquivo numa pasta temporária.
	 * 
	 * @param upload
	 * @param caminhoWebFotos
	 * @param configuracaoGeralSistemaVO
	 * @throws Exception
	 */
	public void upLoadDocumentacaoScanner(String nomeArquivo, ArquivoVO arquivoVO,
			PastaBaseArquivoEnum pastaBaseArquivoEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			UsuarioVO usuarioVO) throws Exception {
		// File arquivoExistente = null;
		File arquivoFinal = null;
		File arquivoScanner = null;
		try {
			// if(arquivoVO.getCodigo() != null && arquivoVO.getCodigo() > 0){
			// arquivoExistente = new
			// File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+arquivoVO.getPastaBaseArquivo()+File.separator+arquivoVO.getNome());
			// }

			arquivoScanner = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.SCANNER.getValue() + File.separator + nomeArquivo);
			if (!arquivoScanner.exists()) {
				throw new Exception("Não foi localizado o arquivo scaneado, tente novamente.");
			}

			arquivoFinal = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getCpfAlunoDocumentacao());
			if (!arquivoFinal.exists()) {
				arquivoFinal.mkdirs();
			}

			byte[] buff = new byte[4080];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(arquivoScanner));
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(arquivoFinal.getAbsoluteFile() + File.separator + nomeArquivo));
			try {
				int tamanho = 0;
				while ((tamanho = bis.read(buff)) > 0) {
					bos.write(buff, 0, tamanho);
				}
				bos.flush();
				bos.close();
				bis.close();
			} finally {
				bis = null;
				bos = null;
				buff = null;
			}

			arquivoVO.setNome(nomeArquivo);
			arquivoVO.setExtensao("jpg");
			arquivoVO.setPastaBaseArquivo(
					pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getCpfAlunoDocumentacao());
			arquivoVO.setPastaBaseArquivoEnum(pastaBaseArquivoEnum);
			arquivoVO.setUploadRealizado(true);
			arquivoScanner.delete();
			// if(arquivoExistente != null && arquivoExistente.exists()){
			// arquivoExistente.delete();
			// }
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	public void upLoadLista(FileUploadEvent upload, List<ArquivoVO> listaArquivoVOs,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum, Boolean renderizarImagem,
			boolean validarTamanhoAnexo, UsuarioVO usuarioVO) throws Exception {
		if (validarTamanhoAnexo) {
			getFacadeFactory().getComunicacaoInternaFacade().realizarValidacaoTamanhoAnexosEmail(upload.getUploadedFile(), listaArquivoVOs, configuracaoGeralSistemaVO);
		}
		ArquivoVO arquivo = new ArquivoVO();
		upLoad(upload, arquivo, configuracaoGeralSistemaVO, pastaBaseArquivoEnum, usuarioVO);
		if (renderizarImagem) {
			arquivo.setCaminhoImagemAnexo(renderizarAnexo(arquivo, arquivo.getPastaBaseArquivo(), configuracaoGeralSistemaVO, "", "", false));
			arquivo.setExtensao(getExtensaoArquivo(arquivo.getNome()));
		}
		
		listaArquivoVOs.add(arquivo);
	}

	public String getArquivoUploadFoto(ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			PastaBaseArquivoEnum pastaBaseArquivoEnum, UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		String caminhoAteDiretorio = "";

		try {
			caminhoAteDiretorio = criarCaminhoPastaAteDiretorio(arquivoVO, pastaBaseArquivoEnum.getValue(),
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp());
			dir = new File(caminhoAteDiretorio);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			arquivoVO.setNome(criarNomeArquivo("", pastaBaseArquivoEnum, usuarioVO) + ".jpg");
			arquivoVO.setPastaBaseArquivo(arquivoVO.recuperaNomePastaBaseCorrigidoLocalUpload(caminhoAteDiretorio,
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()));
			arquivoVO.setPastaBaseArquivoEnum(pastaBaseArquivoEnum);
			arquivoVO.setUploadRealizado(true);
			return dir.getPath() + File.separator + arquivoVO.getNome();

		} catch (Exception e) {
			throw e;
		}
	}

	public void validarTamanhoSelo(FileUploadEvent uploadEvent) throws Exception {
		InputStream inputStream = null;
		Image image = null;
		try {
			inputStream = uploadEvent.getUploadedFile().getInputStream();
			image = ImageIO.read(inputStream);
			if (!(image.getWidth(null) <= 35) || !(image.getHeight(null) <= 35)) {
				throw new Exception("A altura e largura do Selo é maior que 35px");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			image = null;
			if (inputStream != null) {
				inputStream.close();
			}
			inputStream = null;
		}
	}

	public void upLoad(FileUploadEvent upload, ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			UsuarioVO usuarioVO) throws Exception {
		upLoad(upload, upload.getUploadedFile().getInputStream(), arquivoVO, configuracaoGeralSistemaVO, pastaBaseArquivoEnum, usuarioVO);
	}
	
	public void upLoad(FileUploadEvent upload, InputStream inputStream, ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		String nomeArquivoSemAcento = "";
		String localUplod = "";
		String caminhoAteDiretorio = "";

		try {
			localUplod = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.CERTIFICADO)
					|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED)
					|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.EAD_QUESTOES)) {
				localUplod = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
			}

			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.CERTIFICADO)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ pastaBaseArquivoEnum.getValue());
			}

			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.CERTIFICADO_NFE)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ pastaBaseArquivoEnum.getValue());
			}

			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator
						+ UteisData.getDataMesAnoConcatenado());
			}
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.EAD_QUESTOES)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.EAD.getValue() + File.separator + pastaBaseArquivoEnum.getValue()
						+ File.separator + arquivoVO.getDisciplina().getCodigo());
			}
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.PROCESSO_SELETIVO_QUESTOES)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.PROCESSO_SELETIVO_QUESTOES.getValue() + File.separator
						+ arquivoVO.getCodOrigem());
			}
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.LAYOUT_HISTORICO_TMP)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.LAYOUT_HISTORICO_TMP.getValue() + File.separator
						+ arquivoVO.getNivelEducacional() + File.separator + arquivoVO.getTipoRelatorio().name());
			}
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.LAYOUT_HISTORICO)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.LAYOUT_HISTORICO.getValue() + File.separator
						+ arquivoVO.getNivelEducacional() + File.separator + arquivoVO.getTipoRelatorio().name());
			}
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS_TMP)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS_TMP.getValue() + File.separator + arquivoVO.getTipoRelatorio().name());
			}
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.getValue() + File.separator + arquivoVO.getTipoRelatorio().name());
			}

			caminhoAteDiretorio = criarCaminhoPastaAteDiretorio(arquivoVO, pastaBaseArquivoEnum.getValue(), localUplod);
			dir = new File(caminhoAteDiretorio);
			if ((pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP)
					|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA))) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
						+ pastaBaseArquivoEnum.getValue());
			}

			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (arquivoVO.getOrigem().equals(OrigemArquivo.TEXTO_PADRAO.getValor()) || arquivoVO.getOrigem().equals(OrigemArquivo.TEXTO_PADRAO_DECLARACAO.getValor()) || arquivoVO.getOrigem().equals(OrigemArquivo.LAYOUT_RELATORIO_SEI_DECIDIDIR.getValor())  || arquivoVO.getOrigem().equals(PastaBaseArquivoEnum.LAYOUT_HISTORICO.name()) || arquivoVO.getOrigem().equals(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.name())) {
				arquivoVO.setNome(upload.getUploadedFile().getName());
			} else {
				nomeArquivoSemAcento = criarNomeArquivo(upload.getUploadedFile().getName().substring(0,
						upload.getUploadedFile().getName().lastIndexOf(".")), pastaBaseArquivoEnum, usuarioVO);
				arquivoVO.setNome(nomeArquivoSemAcento + "." + upload.getUploadedFile().getName()
						.substring(upload.getUploadedFile().getName().lastIndexOf(".") + 1));
			}
			arquivoVO.setDescricao(upload.getUploadedFile().getName());
			if (arquivoVO.getDescricaoArquivo().trim().isEmpty()) {
				arquivoVO.setDescricaoArquivo(upload.getUploadedFile().getName());
			}
			arquivoVO.setDataUpload(new Date());
			arquivoVO.setTamanhoArquivo(upload.getUploadedFile().getSize());
			fileArquivo = new File(dir.getPath() + File.separator + arquivoVO.getNome());

			InputStream is = inputStream;
			OutputStream out = new FileOutputStream(fileArquivo);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();
			upload.getUploadedFile().delete();
			arquivoVO.setPastaBaseArquivo(
					arquivoVO.recuperaNomePastaBaseCorrigidoLocalUpload(caminhoAteDiretorio, localUplod));
			arquivoVO.setPastaBaseArquivoEnum(pastaBaseArquivoEnum);
			arquivoVO.setUploadRealizado(true);
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
			// upload = null;
			nomeArquivoSemAcento = null;
		}
	}

	public void uploadArquivosComuns(FileUploadEvent upload, ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		String nomeArquivoSemAcento = "";
		String caminhoAteDiretorio = criarCaminhoPastaAteDiretorio(arquivoVO, pastaBaseArquivoEnum.getValue(),
				configuracaoGeralSistemaVO.getLocalUploadArquivoTemp());
		try {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getNivelEducacional());

			if (!dir.exists()) {
				dir.mkdirs();
			}
			nomeArquivoSemAcento = criarNomeArquivo(upload.getUploadedFile().getName().substring(0,
					upload.getUploadedFile().getName().lastIndexOf(".")), pastaBaseArquivoEnum, usuarioVO);
			arquivoVO.setNome(nomeArquivoSemAcento + "." + upload.getUploadedFile().getName()
					.substring(upload.getUploadedFile().getName().lastIndexOf(".") + 1));
			arquivoVO.setDescricao(upload.getUploadedFile().getName());
			fileArquivo = new File(dir.getPath() + File.separator + arquivoVO.getNome());

			InputStream is = upload.getUploadedFile().getInputStream();
			OutputStream out = new FileOutputStream(fileArquivo);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();
			upload.getUploadedFile().delete();
			arquivoVO.setPastaBaseArquivo(arquivoVO.recuperaNomePastaBaseCorrigidoLocalUpload(caminhoAteDiretorio,
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()));
			arquivoVO.setPastaBaseArquivoEnum(pastaBaseArquivoEnum);
			arquivoVO.setUploadRealizado(true);
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
			nomeArquivoSemAcento = null;
		}
	}

	public void uploadArquivoIntegracaoFinanceiro(File file, ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File copiaArquivo = null;
		try {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
					+ arquivoVO.getPastaBaseArquivoEnum().getValue());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			copiaArquivo = new File(dir.getPath() + File.separator + arquivoVO.getNome());
			FileUtils.copyFile(file, copiaArquivo);
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			copiaArquivo = null;
		}
	}

	public void executarCriacaoNovoDiretorioUnificacaoDisciplina(Integer disciplinaExcluir,
			Integer disciplinaPermanecer, List<String> listaNomeArquivo,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum)
			throws Exception {
		File arquivoOrigem = null;
		File arquivoDestino = null;
		File fileArquivo = null;

		try {
			arquivoOrigem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
					+ pastaBaseArquivoEnum.getValue() + File.separator + disciplinaExcluir);
			for (String nomeArquivo : listaNomeArquivo) {

				fileArquivo = new File(arquivoOrigem.getPath() + File.separator + nomeArquivo);

				if (fileArquivo.exists()) {
					arquivoDestino = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
							+ pastaBaseArquivoEnum.getValue() + File.separator + disciplinaPermanecer);
					if (!arquivoDestino.exists()) {
						arquivoDestino.mkdir();
					}
					FileUtils.copyFile(fileArquivo, new File(arquivoDestino, fileArquivo.getName()));
				}
			}
			if (arquivoOrigem.exists()) {
				arquivoOrigem.delete();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			arquivoOrigem = null;
			arquivoDestino = null;
			fileArquivo = null;
		}
	}

	/**
	 * Manipula o nome do arquivo, retirando a acentuação, retirando o nome do
	 * caminho, substituindo os espaços por underline, e concatenando o tempo em
	 * milissegundos se for uma imagem para torná-la única no diretório.
	 * 
	 * @param nomeArquivo
	 * @return
	 */
	public static String criarNomeArquivo(UsuarioVO usuarioVO, String extensao) {
		return usuarioVO.getCodigo() + "_" + new Date().getTime() + "." + extensao;
	}

	public static String getExtensaoArquivo(String nomeArquivo) {
		return nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);
	}

	public static String criarNomeArquivo(String nomeArquivo, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			UsuarioVO usuarioVO) {
		return (usuarioVO == null ? 0 : usuarioVO.getCodigo()) + "_" + new Date().getTime();
		// if(usuarioVO.getDisciplina().getCodigo()>0 &&
		// arquivoVO.getProfessor().getCodigo()>0){
		// return
		// arquivoVO.getDisciplina().getCodigo()+"_"+arquivoVO.getProfessor().getCodigo()+"_"+new
		// Date().getTime();
		// }
		// if(arquivoVO.getDisciplina().getCodigo()>0){
		// return arquivoVO.getDisciplina().getCodigo()+"_"+new
		// Date().getTime();
		// }
		//
		// StringBuffer nomeArquivoBuffer = new
		// StringBuffer(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(Uteis.removerAcentuacao(nomeArquivo)));
		// if (nomeArquivoBuffer.toString().contains("\\")) {
		// nomeArquivoBuffer = new
		// StringBuffer(nomeArquivoBuffer.substring(nomeArquivoBuffer.lastIndexOf("\\")
		// + 1, nomeArquivoBuffer.length()));
		// } else if (nomeArquivoBuffer.toString().contains("/")) {
		// nomeArquivoBuffer = new
		// StringBuffer(nomeArquivoBuffer.substring(nomeArquivoBuffer.lastIndexOf("/")
		// + 1, nomeArquivoBuffer.length()));
		// }
		// String nomeArquivoFinal = nomeArquivoBuffer.toString();
		// try {
		// nomeArquivoFinal = nomeArquivoFinal.replace(" ", "_");
		// if ((pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.IMAGEM) ||
		// pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.IMAGEM_TMP)) ||
		// (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.REQUERIMENTOS) ||
		// pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)))
		// {
		// long tempoEmMilissegundos = new Date().getTime();
		// nomeArquivoFinal = tempoEmMilissegundos + nomeArquivoFinal;
		// }
		// return nomeArquivoFinal.trim();
		// } finally {
		// nomeArquivoBuffer = null;
		// }
	}

	/**
	 * Método recebe um arquivo (File) e o escreve no HD.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void escreverArquivo(File file) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buffer[] = new byte[(int) file.length()];
		int bytesRead = 0;
		FileInputStream fis = new FileInputStream(file);
		try {
			bytesRead = fis.read(buffer);
			if (bytesRead != file.length()) {
				throw new IOException("Erro ao ler o arquivo...");
			}
			baos.write(buffer, 0, bytesRead);
		} finally {
			if (baos != null) {
				baos.flush();
				baos.close();
			}
			if (fis != null) {
				fis.close();
			}
			baos = null;
			fis = null;
			buffer = null;
		}
	}

	public File escreverArquivo(InputStream uploadedInputStream, String uploadedFileLocation) {
		File arquivo = new File(uploadedFileLocation);
		OutputStream out = null;
		try {
			out = new FileOutputStream(arquivo);
			int read = 0;
			byte[] bytes = new byte[4096];
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
					out = null;
				}
				if (uploadedInputStream != null) {
					uploadedInputStream.close();
					uploadedInputStream =  null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return arquivo;
	}

	/**
	 * Método que procura o local da imagem de acordo com as configurações do
	 * arquivo e renderiza a imagem na tela.
	 * 
	 * @param out
	 * @param arquivoVO
	 * @param configuracaoGeralSistemaVO
	 * @param caminhoPastaWeb
	 * @throws IOException
	 */
	public void renderizarImagemNaTela(OutputStream out, ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoPastaWeb, String imagemDefault)
			throws IOException {
		BufferedImage bufferedImage = null;
		File file = null;
		try {
			if (arquivoVO.getNome() != null && !arquivoVO.getNome().equals("")) {
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + "imagem"
						+ File.separator + arquivoVO.getNome());
				bufferedImage = ImageIO.read(file);
				ImageIO.write(bufferedImage, "jpg", out);
			}

		} catch (IIOException e) {
			try {
				file = null;
				file = buscarArquivoDiretorioFixo(arquivoVO, configuracaoGeralSistemaVO);
				bufferedImage = ImageIO.read(file);
				ImageIO.write(bufferedImage, "jpg", out);
			} catch (IIOException ex) {
				file = null;
				file = new File(caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens"
						+ File.separator + "visao" + File.separator + imagemDefault);
				bufferedImage = ImageIO.read(file);
				ImageIO.write(bufferedImage, "png", out);

			} finally {
				file = null;
				bufferedImage = null;
			}

		} finally {
			file = null;
			bufferedImage = null;
		}
	}

	public String renderizarAnexo(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoPastaWeb, String imagemDefault,
			Boolean upLoadImagem) throws Exception {
		String caminhoArquivo;
		String nomeArquivo = arquivoVO.getNome();
		File arquivoImagem = null;
		if (!arquivoVO.getNome().equals("")) {
			if (configuracaoGeralSistemaVO == null) {
				return "/" + pastaBase + "/" + arquivoVO.getNome();
			}
			if (configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo().endsWith("/")) {
				caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + pastaBase + "/";
			} else {
				caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + pastaBase + "/";
			}
			if (!upLoadImagem) {
				arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ pastaBase + File.separator + nomeArquivo);
				if (arquivoImagem.exists()) {
					return caminhoArquivo + "/" + nomeArquivo;
				}
			}
		}
		return "";
	}

	public String renderizarFotoUsuario(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoPastaWeb, String imagemDefault,
			Boolean upLoadImagem) {
		try {
			String caminhoArquivo;
			String nomeArquivo = arquivoVO.getNome();
			File arquivoImagem = null;
			if (Uteis.isAtributoPreenchido(arquivoVO.getPastaBaseArquivo())
					&& arquivoVO.getPastaBaseArquivo().contains(File.separator + "imagem" + File.separator)) {
				arquivoImagem = new File(arquivoVO.getPastaBaseArquivo() + File.separator + nomeArquivo);
				caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + File.separator + pastaBase
						+ File.separator;
				if (arquivoImagem.exists()) {
					String caminho = arquivoVO.getPastaBaseArquivo().substring(
							arquivoVO.getPastaBaseArquivo().lastIndexOf(File.separator + "imagem" + File.separator) + 8,
							arquivoVO.getPastaBaseArquivo().length());
					return caminhoArquivo + File.separator + caminho + File.separator + nomeArquivo;
				}
			}
			if (!arquivoVO.getNome().equals("")) {
				if (configuracaoGeralSistemaVO == null) {
					return "/" + pastaBase + "/" + arquivoVO.getNome();
				}
				if (configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo().endsWith("/")) {
					caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + pastaBase + "/";
				} else {
					caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + pastaBase + "/";
				}
				if (!upLoadImagem) {
					arquivoImagem = new File(
							configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
									+ File.separator + arquivoVO.getCpfRequerimento() + File.separator + nomeArquivo);
					if (arquivoImagem.exists()) {
						return caminhoArquivo + arquivoVO.getCpfRequerimento() + "/" + nomeArquivo;
					} else {
						arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
								+ pastaBase + File.separator + File.separator + nomeArquivo);
						if (arquivoImagem.exists()) {
							return caminhoArquivo + "/" + nomeArquivo;
						} else {
							if(Uteis.isAtributoPreenchido(imagemDefault)) {
								return caminhoPastaWeb + "/" + "resources" + "/" + "imagens" + "/" + imagemDefault;
							}else {
								return "";
							}
						}
					}
				} else {
					if (Uteis.isAtributoPreenchido(arquivoVO.getCpfRequerimento())) {
						return caminhoArquivo + arquivoVO.getCpfRequerimento() + "/" + nomeArquivo;
					} else {
						return caminhoArquivo + "/" + nomeArquivo;
					}
				}
			} else {
				if(Uteis.isAtributoPreenchido(imagemDefault)) {
					return caminhoPastaWeb + "/" + "resources" + "/" + "imagens" + "/" + imagemDefault;
				}else {
					return "";
				}
			}
		} finally {
		}
	}

	public void executarZoomImagemRequerimento(String tipoZoom, ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		String diretorioFoto = "";
		try {
			if (arquivoVO.getPastaBaseArquivo().contains(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp())) {
				diretorioFoto = arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome();
			} else {
				if (arquivoVO.getPastaBaseArquivo().contains(arquivoVO.getCpfRequerimento())) {
					diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome();
				} else {
					diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getCpfRequerimento()
							+ File.separator + arquivoVO.getNome();
				}
			}
			if (arquivoVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue())) {
				diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator
						+ UteisData.getDataMesAnoConcatenado() + File.separator + arquivoVO.getNome();
			}
			executarZoomImagem(tipoZoom, diretorioFoto, arquivoVO.getNome()
					.substring(arquivoVO.getNome().lastIndexOf(".") + 1, arquivoVO.getNome().length()));
			arquivoVO.setDataUpload(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			diretorioFoto = "";
		}
	}

	public void executarZoomImagem(String tipoZoom, String diretorioFoto, String extensao) {
		File arquivoImagem = null;
		BufferedImage imagemOriginal = null;
		BufferedImage resizedImage = null;
		int newImageWidth = 0;
		int newImageHeight = 0;

		try {
			arquivoImagem = new File(diretorioFoto);
			imagemOriginal = ImageIO.read(arquivoImagem);

			if (tipoZoom.equals("in")) {
				newImageWidth = Double.valueOf(imagemOriginal.getWidth() + (imagemOriginal.getWidth() * 0.1))
						.intValue();
				newImageHeight = Double.valueOf(imagemOriginal.getHeight() + (imagemOriginal.getHeight() * 0.1))
						.intValue();
			}

			if (tipoZoom.equals("out")) {
				newImageWidth = Double.valueOf(imagemOriginal.getWidth() - (imagemOriginal.getWidth() * 0.1))
						.intValue();
				newImageHeight = Double.valueOf(imagemOriginal.getHeight() - (imagemOriginal.getHeight() * 0.1))
						.intValue();
			}

			resizedImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = resizedImage.createGraphics();
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(imagemOriginal, 0, 0, newImageWidth, newImageHeight, null);
			g.dispose();
			ImageIO.write(resizedImage, extensao, arquivoImagem);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resizedImage != null) {
				resizedImage.flush();
			}
			if (imagemOriginal != null) {
				imagemOriginal.flush();
			}
			arquivoImagem = null;
			imagemOriginal = null;
			resizedImage = null;
			diretorioFoto = "";
			newImageWidth = 0;
			newImageHeight = 0;
		}
	}

	public String renderizarImagemRequerimento(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoPastaWeb, String imagemDefault,
			PastaBaseArquivoEnum pastaBaseArquivoEnum) {
		// File file = null;
		try {
			if (!arquivoVO.getNome().equals("")) {

				return configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + pastaBaseArquivoEnum.getValue()
						+ "/" + arquivoVO.getCpfRequerimento() + "/" + arquivoVO.getNome() + "?UID="
						+ new Date().getTime();
				// return
				// configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() +
				// "/" + pastaBase + "/" + arquivoVO.getNome();
			} else {
				return "";
			}
		} finally {
			// file = null;
		}
	}

	public Boolean verificarExistenciaArquivoDisk(File file, ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		file = new File(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + pastaBase + "/"
				+ arquivoVO.getNome());
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public String renderizarFotoPadrao(String pastaBase, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			String imagemDefault) {
		return configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + pastaBase + "/" + imagemDefault;
	}

	/**
	 * Rotina que inclui o arquivo no diretório montado e configurado no sistema.
	 * 
	 * @param arquivoVO
	 * @param pastaBase
	 * @param configuracaoGeralSistemaVO
	 * @throws Exception
	 */
	public void incluirArquivoNoDiretorioPadrao(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File diretorioTemp;
		File diretorioFixo;
		File arquivoTemporario;
		File arquivoFixo;
		try {
			if (pastaBase.equals("arquivo")) {
				validarDadosExistenciaArquivoNomeDuplicadoDisco(arquivoVO, pastaBase, configuracaoGeralSistemaVO);
			}
			String caminhoAteDiretorioTemp = criarCaminhoPastaAteDiretorio(arquivoVO, arquivoVO.getPastaBaseArquivo(),
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp());
			String caminhoAteDiretorioFixo = criarCaminhoPastaAteDiretorio(arquivoVO, pastaBase,
					configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
			diretorioTemp = new File(caminhoAteDiretorioTemp);
			if (!diretorioTemp.exists()) {
				diretorioTemp.mkdirs();
			}
			diretorioFixo = new File(caminhoAteDiretorioFixo);
			if (!diretorioFixo.exists()) {
				diretorioFixo.mkdirs();
			}
			arquivoTemporario = new File(diretorioTemp.getPath(), arquivoVO.getNome());
			arquivoFixo = new File(diretorioFixo.getPath(), arquivoVO.getNome());
			copiar(arquivoTemporario, arquivoFixo);
			if (arquivoVO.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.APACHE) && !getFacadeFactory().getArquivoHelper().executarVerificacaoExistenciaArquivoDisco(arquivoVO, arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO)) {
			    throw new Exception("Arquivo não localizado. Por favor, faça o upload novamente.");
			}
			arquivoVO.setPastaBaseArquivo(arquivoVO.recuperaNomePastaBaseCorrigido(caminhoAteDiretorioFixo,
					configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()));
			arquivoTemporario.delete();
		} catch (Exception e) {
			throw e;
		} finally {
			diretorioTemp = null;
			arquivoTemporario = null;
			arquivoFixo = null;
		}
	}

	public void copiarArquivoNaAmazonS3(File arquivoTemporario) {
		ServidorArquivoOnlineS3RS servidorAws = new ServidorArquivoOnlineS3RS("", "", "");
		try {
			/**
			 * TODO: Implementar metodos que consomem recursos da classe
			 * ServidorArquivoOnlineS3RS Ticket Otimize Project: SEI-CA452.23
			 */

			servidorAws.uploadObjeto("teste", arquivoTemporario, false);
		} catch (ConsistirException e) {
			e.printStackTrace();
		}
	}

	public void incluirPastaBaseArquivoImagem(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File diretorioASerIncluido = criarCaminhoPastaAteDiretorioFixoDeInclusaoArquivos(arquivoVO, pastaBase,
				configuracaoGeralSistemaVO);
		File fileTmp = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + pastaBase
				+ "TMP" + File.separator + arquivoVO.getNome());
		try {
			arquivoVO.setPastaBaseArquivo(diretorioASerIncluido.getAbsolutePath());
			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM);
			fileTmp.delete();
		} catch (Exception e) {
			throw e;
		} finally {
			diretorioASerIncluido = null;
			fileTmp = null;
		}

	}

	public Boolean executarVerificacaoExistenciaArquivoDisco(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File arquivoHD = null;
		File diretorioASerIncluido = criarCaminhoPastaAteDiretorioFixoDeInclusaoArquivos(arquivoVO, pastaBase,
				configuracaoGeralSistemaVO);
		try {
			arquivoHD = new File(diretorioASerIncluido.getPath() + File.separator + arquivoVO.getNome());

			return arquivoHD.exists();

		} catch (Exception e) {
			throw e;
		} finally {
			arquivoHD = null;
			diretorioASerIncluido = null;
		}
	}

	public void validarDadosExistenciaArquivoDisco(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File arquivoHD = null;
		File diretorioASerIncluido = criarCaminhoPastaAteDiretorioFixoDeInclusaoArquivos(arquivoVO, pastaBase,
				configuracaoGeralSistemaVO);
		try {
			arquivoHD = new File(diretorioASerIncluido.getPath() + File.separator + arquivoVO.getNome());

			if (!arquivoHD.exists()) {
				throw new Exception(
						"O arquivo não pôde ser disponibilizado, por favor, tente novamente. Se o problema persistir entre em contato com o suporte técnico.");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			arquivoHD = null;
			diretorioASerIncluido = null;
		}
	}

	public void validarDadosExistenciaArquivoNomeDuplicadoDisco(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File arquivoHD = null;
		File diretorioASerIncluido = criarCaminhoPastaAteDiretorioFixoDeInclusaoArquivos(arquivoVO, pastaBase,
				configuracaoGeralSistemaVO);
		try {
			arquivoHD = new File(diretorioASerIncluido.getPath() + File.separator + arquivoVO.getNome());

			if (arquivoHD.exists()) {
				throw new Exception("Nome de arquivo já existente (" + arquivoVO.getNome()
						+ "). Por favor altere o nome do arquivo para concluir o upload.");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			arquivoHD = null;
			diretorioASerIncluido = null;
		}
	}

	public void incluirArquivoNoDiretorioPadraoArquivosComuns(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File diretorioASerIncluido = criarCaminhoPastaAteDiretorioFixoDeInclusaoArquivos(arquivoVO, pastaBase,
				configuracaoGeralSistemaVO);
		File arquivoTemporario;
		File arquivoFixo;
		File fileTmp = null;
		try {
			if (configuracaoGeralSistemaVO.getLocalUploadArquivoFixo().equals("")) {
				arquivoTemporario = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
						+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome());
				arquivoFixo = new File(diretorioASerIncluido.getPath() + File.separator + arquivoVO.getNome());
				arquivoVO.setPastaBaseArquivo(pastaBase);
				arquivoVO.setNome(arquivoVO.getNome());
			} else {
				arquivoTemporario = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
						+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome());
				arquivoFixo = new File(diretorioASerIncluido.getPath() + File.separator + arquivoVO.getNome());
				arquivoVO.setPastaBaseArquivo(
						arquivoVO.recuperaNomePastaBaseCorrigidoLocalUpload(diretorioASerIncluido.getAbsolutePath(),
								configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()));
				arquivoVO.setNome(arquivoVO.getNome());
			}
			copiar(arquivoTemporario, arquivoFixo);
			if (arquivoVO.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.APACHE) && !getFacadeFactory().getArquivoHelper().executarVerificacaoExistenciaArquivoDisco(arquivoVO, arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO)) {
			    throw new Exception("Arquivo não localizado. Por favor, faça o upload novamente.");
			}
			// fileTmp = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() +
			// File.separator + pastaBase+ "TMP" + File.separator +
			// arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome());
			arquivoTemporario.delete();

		} catch (Exception e) {
			throw e;
		} finally {
			diretorioASerIncluido = null;
			arquivoTemporario = null;
			arquivoFixo = null;
			fileTmp = null;
		}
	}

	public void removerArquivoDiretorio(Boolean localArquivoFixo, ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File file = null;

		try {
			if (localArquivoFixo) {
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
						+ File.separator + arquivoVO.getNome());
			} else {
				file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + pastaBase
						+ "TMP" + File.separator + arquivoVO.getNome());
			}
			if (file.exists()) {
				file.delete();
			}

		} catch (Exception e) {
			throw e;
		} finally {
			file = null;
		}
	}

	public File buscarArquivoDiretorioFixo(ArquivoVO obj, ConfiguracaoGeralSistemaVO config) {
		File file = new File(obj.getPastaBaseArquivo() + File.separator + obj.getNome());
		// Validacao para verificar se o arquivo existe no caminho salvo na tabela
		// arquivo pois existe arquivo que nao e salvo o caminho completo
		if (!file.exists() && ((!Uteis.isSistemaOperacionalWindows() && !obj.getPastaBaseArquivo().startsWith("/"))
				|| (Uteis.isSistemaOperacionalWindows() && !obj.getPastaBaseArquivo().contains(":")))) {
			file = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getPastaBaseArquivo()
					+ File.separator + obj.getNome());
		}
		return file;
	}

	public String criarCaminhoPastaAteDiretorio(ArquivoVO arquivoVO, String pastaBase, String localUpload) {
		if (localUpload.endsWith("/")) {
			localUpload = localUpload.substring(0, localUpload.length() - 1);
		}
		String caminhoAteDiretorio;
		if ((pastaBase.equals(PastaBaseArquivoEnum.ARQUIVO.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.ARQUIVO_TMP.getValue()))) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase;
			if (Uteis.isAtributoPreenchido(arquivoVO.getDisciplina().getCodigo())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getDisciplina().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getTurma())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getTurma().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getProfessor())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getProfessor().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getAgrupador())) {
				caminhoAteDiretorio += (File.separator
						+ Uteis.removerAcentuacao(arquivoVO.getAgrupador()).replace(" ", "").replace("/", ""));
			}
		} else if (pastaBase.equals(PastaBaseArquivoEnum.COMUM.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.COMUM_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ arquivoVO.getNivelEducacional();
		} else if ((pastaBase.equals(PastaBaseArquivoEnum.DOCUMENTOS.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue()))
				&& (Uteis.isAtributoPreenchido(arquivoVO.getCpfAlunoDocumentacao())
						|| Uteis.isAtributoPreenchido(arquivoVO.getCpfPessoaDocumentacao()))) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase;
			if (Uteis.isAtributoPreenchido(arquivoVO.getCpfAlunoDocumentacao())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getCpfAlunoDocumentacao());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getCpfPessoaDocumentacao())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getCpfPessoaDocumentacao());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getOrigem())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getOrigem());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getCodOrigem())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getCodOrigem());
			}
		} else if ((pastaBase.equals(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP.getValue()))){
			caminhoAteDiretorio = localUpload + File.separator + pastaBase;
			if (Uteis.isAtributoPreenchido(arquivoVO.getOrigem())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getOrigem());
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getCodOrigem())) {
				caminhoAteDiretorio += (File.separator + arquivoVO.getCodOrigem());
			}
		} else if ((pastaBase.equals(PastaBaseArquivoEnum.REQUERIMENTOS.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue()))
				&& Uteis.isAtributoPreenchido(arquivoVO.getCpfRequerimento())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ arquivoVO.getCpfRequerimento();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.OUVIDORIA.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.OUVIDORIA_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator + arquivoVO.getCodOrigem();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator
					+ UteisData.getMesData(arquivoVO.getDataUpload());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.ESTAGIO.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.ESTAGIO_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator
					+ UteisData.getMesData(arquivoVO.getDataUpload());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.IREPORT.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator + arquivoVO.getOrigem()
					+ File.separator + arquivoVO.getCodOrigem();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.CONTA_PAGAR.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.CONTA_PAGAR_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator
					+ UteisData.getMesData(arquivoVO.getDataUpload()) + File.separator + arquivoVO.getCodOrigem();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.IMAGEM.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.IMAGEM_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ arquivoVO.getCpfRequerimento() + File.separator;
		} else if (pastaBase.equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator
					+ UteisData.getMesData(arquivoVO.getDataUpload());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.EAD_QUESTOES.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + PastaBaseArquivoEnum.EAD.getValue() + File.separator
					+ pastaBase + File.separator + arquivoVO.getDisciplina().getCodigo();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.CERTIFICADO_NFE.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase;
		} else if (pastaBase.equals(PastaBaseArquivoEnum.OFX.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.OFX_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator
					+ UteisData.getMesData(arquivoVO.getDataUpload());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.PROCESSO_SELETIVO_QUESTOES.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator + arquivoVO.getCodOrigem();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES.getValue())
				|| pastaBase.equals(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES_TMP.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator
					+ arquivoVO.getCpfAlunoDocumentacao();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.LAYOUT_HISTORICO.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator+ arquivoVO.getNivelEducacional()+File.separator+arquivoVO.getTipoRelatorio().name()+File.separator+arquivoVO.getCodOrigem();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.LAYOUT_HISTORICO_TMP.getValue())){
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator+ arquivoVO.getNivelEducacional()+File.separator+arquivoVO.getTipoRelatorio().name();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS.getValue())) {
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator+arquivoVO.getTipoRelatorio().name()+File.separator+arquivoVO.getCodOrigem();
		} else if (pastaBase.equals(PastaBaseArquivoEnum.LAYOUT_ATA_RESULTADOS_FINAIS_TMP.getValue())){
			caminhoAteDiretorio = localUpload + File.separator + pastaBase + File.separator+arquivoVO.getTipoRelatorio().name();
		} else {
			caminhoAteDiretorio = !pastaBase.startsWith(localUpload) ? localUpload + File.separator + pastaBase
					: pastaBase;
		}
		return caminhoAteDiretorio;
	}

	/**
	 * Rotina que monta o caminho do diretório onde o arquivo será incluido.
	 * 
	 * @param arquivoVO
	 * @param pastaBase
	 * @param configuracaoGeralSistemaVO
	 * @return
	 */
	public File criarCaminhoPastaAteDiretorioFixoDeInclusaoArquivos(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		String caminhoAteDiretorio = criarCaminhoPastaAteDiretorio(arquivoVO, pastaBase,
				configuracaoGeralSistemaVO.getLocalUploadArquivoFixo());
		File dir = new File(caminhoAteDiretorio);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * Rotina que monta o caminho do diretório onde o arquivo será incluido.
	 * 
	 * @param arquivoVO
	 * @param pastaBase
	 * @param configuracaoGeralSistemaVO
	 * @return
	 */
	public File criarCaminhoPastaDeInclusaoArquivos(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		File dir;
		if (pastaBase.equals(PastaBaseArquivoEnum.ARQUIVO.getValue()) && arquivoVO.getDisciplina().getCodigo() > 0) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + arquivoVO.getDisciplina().getCodigo());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.ARQUIVO.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator);
		} else if (pastaBase.equals(PastaBaseArquivoEnum.COMUM.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + arquivoVO.getNivelEducacional());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.DOCUMENTOS.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + arquivoVO.getCpfAlunoDocumentacao());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.REQUERIMENTOS.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + arquivoVO.getCpfRequerimento());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.OUVIDORIA.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + arquivoVO.getCodOrigem());
		} else if (pastaBase.equals(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator
					+ UteisData.getMesData(arquivoVO.getDataUpload()));
		} else if (pastaBase.equals(PastaBaseArquivoEnum.IMAGEM.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + arquivoVO.getCpfRequerimento() + File.separator);
		} else if (pastaBase.equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + UteisData.getAnoData(arquivoVO.getDataUpload()) + File.separator
					+ UteisData.getMesData(arquivoVO.getDataUpload()));
		} else if (pastaBase.equals(PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES.getValue())) {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator + arquivoVO.getCpfAlunoDocumentacao());
		} else {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase
					+ File.separator);
		}
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public void criarCaminhoPastaDeInclusaoArquivo(String pastaLocalUploadArquivo) throws Exception {
		File dir;
		try {
			if (pastaLocalUploadArquivo != null && !pastaLocalUploadArquivo.equals("")) {
				dir = new File(pastaLocalUploadArquivo);
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
		}
	}

	public void recortarFoto(ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Float largura,
			Float altura, Float x, Float y) throws Exception {
		String diretorioFixo = "";
		String extensao = "";
		try {
			diretorioFixo = (configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + arquivoVO.getCpfRequerimento()
					+ File.separator + arquivoVO.getNome());
			extensao = arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf('.') + 1);
			if (largura.intValue() == 0 || altura.intValue() == 0) {
				ConsistirException consistirException = new ConsistirException(
						UteisJSF.internacionalizar("msg_selecioneAreaNaImagem"));
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_selecioneAreaNaImagem"));
				throw consistirException;
			}

			criarCaminhoPastaDeInclusaoArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + arquivoVO.getCpfRequerimento());

			BufferedImage image = ImageIO.read(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()
					+ File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator
					+ arquivoVO.getCpfRequerimento() + File.separator + arquivoVO.getNome()));
			BufferedImage bufferedImageOut = image.getSubimage(x.intValue(), y.intValue(), largura.intValue(),
					altura.intValue());

			FileOutputStream fos = new FileOutputStream(new File(diretorioFixo));
			ImageIO.write(bufferedImageOut, extensao, fos);
			fos.close();

		} catch (Exception ex) {
			throw ex;
		} finally {
			diretorioFixo = null;
			extensao = null;
		}
	}

	public void recortarImagemRequerimento(ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			Float largura, Float altura, Float x, Float y) throws Exception {
		String diretorioFixo = "";
		String extensao = "";
		try {
			diretorioFixo = (configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator
					+ arquivoVO.getCpfRequerimento() + File.separator + arquivoVO.getNome());
			extensao = arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf('.') + 1);
			if (largura.intValue() == 0 || altura.intValue() == 0) {
				ConsistirException consistirException = new ConsistirException();
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_selecioneAreaNaImagem"));
				throw consistirException;
			}

			BufferedImage image = ImageIO.read(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()
					+ File.separator + PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator
					+ arquivoVO.getCpfRequerimento() + File.separator + arquivoVO.getNome()));
			BufferedImage bufferedImageOut = image.getSubimage(x.intValue(), y.intValue(), largura.intValue(),
					altura.intValue());

			FileOutputStream fos = new FileOutputStream(new File(diretorioFixo));
			ImageIO.write(bufferedImageOut, extensao, fos);
			fos.close();

			FileOutputStream fosTMP = new FileOutputStream(
					new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator
							+ arquivoVO.getCpfRequerimento() + File.separator + arquivoVO.getNome()));
			ImageIO.write(bufferedImageOut, extensao, fosTMP);
			fosTMP.close();

		} catch (Exception ex) {
			throw ex;
		} finally {
			diretorioFixo = null;
			extensao = null;
		}
	}

	public String recortarImagem(String caminhoBase, String nomeArquivoAtual,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Float largura, Float altura, Float x, Float y,
			UsuarioVO usuarioVO) throws Exception {
		String diretorioAtual = "";
		String diretorioNovo = "";
		String extensao = "";
		String novoArquivo = usuarioVO.getCodigo() + "_" + new Date().getTime();
		try {
			extensao = nomeArquivoAtual.substring(nomeArquivoAtual.lastIndexOf(".") + 1);
			if (caminhoBase.contains("_TMP")) {
				diretorioAtual = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + caminhoBase
						+ File.separator + nomeArquivoAtual;
				diretorioNovo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + caminhoBase
						+ File.separator + novoArquivo + "." + extensao;
			} else {
				diretorioAtual = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + caminhoBase
						+ File.separator + nomeArquivoAtual;
				diretorioNovo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + caminhoBase
						+ File.separator + novoArquivo + "." + extensao;
			}
			recortarImagem(diretorioAtual, diretorioNovo, extensao, largura, altura, x, y);
			return novoArquivo + "." + extensao;
		} catch (Exception ex) {
			throw ex;
		} finally {
			extensao = null;
			diretorioAtual = "";
			diretorioNovo = "";
		}
	}

	public void recortarImagem(String arquivoAtual, String novoArquivo, String extensao, Float largura, Float altura,
			Float x, Float y) throws Exception {
		BufferedImage image = null;
		BufferedImage bufferedImageOut = null;
		File arquivo = null;
		FileOutputStream fos = null;
		try {
			image = ImageIO.read(new File(arquivoAtual));
			bufferedImageOut = image.getSubimage(x.intValue(), y.intValue(), largura.intValue(), altura.intValue());

			arquivo = new File(novoArquivo);
			fos = new FileOutputStream(arquivo);
			ImageIO.write(bufferedImageOut, extensao, fos);

			fos.flush();
			fos.close();
			if (arquivoAtual.contains("TMP.")) {
				arquivo = new File(arquivoAtual);
				if (arquivo.exists()) {
					arquivo.delete();
				}
			}

		} catch (Exception ex) {
			if (fos != null) {
				fos.close();
			}
			throw ex;
		} finally {
			arquivo = null;
			extensao = null;
			novoArquivo = "";
			arquivoAtual = "";
			image = null;
			bufferedImageOut = null;
		}
	}

	public void recortarImagem(ArquivoVO arquivoVO, PastaBaseArquivoEnum pastaBaseArquivoTmp,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Float largura, Float altura, Float x, Float y,
			UsuarioVO usuarioVO) throws Exception {
		String extensao = arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf('.') + 1);
		String novoArquivo = usuarioVO.getCodigo() + "_" + new Date().getTime() + "." + extensao;
		String caminhoAteDiretorio = criarCaminhoPastaAteDiretorio(arquivoVO, pastaBaseArquivoTmp.getValue(),
				configuracaoGeralSistemaVO.getLocalUploadArquivoTemp());
		String diretorioAtual = caminhoAteDiretorio;
		String diretorioNovo = diretorioAtual;
		try {
			if (largura.intValue() == 0 || altura.intValue() == 0) {
				ConsistirException consistirException = new ConsistirException();
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_selecioneAreaNaImagem"));
				throw consistirException;
			}

			diretorioAtual = diretorioAtual + File.separator + arquivoVO.getNome();
			diretorioNovo = diretorioNovo + File.separator + novoArquivo;
			recortarImagem(diretorioAtual, diretorioNovo, extensao, largura, altura, x, y);

			arquivoVO.setPastaBaseArquivo(arquivoVO.recuperaNomePastaBaseCorrigidoLocalUpload(caminhoAteDiretorio,
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()));
			arquivoVO.setPastaBaseArquivoEnum(pastaBaseArquivoTmp);
			arquivoVO.setNome(novoArquivo);

		} catch (Exception ex) {

			throw ex;
		} finally {
			diretorioAtual = "";
			diretorioNovo = "";

		}
	}

	public void rotacionar90GrausParaEsquerda(String arquivoAtual, String extensao) throws Exception {
		File arquivoImagem = null;
		BufferedImage imagemOriginal = null;
		BufferedImage imageFinal = null;
		int width = 0;
		int height = 0;
		try {
			arquivoImagem = new File(arquivoAtual);
			imagemOriginal = ImageIO.read(arquivoImagem);

			height = imagemOriginal.getHeight();
			width = imagemOriginal.getWidth();
			imageFinal = new BufferedImage(imagemOriginal.getHeight(), imagemOriginal.getWidth(),
					imagemOriginal.getType());
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					imageFinal.setRGB(j, width - i - 1, imagemOriginal.getRGB(i, j));
				}
			}

			ImageIO.write(imageFinal, extensao, arquivoImagem);
		} catch (Exception e) {
			throw e;
		} finally {
			if (imageFinal != null) {
				imageFinal.flush();
			}
			if (imagemOriginal != null) {
				imagemOriginal.flush();
			}
			arquivoImagem = null;
			imagemOriginal = null;
			imageFinal = null;
			width = 0;
			height = 0;
		}

	}

	public void rotacionar90GrausParaEsquerda(ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		String diretorioFoto = "";
		try {
			if (arquivoVO.getPastaBaseArquivo().contains(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp())) {
				diretorioFoto = arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome();
			} else {
				if (arquivoVO.getPastaBaseArquivo().contains(arquivoVO.getCpfRequerimento())) {
					diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome();
				} else {
					diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getCpfRequerimento()
							+ File.separator + arquivoVO.getNome();
				}
			}
			if (arquivoVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue())) {
				diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator
						+ UteisData.getDataMesAnoConcatenado() + File.separator + arquivoVO.getNome();
			}
			rotacionar90GrausParaEsquerda(diretorioFoto, arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf(".") + 1, arquivoVO.getNome().length()));
			arquivoVO.setDataUpload(new Date());
		} catch (Exception e) {
			throw e;
		} finally {
			diretorioFoto = "";
		}
	}

	public String getPastaBase(String caminhoBase, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO)
			throws Exception {
		return configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
				+ caminhoBase.replaceAll("_TMP", "").replaceAll("TMP", "").replaceAll("Tmp", "");
	}

	public void rotacionar90GrausParaDireita(String diretorioFoto, String extensao) throws Exception {
		File arquivoImagem = null;
		BufferedImage imagemOriginal = null;
		BufferedImage imageFinal = null;
		int width = 0;
		int height = 0;
		try {
			arquivoImagem = new File(diretorioFoto);
			imagemOriginal = ImageIO.read(arquivoImagem);

			height = imagemOriginal.getHeight();
			width = imagemOriginal.getWidth();
			imageFinal = new BufferedImage(imagemOriginal.getHeight(), imagemOriginal.getWidth(),
					imagemOriginal.getType());
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					imageFinal.setRGB(height - j - 1, i, imagemOriginal.getRGB(i, j));
				}
			}
			if (!ImageIO.write(imageFinal, extensao, arquivoImagem)) {
				throw new Exception("Não foi possivel realizar esta operação");
			}
			;

		} catch (Exception e) {
			throw e;
		} finally {
			if (imageFinal != null) {
				imageFinal.flush();
			}
			if (imagemOriginal != null) {
				imagemOriginal.flush();
			}
			arquivoImagem = null;
			imagemOriginal = null;
			imageFinal = null;
			diretorioFoto = "";
			width = 0;
			height = 0;
		}
	}

	public void rotacionar90GrausParaDireita(ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO)
			throws Exception {
		String diretorioFoto = "";
		try {
			if (arquivoVO.getPastaBaseArquivo().contains(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp())) {
				diretorioFoto = arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome();
			} else {
				if (arquivoVO.getPastaBaseArquivo().contains(arquivoVO.getCpfRequerimento())) {
					diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome();
				} else {
					diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getCpfRequerimento()
							+ File.separator + arquivoVO.getNome();
				}
			}
			if (arquivoVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue())) {
				diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator
						+ UteisData.getDataMesAnoConcatenado() + File.separator + arquivoVO.getNome();
			}
			rotacionar90GrausParaDireita(diretorioFoto, arquivoVO.getNome()
					.substring(arquivoVO.getNome().lastIndexOf(".") + 1, arquivoVO.getNome().length()));
			arquivoVO.setDataUpload(new Date());
		} catch (Exception e) {
			throw e;
		} finally {
			diretorioFoto = "";
		}
	}

	public void rotacionar180Graus(String diretorioFoto, String extensao) throws Exception {
		File arquivoImagem = null;
		BufferedImage imagemOriginal = null;
		BufferedImage imageFinal = null;
		int width = 0;
		int height = 0;

		try {
			arquivoImagem = new File(diretorioFoto);
			imagemOriginal = ImageIO.read(arquivoImagem);

			height = imagemOriginal.getHeight();
			width = imagemOriginal.getWidth();
			imageFinal = new BufferedImage(imagemOriginal.getHeight(), imagemOriginal.getWidth(),
					imagemOriginal.getType());
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					imageFinal.setRGB(height - j - 1, width - i - 1, imagemOriginal.getRGB(i, j));
				}
			}

			ImageIO.write(imageFinal, extensao, arquivoImagem);

		} catch (Exception e) {
			throw e;
		} finally {
			if (imageFinal != null) {
				imageFinal.flush();
			}
			if (imagemOriginal != null) {
				imagemOriginal.flush();
			}
			arquivoImagem = null;
			imagemOriginal = null;
			imageFinal = null;
			diretorioFoto = "";
			width = 0;
			height = 0;
		}
	}

	public void rotacionar180Graus(ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO)
			throws Exception {
		String diretorioFoto = "";
		try {
			if (arquivoVO.getPastaBaseArquivo().contains(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp())) {
				diretorioFoto = arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome();
			} else {
				if (arquivoVO.getPastaBaseArquivo().contains(arquivoVO.getCpfRequerimento())) {
					diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome();
				} else {
					diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getCpfRequerimento()
							+ File.separator + arquivoVO.getNome();
				}
			}
			if (arquivoVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue())) {
				diretorioFoto = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator
						+ UteisData.getDataMesAnoConcatenado() + File.separator + arquivoVO.getNome();
			}
			rotacionar180Graus(diretorioFoto, arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf(".") + 1,
					arquivoVO.getNome().length()));
			arquivoVO.setDataUpload(new Date());
		} catch (Exception e) {
		} finally {
			diretorioFoto = "";
		}
	}

	public String copiarArquivoPastaBaseAnexoEmail(File file, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File diretorio = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase);
		try {
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			FileUtils.copyFileToDirectory(file, diretorio);
			return diretorio.getAbsolutePath() + File.separator + file.getName();
		} catch (Exception e) {
			throw e;
		} finally {
			diretorio = null;
		}
	}

	public static void salvarArquivoRecursoEducacionalNaPastaTemp(FileUploadEvent upload,
			TipoRecursoEducacionalEnum tipoRecursoEducacionalEnum, Integer disciplina, String nomeArquivo,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		try {

			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator
					+ PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator
					+ tipoRecursoEducacionalEnum.name());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			fileArquivo = new File(dir.getPath() + File.separator + nomeArquivo);

			InputStream is = upload.getUploadedFile().getInputStream();
			OutputStream out = new FileOutputStream(fileArquivo);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();

			// FileUtils.copyFile(arquivo, fileArquivo);
			// arquivo.delete();
//			if (tipoRecursoEducacionalEnum.equals(TipoRecursoEducacionalEnum.VIDEO)) {
			// OggDecoder decorder = new OggDecoder();
			// OggData oggData = decorder.getData(new
			// FileInputStream(arquivo.getAbsolutePath()));
			// FileUtils.writeByteArrayToFile(new File(dir.getPath() +
			// File.separator + nomeArquivo.replaceAll(".mp4", ".ogg")),
			// oggData.data.array());
//			}

		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
			// upload = null;
		}

	}

	public static void copiarArquivoDaPastaTempParaPastaFixa(String caminhoBase, String nomeArquivo,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File dir = null;
		File arquivo = null;
		File fileArquivo = null;
		try {
			arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + caminhoBase
					+ File.separator + nomeArquivo);
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
					+ caminhoBase.replaceAll("_TMP", "").replaceAll("TMP", "").replaceAll("Tmp", ""));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			fileArquivo = new File(dir.getPath() + File.separator + nomeArquivo);
			FileUtils.copyFile(arquivo, fileArquivo);

			arquivo.delete();
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
			arquivo = null;
		}

	}

	public static void salvarIconeNaPastaTemp(File arquivo, String nomeArquivo,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		try {

			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.ICONE.getValue());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			fileArquivo = new File(dir.getPath() + File.separator + nomeArquivo);
			FileUtils.copyFile(arquivo, fileArquivo);
			arquivo.delete();
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
		}

	}

	public static void salvarArquivoNaPastaTemp(FileUploadEvent upload, String nomeArquivo, String pastaBaseArquivo,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		try {
			if (pastaBaseArquivo.contains("_TMP") || pastaBaseArquivo.contains("Tmp")
					|| pastaBaseArquivo.endsWith("TMP")) {
				dir = new File(
						configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + pastaBaseArquivo);
			} else {
				dir = new File(
						configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivo);
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
			fileArquivo = new File(dir.getPath() + File.separator + nomeArquivo);
			InputStream is = upload.getUploadedFile().getInputStream();
			OutputStream out = new FileOutputStream(fileArquivo);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();
			// FileUtils.copyFile(arquivo, fileArquivo);
			// arquivo.delete();
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
		}

	}

	public static void salvarArquivoNaPastaTemp(File arquivo, String nomeArquivo, String pastaBaseArquivo,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		try {
			if (pastaBaseArquivo.contains("_TMP") || pastaBaseArquivo.contains("Tmp")
					|| pastaBaseArquivo.endsWith("TMP")) {
				dir = new File(
						configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + pastaBaseArquivo);
			} else {
				dir = new File(
						configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivo);
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
			fileArquivo = new File(dir.getPath() + File.separator + nomeArquivo);
			FileUtils.copyFile(arquivo, fileArquivo);
			arquivo.delete();
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
		}

	}

	public static void copiarArquivoDaPastaConfiguracaoGedParaPastaDigitalizacaoGed(String caminhoBaseOriginal,
			String caminhoBaseFinal, String nomeArquivoOriginal, String nomeArquivoFinal,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		copiarArquivoDaPastaTempParaPastaFixaComOutroNome(caminhoBaseOriginal, caminhoBaseFinal, nomeArquivoOriginal,
				nomeArquivoFinal, configuracaoGeralSistemaVO, true);
	}

	public static void copiarArquivoDaPastaTempParaPastaFixaComOutroNome(String caminhoBaseOriginal,
			String caminhoBaseFinal, String nomeArquivoOriginal, String nomeArquivoFinal,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean arquivoGED) throws Exception {
		File dir = null;
		File arquivo = null;
		File fileArquivo = null;
		try {
			if (arquivoGED) {
				arquivo = new File(
						configuracaoGeralSistemaVO.getLocalUploadArquivoGED() + File.separator + nomeArquivoOriginal);
			} else {
				arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
						+ caminhoBaseOriginal + File.separator + nomeArquivoOriginal);
			}
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + caminhoBaseFinal);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			fileArquivo = new File(dir.getPath() + File.separator + nomeArquivoFinal);
			FileUtils.copyFile(arquivo, fileArquivo);
			if (!arquivoGED) {
				arquivo.delete();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
			arquivo = null;
		}

	}

	public static void criarArquivoDOC(String textoHTML, ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO config,
			UsuarioVO usuarioVO) throws Exception {
		File fileArquivo = null;
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
			AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.html"));
			afiPart.setBinaryData(textoHTML.getBytes());
			afiPart.setContentType(new ContentType("text/html"));
			Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);

			CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk();
			ac.setId(altChunkRel.getId());
			wordMLPackage.getMainDocumentPart().addObject(ac);

			wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");

			File dir = new File(
					config.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			arquivoVO.setNome(criarNomeArquivo(usuarioVO, "doc"));
			arquivoVO.setPastaBaseArquivo(dir.getPath());

			fileArquivo = new File(arquivoVO.getPastaBaseArquivo() + File.separator + arquivoVO.getNome());

			wordMLPackage.save(fileArquivo);
		} catch (Exception e) {
			throw e;
		}
	}

	public void realizarGravacaoFotoSemRecorte(ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		String diretorioFixo = "";
		String caminhoPastaBase = "";
		String extensao = "";
		try {
			diretorioFixo = (configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + arquivoVO.getCpfRequerimento()
					+ File.separator + arquivoVO.getNome());
			extensao = arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf('.') + 1);
			if (Uteis.isAtributoPreenchido(arquivoVO.getPastaBaseArquivoEnum())) {
				caminhoPastaBase = arquivoVO.getPastaBaseArquivoEnum().getValue();
			} else if (Uteis.isAtributoPreenchido(arquivoVO.getPastaBaseArquivo())) {
				caminhoPastaBase = arquivoVO.getPastaBaseArquivo();
			} else {
				caminhoPastaBase = PastaBaseArquivoEnum.IMAGEM.getValue();
			}
			if (Uteis.isAtributoPreenchido(arquivoVO.getNome())) {
				criarCaminhoPastaDeInclusaoArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()
						+ File.separator + caminhoPastaBase + File.separator + arquivoVO.getCpfRequerimento());
				BufferedImage image = ImageIO.read(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp()
						+ File.separator + caminhoPastaBase + File.separator + arquivoVO.getCpfRequerimento()
						+ File.separator + arquivoVO.getNome()));
				BufferedImage bufferedImageOut = image;
				FileOutputStream fos = new FileOutputStream(new File(diretorioFixo));
				ImageIO.write(bufferedImageOut, extensao, fos);
				fos.close();
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			diretorioFixo = null;
			extensao = null;
		}
	}

	public String disponibilizarArquivoAssinadoParaDowload(String arquivoOrigem, String nomeArquivoDestino)
			throws Exception {
		FileChannel in = null;
		FileChannel out = null;
		FileInputStream srcInputStream = null;
		File fileDestino = null;
		try {
			srcInputStream = new FileInputStream(new File(arquivoOrigem));
			in = srcInputStream.getChannel();

			String copiaDestino = UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoDestino;
			if (Uteis.isSistemaOperacionalWindows() && copiaDestino.startsWith("/")) {
				copiaDestino = copiaDestino.substring(1, copiaDestino.length());
			}
			fileDestino = new File(copiaDestino);
			if (!fileDestino.exists()) {
				fileDestino.createNewFile();
			}
			out = new FileOutputStream(fileDestino).getChannel();
			in.transferTo(0, in.size(), out);
			return copiaDestino;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			in = null;
			out = null;
			srcInputStream = null;
			fileDestino = null;
		}
	}

	public static String getDetectarCharset(File arquivo) throws IOException {
		UniversalDetector detector = new UniversalDetector(null);
		InputStream stream = new FileInputStream(arquivo);
		String charset = null;
		try {
			byte[] buf = new byte[4096];
			int nread;
			while ((nread = stream.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();

			charset = detector.getDetectedCharset();
		} finally {
			stream.close();
		}

		return charset == null ? "UTF-8" : charset;
	}

	/**
	 * Recupera todos os arquivos do diretorio é da extensão informado.
	 * 
	 * @param caminhoDiretorio Caminho do diretorio.
	 * @param extensao         Ex: .txt , .pdf
	 * @return
	 */
	public File[] getTodosArquivosDiretorioPorExtensao(String caminhoDiretorio, String extensao, Date dataUltimoProcessamento) {
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				if(dataUltimoProcessamento != null) {
					return file.lastModified() >= dataUltimoProcessamento.getTime() && file.getName().toLowerCase().endsWith(extensao.toLowerCase()) && file.isFile();
				}
				return file.getName().toLowerCase().endsWith(extensao.toLowerCase()) && file.isFile();
			}
		};

		File arquivo = new File(caminhoDiretorio);
		return arquivo.listFiles(filter);
	}

	/**
	 * Recupera a data de Criação de um arquivo recebendo como parametro um File.
	 * 
	 * @param file
	 * @return - Data Criação
	 * @throws IOException
	 */
	public Date getDataCriacaoArquivo(File file) throws IOException {
		BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		FileTime time = attrs.creationTime();

		return new Date(time.toMillis());
	}

	public void AlterarArquivoTMPparaPadrao(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File diretorioASerIncluido = criarCaminhoPastaDeInclusaoArquivos(arquivoVO, pastaBase,
				configuracaoGeralSistemaVO);
		File arquivoTemporario;
		File arquivoFixo;
		File fileTmp = null;
		File fileVerificacaoTemp;
		try {

			arquivoTemporario = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ pastaBase + "TMP" + File.separator + arquivoVO.getNome());

			arquivoFixo = new File(diretorioASerIncluido.getPath() + File.separator + arquivoVO.getCodigoCatalogo()
					+ File.separator + arquivoVO.getNome());

			fileVerificacaoTemp = new File(
					diretorioASerIncluido.getPath() + File.separator + arquivoVO.getCodigoCatalogo());

			if (!fileVerificacaoTemp.exists()) {
				fileVerificacaoTemp.mkdirs();
			}
			arquivoVO.setPastaBaseArquivo(diretorioASerIncluido.getAbsolutePath());
			arquivoVO.setNome(arquivoVO.getNome());

			copiar(arquivoTemporario, arquivoFixo);
			fileTmp = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + pastaBase
					+ "TMP" + File.separator + arquivoVO.getNome());
			fileTmp.delete();

		} catch (Exception e) {
			throw e;
		} finally {
			diretorioASerIncluido = null;
			arquivoTemporario = null;
			arquivoFixo = null;
			fileTmp = null;
		}
	}

	public String renderizarDadosArquivos(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoPastaWeb, String imagemDefault,
			Boolean upLoadImagem) {
		try {
			String caminhoArquivo;
			String nomeArquivo = arquivoVO.getNome();
			File arquivoImagem = null;
			if (!arquivoVO.getNome().equals("")) {
				if (configuracaoGeralSistemaVO == null) {
					return "/" + pastaBase + "/" + arquivoVO.getNome();
				}
				if (configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo().endsWith("/")) {
					caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + pastaBase + "/";
				} else {
					caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + pastaBase + "/";
				}
				if (!upLoadImagem) {
					arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
							+ pastaBase + File.separator + arquivoVO.getCodOrigem() + File.separator + nomeArquivo);
					if (arquivoImagem.exists()) {
						return caminhoArquivo + arquivoVO.getCodOrigem() + "/" + nomeArquivo;
					} else {
						arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
								+ pastaBase + File.separator + File.separator + nomeArquivo);
						if (arquivoImagem.exists()) {
							return caminhoArquivo + "/" + nomeArquivo;
						} else {
							return caminhoPastaWeb + "/" + "resources" + "/" + "imagens" + "/" + imagemDefault;
						}
					}
				} else {

					return caminhoArquivo + "/" + nomeArquivo;

				}
			} else {
				return caminhoPastaWeb + "/" + "resources" + "/" + "imagens" + "/" + imagemDefault;
			}
		} finally {
		}
	}

	public void clonarUpLoadArquivo(ArquivoVO arquivoVO, String pastaBase,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		File diretorioASerClonado = criarCaminhoPastaDeInclusaoArquivos(arquivoVO, pastaBase,
				configuracaoGeralSistemaVO);
		File arquivoASerClonado;
		File arquivoClonado;
		File fileTmp = null;
		File fileVerificacaoTemp;
		try {

			arquivoASerClonado = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ pastaBase + File.separator + arquivoVO.getCodOrigem() + File.separator + arquivoVO.getNome());

			arquivoClonado = new File(diretorioASerClonado.getPath() + "TMP" + File.separator + arquivoVO.getNome());

			fileVerificacaoTemp = new File(
					diretorioASerClonado.getPath() + File.separator + arquivoVO.getCodigoCatalogo());

			if (!fileVerificacaoTemp.exists()) {
				fileVerificacaoTemp.mkdirs();
			}
			arquivoVO.setPastaBaseArquivo(pastaBase + "TMP");
			arquivoVO.setNome(arquivoVO.getNome());

			copiar(arquivoASerClonado, arquivoClonado);

		} catch (Exception e) {
			throw e;
		} finally {
			diretorioASerClonado = null;
			arquivoASerClonado = null;
			arquivoClonado = null;
			fileTmp = null;
		}
	}

	public void upLoadDocumentacaoMatriculaRequerimentoAplicativo(FileItem upload, ArquivoVO arquivoVO, String novoNome,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		String nomeArquivoSemAcento = "";
		try {
			dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
					+ pastaBaseArquivoEnum.getValue());
			if ((pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.ARQUIVO_TMP)
					|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.ARQUIVO))
					&& arquivoVO.getDisciplina().getCodigo().intValue() > 0) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
						+ pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getDisciplina().getCodigo());
			} else {
				if ((pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP)
						|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.DOCUMENTOS))
						&& !arquivoVO.getCpfAlunoDocumentacao().equals("")) {
					dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getCpfAlunoDocumentacao());
				} else if ((pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)
						|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.REQUERIMENTOS))
						&& !arquivoVO.getCpfRequerimento().equals("")) {
					dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getCpfRequerimento());
				} else {
					dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ pastaBaseArquivoEnum.getValue());
				}
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
			nomeArquivoSemAcento = criarNomeArquivo(novoNome, pastaBaseArquivoEnum, usuarioVO);
			arquivoVO.setNome(
					nomeArquivoSemAcento + "." + upload.getName().substring(upload.getName().lastIndexOf(".") + 1));
			upload.write(new File(dir.getPath() + File.separator + arquivoVO.getNome()));
			if ((pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.DOCUMENTOS_TMP)
					|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.DOCUMENTOS))
					&& !arquivoVO.getCpfAlunoDocumentacao().equals("")) {
				arquivoVO.setPastaBaseArquivo(
						pastaBaseArquivoEnum.getValue() + File.separator + arquivoVO.getCpfAlunoDocumentacao());
			}
			arquivoVO.setPastaBaseArquivoEnum(pastaBaseArquivoEnum);
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			upload = null;
			nomeArquivoSemAcento = null;
		}
	}

	/*
	 * public static String criarNomeArquivoEbsco(String nomeArquivo , String
	 * extensao) { return nomeArquivo + "_" + new Date().getTime() + "." + extensao;
	 * }
	 */

	public void upLoadArquivoIncricao(FileItem upload, ArquivoVO arquivoVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PastaBaseArquivoEnum pastaBaseArquivoEnum,
			UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		String nomeArquivoSemAcento = "";
		String localUplod = "";
		String caminhoAteDiretorio = "";

		try {
			localUplod = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.CERTIFICADO)
					|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED)
					|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.EAD_QUESTOES)) {
				localUplod = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
			}

			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.CERTIFICADO)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ pastaBaseArquivoEnum.getValue());
			}

			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.CERTIFICADO_NFE)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ pastaBaseArquivoEnum.getValue());
			}

			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator
						+ UteisData.getDataMesAnoConcatenado());
			}
			if (pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.EAD_QUESTOES)) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator
						+ PastaBaseArquivoEnum.EAD.getValue() + File.separator + pastaBaseArquivoEnum.getValue()
						+ File.separator + arquivoVO.getDisciplina().getCodigo());
			}

			caminhoAteDiretorio = criarCaminhoPastaAteDiretorio(arquivoVO, pastaBaseArquivoEnum.getValue(), localUplod);
			dir = new File(caminhoAteDiretorio);
			if ((pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA_TMP)
					|| pastaBaseArquivoEnum.equals(PastaBaseArquivoEnum.ARQUIVOSBILIOTECAEXTERNA))) {
				dir = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
						+ pastaBaseArquivoEnum.getValue());
			}

			if (!dir.exists()) {
				dir.mkdirs();
			}
			nomeArquivoSemAcento = criarNomeArquivo(upload.getName().substring(0, upload.getName().lastIndexOf(".")),
					pastaBaseArquivoEnum, usuarioVO);
			arquivoVO.setNome(
					nomeArquivoSemAcento + "." + upload.getName().substring(upload.getName().lastIndexOf(".") + 1));
			arquivoVO.setDescricao(upload.getName());
			if (arquivoVO.getDescricaoArquivo().trim().isEmpty()) {
				arquivoVO.setDescricaoArquivo(upload.getName());
			}
			arquivoVO.setDataUpload(new Date());
			// arquivoVO.setNome(criarNomeArquivo(upload.getUploadItem().getFileName(),
			// pastaBaseArquivoEnum));

			// arquivoVO.setNome(criarNomeArquivo(upload.getUploadItem().getFileName(),
			// pastaBaseArquivoEnum));

			fileArquivo = new File(dir.getPath() + File.separator + arquivoVO.getNome());

			InputStream is = upload.getInputStream();
			OutputStream out = new FileOutputStream(fileArquivo);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				out.write(buf, 0, len);
			is.close();
			out.close();
			upload.delete();
			arquivoVO.setPastaBaseArquivo(
					arquivoVO.recuperaNomePastaBaseCorrigidoLocalUpload(caminhoAteDiretorio, localUplod));
			arquivoVO.setPastaBaseArquivoEnum(pastaBaseArquivoEnum);
			arquivoVO.setUploadRealizado(true);
		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
			// upload = null;
			nomeArquivoSemAcento = null;
		}
	}
	
	private static String ICONE_IMG = "far fa-image";
	private static String ICONE_VIDEO = "fas fa-video";
	private static String ICONE_ZIP = "fas fa-file-archive";
	private static String ICONE_DOC = "imoon imoon-file-word";
	private static String ICONE_PDF = "imoon imoon-file-pdf";
	private static String ICONE_EXCEL = "imoon imoon-file-excel";
	private static String ICONE_TEXTO = "imoon imoon-file4";
	private static String ICONE_PPT = "imoon imoon-file-powerpoint";
	private static String ICONE_KEY = "fas fa-key";
	private static String ICONE_PADRAO = "fas fa-paperclip";
	private static String ICONE_AUDIO = "fas fa-headphones";
	
	public static String getIcone(String nomeArquivo) {
		if(Uteis.isAtributoPreenchido(nomeArquivo)) {
	if(nomeArquivo.endsWith(".jpg") 
			|| nomeArquivo.endsWith(".png")
			|| nomeArquivo.endsWith(".PNG")
			|| nomeArquivo.endsWith(".gif")
			|| nomeArquivo.endsWith(".GIF")
			|| nomeArquivo.endsWith(".JPG")
			|| nomeArquivo.endsWith(".jpeg")
			|| nomeArquivo.endsWith(".ttif") 
			|| nomeArquivo.endsWith(".JPEG")) {
		return ICONE_IMG; 
	}else if (nomeArquivo.endsWith(".wma") 
			|| nomeArquivo.endsWith(".avi")
			|| nomeArquivo.endsWith(".rmvb")
			|| nomeArquivo.endsWith(".mkv")
			|| nomeArquivo.endsWith(".flv")
			|| nomeArquivo.endsWith(".mov")
			|| nomeArquivo.endsWith(".wmv") 
			|| nomeArquivo.endsWith(".vob")){
		return ICONE_VIDEO; 
	}else if (nomeArquivo.endsWith(".zip") 
			|| nomeArquivo.endsWith(".rar")){
		return ICONE_ZIP; 
	}else if (nomeArquivo.endsWith(".pdf") || nomeArquivo.endsWith(".PDF")) {
		return ICONE_PDF; 
	}else if (nomeArquivo.endsWith(".pfx") || nomeArquivo.endsWith(".PFX") || nomeArquivo.endsWith(".jks") || nomeArquivo.endsWith(".JKS")) {
		return ICONE_KEY; 
	}else if (nomeArquivo.endsWith(".ppt") || nomeArquivo.endsWith(".PPT")) {
		return ICONE_PPT; 
	}else if (nomeArquivo.endsWith(".xls") || nomeArquivo.endsWith(".xlsx") || nomeArquivo.endsWith(".XLS") || nomeArquivo.endsWith(".XLSX") || nomeArquivo.endsWith(".csv") || nomeArquivo.endsWith(".CSV")) {
		return ICONE_EXCEL; 
	}else if (nomeArquivo.endsWith(".txt") || nomeArquivo.endsWith(".TXT")) {
		return ICONE_TEXTO; 
	}else if (nomeArquivo.endsWith(".doc") || nomeArquivo.endsWith(".docx")||nomeArquivo.endsWith(".DOC") || nomeArquivo.endsWith(".DOCX")) {
		return ICONE_DOC; 
	}else if (nomeArquivo.toLowerCase().endsWith(".mp3") || nomeArquivo.toLowerCase().endsWith(".wma")||nomeArquivo.toLowerCase().endsWith(".ogg") || nomeArquivo.toLowerCase().endsWith(".aac") || nomeArquivo.toLowerCase().endsWith(".aiff") || nomeArquivo.toLowerCase().endsWith(".pcm") || nomeArquivo.toLowerCase().endsWith(".flac")) {
		return ICONE_AUDIO; 
	}
		}
		return ICONE_PADRAO;
	}

	public void adicionarMarcaDaguaPDF(String caminhoImg, String caminhoPDF, String caminhoBase) throws IOException, DocumentException {
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		File arquivoTemp = null;
		FileOutputStream os = null;
		File fileParaAssina = new File(caminhoPDF);
		arquivoTemp = criarArquivoTemporario(caminhoBase, fileParaAssina);
		reader = new PdfReader(arquivoTemp.getAbsolutePath());
		os = new FileOutputStream(fileParaAssina);
		int n = reader.getNumberOfPages();
		pdfStamper = new PdfStamper(reader, os);
		int i = 0;
		PdfContentByte under;
		com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(caminhoImg);
		img.setAbsolutePosition(0, 0);
		while (i < n) {
			under = pdfStamper.getUnderContent(i + 1);
			under.addImage(img);
			i++;
		}
		pdfStamper.close();
		reader.close();
		FileUtils.forceDelete(arquivoTemp);
	}
	
	public Boolean verificarPDFIsPDFA(String caminhoArquivo) throws IOException {
		InputStream inputStream = new FileInputStream(caminhoArquivo);
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (int length; (length = inputStream.read(buffer)) != -1;) {
			result.write(buffer, 0, length);
		}
		inputStream.close();
		return result.toString("UTF-8").contains("http://www.aiim.org/pdfa/ns/id/");
	}
	
	public Integer verificarQuantidadeAssinaturasPdf(String caminhoArquivoPdf) {
		Integer quantidadeAssinaturas = 0;
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromFile(caminhoArquivoPdf);
        PdfFormWidget widgets = (PdfFormWidget) pdf.getForm();
        for (int i = 0; i < widgets.getFieldsWidget().getList().size(); i++) {
            PdfFieldWidget widget = (PdfFieldWidget)widgets.getFieldsWidget().getList().get(i);
            if (widget instanceof PdfSignatureFieldWidget) {
            	quantidadeAssinaturas++;
            }
        }
        pdf.close();
        return quantidadeAssinaturas;
	}
	
	public void realizarConversaoPDFPDFAImagem(String caminhoOrigemPdf, String caminhoDestinoPDF, ConfiguracaoGeralSistemaVO config, ArquivoVO obj) throws Exception {
		String resultFile = caminhoDestinoPDF;
		FileInputStream in = new FileInputStream(caminhoOrigemPdf);
		int lastIndex = caminhoDestinoPDF.lastIndexOf("/") + 1 > 0 ? caminhoDestinoPDF.lastIndexOf("/") + 1 : caminhoDestinoPDF.lastIndexOf("\\") + 1;
		String nomeArquivo = caminhoDestinoPDF.substring(lastIndex, caminhoDestinoPDF.lastIndexOf("."));
		File arquivoCopia = null;
		if (obj != null) {
			arquivoCopia = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getPastaBaseArquivo() + File.separator + (nomeArquivo + "_org.pdf"));
		}
		File fileAssinar = new File(caminhoOrigemPdf);
		if (obj != null) {
			copiar(fileAssinar, arquivoCopia);
		}
		PDDocument doc = new PDDocument();
		try {
			PDDocument docSource = PDDocument.load(in);
			PDRectangle A4L = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
			PDPage page = null;
			int pageCount = docSource.getNumberOfPages();
			Boolean isPaisagem = false;
			for (int i = 0; i < pageCount; i++) {
				PDRectangle pageSize = docSource.getPage(i).getMediaBox();
				int degree = docSource.getPage(i).getRotation();
				if ((pageSize.getWidth() > pageSize.getHeight()) || ((degree == 90) || (degree == 270))) {
					isPaisagem = true;
				}
				page = isPaisagem ? new PDPage(A4L) : new PDPage();
				doc.addPage(page);
				doc.setVersion(1.7f);
				PDFRenderer pdfRenderer = new PDFRenderer(docSource);
				PDPageContentStream contents = new PDPageContentStream(doc, page);
				int numPage = i;
				BufferedImage imagePage = pdfRenderer.renderImageWithDPI(numPage, 200);
				PDImageXObject pdfXOImage = JPEGFactory.createFromImage(doc, imagePage);
				float scale = 1f;
				contents.drawImage(pdfXOImage, 0, 0, page.getMediaBox().getWidth() * scale, page.getMediaBox().getHeight() * scale);
				contents.close();
			}
			// add XMP metadata
			XMPMetadata xmp = XMPMetadata.createXMPMetadata();
			PDDocumentCatalog catalogue = doc.getDocumentCatalog();
			Calendar cal = Calendar.getInstance();
			try {
				DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
				// dc.setTitle(file);
				dc.addCreator("SEI");
				dc.addDate(cal);
				PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
				id.setPart(3);
				id.setConformance("A");
				XmpSerializer serializer = new XmpSerializer();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				serializer.serialize(xmp, baos, true);
				PDMetadata metadata = new PDMetadata(doc);
				metadata.importXMPMetadata(baos.toByteArray());
				catalogue.setMetadata(metadata);
			} catch (BadFieldValueException e) {
				throw new IllegalArgumentException(e);
			}
			// sRGB output intent
			InputStream colorProfile = new FileInputStream(UteisJSF.getCaminhoWeb() + "resources/assets/icc/sRGB.icc");
			PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);
			intent.setInfo("sRGB IEC61966-2.1");
			intent.setOutputCondition("sRGB IEC61966-2.1");
			intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
			intent.setRegistryName("http://www.color.org");
			catalogue.addOutputIntent(intent);
			catalogue.setLanguage("en-US");
			PDViewerPreferences pdViewer = new PDViewerPreferences(page.getCOSObject());
			pdViewer.setDisplayDocTitle(true);
			catalogue.setViewerPreferences(pdViewer);
			PDMarkInfo mark = new PDMarkInfo();
			PDStructureTreeRoot treeRoot = new PDStructureTreeRoot();
			catalogue.setMarkInfo(mark);
			catalogue.setStructureTreeRoot(treeRoot);
			catalogue.getMarkInfo().setMarked(true);
			PDDocumentInformation info = doc.getDocumentInformation();
			info.setCreationDate(cal);
			info.setModificationDate(cal);
			info.setAuthor("SEI");
			info.setProducer("SEI");
			info.setCreator("SEI");
			info.setTitle(nomeArquivo);
			info.setSubject(nomeArquivo);
			doc.save(resultFile);
			doc.close();
			docSource.close();
			in.close();
			if (obj != null) {
				arquivoCopia.delete();
			}
		} catch (Exception e) {
			if (obj != null) {
				arquivoCopia.delete();
			}
			fileAssinar.delete();
			throw e;
		}
	}
	
	public void realizarConversaoPdfPdfA(String caminhoArquivoPdf, ConfiguracaoGeralSistemaVO config, ArquivoVO obj, Boolean realizarConversaoPDFPDFAImagem, Boolean conversaoPDFAImagemRealizado) throws Exception {
		realizarConversaoPdfPdfA(caminhoArquivoPdf, config, obj, realizarConversaoPDFPDFAImagem, conversaoPDFAImagemRealizado, Boolean.TRUE);
	}
	
	public void realizarConversaoPdfPdfA(String caminhoArquivoPdf, ConfiguracaoGeralSistemaVO config, ArquivoVO obj, Boolean realizarConversaoPDFPDFAImagem, Boolean conversaoPDFAImagemRealizado, Boolean validarArquivoIsPdfA) throws Exception {
		File arquivoCopia = null;
		String inputFile = null;
		String outputFolder = null;
		File fileAssinar = null;
		Boolean realizandoConversaoPorImagem = false;
		try {
			
			//primeira verificação se tem assinaturas ou se é um pdf-a
	        fileAssinar = new File(caminhoArquivoPdf);
			if (validarArquivoIsPdfA) {
				if ((!realizarConversaoPDFPDFAImagem || (realizarConversaoPDFPDFAImagem && conversaoPDFAImagemRealizado)) && ((Objects.nonNull(obj.getArquivoIsPdfa()) && obj.getArquivoIsPdfa()))) {
					return;
				}
			}
			
			//segunda validação se tem assinaturas ou se é pdf-a
		    String tipoPdf = UteisPdfA.realizarVerificacaoCompatibilidadePDFA(caminhoArquivoPdf);
			if (tipoPdf.toUpperCase().contains("PDF_A") || UteisPdfA.verificaPdfAssinadoDigitalmente(caminhoArquivoPdf)) {
				obj.setArquivoIsPdfa(tipoPdf.toUpperCase().contains("PDF_A"));
				return;
			}

			inputFile = caminhoArquivoPdf;
			outputFolder = caminhoArquivoPdf;
			if (fileAssinar.length() > 63000000L) {
				realizandoConversaoPorImagem = true;
				throw new Exception("Não é possível realizar a conversão para PDF/A, pois o arquivo excede 60mb.");
			}
			
			if (realizarConversaoPDFPDFAImagem) {
				realizandoConversaoPorImagem = true;
				realizarConversaoPDFPDFAImagem(inputFile, outputFolder, config, obj);
				UteisPdfA.validarConformidadeArquivoConvertidoPdfa(fileAssinar);
				obj.setArquivoIsPdfa(Boolean.TRUE);
				return;
			}
			
		    String nomeArquivoCopia = "";
		    int endIndex = obj.getNome().lastIndexOf(".");
		    if (endIndex != -1) {
		        nomeArquivoCopia = obj.getNome().substring(0, endIndex);
		    }
		    arquivoCopia = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getPastaBaseArquivo() + File.separator + (nomeArquivoCopia + "_org.pdf"));
		   
		    FileInputStream in = new FileInputStream(inputFile);
	        PDDocument docSource = PDDocument.load(in);
	        docSource.close();
	        in.close();
	        
	        UteisPdfA.realizarConversaoPDFA(inputFile, outputFolder, PdfAConformance.PDF_A_3B);
	        UteisPdfA.validarConformidadeArquivoConvertidoPdfa(fileAssinar);
	        obj.setArquivoIsPdfa(Boolean.TRUE);
			return;
			

			
			/*if (realizarConversaoPDFPDFAImagem || pageCount >= 9) {
				realizandoConversaoPorImagem = true;
				realizarConversaoPDFPDFAImagem(inputFile, outputFolder, config, obj);
				return;
			}
			copiar(fileAssinar, arquivoCopia);
	        PdfDocument pdfDocument = new PdfDocument();
	        pdfDocument.loadFromFile(inputFile);
	        int lastIndex = caminhoArquivoPdf.lastIndexOf("/") + 1 > 0 ? caminhoArquivoPdf.lastIndexOf("/") + 1 : caminhoArquivoPdf.lastIndexOf("\\") + 1;
	        String nomeArquivo = caminhoArquivoPdf.substring(lastIndex, caminhoArquivoPdf.length());
        	pdfDocument.getDocumentInformation().setCreationDate(new Date());
        	pdfDocument.getDocumentInformation().setTitle(nomeArquivo);
        	pdfDocument.getDocumentInformation().setModificationDate(new Date());
	        pdfDocument.saveToFile(outputFolder);
	        pdfDocument.close();
	        PdfStandardsConverter converter = new PdfStandardsConverter(inputFile);
	        //Converte Pdf para PdfA3A
	        converter.toPdfA3A(outputFolder);
	        arquivoCopia.delete();*/
		} catch (Exception e) {
			//Em caso de erro na conversão será realizado conversão pdf-imagem e depois imagem-pdf.
			if (!realizandoConversaoPorImagem && !conversaoPDFAImagemRealizado) {
				if (arquivoCopia!= null && arquivoCopia.exists()) {
					arquivoCopia.delete();
					arquivoCopia = null;
				}
				try {
					realizarConversaoPDFPDFAImagem(inputFile, outputFolder, config, obj);
					UteisPdfA.validarConformidadeArquivoConvertidoPdfa(fileAssinar);
					obj.setArquivoIsPdfa(Boolean.TRUE);
					return;
				} catch (Exception e2) {
					if (fileAssinar != null) {
						fileAssinar.delete();
					}
					throw new Exception("Erro ao realizar a conversão de PDF/A por imagem (" + e2.getMessage() + ")");
				}
			}
			if (fileAssinar != null) {
				fileAssinar.delete();
			}
			if (arquivoCopia != null) {
				arquivoCopia.delete();
			}
			throw e;
		}
	}
	
	
	/**
	 * Método que cria um ByteArrayInputStream e fecha o antigo InputStream, foi
	 * criado com o intuito de porque com o ByteArrayInputStream é possível
	 * manipulalo multiplas vezes, ao contrário do inputStream que a sua manipulação
	 * poder ser feita apenas uma vez
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * @author Felipi Alves
	 */
	public static ByteArrayInputStream criarByteArrayOutputStream(InputStream inputStream) throws Exception {
		try {
			if (Objects.isNull(inputStream)) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return new ByteArrayInputStream(baos.toByteArray());
		} finally {
			if (Objects.nonNull(inputStream)) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * Método que cria um ByteArrayInputStream e fecha o antigo InputStream, foi
	 * criado com o intuito de porque com o ByteArrayInputStream é possível
	 * manipulalo multiplas vezes, ao contrário do inputStream que a sua manipulação
	 * poder ser feita apenas uma vez
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * @author Felipi Alves
	 */
	public static ByteArrayInputStream criarByteArrayInputStream(InputStream inputStream) throws Exception {
		try {
			if (Objects.isNull(inputStream)) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return new ByteArrayInputStream(baos.toByteArray());
		} finally {
			if (Objects.nonNull(inputStream)) {
				inputStream.close();
			}
		}
	}

	/**
	 * Método que valida se o PDF passado no InputStream é criptografado, caso seja
	 * o sistema retornara uma Exception
	 * 
	 * @param inputStream
	 * @throws Exception
	 * @chamado 42119
	 * @author Felipi Alves
	 */
	public static void validarArquivoPdfCriptografado(ByteArrayInputStream inputStream) throws Exception {
		validarArquivoPdfCriptografado(inputStream, true);
	}
	
	public static void validarArquivoPdfCriptografado(ByteArrayInputStream inputStream, boolean resetInputStream) throws Exception {
		if (Objects.isNull(inputStream)) {
			return;
		}
		try {
			if (resetInputStream) {
				inputStream.mark(Integer.MAX_VALUE);
			}
			validarArquivoPdfNaoCriptografado(inputStream);
		} finally {
			if (resetInputStream) {
				inputStream.reset();
			}
		}
	}
	
	public String lerArquivoDeFontes(String nomeDoArquivo) throws Exception {
        return UteisJSF.getCaminhoWeb() + "resources/fontes/" + nomeDoArquivo;
	}
	
	public enum PdfStatus {
        VALID, VALID_ENCRYPTED, INVALID
    }

    public static PdfStatus validatePdf(InputStream in) {
        if (in == null) return PdfStatus.INVALID;

        InputStream src = in;
        if (!src.markSupported()) {
            src = new BufferedInputStream(src);
        }

        src.mark(Integer.MAX_VALUE);
        try (PDDocument doc = PDDocument.load(src, (String) null, MemoryUsageSetting.setupTempFileOnly())) {
            return doc.isEncrypted() ? PdfStatus.VALID_ENCRYPTED : PdfStatus.VALID;
        } catch (InvalidPasswordException e) {
            // é PDF, mas criptografado
            return PdfStatus.VALID_ENCRYPTED;
        } catch (IOException e) {
            // não é PDF ou está corrompido
            return PdfStatus.INVALID;
        } finally {
            try { src.reset(); } catch (IOException ignore) {}
        }
    }
    
    public static void validarArquivoPdfNaoCriptografado(InputStream in) throws Exception {
        PdfStatus st = validatePdf(in);
        switch (st) {
            case VALID:
                return;
            case VALID_ENCRYPTED:
                throw new Exception("PDF válido, porém criptografado (senha necessária).");
            case INVALID:
            default:
                throw new Exception("Arquivo não é um PDF válido ou está corrompido.");
        }
    }
	
	/**
	 * método que valida se um arquivo é um PDF/A e também se ele está em conformidade sendo 
	 * que se ele for do tipo PDF/A deve ser obrigatório que ele esteja em conformidade
	 * 
	 * @param arquivoVO
	 * @param inputStream
	 * @throws Exception
	 * 
	 * @author Felipi Alves
	 * @chamado 44966
	 */
	public void validarConformidadeArquivoPdf(ArquivoVO arquivoVO, ByteArrayInputStream inputStream) throws Exception {
		if (Objects.nonNull(inputStream)) {
			// vai ser validado se o arquivo que foi passado é um PDF/A, caso sejá vei ser retornado TRUE
			// caso seja falso indica que o arquivo em si não é do tipo PDF/A
			boolean arquivoIsPdfA = UteisPdfA.realizarVerificacaoCompatibilidadePDFA(inputStream);
			
			// validando se o arquivo está em conformidade, sendo que os cenarios mapeados é obrigatorio a conformidade
			// apenas para arquivos que já são um PDF/A
			boolean arquivoIsConformidade = UteisPdfA.validarArquivoIsConformidade(inputStream);
			Uteis.checkState(arquivoIsPdfA && !arquivoIsConformidade, "O arquivo é identificado como PDF/A, mas não está em conformidade com os padrões requeridos. Existem irregularidades técnicas que impedem sua utilização");
			Uteis.checkState(!arquivoIsPdfA && arquivoIsConformidade, "O arquivo PDF apresenta inconsistências em sua formatação interna que impedem a validação. Recomendamos gerar um novo arquivo ou solicitar uma versão corrigida do documento.");
			if (Objects.nonNull(arquivoVO)) {
				arquivoVO.setArquivoIsPdfa(arquivoIsPdfA);
			}
		}
	}
	
	public void validarArquivoIsPdfa(byte[] byteArquivo, String tipoDocumento) throws Exception {
		if (Objects.nonNull(byteArquivo)) {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArquivo);
			try {
				// vai ser validado se o arquivo que foi passado é um PDF/A, caso sejá vei ser retornado TRUE
				// caso seja falso indica que o arquivo em si não é do tipo PDF/A
				boolean arquivoIsPdfA = UteisPdfA.realizarVerificacaoCompatibilidadePDFA(inputStream);
				
				// validando se o arquivo está em conformidade, sendo que os cenarios mapeados é obrigatorio a conformidade
				// apenas para arquivos que já são um PDF/A
				boolean arquivoIsConformidade = UteisPdfA.validarArquivoIsConformidade(inputStream);
				Uteis.checkState(!arquivoIsPdfA, "O arquivo não é PDF/A. Formato obrigatório para documentos, documento: " + tipoDocumento);
				Uteis.checkState(arquivoIsPdfA && !arquivoIsConformidade, "O arquivo é identificado como PDF/A, mas não está em conformidade com os padrões requeridos. Existem irregularidades técnicas que impedem sua utilização, documento: " + tipoDocumento);
			} finally {
				inputStream.close();
				inputStream = null;
			}
		}
	}
}