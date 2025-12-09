package negocio.facade.jdbc.processosel;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.QuestionarioAlunoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaAlunoVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.QuestionarioAlunoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>QuestionarioAlunoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>QuestionarioAlunoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see QuestionarioAlunoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class QuestionarioAluno extends ControleAcesso implements QuestionarioAlunoInterfaceFacade {

    protected static String idEntidade;

    public QuestionarioAluno() throws Exception {
        super();
        setIdEntidade("QuestionarioAluno");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>QuestionarioAlunoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>QuestionarioAlunoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final QuestionarioAlunoVO obj) throws Exception {
        QuestionarioAlunoVO.validarDados(obj);
        final String sql = "INSERT INTO QuestionarioAluno( peso, questionario, aluno) VALUES (  ?, ?, ?  ) returning codigo";
        try {
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getPeso());
                    if (obj.getQuestionario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getQuestionario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getAluno().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getAluno().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
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

            getFacadeFactory().getRespostaPerguntaAlunoFacade().incluirRespostaPerguntaAluno(obj);

        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>QuestionarioAlunoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>QuestionarioAlunoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final QuestionarioAlunoVO obj) throws Exception {
        QuestionarioAlunoVO.validarDados(obj);
        final String sql = "UPDATE QuestionarioAluno set peso=?, questionario=?, aluno=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getPeso());
                sqlAlterar.setInt(2, obj.getQuestionario().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getAluno().getCodigo().intValue());
                sqlAlterar.setInt(4, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>QuestionarioAlunoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>QuestionarioAlunoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(QuestionarioAlunoVO obj) throws Exception {
        QuestionarioAluno.excluir(getIdEntidade());
        String sql = "DELETE FROM QuestionarioAluno WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
    }

//    /**
//     * Operação responsável por excluir todos os objetos da <code>QuestionarioAlunoVO</code> no BD.
//     * Faz uso da operação <code>excluir</code> disponível na classe <code>QuestionarioAluno</code>.
//     * @param <code>pergunta</code> campo chave para exclusão dos objetos no BD.
//     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
//     */
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public void excluirQuestionarioAluno(Integer perguntaQuestionario) throws Exception {
//        RespostaPergunta.excluir(getIdEntidade());
//        String sql = "DELETE FROM QuestionarioAluno WHERE (perfilSocioEconomico = ?)";
//        getConexao().getJdbcTemplate().update(sql, perguntaQuestionario.intValue());
//    }
    /**
     * Operação responsável por alterar todos os objetos da <code>QuestionarioAlunoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirQuestionarioAluno</code> e <code>incluirQuestionarioAluno</code> disponíveis na classe <code>QuestionarioAlunoVO</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
//    public void alterarQuestionarioAluno(Integer perfilSocioEconomico, List objetos) throws Exception {
//        excluirQuestionarioAluno(perfilSocioEconomico);
//        incluirQuestionarioAluno(objetos);
//    }
//    /**
//     * Operação responsável por consultar todos os <code>QuestionarioAlunoVO</code> relacionados a um objeto da classe <code>processosel.PerfilSocioEconomico</code>.
//     * @param pergunta  Atributo de <code>processosel.PerfilSocioEconomico</code> a ser utilizado para localizar os objetos da classe <code>QuestionarioAlunoVO</code>.
//     * @return List  Contendo todos os objetos da classe <code>QuestionarioAlunoVO</code> resultantes da consulta.
//     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
//     */
//    public List consultarQuestionarioAluno(Integer perguntaQuestionario, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        RespostaPergunta.consultar(getIdEntidade());
//        List objetos = new ArrayList();
//        String sql = "SELECT * FROM QuestionarioAluno WHERE perguntaQuestionario = ?";
//        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, perguntaQuestionario.intValue());
//        while (resultado.next()) {
//            QuestionarioAlunoVO novoObj = new QuestionarioAlunoVO();
//            novoObj = QuestionarioAluno.montarDados(resultado, nivelMontarDados, usuario);
//            objetos.add(novoObj);
//        }
//        return objetos;
//    }
//    public List consultarQuestionarioAluno(Integer perguntaQuestionario, Integer perfilSocioEconomico, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        RespostaPergunta.consultar(getIdEntidade());
//        List objetos = new ArrayList();
//        String sql = "SELECT * FROM QuestionarioAluno WHERE perguntaQuestionario = ? AND perfilSocioEconomico  = ?";
//        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, perguntaQuestionario.intValue(), perfilSocioEconomico.intValue());
//        while (resultado.next()) {
//            QuestionarioAlunoVO novoObj = new QuestionarioAlunoVO();
//            novoObj = QuestionarioAluno.montarDados(resultado, nivelMontarDados, usuario);
//            objetos.add(novoObj);
//        }
//        return objetos;
//    }
//    /**
//     * Responsável por realizar uma consulta de <code>QuestionarioAluno</code> através do valor do atributo
//     * <code>String tipoResposta</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
//     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
//     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
//     * @return  List Contendo vários objetos da classe <code>QuestionarioAlunoVO</code> resultantes da consulta.
//     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
//     */
//    public List consultarPorTipoResposta(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
//        String sqlStr = "SELECT * FROM QuestionarioAluno WHERE upper( tipoResposta ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoResposta";
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
//    }
//    /**
//     * Responsável por realizar uma consulta de <code>QuestionarioAluno</code> através do valor do atributo
//     * <code>String texto</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
//     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
//     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
//     * @return  List Contendo vários objetos da classe <code>QuestionarioAlunoVO</code> resultantes da consulta.
//     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
//     */
//    public List consultarPorTexto(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
//        String sqlStr = "SELECT * FROM QuestionarioAluno WHERE upper( texto ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY texto";
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
//    }
//    /**
//     * Responsável por realizar uma consulta de <code>QuestionarioAluno</code> através do valor do atributo
//     * <code>Integer respostaQuestionarioAluno</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
//     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
//     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
//     * @return  List Contendo vários objetos da classe <code>QuestionarioAlunoVO</code> resultantes da consulta.
//     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
//     */
//    public List consultarPorRespostaQuestionarioAluno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
//        String sqlStr = "SELECT * FROM QuestionarioAluno WHERE respostaQuestionarioAluno >= " + valorConsulta.intValue() + " ORDER BY respostaQuestionarioAluno";
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
//    }
    /**
     * Responsável por realizar uma consulta de <code>QuestionarioAluno</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>QuestionarioAlunoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void montarRespostasQuestionarioAluno(QuestionarioVO obj, QuestionarioAlunoVO questionarioAluno, PessoaVO aluno) throws Exception {

        try {

            if (!questionarioAluno.getNovoObj()){
                getFacadeFactory().getRespostaPerguntaAlunoFacade().excluirRespostaPerguntaAluno(questionarioAluno);
                questionarioAluno.setListaRespostaPerguntaAlunoVO(new ArrayList<RespostaPerguntaAlunoVO>(0));
            }

            for (PerguntaQuestionarioVO perguntaQuestionario : obj.getPerguntaQuestionarioVOs()) {
                if (perguntaQuestionario.getPergunta().getTipoResposta().equals("TE")) {
                    RespostaPerguntaAlunoVO respostaPerguntaAlunoVO = new RespostaPerguntaAlunoVO();
                    respostaPerguntaAlunoVO.setPerguntaQuestionario(perguntaQuestionario.getPergunta());
                    respostaPerguntaAlunoVO.setTipoResposta(perguntaQuestionario.getPergunta().getTipoResposta());
                    respostaPerguntaAlunoVO.setTexto(perguntaQuestionario.getPergunta().getTexto());
                    questionarioAluno.getListaRespostaPerguntaAlunoVO().add(respostaPerguntaAlunoVO);
                } else {
                    for (RespostaPerguntaVO resposta : perguntaQuestionario.getPergunta().getRespostaPerguntaVOs()) {
                        if (resposta.getSelecionado()) {
                            RespostaPerguntaAlunoVO respostaPerguntaAlunoVO = new RespostaPerguntaAlunoVO();
                            respostaPerguntaAlunoVO.setPerguntaQuestionario(perguntaQuestionario.getPergunta());
                            respostaPerguntaAlunoVO.setTipoResposta(perguntaQuestionario.getPergunta().getTipoResposta());
                            respostaPerguntaAlunoVO.setRespostaQuestionarioAluno(resposta);
                            questionarioAluno.getListaRespostaPerguntaAlunoVO().add(respostaPerguntaAlunoVO);
                        }
                    }
                }
            }

            if (questionarioAluno.getNovoObj()){
                questionarioAluno.setAluno(aluno);
                questionarioAluno.setQuestionario(obj);
                incluir(questionarioAluno);
            }else {
                getFacadeFactory().getRespostaPerguntaAlunoFacade().incluirRespostaPerguntaAluno(questionarioAluno);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM QuestionarioAluno WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
    }

    public QuestionarioAlunoVO consultarPorQuestionarioAluno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM QuestionarioAluno WHERE questionario = " + valorConsulta.intValue() + " and aluno = " + usuarioLogado.getPessoa().getCodigo().intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
        }
        return null;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>QuestionarioAlunoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public QuestionarioAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        String sql = "SELECT * FROM QuestionarioAluno WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (tabelaResultado.next()) {
            return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
        }
        return null;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>QuestionarioAlunoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            QuestionarioAlunoVO obj = new QuestionarioAlunoVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

//    public void montarListaRespostaQuestionarioAlunoBancoCurriculum(QuestionarioVO questionario, UsuarioVO usuarioVO) throws Exception {
//
//        try {
//            Iterator i = questionario.getPerguntaQuestionarioVOs().iterator();
//            while (i.hasNext()) {
//
//                PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) i.next();
//                Iterator iaux = obj.getPergunta().getRespostaPerguntaVOs().iterator();
//
//                while (iaux.hasNext()) {
//
//                    RespostaPerguntaVO resposta = (RespostaPerguntaVO) iaux.next();
//
//                    if (resposta.getSelecionado() || !obj.getPergunta().getTexto().equals("")) {
//                        QuestionarioAlunoVO questionarioAluno = new QuestionarioAlunoVO();
//                        questionarioAluno.setAluno(usuarioVO.getPessoa());
//                        questionarioAluno.setQuestionario(questionario);
//                        questionarioAluno.setPerguntaQuestionario(obj.getPergunta());
//                        questionarioAluno.setTipoResposta(obj.getPergunta().getTipoResposta());
//                        if (obj.getPergunta().getTipoRespostaTextual()) {
//                            questionarioAluno.setTexto(obj.getPergunta().getTexto());
//                        } else {
//                            questionarioAluno.setRespostaQuestionarioAluno(resposta);
//                        }
//
//                        incluir(questionarioAluno);
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//    }
    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>QuestionarioAlunoVO</code>.
     * @return  O objeto da classe <code>QuestionarioAlunoVO</code> com os dados devidamente montados.
     */
    public static QuestionarioAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        QuestionarioAlunoVO obj = new QuestionarioAlunoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setPeso(dadosSQL.getInt("peso"));
        obj.getQuestionario().setCodigo(dadosSQL.getInt("questionario"));
        obj.getAluno().setCodigo(dadosSQL.getInt("aluno"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

//        montarDadosPerguntaQuestionario(obj, nivelMontarDados, usuario);
//        montarDadosRespostaQuestionario(obj, nivelMontarDados, usuario);
        montarDadosQuestionario(obj, nivelMontarDados, usuario);
        montarDadosAluno(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            montarDadosRespostaPerguntaAluno(obj, nivelMontarDados, usuario);
        }
        return obj;
    }

//    /**
//     * Operação responsável por montar os dados de um objeto da classe <code>PerguntaVO</code> relacionado ao objeto <code>QuestionarioAlunoVO</code>.
//     * Faz uso da chave primária da classe <code>PerguntaVO</code> para realizar a consulta.
//     * @param obj  Objeto no qual será montado os dados consultados.
//     */
//    public static void montarDadosPerguntaQuestionario(QuestionarioAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        if (obj.getPerguntaQuestionario().getCodigo().intValue() == 0) {
//            obj.setPerguntaQuestionario(new PerguntaVO());
//            return;
//        }
//        obj.setPerguntaQuestionario(getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(obj.getPerguntaQuestionario().getCodigo(), nivelMontarDados, usuario));
//    }
//
//    public static void montarDadosRespostaQuestionario(QuestionarioAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        if (obj.getRespostaQuestionarioAluno().getCodigo().intValue() == 0) {
//            obj.setRespostaQuestionarioAluno(new RespostaPerguntaVO());
//            return;
//        }
//        obj.setRespostaQuestionarioAluno(getFacadeFactory().getRespostaPerguntaFacade().consultarPorChavePrimaria(obj.getRespostaQuestionarioAluno().getCodigo(), nivelMontarDados, usuario));
//    }
    public static void montarDadosQuestionario(QuestionarioAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getQuestionario().getCodigo().intValue() == 0) {
            obj.setQuestionario(new QuestionarioVO());
            return;
        }
        obj.setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getQuestionario().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosAluno(QuestionarioAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAluno().getCodigo().intValue() == 0) {
            obj.setAluno(new PessoaVO());
            return;
        }
        obj.setAluno(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getAluno().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosRespostaPerguntaAluno(QuestionarioAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        obj.setListaRespostaPerguntaAlunoVO(getFacadeFactory().getRespostaPerguntaAlunoFacade().consultarPorQuestionarioAluno(obj.getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return QuestionarioAluno.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        QuestionarioAluno.idEntidade = idEntidade;
    }
}
