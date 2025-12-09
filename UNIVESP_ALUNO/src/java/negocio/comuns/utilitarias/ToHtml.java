/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package negocio.comuns.utilitarias;

// Imports estáticos de ALINHAMENTO HORIZONTAL
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER_SELECTION;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.FILL;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.GENERAL;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.JUSTIFY;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT;

// Imports estáticos de BORDAS
import static org.apache.poi.ss.usermodel.BorderStyle.DASH_DOT;
import static org.apache.poi.ss.usermodel.BorderStyle.DASH_DOT_DOT;
import static org.apache.poi.ss.usermodel.BorderStyle.DASHED;
import static org.apache.poi.ss.usermodel.BorderStyle.DOTTED;
import static org.apache.poi.ss.usermodel.BorderStyle.DOUBLE;
import static org.apache.poi.ss.usermodel.BorderStyle.HAIR;
import static org.apache.poi.ss.usermodel.BorderStyle.MEDIUM;
import static org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASHED;
import static org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASH_DOT;
import static org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASH_DOT_DOT;
import static org.apache.poi.ss.usermodel.BorderStyle.NONE;
import static org.apache.poi.ss.usermodel.BorderStyle.SLANTED_DASH_DOT;
import static org.apache.poi.ss.usermodel.BorderStyle.THICK;
import static org.apache.poi.ss.usermodel.BorderStyle.THIN;

// Imports estáticos de ALINHAMENTO VERTICAL
// Removido o import estático de CENTER para evitar ambiguidade
import static org.apache.poi.ss.usermodel.VerticalAlignment.BOTTOM;
import static org.apache.poi.ss.usermodel.VerticalAlignment.TOP;

// Imports estáticos dos NOVOS TIPOS DE CÉLULA (API moderna)
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.ERROR;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;


import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// Imports específicos de HSSF (para .xls) - Necessário para o HSSFHtmlHelper
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
// Imports específicos de XSSF (para .xlsx) - Para checagem de tipo
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Imports da API GERAL (ss.usermodel) - API MODERNA
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.format.CellFormatResult;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType; // API Moderna
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


/**
 * This example shows how to display a spreadsheet in HTML using the classes for
 * spreadsheet display.
 *
 * @author Ken Arnold, Industrious Media LLC
 */
public class ToHtml {

    private final Workbook wb;
    private final Appendable output;
    private boolean completeHTML;
    private Formatter out;
    private boolean gotBounds;
    private int firstColumn;
    private int endColumn;
    private HtmlHelper helper; // Assumindo que HSSFHtmlHelper estende/implementa isso

    private static final String DEFAULTS_CLASS = "excelDefaults";
    private static final String COL_HEAD_CLASS = "colHeader";
    private static final String ROW_HEAD_CLASS = "rowHeader";


