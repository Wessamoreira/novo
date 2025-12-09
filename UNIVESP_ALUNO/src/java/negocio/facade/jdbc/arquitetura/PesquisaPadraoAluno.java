package negocio.facade.jdbc.arquitetura;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.PesquisaPadraoAlunoResponsavelVO;
import negocio.comuns.arquitetura.PesquisaPadraoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.PesquisaPadraoAlunoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class PesquisaPadraoAluno extends ControleAcesso implements PesquisaPadraoAlunoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	private static String idEntidade;

	public PesquisaPadraoAluno() throws Exception {
		super();
		setIdEntidade("PesquisaPadraoAluno");
	}

	public static String getIdEntidade() {
		return PesquisaPadraoAluno.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		PesquisaPadraoAluno.idEntidade = idEntidade;
	}

	@Override
	public List<PesquisaPadraoAlunoVO> consultarAlunoPorNomeCpfEmailResponsavelAutoComplete(String valorConsulta, UsuarioVO usuario) throws Exception {
		try {
			String urlPastaUpload = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarUrlExternoDownloadArquivoPadraoSistema();
			List<PesquisaPadraoAlunoVO> alunos = new ArrayList<PesquisaPadraoAlunoVO>(0);
			String uri = "";
			SqlRowSet tabelaResultadoUri = getConexao().getJdbcTemplate().queryForRowSet("select elastic.consultar_indice('aluno')||'_search'::text as indice");
			if (tabelaResultadoUri.next()) {
				uri = tabelaResultadoUri.getString("indice");
			}
			String content = "";
			int limite = 8;
			SqlRowSet tabelaResultadoContent = getConexao().getJdbcTemplate().queryForRowSet("select elastic.montar_query_aluno(?, " + limite + ") as uri", valorConsulta);
			if (tabelaResultadoContent.next()) {
				content = tabelaResultadoContent.getString("uri");
			}
			HttpClient client = HttpClientBuilder.create().build();
			if (uri != null) {
				HttpPost post = new HttpPost(uri);
				post.setHeader("Content-Type", "application/json; charset=UTF-8");
			    StringEntity entity = new StringEntity(content, "UTF-8");
				post.setEntity(entity);
				HttpResponse response = client.execute(post);
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
				StringBuffer resultadoElastic = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					resultadoElastic.append(line);
				}
				String sqlStr = realizarMontagemSqlPesquisaAluno(resultadoElastic.toString());
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
				int posicao = 0;
				int ultimoCodigo = 0;
				while (tabelaResultado.next()) {
					int novoCodigo = tabelaResultado.getInt("codigo");
					if (novoCodigo == ultimoCodigo) {
						PesquisaPadraoAlunoVO aluno = alunos.get(posicao - 1);
						PesquisaPadraoAlunoResponsavelVO responsavel = new PesquisaPadraoAlunoResponsavelVO();
						responsavel.setNome(tabelaResultado.getString("r_nome"));
						responsavel.setCpf(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(tabelaResultado.getString("r_cpf"), 10, 3, 2));
						responsavel.setEmail(Uteis.realizarAnonimizacaoEmailIgnorandoTagNegrito(tabelaResultado.getString("r_email")));
						responsavel.setTipo(tabelaResultado.getString("r_tipo"));
						if (Uteis.isAtributoPreenchido(responsavel.getEmail())) {
							responsavel.setEmail(responsavel.getEmail().toLowerCase());
						}
						aluno.getResponsaveis().add(responsavel);
					} else {
						PesquisaPadraoAlunoVO obj = new PesquisaPadraoAlunoVO();
						obj.setCodigo(novoCodigo);
				    	if (tabelaResultado.getString("foto") == null || tabelaResultado.getString("foto").trim().isEmpty()) {
				    		obj.setFoto("../../resources/imagens/foto_usuario.jpg");
				    	} else {
				    		obj.setFoto(urlPastaUpload + "/imagem/" + tabelaResultado.getString("foto"));
				    	}
				    	obj.setFoto(obj.getFoto().replace("//imagem", "/imagem"));
						obj.setNome(tabelaResultado.getString("nome"));
						obj.setMatriculas(tabelaResultado.getString("matriculas"));
						obj.setRegistroAcademico(tabelaResultado.getString("registroacademico"));
						obj.setCpf(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(tabelaResultado.getString("cpf"), 10, 3, 2));
						obj.setEmail(Uteis.realizarAnonimizacaoEmailIgnorandoTagNegrito(tabelaResultado.getString("email")));
						if (Uteis.isAtributoPreenchido(obj.getEmail())) {
							obj.setEmail(obj.getEmail().toLowerCase());
						}
						obj.setScore(tabelaResultado.getFloat("score"));

						PesquisaPadraoAlunoResponsavelVO responsavel = new PesquisaPadraoAlunoResponsavelVO();
						responsavel.setNome(tabelaResultado.getString("r_nome"));
						responsavel.setCpf(Uteis.realizarAnonimizacaoIgnorandoTagNegrito(tabelaResultado.getString("r_cpf"), 10, 3, 2));
						responsavel.setEmail(Uteis.realizarAnonimizacaoEmailIgnorandoTagNegrito(tabelaResultado.getString("r_email")));
						if (Uteis.isAtributoPreenchido(responsavel.getEmail())) {
							responsavel.setEmail(responsavel.getEmail().toLowerCase());
						}
						responsavel.setTipo(tabelaResultado.getString("r_tipo"));
						if (!responsavel.getNome().isEmpty()) {
							obj.getResponsaveis().add(responsavel);
						}
						alunos.add(obj);
						posicao++;
						ultimoCodigo = novoCodigo;
					}
				}
				tabelaResultado = null;
				sqlStr = null;
			} else {
				throw new Exception("Indice não encontrado");
			}
			return alunos;
		} catch (Exception e) {
			executarRegistroFalhaPesquisaElastic("aluno", e.getMessage());
			return new ArrayList<PesquisaPadraoAlunoVO>(0);
		}
	}

	private String realizarMontagemSqlPesquisaAluno(String resultadoElastic) {
		StringBuilder consulta = new StringBuilder();
		consulta.append("WITH i AS (")
		.append("		SELECT score, doc.source ->> 'codigo' codigo, doc.source ->> 'foto' foto,")
		.append("			CASE WHEN (doc.highlight -> 'nome') IS NOT NULL 	  THEN REPLACE(doc.highlight_nome, '\"', '') 	   ELSE doc.source ->> 'nome' 				END nome,")
		.append("			CASE WHEN (doc.highlight -> 'matriculas') IS NOT NULL THEN REPLACE(doc.highlight_matriculas, '\"', '') ELSE doc.source ->> 'matriculas' 		END matriculas,")
		.append("			CASE WHEN (doc.highlight -> 'registroacademico') IS NOT NULL THEN REPLACE(doc.highlight_ra, '\"', '')  ELSE doc.source ->> 'registroacademico'	END registroacademico,")
		.append("			CASE WHEN (doc.highlight -> 'cpf') IS NOT NULL 		  THEN REPLACE(doc.highlight_cpf, '\"', '') 	   ELSE doc.source ->> 'cpf' 				END cpf,")
		.append("			CASE WHEN (doc.highlight -> 'email') IS NOT NULL 	  THEN REPLACE(doc.highlight_email, '\"', '') 	   ELSE doc.source ->> 'email'      		END email,")
		.append("			CASE WHEN (doc.source -> 'filiacao') IS NOT NULL 	  THEN doc.source -> 'filiacao' 				   ELSE null 								END responsaveis,")
		.append("			COALESCE((doc.highlight -> 'filiacao.nome'),'[]'::json) h_f_nome,")
		.append("			COALESCE((doc.highlight -> 'filiacao.cpf'),'[]'::json) h_f_cpf,")
		.append("			COALESCE((doc.highlight -> 'filiacao.email'),'[]'::json) h_f_email")
		.append("		FROM ( ")
		.append("SELECT ")
	    .append("  t.score,t.source,t.highlight, ")
		.append("  CASE WHEN highlight_nome.highlight_nome is not null 			    THEN highlight_nome.highlight_nome::TEXT 			 ELSE t.source ->> 'nome' 				END highlight_nome, ")
		.append("  CASE WHEN highlight_matriculas.highlight_matriculas is not null  THEN highlight_matriculas.highlight_matriculas::TEXT ELSE t.source ->> 'matriculas' 		END highlight_matriculas, ")
		.append("  CASE WHEN highlight_ra.highlight_ra is not null  				THEN highlight_ra.highlight_ra::TEXT 				 ELSE t.source ->> 'registroacademico'	END highlight_ra, ")
		.append("  CASE WHEN highlight_cpf.highlight_cpf is not null 				THEN highlight_cpf.highlight_cpf::TEXT 	             ELSE t.source ->> 'cpf' 				END highlight_cpf, ")
		.append("  CASE WHEN highlight_email.highlight_email is not null 			THEN highlight_email.highlight_email::TEXT 			 ELSE t.source ->> 'email' 				END highlight_email ")
		.append("FROM  ( ")
		.append("			SELECT (value ->> '_score')::numeric score, value -> '_source' source,")
		.append("			value #> '{highlight}' highlight FROM json_array_elements(")
		.append("				'").append(resultadoElastic).append("'::json #> '{hits, hits}'")
		.append("			) ) AS t")
		.append(" LEFT JOIN LATERAL json_array_elements(t.highlight -> 'nome') highlight_nome(highlight_nome) 					 ON (t.highlight -> 'nome') IS NOT NULL ")
		.append(" LEFT JOIN LATERAL json_array_elements(t.highlight -> 'matriculas') highlight_matriculas(highlight_matriculas)  ON (t.highlight -> 'matriculas') IS NOT NULL ")
		.append(" LEFT JOIN LATERAL json_array_elements(t.highlight -> 'registroacademico') highlight_ra(highlight_ra)  		 ON (t.highlight -> 'registroacademico') IS NOT NULL ")
		.append(" LEFT JOIN LATERAL json_array_elements(t.highlight -> 'cpf') highlight_cpf(highlight_cpf) 						 ON (t.highlight -> 'cpf') IS NOT NULL ")
		.append(" LEFT JOIN LATERAL json_array_elements(t.highlight -> 'email') highlight_email(highlight_email) 				 ON (t.highlight -> 'email') IS NOT NULL ")
		.append(" ) doc")
		.append("	) SELECT i.score, i.codigo, i.foto, i.nome, i.matriculas, i.registroacademico, i.cpf, i.email,")
		.append("	CASE WHEN i.h_f_nome::text = '[]' THEN resp.value ->> 'nome' ELSE (SELECT c FROM (")
		.append("		SELECT CASE WHEN (resp.value ->> 'nome') = elastic.remover_highlight(lista.item) THEN lista.item ELSE (resp.value ->> 'nome') END c FROM (")
		.append("			SELECT REPLACE(json_array_elements(i.h_f_nome)::text,'\"','') item")
		.append("		) lista")
		.append("	) x	ORDER BY char_length(c) DESC LIMIT 1")
		.append("	) END r_nome,")
		.append("	CASE WHEN i.h_f_cpf::text = '[]' then resp.value ->> 'cpf' ELSE (SELECT c FROM (")
		.append("		SELECT CASE WHEN (resp.value ->> 'cpf') = elastic.remover_highlight(lista.item) THEN lista.item ELSE (resp.value ->> 'cpf') END c FROM (")
		.append("			SELECT REPLACE(json_array_elements(i.h_f_cpf)::text,'\"','') item")
		.append("		) lista")
		.append("	) x	ORDER BY char_length(c) DESC LIMIT 1")
		.append("	) end r_cpf,")
		.append("	CASE WHEN i.h_f_email::text = '[]' THEN resp.value ->> 'email' ELSE (SELECT c FROM (")
		.append("		SELECT CASE WHEN (resp.value ->> 'email') = elastic.remover_highlight(lista.item) THEN lista.item ELSE (resp.value ->> 'email') END c from (")
		.append("			SELECT replace(json_array_elements(i.h_f_email)::text,'\"','') item")
		.append("		) lista")
		.append("	) x	ORDER BY char_length(c) DESC LIMIT 1")
		.append("	) END r_email,")
		.append("	(CASE WHEN (resp.value ->> 'tipo') = 'PA' THEN 'Pai' WHEN (resp.value ->> 'tipo') = 'MA' THEN 'Mãe' WHEN (resp.value ->> 'tipo') = 'RL' THEN 'Resp. Legal' ELSE '' END)||")
		.append("	(CASE WHEN (resp.value ->> 'responsavelfinanceiro')::boolean THEN ' e Resp. Finan.' ELSE '' END) r_tipo")
		.append("	FROM i")
		.append("	LEFT JOIN json_array_elements(i.responsaveis) resp ON 1 = 1 ")
		.append("ORDER BY i.score DESC, i.codigo");
		return consulta.toString();
	}

}