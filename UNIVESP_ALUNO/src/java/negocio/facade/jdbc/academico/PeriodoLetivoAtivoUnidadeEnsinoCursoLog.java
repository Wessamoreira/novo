package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoLogVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoLogInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class PeriodoLetivoAtivoUnidadeEnsinoCursoLog extends ControleAcesso implements PeriodoLetivoAtivoUnidadeEnsinoCursoLogInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4469418972457874642L;

	public void preencherIncluirProcessoMatriculaLog(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO, String nivelProcessoMatricula, String operacao, UsuarioVO usuario) throws Exception {
		try { 
			PeriodoLetivoAtivoUnidadeEnsinoCursoLogVO obj = new PeriodoLetivoAtivoUnidadeEnsinoCursoLogVO();
			obj.setCodigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO(periodoLetivoAtivoUnidadeEnsinoCursoVO.getCodigo());
	        obj.setSituacao(periodoLetivoAtivoUnidadeEnsinoCursoVO.getSituacao());
	        obj.getCursoVO().setCodigo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getCursoVO().getCodigo());
	        obj.getTurnoVO().setCodigo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getTurnoVO().getCodigo());
	        obj.setSemestreReferenciaPeriodoLetivo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getSemestreReferenciaPeriodoLetivo());
	        obj.setAnoReferenciaPeriodoLetivo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getAnoReferenciaPeriodoLetivo());
	        obj.setTipoPeriodoLetivo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getTipoPeriodoLetivo());
	        obj.setDataInicioPeriodoLetivo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataInicioPeriodoLetivo());
	        obj.setDataFimPeriodoLetivo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataFimPeriodoLetivo());
	        obj.setDataAbertura(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataAbertura());
	        obj.getReponsavelAbertura().setCodigo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getResponsavelFechamento().getCodigo());
	        obj.setDataFechamento(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataFechamento());
	        obj.getResponsavelFechamento().setCodigo(periodoLetivoAtivoUnidadeEnsinoCursoVO.getResponsavelFechamento().getCodigo());
	        obj.setOperacao(operacao);
	        obj.getResponsavel().setCodigo(usuario.getCodigo());
	        incluir(obj, nivelProcessoMatricula);
		} catch (Exception e) {
	         throw e;
	    }
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PeriodoLetivoAtivoUnidadeEnsinoCursoLogVO obj, final String nivelProcessoMatricula) throws Exception {
        try {
            final String sql = "INSERT INTO PeriodoLetivoAtivoUnidadeEnsinoCursoLog( situacao, curso, semestreReferenciaPeriodoLetivo, "
                    + "anoReferenciaPeriodoLetivo, tipoPeriodoLetivo, dataInicioPeriodoLetivo, dataFimPeriodoLetivo, dataAbertura, "
                    + "reponsavelAbertura, dataFechamento, responsavelFechamento, operacao, responsavel, dataOperacao, codigoPeriodoLetivoAtivoUnidadeEnsinoCurso, turno ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getSituacao());
                    sqlInserir.setInt(2, obj.getCursoVO().getCodigo().intValue());
                    sqlInserir.setString(3, obj.getSemestreReferenciaPeriodoLetivo());
                    sqlInserir.setString(4, obj.getAnoReferenciaPeriodoLetivo());
                    if (nivelProcessoMatricula.equals("PO") || nivelProcessoMatricula.equals("EX")) {
                        obj.setTipoPeriodoLetivo("IN");
                        sqlInserir.setString(5, obj.getTipoPeriodoLetivo());
                        sqlInserir.setNull(6, 0);
                        sqlInserir.setNull(7, 0);
                    } else {
                        sqlInserir.setString(5, obj.getTipoPeriodoLetivo());
                        sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataInicioPeriodoLetivo()));
                        sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataFimPeriodoLetivo()));
                    }
                    sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataAbertura()));
                    sqlInserir.setInt(9, obj.getReponsavelAbertura().getCodigo().intValue());
                    sqlInserir.setDate(10, Uteis.getDataJDBC(obj.getDataFechamento()));
                    if (obj.getResponsavelFechamento().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(11, obj.getResponsavelFechamento().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
                    sqlInserir.setString(12, obj.getOperacao());
                    sqlInserir.setInt(13, obj.getResponsavel().getCodigo());
                    sqlInserir.setTimestamp(14, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlInserir.setInt(15, obj.getCodigoPeriodoLetivoAtivoUnidadeEnsinoCursoVO());
                    sqlInserir.setInt(16, obj.getTurnoVO().getCodigo().intValue());
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
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

}
