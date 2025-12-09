package negocio.facade.jdbc.processosel;

import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.poi.hssf.util.CellReference;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.CartaoRespostaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.crm.SegmentacaoProspect;
import negocio.interfaces.processosel.CartaoRespostaInterfaceFacade;
import relatorio.negocio.jdbc.processosel.CartaoRespostaRel;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class CartaoResposta extends ControleAcesso implements CartaoRespostaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String idEntidade;

	public CartaoResposta() throws Exception {
		super();
		setIdEntidade("CartaoResposta");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CartaoRespostaVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {

		try {
			obj.setInscricaotopocartao1("37.0");
			obj.setInscricaotopocartao2("37.0");
			obj.setSalatopocartao1("40.0");
			obj.setSalatopocartao2("40.0");
			obj.setCursotopocartao1("49.5");
			obj.setCursotopocartao2("49.5");
			for (int cartao = 1; cartao <= 2; cartao++) {
				for (int y = 1; y <= 10; y++) {
					String coordenadaX = ""; // cartao == 1 ?
												// String.valueOf((24.5+((y-1)*6.5)))
												// :
												// String.valueOf((106.5+((y-1)*6.5)))
												// ;
					switch (y) {
					case 1:// A
						coordenadaX = cartao == 1 ? "24,5" : "106,5";
						break;
					case 2:// B
						coordenadaX = cartao == 1 ? "31,0" : "113,0";
						break;
					case 3:// C
						coordenadaX = cartao == 1 ? "37,5" : "119,5";
						break;
					case 4:// D
						coordenadaX = cartao == 1 ? "44,0" : "126,0";
						break;
					case 5:// E
						coordenadaX = cartao == 1 ? "50,3" : "132,5";
						break;
					case 6:// F
						coordenadaX = cartao == 1 ? "56,0" : "138,5";
						break;
					case 7:// G
						coordenadaX = cartao == 1 ? "62,5" : "145,0";
						break;
					case 8:// H
						coordenadaX = cartao == 1 ? "69,0" : "151,0";
						break;
					case 9:// I
						coordenadaX = cartao == 1 ? "75,0" : "157,0";
						break;
					case 10:// J
						coordenadaX = cartao == 1 ? "81,0" : "163,5";
						break;
					default:
						break;
					}
					for (int x = 1; x <= 9; x++) {
						UtilReflexao.invocarMetodo(obj, "set" + CellReference.convertNumToColString(y - 1) + x + "Cartao" + cartao, coordenadaX);
						String coordenadaY = "";
						switch (x) {
						case 1:
							coordenadaY = "55,0";
							break;
						case 2:
							coordenadaY = "61,0";
							break;
						case 3:
							coordenadaY = "67,9";
							break;
						case 4:
							coordenadaY = "74,2";
							break;
						case 5:
							coordenadaY = "80,5";
							break;
						case 6:
							coordenadaY = "86,8";
							break;
						case 7:
							coordenadaY = "93,3";
							break;
						case 8:
							coordenadaY = "99,4";
							break;
						case 9:
							coordenadaY = "105,7";
							break;
						default:
							break;
						}
						UtilReflexao.invocarMetodo(obj, "setTopo" + x + "Cartao" + cartao, coordenadaY);
					}
				}
			}
			CartaoResposta.incluir(getIdEntidade(), validarAcesso, usuario);

			final String sql = "INSERT INTO cartaoresposta(inscricaotopocartao1, inscricaolinha1cartao1, salatopocartao1, salalinha2cartao1, cursotopocartao1, cursolinha3cartao1, a1cartao1, " + "b1cartao1, c1cartao1, d1cartao1, e1cartao1, f1cartao1, g1cartao1, h1cartao1, i1cartao1, j1cartao1, topo1cartao1, a2cartao1, b2cartao1, c2cartao1, d2cartao1, e2cartao1," + "f2cartao1, g2cartao1, h2cartao1, i2cartao1, j2cartao1, topo2cartao1, a3cartao1, b3cartao1, c3cartao1, d3cartao1, e3cartao1, f3cartao1, g3cartao1, h3cartao1, i3cartao1, " + "j3cartao1, topo3cartao1, a4cartao1, b4cartao1, c4cartao1, d4cartao1, e4cartao1, f4cartao1, g4cartao1, h4cartao1, i4cartao1, j4cartao1, topo4cartao1, a5cartao1, b5cartao1, " + "c5cartao1, d5cartao1, e5cartao1, f5cartao1, g5cartao1, h5cartao1, i5cartao1, j5cartao1, topo5cartao1, a6cartao1, b6cartao1, c6cartao1, d6cartao1, e6cartao1, f6cartao1, "
					+ "g6cartao1, h6cartao1, i6cartao1, j6cartao1, topo6cartao1, a7cartao1, b7cartao1, c7cartao1, d7cartao1, e7cartao1, f7cartao1, g7cartao1, h7cartao1, i7cartao1, j7cartao1, " + "topo7cartao1, a8cartao1, b8cartao1, c8cartao1, d8cartao1, e8cartao1, f8cartao1, g8cartao1, h8cartao1, i8cartao1, j8cartao1, topo8cartao1, a9cartao1, b9cartao1, c9cartao1, " + "d9cartao1, e9cartao1, f9cartao1, g9cartao1, h9cartao1, i9cartao1, j9cartao1, topo9cartao1, inscricaotopocartao2, inscricaolinha1cartao2, salatopocartao2, salalinha2cartao2, " + "cursotopocartao2, cursolinha3cartao2, a1cartao2, b1cartao2, c1cartao2, d1cartao2, e1cartao2, f1cartao2, g1cartao2, h1cartao2, i1cartao2, j1cartao2, topo1cartao2, a2cartao2, " + "b2cartao2, c2cartao2, d2cartao2, e2cartao2, f2cartao2, g2cartao2, h2cartao2, i2cartao2, j2cartao2, topo2cartao2, a3cartao2, b3cartao2, c3cartao2, d3cartao2, e3cartao2, "
					+ "f3cartao2, g3cartao2, h3cartao2, i3cartao2, j3cartao2, topo3cartao2, a4cartao2, b4cartao2, c4cartao2, d4cartao2, e4cartao2, f4cartao2, g4cartao2, h4cartao2, i4cartao2, " + "j4cartao2, topo4cartao2, a5cartao2, b5cartao2, c5cartao2, d5cartao2, e5cartao2, f5cartao2, g5cartao2, h5cartao2, i5cartao2, j5cartao2, topo5cartao2, a6cartao2, b6cartao2, " + "c6cartao2, d6cartao2, e6cartao2, f6cartao2, g6cartao2, h6cartao2, i6cartao2, j6cartao2, topo6cartao2, a7cartao2, b7cartao2, c7cartao2, d7cartao2, e7cartao2, f7cartao2, " + "g7cartao2, h7cartao2, i7cartao2, j7cartao2, topo7cartao2, a8cartao2, b8cartao2, c8cartao2, d8cartao2, e8cartao2, f8cartao2, g8cartao2, h8cartao2, i8cartao2, j8cartao2, " + "topo8cartao2, a9cartao2, b9cartao2,  c9cartao2, d9cartao2, e9cartao2, f9cartao2, g9cartao2, h9cartao2, i9cartao2, j9cartao2, topo9cartao2)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo;";

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlAlterar = conn.prepareStatement(sql);

					if ((!obj.getInscricaotopocartao1().equals(""))) {
						sqlAlterar.setDouble(1, Double.parseDouble(obj.getInscricaotopocartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(1, 37);
					}
					if ((!obj.getInscricaolinha1cartao1().equals(""))) {
						sqlAlterar.setDouble(2, Double.parseDouble(obj.getInscricaolinha1cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(2, 28.0);
					}
					if ((!obj.getSalatopocartao1().equals(""))) {
						sqlAlterar.setDouble(3, Double.parseDouble(obj.getSalatopocartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(3, 40);
					}
					if ((!obj.getSalalinha2cartao1().equals(""))) {
						sqlAlterar.setDouble(4, Double.parseDouble(obj.getSalalinha2cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(4, 28.0);
					}
					if ((!obj.getCursotopocartao1().equals(""))) {
						sqlAlterar.setDouble(5, Double.parseDouble(obj.getCursotopocartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(5, 48);
					}
					if ((!obj.getCursolinha3cartao1().equals(""))) {
						sqlAlterar.setDouble(6, Double.parseDouble(obj.getCursolinha3cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(6, 28.0);
					}

					if ((!obj.getA1Cartao1().equals(""))) {
						sqlAlterar.setDouble(7, Double.parseDouble(obj.getA1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(7, 27.0);
					}

					if ((!obj.getB1Cartao1().equals(""))) {
						sqlAlterar.setDouble(8, Double.parseDouble(obj.getB1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(8, 33.6);
					}
					if ((!obj.getC1Cartao1().equals(""))) {
						sqlAlterar.setDouble(9, Double.parseDouble(obj.getC1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(9, 40.0);
					}
					if ((!obj.getD1Cartao1().equals(""))) {
						sqlAlterar.setDouble(10, Double.parseDouble(obj.getD1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(10, 46.5);
					}
					if ((!obj.getE1Cartao1().equals(""))) {
						sqlAlterar.setDouble(11, Double.parseDouble(obj.getE1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(11, 53.0);
					}
					if ((!obj.getF1Cartao1().equals(""))) {
						sqlAlterar.setDouble(12, Double.parseDouble(obj.getF1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(12, 59.2);
					}
					if ((!obj.getG1Cartao1().equals(""))) {
						sqlAlterar.setDouble(13, Double.parseDouble(obj.getG1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(13, 65.1);
					}
					if ((!obj.getH1Cartao1().equals(""))) {
						sqlAlterar.setDouble(14, Double.parseDouble(obj.getH1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(14, 71.2);
					}
					if ((!obj.getI1Cartao1().equals(""))) {
						sqlAlterar.setDouble(15, Double.parseDouble(obj.getI1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(15, 77.7);
					}
					if ((!obj.getJ1Cartao1().equals(""))) {
						sqlAlterar.setDouble(16, Double.parseDouble(obj.getJ1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(16, 84.3);
					}
					if ((!obj.getTopo1Cartao1().equals(""))) {
						sqlAlterar.setDouble(17, Double.parseDouble(obj.getTopo1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(17, 57.3);
					}

					if ((!obj.getA2Cartao1().equals(""))) {
						sqlAlterar.setDouble(18, Double.parseDouble(obj.getA2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(18, 27.0);
					}
					if ((!obj.getB2Cartao1().equals(""))) {
						sqlAlterar.setDouble(19, Double.parseDouble(obj.getB2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(19, 33.6);
					}
					if ((!obj.getC2Cartao1().equals(""))) {
						sqlAlterar.setDouble(20, Double.parseDouble(obj.getC2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(20, 40.0);
					}
					if ((!obj.getD2Cartao1().equals(""))) {
						sqlAlterar.setDouble(21, Double.parseDouble(obj.getD2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(21, 46.5);
					}
					if ((!obj.getE2Cartao1().equals(""))) {
						sqlAlterar.setDouble(22, Double.parseDouble(obj.getE2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(22, 53.0);
					}
					if ((!obj.getF2Cartao1().equals(""))) {
						sqlAlterar.setDouble(23, Double.parseDouble(obj.getF2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(23, 59.2);
					}
					if ((!obj.getG2Cartao1().equals(""))) {
						sqlAlterar.setDouble(24, Double.parseDouble(obj.getG2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(24, 65.1);
					}
					if ((!obj.getH2Cartao1().equals(""))) {
						sqlAlterar.setDouble(25, Double.parseDouble(obj.getH2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(25, 71.2);
					}
					if ((!obj.getI2Cartao1().equals(""))) {
						sqlAlterar.setDouble(26, Double.parseDouble(obj.getI2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(26, 77.7);
					}
					if ((!obj.getJ2Cartao1().equals(""))) {
						sqlAlterar.setDouble(27, Double.parseDouble(obj.getJ2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(27, 84.3);
					}
					if ((!obj.getTopo2Cartao1().equals(""))) {
						sqlAlterar.setDouble(28, Double.parseDouble(obj.getTopo2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(28, 63.6);
					}

					if ((!obj.getA3Cartao1().equals(""))) {
						sqlAlterar.setDouble(29, Double.parseDouble(obj.getA3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(29, 27.0);
					}
					if ((!obj.getB3Cartao1().equals(""))) {
						sqlAlterar.setDouble(30, Double.parseDouble(obj.getB3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(30, 33.6);
					}
					if ((!obj.getC3Cartao1().equals(""))) {
						sqlAlterar.setDouble(31, Double.parseDouble(obj.getC3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(31, 40.0);
					}
					if ((!obj.getD3Cartao1().equals(""))) {
						sqlAlterar.setDouble(32, Double.parseDouble(obj.getD3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(32, 46.5);
					}
					if ((!obj.getE3Cartao1().equals(""))) {
						sqlAlterar.setDouble(33, Double.parseDouble(obj.getE3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(33, 53.0);
					}
					if ((!obj.getF3Cartao1().equals(""))) {
						sqlAlterar.setDouble(34, Double.parseDouble(obj.getF3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(34, 59.2);
					}
					if ((!obj.getG3Cartao1().equals(""))) {
						sqlAlterar.setDouble(35, Double.parseDouble(obj.getG3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(35, 65.1);
					}
					if ((!obj.getH3Cartao1().equals(""))) {
						sqlAlterar.setDouble(36, Double.parseDouble(obj.getH3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(36, 71.2);
					}
					if ((!obj.getI3Cartao1().equals(""))) {
						sqlAlterar.setDouble(37, Double.parseDouble(obj.getI3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(37, 77.7);
					}
					if ((!obj.getJ3Cartao1().equals(""))) {
						sqlAlterar.setDouble(38, Double.parseDouble(obj.getJ3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(38, 84.3);
					}

					if ((!obj.getTopo3Cartao1().equals(""))) {
						sqlAlterar.setDouble(39, Double.parseDouble(obj.getTopo3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(39, 69.9);
					}

					if ((!obj.getA4Cartao1().equals(""))) {
						sqlAlterar.setDouble(40, Double.parseDouble(obj.getA4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(40, 27.0);
					}
					if ((!obj.getB4Cartao1().equals(""))) {
						sqlAlterar.setDouble(41, Double.parseDouble(obj.getB4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(41, 33.6);
					}
					if ((!obj.getC4Cartao1().equals(""))) {
						sqlAlterar.setDouble(42, Double.parseDouble(obj.getC4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(42, 40.0);
					}
					if ((!obj.getD4Cartao1().equals(""))) {
						sqlAlterar.setDouble(43, Double.parseDouble(obj.getD4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(43, 46.5);
					}
					if ((!obj.getE4Cartao1().equals(""))) {
						sqlAlterar.setDouble(44, Double.parseDouble(obj.getE4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(44, 53.0);
					}
					if ((!obj.getF4Cartao1().equals(""))) {
						sqlAlterar.setDouble(45, Double.parseDouble(obj.getF4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(45, 59.2);
					}
					if ((!obj.getG4Cartao1().equals(""))) {
						sqlAlterar.setDouble(46, Double.parseDouble(obj.getG4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(46, 65.1);
					}
					if ((!obj.getH4Cartao1().equals(""))) {
						sqlAlterar.setDouble(47, Double.parseDouble(obj.getH4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(47, 71.2);
					}
					if ((!obj.getI4Cartao1().equals(""))) {
						sqlAlterar.setDouble(48, Double.parseDouble(obj.getI4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(48, 77.7);
					}
					if ((!obj.getJ4Cartao1().equals(""))) {
						sqlAlterar.setDouble(49, Double.parseDouble(obj.getJ4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(49, 84.3);
					}

					if ((!obj.getTopo4Cartao1().equals(""))) {
						sqlAlterar.setDouble(50, Double.parseDouble(obj.getTopo4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(50, 76.2);
					}

					if ((!obj.getA5Cartao1().equals(""))) {
						sqlAlterar.setDouble(51, Double.parseDouble(obj.getA5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(51, 27.0);
					}
					if ((!obj.getB5Cartao1().equals(""))) {
						sqlAlterar.setDouble(52, Double.parseDouble(obj.getB5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(52, 33.6);
					}
					if ((!obj.getC5Cartao1().equals(""))) {
						sqlAlterar.setDouble(53, Double.parseDouble(obj.getC5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(53, 40.0);
					}
					if ((!obj.getD5Cartao1().equals(""))) {
						sqlAlterar.setDouble(54, Double.parseDouble(obj.getD5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(54, 46.5);
					}
					if ((!obj.getE5Cartao1().equals(""))) {
						sqlAlterar.setDouble(55, Double.parseDouble(obj.getE5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(55, 53.0);
					}
					if ((!obj.getF5Cartao1().equals(""))) {
						sqlAlterar.setDouble(56, Double.parseDouble(obj.getF5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(56, 59.2);
					}
					if ((!obj.getG5Cartao1().equals(""))) {
						sqlAlterar.setDouble(57, Double.parseDouble(obj.getG5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(57, 65.1);
					}
					if ((!obj.getH5Cartao1().equals(""))) {
						sqlAlterar.setDouble(58, Double.parseDouble(obj.getH5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(58, 71.2);
					}
					if ((!obj.getI5Cartao1().equals(""))) {
						sqlAlterar.setDouble(59, Double.parseDouble(obj.getI5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(59, 77.7);
					}
					if ((!obj.getJ5Cartao1().equals(""))) {
						sqlAlterar.setDouble(60, Double.parseDouble(obj.getJ5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(60, 84.3);
					}

					if ((!obj.getTopo5Cartao1().equals(""))) {
						sqlAlterar.setDouble(61, Double.parseDouble(obj.getTopo5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(61, 82.5);
					}

					if ((!obj.getA6Cartao1().equals(""))) {
						sqlAlterar.setDouble(62, Double.parseDouble(obj.getA6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(62, 27.0);
					}
					if ((!obj.getB6Cartao1().equals(""))) {
						sqlAlterar.setDouble(63, Double.parseDouble(obj.getB6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(63, 33.6);
					}
					if ((!obj.getC6Cartao1().equals(""))) {
						sqlAlterar.setDouble(64, Double.parseDouble(obj.getC6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(64, 40.0);
					}
					if ((!obj.getD6Cartao1().equals(""))) {
						sqlAlterar.setDouble(65, Double.parseDouble(obj.getD6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(65, 46.5);
					}
					if ((!obj.getE6Cartao1().equals(""))) {
						sqlAlterar.setDouble(66, Double.parseDouble(obj.getE6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(66, 53.0);
					}
					if ((!obj.getF6Cartao1().equals(""))) {
						sqlAlterar.setDouble(67, Double.parseDouble(obj.getF6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(67, 59.2);
					}
					if ((!obj.getG6Cartao1().equals(""))) {
						sqlAlterar.setDouble(68, Double.parseDouble(obj.getG6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(68, 65.1);
					}
					if ((!obj.getH6Cartao1().equals(""))) {
						sqlAlterar.setDouble(69, Double.parseDouble(obj.getH6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(69, 71.2);
					}
					if ((!obj.getI6Cartao1().equals(""))) {
						sqlAlterar.setDouble(70, Double.parseDouble(obj.getI6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(70, 77.7);
					}
					if ((!obj.getJ6Cartao1().equals(""))) {
						sqlAlterar.setDouble(71, Double.parseDouble(obj.getJ6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(71, 84.3);
					}

					if ((!obj.getTopo6Cartao1().equals(""))) {
						sqlAlterar.setDouble(72, Double.parseDouble(obj.getTopo6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(72, 88.8);
					}

					if ((!obj.getA7Cartao1().equals(""))) {
						sqlAlterar.setDouble(73, Double.parseDouble(obj.getA7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(73, 27.0);
					}
					if ((!obj.getB7Cartao1().equals(""))) {
						sqlAlterar.setDouble(74, Double.parseDouble(obj.getB7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(74, 33.6);
					}
					if ((!obj.getC7Cartao1().equals(""))) {
						sqlAlterar.setDouble(75, Double.parseDouble(obj.getC7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(75, 40.0);
					}
					if ((!obj.getD7Cartao1().equals(""))) {
						sqlAlterar.setDouble(76, Double.parseDouble(obj.getD7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(76, 46.5);
					}
					if ((!obj.getE7Cartao1().equals(""))) {
						sqlAlterar.setDouble(77, Double.parseDouble(obj.getE7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(77, 53.0);
					}
					if ((!obj.getF7Cartao1().equals(""))) {
						sqlAlterar.setDouble(78, Double.parseDouble(obj.getF7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(78, 59.2);
					}
					if ((!obj.getG7Cartao1().equals(""))) {
						sqlAlterar.setDouble(79, Double.parseDouble(obj.getG7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(79, 65.1);
					}
					if ((!obj.getH7Cartao1().equals(""))) {
						sqlAlterar.setDouble(80, Double.parseDouble(obj.getH7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(80, 71.2);
					}
					if ((!obj.getI7Cartao1().equals(""))) {
						sqlAlterar.setDouble(81, Double.parseDouble(obj.getI7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(81, 77.7);
					}
					if ((!obj.getJ7Cartao1().equals(""))) {
						sqlAlterar.setDouble(82, Double.parseDouble(obj.getJ7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(82, 84.3);
					}

					if ((!obj.getTopo7Cartao1().equals(""))) {
						sqlAlterar.setDouble(83, Double.parseDouble(obj.getTopo7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(83, 95.1);
					}

					if ((!obj.getA8Cartao1().equals(""))) {
						sqlAlterar.setDouble(84, Double.parseDouble(obj.getA8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(84, 27.0);
					}
					if ((!obj.getB8Cartao1().equals(""))) {
						sqlAlterar.setDouble(85, Double.parseDouble(obj.getB8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(85, 33.6);
					}
					if ((!obj.getC8Cartao1().equals(""))) {
						sqlAlterar.setDouble(86, Double.parseDouble(obj.getC8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(86, 40.0);
					}
					if ((!obj.getD8Cartao1().equals(""))) {
						sqlAlterar.setDouble(87, Double.parseDouble(obj.getD8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(87, 46.5);
					}
					if ((!obj.getE8Cartao1().equals(""))) {
						sqlAlterar.setDouble(88, Double.parseDouble(obj.getE8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(88, 53.0);
					}
					if ((!obj.getF8Cartao1().equals(""))) {
						sqlAlterar.setDouble(89, Double.parseDouble(obj.getF8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(89, 59.2);
					}
					if ((!obj.getG8Cartao1().equals(""))) {
						sqlAlterar.setDouble(90, Double.parseDouble(obj.getG8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(90, 65.1);
					}
					if ((!obj.getH8Cartao1().equals(""))) {
						sqlAlterar.setDouble(91, Double.parseDouble(obj.getH8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(91, 71.2);
					}
					if ((!obj.getI8Cartao1().equals(""))) {
						sqlAlterar.setDouble(92, Double.parseDouble(obj.getI8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(92, 77.7);
					}
					if ((!obj.getJ8Cartao1().equals(""))) {
						sqlAlterar.setDouble(93, Double.parseDouble(obj.getJ8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(93, 84.3);
					}

					if ((!obj.getTopo8Cartao1().equals(""))) {
						sqlAlterar.setDouble(94, Double.parseDouble(obj.getTopo8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(94, 101.4);
					}

					if ((!obj.getA9Cartao1().equals(""))) {
						sqlAlterar.setDouble(95, Double.parseDouble(obj.getA9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(95, 27.0);
					}
					if ((!obj.getB9Cartao1().equals(""))) {
						sqlAlterar.setDouble(96, Double.parseDouble(obj.getB9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(96, 33.6);
					}
					if ((!obj.getC9Cartao1().equals(""))) {
						sqlAlterar.setDouble(97, Double.parseDouble(obj.getC9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(97, 40.0);
					}
					if ((!obj.getD9Cartao1().equals(""))) {
						sqlAlterar.setDouble(98, Double.parseDouble(obj.getD9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(98, 46.5);
					}
					if ((!obj.getE9Cartao1().equals(""))) {
						sqlAlterar.setDouble(99, Double.parseDouble(obj.getE9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(99, 53.0);
					}
					if ((!obj.getF9Cartao1().equals(""))) {
						sqlAlterar.setDouble(100, Double.parseDouble(obj.getF9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(100, 59.2);
					}
					if ((!obj.getG9Cartao1().equals(""))) {
						sqlAlterar.setDouble(101, Double.parseDouble(obj.getG9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(101, 65.1);
					}
					if ((!obj.getH9Cartao1().equals(""))) {
						sqlAlterar.setDouble(102, Double.parseDouble(obj.getH9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(102, 71.2);
					}
					if ((!obj.getI9Cartao1().equals(""))) {
						sqlAlterar.setDouble(103, Double.parseDouble(obj.getI9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(103, 77.7);
					}
					if ((!obj.getJ9Cartao1().equals(""))) {
						sqlAlterar.setDouble(104, Double.parseDouble(obj.getJ9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(104, 84.3);
					}

					if ((!obj.getTopo9Cartao1().equals(""))) {
						sqlAlterar.setDouble(105, Double.parseDouble(obj.getTopo9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(105, 107.7);
					}

					if ((!obj.getInscricaotopocartao2().equals(""))) {
						sqlAlterar.setDouble(106, Double.parseDouble(obj.getInscricaotopocartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(106, 40.5);
					}
					if ((!obj.getInscricaolinha1cartao2().equals(""))) {
						sqlAlterar.setDouble(107, Double.parseDouble(obj.getInscricaolinha1cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(107, 110.0);
					}
					if ((!obj.getSalatopocartao2().equals(""))) {
						sqlAlterar.setDouble(108, Double.parseDouble(obj.getSalatopocartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(108, 43.5);
					}
					if ((!obj.getSalalinha2cartao2().equals(""))) {
						sqlAlterar.setDouble(109, Double.parseDouble(obj.getSalalinha2cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(109, 110.0);
					}
					if ((!obj.getCursotopocartao2().equals(""))) {
						sqlAlterar.setDouble(110, Double.parseDouble(obj.getCursotopocartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(110, 50.5);
					}
					if ((!obj.getCursolinha3cartao2().equals(""))) {
						sqlAlterar.setDouble(111, Double.parseDouble(obj.getCursolinha3cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(111, 110.0);
					}

					if ((!obj.getA1Cartao2().equals(""))) {
						sqlAlterar.setDouble(112, Double.parseDouble(obj.getA1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(112, 109.6);
					}

					if ((!obj.getB1Cartao2().equals(""))) {
						sqlAlterar.setDouble(113, Double.parseDouble(obj.getB1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(113, 116.3);
					}
					if ((!obj.getC1Cartao2().equals(""))) {
						sqlAlterar.setDouble(114, Double.parseDouble(obj.getC1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(114, 123.1);
					}
					if ((!obj.getD1Cartao2().equals(""))) {
						sqlAlterar.setDouble(115, Double.parseDouble(obj.getD1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(115, 129.2);
					}
					if ((!obj.getE1Cartao2().equals(""))) {
						sqlAlterar.setDouble(116, Double.parseDouble(obj.getE1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(116, 135.6);
					}
					if ((!obj.getF1Cartao2().equals(""))) {
						sqlAlterar.setDouble(117, Double.parseDouble(obj.getF1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(117, 141.9);
					}
					if ((!obj.getG1Cartao2().equals(""))) {
						sqlAlterar.setDouble(118, Double.parseDouble(obj.getG1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(118, 148.1);
					}
					if ((!obj.getH1Cartao2().equals(""))) {
						sqlAlterar.setDouble(119, Double.parseDouble(obj.getH1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(119, 157.9);
					}
					if ((!obj.getI1Cartao2().equals(""))) {
						sqlAlterar.setDouble(120, Double.parseDouble(obj.getI1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(120, 160.5);
					}
					if ((!obj.getJ1Cartao2().equals(""))) {
						sqlAlterar.setDouble(121, Double.parseDouble(obj.getJ1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(121, 166.8);
					}

					if ((!obj.getTopo1Cartao2().equals(""))) {
						sqlAlterar.setDouble(122, Double.parseDouble(obj.getTopo1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(122, 57.3);
					}

					if ((!obj.getA2Cartao2().equals(""))) {
						sqlAlterar.setDouble(123, Double.parseDouble(obj.getA2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(123, 109.6);
					}

					if ((!obj.getB2Cartao2().equals(""))) {
						sqlAlterar.setDouble(124, Double.parseDouble(obj.getB2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(124, 116.3);
					}
					if ((!obj.getC2Cartao2().equals(""))) {
						sqlAlterar.setDouble(125, Double.parseDouble(obj.getC2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(125, 123.1);
					}
					if ((!obj.getD2Cartao2().equals(""))) {
						sqlAlterar.setDouble(126, Double.parseDouble(obj.getD2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(126, 129.2);
					}
					if ((!obj.getE2Cartao2().equals(""))) {
						sqlAlterar.setDouble(127, Double.parseDouble(obj.getE2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(127, 135.6);
					}
					if ((!obj.getF2Cartao2().equals(""))) {
						sqlAlterar.setDouble(128, Double.parseDouble(obj.getF2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(128, 141.9);
					}
					if ((!obj.getG2Cartao2().equals(""))) {
						sqlAlterar.setDouble(129, Double.parseDouble(obj.getG2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(129, 148.1);
					}
					if ((!obj.getH2Cartao2().equals(""))) {
						sqlAlterar.setDouble(130, Double.parseDouble(obj.getH2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(130, 157.9);
					}
					if ((!obj.getI2Cartao2().equals(""))) {
						sqlAlterar.setDouble(131, Double.parseDouble(obj.getI2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(131, 160.5);
					}
					if ((!obj.getJ2Cartao2().equals(""))) {
						sqlAlterar.setDouble(132, Double.parseDouble(obj.getJ2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(132, 166.8);
					}

					if ((!obj.getTopo2Cartao2().equals(""))) {
						sqlAlterar.setDouble(133, Double.parseDouble(obj.getTopo2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(133, 63.6);
					}

					if ((!obj.getA3Cartao2().equals(""))) {
						sqlAlterar.setDouble(134, Double.parseDouble(obj.getA3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(134, 109.6);
					}

					if ((!obj.getB3Cartao2().equals(""))) {
						sqlAlterar.setDouble(135, Double.parseDouble(obj.getB3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(135, 116.3);
					}
					if ((!obj.getC3Cartao2().equals(""))) {
						sqlAlterar.setDouble(136, Double.parseDouble(obj.getC3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(136, 123.1);
					}
					if ((!obj.getD3Cartao2().equals(""))) {
						sqlAlterar.setDouble(137, Double.parseDouble(obj.getD3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(137, 129.2);
					}
					if ((!obj.getE3Cartao2().equals(""))) {
						sqlAlterar.setDouble(138, Double.parseDouble(obj.getE3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(138, 135.6);
					}
					if ((!obj.getF3Cartao2().equals(""))) {
						sqlAlterar.setDouble(139, Double.parseDouble(obj.getF3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(139, 141.9);
					}
					if ((!obj.getG3Cartao2().equals(""))) {
						sqlAlterar.setDouble(140, Double.parseDouble(obj.getG3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(140, 148.1);
					}
					if ((!obj.getH3Cartao2().equals(""))) {
						sqlAlterar.setDouble(141, Double.parseDouble(obj.getH3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(141, 157.9);
					}
					if ((!obj.getI3Cartao2().equals(""))) {
						sqlAlterar.setDouble(142, Double.parseDouble(obj.getI3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(142, 160.5);
					}
					if ((!obj.getJ3Cartao2().equals(""))) {
						sqlAlterar.setDouble(143, Double.parseDouble(obj.getJ3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(143, 166.8);
					}

					if ((!obj.getTopo3Cartao2().equals(""))) {
						sqlAlterar.setDouble(144, Double.parseDouble(obj.getTopo3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(144, 69.9);
					}

					if ((!obj.getA4Cartao2().equals(""))) {
						sqlAlterar.setDouble(145, Double.parseDouble(obj.getA4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(145, 109.6);
					}

					if ((!obj.getB4Cartao2().equals(""))) {
						sqlAlterar.setDouble(146, Double.parseDouble(obj.getB4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(146, 116.3);
					}
					if ((!obj.getC4Cartao2().equals(""))) {
						sqlAlterar.setDouble(147, Double.parseDouble(obj.getC4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(147, 123.1);
					}
					if ((!obj.getD4Cartao2().equals(""))) {
						sqlAlterar.setDouble(148, Double.parseDouble(obj.getD4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(148, 129.2);
					}
					if ((!obj.getE4Cartao2().equals(""))) {
						sqlAlterar.setDouble(149, Double.parseDouble(obj.getE4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(149, 135.6);
					}
					if ((!obj.getF4Cartao2().equals(""))) {
						sqlAlterar.setDouble(150, Double.parseDouble(obj.getF4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(150, 141.9);
					}
					if ((!obj.getG4Cartao2().equals(""))) {
						sqlAlterar.setDouble(151, Double.parseDouble(obj.getG4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(151, 148.1);
					}
					if ((!obj.getH4Cartao2().equals(""))) {
						sqlAlterar.setDouble(152, Double.parseDouble(obj.getH4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(152, 157.9);
					}
					if ((!obj.getI4Cartao2().equals(""))) {
						sqlAlterar.setDouble(153, Double.parseDouble(obj.getI4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(153, 160.5);
					}
					if ((!obj.getJ4Cartao2().equals(""))) {
						sqlAlterar.setDouble(154, Double.parseDouble(obj.getJ4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(154, 166.8);
					}

					if ((!obj.getTopo4Cartao2().equals(""))) {
						sqlAlterar.setDouble(155, Double.parseDouble(obj.getTopo4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(155, 76.2);
					}

					if ((!obj.getA5Cartao2().equals(""))) {
						sqlAlterar.setDouble(156, Double.parseDouble(obj.getA5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(156, 109.6);
					}

					if ((!obj.getB5Cartao2().equals(""))) {
						sqlAlterar.setDouble(157, Double.parseDouble(obj.getB5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(157, 116.3);
					}
					if ((!obj.getC5Cartao2().equals(""))) {
						sqlAlterar.setDouble(158, Double.parseDouble(obj.getC5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(158, 123.1);
					}
					if ((!obj.getD5Cartao2().equals(""))) {
						sqlAlterar.setDouble(159, Double.parseDouble(obj.getD5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(159, 129.2);
					}
					if ((!obj.getE5Cartao2().equals(""))) {
						sqlAlterar.setDouble(160, Double.parseDouble(obj.getE5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(160, 135.6);
					}
					if ((!obj.getF5Cartao2().equals(""))) {
						sqlAlterar.setDouble(161, Double.parseDouble(obj.getF5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(161, 141.9);
					}
					if ((!obj.getG5Cartao2().equals(""))) {
						sqlAlterar.setDouble(162, Double.parseDouble(obj.getG5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(162, 148.1);
					}
					if ((!obj.getH5Cartao2().equals(""))) {
						sqlAlterar.setDouble(163, Double.parseDouble(obj.getH5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(163, 157.9);
					}
					if ((!obj.getI5Cartao2().equals(""))) {
						sqlAlterar.setDouble(164, Double.parseDouble(obj.getI5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(164, 160.5);
					}
					if ((!obj.getJ1Cartao2().equals(""))) {
						sqlAlterar.setDouble(165, Double.parseDouble(obj.getJ1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(165, 166.8);
					}

					if ((!obj.getTopo5Cartao2().equals(""))) {
						sqlAlterar.setDouble(166, Double.parseDouble(obj.getTopo5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(166, 82.5);
					}

					if ((!obj.getA6Cartao2().equals(""))) {
						sqlAlterar.setDouble(167, Double.parseDouble(obj.getA6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(167, 109.6);
					}

					if ((!obj.getB6Cartao2().equals(""))) {
						sqlAlterar.setDouble(168, Double.parseDouble(obj.getB6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(168, 116.3);
					}
					if ((!obj.getC6Cartao2().equals(""))) {
						sqlAlterar.setDouble(169, Double.parseDouble(obj.getC6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(169, 123.1);
					}
					if ((!obj.getD6Cartao2().equals(""))) {
						sqlAlterar.setDouble(170, Double.parseDouble(obj.getD6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(170, 129.2);
					}
					if ((!obj.getE6Cartao2().equals(""))) {
						sqlAlterar.setDouble(171, Double.parseDouble(obj.getE6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(171, 135.6);
					}
					if ((!obj.getF6Cartao2().equals(""))) {
						sqlAlterar.setDouble(172, Double.parseDouble(obj.getF6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(172, 141.9);
					}
					if ((!obj.getG6Cartao2().equals(""))) {
						sqlAlterar.setDouble(173, Double.parseDouble(obj.getG6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(173, 148.1);
					}
					if ((!obj.getH6Cartao2().equals(""))) {
						sqlAlterar.setDouble(174, Double.parseDouble(obj.getH6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(174, 157.9);
					}
					if ((!obj.getI6Cartao2().equals(""))) {
						sqlAlterar.setDouble(175, Double.parseDouble(obj.getI6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(175, 160.5);
					}
					if ((!obj.getJ6Cartao2().equals(""))) {
						sqlAlterar.setDouble(176, Double.parseDouble(obj.getJ6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(176, 166.8);
					}

					if ((!obj.getTopo6Cartao2().equals(""))) {
						sqlAlterar.setDouble(177, Double.parseDouble(obj.getTopo6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(177, 88.8);
					}

					if ((!obj.getA7Cartao2().equals(""))) {
						sqlAlterar.setDouble(178, Double.parseDouble(obj.getA7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(178, 109.6);
					}

					if ((!obj.getB7Cartao2().equals(""))) {
						sqlAlterar.setDouble(179, Double.parseDouble(obj.getB7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(179, 116.3);
					}
					if ((!obj.getC7Cartao2().equals(""))) {
						sqlAlterar.setDouble(180, Double.parseDouble(obj.getC7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(180, 123.1);
					}
					if ((!obj.getD7Cartao2().equals(""))) {
						sqlAlterar.setDouble(181, Double.parseDouble(obj.getD7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(181, 129.2);
					}
					if ((!obj.getE7Cartao2().equals(""))) {
						sqlAlterar.setDouble(182, Double.parseDouble(obj.getE7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(182, 135.6);
					}
					if ((!obj.getF7Cartao2().equals(""))) {
						sqlAlterar.setDouble(183, Double.parseDouble(obj.getF7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(183, 141.9);
					}
					if ((!obj.getG7Cartao2().equals(""))) {
						sqlAlterar.setDouble(184, Double.parseDouble(obj.getG7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(184, 148.1);
					}
					if ((!obj.getH7Cartao2().equals(""))) {
						sqlAlterar.setDouble(185, Double.parseDouble(obj.getH7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(185, 157.9);
					}
					if ((!obj.getI7Cartao2().equals(""))) {
						sqlAlterar.setDouble(186, Double.parseDouble(obj.getI7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(186, 160.5);
					}
					if ((!obj.getJ7Cartao2().equals(""))) {
						sqlAlterar.setDouble(187, Double.parseDouble(obj.getJ7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(187, 166.8);
					}

					if ((!obj.getTopo7Cartao2().equals(""))) {
						sqlAlterar.setDouble(188, Double.parseDouble(obj.getTopo7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(188, 95.1);
					}

					if ((!obj.getA8Cartao2().equals(""))) {
						sqlAlterar.setDouble(189, Double.parseDouble(obj.getA8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(189, 109.6);
					}

					if ((!obj.getB8Cartao2().equals(""))) {
						sqlAlterar.setDouble(190, Double.parseDouble(obj.getB8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(190, 116.3);
					}
					if ((!obj.getC8Cartao2().equals(""))) {
						sqlAlterar.setDouble(191, Double.parseDouble(obj.getC8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(191, 123.1);
					}
					if ((!obj.getD8Cartao2().equals(""))) {
						sqlAlterar.setDouble(192, Double.parseDouble(obj.getD8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(192, 129.2);
					}
					if ((!obj.getE8Cartao2().equals(""))) {
						sqlAlterar.setDouble(193, Double.parseDouble(obj.getE8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(193, 135.6);
					}
					if ((!obj.getF8Cartao2().equals(""))) {
						sqlAlterar.setDouble(194, Double.parseDouble(obj.getF8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(194, 141.9);
					}
					if ((!obj.getG8Cartao2().equals(""))) {
						sqlAlterar.setDouble(195, Double.parseDouble(obj.getG8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(195, 148.1);
					}
					if ((!obj.getH8Cartao2().equals(""))) {
						sqlAlterar.setDouble(196, Double.parseDouble(obj.getH8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(196, 157.9);
					}
					if ((!obj.getI8Cartao2().equals(""))) {
						sqlAlterar.setDouble(197, Double.parseDouble(obj.getI8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(197, 160.5);
					}
					if ((!obj.getJ8Cartao2().equals(""))) {
						sqlAlterar.setDouble(198, Double.parseDouble(obj.getJ8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(198, 166.8);
					}

					if ((!obj.getTopo8Cartao2().equals(""))) {
						sqlAlterar.setDouble(199, Double.parseDouble(obj.getTopo8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(199, 101.4);
					}

					if ((!obj.getA9Cartao2().equals(""))) {
						sqlAlterar.setDouble(200, Double.parseDouble(obj.getA9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(200, 109.6);
					}

					if ((!obj.getB9Cartao2().equals(""))) {
						sqlAlterar.setDouble(201, Double.parseDouble(obj.getB9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(201, 116.3);
					}
					if ((!obj.getC9Cartao2().equals(""))) {
						sqlAlterar.setDouble(202, Double.parseDouble(obj.getC9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(202, 123.1);
					}
					if ((!obj.getD9Cartao2().equals(""))) {
						sqlAlterar.setDouble(203, Double.parseDouble(obj.getD9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(203, 129.2);
					}
					if ((!obj.getE9Cartao2().equals(""))) {
						sqlAlterar.setDouble(204, Double.parseDouble(obj.getE9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(204, 135.6);
					}
					if ((!obj.getF9Cartao2().equals(""))) {
						sqlAlterar.setDouble(205, Double.parseDouble(obj.getF9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(205, 141.9);
					}
					if ((!obj.getG9Cartao2().equals(""))) {
						sqlAlterar.setDouble(206, Double.parseDouble(obj.getG9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(206, 148.1);
					}
					if ((!obj.getH9Cartao2().equals(""))) {
						sqlAlterar.setDouble(207, Double.parseDouble(obj.getH9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(207, 157.9);
					}
					if ((!obj.getI9Cartao2().equals(""))) {
						sqlAlterar.setDouble(208, Double.parseDouble(obj.getI9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(208, 160.5);
					}
					if ((!obj.getJ9Cartao2().equals(""))) {
						sqlAlterar.setDouble(209, Double.parseDouble(obj.getJ9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(209, 166.8);
					}

					if ((!obj.getTopo9Cartao2().equals(""))) {
						sqlAlterar.setDouble(210, Double.parseDouble(obj.getTopo9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(210, 107.7);
					}

					return sqlAlterar;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet res) throws SQLException, DataAccessException {
					if (res.next()) {
						return res.getInt("codigo");
					}
					return null;
				}
			}));

		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Override
	public CartaoRespostaVO consultarCartaoResposta(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from cartaoresposta";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CartaoRespostaVO obj, UsuarioVO usuario) throws Exception {

		try {

			SegmentacaoProspect.alterar(getIdEntidade());
			final String sql = "UPDATE cartaoresposta SET inscricaotopocartao1=?, inscricaolinha1cartao1=?, salatopocartao1=?, " + "salalinha2cartao1=?, cursotopocartao1=?, cursolinha3cartao1=?, a1cartao1=?, b1cartao1=?, c1cartao1=?, " + "d1cartao1=?, e1cartao1=?, f1cartao1=?, g1cartao1=?, h1cartao1=?, i1cartao1=?, j1cartao1=?, topo1cartao1=?, " + "a2cartao1=?, b2cartao1=?, c2cartao1=?, d2cartao1=?, e2cartao1=?, f2cartao1=?, g2cartao1=?, h2cartao1=?, " + "i2cartao1=?, j2cartao1=?, topo2cartao1=?, a3cartao1=?, b3cartao1=?, c3cartao1=?, d3cartao1=?, e3cartao1=?, " + "f3cartao1=?, g3cartao1=?, h3cartao1=?, i3cartao1=?, j3cartao1=?, topo3cartao1=?, a4cartao1=?, b4cartao1=?, " + "c4cartao1=?, d4cartao1=?, e4cartao1=?, f4cartao1=?, g4cartao1=?, h4cartao1=?, i4cartao1=?, j4cartao1=?, " + "topo4cartao1=?, a5cartao1=?, b5cartao1=?, c5cartao1=?, d5cartao1=?, e5cartao1=?, f5cartao1=?, g5cartao1=?, "
					+ "h5cartao1=?, i5cartao1=?, j5cartao1=?, topo5cartao1=?, a6cartao1=?, b6cartao1=?, c6cartao1=?, d6cartao1=?, " + "e6cartao1=?, f6cartao1=?, g6cartao1=?, h6cartao1=?, i6cartao1=?, j6cartao1=?, topo6cartao1=?, a7cartao1=?, " + "b7cartao1=?, c7cartao1=?, d7cartao1=?, e7cartao1=?, f7cartao1=?, g7cartao1=?, h7cartao1=?, i7cartao1=?, " + "j7cartao1=?, topo7cartao1=?, a8cartao1=?, b8cartao1=?, c8cartao1=?, d8cartao1=?, e8cartao1=?, f8cartao1=?, " + "g8cartao1=?, h8cartao1=?, i8cartao1=?, j8cartao1=?, topo8cartao1=?, a9cartao1=?, b9cartao1=?, c9cartao1=?, " + "d9cartao1=?, e9cartao1=?, f9cartao1=?, g9cartao1=?, h9cartao1=?, i9cartao1=?, j9cartao1=?, topo9cartao1=?, " + "inscricaotopocartao2=?, inscricaolinha1cartao2=?, salatopocartao2=?, salalinha2cartao2=?, cursotopocartao2=?, " + "cursolinha3cartao2=?, a1cartao2=?, b1cartao2=?, c1cartao2=?, d1cartao2=?, e1cartao2=?, f1cartao2=?, g1cartao2=?, "
					+ "h1cartao2=?, i1cartao2=?, j1cartao2=?, topo1cartao2=?, a2cartao2=?, b2cartao2=?, c2cartao2=?, d2cartao2=?, " + "e2cartao2=?, f2cartao2=?, g2cartao2=?, h2cartao2=?, i2cartao2=?, j2cartao2=?, topo2cartao2=?, a3cartao2=?, " + "b3cartao2=?, c3cartao2=?, d3cartao2=?, e3cartao2=?, f3cartao2=?, g3cartao2=?, h3cartao2=?, i3cartao2=?, " + "j3cartao2=?, topo3cartao2=?, a4cartao2=?, b4cartao2=?, c4cartao2=?, d4cartao2=?, e4cartao2=?, f4cartao2=?, " + "g4cartao2=?, h4cartao2=?, i4cartao2=?, j4cartao2=?, topo4cartao2=?, a5cartao2=?, b5cartao2=?, c5cartao2=?, " + "d5cartao2=?, e5cartao2=?, f5cartao2=?, g5cartao2=?, h5cartao2=?, i5cartao2=?, j5cartao2=?, topo5cartao2=?, " + "a6cartao2=?, b6cartao2=?, c6cartao2=?, d6cartao2=?, e6cartao2=?, f6cartao2=?, g6cartao2=?, h6cartao2=?, " + "i6cartao2=?, j6cartao2=?, topo6cartao2=?, a7cartao2=?, b7cartao2=?, c7cartao2=?, d7cartao2=?, e7cartao2=?, "
					+ "f7cartao2=?, g7cartao2=?, h7cartao2=?, i7cartao2=?, j7cartao2=?, topo7cartao2=?, a8cartao2=?, b8cartao2=?, " + "c8cartao2=?, d8cartao2=?, e8cartao2=?, f8cartao2=?, g8cartao2=?, h8cartao2=?, i8cartao2=?, j8cartao2=?, " + "topo8cartao2=?, a9cartao2=?, b9cartao2=?, c9cartao2=?, d9cartao2=?, e9cartao2=?, f9cartao2=?, g9cartao2=?, " + "h9cartao2=?, i9cartao2=?, j9cartao2=?, topo9cartao2=? WHERE ((codigo=?));";

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlAlterar = conn.prepareStatement(sql);

					if ((!obj.getInscricaotopocartao1().equals(""))) {
						sqlAlterar.setDouble(1, Double.parseDouble(obj.getInscricaotopocartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(1, 40.5);
					}
					if ((!obj.getInscricaolinha1cartao1().equals(""))) {
						sqlAlterar.setDouble(2, Double.parseDouble(obj.getInscricaolinha1cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(2, 28.0);
					}
					if ((!obj.getSalatopocartao1().equals(""))) {
						sqlAlterar.setDouble(3, Double.parseDouble(obj.getSalatopocartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(3, 43.5);
					}
					if ((!obj.getSalalinha2cartao1().equals(""))) {
						sqlAlterar.setDouble(4, Double.parseDouble(obj.getSalalinha2cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(4, 28.0);
					}
					if ((!obj.getCursotopocartao1().equals(""))) {
						sqlAlterar.setDouble(5, Double.parseDouble(obj.getCursotopocartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(5, 50.5);
					}
					if ((!obj.getCursolinha3cartao1().equals(""))) {
						sqlAlterar.setDouble(6, Double.parseDouble(obj.getCursolinha3cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(6, 28.0);
					}

					if ((!obj.getA1Cartao1().equals(""))) {
						sqlAlterar.setDouble(7, Double.parseDouble(obj.getA1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(7, 27.0);
					}

					if ((!obj.getB1Cartao1().equals(""))) {
						sqlAlterar.setDouble(8, Double.parseDouble(obj.getB1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(8, 33.6);
					}
					if ((!obj.getC1Cartao1().equals(""))) {
						sqlAlterar.setDouble(9, Double.parseDouble(obj.getC1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(9, 40.0);
					}
					if ((!obj.getD1Cartao1().equals(""))) {
						sqlAlterar.setDouble(10, Double.parseDouble(obj.getD1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(10, 46.5);
					}
					if ((!obj.getE1Cartao1().equals(""))) {
						sqlAlterar.setDouble(11, Double.parseDouble(obj.getE1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(11, 53.0);
					}
					if ((!obj.getF1Cartao1().equals(""))) {
						sqlAlterar.setDouble(12, Double.parseDouble(obj.getF1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(12, 59.2);
					}
					if ((!obj.getG1Cartao1().equals(""))) {
						sqlAlterar.setDouble(13, Double.parseDouble(obj.getG1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(13, 65.1);
					}
					if ((!obj.getH1Cartao1().equals(""))) {
						sqlAlterar.setDouble(14, Double.parseDouble(obj.getH1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(14, 71.2);
					}
					if ((!obj.getI1Cartao1().equals(""))) {
						sqlAlterar.setDouble(15, Double.parseDouble(obj.getI1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(15, 77.7);
					}
					if ((!obj.getJ1Cartao1().equals(""))) {
						sqlAlterar.setDouble(16, Double.parseDouble(obj.getJ1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(16, 84.3);
					}
					if ((!obj.getTopo1Cartao1().equals(""))) {
						sqlAlterar.setDouble(17, Double.parseDouble(obj.getTopo1Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(17, 57.3);
					}

					if ((!obj.getA2Cartao1().equals(""))) {
						sqlAlterar.setDouble(18, Double.parseDouble(obj.getA2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(18, 27.0);
					}
					if ((!obj.getB2Cartao1().equals(""))) {
						sqlAlterar.setDouble(19, Double.parseDouble(obj.getB2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(19, 33.6);
					}
					if ((!obj.getC2Cartao1().equals(""))) {
						sqlAlterar.setDouble(20, Double.parseDouble(obj.getC2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(20, 40.0);
					}
					if ((!obj.getD2Cartao1().equals(""))) {
						sqlAlterar.setDouble(21, Double.parseDouble(obj.getD2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(21, 46.5);
					}
					if ((!obj.getE2Cartao1().equals(""))) {
						sqlAlterar.setDouble(22, Double.parseDouble(obj.getE2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(22, 53.0);
					}
					if ((!obj.getF2Cartao1().equals(""))) {
						sqlAlterar.setDouble(23, Double.parseDouble(obj.getF2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(23, 59.2);
					}
					if ((!obj.getG2Cartao1().equals(""))) {
						sqlAlterar.setDouble(24, Double.parseDouble(obj.getG2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(24, 65.1);
					}
					if ((!obj.getH2Cartao1().equals(""))) {
						sqlAlterar.setDouble(25, Double.parseDouble(obj.getH2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(25, 71.2);
					}
					if ((!obj.getI2Cartao1().equals(""))) {
						sqlAlterar.setDouble(26, Double.parseDouble(obj.getI2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(26, 77.7);
					}
					if ((!obj.getJ2Cartao1().equals(""))) {
						sqlAlterar.setDouble(27, Double.parseDouble(obj.getJ2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(27, 84.3);
					}
					if ((!obj.getTopo2Cartao1().equals(""))) {
						sqlAlterar.setDouble(28, Double.parseDouble(obj.getTopo2Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(28, 63.6);
					}

					if ((!obj.getA3Cartao1().equals(""))) {
						sqlAlterar.setDouble(29, Double.parseDouble(obj.getA3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(29, 27.0);
					}
					if ((!obj.getB3Cartao1().equals(""))) {
						sqlAlterar.setDouble(30, Double.parseDouble(obj.getB3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(30, 33.6);
					}
					if ((!obj.getC3Cartao1().equals(""))) {
						sqlAlterar.setDouble(31, Double.parseDouble(obj.getC3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(31, 40.0);
					}
					if ((!obj.getD3Cartao1().equals(""))) {
						sqlAlterar.setDouble(32, Double.parseDouble(obj.getD3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(32, 46.5);
					}
					if ((!obj.getE3Cartao1().equals(""))) {
						sqlAlterar.setDouble(33, Double.parseDouble(obj.getE3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(33, 53.0);
					}
					if ((!obj.getF3Cartao1().equals(""))) {
						sqlAlterar.setDouble(34, Double.parseDouble(obj.getF3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(34, 59.2);
					}
					if ((!obj.getG3Cartao1().equals(""))) {
						sqlAlterar.setDouble(35, Double.parseDouble(obj.getG3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(35, 65.1);
					}
					if ((!obj.getH3Cartao1().equals(""))) {
						sqlAlterar.setDouble(36, Double.parseDouble(obj.getH3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(36, 71.2);
					}
					if ((!obj.getI3Cartao1().equals(""))) {
						sqlAlterar.setDouble(37, Double.parseDouble(obj.getI3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(37, 77.7);
					}
					if ((!obj.getJ3Cartao1().equals(""))) {
						sqlAlterar.setDouble(38, Double.parseDouble(obj.getJ3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(38, 84.3);
					}

					if ((!obj.getTopo3Cartao1().equals(""))) {
						sqlAlterar.setDouble(39, Double.parseDouble(obj.getTopo3Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(39, 69.9);
					}

					if ((!obj.getA4Cartao1().equals(""))) {
						sqlAlterar.setDouble(40, Double.parseDouble(obj.getA4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(40, 27.0);
					}
					if ((!obj.getB4Cartao1().equals(""))) {
						sqlAlterar.setDouble(41, Double.parseDouble(obj.getB4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(41, 33.6);
					}
					if ((!obj.getC4Cartao1().equals(""))) {
						sqlAlterar.setDouble(42, Double.parseDouble(obj.getC4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(42, 40.0);
					}
					if ((!obj.getD4Cartao1().equals(""))) {
						sqlAlterar.setDouble(43, Double.parseDouble(obj.getD4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(43, 46.5);
					}
					if ((!obj.getE4Cartao1().equals(""))) {
						sqlAlterar.setDouble(44, Double.parseDouble(obj.getE4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(44, 53.0);
					}
					if ((!obj.getF4Cartao1().equals(""))) {
						sqlAlterar.setDouble(45, Double.parseDouble(obj.getF4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(45, 59.2);
					}
					if ((!obj.getG4Cartao1().equals(""))) {
						sqlAlterar.setDouble(46, Double.parseDouble(obj.getG4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(46, 65.1);
					}
					if ((!obj.getH4Cartao1().equals(""))) {
						sqlAlterar.setDouble(47, Double.parseDouble(obj.getH4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(47, 71.2);
					}
					if ((!obj.getI4Cartao1().equals(""))) {
						sqlAlterar.setDouble(48, Double.parseDouble(obj.getI4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(48, 77.7);
					}
					if ((!obj.getJ4Cartao1().equals(""))) {
						sqlAlterar.setDouble(49, Double.parseDouble(obj.getJ4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(49, 84.3);
					}

					if ((!obj.getTopo4Cartao1().equals(""))) {
						sqlAlterar.setDouble(50, Double.parseDouble(obj.getTopo4Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(50, 76.2);
					}

					if ((!obj.getA5Cartao1().equals(""))) {
						sqlAlterar.setDouble(51, Double.parseDouble(obj.getA5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(51, 27.0);
					}
					if ((!obj.getB5Cartao1().equals(""))) {
						sqlAlterar.setDouble(52, Double.parseDouble(obj.getB5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(52, 33.6);
					}
					if ((!obj.getC5Cartao1().equals(""))) {
						sqlAlterar.setDouble(53, Double.parseDouble(obj.getC5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(53, 40.0);
					}
					if ((!obj.getD5Cartao1().equals(""))) {
						sqlAlterar.setDouble(54, Double.parseDouble(obj.getD5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(54, 46.5);
					}
					if ((!obj.getE5Cartao1().equals(""))) {
						sqlAlterar.setDouble(55, Double.parseDouble(obj.getE5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(55, 53.0);
					}
					if ((!obj.getF5Cartao1().equals(""))) {
						sqlAlterar.setDouble(56, Double.parseDouble(obj.getF5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(56, 59.2);
					}
					if ((!obj.getG5Cartao1().equals(""))) {
						sqlAlterar.setDouble(57, Double.parseDouble(obj.getG5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(57, 65.1);
					}
					if ((!obj.getH5Cartao1().equals(""))) {
						sqlAlterar.setDouble(58, Double.parseDouble(obj.getH5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(58, 71.2);
					}
					if ((!obj.getI5Cartao1().equals(""))) {
						sqlAlterar.setDouble(59, Double.parseDouble(obj.getI5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(59, 77.7);
					}
					if ((!obj.getJ5Cartao1().equals(""))) {
						sqlAlterar.setDouble(60, Double.parseDouble(obj.getJ5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(60, 84.3);
					}

					if ((!obj.getTopo5Cartao1().equals(""))) {
						sqlAlterar.setDouble(61, Double.parseDouble(obj.getTopo5Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(61, 82.5);
					}

					if ((!obj.getA6Cartao1().equals(""))) {
						sqlAlterar.setDouble(62, Double.parseDouble(obj.getA6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(62, 27.0);
					}
					if ((!obj.getB6Cartao1().equals(""))) {
						sqlAlterar.setDouble(63, Double.parseDouble(obj.getB6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(63, 33.6);
					}
					if ((!obj.getC6Cartao1().equals(""))) {
						sqlAlterar.setDouble(64, Double.parseDouble(obj.getC6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(64, 40.0);
					}
					if ((!obj.getD6Cartao1().equals(""))) {
						sqlAlterar.setDouble(65, Double.parseDouble(obj.getD6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(65, 46.5);
					}
					if ((!obj.getE6Cartao1().equals(""))) {
						sqlAlterar.setDouble(66, Double.parseDouble(obj.getE6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(66, 53.0);
					}
					if ((!obj.getF6Cartao1().equals(""))) {
						sqlAlterar.setDouble(67, Double.parseDouble(obj.getF6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(67, 59.2);
					}
					if ((!obj.getG6Cartao1().equals(""))) {
						sqlAlterar.setDouble(68, Double.parseDouble(obj.getG6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(68, 65.1);
					}
					if ((!obj.getH6Cartao1().equals(""))) {
						sqlAlterar.setDouble(69, Double.parseDouble(obj.getH6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(69, 71.2);
					}
					if ((!obj.getI6Cartao1().equals(""))) {
						sqlAlterar.setDouble(70, Double.parseDouble(obj.getI6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(70, 77.7);
					}
					if ((!obj.getJ6Cartao1().equals(""))) {
						sqlAlterar.setDouble(71, Double.parseDouble(obj.getJ6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(71, 84.3);
					}

					if ((!obj.getTopo6Cartao1().equals(""))) {
						sqlAlterar.setDouble(72, Double.parseDouble(obj.getTopo6Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(72, 88.8);
					}

					if ((!obj.getA7Cartao1().equals(""))) {
						sqlAlterar.setDouble(73, Double.parseDouble(obj.getA7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(73, 27.0);
					}
					if ((!obj.getB7Cartao1().equals(""))) {
						sqlAlterar.setDouble(74, Double.parseDouble(obj.getB7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(74, 33.6);
					}
					if ((!obj.getC7Cartao1().equals(""))) {
						sqlAlterar.setDouble(75, Double.parseDouble(obj.getC7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(75, 40.0);
					}
					if ((!obj.getD7Cartao1().equals(""))) {
						sqlAlterar.setDouble(76, Double.parseDouble(obj.getD7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(76, 46.5);
					}
					if ((!obj.getE7Cartao1().equals(""))) {
						sqlAlterar.setDouble(77, Double.parseDouble(obj.getE7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(77, 53.0);
					}
					if ((!obj.getF7Cartao1().equals(""))) {
						sqlAlterar.setDouble(78, Double.parseDouble(obj.getF7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(78, 59.2);
					}
					if ((!obj.getG7Cartao1().equals(""))) {
						sqlAlterar.setDouble(79, Double.parseDouble(obj.getG7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(79, 65.1);
					}
					if ((!obj.getH7Cartao1().equals(""))) {
						sqlAlterar.setDouble(80, Double.parseDouble(obj.getH7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(80, 71.2);
					}
					if ((!obj.getI7Cartao1().equals(""))) {
						sqlAlterar.setDouble(81, Double.parseDouble(obj.getI7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(81, 77.7);
					}
					if ((!obj.getJ7Cartao1().equals(""))) {
						sqlAlterar.setDouble(82, Double.parseDouble(obj.getJ7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(82, 84.3);
					}

					if ((!obj.getTopo7Cartao1().equals(""))) {
						sqlAlterar.setDouble(83, Double.parseDouble(obj.getTopo7Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(83, 95.1);
					}

					if ((!obj.getA8Cartao1().equals(""))) {
						sqlAlterar.setDouble(84, Double.parseDouble(obj.getA8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(84, 27.0);
					}
					if ((!obj.getB8Cartao1().equals(""))) {
						sqlAlterar.setDouble(85, Double.parseDouble(obj.getB8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(85, 33.6);
					}
					if ((!obj.getC8Cartao1().equals(""))) {
						sqlAlterar.setDouble(86, Double.parseDouble(obj.getC8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(86, 40.0);
					}
					if ((!obj.getD8Cartao1().equals(""))) {
						sqlAlterar.setDouble(87, Double.parseDouble(obj.getD8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(87, 46.5);
					}
					if ((!obj.getE8Cartao1().equals(""))) {
						sqlAlterar.setDouble(88, Double.parseDouble(obj.getE8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(88, 53.0);
					}
					if ((!obj.getF8Cartao1().equals(""))) {
						sqlAlterar.setDouble(89, Double.parseDouble(obj.getF8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(89, 59.2);
					}
					if ((!obj.getG8Cartao1().equals(""))) {
						sqlAlterar.setDouble(90, Double.parseDouble(obj.getG8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(90, 65.1);
					}
					if ((!obj.getH8Cartao1().equals(""))) {
						sqlAlterar.setDouble(91, Double.parseDouble(obj.getH8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(91, 71.2);
					}
					if ((!obj.getI8Cartao1().equals(""))) {
						sqlAlterar.setDouble(92, Double.parseDouble(obj.getI8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(92, 77.7);
					}
					if ((!obj.getJ8Cartao1().equals(""))) {
						sqlAlterar.setDouble(93, Double.parseDouble(obj.getJ8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(93, 84.3);
					}

					if ((!obj.getTopo8Cartao1().equals(""))) {
						sqlAlterar.setDouble(94, Double.parseDouble(obj.getTopo8Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(94, 101.4);
					}

					if ((!obj.getA9Cartao1().equals(""))) {
						sqlAlterar.setDouble(95, Double.parseDouble(obj.getA9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(95, 27.0);
					}
					if ((!obj.getB9Cartao1().equals(""))) {
						sqlAlterar.setDouble(96, Double.parseDouble(obj.getB9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(96, 33.6);
					}
					if ((!obj.getC9Cartao1().equals(""))) {
						sqlAlterar.setDouble(97, Double.parseDouble(obj.getC9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(97, 40.0);
					}
					if ((!obj.getD9Cartao1().equals(""))) {
						sqlAlterar.setDouble(98, Double.parseDouble(obj.getD9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(98, 46.5);
					}
					if ((!obj.getE9Cartao1().equals(""))) {
						sqlAlterar.setDouble(99, Double.parseDouble(obj.getE9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(99, 53.0);
					}
					if ((!obj.getF9Cartao1().equals(""))) {
						sqlAlterar.setDouble(100, Double.parseDouble(obj.getF9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(100, 59.2);
					}
					if ((!obj.getG9Cartao1().equals(""))) {
						sqlAlterar.setDouble(101, Double.parseDouble(obj.getG9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(101, 65.1);
					}
					if ((!obj.getH9Cartao1().equals(""))) {
						sqlAlterar.setDouble(102, Double.parseDouble(obj.getH9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(102, 71.2);
					}
					if ((!obj.getI9Cartao1().equals(""))) {
						sqlAlterar.setDouble(103, Double.parseDouble(obj.getI9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(103, 77.7);
					}
					if ((!obj.getJ9Cartao1().equals(""))) {
						sqlAlterar.setDouble(104, Double.parseDouble(obj.getJ9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(104, 84.3);
					}

					if ((!obj.getTopo9Cartao1().equals(""))) {
						sqlAlterar.setDouble(105, Double.parseDouble(obj.getTopo9Cartao1().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(105, 107.7);
					}

					if ((!obj.getInscricaotopocartao2().equals(""))) {
						sqlAlterar.setDouble(106, Double.parseDouble(obj.getInscricaotopocartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(106, 40.5);
					}
					if ((!obj.getInscricaolinha1cartao2().equals(""))) {
						sqlAlterar.setDouble(107, Double.parseDouble(obj.getInscricaolinha1cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(107, 110.0);
					}
					if ((!obj.getSalatopocartao2().equals(""))) {
						sqlAlterar.setDouble(108, Double.parseDouble(obj.getSalatopocartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(108, 43.5);
					}
					if ((!obj.getSalalinha2cartao2().equals(""))) {
						sqlAlterar.setDouble(109, Double.parseDouble(obj.getSalalinha2cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(109, 110.0);
					}
					if ((!obj.getCursotopocartao2().equals(""))) {
						sqlAlterar.setDouble(110, Double.parseDouble(obj.getCursotopocartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(110, 50.5);
					}
					if ((!obj.getCursolinha3cartao2().equals(""))) {
						sqlAlterar.setDouble(111, Double.parseDouble(obj.getCursolinha3cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(111, 110.0);
					}

					if ((!obj.getA1Cartao2().equals(""))) {
						sqlAlterar.setDouble(112, Double.parseDouble(obj.getA1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(112, 109.6);
					}

					if ((!obj.getB1Cartao2().equals(""))) {
						sqlAlterar.setDouble(113, Double.parseDouble(obj.getB1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(113, 116.3);
					}
					if ((!obj.getC1Cartao2().equals(""))) {
						sqlAlterar.setDouble(114, Double.parseDouble(obj.getC1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(114, 123.1);
					}
					if ((!obj.getD1Cartao2().equals(""))) {
						sqlAlterar.setDouble(115, Double.parseDouble(obj.getD1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(115, 129.2);
					}
					if ((!obj.getE1Cartao2().equals(""))) {
						sqlAlterar.setDouble(116, Double.parseDouble(obj.getE1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(116, 135.6);
					}
					if ((!obj.getF1Cartao2().equals(""))) {
						sqlAlterar.setDouble(117, Double.parseDouble(obj.getF1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(117, 141.9);
					}
					if ((!obj.getG1Cartao2().equals(""))) {
						sqlAlterar.setDouble(118, Double.parseDouble(obj.getG1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(118, 148.1);
					}
					if ((!obj.getH1Cartao2().equals(""))) {
						sqlAlterar.setDouble(119, Double.parseDouble(obj.getH1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(119, 157.9);
					}
					if ((!obj.getI1Cartao2().equals(""))) {
						sqlAlterar.setDouble(120, Double.parseDouble(obj.getI1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(120, 160.5);
					}
					if ((!obj.getJ1Cartao2().equals(""))) {
						sqlAlterar.setDouble(121, Double.parseDouble(obj.getJ1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(121, 166.8);
					}

					if ((!obj.getTopo1Cartao2().equals(""))) {
						sqlAlterar.setDouble(122, Double.parseDouble(obj.getTopo1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(122, 57.3);
					}

					if ((!obj.getA2Cartao2().equals(""))) {
						sqlAlterar.setDouble(123, Double.parseDouble(obj.getA2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(123, 109.6);
					}

					if ((!obj.getB2Cartao2().equals(""))) {
						sqlAlterar.setDouble(124, Double.parseDouble(obj.getB2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(124, 116.3);
					}
					if ((!obj.getC2Cartao2().equals(""))) {
						sqlAlterar.setDouble(125, Double.parseDouble(obj.getC2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(125, 123.1);
					}
					if ((!obj.getD2Cartao2().equals(""))) {
						sqlAlterar.setDouble(126, Double.parseDouble(obj.getD2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(126, 129.2);
					}
					if ((!obj.getE2Cartao2().equals(""))) {
						sqlAlterar.setDouble(127, Double.parseDouble(obj.getE2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(127, 135.6);
					}
					if ((!obj.getF2Cartao2().equals(""))) {
						sqlAlterar.setDouble(128, Double.parseDouble(obj.getF2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(128, 141.9);
					}
					if ((!obj.getG2Cartao2().equals(""))) {
						sqlAlterar.setDouble(129, Double.parseDouble(obj.getG2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(129, 148.1);
					}
					if ((!obj.getH2Cartao2().equals(""))) {
						sqlAlterar.setDouble(130, Double.parseDouble(obj.getH2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(130, 157.9);
					}
					if ((!obj.getI2Cartao2().equals(""))) {
						sqlAlterar.setDouble(131, Double.parseDouble(obj.getI2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(131, 160.5);
					}
					if ((!obj.getJ2Cartao2().equals(""))) {
						sqlAlterar.setDouble(132, Double.parseDouble(obj.getJ2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(132, 166.8);
					}

					if ((!obj.getTopo2Cartao2().equals(""))) {
						sqlAlterar.setDouble(133, Double.parseDouble(obj.getTopo2Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(133, 63.6);
					}

					if ((!obj.getA3Cartao2().equals(""))) {
						sqlAlterar.setDouble(134, Double.parseDouble(obj.getA3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(134, 109.6);
					}

					if ((!obj.getB3Cartao2().equals(""))) {
						sqlAlterar.setDouble(135, Double.parseDouble(obj.getB3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(135, 116.3);
					}
					if ((!obj.getC3Cartao2().equals(""))) {
						sqlAlterar.setDouble(136, Double.parseDouble(obj.getC3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(136, 123.1);
					}
					if ((!obj.getD3Cartao2().equals(""))) {
						sqlAlterar.setDouble(137, Double.parseDouble(obj.getD3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(137, 129.2);
					}
					if ((!obj.getE3Cartao2().equals(""))) {
						sqlAlterar.setDouble(138, Double.parseDouble(obj.getE3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(138, 135.6);
					}
					if ((!obj.getF3Cartao2().equals(""))) {
						sqlAlterar.setDouble(139, Double.parseDouble(obj.getF3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(139, 141.9);
					}
					if ((!obj.getG3Cartao2().equals(""))) {
						sqlAlterar.setDouble(140, Double.parseDouble(obj.getG3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(140, 148.1);
					}
					if ((!obj.getH3Cartao2().equals(""))) {
						sqlAlterar.setDouble(141, Double.parseDouble(obj.getH3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(141, 157.9);
					}
					if ((!obj.getI3Cartao2().equals(""))) {
						sqlAlterar.setDouble(142, Double.parseDouble(obj.getI3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(142, 160.5);
					}
					if ((!obj.getJ3Cartao2().equals(""))) {
						sqlAlterar.setDouble(143, Double.parseDouble(obj.getJ3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(143, 166.8);
					}

					if ((!obj.getTopo3Cartao2().equals(""))) {
						sqlAlterar.setDouble(144, Double.parseDouble(obj.getTopo3Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(144, 69.9);
					}

					if ((!obj.getA4Cartao2().equals(""))) {
						sqlAlterar.setDouble(145, Double.parseDouble(obj.getA4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(145, 109.6);
					}

					if ((!obj.getB4Cartao2().equals(""))) {
						sqlAlterar.setDouble(146, Double.parseDouble(obj.getB4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(146, 116.3);
					}
					if ((!obj.getC4Cartao2().equals(""))) {
						sqlAlterar.setDouble(147, Double.parseDouble(obj.getC4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(147, 123.1);
					}
					if ((!obj.getD4Cartao2().equals(""))) {
						sqlAlterar.setDouble(148, Double.parseDouble(obj.getD4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(148, 129.2);
					}
					if ((!obj.getE4Cartao2().equals(""))) {
						sqlAlterar.setDouble(149, Double.parseDouble(obj.getE4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(149, 135.6);
					}
					if ((!obj.getF4Cartao2().equals(""))) {
						sqlAlterar.setDouble(150, Double.parseDouble(obj.getF4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(150, 141.9);
					}
					if ((!obj.getG4Cartao2().equals(""))) {
						sqlAlterar.setDouble(151, Double.parseDouble(obj.getG4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(151, 148.1);
					}
					if ((!obj.getH4Cartao2().equals(""))) {
						sqlAlterar.setDouble(152, Double.parseDouble(obj.getH4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(152, 157.9);
					}
					if ((!obj.getI4Cartao2().equals(""))) {
						sqlAlterar.setDouble(153, Double.parseDouble(obj.getI4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(153, 160.5);
					}
					if ((!obj.getJ4Cartao2().equals(""))) {
						sqlAlterar.setDouble(154, Double.parseDouble(obj.getJ4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(154, 166.8);
					}

					if ((!obj.getTopo4Cartao2().equals(""))) {
						sqlAlterar.setDouble(155, Double.parseDouble(obj.getTopo4Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(155, 76.2);
					}

					if ((!obj.getA5Cartao2().equals(""))) {
						sqlAlterar.setDouble(156, Double.parseDouble(obj.getA5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(156, 109.6);
					}

					if ((!obj.getB5Cartao2().equals(""))) {
						sqlAlterar.setDouble(157, Double.parseDouble(obj.getB5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(157, 116.3);
					}
					if ((!obj.getC5Cartao2().equals(""))) {
						sqlAlterar.setDouble(158, Double.parseDouble(obj.getC5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(158, 123.1);
					}
					if ((!obj.getD5Cartao2().equals(""))) {
						sqlAlterar.setDouble(159, Double.parseDouble(obj.getD5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(159, 129.2);
					}
					if ((!obj.getE5Cartao2().equals(""))) {
						sqlAlterar.setDouble(160, Double.parseDouble(obj.getE5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(160, 135.6);
					}
					if ((!obj.getF5Cartao2().equals(""))) {
						sqlAlterar.setDouble(161, Double.parseDouble(obj.getF5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(161, 141.9);
					}
					if ((!obj.getG5Cartao2().equals(""))) {
						sqlAlterar.setDouble(162, Double.parseDouble(obj.getG5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(162, 148.1);
					}
					if ((!obj.getH5Cartao2().equals(""))) {
						sqlAlterar.setDouble(163, Double.parseDouble(obj.getH5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(163, 157.9);
					}
					if ((!obj.getI5Cartao2().equals(""))) {
						sqlAlterar.setDouble(164, Double.parseDouble(obj.getI5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(164, 160.5);
					}
					if ((!obj.getJ1Cartao2().equals(""))) {
						sqlAlterar.setDouble(165, Double.parseDouble(obj.getJ1Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(165, 166.8);
					}

					if ((!obj.getTopo5Cartao2().equals(""))) {
						sqlAlterar.setDouble(166, Double.parseDouble(obj.getTopo5Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(166, 82.5);
					}

					if ((!obj.getA6Cartao2().equals(""))) {
						sqlAlterar.setDouble(167, Double.parseDouble(obj.getA6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(167, 109.6);
					}

					if ((!obj.getB6Cartao2().equals(""))) {
						sqlAlterar.setDouble(168, Double.parseDouble(obj.getB6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(168, 116.3);
					}
					if ((!obj.getC6Cartao2().equals(""))) {
						sqlAlterar.setDouble(169, Double.parseDouble(obj.getC6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(169, 123.1);
					}
					if ((!obj.getD6Cartao2().equals(""))) {
						sqlAlterar.setDouble(170, Double.parseDouble(obj.getD6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(170, 129.2);
					}
					if ((!obj.getE6Cartao2().equals(""))) {
						sqlAlterar.setDouble(171, Double.parseDouble(obj.getE6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(171, 135.6);
					}
					if ((!obj.getF6Cartao2().equals(""))) {
						sqlAlterar.setDouble(172, Double.parseDouble(obj.getF6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(172, 141.9);
					}
					if ((!obj.getG6Cartao2().equals(""))) {
						sqlAlterar.setDouble(173, Double.parseDouble(obj.getG6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(173, 148.1);
					}
					if ((!obj.getH6Cartao2().equals(""))) {
						sqlAlterar.setDouble(174, Double.parseDouble(obj.getH6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(174, 157.9);
					}
					if ((!obj.getI6Cartao2().equals(""))) {
						sqlAlterar.setDouble(175, Double.parseDouble(obj.getI6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(175, 160.5);
					}
					if ((!obj.getJ6Cartao2().equals(""))) {
						sqlAlterar.setDouble(176, Double.parseDouble(obj.getJ6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(176, 166.8);
					}

					if ((!obj.getTopo6Cartao2().equals(""))) {
						sqlAlterar.setDouble(177, Double.parseDouble(obj.getTopo6Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(177, 88.8);
					}

					if ((!obj.getA7Cartao2().equals(""))) {
						sqlAlterar.setDouble(178, Double.parseDouble(obj.getA7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(178, 109.6);
					}

					if ((!obj.getB7Cartao2().equals(""))) {
						sqlAlterar.setDouble(179, Double.parseDouble(obj.getB7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(179, 116.3);
					}
					if ((!obj.getC7Cartao2().equals(""))) {
						sqlAlterar.setDouble(180, Double.parseDouble(obj.getC7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(180, 123.1);
					}
					if ((!obj.getD7Cartao2().equals(""))) {
						sqlAlterar.setDouble(181, Double.parseDouble(obj.getD7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(181, 129.2);
					}
					if ((!obj.getE7Cartao2().equals(""))) {
						sqlAlterar.setDouble(182, Double.parseDouble(obj.getE7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(182, 135.6);
					}
					if ((!obj.getF7Cartao2().equals(""))) {
						sqlAlterar.setDouble(183, Double.parseDouble(obj.getF7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(183, 141.9);
					}
					if ((!obj.getG7Cartao2().equals(""))) {
						sqlAlterar.setDouble(184, Double.parseDouble(obj.getG7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(184, 148.1);
					}
					if ((!obj.getH7Cartao2().equals(""))) {
						sqlAlterar.setDouble(185, Double.parseDouble(obj.getH7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(185, 157.9);
					}
					if ((!obj.getI7Cartao2().equals(""))) {
						sqlAlterar.setDouble(186, Double.parseDouble(obj.getI7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(186, 160.5);
					}
					if ((!obj.getJ7Cartao2().equals(""))) {
						sqlAlterar.setDouble(187, Double.parseDouble(obj.getJ7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(187, 166.8);
					}

					if ((!obj.getTopo7Cartao2().equals(""))) {
						sqlAlterar.setDouble(188, Double.parseDouble(obj.getTopo7Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(188, 95.1);
					}

					if ((!obj.getA8Cartao2().equals(""))) {
						sqlAlterar.setDouble(189, Double.parseDouble(obj.getA8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(189, 109.6);
					}

					if ((!obj.getB8Cartao2().equals(""))) {
						sqlAlterar.setDouble(190, Double.parseDouble(obj.getB8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(190, 116.3);
					}
					if ((!obj.getC8Cartao2().equals(""))) {
						sqlAlterar.setDouble(191, Double.parseDouble(obj.getC8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(191, 123.1);
					}
					if ((!obj.getD8Cartao2().equals(""))) {
						sqlAlterar.setDouble(192, Double.parseDouble(obj.getD8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(192, 129.2);
					}
					if ((!obj.getE8Cartao2().equals(""))) {
						sqlAlterar.setDouble(193, Double.parseDouble(obj.getE8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(193, 135.6);
					}
					if ((!obj.getF8Cartao2().equals(""))) {
						sqlAlterar.setDouble(194, Double.parseDouble(obj.getF8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(194, 141.9);
					}
					if ((!obj.getG8Cartao2().equals(""))) {
						sqlAlterar.setDouble(195, Double.parseDouble(obj.getG8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(195, 148.1);
					}
					if ((!obj.getH8Cartao2().equals(""))) {
						sqlAlterar.setDouble(196, Double.parseDouble(obj.getH8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(196, 157.9);
					}
					if ((!obj.getI8Cartao2().equals(""))) {
						sqlAlterar.setDouble(197, Double.parseDouble(obj.getI8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(197, 160.5);
					}
					if ((!obj.getJ8Cartao2().equals(""))) {
						sqlAlterar.setDouble(198, Double.parseDouble(obj.getJ8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(198, 166.8);
					}

					if ((!obj.getTopo8Cartao2().equals(""))) {
						sqlAlterar.setDouble(199, Double.parseDouble(obj.getTopo8Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(199, 101.4);
					}

					if ((!obj.getA9Cartao2().equals(""))) {
						sqlAlterar.setDouble(200, Double.parseDouble(obj.getA9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(200, 109.6);
					}

					if ((!obj.getB9Cartao2().equals(""))) {
						sqlAlterar.setDouble(201, Double.parseDouble(obj.getB9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(201, 116.3);
					}
					if ((!obj.getC9Cartao2().equals(""))) {
						sqlAlterar.setDouble(202, Double.parseDouble(obj.getC9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(202, 123.1);
					}
					if ((!obj.getD9Cartao2().equals(""))) {
						sqlAlterar.setDouble(203, Double.parseDouble(obj.getD9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(203, 129.2);
					}
					if ((!obj.getE9Cartao2().equals(""))) {
						sqlAlterar.setDouble(204, Double.parseDouble(obj.getE9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(204, 135.6);
					}
					if ((!obj.getF9Cartao2().equals(""))) {
						sqlAlterar.setDouble(205, Double.parseDouble(obj.getF9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(205, 141.9);
					}
					if ((!obj.getG9Cartao2().equals(""))) {
						sqlAlterar.setDouble(206, Double.parseDouble(obj.getG9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(206, 148.1);
					}
					if ((!obj.getH9Cartao2().equals(""))) {
						sqlAlterar.setDouble(207, Double.parseDouble(obj.getH9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(207, 157.9);
					}
					if ((!obj.getI9Cartao2().equals(""))) {
						sqlAlterar.setDouble(208, Double.parseDouble(obj.getI9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(208, 160.5);
					}
					if ((!obj.getJ9Cartao2().equals(""))) {
						sqlAlterar.setDouble(209, Double.parseDouble(obj.getJ9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(209, 166.8);
					}

					if ((!obj.getTopo9Cartao2().equals(""))) {
						sqlAlterar.setDouble(210, Double.parseDouble(obj.getTopo9Cartao2().replace(',', '.')));
					} else {
						sqlAlterar.setDouble(210, 107.7);
					}

					sqlAlterar.setInt(211, obj.getCodigo().intValue());

					return sqlAlterar;
				}
			});

		} catch (Exception e) {

			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public static CartaoRespostaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		CartaoRespostaVO obj = new CartaoRespostaVO();

		if (dadosSQL.next()) {
			obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
			obj.setInscricaotopocartao1(dadosSQL.getString("inscricaotopocartao1"));
			obj.setInscricaolinha1cartao1(dadosSQL.getString("inscricaolinha1cartao1"));
			obj.setSalatopocartao1(dadosSQL.getString("salatopocartao1"));
			obj.setSalalinha2cartao1(dadosSQL.getString("salalinha2cartao1"));
			obj.setCursotopocartao1(dadosSQL.getString("cursotopocartao1"));
			obj.setCursolinha3cartao1(dadosSQL.getString("cursolinha3cartao1"));

			obj.setA1Cartao1(dadosSQL.getString("a1cartao1"));
			obj.setB1Cartao1(dadosSQL.getString("b1cartao1"));
			obj.setC1Cartao1(dadosSQL.getString("c1cartao1"));
			obj.setD1Cartao1(dadosSQL.getString("d1cartao1"));
			obj.setE1Cartao1(dadosSQL.getString("e1cartao1"));
			obj.setF1Cartao1(dadosSQL.getString("f1cartao1"));
			obj.setG1Cartao1(dadosSQL.getString("g1cartao1"));
			obj.setH1Cartao1(dadosSQL.getString("h1cartao1"));
			obj.setI1Cartao1(dadosSQL.getString("i1cartao1"));
			obj.setJ1Cartao1(dadosSQL.getString("j1cartao1"));
			obj.setTopo1Cartao1(dadosSQL.getString("topo1cartao1"));

			obj.setA2Cartao1(dadosSQL.getString("a2cartao1"));
			obj.setB2Cartao1(dadosSQL.getString("b2cartao1"));
			obj.setC2Cartao1(dadosSQL.getString("c2cartao1"));
			obj.setD2Cartao1(dadosSQL.getString("d2cartao1"));
			obj.setE2Cartao1(dadosSQL.getString("e2cartao1"));
			obj.setF2Cartao1(dadosSQL.getString("f2cartao1"));
			obj.setG2Cartao1(dadosSQL.getString("g2cartao1"));
			obj.setH2Cartao1(dadosSQL.getString("h2cartao1"));
			obj.setI2Cartao1(dadosSQL.getString("i2cartao1"));
			obj.setJ2Cartao1(dadosSQL.getString("j2cartao1"));
			obj.setTopo2Cartao1(dadosSQL.getString("topo2cartao1"));

			obj.setA3Cartao1(dadosSQL.getString("a3cartao1"));
			obj.setB3Cartao1(dadosSQL.getString("b3cartao1"));
			obj.setC3Cartao1(dadosSQL.getString("c3cartao1"));
			obj.setD3Cartao1(dadosSQL.getString("d3cartao1"));
			obj.setE3Cartao1(dadosSQL.getString("e3cartao1"));
			obj.setF3Cartao1(dadosSQL.getString("f3cartao1"));
			obj.setG3Cartao1(dadosSQL.getString("g3cartao1"));
			obj.setH3Cartao1(dadosSQL.getString("h3cartao1"));
			obj.setI3Cartao1(dadosSQL.getString("i3cartao1"));
			obj.setJ3Cartao1(dadosSQL.getString("j3cartao1"));
			obj.setTopo3Cartao1(dadosSQL.getString("topo3cartao1"));

			obj.setA4Cartao1(dadosSQL.getString("a4cartao1"));
			obj.setB4Cartao1(dadosSQL.getString("b4cartao1"));
			obj.setC4Cartao1(dadosSQL.getString("c4cartao1"));
			obj.setD4Cartao1(dadosSQL.getString("d4cartao1"));
			obj.setE4Cartao1(dadosSQL.getString("e4cartao1"));
			obj.setF4Cartao1(dadosSQL.getString("f4cartao1"));
			obj.setG4Cartao1(dadosSQL.getString("g4cartao1"));
			obj.setH4Cartao1(dadosSQL.getString("h4cartao1"));
			obj.setI4Cartao1(dadosSQL.getString("i4cartao1"));
			obj.setJ4Cartao1(dadosSQL.getString("j4cartao1"));
			obj.setTopo4Cartao1(dadosSQL.getString("topo4cartao1"));

			obj.setA5Cartao1(dadosSQL.getString("a5cartao1"));
			obj.setB5Cartao1(dadosSQL.getString("b5cartao1"));
			obj.setC5Cartao1(dadosSQL.getString("c5cartao1"));
			obj.setD5Cartao1(dadosSQL.getString("d5cartao1"));
			obj.setE5Cartao1(dadosSQL.getString("e5cartao1"));
			obj.setF5Cartao1(dadosSQL.getString("f5cartao1"));
			obj.setG5Cartao1(dadosSQL.getString("g5cartao1"));
			obj.setH5Cartao1(dadosSQL.getString("h5cartao1"));
			obj.setI5Cartao1(dadosSQL.getString("i5cartao1"));
			obj.setJ5Cartao1(dadosSQL.getString("j5cartao1"));
			obj.setTopo5Cartao1(dadosSQL.getString("topo5cartao1"));

			obj.setA6Cartao1(dadosSQL.getString("a6cartao1"));
			obj.setB6Cartao1(dadosSQL.getString("b6cartao1"));
			obj.setC6Cartao1(dadosSQL.getString("c6cartao1"));
			obj.setD6Cartao1(dadosSQL.getString("d6cartao1"));
			obj.setE6Cartao1(dadosSQL.getString("e6cartao1"));
			obj.setF6Cartao1(dadosSQL.getString("f6cartao1"));
			obj.setG6Cartao1(dadosSQL.getString("g6cartao1"));
			obj.setH6Cartao1(dadosSQL.getString("h6cartao1"));
			obj.setI6Cartao1(dadosSQL.getString("i6cartao1"));
			obj.setJ6Cartao1(dadosSQL.getString("j6cartao1"));
			obj.setTopo6Cartao1(dadosSQL.getString("topo6cartao1"));

			obj.setA7Cartao1(dadosSQL.getString("a7cartao1"));
			obj.setB7Cartao1(dadosSQL.getString("b7cartao1"));
			obj.setC7Cartao1(dadosSQL.getString("c7cartao1"));
			obj.setD7Cartao1(dadosSQL.getString("d7cartao1"));
			obj.setE7Cartao1(dadosSQL.getString("e7cartao1"));
			obj.setF7Cartao1(dadosSQL.getString("f7cartao1"));
			obj.setG7Cartao1(dadosSQL.getString("g7cartao1"));
			obj.setH7Cartao1(dadosSQL.getString("h7cartao1"));
			obj.setI7Cartao1(dadosSQL.getString("i7cartao1"));
			obj.setJ7Cartao1(dadosSQL.getString("j7cartao1"));
			obj.setTopo7Cartao1(dadosSQL.getString("topo7cartao1"));

			obj.setA8Cartao1(dadosSQL.getString("a8cartao1"));
			obj.setB8Cartao1(dadosSQL.getString("b8cartao1"));
			obj.setC8Cartao1(dadosSQL.getString("c8cartao1"));
			obj.setD8Cartao1(dadosSQL.getString("d8cartao1"));
			obj.setE8Cartao1(dadosSQL.getString("e8cartao1"));
			obj.setF8Cartao1(dadosSQL.getString("f8cartao1"));
			obj.setG8Cartao1(dadosSQL.getString("g8cartao1"));
			obj.setH8Cartao1(dadosSQL.getString("h8cartao1"));
			obj.setI8Cartao1(dadosSQL.getString("i8cartao1"));
			obj.setJ8Cartao1(dadosSQL.getString("j8cartao1"));
			obj.setTopo8Cartao1(dadosSQL.getString("topo8cartao1"));

			obj.setA9Cartao1(dadosSQL.getString("a9cartao1"));
			obj.setB9Cartao1(dadosSQL.getString("b9cartao1"));
			obj.setC9Cartao1(dadosSQL.getString("c9cartao1"));
			obj.setD9Cartao1(dadosSQL.getString("d9cartao1"));
			obj.setE9Cartao1(dadosSQL.getString("e9cartao1"));
			obj.setF9Cartao1(dadosSQL.getString("f9cartao1"));
			obj.setG9Cartao1(dadosSQL.getString("g9cartao1"));
			obj.setH9Cartao1(dadosSQL.getString("h9cartao1"));
			obj.setI9Cartao1(dadosSQL.getString("i9cartao1"));
			obj.setJ9Cartao1(dadosSQL.getString("j9cartao1"));
			obj.setTopo9Cartao1(dadosSQL.getString("topo9cartao1"));

			obj.setInscricaotopocartao2(dadosSQL.getString("inscricaotopocartao2"));
			obj.setInscricaolinha1cartao2(dadosSQL.getString("inscricaolinha1cartao2"));
			obj.setSalatopocartao2(dadosSQL.getString("salatopocartao2"));
			obj.setSalalinha2cartao2(dadosSQL.getString("salalinha2cartao2"));
			obj.setCursotopocartao2(dadosSQL.getString("cursotopocartao2"));
			obj.setCursolinha3cartao2(dadosSQL.getString("cursolinha3cartao2"));

			obj.setA1Cartao2(dadosSQL.getString("a1cartao2"));
			obj.setB1Cartao2(dadosSQL.getString("b1cartao2"));
			obj.setC1Cartao2(dadosSQL.getString("c1cartao2"));
			obj.setD1Cartao2(dadosSQL.getString("d1cartao2"));
			obj.setE1Cartao2(dadosSQL.getString("e1cartao2"));
			obj.setF1Cartao2(dadosSQL.getString("f1cartao2"));
			obj.setG1Cartao2(dadosSQL.getString("g1cartao2"));
			obj.setH1Cartao2(dadosSQL.getString("h1cartao2"));
			obj.setI1Cartao2(dadosSQL.getString("i1cartao2"));
			obj.setJ1Cartao2(dadosSQL.getString("j1cartao2"));
			obj.setTopo1Cartao2(dadosSQL.getString("topo1cartao2"));

			obj.setA2Cartao2(dadosSQL.getString("a2cartao2"));
			obj.setB2Cartao2(dadosSQL.getString("b2cartao2"));
			obj.setC2Cartao2(dadosSQL.getString("c2cartao2"));
			obj.setD2Cartao2(dadosSQL.getString("d2cartao2"));
			obj.setE2Cartao2(dadosSQL.getString("e2cartao2"));
			obj.setF2Cartao2(dadosSQL.getString("f2cartao2"));
			obj.setG2Cartao2(dadosSQL.getString("g2cartao2"));
			obj.setH2Cartao2(dadosSQL.getString("h2cartao2"));
			obj.setI2Cartao2(dadosSQL.getString("i2cartao2"));
			obj.setJ2Cartao2(dadosSQL.getString("j2cartao2"));
			obj.setTopo2Cartao2(dadosSQL.getString("topo2cartao2"));

			obj.setA3Cartao2(dadosSQL.getString("a3cartao2"));
			obj.setB3Cartao2(dadosSQL.getString("b3cartao2"));
			obj.setC3Cartao2(dadosSQL.getString("c3cartao2"));
			obj.setD3Cartao2(dadosSQL.getString("d3cartao2"));
			obj.setE3Cartao2(dadosSQL.getString("e3cartao2"));
			obj.setF3Cartao2(dadosSQL.getString("f3cartao2"));
			obj.setG3Cartao2(dadosSQL.getString("g3cartao2"));
			obj.setH3Cartao2(dadosSQL.getString("h3cartao2"));
			obj.setI3Cartao2(dadosSQL.getString("i3cartao2"));
			obj.setJ3Cartao2(dadosSQL.getString("j3cartao2"));
			obj.setTopo3Cartao2(dadosSQL.getString("topo3cartao2"));

			obj.setA4Cartao2(dadosSQL.getString("a4cartao2"));
			obj.setB4Cartao2(dadosSQL.getString("b4cartao2"));
			obj.setC4Cartao2(dadosSQL.getString("c4cartao2"));
			obj.setD4Cartao2(dadosSQL.getString("d4cartao2"));
			obj.setE4Cartao2(dadosSQL.getString("e4cartao2"));
			obj.setF4Cartao2(dadosSQL.getString("f4cartao2"));
			obj.setG4Cartao2(dadosSQL.getString("g4cartao2"));
			obj.setH4Cartao2(dadosSQL.getString("h4cartao2"));
			obj.setI4Cartao2(dadosSQL.getString("i4cartao2"));
			obj.setJ4Cartao2(dadosSQL.getString("j4cartao2"));
			obj.setTopo4Cartao2(dadosSQL.getString("topo4cartao2"));

			obj.setA5Cartao2(dadosSQL.getString("a5cartao2"));
			obj.setB5Cartao2(dadosSQL.getString("b5cartao2"));
			obj.setC5Cartao2(dadosSQL.getString("c5cartao2"));
			obj.setD5Cartao2(dadosSQL.getString("d5cartao2"));
			obj.setE5Cartao2(dadosSQL.getString("e5cartao2"));
			obj.setF5Cartao2(dadosSQL.getString("f5cartao2"));
			obj.setG5Cartao2(dadosSQL.getString("g5cartao2"));
			obj.setH5Cartao2(dadosSQL.getString("h5cartao2"));
			obj.setI5Cartao2(dadosSQL.getString("i5cartao2"));
			obj.setJ5Cartao2(dadosSQL.getString("j5cartao2"));
			obj.setTopo5Cartao2(dadosSQL.getString("topo5cartao2"));

			obj.setA6Cartao2(dadosSQL.getString("a6cartao2"));
			obj.setB6Cartao2(dadosSQL.getString("b6cartao2"));
			obj.setC6Cartao2(dadosSQL.getString("c6cartao2"));
			obj.setD6Cartao2(dadosSQL.getString("d6cartao2"));
			obj.setE6Cartao2(dadosSQL.getString("e6cartao2"));
			obj.setF6Cartao2(dadosSQL.getString("f6cartao2"));
			obj.setG6Cartao2(dadosSQL.getString("g6cartao2"));
			obj.setH6Cartao2(dadosSQL.getString("h6cartao2"));
			obj.setI6Cartao2(dadosSQL.getString("i6cartao2"));
			obj.setJ6Cartao2(dadosSQL.getString("j6cartao2"));
			obj.setTopo6Cartao2(dadosSQL.getString("topo6cartao2"));

			obj.setA7Cartao2(dadosSQL.getString("a7cartao2"));
			obj.setB7Cartao2(dadosSQL.getString("b7cartao2"));
			obj.setC7Cartao2(dadosSQL.getString("c7cartao2"));
			obj.setD7Cartao2(dadosSQL.getString("d7cartao2"));
			obj.setE7Cartao2(dadosSQL.getString("e7cartao2"));
			obj.setF7Cartao2(dadosSQL.getString("f7cartao2"));
			obj.setG7Cartao2(dadosSQL.getString("g7cartao2"));
			obj.setH7Cartao2(dadosSQL.getString("h7cartao2"));
			obj.setI7Cartao2(dadosSQL.getString("i7cartao2"));
			obj.setJ7Cartao2(dadosSQL.getString("j7cartao2"));
			obj.setTopo7Cartao2(dadosSQL.getString("topo7cartao2"));

			obj.setA8Cartao2(dadosSQL.getString("a8cartao2"));
			obj.setB8Cartao2(dadosSQL.getString("b8cartao2"));
			obj.setC8Cartao2(dadosSQL.getString("c8cartao2"));
			obj.setD8Cartao2(dadosSQL.getString("d8cartao2"));
			obj.setE8Cartao2(dadosSQL.getString("e8cartao2"));
			obj.setF8Cartao2(dadosSQL.getString("f8cartao2"));
			obj.setG8Cartao2(dadosSQL.getString("g8cartao2"));
			obj.setH8Cartao2(dadosSQL.getString("h8cartao2"));
			obj.setI8Cartao2(dadosSQL.getString("i8cartao2"));
			obj.setJ8Cartao2(dadosSQL.getString("j8cartao2"));
			obj.setTopo8Cartao2(dadosSQL.getString("topo8cartao2"));

			obj.setA9Cartao2(dadosSQL.getString("a9cartao2"));
			obj.setB9Cartao2(dadosSQL.getString("b9cartao2"));
			obj.setC9Cartao2(dadosSQL.getString("c9cartao2"));
			obj.setD9Cartao2(dadosSQL.getString("d9cartao2"));
			obj.setE9Cartao2(dadosSQL.getString("e9cartao2"));
			obj.setF9Cartao2(dadosSQL.getString("f9cartao2"));
			obj.setG9Cartao2(dadosSQL.getString("g9cartao2"));
			obj.setH9Cartao2(dadosSQL.getString("h9cartao2"));
			obj.setI9Cartao2(dadosSQL.getString("i9cartao2"));
			obj.setJ9Cartao2(dadosSQL.getString("j9cartao2"));
			obj.setTopo9Cartao2(dadosSQL.getString("topo9cartao2"));

		}
		return obj;
	}

	public int validaDados(CartaoRespostaVO obj) {
		int contador = 0;

		if ((obj.getInscricaotopocartao1().equals(""))) {
			contador++;
		}

		if ((obj.getInscricaolinha1cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getSalatopocartao1().equals(""))) {
			contador++;
		}

		if ((obj.getSalalinha2cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getCursotopocartao1().equals(""))) {
			contador++;
		}

		if ((obj.getCursolinha3cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getJ1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo1Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI2Cartao1().equals(""))) {
			contador++;
		}
		if ((obj.getJ2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo2Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI3Cartao1().equals(""))) {
			contador++;
		}
		if ((obj.getJ3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo3Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getJ4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo4Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getJ5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo5Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getJ6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo6Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getJ7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo7Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getJ8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo8Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getA9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getB9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getC9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getD9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getE9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getF9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getG9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getH9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getI9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getJ9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getTopo9Cartao1().equals(""))) {
			contador++;
		}

		if ((obj.getInscricaotopocartao2().equals(""))) {
			contador++;
		}

		if ((obj.getInscricaolinha1cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getSalatopocartao2().equals(""))) {
			contador++;
		}

		if ((obj.getSalalinha2cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getCursotopocartao2().equals(""))) {
			contador++;
		}

		if ((obj.getCursolinha3cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo1Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo2Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo3Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo4Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo5Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo6Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo7Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo8Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getA9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getB9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getC9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getD9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getE9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getF9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getG9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getH9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getI9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getJ9Cartao2().equals(""))) {
			contador++;
		}

		if ((obj.getTopo9Cartao2().equals(""))) {
			contador++;
		}

		return contador;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		CartaoResposta.idEntidade = idEntidade;
	}

	@Override
	public String realizarImpressaoTesteCartaoRespostaLC3000(CartaoRespostaVO cartaoRespostaVO, String numeroTeste, Boolean imprimirCartao1, Boolean imprimirCartao2, Integer linhaInicio, Integer colunaInicio) throws Exception {
		Integer numeroCopias = imprimirCartao1 && imprimirCartao2 ? 2 : 1;
		Integer linhaTermino = linhaInicio > 0 ? linhaInicio : 9;
		Integer colunaTermino = colunaInicio > 0 ? colunaInicio : 10;
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		PdfWriter writer = null;
		try {
			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			float alturaPagina = 298f * 2.834645669f;
			float larguraPagina = 210.1f * 2.834645669f;
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, 0f, 0f, 0f, 0f);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();

			PdfContentByte canvas = writer.getDirectContent();
			PdfGraphics2D pagina = (PdfGraphics2D) writer.getDirectContent().createGraphics(pdf.getPageSize().getWidth(), pdf.getPageSize().getHeight());
			if (pagina == null) {
				pagina = (PdfGraphics2D) writer.getDirectContent().createGraphics(pdf.getPageSize().getWidth(), pdf.getPageSize().getHeight());
			}
			Integer colunas = 2;
			Integer coluna = imprimirCartao1 ? 1 : 2;
			Color preto = Color.BLACK;
			for (int copia = 1; copia <= numeroCopias; copia++) {
				// Coloca o nº da inscricao e nome do candidato
				PdfTemplate label = canvas.createTemplate(70 * CartaoRespostaRel.PONTO, 18 * CartaoRespostaRel.PONTO);
				label = canvas.createTemplate(70 * CartaoRespostaRel.PONTO, 18 * CartaoRespostaRel.PONTO);
				label.beginText();
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
				label.setColorFill(preto);
				label.showTextAligned(Element.ALIGN_LEFT, "0123456789 - ALUNO PREVISUALIZAÇÃO", 0, 0, 0f);
				label.endText();

				// canvas.addTemplate(label, (coluna == 1 ? 28 : 110) *
				// CartaoRespostaRel.PONTO, PageSize.A4.getHeight() - (40.5f
				// *
				// CartaoRespostaRel.PONTO));
				canvas.addTemplate(label, (coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getInscricaolinha1cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getInscricaolinha1cartao2().replace(',', '.'))) * CartaoRespostaRel.PONTO, PageSize.A4.getHeight() - ((coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getInscricaotopocartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getInscricaotopocartao2().replace(',', '.'))) * CartaoRespostaRel.PONTO));
				label = null;
				// Coloca o nº da sala

				label = canvas.createTemplate(70 * CartaoRespostaRel.PONTO, 18 * CartaoRespostaRel.PONTO);
				label.beginText();
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
				label.setColorFill(preto);
				label.showTextAligned(Element.ALIGN_LEFT, "Sala - 20 ", 0, 0, 0f);
				label.endText();
				// canvas.addTemplate(label, (coluna == 1 ? 28 : 110) *
				// CartaoRespostaRel.PONTO, PageSize.A4.getHeight() -
				// (43.5f
				// * CartaoRespostaRel.PONTO));
				canvas.addTemplate(label, (coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getSalalinha2cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getSalalinha2cartao2().replace(',', '.'))) * CartaoRespostaRel.PONTO, PageSize.A4.getHeight() - ((coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getSalatopocartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getSalatopocartao2().replace(',', '.'))) * CartaoRespostaRel.PONTO));
				label = null;

				// Coloca o nome do curso
				label = canvas.createTemplate(73 * CartaoRespostaRel.PONTO, 18 * CartaoRespostaRel.PONTO);
				label.beginText();
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
				label.setColorFill(preto);
				label.showTextAligned(Element.ALIGN_LEFT, "CURSO PREVISUALIZAÇÃO - NOTURNO", 0, 0, 0f);
				label.endText();
				canvas.addTemplate(label, (coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getCursolinha3cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getCursolinha3cartao2().replace(',', '.'))) * CartaoRespostaRel.PONTO, PageSize.A4.getHeight() - ((coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getCursotopocartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getCursotopocartao2().replace(',', '.'))) * CartaoRespostaRel.PONTO));
				label = null;
				pagina.setPaint(preto);
				if(!numeroTeste.trim().isEmpty()){
					numeroTeste = Uteis.preencherComZerosPosicoesVagas(numeroTeste, 9);
					for (int x = 1; x <= 9; x++) {
						pagina.fill(new RoundRectangle2D.Float(
								    getMargemEsquerdaMarcaCartaoResposta(x, Integer.parseInt(numeroTeste.substring(x - 1, x)), coluna, cartaoRespostaVO).floatValue(), 
								    getMargemTopoMarcaCartaoResposta(x, coluna, cartaoRespostaVO).floatValue(),  4.76f * CartaoRespostaRel.PONTO, 2.8f * CartaoRespostaRel.PONTO, 0, 0));
					}
				}else{
					for (int y = (linhaInicio > 0 ? linhaInicio : 1); y <= linhaTermino; y++) {
						for (int x = (colunaInicio > 0 ? colunaInicio : 1); x <= colunaTermino; x++) {
							pagina.fill(new RoundRectangle2D.Double(getMargemEsquerdaMarcaCartaoResposta(y, x - 1, coluna, cartaoRespostaVO), getMargemTopoMarcaCartaoResposta(y, coluna, cartaoRespostaVO), 4.76f * CartaoRespostaRel.PONTO, 2.8f * CartaoRespostaRel.PONTO, 0, 0));
						}
					}
				}
				if (coluna + 1 > colunas) {
					coluna = 1;
					pagina.dispose();
					pagina.finalize();
					pagina = null;
					pdf.newPage();
				} else {
					coluna++;
					pagina.dispose();
					pagina.finalize();
				}
			}
			return nomeArquivo;
		} catch (Exception e) {
			throw e;
		} finally {
			if (pdf != null) {
				pdf.close();
			}
			if (arquivo != null) {
				arquivo.flush();
				arquivo.close();
			}
		}
	 	
	}

	private Double getMargemEsquerdaMarcaCartaoResposta(Integer linha, Integer coluna, Integer nrCartao, CartaoRespostaVO cartaoRespostaVO) {
		String coordenadaX = (String) UtilReflexao.invocarMetodoGet(cartaoRespostaVO, CellReference.convertNumToColString(coluna) + linha + "Cartao" + nrCartao);
		if (!coordenadaX.trim().isEmpty()) {
			return Double.parseDouble(coordenadaX.replace(',', '.')) * 2.834645669f;
		}
		return 0.0;
	}

	private Double getMargemTopoMarcaCartaoResposta(Integer linha, Integer nrCartao, CartaoRespostaVO cartaoRespostaVO) {
		String coordenadaY = (String) UtilReflexao.invocarMetodoGet(cartaoRespostaVO, "topo" + linha + "Cartao" + nrCartao);
		if (!coordenadaY.trim().isEmpty()) {
			return Double.parseDouble(coordenadaY.replace(',', '.')) * 2.834645669f;
		}
		return 0.0;
	}

}
