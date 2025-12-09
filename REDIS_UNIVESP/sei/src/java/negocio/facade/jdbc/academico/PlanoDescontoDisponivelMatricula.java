package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.PlanoDescontoDisponivelMatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PlanoDescontoDisponivelMatriculaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>PlanoFinanceiroCursoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>PlanoFinanceiroCursoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PlanoFinanceiroCursoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PlanoDescontoDisponivelMatricula extends ControleAcesso implements PlanoDescontoDisponivelMatriculaInterfaceFacade {

    protected static String idEntidade;

    public PlanoDescontoDisponivelMatricula() throws Exception {
        super();
        setIdEntidade("PlanoFinanceiroCurso");
    }

    public PlanoDescontoDisponivelMatriculaVO novo() throws Exception {
        PlanoDescontoDisponivelMatricula.incluir(getIdEntidade());
        PlanoDescontoDisponivelMatriculaVO obj = new PlanoDescontoDisponivelMatriculaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PlanoFinanceiroCursoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoFinanceiroCursoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoDescontoDisponivelMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            PlanoDescontoDisponivelMatriculaVO.validarDados(obj);
            /**
             * @author Leonardo Riciolle 
             * Comentado 28/10/2014
             *  Classe Subordinada
             */
             // PlanoDescontoDisponivelMatricula.incluir(getIdEntidade());
            final String sql = "INSERT INTO PlanoDescontoDisponivelMatricula( planoDesconto, condicaoPagamentoPlanoFinanceiroCurso )"
                    + "VALUES ( ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getPlanoDesconto().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getPlanoDesconto().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setInt(2, obj.getCondicaoPagamentoPlanoFinanceiroCurso().intValue());
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
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PlanoFinanceiroCursoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoFinanceiroCursoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoDescontoDisponivelMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
            PlanoDescontoDisponivelMatriculaVO.validarDados(obj);
            /**
             * @author Leonardo Riciolle 
             * Comentado 28/10/2014
             *  Classe Subordinada
             */
            // PlanoDescontoDisponivelMatricula.alterar(getIdEntidade());
            final String sql = "UPDATE PlanoDescontoDisponivelMatricula set planoDesconto=?, condicaoPagamentoPlanoFinanceiroCurso=?  WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getPlanoDesconto().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getPlanoDesconto().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setInt(2, obj.getCondicaoPagamentoPlanoFinanceiroCurso().intValue());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PlanoFinanceiroCursoVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoFinanceiroCursoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PlanoDescontoDisponivelMatriculaVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
        	  * @author Leonardo Riciolle 
        	  * Comentado 28/10/2014
        	  *  Classe Subordinada
        	  */
            // PlanoDescontoDisponivelMatricula.excluir(getIdEntidade());
            String sql = "DELETE FROM PlanoDescontoDisponivelMatricula WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoFinanceiroCurso</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PlanoFinanceiroCursoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoDescontoDisponivelMatriculaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoDescontoDisponivelMatricula WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>PlanoFinanceiroCursoVO</code> resultantes da consulta.
     */
    public static List<PlanoDescontoDisponivelMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<PlanoDescontoDisponivelMatriculaVO> vetResultado = new ArrayList<PlanoDescontoDisponivelMatriculaVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    public static List<PlanoDescontoVO> montarDadosConsultaPlanoDesconto(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<PlanoDescontoVO> vetResultado = new ArrayList<PlanoDescontoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add((montarDados(tabelaResultado, nivelMontarDados, usuario)).getPlanoDesconto());
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>PlanoFinanceiroCursoVO</code>.
     *
     * @return O objeto da classe <code>PlanoFinanceiroCursoVO</code> com os dados devidamente montados.
     */
    public static PlanoDescontoDisponivelMatriculaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PlanoDescontoDisponivelMatriculaVO obj = new PlanoDescontoDisponivelMatriculaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getPlanoDesconto().setCodigo(new Integer(dadosSQL.getInt("planoDesconto")));
        obj.setCondicaoPagamentoPlanoFinanceiroCurso(dadosSQL.getInt("condicaoPagamentoPlanoFinanceiroCurso"));
         if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosPlanoDescontoPadrao(obj, nivelMontarDados, usuario);
        return obj;
    }

    public List<PlanoDescontoDisponivelMatriculaVO> consultarPorCodigoPlanoDescontoDisponivelMatricula(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM PlanoDescontoDisponivelMatricula WHERE condicaoPagamentoPlanoFinanceiroCurso = ");
        sqlStr.append(valorConsulta.intValue());
        sqlStr.append("  ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<PlanoDescontoDisponivelMatriculaVO> consultarPorUnidadeEnsinoDisponivelMatricula(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT PlanoDescontoDisponivelMatricula.* FROM PlanoDescontoDisponivelMatricula ");
        sqlStr.append("inner join condicaoPagamentoPlanoFinanceiroCurso on condicaoPagamentoPlanoFinanceiroCurso.codigo = PlanoDescontoDisponivelMatricula.condicaoPagamentoPlanoFinanceiroCurso ");
        sqlStr.append("inner join planofinanceirocurso on condicaoPagamentoPlanoFinanceiroCurso.planoFinanceiroCurso = planoFinanceiroCurso.codigo ");
        sqlStr.append("inner join turma on turma.planofinanceirocurso = planofinanceirocurso.codigo and turma.categoriacondicaopagamento = condicaoPagamentoPlanoFinanceiroCurso.categoria ");
        sqlStr.append("WHERE turma.unidadeensino =  ");
        sqlStr.append(valorConsulta.intValue()).append(" ");
        sqlStr.append("  ORDER BY PlanoDescontoDisponivelMatricula.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PlanoDescontoDisponivelMatriculaVO> consultarPorUnidadeEnsinoDisponivelMatricula_Turma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append("SELECT PlanoDescontoDisponivelMatricula.*, turma.codigo as codigoTurma FROM PlanoDescontoDisponivelMatricula ");
    	sqlStr.append("inner join condicaoPagamentoPlanoFinanceiroCurso on condicaoPagamentoPlanoFinanceiroCurso.codigo = PlanoDescontoDisponivelMatricula.condicaoPagamentoPlanoFinanceiroCurso ");
    	sqlStr.append("inner join planofinanceirocurso on condicaoPagamentoPlanoFinanceiroCurso.planoFinanceiroCurso = planoFinanceiroCurso.codigo ");
    	sqlStr.append("inner join turma on turma.planofinanceirocurso = planofinanceirocurso.codigo and turma.categoriacondicaopagamento = condicaoPagamentoPlanoFinanceiroCurso.categoria ");
    	sqlStr.append("WHERE turma.unidadeensino =  ");
    	sqlStr.append(valorConsulta.intValue()).append(" ");
    	sqlStr.append("  ORDER BY PlanoDescontoDisponivelMatricula.codigo");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	List<PlanoDescontoDisponivelMatriculaVO> vetResultado = new ArrayList<PlanoDescontoDisponivelMatriculaVO>(0);
        while (tabelaResultado.next()) {
            PlanoDescontoDisponivelMatriculaVO obj = new PlanoDescontoDisponivelMatriculaVO();
            obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
            obj.setCodigoTurma(new Integer(tabelaResultado.getInt("codigoTurma")));
            obj.getPlanoDesconto().setCodigo(new Integer(tabelaResultado.getInt("planoDesconto")));
            obj.setCondicaoPagamentoPlanoFinanceiroCurso(tabelaResultado.getInt("condicaoPagamentoPlanoFinanceiroCurso"));
            montarDadosPlanoDescontoPadrao(obj, nivelMontarDados, usuario);
        	vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<PlanoDescontoVO> consultarPlanoDescontoPorCodigoPlanoDescontoDisponivelMatricula( MatriculaPeriodoVO matriculaPeriodoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT PlanoDescontoDisponivelMatricula.* FROM PlanoDescontoDisponivelMatricula ");
        sqlStr.append(" INNER JOIN planodesconto ON PlanoDescontoDisponivelMatricula.planodesconto = planodesconto.codigo ");
        sqlStr.append(" WHERE PlanoDescontoDisponivelMatricula.condicaoPagamentoPlanoFinanceiroCurso = ").append(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo().intValue());
        if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino())) {
			sqlStr.append(" AND ( planodesconto.unidadeensino = ").append(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo().intValue());
			sqlStr.append(" OR planodesconto.unidadeensino IS NULL ) ");
		}
        sqlStr.append(" AND planodesconto.ativo ");
        sqlStr.append("ORDER BY PlanoDescontoDisponivelMatricula.codigo;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsultaPlanoDesconto(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PlanoDescontoVO</code> relacionado ao
     * objeto <code>PlanoFinanceiroCursoVO</code>. Faz uso da chave primária da classe <code>PlanoDescontoVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPlanoDescontoPadrao(PlanoDescontoDisponivelMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoDesconto().getCodigo().intValue() == 0) {
            obj.setPlanoDesconto(new PlanoDescontoVO());
            return;
        }
        obj.setPlanoDesconto(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(obj.getPlanoDesconto().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
    }


    /**
     * Operação responsável por localizar um objeto da classe <code>PlanoFinanceiroCursoVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PlanoDescontoDisponivelMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM PlanoDescontoDisponivelMatricula WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
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
        return PlanoDescontoDisponivelMatricula.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        PlanoDescontoDisponivelMatricula.idEntidade = idEntidade;
    }

}
