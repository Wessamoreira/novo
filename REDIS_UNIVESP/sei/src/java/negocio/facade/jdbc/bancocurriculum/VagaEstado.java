package negocio.facade.jdbc.bancocurriculum;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.VagaEstadoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.VagaEstadoInterfaceFacade;

@Repository
@Lazy
public class VagaEstado extends ControleAcesso implements VagaEstadoInterfaceFacade {

    /**
     *
     */
    private static final long serialVersionUID = 3422808486460352538L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirVagaEstado(VagasVO vagasVO) throws Exception {
        for (VagaEstadoVO vagaEstadoVO : vagasVO.getVagaEstadoVOs()) {
            vagaEstadoVO.setVaga(vagasVO);
            if (vagaEstadoVO.getSelecionado()) {
                incluir(vagaEstadoVO);
            }
        }

    }

    private void incluir(final VagaEstadoVO vagaEstadoVO) throws Exception {
        final StringBuilder sql = new StringBuilder("INSERT INTO VagaEstado (estado, vaga) VALUES (?,?) returning codigo ");
        vagaEstadoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                ps.setInt(1, vagaEstadoVO.getEstado().getCodigo());
                ps.setInt(2, vagaEstadoVO.getVaga().getCodigo());
                return ps;
            }
        }, new ResultSetExtractor<Integer>() {

            @Override
            public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));
        vagaEstadoVO.setNovoObj(false);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final VagaEstadoVO vagaEstadoVO) throws Exception {
        final StringBuilder sql = new StringBuilder("UPDATE VagaEstado set estado=?, vaga=? where codigo = ? ");
        if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                ps.setInt(1, vagaEstadoVO.getEstado().getCodigo());
                ps.setInt(2, vagaEstadoVO.getVaga().getCodigo());
                ps.setInt(3, vagaEstadoVO.getCodigo());
                return ps;
            }
        }) == 0) {
            incluir(vagaEstadoVO);
            return;
        }
        vagaEstadoVO.setNovoObj(false);
    }

    public void excluirVagaEstado(VagasVO vagasVO) {
        StringBuilder sql = new StringBuilder(" DELETE FROM VagaEstado WHERE vaga = ").append(vagasVO.getCodigo());
        sql.append(" and codigo not in (0 ");
        for (VagaEstadoVO vagaEstadoVO : vagasVO.getVagaEstadoVOs()) {
            sql.append(", ").append(vagaEstadoVO.getCodigo());
        }
        sql.append(" ) ");
        getConexao().getJdbcTemplate().update(sql.toString());
    }

    @Override
    public void alterarVagaEstado(VagasVO vagasVO) throws Exception {
        excluirVagaEstado(vagasVO);
        for (VagaEstadoVO vagaEstadoVO : vagasVO.getVagaEstadoVOs()) {
            vagaEstadoVO.setVaga(vagasVO);
            if (vagaEstadoVO.getSelecionado()) {
                if (vagaEstadoVO.isNovoObj()) {
                    incluir(vagaEstadoVO);
                } else {
                    alterar(vagaEstadoVO);
                }
            }
        }

    }

    @Override
    public List<VagaEstadoVO> consultarPorVaga(Integer vaga) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM VagaEstado WHERE vaga = ").append(vaga).append(" order by codigo ");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }

    private List<VagaEstadoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
        List<VagaEstadoVO> vagaEstadoVOs = new ArrayList<VagaEstadoVO>(0);
        VagaEstadoVO vagaEstadoVO = null;
        while (rs.next()) {
            vagaEstadoVO = new VagaEstadoVO();
            vagaEstadoVO.setNovoObj(false);
            vagaEstadoVO.getEstado().setCodigo(rs.getInt("estado"));
            vagaEstadoVO.getVaga().setCodigo(rs.getInt("vaga"));
            vagaEstadoVO.setSelecionado(Boolean.TRUE);
            vagaEstadoVO.setEstado(getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(vagaEstadoVO.getEstado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO()));
            vagaEstadoVOs.add(vagaEstadoVO);
        }

        return vagaEstadoVOs;
    }

    @Override
    public void validarDados(VagaEstadoVO vagaEstadoVO) throws ConsistirException {
    }
}
