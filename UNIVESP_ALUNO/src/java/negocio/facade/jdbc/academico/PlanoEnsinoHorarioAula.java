package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PlanoEnsinoHorarioAulaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PlanoEnsinoHorarioAulaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class PlanoEnsinoHorarioAula extends ControleAcesso implements PlanoEnsinoHorarioAulaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1747463287071873027L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPlanoEnsinoHorarioAulaVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception {	
		for(PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO: planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs()){
			planoEnsinoHorarioAulaVO.setPlanoEnsinoVO(planoEnsinoVO);
			incluir(planoEnsinoHorarioAulaVO, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPlanoEnsinoHorarioAulaVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		excluirPlanoEnsinoHorarioAulaVOs(planoEnsinoVO, usuarioVO);
		for(PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO: planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs()){
			planoEnsinoHorarioAulaVO.setPlanoEnsinoVO(planoEnsinoVO);
			if(planoEnsinoHorarioAulaVO.isNovoObj()){
				incluir(planoEnsinoHorarioAulaVO, usuarioVO);
			}else{
				alterar(planoEnsinoHorarioAulaVO, usuarioVO);
			}
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPlanoEnsinoHorarioAulaVOs(PlanoEnsinoVO planoEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  = new StringBuilder("delete from planoensinohorarioaula where planoensino = ? and codigo not in (0 ");
		for(PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO: planoEnsinoVO.getPlanoEnsinoHorarioAulaVOs()){
			sql.append(", ").append(planoEnsinoHorarioAulaVO.getCodigo());			
		}
		sql.append(" )").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), planoEnsinoVO.getCodigo());		
	}

	@Override
	public void validarDados(PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO) throws ConsistirException {
		if(planoEnsinoHorarioAulaVO.getDiaSemana() == null || planoEnsinoHorarioAulaVO.getDiaSemana().equals(DiaSemana.NENHUM)){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_diaSemana"));
		}
		if(planoEnsinoHorarioAulaVO.getInicioAula().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_inicioAula"));
		}
		if(planoEnsinoHorarioAulaVO.getInicioAula().trim().length() != 5){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_inicioAula_incompleto"));
		}
		if(Integer.valueOf(planoEnsinoHorarioAulaVO.getInicioAula().substring(0, 2)) > 23){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_inicioAula_horaInvalida"));
		}
		if(Integer.valueOf(planoEnsinoHorarioAulaVO.getInicioAula().substring(3, 5)) > 59){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_inicioAula_minitoInvalida"));
		}
		if(planoEnsinoHorarioAulaVO.getTerminoAula().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_terminoAula"));
		}
		if(planoEnsinoHorarioAulaVO.getTerminoAula().trim().length() != 5){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_terminoAula_incompleto"));
		}
		if(Integer.valueOf(planoEnsinoHorarioAulaVO.getTerminoAula().substring(0, 2)) > 23){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_terminoAula_horaInvalida"));
		}
		if(Integer.valueOf(planoEnsinoHorarioAulaVO.getTerminoAula().substring(3, 5)) > 59){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_terminoAula_minutoInvalido"));
		}
		if(Uteis.realizarValidacaoHora1MaiorHora2(planoEnsinoHorarioAulaVO.getInicioAula(), planoEnsinoHorarioAulaVO.getTerminoAula())
				|| planoEnsinoHorarioAulaVO.getInicioAula().equals(planoEnsinoHorarioAulaVO.getTerminoAula())){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PlanoEnsinoHorarioAula_inicioAula_maior_terminoAula"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO, final UsuarioVO usuarioVO) throws Exception {
		validarDados(planoEnsinoHorarioAulaVO);
		final StringBuilder sql = new StringBuilder("insert into planoensinohorarioaula (diasemana, inicioAula, terminoAula, turma, planoensino) values (?,?,?,?,?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		planoEnsinoHorarioAulaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps =  arg0.prepareStatement(sql.toString());
				ps.setString(1, planoEnsinoHorarioAulaVO.getDiaSemana().getValor());
				ps.setString(2, planoEnsinoHorarioAulaVO.getInicioAula());
				ps.setString(3, planoEnsinoHorarioAulaVO.getTerminoAula());
				if(Uteis.isAtributoPreenchido(planoEnsinoHorarioAulaVO.getTurmaVO())){
					ps.setInt(4, planoEnsinoHorarioAulaVO.getTurmaVO().getCodigo());
				}else{
					ps.setNull(4, 0);
				}
				ps.setInt(5, planoEnsinoHorarioAulaVO.getPlanoEnsinoVO().getCodigo());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()){
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		planoEnsinoHorarioAulaVO.setNovoObj(false);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO, final UsuarioVO usuarioVO) throws Exception {
		validarDados(planoEnsinoHorarioAulaVO);
		final StringBuilder sql = new StringBuilder("update planoensinohorarioaula set diasemana =? , inicioAula =?, terminoAula = ?, turma =?, planoensino=? where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps =  arg0.prepareStatement(sql.toString());
				ps.setString(1, planoEnsinoHorarioAulaVO.getDiaSemana().getValor());
				ps.setString(2, planoEnsinoHorarioAulaVO.getInicioAula());
				ps.setString(3, planoEnsinoHorarioAulaVO.getTerminoAula());
				if(Uteis.isAtributoPreenchido(planoEnsinoHorarioAulaVO.getTurmaVO())){
					ps.setInt(4, planoEnsinoHorarioAulaVO.getTurmaVO().getCodigo());
				}else{
					ps.setNull(4, 0);
				}
				ps.setInt(5, planoEnsinoHorarioAulaVO.getPlanoEnsinoVO().getCodigo());
				ps.setInt(6, planoEnsinoHorarioAulaVO.getCodigo());
				return ps;
			}
		}) == 0){
			incluir(planoEnsinoHorarioAulaVO, usuarioVO);
		};	
	}

	@Override
	public List<PlanoEnsinoHorarioAulaVO> consultarPorPlanoEnsino(Integer planoEnsino, UsuarioVO usuarioVO)			throws Exception {
		StringBuilder sql =  getSqlConsultaCompleta();
		sql.append(" where planoensino = ? ");
		sql.append(" order by diasemana, inicioaula, terminoaula ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), planoEnsino), usuarioVO);
	}
	
	private PlanoEnsinoHorarioAulaVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO)throws Exception {
		PlanoEnsinoHorarioAulaVO planoEnsinoHorarioAulaVO =  new PlanoEnsinoHorarioAulaVO();
		planoEnsinoHorarioAulaVO.setNovoObj(false);
		planoEnsinoHorarioAulaVO.setCodigo(rs.getInt("codigo"));
		planoEnsinoHorarioAulaVO.setDiaSemana(DiaSemana.getEnum(rs.getString("diaSemana")));
		planoEnsinoHorarioAulaVO.setInicioAula(rs.getString("inicioAula"));
		planoEnsinoHorarioAulaVO.setTerminoAula(rs.getString("terminoAula"));
		planoEnsinoHorarioAulaVO.getTurmaVO().setCodigo(rs.getInt("turma"));
		planoEnsinoHorarioAulaVO.getTurmaVO().setIdentificadorTurma(rs.getString("identificadorturma"));
		planoEnsinoHorarioAulaVO.getPlanoEnsinoVO().setCodigo(rs.getInt("planoEnsino"));
		return planoEnsinoHorarioAulaVO;
		
	}
	private List<PlanoEnsinoHorarioAulaVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO)throws Exception {
		List<PlanoEnsinoHorarioAulaVO> planoEnsinoHorarioAulaVOs =  new ArrayList<PlanoEnsinoHorarioAulaVO>(0);
		while(rs.next()){
			planoEnsinoHorarioAulaVOs.add(montarDados(rs, usuarioVO));
		}
		return planoEnsinoHorarioAulaVOs;
	}
	
	
	private StringBuilder getSqlConsultaCompleta(){
		StringBuilder sql =  new StringBuilder("select planoensinohorarioaula.*, turma.identificadorturma ");
		sql.append(" from planoensinohorarioaula ");
		sql.append(" left join turma on turma.codigo = planoensinohorarioaula.turma ");
		return sql;
	}

}
