package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.academico.enumeradores.SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.MapaRegistroEvasaoCursoVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MapaRegistroEvasaoCursoMatriculaPeriodoInterfaceFacade;
import webservice.boletoonline.itau.comuns.TokenVO;

@Repository
@Scope("singleton")
@Lazy
public class MapaRegistroEvasaoCursoMatriculaPeriodo extends ControleAcesso implements MapaRegistroEvasaoCursoMatriculaPeriodoInterfaceFacade {
	
	
	
	private static final long serialVersionUID = 3313952858036508715L;
	private static String idEntidade = "MapaRegistroEvasaoCurso";

	public static String getIdEntidade() {
		return MapaRegistroEvasaoCursoMatriculaPeriodo.idEntidade;
	}
	
	private class ThreadMapaRegistroEvasaoCursoMatriculaPeriodo implements Runnable {
		private MapaRegistroEvasaoCursoMatriculaPeriodoVO  mrecmp;
		private UsuarioVO usuarioVO;
		
		public ThreadMapaRegistroEvasaoCursoMatriculaPeriodo(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, UsuarioVO usuarioVO) {
			super();
			this.mrecmp = mrecmp;
			this.usuarioVO = usuarioVO;
		}
		public void run() {
			alterar(mrecmp, false, usuarioVO);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(MapaRegistroEvasaoCursoVO mrec, boolean verificarAcesso, UsuarioVO usuarioVO) {
		for (MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp : mrec.getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO()) {
			if (mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getAlunoSelecionado()) {
				mrecmp.setMapaRegistroEvasaoCursoVO(mrec);
				incluir(mrecmp, verificarAcesso, usuarioVO);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarOperacaoMapaRegistroEvasaoCurso(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, boolean verificarAcesso, Map<Integer, ConfiguracaoGeralSistemaVO> configuracaoGeralSistemaVOs, Map<Integer, ConfiguracaoFinanceiroVO> configuracaoFinanceiroVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			if (mrecmp.getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isAbandonoCurso() || mrecmp.getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isTrancamento() || mrecmp.getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isJubilamento()) {
				getFacadeFactory().getTrancamentoFacade().persistirTrancamentoMapaRegistroAbandonoCursoTrancamentoIndividualmente(mrecmp, configuracaoGeralSistemaVOs.get(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), configuracaoFinanceiroVOs.get(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), usuarioVO);
			} else if (mrecmp.getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoIngressante() || mrecmp.getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento()) {
				getFacadeFactory().getCancelamentoFacade().persistirCancelamentoMapaRegistroAbandonoCursoTrancamentoIndividualmente(mrecmp, configuracaoGeralSistemaVOs.get(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), configuracaoFinanceiroVOs.get(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), usuarioVO);
			} else if (mrecmp.getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isRenovacaoAutomatica()) {
				mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setRenovacaoAutomatica(Boolean.FALSE);
				getFacadeFactory().getPessoaFacade().executarAtualizacaoDadosPessoaRenovacaoAutomatica(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getAluno(), usuarioVO);
			}
			mrecmp.setSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.PROCESSADO);
			mrecmp.setErro(null);
			alterar(mrecmp, verificarAcesso, usuarioVO);
		} catch (Exception e) {
			mrecmp.setSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.ERRO);
			mrecmp.setErro(e.getMessage());
			mrecmp.getMatriculaPeriodoVO().getMatriculaVO().setAlunoSelecionado(false);
			Thread thread = new Thread( new ThreadMapaRegistroEvasaoCursoMatriculaPeriodo(mrecmp, usuarioVO));
			thread.start();
			throw e;
		} 
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEstornoMapaRegistroEvasaoCursoMatriculaPeriodo(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			mrecmp.getCancelamentoVO().setResponsavelEstorno(usuarioVO);			
			mrecmp.getCancelamentoVO().setDataEstorno(new Date());			
			mrecmp.getCancelamentoVO().setSituacao("ES");			
			getFacadeFactory().getCancelamentoFacade().executarEstorno(mrecmp.getCancelamentoVO(), usuarioVO);
			mrecmp.setSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.ESTORNADO);
			mrecmp.setErro(null);
			alterar(mrecmp, verificarAcesso, usuarioVO);
			if (Uteis.isAtributoPreenchido(mrecmp.getMatriculaPeriodoVO())) {
				mrecmp.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(mrecmp.getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, usuarioVO));
			}
		} catch (Exception e) {
			mrecmp.setSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.ERRO);
			mrecmp.setErro("Estorno -"+e.getMessage());
			Thread thread = new Thread( new ThreadMapaRegistroEvasaoCursoMatriculaPeriodo(mrecmp, usuarioVO));
			thread.start();
			throw e;
		} 
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MapaRegistroEvasaoCursoMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			MapaRegistroEvasaoCursoMatriculaPeriodo.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "maparegistroevasaocursomatriculaperiodo", new AtributoPersistencia()
					.add("maparegistroevasaocurso", obj.getMapaRegistroEvasaoCursoVO())
					.add("situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum", obj.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum())
					.add("erro", obj.getErro())
					.add("matriculaPeriodo", obj.getMatriculaPeriodoVO())
					.add("trancamento", obj.getTrancamentoVO())
					.add("cancelamento", obj.getCancelamentoVO())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MapaRegistroEvasaoCursoMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			MapaRegistroEvasaoCursoMatriculaPeriodo.alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "maparegistroevasaocursomatriculaperiodo", new AtributoPersistencia()
					.add("maparegistroevasaocurso", obj.getMapaRegistroEvasaoCursoVO())
					.add("situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum", obj.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum())
					.add("erro", obj.getErro())
					.add("matriculaPeriodo", obj.getMatriculaPeriodoVO())
					.add("trancamento", obj.getTrancamentoVO())
					.add("cancelamento", obj.getCancelamentoVO())
					, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<MapaRegistroEvasaoCursoMatriculaPeriodoVO> consultarPorMapaRegistroAbandonoCursoTrancamentoVO(MapaRegistroEvasaoCursoVO obj, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsulta();
			sqlStr.append(" where maparegistroevasaocursomatriculaperiodo.maparegistroevasaocurso = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
			List<MapaRegistroEvasaoCursoMatriculaPeriodoVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				MapaRegistroEvasaoCursoMatriculaPeriodoVO mractmp = new MapaRegistroEvasaoCursoMatriculaPeriodoVO();
				montarDados(mractmp, tabelaResultado, nivelMontarDados, usuario);
				mractmp.setMapaRegistroEvasaoCursoVO(obj);
				vetResultado.add(mractmp);
			}
			return vetResultado.stream()
			.sorted(Comparator.comparing(p-> ((MapaRegistroEvasaoCursoMatriculaPeriodoVO) p).getMatriculaPeriodoVO().getMatriculaVO().getAlunoSelecionado()).reversed()
			.thenComparing(p-> ((MapaRegistroEvasaoCursoMatriculaPeriodoVO) p).getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getNome())				
			.thenComparing(p-> ((MapaRegistroEvasaoCursoMatriculaPeriodoVO) p).getMatriculaPeriodoVO().getMatriculaVO().getCurso().getNome())				
			.thenComparing(p-> ((MapaRegistroEvasaoCursoMatriculaPeriodoVO) p).getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNome()))
			.collect(Collectors.toList());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsulta() {
		StringBuilder sql = new StringBuilder("select maparegistroevasaocursomatriculaperiodo.codigo, ");
		sql.append("maparegistroevasaocursomatriculaperiodo.situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum, ");
		sql.append("maparegistroevasaocursomatriculaperiodo.erro, ");
		
		sql.append("matricula.matricula as \"matricula.matricula\", ");
		sql.append("matricula.situacao  as \"matricula.situacao\", ");
		
		sql.append("gradeCurricularAtual.codigo AS \"gradeCurricularAtual.codigo\", ");
		sql.append("gradeCurricularAtual.nome AS \"gradeCurricularAtual.nome\", ");
		
		sql.append("matriculaperiodo.codigo as \"matriculaperiodo.codigo\", ");
		sql.append("matriculaperiodo.situacaomatriculaperiodo  as \"matriculaperiodo.situacaomatriculaperiodo\", ");
		sql.append("matriculaperiodo.ano as \"matriculaperiodo.ano\", ");
		sql.append("matriculaperiodo.semestre  as \"matriculaperiodo.semestre\", ");
		
		sql.append("pessoa.codigo as \"pessoa.codigo\", ");
		sql.append("pessoa.nome as \"pessoa.nome\", ");
		sql.append("pessoa.cpf as \"pessoa.cpf\", ");
		sql.append("pessoa.email as \"pessoa.email\", ");
		
		sql.append("unidadeensino.codigo as \"unidadeensino.codigo\", ");
		sql.append("unidadeensino.nome as \"unidadeensino.nome\", ");
		sql.append("unidadeensino.razaosocial  as \"unidadeensino.razaosocial\", ");
		sql.append("unidadeensino.cnpj  as \"unidadeensino.cnpj\", ");
		
		sql.append("curso.codigo as \"curso.codigo\", ");
		sql.append("curso.nome as \"curso.nome\", ");
		sql.append("curso.periodicidade as \"curso.periodicidade\", ");
		
		sql.append("turno.codigo as \"turno.codigo\", ");
		sql.append("turno.nome as \"turno.nome\", ");
		
		sql.append("turma.codigo as \"turma.codigo\", ");
		sql.append("turma.identificadorturma  as \"turma.identificadorturma\", ");
		
		sql.append("periodoletivo.codigo as \"periodoletivo.codigo\", ");
		sql.append("periodoletivo.descricao  as \"periodoletivo.descricao\", ");
		sql.append("periodoletivo.periodoletivo  as \"periodoletivo.periodoletivo\", ");
		
		sql.append(" uec.codigo AS \"uec.codigo\", uec.unidadeEnsino AS \"uec.unidadeEnsino\", uec.curso AS \"uec.curso\",  ");
		
		sql.append("trancamento.codigo as \"trancamento.codigo\", ");		
		sql.append("trancamento.justificativa  as \"trancamento.justificativa\", ");
		sql.append("trancamento.data  as \"trancamento.data\", ");
		sql.append("trancamento.descricao  as \"trancamento.descricao\", ");
		sql.append("trancamento.situacao  as \"trancamento.situacao\", ");
		sql.append("trancamento.ano  as \"trancamento.ano\", ");
		sql.append("trancamento.semestre  as \"trancamento.semestre\", ");
		sql.append("trancamento.tipotrancamento  as \"trancamento.tipotrancamento\", ");
		
		sql.append("cancelamento.codigo as \"cancelamento.codigo\", ");		
		sql.append("cancelamento.justificativa  as \"cancelamento.justificativa\", ");
		sql.append("cancelamento.data  as \"cancelamento.data\", ");
		sql.append("cancelamento.descricao  as \"cancelamento.descricao\", ");
		sql.append("cancelamento.situacao  as \"cancelamento.situacao\", ");
		
		sql.append("mct_trancamento.codigo  as \"mct_trancamento.codigo\", ");
		sql.append("mct_trancamento.nome  as \"mct_trancamento.nome\", ");
		sql.append("mct_cancelamento.codigo  as \"mct_cancelamento.codigo\", ");
		sql.append("mct_cancelamento.nome  as \"mct_cancelamento.nome\" ");
		
		sql.append(" from maparegistroevasaocursomatriculaperiodo  ");
		sql.append(" inner join matriculaperiodo  on matriculaperiodo.codigo = maparegistroevasaocursomatriculaperiodo.matriculaperiodo");
		sql.append(" inner join turma  on turma.codigo = matriculaperiodo.turma ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula  ");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula");
		sql.append(" inner join GradeCurricular as gradeCurricularAtual ON (gradeCurricularAtual.codigo = matricula.gradeCurricularAtual) ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" inner join turno on turno.codigo = matricula.turno ");
		sql.append(" inner join unidadeEnsinoCurso uec ON uec.codigo = matriculaperiodo.unidadeEnsinoCurso");
		sql.append(" left join trancamento on trancamento.codigo  = maparegistroevasaocursomatriculaperiodo.trancamento");
		sql.append(" left join motivocancelamentotrancamento mct_trancamento on mct_trancamento.codigo  = trancamento.motivocancelamentotrancamento ");
		sql.append(" left join cancelamento on cancelamento.codigo  = maparegistroevasaocursomatriculaperiodo.cancelamento ");
		sql.append(" left join motivocancelamentotrancamento mct_cancelamento on mct_cancelamento.codigo  = cancelamento.motivocancelamentotrancamento");
		return sql;
	}
	
	private void montarDados(MapaRegistroEvasaoCursoMatriculaPeriodoVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setErro(dadosSQL.getString("erro"));
		obj.setSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.valueOf(dadosSQL.getString("situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum")));		
		
		obj.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("matriculaperiodo.codigo"));
		obj.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(dadosSQL.getString("matriculaperiodo.situacaomatriculaperiodo"));
		obj.getMatriculaPeriodoVO().setAno(dadosSQL.getString("matriculaperiodo.ano"));
		obj.getMatriculaPeriodoVO().setSemestre(dadosSQL.getString("matriculaperiodo.semestre"));
		obj.getMatriculaPeriodoVO().setMatricula(dadosSQL.getString("matricula.matricula"));
		
		obj.getMatriculaPeriodoVO().getMatriculaVO().setAlunoSelecionado(obj.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().isAguardandoProcessamento() ? true :false);
		obj.getMatriculaPeriodoVO().getMatriculaVO().setMatricula(dadosSQL.getString("matricula.matricula"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().setSituacao(dadosSQL.getString("matricula.situacao"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setEmail(dadosSQL.getString("pessoa.email"));
		
		obj.getMatriculaPeriodoVO().getMatriculaVO().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("gradecurricularatual.codigo"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getGradeCurricularAtual().setNome(dadosSQL.getString("gradeCurricularatual.nome"));
		
		obj.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino.razaosocial"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setCNPJ(dadosSQL.getString("unidadeensino.cnpj"));
		
		obj.getMatriculaPeriodoVO().getMatriculaVO().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getCurso().setPeriodicidade(dadosSQL.getString("curso.periodicidade"));
		
		obj.getMatriculaPeriodoVO().getMatriculaVO().getTurno().setCodigo(dadosSQL.getInt("turno.codigo"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getTurno().setNome(dadosSQL.getString("turno.nome"));
		
		obj.getMatriculaPeriodoVO().getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getMatriculaPeriodoVO().getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
		
		obj.getMatriculaPeriodoVO().getPeriodoLetivo().setCodigo(dadosSQL.getInt("periodoletivo.codigo"));
		obj.getMatriculaPeriodoVO().getPeriodoLetivo().setDescricao(dadosSQL.getString("periodoletivo.descricao"));
		obj.getMatriculaPeriodoVO().getPeriodoLetivo().setPeriodoLetivo(dadosSQL.getInt("periodoletivo.periodoletivo"));
		
		obj.getMatriculaPeriodoVO().setUnidadeEnsinoCurso(dadosSQL.getInt("uec.codigo"));
		obj.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().setCodigo(dadosSQL.getInt("uec.codigo"));
		obj.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().setUnidadeEnsino(dadosSQL.getInt("uec.unidadeEnsino"));
		obj.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getCurso().setCodigo(dadosSQL.getInt("uec.curso"));
		
		if(dadosSQL.getInt("trancamento.codigo") != 0 ) {
			obj.getTrancamentoVO().setCodigo(dadosSQL.getInt("trancamento.codigo"));
			obj.getTrancamentoVO().setJustificativa(dadosSQL.getString("trancamento.justificativa"));
			obj.getTrancamentoVO().setData(dadosSQL.getTimestamp("trancamento.data"));
			obj.getTrancamentoVO().setDescricao(dadosSQL.getString("trancamento.descricao"));
			obj.getTrancamentoVO().setSituacao(dadosSQL.getString("trancamento.situacao"));
			obj.getTrancamentoVO().setAno(dadosSQL.getString("trancamento.ano"));
			obj.getTrancamentoVO().setSemestre(dadosSQL.getString("trancamento.semestre"));
			obj.getTrancamentoVO().setTipoTrancamento(dadosSQL.getString("trancamento.tipotrancamento"));
			obj.getTrancamentoVO().getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("mct_trancamento.codigo"));		
			obj.getTrancamentoVO().getMotivoCancelamentoTrancamento().setNome(dadosSQL.getString("mct_trancamento.nome"));
			obj.getTrancamentoVO().setMatricula(obj.getMatriculaPeriodoVO().getMatriculaVO());
		}
		if(dadosSQL.getInt("cancelamento.codigo") != 0 ) {
			obj.getCancelamentoVO().setCodigo(dadosSQL.getInt("cancelamento.codigo"));
			obj.getCancelamentoVO().setJustificativa(dadosSQL.getString("cancelamento.justificativa"));
			obj.getCancelamentoVO().setData(dadosSQL.getTimestamp("cancelamento.data"));
			obj.getCancelamentoVO().setDescricao(dadosSQL.getString("cancelamento.descricao"));
			obj.getCancelamentoVO().setSituacao(dadosSQL.getString("cancelamento.situacao"));
			obj.getCancelamentoVO().getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("mct_cancelamento.codigo"));		
			obj.getCancelamentoVO().getMotivoCancelamentoTrancamento().setNome(dadosSQL.getString("mct_cancelamento.nome"));
			obj.getCancelamentoVO().setMatricula(obj.getMatriculaPeriodoVO().getMatriculaVO());
		}
	}

}
