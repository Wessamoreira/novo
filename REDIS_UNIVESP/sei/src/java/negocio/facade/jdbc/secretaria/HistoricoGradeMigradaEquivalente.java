package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.HistoricoGradeMigradaEquivalenteVO;
import negocio.facade.jdbc.academico.DisciplinaEquivalente;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.HistoricoGradeMigradeEquivalenteInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy

public class HistoricoGradeMigradaEquivalente extends ControleAcesso implements HistoricoGradeMigradeEquivalenteInterfaceFacade{

	private static final long serialVersionUID = 1L;

	public HistoricoGradeMigradaEquivalente() {
 
	}
	
	public void validarDados(HistoricoGradeMigradaEquivalenteVO obj) throws Exception {
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final HistoricoGradeMigradaEquivalenteVO obj, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
        final String sql = "INSERT INTO HistoricoGradeMigradaEquivalente( transferenciaMatrizCurricular, disciplina, matriculaPeriodo, situacao, mediaFinal, matriculaPeriodoApresentarHistorico, anoHistorico, semestreHistorico, frequencia, mediaFinalNotaConceito  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getTransferenciaMatrizCurricularVO().getCodigo() != 0) {
                    sqlInserir.setInt(1, obj.getTransferenciaMatrizCurricularVO().getCodigo());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getDisciplinaVO().getCodigo() != 0) {
                    sqlInserir.setInt(2, obj.getDisciplinaVO().getCodigo());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setInt(3, obj.getMatriculaPeriodoVO().getCodigo());
                sqlInserir.setString(4, obj.getSituacao());
                if(obj.getMediaFinal() == null){
                	sqlInserir.setNull(5, 0);
                }else{
                	sqlInserir.setDouble(5, obj.getMediaFinal());
                }
                sqlInserir.setInt(6, obj.getMatriculaPeriodoApresentarHistoricoVO().getCodigo());
                sqlInserir.setString(7, obj.getAnoHistorico());
                sqlInserir.setString(8, obj.getSemestreHistorico());
                if(obj.getFrequencia() == null){
                	sqlInserir.setNull(9, 0);
                }else{
                	sqlInserir.setDouble(9, obj.getFrequencia());
                }
                if(obj.getMediaFinalNotaConceito().getCodigo() != null){
                	sqlInserir.setInt(10, obj.getMediaFinalNotaConceito().getCodigo());
                }else{
                	sqlInserir.setNull(10, 0);
                }

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
    }
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final HistoricoGradeMigradaEquivalenteVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "UPDATE HistoricoGradeMigradaEquivalente set transferenciaMatrizCurricular=?, disciplina=?, matriculaPeriodo=?, situacao=?, mediaFinal=?, matriculaPeriodoApresentarHistorico=?, anoHistorico=?, semestreHistorico=?, frequencia=?, mediaFinalNotaConceito=?  WHERE codigo = ?";

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getTransferenciaMatrizCurricularVO().getCodigo() != 0) {
                    sqlAlterar.setInt(1, obj.getTransferenciaMatrizCurricularVO().getCodigo());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (obj.getDisciplinaVO().getCodigo() != 0) {
                    sqlAlterar.setInt(2, obj.getDisciplinaVO().getCodigo());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setInt(3, obj.getMatriculaPeriodoVO().getCodigo().intValue());
                sqlAlterar.setString(4, obj.getSituacao());
                sqlAlterar.setDouble(5, obj.getMediaFinal());
                sqlAlterar.setInt(6, obj.getMatriculaPeriodoApresentarHistoricoVO().getCodigo());
                sqlAlterar.setString(7, obj.getAnoHistorico());
                sqlAlterar.setString(8, obj.getSemestreHistorico());
                sqlAlterar.setDouble(9, obj.getFrequencia());
                if(obj.getMediaFinalNotaConceito().getCodigo() != null){
                	sqlAlterar.setInt(10, obj.getMediaFinalNotaConceito().getCodigo());
                }else{
                	sqlAlterar.setNull(10, 0);
                }
                sqlAlterar.setInt(1, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(HistoricoGradeMigradaEquivalenteVO obj, UsuarioVO usuario) throws Exception {
        DisciplinaEquivalente.excluir(getIdEntidade());
        String sql = "DELETE FROM HistoricoGradeMigradaEquivalente WHERE (codigo = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirHistoricoGradeMigradaEquivalente(Integer transferenciaMatrizCurricular, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			HistoricoGradeMigradaEquivalenteVO obj = (HistoricoGradeMigradaEquivalenteVO) e.next();
			obj.getTransferenciaMatrizCurricularVO().setCodigo(transferenciaMatrizCurricular);
			incluir(obj, usuario);
		}
	}
	
	public List<HistoricoGradeMigradaEquivalenteVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
		List<HistoricoGradeMigradaEquivalenteVO> vetResultado = new ArrayList<HistoricoGradeMigradaEquivalenteVO>(0);
		while (tabelaResultado.next()) {
			HistoricoGradeMigradaEquivalenteVO obj = new HistoricoGradeMigradaEquivalenteVO();
			montarDados(obj, tabelaResultado, usuarioVO);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	public void montarDados(HistoricoGradeMigradaEquivalenteVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina"));
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaPeriodo"));
		obj.getMatriculaPeriodoApresentarHistoricoVO().setCodigo(dadosSQL.getInt("matriculaPeriodoApresentarHistorico"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setMediaFinal(dadosSQL.getDouble("mediaFinal"));
		obj.setFrequencia(dadosSQL.getDouble("frequencia"));
		obj.setAnoHistorico(dadosSQL.getString("anoHistorico"));
		obj.setSemestreHistorico(dadosSQL.getString("semestreHistorico"));
		obj.getMediaFinalNotaConceito().setCodigo(dadosSQL.getInt("mediaFinalNotaConceito"));
		if(obj.getMediaFinalNotaConceito().getCodigo() > 0){
			obj.setMediaFinalNotaConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(obj.getMediaFinalNotaConceito().getCodigo()));
		}
	}
	
	public List<HistoricoGradeMigradaEquivalenteVO> consultarPorMatriculaHistoricoGradeMigradaEquivalenteTransferidasGrade(String matricula, Integer gradeAtual, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select historicogradeMigradaEquivalente.* from transferenciamatrizcurricular tr ");
		sb.append(" inner join historicogradeMigradaEquivalente on historicogradeMigradaEquivalente.transferenciamatrizcurricular = tr.codigo ");
		sb.append(" inner join periodoletivo on periodoletivo.gradecurricular = tr.grademigrar ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sb.append(" where matricula = '").append(matricula).append("' and tr.grademigrar = ").append(gradeAtual);
		sb.append(" and gradedisciplina.disciplina = historicogradeMigradaEquivalente.disciplina ");
		sb.append(" order by disciplina ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	@Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirComBaseNaMatriculaPeriodo(Integer matriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder sqlStr = new StringBuilder("DELETE FROM HistoricoGradeMigradaEquivalente WHERE matriculaPeriodo = ").append(matriculaPeriodo);
        try {
            getConexao().getJdbcTemplate().update(sqlStr.toString());
        } finally {
            sqlStr = null;
        }
    }

}
