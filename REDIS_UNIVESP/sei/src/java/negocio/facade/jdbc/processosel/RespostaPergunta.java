package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.RespostaPerguntaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RespostaPerguntaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>RespostaPerguntaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see RespostaPerguntaVO
 * @see ControleAcesso
 * @see Pergunta
 */
@Repository
@Scope("singleton")
@Lazy 
public class RespostaPergunta extends ControleAcesso implements RespostaPerguntaInterfaceFacade {

    protected static String idEntidade;

    public RespostaPergunta() throws Exception {
        super();
        setIdEntidade("Pergunta");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>RespostaPerguntaVO</code>.
     */
    public RespostaPerguntaVO novo() throws Exception {
        RespostaPergunta.incluir(getIdEntidade());
        RespostaPerguntaVO obj = new RespostaPerguntaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RespostaPerguntaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>RespostaPerguntaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RespostaPerguntaVO obj, UsuarioVO usuario) throws Exception {
        RespostaPerguntaVO.validarDados(obj);
        final String sql = "INSERT INTO RespostaPergunta( descricao, pergunta, texto, tipoPergunta, apresentarRespostaAdicional, ordem, dataResposta, agrupador, nota ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                sqlInserir.setString(1, obj.getDescricao());
                if (obj.getPergunta().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getPergunta().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setString(3, obj.getTexto());
                sqlInserir.setString(4, obj.getTipoPergunta());
                sqlInserir.setBoolean(5, obj.getApresentarRespostaAdicional());
                sqlInserir.setInt(6, obj.getOrdem());
                sqlInserir.setDate(7, Uteis.getDataJDBC(new Date()));
                sqlInserir.setInt(8, obj.getAgrupador());
                sqlInserir.setDouble(9, obj.getNota());
                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return rs.getInt("codigo");
                }
                return null;
            }
        }));
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RespostaPerguntaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RespostaPerguntaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RespostaPerguntaVO obj,UsuarioVO usuario) throws Exception {
        RespostaPerguntaVO.validarDados(obj);
        final String sql = "UPDATE RespostaPergunta set descricao=?, pergunta=?, texto=?, tipoPergunta=?, apresentarRespostaAdicional=?, ordem=?, dataResposta=?, agrupador=?, nota = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getDescricao());
                if (obj.getPergunta().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getPergunta().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setString(3, obj.getTexto());
                sqlAlterar.setString(4, obj.getTipoPergunta());
                sqlAlterar.setBoolean(5, obj.getApresentarRespostaAdicional());
                sqlAlterar.setInt(6, obj.getOrdem());
                sqlAlterar.setDate(7, Uteis.getDataJDBC(new Date()));
                sqlAlterar.setInt(8, obj.getAgrupador());
                sqlAlterar.setDouble(9, obj.getNota());
                sqlAlterar.setInt(10, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RespostaPerguntaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>RespostaPerguntaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RespostaPerguntaVO obj,UsuarioVO usuario) throws Exception {
        RespostaPergunta.excluir(getIdEntidade());
        String sql = "DELETE FROM RespostaPergunta WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
    }

    /**
     * Responsável por realizar uma consulta de <code>RespostaPergunta</code> através do valor do atributo 
     * <code>String texto</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RespostaPerguntaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTexto(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RespostaPergunta WHERE upper( texto ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY texto";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>RespostaPergunta</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RespostaPerguntaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RespostaPergunta WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>RespostaPergunta</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RespostaPerguntaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RespostaPergunta WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>RespostaPerguntaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>RespostaPerguntaVO</code>.
     * @return  O objeto da classe <code>RespostaPerguntaVO</code> com os dados devidamente montados.
     */
    public static RespostaPerguntaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        RespostaPerguntaVO obj = new RespostaPerguntaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setPergunta(new Integer(dadosSQL.getInt("pergunta")));
        obj.setOrdem(new Integer(dadosSQL.getInt("ordem")));
        obj.setTexto(dadosSQL.getString("texto"));
        obj.setTipoPergunta(dadosSQL.getString("tipoPergunta"));
        obj.setApresentarRespostaAdicional(dadosSQL.getBoolean("apresentarRespostaAdicional"));
        obj.setAgrupador(dadosSQL.getInt("agrupador"));
        obj.setNota(dadosSQL.getDouble("nota"));
        obj.setNovoObj(Boolean.FALSE);
        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>RespostaPerguntaVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>RespostaPergunta</code>.
     * @param <code>pergunta</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirRespostaPerguntas(Integer pergunta, UsuarioVO usuario) throws Exception {
        RespostaPergunta.excluir(getIdEntidade());
        String sql = "DELETE FROM RespostaPergunta WHERE (pergunta = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, pergunta.intValue());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirRespostaPerguntas(Integer pergunta, List objetos, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM RespostaPergunta WHERE (pergunta = ?)";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            RespostaPerguntaVO obj = (RespostaPerguntaVO) i.next();
            if (obj.getCodigo().intValue() != 0) {
                sql += " and codigo != " + obj.getCodigo().intValue();
            }
        }
        getConexao().getJdbcTemplate().update(sql+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), pergunta.intValue());
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>RespostaPerguntaVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirRespostaPerguntas</code> e <code>incluirRespostaPerguntas</code> disponíveis na classe <code>RespostaPergunta</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarRespostaPerguntas(Integer pergunta, String tipoPergunta, List objetos, UsuarioVO usuario) throws Exception {
        excluirRespostaPerguntas(pergunta, objetos, usuario);
        incluirRespostaPerguntas(pergunta, tipoPergunta, objetos, usuario);
    }

    /**
     * Operação responsável por incluir objetos da <code>RespostaPerguntaVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>processosel.Pergunta</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirRespostaPerguntas(Integer perguntaPrm, String tipoPergunta, List objetos,UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            RespostaPerguntaVO obj = (RespostaPerguntaVO) e.next();
            obj.setPergunta(perguntaPrm);
            obj.setTipoPergunta(tipoPergunta);
            if (obj.getCodigo().intValue() == 0) {
                incluir(obj,usuario);
            } else {
                alterar(obj, usuario);
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>RespostaPerguntaVO</code> relacionados a um objeto da classe <code>processosel.Pergunta</code>.
     * @param pergunta  Atributo de <code>processosel.Pergunta</code> a ser utilizado para localizar os objetos da classe <code>RespostaPerguntaVO</code>.
     * @return List  Contendo todos os objetos da classe <code>RespostaPerguntaVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List<RespostaPerguntaVO> consultarRespostaPerguntas(Integer pergunta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        RespostaPergunta.consultar(getIdEntidade());
        List<RespostaPerguntaVO> objetos = new ArrayList<RespostaPerguntaVO>();
        String sql = "SELECT * FROM RespostaPergunta WHERE pergunta = ? order by ordem";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, pergunta.intValue());
        while (resultado.next()) {
            RespostaPerguntaVO novoObj = new RespostaPerguntaVO();
            novoObj = RespostaPergunta.montarDados(resultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RespostaPerguntaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RespostaPerguntaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RespostaPergunta WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RespostaPergunta ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RespostaPergunta.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        RespostaPergunta.idEntidade = idEntidade;
    }
}
