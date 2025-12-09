/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import negocio.comuns.crm.EtapaWorkflowAntecedenteVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.EtapaWorkflowAntecedenteInterfaceFacade;

/**
 *
 * @author PEDRO
 */
@Repository
@Scope("singleton")
@Lazy
public class EtapaWorkflowAntecedente extends ControleAcesso implements EtapaWorkflowAntecedenteInterfaceFacade {

    protected static String idEntidade;

    public EtapaWorkflowAntecedente() throws Exception {
        super();
        setIdEntidade("Workflow");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>EtapaWorkflowVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>EtapaWorkflowVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final EtapaWorkflowAntecedenteVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "INSERT INTO etapaworkflowantecedente( etapaAntecedente, etapaWorkflow ) VALUES ( ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getEtapaAntecedente().getCodigo().intValue());
                sqlInserir.setInt(2, obj.getEtapaWorkflow().getCodigo().intValue());
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>EtapaWorkflowVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>EtapaWorkflowVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final EtapaWorkflowAntecedenteVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "UPDATE etapaworkflowantecedente set etapaAntecedente=?, etapaWorkflow=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getEtapaAntecedente().getCodigo().intValue());
                sqlAlterar.setInt(2, obj.getEtapaWorkflow().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>EtapaWorkflowVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>EtapaWorkflowVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override    
    public void excluir(EtapaWorkflowAntecedenteVO obj, UsuarioVO usuario) throws Exception {
        EtapaWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM etapaworkflowantecedente WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>EtapaWorkflowVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    @Override
    public void validarDados(EtapaWorkflowAntecedenteVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (obj.getEtapaAntecedente().getNome() == null || obj.getEtapaAntecedente().getNome().equals("")) {
            throw new Exception("O campo ETAPA ANTECEDENTE (Etapa Workflow) deve ser informado.");
        }
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>EtapaWorkflowVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>EtapaWorkflow</code>.
     * @param <code>workflow</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirEtapaWorkflowsAntecedente(Integer etapaMae, UsuarioVO usuario) throws Exception {
        EtapaWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM etapaworkflowantecedente WHERE (etapaWorkflow = ?)" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{etapaMae});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>EtapaWorkflowVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirEtapaWorkflows</code> e <code>incluirEtapaWorkflows</code> disponíveis na classe <code>EtapaWorkflow</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void alterarEtapaWorkflowsAntecedente(Integer etapaMae, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM etapaworkflowantecedente WHERE etapaWorkflow = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            EtapaWorkflowAntecedenteVO objeto = (EtapaWorkflowAntecedenteVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(str, new Object[]{etapaMae});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            EtapaWorkflowAntecedenteVO objeto = (EtapaWorkflowAntecedenteVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.getEtapaWorkflow().setCodigo(etapaMae);
                incluir(objeto, usuario);
            } else {
                alterar(objeto, usuario);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>EtapaWorkflowVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>CRM.Workflow</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void incluirEtapaWorkflowsAntecedente(Integer etapaMae, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            EtapaWorkflowAntecedenteVO obj = (EtapaWorkflowAntecedenteVO) e.next();
            obj.getEtapaWorkflow().setCodigo(etapaMae);
            incluir(obj, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAtualizacaoEtapaWorkflowAntecedentes(WorkflowVO workflow, EtapaWorkflowVO etapa) {
        for (EtapaWorkflowAntecedenteVO etapaSerAtualizada : etapa.getEtapaWorkflowAntecedenteVOs()) {
            for (EtapaWorkflowVO etapaAtualizada : workflow.getEtapaWorkflowVOs()) {
                if (etapaSerAtualizada.getEtapaAntecedente().getNome().equals(etapaAtualizada.getNome())) {
                    etapaSerAtualizada.getEtapaAntecedente().setCodigo(etapaAtualizada.getCodigo());
                    break;
                }
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>EtapaWorkflowVO</code> relacionados a um objeto da classe <code>CRM.Workflow</code>.
     * @param workflow  Atributo de <code>CRM.Workflow</code> a ser utilizado para localizar os objetos da classe <code>EtapaWorkflowVO</code>.
     * @return List  Contendo todos os objetos da classe <code>EtapaWorkflowVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarEtapaWorkflows(Integer etapaMae, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        EtapaWorkflowAntecedente.consultar(getIdEntidade());
        StringBuilder sql = new StringBuilder(" SELECT  ea.codigo,  ea.etapaAntecedente, etapaworkflow.nome as nome, ea.etapaWorkflow ");
        sql.append(" from etapaworkflowantecedente as ea  ");
        sql.append(" inner join etapaworkflow on etapaworkflow.codigo = ea.etapaAntecedente ");
        sql.append(" WHERE  ea.etapaWorkflow = ").append(etapaMae).append(" ORDER BY ea.codigo");        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>EtapaWorkflowVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>EtapaWorkflowVO</code>.
     * @return  O objeto da classe <code>EtapaWorkflowVO</code> com os dados devidamente montados.
     */
    public static EtapaWorkflowAntecedenteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        EtapaWorkflowAntecedenteVO obj = new EtapaWorkflowAntecedenteVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getEtapaAntecedente().setCodigo(new Integer(dadosSQL.getInt("etapaAntecedente")));
        obj.getEtapaAntecedente().setNome((dadosSQL.getString("nome")));
        obj.getEtapaWorkflow().setCodigo(new Integer(dadosSQL.getInt("etapaWorkflow")));
        obj.setNovoObj(new Boolean(false));
        return obj;
    }


    public void preencherEtapaWorkflowAntecedenteSemReferenciaMemoria(EtapaWorkflowAntecedenteVO objDestino, EtapaWorkflowAntecedenteVO objOrigem) throws Exception {
        objDestino.setCodigo(objOrigem.getCodigo());
        objDestino.getEtapaAntecedente().setCodigo(objOrigem.getEtapaAntecedente().getCodigo());
        getFacadeFactory().getEtapaWorkflowFacade().preencherEtapaWorkflowSemReferenciaMemoria(objDestino.getEtapaWorkflow(), objOrigem.getEtapaWorkflow());
    }



    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return EtapaWorkflowAntecedente.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        EtapaWorkflowAntecedente.idEntidade = idEntidade;
    }
}
