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
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CriterioAvaliacaoDisciplinaInterfaceFacade;

@Repository
@Lazy
public class CriterioAvaliacaoDisciplina extends ControleAcesso implements CriterioAvaliacaoDisciplinaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5501601144578422357L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception {
		validarDados(criterioAvaliacaoDisciplinaVO);
		try {
			criterioAvaliacaoDisciplinaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO criterioAvaliacaoDisciplina (");
					sql.append(" criterioAvaliacaoPeriodoLetivo, disciplina, ordem ) ");
					sql.append(" VALUES (? , ?, ?) returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo());
					ps.setInt(x++, criterioAvaliacaoDisciplinaVO.getDisciplina().getCodigo());
					ps.setInt(x++, criterioAvaliacaoDisciplinaVO.getOrdem());
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
			getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().incluirCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaVO);
			criterioAvaliacaoDisciplinaVO.setNovoObj(false);
		} catch (Exception e) {
			criterioAvaliacaoDisciplinaVO.setNovoObj(true);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception {
		validarDados(criterioAvaliacaoDisciplinaVO);
		try {
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE criterioAvaliacaoDisciplina SET ");
					sql.append(" criterioAvaliacaoPeriodoLetivo = ?, disciplina = ?, ordem = ? ");
					sql.append(" WHERE codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoPeriodoLetivo().getCodigo());
					ps.setInt(x++, criterioAvaliacaoDisciplinaVO.getDisciplina().getCodigo());
					ps.setInt(x++, criterioAvaliacaoDisciplinaVO.getOrdem());
					ps.setInt(x++, criterioAvaliacaoDisciplinaVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(criterioAvaliacaoDisciplinaVO);
				return;
			}
			getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().alterarCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaVO);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void incluirCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
			criterioAvaliacaoDisciplinaVO.setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
			incluir(criterioAvaliacaoDisciplinaVO);
		}

	}

	@Override
	public void alterarCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		excluirCriterioAvaliacaoDisciplinaVO(criterioAvaliacaoPeriodoLetivoVO);
		for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
			criterioAvaliacaoDisciplinaVO.setCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO);
			if (criterioAvaliacaoDisciplinaVO.getNovoObj()) {
				incluir(criterioAvaliacaoDisciplinaVO);
			} else {
				alterar(criterioAvaliacaoDisciplinaVO);
			}
		}

	}

	@Override
	public void excluirCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM criterioAvaliacaoDisciplina where criterioAvaliacaoPeriodoLetivo = ? and codigo not in (0 ");
		for (CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
			sql.append(", ").append(criterioAvaliacaoDisciplinaVO.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), criterioAvaliacaoPeriodoLetivoVO.getCodigo());
	}

	@Override
	public void alterarOrdemCriterioAvaliacaoDisciplinaEixoIndicadorVO(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO, CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO, boolean subir) throws Exception {
		int x = criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem();
		if (subir) {
			x--;
		} else {
			x++;
		}
		if (x == 0 || x > criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().size()) {
			return;
		}
		criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().get(x-1).setOrdem(criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem());
		criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().get(criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem() - 1).setOrdem(x);
		
		Ordenacao.ordenarLista(criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs(), "ordem");
	}

	@Override
	public void validarDados(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws ConsistirException {
		if (criterioAvaliacaoDisciplinaVO.getDisciplina().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoDisciplina_disciplina"));
		}

	}
	
	@Override
	public void realizarCalculoNotaCriterioAvaliacaoDisciplina(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception{
		for(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO: criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()){
			criterioAvaliacaoDisciplinaEixoIndicadorVO.setNota(Uteis.arrendondarForcando4CadasDecimais(10.0/criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().size()));
			getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().realizarCalculoNotaCriterioAvaliacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaEixoIndicadorVO);			
		}
	}

	@Override
	public void adicionarCriterioAvaliacaoDisciplinaEixoIndicadorVO(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO, CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().validarDados(criterioAvaliacaoDisciplinaEixoIndicadorVO);
		for (CriterioAvaliacaoDisciplinaEixoIndicadorVO obj : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
			if (criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem() == 0 && obj.getEixoIndicador().trim().equalsIgnoreCase(criterioAvaliacaoDisciplinaEixoIndicadorVO.getEixoIndicador())) {
				return;
			} else if (criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem() > 0 && !obj.getOrdem().equals(criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem()) && obj.getEixoIndicador().trim().equalsIgnoreCase(criterioAvaliacaoDisciplinaEixoIndicadorVO.getEixoIndicador())) {
				criterioAvaliacaoDisciplinaEixoIndicadorVO.setEixoIndicador(criterioAvaliacaoDisciplinaEixoIndicadorVO.getEixoIndicadorAnt());
				throw new Exception(UteisJSF.internacionalizar("msg_CriterioAvaliacaoDisciplinaEixoIndicador_unico"));
			}

		}
		if (criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem().equals(0)) {
			criterioAvaliacaoDisciplinaEixoIndicadorVO.setOrdem(criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().size() + 1);
			criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().add(criterioAvaliacaoDisciplinaEixoIndicadorVO);
			realizarCalculoNotaCriterioAvaliacaoDisciplina(criterioAvaliacaoDisciplinaVO);
		} else {
			criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().set(criterioAvaliacaoDisciplinaEixoIndicadorVO.getOrdem() - 1, criterioAvaliacaoDisciplinaEixoIndicadorVO);
		}
	}

	@Override
	public void removerCriterioAvaliacaoDisciplinaEixoIndicadorVO(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO, CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception {
		int x = 0;
		for (CriterioAvaliacaoDisciplinaEixoIndicadorVO obj : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
			if (obj.getEixoIndicador().trim().equalsIgnoreCase(criterioAvaliacaoDisciplinaEixoIndicadorVO.getEixoIndicador())) {
				criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().remove(x);
				break;
			}
			x++;
		}
		realizaAltualizacaoOrdemApresentacaoDisciplinaEixoIndicador(criterioAvaliacaoDisciplinaVO);
		realizarCalculoNotaCriterioAvaliacaoDisciplina(criterioAvaliacaoDisciplinaVO);
	}

	private void realizaAltualizacaoOrdemApresentacaoDisciplinaEixoIndicador(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception {
		int x = 1;
		for (CriterioAvaliacaoDisciplinaEixoIndicadorVO obj : criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoDisciplinaEixoIndicadorVOs()) {
			obj.setOrdem(x++);
		}
	}

	public StringBuilder getSqlBasico() {
		StringBuilder sql = new StringBuilder("SELECT CriterioAvaliacaoDisciplina.*, gradedisciplina.ordem as ordemdisciplina, disciplina.nome as \"disciplina.nome\" ");
		sql.append(" from CriterioAvaliacaoDisciplina ");
		sql.append(" inner join Disciplina on disciplina.codigo = CriterioAvaliacaoDisciplina.disciplina ");
		sql.append(" inner join criterioavaliacaoperiodoletivo on criterioavaliacaoperiodoletivo.codigo = criterioavaliacaodisciplina.criterioavaliacaoperiodoletivo ");
		sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = criterioavaliacaoperiodoletivo.periodoletivo and gradedisciplina.disciplina = disciplina.codigo ");
		return sql;
	}

	@Override
	public List<CriterioAvaliacaoDisciplinaVO> consultarPorCriterioAvaliacaoPeriodoLetivo(Integer criterioAvaliacaoPeriodoLetivo,Integer disciplina, int nivelMontarDados) throws Exception {
		StringBuilder sql = getSqlBasico();
		sql.append(" where CriterioAvaliacaoDisciplina.criterioAvaliacaoPeriodoLetivo = ?  ");
		if(disciplina != null && disciplina != 0 ){
			sql.append(" and CriterioAvaliacaoDisciplina.disciplina = "+ disciplina +" "); 
		}
		sql.append(" order by gradedisciplina.ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), criterioAvaliacaoPeriodoLetivo), nivelMontarDados);
	}

	private List<CriterioAvaliacaoDisciplinaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception {
		List<CriterioAvaliacaoDisciplinaVO> criterioAvaliacaoDisciplinaVOs = new ArrayList<CriterioAvaliacaoDisciplinaVO>(0);
		while (rs.next()) {
			criterioAvaliacaoDisciplinaVOs.add(montarDados(rs, nivelMontarDados));
		}
		return criterioAvaliacaoDisciplinaVOs;
	}

	private CriterioAvaliacaoDisciplinaVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception {
		CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO = new CriterioAvaliacaoDisciplinaVO();
		criterioAvaliacaoDisciplinaVO.setNovoObj(false);
		criterioAvaliacaoDisciplinaVO.setCodigo(rs.getInt("codigo"));
		criterioAvaliacaoDisciplinaVO.setOrdem(rs.getInt("ordem"));
		criterioAvaliacaoDisciplinaVO.setOrdemDisciplina(rs.getInt("ordemDisciplina"));
		criterioAvaliacaoDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
		criterioAvaliacaoDisciplinaVO.getDisciplina().setNome(rs.getString("disciplina.nome"));
		criterioAvaliacaoDisciplinaVO.getCriterioAvaliacaoPeriodoLetivo().setCodigo(rs.getInt("criterioAvaliacaoPeriodoLetivo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return criterioAvaliacaoDisciplinaVO;
		}
		criterioAvaliacaoDisciplinaVO.setCriterioAvaliacaoDisciplinaEixoIndicadorVOs(getFacadeFactory().getCriterioAvaliacaoDisciplinaEixoIndicadorFacade().consultarPorCriterioAvaliacaoDisciplina(criterioAvaliacaoDisciplinaVO.getCodigo(), nivelMontarDados));
		return criterioAvaliacaoDisciplinaVO;
	}
}
