package relatorio.negocio.jdbc.academico;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.financeiro.CartaCobrancaRelVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.interfaces.academico.EtiquetaAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EtiquetaAlunoRel extends SuperRelatorio implements EtiquetaAlunoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;
	public static final float PONTO = 2.83f;

	public EtiquetaAlunoRel() {

	}
	
	public void validarDados(Integer curso, Integer turma, String matricula, Integer periodoletivo, String tipoRelatorio) throws Exception {
		if (tipoRelatorio.equals("aluno")) {
			if (matricula.equals("")) {
				throw new Exception("O campo CURSO OU TURMA devem ser informados.");
			}
			if (periodoletivo.equals(0)) {
				throw new Exception("O campo PERÍODO LETIVO deve ser informado.");
			}
		} else if (tipoRelatorio.equals("cartaCobranca")) {
			return;
		} else {
			if (curso.equals(null) || curso.equals(0)) {
				throw new Exception("O campo CURSO deve ser informado.");
			}
			if(turma.equals(null) || turma.equals(0)){
				throw new Exception("O campo TURMA deve ser informado.");
			}
		}
	}

	@Override
	public String realizarImpressaoEtiquetaMatricula(LayoutEtiquetaVO layoutEtiqueta, Integer numeroCopias, Integer linha, Integer coluna, Integer curso, Integer turma, String matricula, String ano, String semestre, Integer periodoletivo, String tipoRelatorio, String nivelEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioLogado, String via) throws Exception {
		validarDados(curso, turma, matricula, periodoletivo, tipoRelatorio);
		List<MatriculaPeriodoVO> listaMatriculaPeriodoVOs = executarConsultaParametrizada(curso, turma, matricula, ano, 
                        semestre, periodoletivo, tipoRelatorio, filtroRelatorioAcademicoVO, usuarioLogado, layoutEtiqueta.getCodigoExpedicaoDiploma(), via);
		if (listaMatriculaPeriodoVOs.isEmpty()) {
			throw new Exception("Nenhum valor foi encontrado com os filtros informados.");
		}
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		try {
			if (layoutEtiqueta.getAlturaEtiqueta().equals(0) || layoutEtiqueta.getAlturaFolhaImpressao().equals(0) || layoutEtiqueta.getLarguraEtiqueta().equals(0) || layoutEtiqueta.getLarguraFolhaImpressao().equals(0)) {
				throw new Exception("Insira um valor para altura ou largura");
			}
			layoutEtiqueta.setLayoutEtiquetaTagVO(getFacadeFactory().getLayoutEtiquetaTagFacade().consultarLayoutEtiquetaTagItens(layoutEtiqueta.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoGeralSistemaVO, usuarioLogado));
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + "/" + "relatorio" + "/"+ nomeArquivo);
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();

			realizarMontagemPreviewEtiquetLaserTinta(layoutEtiqueta, listaMatriculaPeriodoVOs, writer, pdf, numeroCopias, linha, coluna);
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

	public String realizarImpressaoEtiquetaCartaCobranca(LayoutEtiquetaVO layoutEtiqueta, CartaCobrancaRelVO cartaCobranca, Date periodoInicial, Date periodoFinal, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Integer numeroCopias, Integer linha, Integer coluna, Integer curso, Integer turma, String matricula, String ano, String semestre, Integer periodoletivo, String tipoRelatorio, String nivelEducacional, List<CentroReceitaVO> centroReceitaVOs, UnidadeEnsinoVO unidadeEnsino, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		validarDados(curso, turma, matricula, periodoletivo, tipoRelatorio);
		List<MatriculaPeriodoVO> listaMatriculaPeriodoVOs = null;
		listaMatriculaPeriodoVOs = execultarConsultaCartaCobranca(unidadeEnsino, curso, turma, matricula, cartaCobranca, usuarioLogado, periodoInicial,  periodoFinal, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceitaVOs );
		//listaMatriculaPeriodoVOs = execultarConsultaCartaCobranca(cartaCobranca, usuarioVO, periodoInicial, periodoFinal, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO)(null, curso, turma, matricula, cartaCobranca, usuarioLogado, periodoInicial,  periodoFinal, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO );
		// CartaCobrancaRelVO cartaCobranca, UsuarioVO usuarioVO, Date
		// periodoInicial, Date periodoFinal, 
		//FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO
		//) throws Exception {
		if (listaMatriculaPeriodoVOs.isEmpty()) {
			throw new Exception("Nenhum valor foi encontrado com os filtros informados.");
		}
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		String nomeArquivo = String.valueOf(new Date().getTime()) + ".pdf";
		StringBuffer caminhoDaImagem = new StringBuffer();
		try {
			if (layoutEtiqueta.getAlturaEtiqueta().equals(0) || layoutEtiqueta.getAlturaFolhaImpressao().equals(0) || layoutEtiqueta.getLarguraEtiqueta().equals(0) || layoutEtiqueta.getLarguraFolhaImpressao().equals(0)) {
				throw new Exception("Insira um valor para altura ou largura");
			}
			layoutEtiqueta.setLayoutEtiquetaTagVO(getFacadeFactory().getLayoutEtiquetaTagFacade().consultarLayoutEtiquetaTagItens(layoutEtiqueta.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoGeralSistemaVO, usuarioLogado));
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;

			caminhoDaImagem.append(UteisJSF.getCaminhoWeb() + "/" + "relatorio" + "/" + nomeArquivo);
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();

			realizarMontagemPreviewEtiquetLaserTinta(layoutEtiqueta, listaMatriculaPeriodoVOs, writer, pdf, numeroCopias, linha, coluna);
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
	
	public void realizarMontagemPreviewEtiquetLaserTinta(LayoutEtiquetaVO layoutEtiqueta, List<MatriculaPeriodoVO> listaMatriculaPeriodoVOs, PdfWriter writer, Document pdf, Integer numeroCopias, Integer linha, Integer coluna) throws Exception {

		float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
		float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
		float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
		float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;		
		float alturaEtiqueta = layoutEtiqueta.getAlturaEtiqueta().floatValue() * PONTO;
		float larguraEtiqueta = layoutEtiqueta.getLarguraEtiqueta().floatValue() * PONTO;
		float margemHorizontalEntreEtiquetas = (layoutEtiqueta.getMargemEntreEtiquetaHorizontal() == 0 ? 0.2f : layoutEtiqueta.getMargemEntreEtiquetaHorizontal().floatValue()) * PONTO;
		float margemVerticalEntreEtiquetas = layoutEtiqueta.getMargemEntreEtiquetaVertical() * PONTO;
		Float margemSuperiorColuna = 0f;
		Float margemEsquerdaLabel = 0f;
		Float margemSuperiorLabel = 0f;
		Float margemEsquerdaColuna = 0f;

		PdfContentByte canvas = writer.getDirectContent();
		PdfGraphics2D pdfEtiqueta = (PdfGraphics2D) writer.getDirectContent().createGraphics(larguraPagina, alturaPagina);
		Integer linhas = layoutEtiqueta.getNumeroLinhasEtiqueta();
		Integer colunas = layoutEtiqueta.getNumeroColunasEtiqueta();

		for (MatriculaPeriodoVO matriculaPeriodoVO : listaMatriculaPeriodoVOs) {
			for (int copia = 1; copia <= numeroCopias; copia++) {
				margemSuperiorColuna = alturaPagina - margemSuperior - ((linha - 1) * margemHorizontalEntreEtiquetas) - ((linha - 1) * alturaEtiqueta);
				margemEsquerdaColuna = margemEsquerda + ((coluna - 1) * margemVerticalEntreEtiquetas) + ((coluna - 1) * larguraEtiqueta);
				for (LayoutEtiquetaTagVO tag : layoutEtiqueta.getLayoutEtiquetaTagVOs()) {
					margemEsquerdaLabel = tag.getMargemDireita() * PONTO;
					margemSuperiorLabel = tag.getMargemTopo() * PONTO;
					PdfTemplate tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
					tmp.beginText();
					tmp.setFontAndSize(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true), tag.getTamanhoFonte().floatValue());
					String numero = getFacadeFactory().getControleLivroRegistroDiplomaFacade().obterNumeroRegistroMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula());
					matriculaPeriodoVO.getExpedicaoDiplomaVO().setNumeroRegistroDiploma(numero);
					tmp.showTextAligned(Element.ALIGN_LEFT, (tag.getLabelTag() + " " + getValorImprimirEtiqueta(matriculaPeriodoVO, tag.getTagEtiqueta())).trim(), margemEsquerdaLabel.floatValue(), alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
					tmp.endText();					
					canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);

				}
				
				
				if (coluna + 1 > colunas) {
					coluna = 1;
					if (linha + 1 > linhas) {
						pdf.newPage();
						linha = 1;
					} else {
						linha++;
					}
				} else {
					coluna++;
				}
			}
			pdfEtiqueta.dispose(); 	
		}			
		
	}

	public List<MatriculaPeriodoVO> executarConsultaParametrizada(Integer curso, Integer turma, String matricula, 
                String ano, String semestre, Integer periodoletivo, String tipoRelatorio, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuarioVO, Integer codigoExpedicaoDiploma, String viaExpedicaoDiploma) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct matriculaperiodo.turma, matricula.matricula,matriculaperiodo.ano, matriculaperiodo.semestre, pessoa.nome AS \"pessoa.nome\", pessoa.endereco, pessoa.numero, pessoa.setor, ");
		sb.append(" pessoa.complemento, pessoa.telefoneRes, pessoa.celular, pessoa.cpf, periodoletivo.periodoletivo, pessoa.rg, pessoa.cep,  ");
		sb.append(" cidade.nome as \"pessoa.cidade\", estado.sigla as \"pessoa.estado\", expedicaoDiploma.numeroProcesso as \"expedicaoDiploma.numeroProcesso\", ");
		sb.append(" expedicaoDiploma.dataExpedicao as \"expedicaoDiploma.dataExpedicao\", expedicaoDiploma.via as \"expedicaoDiploma.via\", unidadeensino.cidade AS \"unidadeensino.cidade\", curso.nome AS \"curso.nome\", unidadeensino.nome AS \"unidadeensino.nome\", turno.nome AS \"turno.nome\", ");
		sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\" ");
		sb.append(" from matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sb.append(" left join expedicaoDiploma on expedicaoDiploma.matricula = matricula.matricula ");
        sb.append(" and expedicaoDiploma.codigo = (select ed.codigo from expedicaoDiploma ed where ed.matricula = matricula.matricula ");
        if (Uteis.isAtributoPreenchido(viaExpedicaoDiploma)) {
        	sb.append(" and ed.via = '").append(viaExpedicaoDiploma).append("'");
        }
        sb.append(" order by ed.codigo desc limit 1) ");
		sb.append(" left join cidade on cidade.codigo = pessoa.cidade ");
		sb.append(" left join estado on estado.codigo = cidade.estado ");
		sb.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sb.append(" inner join turno on turno.codigo = matricula.turno ");
		sb.append(" inner join turma on turma.codigo = matriculaPeriodo.turma ");
		sb.append(" where 1=1 ");
		if (filtroAcademicoVO != null) {
			sb.append(" and ").append(realizarGeracaoWherePeriodo(filtroAcademicoVO.getDataInicio(), filtroAcademicoVO.getDataTermino(), "matriculaperiodo.data", false));
		}
		if (tipoRelatorio.equals("aluno")) {
			sb.append(" and matricula.matricula = '").append(matricula).append("' ");
            if (periodoletivo != 0) {
				sb.append(" and periodoletivo.periodoletivo = ").append(periodoletivo);
            }
            if (codigoExpedicaoDiploma != 0) {
				sb.append(" and expedicaoDiploma.codigo = ").append(codigoExpedicaoDiploma);
            }
			if (filtroAcademicoVO != null) {            
	            sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaPeriodo"));
	    		sb.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaPeriodo"));
			}
			sb.append(" order by pessoa.nome , matriculaperiodo.ano desc, matriculaperiodo.semestre desc limit 1 ");
		} else {
			if (!curso.equals(0)) {
				sb.append(" and curso.codigo = ").append(curso);
			}
			if (!turma.equals(0)) {
				sb.append(" and matriculaperiodo.turma = ").append(turma);
			}
			if (!ano.equals("")) {
				sb.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
			}
			if (!semestre.equals("")) {
				sb.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			}
			if (filtroAcademicoVO != null) {
				sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaPeriodo"));
				sb.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaPeriodo"));
			}
			sb.append(" order by pessoa.nome , matriculaperiodo.ano desc, matriculaperiodo.semestre desc ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}
	
	
	public List<MatriculaPeriodoVO> execultarConsultaCartaCobranca(UnidadeEnsinoVO unidadeEnsino, Integer curso, Integer turma, String matricula, CartaCobrancaRelVO cartaCobranca, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from (select distinct matriculaperiodo.turma, matricula.matricula,matriculaperiodo.ano, curso.nome as \"curso.nome\",  unidadeensino.nome as \"unidadeensino.nome\", turno.nome as \"turno.nome\",");
		sql.append(" UnidadeEnsino.razaosocial as unidade, UnidadeEnsino.telcomercial1 as fixo, UnidadeEnsino.telcomercial2 as celular, periodoletivo.periodoletivo, ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.nome else pessoa.nome end AS \"pessoa.nome\" , ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.endereco else pessoa.endereco end AS \"endereco\", ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.numero else pessoa.numero end AS \"numero\", ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.complemento else pessoa.complemento end AS \"complemento\", ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.setor else pessoa.setor end AS \"setor\", ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then estadoPais.sigla else estadoAluno.sigla end AS \"pessoa.estado\", ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then cidadePais.nome else cidadeAluno.nome end AS \"pessoa.cidade\",   ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.cep else pessoa.cep end AS \"cep\" ,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.rg else pessoa.rg end AS \"rg\"  ,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.telefoneRes else pessoa.telefoneRes end AS \"telefoneRes\"  ,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.celular else pessoa.celular end AS \"celular\"  ,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.cpf else pessoa.cpf end AS \"cpf\", ");
		sql.append(" expedicaoDiploma.dataExpedicao as \"expedicaoDiploma.dataExpedicao\", expedicaoDiploma.via as \"expedicaoDiploma.via\", expedicaoDiploma.numeroProcesso AS \"expedicaoDiploma.numeroProcesso\", unidadeensino.cidade AS \"unidadeensino.cidade\", unidadeensino.nome AS \"unidadeensino.nome\", ");
		sql.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\" ");
		sql.append(" from contareceber");
		sql.append(" inner JOIN UnidadeEnsino ON (contareceber.unidadeEnsino = unidadeEnsino.codigo)");
		sql.append(" inner join pessoa on ((contareceber.tipopessoa = 'AL' and pessoa.codigo = contareceber.pessoa) or (contareceber.tipopessoa = 'RF' and contareceber.responsavelfinanceiro = pessoa.codigo))");
		sql.append(" left join matricula  on contareceber.matriculaaluno = matricula.matricula");
		sql.append(" left join curso on matricula.curso=curso.codigo");
		sql.append(" left join pessoa as aluno on matricula.aluno= aluno.codigo");
		sql.append(" left join matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by mp.ano ||'/'|| mp.semestre desc, case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, matriculaperiodo.codigo desc limit 1)");
		sql.append(" left join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula  ");
		sql.append(" left join turma on turma.codigo=matriculaPeriodo.turma");
		sql.append(" left join filiacao on filiacao.aluno= aluno.codigo and filiacao.responsavelfinanceiro = true and contareceber.tipopessoa = 'AL'");
		sql.append(" left join pessoa as pais on pais.codigo= filiacao.pais ");
		sql.append(" left join cidade as cidadePais on cidadePais.codigo=pais.cidade");
		sql.append(" left join estado  as estadoPais on estadoPais.codigo=cidadePais.estado");
		sql.append(" left join cidade as cidadeAluno on cidadeAluno.codigo=pessoa.cidade");
		sql.append(" left join estado as estadoAluno on estadoAluno.codigo=cidadeAluno.estado");
		sql.append(" left join turno on turno.codigo = matricula.turno  ");
		sql.append(" left join expedicaoDiploma on expedicaoDiploma.matricula = matricula.matricula ");
		sql.append(" and expedicaoDiploma.codigo = (select ed.codigo from expedicaodiploma ed where ed.matricula = matricula.matricula order by ed.codigo desc limit 1) ");
		sql.append(" WHERE contareceber.situacao = 'AR' and (contareceber.tipopessoa = 'AL' or contareceber.tipopessoa = 'RF') ");
		if (matricula.equals("")) {
			sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		}
		sql.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sql.append(" AND curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append(" AND turma.codigo = ").append(turma);
		}
		StringBuilder sql2 = new StringBuilder("");
		for (CentroReceitaVO centroReceitaVO : centroReceitaVOs) {
			if (centroReceitaVO.getFiltrarCentroReceitaVO()) {
				sql2.append(sql2.length() == 0 ? " and contareceber.centroreceita in(" : ", ").append(centroReceitaVO.getCodigo());
			}
		}
		if (sql2.length() > 0) {
			sql2.append(") ");
			sql.append(sql2);
		}
		if (matricula != null && !matricula.equals("")) {
			sql.append(" AND matricula.matricula = '").append(matricula).append("' ");
		}
		sql.append(" AND ((contareceber.datavencimento >='").append(Uteis.getDataJDBC(periodoInicial)).append("' and contareceber.datavencimento <= '").append(Uteis.getDataJDBC(periodoFinal)).append("')").append(" and contareceber.datavencimento < CURRENT_DATE)");
		sql.append(")as t order by \"pessoa.nome\"");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, usuarioVO);

	}
	
	public List<MatriculaPeriodoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
		List<MatriculaPeriodoVO> listaMatriculaPeriodoVOs = new java.util.ArrayList<MatriculaPeriodoVO>(0);
		while (tabelaResultado.next()) {
			MatriculaPeriodoVO obj = new MatriculaPeriodoVO();
			montarDados(tabelaResultado, obj, usuarioVO);
			listaMatriculaPeriodoVOs.add(obj);
		}
		return listaMatriculaPeriodoVOs;
	}
	
	public void montarDados(SqlRowSet dadosSQL, MatriculaPeriodoVO obj, UsuarioVO usuarioVO) {
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatriculaVO().getAluno().setEndereco(dadosSQL.getString("endereco"));
		obj.getMatriculaVO().getAluno().setNumero(dadosSQL.getString("numero"));
		obj.getMatriculaVO().getAluno().setSetor(dadosSQL.getString("setor"));
		obj.getMatriculaVO().getAluno().setComplemento(dadosSQL.getString("complemento"));
		obj.getMatriculaVO().getAluno().setTelefoneRes(dadosSQL.getString("telefoneRes"));
		obj.getMatriculaVO().getAluno().setCelular(dadosSQL.getString("celular"));
		obj.getMatriculaVO().getAluno().setCPF(dadosSQL.getString("cpf"));
		obj.getMatriculaVO().getAluno().setRG(dadosSQL.getString("rg"));
		obj.getMatriculaVO().getAluno().setCEP(dadosSQL.getString("cep"));
		obj.getMatriculaVO().getAluno().getCidade().setNome(dadosSQL.getString("pessoa.cidade"));
		obj.getMatriculaVO().getAluno().getCidade().getEstado().setSigla(dadosSQL.getString("pessoa.estado"));
		obj.getPeridoLetivo().setPeriodoLetivo(dadosSQL.getInt("periodoletivo"));
		obj.getMatriculaVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
        obj.getMatriculaVO().getUnidadeEnsino().getCidade().setCodigo(dadosSQL.getInt("unidadeEnsino.cidade"));
        obj.getMatriculaVO().getTurno().setNome(dadosSQL.getString("turno.nome"));
		obj.setAno(dadosSQL.getString("ano"));
        try {
			CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getMatriculaVO().getUnidadeEnsino().getCidade().getCodigo(), false, usuarioVO);
            obj.getMatriculaVO().getUnidadeEnsino().setCidade(cidadeVO);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        obj.getExpedicaoDiplomaVO().setDataExpedicao(dadosSQL.getDate("expedicaoDiploma.dataExpedicao"));
        obj.getExpedicaoDiplomaVO().setNumeroProcesso(dadosSQL.getString("expedicaoDiploma.numeroProcesso"));
		obj.getExpedicaoDiplomaVO().setVia(dadosSQL.getString("expedicaoDiploma.via"));
		obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
	}

	private String getValorImprimirEtiqueta(MatriculaPeriodoVO matriculaPeriodoVO, TagEtiquetaEnum tag) {
		switch (tag) {
        case TAG_LABEL_FIXO1:
			return "";
        case TAG_LABEL_FIXO2:
			return "";  
        case TAG_LABEL_FIXO3:
			return "";  
		case VIA2_DIPLOMA:
			if (!matriculaPeriodoVO.getExpedicaoDiplomaVO().getVia().equals("") && !matriculaPeriodoVO.getExpedicaoDiplomaVO().getVia().equals("1")) {
				return "2ª Via";
			}
			return "";  
        case NUMERO_PROCESSO_DIPLOMA:
			return matriculaPeriodoVO.getExpedicaoDiplomaVO().getNumeroProcesso();                    
		case NUMERO_REGISTRO_DIPLOMA:
        	return matriculaPeriodoVO.getExpedicaoDiplomaVO().getNumeroRegistroDiploma();                    
		case DATA_EMISSAO_DIPLOMA:
			return Uteis.getDataCidadeDiaMesPorExtensoEAno("", matriculaPeriodoVO.getExpedicaoDiplomaVO().getDataExpedicao(), true);                    
        case CIDADE_UNIDADE_ENSINO:
			return matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCidade().getNome().toUpperCase();
        case ESTADO_UNIDADE_ENSINO:
			return matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCidade().getEstado().getNome().toUpperCase();
		case MATRICULA:
			return matriculaPeriodoVO.getMatriculaVO().getMatricula();
		case PERIODO_LETIVO:
			return matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo().toString() + "º Período";
		case CPF_PESSOA:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getCPF();
		case NOME_CURSO:
			return matriculaPeriodoVO.getMatriculaVO().getCurso().getNome();
		case NOME_PESSOA:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getNome();
		case NOME_TURNO:
			return matriculaPeriodoVO.getMatriculaVO().getTurno().getNome();
		case NOME_UNIDADE_ENSINO:
			return matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getNome();
		case RG_PESSOA:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getRG();
		case ENDERECO:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getEndereco();
		case BAIRRO:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getSetor();
		case CEP:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getCEP();
		case COMPLEMENTO:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getComplemento();
		case TELEFONE:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getTelefoneRes();
		case CELULAR:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getCelular();
		case CIDADE:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getCidade().getNome();
		case ESTADO:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getCidade().getEstado().getSigla();
		case NUMERO:
			return matriculaPeriodoVO.getMatriculaVO().getAluno().getNumero();
		case TURMA:
			return matriculaPeriodoVO.getTurma().getIdentificadorTurma();
		default:
			return "";
		}
	}
	
}
