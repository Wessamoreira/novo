package negocio.facade.jdbc.ead;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ParametrosGraficoComparativoAvaliacoesOnlinesVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ParametrosGraficoComparativoAvaliacoesOnlinesInterfaceFacade;

/**
 * @author Victor Hugo 07/04/2015
 */
@Repository
@Scope("singleton")
@Lazy
public class ParametrosGraficoComparativoAvaliacoesOnlines extends ControleAcesso implements ParametrosGraficoComparativoAvaliacoesOnlinesInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<ParametrosGraficoComparativoAvaliacoesOnlinesVO> consultarPercentualAcertosComparativoAvaliacoesOnlines(Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoTemaAssunto, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		
		sqlStr.append(" select sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) as acertou, ");
		sqlStr.append(" sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ERROU', 'NAO_RESPONDIDA') then 1 else  0 end) as errou, ");
		sqlStr.append(" count(questao.codigo) as qtdequestao, avaliacaoonline.parametromonitoramentoavaliacaoonline, avaliacaoonlinematricula.datatermino,");
		sqlStr.append(" (sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) *100)/count(questao.codigo) as percentualacertou");
		sqlStr.append(" from avaliacaoonlinematriculaquestao  ");
		sqlStr.append(" inner join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = avaliacaoonlinematriculaquestao.avaliacaoonlinematricula ");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = avaliacaoonlinematricula.avaliacaoonline ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo ");
		sqlStr.append(" inner join questao on questao.codigo = avaliacaoonlinematriculaquestao.questao ");
		sqlStr.append(" inner join questaoassunto on questao.codigo = questaoassunto.questao and unidadeconteudo.temaassunto = questaoassunto.temaassunto ");
		sqlStr.append(" inner join temaassunto on temaassunto.codigo = questaoassunto.temaassunto ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		if(!codigoTemaAssunto.equals(0)) {
			sqlStr.append(" and temaassunto.codigo = ").append(codigoTemaAssunto);			
		}
		sqlStr.append(" group by avaliacaoonlinematricula.datatermino, avaliacaoonline.parametromonitoramentoavaliacaoonline, avaliacaoonlinematricula.codigo");
		sqlStr.append(" order by avaliacaoonlinematricula.datatermino");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		ParametrosGraficoComparativoAvaliacoesOnlinesVO graficoComparativoAvaliacoesOnlinesVO = null;
		List<ParametrosGraficoComparativoAvaliacoesOnlinesVO> graficoComparativoAvaliacoesOnlinesVOs = new ArrayList<ParametrosGraficoComparativoAvaliacoesOnlinesVO>();
		while(rs.next()) {
			graficoComparativoAvaliacoesOnlinesVO = new ParametrosGraficoComparativoAvaliacoesOnlinesVO();
			graficoComparativoAvaliacoesOnlinesVO.setPercentuaisEvolucaoAvaliacoesOnline(rs.getDouble("percentualacertou"));
			graficoComparativoAvaliacoesOnlinesVO.setDataTermino(rs.getDate("datatermino"));
			graficoComparativoAvaliacoesOnlinesVOs.add(graficoComparativoAvaliacoesOnlinesVO);
		}
		return graficoComparativoAvaliacoesOnlinesVOs;
	}
}
