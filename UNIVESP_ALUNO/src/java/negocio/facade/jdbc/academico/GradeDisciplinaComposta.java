package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.GradeDisciplinaCompostaInterfaceFacade;

@Repository
@Lazy
@Scope
public class GradeDisciplinaComposta extends ControleAcesso implements GradeDisciplinaCompostaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6858596353232975192L;

	@Override
	public void persistir(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, String situacaoGradeCurricular, FormulaCalculoNotaEnum formulaCalculoNota, UsuarioVO usuario) throws Exception {
		if (gradeDisciplinaCompostaVO.isNovoObj()) {
			incluir(gradeDisciplinaCompostaVO, situacaoGradeCurricular, formulaCalculoNota, usuario);
		} else {
			alterar(gradeDisciplinaCompostaVO, situacaoGradeCurricular, formulaCalculoNota, usuario);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GradeDisciplinaCompostaVO obj, String situacaoGradeCurricular, FormulaCalculoNotaEnum formulaCalculoNota, UsuarioVO usuario) throws Exception {
		try{
		validarDados(obj, situacaoGradeCurricular, formulaCalculoNota);
		final StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO GradeDisciplinaComposta( periodoLetivo, disciplina, modalidadeDisciplina,  cargahoraria, cargahorariapratica, cargahorariateorica, nrcreditos, ");
		sql.append(" tipodisciplina, configuracaoacademico, diversificada, nrcreditofinanceiro, gradeDisciplina, gradeCurricularGrupoOptativaDisciplina, grupoOptativa, ordem, variavelnota, horaaula) ");
		sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				int x = 1;
				if (!obj.getGrupoOptativa()) {
					sqlInserir.setInt(x++, obj.getPeriodoLetivo().intValue());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				sqlInserir.setInt(x++, obj.getDisciplina().getCodigo().intValue());
				sqlInserir.setString(x++, obj.getModalidadeDisciplina().name());
				sqlInserir.setInt(x++, obj.getCargaHoraria());
				sqlInserir.setInt(x++, obj.getCargaHorariaPratica());
				sqlInserir.setInt(x++, obj.getCargaHorariaTeorica());
				sqlInserir.setInt(x++, obj.getNrCreditos());
				sqlInserir.setString(x++, obj.getTipoDisciplina());
				if (obj.getConfiguracaoAcademico() != null && obj.getConfiguracaoAcademico().getCodigo() > 0) {
					sqlInserir.setInt(x++, obj.getConfiguracaoAcademico().getCodigo().intValue());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				sqlInserir.setBoolean(x++, obj.getDiversificada());
				sqlInserir.setDouble(x++, obj.getNrCreditoFinanceiro());
				if (!obj.getGrupoOptativa()) {
					sqlInserir.setInt(x++, obj.getGradeDisciplina().getCodigo().intValue());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				if (obj.getGrupoOptativa()) {
					sqlInserir.setInt(x++, obj.getGradeCurricularGrupoOptativaDisciplina().getCodigo().intValue());
				} else {
					sqlInserir.setNull(x++, 0);
				}
				sqlInserir.setBoolean(x++, obj.getGrupoOptativa());
				sqlInserir.setInt(x++, obj.getOrdem());
				sqlInserir.setString(x++, obj.getVariavelNota());
				sqlInserir.setInt(x++, obj.getHoraAula());
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
		getFacadeFactory().getDisciplinaPreRequisitoFacade().incluirDisciplinaCompostaPreRequisitos(obj.getCodigo(), obj.getDisciplinaRequisitoVOs(), usuario);
		obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setCodigo(0);
            throw e;
        }		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GradeDisciplinaCompostaVO obj, String situacaoGradeCurricular, FormulaCalculoNotaEnum formulaCalculoNota, UsuarioVO usuario) throws Exception {
		validarDados(obj, situacaoGradeCurricular, formulaCalculoNota);
		final StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE GradeDisciplinaComposta set periodoLetivo = ?, disciplina = ?, modalidadeDisciplina = ?,  ");
		sql.append(" cargahoraria=?, cargahorariapratica=?, cargahorariateorica=?, nrcreditos=?, tipodisciplina=?, configuracaoacademico=?, ");
		sql.append(" diversificada=?, nrcreditofinanceiro = ?, gradeDisciplina = ?, gradeCurricularGrupoOptativaDisciplina = ?, grupoOptativa = ?, ordem = ?, variavelnota = ?, horaaula = ? ");
		sql.append(" WHERE (codigo = ?) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int x = 1;
				if (!obj.getGrupoOptativa()) {
					sqlAlterar.setInt(x++, obj.getPeriodoLetivo().intValue());
				} else {
					sqlAlterar.setNull(x++, 0);
				}
				sqlAlterar.setInt(x++, obj.getDisciplina().getCodigo().intValue());
				sqlAlterar.setString(x++, obj.getModalidadeDisciplina().name());
				sqlAlterar.setInt(x++, obj.getCargaHoraria());
				sqlAlterar.setInt(x++, obj.getCargaHorariaPratica());
				sqlAlterar.setInt(x++, obj.getCargaHorariaTeorica());
				sqlAlterar.setInt(x++, obj.getNrCreditos());
				sqlAlterar.setString(x++, obj.getTipoDisciplina());
				if (obj.getConfiguracaoAcademico() != null && obj.getConfiguracaoAcademico().getCodigo() > 0) {
					sqlAlterar.setInt(x++, obj.getConfiguracaoAcademico().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(x++, 0);
				}
				sqlAlterar.setBoolean(x++, obj.getDiversificada());
				sqlAlterar.setDouble(x++, obj.getNrCreditoFinanceiro());
				if (!obj.getGrupoOptativa()) {
					sqlAlterar.setInt(x++, obj.getGradeDisciplina().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(x++, 0);
				}
				if (obj.getGrupoOptativa()) {
					sqlAlterar.setInt(x++, obj.getGradeCurricularGrupoOptativaDisciplina().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(x++, 0);
				}
				sqlAlterar.setBoolean(x++, obj.getGrupoOptativa());
				sqlAlterar.setInt(x++, obj.getOrdem());
				sqlAlterar.setString(x++, obj.getVariavelNota());
				sqlAlterar.setInt(x++, obj.getHoraAula());
				sqlAlterar.setInt(x++, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) == 0) {
			incluir(obj, situacaoGradeCurricular, formulaCalculoNota, usuario);
			return;
		}
		;
		getFacadeFactory().getDisciplinaPreRequisitoFacade().alterarDisciplinaCompostaPreRequisitos(obj.getCodigo(), obj.getDisciplinaRequisitoVOs(), usuario);
	}

	@Override
	public void validarDados(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, String situacaoGradeCurricular, FormulaCalculoNotaEnum formulaCalculoNota) throws ConsistirException {
		if ((gradeDisciplinaCompostaVO.getDisciplina() == null) || (gradeDisciplinaCompostaVO.getDisciplina().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo DISCIPLINA deve ser informado.");
		}
		if (gradeDisciplinaCompostaVO.getCargaHoraria().intValue() == 0 && situacaoGradeCurricular.equals("CO")) {
			throw new ConsistirException("O campo CARGA HORÁRIA deve ser informado.");
		}
		if (gradeDisciplinaCompostaVO.getCargaHorariaPratica() > gradeDisciplinaCompostaVO.getCargaHoraria()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_cargaHorariaPraticaMaiorCargaHoraria"));
		}
		gradeDisciplinaCompostaVO.setVariavelNota(gradeDisciplinaCompostaVO.getVariavelNota().trim());
		if(formulaCalculoNota != null && formulaCalculoNota.equals(FormulaCalculoNotaEnum.FORMULA_CALCULO) && gradeDisciplinaCompostaVO.getVariavelNota().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_variavelNota"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirGradeDisciplinaCompostaVOsPorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			gradeDisciplinaCompostaVO.setGradeDisciplina(gradeDisciplinaVO);
			gradeDisciplinaCompostaVO.setPeriodoLetivo(gradeDisciplinaVO.getPeriodoLetivo());
			gradeDisciplinaCompostaVO.setGrupoOptativa(false);
			gradeDisciplinaCompostaVO.setDiversificada(gradeDisciplinaVO.getDiversificada());
			gradeDisciplinaCompostaVO.setTipoDisciplina(gradeDisciplinaVO.getTipoDisciplina());
			incluir(gradeDisciplinaCompostaVO, situacaoGradeCurricular, gradeDisciplinaVO.getFormulaCalculoNota(), usuario);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarGradeDisciplinaCompostaVOsPorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		excluirGradeDisciplinaCompostaVOsPorGradeDisciplina(gradeDisciplinaVO, usuario);
		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			gradeDisciplinaCompostaVO.setGradeDisciplina(gradeDisciplinaVO);
			gradeDisciplinaCompostaVO.setPeriodoLetivo(gradeDisciplinaVO.getPeriodoLetivo());
			gradeDisciplinaCompostaVO.setGrupoOptativa(false);
			gradeDisciplinaCompostaVO.setDiversificada(gradeDisciplinaVO.getDiversificada());
			gradeDisciplinaCompostaVO.setTipoDisciplina(gradeDisciplinaVO.getTipoDisciplina());
			persistir(gradeDisciplinaCompostaVO, situacaoGradeCurricular, gradeDisciplinaVO.getFormulaCalculoNota(), usuario);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGradeDisciplinaCompostaVOsPorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sql = new StringBuilder("delete from GradeDisciplinaComposta where grupoOptativa = false and gradeDisciplina = " + gradeDisciplinaVO.getCodigo());
			sql.append(" and codigo not in (0");
			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
				sql.append(", ").append(gradeDisciplinaCompostaVO.getCodigo());
			}
			sql.append(" )");
			getConexao().getJdbcTemplate().update(sql.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<GradeDisciplinaCompostaVO> consultarPorGradeDisciplina(Integer gradeDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" gradeDisciplina.disciplina as \"gradeDisciplina.disciplina\", ");
		sql.append(" null as \"gradeCurricularGrupoOptativaDisciplina.disciplina\" ");
		sql.append(" FROM GradeDisciplinaComposta ");
		sql.append(" inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append(" inner join gradeDisciplina on gradeDisciplina.codigo =  GradeDisciplinaComposta.gradeDisciplina ");		
		sql.append(" where grupoOptativa = false and gradeDisciplina = ").append(gradeDisciplina).append(" order by GradeDisciplinaComposta.ordem ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}
	
	public Boolean consultarPorCodigoDisciplinaECargaHorariaDisciplinaFazParteComposicao(Integer codigoDisciplina, Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT GradeDisciplinaComposta.* ");
		sql.append("FROM GradeDisciplinaComposta "); 
		sql.append("inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append("inner join periodoLetivo on GradeDisciplinaComposta.periodoLetivo = periodoLetivo.codigo ");
		sql.append("where (gradeDisciplinaComposta.disciplina =").append(codigoDisciplina).append(") and ");
		sql.append("(periodoLetivo.gradeCurricular =  ").append(gradeCurricular).append(")");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			return true;
		}
		return false;
	}
	

	private List<GradeDisciplinaCompostaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
		while (rs.next()) {
			gradeDisciplinaCompostaVOs.add(montarDados(rs, nivelMontarDados, usuario));
		}
		return gradeDisciplinaCompostaVOs;
	}

	private GradeDisciplinaCompostaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		GradeDisciplinaCompostaVO obj = new GradeDisciplinaCompostaVO();
		obj.setPeriodoLetivo(new Integer(dadosSQL.getInt("periodoLetivo")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
		obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("modalidadeDisciplina")));
		obj.setVariavelNota(dadosSQL.getString("variavelNota"));
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargahoraria")));
		obj.setCargaHorariaPratica(new Integer(dadosSQL.getInt("cargahorariapratica")));
		obj.setCargaHorariaTeorica(new Integer(dadosSQL.getInt("cargahorariateorica")));
		obj.setCargaHorariaTeorica(obj.getCargaHoraria() - obj.getCargaHorariaPratica());
		obj.setNrCreditos(new Integer(dadosSQL.getInt("nrcreditos")));
		obj.setNrCreditoFinanceiro(dadosSQL.getDouble("nrcreditofinanceiro"));
		obj.setTipoDisciplina(dadosSQL.getString("tipodisciplina"));
		obj.getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("configuracaoacademico"));
		obj.setDiversificada(dadosSQL.getBoolean("diversificada"));
		obj.setGrupoOptativa(dadosSQL.getBoolean("grupoOptativa"));
		obj.setOrdem(dadosSQL.getInt("ordem"));
		obj.setHoraAula(dadosSQL.getInt("horaaula"));
		obj.getGradeCurricularGrupoOptativaDisciplina().setCodigo(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina"));
		// setando os dados da disciplina mãe, que determina a composicao
		obj.getGradeCurricularGrupoOptativaDisciplina().getDisciplina().setCodigo(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina.disciplina"));

		obj.getGradeDisciplina().setCodigo(dadosSQL.getInt("gradeDisciplina"));
		// setando os dados da disciplina mãe, que determina a composicao
		obj.getGradeDisciplina().getDisciplina().setCodigo(dadosSQL.getInt("gradeDisciplina.disciplina"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setDisciplinaRequisitoVOs(getFacadeFactory().getDisciplinaPreRequisitoFacade().consultarDisciplinaCompostaPreRequisitos(obj.getCodigo(), false, usuario));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		// montarDadosDisciplina(obj, usuario);
		if (obj.getConfiguracaoAcademico() != null && obj.getConfiguracaoAcademico().getCodigo() > 0) {
			obj.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getConfiguracaoAcademico().getCodigo(), usuario));
		}

		return obj;
	}

	public void montarDadosDisciplina(GradeDisciplinaCompostaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	@Override
	public GradeDisciplinaCompostaVO consultarPorChavePrimaria(Integer gradeDisciplinaComposta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\",  ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.disciplina as \"gradeCurricularGrupoOptativaDisciplina.disciplina\", ");
		sql.append(" gradeDisciplina.disciplina as \"gradeDisciplina.disciplina\" ");
		sql.append(" FROM GradeDisciplinaComposta ");
		sql.append(" inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append(" left join gradeDisciplina on gradeDisciplina.codigo =  GradeDisciplinaComposta.gradeDisciplina ");
		sql.append(" left join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo =  GradeDisciplinaComposta.gradeCurricularGrupoOptativaDisciplina ");
		sql.append(" where GradeDisciplinaComposta.codigo = ").append(gradeDisciplinaComposta).append(" order by GradeDisciplinaComposta.ordem ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuario);
		}
		throw new Exception("Dados não encontrados (Grade Disciplina Composta)");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			gradeDisciplinaCompostaVO.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO);
			gradeDisciplinaCompostaVO.setGrupoOptativa(true);
			gradeDisciplinaCompostaVO.setDiversificada(gradeCurricularGrupoOptativaDisciplinaVO.getDiversificada());
			gradeDisciplinaCompostaVO.setTipoDisciplina("OP");
			incluir(gradeDisciplinaCompostaVO, situacaoGradeCurricular, gradeCurricularGrupoOptativaDisciplinaVO.getFormulaCalculoNota(), usuario);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception {
		excluirGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO, usuario);
		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			gradeDisciplinaCompostaVO.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaVO);
			gradeDisciplinaCompostaVO.setGrupoOptativa(true);
			gradeDisciplinaCompostaVO.setDiversificada(gradeCurricularGrupoOptativaDisciplinaVO.getDiversificada());
			gradeDisciplinaCompostaVO.setTipoDisciplina("OP");
			persistir(gradeDisciplinaCompostaVO, situacaoGradeCurricular, gradeCurricularGrupoOptativaDisciplinaVO.getFormulaCalculoNota(), usuario);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("delete from GradeDisciplinaComposta where grupoOptativa = true and gradeCurricularGrupoOptativaDisciplina = " + gradeCurricularGrupoOptativaDisciplinaVO.getCodigo());
		sql.append(" and codigo not in (0");
		for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
			sql.append(", ").append(gradeDisciplinaCompostaVO.getCodigo());
		}
		sql.append(" )");
		getConexao().getJdbcTemplate().update(sql.toString() + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

	}

	@Override
	public List<GradeDisciplinaCompostaVO> consultarPorGrupoOptativaDisciplina(Integer grupoOptativaDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.disciplina as \"gradeCurricularGrupoOptativaDisciplina.disciplina\", ");
		sql.append(" gradeDisciplina.disciplina as \"gradeDisciplina.disciplina\" ");
		sql.append(" FROM GradeDisciplinaComposta ");
		sql.append(" inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo =  GradeDisciplinaComposta.gradeCurricularGrupoOptativaDisciplina ");
		sql.append(" left join gradeDisciplina on gradeDisciplina.codigo =  GradeDisciplinaComposta.gradeDisciplina ");
		sql.append(" where grupoOptativa = true and gradeCurricularGrupoOptativaDisciplina = ").append(grupoOptativaDisciplina).append(" order by GradeDisciplinaComposta.ordem ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}

	@Override
	public List<GradeDisciplinaCompostaVO> consultarPorTurmaDisciplina(Integer turma, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" null as \"gradeCurricularGrupoOptativaDisciplina.disciplina\", ");
		sql.append(" gradeDisciplina.disciplina as \"gradeDisciplina.disciplina\" ");
		sql.append(" FROM GradeDisciplinaComposta ");
		sql.append(" inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");		
		sql.append(" inner join turmadisciplina on turmadisciplina.gradedisciplina = gradedisciplinacomposta.gradedisciplina ");
		sql.append(" inner join gradeDisciplina on gradeDisciplina.codigo =  GradeDisciplinaComposta.gradeDisciplina ");
		sql.append(" where gradeDisciplina.disciplina = ").append(disciplina).append(" and turma = ").append(turma).append(" ");
		sql.append(" union all ");
		sql.append(" SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.disciplina as \"gradeCurricularGrupoOptativaDisciplina.disciplina\", ");
		sql.append(" null as \"gradeDisciplina.disciplina\" ");
		sql.append(" FROM GradeDisciplinaComposta ");
		sql.append(" inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append(" inner join turmadisciplina on turmadisciplina.gradeCurricularGrupoOptativaDisciplina = gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina ");
		sql.append(" inner join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo =  GradeDisciplinaComposta.gradeCurricularGrupoOptativaDisciplina ");
		sql.append(" where gradeCurricularGrupoOptativaDisciplina.disciplina = ").append(disciplina).append(" and turma = ").append(turma).append(" ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}
	
	@Override
	public Boolean  realizaVerificacaoDisciplinaEComposta(Integer turma, Integer disciplina) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT GradeDisciplinaComposta.codigo ");
		sql.append(" FROM GradeDisciplinaComposta ");
		sql.append(" inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");		
		sql.append(" inner join turmadisciplina on turmadisciplina.gradedisciplina = gradedisciplinacomposta.gradedisciplina ");		
		sql.append(" where gradeDisciplina.disciplina = ").append(disciplina).append(" and turma = ").append(turma).append(" ");
		sql.append(" union all ");
		sql.append(" SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.disciplina as \"gradeCurricularGrupoOptativaDisciplina.disciplina\", ");
		sql.append(" null as \"gradeDisciplina.disciplina\" ");
		sql.append(" FROM GradeDisciplinaComposta ");
		sql.append(" inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append(" inner join turmadisciplina on turmadisciplina.gradeCurricularGrupoOptativaDisciplina = gradedisciplinacomposta.gradeCurricularGrupoOptativaDisciplina ");		
		sql.append(" where gradeCurricularGrupoOptativaDisciplina.disciplina = ").append(disciplina).append(" and turma = ").append(turma).append(" limit 1");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return rs.next();
	}


	@Override
	public void executarCalcularCargaHorariaTeoricaDisciplinaComposta(Integer cargaHorariaDisciplinaPrincipal, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) throws Exception {
		Integer cargaHorariaTotal = 0;
		for (GradeDisciplinaCompostaVO obj : gradeDisciplinaCompostaVOs) {
			cargaHorariaTotal += obj.getCargaHoraria();
		}
		cargaHorariaTotal += gradeDisciplinaCompostaVO.getCargaHoraria();
		if (cargaHorariaDisciplinaPrincipal < cargaHorariaTotal) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_cargaHorariaMaiorCargaHorariaDisciplinaPrincipal"));
		}
		if (gradeDisciplinaCompostaVO.getCargaHorariaPratica() > gradeDisciplinaCompostaVO.getCargaHoraria()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_cargaHorariaPraticaMaiorCargaHoraria"));
		}
		gradeDisciplinaCompostaVO.setCargaHorariaTeorica(gradeDisciplinaCompostaVO.getCargaHoraria() - gradeDisciplinaCompostaVO.getCargaHorariaPratica());

	}

	@Override
	public void alterarOrdemGradeDisciplinaComposta(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, boolean subir) throws Exception {
		if ((subir && gradeDisciplinaCompostaVO.getOrdem() == 0) || (!subir && gradeDisciplinaCompostaVO.getOrdem() == gradeDisciplinaCompostaVOs.size())) {
			return;
		}
		int ordem1 = gradeDisciplinaCompostaVO.getOrdem();
		int ordem2 = gradeDisciplinaCompostaVO.getOrdem();
		if (subir) {
			ordem2 -= 1;
		} else {
			ordem2 += 1;
		}

		gradeDisciplinaCompostaVOs.get(ordem1).setOrdem(ordem2);
		gradeDisciplinaCompostaVOs.get(ordem2).setOrdem(ordem1);
		Ordenacao.ordenarLista(gradeDisciplinaCompostaVOs, "ordem");
	}
	
	@Override
	public void adicionarDisciplinaPreRequisitoVOs(List<DisciplinaPreRequisitoVO> disciplinaPreRequisitoVOs, DisciplinaPreRequisitoVO obj) throws Exception {
		DisciplinaPreRequisitoVO.validarDados(obj);
		int index = 0;
		Iterator<DisciplinaPreRequisitoVO> i = disciplinaPreRequisitoVOs.iterator();
		while (i.hasNext()) {
			DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				disciplinaPreRequisitoVOs.set(index, obj);
				return;
			}
			index++;
		}
		disciplinaPreRequisitoVOs.add(obj);
	}
	
	@Override
	public void removerDisciplinaPreRequisitoVOs(List<DisciplinaPreRequisitoVO> disciplinaPreRequisitoVOs, DisciplinaPreRequisitoVO obj, UsuarioVO usuario) throws Exception {
		Iterator<DisciplinaPreRequisitoVO> i = disciplinaPreRequisitoVOs.iterator();
		while (i.hasNext()) {
			DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
//				if (!objExistente.getCodigo().equals(0)) {
//					getFacadeFactory().getDisciplinaPreRequisitoFacade().excluir(objExistente, usuario);
//				}
				i.remove();
				return;
			}
		}
	}
	
	public List<GradeDisciplinaCompostaVO> consultarPorGradeCurricular(Integer gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		// GRADEDISCIPLINACOMPOSTA DE GRADES DISCIPLINAS
		sql.append(" (SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" null as \"gradeCurricularGrupoOptativaDisciplina.disciplina\" ");
		sql.append("  FROM GradeDisciplinaComposta ");
		sql.append("  left join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append("  inner join periodoletivo on periodoletivo.codigo = gradedisciplinacomposta.periodoletivo");
		sql.append("  inner join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular");
		sql.append("  where gradecurricular.codigo = ").append(gradeCurricular).append(") ");
		sql.append("  UNION ");
		// GRADEDISCIPLINACOMPOSTA DE GRUPO DE OPTATIVAS
		sql.append(" (SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" null as \"gradeDisciplina.disciplina\" ");
		sql.append("  FROM GradeDisciplinaComposta ");
		sql.append("  left join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append("  inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina");
		sql.append("  inner join gradecurriculargrupooptativa on gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa");
		sql.append("  inner join gradecurricular on gradecurricular.codigo = gradecurriculargrupooptativa.gradecurricular");
		sql.append("  where gradecurricular.codigo = ").append(gradeCurricular).append(") ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}	

	@Override
	public Boolean consultarGradeControlaRecuperacaoPorGradeDisciplinaComposta(Integer gradeDisciplinaComposta) throws Exception{
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select controlarRecuperacaoPelaDisciplinaPrincipal from gradedisciplina ");
		sql.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradedisciplina = gradedisciplina.codigo ");
		sql.append(" where gradeDisciplinaComposta.codigo = ").append(gradeDisciplinaComposta);
		sql.append(" union ");
		sql.append(" select controlarRecuperacaoPelaDisciplinaPrincipal from gradecurriculargrupooptativadisciplina ");
		sql.append(" inner join gradeDisciplinaComposta on gradeDisciplinaComposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
		sql.append(" where gradeDisciplinaComposta.codigo = ").append(gradeDisciplinaComposta);
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return rs.getBoolean("controlarRecuperacaoPelaDisciplinaPrincipal");
		}
		return false;
	}
	
	
	@Override
	public GradeDisciplinaCompostaVO consultarPorCodigoDisciplinaEMatriz(Integer codigoDisciplina, Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT GradeDisciplinaComposta.*, disciplina.nome as \"disciplina.nome\", ");
		sql.append(" gradeDisciplina.disciplina as \"gradeDisciplina.disciplina\", ");
		sql.append(" null as \"gradeCurricularGrupoOptativaDisciplina.disciplina\" ");
		sql.append(" FROM GradeDisciplinaComposta ");
		sql.append(" inner join disciplina on disciplina.codigo =  GradeDisciplinaComposta.disciplina ");
		sql.append(" left join gradeDisciplina on gradeDisciplina.codigo =  GradeDisciplinaComposta.gradeDisciplina ");
		sql.append(" left join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo =  GradeDisciplinaComposta.gradeCurricularGrupoOptativaDisciplina ");		
		sql.append(" left join periodoLetivo on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
		sql.append(" left join gradeCurricularGrupoOptativa on gradeCurricularGrupoOptativaDisciplina.gradeCurricularGrupoOptativa = gradeCurricularGrupoOptativa.codigo ");
		sql.append("where (gradeDisciplinaComposta.disciplina =").append(codigoDisciplina).append(") and ");
		sql.append("((periodoLetivo.gradeCurricular =  ").append(gradeCurricular).append(") or (").append(" gradeCurricularGrupoOptativa.gradeCurricular = ").append(gradeCurricular).append(")) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		return null;
	}
}
