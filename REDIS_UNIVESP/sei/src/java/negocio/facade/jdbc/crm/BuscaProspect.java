/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.BuscaProspectVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.BuscaProspectInterfaceFacade;

/**
 * 
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class BuscaProspect extends ControleAcesso implements BuscaProspectInterfaceFacade {

	private static String idEntidade;
	private HashMap<Integer, Object> prospectsInteracoesListadas;
	private List<String> prospectsEmailsListados;

	public static String getIdEntidade() {
		return idEntidade;
	}

	public void setIdEntidade(String aIdEntidade) {
		idEntidade = aIdEntidade;
	}

	public BuscaProspect() throws Exception {
		super();
		setIdEntidade("BuscaProspect");
	}

	public List<BuscaProspectVO> consultar(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario, Integer limit, Integer offset) throws Exception {
		List<BuscaProspectVO> listaObjs = new ArrayList<BuscaProspectVO>(0);
		getProspectsInteracoesListadas().clear();
		consultarProspectCompromissoAgendado(letra, listaObjs, buscaProspectVO, usuario, limit, offset);
		// consultarProspectSemCompromissoAgendado(letra, listaObjs,
		// buscaProspectVO, usuario);
		Set<Integer> chaves = getProspectsInteracoesListadas().keySet();
		for (Integer codigoProspect : chaves) {
			listaObjs.add((BuscaProspectVO) getProspectsInteracoesListadas().get(codigoProspect));
		}
		if (buscaProspectVO.getOrdenacao().equals("data")) {
			Ordenacao.ordenarListaDecrescente(listaObjs, "dataContato");
		} else if (buscaProspectVO.getOrdenacao().equals("nomeProspect")) {
			Ordenacao.ordenarLista(listaObjs, "nomeProspect");
		} else if (buscaProspectVO.getOrdenacao().equals("dataCadastro")) {
			Ordenacao.ordenarListaDecrescente(listaObjs, "dataCadastro");
		} else {
			Ordenacao.ordenarLista(listaObjs, "nomeConsultor");
		}
		return listaObjs;
	}

	@Override
	public Integer consultarTotalRegistro(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception {
		Boolean permiteConsultarTodasUnidades = verificarPermissaoFuncionalidadeUsuario("BuscaProspect_consultarTodasUnidades", usuario);
		List<BuscaProspectVO> listaObjs = new ArrayList<BuscaProspectVO>(0);
		getProspectsInteracoesListadas().clear();
		if (!buscaProspectVO.getEmail().trim().equals("") || buscaProspectVO.getProspect().getCodigo() != 0) {
			letra = "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(DISTINCT prospects.codigo) as qtde ");
		sql.append("FROM prospects ");
		sql.append("LEFT JOIN interacaoworkflow ON ");
		sql.append(" interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect");
		if (buscaProspectVO.getTipoPeriodo().equals("dataUltimoContato")) {
			if (buscaProspectVO.getDataInicio() != null || buscaProspectVO.getDataFim() != null) {
				// sql.append(" and interacaoworkflow.codigo in ( select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect order by datainicio desc limit 1)");
			}
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and iwf.datainicio >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and iwf.datainicio <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		}
		sql.append(" order by datainicio desc limit 1) ");
		// sql.append("LEFT JOIN interacaoworkflow ON prospects.codigo = interacaoworkflow.prospect and interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow iwf where prospects.codigo = iwf.prospect order by datainicio desc limit 1) ");
		// sql.append("INNER JOIN interacaoworkflow ON prospects.codigo = interacaoworkflow.prospect ");
		sql.append("LEFT JOIN etapaWorkflow ON interacaoworkflow.etapaWorkflow = etapaWorkflow.codigo ");// and
																											// etapaWorkflow.codigo
																											// =
																											// (select
																											// compromissoagendapessoahorario.etapaWorkflow
																											// from
																											// compromissoagendapessoahorario
																											// where
																											// compromissoagendapessoahorario.codigo
																											// =
																											// interacaoworkflow.compromissoagendapessoahorario
																											// )
																											// ");
		sql.append("LEFT JOIN usuario ON interacaoworkflow.responsavel = usuario.codigo ");
		sql.append("LEFT JOIN pessoa AS pessoaResponsavel ON usuario.pessoa = pessoaResponsavel.codigo ");
//		sql.append("LEFT JOIN funcionario  ON prospects.consultorPadrao = funcionario.codigo ");
//		sql.append("LEFT JOIN pessoa AS consultorPadrao  ON consultorPadrao.codigo = funcionario.pessoa ");
		sql.append("LEFT JOIN cursointeresse ON cursointeresse.prospects = prospects.codigo ");
		if (buscaProspectVO.getCurso().getCodigo() == 0) {
			sql.append(" and cursointeresse.codigo = (select ci.codigo from cursointeresse ci where ci.prospects = prospects.codigo ");
			sql.append(" order by ci.codigo desc limit 1) ");
		}

		sql.append("LEFT JOIN curso ON (curso.codigo =  cursointeresse.curso) ");
		sql.append("LEFT JOIN curso c2 ON (c2.codigo =  interacaoworkflow.curso) ");
		sql.append("LEFT JOIN pessoa AS pessoaProspect ON pessoaProspect.codigo = prospects.pessoa ");
		sql.append("LEFT JOIN matricula ON matricula.aluno = pessoaProspect.codigo and matricula.matricula = (select m.matricula from matricula m where m.aluno = pessoaProspect.codigo and m.situacao = 'AT' order by data desc limit 1 ) ");
		sql.append("LEFT JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by codigo desc limit 1) ");
		sql.append("LEFT JOIN curso c3 ON (c3.codigo =  matricula.curso)  ");
//		sql.append("LEFT JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sql.append("LEFT JOIN situacaoprospectworkflow ON etapaWorkflow.situacaodefinirprospectfinal = situacaoprospectworkflow.codigo ");
		sql.append("LEFT JOIN situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline ");

		sql.append("WHERE (prospects.inativo != true) and prospects.nome ilike '").append(letra).append("%' ");
		if (buscaProspectVO.getTipoPeriodo().equals("dataUltimoContato")) {
			if (buscaProspectVO.getDataInicio() != null || buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.codigo in ( select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect order by datainicio desc limit 1)");
			}
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and interacaoworkflow.datainicio >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.datainicio <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		} else {
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and prospects.dataCadastro >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and prospects.dataCadastro <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		}
		if (buscaProspectVO.getProspect().getCodigo() != 0) {
			sql.append(" AND prospects.codigo = ").append(buscaProspectVO.getProspect().getCodigo());
		}
		if (buscaProspectVO.getUnidadeEnsino().getCodigo() > 0) {
			sql.append(" AND prospects.unidadeEnsino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo());
			// sql.append(" AND (interacaoworkflow.unidadeEnsino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo()).append(" or interacaoworkflow.unidadeEnsino is null)");
		} else {
			if (!permiteConsultarTodasUnidades && !usuario.getTipoUsuario().equals("DM")) {
				sql.append(" and prospects.unidadeensino in (select distinct unidadeEnsino.codigo from unidadeEnsino inner join usuarioperfilacesso on usuarioperfilacesso.unidadeensino = unidadeensino.codigo ");
				sql.append(" where usuarioperfilacesso.usuario = ").append(usuario.getCodigo());
				sql.append(" )");
			} else if (buscaProspectVO.getUnidadeEnsino().getCodigo().equals(-1)) {
				sql.append(" AND prospects.unidadeEnsino is null ");
			}
		}
		if (buscaProspectVO.getCurso().getCodigo() != 0) {
			sql.append(" AND curso.codigo = ").append(buscaProspectVO.getCurso().getCodigo());
		}
		if (buscaProspectVO.getCidade().getCodigo() != 0) {
			sql.append(" AND prospects.cidade = ").append(buscaProspectVO.getCidade().getCodigo());
		}
		if (!buscaProspectVO.getEmail().trim().equals("")) {
			sql.append(" AND (prospects.emailprincipal ilike '").append(buscaProspectVO.getEmail()).append("' or prospects.emailsecundario ilike '").append(buscaProspectVO.getEmail()).append("') ");
		}
		if (!buscaProspectVO.getTelefone().trim().equals("")) {
			sql.append(" AND (sem_caracteres_especiais(prospects.telefoneresidencial) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getTelefone()).append("%') OR sem_caracteres_especiais(prospects.telefonecomercial) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getTelefone()).append("%')");
			sql.append(" OR sem_caracteres_especiais(prospects.telefonerecado) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getTelefone()).append("%') OR sem_caracteres_especiais(prospects.celular) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getTelefone()).append("%')) ");
		}
		if (!buscaProspectVO.getCpf().trim().equals("")) {
			sql.append(" AND (sem_caracteres_especiais(prospects.cpf) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getCpf()).append("%')) ");
		}
		if (buscaProspectVO.getConsultorResponsavel().getCodigo() != 0) {
			sql.append(" AND (prospects.consultorpadrao = ").append(buscaProspectVO.getConsultorResponsavel().getCodigo()).append(" or prospects.consultorpadrao is null)");
		}
		if (buscaProspectVO.getConsultor().getPessoa().getCodigo() != 0) {
			sql.append(" AND ((pessoaResponsavel.codigo = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(") OR (pessoaResponsavel.codigo is null AND prospects.consultorpadrao = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(" ) or (pessoaResponsavel.codigo is null AND prospects.consultorpadrao is null)) ");
		} 
		if (!buscaProspectVO.getSituacaoProspect().equals("")) {
			if (buscaProspectVO.getSituacaoProspect().equals("MATRICULADO")) {
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_COM_MATRICULA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_SEM_MATRICULA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_SEM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_COM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_COM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_SEM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			}
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public String consultarEmails(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception {
		List<BuscaProspectVO> emails = consultarEmailProspectCompromissoAgendado(letra, buscaProspectVO, usuario);
		return emails.stream().map(BuscaProspectVO::getEmail).collect(Collectors.joining(";"));
	}

	@Override
	public String consultarEmailsExcel(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception {
		List<BuscaProspectVO> emails = null;
		String nomeArquivo = "";
		FileOutputStream fileOut = null;
		HSSFWorkbook workbook = null;
		HSSFSheet worksheet = null;
		try {
			emails = consultarEmailProspectCompromissoAgendado(letra, buscaProspectVO, usuario);
			if (!emails.isEmpty()) {
				nomeArquivo = "EMAIL_PROSPECTS_" + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + ".xls";
				fileOut = new FileOutputStream(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
				workbook = new HSSFWorkbook();
				worksheet = workbook.createSheet("Prospects");
				HSSFCellStyle styleOfHeader = worksheet.getWorkbook().createCellStyle();
				styleOfHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
				styleOfHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				styleOfHeader.setFillPattern(HSSFCellStyle.BORDER_THIN);
				styleOfHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				styleOfHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
				styleOfHeader.setBottomBorderColor(HSSFColor.BLACK.index);
				styleOfHeader.setLeftBorderColor(HSSFColor.BLACK.index);
				styleOfHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

				HSSFCellStyle styleOfBody = worksheet.getWorkbook().createCellStyle();
				styleOfBody.setFillForegroundColor(HSSFColor.WHITE.index);
				styleOfBody.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				styleOfBody.setFillPattern(HSSFCellStyle.BORDER_THIN);
				styleOfBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				styleOfBody.setBorderRight(HSSFCellStyle.BORDER_THIN);
				styleOfBody.setBottomBorderColor(HSSFColor.BLACK.index);
				styleOfBody.setLeftBorderColor(HSSFColor.BLACK.index);
				styleOfBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				HSSFRow linha = null;
				HSSFCell coluna = null;

				linha = worksheet.createRow(0);

				coluna = linha.createCell(0);
				coluna.setCellValue("Nome");
				coluna.setCellStyle(styleOfHeader);

				coluna = linha.createCell(1);
				coluna.setCellValue("Email");
				coluna.setCellStyle(styleOfHeader);

				coluna = linha.createCell(2);
				coluna.setCellValue("Telefone Comercial");
				coluna.setCellStyle(styleOfHeader);

				coluna = linha.createCell(3);
				coluna.setCellValue("Telefone Residencial");
				coluna.setCellStyle(styleOfHeader);

				coluna = linha.createCell(4);
				coluna.setCellValue("Celular");
				coluna.setCellStyle(styleOfHeader);
				int x = 1;
				for (BuscaProspectVO buscaProspectVO2 : emails) {
					linha = worksheet.createRow(x++);
					coluna = linha.createCell(0);

					coluna.setCellValue(buscaProspectVO2.getNomeProspect());
					coluna.setCellStyle(styleOfBody);

					coluna = linha.createCell(1);
					coluna.setCellValue(buscaProspectVO2.getEmail());
					coluna.setCellStyle(styleOfBody);

					coluna = linha.createCell(2);
					coluna.setCellValue(buscaProspectVO2.getTelefoneComercial());
					coluna.setCellStyle(styleOfBody);

					coluna = linha.createCell(3);
					coluna.setCellValue(buscaProspectVO2.getTelefoneResidencial());
					coluna.setCellStyle(styleOfBody);

					coluna = linha.createCell(4);
					coluna.setCellValue(buscaProspectVO2.getCelular());
					coluna.setCellStyle(styleOfBody);
				}
				worksheet.setColumnWidth(0, 20000);
				worksheet.setColumnWidth(1, 20000);
				worksheet.setColumnWidth(2, 10000);
				worksheet.setColumnWidth(3, 10000);
				worksheet.setColumnWidth(4, 10000);
				workbook.write(fileOut);
				return nomeArquivo;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
			fileOut = null;
			workbook = null;
			worksheet = null;
			if (emails != null) {
				emails.clear();
				emails = null;
			}
		}
		return nomeArquivo;

	}

	private List<BuscaProspectVO> consultarProspectCompromissoAgendado(String letra, List<BuscaProspectVO> listaObjs, BuscaProspectVO buscaProspectVO, UsuarioVO usuario, Integer limit, Integer offset) throws Exception {
		Boolean permiteConsultarTodasUnidades = verificarPermissaoFuncionalidadeUsuario("BuscaProspect_consultarTodasUnidades", usuario);
		if (!buscaProspectVO.getEmail().trim().equals("") || buscaProspectVO.getProspect().getCodigo() != 0) {
			letra = "";
		}
		List<Object> filtros = new ArrayList<>();
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT distinct prospects.nome AS nomeProspect, prospects.codigo AS codigoProspect, prospects.dataCadastro AS dataCadastro, prospects.telefoneResidencial AS telRes,");
		sql.append(" prospects.telefoneComercial AS telComer, prospects.celular AS cel, prospects.telefoneRecado AS telRec, prospects.emailPrincipal AS email,");
		sql.append(" interacaoworkflow.datainicio AS dataContato,interacaoworkflow.observacao AS observacao,");
		sql.append(" situacaoprospectpipeline.controle AS controleSituacaoProspectPipeline,");
		
		sql.append(" (select turma.identificadorturma from turma where turma.codigo = matriculaperiodo.turma) AS turma, ");
		
		sql.append(" curso.nome AS nomeCurso, curso.codigo AS codigoCurso,");
		
		sql.append(" (select pessoa.nome from pessoa ");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo and funcionario.codigo = prospects.consultorPadrao ");
		sql.append(" where pessoa.codigo = funcionario.pessoa ");
		sql.append(" ) AS nomeConsultorPadrao, ");
		
		sql.append(" pessoaResponsavel.nome AS nomeResponsavel,");
		sql.append(" matricula.matricula, c3.nome as cursoMatriculado, ");

		sql.append(" (select count(matricula.matricula) from matricula ");
		sql.append(" where aluno = pessoaProspect.codigo ");
		sql.append(" and matricula.situacao = 'AT') > 0 as possuiMatricula ");

		sql.append(" , sincronizadoRdStation ");
		
		sql.append("FROM prospects ");
		sql.append("LEFT JOIN interacaoworkflow ON ");
		sql.append(" interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect");
		if (buscaProspectVO.getTipoPeriodo().equals("dataUltimoContato")) {
			if (buscaProspectVO.getDataInicio() != null || buscaProspectVO.getDataFim() != null) {
			}
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and iwf.datainicio >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and iwf.datainicio <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		}
		sql.append(" order by datainicio desc limit 1) ");
		sql.append("LEFT JOIN etapaWorkflow ON interacaoworkflow.etapaWorkflow = etapaWorkflow.codigo ");// and
																											// etapaWorkflow.codigo
																											// =
																											// (select
																											// compromissoagendapessoahorario.etapaWorkflow
																											// from
																											// compromissoagendapessoahorario
																											// where
																											// compromissoagendapessoahorario.codigo
																											// =
																											// interacaoworkflow.compromissoagendapessoahorario
																											// )
																											// ");
		sql.append("LEFT JOIN usuario ON interacaoworkflow.responsavel = usuario.codigo ");
		sql.append("LEFT JOIN pessoa AS pessoaResponsavel ON usuario.pessoa = pessoaResponsavel.codigo ");
		sql.append("LEFT JOIN cursointeresse ON cursointeresse.prospects = prospects.codigo ");
		if (buscaProspectVO.getCurso().getCodigo() == 0) {
			sql.append(" and cursointeresse.codigo = (select ci.codigo from cursointeresse ci where ci.prospects = prospects.codigo ");
			sql.append(" order by ci.codigo desc limit 1) ");
		}

		sql.append("LEFT JOIN curso ON (curso.codigo =  cursointeresse.curso) ");
		sql.append("LEFT JOIN curso c2 ON (c2.codigo =  interacaoworkflow.curso) ");
		sql.append("LEFT JOIN pessoa AS pessoaProspect ON pessoaProspect.codigo = prospects.pessoa ");
		sql.append("LEFT JOIN matricula ON matricula.aluno = pessoaProspect.codigo and matricula.matricula = (select m.matricula from matricula m where m.aluno = pessoaProspect.codigo and m.situacao = 'AT' order by data desc limit 1 ) ");
		sql.append("LEFT JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by codigo desc limit 1) ");
		sql.append("LEFT JOIN curso c3 ON (c3.codigo =  matricula.curso)  ");
		sql.append("LEFT JOIN situacaoprospectworkflow ON etapaWorkflow.situacaodefinirprospectfinal = situacaoprospectworkflow.codigo ");
		sql.append("LEFT JOIN situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline ");

		sql.append("WHERE (prospects.inativo != true) and prospects.nome ilike '").append(letra).append("%' ");
		if (buscaProspectVO.getTipoPeriodo().equals("dataUltimoContato")) {
			if (buscaProspectVO.getDataInicio() != null || buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.codigo in ( select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect order by datainicio desc limit 1)");
			}
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and interacaoworkflow.datainicio >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.datainicio <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		} else {
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and prospects.dataCadastro >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and prospects.dataCadastro <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		}
		if (buscaProspectVO.getProspect().getCodigo() != 0) {
			sql.append(" AND prospects.codigo = ? ");
			filtros.add(buscaProspectVO.getProspect().getCodigo());
		}
		if (buscaProspectVO.getUnidadeEnsino().getCodigo() > 0) {
			sql.append(" AND prospects.unidadeEnsino = ? ");
			filtros.add(buscaProspectVO.getUnidadeEnsino().getCodigo());
		} else {
			if (!permiteConsultarTodasUnidades && !usuario.getTipoUsuario().equals("DM")) {
				sql.append(" and prospects.unidadeensino in (select distinct unidadeEnsino.codigo from unidadeEnsino inner join usuarioperfilacesso on usuarioperfilacesso.unidadeensino = unidadeensino.codigo ");
				sql.append(" where usuarioperfilacesso.usuario = ? ");
				sql.append(" )");
				filtros.add(usuario.getCodigo());
			} else if (buscaProspectVO.getUnidadeEnsino().getCodigo().equals(-1)) {
				sql.append(" AND prospects.unidadeEnsino is null ");
			}
		}
		if (buscaProspectVO.getCurso().getCodigo() != 0) {
			sql.append(" AND curso.codigo = ?");
			filtros.add(buscaProspectVO.getCurso().getCodigo());
		}
		if (buscaProspectVO.getCidade().getCodigo() != 0) {
			sql.append(" AND prospects.cidade = ?");
			filtros.add(buscaProspectVO.getCidade().getCodigo());
		}
		if (!buscaProspectVO.getEmail().trim().equals("")) {
			sql.append(" AND (prospects.emailprincipal ilike ? or prospects.emailsecundario ilike ?) ");
			filtros.add(buscaProspectVO.getEmail());
			filtros.add(buscaProspectVO.getEmail());
		}
		if (!buscaProspectVO.getTelefone().trim().equals("")) {
			sql.append(" AND (sem_caracteres_especiais(prospects.telefoneresidencial) ilike sem_caracteres_especiais(?)  OR sem_caracteres_especiais(prospects.telefonecomercial) ilike sem_caracteres_especiais (?) ");
			sql.append(" OR sem_caracteres_especiais(prospects.telefonerecado) ilike sem_caracteres_especiais(?) OR sem_caracteres_especiais(prospects.celular) ilike sem_caracteres_especiais(?)) ");
			filtros.add(buscaProspectVO.getTelefone());
			filtros.add(buscaProspectVO.getTelefone());
			filtros.add(buscaProspectVO.getTelefone());
			filtros.add(buscaProspectVO.getTelefone());
		}
		if (!buscaProspectVO.getCpf().trim().equals("")) {
			sql.append(" AND (sem_caracteres_especiais(prospects.cpf) ilike sem_caracteres_especiais(?)) ");
			filtros.add(buscaProspectVO.getCpf());
		}
		if (buscaProspectVO.getConsultorResponsavel().getCodigo() != 0) {
			sql.append(" AND (prospects.consultorpadrao = ?  or prospects.consultorpadrao is null)");
			filtros.add(buscaProspectVO.getConsultorResponsavel().getCodigo());
		}
		if (buscaProspectVO.getConsultor().getPessoa().getCodigo() != 0) {
			sql.append(" AND ((pessoaResponsavel.codigo =  ? OR (pessoaResponsavel.codigo is null AND prospects.consultorpadrao = ? or (pessoaResponsavel.codigo is null AND prospects.consultorpadrao is null)) ");
			filtros.add(buscaProspectVO.getConsultor().getPessoa().getCodigo());
			filtros.add(buscaProspectVO.getConsultor().getPessoa().getCodigo());
		} 
		if (!buscaProspectVO.getSituacaoProspect().equals("")) {
			if (buscaProspectVO.getSituacaoProspect().equals("MATRICULADO")) {
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_COM_MATRICULA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_SEM_MATRICULA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_SEM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_COM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_COM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_SEM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			}
		}
		if (buscaProspectVO.getOrdenacao().equals("data")) {
			sql.append(" order by interacaoworkflow.datainicio desc, prospects.nome, pessoaResponsavel.nome ");
		} else if (buscaProspectVO.getOrdenacao().equals("nomeProspect")) {
			sql.append(" order by prospects.nome, interacaoworkflow.datainicio desc, pessoaResponsavel.nome ");
		} else if (buscaProspectVO.getOrdenacao().equals("dataCadastro")) {
			sql.append(" order by prospects.dataCadastro, pessoaResponsavel.nome, interacaoworkflow.datainicio desc, prospects.nome ");
		} else {
			sql.append(" order by pessoaResponsavel.nome, interacaoworkflow.datainicio desc, prospects.nome ");
		}
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());	
		
		return (montarDados(listaObjs, buscaProspectVO.getSituacaoProspect(), tabelaResultado, usuario));
	}

	public List<BuscaProspectVO> consultarEmailSuggestionBox(String info,UsuarioVO usuario) throws Exception{
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT emailprincipal as email FROM prospects WHERE emailprincipal like '");
			sql.append("%");
			sql.append(info);
			sql.append("%'");
			sql.append(" union all");
			sql.append(" select emailsecundario as email from prospects WHERE emailsecundario like '");
			sql.append("%");
			sql.append(info);
			sql.append("%'");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return montarDadosEmailSuggestion(tabelaResultado);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<BuscaProspectVO> consultarTelefoneSuggestionBox(String info,UsuarioVO usuario) throws Exception{
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select telefoneresidencial,telefonecomercial,telefonerecado,celular ");
			sql.append("from prospects where 1=1 ");
			sql.append(" AND (prospects.telefoneresidencial ilike '");
			sql.append(info).append("%'");
			sql.append(" OR prospects.telefonecomercial ilike '");
			sql.append(info).append("%'");
			sql.append(" OR prospects.telefonerecado ilike ' ");
			sql.append(info).append("%'");
			sql.append(" OR prospects.celular ilike '");
			sql.append(info).append("%')");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return montarDadosTelefoneSuggestion(tabelaResultado);
		} catch (Exception e) {
			throw e;
		}
	}

	private List<BuscaProspectVO> consultarEmailProspectCompromissoAgendado(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception {
		if (!buscaProspectVO.getEmail().trim().equals("") || buscaProspectVO.getProspect().getCodigo() != 0) {
			letra = "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT prospects.emailprincipal AS email, prospects.nome as nome, prospects.telefoneresidencial as telefoneresidencial, prospects.telefonecomercial as telefonecomercial, prospects.celular as celular ");
		sql.append("FROM prospects ");
		sql.append("LEFT JOIN interacaoworkflow ON ");
		sql.append(" interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect");
		if (buscaProspectVO.getTipoPeriodo().equals("dataUltimoContato")) {
			if (buscaProspectVO.getDataInicio() != null || buscaProspectVO.getDataFim() != null) {
				// sql.append(" and interacaoworkflow.codigo in ( select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect order by datainicio desc limit 1)");
			}
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and iwf.datainicio >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and iwf.datainicio <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		}
		sql.append(" order by datainicio desc limit 1) ");
		// sql.append("LEFT JOIN interacaoworkflow ON prospects.codigo = interacaoworkflow.prospect and interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow iwf where prospects.codigo = iwf.prospect order by datainicio desc limit 1) ");
		// sql.append("INNER JOIN interacaoworkflow ON prospects.codigo = interacaoworkflow.prospect ");
		sql.append("LEFT JOIN etapaWorkflow ON interacaoworkflow.etapaWorkflow = etapaWorkflow.codigo ");// and
																											// etapaWorkflow.codigo
																											// =
																											// (select
																											// compromissoagendapessoahorario.etapaWorkflow
																											// from
																											// compromissoagendapessoahorario
																											// where
																											// compromissoagendapessoahorario.codigo
																											// =
																											// interacaoworkflow.compromissoagendapessoahorario
																											// )
																											// ");
		sql.append("LEFT JOIN usuario ON interacaoworkflow.responsavel = usuario.codigo ");
		sql.append("LEFT JOIN pessoa AS pessoaResponsavel ON usuario.pessoa = pessoaResponsavel.codigo ");
		sql.append("LEFT JOIN funcionario  ON prospects.consultorPadrao = funcionario.codigo ");
		sql.append("LEFT JOIN pessoa AS consultorPadrao  ON consultorPadrao.codigo = funcionario.pessoa ");
		sql.append("LEFT JOIN cursointeresse ON cursointeresse.prospects = prospects.codigo ");
		if (buscaProspectVO.getCurso().getCodigo() == 0) {
			sql.append(" and cursointeresse.codigo = (select ci.codigo from cursointeresse ci where ci.prospects = prospects.codigo ");
			sql.append(" order by ci.codigo desc limit 1) ");
		}

		sql.append("LEFT JOIN curso ON (curso.codigo =  cursointeresse.curso) ");
		sql.append("LEFT JOIN curso c2 ON (c2.codigo =  interacaoworkflow.curso) ");
		sql.append("LEFT JOIN pessoa AS pessoaProspect ON pessoaProspect.codigo = prospects.pessoa ");
		sql.append("LEFT JOIN matricula ON matricula.aluno = pessoaProspect.codigo and matricula.matricula = (select m.matricula from matricula m where m.aluno = pessoaProspect.codigo and m.situacao = 'AT' order by data desc limit 1 ) ");
		sql.append("LEFT JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by codigo desc limit 1) ");
		sql.append("LEFT JOIN curso c3 ON (c3.codigo =  matricula.curso)  ");
		sql.append("LEFT JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sql.append("LEFT JOIN situacaoprospectworkflow ON etapaWorkflow.situacaodefinirprospectfinal = situacaoprospectworkflow.codigo ");
		sql.append("LEFT JOIN situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline ");

		sql.append("WHERE (prospects.inativo != true) and prospects.nome ilike '").append(letra).append("%' ");
		if (buscaProspectVO.getTipoPeriodo().equals("dataUltimoContato")) {
			if (buscaProspectVO.getDataInicio() != null || buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.codigo in ( select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect order by datainicio desc limit 1)");
			}
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and interacaoworkflow.datainicio >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.datainicio <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		} else {
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and prospects.dataCadastro >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and prospects.dataCadastro <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		}
		if (buscaProspectVO.getProspect().getCodigo() != 0) {
			sql.append(" AND prospects.codigo = ").append(buscaProspectVO.getProspect().getCodigo());
		}
		if (buscaProspectVO.getUnidadeEnsino().getCodigo() != 0) {
			sql.append(" AND prospects.unidadeEnsino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo());
			// sql.append(" AND (interacaoworkflow.unidadeEnsino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo()).append(" or interacaoworkflow.unidadeEnsino is null)");
		} else {
			if (!verificarPermissaoFuncionalidadeUsuario("BuscaProspect_consultarTodasUnidades", usuario) && !usuario.getTipoUsuario().equals("DM")) {
				sql.append(" and prospects.unidadeensino in (select distinct unidadeEnsino.codigo from unidadeEnsino inner join usuarioperfilacesso on usuarioperfilacesso.unidadeensino = unidadeensino.codigo ");
				sql.append(" where usuarioperfilacesso.usuario = ").append(usuario.getCodigo());
				sql.append(" )");
			}
		}
		if (buscaProspectVO.getCurso().getCodigo() != 0) {
			sql.append(" AND curso.codigo = ").append(buscaProspectVO.getCurso().getCodigo());
		}
		if (buscaProspectVO.getCidade().getCodigo() != 0) {
			sql.append(" AND prospects.cidade = ").append(buscaProspectVO.getCidade().getCodigo());
		}
		if (!buscaProspectVO.getEmail().trim().equals("")) {
			sql.append(" AND (prospects.emailprincipal ilike '").append(buscaProspectVO.getEmail()).append("' or prospects.emailsecundario ilike '").append(buscaProspectVO.getEmail()).append("') ");
		}
		if (!buscaProspectVO.getTelefone().trim().equals("")) {
			sql.append(" AND (sem_caracteres_especiais(prospects.telefoneresidencial) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getTelefone()).append("%') OR sem_caracteres_especiais(prospects.telefonecomercial) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getTelefone()).append("%')");
			sql.append(" OR sem_caracteres_especiais(prospects.telefonerecado) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getTelefone()).append("%') OR sem_caracteres_especiais(prospects.celular) ilike sem_caracteres_especiais('%").append(buscaProspectVO.getTelefone()).append("%')) ");
		}
		if (buscaProspectVO.getConsultorResponsavel().getCodigo() != 0) {
			sql.append(" AND (prospects.consultorpadrao = ").append(buscaProspectVO.getConsultorResponsavel().getCodigo()).append(" or prospects.consultorpadrao is null)");
		}
		if (buscaProspectVO.getConsultor().getPessoa().getCodigo() != 0) {
			sql.append(" AND ((pessoaResponsavel.codigo = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(") OR (pessoaResponsavel.codigo is null AND prospects.consultorpadrao = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(" ) or (pessoaResponsavel.codigo is null AND prospects.consultorpadrao is null)) ");

		}
		if (!buscaProspectVO.getSituacaoProspect().equals("")) {
			if (buscaProspectVO.getSituacaoProspect().equals("MATRICULADO")) {
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_COM_MATRICULA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_SEM_MATRICULA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_SEM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("NORMAL_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and (situacaoprospectpipeline.controle in ('NENHUM', 'INICIAL') or situacaoprospectpipeline.controle is null )");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_SUCESSO_COM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_SUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_COM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') > 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_SEM_MATRICULA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao = 'AT') = 0 ");
			} else if (buscaProspectVO.getSituacaoProspect().equals("FINALIZADO_INSUCESSO_SEM_MATRICULA_AT_TR_CA")) {
				sql.append(" and situacaoprospectpipeline.controle = 'FINALIZADO_INSUCESSO'");
				sql.append(" and (select count(matricula.matricula) from matricula ");
				sql.append(" where aluno = pessoaProspect.codigo ");
				sql.append(" and matricula.situacao in ('AT', 'TR', 'CA')) = 0 ");
			}
		}

		sql.append(" order by prospects.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosEmail(buscaProspectVO.getSituacaoProspect(), tabelaResultado, usuario);
	}

	private List<BuscaProspectVO> consultarProspectSemCompromissoAgendado(String letra, List<BuscaProspectVO> listaObjs, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception {
		if (!buscaProspectVO.getEmail().trim().equals("") || buscaProspectVO.getProspect().getCodigo() != 0) {
			letra = "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT interacaoworkflow.datainicio AS dataContato, pessoaResponsavel.nome AS nomeResponsavel, prospects.nome AS nomeProspect,interacaoworkflow.observacao AS observacao, prospects.emailprincipal AS email, 'NENHUM' AS controleSituacaoProspectPipeline, '' AS turma, prospects.codigo AS codigoProspect, ");
		sql.append("prospects.nome AS nomeProspect, prospects.dataCadastro AS dataCadastro, prospects.telefoneResidencial AS telRes, prospects.telefoneComercial AS telComer, prospects.celular AS cel, prospects.telefoneRecado AS telRec, prospects.emailPrincipal AS email, curso.nome AS nomeCurso, curso.codigo AS codigoCurso, consultorPadrao.nome AS nomeConsultorPadrao, '' as matricula ");
		sql.append("FROM prospects ");
		sql.append("LEFT JOIN interacaoworkflow ON prospects.codigo = interacaoworkflow.prospect and interacaoworkflow.codigo = (select iwf.codigo from interacaoworkflow iwf where prospects.codigo = iwf.prospect order by datainicio desc limit 1) ");
		sql.append("LEFT JOIN usuario ON interacaoworkflow.responsavel = usuario.codigo ");
		sql.append("LEFT JOIN funcionario  ON prospects.consultorPadrao = funcionario.codigo ");
		sql.append("LEFT JOIN pessoa AS consultorPadrao  ON consultorPadrao.codigo = funcionario.pessoa ");
		sql.append("LEFT JOIN pessoa AS pessoaResponsavel ON usuario.pessoa = pessoaResponsavel.codigo ");
		sql.append("LEFT JOIN curso ON interacaoworkflow.curso = curso.codigo ");
		sql.append("LEFT JOIN cidade ON cidade.codigo = prospects.cidade ");
		sql.append("WHERE (prospects.inativo != true) and prospects.nome ilike '").append(letra).append("%' ");
		if (!getProspectsInteracoesListadas().isEmpty()) {
			sql.append("AND prospects.codigo not in ( ");
			Set<Integer> chaves = getProspectsInteracoesListadas().keySet();
			int cont = 1;
			for (Integer codigoProspect : chaves) {
				if (cont != chaves.size()) {
					sql.append(codigoProspect).append(",");
				} else {
					sql.append(codigoProspect);
				}
				cont++;
			}
			sql.append(") ");
		}
		// sql.append("AND  (interacaoworkflow.datainicio in (select max(datainicio) from interacaoworkflow group by prospect) or datainicio is null) ");
		if (buscaProspectVO.getTipoPeriodo().equals("dataUltimoContato")) {
			if (buscaProspectVO.getDataInicio() != null || buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.codigo in ( select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect order by datainicio desc limit 1)");
			}
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and interacaoworkflow.datainicio >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.datainicio <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		} else {
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and prospects.dataCadastro >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and prospects.dataCadastro <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		}
		if (buscaProspectVO.getProspect().getCodigo() != 0) {
			sql.append(" AND prospects.codigo = ").append(buscaProspectVO.getProspect().getCodigo());
		}
		if (buscaProspectVO.getUnidadeEnsino().getCodigo() != 0) {
			sql.append(" AND prospects.unidadeEnsino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo());
			// sql.append(" AND (interacaoworkflow.unidadeEnsino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo()).append(" OR prospects.unidadeensino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo()).append(") ");
		}
		if (buscaProspectVO.getCurso().getCodigo() != 0) {
			sql.append(" AND interacaoworkflow.curso = ").append(buscaProspectVO.getCurso().getCodigo());
		}
		if (buscaProspectVO.getCidade().getCodigo() != 0) {
			sql.append(" AND prospects.cidade = ").append(buscaProspectVO.getCidade().getCodigo());
		}
		if (!buscaProspectVO.getEmail().trim().equals("")) {
			sql.append(" AND (prospects.emailprincipal ilike '").append(buscaProspectVO.getEmail()).append("%' or prospects.emailsecundario ilike '").append(buscaProspectVO.getEmail()).append("' )");
			;
		}
		if (!buscaProspectVO.getTelefone().trim().equals("")) {
			sql.append(" AND (prospects.telefoneresidencial ilike '%").append(buscaProspectVO.getTelefone()).append("%' OR prospects.telefonecomercial ilike '%").append(buscaProspectVO.getTelefone()).append("%'");
			sql.append(" OR prospects.telefonerecado ilike '%").append(buscaProspectVO.getTelefone()).append("%' OR prospects.celular ilike '%").append(buscaProspectVO.getTelefone()).append("%') ");
		}
		if (buscaProspectVO.getConsultor().getPessoa().getCodigo() != 0) {
			sql.append(" AND (pessoaResponsavel.codigo = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(" OR consultorPadrao.codigo = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(") ");
		}
		if (buscaProspectVO.getOrdenacao().equals("data")) {
			sql.append(" order by interacaoworkflow.datainicio desc, prospects.nome, pessoaResponsavel.nome ");
		} else if (buscaProspectVO.getOrdenacao().equals("nomeProspect")) {
			sql.append(" order by prospects.nome, interacaoworkflow.datainicio desc, pessoaResponsavel.nome ");
		} else if (buscaProspectVO.getOrdenacao().equals("dataCadastro")) {
			sql.append(" order by prospects.dataCadastro, pessoaResponsavel.nome, interacaoworkflow.datainicio desc, prospects.nome ");
		} else {
			sql.append(" order by pessoaResponsavel.nome, interacaoworkflow.datainicio desc, prospects.nome ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDados(listaObjs, buscaProspectVO.getSituacaoProspect(), tabelaResultado, usuario));
	}

	private void consultarEmailProspectSemCompromissoAgendado(String letra, BuscaProspectVO buscaProspectVO, UsuarioVO usuario) throws Exception {
		if (!buscaProspectVO.getEmail().trim().equals("") || buscaProspectVO.getProspect().getCodigo() != 0) {
			letra = "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT prospects.emailprincipal AS email, 'NENHUM' AS controleSituacaoProspectPipeline, '' AS turma ");
		sql.append("FROM prospects ");
		sql.append("LEFT JOIN interacaoworkflow ON prospects.codigo = interacaoworkflow.prospect ");
		sql.append("LEFT JOIN usuario ON interacaoworkflow.responsavel = usuario.codigo ");
		sql.append("LEFT JOIN funcionario  ON prospects.consultorPadrao = funcionario.codigo ");
		sql.append("LEFT JOIN pessoa AS consultorPadrao  ON consultorPadrao.codigo = funcionario.pessoa ");
		sql.append("LEFT JOIN pessoa AS pessoaResponsavel ON usuario.pessoa = pessoaResponsavel.codigo ");
		sql.append("LEFT JOIN curso ON interacaoworkflow.curso = curso.codigo ");
		sql.append("LEFT JOIN cidade ON cidade.codigo = prospects.cidade ");
		sql.append("WHERE (prospects.inativo != true) and prospects.nome ilike '").append(letra).append("%' ");
		if (buscaProspectVO.getTipoPeriodo().equals("dataUltimoContato")) {
			if (buscaProspectVO.getDataInicio() != null || buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.codigo in ( select iwf.codigo from interacaoworkflow iwf WHERE prospects.codigo = iwf.prospect order by datainicio desc limit 1)");
			}
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and interacaoworkflow.datainicio >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and interacaoworkflow.datainicio <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		} else {
			if (buscaProspectVO.getDataInicio() != null) {
				sql.append(" and prospects.dataCadastro >= '").append(Uteis.getDataBD0000(buscaProspectVO.getDataInicio())).append("' ");
			}
			if (buscaProspectVO.getDataFim() != null) {
				sql.append(" and prospects.dataCadastro <= '").append(Uteis.getDataBD2359(buscaProspectVO.getDataFim())).append("' ");
			}
		}
		if (buscaProspectVO.getProspect().getCodigo() != 0) {
			sql.append(" AND prospects.codigo = ").append(buscaProspectVO.getProspect().getCodigo());
		}
		if (buscaProspectVO.getUnidadeEnsino().getCodigo() != 0) {
			sql.append(" AND prospects.unidadeEnsino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo());
			// sql.append(" AND (interacaoworkflow.unidadeEnsino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo()).append(" OR prospects.unidadeensino = ").append(buscaProspectVO.getUnidadeEnsino().getCodigo()).append(") ");
		}
		if (buscaProspectVO.getCurso().getCodigo() != 0) {
			sql.append(" AND interacaoworkflow.curso = ").append(buscaProspectVO.getCurso().getCodigo());
		}
		if (buscaProspectVO.getCidade().getCodigo() != 0) {
			sql.append(" AND prospects.cidade = ").append(buscaProspectVO.getCidade().getCodigo());
		}
		if (!buscaProspectVO.getEmail().trim().equals("")) {
			sql.append(" AND (prospects.emailprincipal ilike '").append(buscaProspectVO.getEmail()).append("%' or prospects.emailsecundario ilike '").append(buscaProspectVO.getEmail()).append("' )");
			;
		}
		if (!buscaProspectVO.getTelefone().trim().equals("")) {
			sql.append(" AND (prospects.telefoneresidencial ilike '%").append(buscaProspectVO.getTelefone()).append("%' OR prospects.telefonecomercial ilike '%").append(buscaProspectVO.getTelefone()).append("%'");
			sql.append(" OR prospects.telefonerecado ilike '%").append(buscaProspectVO.getTelefone()).append("%' OR prospects.celular ilike '%").append(buscaProspectVO.getTelefone()).append("%') ");
		}
		if (buscaProspectVO.getConsultorResponsavel().getCodigo() != 0) {
			sql.append(" AND (prospects.consultorpadrao = ").append(buscaProspectVO.getConsultorResponsavel().getCodigo()).append(")");
		}
		if (buscaProspectVO.getConsultor().getPessoa().getCodigo() != 0) {
			sql.append(" AND (pessoaResponsavel.codigo = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(")");
		}
		// if (buscaProspectVO.getConsultor().getPessoa().getCodigo() != 0) {
		// sql.append(" AND (pessoaResponsavel.codigo = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(" OR consultorPadrao.codigo = ").append(buscaProspectVO.getConsultor().getPessoa().getCodigo()).append(") ")
		// ;
		// }
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		montarDadosEmail(buscaProspectVO.getSituacaoProspect(), tabelaResultado, usuario);
	}

	private List<BuscaProspectVO> montarDados(List<BuscaProspectVO> listaObjs, String situacao, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		while (dadosSQL.next()) {
			BuscaProspectVO obj = new BuscaProspectVO();
			SituacaoProspectPipelineControleEnum situacaoTemp = SituacaoProspectPipelineControleEnum.NENHUM;
			if (dadosSQL.getString("controleSituacaoProspectPipeline") != null) {
				situacaoTemp = SituacaoProspectPipelineControleEnum.valueOf(dadosSQL.getString("controleSituacaoProspectPipeline"));
			} else {
				situacaoTemp = SituacaoProspectPipelineControleEnum.valueOf("INICIAL");
			}

			obj.setPossuiMatricula(dadosSQL.getBoolean("possuiMatricula"));

			if (situacaoTemp.equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)) {
				obj.setSituacao("Finalizado Com Sucesso");
			} else if (situacaoTemp.equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO)) {
				obj.setSituacao("Finalizado com Insucesso");
			} else {
				obj.setSituacao("Em Prospeccão");
			}

			PessoaVO pessoaTemp = new PessoaVO();
			if (dadosSQL.getString("nomeResponsavel") == null || dadosSQL.getString("nomeResponsavel").equals("")) {
				pessoaTemp.setNome(dadosSQL.getString("nomeConsultorPadrao"));
			} else {
				pessoaTemp.setNome(dadosSQL.getString("nomeResponsavel"));
			}
			obj.getProspectNovaInteracao().setCodigo(dadosSQL.getInt("codigoProspect"));
			obj.getProspectNovaInteracao().setNome(dadosSQL.getString("nomeProspect"));
			obj.getProspectNovaInteracao().setTelefoneResidencial(dadosSQL.getString("telRes"));
			obj.getProspectNovaInteracao().setTelefoneRecado(dadosSQL.getString("telRec"));
			obj.getProspectNovaInteracao().setTelefoneComercial(dadosSQL.getString("telComer"));
			obj.getProspectNovaInteracao().setCelular(dadosSQL.getString("cel"));
			obj.getProspectNovaInteracao().setEmailPrincipal(dadosSQL.getString("email"));
			
			obj.getProspectNovaInteracao().setSincronizadoRDStation(dadosSQL.getBoolean("sincronizadoRdStation"));
			
			if (dadosSQL.getInt("codigoCurso") != 0) {
				obj.getCursoNovaInteracao().setCodigo(dadosSQL.getInt("codigoCurso"));
				obj.getCursoNovaInteracao().setNome(dadosSQL.getString("nomeCurso"));
			}
			obj.setDataContato(dadosSQL.getTimestamp("dataContato"));
			obj.setDataCadastro(dadosSQL.getTimestamp("dataCadastro"));
			obj.setNomeConsultor(pessoaTemp.getNomeResumidoNomeSobrenome());
			obj.setNomeProspect(dadosSQL.getString("nomeProspect"));
			obj.setObservacao(dadosSQL.getString("observacao"));
			obj.setEmailProspect(dadosSQL.getString("email"));
			obj.setTurmaMatriculado(dadosSQL.getString("turma"));
			
			obj.getProspect().setSincronizadoRDStation(dadosSQL.getBoolean("sincronizadoRdStation"));

			if (!obj.getSituacao().toLowerCase().equals(("Finalizado com insucesso").toLowerCase())) {
				if (obj.getSituacao().equals(("Matriculado"))) {
					// obj.setIconeDescricaoSituacaoProspect("/imagens/ok.png");
					// obj.setDescricaoSituacaoProspect("Aluno matriculado => "
					// + dadosSQL.getString("matricula") + " - " +
					// dadosSQL.getString("cursoMatriculado"));
				} else if (obj.getSituacao().contains(("Finalizado Com Sucesso"))) {
					// obj.setIconeDescricaoSituacaoProspect("/imagens/ok.png");
					// obj.setDescricaoSituacaoProspect("Aluno matriculado => "
					// + dadosSQL.getString("matricula") + " - " +
					// dadosSQL.getString("cursoMatriculado"));
				} else if (Uteis.getObterDiferencaDiasEntreDuasData(new Date(), obj.getDataContato()) >= 90 && Uteis.getObterDiferencaDiasEntreDuasData(new Date(0, 0, 0), obj.getDataContato()) != 0) {
					obj.setIconeDescricaoSituacaoProspect("fas fa-exclamation-triangle text-danger");
					obj.setDescricaoSituacaoProspect("Prospect contactado a mais de 90 dias.");
				} else if (Uteis.getObterDiferencaDiasEntreDuasData(new Date(0, 0, 0), obj.getDataContato()) != 0) {
					obj.setIconeDescricaoSituacaoProspect("fas fa-exclamation-triangle text-warning");
					obj.setDescricaoSituacaoProspect("Prospect contactado e não finalizado.");
				}
			}
			if (!getProspectsInteracoesListadas().containsKey(obj.getProspectNovaInteracao().getCodigo())) {
				getProspectsInteracoesListadas().put(obj.getProspectNovaInteracao().getCodigo(), obj);
			} else {
				BuscaProspectVO objTemp = (BuscaProspectVO) getProspectsInteracoesListadas().get(obj.getProspectNovaInteracao().getCodigo());
				if (obj.getDataContato() != null) {
					if (obj.getDataContato().after(objTemp.getDataContato())) {
						getProspectsInteracoesListadas().remove(objTemp.getProspectNovaInteracao().getCodigo());
						getProspectsInteracoesListadas().put(obj.getProspectNovaInteracao().getCodigo(), obj);
					}
				}
			}
		}
		return listaObjs;
	}

	private List<BuscaProspectVO> montarDadosEmail(String situacao, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		List<BuscaProspectVO> emails = new ArrayList<BuscaProspectVO>(0);
		BuscaProspectVO obj = null;
		while (dadosSQL.next()) {
			obj = new BuscaProspectVO();
			obj.setEmail(dadosSQL.getString("email"));
			obj.setNomeProspect(dadosSQL.getString("nome"));
			obj.setTelefoneResidencial(dadosSQL.getString("telefoneresidencial"));
			obj.setTelefoneComercial(dadosSQL.getString("telefonecomercial"));
			obj.setCelular(dadosSQL.getString("celular"));
			emails.add(obj);
		}
		return emails;
	}

	List<BuscaProspectVO> emails = new ArrayList<BuscaProspectVO>(0);

	private List<BuscaProspectVO> montarDadosEmailSuggestion(SqlRowSet dadosSQL) throws Exception {
		BuscaProspectVO obj = null;
		List<BuscaProspectVO> listaRetorno = new ArrayList<BuscaProspectVO>(0);
		while (dadosSQL.next()) {
			obj = new BuscaProspectVO();
			obj.setEmail(dadosSQL.getString("email"));
			listaRetorno.add(obj);
		}
		return listaRetorno;
	}
	
	private List<BuscaProspectVO> montarDadosTelefoneSuggestion(SqlRowSet dadosSQL) throws Exception {
		BuscaProspectVO obj = null;
		List<BuscaProspectVO> listaRetorno = new ArrayList<BuscaProspectVO>(0);
		while (dadosSQL.next()) {
			obj = new BuscaProspectVO();
			if(dadosSQL.getString("telefoneresidencial") != null && !dadosSQL.getString("telefoneresidencial").trim().equals("")){
				obj.setTelefone(dadosSQL.getString("telefoneresidencial"));
				listaRetorno.add(obj);
				continue;
			}
			if(dadosSQL.getString("telefonecomercial") != null && !dadosSQL.getString("telefonecomercial").trim().equals("")){
				obj.setTelefone(dadosSQL.getString("telefonecomercial"));
				listaRetorno.add(obj);
				continue;
			}
			if(dadosSQL.getString("telefonerecado") != null && !dadosSQL.getString("telefonerecado").trim().equals("")){
				obj.setTelefone(dadosSQL.getString("telefonerecado"));
				listaRetorno.add(obj);
				continue;
			}
			if(dadosSQL.getString("celular") != null && !dadosSQL.getString("celular").trim().equals("")){
				obj.setTelefone(dadosSQL.getString("celular"));
				listaRetorno.add(obj);
				continue;
			}
		}
		return listaRetorno;
	}

	public HashMap<Integer, Object> getProspectsInteracoesListadas() {
		if (prospectsInteracoesListadas == null) {
			prospectsInteracoesListadas = new HashMap<Integer, Object>();
		}
		return prospectsInteracoesListadas;
	}

	public void setProspectsInteracoesListadas(HashMap<Integer, Object> prospectsInteracoesListadas) {
		this.prospectsInteracoesListadas = prospectsInteracoesListadas;
	}

	public List<String> getProspectsEmailsListados() {
		if (prospectsEmailsListados == null) {
			prospectsEmailsListados = new ArrayList<String>(0);
		}
		return prospectsEmailsListados;
	}

	public void setProspectsEmailsListados(List<String> prospectsEmailsListados) {
		this.prospectsEmailsListados = prospectsEmailsListados;
	}
	
	@Override
	public List<BuscaProspectVO> consultarCpfSuggestionBox(String info,UsuarioVO usuario) throws Exception{
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct cpf ");
			sql.append("from prospects where 1=1 ");
			sql.append(" AND (sem_caracteres_especiais(prospects.cpf) ilike ('");
			sql.append(info).append("%'))");
			sql.append(" order by cpf ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return montarDadosCpfSuggestion(tabelaResultado);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private List<BuscaProspectVO> montarDadosCpfSuggestion(SqlRowSet dadosSQL) throws Exception {
		BuscaProspectVO obj = null;
		List<BuscaProspectVO> listaRetorno = new ArrayList<BuscaProspectVO>(0);
		while (dadosSQL.next()) {
			obj = new BuscaProspectVO();
			if(dadosSQL.getString("cpf") != null && !dadosSQL.getString("cpf").trim().equals("")){
				obj.setCpf(dadosSQL.getString("cpf"));
				listaRetorno.add(obj);
				continue;
			}
		}
		return listaRetorno;
	}
}
