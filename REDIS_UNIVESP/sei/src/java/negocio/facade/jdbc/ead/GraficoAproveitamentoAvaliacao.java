package negocio.facade.jdbc.ead;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.GraficoAproveitamentoAvaliacaoInterfaceFacade;

/**
 * @author Victor Hugo 08/04/2015
 */
@Repository
@Scope("singleton")
@Lazy
public class GraficoAproveitamentoAvaliacao extends ControleAcesso implements GraficoAproveitamentoAvaliacaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<GraficoAproveitamentoAvaliacaoVO> consultarAproveitamentoAvaliacaoOnlineAluno(Integer codigoAvaliacaoOnlineMatricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select row_number()over(order by (acertou*100/qtdequestao)) as ordem, estatistica.*, itemParametrosMonitoramentoAvaliacaoOnline.*, (acertou*100/qtdequestao) as percentualacertou from (");
		sqlStr.append(" select temaassunto.nome, temaassunto.codigo as codigo, sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) as acertou,");
		sqlStr.append(" sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ERROU', 'NAO_RESPONDIDA') then 1 else  0 end) as errou,");
		sqlStr.append("	sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') and avaliacaoonlinematriculaquestao.questao = questao.codigo and questao.nivelcomplexidadequestao = 'FACIL' then avaliacaoonline.notaporquestaonivelfacil else");
		sqlStr.append("		case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') and avaliacaoonlinematriculaquestao.questao = questao.codigo and questao.nivelcomplexidadequestao = 'MEDIO' then avaliacaoonline.notaporquestaonivelmedio else");
		sqlStr.append("		case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') and avaliacaoonlinematriculaquestao.questao = questao.codigo and questao.nivelcomplexidadequestao = 'DIFICIL' then avaliacaoonline.notaporquestaoniveldificil end end end ) as pontos,");
		sqlStr.append(" count(questao.codigo) as qtdequestao, avaliacaoonline.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" from avaliacaoonlinematriculaquestao ");
		sqlStr.append(" inner join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = avaliacaoonlinematriculaquestao.avaliacaoonlinematricula");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = avaliacaoonlinematricula.avaliacaoonline");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo");
		sqlStr.append(" inner join questao on questao.codigo = avaliacaoonlinematriculaquestao.questao");
		sqlStr.append(" inner join questaoassunto on questao.codigo = questaoassunto.questao and unidadeconteudo.temaassunto = questaoassunto.temaassunto");
		sqlStr.append(" inner join temaassunto on temaassunto.codigo = questaoassunto.temaassunto");
		sqlStr.append(" where avaliacaoonlinematricula = ").append(codigoAvaliacaoOnlineMatricula).append(" group by temaassunto.nome, temaassunto.codigo, avaliacaoonline.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" union all");
		sqlStr.append(" select 'GERAL' as nome, 0 as codigo, sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) as acertou,");
		sqlStr.append(" sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ERROU', 'NAO_RESPONDIDA') then 1 else  0 end) as errou,");
		sqlStr.append(" avaliacaoonlinematricula.nota as pontos,");
		sqlStr.append(" count(avaliacaoonlinematriculaquestao.codigo) as qtdequestao, avaliacaoonline.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" from avaliacaoonlinematriculaquestao ");
		sqlStr.append(" inner join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = avaliacaoonlinematriculaquestao.avaliacaoonlinematricula");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = avaliacaoonlinematricula.avaliacaoonline");
		sqlStr.append(" where avaliacaoonlinematricula = ").append(codigoAvaliacaoOnlineMatricula);
		sqlStr.append(" group by avaliacaoonline.parametromonitoramentoavaliacaoonline, pontos");
		sqlStr.append(" ) as estatistica");
		sqlStr.append(" inner join itemParametrosMonitoramentoAvaliacaoOnline on itemParametrosMonitoramentoAvaliacaoOnline.parametrosMonitoramentoAvaliacaoOnline = estatistica.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" and itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= (acertou*100/qtdequestao) and percentualAcertosAte >= (acertou*100/qtdequestao)");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO = null;
		List<GraficoAproveitamentoAvaliacaoVO> graficoAproveitamentoAvaliacaoVOs = new ArrayList<GraficoAproveitamentoAvaliacaoVO>();
		while (rs.next()) {
			graficoAproveitamentoAvaliacaoVO = new GraficoAproveitamentoAvaliacaoVO();
			graficoAproveitamentoAvaliacaoVO.setAssunto(rs.getString("nome"));
			graficoAproveitamentoAvaliacaoVO.setCodigoTemaAssunto(rs.getInt("codigo"));
			graficoAproveitamentoAvaliacaoVO.setOrdem(rs.getInt("ordem"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeAcertos(rs.getInt("acertou"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeErros(rs.getInt("errou"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeQuestoes(rs.getInt("qtdequestao"));
			graficoAproveitamentoAvaliacaoVO.setDescricaoParametro(rs.getString("descricaoparametro"));
			graficoAproveitamentoAvaliacaoVO.setCorGrafico(rs.getString("corgrafico"));
			graficoAproveitamentoAvaliacaoVO.setCorLetraGrafico(rs.getString("corletragrafico"));
			graficoAproveitamentoAvaliacaoVO.setPercentualAcerto(rs.getDouble("percentualacertou"));
			graficoAproveitamentoAvaliacaoVO.setPontos(rs.getDouble("pontos"));
			graficoAproveitamentoAvaliacaoVO.setCodigoAvaliacaoOnlineMatricula(codigoAvaliacaoOnlineMatricula);
			graficoAproveitamentoAvaliacaoVOs.add(graficoAproveitamentoAvaliacaoVO);
		}
		return graficoAproveitamentoAvaliacaoVOs;
	}
	
	@Override
	public List<GraficoAproveitamentoAvaliacaoVO> realizarParametrosGraficoAvaliacaoOnlineMatricula(List<GraficoAproveitamentoAvaliacaoVO> graficoAproveitamentoAvaliacaoVOs, String nomeDisciplina, Integer parametroWidthGrafico) throws Exception {
		for (GraficoAproveitamentoAvaliacaoVO obj : graficoAproveitamentoAvaliacaoVOs) {
			if (obj.getAssunto().equals("GERAL")) {
				obj.setGeral(true);
				obj.setAssunto(nomeDisciplina);
			}
			obj.setParametroWidthGrafico(parametroWidthGrafico);
			obj.setOrdem(obj.getOrdem() + graficoAproveitamentoAvaliacaoVOs.size() + (int) (Math.random() * 1500000));
		}
		return graficoAproveitamentoAvaliacaoVOs;
	}	
	
	@Override
	public GraficoAproveitamentoAvaliacaoVO consultarAproveitamentoAvaliacaoOnlineAlunoPorDisciplina(Integer codigoAvaliacaoOnlineMatricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select row_number()over(order by (acertou*100/qtdequestao)) as ordem, estatistica.*, itemParametrosMonitoramentoAvaliacaoOnline.*, (acertou*100/qtdequestao) as percentualacertou from (");
		sqlStr.append(" select 'GERAL' as nome, 0 as codigo, sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) as acertou,");
		sqlStr.append(" sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ERROU', 'NAO_RESPONDIDA') then 1 else  0 end) as errou,");
		sqlStr.append(" count(avaliacaoonlinematriculaquestao.codigo) as qtdequestao, avaliacaoonline.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" from avaliacaoonlinematriculaquestao ");
		sqlStr.append(" inner join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = avaliacaoonlinematriculaquestao.avaliacaoonlinematricula");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = avaliacaoonlinematricula.avaliacaoonline");
		sqlStr.append(" where avaliacaoonlinematricula = ").append(codigoAvaliacaoOnlineMatricula);
		sqlStr.append(" group by avaliacaoonline.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" ) as estatistica");
		sqlStr.append(" inner join itemParametrosMonitoramentoAvaliacaoOnline on itemParametrosMonitoramentoAvaliacaoOnline.parametrosMonitoramentoAvaliacaoOnline = estatistica.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" and itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= (acertou*100/qtdequestao) and percentualAcertosAte >= (acertou*100/qtdequestao)");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO = null;
		if (rs.next()) {
			graficoAproveitamentoAvaliacaoVO = new GraficoAproveitamentoAvaliacaoVO();
			graficoAproveitamentoAvaliacaoVO.setAssunto(rs.getString("nome"));
			graficoAproveitamentoAvaliacaoVO.setCodigoTemaAssunto(rs.getInt("codigo"));
			graficoAproveitamentoAvaliacaoVO.setOrdem(rs.getInt("ordem"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeAcertos(rs.getInt("acertou"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeErros(rs.getInt("errou"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeQuestoes(rs.getInt("qtdequestao"));
			graficoAproveitamentoAvaliacaoVO.setDescricaoParametro(rs.getString("descricaoparametro"));
			graficoAproveitamentoAvaliacaoVO.setCorGrafico(rs.getString("corgrafico"));
			graficoAproveitamentoAvaliacaoVO.setCorLetraGrafico(rs.getString("corletragrafico"));
			graficoAproveitamentoAvaliacaoVO.setPercentualAcerto(rs.getDouble("percentualacertou"));
			graficoAproveitamentoAvaliacaoVO.setCodigoAvaliacaoOnlineMatricula(codigoAvaliacaoOnlineMatricula);
		}
		return graficoAproveitamentoAvaliacaoVO;
	}
	
	@Override
	public GraficoAproveitamentoAvaliacaoVO consultarAproveitamentoAvaliacaoOnlineAlunoPorAssunto(Integer codigoAvaliacaoOnlineMatricula, Integer codigoTemaAssunto) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select row_number()over(order by (acertou*100/qtdequestao)) as ordem, estatistica.*, itemParametrosMonitoramentoAvaliacaoOnline.*, (acertou*100/qtdequestao) as percentualacertou from (");
		sqlStr.append(" select temaassunto.nome, temaassunto.codigo as codigo, sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) as acertou,");
		sqlStr.append(" sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ERROU', 'NAO_RESPONDIDA') then 1 else  0 end) as errou,");
		sqlStr.append(" count(questao.codigo) as qtdequestao, avaliacaoonline.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" from avaliacaoonlinematriculaquestao ");
		sqlStr.append(" inner join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = avaliacaoonlinematriculaquestao.avaliacaoonlinematricula");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = avaliacaoonlinematricula.avaliacaoonline");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo");
		sqlStr.append(" inner join questao on questao.codigo = avaliacaoonlinematriculaquestao.questao");
		sqlStr.append(" inner join questaoassunto on questao.codigo = questaoassunto.questao and unidadeconteudo.temaassunto = questaoassunto.temaassunto");
		sqlStr.append(" inner join temaassunto on temaassunto.codigo = questaoassunto.temaassunto");
		sqlStr.append(" where avaliacaoonlinematricula = ").append(codigoAvaliacaoOnlineMatricula);
		sqlStr.append(" and temaassunto.codigo = ").append(codigoTemaAssunto);
		sqlStr.append(" group by temaassunto.nome, temaassunto.codigo, avaliacaoonline.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" ) as estatistica");
		sqlStr.append(" inner join itemParametrosMonitoramentoAvaliacaoOnline on itemParametrosMonitoramentoAvaliacaoOnline.parametrosMonitoramentoAvaliacaoOnline = estatistica.parametromonitoramentoavaliacaoonline");
		sqlStr.append(" and itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= (acertou*100/qtdequestao) and percentualAcertosAte >= (acertou*100/qtdequestao)");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO = null;
		if (rs.next()) {
			graficoAproveitamentoAvaliacaoVO = new GraficoAproveitamentoAvaliacaoVO();
			graficoAproveitamentoAvaliacaoVO.setAssunto(rs.getString("nome"));
			graficoAproveitamentoAvaliacaoVO.setCodigoTemaAssunto(rs.getInt("codigo"));
			graficoAproveitamentoAvaliacaoVO.setOrdem(rs.getInt("ordem"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeAcertos(rs.getInt("acertou"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeErros(rs.getInt("errou"));
			graficoAproveitamentoAvaliacaoVO.setQuantidadeQuestoes(rs.getInt("qtdequestao"));
			graficoAproveitamentoAvaliacaoVO.setDescricaoParametro(rs.getString("descricaoparametro"));
			graficoAproveitamentoAvaliacaoVO.setCorGrafico(rs.getString("corgrafico"));
			graficoAproveitamentoAvaliacaoVO.setCorLetraGrafico(rs.getString("corletragrafico"));
			graficoAproveitamentoAvaliacaoVO.setPercentualAcerto(rs.getDouble("percentualacertou"));
			graficoAproveitamentoAvaliacaoVO.setCodigoAvaliacaoOnlineMatricula(codigoAvaliacaoOnlineMatricula);
		}
		return graficoAproveitamentoAvaliacaoVO;
	}
}
