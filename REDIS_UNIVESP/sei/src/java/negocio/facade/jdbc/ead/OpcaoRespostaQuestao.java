package negocio.facade.jdbc.ead;

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
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.OpcaoRespostaQuestaoInterfaceFacade;

@Repository
@Lazy
public class OpcaoRespostaQuestao extends ControleAcesso implements OpcaoRespostaQuestaoInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -329087204575610518L;
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void incluir(final OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO, UsuarioVO usuarioVO) throws Exception {        
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO OpcaoRespostaQuestao ");
            sql.append(" (correta, opcaoResposta, ordemApresentacao, questao ");                                    
            sql.append(" ) VALUES (?,?,?,?) ");
            sql.append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
            opcaoRespostaQuestaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x= 1;
                    ps.setBoolean(x++, opcaoRespostaQuestaoVO.getCorreta());
                    ps.setString(x++, opcaoRespostaQuestaoVO.getOpcaoResposta());
                    ps.setInt(x++, opcaoRespostaQuestaoVO.getOrdemApresentacao());
                    ps.setInt(x++, opcaoRespostaQuestaoVO.getQuestaoVO().getCodigo());
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
            opcaoRespostaQuestaoVO.setNovoObj(false);
        } catch (Exception e) {
            opcaoRespostaQuestaoVO.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void alterar(final OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO, UsuarioVO usuarioVO) throws Exception {        
        try {
            final StringBuilder sql = new StringBuilder("UPDATE OpcaoRespostaQuestao set ");
            sql.append(" correta = ?, opcaoResposta = ?, ordemApresentacao = ?, questao = ?");
            sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
            if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x= 1;
                    ps.setBoolean(x++, opcaoRespostaQuestaoVO.getCorreta());
                    ps.setString(x++, opcaoRespostaQuestaoVO.getOpcaoResposta());
                    ps.setInt(x++, opcaoRespostaQuestaoVO.getOrdemApresentacao());
                    ps.setInt(x++, opcaoRespostaQuestaoVO.getQuestaoVO().getCodigo());
                    ps.setInt(x++, opcaoRespostaQuestaoVO.getCodigo());
                    return ps;
                }
            })==0){
                incluir(opcaoRespostaQuestaoVO, usuarioVO);
                return;
            }            
            opcaoRespostaQuestaoVO.setNovoObj(false);
        } catch (Exception e) {
            opcaoRespostaQuestaoVO.setNovoObj(false);
            throw e;
        }
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirOpcaoRespostaQuestao(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception {
        
        for(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO:questaoVO.getOpcaoRespostaQuestaoVOs()){
            opcaoRespostaQuestaoVO.setQuestaoVO(questaoVO);
            validarDados(opcaoRespostaQuestaoVO);
            incluir(opcaoRespostaQuestaoVO, usuarioVO);
        }

    }
    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirOpcaoRespostaQuestao(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM OpcaoRespostaQuestao where questao =  ").append(questaoVO.getCodigo());
        sb.append(" and codigo not in ( 0 ");
        for(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO:questaoVO.getOpcaoRespostaQuestaoVOs()){
            if (!opcaoRespostaQuestaoVO.isNovoObj()) {
               sb.append(", ").append(opcaoRespostaQuestaoVO.getCodigo());
            }                      
        }
        sb.append(")").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
        getConexao().getJdbcTemplate().execute(sb.toString());
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarOpcaoRespostaQuestao(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception {
        excluirOpcaoRespostaQuestao(questaoVO, usuarioVO);
        for(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO:questaoVO.getOpcaoRespostaQuestaoVOs()){
            opcaoRespostaQuestaoVO.setQuestaoVO(questaoVO);
            validarDados(opcaoRespostaQuestaoVO);
            if(opcaoRespostaQuestaoVO.getNovoObj()){
                incluir(opcaoRespostaQuestaoVO, usuarioVO);
            }else{
                alterar(opcaoRespostaQuestaoVO, usuarioVO);
            }
        }

    }

    @Override
    public List<OpcaoRespostaQuestaoVO> consultarPorQuestao(Integer questao) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT * FROM OpcaoRespostaQuestao where questao = ");
        sb.append(questao).append(" order by ordemApresentacao asc ");        
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), "");
    }

    @Override
    public void validarDados(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) throws ConsistirException {
        ConsistirException ce = null;
        if(Uteis.retiraTags(opcaoRespostaQuestaoVO.getOpcaoResposta()).trim().isEmpty()  && !opcaoRespostaQuestaoVO.getOpcaoResposta().contains("<img")){
            ce = ce == null? new ConsistirException():ce;
            opcaoRespostaQuestaoVO.setEditar(true);
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_OpcaoRespostaQuestao_opcaoReposta").replace("{0}", opcaoRespostaQuestaoVO.getLetraCorrespondente()));
            
        }
        if(ce!= null){
            throw ce;
        }
        
    }
    
    
    public List<OpcaoRespostaQuestaoVO> montarDadosConsulta(SqlRowSet rs, String prefixo){
        List<OpcaoRespostaQuestaoVO> objs = new ArrayList<OpcaoRespostaQuestaoVO>(0);
        while(rs.next()){
            objs.add(montarDados(rs, prefixo));
        }
        return objs;
    }
    
    @Override
    public OpcaoRespostaQuestaoVO montarDados(SqlRowSet rs, String prefixo){
        OpcaoRespostaQuestaoVO obj = new OpcaoRespostaQuestaoVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt(prefixo+"codigo"));
        obj.setOrdemApresentacao(rs.getInt(prefixo+"ordemApresentacao"));
        obj.setOpcaoResposta(rs.getString(prefixo+"opcaoResposta"));
        obj.setCorreta(rs.getBoolean(prefixo+"correta"));
        obj.getQuestaoVO().setCodigo(rs.getInt(prefixo+"questao"));
        return obj;
    }
    
    @Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public OpcaoRespostaQuestaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM OpcaoRespostaQuestao WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if(rs.next()) {
			return (montarDados(rs, ""));			
		} 
		return new OpcaoRespostaQuestaoVO();	
	}

}
