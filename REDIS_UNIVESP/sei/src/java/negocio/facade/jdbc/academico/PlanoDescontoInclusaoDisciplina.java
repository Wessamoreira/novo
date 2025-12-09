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

import negocio.comuns.academico.PlanoDescontoInclusaoDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PlanoDescontoInclusaoDisciplinaInterfaceFacade;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class PlanoDescontoInclusaoDisciplina extends ControleAcesso implements PlanoDescontoInclusaoDisciplinaInterfaceFacade {

    private static String idEntidade;

    public PlanoDescontoInclusaoDisciplina() {
        super();
        setIdEntidade("PlanoDescontoInclusaoDisciplina");
    }

    public void persistir(PlanoDescontoInclusaoDisciplinaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            incluir(obj, usuario);
        } else {
            alterar(obj, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoDescontoInclusaoDisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            validarDadosUnicidade(obj.getDescricao());
            PlanoDescontoInclusaoDisciplina.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO PlanoDescontoInclusaoDisciplina( descricao, valor, situacao, dataAtivacao, dataInativacao, responsavelAtivacao, responsavelInativacao ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setDouble(2, obj.getValor());
                    sqlInserir.setString(3, obj.getSituacao());
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataAtivacao()));
                    sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataInativacao()));
                    if (obj.getResponsavelAtivacao() != null && obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getResponsavelAtivacao().getCodigo());
                    } else {
                        sqlInserir.setInt(6, 0);
                    }
                    if (obj.getResponsavelInativacao() != null && obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getResponsavelInativacao().getCodigo());
                    } else {
                        sqlInserir.setInt(7, 0);
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
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoDescontoInclusaoDisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            validarDadosUnicidade(obj.getDescricao());
            PlanoDescontoInclusaoDisciplina.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE PlanoDescontoInclusaoDisciplina set descricao=?, valor=?, situacao=?, dataAtivacao=?, dataInativacao=?, responsavelAtivacao=?, responsavelInativacao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setDouble(2, obj.getValor());
                    sqlAlterar.setString(3, obj.getSituacao());
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataAtivacao()));
                    sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataInativacao()));
                    if (obj.getResponsavelAtivacao() != null && obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getResponsavelAtivacao().getCodigo());
                    } else {
                        sqlAlterar.setInt(6, 0);
                    }
                    if (obj.getResponsavelInativacao() != null && obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getResponsavelInativacao().getCodigo());
                    } else {
                        sqlAlterar.setInt(7, 0);
                    }

                    sqlAlterar.setInt(8, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PlanoDescontoInclusaoDisciplinaVO obj, UsuarioVO usuario) throws Exception {
        try {
            PlanoDescontoInclusaoDisciplina.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM PlanoDescontoInclusaoDisciplina WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarDados(PlanoDescontoInclusaoDisciplinaVO obj) throws Exception {
        if (obj.getDescricao() == null || obj.getDescricao().equals("")) {
            throw new Exception("O campo DESCRIÇÃO deve ser informado.");
        }
        if (obj.getValor() == null || obj.getValor() == 0.0) {
            throw new Exception("O campo VALOR deve ser informado.");
        }
    }

    public List<PlanoDescontoInclusaoDisciplinaVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuarioLogado) throws Exception {
        List objs = new ArrayList(0);
        try {
            if (campoConsulta.equals("codigo")) {
                if (valorConsulta.equals("")) {
                    valorConsulta = ("0");
                }
                if (valorConsulta.trim() != null || !valorConsulta.trim().isEmpty()) {
                    Uteis.validarSomenteNumeroString(valorConsulta.trim());
                }
                int valorInt = Integer.parseInt(valorConsulta);
                return objs = consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
            }
            if (campoConsulta.equals("descricao")) {
                return objs = consultarPorDescricao(valorConsulta, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
            }
            return new ArrayList(0);
        } finally {
        }

    }

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM PlanoDescontoInclusaoDisciplina WHERE codigo = ");
        sqlStr.append(valorConsulta.intValue());
        sqlStr.append(" ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PlanoDescontoInclusaoDisciplinaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM PlanoDescontoInclusaoDisciplina WHERE lower (descricao) like('");
        sqlStr.append(valorConsulta.toString());
        sqlStr.append("%') ORDER BY descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<PlanoDescontoInclusaoDisciplinaVO> consultarPorSituacao(String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM PlanoDescontoInclusaoDisciplina WHERE situacao = '");
        sqlStr.append(situacao.toString());
        sqlStr.append("' ORDER BY descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public PlanoDescontoInclusaoDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM PlanoDescontoInclusaoDisciplina WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Disciplina).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }


    public static List<PlanoDescontoInclusaoDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        try {
            List<PlanoDescontoInclusaoDisciplinaVO> vetResultado = new ArrayList<PlanoDescontoInclusaoDisciplinaVO>(0);
            while (tabelaResultado.next()) {
                vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
            }
            return vetResultado;
        } catch (Exception e) {
            throw e;
        }
    }

    public static PlanoDescontoInclusaoDisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PlanoDescontoInclusaoDisciplinaVO obj = new PlanoDescontoInclusaoDisciplinaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setValor(dadosSQL.getDouble("valor"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
        obj.setDataAtivacao(dadosSQL.getDate("dataInativacao"));
        obj.getResponsavelAtivacao().setCodigo(dadosSQL.getInt("responsavelAtivacao"));
        obj.getResponsavelInativacao().setCodigo(dadosSQL.getInt("responsavelInativacao"));
        return obj;
    }

    public void realizarAtivacaoPlanoDescontoInclusaoDisciplina(PlanoDescontoInclusaoDisciplinaVO planoDescontoInclusaoDisciplinaVO, UsuarioVO usuario) throws Exception {
        if (planoDescontoInclusaoDisciplinaVO.getSituacao().equals("CO")) {
            planoDescontoInclusaoDisciplinaVO.setSituacao("AT");
            realizarAlterarPlanoDescontoParaAtivo(planoDescontoInclusaoDisciplinaVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarInativacaoPlanoDescontoInclusaoDisciplina(PlanoDescontoInclusaoDisciplinaVO planoDescontoInclusaoDisciplinaVO, UsuarioVO usuario) throws Exception {
        if (planoDescontoInclusaoDisciplinaVO.getSituacao().equals("AT")) {
            planoDescontoInclusaoDisciplinaVO.setSituacao("FI");
            realizarAlterarPlanoDescontoParaAtivo(planoDescontoInclusaoDisciplinaVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAlterarPlanoDescontoParaAtivo(final PlanoDescontoInclusaoDisciplinaVO obj) throws Exception {
        final String sql = "UPDATE PlanoDescontoInclusaoDisciplina set situacao=?, dataAtivacao=?, responsavelAtivacao=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getSituacao());
                sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataAtivacao()));
                sqlAlterar.setInt(3, obj.getResponsavelAtivacao().getCodigo());
                sqlAlterar.setInt(4, obj.getCodigo());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAlterarPlanoDescontoParaInativo(final PlanoDescontoInclusaoDisciplinaVO obj) throws Exception {
        final String sql = "UPDATE PlanoDescontoInclusaoDisciplina set situacao=?, dataInativacao=?, responsavelInativacao=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getSituacao());
                sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataInativacao()));
                sqlAlterar.setInt(3, obj.getResponsavelInativacao().getCodigo());
                sqlAlterar.setInt(4, obj.getCodigo());
                return sqlAlterar;
            }
        });
    }

    public void validarDadosUnicidade(String descricao) {
    }

    public static String getIdEntidade() {
        return idEntidade;
    }

    public static void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }
}
