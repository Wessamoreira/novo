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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.SituacaoProspectPipelineVO;
import negocio.comuns.crm.SituacaoProspectWorkflowVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboSituacaoProspectWorkflowEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.SituacaoProspectWorkflowInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>SituacaoProspectWorkflowVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>SituacaoProspectWorkflowVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see SituacaoProspectWorkflowVO
 * @see ControleAcesso
 * @see Workflow
 */
@Repository
@Scope("singleton")
@Lazy
public class SituacaoProspectWorkflow extends ControleAcesso implements SituacaoProspectWorkflowInterfaceFacade {

    protected static String idEntidade;

    public SituacaoProspectWorkflow() throws Exception {
        super();
        setIdEntidade("Workflow");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>SituacaoProspectWorkflowVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>SituacaoProspectWorkflowVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final SituacaoProspectWorkflowVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "INSERT INTO SituacaoProspectWorkflow( workflow, situacaoProspectPipeline, efetivacaoVendaHistorica ) VALUES ( ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getWorkflow().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getWorkflow().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getSituacaoProspectPipeline().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getSituacaoProspectPipeline().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setDouble(3, obj.getEfetivacaoVendaHistorica());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>SituacaoProspectWorkflowVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>SituacaoProspectWorkflowVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final SituacaoProspectWorkflowVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "UPDATE SituacaoProspectWorkflow set workflow=?, situacaoProspectPipeline=?, efetivacaoVendaHistorica=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getWorkflow().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getWorkflow().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (obj.getSituacaoProspectPipeline().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getSituacaoProspectPipeline().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setDouble(3, obj.getEfetivacaoVendaHistorica());
                sqlAlterar.setInt(4, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>SituacaoProspectWorkflowVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>SituacaoProspectWorkflowVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(SituacaoProspectWorkflowVO obj, UsuarioVO usuario) throws Exception {
        SituacaoProspectWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM SituacaoProspectWorkflow WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>SituacaoProspectWorkflowVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(SituacaoProspectWorkflowVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getSituacaoProspectPipeline() == null) || (obj.getSituacaoProspectPipeline().getCodigo().intValue() == 0)) {
            throw new Exception(UteisJSF.internacionalizar("msg_SituacaoProspectWorkflow_situacaoProspectPipeline"));
        }
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>SituacaoProspectWorkflowVO</code>.
     */
    public void validarUnicidade(List<SituacaoProspectWorkflowVO> lista, SituacaoProspectWorkflowVO obj) throws ConsistirException {
        for (SituacaoProspectWorkflowVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(SituacaoProspectWorkflowVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela SituacaoProspectWorkflowCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<SituacaoProspectWorkflowVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboSituacaoProspectWorkflowEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return getFacadeFactory().getSituacaoProspectWorkflowFacade().consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboSituacaoProspectWorkflowEnum.NOME_WORKFLOW.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getSituacaoProspectWorkflowFacade().consultarPorNomeWorkflow(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboSituacaoProspectWorkflowEnum.NOME_SITUACAO_PROSPECT_PIPELINE.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getSituacaoProspectWorkflowFacade().consultarPorNomeSituacaoProspectPipeline(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ArrayList(0);
    }

    public Boolean verificarExisteSituacao(List<SituacaoProspectWorkflowVO> listaSituacaoProspectWorkflowVO, SituacaoProspectWorkflowVO objSituacaoProspectWorkflowVO) {
        for (SituacaoProspectWorkflowVO obj : listaSituacaoProspectWorkflowVO) {
            if (obj.getSituacaoProspectPipeline().getControle().equals(objSituacaoProspectWorkflowVO.getSituacaoProspectPipeline().getControle()) && !obj.getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.NENHUM)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Responsável por realizar uma consulta de <code>SituacaoProspectWorkflow</code> através do valor do atributo 
     * <code>nome</code> da classe <code>SituacaoProspectPipeline</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>SituacaoProspectWorkflowVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeSituacaoProspectPipeline(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT SituacaoProspectWorkflow.* FROM SituacaoProspectWorkflow, SituacaoProspectPipeline WHERE SituacaoProspectWorkflow.situacaoProspectPipeline = SituacaoProspectPipeline.codigo and upper( SituacaoProspectPipeline.nome ) like(?) ORDER BY SituacaoProspectPipeline.nome";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>SituacaoProspectWorkflow</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Workflow</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>SituacaoProspectWorkflowVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeWorkflow(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT SituacaoProspectWorkflow.* FROM SituacaoProspectWorkflow, Workflow WHERE SituacaoProspectWorkflow.workflow = Workflow.codigo and upper( Workflow.nome ) like(?) ORDER BY Workflow.nome";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>SituacaoProspectWorkflow</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>SituacaoProspectWorkflowVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM SituacaoProspectWorkflow WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>SituacaoProspectWorkflowVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>SituacaoProspectWorkflowVO</code>.
     * @return  O objeto da classe <code>SituacaoProspectWorkflowVO</code> com os dados devidamente montados.
     */
    public static SituacaoProspectWorkflowVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        SituacaoProspectWorkflowVO obj = new SituacaoProspectWorkflowVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getWorkflow().setCodigo(new Integer(dadosSQL.getInt("workflow")));
        obj.getSituacaoProspectPipeline().setCodigo(new Integer(dadosSQL.getInt("situacaoProspectPipeline")));
        obj.setEfetivacaoVendaHistorica(dadosSQL.getDouble("efetivacaoVendaHistorica"));
        obj.setNovoObj(false);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosSituacaoProspectPipeline(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>SituacaoProspectPipelineVO</code> relacionado ao objeto <code>SituacaoProspectWorkflowVO</code>.
     * Faz uso da chave primária da classe <code>SituacaoProspectPipelineVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosSituacaoProspectPipeline(SituacaoProspectWorkflowVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getSituacaoProspectPipeline().getCodigo().intValue() == 0) {
            obj.setSituacaoProspectPipeline(new SituacaoProspectPipelineVO());
            return;
        }
        obj.setSituacaoProspectPipeline(new SituacaoProspectPipeline().consultarPorChavePrimaria(obj.getSituacaoProspectPipeline().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>SituacaoProspectWorkflowVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>SituacaoProspectWorkflow</code>.
     * @param <code>workflow</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirSituacaoProspectWorkflows(Integer workflow, UsuarioVO usuario) throws Exception {
        SituacaoProspectWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM SituacaoProspectWorkflow WHERE (workflow = ?)" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{workflow});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>SituacaoProspectWorkflowVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirSituacaoProspectWorkflows</code> e <code>incluirSituacaoProspectWorkflows</code> disponíveis na classe <code>SituacaoProspectWorkflow</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarSituacaoProspectWorkflows(Integer workflow, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM SituacaoProspectWorkflow WHERE workflow = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            SituacaoProspectWorkflowVO objeto = (SituacaoProspectWorkflowVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(str, new Object[]{workflow});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            SituacaoProspectWorkflowVO objeto = (SituacaoProspectWorkflowVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.getWorkflow().setCodigo(workflow);
                incluir(objeto, usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>SituacaoProspectWorkflowVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>crm.Workflow</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirSituacaoProspectWorkflows(Integer workflowPrm, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            SituacaoProspectWorkflowVO obj = (SituacaoProspectWorkflowVO) e.next();
            obj.getWorkflow().setCodigo(workflowPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>SituacaoProspectWorkflowVO</code> relacionados a um objeto da classe <code>crm.Workflow</code>.
     * @param workflow  Atributo de <code>crm.Workflow</code> a ser utilizado para localizar os objetos da classe <code>SituacaoProspectWorkflowVO</code>.
     * @return List  Contendo todos os objetos da classe <code>SituacaoProspectWorkflowVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarSituacaoProspectWorkflows(Integer workflow, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        SituacaoProspectWorkflow.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM SituacaoProspectWorkflow WHERE workflow = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{workflow});
        while (resultado.next()) {
            SituacaoProspectWorkflowVO novoObj = new SituacaoProspectWorkflowVO();
            novoObj = SituacaoProspectWorkflow.montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>SituacaoProspectWorkflowVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public SituacaoProspectWorkflowVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM SituacaoProspectWorkflow WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            return new SituacaoProspectWorkflowVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<SituacaoProspectWorkflowVO> consultarUnicidade(SituacaoProspectWorkflowVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return SituacaoProspectWorkflow.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        SituacaoProspectWorkflow.idEntidade = idEntidade;
    }
}
