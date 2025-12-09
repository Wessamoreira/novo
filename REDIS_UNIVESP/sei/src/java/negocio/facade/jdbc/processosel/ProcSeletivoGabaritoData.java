package negocio.facade.jdbc.processosel;

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
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoGabaritoDataVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ProcSeletivoGabaritoDataInterfaceFacade;

@Repository
@Lazy
public class ProcSeletivoGabaritoData extends ControleAcesso implements ProcSeletivoGabaritoDataInterfaceFacade {

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO, UsuarioVO usuarioVO) throws Exception {
        final StringBuilder sql = new StringBuilder("INSERT INTO ProcSeletivoGabaritoData (itemProcSeletivoDataProva, gabarito, disciplinaIdioma) VALUES (?, ?, ?) RETURNING codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
        procSeletivoGabaritoDataVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                int x = 1;
                ps.setInt(x++, procSeletivoGabaritoDataVO.getItemProcSeletivoDataProva().getCodigo());
                ps.setInt(x++, procSeletivoGabaritoDataVO.getGabaritoVO().getCodigo());
                if (procSeletivoGabaritoDataVO.getDisciplinaIdioma().getCodigo() != null && procSeletivoGabaritoDataVO.getDisciplinaIdioma().getCodigo() > 0) {
                    ps.setInt(x++, procSeletivoGabaritoDataVO.getDisciplinaIdioma().getCodigo());
                } else {
                    ps.setNull(x++, 0);
                }
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
        procSeletivoGabaritoDataVO.setNovoObj(false);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void altera(final ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO, UsuarioVO usuarioVO) throws Exception {
        final StringBuilder sql = new StringBuilder("UPDATE ProcSeletivoGabaritoData set itemProcSeletivoDataProva =?, gabarito=?, disciplinaIdioma=? WHERE codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
        if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                int x = 1;
                ps.setInt(x++, procSeletivoGabaritoDataVO.getItemProcSeletivoDataProva().getCodigo());
                ps.setInt(x++, procSeletivoGabaritoDataVO.getGabaritoVO().getCodigo());
                if (procSeletivoGabaritoDataVO.getDisciplinaIdioma().getCodigo() != null && procSeletivoGabaritoDataVO.getDisciplinaIdioma().getCodigo() > 0) {
                    ps.setInt(x++, procSeletivoGabaritoDataVO.getDisciplinaIdioma().getCodigo());
                } else {
                    ps.setNull(x++, 0);
                }
                ps.setInt(x++, procSeletivoGabaritoDataVO.getCodigo());
                return ps;
            }
        }) == 0) {
            incluir(procSeletivoGabaritoDataVO, usuarioVO);
            return;
        }
        procSeletivoGabaritoDataVO.setNovoObj(false);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirProcessoSeletivoGabaritoData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception {
        for(ProcSeletivoGabaritoDataVO obj : itemProcSeletivoDataProvaVO.getProcSeletivoGabaritoDataVOs()){
            obj.setItemProcSeletivoDataProva(itemProcSeletivoDataProvaVO);
            incluir(obj, usuarioVO);
        }

    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirProcessoSeletivoGabaritoData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder("DELETE FROM ProcSeletivoGabaritoData WHERE itemProcSeletivoDataProva = ").append(itemProcSeletivoDataProvaVO.getCodigo());
        sql.append(" and codigo not in ( 0 ");
        for(ProcSeletivoGabaritoDataVO obj : itemProcSeletivoDataProvaVO.getProcSeletivoGabaritoDataVOs()){
            sql.append(", ").append(obj.getCodigo());            
        }
        sql.append(") "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));   
        getConexao().getJdbcTemplate().update(sql.toString());
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alteraProcessoSeletivoGabaritoData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception {
    	excluirProcessoSeletivoGabaritoData(itemProcSeletivoDataProvaVO, usuarioVO);
        for(ProcSeletivoGabaritoDataVO obj : itemProcSeletivoDataProvaVO.getProcSeletivoGabaritoDataVOs()){
            obj.setItemProcSeletivoDataProva(itemProcSeletivoDataProvaVO);
            if(obj.getNovoObj()){
                incluir(obj, usuarioVO);
            }else{
                altera(obj, usuarioVO);
            }
        }

    }

    @Override
    public List<ProcSeletivoGabaritoDataVO> consultarPorItemProcSeletivoDataProva(Integer codigo) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT ProcSeletivoGabaritoData.codigo, ProcSeletivoGabaritoData.itemProcSeletivoDataProva, ");
        sql.append(" gabarito.codigo AS \"gabarito.codigo\", gabarito.descricao as \"gabarito.descricao\", ");
        sql.append(" DisciplinasProcSeletivo.codigo AS \"DisciplinasProcSeletivo.codigo\", DisciplinasProcSeletivo.nome AS \"DisciplinasProcSeletivo.nome\", grupoDisciplinaProcSeletivo ");
        sql.append(" FROM ProcSeletivoGabaritoData");
        sql.append(" inner join gabarito on gabarito.codigo = ProcSeletivoGabaritoData.gabarito ");
        sql.append(" left join DisciplinasProcSeletivo on DisciplinasProcSeletivo.codigo = ProcSeletivoGabaritoData.disciplinaIdioma ");
        sql.append(" where ProcSeletivoGabaritoData.itemProcSeletivoDataProva = ").append(codigo);
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }

    private List<ProcSeletivoGabaritoDataVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
        List<ProcSeletivoGabaritoDataVO> objs = new ArrayList<ProcSeletivoGabaritoDataVO>(0);
        while (rs.next()) {
            objs.add(montarDados(rs));
        }
        return objs;
    }

	private ProcSeletivoGabaritoDataVO montarDados(SqlRowSet rs) throws Exception {
		ProcSeletivoGabaritoDataVO obj = new ProcSeletivoGabaritoDataVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.getGabaritoVO().setCodigo(rs.getInt("gabarito.codigo"));
		obj.getGabaritoVO().setDescricao(rs.getString("gabarito.descricao"));
		obj.getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().setCodigo(rs.getInt("grupoDisciplinaProcSeletivo"));
		obj.getItemProcSeletivoDataProva().setCodigo(rs.getInt("itemProcSeletivoDataProva"));
		obj.getDisciplinaIdioma().setCodigo(rs.getInt("DisciplinasProcSeletivo.codigo"));
		obj.getDisciplinaIdioma().setNome(rs.getString("DisciplinasProcSeletivo.nome"));
		if (Uteis.isAtributoPreenchido(obj.getGabaritoVO().getGrupoDisciplinaProcSeletivoVO())) {
			obj.getGabaritoVO().setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarCodigo(obj.getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}
		return obj;
	}

    @Override
    public void validarDados(ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO) throws ConsistirException {
        if (procSeletivoGabaritoDataVO.getGabaritoVO().getCodigo() == null ||
                procSeletivoGabaritoDataVO.getGabaritoVO().getCodigo() == 0) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_ProcessoSeletivoGabaritoData_gabarito"));
        }

    }

}
