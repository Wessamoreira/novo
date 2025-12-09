package negocio.facade.jdbc.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.FamiliaresVO;
import negocio.comuns.crm.FollowUpVO;
import negocio.comuns.crm.HistoricoFollowUpVO;
import negocio.comuns.crm.InteracaoWorkflowHistoricoVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoFollowUpEnum;
import negocio.comuns.crm.enumerador.TipoInteracaoEnum;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.FollowUpInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>FollowUpVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>FollowUpVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see FollowUpVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class FollowUp extends ControleAcesso implements FollowUpInterfaceFacade {

    protected static String idEntidade;
    private Hashtable historicoFollowUps;

    public FollowUp() throws Exception {
        super();
        setIdEntidade("FollowUp");
        setHistoricoFollowUps(new Hashtable(0));
    }

//    /**
//     * Operação responsável por incluir no banco de dados um objeto da classe <code>FollowUpVO</code>.
//     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
//     * para realizar esta operacão na entidade.
//     * Isto, através da operação <code>incluir</code> da superclasse.
//     * @param obj  Objeto da classe <code>FollowUpVO</code> que será gravado no banco de dados.
//     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
//     */
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public void incluir(final FollowUpVO obj) throws Exception {
//        try {
//            validarDados(obj);
//            FollowUp.incluir(getIdEntidade());
//            realizarUpperCaseDados(obj);
//            final String sql = "INSERT INTO FollowUp( prospect, compromissoagendapessoahorario ) VALUES ( ?,? ) returning codigo";
//            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
//
//                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
//                    if (obj.getProspect().getCodigo().intValue() != 0) {
//                        sqlInserir.setInt(1, obj.getProspect().getCodigo().intValue());
//                    } else {
//                        sqlInserir.setNull(1, 0);
//                    }
//                    if (obj.getCompromissoAgendaPessoaHorario().getCodigo().intValue() != 0) {
//                        sqlInserir.setInt(2, obj.getCompromissoAgendaPessoaHorario().getCodigo().intValue());
//                    } else {
//                        sqlInserir.setNull(2, 0);
//                    }
//                    return sqlInserir;
//                }
//            }, new ResultSetExtractor() {
//
//                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
//                    if (arg0.next()) {
//                        obj.setNovoObj(Boolean.FALSE);
//                        return arg0.getInt("codigo");
//                    }
//                    return null;
//                }
//            }));
//            obj.setNovoObj(Boolean.FALSE);
//            getFacadeFactory().getHistoricoFollowUpFacade().incluirHistoricoFollowUps(obj.getCodigo(), obj.getHistoricoFollowUpVOs());
//        } catch (Exception e) {
//            obj.setNovoObj(Boolean.TRUE);
//            throw e;
//        }
//    }
//
//    /**
//     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FollowUpVO</code>.
//     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
//     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
//     * para realizar esta operacão na entidade.
//     * Isto, através da operação <code>alterar</code> da superclasse.
//     * @param obj    Objeto da classe <code>FollowUpVO</code> que será alterada no banco de dados.
//     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
//     */
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public void alterar(final FollowUpVO obj) throws Exception {
//        try {
//            validarDados(obj);
//            FollowUp.alterar(getIdEntidade());
//            realizarUpperCaseDados(obj);
//            final String sql = "UPDATE FollowUp set prospect=? WHERE ((codigo = ?))";
//            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
//
//                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
//                    if (obj.getProspect().getCodigo().intValue() != 0) {
//                        sqlAlterar.setInt(1, obj.getProspect().getCodigo().intValue());
//                    } else {
//                        sqlAlterar.setNull(1, 0);
//                    }
//                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
//                    return sqlAlterar;
//                }
//            });
//            getFacadeFactory().getHistoricoFollowUpFacade().excluirTodosHistoricosFollowUps(obj.getCodigo());
//            getFacadeFactory().getHistoricoFollowUpFacade().incluirHistoricoFollowUps(obj.getCodigo(), obj.getHistoricoFollowUpVOs());
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
//    /**
//     * Operação responsável por excluir no BD um objeto da classe <code>FollowUpVO</code>.
//     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
//     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
//     * para realizar esta operacão na entidade.
//     * Isto, através da operação <code>excluir</code> da superclasse.
//     * @param obj    Objeto da classe <code>FollowUpVO</code> que será removido no banco de dados.
//     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
//     */
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public void excluir(FollowUpVO obj) throws Exception {
//        try {
//            FollowUp.excluir(getIdEntidade());
////            getFacadeFactory().getHistoricoFollowUpFacade().excluirHistoricoFollowUps(obj.getCodigo());
//            String sql = "DELETE FROM FollowUp WHERE ((codigo = ?))";
//            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
//    /**
//     * Método responsavel por verificar se ira incluir ou alterar o objeto.
//     * @param FollowUpVO
//     * @throws Exception
//     */
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public void persistir(FollowUpVO obj) throws Exception {
//        if (obj.isNovoObj().booleanValue()) {
//            incluir(obj);
//        } else {
//            alterar(obj);
//        }
//    }
//    /**
//     * Operação responsável por validar os dados de um objeto da classe <code>FollowUpVO</code>.
//     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
//     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
//     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
//     * o atributo e o erro ocorrido.
//     */
//    public void validarDados(FollowUpVO obj) throws ConsistirException {
//        if (!obj.isValidarDados().booleanValue()) {
//            return;
//        }
//        ConsistirException consistir = new ConsistirException();
//        if ((obj.getProspect() == null)
//                || (obj.getProspect().getCodigo().intValue() == 0)) {
//            consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FollowUp_prospect"));
//        }
//        if (consistir.existeErroListaMensagemErro()) {
//            throw consistir;
//        }
//
//    }
//
//    /**
//     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
//     */
//    public void realizarUpperCaseDados(FollowUpVO obj) {
//        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
//            return;
//        }
//    }
    public List consultaMontarComboboxWorkflow(Integer codigoProspect) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT DISTINCT workflow.codigo AS workflow_codigo,");
        sqlStr.append("workflow.nome AS workflow_nome ");
        sqlStr.append("FROM interacaoworkflow ");
        sqlStr.append("INNER JOIN workflow ON interacaoworkflow.workflow = workflow.codigo ");
        sqlStr.append("WHERE interacaoworkflow.prospect = ").append(codigoProspect);
        return montarWorkflowCombobox(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
    }

    public List consultaMontarComboboxCampanha(Integer codigoProspect) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT DISTINCT campanha.codigo AS campanha_codigo,");
        sqlStr.append("campanha.descricao AS campanha_descricao ");
        sqlStr.append("FROM interacaoworkflow ");
        sqlStr.append("INNER JOIN campanha ON interacaoworkflow.campanha = campanha.codigo ");
        sqlStr.append("WHERE interacaoworkflow.prospect = ").append(codigoProspect);
        return montarCampanhaCombobox(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
    }

    public List consultaMontarComboboxResponsavel(Integer codigoProspect) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT DISTINCT pessoa.codigo AS pessoa_codigo,");
        sqlStr.append("pessoa.nome AS pessoa_nome, ");
        sqlStr.append("usuario.codigo AS usuario_codigo ");
        sqlStr.append("FROM interacaoworkflow ");
        sqlStr.append("INNER JOIN usuario ON interacaoworkflow.responsavel = usuario.codigo ");
        sqlStr.append("INNER JOIN pessoa ON usuario.pessoa = pessoa.codigo ");
        sqlStr.append("WHERE interacaoworkflow.prospect = ").append(codigoProspect);
        return montarResponsavelCombobox(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
    }

    public List consultaMontarComboboxCurso(Integer codigoProspect) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT DISTINCT curso.codigo AS curso_codigo,");
        sqlStr.append("curso.nome AS curso_nome, curso.descricao AS curso_descricao ");
        sqlStr.append("FROM interacaoworkflow ");
        sqlStr.append("INNER JOIN curso ON interacaoworkflow.curso = curso.codigo ");
        sqlStr.append("WHERE interacaoworkflow.prospect = ").append(codigoProspect);
        return montarCursoCombobox(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
    }
    
    public List consultarInteracoes(Integer codigoWorkflow, Integer codigoCampanha, Integer codigoResponsavel, Integer codigoCurso, Integer limite, Integer offset, FollowUpVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	return consultarInteracoes(codigoWorkflow, codigoCampanha, codigoResponsavel, codigoCurso, null, null, null, limite, offset, obj, controlarAcesso, nivelMontarDados, usuario); 
    }

    public List<InteracaoWorkflowVO> consultarInteracoes(Integer codigoWorkflow, Integer codigoCampanha, Integer codigoResponsavel, Integer codigoCurso, TipoOrigemInteracaoEnum tipoOrigem, String identificadorOrigem, Integer codigoEntidadeOrigem, Integer limite, Integer offset, FollowUpVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        getSQLPadraoConsultaInteracoes(sqlStr);
        getFiltroPadraoConsultaInteracoes(codigoWorkflow, codigoCampanha, codigoResponsavel, codigoCurso, tipoOrigem, identificadorOrigem, codigoEntidadeOrigem, obj.getProspect().getCodigo(), "", sqlStr);
        sqlStr.append(" ORDER BY iwf.datainicio desc, iwf.horainicio desc  ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosInteracoes(tabelaResultado, obj);
    }

    public Integer consultarTotalDeRegistroInteracoes(Integer codigoWorkflow, Integer codigoCampanha, Integer codigoResponsavel, Integer codigoCurso, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT DISTINCT COUNT (InteracaoWorkflow.codigo) FROM InteracaoWorkflow WHERE prospect = ").append(valorConsulta);
        if (codigoWorkflow != 0) {
            sqlsb.append(" AND  InteracaoWorkflow.workflow = ").append(codigoWorkflow).append(" ");
        }
        if (codigoCampanha != 0) {
            sqlsb.append(" AND  InteracaoWorkflow.campanha = ").append(codigoCampanha).append(" ");
        }
        if (codigoResponsavel != 0) {
            sqlsb.append(" AND  InteracaoWorkflow.responsavel = ").append(codigoResponsavel).append(" ");
        }
        if (codigoCurso != 0) {
            sqlsb.append(" AND  InteracaoWorkflow.curso = ").append(codigoResponsavel).append(" ");
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public FollowUpVO consultarFollowUpPorCodigoProspect(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        StringBuilder sqlStr = new StringBuilder();
        //Dados prospect
        sqlStr.append("SELECT prospects.codigo AS prospects_codigo,");
        sqlStr.append("prospects.nome AS nome,");
        sqlStr.append("prospects.dataNascimento AS dataNascimento,");
        sqlStr.append("prospects.sexo AS sexo, ");
        sqlStr.append("prospects.unidadeEnsino AS prospects_unidadeEnsino, ");
        sqlStr.append("arquivo.pastaBaseArquivo AS pastaBaseArquivo,");
        sqlStr.append("arquivo.codigo AS codArquivo, arquivo.nome AS nomeArquivo, ");
        sqlStr.append("prospects.emailprincipal AS prospects_email,  ");
        sqlStr.append("prospects.telefoneresidencial AS prospects_telefoneresidencial ,  ");
        sqlStr.append("prospects.celular AS prospects_celular, ");
        sqlStr.append("prospects.telefonecomercial AS prospects_telefonecomercial,  ");
        sqlStr.append("prospects.telefonerecado AS prospects_telefonerecado,  ");
        sqlStr.append("prospects.pessoa AS prospects_pessoa,  ");
        //Dados histórico
        sqlStr.append("hfu.observacao AS observacao,");
        sqlStr.append("hfu.dataRegistro dataRegistro,");
        sqlStr.append("hfu.codigo AS hfu_codigo, ");        
        sqlStr.append("hfu.responsavel AS hfu_responsavel, ");
        sqlStr.append("resphis.nome AS hfu_responsavel_nome, ");
        sqlStr.append("hfu.tipocontato as hfu_tipocontato, ");
        sqlStr.append("tipocontato.descricao as hfu_tipocontato_descricao, ");
        sqlStr.append("hfu.departamento as hfu_departamento, ");
        sqlStr.append("departamento.nome as hfu_departamento_nome ");

        sqlStr.append("FROM prospects ");
        sqlStr.append("LEFT JOIN arquivo ON prospects.arquivoFoto = arquivo.codigo ");
        sqlStr.append("LEFT JOIN historicoFollowUp AS hfu ON hfu.prospect = prospects.codigo ");        
        sqlStr.append("LEFT JOIN tipocontato ON hfu.tipocontato = tipocontato.codigo ");
        sqlStr.append("LEFT JOIN departamento ON hfu.departamento = departamento.codigo ");
        sqlStr.append("LEFT JOIN usuario resphis ON hfu.responsavel = resphis.codigo ");
        sqlStr.append("WHERE prospects.codigo = ").append(valorConsulta);
        sqlStr.append(" order by prospects.codigo, hfu.dataRegistro desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosFollowUp(valorConsulta, tabelaResultado, nivelMontarDados, usuario);
    }

    public FollowUpVO consultarFollowUpPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        StringBuilder sqlStr = new StringBuilder();
        //Dados prospect
        sqlStr.append("SELECT prospects.codigo AS prospects_codigo, pessoa.codigo AS prospects_pessoa, ");
        sqlStr.append("prospects.nome AS nome,");
        sqlStr.append("prospects.dataNascimento AS dataNascimento,");
        sqlStr.append("prospects.sexo AS sexo, ");
        sqlStr.append("prospects.unidadeEnsino AS prospects_unidadeEnsino, ");
        sqlStr.append("arquivo.pastaBaseArquivo AS pastaBaseArquivo,");
        sqlStr.append("arquivo.codigo AS codArquivo, arquivo.nome AS nomeArquivo, ");
        sqlStr.append("prospects.emailprincipal AS prospects_email,  ");
        sqlStr.append("prospects.telefoneresidencial AS prospects_telefoneresidencial ,  ");
        sqlStr.append("prospects.celular AS prospects_celular, ");
        sqlStr.append("prospects.telefonecomercial AS prospects_telefonecomercial,  ");
        sqlStr.append("prospects.telefonerecado AS prospects_telefonerecado,  ");
        //Dados histórico
        sqlStr.append("hfu.observacao AS observacao,");
        sqlStr.append("hfu.dataRegistro dataRegistro,");
        sqlStr.append("hfu.codigo AS hfu_codigo, ");
        sqlStr.append("hfu.responsavel AS hfu_responsavel, ");
        sqlStr.append("resphis.nome AS hfu_responsavel_nome, ");
        sqlStr.append("hfu.tipocontato as hfu_tipocontato, ");
        sqlStr.append("tipocontato.descricao as hfu_tipocontato_descricao, ");
        sqlStr.append("hfu.departamento as hfu_departamento, ");
        sqlStr.append("departamento.nome as hfu_departamento_nome ");

        sqlStr.append("FROM prospects ");
        sqlStr.append("LEFT JOIN arquivo ON prospects.arquivoFoto = arquivo.codigo ");
        sqlStr.append("LEFT JOIN historicoFollowUp AS hfu ON hfu.prospect = prospects.codigo ");
        sqlStr.append("LEFT JOIN tipocontato ON hfu.tipocontato = tipocontato.codigo ");
        sqlStr.append("LEFT JOIN departamento ON hfu.departamento = departamento.codigo ");
        sqlStr.append("LEFT JOIN usuario resphis ON hfu.responsavel = resphis.codigo ");
        sqlStr.append("LEFT JOIN pessoa ON pessoa.codigo = prospects.pessoa  ");
        sqlStr.append("LEFT JOIN pessoa AS pessoa2 ON pessoa2.codProspect = prospects.codigo  ");
        sqlStr.append("WHERE (prospects.pessoa = ").append(valorConsulta).append(" OR pessoa2.codigo = ").append(valorConsulta).append(") ");
        sqlStr.append(" order by prospects.pessoa, hfu.dataRegistro desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosFollowUp(valorConsulta, tabelaResultado, nivelMontarDados, usuario);
    }

    public static SqlRowSet montarCompromissoFollowUp(Integer valorConsulta) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT etapaWorkflow.codigo AS etapaWorkflow_codigo,");
        sqlStr.append("etapaWorkflow.nome AS etapaWorkflow_nome,");
        sqlStr.append("cap.codigo AS codigoCompromisso,");
        sqlStr.append("cap.hora AS horaCompromisso,");
        sqlStr.append("aph.codigo AS codigoAgendaPessoaHorario,");
        sqlStr.append("aph.dia AS diaCompromisso,");
        sqlStr.append("aph.mes AS mesCompromisso,");
        sqlStr.append("aph.ano AS anoCompromisso,");
        sqlStr.append("wf.codigo AS wf_codigo,");
        sqlStr.append("wf.nome AS wf_nome,");
        sqlStr.append("pessoa.codigo AS codigoColaborador, ");
        sqlStr.append("pessoa.nome AS nomeColaborador ");
        sqlStr.append("FROM prospects ");
        sqlStr.append("INNER JOIN compromissoagendapessoahorario AS cap ON cap.prospect = prospects.codigo ");
        sqlStr.append("INNER JOIN campanha ON campanha.codigo = cap.campanha ");
        sqlStr.append("INNER JOIN workflow AS wf ON wf.codigo = campanha.workflow ");
        sqlStr.append("INNER JOIN etapaWorkflow ON wf.codigo = etapaWorkflow.workflow ");
//        sqlStr.append("LEFT JOIN situacaoprospectworkflow ON situacaoprospectworkflow.codigo = etapaworkflow.situacaodefinirprospectfinal ");
//        sqlStr.append("LEFT JOIN situacaoprospectpipeline AS spp ON spp.codigo = situacaoprospectworkflow.situacaoprospectpipeline ");

        sqlStr.append("INNER JOIN agendapessoahorario AS aph ON aph.codigo = cap.agendapessoahorario ");
        sqlStr.append("INNER JOIN agendapessoa AS ap ON ap.codigo = aph.agendapessoa ");
        sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = ap.pessoa ");
        sqlStr.append("LEFT JOIN pessoa AS pessoa2 ON pessoa2.codProspect = prospects.codigo  ");
        sqlStr.append("WHERE ((cap.hora >= '").append(Uteis.getHoraAtual()).append("'");
        sqlStr.append(" AND aph.dia >= ").append(Uteis.getDiaMesData(new Date()));
        sqlStr.append(" AND aph.mes >= ").append(Uteis.getMesData(new Date()));
        sqlStr.append(" AND aph.ano >= ").append(Uteis.getAnoData(new Date()));
//        sqlStr.append(") OR  (aph.dia > ").append(Uteis.getDiaMesData(new Date()));
//        sqlStr.append(" AND aph.mes = ").append(Uteis.getMesData(new Date()));
//        sqlStr.append(" AND aph.ano = ").append(Uteis.getAnoData(new Date()));
//        sqlStr.append(") OR  ( aph.mes > ").append(Uteis.getMesData(new Date()));
//        sqlStr.append(" AND aph.ano = ").append(Uteis.getAnoData(new Date()));
//        sqlStr.append(") OR  aph.ano > ").append(Uteis.getAnoData(new Date()));
//        sqlStr.append(") AND compromissoRealizado = false AND spp.controle = 'INICIAL' ");
        sqlStr.append(")) AND tipoSituacaoCompromissoEnum = '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("' ");
        sqlStr.append(" AND  (prospects.pessoa = ").append(valorConsulta).append(" OR pessoa2.codigo = ").append(valorConsulta);
        sqlStr.append(" ) ORDER BY cap.hora LIMIT 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return tabelaResultado;
    }

    public static FollowUpVO montarDadosFollowUp(Integer valorConsulta, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FollowUpVO obj = new FollowUpVO();
        SqlRowSet dadosSQLCompromisso = montarCompromissoFollowUp(valorConsulta);
        if (dadosSQLCompromisso.next()) {
            obj.getCompromissoAgendaPessoaHorario().getCampanha().getWorkflow().setCodigo(dadosSQLCompromisso.getInt("wf_codigo"));
            obj.getCompromissoAgendaPessoaHorario().getCampanha().getWorkflow().setNome(dadosSQLCompromisso.getString("wf_nome"));
            obj.getCompromissoAgendaPessoaHorario().getEtapaWorkflowVO().setCodigo(dadosSQLCompromisso.getInt("etapaWorkflow_codigo"));
            obj.getCompromissoAgendaPessoaHorario().getEtapaWorkflowVO().setNome(dadosSQLCompromisso.getString("etapaWorkflow_nome"));
            obj.getCompromissoAgendaPessoaHorario().setCodigo(dadosSQLCompromisso.getInt("codigoCompromisso"));
            obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setCodigo(dadosSQLCompromisso.getInt("codigoColaborador"));
            obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().getAgendaPessoa().getPessoa().setNome(dadosSQLCompromisso.getString("nomeColaborador"));
            obj.getCompromissoAgendaPessoaHorario().setHora(dadosSQLCompromisso.getString("horaCompromisso"));
            obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().setCodigo(dadosSQLCompromisso.getInt("codigoAgendaPessoaHorario"));
            obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().setDia(dadosSQLCompromisso.getInt("diaCompromisso"));
            obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().setMes(dadosSQLCompromisso.getInt("mesCompromisso"));
            obj.getCompromissoAgendaPessoaHorario().getAgendaPessoaHorario().setAno(dadosSQLCompromisso.getInt("anoCompromisso"));

        }
        while (dadosSQL.next()) {
            if (obj.getCodigo() == 0) {
                obj.getProspect().setCodigo(new Integer(dadosSQL.getInt("prospects_codigo")));
                obj.getProspect().setNome(dadosSQL.getString("nome"));
                obj.getProspect().setDataNascimento(dadosSQL.getDate("dataNascimento"));
                obj.getProspect().setSexo(dadosSQL.getString("sexo"));
                obj.getProspect().setTelefoneResidencial(dadosSQL.getString("prospects_telefoneresidencial"));
                obj.getProspect().setTelefoneComercial(dadosSQL.getString("prospects_telefonecomercial"));
                obj.getProspect().setTelefoneRecado(dadosSQL.getString("prospects_telefonerecado"));
                obj.getProspect().setCelular(dadosSQL.getString("prospects_celular"));
                obj.getProspect().getUnidadeEnsino().setCodigo(dadosSQL.getInt("prospects_unidadeEnsino"));
                obj.getProspect().setEmailPrincipal(dadosSQL.getString("prospects_email"));
                obj.getProspect().getPessoa().setCodigo(dadosSQL.getInt("prospects_pessoa"));
                obj.getProspect().getArquivoFoto().setCodigo(dadosSQL.getInt("codArquivo"));
                obj.getProspect().getArquivoFoto().setNome(dadosSQL.getString("nomeArquivo"));
                obj.getProspect().getArquivoFoto().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
                obj.setNovoObj(Boolean.FALSE);
            }

            //Dados historico
            montarListaHistorico(dadosSQL, obj);

        }
        return obj;
    }

    public static void montarListaHistorico(SqlRowSet dadosSQL, FollowUpVO obj) {
        HistoricoFollowUpVO historicoVO = null;
        if (adicionarHistorico(obj.getHistoricoFollowUpVOs(), dadosSQL.getInt("hfu_codigo"))) {
            historicoVO = new HistoricoFollowUpVO();
            historicoVO.setCodigo((dadosSQL.getInt("hfu_codigo")));
            historicoVO.getProspect().setCodigo(dadosSQL.getInt("prospects_codigo"));
            historicoVO.getResponsavel().setCodigo(dadosSQL.getInt("hfu_responsavel"));
            historicoVO.getResponsavel().setNome(dadosSQL.getString("hfu_responsavel_nome"));
            historicoVO.setObservacao(dadosSQL.getString("observacao"));
            historicoVO.setDataregistro(dadosSQL.getTimestamp("dataRegistro"));
            historicoVO.getTipoContato().setCodigo(dadosSQL.getInt("hfu_tipocontato"));
            historicoVO.getTipoContato().setDescricao(dadosSQL.getString("hfu_tipocontato_descricao"));
            historicoVO.getDepartamento().setCodigo(dadosSQL.getInt("hfu_departamento"));
            historicoVO.getDepartamento().setNome(dadosSQL.getString("hfu_departamento_nome"));
            if (historicoVO.getCodigo() != null && historicoVO.getCodigo() != 0) {
                obj.getHistoricoFollowUpVOs().add(historicoVO);
            }
        }
    }

    public List<InteracaoWorkflowVO> montarDadosInteracoes(SqlRowSet dadosSQL, FollowUpVO obj) {
        while (dadosSQL.next()) {
            InteracaoWorkflowVO interacaoWorkflowVO = null;
            if (adicionarInteracao(obj.getInteracaoWorkflowVOs(), dadosSQL.getInt("iwf_codigo"))) {
                interacaoWorkflowVO = new InteracaoWorkflowVO();
                interacaoWorkflowVO.setCodigo((dadosSQL.getInt("iwf_codigo")));
                interacaoWorkflowVO.setDataInicio(dadosSQL.getDate("iwf_datainicio"));
                interacaoWorkflowVO.setHoraInicio(dadosSQL.getString("iwf_horainicio"));
                interacaoWorkflowVO.getProspect().setCodigo(dadosSQL.getInt("prospects_codigo"));
                interacaoWorkflowVO.setTipoInteracao(TipoInteracaoEnum.valueOf(dadosSQL.getString("iwf_tipointeracao")));
                interacaoWorkflowVO.setObservacao(dadosSQL.getString("iwf_observacao"));
                if (dadosSQL.getString("iwf_tipoOrigemInteracao") != null) {
                	interacaoWorkflowVO.setTipoOrigemInteracao(TipoOrigemInteracaoEnum.valueOf(dadosSQL.getString("iwf_tipoOrigemInteracao")));
                } else {
                	interacaoWorkflowVO.setTipoOrigemInteracao(TipoOrigemInteracaoEnum.NENHUM);
                }
                interacaoWorkflowVO.setIdentificadorOrigem(dadosSQL.getString("iwf_identificadorOrigem"));
                if(dadosSQL.getObject("iwf_codigoEntidadeOrigem") != null) {
                	interacaoWorkflowVO.setCodigoEntidadeOrigem(dadosSQL.getInt("iwf_codigoEntidadeOrigem"));
                }
                interacaoWorkflowVO.getWorkflow().setCodigo(dadosSQL.getInt("wfinteracao_codigo"));
                interacaoWorkflowVO.getWorkflow().setNome(dadosSQL.getString("wfinteracao_nome"));
                interacaoWorkflowVO.getCampanha().setCodigo(dadosSQL.getInt("campanhainteracao_codigo"));
                interacaoWorkflowVO.getCampanha().setDescricao(dadosSQL.getString("campanhainteracao_descricao"));
                interacaoWorkflowVO.getEtapaWorkflow().setCodigo(dadosSQL.getInt("ewfinteracao_codigo"));
                interacaoWorkflowVO.getEtapaWorkflow().setNome(dadosSQL.getString("ewfinteracao_nome"));

                interacaoWorkflowVO.getCurso().setCodigo(dadosSQL.getInt("cursointeracao_codigo"));
                interacaoWorkflowVO.getCurso().setNome(dadosSQL.getString("cursointeracao_nome"));
                interacaoWorkflowVO.getUnidadeEnsino().setCodigo(dadosSQL.getInt("uEinteracao_codigo"));
                interacaoWorkflowVO.getUnidadeEnsino().setNome(dadosSQL.getString("uEinteracao_nome"));
                interacaoWorkflowVO.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setCodigo(dadosSQL.getInt("sppinteracao_codigo"));
                interacaoWorkflowVO.getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(dadosSQL.getString("sppinteracao_nome"));
                
                interacaoWorkflowVO.getResponsavel().setCodigo(dadosSQL.getInt("usuario_codigo"));
                interacaoWorkflowVO.getResponsavel().setNome(dadosSQL.getString("usuario_nome"));

                interacaoWorkflowVO.getResponsavel().getPessoa().setCodigo(dadosSQL.getInt("pessoainteracao_codigo"));
                interacaoWorkflowVO.getResponsavel().getPessoa().setNome(dadosSQL.getString("pessoainteracao_nome"));
                if (interacaoWorkflowVO.getCodigo() != null && interacaoWorkflowVO.getCodigo() != 0) {
                    obj.getInteracaoWorkflowVOs().add(interacaoWorkflowVO);
                }
            }
        }
        return obj.getInteracaoWorkflowVOs();
    }

    public List montarWorkflowCombobox(SqlRowSet dadosSQL) {
        List objs = new ArrayList(0);
        while (dadosSQL.next()) {
            WorkflowVO workflowVO = new WorkflowVO();
            workflowVO.setCodigo(dadosSQL.getInt("workflow_codigo"));
            workflowVO.setNome(dadosSQL.getString("workflow_nome"));
            objs.add(workflowVO);
        }
        return objs;
    }

    public List montarCampanhaCombobox(SqlRowSet dadosSQL) {
        List objs = new ArrayList(0);
        while (dadosSQL.next()) {
            CampanhaVO campanhaVO = new CampanhaVO();
            campanhaVO.setCodigo(dadosSQL.getInt("campanha_codigo"));
            campanhaVO.setDescricao(dadosSQL.getString("campanha_descricao"));
            objs.add(campanhaVO);
        }
        return objs;
    }

    public List montarResponsavelCombobox(SqlRowSet dadosSQL) {
        List objs = new ArrayList(0);

        while (dadosSQL.next()) {
            UsuarioVO usuarioVO = new UsuarioVO();
            usuarioVO.setCodigo(dadosSQL.getInt("usuario_codigo"));
            usuarioVO.getPessoa().setCodigo(dadosSQL.getInt("pessoa_codigo"));
            usuarioVO.getPessoa().setNome(dadosSQL.getString("pessoa_nome"));
            objs.add(usuarioVO);
        }
        return objs;
    }

    public List montarCursoCombobox(SqlRowSet dadosSQL) {
        List objs = new ArrayList(0);
        while (dadosSQL.next()) {
            CursoVO cursoVO = new CursoVO();
            cursoVO.setCodigo(dadosSQL.getInt("curso_codigo"));
            cursoVO.setDescricao(dadosSQL.getString("curso_descricao"));
            objs.add(cursoVO);
        }
        return objs;
    }

    private static boolean adicionarHistorico(List<HistoricoFollowUpVO> lista, Integer codigo) {
        for (HistoricoFollowUpVO historico : lista) {
            if (historico.getCodigo().equals(codigo)) {
                return false;
            }
        }
        return true;
    }

    private static boolean adicionarInteracao(List<InteracaoWorkflowVO> lista, Integer codigo) {
        for (InteracaoWorkflowVO interacao : lista) {
            if (interacao.getCodigo().equals(codigo)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>HistoricoFollowUpVO</code>
     * ao List <code>historicoFollowUpVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>HistoricoFollowUp</code> - getObservacao() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>HistoricoFollowUpVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjHistoricoFollowUpVOs(FollowUpVO objFollowUpVO, HistoricoFollowUpVO obj) throws Exception {
        getFacadeFactory().getHistoricoFollowUpFacade().validarDados(obj);
        obj.setProspect(objFollowUpVO.getProspect());
        if(obj.getTipoContato().getCodigo() >0){
        	obj.setTipoContato(getFacadeFactory().getTipoContatoFacade().consultarPorChavePrimaria(obj.getTipoContato().getCodigo()));
        }
        if(Uteis.isAtributoPreenchido(obj.getDepartamento())){
        	obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(),false, Uteis.NIVELMONTARDADOS_COMBOBOX, obj.getResponsavel()));
        }
        int index = 0;
        for (HistoricoFollowUpVO objExistente : objFollowUpVO.getHistoricoFollowUpVOs()) {
            if (objExistente.getObservacao().equals(obj.getObservacao())) {
                objFollowUpVO.getHistoricoFollowUpVOs().set(index, obj);
                return;
            }
            index++;
        }
        objFollowUpVO.getHistoricoFollowUpVOs().add(obj);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirObjHistoricoFollowUpVOs(FollowUpVO objFollowUpVO, HistoricoFollowUpVO historicoFollowUpVO, UsuarioVO usuarioVO) throws Exception {
        int index = 0;
        for (HistoricoFollowUpVO objExistente : objFollowUpVO.getHistoricoFollowUpVOs()) {
            if (objExistente.getCodigo().equals(historicoFollowUpVO.getCodigo())) {
                objFollowUpVO.getHistoricoFollowUpVOs().remove(index);
                getFacadeFactory().getHistoricoFollowUpFacade().excluir(historicoFollowUpVO, usuarioVO);
                return;
            }
            index++;
        }

    }

    /**
     * Operação responsável por consultar um objeto da classe <code>HistoricoFollowUpVO</code>
     * no List <code>historicoFollowUpVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>HistoricoFollowUp</code> - getObservacao() - como identificador (key) do objeto no List.
     * @param observacao  Parâmetro para localizar o objeto do List.
     */
    public HistoricoFollowUpVO consultarObjHistoricoFollowUpVO(FollowUpVO objFollowUpVO, String observacao) throws Exception {
        for (HistoricoFollowUpVO objExistente : objFollowUpVO.getHistoricoFollowUpVOs()) {
            if (objExistente.getObservacao().equals(observacao)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por adicionar um objeto da <code>HistoricoFollowUpVO</code> no Hashtable <code>HistoricoFollowUps</code>.
     * Neste Hashtable são mantidos todos os objetos de HistoricoFollowUp de uma determinada FollowUp.
     * @param obj  Objeto a ser adicionado no Hashtable.
     */
    public void adicionarObjHistoricoFollowUps(HistoricoFollowUpVO obj) throws Exception {
        getHistoricoFollowUps().put(obj.getObservacao() + "", obj);
        //adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por remover um objeto da classe <code>HistoricoFollowUpVO</code> do Hashtable <code>HistoricoFollowUps</code>.
     * Neste Hashtable são mantidos todos os objetos de HistoricoFollowUp de uma determinada FollowUp.
     * @param Observacao Atributo da classe <code>HistoricoFollowUpVO</code> utilizado como apelido (key) no Hashtable.
     */
    public void excluirObjHistoricoFollowUps(String Observacao) throws Exception {
        getHistoricoFollowUps().remove(Observacao + "");
        //excluirObjSubordinadoOC
    }

    public AgendaPessoaVO realizarValidacaoAgendaFollowUp(PessoaVO pessoaVO, UsuarioVO usuario) throws Exception {
        AgendaPessoaVO agenda = getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(pessoaVO, usuario);
        if (agenda.getCodigo() == 0) {
            agenda.setPessoa(pessoaVO);
            getFacadeFactory().getAgendaPessoaFacade().persistir(agenda, usuario);
        }
        return agenda;
    }

    public AgendaPessoaHorarioVO realizarValidacaoAgendaPessoaExiste(AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CompromissoAgendaPessoaHorarioVO compromisso, UsuarioVO usuario) throws Exception {
        agendaPessoaHorario = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarPorDiaMesAnoPorAgendaPessoa(0, agenda.getPessoa().getCodigo(), 0, compromisso.getDataCompromisso(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuario);
        if (agendaPessoaHorario.getCodigo() == 0) {
            agendaPessoaHorario = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(compromisso.getDataCompromisso()), Uteis.getMesData(compromisso.getDataCompromisso()), Uteis.getAnoData(compromisso.getDataCompromisso()), Uteis.getDiaSemanaEnum(compromisso.getDataCompromisso()), true);
            agendaPessoaHorario.setAgendaPessoa(agenda);
            agenda.getAgendaPessoaHorarioVOs().add(agendaPessoaHorario);
            getFacadeFactory().getAgendaPessoaHorarioFacade().incluirAgendaPessoaHorarios(agenda.getCodigo(), agenda.getAgendaPessoaHorarioVOs(), usuario);
        }
        return agendaPessoaHorario;
    }

    public Hashtable getHistoricoFollowUps() {
        if (historicoFollowUps == null) {
            historicoFollowUps = new Hashtable(0);
        }
        return (historicoFollowUps);
    }

    public void setHistoricoFollowUps(Hashtable historicoFollowUps) {
        this.historicoFollowUps = historicoFollowUps;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return FollowUp.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        FollowUp.idEntidade = idEntidade;
    }

    
    public Integer consultarCodigoProspectPorCodigoPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT prospects.codigo AS prospects_codigo ");
        sqlStr.append(" FROM prospects ");
        sqlStr.append(" LEFT JOIN pessoa ON pessoa.codigo = prospects.pessoa ");
        sqlStr.append(" LEFT JOIN pessoa AS pessoa2 ON pessoa2.codProspect = prospects.codigo ");
        sqlStr.append(" WHERE prospects.pessoa = ").append(pessoa).append(" OR pessoa2.codigo = ").append(pessoa);
        sqlStr.append(" LIMIT 1 ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (rs.next()) {
        	return rs.getInt("prospects_codigo");
        }
        return 0;
    }
    /**
     * @author Leonardo Riciolle 10/02/2014
     * Consultar criada especificamente para a tela de FollowUp, aba "Familiares"
     * onde ela retorna os parentes que já estudaram na instituição, vairando entre pais,irmãos ou responsavel legal.
     */
    @Override
	public List<FamiliaresVO> consultarFamilires(FollowUpVO followUpVO) throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DISTINCT familiar.codigo as \"familiarCodigo\",matricula.matricula as \"matricula\",matricula.data as \"matriculaData\",familiar.nome as \"familiarNome\",unidadeEnsino.nome as \"unidadeEnsino\", ");
		sb.append(" curso.nome as \"cursoNome\",filiacao.tipo as \"tipoParentesco\",matricula.situacao as \"matriculaSituacao\" ");
		sb.append(" FROM pessoa ");
		sb.append(" inner join filiacao on filiacao.aluno = pessoa.codigo ");
		sb.append(" inner join pessoa as familiar on familiar.codigo = filiacao.pais ");
		sb.append(" inner join matricula on matricula.aluno = familiar.codigo ");
		sb.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sb.append(" inner join curso on matricula.curso = curso.codigo ");
		sb.append(" where  pessoa.codigo = " + followUpVO.getProspect().getPessoa().getCodigo() + " ");
		sb.append(" UNION ");
		sb.append("  SELECT DISTINCT familiar.codigo as \"familiarCodigo\",matricula.matricula as \"matricula\",matricula.data as \"matriculaData\",familiar.nome as \"familiarNome\",unidadeEnsino.nome as \"unidadeEnsino\", ");
		sb.append(" curso.nome as \"cursoNome\",	'IR' as \"tipoParentesco\",	matricula.situacao as \"matriculaSituacao\" ");
		sb.append(" FROM pessoa ");
		sb.append(" inner join filiacao on filiacao.aluno = pessoa.codigo ");
		sb.append(" inner join filiacao as filiacao2 on filiacao2.pais = filiacao.pais and filiacao2.aluno != pessoa.codigo ");
		sb.append(" inner join pessoa as familiar on familiar.codigo = filiacao2.aluno ");
		sb.append(" inner join matricula on matricula.aluno = familiar.codigo ");
		sb.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sb.append(" inner join curso on matricula.curso = curso.codigo  ");
		sb.append(" where  pessoa.codigo = " + followUpVO.getProspect().getPessoa().getCodigo() + " ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<FamiliaresVO> listaFamiliaresVOs = new ArrayList<FamiliaresVO>(0);
		while (tabelaResultado.next()) {
			FamiliaresVO obj = new FamiliaresVO();
			obj.getFamiliar().setCodigo(tabelaResultado.getInt("familiarCodigo"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaVO().setData(tabelaResultado.getDate("matriculaData"));
			obj.getFamiliar().setNome(tabelaResultado.getString("familiarNome"));
			obj.getMatriculaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino"));
			obj.getMatriculaVO().getCurso().setNome(tabelaResultado.getString("cursoNome"));
			obj.getFiliacaoVO().setTipo(tabelaResultado.getString("tipoParentesco"));
			obj.getMatriculaVO().setSituacao(tabelaResultado.getString("matriculaSituacao"));
			listaFamiliaresVOs.add(obj);
		}
		return listaFamiliaresVOs;
    }
    
    @Override
	public List<InteracaoWorkflowHistoricoVO> consultarInteracaoWorkflowPorAlunoFichaAluno(Integer pessoa, Integer responsavel, String mesAno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select interacaoworkflow.codigo AS \"interacaoworkflow.codigo\", interacaoworkflow.horainicio AS \"interacaoworkflow.horainicio\", interacaoworkflow.horatermino AS \"interacaoworkflow.horatermino\", interacaoworkflow.observacao AS \"interacaoworkflow.observacao\", interacaoworkflow.dataInicio AS \"interacaoworkflow.dataInicio\", ");
		sb.append(" etapaworkflow.nome AS \"etapaworkflow.nome\", situacaoprospectpipeline.nome AS \"situacaoprospectpipeline.nome\", campanha.tipocampanha AS \"campanha.tipocampanha\", campanha.descricao AS \"campanha.descricao\", ");
		sb.append(" workflow.nome AS \"workflow.nome\", curso.nome AS \"curso.nome\", ");
		sb.append(" responsavel.codigo AS \"responsavel.codigo\", pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
		sb.append(" from interacaoworkflow ");
		sb.append(" INNER JOIN prospects ON prospects.codigo = interacaoworkflow.prospect ");
		sb.append(" left join campanha on campanha.codigo = interacaoworkflow.campanha ");
		sb.append(" left join workflow on workflow.codigo = interacaoworkflow.workflow ");
		sb.append(" left join curso on curso.codigo = interacaoworkflow.curso ");
		sb.append(" left join etapaworkflow on etapaworkflow.codigo = interacaoworkflow.etapaworkflow ");
		sb.append(" left join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = etapaworkflow.situacaodefinirprospectfinal ");
		sb.append(" left join situacaoprospectpipeline ON situacaoprospectpipeline.codigo = situacaoprospectworkflow.situacaoprospectpipeline ");
		sb.append(" left join usuario responsavel on responsavel.codigo = interacaoworkflow.responsavel ");
		sb.append(" left join pessoa on pessoa.codigo = responsavel.pessoa ");
		sb.append(" where prospects.pessoa = ").append(pessoa);
		if (mesAno != null && !mesAno.equals("")) {
			sb.append(" and extract(month from interacaoworkflow.datainicio) = ").append(getMesDataInteracao(mesAno));
			sb.append(" and extract(year from interacaoworkflow.datainicio) = ").append(getAnoDataInteracao(mesAno));
		}
		if (responsavel != null && !responsavel.equals(0)) {
			sb.append(" and interacaoworkflow.responsavel = ").append(responsavel);
		}
		sb.append(" order by interacaoworkflow.datainicio ");
		List<InteracaoWorkflowHistoricoVO> listaInteracaoWorkflowHistoricoVOs = new ArrayList<InteracaoWorkflowHistoricoVO>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			InteracaoWorkflowHistoricoVO obj = new InteracaoWorkflowHistoricoVO();
			obj.getInteracaoWorkflowVO().setCodigo(tabelaResultado.getInt("interacaoworkflow.codigo"));
			obj.getInteracaoWorkflowVO().setDataInicio(tabelaResultado.getDate("interacaoworkflow.dataInicio"));
			obj.getInteracaoWorkflowVO().setHoraInicio(tabelaResultado.getString("interacaoworkflow.horainicio"));
			obj.getInteracaoWorkflowVO().setHoraTermino(tabelaResultado.getString("interacaoworkflow.horatermino"));
			obj.getInteracaoWorkflowVO().setObservacao(tabelaResultado.getString("interacaoworkflow.observacao"));
			obj.getInteracaoWorkflowVO().getEtapaWorkflow().setNome(tabelaResultado.getString("etapaworkflow.nome"));
			obj.getInteracaoWorkflowVO().getEtapaWorkflow().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().setNome(tabelaResultado.getString("situacaoprospectpipeline.nome"));
			if (tabelaResultado.getString("campanha.tipocampanha") != null) {
				obj.getInteracaoWorkflowVO().getCampanha().setTipoCampanha(TipoCampanhaEnum.valueOf(tabelaResultado.getString("campanha.tipocampanha")));
			}
			obj.getInteracaoWorkflowVO().getCampanha().setDescricao(tabelaResultado.getString("campanha.descricao"));
			obj.getInteracaoWorkflowVO().getWorkflow().setNome(tabelaResultado.getString("workflow.nome"));
			obj.getInteracaoWorkflowVO().getCurso().setNome(tabelaResultado.getString("curso.nome"));
			obj.getInteracaoWorkflowVO().getResponsavel().setCodigo(tabelaResultado.getInt("responsavel.codigo"));
			obj.getInteracaoWorkflowVO().getResponsavel().getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getInteracaoWorkflowVO().getResponsavel().getPessoa().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.setTipoFollowUp(TipoFollowUpEnum.INTERACAO_WORKFLOW);
			listaInteracaoWorkflowHistoricoVOs.add(obj);
		}
		return listaInteracaoWorkflowHistoricoVOs;
	}
	
	public Integer getMesDataInteracao(String mesAno) {
		if (mesAno != null && !mesAno.equals("")) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			return Uteis.getMesConcatenadoReferencia(mes);
		}
		return 0;
	}
	
	public Integer getAnoDataInteracao(String mesAno) {
		if (mesAno != null && !mesAno.equals("")) {
			String ano = mesAno.substring(mesAno.indexOf("/") + 1);
			return Integer.parseInt(ano);
		}
		return 0;
	}
	
	@Override
	public List<InteracaoWorkflowHistoricoVO> consultarHistoricoFollowUpPorAlunoFichaAluno(Integer pessoa, Integer responsavel, String mesAno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select historicofollowup.codigo, historicofollowup.dataregistro, historicofollowup.observacao,  ");
		sb.append(" tipocontato.codigo AS \"tipocontato.codigo\", tipocontato.descricao AS \"tipocontato.descricao\", ");
		sb.append(" usuario.codigo AS \"usuario.codigo\", pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
		sb.append(" from historicofollowup  ");
		sb.append(" inner join prospects on prospects.codigo = historicofollowup.prospect ");
		sb.append(" left join tipocontato on tipocontato.codigo = historicofollowup.tipocontato ");
		sb.append(" left join usuario on usuario.codigo = historicofollowup.responsavel ");
		sb.append(" left join pessoa on pessoa.codigo = usuario.pessoa ");
		sb.append(" where prospects.pessoa = ").append(pessoa);
		if (mesAno != null && !mesAno.equals("")) {
			sb.append(" and extract(month from historicofollowup.dataRegistro) = ").append(getMesDataInteracao(mesAno));
			sb.append(" and extract(year from historicofollowup.dataRegistro) = ").append(getAnoDataInteracao(mesAno));
		}
		if (responsavel != null && !responsavel.equals(0)) {
			sb.append(" and historicofollowup.responsavel = ").append(responsavel);
		}
		sb.append(" order by dataregistro ");
		List<InteracaoWorkflowHistoricoVO> listaInteracaoWorkflowHistoricoVOs = new ArrayList<InteracaoWorkflowHistoricoVO>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			InteracaoWorkflowHistoricoVO obj = new InteracaoWorkflowHistoricoVO();
			obj.getHistoricoFollowUpVO().setCodigo(tabelaResultado.getInt("codigo"));
			obj.getHistoricoFollowUpVO().setDataregistro(tabelaResultado.getDate("dataRegistro"));
			obj.getHistoricoFollowUpVO().setObservacao(tabelaResultado.getString("observacao"));
			obj.getHistoricoFollowUpVO().getTipoContato().setCodigo(tabelaResultado.getInt("tipoContato.codigo"));
			obj.getHistoricoFollowUpVO().getTipoContato().setDescricao(tabelaResultado.getString("tipoContato.descricao"));
			obj.getHistoricoFollowUpVO().getResponsavel().setCodigo(tabelaResultado.getInt("usuario.codigo"));
			obj.getHistoricoFollowUpVO().getResponsavel().getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getHistoricoFollowUpVO().getResponsavel().getPessoa().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.setTipoFollowUp(TipoFollowUpEnum.HISTORICO);
			listaInteracaoWorkflowHistoricoVOs.add(obj);
		}
		return listaInteracaoWorkflowHistoricoVOs;
	}
	
	@Override
	public List<SelectItem> consultarMesAnoInteracaoWorkflowPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select mes ||'/'|| ano AS mesAno from (");
		sb.append(" select distinct case ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 1 then 'JAN' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 2 then 'FEV' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 3 then 'MAR' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 4 then 'ABR' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 5 then 'MAI' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 6 then 'JUN' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 7 then 'JUL' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 8 then 'AGO' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 9 then 'SET' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 10 then 'OUT' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 11 then 'NOV' ");
		sb.append(" when extract(month from interacaoworkflow.dataInicio) = 12 then 'DEZ' ");
		sb.append(" end AS mes, extract(year from interacaoworkflow.dataInicio) AS ano, interacaoworkflow.dataInicio ");
		sb.append(" from interacaoworkflow ");
		sb.append(" INNER JOIN prospects ON prospects.codigo = interacaoworkflow.prospect   ");
		sb.append(" where prospects.pessoa = ").append(aluno);
		sb.append(" order by interacaoworkflow.dataInicio desc) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		HashMap<String, String> mapMesAnoVOs = new HashMap<String, String>(0);
		List<SelectItem> listaSelectItemMesAnoItemEmprestimoVOs = new ArrayList<SelectItem>(0);
		listaSelectItemMesAnoItemEmprestimoVOs.add(new SelectItem("", ""));
		while (tabelaResultado.next()) {
			if (!mapMesAnoVOs.containsKey(tabelaResultado.getString("mesAno"))) {
				listaSelectItemMesAnoItemEmprestimoVOs.add(new SelectItem(tabelaResultado.getString("mesAno"), tabelaResultado.getString("mesAno")));
				mapMesAnoVOs.put(tabelaResultado.getString("mesAno"), tabelaResultado.getString("mesAno"));
			}
		}
		return listaSelectItemMesAnoItemEmprestimoVOs;
	}
	
	private void getFiltroPadraoConsultaInteracoes(Integer codigoWorkflow, Integer codigoCampanha, Integer codigoResponsavel, Integer codigoCurso, TipoOrigemInteracaoEnum tipoOrigem, String identificadorOrigem,
			Integer codigoEntidadeOrigem, int prospescts, String matricula, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(prospescts)) {
            sqlStr.append("AND prospects.codigo = ").append(prospescts).append(" ");
        }
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append("AND iwf.matricula = '").append(matricula).append("' ");
		}
        if ((tipoOrigem != null) && (tipoOrigem != TipoOrigemInteracaoEnum.NENHUM)) {
            sqlStr.append("AND iwf.tipoOrigemInteracao = '").append(tipoOrigem.toString()).append("' ");
        }
        if ((identificadorOrigem != null) && (!identificadorOrigem.equals(""))) {
            sqlStr.append("AND iwf.identificadorOrigem = '").append(identificadorOrigem).append("' ");
        }
        if ((codigoEntidadeOrigem != null) && (codigoEntidadeOrigem.intValue() != 0)) {
            sqlStr.append("AND iwf.codigoEntidadeOrigem = ").append(codigoEntidadeOrigem).append(" ");
        }
        if (codigoWorkflow != 0) {
            sqlStr.append("AND  iwf.workflow = ").append(codigoWorkflow).append(" ");
        }
        if (codigoCampanha != 0) {
            sqlStr.append("AND  iwf.campanha = ").append(codigoCampanha).append(" ");
        }
        if (codigoResponsavel != 0) {
            sqlStr.append("AND  iwf.responsavel = ").append(codigoResponsavel).append(" ");
        }
        if (codigoCurso != 0) {
            sqlStr.append("AND  iwf.curso = ").append(codigoCurso).append(" ");
        }
	}

	private void getSQLPadraoConsultaInteracoes(StringBuilder sqlStr) {
		sqlStr.append(" SELECT DISTINCT iwf.codigo AS iwf_codigo,");
        sqlStr.append(" iwf.horainicio AS iwf_horainicio, ");
        sqlStr.append(" iwf.datainicio AS iwf_datainicio,");
        sqlStr.append(" iwf.tipointeracao AS iwf_tipointeracao, ");
        sqlStr.append(" iwf.observacao AS iwf_observacao,");
        sqlStr.append(" wfInteracao.codigo AS wfInteracao_codigo,");
        sqlStr.append(" wfInteracao.nome AS wfInteracao_nome,");
        
        sqlStr.append(" iwf.tipoOrigemInteracao AS iwf_tipoOrigemInteracao,");
        sqlStr.append(" iwf.identificadorOrigem AS iwf_identificadorOrigem,");
        sqlStr.append(" iwf.codigoEntidadeOrigem AS iwf_codigoEntidadeOrigem,");
        
        sqlStr.append(" campanhaInteracao.codigo AS campanhaInteracao_codigo,");
        sqlStr.append(" campanhaInteracao.descricao AS campanhaInteracao_descricao,");
        sqlStr.append(" ewfInteracao.codigo AS ewfInteracao_codigo,");
        sqlStr.append(" ewfInteracao.nome AS ewfInteracao_nome,");
        sqlStr.append(" cursoInteracao.codigo AS cursoInteracao_codigo, ");
        sqlStr.append(" cursoInteracao.nome AS cursoInteracao_nome, ");
        sqlStr.append(" uEInteracao.codigo AS uEInteracao_codigo, ");
        sqlStr.append(" uEInteracao.nome AS uEInteracao_nome, ");
        sqlStr.append(" pessoaInteracao.codigo AS pessoaInteracao_codigo,");
        sqlStr.append(" pessoaInteracao.nome AS pessoaInteracao_nome,");
        sqlStr.append(" sppInteracao.codigo AS sppInteracao_codigo, ");
        
        sqlStr.append(" usuario.codigo AS usuario_codigo, ");
        sqlStr.append(" usuario.nome AS usuario_nome, ");
        sqlStr.append(" prospects.codigo AS prospects_codigo, ");
        
        sqlStr.append(" sppInteracao.nome AS sppInteracao_nome ");
        sqlStr.append(" FROM InteracaoWorkflow AS iwf ");
        sqlStr.append(" LEFT JOIN prospects ON prospects.codigo = iwf.prospect ");
        sqlStr.append(" LEFT JOIN workflow AS wfInteracao ON wfInteracao.codigo = iwf.workflow ");
        sqlStr.append(" LEFT JOIN campanha AS campanhaInteracao ON iwf.campanha = campanhaInteracao.codigo ");
        sqlStr.append(" LEFT JOIN etapaWorkflow AS ewfInteracao ON iwf.etapaWorkflow = ewfInteracao.codigo ");
        sqlStr.append(" left join situacaoprospectworkflow ON situacaoprospectworkflow.codigo = ewfInteracao.situacaodefinirprospectfinal ");
        sqlStr.append(" left join situacaoprospectpipeline AS sppInteracao ON sppInteracao.codigo = situacaoprospectworkflow.situacaoprospectpipeline  ");
        sqlStr.append(" LEFT JOIN curso AS cursoInteracao ON iwf.curso = cursoInteracao.codigo ");
        sqlStr.append(" LEFT JOIN unidadeEnsino AS uEInteracao ON iwf.unidadeEnsino = uEInteracao.codigo ");
        sqlStr.append(" LEFT JOIN usuario ON iwf.responsavel = usuario.codigo ");
        sqlStr.append(" left JOIN pessoa AS pessoaInteracao ON usuario.pessoa = pessoaInteracao.codigo ");
        sqlStr.append("WHERE 1 = 1 ");
	}
	
	public boolean verificarExistenciaInteracaoWorkflowPorProspectsMatricula(Integer workflow, Integer campanha, Integer responsavel, Integer curso, TipoOrigemInteracaoEnum tipoOrigem, String identificadorOrigem, Integer codigoEntidadeOrigem, Integer prospects, String matricula) throws Exception {
		StringBuilder str = new StringBuilder("SELECT iwf_codigo FROM (");
		getSQLPadraoConsultaInteracoes(str);
		getFiltroPadraoConsultaInteracoes(workflow, campanha, responsavel, curso, tipoOrigem, identificadorOrigem, codigoEntidadeOrigem, prospects, "", str);
		str.append(" UNION ");
		getSQLPadraoConsultaInteracoes(str);
		getFiltroPadraoConsultaInteracoes(workflow, campanha, responsavel, curso, tipoOrigem, identificadorOrigem, codigoEntidadeOrigem, 0, matricula, str);
		str.append(" ) AS T ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return tabelaResultado.next() && Uteis.isAtributoPreenchido(tabelaResultado.getInt("iwf_codigo"));
	}
}
