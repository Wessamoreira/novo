package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CalendarioLancamentoPlanoEnsinoVO;
import negocio.comuns.academico.CalendarioLancamentoPlanoEnsinoVO.EnumCampoCalendarioPor;
import negocio.comuns.academico.CalendarioLancamentoPlanoEnsinoVO.EnumCampoConsultaCalendarioLancamentoPlanoEnsino;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SemestreEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AfastamentoFuncionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.academico.CalendarioLancamentoPlanoEnsinoInterfaceFacade;

@Repository
@Lazy
public class CalendarioLancamentoPlanoEnsino extends SuperFacade<CalendarioLancamentoPlanoEnsinoVO>
	implements CalendarioLancamentoPlanoEnsinoInterfaceFacade<CalendarioLancamentoPlanoEnsinoVO> {

	private static final long serialVersionUID = 2558687642091097114L;

	protected static String idEntidade;

	public CalendarioLancamentoPlanoEnsino() {
		super();
		setIdEntidade("CalendarioLancamentoPlanoEnsino");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(CalendarioLancamentoPlanoEnsinoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		validarDadosUnicidade(obj);
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			alterar(obj, validarAcesso, usuarioVO);
		} else {
			incluir(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(CalendarioLancamentoPlanoEnsinoVO obj, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception {
		CalendarioLancamentoPlanoEnsino.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, "CalendarioLancamentoPlanoEnsino",
				new AtributoPersistencia()
						.add("unidadeEnsino", obj.getUnidadeEnsinoVO())
						.add("calendarioPor", obj.getCalendarioPor())
						.add("ano", !obj.getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL) ? obj.getAno() : null)
						.add("semestre", Uteis.isAtributoPreenchido(obj.getSemestre()) ? obj.getSemestre().getValor() : null)
						.add("curso", obj.getCursoVO())
						.add("turno", obj.getTurnoVO())
						.add("disciplina", obj.getDisciplinaVO())
						.add("periodicidade", obj.getPeriodicidade().name())
						.add("professor", obj.getProfessor())
						.add("dataInicio", Uteis.getDataJDBCTimestamp(obj.getDataInicio()))
						.add("dataFim", Uteis.getDataJDBCTimestamp(obj.getDataFim())), usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterar(CalendarioLancamentoPlanoEnsinoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		CalendarioLancamentoPlanoEnsino.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "CalendarioLancamentoPlanoEnsino",
				new AtributoPersistencia()
						.add("unidadeEnsino", obj.getUnidadeEnsinoVO())
						.add("calendarioPor", obj.getCalendarioPor())
						.add("ano", !obj.getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL) ? obj.getAno() : null)
						.add("semestre", Uteis.isAtributoPreenchido(obj.getSemestre()) ? obj.getSemestre().getValor() : null)
						.add("curso", obj.getCursoVO())
						.add("turno", obj.getTurnoVO())
						.add("disciplina", obj.getDisciplinaVO())
						.add("periodicidade", obj.getPeriodicidade().name())
						.add("professor", obj.getProfessor())
						.add("dataInicio", Uteis.getDataJDBCTimestamp(obj.getDataInicio()))
						.add("dataFim", Uteis.getDataJDBCTimestamp(obj.getDataFim())), 
						new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(CalendarioLancamentoPlanoEnsinoVO obj, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception {
		CalendarioLancamentoPlanoEnsino.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM CalendarioLancamentoPlanoEnsino WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		
	}

	public void validarDadosUnicidade(CalendarioLancamentoPlanoEnsinoVO obj) throws ConsistirException {
		Integer retorno = consultarDadosExisteUnicidade(obj);
		if (retorno > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoPlanoEnsino_validarDadosUnicidade"));
		}
	}

	private Integer consultarDadosExisteUnicidade(CalendarioLancamentoPlanoEnsinoVO obj) {
		DataModelo dataModelo = new DataModelo();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(codigo) as qtde FROM CalendarioLancamentoPlanoEnsino");
		sql.append(" WHERE 1 = 1");

		montarDadosFiltroWhere(dataModelo, obj, sql, true);
		if(Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo());
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	@Override
	public void validarDados(CalendarioLancamentoPlanoEnsinoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getCalendarioPor())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getPeriodicidade())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoPlanoEnsino_periodicidade"));
		}
		
		if (obj.getCalendarioPor().equals(EnumCampoCalendarioPor.PROFESSOR.name())) {
			if (!Uteis.isAtributoPreenchido(obj.getProfessor())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoPlanoEnsino_professor"));
			}
		}
		
		if (obj.getCalendarioPor().equals(EnumCampoCalendarioPor.CURSO.name())) {
			if (!Uteis.isAtributoPreenchido(obj.getCursoVO())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoPlanoEnsino_curso"));
			}

			if (!Uteis.isAtributoPreenchido(obj.getDataFim())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoPlanoEnsino_turno"));
			}
		}
		
		if (!obj.getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL) && !Uteis.isAtributoPreenchido(obj.getAno())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoPlanoEnsino_ano"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getDataInicio())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PainelGestorFinanceiro_dataInicio"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getDataFim())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PainelGestorFinanceiro_dataTermino"));
		}
		if (obj.getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)) {
			if (!Uteis.isAtributoPreenchido(obj.getSemestre())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_CalendarioLancamentoPlanoEnsino_semestre"));
			}
		}
	}

	@Override
	public CalendarioLancamentoPlanoEnsinoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM CalendarioLancamentoPlanoEnsino WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
	}

	@Override
	public CalendarioLancamentoPlanoEnsinoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados)
			throws Exception {
		CalendarioLancamentoPlanoEnsinoVO obj = new CalendarioLancamentoPlanoEnsinoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("unidadeEnsino"))) {			
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("unidadeEnsino"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		obj.setCalendarioPor(tabelaResultado.getString("calendarioPor"));
		obj.setAno(tabelaResultado.getString("ano"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("semestre"))) {
			obj.setSemestre(SemestreEnum.getEnum(tabelaResultado.getString("semestre")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("periodicidade"))) {
			obj.setPeriodicidade(PeriodicidadeEnum.valueOf(tabelaResultado.getString("periodicidade")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("curso"))) {			
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("curso"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("turno"))) {			
			obj.setTurnoVO(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("turno"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("disciplina"))) {			
			obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("disciplina"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("professor"))) {
			obj.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("professor"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		obj.setDataInicio(tabelaResultado.getDate("dataInicio"));
		obj.setDataFim(tabelaResultado.getDate("dataFim"));

		return obj;
	}

	public CalendarioLancamentoPlanoEnsinoVO montarDadosCalendarioLancamentoPlanoEnsion(SqlRowSet tabelaResultado, int nivelMontarDados)
			throws Exception {
		CalendarioLancamentoPlanoEnsinoVO obj = new CalendarioLancamentoPlanoEnsinoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("unidadeEnsinoVO"))) {			
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("unidadeEnsinoVO"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		obj.setCalendarioPor(tabelaResultado.getString("calendarioPor"));
		obj.setAno(tabelaResultado.getString("ano"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("semestre"))) {
			obj.setSemestre(SemestreEnum.valueOf(tabelaResultado.getString("semestre")));
		}
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("periodicidade"))) {
			obj.setPeriodicidade(PeriodicidadeEnum.valueOf(tabelaResultado.getString("periodicidade")));
		}
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("cursoVO"))) {			
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("cursoVO"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("turnoVO"))) {			
			obj.setTurnoVO(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("turnoVO"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("disciplinaVO"))) {			
			obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("disciplinaVO"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("professor"))) {
			obj.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("professor"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		obj.setDataInicio(tabelaResultado.getDate("dataInicio"));
		obj.setDataFim(tabelaResultado.getDate("dataFim"));
		
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarCalendarioLancamentoPlanoEnsino(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalCalendarioLancamentoPlanoEnsino(dataModelo));		
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, CalendarioLancamentoPlanoEnsinoVO obj) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarCalendarioLancamentoPlanoEnsino(dataModelo, obj));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalCalendarioLancamentoPlanoEnsino(dataModelo, obj));		
	}

	/**
	 * Consulta Paginada dos historicos dos dependentes retornando 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<CalendarioLancamentoPlanoEnsinoVO> consultarCalendarioLancamentoPlanoEnsino(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM CalendarioLancamentoPlanoEnsino");
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);

		switch (EnumCampoConsultaCalendarioLancamentoPlanoEnsino.valueOf(dataModelo.getCampoConsulta())) {
		case CODIGO:
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta().toUpperCase());
			sql.append(" AND codigo = ?");
			break;
		default:
			break;
		}

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosLista(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	private List<CalendarioLancamentoPlanoEnsinoVO> consultarCalendarioLancamentoPlanoEnsino(DataModelo dataModelo, CalendarioLancamentoPlanoEnsinoVO obj) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM CalendarioLancamentoPlanoEnsino");
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);
		
		montarDadosFiltroWhere(dataModelo, obj, sql, false);

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		
		return montarDadosLista(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	private void montarDadosFiltroWhere(DataModelo dataModelo, CalendarioLancamentoPlanoEnsinoVO obj,
			StringBuilder sql, boolean validarPeriodo) {
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.unidadeensino = ?");
			dataModelo.getListaFiltros().add(obj.getUnidadeEnsinoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getAno())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.ano = ?");
			dataModelo.getListaFiltros().add(obj.getAno());
		}
		if (Uteis.isAtributoPreenchido(obj.getSemestre())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.semestre = ?");
			dataModelo.getListaFiltros().add(obj.getSemestre().getValor());
		}
		if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.curso = ?");
			dataModelo.getListaFiltros().add(obj.getCursoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getTurnoVO())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.turno = ?");
			dataModelo.getListaFiltros().add(obj.getTurnoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getProfessor())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.professor = ?");
			dataModelo.getListaFiltros().add(obj.getProfessor().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getDisciplinaVO())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.disciplina = ?");
			dataModelo.getListaFiltros().add(obj.getDisciplinaVO().getCodigo());
		}

		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.codigo != ?");
			dataModelo.getListaFiltros().add(obj.getCodigo());			
		}

		if (validarPeriodo) {
			sql.append(" and ").append(realizarGeracaoWhereDataInicioDataTermino(obj.getDataInicio(), obj.getDataFim(), "CalendarioLancamentoPlanoEnsino.datainicio", "CalendarioLancamentoPlanoEnsino.datafim", false));
		}
	}

	private void montarDadosFiltroWherePlanoEnsino(PlanoEnsinoVO obj, List<Object> filtros, StringBuilder sql, boolean visaoProfessor, boolean filtrarCalendarioPor) {
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.unidadeensino = ?");
			filtros.add(obj.getUnidadeEnsino().getCodigo());
		}		
		if (Uteis.isAtributoPreenchido(obj.getAno()) && !obj.getCurso().getIntegral()) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.ano = ?");
			filtros.add(obj.getAno());
		}
		if (Uteis.isAtributoPreenchido(obj.getSemestre()) && obj.getCurso().getSemestral()) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.semestre = ?");
			filtros.add(obj.getSemestre());
		}
		
		if (visaoProfessor || filtrarCalendarioPor) {
			sql.append(" and case");
			sql.append(" 	when calendariopor = 'UNIDADE_ENSINO' then CalendarioLancamentoPlanoEnsino.unidadeensino =").append(obj.getUnidadeEnsino().getCodigo());
			sql.append(" 	when calendariopor = 'CURSO' then CalendarioLancamentoPlanoEnsino.curso = ").append(obj.getCurso().getCodigo().toString());
			sql.append(" 	when calendariopor = 'PROFESSOR' then CalendarioLancamentoPlanoEnsino.professor = ").append(obj.getProfessorResponsavel().getCodigo().toString()).append(" end");
		} else {
			if (Uteis.isAtributoPreenchido(obj.getCurso())) {
				sql.append(" and CalendarioLancamentoPlanoEnsino.curso = ?");
				filtros.add(obj.getCurso().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(obj.getProfessorResponsavel())) {
				sql.append(" and CalendarioLancamentoPlanoEnsino.professor = ?");
				filtros.add(obj.getProfessorResponsavel().getCodigo());
			}
		}

		if (Uteis.isAtributoPreenchido(obj.getTurno())) {
			sql.append(" and CalendarioLancamentoPlanoEnsino.turno = ?");
			filtros.add(obj.getTurno().getCodigo());
		}

		if (Uteis.isAtributoPreenchido(obj.getDisciplina())) {
			sql.append(" and (CalendarioLancamentoPlanoEnsino.disciplina = ? or CalendarioLancamentoPlanoEnsino.disciplina is null)");
			filtros.add(obj.getDisciplina().getCodigo());
		}
	}
	
	/**
	 * Consulta o total de {@link AfastamentoFuncionarioVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalCalendarioLancamentoPlanoEnsino(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT COUNT(codigo) as qtde FROM CalendarioLancamentoPlanoEnsino ");
        sql.append(" WHERE 1 = 1");

        switch (EnumCampoConsultaCalendarioLancamentoPlanoEnsino.valueOf(dataModelo.getCampoConsulta())) {
		case CODIGO:
			sql.append(" AND codigo = ?");
			break;
		default:
			break;
		}

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getValorConsulta());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Consulta o total de {@link AfastamentoFuncionarioVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalCalendarioLancamentoPlanoEnsino(DataModelo dataModelo, CalendarioLancamentoPlanoEnsinoVO obj) throws Exception {
		dataModelo.getListaFiltros().clear();
		StringBuilder sql = new StringBuilder("SELECT COUNT(codigo) as qtde FROM CalendarioLancamentoPlanoEnsino ");
		sql.append(" WHERE 1 = 1");
		
		montarDadosFiltroWhere(dataModelo, obj, sql, false);
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	/**
	 * Monta a lista de {@link AfastamentoFuncionarioVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<CalendarioLancamentoPlanoEnsinoVO> montarDadosLista(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<CalendarioLancamentoPlanoEnsinoVO> calendarioLancamentoPlanoEnsinoVOs = new ArrayList<>();

        while(tabelaResultado.next()) {
        	calendarioLancamentoPlanoEnsinoVOs.add(montarDados(tabelaResultado, nivelMontarDados));
        }
		return calendarioLancamentoPlanoEnsinoVOs;
	}

	@Override
	public CalendarioLancamentoPlanoEnsinoVO consultarCalendarioLancamentoPorPlanoEnsino(PlanoEnsinoVO planoEnsinoVO, boolean visaoProfessor) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM CalendarioLancamentoPlanoEnsino");
		sql.append(" WHERE 1 = 1");
		List<Object> filtros = new ArrayList<>();
		montarDadosFiltroWherePlanoEnsino(planoEnsinoVO, filtros, sql, visaoProfessor, true);
		sql.append(" order by CalendarioLancamentoPlanoEnsino.unidadeensino, CalendarioLancamentoPlanoEnsino.curso, CalendarioLancamentoPlanoEnsino.disciplina limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		if (!tabelaResultado.next()) {
			return null;
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		CalendarioLancamentoPlanoEnsino.idEntidade = idEntidade;
	}
}
