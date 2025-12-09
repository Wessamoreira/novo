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
import negocio.comuns.bancocurriculum.VagaAreaVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.VagaAreaInterfaceFacade;

@Repository
@Lazy
public class VagaArea extends ControleAcesso implements VagaAreaInterfaceFacade {

    /**
     *
     */
    private static final long serialVersionUID = 3422808486460352538L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirVagaArea(VagasVO vagasVO) throws Exception {
        for (VagaAreaVO vagaAreaVO : vagasVO.getVagaAreaVOs()) {
            vagaAreaVO.setVaga(vagasVO);
            if (vagaAreaVO.getSelecionado()) {
                incluir(vagaAreaVO);
            }
        }

    }

    private void incluir(final VagaAreaVO vagaAreaVO) throws Exception {
        final StringBuilder sql = new StringBuilder("INSERT INTO VagaArea (areaProfissional, vaga) VALUES (?,?) returning codigo ");
        vagaAreaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                ps.setInt(1, vagaAreaVO.getAreaProfissional().getCodigo());
                ps.setInt(2, vagaAreaVO.getVaga().getCodigo());
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
        vagaAreaVO.setNovoObj(false);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final VagaAreaVO vagaAreaVO) throws Exception {
        final StringBuilder sql = new StringBuilder("UPDATE VagaArea set AreaProfissional=?, vaga=? where codigo = ? ");
        if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                ps.setInt(1, vagaAreaVO.getAreaProfissional().getCodigo());
                ps.setInt(2, vagaAreaVO.getVaga().getCodigo());
                ps.setInt(3, vagaAreaVO.getCodigo());
                return ps;
            }
        }) == 0) {
            incluir(vagaAreaVO);
            return;
        }
        vagaAreaVO.setNovoObj(false);
    }

    public void excluirVagaArea(VagasVO vagasVO) {
        StringBuilder sql = new StringBuilder(" DELETE FROM VagaArea WHERE vaga = ").append(vagasVO.getCodigo());
        sql.append(" and codigo not in (0 ");
        for (VagaAreaVO vagaAreaVO : vagasVO.getVagaAreaVOs()) {
            sql.append(", ").append(vagaAreaVO.getCodigo());
        }
        sql.append(" ) ");
        getConexao().getJdbcTemplate().update(sql.toString());
    }

    @Override
    public void alterarVagaArea(VagasVO vagasVO) throws Exception {
        excluirVagaArea(vagasVO);
        for (VagaAreaVO vagaAreaVO : vagasVO.getVagaAreaVOs()) {
            vagaAreaVO.setVaga(vagasVO);
            if (vagaAreaVO.getSelecionado()) {
                if (vagaAreaVO.isNovoObj()) {
                    incluir(vagaAreaVO);
                } else {
                    alterar(vagaAreaVO);
                }
            }
        }

    }

    @Override
    public List<VagaAreaVO> consultarPorVaga(Integer vaga) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM VagaArea WHERE vaga = ").append(vaga).append(" order by codigo ");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }

    private List<VagaAreaVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
        List<VagaAreaVO> vagaAreaVOs = new ArrayList<VagaAreaVO>(0);
        VagaAreaVO vagaAreaVO = null;
        while (rs.next()) {
            vagaAreaVO = new VagaAreaVO();
            vagaAreaVO.setNovoObj(false);
            vagaAreaVO.getAreaProfissional().setCodigo(rs.getInt("AreaProfissional"));
            vagaAreaVO.setSelecionado(Boolean.TRUE);
            vagaAreaVO.getVaga().setCodigo(rs.getInt("vaga"));
            vagaAreaVO.setAreaProfissional(getFacadeFactory().getAreaProfissionalFacade().consultarPorChavePrimaria(vagaAreaVO.getAreaProfissional().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO()));
            vagaAreaVOs.add(vagaAreaVO);
        }

        return vagaAreaVOs;
    }

    @Override
    public void validarDados(VagaAreaVO vagaAreaVO) throws ConsistirException {
    }
}
