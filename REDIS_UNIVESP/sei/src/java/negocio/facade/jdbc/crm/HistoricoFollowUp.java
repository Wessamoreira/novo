package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.FollowUpVO;
import negocio.comuns.crm.HistoricoFollowUpVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.TipoContatoVO;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboHistoricoFollowUpEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.interfaces.crm.HistoricoFollowUpInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>HistoricoFollowUpVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>HistoricoFollowUpVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see HistoricoFollowUpVO
 * @see SuperEntidade
 * @see FollowUp
 */
@Repository
@Scope("singleton")
@Lazy
public class HistoricoFollowUp extends ControleAcesso implements HistoricoFollowUpInterfaceFacade {

    protected static String idEntidade;

    public HistoricoFollowUp() throws Exception {
        super();
        setIdEntidade("FollowUp");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>HistoricoFollowUpVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>HistoricoFollowUpVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final HistoricoFollowUpVO obj, UsuarioVO usuarioVO) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        final String sql = "INSERT INTO HistoricoFollowUp(prospect, observacao, dataregistro, responsavel, departamento, tipoContato ) VALUES (?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getProspect().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getProspect().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getObservacao());
                sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataregistro()));
                if (obj.getResponsavel().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(4, obj.getResponsavel().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(4, 0);
                }
                if (obj.getDepartamento().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(5, obj.getDepartamento().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(5, 0);
                }
                if (obj.getTipoContato().getCodigo().intValue() != 0) {
                	sqlInserir.setInt(6, obj.getTipoContato().getCodigo().intValue());
                } else {
                	sqlInserir.setNull(6, 0);
                }
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
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(final HistoricoFollowUpVO obj, UsuarioVO usuarioVO) throws Exception {
        getConexao().getJdbcTemplate().update("DELETE FROM HistoricoFollowUp where codigo = ?"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), obj.getCodigo());        
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final HistoricoFollowUpVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE HistoricoFollowUp SET prospect = ?, observacao = ?, dataregistro = ?, responsavel = ?, departamento=?, tipoContato = ? WHERE codigo = ?" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getProspect().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getProspect().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setString(2, obj.getObservacao());
                    sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataregistro()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    if (obj.getDepartamento().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getDepartamento().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    if (obj.getTipoContato().getCodigo().intValue() != 0) {
                    	sqlAlterar.setInt(6, obj.getTipoContato().getCodigo().intValue());
                    } else {
                    	sqlAlterar.setNull(6, 0);
                    }
                    sqlAlterar.setInt(7, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarProspectHistoricoFollowUp(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE HistoricoFollowUp set prospect=? WHERE ((prospect = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, codProspectManter);
                    sqlAlterar.setInt(2, codProspectRemover);
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>HistoricoFollowUpVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>HistoricoFollowUpVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(HistoricoFollowUpVO obj) throws Exception {
        try {
            HistoricoFollowUp.excluir(getIdEntidade());
            String sql = "DELETE FROM HistoricoFollowUp WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public void excluirTodosHistoricosFollowUps(Integer prospect, UsuarioVO usuario) throws Exception {
        try {
            HistoricoFollowUp.excluir(getIdEntidade());
            String sql = "DELETE FROM HistoricoFollowUp WHERE ((prospect = " + prospect + "))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>HistoricoFollowUpVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(HistoricoFollowUpVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getObservacao().equals("")) {
            throw new ConsistirException("O campo observação deve ser informado para adicionar um histórico");
        }
        if (!Uteis.isAtributoPreenchido(obj.getProspect())) {
            throw new ConsistirException("O campo PROSPECT/ALUNO deve ser informado para adicionar um histórico");
        }
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>HistoricoFollowUpVO</code>.
     */
    public void validarUnicidade(List<HistoricoFollowUpVO> lista, HistoricoFollowUpVO obj) throws ConsistirException {
        for (HistoricoFollowUpVO repetido : lista) {
        }
    }

    public List<HistoricoFollowUpVO> consultarTodosHistoricosFollowUps(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT hfu.observacao AS observacao, hfu.dataRegistro dataRegistro, hfu.codigo AS hfu_codigo, usuario.nome AS nomeResponsavel, departamento.nome as nomeDepartamento, departamento.codigo as codigoDepartamento, ");
        sqlStr.append(" tipoContato.codigo as codigoTipoContato, tipoContato.descricao as descricaoTipoContato ");
        sqlStr.append("FROM HistoricoFollowUp AS hfu ");
        sqlStr.append("LEFT JOIN prospects  ON hfu.prospect = prospects.codigo ");
        sqlStr.append("LEFT JOIN usuario ON usuario.codigo = hfu.responsavel ");
        sqlStr.append("LEFT JOIN departamento ON departamento.codigo = hfu.departamento ");
        sqlStr.append("LEFT JOIN tipoContato ON tipoContato.codigo = hfu.tipoContato ");
        sqlStr.append("WHERE prospects.codigo = ").append(valorConsulta);
        sqlStr.append(" ORDER BY hfu.dataRegistro desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarListaHistorico(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<HistoricoFollowUpVO> montarListaHistorico(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        HistoricoFollowUpVO historicoVO = null;
        FollowUpVO obj = new FollowUpVO();
        while (dadosSQL.next()) {
            if (adicionarHistorico(obj.getHistoricoFollowUpVOs(), dadosSQL.getInt("hfu_codigo"))) {
                historicoVO = new HistoricoFollowUpVO();
                historicoVO.setCodigo((dadosSQL.getInt("hfu_codigo")));
                historicoVO.setObservacao(dadosSQL.getString("observacao"));
                historicoVO.setDataregistro(dadosSQL.getTimestamp("dataRegistro"));
                historicoVO.getDepartamento().setCodigo(dadosSQL.getInt("codigoDepartamento"));
                historicoVO.getDepartamento().setNome(dadosSQL.getString("nomeDepartamento"));
                historicoVO.getTipoContato().setCodigo(dadosSQL.getInt("codigoTipoContato"));
                historicoVO.getTipoContato().setDescricao(dadosSQL.getString("descricaoTipoContato"));
                historicoVO.getResponsavel().setNome(dadosSQL.getString("nomeResponsavel"));
                if (historicoVO.getCodigo() != null && historicoVO.getCodigo() != 0) {
                    obj.getHistoricoFollowUpVOs().add(historicoVO);
                }
            }
        }
        return obj.getHistoricoFollowUpVOs();
    }

    private static boolean adicionarHistorico(List<HistoricoFollowUpVO> lista, Integer codigo) {
        for (HistoricoFollowUpVO historico : lista) {
            if (historico.getCodigo().equals(codigo)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(HistoricoFollowUpVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        obj.setObservacao(obj.getObservacao().toUpperCase());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela HistoricoFollowUpCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<HistoricoFollowUpVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboHistoricoFollowUpEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return getFacadeFactory().getHistoricoFollowUpFacade().consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboHistoricoFollowUpEnum.CODIGO_FOLLOW_UP.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return getFacadeFactory().getHistoricoFollowUpFacade().consultarPorCodigoFollowUp(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboHistoricoFollowUpEnum.OBSERVACAO.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getHistoricoFollowUpFacade().consultarPorObservacao(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboHistoricoFollowUpEnum.DATAREGISTRO.toString())) {
            Date valorData = Uteis.getDate(valorConsulta);
            return getFacadeFactory().getHistoricoFollowUpFacade().consultarPorDataregistro(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboHistoricoFollowUpEnum.MATRICULA_FUNCIONARIO.toString())) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return getFacadeFactory().getHistoricoFollowUpFacade().consultarPorMatriculaFuncionario(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoFollowUp</code> através do valor do atributo 
     * <code>matricula</code> da classe <code>Funcionario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>HistoricoFollowUpVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorMatriculaFuncionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        valorConsulta += "%";
        String sqlStr = "SELECT HistoricoFollowUp.* FROM HistoricoFollowUp, Funcionario WHERE HistoricoFollowUp.responsavel = Funcionario.codigo and upper( Funcionario.matricula ) like(?) ORDER BY Funcionario.matricula";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoFollowUp</code> através do valor do atributo 
     * <code>Date dataregistro</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>HistoricoFollowUpVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataregistro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        String sqlStr = "SELECT * FROM HistoricoFollowUp WHERE ((dataregistro >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataregistro <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataregistro";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoFollowUp</code> através do valor do atributo 
     * <code>String observacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>HistoricoFollowUpVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorObservacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        valorConsulta += "%";
        String sqlStr = "SELECT * FROM HistoricoFollowUp WHERE upper( observacao ) like(?) ORDER BY observacao";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoFollowUp</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>FollowUp</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>HistoricoFollowUpVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoFollowUp(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        String sqlStr = "SELECT HistoricoFollowUp.* FROM HistoricoFollowUp, prospect WHERE HistoricoFollowUp.followup = prospect.codigo and prospect.codigo >= ? ORDER BY FollowUp.codigo";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>HistoricoFollowUp</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>HistoricoFollowUpVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        String sqlStr = "SELECT * FROM HistoricoFollowUp WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>HistoricoFollowUpVO</code> resultantes da consulta.
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
     * em um objeto da classe <code>HistoricoFollowUpVO</code>.
     * @return  O objeto da classe <code>HistoricoFollowUpVO</code> com os dados devidamente montados.
     */
    public static HistoricoFollowUpVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        HistoricoFollowUpVO obj = new HistoricoFollowUpVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getProspect().setCodigo(new Integer(dadosSQL.getInt("prospect")));
        obj.setObservacao(dadosSQL.getString("observacao"));
        obj.setDataregistro(dadosSQL.getTimestamp("dataregistro"));
        obj.getDepartamento().setCodigo(dadosSQL.getInt("departamento"));
        obj.getTipoContato().setCodigo(dadosSQL.getInt("tipoContato"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosTipoContato(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto <code>HistoricoFollowUpVO</code>.
     * Faz uso da chave primária da classe <code>FuncionarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(HistoricoFollowUpVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(new Usuario().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosDepartamento(HistoricoFollowUpVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDepartamento().getCodigo().intValue() == 0) {
            obj.setDepartamento(new DepartamentoVO());
            return;
        }
        obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, nivelMontarDados, usuario));
    }
    public static void montarDadosTipoContato(HistoricoFollowUpVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getTipoContato().getCodigo().intValue() == 0) {
    		obj.setTipoContato(new TipoContatoVO());
    		return;
    	}
    	obj.setTipoContato(getFacadeFactory().getTipoContatoFacade().consultarPorChavePrimaria(obj.getTipoContato().getCodigo()));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>HistoricoFollowUpVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>HistoricoFollowUp</code>.
     * @param <code>followup</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 public void excluirHistoricoFollowUps(Integer followup, UsuarioVO usuario) throws Exception {
        HistoricoFollowUp.excluir(getIdEntidade());
        String sql = "DELETE FROM HistoricoFollowUp WHERE (prospect = " + followup + ")" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql);
    }

    /**
     * Operação responsável por consultar todos os <code>HistoricoFollowUpVO</code> relacionados a um objeto da classe <code>crm.FollowUp</code>.
     * @param followup  Atributo de <code>crm.FollowUp</code> a ser utilizado para localizar os objetos da classe <code>HistoricoFollowUpVO</code>.
     * @return List  Contendo todos os objetos da classe <code>HistoricoFollowUpVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarHistoricoFollowUps(Integer prospect, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        HistoricoFollowUp.consultar(getIdEntidade());
        List objetos = new ArrayList();
        String sql = "SELECT * FROM HistoricoFollowUp WHERE prospect = " + prospect;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        while (tabelaResultado.next()) {
            HistoricoFollowUpVO novoObj = new HistoricoFollowUpVO();
            novoObj = HistoricoFollowUp.montarDados(tabelaResultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>HistoricoFollowUpVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public HistoricoFollowUpVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade());
        String sql = "SELECT * FROM HistoricoFollowUp WHERE codigo = " + codigoPrm;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( HistoricoFollowUp ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<HistoricoFollowUpVO> consultarUnicidade(HistoricoFollowUpVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
        return new ArrayList(0);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirHistoricoFollowUpPorComunicadoInterno(PessoaVO pessoaVO, ComunicacaoInternaVO comunicacaoInternaVO, UsuarioVO usuarioVO) throws Exception {
    	ProspectsVO prospectsVO = getFacadeFactory().getPessoaFacade().realizarVinculoPessoaProspect(pessoaVO, usuarioVO);
		if (Uteis.isAtributoPreenchido(prospectsVO)) {
			HistoricoFollowUpVO historicoFollowUpVO = new HistoricoFollowUpVO();
			historicoFollowUpVO.setObservacao(getFacadeFactory().getComunicacaoInternaFacade().removerCabecalhoERodapeComunicadoInterno(comunicacaoInternaVO.getMensagem()));
			historicoFollowUpVO.getResponsavel().setCodigo(usuarioVO.getCodigo());
			historicoFollowUpVO.setDataregistro(comunicacaoInternaVO.getData());
			historicoFollowUpVO.getProspect().setCodigo(prospectsVO.getCodigo());
			incluir(historicoFollowUpVO, usuarioVO);
		}	
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return HistoricoFollowUp.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        HistoricoFollowUp.idEntidade = idEntidade;
    }
}
