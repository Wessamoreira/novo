package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.administrativo.TipagemOuvidoriaVO;
import negocio.comuns.administrativo.enumeradores.TipoOuvidoriaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.TipagemOuvidoriaInterfaceFacade;

/**
 * 
 * @author Pedro
 */

@Repository
@Scope("singleton")
@Lazy
public class TipagemOuvidoria extends ControleAcesso implements TipagemOuvidoriaInterfaceFacade {

	protected static String idEntidade;

	public TipagemOuvidoria() throws Exception {
		super();
		setIdEntidade("TipagemOuvidoria");

	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>TipagemOuvidoriaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipagemOuvidoriaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipagemOuvidoriaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			incluir(getIdEntidade(), true, usuario);
			realizarUpperCaseDados(obj);
			final String sql = "INSERT INTO TipagemOuvidoria( descricao, tipoOuvidoriaEnum ) VALUES ( ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setString(2, obj.getTipoOuvidoriaEnum().name());
					return sqlInserir;

				}
			}, new ResultSetExtractor() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>TipagemOuvidoriaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipagemOuvidoriaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipagemOuvidoriaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			alterar(getIdEntidade(), true, usuario);
			realizarUpperCaseDados(obj);
			final String sql = "UPDATE TipagemOuvidoria set descricao=?, tipoOuvidoriaEnum=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setString(2, obj.getTipoOuvidoriaEnum().name());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.TipagemOuvidoriaInterfaceFacade#excluir(negocio.comuns.administrativo.TipagemOuvidoriaVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipagemOuvidoriaVO obj, UsuarioVO usuario) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM TipagemOuvidoria WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.TipagemOuvidoriaInterfaceFacade#persistir(negocio.comuns.administrativo.TipagemOuvidoriaVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(TipagemOuvidoriaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuario);
		} else {
			alterar(obj, usuario);
		}
	}

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.TipagemOuvidoriaInterfaceFacade#validarDados(negocio.comuns.administrativo.TipagemOuvidoriaVO)
	 */
	@Override
	public void validarDados(TipagemOuvidoriaVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}

		if ((obj.getDescricao() == null) || (obj.getDescricao().isEmpty())) {
			throw new Exception(UteisJSF.internacionalizar("msg_TipagemOuvidoria_descricao"));
		}
		if (obj.getTipoOuvidoriaEnum() == null) {
			throw new Exception(UteisJSF.internacionalizar("msg_TipagemOuvidoria_tipoOuvidoriaEnum"));
		}

	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>TipagemOuvidoriaVO</code>.
	 */
	public void validarUnicidade(List<TipagemOuvidoriaVO> lista, TipagemOuvidoriaVO obj) throws ConsistirException {
		for (TipagemOuvidoriaVO repetido : lista) {
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(TipagemOuvidoriaVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {			
			return;
		}
		obj.setDescricao(obj.getDescricao().toUpperCase());
	}

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.TipagemOuvidoriaInterfaceFacade#consultar(java.lang.String, java.lang.String, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List<TipagemOuvidoriaVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (campoConsulta.equals("CODIGO")) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return consultarPorCodigo(valorInt, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("DESCRICAO")) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarPorDescricao(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
		}
		return new ArrayList<>(0);
	}

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Curso</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TitulacaoCursoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TipagemOuvidoriaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("SELECT TipagemOuvidoria.* FROM TipagemOuvidoria ");
		sqlSb.append(" WHERE TipagemOuvidoria.descricao ilike ? ");
		sqlSb.append(" ORDER BY TipagemOuvidoria.descricao");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlSb.toString(), valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>TitulacaoCurso</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TitulacaoCursoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	public List<TipagemOuvidoriaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TipagemOuvidoria WHERE codigo >= ?  ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TitulacaoCursoVO</code> resultantes da consulta.
	 */
	public static List<TipagemOuvidoriaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<TipagemOuvidoriaVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TitulacaoCursoVO</code>.
	 * 
	 * @return O objeto da classe <code>TitulacaoCursoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static TipagemOuvidoriaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TipagemOuvidoriaVO obj = new TipagemOuvidoriaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setTipoOuvidoriaEnum(TipoOuvidoriaEnum.valueOf(dadosSQL.getString("tipoOuvidoriaEnum")));
		obj.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		return obj;
	}

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.TipagemOuvidoriaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public TipagemOuvidoriaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM TipagemOuvidoria WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( TipagemOuvidoria ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TipagemOuvidoria.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		TipagemOuvidoria.idEntidade = idEntidade;
	}

}
