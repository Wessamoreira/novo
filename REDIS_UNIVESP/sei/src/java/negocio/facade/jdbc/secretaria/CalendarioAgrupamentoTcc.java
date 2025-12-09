package negocio.facade.jdbc.secretaria;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import examples.unix.rshell;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoDisciplinaVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.estagio.Concedente;
import negocio.interfaces.secretaria.CalendarioAgrupamentoTccInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class CalendarioAgrupamentoTcc extends ControleAcesso implements CalendarioAgrupamentoTccInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3019279713443166659L;
	private static String idEntidade = "CalendarioAgrupamentoTcc";

	public static String getIdEntidade() {
		return CalendarioAgrupamentoTcc.idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void valiarDados(CalendarioAgrupamentoTccVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getClassificacaoAgrupamento()), UteisJSF.internacionalizar("msg_CalendarioAgrupamentoTcc_classificacao"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getAno()), UteisJSF.internacionalizar("msg_CalendarioAgrupamentoTcc_ano"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSemestre()), UteisJSF.internacionalizar("msg_CalendarioAgrupamentoTcc_semestre"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataInicial()), UteisJSF.internacionalizar("msg_CalendarioAgrupamentoTcc_dataInicial"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataFinal()), UteisJSF.internacionalizar("msg_CalendarioAgrupamentoTcc_dataFinal"));
		Uteis.checkState(obj.getDataInicial().after(obj.getDataFinal()), UteisJSF.internacionalizar("msg_CalendarioAgrupamentoTcc_dataInicialMaiorDataFinal"));
		Uteis.checkState(obj.getDataFinal().before(obj.getDataInicial()), UteisJSF.internacionalizar("msg_CalendarioAgrupamentoTcc_dataFinalMenorDataInicial"));
		validarUnicidade(obj, true);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CalendarioAgrupamentoTccVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			valiarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CalendarioAgrupamentoTccVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CalendarioAgrupamentoTcc.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "calendarioAgrupamentoTcc", new AtributoPersistencia()
					.add("classificacaoAgrupamento", obj.getClassificacaoAgrupamento())
					.add("ano", obj.getAno())
					.add("semestre", obj.getSemestre()) 
					.add("datainicial", obj.getDataInicial()) 
					.add("datafinal", obj.getDataFinal()), 
					usuario);
			getFacadeFactory().getCalendarioAgrupamentoDisciplinaFacade().incluirCalendarioAgrupamentoDisciplinaVOs(obj, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CalendarioAgrupamentoTccVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			CalendarioAgrupamentoTcc.alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "calendarioAgrupamentoTcc", new AtributoPersistencia()
					.add("classificacaoAgrupamento", obj.getClassificacaoAgrupamento())
					.add("ano", obj.getAno())
					.add("semestre", obj.getSemestre()) 
					.add("datainicial", obj.getDataInicial()) 
					.add("datafinal", obj.getDataFinal())
					, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
			getFacadeFactory().getCalendarioAgrupamentoDisciplinaFacade().alterarCalendarioAgrupamentoDisciplinaVOs(obj, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CalendarioAgrupamentoTccVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			Concedente.excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM calendarioAgrupamentoTcc WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, CalendarioAgrupamentoTccVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
	}

	private List<CalendarioAgrupamentoTccVO> consultaRapidaPorFiltros(CalendarioAgrupamentoTccVO obj, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY calendarioAgrupamentoTcc.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			montarTotalizadorConsultaBasica(dataModelo, tabelaResultado);
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(CalendarioAgrupamentoTccVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sqlStr.append(" and calendarioAgrupamentoTcc.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getClassificacaoAgrupamento())) {
			sqlStr.append(" and calendarioAgrupamentoTcc.classificacaoAgrupamento = ?");
			dataModelo.getListaFiltros().add(obj.getClassificacaoAgrupamento().name());
		}
		if (Uteis.isAtributoPreenchido(obj.getAno())) {
			sqlStr.append(" and calendarioAgrupamentoTcc.ano = ?");
			dataModelo.getListaFiltros().add(obj.getAno().toLowerCase());
		}
		if (Uteis.isAtributoPreenchido(obj.getSemestre())) {
			sqlStr.append(" and calendarioAgrupamentoTcc.semestre = ?");
			dataModelo.getListaFiltros().add(obj.getSemestre().toLowerCase());
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAgrupamentoTccVO consultarPorClassificacaoPorAnoPorSemestre(ClassificacaoDisciplinaEnum classificacaoAgrupamento, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where calendarioAgrupamentoTcc.classificacaoAgrupamento = ? ");
			sqlStr.append(" and calendarioAgrupamentoTcc.ano = ? ");
			sqlStr.append(" and calendarioAgrupamentoTcc.semestre = ? order by case when  calendarioAgrupamentoTcc.dataInicial <= current_date and calendarioAgrupamentoTcc.dataFinal >= current_date then 0 else 1 end,  calendarioAgrupamentoTcc.dataInicial desc limit 1 ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), classificacaoAgrupamento.name(), ano, semestre);
			if (!tabelaResultado.next()) {
				return new CalendarioAgrupamentoTccVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CalendarioAgrupamentoTccVO> consultarPorClassificacaoAnoSemestre(ClassificacaoDisciplinaEnum classificacaoAgrupamento, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where calendarioAgrupamentoTcc.classificacaoAgrupamento = ? ");
			sqlStr.append(" and calendarioAgrupamentoTcc.ano = ? ");
			sqlStr.append(" and calendarioAgrupamentoTcc.semestre = ? order by case when  calendarioAgrupamentoTcc.dataInicial <= current_date and calendarioAgrupamentoTcc.dataFinal >= current_date then 0 else 1 end,  calendarioAgrupamentoTcc.dataInicial desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), classificacaoAgrupamento.name(), ano, semestre);			
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAgrupamentoTccVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where calendarioAgrupamentoTcc.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( CalendarioAgrupamentoTccVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Boolean validarUnicidade(CalendarioAgrupamentoTccVO obj, boolean isLevantarExcecao) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM calendarioAgrupamentoTcc ");
		sql.append(" WHERE classificacaoagrupamento = ? ");
		sql.append(" and ano=  ? ");		
		sql.append(" and semestre= ? ");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		sql.append(" and ((dataInicial >= '").append(Uteis.getDataJDBC(obj.getDataInicial())).append("' and dataFinal <= '").append(Uteis.getDataJDBC(obj.getDataFinal())).append("' ) ");
		sql.append(" or (dataInicial <= '").append(Uteis.getDataJDBC(obj.getDataInicial())).append("' and dataFinal >= '").append(Uteis.getDataJDBC(obj.getDataFinal())).append("' ) ");
		sql.append(" or (dataInicial >= '").append(Uteis.getDataJDBC(obj.getDataInicial())).append("' and dataFinal >= '").append(Uteis.getDataJDBC(obj.getDataFinal())).append("' and dataFinal <= '").append(Uteis.getDataJDBC(obj.getDataInicial())).append("' ) ");
		sql.append(" or (dataInicial <= '").append(Uteis.getDataJDBC(obj.getDataInicial())).append("' and dataFinal >= '").append(Uteis.getDataJDBC(obj.getDataInicial())).append("') ");
		sql.append(" ) ");
		
		if(!obj.getCalendarioAgrupamentoDisciplinaVOs().isEmpty() && obj.getCalendarioAgrupamentoDisciplinaVOs().stream().anyMatch(t -> t.getSelecionado())) {
			sql.append(" and ( not exists( select calendarioAgrupamentoDisciplina.codigo from calendarioAgrupamentoDisciplina where calendarioAgrupamentoDisciplina.calendarioAgrupamento = calendarioAgrupamentoTcc.codigo ) ");
			sql.append(" or exists ( select calendarioAgrupamentoDisciplina.codigo from calendarioAgrupamentoDisciplina where calendarioAgrupamentoDisciplina.calendarioAgrupamento = calendarioAgrupamentoTcc.codigo and calendarioAgrupamentoDisciplina.disciplina in (0 ");			
			for(CalendarioAgrupamentoDisciplinaVO agrupamentoDisciplinaVO: obj.getCalendarioAgrupamentoDisciplinaVOs()) {
				if(agrupamentoDisciplinaVO.getSelecionado()) {
					sql.append(", ").append(agrupamentoDisciplinaVO.getDisciplinaVO().getCodigo());
				}
			}
			sql.append(" ))) ");
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getClassificacaoAgrupamento().name(), obj.getAno(), obj.getSemestre());
		boolean existeUnicidade = tabelaResultado.next();
		if (existeUnicidade && !isLevantarExcecao ) {
			return true;
		}else if(existeUnicidade && isLevantarExcecao ) {
			Uteis.checkState(true, UteisJSF.internacionalizar("msg_CalendarioAgrupamentoTcc_validarUnicidade").replace("{0}", obj.getClassificacaoAgrupamento().name()).replace("{1}", obj.getAno()).replace("{2}", obj.getSemestre()));
		}
		return false ;
	}
	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(getSelectTotalizadorConsultaBasica());
		sql.append(" calendarioAgrupamentoTcc.codigo, calendarioAgrupamentoTcc.ano, calendarioAgrupamentoTcc.semestre, ");
		sql.append(" calendarioAgrupamentoTcc.dataInicial, calendarioAgrupamentoTcc.dataFinal, calendarioAgrupamentoTcc.classificacaoAgrupamento, (select array_to_string(array_agg(d.abreviatura order by d.abreviatura), ', ') from calendarioagrupamentodisciplina cad inner join disciplina as d on d.codigo = cad.disciplina  where cad.calendarioagrupamento = calendarioagrupamentotcc.codigo) as disciplinaApresentarConsulta ");
		sql.append(" FROM calendarioAgrupamentoTcc ");
		return sql;
	}

	private List<CalendarioAgrupamentoTccVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CalendarioAgrupamentoTccVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private CalendarioAgrupamentoTccVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CalendarioAgrupamentoTccVO obj = new CalendarioAgrupamentoTccVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setClassificacaoAgrupamento(ClassificacaoDisciplinaEnum.valueOf(dadosSQL.getString("classificacaoAgrupamento")));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setDataInicial(dadosSQL.getDate("dataInicial"));
		obj.setDataFinal(dadosSQL.getDate("dataFinal"));
		obj.setDisciplinaApresentarConsulta(dadosSQL.getString("disciplinaApresentarConsulta"));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setCalendarioAgrupamentoDisciplinaVOs(getFacadeFactory().getCalendarioAgrupamentoDisciplinaFacade().consultarPorCalendarioAgrupamento(obj.getCodigo(), usuario));
		}		
		return obj;

	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAgrupamentoTccVO consultarPorClassificacaoPorAnoPorSemestrePorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, ClassificacaoDisciplinaEnum classificacaoAgrupamento, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where calendarioAgrupamentoTcc.classificacaoAgrupamento = ? ");
			sqlStr.append(" and calendarioAgrupamentoTcc.ano = ? ");
			sqlStr.append(" and calendarioAgrupamentoTcc.semestre = ? ");
			sqlStr.append(" and ((not exists (select calendarioagrupamentodisciplina.codigo from calendarioagrupamentodisciplina where calendarioagrupamentodisciplina.calendarioAgrupamento = calendarioAgrupamentoTcc.codigo limit 1 ) ) ");
			sqlStr.append(" or (exists (select calendarioagrupamentodisciplina.codigo from calendarioagrupamentodisciplina where calendarioagrupamentodisciplina.calendarioAgrupamento = calendarioAgrupamentoTcc.codigo ");
			sqlStr.append(" and calendarioagrupamentodisciplina.disciplina in (0");
			for(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO: matriculaPeriodoTurmaDisciplinaVOs) {
				if(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getClassificacaoDisciplina().equals(classificacaoAgrupamento)) {
					sqlStr.append(",").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
				}
			}
			sqlStr.append(" ) ))) ");
			sqlStr.append(" order by case when  calendarioAgrupamentoTcc.dataInicial <= current_date and calendarioAgrupamentoTcc.dataFinal >= current_date then 0 else 1 end,  calendarioAgrupamentoTcc.dataInicial desc limit 1 ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), classificacaoAgrupamento.name(), ano, semestre);
			if (!tabelaResultado.next()) {
				return new CalendarioAgrupamentoTccVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAgrupamentoTccVO consultarPorClassificacaoPorAnoPorSemestrePorDisciplina(Integer disciplina, ClassificacaoDisciplinaEnum classificacaoAgrupamento, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where calendarioAgrupamentoTcc.classificacaoAgrupamento = ? ");
			sqlStr.append(" and calendarioAgrupamentoTcc.ano = ? ");
			sqlStr.append(" and calendarioAgrupamentoTcc.semestre = ? ");
			sqlStr.append(" and ((not exists (select calendarioagrupamentodisciplina.codigo from calendarioagrupamentodisciplina where calendarioagrupamentodisciplina.calendarioAgrupamento = calendarioAgrupamentoTcc.codigo limit 1 ) ) ");
			sqlStr.append(" or (exists (select calendarioagrupamentodisciplina.codigo from calendarioagrupamentodisciplina where calendarioagrupamentodisciplina.calendarioAgrupamento = calendarioAgrupamentoTcc.codigo ");
			sqlStr.append(" and calendarioagrupamentodisciplina.disciplina = ").append(disciplina);			
			sqlStr.append(" ))) ");
			sqlStr.append(" order by case when  calendarioAgrupamentoTcc.dataInicial <= current_date and calendarioAgrupamentoTcc.dataFinal >= current_date then 0 else 1 end,  calendarioAgrupamentoTcc.dataInicial desc limit 1 ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), classificacaoAgrupamento.name(), ano, semestre);
			if (!tabelaResultado.next()) {
				return new CalendarioAgrupamentoTccVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAgrupamentoTccVO consultarPorClassificacaoPorHistoricoAnoSemestreAprovadoPorDisciplina(Integer disciplina, String  matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" left join lateral (	");
			sqlStr.append(" 			    select  historico.anohistorico, historico.semestrehistorico  from matricula ");
			sqlStr.append(" 				inner join periodoletivo on periodoletivo.gradecurricular = matricula.gradecurricularatual ");
			sqlStr.append(" 				inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.periodoletivo	 	");
			sqlStr.append(" 				inner join historico  on historico.matricula = matricula.matricula and historico.gradedisciplina = gradedisciplina.codigo and historico.disciplina = gradedisciplina.disciplina ");
			sqlStr.append(" 				inner join disciplina on disciplina.codigo = gradedisciplina.disciplina ");
			sqlStr.append(" 				where  matricula.matricula = ? and historico.situacao IN ('AP','AA','AE','IS','CC','CH','AD','AB')  ");
			sqlStr.append(" 				and ( gradedisciplina.disciplinatcc = true or disciplina.classificacaodisciplina = 'TCC' ) ");
			sqlStr.append(" 		) as historicoAprovado on 1=1												");
			sqlStr.append(" where calendarioAgrupamentoTcc.classificacaoAgrupamento = 'TCC' ");
			sqlStr.append(" and calendarioAgrupamentoTcc.ano =  historicoAprovado.anohistorico  ");
			sqlStr.append(" and calendarioAgrupamentoTcc.semestre = historicoAprovado.semestrehistorico  ");
			sqlStr.append(" and ((not exists (select calendarioagrupamentodisciplina.codigo from calendarioagrupamentodisciplina where calendarioagrupamentodisciplina.calendarioAgrupamento = calendarioAgrupamentoTcc.codigo limit 1 ) ) ");
			sqlStr.append(" or (exists (select calendarioagrupamentodisciplina.codigo from calendarioagrupamentodisciplina where calendarioagrupamentodisciplina.calendarioAgrupamento = calendarioAgrupamentoTcc.codigo ");
			sqlStr.append(" and calendarioagrupamentodisciplina.disciplina = ? ");;			
			sqlStr.append(" ))) ");
			sqlStr.append(" order by case when  calendarioAgrupamentoTcc.dataInicial <= current_date and calendarioAgrupamentoTcc.dataFinal >= current_date then 0 else 1 end,  calendarioAgrupamentoTcc.dataInicial desc limit 1 ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula, disciplina);
			if (!tabelaResultado.next()) {
				return new CalendarioAgrupamentoTccVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

}
