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

import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.SalaLocalAulaInterfaceFacade;

@Repository
@Lazy
public class SalaLocalAula extends ControleAcesso implements SalaLocalAulaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -388005119259085154L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirSalaLocalAula(LocalAulaVO localAulaVO) throws Exception {
		for (SalaLocalAulaVO salaLocalAulaVO : localAulaVO.getSalaLocalAulaVOs()) {
			salaLocalAulaVO.setLocalAula(localAulaVO);
			incluir(salaLocalAulaVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final SalaLocalAulaVO salaLocalAulaVO) throws Exception {
		validarDados(salaLocalAulaVO);
		salaLocalAulaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO SalaLocalAula ");
				sql.append(" ( sala, capacidade, localAula, ordem, naoControlarChoqueSala, inativo) VALUES (?, ?, ?, ?, ?, ?) returning codigo ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, salaLocalAulaVO.getSala());
				ps.setInt(2, salaLocalAulaVO.getCapacidade());
				ps.setInt(3, salaLocalAulaVO.getLocalAula().getCodigo());
				ps.setInt(4, salaLocalAulaVO.getOrdem());
				ps.setBoolean(5, salaLocalAulaVO.getNaoControlarChoqueSala());
				ps.setBoolean(6, salaLocalAulaVO.getInativo());
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
		salaLocalAulaVO.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterar(final SalaLocalAulaVO salaLocalAulaVO) throws Exception {
		validarDados(salaLocalAulaVO);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("UPDATE SalaLocalAula SET ");
				sql.append(" sala = ?, capacidade = ?, localAula = ?, ordem = ?, naoControlarChoqueSala = ?, inativo = ? WHERE codigo = ? ");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, salaLocalAulaVO.getSala());
				ps.setInt(2, salaLocalAulaVO.getCapacidade());
				ps.setInt(3, salaLocalAulaVO.getLocalAula().getCodigo());
				ps.setInt(4, salaLocalAulaVO.getOrdem());
				ps.setBoolean(5, salaLocalAulaVO.getNaoControlarChoqueSala());
				ps.setBoolean(6, salaLocalAulaVO.getInativo());
				ps.setInt(7, salaLocalAulaVO.getCodigo());
				return ps;
			}
		}) == 0) {
			incluir(salaLocalAulaVO);
			return;
		}
		;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirSalaLocalAula(LocalAulaVO localAulaVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM SalaLocalAula WHERE localAula = ").append(localAulaVO.getCodigo());
		sql.append(" and codigo not in ( 0 ");
		for (SalaLocalAulaVO salaLocalAulaVO : localAulaVO.getSalaLocalAulaVOs()) {
			sql.append(", ").append(salaLocalAulaVO.getCodigo());
		}
		sql.append(" ) ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterarSalaLocalAula(LocalAulaVO localAulaVO) throws Exception {
		excluirSalaLocalAula(localAulaVO);
		for (SalaLocalAulaVO salaLocalAulaVO : localAulaVO.getSalaLocalAulaVOs()) {
			salaLocalAulaVO.setLocalAula(localAulaVO);
			if (localAulaVO.getNovoObj()) {
				incluir(salaLocalAulaVO);
			} else {
				alterar(salaLocalAulaVO);
			}
		}

	}

	@Override
	public void validarDados(SalaLocalAulaVO salaLocalAulaVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		if (salaLocalAulaVO.getSala().trim().isEmpty()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SalaLocalAula_sala"));
		}
		if (salaLocalAulaVO.getCapacidade() <= 0) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_SalaLocalAula_capacidade"));
		}
		if(!consistirException.getListaMensagemErro().isEmpty()){
			throw consistirException;
		}else{
			consistirException = null;
		}

	}

	@Override
	public List<SalaLocalAulaVO> consultarPorLocalAula(Integer localAula) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" where SalaLocalAula.localAula = ").append(localAula);
		sql.append(" order by SalaLocalAula.ordem ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	@Override
	public List<SalaLocalAulaVO> consultarPorSala(String sala, LocalAulaVO localAula, Integer unidadeEnsino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, StatusAtivoInativoEnum situacao) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" where sem_acentos(SalaLocalAula.sala) ilike sem_acentos(?) ");
		if (localAula.getCodigo() != 0) {
			sql.append(" and SalaLocalAula.localaula = ").append(localAula.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sql.append(" and (localaula.unidadeEnsino is null or localaula.unidadeEnsino = ").append(unidadeEnsino).append(") ");
		}
		if(Uteis.isAtributoPreenchido(situacao)) {
			if(situacao.equals(StatusAtivoInativoEnum.ATIVO)) {
				sql.append(" and SalaLocalAula.inativo = false ");
			}else {
				sql.append(" and SalaLocalAula.inativo = true ");
			}
		}
		String unidadeEnsinoIN = "";
		for(UnidadeEnsinoVO unidadeEnsinoVO: unidadeEnsinoVOs){
			if(unidadeEnsinoVO.getFiltrarUnidadeEnsino()){
				if(!unidadeEnsinoIN.trim().isEmpty()){
					unidadeEnsinoIN += ", ";
				}
				unidadeEnsinoIN += unidadeEnsinoVO.getCodigo(); 
			}
		}
		if(!unidadeEnsinoIN.trim().isEmpty()){
			sql.append(" and (localAula.unidadeEnsino is null or localAula.unidadeEnsino in (").append(unidadeEnsinoIN).append(")) ");
		}

		sql.append(" order by SalaLocalAula.ordem, SalaLocalAula.sala ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), sala + PERCENT));
	}

	public List<SalaLocalAulaVO> montarDadosConsulta(SqlRowSet rs) {
		List<SalaLocalAulaVO> salaLocalAulaVOs = new ArrayList<SalaLocalAulaVO>(0);
		while (rs.next()) {
			salaLocalAulaVOs.add(montarDados(rs));
		}
		return salaLocalAulaVOs;
	}

	public SalaLocalAulaVO montarDados(SqlRowSet rs) {
		SalaLocalAulaVO salaLocalAulaVO = new SalaLocalAulaVO();
		salaLocalAulaVO.setNovoObj(false);
		salaLocalAulaVO.setCodigo(rs.getInt("codigo"));
		salaLocalAulaVO.setCapacidade(rs.getInt("capacidade"));
		salaLocalAulaVO.setSala(rs.getString("sala"));
		salaLocalAulaVO.getLocalAula().setCodigo(rs.getInt("localAula"));
		salaLocalAulaVO.setOrdem(rs.getInt("ordem"));
		salaLocalAulaVO.setNaoControlarChoqueSala(rs.getBoolean("naoControlarChoqueSala"));
		salaLocalAulaVO.setInativo(rs.getBoolean("inativo"));
		salaLocalAulaVO.getLocalAula().setLocal(rs.getString("localaula_local"));
		salaLocalAulaVO.getLocalAula().getUnidadeEnsino().setNome(rs.getString("unidadeEnsino_nome"));
		salaLocalAulaVO.getLocalAula().getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino_codigo"));
		return salaLocalAulaVO;
	}
	
	private String getSqlConsultaCompleta(){
		StringBuilder sql  =  new StringBuilder("SELECT distinct SalaLocalAula.*, localaula.local as localaula_local, UnidadeEnsino.nome as unidadeEnsino_nome , UnidadeEnsino.codigo as unidadeEnsino_codigo FROM SalaLocalAula ");
		sql.append(" inner join localaula on localaula.codigo =  SalaLocalAula.localaula ");
		sql.append(" left join unidadeEnsino on localaula.unidadeEnsino =  unidadeEnsino.codigo ");
		return sql.toString();
	}
	
	@Override
	public SalaLocalAulaVO consultarPorChavePrimaria(Integer codigo) throws Exception{
		StringBuilder sql  =  new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE SalaLocalAula.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return montarDados(rs);
		}
		return null;
	}
	
	@Override
	public List<SalaLocalAulaVO> consultarComboboxPorUnidadeEnsino(Integer unidadeEnsino) throws Exception{
		StringBuilder sql  =  new StringBuilder("SELECT SalaLocalAula.codigo, SalaLocalAula.sala, localaula.local FROM SalaLocalAula ");
		sql.append(" inner join localaula on localaula.codigo = SalaLocalAula.localAula ");
		sql.append(" WHERE localaula.unidadeEnsino = ").append(unidadeEnsino).append(" order by SalaLocalAula.ordem ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SalaLocalAulaVO> localAulaVOs = new ArrayList<SalaLocalAulaVO>(0);
		SalaLocalAulaVO sala = null;
		while(rs.next()){
			sala = new SalaLocalAulaVO();
			sala.setCodigo(rs.getInt("codigo"));
			sala.setSala(rs.getString("sala"));
			sala.getLocalAula().setLocal(rs.getString("local"));
			localAulaVOs.add(sala);
		}
		return localAulaVOs;
	}
	
	@Override
	public List<SalaLocalAulaVO> consultarComboboxPorLocalAula(Integer localAula, Boolean ativo) throws Exception{
		StringBuilder sql  =  new StringBuilder("SELECT SalaLocalAula.codigo, SalaLocalAula.sala, localaula.local FROM SalaLocalAula ");
		sql.append(" inner join localaula on localaula.codigo = SalaLocalAula.localAula ");
		sql.append(" WHERE localaula.codigo = ").append(localAula).append(" ");
		if(ativo != null){
			if(ativo){
				sql.append(" and  SalaLocalAula.inativo = false ");
			}else{
				sql.append(" and  SalaLocalAula.inativo = true ");
			}
		}
		sql.append(" order by SalaLocalAula.ordem ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SalaLocalAulaVO> localAulaVOs = new ArrayList<SalaLocalAulaVO>(0);
		SalaLocalAulaVO sala = null;
		while(rs.next()){
			sala = new SalaLocalAulaVO();
			sala.setCodigo(rs.getInt("codigo"));
			sala.setSala(rs.getString("sala"));
			sala.getLocalAula().setLocal(rs.getString("local"));
			localAulaVOs.add(sala);
		}
		return localAulaVOs;
	}

}