    private static <K, V> Map<K, V> mapFor(Object... mapping) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < mapping.length; i += 2) {
            map.put((K) mapping[i], (V) mapping[i + 1]);
        }
        return map;
    }

    // CORREÇÃO: O map ALIGN estava misturando VerticalAlignment.CENTER
    private static final Map<HorizontalAlignment, String> ALIGN = mapFor(
            LEFT, "left",
            HorizontalAlignment.CENTER, "center", // CORRIGIDO: Estava VerticalAlignment.CENTER
            RIGHT, "right",
            FILL, "left",
            JUSTIFY, "left",
            CENTER_SELECTION, "center"
    );

    private static final Map<VerticalAlignment, String> VERTICAL_ALIGN = mapFor(
            BOTTOM, "bottom",
            org.apache.poi.ss.usermodel.VerticalAlignment.CENTER, "middle", // CORRIGIDO: Usando nome completo para evitar ambiguidade
            TOP, "top"
    );

    private static final Map<BorderStyle, String> BORDER = mapFor(
            DASH_DOT, "dashed 1pt",
            DASH_DOT_DOT, "dashed 1pt",
            DASHED, "dashed 1pt",
            DOTTED, "dotted 1pt",
            DOUBLE, "double 3pt",
            HAIR, "solid 1px",
            MEDIUM, "solid 2pt",
            MEDIUM_DASH_DOT, "dashed 2pt",
            MEDIUM_DASH_DOT_DOT, "dashed 2pt",
            MEDIUM_DASHED, "dashed 2pt",
            NONE, "none",
            SLANTED_DASH_DOT, "dashed 2pt",
            THICK, "solid 3pt",
            THIN, "dashed 1pt" // ATENÇÃO: No original estava "dashed 1pt", pode ser "solid 1pt"
    );


    /**
     * Creates a new converter to HTML for the given workbook.
     *
     * @param wb     The workbook.
     * @param output Where the HTML output will be written.
     * @return An object for converting the workbook to HTML.
     */
    public static ToHtml create(Workbook wb, Appendable output) {
        return new ToHtml(wb, output);
    }

    /**
     * Creates a new converter to HTML for the given workbook.  If the path ends
     * with "<tt>.xlsx</tt>" an {@link XSSFWorkbook} will be used; otherwise
     * this will use an {@link HSSFWorkbook}.
     *
     * @param path   The file that has the workbook.
     * @param output Where the HTML output will be written.
     * @return An object for converting the workbook to HTML.
     */
    public static ToHtml create(String path, Appendable output)
            throws IOException {
        return create(new FileInputStream(path), output);
    }

    /**
     * Creates a new converter to HTML for the given workbook.  This attempts to
     * detect whether the input is XML (so it should create an {@link
     * XSSFWorkbook} or not (so it should create an {@link HSSFWorkbook}).
     *
     * @param in     The input stream that has the workbook.
     * @param output Where the HTML output will be written.
     * @return An object for converting the workbook to HTML.
     */
    public static ToHtml create(InputStream in, Appendable output)
            throws IOException {
        try {
            Workbook wb = WorkbookFactory.create(in);
            return create(wb, output);
        } catch (IOException e) { // MUDANÇA: Capturando IOException (que WorkbookFactory lança)
            throw new IllegalArgumentException("Cannot create workbook from stream", e);
        }
    }

    private ToHtml(Workbook wb, Appendable output) {
        if (wb == null)
            throw new NullPointerException("wb");
        if (output == null)
            throw new NullPointerException("output");
        this.wb = wb;
        this.output = output;
        setupColorMap();
    }

    private void setupColorMap() {
        // ATUALIZADO: Lógica para suportar HSSF e XSSF (ou falhar de forma clara)
        if (wb instanceof HSSFWorkbook) {
            helper = new HSSFHtmlHelper((HSSFWorkbook) wb);
        } else if (wb instanceof XSSFWorkbook) {
            // TODO: Você precisa de um XSSFHtmlHelper para arquivos .xlsx
            // Você precisará criar uma classe 'XSSFHtmlHelper' que saiba
            // ler as cores de um XSSFWorkbook (que são diferentes do HSSF).
            // Ex: helper = new XSSFHtmlHelper((XSSFWorkbook) wb);
            //
            // Por enquanto, vamos lançar um erro claro:
            throw new IllegalArgumentException(
                    "Este conversor (ToHtml) foi configurado apenas para HSSF (arquivos .xls), " +
                    "mas recebeu um XSSF (arquivos .xlsx). " +
                    "Você precisa criar uma classe XSSFHtmlHelper correspondente.");
        } else {
            throw new IllegalArgumentException(
                    "Tipo de workbook desconhecido: " + wb.getClass().getSimpleName());
        }
    }

    /**
     * Run this class as a program
     *
     * @param args The command line arguments.
     * @throws Exception Exception we don't recover from.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("usage: ToHtml inputWorkbook outputHtmlFile");
            return;
        }

        // Usando try-with-resources para garantir que os writers sejam fechados
        try (PrintWriter writer = new PrintWriter(new FileWriter(args[1]))) {
            ToHtml toHtml = create(args[0], writer);
            toHtml.setCompleteHTML(true);
            toHtml.printPage();
        }
    }

    public void setCompleteHTML(boolean completeHTML) {
        this.completeHTML = completeHTML;
    }

    public void printPage() throws IOException {
        try {
            ensureOut();
            if (completeHTML) {
                out.format(
                        "<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>%n");
                out.format("<html>%n");
                out.format("<head>%n");
                out.format("</head>%n");
                out.format("<body>%n");
            }

            print();

            if (completeHTML) {
                out.format("</body>%n");
                out.format("</html>%n");
            }
        } finally {
            if (out != null)
                out.close();
            if (output instanceof Closeable) {
                Closeable closeable = (Closeable) output;
                closeable.close();
            }
        }
    }

    public void print() {
        printInlineStyle();
        printSheets();
    }

    private void printInlineStyle() {
        //out.format("<link href=\"excelStyle.css\" rel=\"stylesheet\" type=\"text/css\">%n");
        out.format("<style type=\"text/css\">%n");
        printStyles();
        out.format("</style>%n");
    }

    private void ensureOut() {
        if (out == null)
            out = new Formatter(output);
    }

    public void printStyles() {
        ensureOut();

        // First, copy the base css
        // ATUALIZADO: Usando try-with-resources (Java 7+)
        try (InputStream is = getClass().getResourceAsStream("excelStyle.css")) {
            if (is == null) {
                throw new IllegalStateException("Não foi possível encontrar 'excelStyle.css' no classpath.");
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = in.readLine()) != null) {
                    out.format("%s%n", line);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Reading standard css", e);
        }

        // now add css for each used style
        Set<CellStyle> seen = new HashSet<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            Iterator<Row> rows = sheet.rowIterator();
            while (rows.hasNext()) {
                Row row = rows.next();
                for (Cell cell : row) {
                    CellStyle style = cell.getCellStyle();
                    if (!seen.contains(style)) {
                        printStyle(style);
                        seen.add(style);
                    }
                }
            }
        }
    }

    private void printStyle(CellStyle style) {
        out.format(".%s .%s {%n", DEFAULTS_CLASS, styleName(style));
        styleContents(style);
        out.format("}%n");
    }

    private void styleContents(CellStyle style) {
        // CORREÇÃO: Usando getVerticalAlignment() para o alinhamento vertical
        styleOut("text-align", style.getAlignment(), ALIGN);
        styleOut("vertical-align", style.getVerticalAlignment(), VERTICAL_ALIGN);
        fontStyle(style);
        borderStyles(style);
        helper.colorStyles(style, out);
    }

    private void borderStyles(CellStyle style) {
        styleOut("border-left", style.getBorderLeft(), BORDER);
        styleOut("border-right", style.getBorderRight(), BORDER);
        styleOut("border-top", style.getBorderTop(), BORDER);
        styleOut("border-bottom", style.getBorderBottom(), BORDER);
    }

    private void fontStyle(CellStyle style) {
        // ATUALIZADO: getFontIndex() -> getFontIndexAsInt() (API moderna)
        Font font = wb.getFontAt(style.getFontIndexAsInt());

        // ATUALIZADO: getBoldweight() -> getBold() (API moderna)
        if (font.getBold())
            out.format("  font-weight: bold;%n");
        if (font.getItalic())
            out.format("  font-style: italic;%n");

        int fontheight = font.getFontHeightInPoints();
        if (fontheight == 9) {
            //fix for stupid ol Windows
            fontheight = 10;
        }
        out.format("  font-size: %dpt;%n", fontheight);

        // Font color is handled with the other colors by HtmlHelper
    }

    private String styleName(CellStyle style) {
        if (style == null)
            style = wb.getCellStyleAt((short) 0);
        StringBuilder sb = new StringBuilder();
        try (Formatter fmt = new Formatter(sb)) { // Usando try-with-resources
            fmt.format("style_%02x", style.getIndex());
            return fmt.toString();
        }
    }

    private <K> void styleOut(String attr, K key, Map<K, String> mapping) {
        String value = mapping.get(key);
        if (value != null) {
            out.format("  %s: %s;%n", attr, value);
        }
    }


    private void printSheets() {
        ensureOut();
        Sheet sheet = wb.getSheetAt(0);
        printSheet(sheet);
    }

    public void printSheet(Sheet sheet) {
        ensureOut();
        out.format("<table class=%s>%n", DEFAULTS_CLASS);
        printCols(sheet);
        printSheetContent(sheet);
        out.format("</table>%n");
    }

    private void printCols(Sheet sheet) {
        out.format("<col/>%n");
        ensureColumnBounds(sheet);
        for (int i = firstColumn; i < endColumn; i++) {
            out.format("<col/>%n");
        }
    }

    // ATUALIZADO: Usando a API moderna (CellType enum)
    private static CellType ultimateCellType(Cell c) {
        CellType type = c.getCellType();
        if (type == FORMULA)
            type = c.getCachedFormulaResultType();
        return type;
    }


    private void ensureColumnBounds(Sheet sheet) {
        if (gotBounds)
            return;

        Iterator<Row> iter = sheet.rowIterator();
        firstColumn = (iter.hasNext() ? Integer.MAX_VALUE : 0);
        endColumn = 0;
        while (iter.hasNext()) {
            Row row = iter.next();
            // ATUALIZADO: getFirstCellNum() retorna int, não short
            int firstCell = row.getFirstCellNum(); 
            if (firstCell >= 0) {
                firstColumn = Math.min(firstColumn, firstCell);
                endColumn = Math.max(endColumn, row.getLastCellNum());
            }
        }
        gotBounds = true;
    }

    private void printColumnHeads() {
        out.format("<thead>%n");
        out.format("  <tr class=%s>%n", COL_HEAD_CLASS);
        out.format("    <th class=%s>&#x25CA;</th>%n", COL_HEAD_CLASS);
        //noinspection UnusedDeclaration
        StringBuilder colName = new StringBuilder();
        for (int i = firstColumn; i < endColumn; i++) {
            colName.setLength(0);
            int cnum = i;
            do {
                colName.insert(0, (char) ('A' + cnum % 26));
                cnum /= 26; // Isso está incorreto para nomes de colunas (ex: Z -> AA), mas mantendo a lógica original
            } while (cnum > 0);
            out.format("    <th class=%s>%s</th>%n", COL_HEAD_CLASS, colName);
        }
        out.format("  </tr>%n");
        out.format("</thead>%n");
    }

    private void printSheetContent(Sheet sheet) {
        printColumnHeads();

        out.format("<tbody>%n");
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            Row row = rows.next();

            out.format("  <tr>%n");
            out.format("    <td class=%s>%d</td>%n", ROW_HEAD_CLASS,
                    row.getRowNum() + 1);
            for (int i = firstColumn; i < endColumn; i++) {
                String content = "&nbsp;";
                String attrs = "";
                CellStyle style = null;
                if (i >= row.getFirstCellNum() && i < row.getLastCellNum()) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        style = cell.getCellStyle();
                        attrs = tagStyle(cell, style);
                        //Set the value that is rendered for the cell
                        //also applies the format
                        CellFormat cf = CellFormat.getInstance(
                                style.getDataFormatString());
                        CellFormatResult result = cf.apply(cell);
                        content = result.text;
                        if (content.equals(""))
                            content = "&nbsp;";
                    }
                }
                out.format("    <td class=%s %s>%s</td>%n", styleName(style),
                        attrs, content);
            }
            out.format("  </tr>%n");
        }
        out.format("</tbody>%n");
    }

    private String tagStyle(Cell cell, CellStyle style) {
        // ATUALIZADO: A constante 'ALIGN_GENERAL' não existia/estava depreciada.
        // A API moderna usa o enum 'HorizontalAlignment.GENERAL' (importado estaticamente como 'GENERAL')
        if (style.getAlignment() == GENERAL) {
            // ATUALIZADO: Usando o 'ultimateCellType' moderno que retorna 'CellType' enum
            switch (ultimateCellType(cell)) {
                case STRING:
                    return "style=\"text-align: left;\"";
                case BOOLEAN:
                case ERROR:
                    return "style=\"text-align: center;\"";
                case NUMERIC:
                default:
                    // "right" is the default
                    break;
            }
        }
        return "";
    }
}