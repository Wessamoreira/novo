package negocio.facade.jdbc.crm;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CursoEtapaWorkflowVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.Curso;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.CursoEtapaWorkflowInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CursoEtapaWorkflowVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>CursoEtapaWorkflowVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see CursoEtapaWorkflowVO
 * @see ControleAcesso
 * @see Workflow
 */
@Repository
@Scope("singleton")
@Lazy
public class CursoEtapaWorkflow extends ControleAcesso implements CursoEtapaWorkflowInterfaceFacade {

    protected static String idEntidade;

    public CursoEtapaWorkflow() throws Exception {
        super();
        setIdEntidade("Workflow");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CursoEtapaWorkflowVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CursoEtapaWorkflowVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CursoEtapaWorkflowVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "INSERT INTO CursoEtapaWorkflow( etapaWorkflow, curso, script ) VALUES ( ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getEtapaWorkflow().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getEtapaWorkflow().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getCurso().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getCurso().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setString(3, obj.getScript());
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
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CursoEtapaWorkflowVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>CursoEtapaWorkflowVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CursoEtapaWorkflowVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "UPDATE CursoEtapaWorkflow set etapaWorkflow=?, curso=?, script=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getEtapaWorkflow().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getEtapaWorkflow().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (obj.getCurso().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getCurso().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setString(3, obj.getScript());
                sqlAlterar.setInt(4, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CursoEtapaWorkflowVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CursoEtapaWorkflowVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(CursoEtapaWorkflowVO obj, UsuarioVO usuario) throws Exception {
        CursoEtapaWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM CursoEtapaWorkflow WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>CursoEtapaWorkflowVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(CursoEtapaWorkflowVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }        
        if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
             throw  new Exception (UteisJSF.internacionalizar("msg_ArquivoWorkflow_curso"));
        }        

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>CursoEtapaWorkflowVO</code>.
     */
    public void validarUnicidade(List<CursoEtapaWorkflowVO> lista, CursoEtapaWorkflowVO obj) throws ConsistirException {
        for (CursoEtapaWorkflowVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(CursoEtapaWorkflowVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

//    /**
//     * Rotina responsavel por executar as consultas disponiveis na Tela CursoEtapaWorkflowCons.jsp.
//     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
//     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
//     */
//    public List<CursoEtapaWorkflowVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//        if (campoConsulta.equals(TipoConsultaComboCursoEtapaWorkflowEnum.CODIGO.toString())) {
//            if (valorConsulta.trim().equals("")) {
//                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
//            }
//            if (valorConsulta.equals("")) {
//                valorConsulta = "0";
//            }
//            int valorInt = Integer.parseInt(valorConsulta);
//            return getFacadeFactory().getCursoEtapaWorkflowFacade().consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
//        }
//        return new ArrayList(0);
//    }

//    /**
//     * Responsável por realizar uma consulta de <code>CursoEtapaWorkflow</code> através do valor do atributo
//     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
//     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
//     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
//     * @return  List Contendo vários objetos da classe <code>CursoEtapaWorkflowVO</code> resultantes da consulta.
//     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
//     */
//    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//        String sqlStr = "SELECT * FROM CursoEtapaWorkflow WHERE codigo >= ?  ORDER BY codigo";
//        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
//    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>CursoEtapaWorkflowVO</code> resultantes da consulta.
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
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>CursoEtapaWorkflowVO</code>.
     * @return  O objeto da classe <code>CursoEtapaWorkflowVO</code> com os dados devidamente montados.
     */
    public static CursoEtapaWorkflowVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CursoEtapaWorkflowVO obj = new CursoEtapaWorkflowVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getEtapaWorkflow().setCodigo(new Integer(dadosSQL.getInt("etapaWorkflow")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.setScript(dadosSQL.getString("script"));
        obj.getCurso().setNome((dadosSQL.getString("nome")));
        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosCurso(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto <code>CursoEtapaWorkflowVO</code>.
     * Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCurso(CursoEtapaWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(new Curso().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>CursoEtapaWorkflowVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>CursoEtapaWorkflow</code>.
     * @param <code>workflow</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirCursoEtapaWorkflows(Integer etapaWorkflow, UsuarioVO usuario) throws Exception {
        CursoEtapaWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM CursoEtapaWorkflow WHERE (etapaWorkflow = ?)" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{etapaWorkflow});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>CursoEtapaWorkflowVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirCursoEtapaWorkflows</code> e <code>incluirCursoEtapaWorkflows</code> disponíveis na classe <code>CursoEtapaWorkflow</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarCursoEtapaWorkflows(Integer etapaWorkflow, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM CursoEtapaWorkflow WHERE etapaWorkflow = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            CursoEtapaWorkflowVO objeto = (CursoEtapaWorkflowVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(str, new Object[]{etapaWorkflow});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CursoEtapaWorkflowVO objeto = (CursoEtapaWorkflowVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>CursoEtapaWorkflowVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>crm.Workflow</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirCursoEtapaWorkflows(Integer workflowPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CursoEtapaWorkflowVO obj = (CursoEtapaWorkflowVO) e.next();
            obj.getEtapaWorkflow().setCodigo(workflowPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>CursoEtapaWorkflowVO</code> relacionados a um objeto da classe <code>crm.Workflow</code>.
     * @param workflow  Atributo de <code>crm.Workflow</code> a ser utilizado para localizar os objetos da classe <code>CursoEtapaWorkflowVO</code>.
     * @return List  Contendo todos os objetos da classe <code>CursoEtapaWorkflowVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarCursoEtapaWorkflows(Integer etapaWorkflow, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CursoEtapaWorkflow.consultar(getIdEntidade());
        List objetos = new ArrayList();
        StringBuilder sql =  new StringBuilder("SELECT  cea.codigo,  cea.etapaworkflow, cea.curso, curso.nome as nome, cea.script as script ");
        sql.append(" from CursoEtapaWorkflow as cea  ");
        sql.append(" inner join curso on curso.codigo = cea.curso  ");
        sql.append(" Where cea.etapaworkflow = ?  ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{etapaWorkflow});
        while (resultado.next()) {
            CursoEtapaWorkflowVO novoObj = new CursoEtapaWorkflowVO();
            novoObj = CursoEtapaWorkflow.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CursoEtapaWorkflowVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CursoEtapaWorkflowVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sql =  new StringBuilder("SELECT  cea.codigo,  cea.etapaworkflow, cea.curso, curso.nome as nome ");
        sql.append(" from CursoEtapaWorkflow as cea  ");
        sql.append(" inner join curso on curso.codigo = cea.curso  ");
        sql.append(" Where cea.codigo = ?  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( CursoEtapaWorkflow ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public String consultarScriptPorCodigoCursoCodigoEtapa(Integer codigoEtapa, Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sql =  new StringBuilder("SELECT script ");
        sql.append(" from CursoEtapaWorkflow  ");
        sql.append(" Where curso = ").append(codigoCurso).append(" AND etapaworkflow = ").append(codigoEtapa);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (!tabelaResultado.next()) {
            return "";
        }
        return tabelaResultado.getString("script");
    }

    public List<CursoEtapaWorkflowVO> consultarUnicidade(CursoEtapaWorkflowVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CursoEtapaWorkflow.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        CursoEtapaWorkflow.idEntidade = idEntidade;
    }
}
