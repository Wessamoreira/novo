package negocio.facade.jdbc.avaliacaoinst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPresencialRespostaVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalPresencialItemRespostaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see AvaliacaoInstitucionalPresencialRespostaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoInstitucionalPresencialItemResposta extends ControleAcesso implements AvaliacaoInstitucionalPresencialItemRespostaInterfaceFacade {

    protected static String idEntidade;

    public AvaliacaoInstitucionalPresencialItemResposta() throws Exception {
        super();
        setIdEntidade("AvaliacaoInstitucionalPresencialItemResposta");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     */
    public AvaliacaoInstitucionalPresencialRespostaVO novo() throws Exception {
        AvaliacaoInstitucionalPresencialItemResposta.incluir(getIdEntidade());
        AvaliacaoInstitucionalPresencialRespostaVO obj = new AvaliacaoInstitucionalPresencialRespostaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AvaliacaoInstitucionalPresencialRespostaVO obj, final RespostaPerguntaVO respostaPergunta) throws Exception {
        try {
//            AvaliacaoInstitucionalPresencialRespostaVO.validarDados(obj);
//            AvaliacaoInstitucionalPresencialItemResposta.incluir(getIdEntidade());
            final String sql = "INSERT INTO AvaliacaoInstitucionalPresencialItemResposta( avaliacaoInstitucionalPresencialResposta, questionario, pergunta, " // 1 - 3
                    + "respostaPergunta, qtdeRespostas, texto ) " // 4 - 5
                    + "VALUES ( ?, ?, ?, ?, ?, ?) returning codigo"; // 5
            respostaPergunta.setAvaliacaoInstitucionalPresencialItemResposta((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getCodigo());
                    sqlInserir.setInt(2, obj.getAvaliacaoInstitucional().getQuestionarioVO().getCodigo());
                    sqlInserir.setInt(3, respostaPergunta.getPergunta());
                    sqlInserir.setInt(4, respostaPergunta.getCodigo());
                    sqlInserir.setInt(5, respostaPergunta.getQtdeRespostas());
                    sqlInserir.setString(6, respostaPergunta.getTexto());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        respostaPergunta.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            respostaPergunta.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            respostaPergunta.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AvaliacaoInstitucionalPresencialRespostaVO obj, final RespostaPerguntaVO respostaPergunta) throws Exception {
        try {
//            AvaliacaoInstitucionalPresencialRespostaVO.validarDados(obj);
//            AvaliacaoInstitucionalPresencialItemResposta.alterar(getIdEntidade());
            final String sql = "UPDATE AvaliacaoInstitucionalPresencialItemResposta set avaliacaoInstitucionalPresencialResposta=?, questionario=?, pergunta=?, " // 1 - 3
                    + "respostaPergunta=?, qtdeRespostas=? " // 4 - 5
                    + "WHERE (codigo = ?)";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getCodigo());
                    sqlAlterar.setInt(2, obj.getAvaliacaoInstitucional().getQuestionarioVO().getCodigo());
                    sqlAlterar.setInt(3, respostaPergunta.getPergunta());
                    sqlAlterar.setInt(4, respostaPergunta.getCodigo());
                    sqlAlterar.setInt(5, respostaPergunta.getQtdeRespostas());
                    sqlAlterar.setInt(6, respostaPergunta.getAvaliacaoInstitucionalPresencialItemResposta());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>AvaliacaoInstitucionalPresencialRespostaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AvaliacaoInstitucionalPresencialRespostaVO obj, UsuarioVO usuario) throws Exception {
        try {
            AvaliacaoInstitucionalPresencialItemResposta.excluir(getIdEntidade());
            String sql = "DELETE FROM AvaliacaoInstitucionalPresencialItemResposta WHERE (codigo = ?)";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorAvaliacaoInstitucionalPresencialResposta(AvaliacaoInstitucionalPresencialRespostaVO obj) throws Exception {
        try {            
            String sql = "DELETE FROM AvaliacaoInstitucionalPresencialItemResposta WHERE (avaliacaoInstitucionalPresencialResposta = ?)";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return AvaliacaoInstitucionalPresencialItemResposta.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        AvaliacaoInstitucionalPresencialItemResposta.idEntidade = idEntidade;
    }
}
