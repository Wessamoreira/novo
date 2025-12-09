package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.InclusaoHistoricoAlunoDisciplinaVO;
import negocio.comuns.academico.InclusaoHistoricoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.InclusaoHistoricoAlunoDisciplinaInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class InclusaoHistoricoAlunoDisciplina extends ControleAcesso
		implements InclusaoHistoricoAlunoDisciplinaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4866338166226811717L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirInclusaoHistoricoAlunoDisciplinaVOs(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, UsuarioVO usuarioVO) throws Exception {
		for(InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO: inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs()){
			getFacadeFactory().getHistoricoFacade().incluir(inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO(), usuarioVO);
			inclusaoHistoricoAlunoDisciplinaVO.setInclusaoHistoricoAlunoVO(inclusaoHistoricoAlunoVO);
			incluir(inclusaoHistoricoAlunoDisciplinaVO, usuarioVO);
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO, UsuarioVO usuarioVO) throws Exception{
			final String sql = "INSERT INTO inclusaoHistoricoAlunoDisciplina( inclusaoHistoricoAluno, historico, matriculaAproveitarDisciplina, mapaEquivalenciaDisciplina ) VALUES ( ?, ?, ?, ? ) returning codigo "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			inclusaoHistoricoAlunoDisciplinaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
							PreparedStatement sqlInserir = arg0.prepareStatement(sql);
							sqlInserir.setInt(1, inclusaoHistoricoAlunoDisciplinaVO.getInclusaoHistoricoAlunoVO().getCodigo());
							sqlInserir.setInt(2, inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getCodigo());
							if(Uteis.isAtributoPreenchido(inclusaoHistoricoAlunoDisciplinaVO.getMatriculaAproveitarDisciplinaVO().getMatricula())){
								sqlInserir.setString(3, inclusaoHistoricoAlunoDisciplinaVO.getMatriculaAproveitarDisciplinaVO().getMatricula());
							}else{
								sqlInserir.setNull(3, 0);
							}
							if(Uteis.isAtributoPreenchido(inclusaoHistoricoAlunoDisciplinaVO.getMapaEquivalenciaDisciplinaVO().getCodigo())){
								sqlInserir.setInt(4, inclusaoHistoricoAlunoDisciplinaVO.getMapaEquivalenciaDisciplinaVO().getCodigo());
							}else{
								sqlInserir.setNull(4, 0);
							}
							return sqlInserir;
						}
					}, new ResultSetExtractor<Integer>() {
						public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
							if (arg0.next()) {								
								return arg0.getInt("codigo");
							}
							return null;
						}
					}));

			inclusaoHistoricoAlunoDisciplinaVO.setNovoObj(Boolean.FALSE);
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO, UsuarioVO usuarioVO) throws Exception{
		InclusaoHistoricoAluno.excluir("InclusaoHistoricoAluno", true, usuarioVO);
		getFacadeFactory().getHistoricoFacade().excluir(inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO(), false, usuarioVO);	
		inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().remove(inclusaoHistoricoAlunoDisciplinaVO);
		if(inclusaoHistoricoAlunoVO.getInclusaoHistoricoAlunoDisciplinaVOs().isEmpty()){
			getFacadeFactory().getInclusaoHistoricoAlunoFacade().excluir(inclusaoHistoricoAlunoVO, false, usuarioVO);
		}
	}
	
	private String getSqlConsultaCompleta(){
		StringBuilder sql  = new StringBuilder("select inclusaoHistoricoAlunoDisciplina.*, ");
		sql.append(" historico.codigo as \"historico.codigo\", historico.situacao as \"historico.situacao\", historico.mediafinal as \"historico.mediafinal\", ");
		sql.append(" historico.notafinalconceito as \"historico.notaFinalConceito\", ");
		sql.append(" historico.mediaFinalConceito as \"historico.mediaFinalConceito\", ");
		sql.append(" historico.utilizaNotaFinalConceito as \"historico.utilizaNotaFinalConceito\", ");
		sql.append(" historico.configuracaoAcademico as \"historico.configuracaoAcademico\", ");
		sql.append(" historico.freguencia as \"historico.freguencia\", ");
		sql.append(" historico.gradeDisciplina as \"historico.gradeDisciplina\", ");
		sql.append(" historico.gradeCurricularGrupoOptativaDisciplina as \"historico.gradeCurricularGrupoOptativaDisciplina\", ");
		sql.append(" historico.disciplina as \"historico.disciplina\", ");
		sql.append(" historico.cargahorariadisciplina as \"historico.cargahorariadisciplina\", ");
		sql.append(" historico.tipoHistorico as \"historico.tipoHistorico\", ");
		sql.append(" historico.periodoletivomatrizcurricular as \"historico.periodoletivomatrizcurricular\", ");
		sql.append(" historico.periodoletivocursada as \"historico.periodoletivocursada\", ");
		sql.append(" historico.matrizCurricular as \"historico.matrizCurricular\", ");
		sql.append(" historico.disciplinaReferenteAUmGrupoOptativa as \"historico.disciplinaReferenteAUmGrupoOptativa\", ");
		sql.append(" historico.historicoDisciplinaComposta as \"historico.historicoDisciplinaComposta\", ");
		sql.append(" historico.anoHistorico as \"historico.anoHistorico\", ");
		sql.append(" historico.semestreHistorico as \"historico.semestreHistorico\", ");
		sql.append(" historico.matricula as \"historico.matricula\", ");
		sql.append(" historico.matriculaperiodo as \"historico.matriculaperiodo\", ");
		sql.append(" periodoletivo.periodoletivo as \"periodoletivo.periodoletivo\", ");		
		sql.append(" disciplina.nome as \"disciplina.nome\", ");
		sql.append(" notaFinalConceito.conceitoNota as \"notaFinalConceito.conceitoNota\", ");
		sql.append(" notaFinalConceito.abreviaturaConceitoNota as \"notaFinalConceito.abreviaturaConceitoNota\" ");
		sql.append(" from inclusaoHistoricoAlunoDisciplina ");
		sql.append(" inner join historico on historico.codigo = inclusaoHistoricoAlunoDisciplina.historico ");
		sql.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = historico.periodoletivomatrizcurricular ");
		sql.append(" inner join inclusaoHistoricoAluno on inclusaoHistoricoAluno.codigo = inclusaoHistoricoAlunoDisciplina.inclusaoHistoricoAluno ");		
		sql.append(" left join configuracaoAcademicoNotaConceito notaFinalConceito on notaFinalConceito.codigo = historico.mediaFinalConceito ");
		
		return sql.toString();
	}

	@Override
	public List<InclusaoHistoricoAlunoDisciplinaVO> consultarPorInclusaoHistoricoAluno(Integer inclusaoHistoricoAluno,
			UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" where inclusaoHistoricoAlunoDisciplina.inclusaoHistoricoAluno =  ").append(inclusaoHistoricoAluno);
		sql.append(" order by periodoletivo.periodoletivo, disciplina.nome, anohistorico, semestrehistorico ");		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), usuarioVO);
	}
	
	private List<InclusaoHistoricoAlunoDisciplinaVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuario) throws Exception{
		List<InclusaoHistoricoAlunoDisciplinaVO> inclusaoHistoricoAlunoDisciplinaVOs = new ArrayList<InclusaoHistoricoAlunoDisciplinaVO>(0);
		Map<Integer, ConfiguracaoAcademicoVO> mapConfAcad = new HashMap<Integer, ConfiguracaoAcademicoVO>(0); 
		while(rs.next()){
			if(!mapConfAcad.containsKey(rs.getInt("historico.configuracaoAcademico"))){
				mapConfAcad.put(rs.getInt("historico.configuracaoAcademico"), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(rs.getInt("historico.configuracaoAcademico"), usuario));
			}
			inclusaoHistoricoAlunoDisciplinaVOs.add(montarDados(rs, mapConfAcad.get(rs.getInt("historico.configuracaoAcademico"))));
		}
		return inclusaoHistoricoAlunoDisciplinaVOs;
	}
	
	private InclusaoHistoricoAlunoDisciplinaVO montarDados(SqlRowSet rs, ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception{
		InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO  = new InclusaoHistoricoAlunoDisciplinaVO();
		inclusaoHistoricoAlunoDisciplinaVO.setNovoObj(false);
		inclusaoHistoricoAlunoDisciplinaVO.setCodigo(rs.getInt("codigo"));
		inclusaoHistoricoAlunoDisciplinaVO.getInclusaoHistoricoAlunoVO().setCodigo(rs.getInt("inclusaoHistoricoAluno"));
		inclusaoHistoricoAlunoDisciplinaVO.getInclusaoHistoricoAlunoVO().getMatriculaVO().setMatricula(rs.getString("historico.matricula"));
		inclusaoHistoricoAlunoDisciplinaVO.getMatriculaAproveitarDisciplinaVO().setMatricula(rs.getString("matriculaAproveitarDisciplina"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setCodigo(rs.getInt("historico.codigo"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setSituacao(rs.getString("historico.situacao"));
		if(rs.getObject("historico.mediafinal") != null){
			inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setMediaFinal(rs.getDouble("historico.mediafinal"));
		}
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setNotaFinalConceito(rs.getString("historico.notaFinalConceito"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getMediaFinalConceito().setCodigo(rs.getInt("historico.mediaFinalConceito"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getMediaFinalConceito().setConceitoNota(rs.getString("notaFinalConceito.conceitoNota"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getMediaFinalConceito().setAbreviaturaConceitoNota(rs.getString("notaFinalConceito.abreviaturaConceitoNota"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setUtilizaNotaFinalConceito(rs.getBoolean("historico.utilizaNotaFinalConceito"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getConfiguracaoAcademico().setCodigo(rs.getInt("historico.configuracaoAcademico"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setConfiguracaoAcademico(configuracaoAcademicoVO);
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setFreguencia(rs.getDouble("historico.freguencia"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getGradeDisciplinaVO().setCodigo(rs.getInt("historico.gradeDisciplina"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(rs.getInt("historico.gradeCurricularGrupoOptativaDisciplina"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getDisciplina().setCodigo(rs.getInt("historico.disciplina"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getDisciplina().setNome(rs.getString("disciplina.nome"));
		if(Uteis.isAtributoPreenchido(inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getGradeDisciplinaVO().getCodigo())){
			inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getGradeDisciplinaVO().getDisciplina().setCodigo(rs.getInt("historico.disciplina"));
			inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getGradeDisciplinaVO().getDisciplina().setNome(rs.getString("disciplina.nome"));
		}else{
			inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().setCodigo(rs.getInt("historico.disciplina"));
			inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().setNome(rs.getString("disciplina.nome"));
		}
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setCargaHorariaDisciplina(rs.getInt("historico.cargahorariadisciplina"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setTipoHistorico(rs.getString("historico.tipoHistorico"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getPeriodoLetivoMatrizCurricular().setCodigo(rs.getInt("historico.periodoletivomatrizcurricular"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getPeriodoLetivoMatrizCurricular().setPeriodoLetivo(rs.getInt("periodoletivo.periodoletivo"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getPeriodoLetivoCursada().setCodigo(rs.getInt("historico.periodoletivocursada"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getMatrizCurricular().setCodigo(rs.getInt("historico.matrizCurricular"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setDisciplinaReferenteAUmGrupoOptativa(rs.getBoolean("historico.disciplinaReferenteAUmGrupoOptativa"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setHistoricoDisciplinaComposta(rs.getBoolean("historico.historicoDisciplinaComposta"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setAnoHistorico(rs.getString("historico.anoHistorico"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().setSemestreHistorico(rs.getString("historico.semestreHistorico"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getMatricula().setMatricula(rs.getString("historico.matricula"));
		inclusaoHistoricoAlunoDisciplinaVO.getHistoricoVO().getMatriculaPeriodo().setCodigo(rs.getInt("historico.matriculaperiodo"));				
		
		return inclusaoHistoricoAlunoDisciplinaVO;
	}

	@Override
	public void validarDados(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, InclusaoHistoricoAlunoDisciplinaVO obj) throws ConsistirException {
		HistoricoVO.validarDados(obj.getHistoricoVO());
		if(!inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getPeriodicidade().equals("IN")
				&& (!Uteis.isAtributoPreenchido(obj.getHistoricoVO().getAnoHistorico().trim()) || obj.getHistoricoVO().getAnoHistorico().length() != 4)){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_InclusaoHistoricoAlunoDisciplina_ano"));
		}
		if(inclusaoHistoricoAlunoVO.getMatriculaVO().getCurso().getPeriodicidade().equals("SE")
				&& (!Uteis.isAtributoPreenchido(obj.getHistoricoVO().getSemestreHistorico().trim()) 
				|| (!obj.getHistoricoVO().getSemestreHistorico().equals("1") && !obj.getHistoricoVO().getSemestreHistorico().equals("2")))){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_InclusaoHistoricoAlunoDisciplina_semestre"));
		}

	}

}
