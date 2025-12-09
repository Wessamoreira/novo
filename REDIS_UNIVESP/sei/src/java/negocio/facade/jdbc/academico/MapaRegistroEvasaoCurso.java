package negocio.facade.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialArray;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.MotivoSolicitacaoLiberacaoMatriculaEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.secretaria.MapaRegistroEvasaoCursoVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.TipoTrancamentoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.MapaRegistroEvasaoCursoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class MapaRegistroEvasaoCurso extends ControleAcesso implements MapaRegistroEvasaoCursoInterfaceFacade {
	
	private static final long serialVersionUID = 3584892948831059709L;
	private static String idEntidade = "MapaRegistroEvasaoCurso";

	public static String getIdEntidade() {
		return MapaRegistroEvasaoCurso.idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void valiarDados(MapaRegistroEvasaoCursoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoTrancamentoEnum()), UteisJSF.internacionalizar("msg_MapaRegistroAbandonoCursoTrancamento_tipoTrancamento"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getAno()), UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
		Uteis.checkState(PeriodicidadeEnum.SEMESTRAL.getValor().equals(obj.getPeriodicidade()) && !Uteis.isAtributoPreenchido(obj.getSemestre()), UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
		Uteis.checkState(obj.getUnidadeEnsinoVOs().stream().allMatch(p-> !p.getFiltrarUnidadeEnsino()), "Deve ser informado pelo menos uma UNIDADE DE ENSINO para geração do Mapa.");
		Uteis.checkState(obj.getTipoTrancamentoEnum().isRenovacaoAutomatica() && obj.getQtdMesAlunosRenovacaoSemAcessoAva().equals(0), "o campo Quantidade de Meses para Alunos com Renovação Automática sem Acessar o AVA deve ser informado.");
		Uteis.checkState(obj.getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() && obj.getQtdTrancamentoEmExcesso().equals(0), "o campo Quantidade de Trancamento em excesso para que seja realizado o cancelamento da Matrícula deve ser informado.");
		Uteis.checkState(obj.getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().stream().allMatch(p-> !p.getMatriculaPeriodoVO().getMatriculaVO().getAlunoSelecionado()), UteisJSF.internacionalizar("msg_RenovarTurma_selecionarMatricula"));
		Uteis.checkState(!obj.getTipoTrancamentoEnum().isRenovacaoAutomatica() && !Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento()), "Para que seja realizado a operação, o campo Motivo Cancelamento/Trancamento deve ser informado, Caso o contrário é necessário que seja informado o Motivo Padrão de Evasão/Trancamento de curso, dentro da configuração geral do sistema.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(MapaRegistroEvasaoCursoVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) {
		try {
			if(!Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento()) && !obj.getTipoTrancamentoEnum().isRenovacaoAutomatica()) {
				obj.setMotivoCancelamentoTrancamento(configuracaoGeralSistemaVO.getMotivoPadraoAbandonoCurso());
			}
			valiarDados(obj);
			incluir(obj, verificarAcesso, usuarioVO);
			getFacadeFactory().getMapaRegistroEvasaoCursoMatriculaPeriodoFacade().persistir(obj, verificarAcesso, usuarioVO);
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(true);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MapaRegistroEvasaoCursoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			MapaRegistroEvasaoCurso.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "MapaRegistroEvasaoCurso", new AtributoPersistencia()
					.add("dataRegistro", obj.getDataRegistro())
					.add("tipoTrancamentoEnum", obj.getTipoTrancamentoEnum())
					.add("ano", obj.getAno())
					.add("semestre", obj.getSemestre())
					.add("periodicidade", obj.getPeriodicidade())
					.add("trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte", obj.isTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte())
					.add("trazerAlunoTrancadoAbandonadoAnoSemestreBase", obj.isTrazerAlunoTrancadoAbandonadoAnoSemestreBase())
					.add("considerarTrancamentoConsecutivo", obj.isConsiderarTrancamentoConsecutivo())
					.add("qtdDisciplinaReprovadas", obj.getQtdDisciplinaReprovadas())					
					.add("qtdDiasAlunosSemAcessoAva", obj.getQtdDiasAlunosSemAcessoAva())
					.add("qtdTrancamentoEmExcesso", obj.getQtdTrancamentoEmExcesso())
					.add("qtdMesAlunosRenovacaoSemAcessoAva", obj.getQtdMesAlunosRenovacaoSemAcessoAva())
					.add("usuarioResponsavel", obj.getUsuarioResponsavel())
					.add("motivoCancelamentoTrancamento", obj.getMotivoCancelamentoTrancamento())
					.add("justificativa", obj.getJustificativa())
					.add("unidadeensinos", UtilReflexao.converteListaEntidadeParaArrayInteiro(obj.getUnidadeEnsinoVOs()))
					.add("nivelEducacional", obj.getNivelEducacional())
					.add("cursos", UtilReflexao.converteListaEntidadeParaArrayInteiro(obj.getCursoVOs()))
					.add("turnos", UtilReflexao.converteListaEntidadeParaArrayInteiro(obj.getTurnoVOs()))
					.add("anoRegistroEvasao", obj.getAnoRegistroEvasao())
					.add("semestreRegistroEvasao", obj.getSemestreRegistroEvasao())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}
	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder("select distinct ");
		sql.append(" maparegistroevasaocurso.codigo, ");
		sql.append(" maparegistroevasaocurso.dataRegistro, ");		
		sql.append(" maparegistroevasaocurso.tipoTrancamentoEnum, ");		
		sql.append(" maparegistroevasaocurso.ano, ");		
		sql.append(" maparegistroevasaocurso.semestre, ");		
		sql.append(" maparegistroevasaocurso.periodicidade, ");		
		sql.append(" maparegistroevasaocurso.trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte, ");		
		sql.append(" maparegistroevasaocurso.trazerAlunoTrancadoAbandonadoAnoSemestreBase, ");		
		sql.append(" maparegistroevasaocurso.considerarTrancamentoConsecutivo, ");		
		sql.append(" maparegistroevasaocurso.qtdDisciplinaReprovadas, ");
		sql.append(" maparegistroevasaocurso.qtdDiasAlunosSemAcessoAva, ");		
		sql.append(" maparegistroevasaocurso.qtdTrancamentoEmExcesso, ");		
		sql.append(" maparegistroevasaocurso.qtdMesAlunosRenovacaoSemAcessoAva, ");
		sql.append(" maparegistroevasaocurso.motivoCancelamentoTrancamento, ");
		sql.append(" maparegistroevasaocurso.justificativa, ");
		sql.append(" maparegistroevasaocurso.unidadeensinos, ");
		sql.append(" maparegistroevasaocurso.niveleducacional, ");
		sql.append(" maparegistroevasaocurso.cursos, ");
		sql.append(" maparegistroevasaocurso.turnos, ");
		sql.append(" maparegistroevasaocurso.anoRegistroEvasao, ");
		sql.append(" maparegistroevasaocurso.semestreRegistroEvasao, ");
		
		sql.append(" usuario.codigo as \"usuario.codigo\", ");
		sql.append(" usuario.nome as \"usuario.nome\", ");
		sql.append(" pessoa_usuario.codigo as \"pessoa_usuario.codigo\", ");
		sql.append(" pessoa_usuario.nome as \"pessoa_usuario.nome\", ");
		sql.append(" pessoa_usuario.cpf as \"pessoa_usuario.cpf\", ");
		sql.append(" pessoa_usuario.email as \"pessoa_usuario.email\" ");
		
		sql.append(" FROM maparegistroevasaocurso ");
		sql.append(" inner join maparegistroevasaocursomatriculaperiodo on  maparegistroevasaocursomatriculaperiodo.maparegistroevasaocurso = maparegistroevasaocurso.codigo ");
		sql.append(" inner join matriculaperiodo on  maparegistroevasaocursomatriculaperiodo.matriculaperiodo = matriculaperiodo.codigo ");
		sql.append(" inner join matricula on  matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" inner join curso on  curso.codigo = matricula.curso ");
		sql.append(" inner join usuario on  usuario.codigo = maparegistroevasaocurso.usuarioresponsavel ");
		sql.append(" left join pessoa as pessoa_usuario on  pessoa_usuario.codigo = usuario.pessoa ");
		

		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, MapaRegistroEvasaoCursoVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo));
	}
	
	private List<MapaRegistroEvasaoCursoVO> consultaRapidaPorFiltros(MapaRegistroEvasaoCursoVO obj, DataModelo dataModelo) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY maparegistroevasaocurso.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Integer consultarTotalPorFiltros(MapaRegistroEvasaoCursoVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" select count (t.codigo) as qtde from ( ");
			sqlStr.append(" select distinct  maparegistroevasaocurso.codigo ");
			sqlStr.append(" FROM maparegistroevasaocurso ");
			sqlStr.append(" inner join maparegistroevasaocursomatriculaperiodo on  maparegistroevasaocursomatriculaperiodo.maparegistroevasaocurso = maparegistroevasaocurso.codigo ");
			sqlStr.append(" inner join matriculaperiodo on  maparegistroevasaocursomatriculaperiodo.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" inner join matricula on  matriculaperiodo.matricula = matricula.matricula ");
			sqlStr.append(" inner join curso on  curso.codigo = matricula.curso ");
			sqlStr.append(" inner join usuario on  usuario.codigo = maparegistroevasaocurso.usuarioresponsavel ");
			sqlStr.append(" left join pessoa as pessoa_usuario on  pessoa_usuario.codigo = usuario.pessoa ");			
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ) as t ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}


	private void montarFiltrosParaConsulta(MapaRegistroEvasaoCursoVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(dataModelo.getDataIni()) && Uteis.isAtributoPreenchido(dataModelo.getDataFim())) {
			sqlStr.append(" and maparegistroevasaocurso.dataRegistro between ? and ? ");
			dataModelo.getListaFiltros().add(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataModelo.getDataIni()));
			dataModelo.getListaFiltros().add(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataModelo.getDataFim()));
		}
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoFiltro().getCodigo())) {
			sqlStr.append(" and matricula.unidadeensino = ? ");
			dataModelo.getListaFiltros().add(obj.getUnidadeEnsinoFiltro().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getCursoFiltro().getCodigo())) {
			sqlStr.append(" and curso.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCursoFiltro().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getTurnoFiltro().getCodigo())) {
			sqlStr.append(" and matricula.turno = ? ");
			dataModelo.getListaFiltros().add(obj.getTurnoFiltro().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getAno())) {
			sqlStr.append(" and maparegistroevasaocurso.ano = ? ");
			dataModelo.getListaFiltros().add(obj.getAno());
		}
		if (Uteis.isAtributoPreenchido(obj.getSemestre())) {
			sqlStr.append(" and maparegistroevasaocurso.semestre = ? ");
			dataModelo.getListaFiltros().add(obj.getSemestre());
		}
		if (Uteis.isAtributoPreenchido(obj.getAnoRegistroEvasao())) {
			sqlStr.append(" and maparegistroevasaocurso.anoRegistroEvasao = ? ");
			dataModelo.getListaFiltros().add(obj.getAnoRegistroEvasao());
		}
		if (Uteis.isAtributoPreenchido(obj.getSemestreRegistroEvasao())) {
			sqlStr.append(" and maparegistroevasaocurso.semestreRegistroEvasao = ? ");
			dataModelo.getListaFiltros().add(obj.getSemestreRegistroEvasao());
		}
		if (Uteis.isAtributoPreenchido(obj.getNivelEducacional())) {
			sqlStr.append(" and curso.niveleducacional = ? ");
			dataModelo.getListaFiltros().add(obj.getNivelEducacional());
		}
		if (Uteis.isAtributoPreenchido(obj.getUsuarioResponsavel().getPessoa().getNome())) {
			sqlStr.append(" and lower(sem_acentos(pessoa_usuario.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(obj.getUsuarioResponsavel().getPessoa().getNome().toLowerCase());
		}
		if (Uteis.isAtributoPreenchido(obj.getTipoTrancamentoEnum())) {
			sqlStr.append(" and maparegistroevasaocurso.tipoTrancamentoEnum = ? ");
			dataModelo.getListaFiltros().add(obj.getTipoTrancamentoEnum().name());
		}
		if (Uteis.isAtributoPreenchido(obj.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro())) {
			sqlStr.append(" and maparegistroevasaocursomatriculaperiodo.situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum = ? ");
			dataModelo.getListaFiltros().add(obj.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro().name());
		}
		if (Uteis.isAtributoPreenchido(obj.getMatriculaFiltro().getMatricula())) {
			sqlStr.append(" and matricula.matricula = ? ");
			dataModelo.getListaFiltros().add(obj.getMatriculaFiltro().getMatricula());
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public MapaRegistroEvasaoCursoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where maparegistroevasaocurso.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados (MapaRegistroEvasaoCursoVO).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private List<MapaRegistroEvasaoCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<MapaRegistroEvasaoCursoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	private MapaRegistroEvasaoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MapaRegistroEvasaoCursoVO obj = new MapaRegistroEvasaoCursoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDataRegistro(dadosSQL.getTimestamp("dataRegistro"));
		obj.setTipoTrancamentoEnum(TipoTrancamentoEnum.valueOf(dadosSQL.getString("tipoTrancamentoEnum")));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setPeriodicidade(dadosSQL.getString("periodicidade"));
		obj.setTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte(dadosSQL.getBoolean("trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte"));
		obj.setTrazerAlunoTrancadoAbandonadoAnoSemestreBase(dadosSQL.getBoolean("trazerAlunoTrancadoAbandonadoAnoSemestreBase"));
		obj.setConsiderarTrancamentoConsecutivo(dadosSQL.getBoolean("considerarTrancamentoConsecutivo"));
		obj.setQtdDisciplinaReprovadas(dadosSQL.getInt("qtdDisciplinaReprovadas"));
		obj.setQtdDiasAlunosSemAcessoAva(dadosSQL.getInt("qtdDiasAlunosSemAcessoAva"));
		obj.setQtdTrancamentoEmExcesso(dadosSQL.getInt("qtdTrancamentoEmExcesso"));
		obj.setQtdMesAlunosRenovacaoSemAcessoAva(dadosSQL.getInt("qtdMesAlunosRenovacaoSemAcessoAva"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		
		obj.getUsuarioResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getUsuarioResponsavel().setNome(dadosSQL.getString("usuario.nome"));		
		obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("motivoCancelamentoTrancamento"));
		obj.setNivelEducacional(dadosSQL.getString("niveleducacional"));
				
		if(dadosSQL.getInt("pessoa_usuario.codigo") != 0) {
			obj.getUsuarioResponsavel().getPessoa().setCodigo(dadosSQL.getInt("pessoa_usuario.codigo"));
			obj.getUsuarioResponsavel().getPessoa().setNome(dadosSQL.getString("pessoa_usuario.nome"));
			obj.getUsuarioResponsavel().getPessoa().setCPF(dadosSQL.getString("pessoa_usuario.CPF"));
			obj.getUsuarioResponsavel().getPessoa().setEmail(dadosSQL.getString("pessoa_usuario.email"));
		}
		obj.setAnoRegistroEvasao(dadosSQL.getString("anoRegistroEvasao"));
		obj.setSemestreRegistroEvasao(dadosSQL.getString("semestreRegistroEvasao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		
		obj.setUnidadeEnsinoVOs(UtilReflexao.convertePostgresArrayParaListaEntidade(new UnidadeEnsinoVO(), (SerialArray) dadosSQL.getObject("unidadeensinos")));
		obj.setCursoVOs(UtilReflexao.convertePostgresArrayParaListaEntidade(new CursoVO(), (SerialArray) dadosSQL.getObject("cursos")));
		obj.setTurnoVOs(UtilReflexao.convertePostgresArrayParaListaEntidade(new TurnoVO(), (SerialArray) dadosSQL.getObject("turnos")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO(getFacadeFactory().getMapaRegistroEvasaoCursoMatriculaPeriodoFacade().consultarPorMapaRegistroAbandonoCursoTrancamentoVO(obj, nivelMontarDados, usuario));
		return obj;
	}
	
	private void getSqlMapaRegistroEvasaoCursoCancelamentoIngressante(StringBuilder sqlStr, MapaRegistroEvasaoCursoVO mract) {
		if (mract.getTipoTrancamentoEnum().isCancelamentoIngressante()) {
			sqlStr.append("with cancelamentoIngressante as (");
			sqlStr.append("	select");
			sqlStr.append("	t.matricula, ");
			sqlStr.append("	count(*) filter (where situacao_historico in ('RF','RE') ) as total_reprovado, ");
			sqlStr.append("	count(*) as total_historico from ");
			sqlStr.append("	(");
			sqlStr.append("	select matricula.matricula, historico.codigo, historico.situacao as situacao_historico ");
			sqlStr.append("	from matriculaperiodo ");
			sqlStr.append(" INNER JOIN lateral ( ");
			sqlStr.append(" 	select min(mpIngressante.ano||'/'||mpIngressante.semestre) primeiroAno , sum(case when (mpIngressante.ano||'/'||mpIngressante.semestre) <= '").append(mract.getAno()).append("/").append(mract.getSemestre()).append("' then 1 else 0 end) as qtdPeriodo, ");
			sqlStr.append(" 	sum(case when (mpIngressante.ano||'/'||mpIngressante.semestre) > '").append(mract.getAno()).append("/").append(mract.getSemestre()).append("' then 1 else 0 end) as qtdePeriodoPosterior ");
			sqlStr.append(" 	from matriculaperiodo mpIngressante  ");
			sqlStr.append(" 	where mpIngressante.matricula = matriculaperiodo.matricula  ");			
			sqlStr.append(" ) as alunoIngressante on alunoIngressante.qtdPeriodo = 1 and coalesce(alunoIngressante.qtdePeriodoPosterior, 0) <= 1 ");
			sqlStr.append(" and alunoIngressante.primeiroAno =  '").append(mract.getAno()).append("/").append(mract.getSemestre()).append("' ");
			sqlStr.append("	inner join matricula on matriculaperiodo.matricula = matricula.matricula ");
			sqlStr.append("	inner join historico on historico.matrizcurricular = matricula.gradecurricularatual and  historico.matricula = matricula.matricula and (historico.anohistorico||historico.semestrehistorico) <=  (matriculaperiodo.ano||matriculaperiodo.semestre) ");
			sqlStr.append("	inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula and periodoletivo.periodoletivo = 1 ");
			
			sqlStr.append(" where matriculaperiodo.ano = '").append(mract.getAno()).append("' ");
			if (Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append(" AND matriculaperiodo.semestre = '").append(mract.getSemestre()).append("' ");
			}
			sqlStr.append(adicionarFiltroUnidadeEnsino(mract.getUnidadeEnsinoVOs(), "matricula.unidadeEnsino"));
			sqlStr.append(adicionarFiltroCurso(mract.getCursoVOs(), "matricula.curso"));		
			sqlStr.append(adicionarFiltroTurno(mract.getTurnoVOs(), "matricula.turno"));
			
			sqlStr.append(" AND (matricula.situacao = ('AT') or (matricula.situacao = ('TR') AND coalesce(alunoIngressante.qtdePeriodoPosterior, 0) = 1)) ");
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("AND"));
			sqlStr.append("	group by matricula.matricula, historico.codigo ) as t ");
			sqlStr.append("	group by t.matricula ");
			sqlStr.append(")");
		}
	}
	
	private void getSqlMapaRegistroEvasaoCursoCancelamentoJubilado(StringBuilder sqlStr, MapaRegistroEvasaoCursoVO mract) {
		if (mract.getTipoTrancamentoEnum().isJubilamento()) {
			sqlStr.append("with cancelamentoJubilado as ( ");
			sqlStr.append("	select matricula from (  ");
			sqlStr.append("		select matricula.matricula,");
			sqlStr.append("		matriculaperiodo.ano, matriculaperiodo.semestre, ");
			sqlStr.append("		gradecurricular.qtdeanosemestreparaintegralizacaocurso, ");
			if(Uteis.isAtributoPreenchido( mract.getAno()) && !Uteis.isAtributoPreenchido( mract.getSemestre())) {
				sqlStr.append("	somaano(matriculaperiodo.ano, (gradecurricular.qtdeanosemestreparaintegralizacaocurso -1)) as anoLimiteConclusao  ");
			}else if(Uteis.isAtributoPreenchido( mract.getAno()) && Uteis.isAtributoPreenchido( mract.getSemestre())) {
				sqlStr.append("	somasemestre(matriculaperiodo.ano, matriculaperiodo.semestre, (gradecurricular.qtdeanosemestreparaintegralizacaocurso -1)) as anoSemestreLimiteConclusao  ");
			}
			sqlStr.append("		from matricula ");
			sqlStr.append("		inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
			sqlStr.append("	 	inner join curso on curso.codigo = matricula.curso ");		
			sqlStr.append("		inner join lateral( ");
			sqlStr.append("			select pl.codigo from periodoletivo pl  where pl.gradecurricular = gradecurricular.codigo order by pl.periodoletivo desc limit 1  ");
			sqlStr.append("		) ultimoperiodo on 1=1  ");
			sqlStr.append("		inner join lateral(");
			sqlStr.append("			select mp.codigo from matriculaperiodo mp  where mp.periodoletivomatricula = ultimoperiodo.codigo  and mp.matricula = matricula.matricula ");
			sqlStr.append("			order by (coalesce(mp.ano, '0')||coalesce(mp.semestre, '0'))  limit 1  ");
			sqlStr.append("		) as primeiroMPUltimoPeriodo on 1=1  ");
			sqlStr.append("		inner join matriculaperiodo on matriculaperiodo.codigo = primeiroMPUltimoPeriodo.codigo");
			sqlStr.append("		where matricula.situacao in ('AT', 'TR') ");
			sqlStr.append(adicionarFiltroUnidadeEnsino(mract.getUnidadeEnsinoVOs(), "matricula.unidadeEnsino"));
			sqlStr.append(adicionarFiltroCurso(mract.getCursoVOs(), "matricula.curso"));		
			sqlStr.append(adicionarFiltroTurno(mract.getTurnoVOs(), "matricula.turno"));
			sqlStr.append("		and matriculaperiodo.situacaomatriculaperiodo in ('AT','TR' ,'PR', 'FI', 'FO') ");
			sqlStr.append("		and gradecurricular.regracontagemperiodoletivoenum  = 'ULTIMO_PERIODO' ");
			sqlStr.append("		and gradecurricular.qtdeanosemestreparaintegralizacaocurso > 0 ");
			sqlStr.append("		and curso.periodicidade = '").append(mract.getPeriodicidade()).append("' ");
			sqlStr.append(" 	group by matricula.matricula, matriculaperiodo.ano, matriculaperiodo.semestre, gradecurricular.qtdeanosemestreparaintegralizacaocurso ");
			if(Uteis.isAtributoPreenchido( mract.getAno()) && !Uteis.isAtributoPreenchido( mract.getSemestre())) {
				sqlStr.append(" having somaano(matriculaperiodo.ano, (gradecurricular.qtdeanosemestreparaintegralizacaocurso-1)) ");
				sqlStr.append("	< '").append(mract.getAno()).append("' ");				
			}else if(Uteis.isAtributoPreenchido( mract.getAno()) && Uteis.isAtributoPreenchido( mract.getSemestre())) {
				sqlStr.append("	having somasemestre(matriculaperiodo.ano, matriculaperiodo.semestre, (gradecurricular.qtdeanosemestreparaintegralizacaocurso-1)) ");
				sqlStr.append("	< '").append(mract.getAno()).append("/").append(mract.getSemestre()).append("' ");
			}
			sqlStr.append("	) as tup ");			
			sqlStr.append(" union all ");
			sqlStr.append("	select matricula from (  ");
			sqlStr.append("		select matricula.matricula, matricula.anoingresso, matricula.semestreingresso,  ");
			sqlStr.append("		gradecurricular.qtdeanosemestreparaintegralizacaocurso,  ");
			sqlStr.append("		(case when count(distinct matriculaperiodo.codigo) >").append(mract.getQtdTrancamentoEmExcesso()).append(" then ").append(mract.getQtdTrancamentoEmExcesso()).append(" else count(distinct matriculaperiodo.codigo) end) as qtdeMatriculaPeriodoTrancado, ");
			//sqlStr.append("		count(distinct matriculaperiodo.codigo) as qtdeMatriculaPeriodoTrancado,  ");
			if(Uteis.isAtributoPreenchido(mract.getAno()) && !Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append("	somaano(matricula.anoingresso, (gradecurricular.qtdeanosemestreparaintegralizacaocurso + ");
				sqlStr.append("	(case when count(distinct matriculaperiodo.codigo) >").append(mract.getQtdTrancamentoEmExcesso()).append(" then ").append(mract.getQtdTrancamentoEmExcesso()).append(" else count(distinct matriculaperiodo.codigo) end) ");
				sqlStr.append("	-1)) as anoLimiteConclusao  ");
			}else if(Uteis.isAtributoPreenchido(mract.getAno()) && Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append("	somasemestre(matricula.anoingresso, matricula.semestreingresso, (gradecurricular.qtdeanosemestreparaintegralizacaocurso + ");
				sqlStr.append("	(case when count(distinct matriculaperiodo.codigo) >").append(mract.getQtdTrancamentoEmExcesso()).append(" then ").append(mract.getQtdTrancamentoEmExcesso()).append(" else count(distinct matriculaperiodo.codigo) end) ");
				sqlStr.append("	 -1)) as anoSemestreLimiteConclusao  ");
			}
			sqlStr.append("		from matricula  ");
			sqlStr.append("		inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual   ");
			sqlStr.append("		inner join curso on curso.codigo = matricula.curso ");
			sqlStr.append("		left join matriculaperiodo  on matriculaperiodo.matricula = matricula.matricula ");
			sqlStr.append("		and (gradecurricular.considerarperiodotrancadoparajubilamento = false and matriculaperiodo.situacaomatriculaperiodo = 'TR')");
			sqlStr.append("		where matricula.situacao in ('AT','TR')	");
			sqlStr.append(adicionarFiltroUnidadeEnsino(mract.getUnidadeEnsinoVOs(), "matricula.unidadeEnsino"));
			sqlStr.append(adicionarFiltroCurso(mract.getCursoVOs(), "matricula.curso"));		
			sqlStr.append(adicionarFiltroTurno(mract.getTurnoVOs(), "matricula.turno"));
			sqlStr.append("		and gradecurricular.regracontagemperiodoletivoenum = 'TODOS_PERIODO_CURSADO'	");
			sqlStr.append("		and gradecurricular.qtdeanosemestreparaintegralizacaocurso > 0	");
			sqlStr.append("		and curso.periodicidade = '").append(mract.getPeriodicidade()).append("' ");
			if (mract.getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
				sqlStr.append(" and matricula.anoingresso IS NOT NULL AND matricula.anoingresso != '' AND matricula.semestreingresso IS NOT NULL AND matricula.semestreingresso != ''");
			} else {
				sqlStr.append(" and matricula.anoingresso IS NOT NULL AND matricula.anoingresso != '' ");
			}
			sqlStr.append("		group by matricula.matricula, matricula.anoingresso, matricula.semestreingresso, gradecurricular.qtdeanosemestreparaintegralizacaocurso ");
			if(Uteis.isAtributoPreenchido(mract.getAno()) && !Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append(" having somaano(matricula.anoingresso, (gradecurricular.qtdeanosemestreparaintegralizacaocurso + ");
				sqlStr.append("	(case when count(distinct matriculaperiodo.codigo) >").append(mract.getQtdTrancamentoEmExcesso()).append(" then ").append(mract.getQtdTrancamentoEmExcesso()).append(" else count(distinct matriculaperiodo.codigo) end) ");
				sqlStr.append(" -1)) ");
				sqlStr.append("	< '").append(mract.getAno()).append("' ");				
			}else if(Uteis.isAtributoPreenchido( mract.getAno()) && Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append("	having somasemestre(matricula.anoingresso, matricula.semestreingresso, (gradecurricular.qtdeanosemestreparaintegralizacaocurso + ");
				sqlStr.append("	(case when count(distinct matriculaperiodo.codigo) >").append(mract.getQtdTrancamentoEmExcesso()).append(" then ").append(mract.getQtdTrancamentoEmExcesso()).append(" else count(distinct matriculaperiodo.codigo) end) ");
				sqlStr.append("	-1)) ");
				sqlStr.append("	< '").append(mract.getAno()).append("/").append(mract.getSemestre()).append("' ");
			}
			sqlStr.append("	) as tpc ");
			sqlStr.append(")");
		}
	}
	
	private void getSqlMapaRegistroEvasaoCursoExcessoTrancamento(StringBuilder sqlStr, MapaRegistroEvasaoCursoVO mract) {
		if (mract.getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() && mract.getQtdTrancamentoEmExcesso() > 0) {
			sqlStr.append("with excessotrancamento as (");
			sqlStr.append("	SELECT ");
			sqlStr.append("	  resultado.matricula , count(resultado.total_trancamento) as qtdTrancamento");
			sqlStr.append("	FROM ( ");
			sqlStr.append("		SELECT  matricula, anosemestre, situacaomatriculaperiodo ");
			if(mract.isConsiderarTrancamentoConsecutivo()) {
				sqlStr.append("	,((count(*) FILTER  (WHERE t.situacaomatriculaperiodo = 'TR' AND t.sit_posterior = 'TR')) + (count(*) FILTER  (WHERE t.situacaomatriculaperiodo = 'TR' AND t.sit_anterior = 'TR')))   AS total_trancamento");
				sqlStr.append("	,sit_anterior,  sit_posterior");
			}else {
				sqlStr.append("	, (count (anosemestre)) AS total_trancamento ");
			}
			sqlStr.append(" FROM (  ");
			sqlStr.append("	SELECT matriculaperiodo.matricula, situacaomatriculaperiodo ");
			if(Uteis.isAtributoPreenchido( mract.getAno()) && !Uteis.isAtributoPreenchido( mract.getSemestre())) {
				sqlStr.append(" ,coalesce(ano,'0')  AS anosemestre");
				if(mract.isConsiderarTrancamentoConsecutivo()) {
					sqlStr.append(" ,lag(situacaomatriculaperiodo,1) over( PARTITION  BY  matriculaperiodo.matricula  order by  matriculaperiodo.matricula, coalesce(ano,'0')) as sit_anterior");
					sqlStr.append(" ,lead(situacaomatriculaperiodo,1) over(PARTITION  BY  matriculaperiodo.matricula  order by  matriculaperiodo.matricula, coalesce(ano,'0')) as sit_posterior");
				}
			}else if(Uteis.isAtributoPreenchido( mract.getAno()) && Uteis.isAtributoPreenchido( mract.getSemestre())) {
				sqlStr.append(" ,(coalesce(ano,'0')||coalesce(semestre,'0'))  AS anosemestre");
				if(mract.isConsiderarTrancamentoConsecutivo()) {
					sqlStr.append("	,lag(situacaomatriculaperiodo,1) over( PARTITION  BY  matriculaperiodo.matricula  order by  matriculaperiodo.matricula, (coalesce(ano,'0')||coalesce(semestre,'0'))) as sit_anterior");
					sqlStr.append("	 ,lead(situacaomatriculaperiodo,1) over(PARTITION  BY  matriculaperiodo.matricula  order by  matriculaperiodo.matricula, (coalesce(ano,'0')||coalesce(semestre,'0'))) as sit_posterior");
				}
			}
			sqlStr.append("	FROM matriculaperiodo ");
//			sqlStr.append("	inner join lateral(	");
//			sqlStr.append("	select mp.codigo from matriculaperiodo mp  ");
//			sqlStr.append("	where mp.matricula = matriculaperiodo.matricula and matriculaperiodo.ano = mp.ano and matriculaperiodo.semestre  = mp.semestre ");
//			sqlStr.append("	order by case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1 ");
//			sqlStr.append("	) as ultimaMatriculaPeriodo on ultimaMatriculaPeriodo.codigo = matriculaperiodo.codigo ");
			sqlStr.append("	inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sqlStr.append("	inner join curso on curso.codigo = matricula.curso ");
			sqlStr.append(" 	where  curso.periodicidade = '").append(mract.getPeriodicidade()).append("' ");
			if(mract.isConsiderarTrancamentoConsecutivo() && Uteis.isAtributoPreenchido(mract.getAno()) && !Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append("			and matriculaperiodo.ano in (").append(UteisTexto.obterListaDeSemestrePorQuantidade(mract.getAno(), "", mract.getQtdTrancamentoEmExcesso())).append( ")");	
			}else if(mract.isConsiderarTrancamentoConsecutivo() && Uteis.isAtributoPreenchido( mract.getAno()) && Uteis.isAtributoPreenchido( mract.getSemestre())) {
				sqlStr.append("			and matriculaperiodo.ano||matriculaperiodo.semestre in (").append(UteisTexto.obterListaDeSemestrePorQuantidade(mract.getAno(), mract.getSemestre(), mract.getQtdTrancamentoEmExcesso())).append( ")");
			}
			sqlStr.append(adicionarFiltroUnidadeEnsino(mract.getUnidadeEnsinoVOs(), "matricula.unidadeEnsino"));
			sqlStr.append(adicionarFiltroCurso(mract.getCursoVOs(), "matricula.curso"));		
			sqlStr.append(adicionarFiltroTurno(mract.getTurnoVOs(), "matricula.turno"));
			if(Uteis.isAtributoPreenchido( mract.getAno()) && !Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append("				GROUP BY  matriculaperiodo.matricula, situacaomatriculaperiodo, coalesce(ano,'0')");
				sqlStr.append("				ORDER BY  matriculaperiodo.matricula, coalesce(ano,'0')");
			}else if(Uteis.isAtributoPreenchido( mract.getAno()) && Uteis.isAtributoPreenchido( mract.getSemestre())) {
				sqlStr.append("				GROUP BY  matriculaperiodo.matricula, situacaomatriculaperiodo, (coalesce(ano,'0')||coalesce(semestre,'0'))");
				sqlStr.append("				ORDER BY  matriculaperiodo.matricula, (coalesce(ano,'0')||coalesce(semestre,'0'))");	
			}			
			sqlStr.append("			) AS t ");
			sqlStr.append("			where t.situacaomatriculaperiodo = 'TR'");
			sqlStr.append("			GROUP BY t.matricula, t.situacaomatriculaperiodo, t.anosemestre ");
			if(mract.isConsiderarTrancamentoConsecutivo()) {
				sqlStr.append(", t.sit_anterior, t.sit_posterior ");
			}
			sqlStr.append("	) AS resultado ");
			sqlStr.append("	where resultado.total_trancamento > 0");
			sqlStr.append("	GROUP BY resultado.matricula");
			sqlStr.append(")");
		}
	}
	
	
//	private void getSqlMapaRegistroEvasaoCursoIgnoraAlunosPossiveisFormandoAndDisciplinasObrigatoriasNaoComprida(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) {
//		if(mract.getTipoTrancamentoEnum().isAbandonoCurso() 
//				|| mract.getTipoTrancamentoEnum().isJubilamento()
//				|| mract.getTipoTrancamentoEnum().isTrancamento()) {
//			sqlStr.append("	left join lateral ( ");
//			sqlStr.append("		select case when count(t.codigo) > 0 then true else false end  disciplina_nao_cursada ");
//			sqlStr.append("		from ( ");
//			sqlStr.append("			select gradedisciplina.codigo  from gradedisciplina ");
//			sqlStr.append("			inner join periodoletivo pl on pl.codigo = gradedisciplina.periodoletivo ");
//			sqlStr.append("			where pl.gradecurricular = matricula_label.gradecurricularatual ");
//			sqlStr.append("			 and gradedisciplina.tipodisciplina in ('OB', 'LG') ");
//			sqlStr.append("			 and gradedisciplina.codigo not in ( ");
//			sqlStr.append("		     select gradedisciplina from historico ");
//			sqlStr.append("			 inner join gradedisciplina gd on gd.codigo = historico.gradedisciplina ");
//			sqlStr.append("			 where historico.matricula = matricula_label.matricula ");
//			sqlStr.append("			 and historico.matrizcurricular = matricula_label.gradecurricularatual ");
//			sqlStr.append("			 and gradedisciplina is not null ");
//			sqlStr.append("			 and gd.tipodisciplina in ('OB', 'LG') and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') )limit 1 ");
//			sqlStr.append("		  ) as t ");
//			sqlStr.append("	) as gradedisciplina_nao_cursada on 1=1 ");
//			sqlStr.append("																																		");
//			sqlStr.append("	left join lateral (");
//			sqlStr.append("			select");
//			sqlStr.append("				case");
//			sqlStr.append("					when (cargaHorariaOptativaCumprida >= 0");
//			sqlStr.append("						or cargaHorariaOptativaCumprida is null) then case");
//			sqlStr.append("						when (case");
//			sqlStr.append("							when cargaHorariaOptativaCumprida is null then 0");
//			sqlStr.append("							else cargaHorariaOptativaCumprida");
//			sqlStr.append("						end) >= cargaHoraria - totalcargahorariaestagio - totalcargahorariaatividadecomplementar - cargahorariaobrigatoria then true");
//			sqlStr.append("						else false");
//			sqlStr.append("					end");
//			sqlStr.append("					else true");
//			sqlStr.append("				end as cumpriu_toda_cargahoraria");
//			sqlStr.append("			from");
//			sqlStr.append("				(");
//			sqlStr.append("				select");
//			sqlStr.append("					cargahoraria,");
//			sqlStr.append("					totalcargahorariaestagio,");
//			sqlStr.append("					totalcargahorariaatividadecomplementar,");
//			sqlStr.append("					(");
//			sqlStr.append("					select sum(cargahoraria)");
//			sqlStr.append("					from gradedisciplina");
//			sqlStr.append("					inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo");
//			sqlStr.append("					where periodoletivo.gradecurricular = gradecurricular.codigo");
//			sqlStr.append("						and gradedisciplina.tipodisciplina not in ('OP', 'LO') )as cargahorariaobrigatoria,");
//			sqlStr.append("					(");
//			sqlStr.append("					select");
//			sqlStr.append("						sum(cargahorariadisciplina)");
//			sqlStr.append("					from");
//			sqlStr.append("						(");
//			sqlStr.append("						select");
//			sqlStr.append("							historico.cargahorariadisciplina");
//			sqlStr.append("						from");
//			sqlStr.append("							historico");
//			sqlStr.append("						inner join gradedisciplina on");
//			sqlStr.append("							gradedisciplina.codigo = historico.gradedisciplina");
//			sqlStr.append("						where");
//			sqlStr.append("							matricula = matricula_label.matricula");
//			sqlStr.append("							and historico.matrizcurricular = matricula_label.gradecurricularAtual");
//			sqlStr.append("							and gradedisciplina.tipodisciplina in ('OP', 'LO')");
//			sqlStr.append("								and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CS', 'CH', 'AE', 'AB', 'CE')");
//			sqlStr.append("						union all");
//			sqlStr.append("							select");
//			sqlStr.append("								historico.cargahorariadisciplina");
//			sqlStr.append("							from");
//			sqlStr.append("								historico");
//			sqlStr.append("							inner join gradecurriculargrupooptativadisciplina on");
//			sqlStr.append("								gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina");
//			sqlStr.append("							where");
//			sqlStr.append("								matricula = matricula_label.matricula");
//			sqlStr.append("								and historico.matrizcurricular = matricula_label.gradecurricularAtual");
//			sqlStr.append("								and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CS', 'CH', 'AE') )as t ) as cargaHorariaOptativaCumprida");
//			sqlStr.append("				from");
//			sqlStr.append("					gradecurricular");
//			sqlStr.append("				where");
//			sqlStr.append("					codigo = matricula_label.gradecurricularatual ) as o ");
//			sqlStr.append("");
//			sqlStr.append("	) as cumpriu_toda_cargahoraria on 1=1");
//		}
//	}
	
	
	private void getSqlMapaRegistroEvasaoCurso(StringBuilder sqlStr, MapaRegistroEvasaoCursoVO mract) {
		sqlStr.append(" select matriculaperiodo_label.codigo, matriculaperiodo_label.data, matriculaperiodo_label.dataVencimentoMatriculaEspecifico, matriculaperiodo_label.dataBaseGeracaoParcelas, matriculaperiodo_label.situacao, matriculaperiodo_label.matricula, matriculaperiodo_label.alunoTransferidoUnidade, matriculaperiodo_label.financeiroManual, matriculaperiodo_label.transferenciaEntrada ,  ");
		sqlStr.append(" matriculaperiodo_label.situacaoMatriculaPeriodo, matriculaperiodo_label.semestre, matriculaperiodo_label.ano, matriculaperiodo_label.carneEntregue, matriculaperiodo_label.responsavelrenovacaomatricula,  ");
		sqlStr.append(" matriculaperiodo_label.unidadeEnsinoCurso, matriculaperiodo_label.processoMatricula, matriculaperiodo_label.gradecurricular, GradeCurricular.nome AS \"gradeCurricular.nome\", matriculaperiodo_label.bolsista, ");
		sqlStr.append(" matriculaperiodo_label.turma, Turma.identificadorturma as \"turma.identificadorturma\", Turma.observacao as \"turma.observacao\", ");
		sqlStr.append(" matriculaperiodo_label.matriculaEspecial, matriculaperiodo_label.planofinanceirocurso, matriculaperiodo_label.condicaoPagamentoplanofinanceirocurso, planofinanceirocurso.descricao as \"planofinanceirocurso.descricao\", matriculaperiodo_label.motivoliberacaopgtomatricula, ");
		sqlStr.append(" matriculaperiodo_label.periodoLetivoMatricula, PeriodoLetivo.descricao as \"PeriodoLetivo.descricao\", PeriodoLetivo.periodoLetivo as \"PeriodoLetivo.periodoLetivo\",  ");
		sqlStr.append(" matricula_label.matricula as \"Matricula.matricula\", matricula_label.gradeCurricularAtual as \"Matricula.gradeCurricularAtual\", matricula_label.data as \"Matricula.data\", matricula_label.matriculaSuspensa as \"Matricula.matriculaSuspensa\", matricula_label.updated as \"Matricula.updated\", ");
		sqlStr.append(" matricula_label.turno as \"Matricula.turno\", Turno.nome as \"Turno.nome\", ");
		sqlStr.append(" matricula_label.situacao as \"Matricula.situacao\", matricula_label.situacaoFinanceira as \"Matricula.situacaoFinanceira\", matricula_label.alunoConcluiuDisciplinasRegulares as \"Matricula.alunoConcluiuDisciplinasRegulares\",");
		sqlStr.append(" Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\",  Pessoa.registroAcademico as \"Pessoa.registroAcademico\" ,");
		sqlStr.append(" Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", ");
		sqlStr.append(" UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", matricula_label.naoEnviarMensagemCobranca as \"Matricula.naoEnviarMensagemCobranca\", ");
		sqlStr.append(" turnoPeriodo.codigo AS \"turnoPeriodo.codigo\", turnoPeriodo.nome AS \"turnoPeriodo.nome\", ");
		sqlStr.append(" gradeCurricularAtual.codigo AS \"gradeCurricularAtual.codigo\", gradeCurricularAtual.nome AS \"gradeCurricularAtual.nome\", ");
		sqlStr.append(" ProcessoMatricula.codigo AS \"ProcessoMatricula.codigo\", ProcessoMatricula.descricao AS \"ProcessoMatricula.descricao\", ");
		sqlStr.append(" condicaopagamentoplanofinanceirocurso.descricao AS \"condicaopagamentoplanofinanceirocurso.descricao\", matricula_label.canceladofinanceiro, matriculaperiodo_label.categoriaCondicaoPagamento, ");
		sqlStr.append(" uec.codigo AS \"uec.codigo\", uec.unidadeEnsino AS \"uec.unidadeEnsino\", uec.curso AS \"uec.curso\",  matricula_label.bloqueioPorSolicitacaoLiberacaoMatricula as \"Matricula.bloqueioPorSolicitacaoLiberacaoMatricula\", matricula_label.motivoMatriculaBloqueada as \"Matricula.motivoMatriculaBloqueada\"  ");
		if (mract.getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento()) {
			sqlStr.append(" from excessotrancamento ");		
			sqlStr.append(" inner join Matricula as matricula_label on matricula_label.matricula = excessotrancamento.matricula and excessotrancamento.qtdTrancamento >= " + mract.getQtdTrancamentoEmExcesso() + " ");
			sqlStr.append(" inner join MatriculaPeriodo as matriculaperiodo_label on matriculaperiodo_label.matricula = matricula_label.matricula ");
			if(!mract.isConsiderarTrancamentoConsecutivo()) {
				sqlStr.append(" inner join lateral(");
				sqlStr.append("	 select mp.codigo from matriculaperiodo mp  where mp.matricula = matriculaperiodo_label.matricula  and mp.situacaoMatriculaPeriodo  = 'TR' ");
				sqlStr.append("	 order by (coalesce(mp.ano, '0')|| coalesce(mp.semestre, '0')) desc, mp.codigo desc  limit 1 ");
				sqlStr.append(" ) as ultimaMatriculaPeriodo on ultimaMatriculaPeriodo.codigo = matriculaperiodo_label.codigo ");
			}
		} else if (mract.getTipoTrancamentoEnum().isJubilamento()) {
			sqlStr.append(" from cancelamentoJubilado ");		
			sqlStr.append(" inner join Matricula AS matricula_label ON (matricula_label.matricula = cancelamentoJubilado.matricula) ");
			sqlStr.append(" inner join MatriculaPeriodo as matriculaperiodo_label on matriculaperiodo_label.matricula = matricula_label.matricula ");
		} else if (mract.getTipoTrancamentoEnum().isCancelamentoIngressante()) {
			sqlStr.append(" from cancelamentoIngressante ");		
			sqlStr.append(" inner join Matricula AS matricula_label ON matricula_label.matricula = cancelamentoIngressante.matricula ");
			if (mract.getQtdDisciplinaReprovadas() > 0 ) {
				sqlStr.append(" and cancelamentoIngressante.total_reprovado > 0 and cancelamentoIngressante.total_reprovado >= ").append(mract.getQtdDisciplinaReprovadas()).append(" ");
			}else if (mract.getQtdDisciplinaReprovadas().equals(0)) {
				sqlStr.append(" and cancelamentoIngressante.total_reprovado > 0 and  cancelamentoIngressante.total_reprovado = cancelamentoIngressante.total_historico ");
			}
			sqlStr.append(" inner join MatriculaPeriodo as matriculaperiodo_label on matriculaperiodo_label.matricula = matricula_label.matricula ");
		} else {
			sqlStr.append(" FROM MatriculaPeriodo  as matriculaperiodo_label ");		
			sqlStr.append(" INNER JOIN Matricula as matricula_label ON (matriculaperiodo_label.matricula = matricula_label.matricula) ");
		}
		sqlStr.append(" INNER JOIN Pessoa ON (matricula_label.aluno = pessoa.codigo) ");
		sqlStr.append(" INNER JOIN usuario ON (usuario.pessoa = pessoa.codigo) ");
		sqlStr.append(" INNER JOIN UnidadeEnsino ON (matricula_label.unidadeEnsino = unidadeEnsino.codigo) ");
		sqlStr.append(" INNER JOIN Curso ON (matricula_label.curso = curso.codigo) ");
		sqlStr.append(" INNER JOIN PeriodoLetivo ON (matriculaperiodo_label.periodoLetivoMatricula = PeriodoLetivo.codigo) ");
		sqlStr.append(" INNER JOIN GradeCurricular ON (matriculaperiodo_label.gradecurricular = GradeCurricular.codigo) ");
		sqlStr.append(" INNER JOIN GradeCurricular as gradeCurricularAtual ON (gradeCurricularAtual.codigo = matricula_label.gradeCurricularAtual) ");
		sqlStr.append(" LEFT JOIN turma ON matriculaperiodo_label.turma = turma.codigo  ");
		sqlStr.append(" INNER JOIN Turno ON matricula_label.turno = turno.codigo  ");
		sqlStr.append(" LEFT JOIN planofinanceirocurso ON matriculaperiodo_label.planofinanceirocurso = planofinanceirocurso.codigo  ");
		sqlStr.append(" LEFT JOIN condicaopagamentoplanofinanceirocurso ON matriculaperiodo_label.condicaopagamentoplanofinanceirocurso = condicaopagamentoplanofinanceirocurso.codigo  ");
		sqlStr.append(" INNER JOIN unidadeEnsinoCurso uec ON uec.codigo = matriculaperiodo_label.unidadeEnsinoCurso");
		sqlStr.append(" INNER JOIN Turno turnoPeriodo ON turnoPeriodo.codigo = uec.turno ");
		sqlStr.append(" LEFT JOIN ProcessoMatricula ON ProcessoMatricula.codigo = matriculaperiodo_label.processoMatricula ");
		if (mract.getTipoTrancamentoEnum().isJubilamento()) {
			sqlStr.append(" INNER JOIN LATERAL ( SELECT CASE WHEN ((cargaHorariaDisciplinaObrigatorioCumprida < cargaHorariaDisciplinaObrigatorioExigida) OR (cargaHorariaDisciplinaOptativaCumprida < cargaHorariaDisciplinaOptativaExigida)) THEN TRUE ELSE FALSE END permitirRealizarJubilamento FROM matriculaintegralizacaocurricular(matricula_label.matricula) ) matriculaintegralizacaocurricular ON TRUE ");
		}
	}
	
	

	@Override
	public void consultarPorUnidadeEnsinoCursoTurnoMapaRegistroAbandonoCursoTrancamento(MapaRegistroEvasaoCursoVO mract, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		getSqlMapaRegistroEvasaoCursoCancelamentoIngressante(sqlStr, mract);
		getSqlMapaRegistroEvasaoCursoCancelamentoJubilado(sqlStr, mract);
		getSqlMapaRegistroEvasaoCursoExcessoTrancamento(sqlStr, mract);
		getSqlMapaRegistroEvasaoCurso(sqlStr, mract);
		//getSqlMapaRegistroEvasaoCursoIgnoraAlunosPossiveisFormandoAndDisciplinasObrigatoriasNaoComprida(mract, sqlStr);
		
		sqlStr.append(" WHERE 1=1 ");
		adicionarFiltroSituacao(mract, sqlStr);
		adicionarFiltroAnoSemestre(mract, sqlStr);
		sqlStr.append(" and curso.periodicidade = '").append(mract.getPeriodicidade()).append("' ");
		if(Uteis.isAtributoPreenchido(mract.getNivelEducacional())){
			sqlStr.append(" and curso.niveleducacional = '").append(mract.getNivelEducacional()).append("' ");
		}
		sqlStr.append(adicionarFiltroUnidadeEnsino(mract.getUnidadeEnsinoVOs(), "unidadeEnsino.codigo"));
		sqlStr.append(adicionarFiltroCurso(mract.getCursoVOs(), "curso.codigo"));		
		sqlStr.append(adicionarFiltroTurno(mract.getTurnoVOs(), "turno.codigo"));
		if(mract.getTipoTrancamentoEnum().isAbandonoCurso() 
				|| mract.getTipoTrancamentoEnum().isJubilamento()
				|| mract.getTipoTrancamentoEnum().isTrancamento()) {
			sqlStr.append(" AND not exists ( ");
			sqlStr.append(" select mp2.matricula from matriculaperiodo mp2 ");
			sqlStr.append(" where mp2.matricula = matriculaperiodo_label.matricula ");
			sqlStr.append(" and (mp2.ano||'/'||mp2.semestre) > (matriculaperiodo_label.ano||'/'||matriculaperiodo_label.semestre)) ");
			
			//sqlStr.append(" and ((gradedisciplina_nao_cursada.disciplina_nao_cursada is true) or (cumpriu_toda_cargahoraria.cumpriu_toda_cargahoraria  is false )) ");
		}
		adicionarFiltroCancelamentoIngressante(mract, sqlStr);
		adicionarFiltroCancelamentoJubilado(mract, sqlStr);
//		adicionarFiltroExcessoTrancamento(mract, sqlStr);
		adicionarFiltroRenovacaoAutomaticaTrancamento(mract, sqlStr);
		adicionarFiltroTrazerAlunoTrancadoAbandonadoAnoSemestreBase(mract, sqlStr);
		adicionarFiltroTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte(mract, sqlStr);
		System.out.println(sqlStr.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		montarDadosMapaRegistroEvasaoCursoMatriculaPeriodoVO(mract, tabelaResultado);
	}
	

	private void adicionarFiltroSituacao(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) throws Exception {
		if(mract.getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() ) {
			sqlStr.append(" and  matriculaperiodo_label.situacaoMatriculaPeriodo  = 'TR'  ");	
			sqlStr.append(" and  matricula_label.situacao  = 'TR'  ");
		}else if(mract.getTipoTrancamentoEnum().isCancelamentoIngressante() || mract.getTipoTrancamentoEnum().isJubilamento()) {
			sqlStr.append(" and  matricula_label.situacao  in ('AT', 'TR')  ");
		}else {
			sqlStr.append(" and  matricula_label.situacao  = 'AT'  ");
		}
	}
	
	private void adicionarFiltroAnoSemestre(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) throws Exception {
		if(mract.getTipoTrancamentoEnum().isJubilamento()){
			if (Uteis.isAtributoPreenchido(mract.getAno()) && !Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append(" AND matriculaperiodo_label.ano = '").append(mract.getAno()).append("' ");
			}else {
				sqlStr.append(" AND (matriculaperiodo_label.ano||'/'||matriculaperiodo_label.semestre ) ='").append(mract.getAno()).append("/").append(mract.getSemestre()).append("' ");
			}	
		}else if((!mract.getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() && !mract.getTipoTrancamentoEnum().isJubilamento()) 
				|| (mract.getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() && mract.isConsiderarTrancamentoConsecutivo())) {
			sqlStr.append(" AND matriculaperiodo_label.ano = '").append(mract.getAno()).append("' ");
			if (Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append(" AND matriculaperiodo_label.semestre = '").append(mract.getSemestre()).append("' ");
			}
		}else if(mract.getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() && !mract.isConsiderarTrancamentoConsecutivo()) {
			sqlStr.append(" AND not exists ( ");
			sqlStr.append(" select mp2.matricula from matriculaperiodo mp2 ");
			sqlStr.append(" where mp2.matricula = matriculaperiodo_label.matricula ");
			sqlStr.append(" and (mp2.ano||'/'||mp2.semestre) > ('").append(mract.getAno()).append("/").append(mract.getSemestre()).append("'))");
		}
	}
	
	private void adicionarFiltroCancelamentoIngressante(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) throws Exception {
		if (mract.getTipoTrancamentoEnum().isCancelamentoIngressante() && mract.getQtdDiasAlunosSemAcessoAva() > 0) {
			sqlStr.append("AND usuario.dataUltimoAcessoBlackboard <= '").append(Uteis.getDataBD2359(getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(new Date(), (mract.getQtdDiasAlunosSemAcessoAva() * -1), 0, false, false, ConsiderarFeriadoEnum.ACADEMICO))).append("'");
		}
//		if (mract.getTipoTrancamentoEnum().isCancelamentoIngressante() && mract.getQtdDisciplinaReprovadas() > 0 ) {
//			sqlStr.append("	and exists ( select m.matricula from cancelamentoIngressante m where m.matricula = matriculaperiodo_label.matricula and m.total_reprovado > 0 and m.total_reprovado >=").append(mract.getQtdDisciplinaReprovadas()).append(")");
//		}else if (mract.getTipoTrancamentoEnum().isCancelamentoIngressante() && mract.getQtdDisciplinaReprovadas().equals(0)) {
//			sqlStr.append("	and exists ( select m.matricula from cancelamentoIngressante m where m.matricula = matriculaperiodo_label.matricula and m.total_reprovado > 0 and  m.total_reprovado = m.total_historico )");	
//		}
	}
	
	private void adicionarFiltroCancelamentoJubilado(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) {
		if (mract.getTipoTrancamentoEnum().isJubilamento() ) {
			sqlStr.append(" AND matriculaintegralizacaocurricular.permitirRealizarJubilamento ");
//			sqlStr.append("	and exists ( select m.matricula from cancelamentoJubilado m where m.matricula = matriculaperiodo_label.matricula ) ");
		}
	}
	
	private void adicionarFiltroExcessoTrancamento(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) {
		if (mract.getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() && mract.getQtdTrancamentoEmExcesso() > 0) {
			sqlStr.append("	and exists ( select m.matricula from excessotrancamento m where m.matricula = matriculaperiodo_label.matricula and  m.qtdTrancamento >=").append(mract.getQtdTrancamentoEmExcesso()).append(")");
		}
	}
	
	private void adicionarFiltroRenovacaoAutomaticaTrancamento(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) throws Exception {
		if (mract.getTipoTrancamentoEnum().isRenovacaoAutomatica()){
			sqlStr.append("	and pessoa.renovacaoautomatica = true  ");
			sqlStr.append(" and not exists ( ");
			sqlStr.append(" select mp2.matricula from matriculaperiodo mp2  where mp2.matricula = matriculaperiodo_label.matricula ");
			sqlStr.append(" and mp2.situacaomatriculaperiodo in ('AT', 'PR') ");
			sqlStr.append(" and (mp2.ano||'/'||mp2.semestre) > (matriculaperiodo_label.ano||'/'||matriculaperiodo_label.semestre)) ");
			if(mract.getQtdMesAlunosRenovacaoSemAcessoAva() > 0) {
				sqlStr.append("and (usuario.dataUltimoAcessoBlackboard + interval '").append(mract.getQtdMesAlunosRenovacaoSemAcessoAva()).append(" day') < '").append(Uteis.getDataBD2359(new Date())).append("'");
			}
		}
	}
	
	private void adicionarFiltroTrazerAlunoTrancadoAbandonadoAnoSemestreBase(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) {
		if((mract.getTipoTrancamentoEnum().isAbandonoCurso() || mract.getTipoTrancamentoEnum().isTrancamento())
				&& mract.isTrazerAlunoTrancadoAbandonadoAnoSemestreBase()) {		
			sqlStr.append(" union ");
			getSqlMapaRegistroEvasaoCurso(sqlStr, mract);
			sqlStr.append(" WHERE matriculaperiodo_label.ano = '").append(mract.getAno()).append("' ");
			if (Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append(" AND matriculaperiodo_label.semestre = '").append(mract.getSemestre()).append("' ");
			}
			sqlStr.append(" AND matricula_label.situacao = 'TR' ");
			sqlStr.append(" AND curso.periodicidade = '").append(mract.getPeriodicidade()).append("' ");
			sqlStr.append(adicionarFiltroUnidadeEnsino(mract.getUnidadeEnsinoVOs(), "unidadeEnsino.codigo"));
			sqlStr.append(adicionarFiltroCurso(mract.getCursoVOs(), "curso.codigo"));		
			sqlStr.append(adicionarFiltroTurno(mract.getTurnoVOs(), "turno.codigo"));
			
			if(mract.getTipoTrancamentoEnum().isAbandonoCurso() || mract.getTipoTrancamentoEnum().isTrancamento()) {
				sqlStr.append("AND not exists ( ");
				sqlStr.append("select mp2.matricula from matriculaperiodo mp2 ");
				sqlStr.append("where mp2.matricula = matriculaperiodo_label.matricula ");
				sqlStr.append("and (mp2.ano||'/'||mp2.semestre) > (matriculaperiodo_label.ano||'/'||matriculaperiodo_label.semestre)) ");
			}
		}
	}

	private void adicionarFiltroTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte(MapaRegistroEvasaoCursoVO mract, StringBuilder sqlStr) {		
		if((mract.getTipoTrancamentoEnum().isAbandonoCurso() || mract.getTipoTrancamentoEnum().isTrancamento())
				&& mract.isTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte()) {
			sqlStr.append("union ");
			getSqlMapaRegistroEvasaoCurso(sqlStr, mract);
			sqlStr.append(" WHERE matriculaperiodo_label.ano = '").append(mract.getAno()).append("' ");
			if (Uteis.isAtributoPreenchido(mract.getSemestre())) {
				sqlStr.append(" AND matriculaperiodo_label.semestre = '").append(mract.getSemestre()).append("' ");
			}
			sqlStr.append("AND matricula_label.situacao  = 'AT' ");
			sqlStr.append(" AND curso.periodicidade = '").append(mract.getPeriodicidade()).append("' ");
			sqlStr.append(adicionarFiltroUnidadeEnsino(mract.getUnidadeEnsinoVOs(), "unidadeEnsino.codigo"));
			sqlStr.append(adicionarFiltroCurso(mract.getCursoVOs(), "curso.codigo"));		
			sqlStr.append(adicionarFiltroTurno(mract.getTurnoVOs(), "turno.codigo"));
			
			sqlStr.append("AND exists ( ");
			sqlStr.append("select m2.matricula from matriculaperiodo mp2 ");
			sqlStr.append("inner join matricula m2 on m2.matricula = mp2.matricula ");
			sqlStr.append("where m2.matricula = matriculaperiodo_label.matricula ");
			sqlStr.append("and (m2.canceladoFinanceiro or not exists(SELECT contareceber.codigo FROM contareceber WHERE contareceber.matriculaaluno = m2.matricula and contareceber.situacao = 'AR' and contareceber.tipoOrigem not in ('RE'))) ");
			sqlStr.append("and mp2.situacaomatriculaperiodo in ('PR') ");
			sqlStr.append("and (mp2.ano||'/'||mp2.semestre) > (matriculaperiodo_label.ano||'/'||matriculaperiodo_label.semestre)) ");
		}
	}

	

	private String adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String campo) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" and ").append(campo).append(" in (0 ");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sqlStr.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sqlStr.append(") ");
		return sqlStr.toString();
	}

	private String adicionarFiltroCurso(List<CursoVO> cursoVOs, String campo) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		sql.append(" and ").append(campo).append(" in (0 ");
		for (CursoVO cursoVO : cursoVOs) {
			if (cursoVO.getFiltrarCursoVO()) {
				sql.append(", ").append(cursoVO.getCodigo());
				encontrado = true;
			}
		}
		if (!encontrado) {
			return "";
		}
		sql.append(") ");
		return sql.toString();
	}

	private String adicionarFiltroTurno(List<TurnoVO> turnoVOs, String campo) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		sql.append(" and ").append(campo).append(" in (0");
		for (TurnoVO turnoVO : turnoVOs) {
			if (turnoVO.getFiltrarTurnoVO()) {
				sql.append(", ").append(turnoVO.getCodigo());
				encontrado = true;
			}
		}
		if (!encontrado) {
			return "";
		}
		sql.append(") ");
		return sql.toString();
	}
	
	public void montarDadosMapaRegistroEvasaoCursoMatriculaPeriodoVO(MapaRegistroEvasaoCursoVO mract, SqlRowSet tabelaResultado) throws Exception {
		mract.getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().clear();
		 List<MapaRegistroEvasaoCursoMatriculaPeriodoVO> lista = new ArrayList<MapaRegistroEvasaoCursoMatriculaPeriodoVO>();
		while (tabelaResultado.next()) {
			MapaRegistroEvasaoCursoMatriculaPeriodoVO mractmp = new MapaRegistroEvasaoCursoMatriculaPeriodoVO();
			mractmp.setMapaRegistroEvasaoCursoVO(mract);
			mractmp.setMatriculaPeriodoVO(montarDadosBasicoMatriculaPeriodo(tabelaResultado));
			lista.add(mractmp);
		}
		mract.setMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO(lista.stream()
				.sorted(Comparator.comparing(p-> ((MapaRegistroEvasaoCursoMatriculaPeriodoVO) p).getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getNome())
				.thenComparing(p-> ((MapaRegistroEvasaoCursoMatriculaPeriodoVO) p).getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNome())				
				.thenComparing(p-> ((MapaRegistroEvasaoCursoMatriculaPeriodoVO) p).getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNome()))
				.collect(Collectors.toList()));
	}
	
	
	public MatriculaPeriodoVO montarDadosBasicoMatriculaPeriodo(SqlRowSet dadosSQL) throws Exception {
		MatriculaPeriodoVO obj =  new MatriculaPeriodoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDataVencimentoMatriculaEspecifico(dadosSQL.getDate("dataVencimentoMatriculaEspecifico"));
		obj.setDataBaseGeracaoParcelas(dadosSQL.getDate("dataBaseGeracaoParcelas"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoMatriculaPeriodo(dadosSQL.getString("situacaoMatriculaPeriodo"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setMatriculaEspecial(dadosSQL.getBoolean("matriculaEspecial"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setUnidadeEnsinoCurso(dadosSQL.getInt("unidadeEnsinoCurso"));
		obj.setProcessoMatricula(dadosSQL.getInt("processoMatricula"));
		obj.getGradeCurricular().setCodigo(dadosSQL.getInt("gradecurricular"));
		obj.getGradeCurricular().setNome(dadosSQL.getString("gradeCurricular.nome"));
		obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
		obj.getPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("planofinanceirocurso"));
		obj.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("condicaoPagamentoplanofinanceirocurso"));
		obj.getCondicaoPagamentoPlanoFinanceiroCurso().setDescricao(dadosSQL.getString("condicaoPagamentoplanofinanceirocurso.descricao"));
		obj.setCategoriaCondicaoPagamento(dadosSQL.getString("categoriaCondicaoPagamento"));
		obj.setCarneEntregue(dadosSQL.getBoolean("carneEntregue"));
		obj.setAlunoTransferidoUnidade(dadosSQL.getBoolean("alunoTransferidoUnidade"));
		obj.setMotivoLiberacaoPgtoMatricula(dadosSQL.getString("motivoliberacaopgtomatricula"));
		obj.setTranferenciaEntrada(dadosSQL.getInt("transferenciaEntrada"));		
		obj.setBolsista(dadosSQL.getBoolean("bolsista"));
		obj.setFinanceiroManual(dadosSQL.getBoolean("financeiroManual"));
		
		obj.setNovoObj(false);
		obj.setNivelMontarDados(NivelMontarDados.BASICO);

		// Turma
		obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
		obj.getTurma().setObservacao(dadosSQL.getString("turma.observacao"));
		// PlanoFinanceiroCurso
		obj.getPlanoFinanceiroCurso().setDescricao(dadosSQL.getString("planofinanceirocurso.descricao"));
		// Periodo Letivo
		obj.getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoLetivoMatricula"));
		obj.getPeridoLetivo().setDescricao(dadosSQL.getString("PeriodoLetivo.descricao"));
		obj.getPeridoLetivo().setPeriodoLetivo(dadosSQL.getInt("PeriodoLetivo.periodoletivo"));
		// Dados Processo Matrícula
		obj.getProcessoMatriculaVO().setCodigo(dadosSQL.getInt("ProcessoMatricula.codigo"));
		obj.getProcessoMatriculaVO().setDescricao(dadosSQL.getString("ProcessoMatricula.descricao"));
		// Dados da Matrícula
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("Matricula.matricula"));

		GradeCurricularVO gradeCurricularAtual = new GradeCurricularVO();
		gradeCurricularAtual.setCodigo(dadosSQL.getInt("Matricula.gradeCurricularAtual"));
		obj.getMatriculaVO().setGradeCurricularAtual(gradeCurricularAtual);
		obj.getMatriculaVO().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("gradecurricularatual.codigo"));
		obj.getMatriculaVO().getGradeCurricularAtual().setNome(dadosSQL.getString("gradeCurricularatual.nome"));
		obj.getMatriculaVO().setData(dadosSQL.getDate("Matricula.data"));
		obj.getMatriculaVO().setMatriculaSuspensa(dadosSQL.getBoolean("Matricula.matriculaSuspensa"));
		obj.getMatriculaVO().setSituacao(dadosSQL.getString("Matricula.situacao"));
		obj.getMatriculaVO().setSituacaoFinanceira(dadosSQL.getString("Matricula.situacaoFinanceira"));
		obj.getMatriculaVO().setAlunoConcluiuDisciplinasRegulares(dadosSQL.getBoolean("Matricula.alunoConcluiuDisciplinasRegulares"));
		obj.getMatriculaVO().setUpdated(dadosSQL.getTimestamp("Matricula.updated"));
		obj.getMatriculaVO().setNaoEnviarMensagemCobranca(dadosSQL.getBoolean("Matricula.naoEnviarMensagemCobranca"));
		obj.getMatriculaVO().setCanceladoFinanceiro(dadosSQL.getBoolean("canceladoFinanceiro"));
		// Dados do Turno
		obj.getMatriculaVO().getTurno().setCodigo(dadosSQL.getInt("Matricula.turno"));
		obj.getMatriculaVO().getTurno().setNome(dadosSQL.getString("Turno.nome"));
		// Dados do Aluno
		obj.getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getMatriculaVO().getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getMatriculaVO().getAluno().setRegistroAcademico(dadosSQL.getString("Pessoa.registroAcademico"));
		obj.getMatriculaVO().getAluno().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Curso
		obj.getMatriculaVO().getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getMatriculaVO().getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getMatriculaVO().getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getMatriculaVO().getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Unidade
		obj.getMatriculaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
		obj.getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
		obj.getMatriculaVO().getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setUnidadeEnsinoCurso(dadosSQL.getInt("unidadeEnsinoCurso"));
		obj.getUnidadeEnsinoCursoVO().setCodigo(dadosSQL.getInt("uec.codigo"));		
		obj.getUnidadeEnsinoCursoVO().getCurso().setCodigo(dadosSQL.getInt("uec.curso"));
		obj.getUnidadeEnsinoCursoVO().getTurno().setCodigo(dadosSQL.getInt("turnoPeriodo.codigo"));
		obj.getUnidadeEnsinoCursoVO().getTurno().setNome(dadosSQL.getString("turnoPeriodo.nome"));
		obj.getResponsavelRenovacaoMatricula().setCodigo(dadosSQL.getInt("responsavelrenovacaomatricula"));		
		obj.getMatriculaVO().setPendenciaLiberacaoMatriculaVOs(getFacadeFactory().getPendenciaLiberacaoMatriculaInterfaceFacade().consultarPorMatricula(obj.getMatricula()));
		obj.getMatriculaVO().getPendenciaLiberacaoMatriculaVOs().stream().forEach(plMatricula ->{
			if(plMatricula.getMotivoSolicitacao().equals(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_LIBERACAO_MATRICULA_APOS_X_MODULOS)) {
				obj.getMatriculaVO().setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica(Boolean.TRUE);
			}
			else if(plMatricula.getMotivoSolicitacao().equals(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_APROVACAO_LIBERACAO_FINANCEIRA)) {
				obj.getMatriculaVO().setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira(Boolean.TRUE);
			}
		});
		obj.getMatriculaVO().setBloqueioPorSolicitacaoLiberacaoMatricula(dadosSQL.getBoolean("Matricula.bloqueioPorSolicitacaoLiberacaoMatricula"));
		obj.getMatriculaVO().setMotivoMatriculaBloqueada(dadosSQL.getString("Matricula.motivoMatriculaBloqueada"));
		return obj;
	}
	
	public static String designIReportRelatorioExcel() {		
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "MapaRegistroEvasaoCursosExcel" + ".jrxml");
	}
	public static String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}
}
