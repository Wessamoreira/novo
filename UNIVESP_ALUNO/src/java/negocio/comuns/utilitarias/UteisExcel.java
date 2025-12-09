package negocio.comuns.utilitarias;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Date;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.TextAlign;
import org.apache.poi.xssf.usermodel.TextDirection;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import negocio.comuns.administrativo.LayoutRelatorioSeiDecidirCampoVO;


public class UteisExcel {
	
	private static final int WIDTH_COLUNA = 200;

	/*
	 * private Map<String , Object> mapaExcel;
	 * 
	 * public Map<String, Object> getMapaExcel() { return mapaExcel; }
	 * 
	 * public void setMapaExcel(Map<String, Object> mapaExcel) { this.mapaExcel = mapaExcel; }
	 */
	
	CellStyle headerStyle;
	CellStyle textoCellStyle;
	CellStyle moedaCellStyle;
	CellStyle dataCellStyle;
	CellStyle intCellStyle;
	DataFormat dataFormat;
	DataFormat doubleFormat;
	DataFormat textoFormat;
	DataFormat intFormat;

	public UteisExcel() {
		super();
	}
	
	public CellStyle preencherLayoutNumerico() {
	    moedaCellStyle.setDataFormat(doubleFormat.getFormat("#,##0.00"));
	    moedaCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
	    moedaCellStyle.setAlignment(HorizontalAlignment.CENTER);
	    moedaCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    moedaCellStyle.setBorderRight(BorderStyle.THIN);
	    moedaCellStyle.setBorderBottom(BorderStyle.THIN);
	    moedaCellStyle.setBorderLeft(BorderStyle.THIN);
	    return moedaCellStyle;
	}


	public UteisExcel(Workbook workbook) {
		headerStyle = workbook.createCellStyle();
		textoCellStyle = workbook.createCellStyle();
		moedaCellStyle = workbook.createCellStyle();
		dataCellStyle = workbook.createCellStyle();
		intCellStyle = workbook.createCellStyle();
		textoFormat = workbook.createDataFormat();
		dataFormat = workbook.createDataFormat();
		doubleFormat = workbook.createDataFormat();
		intFormat = workbook.createDataFormat();
	}
	 

