/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package relatorio.negocio.jdbc.processosel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.CartaoRespostaVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisPDF;
import negocio.comuns.utilitarias.UtilReflexao;

import org.apache.poi.hssf.util.CellReference;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.CartaoRespostaRelVO;
import relatorio.negocio.interfaces.processosel.CartaoRespostaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class CartaoRespostaRel extends SuperRelatorio implements CartaoRespostaRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8156954537744206039L;

	public List<CartaoRespostaRelVO> criarObjeto(Boolean trazerSomenteCandidatosComInscricaoPaga, Integer candidato, Integer processoSeletivo, Integer itemProcSeletivoDataProva, Integer sala, Integer inscricao) throws Exception {
		validarDados(processoSeletivo);
		List<CartaoRespostaRelVO> cartaoRespostaRelVO = consultarCandidatos(trazerSomenteCandidatosComInscricaoPaga, candidato, processoSeletivo, itemProcSeletivoDataProva, sala, inscricao);
		return cartaoRespostaRelVO;
	}

	public void validarDados(Integer processoSeletivo) throws ConsistirException {
		if (processoSeletivo == 0) {
			throw new ConsistirException("O campo processo seletivo deve ser informado.");
		}
	}

	private List<CartaoRespostaRelVO> consultarCandidatos(Boolean trazerSomenteCandidatosComInscricaoPaga, Integer candidato, Integer processoSeletivo, Integer itemProcSeletivoDataProva, Integer sala, Integer inscricao) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select inscricao.codigo as codigo, curso.nome as curso, turno.nome as turno, sala.sala as sala, pessoa.nome as nomeCandidato, itemProcSeletivoDataProva.dataprova as dataProva, itemProcSeletivoDataProva.hora as horaProva FROM inscricao ");
		sb.append(" inner join pessoa on pessoa.codigo = inscricao.candidato  ");
		sb.append(" left join SalaLocalAula sala on sala.codigo = inscricao.sala  ");
		sb.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = inscricao.cursoopcao1 ");
		sb.append(" inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sb.append(" inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sb.append(" left join itemProcSeletivoDataProva on itemProcSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva ");
		sb.append(" WHERE inscricao.procSeletivo =  ");
		sb.append(processoSeletivo);
		if (candidato != 0) {
			sb.append(" AND inscricao.candidato = ");
			sb.append(candidato);
		}
		if (sala != null && sala > 0) {
			sb.append(" AND sala.codigo = ");
			sb.append(sala);
		}
		if (inscricao != null && inscricao > 0) {
			sb.append(" AND inscricao.codigo = ");
			sb.append(inscricao);
		}
		if (itemProcSeletivoDataProva != 0) {
			sb.append(" AND itemProcSeletivoDataProva.codigo = ");
			sb.append(itemProcSeletivoDataProva);
		}
		if (trazerSomenteCandidatosComInscricaoPaga) {
			sb.append(" AND inscricao.situacao = 'CO' ");
		}
		sb.append(" order by pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<CartaoRespostaRelVO> vetResultado = new ArrayList<CartaoRespostaRelVO>(0);

		while (tabelaResultado.next()) {
			CartaoRespostaRelVO obj = new CartaoRespostaRelVO();
			obj.setInscricao(tabelaResultado.getInt("codigo"));
			obj.setCurso(tabelaResultado.getString("curso").toUpperCase());
			obj.setTurno(tabelaResultado.getString("turno").toUpperCase());
			obj.setSala(tabelaResultado.getString("sala"));
			obj.setNomeAluno(tabelaResultado.getString("nomeCandidato").toUpperCase());
			if (tabelaResultado.getString("horaProva") != null || tabelaResultado.getString("horaProva").equals("")) {
				obj.setDataProva(Uteis.getData(tabelaResultado.getDate("dataProva"), "dd/MM/yyyy") + " - " + tabelaResultado.getString("horaProva"));
			} else {
				obj.setDataProva(Uteis.getData(tabelaResultado.getDate("dataProva"), "dd/MM/yyyy"));
			}
			try {
				obj.setCodigoBarra(Uteis.getCompletarNumeroComZero(5, tabelaResultado.getInt("codigo")));
			} catch (Exception e) {
				obj.setCodigoBarra(String.valueOf(tabelaResultado.getInt("codigo")));
			}
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<InscricaoVO> consultarTodosCandidatos(String valorPesquisa, String campoPesquisa, Integer itemProcSeletivoDataProva) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT inscricao.codigo AS inscricao_codigo, pessoa.codigo AS pessoa_codigo, pessoa.nome AS pessoa_nome, pessoa.cpf AS pessoa_cpf  ");
		sb.append("FROM inscricao ");
		sb.append("INNER JOIN pessoa ON inscricao.candidato = pessoa.codigo ");
		sb.append("left join itemProcSeletivoDataProva on itemProcSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva ");
		sb.append("WHERE ");
		if (campoPesquisa.equals("nome")) {
			sb.append("pessoa.nome ilike '");
			sb.append(valorPesquisa);
			sb.append("%'");
		} else if (campoPesquisa.equals("inscricao")) {
			sb.append("inscricao.codigo = ");
			sb.append(Integer.parseInt(valorPesquisa));
		} else if (campoPesquisa.equals("cpf")) {
			sb.append("pessoa.cpf ilike '");
			sb.append(valorPesquisa);
			sb.append("%'");
		}
		if (itemProcSeletivoDataProva != 0) {
			sb.append(" AND itemProcSeletivoDataProva.codigo = ");
			sb.append(itemProcSeletivoDataProva);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<InscricaoVO> vetResultado = new ArrayList<InscricaoVO>(0);
		InscricaoVO obj = null;
		while (tabelaResultado.next()) {
			obj = new InscricaoVO();
			obj.setCodigo(tabelaResultado.getInt("inscricao_codigo"));
			obj.getCandidato().setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			obj.getCandidato().setNome(tabelaResultado.getString("pessoa_nome").toUpperCase());
			obj.getCandidato().setCPF(tabelaResultado.getString("pessoa_cpf"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public String designIReportRelatorio() {
		return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	public static String getIdEntidade() {
		return "CartaoRespostaRel";
	}

	public static final float PONTO = 2.835f;

	@Override
	public String realizarImpressaoCartaoRespostaLC3000(List<CartaoRespostaRelVO> inscricaoVOs, Integer numeroCopias, Integer iniciarColuna,  boolean preenchimento, boolean impressarTeste, UsuarioVO usuarioLogado) throws Exception {
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		PdfWriter writer = null;
		try {
			float margemEsquerda = 0 * PONTO;
			float margemDireita =  0 * PONTO;
			float margemSuperior = 0 * PONTO;
			float margemInferior = 0 * PONTO;
			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			if(preenchimento){
				float alturaPagina = 298f * 2.834645669f;
				float larguraPagina = 210.1f * 2.834645669f;
				tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
				pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
				arquivo = new FileOutputStream(caminhoDaImagem.toString());
				writer = PdfWriter.getInstance(pdf, arquivo);
				pdf.open();
				realizarPreenchimentoCartaoResposta(writer, pdf, inscricaoVOs, numeroCopias,  iniciarColuna, impressarTeste);
			}else{
				tamanhoPagina = new Rectangle(PageSize.A4);
				pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
				arquivo = new FileOutputStream(caminhoDaImagem.toString());
				writer = PdfWriter.getInstance(pdf, arquivo);
				pdf.open();
				realizarMontagemPreviewCartao(writer, pdf, inscricaoVOs, numeroCopias, iniciarColuna);
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

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta
	 * para o tipo de impressora Laser/Tinta. Tem como passagem por parâmetros a
	 * <code>LayoutEtiquetaVO</code>>
	 */
	public void realizarPreenchimentoCartaoResposta(PdfWriter writer, Document pdf, List<CartaoRespostaRelVO> inscricaoVOs, Integer numeroCopias, Integer iniciarColuna, boolean impressaoTeste) throws Exception {	
		CartaoRespostaVO cartaoRespostaVO = getFacadeFactory().getCartaoRespostaFacade().consultarCartaoResposta(false, Uteis.NIVELMONTARDADOS_TODOS, null);
			
		if (cartaoRespostaVO.getCodigo().equals(0)) {
				getFacadeFactory().getCartaoRespostaFacade().incluir(cartaoRespostaVO, false, null);
				cartaoRespostaVO = (getFacadeFactory().getCartaoRespostaFacade().consultarCartaoResposta(false, Uteis.NIVELMONTARDADOS_TODOS, null));
			} 
	
		
		PdfContentByte canvas = writer.getDirectContent();
		PdfGraphics2D pagina = (PdfGraphics2D) writer.getDirectContent().createGraphics(pdf.getPageSize().getWidth(), pdf.getPageSize().getHeight());
		Integer colunas = 2;
		Integer coluna = iniciarColuna;
		Color preto = Color.BLACK;

		for (CartaoRespostaRelVO inscricaoVO : inscricaoVOs) {
			for (int copia = 1; copia <= numeroCopias; copia++) {

				if (pagina == null) {
					pagina = (PdfGraphics2D) writer.getDirectContent().createGraphics(pdf.getPageSize().getWidth(), pdf.getPageSize().getHeight());
				}
				// Coloca o nº da inscricao e nome do candidato
				PdfTemplate label = canvas.createTemplate(70 * PONTO, 18 * PONTO);
				label = canvas.createTemplate(70 * PONTO, 18 * PONTO);
				label.beginText();
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
				label.setColorFill(preto);
				label.showTextAligned(Element.ALIGN_LEFT, inscricaoVO.getInscricao() + " - " + inscricaoVO.getNomeAluno().toUpperCase(), 0, 0, 0f);
				label.endText();
				
				
				//canvas.addTemplate(label, (coluna == 1 ? 28 : 110) * PONTO, PageSize.A4.getHeight() - (40.5f * PONTO));
				canvas.addTemplate(label, (coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getInscricaolinha1cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getInscricaolinha1cartao2().replace(',', '.'))) * PONTO, PageSize.A4.getHeight() - ((coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getInscricaotopocartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getInscricaotopocartao2().replace(',', '.'))) * PONTO));
				label = null;
				// Coloca o nº da sala
				if (!inscricaoVO.getSala().trim().isEmpty()) {
					label = canvas.createTemplate(70 * PONTO, 18 * PONTO);
					label.beginText();
					label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
					label.setColorFill(preto);
					label.showTextAligned(Element.ALIGN_LEFT, "Sala - " + inscricaoVO.getSala(), 0, 0, 0f);
					label.endText();
					//canvas.addTemplate(label, (coluna == 1 ? 28 : 110) * PONTO, PageSize.A4.getHeight() - (43.5f * PONTO));
					canvas.addTemplate(label, (coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getSalalinha2cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getSalalinha2cartao2().replace(',', '.'))) * PONTO, PageSize.A4.getHeight() -  ((coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getSalatopocartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getSalatopocartao2().replace(',', '.'))) * PONTO));
					label = null;
				}

				// Coloca o nome do curso
				label = canvas.createTemplate(73 * PONTO, 18 * PONTO);
				label.beginText();
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
				label.setColorFill(preto);
				label.showTextAligned(Element.ALIGN_LEFT, inscricaoVO.getCurso().toUpperCase() + " - " + inscricaoVO.getTurno(), 0, 0, 0f);
				
				label.endText();
				//canvas.addTemplate(label, (coluna == 1 ? 28 : 110) * PONTO, PageSize.A4.getHeight() - (50.5f * PONTO));
				canvas.addTemplate(label,  (coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getCursolinha3cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getCursolinha3cartao2().replace(',', '.'))) * PONTO, PageSize.A4.getHeight() - ((coluna == 1 ? Float.parseFloat(cartaoRespostaVO.getCursotopocartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getCursotopocartao2().replace(',', '.'))) * PONTO));
				label = null;
				String inscricao = Uteis.preencherComZerosPosicoesVagas(inscricaoVO.getInscricao().toString(), 9);
				pagina.setPaint(preto);	
				if(!impressaoTeste){
					for (int x = 1; x <= 9; x++) {
						pagina.fill(new RoundRectangle2D.Float(
								    getMargemEsquerdaMarcaCartaoResposta(x, Integer.parseInt(inscricao.substring(x - 1, x)), coluna, cartaoRespostaVO).floatValue(), 
								    getMargemTopoMarcaCartaoResposta(x, coluna, cartaoRespostaVO).floatValue(),  4.76f * PONTO, 2.8f * PONTO, 0, 0));
					}
				}else{
					for (int y = 3; y <= 3; y++) {
						for (int x = 1; x <= 10; x++) {						
									pagina.fill(new RoundRectangle2D.Double(
											getMargemEsquerdaMarcaCartaoResposta(y, x-1, coluna, cartaoRespostaVO), 
											getMargemTopoMarcaCartaoResposta(y, coluna, cartaoRespostaVO),  4.76f * PONTO, 2.8f * PONTO, 0, 0));
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
		}
	}

	private Double getMargemEsquerdaMarcaCartaoResposta(Integer linha, Integer coluna, Integer nrCartao, CartaoRespostaVO cartaoRespostaVO) {
		String coordenadaX = (String) UtilReflexao.invocarMetodoGet(cartaoRespostaVO, CellReference.convertNumToColString(coluna)+linha+"Cartao"+nrCartao);
		if(!coordenadaX.trim().isEmpty()){
			return Double.parseDouble(coordenadaX.replace(',', '.')) * 2.834645669f;
		}
		return 0.0;
//		String celula = "";
//		switch (coluna) {
//		case 0:
//			celula = (nrCartao == 1? "A"+linha+"Cartao"+1 : "A"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 27.0f : 109.6f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getA1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getA1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 1:
//			celula = (nrCartao == 1? "B"+linha+"Cartao"+1 : "B"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 33.6f : 116.3f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getB1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getB1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 2:
//			celula = (nrCartao == 1? "C"+linha+"Cartao"+1 : "C"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 40.0f : 123.1f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getC1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getC1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 3:
//			celula = (nrCartao == 1? "D"+linha+"Cartao"+1 : "D"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 46.5f : 129.2f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getD1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getD1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 4:
//			celula = (nrCartao == 1? "E"+linha+"Cartao"+1 : "E"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 53.0f : 135.6f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getE1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getE1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 5:
//			celula = (nrCartao == 1? "F"+linha+"Cartao"+1 : "F"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 59.2f : 141.9f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getF1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getF1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 6:
//			celula = (nrCartao == 1? "G"+linha+"Cartao"+1 : "G"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 65.1f : 148.1f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getG1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getG1Cartao2().replace(',', '.'))) * 2.834645669, 2);					
//		case 7:
//			celula = (nrCartao == 1? "H"+linha+"Cartao"+1 : "H"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 71.2f : 157.9f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getH1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getH1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 8:
//			celula = (nrCartao == 1? "I"+linha+"Cartao"+1 : "I"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 77.7f : 160.5f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getI1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getI1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 9:
//			celula = (nrCartao == 1? "J"+linha+"Cartao"+1 : "J"+linha+"Cartao"+2);
//			break;
//			//return Uteis.arredondarDecimal((nrCartao == 1 ? 84.3f : 166.8f) * PONTO, 2);
////			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getJ1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getJ1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//			
//		default:
//			return 0.0;
//		}
		
	}

	private Double getMargemTopoMarcaCartaoResposta(Integer linha, Integer nrCartao, CartaoRespostaVO cartaoRespostaVO) {
		String coordenadaY = (String) UtilReflexao.invocarMetodoGet(cartaoRespostaVO, "topo"+linha+"Cartao"+nrCartao);
		if(!coordenadaY.trim().isEmpty()){
			return Double.parseDouble(coordenadaY.replace(',', '.')) * 2.834645669f;
		}
		return 0.0;
//		switch (linha) {
//		case 1:
//			//return Uteis.arredondarDecimal((57.3F) * PONTO, 2);
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo1Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo1Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 2:
//			//return Uteis.arredondarDecimal((63.6F) * PONTO, 2);
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo2Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo2Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 3:
//			//return Uteis.arredondarDecimal((69.9F) * PONTO, 2);
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo3Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo3Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 4:
//			//return Uteis.arredondarDecimal((76.2F) * PONTO, 2);
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo4Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo4Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 5:
//			//return Uteis.arredondarDecimal((82.5F) * PONTO, 2);
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo5Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo5Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 6:
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo6Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo6Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 7:
//			//return Uteis.arredondarDecimal((95.1F) * PONTO, 2);
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo7Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo7Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 8:
//			//return Uteis.arredondarDecimal((101.4F) * PONTO, 2);
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo8Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo8Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		case 9:
//			//return Uteis.arredondarDecimal((107.7F) * PONTO, 2);
//			return Uteis.arredondarDecimal((nrCartao == 1 ? Float.parseFloat(cartaoRespostaVO.getTopo9Cartao1().replace(',', '.')) : Float.parseFloat(cartaoRespostaVO.getTopo9Cartao2().replace(',', '.'))) * 2.834645669, 2);
//		default:
//			return 0.0;
//		}
//		switch (linha) {
//		case 1:
//			return (57.1F) * PONTO;
//		case 2:
//			return (63.3F) * PONTO;
//		case 3:
//			return (69.6F) * PONTO;
//		case 4:
//			return (75.95F) * PONTO;
//		case 5:
//			return (82.25F) * PONTO;
//		case 6:
//			return (88.55F) * PONTO;
//		case 7:
//			return (94.85F) * PONTO;
//		case 8:
//			return (101.15F) * PONTO;
//		case 9:
//			return (107.45F) * PONTO;
//		default:
//			return 0f;
//		}
	}

	public void realizarMontagemPreviewCartao(PdfWriter writer, Document pdf, List<CartaoRespostaRelVO> inscricaoVOs, Integer numeroCopias, Integer iniciarColuna) throws Exception {
		float alturaPagina = PageSize.LETTER.getHeight();
		float larguraPagina = PageSize.LETTER.getWidth();
		float margemEsquerda = 26F * PONTO;
		float margemSuperior = 5 * PONTO;
		float margemInferior = 5 * PONTO;
		float alturaEtiqueta = PageSize.LETTER.getHeight() - margemInferior - margemInferior;
		float larguraEtiqueta = 82.55f * PONTO;
		float margemVerticalEntreEtiquetas = 0.4F * PONTO;
		Float margemSuperiorColuna = 0f;
		Float margemEsquerdaLabel = 0f;
		Float margemSuperiorLabel = 0f;
		Float margemEsquerdaColuna = 0f;
		float larguraStrobe = 3.17f * PONTO;
		float alturaMarca = 2.54f * PONTO;
		float larguraMarca = 4.76f * PONTO;
		float distanciaEntreStrobePrimeiraMarca = 4.5f * PONTO;
		float distanciaEntreMarcaVerticalmente = 6.35f * PONTO;
		float distanciaEntreMarcaHorizontalmente = 2.54f * PONTO;
		PdfContentByte canvas = writer.getDirectContent();

		PdfGraphics2D pagina = (PdfGraphics2D) writer.getDirectContent().createGraphics(larguraPagina, alturaPagina);

		Integer colunas = 2;
		Integer coluna = iniciarColuna;
		Color laranja = new Color(168, 42, 0); // new Color(217,144,88,10);
		Color preto = Color.BLACK;
		Color cinza = new Color(193, 192, 208);

		float dash1[] = { 2.0f };
		BasicStroke bordaTracejada = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash1, 0.0f);
		BasicStroke bordaSimples = new BasicStroke(1.0f);
		java.awt.Font fontNumeroCartao = new java.awt.Font(BaseFont.TIMES_ROMAN, Font.NORMAL, 5);
		java.awt.Font fontVetibular = new java.awt.Font(BaseFont.COURIER_BOLD, Font.BOLD, 14);
		Image logo = Image.getInstance(UteisJSF.getCaminhoWeb() + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
		logo.scaleToFit(larguraEtiqueta, 10 * PONTO);
		logo.setAbsolutePosition(0f * PONTO, 0 * PONTO);
		logo.setAlignment(Image.MIDDLE);
		Float margemEsquerdaLogo = (larguraEtiqueta - logo.getPlainWidth()) / 2;

		for (CartaoRespostaRelVO inscricaoVO : inscricaoVOs) {
			for (int copia = 1; copia <= numeroCopias; copia++) {

				if (pagina == null) {
					pagina = (PdfGraphics2D) writer.getDirectContent().createGraphics(larguraPagina, alturaPagina);
				}

				margemSuperiorColuna = alturaPagina - margemSuperior;
				margemEsquerdaColuna = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);

				// Borda Geral do Cartao
				pagina.setStroke(bordaTracejada);
				pagina.setPaint(cinza);
				pagina.draw(new RoundRectangle2D.Double(margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta) - (0.4 * PONTO), margemSuperior, larguraEtiqueta + (0.4 * PONTO), alturaEtiqueta, 0, 0));

				// Coloca o texto "VESTIBULAR" no cartao resposta

				pagina.setPaint(laranja);
				pagina.fill(new RoundRectangle2D.Double(margemEsquerdaColuna + (13f * PONTO), margemSuperior + (2 * PONTO), 56 * PONTO, 6 * PONTO, 0, 0));

				pagina.setPaint(Color.WHITE);
				pagina.setFont(fontVetibular);
				pagina.drawString("VESTIBULAR", margemEsquerdaColuna + (25f * PONTO), margemSuperior + (6.8f * PONTO));

				// Coloca a logo no cartao resposta
				PdfTemplate tmpImage = canvas.createTemplate(larguraEtiqueta, 10 * PONTO);
				tmpImage.beginText();
				tmpImage.addImage(logo, true);
				tmpImage.endText();
				canvas.addTemplate(tmpImage, margemEsquerdaColuna + margemEsquerdaLogo, margemSuperiorColuna - (20 * PONTO));
				tmpImage = null;
				// Criar a borda com os dados do candidato
				pagina.setStroke(bordaSimples);
				pagina.setPaint(laranja);
				pagina.draw(new RoundRectangle2D.Double(margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta) + (4 * PONTO), margemSuperior + (22 * PONTO), 73 * PONTO, 18 * PONTO, 10, 10));

				margemEsquerdaLabel = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta) + (4 * PONTO);
				margemSuperiorLabel = alturaPagina - margemSuperior - (30 * PONTO);

				// Coloca o texto "INSCRIÇÃO/CANDIDATO"
				PdfTemplate label = canvas.createTemplate(73 * PONTO, 18 * PONTO);
				label.beginText();
				label.setColorFill(laranja);
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 6f);
				label.showTextAligned(Element.ALIGN_LEFT, "INSCRIÇÃO/CANDIDATO", (2 * PONTO), (6 * PONTO), 0f);
				label.endText();
				canvas.addTemplate(label, margemEsquerdaLabel, margemSuperiorLabel);
				label = null;
				// Coloca o nº da inscricao e nome do candidato
				margemSuperiorLabel -= (4 * PONTO);
				label = canvas.createTemplate(73 * PONTO, 18 * PONTO);
				label.beginText();
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
				label.setColorFill(preto);
				label.showTextAligned(Element.ALIGN_LEFT, inscricaoVO.getInscricao() + " - " + inscricaoVO.getNomeAluno().toUpperCase(), (2 * PONTO), (6 * PONTO), 0f);
				label.endText();
				canvas.addTemplate(label, margemEsquerdaLabel, margemSuperiorLabel);
				label = null;
				// Coloca o nº da sala

				margemSuperiorLabel -= (4 * PONTO);
				if (!inscricaoVO.getSala().trim().isEmpty()) {
					label = canvas.createTemplate(73 * PONTO, 18 * PONTO);
					label.beginText();
					label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
					label.setColorFill(preto);
					label.showTextAligned(Element.ALIGN_LEFT, "Sala - " + inscricaoVO.getSala().toString(), (2 * PONTO), (6 * PONTO), 0f);
					label.endText();
					canvas.addTemplate(label, margemEsquerdaLabel, margemSuperiorLabel);
					label = null;
				}
				// Coloca o texto "CURSO/OPCAO"
				margemSuperiorLabel -= (3 * PONTO);
				label = canvas.createTemplate(73 * PONTO, 18 * PONTO);
				label.beginText();
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 6f);
				label.setColorFill(laranja);
				label.showTextAligned(Element.ALIGN_LEFT, "CURSO/OPCAO", (2 * PONTO), (6 * PONTO), 0f);
				label.endText();
				canvas.addTemplate(label, margemEsquerdaLabel, margemSuperiorLabel);
				label = null;
				// Coloca o nome do curso
				margemSuperiorLabel -= (4 * PONTO);
				label = canvas.createTemplate(73 * PONTO, 18 * PONTO);
				label.beginText();
				label.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), 10f);
				label.setColorFill(preto);
				label.showTextAligned(Element.ALIGN_LEFT, inscricaoVO.getCurso().toUpperCase() + " - " + inscricaoVO.getTurno(), (2 * PONTO), (6 * PONTO), 0f);
				label.endText();
				canvas.addTemplate(label, margemEsquerdaLabel, margemSuperiorLabel);
				label = null;
				// cria as linhas marcadas com o numero da inscricao
				margemSuperiorLabel = margemSuperior + (18f * PONTO) + (22f * PONTO) + (5 * PONTO);
				for (int x = 0; x < 9; x++) {
					margemEsquerdaLabel = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);
					if (x >= 1) {
						margemSuperiorLabel = margemSuperiorLabel + distanciaEntreMarcaHorizontalmente + alturaMarca;
					}
					pagina.setStroke(bordaSimples);
					pagina.setPaint(preto);
					pagina.draw(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraStrobe, alturaMarca, 0, 0));

					pagina.fill(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraStrobe, alturaMarca, 0, 0));
					String inscricao = Uteis.preencherComZerosPosicoesVagas(inscricaoVO.getInscricao().toString(), 9);
					for (int y = 1; y <= 12; y++) {
						pagina.setStroke(bordaSimples);
						pagina.setPaint(laranja);
						if (y > 1) {
							margemEsquerdaLabel += distanciaEntreMarcaVerticalmente;
						} else {
							margemEsquerdaLabel += distanciaEntreStrobePrimeiraMarca;
						}

						pagina.draw(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraMarca, alturaMarca, 0, 0));
						if (inscricao.length() >= x + 1 && Integer.parseInt(inscricao.substring(x, x + 1)) == y - 1) {
							pagina.setPaint(preto);

							pagina.fill(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraMarca, alturaMarca, 0, 0));
						}
					}
				}

				// cria as linhas de marcacao de resposta
				for (int x = 1; x <= 32; x++) {
					margemEsquerdaLabel = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);
					margemSuperiorLabel = margemSuperiorLabel + distanciaEntreMarcaHorizontalmente + alturaMarca;
					pagina.setStroke(bordaSimples);
					pagina.setPaint(preto);
					pagina.draw(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraStrobe, alturaMarca, 0, 0));

					pagina.fill(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraStrobe, alturaMarca, 0, 0));

					for (int y = 1; y <= 12; y++) {
						pagina.setStroke(bordaSimples);
						pagina.setPaint(laranja);
						if (y > 1) {
							margemEsquerdaLabel += distanciaEntreMarcaVerticalmente;
						} else {
							margemEsquerdaLabel += distanciaEntreStrobePrimeiraMarca;
						}

						if (y == 7 && x == 32) {
							break;
						} else {

							pagina.draw(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraMarca, alturaMarca, 0, 0));
							if (y == 1) {
								pagina.setPaint(laranja);
								pagina.fill(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraMarca, alturaMarca, 0, 0));
								pagina.setPaint(Color.WHITE);
								pagina.setFont(fontNumeroCartao);
								pagina.drawString(String.valueOf(x).length() == 1 ? "0" + String.valueOf(x) : String.valueOf(x), margemEsquerdaLabel + (1f * PONTO), margemSuperiorLabel + (2 * PONTO));

							} else if (y == 7 && x <= 31) {
								pagina.setPaint(laranja);
								pagina.fill(new RoundRectangle2D.Double(margemEsquerdaLabel, margemSuperiorLabel, larguraMarca, alturaMarca, 0, 0));
								pagina.setPaint(Color.WHITE);
								pagina.drawString(String.valueOf(x + 32), margemEsquerdaLabel + (1f * PONTO), margemSuperiorLabel + (2 * PONTO));
							} else {
								pagina.setPaint(laranja);
								pagina.setFont(fontNumeroCartao);
								pagina.drawString(y == 2 || y == 8 ? "A" : y == 3 || y == 9 ? "B" : y == 4 || y == 10 ? "C" : y == 5 || y == 11 ? "D" : "E", margemEsquerdaLabel + (1.5f * PONTO), margemSuperiorLabel + (2 * PONTO));
							}
						}
					}
				}

				margemEsquerdaLabel = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);
				margemSuperiorLabel = margemSuperiorLabel + (4.5F * PONTO);
				// Criar a borda de assinatura
				pagina.setStroke(bordaSimples);
				pagina.setPaint(laranja);
				pagina.draw(new RoundRectangle2D.Double(margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta) + (4 * PONTO), margemSuperiorLabel, 73 * PONTO, 8 * PONTO, 10, 10));

				// Coloca o texto "A S S I N A T U R A"
				margemSuperiorLabel += (10 * PONTO);
				pagina.setPaint(laranja);
				pagina.setFont(fontNumeroCartao.deriveFont(5f));
				pagina.drawString("A S S I N A T U R A", margemEsquerdaLabel + (33.5f * PONTO), margemSuperiorLabel);

				// Coloca o texto "NÃO RASURAR O CARTÃO. USAR CANETA"
				margemSuperiorLabel += (3f * PONTO);
				pagina.setPaint(laranja);
				pagina.setFont(fontNumeroCartao.deriveFont(6f));
				pagina.drawString("NÃO RASURAR O CARTÃO. USAR CANETA", margemEsquerdaLabel + (20.3f * PONTO), margemSuperiorLabel);

				// Coloca o texto "ESFEROGRÁFICA AZUL OU PRETA"
				margemSuperiorLabel += (3f * PONTO);
				pagina.setPaint(laranja);
				pagina.setFont(fontNumeroCartao.deriveFont(6f));
				pagina.drawString("ESFEROGRÁFICA AZUL OU PRETA", margemEsquerdaLabel + (24.3f * PONTO), margemSuperiorLabel);

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
		}
	}

}
