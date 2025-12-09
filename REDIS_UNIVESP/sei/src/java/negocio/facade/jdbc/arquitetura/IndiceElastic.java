package negocio.facade.jdbc.arquitetura;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.elastic.ExcecaoElasticVO;
import negocio.comuns.elastic.IndiceElasticVO;
import negocio.comuns.elastic.IndiceVersaoElasticVO;
import negocio.comuns.elastic.SincronismoElasticVO;

@Repository
@Scope("singleton")
@Lazy
public class IndiceElastic extends ControleAcesso implements IndiceElasticInterfaceFacade {
	
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

    public IndiceElastic() {
    	super();
		setIdEntidade("IndiceElastic");
    }
    
    @Override
	public IndiceElasticVO consultarPorNome(String valorConsulta, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM elastic.indice where nome = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
		IndiceElasticVO indice = new IndiceElasticVO();
		if (tabelaResultado.next()) {
			indice.setNome(tabelaResultado.getString("nome"));
			indice.setUrl(tabelaResultado.getString("url"));
			String sqlStrVersao = "SELECT * FROM elastic.indiceversao where indice = ? order by indice";
			SqlRowSet tabelaResultadoVersao = getConexao().getJdbcTemplate().queryForRowSet(sqlStrVersao, valorConsulta);
			while (tabelaResultadoVersao.next()) {
				IndiceVersaoElasticVO versao = new IndiceVersaoElasticVO();
				versao.setNumero(tabelaResultadoVersao.getString("numero"));
				versao.getIndice().setNome(indice.getNome());
				versao.getIndice().setUrl(indice.getUrl());
				versao.setAtivo(tabelaResultadoVersao.getBoolean("ativo"));
				versao.setRegistros(executarConsultaRegistrosIndiceVersao(indice.getUrl() + "/" + indice.getNome().toLowerCase() + "/" + versao.getNumero() + "/_count"));
				indice.getVersoes().add(versao);
			}
		}
		return indice;
	}
    
    @Override
	public List<SincronismoElasticVO> consultarSincronismos() throws Exception {
		String sqlStr = "SELECT inicio, fim, registros, mensagem, ativa FROM elastic.sync order by inicio desc limit 10";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<SincronismoElasticVO> sincronismos = new ArrayList<SincronismoElasticVO>();
		while (tabelaResultado.next()) {
			SincronismoElasticVO sincronismo = new SincronismoElasticVO();
			sincronismo.setInicio(tabelaResultado.getTimestamp("inicio"));
			sincronismo.setFim(tabelaResultado.getTimestamp("fim"));
			sincronismo.setRegistros(tabelaResultado.getInt("registros"));
			sincronismo.setMensagem(tabelaResultado.getString("mensagem"));
			sincronismo.setAtiva(tabelaResultado.getBoolean("ativa"));
			sincronismos.add(sincronismo);
		}
		return sincronismos;
	}
    
    @Override
	public List<ExcecaoElasticVO> consultarExcecoesPesquisa() throws Exception {
		String sqlStr = "SELECT created, origin, message_text FROM elastic.search_exception order by created desc limit 10";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<ExcecaoElasticVO> excecoes = new ArrayList<ExcecaoElasticVO>();
		while (tabelaResultado.next()) {
			ExcecaoElasticVO excecao = new ExcecaoElasticVO();
			excecao.setCriado(tabelaResultado.getTimestamp("created"));
			excecao.setOrigem(tabelaResultado.getString("origin"));
			excecao.setMensagem(tabelaResultado.getString("message_text"));
			excecoes.add(excecao);
		}
		return excecoes;
	}
    
