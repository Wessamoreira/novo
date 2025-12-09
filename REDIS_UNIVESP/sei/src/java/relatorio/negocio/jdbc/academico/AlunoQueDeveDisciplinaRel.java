/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaFiltroRelVO;
import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaRelVO;
import relatorio.negocio.interfaces.academico.AlunoQueDeveDisciplinaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Otimize-Not
 */
@Service
@Scope
@Lazy
public class AlunoQueDeveDisciplinaRel extends SuperRelatorio implements AlunoQueDeveDisciplinaRelInterfaceFacade {

	public AlunoQueDeveDisciplinaRel() {
	}

	public void validarDados(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) throws ConsistirException {
		if (alunoNaoCursouDisciplinaFiltroRelVO.getDisciplinaVO().getCodigo().intValue() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunoNaoCursouDisciplinaRel_disciplina"));
		}
	}

	public void consultarRelatorio(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) throws Exception {
		validarDados(alunoNaoCursouDisciplinaFiltroRelVO);
		alunoNaoCursouDisciplinaFiltroRelVO.setAlunoNaoCursouDisciplinaRelVOs(new ArrayList<AlunoNaoCursouDisciplinaRelVO>(0));
		StringBuilder sb = new StringBuilder();
		sb.append(realizarMontarConsultaSQL(alunoNaoCursouDisciplinaFiltroRelVO));
		SqlRowSet sqlRowSet = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		montaDadosConsulta(sqlRowSet, alunoNaoCursouDisciplinaFiltroRelVO);
	}

	private StringBuilder realizarMontarConsultaSQL(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT m.matricula AS matricula_matricula, p.nome AS pessoa_nome, p.cpf AS pessoa_cpf, p.telefoneres AS pessoa_telefoneres, ");
		sb.append("p.telefonecomer AS pessoa_telefonecomer, p.telefonerecado AS pessoa_telefonerecado, p.celular AS pessoa_celular, p.email AS pessoa_email, ");
		sb.append("c.nome AS curso_nome, ue.nome  AS unidadeensino_nome, m.anoingresso AS matricula_anoingresso from matricula m ");
		sb.append("LEFT JOIN pessoa p on  p.codigo = m.aluno ");
		sb.append("LEFT JOIN curso c on m.curso = c.codigo ");
		sb.append("LEFT JOIN unidadeensino ue on ue.codigo = m.unidadeensino ");
		sb.append("LEFT JOIN matriculaperiodo mp on m.matricula = mp.matricula ");
		sb.append("INNER JOIN gradecurricular gc on mp.gradecurricular = gc.codigo ");
		sb.append("INNER JOIN periodoletivo pl on gc.codigo = pl.gradecurricular ");
		sb.append("INNER JOIN gradedisciplina gd on gd.periodoletivo = pl.codigo ");
		sb.append("LEFT JOIN historico h on h.matricula = m.matricula AND gd.disciplina = h.disciplina ");
		sb.append("WHERE m.situacao = 'AT' AND (m.anoingresso::int >= ");
		sb.append(alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseInicio() + " ");
		sb.append("AND m.anoingresso::int <= ");
		sb.append(alunoNaoCursouDisciplinaFiltroRelVO.getAnoBaseFim() + " ) ");
		sb.append("AND gd.disciplina = ");
		sb.append(alunoNaoCursouDisciplinaFiltroRelVO.getDisciplinaVO().getCodigo() + " ");
		sb.append("AND ((h.situacao <> 'AP' AND h.situacao <> 'AA' ) ");
                sb.append(" OR h.situacao is null OR h.codigo is null) ");
                sb.append(" AND (((h.mediafinal = 0 or h.mediafinal is null) AND c.niveleducacional = 'PO') OR ((h.situacao <> 'CS' or h.situacao is null) AND c.niveleducacional <> 'PO')) ");
//		if (!alunoNaoCursouDisciplinaFiltroRelVO.getMatriculaAtiva()) {
//			sb.append(" AND h.situacao <> 'CS' ) ");
//		} else {
//			sb.append(" ) ");
//		}
//		sb.append(" OR h.situacao is null OR h.codigo is null) ");
		if (alunoNaoCursouDisciplinaFiltroRelVO.getTurmaVO().getCodigo() != null && alunoNaoCursouDisciplinaFiltroRelVO.getTurmaVO().getCodigo() != 0) {
			sb.append("AND mp.turma = ");
			sb.append(alunoNaoCursouDisciplinaFiltroRelVO.getTurmaVO().getCodigo() + " ");
		}
		if (alunoNaoCursouDisciplinaFiltroRelVO.getCursoVO().getCodigo() != null && alunoNaoCursouDisciplinaFiltroRelVO.getCursoVO().getCodigo() != 0) {
			sb.append("AND m.curso = ");
			sb.append(alunoNaoCursouDisciplinaFiltroRelVO.getCursoVO().getCodigo() + " ");
		}
		sb.append("GROUP BY m.matricula , p.nome , p.cpf, p.telefoneres , p.telefonecomer , p.telefonerecado , p.celular , c.nome ,  ue.nome, p.email, m.anoingresso ");
		sb.append("ORDER BY p.nome");
		return sb;
	}

	public void montaDadosConsulta(SqlRowSet sqlRowSet, AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) {
		while (sqlRowSet.next()) {
			alunoNaoCursouDisciplinaFiltroRelVO.getAlunoNaoCursouDisciplinaRelVOs().add(montarDados(sqlRowSet));
		}
	}

	public AlunoNaoCursouDisciplinaRelVO montarDados(SqlRowSet sqlRowSet) {
		AlunoNaoCursouDisciplinaRelVO alunoNaoCursouDisciplinaRelVO = new AlunoNaoCursouDisciplinaRelVO();
		alunoNaoCursouDisciplinaRelVO.setAluno(sqlRowSet.getString("pessoa_nome"));
		alunoNaoCursouDisciplinaRelVO.setAnoMatricula(sqlRowSet.getString("matricula_anoingresso"));
		alunoNaoCursouDisciplinaRelVO.setCelular(sqlRowSet.getString("pessoa_celular"));
		alunoNaoCursouDisciplinaRelVO.setCpf(sqlRowSet.getString("pessoa_cpf"));
		alunoNaoCursouDisciplinaRelVO.setCurso(sqlRowSet.getString("curso_nome"));
		alunoNaoCursouDisciplinaRelVO.setEmail(sqlRowSet.getString("pessoa_email"));
		alunoNaoCursouDisciplinaRelVO.setMatricula(sqlRowSet.getString("matricula_matricula"));
		alunoNaoCursouDisciplinaRelVO.setTelefoneComer(sqlRowSet.getString("pessoa_telefoneComer"));
		alunoNaoCursouDisciplinaRelVO.setTelefoneRec(sqlRowSet.getString("pessoa_telefoneRecado"));
		alunoNaoCursouDisciplinaRelVO.setTelefoneRes(sqlRowSet.getString("pessoa_telefoneRes"));
		alunoNaoCursouDisciplinaRelVO.setUnidadeEnsino(sqlRowSet.getString("unidadeEnsino_nome"));
		return alunoNaoCursouDisciplinaRelVO;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("AlunoNaoCursouDisciplinaRel");
	}
}