	public CellStyle preencherHeader() {	
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.MEDIUM);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		return headerStyle;
	}
	
	public CellStyle preencherLayoutTexto() {
		textoCellStyle.setDataFormat(textoFormat.getFormat(""));
		textoCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		textoCellStyle.setAlignment(HorizontalAlignment.CENTER);
		textoCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		textoCellStyle.setBorderRight(BorderStyle.THIN);
		textoCellStyle.setBorderBottom(BorderStyle.THIN);
		textoCellStyle.setBorderLeft(BorderStyle.THIN);
		return textoCellStyle;
	}

	public CellStyle preencherLayoutMoeda() {
		//moedaCellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));		/
		moedaCellStyle.setDataFormat(doubleFormat.getFormat("R$#,##0.00;-R$#,##0.00"));
		moedaCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		moedaCellStyle.setAlignment(HorizontalAlignment.CENTER);
		moedaCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		moedaCellStyle.setBorderRight(BorderStyle.THIN);
		moedaCellStyle.setBorderBottom(BorderStyle.THIN);
		moedaCellStyle.setBorderLeft(BorderStyle.THIN);
		return moedaCellStyle;
	}

	public CellStyle preencherLayoutData() {
		//CellStyle dateStyle = workbook.createCellStyle();
		//dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));
		dataCellStyle.setDataFormat(dataFormat.getFormat("dd/MM/yyyy"));
		dataCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
		dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		dataCellStyle.setBorderRight(BorderStyle.THIN);
		dataCellStyle.setBorderBottom(BorderStyle.THIN);
		dataCellStyle.setBorderLeft(BorderStyle.THIN);
		return dataCellStyle;
	}

	
	
	public CellStyle preencherLayoutInteiro() {
		intCellStyle.setDataFormat(intFormat.getFormat("0"));
		intCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		intCellStyle.setAlignment(HorizontalAlignment.CENTER);
		intCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		intCellStyle.setBorderRight(BorderStyle.THIN);
		intCellStyle.setBorderBottom(BorderStyle.THIN);
		intCellStyle.setBorderLeft(BorderStyle.THIN);
		return intCellStyle;
	}

	public void preencherCelulaCabecalho(HSSFSheet sheet, Row row, int cellnum, int tamanho, String campo) {
		sheet.setColumnWidth(cellnum, tamanho);
		Cell cell = row.createCell(cellnum);
		cell.setCellStyle(preencherHeader());
		cell.setCellValue((String) campo);

	}
	public void preencherCelulaCabecalho(XSSFSheet sheet, Row row, int cellnum, int tamanho, String campo) {
		sheet.setColumnWidth(cellnum, tamanho);
		Cell cell = row.createCell(cellnum);
		cell.setCellStyle(preencherHeader());
		cell.setCellValue((String) campo);
		
	}

	public void preencherCelula(Row row, int cellnum, Object campo) {
		Cell cell = row.createCell(cellnum);
		if (campo instanceof String) {
			cell.setCellStyle(preencherLayoutTexto());
			cell.setCellValue((String) campo);			
		} else if (campo instanceof Integer) {
			cell.setCellStyle(preencherLayoutInteiro());
			cell.setCellValue((Integer) campo);
		} else if (campo instanceof Double) {
			cell.setCellStyle(preencherLayoutMoeda());
			cell.setCellValue((Double) campo);			
		} else if (campo instanceof Date) {
			cell.setCellStyle(preencherLayoutData());
			cell.setCellValue((Date) campo);
			
		}
	}

	public void realizarGeracaoTopoPadraoRelatorio(HSSFWorkbook workbook, HSSFSheet worksheet, String caminhoLogo, Integer qtdeColunaExcel, String descricao) throws Exception {
		int qtdeColuna = 0;
		int colunaImagem = -1;
		double widthColumn = 0;
		int pictureIndex = realizarImportacaoImagemExcel(workbook, caminhoLogo);
		HSSFPatriarch patriarch = worksheet.createDrawingPatriarch();
		HSSFPicture imagem = null;
		HSSFClientAnchor anchor = null;
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);		
//		if (relatorioSEIDecidirVO != null 
//				&& relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO() != null 
//				&& Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs())) {
//			qtdeColuna = preencherEspacoColunaTopo(worksheet, relatorioSEIDecidirVO);			
//			while (widthColumn < 220) {
//				colunaImagem++;
//				widthColumn += (worksheet.getColumnWidth(colunaImagem) / 256) * 7;
//			}
//		} else {
//			qtdeColuna = qtdeColunaExcel - 1;
//			colunaImagem = 0;
//			widthColumn = 0;
//		}
		worksheet.addMergedRegion(new CellRangeAddress(0, 4, 0, qtdeColuna));
		if (Uteis.isAtributoPreenchido(descricao)) {
			HSSFTextbox textbox1 = patriarch.createTextbox(new HSSFClientAnchor(0, 0, 1023, 0, (short) 0, 0, (short) qtdeColuna, 5));
			HSSFRichTextString texto = new HSSFRichTextString(descricao);
			textbox1.setString(texto);
			textbox1.setVerticalAlignment(VerticalAlignment.CENTER.getCode());
			textbox1.setHorizontalAlignment(HorizontalAlignment.CENTER.getCode());
		}
		anchor = new HSSFClientAnchor(0, 0, (short) 1023, 180, (short) 0, 0, (short) colunaImagem, 5);
		imagem = patriarch.createPicture(anchor, pictureIndex);
		if (widthColumn > 0 &&  imagem.getImageDimension().getWidth() > widthColumn) {
			imagem.resize(220 / imagem.getImageDimension().getWidth());
		} else if (widthColumn > 0 && imagem.getImageDimension().getHeight() > (5 * 17)) {
			imagem.resize((5 * 17) / imagem.getImageDimension().getHeight());
		}
	}

