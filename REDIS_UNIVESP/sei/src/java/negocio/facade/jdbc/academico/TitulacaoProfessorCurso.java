/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.TitulacaoProfessorCursoVO;
import negocio.comuns.academico.TitulacaoQuantidadeFuncionariosVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TitulacaoProfessorCursoInterfaceFacade;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class TitulacaoProfessorCurso extends ControleAcesso implements TitulacaoProfessorCursoInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public TitulacaoProfessorCurso() throws Exception {
        super();
        setIdEntidade("TitulacaoProfessorCurso");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TitulacaoProfessorCursoVO obj) throws Exception {
        try {
            validarDados(obj);
            final String sql = "INSERT INTO TitulacaoProfessorCurso(curso, quantidadeTecnico,quantidadeGraduacao,quantidadeEspecializacao,quantidadeMestrado,quantidadeDoutorado,quantidadePosDoutorado) VALUES (?,?,?,?,?,?,?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getTurma().getCurso().getCodigo());
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
    public void alterar(final TitulacaoProfessorCursoVO obj) throws Exception {
        try {
            validarDados(obj);
            TitulacaoProfessorCurso.alterar(getIdEntidade());
            final String sql = "UPDATE TitulacaoProfessorCurso SET (curso=?, quantidadeTecnico=?,quantidadeGraduacao=?,quantidadeEspecializacao=?,quantidadeMestrado=?,quantidadeDoutorado=?,quantidadePosDoutorado=?) WHERE codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getTurma().getCurso().getCodigo());
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
    public void excluir(TitulacaoProfessorCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            TitulacaoProfessorCurso.excluir(getIdEntidade());
            String sql = "DELETE FROM TitulacaoProfessorCurso WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public List consultar(String campoConsulta, String valorConsulta, UsuarioVO usuario) throws Exception {
        List objs = new ArrayList(0);
        if (campoConsulta.equals("nomeCurso")) {
            objs = consultarPorNomeCurso(valorConsulta, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        }
        return objs;
    }

    private void validarDados(TitulacaoProfessorCursoVO obj) throws ConsistirException {
        if (obj.getTurma().getCurso().getCodigo() == 0) {
            throw new ConsistirException("Deve ser selecionado um curso.");
        }
    }

    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT curso.codigo AS curso_codigo, curso.nome AS curso_nome, tpc.codigo AS tpc_codigo, tpc.quantidadeTecnico as tpc_quantidadeTecnico, "
                + "tpc.quantidadeGraduacao as tpc_quantidadeGraduacao, tpc.quantidadeEspecializacao as tpc_quantidadeEspecializacao, tpc.quantidadeMestrado as tpc_quantidadeMestrado,"
                + "tpc.quantidadeDoutorado as tpc_quantidadeDoutorado, tpc.quantidadePosDoutorado as tpc_quantidadePosDoutorado  "
                + "FROM TitulacaoProfessorCurso AS tpc "
                + "INNER JOIN curso ON curso.codigo = tpc.curso "
                + "WHERE lower(curso.nome) like '" + valorConsulta.toLowerCase() + "%'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public TitulacaoProfessorCursoVO consultarPorChavePrimaria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT curso.codigo AS curso_codigo, curso.nome AS curso_nome,"
                + "tpc.codigo AS tpc_codigo,"
                + "tpc.quantidadeTecnico AS tpc_quantidadeTecnico, "
                + "tpc.quantidadeGraduacao AS tpc_quantidadeGraduacao, "
                + "tpc.quantidadeEspecializacao AS tpc_quantidadeEspecializacao, "
                + "tpc.quantidadeMestrado AS tpc_quantidadeMestrado, "
                + "tpc.quantidadeDoutorado AS tpc_quantidadeDoutorado, "
                + "tpc.quantidadePosDoutorado AS tpc_quantidadePosDoutorado "
                + "FROM TitulacaoProfessorCurso AS tpc "
                + "INNER JOIN curso ON curso.codigo = tpc.curso "
                + "WHERE tpc.codigo = " + valorConsulta;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return montarDadosPorCurso(tabelaResultado, nivelMontarDados, usuario);
        }
        return new TitulacaoProfessorCursoVO();
    }

    public TitulacaoProfessorCursoVO consultarTitulacaoProfessorCursoPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT turma.codigo AS turma_codigo, turma.identificadorturma AS turma_identificadorturma,"
                + "tpc.codigo AS tpc_codigo,"
                + "tpc.quantidadeTecnico AS tpc_quantidadeTecnico, "
                + "tpc.quantidadeGraduacao AS tpc_quantidadeGraduacao, "
                + "tpc.quantidadeEspecializacao AS tpc_quantidadeEspecializacao, "
                + "tpc.quantidadeMestrado AS tpc_quantidadeMestrado, "
                + "tpc.quantidadeDoutorado AS tpc_quantidadeDoutorado, "
                + "tpc.quantidadePosDoutorado AS tpc_quantidadePosDoutorado "
                + "FROM TitulacaoProfessorCurso AS tpc "
                + "INNER JOIN curso ON curso.codigo = tpc.curso "
                + "INNER JOIN turma ON turma.curso = curso.codigo "
                + "WHERE turma.codigo = " + valorConsulta + " ORDER BY tpc.codigo desc ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return montarDadosPorTurma(tabelaResultado, nivelMontarDados, usuario);
        }
        return new TitulacaoProfessorCursoVO();
    }

    public TitulacaoProfessorCursoVO consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT curso.codigo AS curso_codigo, curso.nome AS curso_nome,"
                + "tpc.codigo AS tpc_codigo,"
                + "tpc.quantidadeTecnico AS tpc_quantidadeTecnico, "
                + "tpc.quantidadeGraduacao AS tpc_quantidadeGraduacao, "
                + "tpc.quantidadeEspecializacao AS tpc_quantidadeEspecializacao, "
                + "tpc.quantidadeMestrado AS tpc_quantidadeMestrado, "
                + "tpc.quantidadeDoutorado AS tpc_quantidadeDoutorado, "
                + "tpc.quantidadePosDoutorado AS tpc_quantidadePosDoutorado "
                + "FROM TitulacaoProfessorCurso AS tpc "
                + "INNER JOIN curso ON curso.codigo = tpc.curso "
                + "WHERE curso.codigo = " + valorConsulta;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return montarDadosPorCurso(tabelaResultado, nivelMontarDados, usuario);
        }
        return new TitulacaoProfessorCursoVO();
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosPorCurso(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public static TitulacaoProfessorCursoVO montarDadosPorTurma(SqlRowSet dadosSQL, Integer nivelMontarDados, UsuarioVO usuario) {
        TitulacaoProfessorCursoVO obj = new TitulacaoProfessorCursoVO();
        obj.setCodigo(dadosSQL.getInt("tpc_codigo"));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma_codigo"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma_identificadorturma"));
        obj.setQuantidadeTecnico(dadosSQL.getInt("tpc_quantidadeTecnico"));
        obj.setQuantidadeGraduacao(dadosSQL.getInt("tpc_quantidadeGraduacao"));
        obj.setQuantidadeEspecializacao(dadosSQL.getInt("tpc_quantidadeEspecializacao"));
        obj.setQuantidadeMestrado(dadosSQL.getInt("tpc_quantidadeMestrado"));
        obj.setQuantidadeDoutorado(dadosSQL.getInt("tpc_quantidadeDoutorado"));
        obj.setQuantidadePosDoutorado(dadosSQL.getInt("tpc_quantidadePosDoutorado"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        //Dados Titulacao QuantidadeFuncionarios
        montarDadosTitulacaoQuantidadeFuncionarios(dadosSQL, obj);
        return obj;
    }

    public static TitulacaoProfessorCursoVO montarDadosPorCurso(SqlRowSet dadosSQL, Integer nivelMontarDados, UsuarioVO usuario) {
        TitulacaoProfessorCursoVO obj = new TitulacaoProfessorCursoVO();
        obj.setCodigo(dadosSQL.getInt("tpc_codigo"));
        obj.getTurma().getCurso().setCodigo(dadosSQL.getInt("curso_codigo"));
        obj.getTurma().getCurso().setNome(dadosSQL.getString("curso_nome"));
        obj.setQuantidadeTecnico(dadosSQL.getInt("tpc_quantidadeTecnico"));
        obj.setQuantidadeGraduacao(dadosSQL.getInt("tpc_quantidadeGraduacao"));
        obj.setQuantidadeEspecializacao(dadosSQL.getInt("tpc_quantidadeEspecializacao"));
        obj.setQuantidadeMestrado(dadosSQL.getInt("tpc_quantidadeMestrado"));
        obj.setQuantidadeDoutorado(dadosSQL.getInt("tpc_quantidadeDoutorado"));
        obj.setQuantidadePosDoutorado(dadosSQL.getInt("tpc_quantidadePosDoutorado"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        //Dados Titulacao QuantidadeFuncionarios
        montarDadosTitulacaoQuantidadeFuncionarios(dadosSQL, obj);
        return obj;
    }

    public static void montarDadosTitulacaoQuantidadeFuncionarios(SqlRowSet dadosSQL, TitulacaoProfessorCursoVO obj) {
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

    public void preencherVagasTitulacaoProfessorCurso(List<PessoaVO> listaProfessores, TitulacaoProfessorCursoVO titulacaoProfessorCursoVO) {
        for (PessoaVO professor : listaProfessores) {
            Boolean passarProximoProfessor = false;
            if (titulacaoProfessorCursoVO.getCodigo() != 0) {
                for (FormacaoAcademicaVO formacaoAcademicaVO : professor.getFormacaoAcademicaVOs()) {
                    if (formacaoAcademicaVO.getEscolaridade().equals("PD")) {
                        if (titulacaoProfessorCursoVO.getQuantidadePosDoutorado() > 0) {
                            titulacaoProfessorCursoVO.setQuantidadePosDoutorado(titulacaoProfessorCursoVO.getQuantidadePosDoutorado() - 1);
                            titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().clear();
                            preencherListaTitulacaoQuantidadeFuncionarios(titulacaoProfessorCursoVO);
                            passarProximoProfessor = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
                if (passarProximoProfessor) {
                    continue;
                }
                for (FormacaoAcademicaVO formacaoAcademicaVO : professor.getFormacaoAcademicaVOs()) {
                    if (formacaoAcademicaVO.getEscolaridade().equals("DR")) {
                        if (titulacaoProfessorCursoVO.getQuantidadeDoutorado() > 0) {
                            titulacaoProfessorCursoVO.setQuantidadeDoutorado(titulacaoProfessorCursoVO.getQuantidadeDoutorado() - 1);
                            titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().clear();
                            preencherListaTitulacaoQuantidadeFuncionarios(titulacaoProfessorCursoVO);
                            passarProximoProfessor = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
                if (passarProximoProfessor) {
                    continue;
                }
                for (FormacaoAcademicaVO formacaoAcademicaVO : professor.getFormacaoAcademicaVOs()) {
                    if (formacaoAcademicaVO.getEscolaridade().equals("MS")) {
                        if (titulacaoProfessorCursoVO.getQuantidadeMestrado() > 0) {
                            titulacaoProfessorCursoVO.setQuantidadeMestrado(titulacaoProfessorCursoVO.getQuantidadeMestrado() - 1);
                            titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().clear();
                            preencherListaTitulacaoQuantidadeFuncionarios(titulacaoProfessorCursoVO);
                            passarProximoProfessor = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
                if (passarProximoProfessor) {
                    continue;
                }
                for (FormacaoAcademicaVO formacaoAcademicaVO : professor.getFormacaoAcademicaVOs()) {
                    if (formacaoAcademicaVO.getEscolaridade().equals("EP")) {
                        if (titulacaoProfessorCursoVO.getQuantidadeEspecializacao() > 0) {
                            titulacaoProfessorCursoVO.setQuantidadeEspecializacao(titulacaoProfessorCursoVO.getQuantidadeEspecializacao() - 1);
                            titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().clear();
                            preencherListaTitulacaoQuantidadeFuncionarios(titulacaoProfessorCursoVO);
                            passarProximoProfessor = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
                if (passarProximoProfessor) {
                    continue;
                }
                for (FormacaoAcademicaVO formacaoAcademicaVO : professor.getFormacaoAcademicaVOs()) {
                    if (formacaoAcademicaVO.getEscolaridade().equals("GR")) {
                        if (titulacaoProfessorCursoVO.getQuantidadeGraduacao() > 0) {
                            titulacaoProfessorCursoVO.setQuantidadeGraduacao(titulacaoProfessorCursoVO.getQuantidadeGraduacao() - 1);
                            titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().clear();
                            preencherListaTitulacaoQuantidadeFuncionarios(titulacaoProfessorCursoVO);
                            passarProximoProfessor = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
                if (passarProximoProfessor) {
                    continue;
                }
                for (FormacaoAcademicaVO formacaoAcademicaVO : professor.getFormacaoAcademicaVOs()) {
                    if (formacaoAcademicaVO.getEscolaridade().equals("TE")) {
                        if (titulacaoProfessorCursoVO.getQuantidadeTecnico() > 0) {
                            titulacaoProfessorCursoVO.setQuantidadeTecnico(titulacaoProfessorCursoVO.getQuantidadeTecnico() - 1);
                            titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().clear();
                            preencherListaTitulacaoQuantidadeFuncionarios(titulacaoProfessorCursoVO);
                            passarProximoProfessor = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
                if (passarProximoProfessor) {
                    continue;
                }
            }
        }
    }

    public void removerVagasTitulacaoProfessorCurso(List<PessoaVO> listaProfessores, Integer professorExcluido, TitulacaoProfessorCursoVO titulacaoProfessorCursoVO) {
        for (int i = 0; i < listaProfessores.size(); i++) {
            PessoaVO professor = listaProfessores.get(i);
            if (professor.getCodigo().equals(professorExcluido)) {
                listaProfessores.remove(professor);
            }
        }
    }

    public void preencherListaTitulacaoQuantidadeFuncionarios(TitulacaoProfessorCursoVO titulacaoProfessorCursoVO) {
        TitulacaoQuantidadeFuncionariosVO obj1 = new TitulacaoQuantidadeFuncionariosVO();
        obj1.setTitulacao("tecnico");
        obj1.setQuantidadeFuncionarios(titulacaoProfessorCursoVO.getQuantidadeTecnico());
        titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().add(obj1);
        TitulacaoQuantidadeFuncionariosVO obj2 = new TitulacaoQuantidadeFuncionariosVO();
        obj2.setTitulacao("graduacao");
        obj2.setQuantidadeFuncionarios(titulacaoProfessorCursoVO.getQuantidadeGraduacao());
        titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().add(obj2);
        TitulacaoQuantidadeFuncionariosVO obj3 = new TitulacaoQuantidadeFuncionariosVO();
        obj3.setTitulacao("especializacao");
        obj3.setQuantidadeFuncionarios(titulacaoProfessorCursoVO.getQuantidadeEspecializacao());
        titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().add(obj3);
        TitulacaoQuantidadeFuncionariosVO obj4 = new TitulacaoQuantidadeFuncionariosVO();
        obj4.setTitulacao("mestrado");
        obj4.setQuantidadeFuncionarios(titulacaoProfessorCursoVO.getQuantidadeMestrado());
        titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().add(obj4);
        TitulacaoQuantidadeFuncionariosVO obj5 = new TitulacaoQuantidadeFuncionariosVO();
        obj5.setTitulacao("doutorado");
        obj5.setQuantidadeFuncionarios(titulacaoProfessorCursoVO.getQuantidadeDoutorado());
        titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().add(obj5);
        TitulacaoQuantidadeFuncionariosVO obj6 = new TitulacaoQuantidadeFuncionariosVO();
        obj6.setTitulacao("posDoutorado");
        obj6.setQuantidadeFuncionarios(titulacaoProfessorCursoVO.getQuantidadePosDoutorado());
        titulacaoProfessorCursoVO.getListaTitulacaoQuantidadeFuncionarios().add(obj6);
    }
}
