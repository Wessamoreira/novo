/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

import negocio.comuns.academico.TitulacaoProfessorCursoPendenteVO;
import negocio.comuns.academico.TitulacaoProfessorCursoVO;
import negocio.comuns.academico.TitulacaoQuantidadeFuncionariosVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TitulacaoProfessorCursoPendenteInterfaceFacade;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class TitulacaoProfessorCursoPendente extends ControleAcesso implements TitulacaoProfessorCursoPendenteInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public TitulacaoProfessorCursoPendente() throws Exception {
        super();
        setIdEntidade("TitulacaoProfessorCursoPendente");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirTitulacaoProfessorCursoPendente(final TitulacaoProfessorCursoVO obj, final Integer codigoTurma) throws Exception {
        try {
            final String sql = "INSERT INTO TitulacaoProfessorCursoPendente(turma, quantidadeTecnico,quantidadeGraduacao,quantidadeEspecializacao,quantidadeMestrado,quantidadeDoutorado,quantidadePosDoutorado) VALUES (?,?,?,?,?,?,?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, codigoTurma);
                    sqlInserir.setInt(2, obj.getQuantidadeTecnico());
                    sqlInserir.setInt(3, obj.getQuantidadeGraduacao());
                    sqlInserir.setInt(4, obj.getQuantidadeEspecializacao());
                    sqlInserir.setInt(5, obj.getQuantidadeMestrado());
                    sqlInserir.setInt(6, obj.getQuantidadeDoutorado());
                    sqlInserir.setInt(7, obj.getQuantidadePosDoutorado());
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
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarTitulacaoProfessorCursoPendente(final TitulacaoProfessorCursoPendenteVO obj) throws Exception {
        try {
            TitulacaoProfessorCurso.alterar(getIdEntidade());
            final String sql = "UPDATE TitulacaoProfessorCursoPendente SET (turma=?, quantidadeTecnico=?,quantidadeGraduacao=?,quantidadeEspecializacao=?,quantidadeMestrado=?,quantidadeDoutorado=?,quantidadePosDoutorado=?) WHERE codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getTurma().getCodigo());
                    sqlAlterar.setInt(2, obj.getQuantidadeTecnico());
                    sqlAlterar.setInt(3, obj.getQuantidadeGraduacao());
                    sqlAlterar.setInt(4, obj.getQuantidadeEspecializacao());
                    sqlAlterar.setInt(5, obj.getQuantidadeMestrado());
                    sqlAlterar.setInt(6, obj.getQuantidadeDoutorado());
                    sqlAlterar.setInt(7, obj.getQuantidadePosDoutorado());
                    sqlAlterar.setInt(8, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirTitulacaoProfessorCursoPendente(TitulacaoProfessorCursoPendenteVO obj, UsuarioVO usuario) throws Exception {
        try {
            TitulacaoProfessorCurso.excluir(getIdEntidade());
            String sql = "DELETE FROM TitulacaoProfessorCursoPendente WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public TitulacaoProfessorCursoPendenteVO consultarTitulacaoProfessorCursoPendentePorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT turma.codigo AS turma_codigo, turma.identificadorturma AS turma_identificadorturma, "
                + "tpc.codigo AS tpc_codigo, "
                + "tpc.quantidadeTecnico AS tpc_quantidadeTecnico, "
                + "tpc.quantidadeGraduacao AS tpc_quantidadeGraduacao, "
                + "tpc.quantidadeEspecializacao AS tpc_quantidadeEspecializacao, "
                + "tpc.quantidadeMestrado AS tpc_quantidadeMestrado, "
                + "tpc.quantidadeDoutorado AS tpc_quantidadeDoutorado, "
                + "tpc.quantidadePosDoutorado AS tpc_quantidadePosDoutorado "
                + "FROM TitulacaoProfessorCursoPendente AS tpc "
                + "INNER JOIN turma ON turma.codigo = tpc.turma "
                + "WHERE turma.codigo = " + valorConsulta + " ORDER BY tpc.codigo desc ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, nivelMontarDados, usuario);
        } else {
            return new TitulacaoProfessorCursoPendenteVO();
        }
    }

    public static TitulacaoProfessorCursoPendenteVO montarDados(SqlRowSet dadosSQL, Integer nivelMontarDados, UsuarioVO usuario) {
        TitulacaoProfessorCursoPendenteVO obj = new TitulacaoProfessorCursoPendenteVO();
        obj.setCodigo(dadosSQL.getInt("tpc_codigo"));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma_codigo"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma_identificadorturma"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        //Dados Titulacao QuantidadeFuncionarios
        montarDadosTitulacaoQuantidadeFuncionarios(dadosSQL, obj);
        return obj;
    }

    public static void montarDadosTitulacaoQuantidadeFuncionarios(SqlRowSet dadosSQL, TitulacaoProfessorCursoPendenteVO obj) {
        TitulacaoQuantidadeFuncionariosVO obj1 = new TitulacaoQuantidadeFuncionariosVO();
        obj1.setTitulacao("tecnico");
        obj1.setQuantidadeFuncionarios(dadosSQL.getInt("tpc_quantidadeTecnico"));
        obj.getListaTitulacaoQuantidadeFuncionarios().add(obj1);
        TitulacaoQuantidadeFuncionariosVO obj2 = new TitulacaoQuantidadeFuncionariosVO();
        obj2.setTitulacao("graduacao");
        obj2.setQuantidadeFuncionarios(dadosSQL.getInt("tpc_quantidadeGraduacao"));
        obj.getListaTitulacaoQuantidadeFuncionarios().add(obj2);
        TitulacaoQuantidadeFuncionariosVO obj3 = new TitulacaoQuantidadeFuncionariosVO();
        obj3.setTitulacao("especializacao");
        obj3.setQuantidadeFuncionarios(dadosSQL.getInt("tpc_quantidadeEspecializacao"));
        obj.getListaTitulacaoQuantidadeFuncionarios().add(obj3);
        TitulacaoQuantidadeFuncionariosVO obj4 = new TitulacaoQuantidadeFuncionariosVO();
        obj4.setTitulacao("mestrado");
        obj4.setQuantidadeFuncionarios(dadosSQL.getInt("tpc_quantidadeMestrado"));
        obj.getListaTitulacaoQuantidadeFuncionarios().add(obj4);
        TitulacaoQuantidadeFuncionariosVO obj5 = new TitulacaoQuantidadeFuncionariosVO();
        obj5.setTitulacao("doutorado");
        obj5.setQuantidadeFuncionarios(dadosSQL.getInt("tpc_quantidadeDoutorado"));
        obj.getListaTitulacaoQuantidadeFuncionarios().add(obj5);
        TitulacaoQuantidadeFuncionariosVO obj6 = new TitulacaoQuantidadeFuncionariosVO();
        obj6.setTitulacao("posDoutorado");
        obj6.setQuantidadeFuncionarios(dadosSQL.getInt("tpc_quantidadePosDoutorado"));
        obj.getListaTitulacaoQuantidadeFuncionarios().add(obj6);
    }
}
