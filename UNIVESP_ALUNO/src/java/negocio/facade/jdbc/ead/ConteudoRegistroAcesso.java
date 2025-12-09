package negocio.facade.jdbc.ead;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.ead.ConteudoRegistroAcessoVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ConteudoRegistroAcessoInterfaceFacade;

@Repository
@Lazy
public class ConteudoRegistroAcesso extends ControleAcesso implements ConteudoRegistroAcessoInterfaceFacade {

	 @Override
	    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    public void incluir(final String matricula, final Integer conteudo, final Integer unidadeConteudo, final Integer pagina, final Integer matriculaPeriodoTurmaDisciplina) throws Exception {
	        try {
	            if (matricula != null && !matricula.trim().isEmpty() && conteudo != null && conteudo > 0 && pagina != null && pagina > 0 && unidadeConteudo != null && unidadeConteudo > 0) {
	                final StringBuilder sql = new StringBuilder("INSERT INTO ConteudoRegistroAcesso ");
	                sql.append(" ( matricula, conteudo, conteudoUnidadePagina, dataAcesso, unidadeConteudo, matriculaperiodoturmadisciplina) ");
	                sql.append(" VALUES ( ?, ?, ?, ?, ?, ?)");
	                getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

	                    public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
	                        int x = 1;
	                        PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
	                        sqlInserir.setString(x++, matricula);
	                        sqlInserir.setInt(x++, conteudo);
	                        sqlInserir.setInt(x++, pagina);
	                        sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
	                        sqlInserir.setInt(x++, unidadeConteudo);
	                        sqlInserir.setInt(x++, matriculaPeriodoTurmaDisciplina);
	                        return sqlInserir;
	                    }
	                });
	            }
	        } catch (Exception e) {

	        }

	    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public ConteudoRegistroAcessoVO consultarConteudoUltimoRegistroAcessoPorMatriculaConteudo(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM ConteudoRegistroAcesso ");
        sql.append(" WHERE matricula = '").append(matricula).append("' ");
        sql.append(" and conteudo = ").append(conteudo).append(" ");
        sql.append(" and matriculaperiodoturmadisciplina = ").append(matriculaPeriodoTurmaDisciplina).append(" ");
        sql.append(" order by dataAcesso desc limit 1");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return montarDados(rs);
        }
        return new ConteudoRegistroAcessoVO();
    }

    private ConteudoRegistroAcessoVO montarDados(SqlRowSet rs) {
        ConteudoRegistroAcessoVO obj = new ConteudoRegistroAcessoVO();
        obj.setMatricula(rs.getString("matricula"));
        obj.setCodigo(rs.getInt("codigo"));
        obj.setConteudo(rs.getInt("conteudo"));
        obj.setConteudoUnidadePagina(rs.getInt("conteudoUnidadePagina"));
        obj.setUnidadeConteudo(rs.getInt("unidadeConteudo"));
        obj.setDataAcesso(rs.getTimestamp("dataAcesso"));
        obj.setMatriculaPeriodoTurmaDisciplinaVO(rs.getInt("matriculaperiodoturmadisciplina"));
        obj.setNovoObj(false);
        return obj;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Double consultarTotalPontosAlunoAtingiuConteudo(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo) {
        try {
            StringBuilder sql = new StringBuilder(" select sum(ponto) as pontos from ConteudoUnidadePagina where codigo in ( ");
            sql.append(" SELECT distinct conteudoUnidadePagina FROM ConteudoRegistroAcesso ");
            sql.append(" WHERE matricula = '").append(matricula).append("' ");
            sql.append(" and conteudo = ").append(conteudo).append(" ");
            sql.append(" and matriculaPeriodoTurmaDisciplina = ").append(matriculaPeriodoTurmaDisciplina).append(" ");
            sql.append(" ) ");

            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
            if (rs.next()) {
                return rs.getDouble("pontos");
            }
            return 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer consultarTotalAcessoAlunoRealizouConteudo(String matricula, Integer matriculaPeriodoTurmaDisciplina,  Integer conteudo) {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(" SELECT count(codigo) as qtde FROM ConteudoRegistroAcesso ");
            sql.append(" WHERE matricula = '").append(matricula).append("' ");
            sql.append(" and conteudo = ").append(conteudo).append(" ");
            sql.append(" and matriculaPeriodoTurmaDisciplina = ").append(matriculaPeriodoTurmaDisciplina).append(" ");

            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
            if (rs.next()) {
                return rs.getInt("qtde");
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer consultarTotalAcessoAlunoRealizouPagina(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo, Integer pagina) {
        try {
            StringBuilder sql = new StringBuilder("");
            sql.append(" SELECT count(codigo) as qtde FROM ConteudoRegistroAcesso ");
            sql.append(" WHERE matricula = '").append(matricula).append("' ");
            sql.append(" and conteudo = ").append(conteudo).append(" ");
            sql.append(" and matriculaPeriodoTurmaDisciplina = ").append(matriculaPeriodoTurmaDisciplina).append(" ");
            sql.append(" and conteudoUnidadePagina = ").append(pagina).append(" ");
            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
            if (rs.next()) {
                return rs.getInt("qtde");
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
	 * @author Victor Hugo 17/12/2014
	 * 
	 * Método responsável por trazer os dados necessários para montar um gráfico.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<ConteudoRegistroAcessoVO> consultarDataAcessoPontosTotalAcumuladoConteudo(Integer codigoMatriculaPeriodoTurmaDisciplina) throws Exception {
		
		StringBuilder sqlStr = new StringBuilder();
		
		sqlStr.append(" select dataacesso, sum(ponto) as ponto,");
		sqlStr.append(" (select sum(ponto) from (");
		sqlStr.append(" select dataacesso, sum(ponto) as ponto from (");
		sqlStr.append("		select distinct cast(min(conteudoregistroacesso.dataacesso) as date) as dataacesso, conteudoregistroacesso.conteudounidadepagina, conteudounidadepagina.ponto from conteudounidadepagina");
		sqlStr.append("		inner join conteudoregistroacesso on conteudoregistroacesso.conteudounidadepagina = conteudounidadepagina.codigo");
		sqlStr.append("		where conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append(" 	group by conteudoregistroacesso.conteudounidadepagina, conteudounidadepagina.ponto");
		sqlStr.append(") as t");
		sqlStr.append(" group by dataacesso");
		sqlStr.append(") as g");
		sqlStr.append(" where g.dataacesso <= t.dataacesso");
		sqlStr.append(") as totalacumulado");
		sqlStr.append(" from (");
		sqlStr.append("		select distinct cast(min(conteudoregistroacesso.dataacesso) as date) as dataacesso, conteudoregistroacesso.conteudounidadepagina, conteudounidadepagina.ponto from conteudounidadepagina");
		sqlStr.append("		inner join conteudoregistroacesso on conteudoregistroacesso.conteudounidadepagina = conteudounidadepagina.codigo");
		sqlStr.append("		where conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append("		group by conteudoregistroacesso.conteudounidadepagina, conteudounidadepagina.ponto");
		sqlStr.append(") as t");
		sqlStr.append(" group by dataacesso");
		sqlStr.append(" order by dataacesso asc");
		
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ConteudoRegistroAcessoVO> conteudoRegistroAcessoVOs = new ArrayList<ConteudoRegistroAcessoVO>();
		ConteudoRegistroAcessoVO conteudoRegistroAcessoVO = null;
		
		while(rs.next()) {
			conteudoRegistroAcessoVO = new ConteudoRegistroAcessoVO();
			conteudoRegistroAcessoVO.setPonto(rs.getDouble("ponto"));
			conteudoRegistroAcessoVO.setDataAcesso(rs.getDate("dataacesso"));
			conteudoRegistroAcessoVO.setTotalAcumulado(rs.getDouble("totalacumulado"));
			conteudoRegistroAcessoVOs.add(conteudoRegistroAcessoVO);
		}
		return conteudoRegistroAcessoVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer consultarQuantidadePaginasAcessou(String matricula, Integer matriculaPeriodoTurmaDisciplina, Integer conteudo ) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT count(distinct conteudounidadepagina) as qdte FROM ConteudoRegistroAcesso ");
        sql.append(" inner join unidadeconteudo on unidadeconteudo.codigo = ConteudoRegistroAcesso.unidadeconteudo");
        sql.append(" inner join conteudo on conteudo.codigo = unidadeconteudo.conteudo");
        sql.append(" where conteudo.codigo = ").append(conteudo);
        sql.append(" and ConteudoRegistroAcesso.matricula = '").append(matricula).append("' ");
        sql.append(" and ConteudoRegistroAcesso.matriculaPeriodoTurmaDisciplina = ").append(matriculaPeriodoTurmaDisciplina).append(" ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return rs.getInt("qdte");
        }
        return 0;
    }
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<ConteudoRegistroAcessoVO> consultarPorGestaoEventoConteudoTurma(GestaoEventoConteudoTurmaVO obj) throws Exception {
		StringBuilder sql = new StringBuilder(" select dataacesso,nome, mptd_codigo, ");
		if(obj.getTipoRecurso().isConteudoUnidadePagina()){
			sql.append(" (select count(distinct cra.conteudounidadepagina) from conteudoregistroacesso cra  where cra.conteudo =").append(obj.getConteudoVO().getCodigo()).append(" and  cra.unidadeconteudo= ").append(obj.getUnidadeConteudoVO().getCodigo()).append(" and  cra.conteudounidadepagina= ").append(obj.getConteudoUnidadePaginaVO().getCodigo()).append(" and cra.matriculaperiodoturmadisciplina = mptd_codigo ) qtdPaginas,");
        	sql.append(" 1 as totalPaginas, ");
        	sql.append(" (select sum(ponto) totalPontos from ((select distinct conteudounidadepagina.codigo, conteudounidadepagina.ponto from conteudoregistroacesso cra inner join conteudounidadepagina on conteudounidadepagina.codigo = cra.conteudounidadepagina where cra.conteudo = ").append(obj.getConteudoVO().getCodigo()).append(" and cra.unidadeconteudo = ").append(obj.getUnidadeConteudoVO().getCodigo()).append(" and cra.conteudounidadepagina= ").append(obj.getConteudoUnidadePaginaVO().getCodigo()).append(" and cra.matriculaperiodoturmadisciplina = mptd_codigo)) as t) qtdPontos,	");
        	sql.append(" (select sum(conteudounidadepagina.ponto) from conteudounidadepagina  inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo where unidadeconteudo.conteudo = ").append(obj.getConteudoVO().getCodigo()).append(" and unidadeconteudo.codigo = ").append(obj.getUnidadeConteudoVO().getCodigo()).append(" and conteudounidadepagina.codigo = ").append(obj.getConteudoUnidadePaginaVO().getCodigo()).append(" ) totalPontos ");
        }else if(obj.getTipoRecurso().isUnidadeConteudo()){
        	sql.append(" (select count(distinct cra.conteudounidadepagina) from conteudoregistroacesso cra  where cra.conteudo =").append(obj.getConteudoVO().getCodigo()).append(" and  cra.unidadeconteudo= ").append(obj.getUnidadeConteudoVO().getCodigo()).append(" and cra.matriculaperiodoturmadisciplina = mptd_codigo ) qtdPaginas,");
            sql.append(" (select count(distinct conteudounidadepagina.codigo) from conteudounidadepagina  where unidadeconteudo= ").append(obj.getUnidadeConteudoVO().getCodigo()).append(" ) totalPaginas, ");
            sql.append(" (select sum(ponto) totalPontos from ((select distinct conteudounidadepagina.codigo, conteudounidadepagina.ponto from conteudoregistroacesso cra inner join conteudounidadepagina on conteudounidadepagina.codigo = cra.conteudounidadepagina where cra.conteudo = ").append(obj.getConteudoVO().getCodigo()).append(" and cra.unidadeconteudo = ").append(obj.getUnidadeConteudoVO().getCodigo()).append(" and cra.matriculaperiodoturmadisciplina = mptd_codigo)) as t) qtdPontos,	");
        	sql.append(" (select sum(conteudounidadepagina.ponto) from conteudounidadepagina  inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo where unidadeconteudo.conteudo = ").append(obj.getConteudoVO().getCodigo()).append(" and unidadeconteudo.codigo = ").append(obj.getUnidadeConteudoVO().getCodigo()).append(" ) totalPontos ");
        }else if(obj.getTipoRecurso().isConteudo()){
        	sql.append(" ( select count(distinct cra.conteudounidadepagina) from conteudoregistroacesso cra  where cra.conteudo = ").append(obj.getConteudoVO().getCodigo()).append(" and cra.matriculaperiodoturmadisciplina = mptd_codigo ) qtdPaginas, ");
        	sql.append(" ( select count(distinct conteudounidadepagina.codigo)  from conteudounidadepagina  inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo where unidadeconteudo.conteudo = ").append(obj.getConteudoVO().getCodigo()).append(") totalPaginas, ");
        	sql.append(" (select sum(ponto) totalPontos from ((select distinct conteudounidadepagina.codigo, conteudounidadepagina.ponto from conteudoregistroacesso cra inner join conteudounidadepagina on conteudounidadepagina.codigo = cra.conteudounidadepagina where cra.conteudo = ").append(obj.getConteudoVO().getCodigo()).append(" and cra.matriculaperiodoturmadisciplina = mptd_codigo)) as t) qtdPontos,	");
        	sql.append(" (select sum(conteudounidadepagina.ponto) from conteudounidadepagina  inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo where unidadeconteudo.conteudo = ").append(obj.getConteudoVO().getCodigo()).append(" ) totalPontos ");
        }
		sql.append("  from ( ");
		sql.append(" select distinct cast(min(conteudoregistroacesso.dataacesso) as date) as dataacesso, ");
        sql.append(" mptd.codigo as mptd_codigo, pessoa.nome ");
        sql.append(" from matriculaperiodoturmadisciplina mptd  ");
        sql.append(" inner join matricula on matricula.matricula = mptd.matricula  ");
        sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno  ");
        sql.append(" left join conteudoregistroacesso on mptd.codigo = conteudoregistroacesso.matriculaperiodoturmadisciplina ");
        if(Uteis.isAtributoPreenchido(obj.getUnidadeConteudoVO().getCodigo())){
        	sql.append(" and conteudoregistroacesso.unidadeconteudo = ").append(obj.getUnidadeConteudoVO().getCodigo());	
        }
        if(Uteis.isAtributoPreenchido(obj.getConteudoUnidadePaginaVO().getCodigo())){
        	sql.append(" and conteudoregistroacesso.conteudounidadepagina = ").append(obj.getConteudoUnidadePaginaVO().getCodigo());
        }
        sql.append(" where mptd.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
        sql.append(" and mptd.turma = ").append(obj.getTurmaVO().getCodigo());
        sql.append(" and mptd.ano = '").append(obj.getAno()).append("' ");
        sql.append(" and mptd.semestre = '").append(obj.getSemestre()).append("' ");
        sql.append(" group by pessoa.nome, mptd.codigo  ");
        sql.append(" ) as t ");
        sql.append(" order by t.nome"); 
        
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<ConteudoRegistroAcessoVO> conteudoRegistroAcessoVOs = new ArrayList<ConteudoRegistroAcessoVO>();
		ConteudoRegistroAcessoVO conteudoRegistroAcessoVO = null;
		
		while(rs.next()) {
			conteudoRegistroAcessoVO = new ConteudoRegistroAcessoVO();
			conteudoRegistroAcessoVO.setNomeAluno(rs.getString("nome"));
			conteudoRegistroAcessoVO.setMatriculaPeriodoTurmaDisciplinaVO(rs.getInt("mptd_codigo"));
			conteudoRegistroAcessoVO.setDataAcesso(rs.getDate("dataacesso"));
			conteudoRegistroAcessoVO.setTotalPagina(rs.getDouble("totalPaginas"));
			conteudoRegistroAcessoVO.setPagina(Uteis.arrendondarForcando2CasasDecimais((rs.getDouble("qtdPaginas")*100)/conteudoRegistroAcessoVO.getTotalPagina()));
			conteudoRegistroAcessoVO.setTotalAcumulado(rs.getDouble("totalPontos"));
			conteudoRegistroAcessoVO.setPonto(rs.getDouble("qtdPontos"));
			if(conteudoRegistroAcessoVO.getTotalAcumulado() == 0.0  && conteudoRegistroAcessoVO.getPonto() ==0.0){
				conteudoRegistroAcessoVO.setPorcentagemPonto(100.0);
			}else{
				conteudoRegistroAcessoVO.setPorcentagemPonto(Uteis.arrendondarForcando2CasasDecimais((rs.getDouble("qtdPontos")*100)/conteudoRegistroAcessoVO.getTotalAcumulado()));
			}
			conteudoRegistroAcessoVOs.add(conteudoRegistroAcessoVO);
		}
		return conteudoRegistroAcessoVOs;
		
	}
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public ConteudoRegistroAcessoVO consultarPorMatriculaConteudoUnidadePagina(String matricula, Integer conteudoUnidadePagina) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM ConteudoRegistroAcesso ");
        sql.append(" WHERE matricula = '").append(matricula).append("' ");
        sql.append(" and conteudoUnidadePagina = ").append(conteudoUnidadePagina).append(" ");
        sql.append(" order by dataAcesso limit 1");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return montarDados(rs);
        }
        return new ConteudoRegistroAcessoVO();
    }
    
    public Integer consultarQuantidadeRegistrosPorConteudoUnidadePaginaUnidadeConteudo(Integer conteudoUnidadePagina, Integer unidadeConteudo) throws Exception {
    	StringBuilder sql = new StringBuilder().append("select count(codigo) as quantidadeRegistros from conteudoregistroacesso where 1 = 1 ");
    	if (Uteis.isAtributoPreenchido(conteudoUnidadePagina)) {
    		sql.append(" and conteudoUnidadePagina = ").append(conteudoUnidadePagina);
    	}
    	if (Uteis.isAtributoPreenchido(unidadeConteudo)) {
    		sql.append(" and unidadeConteudo = ").append(unidadeConteudo);
    	}
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	return rs.next() ? rs.getInt("quantidadeRegistros") : 0;
    }
}
