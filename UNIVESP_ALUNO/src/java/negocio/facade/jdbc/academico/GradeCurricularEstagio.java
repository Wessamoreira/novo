package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.enumeradores.GradeCurricularEstagioAreaEnum;
import negocio.comuns.academico.enumeradores.GradeCurricularEstagioQuestionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.estagio.GrupoPessoaItem;
import negocio.interfaces.academico.GradeCurricularEstagioInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class GradeCurricularEstagio extends ControleAcesso implements GradeCurricularEstagioInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3309151762870268547L;
	
	@Override
	public void validarDados(GradeCurricularEstagioVO gre) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(gre.getNome()), "O campo Componentes de Estágio deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(gre.getCargaHorarioObrigatorio()), "O campo Horas Estágio deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(gre.getTextoPadraoDeclaracaoVO()), "O campo Modelo Termo de Estágio deve ser informado.");
//		Uteis.checkState(!Uteis.isAtributoPreenchido(gre.getQuestionarioRelatorioFinal()), "O campo Formulário Relatório Final deve ser informado.");
		Uteis.checkState(Uteis.isAtributoPreenchido(gre.getGradeCurricularEstagioQuestionarioEnum()) && !Uteis.isAtributoPreenchido(gre.getHoraMaximaAproveitamentoOuEquivalencia()), "O campo Horas Máximas de Aproveitamento/Equivalência deve ser informado.");
		Uteis.checkState(Uteis.isAtributoPreenchido(gre.getHoraMaximaAproveitamentoOuEquivalencia()) && gre.getHoraMaximaAproveitamentoOuEquivalencia() > gre.getCargaHorarioObrigatorio(), "O campo Horas Máximas de Aproveitamento/Equivalência não pode ser maior que as Horas Estágio.");
//		Uteis.checkState(Uteis.isAtributoPreenchido(gre.getGradeCurricularEstagioQuestionarioEnum()) && gre.getGradeCurricularEstagioQuestionarioEnum().isAproveitamento() && !Uteis.isAtributoPreenchido(gre.getQuestionarioAproveitamentoPorDocenteRegular()), "O campo Formulário de Aproveitamento de Estágio por Docente Regular Educação Básica deve ser informado para o Componentes de Estágio -"+gre.getNome()+".");
//		Uteis.checkState(Uteis.isAtributoPreenchido(gre.getGradeCurricularEstagioQuestionarioEnum()) && gre.getGradeCurricularEstagioQuestionarioEnum().isAproveitamento() && !Uteis.isAtributoPreenchido(gre.getQuestionarioAproveitamentoPorLicenciatura()), "O campo Formulário de Aproveitamento de Estágio por Licenciatura em Outro Curso deve ser informado para o Componentes de Estágio -"+gre.getNome()+".");
//		Uteis.checkState(Uteis.isAtributoPreenchido(gre.getGradeCurricularEstagioQuestionarioEnum()) && gre.getGradeCurricularEstagioQuestionarioEnum().isEquivalencia() && !Uteis.isAtributoPreenchido(gre.getQuestionarioEquivalencia()), "O campo Formulário Equivalência deve ser informado para o Componentes de Estágio -"+gre.getNome()+".");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<GradeCurricularEstagioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (GradeCurricularEstagioVO obj : lista) {
				validarDados(obj);
				if (obj.getCodigo() == 0) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);			
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GradeCurricularEstagioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "gradeCurricularEstagio", new AtributoPersistencia()
					.add("gradeCurricular", obj.getGradeCurricularVO())
					.add("nome", obj.getNome())
					.add("gradeCurricularEstagioAreaEnum", obj.getGradeCurricularEstagioAreaEnum())
					.add("gradeCurricularEstagioQuestionarioEnum", obj.getGradeCurricularEstagioQuestionarioEnum())					
					.add("cargaHorarioObrigatorio", obj.getCargaHorarioObrigatorio())
					.add("horaMaximaAproveitamentoOuEquivalencia", obj.getHoraMaximaAproveitamentoOuEquivalencia())
					.add("permiteHorasFragmentadas", obj.isPermiteHorasFragmentadas())
					.add("textoPadraoDeclaracao", obj.getTextoPadraoDeclaracaoVO())
