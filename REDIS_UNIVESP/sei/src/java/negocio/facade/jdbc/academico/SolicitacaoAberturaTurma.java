package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.SolicitacaoAberturaTurmaDisciplinaVO;
import negocio.comuns.academico.SolicitacaoAberturaTurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.SituacaoSolicitacaoAberturaTurmaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.SolicitacaoAberturaTurmaInterfaceFacade;

@Repository
@Lazy
public class SolicitacaoAberturaTurma extends ControleAcesso implements SolicitacaoAberturaTurmaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1766705702584196321L;
	private static String idEntidade = "SolicitarAberturaTurma";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persitir(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {

		if (solicitacaoAberturaTurmaVO.isNovoObj()) {

			incluir(solicitacaoAberturaTurmaVO, usuarioVO);
		} else {

			alterar(solicitacaoAberturaTurmaVO, usuarioVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			solicitacaoAberturaTurmaVO.setDataSolicitacao(new Date());
			solicitacaoAberturaTurmaVO.getUsuarioSolicitacao().setCodigo(usuarioVO.getCodigo());
			solicitacaoAberturaTurmaVO.getUsuarioSolicitacao().setNome(usuarioVO.getNome());
			validarDados(solicitacaoAberturaTurmaVO);
			incluir(getIdEntidade(), true, usuarioVO);
			solicitacaoAberturaTurmaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO SolicitacaoAberturaTurma (");
					sql.append("quantidadeModulo, dataSolicitacao, turma, situacaoSolicitacaoAberturaTurma, unidadeEnsino, usuarioSolicitacao ");
					sql.append(") VALUES (?, ?, ?, ?, ?, ?) returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, solicitacaoAberturaTurmaVO.getQuantidadeModulo());
					ps.setDate(x++, Uteis.getDataJDBC(solicitacaoAberturaTurmaVO.getDataSolicitacao()));
					ps.setInt(x++, solicitacaoAberturaTurmaVO.getTurma().getCodigo());
					ps.setString(x++, solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma().name());
					ps.setInt(x++, solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCodigo());
					ps.setInt(x++, solicitacaoAberturaTurmaVO.getUsuarioSolicitacao().getCodigo());
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {

						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getSolicitacaoAberturaTurmaDisciplinaFacade().incluirSolicitacaoAberturaTurmaDisciplina(solicitacaoAberturaTurmaVO);
			solicitacaoAberturaTurmaVO.setNovoObj(false);
		} catch (Exception e) {
			solicitacaoAberturaTurmaVO.setNovoObj(true);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		solicitacaoAberturaTurmaVO.setDataAlteracao(new Date());
		solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setCodigo(usuarioVO.getCodigo());
		solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setNome(usuarioVO.getNome());
		validarDados(solicitacaoAberturaTurmaVO);
		alterar(getIdEntidade(), true, usuarioVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE SolicitacaoAberturaTurma SET ");
				sql.append("quantidadeModulo = ? , dataAlteracao = ?, turma = ?, situacaoSolicitacaoAberturaTurma = ?, unidadeEnsino=?, usuarioAlteracao = ? ");
				sql.append("WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, solicitacaoAberturaTurmaVO.getQuantidadeModulo());
				ps.setDate(x++, Uteis.getDataJDBC(solicitacaoAberturaTurmaVO.getDataAlteracao()));
				ps.setInt(x++, solicitacaoAberturaTurmaVO.getTurma().getCodigo());
				ps.setString(x++, solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma().name());
				ps.setInt(x++, solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCodigo());
				ps.setInt(x++, solicitacaoAberturaTurmaVO.getUsuarioAlteracao().getCodigo());
				ps.setInt(x++, solicitacaoAberturaTurmaVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(solicitacaoAberturaTurmaVO, usuarioVO);
			return;
		}
		getFacadeFactory().getSolicitacaoAberturaTurmaDisciplinaFacade().alterarSolicitacaoAberturaTurmaDisciplina(solicitacaoAberturaTurmaVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void registrarNaoAutorizacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnumTmp = solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma();
		try {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum.NAO_AUTORIZADO);
			solicitacaoAberturaTurmaVO.setDataAlteracao(new Date());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setCodigo(usuarioVO.getCodigo());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setNome(usuarioVO.getNome());
			alterar(solicitacaoAberturaTurmaVO, usuarioVO);
		} catch (Exception e) {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(situacaoSolicitacaoAberturaTurmaEnumTmp);
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void registrarAutorizacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnumTmp = solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma();
		try {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum.AUTORIZADO);
			solicitacaoAberturaTurmaVO.setDataAlteracao(new Date());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setCodigo(usuarioVO.getCodigo());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setNome(usuarioVO.getNome());
			alterar(solicitacaoAberturaTurmaVO, usuarioVO);
		} catch (Exception e) {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(situacaoSolicitacaoAberturaTurmaEnumTmp);
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void registrarRevisaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnumTmp = solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma();
		try {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum.EM_REVISAO);
			solicitacaoAberturaTurmaVO.setDataAlteracao(new Date());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setCodigo(usuarioVO.getCodigo());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setNome(usuarioVO.getNome());
			alterar(solicitacaoAberturaTurmaVO, usuarioVO);
		} catch (Exception e) {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(situacaoSolicitacaoAberturaTurmaEnumTmp);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void registrarRevisaoRealizadaAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnumTmp = solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma();
		try {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO);
			solicitacaoAberturaTurmaVO.setDataAlteracao(new Date());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setCodigo(usuarioVO.getCodigo());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setNome(usuarioVO.getNome());
			for (SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
				solicitacaoAberturaTurmaDisciplinaVO.setRevisar(false);
				solicitacaoAberturaTurmaDisciplinaVO.setMotivoRevisao("");
			}
			alterar(solicitacaoAberturaTurmaVO, usuarioVO);
		} catch (Exception e) {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(situacaoSolicitacaoAberturaTurmaEnumTmp);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void registrarFinalizacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnumTmp = solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma();
		try {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum.FINALIZADO);
			solicitacaoAberturaTurmaVO.setDataAlteracao(new Date());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setCodigo(usuarioVO.getCodigo());
			solicitacaoAberturaTurmaVO.getUsuarioAlteracao().setNome(usuarioVO.getNome());
			alterar(solicitacaoAberturaTurmaVO, usuarioVO);
			for(SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO:solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()){
				solicitacaoAberturaTurmaDisciplinaVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(solicitacaoAberturaTurmaDisciplinaVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			}
			realizarCriacaoHorarioTurma(solicitacaoAberturaTurmaVO, usuarioVO);
		} catch (Exception e) {
			solicitacaoAberturaTurmaVO.setSituacaoSolicitacaoAberturaTurma(situacaoSolicitacaoAberturaTurmaEnumTmp);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarCriacaoHorarioTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		HorarioTurmaVO horarioTurmaVO = null;
		if (solicitacaoAberturaTurmaVO.getTurma().getCurso().getNivelEducacional().equals("PO") || solicitacaoAberturaTurmaVO.getTurma().getCurso().getNivelEducacional().equals("EX")) {
			horarioTurmaVO = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(solicitacaoAberturaTurmaVO.getTurma().getCodigo(), "", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			horarioTurmaVO.setAnoVigente("");
			horarioTurmaVO.setSemestreVigente("");
		} else {
			horarioTurmaVO = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(solicitacaoAberturaTurmaVO.getTurma().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			horarioTurmaVO.setAnoVigente(Uteis.getAnoDataAtual());
			horarioTurmaVO.setSemestreVigente(Uteis.getSemestreAtual());
		}
		if (horarioTurmaVO.getCodigo() > 0) {
			throw new Exception("Já existe uma programação de aula para esta turma. Este cadastro é exclusivo para turmas sem aulas programadas");
		}
		solicitacaoAberturaTurmaVO.getTurma().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(solicitacaoAberturaTurmaVO.getTurma().getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		horarioTurmaVO.setTurma(solicitacaoAberturaTurmaVO.getTurma());
		horarioTurmaVO.setTurno(solicitacaoAberturaTurmaVO.getTurma().getTurno());
		horarioTurmaVO.getDisciplina().setCodigo(1);
		horarioTurmaVO.getProfessor().setCodigo(1);
		horarioTurmaVO.getProfessor().setNome("Professor Teste");
		realizarCriacaoHorarioTurmaDia(solicitacaoAberturaTurmaVO, horarioTurmaVO, usuarioVO);
		horarioTurmaVO.montarDadosHorarioTurma();
		getFacadeFactory().getHorarioTurmaFacade().incluir(horarioTurmaVO, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarCriacaoHorarioTurmaDia(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, HorarioTurmaVO horarioTurmaVO, UsuarioVO usuarioVO) throws Exception {
		HorarioTurmaDiaVO horarioTurmaDiaVO = null;

		for (SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
			for (Date dataIni = solicitacaoAberturaTurmaDisciplinaVO.getDataInicio(); Uteis.getDataJDBC(dataIni).compareTo(Uteis.getDataJDBC(solicitacaoAberturaTurmaDisciplinaVO.getDataTermino())) <= 0; dataIni = Uteis.obterDataAvancada(dataIni, 1)) {
				horarioTurmaDiaVO = new HorarioTurmaDiaVO();
				horarioTurmaDiaVO.setData(dataIni);
				getFacadeFactory().getHorarioTurmaDiaFacade().montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO, horarioTurmaDiaVO);
				for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
					horarioTurmaDiaItemVO.getDisciplinaVO().setCodigo(solicitacaoAberturaTurmaDisciplinaVO.getDisciplina().getCodigo());
					horarioTurmaDiaItemVO.getDisciplinaVO().setNome(solicitacaoAberturaTurmaDisciplinaVO.getDisciplina().getNome());
					horarioTurmaDiaItemVO.getFuncionarioVO().setCodigo(solicitacaoAberturaTurmaDisciplinaVO.getProfessor().getCodigo());
					horarioTurmaDiaItemVO.getFuncionarioVO().setNome(solicitacaoAberturaTurmaDisciplinaVO.getProfessor().getNome());
				}
				horarioTurmaVO.adicinarHorarioTurmaPorDiaPorDia(horarioTurmaDiaVO, true, null);
			}
		}
	}

	private String getSqlConsultaBasica() {
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" SolicitacaoAberturaTurma.*, turma.identificadorTurma as \"turma.identificadorTurma\",  unidadeEnsino.nome as \"unidadeEnsino.nome\", curso.nome as \"curso.nome\", turno.nome as \"turno.nome\", ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nivelEducacional as  \"curso.nivelEducacional\", turno.codigo as \"turno.codigo\", ");
		sql.append(" usuarioSolicitacao.nome as \"usuarioSolicitacao.nome\", usuarioAlteracao.nome as \"usuarioAlteracao.nome\", ");
		sql.append(" pessoaSolicitacao.nome as \"pessoaSolicitacao.nome\", pessoaSolicitacao.codigo as \"pessoaSolicitacao.codigo\", pessoaSolicitacao.email as \"pessoaSolicitacao.email\" ");
		sql.append(" FROM SolicitacaoAberturaTurma ");
		sql.append(" inner join UnidadeEnsino on UnidadeEnsino.codigo = SolicitacaoAberturaTurma.UnidadeEnsino ");
		sql.append(" inner join Turma on Turma.codigo = SolicitacaoAberturaTurma.turma ");
		sql.append(" inner join UnidadeEnsinoCurso on UnidadeEnsinoCurso.curso = Turma.curso and UnidadeEnsinoCurso.turno = Turma.turno and UnidadeEnsinoCurso.unidadeEnsino = Turma.UnidadeEnsino ");
		sql.append(" inner join Curso on Curso.codigo = UnidadeEnsinoCurso.curso ");
		sql.append(" inner join Turno on Turno.codigo = UnidadeEnsinoCurso.turno ");
		sql.append(" inner join Usuario as usuarioSolicitacao on usuarioSolicitacao.codigo = SolicitacaoAberturaTurma.usuarioSolicitacao ");
		sql.append(" left join Pessoa as pessoaSolicitacao on pessoaSolicitacao.codigo = usuarioSolicitacao.pessoa ");
		sql.append(" left join Usuario as usuarioAlteracao on usuarioAlteracao.codigo = SolicitacaoAberturaTurma.usuarioAlteracao ");
		return sql.toString();
	}

	@Override
	public List<SolicitacaoAberturaTurmaVO> consultar(Integer unidadeEnsino, Integer unidadeEnsinoCurso, String turma, SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnum, Integer limite, Integer offset, boolean verificarPermissao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaBasica());
		sql.append(" WHERE 0 = 0 ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso);
		}
		if (turma != null && !turma.trim().isEmpty()) {
			sql.append(" and upper(sem_acentos(turma.identificadorTurma)) like upper(sem_acentos('%").append(turma).append("%'))");
		}
		if (situacaoSolicitacaoAberturaTurmaEnum != null && !situacaoSolicitacaoAberturaTurmaEnum.equals(SituacaoSolicitacaoAberturaTurmaEnum.TODAS)) {
			sql.append(" and situacaoSolicitacaoAberturaTurma = '").append(situacaoSolicitacaoAberturaTurmaEnum.name()).append("' ");
		}
		sql.append(" order by dataSolicitacao ");
		if (limite != null && limite > 0) {
			sql.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	@Override
	public Integer consultarTotalRegistro(Integer unidadeEnsino, Integer unidadeEnsinoCurso, String turma, SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnum) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT COUNT(SolicitacaoAberturaTurma.codigo) as qtde from SolicitacaoAberturaTurma ");
		sql.append(" inner join turma on turma.codigo = SolicitacaoAberturaTurma.turma ");
		sql.append(" inner join UnidadeEnsinoCurso on UnidadeEnsinoCurso.curso = Turma.curso and UnidadeEnsinoCurso.turno = Turma.turno and UnidadeEnsinoCurso.unidadeEnsino = Turma.UnidadeEnsino ");
		sql.append(" WHERE 0 = 0 ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and SolicitacaoAberturaTurma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso);
		}
		if (turma != null && !turma.trim().isEmpty()) {
			sql.append(" and upper(sem_acentos(turma.identificadorTurma)) like upper(sem_acentos('%").append(turma).append("%'))");
		}
		if (situacaoSolicitacaoAberturaTurmaEnum != null && !situacaoSolicitacaoAberturaTurmaEnum.equals(SituacaoSolicitacaoAberturaTurmaEnum.TODAS)) {
			sql.append(" and situacaoSolicitacaoAberturaTurma = '").append(situacaoSolicitacaoAberturaTurmaEnum.name()).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	private List<SolicitacaoAberturaTurmaVO> montarDadosConsulta(SqlRowSet rs) {
		List<SolicitacaoAberturaTurmaVO> solicitacaoAberturaTurmaVOs = new ArrayList<SolicitacaoAberturaTurmaVO>(0);
		while (rs.next()) {
			solicitacaoAberturaTurmaVOs.add(montarDados(rs));
		}
		return solicitacaoAberturaTurmaVOs;
	}

	private SolicitacaoAberturaTurmaVO montarDados(SqlRowSet rs) {
		SolicitacaoAberturaTurmaVO solicitacaoAberturaTurma = new SolicitacaoAberturaTurmaVO();
		solicitacaoAberturaTurma.setNovoObj(false);
		solicitacaoAberturaTurma.setCodigo(rs.getInt("codigo"));
		solicitacaoAberturaTurma.setDataAlteracao(rs.getDate("dataAlteracao"));
		solicitacaoAberturaTurma.setDataSolicitacao(rs.getDate("dataSolicitacao"));
		solicitacaoAberturaTurma.getTurma().setCodigo(rs.getInt("turma"));
		solicitacaoAberturaTurma.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		solicitacaoAberturaTurma.setQuantidadeModulo(rs.getInt("quantidadeModulo"));
		solicitacaoAberturaTurma.setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum.valueOf(rs.getString("SituacaoSolicitacaoAberturaTurma")));
		solicitacaoAberturaTurma.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		solicitacaoAberturaTurma.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
		solicitacaoAberturaTurma.getTurma().getCurso().setCodigo(rs.getInt("curso.codigo"));
		solicitacaoAberturaTurma.getTurma().getCurso().setNome(rs.getString("curso.nome"));
		solicitacaoAberturaTurma.getTurma().getCurso().setNivelEducacional(rs.getString("curso.nivelEducacional"));
		solicitacaoAberturaTurma.getTurma().getTurno().setCodigo(rs.getInt("turno.codigo"));
		solicitacaoAberturaTurma.getTurma().getTurno().setNome(rs.getString("turno.nome"));
		solicitacaoAberturaTurma.getUsuarioAlteracao().setCodigo(rs.getInt("usuarioAlteracao"));
		solicitacaoAberturaTurma.getUsuarioAlteracao().setNome(rs.getString("usuarioAlteracao.nome"));
		solicitacaoAberturaTurma.getUsuarioSolicitacao().setCodigo(rs.getInt("usuarioSolicitacao"));
		solicitacaoAberturaTurma.getUsuarioSolicitacao().setNome(rs.getString("usuarioSolicitacao.nome"));
		solicitacaoAberturaTurma.getUsuarioSolicitacao().getPessoa().setCodigo(rs.getInt("pessoaSolicitacao.codigo"));
		solicitacaoAberturaTurma.getUsuarioSolicitacao().getPessoa().setNome(rs.getString("pessoaSolicitacao.nome"));
		solicitacaoAberturaTurma.getUsuarioSolicitacao().getPessoa().setEmail(rs.getString("pessoaSolicitacao.email"));
		return solicitacaoAberturaTurma;
	}

	@Override
	public void validarDados(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws ConsistirException, Exception {
		if (!solicitacaoAberturaTurmaVO.isValidarDados()) {
			return;
		}
		ConsistirException consistirException = new ConsistirException();
		if (solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCodigo() == 0) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_unidadeEnsino"));
		}
		if (solicitacaoAberturaTurmaVO.getTurma().getCodigo() == 0) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_turma"));
		}
		if (solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs().isEmpty()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_disciplinas"));
		}
		if (solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.EM_REVISAO)) {
			boolean existeRevisao = false;
			for (SolicitacaoAberturaTurmaDisciplinaVO obj : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
				if (obj.getRevisar()) {
					existeRevisao = true;
					break;
				}
			}
			if (!existeRevisao) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_informarModuloRevisar"));
			}
		}
		if (solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO)) {
			Map<Date, SolicitacaoAberturaTurmaDisciplinaVO> mapSolicitacaoAberturaTurmaDisciplina = new HashMap<Date, SolicitacaoAberturaTurmaDisciplinaVO>(0);
			for (SolicitacaoAberturaTurmaDisciplinaVO obj : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
				if (obj.getDataInicio() != null && obj.getDataTermino() != null) {
					for (Date dataIni = obj.getDataInicio(); Uteis.getDataJDBC(dataIni).compareTo(Uteis.getDataJDBC(obj.getDataTermino())) <= 0; dataIni = Uteis.obterDataAvancada(dataIni, 1)) {
						if (mapSolicitacaoAberturaTurmaDisciplina.containsKey(dataIni)) {
							SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaConflito = mapSolicitacaoAberturaTurmaDisciplina.get(dataIni);
							consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_conflitoDataAula").replace("{0}", obj.getNumeroModulo().toString()).replace("{1}", solicitacaoAberturaTurmaDisciplinaConflito.getNumeroModulo().toString()));
							break;
						} else {
							mapSolicitacaoAberturaTurmaDisciplina.put(dataIni, obj);
						}
					}
				}
			}
		}

		if (solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.FINALIZADO)) {
			Map<Integer, SolicitacaoAberturaTurmaDisciplinaVO> mapSolicitacaoAberturaTurmaDisciplina = new HashMap<Integer, SolicitacaoAberturaTurmaDisciplinaVO>(0);
			for (SolicitacaoAberturaTurmaDisciplinaVO obj : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
				if (obj.getDisciplina().getCodigo() > 0) {
					if (mapSolicitacaoAberturaTurmaDisciplina.containsKey(obj.getDisciplina().getCodigo())) {
						SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaConflito = mapSolicitacaoAberturaTurmaDisciplina.get(obj.getDisciplina().getCodigo());
						consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_conflitoDisciplinaAula").replace("{0}", obj.getNumeroModulo().toString()).replace("{1}", solicitacaoAberturaTurmaDisciplinaConflito.getNumeroModulo().toString()));
					} else {
						mapSolicitacaoAberturaTurmaDisciplina.put(obj.getDisciplina().getCodigo(), obj);
					}
				}
			}
		}

		if (consistirException != null && !consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		} else {
			consistirException = null;
		}

	}

	@Override
	public void adicionarSolicitacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws Exception {
		Ordenacao.ordenarLista(solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs(), "numeroModulo");
		if (solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs().size() > solicitacaoAberturaTurmaVO.getQuantidadeModulo()) {
			for (int x = solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs().size(); x > solicitacaoAberturaTurmaVO.getQuantidadeModulo(); x--) {
				solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs().remove(x - 1);
			}
		}
		if (solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs().size() < solicitacaoAberturaTurmaVO.getQuantidadeModulo()) {
			for (int x = solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs().size() + 1; x <= solicitacaoAberturaTurmaVO.getQuantidadeModulo(); x++) {
				SolicitacaoAberturaTurmaDisciplinaVO obj = new SolicitacaoAberturaTurmaDisciplinaVO();
				obj.setNumeroModulo(x);
				solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs().add(obj);
			}
		}

	}

	@Override
	public void realizarGeracaoCalendarioSolicitacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws Exception {
		solicitacaoAberturaTurmaVO.getCalendarioHorarioAulaVOs().clear();
		CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO> calendarioHorarioAulaVO = null;
		for (SolicitacaoAberturaTurmaDisciplinaVO obj : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
			if (obj.getDataInicio() != null && obj.getDataTermino() != null) {
				for (Date dataIni = obj.getDataInicio(); Uteis.getDataJDBC(dataIni).compareTo(Uteis.getDataJDBC(obj.getDataTermino())) <= 0; dataIni = Uteis.obterDataAvancada(dataIni, 1)) {
					calendarioHorarioAulaVO = obterCalendarioHorarioAulaVOMesAno(solicitacaoAberturaTurmaVO, dataIni);
					for (SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO : calendarioHorarioAulaVO.consultarListaCalendarioPorDiaSemana(Uteis.getDiaSemanaEnum(dataIni))) {
						if (Uteis.getData(solicitacaoAberturaTurmaDisciplinaVO.getDataInicio()).equals(Uteis.getData(dataIni))) {
							if (solicitacaoAberturaTurmaDisciplinaVO.getIsFeriado()) {
								throw new Exception(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_diaFeriadoDataAula").replace("{0}", Uteis.getData(solicitacaoAberturaTurmaDisciplinaVO.getDataInicio())).replace("{1}", obj.getNomeModulo().toString()));
							}
							solicitacaoAberturaTurmaDisciplinaVO.setNumeroModulo(obj.getNumeroModulo());
						}
					}
				}
			}
		}

	}

	private CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO> obterCalendarioHorarioAulaVOMesAno(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO, Date dataBase) throws Exception {
		for (CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO> calendarioHorarioAulaVO : solicitacaoAberturaTurmaVO.getCalendarioHorarioAulaVOs()) {
			if (calendarioHorarioAulaVO.getMesAno().equals(MesAnoEnum.getMesData(dataBase)) && calendarioHorarioAulaVO.getAno().equals(Uteis.getData(dataBase, "yyyy"))) {
				return calendarioHorarioAulaVO;
			}
		}
		CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO> calendarioHorarioAulaVO = new CalendarioHorarioAulaVO<SolicitacaoAberturaTurmaDisciplinaVO>();
		calendarioHorarioAulaVO.setMesAno(MesAnoEnum.getMesData(dataBase));
		calendarioHorarioAulaVO.setAno(Uteis.getData(dataBase, "yyyy"));
		DiaSemana diaSemanaInicial = Uteis.getDiaSemanaEnum(Uteis.getDataPrimeiroDiaMes(dataBase));
		DiaSemana diaSemanaFinal = Uteis.getDiaSemanaEnum(Uteis.getDataUltimoDiaMes(dataBase));
		if (solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCidade().getCodigo() == 0) {
			solicitacaoAberturaTurmaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCodigo(), false, null));
		}
		List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(Uteis.getDataPrimeiroDiaMes(dataBase), Uteis.getDataUltimoDiaMes(dataBase), solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCidade().getCodigo(), ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		dataBase = Uteis.getDataPrimeiroDiaMes(dataBase);
		Date dataFinal = Uteis.getDataUltimoDiaMes(dataBase);
		/*
		 * adiciona dias anteriores
		 */
		int x = 1;
		switch (diaSemanaInicial) {
		case DOMINGO:
			break;
		case SEGUNGA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.DOMINGO);
			break;
		case TERCA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.SEGUNGA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.DOMINGO);
			break;
		case QUARTA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.TERCA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.SEGUNGA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.DOMINGO);
			break;
		case QUINTA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.QUARTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.TERCA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.SEGUNGA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.DOMINGO);
			break;
		case SEXTA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.QUINTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.QUARTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.TERCA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.SEGUNGA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.DOMINGO);
			break;
		case SABADO:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.SEXTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.QUINTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.QUARTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.TERCA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.SEGUNGA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAntiga(dataBase, x++), true), DiaSemana.DOMINGO);
			break;
		default:
			break;
		}

		for (Date dataIni = dataBase; Uteis.getDataJDBC(dataIni).compareTo(Uteis.getDataJDBC(dataFinal)) <= 0; dataIni = Uteis.obterDataAvancada(dataIni, 1)) {
			////System.out.println(Uteis.getData(dataIni));
			SolicitacaoAberturaTurmaDisciplinaVO objClone = new SolicitacaoAberturaTurmaDisciplinaVO();
			objClone.setNumeroModulo(0);
			objClone.setDataInicio(dataIni);
			for (FeriadoVO feriadoVO : feriadoVOs) {
				if (Uteis.getDiaMesData(feriadoVO.getData()) == Uteis.getDiaMesData(objClone.getDataInicio()) && Uteis.getMesData(feriadoVO.getData()) == Uteis.getMesData(objClone.getDataInicio())) {
					objClone.setFeriado(feriadoVO);
					break;
				}
			}
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(objClone, Uteis.getDiaSemanaEnum(dataIni));

		}

		/*
		 * adiciona dias posteriores
		 */
		x = 1;
		switch (diaSemanaFinal) {
		case DOMINGO:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SEGUNGA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.TERCA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.QUARTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.QUINTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SEXTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SABADO);
			break;
		case SEGUNGA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.TERCA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.QUARTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.QUINTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SEXTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SABADO);
			break;
		case TERCA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.QUARTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.QUINTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SEXTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SABADO);
			break;
		case QUARTA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.QUINTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SEXTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SABADO);
			break;
		case QUINTA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SEXTA);
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SABADO);
			break;
		case SEXTA:
			calendarioHorarioAulaVO.adicionarItemListaCalendarioPorDiaSemana(new SolicitacaoAberturaTurmaDisciplinaVO(0, Uteis.obterDataAvancada(dataFinal, x++), true), DiaSemana.SABADO);
			break;
		case SABADO:
			break;
		default:
			break;
		}
		Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo(), "dataInicio");
		Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda(), "dataInicio");
		Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca(), "dataInicio");
		Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta(), "dataInicio");
		Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta(), "dataInicio");
		Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta(), "dataInicio");
		Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado(), "dataInicio");
		solicitacaoAberturaTurmaVO.getCalendarioHorarioAulaVOs().add(calendarioHorarioAulaVO);
		return calendarioHorarioAulaVO;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		SolicitacaoAberturaTurma.idEntidade = idEntidade;
	}

	@Override
	public void realizarEnvioNotificacao(ComunicacaoInternaVO comunicacaoInternaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
		destinatario.setCiJaLida(Boolean.FALSE);
		destinatario.setDestinatario(comunicacaoInternaVO.getPessoa());
		destinatario.setEmail(comunicacaoInternaVO.getPessoa().getEmail());
		destinatario.setNome(comunicacaoInternaVO.getPessoa().getNome());
		destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(destinatario);
		comunicacaoInternaVO.setTipoDestinatario("FU");
		comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
		// Para obter a mensagem do email formatado Usamos um metodo a parte.
		comunicacaoInternaVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoInternaVO.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
		comunicacaoInternaVO.setTipoMarketing(Boolean.FALSE);
		comunicacaoInternaVO.setTipoLeituraObrigatoria(Boolean.FALSE);
		comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
		comunicacaoInternaVO.setResponsavel(usuarioVO.getPessoa());
		getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioVO, configuracaoGeralSistemaVO,null);
	}

	@Override
	public SolicitacaoAberturaTurmaVO consultarSolicitacaoAberturaTurmaEmAbertoPorTurma(Integer turma) {
		StringBuilder sql = new StringBuilder(getSqlConsultaBasica());
		sql.append(" WHERE ");
		sql.append(" turma.codigo = ").append(turma).append(" ");
		sql.append(" and situacaoSolicitacaoAberturaTurma IN ('").append(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO.name()).append("', ");
		sql.append(" '").append(SituacaoSolicitacaoAberturaTurmaEnum.EM_REVISAO.name()).append("', ");
		sql.append(" '").append(SituacaoSolicitacaoAberturaTurmaEnum.AUTORIZADO.name()).append("', ");
		sql.append(" '").append(SituacaoSolicitacaoAberturaTurmaEnum.FINALIZADO.name()).append("' ");
		sql.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs);
		}
		return null;
	}

}
