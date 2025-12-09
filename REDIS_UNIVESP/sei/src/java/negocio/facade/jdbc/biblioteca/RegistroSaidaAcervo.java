package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ItemRegistroSaidaAcervoVO;
import negocio.comuns.biblioteca.RegistroSaidaAcervoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.SituacaoHistoricoExemplar;
import negocio.comuns.utilitarias.dominios.TipoSaidaAcervo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.RegistroSaidaAcervoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>RegistroSaidaAcervoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>RegistroSaidaAcervoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see RegistroSaidaAcervoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class RegistroSaidaAcervo extends ControleAcesso implements RegistroSaidaAcervoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1937638147629178593L;
	protected static String idEntidade;

	public RegistroSaidaAcervo() throws Exception {
		super();
		setIdEntidade("RegistroSaidaAcervo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>RegistroSaidaAcervoVO</code>.
	 */
	public RegistroSaidaAcervoVO novo() throws Exception {
		RegistroSaidaAcervo.incluir(getIdEntidade());
		RegistroSaidaAcervoVO obj = new RegistroSaidaAcervoVO();
		return obj;
	}

	public void inicializarDadosRegistroSaidaAcervoNovo(RegistroSaidaAcervoVO registroSaidaAcervoVO, UsuarioVO usuario) throws Exception {
		registroSaidaAcervoVO.setFuncionario(usuario);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>RegistroSaidaAcervoVO</code>.
	 * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroSaidaAcervoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RegistroSaidaAcervoVO obj, UsuarioVO usuario) throws Exception {
		try {

			RegistroSaidaAcervoVO.validarDados(obj);
			RegistroSaidaAcervo.incluir(getIdEntidade(), true, usuario);
			obj.realizarUpperCaseDados();
			final StringBuilder sql = new StringBuilder();
			
			sql.append("INSERT INTO RegistroSaidaAcervo( funcionario, data, justificativa, biblioteca ) VALUES ( ?, ?, ?, ? ) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(3, obj.getJustificativa());
					if (obj.getBiblioteca().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getBiblioteca().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			// Modifica a situaçãoAtual de cada exemplar. Em todos os casos o
			// exemplar é inutilizado. Inclui um registro no histórico Exemplar.
			modificarSituacaoAtualExemplarRegistrarHistoricoExemplar(obj, usuario);
			getFacadeFactory().getItemRegistroSaidaAcervoFacade().incluirItemRegistroSaidaAcervos(obj.getCodigo(), obj.getItemRegistroSaidaAcervoVOs());
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Método que muda a situaçãoAtual do exemplar para INUTILIZADO. Registra um históricoExemplar.
	 * 
	 * @param registroSaidaAcervoVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void modificarSituacaoAtualExemplarRegistrarHistoricoExemplar(RegistroSaidaAcervoVO registroSaidaAcervoVO, UsuarioVO usuario) throws Exception {
		for (ItemRegistroSaidaAcervoVO itemRegistroSaidaAcervoVO : registroSaidaAcervoVO.getItemRegistroSaidaAcervoVOs()) {
			if (itemRegistroSaidaAcervoVO.getTipoSaida().equals(TipoSaidaAcervo.RESTAURACAO.getValor())) {
				getFacadeFactory().getExemplarFacade().executarAlteracaoSituacaoExemplares(itemRegistroSaidaAcervoVO.getExemplar(), SituacaoExemplar.INUTILIZADO.getValor(), usuario);
				getFacadeFactory().getHistoricoExemplarFacade().registrarHistoricoExemplarParaRegistroAcervo(itemRegistroSaidaAcervoVO.getExemplar(), SituacaoHistoricoExemplar.RESTAURACAO.getValor(), usuario);
			} else {
				getFacadeFactory().getExemplarFacade().executarAlteracaoSituacaoExemplares(itemRegistroSaidaAcervoVO.getExemplar(), SituacaoExemplar.INUTILIZADO.getValor(), usuario);
				getFacadeFactory().getHistoricoExemplarFacade().registrarHistoricoExemplarParaRegistroAcervo(itemRegistroSaidaAcervoVO.getExemplar(), SituacaoHistoricoExemplar.REMOVIDO.getValor(), usuario);
				// Ao inutilizar um exemplar, deve-se tirar 1 unidade da sua obra corespondente.
				getFacadeFactory().getCatalogoFacade().subtrairUmExemplarNumeroExemplaresCatalogo(itemRegistroSaidaAcervoVO.getExemplar().getCatalogo());
			}
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroSaidaAcervoVO</code>. Sempre
	 * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroSaidaAcervoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RegistroSaidaAcervoVO obj, UsuarioVO usuario) throws Exception {
		try {

			RegistroSaidaAcervoVO.validarDados(obj);
			RegistroSaidaAcervo.alterar(getIdEntidade(), true, usuario);
			obj.realizarUpperCaseDados();

			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE RegistroSaidaAcervo set funcionario=?, data=?, justificativa=?, biblioteca=? WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(3, obj.getJustificativa());
					if (obj.getBiblioteca().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getBiblioteca().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setInt(5, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getItemRegistroSaidaAcervoFacade().alterarItemRegistroSaidaAcervos(obj.getCodigo(), obj.getItemRegistroSaidaAcervoVOs());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>RegistroSaidaAcervoVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroSaidaAcervoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RegistroSaidaAcervoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			RegistroSaidaAcervo.excluir(getIdEntidade(), true, usuarioVO);
			sql.append("DELETE FROM RegistroSaidaAcervo WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
			getFacadeFactory().getItemRegistroSaidaAcervoFacade().excluirItemRegistroSaidaAcervos(obj.getCodigo());
			for(ItemRegistroSaidaAcervoVO itemRegistroSaidaAcervoVO: obj.getItemRegistroSaidaAcervoVOs()){
				if(!itemRegistroSaidaAcervoVO.getExemplar().getSituacaoAtual().equals(SituacaoExemplar.DISPONIVEL.getValor()) && !itemRegistroSaidaAcervoVO.getExemplar().getSituacaoAtual().equals(SituacaoExemplar.EMPRESTADO.getValor())){
					getFacadeFactory().getExemplarFacade().executarAlteracaoSituacaoExemplares(itemRegistroSaidaAcervoVO.getExemplar(), SituacaoExemplar.DISPONIVEL.getValor(), usuarioVO);
				}
			}	
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroSaidaAcervo</code> através do valor do atributo
	 * <code>nome</code> da classe <code>Biblioteca</code> Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>RegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RegistroSaidaAcervoVO> consultarPorNomeBiblioteca(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT distinct RegistroSaidaAcervo.* FROM RegistroSaidaAcervo inner join  Biblioteca on RegistroSaidaAcervo.biblioteca = Biblioteca.codigo "
				+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo "
				+ " WHERE upper( Biblioteca.nome ) like('" + valorConsulta.toUpperCase() + "%') ";
		if (unidadeEnsino > 0) {
			sqlStr += " and unidadeensinobiblioteca.unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY RegistroSaidaAcervo.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	/**
	 * Responsável por realizar uma consulta de <code>RegistroSaidaAcervo</code> através do valor do atributo
	 * <code>nome</code> da classe <code>Biblioteca</code> Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>RegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	public List<RegistroSaidaAcervoVO> consultarPorTombo(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT distinct RegistroSaidaAcervo.* FROM RegistroSaidaAcervo inner join  Biblioteca on RegistroSaidaAcervo.biblioteca = Biblioteca.codigo "
				+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo "
				+ " WHERE exists (select itemregistrosaidaacervo.codigo from itemregistrosaidaacervo inner join exemplar on exemplar.codigo = itemregistrosaidaacervo.exemplar "
				+ " and itemregistrosaidaacervo.RegistroSaidaAcervo = RegistroSaidaAcervo.codigo "
				+ " and exemplar.codigobarra ilike ('"+valorConsulta+"'))";
		
		if (unidadeEnsino > 0) {
			sqlStr += " and unidadeensinobiblioteca.unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY RegistroSaidaAcervo.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroSaidaAcervo</code> através do valor do atributo
	 * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>RegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RegistroSaidaAcervoVO> consultarPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct RegistroSaidaAcervo.* FROM RegistroSaidaAcervo "
				+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = RegistroSaidaAcervo.biblioteca "
				+ " WHERE ((RegistroSaidaAcervo.data = '" + Uteis.getDataJDBC(prmIni) + "') and (RegistroSaidaAcervo.data <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (unidadeEnsino > 0) {
			sqlStr += " and unidadeensinobiblioteca.unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY RegistroSaidaAcervo.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroSaidaAcervo</code> através do valor do atributo
	 * <code>nome</code> da classe <code>Usuario</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza
	 * o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>RegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RegistroSaidaAcervoVO> consultarPorNomeUsuario(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT distinct RegistroSaidaAcervo.* FROM RegistroSaidaAcervo inner join Usuario on RegistroSaidaAcervo.funcionario = Usuario.codigo "
				+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = RegistroSaidaAcervo.biblioteca "
				+ " WHERE upper( Usuario.nome ) like('"	+ valorConsulta.toUpperCase() + "%') ";
		if (unidadeEnsino > 0) {
			sqlStr += " and unidadeensinobiblioteca.unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY RegistroSaidaAcervo.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>RegistroSaidaAcervo</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>RegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RegistroSaidaAcervoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct RegistroSaidaAcervo.* FROM RegistroSaidaAcervo "
				+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = RegistroSaidaAcervo.biblioteca "
				+ "WHERE RegistroSaidaAcervo.codigo = " + valorConsulta.intValue() + " ";
		if (unidadeEnsino > 0) {
			sqlStr += " and unidadeensinobiblioteca.unidadeensino = " + unidadeEnsino;
		}
		sqlStr += " ORDER BY RegistroSaidaAcervo.codigo"; 
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>RegistroSaidaAcervoVO</code> resultantes da consulta.
	 */
	public static List<RegistroSaidaAcervoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RegistroSaidaAcervoVO> vetResultado = new ArrayList<RegistroSaidaAcervoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>RegistroSaidaAcervoVO</code>.
	 * 
	 * @return O objeto da classe <code>RegistroSaidaAcervoVO</code> com os dados devidamente montados.
	 */
	public static RegistroSaidaAcervoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegistroSaidaAcervoVO obj = new RegistroSaidaAcervoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.getBiblioteca().setCodigo(dadosSQL.getInt("biblioteca"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setItemRegistroSaidaAcervoVOs(getFacadeFactory().getItemRegistroSaidaAcervoFacade().consultarItemRegistroSaidaAcervos(obj.getCodigo(), false, nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosFuncionario(obj, nivelMontarDados, usuario);
		montarDadosBiblioteca(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>BibliotecaVO</code> relacionado ao objeto
	 * <code>RegistroSaidaAcervoVO</code>. Faz uso da chave primária da classe <code>BibliotecaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosBiblioteca(RegistroSaidaAcervoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBiblioteca().getCodigo().intValue() == 0) {
			obj.setBiblioteca(new BibliotecaVO());
			return;
		}
		obj.setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(obj.getBiblioteca().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto
	 * <code>RegistroSaidaAcervoVO</code>. Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosFuncionario(RegistroSaidaAcervoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionario().getCodigo().intValue() == 0) {
			obj.setFuncionario(new UsuarioVO());
			return;
		}
		obj.setFuncionario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), nivelMontarDados, usuario));
	}


	/**
	 * Operação responsável por localizar um objeto da classe <code>RegistroSaidaAcervoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public RegistroSaidaAcervoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM RegistroSaidaAcervo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return RegistroSaidaAcervo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		RegistroSaidaAcervo.idEntidade = idEntidade;
	}
}