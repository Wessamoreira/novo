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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ArquivoEtapaWorkflowVO;
import negocio.comuns.crm.CursoEtapaWorkflowVO;
import negocio.comuns.crm.EtapaWorkflowAntecedenteVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.SituacaoProspectWorkflowVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboEtapaWorkflowEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.EtapaWorkflowInterfaceFacade;

/**
 *
 * @author MarcoTulio
 */
@Repository
@Scope("singleton")
@Lazy
public class EtapaWorkflow extends ControleAcesso implements EtapaWorkflowInterfaceFacade {

    protected static String idEntidade;

    public EtapaWorkflow() throws Exception {
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
    public void incluir(final EtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "INSERT INTO EtapaWorkflow( workflow, nome, script, cor, tempoMinimo, tempoMaximo, permitirFinalizarDessaEtapa, obrigatorioInformarObservacao, nivelApresentacao, situacaoDefinirProspectFinal, motivo, corFonte ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getWorkflow().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getWorkflow().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getNome());
                sqlInserir.setString(3, obj.getScript());
                sqlInserir.setString(4, obj.getCor());
                sqlInserir.setLong(5, obj.getTempoMinimo().longValue());
                sqlInserir.setLong(6, obj.getTempoMaximo().longValue());
                sqlInserir.setBoolean(7, obj.isPermitirFinalizarDessaEtapa().booleanValue());
                sqlInserir.setBoolean(8, obj.isObrigatorioInformarObservacao().booleanValue());
                sqlInserir.setInt(9, obj.getNivelApresentacao().intValue());
                if (obj.getSituacaoDefinirProspectFinal().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(10, obj.getSituacaoDefinirProspectFinal().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(10, 0);
                }
                sqlInserir.setString(11, obj.getMotivo().trim());
                sqlInserir.setString(12, obj.getCorFonte());
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
        getFacadeFactory().getCursoEtapaWorkflowFacade().incluirCursoEtapaWorkflows(obj.getCodigo(), obj.getCursoEtapaWorkflowVOs(), usuarioLogado);
        getFacadeFactory().getArquivoEtapaWorkflowFacade().incluirArquivoEtapaWorkflows(obj.getCodigo(), obj.getArquivoEtapaWorkflowVOs(), usuarioLogado, configuracaoGeralSistemaVO);
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
    public void alterar(final EtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "UPDATE EtapaWorkflow set workflow=?, nome=?, script=?, cor=?, tempoMinimo=?, tempoMaximo=?, permitirFinalizarDessaEtapa=?, obrigatorioInformarObservacao=?, nivelApresentacao=?, situacaoDefinirProspectFinal=?, motivo = ?, corFonte=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getWorkflow().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getWorkflow().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setString(2, obj.getNome());
                sqlAlterar.setString(3, obj.getScript());
                sqlAlterar.setString(4, obj.getCor());
                sqlAlterar.setLong(5, obj.getTempoMinimo().longValue());
                sqlAlterar.setLong(6, obj.getTempoMaximo().longValue());
                sqlAlterar.setBoolean(7, obj.isPermitirFinalizarDessaEtapa().booleanValue());
                sqlAlterar.setBoolean(8, obj.isObrigatorioInformarObservacao().booleanValue());
                sqlAlterar.setInt(9, obj.getNivelApresentacao().intValue());
                if (obj.getSituacaoDefinirProspectFinal().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(10, obj.getSituacaoDefinirProspectFinal().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(10, 0);
                }
                sqlAlterar.setString(11, obj.getMotivo().trim());
                sqlAlterar.setString(12, obj.getCorFonte());
                sqlAlterar.setInt(13, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
        getFacadeFactory().getCursoEtapaWorkflowFacade().alterarCursoEtapaWorkflows(obj.getCodigo(), obj.getCursoEtapaWorkflowVOs(), usuarioLogado);
        getFacadeFactory().getArquivoEtapaWorkflowFacade().alterarArquivoEtapaWorkflows(obj.getCodigo(), obj.getArquivoEtapaWorkflowVOs(), usuarioLogado, configuracaoGeralSistemaVO);

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarScriptCorEtapa(final EtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("UPDATE EtapaWorkflow");
        sqlStr.append(" set script = '").append(obj.getScript()).append("',");
        sqlStr.append(" cor = '").append(obj.getCor()).append("',");
        sqlStr.append(" corFonte = '").append(obj.getCorFonte()).append("',");
        sqlStr.append(" permitirfinalizardessaetapa = '").append(obj.getPermitirFinalizarDessaEtapa()).append("',");
        sqlStr.append(" tempominimo = '").append(obj.getTempoMinimo()).append("',");
        sqlStr.append(" tempomaximo = '").append(obj.getTempoMaximo()).append("',");
        sqlStr.append(" obrigatorioInformarObservacao = '").append(obj.getObrigatorioInformarObservacao()).append("',");
        sqlStr.append(" motivo = '").append(obj.getMotivo()).append("'");
        sqlStr.append(" WHERE ((codigo = ").append(obj.getCodigo()).append("))");
        sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
        getConexao().getJdbcTemplate().update(sqlStr.toString());
        getFacadeFactory().getCursoEtapaWorkflowFacade().alterarCursoEtapaWorkflows(obj.getCodigo(), obj.getCursoEtapaWorkflowVOs(), usuarioLogado);
        getFacadeFactory().getArquivoEtapaWorkflowFacade().alterarArquivoEtapaWorkflows(obj.getCodigo(), obj.getArquivoEtapaWorkflowVOs(), usuarioLogado, configuracaoGeralSistemaVO);
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
    public void excluir(EtapaWorkflowVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        EtapaWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM EtapaWorkflow WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        getFacadeFactory().getCursoEtapaWorkflowFacade().excluirCursoEtapaWorkflows(obj.getCodigo(), usuarioLogado);
        for (ArquivoEtapaWorkflowVO arquivo : obj.getArquivoEtapaWorkflowVOs()) {
            getFacadeFactory().getArquivoEtapaWorkflowFacade().excluir(arquivo, usuarioLogado, configuracaoGeralSistemaVO);
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>EtapaWorkflowVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(EtapaWorkflowVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if (obj.getNome() == null || obj.getNome().equals("")) {
            throw new Exception("O campo NOME (Etapa Workflow) deve ser informado.");
        }
        if (obj.getScript() == null || obj.getScript().equals("")) {
            throw new Exception("O campo SCRIPT (Etapa Workflow) deve ser informado.");
        }
        if (obj.getTempoMinimo() == null || obj.getTempoMinimo() == 0) {
            throw new Exception("O campo Tempo Mínimo (Etapa Workflow) deve ser informado.");
        }
        if (obj.getTempoMaximo() == null || obj.getTempoMaximo() == 0) {
            throw new Exception("O campo Tempo Máximo (Etapa Workflow) deve ser informado.");
        }
        if (obj.getNivelApresentacao() == null || obj.getNivelApresentacao().equals(0)) {
            throw new Exception("O campo Nível Apresentação (Etapa Workflow) deve ser informado.");
        }
        if (obj.getCor() == null || obj.getCor().equals("") || obj.getCor().equals("#FFFFFF") || obj.getCor().equals("#000000")) {
            throw new Exception("O campo COR (Etapa Workflow) deve ser informado e diferente de branco e de preto.");
        }
        if (obj.getCorFonte() == null || obj.getCorFonte().equals("")) {
            throw new Exception("O campo COR FONTE (Etapa Workflow) deve ser informado.");
        }

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>EtapaWorkflowVO</code>.
     */
    public void validarUnicidade(List<EtapaWorkflowVO> lista, EtapaWorkflowVO obj) throws ConsistirException {
        for (EtapaWorkflowVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(EtapaWorkflowVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        obj.setNome(obj.getNome().toUpperCase());
        obj.setScript(obj.getScript().toUpperCase());
        obj.setCor(obj.getCor().toUpperCase());
//        obj.setSituacaoDefinirProspectFinal(obj.getSituacaoDefinirProspectFinal().toUpperCase());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela EtapaWorkflowCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<EtapaWorkflowVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboEtapaWorkflowEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>EtapaWorkflow</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>EtapaWorkflowVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM EtapaWorkflow WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    public List consultarEtapasAntecedentes(Integer codigoEtapaAtual, boolean controlarAcesso, int nivelMontarDados, Boolean recursividade, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT DISTINCT ");
        sqlStr.append(" etapaworkflowantecedente.codigo as etapaworkflowantecedente_codigo, ");
        sqlStr.append(" etapaworkflow.codigo as etapaworkflow_codigo, ");
        sqlStr.append(" etapaworkflow.nome as etapaworkflow_nome,  ");
        //sqlStr.append(" aewf.codigo AS aewf_codigo, ");
        //sqlStr.append(" arquivoEWF.codigo AS arquivoEWF_codigo,  ");
        //sqlStr.append(" arquivoEWF.nome AS arquivoEWF_nome,  ");
        //sqlStr.append(" arquivoEWF.descricao AS arquivoEWF_descricao,  ");
        //sqlStr.append(" arquivoEWF.pastabasearquivo AS arquivoEWF_pastabasearquivo,  ");
        //sqlStr.append(" cursoEWF.codigo AS cursoEWF_codigo, ");
        //sqlStr.append(" cewf.codigo AS cewf_codigo,  ");
        //sqlStr.append(" cewf.script AS cewf_script,  ");
        //sqlStr.append(" cursoEWF.nome AS cursoEWF_nome, ");
        sqlStr.append(" etapaworkflow.tempominimo as etapaworkflow_tempominimo, ");
        sqlStr.append(" etapaworkflow.tempomaximo as etapaworkflow_tempomaximo, ");
        sqlStr.append(" etapaworkflow.script as etapaworkflow_script, ");
        sqlStr.append(" etapaworkflow.nivelApresentacao as etapaworkflow_nivelApresentacao, ");
        sqlStr.append(" etapaworkflow.cor as etapaworkflow_cor, ");
        sqlStr.append(" etapaworkflow.corFonte as etapaworkflow_corFonte, ");
        sqlStr.append(" etapaworkflow.motivo as etapaworkflow_motivo, ");
        sqlStr.append(" etapaworkflow.permitirfinalizardessaetapa as etapaworkflow_permitirfinalizardessaetapa, ");
        sqlStr.append(" etapaworkflow.obrigatorioInformarObservacao AS etapaworkflow_obrigatorioInformarObservacao, ");
        sqlStr.append(" situacaoprospectworkflow.codigo as situacaoprospectworkflow_codigo, ");
        sqlStr.append(" situacaoprospectpipeline.codigo as situacaoprospectpipeline_codigo, ");
        sqlStr.append(" situacaoprospectpipeline.nome as situacaoprospectpipeline_nome, ");
        sqlStr.append(" situacaoprospectpipeline.controle as situacaoprospectpipeline_controle ");
        sqlStr.append(" from etapaworkflowantecedente  ");
        sqlStr.append(" inner join etapaworkflow on etapaworkflow.codigo = etapaworkflowantecedente.etapaworkflow ");
        sqlStr.append(" left join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = etapaworkflow.situacaodefinirprospectfinal ");
        sqlStr.append(" left join situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline  ");
        //sqlStr.append(" left join arquivoEtapaWorkflow AS aewf ON  aewf.etapaworkflow = etapaworkflow.codigo ");
        //sqlStr.append(" left join arquivo AS arquivoEWF ON arquivoEWF.codigo = aewf.arquivo ");
        //sqlStr.append(" left join cursoEtapaWorkFlow AS cewf ON cewf.etapaworkflow = etapaworkflow.codigo ");
        //sqlStr.append(" left join curso AS cursoEWF ON cursoEWF.codigo = cewf.curso ");
        sqlStr.append(" where etapaworkflowantecedente.etapaantecedente  = ").append(codigoEtapaAtual).append(" ORDER BY etapaworkflow.codigo");
        return (montarListaEtapasAntecedentes(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), recursividade, nivelMontarDados, usuario));
    }

    public List<EtapaWorkflowAntecedenteVO> montarListaEtapasAntecedentes(SqlRowSet tabelaResultado, Boolean recursividade, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<EtapaWorkflowAntecedenteVO> listaEtapasAntecedentes = new ArrayList(0);
        while (tabelaResultado.next()) {
            listaEtapasAntecedentes.add(montarEtapasAntecedentes(tabelaResultado, recursividade));
        }
        return listaEtapasAntecedentes;
    }

    public static EtapaWorkflowVO consultarAntecedentePorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM EtapaWorkflow WHERE codigo = ? ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{valorConsulta});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

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

    public static List montarDadosConsultaEtapaAntecedente(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            Integer codigoEtapaAntecedente = (new Integer(tabelaResultado.getInt("etapaAntecedente")));
            vetResultado.add(consultarAntecedentePorCodigo(codigoEtapaAntecedente, false, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>EtapaWorkflowVO</code>.
     * @return  O objeto da classe <code>EtapaWorkflowVO</code> com os dados devidamente montados.
     */
    public static EtapaWorkflowVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        EtapaWorkflowVO obj = new EtapaWorkflowVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getWorkflow().setCodigo(new Integer(dadosSQL.getInt("workflow")));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setScript(dadosSQL.getString("script"));
        obj.setCor(dadosSQL.getString("cor"));
        obj.setCorFonte(dadosSQL.getString("corFonte"));
        obj.setMotivo(dadosSQL.getString("motivo"));
        obj.setTempoMinimo(dadosSQL.getLong("tempoMinimo"));
        obj.setTempoMaximo(dadosSQL.getLong("tempoMaximo"));
        obj.setPermitirFinalizarDessaEtapa((dadosSQL.getBoolean("permitirFinalizarDessaEtapa")));
        obj.setObrigatorioInformarObservacao((dadosSQL.getBoolean("obrigatorioInformarObservacao")));
        obj.setNivelApresentacao(new Integer(dadosSQL.getInt("nivelApresentacao")));
        obj.getSituacaoDefinirProspectFinal().setCodigo(new Integer(dadosSQL.getInt("situacaoDefinirProspectFinal")));

        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setEtapaWorkflowAntecedenteVOs(EtapaWorkflowAntecedente.consultarEtapaWorkflows(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        obj.setCursoEtapaWorkflowVOs(CursoEtapaWorkflow.consultarCursoEtapaWorkflows(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        obj.setArquivoEtapaWorkflowVOs(ArquivoEtapaWorkflow.consultarArquivoEtapaWorkflows(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        obj.setSituacaoDefinirProspectFinal(getFacadeFactory().getSituacaoProspectWorkflowFacade().consultarPorChavePrimaria(obj.getSituacaoDefinirProspectFinal().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));


        return obj;
    }

    public void preencherEtapaWorkflowSemReferenciaMemoria(EtapaWorkflowVO objDestino, EtapaWorkflowVO objOrigem) {
        objDestino.setCodigo(objOrigem.getCodigo());
        objDestino.setNome(objOrigem.getNome());
        objDestino.setTempoMinimo(objOrigem.getTempoMinimo());
        objDestino.setTempoMaximo(objOrigem.getTempoMaximo());
        objDestino.setNivelApresentacao(objOrigem.getNivelApresentacao());
        objDestino.setScript(objOrigem.getScript());
        objDestino.setCor(objOrigem.getCor());
        objDestino.setCorFonte(objOrigem.getCorFonte());
        objDestino.setMotivo(objOrigem.getMotivo());
        objDestino.setArquivoEtapaWorkflowVOs(objOrigem.getArquivoEtapaWorkflowVOs());
        objDestino.setCursoEtapaWorkflowVOs(objOrigem.getCursoEtapaWorkflowVOs());
        objDestino.setPermitirFinalizarDessaEtapa(objOrigem.getPermitirFinalizarDessaEtapa());
        objDestino.setObrigatorioInformarObservacao(objOrigem.getObrigatorioInformarObservacao());
        objDestino.getSituacaoDefinirProspectFinal().setCodigo(objOrigem.getSituacaoDefinirProspectFinal().getCodigo());
        objDestino.getSituacaoDefinirProspectFinal().setEfetivacaoVendaHistorica(objOrigem.getSituacaoDefinirProspectFinal().getEfetivacaoVendaHistorica());
        objDestino.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(objOrigem.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getCodigo());
        objDestino.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(objOrigem.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getNome());
        objDestino.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(objOrigem.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle());
    }

    public void montarCursosEtapaWorkflowInteracaoWorkflow(SqlRowSet dadosSQL, InteracaoWorkflowVO obj) {
        CursoEtapaWorkflowVO cursoEtapaWorkflowVO = null;
        if (adicionarCursos(obj.getEtapaWorkflow().getCursoEtapaWorkflowVOs(), dadosSQL.getInt("cewf_codigo"))) {
            cursoEtapaWorkflowVO = new CursoEtapaWorkflowVO();
            cursoEtapaWorkflowVO.setCodigo(dadosSQL.getInt("cewf_codigo"));
            cursoEtapaWorkflowVO.getEtapaWorkflow().setCodigo((dadosSQL.getInt("ewf_codigo")));
            cursoEtapaWorkflowVO.getCurso().setCodigo((dadosSQL.getInt("cursoEWF_codigo")));
            cursoEtapaWorkflowVO.getCurso().setNome((dadosSQL.getString("cursoEWF_nome")));
            cursoEtapaWorkflowVO.setScript(dadosSQL.getString("cewf_script"));
            if (cursoEtapaWorkflowVO.getCodigo() != null && cursoEtapaWorkflowVO.getCodigo() != 0) {
                obj.getEtapaWorkflow().getCursoEtapaWorkflowVOs().add(cursoEtapaWorkflowVO);
            }
        }
    }

    public void montarEtapasAntecedentesInteracaoWorkflow(SqlRowSet dadosSQL, InteracaoWorkflowVO obj) {
        EtapaWorkflowAntecedenteVO etapaWorkflowAntecedenteVO = null;
        if (adicionarEtapaWorkflowAntecedente(obj.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs(), dadosSQL.getInt("ewfantecedente_codigo"))) {
            etapaWorkflowAntecedenteVO = new EtapaWorkflowAntecedenteVO();
            etapaWorkflowAntecedenteVO.setEtapaWorkflow(obj.getEtapaWorkflow());
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setCodigo(dadosSQL.getInt("ewfantecedente_codigo"));
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setNome(dadosSQL.getString("ewfantecedente_nome"));
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setTempoMinimo(dadosSQL.getLong("ewfantecedente_tempominimo"));
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setTempoMaximo(dadosSQL.getLong("ewfantecedente_tempomaximo"));
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setScript(dadosSQL.getString("ewfantecedente_script"));
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setCor(dadosSQL.getString("ewfantecedente_cor"));
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setCorFonte(dadosSQL.getString("ewfantecedente_corFonte"));
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setMotivo(dadosSQL.getString("ewfantecedente_motivo"));
            etapaWorkflowAntecedenteVO.getEtapaAntecedente().setPermitirFinalizarDessaEtapa(dadosSQL.getBoolean("ewfantecedente_permitirfinalizardessaetapa"));
            if (etapaWorkflowAntecedenteVO.getCodigo() != null && etapaWorkflowAntecedenteVO.getEtapaAntecedente().getCodigo() != 0) {
                obj.getEtapaWorkflow().getEtapaWorkflowAntecedenteVOs().add(etapaWorkflowAntecedenteVO);
            }
        }
    }

    public void montarArquivosEtapaWorkflowInteracaoWorkflow(SqlRowSet dadosSQL, InteracaoWorkflowVO obj) {
        ArquivoEtapaWorkflowVO arquivoEtapaWorkflowVO = null;
        if (adicionarArquivos(obj.getEtapaWorkflow().getArquivoEtapaWorkflowVOs(), dadosSQL.getInt("aewf_codigo"))) {
            arquivoEtapaWorkflowVO = new ArquivoEtapaWorkflowVO();
            arquivoEtapaWorkflowVO.setCodigo(dadosSQL.getInt("aewf_codigo"));
            arquivoEtapaWorkflowVO.getEtapaWorkflow().setCodigo(dadosSQL.getInt("ewf_codigo"));
            arquivoEtapaWorkflowVO.getArquivo().setCodigo((dadosSQL.getInt("arquivoEWF_codigo")));
            arquivoEtapaWorkflowVO.getArquivo().setNome((dadosSQL.getString("arquivoEWF_nome")));
            arquivoEtapaWorkflowVO.getArquivo().setDescricao((dadosSQL.getString("arquivoEWF_descricao")));
            arquivoEtapaWorkflowVO.getArquivo().setDescricaoAntesAlteracao((dadosSQL.getString("arquivoEWF_descricao")));
            if (arquivoEtapaWorkflowVO.getCodigo() != null && arquivoEtapaWorkflowVO.getCodigo() != 0) {
                obj.getEtapaWorkflow().getArquivoEtapaWorkflowVOs().add(arquivoEtapaWorkflowVO);
            }
        }
    }

    public EtapaWorkflowAntecedenteVO montarEtapasAntecedentes(SqlRowSet dadosSQL, Boolean recursividade) throws Exception {
        EtapaWorkflowAntecedenteVO etapaWorkflowAntecedenteVO = null;
        etapaWorkflowAntecedenteVO = new EtapaWorkflowAntecedenteVO();
        etapaWorkflowAntecedenteVO.setCodigo(dadosSQL.getInt("etapaworkflowantecedente_codigo"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setCodigo(dadosSQL.getInt("etapaworkflow_codigo"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setNome(dadosSQL.getString("etapaworkflow_nome"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setTempoMinimo(dadosSQL.getLong("etapaworkflow_tempominimo"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setTempoMaximo(dadosSQL.getLong("etapaworkflow_tempomaximo"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setNivelApresentacao(dadosSQL.getInt("etapaworkflow_nivelApresentacao"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setScript(dadosSQL.getString("etapaworkflow_script"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setCor(dadosSQL.getString("etapaworkflow_cor"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setCorFonte(dadosSQL.getString("etapaworkflow_corFonte"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setMotivo(dadosSQL.getString("etapaworkflow_motivo"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setPermitirFinalizarDessaEtapa(dadosSQL.getBoolean("etapaworkflow_permitirfinalizardessaetapa"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().setObrigatorioInformarObservacao(dadosSQL.getBoolean("etapaworkflow_obrigatorioInformarObservacao"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().getSituacaoDefinirProspectFinal().setCodigo(dadosSQL.getInt("situacaoprospectworkflow_codigo"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(dadosSQL.getInt("situacaoprospectpipeline_codigo"));
        etapaWorkflowAntecedenteVO.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(dadosSQL.getString("situacaoprospectpipeline_nome"));
        if (dadosSQL.getString("situacaoprospectpipeline_controle") != null && !dadosSQL.getString("situacaoprospectpipeline_controle").isEmpty()) {
            etapaWorkflowAntecedenteVO.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setControle(SituacaoProspectPipelineControleEnum.valueOf(dadosSQL.getString("situacaoprospectpipeline_controle")));
        }

        //montarArquivosEtapaWorkflowAntecedente(dadosSQL, etapaWorkflowAntecedenteVO);
        //montarCursosEtapaWorkflowAntecedente(dadosSQL, etapaWorkflowAntecedenteVO);

        //Método de recursividade (possível sobrecarga)
        if (recursividade) {
            etapaWorkflowAntecedenteVO.getEtapaWorkflow().setEtapaWorkflowAntecedenteVOs(getFacadeFactory().getEtapaWorkflowFacade().consultarEtapasAntecedentes(etapaWorkflowAntecedenteVO.getEtapaWorkflow().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, true, null));
        }
        return etapaWorkflowAntecedenteVO;
    }

    public boolean adicionarEtapaWorkflowAntecedente(List<EtapaWorkflowAntecedenteVO> lista, Integer codigo) {
        for (EtapaWorkflowAntecedenteVO etapaWorkflowAntecedente : lista) {
            if (etapaWorkflowAntecedente.getCodigo().equals(codigo)) {
                return false;
            }
        }
        return true;
    }

    public boolean adicionarCursos(List<CursoEtapaWorkflowVO> lista, Integer codigo) {
        for (CursoEtapaWorkflowVO curso : lista) {
            if (curso.getCodigo().equals(codigo)) {
                return false;
            }
        }
        return true;
    }

    public boolean adicionarArquivos(List<ArquivoEtapaWorkflowVO> lista, Integer codigo) {
        for (ArquivoEtapaWorkflowVO arquivo : lista) {
            if (arquivo.getCodigo().equals(codigo)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>EtapaWorkflowVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>EtapaWorkflow</code>.
     * @param <code>workflow</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirEtapaWorkflows(Integer workflow, UsuarioVO usuario) throws Exception {
        EtapaWorkflow.excluir(getIdEntidade());
        String sql = "DELETE FROM EtapaWorkflow WHERE (workflow = ?)" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{workflow});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>EtapaWorkflowVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirEtapaWorkflows</code> e <code>incluirEtapaWorkflows</code> disponíveis na classe <code>EtapaWorkflow</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarEtapaWorkflows(Integer workflow, List objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        String str = "DELETE FROM EtapaWorkflow WHERE workflow = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            EtapaWorkflowVO objeto = (EtapaWorkflowVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(str, new Object[]{workflow});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            EtapaWorkflowVO objeto = (EtapaWorkflowVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.getWorkflow().setCodigo(workflow);
                incluir(objeto, usuarioLogado, configuracaoGeralSistemaVO);
            } else {
                alterar(objeto, usuarioLogado, configuracaoGeralSistemaVO);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>EtapaWorkflowVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>CRM.Workflow</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirEtapaWorkflows(Integer workflowPrm, List objetos, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            EtapaWorkflowVO obj = (EtapaWorkflowVO) e.next();
            obj.getWorkflow().setCodigo(workflowPrm);
            incluir(obj, usuarioLogado, configuracaoGeralSistemaVO);
        }
    }

    public void atualizarCodigoSituacaoProspectWorkflow(WorkflowVO obj) {
        for (EtapaWorkflowVO etapa : obj.getEtapaWorkflowVOs()) {
            for (SituacaoProspectWorkflowVO situacao : obj.getSituacaoProspectWorkflowVOs()) {
                if (situacao.getSituacaoProspectPipeline().getCodigo().equals(etapa.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getCodigo())) {
                    etapa.setSituacaoDefinirProspectFinal(situacao);
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
    public static List consultarEtapaWorkflows(Integer workflow, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        EtapaWorkflow.consultar(getIdEntidade());
        String sql = "SELECT * FROM EtapaWorkflow WHERE workflow = " + workflow + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public static List consultarEtapaWorkflowAntecedentes(Integer etapaWorkflow, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        EtapaWorkflow.consultar(getIdEntidade());
        String sql = "select * from EtapaWorkflowAntecedente WHERE etapaworkflow = " + etapaWorkflow + " ORDER BY etapaAntecedente";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        return (montarDadosConsultaEtapaAntecedente(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>EtapaWorkflowVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public EtapaWorkflowVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM EtapaWorkflow WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>EtapaWorkflowVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public EtapaWorkflowVO consultarPorCodigoWorkFlowEtapaInicial(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        ControleAcesso.consultar(getIdEntidade(), false, usuario);
//        StringBuilder sql = new StringBuilder();
//        sql.append(" SELECT distinct etapaworkflow.* from etapaworkflow  ");        
//        sql.append(" inner join situacaoprospectworkflow as spw on spw.codigo = etapaworkflow.situacaodefinirprospectfinal ");
//        sql.append(" inner join situacaoprospectpipeline as spp on spp.codigo = spw.situacaoprospectpipeline and spp.controle = '").append(SituacaoProspectPipelineControleEnum.INICIAL).append("'  ");
//        sql.append(" where  etapaworkflow.workflow = ").append(codigoPrm);        
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//        if (!tabelaResultado.next()) {
//            throw new ConsistirException("Dados Não Encontrados.");
//        }
//        return montarDados(tabelaResultado, nivelMontarDados, usuario);
        return null;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>EtapaWorkflowVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public EtapaWorkflowVO consultarPorCodigoCampanhaEtapaInicial(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT distinct etapaworkflow.* from etapaworkflow  ");
        sql.append(" inner join workflow on workflow.codigo = etapaworkflow.workflow ");
        sql.append(" inner join campanha on workflow.codigo = campanha.workflow ");
        //sql.append(" inner join situacaoprospectworkflow as spw on spw.workflow = workflow.codigo ");
        //sql.append(" inner join situacaoprospectpipeline as spp on spp.codigo = spw.situacaoprospectpipeline and spp.controle = '").append(SituacaoProspectPipelineControleEnum.INICIAL).append("'  ");
        sql.append(" where  campanha.codigo = ").append(codigoPrm).append(" AND etapaworkflow.nivelapresentacao = 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (!tabelaResultado.next()) {
        	throw new ConsistirException("Dados Não Encontrados (Etapa Workflow).");
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<EtapaWorkflowVO> consultarUnicidade(EtapaWorkflowVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>CursoEtapaWorkflowVO</code>
     * ao List <code>CursoEtapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>CursoWorkflow</code> - getCurso().getCodigo() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>CursoEtapaWorkflowVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjCursoEtapaWorkflowVOs(EtapaWorkflowVO objEtapaWorkflowVO, CursoEtapaWorkflowVO obj) throws Exception {
        getFacadeFactory().getCursoEtapaWorkflowFacade().validarDados(obj);
        obj.setEtapaWorkflow(objEtapaWorkflowVO);
        int index = 0;
        for (CursoEtapaWorkflowVO objExistente : objEtapaWorkflowVO.getCursoEtapaWorkflowVOs()) {
            if (objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo())) {
                objEtapaWorkflowVO.getCursoEtapaWorkflowVOs().set(index, obj);
                return;
            }
            index++;
        }
        objEtapaWorkflowVO.getCursoEtapaWorkflowVOs().add(obj);
        //adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>CursoEtapaWorkflowVO</code>
     * no List <code>CursoEtapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>CursoWorkflow</code> - getCurso().getCodigo() - como identificador (key) do objeto no List.
     * @param curso  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjCursoEtapaWorkflowVOs(EtapaWorkflowVO objEtapaWorkflowVO, Integer curso) throws Exception {
        int index = 0;
        for (CursoEtapaWorkflowVO objExistente : objEtapaWorkflowVO.getCursoEtapaWorkflowVOs()) {
            if (objExistente.getCurso().getCodigo().equals(curso)) {
                objEtapaWorkflowVO.getCursoEtapaWorkflowVOs().remove(index);
                return;
            }
            index++;
        }
        //excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>CursoEtapaWorkflowVO</code>
     * no List <code>CursoEtapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>CursoWorkflow</code> - getCurso().getCodigo() - como identificador (key) do objeto no List.
     * @param curso  Parâmetro para localizar o objeto do List.
     */
    public CursoEtapaWorkflowVO consultarObjCursoEtapaWorkflowVO(EtapaWorkflowVO objEtapaWorkflowVO, Integer curso) throws Exception {
        for (CursoEtapaWorkflowVO objExistente : objEtapaWorkflowVO.getCursoEtapaWorkflowVOs()) {
            if (objExistente.getCurso().getCodigo().equals(curso)) {
                return objExistente;
            }
        }
        return null;
        //consultarObjSubordinadoOC
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>ArquivoEtapaWorkflowVO</code>
     * ao List <code>ArquivoEtapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>ArquivoEtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>ArquivoEtapaWorkflowVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjArquivoEtapaWorkflowVOs(EtapaWorkflowVO objEtapaWorkflowVO, ArquivoEtapaWorkflowVO obj) throws Exception {
        getFacadeFactory().getArquivoEtapaWorkflowFacade().validarDados(obj);
        obj.setEtapaWorkflow(objEtapaWorkflowVO);
        for (ArquivoEtapaWorkflowVO objExistente : objEtapaWorkflowVO.getArquivoEtapaWorkflowVOs()) {
            if (objExistente.getArquivo().getNome().equals(obj.getArquivo().getNome())) {
                return;
            }
        }
        objEtapaWorkflowVO.getArquivoEtapaWorkflowVOs().add(obj);
        //adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>ArquivoEtapaWorkflowVO</code>
     * no List <code>ArquivoEtapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>ArquivoEtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param nome  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjArquivoEtapaWorkflowVOs(EtapaWorkflowVO objEtapaWorkflowVO, String nome) throws Exception {
        int index = 0;
        for (ArquivoEtapaWorkflowVO objExistente : objEtapaWorkflowVO.getArquivoEtapaWorkflowVOs()) {
            if (objExistente.getArquivo().getNome().equals(nome)) {
                objEtapaWorkflowVO.getArquivoEtapaWorkflowVOs().remove(index);
                return;
            }
            index++;
        }
        //excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>ArquivoEtapaWorkflowVO</code>
     * no List <code>ArquivoEtapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>ArquivoEtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param nome  Parâmetro para localizar o objeto do List.
     */
    public ArquivoEtapaWorkflowVO consultarObjArquivoEtapaWorkflowVO(EtapaWorkflowVO objEtapaWorkflowVO, String nome) throws Exception {
        for (ArquivoEtapaWorkflowVO objExistente : objEtapaWorkflowVO.getArquivoEtapaWorkflowVOs()) {
            if (objExistente.getArquivo().getNome().equals(nome)) {
                return objExistente;
            }
        }
        return null;
        //consultarObjSubordinadoOC
    }

    /**
     * Operação responsável por consultar e carregar lista de objetos da classe <code>ArquivoEtapaWorkflowVO</code>
     * no List <code>ArquivoEtapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>ArquivoEtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param NOME  Parâmetro para localizar o objeto do List.
     */
    public void carregarArquivosEtapaWorkflowVO(EtapaWorkflowVO objEtapaWorkflowVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT DISTINCT ");
        sqlStr.append(" etapaworkflow.codigo as etapaworkflow_codigo, ");
        sqlStr.append(" etapaworkflow.nome as etapaworkflow_nome,  ");
        sqlStr.append(" arquivoEtapaWorkflow.codigo AS arquivoEtapaWorkflow_codigo, ");
        sqlStr.append(" arquivo.codigo AS arquivo_codigo,  ");
        sqlStr.append(" arquivo.nome AS arquivo_nome,  ");
        sqlStr.append(" arquivo.descricao AS arquivo_descricao,  ");
        sqlStr.append(" arquivo.pastabasearquivo AS arquivo_pastabasearquivo ");
        sqlStr.append(" from arquivoEtapaWorkflow  ");
        sqlStr.append(" inner join etapaworkflow on etapaworkflow.codigo = arquivoEtapaWorkflow.etapaworkflow ");
        sqlStr.append(" left join arquivo ON arquivo.codigo = arquivoEtapaWorkflow.arquivo ");
        sqlStr.append(" where etapaworkflow.codigo  = ").append(objEtapaWorkflowVO.getCodigo()).append(" ORDER BY arquivo.descricao");
        montarArquivosEtapaWorkflow(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), objEtapaWorkflowVO);
    }

    public void montarArquivosEtapaWorkflow(SqlRowSet dadosSQL, EtapaWorkflowVO obj) {
        while (dadosSQL.next()) {
            ArquivoEtapaWorkflowVO arquivoEtapaWorkflowVO = null;
            if (adicionarArquivos(obj.getArquivoEtapaWorkflowVOs(), dadosSQL.getInt("arquivoEtapaWorkflow_codigo"))) {
                arquivoEtapaWorkflowVO = new ArquivoEtapaWorkflowVO();
                arquivoEtapaWorkflowVO.setCodigo(dadosSQL.getInt("arquivoEtapaWorkflow_codigo"));
                arquivoEtapaWorkflowVO.getEtapaWorkflow().setCodigo(dadosSQL.getInt("etapaworkflow_codigo"));
                arquivoEtapaWorkflowVO.getArquivo().setCodigo((dadosSQL.getInt("arquivo_codigo")));
                arquivoEtapaWorkflowVO.getArquivo().setNome((dadosSQL.getString("arquivo_nome")));
                arquivoEtapaWorkflowVO.getArquivo().setDescricao((dadosSQL.getString("arquivo_descricao")));
                arquivoEtapaWorkflowVO.getArquivo().setDescricaoAntesAlteracao((dadosSQL.getString("arquivo_descricao")));
                arquivoEtapaWorkflowVO.getArquivo().setPastaBaseArquivo((dadosSQL.getString("arquivo_pastabasearquivo")));
                if (arquivoEtapaWorkflowVO.getCodigo() != null && arquivoEtapaWorkflowVO.getCodigo() != 0) {
                    obj.getArquivoEtapaWorkflowVOs().add(arquivoEtapaWorkflowVO);
                }
            }
        }
    }

    public void carregarCursosEtapaWorkflowVO(EtapaWorkflowVO objEtapaWorkflowVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT DISTINCT ");
        sqlStr.append(" etapaworkflow.codigo as etapaworkflow_codigo, ");
        sqlStr.append(" etapaworkflow.nome as etapaworkflow_nome,  ");
        sqlStr.append(" curso.codigo AS curso_codigo, ");
        sqlStr.append(" cursoEtapaWorkFlow.codigo AS cursoEtapaWorkFlow_codigo,  ");
        sqlStr.append(" cursoEtapaWorkFlow.script AS cursoEtapaWorkFlow_script,  ");
        sqlStr.append(" curso.nome AS curso_nome ");
        sqlStr.append(" from cursoEtapaWorkFlow  ");
        sqlStr.append(" inner join etapaworkflow on etapaworkflow.codigo = cursoEtapaWorkFlow.etapaworkflow ");
        sqlStr.append(" left join curso ON curso.codigo = cursoEtapaWorkFlow.curso ");
        sqlStr.append(" where etapaworkflow.codigo  = ").append(objEtapaWorkflowVO.getCodigo()).append(" ORDER BY curso.nome");

        montarCursosEtapaWorkflow(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), objEtapaWorkflowVO);
    }

    public void montarCursosEtapaWorkflow(SqlRowSet dadosSQL, EtapaWorkflowVO obj) {
        
        while (dadosSQL.next()) {
            CursoEtapaWorkflowVO cursoEtapaWorkflowVO = null;
            if (adicionarCursos(obj.getCursoEtapaWorkflowVOs(), dadosSQL.getInt("cursoEtapaWorkFlow_codigo"))) {
                cursoEtapaWorkflowVO = new CursoEtapaWorkflowVO();
                cursoEtapaWorkflowVO.setCodigo(dadosSQL.getInt("cursoEtapaWorkFlow_codigo"));
                cursoEtapaWorkflowVO.getEtapaWorkflow().setCodigo((dadosSQL.getInt("etapaworkflow_codigo")));
                cursoEtapaWorkflowVO.getCurso().setCodigo((dadosSQL.getInt("curso_codigo")));
                cursoEtapaWorkflowVO.getCurso().setNome((dadosSQL.getString("curso_nome")));
                cursoEtapaWorkflowVO.setScript(dadosSQL.getString("cursoEtapaWorkFlow_script"));
                if (cursoEtapaWorkflowVO.getCodigo() != null && cursoEtapaWorkflowVO.getCodigo() != 0) {
                    obj.getCursoEtapaWorkflowVOs().add(cursoEtapaWorkflowVO);
                }
            }
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return EtapaWorkflow.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        EtapaWorkflow.idEntidade = idEntidade;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>EtapaWorkflowVO</code>
     * ao List <code>etapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>EtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>EtapaWorkflowVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjEtapaWorkflowSubordinadaVOs(EtapaWorkflowVO obj, EtapaWorkflowAntecedenteVO etapaWorkflowAntecedenteVO) throws Exception {
        etapaWorkflowAntecedenteVO.setEtapaWorkflow(obj);
        if (etapaWorkflowAntecedenteVO.getEtapaAntecedente().getStringConcatenadaNomeNivelApresentacao().substring(0, etapaWorkflowAntecedenteVO.getEtapaAntecedente().getStringConcatenadaNomeNivelApresentacao().lastIndexOf(" ") - 1).equals("")) {
            throw new Exception("O campo ETAPA WORKFLOW (Etapas Antecedentes) deve ser informado.");
        }
        if (etapaWorkflowAntecedenteVO.getEtapaAntecedente().getStringConcatenadaNomeNivelApresentacao().substring(0, etapaWorkflowAntecedenteVO.getEtapaAntecedente().getStringConcatenadaNomeNivelApresentacao().lastIndexOf(" ") - 1).equals(etapaWorkflowAntecedenteVO.getEtapaWorkflow().getNome())) {
            throw new Exception("A ETAPA não pode anteceder ela mesma.");
        }
        if (Integer.parseInt(etapaWorkflowAntecedenteVO.getEtapaAntecedente().getStringConcatenadaNomeNivelApresentacao().substring(etapaWorkflowAntecedenteVO.getEtapaAntecedente().getStringConcatenadaNomeNivelApresentacao().lastIndexOf(" ") + 1))
                > etapaWorkflowAntecedenteVO.getEtapaWorkflow().getNivelApresentacao().intValue()) {
            throw new Exception("A ETAPA ANTECEDENTE não pode ter NÍVEL DE APRESENTAÇÃO maior que a etapa a qual ele antecede.");
        }
        for (EtapaWorkflowAntecedenteVO objExistente : obj.getEtapaWorkflowAntecedenteVOs()) {
            if (objExistente.getEtapaAntecedente().getNome().equals(etapaWorkflowAntecedenteVO.getEtapaAntecedente().getNome())) {
                return;
            }
        }
        etapaWorkflowAntecedenteVO.getEtapaAntecedente().setNome(etapaWorkflowAntecedenteVO.getEtapaAntecedente().getStringConcatenadaNomeNivelApresentacao().substring(0, etapaWorkflowAntecedenteVO.getEtapaAntecedente().getStringConcatenadaNomeNivelApresentacao().lastIndexOf(" ")));
        obj.getEtapaWorkflowAntecedenteVOs().add(etapaWorkflowAntecedenteVO);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>EtapaWorkflowVO</code>
     * no List <code>etapaWorkflowVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>EtapaWorkflow</code> - getNome() - como identificador (key) do objeto no List.
     * @param nome  Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjEtapaWorkflowVOs(EtapaWorkflowVO objEtapaWorkflow, String nome) throws Exception {
        int index = 0;
        for (EtapaWorkflowAntecedenteVO objExistente : objEtapaWorkflow.getEtapaWorkflowAntecedenteVOs()) {
            if (objExistente.getEtapaAntecedente().getNome().equals(nome)) {
                objEtapaWorkflow.getEtapaWorkflowAntecedenteVOs().remove(index);
                return;
            }
            index++;
        }
    }
}
