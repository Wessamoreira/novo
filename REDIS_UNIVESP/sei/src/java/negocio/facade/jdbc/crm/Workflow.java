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
import java.util.Hashtable;
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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.SituacaoProspectWorkflowVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoWorkflowEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboWorkflowEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.WorkflowInterfaceFacade;

/**
 *
 * @author MarcoTulio
 */
@Repository
@Scope("singleton")
@Lazy
public class Workflow extends ControleAcesso implements WorkflowInterfaceFacade {

    protected static String idEntidade;
    private Hashtable etapaWorkflows;

    public Workflow() throws Exception {
        super();
        setIdEntidade("Workflow");
        setEtapaWorkflows(new Hashtable(0));
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>WorkflowVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>WorkflowVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final WorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            validarDados(obj);
            Workflow.incluir(getIdEntidade(), true , usuarioLogado);
            realizarUpperCaseDados(obj);
            final String sql = "INSERT INTO Workflow( nome, situacaoWorkflow, objetivo, tempoMedioGerarAgenda, numeroSegundosAlertarUsuarioTempoMaximoInteracao ) VALUES ( ?, ?, ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getTipoSituacaoWorkflow().name());
                    sqlInserir.setString(3, obj.getObjetivo());
                    sqlInserir.setInt(4, obj.getTempoMedioGerarAgenda().intValue());
                    sqlInserir.setInt(5, obj.getNumeroSegundosAlertarUsuarioTempoMaximoInteracao().intValue());
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
            getFacadeFactory().getSituacaoProspectWorkflowFacade().incluirSituacaoProspectWorkflows(obj.getCodigo(), obj.getSituacaoProspectWorkflowVOs(), usuarioLogado);
            getFacadeFactory().getEtapaWorkflowFacade().atualizarCodigoSituacaoProspectWorkflow(obj);
            getFacadeFactory().getEtapaWorkflowFacade().incluirEtapaWorkflows(obj.getCodigo(), obj.getEtapaWorkflowVOs(), usuarioLogado, configuracaoGeralSistemaVO);
            for (EtapaWorkflowVO etapa : obj.getEtapaWorkflowVOs()) {
                getFacadeFactory().getEtapaWorkflowAntecedenteFacade().realizarAtualizacaoEtapaWorkflowAntecedentes(obj, etapa);
                getFacadeFactory().getEtapaWorkflowAntecedenteFacade().incluirEtapaWorkflowsAntecedente(etapa.getCodigo(), etapa.getEtapaWorkflowAntecedenteVOs(), usuarioLogado);
            }

        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>WorkflowVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>WorkflowVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final WorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            validarDados(obj);
            Workflow.alterar(getIdEntidade(), true, usuarioLogado);
            realizarUpperCaseDados(obj);
            final String sql = "UPDATE Workflow set nome=?, situacaoWorkflow=?, objetivo=?, tempoMedioGerarAgenda=?, numeroSegundosAlertarUsuarioTempoMaximoInteracao=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getTipoSituacaoWorkflow().name());
                    sqlAlterar.setString(3, obj.getObjetivo());
                    sqlAlterar.setInt(4, obj.getTempoMedioGerarAgenda().intValue());
                    sqlAlterar.setInt(5, obj.getNumeroSegundosAlertarUsuarioTempoMaximoInteracao().intValue());
                    sqlAlterar.setInt(6, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

            getFacadeFactory().getSituacaoProspectWorkflowFacade().alterarSituacaoProspectWorkflows(obj.getCodigo(), obj.getSituacaoProspectWorkflowVOs(), usuarioLogado);
            getFacadeFactory().getEtapaWorkflowFacade().atualizarCodigoSituacaoProspectWorkflow(obj);
            getFacadeFactory().getEtapaWorkflowFacade().alterarEtapaWorkflows(obj.getCodigo(), obj.getEtapaWorkflowVOs(), usuarioLogado, configuracaoGeralSistemaVO);
            for (EtapaWorkflowVO etapa : obj.getEtapaWorkflowVOs()) {
                getFacadeFactory().getEtapaWorkflowAntecedenteFacade().realizarAtualizacaoEtapaWorkflowAntecedentes(obj, etapa);
                getFacadeFactory().getEtapaWorkflowAntecedenteFacade().alterarEtapaWorkflowsAntecedente(etapa.getCodigo(), etapa.getEtapaWorkflowAntecedenteVOs(), usuarioLogado);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>WorkflowVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>WorkflowVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(WorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            Workflow.excluir(getIdEntidade(), true, usuarioLogado);
            String sql = "DELETE FROM Workflow WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getSituacaoProspectWorkflowFacade().excluirSituacaoProspectWorkflows(obj.getCodigo(), usuarioLogado);
            for (EtapaWorkflowVO etapa : obj.getEtapaWorkflowVOs()) {
                getFacadeFactory().getEtapaWorkflowFacade().excluir(etapa, usuarioLogado, configuracaoGeralSistemaVO);
            }

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsavel por verificar se ira incluir ou alterar o objeto.
     * @param WorkflowVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(WorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        if (obj.getTipoSituacaoWorkflow().equals(TipoSituacaoWorkflowEnum.ATIVO)) {
            validarDadosParaAtivacao(obj);
        }
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuarioLogado, configuracaoGeralSistemaVO);
        } else {
            alterar(obj, usuarioLogado, configuracaoGeralSistemaVO);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>WorkflowVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(WorkflowVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (obj.getNome().equals("")) {
            throw new Exception(UteisJSF.internacionalizar("msg_Workflow_nome"));
        }

        if (obj.getTempoMedioGerarAgenda() == 0) {
            throw new Exception("O campo TEMPO MÉDIO GERAR AGENDA (Workflow) deve ser informado.");
        }
        
        if (obj.getTempoMedioGerarAgenda() >= 1440) {
            throw new Exception("O campo TEMPO MÉDIO GERAR AGENDA (Workflow) deve ser menor ao equivalente de um dia(1440 minutos).");
        }

//        if (!validarControleSituacaoProspect(obj)) {
//            throw new Exception("Deve existir ao menos 1 situação com controle Inicial, Finalizado com sucesso e Finalizado com insucesso.");
//        }

//        if (!validarSituacaoDefinirProspectFinalEtapa(obj)) {
//            throw new Exception("Deve existir ao menos 1 etapa situação de controle Inicial, Finalizado com sucesso e Finalizado com insucesso.");
//        }
    }

    public void validarDadosParaAtivacao(WorkflowVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (obj.getNome().equals("")) {
            throw new Exception(UteisJSF.internacionalizar("msg_Workflow_nome"));
        }

        if (obj.getTempoMedioGerarAgenda() == 0) {
            throw new Exception("O campo TEMPO MÉDIO GERAR AGENDA (Workflow) deve ser informado.");
        }
        //Validação autorizada pelo Thyago Jayme, devido estar sendo configurado em clientes tempo de 180 minutos causando assim lentidão no sistema
        //Autor Carlos
        if (obj.getTempoMedioGerarAgenda() > 10) {
            throw new Exception("O campo TEMPO MÉDIO GERAR AGENDA (Workflow) não pode ser superior a 10(min).");
        }

        if (!validarControleSituacaoProspect(obj)) {
            throw new Exception("Deve existir ao menos 1 situação com controle Inicial, Finalizado com sucesso e Finalizado com insucesso.");
        }

        if (!validarSituacaoDefinirProspectFinalEtapa(obj)) {
            throw new Exception("Deve existir ao menos 1 etapa situação de controle Inicial, Finalizado com sucesso e Finalizado com insucesso.");
        }
    }

    public Boolean validarControleSituacaoProspect(WorkflowVO obj) throws Exception {
        Boolean inicial = false;
        Boolean finalizadoSucesso = false;
        Boolean finalizadoInsucesso = false;
        for (SituacaoProspectWorkflowVO objSituacaoProspectWorkflowVO : obj.getSituacaoProspectWorkflowVOs()) {
            if (objSituacaoProspectWorkflowVO.getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                if (inicial == true) {
                    throw new Exception("Deve existir apenas 1 situação com controle 'Inicial'.");
                }
                inicial = true;
            }

            if (objSituacaoProspectWorkflowVO.getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)) {
                if (finalizadoSucesso == true) {
                    throw new Exception("Deve existir apenas 1 situação com controle 'Finalizado com sucesso'.");
                }
                finalizadoSucesso = true;
            }

            if (objSituacaoProspectWorkflowVO.getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO)) {
                if (finalizadoInsucesso == true) {
                    throw new Exception("Deve existir apenas 1 situação com controle 'Finalizado com insucesso'.");
                }
                finalizadoInsucesso = true;
            }
        }
        if (inicial && finalizadoSucesso && finalizadoInsucesso) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean validarSituacaoDefinirProspectFinalEtapa(WorkflowVO obj) throws Exception {
        Boolean inicial = false;
        Boolean finalizadoSucesso = false;
        Boolean finalizadoInsucesso = false;
        for (EtapaWorkflowVO objEtapaWorkflowVO : obj.getEtapaWorkflowVOs()) {
            if (objEtapaWorkflowVO.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                if (inicial == true) {
                    throw new Exception("Deve existir apenas 1 etapa com situacao de controle 'Inicial'.");
                }
                inicial = true;
            }

            if (objEtapaWorkflowVO.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)) {
                if (finalizadoSucesso == true) {
                    throw new Exception("Deve existir apenas 1 etapa com situacao de controle 'Finalizado com sucesso'.");
                }
                finalizadoSucesso = true;
            }

            if (objEtapaWorkflowVO.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO)) {
                if (finalizadoInsucesso == true) {
                    throw new Exception("Deve existir apenas 1 etapa com situacao de controle 'Finalizado com insucesso'.");
                }
                finalizadoInsucesso = true;
            }
        }
        if (inicial && finalizadoSucesso && finalizadoInsucesso) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>WorkflowVO</code>.
     */
    public void validarUnicidade(List<WorkflowVO> lista, WorkflowVO obj) throws ConsistirException {
        for (WorkflowVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(WorkflowVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        obj.setNome(obj.getNome().toUpperCase());
        obj.setObjetivo(obj.getObjetivo().toUpperCase());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela WorkflowCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<WorkflowVO> consultar(String valorConsulta, String campoConsulta, TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (campoConsulta.equalsIgnoreCase(TipoConsultaComboWorkflowEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return getFacadeFactory().getWorkflowFacade().consultarPorCodigo(valorInt, tipoSituacaoWorkflowEnum, controlarAcesso, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        }
        if (campoConsulta.equalsIgnoreCase(TipoConsultaComboWorkflowEnum.NOME.toString())) {
            return getFacadeFactory().getWorkflowFacade().consultarPorNome(valorConsulta, tipoSituacaoWorkflowEnum, controlarAcesso, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        }
        if (campoConsulta.equalsIgnoreCase(TipoConsultaComboWorkflowEnum.SITUACAO.toString())) {
        	//TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum = TipoSituacaoWorkflowEnum.ATIVO;
        	//return getFacadeFactory().getWorkflowFacade().consultarPorNome(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);        	
        	return getFacadeFactory().getWorkflowFacade().consultarPorNomePorSituacao(valorConsulta, tipoSituacaoWorkflowEnum, controlarAcesso, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>Workflow</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>WorkflowVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNome(String valorConsulta, TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT * FROM Workflow WHERE upper( nome ) like(?) and  situacaoWorkflow = '" + tipoSituacaoWorkflowEnum.toString() + "' ORDER BY nome";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Workflow</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>WorkflowVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePorSituacao(String valorConsulta, TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        valorConsulta += "%";
        String sqlStr = "SELECT * FROM Workflow WHERE upper( nome ) like(?) and  situacaoWorkflow = '" + tipoSituacaoWorkflowEnum.toString() + "' ORDER BY nome";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Workflow</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>WorkflowVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Workflow WHERE codigo >= ?  and  situacaoWorkflow = '" + tipoSituacaoWorkflowEnum.toString() + "' ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>WorkflowVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>WorkflowVO</code>.
     * @return  O objeto da classe <code>WorkflowVO</code> com os dados devidamente montados.
     */
    public static WorkflowVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        WorkflowVO obj = new WorkflowVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setTipoSituacaoWorkflow(TipoSituacaoWorkflowEnum.valueOf(dadosSQL.getString("situacaoWorkflow")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setObjetivo(dadosSQL.getString("objetivo"));
        obj.setTempoMedioGerarAgenda(new Integer(dadosSQL.getInt("tempoMedioGerarAgenda")));
        obj.setNumeroSegundosAlertarUsuarioTempoMaximoInteracao(new Integer(dadosSQL.getInt("numeroSegundosAlertarUsuarioTempoMaximoInteracao")));
        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setEtapaWorkflowVOs(EtapaWorkflow.consultarEtapaWorkflows(obj.getCodigo(), nivelMontarDados, usuario));
        obj.setSituacaoProspectWorkflowVOs(SituacaoProspectWorkflow.consultarSituacaoProspectWorkflows(obj.getCodigo(), nivelMontarDados, usuario));
        return obj;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>EtapaWorkflowVO</code>
     * ao List <code>etapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>EtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>EtapaWorkflowVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjEtapaWorkflowVOs(WorkflowVO objWorkflowVO, EtapaWorkflowVO obj, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getEtapaWorkflowFacade().validarDados(obj);
        obj.setWorkflow(objWorkflowVO);
        for (EtapaWorkflowVO objExistente : objWorkflowVO.getEtapaWorkflowVOs()) {
            if (Uteis.removerAcentos(objExistente.getNome()).equalsIgnoreCase(Uteis.removerAcentos(obj.getNome()))) {
                return;
            }
            if (objExistente.getNivelApresentacao().equals(obj.getNivelApresentacao()) && obj.getNivelApresentacao() == 1) {
                throw new Exception("Já existe uma etapa com NÍVEL DE APRESENTAÇÃO 1, por favor mude e tente adicionar novamente.");
            }
        }
        if (!obj.getSituacaoDefinirProspectFinal().getCodigo().equals(0)) {
            obj.getSituacaoDefinirProspectFinal().setSituacaoProspectPipeline(getFacadeFactory().getSituacaoProspectPipelineFacade().consultarPorChavePrimaria(obj.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        }
        objWorkflowVO.getEtapaWorkflowVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>EtapaWorkflowVO</code>
     * no List <code>etapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>EtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param nome  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjEtapaWorkflowVOs(WorkflowVO objWorkflowVO, String nome) throws Exception {
        int index = 0;
        for (EtapaWorkflowVO objExistente : objWorkflowVO.getEtapaWorkflowVOs()) {
            if (objExistente.getNome().equals(nome)) {
                objWorkflowVO.getEtapaWorkflowVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>EtapaWorkflowVO</code>
     * no List <code>etapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>EtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param nome  Parâmetro para localizar o objeto do List.
     */
    public EtapaWorkflowVO consultarObjEtapaWorkflowVO(WorkflowVO objWorkflowVO, String nome) throws Exception {
        for (EtapaWorkflowVO objExistente : objWorkflowVO.getEtapaWorkflowVOs()) {
            if (objExistente.getNome().equals(nome)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por adicionar um objeto da <code>EtapaWorkflowVO</code> no Hashtable <code>EtapaWorkflows</code>.
     * Neste Hashtable são mantidos todos os objetos de EtapaWorkflow de uma determinada Workflow.
     * @param obj  Objeto a ser adicionado no Hashtable.
     */
    public void adicionarObjEtapaWorkflows(EtapaWorkflowVO obj) throws Exception {
        getEtapaWorkflows().put(obj.getNome() + "", obj);
        //adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por remover um objeto da classe <code>EtapaWorkflowVO</code> do Hashtable <code>EtapaWorkflows</code>.
     * Neste Hashtable são mantidos todos os objetos de EtapaWorkflow de uma determinada Workflow.
     * @param Nome Atributo da classe <code>EtapaWorkflowVO</code> utilizado como apelido (key) no Hashtable.
     */
    public void excluirObjEtapaWorkflows(String Nome) throws Exception {
        getEtapaWorkflows().remove(Nome + "");
        //excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>WorkflowVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public WorkflowVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Workflow WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<WorkflowVO> consultarUnicidade(WorkflowVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }

    public Hashtable getEtapaWorkflows() {
        if (etapaWorkflows == null) {
            etapaWorkflows = new Hashtable(0);
        }
        return (etapaWorkflows);
    }

    public void setEtapaWorkflows(Hashtable etapaWorkflows) {
        this.etapaWorkflows = etapaWorkflows;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Workflow.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Workflow.idEntidade = idEntidade;
    }

    public void adicionarObjSituacaoProspectWorkflowVOs(WorkflowVO objWorkflowVO, SituacaoProspectWorkflowVO obj) throws Exception {
        getFacadeFactory().getSituacaoProspectWorkflowFacade().validarDados(obj);
        obj.setWorkflow(objWorkflowVO);
        int index = 0;
        for (SituacaoProspectWorkflowVO objExistente : objWorkflowVO.getSituacaoProspectWorkflowVOs()) {
            if (objExistente.getSituacaoProspectPipeline().getCodigo().equals(obj.getSituacaoProspectPipeline().getCodigo())) {
                objWorkflowVO.getSituacaoProspectWorkflowVOs().set(index, obj);
                return;
            }
            index++;
        }
        objWorkflowVO.getSituacaoProspectWorkflowVOs().add(obj);
    }

    public void ordenarListaSituacaoProspectWorkflow(List<SituacaoProspectWorkflowVO> lista) {
    	Ordenacao.ordenarLista(lista, "ordenacao");
//        for (int i = 0; i < lista.size(); i++) {
//            SituacaoProspectWorkflowVO obj = new SituacaoProspectWorkflowVO();
//            if (lista.get(i).getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL) && i != 0) {
//                obj = lista.get(0);
//                lista.set(0, lista.get(i));
//                lista.set(i, obj);
//                if (i > 0) {
//                    //i--;
//                }
//            }
//            if (lista.get(i).getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO) && i != (lista.size() - 2)) {
//                obj = lista.get((lista.size() - 2));
//                lista.set((lista.size() - 2), lista.get(i));
//                lista.set(i, obj);
//                if (i > 0) {
//                    i--;
//                }
//            }
//            if (lista.get(i).getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO) && i != lista.size() - 1) {
//                obj = lista.get(lista.size() - 1);
//                lista.set(lista.size() - 1, lista.get(i));
//                lista.set(i, obj);
//                if (i > 0) {
//                    i--;
//                }
//            }
//        }
    }

    public void mudarPosicaoListaSituacaoProspectWorkflow(List<SituacaoProspectWorkflowVO> lista, SituacaoProspectWorkflowVO obj, Boolean direcao) {
        for (int i = 0; i < lista.size(); i++) {
            SituacaoProspectWorkflowVO objExistente = new SituacaoProspectWorkflowVO();
            if (lista.get(i).getSituacaoProspectPipeline().getCodigo().equals(obj.getSituacaoProspectPipeline().getCodigo())) {
                if (direcao) {
                    objExistente = lista.get(i + 1);
                    lista.set(i, objExistente);
                    lista.set(i + 1, obj);
                    break;
                } else {
                    objExistente = lista.get(i - 1);
                    lista.set(i, objExistente);
                    lista.set(i - 1, obj);
                    break;
                }
            }
        }
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>SituacaoProspectWorkflowVO</code>
     * no List <code>situacaoProspectWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>SituacaoProspectWorkflow</code> - getSituacaoProspectPipeline().getCodigo() - como identificador (key) do objeto no List.
     * @param situacaoProspectPipeline  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjSituacaoProspectWorkflowVOs(WorkflowVO objWorkflowVO, Integer situacaoProspectPipeline) throws Exception {
        int index = 0;
        for (SituacaoProspectWorkflowVO objExistente : objWorkflowVO.getSituacaoProspectWorkflowVOs()) {
            if (objExistente.getSituacaoProspectPipeline().getCodigo().equals(situacaoProspectPipeline)) {
                objWorkflowVO.getSituacaoProspectWorkflowVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>SituacaoProspectWorkflowVO</code>
     * no List <code>situacaoProspectWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>SituacaoProspectWorkflow</code> - getSituacaoProspectPipeline().getCodigo() - como identificador (key) do objeto no List.
     * @param situacaoProspectPipeline  Parâmetro para localizar o objeto do List.
     */
    public SituacaoProspectWorkflowVO consultarObjSituacaoProspectWorkflowVO(WorkflowVO objWorkflowVO, Integer situacaoProspectPipeline) throws Exception {
        for (SituacaoProspectWorkflowVO objExistente : objWorkflowVO.getSituacaoProspectWorkflowVOs()) {
            if (objExistente.getSituacaoProspectPipeline().getCodigo().equals(situacaoProspectPipeline)) {
                return objExistente;
            }
        }
        return null;
    }
}
