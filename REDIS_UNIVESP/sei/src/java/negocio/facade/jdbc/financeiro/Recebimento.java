package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.RecebimentoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.processosel.Inscricao;
import negocio.facade.jdbc.sad.ReceitaDW;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.RecebimentoInterfaceFacade;
import relatorio.negocio.comuns.financeiro.ComprovanteRecebimentoRelVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RecebimentoVO</code>
 * . Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>RecebimentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see RecebimentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class Recebimento extends ControleAcesso implements RecebimentoInterfaceFacade {

    protected static String idEntidade;

    public Recebimento() throws Exception {
        super();
        setIdEntidade("Recebimento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>RecebimentoVO</code>.
     */
    public RecebimentoVO novo() throws Exception {
        Recebimento.incluir(getIdEntidade());
        RecebimentoVO obj = new RecebimentoVO();
        return obj;
    }

    public static void validarPermisaoRenegociarContaReceber(UsuarioVO usuario) throws Exception {
        verificarPermissaoUsuarioFuncionalidade("RenegociarContaReceber", usuario);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RecebimentoVO</code>. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>RecebimentoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RecebimentoVO obj, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        try {
            Recebimento.incluir(getIdEntidade());
            RecebimentoVO.validarDados(obj);
            final String sql = "INSERT INTO Recebimento( data, valor, descricao, nrDocumento, contaCorrente, centroReceita, matriculaAluno, funcionario, tipoOrigem, codigoOrigem, contaReceber, tipoPessoa, candidato, codigoBarra, descontoProgressivo, pessoa, valorRecebido, unidadeEnsino ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setDouble(2, obj.getValor().doubleValue());
                    sqlInserir.setString(3, obj.getDescricao());
                    sqlInserir.setString(4, obj.getNrDocumento());
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    if (obj.getCentroReceita().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getCentroReceita().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (!obj.getMatriculaAluno().getMatricula().equals("")) {
                        sqlInserir.setString(7, obj.getMatriculaAluno().getMatricula());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if (obj.getFuncionario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getFuncionario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    sqlInserir.setString(9, obj.getTipoOrigem());
                    sqlInserir.setString(10, obj.getCodigoOrigem());
                    if (obj.getContaReceber().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(11, obj.getContaReceber().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
                    sqlInserir.setString(12, obj.getTipoPessoa());
                    if (obj.getCandidato().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(13, obj.getCandidato().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(13, 0);
                    }
                    sqlInserir.setString(14, obj.getCodigoBarra());
                    if (obj.getDescontoProgressivo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(15, obj.getDescontoProgressivo().getCodigo());
                    } else {
                        sqlInserir.setNull(15, 0);
                    }
                    sqlInserir.setInt(16, obj.getPessoa().getCodigo().intValue());
                    sqlInserir.setDouble(17, obj.getValorRecebido().doubleValue());
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(18, obj.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlInserir.setNull(18, 0);
                    }
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
            if (!obj.getRenegociacaoContaReceberVOs().isEmpty() && obj.getRenegociar().equals(Boolean.TRUE)) {
                ContaReceber.excluirContaFilho(obj.getContaReceber(), usuario);
                ContaReceber.incluirContaFilho(obj.getRenegociacaoContaReceberVOs(), configuracaoFinanceiroVO, usuario);
            }
            alterarSituacaoContaReceber(obj, configuracaoFinanceiroVO, usuario);
            alterarSituacaoOrigemContaReceber(obj, usuario);
            incluirRecebimentoEmReceitaDW(obj,configuracaoFinanceiroVO, usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    public void incluirRecebimentoEmReceitaDW(RecebimentoVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        MatriculaVO matricula = new MatriculaVO();
        MatriculaPeriodoVO matriculaPeriodo = new MatriculaPeriodoVO();
        InscricaoVO inscricao = new InscricaoVO();
        if (obj.getTipoOrigem().equals("MAT") || obj.getTipoOrigem().equals("MEN")) {
            matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaAluno().getMatricula(), 0, NivelMontarDados.TODOS, usuario);
            Matricula.montarDadosCurso(matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(Integer.parseInt(obj.getCodigoOrigem()), Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO,usuario);
        }
        if (obj.getTipoOrigem().equals("IPS")) {
            inscricao = new Inscricao().consultarPorChavePrimaria(Integer.parseInt(obj.getCodigoOrigem()), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        }
        new ReceitaDW().incluir(obj.getReceitaDWVO(matricula, matriculaPeriodo, inscricao));
    }

    public void alterarSituacaoContaReceber(RecebimentoVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {        
        if (obj.getExisteContaReceber()) {
            if (!obj.getRenegociacaoContaReceberVOs().isEmpty()) {
                obj.getContaReceber().setSituacao("NE");
            } else {
                obj.getContaReceber().setSituacao("RE");
            }
            obj.getContaReceber().setValorRecebido(obj.getValorRecebido());
            getFacadeFactory().getContaReceberFacade().alterar(obj.getContaReceber(), true, configuracaoFinanceiro, usuario);
        }
    }

    public void alterarSituacaoOrigemContaReceber(RecebimentoVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getTipoOrigem().equals("IPS")) {
            if (!obj.getCodigoOrigem().equals("")) {
                alterarStatusInscricaoProcessoSeletivo(Integer.parseInt(obj.getContaReceber().getCodOrigem()), obj.getUnidadeEnsino().getCodigo(), usuarioVO);
            }
        } else if (obj.getTipoOrigem().equals("MAT")) {
            if (!obj.getCodigoOrigem().equals("")) {
                alterarSituacaoMatricula(Integer.parseInt(obj.getContaReceber().getCodOrigem()));
            }
        } else if (obj.getTipoOrigem().equals("REQ")) {
            if (!obj.getCodigoOrigem().equals("")) {
                alterarSituacaoRequerimento(Integer.parseInt(obj.getContaReceber().getCodOrigem()), usuarioVO);
            }
        }
    }    

    public void alterarStatusInscricaoProcessoSeletivo(Integer codigo, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
    	getFacadeFactory().getInscricaoFacade().alterarSituacaoFinanceira(codigo, "CO", unidadeEnsino, usuarioVO);
    }

    public void alterarSituacaoMatricula(Integer matricula) throws Exception {
        getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoFinanceiraMatriculaPeriodo(matricula, "CO");
    }

    public void alterarSituacaoRequerimento(Integer codigo, UsuarioVO usuarioVO) throws Exception {
        getFacadeFactory().getRequerimentoFacade().alterarSituacaoFinanceiraESituacaoExecucao(codigo, false, "EX", "PG", false, usuarioVO);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RecebimentoVO</code>. Sempre utiliza
     * a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os
     * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>RecebimentoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(final RecebimentoVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
        try {
            Recebimento.alterar(getIdEntidade());
            RecebimentoVO.validarDados(obj);
            final String sql = "UPDATE Recebimento set data=?, valor=?, descricao=?, nrDocumento=?, contaCorrente=?, centroReceita=?, matriculaAluno=?, funcionario=?, tipoOrigem=?, codigoOrigem=?, contaReceber=?, tipoPessoa=?, candidato=?, codigoBarra = ?, descontoProgressivo = ?, pessoa = ?, valorRecebido=?, unidadeEnsino=?  WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setDouble(2, obj.getValor().doubleValue());
                    sqlAlterar.setString(3, obj.getDescricao());
                    sqlAlterar.setString(4, obj.getNrDocumento());
                    if (obj.getContaCorrente().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getContaCorrente().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    if (obj.getCentroReceita().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getCentroReceita().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    if (!obj.getMatriculaAluno().getMatricula().equals("")) {
                        sqlAlterar.setString(7, obj.getMatriculaAluno().getMatricula());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    if (obj.getFuncionario().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getFuncionario().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    sqlAlterar.setString(9, obj.getTipoOrigem());
                    sqlAlterar.setString(10, obj.getCodigoOrigem());
                    if (obj.getContaReceber().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(11, obj.getContaReceber().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(11, 0);
                    }
                    sqlAlterar.setString(12, obj.getTipoPessoa());
                    if (obj.getCandidato().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(13, obj.getCandidato().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(13, 0);
                    }
                    sqlAlterar.setString(14, obj.getCodigoBarra());
                    if (obj.getDescontoProgressivo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(15, obj.getDescontoProgressivo().getCodigo());
                    } else {
                        sqlAlterar.setNull(15, 0);
                    }
                    sqlAlterar.setInt(16, obj.getPessoa().getCodigo().intValue());
                    sqlAlterar.setDouble(17, obj.getValorRecebido().intValue());
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(18, obj.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlAlterar.setNull(18, 0);
                    }
                    sqlAlterar.setInt(19, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

            if (!obj.getRenegociacaoContaReceberVOs().equals(new ArrayList(0))) {
                ContaReceber.excluirContaFilho(obj.getContaReceber(), usuario);
                ContaReceber.incluirContaFilho(obj.getRenegociacaoContaReceberVOs(), configuracaoFinanceiro, usuario);
            }
            alterarSituacaoContaReceber(obj, configuracaoFinanceiro, usuario);
            alterarSituacaoOrigemContaReceber(obj, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RecebimentoVO</code>. Sempre localiza o registro
     * a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>RecebimentoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RecebimentoVO obj, UsuarioVO usuario) throws Exception {
        try {
            Recebimento.excluir(getIdEntidade());
            String sql = "DELETE FROM Recebimento WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Recebimento</code> através do valor do atributo
     * <code>identificadorCentroReceita</code> da classe <code>CentroReceita</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>RecebimentoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorIdentificadorCentroReceitaCentroReceita(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Recebimento.* FROM Recebimento, CentroReceita WHERE Recebimento.centroReceita = CentroReceita.codigo and upper( CentroReceita.identificadorCentroReceita ) like('"
                + valorConsulta.toUpperCase() + "%') ORDER BY CentroReceita.identificadorCentroReceita";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT Recebimento.* FROM Recebimento, CentroReceita WHERE Recebimento.centroReceita = CentroReceita.codigo and upper( CentroReceita.identificadorCentroReceita ) like('"
                    + valorConsulta.toUpperCase()
                    + "%') and Recebimento.unidadeEnsino = "
                    + unidadeEnsino.intValue()
                    + " ORDER BY CentroReceita.identificadorCentroReceita";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    public List consultarPorCodigoContaReceber(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Recebimento.* FROM Recebimento WHERE Recebimento.contaReceber = "
                + valorConsulta.intValue() + " ORDER BY Recebimento.contaReceber";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT Recebimento.* FROM Recebimento WHERE Recebimento.contaReceber = "
                    + valorConsulta.intValue() + " and Recebimento.unidadeEnsino = " + unidadeEnsino.intValue()
                    + " ORDER BY Recebimento.contaReceber";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Recebimento</code> através do valor do atributo
     * <code>String nrDocumento</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>RecebimentoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNrDocumento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Recebimento WHERE upper( nrDocumento ) like('" + valorConsulta.toUpperCase()
                + "%') ORDER BY nrDocumento";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM Recebimento WHERE upper( nrDocumento ) like('" + valorConsulta.toUpperCase()
                    + "%') and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY nrDocumento";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    public List consultarPorCodigoBarra(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Recebimento WHERE upper( codigoBarra ) = ('" + valorConsulta.toUpperCase()
                + "') ORDER BY codigobarra";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM Recebimento WHERE upper( codigoBarra ) = ('" + valorConsulta.toUpperCase()
                    + "') and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY codigobarra";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    public List consultarPorCandidato(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Recebimento, Pessoa WHERE (recebimento.candidato = pessoa.codigo) and upper( pessoa.cpf ) like('"
                + valorConsulta.toUpperCase() + "%') ORDER BY pessoa.cpf";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM Recebimento, Pessoa WHERE (recebimento.candidato = pessoa.codigo) and upper( pessoa.cpf ) like('"
                    + valorConsulta.toUpperCase()
                    + "%') and recebimento.unidadeEnsino = "
                    + unidadeEnsino.intValue()
                    + " ORDER BY pessoa.cpf";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));


    }

    public List consultarPorAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados ,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Recebimento, Matricula WHERE (recebimento.matriculaAluno = matricula.matricula) and upper( matricula.matricula ) like('"
                + valorConsulta.toUpperCase() + "%') ORDER BY matricula.matricula";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM Recebimento, Matricula WHERE (recebimento.matriculaAluno = matricula.matricula) and upper( matricula.matricula ) like('"
                    + valorConsulta.toUpperCase()
                    + "%') and recebimento.unidadeEnsino = "
                    + unidadeEnsino.intValue()
                    + " ORDER BY matricula.matricula";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    public List consultarPorFuncionario(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Recebimento, Funcionario WHERE (recebimento.funcionario = funcionario.codigo) and lower( funcionario.matricula ) like('"
                + valorConsulta.toLowerCase() + "%') ORDER BY funcionario.matricula";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM Recebimento, Funcionario WHERE (recebimento.funcionario = funcionario.codigo) and lower( funcionario.matricula ) like('"
                    + valorConsulta.toLowerCase()
                    + "%') and recebimento.unidadeEnsino = "
                    + unidadeEnsino.intValue()
                    + " ORDER BY funcionario.matricula";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Recebimento</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>RecebimentoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Recebimento WHERE codigo " + (valorConsulta.intValue() == 0 ? ">" : "") + ">= "
                + valorConsulta.intValue() + " ORDER BY codigo";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM Recebimento WHERE codigo " + (valorConsulta.intValue() == 0 ? ">" : "") + "= "
                    + valorConsulta.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue()
                    + " ORDER BY codigo";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>RecebimentoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>RecebimentoVO</code>.
     *
     * @return O objeto da classe <code>RecebimentoVO</code> com os dados devidamente montados.
     */
    public static RecebimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        RecebimentoVO obj = new RecebimentoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.setValorRecebido(new Double(dadosSQL.getDouble("valorRecebido")));
        obj.setNrDocumento(dadosSQL.getString("nrDocumento"));
        obj.getMatriculaAluno().setMatricula(dadosSQL.getString("matriculaAluno"));
        obj.getFuncionario().setCodigo(new Integer(dadosSQL.getInt("funcionario")));
        obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
        obj.getCandidato().setCodigo(new Integer(dadosSQL.getInt("candidato")));
        obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosMatriculaAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosCandidato(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }

        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contaCorrente")));
        obj.getCentroReceita().setCodigo(new Integer(dadosSQL.getInt("centroReceita")));
        obj.setTipoOrigem(dadosSQL.getString("tipoOrigem"));
        obj.setCodigoOrigem(dadosSQL.getString("codigoOrigem"));
        obj.setCodigoBarra(dadosSQL.getString("codigoBarra"));
        obj.getContaReceber().setCodigo(new Integer(dadosSQL.getInt("contaReceber")));
        obj.getDescontoProgressivo().setCodigo(new Integer(dadosSQL.getInt("descontoProgressivo")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));

        obj.setNovoObj(Boolean.FALSE);

        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosCentroReceita(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosMatriculaAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosCandidato(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosContaReceber(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
        montarDadosDescontoProgressivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaReceberVO</code> relacionado ao objeto
     * <code>RecebimentoVO</code>. Faz uso da chave primária da classe <code>ContaReceberVO</code> para realizar a
     * consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaReceber(RecebimentoVO obj, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        if (obj.getContaReceber().getCodigo().intValue() == 0) {
            obj.setContaReceber(new ContaReceberVO());
            return;
        }
        obj.setContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(obj.getContaReceber().getCodigo(), false, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    public static void montarDadosPessoa(RecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosUnidadeEnsino(RecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosDescontoProgressivo(RecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDescontoProgressivo().getCodigo().intValue() == 0) {
            obj.setDescontoProgressivo(new DescontoProgressivoVO());
            return;
        }
        obj.setDescontoProgressivo(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivo().getCodigo(), usuario));
    }

    public static void montarDadosCandidato(RecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCandidato().getCodigo().intValue() == 0) {
            obj.setCandidato(new PessoaVO());
            return;
        }
        obj.setCandidato(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCandidato().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>RecebimentoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosFuncionario(RecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionario().getCodigo().intValue() == 0) {
            obj.setFuncionario(new FuncionarioVO());
            return;
        }
        obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto
     * <code>RecebimentoVO</code>. Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a
     * consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosMatriculaAluno(RecebimentoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if ((obj.getMatriculaAluno().getMatricula() == null) || (obj.getMatriculaAluno().getMatricula().equals(""))) {
            obj.setMatriculaAluno(new MatriculaVO());
            return;
        }
        obj.setMatriculaAluno(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaAluno().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CentroReceitaVO</code> relacionado ao
     * objeto <code>RecebimentoVO</code>. Faz uso da chave primária da classe <code>CentroReceitaVO</code> para realizar
     * a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCentroReceita(RecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceita().getCodigo().intValue() == 0) {
            obj.setCentroReceita(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceita(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceita().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao
     * objeto <code>RecebimentoVO</code>. Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para realizar
     * a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaCorrente(RecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrente().getCodigo().intValue() == 0) {
            obj.setContaCorrente(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RecebimentoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Recebimento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados  ( Recebimento ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Recebimento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Recebimento.idEntidade = idEntidade;
    }
    
    
    @Override
    public Date consultarDataVencimentoPrevistaCartaoCredito(Integer codigoContaReceber,Integer codigoFormaPagamentoNegociacaoRecebimento) throws Exception{
    	try {
    		StringBuilder sql = new StringBuilder();
    		sql.append(" SELECT distinct formapagamentonegociacaorecebimentocartaocredito.datavencimento");
    		sql.append(" FROM contareceberrecebimento ");
    		sql.append(" INNER JOIN formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = contareceberrecebimento.formapagamentonegociacaorecebimento");
    		sql.append(" INNER JOIN formapagamentonegociacaorecebimentocartaocredito on formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo");
    		sql.append(" WHERE contareceber = ").append(codigoContaReceber);
    		sql.append(" AND formapagamentonegociacaorecebimento.codigo = ").append(codigoFormaPagamentoNegociacaoRecebimento);
    		sql.append(" ORDER BY formapagamentonegociacaorecebimentocartaocredito.datavencimento");
    		sql.append(" LIMIT 1");
    		sql.append("");
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
            if (!tabelaResultado.next()) {
                throw new ConsistirException("Dados Não Encontrados  ( DataVencimento Cartão de Crédito ).");
            }
            return tabelaResultado.getDate("datavencimento");
			
		} catch (Exception e) {
			throw e;
		}
    	
    }
    
    @Override
	public List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentoCartaoRelatorio(FormaPagamentoNegociacaoRecebimentoVO formaPgto, ComprovanteRecebimentoRelVO comprovante) throws Exception {
		try {
			List<FormaPagamentoNegociacaoRecebimentoVO> lista = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
			StringBuilder sql = new StringBuilder();
			sql.append(" select distinct formapagamentonegociacaorecebimentocartaocredito.* from formapagamentonegociacaorecebimento ");
			sql.append(" inner join formapagamentonegociacaorecebimentocartaocredito  on formapagamentonegociacaorecebimento.codigo = formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento ");
			sql.append(" where formapagamentonegociacaorecebimento.codigo = ").append(formaPgto.getCodigo());
			sql.append(" union all ");
			sql.append(" select distinct formapagamentonegociacaorecebimentocartaocredito.* from formapagamentonegociacaorecebimento ");
			sql.append(" inner join formapagamentonegociacaorecebimentocartaocredito  on formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito = formapagamentonegociacaorecebimentocartaocredito.codigo ");
			sql.append(" where formapagamentonegociacaorecebimento.codigo =  ").append(formaPgto.getCodigo());

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while (tabelaResultado.next()) {
				FormaPagamentoNegociacaoRecebimentoVO obj = new FormaPagamentoNegociacaoRecebimentoVO();
				obj.setCidadeDataRecebimentoPorExtenso(comprovante.getCidadeDataRecebimentoPorExtenso());
				obj.setNomeResponsavel(comprovante.getNomeResponsavel());
				obj.setFormaPagamento(formaPgto.getFormaPagamento());
				obj.setValorRecebimento(tabelaResultado.getDouble("valorparcela"));
				obj.setDataEmissaoCartao(tabelaResultado.getDate("dataemissao"));
				obj.setDataPrevisaCartao(tabelaResultado.getDate("datavencimento"));
				lista.add(obj);
			}
			return lista;
		} catch (Exception e) {
			throw e;
		}
	}
    
    @Override
	public List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentoCartaoRelatorioDCC(FormaPagamentoNegociacaoRecebimentoVO formaPgto, ComprovanteRecebimentoRelVO comprovante) throws Exception {
		try {
			List<FormaPagamentoNegociacaoRecebimentoVO> lista = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
			StringBuilder sql = new StringBuilder();
			sql.append(" select distinct formapagamentonegociacaorecebimentocartaocredito.* from formapagamentonegociacaorecebimentodcc ");
			sql.append(" inner join formapagamentonegociacaorecebimentocartaocredito  on formapagamentonegociacaorecebimentodcc.formapagamentonegociacaorecebimentocartaocredito = formapagamentonegociacaorecebimentocartaocredito.codigo ");
			sql.append(" where formapagamentonegociacaorecebimentodcc.codigo =  ").append(formaPgto.getCodigo());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while (tabelaResultado.next()) {
				FormaPagamentoNegociacaoRecebimentoVO obj = new FormaPagamentoNegociacaoRecebimentoVO();
				obj.setCidadeDataRecebimentoPorExtenso(comprovante.getCidadeDataRecebimentoPorExtenso());
				obj.setNomeResponsavel(comprovante.getNomeResponsavel());
				obj.setFormaPagamento(formaPgto.getFormaPagamento());
				obj.setValorRecebimento(tabelaResultado.getDouble("valorparcela"));
				obj.setDataEmissaoCartao(tabelaResultado.getDate("dataemissao"));
				obj.setDataPrevisaCartao(tabelaResultado.getDate("datavencimento"));
				lista.add(obj);
			}
			return lista;
		} catch (Exception e) {
			throw e;
		}
	}
}
