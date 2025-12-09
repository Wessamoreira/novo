package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FollowMeGrupoDestinatarioVO;
import negocio.comuns.administrativo.FollowMeVO;
import negocio.comuns.administrativo.enumeradores.FrequenciaEnvioFollowMeEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.FollowMeGrupoDestinatarioInterfaceFacade;

@Repository
@Lazy
public class FollowMeGrupoDestinatario extends ControleAcesso implements FollowMeGrupoDestinatarioInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = 6428363166281837574L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void persistir(FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO, FollowMeVO followMeVO) throws Exception {
        followMeGrupoDestinatarioVO.setFollowMe(followMeVO);
        if (followMeGrupoDestinatarioVO.isNovoObj()) {
            incluir(followMeGrupoDestinatarioVO);
        } else {
            alterar(followMeGrupoDestinatarioVO);
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void excluirFollowMeGrupoDestinatario(FollowMeVO followMeVO) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM followMeGrupoDestinatario where followme =  ").append(followMeVO.getCodigo());
        sb.append(" and codigo not in ( 0 ");
        for (FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO : followMeVO.getFollowMeGrupoDestinatarioVOs()) {
            if (!followMeGrupoDestinatarioVO.isNovoObj()) {
                sb.append(", ").append(followMeGrupoDestinatarioVO.getCodigo());
            }
        }
        sb.append(")");
        getConexao().getJdbcTemplate().execute(sb.toString());
    }

