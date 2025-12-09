package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.SolicitacaoAberturaTurmaDisciplinaVO;
import negocio.comuns.academico.SolicitacaoAberturaTurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoSolicitacaoAberturaTurmaEnum;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.SolicitacaoAberturaTurmaDisciplinaInterfaceFacade;

@Repository
@Lazy
public class SolicitacaoAberturaTurmaDisciplina extends ControleAcesso implements SolicitacaoAberturaTurmaDisciplinaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1716361798995117219L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirSolicitacaoAberturaTurmaDisciplina(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws Exception {
		if (solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCidade().getCodigo() == 0) {
			solicitacaoAberturaTurmaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCodigo(), false, null));
		}
		for (SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
			solicitacaoAberturaTurmaDisciplinaVO.setSolicitacaoAberturaTurma(solicitacaoAberturaTurmaVO);
			incluir(solicitacaoAberturaTurmaDisciplinaVO, solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCidade().getCodigo(), solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma());
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO, Integer cidade, SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAbertura) throws Exception {
		validarDados(solicitacaoAberturaTurmaDisciplinaVO, cidade, situacaoSolicitacaoAbertura);
		solicitacaoAberturaTurmaDisciplinaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO SolicitacaoAberturaTurmaDisciplina (");
				sql.append("numeroModulo, dataInicio, dataTermino, solicitacaoAberturaTurma");
				sql.append(") VALUES (?,?,?,?) returning codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo());
				ps.setDate(x++, Uteis.getDataJDBC(solicitacaoAberturaTurmaDisciplinaVO.getDataInicio()));
				ps.setDate(x++, Uteis.getDataJDBC(solicitacaoAberturaTurmaDisciplinaVO.getDataTermino()));
				ps.setInt(x++, solicitacaoAberturaTurmaDisciplinaVO.getSolicitacaoAberturaTurma().getCodigo());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					solicitacaoAberturaTurmaDisciplinaVO.setNovoObj(false);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO, Integer cidade, SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAbertura) throws Exception {
		validarDados(solicitacaoAberturaTurmaDisciplinaVO, cidade, situacaoSolicitacaoAbertura);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE SolicitacaoAberturaTurmaDisciplina SET ");
				sql.append("numeroModulo = ?, dataInicio = ?, dataTermino = ?, solicitacaoAberturaTurma = ?, revisar = ?, motivoRevisao = ?, disciplina = ?, professor = ? ");
				sql.append("WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo());
				ps.setDate(x++, Uteis.getDataJDBC(solicitacaoAberturaTurmaDisciplinaVO.getDataInicio()));
				ps.setDate(x++, Uteis.getDataJDBC(solicitacaoAberturaTurmaDisciplinaVO.getDataTermino()));
				ps.setInt(x++, solicitacaoAberturaTurmaDisciplinaVO.getSolicitacaoAberturaTurma().getCodigo());
				ps.setBoolean(x++, solicitacaoAberturaTurmaDisciplinaVO.getRevisar());
				ps.setString(x++, solicitacaoAberturaTurmaDisciplinaVO.getMotivoRevisao());
				if (solicitacaoAberturaTurmaDisciplinaVO.getDisciplina().getCodigo() > 0) {
					ps.setInt(x++, solicitacaoAberturaTurmaDisciplinaVO.getDisciplina().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (solicitacaoAberturaTurmaDisciplinaVO.getProfessor().getCodigo() > 0) {
					ps.setInt(x++, solicitacaoAberturaTurmaDisciplinaVO.getProfessor().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setInt(x++, solicitacaoAberturaTurmaDisciplinaVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(solicitacaoAberturaTurmaDisciplinaVO, cidade, situacaoSolicitacaoAbertura);
			return;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarSolicitacaoAberturaTurmaDisciplina(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws Exception {
		excluirSolicitacaoAberturaTurmaDisciplina(solicitacaoAberturaTurmaVO);
		if (solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCidade().getCodigo() == 0) {
			solicitacaoAberturaTurmaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCodigo(), false, null));
		}
		for (SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
			solicitacaoAberturaTurmaDisciplinaVO.setSolicitacaoAberturaTurma(solicitacaoAberturaTurmaVO);
			if (solicitacaoAberturaTurmaDisciplinaVO.isNovoObj()) {
				incluir(solicitacaoAberturaTurmaDisciplinaVO, solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCidade().getCodigo(), solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma());
			} else {
				alterar(solicitacaoAberturaTurmaDisciplinaVO, solicitacaoAberturaTurmaVO.getUnidadeEnsino().getCidade().getCodigo(), solicitacaoAberturaTurmaVO.getSituacaoSolicitacaoAberturaTurma());
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void excluirSolicitacaoAberturaTurmaDisciplina(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM SolicitacaoAberturaTurmaDisciplina WHERE solicitacaoAberturaTurma = ").append(solicitacaoAberturaTurmaVO.getCodigo());
		sql.append(" and codigo not in ( 0 ");
		for (SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO : solicitacaoAberturaTurmaVO.getSolicitacaoAberturaTurmaDisciplinaVOs()) {
			sql.append(", ").append(solicitacaoAberturaTurmaDisciplinaVO.getCodigo());
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	private String getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT SolicitacaoAberturaTurmaDisciplina.*, professor.nome as \"professor.nome\", disciplina.nome as \"disciplina.nome\"  FROM SolicitacaoAberturaTurmaDisciplina ");
		sql.append(" left join Pessoa as professor on professor.codigo = SolicitacaoAberturaTurmaDisciplina.professor ");
		sql.append(" left join Disciplina on Disciplina.codigo = SolicitacaoAberturaTurmaDisciplina.Disciplina ");
		return sql.toString();
	}

	@Override
	public List<SolicitacaoAberturaTurmaDisciplinaVO> consultarPorSolicitacaoAberturaTurma(Integer solicitacaoAberturaTurma) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE solicitacaoAberturaTurma = ").append(solicitacaoAberturaTurma);
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	public List<SolicitacaoAberturaTurmaDisciplinaVO> montarDadosConsulta(SqlRowSet rs) {
		List<SolicitacaoAberturaTurmaDisciplinaVO> solicitacaoAberturaTurmaDisciplinaVOs = new ArrayList<SolicitacaoAberturaTurmaDisciplinaVO>(0);
		while (rs.next()) {
			solicitacaoAberturaTurmaDisciplinaVOs.add(montarDados(rs));
		}
		Ordenacao.ordenarLista(solicitacaoAberturaTurmaDisciplinaVOs, "numeroModulo");
		return solicitacaoAberturaTurmaDisciplinaVOs;
	}

	public SolicitacaoAberturaTurmaDisciplinaVO montarDados(SqlRowSet rs) {
		SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplina = new SolicitacaoAberturaTurmaDisciplinaVO();
		solicitacaoAberturaTurmaDisciplina.setNovoObj(false);
		solicitacaoAberturaTurmaDisciplina.setCodigo(rs.getInt("codigo"));
		solicitacaoAberturaTurmaDisciplina.setNumeroModulo(rs.getInt("numeroModulo"));
		solicitacaoAberturaTurmaDisciplina.getSolicitacaoAberturaTurma().setCodigo(rs.getInt("solicitacaoAberturaTurma"));
		solicitacaoAberturaTurmaDisciplina.setDataInicio(rs.getDate("dataInicio"));
		solicitacaoAberturaTurmaDisciplina.setDataTermino(rs.getDate("dataTermino"));
		solicitacaoAberturaTurmaDisciplina.setMotivoRevisao(rs.getString("motivoRevisao"));
		solicitacaoAberturaTurmaDisciplina.setRevisar(rs.getBoolean("revisar"));
		solicitacaoAberturaTurmaDisciplina.getDisciplina().setCodigo(rs.getInt("disciplina"));
		solicitacaoAberturaTurmaDisciplina.getDisciplina().setNome(rs.getString("disciplina.nome"));
		solicitacaoAberturaTurmaDisciplina.getProfessor().setCodigo(rs.getInt("professor"));
		solicitacaoAberturaTurmaDisciplina.getProfessor().setNome(rs.getString("professor.nome"));
		return solicitacaoAberturaTurmaDisciplina;
	}

	@Override
	public void validarDados(SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO, Integer cidade, SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurmaEnum) throws ConsistirException, Exception {
		if (!solicitacaoAberturaTurmaDisciplinaVO.isValidarDados()) {
			return;
		}
		ConsistirException consistirException = new ConsistirException();
		if (solicitacaoAberturaTurmaDisciplinaVO.getDataInicio() == null) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurmaDisciplina_dataInicio").replace("{0}", solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo().toString()));
		}
		if (solicitacaoAberturaTurmaDisciplinaVO.getDataTermino() == null) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurmaDisciplina_dataTermino").replace("{0}", solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo().toString()));
		}
		if (solicitacaoAberturaTurmaDisciplinaVO.getDataInicio() != null && solicitacaoAberturaTurmaDisciplinaVO.getDataTermino() != null && solicitacaoAberturaTurmaDisciplinaVO.getDataInicio().compareTo(solicitacaoAberturaTurmaDisciplinaVO.getDataTermino()) > 0) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurmaDisciplina_dataInicio_dataTermino").replace("{0}", solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo().toString()));
		}
		if (situacaoSolicitacaoAberturaTurmaEnum.equals(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO)) {
			if (solicitacaoAberturaTurmaDisciplinaVO.getDataInicio() != null && solicitacaoAberturaTurmaDisciplinaVO.getDataTermino() != null) {
				Integer feriados = getFacadeFactory().getFeriadoFacade().consultaNumeroFeriadoNoPeriodoDesconsiderandoFimSemana(solicitacaoAberturaTurmaDisciplinaVO.getDataInicio(), solicitacaoAberturaTurmaDisciplinaVO.getDataTermino(), cidade, ConsiderarFeriadoEnum.ACADEMICO);
				if (feriados > 0) {
					consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurma_feriadoDataAula").replace("{0}", solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo().toString()).replace("{1}", feriados.toString()));
				}
			}
		}

		if (!situacaoSolicitacaoAberturaTurmaEnum.equals(SituacaoSolicitacaoAberturaTurmaEnum.EM_REVISAO)) {
			solicitacaoAberturaTurmaDisciplinaVO.setRevisar(false);
			solicitacaoAberturaTurmaDisciplinaVO.setMotivoRevisao("");
		} else {
			if (solicitacaoAberturaTurmaDisciplinaVO.getRevisar() && solicitacaoAberturaTurmaDisciplinaVO.getMotivoRevisao().trim().isEmpty()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurmaDisciplina_motivoRevisao").replace("{0}", solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo().toString()));
			}
		}

		if (situacaoSolicitacaoAberturaTurmaEnum.equals(SituacaoSolicitacaoAberturaTurmaEnum.FINALIZADO)) {
			if (solicitacaoAberturaTurmaDisciplinaVO.getDisciplina().getCodigo() == 0) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurmaDisciplina_disciplina").replace("{0}", solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo().toString()));
			}
			if (solicitacaoAberturaTurmaDisciplinaVO.getProfessor().getCodigo() == 0) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SolicitacaoAberturaTurmaDisciplina_professor").replace("{0}", solicitacaoAberturaTurmaDisciplinaVO.getNumeroModulo().toString()));
			}
		}

		if (consistirException != null && !consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}

	}

}
