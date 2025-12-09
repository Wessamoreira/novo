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

import negocio.comuns.academico.TurmaContratoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurmaContratoInterfaceFacade;

@Scope("singleton")
@Repository
@Lazy
public class TurmaContrato extends ControleAcesso implements TurmaContratoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143365271872680305L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirTurmaContratoVOs(TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception {
		for(TurmaContratoVO turmaContratoVO: turmaVO.getTurmaContratoVOs()) {
			turmaContratoVO.setTurmaVO(turmaVO);
			validarDados(turmaContratoVO);
			incluir(turmaContratoVO, usuarioVO);
		}

	}
	
	@Override	
	public void validarDados(TurmaContratoVO turmaContratoVO) throws ConsistirException {
		if(!Uteis.isAtributoPreenchido(turmaContratoVO.getTextoPadraoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TurmaContrato_textoPadrao"));
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaContratoVO turmaContratoVO,final  UsuarioVO usuarioVO) throws Exception {
		validarDados(turmaContratoVO);
		turmaContratoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql =  new StringBuilder("insert into turmacontrato ( ");
				sql.append(" turma, textopadrao, tipocontratomatricula, padrao) ");
				sql.append(" values (?, ?, ?, ?) ");
				sql.append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				PreparedStatement ps =arg0.prepareStatement(sql.toString());
				ps.setInt(1, turmaContratoVO.getTurmaVO().getCodigo());
				ps.setInt(2, turmaContratoVO.getTextoPadraoVO().getCodigo());
				ps.setString(3, turmaContratoVO.getTipoContratoMatricula().name());
				ps.setBoolean(4, turmaContratoVO.getPadrao());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()) {
					turmaContratoVO.setNovoObj(false);
					return arg0.getInt("codigo");
				}
				return null;
			}
			
		}));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterarTurmaContratoVOs(TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception {
		excluirTurmaContratoVOs(turmaVO, usuarioVO);
		for(TurmaContratoVO turmaContratoVO: turmaVO.getTurmaContratoVOs()) {		
			turmaContratoVO.setTurmaVO(turmaVO);
			if(!Uteis.isAtributoPreenchido(turmaContratoVO)) {
				incluir(turmaContratoVO, usuarioVO);
			}else {
				alterar(turmaContratoVO, usuarioVO);
			}
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterar(final TurmaContratoVO turmaContratoVO,final  UsuarioVO usuarioVO) throws Exception {
		validarDados(turmaContratoVO);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql =  new StringBuilder("update turmacontrato set ");
				sql.append(" turma = ?, textopadrao = ?, tipocontratomatricula = ?, padrao = ? ");
				sql.append(" where codigo = ? ");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				PreparedStatement ps =arg0.prepareStatement(sql.toString());
				ps.setInt(1, turmaContratoVO.getTurmaVO().getCodigo());
				ps.setInt(2, turmaContratoVO.getTextoPadraoVO().getCodigo());
				ps.setString(3, turmaContratoVO.getTipoContratoMatricula().name());
				ps.setBoolean(4, turmaContratoVO.getPadrao());
				ps.setInt(5, turmaContratoVO.getCodigo());
				return ps;
			}
		})==0){
			incluir(turmaContratoVO, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void excluirTurmaContratoVOs(TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("delete from turmacontrato where turma = ? and codigo not in (0 ");
		for(TurmaContratoVO turmaContratoVO: turmaVO.getTurmaContratoVOs()) {		
			sql.append(", ").append(turmaContratoVO.getCodigo());
		}
		sql.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), turmaVO.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void excluir(TurmaContratoVO turmaContratoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("delete from turmacontrato where codigo = ? ");		
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), turmaContratoVO.getCodigo());

	}

	@Override
	public void adicionarTurmaContratoVOs(TurmaVO turmaVO, TurmaContratoVO turmaContratoAdicionarVO, UsuarioVO usuarioVO) throws Exception {	
		validarDados(turmaContratoAdicionarVO);
		for(TurmaContratoVO turmaContratoVO: turmaVO.getTurmaContratoVOs()) {		
			if(turmaContratoVO.getTipoContratoMatricula().equals(turmaContratoAdicionarVO.getTipoContratoMatricula())) {
				if(turmaContratoVO.getPadrao() && turmaContratoAdicionarVO.getPadrao()) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_TurmaContrato_tipoContratoPadrao_duplicidade"));
				}
				if(turmaContratoVO.getTextoPadraoVO().getCodigo().equals(turmaContratoAdicionarVO.getTextoPadraoVO().getCodigo())) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_TurmaContrato_textoPadrao"));
				}
			}		
		}
		turmaContratoAdicionarVO.setTextoPadraoVO(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(turmaContratoAdicionarVO.getTextoPadraoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		turmaVO.getTurmaContratoVOs().add(turmaContratoAdicionarVO);

	}

	@Override
	public void removerTurmaContratoVOs(TurmaVO turmaVO, TurmaContratoVO turmaContratoRemoverVO, UsuarioVO usuarioVO) throws Exception {
		int x = 0;
		for(TurmaContratoVO turmaContratoVO: turmaVO.getTurmaContratoVOs()) {		
			if(turmaContratoVO.getTipoContratoMatricula().equals(turmaContratoRemoverVO.getTipoContratoMatricula()) && turmaContratoVO.getTextoPadraoVO().getCodigo().equals(turmaContratoRemoverVO.getTextoPadraoVO().getCodigo())) {
				turmaVO.getTurmaContratoVOs().remove(x);
				break;
			}		
			x++;
		}

	}

	@Override
	public List<TurmaContratoVO> consultarPorTurma(Integer turma, UsuarioVO usuarioVO) throws Exception {		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select * from turmacontrato where turma = ? order by tipocontratomatricula, codigo", turma), usuarioVO);
	}
	
	private List<TurmaContratoVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		List<TurmaContratoVO> turmaContratoVOs =  new ArrayList<TurmaContratoVO>(0);
		while(rs.next()) {
			turmaContratoVOs.add(montarDados(rs, usuarioVO));
		}
		return turmaContratoVOs;
	}
	private TurmaContratoVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		TurmaContratoVO turmaContratoVO =  new TurmaContratoVO();
		turmaContratoVO.setNovoObj(false);
		turmaContratoVO.setCodigo(rs.getInt("codigo"));
		turmaContratoVO.getTurmaVO().setCodigo(rs.getInt("turma"));
		turmaContratoVO.getTextoPadraoVO().setCodigo(rs.getInt("textoPadrao"));
		turmaContratoVO.setTipoContratoMatricula(TipoContratoMatriculaEnum.valueOf(rs.getString("tipoContratoMatricula")));
		turmaContratoVO.setPadrao(rs.getBoolean("padrao"));
		turmaContratoVO.setTextoPadraoVO(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(turmaContratoVO.getTextoPadraoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
		return turmaContratoVO;		
	}

	@Override
	public TurmaContratoVO consultarChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select * from turmacontrato where codigo = ? ", codigo);
		if(rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		throw new Exception("Dados não encontrados (TurmaContrato - "+codigo+").");
		
	}

	@Override
	public List<TurmaContratoVO> consultarTurmaTipoContratoMatricula(Integer turma,
			TipoContratoMatriculaEnum tipoContratoMatriculaEnum, boolean trazerApenasPadrao, UsuarioVO usuarioVO)
			throws Exception {
		StringBuilder sql  = new StringBuilder("select * from turmacontrato where turma = ? ");
		sql.append(" and tipocontratomatricula = ? ");
		if(trazerApenasPadrao) {
			sql.append(" and padrao = true ");
		}
		sql.append(" order by tipocontratomatricula, codigo ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), turma, tipoContratoMatriculaEnum.name()), usuarioVO);
	}

}
