package negocio.facade.jdbc.crm;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.crm.TipoContatoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.TipoContatoInterfaceFacade;

@Repository
@Lazy
public class TipoContato extends ControleAcesso implements TipoContatoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3487072363462026340L;
	private static final String idEntidade = "TipoContato";

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void persistir(TipoContatoVO tipoContatoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		if(tipoContatoVO.isNovoObj()){
			incluir(tipoContatoVO, controlarAcesso, usuarioVO);
		}else{
			alterar(tipoContatoVO, controlarAcesso, usuarioVO);
		}
	}
	
	private void validarDados(TipoContatoVO tipoContatoVO) throws ConsistirException{
		if(tipoContatoVO.getDescricao().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoContato_descricao"));
		}
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	private void incluir(final TipoContatoVO tipoContatoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception{
		incluir(idEntidade, controlarAcesso, usuarioVO);
		validarDados(tipoContatoVO);
		try{
		tipoContatoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql  = new StringBuilder("INSERT INTO TipoContato (descricao, situacao) values (?,?) returning codigo");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, tipoContatoVO.getDescricao());
				ps.setString(2, tipoContatoVO.getSituacao().name());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()){
					tipoContatoVO.setNovoObj(false);
					return arg0.getInt("codigo");					
				}
				return null;
			}
		}));
		}catch(Exception e){
			if(e.getMessage().contains("tipocontato_descricao_key")){
				throw new Exception("Já existe um TIPO DE CONTATO cadastrado com esta DESCRIÇÃO.");
			}
			throw e;
		}
		
	}
	
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	private void alterar(final TipoContatoVO tipoContatoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception{
		alterar(idEntidade, controlarAcesso, usuarioVO);
		validarDados(tipoContatoVO);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql  = new StringBuilder("UPDATE TipoContato SET descricao = ?, situacao = ? where codigo = ? ");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setString(1, tipoContatoVO.getDescricao());
				ps.setString(2, tipoContatoVO.getSituacao().name());
				ps.setInt(3, tipoContatoVO.getCodigo());
				return ps;
			}
		}) == 0){
			incluir(tipoContatoVO, controlarAcesso, usuarioVO);
			return;
		}
	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void ativar(TipoContatoVO tipoContatoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try{
			tipoContatoVO.setSituacao(StatusAtivoInativoEnum.ATIVO);
			persistir(tipoContatoVO, controlarAcesso, usuarioVO);
		}catch(Exception e){
			tipoContatoVO.setSituacao(StatusAtivoInativoEnum.INATIVO);
			throw e;
		}

	}

	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void inativar(TipoContatoVO tipoContatoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try{
			tipoContatoVO.setSituacao(StatusAtivoInativoEnum.INATIVO);
			persistir(tipoContatoVO, controlarAcesso, usuarioVO);
		}catch(Exception e){
			tipoContatoVO.setSituacao(StatusAtivoInativoEnum.ATIVO);
			throw e;
		}

	}

	@Override
	public TipoContatoVO consultarPorChavePrimaria(Integer codigo) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM TipoContato WHERE codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return montarDados(rs);
		}
		return new TipoContatoVO();
	}
	
	private TipoContatoVO montarDados(SqlRowSet rs) throws Exception{
		TipoContatoVO obj = new TipoContatoVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.setDescricao(rs.getString("descricao"));
		obj.setSituacao(StatusAtivoInativoEnum.valueOf(rs.getString("situacao")));
		return obj;
	}
	
	private List<TipoContatoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<TipoContatoVO> tipoContatoVOs = new ArrayList<TipoContatoVO>(0);
		while(rs.next()){
			tipoContatoVOs.add(montarDados(rs));
		}
		return tipoContatoVOs;
	}

	@Override
	public List<TipoContatoVO> consultar(String descricao, StatusAtivoInativoEnum situacao, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM TipoContato WHERE ");
		sql.append(" sem_acentos(upper(descricao)) like sem_acentos(upper('").append(descricao).append("%'))");
		if(situacao!= null){
			sql.append(" and situacao = '").append(situacao.name()).append("' ");
		}
		sql.append(" order by descricao ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs);
	}

}
