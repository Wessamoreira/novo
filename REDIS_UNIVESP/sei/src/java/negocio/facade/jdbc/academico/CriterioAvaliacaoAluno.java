package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CriterioAvaliacaoAlunoVO;
import negocio.comuns.academico.CriterioAvaliacaoDisciplinaEixoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoDisciplinaVO;
import negocio.comuns.academico.CriterioAvaliacaoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoNotaConceitoVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.academico.CriterioAvaliacaoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.AvaliarNaoAvaliarEnum;
import negocio.comuns.academico.enumeradores.OrigemCriterioAvaliacaoIndicadorEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.FechamentoPeriodoLetivoException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.CriterioAvaliacaoAlunoInterfaceFacade;
import relatorio.negocio.comuns.academico.BoletimAcademicoRelVO;

@Repository
@Lazy
public class CriterioAvaliacaoAluno extends ControleAcesso implements CriterioAvaliacaoAlunoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7556722197850734915L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(CriterioAvaliacaoAlunoVO criterioAvaliacaoAlunoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		criterioAvaliacaoAlunoVO.getUsuarioCadastro().setCodigo(usuarioVO.getCodigo());
		criterioAvaliacaoAlunoVO.getUsuarioCadastro().setNome(usuarioVO.getNome());
		if (criterioAvaliacaoAlunoVO.isNovoObj()) {
			incluir(criterioAvaliacaoAlunoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(criterioAvaliacaoAlunoVO, verificarAcesso, usuarioVO);
		}

	}

	private void validarDados(CriterioAvaliacaoAlunoVO criterioAvaliacaoAlunoVO) throws ConsistirException {
		// if(criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getCodigo()
		// == 0){
		// throw new
		// ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_criterioAvaliacaoIndicador"));
		// }
		// if(criterioAvaliacaoAlunoVO.getNotaConceitoIndicadorAvaliacao().getCodigo()
		// == 0){
		// throw new
		// ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_notaConceitoIndicadorAvaliacao"));
		// }
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CriterioAvaliacaoAlunoVO criterioAvaliacaoAlunoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(getIdentidade(usuarioVO), verificarAcesso, usuarioVO);
		validarDados(criterioAvaliacaoAlunoVO);
		try {
			criterioAvaliacaoAlunoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO criterioAvaliacaoAluno (");
					sql.append(" matriculaPeriodo, criterioAvaliacao, criterioAvaliacaoPeriodoLetivo, criterioAvaliacaoDisciplina, criterioAvaliacaoDisciplinaEixoIndicador, criterioAvaliacaoIndicador, ");
					sql.append(" criterioAvaliacaoNotaConceito1Bimestre, nota1Bimestre, criterioAvaliacaoNotaConceito2Bimestre, nota2Bimestre, ano, semestre, ");
					sql.append(" criterioAvaliacaoNotaConceito3Bimestre, nota3Bimestre, criterioAvaliacaoNotaConceito4Bimestre, nota4Bimestre, dataCadastro, usuarioCadastro, origemCriterioAvaliacaoIndicador) ");
					sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getMatriculaPeriodo().getCodigo());
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacao().getCodigo());
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo());
					if (criterioAvaliacaoAlunoVO.getOrigemCriterioAvaliacaoIndicador().equals(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoDisciplina().getCodigo());
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoDisciplinaEixoIndicador().getCodigo());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getCodigo());
					if (criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().getCodigo() > 0 && criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getAvaliarPrimeiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().getCodigo());
						ps.setDouble(x++, criterioAvaliacaoAlunoVO.getNota1Bimestre());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					if (criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().getCodigo() > 0 && criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getAvaliarSegundoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().getCodigo());
						ps.setDouble(x++, criterioAvaliacaoAlunoVO.getNota2Bimestre());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					ps.setString(x++, criterioAvaliacaoAlunoVO.getAno());
					ps.setString(x++, criterioAvaliacaoAlunoVO.getSemestre());
					if (criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().getCodigo() > 0 && criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getAvaliarTerceiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().getCodigo());
						ps.setDouble(x++, criterioAvaliacaoAlunoVO.getNota3Bimestre());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					if (criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().getCodigo() > 0 && criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getAvaliarQuartoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().getCodigo());
						ps.setDouble(x++, criterioAvaliacaoAlunoVO.getNota4Bimestre());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(criterioAvaliacaoAlunoVO.getDataCadastro()));
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getUsuarioCadastro().getCodigo());
					ps.setString(x++, criterioAvaliacaoAlunoVO.getOrigemCriterioAvaliacaoIndicador().name());
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
			criterioAvaliacaoAlunoVO.setNovoObj(false);
		} catch (Exception e) {
			criterioAvaliacaoAlunoVO.setNovoObj(true);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CriterioAvaliacaoAlunoVO criterioAvaliacaoAlunoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		alterar(getIdentidade(usuarioVO), verificarAcesso, usuarioVO);
		validarDados(criterioAvaliacaoAlunoVO);
		try {
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE criterioAvaliacaoAluno SET ");
					sql.append(" matriculaPeriodo = ?, criterioAvaliacao = ?, criterioAvaliacaoPeriodoLetivo = ?, criterioAvaliacaoDisciplina = ?, criterioAvaliacaoDisciplinaEixoIndicador = ?, criterioAvaliacaoIndicador = ?, ");
					sql.append(" criterioAvaliacaoNotaConceito1Bimestre = ?, nota1Bimestre = ?, criterioAvaliacaoNotaConceito2Bimestre = ?, nota2Bimestre = ?, ano = ?, semestre = ?, ");
					sql.append(" criterioAvaliacaoNotaConceito3Bimestre = ?, nota3Bimestre = ?, criterioAvaliacaoNotaConceito4Bimestre = ?, nota4Bimestre = ?, dataCadastro = ?, usuarioCadastro = ?, origemCriterioAvaliacaoIndicador = ? ");
					sql.append(" WHERE codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getMatriculaPeriodo().getCodigo());
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacao().getCodigo());
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo());
					if (criterioAvaliacaoAlunoVO.getOrigemCriterioAvaliacaoIndicador().equals(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoDisciplina().getCodigo());
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoDisciplinaEixoIndicador().getCodigo());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getCodigo());
					if (criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().getCodigo() > 0 && criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getAvaliarPrimeiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().getCodigo());
						ps.setDouble(x++, criterioAvaliacaoAlunoVO.getNota1Bimestre());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					if (criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().getCodigo() > 0 && criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getAvaliarSegundoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().getCodigo());
						ps.setDouble(x++, criterioAvaliacaoAlunoVO.getNota2Bimestre());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					ps.setString(x++, criterioAvaliacaoAlunoVO.getAno());
					ps.setString(x++, criterioAvaliacaoAlunoVO.getSemestre());
					if (criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().getCodigo() > 0 && criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getAvaliarTerceiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().getCodigo());
						ps.setDouble(x++, criterioAvaliacaoAlunoVO.getNota3Bimestre());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					if (criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().getCodigo() > 0 && criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().getAvaliarQuartoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
						ps.setInt(x++, criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().getCodigo());
						ps.setDouble(x++, criterioAvaliacaoAlunoVO.getNota4Bimestre());
					} else {
						ps.setNull(x++, 0);
						ps.setNull(x++, 0);
					}
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(criterioAvaliacaoAlunoVO.getDataCadastro()));
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getUsuarioCadastro().getCodigo());
					ps.setString(x++, criterioAvaliacaoAlunoVO.getOrigemCriterioAvaliacaoIndicador().name());
					ps.setInt(x++, criterioAvaliacaoAlunoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(criterioAvaliacaoAlunoVO, verificarAcesso, usuarioVO);
				return;
			}
			criterioAvaliacaoAlunoVO.setNovoObj(false);
		} catch (Exception e) {
			criterioAvaliacaoAlunoVO.setNovoObj(false);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirCriterioAvaliacaoAlunoVO(CriterioAvaliacaoVO criterioAvaliacaoVO, List<CriterioAvaliacaoPeriodoLetivoVO> criterioAvaliacaoPeriodoLetivoVOs, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir(getIdentidade(usuarioVO), verificarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM CriterioAvaliacaoAluno WHERE criterioAvaliacao = ? and matriculaperiodo = ? and codigo not in (0 ");

		for (CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO : criterioAvaliacaoPeriodoLetivoVOs) {
			for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
				for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
					for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
						sql.append(", ").append(criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCodigo());
					}
				}
			}

			for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
				sql.append(", ").append(criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCodigo());
			}
		}
		sql.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), criterioAvaliacaoVO.getCodigo(), criterioAvaliacaoVO.getMatriculaPeriodoVO().getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirCriterioAvaliacaoAlunoVO(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		//if (!usuarioVO.getIsApresentarVisaoProfessor()) {
			excluirCriterioAvaliacaoAlunoVO(criterioAvaliacaoVO, criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs(), false, usuarioVO);
		//}
		for (CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO : criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs()) {
			for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
				for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
					for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setMatriculaPeriodo(criterioAvaliacaoVO.getMatriculaPeriodoVO());
						if (criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().isNovoObj()) {
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setAno(criterioAvaliacaoVO.getMatriculaPeriodoVO().getAno());
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setSemestre(criterioAvaliacaoVO.getMatriculaPeriodoVO().getSemestre());
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacao(criterioAvaliacaoVO);
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoDisciplina(criterioAvaliacaoDisciplinaVO);
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoEixoIndicadorVO);
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoIndicador(criterioAvaliacaoIndicadorVO);
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA);
						}
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota1Bimestre(0.0);
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota2Bimestre(0.0);
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota3Bimestre(0.0);
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota4Bimestre(0.0);
						if (criterioAvaliacaoIndicadorVO.getAvaliarPrimeiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR) && !criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getCodigo().equals(0)) {
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota1Bimestre(criterioAvaliacaoIndicadorVO.getNota1Bimestre() * criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getPeso());
						}
						if (criterioAvaliacaoIndicadorVO.getAvaliarSegundoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR) && !criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getCodigo().equals(0)) {
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota2Bimestre(criterioAvaliacaoIndicadorVO.getNota2Bimestre() * criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getPeso());
						}
						if (criterioAvaliacaoIndicadorVO.getAvaliarTerceiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR) && !criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getCodigo().equals(0)) {
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota3Bimestre(criterioAvaliacaoIndicadorVO.getNota3Bimestre() * criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getPeso());
						}
						if (criterioAvaliacaoIndicadorVO.getAvaliarQuartoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR) && !criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getCodigo().equals(0)) {
							criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota4Bimestre(criterioAvaliacaoIndicadorVO.getNota4Bimestre() * criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getPeso());
						}
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
						persistir(criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO(), verificarAcesso, usuarioVO);
					}
				}
			}
			//if (!usuarioVO.getIsApresentarVisaoProfessor()) {
				for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
					criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setMatriculaPeriodo(criterioAvaliacaoVO.getMatriculaPeriodoVO());
					if (criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().isNovoObj()) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setAno(criterioAvaliacaoVO.getMatriculaPeriodoVO().getAno());
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setSemestre(criterioAvaliacaoVO.getMatriculaPeriodoVO().getSemestre());
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacao(criterioAvaliacaoVO);
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoDisciplina(null);
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoDisciplinaEixoIndicador(null);
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.GERAL);
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoIndicador(criterioAvaliacaoIndicadorVO);
					}
					criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota1Bimestre(0.0);
					criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota2Bimestre(0.0);
					criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota3Bimestre(0.0);
					criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota4Bimestre(0.0);
					if (criterioAvaliacaoIndicadorVO.getAvaliarPrimeiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR) && !criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getCodigo().equals(0)) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota1Bimestre(criterioAvaliacaoIndicadorVO.getNota1Bimestre() * criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getPeso());
					}
					if (criterioAvaliacaoIndicadorVO.getAvaliarSegundoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR) && !criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getCodigo().equals(0)) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota2Bimestre(criterioAvaliacaoIndicadorVO.getNota2Bimestre() * criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getPeso());
					}
					if (criterioAvaliacaoIndicadorVO.getAvaliarTerceiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR) && !criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getCodigo().equals(0)) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota3Bimestre(criterioAvaliacaoIndicadorVO.getNota3Bimestre() * criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getPeso());
					}
					if (criterioAvaliacaoIndicadorVO.getAvaliarQuartoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR) && !criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getCodigo().equals(0)) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setNota4Bimestre(criterioAvaliacaoIndicadorVO.getNota4Bimestre() * criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getPeso());
					}
					persistir(criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO(), verificarAcesso, usuarioVO);
				}
		//	}
		}
		registrarNotaHistoricoAlunoRel(criterioAvaliacaoVO, usuarioVO);
		//if (!usuarioVO.getIsApresentarVisaoProfessor()) {
			getFacadeFactory().getMatriculaPeriodoFacade().incluirObservacaoCriterioAvaliacaoAluno(criterioAvaliacaoVO.getMatriculaPeriodoVO(), usuarioVO);
		//}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void registrarNotaHistoricoAlunoRel(CriterioAvaliacaoVO criterioAvaliacaoVO, UsuarioVO usuarioVO) throws Exception {
		Double nota1;
		Double nota2;
		Double nota3;
		Double nota4;
		Map<Integer, ConfiguracaoAcademicoVO> configuracoes = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		for (CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO : criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs()) {
			for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
				nota1 = 0.0;
				nota2 = 0.0;
				nota3 = 0.0;
				nota4 = 0.0;
				for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
					for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
						nota1 += criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getNota1Bimestre();
						nota2 += criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getNota2Bimestre();
						nota3 += criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getNota3Bimestre();
						nota4 += criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getNota4Bimestre();
					}
				}

				HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula_matriculaPeriodo_Disciplina(criterioAvaliacaoVO.getMatriculaVO().getMatricula(), criterioAvaliacaoVO.getMatriculaPeriodoVO().getCodigo(), criterioAvaliacaoVO.getGradeCurricularVO().getCodigo(), criterioAvaliacaoDisciplinaVO.getDisciplina().getCodigo(), false, usuarioVO);
				if (historicoVO != null && historicoVO.getCodigo() > 0 && historicoVO.getConfiguracaoAcademico().getCodigo() > 0) {
					if (!configuracoes.containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())) {
						configuracoes.put(historicoVO.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(historicoVO.getConfiguracaoAcademico().getCodigo(), usuarioVO));
					}
					historicoVO.setConfiguracaoAcademico(configuracoes.get(historicoVO.getConfiguracaoAcademico().getCodigo()));
					for (int y = 1; y <= 30; y++) {
						UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "nota" + y);
					}
					historicoVO.setHistoricoCriterioAvaliacaoAluno(true);
					historicoVO.setCriterioAvaliacao(criterioAvaliacaoVO);
					historicoVO.setNota1(Uteis.arrendondarForcando2CadasDecimais(nota1));
					historicoVO.setNota2(Uteis.arrendondarForcando2CadasDecimais(nota2));
					historicoVO.setNota3(Uteis.arrendondarForcando2CadasDecimais(nota3));
					historicoVO.setNota4(Uteis.arrendondarForcando2CadasDecimais(nota4));
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota1PorConceito() && nota1 != null) {
						for (ConfiguracaoAcademicoNotaConceitoVO notaConceito : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota1ConceitoVOs()) {
							if (notaConceito.getFaixaNota1() >= nota1 && notaConceito.getFaixaNota2() <= nota1) {
								historicoVO.setNota1Conceito(notaConceito);
							}
						}
					}
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota2PorConceito() && nota2 != null) {

						for (ConfiguracaoAcademicoNotaConceitoVO notaConceito : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota2ConceitoVOs()) {
							if (notaConceito.getFaixaNota1() >= nota2 && notaConceito.getFaixaNota2() <= nota2) {
								historicoVO.setNota2Conceito(notaConceito);
							}
						}
					}
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota3PorConceito() && nota3 != null) {

						for (ConfiguracaoAcademicoNotaConceitoVO notaConceito : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota3ConceitoVOs()) {
							if (notaConceito.getFaixaNota1() >= nota3 && notaConceito.getFaixaNota2() <= nota3) {
								historicoVO.setNota3Conceito(notaConceito);
							}
						}
					}
					if (historicoVO.getConfiguracaoAcademico().getUtilizarNota4PorConceito() && nota4 != null) {

						for (ConfiguracaoAcademicoNotaConceitoVO notaConceito : historicoVO.getConfiguracaoAcademico().getConfiguracaoAcademicoNota4ConceitoVOs()) {
							if (notaConceito.getFaixaNota1() >= nota4 && notaConceito.getFaixaNota2() <= nota4) {
								historicoVO.setNota4Conceito(notaConceito);
							}
						}
					}
					getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(historicoVO, usuarioVO);
					if (getFacadeFactory().getRegistroAulaFacade().consultarExistenciaRegistroAula(criterioAvaliacaoVO.getMatriculaVO().getMatricula(), criterioAvaliacaoVO.getMatriculaPeriodoVO().getTurma().getCodigo(), criterioAvaliacaoDisciplinaVO.getDisciplina().getCodigo(), 0, criterioAvaliacaoVO.getMatriculaPeriodoVO().getAno(), criterioAvaliacaoVO.getMatriculaPeriodoVO().getSemestre())) {
						getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(historicoVO, historicoVO.getConfiguracaoAcademico(), usuarioVO);
					}
					if (!historicoVO.getConfiguracaoAcademico().verificarHistoricoProvenienteImportacao(historicoVO)) {
						boolean resultado = false;
						try {
							getFacadeFactory().getHistoricoFacade().verificaAlunoReprovadoFalta(historicoVO, historicoVO.getConfiguracaoAcademico(), usuarioVO);
							resultado = historicoVO.getConfiguracaoAcademico().substituirVariaveisFormulaPorValores(historicoVO, null, true);
						} catch (FechamentoPeriodoLetivoException e) {
							historicoVO.setMediaFinal(null);
							getFacadeFactory().getLogFechamentoFacade().realizarRegistroLogFechamento(historicoVO.getMatricula().getMatricula());
						}

						if (historicoVO.getMediaFinal() != null) {
							if (historicoVO.getConfiguracaoAcademico().getUtilizarArredondamentoMediaParaMais()) {
								historicoVO.setMediaFinal(Uteis.arredondarMultiploDeCincoParaCima(historicoVO.getMediaFinal()));
							} else if (historicoVO.getConfiguracaoAcademico().getNotasDeCincoEmCincoDecimos() || historicoVO.getConfiguracaoAcademico().getNotasDeCincoEmCincoDecimosApenasMedia()) {
								historicoVO.setMediaFinal(Math.round(2 * historicoVO.getMediaFinal()) / 2.0);
							}

							if ((!historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) && (!historicoVO.getSituacao().equals(SituacaoHistorico.ISENTO.getValor())) && !historicoVO.getSituacao().equals("")) {
								if (resultado) {
									historicoVO.setSituacao(SituacaoHistorico.APROVADO.getValor());
								} else {
									historicoVO.setSituacao(SituacaoHistorico.REPROVADO.getValor());
								}
							}
						} else {
							historicoVO.setSituacao(SituacaoHistorico.CURSANDO.getValor());
						}
					}
					getFacadeFactory().getHistoricoFacade().alterar(historicoVO, usuarioVO);
				}
			}
		}
	}

	@Override
	public List<CriterioAvaliacaoVO> consultarCriterioAvaliacaoAlunoResponder(String consultarPor, Integer matriculaPeriodo, MatriculaVO matricula, Integer disciplina, String situacao, TurmaVO turma, String ano, String semestre, Integer unidadeEnsino, Integer gradeCurricular, boolean ordenarVisaoPais, boolean visaoProfessor, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdentidade(usuarioVO), verificarAcesso, usuarioVO);
		if (unidadeEnsino == null || unidadeEnsino.equals(0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_unidadeEnsino"));
		}
		if (consultarPor.equals("matricula") && (matricula == null || matricula.getMatricula().trim().isEmpty())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_matricula"));
		}
		if (consultarPor.equals("turma") && (turma == null || turma.getCodigo().equals(0))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_turma"));
		}
		if (consultarPor.equals("turma") && !turma.getIntegral() && (ano == null || ano.trim().isEmpty() || ano.length() != 4)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_ano"));
		}
		// if (consultarPor.equals("matricula") && !matricula.getCurso().getPeriodicidade().equals("IN") && (ano == null || ano.trim().isEmpty() || ano.length() != 4)) {
		// throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_ano"));
		// }
		if (consultarPor.equals("turma") && turma.getSemestral() && (semestre == null || semestre.trim().isEmpty() || semestre.length() != 1)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_semestre"));
		}
		// if (consultarPor.equals("matricula") && matricula.getCurso().getPeriodicidade().equals("SE") && (semestre == null || semestre.trim().isEmpty() || semestre.length() != 1)) {
		// throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_semestre"));
		// }
		List<CriterioAvaliacaoVO> criterioAvaliacaoVOs = new ArrayList<CriterioAvaliacaoVO>();
		if (situacao == null || situacao.trim().isEmpty() || situacao.equals("respondido")) {
			criterioAvaliacaoVOs = consultarCriterioAvaliacaoAlunoRespondido(consultarPor, matriculaPeriodo, matricula.getMatricula(), disciplina, turma.getCodigo(), ano, semestre, unidadeEnsino, gradeCurricular, ordenarVisaoPais, visaoProfessor, nivelMontarDados, verificarAcesso, usuarioVO);
		}
		if (situacao == null || situacao.trim().isEmpty() || situacao.equals("naoRespondido")) {
			criterioAvaliacaoVOs.addAll(consultarCriterioAvaliacaoAlunoNaoRespondido(consultarPor, matriculaPeriodo, matricula.getMatricula(), disciplina, turma.getCodigo(), ano, semestre, unidadeEnsino, gradeCurricular, ordenarVisaoPais, visaoProfessor, nivelMontarDados, verificarAcesso, usuarioVO));
		}		
		Ordenacao.ordenarLista(criterioAvaliacaoVOs, "ordenarPorAluno");
		if (!criterioAvaliacaoVOs.isEmpty()) {
			Iterator<CriterioAvaliacaoVO> i = criterioAvaliacaoVOs.iterator();
			while (i.hasNext()) {
				CriterioAvaliacaoVO obj = i.next();
				obj.getMatriculaPeriodoVO().getTurma().getProfessor().setNome(consultarProfessorComMaiorVinculoTurma(obj.getMatriculaPeriodoVO().getCodigo()));
			}
		}
		return criterioAvaliacaoVOs;
	}

	@Override
	public List<CriterioAvaliacaoVO> consultarCriterioAvaliacaoAlunoNaoRespondido(String consultarPor, Integer matriculaPeriodo, String matricula, Integer disciplina, Integer turma, String ano, String semestre, Integer unidadeEnsino, Integer gradeCurricular, boolean ordenarVisaoPais, boolean visaoProfessor, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdentidade(usuarioVO), verificarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("SELECT distinct matriculaPeriodo.codigo as \"matriculaPeriodo.codigo\", matriculaPeriodo.ano as \"matriculaPeriodo.ano\", matriculaPeriodo.semestre as \"matriculaPeriodo.semestre\", ");
		sql.append("  pessoa.nome as \"pessoa.nome\", pessoa.codigo as \"pessoa.codigo\", turma.identificadorturma as \"turma.identificadorturma\", turma.codigo as \"turma.codigo\", unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\",  ");
		sql.append("  Matricula.matricula as \"matricula.matricula\", Matricula.situacao as \"matricula.situacao\", matriculaperiodo.situacaomatriculaperiodo as \"matriculaperiodo.situacaomatriculaperiodo\", matriculaperiodo.observacaocriterioavaliacao as \"matriculaperiodo.observacaocriterioavaliacao\",  ");
		sql.append("  GradeCurricular.codigo as \"GradeCurricular.codigo\", GradeCurricular.nome as \"GradeCurricular.nome\",  ");
		sql.append("  curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		// Dados Criterio Avaliacao
		sql.append("  CriterioAvaliacao.codigo as \"CriterioAvaliacao.codigo\",  ");
		// Dados Criterio Periodo Letivo
		sql.append("  CriterioAvaliacaoPeriodoLetivo.codigo as \"criterioAvaliacaoPeriodoLetivo.codigo\",  ");
		// Dados Periodo Letivo
		sql.append(" periodoLetivo.codigo as \"periodoLetivo.codigo\", periodoLetivo.descricao as \"periodoLetivo.descricao\", periodoLetivo.periodoLetivo as \"periodoLetivo.periodoLetivo\" ");
		// Verificar se possui Visão Professor
		if (visaoProfessor) {
			sql.append(" ,criterioavaliacaodisciplina.codigo as \"criterioavaliacaodisciplina.codigo\" ");
		}
		sql.append(" FROM MatriculaPeriodo ");
		sql.append(" inner join Matricula on  Matricula.matricula = matriculaPeriodo.matricula ");
		sql.append(" inner join Curso on  Matricula.Curso = Curso.codigo ");
		sql.append(" inner join Pessoa on  Pessoa.codigo = matricula.aluno ");
		sql.append(" inner join UnidadeEnsino on  UnidadeEnsino.codigo = matricula.unidadeEnsino ");                                                   
		sql.append(" left join Matriculaperiodoturmadisciplina on Matriculaperiodoturmadisciplina.MatriculaPeriodo = MatriculaPeriodo.codigo ");
		sql.append(" inner join historico on historico.matricula = matricula.matricula");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sql.append(" inner join CriterioAvaliacao on  CriterioAvaliacao.unidadeensino = UnidadeEnsino.codigo and  CriterioAvaliacao.gradecurricular = historico.matrizcurricular and CriterioAvaliacao.situacao =  'ATIVO' and CriterioAvaliacao.anovigencia <= matriculaperiodo.ano ");
		sql.append(" and  CriterioAvaliacao.codigo = (select ca.codigo from CriterioAvaliacao as ca where ca.unidadeensino = UnidadeEnsino.codigo and  ca.gradecurricular = historico.matrizcurricular and ca.situacao =  'ATIVO' and ca.anovigencia <= matriculaperiodo.ano order by ca.anovigencia desc limit 1) ");
		sql.append(" inner join CriterioAvaliacaoPeriodoLetivo on  CriterioAvaliacaoPeriodoLetivo.CriterioAvaliacao = CriterioAvaliacao.codigo ");
		sql.append(" inner join criterioavaliacaodisciplina on criterioavaliacaodisciplina.CriterioAvaliacaoPeriodoLetivo = CriterioAvaliacaoPeriodoLetivo.codigo and Matriculaperiodoturmadisciplina.disciplina = criterioavaliacaodisciplina.disciplina ");
		sql.append(" inner join PeriodoLetivo on  CriterioAvaliacaoPeriodoLetivo.periodoLetivo = periodoLetivo.codigo and periodoletivo.codigo = historico.periodoletivomatrizcurricular ");
		sql.append(" inner join GradeCurricular on  CriterioAvaliacao.GradeCurricular = GradeCurricular.codigo and GradeCurricular.codigo = matricula.gradecurricularatual ");
		sql.append(" inner join Turma on  Turma.codigo = Matriculaperiodoturmadisciplina.Turma ");

		sql.append(" where 1 = 1 ");
		sql.append(" and MatriculaPeriodo.situacaoMatriculaPeriodo  in ('AT', 'CO', 'FI', 'FO') ");
		if (matricula != null && !matricula.trim().isEmpty() && consultarPor.equals("matricula")) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		if (matriculaPeriodo != null && matriculaPeriodo > 0 && consultarPor.equals("matriculaPeriodo")) {
			sql.append(" and MatriculaPeriodo.codigo = ").append(matriculaPeriodo).append(" ");
		}
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and MatriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and Matricula.unidadeEnsino = '").append(unidadeEnsino).append("' ");
		}
		if (gradeCurricular != null && gradeCurricular > 0) {
			sql.append(" and GradeCurricular.codigo = ").append(gradeCurricular).append(" ");
		}
		if (turma != null && turma > 0 && consultarPor.equals("turma")) {
			sql.append(" and MatriculaPeriodo.turma = ").append(turma).append(" ");
		}
		if (disciplina != null && disciplina > 0 && visaoProfessor) {
			sql.append(" and criterioavaliacaodisciplina.disciplina = ").append(disciplina).append(" ");
		}

		sql.append(" and MatriculaPeriodo.codigo not in ( select distinct matriculaperiodo from criterioAvaliacaoAluno ");
		sql.append(" inner join criterioavaliacao ca on ca.codigo = criterioAvaliacaoAluno.criterioAvaliacao and ca.codigo = CriterioAvaliacao.codigo ");
		sql.append(" inner join criterioavaliacaoperiodoletivo capl on capl.codigo = criterioAvaliacaoAluno.criterioAvaliacaoPeriodoLetivo and capl.codigo = CriterioAvaliacaoPeriodoLetivo.codigo ");
		if (disciplina != null && disciplina > 0 && visaoProfessor) {
			sql.append(" inner join criterioavaliacaodisciplina cad on cad.codigo = criterioAvaliacaoAluno.criterioavaliacaodisciplina ");
			sql.append(" and cad.disciplina = ").append(disciplina);
		}
		sql.append(" WHERE criterioAvaliacaoAluno.matriculaperiodo = matriculaperiodo.codigo ) ");
		
		sql.append(" and not exists (select distinct matriculaperiodo.codigo from criterioAvaliacaoAluno caa inner join criterioavaliacao ca on ca.codigo = caa.criterioAvaliacao ");
		sql.append(" inner join criterioavaliacaoperiodoletivo capl on capl.criterioAvaliacao = ca.codigo and capl.periodoletivo = CriterioAvaliacaoPeriodoLetivo.periodoletivo ");
		if (disciplina != null && disciplina > 0 && visaoProfessor) {
			sql.append(" inner join criterioavaliacaodisciplina cad on cad.codigo = caa.criterioavaliacaodisciplina ");
			sql.append(" and cad.disciplina = ").append(disciplina);
		}
		sql.append(" WHERE caa.matriculaperiodo = matriculaperiodo.codigo and ca.gradecurricular = CriterioAvaliacao.gradecurricular) ");
		
		sql.append(" order by pessoa.nome, matricula.matricula, matriculaperiodo.ano, matriculaperiodo.semestre, periodoLetivo.periodoLetivo ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<CriterioAvaliacaoVO> criterioAvaliacaoVOs = new ArrayList<CriterioAvaliacaoVO>(0);
		CriterioAvaliacaoVO criterioAvaliacaoVO = null;
		CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO = null;
		while (rs.next()) {
			criterioAvaliacaoVO = new CriterioAvaliacaoVO();
			criterioAvaliacaoVO.setNovoObj(false);
			criterioAvaliacaoVO.setCriterioAvaliacaoAlunoRespondido(false);
			criterioAvaliacaoVO.setCodigo(rs.getInt("CriterioAvaliacao.codigo"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().setCodigo(rs.getInt("matriculaPeriodo.codigo"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().setAno(rs.getString("matriculaPeriodo.ano"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().setSemestre(rs.getString("matriculaPeriodo.semestre"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().setMatricula(rs.getString("matricula.matricula"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().setObservacaoCriterioAvaliacao(rs.getString("matriculaPeriodo.observacaocriterioavaliacao"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().getPeridoLetivo().setCodigo(rs.getInt("periodoLetivo.codigo"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(rs.getInt("periodoLetivo.periodoLetivo"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().getPeridoLetivo().setDescricao(rs.getString("periodoLetivo.descricao"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(rs.getString("matriculaPeriodo.situacaoMatriculaPeriodo"));
			criterioAvaliacaoVO.getMatriculaVO().setMatricula(rs.getString("matricula.matricula"));
			criterioAvaliacaoVO.getMatriculaVO().setSituacao(rs.getString("matricula.situacao"));
			criterioAvaliacaoVO.getMatriculaVO().getAluno().setCodigo(rs.getInt("pessoa.codigo"));
			criterioAvaliacaoVO.getMatriculaVO().getAluno().setNome(rs.getString("pessoa.nome"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().getTurma().setCodigo(rs.getInt("turma.codigo"));
			criterioAvaliacaoVO.getMatriculaPeriodoVO().getTurma().setIdentificadorTurma(rs.getString("turma.identificadorturma"));
			criterioAvaliacaoVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino.codigo"));
			criterioAvaliacaoVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
			criterioAvaliacaoVO.getGradeCurricularVO().setCodigo(rs.getInt("gradeCurricular.codigo"));
			criterioAvaliacaoVO.getGradeCurricularVO().setNome(rs.getString("gradeCurricular.nome"));
			criterioAvaliacaoVO.getMatriculaVO().getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino.codigo"));
			criterioAvaliacaoVO.getMatriculaVO().getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
			criterioAvaliacaoVO.getCurso().setCodigo(rs.getInt("curso.codigo"));
			criterioAvaliacaoVO.getCurso().setNome(rs.getString("curso.nome"));
			criterioAvaliacaoVO.getMatriculaVO().getCurso().setCodigo(rs.getInt("curso.codigo"));
			criterioAvaliacaoVO.getMatriculaVO().getCurso().setNome(rs.getString("curso.nome"));

			if (nivelMontarDados.equals(NivelMontarDados.TODOS) && rs.getInt("criterioAvaliacaoPeriodoLetivo.codigo") > 0) {
				criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs().add(getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().consultarPorChavePrimaria(rs.getInt("criterioAvaliacaoPeriodoLetivo.codigo"), disciplina, Uteis.NIVELMONTARDADOS_TODOS));
			} else {
				criterioAvaliacaoPeriodoLetivoVO = new CriterioAvaliacaoPeriodoLetivoVO();
				criterioAvaliacaoPeriodoLetivoVO.setCodigo(rs.getInt("CriterioAvaliacaoPeriodoLetivo.codigo"));
				criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setCodigo(rs.getInt("periodoLetivo.codigo"));
				criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setPeriodoLetivo(rs.getInt("periodoLetivo.periodoLetivo"));
				criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setDescricao(rs.getString("periodoLetivo.descricao"));
				criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs().add(criterioAvaliacaoPeriodoLetivoVO);
			}

			criterioAvaliacaoVOs.add(criterioAvaliacaoVO);
		}
		return criterioAvaliacaoVOs;
	}

	private StringBuilder getSqlCompletoCriterioAvaliacaoRespondido() {
		StringBuilder sql = new StringBuilder("SELECT distinct CriterioAvaliacaoAluno.*, pessoa.nome as \"pessoa.nome\", turma.identificadorturma as \"turma.identificadorturma\", turma.codigo as \"turma.codigo\", unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\",  ");
		sql.append("  Matricula.matricula as \"matricula.matricula\", Matricula.situacao as \"matricula.situacao\", matriculaperiodo.situacaomatriculaperiodo as \"matriculaperiodo.situacaomatriculaperiodo\",  ");
		sql.append("  GradeCurricular.codigo as \"GradeCurricular.codigo\", GradeCurricular.nome as \"GradeCurricular.nome\",  ");
		sql.append("  curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		sql.append("  matriculaperiodo.ano as \"matriculaperiodo.ano\", matriculaperiodo.semestre as \"matriculaperiodo.semestre\", matriculaperiodo.observacaocriterioavaliacao as \"matriculaperiodo.observacaocriterioavaliacao\",  ");
		// Dados Criterio Periodo Letivo
		sql.append("  CriterioAvaliacaoPeriodoLetivo.codigo as \"criterioAvaliacaoPeriodoLetivo.codigo\",  ");
		// Dados Periodo Letivo
		sql.append(" periodoLetivo.codigo as \"periodoLetivo.codigo\", periodoLetivo.descricao as \"periodoLetivo.descricao\", periodoLetivo.periodoLetivo as \"periodoLetivo.periodoLetivo\", ");
		// Dados Criterio Disciplina
		sql.append(" CriterioAvaliacaoDisciplina.codigo as \"CriterioAvaliacaoDisciplina.codigo\", CriterioAvaliacaoDisciplina.ordem as \"CriterioAvaliacaoDisciplina.ordem\", ");
		// Dados Disciplina
		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
		// Dados Criterio Eixo Indicador
		sql.append(" CriterioAvaliacaoDisciplinaEixoIndicador.codigo as \"CriterioAvaliacaoDisciplinaEixoIndicador.codigo\", CriterioAvaliacaoDisciplinaEixoIndicador.eixoIndicador as \"CriterioAvaliacaoDisciplinaEixoIndicador.eixoIndicador\" , ");
		sql.append(" CriterioAvaliacaoDisciplinaEixoIndicador.ordem as \"CriterioAvaliacaoDisciplinaEixoIndicador.ordem\", CriterioAvaliacaoDisciplinaEixoIndicador.nota as \"CriterioAvaliacaoDisciplinaEixoIndicador.nota\", ");
		// Dados Criterio Indicador
		sql.append(" criterioAvaliacaoIndicador.codigo as \"criterioAvaliacaoIndicador.codigo\", criterioAvaliacaoIndicador.descricao as \"criterioAvaliacaoIndicador.descricao\" , ");
		sql.append(" criterioAvaliacaoIndicador.ordem as \"criterioAvaliacaoIndicador.ordem\", criterioAvaliacaoIndicador.origemCriterioAvaliacaoIndicador as \"criterioAvaliacaoIndicador.origemCriterioAvaliacaoIndicador\" , ");
		sql.append(" criterioAvaliacaoIndicador.avaliarPrimeiroBimestre as \"criterioAvaliacaoIndicador.avaliarPrimeiroBimestre\", criterioAvaliacaoIndicador.avaliarSegundoBimestre as \"criterioAvaliacaoIndicador.avaliarSegundoBimestre\", ");
		sql.append(" criterioAvaliacaoIndicador.avaliarTerceiroBimestre as \"criterioAvaliacaoIndicador.avaliarTerceiroBimestre\", criterioAvaliacaoIndicador.avaliarQuartoBimestre as \"criterioAvaliacaoIndicador.avaliarQuartoBimestre\", ");
		sql.append(" criterioAvaliacaoIndicador.nota1Bimestre as \"criterioAvaliacaoIndicador.nota1Bimestre\", criterioAvaliacaoIndicador.nota2Bimestre as \"criterioAvaliacaoIndicador.nota2Bimestre\", ");
		sql.append(" criterioAvaliacaoIndicador.nota3Bimestre as \"criterioAvaliacaoIndicador.nota3Bimestre\", criterioAvaliacaoIndicador.nota4Bimestre as \"criterioAvaliacaoIndicador.nota4Bimestre\", ");
		// Dados da nota 1
		sql.append(" criterioAvaliacaoNotaConceito1Bimestre.peso as \"criterioAvaliacaoNotaConceito1Bimestre.peso\", ");
		sql.append(" nota1.codigo as \"nota1.codigo\", nota1.descricao as \"nota1.descricao\", nota1.nomeArquivo as \"nota1.nomeArquivo\",  nota1.pastaBaseArquivo as \"nota1.pastaBaseArquivo\", ");
		// Dados da nota 2
		sql.append(" criterioAvaliacaoNotaConceito2Bimestre.peso as \"criterioAvaliacaoNotaConceito2Bimestre.peso\", ");
		sql.append(" nota2.codigo as \"nota2.codigo\", nota2.descricao as \"nota2.descricao\", nota2.nomeArquivo as \"nota2.nomeArquivo\",  nota2.pastaBaseArquivo as \"nota2.pastaBaseArquivo\", ");
		// Dados da nota 3
		sql.append(" criterioAvaliacaoNotaConceito3Bimestre.peso as \"criterioAvaliacaoNotaConceito3Bimestre.peso\", ");
		sql.append(" nota3.codigo as \"nota3.codigo\", nota3.descricao as \"nota3.descricao\", nota3.nomeArquivo as \"nota3.nomeArquivo\",  nota3.pastaBaseArquivo as \"nota3.pastaBaseArquivo\", ");
		// Dados da nota 3
		sql.append(" criterioAvaliacaoNotaConceito4Bimestre.peso as \"criterioAvaliacaoNotaConceito4Bimestre.peso\", ");
		sql.append(" nota4.codigo as \"nota4.codigo\", nota4.descricao as \"nota4.descricao\", nota4.nomeArquivo as \"nota4.nomeArquivo\",  nota4.pastaBaseArquivo as \"nota4.pastaBaseArquivo\" ");

		sql.append(" FROM CriterioAvaliacaoAluno ");
		sql.append(" inner join MatriculaPeriodo on  MatriculaPeriodo.codigo = CriterioAvaliacaoAluno.matriculaPeriodo ");
		sql.append(" inner join Matricula on  Matricula.matricula = matriculaPeriodo.matricula ");
		sql.append(" inner join Pessoa on  Pessoa.codigo = matricula.aluno ");
		sql.append(" inner join Curso on  Matricula.Curso = Curso.codigo ");
		sql.append(" inner join UnidadeEnsino on  UnidadeEnsino.codigo = matricula.unidadeEnsino ");
		sql.append(" inner join CriterioAvaliacao on  CriterioAvaliacao.codigo = CriterioAvaliacaoAluno.CriterioAvaliacao ");
		sql.append(" inner join GradeCurricular on  CriterioAvaliacao.GradeCurricular = GradeCurricular.codigo ");
		sql.append(" inner join CriterioAvaliacaoPeriodoLetivo on  CriterioAvaliacaoPeriodoLetivo.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoPeriodoLetivo ");
		sql.append(" inner join PeriodoLetivo on  CriterioAvaliacaoPeriodoLetivo.periodoLetivo = periodoLetivo.codigo ");
		sql.append(" inner join CriterioAvaliacaoIndicador on  CriterioAvaliacaoIndicador.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoIndicador ");
		sql.append(" inner join historico on  CriterioAvaliacao.codigo = historico.CriterioAvaliacao ");		
		sql.append(" left join CriterioAvaliacaoDisciplina on  CriterioAvaliacaoDisciplina.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoDisciplina ");
		sql.append(" left join Disciplina on  Disciplina.codigo = criterioAvaliacaoDisciplina.disciplina ");
		sql.append(" left join CriterioAvaliacaoDisciplinaEixoIndicador on  CriterioAvaliacaoDisciplinaEixoIndicador.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoDisciplinaEixoIndicador ");
		sql.append(" left join CriterioAvaliacaoNotaConceito criterioAvaliacaoNotaConceito1Bimestre on  criterioAvaliacaoNotaConceito1Bimestre.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoNotaConceito1Bimestre ");
		sql.append(" left join CriterioAvaliacaoNotaConceito criterioAvaliacaoNotaConceito2Bimestre on  criterioAvaliacaoNotaConceito2Bimestre.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoNotaConceito2Bimestre ");
		sql.append(" left join CriterioAvaliacaoNotaConceito criterioAvaliacaoNotaConceito3Bimestre on  criterioAvaliacaoNotaConceito3Bimestre.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoNotaConceito3Bimestre ");
		sql.append(" left join CriterioAvaliacaoNotaConceito criterioAvaliacaoNotaConceito4Bimestre on  criterioAvaliacaoNotaConceito4Bimestre.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoNotaConceito4Bimestre ");
		sql.append(" left join NotaConceitoIndicadorAvaliacao nota1 on  criterioAvaliacaoNotaConceito1Bimestre.notaConceitoIndicadorAvaliacao = nota1.codigo ");
		sql.append(" left join NotaConceitoIndicadorAvaliacao nota2 on  criterioAvaliacaoNotaConceito2Bimestre.notaConceitoIndicadorAvaliacao = nota2.codigo ");
		sql.append(" left join NotaConceitoIndicadorAvaliacao nota3 on  criterioAvaliacaoNotaConceito3Bimestre.notaConceitoIndicadorAvaliacao = nota3.codigo ");
		sql.append(" left join NotaConceitoIndicadorAvaliacao nota4 on  criterioAvaliacaoNotaConceito4Bimestre.notaConceitoIndicadorAvaliacao = nota4.codigo ");
		sql.append(" left join Turma on  Turma.codigo = matriculaPeriodo.turma ");
		return sql;
	}

	private StringBuilder getSqlBasicoCriterioAvaliacaoRespondido() {
		StringBuilder sql = new StringBuilder("SELECT distinct CriterioAvaliacaoAluno.criterioAvaliacao, CriterioAvaliacaoAluno.ano, CriterioAvaliacaoAluno.semestre, CriterioAvaliacaoAluno.matriculaPeriodo, CriterioAvaliacaoAluno.criterioAvaliacaoPeriodoLetivo, pessoa.nome as \"pessoa.nome\", turma.identificadorturma as \"turma.identificadorturma\", turma.codigo as \"turma.codigo\", unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\",  ");
		sql.append("  Matricula.matricula as \"matricula.matricula\", Matricula.situacao as \"matricula.situacao\", matriculaperiodo.situacaomatriculaperiodo as \"matriculaperiodo.situacaomatriculaperiodo\", matriculaperiodo.observacaocriterioavaliacao as \"matriculaperiodo.observacaocriterioavaliacao\",  ");
		sql.append("  GradeCurricular.codigo as \"GradeCurricular.codigo\", GradeCurricular.nome as \"GradeCurricular.nome\",  ");
		sql.append("  curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		sql.append("  matriculaperiodo.ano as \"matriculaperiodo.ano\", matriculaperiodo.semestre as \"matriculaperiodo.semestre\", ");
		// Dados Criterio Periodo Letivo
		sql.append("  CriterioAvaliacaoPeriodoLetivo.codigo as \"criterioAvaliacaoPeriodoLetivo.codigo\",  ");
		// Dados Periodo Letivo
		sql.append(" periodoLetivo.codigo as \"periodoLetivo.codigo\", periodoLetivo.descricao as \"periodoLetivo.descricao\", periodoLetivo.periodoLetivo as \"periodoLetivo.periodoLetivo\" ");

		sql.append(" FROM CriterioAvaliacaoAluno ");
		sql.append(" inner join MatriculaPeriodo on  MatriculaPeriodo.codigo = CriterioAvaliacaoAluno.matriculaPeriodo ");
		sql.append(" inner join Matricula on  Matricula.matricula = matriculaPeriodo.matricula ");
		sql.append(" inner join Curso on  Matricula.Curso = Curso.codigo ");
		sql.append(" inner join Pessoa on  Pessoa.codigo = matricula.aluno ");
		sql.append(" inner join UnidadeEnsino on  UnidadeEnsino.codigo = matricula.unidadeEnsino ");
		sql.append(" inner join CriterioAvaliacao on  CriterioAvaliacao.codigo = CriterioAvaliacaoAluno.CriterioAvaliacao ");
		sql.append(" inner join GradeCurricular on  CriterioAvaliacao.GradeCurricular = GradeCurricular.codigo ");
		sql.append(" inner join CriterioAvaliacaoPeriodoLetivo on  CriterioAvaliacaoPeriodoLetivo.codigo = CriterioAvaliacaoAluno.criterioAvaliacaoPeriodoLetivo ");
		sql.append(" inner join PeriodoLetivo on  CriterioAvaliacaoPeriodoLetivo.periodoLetivo = periodoLetivo.codigo ");
		sql.append(" left join Turma on  Turma.codigo = matriculaPeriodo.turma ");
		return sql;
	}

	@Override
	public List<CriterioAvaliacaoVO> consultarCriterioAvaliacaoAlunoRespondido(String consultarPor, Integer matriculaPeriodo, String matricula, Integer disciplina, Integer turma, String ano, String semestre, Integer unidadeEnsino, Integer gradeCurricular, boolean ordenarVisaoPais, boolean visaoProfessor, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdentidade(usuarioVO), verificarAcesso, usuarioVO);
		StringBuilder sql = nivelMontarDados.equals(NivelMontarDados.TODOS) ? getSqlCompletoCriterioAvaliacaoRespondido() : getSqlBasicoCriterioAvaliacaoRespondido();
		sql.append(" where 1 = 1 ");
		sql.append(" and MatriculaPeriodo.situacaoMatriculaPeriodo  in ('AT', 'CO', 'FI', 'FO') ");
		if (ano != null && !ano.trim().isEmpty()) {
		sql.append(" and CriterioAvaliacaoAluno.ano = '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and CriterioAvaliacaoAluno.semestre = '").append(semestre).append("' ");
		}
		if (matricula != null && !matricula.trim().isEmpty() && consultarPor.equals("matricula")) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		if (matriculaPeriodo != null && matriculaPeriodo > 0 && consultarPor.equals("matriculaPeriodo")) {
			sql.append(" and matriculaPeriodo.codigo = ").append(matriculaPeriodo).append(" ");
		}
		if (disciplina != null && disciplina > 0 && visaoProfessor) {
			if (Uteis.isAtributoPreenchido(matriculaPeriodo)) {
				sql.append(" and  ((CriterioAvaliacaoAluno.origemcriterioavaliacaoindicador = 'GERAL') ");
				sql.append(" or (CriterioAvaliacaoAluno.origemcriterioavaliacaoindicador = 'DISCIPLINA' and historico.disciplina = ").append(disciplina);
			} else {
				sql.append(" and (disciplina.codigo = ").append(disciplina).append(" and ( historico.disciplina =  ").append(disciplina);
			}
			if (ano != null && !ano.trim().isEmpty()) {
				sql.append(" and historico.anohistorico = '").append(ano).append("' ))");
			} else {
				sql.append(" )) ");
			}
		}
		
		if (turma != null && turma > 0 && consultarPor.equals("turma")) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (gradeCurricular != null && gradeCurricular > 0) {
			sql.append(" and CriterioAvaliacao.gradeCurricular = ").append(gradeCurricular);
		}
		if (ordenarVisaoPais == true) {
			sql.append(" ORDER BY CriterioAvaliacaoAluno.ano DESC ");
		} else if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
			sql.append(" order by pessoa.nome, matricula.matricula, CriterioAvaliacaoAluno.criterioAvaliacao, matriculaperiodo.ano, matriculaperiodo.semestre, periodoLetivo.periodoLetivo, CriterioAvaliacaoAluno.origemCriterioAvaliacaoIndicador,  CriterioAvaliacaoDisciplina.ordem, ");
			sql.append(" CriterioAvaliacaoDisciplinaEixoIndicador.ordem,  CriterioAvaliacaoIndicador.ordem ");
		} else {
			sql.append(" order by pessoa.nome, matricula.matricula, matriculaperiodo.ano, matriculaperiodo.semestre, CriterioAvaliacaoAluno.criterioAvaliacao, periodoLetivo.periodoLetivo ");
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosCompleto(rs, disciplina, nivelMontarDados, usuarioVO);
	}

	public String consultarProfessorComMaiorVinculoTurma(Integer matriculaPeriodo) throws Exception {
		StringBuilder sql = new StringBuilder("select count(horarioturmadiaitem.professor), horarioturmadiaitem.professor, pessoa.nome as nomeprofessor from horarioturmadiaitem  ");
		sql.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sql.append(" where horarioturmadia  in (select horarioturmadia.codigo from horarioturmadia inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");		
		sql.append(" inner join matriculaperiodo on matriculaperiodo.turma = horarioturma.turma where matriculaperiodo.codigo = ").append(matriculaPeriodo);		
		sql.append(" ) group by horarioturmadiaitem.professor , pessoa.nome ");		
		sql.append(" order by count(horarioturmadiaitem.professor) desc limit 1 ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		String professor = "";
		while (rs.next()) {
			professor = rs.getString("nomeProfessor");
		}
		return professor;
	}

	private List<CriterioAvaliacaoVO> montarDadosCompleto(SqlRowSet rs, Integer disciplina, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO ) throws Exception {
		List<CriterioAvaliacaoVO> criterioAvaliacaoVOs = new ArrayList<CriterioAvaliacaoVO>(0);
		CriterioAvaliacaoVO criterioAvaliacaoVO = null;
		CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO = null;
		CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO = null;
		CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO = null;
		CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO = null;
		Integer matriculaPeriodo = 0;
		Integer criterioAvaliacao = 0;
		Integer periodoLetivo = 0;
		Integer eixo = 0;
		Map<String, BoletimAcademicoRelVO> matriculaAnoSemestreBoletimAcademicoRelVO = new HashMap<>();
		while (rs.next()) {
			if (matriculaPeriodo == 0 || !matriculaPeriodo.equals(rs.getInt("matriculaPeriodo")) || !criterioAvaliacao.equals(rs.getInt("criterioAvaliacao"))) {
				criterioAvaliacaoVO = montarDadosCriterioAvaliacaoRespondido(rs);
				criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs().add(getFacadeFactory().getCriterioAvaliacaoPeriodoLetivoFacade().consultarPorChavePrimaria(rs.getInt("criterioAvaliacaoPeriodoLetivo"), disciplina, Uteis.NIVELMONTARDADOS_TODOS));
				criterioAvaliacaoVOs.add(criterioAvaliacaoVO);
				matriculaPeriodo = rs.getInt("matriculaPeriodo");
				criterioAvaliacao = rs.getInt("criterioAvaliacao");
				periodoLetivo = 0;
				disciplina = 0;
				eixo = 0;
			}
			if (periodoLetivo == 0 || !periodoLetivo.equals(rs.getInt("periodoLetivo.codigo"))) {
				criterioAvaliacaoPeriodoLetivoVO = montarDadosCriterioAvaliacaoPeriodoLetivoRespondido(rs, criterioAvaliacaoVO);
				// criterioAvaliacaoPeriodoLetivoVO.setCriterioAvaliacaoVO(criterioAvaliacaoVO);
				// criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs().add(criterioAvaliacaoPeriodoLetivoVO);
				periodoLetivo = rs.getInt("periodoLetivo.codigo");
			}
			if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
				if ((disciplina == 0 || !disciplina.equals(rs.getInt("disciplina.codigo"))) && OrigemCriterioAvaliacaoIndicadorEnum.valueOf(rs.getString("origemCriterioAvaliacaoIndicador")).equals(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA)) {
					if (Uteis.isAtributoPreenchido(criterioAvaliacaoPeriodoLetivoVO)) {
						criterioAvaliacaoDisciplinaVO = montarDadosCriterioAvaliacaoDisciplinaRespondido(rs, criterioAvaliacaoPeriodoLetivoVO);
					}
					// criterioAvaliacaoDisciplinaVO.setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
					// criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().add(criterioAvaliacaoDisciplinaVO);
					disciplina = rs.getInt("disciplina.codigo");
				}
				if ((eixo == 0 || !eixo.equals(rs.getInt("criterioAvaliacaoDisciplinaEixoIndicador"))) && OrigemCriterioAvaliacaoIndicadorEnum.valueOf(rs.getString("origemCriterioAvaliacaoIndicador")).equals(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA)) {
					if (Uteis.isAtributoPreenchido(criterioAvaliacaoDisciplinaVO)) {
						criterioAvaliacaoDisciplinaEixoIndicadorVO = montarDadosCriterioAvaliacaoDisciplinaEixoIndicadorRespondido(rs, criterioAvaliacaoDisciplinaVO);
						eixo = rs.getInt("criterioAvaliacaoDisciplinaEixoIndicador");
					}
					// criterioAvaliacaoDisciplinaEixoIndicadorVO.setCriterioAvaliacaoDisciplina(criterioAvaliacaoDisciplinaVO);
					// criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().add(criterioAvaliacaoDisciplinaEixoIndicadorVO);
				}
				criterioAvaliacaoIndicadorVO = montarDadosCriterioAvaliacaoIndicadorRespondido(rs, usuarioVO);
				String matricula = rs.getString("matricula.matricula");
				StringBuilder key = new StringBuilder().append(matricula).append("-").append(rs.getString("ano")).append("-").append(rs.getString("semestre"));
				if (matriculaAnoSemestreBoletimAcademicoRelVO.containsKey(key.toString())) {
					BoletimAcademicoRelVO boletimAcademicoRelVO = matriculaAnoSemestreBoletimAcademicoRelVO.get(key.toString());
					criterioAvaliacaoIndicadorVO.setTotalFalta1Bimestre(boletimAcademicoRelVO.getTotalFaltaPrimeiroBimestre());
					criterioAvaliacaoIndicadorVO.setTotalFalta2Bimestre(boletimAcademicoRelVO.getTotalFaltaSegundoBimestre());
					criterioAvaliacaoIndicadorVO.setTotalFalta3Bimestre(boletimAcademicoRelVO.getTotalFaltaTerceiroBimestre());
					criterioAvaliacaoIndicadorVO.setTotalFalta4Bimestre(boletimAcademicoRelVO.getTotalFaltaQuartoBimestre());
					criterioAvaliacaoIndicadorVO.setTotalFaltaGeral(boletimAcademicoRelVO.getTotalFaltaPrimeiroBimestre() + boletimAcademicoRelVO.getTotalFaltaSegundoBimestre() + boletimAcademicoRelVO.getTotalFaltaTerceiroBimestre() + boletimAcademicoRelVO.getTotalFaltaQuartoBimestre());
				} else {
					BoletimAcademicoRelVO boletim = new BoletimAcademicoRelVO();
					boletim.setMatricula(matricula);
					List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaAluno(rs.getInt("unidadeEnsino.codigo"), "", matricula, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO, rs.getString("ano"), rs.getString("semestre"));
					getFacadeFactory().getBoletimAcademicoRelFacade().realizarConsultaRegistroPorMatriculaAnoSemestre(boletim, rs.getString("ano"), rs.getString("semestre"), null, disciplinaVOs, usuarioVO);
					matriculaAnoSemestreBoletimAcademicoRelVO.put(key.toString(), boletim);
					criterioAvaliacaoIndicadorVO.setTotalFalta1Bimestre(boletim.getTotalFaltaPrimeiroBimestre());
					criterioAvaliacaoIndicadorVO.setTotalFalta2Bimestre(boletim.getTotalFaltaSegundoBimestre());
					criterioAvaliacaoIndicadorVO.setTotalFalta3Bimestre(boletim.getTotalFaltaTerceiroBimestre());
					criterioAvaliacaoIndicadorVO.setTotalFalta4Bimestre(boletim.getTotalFaltaQuartoBimestre());
					criterioAvaliacaoIndicadorVO.setTotalFaltaGeral(boletim.getTotalFaltaPrimeiroBimestre() + boletim.getTotalFaltaSegundoBimestre() + boletim.getTotalFaltaTerceiroBimestre() + boletim.getTotalFaltaQuartoBimestre());
				}
				if (OrigemCriterioAvaliacaoIndicadorEnum.valueOf(rs.getString("origemCriterioAvaliacaoIndicador")).equals(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA)) {
					if (Uteis.isAtributoPreenchido(criterioAvaliacaoDisciplinaEixoIndicadorVO)) {
						for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO2 : criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
							if (criterioAvaliacaoIndicadorVO2.getCodigo().equals(criterioAvaliacaoIndicadorVO.getCodigo())) {
								criterioAvaliacaoIndicadorVO2.setTotalFalta1Bimestre(criterioAvaliacaoIndicadorVO.getTotalFalta1Bimestre());
								criterioAvaliacaoIndicadorVO2.setTotalFalta2Bimestre(criterioAvaliacaoIndicadorVO.getTotalFalta2Bimestre());
								criterioAvaliacaoIndicadorVO2.setTotalFalta3Bimestre(criterioAvaliacaoIndicadorVO.getTotalFalta3Bimestre());
								criterioAvaliacaoIndicadorVO2.setTotalFalta4Bimestre(criterioAvaliacaoIndicadorVO.getTotalFalta4Bimestre());
								criterioAvaliacaoIndicadorVO2.setTotalFaltaGeral(criterioAvaliacaoIndicadorVO.getTotalFaltaGeral());
								criterioAvaliacaoIndicadorVO2.setCriterioAvaliacaoAlunoVO(criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO());
								break;
							}
						}
					}
					
					// criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs().add(criterioAvaliacaoIndicadorVO);
				} else if (OrigemCriterioAvaliacaoIndicadorEnum.valueOf(rs.getString("origemCriterioAvaliacaoIndicador")).equals(OrigemCriterioAvaliacaoIndicadorEnum.GERAL)) {
					if (Uteis.isAtributoPreenchido(criterioAvaliacaoPeriodoLetivoVO)) {
						for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO2 : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
							if (criterioAvaliacaoIndicadorVO2.getCodigo().equals(criterioAvaliacaoIndicadorVO.getCodigo())) {
								criterioAvaliacaoIndicadorVO2.setTotalFalta1Bimestre(criterioAvaliacaoIndicadorVO.getTotalFalta1Bimestre());
								criterioAvaliacaoIndicadorVO2.setTotalFalta2Bimestre(criterioAvaliacaoIndicadorVO.getTotalFalta2Bimestre());
								criterioAvaliacaoIndicadorVO2.setTotalFalta3Bimestre(criterioAvaliacaoIndicadorVO.getTotalFalta3Bimestre());
								criterioAvaliacaoIndicadorVO2.setTotalFalta4Bimestre(criterioAvaliacaoIndicadorVO.getTotalFalta4Bimestre());
								criterioAvaliacaoIndicadorVO2.setTotalFaltaGeral(criterioAvaliacaoIndicadorVO.getTotalFaltaGeral());
								criterioAvaliacaoIndicadorVO2.setCriterioAvaliacaoAlunoVO(criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO());
								break;
							}
						}
					}
					
					// criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs().add(criterioAvaliacaoIndicadorVO);
				}
			}
		}

		return criterioAvaliacaoVOs;
	}

	private CriterioAvaliacaoVO montarDadosCriterioAvaliacaoRespondido(SqlRowSet rs) throws Exception {
		CriterioAvaliacaoVO criterioAvaliacaoVO = new CriterioAvaliacaoVO();
		criterioAvaliacaoVO.setCodigo(rs.getInt("criterioAvaliacao"));
		criterioAvaliacaoVO.setCriterioAvaliacaoAlunoRespondido(true);

		criterioAvaliacaoVO.getMatriculaPeriodoVO().setCodigo(rs.getInt("matriculaPeriodo"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().setAno(rs.getString("ano"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().setSemestre(rs.getString("semestre"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().getTurma().setCodigo(rs.getInt("turma.codigo"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().setMatricula(rs.getString("matricula.matricula"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(rs.getString("matriculaPeriodo.situacaoMatriculaPeriodo"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().setObservacaoCriterioAvaliacao(rs.getString("matriculaperiodo.observacaocriterioavaliacao"));
		criterioAvaliacaoVO.getMatriculaVO().setMatricula(rs.getString("matricula.matricula"));
		criterioAvaliacaoVO.getMatriculaVO().getAluno().setNome(rs.getString("pessoa.nome"));
		criterioAvaliacaoVO.getMatriculaVO().setSituacao(rs.getString("matricula.situacao"));
		criterioAvaliacaoVO.setCodigo(rs.getInt("criterioAvaliacao"));
		criterioAvaliacaoVO.getGradeCurricularVO().setCodigo(rs.getInt("gradeCurricular.codigo"));
		criterioAvaliacaoVO.getGradeCurricularVO().setNome(rs.getString("gradeCurricular.nome"));
		criterioAvaliacaoVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino.codigo"));
		criterioAvaliacaoVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
		criterioAvaliacaoVO.getMatriculaVO().getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino.codigo"));
		criterioAvaliacaoVO.getMatriculaVO().getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().getPeridoLetivo().setCodigo(rs.getInt("periodoLetivo.codigo"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(rs.getInt("periodoLetivo.periodoLetivo"));
		criterioAvaliacaoVO.getMatriculaPeriodoVO().getPeridoLetivo().setDescricao(rs.getString("periodoLetivo.descricao"));
		criterioAvaliacaoVO.getCurso().setCodigo(rs.getInt("curso.codigo"));
		criterioAvaliacaoVO.getCurso().setNome(rs.getString("curso.nome"));
		criterioAvaliacaoVO.getMatriculaVO().getCurso().setCodigo(rs.getInt("curso.codigo"));
		criterioAvaliacaoVO.getMatriculaVO().getCurso().setNome(rs.getString("curso.nome"));
		criterioAvaliacaoVO.setNovoObj(false);
		return criterioAvaliacaoVO;

	}

	private CriterioAvaliacaoPeriodoLetivoVO montarDadosCriterioAvaliacaoPeriodoLetivoRespondido(SqlRowSet rs, CriterioAvaliacaoVO criterioAvaliacaoVO) throws Exception {
		for (CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO : criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs()) {
			if (criterioAvaliacaoPeriodoLetivoVO.getCodigo().intValue() == rs.getInt("criterioAvaliacaoPeriodoLetivo")) {
				return criterioAvaliacaoPeriodoLetivoVO;
			}
		}
		// CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO = new CriterioAvaliacaoPeriodoLetivoVO();
		// criterioAvaliacaoPeriodoLetivoVO.setCodigo(rs.getInt("criterioAvaliacaoPeriodoLetivo"));
		// criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setCodigo(rs.getInt("periodoLetivo.codigo"));
		// criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setPeriodoLetivo(rs.getInt("periodoLetivo.periodoLetivo"));
		// criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setDescricao(rs.getString("periodoLetivo.descricao"));
		// criterioAvaliacaoPeriodoLetivoVO.setCriterioAvaliacaoNotaConceitoVOs(getFacadeFactory().getCriterioAvaliacaoNotaConceitoFacade().consultarPorCriterioAvaliacaoPeriodoLetivo(rs.getInt("criterioAvaliacaoPeriodoLetivo")));
		// criterioAvaliacaoPeriodoLetivoVO.setNovoObj(false);
		// return criterioAvaliacaoPeriodoLetivoVO;
		return null;

	}

	private CriterioAvaliacaoDisciplinaVO montarDadosCriterioAvaliacaoDisciplinaRespondido(SqlRowSet rs, CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
			if (criterioAvaliacaoDisciplinaVO.getCodigo().intValue() == rs.getInt("criterioAvaliacaoDisciplina")) {
				return criterioAvaliacaoDisciplinaVO;
			}
		}
		// CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO = new CriterioAvaliacaoDisciplinaVO();
		// criterioAvaliacaoDisciplinaVO.setCodigo(rs.getInt("criterioAvaliacaoDisciplina"));
		// criterioAvaliacaoDisciplinaVO.setOrdem(rs.getInt("criterioAvaliacaoDisciplina.ordem"));
		// criterioAvaliacaoDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
		// criterioAvaliacaoDisciplinaVO.getDisciplina().setNome(rs.getString("disciplina.nome"));
		// criterioAvaliacaoDisciplinaVO.setNovoObj(false);
		// return criterioAvaliacaoDisciplinaVO;
		return null;

	}

	private CriterioAvaliacaoDisciplinaEixoIndicadorVO montarDadosCriterioAvaliacaoDisciplinaEixoIndicadorRespondido(SqlRowSet rs, CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception {
		for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
			if (criterioAvaliacaoDisciplinaEixoIndicadorVO.getCodigo().intValue() == rs.getInt("criterioAvaliacaoDisciplinaEixoIndicador")) {
				return criterioAvaliacaoDisciplinaEixoIndicadorVO;
			}
		}
		// CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO = new CriterioAvaliacaoDisciplinaEixoIndicadorVO();
		// criterioAvaliacaoDisciplinaEixoIndicadorVO.setCodigo(rs.getInt("criterioAvaliacaoDisciplinaEixoIndicador"));
		// criterioAvaliacaoDisciplinaEixoIndicadorVO.setOrdem(rs.getInt("criterioAvaliacaoDisciplinaEixoIndicador.ordem"));
		// criterioAvaliacaoDisciplinaEixoIndicadorVO.setEixoIndicador(rs.getString("criterioAvaliacaoDisciplinaEixoIndicador.eixoIndicador"));
		// criterioAvaliacaoDisciplinaEixoIndicadorVO.setNota(rs.getDouble("criterioAvaliacaoDisciplinaEixoIndicador.nota"));
		// criterioAvaliacaoDisciplinaEixoIndicadorVO.setNovoObj(false);
		// return criterioAvaliacaoDisciplinaEixoIndicadorVO;
		return null;

	}

	private CriterioAvaliacaoIndicadorVO montarDadosCriterioAvaliacaoIndicadorRespondido(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception {
		CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO = new CriterioAvaliacaoIndicadorVO();
		criterioAvaliacaoIndicadorVO.setCodigo(rs.getInt("criterioAvaliacaoIndicador.codigo"));
		criterioAvaliacaoIndicadorVO.setOrdem(rs.getInt("criterioAvaliacaoIndicador.ordem"));
		criterioAvaliacaoIndicadorVO.setDescricao(rs.getString("criterioAvaliacaoIndicador.descricao"));
		criterioAvaliacaoIndicadorVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.valueOf(rs.getString("criterioAvaliacaoIndicador.origemCriterioAvaliacaoIndicador")));
		criterioAvaliacaoIndicadorVO.setAvaliarPrimeiroBimestre(AvaliarNaoAvaliarEnum.valueOf(rs.getString("criterioAvaliacaoIndicador.avaliarPrimeiroBimestre")));
		criterioAvaliacaoIndicadorVO.setAvaliarSegundoBimestre(AvaliarNaoAvaliarEnum.valueOf(rs.getString("criterioAvaliacaoIndicador.avaliarSegundoBimestre")));
		criterioAvaliacaoIndicadorVO.setAvaliarTerceiroBimestre(AvaliarNaoAvaliarEnum.valueOf(rs.getString("criterioAvaliacaoIndicador.avaliarTerceiroBimestre")));
		criterioAvaliacaoIndicadorVO.setAvaliarQuartoBimestre(AvaliarNaoAvaliarEnum.valueOf(rs.getString("criterioAvaliacaoIndicador.avaliarQuartoBimestre")));
		criterioAvaliacaoIndicadorVO.setNota1Bimestre(rs.getDouble("criterioAvaliacaoIndicador.nota1Bimestre"));
		criterioAvaliacaoIndicadorVO.setNota2Bimestre(rs.getDouble("criterioAvaliacaoIndicador.nota2Bimestre"));
		criterioAvaliacaoIndicadorVO.setNota3Bimestre(rs.getDouble("criterioAvaliacaoIndicador.nota3Bimestre"));
		criterioAvaliacaoIndicadorVO.setNota4Bimestre(rs.getDouble("criterioAvaliacaoIndicador.nota4Bimestre"));
		
		criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoDisciplinaEixoIndicador().setCodigo(rs.getInt("criterioAvaliacaoDisciplinaEixoIndicador"));
		criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoPeriodoLetivo().setCodigo(rs.getInt("criterioAvaliacaoPeriodoLetivo"));
		criterioAvaliacaoIndicadorVO.setNovoObj(false);

		CriterioAvaliacaoAlunoVO criterioAvaliacaoAlunoVO = new CriterioAvaliacaoAlunoVO();
		criterioAvaliacaoAlunoVO.setNovoObj(false);
		criterioAvaliacaoAlunoVO.setCodigo(rs.getInt("codigo"));
		criterioAvaliacaoAlunoVO.setAno(rs.getString("ano"));
		criterioAvaliacaoAlunoVO.setSemestre(rs.getString("semestre"));
		criterioAvaliacaoAlunoVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.valueOf(rs.getString("origemCriterioAvaliacaoIndicador")));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacao().setCodigo(rs.getInt("criterioAvaliacao"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoPeriodoLetivo().setCodigo(rs.getInt("criterioAvaliacaoPeriodoLetivo"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoDisciplina().setCodigo(rs.getInt("criterioAvaliacaoDisciplina"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoDisciplinaEixoIndicador().setCodigo(rs.getInt("criterioAvaliacaoDisciplinaEixoIndicador"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoIndicador().setCodigo(rs.getInt("criterioAvaliacaoIndicador"));

		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().setCodigo(rs.getInt("criterioAvaliacaoNotaConceito1Bimestre"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().setPeso(rs.getDouble("criterioAvaliacaoNotaConceito1Bimestre.peso"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().setCodigo(rs.getInt("nota1.codigo"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().setDescricao(rs.getString("nota1.descricao"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().setNomeArquivo(rs.getString("nota1.nomeArquivo"));

		if (rs.getString("nota1.pastaBaseArquivo") != null && !rs.getString("nota1.pastaBaseArquivo").trim().isEmpty()) {
			criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().setPastaBaseArquivo(PastaBaseArquivoEnum.valueOf(rs.getString("nota1.pastaBaseArquivo")));
		}

		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().setCodigo(rs.getInt("criterioAvaliacaoNotaConceito2Bimestre"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().setPeso(rs.getDouble("criterioAvaliacaoNotaConceito2Bimestre.peso"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().setCodigo(rs.getInt("nota2.codigo"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().setDescricao(rs.getString("nota2.descricao"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().setNomeArquivo(rs.getString("nota2.nomeArquivo"));
		if (rs.getString("nota2.pastaBaseArquivo") != null && !rs.getString("nota2.pastaBaseArquivo").trim().isEmpty()) {
			criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().setPastaBaseArquivo(PastaBaseArquivoEnum.valueOf(rs.getString("nota2.pastaBaseArquivo")));
		}

		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().setCodigo(rs.getInt("criterioAvaliacaoNotaConceito3Bimestre"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().setPeso(rs.getDouble("criterioAvaliacaoNotaConceito3Bimestre.peso"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().setCodigo(rs.getInt("nota3.codigo"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().setDescricao(rs.getString("nota3.descricao"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().setNomeArquivo(rs.getString("nota3.nomeArquivo"));
		if (rs.getString("nota3.pastaBaseArquivo") != null && !rs.getString("nota3.pastaBaseArquivo").trim().isEmpty()) {
			criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().setPastaBaseArquivo(PastaBaseArquivoEnum.valueOf(rs.getString("nota3.pastaBaseArquivo")));
		}

		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().setCodigo(rs.getInt("criterioAvaliacaoNotaConceito4Bimestre"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().setPeso(rs.getDouble("criterioAvaliacaoNotaConceito4Bimestre.peso"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().setCodigo(rs.getInt("nota4.codigo"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().setDescricao(rs.getString("nota4.descricao"));
		criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().setNomeArquivo(rs.getString("nota4.nomeArquivo"));
		if (rs.getString("nota4.pastaBaseArquivo") != null && !rs.getString("nota4.pastaBaseArquivo").trim().isEmpty()) {
			criterioAvaliacaoAlunoVO.getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().setPastaBaseArquivo(PastaBaseArquivoEnum.valueOf(rs.getString("nota4.pastaBaseArquivo")));
		}

		criterioAvaliacaoIndicadorVO.setCriterioAvaliacaoAlunoVO(criterioAvaliacaoAlunoVO);
		return criterioAvaliacaoIndicadorVO;

	}

	public static String getIdentidade(UsuarioVO usuarioVO) {
		if (usuarioVO == null || usuarioVO.getIsApresentarVisaoAdministrativa()) {
			return "CriterioAvaliacaoAluno";
		} else if (usuarioVO.getIsApresentarVisaoProfessor()) {
			return "CriterioAvaliacaoAlunoVisaoProfessor";
		} else if (usuarioVO.getIsApresentarVisaoCoordenador()) {
			return "CriterioAvaliacaoAlunoVisaoCoordenador";
		} else if (usuarioVO.getIsApresentarVisaoAluno()) {
			return "CriterioAvaliacaoAlunoVisaoAluno";
		} else if (usuarioVO.getIsApresentarVisaoPais()) {
			return "CriterioAvaliacaoAlunoVisaoPais";
		} else {
			return "CriterioAvaliacaoAluno";
		}

	}

	@Override
	public void selecionarNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, Integer criterioAvaliacaoDisciplina, Integer criterioAvaliacaoEixoIndicador, Integer criterioAvaliacaoIndicador, String origem, Integer nota, Integer criterioAvaliacaoNotaConceito) throws Exception {
		CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO = new CriterioAvaliacaoNotaConceitoVO();
		if (criterioAvaliacaoNotaConceito != null && criterioAvaliacaoNotaConceito > 0) {
			for (CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO2 : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs()) {
				if (criterioAvaliacaoNotaConceitoVO2.getCodigo().equals(criterioAvaliacaoNotaConceito)) {
					criterioAvaliacaoNotaConceitoVO = criterioAvaliacaoNotaConceitoVO2;
				}
			}
		}
		if (OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA.name().equals(origem)) {
			for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
				if (criterioAvaliacaoDisciplinaVO.getCodigo().equals(criterioAvaliacaoDisciplina)) {
					for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
						if (criterioAvaliacaoDisciplinaEixoIndicadorVO.getCodigo().equals(criterioAvaliacaoEixoIndicador)) {
							for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
								if (criterioAvaliacaoIndicadorVO.getCodigo().equals(criterioAvaliacaoIndicador)) {
									if (nota.equals(1)) {
										criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(criterioAvaliacaoNotaConceitoVO);
									} else if (nota.equals(2)) {
										criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(criterioAvaliacaoNotaConceitoVO);
									} else if (nota.equals(3)) {
										criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(criterioAvaliacaoNotaConceitoVO);
									} else if (nota.equals(4)) {
										criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(criterioAvaliacaoNotaConceitoVO);
									}
								}
							}
						}
					}
				}
			}
		} else {
			for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
				if (criterioAvaliacaoIndicadorVO.getCodigo().equals(criterioAvaliacaoIndicador)) {
					if (nota.equals(1)) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(criterioAvaliacaoNotaConceitoVO);
					} else if (nota.equals(2)) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(criterioAvaliacaoNotaConceitoVO);
					} else if (nota.equals(3)) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(criterioAvaliacaoNotaConceitoVO);
					} else if (nota.equals(4)) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(criterioAvaliacaoNotaConceitoVO);
					}
				}
			}
		}

	}

	@Override
	public void realizacaoCriacaoOpcaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
			for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
				for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
					if (!criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo().isEmpty()) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
					}
					if (!criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo().isEmpty()) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
					}
					if (!criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo().isEmpty()) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
					}
					if (!criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo().isEmpty()) {
						criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
					}
				}
			}
		}

		for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (!criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo().isEmpty()) {
				criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito1Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
			}
			if (!criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo().isEmpty()) {
				criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito2Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
			}
			if (!criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo().isEmpty()) {
				criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito3Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
			}
			if (!criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo().isEmpty()) {
				criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoIndicadorVO.getCriterioAvaliacaoAlunoVO().getCriterioAvaliacaoNotaConceito4Bimestre().getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
			}
		}

	}

	public void validarDadosImpressaoRelatorio(Integer unidadeEnsino, String opcaoConsulta, MatriculaVO matriculaVO, TurmaVO turmaVO, String ano, String semestre) {

	}
}
