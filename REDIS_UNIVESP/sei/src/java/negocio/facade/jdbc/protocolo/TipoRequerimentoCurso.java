package negocio.facade.jdbc.protocolo;

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

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoTransferenciaInternaCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoTurmaVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.TipoRequerimentoCursoInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class TipoRequerimentoCurso extends ControleAcesso implements TipoRequerimentoCursoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TipoRequerimentoCurso() throws Exception {
		super();
		setIdEntidade("TipoRequerimentoCurso");
	}

	public TipoRequerimentoCursoVO novo() throws Exception {
		TipoRequerimentoCurso.incluir(getIdEntidade());
		TipoRequerimentoCursoVO obj = new TipoRequerimentoCursoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoRequerimentoCursoVO obj, UsuarioVO usuarioVO) throws Exception {
 		final StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO tiporequerimentocurso (tiporequerimento, curso ) values (?,?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				sqlInserir.setInt(1, obj.getTipoRequerimento());
				sqlInserir.setInt(2, obj.getCursoVO().getCodigo());
				return sqlInserir;
			}
		}, new ResultSetExtractor() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		getFacadeFactory().getTipoRequerimentoTurmaFacade().incluirTipoRequerimentoTurmaVOs(obj.getCodigo(), obj.getListaTipoRequerimentoTurmaVOs(), usuarioVO);
		getFacadeFactory().getTipoRequerimentoCursoTransferenciaFacade().incluirTipoRequerimentoCursoTransferenciaInternaCursoVOs(obj.getCodigo(), obj.getListaTipoRequerimentoTransferenciaCursoVOs(), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoRequerimentoCursoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE TipoRequerimentoCurso SET tiporequerimento=?,curso=? ");
			sql.append(" WHERE (codigo = ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getTipoRequerimento());
					sqlAlterar.setInt(2, obj.getCursoVO().getCodigo());
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getTipoRequerimentoTurmaFacade().alterarTipoRequerimentoTurmaVOs(obj.getCodigo(), obj.getListaTipoRequerimentoTurmaVOs(), usuarioVO);
			getFacadeFactory().getTipoRequerimentoCursoTransferenciaFacade().alterarTipoRequerimentoCursoTransferenciaInternaCursoVOs(obj.getCodigo(), obj.getListaTipoRequerimentoTransferenciaCursoVOs(), usuarioVO);

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTipoRequerimentoCursoVOs(Integer tipoRequerimento, List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM tipoRequerimentoCurso WHERE (tiporequerimento = " + tipoRequerimento);
			sql.append(") and codigo not in(");
			for (TipoRequerimentoCursoVO obj : tipoRequerimentoCursoVOs) {
				sql.append(obj.getCodigo()).append(", ");
			}
			sql.append("0) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirTipoRequerimentoCursoVOs(Integer tipoRequerimento, List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			for (TipoRequerimentoCursoVO obj : tipoRequerimentoCursoVOs) {
				obj.setTipoRequerimento(tipoRequerimento);
				if (obj.getNovoObj()) {
					incluir(obj, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTipoRequerimentoCursoVOs(Integer tipoRequerimento, List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			for (TipoRequerimentoCursoVO tipoRequerimentoCursoVO : tipoRequerimentoCursoVOs) {
				getFacadeFactory().getTipoRequerimentoTurmaFacade().excluirTipoRequerimentoTurmaVOs(tipoRequerimentoCursoVO.getCodigo(), tipoRequerimentoCursoVO.getListaTipoRequerimentoTurmaVOs(), usuarioVO);
				getFacadeFactory().getTipoRequerimentoCursoTransferenciaFacade().excluirTipoRequerimentoCursoTransferenciaInternaCursoVOs(tipoRequerimentoCursoVO.getCodigo(), tipoRequerimentoCursoVO.getListaTipoRequerimentoTransferenciaCursoVOs(), usuarioVO);
			}
			this.excluirTipoRequerimentoCursoVOs(tipoRequerimento, tipoRequerimentoCursoVOs, usuarioVO);
			for (TipoRequerimentoCursoVO obj : tipoRequerimentoCursoVOs) {
				obj.setTipoRequerimento(tipoRequerimento);
				if (obj.getNovoObj()) {
					incluir(obj, usuarioVO);
				} else {
					alterar(obj, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static List<TipoRequerimentoCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<TipoRequerimentoCursoVO> vetResultado = new ArrayList<TipoRequerimentoCursoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}
	
	public static TipoRequerimentoCursoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		TipoRequerimentoCursoVO obj = new TipoRequerimentoCursoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTipoRequerimento(new Integer(dadosSQL.getInt("tipoRequerimento")));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso"));
		if(Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())){
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
		}
		obj.setListaTipoRequerimentoTurmaVOs(getFacadeFactory().getTipoRequerimentoTurmaFacade().consultarTipoRequerimentoTurmaPorTipoRequerimentoCurso(obj.getCodigo(), usuario));
		obj.setListaTipoRequerimentoTransferenciaCursoVOs(getFacadeFactory().getTipoRequerimentoCursoTransferenciaFacade().consultarTipoRequerimentoCursoTransferenciaInternaCursoPorTipoRequerimentoCurso(obj.getCodigo(), usuario));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	@Override
	public List<TipoRequerimentoCursoVO> consultarTipoRequerimentoCursoPorTipoRequerimento(Integer tipoRequerimento,UsuarioVO usuarioVO) throws Exception{
		StringBuilder sql = new StringBuilder();
		sql.append("Select * from tiporequerimentocurso WHERE tipoRequerimento = ? ORDER BY codigo");		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), tipoRequerimento);
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TipoRequerimentoCurso.idEntidade = idEntidade;
	}

	@Override
	public void removerTipoRequerimentoTurma(List<TipoRequerimentoCursoVO> listaTipoRequerimentoCursoVOs, TurmaVO turmaVO, UsuarioVO usuarioVO) {
		
		for (TipoRequerimentoCursoVO tipoRequerimentoCursoVO : listaTipoRequerimentoCursoVOs) {
			int index = 0;
			for (TipoRequerimentoTurmaVO tipoRequerimentoTurmaVO : tipoRequerimentoCursoVO.getListaTipoRequerimentoTurmaVOs()) {
				if (tipoRequerimentoTurmaVO.getTurmaVO().getCodigo().equals(turmaVO.getCodigo())) {
					tipoRequerimentoCursoVO.getListaTipoRequerimentoTurmaVOs().remove(index);
					return;
				}
				index++;
			}
		}
	}
	
	@Override
	public void adicionarTipoRequerimentoTurma(List<TipoRequerimentoCursoVO> listaTipoRequerimentoCursoVOs, TipoRequerimentoTurmaVO tipoRequerimentoTurmaIncluirVO, UsuarioVO usuarioVO) {
		
		for (TipoRequerimentoCursoVO tipoRequerimentoCursoVO : listaTipoRequerimentoCursoVOs) {
			if (tipoRequerimentoCursoVO.getCursoVO().getCodigo().equals(tipoRequerimentoTurmaIncluirVO.getTipoRequerimentoCursoVO().getCursoVO().getCodigo())) {
				int index = 0;
				for (TipoRequerimentoTurmaVO tipoRequerimentoTurmaVO : tipoRequerimentoCursoVO.getListaTipoRequerimentoTurmaVOs()) {
					if (tipoRequerimentoTurmaVO.getTurmaVO().getCodigo().equals(tipoRequerimentoTurmaIncluirVO.getTurmaVO().getCodigo())) {
						tipoRequerimentoCursoVO.getListaTipoRequerimentoTurmaVOs().set(index, tipoRequerimentoTurmaIncluirVO);
						return;
					}
					index++;
				}
				tipoRequerimentoCursoVO.getListaTipoRequerimentoTurmaVOs().add(tipoRequerimentoTurmaIncluirVO);
			}
			
		}
	}
	
	
	
	 @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void adicionarTipoRequerimentoTransferenciaInternaCursoVOs(List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoTransferenciaInternaCursoVOs, TipoRequerimentoCursoTransferenciaInternaCursoVO obj) {
    	if (obj.getCursoVO().getCodigo() == 0) {
    		throw new StreamSeiException("O curso (Tipo Requerimento Curso) deve ser informado");
    	}
    	for (TipoRequerimentoCursoTransferenciaInternaCursoVO tipoRequerimentoTransferenciaInternaCursoVO : tipoRequerimentoTransferenciaInternaCursoVOs) {
    		if (tipoRequerimentoTransferenciaInternaCursoVO.getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo())) {
    			throw new StreamSeiException("Este curso já esta na lista !");
    		}
    		
    	}
    	tipoRequerimentoTransferenciaInternaCursoVOs.add(obj);
    }
    
    @Override
    public void removerTipoRequerimentoTransferenciaInternaCursoVOs(List<TipoRequerimentoCursoTransferenciaInternaCursoVO> tipoRequerimentoTransferenciaInternaCursoVOs, TipoRequerimentoCursoTransferenciaInternaCursoVO obj) {
    	for (TipoRequerimentoCursoTransferenciaInternaCursoVO tipoRequerimentoTransferenciaInternaCursoVO : tipoRequerimentoTransferenciaInternaCursoVOs) {
    		if (tipoRequerimentoTransferenciaInternaCursoVO.getCursoVO().getCodigo().equals(obj.getCursoVO().getCodigo())) {
    			tipoRequerimentoTransferenciaInternaCursoVOs.remove(tipoRequerimentoTransferenciaInternaCursoVO);
    			return;
    		}
    	}
    }
}