//					.add("questionarioRelatoriofinal", obj.getQuestionarioRelatorioFinal())
//					.add("questionarioAproveitamentoPorDocenteRegular", obj.getQuestionarioAproveitamentoPorDocenteRegular())
//					.add("questionarioAproveitamentoPorLicenciatura", obj.getQuestionarioAproveitamentoPorLicenciatura())
//					.add("questionarioEquivalencia", obj.getQuestionarioEquivalencia())
					.add("docenteResponsavelEstagio", Uteis.isAtributoPreenchido(obj.getDocenteResponsavelEstagio()) ? obj.getDocenteResponsavelEstagio().getCodigo() : null)
					.add("informarSupervisorEstagio", obj.getInformarSupervisorEstagio())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GradeCurricularEstagioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "gradeCurricularEstagio", new AtributoPersistencia()
					.add("gradeCurricular", obj.getGradeCurricularVO())
					.add("nome", obj.getNome())
					.add("gradeCurricularEstagioAreaEnum", obj.getGradeCurricularEstagioAreaEnum())
					.add("gradeCurricularEstagioQuestionarioEnum", obj.getGradeCurricularEstagioQuestionarioEnum())					
					.add("cargaHorarioObrigatorio", obj.getCargaHorarioObrigatorio())
					.add("horaMaximaAproveitamentoOuEquivalencia", obj.getHoraMaximaAproveitamentoOuEquivalencia())
					.add("permiteHorasFragmentadas", obj.isPermiteHorasFragmentadas())
					.add("textoPadraoDeclaracao", obj.getTextoPadraoDeclaracaoVO())
