package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import relatorio.negocio.comuns.academico.AlunosAtivosInativosVO;
import relatorio.negocio.comuns.academico.QuadroAlunosAtivosInativosRelVO;
import relatorio.negocio.interfaces.academico.QuadroAlunosAtivosInativosRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class QuadroAlunosAtivosInativosRel extends SuperRelatorio implements QuadroAlunosAtivosInativosRelInterfaceFacade {

	public QuadroAlunosAtivosInativosRel() {

	}

	public static void validarDados(UnidadeEnsinoVO unidade, String ano, String semestre) throws ConsistirException {
		if (unidade == null || unidade.getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
		}
		// if (ano == "" || semestre == "") {
		// throw new ConsistirException("O ano e o semestre devem ser informados
		// para a geração do relatório.");
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.
	 * QuadroAlunosAtivosInativosRelInterfaceFacade#criarObjeto(java.lang.
	 * String, java.lang.String)
	 */
	public List<QuadroAlunosAtivosInativosRelVO> criarObjeto(String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCurso, TurmaVO turma) throws Exception {
		QuadroAlunosAtivosInativosRelVO quadroAlunosAtivosInativosRelVO = new QuadroAlunosAtivosInativosRelVO();
		List<QuadroAlunosAtivosInativosRelVO> listaRelatorio = new ArrayList<QuadroAlunosAtivosInativosRelVO>(0);
		quadroAlunosAtivosInativosRelVO.setAlunosAtivosInativosVOs(montarListaAlunosAtivosInativos(ano, semestre, unidadeEnsinoVO, unidadeEnsinoCurso, turma));
		for (AlunosAtivosInativosVO alunosAtivosInativosVO : quadroAlunosAtivosInativosRelVO.getAlunosAtivosInativosVOs()) {
			if (alunosAtivosInativosVO.getSituacao().equals(SituacaoVinculoMatricula.ATIVA.getDescricao()) || alunosAtivosInativosVO.getSituacao().equals(SituacaoVinculoMatricula.FINALIZADA.getDescricao())) {
				quadroAlunosAtivosInativosRelVO.setNrAlunosAtivos(quadroAlunosAtivosInativosRelVO.getNrAlunosAtivos() + alunosAtivosInativosVO.getNrAlunosTotal());
			} else {
				quadroAlunosAtivosInativosRelVO.setNrAlunosInativos(quadroAlunosAtivosInativosRelVO.getNrAlunosInativos() + alunosAtivosInativosVO.getNrAlunosTotal());
			}
			quadroAlunosAtivosInativosRelVO.setNrTotalAlunos(quadroAlunosAtivosInativosRelVO.getNrTotalAlunos() + alunosAtivosInativosVO.getNrAlunosTotal());
		}
		quadroAlunosAtivosInativosRelVO.setAno(ano);
		quadroAlunosAtivosInativosRelVO.setSemestre(semestre);
		if (!unidadeEnsinoVO.getCodigo().equals(0) && unidadeEnsinoVO.getNome().equals("")) {
			UnidadeEnsinoVO unidadeEnsinoVO2 = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(unidadeEnsinoVO.getCodigo(), false, null);
			quadroAlunosAtivosInativosRelVO.setUnidadeEnsino(unidadeEnsinoVO2.getNome());
		}
		quadroAlunosAtivosInativosRelVO.setCurso(unidadeEnsinoCurso.getCurso().getNome());
		quadroAlunosAtivosInativosRelVO.setTurma(turma.getIdentificadorTurma());
		listaRelatorio.add(quadroAlunosAtivosInativosRelVO);
		return listaRelatorio;
	}

	private List<AlunosAtivosInativosVO> montarListaAlunosAtivosInativos(String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCurso, TurmaVO turma) throws Exception {
		List<AlunosAtivosInativosVO> lista = montarListaNrAlunosMatriculados(ano, semestre, unidadeEnsinoVO, unidadeEnsinoCurso, turma);
		if (lista.isEmpty()) {
			throw new ConsistirException("Não existem dados a serem exibidos com os parâmetros de pesquisa acima.");
		}
		return lista;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.
	 * QuadroAlunosAtivosInativosRelInterfaceFacade#
	 * montarListaNrAlunosMatriculados (java.lang.String, java.lang.String)
	 */
	public List<AlunosAtivosInativosVO> montarListaNrAlunosMatriculados(String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCurso, TurmaVO turma) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT consulta.identificadorturma, consulta.situacao, count(consulta.matricula) AS contador FROM ");
		sqlStr.append("( SELECT distinct turma.identificadorturma, matriculaperiodo.situacaomatriculaperiodo as situacao, matricula.matricula, matriculaperiodo.ano, matriculaperiodo.semestre FROM matriculaperiodo ");
		sqlStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodo.turma INNER JOIN matricula ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append("INNER JOIN curso ON matricula.curso = curso.codigo WHERE 1=1 ");
		if (!ano.equalsIgnoreCase("")) {
			sqlStr.append("AND matriculaperiodo.ano = '" + ano + "' ");
		}
		if (!semestre.equalsIgnoreCase("")) {
			sqlStr.append("AND matriculaperiodo.semestre = '" + semestre + "' ");
		}
		if (!(unidadeEnsinoVO == null || unidadeEnsinoVO.getCodigo() == null || unidadeEnsinoVO.getCodigo().intValue() == 0)) {
			sqlStr.append("AND matricula.unidadeEnsino = " + unidadeEnsinoVO.getCodigo().intValue() + " ");
		}
		if (unidadeEnsinoCurso.getCurso().getCodigo() != 0) {
			sqlStr.append(" AND curso.codigo = ").append(unidadeEnsinoCurso.getCurso().getCodigo()).append(" ");
		}
		if (turma.getCodigo() != 0) {
			sqlStr.append(" AND turma.codigo = ").append(turma.getCodigo()).append(" ");
		}
		sqlStr.append(" AND matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC') ");

		sqlStr.append(") AS consulta ");
		sqlStr.append(" GROUP BY consulta.identificadorturma, consulta.situacao ORDER BY consulta.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarLista(tabelaResultado);
	}

	private List<AlunosAtivosInativosVO> montarLista(SqlRowSet tabelaResultado) throws Exception {
		List<AlunosAtivosInativosVO> alunosAtivosInativosVOs = new ArrayList<AlunosAtivosInativosVO>(0);
		while (tabelaResultado.next()) {
			alunosAtivosInativosVOs.add(montarDados(tabelaResultado));
		}
		return alunosAtivosInativosVOs;
	}

	private AlunosAtivosInativosVO montarDados(SqlRowSet tabelaResultado) throws Exception {
		AlunosAtivosInativosVO alunosAtivosInativosVO = new AlunosAtivosInativosVO();
		alunosAtivosInativosVO.setNomeTurma(tabelaResultado.getString("identificadorturma"));
		alunosAtivosInativosVO.setSituacao(SituacaoVinculoMatricula.getDescricao(tabelaResultado.getString("situacao")));
		alunosAtivosInativosVO.setNrAlunosTotal(tabelaResultado.getInt("contador"));
		return alunosAtivosInativosVO;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("QuadroAlunosAtivosInativosRel");
	}

}
