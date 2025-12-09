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

import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MotivoCancelamentoTrancamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MotivoCancelamentoTrancamentoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MotivoCancelamentoTrancamentoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see MotivoCancelamentoTrancamentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class MotivoCancelamentoTrancamento extends ControleAcesso implements MotivoCancelamentoTrancamentoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7694332583627482870L;
	protected static String idEntidade;

    public MotivoCancelamentoTrancamento() throws Exception {
        super();
        setIdEntidade("MotivoCancelamentoTrancamento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>MotivoCancelamentoTrancamentoVO</code>.
     */
    public MotivoCancelamentoTrancamentoVO novo() throws Exception {
        MotivoCancelamentoTrancamento.incluir(getIdEntidade());
        MotivoCancelamentoTrancamentoVO obj = new MotivoCancelamentoTrancamentoVO();
        return obj;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>MotivoCancelamentoTrancamentoVO</code>. Todos os tipos de consistência de dados são e devem
     * ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public void validarDados(MotivoCancelamentoTrancamentoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (MotivoCancelamentoTrancamento) deve ser informado.");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MotivoCancelamentoTrancamentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>MotivoCancelamentoTrancamentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MotivoCancelamentoTrancamentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            MotivoCancelamentoTrancamento.incluir(getIdEntidade(), true, usuarioVO);
            final String sql = "INSERT INTO MotivoCancelamentoTrancamento( nome, situacao, tipoJustificativa, apresentarRequerimentoVisaoAluno  ) VALUES ( ?, ?, ?, ? ) returning codigo";
            obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getSituacao());
                    sqlInserir.setString(3, obj.getTipoJustificativa());
                    sqlInserir.setBoolean(4, obj.getApresentarRequerimentoVisaoAluno());
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
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            obj.setCodigo(0);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MotivoCancelamentoTrancamentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>MotivoCancelamentoTrancamentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MotivoCancelamentoTrancamentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            MotivoCancelamentoTrancamento.alterar(getIdEntidade(), true, usuarioVO);
            final String sql = "UPDATE MotivoCancelamentoTrancamento set nome=?, situacao=?, tipoJustificativa=?, apresentarRequerimentoVisaoAluno = ? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getSituacao());
                    sqlAlterar.setString(3, obj.getTipoJustificativa());
                    sqlAlterar.setBoolean(4, obj.getApresentarRequerimentoVisaoAluno());
                    sqlAlterar.setInt(5, obj.getCodigo());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MotivoCancelamentoTrancamentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>MotivoCancelamentoTrancamentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MotivoCancelamentoTrancamentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            MotivoCancelamentoTrancamento.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM MotivoCancelamentoTrancamento WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>MotivoCancelamentoTrancamento</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MotivoCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<MotivoCancelamentoTrancamentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MotivoCancelamentoTrancamento WHERE lower (nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List<MotivoCancelamentoTrancamentoVO> consultarPorNome(String valorConsulta, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CursoMotivoCancelamentoTrancamento, MotivoCancelamentoTrancamento WHERE ((CursoMotivoCancelamentoTrancamento.MotivoCancelamentoTrancamento = MotivoCancelamentoTrancamento.codigo) and (CursoMotivoCancelamentoTrancamento.curso = " + codigoCurso.intValue() + ") and (lower (MotivoCancelamentoTrancamento.nome) like('" + valorConsulta.toLowerCase() + "%'))) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List<MotivoCancelamentoTrancamentoVO> consultarPorNomeAtivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MotivoCancelamentoTrancamento WHERE situacao = 'AT' ";
        if(usuario != null && usuario.getIsApresentarVisaoAlunoOuPais()) {
        	sqlStr += " and apresentarRequerimentoVisaoAluno = true ";
        }
        sqlStr += " ORDER BY nome"; 
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>MotivoCancelamentoTrancamento</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MotivoCancelamentoTrancamentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<MotivoCancelamentoTrancamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MotivoCancelamentoTrancamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MotivoCancelamentoTrancamentoVO</code> resultantes da consulta.
     */
    public static List<MotivoCancelamentoTrancamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<MotivoCancelamentoTrancamentoVO> vetResultado = new ArrayList<MotivoCancelamentoTrancamentoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MotivoCancelamentoTrancamentoVO</code>.
     * @return  O objeto da classe <code>MotivoCancelamentoTrancamentoVO</code> com os dados devidamente montados.
     */
    public static MotivoCancelamentoTrancamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ////System.out.println(">> Montar dados(MotivoCancelamentoTrancamento) - " + new Date());
        MotivoCancelamentoTrancamentoVO obj = new MotivoCancelamentoTrancamentoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setTipoJustificativa(dadosSQL.getString("tipoJustificativa"));
        obj.setApresentarRequerimentoVisaoAluno(dadosSQL.getBoolean("apresentarRequerimentoVisaoAluno"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MotivoCancelamentoTrancamentoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public MotivoCancelamentoTrancamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM MotivoCancelamentoTrancamento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public MotivoCancelamentoTrancamentoVO consultarPorTipoJustificativa(String tipoJustificativa) throws Exception {
        String sql = "SELECT * FROM MotivoCancelamentoTrancamento WHERE tipoJustificativa = ? order by codigo limit 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{tipoJustificativa});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados(Motivo Cancelamento/Trancamento).");
        }
        return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
    }
    
    @Override
    public List<MotivoCancelamentoTrancamentoVO> consultarPorTipoJustificativaSituacao(String tipoJustificativa, String situacao) throws Exception {
    	String sql = "SELECT * FROM MotivoCancelamentoTrancamento WHERE tipoJustificativa = ? and situacao = ? order by nome ";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{tipoJustificativa, situacao});    	
    	return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MotivoCancelamentoTrancamento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        MotivoCancelamentoTrancamento.idEntidade = idEntidade;
    }
    
}