//	private int preencherEspacoColunaTopo(HSSFSheet worksheet, RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
//		int qtdeColuna = 1;
//		if (!worksheet.getSheetName().contains("Filtros")) {
//			int coluna = 0;
//			qtdeColuna = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size() - 1;
//			for (LayoutRelatorioSeiDecidirCampoVO campo : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
//				worksheet.setColumnWidth(coluna, campo.getTamanhoColuna() * WIDTH_COLUNA);
//				coluna++;
//			}
//		} else {
//			worksheet.setColumnWidth(0, 7000);
//			worksheet.setColumnWidth(1, 24000);
//		}
//		return qtdeColuna;
//	}

	private int realizarImportacaoImagemExcel(HSSFWorkbook workbook, String caminhoLogo) throws Exception {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		int pictureIndex = 0;
		try {
			fis = new FileInputStream(caminhoLogo);
			bos = new ByteArrayOutputStream();
			int c;
			while ((c = fis.read()) != -1)
				bos.write(c);
			if (caminhoLogo.endsWith("png") || caminhoLogo.endsWith("PNG")) {
				pictureIndex = workbook.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
			} else {
				pictureIndex = workbook.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
			}
		} finally {
			if (fis != null)
				fis.close();
			if (bos != null)
				bos.close();
		}
		return pictureIndex;
	}
	
	public void preencherCelula(Row row, int inicio, int fim, Object campo) {
		IntStream.rangeClosed(inicio, fim).forEach(i -> preencherCelula(row, i, campo));
	}
	
	public void realizarGeracaoTopoPadraoRelatorio(XSSFWorkbook workbook, XSSFSheet worksheet, String caminhoLogo,  Integer qtdeColunaExcel, String descricao) throws Exception {
		int qtdeColuna = 0;
		int colunaImagem = -1;
		double widthColumn = 0;
		int pictureIndex = realizarImportacaoImagemExcel(workbook, caminhoLogo);
		
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);
		worksheet.createRow(worksheet.getLastRowNum() + 1);		
//		if (relatorioSEIDecidirVO != null 
//				&& relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO() != null 
//				&& Uteis.isAtributoPreenchido(relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs())) {
//			qtdeColuna = preencherEspacoColunaTopo(worksheet, relatorioSEIDecidirVO);			
//			while (widthColumn < 220) {
//				colunaImagem++;
//				widthColumn += (worksheet.getColumnWidth(colunaImagem) / 256) * 7;
//			}
//		} else {
//			qtdeColuna = qtdeColunaExcel - 1;
//			colunaImagem = 0;
//			widthColumn = 0;
//		}
		worksheet.addMergedRegion(new CellRangeAddress(0, 4, 0, 0));
		XSSFDrawing patriarch = worksheet.createDrawingPatriarch();		
		XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, (short) 1023, 180, (short) 0, 0, (short) 1, 5);
		patriarch.createPicture(anchor, pictureIndex);
		worksheet.autoSizeColumn(0);
		worksheet.addMergedRegion(new CellRangeAddress(0, 4, 1, qtdeColuna));
		if (Uteis.isAtributoPreenchido(descricao)) {
			XSSFClientAnchor anchor2 = new XSSFClientAnchor(0, 0, 1023, 180, 1, 0, qtdeColuna+1, 5);			
			anchor2.setAnchorType(AnchorType.byId(2));
			XSSFDrawing patriarch2 = worksheet.createDrawingPatriarch();		
			XSSFTextBox textbox1 = patriarch2.createTextbox(anchor2);
			XSSFRichTextString texto = new XSSFRichTextString(descricao);				
			textbox1.setText(texto);
			textbox1.setVerticalAlignment(VerticalAlignment.CENTER);											
			textbox1.setTextDirection(TextDirection.HORIZONTAL);
			textbox1.getTextParagraphs().get(0).setTextAlign(TextAlign.CENTER);
			worksheet.autoSizeColumn(1);
		}
	}
	
	private int realizarImportacaoImagemExcel(XSSFWorkbook workbook, String caminhoLogo) throws Exception {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		int pictureIndex = 0;
		try {
			fis = new FileInputStream(caminhoLogo);
			bos = new ByteArrayOutputStream();
			int c;
			while ((c = fis.read()) != -1)
				bos.write(c);
			if (caminhoLogo.endsWith("png") || caminhoLogo.endsWith("PNG")) {
				pictureIndex = workbook.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
			} else {
				pictureIndex = workbook.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
			}
		} finally {
			if (fis != null)
				fis.close();
			if (bos != null)
				bos.close();
		}
		return pictureIndex;
	}
	
//	private int preencherEspacoColunaTopo(XSSFSheet worksheet, RelatorioSEIDecidirVO relatorioSEIDecidirVO) {
//		int qtdeColuna = 1;
//		if (!worksheet.getSheetName().contains("Filtros")) {
//			int coluna = 0;
//			qtdeColuna = relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs().size() - 1;
//			for (LayoutRelatorioSeiDecidirCampoVO campo : relatorioSEIDecidirVO.getLayoutRelatorioSEIDecidirVO().getLayoutRelatorioSeiDecidirCampoVOs()) {
//				worksheet.setColumnWidth(coluna, campo.getTamanhoColuna() * WIDTH_COLUNA);
//				coluna++;
//			}
//		} else {
//			worksheet.setColumnWidth(0, 7000);
//			worksheet.setColumnWidth(1, 24000);
//		}
//		return qtdeColuna;
//	}
}
