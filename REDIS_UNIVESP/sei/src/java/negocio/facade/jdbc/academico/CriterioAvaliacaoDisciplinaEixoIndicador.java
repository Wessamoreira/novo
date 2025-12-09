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

import negocio.comuns.academico.CriterioAvaliacaoDisciplinaEixoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoDisciplinaVO;
import negocio.comuns.academico.CriterioAvaliacaoIndicadorVO;
import negocio.comuns.academico.enumeradores.AvaliarNaoAvaliarEnum;
import negocio.comuns.academico.enumeradores.OrigemCriterioAvaliacaoIndicadorEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CriterioAvaliacaoDisciplinaEixoIndicadorIntefaceFacade;

@Repository
@Lazy
public class CriterioAvaliacaoDisciplinaEixoIndicador extends ControleAcesso implements CriterioAvaliacaoDisciplinaEixoIndicadorIntefaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1044659613782412606L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		validarDados(criterioAvaliacaoDisciplinaEixoIndicadorVO);
		try {
			criterioAvaliacaoDisciplinaEixoIndicadorVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO criterioAvaliacaoDisciplinaEixoIndicador (");
					sql.append(" criterioAvaliacaoDisciplina, eixoIndicador, ordem, nota ) ");
					sql.append(" VALUES (? , ?, ?, ?) returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoDisciplina().getCodigo());
					ps.setString(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getEixoIndicador());
					ps.setInt(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem());
					ps.setDouble(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getNota());
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
			getFacadeFactory().getCriterioAvaliacaoIndicadorFacade().incluirCriterioAvaliacaoIndicadorPorEixoIndicadorVO(criterioAvaliacaoDisciplinaEixoIndicadorVO);
			criterioAvaliacaoDisciplinaEixoIndicadorVO.setNovoObj(false);
		} catch (Exception e) {
			criterioAvaliacaoDisciplinaEixoIndicadorVO.setNovoObj(true);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		validarDados(criterioAvaliacaoDisciplinaEixoIndicadorVO);
		try {
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE criterioAvaliacaoDisciplinaEixoIndicador SET ");
					sql.append(" criterioAvaliacaoDisciplina = ?, eixoIndicador = ?, ordem = ?, nota = ? ");
					sql.append(" WHERE codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoDisciplina().getCodigo());
					ps.setString(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getEixoIndicador());
					ps.setInt(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem());
					ps.setDouble(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getNota());
					ps.setInt(x++, criterioAvaliacaoDisciplinaEixoIndicadorVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(criterioAvaliacaoDisciplinaEixoIndicadorVO);
				return;
			}
			getFacadeFactory().getCriterioAvaliacaoIndicadorFacade().alterarCriterioAvaliacaoIndicadorPorEixoIndicadorVO(criterioAvaliacaoDisciplinaEixoIndicadorVO);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void incluirCriterioAvaliacaoDisciplinaEixoIndicador(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception {
		for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
			criterioAvaliacaoDisciplinaEixoIndicadorVO.setCriterioAvaliacaoDisciplina(criterioAvaliacaoDisciplinaVO);
			incluir(criterioAvaliacaoDisciplinaEixoIndicadorVO);
		}

	}

	@Override
	public void alterarCriterioAvaliacaoDisciplinaEixoIndicador(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception {
		excluirCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaVO);
		for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
			criterioAvaliacaoDisciplinaEixoIndicadorVO.setCriterioAvaliacaoDisciplina(criterioAvaliacaoDisciplinaVO);
			if (criterioAvaliacaoDisciplinaEixoIndicadorVO.getNovoObj()) {
				incluir(criterioAvaliacaoDisciplinaEixoIndicadorVO);
			} else {
				alterar(criterioAvaliacaoDisciplinaEixoIndicadorVO);
			}
		}

	}

	@Override
	public void excluirCriterioAvaliacaoDisciplinaEixoIndicador(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM criterioAvaliacaoDisciplinaEixoIndicador where criterioAvaliacaoDisciplina = ? and codigo not in (0 ");
		for (CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
			sql.append(", ").append(criterioAvaliacaoDisciplinaEixoIndicadorVO.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), criterioAvaliacaoDisciplinaVO.getCodigo());

	}

	@Override
	public void validarDados(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws ConsistirException {
		if (criterioAvaliacaoDisciplinaEixoIndicadorVO.getEixoIndicador().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoDisciplinaEixoIndicador_eixoIndicador"));
		}

	}

	@Override
	public List<CriterioAvaliacaoDisciplinaEixoIndicadorVO> consultarPorCriterioAvaliacaoDisciplina(Integer criterioAvaliacaoDisciplina, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder("select * from CriterioAvaliacaoDisciplinaEixoIndicador where CriterioAvaliacaoDisciplina = ? order by ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), criterioAvaliacaoDisciplina), nivelMontarDados);
	}

	private List<CriterioAvaliacaoDisciplinaEixoIndicadorVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception {
		List<CriterioAvaliacaoDisciplinaEixoIndicadorVO> criterioAvaliacaoDisciplinaEixoIndicadorVOs = new ArrayList<CriterioAvaliacaoDisciplinaEixoIndicadorVO>(0);
		while (rs.next()) {
			criterioAvaliacaoDisciplinaEixoIndicadorVOs.add(montarDados(rs, nivelMontarDados));
		}
		return criterioAvaliacaoDisciplinaEixoIndicadorVOs;
	}

	private CriterioAvaliacaoDisciplinaEixoIndicadorVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception {
		CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO = new CriterioAvaliacaoDisciplinaEixoIndicadorVO();
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setNovoObj(false);
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setEixoIndicador(rs.getString("eixoIndicador"));
		criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoDisciplina().setCodigo(rs.getInt("criterioAvaliacaoDisciplina"));
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setOrdem(rs.getInt("ordem"));
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setCodigo(rs.getInt("codigo"));
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setNota(rs.getDouble("nota"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return criterioAvaliacaoDisciplinaEixoIndicadorVO;
		}
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setCriterioAvaliacaoIndicadorVOs(getFacadeFactory().getCriterioAvaliacaoIndicadorFacade().consultarPorCriterioAvalicaoDisciplinaPorEixoIndicador(criterioAvaliacaoDisciplinaEixoIndicadorVO.getCodigo()));

		return criterioAvaliacaoDisciplinaEixoIndicadorVO;
	}

	@Override
	public void alterarOrdemCriterioAvaliacaoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO, negocio.comuns.academico.CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO, boolean subir) throws Exception {
		int x = criterioAvaliacaoIndicadorVO.getOrdem();
		if (subir) {
			x--;
		} else {
			x++;
		}
		if (x == 0 || x > criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs().size()) {
			return;
		}
		criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs().get(x - 1).setOrdem(criterioAvaliacaoIndicadorVO.getOrdem());
		criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs().get(criterioAvaliacaoIndicadorVO.getOrdem() - 1).setOrdem(x);
		Ordenacao.ordenarLista(criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs(), "ordem");
	}

	@Override
	public void realizarCalculoQtdeIndicadorPorBimestre(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setQtdeIndicador1Bimestre(0);
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setQtdeIndicador2Bimestre(0);
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setQtdeIndicador3Bimestre(0);
		criterioAvaliacaoDisciplinaEixoIndicadorVO.setQtdeIndicador4Bimestre(0);
		for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (criterioAvaliacaoIndicadorVO.getAvaliarPrimeiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoDisciplinaEixoIndicadorVO.setQtdeIndicador1Bimestre(criterioAvaliacaoDisciplinaEixoIndicadorVO.getQtdeIndicador1Bimestre() + 1);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarSegundoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoDisciplinaEixoIndicadorVO.setQtdeIndicador2Bimestre(criterioAvaliacaoDisciplinaEixoIndicadorVO.getQtdeIndicador2Bimestre() + 1);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarTerceiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoDisciplinaEixoIndicadorVO.setQtdeIndicador3Bimestre(criterioAvaliacaoDisciplinaEixoIndicadorVO.getQtdeIndicador3Bimestre() + 1);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarQuartoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoDisciplinaEixoIndicadorVO.setQtdeIndicador4Bimestre(criterioAvaliacaoDisciplinaEixoIndicadorVO.getQtdeIndicador4Bimestre() + 1);
			}
		}
	}

	@Override
	public void realizarCalculoNotaCriterioAvaliacaoDisciplinaEixoIndicador(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		realizarCalculoQtdeIndicadorPorBimestre(criterioAvaliacaoDisciplinaEixoIndicadorVO);
		for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (criterioAvaliacaoIndicadorVO.getAvaliarPrimeiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoIndicadorVO.setNota1Bimestre(Uteis.arrendondarForcando4CadasDecimais(criterioAvaliacaoDisciplinaEixoIndicadorVO.getNota() / criterioAvaliacaoDisciplinaEixoIndicadorVO.getQtdeIndicador1Bimestre()));
			} else {
				criterioAvaliacaoIndicadorVO.setNota1Bimestre(0.0);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarSegundoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoIndicadorVO.setNota2Bimestre(Uteis.arrendondarForcando4CadasDecimais(criterioAvaliacaoDisciplinaEixoIndicadorVO.getNota() / criterioAvaliacaoDisciplinaEixoIndicadorVO.getQtdeIndicador2Bimestre()));
			} else {
				criterioAvaliacaoIndicadorVO.setNota2Bimestre(0.0);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarTerceiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoIndicadorVO.setNota3Bimestre(Uteis.arrendondarForcando4CadasDecimais(criterioAvaliacaoDisciplinaEixoIndicadorVO.getNota() / criterioAvaliacaoDisciplinaEixoIndicadorVO.getQtdeIndicador3Bimestre()));
			} else {
				criterioAvaliacaoIndicadorVO.setNota3Bimestre(0.0);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarQuartoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoIndicadorVO.setNota4Bimestre(Uteis.arrendondarForcando4CadasDecimais(criterioAvaliacaoDisciplinaEixoIndicadorVO.getNota() / criterioAvaliacaoDisciplinaEixoIndicadorVO.getQtdeIndicador4Bimestre()));
			} else {
				criterioAvaliacaoIndicadorVO.setNota4Bimestre(0.0);
			}
			
		}
	}

	@Override
	public void adicionarCriterioAvaliacaoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO, CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) throws Exception {
		getFacadeFactory().getCriterioAvaliacaoIndicadorFacade().validarDados(criterioAvaliacaoIndicadorVO);
		for (CriterioAvaliacaoIndicadorVO obj : criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (criterioAvaliacaoIndicadorVO.getOrdem() > 0 && obj.getDescricao().trim().equalsIgnoreCase(criterioAvaliacaoIndicadorVO.getDescricao()) && !obj.getOrdem().equals(criterioAvaliacaoIndicadorVO.getOrdem())) {
				criterioAvaliacaoIndicadorVO.setDescricao(criterioAvaliacaoIndicadorVO.getDescricaoAnt());
				throw new Exception(UteisJSF.internacionalizar("msg_CriterioAvaliacaoIndicador_unico"));
			} else if (obj.getDescricao().trim().equalsIgnoreCase(criterioAvaliacaoIndicadorVO.getDescricao())) {
				return;
			}
		}
		if (criterioAvaliacaoIndicadorVO.getOrdem().equals(0)) {
			criterioAvaliacaoIndicadorVO.setOrdem(criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs().size() + 1);
			criterioAvaliacaoIndicadorVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA);
			criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs().add(criterioAvaliacaoIndicadorVO);
			
		} else {
			criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs().set(criterioAvaliacaoIndicadorVO.getOrdem() - 1, criterioAvaliacaoIndicadorVO);
		}
		realizarCalculoNotaCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaEixoIndicadorVO);
	}

	@Override
	public void excluirCriterioAvaliacaoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO, CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) throws Exception {
		int x = 0;
		for (CriterioAvaliacaoIndicadorVO obj : criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (obj.getDescricao().trim().equalsIgnoreCase(criterioAvaliacaoIndicadorVO.getDescricao())) {
				criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs().remove(x);
				break;
			}
			x++;
		}
		realizaAltualizacaoOrdemApresentacaoIndicador(criterioAvaliacaoDisciplinaEixoIndicadorVO);
		realizarCalculoNotaCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaEixoIndicadorVO);
	}

	private void realizaAltualizacaoOrdemApresentacaoIndicador(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		int x = 1;
		for (CriterioAvaliacaoIndicadorVO obj : criterioAvaliacaoDisciplinaEixoIndicadorVO.getCriterioAvaliacaoIndicadorVOs()) {
			obj.setOrdem(x++);
		}
	}

}
