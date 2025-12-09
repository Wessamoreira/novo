package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CondicaoPlanoFinanceiroCursoTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CondicaoPlanoFinanceiroCursoTurmaInterfaceFacade;

@Repository
@Lazy
public class CondicaoPlanoFinanceiroCursoTurma extends ControleAcesso implements CondicaoPlanoFinanceiroCursoTurmaInterfaceFacade {

   
    private static final long serialVersionUID = -1781459054094517224L;

    @Override
    public void persistir(CondicaoPlanoFinanceiroCursoTurmaVO obj) throws Exception {
        if(obj.isNovoObj()){
            incluir(obj);
        }else{
            alterar(obj);
        }

    }
    
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
    private void incluir(final CondicaoPlanoFinanceiroCursoTurmaVO obj) throws Exception{
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO CondicaoPlanoFinanceiroCursoTurma ");
            sql.append(" (condicaoPagamentoPlanoFinanceiroCurso, disciplina, valor ) ");
            sql.append(" VALUES ( ?, ?, ? ) returning codigo");
            obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setInt(x++, obj.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
                    sqlInserir.setInt(x++, obj.getDisciplina().getCodigo());
                    sqlInserir.setDouble(x++, obj.getValor());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }
    
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
    private void alterar(final CondicaoPlanoFinanceiroCursoTurmaVO obj) throws Exception{
        try {
            final StringBuilder sql = new StringBuilder("UPDATE CondicaoPlanoFinanceiroCursoTurma set ");
            sql.append(" condicaoPagamentoPlanoFinanceiroCurso = ?, disciplina = ?, valor = ? ");            
            sql.append(" where codigo = ? ");
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setInt(x++, obj.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
                    sqlAlterar.setInt(x++, obj.getDisciplina().getCodigo());
                    sqlAlterar.setDouble(x++, obj.getValor());
                    sqlAlterar.setInt(x++, obj.getCodigo());
                    return sqlAlterar;
                }
            }) <= 0) {
                incluir(obj);
                return;
            }
            obj.setNovoObj(false);            
        } catch (Exception e) {
            obj.setNovoObj(false);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
    public void incluirCondicaoPlanoFinanceiroCursoTurma(CondicaoPagamentoPlanoFinanceiroCursoVO obj) throws Exception {
        for(CondicaoPlanoFinanceiroCursoTurmaVO condicaoPlanoFinanceiroCursoTurmaVO:obj.getCondicaoPlanoFinanceiroCursoTurmaVOs()){
            condicaoPlanoFinanceiroCursoTurmaVO.setCondicaoPagamentoPlanoFinanceiroCurso(obj);
            incluir(condicaoPlanoFinanceiroCursoTurmaVO);
        }

    }
    
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
    private void excluirCondicaoPlanoFinanceiroCursoTurma(CondicaoPagamentoPlanoFinanceiroCursoVO obj)  throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM CondicaoPlanoFinanceiroCursoTurma where condicaoPagamentoPlanoFinanceiroCurso =  ").append(obj.getCodigo());
        sb.append(" and codigo not in ( 0 " );
        for(CondicaoPlanoFinanceiroCursoTurmaVO condicaoPlanoFinanceiroCursoTurmaVO:obj.getCondicaoPlanoFinanceiroCursoTurmaVOs()){            
            if(!condicaoPlanoFinanceiroCursoTurmaVO.isNovoObj()){
                sb.append(", ").append(condicaoPlanoFinanceiroCursoTurmaVO.getCodigo());
            }
        }
        sb.append(")" );
        getConexao().getJdbcTemplate().execute(sb.toString());
    }

    @Override
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
    public void alterarCondicaoPlanoFinanceiroCursoTurma(CondicaoPagamentoPlanoFinanceiroCursoVO obj) throws Exception {
        excluirCondicaoPlanoFinanceiroCursoTurma(obj);
        for(CondicaoPlanoFinanceiroCursoTurmaVO condicaoPlanoFinanceiroCursoTurmaVO:obj.getCondicaoPlanoFinanceiroCursoTurmaVOs()){
            condicaoPlanoFinanceiroCursoTurmaVO.setCondicaoPagamentoPlanoFinanceiroCurso(obj);            
            persistir(condicaoPlanoFinanceiroCursoTurmaVO);            
        }
    }

	@Override
	public List<CondicaoPlanoFinanceiroCursoTurmaVO> consultarPorCondicaoPlanoFinanceiroCurso(Integer condicaoPlanoFinanceiroCurso) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT CondicaoPlanoFinanceiroCursoTurma.* ");
		sql.append("from CondicaoPlanoFinanceiroCursoTurma ");
		sql.append("WHERE condicaoPagamentoPlanoFinanceiroCurso = ").append(condicaoPlanoFinanceiroCurso);
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
    
    @Override
    public Double consultarValorCondicaoPlanoFinanceiroCursoTurma(Integer condicaoPlanoFinanceiroCurso, Integer disciplina, UsuarioVO usuario) throws Exception {
        if((condicaoPlanoFinanceiroCurso ==null || condicaoPlanoFinanceiroCurso == 0) || (disciplina ==null || disciplina ==0 )){
            return 0.0;
        }
        StringBuilder sql = new StringBuilder("SELECT CondicaoPlanoFinanceiroCursoTurma.valor   ");        
        sql.append(" from CondicaoPlanoFinanceiroCursoTurma ");       
        sql.append(" WHERE condicaoPagamentoPlanoFinanceiroCurso = ").append(condicaoPlanoFinanceiroCurso);
        sql.append(" and disciplina = ").append(disciplina);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if(rs.next()){
            return rs.getDouble("valor");
        }
        throw new Exception("Não foi definido um VALOR para a disciplina "+getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario).getNome()+" no plano financeiro do curso.");
    }
    
    @Override
    public List<CondicaoPlanoFinanceiroCursoTurmaVO> consultarDadosParaCriacaoCondicaoPlanoFinanceiroCursoTurma(Integer turma) throws Exception {
        StringBuilder sql = new StringBuilder(" SELECT 0 as codigo, 0 as condicaoPagamentoPlanoFinanceiroCurso, disciplina.codigo as disciplina,  disciplina.nome as \"disciplina.nome\", gradedisciplina.cargahoraria as \"gradedisciplina.cargahoraria\", gradedisciplina.nrcreditos as \"gradedisciplina.nrcreditos\", 0.0 as valor ");        
        sql.append(" from TurmaDisciplina ");
        sql.append(" inner join disciplina on disciplina.codigo = TurmaDisciplina.disciplina ");
        sql.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
        sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = turma.periodoletivo and gradedisciplina.disciplina = disciplina.codigo ");
        sql.append(" WHERE turma.codigo = ").append(turma);
        sql.append(" order by disciplina.nome");
        return montarDadosConsultaParaCriacao(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }
    
    private List<CondicaoPlanoFinanceiroCursoTurmaVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
        List<CondicaoPlanoFinanceiroCursoTurmaVO> objs = new ArrayList<CondicaoPlanoFinanceiroCursoTurmaVO>(0);
        while(rs.next()){
            objs.add(montarDados(rs));
        }
        return objs;
    }
    
	private CondicaoPlanoFinanceiroCursoTurmaVO montarDados(SqlRowSet rs) throws Exception {
		CondicaoPlanoFinanceiroCursoTurmaVO obj = new CondicaoPlanoFinanceiroCursoTurmaVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(rs.getInt("condicaoPagamentoPlanoFinanceiroCurso"));
		obj.getDisciplina().setCodigo(rs.getInt("disciplina"));
		obj.setValor(rs.getDouble("valor"));
		obj.setNovoObj(false);
		return obj;
	}
	
    private List<CondicaoPlanoFinanceiroCursoTurmaVO> montarDadosConsultaParaCriacao(SqlRowSet rs){
        List<CondicaoPlanoFinanceiroCursoTurmaVO> objs = new ArrayList<CondicaoPlanoFinanceiroCursoTurmaVO>(0);
        while(rs.next()){
            objs.add(montarDadosParaCriacao(rs));
        }
        return objs;
    }
    
    private CondicaoPlanoFinanceiroCursoTurmaVO montarDadosParaCriacao(SqlRowSet rs){
        CondicaoPlanoFinanceiroCursoTurmaVO obj = new CondicaoPlanoFinanceiroCursoTurmaVO();
        obj.setCodigo(rs.getInt("codigo"));
        obj.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(rs.getInt("condicaoPagamentoPlanoFinanceiroCurso"));
        obj.getDisciplina().setCodigo(rs.getInt("disciplina"));
        obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
        obj.getGradeDisciplinaVO().setCargaHoraria(rs.getInt("gradedisciplina.cargahoraria"));
        obj.getGradeDisciplinaVO().setNrCreditos(rs.getInt("gradedisciplina.nrcreditos"));
        obj.setValor(rs.getDouble("valor"));
        if(obj.getCodigo().intValue() == 0){
            obj.setNovoObj(true);
        }else{
            obj.setNovoObj(false);
        }
        return obj;
    }

}
