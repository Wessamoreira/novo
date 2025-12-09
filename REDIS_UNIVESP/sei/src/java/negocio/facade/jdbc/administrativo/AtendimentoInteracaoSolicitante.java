package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade;


/**
 * 
 * @author Pedro
 */

@Repository
@Scope("singleton")
@Lazy
public class AtendimentoInteracaoSolicitante extends ControleAcesso implements AtendimentoInteracaoSolicitanteInterfaceFacade {
	protected static String idEntidade;

	public AtendimentoInteracaoSolicitante() throws Exception {
		super();
		setIdEntidade("Atendimento");

	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#excluirAtendimentoInteracaoSolicitanteVO(java.lang.Integer, negocio.comuns.arquitetura.UsuarioVO)
	 */
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#excluirAtendimentoInteracaoSolicitanteVO(java.lang.Integer, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAtendimentoInteracaoSolicitanteVO(Integer atendimento, UsuarioVO usuarioLogado) throws Exception {
		/**
         * Comentado 22/10/2014
         * @author Leonardo Riciolle
         */
		//ItemTitulacaoCurso.excluir(getIdEntidade());
		String sql = "DELETE FROM AtendimentoInteracaoSolicitante WHERE (atendimento = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { atendimento });
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#alterarAtendimentoInteracaoSolicitanteVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#alterarAtendimentoInteracaoSolicitanteVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAtendimentoInteracaoSolicitanteVO(Integer atendimento, List objetos, UsuarioVO usuarioLogado) throws Exception {
		String str = "DELETE FROM AtendimentoInteracaoSolicitante WHERE atendimento = " + atendimento;
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			AtendimentoInteracaoSolicitanteVO objeto = (AtendimentoInteracaoSolicitanteVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		getConexao().getJdbcTemplate().update(str);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			AtendimentoInteracaoSolicitanteVO objeto = (AtendimentoInteracaoSolicitanteVO) e.next();
			if (objeto.getCodigo().equals(0)) {
				objeto.getAtendimentoVO().setCodigo(atendimento);
				incluir(objeto, usuarioLogado);
			} else {
				alterar(objeto, usuarioLogado);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#incluirAtendimentoInteracaoSolicitanteVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#incluirAtendimentoInteracaoSolicitanteVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAtendimentoInteracaoSolicitanteVO(Integer atendimento, List<AtendimentoInteracaoSolicitanteVO> objetos, UsuarioVO usuarioLogado) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			AtendimentoInteracaoSolicitanteVO obj = (AtendimentoInteracaoSolicitanteVO) e.next();
			obj.getAtendimentoVO().setCodigo(atendimento);
			incluir(obj, usuarioLogado);
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>AtendimentoInteracaoSolicitanteVO</code>. Primeiramente valida
	 * os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>AtendimentoInteracaoSolicitanteVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AtendimentoInteracaoSolicitanteVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
			//AtendimentoInteracaoSolicitante.incluir(getIdEntidade());
			realizarUpperCaseDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO AtendimentoInteracaoSolicitante( atendimento, questionamentoOuvidor, questionamentoSolicitante,  dataRegistro , dataRegistroRespostaQuestionamento, usuarioquestionamento) ");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?) returning codigo");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(1, obj.getAtendimentoVO().getCodigo());
					sqlInserir.setString(2, obj.getQuestionamentoOuvidor());
					sqlInserir.setString(3, obj.getQuestionamentoSolicitante());
					sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));					
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataRegistroRespostaQuestionamento()));
					if(Uteis.isAtributoPreenchido(obj.getUsuarioQuestionamento())) {
						sqlInserir.setInt(6, obj.getUsuarioQuestionamento().getCodigo());
					}else {
						sqlInserir.setNull(6, 0);
					}
					
					
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
	 * <code>AtendimentoInteracaoSolicitanteVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>AtendimentoInteracaoSolicitanteVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AtendimentoInteracaoSolicitanteVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
			//AtendimentoInteracaoSolicitante.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);
			final StringBuilder sql = new StringBuilder("UPDATE AtendimentoInteracaoSolicitante set ");
			sql.append(" atendimento= ?, questionamentoOuvidor= ?, questionamentoSolicitante= ?,  dataRegistro= ?, dataRegistroRespostaQuestionamento=?, usuariorespostaquestionamento=? ");
			sql.append("  WHERE ((codigo = ?))");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getAtendimentoVO().getCodigo());
					sqlAlterar.setString(2, obj.getQuestionamentoOuvidor());
					sqlAlterar.setString(3, obj.getQuestionamentoSolicitante());
					sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));					
					sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataRegistroRespostaQuestionamento()));
					if (Uteis.isAtributoPreenchido(obj.getUsuarioRespostaQuestionamento())) {
						sqlAlterar.setInt(6, obj.getUsuarioRespostaQuestionamento().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#validarDados(negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO)
	 */
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#validarDados(negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO)
	 */
	@Override
	public void validarDados(AtendimentoInteracaoSolicitanteVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if(obj.getDataRegistro() != null  && ((obj.getQuestionamentoOuvidor() == null) || (Uteis.retiraTags(obj.getQuestionamentoOuvidor()).replaceAll("Untitled document", "").trim().isEmpty()))){
			throw new Exception(UteisJSF.internacionalizar("msg_AtendimentoInteracaoSolicitante_questionamentoOuvidor"));
		}
		if(obj.getDataRegistroRespostaQuestionamento() != null  && ((obj.getQuestionamentoSolicitante() == null) || (Uteis.retiraTags(obj.getQuestionamentoSolicitante()).replaceAll("Untitled document", "").trim().isEmpty()))){
			throw new Exception(UteisJSF.internacionalizar("msg_AtendimentoInteracaoSolicitante_questionamentoSolicitante"));
		}
		
		
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(AtendimentoInteracaoSolicitanteVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#consultarPorCodigoOuvidoria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#consultarPorCodigoOuvidoria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List consultarPorCodigoOuvidoria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = getSqlConsultaCompleta()+" WHERE  atendimento = ? ORDER BY codigo";
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TitulacaoCursoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
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
	public static AtendimentoInteracaoSolicitanteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		AtendimentoInteracaoSolicitanteVO obj = new AtendimentoInteracaoSolicitanteVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));		
		obj.getAtendimentoVO().setCodigo(dadosSQL.getInt("atendimento"));		
		obj.setQuestionamentoOuvidor(dadosSQL.getString("questionamentoOuvidor"));
		obj.setQuestionamentoSolicitante(dadosSQL.getString("questionamentoSolicitante"));
		obj.setDataRegistro(dadosSQL.getDate("dataRegistro"));
		obj.setDataRegistroRespostaQuestionamento(dadosSQL.getDate("dataRegistroRespostaQuestionamento"));
		obj.getUsuarioQuestionamento().setCodigo(dadosSQL.getInt("usuarioquestionamento"));
		obj.getUsuarioQuestionamento().setNome(dadosSQL.getString("uq_nome"));
		obj.getUsuarioRespostaQuestionamento().setCodigo(dadosSQL.getInt("usuariorespostaquestionamento"));
		obj.getUsuarioRespostaQuestionamento().setNome(dadosSQL.getString("urq_nome"));
		obj.setNovoObj(false);
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}		
		return obj;
	}
	
	private StringBuilder getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT AtendimentoInteracaoSolicitante.*, uq.nome as uq_nome, urq.nome as urq_nome FROM AtendimentoInteracaoSolicitante ");
		sql.append(" left join usuario as uq on uq.codigo = AtendimentoInteracaoSolicitante.usuarioquestionamento ");
		sql.append(" left join usuario as urq on urq.codigo = AtendimentoInteracaoSolicitante.usuariorespostaquestionamento ");
		return sql;
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoSolicitanteInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public AtendimentoInteracaoSolicitanteVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = getSqlConsultaCompleta()+" WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( AtendimentoInteracaoSolicitante ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return AtendimentoInteracaoSolicitante.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		AtendimentoInteracaoSolicitante.idEntidade = idEntidade;
	}
}