    @Override
	public Integer consultarTotalExcecoesPesquisa() throws Exception {
		String sqlStr = "SELECT count(*) total FROM elastic.search_exception";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("total");
		} else {
			return 0;
		}
	}
    
    @Override
	public List<ExcecaoElasticVO> consultarExcecoesAgendamento() throws Exception {
		String sqlStr = "SELECT created, origin, message_text FROM elastic.fila_exception order by created desc limit 10";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<ExcecaoElasticVO> excecoes = new ArrayList<ExcecaoElasticVO>();
		while (tabelaResultado.next()) {
			ExcecaoElasticVO excecao = new ExcecaoElasticVO();
			excecao.setCriado(tabelaResultado.getTimestamp("created"));
			excecao.setOrigem(tabelaResultado.getString("origin"));
			excecao.setMensagem(tabelaResultado.getString("message_text"));
			excecoes.add(excecao);
		}
		return excecoes;
	}
    
    @Override
	public Integer consultarTotalExcecoesAgendamento() throws Exception {
		String sqlStr = "SELECT count(*) total FROM elastic.fila_exception";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("total");
		} else {
			return 0;
		}
	}
    
    @Override
	public List<ExcecaoElasticVO> consultarExcecoesSincronismo() throws Exception {
		String sqlStr = "SELECT created, origin, message_text FROM elastic.sync_exception order by created desc limit 10";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<ExcecaoElasticVO> excecoes = new ArrayList<ExcecaoElasticVO>();
		while (tabelaResultado.next()) {
			ExcecaoElasticVO excecao = new ExcecaoElasticVO();
			excecao.setCriado(tabelaResultado.getTimestamp("created"));
			excecao.setOrigem(tabelaResultado.getString("origin"));
			excecao.setMensagem(tabelaResultado.getString("message_text"));
			excecoes.add(excecao);
		}
		return excecoes;
	}
    
    @Override
	public Integer consultarTotalExcecoesSincronismo() throws Exception {
		String sqlStr = "SELECT count(*) total FROM elastic.sync_exception";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("total");
		} else {
			return 0;
		}
	}
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoIndiceElastic(IndiceElasticVO indice, UsuarioVO usuario) throws Exception {
		StringBuilder sqlIndice = new StringBuilder()
			.append("{")
			.append("  \"settings\": {")
			.append("		\"analysis\": {")
			.append("			\"filter\" : {")
			.append("				\"numeric_filter\" : {")
			.append("					\"type\" : \"word_delimiter\",")
			.append("					\"generate_number_parts\" : true,")
			.append("					\"catenate_all\" : false,")
			.append("					\"split_on_numerics\" : false")
			.append("				}")
			.append("			},")
			.append("			\"analyzer\" : {")
			.append("				\"lowercase_analyzer\": {")
			.append("					\"type\": \"custom\",")
			.append("					\"tokenizer\" : \"standard\",")
			.append("					\"filter\" : [\"lowercase\"]")
			.append("				}")
			.append("			}")
			.append("		},")
			.append("		\"number_of_shards\":1,") // índices pequenos não compensa ter mais de 1 shard
			.append("		\"number_of_replicas\":0") // inicialmente não possui réplicas
			.append("  }")
			.append("}");
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut put = new HttpPut(indice.getUrl() + "/" + indice.getNome().toLowerCase());
		put.setHeader("Content-Type", "application/json; charset=UTF-8");
		StringEntity entity = new StringEntity(sqlIndice.toString(), "UTF-8");
		put.setEntity(entity);
		HttpResponse response = client.execute(put);
		if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 204) {
			getConexao().getJdbcTemplate().update("insert into elastic.indice (nome, url) values (?, ?)", indice.getNome().toLowerCase(), indice.getUrl());
		} else {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			throw new Exception(result.toString());
		}
	}
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoIndiceVersaoElastic(IndiceVersaoElasticVO versao, UsuarioVO usuario) throws Exception {
		StringBuilder sqlIndice = new StringBuilder()
			.append("{")
			.append("	\"properties\" : {")
			.append("		\"codigo\": {")
			.append("		    \"type\": \"long\"")
			.append("		},")
			.append("		\"foto\": {")
			.append("		   	\"type\": \"keyword\"")
			.append("		},")
			.append("		\"cpf\": {")
			.append("		    \"type\": \"text\"")
			.append("		},")
			.append("		\"email\": {")
			.append("		    \"type\": \"text\",")
			.append("			\"analyzer\" : \"lowercase_analyzer\"")
			.append("		},")
			.append("		\"nome\": {")
			.append("		    \"type\": \"text\",")
			.append("			\"analyzer\" : \"lowercase_analyzer\"")
			.append("		},")
			.append("		\"matriculas\": {")
			.append("		    \"type\": \"text\",")
			.append("			\"analyzer\" : \"lowercase_analyzer\"")
			.append("		},")
			.append("		\"registroacademico\": {")
			.append("		    \"type\": \"text\",")
			.append("			\"analyzer\" : \"lowercase_analyzer\"")
			.append("		},")
			.append("		\"filiacao.cpf\": {")
			.append("		    \"type\": \"text\"")
			.append("		},")
			.append("		\"filiacao.email\": {")
			.append("		    \"type\": \"text\",")
			.append("			\"analyzer\" : \"lowercase_analyzer\"")
			.append("		},")
			.append("		\"filiacao.nome\": {")
			.append("		    \"type\": \"text\",")
			.append("			\"analyzer\" : \"lowercase_analyzer\"")
			.append("		},")
			.append("		\"filiacao.tipo\": {")
			.append("		    \"type\": \"keyword\"")
			.append("		}")
			.append("    }")
			.append("}");
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut put = new HttpPut(versao.getIndice().getUrl() + "/" + versao.getIndice().getNome().toLowerCase() + "/_mapping/" + versao.getNumero() + "?include_type_name=true");
		put.setHeader("Content-Type", "application/json; charset=UTF-8");
		StringEntity entity = new StringEntity(sqlIndice.toString(), "UTF-8");
		put.setEntity(entity);
		HttpResponse response = client.execute(put);
		if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 204) {
			getConexao().getJdbcTemplate().update("insert into elastic.indiceversao (numero, indice) values (?, ?)", versao.getNumero(), versao.getIndice().getNome().toLowerCase());
		} else {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			throw new Exception(result.toString());
		}
	}
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarExclusaoIndiceElastic(IndiceElasticVO indice, UsuarioVO usuario) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete delete = new HttpDelete(indice.getUrl() + "/" + indice.getNome().toLowerCase());
		HttpResponse response = client.execute(delete);
		if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
			getConexao().getJdbcTemplate().update("delete from elastic.indice where nome = ?", indice.getNome());
		} else {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			throw new Exception(result.toString());
		}
	}
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarExclusaoIndiceVersaoElastic(IndiceVersaoElasticVO versao, UsuarioVO usuario) throws Exception {
//		HttpClient client = HttpClientBuilder.create().build();
//		HttpDelete delete = new HttpDelete(versao.getIndice().getUrl() + "/" + versao.getIndice().getNome().toLowerCase() + "/_query?q=_type:" + versao.getNumero());
//		HttpResponse response = client.execute(delete);
//		if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
//			getConexao().getJdbcTemplate().update("delete from elastic.indiceversao where indice = ? and numero = ?", versao.getIndice().getNome().toLowerCase(), versao.getNumero());
//		} else {
//			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//			StringBuffer result = new StringBuffer();
//			String line = "";
//			while ((line = rd.readLine()) != null) {
//				result.append(line);
//			}
//			throw new Exception(result.toString());
//		}
	}
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAtivacaoIndiceVersaoElastic(IndiceVersaoElasticVO versao, UsuarioVO usuario) throws Exception {
    	getConexao().getJdbcTemplate().update("update elastic.indiceversao set ativo = false where indice = ?", versao.getIndice().getNome());
    	getConexao().getJdbcTemplate().update("update elastic.indiceversao set ativo = true where indice = ? and numero = ?", versao.getIndice().getNome().toLowerCase(), versao.getNumero());
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAgendamentoIndexacaoVersaoElastic(IndiceVersaoElasticVO versao, UsuarioVO usuario) throws Exception {
		String sqlStr = "select elastic.agendar_alunos()";
		getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    }

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		IndiceElastic.idEntidade = idEntidade;
	}
    
}