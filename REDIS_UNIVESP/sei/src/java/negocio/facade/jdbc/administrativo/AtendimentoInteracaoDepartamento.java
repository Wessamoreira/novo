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

import negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO;
import negocio.comuns.administrativo.AtendimentoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade;

/**
 * 
 * @author Pedro
 */

@Repository
@Scope("singleton")
@Lazy
public class AtendimentoInteracaoDepartamento extends ControleAcesso implements AtendimentoInteracaoDepartamentoInterfaceFacade {

	protected static String idEntidade;

	public AtendimentoInteracaoDepartamento() throws Exception {
		super();
		setIdEntidade("Atendimento");

	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#excluirAtendimentoInteracaoDepartamentoVO(java.lang.Integer, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAtendimentoInteracaoDepartamentoVO(Integer atendimento, UsuarioVO usuarioLogado) throws Exception {
		/**
         * Comentado 22/10/2014
         * @author Leonardo Riciolle
         */
		//ItemTitulacaoCurso.excluir(getIdEntidade());
		String sql = "DELETE FROM AtendimentoInteracaoDepartamento WHERE (atendimento = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { atendimento });
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#alterarAtendimentoInteracaoDepartamentoVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAtendimentoInteracaoDepartamentoVO(AtendimentoVO atendimento, List objetos, UsuarioVO usuarioLogado , Integer codigoRespostaAtendimentoInteracaoSolicitanteVO) throws Exception {
		String str = "DELETE FROM AtendimentoInteracaoDepartamento WHERE atendimento = " + atendimento.getCodigo();
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			AtendimentoInteracaoDepartamentoVO objeto = (AtendimentoInteracaoDepartamentoVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		getConexao().getJdbcTemplate().update(str);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			AtendimentoInteracaoDepartamentoVO objeto = (AtendimentoInteracaoDepartamentoVO) e.next();
			if (objeto.getCodigo().equals(0)) {
				objeto.getAtendimentoVO().setCodigo(atendimento.getCodigo());
				incluir(atendimento , objeto, usuarioLogado);
			} else {
				if (objeto.getMensagemEnviada() == true && (objeto.getCodigo().equals(codigoRespostaAtendimentoInteracaoSolicitanteVO))) {									
				alterar(atendimento ,objeto, usuarioLogado);
				}
			}
		}
		
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#incluirAtendimentoInteracaoDepartamentoVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAtendimentoInteracaoDepartamentoVO(AtendimentoVO atendimento, List<AtendimentoInteracaoDepartamentoVO> objetos, UsuarioVO usuarioLogado) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			AtendimentoInteracaoDepartamentoVO obj = (AtendimentoInteracaoDepartamentoVO) e.next();
			obj.getAtendimentoVO().setCodigo(atendimento.getCodigo());
			incluir(atendimento ,obj, usuarioLogado);
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>AtendimentoInteracaoDepartamentoVO</code>. Primeiramente valida
	 * os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>AtendimentoInteracaoDepartamentoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AtendimentoVO atendimento , AtendimentoInteracaoDepartamentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
			//AtendimentoInteracaoDepartamento.incluir(getIdEntidade());
			realizarUpperCaseDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO AtendimentoInteracaoDepartamento( atendimento, questionamento, resposta,  dataRegistro, funcionario, dataRegistroResposta, ");
			sql.append(" departamento, atendimentoAtrasado , mensagemEnviada, usuarioquestionamento ");
			sql.append(" ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? , ?, ? ) returning codigo");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(1, obj.getAtendimentoVO().getCodigo());
					sqlInserir.setString(2, obj.getQuestionamento());
					sqlInserir.setString(3, obj.getResposta());
					sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					sqlInserir.setInt(5, obj.getFuncionarioVO().getCodigo());
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataRegistroResposta()));
					sqlInserir.setInt(7, obj.getDepartamento().getCodigo());
					sqlInserir.setBoolean(8, obj.getAtendimentoAtrasado());
					sqlInserir.setBoolean(9, true);
					if (Uteis.isAtributoPreenchido(obj.getUsuarioquestionamento())) {
						sqlInserir.setInt(10, obj.getUsuarioquestionamento().getCodigo());
					} else {
						sqlInserir.setNull(10, 0);
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
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemOuvidoriaInteracaoDepartamento(atendimento ,obj, true, usuario);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>AtendimentoInteracaoDepartamentoVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>AtendimentoInteracaoDepartamentoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AtendimentoVO atendimento , AtendimentoInteracaoDepartamentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
			//AtendimentoInteracaoDepartamento.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);
			final StringBuilder sql = new StringBuilder("UPDATE AtendimentoInteracaoDepartamento set ");
			sql.append(" atendimento= ?, questionamento= ?, resposta= ?,  dataRegistro= ?, funcionario= ?, dataRegistroResposta=?,  ");
			sql.append(" departamento= ?, atendimentoAtrasado=? , mensagemEnviada=?, usuariorespostaquestionamento=? ");
			sql.append("  WHERE ((codigo = ?))");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getAtendimentoVO().getCodigo());
					sqlAlterar.setString(2, obj.getQuestionamento());
					sqlAlterar.setString(3, obj.getResposta());
					sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					sqlAlterar.setInt(5, obj.getFuncionarioVO().getCodigo());
					sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataRegistroResposta()));
					sqlAlterar.setInt(7, obj.getDepartamento().getCodigo());
					sqlAlterar.setBoolean(8, obj.getAtendimentoAtrasado());
					sqlAlterar.setBoolean(9, true);
					if (Uteis.isAtributoPreenchido(obj.getUsuariorespostaquestionamento())) {
						sqlAlterar.setInt(10, obj.getUsuariorespostaquestionamento().getCodigo());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					sqlAlterar.setInt(11, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});			
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemOuvidoriaInteracaoDepartamento(atendimento ,obj, true, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#validarDados(negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO)
	 */
	@Override
	public void validarDados(AtendimentoInteracaoDepartamentoVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		
		if ((obj.getDepartamento()== null) || (obj.getDepartamento().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtendimentoInteracaoDepartamento_departamento"));
		}
		
		if ((obj.getFuncionarioVO()== null) || (obj.getFuncionarioVO().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AtendimentoInteracaoDepartamento_funcionario"));
		}
		
//		if(obj.getDataRegistro() != null  && ((obj.getQuestionamento() == null) || (Uteis.retiraTags(obj.getQuestionamento()).replaceAll("Untitled document", "").trim().isEmpty()))){
//			throw new Exception(UteisJSF.internacionalizar("msg_AtendimentoInteracaoDepartamento_questionamentoOuvidor"));
//		}
		
		if(obj.getDataRegistroResposta() != null  && ((obj.getResposta() == null) || (Uteis.retiraTags(obj.getResposta()).replaceAll("Untitled document", "").trim().isEmpty()) && !Uteis.isAtributoPreenchido(obj.getQuestionamento())) ){
			throw new Exception(UteisJSF.internacionalizar("msg_AtendimentoInteracaoDepartmanetto_Resposta"));
		}

		
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(AtendimentoInteracaoDepartamentoVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
	}

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#consultarPorCodigoOuvidoria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List consultarPorCodigoOuvidoria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = getSqlConsultaCompleta()+ "WHERE  atendimento = ? ORDER BY codigo";
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
	public static AtendimentoInteracaoDepartamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		AtendimentoInteracaoDepartamentoVO obj = new AtendimentoInteracaoDepartamentoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));		
		obj.getAtendimentoVO().setCodigo(dadosSQL.getInt("atendimento"));
		obj.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario"));
		obj.getDepartamento().setCodigo(dadosSQL.getInt("departamento"));
		obj.setQuestionamento(dadosSQL.getString("questionamento"));
		obj.setResposta(dadosSQL.getString("resposta"));
		obj.setDataRegistro(dadosSQL.getDate("dataRegistro"));
		obj.setDataRegistroResposta(dadosSQL.getDate("dataRegistroResposta"));
		obj.setDataRegistroRespostaTemp(dadosSQL.getDate("dataRegistroResposta"));
		obj.setAtendimentoAtrasado(dadosSQL.getBoolean("atendimentoAtrasado"));
		obj.setMensagemEnviada(dadosSQL.getBoolean("mensagemEnviada"));
		obj.getUsuarioquestionamento().setCodigo(dadosSQL.getInt("usuarioquestionamento"));
		obj.getUsuarioquestionamento().setNome(dadosSQL.getString("uq_nome"));
		obj.getUsuariorespostaquestionamento().setCodigo(dadosSQL.getInt("usuariorespostaquestionamento"));
		obj.getUsuariorespostaquestionamento().setNome(dadosSQL.getString("urq_nome"));
		obj.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}
	
	private StringBuilder getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT AtendimentoInteracaoDepartamento.*, uq.nome as uq_nome, urq.nome as urq_nome FROM AtendimentoInteracaoDepartamento");
		sql.append(" left join usuario as uq on uq.codigo = AtendimentoInteracaoDepartamento.usuarioquestionamento ");
		sql.append(" left join usuario as urq on urq.codigo = AtendimentoInteracaoDepartamento.usuariorespostaquestionamento ");
		return sql;
	}
	
	/**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>TitulacaoCursoVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosFuncionario(AtendimentoInteracaoDepartamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	
        if (obj.getFuncionarioVO().getCodigo().intValue() == 0) {
            obj.setFuncionarioVO(new FuncionarioVO());
            return;
        }
        obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getFuncionarioVO().getCodigo(), false, nivelMontarDados, usuario));
    }
    
    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>TitulacaoCursoVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDepartamento(AtendimentoInteracaoDepartamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	
        if (obj.getDepartamento().getCodigo().intValue() == 0) {
            obj.setDepartamento(new DepartamentoVO());
            return;
        }
        obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, nivelMontarDados, usuario));
    }

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.AtendimentoInteracaoDepartamentoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public AtendimentoInteracaoDepartamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = getSqlConsultaCompleta()+ "WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( AtendimentoInteracaoDepartamento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return AtendimentoInteracaoDepartamento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		AtendimentoInteracaoDepartamento.idEntidade = idEntidade;
	}
}
