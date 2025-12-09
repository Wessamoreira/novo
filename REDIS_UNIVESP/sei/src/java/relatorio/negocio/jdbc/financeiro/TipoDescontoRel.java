/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FinanciamentoEstudantil;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.AlunoTipoDescontoRelVO;
import relatorio.negocio.comuns.financeiro.DescontoTipoDescontoRelVO;
import relatorio.negocio.comuns.financeiro.TipoDescontoRelVO;
import relatorio.negocio.interfaces.financeiro.TipoDescontoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class TipoDescontoRel extends SuperRelatorio implements TipoDescontoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public List<TipoDescontoRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String filtro, Integer unidadeEnsino, Integer curso, Integer turma, Integer aluno, Boolean descontoAluno, String ano, String semestre, Integer convenio, Integer planoDesconto, Integer descontoprogressivo, String financiamentoestudantil) throws Exception {
		validarDados(unidadeEnsinoVOs);
		SqlRowSet dadosSQL = executarConsultaParametrizada(unidadeEnsinoVOs, filtro, unidadeEnsino, curso, turma, aluno, descontoAluno, ano, semestre, convenio, planoDesconto, descontoprogressivo, financiamentoestudantil);
		return montarDados(dadosSQL, filtro, unidadeEnsinoVOs, curso, turma, aluno, descontoAluno, ano, semestre, convenio, planoDesconto, descontoprogressivo, financiamentoestudantil);
	}

	public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws ConsistirException {
		boolean excessao = true;
		for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
			if (ue.getFiltrarUnidadeEnsino()) {
				excessao = false;
			}
		}
		if (excessao) {
			throw new ConsistirException("O campo UNIDADE DE ENSINO deve ser informado.");
		}
	}

	public SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String filtro, Integer unidadeEnsino, Integer curso, Integer turma, Integer aluno, Boolean descontoAluno, String ano, String semestre, Integer convenio, Integer planoDesconto, Integer descontoprogressivo, String financiamentoEstudantil) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT planofinanceiroaluno.valordescontomatricula AS planofinanceiroaluno_valordescontomatricula, ");
		sqlStr.append("planofinanceiroaluno.valordescontoparcela AS planofinanceiroaluno_valordescontoparcela, pessoa.codigo AS pessoa_codigo,  ");
		sqlStr.append("planofinanceiroaluno.tipodescontomatricula AS planofinanceiroaluno_tipodescontomatricula, ");
		sqlStr.append("planofinanceiroaluno.tipodescontoparcela AS planofinanceiroaluno_tipodescontoparcela, matricula.financiamentoestudantil AS matricula_financiamentoestudantil, ");
		sqlStr.append("matricula.matricula AS matricula, turma.identificadorturma AS turma_identificadorturma, pessoa.nome AS pessoa_nome, curso.nome as curso, turno.nome, ");
		sqlStr.append("descontoprogressivo.codigo AS descontoprogressivo_codigo, descontoprogressivo.nome AS descontoprogressivo_nome, ");
		sqlStr.append("planodesconto.codigo AS planodesconto_codigo, planodesconto.nome AS planodesconto_nome, convenio.codigo AS convenio_codigo, convenio.descricao AS convenio_descricao ");
		sqlStr.append("FROM planofinanceiroaluno ");
		sqlStr.append("INNER JOIN itemPlanoFinanceiroAluno on itemPlanoFinanceiroAluno.planofinanceiroaluno = planofinanceiroaluno.codigo ");
		sqlStr.append("INNER JOIN matriculaPeriodo ON matriculaPeriodo.codigo = planofinanceiroaluno.matriculaperiodo ");
		sqlStr.append("INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append("INNER JOIN turma ON turma.codigo = matriculaPeriodo.turma ");
		sqlStr.append("INNER JOIN turno on turno.codigo = matricula.turno ");
		sqlStr.append("INNER JOIN pessoa ON matricula.aluno = pessoa.codigo ");
		sqlStr.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		sqlStr.append("left join descontoprogressivo on descontoprogressivo.codigo = planofinanceiroaluno.descontoprogressivo ");
		sqlStr.append("left join convenio on convenio.codigo = itemplanofinanceiroaluno.convenio and itemplanofinanceiroaluno.tipoitemplanofinanceiro = 'CO' ");
		sqlStr.append("left join planodesconto on planodesconto.codigo = itemplanofinanceiroaluno.planodesconto and itemplanofinanceiroaluno.tipoitemplanofinanceiro = 'PD' ");
		sqlStr.append("WHERE 1=1 ");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and matricula.unidadeensino in (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND matricula.curso = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND matriculaperiodo.ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND matriculaperiodo.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" AND matriculaPeriodo.turma = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(aluno)) {
			sqlStr.append(" AND pessoa.codigo = ").append(aluno);
		}
		if (!filtro.equals("todos")) {
			if (Uteis.isAtributoPreenchido(descontoprogressivo)) {
				sqlStr.append(" AND descontoprogressivo.codigo = ").append(descontoprogressivo);
			}
			if (Uteis.isAtributoPreenchido(financiamentoEstudantil)) {
				sqlStr.append(" AND financiamentoestudantil = '").append(financiamentoEstudantil).append("' ");
			}
			if (descontoAluno) {
				sqlStr.append(" AND (planofinanceiroaluno.valordescontomatricula <> 0 ").append(" OR planofinanceiroaluno.valordescontoparcela <> 0) ");
			}
			if (Uteis.isAtributoPreenchido(convenio)) {
				sqlStr.append(" AND convenio.codigo = ").append(convenio);
			}
			if (Uteis.isAtributoPreenchido(planoDesconto)) {
				sqlStr.append(" AND planodesconto.codigo = ").append(planoDesconto);
			}
		}
		sqlStr.append(" order by curso.nome, turno.nome, turma.identificadorturma, pessoa.nome");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	public List<TipoDescontoRelVO> montarDados(SqlRowSet dadosSQL, String filtro, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer turma, Integer aluno, Boolean descontoAluno, String ano, String semestre, Integer convenio, Integer planoDesconto, Integer descontoprogressivo, String financiamentoEstudantil) throws Exception {
		List<TipoDescontoRelVO> descontoRelVOs = new ArrayList<TipoDescontoRelVO>(0);
		while (dadosSQL.next()) {
			TipoDescontoRelVO obj = new TipoDescontoRelVO();
			obj.setTurma(dadosSQL.getString("turma_identificadorturma"));
			obj.setCurso(dadosSQL.getString("curso"));
			AlunoTipoDescontoRelVO objAluno = new AlunoTipoDescontoRelVO();
			objAluno.setAluno(dadosSQL.getString("pessoa_nome"));
			objAluno.setMatricula(dadosSQL.getString("matricula"));
			if (!filtro.equals("todos")) {
				if (filtro.equals("descontoProgressivo")) {
					if (dadosSQL.getString("descontoprogressivo_nome") != null) {
						DescontoTipoDescontoRelVO objDescontoProgressivo = new DescontoTipoDescontoRelVO();
						objDescontoProgressivo.setTipo("Desconto progressivo");
						objDescontoProgressivo.setDescricao(dadosSQL.getString("descontoprogressivo_nome"));
						objAluno.getDescontos().add(objDescontoProgressivo);
					}
				} else if (filtro.equals("descontoAluno")) {
					if (dadosSQL.getDouble("planofinanceiroaluno_valordescontoparcela") > 0) {
						DescontoTipoDescontoRelVO objDescontoParcela = new DescontoTipoDescontoRelVO();
						objDescontoParcela.setTipo("Desconto Aluno: Parcela");
						if (TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontoparcela")).equals("R$")) {
							objDescontoParcela.setDescricao(TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontoparcela")) + " " + dadosSQL.getString("planofinanceiroaluno_valordescontoparcela"));
						} else {
							objDescontoParcela.setDescricao(dadosSQL.getString("planofinanceiroaluno_valordescontoparcela") + TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontoparcela")));
						}
						objAluno.getDescontos().add(objDescontoParcela);
					}
					if (dadosSQL.getDouble("planofinanceiroaluno_valordescontomatricula") > 0) {
						DescontoTipoDescontoRelVO objDescontoMatricula = new DescontoTipoDescontoRelVO();
						objDescontoMatricula.setTipo("Desconto Aluno: Matrícula");
						if (TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontomatricula")).equals("R$")) {
							objDescontoMatricula.setDescricao(TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontomatricula")) + " " + dadosSQL.getString("planofinanceiroaluno_valordescontomatricula"));
						} else {
							objDescontoMatricula.setDescricao(dadosSQL.getString("planofinanceiroaluno_valordescontomatricula") + TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontomatricula")));
						}
						objAluno.getDescontos().add(objDescontoMatricula);
					}
				} else if (filtro.equals("financiamentoEstudantil")) {
					if (dadosSQL.getString("matricula_financiamentoestudantil") != null && !dadosSQL.getString("matricula_financiamentoestudantil").trim().equals("")) {
						DescontoTipoDescontoRelVO objFinanciamentoEstudantil = new DescontoTipoDescontoRelVO();
						objFinanciamentoEstudantil.setTipo("Financiamento Estudantil");
						objFinanciamentoEstudantil.setDescricao(FinanciamentoEstudantil.getDescricao(dadosSQL.getString("matricula_financiamentoestudantil")));
						objAluno.getDescontos().add(objFinanciamentoEstudantil);
					}
				} else if (filtro.equals("descontoConvenio")) {
					DescontoTipoDescontoRelVO objDescontoConvenio = new DescontoTipoDescontoRelVO();
					objDescontoConvenio.setTipo("Convênio");
					objDescontoConvenio.setDescricao(dadosSQL.getString("convenio_descricao"));
					objAluno.getDescontos().add(objDescontoConvenio);
				} else if (filtro.equals("descontoInstituicao")) {
					DescontoTipoDescontoRelVO objDescontoInstituicao = new DescontoTipoDescontoRelVO();
					objDescontoInstituicao.setTipo("Plano Desconto");
					objDescontoInstituicao.setDescricao(dadosSQL.getString("planodesconto_nome"));
					objAluno.getDescontos().add(objDescontoInstituicao);
				}
			} else {
				if (Uteis.isAtributoPreenchido(dadosSQL.getString("descontoprogressivo_nome"))) {
					DescontoTipoDescontoRelVO objDescontoProgressivo = new DescontoTipoDescontoRelVO();
					objDescontoProgressivo.setTipo("Desconto progressivo");
					objDescontoProgressivo.setDescricao(dadosSQL.getString("descontoprogressivo_nome"));
					objAluno.getDescontos().add(objDescontoProgressivo);
				}
				if (Uteis.isAtributoPreenchido(dadosSQL.getDouble("planofinanceiroaluno_valordescontoparcela"))) {
					DescontoTipoDescontoRelVO objDescontoParcela = new DescontoTipoDescontoRelVO();
					objDescontoParcela.setTipo("Desconto Aluno: Parcela");
					if (TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontoparcela")).equals("R$")) {
						objDescontoParcela.setDescricao(TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontoparcela")) + " " + dadosSQL.getString("planofinanceiroaluno_valordescontoparcela"));
					} else {
						objDescontoParcela.setDescricao(dadosSQL.getString("planofinanceiroaluno_valordescontoparcela") + TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontoparcela")));
					}
					objAluno.getDescontos().add(objDescontoParcela);
				}
				if (Uteis.isAtributoPreenchido(dadosSQL.getDouble("planofinanceiroaluno_valordescontomatricula"))) {
					DescontoTipoDescontoRelVO objDescontoMatricula = new DescontoTipoDescontoRelVO();
					objDescontoMatricula.setTipo("Desconto Aluno: Matrícula");
					if (TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontomatricula")).equals("R$")) {
						objDescontoMatricula.setDescricao(TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontomatricula")) + " " + dadosSQL.getString("planofinanceiroaluno_valordescontomatricula"));
					} else {
						objDescontoMatricula.setDescricao(dadosSQL.getString("planofinanceiroaluno_valordescontomatricula") + TipoDescontoAluno.getDescricao(dadosSQL.getString("planofinanceiroaluno_tipodescontomatricula")));
					}
					objAluno.getDescontos().add(objDescontoMatricula);
				}
				if (Uteis.isAtributoPreenchido(dadosSQL.getString("matricula_financiamentoestudantil"))) {
					DescontoTipoDescontoRelVO objFinanciamentoEstudantil = new DescontoTipoDescontoRelVO();
					objFinanciamentoEstudantil.setTipo("Financiamento Estudantil");
					objFinanciamentoEstudantil.setDescricao(FinanciamentoEstudantil.getDescricao(dadosSQL.getString("matricula_financiamentoestudantil")));
					objAluno.getDescontos().add(objFinanciamentoEstudantil);
				}
				if (Uteis.isAtributoPreenchido(dadosSQL.getString("convenio_descricao"))) {
					DescontoTipoDescontoRelVO objDescontoConvenio = new DescontoTipoDescontoRelVO();
					objDescontoConvenio.setTipo("Convênio");
					objDescontoConvenio.setDescricao(dadosSQL.getString("convenio_descricao"));
					objAluno.getDescontos().add(objDescontoConvenio);
				}
				if (Uteis.isAtributoPreenchido(dadosSQL.getString("planodesconto_nome"))) {
					DescontoTipoDescontoRelVO objDescontoInstituicao = new DescontoTipoDescontoRelVO();
					objDescontoInstituicao.setTipo("Plano Desconto");
					objDescontoInstituicao.setDescricao(dadosSQL.getString("planodesconto_nome"));
					objAluno.getDescontos().add(objDescontoInstituicao);
				}
			}
			if (validarNovaTurma(descontoRelVOs, obj, objAluno)) {
				if (!objAluno.getDescontos().isEmpty()) {
					descontoRelVOs.add(obj);
				}
			}
		}
		return descontoRelVOs;
	}

	private Boolean validarNovaTurma(List<TipoDescontoRelVO> listaObj, TipoDescontoRelVO obj, AlunoTipoDescontoRelVO objAluno) {
		for (TipoDescontoRelVO objExistente : listaObj) {
			if (objExistente.getTurma().equals(obj.getTurma())) {
				objExistente.getAlunos().add(objAluno);
				return false;
			}
		}
		obj.getAlunos().add(objAluno);
		return true;
	}

	@Override
	public String designIReportRelatorio() {
		return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
	}

	@Override
	public String designIReportRelatorioExcel() {
		return (caminhoBaseRelatorio() + getIdEntidadeExcel() + ".jrxml");
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return "TipoDescontoRel";
	}

	public static String getIdEntidadeExcel() {
		return "TipoDescontoExcelRel";
	}

	public static String getDescricaoTipoDesconto(String tipoDesconto) {
		if (tipoDesconto.equals("todos")) {
			return "Todos";
		} else if (tipoDesconto.equals("descontoInstituicao")) {
			return "Desconto Instituição";
		} else if (tipoDesconto.equals("descontoProgressivo")) {
			return "Desconto Progressivo";
		} else if (tipoDesconto.equals("descontoConvenio")) {
			return "Desconto Convênio";
		} else if (tipoDesconto.equals("descontoAluno")) {
			return "Desconto Aluno";
		} else if (tipoDesconto.equals("financiamentoEstudantil")) {
			return "Financiamento Estudantil";
		}
		return "";
	}

}
