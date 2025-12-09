package relatorio.negocio.jdbc.processosel;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.enumeradores.TipoLayoutApresentacaoResultadoPerguntaEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.processosel.RespostaPergunta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstucionalRelVO;
import relatorio.negocio.comuns.avaliacaoInst.PerguntaRelVO;
import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;
import relatorio.negocio.comuns.avaliacaoInst.RespostaRelVO;
import relatorio.negocio.interfaces.processosel.ResultadoQuestionarioProcessoSeletivoRelInterfaceFacade;

@Service
public class ResultadoQuestionarioProcessoSeletivoRel extends ControleAcesso implements ResultadoQuestionarioProcessoSeletivoRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1242237551184000722L;

	@Override
	public AvaliacaoInstucionalRelVO consultarDadosGeracaoRelatorio(Integer processoSeletivo, Integer unidadeEnsinoCurso, Integer dataProva, Integer sala, Boolean trazerSomenteCandidatosConfirmados, UsuarioVO usuario) throws Exception {
		QuestionarioRelVO questionarioRelVO = consultarDadosGeracaoRelatorioGrafico(processoSeletivo, unidadeEnsinoCurso, dataProva, sala, trazerSomenteCandidatosConfirmados, usuario);
		if (questionarioRelVO.getCodigo() > 0) {

			AvaliacaoInstucionalRelVO avaliacaoInstucionalRelVO = new AvaliacaoInstucionalRelVO();
			avaliacaoInstucionalRelVO.getQuestionarioRelVOs().add(questionarioRelVO);
			return avaliacaoInstucionalRelVO;
		}
		return null;
	}

	public QuestionarioRelVO consultarDadosGeracaoRelatorioGrafico(Integer processoSeletivo, Integer unidadeEnsinoCurso, Integer dataProva, Integer sala, Boolean trazerSomenteCandidatosConfirmados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" Select count(RespostaAvaliacaoInstitucionalDW.pessoa) as totalPessoa, perguntaquestionario.codigo, resposta, pergunta.codigo AS \"pergunta\", Questionario.codigo as questionario, ");
		sqlStr.append(" Questionario.escopo as questionario_escopo, questionario.descricao as questionario_descricao, Pergunta.descricao as Pergunta_descricao, Pergunta.tipoResposta as Pergunta_tipoResposta, ");
		sqlStr.append(" pergunta.tipoResultadoGrafico, perguntaquestionario.ordem as ordem, array_to_string(array_agg(''||RespostaAvaliacaoInstitucionalDW.respostaadicional ||''), ';') as respostasAdicionais");
		sqlStr.append(" from RespostaAvaliacaoInstitucionalDW ");
		sqlStr.append(" inner join Questionario on Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario");
		sqlStr.append(" inner join Pergunta on Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta");
		sqlStr.append(" inner join perguntaquestionario on perguntaquestionario.pergunta = pergunta.codigo and perguntaquestionario.questionario = questionario.codigo ");
		sqlStr.append(" inner join inscricao on RespostaAvaliacaoInstitucionalDW.inscricaoprocessoseletivo = inscricao.codigo ");
		sqlStr.append(" where  RespostaAvaliacaoInstitucionalDW.processoSeletivo = " + processoSeletivo.intValue());
		if (trazerSomenteCandidatosConfirmados != null && trazerSomenteCandidatosConfirmados) {
			sqlStr.append(" AND inscricao.situacao = 'CO' ");
		}
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso.intValue() != 0) {
			sqlStr.append(" and inscricao.cursoopcao1 = ").append(unidadeEnsinoCurso);
		}
		if (dataProva != null && dataProva.intValue() != 0) {
			sqlStr.append(" and inscricao.itemprocessoseletivodataprova = ").append(dataProva);
		}
		if (dataProva != null && sala != null && sala.intValue() != 0) {
			if (sala < 0) {
				sqlStr.append(" and inscricao.sala is null ");
			} else {
				sqlStr.append(" and inscricao.sala = ").append(sala);
			}
		}
		sqlStr.append(" group by resposta, pergunta.codigo, questionario_escopo, ");
		sqlStr.append(" questionario_descricao, Pergunta.descricao, Pergunta_tipoResposta,  ");
		sqlStr.append(" Questionario.codigo, questionario_descricao, perguntaquestionario.codigo, tipoResultadoGrafico, perguntaquestionario.ordem ");
		sqlStr.append(" order by perguntaquestionario.ordem ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		QuestionarioRelVO questionarioRelVO = montarDadosConsulta(tabelaResultado);
		if (questionarioRelVO == null) {
			questionarioRelVO = new QuestionarioRelVO();
		}
		PerguntaRelVO perguntaSexo = realizarCriacaoPerguntaSexo(processoSeletivo, unidadeEnsinoCurso, dataProva, sala, trazerSomenteCandidatosConfirmados);
		if (perguntaSexo != null) {
			questionarioRelVO.getPerguntaRelVOs().add(0, perguntaSexo);
		}
		PerguntaRelVO perguntaCidade = realizarCriacaoPerguntaCidade(processoSeletivo, unidadeEnsinoCurso, dataProva, sala, trazerSomenteCandidatosConfirmados);
		if (perguntaCidade != null) {
			questionarioRelVO.getPerguntaRelVOs().add(1, perguntaCidade);
		}
		for (PerguntaRelVO perguntaRelVO : questionarioRelVO.getPerguntaRelVOs()) {
			Ordenacao.ordenarLista(perguntaRelVO.getRespostaTexto(), "ordem");
		}
		Ordenacao.ordenarLista(questionarioRelVO.getPerguntaRelVOs(), "nrPergunta");
		int index = 1;
		for (PerguntaRelVO perguntaRelVO : questionarioRelVO.getPerguntaRelVOs()) {
			Integer qtdeResposta = 0;
			perguntaRelVO.setNrPergunta(index);
			for (RespostaRelVO respostaRelVO : perguntaRelVO.getRespostaTexto()) {
				qtdeResposta += respostaRelVO.getQuantidadePessoa();
			}
			for (RespostaRelVO respostaRelVO : perguntaRelVO.getRespostaTexto()) {
				respostaRelVO.setTotalPessoas(qtdeResposta);
			}
			index++;
		}

		return questionarioRelVO;
	}

	public PerguntaRelVO realizarCriacaoPerguntaSexo(Integer processoSeletivo, Integer unidadeEnsinoCurso, Integer dataProva, Integer sala, Boolean trazerSomenteCandidatosConfirmados) {
		StringBuilder sb = new StringBuilder("SELECT candidato.sexo, count(distinct candidato.codigo) as quantidade ");
		sb.append(" from inscricao ");
		sb.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sb.append(" and unidadeensinocursoopcao1.codigo = ").append(unidadeEnsinoCurso);
		}
		sb.append(" left join SalaLocalAula as sala on sala.codigo = inscricao.sala ");
		sb.append(" left join LocalAula as local on local.codigo = sala.LocalAula ");
		sb.append(" inner join ItemProcSeletivoDataProva on  ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		if (dataProva != null && dataProva > 0) {
			sb.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProva);
		}
		sb.append(" where 1=1 ");
		if (trazerSomenteCandidatosConfirmados != null && trazerSomenteCandidatosConfirmados) {
			sb.append(" AND inscricao.situacao = 'CO' ");
		}
		if (processoSeletivo > 0) {
			sb.append(" and procseletivo.codigo = ").append(processoSeletivo);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala > 0) {

			sb.append(" and  sala.codigo =  ").append(sala);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala < 0) {
			sb.append(" and  inscricao.sala is null  ");
		}
		sb.append(" group by sexo order by case sexo when 'M' then 1 when 'F' then 2 else 3 end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		PerguntaRelVO perguntaRelVO = null;
		while (rs.next()) {
			if (perguntaRelVO == null) {
				perguntaRelVO = new PerguntaRelVO();
				perguntaRelVO.setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_PIZZA);
				perguntaRelVO.setNome("Estatística Por Sexo");
				perguntaRelVO.setNrPergunta(-2);
				perguntaRelVO.setCodigo(1000);
				perguntaRelVO.setTipoResposta("SE");
			}
			perguntaRelVO.setTotalPessoas(perguntaRelVO.getTotalPessoas() + rs.getInt("quantidade"));
			RespostaRelVO respostaRelVO = new RespostaRelVO();
			if (rs.getString("sexo") != null && rs.getString("sexo").equals("M")) {
				respostaRelVO.setApresentarRespota(true);
				respostaRelVO.setNomeResposta("Masculino");
				respostaRelVO.setQuantidadePessoa(rs.getInt("quantidade"));
				// respostaRelVO.setResposta("Masculino");
				respostaRelVO.setSiglaResposta("A");
				respostaRelVO.setOrdem(1);
			} else if (rs.getString("sexo") != null && rs.getString("sexo").equals("F")) {
				respostaRelVO.setApresentarRespota(true);
				respostaRelVO.setNomeResposta("Feminino");
				respostaRelVO.setQuantidadePessoa(rs.getInt("quantidade"));
				// respostaRelVO.setResposta("Feminino");
				respostaRelVO.setSiglaResposta("B");
				respostaRelVO.setOrdem(2);
			} else if (rs.getString("sexo") != null && rs.getString("sexo").equals("")) {
				respostaRelVO.setApresentarRespota(true);
				respostaRelVO.setNomeResposta("Não Informado");
				respostaRelVO.setQuantidadePessoa(rs.getInt("quantidade"));
				// respostaRelVO.setResposta("Não Informado");
				respostaRelVO.setSiglaResposta("C");
				respostaRelVO.setOrdem(3);
			}
			perguntaRelVO.getRespostaTexto().add(respostaRelVO);
		}
		return perguntaRelVO;
	}

	public PerguntaRelVO realizarCriacaoPerguntaCidade(Integer processoSeletivo, Integer unidadeEnsinoCurso, Integer dataProva, Integer sala, Boolean trazerSomenteCandidatosConfirmados) {
		StringBuilder sb = new StringBuilder("SELECT cidade.nome||' - '||estado.sigla as cidade, count(distinct candidato.codigo) as quantidade ");
		sb.append(" from inscricao ");
		sb.append(" inner join pessoa as candidato on candidato.codigo = inscricao.candidato ");
		sb.append(" left join cidade  on candidato.cidade = cidade.codigo ");
		sb.append(" left join estado  on cidade.estado = estado.codigo ");
		sb.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sb.append(" inner join unidadeensinocurso as unidadeensinocursoopcao1 on unidadeensinocursoopcao1.codigo = inscricao.cursoopcao1 ");
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sb.append(" and unidadeensinocursoopcao1.codigo = ").append(unidadeEnsinoCurso);
		}
		sb.append(" left join SalaLocalAula as sala on sala.codigo = inscricao.sala ");
		sb.append(" left join LocalAula as local on local.codigo = sala.LocalAula ");
		sb.append(" inner join ItemProcSeletivoDataProva on  ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		if (dataProva != null && dataProva > 0) {
			sb.append(" and ItemProcSeletivoDataProva.codigo = ").append(dataProva);
		}
		sb.append(" where 1=1 ");
		if (trazerSomenteCandidatosConfirmados != null && trazerSomenteCandidatosConfirmados) {
			sb.append(" AND inscricao.situacao = 'CO' ");
		}
		if (processoSeletivo > 0) {
			sb.append(" and procseletivo.codigo = ").append(processoSeletivo);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala > 0) {

			sb.append(" and  sala.codigo =  ").append(sala);
		}
		if (dataProva != null && dataProva > 0 && sala != null && sala < 0) {
			sb.append(" and  inscricao.sala is null  ");
		}
		sb.append(" group by cidade.nome, estado.sigla order by cidade ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		PerguntaRelVO perguntaRelVO = null;
		int index = 1;
		while (rs.next()) {
			if (perguntaRelVO == null) {
				perguntaRelVO = new PerguntaRelVO();
				perguntaRelVO.setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum.LISTA);
				perguntaRelVO.setNome("Estatística Por Cidade");
				perguntaRelVO.setNrPergunta(-1);

				perguntaRelVO.setCodigo(2000);
				perguntaRelVO.setTipoResposta("SE");
			}
			perguntaRelVO.setTotalPessoas(perguntaRelVO.getTotalPessoas() + rs.getInt("quantidade"));
			RespostaRelVO respostaRelVO = new RespostaRelVO();
			if (rs.getString("cidade") != null && !rs.getString("cidade").equals("")) {
				respostaRelVO.setApresentarRespota(true);
				respostaRelVO.setNomeResposta(rs.getString("cidade"));
				respostaRelVO.setQuantidadePessoa(rs.getInt("quantidade"));
				// respostaRelVO.setResposta(rs.getString("cidade"));
				respostaRelVO.setSiglaResposta(index + "");
				respostaRelVO.setOrdem(index);
			} else if (rs.getString("cidade") == null || rs.getString("cidade").equals("")) {
				respostaRelVO.setApresentarRespota(true);
				respostaRelVO.setNomeResposta("Não Informado");
				respostaRelVO.setQuantidadePessoa(rs.getInt("quantidade"));
				// respostaRelVO.setResposta("Não Informado");
				respostaRelVO.setSiglaResposta("N");
				respostaRelVO.setOrdem(index);
			}
			index++;
			perguntaRelVO.getRespostaTexto().add(respostaRelVO);
		}
		return perguntaRelVO;
	}

	@SuppressWarnings("unchecked")
	private QuestionarioRelVO montarDadosConsulta(SqlRowSet dadosSQL) throws Exception {
		QuestionarioRelVO obj = null;
		while (dadosSQL.next()) {
			if (obj == null) {
				obj = new QuestionarioRelVO();
				obj.setNome(dadosSQL.getString("questionario_descricao"));
				obj.setCodigo(dadosSQL.getInt("questionario"));
				obj.setEscopo(dadosSQL.getString("questionario_escopo"));
			}
			PerguntaRelVO pergunta = obj.consultarPerguntaRelVOs(new Integer(dadosSQL.getInt("pergunta")));
			if (pergunta.getCodigo().intValue() == 0) {
				pergunta.setNome(dadosSQL.getString("Pergunta_descricao"));
				pergunta.setCodigo(dadosSQL.getInt("pergunta"));
				pergunta.setNrPergunta(dadosSQL.getInt("ordem"));
				pergunta.setRespostaPerguntaVOs(RespostaPergunta.consultarRespostaPerguntas(pergunta.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				pergunta.setTipoResposta(dadosSQL.getString("Pergunta_tipoResposta"));
				if (dadosSQL.getString("tipoResultadoGrafico") != null) {
					pergunta.setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum.valueOf(dadosSQL.getString("tipoResultadoGrafico")));
				}
			}
			RespostaRelVO resposta = new RespostaRelVO();
			resposta.setResposta(dadosSQL.getString("resposta"));
			resposta.setListaRespostaAgrupadas(pergunta.consultarCodigoResposta(resposta.getResposta()));
			resposta.setAgruparResposta(pergunta.consultarAgrupadorResposta(resposta.getResposta()));
			resposta.setQuantidadePessoa(dadosSQL.getInt("totalPessoa"));
			resposta.setNomeRespostaAdicional(dadosSQL.getString("respostasAdicionais"));
			List<String> respostasAdicionais = new ArrayList<String>();
			respostasAdicionais= Arrays.asList(resposta.getNomeRespostaAdicional().split("\\s*;\\s*"));
			if (!respostasAdicionais.isEmpty()) {
				for (String respostaAdicional : respostasAdicionais) {
					if (Uteis.isAtributoPreenchido(respostaAdicional)) {
						RespostaRelVO respostaRelVO = new RespostaRelVO();
						respostaAdicional = respostaAdicional.replace(respostaAdicional.substring(0, respostaAdicional.indexOf("{") + 1), "");
						respostaAdicional = respostaAdicional.replace("}", "");
						respostaRelVO.setResposta(respostaAdicional);
						resposta.getListaRespostasAdicionais().add(respostaRelVO);
					}
				}
			}
			if ((pergunta.getTipoResposta().equals("TE") || pergunta.getTipoResposta().equals("SE")) && !dadosSQL.getString("resposta").trim().isEmpty()) {
				pergunta.adicionarRespostaPergunta(resposta, resposta.getNomeRespostaAdicional());
			} else if (!dadosSQL.getString("resposta").trim().isEmpty()) {
				pergunta.adicionarPerguntaMultiplaEscolha(resposta.getResposta(), resposta.getQuantidadePessoa(), resposta.getNomeRespostaAdicional());
			}
			if (!pergunta.getRespostaTexto().isEmpty()) {
				obj.adiconarPerguntaRelVOs(pergunta);
			}
		}
		return obj;
	}

}
