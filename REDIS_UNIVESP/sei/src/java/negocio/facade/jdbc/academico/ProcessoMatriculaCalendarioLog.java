package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ProcessoMatriculaCalendarioLogVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ProcessoMatriculaCalendarioLogInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ProcessoMatriculaCalendarioLog extends ControleAcesso implements ProcessoMatriculaCalendarioLogInterfaceFacade{
	
	public void preencherIncluirProcessoMatriculaCalendarioLog(ProcessoMatriculaVO processoMatricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO, String operacao, UsuarioVO usuario) throws Exception {
//		try { 
//			ProcessoMatriculaCalendarioLogVO obj = new ProcessoMatriculaCalendarioLogVO();
//			obj.setProcessoMatricula(processoMatriculaCalendarioVO.getProcessoMatricula());
//			obj.setMesVencimentoPrimeiraMensalidade(processoMatriculaCalendarioVO.getMesVencimentoPrimeiraMensalidade());
//			obj.setDiaVencimentoPrimeiraMensalidade(processoMatriculaCalendarioVO.getDiaVencimentoPrimeiraMensalidade());
//			obj.setAnoVencimentoPrimeiraMensalidade(processoMatriculaCalendarioVO.getAnoVencimentoPrimeiraMensalidade());
//			obj.getCursoVO().setCodigo(processoMatriculaCalendarioVO.getCursoVO().getCodigo());
//			obj.getTurnoVO().setCodigo(processoMatriculaCalendarioVO.getTurnoVO().getCodigo());
//			obj.setDataInicioMatricula(processoMatriculaCalendarioVO.getDataInicioMatricula());
//			obj.setDataFinalMatricula(processoMatriculaCalendarioVO.getDataFinalMatricula());
//			obj.setDataInicioInclusaoDisciplina(processoMatriculaCalendarioVO.getDataInicioInclusaoDisciplina());
//			obj.setDataFinalInclusaoDisciplina(processoMatriculaCalendarioVO.getDataFinalInclusaoDisciplina());
//			obj.setDataInicioMatForaPrazo(processoMatriculaCalendarioVO.getDataInicioMatForaPrazo());
//			obj.setDataFinalMatForaPrazo(processoMatriculaCalendarioVO.getDataFinalMatForaPrazo());
//			obj.setDataVencimentoMatricula(processoMatriculaCalendarioVO.getDataVencimentoMatricula());
//			obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setCodigo(processoMatriculaCalendarioVO.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo());
//			obj.setUsarDataVencimentoDataMatricula(processoMatriculaCalendarioVO.getUsarDataVencimentoDataMatricula());
//			obj.setMesSubsequenteMatricula(processoMatriculaCalendarioVO.getMesSubsequenteMatricula());
//			obj.setMesDataBaseGeracaoParcelas(processoMatriculaCalendarioVO.getMesDataBaseGeracaoParcelas());
//	        obj.setOperacao(operacao);
//	        obj.setResponsavel(usuario.getCodigo());
//	        incluir(processoMatricula, obj, usuario);
//		} catch (Exception e) {
//	         throw e;
//	    }
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProcessoMatriculaVO processoMatriculaVO, final ProcessoMatriculaCalendarioLogVO obj, UsuarioVO usuario) throws Exception {
//		final String sql = "INSERT INTO ProcessoMatriculaCalendarioLog( processoMatricula, unidadeEnsinoCurso, dataInicioMatricula, dataFinalMatricula, "
//				+ "dataInicioInclusaoDisciplina, dataFinalInclusaoDisciplina, dataInicioMatForaPrazo, dataFinalMatForaPrazo, dataVencimentoMatricula, "
//				+ "mesVencimentoPrimeiraMensalidades, diaVencimentoPrimeiraMensalidades, anoVencimentoPrimeiraMensalidades, "
//				+ "periodoLetivoAtivoUnidadeEnsinoCurso, usarDataVencimentoDataMatricula, mesSubsequenteMatricula,"
//                                + "mesDataBaseGeracaoParcelas, responsavel, operacao, dataOperacao ) "
//                                + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? , ?, ?, ?, ?, ?, ? )";
//		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
//
//			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
//				sqlInserir.setInt(1, obj.getProcessoMatricula().intValue());
//				sqlInserir.setInt(2, obj.getUnidadeEnsinoCurso().getCodigo().intValue());
//				if (processoMatriculaVO.getNivelProcessoMatricula().equals(TipoNivelEducacional.POS_GRADUACAO.getValor())) {
//					sqlInserir.setNull(3, 0);
//					sqlInserir.setNull(4, 0);
//					sqlInserir.setNull(5, 0);
//					sqlInserir.setNull(6, 0);
//					sqlInserir.setNull(7, 0);
//					sqlInserir.setNull(8, 0);
//				} else {
//					sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataInicioMatricula()));
//					sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFinalMatricula()));
//					sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataInicioInclusaoDisciplina()));
//					sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataFinalInclusaoDisciplina()));
//					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataInicioMatForaPrazo()));
//					sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataFinalMatForaPrazo()));
//				}
//				if (obj.getDataVencimentoMatricula() != null) {
//					sqlInserir.setDate(9, Uteis.getDataJDBC(obj.getDataVencimentoMatricula()));
//				} else {
//					sqlInserir.setNull(9, 0);
//				}
//				if (obj.getMesVencimentoPrimeiraMensalidade() != null && obj.getMesVencimentoPrimeiraMensalidade() != 0) {
//					sqlInserir.setInt(10, obj.getMesVencimentoPrimeiraMensalidade().intValue());
//				} else {
//					sqlInserir.setNull(10, 0);
//				}
//				if (obj.getDiaVencimentoPrimeiraMensalidade() != null && obj.getDiaVencimentoPrimeiraMensalidade() != 0) {
//					sqlInserir.setInt(11, obj.getDiaVencimentoPrimeiraMensalidade().intValue());
//				} else {
//					sqlInserir.setNull(11, 0);
//				}
////				sqlInserir.setInt(11, obj.getDiaVencimentoPrimeiraMensalidade().intValue());
//				if (obj.getAnoVencimentoPrimeiraMensalidade() != null && obj.getAnoVencimentoPrimeiraMensalidade() != 0) {
//					sqlInserir.setInt(12, obj.getAnoVencimentoPrimeiraMensalidade().intValue());
//				} else {
//					sqlInserir.setNull(12, 0);
//				}
//				sqlInserir.setInt(13, obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo());
//				sqlInserir.setBoolean(14, obj.getUsarDataVencimentoDataMatricula());
//				sqlInserir.setBoolean(15, obj.getMesSubsequenteMatricula());
//				sqlInserir.setBoolean(16, obj.getMesDataBaseGeracaoParcelas());
//				sqlInserir.setInt(17, obj.getResponsavel());
//                sqlInserir.setString(18, obj.getOperacao());
//                sqlInserir.setTimestamp(19, Uteis.getDataJDBCTimestamp(new Date()));
//				return sqlInserir;
//			}
//		});
	}

}
