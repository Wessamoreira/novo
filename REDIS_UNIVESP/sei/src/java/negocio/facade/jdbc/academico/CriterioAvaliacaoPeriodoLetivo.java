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

import negocio.comuns.academico.CriterioAvaliacaoDisciplinaVO;
import negocio.comuns.academico.CriterioAvaliacaoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoNotaConceitoVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.academico.CriterioAvaliacaoVO;
import negocio.comuns.academico.enumeradores.AvaliarNaoAvaliarEnum;
import negocio.comuns.academico.enumeradores.OrigemCriterioAvaliacaoIndicadorEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CriterioAvaliacaoPeriodoLetivoInterfaceFacade;

@Repository
@Lazy
public class CriterioAvaliacaoPeriodoLetivo extends ControleAcesso implements CriterioAvaliacaoPeriodoLetivoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4255963207992684463L;

	/**
	 * 
	 */

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		validarDados(criterioAvaliacaoPeriodoLetivoVO);
		try {
			criterioAvaliacaoPeriodoLetivoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("INSERT INTO criterioAvaliacaoPeriodoLetivo (");
					sql.append(" criterioAvaliacao, periodoLetivo )");
					sql.append(" VALUES (? , ?) returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoVO().getCodigo());
					ps.setInt(x++, criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().getCodigo());
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
			getFacadeFactory().getCriterioAvaliacaoDisciplinaFacade().incluirCriterioAvaliacaoDisciplinaVO(criterioAvaliacaoPeriodoLetivoVO);
			getFacadeFactory().getCriterioAvaliacaoNotaConceitoFacade().incluirCriterioAvaliacaoNotaConceito(criterioAvaliacaoPeriodoLetivoVO);
			getFacadeFactory().getCriterioAvaliacaoIndicadorFacade().incluirCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(criterioAvaliacaoPeriodoLetivoVO);
			criterioAvaliacaoPeriodoLetivoVO.setNovoObj(false);
		} catch (Exception e) {
			criterioAvaliacaoPeriodoLetivoVO.setNovoObj(true);
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		validarDados(criterioAvaliacaoPeriodoLetivoVO);
		try {
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE criterioAvaliacaoPeriodoLetivo SET ");
					sql.append(" criterioAvaliacao = ?, periodoLetivo = ? ");
					sql.append(" WHERE codigo = ? ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoVO().getCodigo());
					ps.setInt(x++, criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().getCodigo());
					ps.setInt(x++, criterioAvaliacaoPeriodoLetivoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(criterioAvaliacaoPeriodoLetivoVO);
				return;
			}
			getFacadeFactory().getCriterioAvaliacaoDisciplinaFacade().alterarCriterioAvaliacaoDisciplinaVO(criterioAvaliacaoPeriodoLetivoVO);
			getFacadeFactory().getCriterioAvaliacaoNotaConceitoFacade().alterarCriterioAvaliacaoNotaConceito(criterioAvaliacaoPeriodoLetivoVO);
			getFacadeFactory().getCriterioAvaliacaoIndicadorFacade().alterarCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(criterioAvaliacaoPeriodoLetivoVO);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO) throws Exception {
		for (CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO : criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs()) {
			criterioAvaliacaoPeriodoLetivoVO.setCriterioAvaliacaoVO(criterioAvaliacaoVO);
			incluir(criterioAvaliacaoPeriodoLetivoVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO) throws Exception {
		excluirCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoVO);
		for (CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO : criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs()) {
			criterioAvaliacaoPeriodoLetivoVO.setCriterioAvaliacaoVO(criterioAvaliacaoVO);
			if (criterioAvaliacaoPeriodoLetivoVO.isNovoObj()) {
				incluir(criterioAvaliacaoPeriodoLetivoVO);
			} else {
				alterar(criterioAvaliacaoPeriodoLetivoVO);
			}
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoVO criterioAvaliacaoVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM criterioAvaliacaoPeriodoLetivo where criterioAvaliacao = ? and codigo not in (0 ");
		for (CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO : criterioAvaliacaoVO.getCriterioAvaliacaoPeriodoLetivoVOs()) {
			sql.append(", ").append(criterioAvaliacaoPeriodoLetivoVO.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString(), criterioAvaliacaoVO.getCodigo());
	}

	@Override
	public void validarDados(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws ConsistirException {
		if (criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CriterioAvaliacaoPeriodoLetivo_periodoLetivo"));
		}

	}

	@Override
	public void adicionarCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, Integer disciplina, UsuarioVO usuarioVO) throws Exception {
		CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO = new CriterioAvaliacaoDisciplinaVO();
		criterioAvaliacaoDisciplinaVO.getDisciplina().setCodigo(disciplina);
		getFacadeFactory().getCriterioAvaliacaoDisciplinaFacade().validarDados(criterioAvaliacaoDisciplinaVO);
		for (CriterioAvaliacaoDisciplinaVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
			if (obj.getDisciplina().getCodigo().intValue() == criterioAvaliacaoDisciplinaVO.getDisciplina().getCodigo().intValue()) {
				return;
			}
		}
		criterioAvaliacaoDisciplinaVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		criterioAvaliacaoDisciplinaVO.setOrdem(criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().size() + 1);
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().add(criterioAvaliacaoDisciplinaVO);

	}

	@Override
	public void alterarOrdemCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO, boolean subir) throws Exception {
		int x = criterioAvaliacaoDisciplinaVO.getOrdem();
		if (subir) {
			x--;
		} else {
			x++;
		}
		if (x == 0 || x > criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().size()) {
			return;
		}
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().get(x - 1).setOrdem(criterioAvaliacaoDisciplinaVO.getOrdem());
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().get(criterioAvaliacaoDisciplinaVO.getOrdem() - 1).setOrdem(x);

		Ordenacao.ordenarLista(criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs(), "ordem");
	}

	@Override
	public void removerCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception {
		int x = 0;
		for (CriterioAvaliacaoDisciplinaVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
			if (obj.getDisciplina().getCodigo().intValue() == criterioAvaliacaoDisciplinaVO.getDisciplina().getCodigo().intValue()) {
				criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().remove(x);
				break;
			}
			x++;
		}
		realizaAltualizacaoOrdemApresentacaoDisciplina(criterioAvaliacaoPeriodoLetivoVO);
	}

	private void realizaAltualizacaoOrdemApresentacaoDisciplina(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		int x = 1;
		for (CriterioAvaliacaoDisciplinaVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs()) {
			obj.setOrdem(x++);
		}
	}

	@Override
	public void adicionarCriterioAvaliacaoNotaConceitoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getCriterioAvaliacaoNotaConceitoFacade().validarDados(criterioAvaliacaoNotaConceitoVO);
		for (CriterioAvaliacaoNotaConceitoVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs()) {
			if (obj.getNotaConceitoIndicadorAvaliacao().getCodigo().intValue() == criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getCodigo().intValue()) {
				return;
			}
		}
		criterioAvaliacaoNotaConceitoVO.setOrdem(criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs().size() + 1);
		criterioAvaliacaoNotaConceitoVO.setNotaConceitoIndicadorAvaliacao(getFacadeFactory().getNotaConceitoIndicadorAvaliacaoFacade().consultarPorChavePrimaria(criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs().add(criterioAvaliacaoNotaConceitoVO);

	}

	@Override
	public void removerCriterioAvaliacaoNotaConceitoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO) throws Exception {
		int x = 0;
		for (CriterioAvaliacaoNotaConceitoVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs()) {
			if (obj.getNotaConceitoIndicadorAvaliacao().getCodigo().intValue() == criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getCodigo().intValue()) {
				criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs().remove(x);
				break;
			}
			x++;
		}
		realizaAltualizacaoOrdemApresentacaoNotaConceito(criterioAvaliacaoPeriodoLetivoVO);
	}

	private void realizaAltualizacaoOrdemApresentacaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		int x = 1;
		for (CriterioAvaliacaoNotaConceitoVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs()) {
			obj.setOrdem(x++);
		}
	}

	@Override
	public void alterarOrdemCriterioAvaliacaoNotaConceitoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO, boolean subir) throws Exception {
		int x = criterioAvaliacaoNotaConceitoVO.getOrdem();
		if (subir) {
			x--;
		} else {
			x++;
		}
		if (x == 0 || x > criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoDisciplinaVOs().size()) {
			return;
		}
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs().get(x - 1).setOrdem(criterioAvaliacaoNotaConceitoVO.getOrdem());
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs().get(criterioAvaliacaoNotaConceitoVO.getOrdem() - 1).setOrdem(x);

		Ordenacao.ordenarLista(criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoNotaConceitoVOs(), "ordem");
	}

	private StringBuilder getSqlBasico() {
		StringBuilder sql = new StringBuilder("SELECT CriterioAvaliacaoPeriodoLetivo.*, periodoLetivo.descricao as \"periodoLetivo.descricao\", periodoLetivo.periodoLetivo as \"periodoLetivo.periodoLetivo\" ");
		sql.append(" FROM CriterioAvaliacaoPeriodoLetivo ");
		sql.append(" inner join PeriodoLetivo on periodoLetivo.codigo = CriterioAvaliacaoPeriodoLetivo.periodoLetivo ");
		return sql;
	}

	@Override
	public List<CriterioAvaliacaoPeriodoLetivoVO> consultarPorCriterioAvaliacao(Integer criterioAvaliacao, Integer disciplina,int nivelMontarDados) throws Exception {
		StringBuilder sql = getSqlBasico();
		sql.append(" WHERE criterioAvaliacao = ? order by periodoletivo.periodoletivo");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), criterioAvaliacao),disciplina, nivelMontarDados);
	}
	
	@Override
	public CriterioAvaliacaoPeriodoLetivoVO consultarPorChavePrimaria(Integer criterioAvaliacaoPeriodoLetivo,Integer disciplina, int nivelMontarDados) throws Exception {
		StringBuilder sql = getSqlBasico();
		sql.append(" WHERE criterioAvaliacaoPeriodoLetivo.codigo = ? order by periodoletivo.periodoletivo");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), criterioAvaliacaoPeriodoLetivo);
		if(rs.next()){
			return montarDados(rs,disciplina, nivelMontarDados);
		}
		return null;
	}

	private List<CriterioAvaliacaoPeriodoLetivoVO> montarDadosConsulta(SqlRowSet rs,Integer disciplina, int nivelMontarDados) throws Exception {
		List<CriterioAvaliacaoPeriodoLetivoVO> criterioAvaliacaoPeriodoLetivoVOs = new ArrayList<CriterioAvaliacaoPeriodoLetivoVO>(0);
		while (rs.next()) {
			criterioAvaliacaoPeriodoLetivoVOs.add(montarDados(rs,disciplina, nivelMontarDados));
		}
		return criterioAvaliacaoPeriodoLetivoVOs;
	}

	private CriterioAvaliacaoPeriodoLetivoVO montarDados(SqlRowSet rs,Integer disciplina,int nivelMontarDados) throws Exception {
		CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO = new CriterioAvaliacaoPeriodoLetivoVO();
		criterioAvaliacaoPeriodoLetivoVO.setNovoObj(false);
		criterioAvaliacaoPeriodoLetivoVO.setCodigo(rs.getInt("codigo"));
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoVO().setCodigo(rs.getInt("criterioAvaliacao"));
		criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setCodigo(rs.getInt("periodoletivo"));
		criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setPeriodoLetivo(rs.getInt("periodoLetivo.periodoletivo"));
		criterioAvaliacaoPeriodoLetivoVO.getPeriodoLetivoVO().setDescricao(rs.getString("periodoLetivo.descricao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return criterioAvaliacaoPeriodoLetivoVO;
		}
		criterioAvaliacaoPeriodoLetivoVO.setCriterioAvaliacaoDisciplinaVOs(getFacadeFactory().getCriterioAvaliacaoDisciplinaFacade().consultarPorCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO.getCodigo(),disciplina, nivelMontarDados));
		criterioAvaliacaoPeriodoLetivoVO.setCriterioAvaliacaoNotaConceitoVOs(getFacadeFactory().getCriterioAvaliacaoNotaConceitoFacade().consultarPorCriterioAvaliacaoPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO.getCodigo()));
		criterioAvaliacaoPeriodoLetivoVO.setCriterioAvaliacaoIndicadorVOs(getFacadeFactory().getCriterioAvaliacaoIndicadorFacade().consultarPorCriterioAvalicaoDisciplinaPorPeriodoLetivo(criterioAvaliacaoPeriodoLetivoVO.getCodigo()));
		return criterioAvaliacaoPeriodoLetivoVO;
	}

	@Override
	public void alterarOrdemCriterioAvaliacaoIndicadorVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO, boolean subir) throws Exception {
		int x = criterioAvaliacaoIndicadorVO.getOrdem();
		if (subir) {
			x--;
		} else {
			x++;
		}
		if (x == 0 || x > criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs().size()) {
			return;
		}
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs().get(x - 1).setOrdem(criterioAvaliacaoIndicadorVO.getOrdem());
		criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs().get(criterioAvaliacaoIndicadorVO.getOrdem() - 1).setOrdem(x);
		Ordenacao.ordenarLista(criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs(), "ordem");
	}

	@Override
	public void adicionarCriterioAvaliacaoIndicadorVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) throws Exception {
		getFacadeFactory().getCriterioAvaliacaoIndicadorFacade().validarDados(criterioAvaliacaoIndicadorVO);
		for (CriterioAvaliacaoIndicadorVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (criterioAvaliacaoIndicadorVO.getOrdem() > 0 && obj.getDescricao().trim().equalsIgnoreCase(criterioAvaliacaoIndicadorVO.getDescricao()) && !obj.getOrdem().equals(criterioAvaliacaoIndicadorVO.getOrdem())) {
				criterioAvaliacaoIndicadorVO.setDescricao(criterioAvaliacaoIndicadorVO.getDescricaoAnt());
				throw new Exception(UteisJSF.internacionalizar("msg_CriterioAvaliacaoIndicador_unico"));
			} else if (obj.getDescricao().trim().equalsIgnoreCase(criterioAvaliacaoIndicadorVO.getDescricao())) {
				return;
			}
		}
		if (criterioAvaliacaoIndicadorVO.getOrdem().equals(0)) {
			criterioAvaliacaoIndicadorVO.setOrdem(criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs().size() + 1);
			criterioAvaliacaoIndicadorVO.setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum.GERAL);
			criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs().add(criterioAvaliacaoIndicadorVO);

		} else {
			criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs().set(criterioAvaliacaoIndicadorVO.getOrdem() - 1, criterioAvaliacaoIndicadorVO);
		}
		realizarCalculoNotaCriterioAvaliacaoIndicador(criterioAvaliacaoPeriodoLetivoVO);
	}

	@Override
	public void realizarCalculoQtdeIndicadorPorBimestre(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		criterioAvaliacaoPeriodoLetivoVO.setQtdeIndicador1Bimestre(0);
		criterioAvaliacaoPeriodoLetivoVO.setQtdeIndicador2Bimestre(0);
		criterioAvaliacaoPeriodoLetivoVO.setQtdeIndicador3Bimestre(0);
		criterioAvaliacaoPeriodoLetivoVO.setQtdeIndicador4Bimestre(0);
		for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (criterioAvaliacaoIndicadorVO.getAvaliarPrimeiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoPeriodoLetivoVO.setQtdeIndicador1Bimestre(criterioAvaliacaoPeriodoLetivoVO.getQtdeIndicador1Bimestre() + 1);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarSegundoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoPeriodoLetivoVO.setQtdeIndicador2Bimestre(criterioAvaliacaoPeriodoLetivoVO.getQtdeIndicador2Bimestre() + 1);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarTerceiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoPeriodoLetivoVO.setQtdeIndicador3Bimestre(criterioAvaliacaoPeriodoLetivoVO.getQtdeIndicador3Bimestre() + 1);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarQuartoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoPeriodoLetivoVO.setQtdeIndicador4Bimestre(criterioAvaliacaoPeriodoLetivoVO.getQtdeIndicador4Bimestre() + 1);
			}
		}
	}

	public void realizarCalculoNotaCriterioAvaliacaoIndicador(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		realizarCalculoQtdeIndicadorPorBimestre(criterioAvaliacaoPeriodoLetivoVO);
		for (CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (criterioAvaliacaoIndicadorVO.getAvaliarPrimeiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoIndicadorVO.setNota1Bimestre(Uteis.arrendondarForcando4CadasDecimais(10.0 / criterioAvaliacaoPeriodoLetivoVO.getQtdeIndicador1Bimestre()));
			} else {
				criterioAvaliacaoIndicadorVO.setNota1Bimestre(0.0);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarSegundoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoIndicadorVO.setNota2Bimestre(Uteis.arrendondarForcando4CadasDecimais(10.0 / criterioAvaliacaoPeriodoLetivoVO.getQtdeIndicador2Bimestre()));
			} else {
				criterioAvaliacaoIndicadorVO.setNota2Bimestre(0.0);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarTerceiroBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoIndicadorVO.setNota3Bimestre(Uteis.arrendondarForcando4CadasDecimais(10.0 / criterioAvaliacaoPeriodoLetivoVO.getQtdeIndicador3Bimestre()));
			} else {
				criterioAvaliacaoIndicadorVO.setNota3Bimestre(0.0);
			}
			if (criterioAvaliacaoIndicadorVO.getAvaliarQuartoBimestre().equals(AvaliarNaoAvaliarEnum.AVALIAR)) {
				criterioAvaliacaoIndicadorVO.setNota4Bimestre(Uteis.arrendondarForcando4CadasDecimais(10.0 / criterioAvaliacaoPeriodoLetivoVO.getQtdeIndicador4Bimestre()));
			} else {
				criterioAvaliacaoIndicadorVO.setNota4Bimestre(0.0);
			}
		}

	}

	@Override
	public void excluirCriterioAvaliacaoIndicadorVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) throws Exception {
		int x = 0;
		for (CriterioAvaliacaoIndicadorVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
			if (obj.getDescricao().trim().equalsIgnoreCase(criterioAvaliacaoIndicadorVO.getDescricao())) {
				criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs().remove(x);
				break;
			}
			x++;
		}
		realizaAltualizacaoOrdemApresentacaoIndicador(criterioAvaliacaoPeriodoLetivoVO);
		realizarCalculoNotaCriterioAvaliacaoIndicador(criterioAvaliacaoPeriodoLetivoVO);
	}

	private void realizaAltualizacaoOrdemApresentacaoIndicador(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception {
		int x = 1;
		for (CriterioAvaliacaoIndicadorVO obj : criterioAvaliacaoPeriodoLetivoVO.getCriterioAvaliacaoIndicadorVOs()) {
			obj.setOrdem(x++);
		}
	}

}
