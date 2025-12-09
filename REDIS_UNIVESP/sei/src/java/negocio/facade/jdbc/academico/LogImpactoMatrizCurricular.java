package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogGradeCurricularInterfaceFacade;
import negocio.interfaces.academico.LogImpactoMatrizCurricularInterfaceFacade;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.LogGradeCurricularVO;
import negocio.comuns.academico.LogImpactoMatrizCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoAlteracaoMatrizCurricularEnum;
import negocio.comuns.arquitetura.UsuarioVO;

@Repository
@Scope("singleton")
@Lazy
public class LogImpactoMatrizCurricular extends ControleAcesso implements LogImpactoMatrizCurricularInterfaceFacade {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final LogImpactoMatrizCurricularVO obj, final UsuarioVO usuario){
        try {
            final String sql = "INSERT INTO logImpactoMatrizCurricular(logGradeCurricular, tituloImpactoMatrizCurricular, msgAvisoAlteracaoGrade, mensagemLiEConcordoComOsTermos, liEConcordoComOsTermos,"
            		+ "tipoExclusaoHistorico, qtdeAulaProgramadaDisciplina, qtdeRegistroAulaDisciplina, qtdeVagaTurmaDisciplina, impactos) VALUES (?,?,?,?,?,?,?,?,?,?) returning codigo";
            
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                  PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                  PGobject jsonObject = new PGobject();
                  if (Uteis.isAtributoPreenchido(obj.getLogGradeCurricularVO())) {
                	  sqlInserir.setInt(1, obj.getLogGradeCurricularVO().getCodigo());
                  } else {
                	  sqlInserir.setNull(1, 0);
                  }
                  sqlInserir.setString(2, obj.getTituloImpactoMatrizCurricularEnum().name());
                  sqlInserir.setString(3, obj.getMsgAvisoAlteracaoGrade().toString());
                  sqlInserir.setString(4, obj.getMensagemLiEConcordoComOsTermos());
                  sqlInserir.setBoolean(5, obj.getLiEConcordoComOsTermos());
                  sqlInserir.setString(6, obj.getTipoExclusaoHistorico());
                  sqlInserir.setInt(7, obj.getQtdeAulaProgramadaDisciplina());
                  sqlInserir.setInt(8, obj.getQtdeRegistroAulaDisciplina());
                  sqlInserir.setInt(9, obj.getQtdeVagaTurmaDisciplina());
                  jsonObject.setType("json");
                  jsonObject.setValue(obj.getImpactos().toJSONString());
                  sqlInserir.setObject(10, jsonObject);
                    return sqlInserir;
                }
         }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        return arg0.getInt("codigo");
                    }
                    return null;
              }
            }));
        } catch (Exception e) {
            throw e;
        }
    }

	@Override
	public void incluirLogImpactosGradeDisciplina(Integer logGradeCurricular, List<LogImpactoMatrizCurricularVO> listaLogImpactoMatrizCurricularVOs, UsuarioVO usuarioVO) {
		for (LogImpactoMatrizCurricularVO logImpactoMatrizCurricularVO : listaLogImpactoMatrizCurricularVOs) {
			logImpactoMatrizCurricularVO.getLogGradeCurricularVO().setCodigo(logGradeCurricular);
			incluir(logImpactoMatrizCurricularVO, usuarioVO);
		}
		
	}
	
	public List<HistoricoVO> montarJSON(JSONArray impactos) {
    	List<HistoricoVO> historicoVOs = new ArrayList<HistoricoVO>(); 
    	JSONObject ob = new JSONObject();
    	
    	for (int i=0; i < impactos.size(); i++ ) {
    		
    		ob = (JSONObject)impactos.get(i);
    		HistoricoVO historicoVO = new HistoricoVO();
    		historicoVO.getMatricula().getAluno().setNome((String) ob.get("aluno"));
    		historicoVO.getMatricula().setMatricula((String) ob.get("matricula"));
    		historicoVO.getMatricula().setSituacao((String) ob.get("situacao_matricula"));
    		historicoVO.getMatriculaPeriodo().setSituacao((String) ob.get("situacao_matricula_periodo"));
    		historicoVO.getMatriculaPeriodo().setAno((String) ob.get("ano"));
    		historicoVO.getMatriculaPeriodo().setSemestre((String) ob.get("semestre"));
    		if(ob.get("disciplina") != null) {
    			historicoVO.getDisciplina().setCodigo( Math.toIntExact((long) ob.get("disciplina")));
    		}
    		
    		historicoVO.setFreguencia((Double) ob.get("frequencia"));
    		historicoVO.setMediaFinal((Double) ob.get("media_final"));
    		historicoVO.setSituacao((String) ob.get("situacao_historico"));
    		
    		historicoVOs.add(historicoVO);
    		
    	}
    	return historicoVOs;
    }
	
	public JSONArray matriculaToJSON(List <MatriculaVO> matriculaVOs) {
		JSONArray jsonArray = new JSONArray();

		for (MatriculaVO matriculaVO : matriculaVOs) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("aluno", matriculaVO.getAluno().getNome());
			jsonObject.put("matricula", matriculaVO.getMatricula());
			jsonObject.put("situacao", matriculaVO.getSituacao());
			
    		jsonArray.add(jsonObject);
		}
		
		return jsonArray;
	}
	
	public List<MatriculaVO> montarMatriculaJSON(JSONArray impactos) {
    	List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(); 
    	JSONObject ob = new JSONObject();
    	
    	for (int i=0; i < impactos.size(); i++ ) {
    		ob = (JSONObject)impactos.get(i);
    		MatriculaVO matriculaVO = new MatriculaVO();
    		matriculaVO.getAluno().setNome((String) ob.get("aluno"));
    		matriculaVO.setMatricula((String) ob.get("matricula"));
    		matriculaVO.setSituacao((String) ob.get("situacao_matricula"));
    		    		
    		matriculaVOs.add(matriculaVO);
    		
    	}
    	return matriculaVOs;
    }
	
	@Override
	public void inicializarDadosJsonImpactos(List<LogImpactoMatrizCurricularVO> listaLogImpactoMatrizCurricularVOs) {
		for (LogImpactoMatrizCurricularVO logImpactoMatrizCurricularVO : listaLogImpactoMatrizCurricularVOs) {
			if (!logImpactoMatrizCurricularVO.getListaHistoricoImpactoAlteracaoVOs().isEmpty()) {
				inicializarDadosJsonImpactosHistorico(logImpactoMatrizCurricularVO);
			}
			if (!logImpactoMatrizCurricularVO.getListaTurmaVOs().isEmpty()) {
				inicializarDadosJsonImpactosTurmaDisciplina(logImpactoMatrizCurricularVO);
			}
			if (!logImpactoMatrizCurricularVO.getListaMatriculaVOs().isEmpty()) {
				inicializarDadosJsonImpactosMatricula(logImpactoMatrizCurricularVO);
			}
		}
	}
	
	@Override
	public void inicializarDadosJsonImpactosHistorico(LogImpactoMatrizCurricularVO logImpactoMatrizCurricularVO) {

		JSONArray jsonArray = new JSONArray();

		for (HistoricoVO historicoVO : logImpactoMatrizCurricularVO.getListaHistoricoImpactoAlteracaoVOs()) {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("codigoHistorico", historicoVO.getCodigo());
			jsonObject.put("aluno", historicoVO.getMatricula().getAluno().getNome());
			jsonObject.put("matricula", historicoVO.getMatricula().getMatricula());
			jsonObject.put("situacao_matricula", historicoVO.getMatricula().getSituacao());
			jsonObject.put("situacao_matricula_periodo", historicoVO.getMatriculaPeriodo().getSituacaoMatriculaPeriodo());
			jsonObject.put("anoHistorico", historicoVO.getAnoHistorico());
			jsonObject.put("semestreHistorico", historicoVO.getSemestreHistorico());

			if (historicoVO.getDisciplina().getCodigo() != 0) {
				jsonObject.put("disciplina", historicoVO.getDisciplina().getCodigo());
			}
			if (historicoVO.getFreguencia() != 0.0) {
				jsonObject.put("frequencia", historicoVO.getFreguencia());
			}
			if (historicoVO.getMediaFinal() != null) {
				jsonObject.put("media_final", historicoVO.getMediaFinal());
			}
			if (historicoVO.getSituacao() != "NC") {
				jsonObject.put("situacao_historico", historicoVO.getSituacao_Apresentar());
			}
			jsonObject.put("matrizCurricular", historicoVO.getMatrizCurricular().getCodigo());
			if (!historicoVO.getGradeDisciplinaVO().getCodigo().equals(0)) {
				jsonObject.put("gradeDisciplina", historicoVO.getGradeDisciplinaVO());
			}

			jsonArray.add(jsonObject);
		}
		logImpactoMatrizCurricularVO.setImpactos(jsonArray);

	}
	
	
	@Override
	public void inicializarDadosJsonImpactosMatricula(LogImpactoMatrizCurricularVO logImpactoMatrizCurricularVO) {
		JSONArray jsonArray = new JSONArray();

		for (MatriculaVO matriculaVO : logImpactoMatrizCurricularVO.getListaMatriculaVOs()) {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("matricula", matriculaVO.getMatricula());
			jsonObject.put("codigoAluno", matriculaVO.getAluno().getCodigo());
			jsonObject.put("situacao", matriculaVO.getSituacao());
			jsonObject.put("situacaoFinanceira", matriculaVO.getSituacaoFinanceira());
			jsonArray.add(jsonObject);
		}
		logImpactoMatrizCurricularVO.setImpactos(jsonArray);

	}
	
	@Override
	public void inicializarDadosJsonImpactosTurmaDisciplina(LogImpactoMatrizCurricularVO logImpactoMatrizCurricularVO) {
		JSONArray jsonArray = new JSONArray();

		for (TurmaVO turmaVO : logImpactoMatrizCurricularVO.getListaTurmaVOs()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("codigoTurma", turmaVO.getCodigo());
			jsonObject.put("identificadorTurma", turmaVO.getIdentificadorTurma());
			jsonObject.put("situacao", turmaVO.getSituacao());

			jsonArray.add(jsonObject);
		}
		logImpactoMatrizCurricularVO.setImpactos(jsonArray);

	}
	
}