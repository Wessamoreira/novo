package negocio.facade.jdbc.processosel;

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
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioAlunoVO;
import negocio.comuns.processosel.RespostaPerguntaAlunoVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.RespostaPerguntaAlunoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RespostaPerguntaAluno extends ControleAcesso implements RespostaPerguntaAlunoInterfaceFacade {

    protected static String idEntidade;

    public RespostaPerguntaAluno() throws Exception {
        super();
        setIdEntidade("RespostaPerguntaAluno");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RespostaPerguntaAlunoVO obj) throws Exception {

        RespostaPerguntaAlunoVO.validarDados(obj);
        try {
            final String sql = "INSERT INTO RespostaPerguntaAluno(questionarioAluno, respostapergunta,  pergunta , texto, tipoResposta) VALUES (  ?, ?, ?, ?, ?  ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    if (obj.getQuestionarioAluno().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getQuestionarioAluno().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getRespostaQuestionarioAluno().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getRespostaQuestionarioAluno().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getPerguntaQuestionario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getPerguntaQuestionario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setString(4, obj.getTexto());
                    sqlInserir.setString(5, obj.getTipoResposta());
                    return sqlInserir;

                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RespostaPerguntaAlunoVO obj) throws Exception {

        RespostaPerguntaAlunoVO.validarDados(obj);

        final String sql = "UPDATE RespostaPerguntaAluno set questionarioAluno=?, respostapergunta=?, pergunta=?, texto=?, tipoResposta=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getQuestionarioAluno().intValue());
                sqlAlterar.setInt(2, obj.getRespostaQuestionarioAluno().getCodigo().intValue());
                sqlAlterar.setInt(3, obj.getPerguntaQuestionario().getCodigo().intValue());
                sqlAlterar.setString(4, obj.getTexto());
                sqlAlterar.setString(5, obj.getTipoResposta());
                sqlAlterar.setInt(6, obj.getCodigo());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RespostaPerguntaAlunoVO obj) throws Exception {
        RespostaPerguntaAluno.excluir(getIdEntidade());
        String sql = "DELETE FROM RespostaPerguntaAluno WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirRespostaPerguntaAluno(QuestionarioAlunoVO questionarioAluno) throws Exception {
        Iterator e = questionarioAluno.getListaRespostaPerguntaAlunoVO().iterator();
        while (e.hasNext()) {
            RespostaPerguntaAlunoVO obj = (RespostaPerguntaAlunoVO) e.next();
            obj.setQuestionarioAluno(questionarioAluno.getCodigo());
            incluir(obj);
        }
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirRespostaPerguntaAluno(QuestionarioAlunoVO questionarioAluno) throws Exception {
        Iterator e = questionarioAluno.getListaRespostaPerguntaAlunoVO().iterator();
        while (e.hasNext()) {
            RespostaPerguntaAlunoVO obj = (RespostaPerguntaAlunoVO) e.next();
            excluir(obj);
        }
    }

    public List<RespostaPerguntaAlunoVO> consultarPorQuestionarioAluno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM RespostaPerguntaAluno WHERE questionarioAluno = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));
    }

    public RespostaPerguntaAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        String sql = "SELECT * FROM RespostaPerguntaAluno WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RespostaPerguntaAluno!).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            RespostaPerguntaAlunoVO obj = new RespostaPerguntaAlunoVO();
            obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public static RespostaPerguntaAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            RespostaPerguntaAlunoVO obj = new RespostaPerguntaAlunoVO();
            obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
            obj.setTexto(dadosSQL.getString("texto"));
            obj.setTipoResposta(dadosSQL.getString("tiporesposta"));
            obj.setNovoObj(Boolean.FALSE);
            obj.setQuestionarioAluno(dadosSQL.getInt("questionarioaluno"));
            obj.getPerguntaQuestionario().setCodigo(dadosSQL.getInt("pergunta"));
            obj.getRespostaQuestionarioAluno().setCodigo(dadosSQL.getInt("respostapergunta"));
            if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
                return obj;
            }

            montarDadosRespostaPergunta(obj, nivelMontarDados, usuario);
            montarDadosPergunta(obj, nivelMontarDados, usuario);
            return obj;
        } catch (Exception e) {
            throw e;
        }
    }


    public static void montarDadosRespostaPergunta(RespostaPerguntaAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getRespostaQuestionarioAluno().getCodigo().intValue() == 0) {
            obj.setRespostaQuestionarioAluno(new RespostaPerguntaVO());
            return;
        }
        obj.setRespostaQuestionarioAluno(getFacadeFactory().getRespostaPerguntaFacade().consultarPorChavePrimaria(obj.getRespostaQuestionarioAluno().getCodigo().intValue(), nivelMontarDados, usuario));
    }

    public static void montarDadosPergunta(RespostaPerguntaAlunoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPerguntaQuestionario().getCodigo().intValue() == 0) {
            obj.setPerguntaQuestionario(new PerguntaVO());
            return;
        }
        obj.setPerguntaQuestionario(getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(obj.getPerguntaQuestionario().getCodigo(), nivelMontarDados, usuario));
    }

    public static String getIdEntidade() {
        return RespostaPerguntaAluno.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        RespostaPerguntaAluno.idEntidade = idEntidade;
    }
}
