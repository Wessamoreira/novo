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

import negocio.comuns.academico.ProcessoMatriculaLogVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ProcessoMatriculaLogInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ProcessoMatriculaLog extends ControleAcesso implements ProcessoMatriculaLogInterfaceFacade {

	public void preencherIncluirProcessoMatriculaLog(ProcessoMatriculaVO processoMatricula, String operacao, UsuarioVO usuario) throws Exception {
		try { 
			ProcessoMatriculaLogVO obj = new ProcessoMatriculaLogVO();
			obj.setCodigoProcessoMatricula(processoMatricula.getCodigo());
	        obj.setDescricao(processoMatricula.getDescricao());
	        obj.setData(processoMatricula.getData()); 
	        
	        obj.setSituacao(processoMatricula.getSituacao());
	        obj.setNivelProcessoMatricula(processoMatricula.getNivelProcessoMatricula());
	        obj.setDataInicio(processoMatricula.getDataInicio());
	        obj.setDataFinal(processoMatricula.getDataFinal());
	        obj.setValidoPelaInternet(processoMatricula.getValidoPelaInternet());
	        obj.setApresentarProcessoVisaoAluno(processoMatricula.getApresentarProcessoVisaoAluno());
	        obj.setPermiteIncluirExcluirDisciplinaVisaoAluno(processoMatricula.getPermiteIncluirExcluirDisciplinaVisaoAluno());
	        obj.setMensagemApresentarVisaoAluno(processoMatricula.getMensagemApresentarVisaoAluno());
	        obj.setDataInicioMatriculaOnline(processoMatricula.getDataInicioMatriculaOnline());
	        obj.setDataFimMatriculaOnline(processoMatricula.getDataFimMatriculaOnline());
	        obj.setExigeConfirmacaoPresencial(processoMatricula.getExigeConfirmacaoPresencial());
	        obj.setOperacao(operacao);
	        obj.setResponsavel(usuario.getCodigo());
	        incluir(obj, usuario);
		} catch (Exception e) {
	         throw e;
	    }
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProcessoMatriculaLogVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO ProcessoMatriculaLog( descricao, data, dataInicio, dataFinal, validoPelaInternet, exigeConfirmacaoPresencial, "
                    + "unidadeEnsino, situacao, nivelProcessoMatricula, apresentarProcessoVisaoAluno, dataInicioMatriculaOnline, dataFimMatriculaOnline, " +
                    "  mensagemApresentarVisaoAluno, permiteIncluirExcluirDisciplinaVisaoAluno, responsavel, operacao, codigoProcessoMatricula, dataOperacao ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataInicio()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFinal()));
                    sqlInserir.setBoolean(5, obj.isValidoPelaInternet().booleanValue());
                    sqlInserir.setBoolean(6, obj.isExigeConfirmacaoPresencial().booleanValue());
                    sqlInserir.setNull(7, 0);
                    sqlInserir.setString(8, obj.getSituacao());
                    sqlInserir.setString(9, obj.getNivelProcessoMatricula());
                    sqlInserir.setBoolean(10, obj.getApresentarProcessoVisaoAluno());
                    sqlInserir.setDate(11, Uteis.getDataJDBC(obj.getDataInicioMatriculaOnline()));
                    sqlInserir.setDate(12, Uteis.getDataJDBC(obj.getDataFimMatriculaOnline()));
                    sqlInserir.setString(13, obj.getMensagemApresentarVisaoAluno());
                    sqlInserir.setBoolean(14, obj.getPermiteIncluirExcluirDisciplinaVisaoAluno());
                    sqlInserir.setInt(15, obj.getResponsavel());
                    sqlInserir.setString(16, obj.getOperacao());
                    sqlInserir.setInt(17, obj.getCodigoProcessoMatricula());
                    sqlInserir.setTimestamp(18, Uteis.getDataJDBCTimestamp(new Date()));
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
            obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

}
