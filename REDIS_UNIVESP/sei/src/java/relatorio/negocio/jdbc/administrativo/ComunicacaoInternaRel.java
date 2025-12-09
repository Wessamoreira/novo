package relatorio.negocio.jdbc.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.administrativo.ComunicacaoInternaRelVO;
import relatorio.negocio.interfaces.administrativo.ComunicacaoInternaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ComunicacaoInternaRel extends SuperRelatorio implements ComunicacaoInternaRelInterfaceFacade {

	public List<ComunicacaoInternaRelVO> criarObjeto(Integer unidadeEnsino, String tipoPessoa, Integer codigoPessoa, Date dataInicio, Date dataFim, Boolean filtroLeituraRegistrada, String tipoOrdenacao, UsuarioVO usuarioVO) throws Exception {
		List<ComunicacaoInternaRelVO> comunicacaoInternaRelVOs = new ArrayList<ComunicacaoInternaRelVO>(0);
		SqlRowSet rs = executarConsultaParametrizada(unidadeEnsino, tipoPessoa, codigoPessoa, dataInicio, dataFim, filtroLeituraRegistrada, tipoOrdenacao, usuarioVO);
		while (rs.next()) {
			comunicacaoInternaRelVOs.add(montarDados(rs, usuarioVO));
		}
		return comunicacaoInternaRelVOs;
	}

	public SqlRowSet executarConsultaParametrizada(Integer unidadeEnsino, String tipoPessoa, Integer codigoPessoa, Date dataInicio, Date dataFim, Boolean filtroLeituraRegistrada, String tipoOrdenacao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSqlConsultaBase(unidadeEnsino, tipoPessoa, codigoPessoa, dataInicio, dataFim, filtroLeituraRegistrada, tipoOrdenacao, usuarioVO);
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	public StringBuilder getSqlConsultaBase(Integer unidadeEnsino, String tipoPessoa, Integer codigoPessoa, Date dataInicio, Date dataFim, Boolean filtroLeituraRegistrada, String tipoOrdenacao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT comunicacaointerna.data as dataenvio, comunicacaointerna.assunto, comunicacaointerna.mensagem, comunicadointernodestinatario.cijalida as leituraRegistrada, ");
		sqlStr.append(" comunicadointernodestinatario.dataleitura, remetente.nome as \"remetente.nome\", destinatario.nome as \"destinatario.nome\" ");
		sqlStr.append(" FROM comunicacaointerna ");
		sqlStr.append(" INNER JOIN comunicadointernodestinatario ON comunicadointernodestinatario.comunicadointerno = comunicacaointerna.codigo ");
		sqlStr.append(" INNER JOIN pessoa AS remetente ON remetente.codigo = comunicacaointerna.responsavel ");
		sqlStr.append(" INNER JOIN pessoa AS destinatario ON destinatario.codigo = comunicadointernodestinatario.destinatario ");
		sqlStr.append(" WHERE 1=1 ");
		if (tipoPessoa.equals("aluno")) {
			sqlStr.append(" AND tipodestinatario = 'AL' ");
		} else if (tipoPessoa.equals("professor")) {
			sqlStr.append(" AND tipodestinatario = 'PR' ");
		} else if (tipoPessoa.equals("coordenador")) {
			sqlStr.append(" AND tipodestinatario = 'CO' ");
		} else if (tipoPessoa.equals("funcionario")) {
			sqlStr.append(" AND tipodestinatario = 'FU' ");
		}
		if (!codigoPessoa.equals(0)) {
			sqlStr.append(" AND destinatario.codigo = ").append(codigoPessoa);
		}
		if (filtroLeituraRegistrada) {
			sqlStr.append(" AND comunicadointernodestinatario.cijalida = ").append(filtroLeituraRegistrada);
		}
		sqlStr.append(" AND comunicacaointerna.data >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
		sqlStr.append(" AND comunicacaointerna.data <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		if (tipoOrdenacao.equals("destinatario")) {
			sqlStr.append(" ORDER BY destinatario.nome ");
		} else if (tipoOrdenacao.equals("remetente")) {
			sqlStr.append(" ORDER BY remetente.nome ");
		} else if (tipoOrdenacao.equals("assunto")) {
			sqlStr.append(" ORDER BY comunicacaointerna.assunto ");
		} else if (tipoOrdenacao.equals("dataEnvio")) {
			sqlStr.append(" ORDER BY comunicacaointerna.data ");
		} else if (tipoOrdenacao.equals("dataLeitura")) {
			sqlStr.append(" ORDER BY comunicadointernodestinatario.dataleitura ");
		}
		return sqlStr;
	}

	public ComunicacaoInternaRelVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioLogado) throws Exception {
		ComunicacaoInternaRelVO obj = new ComunicacaoInternaRelVO();
		obj.setAssunto(dadosSQL.getString("assunto"));
		obj.setDataEnvio(dadosSQL.getDate("dataenvio"));
		obj.setDataLeitura(dadosSQL.getDate("dataleitura"));

		Document doc = Jsoup.parse(dadosSQL.getString("mensagem"));
		doc.select("br").append("\\n");
		doc.select("p").prepend("\\n");
		String text = doc.body().text().replace("\\n", "\n");
		obj.setMensagem(text.trim());

		obj.setRemetente(dadosSQL.getString("remetente.nome"));
		obj.setDestinatario(dadosSQL.getString("destinatario.nome"));
		obj.setLeituraRegistrada(dadosSQL.getBoolean("leituraRegistrada"));
		return obj;
	}

	public static String getCaminhoBaseDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator);
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getIdEntidade() {
		return ("ComunicacaoInternaRel");
	}

}
