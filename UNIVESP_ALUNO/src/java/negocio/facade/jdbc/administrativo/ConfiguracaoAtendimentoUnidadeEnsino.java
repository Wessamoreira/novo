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

import negocio.comuns.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade;

/**
 * 
 * @author Pedro
 */

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoAtendimentoUnidadeEnsino extends ControleAcesso implements ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade {
	
	protected static String idEntidade;

	public ConfiguracaoAtendimentoUnidadeEnsino() throws Exception {
		super();
		setIdEntidade("ConfiguracaoAtendimento");

	}
	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade#excluirConfiguracaoAtendimentoUnidadeEnsinoVO(java.lang.Integer, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirConfiguracaoAtendimentoUnidadeEnsinoVO(Integer configuracaoAtendimento, UsuarioVO usuarioLogado) throws Exception {
		/**
         * @author Leonardo Riciolle
         * Comentado 23/10/2014
         */
		//ItemTitulacaoCurso.excluir(getIdEntidade());
        String sql = "DELETE FROM ConfiguracaoAtendimentoUnidadeEnsino WHERE (configuracaoAtendimento = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{configuracaoAtendimento});
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade#alterarConfiguracaoAtendimentoUnidadeEnsinoVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarConfiguracaoAtendimentoUnidadeEnsinoVO(Integer configuracaoAtendimento, List objetos,  UsuarioVO usuarioLogado) throws Exception {
        String str = "DELETE FROM ConfiguracaoAtendimentoUnidadeEnsino WHERE configuracaoAtendimento = " + configuracaoAtendimento;
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
        	ConfiguracaoAtendimentoUnidadeEnsinoVO objeto = (ConfiguracaoAtendimentoUnidadeEnsinoVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
        	ConfiguracaoAtendimentoUnidadeEnsinoVO objeto = (ConfiguracaoAtendimentoUnidadeEnsinoVO) e.next();
            if (objeto.getCodigo().equals(0)) {
            	objeto.getConfiguracaoAtendimentoVO().setCodigo(configuracaoAtendimento);
                incluir(objeto, usuarioLogado);
            } else {
                alterar(objeto, usuarioLogado);
            }
        }
    }

    /* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade#incluirConfiguracaoAtendimentoUnidadeEnsinoVO(java.lang.Integer, java.util.List, negocio.comuns.arquitetura.UsuarioVO)
	 */
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirConfiguracaoAtendimentoUnidadeEnsinoVO(Integer configuracaoAtendimento, List<ConfiguracaoAtendimentoUnidadeEnsinoVO> objetos,  UsuarioVO usuarioLogado) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
        	ConfiguracaoAtendimentoUnidadeEnsinoVO obj = (ConfiguracaoAtendimentoUnidadeEnsinoVO) e.next();
            obj.getConfiguracaoAtendimentoVO().setCodigo(configuracaoAtendimento);
            incluir(obj, usuarioLogado);
        }
    }

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ConfiguracaoAtendimentoUnidadeEnsinoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoAtendimentoUnidadeEnsinoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoAtendimentoUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			/**
             * @author Leonardo Riciolle
             * Comentado 23/10/2014
             */
			//ConfiguracaoAtendimentoUnidadeEnsino.incluir(getIdEntidade());
			realizarUpperCaseDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO ConfiguracaoAtendimentoUnidadeEnsino( configuracaoAtendimento, unidadeEnsino ) ");						
			sql.append(" VALUES ( ?, ? ) returning codigo");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(1, obj.getConfiguracaoAtendimentoVO().getCodigo());					
					sqlInserir.setInt(2, obj.getUnidadeEnsinoVO().getCodigo());										
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
	 * <code>ConfiguracaoAtendimentoUnidadeEnsinoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoAtendimentoUnidadeEnsinoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoAtendimentoUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			/**
             * @author Leonardo Riciolle
             * Comentado 23/10/2014
             */
			//ConfiguracaoAtendimentoUnidadeEnsino.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);			
			final StringBuilder sql = new StringBuilder("UPDATE ConfiguracaoAtendimentoUnidadeEnsino set ");
			sql.append(" configuracaoAtendimento=?, unidadeEnsino=?  ");						
			sql.append("  WHERE ((codigo = ?))");
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getConfiguracaoAtendimentoVO().getCodigo());					
					sqlAlterar.setInt(2, obj.getUnidadeEnsinoVO().getCodigo());		
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}	
	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade#validarDados(negocio.comuns.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoVO)
	 */
	@Override
	public void validarDados(ConfiguracaoAtendimentoUnidadeEnsinoVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}

		if ((obj.getUnidadeEnsinoVO() == null) || (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoAtendimentoFuncionario_unidadeEnsino"));
		}
	}

	

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(ConfiguracaoAtendimentoUnidadeEnsinoVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {			
			return;
		}
	}

	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade#consultarPorCodigoConfiguracaoAtendimento(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
    @Override
	public List consultarPorCodigoConfiguracaoAtendimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {        
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ConfiguracaoAtendimentoUnidadeEnsino WHERE  configuracaoAtendimento = ? ORDER BY codigo";
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
	public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
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
	public  ConfiguracaoAtendimentoUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoAtendimentoUnidadeEnsinoVO obj = new ConfiguracaoAtendimentoUnidadeEnsinoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getConfiguracaoAtendimentoVO().setCodigo(dadosSQL.getInt("configuracaoAtendimento"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}
	
	/**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>TitulacaoCursoVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosUnidadeEnsino(ConfiguracaoAtendimentoUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	
        if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuario));
    }

	
	/* (non-Javadoc)
	 * @see negocio.facade.jdbc.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public ConfiguracaoAtendimentoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ConfiguracaoAtendimentoUnidadeEnsino WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ConfiguracaoAtendimentoUnidadeEnsino ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoAtendimentoUnidadeEnsino.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoAtendimentoUnidadeEnsino.idEntidade = idEntidade;
	}

}
