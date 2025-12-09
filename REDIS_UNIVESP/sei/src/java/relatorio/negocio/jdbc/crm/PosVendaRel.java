package relatorio.negocio.jdbc.crm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import relatorio.negocio.comuns.crm.PosVendaPresencaRelVO;
import relatorio.negocio.comuns.crm.PosVendaRelVO;
import relatorio.negocio.interfaces.crm.PosVendaRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author PedroOtimize
 */
@Repository
@Scope("singleton")
@Lazy
public class PosVendaRel extends SuperRelatorio implements PosVendaRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5691990280964511431L;

	private void validarDados(PosVendaRelVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTurmaVO()), "O campo Turma (Relatório de Pós-Vendas) deve ser informado.");

	}

	@Override
	public List<PosVendaRelVO> criarObjeto(PosVendaRelVO filotrPosVendaRelVO, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuario) {
		validarDados(filotrPosVendaRelVO);
		return consultaDadosPosVenda(filotrPosVendaRelVO, filtroAcademicoVO, usuario);
	}

	private List<PosVendaRelVO> consultaDadosPosVenda(PosVendaRelVO filotrPosVendaRelVO, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsulta();
		sqlStr.append(" from matricula");
		sqlStr.append(" INNER JOIN curso ON matricula.curso = curso.codigo");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula  = matricula.matricula");
		sqlStr.append(" INNER JOIN Turma ON turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" INNER JOIN condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso");
		sqlStr.append(" INNER JOIN matriculaperiodovencimento on matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo");
		sqlStr.append(" INNER JOIN pessoa ON matricula.aluno = pessoa.codigo");
		sqlStr.append(" LEFT  JOIN funcionario on funcionario.codigo = matricula.consultor");
		sqlStr.append(" LEFT JOIN pessoa as pessoafunc on pessoafunc.codigo = funcionario.pessoa");
		sqlStr.append(" LEFT JOIN documetacaomatricula on documetacaomatricula.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento");
		sqlStr.append(" INNER JOIN historico on historico.matriculaperiodo = matriculaperiodo.codigo");
		if(Uteis.isAtributoPreenchido(filotrPosVendaRelVO.getQuantidadeDisciplinasConcluidas())){
			sqlStr.append(" and (historico.situacao in ('AA', 'CC', 'CH', 'IS', 'AB') or historico.disciplina in (");
			sqlStr.append(" select disciplina from (");
			sqlStr.append(" select HorarioTurmadiaitem.disciplina, min(HorarioTurmadiaitem.data) as menorData from HorarioTurma");
			sqlStr.append(" inner join HorarioTurmadia on HorarioTurmadia.HorarioTurma = HorarioTurma.codigo");
			sqlStr.append(" inner join HorarioTurmadiaitem on HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo");
			sqlStr.append(" where HorarioTurma.turma = turma.codigo");
			sqlStr.append(" group by HorarioTurmadiaitem.disciplina");
			sqlStr.append(" order by menorData");
			sqlStr.append(" limit ").append(filotrPosVendaRelVO.getQuantidadeDisciplinasConcluidas());
			sqlStr.append(" ) as t");
			sqlStr.append(" ))");	
		}
		sqlStr.append(" WHERE Turma.codigo = ").append(filotrPosVendaRelVO.getTurmaVO().getCodigo());
	
		sqlStr.append(" and matriculaperiodovencimento.parcela like('1/%') and matriculaperiodovencimento.tipoorigemmatriculaperiodovencimento  = 'MENSALIDADE'");
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));

		if (!filotrPosVendaRelVO.isTrazerAlunoTransferencia()) {
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}
		sqlStr.append(getSQLGroupByPadraoConsulta());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, filotrPosVendaRelVO);
	}

	private StringBuilder getSQLPadraoConsulta() {
		StringBuilder sqlStr = new StringBuilder(" select ");
		sqlStr.append(" matricula.matricula, ");
		sqlStr.append(" pessoa.nome as nomeAluno, ");
		sqlStr.append(" case when pessoa.email is not null then pessoa.email else pessoa.email2 end as emailAluno, ");
		sqlStr.append(" pessoa.celular, ");
		sqlStr.append(" case when pessoa.telefonecomer is not null then pessoa.telefonecomer ");
		sqlStr.append(" when pessoa.telefonerecado is not null then pessoa.telefonerecado ");
		sqlStr.append(" when pessoa.telefoneres is not null then pessoa.telefoneres ");
		sqlStr.append(" else '' end as telefonefixoAluno, ");

		sqlStr.append(" condicaopagamentoplanofinanceirocurso.nrparcelasperiodo as quantidadeParcelas, ");
		sqlStr.append(" matriculaperiodovencimento.valor, ");
		sqlStr.append(" (matriculaperiodovencimento.valor - matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos) as desconto, ");
		sqlStr.append(" matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos , ");

		sqlStr.append(" pessoafunc.nome as consultor, ");

		sqlStr.append(" tipodocumento.nome as nomeDocumento, ");
		sqlStr.append(" documetacaomatricula.situacao as situacaoDocumento, ");
		sqlStr.append(" historico.disciplina, ");
		sqlStr.append(" case when historico.situacao = 'RF' then 'F' else ' ' end as presenca ");
		sqlStr.append("  ");
		return sqlStr;
	}

	private StringBuilder getSQLGroupByPadraoConsulta() {
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" group by Pessoa.nome ,pessoa.email , pessoa.email2,");
		sqlStr.append(" pessoa.celular,pessoa.telefonecomer,pessoa.telefonerecado,");
		sqlStr.append(" pessoa.telefoneres,");
		sqlStr.append(" pessoafunc.nome,");
		sqlStr.append(" condicaopagamentoplanofinanceirocurso.nrparcelasperiodo,");
		sqlStr.append(" matriculaperiodovencimento.valor, ");
		sqlStr.append(" matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos,");
		sqlStr.append(" tipodocumento.nome , documetacaomatricula.situacao,");
		sqlStr.append(" historico.situacao,");
		sqlStr.append(" historico.disciplina,");
		sqlStr.append(" matricula.matricula");
		sqlStr.append(" order by Pessoa.nome, historico.disciplina");
		sqlStr.append("  ");

		return sqlStr;
	}

	private List<PosVendaRelVO> montarDadosConsulta(SqlRowSet dadosSQL, PosVendaRelVO filotrPosVendaRelVO) {
		List<PosVendaRelVO> listaPosVendaRelVOs = new ArrayList<>(0);
		while (dadosSQL.next()) {
			PosVendaRelVO obj = consultarPosVendaRelVO(dadosSQL.getString("matricula"), listaPosVendaRelVOs);
			if (!Uteis.isAtributoPreenchido(obj.getMatricula())) {
				obj.setMatricula(dadosSQL.getString("matricula"));
				obj.setNomeAluno(dadosSQL.getString("nomeAluno"));
				obj.setEmailAluno(dadosSQL.getString("emailAluno"));
				obj.setCelularAluno(dadosSQL.getString("celular"));
				obj.setTelefoneFixoAluno(dadosSQL.getString("telefonefixoAluno"));
				obj.setQuantidadeParcelas(dadosSQL.getInt("quantidadeParcelas"));
				obj.setValorPrimeiraMensalidade(dadosSQL.getDouble("valor"));
				obj.setDescontoPrimeiraMensalidade(dadosSQL.getDouble("desconto"));
				obj.setValorFinalPrimeiraMensalidade(dadosSQL.getDouble("valordescontocalculadoprimeirafaixadescontos"));
				obj.getConsultor().getPessoa().setNome(dadosSQL.getString("consultor"));
			}
			DocumetacaoMatriculaVO dm = consultarDocumetacaoMatriculaVO(dadosSQL.getString("nomeDocumento"), obj);
			if (!Uteis.isAtributoPreenchido(dm.getTipoDeDocumentoVO().getNome())) {
				dm.getTipoDeDocumentoVO().setNome(dadosSQL.getString("nomeDocumento"));
				dm.setSituacao(dadosSQL.getString("situacaoDocumento"));
			}
			adicionarDocumetacaoMatriculaVO(dm, filotrPosVendaRelVO);//Montar Dados Para o Cabecalho
			adicionarDocumetacaoMatriculaVO(dm, obj);

			if (Uteis.isAtributoPreenchido(dadosSQL.getString("disciplina"))) {
				PosVendaPresencaRelVO pvp = consultarPosVendaPresencaRelVO(dadosSQL.getString("disciplina"), obj);
				if (!Uteis.isAtributoPreenchido(pvp.getDisciplina())) {
					pvp.setDisciplina(dadosSQL.getString("disciplina"));
					pvp.setPresenca(dadosSQL.getString("presenca"));
				}
				adicionarPosVendaPresencaRelVO(pvp, filotrPosVendaRelVO);//Montar Dados Para o Cabecalho
				adicionarPosVendaPresencaRelVO(pvp, obj);
			}
			adicionarPosVendaRelVO(listaPosVendaRelVOs, obj);
		}
		return listaPosVendaRelVOs;
	}

	private PosVendaRelVO consultarPosVendaRelVO(String matricula, List<PosVendaRelVO> listaPosVendaRelVOs) {
		Optional<PosVendaRelVO> findFirst = listaPosVendaRelVOs.stream().filter(p -> p.getMatricula().equals(matricula)).findFirst();
		if (findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getMatricula())) {
			return findFirst.get();
		}
		return new PosVendaRelVO();
	}

	private void adicionarPosVendaRelVO(List<PosVendaRelVO> listaPosVendaRelVOs, PosVendaRelVO obj) {
		int index = 0;
		for (PosVendaRelVO objsExistente : listaPosVendaRelVOs) {
			if (objsExistente.getMatricula().equals(obj.getMatricula())) {
				listaPosVendaRelVOs.set(index, obj);
				return;
			}
			index++;
		}
		listaPosVendaRelVOs.add(obj);
	}

	private DocumetacaoMatriculaVO consultarDocumetacaoMatriculaVO(String nomeDocumento, PosVendaRelVO obj) {
		Optional<DocumetacaoMatriculaVO> findFirst = obj.getListaDocumetacaoMatriculaVOs().stream().filter(p -> p.getTipoDeDocumentoVO().getNome().equals(nomeDocumento)).findFirst();
		if (findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getTipoDeDocumentoVO().getNome())) {
			return findFirst.get();
		}
		return new DocumetacaoMatriculaVO();
	}

	private void adicionarDocumetacaoMatriculaVO(DocumetacaoMatriculaVO dm, PosVendaRelVO obj) {
		int index = 0;
		for (DocumetacaoMatriculaVO objsExistente : obj.getListaDocumetacaoMatriculaVOs()) {
			if (objsExistente.getTipoDeDocumentoVO().getNome().equals(dm.getTipoDeDocumentoVO().getNome())) {
				obj.getListaDocumetacaoMatriculaVOs().set(index, dm);
				return;
			}
			index++;
		}
		obj.getListaDocumetacaoMatriculaVOs().add(dm);
	}

	private PosVendaPresencaRelVO consultarPosVendaPresencaRelVO(String nomeDisciplina, PosVendaRelVO obj) {
		Optional<PosVendaPresencaRelVO> findFirst = obj.getListaPosVendaPresencaVOs().stream().filter(p -> p.getDisciplina().equals(nomeDisciplina)).findFirst();
		if (findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getDisciplina())) {
			return findFirst.get();
		}
		return new PosVendaPresencaRelVO();
	}

	private void adicionarPosVendaPresencaRelVO(PosVendaPresencaRelVO pvp, PosVendaRelVO obj) {
		int index = 0;
		for (PosVendaPresencaRelVO objsExistente : obj.getListaPosVendaPresencaVOs()) {
			if (objsExistente.getDisciplina().equals(pvp.getDisciplina())) {
				obj.getListaPosVendaPresencaVOs().set(index, pvp);
				return;
			}
			index++;
		}
		obj.getListaPosVendaPresencaVOs().add(pvp);
	}

	@Override
	public void montarExcel(List<PosVendaRelVO> listaPosVendaRelVOs, PosVendaRelVO filotrPosVendaRelVO, File arquivo) {
		try {
			Workbook wb = new HSSFWorkbook();
			Sheet guia = wb.createSheet("Relatório de Pós-Vendas");
			Row linha = null;
			Cell coluna = null;
			montarCabecalho(wb, guia, linha, coluna, filotrPosVendaRelVO);
			montarItens(listaPosVendaRelVOs, filotrPosVendaRelVO, guia, linha, coluna);
			FileOutputStream out = new FileOutputStream(arquivo);
			wb.write(out);
			out.close();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	private void montarItens(List<PosVendaRelVO> listaPosVendaRelVOs, PosVendaRelVO filotrPosVendaRelVO, Sheet guia, Row linha, Cell coluna) {
		int numeroLinha = 2;
		for (PosVendaRelVO obj : listaPosVendaRelVOs) {
			int numeroColuna = 0;
			linha = guia.createRow(numeroLinha);
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getNomeAluno());
			if (!filotrPosVendaRelVO.getListaPosVendaPresencaVOs().isEmpty()) {
				for (PosVendaPresencaRelVO pvpCabecalho : filotrPosVendaRelVO.getListaPosVendaPresencaVOs()) {
					Optional<PosVendaPresencaRelVO> findFirst = obj.getListaPosVendaPresencaVOs().stream().filter(p-> p.getDisciplina().equals(pvpCabecalho.getDisciplina())).findFirst();
					if (findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getDisciplina())) {
						UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, findFirst.get().getPresenca());
					}else{
						UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, "");
					}	
				}
			} else {
				UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, "");
			}
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getEmailAluno());
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, "");
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getCelularAluno());
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getTelefoneFixoAluno());
			if (!filotrPosVendaRelVO.getListaDocumetacaoMatriculaVOs().isEmpty()) {
				for (DocumetacaoMatriculaVO dmCabecalho : filotrPosVendaRelVO.getListaDocumetacaoMatriculaVOs()) {
					Optional<DocumetacaoMatriculaVO> findFirst = obj.getListaDocumetacaoMatriculaVOs().stream().filter(p-> p.getTipoDeDocumentoVO().getNome().equals(dmCabecalho.getTipoDeDocumentoVO().getNome())).findFirst();
					if (findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get().getTipoDeDocumentoVO().getNome())) {
						UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, findFirst.get().getSituacao_Apresentar());
					}else{
						UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, "");
					}
				}
			} else {
				UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, "");
			}
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, "");
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, "");
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getQuantidadeParcelas());
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getValorPrimeiraMensalidade());
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getDescontoPrimeiraMensalidade());
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getValorFinalPrimeiraMensalidade());
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, obj.getConsultor().getPessoa().getNome());
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, null, "");
			numeroLinha++;
		}
	}

	private void montarCabecalho(Workbook wb, Sheet guia, Row linha, Cell coluna, PosVendaRelVO filotrPosVendaRelVO) {		
		// Set Header Font
		HSSFFont headerFont = (HSSFFont) wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 10);

		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setFont(headerFont);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);

		int numeroColuna = 0;
		linha = guia.createRow(0);
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, filotrPosVendaRelVO.getTurmaVO().getIdentificadorTurma());
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Presença");
		if (!filotrPosVendaRelVO.getListaPosVendaPresencaVOs().isEmpty()) {
			int colunaInicial = numeroColuna - 1;
			for (int i = 1; i < filotrPosVendaRelVO.getListaPosVendaPresencaVOs().size(); i++) {// i começa em 1 pois o campo Presença ja conta como zero
				UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
			}
			guia.addMergedRegion(new CellRangeAddress(0, 0, colunaInicial, numeroColuna - 1));
		}
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "E-mail");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Enviar E-mail");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Telefones");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		guia.addMergedRegion(new CellRangeAddress(0, 0, numeroColuna - 2, numeroColuna - 1));

		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Documentação");
		if (!filotrPosVendaRelVO.getListaDocumetacaoMatriculaVOs().isEmpty()) {
			int colunaInicial = numeroColuna - 1;
			for (int i = 1; i < filotrPosVendaRelVO.getListaDocumetacaoMatriculaVOs().size(); i++) {// i começa em 1 pois o campo Documentação ja conta como zero
				UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
			}
			guia.addMergedRegion(new CellRangeAddress(0, 0, colunaInicial, numeroColuna - 1));
		}

		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Portal do Aluno");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		guia.addMergedRegion(new CellRangeAddress(0, 0, numeroColuna - 2, numeroColuna - 1));

		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Plano Financeiro");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		guia.addMergedRegion(new CellRangeAddress(0, 0, numeroColuna - 4, numeroColuna - 1));

		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Avaliação da consultoria de vendas");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		guia.addMergedRegion(new CellRangeAddress(0, 0, numeroColuna - 2, numeroColuna - 1));

		linha = guia.createRow(1);
		numeroColuna = 0;
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		guia.addMergedRegion(new CellRangeAddress(0, 1, numeroColuna - 1, numeroColuna - 1));
		if (!filotrPosVendaRelVO.getListaPosVendaPresencaVOs().isEmpty()) {
			for (int i = 0; i < filotrPosVendaRelVO.getListaPosVendaPresencaVOs().size(); i++) {
				UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, (i + 1 + "º"));
			}
		} else {
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
			guia.addMergedRegion(new CellRangeAddress(0, 1, numeroColuna - 1, numeroColuna - 1));
		}
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		guia.addMergedRegion(new CellRangeAddress(0, 1, numeroColuna - 1, numeroColuna - 1));
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
		guia.addMergedRegion(new CellRangeAddress(0, 1, numeroColuna - 1, numeroColuna - 1));
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Celular");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Fixo");
		if (!filotrPosVendaRelVO.getListaDocumetacaoMatriculaVOs().isEmpty()) {
			for (DocumetacaoMatriculaVO dm : filotrPosVendaRelVO.getListaDocumetacaoMatriculaVOs()) {
				UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, dm.getTipoDeDocumentoVO().getNome());
			}
		} else {
			UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "");
			guia.addMergedRegion(new CellRangeAddress(0, 1, numeroColuna - 1, numeroColuna - 1));
		}
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Reposição");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Declaração");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Qtd");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Valor");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Desconto");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Final");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Consultor");
		UteisTexto.montarColunaExcelDinamicamente(linha, coluna, numeroColuna++, headerStyle, "Nota");
	}

}
