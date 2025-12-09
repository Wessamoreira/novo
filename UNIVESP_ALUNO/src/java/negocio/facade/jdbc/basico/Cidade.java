package negocio.facade.jdbc.basico;

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

import controle.arquitetura.ControleConsultaCidade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.CidadeInterfaceFacade;
import webservice.servicos.CidadeObject;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CidadeVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CidadeVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CidadeVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Cidade extends ControleAcesso implements CidadeInterfaceFacade {

	protected static String idEntidade;

	public Cidade() throws Exception {
		super();
		setIdEntidade("Cidade");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>CidadeVO</code>.SuperFacade
	 */
	public CidadeVO novo() throws Exception {
		Cidade.incluir(getIdEntidade());
		CidadeVO obj = new CidadeVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CidadeVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CidadeVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CidadeVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			CidadeVO.validarDados(obj);
//			Cidade.incluir(getIdEntidade(), true, usuarioVO);

			final String sql = "INSERT INTO Cidade( nome, estado, codigoinep, codigoibge, codigodms ) VALUES ( ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setInt(2, obj.getEstado().getCodigo());
					sqlInserir.setInt(3, obj.getCodigoInep());
					sqlInserir.setString(4, obj.getCodigoIBGE());
					sqlInserir.setInt(5, obj.getCodigoDMS());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			setIncluirSeiLog("cidade", obj.getCodigo(), usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
			//inserir(obj, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>CidadeVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CidadeVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CidadeVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			CidadeVO.validarDados(obj);
			alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE Cidade set nome=?, estado=?, codigoinep=?, codigoibge=?, codigodms=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setInt(2, obj.getEstado().getCodigo());
					sqlAlterar.setInt(3, obj.getCodigoInep());
					sqlAlterar.setString(4, obj.getCodigoIBGE());
					sqlAlterar.setInt(5, obj.getCodigoDMS());
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			setAlterarSeiLog("cidade", obj.getCodigo(), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoIBGECidade(final CidadeVO obj) throws Exception {
		try {
			CidadeVO.validarDados(obj);
			Cidade.alterar(getIdEntidade());
			final String sql = "UPDATE Cidade set codigoibge=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getCodigoIBGE());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>CidadeVO</code>. Sempre localiza o registro a ser excluído através
	 * da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CidadeVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CidadeVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM Cidade WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cidade</code> através do
	 * valor do atributo <code>String estado</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CidadeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorEstado(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cidade INNER JOIN estado on estado.codigo = cidade.estado WHERE lower (estado.nome) like(?) ORDER BY estado";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase()+"%");
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));

	}

	/**
	 * Responsável por realizar uma consulta de <code>Cidade</code> através do
	 * valor do atributo <code>String nome</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CidadeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<CidadeVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cidade WHERE lower (sem_acentos(nome)) ilike(sem_acentos(?)) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public List consultarPorNomeCodigoEstado(String valorConsulta, boolean controlarAcesso, Integer estado, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cidade WHERE sem_acentos(nome) ilike(sem_acentos(?)) AND estado = ? ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase() + "%", estado);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cidade</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CidadeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<CidadeVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cidade WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public List consultarPorCodigoEstado(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cidade INNER JOIN Estado ON cidade.estado = estado.codigo WHERE estado.codigo = " + valorConsulta.intValue() + " ORDER BY cidade.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cidade</code> Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 * 
	 */
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT cidade.codigo, cidade.nome, estado.sigla AS \"estado.sigla\", estado.nome AS \"estado.nome\", estado.codigo AS \"estado.codigo\", count(*) over() as qtde_total_registros FROM cidade ");
		str.append("INNER JOIN estado ON estado.codigo = cidade.estado ");
		return str;
	}

	public List<CidadeVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, int limit, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct cidade.codigo, cidade.nome ");
		sqlStr.append("from cidade ");
		sqlStr.append("WHERE sem_acentos(lower(cidade.nome)) ilike(sem_acentos(?)) ");
		sqlStr.append(" ORDER BY cidade.nome ");
		sqlStr.append(" limit ").append(limit);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		sqlStr = null;
		List<CidadeVO> listaCidades = new ArrayList<CidadeVO>();
		while (tabelaResultado.next()) {
			CidadeVO obj = new CidadeVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.setNome(tabelaResultado.getString("nome"));
			listaCidades.add(obj);
		}
		return listaCidades;
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<CidadeVO> consultaRapidaPorCodigo(Integer codCidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE cidade.codigo = ");
		sqlStr.append(codCidade.intValue());
		sqlStr.append(" ORDER BY cidade.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public CidadeVO consultaCidadeRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(cidade.nome) = (sem_acentos(?)) ");
		sqlStr.append(" ORDER BY cidade.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		if (!tabelaResultado.next()) {
			return null;
		}
		CidadeVO cidadeVO = new CidadeVO();
		montarDadosBasico(cidadeVO, tabelaResultado);
		return cidadeVO;
	}
	
	public List<CidadeVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(cidade.nome) ilike(sem_acentos(?)) ");
		sqlStr.append(" ORDER BY cidade.nome LIMIT 100");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public CidadeVO consultarCidadeUnidadeEnsinoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("inner join unidadeEnsino on unidadeEnsino.cidade = cidade.codigo ");
		sqlStr.append("inner join matricula on matricula.unidadeensino = unidadeensino.codigo ");
		sqlStr.append("where matricula.matricula ilike ? ");
		sqlStr.append(" ORDER BY cidade.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		if (!tabelaResultado.next()) {
			return null;
		}
		CidadeVO cidadeVO = new CidadeVO();
		montarDadosBasico(cidadeVO, tabelaResultado);
		return cidadeVO;
	}

	public CidadeVO consultarCidadePorUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("inner join unidadeEnsino on unidadeEnsino.cidade = cidade.codigo ");
		sqlStr.append("where unidadeEnsino.codigo = ");
		sqlStr.append(valorConsulta);
		sqlStr.append(" ORDER BY cidade.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		CidadeVO cidadeVO = new CidadeVO();
		montarDadosBasico(cidadeVO, tabelaResultado);
		return cidadeVO;
	}

	public CidadeVO consultarCidadePorCodigoIBGENomeCidade(String codigoIBGE, String nomeCidade, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT cidade.codigo, cidade.codigoIBGE, cidade.nome, estado.sigla AS \"estado.sigla\", estado.nome AS \"estado.nome\", estado.codigo AS \"estado.codigo\" FROM cidade ");
		sqlStr.append("INNER JOIN estado ON estado.codigo = cidade.estado ");
		sqlStr.append("where ");
		sqlStr.append(" cidade.codigoIBGE = ? ");		
		sqlStr.append(" ORDER BY cidade.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoIBGE);
		if (!tabelaResultado.next()) {
			sqlStr = new StringBuffer();
			sqlStr.append("SELECT cidade.codigo, cidade.codigoIBGE, cidade.nome, estado.sigla AS \"estado.sigla\", estado.nome AS \"estado.nome\", estado.codigo AS \"estado.codigo\" FROM cidade ");
			sqlStr.append("INNER JOIN estado ON estado.codigo = cidade.estado ");
			sqlStr.append("where ");
			sqlStr.append(" cidade.nome ilike ? ");
			sqlStr.append(" ORDER BY cidade.nome");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeCidade);
			if (!tabelaResultado.next()) {
				return null;
			}
			CidadeVO cidadeVO = new CidadeVO();
			montarDadosBasico(cidadeVO, tabelaResultado);
			cidadeVO.setCodigoIBGE(codigoIBGE);
			return cidadeVO;			
		}
		CidadeVO cidadeVO = new CidadeVO();
		montarDadosBasico(cidadeVO, tabelaResultado);
		cidadeVO.setCodigoIBGE(tabelaResultado.getString("codigoIBGE"));
		return cidadeVO;
	}

	public List<CidadeVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<CidadeVO> vetResultado = new ArrayList<CidadeVO>(0);
		while (tabelaResultado.next()) {
			CidadeVO obj = new CidadeVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(CidadeVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Unidade Ensino
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));

		// Dados da Cidade
		obj.getEstado().setCodigo(new Integer(dadosSQL.getInt("estado.codigo")));
		obj.getEstado().setNome(dadosSQL.getString("estado.nome"));
		obj.getEstado().setSigla(dadosSQL.getString("estado.sigla"));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>EstadoVO</code>
	 *         resultantes da consulta.
	 */
	public  List<CidadeVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CidadeVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>CidadeVO</code>.
	 * 
	 * @return O objeto da classe <code>CidadeVO</code> com os dados devidamente
	 *         montados.
	 */
	public  CidadeVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CidadeVO obj = new CidadeVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setCep(dadosSQL.getString("cep"));
		obj.getEstado().setCodigo(new Integer(dadosSQL.getInt("estado")));
		obj.setCodigoInep(dadosSQL.getInt("codigoinep"));
		obj.setCodigoIBGE(dadosSQL.getString("codigoibge"));
		obj.setCodigoDistrito(dadosSQL.getInt("codigoDistrito"));
		obj.setCodigoDMS(dadosSQL.getInt("codigoDMS"));
		montarDadosEstado(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PaizVO</code> relacionado ao objeto <code>PessoaVO</code>. Faz uso
	 * da chave primária da classe <code>PaizVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosEstado(CidadeVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getEstado().getCodigo().intValue() == 0) {
			obj.setEstado(new EstadoVO());
			return;
		}
		obj.setEstado(getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(obj.getEstado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>CidadeVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public CidadeVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getCidadeVO(codigoPrm, usuario);
	}
	
	@Override
	public CidadeVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Cidade WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });

		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		
		return new CidadeVO();
	}

	public CidadeVO consultarPorUnidadeEnsinoMatriz(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select cidade.* from cidade ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.cidade = cidade.codigo ");
		sqlStr.append(" where unidadeensino.matriz = true ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (!tabelaResultado.next()) {
			return null;
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	}

	public CidadeVO consultarPorMatriculaAluno(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT cidade.* FROM cidade ");
		sqlStr.append(" INNER JOIN  pessoa ON cidade.codigo = pessoa.cidade ");
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" where matricula.matricula = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);

		if (!tabelaResultado.next()) {
			return null;
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Cidade.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Cidade.idEntidade = idEntidade;
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cidade</code> através do
	 * valor do atributo <code>sigla</code> da classe <code>Estado</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>CidadeVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<CidadeVO> consultarPorSiglaEstado(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cidade.* FROM Cidade, Estado WHERE Cidade.estado = Estado.codigo and upper( Estado.sigla ) like( ? ) ORDER BY Estado.sigla";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cidade</code> através do
	 * valor do atributo <code>String cep</code>. Retorna os objetos, com início
	 * do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CidadeVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCep(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cidade WHERE upper( cep ) like(?) ORDER BY cep";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<CidadeObject> consultarPorNomeRS(String nome) throws Exception{
		String sqlStr = "SELECT * FROM Cidade";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsultaRS(tabelaResultado);
	}
	
	@Override
	public List<CidadeObject> consultarPorCodigoEstadoRS(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cidade.* FROM Cidade INNER JOIN Estado ON cidade.estado = estado.codigo WHERE estado.codigo = " + valorConsulta.intValue() + " ORDER BY cidade.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsultaRS(tabelaResultado));
	}

	private List<CidadeObject> montarDadosConsultaRS(SqlRowSet tabelaResultado) throws Exception {
		List<CidadeObject> listaConsulta = new ArrayList<CidadeObject>();
		while (tabelaResultado.next()) {
			listaConsulta.add(montarDadosRS(tabelaResultado));
		}
		return listaConsulta;
	}

	private CidadeObject montarDadosRS(SqlRowSet dadosSQL) throws Exception {
		CidadeObject co = new CidadeObject();
		co.setCodigo(dadosSQL.getInt("codigo"));
		co.setCep(dadosSQL.getString("cep"));
		co.setCodigoInep(dadosSQL.getInt("codigoInep"));
		co.getEstado().setCodigo(dadosSQL.getInt("estado"));
		co.setNome(dadosSQL.getString("nome"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("estado"))) {
			co.setEstado(getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(dadosSQL.getInt("estado"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));	
		}
		return co;
	}
	
	public CidadeVO consultarDadosComboBoxPorBiblioteca(Integer biblioteca, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select cidade.codigo, cidade.nome from cidade ");
		sb.append(" inner join biblioteca on biblioteca.cidade = cidade.codigo ");
		sb.append(" where biblioteca.codigo = ").append(biblioteca);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		CidadeVO obj = null;
		if (tabelaResultado.next()) {
			obj = new CidadeVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
		}
		return obj;
		
	}
	
	@Override
	public Integer consultarCodigoDMS(CidadeVO cidade, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT Cidade.codigoDMS FROM Cidade ");
		sb.append(" INNER JOIN Estado ON cidade.estado = estado.codigo "); 
		sb.append(" WHERE estado.codigo = ").append(cidade.getEstado().getCodigo());
		sb.append(" AND cidade.codigo = ").append(cidade.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Integer codigo = 0;
		if (tabelaResultado.next()) {
			codigo = (tabelaResultado.getInt("codigoDMS"));
		}
		return codigo;
		
	}
	
	@Override
	public List<CidadeVO> consultarPorNomeSiglaEstado(String nomeCidade, String siglaEstado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);		
		StringBuilder sqlStr = new StringBuilder("SELECT Cidade.*, Estado.sigla FROM Cidade inner join Estado on Cidade.estado = Estado.codigo where 1 = 1 ");
		sqlStr.append("	and  (sem_acentos(Cidade.nome)) ilike(sem_acentos(?)) ");
		if(Uteis.isAtributoPreenchido(siglaEstado)) {
			sqlStr.append("	and upper(Estado.sigla) like(upper(?)) ");
		}
		sqlStr.append("	ORDER BY Estado.sigla, Cidade.nome");
		SqlRowSet tabelaResultado =  null;
		if(Uteis.isAtributoPreenchido(siglaEstado)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeCidade.toUpperCase() + "%", siglaEstado.toUpperCase());
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeCidade.toUpperCase() + "%");
		}
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public CidadeVO consultarPorNomeCidadeSiglaEstado(String nomeCidade, String siglaEstado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);		
		StringBuilder sqlStr = new StringBuilder("SELECT Cidade.*, Estado.sigla FROM Cidade inner join Estado on Cidade.estado = Estado.codigo where 1 = 1 ");
		sqlStr.append("	and  trim(sem_acentos(Cidade.nome)) ilike(trim(sem_acentos(?))) ");
		sqlStr.append("	and upper(Estado.sigla) like(upper(?)) ");
		sqlStr.append("	ORDER BY Estado.sigla, Cidade.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeCidade.toUpperCase() + "%", siglaEstado.toUpperCase());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return new CidadeVO();
	}
	
	@Override
	public void consultarCidade(ControleConsultaCidade controleConsultaCidade, Boolean controleAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controleAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		List<Object> filtros = new ArrayList<Object>(0);
		sql.append(" WHERE ");
		if (controleConsultaCidade.isNomeCidade()) {
			sql.append(" sem_acentos(cidade.nome) ILIKE sem_acentos(?) ");
			filtros.add(controleConsultaCidade.getValorConsulta() + PERCENT);
		} else if (controleConsultaCidade.isSiglaEstado()) {
			sql.append(" estado.codigo IN (?) ");
			filtros.add(controleConsultaCidade.getEstado());
		}
		sql.append(" ORDER BY cidade.nome");
		sql.append(" LIMIT ").append(controleConsultaCidade.getLimitePorPagina());
		sql.append(" OFFSET ").append(controleConsultaCidade.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		controleConsultaCidade.setTotalRegistrosEncontrados(0);
		if (tabelaResultado.next()) {
			controleConsultaCidade.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsultaCidade.setListaConsulta(montarDadosConsultaRapida(tabelaResultado));
		} else {
			controleConsultaCidade.setListaConsulta(new ArrayList<>(0));
		}
	}

}