    @Override
    public void validarDados(FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO) throws ConsistirException {
        ConsistirException consistirException = null;
        if (followMeGrupoDestinatarioVO.getGrupoDestinatario() == null
                || followMeGrupoDestinatarioVO.getGrupoDestinatario().getCodigo() == null
                || followMeGrupoDestinatarioVO.getGrupoDestinatario().getCodigo() == 0) {
            consistirException = consistirException == null ? consistirException = new ConsistirException() : consistirException;
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FollowMeGrupoDestinatario_grupoDestinatario"));
        }
        if (followMeGrupoDestinatarioVO.getFrequenciaEnvioFollowMeEnum() == null) {
            consistirException = consistirException == null ? consistirException = new ConsistirException() : consistirException;
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FollowMeGrupoDestinatario_frequenciaEnvioFollowMeEnum"));
        }

        if (followMeGrupoDestinatarioVO.getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.SEMANAL)
                && followMeGrupoDestinatarioVO.getDiaSemana() == null) {
            consistirException = consistirException == null ? consistirException = new ConsistirException() : consistirException;
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FollowMeGrupoDestinatario_diaSemana"));
        }
        if (followMeGrupoDestinatarioVO.getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.DATA_ESPECIFICA)
                && followMeGrupoDestinatarioVO.getDiaMesEspecifico() == null) {
            consistirException = consistirException == null ? consistirException = new ConsistirException() : consistirException;
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FollowMeGrupoDestinatario_diaMesEspecifico"));
        }
        if (followMeGrupoDestinatarioVO.getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.MENSAL)
                && (followMeGrupoDestinatarioVO.getDiaDoMes() == null || followMeGrupoDestinatarioVO.getDiaDoMes() == 0
                || followMeGrupoDestinatarioVO.getDiaDoMes() > 31)) {
            consistirException = consistirException == null ? consistirException = new ConsistirException() : consistirException;
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FollowMeGrupoDestinatario_diaDoMes"));
        }
    }

    private PreparedStatementCreator getPreparedStatementCreator(final FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO, final String sql, final boolean incluir) {
        return new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                int x = 1;
                PreparedStatement preparedStatement = arg0.prepareStatement(sql);
                if (followMeGrupoDestinatarioVO.getIsMensal()) {
                    preparedStatement.setInt(x++, followMeGrupoDestinatarioVO.getDiaDoMes());
                } else {
                    preparedStatement.setNull(x++, 0);
                }
                preparedStatement.setInt(x++, followMeGrupoDestinatarioVO.getGrupoDestinatario().getCodigo());
                preparedStatement.setInt(x++, followMeGrupoDestinatarioVO.getFollowMe().getCodigo());
                if (followMeGrupoDestinatarioVO.getIsDataEspecifica()) {
                    preparedStatement.setDate(x++, Uteis.getDataJDBC(followMeGrupoDestinatarioVO.getDiaMesEspecifico()));
                } else {
                    preparedStatement.setNull(x++, 0);
                }
                if (followMeGrupoDestinatarioVO.getIsSemanal()) {
                    preparedStatement.setString(x++, followMeGrupoDestinatarioVO.getDiaSemana().getValor());
                } else {
                    preparedStatement.setNull(x++, 0);
                }
                preparedStatement.setString(x++, followMeGrupoDestinatarioVO.getFrequenciaEnvioFollowMeEnum().name());
                preparedStatement.setInt(x++, followMeGrupoDestinatarioVO.getOrdem());
                if (!incluir) {
                    preparedStatement.setInt(x++, followMeGrupoDestinatarioVO.getCodigo());
                }
                return preparedStatement;
            }
        };
    }

    private void alterar(final FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO) throws Exception {
        StringBuilder sql = new StringBuilder("UPDATE followMeGrupoDestinatario SET DiaDoMes = ?, grupoDestinatario = ?, followMe = ?, ");
        sql.append(" DiaMesEspecifico = ?, DiaSemana = ?, FrequenciaEnvioFollowMe = ?, Ordem = ? ");
        sql.append(" WHERE codigo = ? ");
        if (getConexao().getJdbcTemplate().update(getPreparedStatementCreator(followMeGrupoDestinatarioVO, sql.toString(), false)) == 0) {
            incluir(followMeGrupoDestinatarioVO);
        };
    }

    private void incluir(final FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("INSERT INTO followMeGrupoDestinatario (DiaDoMes, grupoDestinatario, followMe, ");
            sql.append(" DiaMesEspecifico, DiaSemana, FrequenciaEnvioFollowMe, Ordem ");
            sql.append(") VALUES (?,?,?,?,?,?,?) returning codigo");
            followMeGrupoDestinatarioVO.setCodigo(getConexao().getJdbcTemplate().query(getPreparedStatementCreator(followMeGrupoDestinatarioVO, sql.toString(), true),
                    new ResultSetExtractor<Integer>() {

                        public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                            if (arg0.next()) {
                                followMeGrupoDestinatarioVO.setNovoObj(Boolean.FALSE);
                                return arg0.getInt("codigo");
                            }
                            return null;
                        }
                    }));

        } catch (Exception e) {
            followMeGrupoDestinatarioVO.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    public String getSelectCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select followMeGrupoDestinatario.*, grupodestinatarios.nomegrupo as \"grupodestinatarios.nomegrupo\" ");
        sb.append(" from followMeGrupoDestinatario ");
        sb.append(" inner join grupodestinatarios on grupodestinatarios.codigo = followMeGrupoDestinatario.grupodestinatario  ");
        return sb.toString();
    }

    @Override
    public List<FollowMeGrupoDestinatarioVO> consultarPorFollowMe(Integer followMe, int nivelMontarDados) {
        StringBuilder sb = new StringBuilder(getSelectCompleto());
        sb.append(" where followMe = " + followMe + " order by ordem");
        return montarDadosConsulta(getConexao().getJdbcTemplate().
                queryForRowSet(sb.toString()),
                nivelMontarDados);
    }

    private List<FollowMeGrupoDestinatarioVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) {
        List<FollowMeGrupoDestinatarioVO> followMeGrupoDestinatarioVOs = new ArrayList<FollowMeGrupoDestinatarioVO>(0);
        while (rs.next()) {
            followMeGrupoDestinatarioVOs.add(montarDados(rs, nivelMontarDados));
        }
        return followMeGrupoDestinatarioVOs;
    }

    private FollowMeGrupoDestinatarioVO montarDados(SqlRowSet rs, int nivelMontarDados) {
        FollowMeGrupoDestinatarioVO obj = new FollowMeGrupoDestinatarioVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt("codigo"));
        obj.setOrdem(rs.getInt("ordem"));
        obj.getGrupoDestinatario().setCodigo(rs.getInt("grupoDestinatario"));
        obj.getGrupoDestinatario().setNomeGrupo(rs.getString("grupodestinatarios.nomegrupo"));
        obj.getFollowMe().setCodigo(rs.getInt("followMe"));
        obj.setDiaDoMes(rs.getInt("diaDoMes"));
        obj.setFrequenciaEnvioFollowMeEnum(FrequenciaEnvioFollowMeEnum.valueOf(rs.getString("frequenciaEnvioFollowMe")));
        if (obj.getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.DATA_ESPECIFICA)) {
            obj.setDiaMesEspecifico(rs.getDate("diaMesEspecifico"));
        }
        if (obj.getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.SEMANAL)) {
            obj.setDiaSemana(DiaSemana.getEnum(rs.getString("diaSemana")));
        }
        if (obj.getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.MENSAL)) {
            obj.setDiaDoMes(rs.getInt("diaDoMes"));
        }

        return obj;
    }

}
