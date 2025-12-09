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
import negocio.comuns.processosel.ProcessoSeletivoProvaDataVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ProcessoSeletivoProvaDataInterfaceFacade;

@Repository
@Lazy
public class ProcessoSeletivoProvaData extends ControleAcesso implements ProcessoSeletivoProvaDataInterfaceFacade {

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO, UsuarioVO usuarioVO) throws Exception {
        final StringBuilder sql = new StringBuilder("INSERT INTO ProcessoSeletivoProvaData (itemProcSeletivoDataProva, disciplinaIdioma, provaProcessoSeletivo) VALUES (?,?,?) RETURNING codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
        processoSeletivoProvaDataVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                int x = 1;
                ps.setInt(x++, processoSeletivoProvaDataVO.getItemProcSeletivoDataProva().getCodigo());
                if (processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo() != null && processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo() > 0) {
                    ps.setInt(x++, processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo());
                } else {
                    ps.setNull(x++, 0);
                }
                ps.setInt(x++, processoSeletivoProvaDataVO.getProvaProcessoSeletivo().getCodigo());
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
        processoSeletivoProvaDataVO.setNovoObj(false);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void altera(final ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO, UsuarioVO usuarioVO) throws Exception {
        final StringBuilder sql = new StringBuilder("UPDATE ProcessoSeletivoProvaData set itemProcSeletivoDataProva =?, disciplinaIdioma=?, provaProcessoSeletivo=? WHERE codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
        if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                int x = 1;
                ps.setInt(x++, processoSeletivoProvaDataVO.getItemProcSeletivoDataProva().getCodigo());
                if (processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo() != null && processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo() > 0) {
                    ps.setInt(x++, processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo());
                } else {
                    ps.setNull(x++, 0);
                }
                ps.setInt(x++, processoSeletivoProvaDataVO.getProvaProcessoSeletivo().getCodigo());
                ps.setInt(x++, processoSeletivoProvaDataVO.getCodigo());
                return ps;
            }
        }) == 0) {
            incluir(processoSeletivoProvaDataVO, usuarioVO);
            return;
        }
        processoSeletivoProvaDataVO.setNovoObj(false);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirProcessoSeletivoProvaData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception {
        for(ProcessoSeletivoProvaDataVO obj:itemProcSeletivoDataProvaVO.getProcessoSeletivoProvaDataVOs()){
            obj.setItemProcSeletivoDataProva(itemProcSeletivoDataProvaVO);
            incluir(obj, usuarioVO);
        }

    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirProcessoSeletivoProvaData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder("DELETE FROM ProcessoSeletivoProvaData WHERE itemProcSeletivoDataProva = ").append(itemProcSeletivoDataProvaVO.getCodigo());
        sql.append(" and codigo not in ( 0 ");
        for(ProcessoSeletivoProvaDataVO obj:itemProcSeletivoDataProvaVO.getProcessoSeletivoProvaDataVOs()){
            sql.append(", ").append(obj.getCodigo());            
        }
        sql.append(") "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));   
        getConexao().getJdbcTemplate().update(sql.toString());
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alteraProcessoSeletivoProvaData(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, UsuarioVO usuarioVO) throws Exception {
    	excluirProcessoSeletivoProvaData(itemProcSeletivoDataProvaVO, usuarioVO);
        for(ProcessoSeletivoProvaDataVO obj:itemProcSeletivoDataProvaVO.getProcessoSeletivoProvaDataVOs()){
            obj.setItemProcSeletivoDataProva(itemProcSeletivoDataProvaVO);
            if(obj.getNovoObj()){
                incluir(obj, usuarioVO);
            }else{
                altera(obj, usuarioVO);
            }
        }

    }

    @Override
    public List<ProcessoSeletivoProvaDataVO> consultarPorItemProcSeletivoDataProva(Integer codigo) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT ProcessoSeletivoProvaData.*, ");
        sql.append(" provaProcessoSeletivo.descricao as \"provaProcessoSeletivo.descricao\", ");
        sql.append(" provaProcessoSeletivo.possuiredacao as \"provaProcessoSeletivo.possuiredacao\", ");
        sql.append(" DisciplinasProcSeletivo.nome as \"DisciplinasProcSeletivo.nome\", grupoDisciplinaProcSeletivo ");
        sql.append(" FROM ProcessoSeletivoProvaData");
        sql.append(" inner join provaProcessoSeletivo on provaProcessoSeletivo.codigo = ProcessoSeletivoProvaData.provaProcessoSeletivo ");
        sql.append(" left join DisciplinasProcSeletivo on DisciplinasProcSeletivo.codigo = ProcessoSeletivoProvaData.disciplinaIdioma ");
        sql.append(" where ProcessoSeletivoProvaData.itemProcSeletivoDataProva = ").append(codigo);
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }

    private List<ProcessoSeletivoProvaDataVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
        List<ProcessoSeletivoProvaDataVO> objs = new ArrayList<ProcessoSeletivoProvaDataVO>(0);
        while (rs.next()) {
            objs.add(montarDados(rs));
        }
        return objs;
    }

	private ProcessoSeletivoProvaDataVO montarDados(SqlRowSet rs) throws Exception {
		ProcessoSeletivoProvaDataVO obj = new ProcessoSeletivoProvaDataVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.getProvaProcessoSeletivo().setCodigo(rs.getInt("provaProcessoSeletivo"));
		obj.getProvaProcessoSeletivo().setDescricao(rs.getString("provaProcessoSeletivo.descricao"));
		obj.getProvaProcessoSeletivo().setPossuiRedacao(rs.getBoolean("provaProcessoSeletivo.possuiredacao"));
		obj.getDisciplinaIdioma().setCodigo(rs.getInt("disciplinaIdioma"));
		obj.getDisciplinaIdioma().setNome(rs.getString("DisciplinasProcSeletivo.nome"));
		obj.getProvaProcessoSeletivo().getGrupoDisciplinaProcSeletivoVO().setCodigo(rs.getInt("grupoDisciplinaProcSeletivo"));
		if (Uteis.isAtributoPreenchido(obj.getProvaProcessoSeletivo().getGrupoDisciplinaProcSeletivoVO())) {
			obj.getProvaProcessoSeletivo().setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarCodigo(obj.getProvaProcessoSeletivo().getGrupoDisciplinaProcSeletivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}
		return obj;
	}

    @Override
    public void validarDados(ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO) throws ConsistirException {
        if (processoSeletivoProvaDataVO.getProvaProcessoSeletivo().getCodigo() == null ||
                processoSeletivoProvaDataVO.getProvaProcessoSeletivo().getCodigo() == 0) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_ProcessoSeletivoProvaData_provaProcessoSeletivo"));
        }

    }

    @Override
    public void validarDadosGabaritoData(ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO) throws ConsistirException {
    	if (procSeletivoGabaritoDataVO.getGabaritoVO().getCodigo() == null ||
    			procSeletivoGabaritoDataVO.getGabaritoVO().getCodigo() == 0) {
    		throw new ConsistirException(UteisJSF.internacionalizar("msg_ProcessoSeletivoProvaData_provaProcessoSeletivo"));
    	}
    	
    }
    
}