//					.add("questionarioRelatoriofinal", obj.getQuestionarioRelatorioFinal())
//					.add("questionarioAproveitamentoPorDocenteRegular", obj.getQuestionarioAproveitamentoPorDocenteRegular())
//					.add("questionarioAproveitamentoPorLicenciatura", obj.getQuestionarioAproveitamentoPorLicenciatura())
//					.add("questionarioEquivalencia", obj.getQuestionarioEquivalencia())
					.add("docenteResponsavelEstagio", Uteis.isAtributoPreenchido(obj.getDocenteResponsavelEstagio()) ? obj.getDocenteResponsavelEstagio().getCodigo() : null)
					.add("informarSupervisorEstagio", obj.getInformarSupervisorEstagio())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsulta() {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" gradeCurricularEstagio.codigo as \"gradeCurricularEstagio.codigo\",  ");
		sql.append(" gradeCurricularEstagio.nome as \"gradeCurricularEstagio.nome\",  ");				
		sql.append(" gradeCurricularEstagio.cargaHorarioObrigatorio as \"gradeCurricularEstagio.cargaHorarioObrigatorio\", ");		
		sql.append(" gradeCurricularEstagio.horaMaximaAproveitamentoOuEquivalencia as \"gradeCurricularEstagio.horaMaximaAproveitamentoOuEquivalencia\", ");		
		sql.append(" gradeCurricularEstagio.gradeCurricularEstagioAreaEnum as \"gradeCurricularEstagio.gradeCurricularEstagioAreaEnum\", ");		
		sql.append(" gradeCurricularEstagio.gradeCurricularEstagioQuestionarioEnum as \"gradeCurricularEstagio.gradeCurricularEstagioQuestionarioEnum\", ");		
		sql.append(" gradeCurricularEstagio.permiteHorasFragmentadas as \"gradeCurricularEstagio.permiteHorasFragmentadas\", ");
		sql.append(" textoPadraoDeclaracao.codigo as \"textoPadraoDeclaracao.codigo\", textoPadraoDeclaracao.descricao as \"textoPadraoDeclaracao.descricao\", ");	
		sql.append(" gradeCurricularEstagio.informarSupervisorEstagio as \"gradeCurricularEstagio.informarSupervisorEstagio\",  ");
		sql.append(" qrf.codigo as \"qrf.codigo\", qrf.descricao as \"qrf.descricao\", ");		
		sql.append(" qadr.codigo as \"qadr.codigo\", qadr.descricao as \"qadr.descricao\", ");		
		sql.append(" qal.codigo as \"qal.codigo\", qal.descricao as \"qal.descricao\", ");		
		sql.append(" qe.codigo as \"qe.codigo\", qe.descricao as \"qe.descricao\", ");
		sql.append(" docenteresponsavelestagio.codigo as \"docenteresponsavelestagio.codigo\", docenteresponsavelestagio.nome as \"docenteresponsavelestagio.nome\" ");
		sql.append(" FROM gradeCurricularEstagio ");
		sql.append(" left join textoPadraoDeclaracao on textoPadraoDeclaracao.codigo = gradeCurricularEstagio.textoPadraoDeclaracao ");
		sql.append(" left join questionario as qrf on qrf.codigo = gradeCurricularEstagio.questionarioRelatoriofinal ");
		sql.append(" left join questionario as qadr on qadr.codigo = gradeCurricularEstagio.questionarioAproveitamentoPorDocenteRegular ");
		sql.append(" left join questionario as qal on qal.codigo = gradeCurricularEstagio.questionarioAproveitamentoPorLicenciatura ");
		sql.append(" left join questionario as qe on qe.codigo = gradeCurricularEstagio.questionarioequivalencia");
		sql.append(" left join pessoa as docenteresponsavelestagio on docenteresponsavelestagio.codigo = gradeCurricularEstagio.docenteresponsavelestagio");
		return sql;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaRapida() {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" curso.codigo as \"curso.codigo\", ");
		sql.append(" curso.nome as \"curso.nome\", ");
		sql.append(" gradeCurricular.codigo as \"gradeCurricular.codigo\", ");
		sql.append(" gradeCurricular.nome as \"gradeCurricular.nome\", ");
		sql.append(" gradeCurricularEstagio.codigo as \"gradeCurricularEstagio.codigo\",  ");
		sql.append(" gradeCurricularEstagio.nome as \"gradeCurricularEstagio.nome\",  ");				
		sql.append(" gradeCurricularEstagio.cargaHorarioObrigatorio as \"gradeCurricularEstagio.cargaHorarioObrigatorio\", ");		
		sql.append(" gradeCurricularEstagio.horaMaximaAproveitamentoOuEquivalencia as \"gradeCurricularEstagio.horaMaximaAproveitamentoOuEquivalencia\", ");		
		sql.append(" gradeCurricularEstagio.gradeCurricularEstagioAreaEnum as \"gradeCurricularEstagio.gradeCurricularEstagioAreaEnum\", ");		
		sql.append(" gradeCurricularEstagio.gradeCurricularEstagioQuestionarioEnum as \"gradeCurricularEstagio.gradeCurricularEstagioQuestionarioEnum\", ");
		sql.append(" gradeCurricularEstagio.informarSupervisorEstagio as \"gradeCurricularEstagio.informarSupervisorEstagio\",  ");
		sql.append(" gradeCurricularEstagio.permiteHorasFragmentadas as \"gradeCurricularEstagio.permiteHorasFragmentadas\" ");
		sql.append(" FROM gradeCurricularEstagio ");
		sql.append(" inner join gradeCurricular on gradeCurricular.codigo = gradeCurricularEstagio.gradeCurricular ");
		sql.append(" inner join curso on curso.codigo = gradeCurricular.curso ");
		return sql;
	}
	
	@Override
	public Integer consultarCargahorariaDeferidaEstagio(String matricula, Integer gradeCurricularEstagio) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(cargahorariadeferida) as cargahorariadeferida from estagio where matricula = ? and gradecurricularestagio = ?");
		sql.append(" and estagio.situacaoEstagioEnum != '").append(SituacaoEstagioEnum.INDEFERIDO).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, gradeCurricularEstagio);
		if (rs.next()) {
			return rs.getInt("cargahorariadeferida");
		}
		return 0;
	}
	
	@Override
	public Boolean consultarSeExisteGradeCurricularEstagioPorGradeCurricular(Integer gradeCurricular) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(codigo) QTDE from gradecurricularestagio where gradecurricular = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), gradeCurricular);
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GradeCurricularEstagioVO> consultarGradeCurricularEstagioRelatorioSeiDecidir(UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = new StringBuilder("select ");
			sqlStr.append(" gradeCurricularEstagio.codigo as \"gradeCurricularEstagio.codigo\",  ");
			sqlStr.append(" gradeCurricularEstagio.nome as \"gradeCurricularEstagio.nome\"  ");	
			sqlStr.append(" from gradeCurricularEstagio ");
			sqlStr.append(" order by  gradeCurricularEstagio.nome ");
			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			List<GradeCurricularEstagioVO> vetResultado = new ArrayList<>();
			while (dadosSQL.next()) {
				GradeCurricularEstagioVO obj = new GradeCurricularEstagioVO();
				obj.setNovoObj(Boolean.FALSE);
				obj.setCodigo(dadosSQL.getInt("gradeCurricularEstagio.codigo"));
				obj.setNome(dadosSQL.getString("gradeCurricularEstagio.nome"));
				vetResultado.add(obj);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GradeCurricularEstagioVO> consultarPorGradeCurricularVO(GradeCurricularVO obj, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsulta();
			sqlStr.append(" where gradeCurricularEstagio.gradeCurricular = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
			List<GradeCurricularEstagioVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				GradeCurricularEstagioVO gre = new GradeCurricularEstagioVO();
				montarDadosBasico(gre, tabelaResultado, nivelMontarDados, usuario);
				gre.setGradeCurricularVO(obj);
				vetResultado.add(gre);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GradeCurricularEstagioVO> consultaRapidaPorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaRapida();
		sqlStr.append("WHERE sem_acentos(gradeCurricularEstagio.nome) ilike(sem_acentos(?)) ");
		sqlStr.append("ORDER BY gradeCurricularEstagio.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GradeCurricularEstagioVO> consultaRapidaPorCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaRapida();
		sqlStr.append("WHERE sem_acentos(curso.nome) ilike(sem_acentos(?)) ");
		sqlStr.append("ORDER BY gradeCurricularEstagio.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GradeCurricularEstagioVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sql = getSQLPadraoConsultaRapida();
			sql.append(" where gradeCurricularEstagio.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
			GradeCurricularEstagioVO obj = new GradeCurricularEstagioVO();
			if (tabelaResultado.next()) {
				montarDadosRapido(obj, tabelaResultado, nivelMontarDados, usuario);
			}
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GradeCurricularEstagioVO> montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<GradeCurricularEstagioVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			GradeCurricularEstagioVO obj = new GradeCurricularEstagioVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(GradeCurricularEstagioVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("gradeCurricularEstagio.codigo"));
		obj.setNome(dadosSQL.getString("gradeCurricularEstagio.nome"));
		obj.setCargaHorarioObrigatorio(dadosSQL.getInt("gradeCurricularEstagio.cargaHorarioObrigatorio"));
		obj.setHoraMaximaAproveitamentoOuEquivalencia(dadosSQL.getInt("gradeCurricularEstagio.horaMaximaAproveitamentoOuEquivalencia"));
		obj.setPermiteHorasFragmentadas(dadosSQL.getBoolean("gradeCurricularEstagio.permiteHorasFragmentadas"));
		obj.setGradeCurricularEstagioAreaEnum(GradeCurricularEstagioAreaEnum.valueOf(dadosSQL.getString("gradeCurricularEstagio.gradeCurricularEstagioAreaEnum")));
		obj.setGradeCurricularEstagioQuestionarioEnum(GradeCurricularEstagioQuestionarioEnum.valueOf(dadosSQL.getString("gradeCurricularEstagio.gradeCurricularEstagioQuestionarioEnum")));
		obj.getTextoPadraoDeclaracaoVO().setCodigo(dadosSQL.getInt("textoPadraoDeclaracao.codigo"));
		obj.getTextoPadraoDeclaracaoVO().setDescricao(dadosSQL.getString("textoPadraoDeclaracao.descricao"));
//		obj.getQuestionarioRelatorioFinal().setCodigo(dadosSQL.getInt("qrf.codigo"));
//		obj.getQuestionarioRelatorioFinal().setDescricao(dadosSQL.getString("qrf.descricao"));
//		obj.getQuestionarioAproveitamentoPorDocenteRegular().setCodigo(dadosSQL.getInt("qadr.codigo"));
//		obj.getQuestionarioAproveitamentoPorDocenteRegular().setDescricao(dadosSQL.getString("qadr.descricao"));
//		obj.getQuestionarioAproveitamentoPorLicenciatura().setCodigo(dadosSQL.getInt("qal.codigo"));
//		obj.getQuestionarioAproveitamentoPorLicenciatura().setDescricao(dadosSQL.getString("qal.descricao"));
//		obj.getQuestionarioEquivalencia().setCodigo(dadosSQL.getInt("qe.codigo"));
//		obj.getQuestionarioEquivalencia().setDescricao(dadosSQL.getString("qe.descricao"));
		obj.getDocenteResponsavelEstagio().setCodigo(dadosSQL.getInt("docenteresponsavelestagio.codigo"));
		obj.getDocenteResponsavelEstagio().setNome(dadosSQL.getString("docenteresponsavelestagio.nome"));
		obj.setInformarSupervisorEstagio(dadosSQL.getBoolean("gradeCurricularEstagio.informarSupervisorEstagio"));
	}
	
	
	public List<GradeCurricularEstagioVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<GradeCurricularEstagioVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			GradeCurricularEstagioVO obj = new GradeCurricularEstagioVO();
			montarDadosRapido(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDadosRapido(GradeCurricularEstagioVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("gradeCurricularEstagio.codigo"));
		obj.setNome(dadosSQL.getString("gradeCurricularEstagio.nome"));		
		obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("gradeCurricular.codigo"));
		obj.getGradeCurricularVO().setNome(dadosSQL.getString("gradeCurricular.nome"));		
		obj.getGradeCurricularVO().getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getGradeCurricularVO().getCursoVO().setNome(dadosSQL.getString("curso.nome"));		
		obj.setCargaHorarioObrigatorio(dadosSQL.getInt("gradeCurricularEstagio.cargaHorarioObrigatorio"));
		obj.setHoraMaximaAproveitamentoOuEquivalencia(dadosSQL.getInt("gradeCurricularEstagio.horaMaximaAproveitamentoOuEquivalencia"));
		obj.setPermiteHorasFragmentadas(dadosSQL.getBoolean("gradeCurricularEstagio.permiteHorasFragmentadas"));
		obj.setGradeCurricularEstagioAreaEnum(GradeCurricularEstagioAreaEnum.valueOf(dadosSQL.getString("gradeCurricularEstagio.gradeCurricularEstagioAreaEnum")));
		obj.setGradeCurricularEstagioQuestionarioEnum(GradeCurricularEstagioQuestionarioEnum.valueOf(dadosSQL.getString("gradeCurricularEstagio.gradeCurricularEstagioQuestionarioEnum")));
		obj.setInformarSupervisorEstagio(dadosSQL.getBoolean("gradeCurricularEstagio.informarSupervisorEstagio"));
	}
	
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GradeCurricularEstagioVO> consultarPorGradeCurricularMatriculaHistoricoRel(Integer gradeCurricular, String matricula, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select gradecurricularestagio.nome, pessoa.codigo as codigodocente, pessoa.nome as nomedocente, '' as titulacao, gradecurricularestagio.cargahorarioobrigatorio,");
			sqlStr.append(" coalesce(sum(case when estagio.situacaoestagioenum = 'DEFERIDO' THEN estagio.cargahorariadeferida else 0 END), 0) as cargaHorariaAprovada,");
			sqlStr.append(" coalesce(sum(case when estagio.situacaoestagioenum not in ('DEFERIDO', 'INDEFERIDO') THEN estagio.cargahorariadeferida else 0 END), 0) as cargaHorariaEmRealizacao,");
			sqlStr.append(" coalesce(sum(case when estagio.situacaoestagioenum =  'INDEFERIDO' THEN estagio.cargahorariadeferida else 0 END), 0) as cargaHorariaIndeferida,");
			sqlStr.append(" case when gradecurricularestagio.cargahorarioobrigatorio    -    coalesce(sum(case when estagio.situacaoestagioenum !=  'INDEFERIDO'");			
				sqlStr.append(" THEN estagio.cargahorariadeferida else 0 END), 0) > 0");
				sqlStr.append(" THEN gradecurricularestagio.cargahorarioobrigatorio    -    coalesce(sum(case when estagio.situacaoestagioenum !=  'INDEFERIDO'");
				sqlStr.append(" THEN estagio.cargahorariadeferida else 0 END), 0) else 0 end as cargaHorariaPendente,");
			sqlStr.append(" case when coalesce(sum(case when estagio.situacaoestagioenum not in ('DEFERIDO', 'INDEFERIDO') THEN estagio.cargahorariadeferida else 0 END), 0) > 0  ");
				sqlStr.append(" then 'CURSANDO' else case when coalesce(sum(case when estagio.situacaoestagioenum = 'DEFERIDO'");
				sqlStr.append(" THEN estagio.cargahorariadeferida else 0 END), 0) >= gradecurricularestagio.cargahorarioobrigatorio then 'APROVADO' else 'A CURSAR' end end as situacao,");
			sqlStr.append(" ((coalesce(sum(case when estagio.situacaoestagioenum = 'DEFERIDO' THEN estagio.cargahorariadeferida else 0 END), 0) * 100) /  gradecurricularestagio.cargahorarioobrigatorio) as percentual,");
			sqlStr.append(" coalesce(extract( year from max(estagio.datadeferimento))::varchar, '')||'/'||");
			sqlStr.append(" case when extract( month from max(estagio.datadeferimento)) <= 3 then '1' else ");
			sqlStr.append(" case when extract( month from max(estagio.datadeferimento)) <= 6 then '2' else");
			sqlStr.append(" case when extract( month from max(estagio.datadeferimento)) <= 9 then '3' else");
			sqlStr.append(" case when extract( month from max(estagio.datadeferimento)) <= 12 then '4' else ");
			sqlStr.append(" '' end end end end as anoBimestre, docenteresponsavelestagio.codigo codigodocenteresponsavelestagio, docenteresponsavelestagio.nome nomedocenteresponsavelestagio");
			sqlStr.append(" from gradecurricularestagio");
			sqlStr.append(" left join estagio on estagio.gradecurricularestagio = gradecurricularestagio.codigo and estagio.matricula = '").append(matricula).append("'");
			sqlStr.append(" inner join matricula on matricula.matricula = '").append(matricula).append("'");;
			sqlStr.append(" inner join unidadeensino on unidadeensino.codigo =  matricula.unidadeensino ");
			sqlStr.append(" left join funcionario on unidadeensino.orientadorPadraoEstagio =  funcionario.codigo ");
			sqlStr.append(" left join pessoa on funcionario.pessoa = pessoa.codigo");
			sqlStr.append(" left join pessoa docenteresponsavelestagio on docenteresponsavelestagio.codigo = estagio.docenteresponsavelestagio");
			sqlStr.append(" where gradecurricularestagio.gradecurricular = ").append(gradeCurricular);
			sqlStr.append(" and not exists (select gradedisciplina.codigo from periodoletivo");
				sqlStr.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
				sqlStr.append(" inner join disciplina on gradedisciplina.disciplina = disciplina.codigo");
				sqlStr.append(" where periodoletivo.gradecurricular = ").append(gradeCurricular).append(" and disciplina.classificacaodisciplina = 'ESTAGIO' limit 1)");
			sqlStr.append("  group by gradecurricularestagio.nome,  gradecurricularestagio.gradecurricular, gradecurricularestagio.cargahorarioobrigatorio, pessoa.nome, pessoa.codigo, docenteresponsavelestagio.codigo, docenteresponsavelestagio.nome");
			
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			List<GradeCurricularEstagioVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				GradeCurricularEstagioVO gradeCurricularEstagioVO = new GradeCurricularEstagioVO();
				gradeCurricularEstagioVO.setNome(tabelaResultado.getString("nome"));
				if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("codigodocenteresponsavelestagio"))) {
					gradeCurricularEstagioVO.getDocente().setCodigo(tabelaResultado.getInt("codigodocenteresponsavelestagio"));
					gradeCurricularEstagioVO.setNomeDocente(tabelaResultado.getString("nomedocenteresponsavelestagio"));				
				} else {
					gradeCurricularEstagioVO.getDocente().setCodigo(tabelaResultado.getInt("codigodocente"));
					gradeCurricularEstagioVO.setNomeDocente(tabelaResultado.getString("nomedocente"));				
				}
				gradeCurricularEstagioVO.setCargaHorarioObrigatorio(tabelaResultado.getInt("cargahorarioobrigatorio"));
				gradeCurricularEstagioVO.setCargaHorariaAprovada(tabelaResultado.getInt("cargaHorariaAprovada"));
				gradeCurricularEstagioVO.setCargaHorariaEmRealizacao(tabelaResultado.getInt("cargaHorariaEmRealizacao"));
				gradeCurricularEstagioVO.setCargaHorariaIndeferida(tabelaResultado.getInt("cargaHorariaIndeferida"));
				gradeCurricularEstagioVO.setCargaHorariaPendente(tabelaResultado.getInt("cargaHorariaPendente"));
				gradeCurricularEstagioVO.setSituacao(tabelaResultado.getString("situacao"));
				//gradeCurricularEstagioVO.setPercentual(tabelaResultado.getInt("percentual"));
				gradeCurricularEstagioVO.setAnoBimestre(tabelaResultado.getString("anoBimestre"));
				
				if(Uteis.isAtributoPreenchido(gradeCurricularEstagioVO.getDocente())) {
					gradeCurricularEstagioVO.setDocente(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(gradeCurricularEstagioVO.getDocente().getCodigo(), true, false, false, usuario));
					gradeCurricularEstagioVO.setTitulacaoDocente(gradeCurricularEstagioVO.getDocente().getMaiorTitulacaoNivelEscolaridade());
				}
				vetResultado.add(gradeCurricularEstagioVO);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	

	@Override
	public Map<Integer, Integer> consultarCargahorariaDeferidaEstagio(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(cargahorariadeferida) as cargahorariadeferida, gradecurricularestagio from estagio where matricula = ? ");
		sql.append(" and estagio.situacaoEstagioEnum != '").append(SituacaoEstagioEnum.INDEFERIDO).append("' group by gradecurricularestagio ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		Map<Integer, Integer> retorno = new HashMap<Integer, Integer>(0); 
		while (rs.next()) {
			retorno.put(rs.getInt("gradecurricularestagio"), rs.getInt("cargahorariadeferida"));
		}
		return retorno;
	}

}
