package negocio.facade.jdbc.faturamento.nfe;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoItemVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.IntegracaoGinfesCursoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class IntegracaoGinfesCurso extends ControleAcesso implements IntegracaoGinfesCursoInterfaceFacade {

	private static final long serialVersionUID = -1L;
	protected static String idEntidade = "IntegracaoGinfesCurso";

	public IntegracaoGinfesCurso() {
		super();
	}

	public void validarDados(IntegracaoGinfesCursoVO obj) throws ConsistirException {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo UNIDADE ENSINO (IntegracaoGinfesCurso) deve ser informado.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(IntegracaoGinfesCursoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final IntegracaoGinfesCursoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			IntegracaoGinfesCurso.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO IntegracaoGinfesCurso (unidadeensino) VALUES (?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsino(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getIntegracaoGinfesCursoItemFacade().incluirCursos(obj, obj.getCursos(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final IntegracaoGinfesCursoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			if (obj.getImportado()) {
				throw new Exception("Este cadastro não pode ser alterado pois já foi marcado como importado!");
			}
			IntegracaoGinfesCurso.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE IntegracaoGinfesCurso SET unidadeensino=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsino(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
			getFacadeFactory().getIntegracaoGinfesCursoItemFacade().excluirCursos(obj, usuario);
			getFacadeFactory().getIntegracaoGinfesCursoItemFacade().incluirCursos(obj, obj.getCursos(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void importar(final IntegracaoGinfesCursoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			IntegracaoGinfesCurso.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE IntegracaoGinfesCurso SET dataImportacao = ?, importado = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getDataImportacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getImportado(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				throw new Exception("Falha ao definir arquivo como importado!");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(IntegracaoGinfesCursoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			if (obj.getImportado()) {
				throw new Exception("Este cadastro não pode ser excluido pois já foi marcado como importado!");
			}
			IntegracaoGinfesCurso.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM IntegracaoGinfesCurso WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoGinfesCursoVO> consultar(Integer unidadeEnsino, Date dataInicio, Date dataFim, Boolean naoImportados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM IntegracaoGinfesCurso WHERE 1=1");
		if (unidadeEnsino > 0) {
			sqlStr.append(" AND unidadeensino = ").append(unidadeEnsino);
		}
		if (naoImportados) {
			sqlStr.append(" AND dataimportacao is null");
		} else {
			if (dataInicio != null) {
				sqlStr.append(" AND dataimportacao >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
			}
			if (dataFim != null) {
				sqlStr.append(" AND dataimportacao <= '").append(Uteis.getDataJDBC(dataFim)).append("'");
			}
		}
		sqlStr.append(" ORDER BY dataimportacao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public IntegracaoGinfesCursoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = new StringBuilder("select * from IntegracaoGinfesCurso where codigo = ?");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( IntegracaoGinfesCursoVO ).");
			}
			IntegracaoGinfesCursoVO obj = new IntegracaoGinfesCursoVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public String executarGeracaoArquivoImportacao(IntegracaoGinfesCursoVO obj, int limite, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select codigo||(case when abreviatura is not null and trim(abreviatura) <> '' then '-'||trim(unaccent(abreviatura)) else '' end) arquivo, inscmunicipal im, regexp_replace(cnpj, '[^0-9]*', '', 'g') cnpj " + 
				"from unidadeensino where codigo = ").append(obj.getUnidadeEnsino().getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			Map<String, File> mapfile = new HashMap<String, File>(0);
			StringBuilder conteudoIncluir = new StringBuilder();
			StringBuilder conteudoAlterar = new StringBuilder();
			StringBuilder arquivoIncluir = new StringBuilder();
			StringBuilder arquivoAlterar = new StringBuilder();
			int parteIncluir = 1;
			int parteAlterar = 1;
			int contadorIncluir = 1;
			int contadorAlterar = 1;
			for (IntegracaoGinfesCursoItemVO item : obj.getCursos()) {
				if (item.getOperacao().intValue() == 0) {
					conteudoIncluir.append(realizarGeracaoConteudoXML(item));
					if (contadorIncluir >= limite) {
						contadorIncluir = 1;
						arquivoIncluir.append(realizarGeracaoCabecalhoXML(0, tabelaResultado));
						arquivoIncluir.append(conteudoIncluir);
						arquivoIncluir.append(realizarGeracaoRodapeXML());
						mapfile.put("0-" + parteIncluir, realizarGravacaoArquivoImportacao("curso-incluir-" + tabelaResultado.getString("arquivo") + "-parte" + parteIncluir, arquivoIncluir.toString()));
						parteIncluir++;
						conteudoIncluir = new StringBuilder();
						arquivoIncluir = new StringBuilder();
					} else {
						contadorIncluir++;
					}
				} else {
					conteudoAlterar.append(realizarGeracaoConteudoXML(item));
					if (contadorAlterar >= limite) {
						contadorAlterar = 1;
						arquivoAlterar.append(realizarGeracaoCabecalhoXML(0, tabelaResultado));
						arquivoAlterar.append(conteudoAlterar);
						arquivoAlterar.append(realizarGeracaoRodapeXML());
						mapfile.put("1-" + parteAlterar, realizarGravacaoArquivoImportacao("curso-alterar-" + tabelaResultado.getString("arquivo") + "-parte" + parteAlterar, arquivoAlterar.toString()));
						parteAlterar++;
						conteudoAlterar = new StringBuilder();
						arquivoAlterar = new StringBuilder();
					} else {
						contadorAlterar++;
					}
				}
			}
			if (!conteudoIncluir.toString().trim().isEmpty()) {
				arquivoIncluir.append(realizarGeracaoCabecalhoXML(0, tabelaResultado));
				arquivoIncluir.append(conteudoIncluir);
				arquivoIncluir.append(realizarGeracaoRodapeXML());
				mapfile.put("0-" + parteIncluir, realizarGravacaoArquivoImportacao("curso-incluir-" + tabelaResultado.getString("arquivo") + "-parte" + parteIncluir, arquivoIncluir.toString()));
			}
			if (!conteudoAlterar.toString().trim().isEmpty()) {
				arquivoAlterar.append(realizarGeracaoCabecalhoXML(1, tabelaResultado));
				arquivoAlterar.append(conteudoAlterar);
				arquivoAlterar.append(realizarGeracaoRodapeXML());
				mapfile.put("1-" + parteAlterar, realizarGravacaoArquivoImportacao("curso-alterar-" + tabelaResultado.getString("arquivo") + "-parte" + parteAlterar, arquivoAlterar.toString()));
			}
			String caminhoZip = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + "curso-" + tabelaResultado.getString("arquivo") + "-" + Uteis.getData(new Date(), "dd-MM-yyyy") + ".zip";
			File arquivoZip = new File(caminhoZip);
			File[] files = new File[mapfile.size()];
			int y = 0;
			for (File file : mapfile.values()) {
				files[y++] = file;
			}
			getFacadeFactory().getArquivoHelper().zip(files, arquivoZip);
			return "curso-" + tabelaResultado.getString("arquivo") + "-" + Uteis.getData(new Date(), "dd-MM-yyyy") + ".zip";
		} else {
			throw new Exception("Unidade de Ensino não encontrada!");
		}
	}
	
	private String realizarGeracaoCabecalhoXML(int operacao, SqlRowSet tabelaResultado) {
		StringBuilder cabecalho = new StringBuilder();
		cabecalho.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><tns:importacaoCursoEnvio xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:tns=\"http://www.ginfes.com.br/importacao_curso_envio\" xmlns:tpEdu=\"http://www.ginfes.com.br/tpEdu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" xsi:schemaLocation=\"http://www.ginfes.com.br/importacao_curso_envio xsd/v01/importacao_curso_envio.xsd\">")
			.append("<tns:empresaEscola><tpEdu:operacao>").append(operacao).append("</tpEdu:operacao><tpEdu:dsIm>").append(tabelaResultado.getString("im")).append("</tpEdu:dsIm><tpEdu:cpfCnpj>")
			.append(tabelaResultado.getString("cnpj")).append("</tpEdu:cpfCnpj><tpEdu:listaCursos>");
		return cabecalho.toString();
	}
	
	private String realizarGeracaoConteudoXML(IntegracaoGinfesCursoItemVO item) {
		StringBuilder str = new StringBuilder()
			.append("<tpEdu:Curso><tpEdu:codCurso>").append(item.getCodigoUnidadeEnsinoCurso())
			.append("</tpEdu:codCurso><tpEdu:descricaoCurso>").append(item.getDescricaoCursoTurno())
			.append("</tpEdu:descricaoCurso><tpEdu:valores><tpEdu:valorServico>").append(Uteis.retornarDuasCasasDecimais(String.valueOf(item.getValorServico())))
			.append("</tpEdu:valorServico><tpEdu:itemListaServico>").append(item.getItemListaServico()).append("</tpEdu:itemListaServico><tpEdu:descricaoServico>Prestacao de Servicos</tpEdu:descricaoServico></tpEdu:valores><tpEdu:tpCurso>1</tpEdu:tpCurso><tpEdu:icAtivo>")
			.append(item.getAtivo()).append("</tpEdu:icAtivo></tpEdu:Curso>");
		return str.toString();
	}
	
	private String realizarGeracaoRodapeXML() {
		return "</tpEdu:listaCursos></tns:empresaEscola></tns:importacaoCursoEnvio>";
	}
	
	private File realizarGravacaoArquivoImportacao(String nome, String conteudo) throws Exception {
		String caminho = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + nome + "-" + Uteis.getData(new Date(), "dd-MM-yyyy") + ".xml";
		File xml = new File(caminho);
		if (xml.exists()) {
			try {
				xml.delete();
			} catch (Exception e) {
				throw new Exception("Erro ao excluir arquivo de importação Ginfes.");
			}
		}
		PrintWriter out = new PrintWriter(xml);
		try {
			out.println(conteudo);
		} catch (Exception e) {
			throw new Exception("Erro ao gravar arquivo de importação Ginfes.");
		} finally {
			out.flush();
			out.close();
		}
		return xml;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private List<IntegracaoGinfesCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<IntegracaoGinfesCursoVO> vetResultado = new ArrayList<IntegracaoGinfesCursoVO>(0);
		while (tabelaResultado.next()) {
			IntegracaoGinfesCursoVO obj = new IntegracaoGinfesCursoVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDados(IntegracaoGinfesCursoVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDataImportacao(dadosSQL.getTimestamp("dataimportacao"));
		obj.setImportado(dadosSQL.getBoolean("importado"));
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(dadosSQL.getInt("unidadeensino"), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		obj.setCursos(getFacadeFactory().getIntegracaoGinfesCursoItemFacade().consultarPorIntegracao(obj.getCodigo(), usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	public static String getIdEntidade() {
		return IntegracaoGinfesCurso.idEntidade;
	}

}
