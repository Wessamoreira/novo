package negocio.facade.jdbc.academico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.MatriculaControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MatriculaControleLivroRegistroDiplomaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MatriculaControleLivroRegistroDiplomaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see MatriculaControleLivroRegistroDiplomaVO
 * @see SuperEntidade
*/
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MatriculaControleLivroRegistroDiploma extends ControleAcesso  {
	
    protected static String idEntidade;
	
    public MatriculaControleLivroRegistroDiploma() throws Exception {
        super();
        setIdEntidade("MatriculaControleLivroRegistroDiploma");
    }
	
    /**
     * Operação responsável por retornar um novo objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code>.
    */
    public MatriculaControleLivroRegistroDiplomaVO novo() throws Exception {
        MatriculaControleLivroRegistroDiploma.incluir(getIdEntidade());
        MatriculaControleLivroRegistroDiplomaVO obj = new MatriculaControleLivroRegistroDiplomaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MatriculaControleLivroRegistroDiplomaVO obj) throws Exception {
        try {
            MatriculaControleLivroRegistroDiplomaVO.validarDados(obj);
            MatriculaControleLivroRegistroDiploma.incluir(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO MatriculaControleLivroRegistroDiploma( matricula, dataEntregaRecibo, responsavelEntregaRecibo, utilizouProcuracao, nomePessoaProcuracao, cpfPessoaProcuracao, controleLivroFolhaRecibo, certificadorecebido ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    if (!obj.getMatricula().getMatricula().equals("")) {
                        sqlInserir.setString(1, obj.getMatricula().getMatricula() );
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setDate( 2, Uteis.getDataJDBC(obj.getDataEntregaRecibo() ));
                    sqlInserir.setInt( 3, obj.getResponsavelEntregaRecibo().intValue() );
                    sqlInserir.setBoolean( 4, obj.isUtilizouProcuracao().booleanValue() );
                    sqlInserir.setString( 5, obj.getNomePessoaProcuracao() );
                    sqlInserir.setString( 6, obj.getCpfPessoaProcuracao() );
                    if (obj.getControleLivroFolhaRecibo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getControleLivroFolhaRecibo().getCodigo().intValue() );
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    sqlInserir.setBoolean( 8, obj.isCertificadoRecebido().booleanValue() );
                    
                    return sqlInserir;
                }
            });            
              
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MatriculaControleLivroRegistroDiplomaVO obj) throws Exception {
        try {
            MatriculaControleLivroRegistroDiplomaVO.validarDados(obj);
            MatriculaControleLivroRegistroDiploma.alterar(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE MatriculaControleLivroRegistroDiploma set dataEntregaRecibo=?, responsavelEntregaRecibo=?, utilizouProcuracao=?, nomePessoaProcuracao=?, cpfPessoaProcuracao=?, controleLivroFolhaRecibo=?, certificadorecebido=? WHERE ((matricula = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setDate( 1, Uteis.getDataJDBC(obj.getDataEntregaRecibo() ));
                    sqlAlterar.setInt( 2, obj.getResponsavelEntregaRecibo().intValue() );
                    sqlAlterar.setBoolean( 3, obj.isUtilizouProcuracao().booleanValue() );
                    sqlAlterar.setString( 4, obj.getNomePessoaProcuracao() );
                    sqlAlterar.setString( 5, obj.getCpfPessoaProcuracao() );
                    if (obj.getControleLivroFolhaRecibo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getControleLivroFolhaRecibo().getCodigo().intValue() );
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    if (!obj.getMatricula().getMatricula().equals("")) {
                        sqlAlterar.setString(7, obj.getMatricula().getMatricula() );
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    sqlAlterar.setBoolean( 8, obj.isCertificadoRecebido().booleanValue() );

                    return sqlAlterar;
                }
            });
            
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)    
    public void excluir(final MatriculaControleLivroRegistroDiplomaVO obj) throws Exception {
        try {
            MatriculaControleLivroRegistroDiploma.excluir(getIdEntidade());
            final String sql = "DELETE FROM MatriculaControleLivroRegistroDiploma WHERE ((matricula = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlExcluir = con.prepareStatement(sql);
                    if (!obj.getMatricula().getMatricula().equals("")) {
                        sqlExcluir.setString(1, obj.getMatricula().getMatricula() );
                    } else {
                        sqlExcluir.setNull(1, 0);
                    }
                    return sqlExcluir;
                }
            });
            
        } catch (Exception e) {
            throw e;
        }
    }

    public void gravarMatriculaRegistroLivro(List<MatriculaControleLivroRegistroDiplomaVO> matriculaControleLivroRegistroDiploma, UsuarioVO usuario) throws Exception {
		Iterator i = matriculaControleLivroRegistroDiploma.iterator();
		while (i.hasNext()) {
			MatriculaControleLivroRegistroDiplomaVO obj = (MatriculaControleLivroRegistroDiplomaVO) i.next();
//			alterar(obj.getControleLivroFolhaRecibo(), usuario);
			excluir(obj);
			incluir(obj);
		}
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaControleLivroRegistroDiploma</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>ControleLivroFolhaRecibo</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MatriculaControleLivroRegistroDiplomaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorCodigoControleLivroFolhaRecibo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MatriculaControleLivroRegistroDiploma.* FROM MatriculaControleLivroRegistroDiploma, ControleLivroFolhaRecibo WHERE MatriculaControleLivroRegistroDiploma.controleLivroFolhaRecibo = ControleLivroFolhaRecibo.codigo and ControleLivroFolhaRecibo.codigo >= " + valorConsulta.intValue() + " ORDER BY ControleLivroFolhaRecibo.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaControleLivroRegistroDiploma</code> através do valor do atributo 
     * <code>Date dataEntregaRecibo</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MatriculaControleLivroRegistroDiplomaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
    public List consultarPorDataEntregaRecibo(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MatriculaControleLivroRegistroDiploma WHERE ((dataEntregaRecibo >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataEntregaRecibo <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataEntregaRecibo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MatriculaControleLivroRegistroDiploma</code> através do valor do atributo 
     * <code>matricula</code> da classe <code>Matricula</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MatriculaControleLivroRegistroDiplomaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public MatriculaControleLivroRegistroDiplomaVO consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MatriculaControleLivroRegistroDiploma.* FROM MatriculaControleLivroRegistroDiploma, Matricula WHERE MatriculaControleLivroRegistroDiploma.matricula = Matricula.matricula and upper( Matricula.matricula ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()){
	        return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return null;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MatriculaControleLivroRegistroDiplomaVO</code> resultantes da consulta.
    */
    public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code>.
     * @return  O objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code> com os dados devidamente montados.
    */
    public  MatriculaControleLivroRegistroDiplomaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        MatriculaControleLivroRegistroDiplomaVO obj = new MatriculaControleLivroRegistroDiplomaVO();
        obj.getMatricula().setMatricula( dadosSQL.getString("matricula"));
        obj.setDataEntregaRecibo( dadosSQL.getDate("dataEntregaRecibo"));
        obj.setCertificadoRecebido( dadosSQL.getBoolean("certificadorecebido"));        
        obj.setResponsavelEntregaRecibo( new Integer( dadosSQL.getInt("responsavelEntregaRecibo")));
        obj.setUtilizouProcuracao( new Boolean(dadosSQL.getBoolean("utilizouProcuracao")));
        obj.setNomePessoaProcuracao( dadosSQL.getString("nomePessoaProcuracao"));
        obj.setCpfPessoaProcuracao( dadosSQL.getString("cpfPessoaProcuracao"));
        obj.getControleLivroFolhaRecibo().setCodigo( new Integer( dadosSQL.getInt("controleLivroFolhaRecibo")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosControleLivroFolhaRecibo(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosMatricula(obj, nivelMontarDados, usuario);

        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ControleLivroFolhaReciboVO</code> relacionado ao objeto <code>MatriculaControleLivroRegistroDiplomaVO</code>.
     * Faz uso da chave primária da classe <code>ControleLivroFolhaReciboVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public  void montarDadosControleLivroFolhaRecibo(MatriculaControleLivroRegistroDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getControleLivroFolhaRecibo().getCodigo().intValue() == 0) {
            obj.setControleLivroFolhaRecibo(new ControleLivroFolhaReciboVO());
            return;
        }
//        obj.setControleLivroFolhaRecibo(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorChavePrimaria(obj.getControleLivroFolhaRecibo().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto <code>MatriculaControleLivroRegistroDiplomaVO</code>.
     * Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public  void montarDadosMatricula(MatriculaControleLivroRegistroDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
            obj.setMatricula(new MatriculaVO());
            return;
        }
        obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), obj.getMatricula().getUnidadeEnsino().getCodigo(), NivelMontarDados.getEnum(nivelMontarDados), usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MatriculaControleLivroRegistroDiplomaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public MatriculaControleLivroRegistroDiplomaVO consultarPorChavePrimaria( String matriculaPrm, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM MatriculaControleLivroRegistroDiploma WHERE matricula = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{matriculaPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( MatriculaControleLivroRegistroDiploma ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
	

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return MatriculaControleLivroRegistroDiploma.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        MatriculaControleLivroRegistroDiploma.idEntidade = idEntidade;
    }
    
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorControleLivroFolhaRecibo(final Integer controleLivroFolhaRecibo) throws Exception {
		try {
			MatriculaControleLivroRegistroDiploma.excluir(getIdEntidade());
			final String sql = "DELETE FROM MatriculaControleLivroRegistroDiploma WHERE ((controleLivroFolhaRecibo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlExcluir = con.prepareStatement(sql);
					sqlExcluir.setInt(1, controleLivroFolhaRecibo);
					return sqlExcluir;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
    
}