package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PlanoFinanceiroAlunoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PlanoFinanceiroAlunoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PlanoFinanceiroAlunoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PlanoFinanceiroAlunoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PlanoFinanceiroAluno extends ControleAcesso implements PlanoFinanceiroAlunoInterfaceFacade {

    protected static String idEntidade;
    //private Hashtable itemPlanoFinanceiroAlunos;

    public PlanoFinanceiroAluno() throws Exception {
        super();
        setIdEntidade("Matricula");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PlanoFinanceiroAlunoVO</code>.
     */
    public PlanoFinanceiroAlunoVO novo() throws Exception {
        PlanoFinanceiroAluno.incluir(getIdEntidade());
        PlanoFinanceiroAlunoVO obj = new PlanoFinanceiroAlunoVO();
        return obj;
    }
    
    

    /**
     * Incluir objeto ou altera, dependendo da situação do mesmo (se é novo ou não)
     * @param obj
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(PlanoFinanceiroAlunoVO obj) throws Exception {
        if (obj.getCodigo().equals(0)) {
            incluir(obj);
        } else {
            alterar(obj);
        }
    }

    // TODO ((SEI CA37.1)) Adicionado para levar em conta o código da matricula periodo, sempre irá ser criado um novo
    // registro de plano financeiro aluno se a matrícula periodo for diferente.
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistirLevandoEmContaMatriculaPeriodo(PlanoFinanceiroAlunoVO obj, Integer codigoMatriculaPeriodo) throws Exception {
        if ((obj.getCodigo().equals(0))
                || (!obj.getMatriculaPeriodo().equals(codigoMatriculaPeriodo))) {
            obj.setMatriculaPeriodo(codigoMatriculaPeriodo);
            incluir(obj);
        } else {
            alterar(obj);
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PlanoFinanceiroAlunoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PlanoFinanceiroAlunoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoFinanceiroAlunoVO obj) throws Exception {
//        PlanoFinanceiroAlunoVO.validarDados(obj);
        final String sql = "INSERT INTO PlanoFinanceiroAluno( matricula, data, percDescontoMatricula, percDescontoParcela,descontoProgressivo, "
                + "justificativa, responsavel, documentoComprobatorio, ordemDescontoAluno, ordemPlanoDesconto, ordemConvenio, valorDescontoMatricula, "
                + "valorDescontoParcela, tipoDescontoMatricula, tipoDescontoParcela, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, "
                + "ordemDescontoAlunoValorCheio, ordemPlanoDescontoValorCheio, ordemConvenioValorCheio, ordemDescontoProgressivoValorCheio, "
                + "ordemDescontoProgressivo, matriculaPeriodo, descontoProgressivoMatricula, descontoProgressivoPrimeiraParcela, descontoValidoAteDataParcela ) "
                + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setString(1, obj.getMatricula());
                sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
                sqlInserir.setDouble(3, obj.getPercDescontoMatricula().doubleValue());
                sqlInserir.setDouble(4, obj.getPercDescontoParcela().doubleValue());
                if (obj.getDescontoProgressivo().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(5, obj.getDescontoProgressivo().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(5, 0);
                }
                sqlInserir.setString(6, obj.getJustificativa());
                if (Uteis.isAtributoPreenchido(obj.getResponsavel())) {
                	sqlInserir.setInt(7, obj.getResponsavel().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(7, 0);
                }
                sqlInserir.setString(8, obj.getDocumentoComprobatorio());
                sqlInserir.setInt(9, obj.getOrdemDescontoAluno());
                sqlInserir.setInt(10, obj.getOrdemPlanoDesconto());
                sqlInserir.setInt(11, obj.getOrdemConvenio());
                sqlInserir.setDouble(12, obj.getValorDescontoMatricula());
                sqlInserir.setDouble(13, obj.getValorDescontoParcela());
                sqlInserir.setString(14, obj.getTipoDescontoMatricula());
                sqlInserir.setString(15, obj.getTipoDescontoParcela());
                if (obj.getPlanoFinanceiroCursoVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(16, obj.getPlanoFinanceiroCursoVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(16, 0);
                }
                if (obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(17, obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(17, 0);
                }
                sqlInserir.setBoolean(18, obj.getOrdemDescontoAlunoValorCheio());
                sqlInserir.setBoolean(19, obj.getOrdemPlanoDescontoValorCheio());
                sqlInserir.setBoolean(20, obj.getOrdemConvenioValorCheio());
                sqlInserir.setBoolean(21, obj.getOrdemDescontoProgressivoValorCheio());
                sqlInserir.setInt(22, obj.getOrdemDescontoProgressivo());
                sqlInserir.setInt(23, obj.getMatriculaPeriodo());
                if (obj.getDescontoProgressivoMatricula().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(24, obj.getDescontoProgressivoMatricula().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(24, 0);
                }
                if (obj.getDescontoProgressivoPrimeiraParcela().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(25, obj.getDescontoProgressivoPrimeiraParcela().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(25, 0);
                }
                sqlInserir.setBoolean(26, obj.getDescontoValidoAteDataParcela());
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
        getFacadeFactory().getItemPlanoFinanceiroAlunoFacade().incluirItemPlanoFinanceiroAlunos(obj.getCodigo(), obj.getItemPlanoFinanceiroAlunoVOs());
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PlanoFinanceiroAlunoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PlanoFinanceiroAlunoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoFinanceiroAlunoVO obj) throws Exception {
//        PlanoFinanceiroAlunoVO.validarDados(obj);
        final String sql = "UPDATE PlanoFinanceiroAluno set matricula=?, data=?, percDescontoMatricula=?, percDescontoParcela=?,  descontoProgressivo=?, "
                + "justificativa=?, responsavel=?, documentoComprobatorio=?, ordemDescontoAluno=?, ordemPlanoDesconto=?, ordemConvenio=?, valorDescontoMatricula=?, "
                + "valorDescontoParcela=?, tipoDescontoMatricula=?, tipoDescontoParcela=?, planoFinanceiroCurso=?, condicaoPagamentoPlanoFinanceiroCurso=?, "
                + "ordemDescontoAlunoValorCheio =?, ordemPlanoDescontoValorCheio =?, ordemConvenioValorCheio=?, ordemDescontoProgressivoValorCheio=?, "
                + "ordemDescontoProgressivo=?, matriculaPeriodo=?, descontoProgressivoMatricula=?, descontoProgressivoPrimeiraParcela=?, descontoValidoAteDataParcela=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getMatricula());
                sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
                sqlAlterar.setDouble(3, obj.getPercDescontoMatricula().doubleValue());
                sqlAlterar.setDouble(4, obj.getPercDescontoParcela().doubleValue());
                if (obj.getDescontoProgressivo().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(5, obj.getDescontoProgressivo().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(5, 0);
                }
                sqlAlterar.setString(6, obj.getJustificativa());
                if (Uteis.isAtributoPreenchido(obj.getResponsavel())) {
                	sqlAlterar.setInt(7, obj.getResponsavel().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(7, 0);
                }
                sqlAlterar.setString(8, obj.getDocumentoComprobatorio());
                sqlAlterar.setInt(9, obj.getOrdemDescontoAluno());
                sqlAlterar.setInt(10, obj.getOrdemPlanoDesconto());
                sqlAlterar.setInt(11, obj.getOrdemConvenio());
                sqlAlterar.setDouble(12, obj.getValorDescontoMatricula());
                sqlAlterar.setDouble(13, obj.getValorDescontoParcela());
                sqlAlterar.setString(14, obj.getTipoDescontoMatricula());
                sqlAlterar.setString(15, obj.getTipoDescontoParcela());
                if (obj.getPlanoFinanceiroCursoVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(16, obj.getPlanoFinanceiroCursoVO().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(16, 0);
                }
                if (obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(17, obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(17, 0);
                }
                sqlAlterar.setBoolean(18, obj.getOrdemDescontoAlunoValorCheio());
                sqlAlterar.setBoolean(19, obj.getOrdemPlanoDescontoValorCheio());
                sqlAlterar.setBoolean(20, obj.getOrdemConvenioValorCheio());
                sqlAlterar.setBoolean(21, obj.getOrdemDescontoProgressivoValorCheio());
                sqlAlterar.setInt(22, obj.getOrdemDescontoProgressivo());
                sqlAlterar.setInt(23, obj.getMatriculaPeriodo());
                if (obj.getDescontoProgressivoMatricula().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(24, obj.getDescontoProgressivoMatricula().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(24, 0);
                }

                if (obj.getDescontoProgressivoPrimeiraParcela().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(25, obj.getDescontoProgressivoPrimeiraParcela().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(25, 0);
                }

                sqlAlterar.setBoolean(26, obj.getDescontoValidoAteDataParcela());
                sqlAlterar.setInt(27, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
        getFacadeFactory().getItemPlanoFinanceiroAlunoFacade().alterarItemPlanoFinanceiroAlunos(obj.getCodigo(), obj.getItemPlanoFinanceiroAlunoVOs());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPlanoFinanceiroAlunoDescontoProgressivoCondicaoPagamentoPlanoCursoPorMatriculaPeriodo(final Integer descontoProgressivo, final Integer descontoProgressivoMatricula, final Integer planoFinanceiroCurso, final Integer condicaoPagamentoPlanoFinanceiroCurso, final Integer matriculaPeriodo) {
        final String sql = "UPDATE PlanoFinanceiroAluno set descontoProgressivo=?, descontoProgressivoMatricula=?, planoFinanceiroCurso=?, condicaoPagamentoPlanoFinanceiroCurso=? WHERE ((matriculaPeriodo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, descontoProgressivo.intValue());
                sqlAlterar.setInt(2, descontoProgressivoMatricula.intValue());
                sqlAlterar.setInt(3, planoFinanceiroCurso.intValue());
                sqlAlterar.setInt(4, condicaoPagamentoPlanoFinanceiroCurso.intValue());
                sqlAlterar.setInt(5, matriculaPeriodo.intValue());
                return sqlAlterar;
            }
        });

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PlanoFinanceiroAlunoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PlanoFinanceiroAlunoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PlanoFinanceiroAlunoVO obj) throws Exception {
//        PlanoFinanceiroAluno.excluir(getIdEntidade());
        String sql = "DELETE FROM PlanoFinanceiroAluno WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        getFacadeFactory().getItemPlanoFinanceiroAlunoFacade().excluirItemPlanoFinanceiroAlunos(obj.getCodigo());
    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoFinanceiroAluno</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PlanoFinanceiroAlunoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoFinanceiroAlunoVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PlanoFinanceiroAluno.* FROM PlanoFinanceiroAluno, Pessoa WHERE PlanoFinanceiroAluno.responsavel = Pessoa.codigo and Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoFinanceiroAluno</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PlanoFinanceiroAlunoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoFinanceiroAlunoVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroAluno WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoFinanceiroAluno</code> através do valor do atributo 
     * <code>matricula</code> da classe <code>Matricula</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PlanoFinanceiroAlunoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoFinanceiroAlunoVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PlanoFinanceiroAluno.* FROM PlanoFinanceiroAluno, Matricula WHERE PlanoFinanceiroAluno.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') ORDER BY Matricula.matricula";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public PlanoFinanceiroAlunoVO consultarPorMatriculaMatriculaUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PlanoFinanceiroAluno.* FROM PlanoFinanceiroAluno, Matricula WHERE PlanoFinanceiroAluno.matricula = Matricula.matricula and lower (Matricula.matricula) = ('" + valorConsulta.toLowerCase() + "') ORDER BY PlanoFinanceiroAluno.codigo desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new PlanoFinanceiroAlunoVO();
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    // TODO (SEI CA37.1) Adicionado para consultar o plano financeiro do aluno com base na matrícula periodo.
    public PlanoFinanceiroAlunoVO consultarPorMatriculaPeriodo(Integer codigoMatriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT pfa.* FROM planoFinanceiroAluno pfa ");
        sqlStr.append("INNER JOIN matriculaPeriodo mp ON pfa.matriculaPeriodo = mp.codigo ");
        sqlStr.append("WHERE mp.codigo = ?");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoMatriculaPeriodo});
            if (!tabelaResultado.next()) {
                return new PlanoFinanceiroAlunoVO();
            }
            return montarDados(tabelaResultado, nivelMontarDados, usuario);
        } finally {
            sqlStr = null;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoFinanceiroAluno</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PlanoFinanceiroAlunoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoFinanceiroAlunoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroAluno WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PlanoFinanceiroAlunoVO> consultarPorPlanoFinanceiroCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroAluno WHERE planoFinanceiroCurso = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PlanoFinanceiroAlunoVO> consultarPorCondicaoPagamentoPlanoFinanceiroCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroAluno WHERE condicaoPagamentoPlanoFinanceiroCurso = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PlanoFinanceiroAlunoVO</code> resultantes da consulta.
     */
    public static List<PlanoFinanceiroAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<PlanoFinanceiroAlunoVO> vetResultado = new ArrayList<PlanoFinanceiroAlunoVO>(0);
        while (tabelaResultado.next()) {
            PlanoFinanceiroAlunoVO obj = new PlanoFinanceiroAlunoVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PlanoFinanceiroAlunoVO</code>.
     * @return  O objeto da classe <code>PlanoFinanceiroAlunoVO</code> com os dados devidamente montados.
     */
    public static PlanoFinanceiroAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ////System.out.println(">> Montar dados(Configuracao) - " + new Date());
        PlanoFinanceiroAlunoVO obj = new PlanoFinanceiroAlunoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setPercDescontoMatricula(dadosSQL.getDouble("percDescontoMatricula"));
        obj.setPercDescontoParcela(dadosSQL.getDouble("percDescontoParcela"));
        obj.getDescontoProgressivo().setCodigo(dadosSQL.getInt("descontoProgressivo"));
        obj.getDescontoProgressivoMatricula().setCodigo(dadosSQL.getInt("descontoProgressivoMatricula"));
        obj.getDescontoProgressivoPrimeiraParcela().setCodigo(dadosSQL.getInt("descontoProgressivoPrimeiraParcela"));
        obj.setJustificativa(dadosSQL.getString("justificativa"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.setDocumentoComprobatorio(dadosSQL.getString("documentoComprobatorio"));
        obj.setOrdemDescontoAluno(dadosSQL.getInt("ordemDescontoAluno"));
        obj.setOrdemPlanoDesconto(dadosSQL.getInt("ordemPlanoDesconto"));
        obj.setOrdemConvenio(dadosSQL.getInt("ordemConvenio"));
        obj.setOrdemDescontoProgressivo(dadosSQL.getInt("ordemDescontoProgressivo"));
        obj.setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("ordemDescontoAlunoValorCheio"));
        obj.setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("ordemPlanoDescontoValorCheio"));
        obj.setOrdemConvenioValorCheio(dadosSQL.getBoolean("ordemConvenioValorCheio"));
        obj.setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("ordemDescontoProgressivoValorCheio"));
        obj.setValorDescontoMatricula(dadosSQL.getDouble("valorDescontoMatricula"));
        obj.setValorDescontoParcela(dadosSQL.getDouble("valorDescontoParcela"));
        obj.setTipoDescontoMatricula(dadosSQL.getString("tipoDescontoMatricula"));
        obj.setTipoDescontoParcela(dadosSQL.getString("tipoDescontoParcela"));
        obj.getPlanoFinanceiroCursoVO().setCodigo(dadosSQL.getInt("planoFinanceiroCurso"));
        obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().setCodigo(dadosSQL.getInt("condicaoPagamentoPlanoFinanceiroCurso"));
        obj.setMatriculaPeriodo(dadosSQL.getInt("matriculaPeriodo"));
        obj.setDescontoValidoAteDataParcela(dadosSQL.getBoolean("descontoValidoAteDataParcela"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setItemPlanoFinanceiroAlunoVOs(ItemPlanoFinanceiroAluno.consultarItemPlanoFinanceiroAlunos(obj.getCodigo(), false, usuario));
        Ordenacao.ordenarLista(obj.getItemPlanoFinanceiroAlunoVOs(), "ordenacao");
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            montarDadosDescontoProgressivoPrimeiraParcela(obj, nivelMontarDados, usuario);
            montarDadosCondicaoPagamentoPlanoFinanceiroCurso(obj, nivelMontarDados, usuario);
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        montarDadosPlanoFinanceiroCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosCondicaoPagamentoPlanoFinanceiroCurso(obj, nivelMontarDados, usuario);
        montarDadosDescontoProgressivo(obj, nivelMontarDados, usuario);
        montarDadosDescontoProgressivoMatricula(obj, nivelMontarDados, usuario);
        montarDadosDescontoProgressivoPrimeiraParcela(obj, nivelMontarDados, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PlanoFinanceiroAlunoVO</code>.
     * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(PlanoFinanceiroAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PlanoFinanceiroCursoVO</code> relacionado ao objeto <code>PlanoFinanceiroAlunoVO</code>.
     * Faz uso da chave primária da classe <code>PlanoFinanceiroCursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosPlanoFinanceiroCurso(PlanoFinanceiroAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPlanoFinanceiroCursoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setPlanoFinanceiroCursoVO(getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(obj.getPlanoFinanceiroCursoVO().getCodigo(), "", nivelMontarDados, usuario));

    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CondicaoPagamentoPlanoFinanceiroCursoVO</code> relacionado ao objeto <code>PlanoFinanceiroAlunoVO</code>.
     * Faz uso da chave primária da classe <code>CondicaoPagamentoPlanoFinanceiroCursoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCondicaoPagamentoPlanoFinanceiroCurso(PlanoFinanceiroAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue() == 0) {
            return;
        }
        obj.setCondicaoPagamentoPlanoFinanceiroCursoVO(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DescontoProgressivoVO</code> relacionado ao objeto <code>PlanoFinanceiroAlunoVO</code>.
     * Faz uso da chave primária da classe <code>DescontoProgressivoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDescontoProgressivo(PlanoFinanceiroAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDescontoProgressivo().getCodigo().intValue() == 0) {
            return;
        }
        obj.setDescontoProgressivo(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivo().getCodigo(), usuario));
    }

    public static void montarDadosDescontoProgressivoPrimeiraParcela(PlanoFinanceiroAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDescontoProgressivoPrimeiraParcela().getCodigo().intValue() == 0) {
            return;
        }
        obj.setDescontoProgressivoPrimeiraParcela(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivoPrimeiraParcela().getCodigo(), usuario));
    }

    public static void montarDadosDescontoProgressivoMatricula(PlanoFinanceiroAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDescontoProgressivoMatricula().getCodigo().intValue() == 0) {
            return;
        }
        obj.setDescontoProgressivoMatricula(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivoMatricula().getCodigo(), usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PlanoFinanceiroAlunoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PlanoFinanceiroAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM PlanoFinanceiroAluno WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PlanoFinanceiroAluno.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        PlanoFinanceiroAluno.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder sqlStr = new StringBuilder("DELETE FROM planofinanceiroaluno WHERE matricula = '").append(matricula).append("' ");
        try {
            getConexao().getJdbcTemplate().update(sqlStr.toString());
        } finally {
            sqlStr = null;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirComBaseNaMatriculaPeriodo(Integer codMatriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder sqlStr = new StringBuilder("DELETE FROM planofinanceiroaluno WHERE matriculaperiodo = ").append(codMatriculaPeriodo).append(" ");
        try {
            getConexao().getJdbcTemplate().update(sqlStr.toString());
        } finally {
            sqlStr = null;
        }
    }
    
   @Override 
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public void adicionarObjItemPlanoFinanceiroAlunoVOs(PlanoFinanceiroAlunoVO obj, ItemPlanoFinanceiroAlunoVO ipfa) throws Exception {
        ItemPlanoFinanceiroAlunoVO.validarDados(ipfa);
      //  Uteis.checkState(consultarSeExisteContaReceberParaParceiro(obj, ipfa), "Não é possível realizar essa operação. Pois existem Contas a Receber que estão Quitadas para o Parceiro informado nesse Convênio dessa Matrícula.");       
        int index = 0;
        for (ItemPlanoFinanceiroAlunoVO objExistente : obj.getItemPlanoFinanceiroAlunoVOs()) {
        	if (objExistente.getTipoItemPlanoFinanceiro().equals("CO") 
        			&& ipfa.getTipoItemPlanoFinanceiro().equals("CO")
        			&& objExistente.getConvenio().getCodigo().equals(ipfa.getConvenio().getCodigo())) {
            	obj.getItemPlanoFinanceiroAlunoVOs().set(index, ipfa);
                return;
            } else if (objExistente.getTipoItemPlanoFinanceiro().equals("PD") 
            		&& ipfa.getTipoItemPlanoFinanceiro().equals("PD")
            		&& objExistente.getPlanoDesconto().getCodigo().equals(ipfa.getPlanoDesconto().getCodigo())) {
                obj.getItemPlanoFinanceiroAlunoVOs().set(index, ipfa);
                return;
            }
            index++;
		}
        obj.getItemPlanoFinanceiroAlunoVOs().add(ipfa);
    }

    /**
     *  Este método é responsável por alterar o plano financeiro do aluno conforme os dados de desconto progressivo e plano desconto informado
     * no mapa de pendencia de controle de conbrança.
     * O plano financeiro será totalmente modificado e todos os seus descontos serão apagados e será aplicado somente o desconto progressivo e o plano de desconto.
     * @param matriculaPeriodo
     * @param planoDesconto
     * @param descontoProgressivo
     * @param usuarioVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAlteracaoPlanoFinanceiroAlunoConformeMapaPendenciaControleCobranca(Integer matriculaPeriodo, Integer planoDesconto, Integer descontoProgressivo, UsuarioVO usuarioVO) throws Exception {

        PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO = null;
        ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO = null;

        try {
            planoFinanceiroAlunoVO = consultarPorMatriculaPeriodo(matriculaPeriodo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
            if (planoFinanceiroAlunoVO.getCodigo().intValue() == 0) {
                throw new Exception("Não foi encontrado um plano financeiro para esta matrícula.");
            }
            planoFinanceiroAlunoVO.getDescontoProgressivo().setCodigo(descontoProgressivo);
            planoFinanceiroAlunoVO.setPercDescontoParcela(0.0);
            planoFinanceiroAlunoVO.setTipoDescontoParcela("VE");
            planoFinanceiroAlunoVO.getItemPlanoFinanceiroAlunoVOs().clear();
            if (planoDesconto != null && planoDesconto.intValue() > 0) {
                itemPlanoFinanceiroAlunoVO = new ItemPlanoFinanceiroAlunoVO();
                itemPlanoFinanceiroAlunoVO.setTipoItemPlanoFinanceiro("PD");
                itemPlanoFinanceiroAlunoVO.setPlanoFinanceiroAluno(planoFinanceiroAlunoVO.getCodigo());
                itemPlanoFinanceiroAlunoVO.getPlanoDesconto().setCodigo(planoDesconto);
                itemPlanoFinanceiroAlunoVO.setRegerarConta(Boolean.FALSE);
                planoFinanceiroAlunoVO.getItemPlanoFinanceiroAlunoVOs().add(itemPlanoFinanceiroAlunoVO);
            }
            alterar(planoFinanceiroAlunoVO);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.removerObjetoMemoria(planoFinanceiroAlunoVO);
            Uteis.removerObjetoMemoria(itemPlanoFinanceiroAlunoVO);
        }
    }
    
    public void excluirObjItemPlanoFinanceiroPlanoDescontoAlunoVOs(List itemPlanoFinanceiroAlunoVOs) throws Exception {
        List listaTemp = new ArrayList(0);
        listaTemp.addAll(itemPlanoFinanceiroAlunoVOs);
        Iterator i = listaTemp.iterator();
        while (i.hasNext()) {
            ItemPlanoFinanceiroAlunoVO objExistente = (ItemPlanoFinanceiroAlunoVO) i.next();
            if (objExistente.getTipoItemPlanoFinanceiro().equals("PD")) {
            	itemPlanoFinanceiroAlunoVOs.remove(objExistente);
            }
        }
        listaTemp = null;
    }
    
    @Override
    public void excluirObjItemPlanoFinanceiroAlunoConfiguradoRemoverRenovacao(List<ItemPlanoFinanceiroAlunoVO> itemPlanoFinanceiroAlunoVOs) throws Exception {
    	 List listaTemp = new ArrayList(0);
         listaTemp.addAll(itemPlanoFinanceiroAlunoVOs);
         Iterator i = listaTemp.iterator();
         while (i.hasNext()) {
             ItemPlanoFinanceiroAlunoVO objExistente = (ItemPlanoFinanceiroAlunoVO) i.next();
             if (objExistente.getTipoItemPlanoFinanceiro().equals("PD")) {
 				if (objExistente.getPlanoDesconto().getRemoverDescontoRenovacao()) {
 					itemPlanoFinanceiroAlunoVOs.remove(objExistente);
 				}
 			} else {
 				if (objExistente.getConvenio().getRemoverDescontoRenovacao()) {
 					itemPlanoFinanceiroAlunoVOs.remove(objExistente);
 				}
 			}
         }
         listaTemp = null;
        
   }

    @Override
    public PlanoFinanceiroAlunoVO consultarPorMatriculaPeriodoFichaAluno(Integer matriculaPeriodo, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select planofinanceiroaluno.codigo, planofinanceiroaluno.valorDescontoMatricula, planofinanceiroaluno.valorDescontoParcela, descontovalidoatedataparcela, ");
    	sb.append(" planofinanceiroaluno.tipoDescontoMatricula, planofinanceiroaluno.tipoDescontoParcela, ");
    	sb.append(" descontoProgressivo.codigo AS \"descontoProgressivo.codigo\", descontoProgressivo.nome \"descontoProgressivo.nome\", ");
    	sb.append(" descontoProgressivomatricula.codigo AS \"descontoProgressivomatricula.codigo\", descontoProgressivomatricula.nome \"descontoProgressivomatricula.nome\" ");
    	sb.append(" from planofinanceiroaluno ");
    	sb.append(" left join descontoProgressivo on descontoProgressivo.codigo = planofinanceiroaluno.descontoprogressivo ");
    	sb.append(" left join descontoProgressivo descontoProgressivomatricula on descontoProgressivomatricula.codigo = planofinanceiroaluno.descontoProgressivomatricula ");
    	sb.append(" where planofinanceiroaluno.matriculaperiodo = ").append(matriculaPeriodo);
    	sb.append("");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	PlanoFinanceiroAlunoVO obj = new PlanoFinanceiroAlunoVO();
    	if (tabelaResultado.next()) {
    		obj.setCodigo(tabelaResultado.getInt("codigo"));
    		obj.setValorDescontoMatricula(tabelaResultado.getDouble("valorDescontoMatricula"));
    		obj.setValorDescontoParcela(tabelaResultado.getDouble("valorDescontoParcela"));
    		obj.setDescontoValidoAteDataParcela(tabelaResultado.getBoolean("descontovalidoatedataparcela"));
    		obj.setTipoDescontoMatricula(tabelaResultado.getString("tipoDescontoMatricula"));
    		obj.setTipoDescontoParcela(tabelaResultado.getString("tipoDescontoParcela"));
    		
    		obj.getDescontoProgressivo().setCodigo(tabelaResultado.getInt("descontoProgressivo.codigo"));
    		obj.getDescontoProgressivo().setNome(tabelaResultado.getString("descontoProgressivo.nome"));
    		
    		obj.getDescontoProgressivoMatricula().setCodigo(tabelaResultado.getInt("descontoProgressivoMatricula.codigo"));
    		obj.getDescontoProgressivoMatricula().setNome(tabelaResultado.getString("descontoProgressivoMatricula.nome"));
    		obj.setItemPlanoFinanceiroAlunoVOs(getFacadeFactory().getItemPlanoFinanceiroAlunoFacade().consultarPorCodigoPlanoFinanceiroAluno(obj.getCodigo(), false, usuarioVO));
            Ordenacao.ordenarLista(obj.getItemPlanoFinanceiroAlunoVOs(), "ordenacao");
    	}
    	return obj;
    }
    
   
    private Boolean consultarSeExisteContaReceberParaParceiro(PlanoFinanceiroAlunoVO obj, ItemPlanoFinanceiroAlunoVO ipfa) throws Exception {
    	StringBuilder sql = new StringBuilder("select count(codigo) as qtd from contareceber where situacao!=? and tipoorigem = ? and parceiro = ? and matriculaaluno=? and matriculaperiodo=? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), "AR","BCC", ipfa.getConvenio().getParceiro().getCodigo(), obj.getMatricula(), obj.getMatriculaPeriodo());
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
    }
}
