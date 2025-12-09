package negocio.facade.jdbc.processosel;

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

import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.OpcaoRespostaQuestaoProcessoSeletivoInterfaceFacade;

@Repository
@Lazy
@Scope
public class OpcaoRespostaQuestaoProcessoSeletivo extends ControleAcesso implements OpcaoRespostaQuestaoProcessoSeletivoInterfaceFacade {

    
    public void incluir(final OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO OpcaoRespostaQuestaoProcessoSeletivo ");
            sql.append(" (correta, opcaoResposta, ordemApresentacao, questaoProcessoSeletivo ");                                    
            sql.append(" ) VALUES (?,?,?,?) ");
            sql.append(" returning codigo ");
            opcaoRespostaQuestaoProcessoSeletivoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x= 1;
                    ps.setBoolean(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getCorreta());
                    ps.setString(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getOpcaoResposta());
                    ps.setInt(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getOrdemApresentacao());
                    ps.setInt(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo());
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
            opcaoRespostaQuestaoProcessoSeletivoVO.setNovoObj(false);
        } catch (Exception e) {
            opcaoRespostaQuestaoProcessoSeletivoVO.setNovoObj(true);
            throw e;
        }

    }

    
    public void alterar(final OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("UPDATE OpcaoRespostaQuestaoProcessoSeletivo set ");
            sql.append(" correta = ?, opcaoResposta = ?, ordemApresentacao = ?, questaoProcessoSeletivo = ?");
            sql.append(" where codigo = ? ");
            if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x= 1;
                    ps.setBoolean(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getCorreta());
                    ps.setString(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getOpcaoResposta());
                    ps.setInt(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getOrdemApresentacao());
                    ps.setInt(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo());
                    ps.setInt(x++, opcaoRespostaQuestaoProcessoSeletivoVO.getCodigo());
                    return ps;
                }
            })==0){
                incluir(opcaoRespostaQuestaoProcessoSeletivoVO);
                return;
            }            
            opcaoRespostaQuestaoProcessoSeletivoVO.setNovoObj(false);
        } catch (Exception e) {
            opcaoRespostaQuestaoProcessoSeletivoVO.setNovoObj(false);
            throw e;
        }

    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws Exception {
        
        for(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoVO:questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()){
            opcaoRespostaQuestaoVO.setQuestaoProcessoSeletivo(questaoProcessoSeletivoVO);
            validarDados(opcaoRespostaQuestaoVO);
            incluir(opcaoRespostaQuestaoVO);
        }

    }
    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM OpcaoRespostaQuestaoProcessoSeletivo where questaoProcessoSeletivo =  ").append(questaoProcessoSeletivoVO.getCodigo());
        sb.append(" and codigo not in ( 0 ");
        for(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoVO:questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()){
            if (!opcaoRespostaQuestaoVO.isNovoObj()) {
               sb.append(", ").append(opcaoRespostaQuestaoVO.getCodigo());
            }                      
        }
        sb.append(")");
        getConexao().getJdbcTemplate().execute(sb.toString());
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws Exception {
        excluirOpcaoRespostaQuestao(questaoProcessoSeletivoVO);
        for(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoVO:questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()){
            opcaoRespostaQuestaoVO.setQuestaoProcessoSeletivo(questaoProcessoSeletivoVO);
            validarDados(opcaoRespostaQuestaoVO);
            if(opcaoRespostaQuestaoVO.getNovoObj()){
                incluir(opcaoRespostaQuestaoVO);
            }else{
                alterar(opcaoRespostaQuestaoVO);
            }
        }

    }

    @Override
    public List<OpcaoRespostaQuestaoProcessoSeletivoVO> consultarPorQuestao(Integer questao) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT * FROM OpcaoRespostaQuestaoProcessoSeletivo where questaoProcessoSeletivo = ");
        sb.append(questao).append(" order by ordemApresentacao asc ");        
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), "");
    }

    @Override
    public void validarDados(OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoVO) throws ConsistirException {
        ConsistirException ce = null;
        if(Uteis.retiraTags(opcaoRespostaQuestaoVO.getOpcaoResposta()).trim().isEmpty()){
            ce = ce == null? new ConsistirException():ce;
            opcaoRespostaQuestaoVO.setEditar(true);
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_OpcaoRespostaQuestaoProcessoSeletivo_opcaoReposta").replace("{0}", opcaoRespostaQuestaoVO.getLetraCorrespondente()));
            
        }
        if(ce!= null){
            throw ce;
        }
        
    }
    
    
    public List<OpcaoRespostaQuestaoProcessoSeletivoVO> montarDadosConsulta(SqlRowSet rs, String prefixo){
        List<OpcaoRespostaQuestaoProcessoSeletivoVO> objs = new ArrayList<OpcaoRespostaQuestaoProcessoSeletivoVO>(0);
        while(rs.next()){
            objs.add(montarDados(rs, prefixo));
        }
        return objs;
    }
    
    @Override
    public OpcaoRespostaQuestaoProcessoSeletivoVO montarDados(SqlRowSet rs, String prefixo){
        OpcaoRespostaQuestaoProcessoSeletivoVO obj = new OpcaoRespostaQuestaoProcessoSeletivoVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt(prefixo+"codigo"));
        obj.setOrdemApresentacao(rs.getInt(prefixo+"ordemApresentacao"));
        obj.setOpcaoResposta(rs.getString(prefixo+"opcaoResposta"));
        obj.setCorreta(rs.getBoolean(prefixo+"correta"));
        obj.getQuestaoProcessoSeletivo().setCodigo(rs.getInt(prefixo+"questaoProcessoSeletivo"));
        return obj;
    }

}
