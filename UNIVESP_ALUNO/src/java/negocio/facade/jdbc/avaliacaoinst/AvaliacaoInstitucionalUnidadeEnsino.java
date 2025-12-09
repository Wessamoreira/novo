package negocio.facade.jdbc.avaliacaoinst;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalUnidadeEnsinoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalUnidadeEnsinoInterfaceFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Lazy
public class AvaliacaoInstitucionalUnidadeEnsino extends ControleAcesso implements AvaliacaoInstitucionalUnidadeEnsinoInterfaceFacade {

    private static final long serialVersionUID = -2917408135383822717L;


    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception {

        for(AvaliacaoInstitucionalUnidadeEnsinoVO avaliacaoInstitucionalUnidadeEnsinoVO: avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs()) {
            avaliacaoInstitucionalUnidadeEnsinoVO.setAvaliacaoInstitucionalVO(avaliacaoInstitucionalVO);
            incluir(avaliacaoInstitucionalUnidadeEnsinoVO, usuarioVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(AvaliacaoInstitucionalUnidadeEnsinoVO avaliacaoInstitucionalUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
        incluir(avaliacaoInstitucionalUnidadeEnsinoVO, "avaliacaoInstitucionalUnidadeEnsino", new AtributoPersistencia()
                .add("unidadeensino", avaliacaoInstitucionalUnidadeEnsinoVO.getUnidadeEnsinoVO())
                .add("avaliacaoInstitucional", avaliacaoInstitucionalUnidadeEnsinoVO.getAvaliacaoInstitucionalVO()), usuarioVO);

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(AvaliacaoInstitucionalUnidadeEnsinoVO avaliacaoInstitucionalUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
        alterar(avaliacaoInstitucionalUnidadeEnsinoVO, "avaliacaoInstitucionalUnidadeEnsino", new AtributoPersistencia()
                .add("unidadeensino", avaliacaoInstitucionalUnidadeEnsinoVO.getUnidadeEnsinoVO())
                .add("avaliacaoInstitucional", avaliacaoInstitucionalUnidadeEnsinoVO.getAvaliacaoInstitucionalVO()), new AtributoPersistencia().add("codigo", avaliacaoInstitucionalUnidadeEnsinoVO.getCodigo()), usuarioVO);
    }

    @Override
    public void alterarAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception {
        excluirAvaliacaoInstitucionalUnidadeEnsino(avaliacaoInstitucionalVO, usuarioVO);
        for(AvaliacaoInstitucionalUnidadeEnsinoVO avaliacaoInstitucionalUnidadeEnsinoVO: avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs()) {
            avaliacaoInstitucionalUnidadeEnsinoVO.setAvaliacaoInstitucionalVO(avaliacaoInstitucionalVO);
            alterar(avaliacaoInstitucionalUnidadeEnsinoVO, usuarioVO);
        }
    }

    @Override
    public void excluirAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuarioVO) throws Exception {
        excluirListaSubordinada(avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs(), "avaliacaoInstitucionalUnidadeEnsino",
                new AtributoPersistencia().add("avaliacaoInstitucional", avaliacaoInstitucionalVO.getCodigo()), usuarioVO);
    }

    @Override
    public List<AvaliacaoInstitucionalUnidadeEnsinoVO> consultarPorAvaliacaoInstitucional(Integer avaliacaoInstitucional, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql  = new StringBuilder("select avaliacaoInstitucionalUnidadeEnsino.*, unidadeensino.nome as unidadeensino_nome from avaliacaoInstitucionalUnidadeEnsino inner join unidadeEnsino on unidadeensino.codigo = avaliacaoInstitucionalUnidadeEnsino.unidadeensino where avaliacaoInstitucionalUnidadeEnsino.avaliacaoInstitucional = ? order by unidadeensino_nome ");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), avaliacaoInstitucional));
    }

    public List<AvaliacaoInstitucionalUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
        List<AvaliacaoInstitucionalUnidadeEnsinoVO> avaliacaoInstitucionalUnidadeEnsinoVOs =  new ArrayList<AvaliacaoInstitucionalUnidadeEnsinoVO>();
        while(rs.next()) {
            avaliacaoInstitucionalUnidadeEnsinoVOs.add(montarDados(rs));
        }
        return avaliacaoInstitucionalUnidadeEnsinoVOs;
    }
    public AvaliacaoInstitucionalUnidadeEnsinoVO montarDados(SqlRowSet rs) throws Exception {
        AvaliacaoInstitucionalUnidadeEnsinoVO avaliacaoInstitucionalUnidadeEnsinoVO =  new AvaliacaoInstitucionalUnidadeEnsinoVO();
        avaliacaoInstitucionalUnidadeEnsinoVO.setNovoObj(false);
        avaliacaoInstitucionalUnidadeEnsinoVO.setCodigo(rs.getInt("codigo"));
        avaliacaoInstitucionalUnidadeEnsinoVO.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeensino"));
        avaliacaoInstitucionalUnidadeEnsinoVO.getUnidadeEnsinoVO().setNome(rs.getString("unidadeensino_nome"));
        avaliacaoInstitucionalUnidadeEnsinoVO.getAvaliacaoInstitucionalVO().setCodigo(rs.getInt("avaliacaoInstitucional"));
        return avaliacaoInstitucionalUnidadeEnsinoVO;
    }
}
