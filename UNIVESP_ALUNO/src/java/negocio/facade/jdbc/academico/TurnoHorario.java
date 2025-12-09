/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurnoHorarioInterfaceFacade;

/**
 *
 * @author Otimize-Not
 */
@Repository
@Lazy
public class TurnoHorario extends ControleAcesso implements TurnoHorarioInterfaceFacade {

		public void validarDados(TurnoHorarioVO turnoHorarioVO, DiaSemana diaSemana, String horarioAulaAnterior) throws ConsistirException {
	        
	        StringBuilder sb = null;
	        try {
	
	            if (turnoHorarioVO.getHorarioInicioAula().length() < 5) {
	                sb = new StringBuilder(UteisJSF.internacionalizar("msg_TurnoHorario_aulaNumero"));
	                sb.append(" ").append(turnoHorarioVO.getNumeroAula()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_noa"));
	                sb.append(" ").append(diaSemana.getDescricao()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_horarioInvalido"));
	                throw new ConsistirException(sb.toString());
	            }
	            turnoHorarioVO.setHorarioInicioAula(Uteis.gethoraHHMMSS(turnoHorarioVO.getHorarioInicioAula()));
	            if (turnoHorarioVO.getHorarioInicioAula().equals("")) {
	                sb = new StringBuilder(UteisJSF.internacionalizar("msg_TurnoHorario_aulaNumero"));
	                sb.append(" ").append(turnoHorarioVO.getNumeroAula()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_noa"));
	                sb.append(" ").append(diaSemana.getDescricao()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_deveSerInformado"));
	                throw new ConsistirException(sb.toString());
	            }
	            if (horarioAulaAnterior != null && horarioAulaAnterior.trim().length() == 5  && Uteis.realizarValidacaoHora1MaiorHora2(horarioAulaAnterior, turnoHorarioVO.getHorarioInicioAula())) {
	                sb = new StringBuilder(UteisJSF.internacionalizar("msg_TurnoHorario_aulaNumero"));
	                sb.append(" ").append(turnoHorarioVO.getNumeroAula()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_noa"));
	                sb.append(" ").append(diaSemana.getDescricao()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_horarioAtualMaiorAnterior"));
	                throw new ConsistirException(sb.toString());
	            }
	
	        } catch(ConsistirException e){
	            throw e;
	        }finally {
	            sb = null;
	            diaSemana = null;
	            horarioAulaAnterior = null;
	        }
	
	
		}

	public void validarDadosTurnoHorarioVO(List<TurnoHorarioVO> turnoHorarioVOs, DiaSemana diaSemana) throws ConsistirException {
	    String horarioAulaAnterior = null;
	    StringBuilder sb = null;
	    try {
	        for (TurnoHorarioVO turnoHorarioVO : turnoHorarioVOs) {
	            if (turnoHorarioVO.getHorarioInicioAula().length() < 5) {
	                sb = new StringBuilder(UteisJSF.internacionalizar("msg_TurnoHorario_aulaNumero"));
	                sb.append(" ").append(turnoHorarioVO.getNumeroAula()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_noa"));
	                sb.append(" ").append(diaSemana.getDescricao()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_horarioInvalido"));
	                throw new ConsistirException(sb.toString());
	            }
	            turnoHorarioVO.setHorarioInicioAula(Uteis.gethoraHHMMSS(turnoHorarioVO.getHorarioInicioAula()));
	            if (turnoHorarioVO.getHorarioInicioAula().equals("")) {
	                sb = new StringBuilder(UteisJSF.internacionalizar("msg_TurnoHorario_aulaNumero"));
	                sb.append(" ").append(turnoHorarioVO.getNumeroAula()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_noa"));
	                sb.append(" ").append(diaSemana.getDescricao()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_deveSerInformado"));
	                throw new ConsistirException(sb.toString());
	            }
	            if (horarioAulaAnterior != null && horarioAulaAnterior.trim().length() == 5  && Uteis.realizarValidacaoHora1MaiorHora2(horarioAulaAnterior, turnoHorarioVO.getHorarioInicioAula())) {
	                sb = new StringBuilder(UteisJSF.internacionalizar("msg_TurnoHorario_aulaNumero"));
	                sb.append(" ").append(turnoHorarioVO.getNumeroAula()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_noa"));
	                sb.append(" ").append(diaSemana.getDescricao()).append(" ").append(UteisJSF.internacionalizar("msg_TurnoHorario_horarioAtualMaiorAnterior"));
	                throw new ConsistirException(sb.toString());
	            }
	            horarioAulaAnterior = turnoHorarioVO.getHorarioFinalAula();
	        }
	    } catch(ConsistirException e){
	        throw e;
	    } finally {
	        sb = null;
	        diaSemana = null;
	        turnoHorarioVOs = null;
	        horarioAulaAnterior = null;
	    }
	
	
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(TurnoHorarioVO turnoHorarioVO) throws Exception {
        if (turnoHorarioVO.getNovoObj()) {
            incluir(turnoHorarioVO);
        } else {
            alterar(turnoHorarioVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TurnoHorarioVO obj) throws Exception {
        final String sql = "INSERT INTO TurnoHorario( numeroAula, horarioInicioAula, horarioFinalAula, duracaoAula, turno, diaSemana) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getNumeroAula());
                sqlInserir.setString(2, obj.getHorarioInicioAula());
                sqlInserir.setString(3, obj.getHorarioFinalAula());
                sqlInserir.setInt(4, obj.getDuracaoAula());
                sqlInserir.setInt(5, obj.getTurno());
                sqlInserir.setString(6, obj.getDiaSemana().getValor());
                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                    obj.setNovoObj(false);
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TurnoHorarioVO obj) throws Exception {
        final String sql = "UPDATE TurnoHorario set numeroAula = ?, horarioInicioAula = ?, horarioFinalAula = ?, duracaoAula = ?, turno = ?, diaSemana = ? WHERE (codigo = ?) returning codigo";
        if (!(Boolean) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getNumeroAula());
                sqlInserir.setString(2, obj.getHorarioInicioAula());
                sqlInserir.setString(3, obj.getHorarioFinalAula());
                sqlInserir.setInt(4, obj.getDuracaoAula());
                sqlInserir.setInt(5, obj.getTurno());
                sqlInserir.setString(6, obj.getDiaSemana().getValor());
                sqlInserir.setInt(7, obj.getCodigo());
                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                return arg0.next();
            }
        })) {
            incluir(obj);
            return;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TurnoHorarioVO turnoHorarioVO) throws Exception {
        getConexao().getJdbcTemplate().update("DELETE FROM TurnoHorario WHERE codigo = ?", turnoHorarioVO.getCodigo());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirTurnoHorarioVOs(Integer turno) throws Exception {
        getConexao().getJdbcTemplate().update("DELETE FROM TurnoHorario WHERE turno = ?", turno);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirTurnoHorarioVOs(Integer turno, List<TurnoHorarioVO> turnoHorarioVOs) throws Exception {
        for (TurnoHorarioVO turnoHorarioVO : turnoHorarioVOs) {
            turnoHorarioVO.setTurno(turno);
            incluir(turnoHorarioVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarTurnoHorarioVOs(Integer turno, List<TurnoHorarioVO> turnoHorarioVOs, DiaSemana diaSemana) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM TurnoHorario WHERE turno = ? and diaSemana = ? ");
        for (TurnoHorarioVO turnoHorarioVO : turnoHorarioVOs) {
            sb.append(" and codigo <> ").append(turnoHorarioVO.getCodigo());
        }
        getConexao().getJdbcTemplate().update(sb.toString(), turno, diaSemana.getValor());
        for (TurnoHorarioVO turnoHorarioVO : turnoHorarioVOs) {
            turnoHorarioVO.setTurno(turno);
            if (turnoHorarioVO.getCodigo().equals(0)) {
                incluir(turnoHorarioVO);
            } else {
                alterar(turnoHorarioVO);
            }
        }
    }

    public void realizarCalculoHorarioFinal(TurnoVO turnoVO, TurnoHorarioVO obj) throws ConsistirException {
        validarDados(obj, obj.getDiaSemana(), realizarBuscaHorarioAnterior(turnoVO, obj));
        obj.setHorarioFinalAula(Uteis.getCalculodeHoraSemIntervalo(obj.getHorarioInicioAula(), 1, obj.getDuracaoAula()));
    }

    public String realizarBuscaHorarioAnterior(TurnoVO turnoVO, TurnoHorarioVO turnoHorarioVO) {
        if(turnoHorarioVO.getNumeroAula().intValue() == 1){
            return null;
        }
        List<TurnoHorarioVO> turnoHorarioVOs = null;
        if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.DOMINGO)) {
            turnoHorarioVOs = turnoVO.getTurnoHorarioDomingo();
        } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.SEGUNGA)) {
            turnoHorarioVOs = turnoVO.getTurnoHorarioSegunda();
        } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.TERCA)) {
            turnoHorarioVOs = turnoVO.getTurnoHorarioTerca();
        } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.QUARTA)) {
            turnoHorarioVOs = turnoVO.getTurnoHorarioQuarta();
        } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.QUINTA)) {
            turnoHorarioVOs = turnoVO.getTurnoHorarioQuinta();
        } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.SEXTA)) {
            turnoHorarioVOs = turnoVO.getTurnoHorarioSexta();
        } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.SABADO)) {
            turnoHorarioVOs = turnoVO.getTurnoHorarioSabado();
        }

        for(TurnoHorarioVO obj : turnoHorarioVOs ){
            if(obj.getNumeroAula().equals(turnoHorarioVO.getNumeroAula() - 1)){
                return obj.getHorarioFinalAula();
            }
        }
        return null;

    }

    public void consultarTurnoHorarioVOsSeparadoPorDiaSemana(TurnoVO turno) throws Exception {
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("SELECT * FROM TurnoHorario where turno = ? order by numeroaula ", turno.getCodigo());
        while (rs.next()) {
            TurnoHorarioVO turnoHorarioVO = montarDados(rs);
            if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.DOMINGO)) {
                turno.getTurnoHorarioDomingo().add(turnoHorarioVO);
            } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.SEGUNGA)) {
                turno.getTurnoHorarioSegunda().add(turnoHorarioVO);
            } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.TERCA)) {
                turno.getTurnoHorarioTerca().add(turnoHorarioVO);
            } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.QUARTA)) {
                turno.getTurnoHorarioQuarta().add(turnoHorarioVO);
            } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.QUINTA)) {
                turno.getTurnoHorarioQuinta().add(turnoHorarioVO);
            } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.SEXTA)) {
                turno.getTurnoHorarioSexta().add(turnoHorarioVO);
            } else if (turnoHorarioVO.getDiaSemana().equals(DiaSemana.SABADO)) {
                turno.getTurnoHorarioSabado().add(turnoHorarioVO);
            }
        }
    }

    public List<TurnoHorarioVO> consultarTurnoHorarioVOs(Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar("Turno", controlarAcesso, usuario);
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("SELECT * FROM TurnoHorario where turno = ? ", turno));
    }

    public List<TurnoHorarioVO> montarDadosConsulta(SqlRowSet rs) {
        List<TurnoHorarioVO> turnoHorarioVOs = new ArrayList<TurnoHorarioVO>(0);
        while (rs.next()) {
            turnoHorarioVOs.add(montarDados(rs));
        }
        return turnoHorarioVOs;
    }

    public TurnoHorarioVO montarDados(SqlRowSet rs) {
        TurnoHorarioVO obj = new TurnoHorarioVO();
        obj.setCodigo(rs.getInt("codigo"));
        obj.setTurno(rs.getInt("turno"));
        obj.setDuracaoAula(rs.getInt("duracaoAula"));
        obj.setDiaSemana(DiaSemana.getEnum(rs.getString("diaSemana")));
        obj.setNumeroAula(rs.getInt("numeroAula"));
        obj.setHorarioFinalAula(rs.getString("horarioFinalAula"));
        obj.setHorarioInicioAula(rs.getString("horarioInicioAula"));
		obj.setNovoObj(Boolean.FALSE);
        return obj;
    }
    
    
    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca(Integer turno, String horario, String diaSemana) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select * from turnohorario  where turno =  ").append(turno).append("  ");
    	if (Uteis.isAtributoPreenchido(horario)) {
    		sb.append(" and (horarioinicioaula <= '").append(horario).append("' and horariofinalaula >= '").append(horario).append("') ");
		}    	
    	sb.append(" and diasemana = '").append(diaSemana).append("' order by diasemana, horarioinicioaula limit 1 ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	TurnoHorarioVO turnoHorarioVO = new TurnoHorarioVO();
    	if (tabelaResultado.next()) {
    		turnoHorarioVO = montarDados(tabelaResultado);
    	}
    	return turnoHorarioVO;
    }

    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_ProxHorario(Integer turno, String horario, String diaSemana) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select * from turnohorario  where turno =  ").append(turno).append("  ");
    	sb.append(" and horarioinicioaula > '").append(horario).append("' ");
    	sb.append(" and diasemana = '").append(diaSemana).append("' order by diasemana, horarioinicioaula limit 1 ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	TurnoHorarioVO turnoHorarioVO = new TurnoHorarioVO();
    	if (tabelaResultado.next()) {
    		turnoHorarioVO = montarDados(tabelaResultado);
    	}
    	return turnoHorarioVO;
    }
    
    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_ProxDiaHorario(Integer turno, String horario, String diaSemana) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select * from turnohorario  where turno =  ").append(turno).append("  ");
    	sb.append(" and horarioinicioaula > '").append(horario).append("' ");
    	sb.append(" and diasemana = '").append(diaSemana).append("' order by diasemana, horarioinicioaula limit 1 ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	TurnoHorarioVO turnoHorarioVO = new TurnoHorarioVO();
    	if (tabelaResultado.next()) {
    		turnoHorarioVO = montarDados(tabelaResultado);
    	}
    	return turnoHorarioVO;
    }

    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_UltimoHorarioDia(Integer turno, String horario, String diaSemana) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select * from turnohorario  where turno =  ").append(turno).append("  ");
    	sb.append(" and horariofinalaula > '").append(horario).append("' ");
    	sb.append(" and diasemana = '").append(diaSemana).append("' order by diasemana, horarioinicioaula desc limit 1 ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	TurnoHorarioVO turnoHorarioVO = new TurnoHorarioVO();
    	if (tabelaResultado.next()) {
    		turnoHorarioVO = montarDados(tabelaResultado);
    	}
    	return turnoHorarioVO;
    }

    public TurnoHorarioVO consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_ProxDiaHorario(Integer turno, String diaSemana) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select * from turnohorario  where turno =  ").append(turno).append("  ");
    	sb.append(" and diasemana <> '").append(diaSemana).append("' ");
    	sb.append(" and diasemana > '").append(diaSemana).append("' ");
    	sb.append(" order by diasemana, horarioinicioaula limit 1 ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	TurnoHorarioVO turnoHorarioVO = new TurnoHorarioVO();
    	if (tabelaResultado.next()) {
    		turnoHorarioVO = montarDados(tabelaResultado);
    	} else {
    		if (Integer.parseInt(diaSemana) == 8) {
    			diaSemana = "01";
    		} else {
    			diaSemana = "0" + (Integer.parseInt(diaSemana) + 1);
    		}
    		return this.consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_ProxDiaHorario(turno, diaSemana);
    	}
    	return turnoHorarioVO;
    }
    
	public List<String> consultarDiasSemanaHorarioTurnoPorTurmaCenso(Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("select turnohorario.diasemana ");
		sql.append(" from turnohorario");
		sql.append(" inner join turno on turnohorario.turno = turno.codigo");
		sql.append(" inner join turma on turma.turno = turno.codigo ");
		sql.append(" where turma.codigo = ").append(turma);			
		sql.append(" ORDER BY turnohorario.diasemana ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<String> listaDiasSemanas = new ArrayList<String>(0);
		while (tabelaResultado.next()) {
			TurnoHorarioVO turnoHorarioVO = new TurnoHorarioVO();
			turnoHorarioVO.setDiaSemana(DiaSemana.getEnum(tabelaResultado.getString("diaSemana")));
			listaDiasSemanas.add(turnoHorarioVO.getDiaSemana().getValor());
		}
		return listaDiasSemanas;		
	}
}
