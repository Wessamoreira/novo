package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AnotacaoDisciplinaVO;
import negocio.comuns.ead.enumeradores.NivelImportanciaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.AnotacaoDisciplinaInterfaceFacade;

/**
 * @author Victor Hugo 08/09/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AnotacaoDisciplina extends ControleAcesso implements AnotacaoDisciplinaInterfaceFacade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AnotacaoDisciplina.idEntidade = idEntidade;
	}

	public AnotacaoDisciplina() throws Exception {
		super();
		setIdEntidade("AnotacaoDisciplina");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AnotacaoDisciplinaVO anotacaoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(anotacaoDisciplinaVO);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO anotacaodisciplina (datacriacao, anotacao, " + "palavraschaves, publica, disciplina, conteudo, unidadeconteudo, " + "responsavel, matricula, conteudounidadepagina, conteudounidadepaginarecursoeducacional, nivelimportancia) " + "VALUES (?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			;
			anotacaoDisciplinaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(anotacaoDisciplinaVO.getDataCriacao()));
					sqlInserir.setString(2, anotacaoDisciplinaVO.getAnotacao());
					sqlInserir.setString(3, anotacaoDisciplinaVO.getPalavraChave());
					sqlInserir.setBoolean(4, anotacaoDisciplinaVO.getPublica());
					if (anotacaoDisciplinaVO.getDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, anotacaoDisciplinaVO.getDisciplinaVO().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (anotacaoDisciplinaVO.getConteudoVOs().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, anotacaoDisciplinaVO.getConteudoVOs().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (anotacaoDisciplinaVO.getUnidadeConteudoVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, anotacaoDisciplinaVO.getUnidadeConteudoVO().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (anotacaoDisciplinaVO.getPessoaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, anotacaoDisciplinaVO.getPessoaVO().getCodigo());
					} else {
						sqlInserir.setNull(8, 0);
					}
					if (anotacaoDisciplinaVO.getMatriculaVO().getMatricula().length() != 0) {
						sqlInserir.setString(9, anotacaoDisciplinaVO.getMatriculaVO().getMatricula());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (anotacaoDisciplinaVO.getConteudoUnidadePaginaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, anotacaoDisciplinaVO.getConteudoUnidadePaginaVO().getCodigo());
					} else {
						sqlInserir.setNull(10, 0);
					}
					if (anotacaoDisciplinaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, anotacaoDisciplinaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo());
					} else {
						sqlInserir.setNull(11, 0);
					}
					sqlInserir.setString(12, anotacaoDisciplinaVO.getNivelImportanciaEnum().name());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						anotacaoDisciplinaVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			anotacaoDisciplinaVO.setNovoObj(Boolean.TRUE);
			anotacaoDisciplinaVO.setCodigo(0);
			throw e;
		}
	}

	public void validarDados(AnotacaoDisciplinaVO anotacaoDisciplinaVO) throws Exception {
		if (anotacaoDisciplinaVO.getAnotacao().length() < 0) {
			throw new Exception("Digite uma anotação");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AnotacaoDisciplinaVO anotacaoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (anotacaoDisciplinaVO.getCodigo().equals(0)) {
			incluir(anotacaoDisciplinaVO, verificarAcesso, usuarioVO);
		} else {
			alterar(anotacaoDisciplinaVO, verificarAcesso, usuarioVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AnotacaoDisciplinaVO anotacaoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE anotacaodisciplina set datacriacao = ?, anotacao = ?, palavraschaves = ?, " + "publica = ?, disciplina = ?, conteudo = ?, unidadeconteudo = ?, " + " responsavel = ?, matricula = ?, conteudounidadepagina = ?, conteudounidadepaginarecursoeducacional = ?, nivelimportancia = ?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			;
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(anotacaoDisciplinaVO.getDataCriacao()));
					sqlAlterar.setString(2, anotacaoDisciplinaVO.getAnotacao());
					sqlAlterar.setString(3, anotacaoDisciplinaVO.getPalavraChave());
					sqlAlterar.setBoolean(4, anotacaoDisciplinaVO.getPublica());
					if (anotacaoDisciplinaVO.getDisciplinaVO().getCodigo() != 0) {
						sqlAlterar.setInt(5, anotacaoDisciplinaVO.getDisciplinaVO().getCodigo());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (anotacaoDisciplinaVO.getConteudoVOs().getCodigo() != 0) {
						sqlAlterar.setInt(6, anotacaoDisciplinaVO.getConteudoVOs().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (anotacaoDisciplinaVO.getUnidadeConteudoVO().getCodigo() != 0) {
						sqlAlterar.setInt(7, anotacaoDisciplinaVO.getUnidadeConteudoVO().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (anotacaoDisciplinaVO.getPessoaVO().getCodigo() != 0) {
						sqlAlterar.setInt(8, anotacaoDisciplinaVO.getPessoaVO().getCodigo());
					} else {
						sqlAlterar.setNull(8, 0);
					}
					if (anotacaoDisciplinaVO.getMatriculaVO().getMatricula().length() != 0) {
						sqlAlterar.setString(9, anotacaoDisciplinaVO.getMatriculaVO().getMatricula());
					} else {
						sqlAlterar.setNull(9, 0);
					}
					if (anotacaoDisciplinaVO.getConteudoUnidadePaginaVO().getCodigo() != 0) {
						sqlAlterar.setInt(10, anotacaoDisciplinaVO.getConteudoUnidadePaginaVO().getCodigo());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if (anotacaoDisciplinaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo() != 0) {
						sqlAlterar.setInt(11, anotacaoDisciplinaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setString(12, anotacaoDisciplinaVO.getNivelImportanciaEnum().name());
					sqlAlterar.setInt(13, anotacaoDisciplinaVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final AnotacaoDisciplinaVO anotacaoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM anotacaodisciplina WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, anotacaoDisciplinaVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public AnotacaoDisciplinaVO montarDadosBasico(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) {
		AnotacaoDisciplinaVO obj = new AnotacaoDisciplinaVO();
		obj.setCodigo(tabelaResultado.getInt("ad.codigo"));
		obj.setDataCriacao(Uteis.getDataJDBC(tabelaResultado.getDate("ad.datacriacao")));
		obj.setAnotacao(tabelaResultado.getString("ad.anotacao"));
		obj.setPalavraChave(tabelaResultado.getString("ad.palavraschaves"));
		obj.setPublica(tabelaResultado.getBoolean("ad.publica"));
		obj.setNivelImportanciaEnum(NivelImportanciaEnum.valueOf(tabelaResultado.getString("ad.nivelimportancia")));
		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
		obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
		obj.getConteudoVOs().setCodigo(tabelaResultado.getInt("conteudo.codigo"));
		obj.getConteudoVOs().setDescricao(tabelaResultado.getString("conteudo.descricao"));
		obj.getUnidadeConteudoVO().setCodigo(tabelaResultado.getInt("unidadeconteudo.codigo"));
		obj.getUnidadeConteudoVO().setTitulo(tabelaResultado.getString("unidadeconteudo.titulo"));
		obj.getUnidadeConteudoVO().setOrdem(tabelaResultado.getInt("unidadeconteudo.ordem"));
		obj.getConteudoUnidadePaginaVO().setCodigo(tabelaResultado.getInt("conteudoUnidadePagina.codigo"));
		obj.getConteudoUnidadePaginaVO().setPagina(tabelaResultado.getInt("conteudoUnidadePagina.pagina"));
		obj.getConteudoUnidadePaginaVO().setTitulo(tabelaResultado.getString("conteudoUnidadePagina.titulo"));
		
		obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
		obj.getPessoaVO().setNome(tabelaResultado.getString("pessoa.nome"));
		obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula.matricula"));
		obj.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("pessoa_mat.codigo"));
		obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("pessoa_mat.nome"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(tabelaResultado.getInt("ad.conteudounidadepaginarecursoeducacional"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			return obj;
		}
		return obj;
	}

	public AnotacaoDisciplinaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) {
		AnotacaoDisciplinaVO obj = new AnotacaoDisciplinaVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDataCriacao(Uteis.getDataJDBC(tabelaResultado.getDate("datacriacao")));
		obj.setAnotacao(tabelaResultado.getString("anotacao"));
		obj.setPalavraChave(tabelaResultado.getString("palavraschaves"));
		obj.setPublica(tabelaResultado.getBoolean("publica"));
		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina"));
		obj.getConteudoVOs().setCodigo(tabelaResultado.getInt("conteudo"));
		obj.getUnidadeConteudoVO().setCodigo(tabelaResultado.getInt("unidadeconteudo"));
		obj.getPessoaVO().setCodigo(tabelaResultado.getInt("responsavel"));
		obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
		obj.getConteudoUnidadePaginaVO().setCodigo(tabelaResultado.getInt("conteudounidadepagina"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(tabelaResultado.getInt("conteudounidadepaginarecursoeducacional"));
		obj.setNivelImportanciaEnum(NivelImportanciaEnum.valueOf(tabelaResultado.getString("nivelimportancia")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			return obj;
		}
		return obj;
	}

	public List<AnotacaoDisciplinaVO> montarDadosBasicoConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<AnotacaoDisciplinaVO> vetResultado = new ArrayList<AnotacaoDisciplinaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasico(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}

		return vetResultado;
	}
	
	public List<AnotacaoDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<AnotacaoDisciplinaVO> vetResultado = new ArrayList<AnotacaoDisciplinaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}
		
		return vetResultado;
	}

	public List<AnotacaoDisciplinaVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		Integer codigo;
		if (campoConsulta.equals("palavrachave")) {
			if (valorConsulta.length() < 2) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
			} else {
				return consultarPorPalavraChave(valorConsulta, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			}
		} else {
			if (campoConsulta.equals("codigo")) {
				if (!Uteis.isAtributoPreenchido(valorConsulta)) {
					throw new Exception(UteisJSF.internacionalizar("msg_dados_parametroConsulta"));
				} else {
					try {
						codigo = Integer.parseInt(valorConsulta);
					} catch (Exception e) {
						throw new Exception(UteisJSF.internacionalizar("msg_validarSomenteNumeroString"));
					}
					return consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
				}
			}
		}
		return null;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM anotacaodisciplina WHERE codigo = ?";

		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql, codigo), nivelMontarDados, usuarioLogado));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorPalavraChave(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "SELECT * FROM anotacaodisciplina WHERE upper(palavraschaves) like('" + valorConsulta.toUpperCase() + "%') ORDER BY codigo";

		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr), nivelMontarDados, usuarioLogado));
	}

	@Override
	public AnotacaoDisciplinaVO realizarCriarAnotacaoUnidadeConteudo(String matriculaVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, UsuarioVO usuarioLogado) {
		AnotacaoDisciplinaVO anotacaoDisciplinaVO = new AnotacaoDisciplinaVO();
		anotacaoDisciplinaVO.getMatriculaVO().setMatricula(matriculaVO);
		anotacaoDisciplinaVO.setDisciplinaVO(disciplinaVO);
		anotacaoDisciplinaVO.setConteudoVOs(unidadeConteudoVO.getConteudo());
		anotacaoDisciplinaVO.setUnidadeConteudoVO(unidadeConteudoVO);
		anotacaoDisciplinaVO.setPessoaVO(usuarioLogado.getPessoa());
		anotacaoDisciplinaVO.setNovoObj(Boolean.TRUE);
		return anotacaoDisciplinaVO;
	}

	@Override
	public AnotacaoDisciplinaVO realizarCriarAnotacaoConteudoUnidadePagina(String matriculaVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UsuarioVO usuarioLogado) {
		AnotacaoDisciplinaVO anotacaoDisciplinaVO = new AnotacaoDisciplinaVO();
		anotacaoDisciplinaVO.getMatriculaVO().setMatricula(matriculaVO);
		anotacaoDisciplinaVO.setDisciplinaVO(disciplinaVO);
		anotacaoDisciplinaVO.setConteudoVOs(conteudoVO);
		anotacaoDisciplinaVO.setUnidadeConteudoVO(conteudoUnidadePaginaVO.getUnidadeConteudo());
		anotacaoDisciplinaVO.setPessoaVO(usuarioLogado.getPessoa());
		anotacaoDisciplinaVO.setConteudoUnidadePaginaVO(conteudoUnidadePaginaVO);
		anotacaoDisciplinaVO.setNovoObj(Boolean.TRUE);
		return anotacaoDisciplinaVO;
	}

	public AnotacaoDisciplinaVO consultarAnotacaoDisciplinaPorDisciplinaMatriculaConteudoConteudoUnidadePagina(String matriculaVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM anotacaodisciplina");
		sql.append(" where 1=1 ");
		sql.append(" and matricula = '").append(matriculaVO).append("'");
		sql.append(" and responsavel = ").append(usuarioLogado.getPessoa().getCodigo());
		sql.append(" and disciplina = ").append(disciplinaVO.getCodigo());
		if (Uteis.isAtributoPreenchido(unidadeConteudoVO.getConteudo())) {
			sql.append(" and conteudo = ").append(unidadeConteudoVO.getConteudo().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(conteudoUnidadePaginaVO.getUnidadeConteudo())) {
			sql.append(" and unidadeconteudo = ").append(conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(conteudoUnidadePaginaVO)) {
			sql.append(" and conteudounidadepagina = ").append(conteudoUnidadePaginaVO.getCodigo());
		}
		sql.append(" limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		return new AnotacaoDisciplinaVO();
	}

	@Override
	public AnotacaoDisciplinaVO consultarAnotacaoDisciplinaPorDisciplinaMatriculaUnidadeConteudo(String matriculaVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM anotacaodisciplina");
		sql.append(" where 1=1 ");
		sql.append(" and matricula = '").append(matriculaVO).append("'");
		sql.append(" and responsavel = ").append(usuarioLogado.getPessoa().getCodigo());
		sql.append(" and disciplina = ").append(disciplinaVO.getCodigo());
		if (Uteis.isAtributoPreenchido(unidadeConteudoVO.getConteudo())) {
			sql.append(" and conteudo = ").append(unidadeConteudoVO.getConteudo().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(unidadeConteudoVO)) {
			sql.append(" and unidadeconteudo = ").append(unidadeConteudoVO.getCodigo());
		}
		sql.append(" and conteudounidadepagina is null");
		sql.append(" limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		return new AnotacaoDisciplinaVO();
	}

	@Override
	public boolean consultarExistenciaAnotacaoDisciplinaPorUnidadeConteudo(Integer unidadeConteudo, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select codigo from AnotacaoDisciplina ");
		if (Uteis.isAtributoPreenchido(unidadeConteudo)) {
			sqlStr.append("where unidadeConteudo  = ").append(unidadeConteudo);
		}
		sqlStr.append("and conteudounidadepagina is null");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	}

	public boolean consultarExistenciaAnotacaoDisciplinaPorUnidadeConteudoUnidadeConteudoPagina(Integer unidadeConteudo, Integer conteudoUnidadePagina, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select codigo from AnotacaoDisciplina ");
		if (Uteis.isAtributoPreenchido(conteudoUnidadePagina)) {
			sqlStr.append("where conteudoUnidadePagina  = ").append(conteudoUnidadePagina);
		} else if (Uteis.isAtributoPreenchido(unidadeConteudo)) {
			sqlStr.append("and unidadeconteudo = ").append(unidadeConteudo);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	}

	@Override
	public List<AnotacaoDisciplinaVO> consultaAnotacoesPorTemaAssuntoEConteudo(String matricula, Integer codigoTemaAssunto, Integer codigoConteudo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(codigoConteudo)) {
			sqlStr.append(" and conteudo.codigo = ").append(codigoConteudo);
		}
		if (Uteis.isAtributoPreenchido(codigoTemaAssunto)) {
			sqlStr.append(" and unidadeconteudo.temaassunto = ").append(codigoTemaAssunto);
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" and matricula.matricula = '").append(matricula).append("'");
			sqlStr.append(" and ad.publica = false ");
		} else {
			sqlStr.append(" and ad.publica = true ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return montarDadosBasicoConsulta(rs, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" ");
		sql.append(" SELECT ad.codigo as \"ad.codigo\", ad.datacriacao as \"ad.datacriacao\", ad.anotacao as \"ad.anotacao\", ad.palavraschaves as \"ad.palavraschaves\", ");
		sql.append(" ad.publica as \"ad.publica\", ad.nivelimportancia as \"ad.nivelimportancia\",  ");
		sql.append("  ad.conteudounidadepagina as \"ad.conteudounidadepagina\", ad.conteudounidadepaginarecursoeducacional as \"ad.conteudounidadepaginarecursoeducacional\",  ");
		sql.append(" disciplina.codigo as \"disciplina.codigo\",  disciplina.nome as \"disciplina.nome\", ");
		sql.append(" conteudo.codigo as \"conteudo.codigo\",  conteudo.descricao as \"conteudo.descricao\", ");
		sql.append(" unidadeconteudo.codigo as \"unidadeconteudo.codigo\",  unidadeconteudo.titulo as \"unidadeconteudo.titulo\",  unidadeconteudo.ordem as \"unidadeconteudo.ordem\",");
		sql.append(" conteudoUnidadePagina.codigo as \"conteudoUnidadePagina.codigo\",  conteudoUnidadePagina.titulo as \"conteudoUnidadePagina.titulo\",  conteudoUnidadePagina.pagina as \"conteudoUnidadePagina.pagina\",");
		sql.append(" pessoa.codigo as \"pessoa.codigo\",  pessoa.nome as \"pessoa.nome\", ");
		sql.append(" matricula.matricula as \"matricula.matricula\",  ");
		sql.append(" pessoa_mat.codigo as \"pessoa_mat.codigo\", pessoa_mat.nome as \"pessoa_mat.nome\" ");
		sql.append(" FROM anotacaodisciplina ad ");
		sql.append(" inner join disciplina  on disciplina.codigo = ad.disciplina ");
		sql.append(" inner join conteudo on conteudo.codigo = ad.conteudo ");
		sql.append(" inner join pessoa on pessoa.codigo = ad.responsavel ");
		sql.append(" inner join matricula on matricula.matricula= ad.matricula ");
		sql.append(" inner join pessoa as pessoa_mat on pessoa_mat.codigo = matricula.aluno ");
		sql.append(" left join unidadeconteudo on unidadeconteudo.codigo = ad.unidadeconteudo ");
		sql.append(" left join conteudoUnidadePagina on conteudoUnidadePagina.codigo = ad.conteudoUnidadePagina ");
		return sql;
	}
}
