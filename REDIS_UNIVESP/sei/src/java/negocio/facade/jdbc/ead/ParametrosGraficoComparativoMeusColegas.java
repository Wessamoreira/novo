package negocio.facade.jdbc.ead;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ParametrosGraficoComparativoMeusColegasVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ParametrosGraficoComparativoMeusColegasInterfaceFacade;

/**
 * @author Victor Hugo 08/04/2015
 */
@Repository
@Scope("singleton")
@Lazy
public class ParametrosGraficoComparativoMeusColegas extends ControleAcesso implements ParametrosGraficoComparativoMeusColegasInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<ParametrosGraficoComparativoMeusColegasVO> consultarParametrosGraficoComparativoMeusColegasVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoCurso, Integer codigoTemaAssunto, UsuarioVO usuarioVO) throws Exception {
		matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append("	select (sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) *100)/count(mptd.codigo) as percentualacertou ");
		sqlStr.append("	from matriculaperiodoturmadisciplina mptd");
		sqlStr.append("	inner join avaliacaoonlinematricula on mptd.codigo = avaliacaoonlinematricula.matriculaperiodoturmadisciplina");
		sqlStr.append("	inner join avaliacaoonlinematriculaquestao on avaliacaoonlinematriculaquestao.avaliacaoonlinematricula = avaliacaoonlinematricula.codigo");
		sqlStr.append("	inner join questao on questao.codigo = avaliacaoonlinematriculaquestao.questao");
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append("	inner join questaoassunto on questaoassunto.questao = questao.codigo");
		}
		sqlStr.append("	where mptd.turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
		sqlStr.append("	and mptd.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("'");
		sqlStr.append("	and mptd.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("'");
		sqlStr.append("	and mptd.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
		sqlStr.append("	and mptd.codigo != ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append("	and questaoassunto.temaassunto = ").append(codigoTemaAssunto);
		}
		sqlStr.append("	and mptd.conteudo = ").append(matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo());
		sqlStr.append("	union all");
		sqlStr.append("	select (sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) *100)/count(mptd.codigo) as percentualacertou ");
		sqlStr.append("	from matriculaperiodoturmadisciplina mptd");
		sqlStr.append("	inner join avaliacaoonlinematricula on mptd.codigo = avaliacaoonlinematricula.matriculaperiodoturmadisciplina");
		sqlStr.append("	inner join avaliacaoonlinematriculaquestao on avaliacaoonlinematriculaquestao.avaliacaoonlinematricula = avaliacaoonlinematricula.codigo");
		sqlStr.append("	inner join turma on turma.codigo = mptd.turma");
		sqlStr.append("	inner join questao on questao.codigo = avaliacaoonlinematriculaquestao.questao");
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append("	inner join questaoassunto on questaoassunto.questao = questao.codigo");
		}
		sqlStr.append("	where turma.curso = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCurso().getCodigo());
		sqlStr.append("	and mptd.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("'");
		sqlStr.append("	and mptd.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("'");
		sqlStr.append("	and mptd.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
		sqlStr.append("	and mptd.codigo != ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append("	and questaoassunto.temaassunto = ").append(codigoTemaAssunto);
		}
		sqlStr.append("	and mptd.conteudo = ").append(matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo());
		sqlStr.append("	union all");
		sqlStr.append("	select (sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) *100)/count(mptd.codigo) as percentualacertou ");
		sqlStr.append("	from matriculaperiodoturmadisciplina mptd");
		sqlStr.append("	inner join avaliacaoonlinematricula on mptd.codigo = avaliacaoonlinematricula.matriculaperiodoturmadisciplina");
		sqlStr.append("	inner join avaliacaoonlinematriculaquestao on avaliacaoonlinematriculaquestao.avaliacaoonlinematricula = avaliacaoonlinematricula.codigo");
		sqlStr.append("	inner join turma on turma.codigo = mptd.turma");
		sqlStr.append("	inner join questao on questao.codigo = avaliacaoonlinematriculaquestao.questao");
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append("	inner join questaoassunto on questaoassunto.questao = questao.codigo");
		}
		sqlStr.append("	where mptd.conteudo = ").append(matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo());
		sqlStr.append("	and mptd.ano = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("'");
		sqlStr.append("	and mptd.semestre = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("'");
		sqlStr.append("	and mptd.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
		sqlStr.append("	and mptd.codigo != ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append("	and questaoassunto.temaassunto = ").append(codigoTemaAssunto);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		ParametrosGraficoComparativoMeusColegasVO parametrosGraficoComparativoMeusColegasVO = null;
		List<ParametrosGraficoComparativoMeusColegasVO> parametrosGraficoComparativoMeusColegasVOs = new ArrayList<ParametrosGraficoComparativoMeusColegasVO>();
		while (rs.next()) {
			parametrosGraficoComparativoMeusColegasVO = new ParametrosGraficoComparativoMeusColegasVO();
			parametrosGraficoComparativoMeusColegasVO.setPercentualAcertou(rs.getDouble("percentualacertou"));
			parametrosGraficoComparativoMeusColegasVOs.add(parametrosGraficoComparativoMeusColegasVO);
		}
		return parametrosGraficoComparativoMeusColegasVOs;
	}
}
